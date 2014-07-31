package uk.co.metoffice.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.service.GCSPersistenceService;

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
		String dates = formatter.format(date);
		return dates;
	}
	//cookie verification 
	public static String verifyCookire(Cookie[] cookies){
		String clientId = null;
		for(Cookie cookie : cookies){
			if("RetailPortal".equals(cookie.getName())){
				clientId = cookie.getValue();
			}
		}
		return clientId;
	}
	

}
