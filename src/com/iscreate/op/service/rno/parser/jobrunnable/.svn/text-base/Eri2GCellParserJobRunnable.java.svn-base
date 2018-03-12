package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.Rno2GEriCellManageService;
import com.iscreate.op.service.rno.Rno2GEriCellManageServiceImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.Eri2GCellChGroup;
import com.iscreate.op.service.rno.parser.vo.Eri2GNcellParam;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class Eri2GCellParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(Eri2GCellParserJobRunnable.class);
	private Rno2GEriCellManageService cellManageService=new Rno2GEriCellManageServiceImpl();
	
	public Eri2GCellParserJobRunnable() {
		super();
		super.setJobType("2GERICELLFILE");
	}
	// 构建insert语句
	private static String eri2GCellTable = "RNO_2G_ERI_CELL";
	private static String eri2GCellExtraTable = "RNO_2G_ERI_CELL_EXTRA_INFO";
	private static String eri2GCellChGroupTable = "RNO_2G_ERI_CELL_CH_GROUP";
	private static String eri2GNcellParamTable = "RNO_2G_ERI_NCELL_PARAM";
	private static String eri2GCellDescTable = "RNO_2G_ERI_CELL_DESC";
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
		file = FileTool.getFile(filePath);
		long dataId = dataRec.getDataCollectId();
		ResultInfo resultInfo;
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
					log.info("上传的文件是一个压缩包。");

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
							resultInfo=verifyFileDate(f.getName(), baseDate);
							if (resultInfo.isFlag()) {
								//文件格式满足要求才加入
								allFiles.add(f);
							}
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
					resultInfo=verifyFileDate(file.getName(), baseDate);
					if (resultInfo.isFlag()) {
						//文件格式满足要求才加入
						allFiles.add(file);
					}
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
				/*ResultInfo result = rnoStructAnaV2.prepareEriCityCellData(stmt,
						"RNO_CELL_CITY_T", cityId);
				if (!result.isFlag()) {
					log.error("准备分析用的小区数据时，出错！");
					// clearResource(destDir, context);
					return failWithStatus("准备分析用的小区数据时，出错时出错", new Date(), jobWorker,
							stmt, report, status, dataRec.getDataCollectId(),
							DataParseStatus.Failall, DataParseProgress.Prepare);
				}*/
				int totalFileCnt = allFiles.size();
				int i = 0;
				//获取bsc标识到ID的映射关系
				Map<String, Integer> bscToId=cellManageService.queryBscByCityId(stmt,cityId);
				//爱立信2G小区字段对应标题
				Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
				//爱立信2G小区额外信息字段对应标题
				Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
				//爱立信2G小区信道组字段对应标题
				Map<String,DBFieldToLogTitle> eri2GCellChGroupDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellChGroupToTitle.xml");
				//爱立信2G邻区参数字段对应标题
				Map<String,DBFieldToLogTitle> eri2GNcellParaDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GNcellParamToTitle.xml");
				//爱立信2G小区工参字段对应标题
				//Map<String,DBFieldToLogTitle> eri2GCellEnginParaDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellEnginParamToTitle.xml");
				for (File f : allFiles) {
					try {
						log.debug("删除某区域相同日期重复数据");
						cellManageService.rmvEri2GCellRepeatingData(stmt, baseDateStr, cityId);
						// 每一个文件的解析都应该是独立的
						if (fromZip) {
							tmpFileName = f.getName();
						}
						date1 = new Date();
				parseOk = parseEriCellLog(this, report, dataRec, tmpFileName,
						f, connection, stmt, cityId, baseDateStr,
						eri2GCellDbFieldsToTitles,
						eri2GCellExtraInfoDbFieldsToTitles,
						eri2GCellChGroupDbFieldsToTitles,eri2GNcellParaDbFieldsToTitles, bscToId);
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
				/*try {
					stmt.executeUpdate("truncate  table RNO_CELL_CITY_T");
				} catch (SQLException e) {
					e.printStackTrace();
				}*/

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

				// truncate table:RNO_CELL_CITY_T
				// sql = "truncate table RNO_CELL_CITY_T";
				// try {
				// stmt.executeUpdate(sql);
				// } catch (SQLException e) {
				// e.printStackTrace();
				// }
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return status;
	}
	/**
	 * 
	 * @title 从xml配置 文件中读取eri 2g cell 数据库到log的映射关系
	 * @param proFile
	 * @return
	 * @author chao.xj
	 * @date 2014-10-13上午11:15:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static Map<String, DBFieldToLogTitle> readDbToTitleCfgFromXml(String proFile) {  
		Map<String, DBFieldToLogTitle> dbCfgs = new TreeMap<String, DBFieldToLogTitle>();
		try {
			InputStream in =new FileInputStream(new File(Eri2GCellParserJobRunnable.class.getResource(
					proFile).getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
			  Element e = (Element) o;
			  DBFieldToLogTitle dt = new DBFieldToLogTitle();
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
					if("title".equals(key)){
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
	       return  dbCfgs;
	     }
	/**
	 * 
	 * @title 从xml配置 文件中读取eri 2g cell 数据库到log的映射关系并将登记归属表
	 * @param proFile
	 * @return
	 * @author chao.xj
	 * @date 2014-10-13上午11:15:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static Map<String, DBFieldToLogTitle> readDbToTitleCfgAndRecordTabFromXml(String proFile,String tabName) {  
		Map<String, DBFieldToLogTitle> dbCfgs = new TreeMap<String, DBFieldToLogTitle>();
		try {
			InputStream in =new FileInputStream(new File(Eri2GCellParserJobRunnable.class.getResource(
					proFile).getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
			  Element e = (Element) o;
			  DBFieldToLogTitle dt = new DBFieldToLogTitle();
			  dt.setTabName(tabName);
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
					if("title".equals(key)){
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
	       return  dbCfgs;
	     }
	public static class DBFieldToLogTitle {
		String dbField;
		int matchType;// 0：模糊匹配，1：精确匹配
		int index = -1;// 在文件中出现的位置，从0开始
		boolean isMandatory = true;// 是否强制要求出现
		private String dbType;// 类型，Number，String,Date
		List<String> titles = new ArrayList<String>();

		int sqlIndex = -1;// 在sql语句中的位置
		String dbVal;//字段对应的值
		private String tabName;
		public String getTabName() {
			return tabName;
		}

		public void setTabName(String tabName) {
			this.tabName = tabName;
		}

		public String getDbVal() {
			return dbVal;
		}

		public void setDbVal(String dbVal) {
			this.dbVal = dbVal;
		}

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
			return "DBFieldToLogTitle [dbField=" + dbField + ", matchType="
					+ matchType + ", index=" + index + ", isMandatory="
					+ isMandatory + ", dbType=" + dbType + ", titles=" + titles
					+ ", sqlIndex=" + sqlIndex + ", dbVal=" + dbVal + "]";
		}

		
	}
	public boolean parseEriCellLog(JobRunnable jobWorker, JobReport report,RnoDataCollectRec dataRec,
			String tmpFileName, File f, Connection connection, Statement stmt,
			long cityId,String baseDateStr,Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles,Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles,Map<String,DBFieldToLogTitle> eri2GCellChGroupDbFieldsToTitles,Map<String,DBFieldToLogTitle> eri2GNcellParaDbFieldsToTitles,Map<String, Integer> bscToId) {
		String spName = "everyStartHw" + System.currentTimeMillis();
		Savepoint savepoint = null;
		try {
			savepoint = connection.setSavepoint(spName);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		Statement insertEriCellStmt = null;
		Statement insertEriCellExtraStmt = null;
		Statement insertEriCellChGroupStmt = null;
		Statement insertEriNcellParamStmt = null;
		
		Statement insertEriCellDescStmt = null;
		try {
			insertEriCellStmt = connection
					.createStatement();
			insertEriCellExtraStmt = connection
			.createStatement();
			insertEriCellChGroupStmt = connection
			.createStatement();
			insertEriNcellParamStmt = connection
			.createStatement();
			insertEriCellDescStmt = connection
			.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String insertEriCellSql ="";
		String insertEriCellExtraSql ="";
		String insertEriCellChGroupSql ="";
		String insertEriNcellParamSql ="";
		
		long st = System.currentTimeMillis();

		Date date1 = new Date();
		Date date2 = null;
		// 读取文件
		BufferedReader reader = null;
		String charset = null;
		System.out.println("位置："+f.getAbsolutePath());
//	    charset = FileTool.getFileEncode(f.getAbsolutePath());
		charset="utf-8";
		//String f="D:/软件开发/资料/log/eri2gcelltest.log";
		//charset = FileTool.getFileEncode(f.getAbsolutePath());
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
			/*reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), charset));*/
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
				log.debug("line:"+line);
				if(line==null){
					sps=new String[]{};
					break;
				}
				
				lineHead=line;
				sps = line.split("\t");
				if(sps.length<1000){
					sps = line.split(" ");
				}
				log.debug("sps大小："+sps.length);
			} while (sps == null);// || sps.length>2000
			// 读取到标题头
			int fieldCnt = sps.length;
			log.debug("eri cell log文件：" + tmpFileName + ",title 为：" + line
					 + ",有" + fieldCnt + "标题");
			if (line == null) {
				log.error("读取文件：" + tmpFileName + "失败！");
				date2 = new Date();
				report.setFields("读取文件内容", date1, date2,
						DataParseStatus.Failall.toString(), "读取文件：" + tmpFileName
								+ "失败,其中title 行为：" + line);
				report.setAttMsg("读取文件：" + tmpFileName+ "失败,其中title 行为：" + line);
				addJobReport(report);
				return false;
			}
			int index=-1;
			//先记住整个标题的位置，
			Map<Integer, String> totalIndexToField=new TreeMap<Integer, String>();
			//再记住各结点的标识位置
			//1,小区参数 NW          ch_group_0
			int firstChGroupIndex=0;
			//2,信道组     ch_group_0	 n_cell_0
			int firstNcellIndex=0;
			//3,邻区参数 n_cell_0	 un_cell_0
			int firstUncellIndex=0;
			for (String key : sps) {
				index++;
				if("ch_group_0".equals(key)){
					firstChGroupIndex=index;
				}
				if("n_cell_0".equals(key)){
					firstNcellIndex=index;
				}
				if("un_cell_0".equals(key)){
					firstUncellIndex=index;
				}
				//将标题转换成大写
				totalIndexToField.put(index, key.toUpperCase());
			}
			//System.out.println("totalIndexToField--:"+totalIndexToField);
			//标题头变量
			String titleHead;
			//记录有前缀名下划线的变量
			String prefixName="";
			//记录有前缀值下划线的变量,逗号分割
			String prefixVal="";
			//记录有前缀下划线的中间变量
			String interPrefixName="";
			//字段到值
			//爱立信2G小区
			DBFieldToLogTitle dbFieldToLogTitleForEricell;
			//爱立信2G小区额外信息
			DBFieldToLogTitle dbFieldToLogTitleForEricellExtra;
			//爱立信2G小区信道组
			//Map<String, List<Eri2GCellChGroup>> cellChGroupMap=new TreeMap<String, List<Eri2GCellChGroup>>();
			List<Eri2GCellChGroup> cellChGroupList=new ArrayList<Eri2GCellChGroup>();
			Eri2GCellChGroup eri2gCellChGroup=null;
			//爱立信2G小区邻区
			//Map<String, List<Eri2GNcellParam>> nellParamMap=new TreeMap<String, List<Eri2GNcellParam>>();
			List<Eri2GNcellParam> ncellParamList=new ArrayList<Eri2GNcellParam>();
			Eri2GNcellParam eri2gNcellParam=null;
			
			Map<String, Object> keyValMap;
			int count=0;
			// 逐行读取数据
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			long addLineNum=0;
			//先记录下每行的MSC与BSC
			String msc="";
			int bscId=0;
			String cell="";
			boolean flag=true;//该区域是否有该 BSC
			long descId=getNextSeqValue("SEQ_"+eri2GCellDescTable, connection);
			do {
				line = reader.readLine();
				
				if(line==null){
					sps=new String[]{};
					break;
				}
//				System.out.println("----line:"+line);
				lineHead=line;
				index=-1;
				sps = line.split("\t");
				if(sps.length<1000){
					sps = line.split(" ");
					if(sps.length<1000){
						continue;
					}
				}
				totalDataNum++;
				for (String fieldVal : sps) {
					index++;
					//通过索引获取标题头
					titleHead=totalIndexToField.get(index);
					if(index<firstChGroupIndex){
						//分两张表：小区参数经常用/小区数据不经常用
						if(titleHead.contains("_") && isNum(titleHead.substring(titleHead.lastIndexOf("_")+1))){
							prefixName=titleHead.substring(0, titleHead.lastIndexOf("_"));
							if (interPrefixName.isEmpty()) {
								//是空的说明是上个字段不含下划线
								interPrefixName=titleHead;
								prefixVal="";
								prefixVal+=sps[index]+",";
							}else{
								if (prefixName.equals(interPrefixName.substring(0, interPrefixName.lastIndexOf("_")))) {
									//不空并且本字段前缀与上次前缀相同，则归为一类
									interPrefixName=titleHead;
									prefixVal+=sps[index]+",";
								}else{
									//不空并且本字段前缀与上次前缀不同，则是另一个含有下划线字段的开始
									count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1))+1;
									titleHead=interPrefixName.substring(0, interPrefixName.lastIndexOf("_"))+"_"+count;
									prefixVal=prefixVal.substring(0, prefixVal.length()-1);
									dbFieldToLogTitleForEricell=eri2GCellDbFieldsToTitles.get(titleHead);
									if(dbFieldToLogTitleForEricell!=null){
										dbFieldToLogTitleForEricell.setDbVal(prefixVal);
									}
									dbFieldToLogTitleForEricellExtra=eri2GCellExtraInfoDbFieldsToTitles.get(titleHead);
									if(dbFieldToLogTitleForEricellExtra!=null){
										dbFieldToLogTitleForEricellExtra.setDbVal(prefixVal);
									}
//									System.out.println(titleHead+"含下划线="+prefixVal);
									//重新置空
									prefixVal="";
									interPrefixName="";
								}
							}
						}else{
							if (!interPrefixName.isEmpty()) {
								//不同的字段都含下划线且是不连续的
								count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1));
								//如果只有一个后缀0则不加1
								if(count>0){
									count=count+1;
								}
								interPrefixName=interPrefixName.substring(0, interPrefixName.lastIndexOf("_"))+"_"+count;
								prefixVal=prefixVal.substring(0, prefixVal.length()-1);
								dbFieldToLogTitleForEricell=eri2GCellDbFieldsToTitles.get(interPrefixName);
								if(dbFieldToLogTitleForEricell!=null){
									dbFieldToLogTitleForEricell.setDbVal(prefixVal);
								}
								dbFieldToLogTitleForEricellExtra=eri2GCellExtraInfoDbFieldsToTitles.get(interPrefixName);
								if(dbFieldToLogTitleForEricellExtra!=null){
									dbFieldToLogTitleForEricellExtra.setDbVal(prefixVal);
								}
//								System.out.println(interPrefixName+"含下划线---="+prefixVal);
							}
							
							//重新置空
							prefixVal="";
							interPrefixName="";
							prefixVal=sps[index];
							if("MSC".equals(titleHead)){
								msc=prefixVal;
							}
							if("BSC".equals(titleHead)){
								
								if(bscToId.get(prefixVal)!=null){
									bscId=bscToId.get(prefixVal);
									prefixVal=String.valueOf(bscId);
								}else{
									//跳出循环下一行
									flag=false;
									break;
								}
							}
							if("CELL".equals(titleHead)){
								cell=prefixVal;
								if(cell==null||"NULL".equals(cell)){
									//跳出循环下一行
									flag=false;
									break;
								}
							}
