package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoJobDaoImpl implements RnoJobDao {


	private static Log log = LogFactory.getLog(RnoJobDaoImpl.class);

	/**
	 * 获取所有的处于运行表的状态
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-8-15 上午11:35:21
	 */
	public List<JobProfile> getRunningJobs(Statement stmt) {
		if (stmt == null) {
			log.error("getRunningJobs stmt is null!");
			return Collections.EMPTY_LIST;
		}
		String sql = "SELECT JOB_ID,JOB_NAME,CREATOR,PRIORITY,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') AS CREATE_TIME,JOB_TYPE,DESCRIPTION,TO_CHAR(LAUNCH_TIME,'YYYY-MM-DD HH24:MI:SS') AS LAUNCH_TIME,TO_CHAR(COMPLETE_TIME,'YYYY-MM-DD HH24:MI:SS') AS COMPLETE_TIME,JOB_RUNNING_STATUS,STATUS FROM RNO_JOB WHERE JOB_RUNNING_STATUS in ('"
				+JobState.Launched.toString()+"','" 
				+JobState.Running.toString()
				+ "','"
				+ JobState.Initiate.toString() + "')";
		List<Map<String, Object>> result = RnoHelper.commonQuery(stmt, sql);
		List<JobProfile> jobs = new ArrayList<JobProfile>();
		DateUtil dateUtil=new DateUtil();
		if (result.size() > 0) {
			JobProfile job = null;
			for (Map<String, Object> one : result) {
				job = RnoHelper.commonInjection(JobProfile.class, one,dateUtil);
				if (job != null) {
					jobs.add(job);
				}
			}
		}

		return jobs;
	}

	/**
	 * 获取job的报告
	 * 
	 * @param jobId
	 * @return
	 * @author brightming 2014-8-15 上午11:36:03
	 */
	public List<JobReport> getJobReport(Statement stmt, long jobId) {
		if (stmt == null) {
			log.error("getJobReport stmt is null!");
			return Collections.EMPTY_LIST;
		}
		List<JobReport> reports = new ArrayList<JobReport>();
		String sql = "select REPORT_ID,JOB_ID,STAGE,TO_CHAR(BEG_TIME,'YYYY-MM-DD HH24:MI:SS') AS BEG_TIME,TO_CHAR(END_TIME,STATE,'YYYY-MM-DD HH24:MI:SS') AS END_TIME,ATT_MSG,REPORT_TYPE from RNO_JOB_REPORT where JOB_ID="
				+ jobId;
		List<Map<String, Object>> result = RnoHelper.commonQuery(stmt, sql);
		if (result.size() > 0) {
			DateUtil dateUtil=new DateUtil();
			for (Map<String, Object> one : result) {
				JobReport rep = RnoHelper.commonInjection(JobReport.class, one,dateUtil);
				reports.add(rep);
			}
		}
		return reports;
	}

	/**
	 * 保存job报告
	 * 
	 * @param stmt
	 * @param report
	 * @return
	 * @author brightming 2014-8-15 上午11:38:37
	 */
	public boolean saveJobReport(Statement stmt, JobReport report) {
		log.debug("saveJobReport.stmt=" + stmt + ",report=" + report);
		if (stmt == null || report == null) {
			log.error("saveJobReport invalid params!stmt=" + stmt + ",report="
					+ report);
			return false;
		}
		DateUtil dateUtil=new DateUtil();
		String sql = "insert into RNO_JOB_REPORT(REPORT_ID,JOB_ID,STAGE,BEG_TIME,END_TIME,STATE,ATT_MSG,REPORT_TYPE) values(SEQ_RNO_JOB_REPORT_ID.NEXTVAL,"
				+ report.getJobId() + ",'" + report.getStage() + "'";
		if (report.getBegTime() != null) {
			sql += ",to_date('"
					+ dateUtil.format_yyyyMMddHHmmss(report.getBegTime())
					+ "','YYYY-MM-DD HH24:Mi:SS')";
		} else {
			sql += ",null";
		}
		if (report.getEndTime() != null) {
			sql += ",to_date('"
					+ dateUtil.format_yyyyMMddHHmmss(report.getEndTime())
					+ "','YYYY-MM-DD HH24:Mi:SS')";
		} else {
			sql += ",null";
		}

		String attmsg=report.getAttMsg();
		if(attmsg!=null){
			int uplimit=1010;
			//@author chao.xj  2015-8-3 下午4:03:11
			uplimit = 4000;
			if(attmsg.getBytes().length>uplimit){
				byte[] old=attmsg.getBytes();
				byte[] nmb=new byte[uplimit];
				for(int i=0;i<uplimit;i++){
					nmb[i]=old[i];
				}
				attmsg=new String(nmb);
				attmsg+="...";
			}
		}else{
			attmsg="";
		}
		sql += ",'" + report.getFinishState() + "','" + attmsg
				+ "',"+report.getReportType()+")";
		log.debug("saveJobReport sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("saveJobReport fail! sql=" + sql);
			return false;
		}
		return true;
	}

	/**
	 * 运行任务
	 * 
	 * @param stmt
	 * @param status
	 * @return
	 * @author brightming 2014-8-15 下午3:45:31
	 */
	public JobStatus launchJob(Statement stmt, JobStatus jobStatus) {
		if (stmt == null || jobStatus == null) {
			log.error("endOneJob invalid params!stmt=" + stmt + ",jobStatus="
					+ jobStatus);
			return null;
		}
		DateUtil dateUtil=new DateUtil();
		// update RNO_JOB table
		String sql = "update RNO_JOB set LAUNCH_TIME=to_date('"
				+ dateUtil.format_yyyyMMddHHmmss(jobStatus.getUpdateTime())
				+ "','YYYY-MM-DD HH24:MI:SS'),JOB_RUNNING_STATUS='"
				+ JobState.Launched.toString() + "' where job_id="
				+ jobStatus.getJobId();
		log.debug("endOneJob,update RNO_JOB table ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("endOneJob,update RNO_JOB table fail! sql=" + sql);
			return null;
		}

		return jobStatus;
	}

	/**
	 * 结束一项job
	 * 
	 * @param stmt
	 * @param jobStatus
	 * @return
	 * @author brightming 2014-8-15 上午11:39:18
	 */
	public boolean endOneJob(Statement stmt, JobStatus jobStatus) {
		if (stmt == null || jobStatus == null) {
			log.error("endOneJob invalid params!stmt=" + stmt + ",jobStatus="
					+ jobStatus);
			return false;
		}
		DateUtil dateUtil=new DateUtil();
		// update RNO_JOB table
		String sql = "update RNO_JOB set COMPLETE_TIME=to_date('"
				+ dateUtil.format_yyyyMMddHHmmss(jobStatus.getUpdateTime())
				+ "','YYYY-MM-DD HH24:MI:SS'),JOB_RUNNING_STATUS='"
				+ jobStatus.getJobState() + "' where job_id="
				+ jobStatus.getJobId();
		log.debug("endOneJob,update RNO_JOB table ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("endOneJob,update RNO_JOB table fail! sql=" + sql);
			return false;
		}

		return true;
	}

	/**
	 * 新增一项job
	 * 
	 * @param stmt
	 * @param job
	 * @return
	 * @author brightming 2014-8-15 上午11:39:50
	 */
	public JobProfile addOneJob(Statement stmt, JobProfile job) {
		if (stmt == null || job == null) {
			log.error("addOneJob invalid params!stmt=" + stmt + ",job=" + job);
			return null;
		}

		DateUtil dateUtil=new DateUtil();
		long job_id = RnoHelper.getNextSeqValue("SEQ_RNO_JOB_ID", stmt);
		if (job_id == -1) {
			log.error("addOneJob。获取job_id失败！");
			return null;
		}
		job.setJobId(job_id);

		// 插入进行表
		String sql = "insert into RNO_JOB";
		String values = "(JOB_ID,JOB_NAME,CREATOR,PRIORITY,CREATE_TIME,JOB_TYPE,DESCRIPTION,LAUNCH_TIME,COMPLETE_TIME,JOB_RUNNING_STATUS,STATUS) values("
				+ job_id
				+ ",'"
				+ job.getJobName()
				+ "','"
				+ job.getAccount()
				+ "',"
				+ job.getPriority()
				+ ",to_date('"
				+ dateUtil.format_yyyyMMddHHmmss(job.getSubmitTime())
				+ "','YYYY-MM-DD HH24:MI:SS'),"
				+ "'"
				+ job.getJobType()
				+ "','"
				+ job.getDescription() + "',";
		// 是否有LAUNCH_time
		if (job.getLaunchTime() != null) {
			values += "to_date('"
					+ dateUtil.format_yyyyMMddHHmmss(job.getLaunchTime())
					+ "','YYYY-MM-DD HH24:MI:SS'),";
		} else {
			values += "null,";
		}
		// 是否有COMPLETE_TIME
		if (job.getFinishTime() != null) {
			values += "to_date('"
					+ dateUtil.format_yyyyMMddHHmmss(job.getFinishTime())
					+ "','YYYY-MM-DD HH24:MI:SS'),";
		} else {
			values += "null,";
		}

		// 有没有状态
		if (job.getJobStatus() == null) {
			values += "'" + JobState.Initiate.toString() + "'";
		} else {
			values += "'" + job.getJobStatus().getJobState() + "'";
		}
		values += ",'N')";

		// 插入总表
		try {
			sql += values;
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("增加job时，插入总表RNO_JOB失败！sql=" + sql);
			return null;
		}
		job.setJobId(job_id);
		return job;
	}

	/**
	 * 更新job状态
	 * 
	 * @param stmt
	 * @param status
	 * @return
	 * @author brightming 2014-8-15 上午11:40:26
	 */
	public JobStatus updateJobRunningStatus(Statement stmt, JobStatus status) {
		if (stmt == null || status == null) {
			log.error("updateJobStatus invalid params!stmt=" + stmt
					+ ",status=" + status);
			return null;
		}
		if(JobState.Launched==status.getJobState()){
			//job launched
			status=launchJob(stmt, status);
			return status;
		}
		else if (JobState.isInEndState(status.getJobState())) {
			// job ended
			endOneJob(stmt, status);
			return status;
		} 
		else {
			String sql = "update RNO_JOB SET JOB_RUNNING_STATUS='"
					+ status.getJobState() + "' where JOB_ID="
					+ status.getJobId();
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("更新job运行状态失败！sql=" + sql);
				e.printStackTrace();
				return null;
			}
			return status;
		}
	}

}
