package com.nee.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	
	public void readCSV() throws IOException{
		 BufferedReader CSVFile = new BufferedReader(new FileReader("weather.csv"));
		try {
		//	CSVFile = new BufferedReader(new FileReader("Sub-Companies.csv"));
			String dataRow = CSVFile.readLine();
		      // Read the number of the lines in .csv file 
		      // i = row of the .csv file
		      int i = 0; 
		      while (dataRow != null){
		          i++;
		          dataRow = CSVFile.readLine();

		        }
		      System.out.println(i);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 // Close the file once all data has been read.
			CSVFile.close();
			
		}

		      // End the printout with a blank line.
		      System.out.println();

	}
}
