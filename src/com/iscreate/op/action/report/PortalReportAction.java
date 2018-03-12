package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.PortalReportService;

public class PortalReportAction {
	private String startTime;
	
	private PortalReportService portalReportService;
	
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	//报表类型
	private String reportType;
	
	private String indicators;
	
	private String dimension;
	
	private long bizunitinnstId;
	
	private String statisticaltime;
	
	private long reportId;
	
	
	private String userId;
	
	private String userName;
	
	private String commentTime;
	
	private String commentText;
	
	private String fileName;
	
	private String endTime;
	
	private String type;
	
	private long orgId;
	
	private String projectId;
	
	

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public PortalReportService getPortalReportService() {
		return portalReportService;
	}

	public void setPortalReportService(PortalReportService portalReportService) {
		this.portalReportService = portalReportService;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getIndicators() {
		return indicators;
	}

	public void setIndicators(String indicators) {
		this.indicators = indicators;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public long getBizunitinnstId() {
		return bizunitinnstId;
	}

	public void setBizunitinnstId(long bizunitinnstId) {
		this.bizunitinnstId = bizunitinnstId;
	}

	public String getStatisticaltime() {
		return statisticaltime;
	}

	public void setStatisticaltime(String statisticaltime) {
		this.statisticaltime = statisticaltime;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 根据受理专业获取故障抢修单
	* @date Nov 2, 20125:30:35 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairReportOrderCountByAcceptProfessionalAction(){
		//获取当前登录人
		String userId = (String) SessionService.getInstance().getValueByKey(
		"userId");
		Map<String, Object> urgentRepairReportOrderCountByAcceptProfessional = portalReportService.getUrgentRepairReportOrderCountByAcceptProfessional(userId, startTime, endTime);
		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(urgentRepairReportOrderCountByAcceptProfessional);		
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据时间与组织统计
	* @date Nov 2, 20125:31:05 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairReportOrderCountByByWotemplateAction(){
		//获取当前登录人
		String userId = (String) SessionService.getInstance().getValueByKey(
		"userId");
		Map<String, Object> urgentRepairReportOrderCountByAcceptProfessional = portalReportService.getOrderCountByWotemplate(userId, startTime, endTime);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(urgentRepairReportOrderCountByAcceptProfessional);		
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPortalReportUrgentRepairReportReportAction(){
		log.info("进入===getPortalReportUrgentRepairReportReportAction");
		Map<String, Object> urgentRepairReportOrderCountByAcceptProfessional = new HashMap<String, Object>();
		if(type != null && !"".equals(type)){
			if("Org".equals(type)){
				urgentRepairReportOrderCountByAcceptProfessional = this.portalReportService.getReportByOrgId(orgId, startTime, endTime);
			}else if("Project".equals(type)){
				urgentRepairReportOrderCountByAcceptProfessional = this.portalReportService.getProjectByOrgId(projectId, startTime, endTime);
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
		String result = gson.toJson(urgentRepairReportOrderCountByAcceptProfessional);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("退出===getPortalReportUrgentRepairReportReportAction");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
}