//							System.out.println(titleHead+"不含下划线="+prefixVal);
							dbFieldToLogTitleForEricell=eri2GCellDbFieldsToTitles.get(titleHead);
							if(dbFieldToLogTitleForEricell!=null){
								dbFieldToLogTitleForEricell.setDbVal(prefixVal);
							}
							dbFieldToLogTitleForEricellExtra=eri2GCellExtraInfoDbFieldsToTitles.get(titleHead);
							if(dbFieldToLogTitleForEricellExtra!=null){
								dbFieldToLogTitleForEricellExtra.setDbVal(prefixVal);
							}
						}
					}
					if(firstNcellIndex>index && index>=firstChGroupIndex){
						//小区信道组表 共16个循环组  CH_GROUP_0~15
						if(titleHead.contains("_") && "CH_GROUP".equals(titleHead.substring(0, titleHead.lastIndexOf("_")))){
//							System.out.println(index+"- 小区信道组--＝＝＝＝＝＝＝"+titleHead);
							prefixName=titleHead.substring(0, titleHead.lastIndexOf("_")).toUpperCase();
							prefixVal=fieldVal;
							//System.out.println(titleHead+"信道－"+prefixVal);
							if(eri2gCellChGroup!=null){
								cellChGroupList.add(eri2gCellChGroup);
							}
							//新创建信道组对象
							eri2gCellChGroup=new Eri2GCellChGroup();
							eri2gCellChGroup.put(prefixName, prefixVal);
						}else if(titleHead.contains("_") && isNum(titleHead.substring(titleHead.lastIndexOf("_")+1))){
							prefixName=titleHead.substring(0, titleHead.lastIndexOf("_"));
							if (interPrefixName.isEmpty()) {
								//是空的说明是上个字段不含下划线
								interPrefixName=titleHead;
								prefixVal="";
								prefixVal+=sps[index]+",";
							}else{
								if (prefixName.equals(interPrefixName.substring(0, interPrefixName.lastIndexOf("_")))) {
									//不空并且本字段前缀与上次前缀相同，则归为一类
									interPrefixName=titleHead;
									prefixVal+=sps[index]+",";
								}else{
									//不空并且本字段前缀与上次前缀不同，则是另一个含有下划线字段的开始
									count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1))+1;
									titleHead=interPrefixName.substring(0, interPrefixName.lastIndexOf("_"))+"_"+count;
									prefixVal=prefixVal.substring(0, prefixVal.length()-1);
									eri2gCellChGroup.put(titleHead, prefixVal);
//									System.out.println(titleHead+"含下划线="+prefixVal);
									//重新置空
									prefixVal="";
									interPrefixName="";
								}
							}
						}else{
							if (!interPrefixName.isEmpty()) {
								//不同的字段都含下划线且是不连续的
								count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1));
								//如果只有一个后缀0则不加1
								if(count>0){
									count=count+1;
								}
								interPrefixName=interPrefixName.substring(0, interPrefixName.lastIndexOf("_"))+"_"+count;
								prefixVal=prefixVal.substring(0, prefixVal.length()-1);
								eri2gCellChGroup.put(interPrefixName, prefixVal);
