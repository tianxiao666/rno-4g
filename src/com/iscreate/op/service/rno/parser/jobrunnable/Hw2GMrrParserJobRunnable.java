package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.dao.rno.RnoStructAnaV2;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.MrrParserContext;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class Hw2GMrrParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(Hw2GMrrParserJobRunnable.class);

	private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();

	public Hw2GMrrParserJobRunnable() {
		super();
		super.setJobType("2GHWMRRFILE");
	}

	// 构建insert语句
	private static String tempTable = "";
	private static String insertInoTempTableSql = "";
	
	private static String frateTempTable="RNO_2G_HW_MRR_FRATE_T";
	private static String hrateTempTable="RNO_2G_HW_MRR_HRATE_T";
	//private static String cellTempTable="RNO_CELL_CITY_T";
	// 语句
	// 2G_HW_MRR_DESC sql
	//private static String insert2GHwMrrDescDataSql = "insert into RNO_2G_HW_MRR_DESC (MRR_DESC_ID,MEA_DATE,BSC,CELL_NUM,USER_ACCOUNT,CYCLE,CITY_ID) values (?,?,?,?,?,?,?)";
	// 2G_HW_MRR_FRATE sql
	//private static String insert2GHwMrrFRateTempDataSql = "insert into RNO_2G_HW_MRR_FRATE_T (MEA_DATE,BSC,CELL,CELL_IDX,LAC,CI,TRX_IDX,INTEGRITY,S4100A,S4101A,S4102A,S4103A,S4104A,S4105A,S4106A,S4107A,S4110A,S4111A,S4112A,S4113A,S4114A,S4115A,S4116A,S4117A,S4120A,S4121A,S4122A,S4123A,S4124A,S4125A,S4126A,S4127A,S4130A,S4131A,S4132A,S4133A,S4134A,S4135A,S4136A,S4137A,S4140A,S4141A,S4142A,S4143A,S4144A,S4145A,S4146A,S4147A,S4150A,S4151A,S4152A,S4153A,S4154A,S4155A,S4156A,S4157A,S4160A,S4161A,S4162A,S4163A,S4164A,S4165A,S4166A,S4167A,S4170A,S4171A,S4172A,S4173A,S4174A,S4175A,S4176A,S4177A,S4100B,S4101B,S4102B,S4103B,S4104B,S4105B,S4106B,S4107B,S4110B,S4111B,S4112B,S4113B,S4114B,S4115B,S4116B,S4117B,S4120B,S4121B,S4122B,S4123B,S4124B,S4125B,S4126B,S4127B,S4130B,S4131B,S4132B,S4133B,S4134B,S4135B,S4136B,S4137B,S4140B,S4141B,S4142B,S4143B,S4144B,S4145B,S4146B,S4147B,S4150B,S4151B,S4152B,S4153B,S4154B,S4155B,S4156B,S4157B,S4160B,S4161B,S4162B,S4163B,S4164B,S4165B,S4166B,S4167B,S4170B,S4171B,S4172B,S4173B,S4174B,S4175B,S4176B,S4177B,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 2G_HW_MRR_HRATE sql
	//private static String insert2GHwMrrHRateTempDataSql = "insert into RNO_2G_HW_MRR_HRATE_T (MEA_DATE,BSC,CELL,CELL_IDX,LAC,CI,TRX_IDX,INTEGRITY,S4100C,S4101C,S4102C,S4103C,S4104C,S4105C,S4106C,S4107C,S4110C,S4111C,S4112C,S4113C,S4114C,S4115C,S4116C,S4117C,S4120C,S4121C,S4122C,S4123C,S4124C,S4125C,S4126C,S4127C,S4130C,S4131C,S4132C,S4133C,S4134C,S4135C,S4136C,S4137C,S4140C,S4141C,S4142C,S4143C,S4144C,S4145C,S4146C,S4147C,S4150C,S4151C,S4152C,S4153C,S4154C,S4155C,S4156C,S4157C,S4160C,S4161C,S4162C,S4163C,S4164C,S4165C,S4166C,S4167C,S4170C,S4171C,S4172C,S4173C,S4174C,S4175C,S4176C,S4177C,S4100D,S4101D,S4102D,S4103D,S4104D,S4105D,S4106D,S4107D,S4110D,S4111D,S4112D,S4113D,S4114D,S4115D,S4116D,S4117D,S4120D,S4121D,S4122D,S4123D,S4124D,S4125D,S4126D,S4127D,S4130D,S4131D,S4132D,S4133D,S4134D,S4135D,S4136D,S4137D,S4140D,S4141D,S4142D,S4143D,S4144D,S4145D,S4146D,S4147D,S4150D,S4151D,S4152D,S4153D,S4154D,S4155D,S4156D,S4157D,S4160D,S4161D,S4162D,S4163D,S4164D,S4165D,S4166D,S4167D,S4170D,S4171D,S4172D,S4173D,S4174D,S4175D,S4176D,S4177D,CITY_ID) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	// 数据库临时表每个字段可能对应的标题起始名称
	private Map<String, DBFieldToTitle> fRateDbFieldsToTitles = new TreeMap<String, DBFieldToTitle>();
	private Map<String, DBFieldToTitle> hRateDbFieldsToTitles = new TreeMap<String, DBFieldToTitle>();
	private Map<String, DBFieldToTitle> dbFieldsToTitles= new TreeMap<String, DBFieldToTitle>();
	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {

		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

		// 准备资源
//		Statement stmt = null;
//		try {
//			stmt = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//
//			// 失败了
//			status.setJobState(JobState.Failed);
//			status.setUpdateTime(new Date());
//
//			// 报告
//			report.setFinishState("失败");
//			report.setStage("准备数据库连接");
//			report.setBegTime(new Date());
//			report.setReportType(2);
//			addJobReport(report);
//			
//			return status;
//		}

		// 获取job相关的信息
		String sql = "select * from RNO_DATA_COLLECT_REC where JOB_ID=" + jobId;
		List<Map<String, Object>> recs = RnoHelper.commonQuery(stmt, sql);
		RnoDataCollectRec dataRec = null;
		if (recs != null && recs.size() > 0) {
			dataRec = RnoHelper.commonInjection(RnoDataCollectRec.class,
					recs.get(0),new DateUtil());
		}
		// log.debug("jobId=" + jobId + ",对应的dataRec=" + dataRec);
		if (dataRec == null) {
			log.error("转换RnoDataCollectRec失败！");
			// 失败了
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());

			// 报告
			report.setFinishState("失败");
			report.setStage("获取上传文件");
			report.setBegTime(new Date());
			report.setReportType(1);
			report.setAttMsg("未找到上传文件！");
			addJobReport(report);

			return status;
		}

		// 准备
		long cityId = dataRec.getCityId();
		String fileName = dataRec.getFileName();
		filePath = FileInterpreter.makeFullPath(dataRec.getFullPath());
