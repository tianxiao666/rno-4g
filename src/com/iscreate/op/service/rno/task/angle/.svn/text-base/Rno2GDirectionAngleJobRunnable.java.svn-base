package com.iscreate.op.service.rno.task.angle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.iscreate.op.dao.rno.RnoNcsDynaCoverageDaoImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class Rno2GDirectionAngleJobRunnable extends BaseJobRunnable {
	private static Log log = LogFactory.getLog(Rno2GDirectionAngleJobRunnable.class);

	private static String jobType = "CAL_2G_DIRECTION_ANGLE";

	private RnoNcsDynaCoverageDaoImpl dynaCoverageDaoImpl = null;
	private Connection connection ;
	private Statement stmt ;
	private long descId=-1;
	
	
	public Rno2GDirectionAngleJobRunnable(){
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

		dynaCoverageDaoImpl = new RnoNcsDynaCoverageDaoImpl();

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

		// 通过jobId获取任务信息
		List<Map<String, Object>> task = dynaCoverageDaoImpl
				.queryDirectionAngleTaskByJobId(stmt, jobId);
		descId = Long.parseLong(task.get(0)
				.get("DESC_ID").toString());
		String filePath = task.get(0).get("FILE_PATH").toString();
		long cityId = Long.parseLong(task.get(0).get("CITY_ID")
				.toString());
		String startMeaDate = task.get(0).get("START_MEA_DATE")
				.toString();
		String endMeaDate = task.get(0).get("END_MEA_DATE")
				.toString();

		// 更新任务信息计算状态
		dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
				"正在计算");

		// 做数据处理，先关闭自动提交
		startTime = new Date();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			log.error("无法执行，发生系统错误，无法连接数据库！");
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

			log.info("descId[" + descId + "]2g小区方向角计算失败");
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
					"计算失败");

			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		// --------------计算，并保存结果文件 start--------------//
		//计算完成后提供csv文件下载，输出字段：小区名称、小区ID、现网方向角、计算方向角、方向角差值。
		//方向角计算标准：正北为0顺时针计算，以RELSS>-12的比例计算。
		
		Map<String, Map<String, String>> cellIdToInfo = dynaCoverageDaoImpl
				.queryCellsByCityId(stmt, cityId);
		
		// 保存报告信息
		if (cellIdToInfo.size() <= 0) {
			log.error("城市id="+cityId+"，获取小区信息数量为0");
			// 保存报告信息
			report.setStage("获取小区信息数据");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("获取小区信息数量为0");
			addJobReport(report);

			log.info("descId[" + descId + "]2g小区方向角计算失败");
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
					"计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		//获取所需的计算数据
		List<Map<String,Object>> dataList = dynaCoverageDaoImpl
				.queryDataForCalc2GDirectionAngle(stmt, cityId, startMeaDate, endMeaDate);
		// 保存报告信息
		if (dataList.size() <= 0) {
			log.error("城市id="+cityId+"，获取小区信息数量为0");
			// 保存报告信息
			report.setStage("获取计算2g小区方向角数据");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("获取计算2g小区方向角数据失败");
			addJobReport(report);

			log.info("descId[" + descId + "]2g小区方向角计算失败");
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
					"计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		//计算方向角并保存在csv文件
		File realdir = new File(filePath);
		if(!realdir.exists()){
			realdir.mkdirs();
		}
		File csvFile = new File(filePath+"/"+jobId+"_2G方向角.csv");
		if(!csvFile.exists()){
			try {
				csvFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				log.info("descId[" + descId + "]2g小区方向角计算失败");
				dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
						"计算失败");
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				return status;
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(csvFile, true), "gbk"));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("descId[" + descId + "]2g小区方向角计算失败");
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
					"计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		try {
			//标题
			bw.write("小区名,CELL,现网方向角,计算方向角,方向角差值");
			bw.newLine();
			double lng,lat,lngDiff,latDiff,sinV;
			int azimuth=0;
			String cellId = "";
			String cellName = "";
			String currentAzimuth = "";
			for (Map<String, Object> map : dataList) {
				lng = Double.parseDouble(map.get("LNG").toString());
				lat = Double.parseDouble(map.get("LAT").toString());
				lngDiff = Double.parseDouble(map.get("LNG_DIFF").toString());
				latDiff = Double.parseDouble(map.get("LAT_DIFF").toString());
				cellId = map.get("CELL").toString();
				
//				if("MZSJYN1".equals(cellId)) {
//					System.out.println("");
//				}
				
				//过滤分母为0的
				if((lngDiff-lng)==0 && (latDiff-lat)==0) {
					continue;
				}
				//按照不同象限判断角度大小，正北为0，顺时针增加
				//第一象限
				if(lngDiff>lng && latDiff>=lat) {
					sinV = (lngDiff - lng)
							/ (Math.sqrt((lngDiff - lng) * (lngDiff - lng)
									+ (latDiff - lat) * (latDiff - lat)));
					azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
				}
				//第二象限
				else if(lngDiff<=lng && latDiff>lat) {
					sinV = (lngDiff - lng)
							/ (Math.sqrt((lngDiff - lng) * (lngDiff - lng)
									+ (latDiff - lat) * (latDiff - lat)));
					azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
					//asin在第二象限算出来的值为负值，改为正
					azimuth = -azimuth;
					//正北为0且以顺时针方向增加
					azimuth = 360 - azimuth;
				}
				//第三象限
				else if(lngDiff<lng && latDiff<=lat) {
					sinV = (lngDiff - lng)
							/ (Math.sqrt((lngDiff - lng) * (lngDiff - lng)
									+ (latDiff - lat) * (latDiff - lat)));
					azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
					//asin在第三象限算出来的值为负值，改为正
					azimuth = -azimuth;
					//正北为0且以顺时针方向增加
					azimuth = 180 + azimuth;
				}
				//第四象限
				else if(lngDiff>=lng && latDiff<lat) {
					sinV = (lngDiff - lng)
							/ (Math.sqrt((lngDiff - lng) * (lngDiff - lng)
									+ (latDiff - lat) * (latDiff - lat)));
					azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
					//正北为0且以顺时针方向增加
					azimuth = 180 - azimuth;
				}
				if(cellIdToInfo.get(cellId) != null) {
					cellName = cellIdToInfo.get(cellId).get("CELL_NAME");
					currentAzimuth = cellIdToInfo.get(cellId).get("AZIMUTH");
				}

				String line = "";
				if(("").equals(currentAzimuth)) {
					line = "\""+cellName
							+"\",\""+cellId
							+"\",\""+currentAzimuth
							+"\",\""+azimuth
							+"\",\""+azimuth+"\"";
				} else {
					int ca = Integer.parseInt(currentAzimuth);
					int diffAzimuth = Math.abs(azimuth-ca);
					int curAzimuthInt = diffAzimuth>180?360-diffAzimuth:diffAzimuth;

					line = "\""+cellName
						+"\",\""+cellId
						+"\",\""+currentAzimuth
						+"\",\""+azimuth
						+"\",\""+curAzimuthInt+"\"";
				}
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("descId[" + descId + "]2g小区方向角计算失败");
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
					"计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// --------------计算，并保存结果文件 end--------------//

		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 保存结果状态
		log.info("descId[" + descId + "]干扰矩阵2g小区方向角计算完成");
		dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId,
				"计算完成");

		status.setJobState(JobState.Finished);
		status.setUpdateTime(new Date());
		return status;
	}

	@Override
	public void releaseRes() {

		
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if(stmt!=null && dynaCoverageDaoImpl!=null){
			//更新任务的进度
			dynaCoverageDaoImpl.updateDirectionAngleTaskWorkStatusByStmt(stmt, descId, jobStatus.getJobState().getCode());
		}
	}

}
