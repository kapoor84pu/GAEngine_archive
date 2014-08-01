package uk.co.metoffice.beans;

import java.util.List;

import com.google.common.collect.Lists;

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
	 * @return
	 */
	public static List<String> getAllRegions(){
		
		List<String> allRegions = Lists.newArrayList();
		for (Regions level : Regions.values()){
			allRegions.add(String.valueOf(level));
		}
		return allRegions;
	}
	
	public static String[] convertListIntoStringArray(List<String> list){
		String list1[] = new String[list.size()];
		list1 = list.toArray(list1);
		return list1;
	}	
}