package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_LittleIconSetting implements Serializable  {
	
	private int id;
	private int showOrNot;
	private int littleicon_id;
	private String userId;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShowOrNot() {
		return showOrNot;
	}
	public void setShowOrNot(int showOrNot) {
		this.showOrNot = showOrNot;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getLittleicon_id() {
		return littleicon_id;
	}
	public void setLittleicon_id(int littleicon_id) {
		this.littleicon_id = littleicon_id;
	}
	
}
