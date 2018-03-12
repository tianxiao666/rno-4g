package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;

@Deprecated
public class CardispatchCarUseActionForMobile {
	
	/*
	 * 注入开始
	 */
	private CardispatchWorkorderService cardispatchWorkorderService;

	private TaskTracingRecordService taskTracingRecordService;
	//日志
	private Log log = LogFactory.getLog(CardispatchCarUseActionForMobile.class);
	
	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}

	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}

	public CardispatchWorkorderService getCardispatchWorkorderService() {
		return cardispatchWorkorderService;
	}

	public void setCardispatchWorkorderService(
			CardispatchWorkorderService cardispatchWorkorderService) {
		this.cardispatchWorkorderService = cardispatchWorkorderService;
		
	}
	
	/*
	 * 注入结束
	 */
	
	/**
	 * 用车
	 */
	public void useCarAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId =(String)session.getAttribute("userId");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
				
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageCommunicationHelper.getMobilePackage ();
				
		// mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}		
		String content = mobilePackage.getContent();
				
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		// 获取表单提交的JSON信息的Map
		Map<String, String> contentMap = mch.getGroupByKey ("request");
		// 参数转换
		String WOID = contentMap.get("WOID");
		//String WOID = "CD-20121120-0016";//测试
		String realUseCarMileage = contentMap.get("realUseCarMileage");
		String longitude = contentMap.get("longitude");
		String latitude = contentMap.get("latitude");
		String address = contentMap.get("address");
		Map<String, String> requestParamMap = new HashMap<String, String>();
		requestParamMap.put("woId", WOID);
		requestParamMap.put("realUseCarMileage", realUseCarMileage);
		requestParamMap.put("realUseCarLongitude", longitude);
		requestParamMap.put("realUseCarLatitude", latitude);
		requestParamMap.put("realUseCarMeetAddress", address);
		Boolean flag = false;
		try {
			flag = cardispatchWorkorderService.txUseCar(requestParamMap, null);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("returnFlag", flag+"");
		returnMobilePackage(returnMap, "returnMap");
	}

	
	
	/**
	 * 还车
	 */
	public void returnCar() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId =(String)session.getAttribute("userId");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
				
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageCommunicationHelper.getMobilePackage ();
				
		// mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}		
		String content = mobilePackage.getContent();
				
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		// 获取表单提交的JSON信息的Map
		Map<String, String> contentMap = mch.getGroupByKey ("request");
		// 参数转换
		String WOID = contentMap.get("WOID");
		//String WOID = "CD-20121120-0016";//测试
		String realUseCarMileage = contentMap.get("realUseCarMileage");
		String longitude = contentMap.get("longitude");
		String latitude = contentMap.get("latitude");
		String address = contentMap.get("address");
		Map<String, String> requestParamMap = new HashMap<String, String>();
		requestParamMap.put("woId", WOID);
		requestParamMap.put("realReturnCarMileage", realUseCarMileage);
		requestParamMap.put("realReturnCarLongitude", longitude);
		requestParamMap.put("realReturnCarLatitude", latitude);
		requestParamMap.put("realReturnCarAddress", address);
		boolean flag = cardispatchWorkorderService.txReturnCar(requestParamMap, null);
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("returnFlag", flag+"");
		returnMobilePackage(returnMap, "returnMap");
	}
	
	
	/**
	 * 保存用车费用
	 * (请求参数) woId , feeType , feeAmount , description
	 * (响应) (boolean)保存是否成功 (true：成功)
	 */
	public void saveFeeAmount () {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId =(String)session.getAttribute("userId");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
				
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageCommunicationHelper.getMobilePackage ();
				
		// mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}		
		String content = mobilePackage.getContent();
				
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		// 获取表单提交的JSON信息的Map
		Map<String, String> contentMap = mch.getGroupByKey ("request");
		// 参数转换
		String WOID = contentMap.get("WOID");
		//String WOID = "CD-20121120-0016";//测试
		String feeType = contentMap.get("feeType");
		String feeAmount = contentMap.get("feeAmount");
		String description = contentMap.get("description");
		Map<String, String> requestParamMap = new HashMap<String, String>();
		requestParamMap.put("woId", WOID);
		requestParamMap.put("feeType", feeType);
		requestParamMap.put("feeAmount", feeAmount);
		requestParamMap.put("description", description);
		boolean saveFeeAmount = cardispatchWorkorderService.saveFeeAmount(requestParamMap);
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("returnFlag", saveFeeAmount+"");
		returnMobilePackage(returnMap, "returnMap");
	}
	
	/**
	 * 催办
	* @date Nov 21, 20129:39:40 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void remindersCarTaskAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId =(String)session.getAttribute("userId");
		String userName =(String)session.getAttribute("userName");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
				
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageCommunicationHelper.getMobilePackage ();
				
		// mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}		
		String content = mobilePackage.getContent();
				
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		// 获取表单提交的JSON信息的Map
		Map<String, String> contentMap = mch.getGroupByKey ("request");
		// 参数转换
		String WOID = contentMap.get("WOID");
		//String WOID = "CD-20121120-0016";//测试
		String remindersReason = contentMap.get("remindersReason");
		//流程处理者
		Tasktracerecord tc = new Tasktracerecord();
		tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		tc.setWoId(WOID);
		tc.setHandler(userId);
		tc.setHandlerName(userName);
		tc.setHandleResultDescription(remindersReason);
		tc.setHandleTime(new Date());
		tc.setHandleWay(CardispatchConstant.CARDISATCHWORKORDER_ESTATE_PENDINGWORKORDER);
		String flag = taskTracingRecordService.txSaveTaskTracingRecordService(tc);
		boolean returnFlag = false;
		if(flag != null && flag.equals("success")){
			returnFlag = true;
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put("returnFlag", returnFlag+"");
		returnMobilePackage(returnMap, "returnMap");
	}
	
	
	private void returnMobilePackage(Map<String, String> returnMap, String de) {
		MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
		HttpServletResponse response = ServletActionContext.getResponse();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		MobileContentHelper mch = new MobileContentHelper();
		mch.addGroup(de, returnMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
