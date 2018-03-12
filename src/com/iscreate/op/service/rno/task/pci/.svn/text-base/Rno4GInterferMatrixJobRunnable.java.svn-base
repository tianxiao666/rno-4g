package com.iscreate.op.service.rno.task.pci;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.iscreate.op.dao.rno.Rno4GPciDaoImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.mapreduce.IscreateHadoopBaseJob;
import com.iscreate.op.service.rno.mapreduce.matrix.G4PciMatrixMapper;
import com.iscreate.op.service.rno.mapreduce.matrix.G4PciMatrixReducer;
import com.iscreate.op.service.rno.mapreduce.pci.PciMapper;
import com.iscreate.op.service.rno.mapreduce.pci.PciReducer;
import com.iscreate.op.service.rno.parser.vo.G4PciRec;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class Rno4GInterferMatrixJobRunnable extends BaseJobRunnable {


	private static Log log = LogFactory.getLog(Rno4GInterferMatrixJobRunnable.class);
	private static String jobType = "CALC_4G_INTERFER_MATRIX";
	
	Rno4GPciDaoImpl rno4gPciDao=null;
    private Connection connection;
    private Statement stmt;
    private long jobId=0;
    private long matrixRecId=-1;
	@Override
	public boolean isMyJob(JobProfile job) {
		if (job == null)
			return false;
		else {
			return jobType.equals(job.getJobType());
		}
	}

	@Override
	public JobStatus runJob(JobProfile job) throws InterruptedException {
		int n=0;
		// 获取工作id
		jobId = job.getJobId();
		// 初始化
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);
		rno4gPciDao=new Rno4GPciDaoImpl();
		// ---------准备数据库连接----------//
		log.debug("PCI 4g 干扰矩阵的job准备数据库连接");
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		Date startTime = new Date();
		stmt = null;

		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("发生系统错误，无法创建数据库执行器！");
			// 更新PCI计算状态
//			 structureQueryDao.updateInterMartixWorkStatus(jobId,"计算失败");
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
		List<Map<String, Object>> pciPlanRec =rno4gPciDao.query4GInterferMatrixRecByJobId(stmt, jobId);
		if(pciPlanRec.size() <= 0) {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		matrixRecId = Long.parseLong(pciPlanRec.get(0)
				.get("MARTIX_DESC_ID").toString());
		
		//
		String filePath = pciPlanRec.get(0).get("FILE_PATH")
				.toString();
		long cityId = Long.parseLong(pciPlanRec.get(0).get("CITY_ID")
				.toString());
		String startMeaDate = pciPlanRec.get(0).get("START_MEA_DATE")
				.toString();
		String endMeaDate = pciPlanRec.get(0).get("END_MEA_DATE")
				.toString();
		String fileName=pciPlanRec.get(0).get("FILE_NAME").toString();
		DateUtil dateUtil = new DateUtil();
		long startMeaMillis = dateUtil.parseDateArbitrary(startMeaDate).getTime();
		long endMeaMillis = dateUtil.parseDateArbitrary(endMeaDate).getTime();
		//
		String relaNumType="";
	
		//通过城市ID获取从基站标识到lte小区的映射集合：为的获取同站其他小区
		//同站小区判断条件变更由 enodeb->enodeb+earfcn
		Map<String, String> enodebToCells=rno4gPciDao.getEnodebIdForCellsMap(stmt, cityId);
		
		if(enodebToCells==null){
			report.setStage("通过城市ID获取从基站标识到lte小区的映射集合");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("该区域下的lte小区数据不存在");
			addJobReport(report);

			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		/************  PCI干扰计算 start ************/
		Configuration conf = new YarnConfiguration();
		log.debug("准备pci优化干扰矩阵的mapreduce任务...");

		String[] arr = new String[1]; 
		String nodebs=map2String(enodebToCells);
		arr[0]=nodebs;
//		System.out.println("nodebs:"+nodebs);   
		conf.setStrings("enodebToCells", arr);
		//分子
		conf.set("numerator", "RSRPTIMES0");
		//生成文件路径
		/*String idxHdfsPath = filePath + File.separator + "ncs_res_"
				+ matrixRecId + RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
		String dataHdfsPath = filePath + File.separator + "ncs_res_"
				+ matrixRecId + RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;	
		conf.set("idxPath", idxHdfsPath);
		conf.set("dataPath", dataHdfsPath);*/
		String pciOriPath=filePath+"/"+fileName;
		System.out.println("pciOriPath   文件路径是===============>>>>>>>"+pciOriPath);
		conf.set("pciOriPath", pciOriPath);
		// 更新pci优化干扰矩阵状态
		rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId, "正在计算");
		
		Job pciJob = null;
		try {
			pciJob = Job.getInstance(conf,"PciMatrix_Worker");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("创建hadoop集群用于pci优化干扰矩阵的job失败！");
			// 保存报告信息
			report.setStage("创建hadoop pci优化干扰矩阵job");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("创建hadoop pci优化干扰矩阵job失败");
			addJobReport(report);

			rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId,  "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		
		
		Path path = new Path(filePath+"/out");
		try {
			FileSystem fs = FileSystem.get(conf);
			fs.delete(path, true);
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error("设置hadoop集群用于 pci优化干扰矩阵job的输出路径失败！");
			// 保存报告信息
			report.setStage("设置hadoop pci优化干扰矩阵job输出路径");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("设置hadoop pci优化干扰矩阵job输出路径失败");
			addJobReport(report);

			rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		//结果输出路径
        FileOutputFormat.setOutputPath(pciJob, path);
		log.debug("组装rowkey：cityId="+cityId+",开始日期毫秒表示="+startMeaMillis+",结束日期毫秒表示="+endMeaMillis);
        //结果输出路径
        
		
		
		//判断汇总后的MR
//		String fileName=UUID.randomUUID().toString().replaceAll("-", "");
	
		 pciJob.setJarByClass(IscreateHadoopBaseJob.class);
//		 System.out.println("运行模式：  "+conf.get("mapred.job.tracker"));
		 pciJob.setMapperClass(G4PciMatrixMapper.class);
		 pciJob.setReducerClass(G4PciMatrixReducer.class);
		 pciJob.setOutputKeyClass(Text.class);
		 pciJob.setOutputValueClass(Text.class);
//		 FileSystem fs=FileSystem.get(conf);
		
		List<Scan> scans = new ArrayList<Scan>();
		//cityId+"_"+startMeaMillis+"_", cityId+"_"+endMeaMillis+"~"
		//mr 表数据
		Scan scanEriMr = new Scan();
		//加入这两句速度快很多
		scanEriMr.setCacheBlocks(false);
		scanEriMr.setCaching(500);
		scanEriMr.setStartRow(Bytes.toBytes(cityId+"_"+startMeaMillis+"_#"));
		scanEriMr.setStopRow(Bytes.toBytes(cityId+"_"+endMeaMillis+"_~"));
		scanEriMr.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ERI_MR"));
//		Filter filter=new SingleColumnValueFilter("NCSINFO".getBytes(), "DISTANCE".getBytes(), CompareOp.LESS_OR_EQUAL, "3".getBytes());
		scans.add(scanEriMr);
		
		Scan scanZteMr = new Scan();
		//加入这两句速度快很多
		scanZteMr.setCacheBlocks(false);
		scanZteMr.setCaching(500);
		scanZteMr.setStartRow(Bytes.toBytes(cityId+"_"+startMeaMillis+"_#"));
		scanZteMr.setStopRow(Bytes.toBytes(cityId+"_"+endMeaMillis+"_~"));
		scanZteMr.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ZTE_MR"));
//		Filter filter=new SingleColumnValueFilter("NCSINFO".getBytes(), "DISTANCE".getBytes(), CompareOp.LESS_OR_EQUAL, "3".getBytes());
		scans.add(scanZteMr);
		try {
//			 FileInputFormat.addInputPath(pciJob, new Path(pciOriPath));
			 TableMapReduceUtil.initTableMapperJob(scans, G4PciMatrixMapper.class, Text.class, Text.class, pciJob);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("初始化 pci优化干扰矩阵job的Mapper失败！");
			// 保存报告信息
			report.setStage("初始化 pci优化干扰矩阵job的Mapper");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("初始化 pci优化干扰矩阵job的Mapper失败");
			addJobReport(report);

			rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId,  "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}
		 try {
	        	pciJob.submit();
	        	int progMonitorPollIntervalMillis = 
	        		      Job.getProgressPollInterval(conf);
	        	JobID pciJobId = pciJob.getJobID();
	        	while(!pciJob.isComplete()) {
	        		log.info("jobId="+pciJobId+",名称="+job.getJobName()+"执行中...");
	        		log.info(" map " + StringUtils.formatPercent(pciJob.mapProgress(), 0)+
	        		            " reduce " +  StringUtils.formatPercent(pciJob.reduceProgress(), 0));
	        		Thread.sleep(progMonitorPollIntervalMillis);
	        	}
				//job.waitForCompletion(true);
	        	
	        	if(pciJob.isSuccessful()) {
	        		log.info("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务成功！");
	        		log.info("pci优化干扰矩阵完成");
	    			// 保存报告信息
	    			//report.setStage("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务成功！");
	    			report.setBegTime(startTime);
	    			report.setEndTime(new Date());
	    			report.setFinishState("成功");
	    			report.setAttMsg("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务成功！");
	    			addJobReport(report);
	        		rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId, "计算完成");
	        	} else {
	        		log.info("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务失败！");
	        		log.info("pci优化干扰矩阵失败");
	        		// 保存报告信息
	    			//report.setStage("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务失败！");
	    			report.setBegTime(startTime);
	    			report.setEndTime(new Date());
	    			report.setFinishState("失败");
	    			report.setAttMsg("集群jobId="+pciJobId+",名称="+job.getJobName()+",结果：任务失败！");
	    			addJobReport(report);
	        		rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId, "计算失败");
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
		/************  pci优化干扰矩阵，并保存结果文件 end ************/
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if(stmt!=null && rno4gPciDao!=null){
			//更新pci优化干扰矩阵表的进度
			rno4gPciDao.update4GInterMatrixWorkStatusByStmt(stmt, matrixRecId, jobStatus.getJobState().getCode());
		}
	}
	public static String map2String(Map<String, String> enodebToCells) {
		String str="";
		for (String key : enodebToCells.keySet()) {
			str+=key+"="+enodebToCells.get(key)+"|";
		}
		return str.substring(0, str.length()-1);
	}
}
