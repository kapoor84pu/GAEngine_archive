package uk.co.metoffice.resource;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;
import uk.co.metoffice.business.MetoBusiness;
import uk.co.metoffice.util.AppHelper;

import com.sun.jersey.api.view.Viewable;

import javax.servlet.http.Cookie;

@Path("/WeatherProduct")
public class ProductResource {
	private final static Logger logger = LoggerFactory.getLogger(ProductResource.class);

	private MetoBusiness business = new MetoBusiness();
	
	/**
	 * http://<DOMAIN>/meto/Weatherdata/Products/ddmmyyyy/ddmmyyyy/id1-id2-id3
	 */
	@POST
	@Path("/All")
	public Viewable getPDFList(@Context HttpServletRequest req,
			@Context HttpServletResponse resp,
			@FormParam("fromDate") String fromDate,
			@FormParam("toDate") String toDate) {
		
		List<MetaData> list = null;
		MetoResponse metoResponse = null;
		Cookie[] cookie = req.getCookies();
		String clientId = AppHelper.verifyCookire(cookie);
		

		MetoRequest request = new MetoRequest(fromDate, toDate, null, clientId);
		
		logger.info("invoking get method of /WeatherData/Products/fromDate/toDate");
		logger.debug("printing fromDate and toDate" + fromDate + toDate);

		try {
			metoResponse = business.getHistoProduct(request);
			list = metoResponse.getMetaDataList();
			req.setAttribute("list", list);
		} 
		catch (Exception ex) {
			logger.error("error while fetching products list",ex);
		}

		return new Viewable("/jsp/viewHistProduct.jsp");
	}
	
	  /**
	   * Retrieves a file from GCS and returns it in the http response.
	   * If a get comes in for request path /gcs/bucke/filename this will be interpreted as
	   * a request to read the GCS file named filename in the bucket bucke.
	   * @throws MessagingException 
	   */
		@GET
	    @Path("save/{filename}")
		@Produces("application/pdf")
		public Response doGet(@PathParam("filename") String filename) throws MessagingException {
			ResponseBuilder builder = null;
			MetoRequest request = new MetoRequest();
			MetoResponse metoResponse = new MetoResponse();
			logger.info("looking for " + filename + " in Google Cloud Storage bucket");
			
			try{
				request.setFilename(filename);
				//set file name in request and sendover to business class.
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
}
