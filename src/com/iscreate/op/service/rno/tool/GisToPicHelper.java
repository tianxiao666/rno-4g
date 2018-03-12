package com.iscreate.op.service.rno.tool;

import com.iscreate.op.service.rno.vo.GisArea;
import com.iscreate.op.service.rno.vo.GisPoint;
import com.iscreate.op.service.rno.vo.PicArea;
import com.iscreate.op.service.rno.vo.PicPoint;

public class GisToPicHelper {

	GisArea gisArea;
	PicArea picArea;
	public GisArea getGisArea() {
		return gisArea;
	}
	public void setGisArea(GisArea gisArea) {
		this.gisArea = gisArea;
	}
	public PicArea getPicArea() {
		return picArea;
	}
	public void setPicArea(PicArea picArea) {
		this.picArea = picArea;
	}
	
	boolean hasBuild=false;
	double ratio=0;
	
	double gis2PicScaleX,gis2PicScaleY;
	double pic2GisScaleX,pic2GisScaleY;
	public boolean build(GisArea gisArea,PicArea picArea){
//		if(Math.abs((gisArea.getLngLatRatio()-picArea.getWHRatio()))>0.001){
//			System.err.println("picArea与gisArea的比例不匹配！相差="+Math.abs((gisArea.getLngLatRatio()-picArea.getWHRatio())));
//			return false;
//		}
		hasBuild=true;
		this.gisArea=gisArea;
		double h=picArea.getWidth()/gisArea.getLngLatRatio();
		picArea.setHeight((int)h);
		this.picArea=picArea;
		
		gis2PicScaleX=picArea.getWidth()/gisArea.getLngRange();
		gis2PicScaleY=picArea.getHeight()/gisArea.getLatRange();
		
		pic2GisScaleX=gisArea.getLngRange()/picArea.getWidth();
		pic2GisScaleY=gisArea.getLatRange()/picArea.getHeight();
		return true;
	}
	
	public PicPoint gisPoint2PicPoint(GisPoint gpoint){
		if(!hasBuild){
			return null;
		}
		double x=(gpoint.getLng()-gisArea.getMinLng())*gis2PicScaleX;
		double y=Math.abs((gpoint.getLat()-gisArea.getMaxLat()))*gis2PicScaleY;

		return new PicPoint(x,y);
	}
	
	public GisPoint picPoint2GisPoint(PicPoint pp){
		if(!hasBuild){
			return null;
		}
		
		double lng=pp.getX()*pic2GisScaleX+gisArea.getMinLng();
		double lat=gisArea.getMaxLat()-pp.getY()*pic2GisScaleY;
		
		return new GisPoint(lng,lat);
	}
	
	public static void main(String[] args) {
		int w=1024;
		int h=836;
		
		PicArea picArea=new PicArea();
		picArea.setWidth(w);
		picArea.setHeight(h);
		//System.out.println(picArea.getWHRatio());
		
		GisArea gisArea=new GisArea();
		gisArea.setMaxLat(23.13*3600);
		gisArea.setMinLat(22.33*3600);
		gisArea.setMinLng(113.58*3600);
		gisArea.setMaxLng(114.56*3600);
		
		//System.out.println(gisArea.getLngLatRatio());
		//System.out.println(w/gisArea.getLngLatRatio());
		
		GisToPicHelper gtp=new GisToPicHelper();
		gtp.build(gisArea, picArea);
		
		//----------------------//
		double lng=113.58*3600;
		double lat=23.13*3600;
		GisPoint gp=new GisPoint(lng,lat);
		PicPoint pp=gtp.gisPoint2PicPoint(gp);
		//System.out.println("gp="+gp+" to pp="+pp);
		
		gp.setLng(114.56*3600);
		gp.setLat(22.33*3600);
		pp=gtp.gisPoint2PicPoint(gp);
		//System.out.println("gp="+gp+" to pp="+pp);
		
		gp=gtp.picPoint2GisPoint(pp);
		//System.out.println(" pp="+pp+" to gp="+gp.getLng()/3600+","+gp.getLat()/3600);
	}
}
