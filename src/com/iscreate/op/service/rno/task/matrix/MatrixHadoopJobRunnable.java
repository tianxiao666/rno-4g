package com.iscreate.op.service.rno.task.matrix;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureQueryDaoImpl;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.mapreduce.IscreateHadoopBaseJob;
import com.iscreate.op.service.rno.mapreduce.matrix.MatrixMapper;
import com.iscreate.op.service.rno.mapreduce.matrix.MatrixReducer;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class MatrixHadoopJobRunnable extends BaseJobRunnable {
	private static Log log = LogFactory.getLog(MatrixHadoopJobRunnable.class);

	private static String jobType = "CALCULATE_INTERFER_MATRIX";

	RnoStructureQueryDaoImpl structureQueryDao=null;
	private Connection connection ;
	private Statement stmt ;
	private long matrixRecId=-1;
	
//	public static void main(String[] args) {
//		Configuration conf = new Configuration();
//		conf.set("maprd.jar", "/op/rno/mapreduce/Matrix.jar");
//		System.out.println(conf.get("maprd.jar")+"  配置文件： "+conf);
//	}
	public MatrixHadoopJobRunnable(){
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
		structureQueryDao = new RnoStructureQueryDaoImpl();
		// ---------准备数据库连接----------//
		log.debug("计算干扰矩阵的job准备数据库连接");
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

			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		
		// 通过jobId获取干扰矩阵计算记录信息
		List<Map<String, Object>> interferMatrixRec = structureQueryDao
				.queryInterferMatrixRecByJobId(stmt, jobId);
		matrixRecId = Long.parseLong(interferMatrixRec.get(0)
				.get("MARTIX_DESC_ID").toString());
		String filePath = interferMatrixRec.get(0).get("FILE_PATH")
				.toString();
		long cityId = Long.parseLong(interferMatrixRec.get(0).get("CITY_ID")
				.toString());
		String startMeaDate = interferMatrixRec.get(0).get("START_MEA_DATE")
				.toString();
		String endMeaDate = interferMatrixRec.get(0).get("END_MEA_DATE")
				.toString();
		DateUtil dateUtil = new DateUtil();
		long startMeaMillis = dateUtil.parseDateArbitrary(startMeaDate).getTime();
		long endMeaMillis = dateUtil.parseDateArbitrary(endMeaDate).getTime();

		// 更新干扰矩阵记录计算状态
		structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId,
				"正在计算");
		
		/************ Hadoop计算干扰矩阵，并保存结果文件 start ************/
		log.debug("准备计算干扰矩阵的mapreduce任务...");
//		System.setProperty("HADOOP_USER_NAME", "rnohbase");
		
		Configuration conf = new YarnConfiguration();
		//conf.set("maprd.jar", "/opt/tomcat/apache-tomcat-7.0.55/webapps/ops/op/rno/mapreduce/Matrix.jar");
		//System.out.println(conf.get("maprd.jar")+"  配置文件： "+conf);
		//System.out.println(new File(conf.get("maprd.jar")).exists());
		//hbase所用到的表
		conf.set("2gHwNcsHbaseTable","RNO_2G_HW_NCS_HBASE");
		conf.set("2gEriNcsHbaseTable","RNO_2G_ERI_NCS_HBASE");
		//生成文件路径
		String idxHdfsPath = filePath + File.separator + "ncs_res_"
				+ matrixRecId + RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
		String dataHdfsPath = filePath + File.separator + "ncs_res_"
				+ matrixRecId + RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;	
		conf.set("idxPath", idxHdfsPath);
		conf.set("dataPath", dataHdfsPath);
		//取默认门限值
		String sql = "select "
				+ jobId
				+ " as job_id,'STRUCTANA' as  param_type,CODE as  param_code,DEFAULT_VAL as param_val from RNO_THRESHOLD where module_type='STRUCTANA'";
		List<Map<String,Object>> rawDatas = RnoHelper.commonQuery(stmt, sql);
		List<RnoThreshold> rnoThresholds = new ArrayList<RnoThreshold>();
		for (int i = 0; i < rawDatas.size(); i++) {
			Map map = (Map) rawDatas.get(i);
			String code = map.get("PARAM_CODE").toString();
			String val = map.get("PARAM_VAL").toString();
			
			RnoThreshold rnoThreshold=new RnoThreshold();
			rnoThreshold.setCode(code);
			rnoThreshold.setDefaultVal(val);
			rnoThresholds.add(rnoThreshold);
		}
		// 最远允许距离
		String farestDis = "8";// km
		// 距离大，ci也大。默认 距离大于3千米，非邻区同频干扰系数大于20%。
		String farDisWithLargeCI_dis = "3";// km
		String farDisWithLargeCi_ci = "0.2";
		// 样本小，ci大
		String littleSampleWithLargeCI_sample = "5000";
		String littleSampleWithLargeCI_ci = "0.5";
		// 样本大，ci小
		String hugeSampleWithTinyCi_sample = "10000";
		String hugeSampleWithTinyCi_ci = "0.00105";
		// 最小的有效样本数量
		String leastSampleCnt = "1000";
		
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("INTERFACTORMOSTDISTANT".toUpperCase())) {
					farestDis = val;
				}
				if (code.equals("OVERSHOOTINGCOEFRFFERDISTANT".toUpperCase())) {
					farDisWithLargeCI_dis = val;
				}
				if (code.equals("NONNCELLSAMEFREQINTERCOEF".toUpperCase())) {
					farDisWithLargeCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTSMALL".toUpperCase())) {
					littleSampleWithLargeCI_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFBIG".toUpperCase())) {
					littleSampleWithLargeCI_ci = val;
				}

				if (code.equals("TOTALSAMPLECNTBIG".toUpperCase())) {
					hugeSampleWithTinyCi_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFSMALL".toUpperCase())) {
					hugeSampleWithTinyCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTTOOSMALL".toUpperCase())) {
					leastSampleCnt = val;
				}
			}
		}
		conf.set("farestDis", farestDis);
		conf.set("farDisWithLargeCI_dis",farDisWithLargeCI_dis);
		conf.set("farDisWithLargeCi_ci",farDisWithLargeCi_ci);
		conf.set("littleSampleWithLargeCI_sample", littleSampleWithLargeCI_sample);
		conf.set("littleSampleWithLargeCI_ci", littleSampleWithLargeCI_ci);
		conf.set("hugeSampleWithTinyCi_ci", hugeSampleWithTinyCi_ci);
		conf.set("leastSampleCnt", leastSampleCnt);
		log.info("门限值：farestDis=" + farestDis + ",farDisWithLargeCI_dis="
				+ farDisWithLargeCI_dis + ",farDisWithLargeCi_ci="
				+ farDisWithLargeCi_ci + ",littleSampleWithLargeCI_sample="
				+ littleSampleWithLargeCI_sample + ",littleSampleWithLargeCI_ci="
				+ littleSampleWithLargeCI_ci + ",hugeSampleWithTinyCi_ci="
				+ hugeSampleWithTinyCi_ci + ",leastSampleCnt=" + leastSampleCnt);
		Job mrJob = null;
		try {
			mrJob = Job.getInstance(conf,"Matrix_Worker");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("创建hadoop集群用于计算干扰矩阵的job失败！");
			// 保存报告信息
			report.setStage("创建hadoop计算干扰矩阵job");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("创建hadoop计算干扰矩阵job失败");
			addJobReport(report);

			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		
		//mrJob.setJarByClass(MatrixHadoopJobRunnable.class);
		mrJob.setJarByClass(IscreateHadoopBaseJob.class);
		mrJob.setReducerClass(MatrixReducer.class);
		mrJob.setOutputKeyClass(Text.class);
		mrJob.setOutputValueClass(Text.class);
        
		Path path = new Path(filePath+"/out");
		try {
			FileSystem fs = FileSystem.get(conf);
			fs.delete(path, true);
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error("设置hadoop集群用于计算干扰矩阵job的输出路径失败！");
			// 保存报告信息
			report.setStage("设置hadoop计算干扰矩阵job输出路径");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("设置hadoop计算干扰矩阵job输出路径失败");
			addJobReport(report);

			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		log.debug("组装rowkey：cityId="+cityId+",开始日期毫秒表示="+startMeaMillis+",结束日期毫秒表示="+endMeaMillis);
        //结果输出路径
        FileOutputFormat.setOutputPath(mrJob, path);
		
		List<Scan> scans = new ArrayList<Scan>();
		Scan scanHw = new Scan();
		//加入这两句速度快很多
		scanHw.setCacheBlocks(false);
		scanHw.setCaching(500); 
		scanHw.setStartRow(Bytes.toBytes(cityId+"_"+startMeaMillis+"_"));
		scanHw.setStopRow(Bytes.toBytes(cityId+"_"+endMeaMillis+"~"));
//		//距离小于等于8km
//		Filter filterHwDis = new SingleColumnValueFilter(Bytes
//				.toBytes("NCSINFO"), Bytes.toBytes("DISTANCE"),
//				CompareOp.LESS_OR_EQUAL, Bytes.toBytes("8")); 
//		scanHw.setFilter(filterHwDis);
//		//S3013要大于等于1
//		Filter filterHwS3013 = new SingleColumnValueFilter(Bytes
//				.toBytes("NCSINFO"), Bytes.toBytes("S3013"),
//				CompareOp.GREATER_OR_EQUAL, Bytes.toBytes("1")); 
//		scanHw.setFilter(filterHwS3013);
		scanHw.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("RNO_2G_HW_NCS_HBASE"));
		scans.add(scanHw);
		
		Scan scanEri = new Scan();
		//加入这两句速度快很多
		scanEri.setCacheBlocks(false);
		scanEri.setCaching(500);
		scanEri.setStartRow(Bytes.toBytes(cityId+"_"+startMeaMillis+"_"));
		scanEri.setStopRow(Bytes.toBytes(cityId+"_"+endMeaMillis+"~"));
//		scanEri.setStartRow(Bytes.toBytes("99_"));
//		scanEri.setStopRow(Bytes.toBytes("99~"));
		scanEri.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("RNO_2G_ERI_NCS_HBASE"));
		scans.add(scanEri);
		
		try {
			TableMapReduceUtil.initTableMapperJob(scans,
					MatrixMapper.class, Text.class, Text.class, mrJob);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("初始化干扰矩阵job的Mapper失败！");
			// 保存报告信息
			report.setStage("初始化干扰矩阵job的Mapper");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("初始化干扰矩阵job的Mapper失败");
			addJobReport(report);

			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt,
					matrixRecId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		log.debug("注册集群用于计算干扰矩阵的job...");
        try {
        	mrJob.submit();
        	int progMonitorPollIntervalMillis = 
        		      Job.getProgressPollInterval(conf);
        	JobID mrJobId = mrJob.getJobID();
        	
        	while(!mrJob.isComplete()) {
        		log.info("jobId="+mrJobId+",名称="+job.getJobName()+"执行中...");
        		log.info(" map " + StringUtils.formatPercent(mrJob.mapProgress(), 0)+
        		            " reduce " +  StringUtils.formatPercent(mrJob.reduceProgress(), 0));
        		Thread.sleep(progMonitorPollIntervalMillis);
        	}
			//job.waitForCompletion(true);
        	
        	if(mrJob.isSuccessful()) {
        		log.info("集群jobId="+mrJobId+",名称="+job.getJobName()+",结果：任务成功！");
        		log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算完成");
        		structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId,
        				"计算完成");
        	} else {
        		log.info("集群jobId="+mrJobId+",名称="+job.getJobName()+",结果：任务失败！");
        		log.info("matrixRecId[" + matrixRecId + "]干扰矩阵计算失败");
        		structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId,
        				"计算失败");
        		status.setJobState(JobState.Failed);
        		status.setUpdateTime(new Date());
        		return status;
        	}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/************ Hadoop计算干扰矩阵，并保存结果文件 end ************/
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
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		status.setJobState(JobState.Finished);
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
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if(stmt!=null && structureQueryDao!=null){
			//更新干扰矩阵计算表的进度
			structureQueryDao.updateInterMatrixWorkStatusByStmt(stmt, matrixRecId, jobStatus.getJobState().getCode());
		}
	}


}
