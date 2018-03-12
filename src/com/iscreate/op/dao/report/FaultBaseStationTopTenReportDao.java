package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;



public interface FaultBaseStationTopTenReportDao {

	
	/**
	 * 根据组织id集合获取前10基站故障数的数据
	 * @param subOrgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getFaultBaseStationTopTenData(List<Map<String,Object>> subOrgList,String beginTime,String endTime);
	
	
	/**
	 * 根据项目id集合获取前10基站故障数的数据
	 * @param projectIdList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String,Object>> getFaultBaseStationTopTenDataByProjectIdList(List<String> projectIdList,String beginTime,String endTime);
	
}
