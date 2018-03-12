package com.iscreate.op.action.report;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.report.ReportComment;
import com.iscreate.op.service.report.CommentReportService;
import com.iscreate.sso.session.UserInfo;

public class CommentReportAction {
	
	private String userId;
	
	private long reportId;
	
	private String userName;
	
	private CommentReportService commentReportService;
	
	private String indicators;
	
	private String dimension;
	
	private long organizationId;
	
	private String statisticaltime;
	
	private String commentText;
	
	private String reportType;
	
	
	
	private List<Map<String, Object>> reportCommentList;
	
	
	public List<Map<String, Object>> getReportCommentList() {
		return reportCommentList;
	}

	public void setReportCommentList(List<Map<String, Object>> reportCommentList) {
		this.reportCommentList = reportCommentList;
	}

	/**
	 * 读取报表评论
	* @date Nov 5, 20129:44:25 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String getReportCommentAction(){
		// session获取账号id
		HttpServletRequest request = ServletActionContext.getRequest();

		this.userId = (String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERID);
		reportCommentList = new ArrayList<Map<String,Object>>();
		reportCommentList = commentReportService.getCommentReport(userId, indicators, dimension, organizationId, statisticaltime);
		this.reportId = commentReportService.getReportMessage(indicators, dimension, organizationId, statisticaltime);
		return "success";
	}
	
	/**
	 * 保存报表评论
	* @date Nov 5, 201210:54:36 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void saveReportCommentAction(){
		long saveCommentReport = commentReportService.saveCommentReport(reportId, userId, userName, commentText);
		String resultString = "";
		if(saveCommentReport > 0){
			resultString = "提交成功";
		}else{
			resultString = "提交失败";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultString);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转报表相应指标说明
	* @date Nov 5, 201210:54:47 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String getReportIndicatorsDescriptionAction(){
		String result = "";
		if(reportType != null && !reportType.equals("")){
			try {
				this.reportType = new String(this.reportType.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		if(this.reportType != null){
			if(!this.reportType.equals("")){
				result = "success";
			}
		}
		return result;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CommentReportService getCommentReportService() {
		return commentReportService;
	}

	public void setCommentReportService(CommentReportService commentReportService) {
		this.commentReportService = commentReportService;
	}

	public String getIndicators() {
		return indicators;
	}

	public void setIndicators(String indicators) {
		this.indicators = indicators;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getStatisticaltime() {
		return statisticaltime;
	}

	public void setStatisticaltime(String statisticaltime) {
		this.statisticaltime = statisticaltime;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}
