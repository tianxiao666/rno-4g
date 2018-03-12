package com.iscreate.op.service.report;


public interface FaultBaseStationTopTenReportService {

	
	/**
	 * 根据用户当前所在组织，获取前10基站故障数的数据
	 * @param userId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenData(String userId,String beginTime,String endTime);
	
	
	
	/**
	 * 根据用户所选组织的，获取前10基站故障数的数据
	 * @param orgId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataByOrg(long orgId,String beginTime,String endTime);
	
	
	
	/**
	 * 按项目获取前10基站故障数的数据
	 * @param userId 
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataForProject(String userId,String beginTime,String endTime);
	
	
	
	/**
	 * 根据用户所选项目的，获取前10基站故障数的数据
	 * @param orgId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataByProject(long projectId,String beginTime,String endTime);
	
	
	
	
	
	/**
	 * 获取项目列表
	 * @param userId
	 * @return
	 */
	public String getProjectTreeData(String userId);
	
}
