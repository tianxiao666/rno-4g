package com.iscreate.op.service.report;

import java.util.List;
import java.util.Map;

public interface CommentReportService {
	/**
	 * 获取报表评论
	* @date Nov 5, 201210:36:24 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param indicators
	* @param @param dimension
	* @param @param organizationId
	* @param @param statisticaltime
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCommentReport(String userId,String indicators,String dimension,long organizationId, String statisticaltime);
	
	/**
	 * 保存报表评论
	* @date Nov 5, 201210:42:45 AM
	* @Description: TODO 
	* @param @param reportId
	* @param @param userId
	* @param @param userName
	* @param @param commentText        
	* @throws
	 */
	public long saveCommentReport(long reportId,String userId,String userName,String commentText);
	
	/**
	 * 获取报表快照
	* @date Nov 5, 20125:00:58 PM
	* @Description: TODO 
	* @param @param indicators
	* @param @param dimension
	* @param @param organizationId
	* @param @param statisticaltime
	* @param @return        
	* @throws
	 */
	public long getReportMessage(String indicators,String dimension,long organizationId, String statisticaltime);
}
