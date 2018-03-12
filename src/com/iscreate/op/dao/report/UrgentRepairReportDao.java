package com.iscreate.op.dao.report;

import java.util.List;
import java.util.Map;

/**
 * 统计故障想修报表
 *
 */
public interface UrgentRepairReportDao {
	/**
	 *  获取故障抢修指定时间段的报表数据
	* @date Nov 2, 20125:02:59 PM
	* @Description: TODO 
	* @param @param bizId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowNames
	* @param @param judges
	* @param @param rowValues
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudge(List<String>  orgIds,String beginTime,String endTime,List<String> rowNames ,List<String> judges ,List<String> rowValues );
	
	/**
	 * 获取故障抢修指定时间段的报表数据(根据资源ID与资源类型)
	* @date Jan 17, 20139:52:04 AM
	* @Description: TODO 
	* @param @param reIds
	* @param @param reType
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowNames
	* @param @param judges
	* @param @param rowValues
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByreIdsAndreType(final List<String> reIds ,final String reType ,final String beginTime,final String endTime,final List<String> rowNames ,final List<String> judges ,final List<String> rowValues );
}
