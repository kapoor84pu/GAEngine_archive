package uk.co.metoffice.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.ResponseParameter;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.beans.xml.IngestionConfiguration;
import uk.co.metoffice.business.MetoBusiness;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;

/**
 * This class download file and interact with business class that further writes XML file
 * in Google Cloud Storage bucket.
 */
@Path("/gcs")
public class UploadFileResource{
	private final static Logger logger = LoggerFactory.getLogger(UploadFileResource.class);
 
	private MetoBusiness business = new MetoBusiness();

	/**
	 * ClientId= 1000001, fileType=PDF, category=MARKS_AND_SPENCER_REGIONAL_6_SHORT, ValidityDate=2014-07-16T09:46:30Z
	 * 
	 * Key : <ClientId>::<category>::<ValidityDate>::<fileType>
	 * Sample : 1000001::marks_and_spencer_regional_6_short::2014-07-16::pdf
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws MessagingException
	 */
    @POST
    @Path("/upload/xml")
    @Consumes(MediaType.APPLICATION_XML)
    public Response uploadFile(IngestionConfiguration file) throws IOException, MessagingException{
		ResponseBuilder builder = null;
		RequestParameter request;
		ResponseParameter responseParameter;
		
		try{
      request = new RequestParameter.RequestParameterBuilder().setIngestionConfiguration(file).build();

			responseParameter = business.ingestDocument(request);
			
			if (responseParameter != null && responseParameter.getFileKey() != null){
				builder = Response.status(200).entity("Uploaded file name : " + responseParameter.getFileKey());
			}else{
				//TODO : Send error
			}
		} catch (Exception e) {
			logger.error("error in uploading XML file", e);
		}
		return builder.build();
    }

}
