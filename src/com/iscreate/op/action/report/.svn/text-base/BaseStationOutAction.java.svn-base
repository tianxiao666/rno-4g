package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.BaseStationOutService;

public class BaseStationOutAction {
	public BaseStationOutService baseStationOutService;
	public String beginTime;
	public String endTime;
	
	public long orgId;
	
	

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
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

	public BaseStationOutService getBaseStationOutService() {
		return baseStationOutService;
	}

	public void setBaseStationOutService(BaseStationOutService baseStationOutService) {
		this.baseStationOutService = baseStationOutService;
	}
	
	public void getbaseStationOutByOrgAction(){
		//获取当前登录人
		String userId = (String) SessionService.getInstance().getValueByKey(
		"userId");
		List<Map<String, Object>> getbaseStationOutByOrg = this.baseStationOutService.getbaseStationOutByOrg(userId, beginTime, endTime);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(getbaseStationOutByOrg);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getbaseStationOutByOrgIdAction(){
		List<Map<String, Object>> getbaseStationOutByOrg = this.baseStationOutService.getbaseStationOutByOrgId(orgId, beginTime, endTime);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(getbaseStationOutByOrg);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getbaseStationOutByOrgIdTopOrgAction(){
		List<Map<String, Object>> getbaseStationOutByOrg = this.baseStationOutService.getbaseStationOutByOrgIdTopOrg(orgId, beginTime, endTime);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(getbaseStationOutByOrg);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getbaseStationOutByOrgIdProjectAction(){
		List<Map<String, Object>> getbaseStationOutByOrg = this.baseStationOutService.getbaseStationOutByOrgIdProject(orgId, beginTime, endTime);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(getbaseStationOutByOrg);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
