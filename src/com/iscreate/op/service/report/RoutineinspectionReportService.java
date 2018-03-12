package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

public interface RoutineinspectionReportService {
	/**
	 * 获取组织ID获取该组织以下组织的巡检报表数据TOP6
	* @date Mar 6, 20133:02:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPSixByOrgId(long orgId);
	
	/**
	 * 获取userId获取用户身份
	* @date Mar 6, 20134:07:54 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getLoginIdBelongEnterpriseType(String userId);
	
	/**
	 * 获取项目ID获取该项目关联组织的巡检报表数据TOP4
	* @date Mar 6, 20134:53:00 PM
	* @Description: TODO 
	* @param @param projectId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPFourByProjectId(String projectId);
}
