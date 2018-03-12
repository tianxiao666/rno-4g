package com.iscreate.op.service.rno.job.server.service;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.avro.AvroRemoteException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.service.AbstractService;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.NodeState;
import org.apache.hadoop.yarn.api.records.QueueACL;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.QueueUserACLInfo;
import org.apache.hadoop.yarn.api.records.YarnClusterMetrics;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobCapacity;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.avro.proto.VmCtrlRequest;
import com.iscreate.op.service.rno.job.avro.proto.VmCtrlResponse;
import com.iscreate.op.service.rno.job.avro.proto.VmInfo;
import com.iscreate.op.service.rno.job.avro.proto.VmInfoList;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.common.NodeType;
import com.iscreate.op.service.rno.job.server.NodeResourceState;
import com.iscreate.op.service.rno.job.server.api.NodeResourceManager;
import com.iscreate.op.service.rno.job.server.impl.JMContext;
import com.iscreate.op.service.rno.job.vmhostctrl.VmHostCtrlClient;

public class NodeResourceManagerService extends AbstractService implements
		NodeResourceManager {

	private static Logger log = Logger
			.getLogger(NodeResourceManagerService.class);
	private JMContext context;

	private CheckNodeThread checkNodeThread;
	private CheckHadoopThread checkHadoopThread;
	private VMResourceMonitorThread vmResMonitorThread;
	private AdjustNodeClusterThread adjustNodeClusterThread;

	private static long checkNodeStateInterval = 60000;// ms
	private static long nodeExpireTime = 120000;// ms，2分钟

	// job在lack队列等待的最长时间，超过了就要扩展node规模
	private long jobMaxWaitInLackQueue = 5 * 60 * 1000;
	private long jobMaxCntInLackQueue = 30;
	// 管理的节点资源
	private Map<NodeResourceState, List<NodeResource>> jobNodeResources = new HashMap<NodeResourceState, List<NodeResource>>();
	private Map<JobNodeId, NodeResource> allNodes = Collections
			.synchronizedMap(new WeakHashMap<JobNodeId, NodeResource>());

	private HadoopCluster hadoopCluster = new HadoopCluster();
	private YarnClient yarnClient;

	// 虚拟机所在主机的控制客户端
	private Map<String, VmHostCtrlClient> vmHostCtrlClients = new ConcurrentHashMap<String, VmHostCtrlClient>();
	// 主机信息
	private Map<String, VmHostInfo> vmHosts = new ConcurrentHashMap<String, NodeResourceManagerService.VmHostInfo>();

	// 当前在处理的调节意见
	private AdjustNodeResult adjustNode;

	public NodeResourceManagerService(String name, JMContext context) {
		super(name);

		this.context = context;

		List<NodeResource> nodes = Collections
				.synchronizedList(new ArrayList<NodeResource>());
		jobNodeResources.put(NodeResourceState.Alive, nodes);

		nodes = Collections.synchronizedList(new ArrayList<NodeResource>());
		jobNodeResources.put(NodeResourceState.LoseContact, nodes);

		nodes = Collections.synchronizedList(new ArrayList<NodeResource>());
		jobNodeResources.put(NodeResourceState.Shutdown, nodes);
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		log.debug("serviceInit...");

		// node超时时间
		nodeExpireTime = conf.getLong(IsConfiguration.NodeExpireTime,
				IsConfiguration.NodeExpireTime_default);
		// 检测node的时间
		checkNodeStateInterval = conf.getLong(
				IsConfiguration.CheckNodeInterval,
				IsConfiguration.CheckNodeInterval_default);
		checkNodeThread = new CheckNodeThread(this);
		checkNodeThread.running = true;

		checkHadoopThread = new CheckHadoopThread();
		checkHadoopThread.run = true;


		adjustNodeClusterThread = new AdjustNodeClusterThread();
		adjustNodeClusterThread.run = true;

		yarnClient = YarnClient.createYarnClient();

		yarnClient.init(new YarnConfiguration());

		jobMaxWaitInLackQueue = conf.getLong(
				IsConfiguration.JobMaxTimeWaitInLackResQueueB4AddNode,
				IsConfiguration.JobMaxTimeWaitInLackResQueueB4AddNode_default);
		jobMaxCntInLackQueue = conf.getLong(
				IsConfiguration.JobMaxCntWaitInLackReqQueueB4AddNode,
				IsConfiguration.JobMaxCntWaitInLackReqQueueB4AddNode_default);

		// 获取用于管理虚拟机的物理主机信息
		Collection<String> hostList = conf
				.getStringCollection(IsConfiguration.VmCtrlHostList);
		if (hostList != null && hostList.size() > 0) {
			String[] hi = null;
			VmHostInfo vmHost = null;
			for (String host : hostList) {
				if(host==null||"".equals(host.trim())){
					continue;
				}
				hi = host.split(":");// ip:port
				vmHost = new VmHostInfo();
				if (hi.length == 2) {
					vmHost.addr = hi[0];
					vmHost.listenPort = Integer.parseInt(hi[1]);
				} else if (hi.length == 1) {
					vmHost.addr = hi[0];
					vmHost.listenPort = IsConfiguration.VmCtrlHostPort_default;
				} else {
					log.warn("无效的VmHost 项:" + host);
					continue;
				}
				vmHosts.put(vmHost.addr, vmHost);
			}
		}

		if(vmHosts!=null && vmHosts.size()>0){
			vmResMonitorThread = new VMResourceMonitorThread();
			vmResMonitorThread.run = true;
		}
		super.serviceInit(conf);
		log.debug("serviceInited");
	}

	@Override
	protected void serviceStart() throws Exception {
		log.debug("serviceStart...");

		log.debug("try to start yarnclient...");
		yarnClient.start();
		super.serviceStart();
		log.debug("serviceStarted");

		log.debug("yarnclient started...");

		checkNodeThread.start();
		checkHadoopThread.start();
		
		if(vmResMonitorThread!=null){
			vmResMonitorThread.start();
		}
		
		adjustNodeClusterThread.start();

	}

	@Override
	protected void serviceStop() throws Exception {
		log.debug("serviceStopping...");
		checkNodeThread.running = false;
		try {
			checkNodeThread.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}

		checkHadoopThread.run = false;
		try {
			checkHadoopThread.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (vmResMonitorThread != null) {
			vmResMonitorThread.run = false;
			try {
				vmResMonitorThread.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			vmResMonitorThread=null;
		}

		adjustNodeClusterThread.run = false;
		try {
			adjustNodeClusterThread.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		yarnClient.stop();

		for (VmHostCtrlClient vmClient : vmHostCtrlClients.values()) {
			vmClient.stop();
		}
		super.serviceStop();
		log.debug("serviceStoped");
	}

	@Override
	public boolean addNode(JobWorkerStatus workerNode) {
		log.debug("addNode...workerNode=" + workerNode);
		if (workerNode == null) {
			return false;
		}
		List<NodeResource> liveNodes = jobNodeResources
				.get(NodeResourceState.Alive);
		NodeResource newNode = new NodeResource();
		newNode.setWorkerData(new JobWorkerStatus(workerNode));
		newNode.setState(NodeResourceState.Alive);
		newNode.setLastTouchTime(new Date().getTime());

		if (liveNodes.contains(newNode)) {
			log.warn("addNode 结果：已经包含该节点信息。workerNode=" + workerNode);
			return false;
		}
		log.debug("put node into live queue." + newNode);
		addOneNodeInternal(newNode);
		log.debug("成功 addNode:" + workerNode);
		return true;
	}

	private void addOneNodeInternal(NodeResource node) {
		jobNodeResources.get(NodeResourceState.Alive).add(node);
		allNodes.put(node.getWorkerData().getJobNodeId(), node);
	}

	@Override
	public boolean removeNode(JobNodeId jobNodeId) {
		synchronized (jobNodeResources) {
			// 需要询问该node是否有正在负责的job未完成
		}
		return false;
	}

	@Override
	public void touchNode(JobWorkerStatus workerNode) {
		// 更新touchtime
		if (workerNode == null) {
			return;
		}
		if (allNodes.containsKey(workerNode.getJobNodeId())) {
			synchronized (jobNodeResources) {
				NodeResource node = allNodes.get(workerNode.getJobNodeId());
				if (node != null) {
					// 时间
					node.setLastTouchTime(new Date().getTime());
					// 资源信息不能由客户端随意改动，最多运行改动maxSlot的值，这个是可以的
					// node.getWorkerData().setJobCapacities(
					// new HashMap(workerNode.getJobCapacities()));
					//
					if (node.getState() == NodeResourceState.LoseContact) {
						log.debug("node:" + node + " 重新连接上了。");
						node.setState(NodeResourceState.Alive);
						jobNodeResources.get(NodeResourceState.Alive).add(node);
						jobNodeResources.get(NodeResourceState.LoseContact)
								.remove(node);
					}
				}
			}
		}
	}

	@Override
	public NodeResource getNode(JobNodeId jobNodeId) {
		return allNodes.get(jobNodeId);
	}

	@Override
	public List<NodeResource> getAllNodes() {
		return new ArrayList(allNodes.values());
	}

	public List<NodeResource> getNodeByState(NodeResourceState state) {
		return new ArrayList(jobNodeResources.get(state));
	}

	/**
	 * 实现简单的轮询分配
	 * @param jobType
	 * @return
	 * @author brightming
	 * 2014-12-22 上午11:36:10
	 */
	@Override
	public NodeResource popCapabilityNode(String jobType) {
		log.debug("try to find a node to handle job：" + jobType);
		synchronized (jobNodeResources) {
			List<NodeResource> liveNodes = jobNodeResources
					.get(NodeResourceState.Alive);
			List<NodeResource> candiNodes = new ArrayList<NodeResource>();
			if (liveNodes != null && liveNodes.size() > 0) {
				log.debug("live node cnt:" + liveNodes.size());
				for (NodeResource one : liveNodes) {
					if (one.getWorkerData().canHandleJob(jobType)) {
						candiNodes.add(one);
					}
				}
			}

			log.debug("count of node which can handle job:" + jobType + " is "
					+ candiNodes.size());
			// 根据策略，从待选的node中得到一个
			if (candiNodes.size() == 0) {
				log.warn("no node can handle job for now:" + jobType);
				return null;
			}
			Collections.sort(candiNodes, new NodeResourceJobTypeComparator(
					jobType));
			
			NodeResource selectedNode = candiNodes.get(0);
			log.debug("find node for job:" + jobType + " node is :"
					+ selectedNode);
			// 预先占用资源，可用资源应减少
			selectedNode.getWorkerData().occupyJobSlot(jobType);
			return selectedNode;
		}
	}

	/**
	 * 根据一项jobtype，对候选资源进行排队： 剩余资源多的，排在前面
	 * 
	 * @author brightming
	 * 
	 */
	static class NodeResourceJobTypeComparator implements Comparator<NodeResource> {
		String jobType = "";

		NodeResourceJobTypeComparator(String jobT) {
			this.jobType = jobT;
		}

		@Override
		public int compare(NodeResource o1, NodeResource o2) {
//			String o1N=o1.getWorkerData().getJobNodeId().getName();
//			String o2N=o2.getWorkerData().getJobNodeId().getName();
//			System.out.print("\n------------------\no1="+o1N+",o2="+o2N);
			int n1freecnt = o1.getWorkerData().getJobCapacities().get(jobType)
					.getMaxSlots()-o1.getWorkerData().getJobCapacities().get(jobType)
					.getCurSlots();
			int n2freecnt = o2.getWorkerData().getJobCapacities().get(jobType)
					.getMaxSlots()-o2.getWorkerData().getJobCapacities().get(jobType)
					.getCurSlots();
//			System.out.println("  "+o1N+"剩余资源数量："+n1freecnt+","+o2N+"剩余资源数量："+n2freecnt);
			if (n1freecnt > n2freecnt) {
				// node1的剩余资源大于node2的剩余资源,排在前面
				return -1;
			} else if (n1freecnt < n2freecnt) {
				return 1;
			}
			return 0;
		}

	}

	
	public static void main(String[] args) {
		List<NodeResource> nodes=new ArrayList<NodeResource>();
		NodeResource node=null;
		JobWorkerStatus worker=null;//new JobWorkerStatus();
		JobCapacity capacity=null;
		Map<String,JobCapacity> caps=null;

		//node1
		node=new NodeResource();
		nodes.add(node);
		worker=new JobWorkerStatus();
		node.setWorkerData(worker);
		worker.setJobNodeId(new JobNodeId("node1","",NodeType.WorkerNode));
		caps=new HashMap<String,JobCapacity>();
		worker.setJobCapacities(caps);
		
		capacity=new JobCapacity();
		capacity.setCurSlots(4);
		capacity.setMaxSlots(5);
		caps.put("1", capacity);
		
		
		//node2
		node=new NodeResource();
		nodes.add(node);
		worker=new JobWorkerStatus();
		node.setWorkerData(worker);
		worker.setJobNodeId(new JobNodeId("node2","",NodeType.WorkerNode));
		caps=new HashMap<String,JobCapacity>();
		worker.setJobCapacities(caps);
		
		capacity=new JobCapacity();
		capacity.setCurSlots(3);
		capacity.setMaxSlots(5);
		caps.put("1", capacity);
		
		//node3
		node=new NodeResource();
		nodes.add(node);
		worker=new JobWorkerStatus();
		node.setWorkerData(worker);
		worker.setJobNodeId(new JobNodeId("node3","",NodeType.WorkerNode));
		caps=new HashMap<String,JobCapacity>();
		worker.setJobCapacities(caps);
		
		capacity=new JobCapacity();
		capacity.setCurSlots(4);
		capacity.setMaxSlots(5);
		caps.put("1", capacity);
		
		
		
		
		Collections.sort(nodes,new NodeResourceJobTypeComparator("1"));
		for(NodeResource n:nodes){
		System.out.println(n.getWorkerData().getJobNodeId().getName());
		}
	}
	
	/**
	 * 未负责任何job的vm node
	 * 
	 * @return
	 */
	public List<VmNodeWithHost> getIdleNodeWithHost() {
		List<NodeResource> nodes = getNodeByState(NodeResourceState.Alive);
		List<VmNodeWithHost> nodeWithHosts = new ArrayList<VmNodeWithHost>();
		VmHostInfo host = null;
		for (NodeResource n : nodes) {
			if (n.getWorkerData().isIdle()) {
				host = getVmNodeHost(n);
				if (host != null) {
					nodeWithHosts.add(new VmNodeWithHost(n, host));
				}
			}
		}
		return nodeWithHosts;
	}

	/**
	 * 判断是否是一个vm 节点，
	 * 
	 * @param n
	 * @return
	 */
	private boolean isAVmNode(NodeResource n) {
		if (n == null) {
			return false;
		}
		String hostname = n.getWorkerData().getJobNodeId().getNodeHost();
		for (VmHostInfo vmhost : vmHosts.values()) {
			if (vmhost.hasVm(hostname)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取一个节点的
	 * 
	 * @param n
	 * @return
	 */
	private VmHostInfo getVmNodeHost(NodeResource n) {
		if (n == null) {
			return null;
		}
		String hostname = n.getWorkerData().getJobNodeId().getNodeHost();
		for (VmHostInfo vmhost : vmHosts.values()) {
			if (vmhost.hasVm(hostname)) {
				return vmhost;
			}
		}
		return null;
	}

	/**
	 * 调整workerNode的数量 返回0：表示无需调整 返回大于0：表示需要新增指定节点 返回小于0：表示需要减少指定节点
	 * 
	 * @return
	 */
	private AdjustNodeResult adjustWorkerNode() {
		// 根据排队的job数量，排队的时间长度，某类型每个job的运行时间长度来判断
		int cnt = 0;
		JobClientService jobMgr = context.getManagerNode()
				.getJobClientService();
		long waitT = jobMgr.getJobMaxWaitingTime();
		long jobCnt = jobMgr.getLackResJobCnt();

		AdjustNodeResult adjust = new AdjustNodeResult();
		if (waitT > jobMaxWaitInLackQueue || jobCnt > 30) {
			// 等待超过5分钟
			cnt = 1;
			adjust.isIncrement = true;
			adjust.cnt = cnt;
			return adjust;
		}

		// 减少规模
		List<VmNodeWithHost> idleNodes = getIdleNodeWithHost();
		// 最少要保留一个node
		if (idleNodes.size() > 0) {
			if (idleNodes.size() == getNodeByState(NodeResourceState.Alive)
					.size()) {
				cnt = idleNodes.size() - 1;
			}
			idleNodes.remove(0);
			adjust.isIncrement = false;
			adjust.cnt = cnt * -1;
			adjust.nodeWithHost = idleNodes;
		}
		return adjust;
	}

	/**
	 * 调整hadoop Node的数量 返回0：表示无需调整 返回大于0：表示需要新增指定节点 返回小于0：表示需要减少指定节点
	 * 
	 * @return
	 */
	private int adjustHadoopNode() {
		// 集群job数量的情况，
		int cnt = 0;
		if (hadoopCluster.numNodeManagers < 2) {
			cnt = 1;
		}
		// TODO and then?
		return cnt;
	}

	class CheckNodeThread extends Thread {
		private NodeResourceManagerService outter;
		boolean running = false;

		protected CheckNodeThread(NodeResourceManagerService serv) {
			outter = serv;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.currentThread().sleep(checkNodeStateInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkNode();
			}
		}

		private void checkNode() {
			synchronized (outter.jobNodeResources) {
				// 只检查Alive状态的
				List<NodeResource> nodes = outter.jobNodeResources
						.get(NodeResourceState.Alive);
				long now = new Date().getTime();
				List<NodeResource> maybeLoseContNodes = new ArrayList<NodeResource>();
				if (nodes != null && nodes.size() > 0) {
					for (NodeResource node : nodes) {
						if (now - node.getLastTouchTime() > nodeExpireTime) {
							log.warn("节点：" + node + " 已经超过了指定时间："
									+ nodeExpireTime
									+ "未与系统联系，将移入lostcontact队列");
							maybeLoseContNodes.add(node);
							node.setState(NodeResourceState.LoseContact);
							context.getManagerNode()
									.getJobClientService()
									.reAssignPendingJobQueue(
											node.getWorkerData().getJobNodeId());
						}
					}
				}
				if (maybeLoseContNodes.size() > 0) {
					log.debug("将以下节点转为LostContact队列："
							+ maybeLoseContNodes.size() + " -- "
							+ maybeLoseContNodes);
					nodes.removeAll(maybeLoseContNodes);
					outter.jobNodeResources.get(NodeResourceState.LoseContact)
							.addAll(maybeLoseContNodes);
				}
				// TODO 发送事件给jobclientservice
			}
		}
	}

	/**
	 * 处理节点关闭事件
	 * 
	 * @param nodeResource
	 */
	public void shutdownNode_req(NodeResource nodeResource) {
		// 节点申请shutdown
		if (nodeResource == null) {
			return;
		}
		log.debug("node:" + nodeResource.getWorkerData().getJobNodeId()
				+ " need to shut down...");
		NodeResource node = allNodes.get(nodeResource.getWorkerData()
				.getJobNodeId());
		if (node != null) {
			if (jobNodeResources.get(NodeResourceState.Shutdown).contains(node)) {
				return;
			}
			node.setState(NodeResourceState.Shutdown);
			List<NodeResource> ns = jobNodeResources
					.get(NodeResourceState.Alive);
			if (ns.contains(node)) {
				ns.remove(node);
			}
			jobNodeResources.get(NodeResourceState.Shutdown).add(node);
		}
	}

	public void shutdownNode_ack(NodeResource nodeResource) {
		log.debug("shutdownNode_ack.node=" + nodeResource);
		// 节点shutdown工作完成
		if (nodeResource == null) {
			return;
		}
		log.debug("node:" + nodeResource.getWorkerData().getJobNodeId()
				+ " shut down finished");
		NodeResource node = allNodes.get(nodeResource.getWorkerData()
				.getJobNodeId());
		if (node != null) {
			if (!jobNodeResources.get(NodeResourceState.Shutdown)
					.contains(node)) {
				System.out.println("shutdownNode_ack的node不在shutdown队列！");
				return;
			}
			log.debug("从allNodes中移除。。。");
			allNodes.remove(node.getWorkerData().getJobNodeId());
			log.debug("从shudown node中移除。。。");
			jobNodeResources.get(NodeResourceState.Shutdown).remove(node);
			jobNodeResources.get(NodeResourceState.Alive).remove(node);
		}
	}

	/**
	 * 处理节点失去联系事件
	 * 
	 * @param nodeResource
	 */
	public void nodeLoseContact(NodeResource nodeResource) {

	}

	/**
	 * 定期检查hadoop节点数量、应用数量的情况
	 * 
	 * @author brightming
	 * 
	 */
	class CheckHadoopThread extends Thread {

		// 集群最小的nodemanager的数量
		int leastNodeNum = 5;
		boolean run = false;

		@Override
		public void run() {
			run = true;
			while (run) {
				try {
					YarnClusterMetrics clusterMetrics = yarnClient
							.getYarnClusterMetrics();
					int numTotalNodes = clusterMetrics.getNumNodeManagers();
					hadoopCluster.numNodeManagers = numTotalNodes;

					log.info("Number of node is :" + numTotalNodes);
					List<NodeReport> clusterNodeReports = yarnClient
							.getNodeReports(NodeState.RUNNING);
					hadoopCluster.nodeReports = clusterNodeReports;

					log.info("Got Cluster node info from ASM");
					for (NodeReport node : clusterNodeReports) {
						log.info("Got node report from ASM for" + ", nodeId="
								+ node.getNodeId() + ", nodeAddress"
								+ node.getHttpAddress() + ", nodeRackName"
								+ node.getRackName() + ", nodeNumContainers"
								+ node.getNumContainers());
					}

					QueueInfo queueInfo = yarnClient.getQueueInfo("default");
					log.info("Queue info" + ", queueName="
							+ queueInfo.getQueueName()
							+ ", queueCurrentCapacity="
							+ queueInfo.getCurrentCapacity()
							+ ", queueMaxCapacity="
							+ queueInfo.getMaximumCapacity()
							+ ", queueApplicationCount="
							+ queueInfo.getApplications().size()
							+ ", queueChildQueueCount="
							+ queueInfo.getChildQueues().size());

					hadoopCluster.queueInfo = queueInfo;

					List<QueueUserACLInfo> listAclInfo = yarnClient
							.getQueueAclsInfo();
					for (QueueUserACLInfo aclInfo : listAclInfo) {
						for (QueueACL userAcl : aclInfo.getUserAcls()) {
							log.info("User ACL Info for Queue" + ", queueName="
									+ aclInfo.getQueueName() + ", userAcl="
									+ userAcl.name());
						}
					}
				} catch (YarnException e1) {
					e1.printStackTrace();
				}catch (IOException e2) {
					e2.printStackTrace();
				}
				try {
					List<ApplicationReport> apps = yarnClient.getApplications();
					hadoopCluster.applications = apps;
				} catch (YarnException e) {
					e.printStackTrace();
				}catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 定期检查节点集群的情况，是否过多，是否过少 单线程调节
	 * 
	 * @author brightming
	 * 
	 */
	class AdjustNodeClusterThread extends Thread {
		boolean run = false;

		@Override
		public void run() {
			run = true;
			while (run) {
				try {
					Thread.sleep(20 * 60 * 1000);// 20分钟检查一次
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				adjustNode = adjustWorkerNode();

				// adjustNode=new AdjustNodeResult();
				// adjustNode.isIncrement=true;
				// adjustNode.cnt=1;

				log.debug("调节建议：" + adjustNode);
				// 同步处理，
				if (adjustNode.isIncrement && adjustNode.cnt > 0) {
					if (vmHosts == null || vmHosts.size() == 0) {
						// 没有物理机，无法扩展
						log.error("没有物理机，无法执行扩展。。。");
					} else {
						// 添加节点，均匀分配
						List<VmHostInfo> vhs = new ArrayList<VmHostInfo>();
						int curRunVm = 0, totalRunVm = 0;
						for (VmHostInfo vh : vmHosts.values()) {
							vhs.add(vh);
							curRunVm += vh.getRunningVmCnt();
						}
						;
						totalRunVm = curRunVm + adjustNode.cnt;
						log.debug("当前有：" + curRunVm + "个虚拟机在运行，需要扩展到："
								+ totalRunVm + "个");
						// 按照running的vm数量进行从低到高排序
						Collections.sort(vhs, new Comparator<VmHostInfo>() {
							@Override
							public int compare(VmHostInfo o1, VmHostInfo o2) {
								if (o1.getRunningVmCnt() > o2.getRunningVmCnt()) {
									return 1;
								} else if (o1.getRunningVmCnt() < o2
										.getRunningVmCnt()) {
									return -1;
								}
								return 0;
							}
						});
						// 共有totalRunVm，均匀分配给各个host
						int avg = (int) Math.ceil(totalRunVm * 1.0f
								/ vmHosts.size());
						for (VmHostInfo vh : vhs) {
							if (!run) {
								return;
							}
							int diff = avg - vh.getRunningVmCnt();
							if (diff > 0) {
								log.debug("vmhost:" + vh.addr
										+ " need to increase :" + diff + " vm");
								for (int i = 0; i < diff; i++) {
									vh.createVm(NodeType.WorkerNode);
									curRunVm++;
									if (curRunVm >= totalRunVm) {
										break;
									}
								}
							}
							if (curRunVm >= totalRunVm) {
								break;
							}
						}
						// for (int i = 0; i < adjustNode.cnt; i++) {
						// // TODO
						// // 询问各虚拟机主机，是否能支持该节点
						//
						// // 构造请求
						//
						// // 一次性发送请求
						//
						// // 循环查询节点启动情况
						//
						// //平均分配
						// }
					}

				} else if (!adjustNode.isIncrement
						&& adjustNode.nodeWithHost != null
						&& adjustNode.nodeWithHost.size() > 0) {
					log.debug("开始申请减少节点...");
					JobClientService clientService = context.getManagerNode()
							.getJobClientService();
					for (VmNodeWithHost nh : adjustNode.nodeWithHost) {
						// 在此要检查该node如果又被分派了job，则不再执行shutdown
						if (!nh.node.getWorkerData().isIdle()) {
							log.debug("待移除的node:" + nh.node + ",已被分配有任务，不再移除。");
							continue;
						}
						// 告知工作节点，请shutdown自己，收到ack以后，
						shutdownNode_req(nh.node);
						shutdownNode_ack(nh.node);
						// 一般不应该有
						clientService.reAssignPendingJobQueue(nh.node
								.getWorkerData().getJobNodeId());
						// 告知工作节点所属的主机，shutdown该虚拟机
						nh.vmHost.destroy(nh.node);
					}
				}
			}
		}
	}

	/**
	 * 代表hadoop的集群情况
	 * 
	 * @author brightming
	 * 
	 */
	static class HadoopCluster {
		int numNodeManagers = 0;// nodemanager 数量
		List<ApplicationReport> applications = null;
		List<NodeReport> nodeReports = null;
		QueueInfo queueInfo = null;

	}

	/**
	 * 申请node的创建或关闭的请求
	 * 
	 * @author brightming
	 * 
	 */
	class NodeControlRequest implements Writable {
		NodeType nodeType;
		String vmName = "";
		String nodeToken = "";// 如果是创建，则要求其启动后汇报自己这个token；如果是销毁，则vm主机应该根据这个去销毁相应的vm
		int cpu = 1;
		float memory = 1024;// M
		float disk = 10 * 1024;// M

		@Override
		public void readFields(DataInput arg0) throws IOException {
			nodeType = NodeType.getByCode(WritableUtils.readString(arg0));
			vmName = WritableUtils.readString(arg0);
			nodeToken = WritableUtils.readString(arg0);
			cpu = arg0.readInt();
			memory = arg0.readFloat();
			disk = arg0.readFloat();
		}

		@Override
		public void write(DataOutput arg0) throws IOException {
			WritableUtils.writeString(arg0, nodeType.getCode());
			WritableUtils.writeString(arg0, vmName);
			WritableUtils.writeString(arg0, nodeToken);
			arg0.writeInt(cpu);
			arg0.writeFloat(memory);
			arg0.writeFloat(disk);
		}
	}

	/**
	 * vm主机的信息
	 * 
	 * @author brightming
	 * 
	 */
	class VmHostInfo {
		String addr = "";// hostname or ip
		int listenPort = 0;
		List<VmInfo> vms;// 管理的vm

		/**
		 * 获取运行状态的vm数量
		 * 
		 * @return
		 */
		public int getRunningVmCnt() {
			if (vms == null || vms.size() == 0) {
				return 0;
			}
			int cnt = 0;
			for (VmInfo vm : vms) {
				if (vm.getState().toString().contains("run")) {
					cnt++;
				}
			}
			return cnt;
		}

		public void update() {
			VmHostCtrlClient vmClient = getMyClient();

			if (vmClient == null) {
				return;
			}
			try {
				VmInfoList vmList = vmClient.listVm();
				List<VmInfo> tempvms = vmList.getVmlist();
				if (tempvms == null) {
					tempvms = new ArrayList<VmInfo>();
				}
				if (vms == null) {
					log.debug("first time get vmlist from host:" + addr);
					vms = tempvms;
				} else {
					String vname = "";
					boolean anewvm = false;
					for (VmInfo tvm : tempvms) {
						vname = tvm.getName().toString();
						// log.debug("tvm-name=" + vname);
						anewvm = true;
						for (VmInfo vm : vms) {
							// log.debug(" ---- vm-name="
							// + vm.getName().toString());
							if (vm.getName().toString().equals(vname)) {
								anewvm = false;
								if (!vm.getState().equals(tvm.getState())) {
									log.debug("state  of vm:[" + vname
											+ "] in host:[" + addr
											+ "] change from :" + vm.getState()
											+ " to " + tvm.getState());
								}
								break;
							}
						}
						if (anewvm) {
							log.debug("vm:[" + vname + "] in host:[" + addr
									+ "] is a new one! Its state="
									+ tvm.getState());
						}
					}
					vms = tempvms;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void createVm(NodeType nodeType) {
			VmCtrlRequest request = new VmCtrlRequest();
			request.setCmdType(VmCtrlCmdType.Destroy.getCode());
			request.setVmName(nodeType.getCode() + "_"
					+ UUID.randomUUID().toString().replaceAll("-", ""));
			request.setCpu(1);
			request.setMemory(512f);
			request.setNodeToken("token");
			request.setNodeType(nodeType.getCode());

			VmHostCtrlClient client = getMyClient();
			if (client == null) {
				log.error("fail to create VmHostClient !destroy request fail!");
				return;
			}
			VmCtrlResponse response;
			try {
				response = client.createVm(request);
				log.debug("finished create vm:" + request.getVmName()
						+ " in host:" + addr + ", response:" + response);
			} catch (AvroRemoteException e) {
				e.printStackTrace();
			}
		}

		public void destroy(NodeResource node) {
			if (!hasVm(node.getWorkerData().getJobNodeId().getNodeHost())) {
				log.error("node:" + node + " 不是host:[" + addr
						+ "]下面的vm,无法由该vmhost进行关闭");
			}
			VmCtrlRequest request = new VmCtrlRequest();
			request.setCmdType(VmCtrlCmdType.Destroy.getCode());
			request.setVmName(node.getWorkerData().getJobNodeId().getNodeHost());
			request.setCpu(1);
			request.setNodeToken("token");
			request.setNodeType(node.getWorkerData().getJobNodeId()
					.getNodeType().getCode());

			VmHostCtrlClient client = getMyClient();
			if (client == null) {
				log.error("fail to create VmHostClient !destroy request fail!");
				return;
			}
			VmCtrlResponse response;
			try {
				response = client.destroyVm(request);
				log.debug("finished destroy vm:" + request.getVmName()
						+ " in host:" + addr + ", response:" + response);
			} catch (AvroRemoteException e) {
				e.printStackTrace();
			}
		}

		private VmHostCtrlClient getMyClient() {
			synchronized (vmHostCtrlClients) {
				if (!vmHostCtrlClients.containsKey(addr + ":" + listenPort)) {
					vmHostCtrlClients.put(addr + ":" + listenPort,
							VmHostCtrlClient.getClient(getConfig(), addr,
									listenPort));
				}
			}
			VmHostCtrlClient vmClient = vmHostCtrlClients.get(addr + ":"
					+ listenPort);
			return vmClient;
		}

		/**
		 * 通过主机名与vm的name来定义 各虚拟机
		 * 
		 * @param hostname
		 * @return
		 */
		public boolean hasVm(String hostname) {
			if (vms != null) {
				for (VmInfo vm : vms) {
					if (vm.getName().toString().equals(hostname)) {
						return true;
					}
				}
			}
			return false;
		}

	}

	// /**
	// * vm机器的信息
	// * @author brightming
	// *
	// */
	// class VmInfo implements Writable{
	// NodeType nodeType;
	// long createTime;
	// long startTime;
	// boolean isFixed;//是否是固定不能调节的
	// String name;//vm的名称
	// String token;
	// VmState state;
	// @Override
	// public void readFields(DataInput arg0) throws IOException {
	// nodeType=NodeType.getByCode(WritableUtils.readString(arg0));
	// createTime=arg0.readLong();
	// startTime=arg0.readLong();
	// isFixed=arg0.readBoolean();
	// name=WritableUtils.readString(arg0);
	// token=WritableUtils.readString(arg0);
	// state=VmState.getByCode(WritableUtils.readString(arg0));
	// }
	// @Override
	// public void write(DataOutput arg0) throws IOException {
	// WritableUtils.writeString(arg0, nodeType.getCode());
	// arg0.writeLong(createTime);
	// arg0.writeLong(startTime);
	// arg0.writeBoolean(isFixed);
	// WritableUtils.writeString(arg0, name);
	// WritableUtils.writeString(arg0, token);
	// WritableUtils.writeString(arg0, state.getCode());
	// }
	//
	// }

	/**
	 * node的调整意见
	 * 
	 * @author brightming
	 * 
	 */
	class AdjustNodeResult {
		boolean isIncrement = true;// false 表示减少
		int cnt = 0;
		List<VmNodeWithHost> nodeWithHost;
	}

	class VmNodeWithHost {
		NodeResource node;
		VmHostInfo vmHost;

		public VmNodeWithHost(NodeResource n, VmHostInfo host) {
			this.node = n;
			this.vmHost = host;
		}

	}

	/**
	 * 周期性检测vm host上的资源的线程
	 * 
	 * @author brightming
	 * 
	 */
	class VMResourceMonitorThread extends Thread {
		boolean run = true;

		@Override
		public void run() {
			run = true;
			while (run) {
				for (VmHostInfo vh : vmHosts.values()) {
					vh.update();
				}

				try {
					Thread.sleep(30 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
