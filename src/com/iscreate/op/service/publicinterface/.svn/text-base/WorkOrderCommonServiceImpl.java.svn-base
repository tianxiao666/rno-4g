package com.iscreate.op.service.publicinterface;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.iscreate.op.dao.urgentrepair.UrgentRepairWorkOrderDao;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevice2gworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevicetdworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelation;
import com.iscreate.op.service.urgentrepair.UrgentRepairCustomerWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelationService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class WorkOrderCommonServiceImpl implements WorkOrderCommonService {
	private UrgentRepairWorkOrderDao urgentRepairWorkOrderDao;
	private UrgentRepairCustomerWorkOrderService  urgentRepairCustomerWorkOrderService;
	private WorkManageService workManageService;
	private WorkorderinterfaceWangyouWorkorderRelationService workorderinterfaceWangyouWorkorderRelationService;
	public String getAllUrgentRepairWorkOrderInfoService() {
		
		return "success";
	}
	
	/**
	 * 获取工单信息
	 * 抢修的工单页面或任务单页面里的工单信息tab
	 * */
	public Map getUrgentRepairWorkOrderService(String woId) {
		Map urgentrepairWorkorderMap = new HashMap();
		Map workOrderMap  = workManageService.getWorkOrderForShow(woId);
		List<UrgentrepairWorkorder>  urgentrepairWorkorders = urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId(woId);
		if(urgentrepairWorkorders!=null&&workOrderMap!=null&&!urgentrepairWorkorders.isEmpty()){
			UrgentrepairWorkorder urgentrepairWorkorder = urgentrepairWorkorders.get(0);
			
			//****** 创建工单信息 *********************
			String woTitle = "";
			if(workOrderMap.get("woTitle")!=null){
				woTitle = workOrderMap.get("woTitle")+"";
			}
			urgentrepairWorkorderMap.put("woTitle", woTitle);
			
			String acceptProfessional = "";
			if(urgentrepairWorkorder.getAcceptProfessional()!=null){
				acceptProfessional = urgentrepairWorkorder.getAcceptProfessional()+"";
			}
			urgentrepairWorkorderMap.put("acceptProfessional",acceptProfessional);
			
			String baseStationLevel = "";
			if(urgentrepairWorkorder.getBaseStationLevel()!=null){
				baseStationLevel = urgentrepairWorkorder.getBaseStationLevel()+"";
			}
			urgentrepairWorkorderMap.put("baseStationLevel", baseStationLevel);
			
			String stationOfArea = "";
			if( urgentrepairWorkorder.getFaultArea()!=null){
				stationOfArea =  urgentrepairWorkorder.getFaultArea()+"";
			}
			urgentrepairWorkorderMap.put("stationOfArea", stationOfArea);
			
			String stationName = "";
			if( urgentrepairWorkorder.getStationName()!=null){
				stationName =  urgentrepairWorkorder.getStationName()+"";
			}
			urgentrepairWorkorderMap.put("stationName", stationName);
			//故障发生时间
			urgentrepairWorkorderMap.put("faultOccuredTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getFaultOccuredTime()));
			//故障处理时限
			urgentrepairWorkorderMap.put("latestAllowedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getLatestAllowedTime()));
			
			String faultDescription = "";
			if( urgentrepairWorkorder.getFaultDescription()!=null){
				faultDescription =  urgentrepairWorkorder.getFaultDescription()+"";
			}
			urgentrepairWorkorderMap.put("faultDescription",faultDescription);
			
			String faultType = "";
			if(  urgentrepairWorkorder.getFaultType()!=null){
				faultType =  urgentrepairWorkorder.getFaultType()+"";
			}
			urgentrepairWorkorderMap.put("faultType", faultType);
			
			String faultLevel = "";
			if(urgentrepairWorkorder.getFaultLevel()!=null){
				faultLevel = urgentrepairWorkorder.getFaultLevel()+"";
			}
			urgentrepairWorkorderMap.put("faultLevel",faultLevel);
			
			String netElementName = "";
			if( urgentrepairWorkorder.getNetElementName()!=null){
				netElementName = urgentrepairWorkorder.getNetElementName()+"";
			}
			urgentrepairWorkorderMap.put("netElementName",netElementName);
			
			String faultStationName = "";
			if( urgentrepairWorkorder.getFaultStationName()!=null){
				faultStationName = urgentrepairWorkorder.getFaultStationName()+"";
			}
			urgentrepairWorkorderMap.put("faultStationName",faultStationName);
			
			String faultStationAddress = "";
			if(urgentrepairWorkorder.getFaultStationAddress()!=null){
				faultStationAddress = urgentrepairWorkorder.getFaultStationAddress()+"";
			}
			urgentrepairWorkorderMap.put("faultStationAddress", urgentrepairWorkorder.getFaultStationAddress()+"");
			
		}
		return urgentrepairWorkorderMap;
	}
	
	/**
	 * 获取客户工单信息
	 * 抢修的工单页面或任务单页面里的客户工单信息tab
	 * */
	public Map getUrgentRepairCustomerWorkOrderService(String woId) {
		Map customerWorkorderMap = new HashMap();
		List<UrgentrepairCustomerworkorder> urgentrepairCustomerworkorders = urgentRepairCustomerWorkOrderService.getUrgentrepairCustomerWorkorderByWoId(woId);
		if(urgentrepairCustomerworkorders!=null&&!urgentrepairCustomerworkorders.isEmpty()){
			UrgentrepairCustomerworkorder urgentrepairCustomerworkorder = urgentrepairCustomerworkorders.get(0);
			String affectedServiceName = "";
			if(  urgentrepairCustomerworkorder.getAffectedServiceName()!=null){
				affectedServiceName =   urgentrepairCustomerworkorder.getAffectedServiceName()+"";
			}
			customerWorkorderMap.put("affectedServiceName", affectedServiceName);
			
			String alarmAssociatedId = "";
			if( urgentrepairCustomerworkorder.getAlarmAssociatedId()!=null){
				alarmAssociatedId = urgentrepairCustomerworkorder.getAlarmAssociatedId()+"";
			}
			customerWorkorderMap.put("alarmAssociatedId",alarmAssociatedId);
			
			String alarmClass = "";
			if(  urgentrepairCustomerworkorder.getAlarmClass()!=null){
				alarmClass = urgentrepairCustomerworkorder.getAlarmClass()+"";
			}
			customerWorkorderMap.put("alarmClass",alarmClass);
			
			String alarmFormalName = "";
			if( urgentrepairCustomerworkorder.getAlarmFormalName()!=null){
				alarmFormalName =  urgentrepairCustomerworkorder.getAlarmFormalName()+"";
			}
			customerWorkorderMap.put("alarmFormalName", alarmFormalName);
			
			String alarmLogicalClass = "";
			if( urgentrepairCustomerworkorder.getAlarmLogicalClass()!=null){
				alarmLogicalClass =  urgentrepairCustomerworkorder.getAlarmLogicalClass()+"";
			}
			customerWorkorderMap.put("alarmLogicalClass", alarmLogicalClass);
			
			String alarmLogicalSubClass = "";
			if( urgentrepairCustomerworkorder.getAlarmLogicalSubClass()!=null){
				alarmLogicalSubClass =  urgentrepairCustomerworkorder.getAlarmLogicalSubClass()+"";
			}
			customerWorkorderMap.put("alarmLogicalSubClass", alarmLogicalSubClass);
			
			String alarmNetManageSource = "";
			if(urgentrepairCustomerworkorder.getAlarmNetManageSource()!=null){
				alarmNetManageSource =  urgentrepairCustomerworkorder.getAlarmNetManageSource()+"";
			}
			customerWorkorderMap.put("alarmNetManageSource", alarmNetManageSource);
			
			String customerSenderDepartment = "";
			if( urgentrepairCustomerworkorder.getCustomerSenderDepartment()!=null){
				customerSenderDepartment =  urgentrepairCustomerworkorder.getCustomerSenderDepartment()+"";
			}
			customerWorkorderMap.put("customerSenderDepartment",customerSenderDepartment);
			
			String customerWoCurrentDepartment = "";
			if( urgentrepairCustomerworkorder.getCustomerWoCurrentDepartment()!=null){
				customerWoCurrentDepartment =  urgentrepairCustomerworkorder.getCustomerWoCurrentDepartment()+"";
			}
			
			customerWorkorderMap.put("customerWoCurrentDepartment",customerWoCurrentDepartment);
			
			String customerWoCurrentHandler = "";
			if(urgentrepairCustomerworkorder.getCustomerWoCurrentHandler()!=null){
				customerWoCurrentHandler =  urgentrepairCustomerworkorder.getCustomerWoCurrentHandler()+"";
			}
			customerWorkorderMap.put("customerWoCurrentHandler", customerWoCurrentHandler);
			
			String customerWoTitle = "";
			if(urgentrepairCustomerworkorder.getCustomerWoTitle()!=null){
				customerWoTitle = urgentrepairCustomerworkorder.getCustomerWoTitle()+"";
			}
			customerWorkorderMap.put("customerWoTitle", customerWoTitle);
			
			String customerWoSender = "";
			if( urgentrepairCustomerworkorder.getCustomerWoSender()!=null){
				customerWoSender =  urgentrepairCustomerworkorder.getCustomerWoSender()+"";
			}
			customerWorkorderMap.put("customerWoSender", customerWoSender);
			
			String isEmergencyFault = "";
			if( urgentrepairCustomerworkorder.getIsEmergencyFault()!=null){
				isEmergencyFault =  urgentrepairCustomerworkorder.getIsEmergencyFault()+"";
			}
			customerWorkorderMap.put("isEmergencyFault", isEmergencyFault);
			
			String netmanageAlarmLevel = "";
			if(urgentrepairCustomerworkorder.getNetmanageAlarmLevel()!=null){
				netmanageAlarmLevel = urgentrepairCustomerworkorder.getNetmanageAlarmLevel()+"";
			}
			customerWorkorderMap.put("netmanageAlarmLevel", netmanageAlarmLevel);
			
			String sendWoWay = "";
			if(urgentrepairCustomerworkorder.getSendWoWay()!=null){
				sendWoWay = urgentrepairCustomerworkorder.getSendWoWay()+"";
			}
			customerWorkorderMap.put("sendWoWay",sendWoWay);
			
			String sideeffectService = "";
			if( urgentrepairCustomerworkorder.getSideeffectService()!=null&&!"-1".equals(urgentrepairCustomerworkorder.getSideeffectService())){
				if(urgentrepairCustomerworkorder.getSideeffectService().equals("1")){
					sideeffectService = "是";
				}else if(urgentrepairCustomerworkorder.getSideeffectService().equals("0")){
					sideeffectService = "否";
				}
			}
			customerWorkorderMap.put("sideeffectService", sideeffectService);
			
			String subAlarmInfo = "";
			if(  urgentrepairCustomerworkorder.getSubAlarmInfo()!=null){
				subAlarmInfo =   urgentrepairCustomerworkorder.getSubAlarmInfo()+"";
			}
			customerWorkorderMap.put("subAlarmInfo",subAlarmInfo);
			
			String subAlarmNumber = "";
			if(  urgentrepairCustomerworkorder.getSubAlarmNumber()!=null){
				subAlarmNumber =   urgentrepairCustomerworkorder.getSubAlarmNumber()+"";
			}
			customerWorkorderMap.put("subAlarmNumber", subAlarmNumber);
			
			String cuswWoId = "";
			if( urgentrepairCustomerworkorder.getCustomerWoId()!=null){
				cuswWoId = urgentrepairCustomerworkorder.getCustomerWoId()+"";
			}
			customerWorkorderMap.put("customerWoId", cuswWoId);
		}
		return customerWorkorderMap;
	}
	
	
	/**
	 * 获取网优之家2g工单信息
	 * 抢修的工单页面或任务单页面里的客户工单信息tab，网优之家的2g工单的客户工单信息
	 * */
	public Map getUrgentRepairHome2GWorkOrderService(String woId) {
		Map customerWorkorderMap = new HashMap();
		Map params = new HashMap();
		params.put("woId", woId);
		List<UrgentrepairDevice2gworkorder> urgentrepairDevice2gworkorders = urgentRepairCustomerWorkOrderService.getUrgentrepairDevice2gWorkorderByParams(params);
		if(urgentrepairDevice2gworkorders!=null&&!urgentrepairDevice2gworkorders.isEmpty()){
			UrgentrepairDevice2gworkorder urgentrepairDevice2gworkorder = urgentrepairDevice2gworkorders.get(0);
			String woStatus = "";
			if(  urgentrepairDevice2gworkorder.getWoStatus()!=null){
				woStatus =  urgentrepairDevice2gworkorder.getWoStatus()+"";
			}
			customerWorkorderMap.put("woStatus", woStatus);
			
			String acceptMaintain = "";
			if(  urgentrepairDevice2gworkorder.getAcceptMaintain()!=null){
				acceptMaintain =  urgentrepairDevice2gworkorder.getAcceptMaintain()+"";
			}
			customerWorkorderMap.put("acceptMaintain", acceptMaintain);
			
			String assginOrderTime = "";
			if(  urgentrepairDevice2gworkorder.getAssginOrderTime()!=null){
				assginOrderTime = TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevice2gworkorder.getAssginOrderTime());
			}
			customerWorkorderMap.put("assginOrderTime", assginOrderTime);
			
			String acceptTime = "";
			if(  urgentrepairDevice2gworkorder.getAcceptTime()!=null){
				acceptTime = TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevice2gworkorder.getAcceptTime());
			}
			customerWorkorderMap.put("acceptTime", acceptTime);
			
			String isOverTime = "";
			if(  urgentrepairDevice2gworkorder.getIsOverTime()!=null){
				isOverTime =  urgentrepairDevice2gworkorder.getIsOverTime()+"";
			}
			customerWorkorderMap.put("isOverTime", isOverTime);
			
			String vipLevel = "";
			if(  urgentrepairDevice2gworkorder.getVipLevel()!=null){
				vipLevel =  urgentrepairDevice2gworkorder.getVipLevel()+"";
			}
			customerWorkorderMap.put("vipLevel", vipLevel);
			
			String smallAreaName = "";
			if(  urgentrepairDevice2gworkorder.getSmallAreaName()!=null){
				smallAreaName =  urgentrepairDevice2gworkorder.getSmallAreaName()+"";
			}
			customerWorkorderMap.put("smallAreaName", smallAreaName);
			
			String smallAreaNumber = "";
			if(  urgentrepairDevice2gworkorder.getSmallAreaNumber()!=null){
				smallAreaNumber =  urgentrepairDevice2gworkorder.getSmallAreaNumber()+"";
			}
			customerWorkorderMap.put("smallAreaNumber", smallAreaNumber);
			
			String alarmType = "";
			if(  urgentrepairDevice2gworkorder.getAlarmType()!=null){
				alarmType =  urgentrepairDevice2gworkorder.getAlarmType()+"";
			}
			customerWorkorderMap.put("alarmType", alarmType);
			
			String alarmLevel = "";
			if(  urgentrepairDevice2gworkorder.getAlarmLevel()!=null){
				alarmLevel =  urgentrepairDevice2gworkorder.getAlarmLevel()+"";
			}
			customerWorkorderMap.put("alarmLevel", alarmLevel);
			
			String faultType = "";
			if(  urgentrepairDevice2gworkorder.getFaultType()!=null){
				faultType =  urgentrepairDevice2gworkorder.getFaultType()+"";
			}
			customerWorkorderMap.put("faultType", faultType);
			
			String faultCause = "";
			if(  urgentrepairDevice2gworkorder.getFaultCause()!=null){
				faultCause =  urgentrepairDevice2gworkorder.getFaultCause()+"";
			}
			customerWorkorderMap.put("faultCause", faultCause);
			
			String firstAlarmTime = "";
			if(  urgentrepairDevice2gworkorder.getFirstAlarmTime()!=null){
				firstAlarmTime =  TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevice2gworkorder.getFirstAlarmTime())+"";
			}
			customerWorkorderMap.put("firstAlarmTime", firstAlarmTime);
			
			String latestAlarmTime = "";
			if(  urgentrepairDevice2gworkorder.getLatestAlarmTime()!=null){
				latestAlarmTime = TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevice2gworkorder.getLatestAlarmTime())+"";
			}
			customerWorkorderMap.put("latestAlarmTime", latestAlarmTime);
			
			String recoverTime = "";
			if(  urgentrepairDevice2gworkorder.getRecoverTime()!=null){
				recoverTime =  TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevice2gworkorder.getRecoverTime())+"";
			}
			customerWorkorderMap.put("recoverTime", recoverTime);
			
			String alarmContent = "";
			if(  urgentrepairDevice2gworkorder.getAlarmContent()!=null){
				alarmContent =  urgentrepairDevice2gworkorder.getAlarmContent()+"";
			}
			customerWorkorderMap.put("alarmContent", alarmContent);
			
			String customerWoId = "";
			if(  urgentrepairDevice2gworkorder.getCustomerWoId()!=null){
				customerWoId =  urgentrepairDevice2gworkorder.getCustomerWoId()+"";
			}
			customerWorkorderMap.put("customerWoId", customerWoId);
		}
		return customerWorkorderMap;
	}
	
	/**
	 * 获取网优之家Td工单信息
	 * 抢修的工单页面或任务单页面里的客户工单信息tab，网优之家的td工单的客户工单信息
	 * */
	public Map getUrgentRepairHomeTdWorkOrderService(String woId) {
		Map customerWorkorderMap = new HashMap();
		Map params = new HashMap();
		params.put("woId", woId);
		List<UrgentrepairDevicetdworkorder> urgentrepairDevicetdworkorders = urgentRepairCustomerWorkOrderService.getUrgentrepairDevicetdWorkorderByParams(params);
		if(urgentrepairDevicetdworkorders!=null&&!urgentrepairDevicetdworkorders.isEmpty()){
			UrgentrepairDevicetdworkorder urgentrepairDevicetdworkorder = urgentrepairDevicetdworkorders.get(0);
			String woStatus = "";
			if(  urgentrepairDevicetdworkorder.getWoStatus()!=null){
				woStatus =  urgentrepairDevicetdworkorder.getWoStatus()+"";
			}
			customerWorkorderMap.put("woStatus", woStatus);
			
			String deviceMaintain = "";
			if(  urgentrepairDevicetdworkorder.getDeviceMaintain()!=null){
				deviceMaintain =  urgentrepairDevicetdworkorder.getDeviceMaintain()+"";
			}
			customerWorkorderMap.put("deviceMaintain", deviceMaintain);
			
			String assginOrderTime = "";
			if(  urgentrepairDevicetdworkorder.getAssginOrderTime()!=null){
				assginOrderTime = TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevicetdworkorder.getAssginOrderTime());
			}
			customerWorkorderMap.put("assginOrderTime", assginOrderTime);
			
			String deviceTdName = "";
			if(  urgentrepairDevicetdworkorder.getDeviceTdName()!=null){
				deviceTdName = urgentrepairDevicetdworkorder.getDeviceTdName();
			}
			customerWorkorderMap.put("deviceTdName", deviceTdName);
			
			String handleProgress = "";
			if(  urgentrepairDevicetdworkorder.getHandleProgress()!=null){
				handleProgress =  urgentrepairDevicetdworkorder.getHandleProgress()+"";
			}
			customerWorkorderMap.put("handleProgress", handleProgress);
			
			String acceptDomain = "";
			if(  urgentrepairDevicetdworkorder.getAcceptDomain()!=null){
				acceptDomain =  urgentrepairDevicetdworkorder.getAcceptDomain()+"";
			}
			customerWorkorderMap.put("acceptDomain", acceptDomain);
			
			String vipLevel = "";
			if(  urgentrepairDevicetdworkorder.getVipLevel()!=null){
				vipLevel =  urgentrepairDevicetdworkorder.getVipLevel()+"";
			}
			customerWorkorderMap.put("vipLevel", vipLevel);
			
			String seriousLevel = "";
			if(  urgentrepairDevicetdworkorder.getSeriousLevel()!=null){
				seriousLevel =  urgentrepairDevicetdworkorder.getSeriousLevel()+"";
			}
			customerWorkorderMap.put("seriousLevel", seriousLevel);
			
			
			String alarmLevel = "";
			if(  urgentrepairDevicetdworkorder.getAlarmLevel()!=null){
				alarmLevel =  urgentrepairDevicetdworkorder.getAlarmLevel()+"";
			}
			customerWorkorderMap.put("alarmLevel", alarmLevel);
			
			String faultType = "";
			if(  urgentrepairDevicetdworkorder.getFaultType()!=null){
				faultType =  urgentrepairDevicetdworkorder.getFaultType()+"";
			}
			customerWorkorderMap.put("faultType", faultType);
			
			String faultCause = "";
			if(  urgentrepairDevicetdworkorder.getFaultReason()!=null){
				faultCause =  urgentrepairDevicetdworkorder.getFaultReason()+"";
			}
			customerWorkorderMap.put("faultCause", faultCause);
			
			String firstAlarmTime = "";
			if(  urgentrepairDevicetdworkorder.getFirstAlarmTime()!=null){
				firstAlarmTime =  TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevicetdworkorder.getFirstAlarmTime())+"";
			}
			customerWorkorderMap.put("firstAlarmTime", firstAlarmTime);
			
			String latestAlarmTime = "";
			if(  urgentrepairDevicetdworkorder.getLatestAlarmTime()!=null){
				latestAlarmTime = TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevicetdworkorder.getLatestAlarmTime())+"";
			}
			customerWorkorderMap.put("latestAlarmTime", latestAlarmTime);
			
			String recoverTime = "";
			if(  urgentrepairDevicetdworkorder.getRecoverTime()!=null){
				recoverTime =  TimeFormatHelper.getTimeFormatBySecond(urgentrepairDevicetdworkorder.getRecoverTime())+"";
			}
			customerWorkorderMap.put("recoverTime", recoverTime);
			
			String alarmContent = "";
			if(  urgentrepairDevicetdworkorder.getAlarmContent()!=null){
				alarmContent =  urgentrepairDevicetdworkorder.getAlarmContent()+"";
			}
			customerWorkorderMap.put("alarmContent", alarmContent);
			
			String customerWoId = "";
			if(  urgentrepairDevicetdworkorder.getCustomerWoId()!=null){
				customerWoId =  urgentrepairDevicetdworkorder.getCustomerWoId()+"";
			}
			customerWorkorderMap.put("customerWoId", customerWoId);
		}
		return customerWorkorderMap;
	}
	
	/**
	 * 获取任务单信息
	 * @param toId
	 * @return
	 */
	public Map getUrgentRepairTaskOrderService(String toId){
		Map taskOrderMap = workManageService.getTaskOrderForShow(toId);
		if(taskOrderMap==null){
			return null;
		}
		taskOrderMap.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(taskOrderMap.get("assignTime")));
		taskOrderMap.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(taskOrderMap.get("requireCompleteTime")));
		return taskOrderMap;
	}

	/**
	 * 获工单流转过程
	 * 抢修的工单页面或任务单页面里的流转过程tab
	 * */
	public List<Map> getUrgentRepairWorkOrderProcessInfoService(String woId) {
		List<Map> procedureMapList = new ArrayList<Map>();
		Map procedureMap = new HashMap();
		Map workOrderMap = workManageService.getWorkOrderForShow(woId);
		if(workOrderMap!=null){
			procedureMap.put("orderId", workOrderMap.get("woId"));
			procedureMap.put("type", workOrderMap.get("bizProcessName"));
			List<Map> childTasks = workManageService.getChildTaskOrderListByWoId(woId);
			if(childTasks!=null&&!childTasks.isEmpty()){
				List<Map> childToList = new ArrayList<Map>();
				for(Map map : childTasks){
					List<Map> childMaps = childMaps = getTaskOrderTree(map.get("toId")+"");
					childToList.addAll(childMaps);
					
				}
				procedureMap.put("childTree", childToList);
			}
			procedureMapList.add(procedureMap);
		}
		return procedureMapList;
	}
	
	/**
	 * 递归获取任务单
	 * 获取工单流转过程需要递归获取任务单，getUrgentRepairWorkOrderProcessInfoService 调用到
	 * @param toId
	 * @return
	 */
	private List<Map> getTaskOrderTree(String toId){
		Map taskOrder = workManageService.getTaskOrderForShow(toId);
		if(taskOrder==null){
			return null;
		}
		List<Map> tempList = new ArrayList<Map>();
		Map temp = new HashMap();
		temp.put("orderId", taskOrder.get("toId"));
		temp.put("type",taskOrder.get("bizProcessName"));
		List<Map> taskOrderList = workManageService.getChildTaskOrderListByToId(toId);
		if(taskOrderList!=null&&!taskOrderList.isEmpty()){
			for(Map map : taskOrderList){
				List<Map> childTaskOrderList = getTaskOrderTree(map.get("toId")+"");
				temp.put("childTree", childTaskOrderList);
			}
		}
		tempList.add(temp);
		
		return tempList;
	}
	
	/**
	 * 检查工单的下级任务是否已全部结束或已撤销
	 * @param woId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByWoId(String woId){
		//获取任务单的下级任务
		List<Map> subToMaps = workManageService.getChildTaskOrderListByWoId(woId);
		//当下级任务不为空
		if(subToMaps!=null&&!subToMaps.isEmpty()){
			//遍历下级任务
			for(Map subToMap : subToMaps){
				//当发现下级任务有状态为未结束或未撤销的任务时，返回false
				if(!"已结束".equals(subToMap.get("statusName"))&&!"已撤销".equals(subToMap.get("statusName"))){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 检查工单的下级任务是否已全部结束或已撤销(当前任务除外)
	 * 任务单最终回复时，要检查其隶属的工单下的子任务是否全部结束，自身除外，若除自身外结束则更改工单状态为“处理中”
	 * @param woId
	 * @param subToId  检查的下级任务中排除当前任务
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByWoId(String woId,String subToId){
		//获取任务单的下级任务
		List<Map> subToMaps = workManageService.getChildTaskOrderListByWoId(woId);
		//当下级任务不为空
		if(subToMaps!=null&&!subToMaps.isEmpty()){
			//遍历下级任务
			for(Map subToMap : subToMaps){
				if(!subToId.equals(subToMap.get("toId"))){
					//当发现下级任务有状态为未结束或未撤销的任务时，返回false
					if(!"已结束".equals(subToMap.get("statusName"))&&!"已撤销".equals(subToMap.get("statusName"))){
						return false;
					}
				}
				
			}
		}
		return true;
	}
	
	/**
	 * 检查任务单的下级任务是否已全部结束
	 * 工单最终回复时要检查下级任务是否全部结束
	 * @param woId
	 * @return
	 */
	public boolean hasAllSubTasksFinishedByToId(String toId){
		//获取任务单的下级任务
		List<Map> subToMaps = workManageService.getChildTaskOrderListByToId(toId);
		//当下级任务不为空
		if(subToMaps!=null&&!subToMaps.isEmpty()){
			//遍历下级任务
			for(Map subToMap : subToMaps){
				//当发现下级任务有状态为未结束的任务时，返回false
				if(!"已结束".equals(subToMap.get("statusName"))&&!"已撤销".equals(subToMap.get("statusName"))){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据工单责任人账号和工单受理专业获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 */
	public List<Map> getWorkOrderListByAccountAndAcceptProfessionalService(String account,String acceptProfessional,String startTime,String endTime){
		List<Map> woList = new ArrayList<Map>();
		woList = urgentRepairWorkOrderDao.getWorkOrderListByAccountAndAcceptProfessionalDao(account, acceptProfessional, startTime, endTime);
		return woList;
		
	}
	
	/**
	 * 根据组织id集合获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 */
	public List<Map> getWorkOrderListByOrgIdsService(List<String> orgIds,String startTime,String endTime){
		List<Map> woList = new ArrayList<Map>();
		woList = urgentRepairWorkOrderDao.getWorkOrderListByOrgIdsDao(orgIds, startTime, endTime);
		return woList;
		
	}
	
	/**
	 * 通过webserver接口受理网优之家工单
	 * 网优之家的工单，在iosm工单受理时需要调用网优之家接口去更新工单状态
	 */
	public boolean callWebServerAcceptWorkOrder(String woId,String customerWoType){
		String status ="";
		String homeWoId="";
		String res = "";
		Map map = new HashMap();
		map.put("iosmWoId", woId);
		List<WorkorderinterfaceWangyouWorkorderRelation> relation=this.workorderinterfaceWangyouWorkorderRelationService.getWorkorderinterfaceWangyouWorkorderRelationList(map);
		if(relation!=null&&!relation.isEmpty()){
			homeWoId = relation.get(0).getCustomerWoId();
			//判断是否为2g工单
			if(customerWoType.equals("home2g")){
				Map cwoMap = new HashMap();
				cwoMap.put("customerWoId", homeWoId);
				List<UrgentrepairDevice2gworkorder> wos = this.urgentRepairCustomerWorkOrderService.getUrgentrepairDevice2gWorkorderByParams(cwoMap);
				if(wos!=null){
					status = wos.get(0).getWoStatus();
				}
			}
			//判断是否为td工单
			if(customerWoType.equals("homeTd")){
				Map cwoMap = new HashMap();
				cwoMap.put("customerWoId", homeWoId);
				List<UrgentrepairDevicetdworkorder> wos = this.urgentRepairCustomerWorkOrderService.getUrgentrepairDevicetdWorkorderByParams(cwoMap);
				if(wos!=null){
					status = wos.get(0).getWoStatus();
				}
			}
			//如果网优之家工单的状态为“未签收”，则对网优之家的工单进行签收，并更新其状态和信息
			if(status.equals("未签收")){
				try{
					String customWoId="";
					String customSystem="";
					String queryType="";
					//根据iosmWorkOrderId获取客户工单id
					customWoId=relation.get(0).getCustomerWoId();
					customSystem=relation.get(0).getCustomInterfaceSys();
					queryType=relation.get(0).getWorkOrderTypeCode();
					
					if(customWoId!=null && !"".equals(customWoId)){
						//调用webServer接口，URL在InterfaceURLConfig.properties里
						String interfaceURL = com.iscreate.plat.tools.InterfaceUtil.getConfigValueFromProperties("acceptUrgentRepairWorkOrderAction.URL")+"?customWorkOrderId="+customWoId+"&acceptResult=success&customSystem="+customSystem+"&queryType="+queryType;
						res =com.iscreate.plat.tools.InterfaceUtil.httpClientGetReuqest(interfaceURL);
						if(res==null||"".equals(res)||"null".equals(res)||"fail".equals(res)){
							return false;
						}else{
							return true;
						}
					}
					
				}catch(Exception e){
					return false;
				}
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过webserver接口回复网优之家工单
	 * 网优之家的工单，在iosm工单受理时需要调用网优之家接口去更新工单状态
	 */
	public boolean callWebServerReplyWorkOrder(String woId,UrgentrepairWorkorder urgentrepairWorkorder,String acceptedPeople){
		try{
			String customWoId="";
			String customSystem="";
			String queryType="";
			String res ="";
			String woJson="";
			
			Map<String,Object> woMap = new HashMap<String,Object>();;
			woMap.put("faultCause", urgentrepairWorkorder.getFaultCause());
			woMap.put("faultGenera", urgentrepairWorkorder.getFaultGenera());
			woMap.put("howToDeal", urgentrepairWorkorder.getHowToDeal());
			woMap.put("resonForDelayApply", urgentrepairWorkorder.getResonForDelayApply());
			woMap.put("isAlarmClear", urgentrepairWorkorder.getIsAlarmClear());
			woMap.put("affectedServiceName", urgentrepairWorkorder.getAffectedServiceName());
			woMap.put("sideeffectService", urgentrepairWorkorder.getSideeffectService());
			woMap.put("faultDealResult", urgentrepairWorkorder.getFaultDealResult());
			woMap.put("alarmClearTime", urgentrepairWorkorder.getAlarmClearTime());
			if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
				woMap.put("faultSolveTime", urgentrepairWorkorder.getFaultSolveTime());
			}else{
				//预计解决时间
				woMap.put("foreseeResolveTime", urgentrepairWorkorder.getForeseeResolveTime());
			}
			woMap.put("workOrderAcceptedPeople", acceptedPeople);
			woMap.put("handleTime", new Date());
			woMap.put("acceptProfessional", urgentrepairWorkorder.getAcceptProfessional());
			if(woMap!=null){
				Gson gson = new Gson();
				woJson = gson.toJson(woMap);
				woJson =URLEncoder.encode(woJson, "UTF-8");

			}
			
			//根据iosmWorkOrderId获取客户工单id
			Map map = new HashMap();
			map.put("iosmWoId", woId);
			List<WorkorderinterfaceWangyouWorkorderRelation> relation=this.workorderinterfaceWangyouWorkorderRelationService.getWorkorderinterfaceWangyouWorkorderRelationList(map);
			if(relation!=null&&!relation.isEmpty()){
				customWoId=relation.get(0).getCustomerWoId();
				customSystem=relation.get(0).getCustomInterfaceSys();
				queryType=relation.get(0).getWorkOrderTypeCode();
				if(customWoId!=null && !"".equals(customWoId)){
					//调用webServer接口，URL在InterfaceURLConfig.properties里
					String interfaceURL = com.iscreate.plat.tools.InterfaceUtil.getConfigValueFromProperties("replyUrgentRepairIosmWorkOrderOfLastAction.URL")+"?customWorkOrderId="+customWoId+"&lastReplyResult=success&lastReplyJson="+woJson+"&customSystem="+customSystem+"&queryType="+queryType;
					res=com.iscreate.plat.tools.InterfaceUtil.httpClientGetReuqest(interfaceURL);
					if("fail".equals(res)){
						return false;
					}else{
						return true;
					}
				}
			}else{
				return false;
			}
				
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
	/**
	 * 通过webserver接口阶段回复网优之家工单
	 * 网优之家的工单，在iosm工单受理时需要调用网优之家接口去更新工单状态
	 */
	public boolean callWebServerStepReplyWorkOrder(String woId,String customerWoType,String processingProgress,String faultType,String faultResult){
		String woJson = "";
		Map map = new HashMap();
		map.put("iosmWoId", woId);
		//获取网优之家工单与iosm工单的关联表
		List<WorkorderinterfaceWangyouWorkorderRelation> relation=this.workorderinterfaceWangyouWorkorderRelationService.getWorkorderinterfaceWangyouWorkorderRelationList(map);
		if(relation!=null&&!relation.isEmpty()){
			String homeWoId = relation.get(0).getCustomerWoId();
			String status ="";
			//判断是否为2G工单
			if(customerWoType.equals("home2g")){
				Map cwoMap = new HashMap();
				cwoMap.put("customerWoId", homeWoId);
				List<UrgentrepairDevice2gworkorder> wos = this.urgentRepairCustomerWorkOrderService.getUrgentrepairDevice2gWorkorderByParams(cwoMap);
				if(wos!=null){
					status = wos.get(0).getWoStatus();
				}
			}
			//判断是否为TD工单
			if(customerWoType.equals("homeTd")){
				Map cwoMap = new HashMap();
				cwoMap.put("customerWoId", homeWoId);
				List<UrgentrepairDevicetdworkorder> wos = this.urgentRepairCustomerWorkOrderService.getUrgentrepairDevicetdWorkorderByParams(cwoMap);
				if(wos!=null){
					status = wos.get(0).getWoStatus();
				}
			}
			//判断网优之家工单的状态，如果不是“已回复”，则对其阶段性回复更新其信息
			if(!status.equals("已回复")){
				try{
					//webserver 个性工单信息 json
					Map<String,Object> woMap = new HashMap<String,Object>();
					woMap.put("dealProgress", processingProgress);
					woMap.put("faultType", faultType);
					woMap.put("faultCause", faultResult);
					
					if(woMap!=null){
						Gson gson = new Gson();
						woJson = gson.toJson(woMap);
						woJson =URLEncoder.encode(woJson, "UTF-8");
		
					}
					//调用webServer接口，URL在InterfaceURLConfig.properties里
					String interfaceURL = com.iscreate.plat.tools.InterfaceUtil.getConfigValueFromProperties("replyUrgentRepairIosmWorkOrderOfLastAction.URL")+"?iosmWorkOrderId="+woId+"&lastReplyResult=success&lastReplyJson="+woJson;
					String res=com.iscreate.plat.tools.InterfaceUtil.httpClientGetReuqest(interfaceURL);
					
					if("fail".equals(res)){
						return false;
					}else{
						return true;
					}
				}catch(Exception e){
					return false;
				}
			}
		}else{
			return false;
		}
		
		return false;
	}
	
	public UrgentRepairWorkOrderDao getUrgentRepairWorkOrderDao() {
		return urgentRepairWorkOrderDao;
	}

	public void setUrgentRepairWorkOrderDao(
			UrgentRepairWorkOrderDao urgentRepairWorkOrderDao) {
		this.urgentRepairWorkOrderDao = urgentRepairWorkOrderDao;
	}

	
	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	public WorkorderinterfaceWangyouWorkorderRelationService getWorkorderinterfaceWangyouWorkorderRelationService() {
		return workorderinterfaceWangyouWorkorderRelationService;
	}

	public void setWorkorderinterfaceWangyouWorkorderRelationService(
			WorkorderinterfaceWangyouWorkorderRelationService workorderinterfaceWangyouWorkorderRelationService) {
		this.workorderinterfaceWangyouWorkorderRelationService = workorderinterfaceWangyouWorkorderRelationService;
	}

	public UrgentRepairCustomerWorkOrderService getUrgentRepairCustomerWorkOrderService() {
		return urgentRepairCustomerWorkOrderService;
	}

	public void setUrgentRepairCustomerWorkOrderService(
			UrgentRepairCustomerWorkOrderService urgentRepairCustomerWorkOrderService) {
		this.urgentRepairCustomerWorkOrderService = urgentRepairCustomerWorkOrderService;
	}
	
	
}