//								System.out.println(interPrefixName+"含下划线="+prefixVal);
							}
							
							//重新置空
							prefixVal="";
							interPrefixName="";
							prefixVal=sps[index];
							eri2gCellChGroup.put(titleHead, prefixVal);
//							System.out.println(titleHead+"不含下划线－－－="+prefixVal);
						}
					}
					if(firstNcellIndex<=index && index<firstUncellIndex){
						//最后一个ch_group要加上
						if(eri2gCellChGroup!=null){
							cellChGroupList.add(eri2gCellChGroup);
							eri2gCellChGroup=null;
						}
						//小区邻区表 共64个循环  N_CELL_0~63
						//小区邻区参数表
						if(titleHead.contains("_") && "N_CELL".equals(titleHead.substring(0, titleHead.lastIndexOf("_")))){
//							System.out.println(index+"- 邻区组--＝＝＝＝＝＝＝"+titleHead);
							prefixVal=fieldVal;
							prefixName=titleHead.substring(0, titleHead.lastIndexOf("_")).toUpperCase();
//							System.out.println(titleHead+"邻区－"+prefixVal);
							//Map<String, List<E>>
							//新创建信道组对象
							if(eri2gNcellParam!=null){
								ncellParamList.add(eri2gNcellParam);
							}
							eri2gNcellParam=new Eri2GNcellParam();
							eri2gNcellParam.put(prefixName, prefixVal);
						}else if(titleHead.contains("_") && isNum(titleHead.substring(titleHead.lastIndexOf("_")+1))){
							prefixName=titleHead.substring(0, titleHead.indexOf("_"));
							if (interPrefixName.isEmpty()) {
								//是空的说明是上个字段不含下划线
								interPrefixName=titleHead;
								prefixVal="";
								prefixVal+=sps[index]+",";
							}else{
								if (prefixName.equals(interPrefixName.substring(0, interPrefixName.lastIndexOf("_")))) {
									//不空并且本字段前缀与上次前缀相同，则归为一类
									interPrefixName=titleHead;
									prefixVal+=sps[index]+",";
								}else{
									//不空并且本字段前缀与上次前缀不同，则是另一个含有下划线字段的开始
									count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1))+1;
									titleHead=interPrefixName.substring(0, interPrefixName.indexOf("_"))+"_"+count;
									prefixVal=prefixVal.substring(0, prefixVal.length()-1);
									eri2gNcellParam.put(titleHead, prefixVal);
//									System.out.println(titleHead+"含下划线="+prefixVal);
									//重新置空
									prefixVal="";
									interPrefixName="";
								}
							}
						}else{
							if (!interPrefixName.isEmpty()) {
								//不同的字段都含下划线且是不连续的
								count=Integer.parseInt(interPrefixName.substring(interPrefixName.lastIndexOf("_")+1))+1;
								interPrefixName=interPrefixName.substring(0, interPrefixName.lastIndexOf("_"))+"_"+count;
								prefixVal=prefixVal.substring(0, prefixVal.length()-1);
								eri2gNcellParam.put(interPrefixName, prefixVal);
//								System.out.println(interPrefixName+"含下划线---="+prefixVal);
							}
							
							//重新置空
							prefixVal="";
							interPrefixName="";
							prefixVal=sps[index];
							eri2gNcellParam.put(titleHead, prefixVal);
//							System.out.println(titleHead+"不含下划线="+prefixVal);
						}
					}
					if(index>=firstUncellIndex){
						//后面的已经去掉不考虑，跳出此行循环
						//System.out.println("跳出循环。。。。");
						if(eri2gNcellParam!=null){
							ncellParamList.add(eri2gNcellParam);
							eri2gNcellParam=null;
						}
						break;
					}
				}
				//系统中不存在该BSC信息则不须录入
				if(!flag){
					flag=true;
					continue;
				}
				addLineNum++;//记录满足要求的行数
