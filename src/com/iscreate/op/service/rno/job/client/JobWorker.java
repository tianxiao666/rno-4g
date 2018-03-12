package com.iscreate.op.service.rno.job.client;

import java.util.List;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;

/**
 * job的执行管理接口
 * @author brightming
 *
 */
public interface JobWorker {

	public void start();
	
	public void stop();
	
	public void startJob(JobProfile jobProfile);
	
	public void stopJob(JobProfile jobProfile);
	
	public void beforeDoingJob(JobProfile jobProfile);
	
	public void afterExecutedJob(JobProfile jobProfile,JobStatus jobStatus);
	
	public void heartbeat();
	
	public void updateJobStatus(JobStatus jobStatus);
	
	public void addJobReport(JobReport jobReport);
}
