package com.iscreate.op.action.informationmanage.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述:此类为日期工具类
 * 
 * @author pure
 */
public class DateUtil {
	

	private static SimpleDateFormat[] sdfarray = {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") , 
			new SimpleDateFormat("yyyy-MM-dd HH:mm") , 
			new SimpleDateFormat("yyyy-MM-dd HH") , 
			new SimpleDateFormat("yyyy-MM-dd") , 
			new SimpleDateFormat("yyyy-MM") , 
			new SimpleDateFormat("yyyy") , 
			new SimpleDateFormat("MM-dd HH:mm")
	};

	private static Map<String,SimpleDateFormat> sdfs = new LinkedHashMap<String, SimpleDateFormat>();
	
	static {
		for (int i = 0; i < sdfarray.length ; i++) {
			String key = sdfarray[i].toPattern();
			sdfs.put(key, sdfarray[i]);
		}
	}
	
	/**
	 * 格式化日期字符串
	 * @param dateString - 日期字符串
	 * @param pattern - 日期格式
	 * @return 格式化后的字符串
	 */
	public static String formatDateString ( String dateString , String pattern ) {
		SimpleDateFormat sdf = null;
		if ( sdfs.containsKey(pattern) ) {
			sdf = sdfs.get(pattern);
		} else {
			sdf = new SimpleDateFormat(pattern);
		}
		try {
			Date parse = sdf.parse(dateString);
			dateString = sdf.format(parse);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}
	
	
	/**
	 * 格式化日期成字符串
	 * @param date - 操作日期
	 * @param pattern - 格式
	 * @return 转换后的字符串
	 */
	public static String formatDate ( Date date , String pattern ) {
		SimpleDateFormat sdf = sdfs.get(pattern);
		String format = sdf.format(date);
		return format;
	}
	
	/**
	 * 功能：得到当前日期 格式为：yyyy-MM-dd (eg: 2011-11-08)
	 * @return String 当前日期字符串
	 */
	public static String getTodayDate() {
		String strY = null;
		String strZ = null;
		Calendar localTime = Calendar.getInstance();
		int  x = localTime.get(Calendar.YEAR);
		int y = localTime.get(Calendar.MONTH) + 1;
		int z = localTime.get(Calendar.DATE);
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		strZ = z >= 10 ? String.valueOf(z) : ("0" + z);
		return x + "-" + strY + "-" + strZ;
	}
	
	/**
	 * 根据日期字符串 , 获取日期
	 * @param yearMonth 年月
	 * @return
	 * @throws ParseException
	 */
	public static int getDateFromYearMonthDay(String yearMonthday) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date inputDate  = sdf.parse(yearMonthday.trim());	
		cal.setTime(inputDate);
		return cal.get(Calendar.DATE);
	}
	

	
	/**
	 * 日期集合转换日期字符串
	 * @param date_list 日期集合
	 * @param pattern 转换字符串的格式
	 * @return 日期字符串集合
	 */
	public static List<String> changeDate2StringForList( List<Date> date_list , String pattern){
		List<String> string_list = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		for (int i = 0; i < date_list.size(); i++) {
			Date date = date_list.get(i);
			String format = sdf.format(date);
			string_list.add(format);
		}
		return string_list;
	}

