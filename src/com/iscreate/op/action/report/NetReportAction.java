package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.report.NetReportService;
import com.iscreate.sso.session.UserInfo;
import com.opensymphony.xwork2.ActionContext;

public class NetReportAction {
	
	private String areaId;
	
	private long orgId;
	
	private NetReportService netReportService;
	
	
	public void getStationByAreaAction(){
		String userId = getUserId();
		List<Map<String,String>> list = this.netReportService.getStationByArea(userId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByAreaAction(){
		String userId = getUserId();
		List<Map<String,String>> list = this.netReportService.getWANByArea(userId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getStationByOrgAction(){
		String userId = getUserId();
		List<Map<String,String>> list = this.netReportService.getStationByOrg(userId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getWANByOrgAction(){
		String userId = getUserId();
		List<Map<String,String>> list = this.netReportService.getWANByOrg(userId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getStationByAreaIdAction(){
		List<Map<String,String>> list = this.netReportService.getStationByAreaId(this.areaId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByAreaIdAction(){
		List<Map<String,String>> list = this.netReportService.getWANByAreaId(this.areaId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getStationByOrgIdAction(){
		List<Map<String,String>> list = this.netReportService.getStationByOrgId(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByOrgIdAction(){
		List<Map<String,String>> list = this.netReportService.getWANByOrgId(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public void getStationByAreaIdAndTopOrgAction(){
		List<Map<String,String>> list = null;
		String userId = getUserId();
		list = this.netReportService.getStationByAreaIdAndTopOrg(userId,this.areaId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByAreaIdAndTopOrgAction(){
		String userId = getUserId();
		List<Map<String,String>> list = this.netReportService.getWANByAreaIdAndTopOrg(userId,this.areaId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void getStationByOrgIdAndTopOrgAction(){
		List<Map<String,String>> list = this.netReportService.getStationByOrgIdAndTopOrg(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByOrgIdAndTopOrgAction(){
		List<Map<String,String>> list = this.netReportService.getWANByOrgIdAndTopOrg(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getStationByOrgIdProjectAction(){
		List<Map<String,String>> list = this.netReportService.getStationByOrgIdProject(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getWANByOrgIdProjectAction(){
		List<Map<String,String>> list = this.netReportService.getWANByOrgIdProject(this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取用户id
	 * 
	 * @return Jul 4, 2012 3:24:08 PM gmh
	 */
	private String getUserId() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> requestMap = (Map<String, Object>) ctx
				.get("request");
		String userId = (String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERID);

		return userId;
	}
	
	

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public NetReportService getNetReportService() {
		return netReportService;
	}

	public void setNetReportService(NetReportService netReportService) {
		this.netReportService = netReportService;
	}

}
