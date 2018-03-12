package com.iscreate.op.service.rno.task.matrix;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImpl;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImplV2;
import com.iscreate.op.dao.rno.RnoStructureQueryDaoImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class InterferMatrixJobRunnable extends BaseJobRunnable {

	private static Log log = LogFactory.getLog(InterferMatrixJobRunnable.class);

	private static String jobType = "CALCULATE_INTERFER_MATRIX";

	RnoStructureQueryDaoImpl structureQueryDao=null;
	private Connection connection ;
	private Statement stmt ;
	private long matrixRecId=-1;
	
	
	public InterferMatrixJobRunnable(){
		super(jobType);
	}
	
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

		structureQueryDao = new RnoStructureQueryDaoImpl();
		RnoStructureAnalysisDaoImpl structureAnalysisDao = new RnoStructureAnalysisDaoImpl();
		RnoStructureAnalysisDaoImplV2 structureAnalysisDaoV2 = new RnoStructureAnalysisDaoImplV2();

		// ---------准备数据库连接----------//
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		Date startTime = new Date();
		stmt = null;

		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("发生系统错误，无法创建数据库执行器！");
			// 更新干扰矩阵记录计算状态
			// structureQueryDao.updateInterMartixWorkStatus(jobId,"计算失败");
			// 保存报告信息
			report.setStage("创建数据库执行器");
			report.setBegTime(startTime);
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

//			status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		// 通过jobId获取干扰矩阵计算记录信息
		List<Map<String, Object>> interferMatrixRec = structureQueryDao
				.queryInterferMatrixRecByJobId(stmt, jobId);
		matrixRecId = Long.parseLong(interferMatrixRec.get(0)
				.get("MARTIX_DESC_ID").toString());
		String filePath = interferMatrixRec.get(0).get("FILE_PATH").toString();
		// String realDiskPath = ServletActionContext.getServletContext()
		// .getRealPath(filePath);
		String realDiskPath = FileInterpreter.makeFullPath(filePath);
		long cityId = Long.parseLong(interferMatrixRec.get(0).get("CITY_ID")
				.toString());
		String startMeaDate = interferMatrixRec.get(0).get("START_MEA_DATE")
				.toString();
		String endMeaDate = interferMatrixRec.get(0).get("END_MEA_DATE")
				.toString();

		// 更新干扰矩阵记录计算状态
		structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId,
				"正在计算");

		// 做数据处理，先关闭自动提交
		startTime = new Date();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			log.error("无法执行，发生系统错误，无法连接数据库！");
			// 更新干扰矩阵记录计算状态
			// structureQueryDao.updateInterMartixWorkStatus(jobId,"计算失败");
			// 保存报告信息
			report.setStage("准备数据库连接");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("无法连接数据库");
			addJobReport(report);
			// 关闭连接
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");

