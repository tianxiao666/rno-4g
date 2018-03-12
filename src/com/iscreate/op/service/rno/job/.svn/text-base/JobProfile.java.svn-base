package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.common.JobFramework;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.tool.DbValInject;

public class JobProfile implements Writable{

	@DbValInject(type="Long",dbField="JOB_ID")
	private Long jobId;
	
	@DbValInject(type="Date",dbField="CREATE_TIME")
	private Date submitTime=null;

	@DbValInject(type="Date",dbField="LAUNCH_TIME")
	private Date launchTime=null;
	
	@DbValInject(type="Date",dbField="COMPLETE_TIME")
	private Date finishTime=null;
	
	@DbValInject(type="Integer",dbField="PRIORITY")
	private Integer priority=1;
	
	@DbValInject(type="String",dbField="JOB_TYPE")
	private String jobType;
	
	@DbValInject(type="String",dbField="CREATOR")
	private String account;
	
	@DbValInject(type="String",dbField="JOB_NAME")
	private String jobName;
	
	@DbValInject(type="String",dbField="JOB_RUNNING_STATUS")
	private String jobStateStr;
	
	@DbValInject(type="String",dbField="DESCRIPTION")
	private String description;
	
	//job的类型，是本地，还是haodoop或者是其他
	private JobFramework jobFramework=JobFramework.Local;
	
	private JobStatus jobStatus=new JobStatus();
	
	
	public JobProfile(){}
	
	
	
	public JobProfile(Long jobId){
		this.jobId=jobId;
		this.jobStatus.setJobId(jobId);
	}
	
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
		jobStatus.setJobId(jobId);
	}
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public Date getLaunchTime() {
		return launchTime;
	}
	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobStateStr() {
		return jobStateStr;
	}
	public void setJobStateStr(String jobStateStr) {
		this.jobStateStr = jobStateStr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}
	

	/**
	 * 修改job状态
	 * @param state
	 * @param progress
	 */
	public void modifyJobState(JobState state,String progress){
		if(state==null){
			return;
		}
		jobStatus.setJobState(state);
		jobStatus.setProgress(progress);
		jobStatus.setUpdateTime(new Date());
		
		jobStateStr=state.getCode();
	}
	
	@Override
	public String toString() {
		return "JobProfile [jobId=" + jobId + ", submitTime=" + submitTime
				+ ", launchTime=" + launchTime + ", finishTime=" + finishTime
				+ ", priority=" + priority + ", jobType=" + jobType
				+ ", account=" + account + ", jobName=" + jobName
				+ ", jobRunningStatus=" + jobStateStr + ", description="
				+ description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobProfile other = (JobProfile) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		return true;
	}

	

	@Override
	public void readFields(DataInput arg0) throws IOException {
		long jid=arg0.readLong();
		if(jid==-1){
			this.jobId=null;
		}else{
			this.jobId=jid;
		}
		long t=arg0.readLong();
		if(t==-1){
			this.submitTime=null;
		}else{
			this.submitTime=new Date(t);
		}
		t=arg0.readLong();
		if(t==-1){
			this.launchTime=null;
		}else{
			this.launchTime=new Date(t);
		}
		t=arg0.readLong();
		if(t==-1){
			this.finishTime=null;
		}else{
			this.finishTime=new Date(t);
		}
//		
//		
		int pri=arg0.readInt();
		if(pri==-1){
			this.priority=null;
		}else{
			this.priority=pri;
		}
		this.jobType=WritableUtils.readString(arg0);
		this.account=WritableUtils.readString(arg0);
		this.jobName=WritableUtils.readString(arg0);
		this.jobStateStr=WritableUtils.readString(arg0);
		this.description=WritableUtils.readString(arg0);
		jobStatus=new JobStatus();
		jobStatus.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		if(jobId==null){
			arg0.writeLong(-1);
		}else{
			arg0.writeLong(jobId.longValue());
		}
		if(submitTime==null){
			arg0.writeLong(-1);
		}else{
			arg0.writeLong(submitTime.getTime());
		}
		if(launchTime==null){
			arg0.writeLong(-1);
		}else{
			arg0.writeLong(launchTime.getTime());
		}
		if(finishTime==null){
			arg0.writeLong(-1);
		}else{
			arg0.writeLong(finishTime.getTime());
		}
		if(priority==null){
			arg0.writeInt(-1);
		}else{
			arg0.writeInt(priority);
		}
		WritableUtils.writeString(arg0, jobType);
		WritableUtils.writeString(arg0, account);
		WritableUtils.writeString(arg0, jobName);
		WritableUtils.writeString(arg0, jobStateStr);
		WritableUtils.writeString(arg0, description);

		jobStatus.write(arg0);
	}

	
	
	public static void main(String[] args) throws IOException {
		JobProfile job1=new JobProfile(1L);
		
		FileOutputStream fos=new FileOutputStream(new File("d:/tmp/test.t"));
		DataOutputStream dos=new DataOutputStream(fos);
		job1.write(dos);
		dos.close();
		
		FileInputStream fis=new FileInputStream(new File("d:/tmp/test.t"));
		DataInputStream dis=new DataInputStream(fis);
		job1.readFields(dis);
		System.out.println(job1);
	}
}
