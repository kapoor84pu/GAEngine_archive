package uk.co.metoffice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.metoffice.service.GCSPersistenceService;

import javax.servlet.http.Cookie;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author neelam.kapoor
 *
 */
public class AppHelper {

	private final static Logger logger = LoggerFactory.getLogger(GCSPersistenceService.class);
	
	public static Date convertStringIntoDate(String date){
		
	    DateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
	    Date startDate = null;
	    try {
	        startDate = df.parse(date);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return startDate;
	}	
	
	public static Date convertStringIntoDate(String date, String format){
		
		logger.debug("Date is " + date + " and changing to " + format);
	    DateFormat df = new SimpleDateFormat(format); 
	    Date startDate = null;
	    try {
	        startDate = df.parse(date);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return startDate;
	}	
	
	public static String convertDateIntoString(Date date){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    return formatter.format(date);
	}
	//cookie verification
  //TODO: change in JSP as well
	public static String verifyCookie(Cookie[] cookies){
		String clientId = null;
		for(Cookie cookie : cookies){
			if("RetailPortal".equals(cookie.getName())){
				clientId = cookie.getValue();
			}
		}
		return clientId;
	}
	

}
