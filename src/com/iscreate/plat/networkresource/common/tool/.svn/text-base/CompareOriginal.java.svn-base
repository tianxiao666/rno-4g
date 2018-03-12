package com.iscreate.plat.networkresource.common.tool;
import java.util.Comparator;
import java.util.Map;

public class CompareOriginal implements Comparator<Map<String,Object>>{
	double longitude;
	double latitude;
	public CompareOriginal(double longitude, double latitude){                  
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public int compare(Map<String,Object> ae1, Map<String,Object> ae2) {
		
		double distance1=(Double)ae1.get("GPSsum");
		double distance2=(Double)ae2.get("GPSsum");
		
//		System.out.println("distance1 = "+distance1+" , distance2 = "+distance2);
		return distance1<distance2?-1:(distance1==distance2?0:1);
	}

	 private static double EARTH_RADIUS = 6378.137; 

	 

	    private static double rad(double d) { 

	        return d * Math.PI / 180.0; 

	    } 

	 

	    public static double getDistance(double lat1, double lng1, double lat2, 

	            double lng2) { 

	        double radLat1 = rad(lat1); 

	        double radLat2 = rad(lat2); 

	        double a = radLat1 - radLat2; 

	        double b = rad(lng1) - rad(lng2); 

	        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) 

	                + Math.cos(radLat1) * Math.cos(radLat2) 

	                * Math.pow(Math.sin(b / 2), 2))); 

	        s = s * EARTH_RADIUS; 

	        s = Math.round(s * 10000) / 10000; 

	        return s; 

	    } 


}
