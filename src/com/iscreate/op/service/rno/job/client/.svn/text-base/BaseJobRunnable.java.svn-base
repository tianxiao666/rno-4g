package com.iscreate.op.service.rno.job.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.server.impl.JobRunningContext;

public abstract class BaseJobRunnable implements JobRunnable {

	private static Log log = LogFactory.getLog(BaseJobRunnable.class);
	private String jobType = "_base_job_";
	private JobProfile job;
	private JobRunningContext context;
	private boolean stop = false;// 是否停止

	
	public BaseJobRunnable(){
		this("_base_job_");
	}
	
	public BaseJobRunnable(String jobType) {
		this.jobType = jobType;
	}

	/**
	 * 由jobworker在得到这个runnable的时候进行注入
	 * 
	 * @param jobWorker
	 * @author brightming 2014-8-18 上午10:58:39
	 */

	public void setJobRunningContext(JobRunningContext context) {
		this.context = context;
	}

	public void stop() {
		this.stop = true;
	}

	public boolean isStop() {
		return stop;
	}

	@Override
	public final void run() {
		context.getWorkerNode().beforeDoingJob(job);
		JobStatus status=job.getJobStatus();
		try{
			status = runJob(job);
			if (status != null) {
				job.modifyJobState(status.getJobState(), status.getProgress());
			}
		}catch(InterruptedException e){
			log.warn("线程被外部强行终止！");
			job.modifyJobState(JobState.Killed, "强行终止");
		}finally{
			releaseRes();
			status=job.getJobStatus();
		}
		
		context.getWorkerNode().afterExecutedJob(job);
		
	}

	protected void setContext(JobRunningContext context) {
		this.context = context;
	}

	@Override
	public void setJob(JobProfile job) {
		this.job = job;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public JobProfile getJob() {
		return job;
	}

	@Override
	public abstract JobStatus runJob(JobProfile job) throws InterruptedException;

	public boolean isMyJob(JobProfile job) {
		if (job == null) {
			return false;
		}
		log.debug("my jobtype=" + jobType + ",job's type=" + job.getJobType());
		return jobType.equals(job.getJobType());
	}

	@Override
	public void updateProgress(JobStatus jobStatus) {
		context.getWorkerNode().updateJobStatus(jobStatus);
		updateOwnProgress(jobStatus);
	}

	public void addJobReport(JobReport report) {
		context.getWorkerNode().addJobReport(report);
	}

	public abstract void updateOwnProgress(JobStatus jobStatus);
	
}
