package com.iscreate.op.service.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.report.CommentReportDao;
import com.iscreate.op.pojo.report.ReportComment;
import com.iscreate.op.pojo.report.ReportMessageMonth;


/**
 * 报表评论
 */
public class CommentReportServiceImpl implements CommentReportService{
	
	
	
	
	public CommentReportDao commentReportDao;
	
	
	
	public CommentReportDao getCommentReportDao() {
		return commentReportDao;
	}



	public void setCommentReportDao(CommentReportDao commentReportDao) {
		this.commentReportDao = commentReportDao;
	}


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
	public List<Map<String, Object>> getCommentReport(String userId,String indicators,String dimension,long organizationId, String statisticaltime){
		long reportId = 0;
		
		Map<String, Object> reportMessage = commentReportDao.getReportMessage(indicators, dimension, organizationId, statisticaltime);
		if(reportMessage == null || reportMessage.isEmpty()){
			ReportMessageMonth reportMessageMonth = new ReportMessageMonth();
			reportMessageMonth.setIndicators(indicators);
			reportMessageMonth.setDimension(dimension);
			reportMessageMonth.setOrganizationId(organizationId);
			Date statisticalDate = null;
			if(statisticaltime != null && !"null".equals(statisticaltime) && !"".equals(statisticaltime)){
				try {
					statisticalDate = new SimpleDateFormat("yyyy-MM-dd").parse(statisticaltime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//applicationEntity.setValue("statisticaltime", statisticalDate);
			reportMessageMonth.setStatisticaltime(statisticalDate);
			reportId = this.commentReportDao.saveReportMessageMonth(reportMessageMonth);
			//Map<String, Object> reportMessage2 = commentReportDao.getReportMessage(indicators, dimension, organizationId, statisticaltime);
//			if(reportMessage2 != null && !reportMessage2.isEmpty()){
//				reportId = Long.parseLong(reportMessage2.get("id")+"");
//			}
		}else{
			reportId = Long.parseLong(reportMessage.get("id")+"");
		}
		List<Map<String,Object>> reportComment = commentReportDao.getReportComment(reportId);
		return reportComment;
	}
	
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
	public long saveCommentReport(long reportId,String userId,String userName,String commentText){
		ReportComment reportComment = new ReportComment();
		reportComment.setContent(commentText);
		reportComment.setCriticsid(userId);
		reportComment.setCriticsname(userName);
		reportComment.setReportmessageid(reportId);
		reportComment.setCriticstime(new Date());
		return commentReportDao.saveReportComment(reportComment);
	}
	
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
	public long getReportMessage(String indicators,String dimension,long organizationId, String statisticaltime){
		long returnLong = 0;
		Map<String, Object> reportMessage = commentReportDao.getReportMessage(indicators, dimension, organizationId, statisticaltime);
		if(reportMessage != null && !reportMessage.isEmpty()){
			returnLong = Long.valueOf(reportMessage.get("id").toString());
		}
		return returnLong;
	}
}
