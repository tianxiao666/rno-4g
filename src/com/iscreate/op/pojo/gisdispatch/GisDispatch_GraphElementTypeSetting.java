package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_GraphElementTypeSetting implements Serializable  {
	
	private int id;
	private String userId;
	private int isVisible;
	private int geType_id;
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getIsVisible() {
		return isVisible;
	}
	public void setIsVisible(int isVisible) {
		this.isVisible = isVisible;
	}
	public int getGeType_id() {
		return geType_id;
	}
	public void setGeType_id(int geType_id) {
		this.geType_id = geType_id;
	}
}
