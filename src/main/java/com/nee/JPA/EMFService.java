package com.nee.JPA;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author neelam.kapoor
 *
 */
public class EMFService {
	
	/*transactions-optional is the configuration in persitence.xml, 
	 		can have more than one configuration in persistemce.xml */
  private static final EntityManagerFactory emfInstance = Persistence
      .createEntityManagerFactory("transactions-optional");

  private EMFService() {
  }

  public static EntityManagerFactory get() {
    return emfInstance;
  }
}
