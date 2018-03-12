package com.iscreate.op.action.report;

import java.io.UnsupportedEncodingException;

/**
 * 通用
 */
public class CommonReportAction {
	
	private String reportType;
	
	
	public String getReportType() {
		return reportType;
	}


	public void setReportType(String reportType) {
		this.reportType = reportType;
	}


	//跳转报表相应指标说明
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
}
