package com.iscreate.op.service.cms;

import java.util.List;
import java.util.Map;

public interface CmsInfoProviderService {
	/**
	 * 获取用户能看的所有的时间有效的公告的数量
	* @date Nov 13, 20129:42:38 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public int getAllValidAnnouncementsCount(String userId);


	
	
	/**
	 * 获取公告指定分页参数的记录
	* @date Nov 13, 20129:50:04 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRanageValidAnnouncements(String userId,
			int start, int length, boolean timeAsc);
	
	/**
	 * 获取前面N条记录（最新发布的在前）
	* @date Nov 13, 20129:50:51 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param length
	* @param @param isAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getTopNValidAnnouncements(String userId,
			int length, boolean isAsc);
	
	/**
	 * 获取公告的详情
	* @date Nov 13, 20129:59:43 AM
	* @Description: TODO 
	* @param @param infoid
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getAnnouncementDetailById(long infoid,String userId);
	
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
	public List<Map<String, Object>> getCmsReportProjectAppraisals(String userId,
			int start, int length, boolean timeAsc,int releaseScopeType);
}
