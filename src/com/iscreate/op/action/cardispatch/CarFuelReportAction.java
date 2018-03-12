package com.iscreate.op.action.cardispatch;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.cardispatch.CarFuelReportService;
import com.iscreate.op.service.cardispatch.CardispatchExportExcelService;
import com.opensymphony.xwork2.ActionContext;

public class CarFuelReportAction {
	private final Log log = LogFactory.getLog(this.getClass());
	private String orgId;
	private String orgName;
	private String dateString;
	
	private int currentPage;
	private int pageSize=10;
	private int totalPage;
	
	private Map<String,Object> resultMap;
	
	private CarFuelReportService carFuelReportService;
	
	InputStream excelStream;
    String filename;
	/**
	 * 
	 * @description: 根据组织id 时间月份 获取车辆油费等相关统计信息
	 * @author：yuan.yw
	 * @return     
	 * @return String     
	 * @date：Aug 2, 2013 2:11:04 PM
	 */
	public String getCarFuelBillsAction(){
		log.info("进入getCarFuelBillsAction()，根据组织id 时间月份 获取车辆油费等相关统计信息");
		log.info("参数orgId="+orgId+",currentPage="+currentPage+",dateString="+dateString);
		if(currentPage>totalPage){
			currentPage=totalPage;
		}
		Map<String,Object> result = this.carFuelReportService.getCarFuelInfoForPage(orgId, currentPage, pageSize, dateString);
		if(result!=null && !result.isEmpty()){
			int count = Integer.parseInt(result.get("count")+"");//总数
			if(count%this.pageSize==0){
				totalPage = count/this.pageSize;//总页数
			}else{
				totalPage = count/this.pageSize+1;
			}
			this.resultMap = (Map<String,Object>)result.get("resultMap");
		}
		log.info("退出getCarFuelBillsAction()，返回结果resultMap:"+resultMap);
		return "success";
	}
	/**
	 * 
	 * @description: 导出油费报表
	 * @author：yuan.yw
	 * @return     
	 * @return String     
	 * @date：Aug 2, 2013 5:36:33 PM
	 */
	public String exportCarFuelBillsAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GBK");	
		excelStream =this.carFuelReportService.exportCarFuelBillsService(orgId,orgName,dateString);
        String downFileName;
		try {
			downFileName = new String((orgName+"车辆油费统计").getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			downFileName="carfuelreport";
		}
		this.filename=dateString+downFileName+".xls";      
		return "success";
	}
	
	public InputStream getExcelStream() {
		return excelStream;
	}
	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	///
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public CarFuelReportService getCarFuelReportService() {
		return carFuelReportService;
	}
	public void setCarFuelReportService(CarFuelReportService carFuelReportService) {
		this.carFuelReportService = carFuelReportService;
	}
	public Map<String, Object> getResultMap() {
		return resultMap;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
}
