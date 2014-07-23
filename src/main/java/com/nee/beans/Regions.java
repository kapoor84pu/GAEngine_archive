package com.nee.beans;

import java.util.List;

import com.google.common.collect.Lists;


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
	
	
	public static String[] getAllRegions(){
		
		List<String> allRegions = Lists.newArrayList();
		for (Regions level : Regions.values()){
			allRegions.add(String.valueOf(level));
		}
		return convertListIntoStringArray(allRegions);
	}
	
	public static String[] convertListIntoStringArray(List<String> list){
		String list1[] = new String[list.size()];
		list1 = list.toArray(list1);
		return list1;
	}	
}