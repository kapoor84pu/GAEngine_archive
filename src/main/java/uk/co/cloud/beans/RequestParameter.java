package uk.co.cloud.beans;

import uk.co.cloud.beans.xml.IngestionConfiguration;

/**
 * This class bundles parameter inside a request.
 * @author neelam.kapoor
 *
 */

public class RequestParameter {
	
	private String filename;
	private String fromDate;
	private String toDate;
	private String region;
	private IngestionConfiguration ingestionConfiguration;
	private MetaData metaData;
	private String clientId;
  private String day;

  public RequestParameter(RequestParameterBuilder requestParameterBuilder) {
    this.filename = requestParameterBuilder.filename;
    this.fromDate = requestParameterBuilder.fromDate;
    this.toDate = requestParameterBuilder.toDate;
    this.region = requestParameterBuilder.region;
    this.ingestionConfiguration = requestParameterBuilder.ingestionConfiguration;
    this.metaData = requestParameterBuilder.metaData;
    this.clientId = requestParameterBuilder.clientId;
    this.day = requestParameterBuilder.day;
  }

  public String getFromDate() {
		return fromDate;
	}

	public String getRegion() {
		return region;
	}

	public String getFilename() {
		return filename;
	}

	public IngestionConfiguration getIngestionConfiguration() {
		return ingestionConfiguration;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public String getToDate() {
		return toDate;
	}

	public String getClientId() {
		return clientId;
	}

  public String getday() {
    return day;
  }

  public static class RequestParameterBuilder {
    private String filename;
    private String fromDate;
    private String toDate;
    private String region;
    private IngestionConfiguration ingestionConfiguration;
    private MetaData metaData;
    private String clientId;
    private String day;

    public RequestParameterBuilder(){

    }
    public RequestParameterBuilder(String filename, String fromDate,
                                   String toDate, String region, IngestionConfiguration ingestionConfiguration,
                                   MetaData metaData, String clientId, String day) {
      this.filename = filename;
      this.fromDate = fromDate;
      this.toDate = toDate;
      this.region = region;
      this.ingestionConfiguration = ingestionConfiguration;
      this.metaData = metaData;
      this.clientId = clientId;
      this.day = day;
    }

    public RequestParameterBuilder setFilename(String filename) {
      this.filename = filename;
      return this;
    }

    public RequestParameterBuilder setFromDate(String fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public RequestParameterBuilder setToDate(String toDate) {
      this.toDate = toDate;
      return this;
    }

    public RequestParameterBuilder setRegion(String region) {
      this.region = region;
      return this;
    }
    public RequestParameterBuilder setIngestionConfiguration(IngestionConfiguration ingestionConfiguration) {
      this.ingestionConfiguration = ingestionConfiguration;
      return this;
    }

    public RequestParameterBuilder setMetaData(MetaData metaData) {
      this.metaData = metaData;
      return this;
    }

    public RequestParameterBuilder setClientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public RequestParameterBuilder setDay(String day) {
      this.day = day;
      return this;
    }

    public RequestParameter build() {
      RequestParameter requestParameter = new RequestParameter(this);
      return requestParameter;
    }

  }
}
