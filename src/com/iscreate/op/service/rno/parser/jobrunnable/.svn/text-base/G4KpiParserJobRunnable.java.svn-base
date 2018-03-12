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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import com.iscreate.op.dao.rno.RnoMrAdjCellMatchImpl.CellForMatch;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.DateUtil.DateFmt;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class G4KpiParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(G4KpiParserJobRunnable.class);
	private RnoMrAdjCellMatch rnoMrAdjCellMatch=new RnoMrAdjCellMatchImpl();
	private StringBuffer cellBuffer=new StringBuffer();
	public G4KpiParserJobRunnable() {
		super();
		super.setJobType("G4KPIFILE");
	}
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
		List<File> allKpiFiles = new ArrayList<File>();// 将所有待处理的KPI文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2,cacheDate1;
		cacheDate1 = date1;//缓存第一次进入runable的时间
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的KPI文件是一个压缩包。");

			// 进行解压
			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "") + "/";

			//
			boolean unzipOk = false;
			try {
				
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
					allKpiFiles.add(f);
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
			log.info("上传的KPI是一个普通文件。");
			allKpiFiles.add(file);
		}

		if (allKpiFiles.isEmpty()) {
			msg = "未上传有效的KPI文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的KPI文件！注意zip包里不能再包含有文件夹！");
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
		
		
		int totalFileCnt = allKpiFiles.size();
		int i = 0;
		//是否还需要验证标题头
		Map<String, DBFieldToTitle> dbFieldsToTitles = readDbToTitleCfgFromXml();
		for (File f : allKpiFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
					//对文件进行处理：在更新该区域的lte小区的频点及PCI信息基础上进行邻区匹配及数据入库工作
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

		if (sucCnt == allKpiFiles.size()) {
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
				log.error("检查KPI文件：" + tmpFileName + "的标题头有问题！" + msg);
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
					"LTE4G_KPI_DATA");
			mrDescTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_KPI_DESC");
			mrTable.setAutoFlushTo(false);
			mrTable.setWriteBufferSize(10 * 1024 * 1024);
			mrDescTable.setAutoFlushTo(false);

			
			cellMeaTable = new HTable(
					HadoopXml.getHbaseConfig(),
					"LTE4G_CELL_MEATIME");
			cellMeaTable.setAutoFlushTo(false);
			
			
			// 获取邻区匹配所需要信息
//			CellForMatch cellForMatch = rnoMrAdjCellMatch.getMatchCellContext(stmt, cityId);
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
				totalDataNum++;
			
				handleLineOk = handleMrLine(sps, fieldCnt, pois,
						dbFieldsToTitles, dateUtil, mrDataPuts, mrDataNumMap,cellMeatimePuts,
						cityId);

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
											.getTime())));
					try {
						put.add("DESCINFO".getBytes(), "CITY_ID".getBytes(),
								String.valueOf(cityId).getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"MEA_TIME".getBytes(),
								recordeTime.getBytes("utf-8"));
						put.add("DESCINFO".getBytes(),"RECORD_COUNT".getBytes(),
								String.valueOf(mrDataNumMap.get(recordeTime)).getBytes("utf-8"));
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
				report.setSystemFields("生成kpi描述信息", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				/*report.setSystemFields("生成mr描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());*/
				report.setSystemFields("生成kpi描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg);
				addJobReport(report);
				return false;
			}

			long et = System.currentTimeMillis();
			log.info("退出对kpi文件：" + tmpFileName + "的解析，耗时：" + (et - st)
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
			/*try {
				insertTStmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}*/
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
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			 DateUtil dateUtil,
			List<Put> ncsDataPuts, Map<String, Integer> mrDataNumMap,List<Put> cellMeatimePuts,long cityId) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		/*if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}*/
		if (sps == null) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String  meaDate = "", ncEarfcn = "", cellName="",cellId="",mmeId="";
		double	cellLon=0 ,cellLat=0;
		for (int i = 0; i < sps.length; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段 即xml中的name
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("MEA_TIME")) {
				meaDate = sps[i];
			} else if (dbField.equals("CELL_ID")) {
				//186880-2
				cellId = sps[i].replace("-", "");
			} else if (dbField.equals("MME_ID")) {
				mmeId = sps[i];
			} 
		}
		//小区匹配：判断工参表中是否存在该小区，不存在则不录入库-----验证服务小区是否满足条件，不满足则不处理
		/*cellName=cfm.getMatchCell(enodebId+"_"+scEarfcn+"_"+scPci);
		if(cellName==null){
			return false;
		}*/
		if(cellId==null||"".equals(cellId)){
			//不存在则将其添加至某逗号相分割的字符串中
			return false;
		}
		//描述表记录数登记
		Date mDate= dateUtil.parseDateArbitrary(meaDate);
		//yyyy-mm-dd
		meaDate = dateUtil.format_yyyyMMdd(mDate);
		if(mrDataNumMap.get(meaDate)==null){
			mrDataNumMap.put(meaDate, 1);
		}else{
			mrDataNumMap.put(meaDate, mrDataNumMap.get(meaDate)+1);
		}
		
		
		// CELL MEATIME ROWKEY
		Calendar ca = Calendar.getInstance();
		ca.setTime(dateUtil.parseDateArbitrary(meaDate));
		Put cellMeatimePut = new Put(Bytes.toBytes(String.valueOf(cityId) + "_"
				+ "KPI" + "_" + String.valueOf(ca.get(Calendar.YEAR)) + "_"
				+ cellId));//cellId   小区标识为服务小区建索引
		//yyyy-mm-dd hh:mm:ss
		cellMeatimePut.add(String.valueOf(ca.get(Calendar.MONTH) + 1)
				.getBytes(), Bytes.toBytes(String.valueOf(cityId)
				+ "_"
				+ String.valueOf(dateUtil.priorityAssignedParseDate(dateUtil.format_yyyyMMddHHmmss(mDate),
						DateFmt.SDF10.getIndex()).getTime()) + "_" + cellId+"_"+mmeId),
				null);
		// hbase rowkey MR
		//yyyy-mm-dd hh:mm:ss
		Put put = new Put(Bytes.toBytes(String.valueOf(cityId)
				+ "_"
				+ String.valueOf(dateUtil.priorityAssignedParseDate(dateUtil.format_yyyyMMddHHmmss(mDate),
						DateFmt.SDF10.getIndex()).getTime()) + "_" + cellId+"_"+mmeId));
		put.add("KPIINFO".getBytes(), "CITY_ID".getBytes(),
				String.valueOf(cityId).getBytes());
		
		Iterator<DBFieldToTitle>  dbToTitles=dbFtts.values().iterator();
		DBFieldToTitle dbToTitle=null;
		while (dbToTitles.hasNext()) {
			dbToTitle=dbToTitles.next();
			try {
				if(sps.length-1<dbToTitle.index || "CELL_ID".equals(dbToTitle.dbField.toString())){
					continue;
				}
				
				put.add("KPIINFO".getBytes(),dbToTitle.dbField.getBytes(),
						sps[dbToTitle.index].getBytes("utf-8"));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		try {
			//添加小区信息
			put.add("KPIINFO".getBytes(),"CELL_ID".getBytes(),
					cellId.getBytes("utf-8"));
			
			//放入集合
			ncsDataPuts.add(put);
			cellMeatimePuts.add(cellMeatimePut);
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
					G4KpiParserJobRunnable.class.getResource(
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
					G4KpiParserJobRunnable.class.getResource(
							"g4KpiDbToTitles.xml").getPath()));
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
