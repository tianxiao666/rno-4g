package com.iscreate.op.action.routineinspection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessConf;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.routineinspection.RoutineInspectionPlanService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageIdGenerator;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.paginghelper.PagingHelper;
import com.iscreate.sso.session.UserInfo;

public class RoutineInspectionPlanAction {

	
	private static final Log logger = LogFactory.getLog(RoutineInspectionPlanAction.class);
	
	private RoutineInspectionPlanService routineInspectionPlanService;
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	private WorkManageService workManageService;
	
	private RoutineinspectionPlanworkorder routineinspectionPlanworkorder = new RoutineinspectionPlanworkorder();
	
	private Long orgId;
	private String orgName;
	private String showPlanStartTime;
	private String showPlanEndTime;
	
	
	private List<Map> showRoomWithOrgList=new ArrayList<Map>();
//	private List<Map> showRoomWithoutOrgList=new ArrayList<Map>();
	
	private List<String> selectRoomWithOrgList;
	private List<String> selectRoomToPlanList;
	
	private Long selectRoomToPlanOrgId;
	private String selectRoomToPlanOrgName;
	private Date selectRoomToPlanBeginTime;
	private Date selectRoomToPlanEndTime;
	
	private String routineInspectionProfession;
	
	
	private String emailTitle;
	private String emailPlanContent;
	private Map<String,String> emailContentMap=new HashMap<String,String>();
	
	
	private String orgType;
	
	private Map<String,String> planWorkOrderInfoMap;
	private String WOID;
	private String type;
	private String status;
	private String planName;
	private int currentPage;
	private int totalPage;
	private int pageSize;
	private List<Map<String,String>> planWorkOrderList;
	
	private String searchOrgId;
	private String searchRoomName;
	private List<String> saveSelectRoomWithOrgList;
	
	private Long showPlanOrgId;
	
	
	private Long modifySelectRoomToPlanOrgId;
	private String modifySelectRoomToPlanOrgName;
	private Date modifySelectRoomToPlanBeginTime;
	private Date modifySelectRoomToPlanEndTime;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	


