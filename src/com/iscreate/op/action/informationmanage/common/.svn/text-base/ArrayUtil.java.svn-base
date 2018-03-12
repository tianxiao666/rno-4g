package com.iscreate.op.action.informationmanage.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;

/**
 * 集合工具类
 * @author andy
 */
public class ArrayUtil {
	
	/**
	 * 判断集合是否为空,和长度为0
	 * @param collection 判断的集合
	 * @return 为空 - true
	 */
	public static boolean isNullOrEmpty( Collection collection ) {
		return collection == null || collection.size() == 0;
	}
	
	/**
	 * 合并数组
	 * @param strs - 要合并的数组
	 * @return 合并后的数组
	 */
	public static byte[] arrayJoin( byte[] strs , byte[] strs2 ){
		byte[] data3 = new byte[strs.length+strs2.length];
	    System.arraycopy(strs,0,data3,0,strs.length);
	    System.arraycopy(strs2,0,data3,strs.length,strs2.length);
	    return data3;
	}
	
	/**
	 * 集合转数组
	 * @param <T> 泛型
	 * @param ls - 要转换的集合
	 * @return 数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray ( List<T> ls ) {
		T t = ls.get(0);
		T[] tt = (T[]) Array.newInstance(t.getClass(), ls.size()); 
		for (int i = 0; i < ls.size(); i++) {
			tt[i] = ls.get(i);
		}
		return tt;
	}
	
	/**
	 * 截取byte数组
	 * @param sbyte - 数组
	 * @param start - 开始索引
	 * @param end - 结束索引
	 * @return 截取后的数组
	 */
	public static byte[] subBytes( byte[] sbyte , int start , int end ) {
		byte[] returnByte = new byte[end - start];
		for (int i = 0; i < returnByte.length; i++) {
			returnByte[i] = sbyte[start + i];
		}
		return returnByte;
	} 
	
	/**
	 * 把list集合,转成字符串
	 * @param <T> 泛型
	 * @param separator - 分隔符
	 * @param list - 集合
	 * @return 组成后的字符串
	 */
	public static <T> String listToString ( String separator , List<T> list){
		StringBuffer str = new StringBuffer("");
		for ( int i = 0; i < list.size(); i++ ) {
			T t = list.get(i);
			String bb = t+"";
			if ( t == null || (bb).isEmpty()) {
				continue;
			}
			if ( i != 0 ) {
				str.append(separator);
			}
			str.append(t);
		}
		return str.toString();
	}
	
	/**
	 * 创建List集合
	 * @param <T> 泛型
	 * @param values - 集合元素
	 * @return 集合
	 */
	public static <T> List<T> createArrayList1 ( T... values ) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < values.length; i++) {
			T v = values[i];
			list.add(v);
		}
		return list;
	}
	
	/**
	 * 指定key值,设置map默认值
	 * @param map - 操作集合
	 * @param defaultValue - 默认值
	 * @param key - 指定的key值
	 */
	public static void setMapDefault ( Map map , String defaultValue , String... keys ) {
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String value = map.get(key)+"";
			if ( (map.get(key) == null || value.trim().isEmpty() || value.trim().equals("null")) ) {
				map.put(key, defaultValue);
			}
		}
	}
	
	public static void removeEmpty ( Map map ) {
		for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
			String key = it.next();
			Object value = map.get(key);
			if ( value == null || "null".equalsIgnoreCase(value+"") ) {
				it.remove();
				map.remove(key);
			} else {
				String param = value+"";
				if ( param.isEmpty() || param.equalsIgnoreCase("null") ) {
					it.remove();
					map.remove(key);
				}
			}
		}
	}
	
	/**
	 * set集合转list集合
	 * @param <T> 泛型
	 * @param set - set集合
	 * @return list集合
	 */
	public static <T> List<T> set2List ( Set<T> set ) {
		List<T> list = new ArrayList<T>();
		list.addAll(set);
		return list;
	}
	
	/**
	 * 集合筛选
	 * @param <T> 
	 * @return
	 */
	public static <T> List<T> listFilter ( Collection<T> list , ArrayUtilCallBack callBack ) {
		for (int i = 0; i < list.size() ; i++) {
			list.getClass();
		}
		return null;
	}
	
	
	
	public static interface ArrayUtilCallBack {
		public boolean execute( T t );
	}
	
}
