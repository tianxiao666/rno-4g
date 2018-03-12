package com.iscreate.op.service.rno.task.structana;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImplV2;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoV2;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoStructAnaJobRec;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoStructAnaJobRunnable extends BaseJobRunnable {

	private static Log log = LogFactory.getLog(RnoStructAnaJobRunnable.class);
	private static String jobType = "RNO_STRUCT_ANA";
    private Connection connection;
    private Statement stmt;
	@Override
	public boolean isMyJob(JobProfile job) {
		if (job == null)
			return false;
		else {
			return jobType.equals(job.getJobType());
		}
	}

	@Override
	public JobStatus runJob(JobProfile job) {

		// 获取工作id
		long jobId = job.getJobId();
		// 初始化
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);
//		JobWorker worker = super.getJobWorker();

		RnoStructureAnalysisDaoV2 structureAnalysisDaoV2 = new RnoStructureAnalysisDaoImplV2();

		// ---------准备数据库连接----------//
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();

		try {
			connection.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//
			report.setStage("调整数据连接的属性");
			report.setBegTime(new Date());
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("调整数据连接的属性");
			addJobReport(report);
			//
//			status.setJobState(JobState.Failed);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("发生系统错误，无法创建数据库执行器！");
			// 保存报告信息
			report.setStage("创建数据库执行器");
			report.setBegTime(new Date());
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("无法创建数据库执行器");
			addJobReport(report);
			// 关闭连接
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		// 是否停止
		if (super.isStop()) {
			return clearWhenStop(job, connection, stmt);
		}

		String stage = "准备数据";
		Date begTime = new Date(), endTime;
		String finishState = "";
		String attMsg = "";
		// 获取任务的详细信息
		String sql = "";
		sql = "select JOB_ID,BEG_MEA_TIME,END_MEA_TIME,CITY_ID,DL_FILE_NAME,RD_FILE_NAME,RESULT_DIR,FINISH_STATE,STATUS,CREATE_TIME,MOD_TIME from RNO_STRUCANA_JOB where job_id="
				+ jobId;
		List<Map<String, Object>> rawDatas = RnoHelper.commonQuery(stmt, sql);
		if (rawDatas == null || rawDatas.isEmpty()) {
			endTime = new Date();
			finishState = DataParseStatus.Failall.toString();
			attMsg = "不存在此结构分析需要的数据！";
			report.setFields(stage, begTime, endTime, finishState, attMsg);
			addJobReport(report);

			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());

			// 更新数据
			return status;
		}

		// 反射为对象
		RnoStructAnaJobRec jobRec = RnoHelper.commonInjection(
				RnoStructAnaJobRec.class, rawDatas.get(0),new DateUtil());
		log.debug("结构分析的数据信息：" + jobRec);

		// 获取门限
		sql = "select job_id,param_type,param_code,param_val from RNO_STRUCANA_JOB_PARAM where job_id="
				+ jobId;
		sql = "select job_id,param_type,param_code,param_val from RNO_STRUCANA_JOB_PARAM where param_type='STRUCTANA_THRESHOLD' AND job_id="
				+ jobId;
		rawDatas = RnoHelper.commonQuery(stmt, sql);
		if (rawDatas == null || rawDatas.isEmpty()) {
			// 取默认值
			sql = "select "
					+ jobId
					+ " as job_id,'STRUCTANA' as  param_type,CODE as  param_code,DEFAULT_VAL as param_val from RNO_THRESHOLD where module_type='STRUCTANA'";
			rawDatas = RnoHelper.commonQuery(stmt, sql);
		}

		if (rawDatas == null || rawDatas.isEmpty()) {
			// 缺少必须的参数，无法计算
		}
		RnoStructureAnalysisTask analysisTask=new RnoStructureAnalysisTask();
		RnoStructureAnalysisTask.TaskInfo ti= analysisTask.getTaskInfo();
		// ---装载参数---//
//		Threshold threshold = new Threshold();
		List<RnoThreshold> rnoThresholds=new ArrayList<RnoThreshold>();
		for (int i = 0; i < rawDatas.size(); i++) {
			Map map = (Map) rawDatas.get(i);
			String code = map.get("PARAM_CODE").toString();
			String val = map.get("PARAM_VAL").toString();
			
			RnoThreshold rnoThreshold=new RnoThreshold();
			rnoThreshold.setCode(code);
			rnoThreshold.setDefaultVal(val);
			rnoThresholds.add(rnoThreshold);
		}
		log.debug("结构分析的门限参数：" + rnoThresholds);
		//将门限装载到总分析任务中
		analysisTask.setRnoThresholds(rnoThresholds);
		//业务数据类型
		sql = "select job_id,param_type,param_code,param_val from RNO_STRUCANA_JOB_PARAM where param_type='STRUCTANA_DATATYPE' AND job_id="
				+ jobId;
		rawDatas = RnoHelper.commonQuery(stmt, sql);
		
		Map<String, Boolean> busDataTypes=new HashMap<String, Boolean>();
		
		for (int i = 0; i < rawDatas.size(); i++) {
			Map map = (Map) rawDatas.get(i);
			String code = map.get("PARAM_CODE").toString();
			String val = map.get("PARAM_VAL").toString();
			if("Y".equals(val)){
				busDataTypes.put(code, true);
			}else {
				busDataTypes.put(code, false);
			}
		}
		ti.setBusDataType(busDataTypes);
		log.debug("结构分析的数据类型：" + busDataTypes);
		//计算过程
		sql = "select job_id,param_type,param_code,param_val from RNO_STRUCANA_JOB_PARAM where param_type='STRUCTANA_CALPROCE' AND job_id="
				+ jobId;
		rawDatas = RnoHelper.commonQuery(stmt, sql);
		Map<String, Boolean> calProcedures=new HashMap<String, Boolean>();
		for (int i = 0; i < rawDatas.size(); i++) {
			Map map = (Map) rawDatas.get(i);
			String code = map.get("PARAM_CODE").toString();
			String val = map.get("PARAM_VAL").toString();
			if("Y".equals(val)){
				calProcedures.put(code, true);
			}else {
				calProcedures.put(code, false);
			}
		}
		ti.setCalProcedure(calProcedures);
		log.debug("结构分析的计算过程：" + calProcedures);
		// 更新进度
//		status.setJobRunningStatus(JobRunningStatus.Running);
		status.setJobState(JobState.Running);
		status.setUpdateTime(new Date());
		updateProgress(status);

		sql = "update RNO_STRUCANA_JOB set FINISH_STATE='开始分析' where job_id="
				+ jobId;
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 是否停止
		if (super.isStop()) {
			return clearWhenStop(job, connection, stmt);
		}

		// 开始分析
		ResultInfo resultInfo = new ResultInfo(false);
		try {
			begTime = new Date();
			/*resultInfo = structureAnalysisDaoV2.do2GStructAnalysis(worker,
					connection, jobRec, rnoThresholds);*/
			resultInfo = structureAnalysisDaoV2.do2GStructAnalysis(this,
					connection, jobRec, analysisTask);
			endTime = new Date();
			// 报告
			stage = "任务总结";
			if (resultInfo.isFlag()) {
				// 任务状态
//				status.setJobRunningStatus(JobRunningStatus.Succeded);
				status.setJobState(JobState.Finished);
				status.setUpdateTime(new Date());
				
				finishState = DataParseStatus.Succeded.toString();
				attMsg = "结构分析完成！";
			} else {
				// 任务状态
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				
				finishState = DataParseStatus.Failall.toString();
				attMsg = "结构分析异常！" + resultInfo.getMsg();
			}
			report.setFields(stage, begTime, endTime, finishState, attMsg);
			addJobReport(report);
		} catch (Exception e) {
			log.error(jobId + "的结构分析出错！");
			e.printStackTrace();
			endTime = new Date();
			// 状态
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			
			// 报告
			stage = "任务总结";
			finishState = DataParseStatus.Failall.toString();
			attMsg = "结构分析异常！";
			report.setFields(stage, begTime, endTime, finishState, attMsg);
			addJobReport(report);
		}
		log.debug("结构分析任务完成，result=" + resultInfo);
		status.setUpdateTime(new Date());
		
		// 更新结构分析表
		sql = "update RNO_STRUCANA_JOB set FINISH_STATE='分析完成' where job_id="
				+ jobId;
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 删除临时表的小区
		freeTableSpace(stmt);

		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	private void freeTableSpace(Statement stmt) {
		try {
			stmt.executeUpdate("truncate table rno_cell_city_t");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_2G_NCS_ANA_T");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_2G_MRR_ANA_T");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_NCS_WEIGHT_MID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_NCS_CLUSTER_CELL_RELA_MID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_NCS_CLUSTER_CELL_MID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_NCS_CLUSTER_MID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			stmt.executeUpdate("truncate table RNO_NCS_CELL_ANA_RESULT_MID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当要求stop时，做一些处理
	 * 
	 * @param job
	 * @param connection
	 * @param stmt
	 * @author brightming 2014-8-27 下午3:44:20
	 */
	private JobStatus clearWhenStop(JobProfile job, Connection connection,
			Statement stmt) {
		long jobId = job.getJobId();
		log.warn("本任务需要停止。" + job);
		try {
			stmt.executeUpdate("update RNO_STRUCANA_JOB set FINISH_STATE='被停止' where job_id="
					+ jobId);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			connection.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}

		// 任务
		JobStatus status = new JobStatus(jobId);
//		status.setJobRunningStatus(JobRunningStatus.Stopped);
		status.setJobState(JobState.Killed);
		status.setUpdateTime(new Date());
		return status;
	}

	@Override
	public void releaseRes() {
		if(stmt!=null){
			try {
					stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try {
					try{
						connection.setAutoCommit(true);
					}catch(Exception ee){
						
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if(jobStatus==null){
			return;
		}
		if(stmt!=null){
			try {
				String prog=jobStatus.getProgress();
				if(prog==null){
					prog="";
				}
				prog=prog.trim();
				if("".equals(prog)){
					prog=jobStatus.getJobState().getCode();
				}
				stmt.executeUpdate("update RNO_STRUCANA_JOB set FINISH_STATE='"+prog+"',mod_time=to_date('"+new DateUtil().format_yyyyMMddHHmmss(jobStatus.getUpdateTime())+"','yyyy-mm-dd hh24:mi:ss') where job_id="+jobStatus.getJobId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
