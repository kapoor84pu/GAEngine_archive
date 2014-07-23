package com.nee.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nee.JPA.AddJPALogic;
import com.nee.beans.MetoDataJPA;
import com.nee.beans.MetoResponse;
import com.nee.utils.MetoHelper;

/**
 * RESTful web service to provide an entry point to add csv and pdf data in data store.
 * @author neelam.kapoor
 *
*/
public class MetoAdd {
	
    public String postOnlyCSV(String incomingCSV) { 
		
		System.out.println("inside postOnlyCSV method");
		MetoResponse metoResponse = null;
		List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
        try {
			System.out.println("incomingCSV :" + incomingCSV);  
			metoDataList = createList(incomingCSV);
			metoResponse = AddJPALogic.INSTANCE.add(metoDataList);
		} catch (Exception e) {
			System.out.println("error in posting csv" + e);
			e.printStackTrace();
		}
        return metoResponse.getMessage(); 
    } 
	
	/**
	 * 
	 * @param incomingString that is similar to 
	 * 		01072014,LON,1.0,2.0,3.0
	 * 		01072014,SE_ENG,1.0,2.0,3.0
	 * 		01072014,SE_ENG,1.0,2.0,3.0
	 * @return list of MetoDataJPA objects.
	 */
	private List<MetoDataJPA> createList(String incomingString){
		String receivedString = incomingString.replaceAll("\\s+","\n");
		System.out.println("inside createList method");
		List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
		
		MetoDataJPA tmpMetoData = null;
		String[] line = receivedString.split("\n");
		for(String temp:line){
			String[] split = temp.split(",");
			Date date = MetoHelper.convertStringIntoDate(split[0], "ddMMyyyy");
			tmpMetoData = new MetoDataJPA(split[0] + ":" + split[1],date, split[1], split[2], split[3]);
			metoDataList.add(tmpMetoData);
			System.out.println("adding csv data to list");
		}
	
		return metoDataList;
	}
	
}



