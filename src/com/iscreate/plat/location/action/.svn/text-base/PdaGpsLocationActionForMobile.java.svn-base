package com.iscreate.plat.location.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.location.service.PdaGpsService;

public class PdaGpsLocationActionForMobile {

	private PdaGpsService pdaGpsService;
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 保存终端GPS位置
	 */
	public void saveGpsLocationActionForMobile(){
		log.info("进入saveGpsLocationActionForMobile方法");
		HttpServletRequest request = ServletActionContext.getRequest();
		String gpsLocation = request.getParameter("personGpsLocationInfo");
		try {
			gpsLocation = URLDecoder.decode(gpsLocation, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("gpsLocation转码失败");
			throw new UserDefinedException("gpsLocation转码失败");
		}
		this.pdaGpsService.saveGpsLocationService(gpsLocation);
		log.info("执行saveGpsLocationActionForMobile方法成功，实现了”保存终端GPS位置“的功能");
		log.info("退出saveGpsLocationActionForMobile方法,返回void");
	}
	
	/**
	 * 获取终端GPS位置
	 */
	public void getGpsLocationActionForMobile(){
		
	}
	
	/**
	 * 保存登陆客户端
	 */
	public void saveClientLoginActionForMobile(){
		log.info("进入saveClientLoginActionForMobile方法");
		HttpServletRequest request = ServletActionContext.getRequest();
		String gpsLocation = request.getParameter("personGpsLocationInfo");
		try {
			gpsLocation = URLDecoder.decode(gpsLocation, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("gpsLocation转码失败");
			throw new UserDefinedException("gpsLocation转码失败");
		}
		this.pdaGpsService.saveClientLoginService(gpsLocation);
		log.info("执行saveClientLoginActionForMobile方法成功，实现了”保存登陆客户端“的功能");
		log.info("退出saveClientLoginActionForMobile方法,返回void");
	}
	
	/**
	 * 保存退出客户端
	 */
	public void saveClientExitActionForMobile(){
		log.info("进入saveClientExitActionForMobile方法");
		HttpServletRequest request = ServletActionContext.getRequest();
		String gpsLocation = request.getParameter("personGpsLocationInfo");
		try {
			gpsLocation = URLDecoder.decode(gpsLocation, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("gpsLocation转码失败");
			throw new UserDefinedException("gpsLocation转码失败");
		}
		this.pdaGpsService.saveClientExitService(gpsLocation);
		log.info("执行saveClientExitActionForMobile方法成功，实现了”保存退出客户端“的功能");
		log.info("退出saveClientExitActionForMobile方法,返回void");
	}

	public PdaGpsService getPdaGpsService() {
		return pdaGpsService;
	}

	public void setPdaGpsService(PdaGpsService pdaGpsService) {
		this.pdaGpsService = pdaGpsService;
	}
	
}
