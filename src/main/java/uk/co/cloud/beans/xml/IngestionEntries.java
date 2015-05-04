package uk.co.cloud.beans.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class IngestionEntries {

	private List<IngestionEntry> ingestionEntry;

	public List<IngestionEntry> getIngestionEntry() {
		return ingestionEntry;
	}

	@XmlElement(name = "IngestionEntry")
	public void setIngestionEntry(List<IngestionEntry> ingestionEntry) {
		this.ingestionEntry = ingestionEntry;
	}

	@Override
	public String toString() {
		return "IngestionEntries [ingestionEntry=" + ingestionEntry + "]";
	}


}
