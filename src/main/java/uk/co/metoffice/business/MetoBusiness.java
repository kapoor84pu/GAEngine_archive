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
import uk.co.metoffice.util.MetoHelper;
import uk.co.metoffice.util.StreamProcessor;

/**
 * 
 * @author neelam.kapoor
 *
 */
public class MetoBusiness {	
	private final static Logger logger = LoggerFactory.getLogger(MetoBusiness.class);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public MetoResponse getHistoProduct(MetoRequest request){
		List<MetaData> list = null;
		MetoResponse metoResponse = new MetoResponse();

		try {
			//TODO investigate about date conversion error
			Date dateFrom = MetoHelper.convertStringIntoDate(request.getFromDate(), "yyyy/MM/dd");
			Date dateTo = MetoHelper.convertStringIntoDate(request.getToDate(), "yyyy/MM/dd");
			
			System.out.println("printing fromDate and toDate after conversion" + dateFrom + dateTo);
			
			list = JPAPersistenceService.INSTANCE.getProductBetweenDates(dateFrom,dateTo);
			
			System.out.println("inside metoget resource and list from database is" + list.toString());
			metoResponse.setMetaDataList(list);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return metoResponse;
	}
	
		/**
		 * 
		 * @param request
		 * @return
		 */
		public MetoResponse getHistoData(MetoRequest request){
			List<MetoDataJPA> list = null;
			InputStream is = null;
			MetoResponse metoResponse = new MetoResponse();

			try {
				String[] region = createStringArray(request.getRegion());
				Date dateFrom = MetoHelper.convertStringIntoDate(request.getFromDate(), "yyyyMMdd");
				Date dateTo = MetoHelper.convertStringIntoDate(request.getToDate(), "yyyyMMdd");

				list = JPAPersistenceService.INSTANCE.getWeatherBetweenDates(dateFrom, dateTo, region);
				
				System.out.println("printing fromDate and toDate after conversion" + dateFrom + dateTo);
				System.out.println("inside metoget resource and list from database is" + list.toString());
				String str = createString(list);
				is = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
				metoResponse.setStream(is);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return metoResponse;
		}
		
		
		/**
		 * This method receives a request and extracts file as request.getFile().
		 * According to file type send them to persistence service class. So if it's
		 * CSV-JPA Persistence Service or if its PDF - GCS Persistence Service
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
			    	metoResponse = postOnlyCSV(str);
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
		 * 
		 * @param request
		 * @return
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
	   public MetoResponse postOnlyCSV(String incomingCSV) { 
			
			logger.info("inside postOnlyCSV method");
			MetoResponse metoResponse = null;
			List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
	        try {
				System.out.println("incomingCSV :" + incomingCSV);  
				metoDataList = createList(incomingCSV);
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
		private List<MetoDataJPA> createList(String incomingString){
			String receivedString = incomingString.replaceAll("\\s+","\n");
			logger.info("inside createList method");
			List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
			
			MetoDataJPA tmpMetoData = null;
			String[] line = receivedString.split("\n");
			for(String temp:line){
				String[] split = temp.split(",");
				Date date = MetoHelper.convertStringIntoDate(split[0], "ddMMyyyy");
				tmpMetoData = new MetoDataJPA(split[0] + ":" + split[1],date, split[1], split[2], split[3]);
				metoDataList.add(tmpMetoData);
				logger.info("adding csv data to list");
			}
			return metoDataList;
		}
		
		
	    /**
	     * 
	     */
		private MetaData generateMetadata(IngestionConfiguration metoFile)
				throws MessagingException {
			
			String encodedMetaData = metoFile.getIngestionEntries().getIngestionEntry().get(0).getMetaData();
			ByteArrayInputStream bais = new ByteArrayInputStream(encodedMetaData.getBytes());
			InputStream b64is = MimeUtility.decode(bais, "base64");
			   
			//converting bytestream into metadata object
			MetaData metaData = JAXB.unmarshal(b64is, MetaData.class);
	        System.out.println("MataData XML" + metaData);
	 
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
	       
	       String  validDate = MetoHelper.convertDateIntoString(date);
		   
	       key = (clientId + separator + category + separator + validDate + separator + fileType).toLowerCase();
	       logger.info("The key generated is " + key);
	       return key;
		}

		
		/**
		 * 
		 * @param incomingString
		 * @return
		 */
		private String[] createStringArray(String incomingString) {
			String[] regions = incomingString.split("-");
			return regions;
		}
	
		
		/**
		 * 
		 * @param list
		 * @return
		 */
		private String createString(List<MetoDataJPA> list) {
			StringBuffer buff = new StringBuffer();
			System.out.println("The list is : " + list);
			for (MetoDataJPA obj : list) {
				System.out.println("Adding " + obj);
				buff.append(obj.getStringRepresentation()).append(
						System.lineSeparator());
			}
			System.out.println("The final string is : " + buff.toString());
			return buff.toString();
		}
}
