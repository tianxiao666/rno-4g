package com.iscreate.op.action.routineinspection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;

import com.iscreate.op.pojo.system.SysOrg;

import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.routineinspection.RoutineInspectionTaskService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.paginghelper.PagingHelper;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;
import com.iscreate.sso.session.UserInfo;

public class RoutineInspectionTaskAction {

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskService routineInspectionTaskService;
	
	
	private ServiceBean workFlowService;
	
	private String WOID;
	private String TOID;
	private String orgId;   //组织Id
	private String type;   //巡检类型
	private String status;  //状态
	private String planName;    //计划名
	private String taskName;    //任务名
	private String resourceName;   //机房名
	private String resourceLevel;  //机房等级
	private int currentPage=1;
	private int totalPage=1;
	private int pageSize;
	private Map<String,String> taskInfoMap;
	private List<Map<String,Object>> taskList;
	private int taskCount;    //任务总数
	private int taskCloseCount;   //已关闭数
	private int noStartCount;    //待巡检数
	private int startingCount;   //巡检中数
	private Float averageDeviate;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	/**
	 * 获取工单所属的任务单信息
	 */
	public String loadRoutineInspectionTaskInfoByWoIdAction(){
		this.taskList = new ArrayList<Map<String,Object>>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		if(this.orgId != null && !"".equals(this.orgId)){
			intParams.put("orgId", this.orgId);
		}
		if(this.WOID != null && !"".equals(this.WOID)){
			intParams.put("woId", this.WOID);
		}
		if(this.resourceLevel != null && !"".equals(this.resourceLevel)){
			intParams.put("resourceLevel", this.resourceLevel);
		}
		if(this.resourceName != null && !"".equals(this.resourceName)){
			strParams.put("resourceName", this.resourceName);
		}
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		Map<String, Object> map = this.routineInspectionTaskService.getRoutineInspectionInfoByWoIdtoPageService(this.currentPage, this.pageSize, strParams, intParams);
		if(map!=null){
			int totalCount = Integer.parseInt(map.get("count")+"");
			PagingHelper ph = new PagingHelper();
			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
			if(pageMap!=null && pageMap.size()>0){
				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
			}
			this.taskList = (List<Map<String,Object>>)map.get("entityList");
			this.taskCount = Integer.parseInt(map.get("taskCount")+"");
			this.taskCloseCount = Integer.parseInt(map.get("taskCloseCount")+"");
			this.noStartCount = Integer.parseInt(map.get("noStartCount")+"");
			this.startingCount = Integer.parseInt(map.get("startingCount")+"");
			this.averageDeviate = Float.parseFloat(map.get("averageDeviate")+"");
		}
		return "success";
	}
	
	/**
	 * 根据巡检任务单Id获取巡检任务单详细信息（任务信息，巡检指标，问题点）
	 * 在待办巡检任务列表内点击连接跳转巡检任务单详细信息
	 * 跳转 loadRoutineInspectionTaskOrderInfo.jsp
	 * @return
	 */
	public String loadRoutineInspectionTaskInfoByToIdAction(){
		//this.TOID = "XJ-20130223-0098-0001";
		//根据巡检任务单Id获取巡检任务单详细信息（任务信息，巡检指标，问题点）
		this.taskInfoMap = this.routineInspectionTaskService.loadRoutineInspectionInfoByToIdService(this.TOID);
		return "success";
	}
	
