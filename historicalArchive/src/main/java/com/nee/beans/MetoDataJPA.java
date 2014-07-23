package com.nee.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MetoDataJPA {

	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	private String date;
	private String regions;
	private String temperature;
	private String pressure;
	
	public MetoDataJPA(){
		
	}
	
	public MetoDataJPA(String date, String regions,
			String temperature, String pressure) {
		
		this.date = date;
		this.regions = regions;
		this.temperature = temperature;
		this.pressure = pressure;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getRegions() {
		return regions;
	}
	public void setRegions(String regions) {
		this.regions = regions;
	}
	
	@Override
	public String toString() {
		return "MetoDataJPA [id=" + id + ", date=" + date + ", regions="
				+ regions + ", temperature=" + temperature + ", pressure="
				+ pressure + "]";
	}
		
		
}
