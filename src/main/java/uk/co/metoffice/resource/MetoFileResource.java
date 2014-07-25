package uk.co.metoffice.resource;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;
import uk.co.metoffice.beans.xml.IngestionConfiguration;
import uk.co.metoffice.business.MetoBusiness;

/**
 * A simple resource that reads from and writes to Google Cloud Storage bucket.
 */
@Path("/gcs")
public class MetoFileResource{
	private final static Logger logger = LoggerFactory.getLogger(MetoFileResource.class);
 
	private MetoBusiness business = new MetoBusiness();
	
  /**
   * Retrieves a file from GCS and returns it in the http response.
   * If a get comes in for request path /gcs/bucke/filename this will be interpreted as
   * a request to read the GCS file named filename in the bucket bucke.
   * @throws MessagingException 
   */
	@GET
    @Path("get/{filename}")
	@Produces("application/pdf")
	public Response doGet(@PathParam("filename") String filename) throws MessagingException {
		ResponseBuilder builder = null;
		MetoRequest request = new MetoRequest();
		MetoResponse metoResponse = new MetoResponse();
		logger.info("looking for " + filename + " in Google Cloud Storage bucket");
		
		try{
			request.setFilename(filename);
			metoResponse = business.getDocumentStream(request);
			
			if (metoResponse != null && metoResponse.getStream() != null){
				builder = Response.ok((Object)metoResponse.getStream());
				builder.header("Content-Disposition", "attachment; filename=" + filename+ ".pdf");	
			}else{
				//TODO : Send error
			}
		} catch (Exception e) {
			logger.error("could not find " + filename);
			e.printStackTrace();
		}
		return builder.build();
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
		ResponseBuilder builder = null;
		MetoRequest request = new MetoRequest();
		MetoResponse metoResponse = new MetoResponse();
		
		try{
			request.setIngestionConfiguration(metoFile);
			metoResponse = business.injestDocument(request);
			
			if (metoResponse != null && metoResponse.getFilekey() != null){
				builder = Response.status(200).entity("Uploaded file name : " + metoResponse.getFilekey());
			}else{
				//TODO : Send error
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.build();
    }

}
