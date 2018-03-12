package com.iscreate.op.service.rno.job.server.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.service.AbstractService;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.common.JobProgressEventType;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.event.JobNodeEvent;
import com.iscreate.op.service.rno.job.event.JobNodeEventType;
import com.iscreate.op.service.rno.job.event.JobProgressEvent;
import com.iscreate.op.service.rno.job.protocol.WorkerToManagerNodeProtocol;
import com.iscreate.op.service.rno.job.request.HeartbeatEventType;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatRequest;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatResponse;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeRequest;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeResponse;
import com.iscreate.op.service.rno.job.server.JobProgressData;
import com.iscreate.op.service.rno.job.server.NodeResourceState;
import com.iscreate.op.service.rno.job.server.impl.JMContext;

public class WorkerToManagerNodeService extends AbstractService implements
		WorkerToManagerNodeProtocol {

	private static Logger log = Logger
			.getLogger(WorkerToManagerNodeService.class);
	InetSocketAddress serverAddr;
	Server server;
	private JMContext context;
	private JobClientService jobClientService ;

	public WorkerToManagerNodeService(JMContext context) {
		this("WorkerToManagerNodeService", context);
	}

	public WorkerToManagerNodeService(String name, JMContext context) {
		super(name);
		this.context = context;
		jobClientService= context.getManagerNode().getJobClientService();
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		log.debug("WorkerToManagerNodeService serviceIniting....");
		serverAddr = NetUtils.createSocketAddrForHost(conf.get(
				IsConfiguration.JobNodeManagerHost,
				IsConfiguration.JobNodeManagerHost_default), conf.getInt(
				IsConfiguration.JobNodeManagerWorkerPort,
				IsConfiguration.JobNodeManagerWorkerPort_default));
		super.serviceInit(conf);
		log.debug("WorkerToManagerNodeService serviceInited");
	}

	@Override
	protected void serviceStart() throws Exception {
		log.debug("serviceStarting....");

		RPC.Builder builder = new RPC.Builder(super.getConfig());
		builder.setProtocol(WorkerToManagerNodeProtocol.class);
		builder.setInstance(this);
		builder.setPort(serverAddr.getPort());
		builder.setBindAddress(serverAddr.getHostString());
		server = builder.build();
		server.start();
		// server.join();

		super.serviceStart();

		log.debug("serviceStarted");
	}

	@Override
	protected void serviceStop() throws Exception {
		log.debug("serviceStopping....");
		server.stop();

		super.serviceStop();
		log.debug("serviceStopped");
	}

	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return ProtocolSignature.getProtocolSignature(this, protocol,
				clientVersion, clientMethodsHash);
	}

	@Override
	public long getProtocolVersion(String arg0, long arg1) throws IOException {
		return WorkerToManagerNodeProtocol.versionID;
	}

	@Override
	public RegisterWorkerNodeResponse registerWorkerNode(
			RegisterWorkerNodeRequest request) {
		log.debug("registerWorkerNode...request=" + request);
		RegisterWorkerNodeResponse resp = new RegisterWorkerNodeResponse();
		if (request == null || request.getWorkerNodeData() == null) {
			log.error("registerWorkerNode的request不能为空！");
			resp.setFlag(false);
			return resp;
		}
		resp.setFlag(true);
		resp.setRegisterTime(new Date().getTime());

		NodeResource node = new NodeResource();
		node.setState(NodeResourceState.Alive);
		node.setWorkerData(request.getWorkerNodeData());

		// 触发事件
		context.getDispatcher().getEventHandler()
				.handle(new JobNodeEvent(node, JobNodeEventType.Add));
		return resp;
	}

	@Override
	public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest request) {
		log.debug("nodeHeartbeat...request=" + request);
		NodeHeartbeatResponse response = new NodeHeartbeatResponse();

		JobNodeId jnid = request.getWorkerData().getJobNodeId();
		NodeResource node = context.getManagerNode().getNodeResManagerService()
				.getNode(jnid);
		JobProgressData progressData=new JobProgressData();
		progressData.setFinishedJobs(request.getFinishedJobs());
		progressData.setRunningJobs(request.getRunningJobs());
		progressData.setJobNodeId(jnid);
		JobProgressEvent jobProgressEvent=new JobProgressEvent(JobProgressEventType.Update,progressData);	
		
		if (request.getHeartbeatType() == HeartbeatEventType.Normal) {
			// 领取job
			List<JobAction> jas = jobClientService.takePendingJobForNode(request
					.getWorkerData().getJobNodeId());

			// 异步处理状态
			JobNodeEvent event = new JobNodeEvent(node,
					JobNodeEventType.Heartbeat);
			// node的心跳事件
			context.getDispatcher().getEventHandler().handle(event);

			//具体处理返回的job进度
			//处理job进度
			context.getDispatcher().getEventHandler().handle(jobProgressEvent);
			
			response.setJobActions(jas);
			return response;
		} else if (request.getHeartbeatType() == HeartbeatEventType.Shutdown_req) {
			log.debug("node want to shutdown :" + request.getWorkerData());
			
			//处理job进度
			context.getDispatcher().getEventHandler().handle(jobProgressEvent);
			//不要分派任务给它 
			JobNodeEvent event = new JobNodeEvent(node,
					JobNodeEventType.Shutdown_req);
			//通知shutdown 事件
			context.getDispatcher().getEventHandler().handle(event);
			
			//检查pending队列，看是否有stop的任务，如果有，就分派给它，其他的就需要重新调度
			List<JobAction> jas=jobClientService.reAssignPendingJobQueue(node.getWorkerData().getJobNodeId());

			response.setJobActions(jas);
			return response;
		} else if (request.getHeartbeatType() == HeartbeatEventType.Shutdown_ack) {
			log.debug("node shutdown completed:" + request.getWorkerData());
			
			//处理job进度
			context.getDispatcher().getEventHandler().handle(jobProgressEvent);
			//不要分派任务给它 
			JobNodeEvent event = new JobNodeEvent(node,
					JobNodeEventType.Shutdown_ack);
			//通知shutdown 事件
			context.getDispatcher().getEventHandler().handle(event);
			
			return null;
		} else {
			return null;
		}
	}

}
