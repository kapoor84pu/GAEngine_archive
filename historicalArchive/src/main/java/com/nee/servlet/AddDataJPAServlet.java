package com.nee.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nee.JPA.AddJPALogic;
import com.nee.utils.MetoHelper;

public class AddDataJPAServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//List<MetoDataJPA> list = new ArrayList<MetoDataJPA>();
		String date = req.getParameter("fromDate");
		String temperature = req.getParameter("temperature");
		String pressure = req.getParameter("pressure");
		String regions = req.getParameter("regions");
		
		String fromDate = MetoHelper.convertDate(date, "yy/mm/dd", "ddmmyyyy");
		//TODO Date
		//MetoDataJPA metoDataJPA = new MetoDataJPA(new Date(), regions, temperature, pressure);
		//MetoDataJPA metoDataJPA = new MetoDataJPA(date, temperature, pressure, regions);
		//list.add(metoDataJPA);
		//String message = AddJPALogic.INSTANCE.add(list);
		String message = AddJPALogic.INSTANCE.add(fromDate, regions, temperature, pressure);
		System.out.println("*************"+ message + "****************");
		
		
	
	}
}
