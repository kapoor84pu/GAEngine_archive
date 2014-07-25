package uk.co.metoffice.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;
import uk.co.metoffice.business.MetoBusiness;

import com.sun.jersey.api.view.Viewable;

@Path("/WeatherProduct")
public class MetoProductResource {
	private final static Logger logger = LoggerFactory.getLogger(MetoProductResource.class);

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

		MetoRequest request = new MetoRequest(fromDate, toDate, null);
		
		System.out.println("invoking get method of /WeatherData/Products/fromDate/toDate");
		System.out.println("printing fromDate and toDate" + fromDate + toDate);

		try {
			metoResponse = business.getHistoProduct(request);
			list = metoResponse.getMetaDataList();
			req.setAttribute("list", list);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return new Viewable("/jsp/viewHistProduct.jsp", null);
	}
}
