package uk.co.metoffice.beans;

/**
 *  @author neelam.kapoor 
 */

import com.google.appengine.api.datastore.Entity;

import java.io.InputStream;
import java.util.List;

public class ResponseParameter {
	
	private String message;
	private Entity entity;
	private InputStream stream;
	private String fileKey;
	private List<MetaData> metaDataList;
	
	public ResponseParameter(){
		
	}
	
	public ResponseParameter(ResponseParameterBuilder responseParameterBuilder) {
    this.message = responseParameterBuilder.message;
    this.entity = responseParameterBuilder.entity;
    this.stream = responseParameterBuilder.stream;
    this.fileKey = responseParameterBuilder.fileKey;
    this.metaDataList = responseParameterBuilder.metaDataList;

	}
	public String getMessage() {
		return message;
	}

	public Entity getEntity() {
		return entity;
	}

	public InputStream getStream() {
		return stream;
	}

	public String getFileKey() {
		return fileKey;
	}

	public List<MetaData> getMetaDataList() {
		return metaDataList;
	}

	@Override
	public String toString() {
		return "ResponseParameter [message=" + message + ", entity=" + entity + "]";
	}

  public static class ResponseParameterBuilder{
    private String message;
    private Entity entity;
    private InputStream stream;
    private String fileKey;
    private List<MetaData> metaDataList;

    public ResponseParameterBuilder() {
    }

    public ResponseParameterBuilder(String message, Entity entity, InputStream stream, String fileKey, List<MetaData> metaDataList) {
      this.message = message;
      this.entity = entity;
      this.stream = stream;
      this.fileKey = fileKey;
      this.metaDataList = metaDataList;
    }


    public ResponseParameterBuilder setMessage(String message) {
      this.message = message;
      return this;
    }

    public ResponseParameterBuilder setEntity(Entity entity) {
      this.entity = entity;
      return this;
    }
    public ResponseParameterBuilder setInputStream(InputStream stream) {
      this.stream = stream;
      return this;
    }
    public ResponseParameterBuilder setFileKey(String fileKey) {
      this.fileKey = fileKey;
      return this;
    }
    public ResponseParameterBuilder setMetaDataList(List<MetaData> metaDataList) {
      this.metaDataList = metaDataList;
      return this;
    }

    public ResponseParameter build() {
      ResponseParameter responseParameter = new ResponseParameter(this);
      return responseParameter;
    }





  }
	

}
