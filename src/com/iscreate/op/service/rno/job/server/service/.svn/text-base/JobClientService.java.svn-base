package com.iscreate.op.service.rno.job.server.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.service.AbstractService;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.log4j.Logger;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.service.rno.job.ActionType;
import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.op.service.rno.job.common.KillResponseType;
import com.iscreate.op.service.rno.job.common.NodeResource;
import com.iscreate.op.service.rno.job.event.IsAsyncDispatcher;
import com.iscreate.op.service.rno.job.event.JobEvent;
import com.iscreate.op.service.rno.job.event.JobEventType;
import com.iscreate.op.service.rno.job.protocol.JobClientProtocol;
import com.iscreate.op.service.rno.job.request.DowngradeRequest;
import com.iscreate.op.service.rno.job.request.DowngradeResponse;
import com.iscreate.op.service.rno.job.request.GetAllJobsResponse;
import com.iscreate.op.service.rno.job.request.GetAllNodeMetricsResponse;
import com.iscreate.op.service.rno.job.request.GetJobDetailResponse;
import com.iscreate.op.service.rno.job.request.KillJobRequest;
import com.iscreate.op.service.rno.job.request.KillJobResponse;
import com.iscreate.op.service.rno.job.request.SubmitJobRequest;
import com.iscreate.op.service.rno.job.request.SubmitJobResponse;
import com.iscreate.op.service.rno.job.server.JobProgressData;
import com.iscreate.op.service.rno.job.server.NodeResourceState;
import com.iscreate.op.service.rno.job.server.impl.JMContext;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class JobClientService extends AbstractService implements
		JobClientProtocol {

	private static Logger log = Logger.getLogger(JobClientService.class);
	private InetSocketAddress serverAddr;
	private Server server = null;
	private JMContext context = null;
	private Connection connection = null;
	private Statement stmt = null;
	private RnoJobDao jobDao = new RnoJobDaoImpl();

	// job先加入“新加入的job”，以及“所有的job”队列，然后异步处理：
	// 如果能找到资源处理，则从newJobs中移除，加入到pendingLaunchJobs队列，等相应的node节点过来的时候，领取该job
	// 如果由于找不到合理的资源，则放入到lackingResourceNewJob队列，从newJobs队列移除。
	// 当某个node心跳信息过来的时候，从pendingLaunchJobs领取自己的任务，
	// job将从pendingLaunchJobs移动到nodeToJobs队列。

	// 所有的工作
	Map<Long, JobProfile> allJobs = new ConcurrentHashMap<Long, JobProfile>();// key=jobid

	// 新加入的job
	List<JobProfile> newJobs = new ArrayList<JobProfile>();
	// 由于暂时没有资源，不能执行的job
	List<JobEvent> lackingResourceNewJobs = Collections.synchronizedList(new ArrayList<JobEvent>());

	// ----下面的队列都是一对的，为了从2个角度快速找到数据，在进行同步的时候，取主的进行同步就可以了----//
	// 给某个主机的待领取的任务:launch,stop
	Map<JobNodeId, List<PendingJob>> pendingAdoptJobs = new ConcurrentHashMap<JobNodeId, List<PendingJob>>();
	Map<Long, PendingJob> pendingJobsToNodes = new ConcurrentHashMap<Long, PendingJob>();

	// 各node已经领取的工作，通过心跳包领取
	Map<JobNodeId, List<JobProfile>> nodeToJobs = new ConcurrentHashMap<JobNodeId, List<JobProfile>>();
	// 已经运行的job与node的对应关系
	Map<Long, NodeResource> jobRunningInNodes = new ConcurrentHashMap<Long, NodeResource>();

	// 事件分发
	private IsAsyncDispatcher dispatch = null;
	private NodeResourceManagerService nodeResMgrService = null;

	// /---------------beg get/set---------------//
	public Map<Long, JobProfile> getAllJobs() {
		return allJobs;
	}

	public List<JobProfile> getNewJobs() {
		return newJobs;
	}

	public List<JobEvent> getLackingResourceNewJobs() {
		return lackingResourceNewJobs;
	}

	public Map<JobNodeId, List<PendingJob>> getPendingAdoptJobs() {
		return pendingAdoptJobs;
	}

	public Map<Long, PendingJob> getPendingJobsToNodes() {
		return pendingJobsToNodes;
	}

	public Map<JobNodeId, List<JobProfile>> getNodeToJobs() {
		return nodeToJobs;
	}

	public IsAsyncDispatcher getDispatch() {
		return dispatch;
	}

	// /---------------end get/set---------------//

	public JobClientService(JMContext context) {
		this("JobClientService", context);
	}

	public JobClientService(String name, JMContext context) {
		super(name);
		this.context = context;
		dispatch = context.getDispatcher();
		nodeResMgrService = context.getManagerNode().getNodeResManagerService();
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		log.debug("JobClientService serviceIniting...");
		serverAddr = NetUtils.createSocketAddrForHost(conf.get(
				IsConfiguration.JobNodeManagerHost,
				IsConfiguration.JobNodeManagerHost_default), conf.getInt(
				IsConfiguration.JobNodeManagerClientPort,
				IsConfiguration.JobNodeManagerClientPort_default));
		super.serviceInit(conf);

		// 准备数据库连接
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		stmt = connection.createStatement();

		log.debug("JobClientService serviceInited");
	}

	@Override
	protected void serviceStart() throws Exception {
		log.debug("JobClientService serviceStarting...");

		RPC.Builder builder = new RPC.Builder(super.getConfig());
		builder.setProtocol(JobClientProtocol.class);
		builder.setInstance(this);
		builder.setPort(serverAddr.getPort());
		builder.setBindAddress(serverAddr.getHostString());
		server = builder.build();
		server.start();
		// server.join();

		super.serviceStart();
		log.debug("JobClientService serviceStarted");
	}

	@Override
	protected void serviceStop() throws Exception {
		server.stop();
		// try {
		// stmt.close();
		// } catch (Exception e) {
		// }
		// try {
		// connection.close();
		// } catch (Exception e) {
		// }
		super.serviceStop();
	}

	@Override
	public ProtocolSignature getProtocolSignature(String protocol,
			long clientVersion, int clientMethodsHash) throws IOException {
		return ProtocolSignature.getProtocolSignature(this, protocol,
				clientVersion, clientMethodsHash);
	}

	@Override
	public long getProtocolVersion(String arg0, long arg1) throws IOException {
		return JobClientProtocol.versionID;
	}

	@Override
	public SubmitJobResponse submitJob(SubmitJobRequest request) {
		log.debug("server recived submitJob,request=" + request);
		JobProfile job = request.getJobProfile();
		job.setSubmitTime(new Date());
		SubmitJobResponse response = new SubmitJobResponse();
		response.setJobProfile(job);
		response.setAccepted(true);

		// 保存job获取数据库id
		job = jobDao.addOneJob(stmt, request.getJobProfile());
		log.debug("保存到数据库后，job=" + job);

		if (job.getJobId() == null) {
			log.error("job 提交失败！");
			response.setAccepted(false);
			return response;
		} else {
			response.setAccepted(true);
		}

		job.modifyJobState(JobState.Initiate, "已初始化");
		// 加入到新工作队列
		synchronized (newJobs) {
			newJobs.add(job);
		}
		synchronized (allJobs) {
			allJobs.put(job.getJobId(), job);
		}

		// 通知有新的job加入
		log.debug("JobClientService 通知有新的job事件 。thread="
				+ Thread.currentThread().getId());
		JobEvent jobEvent = new JobEvent(JobEventType.Added, job);
		dispatch.getEventHandler().handle(jobEvent);

		// 返回结果
		return response;
	}

	// 是否在分配给某个node的队列里，尚未被领取
	// 是否在newjob里
	// 是否在缺乏资源的队列里
	// 是否已经被分配出去
	@Override
	public KillJobResponse killJob(KillJobRequest request) {
		KillJobResponse resp = new KillJobResponse();

		if (request == null) {
			log.error("killJob的请求是空的！");
			resp.setResponseType(KillResponseType.Rejected);
			resp.setJobStatus(null);
			return resp;
		}
		JobProfile job = request.getJob();
		if (job == null) {
			log.error("killJob的请求里的job内容是空的！");
			resp.setResponseType(KillResponseType.Rejected);
			resp.setJobStatus(null);
			return resp;
		}

		// 如果已经正在killing，则忽略此请求
		JobProfile jobT = allJobs.get(job.getJobId());
		if (jobT == null) {
			log.error("killJob的请求里的job在系统中不存在！");
			resp.setResponseType(KillResponseType.Rejected);
			resp.setJobStatus(null);
			return resp;
		}
		if (jobT.getJobStatus().getJobState() == JobState.Killing) {
			log.debug("已经正在killing了，不必重复提交请求。");
			resp.setResponseType(KillResponseType.Killing);
			resp.setJobStatus(jobT.getJobStatus());
			return resp;
		}

		jobT.modifyJobState(JobState.Killing, "正在停止...");
		// pending队列
		synchronized (pendingAdoptJobs) {
			if (pendingJobsToNodes.containsKey(job.getJobId())) {
				resp = killJobInPending(job);
				return resp;
			}
		}

		// 是否在newjob里,这种情况可能产生的情况就是：刚提交了job，就马上要kill掉，
		boolean isNew = false;
		synchronized (newJobs) {
			if (newJobs.contains(job)) {
				isNew = true;
				newJobs.remove(job);
			}
		}
		if (isNew) {
			job = allJobs.remove(job.getJobId());
			resp.setJobStatus(job.getJobStatus());
			resp.setResponseType(KillResponseType.Killed);
			log.debug("job=" + job + "在newjob队列，已经kill");
			return resp;
		}

		// 是否在缺乏资源的队列里
		boolean isInLack = false;
		synchronized (lackingResourceNewJobs) {
			if (lackingResourceNewJobs.contains(new JobEvent(
					JobEventType.Added, job))) {
				lackingResourceNewJobs.remove(new JobEvent(JobEventType.Added,
						job));
				isInLack = true;
			}
		}
		if (isInLack) {
			job = allJobs.remove(job.getJobId());
			resp.setJobStatus(job.getJobStatus());
			resp.setResponseType(KillResponseType.Killed);
			log.debug("job=" + job + "在lackingResourceNewJobs队列，已经kill");
			return resp;
		}

		// 已经被node领取并运行
		NodeResource node = null;
		synchronized (jobRunningInNodes) {
			node = jobRunningInNodes.get(job.getJobId());
		}
		if (node == null) {
			log.error("指定的job不存在任何node的工作队列里！无法执行kill操作");
			resp.setResponseType(KillResponseType.Rejected);
			resp.setJobStatus(null);
			return resp;
		}
		// 存在的话，需要构建kill指令
		PendingJob killCmd = new PendingJob(
				new JobAction(ActionType.Stop, job), node);
		synchronized (pendingAdoptJobs) {
			List<PendingJob> pjs = pendingAdoptJobs.get(node.getWorkerData()
					.getJobNodeId());
			if (pjs == null) {
				pjs = new ArrayList<PendingJob>();
				pendingAdoptJobs.put(node.getWorkerData().getJobNodeId(), pjs);
			}
			pjs.add(killCmd);
			// 另一端的映射关系
			pendingJobsToNodes.put(job.getJobId(), killCmd);
		}
		log.debug("queue the killjob request in the pending queue.");
		resp.setResponseType(KillResponseType.Killing);
		allJobs.get(job.getJobId()).getJobStatus()
				.setJobState(JobState.Killing);
		resp.setJobStatus(allJobs.get(job.getJobId()).getJobStatus());

		// 同步处理
		return resp;
	}

	/**
	 * 将pending队列里的job进行kill 进入此方法，是已经加锁的，并且确定已经存在
	 * 
	 * @param job
	 * @return
	 */
	private KillJobResponse killJobInPending(JobProfile job) {
		KillJobResponse resp = new KillJobResponse();
		// 在pending队列
		PendingJob pj = pendingJobsToNodes.get(job.getJobId());

		// 已存在kill的要求
		if (pj.getJobAction().getActionType() == ActionType.Stop) {
			// 已经发出了该stop指令，不需要理会。
			resp.setResponseType(KillResponseType.Killing);
			resp.setJobStatus(allJobs.get(job.getJobId()).getJobStatus());
			return resp;
		}

		// 如果存在的是“等待launch的指令”，则可以移除；
		if (pj.getJobAction().getActionType() == ActionType.Launch) {
			List<PendingJob> pjobs = pendingAdoptJobs.get(pj.getNode()
					.getWorkerData().getJobNodeId());
			// 将预先占用的资源减1
			pj.getNode().getWorkerData().releaseJobSlot(job.getJobType());
			pjobs.remove(pj);
			pendingJobsToNodes.remove(pj.getJobAction().getJobProfile()
					.getJobId());

			JobStatus js = allJobs.get(job.getJobId()).getJobStatus();
			js.setJobState(JobState.Killed);
			resp.setResponseType(KillResponseType.Killed);
			resp.setJobStatus(js);
			log.debug("job=" + job + "已经被移除。。。");

			// 其他队列
			allJobs.remove(pj.getJobAction().getJobProfile());// 从总工作队列中移除
			return resp;
		}
		return resp;
	}

	@Override
	public DowngradeResponse downgrade(DowngradeRequest request) {
		return null;
	}
	
	/**
	 * 获取job的最大等待时间
	 * @return
	 */
	public long getJobMaxWaitingTime(){
		long res=0,t=0;
		synchronized(lackingResourceNewJobs){
			long now=new Date().getTime();
			for(JobEvent je:lackingResourceNewJobs){
				t=now-je.getJobProfile().getSubmitTime().getTime();
				if(t>res){
					res=t;
				}
			}
		}
		return res;
	}
	
	
	/**
	 * 排队等候资源的job的数量
	 * @return
	 */
	public int getLackResJobCnt(){
		return lackingResourceNewJobs.size();
	}
	
	public int getAllJobsCnt(){
		return allJobs.size();
	}

	public static void main(String[] args) {
		JobClientService service = new JobClientService(null);
		service.init(new IsConfiguration());
		service.start();
		System.out.println("service 在监听 。。。。。");
	}

	/**
	 * 等待队列的封装类
	 * 
	 * @author brightming
	 *
	 */
	static class PendingJob {
		// ActionType actionType;
		// JobProfile job;
		JobAction jobAction;
		NodeResource node;

		PendingJob(JobAction jobAction, NodeResource node) {
			this.jobAction = jobAction;
			this.node = node;
		}

		public JobAction getJobAction() {
			return jobAction;
		}

		public void setJobAction(JobAction jobAction) {
			this.jobAction = jobAction;
		}

		public NodeResource getNode() {
			return node;
		}

		public void setNode(NodeResource node) {
			this.node = node;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((jobAction == null) ? 0 : jobAction.hashCode());
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PendingJob other = (PendingJob) obj;
			if (jobAction == null) {
				if (other.jobAction != null)
					return false;
			} else if (!jobAction.equals(other.jobAction))
				return false;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			return true;
		}

	}

	public static class JobEventDispatcher implements EventHandler<JobEvent> {
		private static Logger log = Logger.getLogger(JobEventDispatcher.class);
		JobClientService service;
		NodeResourceManagerService resource;

		public JobEventDispatcher(JMContext context) {
			this.service = context.getManagerNode().getJobClientService();
			resource = context.getManagerNode().getNodeResManagerService();
			log.debug("service==" + service + ",resourceManager=" + resource);
		}

		@Override
		public void handle(JobEvent arg0) {
			log.debug("JobEventDispatcher handle event.Thread="
					+ Thread.currentThread().getId());
			JobProfile job = arg0.getJobProfile();

			if (job == null) {
				// 检查因缺资源而挂起的队列
				// 由以下事件触发：
				// 1、节点注册
				// 2、节点汇报有job完成：正常完成，或kill完成
				service.checkLackingResourceJobQueue();
				return;
			}
			if (arg0.getType() == JobEventType.Added) {
				// 从newJobs里移除
				log.debug("in handle method,service==" + service);
				synchronized (service.newJobs) {
					if (!service.newJobs.contains(job)) {
						// 可能是刚提交就要求kill掉
						log.debug("该job不在newjob队列里，忽略。");
						return;
					}
					service.newJobs.remove(job);
				}

				// 向资源管理器找资源
				NodeResource node = resource
						.popCapabilityNode(job.getJobType());
				log.debug("get node :" + node + " for job:" + job);
				if (node == null) {
					job.modifyJobState(JobState.Pending, "等待资源中");
					log.warn("当前无资源可以处理该job:" + job + ",将延迟处理");
					synchronized (service.lackingResourceNewJobs) {
						service.lackingResourceNewJobs.add(arg0);
					}
				} else {
					// 分配给某个node
					job.modifyJobState(JobState.Queue, "等待加载中");
					service.assignJobToNode(job, node);
				}
			} else {
				// 不会进入这里。。。
				// 需要kill TODO
			}

		}

	}

	/**
	 * 具体将job关联给某个node
	 * 
	 * @param job
	 * @param node
	 */
	private void assignJobToNode(JobProfile job, NodeResource node) {
		log.debug("---assignJobToNode . job=" + job.getJobId() + ",node="
				+ node.getWorkerData().getJobNodeId());
		synchronized (getPendingAdoptJobs()) {
			List<PendingJob> assignedjobs = pendingAdoptJobs.get(node
					.getWorkerData().getJobNodeId());
			if (assignedjobs == null) {
				assignedjobs = new ArrayList<PendingJob>();
				pendingAdoptJobs.put(node.getWorkerData().getJobNodeId(),
						assignedjobs);
			}
			// 加入到待分配
			PendingJob pendJob = new PendingJob(new JobAction(
					ActionType.Launch, job), node);
			assignedjobs.add(pendJob);
			// 建立映射关系
			pendingJobsToNodes.put(job.getJobId(), pendJob);
		}
	}

	/**
	 * 检查缺资源而陷入等待的job
	 */
	public void checkLackingResourceJobQueue() {
		log.debug("checkLackingResourceJobQueue。。。。。");
		if (getLackingResourceNewJobs().size() > 0) {
			List<JobEvent> handledEvents = new ArrayList<JobEvent>();
			synchronized (getLackingResourceNewJobs()) {
				NodeResource node;
				JobProfile job;
				for (JobEvent je : getLackingResourceNewJobs()) {
					job = je.getJobProfile();
					node = context.getManagerNode().getNodeResManagerService()
							.popCapabilityNode(job.getJobType());
					log.debug("get node :" + node + " for job:" + job);
					if (node != null) {
						// 分配
						assignJobToNode(job, node);
						handledEvents.add(je);
					}
				}
				log.debug("处理了lackingResourceNewJobs的" + handledEvents.size()
						+ "个job");
				if (handledEvents.size() > 0) {
					getLackingResourceNewJobs().removeAll(handledEvents);
				}
			}
		}
	}

	@Override
	public GetAllJobsResponse getAllOnGoingJobs() {
		GetAllJobsResponse resp = new GetAllJobsResponse();
		List<JobProfile> jobs = new ArrayList(allJobs.values());
		List<GetJobDetailResponse> jobDets = new ArrayList<GetJobDetailResponse>();
		if (jobs != null && jobs.size() > 0) {
			GetJobDetailResponse jobDet;
			for (JobProfile j : jobs) {
				jobDet = new GetJobDetailResponse();
				jobDet.setJob(j);
				// find node
				jobDet.setNode(jobRunningInNodes.get(j.getJobId()));

				jobDets.add(jobDet);

			}
		}
		resp.setJobs(jobDets);
		return resp;
	}

	@Override
	public GetAllNodeMetricsResponse getAllNodeMetrics() {
		GetAllNodeMetricsResponse resp = new GetAllNodeMetricsResponse();
		List<NodeResource> nodes = new ArrayList(
				context.getManagerNode().getNodeResManagerService().getAllNodes());
		resp.setNodes(nodes);
		return resp;
	}

	@Override
	public NodeResource getManagerNode() {
		NodeResource node = new NodeResource();
		node.setState(NodeResourceState.Alive);
		JobWorkerStatus jws = new JobWorkerStatus();
		jws.setJobNodeId(context.getManagerNode().getJobNodeId());
		node.setWorkerData(jws);
		return node;
	}

	@Override
	public GetJobDetailResponse getJobInfo(long jobId) {
		log.debug("getJobInfo-- jobId=" + jobId);
		JobProfile job = allJobs.get(jobId);
//		log.debug("get job:" + jobId + " from allJobs is == " + job);
		GetJobDetailResponse resp = new GetJobDetailResponse();
		resp.setJob(job);
		resp.setNode(jobRunningInNodes.get(jobId));
		return resp;
	}

	/**
	 * 一个node过来领取job了 heartbeat的时候，携带的是normal类型。具体类型参考HeartbeatEventType
	 * 
	 * @param jobNodeId
	 * @return
	 */
	public List<JobAction> takePendingJobForNode(JobNodeId jobNodeId) {
		log.debug("node come to get pending job :" + jobNodeId);
		List<JobAction> jas = new ArrayList<JobAction>();
		// 用于建立job与node的关系
		List<JobProfile> myJobs = null;
		synchronized (nodeToJobs) {
			myJobs = nodeToJobs.get(jobNodeId);
			if (myJobs == null) {
				myJobs = Collections
						.synchronizedList(new ArrayList<JobProfile>());
				nodeToJobs.put(jobNodeId, myJobs);
			}
		}
		synchronized (pendingAdoptJobs) {
			List<PendingJob> pendJs = pendingAdoptJobs.get(jobNodeId);
			if (pendJs != null && pendJs.size() > 0) {
				for (PendingJob pj : pendJs) {
					jas.add(pj.getJobAction());
					// 移除
					pendingJobsToNodes.remove(pj.getJobAction().getJobProfile()
							.getJobId());
					// 加入队列nodeToJobs，建立映射关系
					if (pj.getJobAction().getActionType() == ActionType.Launch) {
						pj.getJobAction().getJobProfile().modifyJobState(JobState.Launched, "已被领取");
						myJobs.add(pj.getJobAction().getJobProfile());
						log.debug("新增node与job的对应关系：node="
								+ pj.getNode().getWorkerData().getJobNodeId()
								+ ",job="
								+ pj.getJobAction().getJobProfile().getJobId());
					}
					// 如果是新增的job，不是kill类型的
					if (pj.getJobAction().getActionType() == ActionType.Launch) {
						jobRunningInNodes.put(pj.getJobAction().getJobProfile()
								.getJobId(), pj.getNode());
					}
				}
				// 移除掉这个node的所有的pending
				pendingAdoptJobs.remove(jobNodeId);
			}
		}

		log.debug("node:" + jobNodeId.getNodeHost()+ " 领取的job数量："
				+ jas.size());
		return jas;
	}

	/**
	 * 处理随着heartbeat带过来的job的进度信息
	 * 
	 * @param progressData
	 */
	public void updateJobProgress(JobProgressData data) {
		log.debug("最终处理heartbeat带来的job的进度信息,data=" + data);
		if (data == null) {
			return;
		}

		JobNodeId jnid = data.getJobNodeId();
		NodeResource node = nodeResMgrService.getNode(jnid);

		// 同步？？？TODO
		// 由该node负责的job
		List<JobProfile> myJobs = nodeToJobs.get(jnid);

		// 正在进行的
		List<JobStatus> running = data.getRunningJobs();
		if (running != null && running.size() > 0) {
			// 逐个更新进度
			log.debug("有job需要更新running进度。。。");
			// 更新涉及的job的进度情况
			JobProfile tmpUse = null;
			for (JobStatus js : running) {
				tmpUse = allJobs.get(js.getJobId());
				if (myJobs.contains(tmpUse)) {
					// 合法的，更新进度
					tmpUse.getJobStatus().update(js);
				} else {
					log.warn("非法的job进度更新请求！job：" + js + " 不是node负责的！node="
							+ node);// 不是该node负责的job，不能由它来负责更新进度
					continue;
				}
			}
		}
		
		// ---已经完成的，包括：finished、killed、failed----//
		List<JobStatus> finished = data.getFinishedJobs();
		if (finished == null) {
			finished = new ArrayList<JobStatus>();
		}
		if (finished != null && finished.size() > 0) {
			log.debug("有已经完成的job。。。");
			// 更新所有的job的进度情况，释放占用的node的资源
			JobProfile tmpUse = null;
			for (JobStatus js : finished) {
				tmpUse = allJobs.get(js.getJobId());
				if (myJobs.contains(tmpUse)) {
					// 合法的
					// 释放占用的资源
					node.getWorkerData().releaseJobSlot(
							allJobs.get(js.getJobId()).getJobType());
					// 将job从系统中移除
					myJobs.remove(tmpUse);
					// job对应的node数据
					jobRunningInNodes.remove(js.getJobId());
				} else {
					log.warn("非法的job进度更新请求！job：" + js + " 不是node负责的！node="
							+ node);// 不是该node负责的job，不能由它来负责更新进度
					continue;
				}
			}

			// 统一进行检查，是否有job已经可以从lack队列进行分配了
			checkLackingResourceJobQueue();
		}

	}

	/**
	 * 当node需要shutdown的时候，需要对pending的job进行处理： 1、对于要求其lanuch的job，需要重新考虑分派新的节点；
	 * 2、对于要求其stop的job，可以正常发送过去
	 * 
	 * @param jnid
	 */
	public List<JobAction> reAssignPendingJobQueue(JobNodeId jnid) {
		if (jnid == null) {
			return null;
		}
		List<JobAction> jas = new ArrayList<JobAction>();
		List<PendingJob> pends = null;
		synchronized (pendingAdoptJobs) {
			// 尽快释放锁
			pends = pendingAdoptJobs.remove(jnid);
		}
		List<JobProfile> needToReAssignJobs = new ArrayList<JobProfile>();
		if (pends != null && pends.size() > 0) {
			for (PendingJob pj : pends) {
				if (pj.getJobAction().getActionType() == ActionType.Stop) {
					jas.add(pj.getJobAction());
				} else {
					pj.getJobAction().getJobProfile()
							.modifyJobState(JobState.Initiate, "重新调度");
					needToReAssignJobs.add(pj.getJobAction().getJobProfile());
				}
			}
		}

		if (needToReAssignJobs != null && needToReAssignJobs.size() > 0) {
			// 重新加入newJobs队列
			synchronized (newJobs) {
				newJobs.addAll(0, needToReAssignJobs);
			}
			// 是否需要优化一下？
			JobEvent jobEvent = null;
			for (JobProfile job : needToReAssignJobs) {
				jobEvent = new JobEvent(JobEventType.Added, job);
				dispatch.getEventHandler().handle(jobEvent);
			}
		}
		log.debug("节点：" + jnid + " 请求shutdown，造成：" + needToReAssignJobs.size()
				+ "个launch的job需要重新调度，" + jas.size()
				+ "个stop的job照常发送给该节点，要求其stop后回复ack");
		return jas;
	}
}
