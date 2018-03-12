package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.service.cardispatch.CardispatchCarLocusService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class CardispatchCarLocusAction {
	
	public CardispatchCarLocusService cardispatchCarLocusService;
	
	public Map carLocusInfoMap;

	public CardispatchCarLocusService getCardispatchCarLocusService() {
		return cardispatchCarLocusService;
	}

	public void setCardispatchCarLocusService(
			CardispatchCarLocusService cardispatchCarLocusService) {
		this.cardispatchCarLocusService = cardispatchCarLocusService;
	}
	
	
	
	public Map getCarLocusInfoMap() {
		return carLocusInfoMap;
	}

	public void setCarLocusInfoMap(Map carLocusInfoMap) {
		this.carLocusInfoMap = carLocusInfoMap;
	}

	/**
	 * 跳转到车辆状态监控页面
	 * @return
	 */
	public String loadCarStateMonitoringPageAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String woId = request.getParameter("woId");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		//参数处理
		if(beginTime==null || "".equals(beginTime) || beginTime.equals("null")){
			beginTime = TimeFormatHelper.getTimeFormatByDay(new Date());
			beginTime = beginTime+" 00:00:00";
		}
		if(endTime==null || "".equals(endTime) || endTime.equals("null")){
			endTime = TimeFormatHelper.getTimeFormatByDay(new Date());
			endTime = endTime+" 23:59:59";
		}
		String carNumber = "";
		try {
			carNumber = URLDecoder.decode(request.getParameter("carNumber"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String curPosition = request.getParameter("curPosition");
		
		carLocusInfoMap = new HashMap();
		carLocusInfoMap.put("carNumber", carNumber);
		carLocusInfoMap.put("woId", woId);
		carLocusInfoMap.put("beginTime", beginTime);
		carLocusInfoMap.put("endTime", endTime);
		carLocusInfoMap.put("curPosition", curPosition);
		return "success";
	}
	/**
	 * 获取GPS信息
	 * (车辆轨迹用)
	 */
	public void getGpsInfoForCarLocusAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String woId = request.getParameter("woId");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String carNumber = request.getParameter("carNumber");
		if(woId!=null && !"".equals(woId)){
			Map workOrderInfo = cardispatchCarLocusService.getCarWorkOrderInfoService(woId);
			if(workOrderInfo!=null){
				beginTime = (String)workOrderInfo.get("realUseCarTime");
				endTime = (String)workOrderInfo.get("realReturnCarTime");
			}
		}
		// 获取车辆轨迹
		Map beginGpsInfo = cardispatchCarLocusService.getGpsInfoByPickTime(carNumber, beginTime,endTime,"asc");
		Map endGpsInfo = cardispatchCarLocusService.getGpsInfoByPickTime(carNumber, beginTime,endTime,"desc");
		// 返回结果
		Map resMap = new HashMap();
		if(beginGpsInfo!=null){
			String bPickTime = (String)beginGpsInfo.get("pickTime");
			String bLatitude = (String)beginGpsInfo.get("weidu");
			String bLongitude = (String)beginGpsInfo.get("jingdu");
			resMap.put("bPickTime", bPickTime);
			resMap.put("bLatitude", bLatitude);
			resMap.put("bLongitude", bLongitude);
		}
		if(endGpsInfo!=null){
			String ePickTime = (String)endGpsInfo.get("pickTime");
			String eLatitude = (String)endGpsInfo.get("weidu");
			String eLongitude = (String)endGpsInfo.get("jingdu");
			resMap.put("ePickTime", ePickTime);
			resMap.put("eLatitude", eLatitude);
			resMap.put("eLongitude", eLongitude);
		}
		resMap.put("lbTime", beginTime);
		resMap.put("leTime", endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resMap, new TypeToken<Map>(){}.getType());		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
