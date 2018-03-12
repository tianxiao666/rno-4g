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
import com.iscreate.op.service.report.OrgStaffCountMonthReportService;

import com.iscreate.sso.session.UserInfo;

/**
 * 统计组织工单数报表
 *
 */
public class OrgStaffCountMonthReportAction {
	private OrgStaffCountMonthReportService orgStaffCountMonthReportService;
	private long orgId;
	private String beginTime;
	private String endTime;
	private List<String> yearMonthList;	//年月List

	
	public OrgStaffCountMonthReportService getOrgStaffCountMonthReportService() {
		return orgStaffCountMonthReportService;
	}
	public void setOrgStaffCountMonthReportService(
			OrgStaffCountMonthReportService orgStaffCountMonthReportService) {
		this.orgStaffCountMonthReportService = orgStaffCountMonthReportService;
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
	 * 获取当前用户组织架构下人员数统计
	 */
	public void getUserOrgStaffCountReportAction(){
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		//获取数据
		Map reportInfo = orgStaffCountMonthReportService.getUserOrgStaffrCountReportService(userId, beginTime, endTime);
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
	 * 根据组织Id获取下级组织人员数量报表信息
	 */
	public void getNextOrgStaffCountReportByOrgIdAction(){
		//获取数据
		Map reportInfo = orgStaffCountMonthReportService.getNextOrgStaffCountReportInfoService(orgId, beginTime, endTime);
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
	 * 获取组织下人员数据环比报表信息
	 */
	public void getOrgStaffCountChainReportAction(){
		//获取数据
		Map resMap = orgStaffCountMonthReportService.getOrgStaffCountChainReportService(orgId, yearMonthList);
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
	 * 获取组织下人员技能分类统计报表信息
	 */
	public void getOrgStaffSkillReportAction(){
		//获取数据
		Map resMap = orgStaffCountMonthReportService.getUserOrgStaffSkillReportService(orgId, beginTime, endTime);
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
	 * 获取上级组织人员数量报表信息
	 */
	public void getUpOrgStaffCountReportByOrgIdAction(){
		//获取数据
		Map reportInfo = orgStaffCountMonthReportService.getUpOrgStaffCountReportInfoService(orgId, beginTime, endTime);
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
	 * 获取上级组织人员数据环比报表信息
	 */
	public void getUpOrgStaffCountChainReportAction(){
		//获取数据
		Map resMap = orgStaffCountMonthReportService.getUpOrgStaffCountChainReportService(orgId, yearMonthList);
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
	 * 获取组织架构下项目人员数统计
	 */
	public void getOrgProjectStaffCountReportAction(){
		Map resMap = orgStaffCountMonthReportService.getOrgProjectStaffCountReportService(orgId, beginTime, endTime);
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
