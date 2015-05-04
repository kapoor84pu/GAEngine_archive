package uk.co.cloud.beans;

import uk.co.cloud.util.DateAdapter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Class contains metadata of product files stored in bucket.
 * neelam.kapoor
 */

@Entity
public class MetaData {
	
	@Id
	private String id;
	
	private String clientId;
	private String fileType;
	private String category;
	private Date validityDate;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	@XmlElement(name = "ClientId")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getFileType(){
		return fileType;
	}
	
	@XmlElement(name = "FileType")
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getCategory() {
		return category;
	}
	
	@XmlElement(name = "Category")
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getValidityDate() {
		return validityDate;
	}
	
	@XmlElement(name = "ValidityDate")
	@XmlJavaTypeAdapter(DateAdapter.class)
	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

  public String createCompositeKey(String clientId, String category, String validDate, String fileType){
    String separator = "::";
    String key = (clientId + separator + category + separator + validDate + separator + fileType).toLowerCase();
    return key;
  }

	@Override
	public String toString() {
		return "MetaData [id=" + id + ", clientId=" + clientId + ", fileType="
				+ fileType + ", category=" + category + ", validityDate="
				+ validityDate + "]";
	}
	
	
}