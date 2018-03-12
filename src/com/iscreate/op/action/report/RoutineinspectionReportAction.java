package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.RoutineinspectionReportService;

public class RoutineinspectionReportAction {
	
	private RoutineinspectionReportService routineinspectionReportService;
	

	
	private List<Map<String, Object>> orgRoProjectList;
	
	private String projectId;
	
	private long orgId;
	
	private String type;
	
	private Log log = LogFactory.getLog(this.getClass());

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public List<Map<String, Object>> getOrgRoProjectList() {
		return orgRoProjectList;
	}

	public void setOrgRoProjectList(List<Map<String, Object>> orgRoProjectList) {
		this.orgRoProjectList = orgRoProjectList;
	}

	

	public RoutineinspectionReportService getRoutineinspectionReportService() {
		return routineinspectionReportService;
	}

	public void setRoutineinspectionReportService(
			RoutineinspectionReportService routineinspectionReportService) {
		this.routineinspectionReportService = routineinspectionReportService;
	}
	
	//获取userId获取用户身份
	public String loadRoutineinspectionReportAssortmentAction(){
		//获取当前登录人
		String userId = (String) SessionService.getInstance().getValueByKey(
		"userId");
		orgRoProjectList = this.routineinspectionReportService.getLoginIdBelongEnterpriseType(userId);
		return "success";
	}
	
	//获取项目ID该组织以下项目的巡检报表数据TOP4或者组织ID获取该组织以下组织的巡检报表数据TOP6
	public void getRoutineinspectionReportAction(){
		List<Map<String, Object>> routineinspectionReport = new ArrayList<Map<String,Object>>();
		if(type != null && !"".equals(type)){
			if("Org".equals(type)){
				routineinspectionReport = this.routineinspectionReportService.getRoutineinspectionReportTOPSixByOrgId(orgId);
			}else if("Project".equals(type)){
				routineinspectionReport = this.routineinspectionReportService.getRoutineinspectionReportTOPFourByProjectId(projectId);
			}else{
				log.error("type没有对应值");
			}
		}else{
			log.info("type为空");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(routineinspectionReport);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
