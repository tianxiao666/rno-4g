package com.iscreate.op.service.gisdispatch;

import com.iscreate.op.pojo.gisdispatch.LatLng;
import com.iscreate.op.pojo.gisdispatch.LatLngBounds;
import com.iscreate.plat.tools.LatLngHelper;


public class DoubleBackgroundLatLngCalculator implements
		BackgroundLatLngCalculator {

	public LatLngBounds calculate(LatLngBounds windowLatLngRange) {
		if(windowLatLngRange==null){
			return null;
		}
		
		LatLng sw=windowLatLngRange.getSw();
		LatLng ne=windowLatLngRange.getNe();
		
		double width=ne.getLongitude()-sw.getLongitude();
		double height=ne.getLatitude()-sw.getLatitude();
		
		
		LatLng bsw=new LatLng();
		bsw.setLatitude(sw.getLatitude()-height/2);
		bsw.setLongitude(sw.getLongitude()-width/2);
		
		LatLng bne=new LatLng();
		bne.setLatitude(ne.getLatitude()+height/2);
		bne.setLongitude(ne.getLongitude()+width/2);
		
		return new LatLngBounds(sw,ne);
	}
	
	/**
	 * 计算中心点范围
	 * @param centerLatLng 中心经纬度
	 * @return
	 */
	public LatLngBounds calculate(LatLng centerLatLng){
		Double latitude = centerLatLng.getLatitude();
	    Double longitude = centerLatLng.getLongitude();
	
	    LatLng bsw=new LatLng();
		bsw.setLatitude(latitude);
		bsw.setLongitude(longitude);
		
		LatLng bne=new LatLng();
		bne.setLatitude(latitude);
		bne.setLongitude(longitude);
	    
	    long distance = 2000;
	    
	    double lng2 = LatLngHelper.moveEast(bne.getLatitude(), bne.getLongitude(), distance);
	    bne.setLongitude(lng2);
	    double lat2 = LatLngHelper.moveNorth(bne.getLatitude(), bne.getLongitude(), distance);
	    bne.setLatitude(lat2);
	    
	    double lng3 = LatLngHelper.moveEast(bsw.getLatitude(), bsw.getLongitude(), -distance);
	    bsw.setLongitude(lng3);
	    double lat3 = LatLngHelper.moveNorth(bsw.getLatitude(), bsw.getLongitude(), -distance);
	    bsw.setLatitude(lat3);
	    
//		System.out.println("CL:"+centerLatLng);
//		System.out.println("bsw:"+bsw);
//		System.out.println("bne:"+bne);
		
		return new LatLngBounds(bsw,bne);
	}
}
