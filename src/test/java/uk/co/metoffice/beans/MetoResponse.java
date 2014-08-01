package uk.co.metoffice.beans;

/**
 * This class gives a response that includes message and entity.
 *  @author neelam.kapoor 
 */

import java.io.InputStream;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

public class MetoResponse {
	
	private String message;
	private Entity entity;
	private InputStream stream;
	private String filekey;
	private List<MetaData> metaDataList;
	
	public MetoResponse(){
		
	}
	
	public MetoResponse(String message, Entity entity) {
		super();
		this.message = message;
		this.entity = entity;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	
	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	public String getFilekey() {
		return filekey;
	}

	public void setFilekey(String filekey) {
		this.filekey = filekey;
	}

	public List<MetaData> getMetaDataList() {
		return metaDataList;
	}

	public void setMetaDataList(List<MetaData> metaDataList) {
		this.metaDataList = metaDataList;
	}

	@Override
	public String toString() {
		return "MetoResponse [message=" + message + ", entity=" + entity + "]";
	}
	
	

}