	/**
	 * 跳转到查询巡检任务页面
	 * @return
	 */
	public String jumpSearchRoutineInspectionTaskAction(){
		return "success";
	}
	
	
	/**
	 * 根据条件查询巡检任务页面（分页）
	 */
	public String searchRoutineInspectionTaskToPageAction(){
		this.taskList = new ArrayList<Map<String,Object>>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		Map<String,String> intInParams = new HashMap<String, String>();
		if(this.orgId != null && !"".equals(this.orgId)){
			intInParams.put("orgId", this.orgId);
		}
		if(this.type != null && !"".equals(this.type)){
			intParams.put("type", this.type);
		}
		if(this.status != null && !"".equals(this.status)){
			intParams.put("status", this.status);
		}
		if(this.planName != null && !"".equals(this.planName)){
			strParams.put("planTitle", this.planName);
		}
		if(this.taskName != null && !"".equals(this.taskName)){
			strParams.put("toTitle", this.taskName);
		}
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		Map<String, Object> map = this.routineInspectionTaskService.searchRoutineInspectionByParamsService(this.currentPage, this.pageSize, strParams, intParams , intInParams);
		if(map!=null){
			int totalCount = Integer.parseInt(map.get("count")+"");
			PagingHelper ph = new PagingHelper();
			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
			if(pageMap!=null && pageMap.size()>0){
				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
			}
			Date date = new Date();
			long treeDate = 1000*60*60*24*3;
			List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("entityList");
			if(list!=null && list.size()>0){
				for (Map<String, Object> map2 : list) {
					String overFlag = "";
					Date taskPlanEndTime = TimeFormatHelper.setTimeFormat(map2.get("taskPlanEndTime"));
					if(taskPlanEndTime!=null){
						if(date.getTime()<taskPlanEndTime.getTime() && (date.getTime()+treeDate)>taskPlanEndTime.getTime()){
							overFlag = "fast";
						}else if(date.getTime()>taskPlanEndTime.getTime()){
							overFlag = "over";
						}
					}
					if(map2.get("taskPlanBeginTime")!=null){
						map2.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatByDay(map2.get("taskPlanBeginTime")));
					}else{
						map2.put("taskPlanBeginTime", "");
					}
					if(map2.get("taskPlanBeginTime")!=null){
						map2.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatByDay(map2.get("taskPlanEndTime")));
					}else{
						map2.put("taskPlanEndTime", "");
					}
					map2.put("overFlag", overFlag);
					this.taskList.add(map2);
				}
			}
		}
		return "success";
	}
	
	/**
	 * 巡检任务签到
	 */
	public void signInRoutineInspectionAction(){
		String json="";
		Gson gson=new Gson();
		boolean isSuccess = this.routineInspectionTaskService.txSignInRoutineInspectionService(this.TOID, null, null,null);
		if(!isSuccess){
			log.error("PC签到失败!");
		}
		json=gson.toJson(isSuccess);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC签到操作时返回页面信息出错");
		}
	}
	
	/**
	 * 签退
	 */
	public void signOutRoutineInspectionAction(){
		String json="";
		Gson gson=new Gson();
		boolean isSuccess = this.routineInspectionTaskService.signOutRoutineInspectionService(this.TOID);
		if(!isSuccess){
			log.error("PC签到失败!");
		}
		json=gson.toJson(isSuccess);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC签到操作时返回页面信息出错");
		}
	}
	
	/**
	 * 判断登陆人是否为维护人员
	 */
	public void judgeLoginPeopleIsMaintenanceWorkerAction(){
		String json="";
		Gson gson=new Gson();
		String userId = (String) SessionService.getInstance().getValueByKey("userId");
		boolean isSuccess = this.routineInspectionTaskService.judgeLoginPeopleIsMaintenanceWorkerService(userId,this.TOID);
		if(!isSuccess){
			log.info("登陆人："+userId+"不是维护人员");
		}
		json=gson.toJson(isSuccess);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做判断登陆人是否为维护人员操作时返回页面信息出错");
		}
	}
	
	
	
	
	//che.yd------- begin ------
	
	/**
	 * 跳转到“待巡检任务”页面
	 * 跳转 pendingRoutineInspectionTaskOrder.jsp
	 * @return
	 */
	public String jumpPendingRoutineInspectionTaskAction(){
		return "success";
	}
	
	
	

	/**
	 * 获取巡检待办任务页面（分页）
	 * 待巡检任务 展示 巡检待办任务列表
	 */
	public String getPendingRoutineInspectionTaskToPageAction(){
		this.taskList = new ArrayList<Map<String,Object>>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		Map<String,String> intInParams = new HashMap<String, String>();
		if(this.orgId != null && !"".equals(this.orgId)){
			intInParams.put("orgId", this.orgId);
		}
		if(this.type != null && !"".equals(this.type)){
			intParams.put("type", this.type);
		}
		if(this.status != null && !"".equals(this.status)){
			intParams.put("status", this.status);
		}
		if(this.planName != null && !"".equals(this.planName)){
			strParams.put("planTitle", this.planName);
		}
		if(this.taskName != null && !"".equals(this.taskName)){
			strParams.put("toTitle", this.taskName);
		}
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		
		
		//获取当前登录人
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		List<FlowTaskInfo> taskInfoList=new ArrayList<FlowTaskInfo>();
		String queryCondition="";
		
		//获取当前登录人所属的组织
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
		if(orgList!=null && !orgList.isEmpty()){
			for(SysOrg org:orgList){
				List<FlowTaskInfo> groupTaskList=null;
				try {
					groupTaskList=(List<FlowTaskInfo>)this.workFlowService.queryShareTasks(ProcessConstants.AcceptorType.ACCEPTOR_ORGROLE, String.valueOf(org.getOrgId()), ProcessConstants.TaskStatus.INIT);
				} catch (WFException e) {
					log.error("根据组织【"+org.getOrgId()+"】，获取对应的群组任务失败");
					e.printStackTrace();
				}
				if(groupTaskList!=null && !groupTaskList.isEmpty()){
					taskInfoList.addAll(groupTaskList);
				}
			}
		}
		
		//获取用户个人的任务
		List<FlowTaskInfo> personalTaskList=null;
		try {
			personalTaskList = (List<FlowTaskInfo>)this.workFlowService.queryOwnTasks(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE, userId, ProcessConstants.TaskStatus.HANDLING);
		} catch (WFException e) {
			log.error("获取用户【"+userId+"】，的个人任务失败");
			e.printStackTrace();
		}
		
		if(personalTaskList!=null && !personalTaskList.isEmpty()){
			taskInfoList.addAll(personalTaskList);
		}
		
		if(taskInfoList!=null && !taskInfoList.isEmpty()){
			
			Map<String,FlowTaskInfo> flowTaskInfoMap=new HashMap<String,FlowTaskInfo>();
			for(FlowTaskInfo tempFlowTaskInfo:taskInfoList){
				String flowInstanceId=tempFlowTaskInfo.getInstanceId();
				if(flowInstanceId!=null && !"".equals(flowInstanceId)){
					flowTaskInfoMap.put(flowInstanceId, tempFlowTaskInfo);
				}
			}
			
			String orgIds="";
			if(orgList!=null && !orgList.isEmpty()){
				for(SysOrg org:orgList){
					orgIds=orgIds+org.getOrgId()+",";
				}
				orgIds=orgIds.substring(0, orgIds.length()-1);
			}
			
			if(orgIds!=null && !orgIds.isEmpty()){
				queryCondition=queryCondition+" and \"orgId\" in ("+orgIds+")";
			}
			
			List<Map<String, Object>> returnList = null;
			
			Map<String, Object> returnMap = this.routineInspectionTaskService.searchPendingRoutineInspectionByParamsService(this.currentPage, this.pageSize, strParams, intParams , intInParams,queryCondition);
			if(returnMap!=null){
				returnList = (List<Map<String,Object>>)returnMap.get("entityList");
				int totalCount = Integer.parseInt(returnMap.get("count")+"");
				
//				int totalCount =0;
				if (returnList != null && !returnList.isEmpty()) {
					Date date = new Date();
					long treeDate = 1000*60*60*24*3;
//					List<Map<String,Object>> newMapList = new ArrayList<Map<String,Object>>();
					for(Map<String,Object> itemMap:returnList){
						String processInstanceId=itemMap.get("processInstId")==null?"":itemMap.get("processInstId").toString();
						if(flowTaskInfoMap.containsKey(processInstanceId)){
							String overFlag = "";
							Date taskPlanEndTime = TimeFormatHelper.setTimeFormat(itemMap.get("taskPlanEndTime"));
							if(taskPlanEndTime!=null){
								if(date.getTime()<taskPlanEndTime.getTime() && (date.getTime()+treeDate)>taskPlanEndTime.getTime()){
									overFlag = "fast";
								}else if(date.getTime()>taskPlanEndTime.getTime()){
									overFlag = "over";
								}
							}
							if(itemMap.get("taskPlanBeginTime")!=null){
								itemMap.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatByDay(itemMap.get("taskPlanBeginTime")));
							}else{
								itemMap.put("taskPlanBeginTime", "");
							}
							if(itemMap.get("taskPlanBeginTime")!=null){
								itemMap.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatByDay(itemMap.get("taskPlanEndTime")));
							}else{
								itemMap.put("taskPlanEndTime", "");
							}
							itemMap.put("overFlag", overFlag);
							this.taskList.add(itemMap);
							
//							totalCount++;
//							newMapList.add(itemMap);
						}
					}
					
					PagingHelper ph = new PagingHelper();
					Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
					if(pageMap!=null && pageMap.size()>0){
						this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
						this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
						this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
					}
				}
			}
		}
		
