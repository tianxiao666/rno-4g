package com.iscreate.op.service.rno.job.client.api;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.protocol.JobClientProtocol;
import com.iscreate.op.service.rno.job.request.GetAllJobsResponse;
import com.iscreate.op.service.rno.job.request.GetAllNodeMetricsResponse;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.request.KillJobRequest;
import com.iscreate.op.service.rno.job.request.KillJobResponse;
import com.iscreate.op.service.rno.job.request.SubmitJobRequest;
import com.iscreate.op.service.rno.job.request.SubmitJobResponse;
import com.iscreate.op.service.rno.job.server.JobAddCallback;

public class JobClientImpl extends JobClient {

	private static Logger log=Logger.getLogger(JobClientImpl.class);
	
	private static Map<String,JobClientImpl> cachedClient=new ConcurrentHashMap<String,JobClientImpl>(1);
	
	JobClientProtocol proxy;
	InetSocketAddress serverAddr;
	public JobClientImpl(){
		super("JobClientImpl");
	}
	@Override
	public void init(Configuration arg0) {
		//server的addr
		serverAddr=NetUtils.createSocketAddrForHost(arg0.get(IsConfiguration.JobNodeManagerHost,IsConfiguration.JobNodeManagerHost_default),arg0.getInt(IsConfiguration.JobNodeManagerClientPort,IsConfiguration.JobNodeManagerClientPort_default));
		super.init(arg0);
	}
	
	@Override
	protected void serviceStart() throws Exception {
		proxy=RPC.waitForProxy(JobClientProtocol.class, JobClientProtocol.versionID, serverAddr, getConfig());
		
		super.serviceStart();
	}
	
	@Override
	protected void serviceStop() throws Exception {
		RPC.stopProxy(proxy);
		super.serviceStop();
	}
	
	@Override
	public <T> T submitJob(JobProfile job, JobAddCallback<T> callback) {
		if(job==null){
			log.error("submitJob 的job不能为空！");
			return null;
		}
		SubmitJobRequest request=new SubmitJobRequest();
		request.setJobProfile(job);
		log.debug("begin to submitJob ... request="+request);
		SubmitJobResponse response=proxy.submitJob(request);
		log.debug("submitJob的返回结果："+response);
		T retObj=null;
		if(callback!=null){
			retObj=callback.doWhenJobAdded(response.getJobProfile());
		}
		return retObj;
	}

	@Override
	public JobStatus killJob(JobProfile job,String account,String reason){
		KillJobRequest request=new KillJobRequest();
	    request.setAccount(account);
	    request.setJob(job);
	    request.setReason(reason);
	    
//		if(request==null){
//			log.error("killJob 的request不能为空！");
//			return null;
//		}
		log.debug("begin to submit killjob request..."+request);
		KillJobResponse response=proxy.killJob(request);
		log.debug("killJob的返回结果："+response);
		return response.getJobStatus();
	}

	@Override
	public GetAllJobsResponse getAllOnGoingJobs() {
		GetAllJobsResponse resp=proxy.getAllOnGoingJobs();
		return resp;
	}
	
	
	@Override
	public List<NodeResource> getAllNodeMetrics() {
		GetAllNodeMetricsResponse resp= proxy.getAllNodeMetrics();
		return resp.getNodes();
	}
	@Override
	public NodeResource getManagerNode() {
		return proxy.getManagerNode();
	}


	/**
	 * 获取某个job的数据，包括进度
	 * @param jobId
	 * @return
	 */
	public GetJobDetailResponse getJobData(long jobId){
		return proxy.getJobInfo(jobId);
	}
	
	
	public static void main(String[] args) {
		JobClient client=JobClient.createJobClient(new IsConfiguration());
		System.out.println(client.getName()+"---"+client.getStartTime()+"---"+client.getServiceState());
		
		client.start();
		
		client.submitJob(null, null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.stop();
	}
}
