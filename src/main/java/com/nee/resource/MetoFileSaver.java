package com.nee.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXB;

import org.apache.commons.io.IOUtils;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.nee.JPA.EMFService;
import com.nee.beans.IngestionConfiguration;
import com.nee.beans.MetaData;
import com.nee.utils.InputStreamToStringConvertor;
import com.nee.utils.MetoHelper;

//import org.apache.commons.codec.binary.Base64;
/**
 * A simple resource that proxies reads and writes to its Google Cloud Storage bucket.
 */
@Path("/gcs")
public class MetoFileSaver{
 
	public static final String ENDPOINT = "gcs";
	public static final String ENDPOINT_BASE = "/gcs/";
	public static final String BUCKET = "retailportal-dev.appspot.com";
	public static final boolean SERVE_USING_BLOBSTORE_API = false;
	private static final int BUFFER_SIZE = 2 * 1024 * 1024; //2Mb
  
  /**
   * This is where backoff parameters are configured. Here it is aggressively retrying with
   * backoff, up to 10 times but taking no more that 15 seconds total to do so.
   */
  private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
      .initialRetryDelayMillis(10)
      .retryMaxAttempts(10)
      .totalRetryPeriodMillis(15000)
      .build());

  /**
   * Retrieves a file from GCS and returns it in the http response.
   * If a get comes in for request path /gcs/Foo/Bar this will be interpreted as
   * a request to read the GCS file named Bar in the bucket Foo.
 * @throws MessagingException 
   */
	@GET
    @Path("get/{filename}")
	@Produces("application/pdf")
	public Response doGet(@PathParam("filename") String filename) throws MessagingException {

		ResponseBuilder response = null;
		String outputString = null;
		GcsInputChannel readChannel = null;

		System.out.println("getting for the file : " + ENDPOINT_BASE + BUCKET + "/" + filename);
		GcsFilename fileName = getFileName(ENDPOINT_BASE + BUCKET + "/" + filename.toLowerCase());

		try {
			System.out.println("All listing :: " + getMetadataListing());
			readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
			outputString = IOUtils.toString(Channels.newInputStream(readChannel), "UTF-8");
			
			ByteArrayInputStream bais = new ByteArrayInputStream(outputString.getBytes());
		    InputStream b64is = MimeUtility.decode(bais, "base64");
			
			response = Response.ok((Object)b64is);
			response.header("Content-Disposition", "attachment; filename=" + filename+ ".pdf");	
						
			//respString = "OK :: Payload starts with > " + outputString.substring(0, 20) + ", Length:" + outputString.length();
		} catch (IOException e) {
			//respString = "ERROR :: Unable to retrieve file for " + filename;
			e.printStackTrace();
		}
		return response.build();
		//return respString;
  }


	/**
	 * ClientId= 1000001, fileType=PDF, category=MARKS_AND_SPENCER_REGIONAL_6_SHORT, ValidityDate=2014-07-16T09:46:30Z
	 * 
	 * Key : <ClientId>::<category>::<ValidityDate>::<fileType>
	 * Sample : 1000001::marks_and_spencer_regional_6_short::2014-07-16::pdf
	 * 
	 * @param metoFile
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
    @POST
    @Path("/upload/xml")
    @Consumes(MediaType.APPLICATION_XML)
    public Response uploadFile(IngestionConfiguration metoFile) throws IOException, MessagingException{
    	MetoAdd metAdd = new MetoAdd();
    	ResponseBuilder builder = null;
    	String fileKey = null;
    	MetaData metaData = null;
    	GcsOutputChannel outputChannel = null;
        System.out.println("Invoked method" + metoFile);

        try{
        	String encodedData = metoFile.getIngestionEntries().getIngestionEntry().get(0).getBlobData();
        	metaData = generateMetadata(metoFile);
	        
        	if(metaData.getFileType().equalsIgnoreCase("CSV")){
        		ByteArrayInputStream bais = new ByteArrayInputStream(encodedData.getBytes());
		    	InputStream b64is = MimeUtility.decode(bais, "base64");
		    	
		    	System.out.println("printing input stream" + b64is);
		    	String str = InputStreamToStringConvertor.getStringFromInputStream(b64is);
		    	metAdd.postOnlyCSV(str);
		    	builder = Response.status(200).entity("Uploaded : " + metaData.getFileType());
        	}
        	else if(metaData.getFileType().equalsIgnoreCase("PDF")){
        		
	        	fileKey = getKey(metaData);
	        
		        // Persist metadata object for client listing
		        metaData.setId(fileKey);
		        
		        persistMetadata(metaData);
	
		        System.out.println("Putting for the file : " + ENDPOINT_BASE + BUCKET + "/" + fileKey);
	
		        outputChannel = gcsService.createOrReplace(getFileName(ENDPOINT_BASE + BUCKET + "/" + fileKey),  GcsFileOptions.getDefaultInstance());
		        InputStream stream = new ByteArrayInputStream(encodedData.getBytes(StandardCharsets.UTF_8));
		        copy(stream, Channels.newOutputStream(outputChannel));
	
		        builder = Response.status(200).entity("Uploaded file name : " + fileKey);
	        } 
        	else{
        		System.out.println("Doesn't accept " + metaData.getFileType() + "file Format");
        	}
        }catch (IOException e) {
			e.printStackTrace();
			builder = Response.status(500).entity("Unable to upload file for  : " + fileKey);
		}
	        	
	    return builder.build();
    }

    /**
     * 
     */
    private void persistMetadata(MetaData metaData){
    	EntityManager em = EMFService.get().createEntityManager();
		try {					
			em.persist(metaData);
			System.out.println("stored entity id : " + em.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			em.close();
		}
    }
    
    /**
     * 
     */
    private List<MetaData> getMetadataListing(){
    	List<MetaData> list = new ArrayList<MetaData>();
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT p FROM MetaData p");
			
			System.out.println("printing query" + query.toString());
			list =  query.getResultList();
			
			System.out.println("+++++++++++++++++" + list.toString());
		}finally{
			em.close();
		}
		return list;
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
	 * 
	 * @param req
	 * @return
	 */
	private GcsFilename getFileName(String req) {
		String[] splits = req.split("/", 4);
		if (!splits[0].equals("") || !splits[1].equals(ENDPOINT)) {
			throw new IllegalArgumentException("The URL is not formed as expected. " +
			"Expecting " + ENDPOINT_BASE + BUCKET + "/<object>");
		}
		return new GcsFilename(splits[2], splits[3]);
	}

	/**
	 * 
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
       System.out.println("The key generated is " + key);
       return key;
       
	}
	
  /**
   * Transfer the data from the inputStream to the outputStream. Then close both streams.
   */
	private void copy(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) { 
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			output.close();
		}
	}
}
