package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

import com.iscreate.op.service.rno.tool.DbValInject;

public class JobReport implements Writable {

	@DbValInject(type="Long",dbField="JOB_ID")
	private long jobId;
	
	@DbValInject(type="String",dbField="STAGE")
	private String stage="";
	
	@DbValInject(type="Date",dbField="BEG_TIME")
	private Date begTime;
	
	@DbValInject(type="Date",dbField="END_TIME")
	private Date endTime;
	
	@DbValInject(type="String",dbField="STATE")
	private String finishState="";
	
	@DbValInject(type="String",dbField="ATT_MSG")
	private String attMsg="";
	
	@DbValInject(type="Integer",dbField="REPORT_TYPE")
	private Integer reportType=1;
	public JobReport(){}
	
	public JobReport(long jobId){
		this.jobId=jobId;
	}
	
	
	
	public JobReport(JobReport jobReport) {
		if(jobReport!=null){
			this.attMsg=jobReport.getAttMsg();
			this.begTime=jobReport.getBegTime();
			this.endTime=jobReport.getEndTime();
			this.finishState=jobReport.getFinishState();
			this.jobId=jobReport.getJobId();
			this.stage=jobReport.getStage();
			this.reportType=jobReport.getReportType();
		}
	}

	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public Date getBegTime() {
		return begTime;
	}
	public void setBegTime(Date begTime) {
		this.begTime = begTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getFinishState() {
		return finishState;
	}
	public void setFinishState(String finishState) {
		this.finishState = finishState;
	}
	public String getAttMsg() {
		return attMsg;
	}
	public void setAttMsg(String attMsg) {
		this.attMsg = attMsg;
	}
	
	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public void setFields(String stage,Date begTime,Date endTime,String finishState,String attMsg){
		this.stage=stage;
		this.begTime=begTime;
		this.endTime=endTime;
		this.finishState=finishState;
		this.attMsg=attMsg;
		this.reportType=1;//业务类型
	}
	public void setSystemFields(String stage,Date begTime,Date endTime,String finishState,String attMsg){
		this.stage=stage;
		this.begTime=begTime;
		this.endTime=endTime;
		this.finishState=finishState;
		this.attMsg=attMsg;
		this.reportType=2;//系统类型
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.jobId=arg0.readLong();
		this.stage=arg0.readUTF();
		long t=arg0.readLong();
		if(t==-1){
			this.begTime=new Date(t);
		}
		t=arg0.readLong();
		if(t==-1){
			this.endTime=new Date(t);
		}
		this.finishState=arg0.readUTF();
		this.attMsg=arg0.readUTF();
		this.reportType=arg0.readInt();
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		
		arg0.writeLong(jobId);
		arg0.writeUTF(stage);
		arg0.writeLong(begTime==null?-1:begTime.getTime());
		arg0.writeLong(endTime==null?-1:endTime.getTime());
		arg0.writeUTF(finishState);
		arg0.writeUTF(attMsg);
		arg0.writeInt(reportType);
	}
}
