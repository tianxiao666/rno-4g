package com.iscreate.op.service.rno.job.server;

import java.util.List;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public interface JobScheduler {

	public void setJobManager(JobManager jm);
	/**
	 * 分配工作
	 * @param jobWorker
	 * @return
	 * @author brightming
	 * 2014-8-14 下午5:38:59
	 */
	public List<JobProfile> assignJobs(JobWorkerStatus jobWorker);
}
