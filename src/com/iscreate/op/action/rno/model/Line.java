package com.iscreate.op.action.rno.model;

public class Line {

	private Point point1;
	private Point point2;

	public Line(Point point, Point point3) {
		
		 this.point1=point;
		 this.point2=point3;
	}

	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

	public Point getPoint2() {
		return point2;
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
}
