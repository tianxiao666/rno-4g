package com.iscreate.op.service.rno.vo;

public class PicPoint {

	int x;
	int y;
	public PicPoint(double x2, double y2) {
		this.x=(int)x2;
		this.y=(int)y2;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "PicPoint [x=" + x + ", y=" + y + "]";
	}
	
}
