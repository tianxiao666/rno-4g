package com.iscreate.op.service.rno.vo;

public class PicArea {

	int width;
	int height;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	//计算宽高比
	public double getWHRatio(){
		return width*1.0/height*1.0;
	}

	@Override
	public String toString() {
		return "PicArea [width=" + width + ", height=" + height + "]";
	}
	
	
}
