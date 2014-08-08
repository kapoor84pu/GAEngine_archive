package uk.co.metoffice.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.*;
import uk.co.metoffice.business.CSVDataController;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.Constants;
import uk.co.metoffice.util.JsonGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * This class read cDownload/export CSV data
 *
 * @author Admin
 */
@Path("/WeatherData")
public class DataResource {
  private final static Logger logger = LoggerFactory.getLogger(DataResource.class);

  private CSVDataController csvDataController = new CSVDataController();

  /**
   * The URL hit would be http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
   * export CSV file
   *
   * @param fromDate
   * @param toDate
   * @param regions
   *
   * @return
   */
  @GET
  @Path("/{fromDate}/{toDate}/{regions}/{day}/{clientId}")
  @Produces("text/plain")
  public Response getHistoricalData(@PathParam("fromDate") String fromDate,
                                    @PathParam("toDate") String toDate,
                                    @PathParam("regions") String regions,
                                    @PathParam("day") String day,
                                    @PathParam("clientId") String clientId)
  {
    InputStream is = null;

    try {
      logger.debug("FromDate: '{}',  toDate: '{}', regions: '{}' for client: '{}'", fromDate, toDate, regions, clientId);
      RequestParameter request = new RequestParameter.RequestParameterBuilder()
                                                      .setFromDate(fromDate)
                                                      .setToDate(toDate)
                                                      .setRegion(regions)
                                                      .setClientId(clientId)
                                                      .setDay(day)
                                                      .build();
      ResponseParameter responseParameter = csvDataController.getHistoricalData(request);
      is = responseParameter.getStream();
    } catch (Exception ex) {
      logger.error("error in exporting CSV data", ex);
    }
    return Response.ok().entity(is).header("Content-Disposition", Constants.OUTPUT_HEADER_CSV).build();
  }


  /**
   * http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
   * show all CSV data between two dates.
   */
  @POST
  @Path("/All")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public String getCSVDataList(@Context HttpServletRequest req,
                                 @Context HttpServletResponse resp,
                                 @FormParam("fromDate") String fromDate,
                                 @FormParam("toDate") String toDate,
                                 @FormParam("day") List<String> day,
                                 @FormParam("regions") List<String> regions) {

    logger.info("invoking post method in DataResource");
    List<WeatherData> list;
    String regionList;
    String dayList;
    Date dateFrom;
    Date dateTo;
    String clientId;
    StringBuilder str = new StringBuilder();
    StringBuilder dayString = new StringBuilder();
    JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
    Cookie[] cookies = req.getCookies();
    String jsonArray = "[]";

    System.out.println("received dates are :" + toDate + fromDate);

    try {
      dateFrom = AppHelper.convertStringIntoDate(fromDate);
      dateTo = AppHelper.convertStringIntoDate(toDate);

      //if region is not present, persistWeatherData all the regions by default
      if (regions.isEmpty()) {
        regions = Regions.getAllRegions();
      }

      if (day.isEmpty()) {
        day = AllDays.getAllDays();
      }
      clientId = AppHelper.verifyCookie(cookies);

      list = jpaPersistenceService.getWeatherDataBetweenDates(dateFrom, dateTo, regions, clientId, day);



      for (String temp : regions) {
        str.append(temp);
        str.append("-");
      }

      for (String tempDay : day) {
        dayString.append(tempDay);
        dayString.append("-");
      }

      regionList = str.toString();
      dayList = dayString.toString();

      jsonArray = JsonGenerator.jsonGenerator(list);

      logger.debug("list of regions" + regionList);

      req.setAttribute("fromDate", fromDate.replaceAll("/", ""));
      req.setAttribute("toDate", toDate.replaceAll("/", ""));
      req.setAttribute("regionList", regionList);
      req.setAttribute("dayList", dayList);
      req.setAttribute("list", list);
      req.setAttribute("jsonArray", jsonArray);

      logger.debug("printing retrieved list" + list.toString());
      logger.trace("exiting post method");
    } catch (Exception e) {
      logger.error("error while fetching data", e);
    }
    return jsonArray;
  }

}


