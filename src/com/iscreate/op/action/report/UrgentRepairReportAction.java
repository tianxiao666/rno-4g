package com.iscreate.op.action.report;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.iscreate.op.pojo.system.SysOrg;

import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.report.RoutineinspectionReportService;
import com.iscreate.op.service.report.UrgentRepairReportService;
import com.iscreate.op.service.system.SysOrganizationService;

/**
 * 统计故障抢修报表
 *
 */
public class UrgentRepairReportAction {
	//组织ID
	private long orgId;
	
	
	private String orgName;
	
	private String DateTimeString;
	
	//开始时间
	private String beginTime;
	
	//结束时间
	private String endTime;
	
	private List<Map<String, Object>> urgentRepairCountList;
	
	//抢修报表类型
	private String reportType;
	
	//筛选字段
	private String rowName;
	
	
	//筛选字段值
	private String rowValue;
	
	//筛选条件
	private String judge;
	
	//维度
	private String statisticsType;
	
	//列名
	private String rowNameText;
	
	private Map<String, Object> treeNode;
	
	private List<Map<String, Object>> urgentRepairworkorderCountList;
	
	private RoutineinspectionReportService routineinspectionReportService;
	
	
	private UrgentRepairReportService urgentRepairReportService;


	
	private List<Map<String, Object>> orgRoProjectList;
	
	private SysOrganizationService sysOrganizationService;
	
	
	
	private String type;

