package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.report.ReportStaffTaskCountMonth;



public interface StaffTaskCountMonthReportDao {
	/**
	 * 根据组织Id获取该组织下属人员某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<ReportStaffTaskCountMonth> getStaffTaskCountReportByOrgId(long orgId,String beginTime,String endTime);
	
	/**
	 * 根据人员帐号获取该人员某段时间内的报表数据
	 * @param staffAccount 人员帐号
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<ReportStaffTaskCountMonth> getStaffTaskCountReportByOrgId(String staffAccount,String beginTime,String endTime);
	
}
