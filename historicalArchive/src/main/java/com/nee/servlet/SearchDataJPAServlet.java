package com.nee.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nee.JPA.GetJPALogic;
import com.nee.beans.MetoDataJPA;
import com.nee.utils.MetoHelper;

public class SearchDataJPAServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		List<MetoDataJPA> list = null;
		
		String date = req.getParameter("fromDate");
		String fromDate = MetoHelper.convertDate(date, "yy/mm/dd", "ddmmyyyy");
		String regions = req.getParameter("regions");
		
		list = GetJPALogic.INSTANCE.getWeatherData(fromDate, regions);
		
		req.setAttribute("list", list);
		System.out.println("exiting post method");
		System.out.println("printing retrieved list" + list.toString());
		req.getRequestDispatcher("/jsp/viewResult.jsp").forward(req, resp);
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/jsp/datePicker.jsp");
	}
	

}