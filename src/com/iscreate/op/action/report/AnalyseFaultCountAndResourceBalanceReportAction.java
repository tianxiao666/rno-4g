package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.AnalyseFaultCountAndResourceBalanceReportService;
import com.iscreate.sso.session.UserInfo;

public class AnalyseFaultCountAndResourceBalanceReportAction {

	private AnalyseFaultCountAndResourceBalanceReportService analyseFaultCountAndResourceBalanceReportService;
	
	private String beginTime;
	private String endTime;
	private List<String> yearMonthList;
	
	private Long orgId;
	private Long areaId;
	
	
	/**
	 * 获取当前登录人所在当前组织的子组织对应的每百个基站的人/车/任务量统计分析的数据action
	 * @throws IOException
	 */
	public void getAnalyseFaultCountAndResourceBalanceDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.analyseFaultCountAndResourceBalanceReportService.getAnalyseFaultCountAndResourceBalanceDataByUserId(userId,beginTime,endTime);
		response.getWriter().write(json);
	}
	
	/**
	 * 获取下级组织对应的每百个基站的人/车/任务量统计分析的数据action
	 * @throws IOException
	 */
	public void getSubOrgAnalyseFaultCountAndResourceBalanceDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String json=this.analyseFaultCountAndResourceBalanceReportService.getSubOrgAnalyseFaultCountAndResourceBalanceDataBySubOrgId(orgId,beginTime,endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 获取组织对应的每百个基站的人/车/任务量统计分析的环比数据action
	 * @throws IOException 
	 */
	public void getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json = this.analyseFaultCountAndResourceBalanceReportService.getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByUserId(userId,this.yearMonthList);
		response.getWriter().write(json);
	}
	
	/**
	 * 按项目获取对应的每百个基站的人/车/任务量统计分析的数据action
	 * @throws IOException
	 */
	public void getAnalyseFaultCountAndResourceBalanceDataForProjectAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String json=this.analyseFaultCountAndResourceBalanceReportService.getAnalyseFaultCountAndResourceBalanceDataByUserIdForProject(userId,beginTime,endTime);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 按项目获取每百个基站的人/车/任务量统计分析的环比数据action
	 * @throws IOException 
	 */
	public void getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataForProjectAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String json = this.analyseFaultCountAndResourceBalanceReportService.getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByOrgIdForProject(userId,this.yearMonthList);
		
		response.getWriter().write(json);
	}
	
	
	/**
	 * 判断组织是否具有下级组织
	 * @throws IOException 
	 */
	public void judgeOrgIsExistSubOrgAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		String json=this.analyseFaultCountAndResourceBalanceReportService.judgeOrgIsExistSubOrg(orgId);
		response.getWriter().write(json);
	}
	
	
	
	
	/**
	 * 获取当前登录人的最高级组织
	 * @throws IOException 
	 */
	public void getUserTopOrgAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		Gson gson=new Gson();
		String json=this.analyseFaultCountAndResourceBalanceReportService.getUserTopOrg(userId);
		response.getWriter().write(json);
	}
	
	
	/**
	 * 获取父级组织
	 * @throws IOException
	 */
	public void getParentOrgBySubOrgAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		Gson gson=new Gson();
		String json=this.analyseFaultCountAndResourceBalanceReportService.getParentOrg(orgId);
		response.getWriter().write(json);
	}
	
	
	
	/**   -----------  getter and setter  ----------------------   */
	
	public void setAnalyseFaultCountAndResourceBalanceReportService(
			AnalyseFaultCountAndResourceBalanceReportService analyseFaultCountAndResourceBalanceReportService) {
		this.analyseFaultCountAndResourceBalanceReportService = analyseFaultCountAndResourceBalanceReportService;
	}
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
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
