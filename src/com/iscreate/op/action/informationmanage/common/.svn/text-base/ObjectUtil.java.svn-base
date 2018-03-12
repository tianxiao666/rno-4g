package com.iscreate.op.action.informationmanage.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 对象工具类
 * @author andy
 */
public class ObjectUtil {
	
	/**
	 * 根据类,获取该类的所有属性
	 * @param clazz 操作的类
	 * @return 属性数组
	 */
	@SuppressWarnings("unchecked")
	public static String[] getClassFieldArray ( Class clazz ) {
		Field[] fields = clazz.getDeclaredFields();
		String[] fieldsName = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldsName[i] = fields[i].getName();
		}
		return fieldsName;
	}
	
	
	/**
	 * request参数集合转对象
	 * @param <T> 泛型
	 * @param map - 数据集合(key,value)任意类型
	 * @param clazz - 生成类
	 * @param objKey - 筛选条件  (Cardispatch_car#carNumber : 参数就为'Cardispatch_car#')
	 * 							为null时没有筛选条件
	 * @return 数据对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T requestMap2Object ( Map map , Class clazz , String objKey ) throws Exception {
		Map param_map = new LinkedHashMap();
		String choiceKey = objKey ;
		for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
			String key = it.next();
			if ( objKey == null ) {
				param_map.put(key, map.get(key));
			} else if ( key.indexOf(choiceKey) != -1 ) {
				String newKey = key.replace(choiceKey, "");
				param_map.put(newKey, map.get(key));
			}
		}
		T instance = map2Object( param_map , clazz );
		return instance;
	}
	
	/**
	 * 数据集合转对象
	 * @param <T> 泛型
	 * @param map - 数据集合(key,value)任意类型
	 * @param className - 生成类的全名
	 * @return 数据对象
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static <T> T map2Object ( Map map , String className ) throws Exception {
		Class<?> clazz = Class.forName(className);
		T instance = map2Object( map , clazz );
		return instance;
	}
	
	
	
	/**
	 * 数据集合转对象
	 * @param <T> 泛型
	 * @param map - 数据集合(key,value)任意类型
	 * @param clazz - 生成类
	 * @return 数据对象
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static <T> T map2Object ( Map map , Class clazz ) throws Exception {
		T instance = (T) clazz.newInstance();
		for (Iterator<String> it = map.keySet().iterator();it.hasNext();) {
			Object object = instance;
			String key = it.next();
			Object value = map.get(key);
			//值为空
			if ( value == null || (value instanceof String && (value+"").isEmpty()) ) {
				continue;
			}
			//判断.
			String[] key_arr = key.split("\\.");
			if ( key_arr != null && key_arr.length > 1 ) {
				createFieldObject(instance,key,value);
				continue;
			}
			setValue(instance, key, value);
		}
		return instance;
	}
	
	public static <T> void setValue  ( T instance , String key , Object value ) throws Exception {
		Method method = null;
		Map<String,Method> methods = getClassMethods(instance.getClass());
		String setterName = getSetterMethodNameString(key);
		method = methods.get(setterName);
		if ( method == null ) {
			return;
		}
		Class<?>[] parameterTypes = method.getParameterTypes();
		if ( parameterTypes == null || parameterTypes.length < 1 || parameterTypes[0] == null ) {
			return;
		}
		Class paramType = parameterTypes[0];
		String simpleName = paramType.getSimpleName();
		
		if ( simpleName.equals("String") ) {
			method.invoke(instance,value);
		} else if ( ( simpleName.equals("Integer") || simpleName.equals("int") ) && !(value instanceof Integer) ) {
			Integer v = Integer.valueOf(value+"");
			method.invoke(instance,v);
		} else if ( simpleName.equalsIgnoreCase("Double") && !(value instanceof Double) ) {
			Double v = Double.valueOf(value+"");
			method.invoke(instance,v);
		} else if ( (simpleName.equalsIgnoreCase("Character") || simpleName.equals("char") ) && !(value instanceof Character) ) {
			Character v = (Character)value;
			method.invoke(instance,v);
		} else if ( simpleName.equalsIgnoreCase("Float") && !(value instanceof Float) ) {
			Float v = Float.valueOf(value+"");
			method.invoke(instance,v);
		} else if ( simpleName.equalsIgnoreCase("Long") && !(value instanceof Long) ) {
			Long v = Long.valueOf(value+"");
			method.invoke(instance,v);
		} else if ( simpleName.equalsIgnoreCase("Boolean") && !(value instanceof Boolean) ) {
			Boolean v = Boolean.valueOf(value+"");
			method.invoke(instance,v);
		} else if ( simpleName.equalsIgnoreCase("Date") && value instanceof String ) {
			String[] patterns = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd HH","yyyy-MM-dd","yyyy-MM-dd","yyyy-MM","yyyy"};
			Date date = null;
			for (int i = 0; i < patterns.length; i++) {
				String pattern = patterns[i];
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				try {
					date = sdf.parse(value+"");
					break;
				} catch (Exception e) {
					continue;
				}
			}
			method.invoke(instance, date);
		} else {
			method.invoke(instance,value);
		}
	}
	
	
	public static <T> void createFieldObject ( T instance , String keys ,Object value  ) {
		try {
			if ( keys.isEmpty() ) {
				return;
			}
			Map<String,Method> methods = getClassMethods(instance.getClass());
			T obj = instance;
			String key = keys;
			String nextKey = "";
			if ( keys.indexOf(".") != -1 ) {
				key = keys.substring(0,keys.indexOf("."));
				nextKey = keys.substring(keys.indexOf(".")+1);
			}
			String setterName = getSetterMethodNameString(key);
			Method method = methods.get(setterName);
			Class<?> fieldClazz = method.getParameterTypes()[0];
			if ( fieldClazz != String.class && 
				 fieldClazz != Date.class &&
				 fieldClazz != java.sql.Date.class &&
				 fieldClazz != Integer.class &&
				 fieldClazz != Double.class &&
				 fieldClazz != Byte.class &&
				 fieldClazz != Boolean.class &&
				 fieldClazz != Float.class && 
				 fieldClazz != BigDecimal.class &&
				 fieldClazz != Long.class &&
				 fieldClazz != BigInteger.class &&
				 fieldClazz != Calendar.class &&
				 fieldClazz != Character.class &&
				 fieldClazz != Short.class &&
				 fieldClazz != Timestamp.class ) {
				Object newObj = null;
				String getterName = getGetterMethodNameString(key);
				Method getterMethod = methods.get(getterName);
				Object invoke = getterMethod.invoke(instance);
				if ( invoke != null ) {
					newObj = invoke;
				} else {
					newObj = fieldClazz.newInstance();
				}
				method.invoke(instance,newObj);
				createFieldObject(newObj,nextKey,value);
			} else {
				setValue(instance, key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个类的所有方法名
	 * @param clazz - 操作的类
	 * @return 方法map<方法名,方法对象>集合
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Method> getClassMethods ( Class clazz ) {
		Map<String,Method> methods_map = getClassMethods(clazz,null);
		return methods_map;
	}
	
	/**
	 * 获取一个类的所有方法名
	 * @param clazz - 操作的类
	 * @param topString - 筛选以topString开头名字的方法( setter 方法 : 'set' )
	 * @return 方法map<方法名,方法对象>集合
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Method> getClassMethods ( Class clazz , String topString ) {
		Map<String,Method> methods_map = new HashMap<String, Method>();
		if ( topString == null ) {
			topString = "";
		}
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if ( methodName.indexOf(topString) == 0 ) {
				methods_map.put( method.getName() , method );
			}
		}
		return methods_map;
	}
	
	/**
	 * 对象转Map集合
	 * @param <T> - 泛型
	 * @param obj -  实例
	 * @param isIncludeSuper 是否包括父类
	 * @return 数据集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String,Object> object2Map( T obj , boolean isIncludeSuper ) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String , Object> map = new LinkedHashMap<String, Object>();
		Class clazz = obj.getClass();
		List<Field> fields = getClassFieldList(clazz, isIncludeSuper);
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			String fieldName = field.getName();
			if ( fieldName.equalsIgnoreCase("this$0") ) {
				continue;
			}
			String getterName = getGetterMethodNameString(fieldName);
			Method method = clazz.getMethod(getterName);
			Object value = method.invoke(obj);
			map.put( fieldName , value);
		}
		return map;
	}
	
	/**
	 * 对象转Map集合
	 * @param <T> - 泛型
	 * @param obj -  实例
	 * @param isIncludeSuper 是否包括父类
	 * @return 数据集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String,String> object2MapString( T obj , boolean isIncludeSuper ) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Map<String , String> map = new LinkedHashMap<String, String>();
		Class clazz = obj.getClass();
		List<Field> fields = getClassFieldList(clazz, isIncludeSuper);
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			String fieldName = field.getName();
			if ( fieldName.equalsIgnoreCase("this$0") ) {
				continue;
			}
			String getterName = getGetterMethodNameString(fieldName);
			Method method = clazz.getMethod(getterName);
			Object value = method.invoke(obj);
			if( value != null ) {
				map.put( fieldName , value+"");
			}
		}
		return map;
	}
	
	/**
	 * 获取一个类的父类集合
	 * @param clazz - 需要操作的类
	 * @return 父类集合
	 */
	@SuppressWarnings({ "unchecked" })
	public static List<Class> getSuperClassList ( Class clazz ) {
		Class spClazz = null;
		try {
			spClazz = Class.forName(clazz.getName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		List<Class> list = new ArrayList<Class>();
		while ( spClazz != null ) {
			try {
				spClazz = spClazz.getSuperclass();
				if ( spClazz == null || spClazz == Object.class ) {
					break;
				}
				list.add(spClazz);
			} catch (Exception e) {
				break;
			}
		}
		return list;
	}
	
	/**
	 * 获取一个类的所有属性名
	 * @param clazz - 需要操作的类
	 * @param isIncludeSuper 是否包括父类
	 * @return 该类的所有属性
	 */
	@SuppressWarnings("unchecked")
	public static List<Field> getClassFieldList ( Class clazz , boolean isIncludeSuper ) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		List<Field> field_list = Arrays.asList(fields);
		list.addAll(field_list);
		
		if ( isIncludeSuper ) {
			List<Class> spCls_list = getSuperClassList(clazz);
			for (int i = 0; i < spCls_list.size(); i++) {
				Class c = spCls_list.get(i);
				List<Field> sp_f_list = Arrays.asList(c.getDeclaredFields());
				list.addAll(sp_f_list);
			}
		}
		return list;
	}
	
	/**
	 * 获取setter名
	 * @param propertyString - 属性名
	 * @return setter方法名
	 */
	public static String getSetterMethodNameString (String propertyString ) {
		String head = propertyString.substring(0,1);
		head = head.toUpperCase();
		String foot = propertyString.substring(1);
		String methodName = "set" + head + foot;
		return methodName;
	}
	
	/**
	 * 获取getter名
	 * @param propertyString - 属性名
	 * @return getter方法名
	 */
	public static String getGetterMethodNameString (String propertyString ) {
		String head = propertyString.substring(0,1);
		head = head.toUpperCase();
		String foot = propertyString.substring(1);
		String methodName = "get" + head + foot;
		return methodName;
	}
	
	
}
