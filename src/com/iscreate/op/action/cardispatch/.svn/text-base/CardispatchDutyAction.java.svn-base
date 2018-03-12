package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.service.cardispatch.CardispatchDutyService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;



@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchDutyAction {
	
	
	/************* 依赖注入 *************/
	
	private CardispatchDutyService cardispatchDutyService;
	
	
	/********* 属性 *********/
	private Log log = LogFactory.getLog(this.getClass());
	
	/**************** 页面数据 ******************/
	
	/********** XML 使用属性 ************/
	private String url;
	

	/************************* Action ******************************/
	/**
	 * 日历控件
	 * (请求参数) 	date - 日期集合
	 * 					carBizId - 车辆组织id
	 * 					carNumber - 车辆牌照
	 * 					freId - 班次id
	 * 					carId - 车辆Id
	 * (响应) (Map<String, Map<String, List<Map<String, Object>>>>)
	 * 			Map<日期,Map<班次,List<Map(数据)>>>
	 */
	public void carDutyCalendarAction () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("date","carBizId","carNumber","freId","carId");
		Map<String, Map<String, List<Map<String, Object>>>> dutyMap = null;
		if ( requestParamMap.get("carNumber") != null ) {
			if ( !StringUtil.isDbSensitiveStringExists(requestParamMap.get("carNumber")) ) {
				//查询数据库
				dutyMap = cardispatchDutyService.cardispatchDutyCalendar(requestParamMap);
			} else {
				dutyMap = new HashMap<String, Map<String,List<Map<String,Object>>>>();
			}
		}
		
		//响应到页面
		try {
			ActionUtil.responseWrite(dutyMap);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询排班列表
	 * (请求参数) 	date - 日期集合
	 * 					carBizId - 车辆组织id
	 * 					carNumber - 车辆牌照
	 * 					freId - 班次id
	 * 					carId - 车辆Id
	 * (响应) 	(Map<String, Map<String, Map<String, Map<String, Object>>>>)
	 * 			Map<车牌,Map<日期,Map<班次, Map(数据)>>>>
	 */
	public void carDudyListAction() {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("date","carBizId","carNumber","freId","carId");
		Map<String, Map<String, Map<String, Map<String, Object>>>> list = null;
		if ( requestParamMap.get("carNumber") != null ) {
			if ( !StringUtil.isDbSensitiveStringExists(requestParamMap.get("carNumber")) ) {
				//查询数据库
				list = cardispatchDutyService.cardispatchDutyList(requestParamMap);
			} else {
				list = new HashMap<String, Map<String, Map<String, Map<String, Object>>>>();
			}
		}
		//响应到页面
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 读取车辆排班班次
	 * (响应) (List<Map<String, Object>>) 班次信息集合
	 */
	public void findCarDutyFreqAction () {
		List<Map<String, Object>> list = cardispatchDutyService.findCarDutyFreq();
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新值班信息
	 * (请求参数) 	dutyDate 日期
	 * 				freId 班次
	 * 				delIds 删除车辆司机配对Id数组
	 * 				addIds 增加车辆司机配对Id数组
	 */
	public void dutyUpdateAction () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String dutyDate = request.getParameter("dutyDate");
		String freId = request.getParameter("freId");
		String delIds = request.getParameter("delIds");
		String addIds = request.getParameter("addIds");
		Gson gson = new Gson();
		List<Map<String, String>> delIds_list = gson.fromJson(delIds, new TypeToken<List<Map<String, String>>>() {}.getType());
		List<Map<String, String>> addIds_list = gson.fromJson(addIds, new TypeToken<List<Map<String, String>>>() {}.getType());
		Map requestParamMapObject = new HashMap();
		requestParamMapObject.put("dutyDate", dutyDate);
		requestParamMapObject.put("freId", freId);
		requestParamMapObject.put("delIds", delIds_list);
		requestParamMapObject.put("addIds", addIds_list);
		cardispatchDutyService.txupdateDuty(requestParamMapObject);
	}
	
	/**
	 * 根据日期班次，删除值班信息
	 * @request_param 	dutyDate , freId
	 */
	public void deleteDuty () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("dutyDate","freId");
		
		boolean flag = cardispatchDutyService.deleteDuty(requestParamMap);
	}
	
	
	/*********** getter setter ***********/
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public CardispatchDutyService getCardispatchDutyService() {
		return cardispatchDutyService;
	}
	public void setCardispatchDutyService(
			CardispatchDutyService cardispatchDutyService) {
		this.cardispatchDutyService = cardispatchDutyService;
	}
	
	
}

