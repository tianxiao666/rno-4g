package com.iscreate.op.service.routineinspection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.routineinspection.RoutineInspectionPlanDao;
import com.iscreate.op.dao.routineinspection.RoutineInspectionTaskDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.workmanage.CommonQueryService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workmanage.exception.WorkManageDefineException;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.LatLngHelper;
import com.iscreate.plat.tools.TimeFormatHelper;

public class RoutineInspectionTaskServiceImpl implements RoutineInspectionTaskService{

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskDao routineInspectionTaskDao;
	private RoutineInspectionPlanDao routineInspectionPlanDao;
	private WorkManageService workManageService;
	private CommonQueryService commonQueryService;
	private NetworkResourceInterfaceService networkResourceService;
	private String className = "com.iscreate.op.service.routineinspection.RoutineInspectionTaskServiceImpl";
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	private SysOrgUserService sysOrgUserService;
	
	
	
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	/**
	 * 根据任务单Id获取任务单信息
	 * @param toId
	 * @return
	 */
	public RoutineinspectionTaskorder getRoutineInspectionTaskByToIdService(String toId){
		log.info("进入getRoutineInspectionTaskByToIdService方法");
		log.info("参数toId="+toId);
		RoutineinspectionTaskorder routineInspectionTaskByToId = this.routineInspectionTaskDao.getRoutineInspectionTaskByToId(toId);
		log.info("执行getRoutineInspectionTaskByToIdService方法成功，实现了”根据任务单Id获取任务单信息“的功能");
		log.info("退出getRoutineInspectionTaskByToIdService方法,返回RoutineinspectionTaskorder");
		return routineInspectionTaskByToId;
	}
	
