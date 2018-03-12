package com.iscreate.op.service.rno.job.server.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.common.NodeType;
import com.iscreate.op.service.rno.job.common.JobProgressEventType;
import com.iscreate.op.service.rno.job.event.IsAsyncDispatcher;
import com.iscreate.op.service.rno.job.event.JobEventType;
import com.iscreate.op.service.rno.job.event.JobNodeEvent;
import com.iscreate.op.service.rno.job.event.JobNodeEventType;
import com.iscreate.op.service.rno.job.event.JobProgressEvent;
import com.iscreate.op.service.rno.job.event.NodeResourceMonitorEvent;
import com.iscreate.op.service.rno.job.event.NodeResourceMonitorEventType;
import com.iscreate.op.service.rno.job.server.JobScheduler;
import com.iscreate.op.service.rno.job.server.service.JobClientService;
import com.iscreate.op.service.rno.job.server.service.NodeResourceManagerService;
import com.iscreate.op.service.rno.job.server.service.WorkerToManagerNodeService;
import com.iscreate.op.service.rno.job.server.service.JobClientService.JobEventDispatcher;

public class JobManagerNode extends JobNode{

	private static Logger log = Logger.getLogger(JobManagerNode.class);

	JMContext context = new JMContext();
	IsAsyncDispatcher dispatch;
	
	
	// 处理客户端请求的service
	JobClientService jobClientService;
	// 处理工作节点请求的service
	WorkerToManagerNodeService workerNodeService;

	// 负责资源管理的service
	NodeResourceManagerService nodeResManagerService;
	
	//负责工作调度的服务
	JobScheduler jobScheduler;
	
	public JobManagerNode() {
		this("JobManagerNode");
	}

	public JobManagerNode(String name) {
		super(name);
		jobNodeId = new JobNodeId("JobManagerNode", NetUtils.getHostname(),
				NodeType.ManagerNode);
		context.setManagerNode(this);
	}

	@Override
	protected void serviceInit(Configuration arg0) throws Exception {
		log.debug("serviceInit...");

		//事件派发中心
		dispatch = new IsAsyncDispatcher();
		dispatch.init(arg0);
		context.setDispatcher(dispatch);
        addService(dispatch); 
		
		// 节点资源管理程序
		nodeResManagerService = new NodeResourceManagerService(
				"NodeResourceManagerService", context);
		nodeResManagerService.init(arg0);
		addService(nodeResManagerService);
		log.debug("addservice nodeResManagerService");

		// 客户端服务程序
		jobClientService = new JobClientService("JobClientService", context);
		jobClientService.init(arg0);
		addService(jobClientService);
		log.debug("addservice jobClientService");
		
		
		
		// 工作节点通信服务
		workerNodeService = new WorkerToManagerNodeService(context);
		workerNodeService.init(arg0);
		addService(workerNodeService);
		log.debug("addservice workerNodeService");

		// TODO 对工作节点的控制客户端代理

		//注册响应
		dispatch.register(JobEventType.class, new JobEventDispatcher(context));
		dispatch.register(JobNodeEventType.class, new JobNodeEventDispatcher(context));
		dispatch.register(NodeResourceMonitorEventType.class, new NodeResourceMonitorEventDispatcher(context));
		dispatch.register(JobProgressEventType.class, new JobProgressEventDispatcher(context));
		super.serviceInit(arg0);

		log.debug("serviceInited");
	}

	
	
	public void shutdown(){
		
	}
	
	
	public JMContext getContext() {
		return context;
	}

	public IsAsyncDispatcher getDispatch() {
		return dispatch;
	}

	public JobClientService getJobClientService() {
		return jobClientService;
	}

	public WorkerToManagerNodeService getWorkerNodeService() {
		return workerNodeService;
	}

	public NodeResourceManagerService getNodeResManagerService() {
		return nodeResManagerService;
	}


	/**
	 * 负责分发处理JobNode的事件：节点增加，节点shutdown，节点失去联系
	 * @author brightming
	 *
	 */
	static class JobNodeEventDispatcher implements EventHandler<JobNodeEvent>{
		JMContext context;
		NodeResourceManagerService nodeResManagerService;
		
		JobNodeEventDispatcher(JMContext context){
			this.context=context;
			nodeResManagerService=context.getManagerNode().nodeResManagerService;
		}
		@Override
		public void handle(JobNodeEvent arg0) {
			log.debug("JobNodeEventDispatcher handle event:"+arg0);
			if(arg0.getType()==JobNodeEventType.Add){
				log.debug("JobNodeEventDispatcher需要处理node新增事件");
				nodeResManagerService.addNode(arg0.getNodeResource().getWorkerData());
				//检查lack队列
				context.getManagerNode().jobClientService.checkLackingResourceJobQueue();
			}else if(arg0.getType()==JobNodeEventType.Shutdown_req){
				log.debug("JobNodeEventDispatcher需要处理node shutdown req事件");
				nodeResManagerService.shutdownNode_req(arg0.getNodeResource());
			}else if(arg0.getType()==JobNodeEventType.Shutdown_ack){
				log.debug("JobNodeEventDispatcher需要处理node shutdown ack事件");
				nodeResManagerService.shutdownNode_ack(arg0.getNodeResource());
			}else if(arg0.getType()==JobNodeEventType.LoseContact){
				log.debug("JobNodeEventDispatcher需要处理node losecontact事件");
				nodeResManagerService.nodeLoseContact(arg0.getNodeResource());
			}else if(arg0.getType()==JobNodeEventType.Heartbeat){
				log.debug("JobNodeEventDispatcher需要处理node heartbeat事件");
				nodeResManagerService.touchNode(arg0.getNodeResource().getWorkerData());
			}else{
				log.error("JobNodeEventDispatcher handle unknown event Type！！"+arg0.getType());
			}
		}
		
	}
	
	/**
	 * 节点资源监控事件分发器
	 * 节点资源不足，节点资源过剩
	 * @author brightming
	 *
	 */
	static class NodeResourceMonitorEventDispatcher implements EventHandler<NodeResourceMonitorEvent>{
		JMContext context;
		NodeResourceMonitorEventDispatcher(JMContext context){
			this.context=context;
		}
		@Override
		public void handle(NodeResourceMonitorEvent arg0) {
			
		}
		
	}
	
	/**
	 * 处理job进度事件
	 * @author brightming
	 *
	 */
	static class JobProgressEventDispatcher implements EventHandler<JobProgressEvent>{
		
		JMContext context;
		JobClientService clientService;
		JobProgressEventDispatcher(JMContext context){
			this.context=context;
			clientService=context.getManagerNode().jobClientService;
		}
		
		@Override
		public void handle(JobProgressEvent arg0) {
			log.debug("NodeHeartbeatEventDispatcher处理job进度事件");
			clientService.updateJobProgress(arg0.getProgressData());
		}
		
	}
}
