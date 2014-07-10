package com.nee.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nee.JPA.AddJPALogic;
import com.nee.utils.MetoHelper;

/**
 * This class receives parameter from jsp and call addJPALogic class to persists parameters into database
 * @author Admin
 *
 */
public class AddDataJPAServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/jsp/addData.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//List<MetoDataJPA> list = new ArrayList<MetoDataJPA>();
		String dateFrom = req.getParameter("fromDate");
		String temperature = req.getParameter("temperature");
		String pressure = req.getParameter("pressure");
		String regions = req.getParameter("regions");
		
		Date fromDate = MetoHelper.convertStringIntoDate(dateFrom);
		
		//TODO create an object of MetoDataJPA and send it over to AddJPALogic class
		String message = AddJPALogic.INSTANCE.add(fromDate, regions, temperature, pressure);
		System.out.println("*************"+ message + "****************");
	}
	
}
