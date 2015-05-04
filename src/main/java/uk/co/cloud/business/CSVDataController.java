package uk.co.cloud.business;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.cloud.beans.RequestParameter;
import uk.co.cloud.beans.WeatherData;
import uk.co.cloud.service.JPAPersistenceService;
import uk.co.cloud.beans.ResponseParameter;
import uk.co.cloud.service.GCSPersistenceService;
import uk.co.cloud.util.AppHelper;
import uk.co.cloud.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *  
 */
public class CSVDataController {
  private final static Logger logger = LoggerFactory.getLogger(CSVDataController.class);
  JPAPersistenceService jpaPersistenceService = new JPAPersistenceService();
  GCSPersistenceService gcsPersistenceService = new GCSPersistenceService();



  /**
   *
   * Take fromDate, toDate and clientId, and give back a list of plain/csv data between two dates.
   * @param request (fromDate, toDate and clientId)
   * @return ResponseParameter
   */
  public ResponseParameter getHistoricalData(RequestParameter request){
    List<WeatherData> list;
    InputStream is;
    ResponseParameter responseParameter = new ResponseParameter();


    try {
      String[] region = createStringArray(request.getRegion());
      Date dateFrom = AppHelper.convertStringIntoDate(request.getFromDate(), "yyyyMMdd");
      Date dateTo = AppHelper.convertStringIntoDate(request.getToDate(), "yyyyMMdd");
      String clientId = request.getClientId();
      String[] day = createStringArray(request.getday());

      list = jpaPersistenceService.getWeatherDataBetweenDates(dateFrom, dateTo, Arrays.asList(region), clientId, Arrays.asList(day));

      logger.debug("printing fromDate and toDate after conversion" + dateFrom + dateTo);
      logger.debug("list of entries between two dates is" + list.toString());
      //convert list into String
      String str = createString(list);
      is = new ByteArrayInputStream(str.getBytes("UTF-8"));
      responseParameter = new ResponseParameter.ResponseParameterBuilder().setInputStream(is).build();

    } catch (Exception ex) {
      logger.error("", ex);
    }
    return responseParameter;
  }



  /**
   * This method receives a CSV data as a String and creates a list of WeatherData entities and
   * ask persistence service to store them.
   * @return ResponseParameter object
   */
  public ResponseParameter persistCSVData(String incomingCSV, String clientID) {

    logger.info("inside persistCSVData method");
    ResponseParameter responseParameter = null;
    List<WeatherData> dataList;
    try {
      logger.info("incomingCSV :" + incomingCSV);
      dataList = createList(incomingCSV,clientID);
      responseParameter = jpaPersistenceService.persistWeatherData(dataList);
    } catch (Exception e) {
      logger.error("error in persisting csv", e);
      e.printStackTrace();
    }
    return responseParameter;
  }


  /**
   * This method converts a String into WeatherData objects
   * @param rawCSVData that is similar to
   * 		01072014,LON,1.0,2.0,3.0
   * 		01072014,SE_ENG,1.0,2.0,3.0
   * 		01072014,SE_ENG,1.0,2.0,3.0
   * @return list of WeatherData objects.
   */
  private List<WeatherData> createList(String rawCSVData, String clientId){
    String receivedString = rawCSVData.replaceAll("\\s+","\n");
    logger.info("inside createList method");
    List<WeatherData> list = new ArrayList<WeatherData>();

    WeatherData tempdata;
    String[] line = receivedString.split("\n");
    for(String temp:line){
      String[] split = temp.split(",");

      //TODO: put that in catch and throw unexpectedParameterException
      Preconditions.checkState(split.length == Constants.NUMBER_OF_PARAMETER, "Number of parameters in file are not as expected");
      Date date = AppHelper.convertStringIntoDate(split[0], "ddMMyyyy");

      String day =  AppHelper.getDay(date);

      tempdata = new WeatherData(split[0] + ":" + split[1]+":" + clientId, date, day, split[1], split[2], split[3],clientId);
      list.add(tempdata);
      logger.info("adding csv data to list");
    }
    return list;
  }


  /**
   * create array of string from a string
   */
  private String[] createStringArray(String incomingString) {
    String[] stringArray = incomingString.split("-");
    return stringArray;
  }




  /**
   * Convert a list into String
   */
  private String createString(List<WeatherData> list) {
    StringBuffer buff = new StringBuffer();
    logger.debug("The list is : " + list);
    for (WeatherData obj : list) {
      logger.info("Adding " + obj);
      buff.append(obj.getStringRepresentation()).append(
        System.lineSeparator());
    }
    logger.error("The final string is : " + buff.toString());
    return buff.toString();
  }
}
