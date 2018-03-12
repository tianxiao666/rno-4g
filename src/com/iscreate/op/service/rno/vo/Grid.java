package com.iscreate.op.service.rno.vo;

import java.util.ArrayList;
import java.util.List;

public class Grid {

	double leftX, upY, rightX, bottomY;
	double sumValue,maxValue,minValue;
	double minLng, maxLng, minLat, maxLat;
	double meanValueThroughIDW;
//	List<Double> values=new ArrayList<Double>();
	int cnt=0;

	public GisPoint getCenterGisPoint() {
		return new GisPoint((maxLng + minLng) / 2, (maxLat + minLat) / 2);
	}

	public double getCentX() {
		return (leftX + rightX) / 2;
	}

	public double getCentY() {
		return (upY + bottomY) / 2;
	}

	public double getMinLng() {
		return minLng;
	}

	public void setMinLng(double minLng) {
		this.minLng = minLng;
	}

	public double getMaxLng() {
		return maxLng;
	}

	public void setMaxLng(double maxLng) {
		this.maxLng = maxLng;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getSumValue() {
		return sumValue;
	}

	public void setValue(double value) {
		this.sumValue = value;
		maxValue=value;
		minValue=value;
		cnt++;
	}

	public double getLeftX() {
		return leftX;
	}

	public void setLeftX(double leftX) {
		this.leftX = leftX;
	}

	public double getUpY() {
		return upY;
	}

	public void setUpY(double upY) {
		this.upY = upY;
	}

	public double getRightX() {
		return rightX;
	}

	public void setRightX(double rightX) {
		this.rightX = rightX;
	}

	public double getBottomY() {
		return bottomY;
	}

	public void setBottomY(double bottomY) {
		this.bottomY = bottomY;
	}

	public double getWidth() {
		return Math.abs(rightX - leftX);
	}

	public double getHeight() {
		return Math.abs(bottomY - upY);
	}

	public void addValue(double value) {
		this.sumValue += value;
		if(maxValue<value){
			maxValue=value;
		}
		if(minValue>value){
			minValue=value;
		}
		cnt++;
	}
	
	public double getMeanValue(){
		return cnt==0?0:sumValue/cnt;
	}

	public double getMaxValue(){
		return maxValue;
	}
	public double getMinValue(){
		return minValue;
	}

	public boolean contains(PicPoint pp) {
		if (pp.getX() >= leftX && pp.getX() < rightX && pp.getY() >= upY
				&& pp.getY() < bottomY) {
			return true;
		}
		return false;
	}

	

	public void setSumValue(double sumValue) {
		this.sumValue = sumValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMeanValueThroughIDW() {
		return meanValueThroughIDW;
	}

	public void setMeanValueThroughIDW(double meanValueThroughIDW) {
		this.meanValueThroughIDW = meanValueThroughIDW;
	}

	@Override
	public String toString() {
		return "Grid [leftX=" + leftX + ", upY=" + upY + ", rightX=" + rightX
				+ ", bottomY=" + bottomY + ", sumValue=" + sumValue
				+ ", maxValue=" + maxValue + ", minValue=" + minValue
				+ ", minLng=" + minLng + ", maxLng=" + maxLng + ", minLat="
				+ minLat + ", maxLat=" + maxLat + ", cnt=" + cnt +"]";
	}

	public void initByArray(String[] vs) {
		this.leftX = Double.parseDouble(vs[0]);
		this.upY = Double.parseDouble(vs[1]);
		this.rightX = Double.parseDouble(vs[2]);
		this.bottomY = Double.parseDouble(vs[3]);
		this.sumValue = Double.parseDouble(vs[4]);
		this.maxValue = Double.parseDouble(vs[5]);
		this.minValue = Double.parseDouble(vs[6]);
		this.minLng = Double.parseDouble(vs[7]);
		this.maxLng = Double.parseDouble(vs[8]);
		this.minLat = Double.parseDouble(vs[9]);
		this.maxLat = Double.parseDouble(vs[10]);
		this.meanValueThroughIDW = Double.parseDouble(vs[11]);
		
	}
}
