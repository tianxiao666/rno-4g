package com.iscreate.op.action.report;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.FaultBaseStationTopTenReportService;
import com.iscreate.sso.session.UserInfo;

public class FaultBaseStationTopTenReportAction {


	private FaultBaseStationTopTenReportService faultBaseStationTopTenReportService;
	
	private String beginTime;
	private String endTime;
	
	private long orgId;
	private long projectId;
	
	


	/**
	 * 获取基站故障数前十action
	 * @throws IOException
	 */
	public void getFaultBaseStationTopTenDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.faultBaseStationTopTenReportService.getFaultBaseStationTopTenData(userId,beginTime,endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 根据用户所选组织的，获取前10基站故障数的数据
	 * @throws IOException
	 */
	public void getFaultBaseStationTopTenDataByOrgAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.faultBaseStationTopTenReportService.getFaultBaseStationTopTenDataByOrg(orgId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按项目获取数据
	 * @throws IOException
	 */
	public void getFaultBaseStationTopTenDataForProjectAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String json=this.faultBaseStationTopTenReportService.getFaultBaseStationTopTenDataForProject(userId, beginTime, endTime);
		
		response.getWriter().write(json);
	}
	
	
	/**
	 * 根据用户所选项目，获取前10基站故障数的数据
	 * @throws IOException
	 */
	public void getFaultBaseStationTopTenDataByProjectAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String json=this.faultBaseStationTopTenReportService.getFaultBaseStationTopTenDataByProject(projectId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	
	
	public void getProjectTreeDataAction() throws IOException{
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String json=this.faultBaseStationTopTenReportService.getProjectTreeData(userId);
		
		response.getWriter().write(json);
	}
	
	/**   -----------  getter and setter  ----------------------   */
	

	public void setFaultBaseStationTopTenReportService(
			FaultBaseStationTopTenReportService faultBaseStationTopTenReportService) {
		this.faultBaseStationTopTenReportService = faultBaseStationTopTenReportService;
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
	
	

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
}
