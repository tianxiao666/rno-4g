package com.iscreate.op.pojo.rno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iscreate.op.service.rno.tool.DbValInject;

/**
 * 结构分析任务的数据
 * 
 * @author brightming
 * 
 */
public class RnoStructAnaJobRec {

	@DbValInject(dbField = "JOB_ID", type = "Long")
	private Long jobId;
	@DbValInject(dbField = "BEG_MEA_TIME", type = "Date")
	private Date begMeaTime;
	@DbValInject(dbField = "END_MEA_TIME", type = "Date")
	private Date endMeaTime;
	@DbValInject(dbField = "CITY_ID", type = "Long")
	private Long cityId;
	@DbValInject(dbField = "DL_FILE_NAME", type = "String")
	private String downLoadFileName;
	@DbValInject(dbField = "RD_FILE_NAME", type = "String")
	private String readByProgFileName;
	@DbValInject(dbField = "RESULT_DIR", type = "String")
	private String resultDir;
	@DbValInject(dbField = "FINISH_STATE", type = "String")
	private String finishState;
	@DbValInject(dbField = "STATUS", type = "String")
	private String status;

	//
	String cityName;
	String tmpDir;
	List<String> workResultFiles = new ArrayList<String>();

	public void addWorkResultFiles(String fileName) {
		workResultFiles.add(fileName);
	}

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<String> getWorkResultFiles() {
		return workResultFiles;
	}

	public void setWorkResultFiles(List<String> workResultFiles) {
		this.workResultFiles = workResultFiles;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Date getBegMeaTime() {
		return begMeaTime;
	}

	public void setBegMeaTime(Date begMeaTime) {
		this.begMeaTime = begMeaTime;
	}

	public Date getEndMeaTime() {
		return endMeaTime;
	}

	public void setEndMeaTime(Date endMeaTime) {
		this.endMeaTime = endMeaTime;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getDownLoadFileName() {
		return downLoadFileName;
	}

	public void setDownLoadFileName(String downLoadFileName) {
		this.downLoadFileName = downLoadFileName;
	}

	public String getReadByProgFileName() {
		return readByProgFileName;
	}

	public void setReadByProgFileName(String readByProgFileName) {
		this.readByProgFileName = readByProgFileName;
	}

	public String getResultDir() {
		return resultDir;
	}

	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}

	public String getFinishState() {
		return finishState;
	}

	public void setFinishState(String finishState) {
		this.finishState = finishState;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
