package uk.co.metoffice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.metoffice.beans.MetaData;
import uk.co.metoffice.beans.MetoDataJPA;
import uk.co.metoffice.beans.MetoResponse;

import com.google.common.collect.Lists;

public enum JPAPersistenceService {
	INSTANCE;
	private final static Logger logger = LoggerFactory.getLogger(JPAPersistenceService.class);
	
	public MetoResponse add(List<MetoDataJPA> metoDataList) {
		EntityManager em = null;
		MetoResponse metoResponse = null;
		
	//	em = EMF.get().createEntityManager();
		try {
			for(MetoDataJPA tempObj:metoDataList){
				em = EMF.get().createEntityManager();
				logger.info("Processing " + tempObj);
 				
				em.persist(tempObj);
				metoResponse = new MetoResponse("SUCCESS",null);
				em.close();	
			} 
			logger.info("Finished with persisting entities");
		} catch (Exception e) {
			logger.error("Error in persisting entity ", e);
			metoResponse = new MetoResponse("FAILURE",null);
		}finally{
		//TODO: close em here rather than in loop.
			}
		return metoResponse;
	}
	
    /**
     * This method persists metaData for PDF/Products in google store 
     * @param MetaData
     */
    public void persistMetadata(MetaData metaData){
    	EntityManager em = EMF.get().createEntityManager();
		try {					
			em.persist(metaData);
			logger.info("stored entity id : " + em.toString());
		} catch (Exception e) {
			logger.error("Error in persisting metadata ", e);
		}
		finally{
			em.close();
		}
    }
    
	/**
     * 
     */
    public List<MetaData> getMetadataListing(){
    	List<MetaData> list = new ArrayList<MetaData>();
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT p FROM MetaData p");
			
			logger.info("inside getMatadata Listing method and printing query" + query.toString());
			list =  query.getResultList();
		}finally{
			em.close();
		}
		return list;
    }

	/**
	 * Method returns weather data from fromDate to toDate for single/list of region/s.
	 * @param fromDate
	 * @param toDate
	 * @param regions
	 * @return list of MetoDataJPA objects
	 */
	@SuppressWarnings({ "unchecked" })
	public List<MetoDataJPA> getWeatherBetweenDates(Date fromDate, Date toDate, String[] regions,String clientId){
		List<MetoDataJPA> list = Lists.newArrayList();
		
		logger.info("received fromDate: " + fromDate + " toDate " + toDate + "and Regions" + regions);
		
		for(String reg: regions){
			if (!reg.equals("")){
			EntityManager em = EMF.get().createEntityManager();
			try {
				Query query = em.createQuery("SELECT m FROM MetoDataJPA m WHERE m.weatherDate BETWEEN :startDate AND :endDate "
						+ "							AND m.regions = :locations AND m.clientId = :clientID ORDER BY m.regions");
				query.setParameter("locations", reg);
				query.setParameter("startDate", fromDate);
				query.setParameter("endDate", toDate);
				query.setParameter("clientID", clientId);
				
				list.addAll(query.getResultList());
				logger.info("for query " + query.toString() + "result list is " + list.toString());
			}finally{
				em.close();
			}
			}
		}
		return list;
	}
	
	/**
	 * Method returns product data list from fromDate to toDate
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MetaData> getProductBetweenDates(Date fromDate, Date toDate, String clientId){
		List<MetaData> list = new ArrayList<MetaData>();
		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery("SELECT p FROM MetaData p WHERE p.validityDate BETWEEN :startDate "
					+ "							AND :endDate AND p.clientId = :clientID");
			query.setParameter("startDate", fromDate);
			query.setParameter("endDate", toDate);
			query.setParameter("clientID", clientId);
				
			list =  query.getResultList();
			
			logger.info("for query " + query.toString() + "result list is " + list.toString());
		}finally{
			em.close();
		}
	
	return list;
	}
}
