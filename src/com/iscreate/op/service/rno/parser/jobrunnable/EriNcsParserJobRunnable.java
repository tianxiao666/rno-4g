package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.iscreate.op.dao.rno.RnoStructAnaV2;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellForMatch;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellLonLat;
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
import com.iscreate.op.service.rno.parser.ParserContext;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.NcsAdmRecord;
import com.iscreate.op.service.rno.parser.vo.NcsCellData;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.parser.vo.NcsNcellData;
import com.iscreate.op.service.rno.parser.vo.NcsRecord;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.TranslateTools;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.LatLngHelper;

public class EriNcsParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(EriNcsParserJobRunnable.class);

	private static int AdministratorRecordLen = 52;

	private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();
	// 语句
	// 管理头部信息
	private static String insertAdmDataSql = "insert into RNO_2G_ERI_NCS_DESCRIPTOR (RNO_2G_ERI_NCS_DESC_ID,NAME,MEA_TIME,RECORD_COUNT,SEGTIME,RELSSN,ABSS,NUMFREQ,RECTIME,NET_TYPE,VENDOR,CREATE_TIME,MOD_TIME,STATUS,FILE_FORMAT,RID,TERM_REASON,ECNOABSS,RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5,NCELLTYPE,NUCELLTYPE,TFDDMRR,NUMUMFI,TNCCPERM_INDICATOR,TNCCPERM_BITMAP,TMBCR,CITY_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 插入小区信息
	private static String insertCellDataSql = "insert into rno_2G_ERI_ncs_cell_data(ID,RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS,MEA_TIME,CITY_ID) values(SEQ_RNO_NCS_CELL_DATA.NEXTVAL,?,?,?,?,?,?,?,?,?)";
	// 邻区测量信息
	private String insertNcellToMidDataSql = "insert into RNO_2G_ERI_NCS_T(RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,MEA_TIME,CITY_ID,NCELL,NCELLS,DISTANCE,CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION,NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING,CELL_TO_NCELL_DIR,SAME_FREQ_CNT,ADJ_FREQ_CNT) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";// ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?

	public EriNcsParserJobRunnable() {
		super();
		super.setJobType("ERICSSONNCSFILE");
	}

	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {

		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

		// 准备资源
		// Statement stmt = null;
		// try {
		// stmt = connection.createStatement();
		// } catch (SQLException e) {
		// e.printStackTrace();
		//
		// // 失败了
		// status.setJobState(JobState.Failed);
		// status.setUpdateTime(new Date());
		//
		// // 报告
		// report.setFinishState("失败");
		// report.setStage("准备数据库连接");
		// report.setBegTime(new Date());
		// report.setReportType(2);
		// addJobReport(report);
		//
		// return status;
		// }

		// 正在运行
		// status.setJobRunningStatus(JobRunningStatus.Running);
		status.setJobState(JobState.Running);
		status.setUpdateTime(new Date());

		// 获取job相关的信息
		String sql = "select * from RNO_DATA_COLLECT_REC where JOB_ID=" + jobId;
		List<Map<String, Object>> recs = RnoHelper.commonQuery(stmt, sql);
		RnoDataCollectRec dataRec = null;
		if (recs != null && recs.size() > 0) {
			dataRec = RnoHelper.commonInjection(RnoDataCollectRec.class,
					recs.get(0), new DateUtil());
		}
		log.debug("jobId=" + jobId + ",对应的dataRec=" + dataRec);
		if (dataRec == null) {
			log.error("转换RnoDataCollectRec失败！");
		}

		// 准备
		long cityId = dataRec.getCityId();
		String fileName = dataRec.getFileName();
		filePath = FileInterpreter.makeFullPath(dataRec.getFullPath());
		
		file = FileTool.getFile(filePath);

		String msg = "";

		// 开始解析
		List<File> allNcsFiles = new ArrayList<File>();// 将所有待处理的ncs文件放置在这个列表里
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
				File file1 = new File(FileInterpreter.makeFullPath(file
						.getAbsolutePath()));
				log.debug("file.getAbsolutePath()="
						+ FileInterpreter.makeFullPath(file.getAbsolutePath())
						+ ",存在？" + file1.exists());

				/*
				 * unzipOk = FileTool.unZip(
				 * FileInterpreter.makeFullPath(file.getAbsolutePath()),
				 * destDir);
				 */
				unzipOk = ZipFileHandler.unZip(
						FileInterpreter.makeFullPath(file.getAbsolutePath()),
						destDir);
			} catch (Exception e) {
				msg = "压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
				e.printStackTrace();
				// super.setCachedInfo(token, msg);
				clearResource(destDir, null);

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
				clearResource(destDir, null);

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
					allNcsFiles.add(f);
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
			clearResource(destDir, null);

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
			log.info("上传的ncs是一个普通文件。");
			allNcsFiles.add(file);
		}

		if (allNcsFiles.isEmpty()) {
			msg = "未上传有效的ncs文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的ncs文件！注意zip包里不能再包含有文件夹！");
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

		// 开始分析
		ParserContext context = new ParserContext();
		// 设置必要的信息
		// 1、设置statment
		// 创建statement
		PreparedStatement insertAdmStmt = null;
		try {
			insertAdmStmt = connection.prepareStatement(insertAdmDataSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-1";
			log.error(msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信ncs描述信息相关数据库资源时出错", new Date(), this,
					stmt, report, status, dataRec.getDataCollectId(),
					DataParseStatus.Failall, DataParseProgress.Prepare);
		}
		context.setPreparedStatment("NcsAdmRecord", insertAdmStmt);

		PreparedStatement insertCellDataStmt = null;
		List<Put> cellDataPuts = null;
		try {
			insertCellDataStmt = connection.prepareStatement(insertCellDataSql);
			// hbase 小区数据
			cellDataPuts = new ArrayList<Put>();
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-2";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信ncs cell信息相关数据库资源时出错", new Date(),
					this, stmt, report, status, dataRec.getDataCollectId(),
					DataParseStatus.Failall, DataParseProgress.Prepare);
		}
		context.setPreparedStatment("NcsCellData", insertCellDataStmt);
		// hbase 小区数据
		context.setHbasePuts("NcsCellData", cellDataPuts);
		PreparedStatement insertNCellDataStmt = null;
		List<Put> ncellDataPuts = null;
		List<Put> cellMeatimePuts = null;
		try {
			insertNCellDataStmt = connection
					.prepareStatement(insertNcellToMidDataSql);
			// hbase 邻区数据
			ncellDataPuts = new ArrayList<Put>();
			// 索引
			cellMeatimePuts = new ArrayList<Put>();
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=ncs-3";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信ncs ncell信息相关数据库资源时出错", new Date(),
					this, stmt, report, status, dataRec.getDataCollectId(),
					DataParseStatus.Failall, DataParseProgress.Prepare);
		}
		context.setPreparedStatment("NcsNcellData", insertNCellDataStmt);
		context.setHbasePuts("NcsNcellData", ncellDataPuts);
		context.setHbasePuts("CellMeaTime", cellMeatimePuts);
		// 2、设置bscname，area，freqsection
		context.setCityId(cityId);
		// -------------
		String tmpFileName = fileName;
		int sucCnt = 0;
		boolean parseOk = false;
		ResultInfo result = rnoStructAnaV2.prepareEriCityCellData(stmt,
				"RNO_CELL_CITY_T", cityId);
		if (!result.isFlag()) {
			log.error("准备分析用的小区数据时，出错！");
			clearResource(destDir, context);
			return failWithStatus("准备分析用的小区数据时，出错时出错", new Date(), this, stmt,
					report, status, dataRec.getDataCollectId(),
					DataParseStatus.Failall, DataParseProgress.Prepare);
		}
		int totalFileCnt = allNcsFiles.size();
		int i = 0;

		// 获取内存匹配邻区所需要的数据
		CellForMatch cfm = rnoStructAnaV2.getMatchCellContext(stmt, cityId);

		for (File f : allNcsFiles) {
			i++;
			if (super.isStop()) {
				log.debug("爱立信ncs解析任务" + job + "  被停止。");
				break;
			}
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				parseOk = parseNcs(this, report, tmpFileName, f, context,
						connection, stmt, cityId, cfm);

				date2 = new Date();
				report.setReportType(1);
				report.setStage("文件处理总结");
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
					sucCnt++;
				} else {
					report.setAttMsg("失败完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
				}
				addJobReport(report);

			} catch (Exception e) {
				e.printStackTrace();
				date2 = new Date();
				msg = tmpFileName + "文件解析出错！";
				report.setReportType(1);
				report.setStage("文件处理总结");
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setAttMsg("文件解析出错（" + i + "/" + totalFileCnt + "）:"
						+ tmpFileName);
				addJobReport(report);
				log.error(msg);
			}
		}
		// // 清空分析表
		// try {
		// stmt.executeUpdate("delete from RNO_CELL_CITY_T");
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		if (super.isStop()) {
			// status.setJobRunningStatus(JobRunningStatus.Stopped);
			status.setJobState(JobState.Killed);
			status.setUpdateTime(new Date());
		} else {
			if (sucCnt > 0) {
				// status.setJobRunningStatus(JobRunningStatus.Succeded);
				status.setJobState(JobState.Finished);
				status.setUpdateTime(new Date());
			} else {
				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());
			}
		}

		if (super.isStop()) {
			sql = "update rno_data_collect_rec set file_status='停止' where data_collect_id="
					+ dataRec.getDataCollectId();
		} else {
			if (sucCnt == allNcsFiles.size()) {
				// 全部成功
				sql = "update rno_data_collect_rec set file_status='"
						+ DataParseStatus.Succeded.toString()
						+ "' where data_collect_id="
						+ dataRec.getDataCollectId();
			} else {
				if (sucCnt > 0) {
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
		}

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// truncate table:RNO_CELL_CITY_T
		sql = "truncate table RNO_CELL_CITY_T";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		rnoStructAnaV2.clearMatchCellContext();
		clearResource(destDir, context);
		return status;
	}

	/**
	 * 清理资源
	 * 
	 * @param destDir
	 * @param context
	 * @author brightming 2014-1-20 上午10:51:14
	 */
	private void clearResource(String destDir, ParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeAllStatement();
		}
	}

	public boolean parseNcs(JobRunnable jobWorker, JobReport report,
			String fileName, File file, ParserContext context,
			Connection connection, Statement statement, long cityId,
			CellForMatch cfm) throws Exception {
		InputStream is = new FileInputStream(file);
		byte[] typeByte = new byte[1];
		byte[] lenByte = new byte[2];
		byte[] content = new byte[512];

		long st = System.currentTimeMillis();
		// 先把相关的数据都clear
		context.clearBatchAllStatement();

		// int cnt = 0;
		long t1, t2;

		String attMsg = "文件:" + fileName;

		int len = -1;
		int type = -1;
		NcsRecord rec = null;
		long ncsId = -1;// 预先申请的ncsid
		Date d1 = new Date(), d2;
		t1 = System.currentTimeMillis();

		boolean fileHead = true;// 读文件的最开始部分
		while ((len = is.read(typeByte, 0, 1)) != -1) {
			if (fileHead) {
				// 文件的第一个字节如果不是50，就说明错了
				len = TranslateTools.makeIntFromByteArray(typeByte, 0, 1);
				if (len != 50) {
					log.error("文件头部不对！期望读取到50，表示NCS管理信息头部内容开始，当前读取到：" + len);
					report.setFields("读取爱立信ncs文件头", d1, new Date(),
							DataParseStatus.Failall.toString(),
							"文件头部不对！期望读取到50，表示NCS管理信息头部内容开始，当前读取到：" + len);
					addJobReport(report);
					is.close();
					return false;
				}
				fileHead = false;
			}
			type = -1;
			len = is.read(lenByte, 0, 2);
			if (len != 2) {
				log.error("异常中断或格式错误！");
				context.appedErrorMsg("文件:[" + file.getName()
						+ "]异常中断或格式错误！<br/>");
				break;
			}
			len = TranslateTools.makeIntFromByteArray(lenByte, 0, 2);
			if (len <= 0) {
				log.error("长度信息有误！");
				context.appedErrorMsg("文件:[" + file.getName()
						+ "]的长度信息有误！<br/>");
				break;
			}
			is.read(content, 0, len - 3);// 减去type占用的1个字节，length内容占用的2个字节
			byte[] wholeSection = mergeByte(typeByte, lenByte,
					TranslateTools.subByte(content, 0, len - 3));

			type = TranslateTools.makeIntFromByteArray(typeByte, 0, 1);
			rec = handleSection(type, wholeSection, context);

			// 处理rec
			if (rec != null) {
				if (ncsId == -1) {
					// 申请id
					ncsId = RnoHelper.getNextSeqValue("seq_rno_ncs_descriptor",
							statement);
				}
				// 转换成sql语句
				prepareRecordSql(ncsId, rec, context, cfm);
			}
		}

		is.close();

		t2 = System.currentTimeMillis();
		log.debug("爱立信ncs文件解析耗时：" + (t2 - t1) + "ms");
		if (ncsId != -1) {
			PreparedStatement pstmt = context
					.getPreparedStatement("NcsAdmRecord");
			setAdmRecordToStmt(fileName, pstmt, ncsId, context.getAreaId(),
					context.getCityId(), context.getLastNcsAdmRecord(ncsId),
					context.getDateUtil());
		}
		is.close();
		d2 = new Date();

		// --报告--//
		report.setSystemFields(DataParseProgress.Decode.toString(), d1, d2,
				DataParseStatus.Succeded.toString(), attMsg);
		addJobReport(report);

		// 对数据的后续处理
		// 执行批处理插入
		// ----ncs报告信息----//
		d1 = new Date();
		log.info("准备批处理插入ncs报告信息。。。");
		PreparedStatement pstmt = context.getPreparedStatement("NcsAdmRecord");
		try {
			pstmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			connection.rollback();
		}
		pstmt.clearBatch();

		// ---小区数据-----//
		log.info("准备批处理插入ncs小区测量信息。。。");
		pstmt = context.getPreparedStatement("NcsCellData");
		List<Put> puts = context.getHbasePuts("NcsCellData");
		HTable cellTable = null;
		try {
			pstmt.executeBatch();
			cellTable = new HTable(HadoopXml.getHbaseConfig(),
					NcsIndex.ERI_NCS_CELL_T.str);
			cellTable.setAutoFlushTo(false);
			cellTable.put(puts);
		} catch (SQLException e2) {
			e2.printStackTrace();
			connection.rollback();
			return false;
		}

		// ---邻区测量数据-----//
		pstmt.clearBatch();
		puts.clear();
		log.info("准备批处理插入ncs邻区测量信息。。。");
		pstmt = context.getPreparedStatement("NcsNcellData");
		puts = context.getHbasePuts("NcsNcellData");
		HTable ncellTable = null;

		try {
			pstmt.executeBatch();

			ncellTable = new HTable(HadoopXml.getHbaseConfig(),
					NcsIndex.ERI_NCS_T.str);
			ncellTable.setAutoFlushTo(false);
			ncellTable.put(puts);
			puts.clear();
		} catch (SQLException e2) {
			e2.printStackTrace();
			connection.rollback();
			return false;
		}
		puts = context.getHbasePuts("CellMeaTime");
		HTable cellMeaTable = null;
		try {
			cellMeaTable = new HTable(HadoopXml.getHbaseConfig(),
					NcsIndex.LTE4G_CELL_MEATIME_T.str);
			cellMeaTable.setAutoFlushTo(false);
			cellMeaTable.put(puts);
			puts.clear();
		} catch (IOException e3) {
			return false;
		}
		// --报告--//
		d2 = new Date();
		report.setSystemFields(DataParseProgress.SaveToDb.toString(), d1, d2,
				DataParseStatus.Succeded.toString(), "文件:" + fileName);
		addJobReport(report);

		d1 = new Date();
		ResultInfo resultInfo = null;
		// 邻区匹配,完善小区的频点等相关信息
		/*
		 * log.info(">>>>>>>>>>>>>>>开始邻区匹配..."); t1 =
		 * System.currentTimeMillis(); ResultInfo resultInfo =
		 * rnoStructAnaV2.matchEriNcsNcell(statement, "RNO_2G_ERI_NCS_T",
		 * "RNO_CELL_CITY_T", cityId); t2 = System.currentTimeMillis();
		 * log.info("<<<<<<<<<<<<<<<完成邻区匹配。耗时:" + (t2 - t1) + "ms"); //
		 * --报告---// d2 = new Date(); if (resultInfo.isFlag()) {
		 * report.setSystemFields("邻区匹配", d1, d2,
		 * DataParseStatus.Succeded.toString(), attMsg); addJobReport(report); }
		 * else { connection.rollback(); report.setSystemFields("邻区匹配", d1, d2,
		 * DataParseStatus.Failall.toString(), attMsg + "--" +
		 * resultInfo.getMsg()); addJobReport(report); return false; }
		 */

		// 通过程序来判断bsc、频段，不需要用户填写
		log.info(">>>>>>开始自动计算ncs所测量的bsc、测量小区的频段");
		// setTokenInfo(token, "正在匹配ncs所测量的bsc、小区频段");
		t1 = System.currentTimeMillis();
		d1 = new Date();
		// 此处针对的是ERI NCS DESC表
		resultInfo = rnoStructAnaV2.matchEriNcsBscAndFreqSection(statement,
				"RNO_2G_ERI_NCS_T", "RNO_CELL_CITY_T",
				"RNO_2G_ERI_NCS_DESCRIPTOR", ncsId);
		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成计算ncs所测量的bsc、测量小区的频段。耗时:" + (t2 - t1) + "ms");
		// --报告---//
		d2 = new Date();
		if (resultInfo.isFlag()) {
			report.setSystemFields("匹配BSC与频段信息", d1, d2,
					DataParseStatus.Succeded.toString(), attMsg);
			addJobReport(report);
		} else {
			connection.rollback();
			report.setSystemFields("匹配BSC与频段信息", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}

		// 计算同频干扰系数、邻频干扰系数分子、分母
		// 同频干扰系数-12，邻频干扰系数+3
		d1 = new Date();
		NcsAdmRecord admRec = context.getLastNcsAdmRecord(ncsId);
		resultInfo = rnoStructAnaV2.prepareCIAndCADivider(statement,
				"RNO_2G_ERI_NCS_T", admRec);
		d2 = new Date();
		if (resultInfo.isFlag()) {
			report.setSystemFields("计算同频干扰系数的分子、分母", d1, d2,
					DataParseStatus.Succeded.toString(), attMsg);
			addJobReport(report);
		} else {
			log.error(resultInfo.getMsg());
			connection.rollback();
			report.setSystemFields("计算同频干扰系数的分子、分母", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}

		// 转移中间表数据到正式表
		d1 = new Date();
		String fields = "RNO_2G_ERI_NCS_DESC_ID,CELL,NCELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,DISTANCE,INTERFER,CA_INTERFER,NCELLS,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CI_DIVIDER,CA_DIVIDER,CELL_INDOOR,NCELL_INDOOR,MEA_TIME,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,NCELL_SITE,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_TO_NCELL_DIR,CELL_BCCH,CELL_TCH,NCELL_TCH,CITY_ID,NCELL_BEARING";
		String translateSql = "insert into RNO_2G_ERI_NCS(RNO_2G_ERI_NCS_ID,"
				+ fields + ") SELECT SEQ_RNO_NCS.nextval," + fields
				+ " from RNO_2G_ERI_NCS_T";
		t1 = t2;
		log.info(">>>>>>>>>>>>>>>转移中间表数据到目标表的sql：" + translateSql);
		t1 = t2;
		try {
			statement.executeUpdate(translateSql);
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
			d2 = new Date();
			report.setSystemFields("转移到存储空间", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}

		// --报告--//
		d2 = new Date();
		report.setSystemFields("转移到存储空间", d1, d2,
				DataParseStatus.Succeded.toString(), attMsg);
		addJobReport(report);

		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成转移中间表数据到目标表，耗时:" + (t2 - t1) + "ms");
		long et = System.currentTimeMillis();
		log.info("退出对爱立信NCS文件：" + fileName + "的解析，耗时：" + (et - st) + "ms");

		// 一个文件一个提交
		connection.commit();
		cellTable.flushCommits();
		ncellTable.flushCommits();
		cellMeaTable.flushCommits();
		
		cellTable.close();
		ncellTable.close();
		cellMeaTable.close();
		return true;
	}

	/**
	 * 准备批处理插入语句
	 * 
	 * @param ncsId
	 * @param rec
	 * @param context
	 * @author brightming 2014-1-13 下午2:10:46
	 */
	private void prepareRecordSql(long ncsId, NcsRecord rec,
			ParserContext context, CellForMatch cfm) {
		if (rec instanceof NcsAdmRecord) {
			// 特别处理
			context.addNcsAdmRecord(ncsId, (NcsAdmRecord) rec);
			context.setMeaTime(((NcsAdmRecord) rec).getStartTime(context
					.getDateUtil()));
		} else if (rec instanceof NcsCellData) {
			PreparedStatement stmt = context
					.getPreparedStatement("NcsCellData");
			List<Put> cellDataPuts = context.getHbasePuts("NcsCellData");
			/*
			 * setCellDataToStmt(stmt, ncsId, (NcsCellData) rec,
			 * context.getMeaTime(), context.getCityId());
			 */
			setCellDataToStmt(stmt, ncsId, (NcsCellData) rec,
					context.getMeaTime(), context.getCityId(), cellDataPuts);
		} else if (rec instanceof NcsNcellData) {
			// 对于这数据量为0的，直接丢掉
			if (((NcsNcellData) rec).getRepArfcn() <= 0) {
				return;
			}
			PreparedStatement stmt = context
					.getPreparedStatement("NcsNcellData");
			List<Put> ncellDataPuts = context.getHbasePuts("NcsNcellData");
			List<Put> cellMeatimePuts = context.getHbasePuts("CellMeaTime");
			/*
			 * setNcellDataToStmt(stmt, ncsId, (NcsNcellData) rec,
			 * context.getMeaTime(), context.getCityId(), cfm);
			 */
			setNcellDataToStmt(stmt, ncsId, (NcsNcellData) rec,
					context.getMeaTime(), context.getCityId(), cfm,
					ncellDataPuts, cellMeatimePuts,
					context.getLastNcsAdmRecord(ncsId));
		} else {
			log.warn("暂时不处理此类型记录！");
		}

	}

	/**
	 * 设置管理头部信息
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午3:33:40
	 */
	private void setAdmRecordToStmt(String fileName, PreparedStatement stmt,
			long ncsId, long areaId, long cityId, NcsAdmRecord rec,
			DateUtil dateUtil) {
		if (stmt == null || rec == null) {
			return;
		}
		int index = 1;
		Date st = rec.getStartTime(dateUtil);
		if (st == null) {
			log.error("管理头部：" + rec + " 的记录开始时间有问题！");
			st = new Date();
			return;
		}
		java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
		try {
			stmt.setLong(index++, ncsId);
			stmt.setString(index++, fileName);
			// stmt.setString(index++, bsc);
			// stmt.setString(index++, freqSection);

			stmt.setTimestamp(index++, new java.sql.Timestamp(st.getTime()));
			stmt.setLong(index++, rec.getRecordInfo());// 最后一个的改字段为record count
			stmt.setInt(index++, rec.getSegTime());
			stmt.setNull(index++, Types.INTEGER);
			stmt.setInt(index++, rec.getAbss());
			stmt.setInt(index++, rec.getNumFreq());

			stmt.setInt(index++, rec.getRecTime());
			stmt.setString(index++, "GSM");
			stmt.setString(index++, "ERICSSON");
			stmt.setTimestamp(index++, now);
			stmt.setTimestamp(index++, now);
			stmt.setString(index++, "N");

			stmt.setInt(index++, rec.getFileFormat());
			stmt.setString(index++, rec.getRid());
			stmt.setInt(index++, rec.getTermReason());
			stmt.setInt(index++, rec.getEcnoAbss());

			stmt.setInt(index++, rec.getRelssSign());
			stmt.setInt(index++, rec.getRelss());
			stmt.setInt(index++, rec.getRelss2Sign());
			stmt.setInt(index++, rec.getRelss2());
			stmt.setInt(index++, rec.getRelss3Sign());
			stmt.setInt(index++, rec.getRelss3());
			stmt.setInt(index++, rec.getRelss4Sign());
			stmt.setInt(index++, rec.getRelss4());
			stmt.setInt(index++, rec.getRelss5Sign());
			stmt.setInt(index++, rec.getRelss5());

			stmt.setInt(index++, rec.getNcellType());
			stmt.setInt(index++, rec.getNUcellType());
			stmt.setInt(index++, rec.getTfddMrr());
			stmt.setInt(index++, rec.getNumUmfi());
			stmt.setInt(index++, rec.getTnccpermValIndi());
			stmt.setInt(index++, rec.getTnccpermBitmap());
			stmt.setInt(index++, rec.getTmbcr());
			stmt.setLong(index++, cityId);

			stmt.addBatch();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置小区测量信息
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午3:16:46
	 */
	private void setCellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsCellData rec, Date meaTime, long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		java.sql.Timestamp meaTimest = new java.sql.Timestamp(meaTime.getTime());
		int index = 1;
		// RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS,MEA_TIME,CITY_ID
		try {

			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setLong(index++, rec.getRep());
			stmt.setLong(index++, rec.getRepHr());
			stmt.setLong(index++, rec.getRepUndefGsm());
			stmt.setInt(index++, rec.getAvss());
			stmt.setTimestamp(index++, meaTimest);
			stmt.setLong(index++, cityId);

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @title 设置小区测量信息及hbase
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @param meaTime
	 * @param cityId
	 * @param puts
	 * @author chao.xj
	 * @date 2014-12-4上午11:07:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void setCellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsCellData rec, Date meaTime, long cityId, List<Put> puts) {
		if (stmt == null || rec == null) {
			return;
		}
		// hbase
		Put put = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ String.valueOf(meaTime.getTime()) + "_" + rec.getCellName()
				+ "_" + String.valueOf(rec.getChgr())));
		put.add(NcsIndex.NCS_CF.str.getBytes(), "RNO_2G_ERI_NCS_DESC_ID"
				.getBytes(), String.valueOf(ncsId).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL".getBytes(), rec
				.getCellName().getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "CHGR".getBytes(), String
				.valueOf(rec.getChgr()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "REP".getBytes(), String
				.valueOf(rec.getRep()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "REPHR".getBytes(), String
				.valueOf(rec.getRepHr()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "REPUNDEFGSM".getBytes(),
				String.valueOf(rec.getRepUndefGsm()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "AVSS".getBytes(), String
				.valueOf(rec.getAvss()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "MEA_TIME".getBytes(), String
				.valueOf(meaTime.getTime()).getBytes());
		put.add(NcsIndex.NCS_CF.str.getBytes(), "CITY_ID".getBytes(), String
				.valueOf(cityId).getBytes());
		puts.add(put);
		java.sql.Timestamp meaTimest = new java.sql.Timestamp(meaTime.getTime());
		int index = 1;
		// RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS,MEA_TIME,CITY_ID
		try {

			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setLong(index++, rec.getRep());
			stmt.setLong(index++, rec.getRepHr());
			stmt.setLong(index++, rec.getRepUndefGsm());
			stmt.setInt(index++, rec.getAvss());
			stmt.setTimestamp(index++, meaTimest);
			stmt.setLong(index++, cityId);

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置ncell的测量结果到批处理stmt
	 * 
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @author brightming 2014-1-13 下午2:40:37
	 */
	private void setNcellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsNcellData rec, Date meaTime, long cityId, CellForMatch cfm) {
		if (stmt == null || rec == null) {
			return;
		}
		java.sql.Timestamp meaTimest = new java.sql.Timestamp(meaTime.getTime());
		int index = 1;
		try {
			// RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,BSIC,ARFCN,
			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setString(index++, rec.getNcellBsicStr());
			stmt.setInt(index++, rec.getArfcn());
			// DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,
			stmt.setInt(index++, rec.getDefinedAsNcell());
			stmt.setLong(index++, rec.getRecTimeArfcn());
			stmt.setLong(index++, rec.getRepArfcn());
			stmt.setLong(index++, rec.getTimes());
			stmt.setInt(index++, rec.getNavss());
			// TIMES1,NAVSS1,TIMES2,NAVSS2,
			stmt.setLong(index++, rec.getTimes1());
			stmt.setInt(index++, rec.getNavss1());
			stmt.setLong(index++, rec.getTimes2());
			stmt.setInt(index++, rec.getNavss2());

			// TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,
			stmt.setLong(index++, rec.getTimes3());
			stmt.setInt(index++, rec.getNavss3());
			stmt.setLong(index++, rec.getTimes4());
			stmt.setInt(index++, rec.getNavss4());
			stmt.setLong(index++, rec.getTimes5());
			stmt.setInt(index++, rec.getNavss5());
			stmt.setLong(index++, rec.getTimes6());
			stmt.setInt(index++, rec.getNavss6());

			// TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,
			stmt.setLong(index++, rec.getTimesRelss());
			stmt.setLong(index++, rec.getTimesRelss2());
			stmt.setLong(index++, rec.getTimesRelss3());
			stmt.setLong(index++, rec.getTimesRelss4());
			stmt.setLong(index++, rec.getTimesRelss5());

			// TIMESABSS,TIMESALONE,
			stmt.setLong(index++, rec.getTimesAbss());
			stmt.setLong(index++, rec.getTimesAlone());

			// MEA_TIME,CITY_ID,
			stmt.setTimestamp(index++, meaTimest);
			stmt.setLong(index++, cityId);

			// 2014-9-16
			// 增加字段：NCELL,NCELLS,DISTANCE,CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION,NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING,CELL_TO_NCELL_DIR,SAME_FREQ_CNT,ADJ_FREQ_CNT
			// 并且将之前的ncell固定设置为null的去掉

			// 此处进行邻区匹配
			// NCELL,NCELLS,DISTANCE,
			NcellInfo ncellInfo = matchNcell(cfm, rec.getCellName(),
					rec.getArfcn() + "", rec.getNcellBsicStr());
			stmt.setString(index++, ncellInfo.getNcell());
			stmt.setString(index++, ncellInfo.getNcells());
			stmt.setDouble(index++, ncellInfo.getDis());
			// stmt.setNull(index++, Types.DOUBLE);

			CellLonLat s_cell = cfm.getCellInfo(rec.getCellName());
			CellLonLat n_cell = cfm.getCellInfo(ncellInfo.getNcell());
			// 设置属性
			if (s_cell != null) {
				// CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,
				stmt.setString(index++, s_cell.getBcch() + "");
				stmt.setString(index++, s_cell.getTch());
				stmt.setInt(index++, s_cell.getTotalFreqCnt());
				stmt.setString(index++, s_cell.getIndoor());

				// CELL_LON,CELL_LAT,CELL_BEARING,
				stmt.setDouble(index++, s_cell.getLon());
				stmt.setDouble(index++, s_cell.getLat());
				stmt.setDouble(index++, s_cell.getBearing());

				// CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION
				stmt.setDouble(index++, s_cell.getDowntilt());
				stmt.setString(index++, s_cell.getSite());
				stmt.setString(index++, s_cell.getFreqSection());
			} else {
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setString(index++, "N");
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setString(index++, rec.getCellName());
				stmt.setNull(index++, Types.VARCHAR);
			}
			if (n_cell != null) {
				// NCELL_TCH,NCELL_FREQ_CNT,
				// NCELL_INDOOR,NCELL_LON,NCELL_LAT,
				// NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING
				stmt.setString(index++, n_cell.getTch());
				stmt.setInt(index++, n_cell.getTotalFreqCnt());
				stmt.setString(index++, n_cell.getIndoor());
				stmt.setDouble(index++, n_cell.getLon());
				stmt.setDouble(index++, n_cell.getLat());
				stmt.setString(index++, n_cell.getSite());
				stmt.setString(index++, n_cell.getFreqSection());
				stmt.setDouble(index++, n_cell.getBearing());
			} else {
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setString(index++, "N");
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setString(index++, "");
				stmt.setString(index++, rec.getArfcn() > 100
						&& rec.getArfcn() < 1000 ? "GSM1800" : "GSM900");
				stmt.setNull(index++, Types.DOUBLE);
			}
			if (s_cell != null && n_cell != null) {
				// CELL_TO_NCELL_DIR,SAME_FREQ_CNT,ADJ_FREQ_CNT
				stmt.setDouble(index++, s_cell.getCellToAnoterCellDir(n_cell));
				stmt.setInt(index++, s_cell.getCoFreqCnt(n_cell));
				stmt.setInt(index++, s_cell.getAdjFreqCnt(n_cell));
			} else {
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setNull(index++, Types.INTEGER);
			}

			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @title 设置ncell的测量结果到批处理stmt和hbase
	 * @param stmt
	 * @param ncsId
	 * @param rec
	 * @param meaTime
	 * @param cityId
	 * @param cfm
	 * @param puts
	 * @author chao.xj
	 * @date 2014-12-4下午1:59:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void setNcellDataToStmt(PreparedStatement stmt, long ncsId,
			NcsNcellData rec, Date meaTime, long cityId, CellForMatch cfm,
			List<Put> puts, List<Put> cellMeatimePuts, NcsAdmRecord admRec) {
		if (stmt == null || rec == null) {
			return;
		}
		// 索引
		Calendar ca = Calendar.getInstance();
		ca.setTime(meaTime);
		Put cellMeatimePut = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ "NCS" + "_" + String.valueOf(ca.get(Calendar.YEAR)) + "_"
				+ rec.getCellName()));
		cellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), String.valueOf(ca.getTime().getTime()).getBytes(),
				null);
		// hbase
		Put put = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ String.valueOf(meaTime.getTime()) + "_" + rec.getCellName()
				+ "_" + String.valueOf(rec.getChgr()) + "_"
				+ String.valueOf(rec.getNcellBsicStr()) + "_"
				+ String.valueOf(rec.getArfcn())));

		java.sql.Timestamp meaTimest = new java.sql.Timestamp(meaTime.getTime());
		int index = 1;
		try {
			// RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,BSIC,ARFCN,
			stmt.setLong(index++, ncsId);
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getChgr());
			stmt.setString(index++, rec.getNcellBsicStr());
			stmt.setInt(index++, rec.getArfcn());

			put.add(NcsIndex.NCS_CF.str.getBytes(), "RNO_2G_ERI_NCS_DESC_ID"
					.getBytes(), String.valueOf(ncsId).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL".getBytes(), rec
					.getCellName().getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CHGR".getBytes(), String
					.valueOf(rec.getChgr()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "BSIC".getBytes(), String
					.valueOf(rec.getNcellBsicStr()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "ARFCN".getBytes(), String
					.valueOf(rec.getArfcn()).getBytes());
			// DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,
			stmt.setInt(index++, rec.getDefinedAsNcell());
			stmt.setLong(index++, rec.getRecTimeArfcn());
			stmt.setLong(index++, rec.getRepArfcn());
			stmt.setLong(index++, rec.getTimes());
			stmt.setInt(index++, rec.getNavss());

			put.add(NcsIndex.NCS_CF.str.getBytes(),
					"DEFINED_NEIGHBOUR".getBytes(),
					String.valueOf(rec.getDefinedAsNcell()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "RECTIMEARFCN".getBytes(),
					String.valueOf(rec.getRecTimeArfcn()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "REPARFCN".getBytes(),
					String.valueOf(rec.getRepArfcn()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES".getBytes(), String
					.valueOf(rec.getTimes()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS".getBytes(), String
					.valueOf(rec.getNavss()).getBytes());
			// TIMES1,NAVSS1,TIMES2,NAVSS2,
			stmt.setLong(index++, rec.getTimes1());
			stmt.setInt(index++, rec.getNavss1());
			stmt.setLong(index++, rec.getTimes2());
			stmt.setInt(index++, rec.getNavss2());

			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES1".getBytes(), String
					.valueOf(rec.getTimes1()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS1".getBytes(), String
					.valueOf(rec.getNavss1()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES2".getBytes(), String
					.valueOf(rec.getTimes2()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS2".getBytes(), String
					.valueOf(rec.getNavss2()).getBytes());
			// TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,
			stmt.setLong(index++, rec.getTimes3());
			stmt.setInt(index++, rec.getNavss3());
			stmt.setLong(index++, rec.getTimes4());
			stmt.setInt(index++, rec.getNavss4());
			stmt.setLong(index++, rec.getTimes5());
			stmt.setInt(index++, rec.getNavss5());
			stmt.setLong(index++, rec.getTimes6());
			stmt.setInt(index++, rec.getNavss6());

			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES3".getBytes(), String
					.valueOf(rec.getTimes3()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS3".getBytes(), String
					.valueOf(rec.getNavss3()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES4".getBytes(), String
					.valueOf(rec.getTimes4()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS4".getBytes(), String
					.valueOf(rec.getNavss4()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES5".getBytes(), String
					.valueOf(rec.getTimes5()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS5".getBytes(), String
					.valueOf(rec.getNavss5()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMES6".getBytes(), String
					.valueOf(rec.getTimes6()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NAVSS6".getBytes(), String
					.valueOf(rec.getNavss6()).getBytes());
			// TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,
			stmt.setLong(index++, rec.getTimesRelss());
			stmt.setLong(index++, rec.getTimesRelss2());
			stmt.setLong(index++, rec.getTimesRelss3());
			stmt.setLong(index++, rec.getTimesRelss4());
			stmt.setLong(index++, rec.getTimesRelss5());

			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESRELSS".getBytes(),
					String.valueOf(rec.getTimesRelss()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESRELSS2".getBytes(),
					String.valueOf(rec.getTimesRelss2()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESRELSS3".getBytes(),
					String.valueOf(rec.getTimesRelss3()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESRELSS4".getBytes(),
					String.valueOf(rec.getTimesRelss4()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESRELSS5".getBytes(),
					String.valueOf(rec.getTimesRelss5()).getBytes());

			// 补充CI/CA分子 开始
			if (admRec == null) {
				log.warn("爱立信NCS文件中管理记录为空！");
			}
			String c_i = admRec.getRelssValueName(-12).toUpperCase();
			if (StringUtils.isBlank(c_i)) {
				c_i = admRec.getRelssValueName(0).toUpperCase();
			}
			String c_a = admRec.getRelssValueName(3).toUpperCase();
			if (StringUtils.isBlank(c_a)) {
				c_a = admRec.getRelssValueName(0).toUpperCase();
			}
			if (StringUtils.isBlank(c_i) || StringUtils.isBlank(c_a)) {
				log.warn("无法找到-12，+3对应的门限参数！");
			}
			if (("TIMES" + c_i).equals("TIMESRELSS")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CI_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss()).getBytes());
			} else if (("TIMES" + c_i).equals("TIMESRELSS2")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CI_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss2()).getBytes());
			} else if (("TIMES" + c_i).equals("TIMESRELSS3")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CI_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss3()).getBytes());
			} else if (("TIMES" + c_i).equals("TIMESRELSS4")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CI_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss4()).getBytes());
			} else if (("TIMES" + c_i).equals("TIMESRELSS5")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CI_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss5()).getBytes());
			}
			if (("TIMES" + c_a).equals("TIMESRELSS")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CA_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss()).getBytes());
			} else if (("TIMES" + c_a).equals("TIMESRELSS2")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CA_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss2()).getBytes());
			} else if (("TIMES" + c_a).equals("TIMESRELSS3")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CA_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss3()).getBytes());
			} else if (("TIMES" + c_a).equals("TIMESRELSS4")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CA_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss4()).getBytes());
			} else if (("TIMES" + c_a).equals("TIMESRELSS5")) {
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CA_DIVIDER".getBytes(),
						String.valueOf(rec.getTimesRelss5()).getBytes());
			}
			// 结束
			// TIMESABSS,TIMESALONE,
			stmt.setLong(index++, rec.getTimesAbss());
			stmt.setLong(index++, rec.getTimesAlone());

			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESABSS".getBytes(),
					String.valueOf(rec.getTimesAbss()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "TIMESALONE".getBytes(),
					String.valueOf(rec.getTimesAlone()).getBytes());
			// MEA_TIME,CITY_ID,
			stmt.setTimestamp(index++, meaTimest);
			stmt.setLong(index++, cityId);

			put.add(NcsIndex.NCS_CF.str.getBytes(), "MEA_TIME".getBytes(),
					String.valueOf(meaTime.getTime()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CITY_ID".getBytes(),
					String.valueOf(cityId).getBytes());
			// 2014-9-16
			// 增加字段：NCELL,NCELLS,DISTANCE,CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION,NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING,CELL_TO_NCELL_DIR,SAME_FREQ_CNT,ADJ_FREQ_CNT
			// 并且将之前的ncell固定设置为null的去掉

			// 此处进行邻区匹配
			// NCELL,NCELLS,DISTANCE,
			NcellInfo ncellInfo = matchNcell(cfm, rec.getCellName(),
					rec.getArfcn() + "", rec.getNcellBsicStr());
			stmt.setString(index++, ncellInfo.getNcell());
			stmt.setString(index++, ncellInfo.getNcells());
			stmt.setDouble(index++, ncellInfo.getDis());
			// stmt.setNull(index++, Types.DOUBLE);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL".getBytes(), String
					.valueOf(ncellInfo.getNcell()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELLS".getBytes(), String
					.valueOf(ncellInfo.getNcells()).getBytes());
			put.add(NcsIndex.NCS_CF.str.getBytes(), "DISTANCE".getBytes(),
					String.valueOf(ncellInfo.getDis()).getBytes());

			CellLonLat s_cell = cfm.getCellInfo(rec.getCellName());
			CellLonLat n_cell = cfm.getCellInfo(ncellInfo.getNcell());
			// 设置属性
			if (s_cell != null) {
				// CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,
				stmt.setString(index++, s_cell.getBcch() + "");
				stmt.setString(index++, s_cell.getTch());
				stmt.setInt(index++, s_cell.getTotalFreqCnt());
				stmt.setString(index++, s_cell.getIndoor());

				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_BCCH".getBytes(),
						String.valueOf(s_cell.getBcch()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_TCH".getBytes(),
						String.valueOf(s_cell.getTch()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CELL_FREQ_CNT".getBytes(),
						String.valueOf(s_cell.getTotalFreqCnt()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CELL_INDOOR".getBytes(),
						String.valueOf(s_cell.getIndoor()).getBytes());

				// CELL_LON,CELL_LAT,CELL_BEARING,
				stmt.setDouble(index++, s_cell.getLon());
				stmt.setDouble(index++, s_cell.getLat());
				stmt.setDouble(index++, s_cell.getBearing());

				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_LON".getBytes(),
						String.valueOf(s_cell.getLon()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_LAT".getBytes(),
						String.valueOf(s_cell.getLat()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CELL_BEARING".getBytes(),
						String.valueOf(s_cell.getBearing()).getBytes());

				// CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION
				stmt.setDouble(index++, s_cell.getDowntilt());
				stmt.setString(index++, s_cell.getSite());
				stmt.setString(index++, s_cell.getFreqSection());

				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CELL_DOWNTILT".getBytes(),
						String.valueOf(s_cell.getDowntilt()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_SITE".getBytes(),
						String.valueOf(s_cell.getSite()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"CELL_FREQ_SECTION".getBytes(),
						String.valueOf(s_cell.getFreqSection()).getBytes());

			} else {
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setString(index++, "N");
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setString(index++, rec.getCellName());
				stmt.setNull(index++, Types.VARCHAR);
			}
			if (n_cell != null) {
				// NCELL_TCH,NCELL_FREQ_CNT,
				// NCELL_INDOOR,NCELL_LON,NCELL_LAT,
				// NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING
				stmt.setString(index++, n_cell.getTch());
				stmt.setInt(index++, n_cell.getTotalFreqCnt());
				stmt.setString(index++, n_cell.getIndoor());
				stmt.setDouble(index++, n_cell.getLon());
				stmt.setDouble(index++, n_cell.getLat());
				stmt.setString(index++, n_cell.getSite());
				stmt.setString(index++, n_cell.getFreqSection());
				stmt.setDouble(index++, n_cell.getBearing());

				put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_TCH".getBytes(),
						String.valueOf(n_cell.getTch()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"NCELL_FREQ_CNT".getBytes(),
						String.valueOf(n_cell.getTotalFreqCnt()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"NCELL_INDOOR".getBytes(),
						String.valueOf(n_cell.getIndoor()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_LON".getBytes(),
						String.valueOf(n_cell.getLon()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_LAT".getBytes(),
						String.valueOf(n_cell.getLat()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"NCELL_SITE".getBytes(),
						String.valueOf(n_cell.getSite()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"NCELL_FREQ_SECTION".getBytes(),
						String.valueOf(n_cell.getFreqSection()).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"NCELL_BEARING".getBytes(),
						String.valueOf(n_cell.getBearing()).getBytes());

			} else {
				stmt.setNull(index++, Types.VARCHAR);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setString(index++, "N");
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setString(index++, "");
				stmt.setString(index++, rec.getArfcn() > 100
						&& rec.getArfcn() < 1000 ? "GSM1800" : "GSM900");
				stmt.setNull(index++, Types.DOUBLE);
			}
			if (s_cell != null && n_cell != null) {
				// CELL_TO_NCELL_DIR,SAME_FREQ_CNT,ADJ_FREQ_CNT
				stmt.setDouble(index++, s_cell.getCellToAnoterCellDir(n_cell));
				stmt.setInt(index++, s_cell.getCoFreqCnt(n_cell));
				stmt.setInt(index++, s_cell.getAdjFreqCnt(n_cell));

				put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_TO_NCELL_DIR"
						.getBytes(),
						String.valueOf(s_cell.getCellToAnoterCellDir(n_cell))
								.getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"SAME_FREQ_CNT".getBytes(),
						String.valueOf(s_cell.getCoFreqCnt(n_cell)).getBytes());
				put.add(NcsIndex.NCS_CF.str.getBytes(),
						"ADJ_FREQ_CNT".getBytes(),
						String.valueOf(s_cell.getAdjFreqCnt(n_cell)).getBytes());

			} else {
				stmt.setNull(index++, Types.DOUBLE);
				stmt.setNull(index++, Types.INTEGER);
				stmt.setNull(index++, Types.INTEGER);
			}

			stmt.addBatch();
			puts.add(put);
			cellMeatimePuts.add(cellMeatimePut);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private NcellInfo matchNcell(CellForMatch cfm, String cell, String arfcn,
			String ncellBsic) {
		// ---邻区匹配----//
		// 邻区信息
		String ncell = "";
		String ncells = "";
		double dis = 1000000;
		// String bsic = ncc + bcc;
		String key = arfcn + "_" + ncellBsic;
		List<String> mayNcells = cfm.getCachedMatchNcell(cell + "_" + key);
		if (mayNcells != null) {
			// 已经匹配过了
			ncell = mayNcells.get(0);
			ncells = mayNcells.get(1);
			try {
				dis = Double.parseDouble(mayNcells.get(2));
			} catch (Exception e) {
			}
		} else {
			// 进行匹配
			CellLonLat cellInfo = cfm.getCellInfo(cell);
			if (cellInfo != null) {
				// 主小区存在
				List<CellLonLat> clls = cfm.getBcchBsicCells(key);
				if (clls != null && clls.size() > 0) {
					if (clls.size() == 1) {
						ncell = clls.get(0).getCell();
						// if (!ncell.equals(cell)) {
						// 算距离
						dis = LatLngHelper.Distance(cellInfo.getLon(), cellInfo
								.getLat(), clls.get(0).getLon(), clls.get(0)
								.getLat());
						dis = dis / 1000;
						ncells = ncell + "(" + clls.get(0).getName() + ",["
								+ dis + "km]";
						// }

					} else {
						// 计算所有的距离
						Map<Double, CellLonLat> disInfo = new TreeMap<Double, RnoStructAnaV2Impl.CellLonLat>();
						double lon = cellInfo.getLon();
						double lat = cellInfo.getLat();
						for (CellLonLat nc : clls) {
							dis = LatLngHelper.Distance(lon, lat, nc.getLon(),
									nc.getLat()) / 1000;
							disInfo.put(dis, nc);
							// System.out.println("disInfo:"+disInfo);
						}
						Set<Entry<Double, CellLonLat>> entry = disInfo
								.entrySet();
						boolean find = false;
						for (Iterator<Entry<Double, CellLonLat>> it = entry
								.iterator(); it.hasNext();) {
							Entry<Double, CellLonLat> entry2 = it.next();
							if (!find) {
								// if (!ncell.equals(cell)) {// 排除自己
								ncell = entry2.getValue().getCell();
								dis = entry2.getKey();
								find = true;
								// }
								// System.out.println("ncell:"+ncell+"------dis:"+dis);
							}
							ncells += entry2.getValue().getCell() + "("
									+ entry2.getValue().getName() + ",["
									+ entry2.getKey() + "]),";
							// num++;//不加永远是最大一个（升序）
							// System.out.println("ncells:"+ncells);
						}
						ncells = ncells.substring(0, ncells.length() - 1);
						// System.out.println("ncells:"+ncells);
					}

					// 设置缓存
					cfm.putCachedMatchNcell(cell + "_" + key,
							Arrays.asList(ncell, ncells, dis + ""));
				} else {
					// bcch与arfcn无组合
					ncell = "NotF_" + arfcn + "_" + ncellBsic;
					ncells = "";
				}
			} else {
				// 主小区不存在
				ncell = "notSure";
				ncells = "";
			}
		}

		return new NcellInfo(ncell, ncells, dis);

	}

	class NcellInfo {

		String ncell;
		String ncells;
		double dis;

		public String getNcell() {
			return ncell;
		}

		public void setNcell(String ncell) {
			this.ncell = ncell;
		}

		public String getNcells() {
			return ncells;
		}

		public void setNcells(String ncells) {
			this.ncells = ncells;
		}

		public double getDis() {
			return dis;
		}

		public void setDis(double dis) {
			this.dis = dis;
		}

		public NcellInfo(String ncell2, String ncells2, double dis2) {
			this.ncell = ncell2;
			this.ncells = ncells2;
			this.dis = dis2;
		}

		@Override
		public String toString() {
			return "NcellInfo [ncell=" + ncell + ", ncells=" + ncells
					+ ", dis=" + dis + "]";
		}

	}

	private static byte[] mergeByte(byte[] typeByte, byte[] lenByte,
			byte[] content) {
		int len1 = typeByte.length;
		int len2 = lenByte.length;
		int len3 = content.length;
		byte[] whole = new byte[len1 + len2 + len3];
		whole[0] = typeByte[0];
		whole[1] = lenByte[0];
		whole[2] = lenByte[1];
		int start = 3;
		for (int i = 0; i < len3; i++) {
			whole[start + i] = content[i];
		}
		return whole;
	}

	public NcsRecord handleSection(int type, byte[] wholeSection,
			ParserContext context) {
		NcsRecord rec = null;
		switch (type) {
		case 50:
			rec = parseAdministrator(wholeSection, context);
			break;
		case 51:
			rec = parseCellDataSection(wholeSection, context);
			break;
		case 52:
			rec = parseNcellData(wholeSection, context);
			break;
		default:
			// System.out.print("无法处理type=" + type + "的内容！");
			break;
		}

		return rec;
	}

	/**
	 * 解析管理字段
	 * 
	 * @param buf
	 */
	public NcsRecord parseAdministrator(byte[] buf, ParserContext context) {
		if (buf.length < AdministratorRecordLen) {
			return null;
		}
		NcsAdmRecord rec = context.getNcsAdmRecord();
		// StringBuilder builder = new StringBuilder();
		// builder.append("\r\n----------------administrator section -------------------\r\n");
		// 1 Record type
		int recType = TranslateTools.byte2Int(buf, 1);
		// System.out.println("1 recType=" + recTypes.get(recType));
		rec.setRecType(recType);
		// 2 Record length
		int recLen = TranslateTools.byte2Int(TranslateTools.subByte(buf, 1, 2),
				2);
		// System.out.println("2 recLen=" + recLen);
		rec.setLength(recLen);

		// 3 File format rev. number = 60
		int fileFormat = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 3, 1), 1);
		// System.out.println("3 fileFormat=" + fileFormat);
		rec.setFileFormat(fileFormat);

		// 4 Year Digit String 0-99
		String year = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 4, 1), 1) + "";
		if (year.length() == 1) {
			year = "0" + year;
		}
		// System.out.println("4 Year=" + year);
		rec.setYear(year);

		// 5 Month Digit String 01-12
		String month = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 5, 1), 1)
				+ "";
		if (month.length() == 1) {
			month = "0" + month;
		}
		// System.out.println("5 Month=" + month);
		rec.setMonth(month);

		// 6 Day Digit String 01-31
		String day = TranslateTools.byte2Int(TranslateTools.subByte(buf, 6, 1),
				1) + "";
		if (day.length() == 1) {
			day = "0" + day;
		}
		// System.out.println("6 day=" + day);
		rec.setDay(day);

		// 7 Hour
		String hour = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 7, 1), 1) + "";
		if (hour.length() == 1) {
			hour = "0" + hour;
		}
		// System.out.println("7 hour=" + hour);
		rec.setHour(hour);

		// 8 minute
		String minute = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 8, 1), 1)
				+ "";
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		// System.out.println("8 minute=" + minute);
		rec.setMinute(minute);

		// 9 second
		String second = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 9, 1), 1)
				+ "";
		if (second.length() == 1) {
			second = "0" + second;
		}
		// System.out.println("9 second=" + second);
		rec.setSecond(second);

		// 10 Record information
		int recInfo = TranslateTools.byte2Int(
				TranslateTools.subByte(buf, 10, 4), 4);
		// System.out.println("10 recInfo=" + recInfo);
		rec.setRecordInfo(recInfo);

		// 11 RID
		String rid = "";
		try {
			rid = new String(TranslateTools.subByte(buf, 14, 7), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// System.out.println("11 rid=" + rid);
		rec.setRid(rid);

		// 12 Start date for the recording Year
		String startDateYear = TranslateTools.makeIntFromByteArray(buf, 21, 1)
				+ "";
		if (startDateYear.length() == 1) {
			startDateYear = "0" + startDateYear;
		}
		// System.out.println("12 startDateYear=" + startDateYear);
		rec.setStartDateYear(startDateYear);

		// 13 Start date for the recording Month
		String startDateMonth = TranslateTools.makeIntFromByteArray(buf, 22, 1)
				+ "";
		if (startDateMonth.length() == 1) {
			startDateMonth = "0" + startDateMonth;
		}
		// System.out.println("13 startDateMonth=" + startDateMonth);
		rec.setStartMonth(startDateMonth);

		// 14 Start date for the recording Day
		String startDateDay = TranslateTools.makeIntFromByteArray(buf, 23, 1)
				+ "";
		if (startDateDay.length() == 1) {
			startDateDay = "0" + startDateDay;
		}
		// System.out.println("14 startDateDay=" + startDateDay);
		rec.setStartDay(startDateDay);

		// 15 Start time for the recording Hour
		String startDateHour = TranslateTools.makeIntFromByteArray(buf, 24, 1)
				+ "";
		if (startDateHour.length() == 1) {
			startDateHour = "0" + startDateHour;
		}
		// System.out.println("15 startDateHour=" + startDateHour);
		rec.setStartHour(startDateHour);

		// 16 Start time for the recording Minute
		String startDateMinute = TranslateTools
				.makeIntFromByteArray(buf, 25, 1) + "";
		if (startDateMinute.length() == 1) {
			startDateMinute = "0" + startDateMinute;
		}
		// System.out.println("16 startDateMinute=" + startDateMinute);
		rec.setStartMinute(startDateMinute);

		// 17 Start time for the recording Second
		String startDateSecond = TranslateTools
				.makeIntFromByteArray(buf, 26, 1) + "";
		if (startDateSecond.length() == 1) {
			startDateSecond = "0" + startDateSecond;
		}
		// System.out.println("17 startDateSecond=" + startDateSecond);
		rec.setStartSecond(startDateSecond);

		// 18 ABSS
		int abss = TranslateTools.makeIntFromByteArray(buf, 27, 1);
		// System.out.println("18 abss=" + abss);
		rec.setAbss(abss);

		// 19 RELSS sign
		int relssSign = TranslateTools.makeIntFromByteArray(buf, 28, 1);
		// System.out.println("19 relssSign=" + relssSign);
		rec.setRelssSign(relssSign);

		// 20 RELSS
		int relss = TranslateTools.makeIntFromByteArray(buf, 29, 1);
		// System.out.println("20 relss=" + relss);
		rec.setRelss(relss);

		// 21 RELSS2 sign
		int relss2Sign = TranslateTools.makeIntFromByteArray(buf, 30, 1);
		// System.out.println("21 relss2Sign=" + relss2Sign);
		rec.setRelss2Sign(relss2Sign);

		// 22 RELSS2
		int relss2 = TranslateTools.makeIntFromByteArray(buf, 31, 1);
		// System.out.println("22 relss2=" + relss2);
		rec.setRelss2(relss2);

		// 23 relss3 sign
		int relssSign3 = TranslateTools.makeIntFromByteArray(buf, 32, 1);
		// System.out.println("23 relssSign3=" + relssSign3);
		rec.setRelss3Sign(relssSign3);

		// 24 relss3
		int relss3 = TranslateTools.makeIntFromByteArray(buf, 33, 1);
		// System.out.println("24 relss3=" + relss3);
		rec.setRelss3(relss3);

		// 25 relss4 sign
		int relssSign4 = TranslateTools.makeIntFromByteArray(buf, 34, 1);
		// System.out.println("25 relssSign4=" + relssSign4);
		rec.setRelss4Sign(relssSign4);

		// 26 relss3
		int relss4 = TranslateTools.makeIntFromByteArray(buf, 35, 1);
		// System.out.println("26 relss4=" + relss4);
		rec.setRelss4(relss4);

		// 27 relss4 sign
		int relssSign5 = TranslateTools.makeIntFromByteArray(buf, 36, 1);
		// System.out.println("27 relssSign5=" + relssSign5);
		rec.setRelss5Sign(relssSign5);

		// 28 relss3
		int relss5 = TranslateTools.makeIntFromByteArray(buf, 37, 1);
		// System.out.println("28 relss5=" + relss5);
		rec.setRelss5(relss5);

		// 29 NCELLTYPE
		int ncellType = TranslateTools.makeIntFromByteArray(buf, 38, 1);
		// System.out.println("29 ncellType=" + ncellType);
		rec.setNcellType(ncellType);

		// NUMFREQ
		int numFreq = TranslateTools.makeIntFromByteArray(buf, 39, 1);
		// System.out.println("NUMFREQ =" + numFreq);
		rec.setNumFreq(numFreq);

		// SEGTIME
		int segTime = TranslateTools.makeIntFromByteArray(buf, 40, 2);
		// System.out.println("segTime =" + segTime);
		rec.setSegTime(segTime);

		// Termination reason
		int terminalReason = TranslateTools.makeIntFromByteArray(buf, 42, 1);
		// System.out.println("terminalReason =" + terminalReason);
		rec.setTermReason(terminalReason);

		// RECTIME
		int recTime = TranslateTools.makeIntFromByteArray(buf, 43, 2);
		// System.out.println("recTime =" + recTime);
		rec.setRecTime(recTime);

		// ECNOABSS
		int ecnoAbss = TranslateTools.makeIntFromByteArray(buf, 45, 1);
		// System.out.println("ecnoAbss =" + ecnoAbss);
		rec.setEcnoAbss(ecnoAbss);

		// NUCELLTYPE
		int nucellType = TranslateTools.makeIntFromByteArray(buf, 46, 1);
		// System.out.println("nucellType =" + nucellType);
		rec.setNUcellType(nucellType);

		// TFDDMRR
		int tfddMrr = TranslateTools.makeIntFromByteArray(buf, 47, 1);
		// System.out.println("tfddMrr =" + tfddMrr);
		rec.setTfddMrr(tfddMrr);

		// NUMUMFI
		int numUmfi = TranslateTools.makeIntFromByteArray(buf, 48, 1);
		// System.out.println("numUmfi =" + numUmfi);
		rec.setNumUmfi(numUmfi);

		// TNCCPERM validity indicator
		int tnccPermValIndicator = TranslateTools.makeIntFromByteArray(buf, 49,
				1);
		// System.out.println("tnccPermValIndicator =" + tnccPermValIndicator);
		rec.setTnccpermValIndi(tnccPermValIndicator);

		// TNCCPERM bitmap
		int tnccPermBitmap = TranslateTools.makeIntFromByteArray(buf, 50, 1);
		// System.out.println("tnccPermBitmap =" + tnccPermBitmap);
		rec.setTnccpermBitmap(tnccPermBitmap);

		// TMBCR
		int tmbcr = TranslateTools.makeIntFromByteArray(buf, 51, 1);
		// System.out.println("tmbcr =" + tmbcr);
		rec.setTmbcr(tmbcr);

		// builder.append(rec.toString());

		return rec;
	}

	/**
	 * 解析cell data部分的数据
	 * 
	 * @param buf
	 */
	public NcsRecord parseCellDataSection(byte[] buf, ParserContext context) {
		// StringBuilder builder = new StringBuilder();
		// builder.append("\r\n--------cell Data ---------\r\n");
		NcsCellData rec = context.getNcsCellData();
		// rectype
		int recType = TranslateTools.makeIntFromByteArray(buf, 0, 1);
		// builder.append("recType="+recType);
		rec.setRecType(recType);

		// record length
		int recLen = TranslateTools.makeIntFromByteArray(buf, 1, 2);
		// builder.append(",recLen="+recLen);
		rec.setLength(recLen);

		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		// cell name
		String cellName = new String(TranslateTools.subByte(buf, 3, 7));
		// builder.append(",cellName="+cellName);
		rec.setCellName(cellName);

		// chgr
		int chgr = TranslateTools.makeIntFromByteArray(buf, 11, 1);
		// builder.append(",chgr="+chgr);
		rec.setChgr(chgr);

		// rep total number of received measurement reports
		long rep = TranslateTools.makeLongFromByteArray(buf, 12, 4);
		// builder.append(",totalNumber="+rep);
		rec.setRep(rep);

		// rephr
		long repHr = TranslateTools.makeLongFromByteArray(buf, 16, 4);
		// builder.append(",repHr="+repHr);
		rec.setRepHr(repHr);

		// REPUNDEFGSM
		long repUndefinedGsm = TranslateTools.makeLongFromByteArray(buf, 20, 4);
		// builder.append(",repUndefinedGsm="+repUndefinedGsm);
		rec.setRepUndefGsm(repUndefinedGsm);

		// avss
		int avss = TranslateTools.makeIntFromByteArray(buf, 24, 1);
		// builder.append(",avss="+avss);
		rec.setAvss(avss);

		// builder.append(rec.toString());
		// 输出

		return rec;
	}

	public NcsRecord parseNcellData(byte[] buf, ParserContext context) {
		NcsNcellData rec = new NcsNcellData();// context.getNcsNcellData();
		// StringBuilder builder = new StringBuilder();
		// builder.append("\r\n--------- ncell data --------\r\n");

		int recType = TranslateTools.makeIntFromByteArray(buf, 0, 1);
		rec.setRecType(recType);

		int len = TranslateTools.makeIntFromByteArray(buf, 1, 2);
		rec.setLength(len);

		// 文档说是8位，但实际只有7位是有效的，最后一位是看不见的字符
		String cellName = new String(TranslateTools.subByte(buf, 3, 7));
		rec.setCellName(cellName);

		int chgr = TranslateTools.makeIntFromByteArray(buf, 11, 1);
		rec.setChgr(chgr);

		int ncellBsic = Integer.parseInt(Integer.toOctalString(TranslateTools
				.makeIntFromByteArray(buf, 12, 1)));
		rec.setNcellBsic(ncellBsic);

		int arfcn = TranslateTools.makeIntFromByteArray(buf, 13, 2);
		rec.setArfcn(arfcn);

		int defined = TranslateTools.makeIntFromByteArray(buf, 15, 1);
		rec.setDefinedAsNcell(defined);

		int recTimeArfcn = TranslateTools.makeIntFromByteArray(buf, 16, 2);
		rec.setRecTimeArfcn(recTimeArfcn);

		int repArfcn = TranslateTools.makeIntFromByteArray(buf, 18, 4);
		rec.setRepArfcn(repArfcn);

		int times = TranslateTools.makeIntFromByteArray(buf, 22, 4);
		rec.setTimes(times);

		int navss = TranslateTools.makeIntFromByteArray(buf, 26, 1);
		rec.setNavss(navss);

		int times1 = TranslateTools.makeIntFromByteArray(buf, 27, 4);
		rec.setTimes1(times1);

		int navss1 = TranslateTools.makeIntFromByteArray(buf, 31, 1);
		rec.setNavss1(navss1);

		int times2 = TranslateTools.makeIntFromByteArray(buf, 32, 4);
		rec.setTimes2(times2);

		int navss2 = TranslateTools.makeIntFromByteArray(buf, 36, 1);
		rec.setNavss2(navss2);

		int times3 = TranslateTools.makeIntFromByteArray(buf, 37, 4);
		rec.setTimes3(times3);

		int navss3 = TranslateTools.makeIntFromByteArray(buf, 41, 1);
		rec.setNavss3(navss3);

		int times4 = TranslateTools.makeIntFromByteArray(buf, 42, 4);
		rec.setTimes4(times4);

		int navss4 = TranslateTools.makeIntFromByteArray(buf, 46, 1);
		rec.setNavss4(navss4);

		int times5 = TranslateTools.makeIntFromByteArray(buf, 47, 4);
		rec.setTimes5(times5);

		int navss5 = TranslateTools.makeIntFromByteArray(buf, 51, 1);
		rec.setNavss5(navss5);

		int times6 = TranslateTools.makeIntFromByteArray(buf, 52, 4);
		rec.setTimes6(times6);

		int navss6 = TranslateTools.makeIntFromByteArray(buf, 56, 1);
		rec.setNavss6(navss6);

		int timesRelss = TranslateTools.makeIntFromByteArray(buf, 57, 4);
		rec.setTimesRelss(timesRelss);

		int timesRelss2 = TranslateTools.makeIntFromByteArray(buf, 61, 4);
		rec.setTimesRelss2(timesRelss2);

		int timesRelss3 = TranslateTools.makeIntFromByteArray(buf, 65, 4);
		rec.setTimesRelss3(timesRelss3);

		int timesRelss4 = TranslateTools.makeIntFromByteArray(buf, 69, 4);
		rec.setTimesRelss4(timesRelss4);

		int timesRelss5 = TranslateTools.makeIntFromByteArray(buf, 73, 4);
		rec.setTimesRelss5(timesRelss5);

		int timesAbss = TranslateTools.makeIntFromByteArray(buf, 77, 4);
		rec.setTimesAbss(timesAbss);

		int timesAlone = TranslateTools.makeIntFromByteArray(buf, 81, 4);
		rec.setTimesAlone(timesAlone);

		// builder.append(rec.toString());
		return rec;
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
