package uk.co.metoffice.beans.xml;

import javax.xml.bind.annotation.XmlElement;

public class IngestionEntry {

	private String productType;

	private String dataDate;

	private String validFrom;

	private String validTo;

	private String metaData;

	private String blobData;

	public String getProductType() {
		return productType;
	}

	@XmlElement(name = "ProductType")
	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDataDate() {
		return dataDate;
	}

	@XmlElement(name = "DataDate")
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getValidFrom() {
		return validFrom;
	}

	@XmlElement(name = "ValidFrom")
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	@XmlElement(name = "ValidTo")
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getMetaData() {
		return metaData;
	}

	@XmlElement(name = "MetaData")
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}

	public String getBlobData() {
		return blobData;
	}

	@XmlElement(name = "BlobData")
	public void setBlobData(String blobData) {
		this.blobData = blobData;
	}

	@Override
	public String toString() {
		return "IngestionEntry [productType=" + productType + ", dataDate="
				+ dataDate + ", validFrom=" + validFrom + ", validTo="
				+ validTo + ", metaData=" + metaData.length() + ", blobData=" + blobData.length()
				+ "]";
	}


}
