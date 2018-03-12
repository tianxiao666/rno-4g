package com.iscreate.op.service.routineinspection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.routineinspection.RoutineInspectionPlanDao;
import com.iscreate.op.dao.routineinspection.RoutineInspectionTaskDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.workmanage.CommonQueryService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workmanage.exception.WorkManageDefineException;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class RoutineInspectionPlanServiceImpl implements
		RoutineInspectionPlanService {

	
	private static final Log logger = LogFactory.getLog(RoutineInspectionPlanServiceImpl.class);
	
	private WorkManageService workManageService;
	private RoutineInspectionPlanDao routineInspectionPlanDao;
	private RoutineInspectionTaskDao routineInspectionTaskDao;
	private CommonQueryService commonQueryService;
	private String className = "com.iscreate.op.service.routineinspection.RoutineInspectionPlanServiceImpl";
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	 
	private SysOrganizationDao sysOrganizationDao;//组织dao du.hw
	
   
	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	//ou.jh
	private SysOrgUserService sysOrgUserService;
	
	
	
	
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	/**
	 * 创建巡检计划工单
	 * 创建计划并创建计划下的巡检任务
	 * @param userId 创建计划人
	 * @param routineinspectionPlanworkorder  巡检计划
	 * @param routineinspectionTaskorderList  巡检任务列表
	 * @return
	 */
	public boolean txCreateRoutineInspectionPlanWorkOrder(String userId,RoutineinspectionPlanworkorder routineinspectionPlanworkorder,
			List<RoutineinspectionTaskorder> routineinspectionTaskorderList) {
//		String woId=null;
		boolean isMethodInvokeSuccess=false;
		boolean woIsCreateSuccess=false;
		
		//保存巡检计划
		boolean isSaveSuccess=this.routineInspectionPlanDao.saveRoutineInspectionPlanWorkOrder(routineinspectionPlanworkorder);
		
		try {
			if(isSaveSuccess){
				WorkmanageWorkorder workmanageWorkorder=new WorkmanageWorkorder();
				workmanageWorkorder.setWoId(routineinspectionPlanworkorder.getRoutineinspectionWoId());
				workmanageWorkorder.setWoTitle(routineinspectionPlanworkorder.getPlanTitle());
				workmanageWorkorder.setWoType("巡检");
				workmanageWorkorder.setCreator(userId);
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//				Staff staff=this.providerOrganizationService.getStaffByAccount(userId);
				if(sysOrgUserByAccount!=null){
					workmanageWorkorder.setCreatorName(sysOrgUserByAccount.getName());
				}
				
				//创建巡检计划工单流程
				woIsCreateSuccess=this.workManageService.createRoutineInspectionPlanWorkOrder(WorkManageConstant.PROCESS_ROUTINEINSPECTION_PLAN_CODE, workmanageWorkorder);
			}
		} catch (WorkManageDefineException e) {
			e.printStackTrace();
			logger.error("【"+routineinspectionPlanworkorder.getRoutineinspectionWoId()+"】创建巡检计划工单流程失败，原因："+e.getMessage());
		}
		
		if(woIsCreateSuccess){	//巡检计划工单创建成功
			//System.out.println("woIsCreateSuccess=="+woIsCreateSuccess);
			if(routineinspectionTaskorderList!=null && !routineinspectionTaskorderList.isEmpty()){
				for(RoutineinspectionTaskorder temp:routineinspectionTaskorderList){
					
					//保存巡检任务
					boolean isSaveTaskSuccess=this.routineInspectionTaskDao.saveRoutineInspectionTaskOrder(temp);
					//创建巡检任务单流程
					if(isSaveTaskSuccess){
						WorkmanageTaskorder workmanageTaskorder=new WorkmanageTaskorder();
//						workmanageTaskorder.setToId(temp.getRoutineinspectionToId());
						workmanageTaskorder.setToTitle(temp.getTaskTitle());
						workmanageTaskorder.setToType("巡检");
						
						workmanageTaskorder.setRequireCompleteTime(temp.getTaskPlanEndTime());
						
						//设置任务派发人
						workmanageTaskorder.setAssigner(userId);
						
						long orgId=temp.getOrgId();
						String toId="";
						try {
							//创建巡检计划下的巡检任务单号
							toId=this.workManageService.createRoutineInspectionTaskOrder(WorkManageConstant.PROCESS_ROUTINEINSPECTION_TASK_CODE,
									routineinspectionPlanworkorder.getRoutineinspectionWoId(),routineinspectionPlanworkorder.getRoutineinspectionWoId(), 
									workmanageTaskorder, ProcessConstants.AcceptorType.ACCEPTOR_ORGROLE, String.valueOf(orgId));
						} catch (WorkManageDefineException e) {
							e.printStackTrace();
							logger.error("toId【"+temp.getRoutineinspectionToId()+"】创建巡检任务流程失败，原因："+e.getMessage());
						}
						
						temp.setRoutineinspectionToId(toId);
						//更新巡检任务
						this.routineInspectionTaskDao.updateRoutineInspectionTaskOrder(temp);
					}
					
				}
				isMethodInvokeSuccess=true;
			}
		}
		return isMethodInvokeSuccess;
	}


	/**
	 * 根据工单号获取工单详细信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionPlanWorkOrderInfoByWoIdService(String woId){
		Map<String,String> map = new HashMap<String, String>();
		//根据巡检计划工单号获取巡检计划工单个性信息
		RoutineinspectionPlanworkorder planworkorder = this.routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
		if(planworkorder!=null){
			//根据组织Id获取组织架构
			SysOrg po = sysOrganizationDao.getOrgByOrgId(planworkorder.getOrgId());
			String orgName = "";
			if(po!=null){
				orgName = po.getName();
			}else{
				logger.error(this.className+"类里的loadRoutineInspectionPlanWorkOrderInfoService方法获取组织架构为空，orgId="+planworkorder.getOrgId());
			}
			map.put("WOID", woId);
			map.put("planTitle", planworkorder.getPlanTitle());
			map.put("orgName", orgName);
			map.put("templateName", "2012年基站巡检模板");     //由于第一版的巡检开发里的巡检模板还不存在所以需要HardCode
			map.put("executeTime", TimeFormatHelper.getTimeFormatByDay(planworkorder.getPlanStartTime())+"至"+TimeFormatHelper.getTimeFormatByDay(planworkorder.getPlanEndTime()));       //执行时间
			map.put("vipCount", planworkorder.getVipBaseStationRoutineInspectionCount()+"");
			map.put("type", this.getRoutineInspectionType(planworkorder.getType()));
			map.put("profession", this.getProfessionName(planworkorder.getRoutineInspectionProfession()));
			map.put("remark", planworkorder.getRemark());
			map.put("activateTime", "");      //激活时间
			//根据工单号获取工单公共信息
			String creatorName = "";
			String createTime = "";
			String finalCompleteTime = "";
			WorkmanageWorkorder workOrderByWoId = this.workManageService.getWorkOrderByWoId(woId);
			if(workOrderByWoId!=null){
				creatorName = workOrderByWoId.getCreatorName();
				String cTime = TimeFormatHelper.getTimeFormatByDay(workOrderByWoId.getCreateTime());
				if(cTime != null){
					createTime = cTime;
				}
				String fTime = TimeFormatHelper.getTimeFormatByDay(workOrderByWoId.getFinalCompleteTime());
				if(fTime != null){
					finalCompleteTime = fTime;
				}
			}else{
				logger.error(this.className+"类里的loadRoutineInspectionPlanWorkOrderInfoService方法获获取工单公共信息为空，woId="+woId);
			}
			map.put("creatorName", creatorName);
			map.put("createTime", createTime);
			map.put("finalCompleteTime", finalCompleteTime);
			
			//根据工单获取工单信息
			String statusName = "";
			Map<String, String> routineInsepctionWorkOrderForShow = this.workManageService.getRoutineInsepctionWorkOrderForShow(woId);
			if(routineInsepctionWorkOrderForShow != null && routineInsepctionWorkOrderForShow.size() > 0){
				statusName = routineInsepctionWorkOrderForShow.get("statusName");
			}
			map.put("statusName", statusName);
			
			String complete = "";
//			int closeTask = 0;
			//已关闭巡检任务数
			int closeTask = this.workManageService.getCountOfRoutineInspectionCloseTaskOrderByWoId(woId);
			//全部任务数
			int allTask = this.workManageService.getCountOfRoutineInspectionTaskOrderByWoId(woId);
			//任务关闭率
			float a = (float)closeTask / (float)allTask*100;
			a = (float)(Math.round(a*100))/100;
			complete = a+"";
			map.put("complete", complete);
		}else{
			logger.error(this.className+"类里的loadRoutineInspectionPlanWorkOrderInfoService方法获取工单个性信息为空，woId="+woId);
		}
		return map;
	}

	/**
	 * 根据条件查询计划
	 * 在巡检计划查询，可根据条件集合进行筛选
	 * @param currentPage  当前页
	 * @param pageSize     每页显示数量 
	 * @param strParams    模糊查找条件集合
	 * @param intParams    精确查找条件集合
	 * @return
	 */
	public Map<String,Object> searchRoutineInspectionPlanWorkOrderByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams){
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
				sql += " and \""+key+"\"=" + intParams.get(key) + "";
			}
		}
		if(intParams==null || !intParams.containsKey("orgId")){
			//查找以当前组织为跟节点的组织树
			String orgIds = "";
			String userId = (String) SessionService.getInstance().getValueByKey("userId");
			if(userId!=null && !"".equals(userId)){
				//List<ProviderOrganization> topLevelOrgByAccount = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
				List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

				if(topLevelOrgByAccount!=null && topLevelOrgByAccount.size()>0){
					//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(topLevelOrgByAccount.get(0).getId());
					//yuan.yw
					List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(topLevelOrgByAccount.get(0).getOrgId());
					if(orgListDownwardByOrg!=null && orgListDownwardByOrg.size()>0){
						for (SysOrg po : orgListDownwardByOrg) {
							orgIds += po.getOrgId()+",";
						}
					}
				}
			}
			if(orgIds!=null && !"".equals(orgIds)){
				orgIds = orgIds.substring(0,orgIds.length()-1);
			}
			sql += " and \"orgId\" in (" + orgIds + ")";
		}
		List<Map<String,Object>> planWorkOrderList = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = this.commonQueryService.commonQueryService( start , pageSize+"" , "\"createTime\"" , "desc" , "V_WM_INSP_WORKORDER" , null , sql);
		if(map!=null){
			Date currentDate = new Date();
			currentDate = TimeFormatHelper.setTimeFormatByDay(currentDate);
			List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("entityList");
			if(list!=null && list.size()>0){
				//Map<String, ProviderOrganization> allProviderToMap = this.providerOrganizationService.getAllProviderToMap();
				//yuan.yw
				Map<String, SysOrg> allProviderToMap = this.sysOrganizationService.getAllOrgToMap();
				//循环获取工单号集合
				List<String> woIds = new ArrayList<String>();
				for (Map<String, Object> map2 : list) {
					if(map2.get("woId")!=null){
						woIds.add(map2.get("woId").toString());
					}
				}
				//获取所有工单对应的已关闭任务
				List<Map> closeTaskList = workManageService.getCountOfRoutineInspectionCloseTaskOrderByWoIdList(woIds);
				if(closeTaskList==null||closeTaskList.isEmpty()){
					logger.error("获取工单对应的已关闭任务为空集");
				}
				//获取所有工单对应的所有任务
				List<Map> allTaskList = workManageService.getCountOfRoutineInspectionTaskOrderByWoIdList(woIds);
				if(allTaskList==null||allTaskList.isEmpty()){
					logger.error("获取工单对应的全部任务为空集");
				}
				
				for (Map<String, Object> map2 : list) {
					Date planStartTime = TimeFormatHelper.setTimeFormat(map2.get("planStartTime"));
					Date planEndTime = TimeFormatHelper.setTimeFormat(map2.get("planEndTime"));
					//获取组织架构
					long subOrgId = Long.valueOf(map2.get("orgId")+"");
					String orgName = "";
					if(allProviderToMap!=null && allProviderToMap.size()>0){
						SysOrg po = allProviderToMap.get(subOrgId+"");
						if(po!=null){
							orgName = po.getName();
						}
					}
					map2.put("orgName", orgName);
					if(map2.get("planStartTime")!=null){
						map2.put("planStartTime", TimeFormatHelper.getTimeFormatByDay(map2.get("planStartTime")));
					}else{
						map2.put("planStartTime", "");
					}
					if(map2.get("planEndTime")!=null){
						map2.put("planEndTime", TimeFormatHelper.getTimeFormatByDay(map2.get("planEndTime")));
					}else{
						map2.put("planEndTime", "");
					}
					
					float designSchedule = 0;   //计划进度
					float realitySchedule = 0;    //实际进度
					if(planStartTime!=null && planEndTime!=null){
						//计划进度 计算逻辑
						float subDesign = ((float)(currentDate.getTime()-planStartTime.getTime()))/((float)(planEndTime.getTime()-planStartTime.getTime()));
						designSchedule = subDesign*100;
						designSchedule = (float)(Math.round(designSchedule*100))/100;
						if(designSchedule<0){
							designSchedule = 0;
						}else if(designSchedule>100){
							designSchedule = 100;
						}
						//实际进度 计算逻辑
						String woId = map2.get("woId")+"";
						//已关闭任务数
						int closeTask = 0 ;
						if(closeTaskList!=null&&!closeTaskList.isEmpty()){
							for(Map ct : closeTaskList){
								if(woId.equals(ct.get("woId"))){
									closeTask = Integer.parseInt(ct.get("count").toString());
								}
							}
						}
						//总任务数
						int allTask = 0 ;
						if(allTaskList!=null&&!allTaskList.isEmpty()){
							for(Map at : allTaskList){
								if(woId.equals(at.get("woId"))){
									allTask = Integer.parseInt(at.get("count").toString());
								}
							}
						}
						//System.out.println(taskList.size() +" === "+ closeTask);
						realitySchedule = (float)closeTask / (float)allTask*100;
						realitySchedule = (float)(Math.round(realitySchedule*100))/100;
					}
					//超时状态
					String overFlag = "";
					if(realitySchedule < designSchedule && realitySchedule>(designSchedule-5)){
						overFlag = "fast";
					}else if(realitySchedule > designSchedule){
						overFlag = "success";
					}else if(realitySchedule < designSchedule){
						overFlag = "over";
					}
					map2.put("overFlag", overFlag);
					map2.put("designSchedule", designSchedule+"");
					map2.put("realitySchedule", realitySchedule+"");
					map2.put("type", this.getRoutineInspectionType(Integer.parseInt(map2.get("type")+"")));
					planWorkOrderList.add(map2);
				}
			}
			map.put("entityList", planWorkOrderList);
		}
		return map;
	}
	
	/**
	 * 根据巡检计划工单号关闭巡检计划
	 * 在巡检计划管理页面，可对巡检计划进行关闭或删除操作
	 * @param woId
	 * @return
	 */
	public boolean txClosePlanWorkOrderByWoIdService(String woId){
		boolean isSuccess = false;
		WorkmanageWorkorder workOrderByWoId = this.workManageService.getWorkOrderByWoId(woId);
		if(workOrderByWoId!=null){
			String planCreator = workOrderByWoId.getCreator();
			try {
				//首先结束巡检计划下的巡检任务单
				List<Map> taskList = this.workManageService. getRoutineInsepctionTaskOrderListByWoId(woId);
				if(taskList!=null && taskList.size()>0){
					Map<String,String> orgIdMap = new HashMap<String, String>();
//					for (Map map : taskList){
//						String orgId = map.get("orgId")+"";
//						orgIdMap.put(orgId, orgId);
//					}
					//ou.jh
					List<Long> orgIdsList = new ArrayList<Long>();
					for (Map map : taskList){
						String orgId = map.get("orgId")+"";
						if(orgId != null && !orgId.equals("")){
							long parseLong = Long.parseLong(orgId);
							orgIdsList.add(parseLong);
						}else{
							//orgId为空
							continue;
						}
					}
					//获取多个组织的维护队队长
					List<Map<String,Object>> teamLeaderByOrgIds = this.sysOrgUserService.getTeamLeaderByOrgIds(orgIdsList);
					Map<String, String> orgAccountMap = new HashMap<String, String>();
					if(teamLeaderByOrgIds!=null && teamLeaderByOrgIds.size()>0){
						for (Map<String,Object> oar : teamLeaderByOrgIds) {
							if(oar!=null){
								orgAccountMap.put(oar.get("ORG_ID")+"", oar.get("ACCOUNT")+"");
							}
						}
					}
//					Map<String, String> orgAccountMap = this.providerOrganizationService.getOrgAccountBeTeamLeaderByOrgIdsToMap(orgIdMap);
					for (Map map : taskList) {
						String toId = map.get("toId")+"";
						//判断任务单是否是群组任务
						//true:群组 false:个人
						boolean judgeTaskIsGroup = this.workManageService.judgeTaskIsGroup(toId);	
						//如果是群组任务，则以维护队队长作任务处理人
						if(judgeTaskIsGroup){
							long orgId = Long.parseLong(map.get("orgId")+"");
							String currentHandler = "";
							if(orgAccountMap!=null && orgAccountMap.containsKey(orgId+"")){
								currentHandler = orgAccountMap.get(orgId+"");
							}
							//先签到巡检任务
							boolean taskTask = this.workManageService.takeTask(toId, currentHandler);
							if(!taskTask){
								throw new UserDefinedException("签到时失败,参数：任务单号："+toId+", 操作账号："+currentHandler);
							}
							//再关闭巡检任务
							boolean finishRoutineInspectionTaskOrder = this.workManageService.finishRoutineInspectionTaskOrder(toId, currentHandler);
							if(!finishRoutineInspectionTaskOrder){
								throw new UserDefinedException("关闭任务单时失败,参数：任务单号："+toId+", 操作账号："+currentHandler);
							}
						}else{
							//个人则以个人本身做任务责任人
							WorkmanageTaskorder taskOrderByToId = this.workManageService.getTaskOrderByToId(toId);
							if(taskOrderByToId!=null){
								String currentHandler = taskOrderByToId.getCurrentHandler();
								//判断巡检任务是否已结束
								boolean isEnd=this.workManageService.judgeTaskOrderIsEnd(toId);
								if(!isEnd){
									//若未结束就将任务关闭单
									boolean finishRoutineInspectionTaskOrder = this.workManageService.finishRoutineInspectionTaskOrder(toId, currentHandler);
									if(!finishRoutineInspectionTaskOrder){
										throw new UserDefinedException("关闭任务单时失败,参数：任务单号："+toId+", 操作账号："+currentHandler);
									}
								}
							}
						}
					}
				}
				//关闭巡检计划
				boolean finishRoutineInspectionPlanWorkOrder = this.workManageService.finishRoutineInspectionPlanWorkOrder(woId, planCreator);
				isSuccess = finishRoutineInspectionPlanWorkOrder;
			} catch (Exception e) {
				isSuccess = false;
				throw new UserDefinedException("关闭工单时失败,参数：工单号："+woId+", 操作账号："+planCreator);
			}
		}
		return isSuccess;
	}
	
	/**
	 * 根据巡检计划工单号删除巡检计划
	 * 在巡检计划管理页面，可对巡检计划进行关闭或删除操作
	 * @param woId
	 * @return
	 */
	public boolean txDeletePlanWorkOrderByWoIdService(String woId){
		boolean isSuccess = false;
		WorkmanageWorkorder workOrderByWoId = this.workManageService.getWorkOrderByWoId(woId);
		if(workOrderByWoId!=null){
			String creator = workOrderByWoId.getCreator();
			try {
				//结束任务单
				List<Map> taskList = this.workManageService. getRoutineInsepctionTaskOrderListByWoId(woId);
				if(taskList!=null && taskList.size()>0){
					for (Map map : taskList) {
						String assigner = "";
						String toId = map.get("toId")+"";
						WorkmanageTaskorder taskOrderByToId = this.workManageService.getTaskOrderByToId(toId);
						if(taskOrderByToId!=null){
							assigner = taskOrderByToId.getAssigner();
						}
						
						//判断巡检任务是否已结束
						boolean isEnd=this.workManageService.judgeTaskOrderIsEnd(toId);
						if(!isEnd){
							//未结束则关闭任务单
							boolean finishRoutineInspectionTaskOrder = this.workManageService.cancelRoutineInspectionTaskOrder(toId, assigner);
							if(!finishRoutineInspectionTaskOrder){
								throw new UserDefinedException("关闭任务单时失败,参数：任务单号："+toId+", 操作账号："+assigner);
							}
						}
					}
				}
				//结束巡检计划
				boolean finishRoutineInspectionPlanWorkOrder = this.workManageService.cancelRoutineInspectionWorkOrder(woId, creator);
				isSuccess = finishRoutineInspectionPlanWorkOrder;
			} catch (Exception e) {
				isSuccess = false;
				throw new UserDefinedException("关闭工单时失败,参数：工单号："+woId+", 操作账号："+creator);
			}
		}
		return isSuccess;
	}
	
	//内部方法================================================================================
	
	/**
	 * 获取巡检类型的数据字典
	 * @param typeCode
	 * @return
	 */
	private String getRoutineInspectionType(Integer typeCode){
		String type = "";
		if(typeCode==null){
			logger.info(this.className+"类里的getRoutineInspectionType方法传入的参数为:"+typeCode);
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
			logger.info(this.className+"类里的getProfessionName方法传入的参数为:"+profession);
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




	public RoutineInspectionTaskDao getRoutineInspectionTaskDao() {
		return routineInspectionTaskDao;
	}




	public void setRoutineInspectionTaskDao(
			RoutineInspectionTaskDao routineInspectionTaskDao) {
		this.routineInspectionTaskDao = routineInspectionTaskDao;
	}


	

	public CommonQueryService getCommonQueryService() {
		return commonQueryService;
	}


	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	
	
	
}
