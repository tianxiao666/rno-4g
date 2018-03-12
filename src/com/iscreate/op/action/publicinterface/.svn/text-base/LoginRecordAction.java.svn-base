package com.iscreate.op.action.publicinterface;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.system.SysSecurityLoginrecordService;

public class LoginRecordAction {

	private SysSecurityLoginrecordService sysSecurityLoginrecordService;
	private Map<String,String> map;
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 获取登陆人信息
	 * @return
	 */
	public String getLoginRecordAction(){
		log.info("进入getLoginRecordAction方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		//从session获取user
		String userId = (String)session.getAttribute("userId");
		if(userId==null || "".equals(userId)){
			this.map = new HashMap<String, String>();
			log.info("session里的用户Id为空");
		}else{
			this.map = this.sysSecurityLoginrecordService.getSysSecurityLoginrecordInfoService(userId);
			log.info("执行getLoginRecordAction方法成功，实现了”获取登陆人信息“的功能");
		}
		log.info("退出getLoginRecordAction方法,返回void");
		return "success";
	}

	
	public SysSecurityLoginrecordService getSysSecurityLoginrecordService() {
		return sysSecurityLoginrecordService;
	}


	public void setSysSecurityLoginrecordService(
			SysSecurityLoginrecordService sysSecurityLoginrecordService) {
		this.sysSecurityLoginrecordService = sysSecurityLoginrecordService;
	}


	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
}
