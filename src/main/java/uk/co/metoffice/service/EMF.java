package uk.co.metoffice.service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * @author neelam.kapoor
 *
 */
public class EMF {
	
	/*transactions-optional is the configuration in persitence.xml, 
	 		can have more than one configuration in persistemce.xml */
  private static final EntityManagerFactory emfInstance = Persistence
      .createEntityManagerFactory("transactions-optional");

  private EMF() {
  }

  public static EntityManagerFactory get() {
    return emfInstance;
  }
}
