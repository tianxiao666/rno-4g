package com.iscreate.op.action.informationmanage.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

public class DBUtil {
	private static Configuration hibernateConf;
	/**
	 * 集合拼装in条件语句
	 * 
	 * @param param_list
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String list2InString(List param_list, String columnName) {
		if (param_list == null || param_list.size() == 0) {
			return "";
		}
		String s = "";
		for (int i = 0; i < param_list.size(); i++) {
			Object object = param_list.get(i);
			if (object instanceof String) {
				s += "'" + object + "'";
			} else {
				s += object;
			}
			if (i != param_list.size() - 1) {
				s += ",";
			}
		}
		if (columnName != null) {
			StringBuffer sb = new StringBuffer("");
			sb.append(" AND " + columnName + " in ( ");
			sb.append(s);
			sb.append(" ) ");
			s = sb.toString();
		}
		return s;
	}

	/**
	 * 集合拼装in条件语句
	 * 
	 * @param param_list
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String list2InString(Object param_list, String columnName) {
		String s = list2InString((List) param_list, columnName);
		return s;
	}
	
	/**
	 * 集合拼装in条件语句
	 * 
	 * @param param_list
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String list2InString(String[] param_list, String columnName) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; param_list != null && i < param_list.length; i++) {
			list.add(param_list[i]);
		}
		String s = list2InString( list , columnName );
		return s;
	}

	/**
	 * 根据字符串数组,拼装selection语句
	 * 
	 * @param strs
	 *            表中列名
	 * @return selection语句
	 */
	public static String array2SelectionString(String... strs) {
		String selection = "";
		for (int i = 0; i < strs.length; i++) {
			if (i != 0) {
				selection += ",";
			}
			selection += " " + strs[i] + " ";
		}
		return selection;
	}

	public static String getUpdateSetString(Map param_map, DBWhereCallBack cb) {
		String updateString = "";
		if (param_map == null) {
			return updateString;
		}
		for (Iterator<String> it = param_map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = param_map.get(key);
			if (value == null
					|| (value instanceof String && (value + "").isEmpty())) {
				continue;
			}
			String where = " , {column} {opera} {value} ";
			StringBuffer opera = new StringBuffer("=");
			StringBuffer v = new StringBuffer(value + "");
			if (cb != null) {
				String callBack = cb.callBack(key, opera, v);
				if (callBack != null) {
					v = new StringBuffer(callBack);
				}
			}
			// 判断是否数组
			if (value instanceof Object[]) {
				Object[] obj = (Object[]) value;
				value = Arrays.asList(obj);
			}
			// 判断是否集合
			if (value instanceof List) {
				opera = new StringBuffer("in");
				v = new StringBuffer("(");
				List v_list = (List) value;
				String list2InString = list2InString(v_list, null);
				v.append(list2InString);
				v.append(")");
			}
			where = where.replace("{column}", key);
			where = where.replace("{opera}", opera);
			if (value instanceof String) {
				where = where.replace("{value}", "'" + v + "'");
			} else {
				where = where.replace("{value}", v);
			}
			updateString += where;
		}
		updateString = updateString.replaceFirst(",", "");
		return updateString;
	}

