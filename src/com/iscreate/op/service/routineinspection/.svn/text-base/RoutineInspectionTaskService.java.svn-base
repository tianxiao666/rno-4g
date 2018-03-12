package com.iscreate.op.service.routineinspection;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;

public interface RoutineInspectionTaskService {

	/**
	 * 根据任务单Id获取任务单信息
	 * @param toId
	 * @return
	 */
	public RoutineinspectionTaskorder getRoutineInspectionTaskByToIdService(String toId);
	
	/**
	 * 根据任务单Id获取任务单信息(终端)
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionTaskInfoByToIdForMobileService(String toId);
	
	/**
	 * 保存任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean txSaveRoutineInspectionTaskOrderService(RoutineinspectionTaskorder routineinspectionTaskorder);
	
	/**
	 * 修改任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean txUpdateRoutineInspectionTaskOrderService(RoutineinspectionTaskorder routineinspectionTaskorder);
	
	/**
	 * 根据工单号获取任务单信息(分页)
	 * @param woId
	 * @return
	 */
	public Map<String,Object> getRoutineInspectionInfoByWoIdtoPageService(int currentPage,int pageSize,Map<String,String> strParams,Map<String,String> intParams);
	
	/**
	 * 根据任务单号获取任务单的详细信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> loadRoutineInspectionInfoByToIdService(String toId);
	
	/**
	 * 根据条件查询任务列表
	 * @param currentPage
	 * @param pageSize
	 * @param strParams
	 * @param intParams
	 * @return
	 */
	public Map<String,Object> searchRoutineInspectionByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,Map<String,String> intInParams);
	
	/**
	 * 签到
	 * @param toId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean txSignInRoutineInspectionService(String toId,Double longitude,Double latitude,Float deviate);
	
	/**
	 * 签退
	 * @param toId
	 * @return
	 */
	public boolean signOutRoutineInspectionService(String toId);
	
	/**
	 * 获取当前位置是否已偏离
	 * @param toId
	 * @return
	 */
	public Map<String,String> getDeviateByToIdService(String toId,Double currentLng,Double currentLat);
	
	/**
	 * 判断登陆人是否为维护人员
	 * @return
	 */
	public boolean judgeLoginPeopleIsMaintenanceWorkerService(String userId,String toId);
	
	
	/**
	 * @ahthor:che.yd
	 * 根据条件查询获取待办任务列表
	 * @param currentPage
	 * @param pageSize
	 * @param strParams
	 * @param intParams
	 * @return
	 */
	public Map<String,Object> searchPendingRoutineInspectionByParamsService(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,Map<String,String> intInParams,String queryCondition);
	
	
	
}
