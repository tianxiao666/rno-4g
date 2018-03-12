package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_UserWork implements Serializable  {
	
	private int id;
	private String workName;
	private String workLocation;
	private String workFlag;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getWorkLocation() {
		return workLocation;
	}
	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}
	public String getWorkFlag() {
		return workFlag;
	}
	public void setWorkFlag(String workFlag) {
		this.workFlag = workFlag;
	}
}
