package uk.co.cloud.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains regions list and returns a String array of all regions. 
 * @author neelam.kapoor
 *
 */
public enum Regions {
	LON, 
	SW_ENG, 
	NE_ENG, 
	NW_ENG_N_WAL, 
	SC_ENG, 
	C_SCO, 
	E_ENG, 
	N_SCO, 
	S_WAL, 
	N_IRE;
	
	/**
	 * Returns a String array of regions.
	 */
	public static List<String> getAllRegions(){
    List<String> regionList = new ArrayList<>();
    for(Regions temp : Regions.values()){
      regionList.add(temp.name());
    }
    return regionList;
  }
}