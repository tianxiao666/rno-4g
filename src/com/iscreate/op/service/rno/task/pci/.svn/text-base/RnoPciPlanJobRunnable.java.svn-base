package com.iscreate.op.service.rno.task.pci;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.iscreate.op.dao.rno.RnoStructureQueryDaoImpl;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.BaseJobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.mapreduce.IscreateHadoopBaseJob;
import com.iscreate.op.service.rno.mapreduce.pci.PciMapper;
import com.iscreate.op.service.rno.mapreduce.pci.PciReducer;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoPciPlanJobRunnable extends BaseJobRunnable {

	private static Log log = LogFactory.getLog(RnoPciPlanJobRunnable.class);
	private static String jobType = "RNO_PCI_PLAN";

	RnoStructureQueryDaoImpl structureQueryDao = null;
	private Connection connection;
	private Statement stmt;
	private long jobId = 0;

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
		// 获取工作id
		jobId = job.getJobId();

		// 初始化 Job 信息
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);
		structureQueryDao = new RnoStructureQueryDaoImpl();

		// 创建数据库连接
		log.debug("PCI 干扰计算的 job 准备数据库连接。");
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		connection = DataSourceConn.initInstance().getConnection();
		Date startTime = new Date();
		stmt = null;

		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("发生系统错误，无法创建数据库执行器！");

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

		// 通过 jobId 获取干扰矩阵计算记录信息(rno_lte_pci_job表），包括变小区的 CLOB 信息
		List<Map<String, Object>> pciPlanRec = structureQueryDao.queryPciPlanJobRecByJobId(stmt, jobId);

		if (pciPlanRec.size() <= 0) {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		long cityId = Long.parseLong(pciPlanRec.get(0).get("CITY_ID").toString());
		String startMeaDate = pciPlanRec.get(0).get("BEG_MEA_TIME").toString();
		String endMeaDate = pciPlanRec.get(0).get("END_MEA_TIME").toString();
		String filePath = pciPlanRec.get(0).get("RESULT_DIR").toString();
		String planType = pciPlanRec.get(0).get("PLAN_TYPE").toString();
		String converType = pciPlanRec.get(0).get("CONVER_TYPE").toString();
		String relaNumType = pciPlanRec.get(0).get("RELA_NUM_TYPE").toString();
		String optimizeCells = pciPlanRec.get(0).get("OPTIMIZE_CELLS").toString();
		String rdFileName = pciPlanRec.get(0).get("RD_FILE_NAME").toString();

		System.out.println("======>>>测量数据开始时间和结束时间：" + startMeaDate + ", " + endMeaDate);

		DateUtil dateUtil = new DateUtil();
		long startMeaMillis = dateUtil.parseDateArbitrary(startMeaDate).getTime();
		long endMeaMillis = dateUtil.parseDateArbitrary(endMeaDate).getTime();

		// 获取指定城市的全部同站小区，以 ENODEBID_EARFCN 为 key，多个小区CELL以#连接，一般一个ENODEBID_EARFCN的同站小区是三个。
		// 这是从 RNO_LTE_ENODEB 表中获取的，或者说是从公参表中获取的
		// @author chao.xj 2015-5-6 下午2:48:25
		// 同站小区判断条件变更由 enodeb->enodeb+earfcn
		Map<String, String> enodebToCells = structureQueryDao.getEnodebIdForCellsMap(stmt, cityId);
		
		if (enodebToCells == null) {
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
		System.out.println("获取到基站与小区的映射enodebToCells的数量为："+enodebToCells.size());
		/************ Hadoop PCI干扰计算 start ************/
		log.debug("准备PCI规划干扰计算的mapreduce任务...");

		Configuration conf = new YarnConfiguration();

		String nodebs = map2String(enodebToCells);

		// 设置同站小区的公参数据
		conf.set("enodebToCells", nodebs);

		// 设置“关联度分子字段名”
		conf.set("numeratorField", relaNumType);

		// 设置其他数据
		conf.set("RESULT_DIR", filePath);
		conf.set("RD_FILE_NAME", rdFileName);
		conf.set("PLAN_TYPE", planType);
		conf.set("CONVER_TYPE", converType);
		conf.set("OPTIMIZE_CELLS", optimizeCells);

		// 获取页面自定义的阈值门限值
		List<RnoThreshold> rnoThresholds = new ArrayList<RnoThreshold>();
		String sql = "select job_id, param_type, param_code, param_val from rno_lte_pci_job_param where job_id = "
				+ jobId;
		List<Map<String, Object>> rawDatas = RnoHelper.commonQuery(stmt, sql);

		if (rawDatas == null || rawDatas.size() == 0) {
			// 取默认门限值
			sql = "select " + jobId
					+ " as job_id, 'STRUCTANA' as param_type, CODE as param_code, DEFAULT_VAL as param_val"
					+ " from RNO_THRESHOLD where module_type = 'LTEINTERFERCALC'";
			rawDatas = RnoHelper.commonQuery(stmt, sql);
		}

		for (int i = 0; i < rawDatas.size(); i++) {
			Map<String, Object> map = rawDatas.get(i);
			String code = map.get("PARAM_CODE").toString();
			String val = map.get("PARAM_VAL").toString();

			RnoThreshold rnoThreshold = new RnoThreshold();
			rnoThreshold.setCode(code);
			rnoThreshold.setDefaultVal(val);
			rnoThresholds.add(rnoThreshold);
		}

		String samefreqcellcoefweight = "0.8"; // 权值
		String switchratioweight = "0.2"; // 切换比例权值

		String cellm3rinterfercoef = "1";
		String cellm6rinterfercoef = "0.8";
		String cellm30rinterfercoef = "0.1";

		String beforenstrongcelltab = "6";
		String topncelllist = "10";

		String increasetopncelllist = "5";
		String convermethod1targetval = "5";
		String convermethod2targetval = "5";
		String convermethod2scoren = "10";

		String mincorrelation = "2";
		String minmeasuresum = "500";

		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("SAMEFREQCELLCOEFWEIGHT".toUpperCase())) {
					samefreqcellcoefweight = val;
				}
				if (code.equals("SWITCHRATIOWEIGHT".toUpperCase())) {
					switchratioweight = val;
				}
				if (code.equals("CELLM3RINTERFERCOEF".toUpperCase())) {
					cellm3rinterfercoef = val;
				}
				if (code.equals("CELLM6RINTERFERCOEF".toUpperCase())) {
					cellm6rinterfercoef = val;
				}
				if (code.equals("CELLM30RINTERFERCOEF".toUpperCase())) {
					cellm30rinterfercoef = val;
				}

				if (code.equals("BEFORENSTRONGCELLTAB".toUpperCase())) {
					beforenstrongcelltab = val;
				}
				if (code.equals("TOPNCELLLIST".toUpperCase())) {
					topncelllist = val;
				}
				if (code.equals("INCREASETOPNCELLLIST".toUpperCase())) {
					increasetopncelllist = val;
				}
				if (code.equals("CONVERMETHOD1TARGETVAL".toUpperCase())) {
					convermethod1targetval = val;
				}
				if (code.equals("CONVERMETHOD2TARGETVAL".toUpperCase())) {
					convermethod2targetval = val;
				}
				if (code.equals("CONVERMETHOD2SCOREN".toUpperCase())) {
					convermethod2scoren = val;
				}
				if (code.equals("MINCORRELATION".toUpperCase())) {
					mincorrelation = val;
				}
				if (code.equals("MINMEASURESUM".toUpperCase())) {
					minmeasuresum = val;
				}
			}
		}

		conf.set("samefreqcellcoefweight", samefreqcellcoefweight);
		conf.set("switchratioweight", switchratioweight);
		conf.set("cellm3rinterfercoef", cellm3rinterfercoef);
		conf.set("cellm6rinterfercoef", cellm6rinterfercoef);
		conf.set("cellm30rinterfercoef", cellm30rinterfercoef);
		conf.set("beforenstrongcelltab", beforenstrongcelltab);
		conf.set("topncelllist", topncelllist);
		conf.set("increasetopncelllist", increasetopncelllist);
		conf.set("convermethod1targetval", convermethod1targetval);
		conf.set("convermethod2targetval", convermethod2targetval);
		conf.set("convermethod2scoren", convermethod2scoren);
		conf.set("mincorrelation", mincorrelation);
		conf.set("minmeasuresum", minmeasuresum);

		log.info("门限值：" + "samefreqcellcoefweight=" + samefreqcellcoefweight + ",switchratioweight="
				+ switchratioweight + ",cellm3rinterfercoef=" + cellm3rinterfercoef + ",cellm6rinterfercoef="
				+ cellm6rinterfercoef + ",cellm30rinterfercoef=" + cellm30rinterfercoef + ",beforenstrongcelltab="
				+ beforenstrongcelltab + ",topncelllist=" + topncelllist + ",increasetopncelllist="
				+ increasetopncelllist + ",convermethod1targetval=" + convermethod1targetval
				+ ",convermethod2targetval=" + convermethod2targetval + ",convermethod2scoren=" + convermethod2scoren
				+ ",mincorrelation=" + mincorrelation + ",minmeasuresum=" + minmeasuresum);

		// 更新PCI规划干扰计算状态
		structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "正在计算");

		// 实例化一个 MapReduce 的 Job
		Job pciJob = null;
		try {
			pciJob = Job.getInstance(conf, "PciCalc_Worker");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("创建hadoop集群用于pci规划干扰计算的job失败！");
			// 保存报告信息
			report.setStage("创建hadoop pci规划干扰计算job");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("创建hadoop pci规划干扰计算job失败");
			addJobReport(report);

			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		Path path = new Path(filePath + "/out");
		try {
			// 先在 HDFS 中删除已经存在的文件，因为 HDFS 文件系统是不能修改的。
			FileSystem fs = FileSystem.get(conf);
			fs.delete(path, true);
		} catch (IOException e1) {
			e1.printStackTrace();
			log.error("设置hadoop集群用于 pci规划干扰计算job的输出路径失败！");
			// 保存报告信息
			report.setStage("设置hadoop pci规划干扰计算job输出路径");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("设置hadoop pci规划干扰计算job输出路径失败");
			addJobReport(report);

			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		System.out.println("组装rowkey：cityId=" + cityId + ", 开始日期毫秒表示=" + startMeaMillis + ", 结束日期毫秒表示=" + endMeaMillis);

		// 在 Hadoop 中设置结果输出路径
		FileOutputFormat.setOutputPath(pciJob, path);

		String cellArr[] = null;
		if (optimizeCells != null && !"".equals(optimizeCells.trim())) {
			cellArr = optimizeCells.split(",");
			if (cellArr.length == 0) {
				log.error("变PCI小区字符串逗号分割后的长度为０,不满足基本需求！");
				// 保存报告信息
				report.setStage("变PCI小区字符串逗号分割后的长度为０,不满足基本需求！");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("失败");
				report.setAttMsg("变PCI小区字符串逗号分割后的长度为０,不满足基本需求！");
				addJobReport(report);

				structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				return status;
			}
		} else {
			log.error("变PCI小区字符串为NULL！");
			// 保存报告信息
			report.setStage("变PCI小区字符串为NULL！");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("变PCI小区字符串为NULL！");
			addJobReport(report);

			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		pciJob.setJarByClass(IscreateHadoopBaseJob.class);

		System.out.println("======>>>运行模式：" + conf.get("mapred.job.tracker"));

		pciJob.setMapperClass(PciMapper.class);
		pciJob.setReducerClass(PciReducer.class);
		pciJob.setOutputKeyClass(Text.class);
		pciJob.setOutputValueClass(Text.class);

		List<Scan> scans = new ArrayList<Scan>();

		// cityId+"_"+startMeaMillis+"_", cityId+"_"+endMeaMillis+"~"
		// HBase ERI MR 表数据
		Scan scanEriMr = new Scan();

		// 加入这两句速度快很多
		scanEriMr.setCacheBlocks(false);
		scanEriMr.setCaching(500);

		scanEriMr.setStartRow(Bytes.toBytes(cityId + "_" + startMeaMillis + "_#"));
		scanEriMr.setStopRow(Bytes.toBytes(cityId + "_" + endMeaMillis + "_~"));
		scanEriMr.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ERI_MR"));

		scans.add(scanEriMr);

		// HBase ZTE MR 表数据
		Scan scanZteMr = new Scan();

		// 加入这两句速度快很多
		scanZteMr.setCacheBlocks(false);
		scanZteMr.setCaching(500);

		scanZteMr.setStartRow(Bytes.toBytes(cityId + "_" + startMeaMillis + "_#"));
		scanZteMr.setStopRow(Bytes.toBytes(cityId + "_" + endMeaMillis + "_~"));
		scanZteMr.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ZTE_MR"));

		scans.add(scanZteMr);

		/* 暂不处理 HO 数据
		// HBase ERI HO 表数据
		Scan scanEriHo = new Scan();

		// 加入这两句速度快很多
		scanEriHo.setCacheBlocks(false);
		scanEriHo.setCaching(500);

		scanEriHo.setStartRow(Bytes.toBytes(cityId + "_" + startMeaMillis + "_#"));
		scanEriHo.setStopRow(Bytes.toBytes(cityId + "_" + endMeaMillis + "_~"));
		scanEriHo.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ERI_HO"));

		scans.add(scanEriHo);

		// HBase ZTE HO 表数据
		Scan scanZteHo = new Scan();

		// 加入这两句速度快很多
		scanZteHo.setCacheBlocks(false);
		scanZteHo.setCaching(500);

		scanZteHo.setStartRow(Bytes.toBytes(cityId + "_" + startMeaMillis + "_#"));
		scanZteHo.setStopRow(Bytes.toBytes(cityId + "_" + endMeaMillis + "_~"));
		scanZteHo.setAttribute(Scan.SCAN_ATTRIBUTES_TABLE_NAME, Bytes.toBytes("LTE4G_ZTE_HO"));

		scans.add(scanZteHo);
		*/

		try {
			// 向 Hadoop 提交 Map 任务
			TableMapReduceUtil.initTableMapperJob(scans, PciMapper.class, Text.class, Text.class, pciJob);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("初始化 pci规划干扰计算job的Mapper失败！");
			// 保存报告信息
			report.setStage("初始化 pci规划干扰计算job的Mapper");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("初始化 pci规划干扰计算job的Mapper失败");
			addJobReport(report);

			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		}

		System.out.println("注册集群用于 pci规划干扰计算的job...");
		String mrJobId = "";
		HttpSession session = SessionService.getSession();

		System.out.println("======>>>HttpSession 为: " + session);

		try {
			// 向 Hadoop 提交 Reduce 任务
			pciJob.submit();

			int progMonitorPollIntervalMillis = Job.getProgressPollInterval(conf);

			JobID pciJobId = pciJob.getJobID();
			mrJobId = pciJob.getJobID().toString();

			// 获取 reduce 的 jobid 存储入 lte_pci_job 表，以备后面获取停止该 job
			// 通过主进程 jobId 更新 lte_pci_job 表中 MR_JOB_ID（MapReduce 的 jobId）
			structureQueryDao.addMapReduceJobId(stmt, jobId, mrJobId);

			// 通过 mrjobID 存储job对象，
			log.debug("+++>>>当前 mrJobId 为:" + mrJobId + "-----当前 mrJob 为: " + pciJob + " 的信息!");

			session.setAttribute(mrJobId, pciJob);

			while (!pciJob.isComplete()) {
				log.info("+++>>>jobId=" + pciJobId + ",名称=" + job.getJobName() + "执行中...");
				log.info("+++>>>Map: " + StringUtils.formatPercent(pciJob.mapProgress(), 0) + " Reduce: "
						+ StringUtils.formatPercent(pciJob.reduceProgress(), 0));
				Thread.sleep(progMonitorPollIntervalMillis);
			}

			if (pciJob.isSuccessful()) {
				log.info("+++>>>集群 jobId = " + pciJobId + ", 名称 = " + job.getJobName() + ", 结果：任务成功！");
				log.info("PCI 规划干扰计算完成。");
				// 保存报告信息
				report.setStage("MapReduce计算结束，成功");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("成功");

				// 从 HDFS 中获取返回信息
				String strReturn = readReturnInfoFromHdfs(filePath, rdFileName);

				if (!strReturn.equals("fail")) {
					report.setAttMsg("集群 jobId = " + pciJobId + ", 名称 = " + job.getJobName() + ", 结果：任务成功！<br>返回信息：" + strReturn);
				} else {
					report.setAttMsg("集群 jobId = " + pciJobId + ", 名称 = " + job.getJobName() + ", 结果：任务成功！<br>读取返回信息失败！");
				}
				addJobReport(report);
				structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算完成");
			} else {
				log.info("+++>>>集群jobId=" + pciJobId + ",名称=" + job.getJobName() + ",结果：任务失败！");
				log.info("PCI 规划干扰计算失败");
				// 保存报告信息
				report.setStage("MapReduce计算结束，未成功完成");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("失败");
				report.setAttMsg("集群 jobId = " + pciJobId + ", 名称 = " + job.getJobName() + ", 结果：任务失败！");
				addJobReport(report);
				structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				return status;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			report.setStage("提交MapReduce任务");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("提交MapReduce任务失败，未找到类");
			addJobReport(report);
			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		} catch (IOException e) {
			e.printStackTrace();
			report.setStage("提交MapReduce任务");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("提交MapReduce任务失败，IO错误");
			addJobReport(report);
			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		} catch (InterruptedException e) {
			e.printStackTrace();
			report.setStage("提交MapReduce任务");
			report.setBegTime(startTime);
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("提交MapReduce任务失败，线程被中断");
			addJobReport(report);
			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
			return status;
		} finally {
			try {
				// 完成或失败后予以删除该对象
				session.removeAttribute(mrJobId);
			} catch (Exception e2) {
				e2.printStackTrace();
				report.setStage("删除 MRJOB ");
				report.setBegTime(startTime);
				report.setEndTime(new Date());
				report.setFinishState("失败");
				report.setAttMsg("通过集群 jobId = " + mrJobId + ", 删除 MRJOB 失败");
				addJobReport(report);
				structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, "计算失败");
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
				return status;
			}
		}
		// 在 Hadoop 集群中 完成 PCI 规划干扰计算，并在 HDFS 中保存结果文件。

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

	/**
	 * 
	 * @param resFilePath Pci计算返回信息文件的hdfs路径
	 * @param rdFileName Pci计算返回信息文件的名称
	 * @return
	 */
	private String readReturnInfoFromHdfs(String resFilePath, String rdFileName) {

		//Pci规划数据源文件的名称,源数据文件有两个，一个是current后缀，一个是backup后缀
		String currentFileName = rdFileName + ".info";

		File currentFile = FileTool.getFile(resFilePath + "/" + currentFileName);

		//两个源文件不存在，不能提供下载
		if (currentFile == null) {
			log.info("Pci规划计算的返回信息文件不存在！currentFileName=" + resFilePath + "/" + currentFileName);
			return "fail";
		}

		FileInputStream fsCur = null;
		DataInputStream disCur = null;
		String isFinished = "";
		String result = "";
		try {
			//当前数据文件存在，则选择current文件
			fsCur = new FileInputStream(currentFile);
			disCur = new DataInputStream(fsCur);
			result = disCur.readUTF();
			isFinished = disCur.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (disCur != null) {
					disCur.close();
				}
				if (fsCur != null) {
					fsCur.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				log.info("获取Pci规划结果源文件中，读取数据出错");
				result = "error";
			}
			log.info("获取Pci规划结果源文件中，读取数据出错");
			result = "error";
		}
		if (result.equals("error")) {
			return "fail";
		}
		if (!isFinished.equals("finished")) {
			return "fail";
		}
		return result;
	}

	@Override
	public void releaseRes() {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if (stmt != null && structureQueryDao != null) {
			// 更新 PCI 规划干扰计算表的进度
			structureQueryDao.updatePciPlanWorkStatusByStmt(stmt, jobId, jobStatus.getJobState().getCode());
		}
	}

	/**
	 * 把基站小区映射转换成以分隔符连接的字符串，用于MR计算
	 * 
	 * @param enodebToCells
	 *            基站小区映射 Map
	 * @return 字符串
	 */
	private String map2String(Map<String, String> enodebToCells) {
		String str = "";
		for (String key : enodebToCells.keySet()) {
			str += key + "=" + enodebToCells.get(key) + "|";
		}
		return str.substring(0, str.length() - 1);
	}
}
