package com.iscreate.op.service.rno.parser.jobrunnable;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.HoParserContext;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.excelhelper.ExcelServiceImpl;

public class Eri4GHoParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(Eri4GHoParserJobRunnable.class);

	//private RnoStructAnaV2 rnoStructAnaV2 = new RnoStructAnaV2Impl();
	//private ExcelServiceImpl excelService = new ExcelServiceImpl();

	// 导入模板规定的标题头
	private static List<String> expectTitles = Arrays.asList("Date", "ERBS",
			"EUtranCellTDD", "EUtranCellRelation", "pmHoExeAttLteIntraF",
			"pmHoExeSuccLteIntraF", "pmHoPrepAttLteIntraF", "pmHoPrepSuccLteIntraF",
			"pmHoExeAttLteInterF", "pmHoExeSuccLteInterF",
			"pmHoPrepAttLteInterF", "pmHoPrepSuccLteInterF");

	//必须的标题头所在的列
	private Map<String,Integer> expectTitlesToColumn = new HashMap<String, Integer>();
	
	private static Map<String, String> excelTitlesToFields = new HashMap<String, String>() {
		{
			put("MEA_TIME", "Date");
		};
		{
			put("ERBS", "ERBS");
		};
		{
			put("EUTRANCELLTDD", "EUtranCellTDD");
		};
		{
			put("EUTRANCELLRELATION", "EUtranCellRelation");
		};
		{
			put("PMHOEXEATTLTEINTRAF", "pmHoExeAttLteIntraF");
		};
		{
			put("PMHOEXESUCCLTEINTRAF", "pmHoExeSuccLteIntraF");
		};
		{
			put("PMHOPREPATTLTEINTRAF", "pmHoPrepAttLteIntraF");
		};
		{
			put("PMHOPREPSUCCLTEINTRAF", "pmHoPrepSuccLteIntraF");
		};
		{
			put("PMHOEXEATTLTEINTERF", "pmHoExeAttLteInterF");
		};
		{
			put("PMHOEXESUCCLTEINTERF", "pmHoExeSuccLteInterF");
		};
		{
			put("PMHOPREPATTLTEINTERF", "pmHoPrepAttLteInterF");
		};
		{
			put("PMHOPREPSUCCLTEINTERF", "pmHoPrepSuccLteInterF");
		};
	};
	
	private Map<String,String> cellNameToCellId = new HashMap<String, String>();
	private Map<String,String> cellNameToENodeBId = new HashMap<String, String>();
	private Map<String,String> cellNameToPci = new HashMap<String, String>();
	private Map<String,String> cellNameToBcch = new HashMap<String, String>();
	private Map<String,String> cellIdToPci = new HashMap<String, String>();
	private Map<String,String> cellIdToBcch = new HashMap<String, String>();

 
	
	public Eri4GHoParserJobRunnable() {
		super();
		super.setJobType("LTE_ERI_HO_FILE");
	}

	
	@Override
	public JobStatus runJobInternal(JobProfile job, Connection connection,
			Statement stmt) {
		
		long jobId = job.getJobId();
		JobStatus status = new JobStatus(jobId);
		JobReport report = new JobReport(jobId);

		//正在运行
//		status.setJobRunningStatus(JobRunningStatus.Running);
		status.setJobState(JobState.Running);
		status.setUpdateTime(new Date());
		
		// 获取job相关的信息
		String sql = "select * from RNO_DATA_COLLECT_REC where JOB_ID=" + jobId;
		List<Map<String, Object>> recs = RnoHelper.commonQuery(stmt, sql);
		RnoDataCollectRec dataRec = null;
		if (recs != null && recs.size() > 0) {
			dataRec = RnoHelper.commonInjection(RnoDataCollectRec.class,
					recs.get(0),new DateUtil());
		}
		log.debug("jobId=" + jobId + ",对应的dataRec=" + dataRec);
		if (dataRec == null) {
			log.error("转换RnoDataCollectRec失败！");
		}
		
		// 准备
		long cityId = dataRec.getCityId();
		String areaIdStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		
		//获取小区与小区id映射
		String lteCellSql = "select c.business_cell_id,c.cell_name, c.pci,c.earfcn, e.business_enodeb_id from rno_lte_cell c,rno_lte_enodeb e where c.enodeb_id=e.enodeb_id and c.area_id in("+areaIdStr+")";
		List<Map<String, Object>> cellNameToCellIdList = RnoHelper.commonQuery(stmt, lteCellSql);
		String cellName="";
		String cellId="";
		String eNodeBId="";
		String bcch="";
		String pci = "";
		for (Map<String, Object> one : cellNameToCellIdList) {
			cellName = one.get("CELL_NAME").toString();
			cellId = one.get("BUSINESS_CELL_ID").toString();
			pci = one.get("PCI").toString();
			eNodeBId = one.get("BUSINESS_ENODEB_ID").toString();
			bcch = one.get("EARFCN").toString();
			cellNameToCellId.put(cellName, cellId);
			cellNameToENodeBId.put(cellName, eNodeBId);
			cellNameToPci.put(cellName, pci);
			cellNameToBcch.put(cellName, bcch);
			cellIdToPci.put(cellId, pci);
			cellIdToBcch.put(cellId, bcch);
		}
		
		String fileName = dataRec.getFileName();
		filePath = FileInterpreter.makeFullPath(dataRec.getFullPath());
//		File file = new File(filePath);
		file = FileTool.getFile(filePath);

		String msg = "";
		
		//开始解析
		List<File> allEriHoFiles = new ArrayList<File>();// 将所有待处理的mrr文件放置在这个列表里
		boolean fromZip = false;
		String destDir = "";
		Date date1 = new Date(), date2;
		if (fileName.endsWith(".zip")
				||fileName.endsWith("ZIP")
				|| fileName.endsWith("Zip")
				) {
			fromZip = true;
			// 压缩包
			log.info("上传的爱立信切换文件是一个压缩包。");

			// 进行解压
			String path = file.getParentFile().getPath();
			destDir = path + "/"
					+ UUID.randomUUID().toString().replaceAll("-", "");

			//
			boolean unzipOk =false;
			try{
				unzipOk=ZipFileHandler.unZip(file.getAbsolutePath(), destDir);
			}catch(Exception e){
				msg="压缩包解析失败！请确认压缩包文件是否被破坏！";
				log.error(msg);
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
			//判断文件数量，不要超过规定数量
//			if(files.length>50){
//				msg="一次上传的mrr文件数量:["+files.length+"]超过了规定的最大数量：50";
//				log.error(msg);
//				setTokenInfo(token, msg);
//				super.setCachedInfo(token, msg);
//				clearResource(destDir, null);
//				areaLockManager.unlockAreasForMrr(affectAreaIds);
//				return false;
//			}
			for (File f : files) {
				// 只要文件，不要目录
				if (f.isFile() && !f.isHidden()) {
					allEriHoFiles.add(f);
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
		}else if(fileName.endsWith(".rar")){
			msg = "请用zip格式压缩文件！";
			log.error(msg);
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
		}
		else {
			log.info("上传的爱立信切换数据是一个普通文件。");
			allEriHoFiles.add(file);

		}
		
		if (allEriHoFiles.isEmpty()) {
			msg = "未上传有效的eri切换文件！zip包里不能再包含有文件夹！";
			log.error(msg);
			clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的eri切换文件！注意zip包里不能再包含有文件夹！");
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

		//正在解析
		sql = "update rno_data_collect_rec set FILE_STATUS='"
				+ DataParseStatus.Parsing.toString()
				+ "' where DATA_COLLECT_ID=" + dataRec.getDataCollectId();
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		//开始分析
		HoParserContext context = new HoParserContext();
		
		//获取爱立信切换的HBase描述表、数据表
		try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			context.addHtable("descTable","LTE4G_HO_DESC",conf);
			context.addHtable("dataTable","LTE4G_ERI_HO",conf);

		} catch (IOException e1) {
			e1.printStackTrace();
			msg = "爱立信切换数据导入时，获取HTable出错！";
			log.error(msg);
			clearResource(destDir, context);
			return failWithStatus("获取HBase表时出错", new Date(),
					this, stmt, report, status,
					dataRec.getDataCollectId(), DataParseStatus.Failall,
					DataParseProgress.Prepare);
		}
		//建立每个表对应的put队列
		context.addPuts("descPuts");
		context.addPuts("dataPuts");
		
		// 2、设置cityId
		context.setCityId(cityId);

		String tmpFileName = fileName;
		int sucCnt = 0;
		HoResultInfo resInfo = new HoResultInfo();
		int totalFileCnt=allEriHoFiles.size();
		int i=0;
		
		for (File f : allEriHoFiles) {

				// 每一个文件的解析都应该是独立的
				if (fromZip) {
					tmpFileName = f.getName();
				}
				date1 = new Date();
				i++;
				//文件解析
				try {
					resInfo = parseHo(this, report, stmt, tmpFileName, f, context,
							connection);
				} catch (Exception e) {
					e.printStackTrace();
					resInfo.setFlag(false);
					date2 = new Date();
					log.error(tmpFileName + "文件解析出错！");
					
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
				if(resInfo.isFlag()){
					report.setFinishState(DataParseStatus.Succeded.toString());
				}else{
					report.setFinishState(DataParseStatus.Failall.toString());
				}
				report.setBegTime(date1);
				report.setEndTime(date2);
				if (resInfo.isFlag()) {
					report.setAttMsg("成功文件（"+i+"/"+totalFileCnt+"）:" + tmpFileName + ","+resInfo.getMsg());
					sucCnt++;
				}else{
					report.setAttMsg("失败文件（"+i+"/"+totalFileCnt+"）:" + tmpFileName+ ","+resInfo.getMsg());
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

		if (sucCnt == allEriHoFiles.size()) {
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
	 * 解析一个爱立信切换数据文件，返回结果
	 * @param jobWorker
	 * @param report
	 * @param statement
	 * @param fileName
	 * @param file
	 * @param context
	 * @param connection
	 * @return
	 */
	private HoResultInfo parseHo(JobRunnable jobWorker, JobReport report,
			Statement statement, String fileName, File file,
			HoParserContext context, Connection connection){
		
		HoResultInfo resInfo = new HoResultInfo();
		long cityId = context.getCityId();
		
		// 读取文件
		BufferedReader reader = null;
		String charset = null;
		charset = FileTool.getFileEncode(file.getAbsolutePath());
		log.debug(fileName + " 文件编码：" + charset);
		if (charset == null) {
			log.error("文件：" + fileName + ":无法识别的文件编码！");
			resInfo.setFlag(false);
			resInfo.setMsg("文件：" + fileName + ":无法识别的文件编码！");
			return resInfo;
		}
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), charset));
		} catch (Exception e) {
			resInfo.setFlag(false);
			resInfo.setMsg("文件读取出错");
			return resInfo;
		}
		long t1 = System.currentTimeMillis();
		long t2 = 0l;
		log.info("----------开始处理爱立信切换数据:");
		String line = "";
		String msg = "";
		int totalDataNum = 0;
		int n=0;
		
		try{
			String[] sps = new String[1];
			do {
				line = reader.readLine();
				if(line==null){
					sps=new String[]{};
					break;
				}
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			// 读取到标题头长度
			int fieldCnt = sps.length;
			
			// 获取必须标题在文件的位置, 判断标题是否齐全
			for (String expectTitle : expectTitles) {
				boolean isExisted = false;
				for (int i = 0; i < sps.length; i++) {
					if(sps[i].equals(expectTitle)) {
						expectTitlesToColumn.put(expectTitle, i);
						isExisted = true;
					}
				}
				if(!isExisted) {
					msg += "["+expectTitle+"]";
				}
			}

			if(expectTitlesToColumn.size() != expectTitles.size()) {
				resInfo.setFlag(false);
				resInfo.setMsg("文件缺少以下列："+msg);
				return resInfo;
			}
			
			// 逐行读取数据(真正读取数据)
			long sTime =System.currentTimeMillis();
			int executeCnt = 0;

			do {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				sps = line.split(",|\t");
				if (sps.length != fieldCnt || sps == null) {
					continue;
				}
				totalDataNum++;
				
				Date meaTime = context.getDateUtil().parseDateArbitrary(
						sps[expectTitlesToColumn.get((excelTitlesToFields.get("MEA_TIME")))]);
				// 缓存进context
				context.setMeaDate(context.getDateUtil().format_yyyyMMdd(meaTime));
				
				//组装rowkey
				long meaMillis = meaTime.getTime();

				String cellName = sps[expectTitlesToColumn
					                  .get((excelTitlesToFields.get("EUTRANCELLTDD")))];
				//例子:4600-657256-12
				String EUtranCellRelation = sps[expectTitlesToColumn
								                  .get((excelTitlesToFields.get("EUTRANCELLRELATION")))];
				if(cellNameToCellId.get(cellName) == null) {
					log.debug("在小区工参匹配不出小区名："+cellName+"，该行数据不入库");
					continue;
				}
				String cellId = cellNameToCellId.get(cellName);
				
				if(cellNameToENodeBId.get(cellName) == null) {
					log.debug("在小区工参匹配不出小区名："+cellName+"，该行数据不入库");
					continue;
				}
				String eNodebId = cellNameToENodeBId.get(cellName);
				
				String rowkey = cityId+"_"+meaMillis+"_"+eNodebId
						+"_"+cellId+"_"+EUtranCellRelation;
				
				Put put = new Put(Bytes.toBytes(rowkey));
				
				//cityId
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("CITY_ID"), 
						Bytes.toBytes(cityId+""));
				//测量时间
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("MEA_TIME"), 
						Bytes.toBytes(context.getMeaDate()));
				//ERBS
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("ERBS"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("ERBS")))]));
				//EUTRANCELLTDD
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("EUTRANCELLTDD"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("EUTRANCELLTDD")))]));
				//CELL_PCI
				if(cellNameToPci.get(cellName) == null) {
					log.debug("在小区工参匹配不出小区名："+cellName+"，该行数据不入库");
					continue;
				}
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_PCI"), 
						Bytes.toBytes(cellNameToPci.get(cellName)));
				//ENODEBID
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("ENODEBID"), 
						Bytes.toBytes(eNodebId));
				//BUS_CELL_ID
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("BUS_CELL_ID"), 
						Bytes.toBytes(cellId));
				//CELL_BCCH
				if(cellNameToBcch.get(cellName) == null) {
					log.debug("在小区工参匹配不出小区名："+cellName+"，该行数据不入库");
					continue;
				}
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_BCCH"), 
						Bytes.toBytes(cellNameToBcch.get(cellName)));
				//NCELL_BCCH
				String[] ncellInfo = EUtranCellRelation.split("-");
				if(cellIdToBcch.get(ncellInfo[1]+ncellInfo[2]) == null) {
					log.debug("在小区工参匹配不出小区名："+cellName+"，该行数据不入库");
					continue;
				}
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("NCELL_BCCH"), 
						Bytes.toBytes(cellIdToBcch.get(ncellInfo[1]+ncellInfo[2])));
				//EUTRANCELLRELATION
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("EUTRANCELLRELATION"), 
						Bytes.toBytes(EUtranCellRelation));
				//PMHOEXEATTLTEINTRAF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOEXEATTLTEINTRAF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOEXEATTLTEINTRAF")))]));
				//PMHOEXESUCCLTEINTRAF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOEXESUCCLTEINTRAF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOEXESUCCLTEINTRAF")))]));
				//PMHOPREPATTLTEINTRAF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOPREPATTLTEINTRAF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOPREPATTLTEINTRAF")))]));
				//PMHOPREPSUCCLTEINTRAF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOPREPSUCCLTEINTRAF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOPREPSUCCLTEINTRAF")))]));
				
				//PMHOEXEATTLTEINTERF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOEXEATTLTEINTERF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOEXEATTLTEINTERF")))]));
				//PMHOEXESUCCLTEINTERF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOEXESUCCLTEINTERF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOEXESUCCLTEINTERF")))]));
				//PMHOPREPATTLTEINTERF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOPREPATTLTEINTERF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOPREPATTLTEINTERF")))]));
				//PMHOPREPSUCCLTEINTERF
				put.add(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOPREPSUCCLTEINTERF"), 
						Bytes.toBytes(sps[expectTitlesToColumn
						                  .get((excelTitlesToFields.get("PMHOPREPSUCCLTEINTERF")))]));
		
				//放进put队列
				if(context.getPuts("dataPuts") != null) {
					context.getPuts("dataPuts").add(put);
				} else {
					log.error("dataPuts队列为null！不能加入put对象");
				}

				executeCnt++;
				
				// 每5000行执行一次提交
				if (executeCnt > 5000) {
					log.info("读取5000行数据耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					
					//存入htable
					if(context.getHtable("dataTable") != null 
							&& context.getPuts("dataPuts") != null 
							&& context.getPuts("dataPuts").size() > 0) {
						log.info("Puts.size()="+context.getPuts("dataPuts").size());
						context.getHtable("dataTable").put(context.getPuts("dataPuts"));
					} else {
						log.info("这5000条数据中中兴切换数据为空，或者没找到对应context对象中的HTable！");
					}
					log.info("存入Hbase的表耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
					sTime = System.currentTimeMillis();
					
					//清除put队列
					context.clearPuts();
					context.tableFlushCommits();
					
					executeCnt = 0;
				}
				n++;
			} while (!StringUtils.isBlank(line));
			
			//将剩余不足5000条的数据存入htable
			if (executeCnt > 0) {
				if(context.getHtable("dataTable") != null 
						&& context.getPuts("dataPuts") != null 
						&& context.getPuts("dataPuts").size() > 0) {
					log.info("Puts.size()="+context.getPuts("dataPuts").size());
					context.getHtable("dataTable").put(context.getPuts("dataPuts"));
				} else {
					log.info("这批数据中ERI切换数据为空，或者没找到对应context对象中的HTable！");
				}
				log.info("存入Hbase的表耗时："+(System.currentTimeMillis()-sTime)/1000+"s");
				//清除put队列
				context.clearPuts();
				context.tableFlushCommits();
			}
			
			if(n<=0) {
				resInfo.setFlag(false);
				resInfo.setMsg("文件数据导入失败，需要对应的小区工参");
				return resInfo;
			}
			
			//将描述信息存进Hbase表
			if(context.getMeaDate() == null) {
				resInfo.setFlag(false);
				resInfo.setMsg("文件解析出错");
				return resInfo;
			}
			long meaMillis = context.getDateUtil().parseDateArbitrary(context.getMeaDate()).getTime();
			int recordNum = n;
			String createTime = context.getDateUtil().format_yyyyMMddHHmmss(new Date());
			String modTime = context.getDateUtil().format_yyyyMMddHHmmss(new Date());

			String rowkey = cityId + "_" + meaMillis + "_ERI";
			Put put = new Put(Bytes.toBytes(rowkey));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"), 
					Bytes.toBytes(cityId+""));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"), 
					Bytes.toBytes(context.getMeaDate()));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"), 
					Bytes.toBytes(recordNum+""));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"), 
					Bytes.toBytes("ERI"));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"), 
					Bytes.toBytes(createTime));
			put.add(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"), 
					Bytes.toBytes(modTime));
			//放进put队列
			if(context.getPuts("descPuts") != null) {
				context.getPuts("descPuts").add(put);
			} else {
				log.error("descPuts队列为null！不能加入put对象");
			}
			//提交到table
			if(context.getHtable("descTable") != null 
					&& context.getPuts("descPuts") != null 
					&& context.getPuts("descPuts").size() > 0) {
				log.info("Puts.size()="+context.getPuts("descPuts").size());
				context.getHtable("descTable").put(context.getPuts("descPuts"));
			} else {
				log.info("描述数据为空，或者没找到对应context对象中的HTable！");
			}
			
			t2 = System.currentTimeMillis();
			log.info("----------ERI切换数据处理完毕。耗时：" + (t2 - t1) + "ms");
		
		} catch (Exception e) {
			e.printStackTrace();
			resInfo.setFlag(false);
			resInfo.setMsg("文件解析出错");
			return resInfo;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				resInfo.setFlag(false);
				resInfo.setMsg("文件解析出错");
				return resInfo;
			}
		}

		//文件解析和保存成功
		resInfo.setFlag(true);
		resInfo.setMsg("[文件数据共："+totalDataNum+"行，成功："+n+"行]");
		return resInfo;
	}


	/**
	 * 清理资源
	 * @param destDir
	 * @param context
	 * @author peng.jm
	 * @date 2014-7-18上午11:31:00
	 */
	private void clearResource(String destDir, HoParserContext context) {
		FileTool.deleteDir(destDir);
		if (context != null) {
			context.closeHTables();
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
	
	
	class HoResultInfo{
		boolean flag = false;
		String msg = "";
		public boolean isFlag() {
			return flag;
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
