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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import oracle.sql.DATE;

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
import com.iscreate.op.dao.rno.RnoMrAdjCellMatchImpl.CellLonLat;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.jobrunnable.HwNcsParserJobRunnable.DBFieldToTitle;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.DateUtil.DateFmt;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.LatLngHelper;

public class G4DtDataServiceParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(G4DtDataServiceParserJobRunnable.class);
	
	public G4DtDataServiceParserJobRunnable() {
		super();
		super.setJobType("4GDTDATASERVICEFILE");
	}
	//数据表
	private static String dataTable = "rno_4g_dt_sampling";
	//描述表
	private static String descTable = "rno_4g_dt_sampling_desc";
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
		List<File> allMrFiles = new ArrayList<File>();// 将所有待处理的DT文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2;
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的DT文件是一个压缩包。");

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
			msg = "未上传有效的DT文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的DT文件！注意zip包里不能再包含有文件夹！");
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
		//获取区域类型
		HttpSession session = SessionService.getInstance().getSession();
		// 在判断器里保存
		String areaType = session.getAttribute("areaType").toString();
		// 读取数据库与文本文件格式的配置
		// dbFieldsToTitles = readDbToTitleCfgFromProfile();

		String tmpFileName = fileName;
		int sucCnt = 0;
		boolean parseOk = false;
		
		
		int totalFileCnt = allMrFiles.size();
		int i = 0;
		//是否还需要验证标题头
		Map<String, DBFieldToTitle> dbFieldsToTitles = readDbToTitleCfgFromXml();
		for (File f : allMrFiles) {
			try {
				// 每一个文件的解析都应该是独立的
				if (fromZip) {
//					tmpFileName = new String(f.getName().getBytes("iso-8859-1"), "utf-8");
					tmpFileName = f.getName();
				}
//				System.out.println("文件名：========="+tmpFileName);
				//以防在LINUX环境下 汉字出现乱码现象
				/*tmpFileName = new String(tmpFileName.getBytes(), "utf-8");
				System.out.println("编码后文件名：=========="+tmpFileName);*/
				date1 = new Date();
				parseOk = parseDt(this, report, tmpFileName, f, connection,
						stmt, cityId, dbFieldsToTitles,areaType);
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
				report.setFinishState(DataParseStatus.Failall.toString());
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setReportType(1);
				report.setAttMsg("文件解析出错（" + i + "/" + totalFileCnt + "）:"
						+ tmpFileName);
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
				log.error(msg);
//				return status;
				
			}finally{
				session.removeAttribute("areaType");
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

		
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

	private boolean parseDt(JobRunnable jobWorker, JobReport report,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId, Map<String, DBFieldToTitle> dbFieldsToTitles,String areaType) {
		//重新读取，因为其为公用变量，为了及时复位更新数据：在多个文件中存在此问题
		dbFieldsToTitles = readDbToTitleCfgFromXml();
		PreparedStatement insertTStmt = null;
		PreparedStatement insertDescState=null;
		long st = System.currentTimeMillis();

		Date date1 = new Date();
		Date date2 = null;
		// 读取文件
		BufferedReader reader = null;
		String charset = null;
//		charset = FileTool.getFileEncode(f.getAbsolutePath());
		charset = "gbk";
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
				if (line == null) {
					sps = new String[] {};
					break;
				}
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			// 读取到标题头
			int fieldCnt = sps.length;
			log.debug("DT文件：" + tmpFileName + ",title 为：" + line + ",有"
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
				log.error("检查DT文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), "在文件中缺失以下标题:" + msg);
				addJobReport(report);
				// connection.releaseSavepoint(savepoint);
				return false;
			}
			//文件名到其聚合对象
			Map<String, DtObj> dtObjMap=new HashMap<String, DtObj>();
			// 逐行读取数据
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			DateUtil dateUtil = new DateUtil();
			// 拼装sql
			String insertTableSql = "insert into " + dataTable + " (";
			String ws = "";
			index = 1;
			for (String d : dbFieldsToTitles.keySet()) {
				if (dbFieldsToTitles.get(d).index >= 0) {
					// 只对出现了的进行组sql
					dbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
					insertTableSql += d + ",";
					ws += "?,";
				}
			}
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			insertTableSql += "CITY_ID,DATA_TYPE,ID,DT_SAMPLING_DESC_ID";

			ws += cityId + ",'DataService',SEQ_RNO_4G_DT_SAMPLING.nextval,?";
			// 此处也要增加相应的位置索引
			insertTableSql += ") values ( " + ws + " )";
			int descIdSqlIndex = index++;
			try {
				insertTStmt = connection
						.prepareStatement(insertTableSql);
			} catch (Exception e) {
				msg = "准备路测数据插入的prepareStatement失败";
				log.error("准备路测数据插入的prepareStatement失败！sql="
						+ insertTableSql);
				e.printStackTrace();
				// connection.releaseSavepoint(savepoint);
				return false;
			}
			do {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line + ",$";
//				System.out.println("line====>>>>"+line);
				sps = line.split(",|\t");
				totalDataNum++;
			
				handleLineOk = handleDtLine(sps, fieldCnt, pois, dbFieldsToTitles, dateUtil, dtObjMap, cityId, insertTStmt,descIdSqlIndex,stmt);
				if (handleLineOk == true) {
					executeCnt++;
				}
				if (executeCnt > 5000) {
					// 每5000行执行一次
					try {						
						insertTStmt.executeBatch();
						insertTStmt.clearBatch();
						
						executeCnt = 0;
					} catch (Exception e) {
						e.printStackTrace();
						insertTStmt.close();
						connection.commit();
						return false;
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				//当数量少的时候提交
				insertTStmt.executeBatch();				
			}

			log.debug("dt数据文件：" + tmpFileName + "共有：" + totalDataNum
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
			
			Iterator<String> fileObj=dtObjMap.keySet().iterator();
			String fileName="";
			DtObj dtObj=null;
			boolean flag=true;
			String insertDescSql="insert into "+descTable+" (dt_sampling_desc_id,file_name,mea_time,record_count,create_time,mod_time,city_id,data_type,area_type) values(?,?,?,?,sysdate,sysdate,"+cityId+",'DataService','"+areaType+"')";
			
			try {
				insertDescState=connection.prepareStatement(insertDescSql);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			while (fileObj.hasNext()) {
				fileName = (String) fileObj.next();
				dtObj=dtObjMap.get(fileName);
				insertDescState.setLong(1, dtObj.getDescId());
				insertDescState.setString(2, fileName);
				insertDescState.setDate(3, new java.sql.Date(dtObj.getMeaTime().getTime()));
				insertDescState.setLong(4, dtObj.getCnt());
				insertDescState.addBatch();
			}
			try {
				insertDescState.executeBatch();
			} catch (Exception e) {
				// TODO: handle exception
				flag=false;
				e.printStackTrace();
			}
			d2 = new Date();
			if (flag) {
				report.setSystemFields("生成dt描述信息", d1, d2,
						DataParseStatus.Succeded.toString(), attMsg);
				addJobReport(report);
			} else {
				/*report.setSystemFields("生成mr描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg + "--"
								+ resultInfo.getMsg());*/
				report.setSystemFields("生成dt描述信息", d1, d2,
						DataParseStatus.Failall.toString(), attMsg);
				addJobReport(report);
				return false;
			}

			long et = System.currentTimeMillis();
			log.info("退出对DT文件：" + tmpFileName + "的解析，耗时：" + (et - st)
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
			if (insertDescState != null) {
				try {
					insertDescState.close();
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
	 * @param dbFtts
	 * @param dateUtil
	 * @param mrDataNumMap
	 * @param cityId
	 * @param insertTStmt
	 * @return
	 * @author chao.xj
	 * @date 2015-7-7下午4:56:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private boolean handleDtLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			 DateUtil dateUtil, Map<String, DtObj> dtObjMap,long cityId,PreparedStatement insertTStmt,int descIdSqlIndex,Statement stmt) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		/*if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}*/
		if (sps == null) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String fileName = "",meaTime="";
		DtObj dtObj=null;
		long descId=-1;
		for (int i = 0; i < sps.length-1; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段 即xml中的name
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("FILE_NAME")) {
				fileName = sps[i];
			}
			if (dbField.equals("MEA_TIME")) {
				meaTime = sps[i];
			}
		}
		
			
			if(!dtObjMap.containsKey(fileName)){
				dtObj=new DtObj();
				descId=RnoHelper.getNextSeqValue("SEQ_RNO_4G_DT_SAMPLING_DESC", stmt);
				dtObj.setDescId(descId);
				dtObj.setCnt(1);
				dtObj.setMeaTime(dateUtil.to_yyMMddHHmmssDate(meaTime));
				dtObjMap.put(fileName, dtObj);
			}else{
				dtObj=dtObjMap.get(fileName);
				//该文件已经存在，则累加1
				dtObj.setCnt(dtObj.getCnt()+1);
				//取出该文件已经存在描述ID
				descId=dtObj.getDescId();
				//时间不变，均为同一天的文件
			}
			try {
				//为该行记录设置描述ID
				insertTStmt.setLong(descIdSqlIndex, descId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			// log.debug(i+" -> dbField="+dbField);
//			System.out.println(i+" -> dbField="+dbField+"----val----->"+sps[i]);
			if (dbField == null) {
				continue;
			}
			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dt != null) {
				setIntoPreStmt(insertTStmt, dt, sps[i], dateUtil);
			}
		}
		
		try {
			insertTStmt.addBatch();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
					G4DtDataServiceParserJobRunnable.class.getResource(
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
	class DtObj{
		private long descId;//某文件名对应的描述ID
		private long cnt;//该文件名拥有N个测量记录
		private Date meaTime;//该文件名产生的日期限于某一天，因为其下是年月日时分秒，只保存年月日
		public Date getMeaTime() {
			return meaTime;
		}
		public void setMeaTime(Date meaTime) {
			this.meaTime = meaTime;
		}
		public long getDescId() {
			return descId;
		}
		public void setDescId(long descId) {
			this.descId = descId;
		}
		public long getCnt() {
			return cnt;
		}
		public void setCnt(long cnt) {
			this.cnt = cnt;
		}
	}
	/**
	 * 
	 * @title 从xml配置 文件中读取4g 路测数据业务 数据库到excel的映射关系
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
					G4DtDataServiceParserJobRunnable.class.getResource(
							"g4DtDataServiceDbToTitles.xml").getPath()));
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
	private void setIntoPreStmt(PreparedStatement pstmt, DBFieldToTitle dt,
			String val, DateUtil dateUtil) {
		String type = dt.getDbType();
		int index = dt.sqlIndex;
		/*if(index==6){
			System.out.println(index);
		}*/
//		System.out.println("-字段->"+dt.getDbField()+"-字段类型->"+dt.getDbType()+"-sqlindex->"+index);
		if (index < 0) {
			log.error(dt + "在sql插入语句中，没有相应的位置！");
			return;
		}
		if (StringUtils.equalsIgnoreCase("Long", type)) {
			if (!StringUtils.isBlank(val) && isNum(val)) {
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
//					Date date = dateUtil.parseDateArbitrary(val);
					Date date = dateUtil.to_yyMMddHHmmssDate(val);
					if (date != null) {
						pstmt.setTimestamp(index,
								new java.sql.Timestamp(date.getTime()));
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
			if (!StringUtils.isBlank(val) && isNum(val)) {
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
		}else if (StringUtils.startsWithIgnoreCase("double", type)) {
			if (!StringUtils.isBlank(val) && isNum(val)) {
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
	/**
	 * 
	 * @title 判断正负、整数小数即是否为数字
	 * @param str
	 * @return
	 * @author chao.xj
	 * @date 2015-7-4下午4:33:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  boolean isNum(String str) {
		//可以判断正负、整数小数
	    Boolean strResult = str.matches("-?[0-9]+.*[0-9]*");
	    return strResult;
	}
	public static void main(String[] args) {
		//113.104507
//		System.out.println(StringUtils.isNumeric("113.104507"));
//		System.out.println(isNum("113.104507"));
		//15-06-01 10:02:01.793
		DateUtil dateUtil=new DateUtil();
//		dateUtil.to_yyyyMMddDate("15-06-01 10:02:01.793");
		System.out.println(dateUtil.to_yyyyMMddDate("15-06-01 10:02:01.793"));
		System.out.println(dateUtil.to_yyyyMMddHHmmssDate("15-06-01 10:02:01.793"));
		System.out.println(new java.sql.Date(dateUtil.to_yyyyMMddDate("15-06-01 10:02:01.793").getTime()));
		System.out.println(new java.sql.Date(dateUtil.to_yyyyMMddHHmmssDate("15-06-01 10:02:01.793").getTime()));
		/*SimpleDateFormat sdf3 = new SimpleDateFormat(
				"yy/MM/dd hh:mm:ss");
		
		try {
			System.out.println(new java.sql.Date(sdf3.parse("15-06-01 10:02:01.793").getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
		//			Date d = sf.parse("15-06-01 10:02:01.793");
					Date d = dateUtil.to_yyMMddHHmmssDate("15-06-01 10:02:01.793");
					System.out.println("sql==="+new java.sql.Timestamp(d.getTime()));
					System.out.println(dateUtil.format_yyyyMMdd(d));
					System.out.println("d = " + d);
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					System.out.println(d+"-----"+dateUtil.format_yyyyMMdd(d));
					System.out.println(c.getTime()+"---"+dateUtil.format_yyyyMMdd(c.getTime()));
		
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
