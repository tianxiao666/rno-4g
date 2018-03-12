package com.iscreate.op.service.rno.job.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.rno.RnoJobDao;
import com.iscreate.op.dao.rno.RnoJobDaoImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.LifeCycle;
import com.iscreate.op.service.rno.job.LifeCycleException;
import com.iscreate.op.service.rno.job.LifeCycleListener;
import com.iscreate.op.service.rno.job.common.JobState;

public class LocalJobManagerLifeCycleListener implements LifeCycleListener {

	private static Log log = LogFactory
			.getLog(LocalJobManagerLifeCycleListener.class);
	private Connection connection;
	private RnoJobDao jobDao = new RnoJobDaoImpl();

	public LocalJobManagerLifeCycleListener(Connection conn) {
		this.connection = conn;
	}

	@Override
	public void onStart(LifeCycle obj) throws LifeCycleException {

		long t1 = System.currentTimeMillis();
		// 负责从数据库读取相应的信息
		LocalJobManager jobManager = (LocalJobManager) (obj);
		if (!jobManager.isReloadWhenStartUp()) {
			return;
		}
		Map<Long, JobProfile> waitJobQueues = jobManager.waitJobQueues;
		Map<Long, JobStatus> jobStatusWrpperes = jobManager.latestJobStatuses;

		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LifeCycleException(e);
		}

		List<JobProfile> fromDbs = jobDao.getRunningJobs(stmt);
		log.debug("从数据库读取到的未完成的job:" + fromDbs);
		if (fromDbs != null && fromDbs.size() > 0) {
			for (JobProfile job : fromDbs) {
				// 加入到队列
				waitJobQueues.put(job.getJobId(), job);
				// 加入status数据
				JobStatus jobStatus = new JobStatus();
				jobStatus.setJobId(job.getJobId());
				jobStatus.setJobState(JobState.Initiate);
				jobStatusWrpperes.put(job.getJobId(), jobStatus);
			}
		}

		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		long t2 = System.currentTimeMillis();
		log.debug("onStart ok.耗时:" + (t2 - t1) + "ms");
	}

	@Override
	public void onStop(LifeCycle obj) throws LifeCycleException {
		LocalJobManager jobManager = (LocalJobManager) (obj);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LifeCycleException(e);
		}

		// 清理全部的最新状态
		jobManager.latestJobStatuses.clear();

		// 保存全部累积状态
		synchronized (jobManager.jobStatuses) {
			// 把队列里的数据逐一写到数据库
			// 写job状态
			List<JobStatus> jobStatuss = jobManager.jobStatuses;
			if (jobStatuss.size() > 0) {
				for (JobStatus js : jobStatuss) {
					log.debug("刷新状态:" + js);
					jobDao.updateJobRunningStatus(stmt, js);
				}
				// 清理掉
				jobStatuss.clear();
			}
		}
		synchronized (jobManager.jobReports) {
			// 写job报告
			List<JobReport> reports = jobManager.jobReports;
			if (reports.size() > 0) {
				for (JobReport rep : reports) {
					if (rep != null) {
						jobDao.saveJobReport(stmt, rep);
					}
				}
			}
			reports.clear();
		}
	}

}
