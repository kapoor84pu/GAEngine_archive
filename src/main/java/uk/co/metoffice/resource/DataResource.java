package uk.co.metoffice.resource;

import com.sun.jersey.api.view.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.*;
import uk.co.metoffice.business.MetoBusiness;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
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

  private MetoBusiness business = new MetoBusiness();

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
  @Path("/{fromDate}/{toDate}/{regions}/{clientId}")
  @Produces("text/plain")
  public Response getHistoricalData(@PathParam("fromDate") String fromDate,
                                    @PathParam("toDate") String toDate,
                                    @PathParam("regions") String regions,
                                    @PathParam("clientId") String clientId)
  {
    InputStream is = null;

    try {
      logger.debug("FromDate: '{}',  toDate: '{}', regions: '{}' for client: '{}'", fromDate, toDate, regions, clientId);
      RequestParameter request = new RequestParameter.RequestParameterBuilder().setFromDate(fromDate).setToDate(toDate).setRegion(regions).setClientId(clientId).build();
      ResponseParameter responseParameter = business.getHistoricalData(request);
      is = responseParameter.getStream();
    } catch (Exception ex) {
      logger.error("error in exporting CSV data", ex);
    }
    return Response.ok().entity(is).header("Content-Disposition", Constants.OUTPUT_HEADER_CSV).build();
  }


  /**
   * http://<DOMAIN>/meto/Weatherdata/ddmmyyyy/ddmmyyyy/id1-id2-id3
   */
  @POST
  @Path("/All")
  public Viewable getCSVDataList(@Context HttpServletRequest req,
                                 @Context HttpServletResponse resp,
                                 @FormParam("fromDate") String fromDate,
                                 @FormParam("toDate") String toDate,
                                 @FormParam("regions") List<String> regions) {

    logger.info("invoking post method in DataResource");
    List<WeatherData> list;
    String regionList;
    Date dateFrom;
    Date dateTo;
    String clientId;
    StringBuilder str = new StringBuilder();
    JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
    Cookie[] cookies = req.getCookies();

    try {
      dateFrom = AppHelper.convertStringIntoDate(fromDate);
      dateTo = AppHelper.convertStringIntoDate(toDate);
      //if region is not present, persistWeatherData all the regions by default
      if (regions == null) {
        regions = Regions.getAllRegions();
      }

      clientId = AppHelper.verifyCookie(cookies);

      list = jpaPersistenceService.getWeatherDataBetweenDates(dateFrom, dateTo, regions, clientId);

      for (String temp : regions) {
        str.append(temp);
        str.append("-");
      }

      regionList = str.toString();

      logger.debug("list of regions" + regionList);

      req.setAttribute("fromDate", fromDate.replaceAll("/", ""));
      req.setAttribute("toDate", toDate.replaceAll("/", ""));
      req.setAttribute("regionList", regionList);
      req.setAttribute("list", list);

      logger.debug("printing retrieved list" + list.toString());
      logger.trace("exiting post method");
    } catch (Exception e) {
      logger.error("error while fetching data", e);
    }
    return new Viewable("/jsp/viewHistoricalData.jsp");
  }

}


