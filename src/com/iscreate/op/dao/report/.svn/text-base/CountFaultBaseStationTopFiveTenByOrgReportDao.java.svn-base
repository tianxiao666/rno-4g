package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysOrg;

public interface CountFaultBaseStationTopFiveTenByOrgReportDao {

	
	/**
	 * 获取基站故障数最差分布的数据
	 * @param allSubOrgWithChildList 当前组织及其所有的子组织集合
	 * @param targetAllSubList 目标的组织的及其下级组织
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenData(List<SysOrg> allSubOrgWithChildList,List<Map<String,Object>> targetAllSubList,String beginTime,String endTime);
	
	
	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param allSubAreaWithChildList
	 * @param targetAllSubAreaIdList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForArea(List<String> allSubAreaWithChildList,List<String> targetAllSubAreaIdList,String beginTime,String endTime);
	
	
	
	
	/**
	 * 按项目获取基站故障数最差分布的数据
	 * @param allSubOrgWithChildList 当前组织及其所有的子组织集合
	 * @param targetOrgList 目标的组织集合
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForProject(List<String> allProjectIdList,String targetProjectId,String beginTime,String endTime);
	
	
	
	
	/**
	 * 获取基站故障数最差分布的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenData(List<SysOrg> orgList,String beginTime,String endTime);
	
	
	
	/**
	 * 获取基站故障数最差分布的环比数据
	 * @param orgList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenLoopCompareData(List<SysOrg> orgList,String beginTime,String endTime);
	
	
	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getCountFaultBaseStationTopFiveTenDataForArea(List<String> areaIdList,String beginTime,String endTime);
	
	
	/**
	 * 按地域获取基站故障数最差分布的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@Deprecated
	public List<Map<String,Object>> getCountFaultBaseStationTopFiveTenLoopCompareDataForArea(List<String> areaIdList,String beginTime,String endTime);
	
	
}
