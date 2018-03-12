package com.iscreate.op.service.rno.parser.jobrunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public abstract class DbParserBaseJobRunnable extends BaseJobRunnable {

	private Connection connection;
	protected Statement stmt;

	public DbParserBaseJobRunnable() {
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JobStatus runJob(JobProfile job) {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		JobStatus status = null;
		try {
			if(connection!=null && stmt!=null){
				status = runJobInternal(job, connection,stmt);
			}else{
				status = new JobStatus(job.getJobId());
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				status.setProgress("失败！数据库连接失败！");
				return status;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = new JobStatus(job.getJobId());
			// status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}
		if (JobState.isEndWithSucceded(status.getJobState())) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				// connection.rollback();
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public void releaseRes() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public abstract JobStatus runJobInternal(JobProfile job, Connection conn,Statement stmt);
}
