package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;



public interface AnalyseFaultCountAndResourceBalanceReportDao {

	
	/**
	 * 根据组织id集合获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceData(List<Map<String,Object>> orgList,String beginTime,String endTime);
	
	
	/**
	 * 根据开始时间、结束时间，获取对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceLoopCompareData(String beginTime,String endTime);
	
	
	
	
	/**
	 * 获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgList
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public List<Map<String, Object>> getAnalyseFaultCountAndResourceBalanceDataForProject(List<String> orgList,String beginTime,String endTime);
	
	
	/**
	 * 按项目获取每百个基站的人/车/任务量统计分析的环比数据
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceLoopCompareDataForProject(String beginTime,String endTime);
	
}
