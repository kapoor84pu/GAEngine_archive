package com.nee.JPA;

import javax.persistence.EntityManager;

import com.nee.beans.MetoDataJPA;

public enum AddJPALogic {
	INSTANCE;
	
	public String add(String date, String regions, String temperature, String pressure ){
		EntityManager em = EMFService.get().createEntityManager();
		
		try {
			MetoDataJPA mJPA = new MetoDataJPA(date, regions, temperature,pressure);
			
			em.persist(mJPA);
			System.out.println("stored entity id : " + em.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAILURE";
		}
		finally{
			em.close();
		}
		return "SUCCESS";
	}
}