//		file = new File(filePath);
		file=FileTool.getFile(filePath);
		long dataId = dataRec.getDataCollectId();

		String msg = "";

		// 开始解析
		List<File> allMrrFiles = new ArrayList<File>();// 将所有待处理的ncs文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2;
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的ncs文件是一个压缩包。");

			// 进行解压
			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "") + "/";

			//
			boolean unzipOk = false;
			try {
//				File file1 = new File(FileInterpreter.makeFullPath(file
//						.getAbsolutePath()));
				// log.debug("file.getAbsolutePath()="
				// + FileInterpreter.makeFullPath(file.getAbsolutePath())
				// + ",存在？" + file1.exists());

				/*unzipOk = FileTool.unZip(
						FileInterpreter.makeFullPath(file.getAbsolutePath()),
						destDir);*/
				unzipOk = ZipFileHandler.unZip(
						FileInterpreter.makeFullPath(file.getAbsolutePath()),
						destDir);
			} catch (Exception e) {
				msg = "压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
				e.printStackTrace();
				// super.setCachedInfo(token, msg);
				// clearResource(destDir, null);

				// job报告
				date2 = new Date();
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setStage(DataParseProgress.Decompress.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setAttMsg("压缩包解析失败！请确认压缩包文件是否被破坏！");
				addJobReport(report);

				// 数据记录本身的状态
				sql = "update rno_data_collect_rec set FILE_STATUS='"
						+ DataParseStatus.Failall.toString()
						+ "' where DATA_COLLECT_ID="
						+ dataRec.getDataCollectId();
				try {
					stmt.executeUpdate(sql);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				// job status
				status.setJobState(JobState.Failed);
				status.setUpdateTime(date2);
				return status;

			}
			if (!unzipOk) {
				msg = "解压失败 ！仅支持zip格式的压缩包！ ";
				log.error(msg);
				// super.setCachedInfo(token, msg);
				// clearResource(destDir, null);

				// job报告
				date2 = new Date();
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setStage(DataParseProgress.Decompress.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setAttMsg("解压失败 ！仅支持zip格式的压缩包！");
				addJobReport(report);

				// 数据记录本身的状态
				sql = "update rno_data_collect_rec set FILE_STATUS='"
						+ DataParseStatus.Failall.toString()
						+ "' where DATA_COLLECT_ID="
						+ dataRec.getDataCollectId();
				try {
					stmt.executeUpdate(sql);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				// job status
				status.setJobState(JobState.Failed);
				status.setUpdateTime(date2);
				return status;

			}
			file = new File(destDir);
			File[] files = file.listFiles();
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allMrrFiles.add(f);
				}
			}

			// ---报告----//
			date2 = new Date();
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setFinishState(DataParseStatus.Succeded.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setAttMsg("解压文件：" + fileName + ",大小："
					+ RnoHelper.getPropSizeExpression(dataRec.getFileSize()));
			addJobReport(report);
		} else if (fileName.endsWith(".rar")) {
			msg = "请用zip格式压缩文件！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);

			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("解压失败 ！请用zip格式压缩文件！");
			addJobReport(report);

			// 数据记录本身的状态
			sql = "update rno_data_collect_rec set FILE_STATUS='"
					+ DataParseStatus.Failall.toString()
					+ "' where DATA_COLLECT_ID=" + dataRec.getDataCollectId();
			try {
				stmt.executeUpdate(sql);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// job status
			status.setJobState(JobState.Failed);
			status.setUpdateTime(date2);
			return status;

		} else {
			log.info("上传的mrr是一个普通文件。");
			allMrrFiles.add(file);
		}

		if (allMrrFiles.isEmpty()) {
			msg = "未上传有效的mrr文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的mrr文件！注意zip包里不能再包含有文件夹！");
			addJobReport(report);

			// 数据记录本身的状态
			sql = "update rno_data_collect_rec set FILE_STATUS='"
					+ DataParseStatus.Failall.toString()
					+ "' where DATA_COLLECT_ID=" + dataRec.getDataCollectId();
			try {
				stmt.executeUpdate(sql);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// job status
			status.setJobState(JobState.Failed);
			status.setUpdateTime(date2);
			return status;
		}

		// 正在解析
		sql = "update rno_data_collect_rec set FILE_STATUS='"
				+ DataParseStatus.Parsing.toString()
				+ "' where DATA_COLLECT_ID=" + dataRec.getDataCollectId();
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		// 读取数据库与文本文件格式的配置
//		dbFieldsToTitles = readDbToTitleCfgFromProfile();
		/*fRateDbFieldsToTitles =readDbToTitleCfgFromProfile("hw2GMrrFRateDbToTitles.properties");
		hRateDbFieldsToTitles =readDbToTitleCfgFromProfile("hw2GMrrHRateDbToTitles.properties");*/
		fRateDbFieldsToTitles =readDbToTitleCfgFromXml("hw2GMrrFRateDbToTitles.xml");
		hRateDbFieldsToTitles =readDbToTitleCfgFromXml("hw2GMrrHRateDbToTitles.xml");
		String tmpFileName = fileName;
		int parseSucCnt = 0;
		int dataHandleSucCnt=0;
		boolean parseOk = false;
		//准备处理华为mrr分析所需要的城市数据信息
//		ResultInfo result = rnoStructAnaV2.prepareEriCityCellData(stmt,
//				 cellTempTable, cityId);
//		if (!result.isFlag()) {
//			log.error("准备分析用的小区数据时，出错！");
//			// clearResource(destDir, context);
//			return failWithStatus("准备分析用的小区数据时，出错时出错", new Date(), jobWorker,
//					stmt, report, status, dataRec.getDataCollectId(),
//					DataParseStatus.Failall, DataParseProgress.Prepare);
//		}
		int totalFileCnt = allMrrFiles.size();
		int i = 0;
		
		Map<String, Map<String, DBFieldToTitle>> rateMap=new HashMap<String, Map<String,DBFieldToTitle>>();
		rateMap.put("fRate", fRateDbFieldsToTitles);
		rateMap.put("hRate", hRateDbFieldsToTitles);
		Rate rate=new Rate();
		rate.setHasFrate(false);
		rate.setHasHrate(false);
		DateUtil dateUtil=new DateUtil();
		
		/********************* HBase start *********************/
		MrrParserContext context = new MrrParserContext();
		//获取爱立信MRR的HBase描述表、数据表和索引表
		try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			context.addHtable("idxTable","LTE4G_CELL_MEATIME",conf);
			context.addHtable("hrateTable","RNO_2G_HW_MRR_HRATE",conf);
			context.addHtable("frateTable","RNO_2G_HW_MRR_FRATE",conf);
		} catch (IOException e1) {
			e1.printStackTrace();
			msg = "获取HTable出错！code=mrr-11";
			log.error(msg);
			context.closeHTables();
			return failWithStatus("获取HBase表时出错", new Date(),
					this, stmt, report, status,
					dataRec.getDataCollectId(), DataParseStatus.Failall,
					DataParseProgress.Prepare);
		}
		//建立每个表对应的put队列
		context.addPuts("idxPuts");
		context.addPuts("fratePuts");
		context.addPuts("hratePuts");
		
		//设置cityId
		context.setCityId(cityId);
		/********************* HBase end *********************/
		
		for (File f : allMrrFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				
				//2014-12-9 peng.jm加入 设置mrr文件属性为未验证区域
				context.setValidateCity(false);
				
				//开始解析mrr文件
				parseOk = parseHwMrr(this, report, tmpFileName, f,
						connection, stmt, cityId,rateMap,rate,dateUtil,context);
				/***** HBase start *****/
				if(parseOk) {
					context.clearPuts();
					context.tableFlushCommits();
				}
				/***** HBase end *****/
				i++;
				date2 = new Date();
				report.setStage("文件处理总结");
				report.setReportType(1);
				if (parseOk) {
					report.setFinishState(DataParseStatus.Succeded.toString());
				} else {
					report.setFinishState(DataParseStatus.Failall.toString());
				}
				report.setBegTime(date1);
				report.setEndTime(date2);
				if (parseOk) {
					report.setAttMsg("成功完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
					parseSucCnt++;
				} else {
					report.setAttMsg("失败完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
				}
				addJobReport(report);
			} catch (Exception e) {
				e.printStackTrace();
				date2 = new Date();
				msg = tmpFileName + "文件解析出错！";
				report.setStage("文件处理总结");
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setReportType(1);
				report.setAttMsg("文件解析出错（" + i + "/" + totalFileCnt + "）:"
						+ tmpFileName);
				addJobReport(report);
				log.error(msg);
			}
		}
		//数据处理 转移
		try {
			dataProcess(connection,fileName,dataRec,rate);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("文件:[" + file.getName() + "]的数据信息有误！");
		}
		
		if (parseSucCnt > 0) {
//			status.setJobRunningStatus(JobRunningStatus.Succeded);
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
		} else {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}

		if (parseSucCnt == allMrrFiles.size()) {
			// 全部成功
			sql = "update rno_data_collect_rec set file_status='"
					+ DataParseStatus.Succeded.toString()
					+ "' where data_collect_id=" + dataRec.getDataCollectId();
		} else {
			if (parseSucCnt > 0) {
				sql = "update rno_data_collect_rec set file_status='"
						+ DataParseStatus.Failpartly.toString()
						+ "' where data_collect_id="
						+ dataRec.getDataCollectId();
			} else {
				sql = "update rno_data_collect_rec set file_status='"
						+ DataParseStatus.Failall.toString()
						+ "' where data_collect_id="
						+ dataRec.getDataCollectId();
			}
		}

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// truncate table:RNO_CELL_CITY_T
		// sql = "truncate table RNO_CELL_CITY_T";
		// try {
		// stmt.executeUpdate(sql);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		try {
			stmt.close();
			
			/****** HBase start *******/
			context.closeHTables() ;
			/****** HBase end *******/
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	private boolean parseHwMrr(JobRunnable jobWorker, JobReport report,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId, Map<String, Map<String, DBFieldToTitle>> rateMap,Rate rate,DateUtil dateUtil,MrrParserContext context) {

		Map<String, DBFieldToTitle> fRate=null;
		Map<String, DBFieldToTitle> hRate=null;
		if (rateMap!=null) {
			fRate=rateMap.get("fRate");
			hRate=rateMap.get("hRate");
		}
		String spName = "everyStartHw" + System.currentTimeMillis();
		Savepoint savepoint = null;
		try {
			savepoint = connection.setSavepoint(spName);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement insertTStmt = null;
		long st = System.currentTimeMillis();

		Date date1 = new Date();
		Date date2 = null;
		// 读取文件
		BufferedReader reader = null;
		String charset = null;
		charset = FileTool.getFileEncode(f.getAbsolutePath());
		log.debug(tmpFileName + " 文件编码：" + charset);
		if (charset == null) {
			log.error("文件：" + tmpFileName + ":无法识别的文件编码！");
			date2 = new Date();
			report.setSystemFields("检查文件编码", date1, date2,
					DataParseStatus.Failall.toString(), "文件：" + tmpFileName
							+ ":无法识别的文件编码！");
			addJobReport(report);
			try {
				connection.releaseSavepoint(savepoint);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), charset));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String msg = "";
		String line = "";
		date1 = new Date();
		date2 = null;
		//以下为了过滤标题头前无用信息
		try {
			String[] sps = new String[1];
			String lineHead="";
			do {
				line = reader.readLine();
				if(line==null){
					sps=new String[]{};
					break;
				}
				lineHead=line;
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			// 读取到标题头长度
			int fieldCnt = sps.length;
			// log.debug("华为ncs文件：" + tmpFileName + ",title 为：" + line
			// + ",有" + fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> pois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			for (String sp : sps) {
				// log.debug("sp==" + sp);
				index++;
				find = false;
				//判断行头是否包含特定的字段来辨别全速率或者半速率
				if(lineHead.contains("S4100C")){
					dbFieldsToTitles=hRate;
					tempTable=hrateTempTable;
					rate.setHasHrate(true);
				}else if(lineHead.contains("S4100A")){
					dbFieldsToTitles=fRate;
					tempTable=frateTempTable;
					rate.setHasFrate(true);
				}else{
					log.error("标题头不符合全速率或半速率的要求，请核查");
					date2 = new Date();
					report.setFields("标题头不符合全速率或半速率的要求，请核查", date1, date2,
							DataParseStatus.Failall.toString(), msg);
					addJobReport(report);
					connection.releaseSavepoint(savepoint);
					return false;
				}
				for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
					// log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						if (dt.matchType == 1) {
							// find;
							// log.debug("--match type=1.dtf=" + dtf
							// + ",sp=" + sp);
							if (StringUtils.equals(dtf, sp)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
								find = true;
								dt.setIndex(index);
								pois.put(index, dt.dbField);// 快速记录
								break;
							}
						} else if (dt.matchType == 0) {
							// log.debug("--match type=0.dtf=" + dtf
							// + ",sp=" + sp);
							if (StringUtils.startsWith(sp, dtf)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
								find = true;
								dt.setIndex(index);
								pois.put(index, dt.dbField);// 快速记录
								break;
							}
						}
					}

					if (find) {
						break;
					}
				}
			}

			// 判断标题头合法性，及各数据库字段对应的位置
			for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
				if (dt.isMandatory && dt.index < 0) {
					//说明该索引值未重新设置，不存在
					msg += "[" + dt.dbField + "在文件中未找到对应的数据]\r\n";
				}
			}
			if (!StringUtils.isBlank(msg)) {
				log.error("检查华为ncs文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), msg);
				addJobReport(report);
				connection.releaseSavepoint(savepoint);
				return false;
			}
			// 拼装sql
			insertInoTempTableSql = "insert into " + tempTable + " (";
			String ws = "";
			index = 1;
			for (String d : dbFieldsToTitles.keySet()) {
				if (dbFieldsToTitles.get(d).index >= 0) {
					// 只对出现了的进行组sql
					dbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
					insertInoTempTableSql += d + ",";
					ws += "?,";
				}
			}
			log.debug("insertInoTempTableSql:"+insertInoTempTableSql);
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			insertInoTempTableSql += "CITY_ID";
			// insertInoTempTableSql = insertInoTempTableSql.substring(0,
			// insertInoTempTableSql.length() - 1);
			// ws = ws.substring(0, ws.length() - 1);
			ws += cityId;
			insertInoTempTableSql += ") values ( " + ws + " )";
			log.debug("华为ncs插入临时表的sql=" + insertInoTempTableSql);

			try {
				insertTStmt = connection
						.prepareStatement(insertInoTempTableSql);
			} catch (Exception e) {
				msg = "准备华为的ncs插入的prepareStatement失败";
				log.error("准备华为的ncs插入的prepareStatement失败！sql="
						+ insertInoTempTableSql);
				e.printStackTrace();
				connection.releaseSavepoint(savepoint);
				return false;
			}
			
			log.info("华为mrr文件数据读取开始");
			long sTime = System.currentTimeMillis();
			// 逐行读取数据(真正读取数据)
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			do {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				sps = line.split(",|\t");
				if (sps.length != fieldCnt) {
					continue;
				}
				totalDataNum++;
				
				/***** HBase start *****/
				//2014-12-6 peng.jm加入 判断该行是全速率还是半速率
				String rateType = "";
				if(lineHead.contains("S4100C")){
					rateType = "hrate";
				}else if(lineHead.contains("S4100A")){
					rateType = "frate";
				}
				context.setRateType(rateType);
				/***** HBase end *****/
				
				//需要改造
				handleLineOk = handleHwMrrLine(sps, fieldCnt, pois,
						dbFieldsToTitles, insertTStmt,dateUtil,context);

				if (handleLineOk == true) {
					executeCnt++;
				}
				if (executeCnt > 5000) {
					log.info("读取5000行数据耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					// 每5000行执行一次
					try {
						insertTStmt.executeBatch();
						insertTStmt.clearBatch();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					log.info("存入oracle耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					
					/******** HBase start **********/
					//2014-12-8 peng.jm加入
					//判断这5000条数据是否符合城市要求
					if(!context.isValidateCity()) {
						boolean flag = rnoStructAnaV2.checkHwMrrArea(connection,context);
						if(!flag) {
							log.error(f.getName()+"文件所属区域不是选择的区域！");
							//文件区域已验证
							context.setValidateCity(true);
							//清除put队列
							context.clearPuts();
							connection.rollback(savepoint);
							return false;
						}
						//文件区域已验证
						context.setValidateCity(true);
					}

					//存入htable
					//半速率
					if(context.getHtable("hrateTable") != null 
							&& context.getPuts("hratePuts") != null 
							&& context.getPuts("hratePuts").size() > 0) {
						log.info("半速率Puts.size()="+context.getPuts("hratePuts").size());
						context.getHtable("hrateTable").put(context.getPuts("hratePuts"));
					} else {
						log.info("这5000条数据中半速率数据为空，或者没找到对应context对象中的HTable！");
					}
					log.info("存入Hbase的半速率表耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					//全速率
					if(context.getHtable("frateTable") != null 
							&& context.getPuts("fratePuts") != null 
							&& context.getPuts("fratePuts").size() > 0) {
						log.info("全速率Puts.size()="+context.getPuts("fratePuts").size());
						context.getHtable("frateTable").put(context.getPuts("fratePuts"));
					} else {
						log.info("这5000条数据中全速率数据为空，或者没找到对应context对象中的HTable！");
					}
					log.info("存入Hbase的全速率表耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					//索引
					if(context.getHtable("idxTable") != null 
							&& context.getPuts("idxPuts") != null
							&& context.getPuts("idxPuts").size() > 0) {
						log.info("索引Puts.size()="+context.getPuts("idxPuts").size());
						context.getHtable("idxTable").put(context.getPuts("idxPuts"));
					} else {
						log.info("这5000条数据中索引数据为空，或者没找到对应context对象中的HTable！");
					}
					log.info("存入Hbase的索引表耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();	
					
					//清除put队列
					context.clearPuts();
					context.tableFlushCommits();
					/******** HBase end **********/
					
					executeCnt = 0;
				}
			} while (!StringUtils.isBlank(line));

			// 执行
			if (executeCnt > 0) {
				insertTStmt.executeBatch();
				
				/******** HBase start **********/
				//将剩余不足5000条的数据存入htable
				if(context.getHtable("hrateTable") != null 
						&& context.getPuts("hratePuts") != null ) {
					context.getHtable("hrateTable").put(context.getPuts("hratePuts"));
				} else {
					log.info("没找到对应的context对象中的HTable！");
				}
				if(context.getHtable("frateTable") != null 
						&& context.getPuts("fratePuts") != null ) {
					context.getHtable("frateTable").put(context.getPuts("fratePuts"));
				} else {
					log.info("没找到对应的context对象中的HTable！");
				}
				if(context.getHtable("idxTable") != null 
						&& context.getPuts("idxPuts") != null ) {
					context.getHtable("idxTable").put(context.getPuts("idxPuts"));
				} else {
					log.info("没找到对应的context对象中的HTable！");
				}
				
				//清除put队列
				context.clearPuts();
				context.tableFlushCommits();
				/******** HBase end **********/
			}
			/*if(rate.isHasFrate()){
				printTmpTable("RNO_2G_HW_MRR_FRATE_T", connection.createStatement());
			}else{
				printTmpTable("RNO_2G_HW_MRR_HRATE_T", connection.createStatement());
			}*/
			
			log.debug("华为mrr数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据");
			/*// 一个文件一个提交
			connection.commit();*/
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback(savepoint);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (insertTStmt != null) {
				try {
					insertTStmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return true;
	}

	/**
	 * 处理每一行数据
	 * 
	 * @param sps
	 * @param expectFieldCnt
	 * @param dbFtts
	 * @param insertTempStatement
	 * @return
	 * @author chao.xj 2014-9-1 下午3:26:34
	 */
	private boolean handleHwMrrLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			PreparedStatement insertTempStatement,DateUtil dateUtil,MrrParserContext context) {

		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		
		/***** HBase start *****/
		//先获取小区，日期，载频索引；用于rowkey
		String cell = "";
		String meaDate = "";
		String trxIdx = "";
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			if(("MEA_DATE").equals(dbField)) {
				meaDate = sps[i];
			}
			if(("CELL").equals(dbField)) {
				cell = sps[i];
			}
			if(("TRX_IDX").equals(dbField)) {
				trxIdx = sps[i];
			}
		}

		Date d = dateUtil.parseDateArbitrary(meaDate);
		meaDate = dateUtil.format_yyyyMMdd(d);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		//int day = cal.get(Calendar.DAY_OF_MONTH);
		long meaMillis = d.getTime();
		
		String rowkey = context.getCityId()+"_"+meaMillis+"_"+cell+"_"+trxIdx;
		String idxRowkey = context.getCityId()+"_MRR_"+year+"_"+cell;
		
		Put put = new Put(rowkey.getBytes());
		Put idxPut = new Put(idxRowkey.getBytes());
		/***** HBase end *****/
		
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}
			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dt != null) {
				setIntoPreStmt(insertTempStatement, dt, sps[i],dateUtil);
			}
			
			//System.out.println("dbField="+dbField+",sps[i]="+sps[i]+",dt.field="+dt.getDbField());
			/***** HBase start *****/
			// 转换到puts队列
			if(("hrate").equals(context.getRateType())) {
				
				//转换日期格式
				if(("MEA_DATE").equals(dbField)) {
					put.add(Bytes.toBytes("val"), Bytes.toBytes(dbField), Bytes
							.toBytes(meaDate));
				} else {
					put.add(Bytes.toBytes("val"), Bytes.toBytes(dbField), Bytes
							.toBytes(sps[i]));
				}
				//索引				
				idxPut.add(Bytes.toBytes(month + ""), Bytes.toBytes(meaMillis + ""),
						Bytes.toBytes(""));
			}
			if(("frate").equals(context.getRateType())) {
				//转换日期格式
				if(("MEA_DATE").equals(dbField)) {
					put.add(Bytes.toBytes("val"), Bytes.toBytes(dbField), Bytes
							.toBytes(meaDate));
				} else {
					put.add(Bytes.toBytes("val"), Bytes.toBytes(dbField), Bytes
							.toBytes(sps[i]));
				}
				//索引
				idxPut.add(Bytes.toBytes(month + ""), Bytes.toBytes(meaMillis + ""),
						Bytes.toBytes(""));
			}
			/***** HBase end *****/
		}
		
		/***** HBase start *****/
		if(("hrate").equals(context.getRateType())) {
			if(context.getPuts("hratePuts") != null) {
				context.getPuts("hratePuts").add(put);
			} else {
				log.error("hratePuts队列为null！不能加入put对象");
			}
		}
		if(("frate").equals(context.getRateType())) {
			if(context.getPuts("fratePuts") != null) {
				context.getPuts("fratePuts").add(put);
			} else {
				log.error("fratePuts队列为null！不能加入put对象");
			}	
		}
		if(context.getPuts("idxPuts") != null) {
			context.getPuts("idxPuts").add(idxPut);
		} else {
			log.error("idxPuts队列为null！不能加入put对象");
		}
		/***** HBase end *****/
		
		try {
			insertTempStatement.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private void setIntoPreStmt(PreparedStatement pstmt, DBFieldToTitle dt,
			String val,DateUtil dateUtil) {
		String type = dt.getDbType();
		int index = dt.sqlIndex;
		if (index < 0) {
			log.error(dt + "在sql插入语句中，没有相应的位置！");
			return;
		}
		if (StringUtils.equalsIgnoreCase("Long", type)) {
			if (!StringUtils.isBlank(val)&&StringUtils.isNumeric(val)) {
				try {
					pstmt.setLong(index, Long.parseLong(val));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					pstmt.setNull(index, java.sql.Types.BIGINT);
				} catch (SQLException e) {
				}
			}
		} else if (StringUtils.equalsIgnoreCase("Date", type)) {
			if (!StringUtils.isBlank(val)) {
				try {
//					SimpleDateFormat sdf5 = new SimpleDateFormat(
//							"MM/dd/yyyy");
//						log.debug("date val:"+new java.sql.Timestamp(sdf5.parse(val).getTime()));
					    Date date=dateUtil.parseDateArbitrary(val);
					    if(date!=null){
					    	pstmt.setTimestamp(index, new java.sql.Timestamp(date.getTime()));
					    }else{
					    	pstmt.setNull(index, java.sql.Types.DATE);
					    }
					/*pstmt.setTimestamp(index, new java.sql.Timestamp(RnoHelper
							.parseDateArbitrary(val).getTime()));*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					pstmt.setNull(index, java.sql.Types.DATE);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (StringUtils.equalsIgnoreCase("String", type)) {
			if (!StringUtils.isBlank(val)) {
				try {
					pstmt.setString(index, val);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				try {
					pstmt.setNull(index, Types.VARCHAR);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else if (StringUtils.equalsIgnoreCase("Float", type)) {
			if (!StringUtils.isBlank(val)) {
				Float f = null;
				try {
					f = Float.parseFloat(val);
				} catch (Exception e) {

				}
				if (f != null) {
					try {
						pstmt.setFloat(index, f);
						return;
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				pstmt.setNull(index, Types.FLOAT);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (StringUtils.startsWithIgnoreCase("int", type)) {
			if (!StringUtils.isBlank(val)&&StringUtils.isNumeric(val)) {
				try {
					pstmt.setInt(index, Integer.parseInt(val));
				} catch (NumberFormatException e) {
				} catch (SQLException e) {
				}
			} else {
				try {
					pstmt.setNull(index, Types.INTEGER);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private JobStatus failWithStatus(String msg, Date date1,
			JobRunnable jobWorker, Statement stmt, JobReport report,
			JobStatus status, long dataId, DataParseStatus parseStatus,
			DataParseProgress progress) {
		// job报告
		Date date2 = new Date();
		report.setFinishState(parseStatus.toString());
		report.setStage(progress.toString());
		report.setBegTime(date1);
		report.setEndTime(date2);
		report.setAttMsg(msg);
		addJobReport(report);

		// 数据记录本身的状态
		String sql = "update rno_data_collect_rec set FILE_STATUS='"
				+ DataParseStatus.Failall.toString()
				+ "' where DATA_COLLECT_ID=" + dataId;
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		// job status
		status.setJobState(JobState.Failed);
		status.setUpdateTime(date2);
		return status;
	}

	private static Map<String, DBFieldToTitle> readDbToTitleCfgFromProfile(String proFile) {
		InputStream is = null;
		Properties prop = null;
		try {
			// 需要实时读取
			is = new BufferedInputStream(new FileInputStream(
					Hw2GMrrParserJobRunnable.class.getResource(proFile).getPath()));
			prop = new Properties();
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		Enumeration<String> names = (Enumeration<String>) prop.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			log.debug(name + "=" + prop.getProperty(name));
			if (StringUtils.isBlank(name) || name.indexOf(",") < 0) {
				continue;
			}
			String vs = prop.getProperty(name);
			if (StringUtils.isBlank(vs)) {
				log.error("跳过 vs==" + vs);
				continue;
			}
			String[] parts1 = name.split(",");
			if (parts1.length != 3) {
				System.out.println("---wrong:" + name);
				continue;
			}
			DBFieldToTitle dt = new DBFieldToTitle();

			if (StringUtils.equals(parts1[0], "1")) {
				// mandaroty
				dt.setMandatory(true);
			} else {
				dt.setMandatory(false);
			}
			// 字段类型
			dt.setDbType(parts1[1]);
			// 字段名称与匹配要求
			if (parts1[2].indexOf("-") > 0) {
				// log.debug("有||,parts1[2]=" + parts1[2]);
				String[] parts2 = parts1[2].split("-");
				// log.debug("parts2=" + parts2.length);
				if (parts2.length != 2) {
					continue;
				}
				dt.setDbField(parts2[0]);
				if (StringUtils.equals(parts2[1], "1")) {
					dt.setMatchType(1);
				} else {
					dt.setMatchType(0);
				}
			} else {
				// log.debug("没有||");
				dt.setDbField(parts1[2]);
				dt.setMatchType(0);
			}

			String[] v = vs.split(",");
			for (String vo : v) {
				dt.addTitle(vo);
			}
			dbCfgs.put(dt.dbField, dt);
			log.debug(dt);
		}
		return dbCfgs;
	}
	/**
	 * 
	 * @title 从xml配置 文件中读取hw mrr 数据库到excel的映射关系
	 * @param proFile
	 * @return
	 * @author chao.xj
	 * @date 2014-9-26下午7:26:20
	 * @company 怡创科技
	 * @version 1.2
	 */
	private static Map<String, DBFieldToTitle> readDbToTitleCfgFromXml(String proFile) {
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in =new FileInputStream(new File(Hw2GMrrParserJobRunnable.class.getResource(
					proFile).getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
			  Element e = (Element) o;
			  DBFieldToTitle dt = new DBFieldToTitle();
			   for (Object obj : e.elements()) {
				   Element e1 = (Element) obj;
				   String key=e1.getName();
				   String val=e1.getTextTrim();
				    if("name".equals(key)){
					   dt.setDbField(val);
				    }
				    if("type".equals(key)){
					   dt.setDbType(val);		   
				    }
					if("essential".equals(key)){
						if (StringUtils.equals(val, "1")) {
							// mandaroty
							dt.setMandatory(true);
						} else {
							dt.setMandatory(false);
						}  
					}
					if("match".equals(key)){
						dt.setMatchType(Integer.parseInt(val));
					}
					if("exceltitle".equals(key)){
						String[] v = val.split(",");
						for (String vo : v) {
							dt.addTitle(vo);
						}
					}
					
			   }
			   dbCfgs.put(dt.dbField, dt); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	       return  dbCfgs;
	}
	/**
	 * 
	 * @title mrr文件的数据处理
	 * @param fileName
	 * @param mrrId
	 * @param context
	 * @param token
	 * @author chao.xj
	 * @date 2014-9-1下午3:04:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void dataProcess(Connection connection,String fileName,
			RnoDataCollectRec dataRec,Rate rate) {

		boolean hasHrate=false,hasFrate=false;
		hasHrate=rate.isHasHrate();
		hasFrate=rate.isHasFrate();
		log.debug("hasHrate,hasFrate:"+hasHrate+"----"+hasFrate);
		Statement statement = null;
		String msg = "";
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		/*try {
			if(rate.isHasFrate()){
				printTmpTable("RNO_2G_HW_MRR_FRATE_T", connection.createStatement());
				}else{
					printTmpTable("RNO_2G_HW_MRR_HRATE_T", connection.createStatement());
				}
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		/**** 解析文件时已经插入临时表信息－－下面将利用临时表数据进行处理  ****/
		ResultInfo resultInfo=null;
		//处理半速率的描述信息
		if(hasHrate){
		resultInfo=rnoStructAnaV2.generateHwMrrDesc("HALF","RNO_2G_HW_MRR_HRATE_T","RNO_2G_HW_MRR_DESC",statement,connection,dataRec.getCityId());
		log.debug("处理半速率的描述信息 resultInfo"+resultInfo);
		}
		//处理全速率的描述信息
		if(hasFrate){
		resultInfo=rnoStructAnaV2.generateHwMrrDesc("FULL","RNO_2G_HW_MRR_FRATE_T","RNO_2G_HW_MRR_DESC",statement,connection,dataRec.getCityId());
		log.debug("处理全速率的描述信息 resultInfo"+resultInfo);
		}
		/**** 将临时表信息转移到正式表 ****/
		// 保存mrr描述信息
		long cityId = dataRec.getCityId();

		//半速率
		if(hasHrate){
			String fields = "MEA_DATE,BSC,CELL,TRX_IDX,S4100C,S4101C,S4102C,S4103C,S4104C,S4105C,S4106C,S4107C,S4110C,S4111C,S4112C,S4113C,S4114C,S4115C,S4116C,S4117C,S4120C,S4121C,S4122C,S4123C,S4124C,S4125C,S4126C,S4127C,S4130C,S4131C,S4132C,S4133C,S4134C,S4135C,S4136C,S4137C,S4140C,S4141C,S4142C,S4143C,S4144C,S4145C,S4146C,S4147C,S4150C,S4151C,S4152C,S4153C,S4154C,S4155C,S4156C,S4157C,S4160C,S4161C,S4162C,S4163C,S4164C,S4165C,S4166C,S4167C,S4170C,S4171C,S4172C,S4173C,S4174C,S4175C,S4176C,S4177C,S4100D,S4101D,S4102D,S4103D,S4104D,S4105D,S4106D,S4107D,S4110D,S4111D,S4112D,S4113D,S4114D,S4115D,S4116D,S4117D,S4120D,S4121D,S4122D,S4123D,S4124D,S4125D,S4126D,S4127D,S4130D,S4131D,S4132D,S4133D,S4134D,S4135D,S4136D,S4137D,S4140D,S4141D,S4142D,S4143D,S4144D,S4145D,S4146D,S4147D,S4150D,S4151D,S4152D,S4153D,S4154D,S4155D,S4156D,S4157D,S4160D,S4161D,S4162D,S4163D,S4164D,S4165D,S4166D,S4167D,S4170D,S4171D,S4172D,S4173D,S4174D,S4175D,S4176D,S4177D,CITY_ID";
			String sql="insert into RNO_2G_HW_MRR_HRATE(MRR_DESC_ID,"+fields+") select RNO_2G_HW_MRR_DESC_ID,"+fields + " from RNO_2G_HW_MRR_HRATE_T";
			
			log.info(">>>>>>>>>>>>>>>转移半速率信息中间表数据到目标表的sql：" + sql);
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("转移半速率信息中间表数据到目标表的sql出错：" + sql);
				e.printStackTrace();
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				msg = "数据处理出错！code=mrr-11";
				log.error(msg);
				try {
					statement.close();
				} catch (Exception e3) {

				}
				return;
			}
			log.info("<<<<<<<<<<<<<<<完成转移半速率信息中间表数据到目标表");
		
		}
		//全速率
		if(hasFrate){
			String fields = "MEA_DATE,BSC,CELL,TRX_IDX,S4100A,S4101A,S4102A,S4103A,S4104A,S4105A,S4106A,S4107A,S4110A,S4111A,S4112A,S4113A,S4114A,S4115A,S4116A,S4117A,S4120A,S4121A,S4122A,S4123A,S4124A,S4125A,S4126A,S4127A,S4130A,S4131A,S4132A,S4133A,S4134A,S4135A,S4136A,S4137A,S4140A,S4141A,S4142A,S4143A,S4144A,S4145A,S4146A,S4147A,S4150A,S4151A,S4152A,S4153A,S4154A,S4155A,S4156A,S4157A,S4160A,S4161A,S4162A,S4163A,S4164A,S4165A,S4166A,S4167A,S4170A,S4171A,S4172A,S4173A,S4174A,S4175A,S4176A,S4177A,S4100B,S4101B,S4102B,S4103B,S4104B,S4105B,S4106B,S4107B,S4110B,S4111B,S4112B,S4113B,S4114B,S4115B,S4116B,S4117B,S4120B,S4121B,S4122B,S4123B,S4124B,S4125B,S4126B,S4127B,S4130B,S4131B,S4132B,S4133B,S4134B,S4135B,S4136B,S4137B,S4140B,S4141B,S4142B,S4143B,S4144B,S4145B,S4146B,S4147B,S4150B,S4151B,S4152B,S4153B,S4154B,S4155B,S4156B,S4157B,S4160B,S4161B,S4162B,S4163B,S4164B,S4165B,S4166B,S4167B,S4170B,S4171B,S4172B,S4173B,S4174B,S4175B,S4176B,S4177B,CITY_ID";
			String sql="insert into RNO_2G_HW_MRR_FRATE(MRR_DESC_ID,"+fields+") select RNO_2G_HW_MRR_DESC_ID,"+fields + " from RNO_2G_HW_MRR_FRATE_T";
			
			log.info(">>>>>>>>>>>>>>>转移全速率信息中间表数据到目标表的sql：" + sql);
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("转移全速率信息中间表数据到目标表的sql出错：" + sql);
				e.printStackTrace();
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				msg = "数据处理出错！code=mrr-12";
				log.error(msg);
				try {
					statement.close();
				} catch (Exception e3) {

				}
				return;
			}
			log.info("<<<<<<<<<<<<<<<完成转移全速率信息中间表数据到目标表");
		}
		try {
			connection.commit();
			statement.close();
		} catch (Exception e3) {

		}
	}
	protected long getNextSeqValue(String seq,Connection connection){
		long descId=-1;
		String vsql = "select "+seq+".NEXTVAL as id from dual";
		Statement pstmt =null;
		try {
			pstmt=connection.createStatement();
		} catch (SQLException e3) {
			e3.printStackTrace();
			return -1;
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery(vsql);
			rs.next();
			descId= rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return descId;
	}
	static class DBFieldToTitle {
		String dbField;
		int matchType;// 0：模糊匹配，1：精确匹配
		int index = -1;// 在文件中出现的位置，从0开始
		boolean isMandatory = true;// 是否强制要求出现
		private String dbType;// 类型，Number，String,Date
		List<String> titles = new ArrayList<String>();

		int sqlIndex = -1;// 在sql语句中的位置

		public String getDbField() {
			return dbField;
		}

		public void setDbField(String dbField) {
			this.dbField = dbField;
		}

		public int getMatchType() {
			return matchType;
		}

		public void setMatchType(int matchType) {
			this.matchType = matchType;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public List<String> getTitles() {
			return titles;
		}

		public void setTitles(List<String> titles) {
			this.titles = titles;
		}

		public boolean isMandatory() {
			return isMandatory;
		}

		public void setMandatory(boolean isMandatory) {
			this.isMandatory = isMandatory;
		}

		public String getDbType() {
			return dbType;
		}

		public void setDbType(String dbType) {
			this.dbType = dbType;
		}

		public void addTitle(String t) {
			if (!StringUtils.isBlank(t)) {
				titles.add(t);
			}
		}

		@Override
		public String toString() {
			return "DBFieldToTitle [dbField=" + dbField + ", matchType="
					+ matchType + ", index=" + index + ", isMandatory="
					+ isMandatory + ", dbType=" + dbType + ", titles=" + titles
					+ "]";
		}

	}
	static class Rate{
		private boolean hasFrate;
		private boolean hasHrate;
		public boolean isHasFrate() {
			return hasFrate;
		}
		public void setHasFrate(boolean hasFrate) {
			this.hasFrate = hasFrate;
		}
		public boolean isHasHrate() {
			return hasHrate;
		}
		public void setHasHrate(boolean hasHrate) {
			this.hasHrate = hasHrate;
		}
		
	}
	private void printTmpTable(String tab, Statement stmt) {
		// TODO Auto-generated method stub
		String sql = "select * from " + tab;
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			log.debug(tab + "\r\n---数据如下：");
			for (Map<String, Object> one : res) {
				log.debug(one);
			}
			log.debug(tab + "---数据展示完毕\r\n");
		} else {
			log.debug(tab + "---数据为空！");
		}
	}
	public static void main(String[] args) {
		//Map<String, DBFieldToTitle> map=readDbToTitleCfgFromProfile("hw2GMrrFRateDbToTitles.properties");
		Map<String, DBFieldToTitle> map=readDbToTitleCfgFromXml("hw2GMrrFRateDbToTitles.xml");
		System.out.println("进入main方法");
		for (String key : map.keySet()) {
//			log.debug("key:"+key+"----------"+"val:"+map.get(key));
			System.out.println("key:"+key+"----------"+"val:"+map.get(key));
		}
		System.out.println(StringUtils.startsWith("中国共和国人民", "中国"));
		// String dd = "123";
		// //log.debug(StringUtils.isNumeric(dd));

		// 读取数据库与文本文件格式的配置

		// Map<String, DBFieldToTitle> dbFieldsToTitles =
		// readDbToTitleCfgFromProfile();
		// HwNcsParserJobRunnable jr = new HwNcsParserJobRunnable();
		// jr.dbFieldsToTitles = dbFieldsToTitles;
		// // 拼装sql
		// insertInoTempTableSql = "insert into " + tempTable + " (";
		// String ws = "";
		// int index = 1;
		// for (String d : dbFieldsToTitles.keySet()) {
		// dbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
		// insertInoTempTableSql += d + ",";
		// ws += "?,";
		// }
		// insertInoTempTableSql = insertInoTempTableSql.substring(0,
		// insertInoTempTableSql.length() - 1);
		// ws = ws.substring(0, ws.length() - 1);
		// insertInoTempTableSql += ") values ( " + ws + " )";
		// // log.debug("华为ncs插入临时表的sql=" + insertInoTempTableSql);
		//
		// File f = new File(
		// "D:/tmp/华为测量数据（20140820）--解压后/(未命名)_46-20140821090058(DGM31B3).csv");
		// jr.parseHwNcs(null, null, "(未命名)_46-20140821090058(DGM31B3).csv", f,
		// null, null, 104, dbFieldsToTitles);

		// String str="S394:服务小区与邻区信号强度差小于邻区干扰电平门限0的测量报告数";
		// String prefix="S394";
		// //log.debug(StringUtils.startsWith(str, prefix));

	}
	
	@Override
	public void updateOwnProgress(JobStatus jobStatus) {
		if (jobStatus == null) {
			return;
		}
		if (stmt != null) {
			try {
				String prog = jobStatus.getProgress();
				if (prog == null) {
					prog = "";
				}
				prog = prog.trim();
				if ("".equals(prog)) {
					prog = jobStatus.getJobState().getCode();
				}
				String str = "update RNO_DATA_COLLECT_REC set file_status='"
						+ prog + "' where job_id=" + jobStatus.getJobId();
				stmt.executeUpdate(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	private File file=null;
	private String filePath=null;
	@Override
	public void releaseRes() {
		try {
			if (file != null && file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileTool.deleteLocOrHdfsFileOrDir(filePath);
		super.releaseRes();
	}
}
