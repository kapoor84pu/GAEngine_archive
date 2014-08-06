package uk.co.metoffice.resource;
//
import com.sun.jersey.api.view.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.business.LogBusiness;
import uk.co.metoffice.util.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/Logs")
public class LogResource {
	private LogBusiness business = new LogBusiness();
	RequestParameter request;
	private static Logger logger = LoggerFactory.getLogger(LogResource.class);
	
	/**
	 * http://<DOMAIN>/meto/Weatherdata/Products/ddmmyyyy/ddmmyyyy/id1-id2-id3
	 * This method fetch all the logs between given dates.
	 */
	@POST
	@Path("/All")
	public Object getPDFList(@Context HttpServletRequest req,
			@Context HttpServletResponse resp,
			@FormParam("fromLogDateTime") String fromLogDateTime,
			@FormParam("toLogDateTime") String toLogDateTime, 
			@FormParam("Continue") String continueButton,
			@FormParam("Download") String downloadButton) {
		
		InputStream is = null;
		String str = "";

		//logger.error("DEBUG MetoLogResource 010101"); 
		logger.info("invoking get method of /Logs/All");
		logger.debug("Params : " + fromLogDateTime + "," + toLogDateTime + "," + downloadButton);

		try {
      request = new RequestParameter.RequestParameterBuilder().setFromDate(fromLogDateTime).setToDate(toLogDateTime).build();
//			request.setFromDate(fromLogDateTime);
//			request.setToDate(toLogDateTime);
			
			req.setAttribute("fromLogDateTime", fromLogDateTime);
			req.setAttribute("toLogDateTime", toLogDateTime);
			
			if (downloadButton != null){
				str = business.generateLogString(request, Constants.NEWLINE_CHAR);
				is = business.stringToStream(str);
				return Response.ok().entity(is).header("Content-Disposition", "attachment; filename=" + "logA.txt").build();
			}else{
				str = business.generateLogString(request, Constants.NEWLINE_HTML_TAG);
		    	req.setAttribute("LogContent", str);
		    	return new Viewable("/jsp/searchLogData.jsp");
			}
		    
		} 
		catch (Exception ex) {
			logger.error("error while fetching logs", ex);
		}
		return new Viewable("/jsp/searchLogData.jsp");
	}
}
