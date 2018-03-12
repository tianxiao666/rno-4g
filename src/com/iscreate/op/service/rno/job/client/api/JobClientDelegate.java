package com.iscreate.op.service.rno.job.client.api;

import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.request.GetAllJobsResponse;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.request.KillJobRequest;
import com.iscreate.op.service.rno.job.request.KillJobResponse;
import com.iscreate.op.service.rno.job.server.JobAddCallback;

public class JobClientDelegate extends JobClient {

	private JobClient client;
	private IsConfiguration conf;

	
	public static JobClient getJobClient(){
		return new JobClientDelegate(new IsConfiguration());
	}
	
	protected void finalize() throws Throwable {
		this.stop();
	};
	
	public JobClientDelegate(IsConfiguration conf) {
		super("");
		this.conf = conf;
		this.client = JobClient.createJobClient(conf);
		init(conf);
		start();
	}
	
	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		client.init(conf);
		super.serviceInit(conf);
	}
	
	@Override
	protected void serviceStart() throws Exception {
		client.start();
		super.serviceStart();
	}

	@Override
	protected void serviceStop() throws Exception {
		client.stop();
		super.serviceStop();
	}
	@Override
	public <T> T submitJob(JobProfile job, JobAddCallback<T> callback) {
		
		return client.submitJob(job, callback);
	}

	@Override
	public JobStatus killJob(JobProfile job,String account,String reason){
		if(job==null||account==null||"".equals(account.trim())||reason==null||"".equals(reason.trim())){
			return null;
		}
		return client.killJob(job, account, reason);
	}

	@Override
	public GetAllJobsResponse getAllOnGoingJobs() {
		return client.getAllOnGoingJobs();
	}

	@Override
	public List<NodeResource> getAllNodeMetrics() {
		return client.getAllNodeMetrics();
	}

	@Override
	public NodeResource getManagerNode() {
		return client.getManagerNode();
	}

	@Override
	public GetJobDetailResponse getJobData(long jobId) {
		return client.getJobData(jobId);
	}

}
