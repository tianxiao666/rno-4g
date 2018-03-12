package com.iscreate.op.service.rno.job.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.service.rno.job.ActionType;
import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.LifeCycle;
import com.iscreate.op.service.rno.job.LifeCycleException;
import com.iscreate.op.service.rno.job.LifeCycleListener;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class LocalJobManager implements JobManager, LifeCycle {
	private static Logger log = Logger.getLogger(LocalJobManager.class);

	// 刷新内存数据的时间周期
	int flushPeriodInSec = 1000 * 10;// 10s

	// 用于同步的lock
	private static Object SyncLock = new Object();
	private static Object WaitObj = new Object();

	//
	Connection connection = null;
	Statement stmt = null;
	RnoJobDao jobDao = new RnoJobDaoImpl();

	private boolean reloadWhenStartUp = false;// 启动的时候是否重新装载任务

	// 运行状态
	private static boolean running = false;
	private static boolean stopping = false;

	// 工作进度监听器
	private List<JobListener> jobListeners = new ArrayList<JobListener>();
	// 生命周期监听器
	private List<LifeCycleListener> lifeCycleListeners = new ArrayList<LifeCycleListener>();
	// 工作调度器
	private JobScheduler jobScheduler;
	// flush线程
	JobFlushThread flushThread;

	// --------下面分类存放----------//
	// 每个jobWorker里的job
	protected Map<JobWorkerStatus, List<JobProfile>> runningJobQueues = Collections
			.synchronizedMap(new HashMap<JobWorkerStatus, List<JobProfile>>());// 已经分配出去的
	// 预留给某个jobworker的任务，如要求停止某个任务
	protected Map<JobWorkerStatus, List<JobAction>> pendingActions = Collections
			.synchronizedMap(new HashMap<JobWorkerStatus, List<JobAction>>());
	// 每个job对应的jobworkerstatus,key为jobId
	protected Map<Long, JobWorkerStatus> jobToWorkers = Collections
			.synchronizedMap(new HashMap<Long, JobWorkerStatus>());

	// 当前在处理的jobworker的heartbeat的状态
	protected List<JobWorkerStatus> currentHandlingJobWorkerStatuses = new ArrayList<JobWorkerStatus>();

	// 对应的job最新状态,key为jobId
	protected Map<Long, JobStatus> latestJobStatuses = Collections
			.synchronizedMap(new HashMap<Long, JobStatus>());
	// 客户端传送过来的累积的工作报告
	protected List<JobStatus> jobStatuses = Collections
			.synchronizedList(new ArrayList<JobStatus>());
	// 所有未分配的job,key为jobId
	protected Map<Long, JobProfile> waitJobQueues = Collections
			.synchronizedSortedMap(new TreeMap<Long, JobProfile>());
	// 累计的报告,更新完后，会清掉
	protected List<JobReport> jobReports = Collections
			.synchronizedList(new ArrayList<JobReport>());

	boolean jvmShutdown = false;

	// ----for spring -----//
	// spring init-method
	public void init() throws Exception {
		// 准备数据库连接
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		// 装配 监听器
		lifeCycleListeners
				.add(new LocalJobManagerLifeCycleListener(connection));

		start();
	}

	public void setLifeCycleListeners(List<LifeCycleListener> lifeCycleListeners) {
		this.lifeCycleListeners = lifeCycleListeners;
	}

	public void setFlushPeriodInSec(int flushPeriodInSec) {
		this.flushPeriodInSec = flushPeriodInSec;
	}

	public void setReloadWhenStartUp(boolean reloadWhenStartUp) {
		this.reloadWhenStartUp = reloadWhenStartUp;
	}

	public boolean isReloadWhenStartUp() {
		return reloadWhenStartUp;
	}

	// -----------end spring----//

	public void start() throws LifeCycleException {
		synchronized (LocalJobManager.class) {
			if (running) {
				log.warn("JobManager is running! Can't start again!");
				return;
			}
			log.debug("starting JobManager...");
			for (LifeCycleListener lc : lifeCycleListeners) {
				lc.onStart(this);
			}
			// 关联jobscheduler
			jobScheduler = new FifoJobScheduler();
			jobScheduler.setJobManager(this);
			// 创建相关队列
			// 读取数据库中的运行态的job到内存
			try {
				stmt = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 创建flush线程
			flushThread = new JobFlushThread();
			flushThread.start();

			log.debug("start JobManager successfully!");
			running = true;
		}

	}

	public void stop() throws LifeCycleException {
		synchronized (LocalJobManager.class) {
			if (!running || stopping) {
				return;
			}
			log.debug("stopping JobManager...");
			stopping = true;

			// 刷新缓存数据到数据库
			log.debug("flush all pending data into database...");
			flushThread.run = false;// 停止
			jvmShutdown = true;
			// 通知监听器
			synchronized (WaitObj) {
				WaitObj.notify();// 通知Flush线程
			}
			log.debug("after notify flush thread...");
			for (LifeCycleListener lc : lifeCycleListeners) {
				lc.onStop(this);
			}

			try {
				flushThread.join(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// while (!flushThread.finish) {
			// // 等待刷新完
			// try {
			// Thread.currentThread().sleep(5 * 1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }

			// 不等待各JobWorker任务运行完成
			// log.debug("waiting all JobWorker to stop!");
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			running = false;
			stopping = false;

			log.debug("JobManager has stopped...");
		}
	}

	/**
	 * 添加job 同步方法
	 * 
	 * @param job
	 * @return
	 * @author brightming 2014-8-15 下午5:48:04
	 */
	public JobStatus addJob(JobProfile job) {
		return addJob(job, new JobAddCallback<JobStatus>() {
			@Override
			public JobStatus doWhenJobAdded(JobProfile job) {
				JobStatus jobStatus = new JobStatus();
				jobStatus.setJobId(job.getJobId());
				jobStatus.setJobState(JobState.Initiate);
				return jobStatus;
			}
		});
	}

	/**
	 * 添加job 同步方法 增加参数，可以介入用户逻辑
	 * 
	 * @param job
	 * @param listener
	 * @return
	 * @author brightming 2014-9-1 下午4:25:57
	 */
	public <T> T addJob(JobProfile job, JobAddCallback<T> listener) {
		if (stopping || !running) {
			// 停止状态，暂停接收任何任务
			log.warn("JobManager is in stopping:" + stopping + ",running:"
					+ running + ",can't accept new job for the moment.");
			return null;
		}
		// 添加，一定要加入成功，这个是一个同步的过程
		JobStatus jobStatus = null;
		job = jobDao.addOneJob(stmt, job);
		T retobj = null;
		if (listener != null) {
			retobj = listener.doWhenJobAdded(job);
		}
		synchronized (waitJobQueues) {
			// 放进队列
			if (job == null) {
				log.error("job添加失败！");
				return null;
			}
			// 状态队列
			jobStatus = new JobStatus();
			jobStatus.setJobId(job.getJobId());
			jobStatus.setJobState(JobState.Initiate);
			jobStatus.setUpdateTime(new Date());
			latestJobStatuses.put(job.getJobId(), jobStatus);
			// 待分配队列
			waitJobQueues.put(job.getJobId(), job);
		}
		return retobj;
	}

	/**
	 * 停止指定 job，
	 * 
	 * @param job
	 * @param account
	 *            发出指令的账号
	 * @param reason
	 *            原因
	 * @return 新的状态
	 * @author brightming 2014-8-14 下午6:49:45
	 */
	public JobStatus killJob(JobProfile job, String account, String reason) {
		if (job == null || job.getJobId() == null) {
			return null;
		}
		synchronized (waitJobQueues) {
			// 该job的最新状态
			JobStatus status = latestJobStatuses.get(job.getJobId());
			if (status == null) {
				log.error("job:" + job + " not found!can't kill it!");
				// 但无论如何，都提交一个状态变化
				status = new JobStatus();
				status.setJobId(job.getJobId());
				status.setJobState(JobState.Killed);
				status.setUpdateTime(new Date());
			}
			// else {
			// 未分配出去的任务，可以直接kill掉
			if (waitJobQueues.containsKey(job.getJobId())) {
				log.debug("job:" + job + " 尚未分配给jobWorker，可以直接kill掉");
				status.setJobState(JobState.Killed);
				status.setUpdateTime(new Date());

				// 从job的最新队列移除该job的状态数据
				latestJobStatuses.remove(job.getJobId());

				// 从waitqueue里移除
				waitJobQueues.remove(job.getJobId());
			}
			// 分配出去了的任务，需要通知相应的job worker停止,需判断是否是已经结束了
			else if (!JobState.isInEndState(status
					.getJobState())) {
				log.debug("job:" + job
						+ " is in running state,will ask jobwork to stop it .");
				// 最新状态更改为停止中
				status.setJobState(JobState.Killing);
				// find out which jobworker is running the job
				JobWorkerStatus worker = jobToWorkers.get(job.getJobId());
				if (worker == null) {
					// 找不到运行该任务的jobworker，则直接停掉
					log.error("unknown worker!impossible!big mistake!");
					// that't weird....so we pretend to response the
					// request....
					// by stop it arbitrarily
					status.setJobState(JobState.Killed);
					status.setUpdateTime(new Date());
					latestJobStatuses.remove(job.getJobId());
				} else {
					// add to the pending action queue for the worker,
					// the worker will get the action for the next interval
					// of
					// 10s heartbeat
					status.setJobState(JobState.Killing);
					status.setUpdateTime(new Date());

					List<JobAction> jas = pendingActions.get(worker);
					if (jas == null) {
						jas = new ArrayList<JobAction>();
						pendingActions.put(worker, jas);
					}
					jas.add(new JobAction(ActionType.Stop, job));
				}
			} else {
				// 其他则是不能停止的状态，如stopping,succeded,fail,stopped
				log.debug("job:" + job + " is in "
						+ status.getJobState() + " ,can't stop it !");
			}

			// }

			// 统一加入到队列,等待刷到数据库
			jobStatuses.add(status);
			// 增加一个系统报告
			JobReport reprt = new JobReport();
			reprt.setJobId(job.getJobId());
			reprt.setStage("停止");
			reprt.setBegTime(new Date());
			if (reason == null) {
				reason = "";
			}
			if (account == null) {
				account = "";
			}
			if (reason.length() > 512) {
				reason = reason.substring(0, 512);
				reason += "...";
			}
			if (account.length() > 128) {
				account = account.substring(0, 128);
				account += "...";
			}
			reprt.setAttMsg("账号：" + account + "因[" + reason + "]停止该job");

			jobReports.add(reprt);
			return status;
		}
	}

	/**
	 * 更新job的状态
	 * 
	 * @param jobStatus
	 * @return
	 * @author brightming 2014-8-17 上午11:07:30
	 */
	// public JobStatus updateJobStatus(JobStatus jobStatus) {
	// if (jobStatus == null) {
	// log.error("updateJobStatus,jobStatus is null!");
	// return null;
	// }
	// long jobId = jobStatus.getJobId();
	// JobStatus status = latestJobStatuses.get(jobId);
	// if (status != null) {
	// // 更新内存里的数据
	// status.setJobRunningStatus(jobStatus.getJobRunningStatus());
	// status.setUpdateTime(jobStatus.getUpdateTime());
	// } else {
	// log.error("找不到jobStatus:" + jobStatus + "对应的数据，无法更新。");
	// status = new JobStatus();
	// status.setJobId(jobStatus.getJobId());
	// status.setJobRunningStatus(jobStatus.getJobRunningStatus());
	// status.setUpdateTime(jobStatus.getUpdateTime());
	// }
	// // 加入到待刷新队列
	// jobStatuses.add(status);
	// return status;
	//
	// }

	/**
	 * 心跳通信， 负责传送jobworker的工作能力，和状态、报告数据 回传一些给jobworker的数据
	 * 
	 * @param jobWorkerStatus
	 * @param reports
	 * @param updateJobStatusList
	 * @return
	 * @author brightming 2014-8-15 下午6:38:31
	 */
	public List<JobAction> heartbeat(JobWorkerStatus jobWorkerStatus,
			List<JobReport> reports, List<JobStatus> updateJobStatusList) {

		// 处理携带的状态数据
		if (updateJobStatusList != null && updateJobStatusList.size() > 0) {
			JobStatus st;
			log.debug("jobWorker:" + jobWorkerStatus + "携带了jobStatus过来。。。。"
					+ updateJobStatusList);
			synchronized (latestJobStatuses) {
				for (JobStatus js : updateJobStatusList) {
					// if
					// (JobRunningStatus.isInEndState(js.getJobRunningStatus()))
					// {
					// 放到刷新线程里做
					// // job结束了，移除状态列表
					// latestJobStatuses.remove(js.getJobId());
					// // 移除与jobworker的关系
					// if (jobToWorkers.containsKey(js.getJobId())) {
					// JobWorkerStatus jws = jobToWorkers.remove(js
					// .getJobId());
					// List<JobProfile> jbps = runningJobQueues.get(jws);
					// jbps.remove(new JobProfile(js.getJobId()));
					// }
					// } else {
					st = latestJobStatuses.get(js.getJobId());
					if (st != null) {
						// 更改最新状态
						st.setJobState(js.getJobState());
						st.setUpdateTime(js.getUpdateTime());
					} else {
						// 是否允许这样做?是否会不安全？
						st = new JobStatus();
						st.setJobId(js.getJobId());
						st.setJobState(js.getJobState());
						st.setUpdateTime(js.getUpdateTime());

						latestJobStatuses.put(js.getJobId(), st);
					}
					// }

					// 加入待刷新状态队列
					jobStatuses.add(js);
				}
			}
		}
		// 处理携带的报告
		if (reports != null && reports.size() > 0) {
			log.debug("jobWorker:" + jobWorkerStatus + "携带了报告过来。。。。" + reports);
			synchronized (jobReports) {
				for (JobReport jr : reports) {
					jobReports.add(jr);
				}
			}
		}

		// 看能给该jobworker一些什么任务。。。。
		// 看是否已经加入到队列
		synchronized (runningJobQueues) {
			if (!runningJobQueues.containsKey(jobWorkerStatus)) {
				runningJobQueues.put(jobWorkerStatus,
						new ArrayList<JobProfile>());
			}
		}
		// 准备响应
		List<JobAction> jobActions = new ArrayList<JobAction>();
		// 有没有pending任务
		synchronized (pendingActions) {
			if (pendingActions.containsKey(jobWorkerStatus)) {
				log.debug("jobWorker:" + jobWorkerStatus
						+ " 具有pending的jobAction:" + pendingActions);
				jobActions.addAll(pendingActions.get(jobWorkerStatus));
				pendingActions.remove(jobWorkerStatus);
			}
		}

		boolean needAssign = true;
		synchronized (currentHandlingJobWorkerStatuses) {
			if (currentHandlingJobWorkerStatuses.contains(jobWorkerStatus)) {
				// 上一次的heartbeat的处理尚未完成，下一个通信又来了，此时，不需要进行任务分配，免得造成问题。
				needAssign = false;
				log.warn(jobWorkerStatus+"上一次的通信尚未处理完成，此次不进行新的等待队列的任务分配。");
			} else {
				currentHandlingJobWorkerStatuses.add(jobWorkerStatus);
			}
		}

		if (needAssign) {
			// 查看资源情况，进行任务分配
			synchronized (waitJobQueues) {
				log.debug("开始给jobWorker:" + jobWorkerStatus + "分配job...");
				List<JobProfile> jobs = jobScheduler
						.assignJobs(jobWorkerStatus);
				if (jobs != null) {
					for (JobProfile job : jobs) {
						JobAction ja = new JobAction(ActionType.Launch, job);
						jobActions.add(ja);
					}
				}
			}
			synchronized (currentHandlingJobWorkerStatuses) {
				currentHandlingJobWorkerStatuses.remove(jobWorkerStatus);
			}
		}
		log.debug("jobActions for jobWorkerStatus:" + jobWorkerStatus + " is :"
				+ jobActions);
		return jobActions;
	}

	// public void addJobReport(JobReport report) {
	// if (report != null) {
	// jobReports.add(report);
	// }
	// }

	/**
	 * 周期更新job的线程
	 * 
	 * @author brightming
	 * 
	 */
	class JobFlushThread extends Thread {
		boolean run = true;
		boolean finish = false;

		List<JobStatus> innerStatuss = new ArrayList<JobStatus>();
		List<JobReport> innerreports = null;
		Statement stmt = null;
		RnoJobDao jobDao = new RnoJobDaoImpl();

		@Override
		public void run() {
			if (connection == null) {
				log.error("connection为null！该线程无法正常工作");
				return;
			}
			try {
				stmt = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("jobflush线程创建statement失败！无法正常工作！");
				return;
			}
			while (run) {
				// 执行
				doFlush(false);
				// 等待
				synchronized (WaitObj) {
					try {
						WaitObj.wait(flushPeriodInSec);
					} catch (InterruptedException e) {
					}
				}
			}
			if (!jvmShutdown) {
				// 清理资源
				doFlush(true);
			}
			// 停止
			finish = true;
		}

		private void doFlush(boolean toEnd) {
//			log.debug("do flush...");
			// 拷贝到自己的空间
			synchronized (innerStatuss) {
				// 检查status
				innerStatuss.clear();
				for (JobStatus st : jobStatuses) {
					innerStatuss.add(st);
				}
				jobStatuses.clear();
				if (toEnd) {// 如果是因为stop而flush，则可以清理所有的资源
					latestJobStatuses.clear();
				}
			}
			synchronized (jobReports) {
				// 检查report
				innerreports = new ArrayList<JobReport>(jobReports);
				jobReports.clear();
			}
			// 慢慢处理
			// 处理状态，及时刷到db
			if (innerStatuss != null && innerStatuss.size() > 0) {
//				log.debug("状态更新队列不空..size=" + innerStatuss.size());
				// 刷
				for (JobStatus st : innerStatuss) {
					if (JobState.isInEndState(st.getJobState())) {
						// job结束了，移除状态列表
						latestJobStatuses.remove(st.getJobId());
						// 移除与jobworker的关系
						if (jobToWorkers.containsKey(st.getJobId())) {
							JobWorkerStatus jws = jobToWorkers.remove(st
									.getJobId());
							List<JobProfile> jbps = runningJobQueues.get(jws);
							jbps.remove(new JobProfile(st.getJobId()));
						}
					}
					jobDao.updateJobRunningStatus(stmt, st);
				}
				// if (!toEnd) {// 如果不是最后一次因为stop要求而刷
				// // 状态更新以后，需要回填更新status，比较dirtytime
				// synchronized (SyncLock) {
				// long hasHandleDirtyTime, curDirtyTime;
				// long jobId;
				// for (JobStatusWrapper wrap : innerStatusWrappers) {
				// jobId = wrap.getJobStatus().getJobId();
				// // 如果status是结束态，则要清理队列
				// if (JobRunningStatus.isInEndState(wrap
				// .getJobStatus().getJobRunningStatus())) {
				// //
				// 清除队列：jobToWorkers队列，jobStatuses，waitJobQueues,runningJobQueues
				// System.out
				// .println("更新的是结束状态，从队列里清除job信息，jobId="
				// + jobId);
				// latestJobStatuses.remove(jobId);
				// if (waitJobQueues.containsKey(jobId)) {
				// waitJobQueues.remove(jobId);
				// } else {
				// List<JobProfile> jobs = runningJobQueues
				// .get(jobToWorkers.get(jobId));
				// if (jobs != null && jobs.size() > 0) {
				// jobs.remove(new JobProfile(jobId));
				// }
				// jobToWorkers.remove(jobId);
				// }
				// continue;
				// }
				// // 否则，就是普通的其他状态变迁
				// hasHandleDirtyTime = wrap.getDirtyTime();
				// curDirtyTime = latestJobStatuses.get(
				// wrap.getJobStatus().getJobId())
				// .getDirtyTime();
				// if (hasHandleDirtyTime == curDirtyTime) {
				// // 在这段时间中，没有新的变化，则可以将这个dirty标记清掉
				// latestJobStatuses.get(
				// wrap.getJobStatus().getJobId())
				// .setDirty(false);
				// }
				// // 否则，在这段时间内，有变化，不能干掉
				// }
				// }
				// }
			}

			// 处理累计的报告
			if (innerreports != null && innerreports.size() > 0) {
//				log.debug("报告队列不空，size=" + innerreports.size());
				// 刷
				for (JobReport rep : innerreports) {
					jobDao.saveJobReport(stmt, rep);
				}
				innerreports.clear();
			}
		}
	}

	@Override
	public JobWorkerStatus register(JobWorkerStatus client) {
		// TODO Auto-generated method stub
		return null;
	}
}
