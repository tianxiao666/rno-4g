package com.iscreate.plat.loginteceptor.service;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

public class LogAdviceImpl{
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void before(Method arg0, Object[] arg1, Object arg2)
			throws Throwable {
		String methodName = arg0.getName();
		System.out.println("拦截到 "+methodName+" 方法");
		//System.out.println("This is before method!");
	}
	
	public static void main(String[] args) {
		System.out.println("RoutineTaskQueryActionForMobile".matches("^((?!ForMobile).)*$"));
	}

	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println(arg0);
		System.out.println(arg1);
		System.out.println(arg2);
		System.out.println(arg3);
	}

	public Object invoke(ProceedingJoinPoint arg0) throws Throwable {
		Object methodReturnValue = null;    //方法返回值
		String methodReturnType = arg0.getSignature().toString();    //方法返回类型
		methodReturnType = methodReturnType.substring(0,methodReturnType.indexOf(" "));
		String methodName = arg0.getSignature().toString();    //方法名
		methodName = methodName.substring(methodName.indexOf(" ")+1,methodName.lastIndexOf("("));
		String methodArgs = "";    //方法参数
		//判断该方法是否带参数
		int index = arg0.toShortString().lastIndexOf("(..)");
		if(index!=-1){
			//获取参数类型的数组
			String longString = arg0.toLongString();
			String argStr = longString.substring(longString.lastIndexOf("(")+1, longString.lastIndexOf(")")-1);
			String[] argType = argStr.split(",");
			methodArgs += ",参数：";
			//获取参数的数组
			Object[] args = arg0.getArgs();
			if(args!=null && args.length>0){
				for (int i = 0; i<args.length; i++) {
					methodArgs += argType[i]+" "+args[i]+",";
				}
				methodArgs = methodArgs.substring(0,methodArgs.length()-1);
			}
		}
		log.debug("[AOP] 进入"+methodName+"方法"+methodArgs);
		methodReturnValue = arg0.proceed();
		if("void".equals(methodReturnType)){
			methodReturnValue = "";
		}
//		log.info(methodName+"方法返回:"+methodReturnType+" "+methodReturnValue);
		log.debug("[AOP] 退出" + methodName + "方法" + methodArgs + " ,返回：" + methodReturnType + " " + methodReturnValue);
//		log.debug("返回：" + methodReturnType + " " + methodReturnValue);
		return methodReturnValue;
	}
	
	


	 
}
