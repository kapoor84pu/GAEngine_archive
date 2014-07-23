package com.nee.beans;

/**
 * This class takes fromDate and region parameters and create a request.
 * @author Admin
 *
 */

public class MetoRequest {
	private String fromDate;
	private String region;
	
	public MetoRequest(String fromDate, String region) {
		super();
		this.fromDate = fromDate;
		this.region = region;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	
	
}