	/**
	 * 根据任务单Id获取任务单信息(终端)
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionTaskInfoByToIdForMobileService(String toId){
		log.info("进入getRoutineInspectionTaskInfoByToIdForMobileService方法");
		log.info("参数toId="+toId);
		Map<String,String> map = new HashMap<String, String>();
		map.put("taskTitle", "");
		map.put("planTitle", "");
		String woId = "";
		//根据任务单Id获取任务单信息
		RoutineinspectionTaskorder routineInspectionTaskByToIdService = this.getRoutineInspectionTaskByToIdService(toId);
		if(routineInspectionTaskByToIdService!=null){
			map.put("reId", routineInspectionTaskByToIdService.getResourceId());
			map.put("reType", routineInspectionTaskByToIdService.getResourceType());
			map.put("taskTitle", routineInspectionTaskByToIdService.getTaskTitle());
			map.put("reName", routineInspectionTaskByToIdService.getResourceName());
			woId = routineInspectionTaskByToIdService.getRoutineinspectionWoId();
		}
		if(woId!=null || !"".equals(woId)){
			//根据工单Id获取工单信息
			RoutineinspectionPlanworkorder planWorkOrder = this.routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			if(planWorkOrder!=null){
				map.put("planTitle", planWorkOrder.getPlanTitle());
			}
		}
		log.info("执行getRoutineInspectionTaskInfoByToIdForMobileService方法成功，实现了”根据任务单Id获取任务单信息(终端)“的功能");
		log.info("退出getRoutineInspectionTaskInfoByToIdForMobileService方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 保存任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean txSaveRoutineInspectionTaskOrderService(RoutineinspectionTaskorder routineinspectionTaskorder){
		boolean isSuccess = this.routineInspectionTaskDao.saveRoutineInspectionTaskOrder(routineinspectionTaskorder);
		if(!isSuccess){
			throw new UserDefinedException(this.className+"类里的txSaveRoutineInspectionTaskOrder方法保存任务单失败");
		}
		return isSuccess;
	}
	
	/**
	 * 修改任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean txUpdateRoutineInspectionTaskOrderService(RoutineinspectionTaskorder routineinspectionTaskorder){
		boolean isSuccess = this.routineInspectionTaskDao.updateRoutineInspectionTaskOrder(routineinspectionTaskorder);
		if(!isSuccess){
			throw new UserDefinedException(this.className+"类里的txUpdateRoutineInspectionTaskOrderService方法修改任务单失败");
		}
		return isSuccess;
	}
	
	/**
	 * 根据工单号获取任务单信息(分页)
	 * 根据巡检计划获取其下级巡检任务列表
	 * @param woId
	 * @return
	 */
	public Map<String,Object> getRoutineInspectionInfoByWoIdtoPageService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams){
		int taskCount = 0;
		int taskCloseCount = 0;
		int noStartCount = 0;
		int startingCount = 0;
		float averageDeviate = 0;
		String woId = intParams.get("woId");
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//根据工单号获取任务单号
		Map<String, Object> taskPage = this.searchRoutineInspectionByParamsService(currentPage, pageSize, strParams, intParams,null);
		if(taskPage!=null && taskPage.size()>0){
			List<Map<String,Object>> taskInfoList = (List<Map<String,Object>>)taskPage.get("entityList");
			if(taskInfoList!=null && taskInfoList.size()>0){
				for (Map<String,Object> taskInfo : taskInfoList) {
					Map<String,String> map = new HashMap<String, String>();
					String isDeviate = "false";
					String deviate = "";
					//终端偏离值计算
					if(taskInfo.get("deviate")!=null && !"".equals(taskInfo.get("deviate"))){
						deviate = taskInfo.get("deviate")+"";
						float subDeviate = Float.parseFloat(deviate);
						if(subDeviate>1000){
							isDeviate = "true";
						}
						//averageDeviate += subDeviate;
					}
					map.put("isDeviate", isDeviate);
					map.put("deviate", deviate);
					if(taskInfo.get("orgName")!=null){
						map.put("orgName", taskInfo.get("orgName")+"");
					}else{
						map.put("orgName", "");
					}
					
					map.put("toId", taskInfo.get("toId")+"");
					map.put("title", taskInfo.get("toTitle")+"");
					String taskPlanBeginTime = TimeFormatHelper.getTimeFormatByDay(taskInfo.get("taskPlanBeginTime"));
					if(taskPlanBeginTime == null){
						taskPlanBeginTime = "";
					}
					map.put("taskPlanBeginTime", taskPlanBeginTime);
					String taskPlanEndTime = TimeFormatHelper.getTimeFormatByDay(taskInfo.get("taskPlanEndTime"));
					if(taskPlanEndTime == null){
						taskPlanEndTime = "";
					}
					map.put("taskPlanEndTime", taskPlanEndTime);
					String signInTime = TimeFormatHelper.getTimeFormatBySecond(taskInfo.get("signInTime"));
					if(signInTime == null){
						signInTime = "";
					}
					map.put("signInTime", signInTime);
					
					Date signInTimeDate = TimeFormatHelper.setTimeFormat(taskInfo.get("signInTime"));
					Date signOutTimeDate = TimeFormatHelper.setTimeFormat(taskInfo.get("signOutTime"));
					Date taskPlanEndTimeDate = TimeFormatHelper.setTimeFormat(taskInfo.get("taskPlanEndTime"));
					String take = "";
					if(taskInfo.get("signOutTime") != null && taskInfo.get("signInTime") != null){
						long cha = signOutTimeDate.getTime() - signInTimeDate.getTime();		
						float result = (float)(cha * 1.0 / (1000 * 60 * 60));
						result = (float)(Math.round(result*100))/100;
						take = result+"";
					}
					map.put("take", take);    //巡检历时
					
					String isOver = "false";
					if(signOutTimeDate == null){
						long isOverTime = 0;
						if(taskPlanEndTimeDate!=null){
							isOverTime = new Date().getTime() - taskPlanEndTimeDate.getTime();
							if(isOverTime>0){
								isOver = "true";
							}
						}
					}else if(signOutTimeDate != null && taskPlanEndTimeDate!=null){
						long isOverTime = signOutTimeDate.getTime() - taskPlanEndTimeDate.getTime();
						if(isOverTime>0){
							isOver = "true";
						}
					}
					map.put("isOver", isOver);
					
					map.put("currentHandlerName", taskInfo.get("currentHandlerName")+"");
					map.put("statusName", taskInfo.get("statusName")+"");
					/*
					String status = taskInfo.get("status")+"";
					if("24".equals(status)){
						//已关闭
						taskCloseCount++;
					}else if("22".equals(status)){
						//待巡检
						noStartCount++;
					}else if("23".equals(status)){
						//巡检中
						startingCount++;
					}
					*/
					list.add(map);
				}
			}
			noStartCount = this.workManageService.getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(woId, WorkManageConstant.TASKORDER_WAIT4INSPECT);
			startingCount = this.workManageService.getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(woId, WorkManageConstant.TASKORDER_INSPECTING);
			taskCloseCount = this.workManageService.getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(woId, WorkManageConstant.TASKORDER_INSPECT_CLOSED);
			taskCount = noStartCount+startingCount+taskCount;
			averageDeviate = this.routineInspectionTaskDao.getTaskOrderAvgDeviateByWoId(woId);
			averageDeviate = (float)(Math.round(averageDeviate*10))/10;
			taskPage.put("averageDeviate", averageDeviate+"");
			taskPage.put("taskCount", taskCount);
			taskPage.put("taskCloseCount", taskCloseCount);
			taskPage.put("noStartCount", noStartCount);
			taskPage.put("startingCount", startingCount);
			taskPage.put("entityList", list);
		}
		return taskPage;
	}
	
	/**
	 * 根据任务单号获取任务单的详细信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> loadRoutineInspectionInfoByToIdService(String toId){
		String woId = "";
		Map<String,String> map = new HashMap<String, String>();
		//获取任务单的个性信息
		RoutineinspectionTaskorder task = this.routineInspectionTaskDao.getRoutineInspectionTaskByToId(toId);
		if(task != null){
			woId = task.getRoutineinspectionWoId();
			map.put("WOID", woId);
			map.put("TOID", task.getRoutineinspectionToId());
			map.put("taskTitle", task.getTaskTitle());
			map.put("orgName", task.getOrgName());
			map.put("reName", task.getResourceName());
			map.put("reId", task.getResourceId());
			map.put("reType", task.getResourceType());
			String taskPlanBeginTime = TimeFormatHelper.getTimeFormatByDay(task.getTaskPlanBeginTime());
			if(taskPlanBeginTime == null){
				taskPlanBeginTime = "";
			}
			map.put("taskPlanBeginTime", taskPlanBeginTime);
			String taskPlanEndTime = TimeFormatHelper.getTimeFormatByDay(task.getTaskPlanEndTime());
			if(taskPlanEndTime == null){
				taskPlanEndTime = "";
			}
			map.put("taskPlanEndTime", taskPlanEndTime);
			String signInTime = TimeFormatHelper.getTimeFormatBySecond(task.getSignInTime());
			if(signInTime == null){
				signInTime = "";
			}
			map.put("signInTime", signInTime);
			String signOutTime = TimeFormatHelper.getTimeFormatBySecond(task.getSignOutTime());
			if(signOutTime == null){
				signOutTime = "";
			}
			map.put("signOutTime", signOutTime);
		}
		
		//获取工单个性信息
		String type = "";
		String profession = "";
		String planTitle = "";
		RoutineinspectionPlanworkorder planworkorder = this.routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
		if(planworkorder!=null){
			planTitle = planworkorder.getPlanTitle();
			type = this.getRoutineInspectionType(planworkorder.getType());
			profession = this.getProfessionName(planworkorder.getRoutineInspectionProfession());
		}
		map.put("planTitle", planTitle);
		map.put("type", type);
		map.put("profession", profession);
		
		//获取任务单的通用信息
		String status = "";
		String statusName = "";
		Map<String, String> routineInsepctionTaskOrderForShow = this.workManageService.getRoutineInsepctionTaskOrderForShow(toId);
		if(routineInsepctionTaskOrderForShow != null && routineInsepctionTaskOrderForShow.size() > 0){
			status = routineInsepctionTaskOrderForShow.get("status")+"";
			statusName = routineInsepctionTaskOrderForShow.get("statusName");
		}
		map.put("status", status);
		map.put("statusName", statusName);
		return map;
	}
	
	/**
	 * 根据条件查询任务列表
	 * @param currentPage
	 * @param pageSize
	 * @param strParams
	 * @param intParams
	 * @return
	 */
	public Map<String,Object> searchRoutineInspectionByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,Map<String,String> intInParams){
		if(currentPage<=0){
			currentPage = 1;
		}
		String start = (currentPage-1)*pageSize+"";
		String sql = "";
		if(strParams!=null && strParams.size()>0){
			for (String key : strParams.keySet()) {
				sql += " and \""+key+"\" like '%" + strParams.get(key) + "%'";
			}
		}
		if(intParams!=null && intParams.size()>0){
			for (String key : intParams.keySet()) {
				if("woId".equals(key)){
					sql += " and \""+key+"\"='" + intParams.get(key) + "'";
				}else{
					sql += " and \""+key+"\"=" + intParams.get(key) + "";
				}
			}
		}
		if(intInParams!=null && intInParams.size()>0){
			for (String key : intInParams.keySet()) {
				if("orgId".equals(key)){
					String orgIds = "";
					long orgId = Long.parseLong(intInParams.get(key));
					//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(orgId);
					//yuan.yw
					List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(orgId);
					if(orgListDownwardByOrg!=null && orgListDownwardByOrg.size()>0){
						for (SysOrg po : orgListDownwardByOrg) {
							orgIds += po.getOrgId()+",";
						}
					}
					if(orgIds!=null && !"".equals(orgIds)){
						orgIds = orgIds.substring(0, orgIds.length()-1);
					}else{
						orgIds = intInParams.get(key);
					}
					sql += " and \""+key+"\" in (" + orgIds + ")";
				}
			}
		}
		sql += "order by \"toTitle\"";
		Map<String, Object> commonQueryService = this.commonQueryService.commonQueryService( start , pageSize+"" , null , null , "V_WM_INSP_TASKORDER" , null , sql);
		return commonQueryService;
	}
	
	/**
	 * 签到
	 * 要对巡检任务进行后续处理，必选先签到
	 * @param toId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean txSignInRoutineInspectionService(String toId,Double longitude,Double latitude,Float deviate){
		boolean result = false;
		RoutineinspectionTaskorder taskOrder = this.getRoutineInspectionTaskByToIdService(toId);
		if(taskOrder!=null){
			if(longitude!=null){
				taskOrder.setLongitude(longitude);
			}else{
				taskOrder.setLongitude(0.0);
			}
			if(latitude!=null){
				taskOrder.setLatitude(latitude);
			}else{
				taskOrder.setLatitude(0.0);
			}
			if(deviate!=null){
				taskOrder.setDeviate(deviate);
			}
			taskOrder.setSignInTime(new Date());
			boolean isSuccess = this.txUpdateRoutineInspectionTaskOrderService(taskOrder);
			if(!isSuccess){
				throw new UserDefinedException("签到时修改巡检个性表失败");
			}
			//获取任务
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = (String)session.getAttribute("userId");
			boolean taskTask = this.workManageService.takeTask(toId, userId);
			result = taskTask;
			if(!taskTask){
				throw new UserDefinedException(this.className+"类里的signInRoutineInspectionActionForMobile方法获取任务失败");
			}
		}
		return result;
	}
	
	/**
	 * 签退
	 * 签退表示巡检任务已结束
	 * @param toId
	 * @return
	 */
	public boolean signOutRoutineInspectionService(String toId){
		boolean isSuccess = true;
		RoutineinspectionTaskorder taskOrder = this.getRoutineInspectionTaskByToIdService(toId);
		String woId = "";
		if(taskOrder!=null){
			woId = taskOrder.getRoutineinspectionWoId();
			//taskOrder.setLongitude(Double.parseDouble(longitude));
			//taskOrder.setLatitude(Double.parseDouble(latitude));
			taskOrder.setSignOutTime(new Date());
			isSuccess = this.txUpdateRoutineInspectionTaskOrderService(taskOrder);
			if(!isSuccess){
				throw new UserDefinedException(this.className+"类里的signOutRoutineInspectionActionForMobile方法签退失败");
			}
			//结束巡检任务单
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = (String)session.getAttribute("userId");
			boolean isTaskOrder = true;
			try {
				isTaskOrder = this.workManageService.finishRoutineInspectionTaskOrder(toId, userId);
			} catch (WorkManageDefineException e) {
				log.debug("巡检签退时关闭任务单失败");
			}
			if(isTaskOrder){
				boolean isAllClose = true;
				int closeTask = this.workManageService.getCountOfRoutineInspectionCloseTaskOrderByWoId(woId);
				int allTask = this.workManageService.getCountOfRoutineInspectionTaskOrderByWoId(woId);
				//比较已关闭巡检任务数和总任务数，如果相等即是巡检计划下所有任务全部关闭
				if(closeTask != allTask){
					isAllClose = false;
				}
				if(isAllClose){
					//巡检计划下所有任务全部关闭，关闭巡检计划
					WorkmanageWorkorder workOrderByWoId = this.workManageService.getWorkOrderByWoId(woId);
					if(workOrderByWoId!=null){
						String currentHandler = workOrderByWoId.getCreator();
						//关闭巡检计划
						boolean finishRoutineInspectionPlanWorkOrder = true;
						try {
							finishRoutineInspectionPlanWorkOrder = this.workManageService.finishRoutineInspectionPlanWorkOrder(woId, currentHandler);
						} catch (WorkManageDefineException e) {
							log.debug("巡检签退时关闭工单时失败");
						}
						isSuccess = finishRoutineInspectionPlanWorkOrder;
						if(!isSuccess){
							throw new UserDefinedException(this.className+"类里的signOutRoutineInspectionActionForMobile方法结束巡检工单失败");
						}
					}
				}
			}
			if(!isTaskOrder){
				throw new UserDefinedException(this.className+"类里的signOutRoutineInspectionActionForMobile方法结束巡检任务单失败");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 获取当前位置是否已偏离
	 * 只有在终端上使用巡检模块会调用，判断手持终端所在位置与机房位置是否偏离
	 * @param toId
	 * @return
	 */
	public Map<String,String> getDeviateByToIdService(String toId,Double currentLng,Double currentLat){
		Map<String,String> distanceMap = new HashMap<String, String>();
		if(currentLng==null || currentLat==null){
			return distanceMap;
		}
		//根据任务单获取资源Id和资源类型
		RoutineinspectionTaskorder task = this.routineInspectionTaskDao.getRoutineInspectionTaskByToId(toId);
		if(task!=null){
			String reId = task.getResourceId();
			String reType = task.getResourceType();
			//获取机房的经纬度
			List<Map<String, String>> resourceService = this.networkResourceService.getResourceService(reId,reType,"Station","PARENT");
			if(resourceService != null && resourceService.size() > 0){
				Double reLongitude = null;
				Double reLatitude = null;
				Map<String,String> map = resourceService.get(0);
				String reLng = map.get("longitude");
				String reLat = map.get("latitude");
				if(reLng!=null && !"".equals(reLng)){
					reLongitude = Double.parseDouble(reLng);
					reLatitude = Double .parseDouble(reLat);
					double distance = LatLngHelper.Distance(currentLng, currentLat, reLongitude, reLatitude);
					float distance1 = (float)(Math.round(distance*100))/100;
					distanceMap.put("distance", distance1+"");
				}
			}
		}
		return distanceMap;
	}
	
	/**
	 * 判断登陆人是否有权限操作任务单时
	 * @return
	 */
	public boolean judgeLoginPeopleIsMaintenanceWorkerService(String userId,String toId){
		if(userId==null){
			log.debug("判断登陆人是否有权限操作任务单时，登陆人为"+userId);
			return false;
		}
		boolean result = false;
		RoutineinspectionTaskorder taskOrder = this.routineInspectionTaskDao.getRoutineInspectionTaskByToId(toId);
		if(taskOrder==null){
			log.debug("判断登陆人是否有权限操作任务单时，找不到任务单"+toId);
			return false;
		}
		long orgId = taskOrder.getOrgId();
//		Map<String,String> orgIdMap = new HashMap<String, String>();
//		orgIdMap.put("orgId", orgId+"");
		//ou.jh
		List<Long> orgIdsList = new ArrayList<Long>();
		orgIdsList.add(orgId);
		//获取多个组织的维护队队长
		List<Map<String,Object>> teamLeaderByOrgIds = this.sysOrgUserService.getTeamLeaderByOrgIds(orgIdsList);
		Map<String, String> teamLeaterMap = new HashMap<String, String>();
		if(teamLeaderByOrgIds!=null && teamLeaderByOrgIds.size()>0){
			for (Map<String,Object> oar : teamLeaderByOrgIds) {
				if(oar!=null){
					teamLeaterMap.put(oar.get("ORG_ID")+"", oar.get("ACCOUNT")+"");
				}
			}
		}
//		Map<String, String> teamLeaterMap = this.providerOrganizationService.getOrgAccountBeTeamLeaderByOrgIdsToMap(orgIdMap);
		if(teamLeaterMap.containsKey(orgId+"")){
			if(userId.equals(teamLeaterMap.get(orgId+""))){
				result = true;
			}
		}
//		List<Role> list = this.providerOrganizationService.getBizRoleToRoleByAccountService(userId);
//		if(list!=null && list.size()>0){
//			for (Role role : list) {
//				if(BizAuthorityConstant.ROLE_MAINTENANCEWORKER.equals(role.getCode())){
//					result = true;
//				}
//			}
//		}
		return result;
	}
	
	//内部方法===================================================================================
	/**
	 * 获取巡检类型的数据字典
	 * @param typeCode
	 * @return
	 */
	private String getRoutineInspectionType(Integer typeCode){
		String type = "";
		if(typeCode==null){
			return type;
		}
		if(typeCode==1){
			type = "基站巡检";
		}
		return type;
	}
	
	/**
	 * 获取巡检专业
	 * @param profession
	 * @return
	 */
	private String getProfessionName(String profession){
		String professionName = "";
		if(profession==null || "".equals(profession.trim())){
			log.info(this.className+"类里的getProfessionName方法传入的参数为:"+profession);
			return professionName;
		}else{
			String[] split = profession.split(",");
			for (String str : split) {
				if("ResGroup_4_Power_Flattening".equals(str)){
					//动力
					professionName +="动力 | ";
				}else if("ResGroup_4_Wireless_Flattening".equals(str)){
					//无线
					professionName +="无线 | ";
				}else if("FlatNavigation_4_Room_4_Transmission".equals(str)){
					//传输
					professionName +="传输 | ";
				}
			}
		}
		if(professionName!=null && !"".equals(professionName)){
			professionName = professionName.substring(0,professionName.length()-3);
		}
		return professionName;
	}
	
	
	
	
	/**
	 * @ahthor:che.yd
	 * 根据条件查询获取待办任务列表
	 * @param currentPage
	 * @param pageSize
	 * @param strParams
	 * @param intParams
	 * @return
	 */
	public Map<String,Object> searchPendingRoutineInspectionByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,Map<String,String> intInParams,String queryCondition){
		if(currentPage<=0){
			currentPage = 1;
		}
		String start = (currentPage-1)*pageSize+"";
		String sql = " and \"status\"<>24";
		if(strParams!=null && strParams.size()>0){
			for (String key : strParams.keySet()) {
				sql += " and \""+key+"\" like '%" + strParams.get(key) + "%'";
			}
		}
		if(intParams!=null && intParams.size()>0){
			for (String key : intParams.keySet()) {
				if("woId".equals(key)){
					sql += " and \""+key+"\"='" + intParams.get(key) + "'";
				}else{
					sql += " and \""+key+"\"=" + intParams.get(key) + "";
				}
			}
		}
		if(intInParams!=null && intInParams.size()>0){
			for (String key : intInParams.keySet()) {
				if("orgId".equals(key)){
					String orgIds = "";
					long orgId = Long.parseLong(intInParams.get(key));
					//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(orgId);
					//yuan.yw
					List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(orgId);
					
					if(orgListDownwardByOrg!=null && orgListDownwardByOrg.size()>0){
						for (SysOrg po : orgListDownwardByOrg) {
							orgIds += po.getOrgId()+",";
						}
					}
					if(orgIds!=null && !"".equals(orgIds)){
						orgIds = orgIds.substring(0, orgIds.length()-1);
					}else{
						orgIds = intInParams.get(key);
					}
					sql += " and \""+key+"\" in (" + orgIds + ")";
				}
			}
		}
		
		if(queryCondition!=null && !queryCondition.isEmpty()){
			sql+=" "+queryCondition;
		}
		Map<String, Object> commonQueryService = this.commonQueryService.commonQueryService( start , pageSize+"" , "\"assignTime\"" , "desc" , "V_WM_INSP_PENDINGTASKORDER" , null , sql);
		return commonQueryService;
	}
	
	
	
	
	//get set===================================================================================
	public RoutineInspectionTaskDao getRoutineInspectionTaskDao() {
		return routineInspectionTaskDao;
	}
	public void setRoutineInspectionTaskDao(
			RoutineInspectionTaskDao routineInspectionTaskDao) {
		this.routineInspectionTaskDao = routineInspectionTaskDao;
	}
	public WorkManageService getWorkManageService() {
		return workManageService;
	}
	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	public RoutineInspectionPlanDao getRoutineInspectionPlanDao() {
		return routineInspectionPlanDao;
	}

	public void setRoutineInspectionPlanDao(
			RoutineInspectionPlanDao routineInspectionPlanDao) {
		this.routineInspectionPlanDao = routineInspectionPlanDao;
	}

	public CommonQueryService getCommonQueryService() {
		return commonQueryService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	
	
}
