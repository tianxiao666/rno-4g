package com.iscreate.plat.exceptioninteceptor.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.exceptioninteceptor.service.ExceptionAdviceImpl;
import com.iscreate.plat.exceptioninteceptor.service.ExceptionHandlerService;

public class ExceptionHandlerAction {

	private String message;
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 跳转到错误页面的异常处理Action
	 * @return
	 */
	public String getErrorByExceptionHandlerAction(){
		log.info("进入getErrorByExceptionHandlerAction方法");
		ExceptionHandlerService exceptionHandlerService = new ExceptionHandlerService();
		this.message = exceptionHandlerService.exceptionHandler(ExceptionAdviceImpl.exName, ExceptionAdviceImpl.ex);
		ExceptionAdviceImpl.exName = "";
		ExceptionAdviceImpl.ex = new Exception();
		log.info("执行getErrorByExceptionHandlerAction方法成功，实现了”跳转到错误页面的异常处理Action“的功能");
		log.info("退出getErrorByExceptionHandlerAction方法,返回String为error");
		return "error";
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	

}
