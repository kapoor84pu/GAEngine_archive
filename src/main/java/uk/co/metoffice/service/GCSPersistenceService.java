package uk.co.metoffice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public enum GCSPersistenceService {
	INSTANCE;
	private final static Logger logger = LoggerFactory.getLogger(GCSPersistenceService.class);
	
	
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
  	 * This method persits Base64 encoded data into Google cloud service buckets as
  	 * --
  	 * @param filekey
  	 * @param encodedData
  	 * @return MetoResponse
  	 */
  	public MetoResponse uploadDocumentStream(String filekey, String encodedData){
    	GcsOutputChannel outputChannel = null;
  		MetoResponse metoResponse = new MetoResponse();
		try {
	        logger.info("Putting for the file : " + ENDPOINT_BASE + BUCKET + "/" + filekey);
			
	        //open output channel to write object using GcsFileOptions, a container class for creating Google storage files
	        outputChannel = gcsService.createOrReplace(getFileName(ENDPOINT_BASE + BUCKET + "/" + filekey),  GcsFileOptions.getDefaultInstance());
	        //convert String into byte stream and write it in bucket
	        InputStream stream = new ByteArrayInputStream(encodedData.getBytes(StandardCharsets.UTF_8));
	        copy(stream, Channels.newOutputStream(outputChannel));
	        metoResponse.setFilekey(filekey);
						
		} catch (Exception e) {
			logger.error("error during writing encoded data in buket", e);
		}
		
		return metoResponse;
  	}
	
	public MetoResponse getDocumentStream(MetoRequest request){

		String outputString = null;
		GcsInputChannel readChannel = null;
		MetoResponse metoResponse = new MetoResponse();
		
		String filename = request.getFilename();
		System.out.println("getting for the file : " + ENDPOINT_BASE + BUCKET + "/" + filename);
		GcsFilename fileName = getFileName(ENDPOINT_BASE + BUCKET + "/" + filename.toLowerCase());

		try {
			readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
			outputString = IOUtils.toString(Channels.newInputStream(readChannel), "UTF-8");
			
			ByteArrayInputStream bais = new ByteArrayInputStream(outputString.getBytes());
		    InputStream b64is = MimeUtility.decode(bais, "base64");
		    
		    metoResponse.setStream(b64is);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return metoResponse;
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
		// GcsFilename(bucketName,objectName)
		return new GcsFilename(splits[2], splits[3]);
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
