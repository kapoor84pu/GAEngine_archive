package uk.co.metoffice.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetoDataJPA;
import uk.co.metoffice.beans.Regions;
import uk.co.metoffice.service.JPAPersistenceService;
import uk.co.metoffice.util.AppHelper;

/**
 * This class receives parameters from searchHistdata.jsp and returns back a list of MetoDataJPA objects to viewHistdata.jsp
 * @author neelam.kapoor
 *
 */
public class SearchDataJPAServlet extends HttpServlet {
	private final static Logger logger = LoggerFactory.getLogger(SearchDataJPAServlet.class);

	private static final long serialVersionUID = 1L;
	
	/**
	 * TODO : This to be moved to a resource to make it consistent with others.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("invoking post method");
		List<MetoDataJPA> list = null;
		String dateFrom = req.getParameter("fromDate");
		String dateTo = req.getParameter("toDate");
		
		Date fromDate = AppHelper.convertStringIntoDate(dateFrom);
		Date toDate = AppHelper.convertStringIntoDate(dateTo);
		
		String[] regions = req.getParameterValues("regions");
		
		//if region is not present, add all the regions by default
		if (regions == null) {
			regions = Regions.getAllRegions();
		}
		
		Cookie[] cookies = req.getCookies();
		String clientId = AppHelper.verifyCookire(cookies);
		
		
		list = JPAPersistenceService.INSTANCE.getWeatherBetweenDates(fromDate, toDate, regions,clientId);
		
		String regionList = null;
		StringBuilder str = new StringBuilder();
		for(String temp:regions){
			str.append(temp);
			str.append("-");
		}
		
		regionList = str.toString();
		
		logger.info(regionList);
		
		req.setAttribute("fromDate",dateFrom);
		req.setAttribute("toDate",dateTo);
		req.setAttribute("regionList",regionList);
		
		req.setAttribute("list", list);
		System.out.println("exiting post method");
		System.out.println("printing retrieved list" + list.toString());
		req.getRequestDispatcher("/jsp/viewHistData.jsp").forward(req, resp);
		
	}

	

}
