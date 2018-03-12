package com.iscreate.op.service.rno.job.server;

import java.util.List;

import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.JobStatus;

public class JobProgressData {
	private List<JobStatus> runningJobs;// 正在运行的job。包括正在killing的也算
	private List<JobStatus> finishedJobs;// 已经完成的job
    private JobNodeId jobNodeId;
    
	public List<JobStatus> getRunningJobs() {
		return runningJobs;
	}

	public void setRunningJobs(List<JobStatus> runningJobs) {
		this.runningJobs = runningJobs;
	}

	public List<JobStatus> getFinishedJobs() {
		return finishedJobs;
	}

	public void setFinishedJobs(List<JobStatus> finishedJobs) {
		this.finishedJobs = finishedJobs;
	}


	public JobNodeId getJobNodeId() {
		return jobNodeId;
	}

	public void setJobNodeId(JobNodeId jobNodeId) {
		this.jobNodeId = jobNodeId;
	}

}
