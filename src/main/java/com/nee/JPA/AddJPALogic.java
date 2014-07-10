package com.nee.JPA;

import java.util.Date;
import javax.persistence.EntityManager;
import com.nee.beans.MetoDataJPA;

/**
 * This class takes weather data parameters and persists them on data stores.
 * @author Admin
 *
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
	public String add(Date date, String regions, String temperature, String pressure ){
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