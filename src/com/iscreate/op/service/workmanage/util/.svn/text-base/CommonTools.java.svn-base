package com.iscreate.op.service.workmanage.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.workmanage.BizProcessConfServiceImpl;
import com.iscreate.plat.tools.TimeFormatHelper;

public class CommonTools {

	//static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final Log logger = LogFactory.getLog(BizProcessConfServiceImpl.class);
	
	/**
	 * 获取2个任务单集合的交集
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<Map> getTaskOrderMixList(List<Map> list1,List<Map> list2){
		List<Map> totalList=new ArrayList<Map>();
		try {
			if((list1!=null && !list1.isEmpty()) && (list2!=null && !list2.isEmpty())){
				for(int i=0;i<list1.size();i++){
					for(int j=0;j<list2.size();j++){
						if(list1.get(i).get("toId").equals(list2.get(j).get("toId"))){
							totalList.add(list1.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalList;
	}
	
	
	
	
	
	/**
	 * 迭代对象的属性值
	 * @param obj 目标对象
	 * @return
	 */
	public static <T> Map getValueOfObjectByPropertyName(T t,Map targetMap){
		String result=null;
		if(t!=null && targetMap!=null){
			Class clazz=t.getClass();
			try {
				Constructor constructor = clazz.getConstructor();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				logger.error("在【"+clazz+"】中，不存在该构造方法");
			}
			Field[] fields = clazz.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					
					boolean isStatic = Modifier.isStatic(field.getModifiers());
					if(!isStatic) {
						 String fieldName = field.getName();	//获取属性名称
							Class fieldTypeClazz = field.getType();
							String methodName = "get"
								+ fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1); // 获取字段的公共访问方法
							Method method;
							Object returnValue=null;
							try {
								method = clazz.getMethod(methodName,new Class[] {});
								returnValue=method.invoke(t,new Object[] {});
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("在【"+clazz+"】中，不存在方法名称为【"+methodName+"】的方法");
							}
							
							
							if(fieldTypeClazz.getName().equals("java.util.Date")){
								if(returnValue!=null){
									//result=sdf.format(returnValue);
									result=TimeFormatHelper.getTimeFormatBySecond(returnValue);
								}else{
									result="";
								}
							}else{
								result=returnValue==null?"":returnValue.toString();
							}
							targetMap.put(fieldName, result);
					}
				}
			}
		}
		return targetMap;
	}
	
	
	
}
