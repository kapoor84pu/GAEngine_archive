package uk.co.cloud.service;

import com.google.appengine.api.log.AppLogLine;
import com.google.appengine.api.log.LogQuery;
import com.google.appengine.api.log.LogService.LogLevel;
import com.google.appengine.api.log.LogServiceFactory;
import com.google.appengine.api.log.RequestLogs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * A service class interact with google cloud and fetch logs in between the provided dates.
 * Uses google api named "LogQuery" to use Log service.
 * 
 * @author neelam.kapoor
 *
 */
public class LogService {

	private final static Logger logger = LoggerFactory.getLogger(LogService.class);
	
	/**
	 * Fetch logs between given dates
	 * @param fromDate
	 * @param toDate
	 * @param newline
	 * @return log String
	 */
	public String fetchLogs(Date fromDate, Date toDate, String newline) {
		String str = "";
		StringBuffer buff = new StringBuffer();
		
		try {
			LogQuery query = LogQuery.Builder
                        .withStartTimeMillis(fromDate.getTime())
                        .endTimeMillis(toDate.getTime())
                        .batchSize(30)
                        .minLogLevel(LogLevel.ERROR);
			query.includeAppLogs(true);

      Iterable<RequestLogs>  requestLogs = LogServiceFactory.getLogService().fetch(query);

			for (RequestLogs record : requestLogs) {
			     buff.append(newline);
			      Calendar cal = Calendar.getInstance();
			      cal.setTimeInMillis(record.getStartTimeUsec() / 1000);
			      
			     buff.append(String.format("%s", cal.getTime().toString()) + ",");
			     buff.append("" + record.getIp() + ",");
			     buff.append(record.getMethod() + ",");
			      
			      String s = record.getResource().replaceAll(" at ", newline + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at ");
			     buff.append(s + ",");

			      // Display all the app logs for each request log.
			      for (AppLogLine appLog : record.getAppLogLines()) {
			       buff.append(newline);
			        Calendar appCal = Calendar.getInstance();
			        appCal.setTimeInMillis(appLog.getTimeUsec() / 1000);

			       buff.append(String.format("%s", appCal.getTime().toString()) + ",");
			       buff.append(appLog.getLogLevel()+",");
			       buff.append(appLog.getLogMessage()+",");

			      } //for each log line
			}
			str = buff.toString();
		} catch (Exception e) {
			logger.error("error while fetching logs", e);
		}
		return str;
	}
}
