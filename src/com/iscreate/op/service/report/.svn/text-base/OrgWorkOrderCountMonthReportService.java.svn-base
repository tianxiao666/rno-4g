package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

public interface OrgWorkOrderCountMonthReportService {
	/**
	 * 根据组织Id获取该组织某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<Map> getOrgWorkOrderCountReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取下级组织某段时间内的报表数据
	 * @param orgId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getNextOrgWorkOrderCountReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取当前用户组织架构下工单数统计
	 * @param userId
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUserOrgWorkOrderCountReportService(String userId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取下级组织报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getNextOrgReportInfoService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取用户组织架构工单数量环比报表信息
	 * @param orgId 组织Id 
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgWorkOrderCountChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取上级工单数报表信息
	 * @param orgId 组织架构Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUpOrgReportInfoService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取上级组织架构工单数量环比报表信息
	 * @param orgId 组织Id 
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgWorkOrderCountChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取组织架构下 项目工单数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgProjectWorkOrderCountReportService(long orgId,String beginTime,String endTime);
	
	/*------------------------------ 人员及任务数统计 -----------------------------------------------*/
	/**
	 * 获取用户组织架构下人员及任务数统计
	 * @param userId 用户Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgCompareReportService(String userId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgCompareReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取人员及任务数统计 环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgCompareChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取上级组织的 人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUpOrgCompareReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取上级组织的 人员及任务数统计 环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgCompareChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取组织下项目的人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgProjectStaffAndWorkOrderCountReportService(long orgId,String beginTime,String endTime);
}
