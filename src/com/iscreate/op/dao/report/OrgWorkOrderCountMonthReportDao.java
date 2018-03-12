package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.report.ReportOrgWorkOrderCountMonth;

public interface OrgWorkOrderCountMonthReportDao {
	/**
	 * 根据组织Id获取该组织某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间 
	 * @param endTime 结束时间
	 * @return
	 */
	public List<Map<String, Object>> getOrgWorkOrderCountReportByOrgId(long orgId,String beginTime,String endTime);
	
	
}
