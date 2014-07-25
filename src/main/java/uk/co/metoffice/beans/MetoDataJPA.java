package uk.co.metoffice.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This class contains all weather data
 * @author neelam.kapoor
 *
 */

@Entity
public class MetoDataJPA {

	@Id	
	private String id;
	private Date weatherDate;
	private String regions;
	private String temperature;
	private String pressure;
	
	public MetoDataJPA(){}
		
	public MetoDataJPA(String id, Date weatherDate, String regions,
			String temperature, String pressure) {
		this.id = id;
		this.weatherDate = weatherDate;
		this.regions = regions;
		this.temperature = temperature;
		this.pressure = pressure;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Date getWeatherDate() {
		return weatherDate;
	}

	public void setWeatherDate(Date weatherDate) {
		this.weatherDate = weatherDate;
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
		return "MetoDataJPA [id=" + id + ", weatherDate=" + weatherDate + ",regions="
				+ regions + ", temperature=" + temperature + ", pressure="
				+ pressure + "]";
	}
	
	public String getStringRepresentation(){
		return this.getWeatherDate()+","+this.getRegions()+","+this.getTemperature()+","+this.getPressure() + "\n";
	}
    
		
		
}