//		Map<String, Object> map = this.routineInspectionTaskService.searchPendingRoutineInspectionByParamsService(this.currentPage, this.pageSize, strParams, intParams , intInParams);
//		if(map!=null){
//			int totalCount = Integer.parseInt(map.get("count")+"");
//			PagingHelper ph = new PagingHelper();
//			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
//			if(pageMap!=null && pageMap.size()>0){
//				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
//				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
//				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
//			}
//			Date date = new Date();
//			long treeDate = 1000*60*60*24*3;
//			List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("entityList");
//			if(list!=null && list.size()>0){
//				for (Map<String, Object> map2 : list) {
//					String overFlag = "";
//					Date taskPlanEndTime = TimeFormatHelper.setTimeFormat(map2.get("taskPlanEndTime"));
//					if(taskPlanEndTime!=null){
//						if(date.getTime()<taskPlanEndTime.getTime() && (date.getTime()+treeDate)>taskPlanEndTime.getTime()){
//							overFlag = "fast";
//						}else if(date.getTime()>taskPlanEndTime.getTime()){
//							overFlag = "over";
//						}
//					}
//					if(map2.get("taskPlanBeginTime")!=null){
//						map2.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatByDay(map2.get("taskPlanBeginTime")));
//					}else{
//						map2.put("taskPlanBeginTime", "");
//					}
//					if(map2.get("taskPlanBeginTime")!=null){
//						map2.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatByDay(map2.get("taskPlanEndTime")));
//					}else{
//						map2.put("taskPlanEndTime", "");
//					}
//					map2.put("overFlag", overFlag);
//					this.taskList.add(map2);
//				}
//			}
//		}
		return "success";
	}
	
	
	//che.yd--------- end -----
	
	
	
	
	//get set ==================================================================
	
	public RoutineInspectionTaskService getRoutineInspectionTaskService() {
		return routineInspectionTaskService;
	}
	public void setRoutineInspectionTaskService(
			RoutineInspectionTaskService routineInspectionTaskService) {
		this.routineInspectionTaskService = routineInspectionTaskService;
	}
	public String getWOID() {
		return WOID;
	}
	public void setWOID(String woid) {
		WOID = woid;
	}

	public String getTOID() {
		return TOID;
	}

	public void setTOID(String toid) {
		TOID = toid;
	}

	public Map<String, String> getTaskInfoMap() {
		return taskInfoMap;
	}

	public void setTaskInfoMap(Map<String, String> taskInfoMap) {
		this.taskInfoMap = taskInfoMap;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Map<String, Object>> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Map<String, Object>> taskList) {
		this.taskList = taskList;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public int getTaskCloseCount() {
		return taskCloseCount;
	}

	public void setTaskCloseCount(int taskCloseCount) {
		this.taskCloseCount = taskCloseCount;
	}

	public int getNoStartCount() {
		return noStartCount;
	}

	public void setNoStartCount(int noStartCount) {
		this.noStartCount = noStartCount;
	}

	public int getStartingCount() {
		return startingCount;
	}

	public void setStartingCount(int startingCount) {
		this.startingCount = startingCount;
	}

	public Float getAverageDeviate() {
		return averageDeviate;
	}

	public void setAverageDeviate(Float averageDeviate) {
		this.averageDeviate = averageDeviate;
	}


	public ServiceBean getWorkFlowService() {
		return workFlowService;
	}

	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	
	
}
