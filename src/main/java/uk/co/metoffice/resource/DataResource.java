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

/**
 * Download/export CSV data
 * TODO: change servlet to rest
 * @author Admin
 *
 */
@Path("/WeatherData")
public class DataResource {
	private final static Logger logger = LoggerFactory.getLogger(DataResource.class);
	
	private MetoBusiness business = new MetoBusiness();
	
	/**
	 *  The URL hit would be http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
	 *  export CSV file
	 * @param fromDate
	 * @param toDate
	 * @param regions
	 * @return
	 */
	@GET
	@Path("/{fromDate}/{toDate}/{regions}/{clientId}")
	@Produces("text/plain")
	public Response getHistoData(@PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate, @PathParam("regions") String regions,@PathParam("clientId") String clientId) {

		InputStream is = null;
		MetoResponse metoResponse = null;
		MetoRequest request = null;
		String outputHeader = "attachment; filename=" + "export.csv";

		try {
			logger.debug("FromDate : " + fromDate + ", toDate : " + toDate
									+ ", regions : " + regions +"for client " + clientId);
			request = new MetoRequest(fromDate, toDate, regions, clientId);
			metoResponse = business.getHistoData(request);
			is = metoResponse.getStream();
		} 
		catch (Exception ex) {
			logger.error("error in exporting CSV data" , ex);
		}
		return Response.ok().entity(is).header("Content-Disposition", outputHeader).build();
	}

}


//
//
///**
// * http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
// */
//@POST
//@Path("/All")
//public Viewable getCSVDataList(@Context HttpServletRequest req,
//		@Context HttpServletResponse resp,
//		@FormParam("fromDate") String fromDate,
//		@FormParam("toDate") String toDate,
//		@FormParam("regions") String[] regions) {
//	
//	logger.info("invoking post method in DataResource");
//	List<MetoDataJPA> list = null;
//	String regionList = null;
//	Date dateFrom = null;
//	Date dateTo = null;
//	String clientId = null;
//	StringBuilder str = new StringBuilder();
//	Cookie[] cookies = req.getCookies();
//	
//	try {
//		dateFrom = AppHelper.convertStringIntoDate(fromDate);
//		dateTo = AppHelper.convertStringIntoDate(toDate);
//		//if region is not present, add all the regions by default
//		if (regions == null) {
//			regions = Regions.getAllRegions();
//		}
//		
//		clientId = AppHelper.verifyCookire(cookies);
//		
//		list = JPAPersistenceService.INSTANCE.getWeatherBetweenDates(dateFrom, dateTo, regions,clientId);
//		
//		for(String temp:regions){
//			str.append(temp);
//			str.append("-");
//		}
//		
//		regionList = str.toString();
//		
//		logger.debug("list of regions" + regionList);
//		
//		req.setAttribute("fromDate",dateFrom);
//		req.setAttribute("toDate",dateTo);
//		req.setAttribute("regionList",regionList);
//		req.setAttribute("list", list);
//		
//		logger.debug("printing retrieved list" + list.toString());
//		logger.info("exiting post method");
//	} catch (Exception e) {
//		logger.error("error while fetching data" , e);
//	}
//	return new Viewable("/jsp/viewHistData.jsp");
//}