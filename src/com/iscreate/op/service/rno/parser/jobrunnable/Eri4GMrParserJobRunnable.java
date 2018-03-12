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
import java.sql.Savepoint;
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

import com.iscreate.op.dao.rno.RnoMrAdjCellMatch;
import com.iscreate.op.dao.rno.RnoMrAdjCellMatchImpl;
import com.iscreate.op.dao.rno.RnoMrAdjCellMatchImpl.CellLonLat;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.dao.rno.RnoMrAdjCellMatchImpl.CellForMatch;
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
import com.iscreate.op.service.rno.parser.jobrunnable.Zte4GMrParserJobRunnable.DBFieldToTitle;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.UnicodeUtil;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.op.service.rno.tool.DateUtil.DateFmt;
import com.iscreate.plat.tools.LatLngHelper;

public class Eri4GMrParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(Eri4GMrParserJobRunnable.class);
	private RnoMrAdjCellMatch rnoMrAdjCellMatch=new RnoMrAdjCellMatchImpl();
	private StringBuffer cellBuffer=new StringBuffer();
	public Eri4GMrParserJobRunnable() {
		super();
		super.setJobType("ERI4GMRFILE");
	}
	//表及相关语句
	private static String insertTempTableSql = "";
	private static String LteCellTempTable="RNO_LTE_IMPORT_MID";
	private static String LteCellTable="RNO_LTE_CELL";
	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {

		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

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
		List<File> allMrFiles = new ArrayList<File>();// 将所有待处理的MR文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2,cacheDate1;
		cacheDate1 = date1;//缓存第一次进入runable的时间
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的MR文件是一个压缩包。");

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
					allMrFiles.add(f);
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
			log.info("上传的MR是一个普通文件。");
			allMrFiles.add(file);
		}

		if (allMrFiles.isEmpty()) {
			msg = "未上传有效的mr文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的MR文件！注意zip包里不能再包含有文件夹！");
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
		boolean preParseOk = false;

		int totalFileCnt = allMrFiles.size();
		int i = 0;
		//是否还需要验证标题头
		Map<String, DBFieldToTitle> dbFieldsToTitles = readDbToTitleCfgFromXml();
		for (File f : allMrFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				//首先对文件进行预处理：插入昨时表，更新该区域的lte小区的频点及PCI信息
				date1 = new Date();
				preParseOk = preParseMr(this, report, tmpFileName, f, connection,
						stmt, cityId, dbFieldsToTitles);
				date2 = new Date();
				report.setStage("文件预处理总结");
				report.setReportType(1);
				report.setBegTime(date1);
				report.setEndTime(date2);
				if (preParseOk) {
					report.setFinishState(DataParseStatus.Succeded.toString());
					report.setAttMsg("成功完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
				} else {
					report.setFinishState(DataParseStatus.Failall.toString());
					report.setAttMsg("失败完成文件（" + i + "/" + totalFileCnt + "）:"
							+ tmpFileName);
				}
				addJobReport(report);

				if(preParseOk){
					//再次对文件进行处理：在更新该区域的lte小区的频点及PCI信息基础上进行邻区匹配及数据入库工作
					date1 = new Date();
					parseOk = parseMr(this, report, tmpFileName, f, connection,
							stmt, cityId, dbFieldsToTitles);
					i++;
					date2 = new Date();
					report.setStage("文件处理总结");
					report.setReportType(1);
					report.setBegTime(date1);
					report.setEndTime(date2);
					if (parseOk) {
						report.setFinishState(DataParseStatus.Succeded.toString());
						report.setAttMsg("成功完成文件（" + i + "/" + totalFileCnt + "）:"
								+ tmpFileName);
						sucCnt++;
					} else {
						report.setFinishState(DataParseStatus.Failall.toString());
						report.setAttMsg("失败完成文件（" + i + "/" + totalFileCnt + "）:"
								+ tmpFileName);
					}
					addJobReport(report);
				}
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

		if (sucCnt > 0) {
			// status.setJobRunningStatus(JobRunningStatus.Succeded);
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
		} else {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}

		if (sucCnt == allMrFiles.size()) {
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
		if(cellBuffer.length()>0){
			date2 = new Date();
			report.setStage("小区匹配汇总");
			report.setReportType(1);

			report.setBegTime(cacheDate1);
			report.setEndTime(date2);
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setAttMsg("在工参中无法匹配到的小区（数量 /" + cellBuffer.substring(0, cellBuffer.length()-1).split(",").length + "）:"
										+ cellBuffer.substring(0, cellBuffer.length()-1));
			addJobReport(report);
			cellBuffer.setLength(0);//清空
		}

		//清除邻区匹配数据
		rnoMrAdjCellMatch.clearMatchCellContext();
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
	private boolean preParseMr(JobRunnable jobWorker, JobReport report,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId, Map<String, DBFieldToTitle> dbFieldsToTitles) {

		PreparedStatement insertTStmt = null;
		long st = System.currentTimeMillis();
		String spName = "everyStartEri" + System.currentTimeMillis();
		Savepoint savepoint = null;
		try {
			savepoint = connection.setSavepoint(spName);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
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
		try {
			String[] sps = new String[1];
			do {
				line = reader.readLine();

			/*	 String testlin=
				  "08/30/2014,MZA28,G五华棉洋镇桥江-2,49,53,7,7,32,100%,19.3077,73.4231,101,16,450,0,16,16,16,16,16,16,16,0,0,16,0,0,0,0,0,0,0,0"
				  ; if (line.equals(testlin)) { log.debug("读取行line:"+line); }*/

				if (line == null) {
					sps = new String[] {};
					break;
				}
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			// 读取到标题头
			int fieldCnt = sps.length;
			log.debug("爱立信MR文件：" + tmpFileName + ",title 为：" + line + ",有"
					+ fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> pois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			for (String sp : sps) {
				// log.debug("sp==" + sp);
//				sp=sp.toUpperCase();//转换成大写
				index++;
				find = false;
				for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
//					 log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						if (dt.matchType == 1) {
							// find;
							// log.debug("--match type=1.dtf=" + dtf
							// + ",sp=" + sp);
							if (StringUtils.equals(dtf, sp)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
//								System.out.println("sp==" + sp+"---dtf=="+dtf);
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
//								System.out.println("sp==" + sp+"---dtf=="+dtf);
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
				log.error("检查MR文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), "在文件中缺失以下标题:" + msg);
				addJobReport(report);
				// connection.releaseSavepoint(savepoint);
				return false;
			}

			// 逐行读取数据
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			DateUtil dateUtil = new DateUtil();

			// 拼装sql
			insertTempTableSql = "insert into " + LteCellTempTable + " (business_cell_id,pci,EARFCN,BUSINESS_ENODEB_ID) values(?,?,?,?)";
			log.debug("insertTempTableSql:"+insertTempTableSql);

			try {
				insertTStmt = connection
						.prepareStatement(insertTempTableSql);
			} catch (Exception e) {
				msg = "准备爱立信MR插入的prepareStatement失败";
				log.error("准备爱立信MR插入的prepareStatement失败！sql="
						+ insertTempTableSql);
				e.printStackTrace();
				connection.releaseSavepoint(savepoint);
				return false;
			}
			//缓存excel表中已经出现过的小区ID
			Map<String, String> cacheExcelCellId = new HashMap<String, String>();
			do {
				line = reader.readLine();

				if (line == null) {
					break;
				}
				sps = line.split(",|\t");
//				System.out.println(sps.length+"：左实际长度----右标题长度："+fieldCnt);
				if (sps.length != fieldCnt) {
					continue;
				}
				totalDataNum++;

				handleLineOk = preHandleMrLine(sps, fieldCnt, pois,
						dbFieldsToTitles, dateUtil,
						cityId,cacheExcelCellId,insertTStmt);

				if (handleLineOk == true) {
					executeCnt++;
				}
				if (executeCnt > 1000) {
					// 每10000行执行一次
					try {
						insertTStmt.executeBatch();
						insertTStmt.clearBatch();
						executeCnt = 0;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				//当数量少的时候提交
				insertTStmt.executeBatch();
			}

			log.debug("mr数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据");
			// ----一下进行数据处理----//
			String attMsg = "文件：" + tmpFileName;
			long t1, t2;
			Date d1 = new Date();
//			ResultInfo resultInfo = new ResultInfo();
			// --报告---//
			Date d2 = new Date();

			//更新系统LTE小区表的频点及PCI、 通过业务小区ID
			d1 = new Date();
			String upLteCellTabSql="merge into rno_lte_cell p using RNO_LTE_IMPORT_MID np on (p.business_cell_id = np.business_cell_id) when matched then update set p.pci = np.pci,p.EARFCN = np.EARFCN";
			boolean flag = false;
			try{
				insertTStmt.execute(upLteCellTabSql);
				flag = true;
			}catch(Exception e){
				e.printStackTrace();
			}
			d2 = new Date();
			if (flag) {
				/*report.setSystemFields("完成更新系统lTE小区表", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);*/
				report.setStage("完成更新系统lTE小区表");
				report.setReportType(1);
				report.setBegTime(d1);
				report.setEndTime(d2);
				report.setFinishState(DataParseStatus.Succeded.toString());
				report.setAttMsg("成功完成文件:"
						+ tmpFileName);
				addJobReport(report);
			} else {

				/*report.setSystemFields("完成更新系统lTE小区表", d1, d2,
						DataParseStatus.Failall.toString(), attMsg);*/
				report.setStage("完成更新系统lTE小区表");
				report.setReportType(1);
				report.setBegTime(d1);
				report.setEndTime(d2);
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setAttMsg("失败完成文件:"
						+ tmpFileName);
				addJobReport(report);
				return false;
			}

			long et = System.currentTimeMillis();
			log.info("退出对mr文件：" + tmpFileName + "的解析，耗时：" + (et - st)
					+ "ms");

			// 一个文件一个提交
			//connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			 try {
			 connection.rollback(savepoint);
			 } catch (SQLException e1) {
			 e1.printStackTrace();
			 }
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

			return false;
		} finally {


			if (insertTStmt != null) {
				try {
					insertTStmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return true;
	}
	private boolean preHandleMrLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			 DateUtil dateUtil,long cityId,Map<String, String> cacheExcelCellId,PreparedStatement insertTempStatement) {
		boolean flag = false;
		if (sps == null) {
			return flag;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String enodebId = "", scEarfcn = "", scPci = "",  cellName="",cellId="",ncellId="",excelCellId="";
		double	cellLon=0 ,cellLat=0;
		for (int i = 0; i < sps.length; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段 即xml中的name
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("ENODEB_ID")) {
				enodebId = sps[i];
			} else if (dbField.equals("CELL_BCCH")) {
				scEarfcn = sps[i];
			} else if (dbField.equals("CELL_PCI")) {
				scPci = sps[i];
			} else if (dbField.equals("CELL_ID")) {
				excelCellId = sps[i];
			}
		}
		if(cacheExcelCellId.get(excelCellId.trim())==null){

			try {
				insertTempStatement.setString(1, excelCellId.trim());
				insertTempStatement.setString(2, scPci);
				insertTempStatement.setString(3, scEarfcn);
				insertTempStatement.setString(4, enodebId);

				insertTempStatement.addBatch();

				cacheExcelCellId.put(excelCellId.trim(), excelCellId.trim());
				flag = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				flag = false;
				e.printStackTrace();
			}
		}else{
			flag = false;
		}
		return flag;
	}
	private boolean parseMr(JobRunnable jobWorker, JobReport report,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId, Map<String, DBFieldToTitle> dbFieldsToTitles) {
		//重新读取，因为其为公用变量，为了及时复位更新数据：在多个文件中存在此问题
		dbFieldsToTitles = readDbToTitleCfgFromXml();
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
		HTable mrTable =null;
		HTable mrDescTable =null;
		//创建hbase 索引表
		HTable cellMeaTable =null;
		//创建hbase 邻区索引表
		HTable ncellMeaTable =null;
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
			log.debug("爱立信MR文件：" + tmpFileName + ",title 为：" + line + ",有"
					+ fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> pois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			for (String sp : sps) {
				// log.debug("sp==" + sp);
//				sp=sp.toUpperCase();//转换成大写
				index++;
				find = false;
				for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
//					 log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						if (dt.matchType == 1) {
							// find;
							// log.debug("--match type=1.dtf=" + dtf
							// + ",sp=" + sp);
							if (StringUtils.equals(dtf, sp)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
//								System.out.println("sp==" + sp+"---dtf=="+dtf);
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
//								System.out.println("sp==" + sp+"---dtf=="+dtf);
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
				log.error("检查MR文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), "在文件中缺失以下标题:" + msg);
				addJobReport(report);
				// connection.releaseSavepoint(savepoint);
				return false;
			}

			List<Put> mrDataPuts = null;
			List<Put> mrDataDescPuts = null;

			List<Put> cellMeatimePuts = null;
			List<Put> ncellMeatimePuts = null;
			Map<String, Integer> mrDataNumMap=new HashMap<String, Integer>();//将某日期对应的记录数量迭加缓存起来
			try {

				// hbase MR数据
				mrDataPuts = new ArrayList<Put>();
				mrDataDescPuts = new ArrayList<Put>();

				//hbase cell索引数据
				cellMeatimePuts = new ArrayList<Put>();
				//hbase ncell索引数据
				ncellMeatimePuts = new ArrayList<Put>();
			} catch (Exception e) {

				e.printStackTrace();
				// connection.releaseSavepoint(savepoint);
				return false;
			}
			// 逐行读取数据
			int executeCnt = 0;
			//缓存成功数量
			int cacheSuCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			DateUtil dateUtil = new DateUtil();
			mrTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_ERI_MR");
			mrDescTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_MR_DESC");
			mrTable.setAutoFlushTo(false);
			mrTable.setWriteBufferSize(10 * 1024 * 1024);
			mrDescTable.setAutoFlushTo(false);

			cellMeaTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_CELL_MEATIME");
			cellMeaTable.setAutoFlushTo(false);

			ncellMeaTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_NCELL_MEATIME");
			ncellMeaTable.setAutoFlushTo(false);
			// 获取邻区匹配所需要信息
			CellForMatch cellForMatch = rnoMrAdjCellMatch.getMatchCellContext(stmt, cityId);
			//缓存excel表中已经出现过的小区ID
			Map<String, String> cacheExcelCellId = new HashMap<String, String>();
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
//				System.out.println(sps.length+"：左实际长度----右标题长度："+fieldCnt);
				/*if (sps.length != fieldCnt) {
					continue;
				}*/
				totalDataNum++;

				handleLineOk = handleMrLine(sps, fieldCnt, pois,cellForMatch,
						dbFieldsToTitles, dateUtil, mrDataPuts, mrDataNumMap,cellMeatimePuts,ncellMeatimePuts,
						cityId,cacheExcelCellId);

				if (handleLineOk == true) {
					executeCnt++;
					cacheSuCnt++;
				}
				if (executeCnt > 10000) {
					// 每10000行执行一次
					try {
						mrTable.put(mrDataPuts);
						mrDataPuts.clear();
						mrTable.flushCommits();

						cellMeaTable.put(cellMeatimePuts);
						cellMeatimePuts.clear();
						cellMeaTable.flushCommits();

						ncellMeaTable.put(ncellMeatimePuts);
						ncellMeatimePuts.clear();
						ncellMeaTable.flushCommits();

						executeCnt = 0;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				//当数量少的时候提交
				mrTable.put(mrDataPuts);
				mrDataPuts.clear();
				mrTable.flushCommits();

				cellMeaTable.put(cellMeatimePuts);
				cellMeatimePuts.clear();
				cellMeaTable.flushCommits();

				ncellMeaTable.put(ncellMeatimePuts);
				ncellMeatimePuts.clear();
				ncellMeaTable.flushCommits();

			}
			if(cacheSuCnt==0){
				return false;
			}
			log.debug("mr数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据");
			// ----一下进行数据处理----//
			String attMsg = "文件：" + tmpFileName;
			long t1, t2;
			Date d1 = new Date();
//			ResultInfo resultInfo = new ResultInfo();
			// --报告---//
			Date d2 = new Date();

			// 生成描述信息
			d1 = new Date();

			Iterator<String> mrDate=mrDataNumMap.keySet().iterator();
			String recordeTime="";
			boolean flag=true;
			try {
				while (mrDate.hasNext()) {
					recordeTime=mrDate.next();
					Put put = new Put(
							Bytes.toBytes(String.valueOf(cityId)
									+ "_"
									+ String.valueOf(dateUtil.priorityAssignedParseDate(recordeTime, DateFmt.SDF10.getIndex())
											.getTime()) + "_"+"ERI"));
					try {
						put.add("DESCINFO".getBytes(), "CITY_ID".getBytes(),
								String.valueOf(cityId).getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"MEA_TIME".getBytes(),
								recordeTime.getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"RECORD_COUNT".getBytes(),
								String.valueOf(mrDataNumMap.get(recordeTime)).getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"FACTORY".getBytes(),
								"ERI".getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"CREATE_TIME".getBytes(),
								dateUtil.format_yyyyMMddHHmmss(new Date()).getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"MOD_TIME".getBytes(),
								dateUtil.format_yyyyMMddHHmmss(new Date()).getBytes("utf-8"));

						mrDataDescPuts.add(put);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				//提交描述表数据
				mrDescTable.put(mrDataDescPuts);
				mrDataDescPuts.clear();
				mrDescTable.flushCommits();
				//清空
				mrDataNumMap.clear();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				flag=false;
			}
			d2 = new Date();
			if (flag) {
				report.setSystemFields("生成mr描述信息", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				/*report.setSystemFields("生成mr描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());*/
				report.setSystemFields("生成mr描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg);
				addJobReport(report);
				return false;
			}

			long et = System.currentTimeMillis();
			log.info("退出对mr文件：" + tmpFileName + "的解析，耗时：" + (et - st)
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

			return false;
		} finally {
			try {
				mrTable.close();
				mrDescTable.close();
				cellMeaTable.close();
				ncellMeaTable.close();

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
	 *
	 * @title 处理每一行数据
	 * @param sps
	 * @param expectFieldCnt
	 * @param pois
	 * @param cfm
	 * @param dbFtts
	 * @param dateUtil
	 * @param ncsDataPuts
	 * @param mrDataNumMap
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-3-19下午2:17:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private boolean handleMrLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois,CellForMatch cfm, Map<String, DBFieldToTitle> dbFtts,
			 DateUtil dateUtil,
			List<Put> ncsDataPuts, Map<String, Integer> mrDataNumMap, List<Put> cellMeatimePuts,List<Put> ncellMeatimePuts,long cityId,Map<String, String> cacheExcelCellId) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		/*if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}*/
		if (sps == null) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String enodebId = "", scEarfcn = "", scPci = "", meaDate = "", ncEarfcn = "", ncPci = "",cellName="",ncEnodebId="",cellId="",ncellId="",excelCellId="";
		double	cellLon=0 ,cellLat=0;
		for (int i = 0; i < sps.length; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段 即xml中的name
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("ENODEB_ID")) {
				enodebId = sps[i];
			} else if (dbField.equals("CELL_BCCH")) {
				scEarfcn = sps[i];
			} else if (dbField.equals("CELL_PCI")) {
				scPci = sps[i];
			} else if (dbField.equals("NCELL_BCCH")) {
				ncEarfcn = sps[i];
			} else if (dbField.equals("NCELL_PCI")) {
				ncPci = sps[i];
			} else if (dbField.equals("MEA_TIME")) {
				meaDate = sps[i];
			}  else if (dbField.equals("CELL_ID")) {
				excelCellId = sps[i];
			}
		}
		//小区匹配：判断工参表中是否存在该小区，不存在则不录入库-----验证服务小区是否满足条件，不满足则不处理
		/*cellName=cfm.getMatchCell(enodebId+"_"+scEarfcn+"_"+scPci);
		if(cellName==null){
			return false;
		}*/
		String cellKey=enodebId+"_"+scEarfcn+"_"+scPci;
		String ncellBcchPciKey=ncEarfcn+"_"+ncPci;
		CellLonLat cellInfo = null;
		cellInfo=cfm.getMatchCell(cellKey);
		//系统工参表中不存在该小区则忽视
		if(cellInfo==null){
			//不存在则将其添加至某逗号相分割的字符串中
			if(cacheExcelCellId.get(excelCellId.trim())==null){
				cacheExcelCellId.put(excelCellId.trim(), excelCellId.trim());
				cellBuffer.append(excelCellId.trim()+",");
			}
			return false;
		}
		cellName=cellInfo.getName();
		cellId=cellInfo.getCell();
		//描述表记录数登记
		if(mrDataNumMap.get(meaDate)==null){
			mrDataNumMap.put(meaDate, 1);
		}else{
			mrDataNumMap.put(meaDate, mrDataNumMap.get(meaDate)+1);
		}
		// ---邻区匹配----//
		// 邻区信息
		String ncell = "";
//		String ncells = "";
		StringBuffer ncells = new StringBuffer();
		double dis = 1000000;
		String ncellKey = ncEarfcn + "_" +ncPci ;
		List<String> mayNcells = cfm.getCachedMatchNcell(cellName + "_" + ncellKey);
		// 邻区属性
		double ncellLon = 0;
		double ncellLat = 0;

		if (mayNcells != null) {
			// 已经匹配过了
			ncell = mayNcells.get(0);
//			ncells = mayNcells.get(1);
			ncells .append(mayNcells.get(1));
			try {
				dis = Double.parseDouble(mayNcells.get(2));
			} catch (Exception e) {
			}
			ncEarfcn = String.valueOf(mayNcells.get(3));
			ncPci = String.valueOf(mayNcells.get(4));

			cellLon = Double.parseDouble(mayNcells.get(5));
			cellLat = Double.parseDouble(mayNcells.get(6));
			// 邻区属性值
			ncellLon = Double.parseDouble(mayNcells.get(7));
			ncellLat = Double.parseDouble(mayNcells.get(8));
			ncellId=mayNcells.get(9);

		} else {
			// 进行匹配
//			cellInfo = cfm.getCells().get(cell);
			cellInfo=cfm.getMatchCell(cellKey);
			if (cellInfo != null) {
				// 主小区存在:此key为ncearfcn+ncpci:同频同PCI下存在若干个小区
				List<CellLonLat> clls = cfm.getBcchBsicCells(ncellBcchPciKey);
				if (clls != null && clls.size() > 0) {
					//同频同PCI的邻区只有一个的情况
					if (clls.size() == 1) {
						ncell = clls.get(0).getName();
						// 算距离米为单位
						dis = LatLngHelper.Distance(cellInfo.getLon(), cellInfo
								.getLat(), clls.get(0).getLon(), clls.get(0)
								.getLat());
						dis = dis / 1000;//转换为千米或公里
						/*ncells = ncell + "(" + clls.get(0).getName() + ",["
								+ dis + "km]";*/
						ncells.append(ncell);
						ncells.append("(");
						ncells.append(",[");
						ncells.append(dis);
						ncells.append("km]");

						ncEnodebId = String.valueOf(clls.get(0).getEnodebId());
						ncEarfcn = String.valueOf(clls.get(0).getBcch());
						ncPci = String.valueOf(clls.get(0).getPci());
						ncellId=clls.get(0).getCell();
					} else {
						//同频同PCI的邻区有若干个的情况
						// 计算所有的距离  treemap可以按序排列
						Map<Double, CellLonLat> disInfo = new TreeMap<Double, CellLonLat>();
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
								ncell = entry2.getValue().getName();
								ncEnodebId = String.valueOf(entry2.getValue().getEnodebId());
								ncEarfcn = String.valueOf(entry2.getValue().getBcch());
								ncPci = String.valueOf(entry2.getValue().getPci());
								ncellId=entry2.getValue().getCell();
								dis = entry2.getKey();
								// System.out.println("ncell:"+ncell+"------dis:"+dis);
							}
							/*ncells += entry2.getValue().getCell() + "("
									+ entry2.getValue().getName() + ",["
									+ entry2.getKey() + "]),";*/
							ncells.append(entry2.getValue().getCell());
							ncells.append("(");
							ncells.append(entry2.getValue().getName());
							ncells.append(",[");
							ncells.append(entry2.getKey());
							ncells.append( "]),");
							num++;// 不加永远是最大一个（升序）
							// System.out.println("ncells:"+ncells);
						}
//						ncells = ncells.substring(0, ncells.length() - 1);
						ncells.deleteCharAt(ncells.length()-1);
						// System.out.println("ncells:"+ncells);

					}
					// 小区属性值
					cellLon = cellInfo.getLon();
					cellLat = cellInfo.getLat();

					// 邻区属性值
//					CellLonLat ncellInfo = cfm.getCells().get(ncell);
					CellLonLat ncellInfo=cfm.getMatchCell(ncEnodebId+"_"+ncEarfcn+"_"+ncPci);
					ncellLon = ncellInfo.getLon();
					ncellLat = ncellInfo.getLat();
					ncellId=ncellInfo.getCell();
				} else {
					// pci与arfcn无组合
					ncell = "NotF_" + ncEarfcn + "_" + ncPci;
//					ncells = "";
					ncells.delete(0, ncells.length());
					// 小区属性值
					scEarfcn = String.valueOf(cellInfo.getBcch());
					cellLon = cellInfo.getLon();
					cellLat = cellInfo.getLat();
				}
			} else {
				// 主小区不存在
				ncell = "notSure";
//				ncells = "";
				ncells.delete(0, ncells.length());
			}
		}
		// 设置缓存
		cfm.putCachedMatchNcell(cellName + "_" + ncellBcchPciKey, Arrays.asList(ncell, ncells.toString(),
				dis + "", ncEarfcn + "", ncPci + "", cellLon + "", cellLat + "", ncellLon + "",
				ncellLat + "",ncellId));

		// CELL MEATIME ROWKEY
		Calendar ca = Calendar.getInstance();
		ca.setTime(dateUtil.parseDateArbitrary(meaDate));
		Put cellMeatimePut = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ "MR" + "_" + String.valueOf(ca.get(Calendar.YEAR)) + "_"
				+ cellId));//cellId   小区标识为服务小区建索引
		/*cellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), Bytes.toBytes(String.valueOf(cityId)
						+ "_"
						+ String.valueOf(dateUtil.priorityAssignedParseDate(meaDate, DateFmt.SDF10.getIndex())
								.getTime()) + "_" + enodebId
						+ "_" + scEarfcn + "_"
						+ scPci+"_"+ncEarfcn + "_"
						+ ncPci),
				null);*/
		cellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), Bytes.toBytes(String.valueOf(cityId)
				+ "_"
				+ String.valueOf(dateUtil.priorityAssignedParseDate(meaDate,
						DateFmt.SDF10.getIndex()).getTime()) + "_" + cellId
				+ "_" + ncellId),
				null);
		//ncell meatime rowkey
		Put ncellMeatimePut = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ "MR" + "_" + String.valueOf(ca.get(Calendar.YEAR)) + "_"
				+ ncellId));//ncellId   邻区标识为服务小区建索引
		ncellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), Bytes.toBytes(String.valueOf(cityId)
				+ "_"
				+ String.valueOf(dateUtil.priorityAssignedParseDate(meaDate,
						DateFmt.SDF10.getIndex()).getTime()) + "_" + cellId
				+ "_" + ncellId),
				null);
		// hbase rowkey MR
		/*Put put = new Put(
				Bytes.toBytes(String.valueOf(cityId)
						+ "_"
						+ String.valueOf(dateUtil.priorityAssignedParseDate(meaDate, DateFmt.SDF10.getIndex())
								.getTime()) + "_" + enodebId
						+ "_" + scEarfcn + "_"
						+ scPci+"_"+ncEarfcn + "_"
						+ ncPci));*/
		Put put = new Put(Bytes.toBytes(String.valueOf(cityId)
				+ "_"
				+ String.valueOf(dateUtil.priorityAssignedParseDate(meaDate,
						DateFmt.SDF10.getIndex()).getTime()) + "_" + cellId
				+ "_" + ncellId));
		/*put.add("MRINFO".getBytes(), "RECORD_TIME".getBytes(),
				recordTime.getBytes());
		put.add("MRINFO".getBytes(), "MEA_TIME".getBytes(),
				meaDate.getBytes());*/
		put.add("MRINFO".getBytes(), "CITY_ID".getBytes(),
				String.valueOf(cityId).getBytes());
		Iterator<DBFieldToTitle>  dbToTitles=dbFtts.values().iterator();
		DBFieldToTitle dbToTitle=null;
		while (dbToTitles.hasNext()) {
			dbToTitle=dbToTitles.next();
			try {
				if(sps.length-1<dbToTitle.index){
					continue;
				}

				put.add("MRINFO".getBytes(),dbToTitle.dbField.getBytes(),
						sps[dbToTitle.index].getBytes("utf-8"));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		try {
			//添加匹配的小区信息
			put.add("MRINFO".getBytes(),"CELL_ID".getBytes(),
					cellId.getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"CELL_NAME".getBytes(),
					cellName.getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"CELL_LON".getBytes(),
					String.valueOf(cellLon).getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"CELL_LAT".getBytes(),
					String.valueOf(cellLat).getBytes("utf-8"));
			//添加匹配的邻区信息
			put.add("MRINFO".getBytes(),"NCELL_ID".getBytes(),
					ncellId.getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"NCELL".getBytes(),
					ncell.getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"NCELL_LON".getBytes(),
					String.valueOf(ncellLon).getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"NCELL_LAT".getBytes(),
					String.valueOf(ncellLat).getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"NCELLS".getBytes(),
					String.valueOf(ncells).getBytes("utf-8"));
			put.add("MRINFO".getBytes(),"DISTANCE".getBytes(),
					String.valueOf(dis).getBytes("utf-8"));
			//放入集合
			ncsDataPuts.add(put);
			cellMeatimePuts.add(cellMeatimePut);
			ncellMeatimePuts.add(ncellMeatimePut);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;

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
					Eri4GMrParserJobRunnable.class.getResource(
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
	 * @title 从xml配置 文件中读取eri MR 数据库到excel的映射关系
	 * @return
	 * @author chao.xj
	 * @date 2015-3-17上午9:54:44
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, DBFieldToTitle> readDbToTitleCfgFromXml() {
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in = new FileInputStream(new File(
					Eri4GMrParserJobRunnable.class.getResource(
							"eri4GMrDbToTitles.xml").getPath()));
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
