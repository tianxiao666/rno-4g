package com.iscreate.plat.networkresource.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

	/**
	 * 判断是否Integer类型，并转换(如果不是Integer类型返回null)
	 * 
	 * @param value
	 *            要转换的字符串
	 * @return 转换后的值(如果不是Integer类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Integer isInteger(String value) {
		Integer num = null;
		try {
			num = Integer.parseInt(value);
		} catch (Exception e) {
			num = null;
		} finally {
			return num;
		}
	}

	/**
	 * 判断是否Long类型，并转换(如果不是Long类型返回null)
	 * 
	 * @param value
	 *            要转换的字符串
	 * @return 转换后的值(如果不是Long类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Long isLong(String value) {
		Long num = null;
		try {
			num = Long.parseLong(value);
		} catch (Exception e) {
			num = null;
		} finally {
			return num;
		}
	}

	/**
	 * 判断是否Double类型，并转换(如果不是Double类型返回null)
	 * 
	 * @param value
	 *            要转换的字符串
	 * @return 转换后的值(如果不是Double类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Double isDouble(String value) {
		Double num = null;
		try {
			num = Double.parseDouble(value);
		} catch (Exception e) {
			num = null;
		} finally {
			return num;
		}
	}

	/**
	 * 判断是否Float类型，并转换(如果不是Float类型返回null)
	 * 
	 * @param value
	 *            要转换的字符串
	 * @return 转换后的值(如果不是Float类型返回null)
	 */
	@SuppressWarnings("finally")
	public static Float isFloat(String value) {
		Float num = null;
		try {
			num = Float.parseFloat(value);
		} catch (Exception e) {
			num = null;
		} finally {
			return num;
		}
	}

	/**
	 * 判断字符串是否为null或者为空
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return 判断结果
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}


}