	/**
	 * 功能：得到某月份月底格式为：yyyy-MM-dd (eg: 2011-11-30)
	 * @return String 
	 * @throws ParseException 
	 */
	public static String getMonthEnd(String yearMonth) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date inputDate  = sdf.parse(yearMonth.trim());	
		Calendar cal=Calendar.getInstance();
		cal.setTime(inputDate);
		String strZ = null;
		boolean leap = false;
		int x =cal.get(Calendar.YEAR);
		int y =cal.get(Calendar.MONTH)+1;
		if (y == 1 || y == 3 || y == 5 || y == 7 || y == 8 || y == 10
				|| y == 12) {
			strZ = "31";
		}
		if (y == 4 || y == 6 || y == 9 || y == 11) {
			strZ = "30";
		}
		if (y == 2) {
			leap = DateUtil.leapYear(x);
			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}		
		return yearMonth+ "-" + strZ;
	}
	
	

	/**
	 * 功能：判断输入年份是否为闰年
	 * @param year
	 * @return 是：true 否：false
	 * @author pure
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}
	
	/**
	 * 根据日期字符串,获取明天字符串
	 * @param date 日期字符串
	 * @return 次日字符串
	 * @throws ParseException
	 */
	public static String getNextDate(String date) throws ParseException{
		String nextDate = null;
		if(date==null||"".equals(date)) return null;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long dif = df.parse(date).getTime()+86400*1000;
        Date date1=new Date();
        date1.setTime(dif);      
        nextDate = df.format(date1);
        return nextDate;
	}
	
	
	/**
	 * 根据开始、结束时间,获取该区间内的时间列表
	 * @param fromDate 开始时间
	 * @param endDate 结束时间
	 * @return 时间列表
	 * @throws ParseException 
	 */
	public static List<String> getDateListByFromEndDateListWithString ( String fromString , String endString ) throws ParseException {
		List<String> date_List = new ArrayList<String>();
		SimpleDateFormat sdf = sdfs.get("yyyy-MM-dd");
		Date fromDate = sdf.parse(fromString);
		Date endDate = sdf.parse(endString);
		if ( fromDate.compareTo(endDate) == 0 ) {
			return date_List;
		} else if ( fromDate.compareTo(endDate) > 0 ) {
			return date_List;
		}
		while ( fromDate.before(endDate)) {
			fromDate.setDate(fromDate.getDate()+1);
			date_List.add(sdf.format(fromDate));
		}
		return date_List;
	}
	
	/**
	 * 格式化日期字符串
	 * @param dateString 日期字符串
	 * @return 返回合适的日期
	 */
	public static Date parseDateByString ( String dateString ) {
		Date date = null;
		for (Iterator<String> it = sdfs.keySet().iterator();it.hasNext();) {
			String key = it.next();
			SimpleDateFormat sdf = sdfs.get(key);
			try {
				date = sdf.parse(dateString);
				break;
			} catch (Exception e) {
				continue;
			}
		}
		return date;
	}
	
	/**
	 * 比较日期大小
	 * @param fromDate 起始日期
	 * @param endDate 结束日期
	 * @return  endDate > fromDate = 1 , 相反 -1 , 相等 0
	 */
	public static int compareTo ( Date fromDate , Date endDate ) {
		if ( fromDate == null || endDate == null ) {
			throw new RuntimeException("参数日期不能为空");
		}
		int flag = 0;
		long fromTime = fromDate.getTime();
		long endTime = endDate.getTime();
		if ( endTime > fromTime ) {
			flag = 1;
		} else if ( endTime < fromTime ) {
			flag = -1;
		} else {
			flag = 0;
		}
		return flag;
	}
	
	/**
	 * 相差小时
	 * @param fromDate 起始日期
	 * @param endDate 结束日期
	 * @return 相隔小时
	 */
	public static double diffHour ( Date fromDate , Date endDate ) {
		long result = diffCurrentTime(fromDate, endDate);
		double diff = time2Hour(result);
		return diff;
	}
	
	/**
	 * 相差毫秒
	 * @param fromDate 起始日期
	 * @param endDate 结束日期
	 * @return 相隔小时
	 */
	public static long diffCurrentTime ( Date fromDate , Date endDate ) {
		if ( fromDate == null || endDate == null ) {
			return 0;
		}
		double diff = 0;
		long endTime = endDate.getTime();
		long fromTime = fromDate.getTime();
		long result = endTime - fromTime;
		return result;
	}
	
	/**
	 * 毫秒转小时
	 * @param time 毫秒
	 * @return 小时
	 */
	public static double time2Hour ( long time ) {
		double t = 1000 * 60 * 60 ;
		double result = time / t;
		int num = (int)(result*10);
		result = (num)/10.0;
		return result;
	}
	
	/**
	 * 毫秒转小时
	 * @param time 毫秒
	 * @return 小时
	 */
	public static double time2Date ( long time ) {
		double t = 1000 * 60 * 60 * 24 ;
		double result = time / t;
		int num = (int)(result*10);
		result = (num)/10.0;
		return result;
	}
	
	
	
}
