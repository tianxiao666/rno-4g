package com.iscreate.plat.networkresource.common.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * 经纬度计算的类
 * @author zhang_jh1
 *
 */
public class LatLngConversion {
	//地球的平均半径，单位为米
	private static double R = 6378137;
	
	/**
	 * 从指定经纬度向东移动指定距离后的经度坐标
	 * (向西，则距离变为负数即可)
	 * @param lat 纬度
	 * @param lng 经度	
	 * @param distance 向东移动的距离
	 * @return 移动后的经度坐标
	 */
	public static double moveEast(double lat,double lng,double distance){
		double differenceOfLongitude = Math.cos(lat/360)*(distance*180)/(Math.PI*R);
		double lng2 = lng+differenceOfLongitude;
		//坐标计算越界后的处理
		if(lng2>180||lng2<-180){
			lng2=(lng2+180)%360-180;  
		}
		return lng2;
	}
	
	/**
	 * 从指定经纬度向北移动指定距离后的纬度坐标
	 * (向南，则距离变为负数即可)
	 * @param lat 纬度
	 * @param lng 经度
	 * @param distance 向北移动的距离
	 * @return 移动后的纬度坐标
	 */
	public static double moveNorth(double lat,double lng,double distance){
		double differenceOfLatitude = (distance*180)/(Math.PI*R);
		double lat2 = lat+differenceOfLatitude;
/*		//越界未处理
		if(lat2>90||lat<-90){
			lat2=lat2%360;

		}*/
		return lat2;
	}
	

	/**
	 * 主函数
	 * @param args
	 */
	public static void main(String[] args) {
		double lat = 23;
		double lng = 113;
		double distance = 21;
		
		double left = LatLngConversion.moveEast(lat, lng, distance);
		double right = LatLngConversion.moveEast(lat, lng, -distance);
		double top = LatLngConversion.moveNorth(lat, lng, distance);
		double bottom = LatLngConversion.moveNorth(lat, lng, -distance);
		
//		System.out.println(left+","+right+","+top+","+bottom);
	}
	
	
	
	/**
	 * 根据distance（半径）判断目标的坐标是否在原点的坐标的范围内
	 * @param oriLatitude
	 * @param oriLongitude
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @return
	 */
	public static boolean isInSpecifiedArea(double oriLatitude, double oriLongitude, double latitude,double longitude,double distance) {
		double left, right, top, bottom;
		double lat = oriLatitude;
		double lng = oriLongitude;
		double dis = distance;
		// 合法性检测
		if (oriLatitude > 90 || oriLatitude < -90 || latitude>90 || latitude<-90) {
			return false;
		} else if (oriLongitude > 180 || oriLongitude < -180 || longitude>180 || longitude<-180) {
			return false;
		} else if (distance < 0) {
			return false;
		}

		left = LatLngConversion.moveEast(lat, lng, -dis);
		right = LatLngConversion.moveEast(lat, lng, dis);
		top = LatLngConversion.moveNorth(lat, lng, dis);
		bottom = LatLngConversion.moveNorth(lat, lng, -dis);
    
	    if(latitude<=top && latitude>=bottom && longitude>=left && longitude<=right){
	    	return true;
	    }else{
	    	return false;
	    }
    }
	
	/**
	 * 根据distance（半径）判断目标的坐标是否在原点的坐标的范围内(圆形)
	 * @param oriLatitude
	 * @param oriLongitude
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @return
	 */
	public static Map<String,Object> getAreaLeftRightTopBottomByRound(double oriLatitude, double oriLongitude,double distance) {
		double left, right, top, bottom;
		double lat = oriLatitude;
		double lng = oriLongitude;
		double dis = distance;
		left = LatLngConversion.moveEast(lat, lng, -dis);
		right = LatLngConversion.moveEast(lat, lng, dis);
		top = LatLngConversion.moveNorth(lat, lng, dis);
		bottom = LatLngConversion.moveNorth(lat, lng, -dis);
		Map map = new HashMap<String, Object>();
		map.put("left", left);
		map.put("right", right);
		map.put("top", top);
		map.put("bottom", bottom);
		return map;
    }
	
	/**
	 * 根据distance（半径）判断目标的坐标是否在原点的坐标的范围内()
	 * @param oriLatitude
	 * @param oriLongitude
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @return
	 */
	public static Map getAreaLeftRightTopBottomByRing(double oriLatitude, double oriLongitude,double innerDistance,double outerDistance) {
		double innerleft, innerright, innertop, innerbottom ,outerleft,outerright,outertop,outerbottom;
		double lat = oriLatitude;
		double lng = oriLongitude;
		innerleft = LatLngConversion.moveEast(lat, lng, -innerDistance);
		innerright = LatLngConversion.moveEast(lat, lng, innerDistance);
		innertop = LatLngConversion.moveNorth(lat, lng, innerDistance);
		innerbottom = LatLngConversion.moveNorth(lat, lng, -innerDistance);
		outerleft = LatLngConversion.moveEast(lat, lng, -outerDistance);
		outerright = LatLngConversion.moveEast(lat, lng, outerDistance);
		outertop = LatLngConversion.moveNorth(lat, lng, outerDistance);
		outerbottom = LatLngConversion.moveNorth(lat, lng, -outerDistance);
		Map map = new HashMap<String, Object>();
		map.put("innerleft", innerleft);
		map.put("innerright", innerright);
		map.put("innertop", innertop);
		map.put("innerbottom", innerbottom);
		map.put("outerleft", outerleft);
		map.put("outerright", outerright);
		map.put("outertop", outertop);
		map.put("outerbottom", outerbottom);
		return map;
    }
}
