package com.nee.JPA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.nee.beans.MetoDataJPA;

/**
 * This class retrieves weather data from database and returns a list to calling servlet.
 * @author Admin
 *
 */
public enum GetJPALogic {
	INSTANCE;
	
	/**
	 *ONLY FOR TEST
	 * This method takes date and region and returns a list of weather data for that date and region. 
	 * @param date
	 * @param regions
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MetoDataJPA> getWeatherData(Date date, String regions){
		List<MetoDataJPA> list = null;
		
		System.out.println("received regions and date" + date + regions);
		
		EntityManager em = EMFService.get().createEntityManager();
		try {
			//Query query = em.createQuery("select m from MetoDataJPA m");
			Query query = em.createQuery("select m from MetoDataJPA m where m.weatherDate = :weatherDate");
			
			//prepare statement
			query.setParameter("weatherDate", date);
			System.out.println("printing query" + query.toString());
			
			list = new ArrayList(query.getResultList());
			System.out.println(list.toString());
		} finally{
			em.close();
		}
		
		return list;
	}
	
	/**
	 * This method takes fromDate, toDate and region and returns weather data from fromDate to toDate for thast region.
	 * @param fromDate
	 * @param toDate
	 * @param regions
	 * @return list of MetoDataJPA objects
	 */
	//search weather data  between two dates
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MetoDataJPA> getWeatherBetweenDates(Date fromDate, Date toDate, String regions){
		List<MetoDataJPA> list = null;
		
		System.out.println("received regions and date" + fromDate + toDate);
		
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT m FROM MetoDataJPA m WHERE m.weatherDate BETWEEN :startDate AND :endDate AND m.regions = :locations");
			query.setParameter("locations", regions);
			query.setParameter("startDate", fromDate);
			query.setParameter("endDate", toDate);
			
			System.out.println("printing query" + query.toString());
			
			list = new ArrayList(query.getResultList());
			System.out.println(list.toString());
		} finally{
			em.close();
		}
		
		return list;
	}

}

	

