package com.iscreate.plat.exceptioninteceptor.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExceptionAdviceImpl {

	 private Log log = LogFactory.getLog(this.getClass());
	 
	 public static String exName;
	 public static Exception ex;
	 
	 public void catchException (Exception e){
		 log.error("iosm-op exception",e);
//		 StackTraceElement[] stackTrace = e.getStackTrace();
//		 System.out.println(stackTrace[0].getMethodName());
//		 System.out.println(stackTrace[0].getClassName());
//		 System.out.println(e.getClass().getName());
//		 UserDefinedExceptionAction u = new UserDefinedExceptionAction();
//		 u.getErrorPageAction();
		 this.exName = e.getClass().getName();
		 this.ex = e;
		 //return "error";
	 }
}
