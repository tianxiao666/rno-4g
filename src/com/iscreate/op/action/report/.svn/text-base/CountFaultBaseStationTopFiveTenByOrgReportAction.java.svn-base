package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.CountFaultBaseStationTopFiveTenByOrgReportService;
import com.iscreate.sso.session.UserInfo;

public class CountFaultBaseStationTopFiveTenByOrgReportAction {

	private CountFaultBaseStationTopFiveTenByOrgReportService countFaultBaseStationTopFiveTenByOrgReportService;
	
	private String beginTime;
	private String endTime;
	private Long orgId;
	private Long areaId;
	
	private List<String> yearMonthList;
	
	
	/**
	 * 获取当前登录人所在当前组织的子组织对应的基站故障数分布的数据action
	 * @throws IOException
	 */
	public void getCountFaultBaseStationTopFiveTenDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenDataByUserId(userId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 获取下级组织对应的基站故障数分布的数据action
	 * @throws IOException
	 */
	public void getSubOrgCountFaultBaseStationTopFiveTenDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenDataBySubOrgId(orgId,beginTime,endTime);
		response.getWriter().write(json);
	}
	
	/**
	 * 获取组织对应的基站故障数分布的环比数据action
	 * @throws IOException 
	 */
	public void getCountFaultBaseStationTopFiveTenLoopCompareDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json = this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserId(userId,this.yearMonthList);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按地域获取当前登录人所在当前组织的对应的区域的基站故障数分布的数据action
	 * @throws IOException 
	 */
	public void getCountFaultBaseStationTopFiveTenDataForAreaAction() throws IOException{
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json = this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenDataByUserIdForArea(userId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	/**
	 * 按地域获取下级区域对应的基站故障数分布的数据action
	 * @throws IOException
	 */
	public void getSubAreaCountFaultBaseStationTopFiveTenDataForAreaAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenDataBySubAreaIdForArea(areaId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按地域获取区域对应的基站故障数分布的环比数据action
	 * @throws IOException
	 */
	public void getCountFaultBaseStationTopFiveTenLoopCompareDataForAreaAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserIdForArea(userId, this.yearMonthList);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按项目获取当前登录人所在当前组织的对应的项目的基站故障数分布的数据action
	 * @throws IOException 
	 */
	public void getCountFaultBaseStationTopFiveTenDataForProjectAction()  throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json = this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenDataByUserIdForProject(userId, beginTime, endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按项目获取对应的基站故障数分布的环比数据action
	 * @throws IOException
	 */			
	public void getCountFaultBaseStationTopFiveTenLoopCompareDataForProjectAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getCountFaultBaseStationTopFiveTenReportLoopCompareDataByOrgIdForProject(userId, this.yearMonthList);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 获取区域信息
	 * @throws IOException
	 */
	public void getAreaInfoByAreaIdAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		Gson gson=new Gson();
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getAreaInfoByAreaId(areaId);
		response.getWriter().write(json);
	}
	
	
	
	/**
	 * 获取父级区域
	 * @throws IOException
	 */
	public void getParentAreaBySubAreaAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		Gson gson=new Gson();
		String json=this.countFaultBaseStationTopFiveTenByOrgReportService.getParentArea(areaId);
		response.getWriter().write(json);
	}
	
	
	/**   -----------  getter and setter  ----------------------   */
	
	public void setCountFaultBaseStationTopFiveTenByOrgReportService(
			CountFaultBaseStationTopFiveTenByOrgReportService countFaultBaseStationTopFiveTenByOrgReportService) {
		this.countFaultBaseStationTopFiveTenByOrgReportService = countFaultBaseStationTopFiveTenByOrgReportService;
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
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public List<String> getYearMonthList() {
		return yearMonthList;
	}

	public void setYearMonthList(List<String> yearMonthList) {
		this.yearMonthList = yearMonthList;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
}
