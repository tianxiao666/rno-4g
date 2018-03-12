package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_GraphElementTypeAndMileSetting implements Serializable  {
	
	private int id;
	private double minVisibleMile;
	private double maxVisibleMile;
	private int geType_id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getMinVisibleMile() {
		return minVisibleMile;
	}
	public void setMinVisibleMile(double minVisibleMile) {
		this.minVisibleMile = minVisibleMile;
	}
	public double getMaxVisibleMile() {
		return maxVisibleMile;
	}
	public void setMaxVisibleMile(double maxVisibleMile) {
		this.maxVisibleMile = maxVisibleMile;
	}
	public int getGeType_id() {
		return geType_id;
	}
	public void setGeType_id(int geType_id) {
		this.geType_id = geType_id;
	}
	
	
}
