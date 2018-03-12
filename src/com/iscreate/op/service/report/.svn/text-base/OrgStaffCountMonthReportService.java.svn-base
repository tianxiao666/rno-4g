package com.iscreate.op.service.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.report.ReportOrgStaffCountMonth;

public interface OrgStaffCountMonthReportService {
	/**
	 * 根据组织Id获取人员数量
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map> getOrgStaffCountByOrgIdService(long orgId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取下级组织人员数量
	 * @param orgId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getNextOrgStaffCountReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取当前用户组织下人员数统计
	 * @param userId 用户Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUserOrgStaffrCountReportService(String userId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取下级组织人员数量报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getNextOrgStaffCountReportInfoService(long orgId, String beginTime,String endTime);
		
	/**
	 * 获取组织人员数量环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgStaffCountChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取组织架构人员技能分类统计信息
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgStaffSkillReportService(long orgId,String beginTime,String endTime);
	
	/**
	 * 根据组织架构Id获取人员技能分类统计信息
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map> getOrgStaffSkillReportByOrgIdService(long orgId,String beginTime,String endTime);
	
	/**
	 * 获取上级组织人员数量报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUpOrgStaffCountReportInfoService(long orgId, String beginTime,String endTime);
		
	/**
	 * 获取上级组织人员数量环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgStaffCountChainReportService(long orgId,List<String> yearMonthList);
	
	/**
	 * 获取组织架构下项目人员数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgProjectStaffCountReportService(long orgId,String beginTime,String endTime);
	
}
