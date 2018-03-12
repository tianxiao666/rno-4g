package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.service.cardispatch.CardispatchCommonService;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderServiceForMobile;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;
import com.iscreate.plat.tools.map.MapHelper;

public class CardispatchCarMonitorActionForMobile {
	private Log log = LogFactory.getLog(this.getClass());
	private CardispatchCommonService cardispatchCommonService;
	private CardispatchWorkorderServiceForMobile cardispatchWorkorderServiceForMobile;
	
	/**
	 * 
	 * @description: 根据组织id获取子组织列表
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 23, 2013 4:24:00 PM
	 */
	public void getChildOrgListByOrgIdActionForMobile(){
		log.info("终端:进入getChildOrgListByOrgIdActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String orgId = formJsonMap.get("orgId");
			
			List<Map<String,Object>> orgList = this.cardispatchCommonService.getChildOrgListByOrgId(orgId, userId);
			
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("orgList", gson.toJson(orgList));
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("getChildOrgListByOrgIdActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：getChildOrgListByOrgIdActionForMobile方法执行成功，实现了“根据组织id获取子组织列表”的功能");
		log.info("终端:退出getChildOrgListByOrgIdActionForMobile方法,返回void");
	}
	/**
	 * 
	 * @description: 获取车辆状态监控列表
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 23, 2013 4:26:18 PM
	 */
	public void getCarStateMonitorListActionForMobile(){
		log.info("终端:进入getCarStateMonitorListActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String orgId = formJsonMap.get("orgId");
			String carType = formJsonMap.get("carType");
			String carState = formJsonMap.get("carState");
			String indexStart = formJsonMap.get("indexStart");
			String indexEnd = formJsonMap.get("indexEnd");
			String longitude = formJsonMap.get("longitude");
			String latitude = formJsonMap.get("latitude");
			String carNumber = formJsonMap.get("carNumber");
			String distance = formJsonMap.get("distance");
			Map<String,Object> conditionMap = new HashMap<String,Object>();
			if(orgId!=null && !"".equals(orgId)){
				conditionMap.put("orgId", orgId);
			}
			if(carType!=null && !"".equals(carType)){
				conditionMap.put("carType", carType);
			}
			if(carState!=null && !"".equals(carState)){
				conditionMap.put("carState", carState);
			}
			if(longitude!=null && !"".equals(longitude)){
				conditionMap.put("longitude", longitude);
			}else{
				conditionMap.put("longitude", 0);
			}
			if(latitude!=null && !"".equals(latitude)){
				conditionMap.put("latitude", latitude);
			}else{
				conditionMap.put("latitude", 0);
			}
			if(carNumber!=null && !"".equals(carNumber)){
				conditionMap.put("carNumber", carNumber);
			}
			if(distance!=null && !"".equals(distance)){
				conditionMap.put("distance", distance);
			}
			List<Map<String,Object>> carInfoResultList = this.cardispatchWorkorderServiceForMobile.getCarStateMonitorListByCondition(conditionMap, indexStart, indexEnd);
			String count ="";
			List<Map<String,Object>> carList =null;
			if(carInfoResultList!=null && !carInfoResultList.isEmpty()){
				Map<String,Object> mp = carInfoResultList.get(0);
				count = mp.get("count")+"";
				carList = (List<Map<String,Object>>)mp.get("carList");
			}
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("count", count);
			resultMap.put("carInfoResultList", gson.toJson(carList));
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("getCarStateMonitorListActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：getCarStateMonitorListActionForMobile方法执行成功，实现了“获取车辆状态监控列表”的功能");
		log.info("终端:退出getCarStateMonitorListActionForMobile方法,返回void");
	}
	/**
	 * 
	 * @description: 根据车辆id获取相关信息
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 23, 2013 4:50:46 PM
	 */
	public void getCarRelatedInformationByCarIdActionForMobile(){
		log.info("终端:进入getCarRelatedInformationByCarIdActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String carId = formJsonMap.get("carId");
			Map<String,Object> carInfoResultMap = this.cardispatchWorkorderServiceForMobile.getCarRelatedInformationByCarId(carId);
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("carInfoResultMap", gson.toJson(carInfoResultMap));
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("getCarRelatedInformationByCarIdActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：getCarRelatedInformationByCarIdActionForMobile方法执行成功，实现了“获取车辆状态监控列表”的功能");
		log.info("终端:退出getCarRelatedInformationByCarIdActionForMobile方法,返回void");
	}
	/***
	 * 
	 * @description: 根据经纬度获取实际地址
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 22, 2013 4:40:50 PM
	 */
	public void getAddressByLngLatActionForMobile(){
		log.info("终端:进入getAddressByLngLatActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String longitude = formJsonMap.get("longitude");
			String latitude = formJsonMap.get("latitude");
			String address ="";
			if(longitude!=null && !"".equals(longitude)&&latitude!=null && !"".equals(latitude)){
				address = MapHelper.convertLatlngToAddress(longitude,latitude);
			}
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("location", address);
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("getAddressByLngLatActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：getAddressByLngLatActionForMobile方法执行成功，实现了“根据经纬度获取实际地址”的功能");
		log.info("终端:退出getAddressByLngLatActionForMobile方法,返回void");
	}
	//getter and setter
	public CardispatchCommonService getCardispatchCommonService() {
		return cardispatchCommonService;
	}
	public void setCardispatchCommonService(
			CardispatchCommonService cardispatchCommonService) {
		this.cardispatchCommonService = cardispatchCommonService;
	}
	public CardispatchWorkorderServiceForMobile getCardispatchWorkorderServiceForMobile() {
		return cardispatchWorkorderServiceForMobile;
	}
	public void setCardispatchWorkorderServiceForMobile(
			CardispatchWorkorderServiceForMobile cardispatchWorkorderServiceForMobile) {
		this.cardispatchWorkorderServiceForMobile = cardispatchWorkorderServiceForMobile;
	}
	
	
	
}
