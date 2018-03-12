package com.iscreate.op.service.rno.job.client;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.server.impl.JobRunningContext;

/**
 * 工作线程接口
 * 内部运行应该时刻检查stop标识，看是否需要停止。
 * 内部必须catch Interrupted 异常，因为这个表示外部管理程序对这个线程发起强制关闭
 * @author brightming
 *
 */
public interface JobRunnable extends Runnable{

	public void setJobRunningContext(JobRunningContext context);
	public void setJob(JobProfile job);
	
	public boolean isMyJob(JobProfile job);
	public JobStatus runJob(JobProfile job) throws InterruptedException;
	public void stop();
	
	public void updateProgress(JobStatus jobStatus);
	
	/**
	 * 释放资源
	 */
	public void releaseRes();
}
