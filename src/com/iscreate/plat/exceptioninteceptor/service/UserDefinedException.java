package com.iscreate.plat.exceptioninteceptor.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserDefinedException extends RuntimeException{
	
	public String message="";
	private String msg = "";
	private Throwable throwable;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public UserDefinedException(String msgs){
		this.message = msgs;
		this.msg = msgs;
		log.error(msgs);
	}
	
	public UserDefinedException(String msgs,Throwable e){
		this.message = msgs;
		this.msg = msgs;
		this.throwable = e;
		log.error(msgs,e);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
