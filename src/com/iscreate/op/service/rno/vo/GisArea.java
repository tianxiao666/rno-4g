package com.iscreate.op.service.rno.vo;

public class GisArea {

	double maxLng;//秒
	double minLng;//秒
	double maxLat;//秒
	double minLat;//秒
	public double getMaxLng() {
		return maxLng;
	}
	public void setMaxLng(double maxLng) {
		this.maxLng = maxLng;
	}
	public double getMinLng() {
		return minLng;
	}
	public void setMinLng(double minLng) {
		this.minLng = minLng;
	}
	public double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}
	public double getMinLat() {
		return minLat;
	}
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}
	
	public double getLngRange(){
		return Math.abs(maxLng-minLng);
	}
	
	public double getLatRange(){
		return Math.abs(maxLat-minLat);
	}
	
	//经度，维度比例
	public double getLngLatRatio(){
		return getLngRange()/getLatRange();
	}
	@Override
	public String toString() {
		return "GisArea [maxLng=" + maxLng + ", minLng=" + minLng + ", maxLat="
				+ maxLat + ", minLat=" + minLat + "]";
	}
	
	
}
