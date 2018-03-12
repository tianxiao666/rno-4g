package com.iscreate.op.action.cms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.cms.CmsManageService;
import com.iscreate.plat.tools.excelhelper.ExcelService;

public class CmsExcelAction {
	private ExcelService excelService;
	private CmsManageService cmsManageService;
	
	
//	private Map<String, String> chineseMap;
//	
//	
//	
//	
//	
//	public Map<String, String> getChineseMap() {
//		chineseMap = new HashMap<String, String>();
//		chineseMap.put("id", "唯一标识");
//		chineseMap.put("projectName", "项目名称");
//		chineseMap.put("company", "公司");
//		chineseMap.put("janData", "一月");
//		chineseMap.put("febData", "二月");
//		chineseMap.put("marData", "三月");
//		chineseMap.put("aprData", "四月");
//		chineseMap.put("mayData", "五月");
//		chineseMap.put("junData", "六月");
//		chineseMap.put("julData", "七月");
//		chineseMap.put("augData", "八月");
//		chineseMap.put("sepData", "九月");
//		chineseMap.put("octData", "十月");
//		chineseMap.put("novData", "十一月");
//		chineseMap.put("decData", "十二月");
//		chineseMap.put("average", "平均");
//		chineseMap.put("ranking", "排名");
//		chineseMap.put("score", "总分");
//		chineseMap.put("year", "年份");
//		return chineseMap;
//	}
//
//
//	public void setChineseMap(Map<String, String> chineseMap) {
//		this.chineseMap = chineseMap;
//	}
//	
	//报表附件上传
	private File uploadReportAttachment;
	private String uploadReportAttachmentFileName;
	private String uploadReportAttachmentContentType;
	/**
	 * 读取excel
	* @date Dec 18, 201211:42:21 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void loadReoprtExcelAction(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<List<String>> listRows = null;
		if(this.uploadReportAttachment != null){
			listRows = this.excelService.getListRows(this.uploadReportAttachment, 0);
			if(listRows != null && !listRows.isEmpty()){
				map.put("listRows",listRows);
				if(listRows.get(1) != null){
					List<String> list = listRows.get(1);
					String year = list.get(0);
					String projectName = list.get(1);
					List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageService.getCmsPeportProjectAppraisal(projectName, year);
					if(cmsPeportProjectAppraisal != null && !cmsPeportProjectAppraisal.isEmpty()){
						map.put("cmsPeportProjectAppraisal", cmsPeportProjectAppraisal);
						map.put("isThere", 1);
					}
				}
			}
			//Map<String, String> cMap = ;
			//map.put("chineseMap", getChineseMap());
		}
//		List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageService.getCmsPeportProjectAppraisal("广州基站维护", "2012年");
//		if(cmsPeportProjectAppraisal != null && !cmsPeportProjectAppraisal.isEmpty()){
//			map.put("cmsPeportProjectAppraisal", cmsPeportProjectAppraisal);
//			map.put("isThere", 1);
//		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(map);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	public ExcelService getExcelService() {
		return excelService;
	}
	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}
	public File getUploadReportAttachment() {
		return uploadReportAttachment;
	}
	public void setUploadReportAttachment(File uploadReportAttachment) {
		this.uploadReportAttachment = uploadReportAttachment;
	}
	public String getUploadReportAttachmentFileName() {
		return uploadReportAttachmentFileName;
	}
	public void setUploadReportAttachmentFileName(
			String uploadReportAttachmentFileName) {
		this.uploadReportAttachmentFileName = uploadReportAttachmentFileName;
	}
	public String getUploadReportAttachmentContentType() {
		return uploadReportAttachmentContentType;
	}
	public void setUploadReportAttachmentContentType(
			String uploadReportAttachmentContentType) {
		this.uploadReportAttachmentContentType = uploadReportAttachmentContentType;
	}
	public CmsManageService getCmsManageService() {
		return cmsManageService;
	}
	public void setCmsManageService(CmsManageService cmsManageService) {
		this.cmsManageService = cmsManageService;
	}
}
