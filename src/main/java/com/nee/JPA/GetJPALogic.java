package com.nee.JPA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.collect.Lists;
import com.nee.beans.MetaData;
import com.nee.beans.MetoDataJPA;

/**
 * This class retrieves weather data from database and returns a list to calling servlet.
 * @author neelam.kapoor
 *
 */
public enum GetJPALogic {
	INSTANCE;
	
	/**
	 * This method returns weather data from fromDate to toDate for single/list of regions.
	 * @param fromDate
	 * @param toDate
	 * @param regions
	 * @return list of MetoDataJPA objects
	 */
	//search weather data  between two dates
	@SuppressWarnings({ "unchecked" })
	public List<MetoDataJPA> getWeatherBetweenDates(Date fromDate, Date toDate, String[] regions){
		List<MetoDataJPA> list = Lists.newArrayList();
		
		System.out.println("received fromDate and toDate" + fromDate + toDate);
		System.out.println("list of regions" + regions);
		
		for(String reg: regions){
			if (!reg.equals("")){
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Query query = em.createQuery("SELECT m FROM MetoDataJPA m WHERE m.weatherDate BETWEEN :startDate AND :endDate AND m.regions = :locations ORDER BY m.regions");
				query.setParameter("locations", reg);
				query.setParameter("startDate", fromDate);
				query.setParameter("endDate", toDate);
				
				System.out.println("printing query" + query.toString());
				
				list.addAll(query.getResultList());
				System.out.println("+++++++++++++++++" + list.toString());
			}finally{
				em.close();
			}
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<MetaData> getProductBetweenDates(Date fromDate, Date toDate){
		List<MetaData> list = new ArrayList<MetaData>();
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT p FROM MetaData p WHERE p.validityDate BETWEEN :startDate AND :endDate");
		//	Query query = em.createQuery("SELECT p FROM MetaData p WHERE p.validityDate = :startDate");
		//	Query query = em.createQuery("SELECT p FROM MetaData p WHERE p.clientId ='1000001'");
				query.setParameter("startDate", fromDate);
				query.setParameter("endDate", toDate);
				
			System.out.println("printing query" + query.toString());
			list =  query.getResultList();
			
			System.out.println("+++++++++++++++++" + list.toString());
		}finally{
			em.close();
		}
	
	return list;
	}

}

	