//			status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		// --------------计算干扰矩阵，并保存结果文件 start--------------//
		boolean result = true;

		DateUtil dateUtil=new DateUtil();
		// 初始化条件对象
		StructAnaTaskInfo structAnaTaskInfo = new StructAnaTaskInfo();
		structAnaTaskInfo.setCityId(cityId);
		structAnaTaskInfo.setStartDate(dateUtil
				.to_yyyyMMddHHmmssDate(startMeaDate));
		structAnaTaskInfo.setEndDate(dateUtil
				.to_yyyyMMddHHmmssDate(endMeaDate));

		Map<String, String> tableMap = new HashMap<String, String>();
		boolean flag = false;

		startTime = new Date();
		/*// 转移NCS数据到临时表
		log.debug(">>>>>>>>>>>>转移数据到临时表...");
		tableMap.put("sourceTab", "RNO_2G_ERI_NCS");
		tableMap.put("targetTab", "RNO_2G_ERI_NCS_T");
		tableMap.put("descTab", "RNO_2G_ERI_NCS_DESCRIPTOR");
		ResultInfo resultInfo = structureAnalysisDaoV2.transfer2GEriNcsToTempTable(stmt,
				structAnaTaskInfo, tableMap);
		log.debug("爱立信NCS数据转移到临时表结果=" + flag);
		if (resultInfo.isFlag()) {
			// 保存报告信息
			report.setStage("爱立信NCS数据转移到临时表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("成功");
			report.setAttMsg("爱立信NCS数据转移到临时表成功");
			addJobReport(report);
		} else {
			log.error("爱立信NCS数据转移到临时表失败");
			// 保存报告信息
			report.setStage("爱立信NCS数据转移到临时表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("爱立信NCS数据转移到临时表失败");
			addJobReport(report);

			log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");

			status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setUpdateTime(new Date());
			return status;
		}
*/
		// 准备数据
		startTime = new Date();
		log.debug(">>>>>>>>>>>>准备干扰矩阵所需数据...");
		tableMap.put("erincssourceTab", "RNO_2G_ERI_NCS");
		tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T");
		flag = structureAnalysisDaoV2.transfer2GEriNcsToUnifyAnaTable(stmt,
				structAnaTaskInfo, tableMap, null,null);
		log.debug("临时表的NCS数据转移到汇总分析表结果=" + flag);
		log.debug("<<<<<<<<<<<干扰矩阵所需数据准备完毕...");
		// 保存报告信息
		if (flag) {
			// 保存报告信息
			report.setStage("临时表的NCS数据转移到汇总分析表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("成功");
			report.setAttMsg("临时表的NCS数据转移到汇总分析表成功");
			addJobReport(report);
		} else {
			log.error("临时表的NCS数据转移到汇总分析表失败");
			// 保存报告信息
			report.setStage("临时表的NCS数据转移到汇总分析表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("临时表的NCS数据转移到汇总分析表失败");
			addJobReport(report);

			log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");

//			status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

//		try {
//			stmt.executeUpdate("delete from RNO_2G_eri_NCS_T");
//		} catch (SQLException e2) {
//			e2.printStackTrace();
//		}
		
		
//		startTime = new Date();
//		tableMap.put("sourceTab", "RNO_2G_HW_NCS");
//		tableMap.put("targetTab", "RNO_2G_HW_NCS_T");
//		tableMap.put("descTab", "RNO_2G_HW_NCS_DESC");
//		resultInfo = structureAnalysisDaoV2.transfer2GHwNcsToTempTable(stmt,
//				structAnaTaskInfo, tableMap);
//		log.debug("华为NCS数据转移到临时表结果=" + flag);
//		log.debug("<<<<<<<<<<<转移数据到临时表完毕...");
//		if (resultInfo.isFlag()) {
//			// 保存报告信息
//			report.setStage("华为NCS数据转移到临时表");
//			report.setBegTime(startTime);
//			report.setEndTime(new Date());
//			report.setFinishState("成功");
//			report.setAttMsg("华为NCS数据转移到临时表成功");
//			addJobReport(report);
//		} else {
//			log.error("爱立信NCS数据转移到临时表失败");
//			// 保存报告信息
//			report.setStage("华为NCS数据转移到临时表");
//			report.setBegTime(startTime);
//			report.setEndTime(new Date());
//			report.setFinishState("失败");
//			report.setAttMsg("华为NCS数据转移到临时表失败");
//			addJobReport(report);
//
//			log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
//			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
//					matrixRecId, "计算失败");
//
//			status.setJobRunningStatus(JobRunningStatus.Fail);
//			status.setUpdateTime(new Date());
//			return status;
//		}
		startTime = new Date();
		log.debug(">>>>>>>>>>>>准备干扰矩阵所需数据...");
		tableMap.put("tmphwncssourceTab", "RNO_2G_HW_NCS");
		tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T");
		flag = structureAnalysisDaoV2.transfer2GHwNcsToUnifyAnaTable(stmt,
				structAnaTaskInfo, tableMap, null,null);
		log.debug("临时表的NCS数据转移到汇总分析表结果=" + flag);
		log.debug("<<<<<<<<<<<干扰矩阵所需数据准备完毕...");
		// 保存报告信息
		if (flag) {
			// 保存报告信息
			report.setStage("临时表的NCS数据转移到汇总分析表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("成功");
			report.setAttMsg("临时表的NCS数据转移到汇总分析表成功");
			addJobReport(report);
		} else {
			log.error("临时表的NCS数据转移到汇总分析表失败");
			// 保存报告信息
			report.setStage("临时表的NCS数据转移到汇总分析表");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("临时表的NCS数据转移到汇总分析表失败");
			addJobReport(report);

			log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");

//			status.setJobRunningStatus(JobRunningStatus.Fail);
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

//		try {
//			stmt.executeUpdate("delete from RNO_2G_HW_NCS_T");
//		} catch (SQLException e2) {
//			e2.printStackTrace();
//		}
		// 计算干扰矩阵
		startTime = new Date();
		log.debug(">>>>>>>>>>>开始计算干扰矩阵.....");
		if (flag) {

			File file = new File(realDiskPath + File.separator);
			if (!file.isDirectory() && !file.exists()) {
				file.mkdirs();
			} else if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			String idxFilePath = realDiskPath + File.separator + "ncs_res_"
					+ matrixRecId
					+ RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
			String dataFilePath = realDiskPath + File.separator + "ncs_res_"
					+ matrixRecId
					+ RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;

			result = structureAnalysisDao.calculateFreqInterferMatrixResult(
					stmt, idxFilePath, dataFilePath);
			// 保存报告信息
			if (result) {
				// 保存报告信息
				report.setStage("计算生成干扰矩阵文件");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("成功");
				report.setAttMsg("计算生成干扰矩阵文件成功");
				addJobReport(report);
			} else {
				log.error("计算生成干扰矩阵文件失败");
				// 保存报告信息
				report.setStage("计算生成干扰矩阵文件");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("失败");
				report.setAttMsg("计算生成干扰矩阵文件失败");
				addJobReport(report);

				log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
				structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
						matrixRecId, "计算失败");

//				status.setJobRunningStatus(JobRunningStatus.Fail);
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				return status;
			}
			log.debug("<<<<<<<<干扰矩阵计算完毕。");
		}
		// --------------计算干扰矩阵，并保存结果文件 end--------------//

		// return result;
		// boolean res =
		// structureAnalysisDao.calculateInterferMartix(connection, stmt,
		// matrixRecId, realDiskPath, areaId, startMeaDate, endMeaDate, worker,
		// report);

		// 保存结果状态
		log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算完成");
		structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId,
				"计算完成");

		try {
			stmt.executeUpdate("truncate table RNO_2G_NCS_ANA_T");
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
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

//		status.setJobRunningStatus(JobRunningStatus.Succeded);
		status.setJobState(JobState.Finished);
		status.setUpdateTime(new Date());
		return status;
	}

	@Override
	public void releaseRes() {
		if(stmt!=null){
			try {
				if(!stmt.isClosed()){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try {
				if(!connection.isClosed()){
					try{
						connection.setAutoCommit(true);
					}catch(Exception ee){
						
					}
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if(stmt!=null && structureQueryDao!=null){
			//更新干扰矩阵计算表的进度
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId, jobStatus.getJobState().getCode());
		}
	}

}
