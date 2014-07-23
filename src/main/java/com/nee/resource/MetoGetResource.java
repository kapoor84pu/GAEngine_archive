package com.nee.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.ResponseBuilder;











import com.nee.JPA.GetJPALogic;
import com.nee.beans.MetaData;
import com.nee.beans.MetoDataJPA;
import com.nee.utils.MetoHelper;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.multipart.FormDataParam;

@Path("/WeatherData")
public class MetoGetResource {
	
	// The URL hit would be
	// http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
	@GET
	@Path("/{fromDate}/{toDate}/{regions}")
	@Produces("text/plain")
	public Response getHistoData(@PathParam("fromDate") String fromDate,
			@PathParam("toDate") String toDate,
			@PathParam("regions") String regions) {
		List<MetoDataJPA> list = null;
		InputStream is = null;

		String outputHeader = "attachment; filename=" + "export.csv";

		try {
			System.out.println("FromDate : " + fromDate + ", toDate : "
					+ toDate + ", regions : " + regions);

			String[] region = createStringArray(regions);
			Date dateFrom = MetoHelper.convertStringIntoDate(fromDate,
					"yyyyMMdd");
			Date dateTo = MetoHelper.convertStringIntoDate(toDate, "yyyyMMdd");

			list = GetJPALogic.INSTANCE.getWeatherBetweenDates(dateFrom,
					dateTo, region);
			
			System.out.println("printing fromDate and toDate after conversion" + dateFrom + dateTo);

			System.out
					.println("inside metoget resource and list from database is"
							+ list.toString());
			String str = createString(list);
			System.out.println("********" + str);
			is = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Response.ok().entity(is).header("Content-Disposition", outputHeader).build();
	}
	
	
	// The URL hit would be
		// http://<DOMAIN>/meto/Weatherdata/Products/ddmmyyyy/ddmmyyyy/id1-id2-id3
		@POST
		@Path("/Products")
		public Viewable getPDFList(@Context HttpServletRequest req,
				@Context HttpServletResponse resp,
				@FormParam("fromDate") String fromDate,
				@FormParam("toDate") String toDate) {
			
			System.out.println("invoking get method of /WeatherData/Products/fromDate/toDate");
			
			System.out.println("printing fromDate and toDate" + fromDate + toDate);
			
			List<MetaData> list = null;

			try {

				//TODO investigate about date conversion error
				Date dateFrom = MetoHelper.convertStringIntoDate(fromDate, "yyyy/MM/dd");
				Date dateTo = MetoHelper.convertStringIntoDate(toDate, "yyyy/MM/dd");
				
				System.out.println("printing fromDate and toDate after conversion" + dateFrom + dateTo);
				
				list = GetJPALogic.INSTANCE.getProductBetweenDates(dateFrom,dateTo);
				
				System.out.println("inside metoget resource and list from database is" + list.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			req.setAttribute("list", list);
			return new Viewable("/jsp/viewProductList.jsp", null);
		}
		
	private String[] createStringArray(String incomingString) {
		String[] regions = incomingString.split("-");
		return regions;
	}

	private String createString(List<MetoDataJPA> list) {
		StringBuffer buff = new StringBuffer();
		System.out.println("The list is : " + list);
		for (MetoDataJPA obj : list) {
			System.out.println("Adding " + obj);
			buff.append(obj.getStringRepresentation()).append(
					System.lineSeparator());
		}
		System.out.println("The final string is : " + buff.toString());
		return buff.toString();
	}
}
