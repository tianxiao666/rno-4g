package com.iscreate.op.service.rno.job.server;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;

public interface JobListener {

	public JobProfile jobAdded(JobProfile jobProfile);
	
	public void jobKilling(JobProfile jobProfile);
	
	public void jobKilled(JobProfile jobProfile);
	
	public void jobStatusUpdated(JobStatus jobStatus);
	
	public void jobReportAdded(JobReport jobReport);
}
