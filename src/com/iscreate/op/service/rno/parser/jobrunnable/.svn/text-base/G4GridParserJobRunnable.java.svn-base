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

import oracle.jdbc.driver.OracleConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.CharField;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoLteSceneManageDao;
import com.iscreate.op.dao.rno.RnoLteSceneManageDaoImpl;
import com.iscreate.op.dao.rno.RnoStructAnaV2;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellForMatch;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellLonLat;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoCommonServiceImpl;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.client.JobRunnable;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.parser.DataParseProgress;
import com.iscreate.op.service.rno.parser.DataParseStatus;
import com.iscreate.op.service.rno.parser.jobmanager.FileInterpreter;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.UnicodeUtil;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.tools.LatLngHelper;

public class G4GridParserJobRunnable extends DbParserBaseJobRunnable {

	private static Log log = LogFactory.getLog(G4GridParserJobRunnable.class);

	private RnoLteSceneManageDao rnoLteSceneManageDao=new RnoLteSceneManageDaoImpl() ;

	public G4GridParserJobRunnable() {
		super();
		super.setJobType("LTEGRIDFILE");
	}

	// 构建insert语句
	private static String dataTab = "RNO_4G_GRID_DATA";

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
		File datFile=null;
		File mifFile=null;
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
				// 只要dat,mif文件，不要目录
				if (f.isFile() && !f.isHidden()) {
//					allNcsFiles.add(f);
					if(f.getName().endsWith(".DAT")){
						datFile=f;
					}
					if(f.getName().endsWith(".MIF")){
						mifFile=f;
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
			log.info("上传是一个未压缩的普通文件。");
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("上传是一个未压缩的普通文件，请用zip格式压缩文件！");
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
		}

		if (datFile==null || mifFile==null) {
			msg = "未上传有效的网格文件！注意zip包里必须包含有.DAT和.MIF数据文件！";
			log.error(msg);
			// super.setCachedInfo(token, msg);
			// clearResource(destDir, null);
			// job报告
			date2 = new Date();
			report.setFinishState(DataParseStatus.Failall.toString());
			report.setStage(DataParseProgress.Decompress.toString());
			report.setBegTime(date1);
			report.setEndTime(date2);
			report.setAttMsg("未上传有效的网格文件！注意zip包里必须包含有.DAT和.MIF数据文件！");
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
		//先确定该区域是否有数据存在，如果有在后续解析成功后将其旧数据
		sql="SELECT count(*) cnt FROM RNO_4G_GRID_DATA where area_id="+cityId;
		List<Map<String, Object>> grids= RnoHelper.commonQuery(stmt, sql);
		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = null;
		standardPoints = rnoLteSceneManageDao
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(stmt,cityId,
						RnoConstant.DBConstant.MAPTYPE_BAIDU);
		int gridCnt = 0;
		if(grids != null){
			gridCnt = Integer.parseInt(grids.get(0).get("CNT").toString());
		}
		int i = 0;
		PreparedStatement insertGridState=null;
		String insertGridSql="insert into RNO_4G_GRID_DATA (id,area_id,grid_id,Grid_Desc,grid_lnglats,Grid_Center,Create_Time) values(SEQ_RNO_4G_GRID_DATA.nextval,"+cityId+",?,?,?,?,sysdate)";
		try {
			insertGridState=connection.prepareStatement(insertGridSql);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				insertGridState.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
				// 每一个文件的解析都应该是独立的
				date1 = new Date();
				//首先解析dat文件
				Map<Integer, String> datObj=null;
				datObj=parseDbfFile(datFile.getAbsolutePath());
				if(datObj!=null && datObj.size()!=0){
					parseOk=parseMifFile(mifFile.getAbsolutePath(), datObj,insertGridState,connection,standardPoints);
				}
				date2 = new Date();
				report.setStage("文件处理总结");
				report.setReportType(1);
				if (parseOk) {

					if(gridCnt>0){
						sql="delete from RNO_4G_GRID_DATA where area_id="+cityId;
						// 清空该区域的旧的网格数据资源
						try {
							stmt.executeUpdate(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					try {
						insertGridState.executeBatch();
					} catch (Exception e) {
						// TODO: handle exception
						insertGridState.close();
						e.printStackTrace();
					}
					report.setFinishState(DataParseStatus.Succeded.toString());
					sql = "update rno_data_collect_rec set file_status='"
							+ DataParseStatus.Succeded.toString()
							+ "' where data_collect_id=" + dataRec.getDataCollectId();
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					report.setFinishState(DataParseStatus.Failall.toString());
					sql = "update rno_data_collect_rec set file_status='"
							+ DataParseStatus.Failall.toString()
							+ "' where data_collect_id="
							+ dataRec.getDataCollectId();
					try {
						stmt.executeUpdate(sql);
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
				report.setBegTime(date1);
				report.setEndTime(date2);
				addJobReport(report);

				status.setJobState(JobState.Finished);
				status.setUpdateTime(new Date());
			} catch (Exception e) {
				e.printStackTrace();
				date2 = new Date();
				msg = tmpFileName + "文件解析出错！";
				report.setStage("文件处理总结");
				report.setBegTime(date1);
				report.setEndTime(date2);
				report.setReportType(1);
				/*report.setAttMsg("文件解析出错（" + i + "/" + totalFileCnt + "）:"
						+ tmpFileName);*/
				addJobReport(report);

				status.setJobState(JobState.Failed);
				status.setUpdateTime(new Date());

				log.error(msg);
			}


		log.debug("更新结果状态 rno_data_collect_rec  sql:" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
			insertGridState.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
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

	public static void main(String[] args) {

//		ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath*:spring/rno-appcontx.xml");
//		System.out.println(Class.class.getClass().getResource("/").getPath());
//		ApplicationContext ctx=new ClassPathXmlApplicationContext(Class.class.getClass().getResource("/").getPath()+"spring/rno-appcontx.xml");
//		System.out.println(ctx);
//		RnoCommonService rnoCommonService=(RnoCommonService)ctx.getBean("rnoCommonService");
		System.out.println(isNum("113.522422 22.213105"));
		System.out.println("  5".trim().split(" ").length);
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
	/**
	 *
	 * @title 解析dbf,dat文件
	 * @param path
	 * @return
	 * @author chao.xj
	 * @date 2015-7-4下午4:30:13
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<Integer, String> parseDbfFile(String pathName) {
		Map<Integer, String> datObj=new HashMap<Integer, String>();
		try {
			DBF classDB = new DBF(pathName);
			CharField id  = (CharField) classDB.getField("ID");
			CharField gridId  = (CharField) classDB.getField("GridID");
			CharField classValue = (CharField) classDB.getField("Class");
			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				datObj.put(Integer.parseInt(id.get()), gridId.get());
//				System.out.println(id.get() + ", " + gridId.get() + ", " + classValue.get());
			}
		} catch (xBaseJException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datObj;
	}
	/**
	 *
	 * @title
	 * @param pathName
	 * @author chao.xj
	 * @date 2015-7-4下午4:32:44
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean parseMifFile(String pathName,Map<Integer, String> datObj,PreparedStatement insertGridState,Connection connection,Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		boolean ok=true;
		//txt文件类型
		File file=new File(pathName);
		InputStreamReader isr=null;
		//为了获取clob对象
		OracleConnection conn =null;
		oracle.sql.CLOB clob=null;
		try {
			//将conn转为oracle的conn
			C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();
	        conn = (OracleConnection) cp30NativeJdbcExtractor
	        		.getNativeConnection(connection);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				isr = new InputStreamReader(new FileInputStream(file),
						"gb18030");
				BufferedReader br = new BufferedReader(isr);
				String lineText = null;
				String lineArr[] = new String[2];
				String centerArr[] = new String[3];
				StringBuffer gridLonLats = new StringBuffer();
				StringBuffer centerLonLat = new StringBuffer();
				boolean beg = false;
				boolean preLineLngLat=false;
				int errCnt=0;
				int cnt = 0;
//				System.out.println("MIF文件===========>>>>>>>>>>>>>>>>>>>>>>>>>>>>开始解析");
				//循环读取每一行
				while ((lineText = br.readLine()) != null) {
					if (lineText.contains("Region")) {
						preLineLngLat=false;
						beg = true;
						cnt++;
//						System.out.println("Region==" + cnt);
					} else {
						if (beg) {
							if(errCnt>0){
								errCnt--;
								continue;
							}
							if (lineText.split(" ").length == 2) {
								//去除前后导空格后，以空格分裂网络经纬度数组
								lineArr = lineText.trim().split(" ");
								if (isNum(lineArr[0]) && isNum(lineArr[1])) {
//									System.out.println("lineText==" + lineText);
									//转换百度坐标
									lineArr=getBaiduLnglat(Double.parseDouble(lineArr[0]), Double.parseDouble(lineArr[1]), standardPoints);
									gridLonLats.append(lineArr[0]+","+lineArr[1]+";");

									//上一行为经纬度信息
									preLineLngLat = true;
								}
							}
							//上一个是经纬度，本次是一个数字且
							if(preLineLngLat && lineText.trim().split(" ").length==1 && isNum(lineText.trim())){
								errCnt = Integer.parseInt(lineText.trim());
								preLineLngLat = false;
							}
							if(lineText.contains("Center")){
								//去除前后导空格后，以空格分裂网格中心点经纬度数组
								centerArr=lineText.trim().split(" ");
//								System.out.println("lineText center==" + centerArr[1]+","+centerArr[2]);
//								System.out.println("Region==" + cnt+"------"+datObj.get(cnt)+"------"+gridLonLats.toString()+"-------"+ centerArr[1]+","+centerArr[2]);
								//获取clob对象
								clob = oracle.sql.CLOB.createTemporary(
										conn, false, oracle.sql.CLOB.DURATION_SESSION);
								clob.open(oracle.sql.CLOB.MODE_READWRITE);

								clob.setString(3, gridLonLats.substring(0, gridLonLats.length()-1).toString());
								insertGridState.setInt(1, cnt);
								insertGridState.setString(2, datObj.get(cnt));
								insertGridState.setClob(3,clob);
								//转换百度坐标
								centerArr=getBaiduLnglat(Double.parseDouble(centerArr[1]), Double.parseDouble(centerArr[2]), standardPoints);
//								insertGridState.setString(4, centerArr[1]+","+centerArr[2]);
								insertGridState.setString(4, centerArr[0]+","+centerArr[1]);
								insertGridState.addBatch();
								gridLonLats.setLength(0);
							}
						}
					}
				}
				if(lineText==null){
//					System.out.println("MIF文件===========<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<解析完毕");
				}
			}else{
				ok=false;
				System.out.println("找不到指定文件！！！！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("读取MIF文件出错！！！！！！！！！！");
			ok=false;
			e.printStackTrace();

		}
		return ok;
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
	public static boolean isNum(String str) {
		//可以判断正负、整数小数
	    Boolean strResult = str.matches("-?[0-9]+.*[0-9]*");
	    return strResult;
	}
	/**
	 *
	 * @title 获取百度坐标
	 * @param longitude
	 * @param latitude
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6下午3:28:23
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private String[] getBaiduLnglat(double longitude, double latitude,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		String[] baidulnglat = null;
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = CoordinateHelper.getLngLatCorrectValue(longitude
						, latitude , standardPoints);
			} else {
				log.info("区域不存在基准点，将使用百度在线接口进行校正。");
				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
		}

		return baidulnglat;
	}
}
