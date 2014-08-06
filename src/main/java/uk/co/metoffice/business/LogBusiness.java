package uk.co.metoffice.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.beans.RequestParameter;
import uk.co.metoffice.service.LogService;
import uk.co.metoffice.util.AppHelper;
import uk.co.metoffice.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * This class interact with resource to get parameter and serve through service class.
 *
 * @author neelam.kapoor
 */
public class LogBusiness {

  private LogService logService = new LogService();
  private final static Logger logger = LoggerFactory.getLogger(LogBusiness.class);

  /**
   * fetch logs as a single string form google logs.
   * @param request, newLine
   * @return String
   */
  public String generateLogString(RequestParameter request, String newLine) {

    logger.info(Constants.METHOD_START + "generateLogString(RequestParameter, String)");

    Date fromDate = AppHelper.convertStringIntoDate(request.getFromDate(), Constants.FORMAT_DATETIME);
    Date toDate = AppHelper.convertStringIntoDate(request.getToDate(), Constants.FORMAT_DATETIME);
    logger.debug("Post conversion params : " + fromDate + "," + toDate);

    String str = logService.fetchLogs(fromDate, toDate, newLine);

    logger.info(Constants.METHOD_END + "generateLogString(RequestParameter, String)");
    return str;
  }


  /**
   * Converts String into ByteArrayInputStream
    */
  public InputStream stringToStream(String string) {
    return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
  }
}
