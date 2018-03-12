package com.iscreate.op.service.rno.job;

import java.net.URL;

import org.apache.hadoop.conf.Configuration;

public class IsConfiguration extends Configuration {

	//默认的配置文件
	public static final String defaultIsNodeConfigFile="jobnode.xml";
	
	//管理节点是能同时作为worker节点
	public static final String JobNodeMgrCanbeAWorker="jobnode.manager.canbe.worker";
	public static final boolean JobNodeMgrCanbeAWorker_default=true;
	
	//管理主机的信息
	public static final String JobNodeManagerHost="jobnode.manager.host";
	public static final String JobNodeManagerHost_default="localhost";
	
	//管理主机监听客户端连接的端口信息
	public static final String JobNodeManagerClientPort="jobnode.manager.client.port";
	public static final int JobNodeManagerClientPort_default=6660;
	
	//管理主机监听工作主机的连接信息
	public static final String JobNodeManagerWorkerPort="jobnode.manager.worker.port";
	public static final int JobNodeManagerWorkerPort_default=6661;
	
	//工作主机监听管理主机的控制信息
	public static final String JobNodeWorkerControlPort="jobnode.worker.control.port";
	public static final int JobNodeWorkerControlPort_default=6662;

	//检测主机是否有效的周期设置
	public static final String CheckNodeInterval="checknode_interval";
	public static final long CheckNodeInterval_default = 60000;//ms 后台检查节点是否有效的周期
	
	//认为主机失去连接的超时时间设置
	public static final String NodeExpireTime="node.expire.time";
	public static final long NodeExpireTime_default=120000;//ms，2分钟

	//用于配置jobnode capacity的文件
	public static final String JobnodeCapacityFile="jobnode-capacity-file";
	public static final String JobnodeCapacityFile_default = "jobnode-capacity.xml";

	//心跳通信设置
	public static final String HeartbeatInterval="node.heartbeat.time.interval";
	public static final long HeartbeatInterval_default = 60000;//ms

	//runnable的最多数量限制
	public static final String MaxAllowedRunnableCnt = "job.runnable.max.allowed.cnt";
	public static final int MaxAllowedRunnableCnt_default = 30;
	
	//默认的jobrunnablemgr管理类
	public static final String JobRunnableMgrClass="jobnode.worker.runnableManager";
	public static final String JobRunnableMgrClass_default="com.iscreate.op.service.rno.job.client.SimpleJobRunnableMgr";
	
	//在lackres队列等待的最长时间设置，超过了就要扩展节点
	public static final String JobMaxTimeWaitInLackResQueueB4AddNode="job.maxtime.wait.in.lackres.queue";
	public static final long JobMaxTimeWaitInLackResQueueB4AddNode_default=5*60*1000;//5min
	//在lackres队列等待的job的最大数量，超过了就要扩展节点
	public static final String JobMaxCntWaitInLackReqQueueB4AddNode="job.maxcnt.wait.in.lackres.queue";
	public static final long JobMaxCntWaitInLackReqQueueB4AddNode_default=30;//30个
	//默认的

	//用于存放虚拟机所属的物理机的信息。逗号分隔每个物理机，每个物理机包含ip与端口，如:192.168.6.167:65000,192.168.6.168:65000
	public static final String VmCtrlHostList = "vm.host.list";
	public static final int VmCtrlHostPort_default = 65000;
	
	//hadoop系统的用户名称
	public static final String HadoopUserName="hadoop.user.name";
	
	public IsConfiguration(){
		this(defaultIsNodeConfigFile);
	}
	
	public IsConfiguration(String configFile){
		//不加载默认的hadoop配置文件
		super(false);
		super.addResource(configFile);
	}
	
	public IsConfiguration(URL url){
		//不加载默认的hadoop配置文件
		super(false);
		super.addResource(url);
	}
}
