package uk.co.metoffice.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.WeatherData;
import uk.co.metoffice.beans.ResponseParameter;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.beans.xml.IngestionConfiguration;
import uk.co.metoffice.service.GCSPersistenceService;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.StreamProcessor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class lies between service and resource. It takes parameters from resource class
 * and ask service class to process and give the results back.
 * @author neelam.kapoor
 *
 */
public class MetoBusiness {	
	private final static Logger logger = LoggerFactory.getLogger(MetoBusiness.class);
  JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
  GCSPersistenceService gcsPersistenceService = new GCSPersistenceService();

	/**
	 * Take fromDate, toDate and clientId, and give back a list of products/pdf between two dates.
	 * @param request (fromDate, toDate and clientId)
	 * @return ResponseParameter
	 */
	public ResponseParameter getHistoricalProduct(RequestParameter request){
		List<MetaData> list;
		ResponseParameter responseParameter = new ResponseParameter();

		try {
			//TODO use Date util class instead of AppHelper
			Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate());
			Date dateTo = AppHelper.convertStringIntoDate(request.getToDate());
			
			String clientId = request.getClientId();
			
			logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo + clientId);
			
			list = jpaPersistenceService.getProductBetweenDates(dateFrom, dateTo, clientId);
			
			logger.debug("list of historical products is" + list.toString());
			responseParameter = new ResponseParameter.ResponseParameterBuilder().setMetaDataList(list).build();

		} catch (Exception ex) {
			logger.error("error in getHistoricalProduct class", ex);
		}
		return responseParameter;
	}
	
		/**
		 * 
		 * Take fromDate, toDate and clientId, and give back a list of plain/csv data between two dates.
		 * @param request (fromDate, toDate and clientId)
		 * @return ResponseParameter
		 */
		public ResponseParameter getHistoricalData(RequestParameter request){
			List<WeatherData> list;
			InputStream is;
			ResponseParameter responseParameter = new ResponseParameter();


			try {
				String[] region = createStringArray(request.getRegion());
				Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate(), "yyyyMMdd");
				Date dateTo = AppHelper.convertStringIntoDate(request.getToDate(), "yyyyMMdd");
				String clientId = request.getClientId();

				list = jpaPersistenceService.getWeatherDataBetweenDates(dateFrom, dateTo, Arrays.asList(region), clientId);
				
				logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo);
				logger.debug("list of entries between two dates is" + list.toString());
				//convert list into String
				String str = createString(list);
				is = new ByteArrayInputStream(str.getBytes("UTF-8"));
        responseParameter = new ResponseParameter.ResponseParameterBuilder().setInputStream(is).build();

			} catch (Exception ex) {
				logger.error("", ex);
			}
			return responseParameter;
		}
		
		
		/**
		 * This method receives a request and extracts file from it and according
		 * to file type send them to persistence service class. 
		 * @param request (includes a IngestionConfiguration file)
		 * @return ResponseParameter object
		 */
    public ResponseParameter ingestDocument(RequestParameter request){
      String fileKey;
      MetaData metaData;
      ResponseParameter responseParameter = null;
      IngestionConfiguration file = request.getIngestionConfiguration();
      logger.info("invoking ingestDocument method");

        try{
          //receive blob data and metadata from Ingestion configuration XML file
          String encodedData = file.getIngestionEntries().getIngestionEntry().get(0).getBlobData();
          metaData = generateMetadata(file);

          if(metaData.getFileType().equalsIgnoreCase("CSV")){
            //if file type is CSV decode it and sent to postOnly CSV
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedData.getBytes());
            InputStream b64is = MimeUtility.decode(byteArrayInputStream, "base64");

            String str = StreamProcessor.getStringFromInputStream(b64is);
            responseParameter = persistCSVData(str, metaData.getClientId());
            responseParameter = new ResponseParameter.ResponseParameterBuilder().setFileKey(metaData.getFileType()).build();
          }
          else if(metaData.getFileType().equalsIgnoreCase("PDF")){
            fileKey = getKey(metaData);

            // Persist metadata object for pdf/product listing
            metaData.setId(fileKey);
            jpaPersistenceService.persistMetadata(metaData);
            responseParameter = gcsPersistenceService.uploadDocumentStream(fileKey, encodedData);
          }
          else{
           logger.warn("Doesn't accept " + metaData.getFileType() + "file Format");
          }
      }catch (Exception e) {
        logger.error("Exception during ingesting file into Google store" , e);

      }
      return responseParameter;
    }

		/**
		 * take the request with set file name and forward to GCSPersistenceService
		 * to read the file in bucket
		 * @param request
		 * @return stream of pdf file set in ResponseParameter object.
		 */
		public ResponseParameter getDocumentStream(RequestParameter request){
			return gcsPersistenceService.getDocumentStream(request);
		}
	
	
		/**
		 * This method receives a CSV data as a String and creates a list of WeatherData entities and
     * ask persistence service to store them.
		 * @return ResponseParameter object
		 */
	   public ResponseParameter persistCSVData(String incomingCSV, String clientID) {
			
			logger.info("inside persistCSVData method");
			ResponseParameter responseParameter = null;
			List<WeatherData> dataList;
	        try {
				logger.info("incomingCSV :" + incomingCSV);  
				dataList = createList(incomingCSV,clientID);
				responseParameter = jpaPersistenceService.persistWeatherData(dataList);
			} catch (Exception e) {
				logger.error("error in persisting csv", e);
				e.printStackTrace();
			}
	        return responseParameter;
	    } 
	   
		
		/**
		 * This method converts a String into WeatherData objects
		 * @param incomingString that is similar to 
		 * 		01072014,LON,1.0,2.0,3.0
		 * 		01072014,SE_ENG,1.0,2.0,3.0
		 * 		01072014,SE_ENG,1.0,2.0,3.0
		 * @return list of WeatherData objects.
		 */
		private List<WeatherData> createList(String incomingString, String clientId){
			String receivedString = incomingString.replaceAll("\\s+","\n");
			logger.info("inside createList method");
			List<WeatherData> list = new ArrayList<WeatherData>();
			
			WeatherData tempdata;
			String[] line = receivedString.split("\n");
			for(String temp:line){
				String[] split = temp.split(",");
				Date date = AppHelper.convertStringIntoDate(split[0], "ddMMyyyy");
				tempdata = new WeatherData(split[0] + ":" + split[1]+":" + clientId,date, split[1], split[2], split[3],clientId);
				list.add(tempdata);
				logger.info("adding csv data to list");
			}
			return list;
		}
		
		
	    /**
	     * convert XML file into java objects
	     * @param file
	     * @return MetaData objects
	     * @throws MessagingException
	     */
		private MetaData generateMetadata(IngestionConfiguration file)
				throws MessagingException {
			
			String encodedMetaData = file.getIngestionEntries().getIngestionEntry().get(0).getMetaData();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encodedMetaData.getBytes());
			InputStream b64is = MimeUtility.decode(byteArrayInputStream, "base64");
			   
			//converting byte stream into metadata object
			MetaData metaData = JAXB.unmarshal(b64is, MetaData.class);
	        logger.debug("MataData XML" + metaData);
	 
			return metaData;
		}	
		
		
		/**
		 * Create a unique key for product/pdf metadata so that later can retrieve data from bucket
		 * using this key.
		 * @param metaData (MetaData object)
		 * @return String (key for mataData in google store)
		 */
		private String getKey(MetaData metaData){
      MetaData metaData1 = new MetaData();
			String key;

	       String clientId = metaData.getClientId();
	       String fileType = metaData.getFileType();
	       String category = metaData.getCategory();

	       Date date = metaData.getValidityDate();
	       
	       String  validDate = AppHelper.convertDateIntoString(date);
		   
	       key = metaData1.createCompositeKey(clientId, category, validDate, fileType);
	       logger.info("The key generated is " + key);
	       return key;
		}

		
		/**
		 * create array of string from a string
		 */
		private String[] createStringArray(String incomingString) {
			String[] regions = incomingString.split("-");
			return regions;
		}
	
		
		/**
		 * Convert a list into String
		 */
		private String createString(List<WeatherData> list) {
			StringBuffer buff = new StringBuffer();
			logger.debug("The list is : " + list);
			for (WeatherData obj : list) {
				logger.info("Adding " + obj);
				buff.append(obj.getStringRepresentation()).append(
						System.lineSeparator());
			}
			logger.error("The final string is : " + buff.toString());
			return buff.toString();
		}
}
