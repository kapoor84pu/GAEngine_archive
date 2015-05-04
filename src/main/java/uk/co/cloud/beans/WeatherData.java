package uk.co.cloud.beans;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * This class contains all weather data
 * @author neelam.kapoor
 *
 */

@Entity
public class WeatherData {

	@Id
  @JsonIgnore
	private String id;

  @JsonProperty
	private Date weatherDate;

  @JsonProperty
  private String day;

  @JsonProperty
	private String regions;

  @JsonProperty
	private String temperature;

  @JsonProperty
	private String pressure;

  @JsonIgnore
	private String clientId;
	
	public WeatherData(){}
		
	public WeatherData(String id, Date weatherDate, String day, String regions,
                     String temperature, String pressure, String clientId) {
		this.id = id;
		this.weatherDate = weatherDate;
    this.day = day;
		this.regions = regions;
		this.temperature = temperature;
		this.pressure = pressure;
		this.clientId = clientId;
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

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
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
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	
	@Override
	public String toString() {
		return "WeatherData [id= " + id + ", weatherDate= " + weatherDate + ",day= "
				+ day + ",regions= " + regions + ", temperature= " + temperature + ", pressure= "
				+ pressure + "clientId= " + clientId +  "]";
	}

  @JsonIgnore
  public String getStringRepresentation(){
		return this.getWeatherDate()+","+this.getRegions()+","+this.getTemperature()+","+this.getPressure() + "\n";
	}
    
		
		
}
