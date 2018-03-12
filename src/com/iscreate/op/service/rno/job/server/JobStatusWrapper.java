package com.iscreate.op.service.rno.job.server;

import com.iscreate.op.service.rno.job.JobStatus;

@Deprecated
public class JobStatusWrapper {

	boolean isDirty;
	long dirtyTime;
	JobStatus jobStatus;
	
	
	public JobStatusWrapper(){
		
	}
	
	public JobStatusWrapper(boolean isDirty,JobStatus jobStatus){
		this.isDirty=isDirty;
		this.jobStatus=jobStatus;
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	public JobStatus getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public long getDirtyTime() {
		return dirtyTime;
	}

	public void setDirtyTime(long dirtyTime) {
		this.dirtyTime = dirtyTime;
	}
	
	
}
