package com.iscreate.plat.gantt.service;

import java.util.List;
import java.util.Map;


public interface GanttService {
	/**
	 * 获取人员某一天任务甘特列表
	 * @param staffId 人员Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getStaffTaskGantt(String staffId,String taskDate);
	
	/**
	 * 获取车辆某一天工单甘特列表
	 * @param carId 车辆Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getCarWorkOrderGantt(String carId,String taskDate);
	
	/**
	 * 获取站址某一天工单甘特列表
	 * @param stationId 站址Id
	 * @param taskDate 任务日期
	 * @return
	 */
	public List<Map> getStationWorkOrderGantt(String stationId,String taskDate);
	
	
	/**
	 * 获取基站某天工单甘特列表
	 * @param baseStationId 基站Id
	 * @param baseStationType 基站类型
	 * @param taskDate 任务日期
	 * @param isOverTime 是否超时
	 * @return
	 */
	public List<Map> getBaseStationWorkOrderGantt(String baseStationId,String baseStationType,String taskDate,int isOverTime);
	
	/**
	 * 获取资源任务甘特列表
	 * @param resourceId 资源Id
	 * @param resourceType 资源类型：car、people、station、baseStation
	 * @param taskDate 任务日期
	 * @param isOverTime 是否超时
	 * @return
	 */
	public List<Map> getResourceTaskGanttService(String resourceId,String resourceType,String taskDate,int isOverTime);
	
	/**
	 * 获取资源某月任务甘特信息
	 * @param resourceId 资源Id
	 * @param resourceType 资源类型
	 * @param taskDate 格式：2012-11 
	 * @param endDate 某月最后一天
	 * @param isOverTime 是否超时
	 * @return
	 */
	public Map getResourceMonthTaskGanttService(String resourceId,String resourceType,String taskDate,String endDate,int isOverTime);
	
}
