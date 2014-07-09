package com.nee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetoHelper {
	
	// Convert the date with the format yy/mm/dd to ddmmyyyy
	// Run a main method, with input as
	// 14/07/13, yy/mm/dd, ddmmyyyy
	public static String convertDate(String date, String fromPattern, String toPattern){
		String mdy = null;
		try {
			Date newDate = new SimpleDateFormat(fromPattern).parse(date);
			SimpleDateFormat mdyFormat = new SimpleDateFormat(toPattern);
			mdy = mdyFormat.format(newDate);
			System.out.println(mdy);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mdy;
	}
}
