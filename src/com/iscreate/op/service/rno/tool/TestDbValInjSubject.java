package com.iscreate.op.service.rno.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestDbValInjSubject {

	@DbValInject(type="String",dbField="nm")
	private String name;
	@DbValInject(type="int",dbField="ae")
	private int age;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public TestDbValInjSubject(){
		
	}
	
	@Override
	public String toString() {
		return "TestDbValInjSubject [name=" + name + ", age=" + age + "]";
	}

	public static void main(String[] args) {
		
		Map<String,Object> val=new HashMap<String,Object>();
		val.put("nm", "some");
		val.put("ae", 21);
//		
//		Class<TestDbValInjSubject> classz=TestDbValInjSubject.class;
//		TestDbValInjSubject obj=null;
//		try {
//			obj=classz.newInstance();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//		Field[] fields=classz.getDeclaredFields();
//		for(Field field:fields){
//			String fieldName=field.getName();
//			DbValInject di=field.getAnnotation(DbValInject.class);
//			String df=di.dbField();
//			String ty=di.type();
//			Object v=val.get(df);
//			if(ty.equals("String")){
//				try {
//					Method method=classz.getDeclaredMethod("set"+(fieldName.substring(0, 1)).toUpperCase()+fieldName.substring(1),new Class[]{java.lang.String.class});
//					method.invoke(obj, v.toString());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else if(ty.equals("int")){
//				try {
//					Method method=classz.getDeclaredMethod("set"+(fieldName.substring(0, 1)).toUpperCase()+fieldName.substring(1),new Class[]{int.class});
//					method.invoke(obj, Integer.parseInt(v.toString()));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
		TestDbValInjSubject obj=RnoHelper.commonInjection(TestDbValInjSubject.class, val,new DateUtil());
		System.out.println("obj="+obj);
	}
}
