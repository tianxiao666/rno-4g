package com.iscreate.op.action.networkresourcemanage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FileDownloadAction {
	
	//文件路径
	private String downloadPath;
	
	private String fileName;//文件名

	private String fileChineseName;//文件中文名
	
	private String downloadType;//下载文件是否为导入模板
	
	private static final Log log = LogFactory.getLog(FileDownloadAction.class);
	
	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

	public String getFileChineseName(){
		return fileChineseName;
	}

	public void setFileChineseName(String fileChineseName) {
		this.fileChineseName = fileChineseName;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public InputStream getDownloadFile() throws FileNotFoundException, UnsupportedEncodingException {
		log.info("进入getDownloadFile()，取得下载的文件流。");
		String path = ServletActionContext.getServletContext().getRealPath("");
		if("importModule".equals(this.downloadType)){
			this.downloadPath = "/importmodulefile/";
			this.fileChineseName = this.fileName;
		}
		this.fileName = new String(this.fileName.getBytes("ISO-8859-1"),"utf-8");
		path = path +this.downloadPath;
		log.info("退出getDownloadFile()方法,文件路径path："+path);
		return new FileInputStream(
				path + fileName);
	}
/**
 * 
 * @description: 判断文件是否存在
 * @author：     
 * @return void     
 * @throws IOException 
 * @throws IOException 
 * @date：Jun 8, 2012 5:30:04 PM
 */
	public void hasDownLoadFileAction() throws IOException{
		log.info("进入hasDownLoadFileAction()，判断下载的文件是否存在。");
		String result="";
		String path = ServletActionContext.getServletContext().getRealPath("");
		path = path +this.downloadPath;
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(
					path + fileName);
			if(inputStream!=null){
				result="success";
				log.info("下载的文件存在。返回结果：result="+result);
			}
			Gson gson = new GsonBuilder().create();
			result=gson.toJson(result);
		} catch (Exception e) {
			result="";
			log.error("下载的文件不存在。返回结果：result="+result);
			e.printStackTrace();
		}
		log.info("退出hasDownLoadFileAction()，返回结果：result="+result);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().write(result);
	}
/**
 * 
 * @description: 下载资源
 * @author：
 * @return     
 * @return String     
 * @date：2012-6-4 下午05:20:24
 */
	public String downLoadFileAction(){
		log.info("进入downLoadFileAction()，下载文件。");
		try {
			log.info("退出downLoadFileAction()，下载成功。");
			return "success";	
		} catch (Exception e) {
			log.error("退出hasDownLoadFileAction()，下载失败，可能目的路径中文件不存在。");
			return "error";
		} 
			
	}

}
