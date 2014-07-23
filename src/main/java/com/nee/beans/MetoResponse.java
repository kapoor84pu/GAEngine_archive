package com.nee.beans;

/**
 * This class gives a response that includes message and entity.
 *  @author neelam.kapoor 
 */

import com.google.appengine.api.datastore.Entity;

public class MetoResponse {
	private String message;
	private Entity entity;
	
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
	@Override
	public String toString() {
		return "MetoResponse [message=" + message + ", entity=" + entity + "]";
	}
	
	

}
