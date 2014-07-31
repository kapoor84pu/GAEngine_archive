package uk.co.metoffice.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.MetoDataJPA;
import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;
import uk.co.metoffice.beans.xml.IngestionConfiguration;
import uk.co.metoffice.service.GCSPersistenceService;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.StreamProcessor;

/**
 * This class lies between service classes and resource classes. Get the parameters from resource class
 * and ask service class to process and give the results back.
 * @author neelam.kapoor
 *
 */
public class MetoBusiness {	
	private final static Logger logger = LoggerFactory.getLogger(MetoBusiness.class);

	/**
	 * Take fromDate, toDate and clientId, and give back a list of products/pdf between two dates.
	 * @param MetoRequest (fromDate, toDate and clientId)
	 * @return MetoResponse
	 */
	public MetoResponse getHistoProduct(MetoRequest request){
		List<MetaData> list = null;
		MetoResponse metoResponse = new MetoResponse();

		try {
			//TODO investigate about date conversion error
			Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate());
			Date dateTo = AppHelper.convertStringIntoDate(request.getToDate());
			
			String clientId = request.getClientId();
			
			logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo + clientId);
			
			list = JPAPersistenceService.INSTANCE.getProductBetweenDates(dateFrom, dateTo, clientId);
			
			logger.info("inside metoget resource and list from database is" + list.toString());
			metoResponse.setMetaDataList(list);

		} catch (Exception ex) {
			logger.error("error in getHistoProduct class", ex);
		}
		return metoResponse;
	}
	
		/**
		 * 
		 * Take fromDate, toDate and clientId, and give back a list of plain/csv data between two dates.
		 * @param MetoRequest (fromDate, toDate and clientId)
		 * @return MetoResponse
		 */
		public MetoResponse getHistoData(MetoRequest request){
			List<MetoDataJPA> list = null;
			InputStream is = null;
			MetoResponse metoResponse = new MetoResponse();

			try {
				String[] region = createStringArray(request.getRegion());
				Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate(), "yyyyMMdd");
				Date dateTo = AppHelper.convertStringIntoDate(request.getToDate(), "yyyyMMdd");
				String clientId = request.getClientId();

				list = JPAPersistenceService.INSTANCE.getWeatherBetweenDates(dateFrom, dateTo, region,clientId);
				
				logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo);
				logger.debug("inside metoget resource and list from database is" + list.toString());
				//convert list into String
				String str = createString(list);
				is = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
				metoResponse.setStream(is);

			} catch (Exception ex) {
				logger.error("", ex);
			}
			return metoResponse;
		}
		
		
		/**
		 * This method receives a request and extracts file from it as request.getFile().
		 * According to file type send them to persistence service class. So if it's
		 * CSV then JPA Persistence Service or if its PDF then GCS Persistence Service
		 * @param MetoRequest
		 * @return MetoResponse object
		 */
		public MetoResponse injestDocument(MetoRequest request){
	    	String fileKey = null;
	    	MetaData metaData = null;
	    	MetoResponse metoResponse = null;
	        IngestionConfiguration metoFile = request.getIngestionConfiguration();
	        logger.info("invoking injestDocument method");
	        
	        try{
	        	//receive blob data and metadata from Ingestion configuration XML file
	        	String encodedData = metoFile.getIngestionEntries().getIngestionEntry().get(0).getBlobData();
	        	metaData = generateMetadata(metoFile);
		        
	        	if(metaData.getFileType().equalsIgnoreCase("CSV")){
	        		//if file type is CSV decode it and sent to postOnly CSV
	        		ByteArrayInputStream bais = new ByteArrayInputStream(encodedData.getBytes());
			    	InputStream b64is = MimeUtility.decode(bais, "base64");
			    	
			    	String str = StreamProcessor.getStringFromInputStream(b64is);
			    	metoResponse = postOnlyCSV(str,metaData.getClientId());
			    	metoResponse.setFilekey(metaData.getFileType());
	        	}
	        	else if(metaData.getFileType().equalsIgnoreCase("PDF")){
	 	        	fileKey = getKey(metaData);
		        
			        // Persist metadata object for pdf/product listing
			        metaData.setId(fileKey);
			        JPAPersistenceService.INSTANCE.persistMetadata(metaData);
			        metoResponse = GCSPersistenceService.INSTANCE.uploadDocumentStream(fileKey, encodedData);
		        } 
	        	else{
	        		logger.warn("Doesn't accept " + metaData.getFileType() + "file Format");
	        	}
	        }catch (Exception e) {
	        	logger.error("Exception during injesting file into Google store" , e);

			}
		    return metoResponse;
		}

		/**
		 * take the request with set file name and forward to GCSPersistenceService
		 * to read the file in bucket
		 * @param request
		 * @return stream of pdf file set in MetoResponse object.
		 */
		public MetoResponse getDocumentStream(MetoRequest request){
			return GCSPersistenceService.INSTANCE.getDocumentStream(request);
		}
	
	
		/**
		 * This method receives a String call createList method to break it into MetoDataJPA object
		 * and forward it to JPA Persistence Service to persist in google store
		 * @param String
		 * @return MetoResponse object
		 */
	   public MetoResponse postOnlyCSV(String incomingCSV, String clientID) { 
			
			logger.info("inside postOnlyCSV method");
			MetoResponse metoResponse = null;
			List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
	        try {
				logger.info("incomingCSV :" + incomingCSV);  
				metoDataList = createList(incomingCSV,clientID);
				metoResponse = JPAPersistenceService.INSTANCE.add(metoDataList);
			} catch (Exception e) {
				logger.error("error in persisting csv", e);
				e.printStackTrace();
			}
	        return metoResponse; 
	    } 
	   
		
		/**
		 * This method converts a String into MetoDataJPA objects
		 * @param incomingString that is similar to 
		 * 		01072014,LON,1.0,2.0,3.0
		 * 		01072014,SE_ENG,1.0,2.0,3.0
		 * 		01072014,SE_ENG,1.0,2.0,3.0
		 * @return list of MetoDataJPA objects.
		 */
		private List<MetoDataJPA> createList(String incomingString, String clientId){
			String receivedString = incomingString.replaceAll("\\s+","\n");
			logger.info("inside createList method");
			List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
			
			MetoDataJPA tmpMetoData = null;
			String[] line = receivedString.split("\n");
			for(String temp:line){
				String[] split = temp.split(",");
				Date date = AppHelper.convertStringIntoDate(split[0], "ddMMyyyy");
				tmpMetoData = new MetoDataJPA(split[0] + ":" + split[1]+":" + clientId,date, split[1], split[2], split[3],clientId);
				metoDataList.add(tmpMetoData);
				logger.info("adding csv data to list");
			}
			return metoDataList;
		}
		
		
	    /**
	     * convert XML file into java objects
	     * @param metoFile
	     * @return MetaData objects
	     * @throws MessagingException
	     */
		private MetaData generateMetadata(IngestionConfiguration metoFile)
				throws MessagingException {
			
			String encodedMetaData = metoFile.getIngestionEntries().getIngestionEntry().get(0).getMetaData();
			ByteArrayInputStream bais = new ByteArrayInputStream(encodedMetaData.getBytes());
			InputStream b64is = MimeUtility.decode(bais, "base64");
			   
			//converting bytestream into metadata object
			MetaData metaData = JAXB.unmarshal(b64is, MetaData.class);
	        logger.debug("MataData XML" + metaData);
	 
			return metaData;
		}	
		
		
		/**
		 * ceate a unique key for product/pdf emtadata so that later can retrive data from bucket 
		 * using this key.
		 * @param MetaData
		 * @return String (key for mataData in google store)
		 */
		private String getKey(MetaData metaData){
			String separator = "::";
			String key = null;
			
	       String clientId = metaData.getClientId();
	       String fileType = metaData.getFileType();
	       String category = metaData.getCategory();
	       
	      // String validDate = (metaData.getValidityDate().split("T"))[0];
	       Date date = metaData.getValidityDate();
	       
	       String  validDate = AppHelper.convertDateIntoString(date);
		   
	       key = (clientId + separator + category + separator + validDate + separator + fileType).toLowerCase();
	       logger.info("The key generated is " + key);
	       return key;
		}

		
		/**
		 * create array of string from a string
		 * @param incomingString
		 * @return
		 */
		private String[] createStringArray(String incomingString) {
			String[] regions = incomingString.split("-");
			return regions;
		}
	
		
		/**
		 * Convert a list into String
		 * @param list
		 * @return
		 */
		private String createString(List<MetoDataJPA> list) {
			StringBuffer buff = new StringBuffer();
			logger.debug("The list is : " + list);
			for (MetoDataJPA obj : list) {
				logger.info("Adding " + obj);
				buff.append(obj.getStringRepresentation()).append(
						System.lineSeparator());
			}
			logger.error("The final string is : " + buff.toString());
			return buff.toString();
		}
}
