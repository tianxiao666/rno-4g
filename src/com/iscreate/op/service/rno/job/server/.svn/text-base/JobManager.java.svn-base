package com.iscreate.op.service.rno.job.server;

import java.util.List;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public interface JobManager {

	public JobStatus addJob(JobProfile job);
	
	public <T> T addJob(JobProfile job,JobAddCallback<T> listener);
	
	public JobStatus killJob(JobProfile job,String acccount,String reason);
	
//	public JobStatus updateJobStatus(JobStatus jobStatus);
	
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,List<JobReport> reports,List<JobStatus> updateJobStatusList);
	
//	public void addJobReport(JobReport report);
	
	public JobWorkerStatus register(JobWorkerStatus client);
}
