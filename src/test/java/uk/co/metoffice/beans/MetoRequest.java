package uk.co.metoffice.beans;

import uk.co.metoffice.beans.xml.IngestionConfiguration;

/**
 * This class takes fromDate and region parameters and create a request.
 * @author neelam.kapoor
 *
 */

public class MetoRequest {
	
	private String filename;
	private String fromDate;
	private String toDate;
	private String region;
	private IngestionConfiguration ingestionConfiguration;
	private MetaData metaData;
	private String clientId;

	public MetoRequest(){
		
	}
	
	public MetoRequest(String fromDate, String toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public MetoRequest(String fromDate, String toDate, String region, String clientId) {
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.region = region;
		this.clientId = clientId;
	}
	public MetoRequest(String fromDate, String region, String clientId) {
		this.fromDate = fromDate;
		this.region = region;
		this.clientId = clientId;
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public IngestionConfiguration getIngestionConfiguration() {
		return ingestionConfiguration;
	}

	public void setIngestionConfiguration(
			IngestionConfiguration ingestionConfiguration) {
		this.ingestionConfiguration = ingestionConfiguration;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
