package com.iscreate.op.action.report;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.OrgWorkOrderCountMonthReportService;

import com.iscreate.sso.session.UserInfo;

/**
 * 统计组织工单数报表
 *
 */
public class OrgWorkOrderCountMonthReportAction {
	private OrgWorkOrderCountMonthReportService orgWorkOrderCountMonthReportService;
	private long orgId;
	private String beginTime;
	private String endTime;
	private List<String> yearMonthList;

	public OrgWorkOrderCountMonthReportService getOrgWorkOrderCountMonthReportService() {
		return orgWorkOrderCountMonthReportService;
	}
	public void setOrgWorkOrderCountMonthReportService(
			OrgWorkOrderCountMonthReportService orgWorkOrderCountMonthReportService) {
		this.orgWorkOrderCountMonthReportService = orgWorkOrderCountMonthReportService;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public long getOrgId() {
		return orgId;
	}
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
	
	public List<String> getYearMonthList() {
		return yearMonthList;
	}
	public void setYearMonthList(List<String> yearMonthList) {
		this.yearMonthList = yearMonthList;
	}
	/**
	 * 获取当前用户组织架构下工单数统计
	 */
	public void getUserOrgWorkOrderCountReportAction(){
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getUserOrgWorkOrderCountReportService(userId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 根据组织Id获取下级组织报表信息
	 */
	public void getNextOrgWorkOrderCountReportByOrgIdAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getNextOrgReportInfoService(orgId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	
	/**
	 * 获取组织工单数环比报表信息
	 */
	public void getOrgWorkOrderCountChainReportAction(){
		//获取数据
		Map resMap = orgWorkOrderCountMonthReportService.getOrgWorkOrderCountChainReportService(orgId, yearMonthList);
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
	/**
	 * 获取上级组织工单数报表信息
	 */
	public void getUpOrgWorkOrderCountReportByOrgIdAction(){
		Map resMap = orgWorkOrderCountMonthReportService.getUpOrgReportInfoService(orgId, beginTime, endTime);
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
	/**
	 * 获取上级组织工单数环比报表信息
	 */
	public void getUpOrgWorkOrderCountChainReportAction(){
		//获取数据
		Map resMap = orgWorkOrderCountMonthReportService.getUpOrgWorkOrderCountChainReportService(orgId, yearMonthList);
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
	/**
	 * 获取组织架构下 项目工单数统计
	 */
	public void getOrgProjectWorkOrderCountReportAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getUserOrgProjectWorkOrderCountReportService(orgId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	
	/*------------------ 人员及任务量统计 -------------------------------*/
	/**
	 * 获取用户组织架构下人员及任务数统计
	 */
	public void getUserOrgCompareReportAction(){
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getUserOrgCompareReportService(userId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 根据组织Id获取 人员及任务量统计 信息
	 */
	public void getNextOrgCompareReportByOrgIdAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getOrgCompareReportService(orgId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 人员及任务数 环比数据
	 */
	public void getOrgCompareChainReportAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getOrgCompareChainReportService(orgId, yearMonthList);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 获取上级组织 人员及任务量统计 信息
	 */
	public void getUpOrgCompareReportByOrgIdAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getUpOrgCompareReportService(orgId, beginTime, endTime);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 获取上级组织 人员及任务数 环比数据
	 */
	public void getUpOrgCompareChainReportAction(){
		//获取数据
		Map reportInfo = orgWorkOrderCountMonthReportService.getUpOrgCompareChainReportService(orgId, yearMonthList);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportInfo, new TypeToken<Map>(){}.getType());		
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
	/**
	 * 获取组织下 项目的人员及任务数统计
	 */
	public void getOrgProjectStaffAndWorderCountReportAction(){
		Map resMap = orgWorkOrderCountMonthReportService.getOrgProjectStaffAndWorkOrderCountReportService(orgId, beginTime, endTime);
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