	/**
	 * 创建巡检计划工单action
	 * 此时创建巡检计划工单号，并未将计划实例保存到数据库
	 * 业务那边定是项目经理才能创建，目前用chen.sg的账号去创建
	 * 执行完跳转到 autoCreateRoutineInspectionTaskAction 带参数 type & orgid
	 */
	public String createRoutineInspectionPlanWorkOrderAction(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		//巡检计划信息
		if(routineinspectionPlanworkorder!=null){
//			System.out.println("专业前："+this.routineinspectionPlanworkorder.getRoutineInspectionProfession());
			String trimProfession=this.routineinspectionPlanworkorder.getRoutineInspectionProfession()==null?"":this.routineinspectionPlanworkorder.getRoutineInspectionProfession().replaceAll(" ", "");
			
			if(this.routineinspectionPlanworkorder!=null){
				this.routineinspectionPlanworkorder.setRoutineInspectionProfession(trimProfession);
			}
//			System.out.println("专业后："+this.routineinspectionPlanworkorder.getRoutineInspectionProfession());
			
			
			this.orgId=this.routineinspectionPlanworkorder==null?0:this.routineinspectionPlanworkorder.getOrgId();
		
			//获取巡检计划工单id
			//根据业务流程Code获取对应的业务流程配置对象
			WorkmanageBizprocessConf bizProcessConf=this.workManageService.getBizProcessConfByProcessCode(WorkManageConstant.PROCESS_ROUTINEINSPECTION_PLAN_CODE);
			WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
			//生成巡检计划工单号
			String routineinspectionWoId=bizIdGenerator.generateWOID(bizProcessConf.getBizFlag());
			routineinspectionPlanworkorder.setRoutineinspectionWoId(routineinspectionWoId);
			HttpSession session = request.getSession();
			
			//清除缓存的机房信息
			session.removeAttribute("cacheRoomTaskToPlanMap");
			session.setAttribute("routineinspectionPlanworkorder", routineinspectionPlanworkorder);
		}
		return "success";
		
	}
	
	
	/**
	 * 展示巡检计划工单信息action
	 * 制定巡检计划设置巡检任务时，“上一步”按钮调用，用于返回制定计划页面
	 * 跳转到 createRoutineInspectionPlanWorkOrder.jsp
	 * @return
	 */
	public String showRoutineInspectionPlanWorkOrderAction(){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		routineinspectionPlanworkorder=(RoutineinspectionPlanworkorder)session.getAttribute("routineinspectionPlanworkorder");
		
		if(routineinspectionPlanworkorder!=null){
			Map<String,String> orgMap = sysOrganizationService.getProviderOrgByOrgIdtoMapService(routineinspectionPlanworkorder.getOrgId());
			this.orgId=Long.parseLong(orgMap.get("id"));
			this.orgName=orgMap.get("name");
			this.showPlanStartTime=TimeFormatHelper.getTimeFormatByDay(routineinspectionPlanworkorder.getPlanStartTime());
			this.showPlanEndTime=TimeFormatHelper.getTimeFormatByDay(routineinspectionPlanworkorder.getPlanEndTime());
			
		}
		return "success";
	}
	
	
	/**
	 * 根据巡检计划，自动创建巡检任务
	 * 跳转到 createRoutineInspectionPlanWorkOrder2.jsp
	 * @return
	 */
	public String autoCreateRoutineInspectionTaskAction(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		//在session内获取巡检计划临时对象，该对象由 createRoutineInspectionPlanWorkOrderAction 传过来
		RoutineinspectionPlanworkorder tempPlanWorkOrder=(RoutineinspectionPlanworkorder)session.getAttribute("routineinspectionPlanworkorder");
		//缓存新增要执行任务的机房
		Map<String,Object> cacheRoomTaskToPlanMap=(Map<String,Object>)session.getAttribute("cacheRoomTaskToPlanMap");	
		//缓存修改的机房
		Map<String,Object> cacheModifyRoomMap=(Map<String,Object>)session.getAttribute("cacheModifyRoomMap");	
		//缓存所有机房对应的巡检任务
		Map<String,Object> cacheRoomToTask=(Map<String,Object>)session.getAttribute("cacheRoomToTask");	
		
		if(cacheRoomToTask==null || cacheRoomToTask.isEmpty()){
			cacheRoomToTask=new HashMap<String,Object>();
		}
		
		Map<String,Map> cacheToExecuteRoom=new HashMap<String,Map>();
		
		String routineinspectionWoId=tempPlanWorkOrder==null?"":tempPlanWorkOrder.getRoutineinspectionWoId();
		String planName=tempPlanWorkOrder==null?"":tempPlanWorkOrder.getPlanTitle();
		String beginTime=tempPlanWorkOrder==null?"":TimeFormatHelper.getTimeFormatByDay(tempPlanWorkOrder.getPlanStartTime());
		String endTime=tempPlanWorkOrder==null?"":TimeFormatHelper.getTimeFormatByDay(tempPlanWorkOrder.getPlanEndTime());
		String routineInspectionProfession=tempPlanWorkOrder==null?"":tempPlanWorkOrder.getRoutineInspectionProfession();
		long planOrgId=tempPlanWorkOrder==null?0:tempPlanWorkOrder.getOrgId();
		
		this.showPlanOrgId=planOrgId;
		this.showPlanStartTime=beginTime;
		this.showPlanEndTime=endTime;
	  
		//把已经添加到计划中的机房任务也添加到界面显示
		if(cacheRoomTaskToPlanMap!=null && !cacheRoomTaskToPlanMap.isEmpty()){
			for(Map.Entry<String, Object> entry:cacheRoomTaskToPlanMap.entrySet()){
				Map roomTaskMap=(Map)entry.getValue();
				String _orgId=roomTaskMap.get("orgId")==null?"":roomTaskMap.get("orgId").toString();
				String _orgName=roomTaskMap.get("orgName")==null?"":roomTaskMap.get("orgName").toString();
				String _roomName=roomTaskMap.get("resourceName")==null?"":roomTaskMap.get("resourceName").toString();
				String _roomType=roomTaskMap.get("_entityType")==null?"":roomTaskMap.get("_entityType").toString();
				String _roomId=roomTaskMap.get("resourceId")==null?"":roomTaskMap.get("resourceId").toString();
				
//				String taskTitle = _roomName+"基站巡检任务";
//				RoutineinspectionTaskorder routineinspectionTaskorder=new RoutineinspectionTaskorder();
//				routineinspectionTaskorder.setRoutineinspectionWoId(routineinspectionWoId);
//				routineinspectionTaskorder.setTaskTitle(taskTitle);
//				routineinspectionTaskorder.setTaskPlanBeginTime(tempPlanWorkOrder.getPlanStartTime());
//				routineinspectionTaskorder.setTaskPlanEndTime(tempPlanWorkOrder.getPlanEndTime());
//				routineinspectionTaskorder.setOrgId(Long.valueOf(_orgId));
//				routineinspectionTaskorder.setOrgName(_orgName);
//				routineinspectionTaskorder.setResourceName(_roomName);
//				routineinspectionTaskorder.setResourceType(_roomType);
//				routineinspectionTaskorder.setResourceId(_roomId);
//				routineinspectionTaskorder.setRoutineInspectionProfession(tempPlanWorkOrder.getRoutineInspectionProfession());
//				cacheRoomToTask.put(_roomId, routineinspectionTaskorder);
				
				showRoomWithOrgList.add(roomTaskMap);
				cacheToExecuteRoom.put(_roomId, roomTaskMap);
			}
		}
		
		Map<String,Map> showRoomWithOrgMap=new HashMap<String,Map>();
		if(showRoomWithOrgList!=null && !showRoomWithOrgList.isEmpty()){
			for(Map tempMap:showRoomWithOrgList){
				String roomId=tempMap.get("resourceId")==null?"0":tempMap.get("resourceId").toString();
				String roomName=tempMap.get("resourceName")==null?"":tempMap.get("resourceName").toString();
				showRoomWithOrgMap.put(roomId, tempMap);
			}
		}
		
		//把修改的机房也添加到界面显示
		if(cacheModifyRoomMap!=null && !cacheModifyRoomMap.isEmpty()){
			for(Map.Entry<String, Object> entry:cacheModifyRoomMap.entrySet()){
				String roomId=entry.getKey();
				Map map=(Map)entry.getValue();
				showRoomWithOrgList.add(map);
				showRoomWithOrgMap.put(roomId, map);
				cacheToExecuteRoom.put(roomId, map);
			}
		}
		
		//根据巡检计划所选择的组织，获取组织（递归包含其下级组织）所对应的机房
		List<Map<String,Object>> roomWithOrgList=null;	//具有组织的机房列表
		try {
			//根据组织Id获取巡检机房(挂到一个维护队)
			roomWithOrgList=this.networkResourceInterfaceService.getRoutineInspectionRoomByOrgIdToMaintenanceTeamService(planOrgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(roomWithOrgList!=null && !roomWithOrgList.isEmpty()){
			for(int i=0;i<roomWithOrgList.size();i++){
				Map tempMap=roomWithOrgList.get(i);
				
				String orgId=tempMap.get("orgId")==null?"0":tempMap.get("orgId").toString();
				String orgName=tempMap.get("orgName")==null?"":tempMap.get("orgName").toString();
				String roomName=tempMap.get("name")==null?"":tempMap.get("name").toString();
				String roomType=tempMap.get("_entityType")==null?"":tempMap.get("_entityType").toString();
				String roomId=tempMap.get("id")==null?"0":tempMap.get("id").toString();
				String address=tempMap.get("address")==null?"0":tempMap.get("address").toString();
				
				//用户展示
				Map taskMap=new HashMap();
				
//				WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
//				String toId=bizIdGenerator.generateTOID(routineinspectionWoId);
				//hardcode
//				String toId=routineinspectionWoId+"-"+(i+1);
//				taskMap.put("routineinspectionToId",toId);
				
				taskMap.put("routineinspectionPlanName", planName);
				taskMap.put("routineinspectionPlanStartTime", beginTime);
				taskMap.put("routineinspectionPlanEndTime", endTime);
				taskMap.put("orgId", orgId);
				taskMap.put("orgName", orgName);
				taskMap.put("resourceName",roomName);
				taskMap.put("resourceId", roomId);
				taskMap.put("routineInspectionProfession", routineInspectionProfession);
				taskMap.put("address", address);
				
				if(!showRoomWithOrgMap.containsKey(roomId)){
					showRoomWithOrgList.add(taskMap);
					String taskTitle = roomName+"基站巡检任务";
					RoutineinspectionTaskorder routineinspectionTaskorder=new RoutineinspectionTaskorder();
					routineinspectionTaskorder.setRoutineinspectionWoId(routineinspectionWoId);
					routineinspectionTaskorder.setTaskTitle(taskTitle);
					routineinspectionTaskorder.setTaskPlanBeginTime(tempPlanWorkOrder.getPlanStartTime());
					routineinspectionTaskorder.setTaskPlanEndTime(tempPlanWorkOrder.getPlanEndTime());
					routineinspectionTaskorder.setOrgId(Long.valueOf(orgId));
					routineinspectionTaskorder.setOrgName(orgName);
					routineinspectionTaskorder.setResourceName(roomName);
					routineinspectionTaskorder.setResourceType(roomType);
					routineinspectionTaskorder.setResourceId(roomId);
					routineinspectionTaskorder.setRoutineInspectionProfession(tempPlanWorkOrder.getRoutineInspectionProfession());
					
					cacheRoomToTask.put(roomId, routineinspectionTaskorder);
					cacheToExecuteRoom.put(roomId, taskMap);
				}
			}
		}
		
		//缓存自动创建的任务列表
		session.setAttribute("cacheRoomToTask", cacheRoomToTask);
		session.setAttribute("cacheToExecuteRoom", cacheToExecuteRoom);
		return "success";
	}
	
	
	/**
	 * ajax获取机房列表
	 * 在制定巡检计划设置巡检任务时，可以添加待巡检的基站（机房），点击“添加”按钮调用此方法，显示列表内没的机房供选择（不显示重复机房）
	 * createRoutineInspectionPlanWorkOrder2.jsp 页面调用
	 */
	public void getAllRoomForAjaxAction(){
		List<Map<String,String>> roomWithMoreOrgList=null;
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Gson gson=new Gson();
		String json="";
		
		//从session中获取巡检计划
		RoutineinspectionPlanworkorder tempPlanWorkOrder=(RoutineinspectionPlanworkorder)session.getAttribute("routineinspectionPlanworkorder");
		try {
			long planOrgId=tempPlanWorkOrder.getOrgId();
			roomWithMoreOrgList=this.networkResourceInterfaceService.getRoutineInspectionRoomByOrgIdService(planOrgId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据组织获取机房资源失败");
		}
		
		if(roomWithMoreOrgList!=null && !roomWithMoreOrgList.isEmpty()){
			//缓存所有没有组织或具有多个组织的机房列表
			Map<String,Object> cacheCanAddRoomList=new HashMap<String,Object>();
			for(int i=0;i<roomWithMoreOrgList.size();i++){
				Map tempMap=roomWithMoreOrgList.get(i);
				String roomName=tempMap.get("name")==null?"":tempMap.get("name").toString();
				String roomType=tempMap.get("_entityType")==null?"":tempMap.get("_entityType").toString();
				String roomId=tempMap.get("id")==null?"0":tempMap.get("id").toString();
				String address=tempMap.get("address")==null?"0":tempMap.get("address").toString();
				String orgId=tempMap.get("orgId")==null?"0":tempMap.get("orgId").toString();
				String orgName=tempMap.get("orgName")==null?"0":tempMap.get("orgName").toString();
				
				//用户展示
				Map roomMap=new HashMap();
				roomMap.put("resourceName", roomName);
				roomMap.put("_entityType", roomType);
				roomMap.put("resourceId", roomId);
				roomMap.put("address", address);
				roomMap.put("orgId", orgId);
				roomMap.put("orgName", orgName);
				cacheCanAddRoomList.put(roomId, roomMap);
			}
			json=gson.toJson(roomWithMoreOrgList);
			session.setAttribute("cacheCanAddRoomList", cacheCanAddRoomList);
		}else{
			logger.info("机房资源为空");
		}
		
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ajax搜索机房
	 * 在制定巡检计划设置巡检任务时，可以添加待巡检的基站（机房），添加框内的搜索功能
	 * createRoutineInspectionPlanWorkOrder2.jsp 页面调用
	 */
	public void searchRoomForAjaxAction(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		
		Gson gson=new Gson();
		String json="";
		
		List<Map> resultList=new ArrayList<Map>();
		Map<String,Object> session_cacheRoomMap=(Map<String,Object>)session.getAttribute("cacheCanAddRoomList");
		
		
		if(session_cacheRoomMap!=null && !session_cacheRoomMap.isEmpty()){
			
			if(searchRoomName==null || "".equals(searchRoomName)){
				for(Map.Entry<String, Object> entry:session_cacheRoomMap.entrySet()){
					Map roomMap=(Map)entry.getValue();
					resultList.add(roomMap);
				}
			}else{
				for(Map.Entry<String, Object> entry:session_cacheRoomMap.entrySet()){
					Map roomMap=(Map)entry.getValue();
					String roomName=roomMap.get("resourceName")==null?"":roomMap.get("resourceName").toString();
					if(roomName.contains(searchRoomName)){
						resultList.add(roomMap);
					}
				}
			}
			
//			if(this.searchOrgId!=null && !"".equals(this.searchOrgId)){
//				for(Map.Entry<String, Object> entry:session_cacheRoomMap.entrySet()){
//					Map roomMap=(Map)entry.getValue();
//					String orgId=roomMap.get("orgId")==null?"":roomMap.get("orgId").toString();
//					if(orgId.equals(this.searchOrgId)){
//						resultList.add(roomMap);
//					}
//				}
//			}
			
			
			json=gson.toJson(resultList);
		}
		
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 为巡检计划添加巡检任务
	 */
	public void submitSelectRoutineInspectionToPlanAction(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		Map<String,Object> cacheCanAddRoomList=(Map<String,Object>)session.getAttribute("cacheCanAddRoomList");
		Map<String,Object> cacheRoomToTask=(Map<String,Object>)session.getAttribute("cacheRoomToTask");
		Map<String,Object> cacheRoomTaskToPlanMap=new HashMap<String,Object>();
		
		RoutineinspectionPlanworkorder tempPlanWorkOrder=(RoutineinspectionPlanworkorder)session.getAttribute("routineinspectionPlanworkorder");
		
		boolean isSuccess=false;
		String result="";
		
		//把已经添加到计划的任务存放到session中
		if(this.selectRoomToPlanList!=null && !this.selectRoomToPlanList.isEmpty()){
			
			String routineinspectionWoId=tempPlanWorkOrder.getRoutineinspectionWoId();
			String planName=tempPlanWorkOrder.getPlanTitle();
			String routineInspectionProfession=tempPlanWorkOrder.getRoutineInspectionProfession();
			String taskTitle="";
			
			int index=0;
			for(String roomId:selectRoomToPlanList){
				if(cacheCanAddRoomList.containsKey(roomId)){
					Map roomMap=(Map)cacheCanAddRoomList.get(roomId);	//获取机房
					String roomName=roomMap.get("resourceName")==null?"":roomMap.get("resourceName").toString();
					String roomType=roomMap.get("_entityType")==null?"":roomMap.get("_entityType").toString();
					String address=roomMap.get("address")==null?"":roomMap.get("address").toString();
					
					roomMap.put("routineinspectionPlanName", planName);
					roomMap.put("routineinspectionPlanStartTime", TimeFormatHelper.getTimeFormatByDay(this.selectRoomToPlanBeginTime));
					roomMap.put("routineinspectionPlanEndTime", TimeFormatHelper.getTimeFormatByDay(this.selectRoomToPlanEndTime));
					roomMap.put("orgId", String.valueOf(this.selectRoomToPlanOrgId));
					roomMap.put("orgName", this.selectRoomToPlanOrgName);
					roomMap.put("_entityType",roomType);
					roomMap.put("resourceName",roomName);
					roomMap.put("resourceId", roomId);
					roomMap.put("routineInspectionProfession", routineInspectionProfession);
					roomMap.put("address", address);
					cacheRoomTaskToPlanMap.put(roomId, roomMap);
					
					taskTitle = roomName+"基站巡检任务";
					RoutineinspectionTaskorder routineinspectionTaskorder=new RoutineinspectionTaskorder();
					routineinspectionTaskorder.setRoutineinspectionWoId(routineinspectionWoId);
					routineinspectionTaskorder.setTaskTitle(taskTitle);
					routineinspectionTaskorder.setTaskPlanBeginTime(tempPlanWorkOrder.getPlanStartTime());
					routineinspectionTaskorder.setTaskPlanEndTime(tempPlanWorkOrder.getPlanEndTime());
					routineinspectionTaskorder.setOrgId(this.selectRoomToPlanOrgId);
					routineinspectionTaskorder.setOrgName(this.selectRoomToPlanOrgName);
					routineinspectionTaskorder.setResourceName(roomName);
					routineinspectionTaskorder.setResourceType(roomType);
					routineinspectionTaskorder.setResourceId(roomId);
					routineinspectionTaskorder.setRoutineInspectionProfession(tempPlanWorkOrder.getRoutineInspectionProfession());
					cacheRoomToTask.put(roomId, routineinspectionTaskorder);
					
					index++;
				}
			}
			//添加到session中
			session.setAttribute("cacheRoomTaskToPlanMap", cacheRoomTaskToPlanMap);
			isSuccess=true;
		}
		if(isSuccess){
			result="success";
		}else{
			result="error";
		}
		try {
			ServletActionContext.getResponse().getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("为巡检计划添加巡检任务时，服务器响应失败");
		}
	}
	
	
	/**
	 * ajax根据组织ID及组织类型向下获取组织树
	 */
	public void getProviderOrgTreeByOrgIdAndOrgTypeAction(){
		String json="";
		Gson gson=new Gson();
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgListDownwardByOrgTypeAndOrg(this.orgId,this.orgType);
		//yuan.yw
		List<Map<String,Object>> orgList = this.sysOrganizationService.getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(this.orgId,this.orgType,"child");
		List<Map> treeList=new ArrayList<Map>();
		if(orgList!=null && !orgList.isEmpty()){
			for(Map<String,Object> org:orgList){
				Map map=new HashMap();
				map.put("orgId", org.get("OrgId"));
				map.put("orgName", org.get("name"));
				treeList.add(map);
			}
			json=gson.toJson(treeList);
		}
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 根据组织id获取组织
	 */
	public void getProviderOrgByOrgIdInRoutineInspectionAction(){
		
		String json="";
		Gson gson=new Gson();
		if(this.orgId!=null){
			Map<String,String> orgMap = sysOrganizationService.getProviderOrgByOrgIdtoMapService(this.orgId);
			if(orgMap!=null){
				json=gson.toJson(orgMap);
			}
		}
		
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 修改添加到计划中要执行巡检任务的机房
	 */
	public void modifyRoutineInspectionTaskToPlanOfRoomAction(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		Map<String,Object> cacheRoomToTask=(Map<String,Object>)session.getAttribute("cacheRoomToTask");
		Map<String,Object> cacheToExecuteRoom=(Map<String,Object>)session.getAttribute("cacheToExecuteRoom");
		Map<String,Object> cacheModifyRoomMap=new HashMap<String,Object>();
		
		boolean isSuccess=false;
		String result="";
		if(this.selectRoomWithOrgList!=null && !this.selectRoomWithOrgList.isEmpty()){
			for(String roomId:selectRoomWithOrgList){
				if(cacheToExecuteRoom.containsKey(roomId)){
					Map roomMap=(Map)cacheToExecuteRoom.get(roomId);	//获取机房
					
					roomMap.put("routineinspectionPlanStartTime", TimeFormatHelper.getTimeFormatByDay(this.modifySelectRoomToPlanBeginTime));
					roomMap.put("routineinspectionPlanEndTime", TimeFormatHelper.getTimeFormatByDay(this.modifySelectRoomToPlanEndTime));
					roomMap.put("orgId", String.valueOf(this.modifySelectRoomToPlanOrgId));
					roomMap.put("orgName", this.modifySelectRoomToPlanOrgName);
					
					cacheModifyRoomMap.put(roomId, roomMap);
					
					RoutineinspectionTaskorder modifyTaskOrder=(RoutineinspectionTaskorder)cacheRoomToTask.get(roomId);
					modifyTaskOrder.setTaskPlanBeginTime(this.modifySelectRoomToPlanBeginTime);
					modifyTaskOrder.setTaskPlanEndTime(this.modifySelectRoomToPlanEndTime);
					modifyTaskOrder.setOrgId(this.modifySelectRoomToPlanOrgId);
					modifyTaskOrder.setOrgName(this.modifySelectRoomToPlanOrgName);
					
					cacheRoomToTask.put(roomId, modifyTaskOrder);
					cacheToExecuteRoom.put(roomId, roomMap);
				}
			}
			session.setAttribute("cacheModifyRoomMap", cacheModifyRoomMap);
			isSuccess=true;
		}
		if(isSuccess){
			result="success";
		}else{
			result="error";
		}
		try {
			ServletActionContext.getResponse().getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("修改添加到计划中要执行巡检任务的机房的时候，服务器响应失败");
		}
	}
	
	
	
	/**
	 * 提交巡检计划工单，并创建流程
	 * 真正保存巡检计划以及其巡检任务到数据库
	 * 跳转到 loadRoutineInspectionPlanWorkOrderInfoAction
	 * @return
	 */
	public String submitRoutineInspectionPlanWorkOrderAction(){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		
		String userId= (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		RoutineinspectionPlanworkorder tempPlanWorkOrder=(RoutineinspectionPlanworkorder)session.getAttribute("routineinspectionPlanworkorder");
		Map<String,Object> cacheTaskMap=(Map<String,Object>)session.getAttribute("cacheRoomToTask");
		List<RoutineinspectionTaskorder> routineinspectionTaskorderList=new ArrayList<RoutineinspectionTaskorder>();
		
		if(this.saveSelectRoomWithOrgList!=null && !this.saveSelectRoomWithOrgList.isEmpty()){
			for(String selectResourceId:saveSelectRoomWithOrgList){
				if(cacheTaskMap.containsKey(selectResourceId)){
					RoutineinspectionTaskorder selectTaskOrder=(RoutineinspectionTaskorder)cacheTaskMap.get(selectResourceId);
					routineinspectionTaskorderList.add(selectTaskOrder);
				}
			}
			
//			//提交计划，并自动创建任务
			boolean isSuccess=this.routineInspectionPlanService.txCreateRoutineInspectionPlanWorkOrder(userId, tempPlanWorkOrder, 
					routineinspectionTaskorderList);
			
			//hardcode
//			boolean isSuccess=true;
			
			//如果创建成功，派发邮件通知相关人员
			if(isSuccess){
//				this.emailTitle="【"+tempPlanWorkOrder.getPlanTitle()+"】通知";
//				this.emailPlanContent="【"+tempPlanWorkOrder.getPlanTitle()+"】于"+TimeFormatHelper.getTimeFormatByDay(tempPlanWorkOrder.getPlanStartTime())
//				+"开始，至"+TimeFormatHelper.getTimeFormatByDay(tempPlanWorkOrder.getPlanEndTime())+"结束";
//				
//				
//				//获取所有巡检任务内容，存放到Map里
//				Map<String,String> cacheEmailContent=this.sortOrgRoomInfo(routineinspectionTaskorderList);
//				
//				//获取巡检计划所选的巡检组织
//				long planOrgId=tempPlanWorkOrder.getOrgId();
//				
//				//递归获取其下级组织
//				List<ProviderOrganization> childrenList=this.providerOrganizationService.getOrgListDownwardByOrg(planOrgId);
//				if(childrenList!=null && !childrenList.isEmpty()){
//					//获取要执行巡检任务的组织的父组织
//					List<String> parentOrgIdList=new ArrayList<String>();
//					for(Map.Entry<String, String> entry:cacheEmailContent.entrySet()){
//						String orgId=entry.getKey();
//						List<ProviderOrganization> parentOrgList=this.providerOrganizationService.getUpProviderOrgByOrgIdService(Long.valueOf(orgId));
//						ProviderOrganization parentOrg=null;
//						if(parentOrgList!=null && !parentOrgList.isEmpty()){
//							parentOrg=parentOrgList.get(0);
//						}
//						
//						if(!parentOrgIdList.contains(String.valueOf(parentOrg.getId()))){
//							parentOrgIdList.add(String.valueOf(parentOrg.getId()));
//						}
//					}
//					
//					for(ProviderOrganization org:childrenList){	//遍历下级组织
//						//判断组织是否是在要执行巡检任务的组织范围内？如果不在，则对该组织发送全部的任务内容
//						if(cacheEmailContent.containsKey(String.valueOf(org.getId()))){
////							System.out.println("【组织："+org.getName()+"，id："+org.getId()+"】获取对应的任务");
//							String email_orgTaskContent=cacheEmailContent.get(String.valueOf(org.getId()));
//							String res=this.getOwnerTaskContent(org.getName(),this.emailPlanContent,email_orgTaskContent );
////							System.out.println(res);
//						}else{
//							List<ProviderOrganization> parentOrgList=this.providerOrganizationService.getUpProviderOrgByOrgIdService(Long.valueOf(org.getId()));
//							ProviderOrganization parentOrg=null;
//							if(parentOrgList!=null && !parentOrgList.isEmpty()){
//								parentOrg=parentOrgList.get(0);
//							}
//							if(!parentOrgIdList.contains(String.valueOf(parentOrg.getId()))){
////								System.out.println("【组织："+org.getName()+"，id："+org.getId()+"】获取所有的任务");
//								String res=this.getAllTaskContent(org.getName(),this.emailPlanContent, cacheEmailContent);
////								System.out.println(res);
//							}else{
////								System.out.println("【组织："+org.getName()+"，id："+org.getId()+"】不应获取任务");
//							}
//						}
////						System.out.println("---------------------------");
//					}
//				}
			}
		}
		this.WOID=tempPlanWorkOrder.getRoutineinspectionWoId();
		return "success";
	}
	
	
	
	/**
	 * 对维护队所负责的机房进行汇总
	 * @param routineinspectionTaskorderList
	 * @return
	 */
	private Map<String,String> sortOrgRoomInfo(List<RoutineinspectionTaskorder> routineinspectionTaskorderList){
		Map<String,String> cacheEmailContent=new HashMap<String,String>();
		if(routineinspectionTaskorderList!=null && !routineinspectionTaskorderList.isEmpty()){
			for(RoutineinspectionTaskorder taskOrder:routineinspectionTaskorderList){
				long longId=taskOrder.getOrgId();
				
				if(cacheEmailContent.containsKey(String.valueOf(longId))){
					String content=cacheEmailContent.get(String.valueOf(longId));
					content=content+"\r\n"+taskOrder.getResourceName()+"\t"+
					TimeFormatHelper.getTimeFormatByDay(taskOrder.getTaskPlanBeginTime())+"\t"+
					TimeFormatHelper.getTimeFormatByDay(taskOrder.getTaskPlanEndTime())+"\t";
					
					cacheEmailContent.put(String.valueOf(longId), content);
				}else{
					String content = taskOrder.getResourceName()+"\t"+
					TimeFormatHelper.getTimeFormatByDay(taskOrder.getTaskPlanBeginTime())+"\t"+
					TimeFormatHelper.getTimeFormatByDay(taskOrder.getTaskPlanEndTime())+"\t";
					cacheEmailContent.put(String.valueOf(longId), content);
				}
				
			}
		}
		
		return cacheEmailContent;
	}
	
	
	/**
	 * 根据组织id，为该组织下的所有用户发送邮件
	 * @param orgId
	 */
	private void SendMailByOrg(long orgId,String emailContent){
		
		
	}
	
	
	/**
	 * 获取对应的任务内容
	 * @param emailContentOfPlan
	 * @param emailContentOfTask
	 * @return
	 */
	private String getOwnerTaskContent(String currentOrgName,String emailPlanContent,String emailContentOfTask){
		return emailPlanContent+"\r\n\r\n"+currentOrgName+"\r\n"+emailContentOfTask;
	}
	
	
	/**
	 * 获取所有的任务内容
	 * @param emailContentOfPlan
	 * @param emailContentOfTask
	 * @return
	 */
	private String getAllTaskContent(String currentOrgName,String emailPlanContent,Map<String,String> cacheEmailContent){
		String emailTaskContent="";
		String orgName="";
		for(Map.Entry<String, String> entry:cacheEmailContent.entrySet()){
			String key=entry.getKey();
			String value=entry.getValue();
			if(key!=null){
				Map<String,String> orgMap = sysOrganizationService.getProviderOrgByOrgIdtoMapService(this.orgId);
				orgName = orgMap.get("name");
			}
			
			emailTaskContent=emailTaskContent+"\r\n"+currentOrgName+"-"+orgName+"\r\n"+value+"\r\n";
		}
		
		return emailPlanContent+emailTaskContent;
	}
	
	
	
	
	private boolean isOdd(int number){
		if(number%2==0){
			return false;
		}else{
			return true;
		}
	}
	
	
	//he.yy开始=========================================================
	
	/**
	 * 获取巡检计划管理里的巡检计划信息
	 * 跳转到 loadRoutineInspectionPlanWorkOrderInfo.jsp
	 */
	public String loadRoutineInspectionPlanWorkOrderInfoAction(){
		//this.WOID = "XJ-20130223-0098";    //HardCode测试
		this.planWorkOrderInfoMap = this.routineInspectionPlanService.getRoutineInspectionPlanWorkOrderInfoByWoIdService(this.WOID);
		return "success";
	}
	
	/**
	 * 跳转到“巡检计划管理”
	 * 跳转 searchRoutineInspectionPlanWorkOrder.jsp
	 * @return
	 */
	public String jumpSearchRoutineInspectionPlanWorkOrderAction(){
		return "success";
	}
	
	/**
	 * 按条件查询巡检计划(分页)
	 * @return
	 */
	public String searchRoutineInspectionPlanWorkOrderAction(){
		this.planWorkOrderList = new ArrayList<Map<String,String>>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		if(this.orgId != null){
			intParams.put("orgId", this.orgId+"");
		}
		if(this.type != null && !"".equals(this.type)){
			intParams.put("type", this.type);
		}
		if(this.status != null && !"".equals(this.status)){
			intParams.put("status", this.status);
		}
		if(this.planName != null && !"".equals(this.planName)){
			strParams.put("woTitle", this.planName);
		}
		
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		
		Map<String, Object> map = this.routineInspectionPlanService.searchRoutineInspectionPlanWorkOrderByParamsService(this.currentPage, this.pageSize, strParams, intParams);
		if(map!=null){
			int totalCount = Integer.parseInt(map.get("count")+"");
			PagingHelper ph = new PagingHelper();
			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
			if(pageMap!=null && pageMap.size()>0){
				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
			}
			this.planWorkOrderList = (List<Map<String,String>>)map.get("entityList");
		}
		return "success";
	}
	
	/**
	 * 根据巡检计划工单号关闭巡检计划工单信息
	 */
	public void closePlanWorkOrderAction(){
		String json="";
		Gson gson=new Gson();
		//关闭巡检计划
		boolean closePlanWorkOrderByWoIdService = this.routineInspectionPlanService.txClosePlanWorkOrderByWoIdService(this.WOID);
		json=gson.toJson(closePlanWorkOrderByWoIdService);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做关闭工单操作时返回页面信息出错");
		}
	}
	
	/**
	 * 根据巡检计划工单号删除巡检计划信息及巡检计划以下的巡检任务单信息
	 */
	public void deletePlanWorkOrderAction(){
		String json="";
		Gson gson=new Gson();
		boolean closePlanWorkOrderByWoIdService = this.routineInspectionPlanService.txDeletePlanWorkOrderByWoIdService(this.WOID);
		json=gson.toJson(closePlanWorkOrderByWoIdService);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做删除工单信息及工单以下的任务单的操作时返回页面信息出错");
		}
	}
	
	//he.yy结束=========================================================



	public RoutineInspectionPlanService getRoutineInspectionPlanService() {
		return routineInspectionPlanService;
	}
	
	public void setRoutineInspectionPlanService(
			RoutineInspectionPlanService routineInspectionPlanService) {
		this.routineInspectionPlanService = routineInspectionPlanService;
	}

	public RoutineinspectionPlanworkorder getRoutineinspectionPlanworkorder() {
		return routineinspectionPlanworkorder;
	}

	public void setRoutineinspectionPlanworkorder(
			RoutineinspectionPlanworkorder routineinspectionPlanworkorder) {
		this.routineinspectionPlanworkorder = routineinspectionPlanworkorder;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}




	public String getShowPlanStartTime() {
		return showPlanStartTime;
	}


	public void setShowPlanStartTime(String showPlanStartTime) {
		this.showPlanStartTime = showPlanStartTime;
	}


	public String getShowPlanEndTime() {
		return showPlanEndTime;
	}


	public void setShowPlanEndTime(String showPlanEndTime) {
		this.showPlanEndTime = showPlanEndTime;
	}


	public Long getOrgId() {
		return orgId;
	}


	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}


	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}


	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}


	public WorkManageService getWorkManageService() {
		return workManageService;
	}


	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}


	


	public List<Map> getShowRoomWithOrgList() {
		return showRoomWithOrgList;
	}


	public void setShowRoomWithOrgList(List<Map> showRoomWithOrgList) {
		this.showRoomWithOrgList = showRoomWithOrgList;
	}


	public List<String> getSelectRoomWithOrgList() {
		return selectRoomWithOrgList;
	}


	public void setSelectRoomWithOrgList(List<String> selectRoomWithOrgList) {
		this.selectRoomWithOrgList = selectRoomWithOrgList;
	}


	public List<String> getSelectRoomToPlanList() {
		return selectRoomToPlanList;
	}


	public void setSelectRoomToPlanList(List<String> selectRoomToPlanList) {
		this.selectRoomToPlanList = selectRoomToPlanList;
	}


	public Long getSelectRoomToPlanOrgId() {
		return selectRoomToPlanOrgId;
	}


	public void setSelectRoomToPlanOrgId(Long selectRoomToPlanOrgId) {
		this.selectRoomToPlanOrgId = selectRoomToPlanOrgId;
	}


	public String getSelectRoomToPlanOrgName() {
		return selectRoomToPlanOrgName;
	}


	public void setSelectRoomToPlanOrgName(String selectRoomToPlanOrgName) {
		this.selectRoomToPlanOrgName = selectRoomToPlanOrgName;
	}


	public Date getSelectRoomToPlanBeginTime() {
		return selectRoomToPlanBeginTime;
	}


	public void setSelectRoomToPlanBeginTime(Date selectRoomToPlanBeginTime) {
		this.selectRoomToPlanBeginTime = selectRoomToPlanBeginTime;
	}


	public Date getSelectRoomToPlanEndTime() {
		return selectRoomToPlanEndTime;
	}


	public void setSelectRoomToPlanEndTime(Date selectRoomToPlanEndTime) {
		this.selectRoomToPlanEndTime = selectRoomToPlanEndTime;
	}


	public String getOrgType() {
		return orgType;
	}


	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}


	public String getRoutineInspectionProfession() {
		return routineInspectionProfession;
	}


	public void setRoutineInspectionProfession(String routineInspectionProfession) {
		this.routineInspectionProfession = routineInspectionProfession;
	}


	public Map<String, String> getPlanWorkOrderInfoMap() {
		return planWorkOrderInfoMap;
	}


	public void setPlanWorkOrderInfoMap(Map<String, String> planWorkOrderInfoMap) {
		this.planWorkOrderInfoMap = planWorkOrderInfoMap;
	}


	public String getWOID() {
		return WOID;
	}


	public void setWOID(String woid) {
		WOID = woid;
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


	public List<Map<String, String>> getPlanWorkOrderList() {
		return planWorkOrderList;
	}


	public void setPlanWorkOrderList(List<Map<String, String>> planWorkOrderList) {
		this.planWorkOrderList = planWorkOrderList;
	}
	
	


	public String getSearchOrgId() {
		return searchOrgId;
	}


	public void setSearchOrgId(String searchOrgId) {
		this.searchOrgId = searchOrgId;
	}


	public String getSearchRoomName() {
		return searchRoomName;
	}


	public void setSearchRoomName(String searchRoomName) {
		this.searchRoomName = searchRoomName;
	}


	public List<String> getSaveSelectRoomWithOrgList() {
		return saveSelectRoomWithOrgList;
	}


	public void setSaveSelectRoomWithOrgList(List<String> saveSelectRoomWithOrgList) {
		this.saveSelectRoomWithOrgList = saveSelectRoomWithOrgList;
	}


	public Long getShowPlanOrgId() {
		return showPlanOrgId;
	}


	public void setShowPlanOrgId(Long showPlanOrgId) {
		this.showPlanOrgId = showPlanOrgId;
	}


	public Long getModifySelectRoomToPlanOrgId() {
		return modifySelectRoomToPlanOrgId;
	}


	public void setModifySelectRoomToPlanOrgId(Long modifySelectRoomToPlanOrgId) {
		this.modifySelectRoomToPlanOrgId = modifySelectRoomToPlanOrgId;
	}


	public String getModifySelectRoomToPlanOrgName() {
		return modifySelectRoomToPlanOrgName;
	}


	public void setModifySelectRoomToPlanOrgName(
			String modifySelectRoomToPlanOrgName) {
		this.modifySelectRoomToPlanOrgName = modifySelectRoomToPlanOrgName;
	}


	public Date getModifySelectRoomToPlanBeginTime() {
		return modifySelectRoomToPlanBeginTime;
	}


	public void setModifySelectRoomToPlanBeginTime(
			Date modifySelectRoomToPlanBeginTime) {
		this.modifySelectRoomToPlanBeginTime = modifySelectRoomToPlanBeginTime;
	}


	public Date getModifySelectRoomToPlanEndTime() {
		return modifySelectRoomToPlanEndTime;
	}


	public void setModifySelectRoomToPlanEndTime(Date modifySelectRoomToPlanEndTime) {
		this.modifySelectRoomToPlanEndTime = modifySelectRoomToPlanEndTime;
	}


	
	
}
