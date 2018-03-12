package com.iscreate.op.service.rno.job.start;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.service.AbstractService;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.common.NodeType;
import com.iscreate.op.service.rno.job.server.impl.JobManagerNode;
import com.iscreate.op.service.rno.job.server.impl.JobNode;
import com.iscreate.op.service.rno.job.server.impl.JobWorkerNode;

public class JobNodeFacade extends AbstractService {

	Logger log = Logger.getLogger(JobNodeFacade.class);

	private JobManagerNode managerNode;
	private JobWorkerNode workerNode;

	private String configFile = "jobnode.xml";
	private IsConfiguration config;

	//
	private boolean canBeAManager = true;
	private boolean canBeAWorker = true;

	public JobNodeFacade() {
		super("JobNodeFacade");
	}


	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public boolean isCanBeAManager() {
		return canBeAManager;
	}

	public void setCanBeAManager(boolean canBeAManager) {
		this.canBeAManager = canBeAManager;
	}

	public boolean isCanBeAWorker() {
		return canBeAWorker;
	}

	public void setCanBeAWorker(boolean canBeAWorker) {
		this.canBeAWorker = canBeAWorker;
	}

	public JobManagerNode getManagerNode() {
		return managerNode;
	}

	public JobWorkerNode getWorkerNode() {
		return workerNode;
	}

	/**
	 * spring的启动接口
	 */
	public void springInit() {
//		System.out.println("jobxml url="+JobNodeFacade.class.getClassLoader().getResource("job.txt"));
//		System.out.println("jobxml url="+JobNodeFacade.class.getClassLoader().getResource("jobnode.xml"));
//		System.out.println("jobxml url="+JobNodeFacade.class.getClassLoader().getResource("job/jobnode.xml"));
		config = new IsConfiguration(JobNodeFacade.class.getClassLoader().getResource(configFile));
		System.out.println("----mgr host:"+config.get("jobnode.manager.host"));
		String managerHost = config.get("jobnode.manager.host", "localhost");
		String clientPort = config.get("jobnode.manager.client.port", "6660");
		String listenWorkerPort = config.get("jobnode.manager.worker.port",
				"6661");
		String WorkerControlPort = config.get("jobnode.worker.control.port",
				"6662");

		log.debug("managerHost=" + managerHost + ",clientPort=" + clientPort
				+ ",listenWorkerPort=" + listenWorkerPort
				+ ",WorkerControlPort=" + WorkerControlPort);
		// 判断本节点是否是管理节点
		InetAddress managerAddr = null;
		try {
			managerAddr = InetAddress.getByName(managerHost);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		String hadoopUserName=config.get(IsConfiguration.HadoopUserName, "unknown");
		log.debug("init hadoopUserName="+hadoopUserName);
		System.setProperty("HADOOP_USER_NAME",hadoopUserName );

		// 是否是管理节点
		boolean isManagerNode = NetUtils.isLocalAddress(managerAddr);

		try {
			if (isManagerNode && canBeAManager) {
				log.debug("启动管理节点。。。。");
				JobNodeId managerId = new JobNodeId("ManagerNode", managerHost,
						NodeType.ManagerNode);
				managerNode = JobNode.createJobManagerNode(config, managerId);
				managerNode.start();
			}
		} catch (Exception e) {

		}

		try {
			// 默认也作为工作节点启动
			if (canBeAWorker
					|| isManagerNode
					&& config.getBoolean(
							IsConfiguration.JobNodeMgrCanbeAWorker,
							IsConfiguration.JobNodeMgrCanbeAWorker_default)) {
				log.debug("启动工作节点....");
				
				String host="";
				try{
				InetAddress ia = InetAddress.getLocalHost();   
				host = ia.getHostName();//获取计算机主机名  
				}catch(Exception e){
					e.printStackTrace();
					host=NetUtils.getHostname();
				}
				JobNodeId  workerId = new JobNodeId("JobWorkerNode-"+host, host,
						NodeType.WorkerNode);
				
				
//				JobNodeId workerId = new JobNodeId("WorkerNode",
//						NetUtils.getHostname(), NodeType.WorkerNode);
				workerNode = JobNode.createJobWorkerNode(config, workerId);
				workerNode.start();
			}
		} catch (Exception e) {
		}

	}

	/**
	 * sping的停止接口
	 */
	public void springStop() {
		if (workerNode != null) {
			workerNode.stop();
		}
		if (managerNode != null) {
			managerNode.stop();
		}

	}

	public JobNodeFacade(String name) {
		super("JobNodeFacade");
	}

	public void shutdown() {
		if (workerNode != null) {
			workerNode.shutdown();
		}
		if (managerNode != null) {
			managerNode.shutdown();
		}
	}

	public static void main(String[] args) {
		// BasicConfigurator.configure();
		JobNodeFacade facade = new JobNodeFacade("JobNodeFacade");
		facade.springInit();
	}

}
