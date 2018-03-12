package com.iscreate.op.action.informationmanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ActionUtil;


import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.tools.FileHelper;
import com.iscreate.plat.tools.IdGenerator;

public class InformationManageForeignAction {
	
	/************* 依赖注入 *************/

	private SysOrganizationService sysOrganizationService;
	
	
	
	/********* 属性 *********/
	private Log log = LogFactory.getLog(this.getClass());
	private File[] file;
	private String fileNameString;
	private String[] fileFileName;
	private String[] fileContentType;
	private String fileUrl;
	
	/**************** action ******************/
	
	/**
	 * 根据组织架构id,查找上级组织
	 * 
	 */
/*	public void findServerUpOrgListByOrgId () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("orgId");
		Long orgId = Long.valueOf(requestParamMap.get("orgId"));
		
		//TODO　change the interface
		List<ProviderOrganization> orgListUpwardByOrg = providerOrganizationService.getOrgListUpwardByOrg(orgId);
		try {
			ActionUtil.responseWrite(orgListUpwardByOrg , false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 根据组织架构id,查找上级组织  (xiao.ll 已改 废弃 使用findTopOrgLstByOrgId())
	 * 
	 */
/*	public void findClientUpOrgListByOrgId () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("orgId");
		Long orgId = Long.valueOf(requestParamMap.get("orgId"));
		
		//TODO　change the interface
		List<CustomerOrganization> orgListUpwardByOrg = customerOrganizationService.getCustomerOrgUpwardByOrgIdService(orgId);
		try {
			ActionUtil.responseWrite(orgListUpwardByOrg , false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}*/
	/**
	 * 获取上级组织树
	 */
	public void findTopOrgLstByOrgId(){
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("orgId");
		Long orgId = Long.valueOf(requestParamMap.get("orgId"));
		List<Map<String,Object>> organizionlist = sysOrganizationService.getAllTheTopOrgByOrgIdService(orgId);
		try {
			ActionUtil.responseWrite(organizionlist , false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	
	/**
	 * 获取上传文件并获取上传的路径的url
	 * (请求参数) file 上传的文件
	 * (响应) (String) 文件上传的路径
	 */
	public void getFileURL () {
		String carImg = "";
		String filePath = "";
		if(this.file!=null && this.file.length>0 && this.file[0]!=null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/information";
			for (int i = 0; i < this.fileFileName.length; i++) {
				String fileFileName = this.fileFileName[i];
				String fileType = fileFileName.substring(fileFileName.lastIndexOf("."));
				this.fileFileName[i] = IdGenerator.makeUuidString()+fileType;
			}
			List<String> saveFiles = FileHelper.saveFileByPath(savePath,this.file, this.fileFileName);
			if(saveFiles!=null && saveFiles.size() == this.file.length){
				filePath = saveFiles.get(0);
				carImg=saveFiles.get(0);
				carImg=carImg.replaceAll("\\\\", "/");
				filePath = "/"+ActionUtil.getProjectName() + "/" + carImg.substring(carImg.lastIndexOf("upload/") , carImg.length());
			}
		}
		try {
			ActionUtil.responseWrite(filePath);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取上传文件并获取上传的路径的url
	 * (请求参数) file 上传的文件
	 * (响应) (String) 文件上传的路径
	 */
	public void uploadFile () {
		String carImg = "";
		String filePath = "";
		Map<String,String> result_map = new HashMap<String, String>();
		if(this.file!=null && this.file.length>0 && this.file[0]!=null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/" + fileUrl;
			for (int i = 0; i < this.fileFileName.length; i++) {
				String fileFileName = this.fileFileName[i];
				String fileType = fileFileName.substring(fileFileName.lastIndexOf("."));
				this.fileFileName[i] = IdGenerator.makeUuidString()+fileType;
			}
			List<String> saveFiles = FileHelper.saveFileByPath(savePath,this.file, this.fileFileName);
			if(saveFiles!=null && saveFiles.size() == this.file.length){
				filePath = saveFiles.get(0);
				File f = new File(filePath);
				
				carImg=saveFiles.get(0);
				carImg=carImg.replaceAll("\\\\", "/");
				filePath = "/"+ActionUtil.getProjectName() + "/" + carImg.substring(carImg.lastIndexOf("upload/") , carImg.length());
				result_map.put("filePath", filePath);
				try {
					result_map.put("fileSize", new FileInputStream( f ).available()+"" );
				} catch (FileNotFoundException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	/*********** getter setter ***********/
	
	
	
	

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String getFileNameString() {
		return fileNameString;
	}

	public void setFileNameString(String fileNameString) {
		this.fileNameString = fileNameString;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String[] getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
