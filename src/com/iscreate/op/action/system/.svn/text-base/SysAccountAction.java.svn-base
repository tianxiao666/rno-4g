package com.iscreate.op.action.system;


import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class SysAccountAction {
     public SysAccountService sysAccountService;
     private Log log = LogFactory.getLog(this.getClass());
     /**
      * @author du.hw
      * @create 2013-05-29
      * 检查账号是否存在，ajax调用
      */
     public void checkAccountIsExistAjaxAction(){
    	log.info("进入checkAccountIsExistAjaxAction方法");
    	HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
    	String account = ServletActionContext.getRequest().getParameter("account");//用户名
 		String result = "found";
 		if(!sysAccountService.checkProviderAccountService(account)){//账号不存在
 			 result = "nofound";
 		}
 		try {
			response.getWriter().write(gson.toJson(result));//ajax返回
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
 		
 		log.info("执行checkAccountIsExistAjaxAction方法成功，实现了”检测账号是否存在“的功能");
 		log.info("退出checkAccountIsExistAjaxAction方法,返回void");
     }
     

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}
     
}
