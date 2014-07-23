package com.nee.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nee.JPA.GetJPALogic;
import com.nee.beans.MetoDataJPA;
import com.nee.beans.Regions;
import com.nee.utils.MetoHelper;

/**
 * This class receives parameters from datePicker.jsp and returns back a list of weather data to viewResult.jsp
 * @author neelam.kapoor
 *
 */
public class SearchDataJPAServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		List<MetoDataJPA> list = null;
		String dateFrom = req.getParameter("fromDate");
		String dateTo = req.getParameter("toDate");
		
		//checking java.util date
		Date fromDate = MetoHelper.convertStringIntoDate(dateFrom);
		Date toDate = MetoHelper.convertStringIntoDate(dateTo);
		
		String[] regions = req.getParameterValues("regions");	
		//if region is not present, add all the regions by default
		if (regions == null) {
			regions = Regions.getAllRegions();
		}
		
		list = GetJPALogic.INSTANCE.getWeatherBetweenDates(fromDate, toDate, regions);
		
		String regionList = null;
		StringBuilder str = new StringBuilder();
		for(String temp:regions){
			str.append(temp);
			str.append("-");
		}
		
		regionList = str.toString();
		
		System.out.println("*******************"+regionList);
		
		req.setAttribute("fromDate",dateFrom);
		req.setAttribute("toDate",dateTo);
		req.setAttribute("regionList",regionList);
		
		req.setAttribute("list", list);
		System.out.println("exiting post method");
		System.out.println("printing retrieved list" + list.toString());
		req.getRequestDispatcher("/jsp/viewResult.jsp").forward(req, resp);
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//resp.sendRedirect("/jsp/datePicker.jsp");
	}
	

}
