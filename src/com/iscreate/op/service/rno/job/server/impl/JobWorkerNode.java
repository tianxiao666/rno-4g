package com.iscreate.op.service.rno.job.server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.service.rno.job.ActionType;
import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobCapacity;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.client.JobRunnableManager;
import com.iscreate.op.service.rno.job.client.SimpleJobRunnableMgr;
import com.iscreate.op.service.rno.job.client.api.WorkerToManagerNodeClient;
import com.iscreate.op.service.rno.job.common.JobNodeState;
import com.iscreate.op.service.rno.job.common.NodeType;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.op.service.rno.job.common.ListenPortType;
import com.iscreate.op.service.rno.job.request.HeartbeatEventType;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatRequest;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatResponse;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeRequest;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeResponse;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class JobWorkerNode extends JobNode {

	private static Logger log = Logger.getLogger(JobWorkerNode.class);

	private JobRunningContext context;
	private WorkerToManagerNodeClient workerToManagerClient;

	private JobWorkerStatus workerNodeData;
	private int forMangerCtrlPort = IsConfiguration.JobNodeWorkerControlPort_default;

	private long heartbeatTimeInteval = 10000;// 30s
	private HeartbeatThread heartbeatThread = null;

	private long hasEndJob = 0;
	// 内部线程池
	private static ExecutorService executor = null;
	private int RunnableCnt = 5;
	private int MaxAllowedRunnableCnt = 30;// 默认最大30个runnable线程

	// 后台通信线程
	private LaunchJobThread launchJobThread;

	// flush线程设置
	private int flushInterval = 10000;// 30s
	private FlushThread flushThread;

	// 相应的锁对象
	Object WaitHeartbeatObj = new Object();
	Object WaitLaunchObj = new Object();

	Class defaultJobRunnableMgrCls = null;
	// 关联 jobrunnablemanager
	Map<Class, JobRunnableManager> jobRunManagers = Collections
			.synchronizedMap(new HashMap<Class, JobRunnableManager>());
	// job与jobrunnable的对应关系
	Map<Long, JobRunnable> jobToRunnables = new ConcurrentHashMap<Long, JobRunnable>();

	// 正常运行的job
	Map<Long, JobProfile> runningJobs = Collections
			.synchronizedMap(new HashMap<Long, JobProfile>());
	// 搞定了的job，包括killed，failed，finished
	List<JobProfile> completedJobs = Collections
			.synchronizedList(new ArrayList<JobProfile>());

	// 等待处理的job
	Map<Long, LaunchActions> wait4Actionjobs = Collections
			.synchronizedMap(new HashMap<Long, LaunchActions>());

	// 从runnable反馈上来的job进度
	// 当heartbeat处理完后，会清除掉
	Map<Long, JobStatus> jobProgresses = Collections
			.synchronizedMap(new HashMap<Long, JobStatus>());

	// 等待flush的jobreport
	List<JobReport> needFlushJobReport = Collections
			.synchronizedList(new ArrayList<JobReport>());

	//
	RnoJobDao jobDao = new RnoJobDaoImpl();

	private boolean shutdownComplete = false;// 是否shutdown完成

	public JobWorkerNode() {
		this("JobWorkerNode");
	}

	public JobWorkerNode(String name) {
		super(name);
		String host="";
		try{
		InetAddress ia = InetAddress.getLocalHost();   
		host = ia.getHostName();//获取计算机主机名  
		}catch(Exception e){
			e.printStackTrace();
			host=NetUtils.getHostname();
		}
		jobNodeId = new JobNodeId("JobWorkerNode-"+host, host,
				NodeType.WorkerNode);
	}

	@Override
	public void init(Configuration arg0) {
		super.init(arg0);

		// 初始化装载能力
		Map<String, JobCapacity> jobCapacities = prepareJobCapacity(arg0);
		log.debug("job capacity is " + jobCapacities);

		// 心跳通信间隔
		heartbeatTimeInteval = arg0.getLong(IsConfiguration.HeartbeatInterval,
				IsConfiguration.HeartbeatInterval_default);

		workerNodeData = new JobWorkerStatus();
		workerNodeData.setJobNodeId(jobNodeId);

		Map<ListenPortType, Integer> listenPorts = new HashMap<ListenPortType, Integer>();
		forMangerCtrlPort = arg0.getInt(
				IsConfiguration.JobNodeManagerWorkerPort,
				IsConfiguration.JobNodeWorkerControlPort_default);
		listenPorts.put(ListenPortType.WorkerNodeCtrlPort, forMangerCtrlPort);
		workerNodeData.setListenPorts(listenPorts);
		workerNodeData.setJobCapacities(jobCapacities);

		// runnable 最高数量的限制
		MaxAllowedRunnableCnt = arg0.getInt(
				IsConfiguration.MaxAllowedRunnableCnt,
				IsConfiguration.MaxAllowedRunnableCnt_default);
		if (MaxAllowedRunnableCnt <= 0) {
			MaxAllowedRunnableCnt = IsConfiguration.MaxAllowedRunnableCnt_default;
		}
		//
		context = new JobRunningContext();
		context.setWorkerNode(this);
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		Connection connection = DataSourceConn.initInstance().getConnection();
		context.setConn(connection);
		try {
			Statement stmt = connection.createStatement();
			context.setStmt(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 准备job 能力相关的数据
	 * 
	 * @param conf
	 */
	private Map<String, JobCapacity> prepareJobCapacity(Configuration conf) {
		// 提供一个默认的jobrunnablemgr
		log.debug("prepareJobCapacity----");
		defaultJobRunnableMgrCls = SimpleJobRunnableMgr.class;
		jobRunManagers.put(SimpleJobRunnableMgr.class,
				new SimpleJobRunnableMgr());
		return createNodeCapacity(conf);
	}

	private Map<String, JobCapacity> createNodeCapacity(Configuration arg0) {
		log.debug("createNodeCapacity-----");
		Map<String, JobCapacity> jobCapacities = new HashMap<String, JobCapacity>();
		String capacityFile = arg0.get(IsConfiguration.JobnodeCapacityFile,
				IsConfiguration.JobnodeCapacityFile_default);
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(capacityFile);

		Document doc = null;

		doc = parseXml(is);

		jobCapacities = readJobCapacityFromXml(doc);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int tmp = 0;
		for (JobCapacity jc : jobCapacities.values()) {
			tmp += jc.getMaxSlots();
		}
		if (tmp > RunnableCnt) {
			RunnableCnt = tmp;
		}
		if (RunnableCnt > MaxAllowedRunnableCnt) {
			RunnableCnt = MaxAllowedRunnableCnt;
		}
		return jobCapacities;
	}

	/**
	 * 从分解出的xml doc中获取job capacity
	 * 
	 * @param doc
	 */
	private Map<String, JobCapacity> readJobCapacityFromXml(Document doc) {
		Map<String, JobCapacity> jobCapacities = new HashMap<String, JobCapacity>();

		Element root = doc.getDocumentElement();
		NodeList nodeLists = root.getChildNodes();
		if (nodeLists != null && nodeLists.getLength() > 0) {
			String jobType = null, maxSlot = null, runnable = null, runnableMgr = null;
			for (int ni = 0; ni < nodeLists.getLength(); ni++) {
				Node node = nodeLists.item(ni);
				log.debug("-xml 一级子节点：" + node.getNodeName() + ":"
						+ node.getLocalName());
				NodeList childs = node.getChildNodes();
				if (childs.getLength() <= 0) {
					continue;
				}
				jobType = null;
				maxSlot = null;
				runnable = null;
				runnableMgr = null;
				Class mgrCls = null;
				for (int ci = 0; ci < childs.getLength(); ci++) {
					Node child = childs.item(ci);
					log.debug("-xml nodeName:" + child.getNodeName()
							+ " localName=" + child.getLocalName() + " value="
							+ child.getTextContent());
					if ("type".equals(child.getLocalName())) {
						jobType = child.getTextContent();
					} else if ("maxSlot".equals(child.getLocalName())) {
						maxSlot = child.getTextContent();
					} else if ("runnable".equals(child.getLocalName())) {
						runnable = child.getTextContent();
					} else if ("runnableMgr".equals(child.getLocalName())) {
						runnableMgr = child.getTextContent().trim();
					}
				}
				log.debug("type=" + jobType + ",maxSlot=" + maxSlot
						+ ",runnable=" + runnable + ",mgr=" + runnableMgr);
				//
				if (jobType == null || "".equals(jobType.trim())
						|| runnable == null || "".equals(runnable.trim())) {
					continue;
				}
				int ms = Integer.parseInt(maxSlot);
				if (ms <= 0) {
					continue;
				}
				if (runnableMgr == null || "".equals(runnableMgr.trim())) {
					// 用默认的
					mgrCls = defaultJobRunnableMgrCls;
				} else {
					try {
						mgrCls = Class.forName(runnableMgr);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						mgrCls = defaultJobRunnableMgrCls;
					}
				}
				JobRunnableManager mgr = jobRunManagers.get(mgrCls);
				if (mgr == null) {
					try {
						Constructor deCont = mgrCls
								.getConstructor(new Class[] {});
						Object obj = deCont.newInstance(new Object[] {});
						if (obj instanceof JobRunnableManager) {
							mgr = (JobRunnableManager) obj;
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					if (mgr != null) {
						jobRunManagers.put(mgrCls, mgr);
					}
				}
				if (mgr != null) {
					mgr.addJobRunnableCls(jobType, runnable);
				}
				JobCapacity jc = new JobCapacity();
				jc.setCurSlots(0);
				jc.setJobType(jobType);
				jc.setMaxSlots(ms);

				jobCapacities.put(jobType, jc);
			}
		}

		return jobCapacities;

	}

	private Document parseXml(InputStream is) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		docBuilderFactory.setIgnoringComments(true);

		docBuilderFactory.setNamespaceAware(true);
		try {
			docBuilderFactory.setXIncludeAware(true);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
		DocumentBuilder builder = null;
		try {
			builder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return null;
		}

		Document doc = null;
		try {
			doc = builder.parse(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}catch (SAXException e2) {
			e2.printStackTrace();
		}
		return doc;
	}

	@Override
	protected void serviceInit(Configuration conf) throws Exception {
		log.debug("serviceIniting...");
		workerToManagerClient = WorkerToManagerNodeClient
				.createWorkerToManagerNodeClient(conf);
		addService(workerToManagerClient);

		//
		heartbeatThread = new HeartbeatThread();
		heartbeatThread.run = true;

		launchJobThread = new LaunchJobThread();
		launchJobThread.run = true;

		flushThread = new FlushThread();
		flushThread.run = true;

		executor = Executors.newFixedThreadPool(RunnableCnt);

		super.serviceInit(conf);
		log.debug("serviceInited.");
	}

	@Override
	protected void serviceStart() throws Exception {
		log.debug("serviceStarting...");
		try {
			curState = JobNodeState.WorkerServerStarting;
			super.serviceStart();

		} catch (Exception e) {
			curState = JobNodeState.OutOfService;
			throw e;
		}

		// 注册自己
		try {
			log.debug("registering...");
			curState = JobNodeState.WorkerRegistering;
			RegisterWorkerNodeRequest request = new RegisterWorkerNodeRequest();
			request.setWorkerNodeData(workerNodeData);
			RegisterWorkerNodeResponse response = workerToManagerClient
					.registerWorkerNode(request);
			log.debug("register finished. response=" + response);
		} catch (Exception e) {
			curState = JobNodeState.OutOfService;
			throw e;
		}

		// 启动周期通信线程
		heartbeatThread.start();
		launchJobThread.start();
		flushThread.start();

		// 状态运行
		curState = JobNodeState.WorkerRunning;
		shutdownComplete = false;

		log.debug("serviceStarted");
	}

	@Override
	protected void serviceStop() throws Exception {
		log.debug("serviceStopping...");
		workerToManagerClient.stop();

		heartbeatThread.run = false;
		launchJobThread.run = false;
		flushThread.run = false;
		try {
			heartbeatThread.interrupt();
		} catch (Exception e) {
		}
		flushThread.interrupt();
		synchronized (WaitLaunchObj) {
			try {
				WaitLaunchObj.notifyAll();
			} catch (Exception e) {
			}
		}

		executor.shutdownNow();
		// 收尾工作
		super.serviceStop();
		log.debug("serviceStopped");
	}

	public boolean startJob(JobProfile jobProfile) {
		if (jobProfile == null) {
			log.error("job==null，无法启动！");
			return false;
		}
		if (jobToRunnables.get(jobProfile.getJobId()) != null) {
			log.warn("job:" + jobProfile + " 已经启动过了，不再启动！");
			return false;
		}
		// find jobrunnable
		JobRunnable worker = null;
		try {
			for (JobRunnableManager jm : jobRunManagers.values()) {
				worker = jm.findJobRunnable(jobProfile);
				if (worker != null) {
					log.debug("find a runner:" + worker + " for job:"
							+ jobProfile);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果没有找到，则失败！
		if (worker == null) {
			log.error("No job runner is responsible for job:" + jobProfile
					+ "!");

			// 发送一个报告，告诉此种情况
			// 更新job状态
			jobProfile.modifyJobState(JobState.Failed, "初始化运行此job的运行器失败！");
			updateJobStatus(jobProfile.getJobStatus());
			return false;
		}

		// 装配
		worker.setJobRunningContext(context);
		worker.setJob(jobProfile);

		// 记录 worker与job 的关系
		synchronized (jobToRunnables) {
			jobProfile.modifyJobState(JobState.Running, "已启动");
			jobProfile.setLaunchTime(new Date());

			jobToRunnables.put(jobProfile.getJobId(), worker);
			// 移入到running队列
			runningJobs.put(jobProfile.getJobId(), jobProfile);

			// 进度更新
			// jobProgresses.add(jobProfile.getJobStatus());
			updateJobStatus(jobProfile.getJobStatus());
		}
		executor.execute(worker);
		return true;
	}

	public void stopJob(JobProfile jobProfile) {
		// JobStatus st = new JobStatus();
		// st.setJobId(jobProfile.getJobId());
		// st.setUpdateTime(new Date());
		//
		// // 找到job的runnable
		// JobRunnable runnable = jobToRunnables.get(jobProfile.getJobId());
		// if (runnable != null) {
		// runnable.stop();
		// st.setJobState(JobState.Killing);
		// } else {
		// // 还没开始调度，直接停止
		// st.setJobState(JobState.Killed);
		// }
		// // 更新状态stopping或stopped
		// updateJobStatus(st);

		synchronized (jobToRunnables) {
			JobRunnable runnable = jobToRunnables.get(jobProfile.getJobId());
			if (runnable != null) {
				JobProfile job = runningJobs.get(jobProfile.getJobId());
				JobStatus st = job.getJobStatus();
				// 不能出现runningJobs下没有改job的情况
				runnable.stop();

				job.setJobStateStr(JobState.Killing.getCode());

				st.setJobState(JobState.Killing);
				st.setProgress("停止中");
				st.setUpdateTime(new Date());
				// 添加到反馈进度的队列，向管理节点反馈进度
				// jobProgresses.add(st);
				updateJobStatus(st);
			}
		}
	}

	// TODO 含义模糊易混淆，需改写，包括方法命名
	public void beforeDoingJob(JobProfile jobProfile) {
		// 记录job 的status为running
		jobProfile.modifyJobState(JobState.Running, "0%");
		JobStatus st = jobProfile.getJobStatus();
		jobDao.launchJob(context.getStmt(), st);

		jobProgresses.put(jobProfile.getJobId(), new JobStatus(st));
	}

	// TODO 含义模糊易混淆，需改写，包括方法命名
	public void afterExecutedJob(JobProfile jobProfile) {
		log.debug("job:" + jobProfile + " complete!");
		// job完成次数加1
		hasEndJob++;

		JobStatus jobStatus = jobProfile.getJobStatus();

		if (!JobState.isInEndState(jobStatus.getJobState())) {
			log.error("任务：[" + jobProfile + "]在结束后未返回完结的状态！将默认赋值为失败！");
			jobStatus.setJobState(JobState.Failed);
		}

		// 移除job的相关信息，添加到已完成队列
		JobProfile jobT = null;
		synchronized (jobToRunnables) {
			// System.out.println("\n-----");
			// System.out.println("移除前，jobToRunnables="+jobToRunnables.size()+",runningJobs="+runningJobs.size());
			jobToRunnables.remove(jobProfile.getJobId());
			jobT = runningJobs.remove(jobProfile.getJobId());
			// System.out.println("待移除的job:"+jobT);
			// System.out.println("移除过后，jobToRunnables="+jobToRunnables.size()+",runningJobs="+runningJobs.size());
			// System.out.println("-----\n");

		}

		jobT.modifyJobState(jobStatus.getJobState(), jobStatus.getProgress());
		jobT.setFinishTime(new Date());
		synchronized (completedJobs) {
			completedJobs.add(jobProfile);
		}

		// 保存状态数据
		jobDao.endOneJob(context.getStmt(), jobT.getJobStatus());

	}

	/**
	 * 运行的jobrunnable里调用的
	 * 
	 * @param jobStatus
	 */
	// TODO 含义模糊易混淆，需改写，包括方法命名
	public void updateJobStatus(JobStatus jobStatus) {
		if (jobStatus != null) {
			// 同步到数据库
			jobDao.updateJobRunningStatus(context.getStmt(), jobStatus);
			jobProgresses.put(jobStatus.getJobId(), new JobStatus(jobStatus));
		}
	}

	public void addJobReport(JobReport report) {
		needFlushJobReport.add(new JobReport(report));
	}

	public void doHeartbeat() {
		List<JobStatus> compJobs = new ArrayList<JobStatus>();
		// 已完成的job：finished、killed、failed
		synchronized (completedJobs) {
			for (JobProfile jp : completedJobs) {
				compJobs.add(jp.getJobStatus());
			}
			completedJobs.clear();
		}
		// 运行进度
		List<JobStatus> runningProgress = new ArrayList<JobStatus>();
		synchronized (jobProgresses) {
			for (JobStatus js : jobProgresses.values()) {
				runningProgress.add(js);
			}
			jobProgresses.clear();
		}
		NodeHeartbeatRequest request = new NodeHeartbeatRequest();

		if (super.curState == JobNodeState.WorkerRunning) {
			// 正常运行
			request.setHeartbeatType(HeartbeatEventType.Normal);
		} else if (super.curState == JobNodeState.WorkerUnregistering) {
			// 取消中
			request.setHeartbeatType(HeartbeatEventType.Shutdown_req);
		} else if (super.curState == JobNodeState.WorkerServerStopping) {
			// 关闭操作完成
			if (shutdownComplete) {
				request.setHeartbeatType(HeartbeatEventType.Shutdown_ack);
			} else {
				// 正常通报进度
				request.setHeartbeatType(HeartbeatEventType.Normal);
			}
		}
		request.setFinishedJobs(compJobs);
		request.setRunningJobs(runningProgress);
		request.setWorkerData(workerNodeData);
		NodeHeartbeatResponse response = workerToManagerClient
				.nodeHeartbeat(request);
		log.debug("heartbeat response:" + response);

		if (shutdownComplete) {
			// 不需要管response了
			log.debug("关闭服务。。。。");
			// 将剩余数据进行同步
			flushThread.interrupt();
			stop();

			return;
		}
		List<JobAction> jas = response.getJobActions();

		if (super.curState == JobNodeState.WorkerUnregistering) {
			// 状态改为服务停止中
			super.curState = JobNodeState.WorkerServerStopping;
			// 并且启动一个定时器，如果负责的任务在定时器到时之前没有完成，则强行关掉
			new Thread() {
				public void run() {
					// 让任务先运行30s
					log.debug("wait for jobs to complete normally...");
					try {
						Thread.currentThread().sleep(2 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.debug("singal the jobs to finished...");
					synchronized (jobToRunnables) {
						// 发送停止消息
						for (JobRunnable jr : jobToRunnables.values()) {
							jr.stop();
						}
					}
					// 等待30s
					try {
						Thread.currentThread().sleep(2 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					log.warn("force to kill the jobs!");
					// 强行停止所有job
					// synchronized (jobToRunnables) {
					// for (JobRunnable jr : jobToRunnables.values()) {
					// try {
					executor.shutdownNow();
					// } catch (Exception e) {
					// e.printStackTrace();
					// }finally{
					// jr.releaseRes();
					// }
					// }
					// }
					shutdownComplete = true;
				};
			}.start();
		}

		int launchCnt = 0;
		if (jas != null && jas.size() > 0) {
			LaunchActions la = null;
			for (JobAction ja : jas) {
				launchCnt++;
				if (wait4Actionjobs.containsKey(ja.getJobProfile().getJobId())) {
					la = wait4Actionjobs.get(ja.getJobProfile().getJobId());
					la.addActionType(ja.getActionType());
				} else {
					la = new LaunchActions();
					la.job = ja.getJobProfile();
					la.addActionType(ja.getActionType());
					wait4Actionjobs.put(ja.getJobProfile().getJobId(), la);
				}
			}
		}
		if (launchCnt > 0) {
			synchronized (WaitLaunchObj) {
				WaitLaunchObj.notify();
			}
		}

	}

	class HeartbeatThread extends Thread {
		boolean run = false;
		boolean idle = true;

		@Override
		public void run() {
			run = true;
			while (run) {
				try {
					Thread.currentThread().sleep(heartbeatTimeInteval);
				} catch (InterruptedException e) {
				}
				idle = false;
				doHeartbeat();
				idle = true;
			}
		}
	}

	// 负责装载运行job的线程
	class LaunchJobThread extends Thread {
		boolean run = true;

		@Override
		public void run() {
			run = true;
			while (run) {
				launchJobs();
				if (wait4Actionjobs.size() == 0) {
					synchronized (WaitLaunchObj) {
						try {
							WaitLaunchObj.wait();
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}

		private void launchJobs() {
			List<LaunchActions> tmpJobs = null;
			if (wait4Actionjobs.size() > 0) {
				synchronized (wait4Actionjobs) {
					tmpJobs = new ArrayList<LaunchActions>(
							wait4Actionjobs.values());
					wait4Actionjobs.clear();
				}
			}
			if (tmpJobs != null) {
				JobProfile job;
				for (LaunchActions launch : tmpJobs) {
					job = launch.job;
					if (launch.actionTs.contains(ActionType.Stop)) {
						if (launch.actionTs.contains(ActionType.Launch)) {
							// 可以直接kill掉，因为launch的指令还没有执行，就收到了kill的指令
							// 这里可以用状态机改写
							job.getJobStatus().setJobState(JobState.Killed);
							completedJobs.add(job);
						} else {
							// 应该在running队列，否则就是无效的kill指令
							// 发出stop指令，添加进度反馈
							stopJob(job);
						}
					} else if (launch.actionTs.contains(ActionType.Launch)) {
						boolean ok = startJob(job);
						if (ok) {
							log.debug("job started。。。 job=" + job);
						} else {
							log.warn("job 启动失败！" + job);
							// 移入完成的队列，标识为失败
							job.modifyJobState(JobState.Failed, "启动失败");
							job.setLaunchTime(new Date());
							job.setFinishTime(new Date());

							synchronized (completedJobs) {
								completedJobs.add(job);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * 负责定期持久化jobstatus、jobreport的线程
	 * 
	 * @author brightming
	 * 
	 */
	class FlushThread extends Thread {
		boolean run = false;

		@Override
		public void run() {
			run = true;
			while (run) {
				try {
					Thread.currentThread().sleep(flushInterval);
				} catch (InterruptedException e) {
				}
				doFlush();
				if (this.interrupted()) {
					doFlush();
					run = false;
				}
			}
		}

		/**
		 * 将jobreport进行保存
		 */
		private void doFlush() {
			List<JobReport> reps = new ArrayList<JobReport>();
			synchronized (needFlushJobReport) {
				if (needFlushJobReport.size() > 0) {
					for (JobReport jr : needFlushJobReport) {
						reps.add(new JobReport(jr));
					}
				}
				needFlushJobReport.clear();
			}
			if (reps != null && reps.size() > 0) {
				// 执行flush
				for (JobReport jr : reps) {
					// flush jr
					jobDao.saveJobReport(context.getStmt(), jr);
				}
			}

		}
	}

	/**
	 * 用于等待队列的元素
	 * 
	 * @author brightming
	 * 
	 */
	class LaunchActions {
		JobProfile job;
		Set<ActionType> actionTs = Collections
				.synchronizedSet(new HashSet<ActionType>());

		void addActionType(ActionType at) {
			actionTs.add(at);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((job == null) ? 0 : job.hashCode());
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
			LaunchActions other = (LaunchActions) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (job == null) {
				if (other.job != null)
					return false;
			} else if (!job.equals(other.job))
				return false;
			return true;
		}

		private JobWorkerNode getOuterType() {
			return JobWorkerNode.this;
		}

	}

	/**
	 * shutdown入口
	 * 
	 * 首先将状态置为取消注册状态； 触发heartbeat通信，向mgr发送shutdown_req的心跳事件，
	 * 接收mgr的反馈，按要求停止job或等待job完成， 一切完成以后，再次发送heartbeat事件，向mgr发送shutdown_ack心跳事件，
	 * 然后调用stop方法，完成退出
	 * 
	 * 
	 */
	public void shutdown() {
		if (super.curState != JobNodeState.WorkerRunning) {
			return;
		}
		log.debug("worker node wants to shutdown...");
		// 标记为取消注册状态
		super.curState = JobNodeState.WorkerUnregistering;
		if (heartbeatThread.idle == true) {
			heartbeatThread.interrupt();
		}
	}
}
