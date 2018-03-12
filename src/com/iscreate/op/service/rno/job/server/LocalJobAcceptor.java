package com.iscreate.op.service.rno.job.server;

import java.util.List;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public class LocalJobAcceptor implements JobAcceptor {

	JobManager jobManager;
	
	public void setJobManager(JobManager jobManager) {
		this.jobManager = jobManager;
	}

	@Override
	public JobStatus recvJob(JobProfile job) {
		return jobManager.addJob(job);
	}

	
	@Override
	public <T> T recvJob(JobProfile job,JobAddCallback<T> listener) {
		return jobManager.addJob(job,listener);
	}

	
//	@Override
//	public JobStatus updateJobStatus(JobStatus jobStatus) {
//		return jobManager.updateJobStatus(jobStatus);
//	}

//	@Override
//	public void addJobReport(JobReport report) {
//		jobManager.addJobReport(report);
//	}

	@Override
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,
			List<JobReport> reports, List<JobStatus> updateJobStatusList) {
		return jobManager.heartbeat(jobWorkerStatus, reports, updateJobStatusList);
	}

	@Override
	public JobStatus killJob(JobProfile job,String account,String reason) {
		return jobManager.killJob(job,account,reason);
	}

}
