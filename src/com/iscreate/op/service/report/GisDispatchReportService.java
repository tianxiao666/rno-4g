package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

public interface GisDispatchReportService {
	/**
	 * 根据userId，获取站址，车辆，人员的数量统计
	* @date Nov 8, 20129:57:39 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String,String>> getResouceCountList(String userId);
	
	
	/**
	 * 根据组织ID获取组织下人员的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param bizunitInstanceId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByStaff(String userId);
	
	/**
	 * 根据组织ID获取组织下车辆的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param bizunitInstanceId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByCar(String userId);
	
	/**
	 * 根据组织ID获取组织下基站的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByBaseStation(String userId);
}