//				System.out.println("----sps大小："+sps.length);
				// 拼装2G小区sql
				insertEriCellSql = "insert into " + eri2GCellTable + " (ERI_CELL_DESC_ID,CITY_ID,MEA_DATE,";
				String field="";
				String dbval=descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),";
				String ws = "";
				index = 1;
				for (String key : eri2GCellDbFieldsToTitles.keySet()) {
					//System.out.println(key+"==="+eri2GCellDbFieldsToTitles.get(key));
					String val=eri2GCellDbFieldsToTitles.get(key).dbVal;
					if(val!=null){
						val=val.replaceAll(",NULL", "");
						if(val.equals("NULL")){
							val=val.replace("NULL", "");
						}
					}
					if("NULL".equals(val)){
						dbval+=val+",";
					}else{
						dbval+="'"+val+"',";
					}
					if("TO".equals(key)){
						key="\""+key+"\"";
					}
					field+=key+",";
				}
				insertEriCellSql=insertEriCellSql+field.substring(0, field.length()-1)+") values("+dbval.substring(0, dbval.length()-1)+")";
				insertEriCellStmt.addBatch(insertEriCellSql);
				
				// 拼装2G小区额外信息sql
				insertEriCellExtraSql = "insert into " + eri2GCellExtraTable + " (ERI_CELL_DESC_ID,CITY_ID,MEA_DATE,";
				dbval=descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),";
				ws = "";
				index = 1;
				field="";
				for (String key : eri2GCellExtraInfoDbFieldsToTitles.keySet()) {
					//System.out.println(key+"==="+eri2GCellDbFieldsToTitles.get(key));
					String val=eri2GCellExtraInfoDbFieldsToTitles.get(key).dbVal;
					if(val!=null){
						val=val.replaceAll(",NULL", "");
						if(val.equals("NULL")){
							val=val.replace("NULL", "");
						}
					}
					
					if("NULL".equals(val)){
						dbval+=val+",";
					}else{
						dbval+="'"+val+"',";
					}
					field+=key+",";
				}
				insertEriCellExtraSql=insertEriCellExtraSql+field.substring(0, field.length()-1)+") values("+dbval.substring(0, dbval.length()-1)+")";
