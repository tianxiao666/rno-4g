package com.iscreate.op.service.rno.job.client.api;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.AbstractService;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.request.GetAllJobsResponse;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.server.JobAddCallback;

public abstract class JobClient extends AbstractService {

	public JobClient(String name) {
		super(name);
	}

	
	public static JobClient createJobClient(Configuration conf){
		JobClient client= new JobClientImpl();
		return client;
	}
	
	/**
	 * 提交一个job
	 * @param job
	 * @param callback
	 * @return
	 */
	public abstract <T> T submitJob(JobProfile job,JobAddCallback<T> callback);
	
	/**
	 * 指定终止某个job
	 * @param job
	 * @return
	 */
	public abstract JobStatus killJob(JobProfile job,String account,String reason);
	
	/**
	 * 获取所有运行中的job
	 * @return
	 */
	public abstract GetAllJobsResponse getAllOnGoingJobs();
	
	/**
	 * 获取所有的工作节点信息
	 * @return
	 */
	public abstract List<NodeResource> getAllNodeMetrics();
	
	/**
	 * 获取管理节点信息
	 * @return
	 */
	public abstract NodeResource getManagerNode();
	
	/**
	 * 获取某个job的数据，包括进度
	 * @param jobId
	 * @return
	 */
	public abstract GetJobDetailResponse getJobData(long jobId);
}
