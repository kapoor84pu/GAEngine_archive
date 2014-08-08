package uk.co.metoffice.service;

import com.google.appengine.tools.cloudstorage.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.ResponseParameter;
import uk.co.metoffice.beans.RequestParameter;

import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

public class GCSPersistenceService {
	private final static Logger logger = LoggerFactory.getLogger(GCSPersistenceService.class);
	
	
	public static final String ENDPOINT = "gcs";
	public static final String ENDPOINT_BASE = "/gcs/";
	public static final String BUCKET = "retailportal-dev.appspot.com";
//	public static final boolean SERVE_USING_BLOBSTORE_API = false;
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
  	 * This method persists Base64 encoded data into Google cloud service buckets as
  	 * --
  	 * @param fileKey
  	 * @param encodedData
  	 * @return ResponseParameter
  	 */
  	public ResponseParameter uploadDocumentStream(String fileKey, String encodedData){
    	GcsOutputChannel outputChannel;
  		ResponseParameter responseParameter = new ResponseParameter();
		try {
	        logger.info("Putting for the file : " + ENDPOINT_BASE + BUCKET + "/" + fileKey);
			
	        //open output channel to write object using GcsFileOptions, a container class for creating Google storage files
	        outputChannel = gcsService.createOrReplace(getFileName(ENDPOINT_BASE + BUCKET + "/" + fileKey),  GcsFileOptions.getDefaultInstance());
	        //convert String into byte stream 
	        InputStream stream = new ByteArrayInputStream(encodedData.getBytes(StandardCharsets.UTF_8));
	        // write it in bucket
	        copy(stream, Channels.newOutputStream(outputChannel));
          responseParameter = new ResponseParameter.ResponseParameterBuilder().setFileKey(fileKey).build();
		} catch (Exception e) {
			logger.error("error during writing encoded data in bucket", e);
		}
		
		return responseParameter;
  	}
  	
  	/**
  	 * This method takes filename and search into bucket and returns a input stream
  	 * @param request
  	 * @return ResponseParameter.setStream()
  	 */
	
	public ResponseParameter getDocumentStream(RequestParameter request){

		String outputString;
		GcsInputChannel readChannel;
		ResponseParameter responseParameter = new ResponseParameter();
		
		String filename = request.getFilename();
		logger.info("getting for the file : " + ENDPOINT_BASE + BUCKET + "/" + filename);
		GcsFilename fileName = getFileName(ENDPOINT_BASE + BUCKET + "/" + filename.toLowerCase());

		try {
			readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
			outputString = IOUtils.toString(Channels.newInputStream(readChannel), "UTF-8");
			
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputString.getBytes());
		    InputStream is = MimeUtility.decode(byteArrayInputStream, "base64");
        responseParameter = new ResponseParameter.ResponseParameterBuilder().setInputStream(is).build();
		   // responseParameter.setStream(b64is);
						
		} catch (Exception e) {
			logger.error("Error while fetching" + filename + "from GCS", e);
		}
		return responseParameter;
	}
	
	
	/**
	 * creates a filename for each entry in bucket
	 * @param req
	 * @return GcsFilename
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
