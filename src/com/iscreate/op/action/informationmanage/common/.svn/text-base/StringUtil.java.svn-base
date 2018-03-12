package com.iscreate.op.action.informationmanage.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 * @author andy
 */
public class StringUtil {
	
	/**
	 * 数组合并为字符串
	 * @param array - 要操作的数组
	 * @param plot - 分隔的字符
	 * @param ignore - 忽略的相应索引的数组对象
	 */
	public static String join ( Object[] array , String plot , int ignore ) {
		String txt = "" ;
		for (int i = 0; i < array.length; i++) {
			if ( ignore == i ) {
				continue;
			}
			txt += array[i] + plot ;
		}
		return txt;
	}
	
	/**
	 * 从左边截取字符串
	 * @param str - 操作的字符串
	 * @param count - 截取的个数
	 * @return 截取出来的字符串
	 */
	public static String left ( String str , int count ) {
		return str.substring(0, count);
	}
	
	/**
	 * 从右边截取字符串
	 * @param str - 操作的字符串
	 * @param count - 截取的个数
	 * @return 截取出来的字符串
	 */
	public static String right ( String str , int count ) {
		return str.substring(str.length() - count);
	}
	
	/**
	 * 判断是否Integer类型，并转换(如果不是Integer类型返回null)
	 * @param value - 要转换的字符串
	 * @return 转换后的值(如果不是Integer类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Integer isInteger(String value) {
		Integer num = null;
		if ( value == null || value.isEmpty() ) {
			return null;
		}
		String v = "";
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if ( c >=48 && c <= 57  ) {
				v += c;
			} else {
				break;
			}
		}
		if ( v != null && !v.isEmpty() ) {
			try {
				num = Integer.parseInt(v);
			} catch (Exception e) {
				e.printStackTrace();
				num = null;
			}
		} else {
			num = null;
		}
		return num;
	}
	

	/**
	 * 判断是否Long类型，并转换(如果不是Long类型返回null)
	 * @param value - 要转换的字符串
	 * @return 转换后的值(如果不是Long类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Long isLong(String value) {
		Long num = null;
		if ( value == null || value.isEmpty() ) {
			return null;
		}
		String v = "";
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if ( c >=48 && c <= 57  ) {
				v += c;
			} else {
				break;
			}
		}
		if ( v != null && !v.isEmpty() ) {
			try {
				num = Long.parseLong(v);
			} catch (Exception e) {
				e.printStackTrace();
				num = null;
			}
		} else {
			num = null;
		}
		return num;
	}

	/**
	 * 判断是否Double类型，并转换(如果不是Double类型返回null)
	 * @param value - 要转换的字符串
	 * @return 转换后的值(如果不是Double类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Double isDouble(String value) {
		Double num = null;
		String v = "";
		int point_count = 0;
		if ( value == null || value.isEmpty() ) {
			return null;
		}
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			//判断是否0开头
			if ( c >= 48 && c <= 57 ) {
				v += c;
			} else if ( c == 46 && point_count == 0 ) {
				v += c;
				point_count++;
			} else {
				break;
			}
		}
		if ( v != null && !v.isEmpty() ) {
			try {
				num = Double.parseDouble(v);
			} catch (Exception e) {
				e.printStackTrace();
				num = null;
			}
		} else {
			num = null;
		}
		return num;
	}

	/**
	 * 判断是否Float类型，并转换(如果不是Float类型返回null)
	 * @param value - 要转换的字符串
	 * @return 转换后的值(如果不是Float类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Float isFloat(String value) {
		Float num = null;
		String v = "";
		int point_count = 0;
		if ( value == null || value.isEmpty() ) {
			return null;
		}
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			//判断是否0开头
			if ( c >= 48 && c <= 57 ) {
				v += c;
			} else if ( c == 46 && point_count == 0 ) {
				v += c;
				point_count++;
			} else {
				break;
			}
		}
		if ( v != null && !v.isEmpty() ) {
			try {
				num = Float.parseFloat(v);
			} catch (Exception e) {
				e.printStackTrace();
				num = null;
			}
		} else {
			num = null;
		}
		return num;
	}

	/**
	 * 判断字符串是否为null或者为空
	 * @param str - 要判断的字符串
	 * @return 判断结果
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty() || str.equals("null");
	}

	
	public static List<String> split2List ( String resString , String separator ) {
		List<String> result_list = new ArrayList<String>();
		for (int i = 0; resString.indexOf(separator) != -1 ; i++) {
			int index = resString.indexOf(separator);
			String spl = resString.substring(0,index);
			resString = resString.substring(index+1);
			result_list.add(spl);
		}
		result_list.add(resString);
		return result_list;
	}
	
	/**
	 * 判断字符是否有数据库敏感字符
	 * @param str - 要判断的字符串
	 * @return
	 */
	public static boolean isDbSensitiveStringExists ( String str ){
		String[] sensitive_string = new String[]{"%","_"};
		for (int i = 0; i < sensitive_string.length; i++) {
			String sep = sensitive_string[i];
			if ( str.indexOf(sep) != -1 ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 处理数据库敏感字符
	 * @param str - 要处理的字符串
	 * @return
	 */
	public static String handleDbSensitiveString ( String str ){
		String[] sensitive_string = new String[]{"%","_","[","]"};
		for (int i = 0; i < sensitive_string.length; i++) {
			String sep = sensitive_string[i];
			for (int j = 0; j < str.length(); j++) {
				String c = str.charAt(j)+"";
				if ( c.equals(sep) ) {
					str = str.substring(0,j) + "\\" + str.substring(j);
					j++;
				}
			}
		}
		str = str.replaceAll(" ", "");
		return str;
	}
	
	
	
	
	public static void main(String[] args) {
//		String a = "1,2,3,4,5";
//		List<String> split2List = split2List(a, ",");
//		System.out.println(split2List);
//		String b = "ab%cad%[";
//		b = handleDbSensitiveString(b);
//		System.out.println(b);
//		String a = "01.1..1a";
//		Float num = isFloat(a);
//		System.out.println(num);
		String str = "%a%a %%%a";
		System.out.println(handleDbSensitiveString(str));
	}
	
	
}
