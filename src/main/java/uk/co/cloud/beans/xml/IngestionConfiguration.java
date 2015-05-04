package uk.co.cloud.beans.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JDO-annotated model class for storing movie properties; movie's promotional
 * image is stored as a Blob (large byte array) in the image field.
 */
@XmlRootElement(name = "IngestionConfiguration")
public class IngestionConfiguration {


	private IngestionEntries ingestionEntries;

	public IngestionEntries getIngestionEntries() {
		return ingestionEntries;
	}
	
	@XmlElement(name = "IngestionEntries")
	public void setIngestionEntries(IngestionEntries ingestionEntries) {
		this.ingestionEntries = ingestionEntries;
	}

	@Override
	public String toString() {
		return "MetoFile [ingestionEntries=" + ingestionEntries + "]";
	}
	
}
