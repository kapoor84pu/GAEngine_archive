package com.nee.JPA;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.nee.beans.MetoDataJPA;

public enum GetJPALogic {
	INSTANCE;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MetoDataJPA> getWeatherData(String date, String regions){
		List<MetoDataJPA> list = null;
		
		System.out.println("received regions and date" + date + regions);
		
		EntityManager em = EMFService.get().createEntityManager();
		try {
			//Query query = em.createQuery("select m from MetoDataJPA m");
			Query query = em.createQuery("select m from MetoDataJPA m where m.regions = :location");
			query.setParameter("location", regions);
			System.out.println("printing query" + query.toString());
			
			list = new ArrayList(query.getResultList());
			System.out.println(list.toString());
		} finally{
			em.close();
		}
		
		return list;
	}

}