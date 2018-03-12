package com.iscreate.op.service.report;

import java.util.Map;

public interface PortalReportService {
	/**
	 * 根据受理专业获取故障抢修单
	* @date Nov 2, 20125:01:05 PM
	* @Description: TODO 
	* @param @param userId
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public Map<String,Object>  getUrgentRepairReportOrderCountByAcceptProfessional(String userId,String startTime ,String endTime);
	
	/**
	 * 根据时间与组织统计
	* @date Nov 2, 20125:01:12 PM
	* @Description: TODO 
	* @param @param userId
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getOrderCountByWotemplate(String userId,String startTime ,String endTime);
	
	public Map<String, Object> getReportByOrgId(long orgId,String startTime,String endTime);
	
	public Map<String, Object> getProjectByOrgId(String projectId,String startTime,String endTime);
}