	public List<Map<String, Object>> getOrgRoProjectList() {
		return orgRoProjectList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOrgRoProjectList(List<Map<String, Object>> orgRoProjectList) {
		this.orgRoProjectList = orgRoProjectList;
	}


	public UrgentRepairReportService getUrgentRepairReportService() {
		return urgentRepairReportService;
	}

	public void setUrgentRepairReportService(
			UrgentRepairReportService urgentRepairReportService) {
		this.urgentRepairReportService = urgentRepairReportService;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDateTimeString() {
		return DateTimeString;
	}

	public void setDateTimeString(String dateTimeString) {
		DateTimeString = dateTimeString;
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

	
	public List<Map<String, Object>> getUrgentRepairCountList() {
		return urgentRepairCountList;
	}

	public void setUrgentRepairCountList(
			List<Map<String, Object>> urgentRepairCountList) {
		this.urgentRepairCountList = urgentRepairCountList;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getRowValue() {
		return rowValue;
	}

	public void setRowValue(String rowValue) {
		this.rowValue = rowValue;
	}

	public String getJudge() {
		return judge;
	}

	public void setJudge(String judge) {
		this.judge = judge;
	}

	public String getStatisticsType() {
		return statisticsType;
	}

	public void setStatisticsType(String statisticsType) {
		this.statisticsType = statisticsType;
	}

	public String getRowNameText() {
		return rowNameText;
	}

	public void setRowNameText(String rowNameText) {
		this.rowNameText = rowNameText;
	}
	
	/**
	 * 获取工单（入口）
	* @date Nov 2, 20125:04:19 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String getUrgentRepairAction(){
		String returnString = "";
		//获取当前登录人
		String userId = (String) SessionService.getInstance().getValueByKey(
		"userId");
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		if(topLevelOrgByAccount != null && !topLevelOrgByAccount.isEmpty()){
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			if(providerOrganization != null){
				this.orgId = providerOrganization.getOrgId();
				this.orgName = providerOrganization.getName();
			}
		}
		treeNode = new HashMap<String, Object>();
		treeNode = urgentRepairReportService.getTreeNode();
		if(this.reportType != null){
			returnString = this.reportType;
		}
		return returnString;
	}
	
	/**
	 * 获取故障工单处理及时率(按组织)
	* @date Nov 2, 20125:05:30 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByBizunitinnstAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按基站类型)
	* @date Nov 2, 20125:09:51 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByBaseStationLevelAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByBaseStationLevel(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障类型)
	* @date Nov 2, 20125:11:31 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultTypeAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultType(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障级别)
	* @date Nov 2, 20125:13:03 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultLevelAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultLevel(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按受理专业)
	* @date Nov 2, 20125:13:38 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByAcceptProfessionalAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByAcceptProfessional(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障大类)
	* @date Nov 2, 20125:14:30 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultGeneraAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultGenera(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取工单信息
	* @date Nov 6, 20129:38:08 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String getUrgentRepairWorkorderManageAction(){
		if(reportType != null && !reportType.equals("")){
			try {
				this.reportType = new String(this.reportType.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(orgName != null && !orgName.equals("")){
			try {
				this.orgName = new String(this.orgName.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(DateTimeString != null && !DateTimeString.equals("")){
			try {
				this.DateTimeString = new String(this.DateTimeString.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(judge != null && !judge.equals("")){
			try {
				this.judge = new String(this.judge.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(rowValue != null && !rowValue.equals("")){
			try {
				this.rowValue = new String(this.rowValue.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(this.statisticsType.equals("biz")){
			this.urgentRepairCountList = urgentRepairReportService.getUrgentRepairWorkorderManage(orgId, beginTime, endTime, rowName, judge, rowValue);
		}else{
			this.urgentRepairCountList = urgentRepairReportService.getUrgentRepairWorkorderManageProject(orgId, beginTime, endTime, rowName, judge, rowValue);	
		}
		return "success";
	}

	public Map<String, Object> getTreeNode() {
		return treeNode;
	}

	public void setTreeNode(Map<String, Object> treeNode) {
		this.treeNode = treeNode;
	}
	
	/**
	 * 自定义报表
	* @date Nov 6, 201211:09:55 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String getUrgentRepairWorkorderByrowNameAction(){
		if(rowName != null && !rowName.equals("")){
			try {
				this.rowName = new String(this.rowName.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(rowNameText != null && !rowNameText.equals("")){
			try {
				this.rowNameText = new String(this.rowNameText.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(judge != null && !judge.equals("")){
			try {
				this.judge = new String(this.judge.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(rowValue != null && !rowValue.equals("")){
			try {
				this.rowValue = new String(this.rowValue.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(orgName != null && !orgName.equals("")){
			try {
				this.orgName = new String(this.orgName.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		treeNode = urgentRepairReportService.getTreeNode();
		urgentRepairworkorderCountList = urgentRepairReportService.getUrgentRepairProcessTimeRateByOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
		return "success";
	}

	public List<Map<String, Object>> getUrgentRepairworkorderCountList() {
		return urgentRepairworkorderCountList;
	}

	public void setUrgentRepairworkorderCountList(
			List<Map<String, Object>> urgentRepairworkorderCountList) {
		this.urgentRepairworkorderCountList = urgentRepairworkorderCountList;
	}

	/**
	 * 获取故障工单处理及时率(按组织)向上
	* @date Nov 2, 20125:05:30 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByBizunitinnstTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按基站类型)向上
	* @date Nov 2, 20125:09:51 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByBaseStationLevelAndTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByBaseStationLevelAndTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障类型)向上
	* @date Nov 2, 20125:11:31 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultTypeAndTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultTypeAndTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障级别)向上
	* @date Nov 2, 20125:13:03 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultLevelAndTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultLevelAndTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按受理专业)向上
	* @date Nov 2, 20125:13:38 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByAcceptProfessionalAndTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByAcceptProfessionalAndTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按故障大类)向上
	* @date Nov 2, 20125:14:30 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByFaultGeneraAndTopOrgAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairProcessTimeRateByFaultGeneraAndTopOrg(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	 * 获取故障工单处理及时率(按项目)
	* @date Nov 2, 20125:05:30 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getUrgentRepairProcessTimeRateByProjectAction(){
		List<Map<String,Object>> list = urgentRepairReportService.getUrgentRepairByOrgCountProject(orgId, beginTime, endTime, rowName, judge, rowValue);
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
	
	
	//获取userId获取用户身份
//	public String loadUrgentRepairReportAssortmentAction(){
//		//获取当前登录人
//		String userId = (String) SessionService.getInstance().getValueByKey(
//		"userId");
//		Map<String,String> loginIdBelongEnterpriseTypeService = this.sysOrganizationService.getLoginIdBelongEnterpriseTypeService();
////		Map<String, String> loginIdBelongEnterpriseTypeService = this.providerOrganizationService.getLoginIdBelongEnterpriseTypeService();
//		if(loginIdBelongEnterpriseTypeService != null ){
//			String isCoo = loginIdBelongEnterpriseTypeService.get("isCoo");
//			if("true".equals(isCoo)){
//				type = "Org";
//			}else{
//				type = "Project";
//			}
//		}
//		orgRoProjectList = this.urgentRepairReportService.getLoginIdBelongEnterpriseType(userId);
//		return "success";
//	}

	public RoutineinspectionReportService getRoutineinspectionReportService() {
		return routineinspectionReportService;
	}

	public void setRoutineinspectionReportService(
			RoutineinspectionReportService routineinspectionReportService) {
		this.routineinspectionReportService = routineinspectionReportService;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
}