//				System.out.println("额外信息："+insertEriCellExtraSql);
				insertEriCellExtraStmt.addBatch(insertEriCellExtraSql);
				//拼装小区信道组sql
				boolean validChGroup=true;
				for (int i = 0; i < cellChGroupList.size(); i++) {
					insertEriCellChGroupSql = "insert into " + eri2GCellChGroupTable + " (ERI_CELL_DESC_ID,CITY_ID,MEA_DATE,MSC,BSC,CELL,";
					dbval=descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),'"+msc+"',"+bscId+",'"+cell+"',";
					ws = "";
					field="";
					eri2gCellChGroup=cellChGroupList.get(i);
					keyValMap=eri2gCellChGroup.getEri2GCellChGroup();
					for (String key : keyValMap.keySet()) {
						if(eri2GCellChGroupDbFieldsToTitles.get(key)==null){
							/*validChGroup=false;
							break;*/
							/*若该字段在数据库中不存在则跳过*/
							continue;
						}
						String val=keyValMap.get(key).toString();
						if(val!=null){
							val=val.replaceAll(",NULL", "");
						}
						if("CH_GROUP".equals(key)){
							
							//System.out.println(key+"==="+val+" is "+ (val.equals("NULL"))+"－－或－－"+(val.equals("NULL")));
						}
						if("CH_GROUP".equals(key)&&val.equals("NULL")){
							//chgroup的值为NULL时不入库
							validChGroup=false;
							break;
						}
						if("NULL".equals(val)){
							dbval+=val+",";
						}else{
							dbval+="'"+val+"',";
						}
						
						field+=key+",";
					}
					if(!validChGroup){
						validChGroup=true;
						continue;
					}
					insertEriCellChGroupSql=insertEriCellChGroupSql+field.substring(0, field.length()-1)+") values("+dbval.substring(0, dbval.length()-1)+")";
					insertEriCellChGroupStmt.addBatch(insertEriCellChGroupSql);
