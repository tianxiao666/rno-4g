package com.iscreate.op.service.rno.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RnoHelper {

	private static Log log=LogFactory.getLog(RnoHelper.class);
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf12 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private static SimpleDateFormat sdf6 = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm:ss");
	private static SimpleDateFormat sdf9 = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm");
	private static SimpleDateFormat sdf7 = new SimpleDateFormat(
			"MM-dd-yyyy");
	
	private static SimpleDateFormat sdf3 = new SimpleDateFormat(
			"yyyy/MM/dd");
	private static SimpleDateFormat sdf8 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat sdf10 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");
	
	private static SimpleDateFormat sdf4 = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");
	private static SimpleDateFormat sdf11 = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm");
	private static SimpleDateFormat sdf5 = new SimpleDateFormat(
			"MM/dd/yyyy");
	
	
	private static SimpleDateFormat sdf20=new SimpleDateFormat("yyyy-MM-dd HH");
	
	@Deprecated
	public synchronized static Date  parseDateArbitrary(String str) {
		
		Date date = null;
		if(StringUtils.isBlank(str)){
			return null;
		}
		if(StringUtils.contains(str, "-")){
			String[] sps=str.split("-");
			if(sps[0].length()>2){
				//yyyy-mm-dd 或yyyy-mm-dd hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf1.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf1.parse(str) fail:str="+str);
						//可能是 或yyyy-mm-dd hh:mm
						try {
							date=sdf12.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf12.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf2.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf2.parse(str) fail:str="+str);
					}
				}
			}else{
				//mm-dd-yyyy 或mm-dd-yyyy hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf6.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf6.parse(str) fail:str="+str);
						//可能是mm-dd-yyyy hh:mm
						try {
							date=sdf9.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf9.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf7.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf7.parse(str) fail:str="+str);
					}
				}
			}
		}else if(StringUtils.contains(str, "/")){
			String[] sps=str.split("/");
			if(sps[0].length()>2){
				//yyyy/mm/dd 或yyyy/mm/dd hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf8.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf8.parse(str) fail:str="+str);
						//可能是yyyy/mm/dd hh:mm、
						try {
							date=sdf10.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf10.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf3.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf3.parse(str) fail:str="+str);
					}
				}
			}else{
				//mm/dd/yyyy 或mm/dd/yyyy hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf4.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf4.parse(str) fail:str="+str);
						//可能是mm/dd/yyyy hh:mm
						try {
							date=sdf11.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf11.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf5.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf5.parse(str) fail:str="+str);
					}
				}
			}
		}
		
		
		
		return date;
//		try {
//			date = sdf1.parse(str);
//		} catch (Exception e1) {
//			try {
//				date = sdf2.parse(str);
//			} catch (Exception e2) {
//				try {
//					date = sdf4.parse(str);
//				} catch (Exception e3) {
//					try {
//						date = sdf3.parse(str);
//					} catch (Exception e4) {
//							return null;
//					}
//				}
//			}
//		}
//		return date;
	}
	