	/**
	 * 根据参数集合,拼装where语句
	 * 
	 * @param param_map
	 *            参数集合
	 * @return where语句
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	public static String getWhereString(Map param_map, DBWhereCallBack cb) {
		String whereString = " WHERE 1=1 ";
		if (param_map == null) {
			return whereString;
		}
		for (Iterator<String> it = param_map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = param_map.get(key);
			if (value instanceof String) {
				String str = value + "";
				if (str == null || str.isEmpty()) {
					continue;
				}
			} else if (value instanceof Collection) {
				Collection collection = (Collection) value;
				if (collection == null || collection.size() == 0) {
					continue;
				}
			}

			String where = " AND {column} {opera} {value} ";
			StringBuffer opera = new StringBuffer("=");
			StringBuffer v = new StringBuffer(value + "");
			if (cb != null) {
				String callBack = cb.callBack(key, opera, v);
				if (callBack != null) {
					v = new StringBuffer(callBack);
				}
			}
			// 判断是否数组
			if (value instanceof Object[]) {
				Object[] obj = (Object[]) value;
				value = Arrays.asList(obj);
			}
			// 判断是否集合
			if (value instanceof List) {
				opera = new StringBuffer("in");
				v = new StringBuffer("(");
				List v_list = (List) value;
				String list2InString = list2InString(v_list, null);
				v.append(list2InString);
				v.append(")");
			}
			where = where.replace("{column}", key);
			where = where.replace("{opera}", opera);
			if (value instanceof String ) {
				where = where.replace("{value}", "'" + v + "'");
			} else {
				where = where.replace("{value}", v);
			}
			whereString += where;
		}
		return whereString;
	}

	// 测试
	public static void main(String[] args) {
		// Map<String,Object> map = new LinkedHashMap<String, Object>();
		// map.put("name", "andy");
		// map.put("carNumber", "粤A33I43");
		// map.put("age", 18);
		// map.put("language",
		// Lists.newArrayList("Spring","Hibernate","Struts"));
		// map.put("score", Lists.newArrayList(89,90,100));
		// map.put("hobby", new String[]{"游泳","爬山","羽毛球","看书"});
		// map.put("isFree", new Integer[]{1,6,5,7});
		// map.put("other", new Object[]{13,"英语大全",5,9.6});
		// String whereString = getWhereString(map,new DBWhereCallBack () {
		// public String callBack(String columnName, StringBuffer
		// opera,StringBuffer value) {
		// if( columnName.equals("name")) {
		// opera.delete(0, opera.length());
		// opera.append("LIKE");
		// return "%" + value + "%";
		// }
		// return null;
		// }
		// });
		// System.out.println(whereString);
		//		
		// String[] s = new String[]{""};
		// Object l = new ArrayList();
		// System.out.println(l instanceof Object[] );

	}

	public static <T> void strikeLazy(T t) {
		Class clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			field.setAccessible(true);
			try {
				Object object = field.get(t);
				// if ( !(object instanceof String) &&
				// !(object instanceof Boolean) &&
				// !(object instanceof Float) &&
				// !(object instanceof Integer) &&
				// !(object instanceof Double) &&
				// !(object instanceof Long) &&
				// !(object instanceof Date) ) {
				// strikeLazy(object);
				// }

				String string = object.toString();
			} catch (Exception e) {

			}
		}
	}

	public interface DBWhereCallBack {
		/**
		 * 更变条件
		 * 
		 * @param columnName -
		 *            列名
		 * @param opera -
		 *            操作符
		 * @param value -
		 *            列值
		 * @return 更变的列值 , 返回 null 不做任何操作
		 */
		public String callBack(String columnName, StringBuffer opera,
				StringBuffer value);


	}

	

	@SuppressWarnings("unused")
	private static Configuration getHibernateConf() {
		if (hibernateConf == null) {
			return new Configuration();
		}
		return hibernateConf;
	}

	@SuppressWarnings("unchecked")
	private static PersistentClass getPersistentClass(Class clazz) {
		synchronized (DBUtil.class) {
			PersistentClass pc = getHibernateConf().getClassMapping(clazz.getName());
			if (pc == null) {
				hibernateConf = getHibernateConf().addClass(clazz);
				pc = getHibernateConf().getClassMapping(clazz.getName());

			}
			return pc;
		}
	}

	/**
	 * 功能描述：获取实体对应的表名
	 * 
	 * @param clazz
	 *            实体类
	 * @return 表名
	 */
	@SuppressWarnings("unchecked")
	public static String getTableName(Class clazz) {
		return getPersistentClass(clazz).getTable().getName();
	}

	/**
	 * 功能描述：获取实体对应表的主键字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @return 主键字段名称
	 */
	@SuppressWarnings("unchecked")
	public static String getPkColumnName(Class clazz) {
		return getPersistentClass(clazz).getTable().getPrimaryKey()
				.getColumn(0).getName();

	}

	/**
	 * 功能描述：通过实体类和属性，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @param propertyName
	 *            属性名称
	 * @return 字段名称
	 */
	@SuppressWarnings("unchecked")
	public static String getColumnName(Class clazz, String propertyName) {
		PersistentClass persistentClass = getPersistentClass(clazz);
		Property property = persistentClass.getProperty(propertyName);
		Iterator it = property.getColumnIterator();
		if (it.hasNext()) {
			Column column = (Column) it.next();
			return column.getName();
		}
		return null;
	}

	/**
	 * 集合拼装in条件语句
	 * 
	 * @param param_list
	 * @param columnName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String list2InLong(Collection list, String columnName) {
		List param_list = new ArrayList();
		param_list.addAll(list);
		if (param_list == null || param_list.size() == 0) {
			return "";
		}
		String s = "";
		for (int i = 0; i < param_list.size(); i++) {
			Object object = param_list.get(i);
			s += object;
			if (i != param_list.size() - 1) {
				s += ",";
			}
		}
		if (columnName != null) {
			StringBuffer sb = new StringBuffer("");
			sb.append(" AND " + columnName + " in ( ");
			sb.append(s);
			sb.append(" ) ");
			s = sb.toString();
		}
		return s;
	}

}
