package com.iscreate.op.service.report;

import java.util.List;

public interface CountFaultBaseStationTopFiveTenByOrgReportService {

	
	/**
	 * 获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserId(String userId,String beginTime,String endTime);
	
	
	
	/**
	 * 获取下级组织对应的基站故障数最差分布的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataBySubOrgId(Long orgId,String beginTime,String endTime);
	
	
	
	/**
	 * 获取组织对应的基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserId(String userId,List<String> yearMonthList);

	
	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserIdForArea(String userId,String beginTime,String endTime);
	
	
	/**
	 * 按地域获取下级区域对应的基站故障数最差分布的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataBySubAreaIdForArea(Long areaId,String beginTime,String endTime);
	
	
	/**
	 * 按地域获取区域对应的基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserIdForArea(String userId,List<String> yearMonthList);
	
	
	
	
	/**
	 * 按项目获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserIdForProject(String userId,String beginTime,String endTime);
	
	
	/**
	 * 按项目获取基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByOrgIdForProject(String userId,List<String> yearMonthList);
	
	
	
	/**
	 * 获取区域对象
	 * @param areaId
	 * @return
	 */
	public String getAreaInfoByAreaId(long areaId);
	
	
	/**
	 * 获取父级区域
	 * @param orgId
	 * @return
	 */
	public String getParentArea(long areaId);
	
}
