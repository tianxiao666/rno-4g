package com.iscreate.op.service.routineinspection;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;

public interface RoutineInspectionPlanService {

	
	
	/**
	 * 创建巡检计划工单
	 * @param userId
	 * @param routineinspectionPlanworkorder
	 * @param routineinspectionTaskorderList
	 * @return
	 */
	public boolean txCreateRoutineInspectionPlanWorkOrder(String userId,RoutineinspectionPlanworkorder routineinspectionPlanworkorder,
			List<RoutineinspectionTaskorder> routineinspectionTaskorderList);
	
	
	/**
	 * 根据工单号获取工单详细信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionPlanWorkOrderInfoByWoIdService(String woId);
	
	
//	public void txSendEmailToRelationPeople(Long planRelationOrg,List);
	
	/**
	 * 根据条件查询计划
	 * @param currentPage
	 * @param pageSize
	 * @param strParams
	 * @param intParams
	 * @return
	 */
	public Map<String,Object> searchRoutineInspectionPlanWorkOrderByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams);
	
	/**
	 * 根据工单号关闭工单
	 * @param woId
	 * @return
	 */
	public boolean txClosePlanWorkOrderByWoIdService(String woId);
	
	/**
	 * 根据工单号删除工单
	 * @param woId
	 * @return
	 */
	public boolean txDeletePlanWorkOrderByWoIdService(String woId);
	
}
