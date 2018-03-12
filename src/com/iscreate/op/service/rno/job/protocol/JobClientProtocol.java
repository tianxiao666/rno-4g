package com.iscreate.op.service.rno.job.protocol;

import org.apache.hadoop.ipc.VersionedProtocol;

import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.request.DowngradeRequest;
import com.iscreate.op.service.rno.job.request.DowngradeResponse;
import com.iscreate.op.service.rno.job.request.GetAllJobsResponse;
import com.iscreate.op.service.rno.job.request.GetAllNodeMetricsResponse;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.request.KillJobRequest;
import com.iscreate.op.service.rno.job.request.KillJobResponse;
import com.iscreate.op.service.rno.job.request.SubmitJobRequest;
import com.iscreate.op.service.rno.job.request.SubmitJobResponse;


public interface JobClientProtocol extends VersionedProtocol{

	public static final long versionID=1L;
	
	/**
	 * 提交job
	 * @param request
	 * @return
	 */
	public SubmitJobResponse submitJob(SubmitJobRequest request);
	
	/**
	 * 终止某个job
	 * @param request
	 * @return
	 */
	public KillJobResponse killJob(KillJobRequest request);
	
	
	/**
	 * @param request
	 * @return
	 */
	public DowngradeResponse downgrade(DowngradeRequest request);
	
	
	/**
	 * 获取所有运行中的job
	 * @return
	 */
	public GetAllJobsResponse getAllOnGoingJobs();
	
	/**
	 * 获取所有的工作节点信息
	 * @return
	 */
	public GetAllNodeMetricsResponse getAllNodeMetrics();
	
	/**
	 * 获取管理节点信息
	 * @return
	 */
	public NodeResource getManagerNode();
	
	/**
	 * 获取某个job的详情，包括进度、在哪个节点运行
	 * @param jobId
	 * @return
	 */
	public GetJobDetailResponse getJobInfo(long jobId);
}

