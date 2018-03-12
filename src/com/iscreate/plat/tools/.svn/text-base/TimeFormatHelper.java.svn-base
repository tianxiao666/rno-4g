package com.iscreate.plat.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatHelper {
	
	/**
	 * 该时间转换的格式为：yyyy-MM-dd
	 * @param beforeTime
	 * @return 假如返回出来的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static String getTimeFormatByDay(Object beforeTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return changeTime(beforeTime,sdf);
	}
	
	/**
	 * 该时间转换的格式为：yyyy-MM-dd HH:mm:SS
	 * @param beforeTime
	 * @return 假如返回出来的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static String getTimeFormatBySecond(Object beforeTime){
		SimpleDateFormat sdf=null;
		if(beforeTime!=null && !"".equals(beforeTime)){
			sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//判断beforeTime是否为Date类型，如果>-1表示为Date类型
//			if(beforeTime.getClass().getName().indexOf("Date") > -1){
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			}else{
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			}
		}
		return changeTime(beforeTime,sdf);
	}
	
	/**
	 * 该时间转换的格式为：yyyy-MM-dd HH:mm
	 * @param beforeTime
	 * @return 假如返回出来的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static String getTimeFormatByMin(Object beforeTime){
		SimpleDateFormat sdf=null;
		if(beforeTime!=null && !"".equals(beforeTime)){
			sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//判断beforeTime是否为Date类型，如果>-1表示为Date类型
//			if(beforeTime.getClass().getName().indexOf("Date") > -1){
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			}else{
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			}
		}
		return changeTime(beforeTime,sdf);
	}
	
	/**
	 * 该时间转换的格式为：yyyy-MM-dd HH:mm:SS E
	 * @param beforeTime
	 * @return 假如返回出来的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static String getTimeFormatByWeek(Object beforeTime){
		SimpleDateFormat sdf=null;
		if(beforeTime!=null){
			sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
			//判断beforeTime是否为Date类型，如果>-1表示为Date类型
//			if(beforeTime.getClass().getName().indexOf("Date") > -1){
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:SS E");
//			}else{
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
//			}
		}
		return changeTime(beforeTime,sdf);
	}
	
	/**
	 * 根据timeFormat定义输出的时间格式
	 * @param beforeTime
	 * @param timeFormat
	 * @return
	 */
	public static String getTimeFormatByFree(Object beforeTime,String timeFormat){
		SimpleDateFormat sdf=null;
		if(beforeTime!=null && !beforeTime.equals("null") && !beforeTime.equals("")){
			sdf= new SimpleDateFormat(timeFormat);
			//判断beforeTime是否为Date类型，如果>-1表示为Date类型
//			if(beforeTime.getClass().getName().indexOf("Date") > -1){
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:SS E");
//			}else{
//				sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
//			}
		}
		return changeTime(beforeTime,sdf);
	}
	
	/**
	 * 保存时间格式为： yyyy-MM-dd HH:mm:ss
	 * @param beforeTime
	 * @return 假如返回出去的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static Date setTimeFormat(Object beforeTime){
		Date afterTime=null;
		try {
			if(beforeTime != null && !beforeTime.equals("null") && !beforeTime.equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strType=beforeTime.getClass().getName();
				if(strType.indexOf("Date")>-1){	//假如传入的参数是Date类型
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else if(strType.indexOf("String")>-1 && !"".equals(beforeTime.toString())){ 	//假如传入的参数是String类型
					afterTime=sdf.parse(beforeTime.toString());
				}else if(strType.indexOf("Timestamp")>-1){
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else{
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return afterTime;
	}
	
	/**
	 * 保存时间格式为： yyyy-MM-dd
	 * @param beforeTime
	 * @return 假如返回出去的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static Date setTimeFormatByDay(Object beforeTime){
		Date afterTime=null;
		try {
			if(beforeTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String strType=beforeTime.getClass().getName();
				if(strType.indexOf("Date")>-1){	//假如传入的参数是Date类型
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else if(strType.indexOf("String")>-1 && !"".equals(beforeTime.toString())){ 	//假如传入的参数是String类型
					afterTime=sdf.parse(beforeTime.toString());
				}else if(strType.indexOf("Timestamp")>-1){
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else{
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return afterTime;
	}
	
	/**
	 * 根据timeFormat定义转换的时间
	 * @param beforeTime
	 * @param timeFormat
	 * @return
	 */
	public static Date setTimeFormatByFree(Object beforeTime,String timeFormat){
		Date afterTime=null;
		try {
			if(beforeTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
				String strType=beforeTime.getClass().getName();
				if(strType.indexOf("Date")>-1){	//假如传入的参数是Date类型
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else if(strType.indexOf("String")>-1 && !"".equals(beforeTime.toString())){ 	//假如传入的参数是String类型
					afterTime=sdf.parse(beforeTime.toString());
				}else if(strType.indexOf("Timestamp")>-1){
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else{
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return afterTime;
	}
	
	private static String changeTime(Object beforeTime,SimpleDateFormat sdf){
		String afterTime = null;
		try {
			if(beforeTime != null && !"".equals(beforeTime)){
				String str = beforeTime.getClass().getName(); 	//获取beforeTime的类型
				if(str.indexOf("Date") == -1) { 	//假如beforeTime不是Date类型
					if(str.indexOf("String") > -1){ 	//假如beforeTime是String类型
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						afterTime = sdf.format(sdf1.parse(beforeTime.toString()));
					}else if(str.indexOf("java.sql.Timestamp")>-1){
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						afterTime = sdf.format(sdf1.parse(beforeTime.toString()));
					}
					else{ 	//假如beforeTime不是Date类型也不是String类型
						return afterTime;
					}
				}else{ 	//假如beforeTime是Date类型
					afterTime = sdf.format(beforeTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return afterTime;
		}
		return afterTime;
	}
}
