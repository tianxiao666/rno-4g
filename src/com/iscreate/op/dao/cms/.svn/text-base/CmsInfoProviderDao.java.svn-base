package com.iscreate.op.dao.cms;

import java.util.List;
import java.util.Map;

public interface CmsInfoProviderDao {
	/**
	 * 所有的时间有效的公告的数量
	* @date Nov 12, 20125:12:10 PM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCount(final String sqlIds,final String startTime,final String endTime);
	
	/**
	 * 获取发布范围类型的公告信息
	* @date Nov 12, 20125:16:56 PM
	* @Description: TODO 
	* @param @param releaseScopeType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getInforeleaseByReleaseScopeType(final int releaseScopeType);
	
	/**
	 * 所有的时间有效的公告(分页)
	* @date Nov 13, 20129:46:45 AM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @param startTime
	* @param @param endTime
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCountPaging(final String sqlIds,final String startTime,final String endTime,final int start,final int length,final boolean timeAsc);
	
	/**
	 * 用户能否查看infoid的公告内容
	* @date Nov 12, 20125:23:07 PM
	* @Description: TODO 
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> allowUserToViewTheAnnouncement(final String startTime,final String endTime);
	
	/**
	 * 根据消息ID获取信息详细内容
	* @date Nov 13, 20129:54:39 AM
	* @Description: TODO 
	* @param @param infoid
	* @param @return        
	* @throws
	 */
	public  List<Map<String, Object>> getAnnouncementDetailById(final long infoid);
	
	/**
	 * 所有的时间有效的公告
	* @date Nov 13, 20129:46:45 AM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @param startTime
	* @param @param endTime
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCount(final String sqlIds,final String startTime,final String endTime,final boolean timeAsc);
	
	/**
	 * 获取项目考核的记录
	* @date Dec 18, 201211:42:57 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @param releaseScopeType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsReportProjectAppraisal(final String sqlIds,final String startTime,final String endTime,final int releaseScopeType,final boolean timeAsc);
}
