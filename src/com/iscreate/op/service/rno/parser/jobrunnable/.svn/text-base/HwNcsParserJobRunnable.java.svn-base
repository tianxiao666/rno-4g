package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.UnicodeUtil;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.LatLngHelper;

public class HwNcsParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(HwNcsParserJobRunnable.class);

	private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();

	public HwNcsParserJobRunnable() {
		super();
		super.setJobType("HUAWEINCSFILE");
	}

	// 构建insert语句
	private static String tempTable = "RNO_2G_HW_NCS_T";

	// private static String insertInoTempTableSql = "";

	// 插入数据库临时表的字段
	// private List<String> tempDbFields = Arrays.asList("MEA_TIME", "PERIOD",
	// "BSC_NAME", "CELL", "ARFCN", "BCC", "NCC", "AS360", "S389", "S368",
	// "S386", "S363", "S371", "AS362", "S387", "S372", "S360", "S394",
	// "S364", "S369", "S388", "S390", "S366", "S362", "S361", "S393",
	// "S365", "S392", "S391", "S367", "S370");
	// 数据库临时表每个字段可能对应的标题起始名称
	// private Map<String, DBFieldToTitle> dbFieldsToTitles = new
	// TreeMap<String, DBFieldToTitle>();

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

		// 获取job相关的信息
		String sql = "select * from RNO_DATA_COLLECT_REC where JOB_ID=" + jobId;
		List<Map<String, Object>> recs = RnoHelper.commonQuery(stmt, sql);
		RnoDataCollectRec dataRec = null;
		if (recs != null && recs.size() > 0) {
			dataRec = RnoHelper.commonInjection(RnoDataCollectRec.class,
					recs.get(0), new DateUtil());
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
				// File file1 = new File(FileInterpreter.makeFullPath(file
				// .getAbsolutePath()));
				// log.debug("file.getAbsolutePath()="
				// + FileInterpreter.makeFullPath(file.getAbsolutePath())
				// + ",存在？" + file1.exists());

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
					connection.commit();
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
					connection.commit();
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
				connection.commit();
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
			// clearResource(destDir, null);
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
			connection.commit();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		// 读取数据库与文本文件格式的配置
		// dbFieldsToTitles = readDbToTitleCfgFromProfile();

		String tmpFileName = fileName;
		int sucCnt = 0;
		boolean parseOk = false;
		ResultInfo result = rnoStructAnaV2.prepareEriCityCellData(stmt,
				"RNO_CELL_CITY_T", cityId);
		if (!result.isFlag()) {
			log.error("准备分析用的小区数据时，出错！");
			// clearResource(destDir, context);
			return failWithStatus("准备分析用的小区数据时，出错时出错", new Date(), this, stmt,
					report, status, dataRec.getDataCollectId(),
					DataParseStatus.Failall, DataParseProgress.Prepare);
		}
		int totalFileCnt = allNcsFiles.size();
		int i = 0;
		Map<String, DBFieldToTitle> dbFieldsToTitles = readDbToTitleCfgFromXml();
		for (File f : allNcsFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				parseOk = parseHwNcs(this, report, tmpFileName, f, connection,
						stmt, cityId, dbFieldsToTitles);
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
		// 清空分析表
		try {
			stmt.executeUpdate("truncate  table RNO_CELL_CITY_T");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (sucCnt > 0) {
			// status.setJobRunningStatus(JobRunningStatus.Succeded);
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
		} else {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}

		if (sucCnt == allNcsFiles.size()) {
			// 全部成功
			sql = "update rno_data_collect_rec set file_status='"
					+ DataParseStatus.Succeded.toString()
					+ "' where data_collect_id=" + dataRec.getDataCollectId();
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
		log.debug("更新结果状态 rno_data_collect_rec  sql:" + sql);
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
		rnoStructAnaV2.clearMatchCellContext();
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	private boolean parseHwNcs(JobRunnable jobWorker, JobReport report,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId, Map<String, DBFieldToTitle> dbFieldsToTitles) {

		// String spName = "everyStartHw" + System.currentTimeMillis();
		// Savepoint savepoint = null;
		// try {
		// savepoint = connection.setSavepoint(spName);
		// } catch (SQLException e2) {
		// e2.printStackTrace();
		// }
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
			// try {
			// connection.releaseSavepoint(savepoint);
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
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
		HTable ncsTable =null;
		HTable cellMeaTable =null;
		try {
			String[] sps = new String[1];
			do {
				line = reader.readLine();
				/*
				 * String testlin=
				 * "08/30/2014,MZA28,G五华棉洋镇桥江-2,49,53,7,7,32,100%,19.3077,73.4231,101,16,450,0,16,16,16,16,16,16,16,0,0,16,0,0,0,0,0,0,0,0"
				 * ; if (line.equals(testlin)) { log.debug("读取行line:"+line); }
				 */
				if (line == null) {
					sps = new String[] {};
					break;
				}
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			// 读取到标题头
			int fieldCnt = sps.length;
			log.debug("华为ncs文件：" + tmpFileName + ",title 为：" + line + ",有"
					+ fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> pois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			for (String sp : sps) {
				// log.debug("sp==" + sp);
				index++;
				find = false;
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
					msg += "[" + dt.dbField + "]";
				}
			}
			if (!StringUtils.isBlank(msg)) {
				log.error("检查华为ncs文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), "在文件中缺失以下标题:" + msg);
				addJobReport(report);
				// connection.releaseSavepoint(savepoint);
				return false;
			}
			// 拼装sql
			String insertInoTempTableSql = "insert into " + tempTable + " (";
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
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			insertInoTempTableSql += "CITY_ID,NCELL,NCELLS,DISTANCE,BSIC,CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION,NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING,same_freq_cnt,adj_freq_cnt,CELL_TO_NCELL_DIR";// 2014-9-15增加

			ws += cityId + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
			int ncellSqlIndex = index++;
			int ncellsSqlIndex = index++;
			int disSqlIndex = index++;
			int bsicIndex = index++;
			// CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION
			int cellbcchIndex = index++;
			int celltchIndex = index++;
			int cellfreqcntIndex = index++;
			int cellindoorIndex = index++;
			int celllonIndex = index++;
			int celllatIndex = index++;
			int cellbearingIndex = index++;
			int celldowntiltIndex = index++;
			int cellsiteIndex = index++;
			int cellfreqsectionIndex = index++;
			// NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION,NCELL_BEARING
			int ncelltchIndex = index++;
			int ncellfreqcntIndex = index++;
			int ncellindoorIndex = index++;
			int ncelllonIndex = index++;
			int ncelllatIndex = index++;
			int ncellsiteIndex = index++;
			int ncellfreqsectionIndex = index++;
			int ncellBearingIndex = index++;
			// same_freq_cnt,adj_freq_cnt,CELL_TO_NCELL_DIR
			int samefreqcntIndex = index++;
			int adjfreqcntIndex = index++;
			int celltoncelldirIndex = index++;
			// 此处也要增加相应的位置索引
			insertInoTempTableSql += ") values ( " + ws + " )";
			log.debug("华为ncs插入临时表的sql=" + insertInoTempTableSql);
			List<Put> ncsDataPuts = null;
			List<Put> cellMeatimePuts = null;
			try {
				insertTStmt = connection
						.prepareStatement(insertInoTempTableSql);
				// hbase ncs数据
				ncsDataPuts = new ArrayList<Put>();
				cellMeatimePuts = new ArrayList<Put>();
			} catch (Exception e) {
				msg = "准备华为的ncs插入的prepareStatement失败";
				log.error("准备华为的ncs插入的prepareStatement失败！sql="
						+ insertInoTempTableSql);
				e.printStackTrace();
				// connection.releaseSavepoint(savepoint);
				return false;
			}

			// 获取邻区匹配所需要信息
			CellForMatch cellForMatch = rnoStructAnaV2.getMatchCellContext(
					stmt, cityId);

			// 逐行读取数据
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			DateUtil dateUtil = new DateUtil();
			
			ncsTable = new HTable(
					HadoopXml.getHbaseConfig(),
					NcsIndex.HW_NCS_T.str);
			cellMeaTable = new HTable(
					HadoopXml.getHbaseConfig(),
					NcsIndex.LTE4G_CELL_MEATIME_T.str);
			ncsTable.setAutoFlushTo(false);
			cellMeaTable.setAutoFlushTo(false);
			do {
				line = reader.readLine();
				/*
				 * String testlin=
				 * "08/30/2014,MZA28,G五华棉洋镇桥江-2,49,53,7,7,32,100%,19.3077,73.4231,101,16,450,0,16,16,16,16,16,16,16,0,0,16,0,0,0,0,0,0,0,0"
				 * ; if (line.equals(testlin)) { log.debug("读取行line:"+line); }
				 */
				// log.debug("读取行line:"+line);
				if (line == null) {
					break;
				}
				sps = line.split(",|\t");
				if (sps.length != fieldCnt) {
					continue;
				}
				totalDataNum++;
				Map<String, Integer> fieldToIndex = new HashMap<String, Integer>();
				fieldToIndex.put("ncellSqlIndex", ncellSqlIndex);
				fieldToIndex.put("ncellsSqlIndex", ncellsSqlIndex);
				fieldToIndex.put("disSqlIndex", disSqlIndex);
				// bsic
				fieldToIndex.put("bsicIndex", bsicIndex);
				// 小区属性索引
				fieldToIndex.put("cellbcchIndex", cellbcchIndex);
				fieldToIndex.put("celltchIndex", celltchIndex);
				fieldToIndex.put("cellfreqcntIndex", cellfreqcntIndex);
				fieldToIndex.put("cellindoorIndex", cellindoorIndex);
				fieldToIndex.put("celllonIndex", celllonIndex);
				fieldToIndex.put("celllatIndex", celllatIndex);
				fieldToIndex.put("cellbearingIndex", cellbearingIndex);
				fieldToIndex.put("celldowntiltIndex", celldowntiltIndex);
				fieldToIndex.put("cellsiteIndex", cellsiteIndex);
				fieldToIndex.put("cellfreqsectionIndex", cellfreqsectionIndex);
				// 邻区属性索引
				fieldToIndex.put("ncelltchIndex", ncelltchIndex);
				fieldToIndex.put("ncellfreqcntIndex", ncellfreqcntIndex);
				fieldToIndex.put("ncellindoorIndex", ncellindoorIndex);
				fieldToIndex.put("ncelllonIndex", ncelllonIndex);
				fieldToIndex.put("ncelllatIndex", ncelllatIndex);
				fieldToIndex.put("ncellsiteIndex", ncellsiteIndex);
				fieldToIndex
						.put("ncellfreqsectionIndex", ncellfreqsectionIndex);
				fieldToIndex.put("ncellBearingIndex", ncellBearingIndex);
				// 更新同频数、邻频数、cell到ncell的夹角（正北方向为0）
				fieldToIndex.put("samefreqcntIndex", samefreqcntIndex);
				fieldToIndex.put("adjfreqcntIndex", adjfreqcntIndex);
				fieldToIndex.put("celltoncelldirIndex", celltoncelldirIndex);
				handleLineOk = handleHwNcsLine(sps, fieldCnt, pois,
						dbFieldsToTitles, insertTStmt, cellForMatch,
						fieldToIndex, dateUtil, ncsDataPuts, cellMeatimePuts,
						cityId);

				if (handleLineOk == true) {
					executeCnt++;
				}
				if (executeCnt > 5000) {
					// 每5000行执行一次
					try {
						insertTStmt.executeBatch();
						insertTStmt.clearBatch();

						
						ncsTable.put(ncsDataPuts);
						ncsDataPuts.clear();
						ncsTable.flushCommits();

						
						cellMeaTable.put(cellMeatimePuts);
						cellMeatimePuts.clear();
						cellMeaTable.flushCommits();

						executeCnt = 0;
					} catch (SQLException e) {
						e.printStackTrace();
						// connection.rollback(savepoint);
						// 清除数据，关闭资源，返回失败
						try {
							stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
						} catch (Exception e3) {
							e3.printStackTrace();
						}
						insertTStmt.close();
						connection.commit();
						return false;
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				insertTStmt.executeBatch();
				//当数量少的时候提交
				ncsTable.put(ncsDataPuts);
				ncsDataPuts.clear();
				ncsTable.flushCommits();
				
				cellMeaTable.put(cellMeatimePuts);
				cellMeatimePuts.clear();
				cellMeaTable.flushCommits();
			}
			
			// 查看一下表内的数据
			// String sql="select * from "+tempTable;
			// List<Map<String,Object>> tempdatas=RnoHelper.commonQuery(stmt,
			// sql);
			// if(tempdatas!=null&&tempdatas.size()>0){
			// for(Map<String,Object> one:tempdatas){
			// System.out.println("temptable data ="+one);
			// }
			// }else{
			// System.err.println("----temptable contains no data!");
			// }

			log.debug("华为ncs数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据");
			// ----一下进行数据处理----//
			String attMsg = "文件：" + tmpFileName;
			long t1, t2;
			Date d1 = new Date();

			ResultInfo resultInfo = null;
			// 邻区匹配,完善小区的频点等相关信息
			/*
			 * log.info(">>>>>>>>>>>>>>>开始邻区匹配..."); t1 =
			 * System.currentTimeMillis(); ResultInfo resultInfo =
			 * rnoStructAnaV2.matchHwNcsNcell(stmt, tempTable,
			 * "RNO_CELL_CITY_T", cityId); t2 = System.currentTimeMillis();
			 * log.info("<<<<<<<<<<<<<<<<完成邻区匹配，耗时：" + (t2 - t1) + "ms");
			 */

			// 查看一下表内的数据
			// sql="select * from "+tempTable;
			// tempdatas=RnoHelper.commonQuery(stmt, sql);
			// if(tempdatas!=null&&tempdatas.size()>0){
			// for(Map<String,Object> one:tempdatas){
			// System.out.println("邻区匹配后--temptable data ="+one);
			// }
			// }else{
			// System.err.println("邻区匹配后------temptable contains no data!");
			// }

			// --报告---//
			Date d2 = new Date();
			/*
			 * if (resultInfo.isFlag()) { report.setSystemFields("邻区匹配", d1, d2,
			 * DataParseStatus.Succeded.toString(), attMsg);
			 * addJobReport(report); } else { connection.rollback(savepoint);
			 * report.setSystemFields("邻区匹配", d1, d2,
			 * DataParseStatus.Failall.toString(), attMsg + "--" +
			 * resultInfo.getMsg()); addJobReport(report);
			 * 
			 * return false; }
			 */

			// 通过程序来判断bsc、频段，不需要用户填写
			log.info(">>>>>>开始自动计算ncs所测量的bsc、测量小区的频段");
			// setTokenInfo(token, "正在匹配ncs所测量的bsc、小区频段");
			t1 = System.currentTimeMillis();
			d1 = new Date();
			resultInfo = rnoStructAnaV2.matchHwNcsBscAndFreqSection(stmt,
					tempTable, "RNO_CELL_CITY_T");
			t2 = System.currentTimeMillis();
			log.info("<<<<<<<<<<<<<<<完成计算ncs所测量的bsc、测量小区的频段。耗时:" + (t2 - t1)
					+ "ms");

			// 查看一下表内的数据
			// sql="select * from "+tempTable;
			// tempdatas=RnoHelper.commonQuery(stmt, sql);
			// if(tempdatas!=null&&tempdatas.size()>0){
			// for(Map<String,Object> one:tempdatas){
			// System.out.println("匹配bsc后--temptable data ="+one);
			// }
			// }else{
			// System.err.println("匹配bsc后------temptable contains no data!");
			// }
			// --报告---//
			d2 = new Date();
			if (resultInfo.isFlag()) {
				report.setSystemFields("匹配BSC与频段信息", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				// connection.rollback(savepoint);
				report.setSystemFields("匹配BSC与频段信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());
				addJobReport(report);
				try {
					stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				insertTStmt.close();
				connection.commit();
				return false;
			}

			// 计算同频干扰系数、邻频干扰系数分子、分母
			// 同频干扰系数-12，邻频干扰系数+3
			d1 = new Date();
			resultInfo = rnoStructAnaV2
					.prepareHwCIAndCADivider(stmt, tempTable);
			d2 = new Date();
			if (resultInfo.isFlag()) {
				report.setSystemFields("计算同频干扰系数的分子、分母", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				report.setSystemFields("计算同频干扰系数的分子、分母", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());
				addJobReport(report);
				try {
					stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				insertTStmt.close();
				connection.commit();
				return false;
			}

			// 生成描述信息
			d1 = new Date();
			resultInfo = rnoStructAnaV2.generateHwNcsDescRec(connection, stmt,
					tempTable, cityId);
			d2 = new Date();
			if (resultInfo.isFlag()) {
				report.setSystemFields("生成华为描述信息", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				report.setSystemFields("生成华为描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());
				addJobReport(report);
				insertTStmt.close();
				connection.rollback();
				try {
					stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
				} catch (Exception e3) {
					e3.printStackTrace();
				}

				return false;
			}

			// 转移中间表数据到正式表
			d1 = new Date();
			// String fields =
			// "RNO_2GHWNCS_DESC_ID,MEA_TIME,CELL,ARFCN,BSIC,AS360,S389,S368,S386,S363,S371,AS362,S387,S372,S360,S394,S364,S369,S388,S390,S366,S362,S361,S393,S365,S392,S391,S367,S370,NCELL,NCELLS,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_BEARING,CELL_INDOOR,NCELL_INDOOR,CELL_DOWNTILT,CELL_SITE,NCELL_SITE,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,DISTANCE,CI_DIVIDER,CA_DIVIDER,CELL_TO_NCELL_DIR,CELL_BCCH,CELL_TCH,CITY_ID,S3013,NCELL_TCH,NCELL_BEARING";
			// String fields = readTargetTabNeedFieldCfgFromProfile();
			String fields = readTargetTabNeedFieldCfgFromXml();
			String translateSql = "insert into RNO_2G_HW_NCS(" + fields
					+ ") SELECT " + fields + " from " + tempTable;
			t1 = t2;
			log.info(">>>>>>>>>>>>>>>转移中间表数据到目标表的sql：" + translateSql);
			t1 = t2;
			try {
				long affcnt = stmt.executeUpdate(translateSql);
				log.debug("从临时表转移华为ncs数据的数量：" + affcnt);
			} catch (SQLException e) {
				e.printStackTrace();
				d2 = new Date();
				report.setFields("转移到存储空间", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());
				addJobReport(report);
				insertTStmt.close();
				connection.rollback();
				try {
					stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				return false;
			}

			//

			// --报告--//
			d2 = new Date();
			report.setSystemFields("转移到存储空间", d1, d2,
					DataParseStatus.Succeded.toString(), attMsg);
			addJobReport(report);

			t2 = System.currentTimeMillis();
			log.info("<<<<<<<<<<<<<<<完成转移中间表数据到目标表，耗时:" + (t2 - t1) + "ms");
			long et = System.currentTimeMillis();
			log.info("退出对爱立信NCS文件：" + tmpFileName + "的解析，耗时：" + (et - st)
					+ "ms");

			// 一个文件一个提交
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// try {
			// connection.rollback(savepoint);
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
			try {
				insertTStmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				stmt.executeUpdate("truncate table RNO_2G_HW_NCS_T");
			} catch (Exception e3) {
				e3.printStackTrace();
			}

			return false;
		} finally {
			try {
				ncsTable.close();
				cellMeaTable.close();
				
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
	 * @author brightming 2014-8-23 下午3:26:34
	 */
	private boolean handleHwNcsLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			PreparedStatement insertTempStatement, CellForMatch cfm,
			Map<String, Integer> fieldToIndex, DateUtil dateUtil,
			List<Put> ncsDataPuts, List<Put> cellMeatimePuts, long cityId) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String cell = "", bcc = "", ncc = "", arfcn = "", meaDate = "", s361 = "", s369 = "", s366 = "";

		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("CELL")) {
				cell = sps[i];
			} else if (dbField.equals("ARFCN")) {
				arfcn = sps[i];
			} else if (dbField.equals("NCC")) {
				ncc = sps[i];
			} else if (dbField.equals("BCC")) {
				bcc = sps[i];
			} else if (dbField.equals("MEA_TIME")) {
				meaDate = sps[i];
			} else if (dbField.equals("S361")) {
				s361 = sps[i];
			} else if (dbField.equals("S366")) {
				s366 = sps[i];
			} else if (dbField.equals("S369")) {
				s369 = sps[i];
			}
		}
		// CELL MEATIME ROWKEY
		Calendar ca = Calendar.getInstance();
		ca.setTime(dateUtil.parseDateArbitrary(meaDate));
		Put cellMeatimePut = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ "NCS" + "_" + String.valueOf(ca.get(Calendar.YEAR)) + "_"
				+ UnicodeUtil.enUnicode(cell)));
		cellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), String.valueOf(ca.getTime().getTime()).getBytes(),
				null);
		// hbase rowkey NCS
		Put put = new Put(
				Bytes.toBytes(String.valueOf(cityId)
						+ "_"
						+ String.valueOf(dateUtil.parseDateArbitrary(meaDate)
								.getTime()) + "_" + UnicodeUtil.enUnicode(cell)
						+ "_" + String.valueOf(ncc + bcc) + "_"
						+ String.valueOf(arfcn)));
		put.add("NCSINFO".getBytes(), "CI_DIVIDER".getBytes(),
				String.valueOf(Long.parseLong(s361) - Long.parseLong(s369))
						.getBytes());
		put.add("NCSINFO".getBytes(), "CA_DIVIDER".getBytes(),
				String.valueOf(Long.parseLong(s361) - Long.parseLong(s366))
						.getBytes());
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			/*
			 * if (dbField.equals("CELL")) { cell = sps[i]; } else if
			 * (dbField.equals("ARFCN")) { arfcn = sps[i]; } else if
			 * (dbField.equals("NCC")) { ncc = sps[i]; } else if
			 * (dbField.equals("BCC")) { bcc = sps[i]; }
			 */
			if (dt != null) {
				setIntoPreStmt(insertTempStatement, dt, sps[i], dateUtil,
						ncsDataPuts, put);
			}
		}

		// ---邻区匹配----//
		// 邻区信息
		String ncell = "";
		String ncells = "";
		double dis = 1000000;
		String bsic = ncc + bcc;
		String key = arfcn + "_" + bsic;
		List<String> mayNcells = cfm.getCachedMatchNcell(cell + "_" + key);

		// @author chao.xj 2014-9-16 下午2:51:48 增加
		// 小区属性
		// BSIC(OK),BCCH,TCH,CELL_FREQ_CNT,INDOOR_CELL_TYPE,LONGITUDE,LATITUDE,BEARING,DOWNTILT,SITE,CELL_FREQ_SECTION
		// 对应CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION
		int cellBcch = 0;
		String cellTch = "";
		int cellFreqCnt = 0;
		String indoorCellType = "";
		double cellLon = 0;
		double cellLat = 0;
		double cellBearing = 0;
		double cellDowntilt = 0;
		String cellSite = "";
		String cellFreqSection = "";
		// 邻区属性
		// TCH,CELL_FREQ_CNT,INDOOR_CELL_TYPE,LONGITUDE,LATITUDE,SITE,CELL_FREQ_SECTION
		// 对应：NCELL_TCH,NCELL_FREQ_CNT,NCELL_INDOOR,NCELL_LON,NCELL_LAT,NCELL_SITE,NCELL_FREQ_SECTION
		String ncellTch = "";
		int ncellFreqCnt = 0;
		String indoorNcellType = "";
		double ncellLon = 0;
		double ncellLat = 0;
		String ncellSite = "";
		String ncellFreqSection = "";
		double ncellBearing = 0;

		// 同频数、邻频数、cell到ncell的夹角（正北方向为0）
		// same_freq_cnt,adj_freq_cnt,CELL_TO_NCELL_DIR
		int sameFreqCnt = 0;
		int adjFreqCnt = 0;
		double cellToNcellDir = 0;
		// 缓存属性
		String cellAttr = "";
		String ncellAttr = "";
		String coadjfreqAttr = "";

		CellLonLat cellInfo = null;
		if (mayNcells != null) {
			// 已经匹配过了
			ncell = mayNcells.get(0);
			ncells = mayNcells.get(1);
			try {
				dis = Double.parseDouble(mayNcells.get(2));
			} catch (Exception e) {
			}
			// 小区属性值
			cellBcch = Integer.parseInt(mayNcells.get(3));
			cellTch = mayNcells.get(4);
			// cellFreqCnt=cellInfo.getTch().split(",").length;
			cellFreqCnt = Integer.parseInt(mayNcells.get(5));
			indoorCellType = mayNcells.get(6);
			cellLon = Double.parseDouble(mayNcells.get(7));
			cellLat = Double.parseDouble(mayNcells.get(8));
			cellBearing = Double.parseDouble(mayNcells.get(9));
			cellDowntilt = Double.parseDouble(mayNcells.get(10));
			cellSite = mayNcells.get(11);
			cellFreqSection = mayNcells.get(12);
			// 邻区属性值
			// CellLonLat ncellInfo = cfm.getCellInfo(ncell);
			ncellTch = mayNcells.get(13);
			// ncellFreqCnt=ncellInfo.getTch().split(",").length;
			ncellFreqCnt = Integer.parseInt(mayNcells.get(14));
			indoorNcellType = mayNcells.get(15);
			ncellLon = Double.parseDouble(mayNcells.get(16));
			ncellLat = Double.parseDouble(mayNcells.get(17));
			ncellSite = mayNcells.get(18);
			ncellFreqSection = mayNcells.get(19);
			ncellBearing = Double.parseDouble(mayNcells.get(23));

			// 更新同频数、邻频数、cell到ncell的夹角（正北方向为0）
			sameFreqCnt = Integer.parseInt(mayNcells.get(20));
			adjFreqCnt = Integer.parseInt(mayNcells.get(21));
			cellToNcellDir = Double.parseDouble(mayNcells.get(22));
		} else {
			// 进行匹配
			// cellInfo = cfm.getCellInfo(cell);
			cellInfo = cfm.getCells().get(cell);
			if (cellInfo != null) {
				// 主小区存在
				List<CellLonLat> clls = cfm.getBcchBsicCells(key);
				if (clls != null && clls.size() > 0) {
					if (clls.size() == 1) {
						ncell = clls.get(0).getCell();
						// 算距离
						dis = LatLngHelper.Distance(cellInfo.getLon(), cellInfo
								.getLat(), clls.get(0).getLon(), clls.get(0)
								.getLat());
						dis = dis / 1000;
						ncells = ncell + "(" + clls.get(0).getName() + ",["
								+ dis + "km]";
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
						int num = 0;
						for (Iterator<Entry<Double, CellLonLat>> it = entry
								.iterator(); it.hasNext();) {
							Entry<Double, CellLonLat> entry2 = it.next();
							if (num == 0) {
								ncell = entry2.getValue().getCell();
								dis = entry2.getKey();
								// System.out.println("ncell:"+ncell+"------dis:"+dis);
							}
							ncells += entry2.getValue().getCell() + "("
									+ entry2.getValue().getName() + ",["
									+ entry2.getKey() + "]),";
							num++;// 不加永远是最大一个（升序）
							// System.out.println("ncells:"+ncells);
						}
						ncells = ncells.substring(0, ncells.length() - 1);
						// System.out.println("ncells:"+ncells);

					}
					// 小区属性值
					cellBcch = cellInfo.getBcch();
					cellTch = cellInfo.getTch();
					// cellFreqCnt=cellInfo.getTch().split(",").length;
					cellFreqCnt = cellInfo.getTotalFreqCnt();
					indoorCellType = cellInfo.getIndoor();
					cellLon = cellInfo.getLon();
					cellLat = cellInfo.getLat();
					cellBearing = cellInfo.getBearing();
					cellDowntilt = cellInfo.getDowntilt();
					cellSite = cellInfo.getSite();
					cellFreqSection = cellInfo.getFreqSection();

					// 邻区属性值
					// CellLonLat ncellInfo = cfm.getCellInfo(ncell);
					CellLonLat ncellInfo = cfm.getCells().get(ncell);
					ncellTch = ncellInfo.getTch();
					// ncellFreqCnt=ncellInfo.getTch().split(",").length;
					ncellFreqCnt = ncellInfo.getTotalFreqCnt();
					indoorNcellType = ncellInfo.getIndoor();
					ncellLon = ncellInfo.getLon();
					ncellLat = ncellInfo.getLat();
					ncellSite = ncellInfo.getSite();
					ncellFreqSection = ncellInfo.getFreqSection();
					ncellBearing = ncellInfo.getBearing();
					// 更新同频数、邻频数、cell到ncell的夹角（正北方向为0）
					sameFreqCnt = cellInfo.getCoFreqCnt(ncellInfo);
					adjFreqCnt = ncellInfo.getAdjFreqCnt(cellInfo);
					cellToNcellDir = cellInfo.getCellToAnoterCellDir(ncellInfo);

				} else {
					// bcch与arfcn无组合
					ncell = "NotF_" + arfcn + "_" + bsic;
					ncells = "";
					// 小区属性值
					cellBcch = cellInfo.getBcch();
					cellTch = cellInfo.getTch();
					// cellFreqCnt=cellInfo.getTch().split(",").length;
					cellFreqCnt = cellInfo.getTotalFreqCnt();
					indoorCellType = cellInfo.getIndoor();
					cellLon = cellInfo.getLon();
					cellLat = cellInfo.getLat();
					cellBearing = cellInfo.getBearing();
					cellDowntilt = cellInfo.getDowntilt();
					cellSite = cellInfo.getSite();
					cellFreqSection = cellInfo.getFreqSection();

				}
			} else {
				// 主小区不存在
				ncell = "notSure";
				ncells = "";
			}
		}
		// 设置缓存
		/*
		 * cellAttr=cellBcch + ", "+cellTch+","+ cellFreqCnt + ","+
		 * indoorCellType+","+ cellLon + "," +cellLat + "," +cellBearing + ","+
		 * cellDowntilt + "," +cellSite+","+ cellFreqSection;
		 * ncellAttr=ncellTch+
		 * ","+ncellFreqCnt+","+indoorNcellType+","+ncellLon+","
		 * +ncellLat+","+ncellSite+","+ncellFreqSection;
		 * coadjfreqAttr=sameFreqCnt+","+adjFreqCnt+","+cellToNcellDir;
		 */
		cfm.putCachedMatchNcell(cell + "_" + key, Arrays.asList(ncell, ncells,
				dis + "", cellBcch + "", cellTch, cellFreqCnt + "",
				indoorCellType + "", cellLon + "", cellLat + "", cellBearing
						+ "", cellDowntilt + "", cellSite, cellFreqSection,
				ncellTch, ncellFreqCnt + "", indoorNcellType, ncellLon + "",
				ncellLat + "", ncellSite, ncellFreqSection, sameFreqCnt + "",
				adjFreqCnt + "", cellToNcellDir + "", ncellBearing + ""));
		// 设置邻区信息
		try {
			// NCELL
			insertTempStatement.setString(fieldToIndex.get("ncellSqlIndex"),
					ncell);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL".getBytes(),
					ncell.getBytes("utf-8"));
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}
		try {
			// NCELLS
			insertTempStatement.setString(fieldToIndex.get("ncellsSqlIndex"),
					ncells);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELLS".getBytes(),
					ncells.getBytes("utf-8"));
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}
		try {
			// DISTANCE
			insertTempStatement.setDouble(fieldToIndex.get("disSqlIndex"), dis);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "DISTANCE".getBytes(),
					String.valueOf(dis).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 增加bsic
		try {
			// BSIC
			insertTempStatement.setString(fieldToIndex.get("bsicIndex"), bsic);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "BSIC".getBytes(), String
					.valueOf(bsic).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 增加小区属性
		try {
			// CELL_BCCH
			insertTempStatement.setInt(fieldToIndex.get("cellbcchIndex"),
					cellBcch);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_BCCH".getBytes(),
					String.valueOf(cellBcch).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_TCH
			insertTempStatement.setString(fieldToIndex.get("celltchIndex"),
					cellTch);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_TCH".getBytes(),
					String.valueOf(cellTch).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_FREQ_CNT
			insertTempStatement.setInt(fieldToIndex.get("cellfreqcntIndex"),
					cellFreqCnt);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_FREQ_CNT".getBytes(),
					String.valueOf(cellFreqCnt).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_INDOOR
			insertTempStatement.setString(fieldToIndex.get("cellindoorIndex"),
					indoorCellType);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_INDOOR".getBytes(),
					String.valueOf(indoorCellType).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_LON
			insertTempStatement.setDouble(fieldToIndex.get("celllonIndex"),
					cellLon);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_LON".getBytes(),
					String.valueOf(cellLon).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_LAT
			insertTempStatement.setDouble(fieldToIndex.get("celllatIndex"),
					cellLat);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_LAT".getBytes(),
					String.valueOf(cellLat).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_BEARING
			insertTempStatement.setDouble(fieldToIndex.get("cellbearingIndex"),
					cellBearing);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_BEARING".getBytes(),
					String.valueOf(cellBearing).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_DOWNTILT
			insertTempStatement.setDouble(
					fieldToIndex.get("celldowntiltIndex"), cellDowntilt);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_DOWNTILT".getBytes(),
					String.valueOf(cellDowntilt).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_SITE
			insertTempStatement.setString(fieldToIndex.get("cellsiteIndex"),
					cellSite);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "CELL_SITE".getBytes(),
					String.valueOf(cellSite).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_FREQ_SECTION
			insertTempStatement.setString(
					fieldToIndex.get("cellfreqsectionIndex"), cellFreqSection);
			put.add(NcsIndex.NCS_CF.str.getBytes(),
					"CELL_FREQ_SECTION".getBytes(),
					String.valueOf(cellFreqSection).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 增加邻区属性
		try {
			// NCELL_TCH
			insertTempStatement.setString(fieldToIndex.get("ncelltchIndex"),
					ncellTch);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_TCH".getBytes(),
					String.valueOf(ncellTch).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_FREQ_CNT
			insertTempStatement.setInt(fieldToIndex.get("ncellfreqcntIndex"),
					ncellFreqCnt);
			put.add(NcsIndex.NCS_CF.str.getBytes(),
					"NCELL_FREQ_CNT".getBytes(), String.valueOf(ncellFreqCnt)
							.getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_INDOOR
			insertTempStatement.setString(fieldToIndex.get("ncellindoorIndex"),
					indoorNcellType);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_INDOOR".getBytes(),
					String.valueOf(indoorNcellType).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_LON
			insertTempStatement.setDouble(fieldToIndex.get("ncelllonIndex"),
					ncellLon);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_LON".getBytes(),
					String.valueOf(ncellLon).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_LAT
			insertTempStatement.setDouble(fieldToIndex.get("ncelllatIndex"),
					ncellLat);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_LAT".getBytes(),
					String.valueOf(ncellLat).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_SITE
			insertTempStatement.setString(fieldToIndex.get("ncellsiteIndex"),
					ncellSite);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_SITE".getBytes(),
					String.valueOf(ncellSite).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_FREQ_SECTION
			insertTempStatement
					.setString(fieldToIndex.get("ncellfreqsectionIndex"),
							ncellFreqSection);
			put.add(NcsIndex.NCS_CF.str.getBytes(),
					"NCELL_FREQ_SECTION".getBytes(),
					String.valueOf(ncellFreqSection).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// NCELL_BEARING
			insertTempStatement.setDouble(
					fieldToIndex.get("ncellBearingIndex"), ncellBearing);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "NCELL_BEARING".getBytes(),
					String.valueOf(ncellBearing).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 更新同频数、邻频数、cell到ncell的夹角（正北方向为0）
		try {
			// SAME_FREQ_CNT
			insertTempStatement.setInt(fieldToIndex.get("samefreqcntIndex"),
					sameFreqCnt);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "SAME_FREQ_CNT".getBytes(),
					String.valueOf(sameFreqCnt).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// ADJ_FREQ_CNT
			insertTempStatement.setDouble(fieldToIndex.get("adjfreqcntIndex"),
					adjFreqCnt);
			put.add(NcsIndex.NCS_CF.str.getBytes(), "ADJ_FREQ_CNT".getBytes(),
					String.valueOf(adjFreqCnt).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// CELL_TO_NCELL_DIR
			insertTempStatement.setDouble(
					fieldToIndex.get("celltoncelldirIndex"), cellToNcellDir);
			put.add(NcsIndex.NCS_CF.str.getBytes(),
					"CELL_TO_NCELL_DIR".getBytes(),
					String.valueOf(cellToNcellDir).getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			insertTempStatement.addBatch();
			ncsDataPuts.add(put);
			cellMeatimePuts.add(cellMeatimePut);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private void setIntoPreStmt(PreparedStatement pstmt, DBFieldToTitle dt,
			String val, DateUtil dateUtil, List<Put> puts, Put put) {
		String type = dt.getDbType();
		int index = dt.sqlIndex;
		if (index < 0) {
			log.error(dt + "在sql插入语句中，没有相应的位置！");
			return;
		}
		if (StringUtils.equalsIgnoreCase("Long", type)) {
			if (!StringUtils.isBlank(val) && StringUtils.isNumeric(val)) {
				try {
					pstmt.setLong(index, Long.parseLong(val));
					put.add(NcsIndex.NCS_CF.str.getBytes(), dt.getDbField()
							.getBytes(), val.getBytes());
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
					Date date = dateUtil.parseDateArbitrary(val);
					if (date != null) {
						pstmt.setTimestamp(index,
								new java.sql.Timestamp(date.getTime()));
						put.add(NcsIndex.NCS_CF.str.getBytes(), dt.getDbField()
								.getBytes(), String.valueOf(date.getTime())
								.getBytes());
					} else {
						pstmt.setNull(index, java.sql.Types.DATE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error("hwncs parse date fail:date str=" + val);
				}
			} else {
				try {
					pstmt.setNull(index, java.sql.Types.DATE);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} else if (StringUtils.equalsIgnoreCase("String", type)) {
			// log.debug("val:"+val);
			if (!StringUtils.isBlank(val)) {
				try {
					pstmt.setString(index, val);
					put.add(NcsIndex.NCS_CF.str.getBytes(), dt.getDbField()
							.getBytes(), val.getBytes("utf-8"));
				} catch (SQLException e) {
					e.printStackTrace();
				}catch (UnsupportedEncodingException e1) {
					// TODO: handle exception
					e1.printStackTrace();
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
					put.add(NcsIndex.NCS_CF.str.getBytes(), dt.getDbField()
							.getBytes(), val.getBytes());
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
			if (!StringUtils.isBlank(val) && StringUtils.isNumeric(val)) {
				try {
					pstmt.setInt(index, Integer.parseInt(val));
					put.add(NcsIndex.NCS_CF.str.getBytes(), dt.getDbField()
							.getBytes(), val.getBytes());
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
		}else if (StringUtils.startsWithIgnoreCase("double", type)) {
			if (!StringUtils.isBlank(val) && StringUtils.isNumeric(val)) {
				try {
					pstmt.setDouble(index, Double.parseDouble(val));
				} catch (NumberFormatException e) {
				} catch (SQLException e) {
				}
			} else {
				try {
					pstmt.setNull(index, Types.DOUBLE);
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

	private static Map<String, DBFieldToTitle> readDbToTitleCfgFromProfile() {
		InputStream is = null;
		Properties prop = null;
		try {
			// 需要实时读取
			is = new BufferedInputStream(new FileInputStream(
					HwNcsParserJobRunnable.class.getResource(
							"hwNcsDbToTitles.properties").getPath()));
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

	public static class DBFieldToTitle {
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

	/**
	 * 
	 * @title 从属性配置 文件中读取hw ncs 系统表所需字段
	 * @return
	 * @author chao.xj
	 * @date 2014-9-11下午5:00:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	private static String readTargetTabNeedFieldCfgFromProfile() {
		InputStream is = null;
		Properties prop = null;
		StringBuffer field = new StringBuffer();
		try {
			// 需要实时读取
			is = new BufferedInputStream(new FileInputStream(
					HwNcsParserJobRunnable.class.getResource(
							"hwNcsTargetTabField.properties").getPath()));
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
		for (Object key : prop.keySet()) {
			field.append(prop.getProperty(key.toString()).trim() + ",");
		}
		return field.substring(0, field.length() - 1).toString();
	}

	/**
	 * 
	 * @title 从xml配置 文件中读取hw ncs 数据库到excel的映射关系
	 * @return
	 * @author chao.xj
	 * @date 2014-9-26下午6:47:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, DBFieldToTitle> readDbToTitleCfgFromXml() {
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in = new FileInputStream(new File(
					HwNcsParserJobRunnable.class.getResource(
							"hwNcsDbToTitles.xml").getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
				Element e = (Element) o;
				DBFieldToTitle dt = new DBFieldToTitle();
				for (Object obj : e.elements()) {
					Element e1 = (Element) obj;
					String key = e1.getName();
					String val = e1.getTextTrim();
					if ("name".equals(key)) {
						dt.setDbField(val);
					}
					if ("type".equals(key)) {
						dt.setDbType(val);
					}
					if ("essential".equals(key)) {
						if (StringUtils.equals(val, "1")) {
							// mandaroty
							dt.setMandatory(true);
						} else {
							dt.setMandatory(false);
						}
					}
					if ("match".equals(key)) {
						dt.setMatchType(Integer.parseInt(val));
					}
					if ("exceltitle".equals(key)) {
						String[] v = val.split(",");
						for (String vo : v) {
							dt.addTitle(vo);
						}
					}

				}
				dbCfgs.put(dt.dbField, dt);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dbCfgs;
	}

	/**
	 * 
	 * @title 从xml配置 文件中读取hw ncs 系统表所需字段
	 * @return
	 * @author chao.xj
	 * @date 2014-9-26下午6:59:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static String readTargetTabNeedFieldCfgFromXml() {
		StringBuffer field = new StringBuffer();
		try {
			InputStream in = new FileInputStream(new File(
					HwNcsParserJobRunnable.class.getResource(
							"hwNcsTargetTabField.xml").getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
				Element e = (Element) o;
				DBFieldToTitle dt = new DBFieldToTitle();
				for (Object obj : e.elements()) {
					Element e1 = (Element) obj;
					String key = e1.getName();
					String val = e1.getTextTrim();
					if ("name".equals(key)) {
						field.append(val + ",");

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return field.substring(0, field.length() - 1).toString();
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

	public static void main(String[] args) {

		/*
		 * Map<String, DBFieldToTitle> map = readDbToTitleCfgFromProfile();
		 * log.debug("map.isEmpty():" + map.isEmpty());
		 * System.out.println("map.isEmpty():" + map.isEmpty()); for (String key
		 * : map.keySet()) { System.out.println( key + "-----" + map.get(key));
		 * }
		 */

		/*
		 * Map<String, DBFieldToTitle> map = readDbToTitleCfgFromXml(); //
		 * log.debug("map.isEmpty():" + map.isEmpty()); //
		 * System.out.println("map.isEmpty():" + map.isEmpty()); for (String key
		 * : map.keySet()) { System.out.println( key + "-----" + map.get(key));
		 * }
		 */
		// String aa=readTargetTabNeedFieldCfgFromProfile();
		// System.out.println(aa);
		String aa = readTargetTabNeedFieldCfgFromXml();
		System.out.println(aa);
		/*
		 * String str = "华为NCS,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,re"; String[]
		 * sps = str.split(","); System.out.println("---sps.len=" + sps.length);
		 */
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