//	public static Date to_yyyyMMddHHmmssDate1(String dt){
//		try {
//			return sdf1.parse(dt);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	public static Date to_yyyyMMddDate1(String dt){
//		try {
//			return sdf2.parse(dt);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//	
//	public static String format_yyyyMMddHHmmss1(Date date){
//		return sdf1.format(date);
//	}
//	
//	public static String format_yyyyMMdd1(Date date) {
//		return sdf2.format(date);
//	}
//	
//	public static String format_yyyyMMddHH1(Date date) {
//		return sdf20.format(date);
//	}
	
	public static long getNextSeqValue(String seq,Connection connection){
		long seqVal=-1L;
		Statement pstmt =null;
		try {
			pstmt=connection.createStatement();
		} catch (SQLException e3) {
			e3.printStackTrace();
			return -1;
		}
		seqVal=getNextSeqValue(seq,pstmt);
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return seqVal;
	}
	
	public static long getNextSeqValue(String seqCode,Statement stmt){
		long seqVal=-1;
		String vsql = "select "+seqCode+".NEXTVAL as id from dual";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(vsql);
			rs.next();
			seqVal= rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return seqVal;
	}
	
	/**
	 * 将列表转为逗号分隔的字符串
	 * @param list
	 * @return
	 * @author brightming
	 * 2014-1-19 下午1:15:30
	 */
	public static String getStrValFromList(List<? extends Object> list){
		if(list==null || list.isEmpty()){
			return "";
		}
		StringBuilder buf=new StringBuilder();
		for(Object o:list){
			if(o==null){
				continue;
			}
			buf.append(o+",");
		}
		if(buf.length()>0){
			buf.deleteCharAt(buf.length()-1);
		}
		return buf.toString();
	}
	
	/**
	 * 同用查询方法
	 * @param stmt
	 * @param sql
	 * @return
	 * @author brightming
	 * 2014-2-17 下午3:51:02
	 */
	public static List<Map<String, Object>> commonQuery(Statement stmt, String sql) {
		ResultSet rs = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			rs = stmt.executeQuery(sql);
			ResultSetMetaData meta = rs.getMetaData();
			int columnCnt = meta.getColumnCount();
			List<String> labels = new ArrayList<String>();
			for (int i = 1; i <= columnCnt; i++) {
				labels.add(meta.getColumnLabel(i));
			}
			Map<String, Object> one = null;
			int i = 0;
			while (rs.next()) {
				one = new HashMap<String, Object>();
				for (i = 1; i <= columnCnt; i++) {
					one.put(labels.get(i-1), rs.getObject(i));
				}
				result.add(one);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static <T> T commonInjection(Class<T> classz,Map<String,Object> val,DateUtil dateUtil){
		T obj=null;
		try {
			obj=classz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Method method=null;
		Field[] fields=classz.getDeclaredFields();
		DbValInject di=null;
		for(Field field:fields){
			String fieldName=field.getName();
			String methodName="set"+(fieldName.substring(0, 1)).toUpperCase()+fieldName.substring(1);
			di=field.getAnnotation(DbValInject.class);
			if(di==null){
				continue;
			}
			String df=di.dbField();
			String ty=di.type();
			Object v=val.get(df);
			if(v==null){
				continue;
			}
			if(ty.equals("String")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.lang.String.class});
					method.invoke(obj, v.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(ty.equals("int")||ty.equals("Integer")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{Integer.class});
					method.invoke(obj, Integer.parseInt(v.toString()));
				} catch (Exception e) {
//					e.printStackTrace();
					try {
						method=classz.getDeclaredMethod(methodName,new Class[]{int.class});
						method.invoke(obj, Integer.parseInt(v.toString()));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			else if(ty.equals("Float")||ty.equals("float")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.lang.Float.class});
					method.invoke(obj, Float.parseFloat(v.toString()));
				} catch (Exception e) {
//					e.printStackTrace();
					try {
						method=classz.getDeclaredMethod(methodName,new Class[]{float.class});
						method.invoke(obj, Float.parseFloat(v.toString()));
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
			}
			else if(ty.equals("Double")||ty.equals("double")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.lang.Double.class});
					method.invoke(obj, Double.parseDouble(v.toString()));
				} catch (Exception e) {
//					e.printStackTrace();
					try {
						method=classz.getDeclaredMethod(methodName,new Class[]{double.class});
						method.invoke(obj, Double.parseDouble(v.toString()));
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
			}
			else if(ty.equals("Long")||ty.equals("long")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.lang.Long.class});
					method.invoke(obj, Long.parseLong(v.toString()));
				} catch (Exception e) {
//					e.printStackTrace();
					try {
						method=classz.getDeclaredMethod(methodName,new Class[]{long.class});
						method.invoke(obj, Long.parseLong(v.toString()));
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
			}
			else if(ty.equals("Date")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.util.Date.class});
					method.invoke(obj, dateUtil.parseDateArbitrary(v.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(ty.equals("BigDecimal")){
				try {
					method=classz.getDeclaredMethod(methodName,new Class[]{java.math.BigDecimal.class});
					method.invoke(obj, new BigDecimal(v.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				log.error("type="+ty+",field="+fieldName+" can't parse!");
			}
		}
		return obj;
	}

	/**
	 * 返回合适的大小字符串表达
	 * @param fileSize
	 * 字节为单位
	 * @return
	 * @author brightming
	 * 2014-8-20 上午10:25:46
	 */
	public static String getPropSizeExpression(Long fileSize) {
		int[] power=new int[]{10,20,30,40,50,60};
		String[] unit=new String[]{"字节","k","M","G","T","P"};
		for(int i=0;i<power.length;i++){
			if(fileSize<Math.pow(2, power[i])){
		    	return (int)(fileSize/(Math.pow(2,power[i]-10)))+unit[i];
		    }
		}
	    
		return "无法衡量";
	}
	
	/**
	 * 获取一段时间的日期列
	 * @param dBegin
	 * @param dEnd
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:01:59
	 */
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}
	
	public static void main(String[] args) {
		System.out.println(getPropSizeExpression(625995L));
	}
}