//					System.out.println("信道信息："+insertEriCellChGroupSql);
				}
				//每行用完后清空
				cellChGroupList.clear();
				
				//拼装邻区参数sql
				boolean validNcell=true;
				for (int i = 0; i < ncellParamList.size(); i++) {
					insertEriNcellParamSql = "insert into " + eri2GNcellParamTable + " (ERI_CELL_DESC_ID,CITY_ID,MEA_DATE,MSC,BSC,CELL,";
					dbval=descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),'"+msc+"',"+bscId+",'"+cell+"',";
					ws = "";
					field="";
					eri2gNcellParam=ncellParamList.get(i);
					keyValMap=eri2gNcellParam.getEri2GNcellParam();
					for (String key : keyValMap.keySet()) {
						if(eri2GNcellParaDbFieldsToTitles.get(key)==null){
							/*validNcell=false;
							break;*/
							/*若该字段在数据库中不存在则跳过*/
							continue;
						}
						String val=keyValMap.get(key).toString();
						if(val!=null){
							val=val.replaceAll(",NULL", "");
						}
						if("N_CELL".equals(key)&&val.equals("NULL")){
							//N_CELL的值为NULL时不入库
							validNcell=false;
							break;
						}
						if("NULL".equals(val)){
							dbval+=val+",";
						}else{
							dbval+="'"+val+"',";
						}
						field+=key+",";
					}
					if(!validNcell){
						validNcell=true;
						continue;
					}
					
					insertEriNcellParamSql=insertEriNcellParamSql+field.substring(0, field.length()-1)+") values("+dbval.substring(0, dbval.length()-1)+")";
					insertEriNcellParamStmt.addBatch(insertEriNcellParamSql);
