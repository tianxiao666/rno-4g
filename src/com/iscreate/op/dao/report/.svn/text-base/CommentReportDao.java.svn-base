package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.report.ReportComment;
import com.iscreate.op.pojo.report.ReportMessageMonth;

public interface CommentReportDao {
	/**
	 * 获取报表评论快照
	* @date Nov 5, 20129:50:23 AM
	* @Description: TODO 
	* @param @param indicators
	* @param @param dimension
	* @param @param bizunitinnstId
	* @param @param statisticaltime
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getReportMessage(final String indicators,final String dimension,final long bizunitinnstId,final String statisticaltime);
	
	
	/**
	 * 保存报表评论
	* @date Nov 5, 201210:10:30 AM
	* @Description: TODO 
	* @param @param org
	* @param @return        
	* @throws
	 */
	public Long saveReportComment(ReportComment reportComment);
	
	
	/**
	 * 保存报表快照
	* @date Nov 5, 201210:10:30 AM
	* @Description: TODO 
	* @param @param org
	* @param @return        
	* @throws
	 */
	public Long saveReportMessageMonth(ReportMessageMonth reportMessageMonth);
	
	/**
	 * 获取指定报表的评论
	 * @param reportmessageid
	 * @return
	 */
	public List<Map<String, Object>> getReportComment(final long reportmessageid);
}
