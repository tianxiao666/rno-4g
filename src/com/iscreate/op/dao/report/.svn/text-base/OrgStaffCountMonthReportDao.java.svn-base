package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.report.ReportOrgStaffCountMonth;
import com.iscreate.op.pojo.report.ReportOrgStaffSkillMonth;

public interface OrgStaffCountMonthReportDao {
	/**
	 * 根据组织Id获取人员数量
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgStaffCountByOrgId(long orgId,String beginTime,String endTime);
	
	/**
	 * 根据组织Id获取人员技能分类统计信息 
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgStaffSkillByOrgId(long orgId,String beginTime,String endTime);
}
