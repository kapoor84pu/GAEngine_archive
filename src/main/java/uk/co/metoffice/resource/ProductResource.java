package uk.co.metoffice.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.beans.ResponseParameter;
import uk.co.metoffice.business.ProductController;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.JsonGenerator;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.List;

@Path("/WeatherProduct")
public class ProductResource {
	private final static Logger logger = LoggerFactory.getLogger(ProductResource.class);

	private ProductController productController = new ProductController();
	
	/**
	 * List the products between fromDate to toDate for a clientId
   * http://<DOMAIN>/meto/Weatherdata/Products/ddmmyyyy/ddmmyyyy/id1-id2-id3
	 */
	@POST
	@Path("/All")
	public String getPDFList(@Context HttpServletRequest req,
			@Context HttpServletResponse resp,
			@FormParam("fromDate") String fromDate,
			@FormParam("toDate") String toDate) {
		
		List<MetaData> list;
    String jsonString = "[]";
		ResponseParameter responseParameter;
		Cookie[] cookie = req.getCookies();
		String clientId = AppHelper.verifyCookie(cookie);
		

		RequestParameter request = new RequestParameter.RequestParameterBuilder()
                                  .setFromDate(fromDate)
                                  .setToDate(toDate)
                                  .setClientId(clientId)
                                  .build();
		
		logger.info("invoking get method of /WeatherData/Products/fromDate/toDate");
		logger.debug("printing fromDate and toDate" + fromDate + toDate);

		try {
			responseParameter = productController.getHistoricalProduct(request);
			list = responseParameter.getMetaDataList();
      jsonString = JsonGenerator.jsonGenerator(list);
			req.setAttribute("list", list);
		} 
		catch (Exception ex) {
			logger.error("error while fetching products list",ex);
		}

		return jsonString;
	}
	
	  /**
	   * Retrieves a file from GCS and returns it in the http response.
	   * If a get comes in for request path /gcs/bucketName/filename this will be interpreted as
	   * a request to read the GCS file named filename in the bucket bucketName.
	   * @throws javax.mail.MessagingException
	   */
		@GET
	  @Path("save/{filename}")
		@Produces("application/pdf")
		public Response doGet(@PathParam("filename") String filename) throws MessagingException {
			ResponseBuilder builder = null;
			RequestParameter request;
			ResponseParameter responseParameter;
			logger.info("looking for " + filename + " in Google Cloud Storage bucket");
			
			try{
        request = new RequestParameter.RequestParameterBuilder()
                      .setFilename(filename)
                      .build();

				//set file name in request and send over to business class.
				responseParameter = productController.getDocumentStream(request);
				
				if (responseParameter != null && responseParameter.getStream() != null){
					builder = Response.ok(responseParameter.getStream())
                             .header("Content-Disposition", "attachment; filename=" + filename + ".pdf");
				}else{
					//TODO : Send error
				}
			} catch (Exception e) {
				logger.error("could not find " + filename);
				e.printStackTrace();
			}
			return builder.build();
	  }
}
