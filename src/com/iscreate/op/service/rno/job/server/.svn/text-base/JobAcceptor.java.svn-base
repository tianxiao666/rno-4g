package com.iscreate.op.service.rno.job.server;

import java.util.List;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public interface JobAcceptor {

	/**
	 * job接收
	 * @param job
	 * @return
	 * @author brightming
	 * 2014-8-14 下午5:34:54
	 */
	public JobStatus recvJob(JobProfile job);
	
	/**
	 * job接收
	 * @param job
	 * @param listener
	 * @return
	 * @author brightming
	 * 2014-9-1 下午4:26:53
	 */
	public <T> T recvJob(JobProfile job,JobAddCallback<T> listener);
	
	/**
	 * job状态更新
	 * @param jobStatus
	 * @return
	 * @author brightming
	 * 2014-8-14 下午5:35:12
	 */
//	public JobStatus updateJobStatus(JobStatus jobStatus);
	
	/**
	 * 增加报告
	 * @param report
	 * @author brightming
	 * 2014-8-14 下午5:37:34
	 */
//	public void addJobReport(JobReport report);
	
	/**
	 * 周期心跳
	 * @param jobWorkerStatus
	 * @param reports
	 * @param updateJobStatusList
	 * @return
	 * @author brightming
	 * 2014-8-14 下午5:35:23
	 */
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,List<JobReport> reports,List<JobStatus> updateJobStatusList);
	
	/**
	 * 停止指定job
	 * @param job
	 * @param account
	 *  发出指令的账号
	 * @param reason
	 *  原因
	 * @return
	 * @author brightming
	 * 2014-8-14 下午5:35:31
	 */
	public JobStatus killJob(JobProfile job,String account,String reason);
	
	
}
