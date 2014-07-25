package uk.co.metoffice.resource;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetoRequest;
import uk.co.metoffice.beans.MetoResponse;
import uk.co.metoffice.business.MetoBusiness;

@Path("/WeatherData")
public class MetoDataResource {
	private final static Logger logger = LoggerFactory.getLogger(MetoDataResource.class);
	
	private MetoBusiness business = new MetoBusiness();
	
	/**
	 *  The URL hit would be http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
	 * @param fromDate
	 * @param toDate
	 * @param regions
	 * @return
	 */
	@GET
	@Path("/{fromDate}/{toDate}/{regions}")
	@Produces("text/plain")
	public Response getHistoData(@PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate, @PathParam("regions") String regions) {

		InputStream is = null;
		MetoResponse metoResponse = null;
		MetoRequest request = new MetoRequest(fromDate, toDate, regions);
		String outputHeader = "attachment; filename=" + "export.csv";

		try {
			System.out.println("FromDate : " + fromDate + ", toDate : " + toDate + ", regions : " + regions);
			metoResponse = business.getHistoData(request);
			is = metoResponse.getStream();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return Response.ok().entity(is).header("Content-Disposition", outputHeader).build();
	}

}
