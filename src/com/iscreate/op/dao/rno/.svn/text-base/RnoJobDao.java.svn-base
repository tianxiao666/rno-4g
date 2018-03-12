package com.iscreate.op.dao.rno;

import java.sql.Statement;
import java.util.List;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;

public interface RnoJobDao {

	/**
	 * 获取所有的处于运行表的状态
	 * @param stmt
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:35:21
	 */
	public List<JobProfile> getRunningJobs(Statement stmt); 
	
	/**
	 * 获取job的报告
	 * @param jobId
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:36:03
	 */
	public List<JobReport> getJobReport(Statement stmt ,long jobId);
	
	/**
	 * 保存job报告
	 * @param stmt
	 * @param report
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:38:37
	 */
	public boolean saveJobReport(Statement stmt,JobReport report);
	/**
	 * 运行任务
	 * 
	 * @param stmt
	 * @param status
	 * @return
	 * @author brightming 2014-8-15 下午3:45:31
	 */
	public JobStatus launchJob(Statement stmt, JobStatus jobStatus);
	
	/**
	 * 结束一项job
	 * @param stmt
	 * @param jobStatus
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:39:18
	 */
	public boolean endOneJob(Statement stmt,JobStatus jobStatus);
	
	/**
	 * 新增一项job
	 * @param stmt
	 * @param job
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:39:50
	 */
	public JobProfile addOneJob(Statement stmt,JobProfile job);
	
	/**
	 * 更新job运行状态
	 * @param stmt
	 * @param status
	 * @return
	 * @author brightming
	 * 2014-8-15 上午11:40:26
	 */
	public JobStatus updateJobRunningStatus(Statement stmt,JobStatus status);

}
