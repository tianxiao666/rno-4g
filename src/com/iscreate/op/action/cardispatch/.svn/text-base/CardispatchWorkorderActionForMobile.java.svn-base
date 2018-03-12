package com.iscreate.op.action.cardispatch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.cardispatch.CardispatchCommonService;
import com.iscreate.op.service.cardispatch.CardispatchManageService;
import com.iscreate.op.service.cardispatch.CardispatchManageServiceForMobile;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderService;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderServiceForMobile;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;

@SuppressWarnings({"unused","deprecation","unchecked"})
public class CardispatchWorkorderActionForMobile {
	
	
	/************* 依赖注入 *************/
	
	private CardispatchWorkorderServiceForMobile cardispatchWorkorderServiceForMobile;
	private CardispatchWorkorderService cardispatchWorkorderService;
	private CardispatchManageServiceForMobile cardispatchManageServiceForMobile;
	private CardispatchCommonService cardispatchCommonService;
	private CardispatchManageService cardispatchManageService;
	private TaskTracingRecordService taskTracingRecordService;
	
	private SysOrgUserService sysOrgUserService;
	
	
	
	/********* 属性 *********/
	private String url;
	private Map<String,Object> actionMap = new LinkedHashMap<String, Object>();
	private String WOID;
	private Log log = LogFactory.getLog(this.getClass());
	
