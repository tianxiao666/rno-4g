package com.iscreate.plat.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 经纬度计算的类
 * 
 * @author zhang_jh1
 * 
 */
public class LatLngHelper {
	// 地球的平均半径，单位为米
	private final static double R = 6371000;
	
	/**
	 * 从指定经纬度向东移动指定距离后的经度坐标 (向西，则距离变为负数即可)
	 * 
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @param distance
	 *            向东移动的距离
	 * @return 移动后的经度坐标
	 */
	public static double moveEast(double lat,double lng,long distance){
		double differenceOfLongitude = Math.cos(lat/360)*(distance*180)/(Math.PI*R);
		double lng2 = lng+differenceOfLongitude;
		// 坐标计算越界后的处理
		if(lng2>180||lng2<-180){
			lng2=(lng2+180)%360-180;  
		}
		return lng2;
	}
	
	/**
	 * 从指定经纬度向北移动指定距离后的纬度坐标 (向南，则距离变为负数即可)
	 * 
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @param distance
	 *            向北移动的距离
	 * @return 移动后的纬度坐标
	 */
	public static double moveNorth(double lat,double lng,long distance){
		double differenceOfLatitude = (distance*180)/(Math.PI*R);
		double lat2 = lat+differenceOfLatitude;
/*
 * //越界未处理 if(lat2>90||lat<-90){ lat2=lat2%360;
 *  }
 */
		return lat2;
	}
	

	/**
	 * 计算gps里程(米)
	 * 
	 * @param startlong
	 * @return 里程
	 */
	public static double Distance(double startlong, double startlat, double endlong, double endlat) {
	      double a, b;
	      startlat = startlat * Math.PI / 180.0;
	      endlat = endlat * Math.PI / 180.0;
	      a = startlat - endlat;
	      b = (startlong - endlong) * Math.PI / 180.0;
	      double d;
	      double sa2, sb2;
	      sa2 = Math.sin(a / 2.0);
	      sb2 = Math.sin(b / 2.0);
	      d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(startlat) * Math.cos(endlat) * sb2 * sb2));
	      return d;
	}
	

	/**
	 * 根据经纬度数组,计算里程
	 * 
	 * @param ll
	 * @return gps里程
	 */
	public static double getDistanceByArray ( List<LatLng> ll ) {
		double sum = 0;
		for (int i = 0; i < ll.size() - 1 ; i++) {
			double distance = Distance( ll.get(i).longitude , ll.get(i).latitude, ll.get(i+1).longitude , ll.get(i+1).latitude);
			sum += distance;
		}
		return sum;
	}
	
	public static void main(String[] args) {
//		List<LatLng> list = new ArrayList<LatLng>();
//		list.add(new LatLng(113.32537500,23.08020500));
//		list.add(new LatLng(113.32661944,23.08047833));
//		list.add(new LatLng(113.32818111,23.08050944));
//		list.add(new LatLng(113.32977167,23.08057833));
//		list.add(new LatLng(113.33117167,23.08079167));
//		double gps = getDistanceByArray(list);
//		System.out.println(gps);
	}
	
	
	//经纬度对象
	public static class LatLng {
		private double longitude ;	// 经度
		private double latitude ; 	// 纬度
		
		/**
		 * @param longitude 经度
		 * @param latitude 纬度
		 */
		public LatLng(double longitude, double latitude) {
			super();
			this.longitude = longitude;
			this.latitude = latitude;
		}
		public LatLng() {
			super();
		}
		/**
		 * 经度
		 */
		public double getLongitude() {
			return longitude;
		}
		/**
		 * 纬度
		 */
		public double getLatitude() {
			return latitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		
	}
	
	
}
