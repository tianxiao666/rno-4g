package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface UrgentRepairReportService {
	/**
	 * 获取故障抢修指定时间段的报表数据
	* @date Nov 2, 20124:54:44 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudge( long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 *  获取工单数(按组织)
	* @date Nov 2, 20124:54:37 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByOrgCount(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue);
	
	/**
	 * 获取故障工单处理及时率(按组织)
	* @date Nov 2, 20123:58:28 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 获取故障工单处理及时率(按基站类型)
	* @date Nov 2, 20124:51:43 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByBaseStationLevel(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障工单处理及时率(按故障类型)
	* @date Nov 2, 20124:51:51 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultType(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 获取故障工单处理及时率(按故障级别)
	* @date Nov 2, 20124:53:53 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultLevel(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障工单处理及时率(按受理专业)
	* @date Nov 2, 20124:54:28 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByAcceptProfessional(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 获取故障工单处理及时率(按故障大类)
	* @date Nov 2, 20125:27:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultGenera(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 根据不同条件查询工单详细信息
	* @date Nov 6, 20129:32:53 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairWorkorderManage(long orgId,String beginTime,String endTime,String rowName,String judge, String rowValue);
	
	
	/**
	 * 根据不同条件查询工单详细信息
	* @date Nov 6, 20129:32:53 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairWorkorderManageProject(long orgId,String beginTime,String endTime,String rowName,String judge, String rowValue);
	
	public Map<String, Object> getTreeNode();
	
	/**
	 * 获取故障工单处理及时率(按组织)向上
	* @date Nov 2, 20123:58:28 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
		
	/**
	 * 获取故障工单处理及时率(按基站类型)向上
	* @date Nov 2, 20124:51:43 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByBaseStationLevelAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 获取故障工单处理及时率(按故障类型)向上
	* @date Nov 2, 20124:51:51 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultTypeAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	
	/**
	 * 获取故障工单处理及时率(按故障级别)向上
	* @date Nov 2, 20124:53:53 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultLevelAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障工单处理及时率(按受理专业)向上
	* @date Nov 2, 20124:54:28 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByAcceptProfessionalAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障工单处理及时率(按故障大类)向上
	* @date Nov 2, 20125:27:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultGeneraAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障工单处理及时率(按项目)
	* @date Nov 2, 20123:58:28 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByOrgCountProject(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
	
	/**
	 * 获取故障抢修指定时间段的报表数据(项目)
	* @date Nov 2, 20124:54:44 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudgeProject( long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue );
		
	
	/**
	 * 获取userId获取用户身份
	* @date Mar 6, 20134:07:54 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getLoginIdBelongEnterpriseType(String userId);
}