//					System.out.println("邻区信息："+insertEriNcellParamSql);
				}
				//每行用完后清空
				ncellParamList.clear();
				executeCnt++;
				if (executeCnt > 5000) {
					// 每5000行执行一次
					try {
						insertEriCellStmt.executeBatch();
						insertEriCellStmt.clearBatch();
						insertEriCellExtraStmt.executeBatch();
						insertEriCellExtraStmt.clearBatch();
						insertEriCellChGroupStmt.executeBatch();
						insertEriCellChGroupStmt.clearBatch();
						insertEriNcellParamStmt.executeBatch();
						insertEriNcellParamStmt.clearBatch();
						executeCnt = 0;
					} catch (SQLException e) {
						connection.rollback(savepoint);
						addLineNum=0;
						e.printStackTrace();
					}
				}
			} while (!line.isEmpty());// || sps.length>2000
			// 执行
			if (executeCnt > 0) {
				insertEriCellStmt.executeBatch();
				insertEriCellExtraStmt.executeBatch();
				insertEriCellChGroupStmt.executeBatch();
				insertEriNcellParamStmt.executeBatch();
			}
			log.debug("总记录数为："+totalDataNum+"----满足要求的行数："+addLineNum);
			if(addLineNum!=0){
				
				String insertEri2GCellDescSql="insert into " + eri2GCellDescTable + " (ERI_CELL_DESC_ID,CITY_ID,MEA_DATE,DATA_TYPE,ACCOUNT,CELL_NUM,CREATE_TIME,MOD_TIME) values("+descId+","+cityId+",to_date('"+baseDateStr+"','yyyy-MM-dd'),'CELLDATA','"+dataRec.getAccount()+"',"+addLineNum+",sysdate,sysdate)";
				insertEriCellDescStmt.execute(insertEri2GCellDescSql);
				// 一个文件一个提交
				connection.commit();
			}else {
				date2 = new Date();
				report.setFields("收集满足要求的行", date1, date2,
						DataParseStatus.Failall.toString(), "文件：" + tmpFileName
								+ "满足要求的行数："+addLineNum);
				report.setAttMsg("文件：" + tmpFileName+ "满足要求的行数："+addLineNum);
				addJobReport(report);
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback(savepoint);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}finally {
			try {
				reader.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (insertEriCellStmt!=null) {
				try {
					insertEriCellStmt.close();
				} catch (SQLException e) {
				}
			}
			if (insertEriCellChGroupStmt!=null) {
				try {
					insertEriCellChGroupStmt.close();
				} catch (SQLException e) {
				}
			}
			if (insertEriCellExtraStmt!=null) {
				try {
					insertEriCellExtraStmt.close();
				} catch (SQLException e) {
				}
			}
			if (insertEriNcellParamStmt!=null) {
				try {
					insertEriCellStmt.close();
				} catch (SQLException e) {
				}
			}
		}

		return true;
	}
	/**
	 * 
	 * @title 是否是数字
	 * @param str
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15下午3:19:39
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	/**
	 * 
	 * @title 核实文件日期
	 * @param filestr
	 * @param baseDate
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15下午3:21:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public ResultInfo verifyFileDate(String filestr,Date baseDate){
		
		ResultInfo resultInfo=new ResultInfo();
		String fileArr[]=filestr.split(" ");
		if(fileArr.length!=2){
			resultInfo.setFlag(false);
			resultInfo.setMsg("文件格式不符合命名规范");
		}
		DateUtil dateUtil=new DateUtil();
		if(dateUtil.isValidDate(fileArr[0])){
			if(dateUtil.isSameDay(dateUtil.to_yyyyMMddDate(fileArr[0]), baseDate)){
				resultInfo.setFlag(true);
				resultInfo.setMsg("文件日期命名符合规范要求");
			}else {
				resultInfo.setFlag(false);
				resultInfo.setMsg("文件日期与所选日期不相符，不是同一天");
			}
		}else{
			resultInfo.setFlag(false);
			resultInfo.setMsg("文件日期不是有效的日期格式");
		}
		return resultInfo;
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
		if(jobStatus==null){
			return;
		}
		if(stmt!=null){
			try {
				String prog=jobStatus.getProgress();
				if(prog==null){
					prog="";
				}
				prog=prog.trim();
				if("".equals(prog)){
					prog=jobStatus.getJobState().getCode();
				}
				String str="update RNO_DATA_COLLECT_REC set file_status='"+prog+"' where job_id="+jobStatus.getJobId();
				stmt.executeUpdate(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {
		/*Map<String, Integer> bscToId=cellManageService.queryBscByCityId(89);
		//爱立信2G小区字段对应标题
		Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
		//爱立信2G小区额外信息字段对应标题
		Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
		*///parse(eri2GCellDbFieldsToTitles,eri2GCellExtraInfoDbFieldsToTitles,bscToId);
		
		/*for (String key : eri2GCellDbFieldsToTitles.keySet()) {
			System.out.println(key+"==="+eri2GCellDbFieldsToTitles.get(key));
		}*/
		//cellManageService.queryBscByCityId(88);
		String pathname="E:\\myjava\\apache-tomcat-6.0.37\\webapps\\ops\\op\\rno\\upload\\2014-10-15\\b981ca3f43c34024a373e6d1a8916fa2\\2014-10-15 allparameters_深圳.log";
		/*File file=new File(pathname);
		String filestr=file.getName();
		Date date=new Date();*/
		//pathname="D:\\软件开发\\资料\\log\\2014-10-15 eri2gcelltest.log";
		String chrest=FileTool.getFileEncode(pathname);
		System.out.println("编码："+chrest);
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
