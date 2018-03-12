package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_GraphLayer implements Serializable  {
	
	private int id;
	private String name;
	private int superId;
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSuperId() {
		return superId;
	}
	public void setSuperId(int superId) {
		this.superId = superId;
	}
	
	
}