	/**************** action ******************/
	
	
	public void findCarLastMileage ( ) {
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String carId = paramMap.get("carId");
			Double mileage = cardispatchManageService.findCarLastMileage(carId);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(mileage));
			mch.addGroup("pageData", resultMap);
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * 查询车辆调度工单
	 * @reqeust_param - woId , state
	 * @description 	woId 为null时,查询所有车辆调度工单 
	 * 				 	state 为 CardispatchConstant常量 , 为null时查询所有状态
	 * (响应) (List<Map<String, String>>) 工单列表
	 */
	public void findCardispatchWordorderByStateForMobile () {
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String state = paramMap.get("state");
			Integer size = 0;
			if ( !StringUtil.isNullOrEmpty(paramMap.get("size")) ) {
				size = Integer.valueOf(paramMap.get("size"));
			}
			Map<String,String> param_map = new LinkedHashMap<String, String>();
			String accountId = (String) SessionService.getInstance().getValueByKey("userId");
			param_map.put("workorder.useCarPersonId", accountId);
			List<Map<String, String>> list = cardispatchWorkorderServiceForMobile.findCardispatchWordorderByStateForSize(param_map, state,size);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 查询车辆调度工单数量
	 * @reqeust_param - woId , state
	 * @description 	woId 为null时,查询所有车辆调度工单 
	 * 				 	state 为 CardispatchConstant常量 , 为null时查询所有状态
	 * (响应) (Integer) 工单数量
	 */
	public void findCardispatchWordorderByStateWithCountForMobile () {
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String state = paramMap.get("state");
			Map<String,String> param_map = new LinkedHashMap<String, String>();
			String accountId = (String) SessionService.getInstance().getValueByKey("userId");
			param_map.put("workorder.useCarPersonId", accountId);
			Integer num = cardispatchWorkorderServiceForMobile.findCardispatchWordorderByStateForSizeCount(param_map, state,null);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(num));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * 查询车辆调度工单
	 * @reqeust_param - woId , state
	 * @description 	woId 为null时,查询所有车辆调度工单 
	 * 				 	state 为 CardispatchConstant常量 , 为null时查询所有状态
	 * (响应) (List<Map<String, String>>) 工单列表
	 */
	public void findCardispatchWordorderByStateCountForMobile () {
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String state = paramMap.get("state");
			Integer size = 0;
			if ( !StringUtil.isNullOrEmpty(paramMap.get("size")) ) {
				size = Integer.valueOf(paramMap.get("size"));
			}
			List<Map<String, String>> list = cardispatchWorkorderServiceForMobile.findCardispatchWordorderByStateForSize(null, state,null);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list.size()));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * (请求参数) useCarTime_startTime returnCarTime_endTime criticalClass carNumber
	 */
	public void findCarWorkOrderListBySearchForMobile () {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			//获取条件参数
			String criticalClass = paramMap.get("criticalClass");
			String carNumber = paramMap.get("carNumber");
			Map<String,String> search_param_map = new LinkedHashMap<String, String>();
			search_param_map.put("workorder.criticalClass", criticalClass);
			search_param_map.put("carNumber", carNumber);
			ArrayUtil.removeEmpty(search_param_map);
			//获取范围条件参数
			String useCarTime_startTime = paramMap.get("useCarTime_startTime");
			String useCarTime_endTime = paramMap.get("useCarTime_endTime");
			//获取排序参数
			String order = paramMap.get("gender");
			String upordown = paramMap.get("upordown");
			Integer size = 1;
			if ( !StringUtil.isNullOrEmpty(paramMap.get("size")) ) {
				size = Integer.valueOf(paramMap.get("size"));
			}
			
			List<Map<String, String>> list_count = cardispatchWorkorderServiceForMobile.findCarWorkOrderListBySearchForMobile(search_param_map, useCarTime_startTime, useCarTime_endTime, order, upordown, null);
			int count = 0;
			if ( list_count != null && list_count.size() > 0 ) {
				count = list_count.size();
			}
			List<Map<String, String>> list = cardispatchWorkorderServiceForMobile.findCarWorkOrderListBySearchForMobile(search_param_map, useCarTime_startTime, useCarTime_endTime, order, upordown, size);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			resultMap.put("pageDataCount", gson.toJson(999));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * (请求参数) woTitle
	 */
	public void findCarWorkOrderListByWoTitleForMobile () {
		try{
		
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			//获取条件参数
			String woTitle = paramMap.get("woTitle");
			Map<String,String> search_param_map = new LinkedHashMap<String, String>();
			search_param_map.put("woTitle", woTitle);
			ArrayUtil.removeEmpty(search_param_map);
			//获取排序参数
			Integer size = 1;
			if ( !StringUtil.isNullOrEmpty(paramMap.get("size")) ) {
				size = Integer.valueOf(paramMap.get("size"));
			}
			//获取数量
			List<Map<String, String>> list_count = cardispatchWorkorderServiceForMobile.findCarWorkOrderListBySearchForMobile(search_param_map, null, null, null, null, null);
			int count = 0;
			if ( list_count != null && list_count.size() > 0 ) {
				count = list_count.size();
			}
			List<Map<String, String>> list = cardispatchWorkorderServiceForMobile.findCarWorkOrderListBySearchForMobile(search_param_map, null, null, null, null, size);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			resultMap.put("pageDataCount", gson.toJson(999));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	
	
	/**
	 * 进入工单页面 - 通过该工单状态跳转到响应页面
	 * (请求参数) 	WOID - 工单id
	 * 					driverCarId - 车辆司机配对id
	 * 					carNumber - 车辆牌照
	 * 					type - 页面类型(view ： 视图)
	 * (返回) (String) 跳转字符 
	 */
	public void enterCardispatchWorkorderActionForMobile() {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			WOID = paramMap.get("WOID");
			String url = cardispatchWorkorderServiceForMobile.enterCardispatchWorkorder(WOID, paramMap);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(url));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	public void findSingleCardispatchWorkorderByWoIdForMobile () {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			WOID = paramMap.get("WOID");
			
			
			Map<String,String> workorder = cardispatchWorkorderServiceForMobile.findSingleCardispatchWorkorderByWoIdForMobile(WOID);
			
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(workorder));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	public void findCardispatchWorkorderAssociateTaskByWoIdForMobile () {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			WOID = paramMap.get("WOID");
			
			
			Map<String,String> taskorder = cardispatchWorkorderServiceForMobile.findCardispatchWorkorderAssociateTaskByWoIdForMobile(WOID);
			
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(taskorder));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	public void findCardispatchWorkorderFeerecordByWoIdForMobile () {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			WOID = paramMap.get("WOID");
			List<Map<String, String>> list = cardispatchWorkorderServiceForMobile.findCardispatchWorkorderFeerecordByWoIdForMobile(WOID);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	public void deleteFeerecordByFeeIdForMobile () {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			Map<String, String> param_map = new LinkedHashMap<String, String>();
			param_map.put("id", paramMap.get("id"));
			boolean flag = cardispatchWorkorderService.deleteFeeAmount(param_map);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(flag));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	
	/**
	 * 非司机人员的自动补全
	 * (请求参数) name - 人名获取account
	 * (响应) (List<Map<String, String>>) 人员集合
	 */
	public void notDriverStaffAutoCompleteForMobile () {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String driverName = paramMap.get("name");
			List<Map<String, String>> list = cardispatchManageService.getNotDriverStaffAutoComplete(driverName,null);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * 根据当前登录人账号获取所在区域
	 * (响应) (List<String>) 区域集合 (由下到上 - 最后是省份 )
	 */
	public void getAreaIdByLoginPersonForMobile () {
		try { 
		
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			List<String> list = cardispatchCommonService.getAreaIdByLoginPerson();
	
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	public void getLoginUserForMobile(){
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			//当前登录人信息
			String loginPerson = (String) SessionService.getInstance().getValueByKey("userId");	
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(loginPerson);
//			Staff loginStaff = providerOrganizationService.getStaffByAccount(loginPerson);
			//loginStaff.setAccountObj(null);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(sysOrgUserByAccount));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	public void createCarDispatchWorkOrderActionForMobile () {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			Map<String, String> request_param_map = new LinkedHashMap<String, String>();
			request_param_map.put("applyDescription", paramMap.get("applyDescription"));
			request_param_map.put("useCarPersonId", paramMap.get("useCarPersonId"));
			request_param_map.put("planUseCarAddress", paramMap.get("planUseCarAddress"));
			request_param_map.put("planUseCarTime", paramMap.get("planUseCarTime"));
			request_param_map.put("planReturnCarTime", paramMap.get("planReturnCarTime"));
			request_param_map.put("criticalClass", paramMap.get("criticalClass"));
			request_param_map.put("useCarType", paramMap.get("useCarType"));
			
			
			Map<String, String> apply_param_map = new LinkedHashMap<String, String>();
			apply_param_map.put("associateWoId", paramMap.get("WOID"));
			apply_param_map.put("associateToId", paramMap.get("TOID"));
			apply_param_map.put("associateWorkType", paramMap.get("woType"));
			//判断是否有用车人
			Boolean flag = false;
			if( !request_param_map.containsKey("useCarPersonId") || StringUtil.isNullOrEmpty(request_param_map.get("useCarPersonId")) ) {
				System.err.println("CardispatchWorkorderAction --> createCarDispatchWorkOrderAction the 'useCarPeople' is null !");
				flag = false;
			} else {
				this.WOID = cardispatchWorkorderService.txCreateCardiaptchWorkorder(request_param_map,apply_param_map);
				if ( !StringUtil.isNullOrEmpty(WOID) ) {
					flag = true;
				}
			}
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(this.WOID));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * 用车
	 */
	public void useCarAction() {
		try { 
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
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
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
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}

	
	
	/**
	 * 还车
	 */
	public void returnCar() {
		try { 
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
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}	
			String content = mobilePackage.getContent();
					
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			// 获取表单提交的JSON信息的Map
			Map<String, String> contentMap = mch.getGroupByKey ("request");
			// 参数转换
			String WOID = contentMap.get("WOID");
			//String WOID = "CD-20121120-0016";//测试
			String realReturnCarMileage = contentMap.get("realReturnCarMileage");
			String longitude = contentMap.get("longitude");
			String latitude = contentMap.get("latitude");
			String address = contentMap.get("address");	
			Map<String, String> requestParamMap = new HashMap<String, String>();
			requestParamMap.put("woId", WOID);
			requestParamMap.put("realReturnCarMileage", realReturnCarMileage);
			requestParamMap.put("realReturnCarLongitude", longitude);
			requestParamMap.put("realReturnCarLatitude", latitude);
			requestParamMap.put("realReturnCarAddress", address);
			boolean flag = cardispatchWorkorderService.txReturnCar(requestParamMap, null);
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("returnFlag", flag+"");
			returnMobilePackage(returnMap, "returnMap");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 保存用车费用
	 * (请求参数) woId , feeType , feeAmount , description
	 * (响应) (boolean)保存是否成功 (true：成功)
	 */
	public void saveFeeAmount () {
		try {
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
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
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
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * 催办
	* @date Nov 21, 20129:39:40 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void remindersCarTaskAction(){
		try { 
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
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
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
			boolean returnFlag = this.cardispatchWorkorderServiceForMobile.remindersCarTask(WOID, remindersReason);
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("returnFlag", returnFlag+"");
			returnMobilePackage(returnMap, "returnMap");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	/**
	 * 返回maobilepackage
	 * @param returnMap
	 * @param de
	 */
	private void returnMobilePackage(Map<String, String> returnMap, String de) {
		MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
		HttpServletResponse response = ServletActionContext.getResponse();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		MobileContentHelper mch = new MobileContentHelper();
		mch.addGroup(de, returnMap);
		
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
	}
	
	/**
	 * 根据车辆woId,查询车辆调度单的服务跟踪记录
	 */
	public void findCardispatchTasktracerecordByWoIdActionForMobile ( ) {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			String woId = paramMap.get("WOID");
			List<Map<String,String>> list = cardispatchWorkorderServiceForMobile.findCardispatchTasktracerecordByWoIdForMobile(woId);
			
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(list));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	
	

	/**
	 * 进入催办页面
	 */
	public void enterCardispatchWorkorderUrgePageActionForMobile() {
		try { 
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
					
			MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
					
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(mobilePackage.getContent());
			
			//获取参数
			Map<String, String> paramMap = mch.getGroupByKey ("request");
			WOID = paramMap.get("WOID");
			String url = cardispatchWorkorderServiceForMobile.enterCardispatchWorkorder(WOID, paramMap);
			//返回信息
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("pageDataList", gson.toJson(WOID));
			mch.addGroup("pageData", resultMap);
			
			mobilePackage.setContent(mch.mapToJson ());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.error(e.getMessage());
			e.printStackTrace();
		} 
	}
	
	
	
	
	/*********** getter setter ***********/
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, Object> getActionMap() {
		return actionMap;
	}
	public void setActionMap(Map<String, Object> actionMap) {
		this.actionMap = actionMap;
	}
	
	public CardispatchWorkorderServiceForMobile getCardispatchWorkorderServiceForMobile() {
		return cardispatchWorkorderServiceForMobile;
	}
	public void setCardispatchWorkorderServiceForMobile(
			CardispatchWorkorderServiceForMobile cardispatchWorkorderServiceForMobile) {
		this.cardispatchWorkorderServiceForMobile = cardispatchWorkorderServiceForMobile;
	}

	public CardispatchManageServiceForMobile getCardispatchManageServiceForMobile() {
		return cardispatchManageServiceForMobile;
	}

	public void setCardispatchManageServiceForMobile( CardispatchManageServiceForMobile cardispatchManageServiceForMobile) {
		this.cardispatchManageServiceForMobile = cardispatchManageServiceForMobile;
	}


	public String getWOID() {
		return WOID;
	}


	public void setWOID(String woid) {
		WOID = woid;
	}

	public CardispatchWorkorderService getCardispatchWorkorderService() {
		return cardispatchWorkorderService;
	}

	public void setCardispatchWorkorderService(
			CardispatchWorkorderService cardispatchWorkorderService) {
		this.cardispatchWorkorderService = cardispatchWorkorderService;
	}

	public CardispatchCommonService getCardispatchCommonService() {
		return cardispatchCommonService;
	}

	public void setCardispatchCommonService(
			CardispatchCommonService cardispatchCommonService) {
		this.cardispatchCommonService = cardispatchCommonService;
	}

	public CardispatchManageService getCardispatchManageService() {
		return cardispatchManageService;
	}

	public void setCardispatchManageService(
			CardispatchManageService cardispatchManageService) {
		this.cardispatchManageService = cardispatchManageService;
	}



	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}

	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	
	
	
	
	
}
