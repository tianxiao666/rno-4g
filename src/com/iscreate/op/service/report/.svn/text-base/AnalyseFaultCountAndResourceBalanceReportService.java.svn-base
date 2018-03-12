package com.iscreate.op.service.report;

import java.util.List;


public interface AnalyseFaultCountAndResourceBalanceReportService {

	
	
	/**
	 * 根据用户账号获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param userId 用户账号
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceDataByUserId(String userId,String beginTime,String endTime);
	
	
	/**
	 * 获取下级组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getSubOrgAnalyseFaultCountAndResourceBalanceDataBySubOrgId(Long orgId,String beginTime,String endTime);
	
	
	/**
	 * 根据用户账号获取组织对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByUserId(String userId,List<String> yearMonthList);

	
	
	/**
	 * 按项目获取，根据用户账号获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param userId 用户账号
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceDataByUserIdForProject(String userId,String beginTime,String endTime);
	
	
	/**
	 * 按项目获取对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param useId
	 * @param yearMonthList
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByOrgIdForProject(String userId,List<String> yearMonthList);
	
	
	
	
	
	/**
	 * 判断组织是否具有下级组织
	 * @param orgId
	 * @return
	 */
	public String judgeOrgIsExistSubOrg(Long orgId);
	
	/**
	 * 获取用户最高级组织
	 * @param userId
	 * @return
	 */
	public String getUserTopOrg(String userId);
	
	
	/**
	 * 获取父级组织
	 * @param orgId
	 * @return
	 */
	public String getParentOrg(long orgId);
	
	
	
	
	
}
