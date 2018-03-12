package com.iscreate.op.service.rno.parser.jobrunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.iscreate.op.dao.rno.RnoStructAnaV2;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.FasParserContext;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.EriFasAdmRecord;
import com.iscreate.op.service.rno.parser.vo.EriFasCellData;
import com.iscreate.op.service.rno.parser.vo.EriFasRecord;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.TranslateTools;
import com.iscreate.op.service.rno.tool.ZipFileHandler;

public class EriFasParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(EriFasParserJobRunnable.class);

	private static int AdministratorRecordLen = 52;

	private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();
	// 语句
	// 爱立信fas文件描述信息
	private static String insertEriFasDescSql = "insert into RNO_ERI_FAS_DESC (FAS_DESC_ID,CITY_ID,MEA_TIME,BSC,RECORD_NUM,CREATE_TIME) values (?,?,?,?,?,?)";
	// 管理头部信息
	private static String insertEriFasAdmTempSql = "insert into RNO_ERI_FAS_ADM_T(FILE_FORMAT,MEA_TIME,REC_INFO,RID,TOTAL_TIME,PERCENT_VAL,CITY_ID) values (?,?,?,?,?,?,?)";
	// 小区数据信息
	private static String insertEriFasCellDataTempSql = "insert into RNO_ERI_FAS_CELL_DATA_T(CELL,FREQ_NUM,ARFCN_1_150,AVMEDIAN_1_150,AVPERCENTILE_1_150,NOOFMEAS_1_150,CITY_ID) values (?,?,?,?,?,?,?)";

	public EriFasParserJobRunnable() {
		super();
		super.setJobType("ERICSSONFASFILE");
	}

	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {

		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

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
		List<File> allFasFiles = new ArrayList<File>();// 将所有待处理的fas文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2;
		if (fileName.endsWith(".zip") || fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")) {
			date1 = new Date();
			fromZip = true;
			// 压缩包
			log.info("上传的fas文件是一个压缩包。");

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
					allFasFiles.add(f);
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
			log.info("上传的fas是一个普通文件。");
			allFasFiles.add(file);
		}

		if (allFasFiles.isEmpty()) {
			msg = "未上传有效的fas文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的fas文件！注意zip包里不能再包含有文件夹！");
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
		FasParserContext context = new FasParserContext();
		// 设置必要的信息
		// 1、设置statment
		PreparedStatement insertEriFasDescStmt = null;
		try {
			insertEriFasDescStmt = connection.prepareStatement(insertEriFasDescSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=fas-1";
			log.error(msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信fas描述信息相关数据库资源时出错", new Date(),
					this, stmt, report, status,
					dataRec.getDataCollectId(), DataParseStatus.Failall,
					DataParseProgress.Prepare);
		}
		context.setPreparedStatment("EriFasDesc", insertEriFasDescStmt);

		PreparedStatement insertEriFasAdmTempStmt = null;
		try {
			insertEriFasAdmTempStmt = connection.prepareStatement(insertEriFasAdmTempSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=fas-2";
			log.error(msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信fas管理头部信息相关数据库资源时出错", new Date(),
					this, stmt, report, status,
					dataRec.getDataCollectId(), DataParseStatus.Failall,
					DataParseProgress.Prepare);
		}
		context.setPreparedStatment("EriFasAdm", insertEriFasAdmTempStmt);
		
		PreparedStatement insertEriFasCellDataTempStmt = null;
		try {
			insertEriFasCellDataTempStmt = connection.prepareStatement(insertEriFasCellDataTempSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "数据处理出错！code=fas-3";
			log.error(msg);
			clearResource(destDir, context);
			return failWithStatus("准备爱立信fas管理头部信息相关数据库资源时出错", new Date(),
					this, stmt, report, status,
					dataRec.getDataCollectId(), DataParseStatus.Failall,
					DataParseProgress.Prepare);
		}
		context.setPreparedStatment("EriFasCellData", insertEriFasCellDataTempStmt);
		
		// 2、设置cityId
		context.setCityId(cityId);

		String tmpFileName = fileName;
		int sucCnt = 0;
		boolean parseOk = false;
		int totalFileCnt = allFasFiles.size();
		int i = 0;
		
		for (File f : allFasFiles) {

				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				i++;
				//文件解析
				try {
					parseOk = parseFas(this, report, stmt, tmpFileName, f, context,
							connection);
				} catch (Exception e) {
					e.printStackTrace();
					parseOk = false;
					date2 = new Date();
					msg = tmpFileName + "文件解析出错！";
					log.error(msg);
					
					report.setReportType(1);
					report.setStage("文件处理总结");
					report.setBegTime(date1);
					report.setEndTime(date2);
					report.setFinishState(DataParseStatus.Failall.toString());
					report.setAttMsg("文件解析出错（"+i+"/"+totalFileCnt+"）:" + tmpFileName);
					addJobReport(report);
					continue;
				}
	
				date2 = new Date();
				report.setReportType(1);
				report.setStage("文件处理总结");
				if(parseOk){
					report.setFinishState(DataParseStatus.Succeded.toString());
				}else{
					report.setFinishState(DataParseStatus.Failall.toString());
				}
				report.setBegTime(date1);
				report.setEndTime(date2);
				if (parseOk) {
					report.setAttMsg("成功文件（"+i+"/"+totalFileCnt+"）:" + tmpFileName);
					sucCnt++;
				}else{
					report.setAttMsg("失败文件（"+i+"/"+totalFileCnt+"）:" + tmpFileName);
				}
				addJobReport(report);
		}
		
		if (sucCnt > 0) {
			status.setJobState(JobState.Finished);
			status.setUpdateTime(new Date());
		} else {
			status.setJobState(JobState.Failed);
			status.setUpdateTime(new Date());
		}

		if (sucCnt == allFasFiles.size()) {
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

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		clearResource(destDir, context);
		return status;		
	}

	/**
	 * 清理资源
	 * @param destDir
	 * @param context
	 * @author peng.jm
	 * @date 2014-7-18上午11:31:00
	 */
	private void clearResource(String destDir, FasParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeAllStatement();
		}
	}

	private boolean parseFas(JobRunnable jobWorker, JobReport report,
			Statement statement, String fileName, File file,
			FasParserContext context, Connection connection){
		
		InputStream is;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		byte[] typeByte = new byte[1];
		byte[] lenByte = new byte[2];
		byte[] content = new byte[2048];
		
		// 先把相关的数据都clear
		context.clearBatchAllStatement();
		
		String attMsg = "文件:" + fileName;
		Date d1 = new Date(), d2;

		long fasId = -1;
		int len = -1;
		int type = -1;
		EriFasRecord rec = null;
		
		Date meaTime = new Date();

		try {
			while ((len = is.read(typeByte, 0, 1)) != -1) {
				type = -1;
				len = is.read(lenByte, 0, 2);
				if (len != 2) {
					log.error("异常中断或格式错误！");
					context.appedErrorMsg("文件:[" + fileName
							+ "]异常中断或格式错误！<br/>");
					break;
				}
				len = TranslateTools.makeIntFromByteArray(lenByte, 0, 2);

				if (len <= 0) {
					//log.error("长度信息有误！");
					context.appedErrorMsg("文件:[" + fileName
							+ "]读取完成，或长度信息有误<br/>");
					break;
				}
				is.read(content, 0, len - 3);// 减去type占用的1个字节，length内容占用的2个字节
				byte[] wholeSection = TranslateTools.mergeByte(typeByte, lenByte,
						TranslateTools.subByte(content, 0, len - 3));

				type = TranslateTools.makeIntFromByteArray(typeByte, 0, 1); 
				rec = handleSection(type, wholeSection);
				
				// 处理rec
				if (rec != null) {
					//文件是否有效数据
					if(rec.getRecType() == 40) {
						//获取测量日期
						EriFasAdmRecord eriFasAdmRecord = (EriFasAdmRecord) rec;
						meaTime = eriFasAdmRecord.getMeaTime(context.getDateUtil());
						//保存测量日期
						context.setMeaDate(context.getDateUtil()
								.format_yyyyMMddHHmmss(meaTime));
						//System.out.println(context.getMeaDate());
						//获取数据量
						context.setRecInfo(eriFasAdmRecord.getRecInfo());
					}
					
					if (fasId == -1) {
						// 申请id
						fasId = RnoHelper.getNextSeqValue("SEQ_ERI_FAS_DESCRIPTOR",
								statement);
					}
					// 转换成sql语句
					prepareRecordSql(fasId, rec, context);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		//关闭流
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// --报告--//
		d2 = new Date();
		report.setSystemFields(DataParseProgress.Decode.toString(), d1, d2,
				DataParseStatus.Succeded.toString(), attMsg);
		addJobReport(report);
		
		//数据处理
		boolean result = false;
		if(fasId != -1) {
			try {
				result = dataProcess(this, report, fileName, fasId,
						context, connection, statement, d1, d2, attMsg);
				if(!result) {
					//清除批命令
					context.clearBatchAllStatement();
					return false;	
				}
			} catch (Exception e) {
				e.printStackTrace();
				//清除批命令
				context.clearBatchAllStatement();
				return false;
			}
		} else {
			log.error("数据信息有误！");
		}
		
		// 一个文件一个提交
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return result;
	}

	
	private boolean dataProcess(
			EriFasParserJobRunnable eriFasParserJobRunnable, JobReport report,
			String fileName, long fasId,
			FasParserContext context, Connection connection,
			Statement statement, Date d1, Date d2, String attMsg) throws Exception {
		
		/**** 插入临时表信息 ****/
		log.info("准备批处理插入Fas头部管理信息。。。");
		PreparedStatement pstmt = context.getPreparedStatement("EriFasAdm");
		try {
			pstmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			log.error("批处理插入Fas头部管理信息出错！");
			connection.rollback();
			return false;
		}
		pstmt.clearBatch();
		
		log.info("准备批处理插入fas的cellData信息。。。");
		pstmt = context.getPreparedStatement("EriFasCellData");
		try {
			pstmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			log.error("批处理插入fas的cellData出错！");
			connection.rollback();
			return false;
		}
		pstmt.clearBatch();

		//检查文件是否对应区域
		log.info("验证"+fileName+"文件是否对应所选区域");
		boolean flag = rnoStructAnaV2.checkEriFasArea(connection, fasId, context.getCityId());
		if(!flag) {
			log.error(fileName+"文件所属区域不是选择的区域！");
			d2 = new Date();
			report.setSystemFields("验证文件所属区域", d1, d2,
					DataParseStatus.Failall.toString(), "文件:" + fileName);
			addJobReport(report);
			connection.rollback();
			return false;
		}
		
		// --报告--//
//		d2 = new Date();
//		report.setSystemFields(DataParseProgress.SaveToDb.toString(), d1, d2,
//				DataParseStatus.Succeded.toString(), "文件:" + fileName);
//		addJobReport(report);
		
		d1 = new Date();
		d2 = new Date();
		
		//设置fas描述信息
		pstmt = context.getPreparedStatement("EriFasDesc");
		setFasDescToStmt(fileName, pstmt, fasId, context);
		log.info("插入Fas描述信息。。。");
		try {
			pstmt.executeBatch();
		} catch (SQLException e2) {
			e2.printStackTrace();
			connection.rollback();
			return false;
		}
		pstmt.clearBatch();
		
		//匹配BSC，保存在fas描述表
		log.info(">>>>>>>>>>>>>>>开始自动计算fas所测量的bsc");
		ResultInfo resultInfo = rnoStructAnaV2.matchFasBsc(connection,"RNO_ERI_FAS_CELL_DATA_T","RNO_ERI_FAS_DESC",fasId+"");
		log.info("<<<<<<<<<<<<<<<完成计算fas所测量的bsc。");
		// --报告---//
		d2 = new Date();
		if (resultInfo.isFlag()) {
			report.setSystemFields("匹配BSC", d1, d2,
					DataParseStatus.Succeded.toString(), attMsg);
			addJobReport(report);
		} else {
			connection.rollback();
			report.setSystemFields("匹配BSC", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}
		
		//转移管理头部信息中间表数据到目标表
		d1=new Date();
		String fields = "FAS_DESC_ID,FILE_FORMAT,MEA_TIME,REC_INFO,RID,TOTAL_TIME,PERCENT_VAL,CITY_ID";
		String translateSql = "insert into RNO_ERI_FAS_ADM(" + fields 
				+ ") SELECT " + fasId + ",t.* from RNO_ERI_FAS_ADM_T t ";
		log.info(">>>>>>>>>>>>>>>转移管理头部信息中间表数据到目标表的sql：" + translateSql);
		try {
			statement.executeUpdate(translateSql);
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
			d2=new Date();
			report.setSystemFields("转移管理头部信息到存储空间", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}
		//--报告--//
		d2=new Date();
		report.setSystemFields("转移管理头部信息到存储空间", d1, d2,
				DataParseStatus.Succeded.toString(), attMsg );
		addJobReport(report);
		log.info("<<<<<<<<<<<<<<<完成转移管理头部信息中间表数据到目标表");
		
		//转移cell_data信息中间表数据到目标表
		d1=new Date();
		fields = "FAS_DESC_ID,MEA_TIME,CELL,FREQ_NUM,ARFCN_1_150,AVMEDIAN_1_150,AVPERCENTILE_1_150,NOOFMEAS_1_150,CITY_ID";
		translateSql = "insert into RNO_ERI_FAS_CELL_DATA(" + fields
				+ ") SELECT " + fasId + ",TO_DATE('" + context.getMeaDate() + "','YYYY-MM-DD HH24:MI:SS'),t.* from RNO_ERI_FAS_CELL_DATA_T t ";
		log.info(">>>>>>>>>>>>>>>转移cell_data信息中间表数据到目标表的sql：" + translateSql);
		try {
			statement.executeUpdate(translateSql);
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
			d2=new Date();
			report.setSystemFields("转移cell_data信息到存储空间", d1, d2,
					DataParseStatus.Failall.toString(), attMsg + "--"
							+ resultInfo.getMsg());
			addJobReport(report);
			return false;
		}
		//--报告--//
		d2=new Date();
		report.setSystemFields("转移cell_data信息到存储空间", d1, d2,
				DataParseStatus.Succeded.toString(), attMsg );
		addJobReport(report);
		log.info("<<<<<<<<<<<<<<<完成转移cell_data信息中间表数据到目标表");
		
		//全部成功
		d2 = new Date();
		report.setSystemFields(DataParseProgress.SaveToDb.toString(), d1, d2,
				DataParseStatus.Succeded.toString(), "文件:" + fileName);
		addJobReport(report);		
		
		return true;
	}

	private void setFasDescToStmt(String fileName, PreparedStatement stmt,
			long fasId, FasParserContext context) {
		if (stmt == null) {
			return;
		}
		int index = 1;
		try {
			stmt.setLong(index++, fasId);
			stmt.setLong(index++, context.getCityId());
			stmt.setTimestamp(index++, new java.sql.Timestamp(
					context.getDateUtil().parseDateArbitrary(context.getMeaDate()).getTime()));
			stmt.setString(index++, ""); //bsc后面自动匹配
			stmt.setLong(index++, context.getRecInfo()); //数据量
			stmt.setTimestamp(index++, new java.sql.Timestamp(new Date().getTime()));
			
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void prepareRecordSql(long fasId, EriFasRecord rec,
			FasParserContext context) {
		if (rec instanceof EriFasAdmRecord) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriFasAdm");
			setAdmRecordToStmt(stmt, fasId, (EriFasAdmRecord) rec,context.getCityId(),context.getDateUtil());
		} else if (rec instanceof EriFasCellData) {
			PreparedStatement stmt = context
					.getPreparedStatement("EriFasCellData");
			setCellDataToStmt(stmt, fasId, (EriFasCellData) rec,context.getCityId());
		} else {
			log.info("暂不处理该类型的fas数据");
		}
	}

	
	private void setAdmRecordToStmt(PreparedStatement stmt, long fasId,
			EriFasAdmRecord rec, long cityId, DateUtil dateUtil) {
		if (stmt == null || rec == null) {
			return;
		}
		int index = 1;
		
		Date mt = rec.getMeaTime(dateUtil);
		if (mt == null) {
			log.error("Fas数据的管理头部：" + rec + " 的记录开始时间有问题！");
			mt = new Date();
			return;
		}
		
		try {
			stmt.setInt(index++, rec.getFileFormat());
			stmt.setTimestamp(index++, new java.sql.Timestamp(mt.getTime()));
			stmt.setInt(index++, rec.getRecInfo());
			stmt.setString(index++, rec.getRid());
			stmt.setInt(index++, rec.getTotalTime());
			stmt.setInt(index++, rec.getPercentVal());
			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setCellDataToStmt(PreparedStatement stmt, long fasId,
			EriFasCellData rec, long cityId) {
		if (stmt == null || rec == null) {
			return;
		}

		int index = 1;
		try {
			stmt.setString(index++, rec.getCellName());
			stmt.setInt(index++, rec.getFreqNum());
			stmt.setString(index++, rec.getArfcn1To150());
			stmt.setString(index++, rec.getAvmedian1To150());
			stmt.setString(index++, rec.getAvpercentile1To150());
			stmt.setString(index++, rec.getNoofmeas1To150());
			stmt.setLong(index++, cityId);
			stmt.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private EriFasRecord handleSection(int type, byte[] wholeSection) {
		EriFasRecord rec = null;
		switch (type) {
		case 40:
			rec = handleEriFasAdmRecord(wholeSection);
			break;
		case 41:
			rec = handleEriFasCellData(wholeSection);
			break;
		default:
			log.info("暂时不支持该格式解析，type=" + type);
			break;
		}
		return rec;
	}
	
	private EriFasRecord handleEriFasAdmRecord(byte[] wholeSection) {
		EriFasAdmRecord rec = new EriFasAdmRecord();
		int intVal = 0;
		String strVal = "";

		// FileFormat
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 3, 1), 1);
		rec.setFileFormat(intVal);
		// year
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 4, 1), 1);
		rec.setYear(intVal);
		// month
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 5, 1), 1);
		rec.setMonth(intVal);
		// day
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 6, 1), 1);
		rec.setDay(intVal);
		// hour
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 7, 1), 1);
		rec.setHour(intVal);
		// minute
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 8, 1), 1);
		rec.setMinute(intVal);
		// second
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 9, 1), 1);
		rec.setSecond(intVal);
		// recInfo
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 10, 2), 2);
		rec.setRecInfo(intVal);
		// rid
		strVal = new String(TranslateTools.subByte(wholeSection, 12, 7));
		rec.setRid(strVal);
		// totalTime
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 19, 2), 2);
		rec.setTotalTime(intVal);
		// percentVal
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 21, 2), 2);
		rec.setPercentVal(intVal);

		return rec;
	}

	private EriFasRecord handleEriFasCellData(byte[] wholeSection) {
		EriFasCellData rec = new EriFasCellData();
		int intVal = 0;
		String strVal = "";

		// cellName
		strVal = new String(TranslateTools.subByte(wholeSection, 3, 7));
		rec.setCellName(strVal);
		// freqNum
		intVal = TranslateTools.byte2Int(
				TranslateTools.subByte(wholeSection, 11, 1), 1);
		rec.setFreqNum(intVal);
		//List
		List<Integer> arfcnList = new ArrayList<Integer>();
		List<Integer> avmedianList = new ArrayList<Integer>();
		List<Integer> avpercentileList = new ArrayList<Integer>();
		List<Integer> noofmeasList = new ArrayList<Integer>();
		
		for (int start = 12; start < wholeSection.length; start=start+8) {
			intVal = TranslateTools.byte2Int(
					TranslateTools.subByte(wholeSection, start, 2), 2);
			arfcnList.add(intVal);
			
			intVal = TranslateTools.byte2Int(
					TranslateTools.subByte(wholeSection, start+2, 1), 1);
			avmedianList.add(intVal);
	
			intVal = TranslateTools.byte2Int(
					TranslateTools.subByte(wholeSection, start+3, 1), 1);
			avpercentileList.add(intVal);
			
			intVal = TranslateTools.byte2Int(
					TranslateTools.subByte(wholeSection, start+4, 4), 4);
			noofmeasList.add(intVal);
		}
		rec.setArfcnList(arfcnList);
		rec.setAvmedianList(avmedianList);
		rec.setAvpercentileList(avpercentileList);
		rec.setNoofmeasList(noofmeasList);
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
