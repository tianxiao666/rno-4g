package com.iscreate.op.service.rno.parser.jobrunnable;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.Rno2GHwCellManageService;
import com.iscreate.op.service.rno.Rno2GHwCellManageServiceImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.jobrunnable.HwNcsParserJobRunnable.DBFieldToTitle;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class Hw2GCellParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(Hw2GCellParserJobRunnable.class);
	private Rno2GHwCellManageService cellManageService=new Rno2GHwCellManageServiceImpl();
	public Hw2GCellParserJobRunnable() {
		super();
		super.setJobType("2GHWCELLFILE");
	}
	// 构建insert语句
	private static String hw2GCellDescTable = "RNO_2G_HW_CELL_DESC";
	private static String hw2GCellTable="RNO_2G_HW_CELL";
	private static String hw2GNcellTable="RNO_2G_HW_NCELL";
	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {
		// TODO Auto-generated method stub
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
		Date baseDate=dataRec.getBusinessTime();
		DateUtil dateUtil=new DateUtil();
		String baseDateStr=dateUtil.format_yyyyMMdd(baseDate);
		String fileName = dataRec.getFileName();
		filePath = FileInterpreter.makeFullPath(dataRec.getFullPath());
//		file = new File(filePath);
		file = FileTool.getFile(filePath);
		long dataId = dataRec.getDataCollectId();

		String msg = "";
		// 开始解析
				List<File> allFiles = new ArrayList<File>();// 将所有待处理的文件放置在这个列表里
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
							allFiles.add(f);
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
					log.info("上传的是一个普通文件。");
					allFiles.add(file);
				}
				if (allFiles.isEmpty()) {
					msg = "未上传有效的文件！zip包里不能再包含有文件夹！";
					log.error(msg);
					// super.setCachedInfo(token, msg);
					// clearResource(destDir, null);
					// job报告
					date2 = new Date();
					report.setFinishState(DataParseStatus.Failall.toString());
					report.setStage(DataParseProgress.Decompress.toString());
					report.setBegTime(date1);
					report.setEndTime(date2);
					report.setAttMsg("未上传有效的文件！注意zip包里不能再包含有文件夹！");
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
				String tmpFileName = fileName;
				int sucCnt = 0;
				boolean parseOk = false;
				int totalFileCnt = allFiles.size();
				int i = 0;
				//获取bsc标识到ID的映射关系
				Map<String, Integer> bscToId=cellManageService.queryBscByCityId(stmt,cityId);
				//华为2G小区字段对应标题
				Map<String,DBFieldToTitle> hw2GCellDbFieldsToTitles  = readDbToTitleCfgFromXml("hw2GCellToTitle.xml");
				//华为2G邻区字段对应标题
				Map<String,DBFieldToTitle> hw2GNcellDbFieldsToTitles  = readDbToTitleCfgFromXml("hw2GNcellParamToTitle.xml");
				for (File f : allFiles) {
					try {
						// 每一个文件的解析都应该是独立的
						if (fromZip) {
							tmpFileName = f.getName();
						}
						date1 = new Date();
						parseOk = parseCsv(this, report,dataRec, tmpFileName, f, connection, stmt, cityId,baseDateStr, hw2GCellDbFieldsToTitles,hw2GNcellDbFieldsToTitles,bscToId);
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

				if (sucCnt > 0) {
					status.setJobState(JobState.Finished);
					status.setUpdateTime(new Date());
				} else {
					status.setJobState(JobState.Failed);
					status.setUpdateTime(new Date());
				}

				if (sucCnt == allFiles.size()) {
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
				log.debug("更新结果状态 rno_data_collect_rec  sql:"+sql);
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
	/**
	 * 
	 * @title 从xml配置 文件中读取eri 2g cell 工程 参数 数据库到csv的映射关系
	 * @param proFile
	 * @return
	 * @author chao.xj
	 * @date 2014-10-13上午11:15:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static Map<String,DBFieldToTitle > readDbToTitleCfgFromXml(String proFile) {  
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in =new FileInputStream(new File(Hw2GCellParserJobRunnable.class.getResource(
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

	private boolean parseCsv(JobRunnable jobWorker, JobReport report,RnoDataCollectRec dataRec,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId,String baseDateStr,Map<String, DBFieldToTitle> cellDbFieldsToTitles,Map<String, DBFieldToTitle> ncellDbFieldsToTitles,Map<String, Integer> bscToId) {

		String spName = "everyStartHw" + System.currentTimeMillis();
		log.debug("f path:"+f.getAbsolutePath());
		Savepoint savepoint = null;
		try {
			savepoint = connection.setSavepoint(spName);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement insertCellTStmt = null;
		Statement insertNcellTStmt = null;
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
		
		try {
			String[] sps = new String[1];
			do {
				line = reader.readLine();
				if (line == null) {
					sps = new String[] {};
					break;
				}
				sps = line.split(",");
			} while (sps == null || sps.length < 14);
			// 读取到标题头：标题头的全部数量
			int fieldCnt = sps.length;
			log.debug("csv文件：" + tmpFileName + ",title 为：" + line
			 + ",有" + fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> cellPois = new HashMap<Integer, String>();
			Map<Integer, String> ncellPois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－两张表共用标题头分别判断  小区表开始－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－//
			//专门记录邻区位置
			for (String sp : sps) {
				// log.debug("sp==" + sp);
				index++;
				find = false;
				for (DBFieldToTitle dt : cellDbFieldsToTitles.values()) {
					// log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						if (dt.matchType == 1) {
							if (StringUtils.equals(dtf, sp)) {
								find = true;
								dt.setIndex(index);
								cellPois.put(index, dt.dbField);// 快速记录标题位置
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
								cellPois.put(index, dt.dbField);// 快速记录
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
			for (DBFieldToTitle dt : cellDbFieldsToTitles.values()) {
				if (dt.isMandatory && dt.index < 0) {
					msg += "[" + dt.dbField + "在文件中未找到对应的数据]\r\n";
				}
			}
			if (!StringUtils.isBlank(msg)) {
				log.error("cell检查csv文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("cell检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), msg);
				addJobReport(report);
				connection.releaseSavepoint(savepoint);
				return false;
			}
			// 拼装sql
			String insertCellTableSql = "insert into " + hw2GCellTable + " (";
			String ws = "";
			index = 1;
			int cellTotalMark=0;//记录问号的总数
			for (String d : cellDbFieldsToTitles.keySet()) {
				if (cellDbFieldsToTitles.get(d).index >= 0) {
					cellTotalMark++;
					//log.debug("dbfield:"+d+"----index:"+index+"-------totalMark:"+totalMark);
					// 只对出现了的进行组sql
					cellDbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
					insertCellTableSql += d + ",";
					ws += "?,";
				}
			}
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			//此处也要增加相应的位置索引
//			insertCellEnginTableSql = insertCellEnginTableSql.substring(0, insertCellEnginTableSql.length()-1)+") values ( " + ws.substring(0, ws.length()-1) + " )";
			insertCellTableSql = insertCellTableSql+"HW_CELL_DESC_ID) values ( " + ws+ "?)";
			log.debug("insertCellTableSql  sql=" + insertCellTableSql);

			try {
				insertCellTStmt = connection
						.prepareStatement(insertCellTableSql);
			} catch (Exception e) {
				msg = "准备华为小区数据插入的prepareStatement失败";
				log.error("准备华为小区数据插入的prepareStatement失败！sql="
						+ insertCellTableSql);
				e.printStackTrace();
				connection.releaseSavepoint(savepoint);
				return false;
			}
			//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－两张表共用标题头分别判断  小区表结束－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－//
			//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－两张表共用标题头分别判断  邻区表开始－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－//
			//记录各邻区
			List<String> ncellSize=new ArrayList<String>();
			Map<Integer, String> ncellMap=new HashMap<Integer, String>();
			date1 = new Date();
			date2 = null;
			int ncellTimes=0;//通过查看邻区出现的次数
			index = -1;
			for (String sp : sps) {
				index++;
				find = false;
				for (DBFieldToTitle dt : ncellDbFieldsToTitles.values()) {
					// log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						
						if (dt.matchType == 1) {
							if (StringUtils.equals(dtf, sp)) {
								find = true;
								dt.setIndex(index);
								
								if(StringUtils.startsWith(sp, "邻区")){
									ncellSize.add(sp);
									ncellMap.put(index, dt.dbField);
								}else{
									ncellPois.put(index, dt.dbField);// 快速记录
								}
								System.out.println("matchType == 1:"+sp);
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
								ncellPois.put(index, dt.dbField);// 快速记录
								System.out.println("matchType == 0:"+sp);
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
			for (DBFieldToTitle dt : ncellDbFieldsToTitles.values()) {
				if (dt.isMandatory && dt.index < 0) {
					msg += "[" + dt.dbField + "在文件中未找到对应的数据]\r\n";
				}
			}
			if (!StringUtils.isBlank(msg)) {
				log.error("ncell检查csv文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				report.setFields("ncell检查文件标题", date1, date2,
						DataParseStatus.Failall.toString(), msg);
				addJobReport(report);
				connection.releaseSavepoint(savepoint);
				return false;
			}
			// 拼装sql
			String insertNcellTableSql = "insert into " + hw2GNcellTable + " (";
			ws = "";
			index = 1;
			int ncellTotalMark=0;//记录问号的总数
			for (String d : ncellDbFieldsToTitles.keySet()) {
				if (ncellDbFieldsToTitles.get(d).index >= 0) {
					ncellTotalMark++;
					//log.debug("dbfield:"+d+"----index:"+index+"-------totalMark:"+totalMark);
					// 只对出现了的进行组sql
					ncellDbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
					insertNcellTableSql += d + ",";
					ws += "?,";
				}
			}
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			//此处也要增加相应的位置索引
//			insertCellEnginTableSql = insertCellEnginTableSql.substring(0, insertCellEnginTableSql.length()-1)+") values ( " + ws.substring(0, ws.length()-1) + " )";
			
//			insertNcellTableSql = insertNcellTableSql+"HW_CELL_DESC_ID) values ( " + ws+ "?)";
		/*	insertNcellTableSql ="insert into RNO_2G_HW_NCELL (MEA_DATE,BSC_ID,CELL,NCELL,CITY_ID,HW_CELL_DESC_ID) values ( ?,?,?,?,?,?)";
			log.debug("insertNcellTableSql  sql=" + insertNcellTableSql);*/
			insertNcellTableSql ="";
			try {
				/*insertNcellTStmt = connection
						.prepareStatement(insertNcellTableSql);*/
				insertNcellTStmt=connection.createStatement();
			} catch (Exception e) {
				msg = "准备华为小区邻区数据插入的prepareStatement失败";
				log.error("准备华为小区邻区数据插入的prepareStatement失败！sql="
						+ insertNcellTableSql);
				e.printStackTrace();
				connection.releaseSavepoint(savepoint);
				return false;
			}
			//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－两张表共用标题头分别判断  邻区表结束－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－//
			//获取市区与ID的对应信息
			SysArea area=AuthDsDataDaoImpl.getSysAreaByAreaId(cityId);
			String areaName=area.getName();

			// 逐行读取数据
			int executeCnt = 0;
			int addLineCnt = 0;
			boolean handleLineOkForCell = false;
			boolean handleLineOkForNcell = false;
			long totalDataNum = 0;
			DateUtil dateUtil=new DateUtil();
			
			long descId=0;
			long length=0;
			do {
				line = reader.readLine();
				
				if (line == null) {
					break;
				}
				sps = line.split(",");
				length=sps.length;
				if (length != fieldCnt) {
					continue;
				}
				totalDataNum++;
				
				handleLineOkForCell = handleLine(sps, fieldCnt, cellPois,
						cellDbFieldsToTitles, insertCellTStmt,
						dateUtil,areaName,baseDateStr,cityId,bscToId);
				
				if (handleLineOkForCell == true) {
					executeCnt++;
					addLineCnt++;
					if(addLineCnt==1){
						descId=getNextSeqValue("SEQ_"+hw2GCellDescTable, connection);
					}
					insertCellTStmt.setLong(cellTotalMark+1, descId);
					try {
						insertCellTStmt.addBatch();
					} catch (SQLException e) {
						addLineCnt=0;
						e.printStackTrace();
					}
				}
				
				String dbVal="";
				if(ncellSize!=null && ncellSize.size()>0){
					String ncellField="";
					String ncellVal="";
					for (int ncellkey : ncellMap.keySet()) {
						ncellField=ncellMap.get(ncellkey);
						ncellVal=sps[ncellkey];
						
						insertNcellTableSql ="insert into RNO_2G_HW_NCELL (";
						ws="";
//						System.out.println(ncellField+"---------"+ncellVal);
						// 该位置对应的数据库ncell其他字段
						for(int key:ncellPois.keySet()){
							insertNcellTableSql+=ncellPois.get(key)+",";
							//System.out.println(ncellPois.get(key)+"--"+sps[key]);
							if("MEA_DATE".equals(ncellPois.get(key))){
								//判断选择日期与文件录入日期是否相同，不相同不录入
								ws+="to_date('"+sps[key]+"','yyyy-MM-dd'),";
							}else if("CITY_ID".equals(ncellPois.get(key))){
								//判断所选城市是否与文件录入城市相同，不同则忽略不录入
								ws+=String.valueOf(cityId)+",";
							}else if("BSC_ID".equals(ncellPois.get(key))){
								//判断文件的BSC名是否出现在系统中，不存在则忽略不录入
								ws+=String.valueOf(bscToId.get(sps[key]))+",";
							}else{
								
								ws+="'"+sps[key]+"',";
							}
						}
						insertNcellTableSql = insertNcellTableSql+"HW_CELL_DESC_ID,"+ncellField+") values ( " + ws+descId+",'"+ncellVal+"')";
						
						log.debug("insertNcellTableSql  sql=" + insertNcellTableSql);
						insertNcellTStmt.addBatch(insertNcellTableSql);
					}
				}
				/*if (ncellSize!=null && ncellSize.size()>0) {
					try {
						insertNcellTStmt.addBatch(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}*/
				if (executeCnt > 5000) {
					// 每5000行执行一次
					try {
						insertCellTStmt.executeBatch();
						insertCellTStmt.clearBatch();
						if (ncellSize!=null && ncellSize.size()>0) {
							insertNcellTStmt.executeBatch();
							insertNcellTStmt.clearBatch();
						}
						executeCnt = 0;
					} catch (SQLException e) {
						connection.rollback(savepoint);
						e.printStackTrace();
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				insertCellTStmt.executeBatch();
				if(ncellSize!=null && ncellSize.size()>0){
					insertNcellTStmt.executeBatch();
				}
			}

			log.debug("华为CSV数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据,共入库="+addLineCnt+"行记录");
			// ----一下进行数据处理----//
			String attMsg = "文件：" + tmpFileName;
			long t1, t2;
			Date d1 = new Date();
			
			ResultInfo resultInfo =null;
			
			Date d2 = new Date();
			
			
			if(addLineCnt!=0){
				
				String insertHw2GCellDescSql="insert into " + hw2GCellDescTable + " (HW_CELL_DESC_ID,CITY_ID,MEA_DATE,DATA_TYPE,ACCOUNT,CELL_NUM,CREATE_TIME,MOD_TIME) values("+descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),'CELLDATA','"+dataRec.getAccount()+"',"+addLineCnt+",sysdate,sysdate)";
				insertCellTStmt.execute(insertHw2GCellDescSql);
				// 一个文件一个提交
				connection.commit();
			}
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
			if (insertCellTStmt != null) {
				try {
					insertCellTStmt.close();
				} catch (SQLException e) {
				}
			}
			if (insertNcellTStmt != null) {
				try {
					insertNcellTStmt.close();
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
	 * @param insertTempStatement
	 * @param dateUtil
	 * @param areaName
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-17下午3:44:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean handleLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			PreparedStatement insertStatement,DateUtil dateUtil,String areaName,String baseDateStr,long cityId,Map<String, Integer> bscToId) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		
		if (sps == null || sps.length != expectFieldCnt) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		int bscId=0;
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
//			log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}
			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if("MEA_DATE".equals(dbField)){
				//判断选择日期与文件录入日期是否相同，不相同不录入
				//log.debug("选择日期："+baseDateStr+"－－－－－－－－－－－－－文件日期："+sps[i]);
				if(!dateUtil.isSameDay(dateUtil.parseDateArbitrary(baseDateStr), dateUtil.parseDateArbitrary(sps[i]))){
					return false;
				}
			}
			if("CITY_ID".equals(dbField)){
				//判断所选城市是否与文件录入城市相同，不同则忽略不录入
				if(!areaName.contains(sps[i])){
					return false;
				}
				if (dt != null) {
					setIntoPreStmt(insertStatement, dt, Long.toString(cityId),dateUtil);
				}
			}else{
				if (dt != null) {
					setIntoPreStmt(insertStatement, dt, sps[i],dateUtil);
				}
			}
			if("BSC_ID".equals(dbField)){
				//判断文件的BSC名是否出现在系统中，不存在则忽略不录入
				if(bscToId.get(sps[i])!=null){
					bscId=bscToId.get(sps[i]);
					if (dt != null) {
						setIntoPreStmt(insertStatement, dt, Integer.toString(bscId),dateUtil);
					}
				}else{
					return false;
				}
}
		}
		/*try {
			insertTempStatement.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}*/
		return true;
	}
	/**
	 * 
	 * @title 对预处理sql语句进行赋值处理
	 * @param pstmt
	 * @param dt
	 * @param val
	 * @param dateUtil
	 * @author chao.xj
	 * @date 2014-10-16下午6:14:37
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void setIntoPreStmt(PreparedStatement pstmt, DBFieldToTitle dt,
			String val,DateUtil dateUtil) {
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
					Date date=dateUtil
							.parseDateArbitrary(val);
					if(date!=null){
						pstmt.setTimestamp(index, new java.sql.Timestamp(date.getTime()));
					}else{
						pstmt.setNull(index, java.sql.Types.DATE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error("hwncs parse date fail:date str="+val);
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
			if (!StringUtils.isBlank(val) && StringUtils.isNumeric(val)) {
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
		}else if (StringUtils.equalsIgnoreCase("double", type)) {
			if (!StringUtils.isBlank(val)) {
				Double d = null;
				try {
					d = Double.parseDouble(val);
				} catch (Exception e) {

				}
				if (d != null) {
					try {
						pstmt.setDouble(index, d);
						return;
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				pstmt.setNull(index, Types.DOUBLE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	protected static long getNextSeqValue(String seq,Connection connection){
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
	
	public static void main(String[] args) {
		String sp="邻区1,邻区2,邻区3";
		System.out.println(StringUtils.startsWith("邻区3",sp));
		System.out.println(StringUtils.equals("邻区2", "邻区2"));
		System.out.println(StringUtils.startsWith("邻区1", "邻区"));
	}
}
