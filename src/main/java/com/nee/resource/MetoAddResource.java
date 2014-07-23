package com.nee.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.nee.JPA.AddJPALogic;
import com.nee.beans.MetoDataJPA;
import com.nee.beans.MetoResponse;
import com.nee.utils.MetoHelper;

/**
 * 
 * @author neelam.kapoor
 *
 */
@Path("/add")
public class MetoAddResource {

	@POST  
    @Path("/text")  
    @Consumes(MediaType.TEXT_PLAIN)   
	@Produces(MediaType.TEXT_PLAIN)
    public String postOnlyCSV(String incomingString) {  
		MetoResponse metoResponse = null;
		List<MetoDataJPA> metoDataList = new ArrayList<MetoDataJPA>();
        try {
			System.out.println("incomingXML :" + incomingString);  
			metoDataList = createList(incomingString);
			metoResponse = AddJPALogic.INSTANCE.add(metoDataList);
		} catch (Exception e) {
			System.out.println("error in posting csv" + e);
		}
        return "SUCCESS";  
    }  
	
	// String is of type 01072014,SE8,1.0,2.0,3.0
	private List<MetoDataJPA> createList(String incomingString){
		List<MetoDataJPA> metoDataList = new ArrayList<>();
		
		MetoDataJPA tmpMetoData = null;
		String[] split = incomingString.split(",");
		Date date = MetoHelper.convertStringIntoDate(split[0], "yyyyMMdd");
		
		
		tmpMetoData = new MetoDataJPA(date + ":"+ split[1], date, split[1], split[2], split[3]);
		metoDataList.add(tmpMetoData);
		
		return metoDataList;
	}
	
	
}
