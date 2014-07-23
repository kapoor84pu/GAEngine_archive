package com.nee.JPA;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.nee.beans.MetoDataJPA;
import com.nee.beans.MetoResponse;

/**
 * This class takes weather data parameters and persists them on data stores.
 * @author neelam.kapoor
 */

public enum AddJPALogic {
	INSTANCE;
	
	/**
	 * This method persists parameters into data stores.
	 * @param date
	 * @param regions
	 * @param temperature
	 * @param pressure
	 * @return "FAILURE" or "SUCCESS" as response
	 */
	public MetoResponse add(Date date, String regions, String temperature, String pressure ){
		MetoResponse metoResponse = null;
		EntityManager em = EMFService.get().createEntityManager();
		try {			
			MetoDataJPA mJPA = new MetoDataJPA(date + ":" + regions, date, regions, temperature,pressure);			
			em.persist(mJPA);
			System.out.println("stored entity id : " + em.toString());
			 metoResponse = new MetoResponse("SUCCESS",null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 metoResponse = new MetoResponse("FAILURE",null);
		}
		finally{
			em.close();
		}
		return metoResponse;
	}

	//Method overloading
	public MetoResponse add(List<MetoDataJPA> metoDataList) {
		EntityManager em = null;
		MetoResponse metoResponse = null;
		
		em = EMFService.get().createEntityManager();
		try {
			for(MetoDataJPA tempObj:metoDataList){
				em = EMFService.get().createEntityManager();
				System.out.println("Processing " + tempObj);
 				
				em.persist(tempObj);
				System.out.println("stored entity id : " + em.toString());
				metoResponse = new MetoResponse("SUCCESS",null);
				em.close();
			} 
			System.out.println("All finished ");
		} catch (Exception e) {
				e.printStackTrace();
				metoResponse = new MetoResponse("FAILURE",null);
		}finally{
				
			}
		return metoResponse;
	}

}