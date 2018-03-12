package com.iscreate.op.action.routineinspection;

import java.util.Date;
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
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.routineinspection.RoutineInspectionTaskService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;

public class RoutineInspectionTaskActionForMobile {

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskService routineInspectionTaskService;
	private WorkManageService workManageService;
	private String className = "com.iscreate.op.action.routineinspection.RoutineInspectionTaskActionForMobile";
	
	/**
	 * 跳转到巡检任务
	 */
	public void jumpRoutineInspectionTaskActionForMobile(){
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
				log.info(this.className+"类里的jumpRoutineInspectionTaskActionForMobile方法中的mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				String woId = formJsonMap.get("WOID");
				rouInfoMap.put("WOID", woId);
				rouInfoMap.put("TOID", toId);
				mch.addGroup("header", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error(this.className+"类里的jumpRoutineInspectionTaskActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	/**
	 * 根据TOID获取任务单信息
	 */
	public void getRoutineInspectionTaskByToIdActionForMobile(){
		log.info("终端：进入getRoutineInspectionTaskByToIdActionForMobile方法");
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
				log.info(this.className+"类里的getRoutineInspectionTaskByToIdActionForMobile方法中的mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				Map<String, String> taskInfo = this.routineInspectionTaskService.getRoutineInspectionTaskInfoByToIdForMobileService(toId);
				if(taskInfo!=null && taskInfo.size()>0){
					rouInfoMap.put("taskTitle", taskInfo.get("taskTitle"));
					rouInfoMap.put("planTitle", taskInfo.get("planTitle"));
					rouInfoMap.put("reId", taskInfo.get("reId"));
					rouInfoMap.put("reType", taskInfo.get("reType"));
					rouInfoMap.put("reName", taskInfo.get("reName"));
				}
				//获取任务状态
				WorkmanageTaskorder taskOrderByToId = this.workManageService.getTaskOrderByToId(toId);
				if(taskOrderByToId!=null){
					rouInfoMap.put("taskStatus", taskOrderByToId.getStatus()+"");
				}
				mch.addGroup("routineTitle", rouInfoMap);
			}
			
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionTaskByToIdActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了getRoutineInspectionTaskByToIdActionForMobile方法，该方法实现了“根据TOID获取任务单信息”");
		log.info("退出getRoutineInspectionTaskByToIdActionForMobile方法，返回void");
	}
	
	/**
	 * 判断终端偏离位置
	 */
	public void chickDeviateActionForMobile(){
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
				log.info("mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				String currentLng = formJsonMap.get("longitude");
				String currentLat = formJsonMap.get("latitude");
				String distance = formJsonMap.get("distance");
				Double longitude = null;
				Double latitude = null;
				if(currentLng!=null && !"".equals(currentLng)){
					longitude = Double.parseDouble(currentLng);
				}
				if(currentLat!=null && !"".equals(currentLat)){
					latitude = Double.parseDouble(currentLat);
				}
				String isDeviate = "true";
				String subDistance = "";
				//当前位置是否已偏离
				Map<String, String> deviateByToIdService = this.routineInspectionTaskService.getDeviateByToIdService(toId, longitude, latitude);
				if(deviateByToIdService!=null && deviateByToIdService.size()>0){
					subDistance = deviateByToIdService.get("distance");
					if(subDistance!=null){
						Double subDistance1 = Double.parseDouble(subDistance);
						if(subDistance1<Double.parseDouble(distance)){
							isDeviate = "false";
						}
					}
				}
				rouInfoMap.put("isDeviate", isDeviate+"");
				rouInfoMap.put("deviate", subDistance);
				rouInfoMap.put("TOID", toId);
				mch.addGroup("chickDeviateStatus", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("chickSignInActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	/**
	 * 判断签到
	 */
	public void chickSignInActionForMobile(){
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
				log.info("mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				String isOverTime = "false";
				RoutineinspectionTaskorder taskInfo = this.routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
				if(taskInfo!=null){
					Date taskPlanBeginTime = taskInfo.getTaskPlanBeginTime();
					Date date = new Date();
					if(taskPlanBeginTime!=null){
						if(taskPlanBeginTime.getTime()>date.getTime()){
							isOverTime = "true";
						}
					}
				}
				rouInfoMap.put("isOverTime", isOverTime);
				rouInfoMap.put("TOID", toId);
				mch.addGroup("chickSignInStatus", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("chickSignInActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	/**
	 * 判断是否已签到或已签退
	 */
	public void isInspectionTaskOrderSignedActionForMobile(){
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
				log.info("mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				RoutineinspectionTaskorder taskInfo = this.routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
				String isSignOut="false";
				String isSignIn="false";
				if(taskInfo!=null){
					Date signOutTime = taskInfo.getSignOutTime();
					if(signOutTime!=null){
						isSignOut ="true";
					}
					Date signInTime = taskInfo.getSignInTime();
					if(signInTime!=null){
						isSignIn ="true";
					}
				}
				rouInfoMap.put("isSignOut", isSignOut);
				rouInfoMap.put("isSignIn", isSignIn);
				rouInfoMap.put("TOID", toId);
				mch.addGroup("chickSignInStatus", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("isInspectionTaskOrderSignedActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	
	/**
	 * 签到Action
	 */
	public void signInRoutineInspectionActionForMobile(){
		log.info("终端：进入signInRoutineInspectionActionForMobile方法");
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
				log.info(this.className+"类里的signInRoutineInspectionActionForMobile方法中的mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				String longitude = formJsonMap.get("longitude");
				String latitude = formJsonMap.get("latitude");
				String deviate = formJsonMap.get("deviate");
				Double lng = null;
				Double lat = null;
				Float floatDeviate = null;
				if(longitude!=null && !"".equals(longitude)){
					lng = Double.parseDouble(longitude);
				}
				if(latitude!=null && !"".equals(latitude)){
					lat = Double.parseDouble(latitude);
				}
				if(deviate!=null && !"".equals(deviate)){
					floatDeviate = Float.parseFloat(deviate);
				}
				
				boolean isSuccess = this.routineInspectionTaskService.txSignInRoutineInspectionService(toId, lng, lat,floatDeviate);
				/*
				RoutineinspectionTaskorder taskOrder = this.routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
				if(taskOrder!=null){
					//taskOrder.setLongitude(Double.parseDouble(longitude));
					//taskOrder.setLatitude(Double.parseDouble(latitude));
					taskOrder.setSignInTime(new Date());
					boolean isSuccess = this.routineInspectionTaskService.txUpdateRoutineInspectionTaskOrderService(taskOrder);
					if(!isSuccess){
						throw new UserDefinedException(this.className+"类里的signInRoutineInspectionActionForMobile方法签到失败");
					}
					rouInfoMap.put("result", isSuccess+"");
					//获取任务
					HttpSession session = ServletActionContext.getRequest().getSession();
					String userId = (String)session.getAttribute("userId");
					boolean taskTask = this.workManageService.taskTask(toId, userId);
					if(!taskTask){
						throw new UserDefinedException(this.className+"类里的signInRoutineInspectionActionForMobile方法获取任务失败");
					}
				}
				*/
				rouInfoMap.put("result", isSuccess+"");
				mch.addGroup("signIn", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error(this.className+"类里的signInRoutineInspectionActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了signInRoutineInspectionActionForMobile方法，该方法实现了“签到”的功能");
		log.info("退出signInRoutineInspectionActionForMobile方法，返回void");
	}
	
	/**
	 * 签退Action
	 */
	public void signOutRoutineInspectionActionForMobile(){
		log.info("终端：进入signOutRoutineInspectionActionForMobile方法");
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
				log.info(this.className+"类里的signOutRoutineInspectionActionForMobile方法中的mobilePackage为空，返回错误信息");
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
			if(formJsonMap!=null){
				Map<String, String> rouInfoMap = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				//String longitude = formJsonMap.get("longitude");
				//String latitude = formJsonMap.get("latitude");
				boolean isSuccess = this.routineInspectionTaskService.signOutRoutineInspectionService(toId);
				rouInfoMap.put("result", isSuccess+"");
				mch.addGroup("signOut", rouInfoMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error(this.className+"类里的signOutRoutineInspectionActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了signOutRoutineInspectionActionForMobile方法，该方法实现了“签退”的功能");
		log.info("退出signOutRoutineInspectionActionForMobile方法，返回void");
	}
	
	public RoutineInspectionTaskService getRoutineInspectionTaskService() {
		return routineInspectionTaskService;
	}
	public void setRoutineInspectionTaskService(
			RoutineInspectionTaskService routineInspectionTaskService) {
		this.routineInspectionTaskService = routineInspectionTaskService;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
}
