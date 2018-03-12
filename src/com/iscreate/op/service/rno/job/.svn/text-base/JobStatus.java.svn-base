package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.common.JobState;

public class JobStatus implements Writable {

	private long jobId;
	private JobState jobState=JobState.Initiate;
	private Date updateTime;
	private String progress;

	public JobStatus() {

	}

	public JobStatus(long jobId) {
		this.jobId = jobId;
	}

	public JobStatus(JobStatus jobStatus) {
		if (jobStatus != null) {
			this.jobId = jobStatus.jobId;
			this.jobState = jobStatus.jobState;
			this.updateTime = jobStatus.updateTime;
			this.progress=jobStatus.progress;
		}
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public JobState getJobState() {
		return jobState;
	}

	public void setJobState(JobState jobState) {
		this.jobState = jobState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "JobStatus [jobId=" + jobId + ", jobRunningStatus="
				+ jobState + ", updateTime=" + updateTime + "]";
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.jobId = arg0.readLong();
		jobState = JobState.getByCode(WritableUtils.readString(arg0));
		long t = arg0.readLong();
		if (t == -1) {
			updateTime = null;
		} else {
			updateTime = new Date(t);
		}
		progress=WritableUtils.readString(arg0);

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeLong(jobId);
		WritableUtils.writeString(arg0,jobState.getCode());
        if(updateTime==null){
        	arg0.writeLong(-1);
        }else{
        	arg0.writeLong(updateTime.getTime());
        }
        WritableUtils.writeString(arg0,progress);
	}

	/**
	 * 用其他数据更新本jobstatus的信息
	 * @param js
	 */
	public void update(JobStatus js) {
		this.jobState=js.jobState;
		this.progress=js.getProgress();
		this.updateTime=new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (jobId ^ (jobId >>> 32));
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
		JobStatus other = (JobStatus) obj;
		if (jobId != other.jobId)
			return false;
		return true;
	}

}
