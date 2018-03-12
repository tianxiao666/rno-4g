package com.iscreate.op.dao.rno;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellLonLat;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoStructAnaJobRec;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.rno.algorithm.Edge;
import com.iscreate.op.service.rno.algorithm.MaximalClique;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.task.structana.RnoStructAnaJobRunnable;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.DumpHelper;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.ZipFileHandler;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class RnoStructureAnalysisDaoImplV2 implements RnoStructureAnalysisDaoV2 {

	private static Log log = LogFactory
			.getLog(RnoStructureAnalysisDaoImplV2.class);
	private MemcachedClient memCached;
	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	/**
	 * 
	 * @title 写过滤后的统一的ncs数据
	 * @param jobRec
	 * @param ress
	 * @author chao.xj
	 * @date 2014-9-19下午5:28:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void debug_output_exportNcsData(RnoStructAnaJobRec jobRec,
			List<Map<String, Object>> ress) {

		BufferedWriter bw = null;
		String cfgDir = DumpHelper.getDumpDir("struct-ana");// D:/tmp/ /tmp/
		if (StringUtils.isEmpty(cfgDir)) {
			cfgDir = "/tmp/";
		}
		String saveFullPath = cfgDir + jobRec.getJobId() + "_"
				+ jobRec.getCityId() + "_ncs_export.txt";
		log.debug("导出ncs临时表的存储文件：" + new File(saveFullPath).getAbsolutePath());
		// 判断是否存在该文件
		File ncsFile = new File(saveFullPath);
		boolean ncsExist = ncsFile.exists();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath, true), "gbk"));
			StringBuffer buf = new StringBuffer();
			if (ress != null || ress.size() > 0) {
				List<String> titles = Arrays.asList("MEA_TIME", "CELL",
						"NCELL", "CELL_LON", "CELL_LAT", "NCELL_LON",
						"NCELL_LAT", "CELL_FREQ_CNT", "NCELL_FREQ_CNT",
						"SAME_FREQ_CNT", "ADJ_FREQ_CNT", "CI_DENOMINATOR",
						"CA_DENOMINATOR", "CI_DIVIDER", "CA_DIVIDER",
						"CELL_BEARING", "CELL_INDOOR", "NCELL_INDOOR",
						"CELL_SITE", "NCELL_SITE", "CELL_FREQ_SECTION",
						"NCELL_FREQ_SECTION", "DISTANCE", "CELL_TO_NCELL_DIR");
				if (!ncsExist) {
					for (String str : titles) {
						buf.append(str).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
				for (Map<String, Object> res : ress) {
					buf.setLength(0);
					if (res == null || res.isEmpty()) {
						continue;
					}
					for (String str : titles) {
						if (res.get(str) == null) {
							buf.append("-,");
							continue;
						}
						buf.append(res.get(str).toString()).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 
	 * @title 通过分页获取过滤后的NCS数据
	 * @param jobRec
	 * @param stmt
	 * @param unifyTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19下午2:39:27
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_getFilteredNcsDataByPage(RnoStructAnaJobRec jobRec,
			Statement stmt, String unifyTab) {
		log.debug("进入getFilteredNcsDataByPage(RnoStructAnaJobRec jobRec,Statement stmt,String unifyTab)"
				+ jobRec + "  " + stmt + "  " + unifyTab);
		if (!DumpHelper.needDump("struct-ana-city-" + jobRec.getCityId())) {
			return 0;
		}
		int cnt = debug_output_filterNcsCnt(stmt, unifyTab);
		log.debug("获取过滤后的NCS数据统一表数据为：" + cnt);
		if (cnt == 0) {
			return cnt;
		}
		String sql = "";
		List<Map<String, Object>> ress = null;
		String fields = "MEA_TIME," + " CELL," + " NCELL," + " CELL_LON,"
				+ " CELL_LAT," + " NCELL_LON," + " NCELL_LAT,"
				+ " CELL_FREQ_CNT," + " NCELL_FREQ_CNT," + " SAME_FREQ_CNT,"
				+ " ADJ_FREQ_CNT," + " CI_DENOMINATOR," + " CA_DENOMINATOR,"
				+ " CI_DIVIDER," + " CA_DIVIDER," + " CELL_BEARING,"
				+ " CELL_INDOOR," + " NCELL_INDOOR," + " CELL_SITE,"
				+ " NCELL_SITE," + " CELL_FREQ_SECTION,"
				+ " NCELL_FREQ_SECTION," + " DISTANCE," + " CELL_TO_NCELL_DIR";
		for (int row = 0; row < cnt; row++) {
			int pageSize = 30000;
			/*
			 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
			 */
			sql = "select * " + "from (select * " + " from (select " + fields
					+ ", row_number() OVER(ORDER BY null) AS \"row_number\" "
					+ " from " + unifyTab + " t) p "
					+ " where p.\"row_number\" > " + row + ") q "
					+ " where rownum <=" + pageSize;
			ress = RnoHelper.commonQuery(stmt, sql);
			// log.debug("统一表数据表本次完成写入记录数为：" + ress.size());
			// 写入文件
			debug_output_exportNcsData(jobRec, ress);
			row += pageSize - 1;// 步长
		}

		return cnt;
	}

	/**
	 * 
	 * @title 获取过滤后的统一NCS表数据量
	 * @param stmt
	 * @param unifyTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19下午5:30:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_filterNcsCnt(Statement stmt, String unifyTab) {
		String sql = "select count(*) cnt from " + unifyTab;
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		int cnt = 0;
		try {
			cnt = Integer.parseInt(res.get(0).get("CNT").toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * 
	 * @title 输出原始的ncs数据
	 * @param jobRec
	 * @param ress
	 * @author chao.xj
	 * @date 2014-9-19上午11:22:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	private void debug_output_exportOriginalNcsData(RnoStructAnaJobRec jobRec,
			List<Map<String, Object>> ress) {

		BufferedWriter bw = null;
		String cfgDir = DumpHelper.getDumpDir("struct-ana");// D:/tmp/ /tmp/
		if (StringUtils.isEmpty(cfgDir)) {
			cfgDir = "/tmp/";
		}
		String saveFullPath = cfgDir + jobRec.getJobId() + "_"
				+ jobRec.getCityId() + "_orincs_export.txt";
		// log.debug("导出ncs临时表的存储文件："
		// + new File(saveFullPath).getAbsolutePath());
		// 判断是否存在该文件
		File ncsFile = new File(saveFullPath);
		boolean ncsExist = ncsFile.exists();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath, true), "gbk"));
			StringBuffer buf = new StringBuffer();
			if (ress != null || ress.size() > 0) {
				List<String> titles = Arrays.asList("CELL", "NCELL",
						"CELL_LON", "CELL_LAT", "NCELL_LON", "NCELL_LAT",
						"CELL_FREQ_CNT", "NCELL_FREQ_CNT", "SAME_FREQ_CNT",
						"ADJ_FREQ_CNT", "CI_DENOMINATOR", "CA_DENOMINATOR",
						"CI_DIVIDER", "CA_DIVIDER", "CELL_BEARING",
						"CELL_INDOOR", "NCELL_INDOOR", "CELL_SITE",
						"NCELL_SITE", "CELL_FREQ_SECTION",
						"NCELL_FREQ_SECTION", "DISTANCE", "CELL_TO_NCELL_DIR");
				if (!ncsExist) {
					for (String str : titles) {
						buf.append(str).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
				for (Map<String, Object> res : ress) {
					buf.setLength(0);
					if (res == null || res.isEmpty()) {
						continue;
					}
					for (String str : titles) {
						if (res.get(str) == null) {
							buf.append("-,");
							continue;
						}
						buf.append(res.get(str).toString()).append(",");
					}
					buf.deleteCharAt(buf.length() - 1);
					bw.write(buf.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 
	 * @title 通过分页获取原始爱立信NCS数据
	 * @param stmt
	 * @param tmperincssourceTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19上午11:19:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_getOriginalEriNcsDataByPage(RnoStructAnaJobRec jobRec,
			Statement stmt, String tmperincssourceTab) {
		log.debug("进入getOriginalEriNcsDataByPage(RnoStructAnaJobRec jobRec,Statement stmt,String tmperincssourceTab)"
				+ jobRec + " " + stmt + "  " + tmperincssourceTab);
		if (!DumpHelper.needDump("struct-fullana-city-" + jobRec.getCityId())) {
			return 0;
		}
		int cnt = debug_output_originalEriNcsCnt(stmt, tmperincssourceTab);
		log.debug("爱立信NCS临时表数据量为：" + cnt);
		if (cnt == 0) {
			return cnt;
		}
		String sql = "";
		List<Map<String, Object>> ress = null;
		String fields = "cell,                                                  "
				+ "          NCELL,                                                "
				+ "        MIN(DEFINED_NEIGHBOUR) DEFINED_NEIGHBOUR,       "
				+ "         min(CELL_LON) CELL_LON,                                          "
				+ "         min(CELL_LAT) CELL_LAT,                                          "
				+ "         min(NCELL_LON) NCELL_LON,                                        "
				+ "         min(NCELL_LAT) NCELL_LAT,                                        "
				+ "         min(CELL_FREQ_CNT) CELL_FREQ_CNT,                                "
				+ "         min(NCELL_FREQ_CNT) NCELL_FREQ_CNT,                              "
				+ "         min(SAME_FREQ_CNT) SAME_FREQ_CNT,                                "
				+ "         min(ADJ_FREQ_CNT) ADJ_FREQ_CNT,                                  "
				+ "         sum(reparfcn) CI_DENOMINATOR,                                    "
				+ "         sum(reparfcn) CA_DENOMINATOR,                                    "
				+ "         sum(ci_divider) ci_divider,                                      "
				+ "         sum(ca_divider) ca_divider,                                      "
				+ "         min(NCELLS) NCELLS,                                              "
				+ "         min(CELL_BEARING) CELL_BEARING,                                  "
				+ "         min(CELL_INDOOR) CELL_INDOOR,                                    "
				+ "         min(NCELL_INDOOR) NCELL_INDOOR,                                  "
				+ "         min(CELL_DOWNTILT) CELL_DOWNTILT,                                "
				+ "         min(CELL_SITE),                                                  "
				+ "         min(NCELL_SITE) NCELL_SITE,                                      "
				+ "         min(CELL_FREQ_SECTION) CELL_FREQ_SECTION,                        "
				+ "         min(NCELL_FREQ_SECTION) NCELL_FREQ_SECTION,                      "
				+ "         min(DISTANCE) DISTANCE,                                        "
				+ "          min(CELL_TO_NCELL_DIR) CELL_TO_NCELL_DIR";
		String group = " group by (cell, ncell)";
		for (int row = 0; row < cnt; row++) {
			int pageSize = 30000;
			/*
			 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
			 */
			sql = "select * " + "from (select * " + " from (select " + fields
					+ ", row_number() OVER(ORDER BY null) AS \"row_number\" "
					+ " from " + tmperincssourceTab + " t " + group + ") p "
					+ " where p.\"row_number\" > " + row + ") q "
					+ " where rownum <=" + pageSize;
			ress = RnoHelper.commonQuery(stmt, sql);
			// 写入文件
			debug_output_exportOriginalNcsData(jobRec, ress);
			log.debug("eri本次完成写入记录数为：" + ress.size());
			row += pageSize - 1;// 步长
		}

		return cnt;
	}

	/**
	 * 
	 * @title 获取原始爱立信NCS数量
	 * @param stmt
	 * @param tmperincssourceTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19下午5:27:53
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_originalEriNcsCnt(Statement stmt, String tmperincssourceTab) {
		String sql = "select count(*) cnt from (select * from "
				+ tmperincssourceTab + " group by (cell, ncell)) ";
		int cnt = 0;
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		try {
			cnt = Integer.parseInt(res.get(0).get("CNT").toString());
		} catch (Exception e) {
			// TODO: handle exception\
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * 
	 * @title 获取原始华为NCS数据
	 * @param stmt
	 * @param tmphwncssourceTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19上午11:19:59
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_getOriginalHwNcsDataByPage(RnoStructAnaJobRec jobRec,
			Statement stmt, String tmphwncssourceTab) {
		log.debug("进入getOriginalHwNcsDataByPage(RnoStructAnaJobRec jobRec,Statement stmt,String tmphwncssourceTab)"
				+ jobRec + "  " + stmt + "  " + tmphwncssourceTab);
		if (!DumpHelper.needDump("struct-fullana-city-" + jobRec.getCityId())) {
			return 0;
		}
		log.debug("目录：" + DumpHelper.getDumpDir("struct-ana"));
		int cnt = debug_output_originalHwNcsCnt(stmt, tmphwncssourceTab);
		log.debug("华为NCS临时表数据量为：" + cnt);
		if (cnt == 0) {
			return cnt;
		}
		String sql = "";
		List<Map<String, Object>> ress = null;
		String fields = " cell,                                                  "
				+ "          NCELL,                                                "
				+ "         0 DEFINED_NEIGHBOUR,        "
				+ "         min(CELL_LON) CELL_LON,                                          "
				+ "         min(CELL_LAT) CELL_LAT,                                          "
				+ "         min(NCELL_LON) NCELL_LON,                                        "
				+ "         min(NCELL_LAT) NCELL_LAT,                                        "
				+ "         min(CELL_FREQ_CNT) CELL_FREQ_CNT,                                "
				+ "         min(NCELL_FREQ_CNT) NCELL_FREQ_CNT,                              "
				+ "         min(SAME_FREQ_CNT) SAME_FREQ_CNT,                                "
				+ "         min(ADJ_FREQ_CNT) ADJ_FREQ_CNT,                                  "
				+ "         sum(S3013) CI_DENOMINATOR,                                        "
				+ "         sum(S3013) CA_DENOMINATOR,                                        "
				+ "         sum(s361－s369) ci_divider,                                            "
				+ "         sum(s361－s366) ca_divider,                              "
				+ "         min(NCELLS) NCELLS,                                              "
				+ "         min(CELL_BEARING) CELL_BEARING,                                  "
				+ "         min(CELL_INDOOR) CELL_INDOOR,                                    "
				+ "         min(NCELL_INDOOR) NCELL_INDOOR,                                  "
				+ "         min(CELL_DOWNTILT) CELL_DOWNTILT,                                "
				+ "         min(CELL_SITE),                                                  "
				+ "         min(NCELL_SITE) NCELL_SITE,                                      "
				+ "         min(CELL_FREQ_SECTION) CELL_FREQ_SECTION,                        "
				+ "         min(NCELL_FREQ_SECTION) NCELL_FREQ_SECTION,                      "
				+ "         min(DISTANCE) DISTANCE,                                           "
				+ "          min(CELL_TO_NCELL_DIR) CELL_TO_NCELL_DIR";
		String group = " group by (cell, ncell)";
		for (int row = 0; row < cnt; row++) {
			int pageSize = 30000;
			/*
			 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
			 */
			sql = "select * " + "from (select * " + " from (select " + fields
					+ ", row_number() OVER(ORDER BY null) AS \"row_number\" "
					+ " from " + tmphwncssourceTab + " t " + group + ") p "
					+ " where p.\"row_number\" > " + row + ") q "
					+ " where rownum <=" + pageSize;
			ress = RnoHelper.commonQuery(stmt, sql);
			// 写入文件
			debug_output_exportOriginalNcsData(jobRec, ress);
			log.debug("hw本次完成写入记录数为：" + ress.size());
			row += pageSize - 1;// 步长
		}

		return cnt;
	}

	/**
	 * 
	 * @title 获取原始华为ncs数量
	 * @param stmt
	 * @param tmphwncssourceTab
	 * @return
	 * @author chao.xj
	 * @date 2014-9-19下午5:27:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int debug_output_originalHwNcsCnt(Statement stmt, String tmphwncssourceTab) {
		String sql = "select count(*) cnt from (select * from "
				+ tmphwncssourceTab + " )";
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		int cnt = 0;
		try {
			cnt = Integer.parseInt(res.get(0).get("CNT").toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cnt;
	}

	/**
	 * 结构分析入口
	 * 
	 * @param worker
	 * @param connection
	 * @param jobRec
	 * @param threshold
	 * @return
	 * @author brightming 2014-8-26 下午5:16:49
	 */
	/*
	 * public ResultInfo do2GStructAnalysis(JobWorker worker, Connection
	 * connection, RnoStructAnaJobRec jobRec, List<RnoThreshold> rnoThresholds)
	 * {
	 * 
	 * log.debug("do2GStructAnalysis当前运行线程：" + Thread.currentThread());
	 * Statement stmt = null; ResultInfo result = new ResultInfo(true);
	 * 
	 * try { stmt = connection.createStatement(); } catch (SQLException e1) {
	 * e1.printStackTrace(); result.setFlag(false); result.setMsg("准备数据库连接失败！");
	 * return result; }
	 * 
	 * long cityId = jobRec.getCityId(); long t1; long t2; Date begTime,
	 * endTime; JobReport report = new JobReport(jobRec.getJobId());
	 * 
	 * StructAnaTaskInfo anaTaskInfo = new StructAnaTaskInfo();
	 * anaTaskInfo.setCityId(cityId);
	 * anaTaskInfo.setEndDate(jobRec.getEndMeaTime());
	 * anaTaskInfo.setStartDate(jobRec.getBegMeaTime()); //
	 * anaTaskInfo.setThreshold(threshold);
	 * anaTaskInfo.setThresholds(rnoThresholds); Date d1 = new Date(); //
	 * 数据库表映射关系 Map<String, String> tableMap = new HashMap<String, String>(); //
	 * 转移2G小区到临时小区表 log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G小区到临时小区表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab
	 * tableMap.put("sourceTab", "cell"); tableMap.put("targetTab",
	 * "RNO_CELL_CITY_T"); begTime = new Date(); boolean ok =
	 * transfer2GCellToTempTable(stmt, anaTaskInfo, tableMap); endTime = new
	 * Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G小区到2G临时小区表，结果:" + ok + ",耗时：" + (t2
	 * - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G小区到2G临时小区表，无法进行运算！"); // report.setFields("整理小区数据",
	 * begTime, endTime, // JobState.Failed.getCode(), "");
	 * report.setSystemFields("整理小区数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { // report.setFields("整理小区数据", begTime, endTime,
	 * // JobState.Finished.getCode(), "");
	 * report.setSystemFields("整理小区数据", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * 
	 * // 转移2G爱立信NCS数据至临时表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信NCS数据至临时表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("sourceTab", "rno_2g_eri_ncs");
	 * tableMap.put("targetTab", "rno_2g_eri_ncs_t "); tableMap.put("descTab",
	 * "rno_2g_eri_ncs_descriptor"); begTime = new Date(); result =
	 * transfer2GEriNcsToTempTable(stmt, anaTaskInfo, tableMap); endTime = new
	 * Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信NCS数据至临时表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!result.isFlag()) { result.setFlag(false);
	 * result.setMsg("无法转移2G爱立信NCS数据至临时表，无法进行运算！"); //
	 * report.setFields("整理爱立信NCS数据", begTime, endTime, //
	 * JobState.Failed.getCode(), "");
	 * report.setSystemFields("整理爱立信NCS数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { // report.setFields("整理爱立信NCS数据", begTime,
	 * endTime, // JobState.Finished.getCode(), "");
	 * report.setSystemFields("整理爱立信NCS数据", begTime, endTime,
	 * JobState.Finished.getCode(), result.getAttach()+"");
	 * worker.addJobReport(report); } //
	 * chktablecount("!!!!!!transfer2GEriNcsToTempTable后",stmt);
	 * 
	 * // 转移2G爱立信NCS数据至统一分析表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信NCS数据至统一分析表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("tmperincssourceTab", "rno_2g_eri_ncs_t");
	 * tableMap.put("tmphwncssourceTab", "rno_2g_hw_ncs_t");
	 * tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T"); begTime = new
	 * Date(); ok = transfer2GEriNcsToUnifyAnaTable(stmt, anaTaskInfo, tableMap,
	 * rnoThresholds); endTime = new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信NCS数据至统一分析表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G爱立信NCS数据至统一分析表，无法进行运算！"); //
	 * report.setFields("统一爱立信与华为NCS数据", begTime, endTime, //
	 * JobState.Failed.getCode(), "");
	 * report.setSystemFields("统一爱立信NCS数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { // report.setFields("统一爱立信与华为NCS数据", begTime,
	 * endTime, // JobState.Finished.getCode(), "");
	 * report.setSystemFields("统一爱立信NCS数据", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * 
	 * // ---转移2G爱立信NCS数据至统一分析表后 删除2g爱立信临时表的内容 try { //
	 * stmt.executeUpdate("delete from rno_2g_eri_ncs_t");
	 * stmt.executeUpdate("truncate table rno_2g_eri_ncs_t"); } catch
	 * (SQLException e1) { e1.printStackTrace(); }
	 * 
	 * // 转移2G华为NCS数据至临时表 log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为NCS数据至临时表...");
	 * t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("sourceTab", "rno_2g_hw_ncs");
	 * tableMap.put("targetTab", "rno_2g_hw_ncs_t "); tableMap.put("descTab",
	 * "rno_2g_hw_ncs_desc"); begTime = new Date(); result =
	 * transfer2GHwNcsToTempTable(stmt, anaTaskInfo, tableMap); endTime = new
	 * Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为NCS数据至临时表，结果:" + ok + ",耗时：" + (t2
	 * - t1)); if (!result.isFlag()) { result.setFlag(false);
	 * result.setMsg("无法转移2G华为NCS数据至临时表，无法进行运算！"); //
	 * report.setFields("整理华为NCS数据", begTime, endTime, //
	 * JobState.Failed.getCode(), "");
	 * report.setSystemFields("整理华为NCS数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { // report.setFields("整理华为NCS数据", begTime,
	 * endTime, // JobState.Finished.getCode(), "");
	 * report.setSystemFields("整理华为NCS数据", begTime, endTime,
	 * JobState.Finished.getCode(), result.getAttach()+"");
	 * worker.addJobReport(report); } //
	 * chktablecount("!!!!!!transfer2GHwNcsToTempTable后",stmt);
	 * 
	 * // 转移2G华为NCS数据至统一分析表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为NCS数据至统一分析表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("tmperincssourceTab", "rno_2g_eri_ncs_t");
	 * tableMap.put("tmphwncssourceTab", "rno_2g_hw_ncs_t");
	 * tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T"); begTime = new
	 * Date(); ok = transfer2GHwNcsToUnifyAnaTable(stmt, anaTaskInfo, tableMap,
	 * rnoThresholds); endTime = new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为NCS数据至统一分析表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G华为NCS数据至统一分析表，无法进行运算！");
	 * report.setSystemFields("统一华为NCS数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { report.setSystemFields("统一华为NCS数据", begTime,
	 * endTime, JobState.Finished.getCode(), "");
	 * worker.addJobReport(report); }
	 * 
	 * // ---转移2G华为NCS数据至统一分析表后 删除2g华为临时表的内容 try { //
	 * stmt.executeUpdate("delete from rno_2g_hw_ncs_t");
	 * stmt.executeUpdate("truncate table rno_2g_hw_ncs_t"); } catch
	 * (SQLException e1) { e1.printStackTrace(); }
	 * 
	 * // 转移2G爱立信MRR数据至临时表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信MRR数据至临时表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("qualitysourceTab",
	 * "RNO_ERI_MRR_QUALITY"); tableMap.put("qualitytargetTab",
	 * "RNO_ERI_MRR_QUALITY_TEMP"); tableMap.put("strengthsourceTab",
	 * "RNO_ERI_MRR_STRENGTH"); tableMap.put("strengthtargetTab",
	 * "RNO_ERI_MRR_STRENGTH_TEMP"); tableMap.put("tasourceTab",
	 * "RNO_ERI_MRR_TA"); tableMap.put("tatargetTab", "RNO_ERI_MRR_TA_TEMP");
	 * tableMap.put("descTab", "rno_eri_mrr_descriptor"); begTime = new Date();
	 * ok = transfer2GEriMrrToTempTable(stmt, anaTaskInfo, tableMap); endTime =
	 * new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信MRR数据至临时表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G爱立信MRR数据至临时表，无法进行运算！");
	 * report.setSystemFields("整理爱立信MRR数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { report.setSystemFields("整理爱立信MRR数据", begTime,
	 * endTime, JobState.Finished.getCode(), "");
	 * worker.addJobReport(report); } //
	 * chktablecount("!!!!!!transfer2GEriMrrToTempTable后",stmt); //
	 * 转移2G爱立信MRR数据至统一分析表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信MRR数据至统一分析表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("tmpqualitysourceTab",
	 * "rno_eri_mrr_quality_temp"); tableMap.put("tmpstrengthsourceTab",
	 * "rno_eri_mrr_strength_temp"); tableMap.put("tmptasourceTab",
	 * "rno_eri_mrr_ta_temp"); tableMap.put("tmphratesourceTab",
	 * "rno_2g_hw_mrr_hrate_t"); tableMap.put("tmpfratesourceTab",
	 * "rno_2g_hw_mrr_frate_t"); tableMap.put("mrranatargetTab",
	 * "RNO_2G_MRR_ANA_T"); begTime = new Date(); ok =
	 * transfer2GEriMrrToUnifyAnaTable(stmt, anaTaskInfo, tableMap); endTime =
	 * new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信MRR数据至统一分析表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G爱立信MRR数据至统一分析表，无法进行运算！"); //
	 * report.setFields("统一爱立信与华为MRR数据", begTime, endTime, //
	 * JobState.Failed.getCode(), "");
	 * report.setSystemFields("统一爱立信MRR数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { report.setFields("统一爱立信MRR数据", begTime, endTime,
	 * JobState.Finished.getCode(), "");
	 * report.setSystemFields("统一爱立信MRR数据", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * // ---转移2G爱立信MRR数据至统一分析表后 删除爱立信临时mrr表数据 try { //
	 * stmt.executeUpdate("delete from RNO_ERI_MRR_QUALITY_TEMP");
	 * stmt.executeUpdate("truncate table RNO_ERI_MRR_QUALITY_TEMP"); } catch
	 * (SQLException e1) { e1.printStackTrace(); } try { //
	 * stmt.executeUpdate("delete from RNO_ERI_MRR_STRENGTH_TEMP");
	 * stmt.executeUpdate("truncate table RNO_ERI_MRR_STRENGTH_TEMP"); } catch
	 * (SQLException e1) { e1.printStackTrace(); } try { //
	 * stmt.executeUpdate("delete from RNO_ERI_MRR_TA_TEMP");
	 * stmt.executeUpdate("truncate table RNO_ERI_MRR_TA_TEMP"); } catch
	 * (SQLException e1) { e1.printStackTrace(); }
	 * 
	 * // 转移2G华为MRR数据至临时表 log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为MRR数据至临时表...");
	 * t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("hratesourceTab", "rno_2g_hw_mrr_hrate");
	 * tableMap.put("hratetargetTab", "rno_2g_hw_mrr_hrate_t");
	 * tableMap.put("fratesourceTab", "rno_2g_hw_mrr_frate");
	 * tableMap.put("fratetargetTab", "rno_2g_hw_mrr_frate_t");
	 * tableMap.put("descTab", "rno_2g_hw_mrr_desc"); begTime = new Date(); ok =
	 * transfer2GHwMrrToTempTable(stmt, anaTaskInfo, tableMap); endTime = new
	 * Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为MRR数据至临时表，结果:" + ok + ",耗时：" + (t2
	 * - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G华为MRR数据至临时表，无法进行运算！"); //
	 * report.setFields("整理华为MRR数据", begTime, endTime, //
	 * JobState.Failed.getCode(), "");
	 * report.setSystemFields("整理华为MRR数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { // report.setFields("整理华为MRR数据", begTime,
	 * endTime, // JobState.Finished.getCode(), "");
	 * report.setSystemFields("整理华为MRR数据", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * // 转移2G华为MRR数据至统一分析表
	 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为MRR数据至统一分析表..."); t1 =
	 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
	 * tableMap.clear(); tableMap.put("tmpqualitysourceTab",
	 * "rno_eri_mrr_quality_temp"); tableMap.put("tmpstrengthsourceTab",
	 * "rno_eri_mrr_strength_temp"); tableMap.put("tmptasourceTab",
	 * "rno_eri_mrr_ta_temp"); tableMap.put("tmphratesourceTab",
	 * "rno_2g_hw_mrr_hrate_t"); tableMap.put("tmpfratesourceTab",
	 * "rno_2g_hw_mrr_frate_t"); tableMap.put("mrranatargetTab",
	 * "RNO_2G_MRR_ANA_T"); begTime = new Date(); ok =
	 * transfer2GHwMrrToUnifyAnaTable(stmt, anaTaskInfo, tableMap); endTime =
	 * new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为MRR数据至统一分析表，结果:" + ok + ",耗时：" +
	 * (t2 - t1)); if (!ok) { result.setFlag(false);
	 * result.setMsg("无法转移2G爱立信MRR数据至统一分析表，无法进行运算！");
	 * report.setSystemFields("统一华为MRR数据", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { report.setFields("统一华为MRR数据", begTime, endTime,
	 * JobState.Finished.getCode(), "");
	 * report.setSystemFields("统一华为MRR数据", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * // ---转移2G爱立信MRR数据至统一分析表后 删除华为临时mrr表数据 try { //
	 * stmt.executeUpdate("delete from rno_2g_hw_mrr_hrate_t");
	 * stmt.executeUpdate("truncate table rno_2g_hw_mrr_hrate_t"); } catch
	 * (SQLException e1) { e1.printStackTrace(); } try { //
	 * stmt.executeUpdate("delete from rno_2g_hw_mrr_frate_t");
	 * stmt.executeUpdate("truncate table rno_2g_hw_mrr_frate_t"); } catch
	 * (SQLException e1) { e1.printStackTrace(); }
	 * 
	 * // chktablecount("!!!!!!transfer2GHwMrrToTempTable后",stmt);
	 * 
	 * // --------------输出2g ncs数据到文件供分析--------------// exportNcsData(jobRec,
	 * stmt);
	 * 
	 * report.setFields("完成数据准备", d1, new Date(),
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report);
	 * // 计算簇 log.debug("汇总>>>>>>>>>>>>>>>开始计算最大连通簇..."); t1 =
	 * System.currentTimeMillis(); begTime = new Date(); ok =
	 * calculate2GConnectedCluster(connection, "rno_2g_ncs_ana_t",
	 * "RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID",
	 * "RNO_NCS_CLUSTER_CELL_RELA_MID", rnoThresholds); endTime = new Date(); t2
	 * = System.currentTimeMillis(); log.debug("汇总<<<<<<<<<<<<<<<完成计算最大连通簇。耗时:"
	 * + (t2 - t1) + "ms"); if (!ok) { result.setFlag(false);
	 * result.setMsg("计算最大连通簇失败！"); report.setFields("计算最大连通簇", begTime,
	 * endTime, JobState.Failed.getCode(), "");
	 * worker.addJobReport(report); return result; } else {
	 * report.setFields("计算最大连通簇", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * // 计算簇约束因子 log.debug("汇总>>>>>>>>>>>>>>>开始计算簇约束因子..."); t1 = t2; begTime =
	 * new Date(); ok = calculate2GClusterConstrain(stmt, "RNO_NCS_CLUSTER_MID",
	 * "RNO_NCS_CLUSTER_CELL_MID", rnoThresholds); endTime = new Date(); t2 =
	 * System.currentTimeMillis(); log.debug("汇总<<<<<<<<<<<<<<<完成计算簇约束因子。耗时:" +
	 * (t2 - t1) + "ms"); if (!ok) { result.setFlag(false);
	 * result.setMsg("计算簇约束因子失败！"); report.setFields("计算簇约束因子", begTime,
	 * endTime, JobState.Failed.getCode(), "");
	 * worker.addJobReport(report); return result; } else {
	 * report.setFields("计算簇约束因子", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * 
	 * //---暂时注释掉---// // 簇权重 // log.debug("汇总>>>>>>>>>>>>>>>开始计算簇权重..."); // t1
	 * = t2; // begTime = new Date(); // ok =
	 * calculate2GNcsClusterWeight(connection, "rno_2g_ncs_ana_t", //
	 * "RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID", // Arrays.asList(-1L,
	 * -2L), true, 0.5f); // endTime = new Date(); // t2 =
	 * System.currentTimeMillis(); // log.debug("汇总<<<<<<<<<<<<<<<完成计算簇权重。耗时:" +
	 * (t2 - t1) + "ms"); // if (!ok) { // result.setFlag(false); //
	 * result.setMsg("计算簇权重失败！"); // report.setFields("计算簇权重", begTime, endTime,
	 * // JobState.Failed.getCode(), ""); // worker.addJobReport(report);
	 * // return result; // } else { // report.setFields("计算簇权重", begTime,
	 * endTime, // JobState.Finished.getCode(), ""); //
	 * worker.addJobReport(report); // }
	 * 
	 * // 结构指数计算 log.debug("汇总>>>>>>>>>>>>>>>开始计算结构指数..."); t1 = t2;
	 * tableMap.clear(); tableMap.put("ncsTab", "RNO_2G_NCS_ANA_T");
	 * tableMap.put("cellResTab", "RNO_NCS_CELL_ANA_RESULT_MID");
	 * tableMap.put("clusterTab", "RNO_NCS_CLUSTER_MID");
	 * tableMap.put("clusterCellTab", "RNO_NCS_CLUSTER_CELL_MID");
	 * tableMap.put("cellMrrAnaTab", "rno_2g_mrr_ana_t"); begTime = new Date();
	 * ok = calculate2GCellRes(worker,report,connection, tableMap,
	 * rnoThresholds); endTime = new Date(); t2 = System.currentTimeMillis();
	 * log.debug("汇总<<<<<<<<<<<<<<<完成计算结构指数。耗时:" + (t2 - t1) + "ms"); if (!ok) {
	 * result.setFlag(false); result.setMsg("计算结构指数失败！");
	 * report.setFields("计算结构指数", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report);
	 * return result; } else { report.setFields("计算结构指数", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * 
	 * // ---------自动优化----// // log.debug("汇总ncs>>>>>>>>>>>>>>>开始产生自动优化建议...");
	 * // t1 = t2; // pick2GCellsWithProblem(stmt, cityId); // t2 =
	 * System.currentTimeMillis(); //
	 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成自动优化建议生成。耗时:" + (t2 - t1) // + "ms");
	 * 
	 * try { // 保存分析结果 begTime = new Date(); result = saveStructAnaResult(stmt,
	 * jobRec); endTime = new Date(); if (result.isFlag()) {
	 * report.setFields("保存分析结果", begTime, endTime,
	 * JobState.Finished.getCode(), ""); worker.addJobReport(report); }
	 * else { report.setFields("保存分析结果", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report); } }
	 * catch (Exception e) { endTime = new Date(); e.printStackTrace();
	 * result.setFlag(false); result.setMsg("保存汇总结果时失败!"); // 报告
	 * report.setFields("保存分析结果", begTime, endTime,
	 * JobState.Failed.getCode(), ""); worker.addJobReport(report); }
	 * return result; }
	 */
	/**
	 * 
	 * @title 结构分析入口
	 * @param worker
	 * @param connection
	 * @param jobRec
	 * @param analysisTask
	 * @return
	 * @author chao.xj
	 * @date 2014-9-10下午12:27:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public ResultInfo do2GStructAnalysis(RnoStructAnaJobRunnable worker,
			Connection connection, RnoStructAnaJobRec jobRec,
			RnoStructureAnalysisTask analysisTask) {

		log.debug("do2GStructAnalysis当前运行线程：" + Thread.currentThread());
		List<RnoThreshold> rnoThresholds = null;
		RnoStructureAnalysisTask.TaskInfo ti = null;

		Statement stmt = null;
		ResultInfo result = new ResultInfo(true);

		if (analysisTask != null) {
			rnoThresholds = analysisTask.getRnoThresholds();
			ti = analysisTask.getTaskInfo();
		} else {
			result.setFlag(false);
			result.setMsg("结构分析任务对象为空！");
			return result;
		}

		try {
			stmt = connection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备数据库连接失败！");
			return result;
		}
		// 准备城市信息
		String cityName = "";
		SysArea sa = AuthDsDataDaoImpl.getSysAreaByAreaId(jobRec.getCityId());
		if (sa != null) {
			cityName = sa.getName();
		}
		jobRec.setCityName(cityName);

		// 准备工作路径
		log.debug("准备结构分析结果保存的路径...");
		String tmpParentDir = System.getProperty("java.io.tmpdir");
		String tmpDir = tmpParentDir + "/"
				+ UUID.randomUUID().toString().replaceAll("-", "") + "/";
		// 确定临时目录存在
		File tmpDirFile = new File(tmpDir);
		if (!tmpDirFile.exists()) {
			tmpDirFile.mkdirs();
		}
		jobRec.setTmpDir(tmpDir);
		// ---工作路径准备完毕
		long cityId = jobRec.getCityId();
		long t1;
		long t2;
		Date stepBegTime, stepEndTime;
		JobReport report = new JobReport(jobRec.getJobId());

		StructAnaTaskInfo anaTaskInfo = new StructAnaTaskInfo();
		anaTaskInfo.setCityId(cityId);
		anaTaskInfo.setEndDate(jobRec.getEndMeaTime());
		anaTaskInfo.setStartDate(jobRec.getBegMeaTime());
		// anaTaskInfo.setThreshold(threshold);
		anaTaskInfo.setThresholds(rnoThresholds);
		Date sectionBegTime = new Date();
		// 数据库表映射关系
		Map<String, String> tableMap = new HashMap<String, String>();
		// 转移2G小区到临时小区表
		log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G小区到临时小区表...");
		t1 = System.currentTimeMillis();
		// sourceTab,targetTab
		tableMap.put("sourceTab", "cell");
		tableMap.put("targetTab", "RNO_CELL_CITY_T");
		stepBegTime = new Date();
		boolean ok = transfer2GCellToTempTable(stmt, anaTaskInfo, tableMap);
		stepEndTime = new Date();
		t2 = System.currentTimeMillis();
		log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G小区到2G临时小区表，结果:" + ok + ",耗时："
				+ (t2 - t1));
		if (!ok) {
			result.setFlag(false);
			result.setMsg("无法转移2G小区到2G临时小区表，无法进行运算！");
			// report.setFields("整理小区数据", begTime, endTime,
			// JobState.Failed.getCode(), "");
			report.setSystemFields("整理小区数据", stepBegTime, stepEndTime,
					JobState.Failed.getCode(), "");
			worker.addJobReport(report);
			return result;
		} else {
			// report.setFields("整理小区数据", begTime, endTime,
			// JobState.Finished.getCode(), "");
			report.setSystemFields("整理小区数据", stepBegTime, stepEndTime,
					JobState.Finished.getCode(), "");
			worker.addJobReport(report);
		}
		// 是否使用爱立信数据
		Map<String, Boolean> busDataTypes = ti.getBusDataType();
		// 计算过程
		Map<String, Boolean> calProcedures = ti.getCalProcedure();
		if (busDataTypes.get("USEERIDATA")) {
			// 2014-9-24 gmh注释，修改为直接从系统表将数据转移到统一分析表
			/*
			 * // 转移2G爱立信NCS数据至临时表
			 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信NCS数据至临时表..."); t1 =
			 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
			 * tableMap.clear(); tableMap.put("sourceTab", "rno_2g_eri_ncs");
			 * tableMap.put("targetTab", "rno_2g_eri_ncs_t ");
			 * tableMap.put("descTab", "rno_2g_eri_ncs_descriptor"); begTime =
			 * new Date(); result = transfer2GEriNcsToTempTable(stmt,
			 * anaTaskInfo, tableMap); endTime = new Date(); t2 =
			 * System.currentTimeMillis();
			 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信NCS数据至临时表，结果:" + ok +
			 * ",耗时：" + (t2 - t1)); if (!result.isFlag()) {
			 * result.setFlag(false);
			 * result.setMsg("无法转移2G爱立信NCS数据至临时表，无法进行运算！"); //
			 * report.setFields("整理爱立信NCS数据", begTime, endTime, //
			 * JobState.Failed.getCode(), "");
			 * report.setSystemFields("整理爱立信NCS数据", begTime, endTime,
			 * JobState.Failed.getCode(), "");
			 * worker.addJobReport(report); return result; } else { //
			 * report.setFields("整理爱立信NCS数据", begTime, endTime, //
			 * JobState.Finished.getCode(), "");
			 * report.setSystemFields("整理爱立信NCS数据", begTime, endTime,
			 * JobState.Finished.getCode(), result.getMsg());
			 * worker.addJobReport(report); } //
			 * chktablecount("!!!!!!transfer2GEriNcsToTempTable后",stmt);
			 */
			
			//-------------------------------爱立信ncs数据 开始-----------------------------//
			// 转移2G爱立信NCS数据至统一分析表
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信NCS数据至统一分析表...");
			t1 = System.currentTimeMillis();
			// sourceTab,targetTab,descTab
			tableMap.clear();

			tableMap.put("erincssourceTab", "rno_2g_eri_ncs");
			tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T");
			tableMap.put("outputOverLapDetail", "true");
			stepBegTime = new Date();
			ok = transfer2GEriNcsToUnifyAnaTable(stmt, anaTaskInfo, tableMap,
					rnoThresholds, jobRec);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信NCS数据至统一分析表，结果:" + ok
					+ ",耗时：" + (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G爱立信NCS数据至统一分析表，无法进行运算！");
				// report.setFields("统一爱立信与华为NCS数据", begTime, endTime,
				// JobState.Failed.getCode(), "");
				report.setSystemFields("统一爱立信NCS数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				// report.setFields("统一爱立信与华为NCS数据", begTime, endTime,
				// JobState.Finished.getCode(), "");
				report.setSystemFields("统一爱立信NCS数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			// －－－获取未经过滤的爱立信原始NCS数据
			// getOriginalEriNcsDataByPage(jobRec, stmt, "rno_2g_eri_ncs_t");
			// --------------输出2g 原始的ncs数据到文件供分析--------------//
			report.setFields("完成爱立信NCS数据准备", sectionBegTime, new Date(),
					JobState.Finished.getCode(), "");
			worker.addJobReport(report);
			//-------------------------------爱立信ncs数据 结束-----------------------------//
			
			// ---转移2G爱立信NCS数据至统一分析表后 删除2g爱立信临时表的内容
			try {
				// stmt.executeUpdate("delete from rno_2g_eri_ncs_t");
				stmt.executeUpdate("truncate table rno_2g_eri_ncs_t");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			//-------------------------------爱立信mrr数据 开始-----------------------------//
			// 转移2G爱立信MRR数据至临时表
			sectionBegTime=new Date();
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信MRR数据至临时表...");
			t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
			tableMap.clear();
			tableMap.put("qualitysourceTab", "RNO_ERI_MRR_QUALITY");
			tableMap.put("qualitytargetTab", "RNO_ERI_MRR_QUALITY_TEMP");
			tableMap.put("strengthsourceTab", "RNO_ERI_MRR_STRENGTH");
			tableMap.put("strengthtargetTab", "RNO_ERI_MRR_STRENGTH_TEMP");
			tableMap.put("tasourceTab", "RNO_ERI_MRR_TA");
			tableMap.put("tatargetTab", "RNO_ERI_MRR_TA_TEMP");
			tableMap.put("descTab", "rno_eri_mrr_descriptor");
			stepBegTime = new Date();
			ok = transfer2GEriMrrToTempTable(stmt, anaTaskInfo, tableMap);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信MRR数据至临时表，结果:" + ok
					+ ",耗时：" + (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G爱立信MRR数据至临时表，无法进行运算！");
				report.setSystemFields("整理爱立信MRR数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setSystemFields("整理爱立信MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			// chktablecount("!!!!!!transfer2GEriMrrToTempTable后",stmt);
			// 转移2G爱立信MRR数据至统一分析表
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G爱立信MRR数据至统一分析表...");
			t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
			tableMap.clear();
			tableMap.put("tmpqualitysourceTab", "rno_eri_mrr_quality_temp");
			tableMap.put("tmpstrengthsourceTab", "rno_eri_mrr_strength_temp");
			tableMap.put("tmptasourceTab", "rno_eri_mrr_ta_temp");
			tableMap.put("tmphratesourceTab", "rno_2g_hw_mrr_hrate_t");
			tableMap.put("tmpfratesourceTab", "rno_2g_hw_mrr_frate_t");
			tableMap.put("mrranatargetTab", "RNO_2G_MRR_ANA_T");
			stepBegTime = new Date();
			ok = transfer2GEriMrrToUnifyAnaTable(stmt, anaTaskInfo, tableMap);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G爱立信MRR数据至统一分析表，结果:" + ok
					+ ",耗时：" + (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G爱立信MRR数据至统一分析表，无法进行运算！");
				// report.setFields("统一爱立信与华为MRR数据", begTime, endTime,
				// JobState.Failed.getCode(), "");
				report.setSystemFields("统一爱立信MRR数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setFields("统一爱立信MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				report.setSystemFields("统一爱立信MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			
			report.setFields("完成爱立信MRR数据准备", sectionBegTime, new Date(),
					JobState.Finished.getCode(), "");
			worker.addJobReport(report);
			//-------------------------------爱立信mrr数据 结束-----------------------------//
			
			// ---转移2G爱立信MRR数据至统一分析表后 删除爱立信临时mrr表数据
			try {
				// stmt.executeUpdate("delete from RNO_ERI_MRR_QUALITY_TEMP");
				stmt.executeUpdate("truncate table RNO_ERI_MRR_QUALITY_TEMP");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				// stmt.executeUpdate("delete from RNO_ERI_MRR_STRENGTH_TEMP");
				stmt.executeUpdate("truncate table RNO_ERI_MRR_STRENGTH_TEMP");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				// stmt.executeUpdate("delete from RNO_ERI_MRR_TA_TEMP");
				stmt.executeUpdate("truncate table RNO_ERI_MRR_TA_TEMP");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

		// 是否使用华为数据
		if (busDataTypes.get("USEHWDATA")) {
			/*
			 * 2014-9-24 gmh 注释，直接从华为的数据表将数据转移到统一分析表 // 转移2G华为NCS数据至临时表
			 * log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为NCS数据至临时表..."); t1 =
			 * System.currentTimeMillis(); // sourceTab,targetTab,descTab
			 * tableMap.clear(); tableMap.put("sourceTab", "rno_2g_hw_ncs");
			 * tableMap.put("targetTab", "rno_2g_hw_ncs_t ");
			 * tableMap.put("descTab", "rno_2g_hw_ncs_desc"); begTime = new
			 * Date(); result = transfer2GHwNcsToTempTable(stmt, anaTaskInfo,
			 * tableMap); endTime = new Date(); t2 = System.currentTimeMillis();
			 * log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为NCS数据至临时表，结果:" + ok +
			 * ",耗时：" + (t2 - t1)); if (!result.isFlag()) {
			 * result.setFlag(false);
			 * result.setMsg("无法转移2G华为NCS数据至临时表，无法进行运算！"); //
			 * report.setFields("整理华为NCS数据", begTime, endTime, //
			 * JobState.Failed.getCode(), "");
			 * report.setSystemFields("整理华为NCS数据", begTime, endTime,
			 * JobState.Failed.getCode(), "");
			 * worker.addJobReport(report); return result; } else { //
			 * report.setFields("整理华为NCS数据", begTime, endTime, //
			 * JobState.Finished.getCode(), "");
			 * report.setSystemFields("整理华为NCS数据", begTime, endTime,
			 * JobState.Finished.getCode(), result.getMsg());
			 * worker.addJobReport(report); } //
			 * chktablecount("!!!!!!transfer2GHwNcsToTempTable后",stmt);
			 */
			
			//-------------------------------华为ncs数据 开始-----------------------------//
			// 转移2G华为NCS数据至统一分析表
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为NCS数据至统一分析表...");
			t1 = System.currentTimeMillis();
			// sourceTab,targetTab,descTab
			tableMap.clear();
			tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T");
			tableMap.put("outputOverLapDetail", "true");
			stepBegTime = new Date();
			sectionBegTime=new Date();
			ok = transfer2GHwNcsToUnifyAnaTable(stmt, anaTaskInfo, tableMap,
					rnoThresholds, jobRec);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为NCS数据至统一分析表，结果:" + ok
					+ ",耗时：" + (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G华为NCS数据至统一分析表，无法进行运算！");
				report.setSystemFields("统一华为NCS数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setSystemFields("统一华为NCS数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			// －－－获取未经过滤的华为原始NCS数据
			debug_output_getOriginalHwNcsDataByPage(jobRec, stmt, "rno_2g_hw_ncs_t");
			// --------------输出2g 原始的ncs数据到文件供分析--------------//
			report.setFields("完成华为NCS数据准备", sectionBegTime, new Date(),
					JobState.Finished.getCode(), "");
			worker.addJobReport(report);
			// ---转移2G华为NCS数据至统一分析表后 删除2g华为临时表的内容
			try {
				// stmt.executeUpdate("delete from rno_2g_hw_ncs_t");
				stmt.executeUpdate("truncate table rno_2g_hw_ncs_t");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//-------------------------------华为ncs数据 结束-----------------------------//
			
			//-------------------------------华为mrr数据 结束-----------------------------//
			// 转移2G华为MRR数据至临时表
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为MRR数据至临时表...");
			t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
			tableMap.clear();
			tableMap.put("hratesourceTab", "rno_2g_hw_mrr_hrate");
			tableMap.put("hratetargetTab", "rno_2g_hw_mrr_hrate_t");
			tableMap.put("fratesourceTab", "rno_2g_hw_mrr_frate");
			tableMap.put("fratetargetTab", "rno_2g_hw_mrr_frate_t");
			tableMap.put("descTab", "rno_2g_hw_mrr_desc");
			stepBegTime = new Date();
			ok = transfer2GHwMrrToTempTable(stmt, anaTaskInfo, tableMap);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为MRR数据至临时表，结果:" + ok + ",耗时："
					+ (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G华为MRR数据至临时表，无法进行运算！");
				// report.setFields("整理华为MRR数据", begTime, endTime,
				// JobState.Failed.getCode(), "");
				report.setSystemFields("整理华为MRR数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				// report.setFields("整理华为MRR数据", begTime, endTime,
				// JobState.Finished.getCode(), "");
				report.setSystemFields("整理华为MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			// 转移2G华为MRR数据至统一分析表
			log.debug("汇总ncs>>>>>>>>>>>>>>>开始转移2G华为MRR数据至统一分析表...");
			t1 = System.currentTimeMillis(); // sourceTab,targetTab,descTab
			tableMap.clear();
			tableMap.put("tmpqualitysourceTab", "rno_eri_mrr_quality_temp");
			tableMap.put("tmpstrengthsourceTab", "rno_eri_mrr_strength_temp");
			tableMap.put("tmptasourceTab", "rno_eri_mrr_ta_temp");
			tableMap.put("tmphratesourceTab", "rno_2g_hw_mrr_hrate_t");
			tableMap.put("tmpfratesourceTab", "rno_2g_hw_mrr_frate_t");
			tableMap.put("mrranatargetTab", "RNO_2G_MRR_ANA_T");
			stepBegTime = new Date();
			ok = transfer2GHwMrrToUnifyAnaTable(stmt, anaTaskInfo, tableMap);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总ncs<<<<<<<<<<<<<<<完成转移2G华为MRR数据至统一分析表，结果:" + ok
					+ ",耗时：" + (t2 - t1));
			if (!ok) {
				result.setFlag(false);
				result.setMsg("无法转移2G爱立信MRR数据至统一分析表，无法进行运算！");
				report.setSystemFields("统一华为MRR数据", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setFields("统一华为MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				report.setSystemFields("统一华为MRR数据", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
			
			report.setFields("完成华为MRR数据准备", stepBegTime, new Date(),
					JobState.Finished.getCode(), "");
			worker.addJobReport(report);
			//-------------------------------华为mrr数据 结束-----------------------------//
			
			// ---转移2G爱立信MRR数据至统一分析表后 删除华为临时mrr表数据
			try {
				// stmt.executeUpdate("delete from rno_2g_hw_mrr_hrate_t");
				stmt.executeUpdate("truncate table rno_2g_hw_mrr_hrate_t");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				// stmt.executeUpdate("delete from rno_2g_hw_mrr_frate_t");
				stmt.executeUpdate("truncate table rno_2g_hw_mrr_frate_t");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			// chktablecount("!!!!!!transfer2GHwMrrToTempTable后",stmt);
		}

		// --------------输出2g 过滤后的ncs数据到文件供分析--------------//
		debug_output_getFilteredNcsDataByPage(jobRec, stmt, "rno_2g_ncs_ana_t");
		// --------------输出2g 过滤后的ncs数据到文件供分析--------------//
//		report.setFields("完成NCS统一数据准备", d1, new Date(),
//				JobState.Finished.getCode(), "");
//		worker.addJobReport(report);

		
		//缓存小区主要数据
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>>>>开始缓存小区主要数据...");
		Map<String, CachedCellData> cachedCellInfos= prepareCachedCellData(stmt);
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<<<完成缓存小区主要数据。耗时:" + (t2 - t1) + "ms");
	
		// 计算簇
		if (calProcedures.get("CALCONCLUSTER")) {
			log.debug("汇总>>>>>>>>>>>>>>>开始计算最大连通簇...");
			t1 = System.currentTimeMillis();
			stepBegTime = new Date();
			ResultInfo resultInfo = calculate2GConnectedCluster(connection, "rno_2g_ncs_ana_t",
					"RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID",
					"RNO_NCS_CLUSTER_CELL_RELA_MID", rnoThresholds, jobRec,cachedCellInfos);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总<<<<<<<<<<<<<<<完成计算最大连通簇。耗时:" + (t2 - t1) + "ms");
			if (!resultInfo.isFlag()) {
				result.setFlag(false);
				result.setMsg("计算最大连通簇失败！");
				report.setFields("计算最大连通簇", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setFields("计算最大连通簇", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
		}
		

		// 结构指数计算
		if (calProcedures.get("CALCELLRES")) {
			log.debug("汇总>>>>>>>>>>>>>>>开始计算结构指数...");
			t1 = t2;
			tableMap.clear();
			tableMap.put("ncsTab", "RNO_2G_NCS_ANA_T");
			tableMap.put("cellResTab", "RNO_NCS_CELL_ANA_RESULT_MID");
			tableMap.put("clusterTab", "RNO_NCS_CLUSTER_MID");
			tableMap.put("clusterCellTab", "RNO_NCS_CLUSTER_CELL_MID");
			tableMap.put("cellMrrAnaTab", "rno_2g_mrr_ana_t");
			stepBegTime = new Date();
			boolean useIdealCoverDis = calProcedures.get("CALIDEALDIS");
			ok = calculate2GCellRes(jobRec.getCityId(), worker, report,
					connection, tableMap, rnoThresholds, useIdealCoverDis);
			stepEndTime = new Date();
			t2 = System.currentTimeMillis();
			log.debug("汇总<<<<<<<<<<<<<<<完成计算结构指数。耗时:" + (t2 - t1) + "ms");
			if (!ok) {
				result.setFlag(false);
				result.setMsg("计算结构指数失败！");
				report.setFields("计算结构指数", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
				return result;
			} else {
				report.setFields("计算结构指数", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			}
		}

		// ---------自动优化----//
		// log.debug("汇总ncs>>>>>>>>>>>>>>>开始产生自动优化建议...");
		// t1 = t2;
		// pick2GCellsWithProblem(stmt, cityId);
		// t2 = System.currentTimeMillis();
		// log.debug("汇总ncs<<<<<<<<<<<<<<<完成自动优化建议生成。耗时:" + (t2 - t1)
		// + "ms");

		try {
			// 保存分析结果
			stepBegTime = new Date();
			result = saveStructAnaResult(stmt, jobRec,cachedCellInfos);
			stepEndTime = new Date();
			if (result.isFlag()) {
				report.setFields("保存分析结果", stepBegTime, stepEndTime,
						JobState.Finished.getCode(), "");
				worker.addJobReport(report);
			} else {
				report.setFields("保存分析结果", stepBegTime, stepEndTime,
						JobState.Failed.getCode(), "");
				worker.addJobReport(report);
			}
		} catch (Exception e) {
			stepEndTime = new Date();
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("保存汇总结果时失败!");
			// 报告
			report.setFields("保存分析结果", stepBegTime, stepEndTime,
					JobState.Failed.getCode(), "");
			worker.addJobReport(report);
		}
		return result;
	}

	/**
	 * 
	 * @title 转移2G小区数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:sourceTab,targetTab val:from cell to rno_cell_city_t
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:28:00
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GCellToTempTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) {

		// String sql = "";
		// String sourceTab = tableMap.get("sourceTab");
		String targetTab = tableMap.get("targetTab");
		long cityId = anaTaskInfo.getCityId();
		RnoStructAnaV2Impl tool = new RnoStructAnaV2Impl();
		ResultInfo result = tool.prepareEriCityCellData(stmt,
				"RNO_CELL_CITY_T", cityId);
		return result.isFlag();

		// String fields="ID,"
		// +"NAME,"
		// +"LABEL,"
		// +"GSMFREQUENCESECTION,"
		// +"LAC,"
		// +"CI,"
		// +"BSIC,"
		// +"BCCH,"
		// +"TCH,"
		// +"ANT_HEIGH,"
		// +"BASETYPE,"
		// +"DOWNTILT,"
		// +"BEARING,"
		// +"LONGITUDE,"
		// +"LATITUDE,"
		// +"SITE,"
		// +"BSC_ID,"
		// +"BTSTYPE,"
		// +"AREA_ID,"
		// +"HEIGHT_ABOVE_SEA,"
		// +"INDOOR_CELL_TYPE";
		//
		// try {
		// sql = "insert into "
		// + targetTab+"("+fields+")"
		// +"select "
		// +"ID,"
		// +"NAME,"
		// +"LABEL,"
		// +"GSMFREQUENCESECTION,"
		// +"LAC,"
		// +"CI,"
		// +"BSIC,"
		// +"BCCH,"
		// +"TCH,"
		// +"ANT_HEIGH,"
		// +"BASETYPE,"
		// +"DOWNTILT,"
		// +"BEARING,"
		// +"LONGITUDE,"
		// +"LATITUDE,"
		// +"SITE,"
		// +"BSC_ID,"
		// +"BTSTYPE,"
		// +"AREA_ID,"
		// +"HEIGHT_ABOVE_SEA,"
		// +"INDOOR_CELL_TYPE"
		// + "    from "
		// + sourceTab
		// +
		// "   where area_id in (select area_id from sys_area where parent_id="+cityId+")";
		//
		//
		// String innerSql = "SELECT MID.LABEL,MID.NAME, "
		// +
		// " (CASE WHEN GSMFREQUENCESECTION IS NULL THEN (CASE WHEN BCCH IS NOT NULL THEN (CASE WHEN BCCH>100 AND BCCH<1000 THEN 'GSM1800' ELSE 'GSM900' END) ELSE NULL END) ELSE GSMFREQUENCESECTION END) AS GSMFREQUENCESECTION,"
		// + " LAC, "
		// + " CI, "
		// +
		// " (case when length(to_char(bsic))=1 then '0'||to_char(bsic) else to_char(bsic) end) BSIC, "
		// + " BCCH, "
		// + " TCH, "
		// + " DOWNTILT, "
		// + " BEARING, "
		// + " LONGITUDE, "
		// + " LATITUDE, "
		// + " SITE, "
		// + " RNO_BSC.ENGNAME, "
		// + " BTSTYPE, "
		// +
		// " (CASE WHEN INDOOR_CELL_TYPE='室内覆盖' THEN 'Y' ELSE 'N' END) AS INDOOR_CELL_TYPE, "
		// + " RNO_GET_FREQ_CNTV2(BCCH,TCH) AS CELL_FREQ_CNT "
		// + " FROM "
		// + " ( "
		// +
		// " SELECT LABEL,NAME,GSMFREQUENCESECTION,LAC,CI,BSIC,BCCH,TCH,DOWNTILT,BEARING,LONGITUDE,LATITUDE,SITE,BSC_ID,BTSTYPE,INDOOR_CELL_TYPE,ROW_NUMBER() OVER(PARTITION BY LABEL ORDER BY LABEL NULLS LAST) RN "
		// +
		// " FROM CELL WHERE CELL.AREA_ID IN (SELECT AREA_ID FROM SYS_AREA WHERE PARENT_ID="
		// + cityId + " OR AREA_ID=" + cityId + ") "
		// + " )MID  LEFT JOIN RNO_BSC  "
		// + " ON(MID.BSC_ID=RNO_BSC.BSC_ID) and rn=1";
		//
		//
		//
		// log.debug("执行transfer2GCellToTempTable前的sql:" + sql);
		// int rows = stmt.executeUpdate(sql);
		// if (rows > 0) {
		// return true;
		// }
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// log.error("转移2G小区数据到临时表出错sql=" + sql);
		// } finally {
		// // try {
		// // stmt.close();
		// // } catch (SQLException e) {
		// // }
		// }
		// return false;
	}

	/**
	 * 
	 * @title 转移2G爱立信NCS数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:sourceTab,targetTab,descTab 旧val:from rno_ncs to
	 *            rno_ncs_mid desc:rno_ncs_descriptor
	 *            新val:rno_2g_eri_ncs,rno_2g_eri_ncs_t
	 *            ,rno_2g_eri_ncs_descriptor;
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:30:12
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*
	 * @Deprecated public ResultInfo transfer2GEriNcsToTempTable(Statement stmt,
	 * StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) { // from
	 * rno_ncs to rno_ncs_mid desc:rno_ncs_descriptor ResultInfo result = new
	 * ResultInfo(); result.setFlag(true); String sql = ""; String sourceTab =
	 * tableMap.get("sourceTab"), targetTab = tableMap .get("targetTab"),
	 * descTab = tableMap.get("descTab"); SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String startDate =
	 * formatter.format(anaTaskInfo.getStartDate()); String endDate =
	 * formatter.format(anaTaskInfo.getEndDate()); long cityId =
	 * anaTaskInfo.getCityId(); try { sql = "insert into " + targetTab + "  (  "
	 * +
	 * "   CELL,                                                                         "
	 * +
	 * "   CHGR,                                                                         "
	 * +
	 * "   BSIC,                                                                         "
	 * +
	 * "   ARFCN,                                                                        "
	 * +
	 * "   DEFINED_NEIGHBOUR,                                                            "
	 * +
	 * "   RECTIMEARFCN,                                                                 "
	 * +
	 * "   REPARFCN,                                                                     "
	 * +
	 * "   NCELL,                                                                        "
	 * +
	 * "   DISTANCE,                                                                     "
	 * +
	 * "   INTERFER,                                                                     "
	 * +
	 * "   CA_INTERFER,                                                                  "
	 * +
	 * "   NCELLS,                                                                       "
	 * +
	 * "   CELL_FREQ_CNT,                                                                "
	 * +
	 * "   NCELL_FREQ_CNT,                                                               "
	 * +
	 * "   SAME_FREQ_CNT,                                                                "
	 * +
	 * "   ADJ_FREQ_CNT,                                                                 "
	 * +
	 * "   CI_DIVIDER,                                                                   "
	 * +
	 * "   CA_DIVIDER,CELL_INDOOR,NCELL_INDOOR,CELL_TO_NCELL_DIR,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_BEARING,CELL_SITE,NCELL_SITE,NCELL_BEARING)                                                                   "
	 * + "  select      " +
	 * "         CELL,                                                                   "
	 * +
	 * "         CHGR,                                                                   "
	 * +
	 * "         BSIC,                                                                   "
	 * +
	 * "         ARFCN,                                                                  "
	 * +
	 * "         DEFINED_NEIGHBOUR,                                                      "
	 * +
	 * "         RECTIMEARFCN,                                                           "
	 * +
	 * "         REPARFCN,                                                               "
	 * +
	 * "         NCELL,                                                                  "
	 * +
	 * "         DISTANCE,                                                               "
	 * +
	 * "         INTERFER,                                                               "
	 * +
	 * "         CA_INTERFER,                                                            "
	 * +
	 * "         NCELLS,                                                                 "
	 * +
	 * "         CELL_FREQ_CNT,                                                          "
	 * +
	 * "         NCELL_FREQ_CNT,                                                         "
	 * +
	 * "         SAME_FREQ_CNT,                                                          "
	 * +
	 * "         ADJ_FREQ_CNT,                                                           "
	 * +
	 * "         CI_DIVIDER,                                                             "
	 * +
	 * "         CA_DIVIDER ,CELL_INDOOR,NCELL_INDOOR,CELL_TO_NCELL_DIR,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_BEARING,CELL_SITE,NCELL_SITE,NCELL_BEARING                                                          "
	 * + "    from " + sourceTab +
	 * " T2                                                                " +
	 * "   where  CELL<>NCELL AND REPARFCN>=1 and city_id=" + cityId +
	 * " and mea_time between to_date('" + startDate +
	 * "','yyyy-MM-dd HH24:mi:ss') " + " 											and to_date('" + endDate +
	 * "','yyyy-MM-dd HH24:mi:ss') ";
	 * log.debug("执行transfer2GEriNcsToTempTable前的sql:" + sql); int rows =
	 * stmt.executeUpdate(sql); result.setMsg("涉及爱立信ncs记录数量：" + rows + "条。");
	 * log.debug("执行transfer2GEriNcsToTempTable，转移数据：" + rows); } catch
	 * (Exception e) { e.printStackTrace(); log.error("转移2G爱立信NCS数据到临时表出错sql=" +
	 * sql); result.setFlag(false); } finally { }
	 * 
	 * // chktablecount("!!!!!!transfer2GEriNcsToTempTable刚执行转移后，在同个方法内",stmt);
	 * return result; }
	 */

	/**
	 * 
	 * @title 转移2G华为、爱立信ncs数据到统一分析表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:tmperincssourceTab,tmphwncssourceTab,ncsanatargetTab
	 *            val:from rno_ncs_mid(rno_2g_eri_ncs_t),rno_2g_hw_ncs_t to
	 *            RNO_NCS_ANA_T(RNO_2G_NCS_ANA_T)
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午5:23:00
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriNcsToUnifyAnaTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap,
			List<RnoThreshold> rnoThresholds, RnoStructAnaJobRec jobRec) {
		// from rno_ncs_mid,rno_2g_hw_ncs_t to RNO_NCS_ANA_T
		String eriNcsSourceTab = tableMap.get("erincssourceTab");
		String ncsAnaTargetTab = tableMap.get("ncsanatargetTab");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate = formatter.format(anaTaskInfo.getStartDate());
		String endDate = formatter.format(anaTaskInfo.getEndDate());
		long cityId = anaTaskInfo.getCityId();

		// chktablecount("!!!!!transfer2GNcsToUnifyAnaTable前",stmt);

		// 最远允许距离
		String farestDis = "8";// km

		// 距离大，ci也大。默认 距离大于3千米，非邻区同频干扰系数大于20%。
		String farDisWithLargeCI_dis = "3";// km
		String farDisWithLargeCi_ci = "0.2";

		// 样本小，ci大
		String littleSampleWithLargeCI_sample = "5000";
		String littleSampleWithLargeCI_ci = "0.5";

		// 样本大，ci小
		String hugeSampleWithTinyCi_sample = "10000";
		String hugeSampleWithTinyCi_ci = "0.00105";

		// 最小的有效样本数量
		String leastSampleCnt = "1000";

		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("INTERFACTORMOSTDISTANT".toUpperCase())) {
					farestDis = val;
				}
				if (code.equals("OVERSHOOTINGCOEFRFFERDISTANT".toUpperCase())) {
					farDisWithLargeCI_dis = val;
				}
				if (code.equals("NONNCELLSAMEFREQINTERCOEF".toUpperCase())) {
					farDisWithLargeCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTSMALL".toUpperCase())) {
					littleSampleWithLargeCI_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFBIG".toUpperCase())) {
					littleSampleWithLargeCI_ci = val;
				}

				if (code.equals("TOTALSAMPLECNTBIG".toUpperCase())) {
					hugeSampleWithTinyCi_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFSMALL".toUpperCase())) {
					hugeSampleWithTinyCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTTOOSMALL".toUpperCase())) {
					leastSampleCnt = val;
				}
			}
		}

		String sql = "";
		// 转移到临时表
		sql = "insert into  rno_2g_eri_ncs_T (  "
				+ "   CELL,                                                                         "
				+ "   CHGR,                                                                         "
				+ "   BSIC,                                                                         "
				+ "   ARFCN,                                                                        "
				+ "   DEFINED_NEIGHBOUR,                                                            "
				+ "   RECTIMEARFCN,                                                                 "
				+ "   REPARFCN,                                                                     "
				+ "   NCELL,                                                                        "
				+ "   DISTANCE,                                                                     "
				+ "   INTERFER,                                                                     "
				+ "   CA_INTERFER,                                                                  "
				+ "   NCELLS,                                                                       "
				+ "   CELL_FREQ_CNT,                                                                "
				+ "   NCELL_FREQ_CNT,                                                               "
				+ "   SAME_FREQ_CNT,                                                                "
				+ "   ADJ_FREQ_CNT,                                                                 "
				+ "   MEA_TIME,                                                                  "
				+ "   CI_DIVIDER, "
				+ "   CA_DIVIDER,CELL_INDOOR,NCELL_INDOOR,CELL_TO_NCELL_DIR,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_BEARING,CELL_SITE,NCELL_SITE,NCELL_BEARING)                                                                   "
				+ "  select      "
				+ "         CELL,                                                                   "
				+ "         CHGR,                                                                   "
				+ "         BSIC,                                                                   "
				+ "         ARFCN,                                                                  "
				+ "         DEFINED_NEIGHBOUR,                                                      "
				+ "         RECTIMEARFCN,                                                           "
				+ "         REPARFCN,                                                               "
				+ "         NCELL,                                                                  "
				+ "         DISTANCE,                                                               "
				+ "         INTERFER,                                                               "
				+ "         CA_INTERFER,                                                            "
				+ "         NCELLS,                                                                 "
				+ "         CELL_FREQ_CNT,                                                          "
				+ "         NCELL_FREQ_CNT,                                                         "
				+ "         SAME_FREQ_CNT,                                                          "
				+ "         ADJ_FREQ_CNT,                                                           "
				+ "   MEA_TIME,                                                                  "
				+ "         CI_DIVIDER,                                                             "
				+ "         CA_DIVIDER ,CELL_INDOOR,NCELL_INDOOR,CELL_TO_NCELL_DIR,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_BEARING,CELL_SITE,NCELL_SITE,NCELL_BEARING                                                          "
				+ "    from rno_2g_eri_ncs T2 " + "   where   distance<="
				+ farestDis + " and CELL<>NCELL AND REPARFCN>=1 and city_id="
				+ cityId + " and mea_time between to_date('" + startDate
				+ "','yyyy-MM-dd HH24:mi:ss') " + " 											and to_date('"
				+ endDate + "','yyyy-MM-dd HH24:mi:ss') ";

		int rows = 0;
		try {
			log.debug("执行transfer2GNcsToUnifyAnaTable，第一步，转移到临时表，sql:" + sql);
			rows = stmt.executeUpdate(sql);
			log.debug("transfer2GNcsToUnifyAnaTable，第一步，转移到临时表转移行数，" + rows);
		} catch (Exception e) {
			log.error("执行transfer2GNcsToUnifyAnaTable，第一步，转移到临时表出现错误，sql="
					+ sql);
			e.printStackTrace();
			return false;
		}

		// 如果需要输出重叠覆盖信息，则输出
		if (tableMap!=null && "true".equals(tableMap.get("outputOverLapDetail"))) {
			log.debug("需要输出爱立信重叠覆盖信息。。");
			ResultInfo ri = outputCellOverlapDetails(true, stmt, jobRec);
			log.debug("完成输出爱立信重叠覆盖信息。。结果，ri=" + ri.isFlag());
		}
		// 转移到统一表
		try {
			sql = "insert into "
					+ ncsAnaTargetTab
					+ "  (CELL,                                                                  "
					+ "   NCELL,                                                                 "
					+ "   DEFINED_NEIGHBOUR,                                                     "
					+ "   CELL_LON,                                                              "
					+ "   CELL_LAT,                                                              "
					+ "   NCELL_LON,                                                             "
					+ "   NCELL_LAT,                                                             "
					+ "   CELL_FREQ_CNT,                                                         "
					+ "   NCELL_FREQ_CNT,                                                        "
					+ "   SAME_FREQ_CNT,                                                         "
					+ "   ADJ_FREQ_CNT,                                                          "
					+ "   CI_DENOMINATOR,                                                        "
					+ "   CA_DENOMINATOR,                                                        "
					+ "   CI_DIVIDER,                                                            "
					+ "   CA_DIVIDER,                                                            "
					+ "   NCELLS,                                                                "
					+ "   CELL_BEARING,                                                          "
					+ "   CELL_INDOOR,                                                           "
					+ "   NCELL_INDOOR,                                                          "
					+ "   CELL_DOWNTILT,                                                         "
					+ "   CELL_SITE,                                                             "
					+ "   NCELL_SITE,                                                            "
					+ "   CELL_FREQ_SECTION,                                                     "
					+ "   NCELL_FREQ_SECTION,NCELL_BEARING,            "
					+ "   DISTANCE,CELL_TO_NCELL_DIR)     "
					+ " select  cell,  "
					+ "          NCELL,  "
					+ "        MIN(DEFINED_NEIGHBOUR) DEFINED_NEIGHBOUR,       "
					+ "         min(CELL_LON) CELL_LON,  "
					+ "         min(CELL_LAT) CELL_LAT,  "
					+ "         min(NCELL_LON) NCELL_LON, "
					+ "         min(NCELL_LAT) NCELL_LAT, "
					+ "         min(CELL_FREQ_CNT) CELL_FREQ_CNT,                                "
					+ "         min(NCELL_FREQ_CNT) NCELL_FREQ_CNT,                              "
					+ "         min(SAME_FREQ_CNT) SAME_FREQ_CNT,                                "
					+ "         min(ADJ_FREQ_CNT) ADJ_FREQ_CNT,                                  "
					+ "         sum(reparfcn) CI_DENOMINATOR,                                    "
					+ "         sum(reparfcn) CA_DENOMINATOR,                                    "
					+ "         sum(ci_divider) ci_divider,                                      "
					+ "         sum(ca_divider) ca_divider,                                      "
					+ "         min(NCELLS) NCELLS,                                              "
					+ "         min(CELL_BEARING) CELL_BEARING,                                  "
					+ "         min(CELL_INDOOR) CELL_INDOOR,                                    "
					+ "         min(NCELL_INDOOR) NCELL_INDOOR,                                  "
					+ "         min(CELL_DOWNTILT) CELL_DOWNTILT,                                "
					+ "         min(CELL_SITE),                                                  "
					+ "         min(NCELL_SITE) NCELL_SITE,                                      "
					+ "         min(CELL_FREQ_SECTION) CELL_FREQ_SECTION,                        "
					+ "         min(NCELL_FREQ_SECTION) NCELL_FREQ_SECTION,MIN(NCELL_BEARING) NCELL_BEARING,  "
					+ "         min(DISTANCE) DISTANCE,                                        "
					+ "          min(CELL_TO_NCELL_DIR) CELL_TO_NCELL_DIR"
					+ "    from rno_2g_eri_ncs_T   "
					// + " where   distance<="
					// + farestDis
					// + " and CELL<>NCELL AND REPARFCN>=1 and city_id="
					// + cityId
					// + " and mea_time between to_date('"
					// + startDate
					// + "','yyyy-MM-dd HH24:mi:ss') "
					// + " and to_date('"
					// + endDate
					// + "','yyyy-MM-dd HH24:mi:ss') "
					+ " group by (cell, ncell)  having "
					// 距离远，ci太大的不要
					+ "  not (min(distance)>="
					+ farDisWithLargeCI_dis
					+ " and sum(ci_divider)/sum(reparfcn)>="
					+ farDisWithLargeCi_ci
					+ " and min(DEFINED_NEIGHBOUR)=1) "
					// 采样少，ci太大的不要
					+ " and not (sum(reparfcn)<="
					+ littleSampleWithLargeCI_sample
					+ " and sum(ci_divider)/sum(reparfcn)>="
					+ littleSampleWithLargeCI_ci
					+ ") "
					// ci太小的不要
					+ " and not (sum(ci_divider)/sum(reparfcn)<="
					+ hugeSampleWithTinyCi_ci + ") "
					// 采样太少，或者距离太远的不要
					+ " and sum(reparfcn)>=" + leastSampleCnt;
			log.debug("执行transfer2GERINcsToUnifyAnaTable前的sql:" + sql);
			long t1=System.currentTimeMillis();
			rows = stmt.executeUpdate(sql);
			long t2=System.currentTimeMillis();
			log.debug("transfer2GERINcsToUnifyAnaTable转移行数：" + rows+"，耗时："+(t2-t1)+"ms");

		} catch (Exception e) {
			log.error("执行transfer2GERINcsToUnifyAnaTable的sql出现错误:" + sql);
			e.printStackTrace();
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			try {
				stmt.executeUpdate("truncate table rno_2g_eri_ncs_T");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 
	 * @title 转移2G华为NCS数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:sourceTab,targetTab,descTab val:from rno_2g_hw_ncs to
	 *            rno_2g_hw_ncs_t desc:rno_2g_hw_ncs_desc
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:32:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*
	 * @Deprecated public ResultInfo transfer2GHwNcsToTempTable(Statement stmt,
	 * StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) { // from
	 * rno_2g_hw_ncs to rno_2g_hw_ncs_t desc:rno_2g_hw_ncs_desc ResultInfo
	 * result = new ResultInfo(); result.setFlag(true); String sql = ""; String
	 * sourceTab = tableMap.get("sourceTab"), targetTab = tableMap
	 * .get("targetTab"), descTab = tableMap.get("descTab"); // SimpleDateFormat
	 * formatter = new SimpleDateFormat("yyyy-MM-dd "); String startDate =
	 * RnoHelper.format_yyyyMMddHHmmss(anaTaskInfo .getStartDate());//
	 * formatter.format(anaTaskInfo.getStartDate()); String endDate =
	 * RnoHelper.format_yyyyMMddHHmmss(anaTaskInfo .getEndDate()); long cityId =
	 * anaTaskInfo.getCityId();
	 * log.debug("进入transfer2GHwNcsToTempTable:sourceTab=" + sourceTab +
	 * ",startDate=" + startDate + ",endDate=" + endDate + ",cityId=" + cityId);
	 * try { sql = "insert into " + targetTab + "										         " +
	 * "  (CELL,                                                                   "
	 * +
	 * "   ARFCN,                                                                  "
	 * +
	 * "   BSIC,                                                                   "
	 * +
	 * "   AS360,                                                                  "
	 * +
	 * "   S389,                                                                   "
	 * +
	 * "   S368,                                                                   "
	 * +
	 * "   S386,                                                                   "
	 * +
	 * "   S363,                                                                   "
	 * +
	 * "   S371,                                                                   "
	 * +
	 * "   AS362,                                                                  "
	 * +
	 * "   S387,                                                                   "
	 * +
	 * "   S372,                                                                   "
	 * +
	 * "   S360,                                                                   "
	 * +
	 * "   S394,                                                                   "
	 * +
	 * "   S364,                                                                   "
	 * +
	 * "   S369,                                                                   "
	 * +
	 * "   S388,                                                                   "
	 * +
	 * "   S390,                                                                   "
	 * +
	 * "   S366,                                                                   "
	 * +
	 * "   S362,                                                                   "
	 * +
	 * "   S361,                                                                   "
	 * +
	 * "   S393,                                                                   "
	 * +
	 * "   S365,                                                                   "
	 * +
	 * "   S392,                                                                   "
	 * +
	 * "   S391,                                                                   "
	 * +
	 * "   S367,                                                                   "
	 * +
	 * "   S370,                                                                   "
	 * +
	 * "   NCELL,                                                                  "
	 * +
	 * "   NCELLS,                                                                 "
	 * +
	 * "   CELL_LON,                                                               "
	 * +
	 * "   CELL_LAT,                                                               "
	 * +
	 * "   NCELL_LON,                                                              "
	 * +
	 * "   NCELL_LAT,                                                              "
	 * +
	 * "   CELL_FREQ_CNT,                                                          "
	 * +
	 * "   NCELL_FREQ_CNT,                                                         "
	 * +
	 * "   SAME_FREQ_CNT,                                                          "
	 * +
	 * "   ADJ_FREQ_CNT,                                                           "
	 * +
	 * "   CELL_BEARING,                                                           "
	 * +
	 * "   CELL_INDOOR,                                                            "
	 * +
	 * "   NCELL_INDOOR,                                                           "
	 * +
	 * "   CELL_DOWNTILT,                                                          "
	 * +
	 * "   CELL_SITE,                                                              "
	 * +
	 * "   NCELL_SITE,                                                             "
	 * +
	 * "   CELL_FREQ_SECTION,                                                      "
	 * +
	 * "   NCELL_FREQ_SECTION,                                                     "
	 * +
	 * "   DISTANCE,CELL_TO_NCELL_DIR,S3013,NCELL_BEARING)                                                               "
	 * +
	 * "  select CELL,                                                             "
	 * +
	 * "         ARFCN,                                                            "
	 * +
	 * "         BSIC,                                                             "
	 * +
	 * "         AS360,                                                            "
	 * +
	 * "         S389,                                                             "
	 * +
	 * "         S368,                                                             "
	 * +
	 * "         S386,                                                             "
	 * +
	 * "         S363,                                                             "
	 * +
	 * "         S371,                                                             "
	 * +
	 * "         AS362,                                                            "
	 * +
	 * "         S387,                                                             "
	 * +
	 * "         S372,                                                             "
	 * +
	 * "         S360,                                                             "
	 * +
	 * "         S394,                                                             "
	 * +
	 * "         S364,                                                             "
	 * +
	 * "         S369,                                                             "
	 * +
	 * "         S388,                                                             "
	 * +
	 * "         S390,                                                             "
	 * +
	 * "         S366,                                                             "
	 * +
	 * "         S362,                                                             "
	 * +
	 * "         S361,                                                             "
	 * +
	 * "         S393,                                                             "
	 * +
	 * "         S365,                                                             "
	 * +
	 * "         S392,                                                             "
	 * +
	 * "         S391,                                                             "
	 * +
	 * "         S367,                                                             "
	 * +
	 * "         S370,                                                             "
	 * +
	 * "         NCELL,                                                            "
	 * +
	 * "         NCELLS,                                                           "
	 * +
	 * "         CELL_LON,                                                         "
	 * +
	 * "         CELL_LAT,                                                         "
	 * +
	 * "         NCELL_LON,                                                        "
	 * +
	 * "         NCELL_LAT,                                                        "
	 * +
	 * "         CELL_FREQ_CNT,                                                    "
	 * +
	 * "         NCELL_FREQ_CNT,                                                   "
	 * +
	 * "         SAME_FREQ_CNT,                                                    "
	 * +
	 * "         ADJ_FREQ_CNT,                                                     "
	 * +
	 * "         CELL_BEARING,                                                     "
	 * +
	 * "         CELL_INDOOR,                                                      "
	 * +
	 * "         NCELL_INDOOR,                                                     "
	 * +
	 * "         CELL_DOWNTILT ,                                                   "
	 * +
	 * "         CELL_SITE,                                                        "
	 * +
	 * "         NCELL_SITE,                                                       "
	 * +
	 * "         CELL_FREQ_SECTION,                                                "
	 * +
	 * "         NCELL_FREQ_SECTION,                                               "
	 * +
	 * "         DISTANCE,CELL_TO_NCELL_DIR,S3013,NCELL_BEARING                                                      "
	 * + "    from " + sourceTab +
	 * "  T2                                                   " +
	 * "   where CELL<>NCELL AND S3013>=1 and city_id=" + cityId +
	 * " and  mea_time between to_date('" + startDate +
	 * "','yyyy-MM-dd HH24:mi:ss') and to_date('" + endDate +
	 * "','yyyy-MM-dd HH24:mi:ss') ";
	 * log.debug("执行transfer2GHwNcsToTempTable前的sql:" + sql); int rows =
	 * stmt.executeUpdate(sql); result.setMsg("涉及华为ncs记录数量：" + rows + "条。");
	 * log.debug("执行transfer2GHwNcsToTempTable，转移数据：" + rows); } catch
	 * (Exception e) { // TODO: handle exception
	 * log.error("执行transfer2GHwNcsToTempTable出错！sql:" + sql);
	 * e.printStackTrace(); result.setFlag(false); } finally { // try { //
	 * stmt.close(); // } catch (SQLException e) { // e.printStackTrace(); // }
	 * }
	 * 
	 * return result; }
	 */

	public boolean transfer2GHwNcsToUnifyAnaTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap,
			List<RnoThreshold> rnoThresholds, RnoStructAnaJobRec jobRec) {
		// from rno_ncs_mid,rno_2g_hw_ncs_t to RNO_NCS_ANA_T
		String ncsAnaTargetTab = tableMap.get("ncsanatargetTab");

		DateUtil dateUtil = new DateUtil();
		String startDate = dateUtil.format_yyyyMMddHHmmss(anaTaskInfo
				.getStartDate());// formatter.format(anaTaskInfo.getStartDate());
		String endDate = dateUtil.format_yyyyMMddHHmmss(anaTaskInfo
				.getEndDate());
		long cityId = anaTaskInfo.getCityId();

		// chktablecount("!!!!!transfer2GNcsToUnifyAnaTable前",stmt);

		// 最远允许距离
		String farestDis = "8";// km

		// 距离大，ci也大。默认 距离大于3千米，非邻区同频干扰系数大于20%。
		String farDisWithLargeCI_dis = "3";// km
		String farDisWithLargeCi_ci = "0.2";

		// 样本小，ci大
		String littleSampleWithLargeCI_sample = "5000";
		String littleSampleWithLargeCI_ci = "0.5";

		// 样本大，ci小
		String hugeSampleWithTinyCi_sample = "10000";
		String hugeSampleWithTinyCi_ci = "0.00105";

		// 最小的有效样本数量
		String leastSampleCnt = "1000";

		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("INTERFACTORMOSTDISTANT".toUpperCase())) {
					farestDis = val;
				}
				if (code.equals("OVERSHOOTINGCOEFRFFERDISTANT".toUpperCase())) {
					farDisWithLargeCI_dis = val;
				}
				if (code.equals("NONNCELLSAMEFREQINTERCOEF".toUpperCase())) {
					farDisWithLargeCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTSMALL".toUpperCase())) {
					littleSampleWithLargeCI_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFBIG".toUpperCase())) {
					littleSampleWithLargeCI_ci = val;
				}

				if (code.equals("TOTALSAMPLECNTBIG".toUpperCase())) {
					hugeSampleWithTinyCi_sample = val;
				}
				if (code.equals("SAMEFREQINTERCOEFSMALL".toUpperCase())) {
					hugeSampleWithTinyCi_ci = val;
				}
				if (code.equals("TOTALSAMPLECNTTOOSMALL".toUpperCase())) {
					leastSampleCnt = val;
				}
			}
		}

		String sql = "";
		// 2014-9-30 为了输出重叠覆盖度，需要转移到临时表
		sql = "insert into rno_2g_hw_ncs_T "
				+ "  (CELL,                                                                   "
				+ "   ARFCN,                                                                  "
				+ "   BSIC,                                                                   "
				+ "   AS360,                                                                  "
				+ "   S389,                                                                   "
				+ "   S368,                                                                   "
				+ "   S386,                                                                   "
				+ "   S363,                                                                   "
				+ "   S371,                                                                   "
				+ "   AS362,                                                                  "
				+ "   S387,                                                                   "
				+ "   S372,                                                                   "
				+ "   S360,                                                                   "
				+ "   S394,                                                                   "
				+ "   S364,                                                                   "
				+ "   S369,                                                                   "
				+ "   S388,                                                                   "
				+ "   S390,                                                                   "
				+ "   S366,                                                                   "
				+ "   S362,                                                                   "
				+ "   S361,                                                                   "
				+ "   S393,                                                                   "
				+ "   S365,                                                                   "
				+ "   S392,                                                                   "
				+ "   S391,                                                                   "
				+ "   S367,                                                                   "
				+ "   S370,                                                                   "
				+ "   NCELL,                                                                  "
				+ "   NCELLS,                                                                 "
				+ "   CELL_LON,                                                               "
				+ "   CELL_LAT,                                                               "
				+ "   NCELL_LON,                                                              "
				+ "   NCELL_LAT,                                                              "
				+ "   CELL_FREQ_CNT,                                                          "
				+ "   NCELL_FREQ_CNT,                                                         "
				+ "   SAME_FREQ_CNT,                                                          "
				+ "   ADJ_FREQ_CNT,                                                           "
				+ "   CELL_BEARING,                                                           "
				+ "   CELL_INDOOR,                                                            "
				+ "   NCELL_INDOOR,                                                           "
				+ "   CELL_DOWNTILT,                                                          "
				+ "   CELL_SITE,                                                              "
				+ "   NCELL_SITE,                                                             "
				+ "   CELL_FREQ_SECTION,                                                      "
				+ "   NCELL_FREQ_SECTION,                                                     "
				+ "   MEA_TIME,                                                                  "
				+ "   DISTANCE,CELL_TO_NCELL_DIR,S3013,NCELL_BEARING)                                                               "
				+ "  select CELL,                                                             "
				+ "         ARFCN,                                                            "
				+ "         BSIC,                                                             "
				+ "         AS360,                                                            "
				+ "         S389,                                                             "
				+ "         S368,                                                             "
				+ "         S386,                                                             "
				+ "         S363,                                                             "
				+ "         S371,                                                             "
				+ "         AS362,                                                            "
				+ "         S387,                                                             "
				+ "         S372,                                                             "
				+ "         S360,                                                             "
				+ "         S394,                                                             "
				+ "         S364,                                                             "
				+ "         S369,                                                             "
				+ "         S388,                                                             "
				+ "         S390,                                                             "
				+ "         S366,                                                             "
				+ "         S362,                                                             "
				+ "         S361,                                                             "
				+ "         S393,                                                             "
				+ "         S365,                                                             "
				+ "         S392,                                                             "
				+ "         S391,                                                             "
				+ "         S367,                                                             "
				+ "         S370,                                                             "
				+ "         NCELL,                                                            "
				+ "         NCELLS,                                                           "
				+ "         CELL_LON,                                                         "
				+ "         CELL_LAT,                                                         "
				+ "         NCELL_LON,                                                        "
				+ "         NCELL_LAT,                                                        "
				+ "         CELL_FREQ_CNT,                                                    "
				+ "         NCELL_FREQ_CNT,                                                   "
				+ "         SAME_FREQ_CNT,                                                    "
				+ "         ADJ_FREQ_CNT,                                                     "
				+ "         CELL_BEARING,                                                     "
				+ "         CELL_INDOOR,                                                      "
				+ "         NCELL_INDOOR,                                                     "
				+ "         CELL_DOWNTILT ,                                                   "
				+ "         CELL_SITE,                                                        "
				+ "         NCELL_SITE,                                                       "
				+ "         CELL_FREQ_SECTION,                                                "
				+ "         NCELL_FREQ_SECTION,                                               "
				+ "   MEA_TIME,                                                                  "
				+ "         DISTANCE,CELL_TO_NCELL_DIR,S3013,NCELL_BEARING                                                      "
				+ "    from rno_2g_hw_ncs  T2 " + "   where distance<="
				+ farestDis + " and CELL<>NCELL AND S3013>=1 and city_id="
				+ cityId + " and  mea_time between to_date('" + startDate
				+ "','yyyy-MM-dd HH24:mi:ss') and to_date('" + endDate
				+ "','yyyy-MM-dd HH24:mi:ss') ";

		int rows = 0;
		try {
			log.debug("执行transfer2GHwNcsToUnifyAnaTable，第一步，转移到临时表，sql:" + sql);
			long t1=System.currentTimeMillis();
			rows = stmt.executeUpdate(sql);
			long t2=System.currentTimeMillis();
			log.debug("transfer2GHwNcsToUnifyAnaTable，第一步，转移到临时表转移行数，" + rows+"，耗时："+(t2-t1)+"ms");
		} catch (Exception e) {
			log.error("执行transfer2GHwNcsToUnifyAnaTable，第一步，转移到临时表出现错误，sql="
					+ sql);
			e.printStackTrace();
			return false;
		}

		// 如果需要输出重叠覆盖信息，则输出
		if ("true".equals(tableMap.get("outputOverLapDetail"))) {
			log.debug("需要输出华为重叠覆盖信息。。");
			ResultInfo ri = outputCellOverlapDetails(false, stmt, jobRec);
			log.debug("完成输出华为重叠覆盖信息。。结果，ri=" + ri.isFlag());
		}
		// 2014-9-24 gmh修改为直接从rno_2g_hw_ncs转移到统一分析表

		try {
			sql = "insert into "
					+ ncsAnaTargetTab
					+ "												"
					+ "  (CELL,                                                                  "
					+ "   NCELL,                                                                 "
					+ "   DEFINED_NEIGHBOUR,                                                     "
					+ "   CELL_LON,                                                              "
					+ "   CELL_LAT,                                                              "
					+ "   NCELL_LON,                                                             "
					+ "   NCELL_LAT,                                                             "
					+ "   CELL_FREQ_CNT,                                                         "
					+ "   NCELL_FREQ_CNT,                                                        "
					+ "   SAME_FREQ_CNT,                                                         "
					+ "   ADJ_FREQ_CNT,                                                          "
					+ "   CI_DENOMINATOR,                                                        "
					+ "   CA_DENOMINATOR,                                                        "
					+ "   CI_DIVIDER,                                                            "
					+ "   CA_DIVIDER,                                                            "
					+ "   NCELLS,                                                                "
					+ "   CELL_BEARING,                                                          "
					+ "   CELL_INDOOR,                                                           "
					+ "   NCELL_INDOOR,                                                          "
					+ "   CELL_DOWNTILT,                                                         "
					+ "   CELL_SITE,                                                             "
					+ "   NCELL_SITE,                                                            "
					+ "   CELL_FREQ_SECTION,                                                     "
					+ "   NCELL_FREQ_SECTION,NCELL_BEARING,                                                    "
					+ "   DISTANCE,CELL_TO_NCELL_DIR)                                                              "
					+ "  select  cell,                                                  "
					+ "          NCELL,                                                "
					+ "         0 DEFINED_NEIGHBOUR,        "
					+ "         min(CELL_LON) CELL_LON,                                          "
					+ "         min(CELL_LAT) CELL_LAT,                                          "
					+ "         min(NCELL_LON) NCELL_LON,                                        "
					+ "         min(NCELL_LAT) NCELL_LAT,                                        "
					+ "         min(CELL_FREQ_CNT) CELL_FREQ_CNT,                                "
					+ "         min(NCELL_FREQ_CNT) NCELL_FREQ_CNT,                              "
					+ "         min(SAME_FREQ_CNT) SAME_FREQ_CNT,                                "
					+ "         min(ADJ_FREQ_CNT) ADJ_FREQ_CNT,                                  "
					+ "         sum(S3013) CI_DENOMINATOR,                                        "
					+ "         sum(S3013) CA_DENOMINATOR,                                        "
					+ "         sum(s361-s369) ci_divider,                                            "
					+ "         sum(s361-s366) ca_divider,                              "
					+ "         min(NCELLS) NCELLS,                                              "
					+ "         min(CELL_BEARING) CELL_BEARING,                                  "
					+ "         min(CELL_INDOOR) CELL_INDOOR,                                    "
					+ "         min(NCELL_INDOOR) NCELL_INDOOR,                                  "
					+ "         min(CELL_DOWNTILT) CELL_DOWNTILT,                                "
					+ "         min(CELL_SITE),                                                  "
					+ "         min(NCELL_SITE) NCELL_SITE,                                      "
					+ "         min(CELL_FREQ_SECTION) CELL_FREQ_SECTION,                        "
					+ "         min(NCELL_FREQ_SECTION) NCELL_FREQ_SECTION, MIN(NCELL_BEARING) NCELL_BEARING,  "
					+ "         min(DISTANCE) DISTANCE,                                           "
					+ "          min(CELL_TO_NCELL_DIR) CELL_TO_NCELL_DIR"
					+ "    from RNO_2G_HW_NCS_T "// 2014-9-24
					// + " where distance<="
					// + farestDis
					// // 2014-9-24 条件增加
					// + "   and CELL<>NCELL AND S3013>=1 and city_id="
					// + cityId
					// + " and  mea_time between to_date('"
					// + startDate
					// + "','yyyy-MM-dd HH24:mi:ss') "
					// + " and to_date('"
					// + endDate
					// + "','yyyy-MM-dd HH24:mi:ss') "
					+ "   group by (cell, ncell)  having  "
					// 距离远，ci太大的不要
					+ "  not (min(distance)>="
					+ farDisWithLargeCI_dis
					+ " and SUM(s361-s369)/SUM(S3013)>="
					+ farDisWithLargeCi_ci
					+ ") "
					// 采样少，ci太大的不要
					+ " and not (SUM(S3013)<=" + littleSampleWithLargeCI_sample
					+ " and SUM(s361-s369)/SUM(S3013)>="
					+ littleSampleWithLargeCI_ci
					+ ") "
					// ci太小的不要
					+ " and not (SUM(s361-s369)/SUM(S3013)<="
					+ hugeSampleWithTinyCi_ci + ") "
					// 采样太少，或者距离太远的不要
					+ " and SUM(S3013)>=" + leastSampleCnt;
			log.debug("执行transfer2GHwNcsToUnifyAnaTable第2步前的sql:" + sql);
			rows = stmt.executeUpdate(sql);
			log.debug("transfer2GHwNcsToUnifyAnaTable第2步转移行数：" + rows);

		} catch (Exception e) {
			log.error("执行transfer2GHwNcsToUnifyAnaTable第2步的sql出现错误:" + sql);
			e.printStackTrace();
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
			try {
				stmt.executeUpdate("truncate table rno_2g_hw_ncs_T");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 
	 * @title 转移2G华为Mrr数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:hratesourceTab,hratetargetTab,fratesourceTab,
	 *            fratetargetTab,descTab val:from rno_2g_hw_mrr_hrate to
	 *            rno_2g_hw_mrr_hrate_t from rno_2g_hw_mrr_frate to
	 *            rno_2g_hw_mrr_frate_t desc rno_2g_hw_mrr_desc
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:37:43
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GHwMrrToTempTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) {
		// from rno_2g_hw_mrr_hrate to rno_2g_hw_mrr_hrate_t
		// from rno_2g_hw_mrr_frate to rno_2g_hw_mrr_frate_t
		// desc rno_2g_hw_mrr_desc
		String sql = "";
		String hratesourceTab = tableMap.get("hratesourceTab"), hratetargetTab = tableMap
				.get("hratetargetTab"), fratesourceTab = tableMap
				.get("fratesourceTab"), fratetargetTab = tableMap
				.get("fratetargetTab"), descTab = tableMap.get("descTab");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate = formatter.format(anaTaskInfo.getStartDate());
		String endDate = formatter.format(anaTaskInfo.getEndDate());
		long cityId = anaTaskInfo.getCityId();
		int hraterows = 0;
		int fraterows = 0;
		try {
			sql = "insert into "
					+ hratetargetTab
					+ "                                         "
					+ "  (MEA_DATE,                                                              "
					+ "   BSC,                                                                   "
					+ "   CELL,                                                                  "
					+ "   TRX_IDX,                                                               "
					+ "   S4100C,                                                                "
					+ "   S4101C,                                                                "
					+ "   S4102C,                                                                "
					+ "   S4103C,                                                                "
					+ "   S4104C,                                                                "
					+ "   S4105C,                                                                "
					+ "   S4106C,                                                                "
					+ "   S4107C,                                                                "
					+ "   S4110C,                                                                "
					+ "   S4111C,                                                                "
					+ "   S4112C,                                                                "
					+ "   S4113C,                                                                "
					+ "   S4114C,                                                                "
					+ "   S4115C,                                                                "
					+ "   S4116C,                                                                "
					+ "   S4117C,                                                                "
					+ "   S4120C,                                                                "
					+ "   S4121C,                                                                "
					+ "   S4122C,                                                                "
					+ "   S4123C,                                                                "
					+ "   S4124C,                                                                "
					+ "   S4125C,                                                                "
					+ "   S4126C,                                                                "
					+ "   S4127C,                                                                "
					+ "   S4130C,                                                                "
					+ "   S4131C,                                                                "
					+ "   S4132C,                                                                "
					+ "   S4133C,                                                                "
					+ "   S4134C,                                                                "
					+ "   S4135C,                                                                "
					+ "   S4136C,                                                                "
					+ "   S4137C,                                                                "
					+ "   S4140C,                                                                "
					+ "   S4141C,                                                                "
					+ "   S4142C,                                                                "
					+ "   S4143C,                                                                "
					+ "   S4144C,                                                                "
					+ "   S4145C,                                                                "
					+ "   S4146C,                                                                "
					+ "   S4147C,                                                                "
					+ "   S4150C,                                                                "
					+ "   S4151C,                                                                "
					+ "   S4152C,                                                                "
					+ "   S4153C,                                                                "
					+ "   S4154C,                                                                "
					+ "   S4155C,                                                                "
					+ "   S4156C,                                                                "
					+ "   S4157C,                                                                "
					+ "   S4160C,                                                                "
					+ "   S4161C,                                                                "
					+ "   S4162C,                                                                "
					+ "   S4163C,                                                                "
					+ "   S4164C,                                                                "
					+ "   S4165C,                                                                "
					+ "   S4166C,                                                                "
					+ "   S4167C,                                                                "
					+ "   S4170C,                                                                "
					+ "   S4171C,                                                                "
					+ "   S4172C,                                                                "
					+ "   S4173C,                                                                "
					+ "   S4174C,                                                                "
					+ "   S4175C,                                                                "
					+ "   S4176C,                                                                "
					+ "   S4177C,                                                                "
					+ "   S4100D,                                                                "
					+ "   S4101D,                                                                "
					+ "   S4102D,                                                                "
					+ "   S4103D,                                                                "
					+ "   S4104D,                                                                "
					+ "   S4105D,                                                                "
					+ "   S4106D,                                                                "
					+ "   S4107D,                                                                "
					+ "   S4110D,                                                                "
					+ "   S4111D,                                                                "
					+ "   S4112D,                                                                "
					+ "   S4113D,                                                                "
					+ "   S4114D,                                                                "
					+ "   S4115D,                                                                "
					+ "   S4116D,                                                                "
					+ "   S4117D,                                                                "
					+ "   S4120D,                                                                "
					+ "   S4121D,                                                                "
					+ "   S4122D,                                                                "
					+ "   S4123D,                                                                "
					+ "   S4124D,                                                                "
					+ "   S4125D,                                                                "
					+ "   S4126D,                                                                "
					+ "   S4127D,                                                                "
					+ "   S4130D,                                                                "
					+ "   S4131D,                                                                "
					+ "   S4132D,                                                                "
					+ "   S4133D,                                                                "
					+ "   S4134D,                                                                "
					+ "   S4135D,                                                                "
					+ "   S4136D,                                                                "
					+ "   S4137D,                                                                "
					+ "   S4140D,                                                                "
					+ "   S4141D,                                                                "
					+ "   S4142D,                                                                "
					+ "   S4143D,                                                                "
					+ "   S4144D,                                                                "
					+ "   S4145D,                                                                "
					+ "   S4146D,                                                                "
					+ "   S4147D,                                                                "
					+ "   S4150D,                                                                "
					+ "   S4151D,                                                                "
					+ "   S4152D,                                                                "
					+ "   S4153D,                                                                "
					+ "   S4154D,                                                                "
					+ "   S4155D,                                                                "
					+ "   S4156D,                                                                "
					+ "   S4157D,                                                                "
					+ "   S4160D,                                                                "
					+ "   S4161D,                                                                "
					+ "   S4162D,                                                                "
					+ "   S4163D,                                                                "
					+ "   S4164D,                                                                "
					+ "   S4165D,                                                                "
					+ "   S4166D,                                                                "
					+ "   S4167D,                                                                "
					+ "   S4170D,                                                                "
					+ "   S4171D,                                                                "
					+ "   S4172D,                                                                "
					+ "   S4173D,                                                                "
					+ "   S4174D,                                                                "
					+ "   S4175D,                                                                "
					+ "   S4176D,                                                                "
					+ "   S4177D)                                                                "
					+ "  select MEA_DATE,                                                        "
					+ "         BSC,                                                             "
					+ "         CELL,                                                            "
					+ "         TRX_IDX,                                                         "
					+ "         S4100C,                                                          "
					+ "         S4101C,                                                          "
					+ "         S4102C,                                                          "
					+ "         S4103C,                                                          "
					+ "         S4104C,                                                          "
					+ "         S4105C,                                                          "
					+ "         S4106C,                                                          "
					+ "         S4107C,                                                          "
					+ "         S4110C,                                                          "
					+ "         S4111C,                                                          "
					+ "         S4112C,                                                          "
					+ "         S4113C,                                                          "
					+ "         S4114C,                                                          "
					+ "         S4115C,                                                          "
					+ "         S4116C,                                                          "
					+ "         S4117C,                                                          "
					+ "         S4120C,                                                          "
					+ "         S4121C,                                                          "
					+ "         S4122C,                                                          "
					+ "         S4123C,                                                          "
					+ "         S4124C,                                                          "
					+ "         S4125C,                                                          "
					+ "         S4126C,                                                          "
					+ "         S4127C,                                                          "
					+ "         S4130C,                                                          "
					+ "         S4131C,                                                          "
					+ "         S4132C,                                                          "
					+ "         S4133C,                                                          "
					+ "         S4134C,                                                          "
					+ "         S4135C,                                                          "
					+ "         S4136C,                                                          "
					+ "         S4137C,                                                          "
					+ "         S4140C,                                                          "
					+ "         S4141C,                                                          "
					+ "         S4142C,                                                          "
					+ "         S4143C,                                                          "
					+ "         S4144C,                                                          "
					+ "         S4145C,                                                          "
					+ "         S4146C,                                                          "
					+ "         S4147C,                                                          "
					+ "         S4150C,                                                          "
					+ "         S4151C,                                                          "
					+ "         S4152C,                                                          "
					+ "         S4153C,                                                          "
					+ "         S4154C,                                                          "
					+ "         S4155C,                                                          "
					+ "         S4156C,                                                          "
					+ "         S4157C,                                                          "
					+ "         S4160C,                                                          "
					+ "         S4161C,                                                          "
					+ "         S4162C,                                                          "
					+ "         S4163C,                                                          "
					+ "         S4164C,                                                          "
					+ "         S4165C,                                                          "
					+ "         S4166C,                                                          "
					+ "         S4167C,                                                          "
					+ "         S4170C,                                                          "
					+ "         S4171C,                                                          "
					+ "         S4172C,                                                          "
					+ "         S4173C,                                                          "
					+ "         S4174C,                                                          "
					+ "         S4175C,                                                          "
					+ "         S4176C,                                                          "
					+ "         S4177C,                                                          "
					+ "         S4100D,                                                          "
					+ "         S4101D,                                                          "
					+ "         S4102D,                                                          "
					+ "         S4103D,                                                          "
					+ "         S4104D,                                                          "
					+ "         S4105D,                                                          "
					+ "         S4106D,                                                          "
					+ "         S4107D,                                                          "
					+ "         S4110D,                                                          "
					+ "         S4111D,                                                          "
					+ "         S4112D,                                                          "
					+ "         S4113D,                                                          "
					+ "         S4114D,                                                          "
					+ "         S4115D,                                                          "
					+ "         S4116D,                                                          "
					+ "         S4117D,                                                          "
					+ "         S4120D,                                                          "
					+ "         S4121D,                                                          "
					+ "         S4122D,                                                          "
					+ "         S4123D,                                                          "
					+ "         S4124D,                                                          "
					+ "         S4125D,                                                          "
					+ "         S4126D,                                                          "
					+ "         S4127D,                                                          "
					+ "         S4130D,                                                          "
					+ "         S4131D,                                                          "
					+ "         S4132D,                                                          "
					+ "         S4133D,                                                          "
					+ "         S4134D,                                                          "
					+ "         S4135D,                                                          "
					+ "         S4136D,                                                          "
					+ "         S4137D,                                                          "
					+ "         S4140D,                                                          "
					+ "         S4141D,                                                          "
					+ "         S4142D,                                                          "
					+ "         S4143D,                                                          "
					+ "         S4144D,                                                          "
					+ "         S4145D,                                                          "
					+ "         S4146D,                                                          "
					+ "         S4147D,                                                          "
					+ "         S4150D,                                                          "
					+ "         S4151D,                                                          "
					+ "         S4152D,                                                          "
					+ "         S4153D,                                                          "
					+ "         S4154D,                                                          "
					+ "         S4155D,                                                          "
					+ "         S4156D,                                                          "
					+ "         S4157D,                                                          "
					+ "         S4160D,                                                          "
					+ "         S4161D,                                                          "
					+ "         S4162D,                                                          "
					+ "         S4163D,                                                          "
					+ "         S4164D,                                                          "
					+ "         S4165D,                                                          "
					+ "         S4166D,                                                          "
					+ "         S4167D,                                                          "
					+ "         S4170D,                                                          "
					+ "         S4171D,                                                          "
					+ "         S4172D,                                                          "
					+ "         S4173D,                                                          "
					+ "         S4174D,                                                          "
					+ "         S4175D,                                                          "
					+ "         S4176D,                                                          "
					+ "         S4177D                                                           "
					+ "    from " + hratesourceTab
					+ "  T2                                            "
					+ "   where city_id = " + cityId
					+ " and mea_date between to_date('" + startDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ " 											and to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') " + "							";
			hraterows = stmt.executeUpdate(sql);
			log.debug("执行hraterows，转移半速率数据：" + hraterows);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("转移2G华为Mrr半速率数据到临时表出错sql=" + sql);
			return false;
		} finally {

		}
		try {
			sql = "insert into "
					+ fratetargetTab
					+ "                                        "
					+ "  (MEA_DATE,                                                             "
					+ "   BSC,                                                                  "
					+ "   CELL,                                                                 "
					+ "   TRX_IDX,                                                              "
					+ "   S4100A,                                                               "
					+ "   S4101A,                                                               "
					+ "   S4102A,                                                               "
					+ "   S4103A,                                                               "
					+ "   S4104A,                                                               "
					+ "   S4105A,                                                               "
					+ "   S4106A,                                                               "
					+ "   S4107A,                                                               "
					+ "   S4110A,                                                               "
					+ "   S4111A,                                                               "
					+ "   S4112A,                                                               "
					+ "   S4113A,                                                               "
					+ "   S4114A,                                                               "
					+ "   S4115A,                                                               "
					+ "   S4116A,                                                               "
					+ "   S4117A,                                                               "
					+ "   S4120A,                                                               "
					+ "   S4121A,                                                               "
					+ "   S4122A,                                                               "
					+ "   S4123A,                                                               "
					+ "   S4124A,                                                               "
					+ "   S4125A,                                                               "
					+ "   S4126A,                                                               "
					+ "   S4127A,                                                               "
					+ "   S4130A,                                                               "
					+ "   S4131A,                                                               "
					+ "   S4132A,                                                               "
					+ "   S4133A,                                                               "
					+ "   S4134A,                                                               "
					+ "   S4135A,                                                               "
					+ "   S4136A,                                                               "
					+ "   S4137A,                                                               "
					+ "   S4140A,                                                               "
					+ "   S4141A,                                                               "
					+ "   S4142A,                                                               "
					+ "   S4143A,                                                               "
					+ "   S4144A,                                                               "
					+ "   S4145A,                                                               "
					+ "   S4146A,                                                               "
					+ "   S4147A,                                                               "
					+ "   S4150A,                                                               "
					+ "   S4151A,                                                               "
					+ "   S4152A,                                                               "
					+ "   S4153A,                                                               "
					+ "   S4154A,                                                               "
					+ "   S4155A,                                                               "
					+ "   S4156A,                                                               "
					+ "   S4157A,                                                               "
					+ "   S4160A,                                                               "
					+ "   S4161A,                                                               "
					+ "   S4162A,                                                               "
					+ "   S4163A,                                                               "
					+ "   S4164A,                                                               "
					+ "   S4165A,                                                               "
					+ "   S4166A,                                                               "
					+ "   S4167A,                                                               "
					+ "   S4170A,                                                               "
					+ "   S4171A,                                                               "
					+ "   S4172A,                                                               "
					+ "   S4173A,                                                               "
					+ "   S4174A,                                                               "
					+ "   S4175A,                                                               "
					+ "   S4176A,                                                               "
					+ "   S4177A,                                                               "
					+ "   S4100B,                                                               "
					+ "   S4101B,                                                               "
					+ "   S4102B,                                                               "
					+ "   S4103B,                                                               "
					+ "   S4104B,                                                               "
					+ "   S4105B,                                                               "
					+ "   S4106B,                                                               "
					+ "   S4107B,                                                               "
					+ "   S4110B,                                                               "
					+ "   S4111B,                                                               "
					+ "   S4112B,                                                               "
					+ "   S4113B,                                                               "
					+ "   S4114B,                                                               "
					+ "   S4115B,                                                               "
					+ "   S4116B,                                                               "
					+ "   S4117B,                                                               "
					+ "   S4120B,                                                               "
					+ "   S4121B,                                                               "
					+ "   S4122B,                                                               "
					+ "   S4123B,                                                               "
					+ "   S4124B,                                                               "
					+ "   S4125B,                                                               "
					+ "   S4126B,                                                               "
					+ "   S4127B,                                                               "
					+ "   S4130B,                                                               "
					+ "   S4131B,                                                               "
					+ "   S4132B,                                                               "
					+ "   S4133B,                                                               "
					+ "   S4134B,                                                               "
					+ "   S4135B,                                                               "
					+ "   S4136B,                                                               "
					+ "   S4137B,                                                               "
					+ "   S4140B,                                                               "
					+ "   S4141B,                                                               "
					+ "   S4142B,                                                               "
					+ "   S4143B,                                                               "
					+ "   S4144B,                                                               "
					+ "   S4145B,                                                               "
					+ "   S4146B,                                                               "
					+ "   S4147B,                                                               "
					+ "   S4150B,                                                               "
					+ "   S4151B,                                                               "
					+ "   S4152B,                                                               "
					+ "   S4153B,                                                               "
					+ "   S4154B,                                                               "
					+ "   S4155B,                                                               "
					+ "   S4156B,                                                               "
					+ "   S4157B,                                                               "
					+ "   S4160B,                                                               "
					+ "   S4161B,                                                               "
					+ "   S4162B,                                                               "
					+ "   S4163B,                                                               "
					+ "   S4164B,                                                               "
					+ "   S4165B,                                                               "
					+ "   S4166B,                                                               "
					+ "   S4167B,                                                               "
					+ "   S4170B,                                                               "
					+ "   S4171B,                                                               "
					+ "   S4172B,                                                               "
					+ "   S4173B,                                                               "
					+ "   S4174B,                                                               "
					+ "   S4175B,                                                               "
					+ "   S4176B,                                                               "
					+ "   S4177B)                                                               "
					+ "  select MEA_DATE,                                                       "
					+ "         BSC,                                                            "
					+ "         CELL,                                                           "
					+ "         TRX_IDX,                                                        "
					+ "         S4100A,                                                         "
					+ "         S4101A,                                                         "
					+ "         S4102A,                                                         "
					+ "         S4103A,                                                         "
					+ "         S4104A,                                                         "
					+ "         S4105A,                                                         "
					+ "         S4106A,                                                         "
					+ "         S4107A,                                                         "
					+ "         S4110A,                                                         "
					+ "         S4111A,                                                         "
					+ "         S4112A,                                                         "
					+ "         S4113A,                                                         "
					+ "         S4114A,                                                         "
					+ "         S4115A,                                                         "
					+ "         S4116A,                                                         "
					+ "         S4117A,                                                         "
					+ "         S4120A,                                                         "
					+ "         S4121A,                                                         "
					+ "         S4122A,                                                         "
					+ "         S4123A,                                                         "
					+ "         S4124A,                                                         "
					+ "         S4125A,                                                         "
					+ "         S4126A,                                                         "
					+ "         S4127A,                                                         "
					+ "         S4130A,                                                         "
					+ "         S4131A,                                                         "
					+ "         S4132A,                                                         "
					+ "         S4133A,                                                         "
					+ "         S4134A,                                                         "
					+ "         S4135A,                                                         "
					+ "         S4136A,                                                         "
					+ "         S4137A,                                                         "
					+ "         S4140A,                                                         "
					+ "         S4141A,                                                         "
					+ "         S4142A,                                                         "
					+ "         S4143A,                                                         "
					+ "         S4144A,                                                         "
					+ "         S4145A,                                                         "
					+ "         S4146A,                                                         "
					+ "         S4147A,                                                         "
					+ "         S4150A,                                                         "
					+ "         S4151A,                                                         "
					+ "         S4152A,                                                         "
					+ "         S4153A,                                                         "
					+ "         S4154A,                                                         "
					+ "         S4155A,                                                         "
					+ "         S4156A,                                                         "
					+ "         S4157A,                                                         "
					+ "         S4160A,                                                         "
					+ "         S4161A,                                                         "
					+ "         S4162A,                                                         "
					+ "         S4163A,                                                         "
					+ "         S4164A,                                                         "
					+ "         S4165A,                                                         "
					+ "         S4166A,                                                         "
					+ "         S4167A,                                                         "
					+ "         S4170A,                                                         "
					+ "         S4171A,                                                         "
					+ "         S4172A,                                                         "
					+ "         S4173A,                                                         "
					+ "         S4174A,                                                         "
					+ "         S4175A,                                                         "
					+ "         S4176A,                                                         "
					+ "         S4177A,                                                         "
					+ "         S4100B,                                                         "
					+ "         S4101B,                                                         "
					+ "         S4102B,                                                         "
					+ "         S4103B,                                                         "
					+ "         S4104B,                                                         "
					+ "         S4105B,                                                         "
					+ "         S4106B,                                                         "
					+ "         S4107B,                                                         "
					+ "         S4110B,                                                         "
					+ "         S4111B,                                                         "
					+ "         S4112B,                                                         "
					+ "         S4113B,                                                         "
					+ "         S4114B,                                                         "
					+ "         S4115B,                                                         "
					+ "         S4116B,                                                         "
					+ "         S4117B,                                                         "
					+ "         S4120B,                                                         "
					+ "         S4121B,                                                         "
					+ "         S4122B,                                                         "
					+ "         S4123B,                                                         "
					+ "         S4124B,                                                         "
					+ "         S4125B,                                                         "
					+ "         S4126B,                                                         "
					+ "         S4127B,                                                         "
					+ "         S4130B,                                                         "
					+ "         S4131B,                                                         "
					+ "         S4132B,                                                         "
					+ "         S4133B,                                                         "
					+ "         S4134B,                                                         "
					+ "         S4135B,                                                         "
					+ "         S4136B,                                                         "
					+ "         S4137B,                                                         "
					+ "         S4140B,                                                         "
					+ "         S4141B,                                                         "
					+ "         S4142B,                                                         "
					+ "         S4143B,                                                         "
					+ "         S4144B,                                                         "
					+ "         S4145B,                                                         "
					+ "         S4146B,                                                         "
					+ "         S4147B,                                                         "
					+ "         S4150B,                                                         "
					+ "         S4151B,                                                         "
					+ "         S4152B,                                                         "
					+ "         S4153B,                                                         "
					+ "         S4154B,                                                         "
					+ "         S4155B,                                                         "
					+ "         S4156B,                                                         "
					+ "         S4157B,                                                         "
					+ "         S4160B,                                                         "
					+ "         S4161B,                                                         "
					+ "         S4162B,                                                         "
					+ "         S4163B,                                                         "
					+ "         S4164B,                                                         "
					+ "         S4165B,                                                         "
					+ "         S4166B,                                                         "
					+ "         S4167B,                                                         "
					+ "         S4170B,                                                         "
					+ "         S4171B,                                                         "
					+ "         S4172B,                                                         "
					+ "         S4173B,                                                         "
					+ "         S4174B,                                                         "
					+ "         S4175B,                                                         "
					+ "         S4176B,                                                         "
					+ "         S4177B                                                          "
					+ "    from " + fratesourceTab
					+ " T2                                            "
					+ "   where  city_id = " + cityId
					+ " and mea_date between to_date('" + startDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ " 											and to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ "                           ";
			fraterows = stmt.executeUpdate(sql);
			log.debug("执行fraterows，转移全速率数据：" + fraterows);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("转移2G华为Mrr全速率数据到临时表出错sql=" + sql);
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		return true;
	}

	public boolean transfer2GHwMrrToUnifyAnaTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) {
		/*
		 * from rno_2g_hw_mrr_frate_t rno_2g_hw_mrr_hrate_t
		 * rno_eri_mrr_quality_temp rno_eri_mrr_strength_temp
		 * rno_eri_mrr_ta_temp to rno_mrr_ana_t
		 */
		String tmpqualitysourceTab = tableMap.get("tmpqualitysourceTab"), tmpstrengthsourceTab = tableMap
				.get("tmpstrengthsourceTab"), tmptasourceTab = tableMap
				.get("tmptasourceTab"), tmphratesourceTab = tableMap
				.get("tmphratesourceTab"), tmpfratesourceTab = tableMap
				.get("tmpfratesourceTab"), mrranatargetTab = tableMap
				.get("mrranatargetTab");

		// 全速率上行
		String frateULQual0 = "SUM(S4100A+S4110A+S4120A+S4130A+S4140A+S4150A+S4160A+S4170A) as FR_UL_QUAL0";// 全速率上行质量为0的
		String frateULQual1 = "SUM(S4101A+S4111A+S4121A+S4131A+S4141A+S4151A+S4161A+S4171A) as FR_UL_QUAL1";// 全速率上行质量为1的
		String frateULQual2 = "SUM(S4102A+S4112A+S4122A+S4132A+S4142A+S4152A+S4162A+S4172A) as FR_UL_QUAL2";// 全速率上行质量为2的
		String frateULQual3 = "SUM(S4103A+S4113A+S4123A+S4133A+S4143A+S4153A+S4163A+S4173A) as FR_UL_QUAL3";// 全速率上行质量为3的
		String frateULQual4 = "SUM(S4104A+S4114A+S4124A+S4134A+S4144A+S4154A+S4164A+S4174A) as FR_UL_QUAL4";// 全速率上行质量为4的
		String frateULQual5 = "SUM(S4105A+S4115A+S4125A+S4135A+S4145A+S4155A+S4165A+S4175A) as FR_UL_QUAL5";// 全速率上行质量为5的
		String frateULQual6 = "SUM(S4106A+S4116A+S4126A+S4136A+S4146A+S4156A+S4166A+S4176A) as FR_UL_QUAL6";// 全速率上行质量为6的
		String frateULQual7 = "SUM(S4107A+S4117A+S4127A+S4137A+S4147A+S4157A+S4167A+S4177A) as FR_UL_QUAL7";// 全速率上行质量为7的

		// 全速率下行
		String frateDLQual0 = "SUM(S4100B+S4110B+S4120B+S4130B+S4140B+S4150B+S4160B+S4170B) as FR_DL_QUAL0";// 全速率下行质量为0的
		String frateDLQual1 = "SUM(S4101B+S4111B+S4121B+S4131B+S4141B+S4151B+S4161B+S4171B) as FR_DL_QUAL1";// 全速率下行质量为1的
		String frateDLQual2 = "SUM(S4102B+S4112B+S4122B+S4132B+S4142B+S4152B+S4162B+S4172B) as FR_DL_QUAL2";// 全速率下行质量为2的
		String frateDLQual3 = "SUM(S4103B+S4113B+S4123B+S4133B+S4143B+S4153B+S4163B+S4173B) as FR_DL_QUAL3";// 全速率下行质量为3的
		String frateDLQual4 = "SUM(S4104B+S4114B+S4124B+S4134B+S4144B+S4154B+S4164B+S4174B) as FR_DL_QUAL4";// 全速率下行质量为4的
		String frateDLQual5 = "SUM(S4105B+S4115B+S4125B+S4135B+S4145B+S4155B+S4165B+S4175B) as FR_DL_QUAL5";// 全速率下行质量为5的
		String frateDLQual6 = "SUM(S4106B+S4116B+S4126B+S4136B+S4146B+S4156B+S4166B+S4176B) as FR_DL_QUAL6";// 全速率下行质量为6的
		String frateDLQual7 = "SUM(S4107B+S4117B+S4127B+S4137B+S4147B+S4157B+S4167B+S4177B) as FR_DL_QUAL7";// 全速率下行质量为7的

		// 半速率上行
		String hrateULQual0 = "SUM(S4100C+S4110C+S4120C+S4130C+S4140C+S4150C+S4160C+S4170C) as HR_UL_QUAL0";// 半速率上行质量为0的
		String hrateULQual1 = "SUM(S4101C+S4111C+S4121C+S4131C+S4141C+S4151C+S4161C+S4171C) as HR_UL_QUAL1";// 半速率上行质量为1的
		String hrateULQual2 = "SUM(S4102C+S4112C+S4122C+S4132C+S4142C+S4152C+S4162C+S4172C) as HR_UL_QUAL2";// 半速率上行质量为2的
		String hrateULQual3 = "SUM(S4103C+S4113C+S4123C+S4133C+S4143C+S4153C+S4163C+S4173C) as HR_UL_QUAL3";// 半速率上行质量为3的
		String hrateULQual4 = "SUM(S4104C+S4114C+S4124C+S4134C+S4144C+S4154C+S4164C+S4174C) as HR_UL_QUAL4";// 半速率上行质量为4的
		String hrateULQual5 = "SUM(S4105C+S4115C+S4125C+S4135C+S4145C+S4155C+S4165C+S4175C) as HR_UL_QUAL5";// 半速率上行质量为5的
		String hrateULQual6 = "SUM(S4106C+S4116C+S4126C+S4136C+S4146C+S4156C+S4166C+S4176C) as HR_UL_QUAL6";// 半速率上行质量为6的
		String hrateULQual7 = "SUM(S4107C+S4117C+S4127C+S4137C+S4147C+S4157C+S4167C+S4177C) as HR_UL_QUAL7";// 半速率上行质量为7的

		// 半速率下行
		String hrateDLQual0 = "SUM(S4100D+S4110D+S4120D+S4130D+S4140D+S4150D+S4160D+S4170D) as HR_DL_QUAL0";// 半速率下行质量为0的
		String hrateDLQual1 = "SUM(S4101D+S4111D+S4121D+S4131D+S4141D+S4151D+S4161D+S4171D) as HR_DL_QUAL1";// 半速率下行质量为1的
		String hrateDLQual2 = "SUM(S4102D+S4112D+S4122D+S4132D+S4142D+S4152D+S4162D+S4172D) as HR_DL_QUAL2";// 半速率下行质量为2的
		String hrateDLQual3 = "SUM(S4103D+S4113D+S4123D+S4133D+S4143D+S4153D+S4163D+S4173D) as HR_DL_QUAL3";// 半速率下行质量为3的
		String hrateDLQual4 = "SUM(S4104D+S4114D+S4124D+S4134D+S4144D+S4154D+S4164D+S4174D) as HR_DL_QUAL4";// 半速率下行质量为4的
		String hrateDLQual5 = "SUM(S4105D+S4115D+S4125D+S4135D+S4145D+S4155D+S4165D+S4175D) as HR_DL_QUAL5";// 半速率下行质量为5的
		String hrateDLQual6 = "SUM(S4106D+S4116D+S4126D+S4136D+S4146D+S4156D+S4166D+S4176D) as HR_DL_QUAL6";// 半速率下行质量为6的
		String hrateDLQual7 = "SUM(S4107D+S4117D+S4127D+S4137D+S4147D+S4157D+S4167D+S4177D) as HR_DL_QUAL7";// 半速率下行质量为7的

		// 组合
		String ulQualStr = "'0='||(case WHEN FR_UL_QUAL0 is not NULL and HR_UL_QUAL0 is not NULL THEN FR_UL_QUAL0+HR_UL_QUAL0  WHEN FR_UL_QUAL0 IS NOT NULL THEN FR_UL_QUAL0  WHEN HR_UL_QUAL0 IS NOT NULL THEN HR_UL_QUAL0 ELSE 0 END)";
		ulQualStr += "||',1='||(case WHEN FR_UL_QUAL1 is not NULL and HR_UL_QUAL1 is not NULL THEN FR_UL_QUAL1+HR_UL_QUAL1 WHEN FR_UL_QUAL1 IS NOT NULL THEN FR_UL_QUAL1 WHEN HR_UL_QUAL1 IS NOT NULL THEN HR_UL_QUAL1 ELSE 0 END)";
		ulQualStr += "||',2='||(case WHEN FR_UL_QUAL2 is not NULL and HR_UL_QUAL2 is not NULL THEN FR_UL_QUAL2+HR_UL_QUAL2 WHEN FR_UL_QUAL2 IS NOT NULL THEN FR_UL_QUAL2 WHEN HR_UL_QUAL2 IS NOT NULL THEN HR_UL_QUAL2 ELSE 0 END)";
		ulQualStr += "||',3='||(case WHEN FR_UL_QUAL3 is not NULL and HR_UL_QUAL3 is not NULL THEN FR_UL_QUAL3+HR_UL_QUAL3 WHEN FR_UL_QUAL3 IS NOT NULL THEN FR_UL_QUAL3 WHEN HR_UL_QUAL3 IS NOT NULL THEN HR_UL_QUAL3 ELSE 0 END)";
		ulQualStr += "||',4='||(case WHEN FR_UL_QUAL4 is not NULL and HR_UL_QUAL4 is not NULL THEN FR_UL_QUAL4+HR_UL_QUAL4 WHEN FR_UL_QUAL4 IS NOT NULL THEN FR_UL_QUAL4 WHEN HR_UL_QUAL4 IS NOT NULL THEN HR_UL_QUAL4 ELSE 0 END)";
		ulQualStr += "||',5='||(case WHEN FR_UL_QUAL5 is not NULL and HR_UL_QUAL5 is not NULL THEN FR_UL_QUAL5+HR_UL_QUAL5 WHEN FR_UL_QUAL5 IS NOT NULL THEN FR_UL_QUAL5 WHEN HR_UL_QUAL5 IS NOT NULL THEN HR_UL_QUAL5 ELSE 0 END)";
		ulQualStr += "||',6='||(case WHEN FR_UL_QUAL6 is not NULL and HR_UL_QUAL6 is not NULL THEN FR_UL_QUAL6+HR_UL_QUAL6 WHEN FR_UL_QUAL6 IS NOT NULL THEN FR_UL_QUAL6 WHEN HR_UL_QUAL6 IS NOT NULL THEN HR_UL_QUAL6 ELSE 0 END)";
		ulQualStr += "||',7='||(case WHEN FR_UL_QUAL7 is not NULL and HR_UL_QUAL7 is not NULL THEN FR_UL_QUAL7+HR_UL_QUAL7 WHEN FR_UL_QUAL7 IS NOT NULL THEN FR_UL_QUAL7 WHEN HR_UL_QUAL7 IS NOT NULL THEN HR_UL_QUAL7 ELSE 0 END)";

		String dlQualStr = "'0='||(case WHEN FR_DL_QUAL0 is not NULL and HR_DL_QUAL0 is not NULL THEN FR_DL_QUAL0+HR_DL_QUAL0 WHEN FR_DL_QUAL0 IS NOT NULL THEN FR_DL_QUAL0 WHEN HR_DL_QUAL0 IS NOT NULL THEN HR_DL_QUAL0 ELSE 0 END)";
		dlQualStr += "||',1='||(case WHEN FR_DL_QUAL1 is not NULL and HR_DL_QUAL1 is not NULL THEN FR_DL_QUAL1+HR_DL_QUAL1 WHEN FR_DL_QUAL1 IS NOT NULL THEN FR_DL_QUAL1 WHEN HR_DL_QUAL1 IS NOT NULL THEN HR_DL_QUAL1 ELSE 0 END)";
		dlQualStr += "||',2='||(case WHEN FR_DL_QUAL2 is not NULL and HR_DL_QUAL2 is not NULL THEN FR_DL_QUAL2+HR_DL_QUAL2 WHEN FR_DL_QUAL2 IS NOT NULL THEN FR_DL_QUAL2 WHEN HR_DL_QUAL2 IS NOT NULL THEN HR_DL_QUAL2 ELSE 0 END)";
		dlQualStr += "||',3='||(case WHEN FR_DL_QUAL3 is not NULL and HR_DL_QUAL3 is not NULL THEN FR_DL_QUAL3+HR_DL_QUAL3 WHEN FR_DL_QUAL3 IS NOT NULL THEN FR_DL_QUAL3 WHEN HR_DL_QUAL3 IS NOT NULL THEN HR_DL_QUAL3 ELSE 0 END)";
		dlQualStr += "||',4='||(case WHEN FR_DL_QUAL4 is not NULL and HR_DL_QUAL4 is not NULL THEN FR_DL_QUAL4+HR_DL_QUAL4 WHEN FR_DL_QUAL4 IS NOT NULL THEN FR_DL_QUAL4 WHEN HR_DL_QUAL4 IS NOT NULL THEN HR_DL_QUAL4 ELSE 0 END)";
		dlQualStr += "||',5='||(case WHEN FR_DL_QUAL5 is not NULL and HR_DL_QUAL5 is not NULL THEN FR_DL_QUAL5+HR_DL_QUAL5 WHEN FR_DL_QUAL5 IS NOT NULL THEN FR_DL_QUAL5 WHEN HR_DL_QUAL5 IS NOT NULL THEN HR_DL_QUAL5 ELSE 0 END)";
		dlQualStr += "||',6='||(case WHEN FR_DL_QUAL6 is not NULL and HR_DL_QUAL6 is not NULL THEN FR_DL_QUAL6+HR_DL_QUAL6 WHEN FR_DL_QUAL6 IS NOT NULL THEN FR_DL_QUAL6 WHEN HR_DL_QUAL6 IS NOT NULL THEN HR_DL_QUAL6 ELSE 0 END)";
		dlQualStr += "||',7='||(case WHEN FR_DL_QUAL7 is not NULL and HR_DL_QUAL7 is not NULL THEN FR_DL_QUAL7+HR_DL_QUAL7 WHEN FR_DL_QUAL7 IS NOT NULL THEN FR_DL_QUAL7 WHEN HR_DL_QUAL7 IS NOT NULL THEN HR_DL_QUAL7 ELSE 0 END)";

		String sql = "";
		try {
			sql = "insert into "
					+ mrranatargetTab
					+ " (CELL,UL_QUALITY,DL_QUALITY,UL_POOR_QUALITY,DL_POOR_QUALITY,UL_COVERAGE,DL_COVERAGE,UL_POOR_COVERAGE,DL_POOR_COVERAGE,TA_AVERAGE,UL_QUAL_DETAIL,DL_QUAL_DETAIL) "
					+ " (select t1.cell cell,												        "
					+ "       ((t1.sum00ato72a + t2.sum00cto72c) +                                                       "
					+ "       (t1.sum03ato75a + t2.sum03cto75c) * 0.7) /                                                 "
					+ "       decode(t1.sum00ato77a + t2.sum00cto77c,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00ato77a + t2.sum00cto77c) ul_quality,                                         "
					+ "       ((t1.sum00bto72b + t2.sum00dto72d) +                                                       "
					+ "       (t1.sum03bto75b + t2.sum03dto75d) * 0.7) /                                                 "
					+ "       decode(t1.sum00bto77b + t2.sum00dto77d,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00bto77b + t2.sum00dto77d) dl_quality,                                         "
					+ "       (t1.sum05ato77a + t2.sum05cto77c) /                                                        "
					+ "       decode(t1.sum00ato77a + t2.sum00cto77c,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00ato77a + t2.sum00cto77c) ul_poor_quality,                                     "
					+ "       (t1.sum05bto77b + t2.sum05dto77d) /                                                        "
					+ "       decode(t1.sum00bto77b + t2.sum00dto77d,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00bto77b + t2.sum00dto77d) dl_poor_quality,                                     "
					+ "       (t1.sum20ato77a + t2.sum20cto77c) /                                                        "
					+ "       decode(t1.sum00ato77a + t2.sum00cto77c,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00ato77a + t2.sum00cto77c) ul_coverage,                                        "
					+ "       (t1.sum30bto77b + t2.sum30dto77d) /                                                        "
					+ "       decode(t1.sum00bto77b + t2.sum00dto77d,                                                    "
					+ "              0,                                                                                  "
					+ "              1,                                                                                  "
					+ "              t1.sum00bto77b + t2.sum00dto77d) dl_coverage,                                        "
					+ "       1 - (t1.sum20ato77a + t2.sum20cto77c) /                                                    "
					+ "       decode(t1.sum00ato77a + t2.sum00cto77c,                                                    "
					+ "                  0,                                                                              "
					+ "                  1,                                                                              "
					+ "                  t1.sum00ato77a + t2.sum00cto77c) ul_poor_coverage,                                "
					+ "       1 - (t1.sum30bto77b + t2.sum30dto77d) /                                                    "
					+ "       decode(t1.sum00bto77b + t2.sum00dto77d,                                                    "
					+ "                  0,                                                                              "
					+ "                  1,                                                                              "
					+ "                  t1.sum00bto77b + t2.sum00dto77d) dl_poor_coverage,                                "
					+ "       null ta_average "
					+ ","
					+ ulQualStr
					+ ","
					+ dlQualStr
					+ "                                                                           "
					+ "  from (select cell,                                                                              "
					+ "               sum(S4100A + S4110A + S4120A + S4130A + S4140A + S4150A +                          "
					+ "                   S4160A + S4170A + S4101A + S4111A + S4121A + S4131A +                          "
					+ "                   S4141A + S4151A + S4161A + S4171A + S4102A + S4112A +                          "
					+ "                   S4122A + S4132A + S4142A + S4152A + S4162A + S4172A +                          "
					+ "                   S4103A + S4113A + S4123A + S4133A + S4143A + S4153A +                          "
					+ "                   S4163A + S4173A + S4104A + S4114A + S4124A + S4134A +                          "
					+ "                   S4144A + S4154A + S4164A + S4174A + S4105A + S4115A +                          "
					+ "                   S4125A + S4135A + S4145A + S4155A + S4165A + S4175A +                          "
					+ "                   S4106A + S4116A + S4126A + S4136A + S4146A + S4156A +                          "
					+ "                   S4166A + S4176A + S4107A + S4117A + S4127A + S4137A +                          "
					+ "                   S4147A + S4157A + S4167A + S4177A) sum00ato77a,                                "
					+ "               sum(S4100B + S4110B + S4120B + S4130B + S4140B + S4150B +                          "
					+ "                   S4160B + S4170B + S4101B + S4111B + S4121B + S4131B +                          "
					+ "                   S4141B + S4151B + S4161B + S4171B + S4102B + S4112B +                          "
					+ "                   S4122B + S4132B + S4142B + S4152B + S4162B + S4172B +                          "
					+ "                   S4103B + S4113B + S4123B + S4133B + S4143B + S4153B +                          "
					+ "                   S4163B + S4173B + S4104B + S4114B + S4124B + S4134B +                          "
					+ "                   S4144B + S4154B + S4164B + S4174B + S4105B + S4115B +                          "
					+ "                   S4125B + S4135B + S4145B + S4155B + S4165B + S4175B +                          "
					+ "                   S4106B + S4116B + S4126B + S4136B + S4146B + S4156B +                          "
					+ "                   S4166B + S4176B + S4107B + S4117B + S4127B + S4137B +                          "
					+ "                   S4147B + S4157B + S4167B + S4177B) sum00bto77b,                                "
					+ "               sum(S4100A + S4110A + S4120A + S4130A + S4140A + S4150A +                          "
					+ "                   S4160A + S4170A + S4101A + S4111A + S4121A + S4131A +                          "
					+ "                   S4141A + S4151A + S4161A + S4171A + S4102A + S4112A +                          "
					+ "                   S4122A + S4132A + S4142A + S4152A + S4162A + S4172A) sum00ato72a,              "
					+ "               sum(S4103A + S4113A + S4123A + S4133A + S4143A + S4153A +                          "
					+ "                   S4163A + S4173A + S4104A + S4114A + S4124A + S4134A +                          "
					+ "                   S4144A + S4154A + S4164A + S4174A + S4105A + S4115A +                          "
					+ "                   S4125A + S4135A + S4145A + S4155A + S4165A + S4175A) sum03ato75a,              "
					+ "               sum(S4100B + S4110B + S4120B + S4130B + S4140B + S4150B +                          "
					+ "                   S4160B + S4170B + S4101B + S4111B + S4121B + S4131B +                          "
					+ "                   S4141B + S4151B + S4161B + S4171B + S4102B + S4112B +                          "
					+ "                   S4122B + S4132B + S4142B + S4152B + S4162B + S4172B) sum00bto72b,              "
					+ "               sum(S4103B + S4113B + S4123B + S4133B + S4143B + S4153B +                          "
					+ "                   S4163B + S4173B + S4104B + S4114B + S4124B + S4134B +                          "
					+ "                   S4144B + S4154B + S4164B + S4174B + S4105B + S4115B +                          "
					+ "                   S4125B + S4135B + S4145B + S4155B + S4165B + S4175B) sum03bto75b,              "
					+ "               sum(S4105A + S4115A + S4125A + S4135A + S4145A + S4155A +                          "
					+ "                   S4165A + S4175A + S4106A + S4116A + S4126A + S4136A +                          "
					+ "                   S4146A + S4156A + S4166A + S4176A + S4107A + S4117A +                          "
					+ "                   S4127A + S4137A + S4147A + S4157A + S4167A + S4177A) sum05ato77a,              "
					+ "               sum(S4105B + S4115B + S4125B + S4135B + S4145B + S4155B +                          "
					+ "                   S4165B + S4175B + S4106B + S4116B + S4126B + S4136B +                          "
					+ "                   S4146B + S4156B + S4166B + S4176B + S4107B + S4117B +                          "
					+ "                   S4127B + S4137B + S4147B + S4157B + S4167B + S4177B) sum05bto77b,              "
					+ "               sum(S4130B + S4131B + S4132B + S4133B + S4134B + S4135B +                          "
					+ "                   S4136B + S4137B + S4140B + S4141B + S4142B + S4143B +                          "
					+ "                   S4144B + S4145B + S4146B + S4147B + S4150B + S4151B +                          "
					+ "                   S4152B + S4153B + S4154B + S4155B + S4156B + S4157B +                          "
					+ "                   S4160B + S4161B + S4162B + S4163B + S4164B + S4165B +                          "
					+ "                   S4166B + S4167B + S4170B + S4171B + S4172B + S4173B +                          "
					+ "                   S4174B + S4175B + S4176B + S4177B) sum30bto77b,                                "
					+ "               sum(S4120A + S4121A + S4122A + S4123A + S4124A + S4125A +                          "
					+ "                   S4126A + S4127A + S4130A + S4131A + S4132A + S4133A +                          "
					+ "                   S4134A + S4135A + S4136A + S4137A + S4140A + S4141A +                          "
					+ "                   S4142A + S4143A + S4144A + S4145A + S4146A + S4147A +                          "
					+ "                   S4150A + S4151A + S4152A + S4153A + S4154A + S4155A +                          "
					+ "                   S4156A + S4157A + S4160A + S4161A + S4162A + S4163A +                          "
					+ "                   S4164A + S4165A + S4166A + S4167A + S4170A + S4171A +                          "
					+ "                   S4172A + S4173A + S4174A + S4175A + S4176A + S4177A) sum20ato77a               "
					+ ","
					+ frateULQual0
					+ ","
					+ frateULQual1
					+ ","
					+ frateULQual2
					+ ","
					+ frateULQual3
					+ ","
					+ frateULQual4
					+ ","
					+ frateULQual5
					+ ","
					+ frateULQual6
					+ ","
					+ frateULQual7
					+ ","
					+ frateDLQual0
					+ ","
					+ frateDLQual1
					+ ","
					+ frateDLQual2
					+ ","
					+ frateDLQual3
					+ ","
					+ frateDLQual4
					+ ","
					+ frateDLQual5
					+ ","
					+ frateDLQual6
					+ ","
					+ frateDLQual7
					+ "          from "
					+ tmpfratesourceTab
					+ "                                                                "
					+ "         group by (cell) having sum(S4100A + S4110A + S4120A + S4130A + S4140A + S4150A +                          "
					+ "                   S4160A + S4170A + S4101A + S4111A + S4121A + S4131A +                          "
					+ "                   S4141A + S4151A + S4161A + S4171A + S4102A + S4112A +                          "
					+ "                   S4122A + S4132A + S4142A + S4152A + S4162A + S4172A +                          "
					+ "                   S4103A + S4113A + S4123A + S4133A + S4143A + S4153A +                          "
					+ "                   S4163A + S4173A + S4104A + S4114A + S4124A + S4134A +                          "
					+ "                   S4144A + S4154A + S4164A + S4174A + S4105A + S4115A +                          "
					+ "                   S4125A + S4135A + S4145A + S4155A + S4165A + S4175A +                          "
					+ "                   S4106A + S4116A + S4126A + S4136A + S4146A + S4156A +                          "
					+ "                   S4166A + S4176A + S4107A + S4117A + S4127A + S4137A +                          "
					+ "                   S4147A + S4157A + S4167A + S4177A)>=1 and sum(S4100A + S4110A + S4120A + S4130A + S4140A + S4150A +                          "
					+ "                   S4160A + S4170A + S4101A + S4111A + S4121A + S4131A +                          "
					+ "                   S4141A + S4151A + S4161A + S4171A + S4102A + S4112A +                          "
					+ "                   S4122A + S4132A + S4142A + S4152A + S4162A + S4172A +                          "
					+ "                   S4103A + S4113A + S4123A + S4133A + S4143A + S4153A +                          "
					+ "                   S4163A + S4173A + S4104A + S4114A + S4124A + S4134A +                          "
					+ "                   S4144A + S4154A + S4164A + S4174A + S4105A + S4115A +                          "
					+ "                   S4125A + S4135A + S4145A + S4155A + S4165A + S4175A +                          "
					+ "                   S4106A + S4116A + S4126A + S4136A + S4146A + S4156A +                          "
					+ "                   S4166A + S4176A + S4107A + S4117A + S4127A + S4137A +                          "
					+ "                   S4147A + S4157A + S4167A + S4177A) is not null and sum(S4100B + S4110B + S4120B + S4130B + S4140B + S4150B +                          "
					+ "                   S4160B + S4170B + S4101B + S4111B + S4121B + S4131B +                          "
					+ "                   S4141B + S4151B + S4161B + S4171B + S4102B + S4112B +                          "
					+ "                   S4122B + S4132B + S4142B + S4152B + S4162B + S4172B +                          "
					+ "                   S4103B + S4113B + S4123B + S4133B + S4143B + S4153B +                          "
					+ "                   S4163B + S4173B + S4104B + S4114B + S4124B + S4134B +                          "
					+ "                   S4144B + S4154B + S4164B + S4174B + S4105B + S4115B +                          "
					+ "                   S4125B + S4135B + S4145B + S4155B + S4165B + S4175B +                          "
					+ "                   S4106B + S4116B + S4126B + S4136B + S4146B + S4156B +                          "
					+ "                   S4166B + S4176B + S4107B + S4117B + S4127B + S4137B +                          "
					+ "                   S4147B + S4157B + S4167B + S4177B)>=1 and sum(S4100B + S4110B + S4120B + S4130B + S4140B + S4150B +                          "
					+ "                   S4160B + S4170B + S4101B + S4111B + S4121B + S4131B +                          "
					+ "                   S4141B + S4151B + S4161B + S4171B + S4102B + S4112B +                          "
					+ "                   S4122B + S4132B + S4142B + S4152B + S4162B + S4172B +                          "
					+ "                   S4103B + S4113B + S4123B + S4133B + S4143B + S4153B +                          "
					+ "                   S4163B + S4173B + S4104B + S4114B + S4124B + S4134B +                          "
					+ "                   S4144B + S4154B + S4164B + S4174B + S4105B + S4115B +                          "
					+ "                   S4125B + S4135B + S4145B + S4155B + S4165B + S4175B +                          "
					+ "                   S4106B + S4116B + S4126B + S4136B + S4146B + S4156B +                          "
					+ "                   S4166B + S4176B + S4107B + S4117B + S4127B + S4137B +                          "
					+ "                   S4147B + S4157B + S4167B + S4177B) is not null) t1,                                                                     "
					+ "       (select cell,                                                                              "
					+ "               sum(S4100C + S4101C + S4102C + S4103C + S4104C + S4105C +                          "
					+ "                   S4106C + S4107C + S4110C + S4111C + S4112C + S4113C +                          "
					+ "                   S4114C + S4115C + S4116C + S4117C + S4120C + S4121C +                          "
					+ "                   S4122C + S4123C + S4124C + S4125C + S4126C + S4127C +                          "
					+ "                   S4130C + S4131C + S4132C + S4133C + S4134C + S4135C +                          "
					+ "                   S4136C + S4137C + S4140C + S4141C + S4142C + S4143C +                          "
					+ "                   S4144C + S4145C + S4146C + S4147C + S4150C + S4151C +                          "
					+ "                   S4152C + S4153C + S4154C + S4155C + S4156C + S4157C +                          "
					+ "                   S4160C + S4161C + S4162C + S4163C + S4164C + S4165C +                          "
					+ "                   S4166C + S4167C + S4170C + S4171C + S4172C + S4173C +                          "
					+ "                   S4174C + S4175C + S4176C + S4177C) sum00cto77c,                                "
					+ "               sum(S4100D + S4101D + S4102D + S4103D + S4104D + S4105D +                          "
					+ "                   S4106D + S4107D + S4110D + S4111D + S4112D + S4113D +                          "
					+ "                   S4114D + S4115D + S4116D + S4117D + S4120D + S4121D +                          "
					+ "                   S4122D + S4123D + S4124D + S4125D + S4126D + S4127D +                          "
					+ "                   S4130D + S4131D + S4132D + S4133D + S4134D + S4135D +                          "
					+ "                   S4136D + S4137D + S4140D + S4141D + S4142D + S4143D +                          "
					+ "                   S4144D + S4145D + S4146D + S4147D + S4150D + S4151D +                          "
					+ "                   S4152D + S4153D + S4154D + S4155D + S4156D + S4157D +                          "
					+ "                   S4160D + S4161D + S4162D + S4163D + S4164D + S4165D +                          "
					+ "                   S4166D + S4167D + S4170D + S4171D + S4172D + S4173D +                          "
					+ "                   S4174D + S4175D + S4176D + S4177D) sum00dto77d,                                "
					+ "               sum(S4100C + S4110C + S4120C + S4130C + S4140C + S4150C +                          "
					+ "                   S4160C + S4170C + S4101C + S4111C + S4121C + S4131C +                          "
					+ "                   S4141C + S4151C + S4161C + S4171C + S4102C + S4112C +                          "
					+ "                   S4122C + S4132C + S4142C + S4152C + S4162C + S4172C) sum00cto72c,              "
					+ "               sum(S4103C + S4113C + S4123C + S4133C + S4143C + S4153C +                          "
					+ "                   S4163C + S4173C + S4104C + S4114C + S4124C + S4134C +                          "
					+ "                   S4144C + S4154C + S4164C + S4174C + S4105C + S4115C +                          "
					+ "                   S4125C + S4135C + S4145C + S4155C + S4165C + S4175C) sum03cto75c,              "
					+ "               sum(S4100D + S4110D + S4120D + S4130D + S4140D + S4150D +                          "
					+ "                   S4160D + S4170D + S4101D + S4111D + S4121D + S4131D +                          "
					+ "                   S4141D + S4151D + S4161D + S4171D + S4102D + S4112D +                          "
					+ "                   S4122D + S4132D + S4142D + S4152D + S4162D + S4172D) sum00dto72d,              "
					+ "               sum(S4103D + S4113D + S4123D + S4133D + S4143D + S4153D +                          "
					+ "                   S4163D + S4173D + S4104D + S4114D + S4124D + S4134D +                          "
					+ "                   S4144D + S4154D + S4164D + S4174D + S4105D + S4115D +                          "
					+ "                   S4125D + S4135D + S4145D + S4155D + S4165D + S4175D) sum03dto75d,              "
					+ "               sum(S4105C + S4115C + S4125C + S4135C + S4145C + S4155C +                          "
					+ "                   S4165C + S4175C + S4106C + S4116C + S4126C + S4136C +                          "
					+ "                   S4146C + S4156C + S4166C + S4176C + S4107C + S4117C +                          "
					+ "                   S4127C + S4137C + S4147C + S4157C + S4167C + S4177C) sum05cto77c,              "
					+ "               sum(S4105D + S4115D + S4125D + S4135D + S4145D + S4155D +                          "
					+ "                   S4165D + S4175D + S4106D + S4116D + S4126D + S4136D +                          "
					+ "                   S4146D + S4156D + S4166D + S4176D + S4107D + S4117D +                          "
					+ "                   S4127D + S4137D + S4147D + S4157D + S4167D + S4177D) sum05dto77d,              "
					+ "               sum(S4130D + S4131D + S4132D + S4133D + S4134D + S4135D +                          "
					+ "                   S4136D + S4137D + S4140D + S4141D + S4142D + S4143D +                          "
					+ "                   S4144D + S4145D + S4146D + S4147D + S4150D + S4151D +                          "
					+ "                   S4152D + S4153D + S4154D + S4155D + S4156D + S4157D +                          "
					+ "                   S4160D + S4161D + S4162D + S4163D + S4164D + S4165D +                          "
					+ "                   S4166D + S4167D + S4170D + S4171D + S4172D + S4173D +                          "
					+ "                   S4174D + S4175D + S4176D + S4177D) sum30dto77d,                                "
					+ "               sum(S4120C + S4121C + S4122C + S4123C + S4124C + S4125C +                          "
					+ "                   S4126C + S4127C + S4130C + S4131C + S4132C + S4133C +                          "
					+ "                   S4134C + S4135C + S4136C + S4137C + S4140C + S4141C +                          "
					+ "                   S4142C + S4143C + S4144C + S4145C + S4146C + S4147C +                          "
					+ "                   S4150C + S4151C + S4152C + S4153C + S4154C + S4155C +                          "
					+ "                   S4156C + S4157C + S4160C + S4161C + S4162C + S4163C +                          "
					+ "                   S4164C + S4165C + S4166C + S4167C + S4170C + S4171C +                          "
					+ "                   S4172C + S4173C + S4174C + S4175C + S4176C + S4177C) sum20cto77c               "
					+ ","
					+ hrateULQual0
					+ ","
					+ hrateULQual1
					+ ","
					+ hrateULQual2
					+ ","
					+ hrateULQual3
					+ ","
					+ hrateULQual4
					+ ","
					+ hrateULQual5
					+ ","
					+ hrateULQual6
					+ ","
					+ hrateULQual7
					+ ","
					+ hrateDLQual0
					+ ","
					+ hrateDLQual1
					+ ","
					+ hrateDLQual2
					+ ","
					+ hrateDLQual3
					+ ","
					+ hrateDLQual4
					+ ","
					+ hrateDLQual5
					+ ","
					+ hrateDLQual6
					+ ","
					+ hrateDLQual7
					+ "          from    "
					+ tmphratesourceTab
					+ "         group by (cell) having sum(S4100C + S4101C + S4102C + S4103C + S4104C + S4105C +                          "
					+ "                   S4106C + S4107C + S4110C + S4111C + S4112C + S4113C +                          "
					+ "                   S4114C + S4115C + S4116C + S4117C + S4120C + S4121C +                          "
					+ "                   S4122C + S4123C + S4124C + S4125C + S4126C + S4127C +                          "
					+ "                   S4130C + S4131C + S4132C + S4133C + S4134C + S4135C +                          "
					+ "                   S4136C + S4137C + S4140C + S4141C + S4142C + S4143C +                          "
					+ "                   S4144C + S4145C + S4146C + S4147C + S4150C + S4151C +                          "
					+ "                   S4152C + S4153C + S4154C + S4155C + S4156C + S4157C +                          "
					+ "                   S4160C + S4161C + S4162C + S4163C + S4164C + S4165C +                          "
					+ "                   S4166C + S4167C + S4170C + S4171C + S4172C + S4173C +                          "
					+ "                   S4174C + S4175C + S4176C + S4177C)>=1 and sum(S4100C + S4101C + S4102C + S4103C + S4104C + S4105C +                          "
					+ "                   S4106C + S4107C + S4110C + S4111C + S4112C + S4113C +                          "
					+ "                   S4114C + S4115C + S4116C + S4117C + S4120C + S4121C +                          "
					+ "                   S4122C + S4123C + S4124C + S4125C + S4126C + S4127C +                          "
					+ "                   S4130C + S4131C + S4132C + S4133C + S4134C + S4135C +                          "
					+ "                   S4136C + S4137C + S4140C + S4141C + S4142C + S4143C +                          "
					+ "                   S4144C + S4145C + S4146C + S4147C + S4150C + S4151C +                          "
					+ "                   S4152C + S4153C + S4154C + S4155C + S4156C + S4157C +                          "
					+ "                   S4160C + S4161C + S4162C + S4163C + S4164C + S4165C +                          "
					+ "                   S4166C + S4167C + S4170C + S4171C + S4172C + S4173C +                          "
					+ "                   S4174C + S4175C + S4176C + S4177C) is not null and sum(S4100D + S4101D + S4102D + S4103D + S4104D + S4105D +                          "
					+ "                   S4106D + S4107D + S4110D + S4111D + S4112D + S4113D +                          "
					+ "                   S4114D + S4115D + S4116D + S4117D + S4120D + S4121D +                          "
					+ "                   S4122D + S4123D + S4124D + S4125D + S4126D + S4127D +                          "
					+ "                   S4130D + S4131D + S4132D + S4133D + S4134D + S4135D +                          "
					+ "                   S4136D + S4137D + S4140D + S4141D + S4142D + S4143D +                          "
					+ "                   S4144D + S4145D + S4146D + S4147D + S4150D + S4151D +                          "
					+ "                   S4152D + S4153D + S4154D + S4155D + S4156D + S4157D +                          "
					+ "                   S4160D + S4161D + S4162D + S4163D + S4164D + S4165D +                          "
					+ "                   S4166D + S4167D + S4170D + S4171D + S4172D + S4173D +                          "
					+ "                   S4174D + S4175D + S4176D + S4177D)>=1 and sum(S4100D + S4101D + S4102D + S4103D + S4104D + S4105D +                          "
					+ "                   S4106D + S4107D + S4110D + S4111D + S4112D + S4113D +                          "
					+ "                   S4114D + S4115D + S4116D + S4117D + S4120D + S4121D +                          "
					+ "                   S4122D + S4123D + S4124D + S4125D + S4126D + S4127D +                          "
					+ "                   S4130D + S4131D + S4132D + S4133D + S4134D + S4135D +                          "
					+ "                   S4136D + S4137D + S4140D + S4141D + S4142D + S4143D +                          "
					+ "                   S4144D + S4145D + S4146D + S4147D + S4150D + S4151D +                          "
					+ "                   S4152D + S4153D + S4154D + S4155D + S4156D + S4157D +                          "
					+ "                   S4160D + S4161D + S4162D + S4163D + S4164D + S4165D +                          "
					+ "                   S4166D + S4167D + S4170D + S4171D + S4172D + S4173D +                          "
					+ "                   S4174D + S4175D + S4176D + S4177D) is not null) t2  where t1.cell=t2.cell)  ";
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			// printTmpTable(mrranatargetTab, stmt);
			// log.debug("transfer2GMrrToUnifyAnaTable  rows"+rows);
			log.debug("转移2G华为mrr数据到统一分析表  行数=" + rows + ",耗时：" + (t2 - t1)
					+ "ms.SQL=" + sql);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("转移2G华为mrr数据到统一分析表失败sql:" + sql);
			return false;
		} finally {
		}
		return true;
	}

	/**
	 * 
	 * @title 转移2G爱立信Mrr数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:qualitysourceTab,qualitytargetTab,strengthsourceTab,
	 *            strengthtargetTab,tasourceTab,tatargetTab,descTab from
	 *            RNO_ERI_MRR_QUALITY to RNO_ERI_MRR_QUALITY_TEMP from
	 *            RNO_ERI_MRR_STRENGTH to RNO_ERI_MRR_STRENGTH_TEMP from
	 *            RNO_ERI_MRR_TA to RNO_ERI_MRR_TA_TEMP
	 *            desc:rno_eri_mrr_descriptor
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:52:31
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriMrrToTempTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) {
		// from RNO_ERI_MRR_QUALITY to RNO_ERI_MRR_QUALITY_TEMP
		// from RNO_ERI_MRR_STRENGTH to RNO_ERI_MRR_STRENGTH_TEMP
		// from RNO_ERI_MRR_TA to RNO_ERI_MRR_TA_TEMP
		// desc:rno_eri_mrr_descriptor
		String sql = "";
		String qualitysourceTab = tableMap.get("qualitysourceTab"), qualitytargetTab = tableMap
				.get("qualitytargetTab"), strengthsourceTab = tableMap
				.get("strengthsourceTab"), strengthtargetTab = tableMap
				.get("strengthtargetTab"), tasourceTab = tableMap
				.get("tasourceTab"), tatargetTab = tableMap.get("tatargetTab"), descTab = tableMap
				.get("descTab");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate = formatter.format(anaTaskInfo.getStartDate());
		String endDate = formatter.format(anaTaskInfo.getEndDate());
		long cityId = anaTaskInfo.getCityId();
		int qualityrows = 0;
		int strengthrows = 0;
		int tarows = 0;
		try {
			// 质量
			sql = "insert into "
					+ qualitytargetTab
					+ "                                        "
					+ "  (CELL_NAME,                                                               "
					+ "   SUBCELL,                                                                 "
					+ "   CHANNEL_GROUP_NUM,                                                       "
					+ "   RXQUALUL0,                                                               "
					+ "   RXQUALUL1,                                                               "
					+ "   RXQUALUL2,                                                               "
					+ "   RXQUALUL3,                                                               "
					+ "   RXQUALUL4,                                                               "
					+ "   RXQUALUL5,                                                               "
					+ "   RXQUALUL6,                                                               "
					+ "   RXQUALUL7,                                                               "
					+ "   RXQUALDL0,                                                               "
					+ "   RXQUALDL1,                                                               "
					+ "   RXQUALDL2,                                                               "
					+ "   RXQUALDL3,                                                               "
					+ "   RXQUALDL4,                                                               "
					+ "   RXQUALDL5,                                                               "
					+ "   RXQUALDL6,                                                               "
					+ "   RXQUALDL7)                                                               "
					+ "  select CELL_NAME,                                                         "
					+ "         SUBCELL,                                                           "
					+ "         CHANNEL_GROUP_NUM,                                                 "
					+ "         RXQUALUL0,                                                         "
					+ "         RXQUALUL1,                                                         "
					+ "         RXQUALUL2,                                                         "
					+ "         RXQUALUL3,                                                         "
					+ "         RXQUALUL4,                                                         "
					+ "         RXQUALUL5,                                                         "
					+ "         RXQUALUL6,                                                         "
					+ "         RXQUALUL7,                                                         "
					+ "         RXQUALDL0,                                                         "
					+ "         RXQUALDL1,                                                         "
					+ "         RXQUALDL2,                                                         "
					+ "         RXQUALDL3,                                                         "
					+ "         RXQUALDL4,                                                         "
					+ "         RXQUALDL5,                                                         "
					+ "         RXQUALDL6,                                                         "
					+ "         RXQUALDL7                                                          "
					+ "    from " + qualitysourceTab
					+ " T2                                               "
					+ "   where city_id = " + cityId
					+ " and  mea_date between to_date('" + startDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ " 											and to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') " + "							  ";
			qualityrows = stmt.executeUpdate(sql);
			log.debug("qualityrows话务质量转移行数：" + qualityrows);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("转移2G爱立信Mrr话务质量数据到临时表出错：sql=" + sql);
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		try {
			// 强度
			sql = "insert into "
					+ strengthtargetTab
					+ "                                      "
					+ "  (CELL_NAME,                                                              "
					+ "   SUBCELL,                                                                "
					+ "   CHANNEL_GROUP_NUM,                                                      "
					+ "   RXLEVUL0,                                                               "
					+ "   RXLEVUL1,                                                               "
					+ "   RXLEVUL2,                                                               "
					+ "   RXLEVUL3,                                                               "
					+ "   RXLEVUL4,                                                               "
					+ "   RXLEVUL5,                                                               "
					+ "   RXLEVUL6,                                                               "
					+ "   RXLEVUL7,                                                               "
					+ "   RXLEVUL8,                                                               "
					+ "   RXLEVUL9,                                                               "
					+ "   RXLEVUL10,                                                              "
					+ "   RXLEVUL11,                                                              "
					+ "   RXLEVUL12,                                                              "
					+ "   RXLEVUL13,                                                              "
					+ "   RXLEVUL14,                                                              "
					+ "   RXLEVUL15,                                                              "
					+ "   RXLEVUL16,                                                              "
					+ "   RXLEVUL17,                                                              "
					+ "   RXLEVUL18,                                                              "
					+ "   RXLEVUL19,                                                              "
					+ "   RXLEVUL20,                                                              "
					+ "   RXLEVUL21,                                                              "
					+ "   RXLEVUL22,                                                              "
					+ "   RXLEVUL23,                                                              "
					+ "   RXLEVUL24,                                                              "
					+ "   RXLEVUL25,                                                              "
					+ "   RXLEVUL26,                                                              "
					+ "   RXLEVUL27,                                                              "
					+ "   RXLEVUL28,                                                              "
					+ "   RXLEVUL29,                                                              "
					+ "   RXLEVUL30,                                                              "
					+ "   RXLEVUL31,                                                              "
					+ "   RXLEVUL32,                                                              "
					+ "   RXLEVUL33,                                                              "
					+ "   RXLEVUL34,                                                              "
					+ "   RXLEVUL35,                                                              "
					+ "   RXLEVUL36,                                                              "
					+ "   RXLEVUL37,                                                              "
					+ "   RXLEVUL38,                                                              "
					+ "   RXLEVUL39,                                                              "
					+ "   RXLEVUL40,                                                              "
					+ "   RXLEVUL41,                                                              "
					+ "   RXLEVUL42,                                                              "
					+ "   RXLEVUL43,                                                              "
					+ "   RXLEVUL44,                                                              "
					+ "   RXLEVUL45,                                                              "
					+ "   RXLEVUL46,                                                              "
					+ "   RXLEVUL47,                                                              "
					+ "   RXLEVUL48,                                                              "
					+ "   RXLEVUL49,                                                              "
					+ "   RXLEVUL50,                                                              "
					+ "   RXLEVUL51,                                                              "
					+ "   RXLEVUL52,                                                              "
					+ "   RXLEVUL53,                                                              "
					+ "   RXLEVUL54,                                                              "
					+ "   RXLEVUL55,                                                              "
					+ "   RXLEVUL56,                                                              "
					+ "   RXLEVUL57,                                                              "
					+ "   RXLEVUL58,                                                              "
					+ "   RXLEVUL59,                                                              "
					+ "   RXLEVUL60,                                                              "
					+ "   RXLEVUL61,                                                              "
					+ "   RXLEVUL62,                                                              "
					+ "   RXLEVUL63,                                                              "
					+ "   RXLEVDL0,                                                               "
					+ "   RXLEVDL1,                                                               "
					+ "   RXLEVDL2,                                                               "
					+ "   RXLEVDL3,                                                               "
					+ "   RXLEVDL4,                                                               "
					+ "   RXLEVDL5,                                                               "
					+ "   RXLEVDL6,                                                               "
					+ "   RXLEVDL7,                                                               "
					+ "   RXLEVDL8,                                                               "
					+ "   RXLEVDL9,                                                               "
					+ "   RXLEVDL10,                                                              "
					+ "   RXLEVDL11,                                                              "
					+ "   RXLEVDL12,                                                              "
					+ "   RXLEVDL13,                                                              "
					+ "   RXLEVDL14,                                                              "
					+ "   RXLEVDL15,                                                              "
					+ "   RXLEVDL16,                                                              "
					+ "   RXLEVDL17,                                                              "
					+ "   RXLEVDL18,                                                              "
					+ "   RXLEVDL19,                                                              "
					+ "   RXLEVDL20,                                                              "
					+ "   RXLEVDL21,                                                              "
					+ "   RXLEVDL22,                                                              "
					+ "   RXLEVDL23,                                                              "
					+ "   RXLEVDL24,                                                              "
					+ "   RXLEVDL25,                                                              "
					+ "   RXLEVDL26,                                                              "
					+ "   RXLEVDL27,                                                              "
					+ "   RXLEVDL28,                                                              "
					+ "   RXLEVDL29,                                                              "
					+ "   RXLEVDL30,                                                              "
					+ "   RXLEVDL31,                                                              "
					+ "   RXLEVDL32,                                                              "
					+ "   RXLEVDL33,                                                              "
					+ "   RXLEVDL34,                                                              "
					+ "   RXLEVDL35,                                                              "
					+ "   RXLEVDL36,                                                              "
					+ "   RXLEVDL37,                                                              "
					+ "   RXLEVDL38,                                                              "
					+ "   RXLEVDL39,                                                              "
					+ "   RXLEVDL40,                                                              "
					+ "   RXLEVDL41,                                                              "
					+ "   RXLEVDL42,                                                              "
					+ "   RXLEVDL43,                                                              "
					+ "   RXLEVDL44,                                                              "
					+ "   RXLEVDL45,                                                              "
					+ "   RXLEVDL46,                                                              "
					+ "   RXLEVDL47,                                                              "
					+ "   RXLEVDL48,                                                              "
					+ "   RXLEVDL49,                                                              "
					+ "   RXLEVDL50,                                                              "
					+ "   RXLEVDL51,                                                              "
					+ "   RXLEVDL52,                                                              "
					+ "   RXLEVDL53,                                                              "
					+ "   RXLEVDL54,                                                              "
					+ "   RXLEVDL55,                                                              "
					+ "   RXLEVDL56,                                                              "
					+ "   RXLEVDL57,                                                              "
					+ "   RXLEVDL58,                                                              "
					+ "   RXLEVDL59,                                                              "
					+ "   RXLEVDL60,                                                              "
					+ "   RXLEVDL61,                                                              "
					+ "   RXLEVDL62,                                                              "
					+ "   RXLEVDL63)                                                              "
					+ "  select CELL_NAME,                                                        "
					+ "         SUBCELL,                                                          "
					+ "         CHANNEL_GROUP_NUM,                                                "
					+ "         RXLEVUL0,                                                         "
					+ "         RXLEVUL1,                                                         "
					+ "         RXLEVUL2,                                                         "
					+ "         RXLEVUL3,                                                         "
					+ "         RXLEVUL4,                                                         "
					+ "         RXLEVUL5,                                                         "
					+ "         RXLEVUL6,                                                         "
					+ "         RXLEVUL7,                                                         "
					+ "         RXLEVUL8,                                                         "
					+ "         RXLEVUL9,                                                         "
					+ "         RXLEVUL10,                                                        "
					+ "         RXLEVUL11,                                                        "
					+ "         RXLEVUL12,                                                        "
					+ "         RXLEVUL13,                                                        "
					+ "         RXLEVUL14,                                                        "
					+ "         RXLEVUL15,                                                        "
					+ "         RXLEVUL16,                                                        "
					+ "         RXLEVUL17,                                                        "
					+ "         RXLEVUL18,                                                        "
					+ "         RXLEVUL19,                                                        "
					+ "         RXLEVUL20,                                                        "
					+ "         RXLEVUL21,                                                        "
					+ "         RXLEVUL22,                                                        "
					+ "         RXLEVUL23,                                                        "
					+ "         RXLEVUL24,                                                        "
					+ "         RXLEVUL25,                                                        "
					+ "         RXLEVUL26,                                                        "
					+ "         RXLEVUL27,                                                        "
					+ "         RXLEVUL28,                                                        "
					+ "         RXLEVUL29,                                                        "
					+ "         RXLEVUL30,                                                        "
					+ "         RXLEVUL31,                                                        "
					+ "         RXLEVUL32,                                                        "
					+ "         RXLEVUL33,                                                        "
					+ "         RXLEVUL34,                                                        "
					+ "         RXLEVUL35,                                                        "
					+ "         RXLEVUL36,                                                        "
					+ "         RXLEVUL37,                                                        "
					+ "         RXLEVUL38,                                                        "
					+ "         RXLEVUL39,                                                        "
					+ "         RXLEVUL40,                                                        "
					+ "         RXLEVUL41,                                                        "
					+ "         RXLEVUL42,                                                        "
					+ "         RXLEVUL43,                                                        "
					+ "         RXLEVUL44,                                                        "
					+ "         RXLEVUL45,                                                        "
					+ "         RXLEVUL46,                                                        "
					+ "         RXLEVUL47,                                                        "
					+ "         RXLEVUL48,                                                        "
					+ "         RXLEVUL49,                                                        "
					+ "         RXLEVUL50,                                                        "
					+ "         RXLEVUL51,                                                        "
					+ "         RXLEVUL52,                                                        "
					+ "         RXLEVUL53,                                                        "
					+ "         RXLEVUL54,                                                        "
					+ "         RXLEVUL55,                                                        "
					+ "         RXLEVUL56,                                                        "
					+ "         RXLEVUL57,                                                        "
					+ "         RXLEVUL58,                                                        "
					+ "         RXLEVUL59,                                                        "
					+ "         RXLEVUL60,                                                        "
					+ "         RXLEVUL61,                                                        "
					+ "         RXLEVUL62,                                                        "
					+ "         RXLEVUL63,                                                        "
					+ "         RXLEVDL0,                                                         "
					+ "         RXLEVDL1,                                                         "
					+ "         RXLEVDL2,                                                         "
					+ "         RXLEVDL3,                                                         "
					+ "         RXLEVDL4,                                                         "
					+ "         RXLEVDL5,                                                         "
					+ "         RXLEVDL6,                                                         "
					+ "         RXLEVDL7,                                                         "
					+ "         RXLEVDL8,                                                         "
					+ "         RXLEVDL9,                                                         "
					+ "         RXLEVDL10,                                                        "
					+ "         RXLEVDL11,                                                        "
					+ "         RXLEVDL12,                                                        "
					+ "         RXLEVDL13,                                                        "
					+ "         RXLEVDL14,                                                        "
					+ "         RXLEVDL15,                                                        "
					+ "         RXLEVDL16,                                                        "
					+ "         RXLEVDL17,                                                        "
					+ "         RXLEVDL18,                                                        "
					+ "         RXLEVDL19,                                                        "
					+ "         RXLEVDL20,                                                        "
					+ "         RXLEVDL21,                                                        "
					+ "         RXLEVDL22,                                                        "
					+ "         RXLEVDL23,                                                        "
					+ "         RXLEVDL24,                                                        "
					+ "         RXLEVDL25,                                                        "
					+ "         RXLEVDL26,                                                        "
					+ "         RXLEVDL27,                                                        "
					+ "         RXLEVDL28,                                                        "
					+ "         RXLEVDL29,                                                        "
					+ "         RXLEVDL30,                                                        "
					+ "         RXLEVDL31,                                                        "
					+ "         RXLEVDL32,                                                        "
					+ "         RXLEVDL33,                                                        "
					+ "         RXLEVDL34,                                                        "
					+ "         RXLEVDL35,                                                        "
					+ "         RXLEVDL36,                                                        "
					+ "         RXLEVDL37,                                                        "
					+ "         RXLEVDL38,                                                        "
					+ "         RXLEVDL39,                                                        "
					+ "         RXLEVDL40,                                                        "
					+ "         RXLEVDL41,                                                        "
					+ "         RXLEVDL42,                                                        "
					+ "         RXLEVDL43,                                                        "
					+ "         RXLEVDL44,                                                        "
					+ "         RXLEVDL45,                                                        "
					+ "         RXLEVDL46,                                                        "
					+ "         RXLEVDL47,                                                        "
					+ "         RXLEVDL48,                                                        "
					+ "         RXLEVDL49,                                                        "
					+ "         RXLEVDL50,                                                        "
					+ "         RXLEVDL51,                                                        "
					+ "         RXLEVDL52,                                                        "
					+ "         RXLEVDL53,                                                        "
					+ "         RXLEVDL54,                                                        "
					+ "         RXLEVDL55,                                                        "
					+ "         RXLEVDL56,                                                        "
					+ "         RXLEVDL57,                                                        "
					+ "         RXLEVDL58,                                                        "
					+ "         RXLEVDL59,                                                        "
					+ "         RXLEVDL60,                                                        "
					+ "         RXLEVDL61,                                                        "
					+ "         RXLEVDL62,                                                        "
					+ "         RXLEVDL63                                                         "
					+ "    from " + strengthsourceTab
					+ "  T2                                            "
					+ "   where city_id = " + cityId
					+ " and mea_date between to_date('" + startDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ " 											and to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') " + "							 ";
			strengthrows = stmt.executeUpdate(sql);
			log.debug("strengthrows信号强度转移行数：" + strengthrows);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("strengthrows信号强度转移数据出错sql:" + sql);
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		try {
			// TA
			sql = "insert into "
					+ tatargetTab
					+ "                                            "
					+ "  (CELL_NAME,                                                              "
					+ "   SUBCELL,                                                                "
					+ "   CHANNEL_GROUP_NUM,                                                      "
					+ "   TAVAL0,                                                                 "
					+ "   TAVAL1,                                                                 "
					+ "   TAVAL2,                                                                 "
					+ "   TAVAL3,                                                                 "
					+ "   TAVAL4,                                                                 "
					+ "   TAVAL5,                                                                 "
					+ "   TAVAL6,                                                                 "
					+ "   TAVAL7,                                                                 "
					+ "   TAVAL8,                                                                 "
					+ "   TAVAL9,                                                                 "
					+ "   TAVAL10,                                                                "
					+ "   TAVAL11,                                                                "
					+ "   TAVAL12,                                                                "
					+ "   TAVAL13,                                                                "
					+ "   TAVAL14,                                                                "
					+ "   TAVAL15,                                                                "
					+ "   TAVAL16,                                                                "
					+ "   TAVAL17,                                                                "
					+ "   TAVAL18,                                                                "
					+ "   TAVAL19,                                                                "
					+ "   TAVAL20,                                                                "
					+ "   TAVAL21,                                                                "
					+ "   TAVAL22,                                                                "
					+ "   TAVAL23,                                                                "
					+ "   TAVAL24,                                                                "
					+ "   TAVAL26,                                                                "
					+ "   TAVAL28,                                                                "
					+ "   TAVAL29,                                                                "
					+ "   TAVAL30,                                                                "
					+ "   TAVAL31,                                                                "
					+ "   TAVAL32,                                                                "
					+ "   TAVAL33,                                                                "
					+ "   TAVAL34,                                                                "
					+ "   TAVAL35,                                                                "
					+ "   TAVAL36,                                                                "
					+ "   TAVAL37,                                                                "
					+ "   TAVAL38,                                                                "
					+ "   TAVAL39,                                                                "
					+ "   TAVAL40,                                                                "
					+ "   TAVAL41,                                                                "
					+ "   TAVAL25,                                                                "
					+ "   TAVAL27,                                                                "
					+ "   TAVAL42,                                                                "
					+ "   TAVAL43,                                                                "
					+ "   TAVAL44,                                                                "
					+ "   TAVAL45,                                                                "
					+ "   TAVAL46,                                                                "
					+ "   TAVAL47,                                                                "
					+ "   TAVAL48,                                                                "
					+ "   TAVAL49,                                                                "
					+ "   TAVAL50,                                                                "
					+ "   TAVAL51,                                                                "
					+ "   TAVAL52,                                                                "
					+ "   TAVAL53,                                                                "
					+ "   TAVAL54,                                                                "
					+ "   TAVAL55,                                                                "
					+ "   TAVAL56,                                                                "
					+ "   TAVAL57,                                                                "
					+ "   TAVAL58,                                                                "
					+ "   TAVAL59,                                                                "
					+ "   TAVAL60,                                                                "
					+ "   TAVAL61,                                                                "
					+ "   TAVAL62,                                                                "
					+ "   TAVAL63,                                                                "
					+ "   TAVAL64,                                                                "
					+ "   TAVAL65,                                                                "
					+ "   TAVAL66,                                                                "
					+ "   TAVAL67,                                                                "
					+ "   TAVAL68,                                                                "
					+ "   TAVAL69,                                                                "
					+ "   TAVAL70,                                                                "
					+ "   TAVAL71,                                                                "
					+ "   TAVAL72,                                                                "
					+ "   TAVAL73,                                                                "
					+ "   TAVAL74,                                                                "
					+ "   TAVAL75)                                                                "
					+ "  select CELL_NAME,                                                        "
					+ "         SUBCELL,                                                          "
					+ "         CHANNEL_GROUP_NUM,                                                "
					+ "         TAVAL0,                                                           "
					+ "         TAVAL1,                                                           "
					+ "         TAVAL2,                                                           "
					+ "         TAVAL3,                                                           "
					+ "         TAVAL4,                                                           "
					+ "         TAVAL5,                                                           "
					+ "         TAVAL6,                                                           "
					+ "         TAVAL7,                                                           "
					+ "         TAVAL8,                                                           "
					+ "         TAVAL9,                                                           "
					+ "         TAVAL10,                                                          "
					+ "         TAVAL11,                                                          "
					+ "         TAVAL12,                                                          "
					+ "         TAVAL13,                                                          "
					+ "         TAVAL14,                                                          "
					+ "         TAVAL15,                                                          "
					+ "         TAVAL16,                                                          "
					+ "         TAVAL17,                                                          "
					+ "         TAVAL18,                                                          "
					+ "         TAVAL19,                                                          "
					+ "         TAVAL20,                                                          "
					+ "         TAVAL21,                                                          "
					+ "         TAVAL22,                                                          "
					+ "         TAVAL23,                                                          "
					+ "         TAVAL24,                                                          "
					+ "         TAVAL26,                                                          "
					+ "         TAVAL28,                                                          "
					+ "         TAVAL29,                                                          "
					+ "         TAVAL30,                                                          "
					+ "         TAVAL31,                                                          "
					+ "         TAVAL32,                                                          "
					+ "         TAVAL33,                                                          "
					+ "         TAVAL34,                                                          "
					+ "         TAVAL35,                                                          "
					+ "         TAVAL36,                                                          "
					+ "         TAVAL37,                                                          "
					+ "         TAVAL38,                                                          "
					+ "         TAVAL39,                                                          "
					+ "         TAVAL40,                                                          "
					+ "         TAVAL41,                                                          "
					+ "         TAVAL25,                                                          "
					+ "         TAVAL27,                                                          "
					+ "         TAVAL42,                                                          "
					+ "         TAVAL43,                                                          "
					+ "         TAVAL44,                                                          "
					+ "         TAVAL45,                                                          "
					+ "         TAVAL46,                                                          "
					+ "         TAVAL47,                                                          "
					+ "         TAVAL48,                                                          "
					+ "         TAVAL49,                                                          "
					+ "         TAVAL50,                                                          "
					+ "         TAVAL51,                                                          "
					+ "         TAVAL52,                                                          "
					+ "         TAVAL53,                                                          "
					+ "         TAVAL54,                                                          "
					+ "         TAVAL55,                                                          "
					+ "         TAVAL56,                                                          "
					+ "         TAVAL57,                                                          "
					+ "         TAVAL58,                                                          "
					+ "         TAVAL59,                                                          "
					+ "         TAVAL60,                                                          "
					+ "         TAVAL61,                                                          "
					+ "         TAVAL62,                                                          "
					+ "         TAVAL63,                                                          "
					+ "         TAVAL64,                                                          "
					+ "         TAVAL65,                                                          "
					+ "         TAVAL66,                                                          "
					+ "         TAVAL67,                                                          "
					+ "         TAVAL68,                                                          "
					+ "         TAVAL69,                                                          "
					+ "         TAVAL70,                                                          "
					+ "         TAVAL71,                                                          "
					+ "         TAVAL72,                                                          "
					+ "         TAVAL73,                                                          "
					+ "         TAVAL74,                                                          "
					+ "         TAVAL75                                                           "
					+ "    from " + tasourceTab
					+ "  T2                                                  "
					+ "   where city_id = " + cityId
					+ " and mea_date between to_date('" + startDate
					+ "','yyyy-MM-dd HH24:mi:ss') "
					+ " 											and to_date('" + endDate
					+ "','yyyy-MM-dd HH24:mi:ss') " + " 					         ";
			tarows = stmt.executeUpdate(sql);
			log.debug("tarows转移行数：" + tarows);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("tarows实时预报TA转移数据错误sql:" + sql);
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		return true;
	}

	/**
	 * 
	 * @title 转移2G华为、爱立信mrr数据到统一分析表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap
	 *            key:tmpqualitysourceTab,tmpstrengthsourceTab,tmptasourceTab,
	 *            tmphratesourceTab,tmpfratesourceTab,mrranatargetTab val:from
	 *            rno_2g_hw_mrr_frate_t rno_2g_hw_mrr_hrate_t
	 *            rno_eri_mrr_quality_temp rno_eri_mrr_strength_temp
	 *            rno_eri_mrr_ta_temp to rno_mrr_ana_t(RNO_2G_MRR_ANA_T)
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午4:55:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriMrrToUnifyAnaTable(Statement stmt,
			StructAnaTaskInfo anaTaskInfo, Map<String, String> tableMap) {
		/*
		 * from rno_2g_hw_mrr_frate_t rno_2g_hw_mrr_hrate_t
		 * rno_eri_mrr_quality_temp rno_eri_mrr_strength_temp
		 * rno_eri_mrr_ta_temp to rno_mrr_ana_t
		 */
		String tmpqualitysourceTab = tableMap.get("tmpqualitysourceTab"), tmpstrengthsourceTab = tableMap
				.get("tmpstrengthsourceTab"), tmptasourceTab = tableMap
				.get("tmptasourceTab"), tmphratesourceTab = tableMap
				.get("tmphratesourceTab"), tmpfratesourceTab = tableMap
				.get("tmpfratesourceTab"), mrranatargetTab = tableMap
				.get("mrranatargetTab");
		String sql = "";
		try {
			sql = "insert into "
					+ mrranatargetTab
					+ " (CELL,UL_QUALITY,DL_QUALITY,UL_POOR_QUALITY,DL_POOR_QUALITY,UL_COVERAGE,DL_COVERAGE,UL_POOR_COVERAGE,DL_POOR_COVERAGE,TA_AVERAGE,UL_QUAL_DETAIL,DL_QUAL_DETAIL) "
					+ "         select t1.cell_name cell,                                                                     "
					+ "       t1.sumul0t5 / decode(t1.sumul0t7, 0, 1, t1.sumul0t7) ul_quality,                            "
					+ "       t1.sumdl0t5 / decode(t1.sumdl0t7, 0, 1, t1.sumdl0t7) dl_quality,                            "
					+ "       t1.sumul6t7 / decode(t1.sumul0t7, 0, 1, t1.sumul0t7) ul_poor_quality,                        "
					+ "       t1.sumdl6t7 / decode(t1.sumdl0t7, 0, 1, t1.sumdl0t7) dl_poor_quality,                        "
					+ "       t2.sumullev10t63 / decode(t2.sumullev0t63, 0, 1, t2.sumullev0t63) ul_coverage,              "
					+ "       t2.sumdllev16t63 / decode(t2.sumdllev0t63, 0, 1, t2.sumdllev0t63) dl_coverage,              "
					+ "       1 -                                                                                        "
					+ "       t2.sumullev10t63 / decode(t2.sumullev0t63, 0, 1, t2.sumullev0t63) ul_poor_coverage,          "
					+ "       1 -                                                                                        "
					+ "       t2.sumdllev16t63 / decode(t2.sumdllev0t63, 0, 1, t2.sumdllev0t63) dl_poor_coverage,          "
					+ "       t3.ta_average,t1.ul_qual_detail,t1.dl_qual_detail                                                                              "
					+ "  from (select cell_name,                                                                         "
					+ "               sum(rxqualul0 + rxqualul1 + rxqualul2 + (rxqualul3 + rxqualul4 +                    "
					+ "                   rxqualul5)*0.7) sumul0t5,                                                           "
					+ "               sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +                    "
					+ "                   rxqualul5 + rxqualul6 + rxqualul7) sumul0t7,                                   "
					+ "               sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + (rxqualdl3 + rxqualdl4 +                    "
					+ "                   rxqualdl5)*0.7) sumdl0t5,                                                           "
					+ "               sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 +                    "
					+ "                   rxqualdl5 + rxqualdl6 + rxqualdl7) sumdl0t7,                                   "
					+ "               sum(rxqualul6 + rxqualul7) sumul6t7,                                               "
					+ "               sum(rxqualdl6 + rxqualdl7) sumdl6t7,"
					+ "  '0='||sum(rxqualul0)||',1='||sum(rxqualul1)||',2='||sum(rxqualul2)||',3='||sum(rxqualul3)||',4='||sum(rxqualul4)||',5='||sum(rxqualul5)||',6='||sum(rxqualul6)||',7='||sum(rxqualul7) as ul_qual_detail,"
					+ " '0='||sum(rxqualdl0)||',1='||sum(rxqualdl1)||',2='||sum(rxqualdl2)||',3='||sum(rxqualdl3)||',4='||sum(rxqualdl4)||',5='||sum(rxqualdl5)||',6='||sum(rxqualdl6)||',7='||sum(rxqualdl7) as dl_qual_detail "
					+ "          from          "
					+ tmpqualitysourceTab
					+ "                                                       "
					+ "         group by cell_name having sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +rxqualul5 + rxqualul6 + rxqualul7)>=1 and sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +rxqualul5 + rxqualul6 + rxqualul7) is not null"
					+ " and sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 + rxqualdl5 + rxqualdl6 + rxqualdl7)>=1 and sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 + rxqualdl5 + rxqualdl6 + rxqualdl7) is not null) t1, "
					+ "       (select cell_name,                                                                         "
					+ "               sum(RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                    "
					+ "                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                    "
					+ "                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                    "
					+ "                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                    "
					+ "                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                    "
					+ "                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                    "
					+ "                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                    "
					+ "                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                    "
					+ "                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                    "
					+ "                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                    "
					+ "                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) sumullev10t63,                  "
					+ "               sum(RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + RXLEVDL20 +                    "
					+ "                   RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + RXLEVDL25 +                    "
					+ "                   RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + RXLEVDL30 +                    "
					+ "                   RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + RXLEVDL35 +                    "
					+ "                   RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + RXLEVDL40 +                    "
					+ "                   RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + RXLEVDL45 +                    "
					+ "                   RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + RXLEVDL50 +                    "
					+ "                   RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + RXLEVDL55 +                    "
					+ "                   RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + RXLEVDL60 +                    "
					+ "                   RXLEVDL61 + RXLEVDL62 + RXLEVDL63) sumdllev16t63,                              "
					+ "               sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 +                         "
					+ "                   RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 +                         "
					+ "                   RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                    "
					+ "                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                    "
					+ "                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                    "
					+ "                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                    "
					+ "                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                    "
					+ "                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                    "
					+ "                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                    "
					+ "                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                    "
					+ "                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                    "
					+ "                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                    "
					+ "                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) sumullev0t63,                   "
					+ "               sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +                         "
					+ "                   RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +                         "
					+ "                   RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +                    "
					+ "                   RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +                    "
					+ "                   RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +                    "
					+ "                   RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +                    "
					+ "                   RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +                    "
					+ "                   RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +                    "
					+ "                   RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +                    "
					+ "                   RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +                    "
					+ "                   RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +                    "
					+ "                   RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +                    "
					+ "                   RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) sumdllev0t63                    "
					+ "          from       "
					+ tmpstrengthsourceTab
					+ "                                                        "
					+ "         group by cell_name having sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 +                         "
					+ "                   RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 +                         "
					+ "                   RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                    "
					+ "                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                    "
					+ "                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                    "
					+ "                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                    "
					+ "                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                    "
					+ "                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                    "
					+ "                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                    "
					+ "                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                    "
					+ "                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                    "
					+ "                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                    "
					+ "                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63)>=1 and sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 +                         "
					+ "                   RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 +                         "
					+ "                   RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                    "
					+ "                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                    "
					+ "                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                    "
					+ "                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                    "
					+ "                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                    "
					+ "                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                    "
					+ "                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                    "
					+ "                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                    "
					+ "                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                    "
					+ "                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                    "
					+ "                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) is not null and sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +                         "
					+ "                   RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +                         "
					+ "                   RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +                    "
					+ "                   RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +                    "
					+ "                   RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +                    "
					+ "                   RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +                    "
					+ "                   RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +                    "
					+ "                   RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +                    "
					+ "                   RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +                    "
					+ "                   RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +                    "
					+ "                   RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +                    "
					+ "                   RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +                    "
					+ "                   RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63)>=1 and sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +                         "
					+ "                   RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +                         "
					+ "                   RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +                    "
					+ "                   RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +                    "
					+ "                   RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +                    "
					+ "                   RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +                    "
					+ "                   RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +                    "
					+ "                   RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +                    "
					+ "                   RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +                    "
					+ "                   RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +                    "
					+ "                   RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +                    "
					+ "                   RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +                    "
					+ "                   RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) is not null) t2,                                                                  "
					+ "       (select t1.cell_name,                                                                      "
					+ "               t1.ta_numerator /                                                                  "
					+ "               decode(t1.ta_denominator, 0, 1, t1.ta_denominator)-1 ta_average                    "
					+ "          from (select cell_name,                                                                 "
					+ "                       sum(TAVAL0 + TAVAL1 * 2 + TAVAL2 * 3 + TAVAL3 * 4 +                        "
					+ "                           TAVAL4 * 5 + TAVAL5 * 6 + TAVAL6 * 7 + TAVAL7 * 8 +                    "
					+ "                           TAVAL8 * 9 + TAVAL9 * 10 + TAVAL10 * 11 +                              "
					+ "                           TAVAL11 * 12 + TAVAL12 * 13 + TAVAL13 * 14 +                           "
					+ "                           TAVAL14 * 15 + TAVAL15 * 16 + TAVAL16 * 17 +                           "
					+ "                           TAVAL17 * 18 + TAVAL18 * 19 + TAVAL19 * 20 +                           "
					+ "                           TAVAL20 * 21 + TAVAL21 * 22 + TAVAL22 * 23 +                           "
					+ "                           TAVAL23 * 24 + TAVAL24 * 25 + TAVAL25 * 26 +                           "
					+ "                           TAVAL26 * 27 + TAVAL27 * 28 + TAVAL28 * 29 +                           "
					+ "                           TAVAL29 * 30 + TAVAL30 * 31 + TAVAL31 * 32 +                           "
					+ "                           TAVAL32 * 33 + TAVAL33 * 34 + TAVAL34 * 35 +                           "
					+ "                           TAVAL35 * 36 + TAVAL36 * 37 + TAVAL37 * 38 +                           "
					+ "                           TAVAL38 * 39 + TAVAL39 * 40 + TAVAL40 * 41 +                           "
					+ "                           TAVAL41 * 42 + TAVAL42 * 43 + TAVAL43 * 44 +                           "
					+ "                           TAVAL44 * 45 + TAVAL45 * 46 + TAVAL46 * 47 +                           "
					+ "                           TAVAL47 * 48 + TAVAL48 * 49 + TAVAL49 * 50 +                           "
					+ "                           TAVAL50 * 51 + TAVAL51 * 52 + TAVAL52 * 53 +                           "
					+ "                           TAVAL53 * 54 + TAVAL54 * 55 + TAVAL55 * 56 +                           "
					+ "                           TAVAL56 * 57 + TAVAL57 * 58 + TAVAL58 * 59 +                           "
					+ "                           TAVAL59 * 60 + TAVAL60 * 61 + TAVAL61 * 62 +                           "
					+ "                           TAVAL62 * 63 + TAVAL63 * 64 + TAVAL64 * 65 +                           "
					+ "                           TAVAL65 * 66 + TAVAL66 * 67 + TAVAL67 * 68 +                           "
					+ "                           TAVAL68 * 69 + TAVAL69 * 70 + TAVAL70 * 71 +                           "
					+ "                           TAVAL71 * 72 + TAVAL72 * 73 + TAVAL73 * 74 +                           "
					+ "                           TAVAL74 * 75 + TAVAL75 * 76) ta_numerator,                             "
					+ "                       sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +                           "
					+ "                           TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +                           "
					+ "                           TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +                      "
					+ "                           TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +                      "
					+ "                           TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +                      "
					+ "                           TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +                      "
					+ "                           TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +                      "
					+ "                           TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +                      "
					+ "                           TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +                      "
					+ "                           TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +                      "
					+ "                           TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +                      "
					+ "                           TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +                      "
					+ "                           TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +                      "
					+ "                           TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +                      "
					+ "                           TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 +                      "
					+ "                           TAVAL75) ta_denominator                                                "
					+ "                  from "
					+ tmptasourceTab
					+ "                                                             "
					+ "                 group by cell_name having sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +                           "
					+ "                           TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +                           "
					+ "                           TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +                      "
					+ "                           TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +                      "
					+ "                           TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +                      "
					+ "                           TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +                      "
					+ "                           TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +                      "
					+ "                           TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +                      "
					+ "                           TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +                      "
					+ "                           TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +                      "
					+ "                           TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +                      "
					+ "                           TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +                      "
					+ "                           TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +                      "
					+ "                           TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +                      "
					+ "                           TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 +                      "
					+ "                           TAVAL75)>=1 and sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +                           "
					+ "                           TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +                           "
					+ "                           TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +                      "
					+ "                           TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +                      "
					+ "                           TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +                      "
					+ "                           TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +                      "
					+ "                           TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +                      "
					+ "                           TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +                      "
					+ "                           TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +                      "
					+ "                           TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +                      "
					+ "                           TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +                      "
					+ "                           TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +                      "
					+ "                           TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +                      "
					+ "                           TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +                      "
					+ "                           TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 +                      "
					+ "                           TAVAL75) is not null) t1) t3 where t1.cell_name=t2.cell_name and t2.cell_name=t3.cell_name";
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			// printTmpTable(mrranatargetTab, stmt);
			// log.debug("transfer2GMrrToUnifyAnaTable  rows"+rows);
			log.debug("转移2G爱立信mrr数据到统一分析表  行数=" + rows + ",耗时：" + (t2 - t1)
					+ "ms.SQL=" + sql);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("转移2G爱立信mrr数据到统一分析表失败sql:" + sql);
			return false;
		} finally {
		}
		return true;
	}

	/**
	 * 
	 * @title 计算小区相关指标的总入口
	 * @param conn
	 * @param threshold
	 * @param tableMap
	 *            key:ncsTab,cellResTab,clusterTab,clusterCellTab,cellMrrAnaTab
	 *            val:RNO_2G_NCS_ANA_T,RNO_NCS_CELL_ANA_RESULT_MID,
	 *            RNO_NCS_CLUSTER_MID,RNO_NCS_CLUSTER_CELL_MID,rno_2g_mrr_ana_t
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20上午11:36:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCellRes(long cityId, RnoStructAnaJobRunnable worker,
			JobReport report, Connection conn, Map<String, String> tableMap,
			List<RnoThreshold> rnoThresholds, boolean useIdealCoverDis) {
		String ncsTab = tableMap.get("ncsTab"), cellResTab = tableMap
				.get("cellResTab"), clusterTab = tableMap.get("clusterTab"), clusterCellTab = tableMap
				.get("clusterCellTab"), cellMrrAnaTab = tableMap
				.get("cellMrrAnaTab");
		log.debug("小区结构指数当前运行线程：" + Thread.currentThread());
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("计准备stmt出错！");
			e.printStackTrace();
			return false;
		}
		String sql = "";
		// sql = "insert into " + cellResTab
		// + " (CELL,FREQ_CNT) SELECT CELL,min(CELL_FREQ_CNT) FROM "
		// + ncsTab + "  group by cell";

		// 2014-9-22 gmh 修改
		long t1 = System.currentTimeMillis();
		long t2 = t1;
		Date begTime = new Date();
		Date endTime = null;
		boolean ok = false;
		// 准备理想覆盖距离数据
		ok = this.prepare2GCellIdealCoverDis(cityId, stmt, cellResTab,
				rnoThresholds, useIdealCoverDis);
		t2 = System.currentTimeMillis();
		endTime = new Date();
		report.setSystemFields("准备小区理想覆盖距离", begTime, endTime,
				ok ? "成功" : "失败", "");
		worker.addJobReport(report);
		log.debug("准备小区理想覆盖距离耗时：" + (t2 - t1) + "ms");

		// 准备小区结果表的数据
		t1 = System.currentTimeMillis();
		sql = "insert into "
				+ cellResTab
				+ " (CELL,FREQ_CNT,EXPECTED_COVER_DIS) SELECT T1.CELL,T1.CELL_FREQ_CNT,T2.IDEAL_DIS FROM rno_cell_city_t T1 ,RNO_CELL_IDEALDIS T2 WHERE T1.CELL=T2.CELL";
		log.debug("将ncs数据[" + ncsTab + "]转移到小区分析结果表[" + cellResTab + "]的sql="
				+ sql);
		try {
			int affcnt = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("准备小区结果表的数据，涉及记录数：" + affcnt + ",耗时：" + (t2 - t1) + "ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("转移小区相关数据到小区分析结果表时出错！");
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		// -------------更新小区对应的MRR指标--------------------------//
		ok = this.update2GCellMrrIndex(conn, cellMrrAnaTab, cellResTab);
		endTime = new Date();
		report.setSystemFields("计算MRR指标", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// -------------计算小区理想覆盖距离--------------------------//
		// begTime=new Date();
		// ok=this.calculate2GCellIdealCoverDis(cityId,stmt, ncsTab, cellResTab,
		// rnoThresholds);
		// endTime=new Date();
		// report.setSystemFields("计算小区理想覆盖距离", begTime, endTime, ok?"成功":"失败",
		// "");
		// worker.addJobReport(report);

		// --------------过覆盖系数------------------------//
		/*
		 * float adjustFactor=1.6f; if (threshold != null){
		 * adjustFactor=Integer.
		 * parseInt(threshold.getOverShootingIdealDisMultiple()); }
		 */
		begTime = new Date();
		ok = this.calculate2GCellOverCoverFactor(stmt, ncsTab, cellResTab,
				rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算过覆盖系数", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// -------------计算干扰系数----------------------//
		begTime = new Date();
		ok = this.calculate2GCellInterferFactor(conn, ncsTab, cellResTab,
				rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算干扰系数", begTime, endTime, ok ? "成功" : "失败", "");
		worker.addJobReport(report);

		// // --------------计算网内干扰系数-------------------//
		// begTime=new Date();
		// ok=this.calculate2GCellNetInterFactor(stmt, ncsTab, cellResTab, null,
		// rnoThresholds);
		// endTime=new Date();
		// report.setSystemFields("计算网内干扰系数", begTime, endTime, ok?"成功":"失败",
		// "");

		// -------------网络结构指数----------------------//
		begTime = new Date();
		ok = this.calculate2GNetworkStrucFactor(conn, ncsTab, cellResTab,
				rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算网络结构指数", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// ------------冗余覆盖度--------------------------//
		begTime = new Date();
		ok = this.calculate2GRedundantCoverFacotr(conn, ncsTab, cellResTab,
				rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算冗余覆盖度", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// ------------重叠覆盖度--------------------------//
		begTime = new Date();
		ok = this.calculate2GOverlapCoverFactor(conn, ncsTab, cellResTab);
		endTime = new Date();
		report.setSystemFields("计算重叠覆盖度", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// ------------小区检测次数------------------------//
		begTime = new Date();
		ok = this.calculate2GDetectCnt(conn, ncsTab, cellResTab, rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算小区检测次数", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// -------------关联邻小区数-------------------------//
		begTime = new Date();
		ok = this.calculate2GStrongAssNcellCnt(conn, ncsTab, cellResTab,
				rnoThresholds);
		endTime = new Date();
		report.setSystemFields("计算关联邻小区数", begTime, endTime, ok ? "成功" : "失败",
				"");
		worker.addJobReport(report);

		// -----------小区覆盖分量-----------??-------------//
		// this.calculate2GCellCover(conn,ncsTab, clusterTab,
		// clusterCellTab, cellResTab,threshold);
		// ------------小区容量分量---------??--------------//
		/*
		 * this.calculate2GCapacityDestroy(conn, ncsTab, clusterTab,
		 * clusterCellTab, cellResTab, rnoThresholds);
		 */

		// 输出原始文件信息
		// dumpRnoNcsMidData(stmt);
		return true;
	}

	/**
	 * 准备理想覆盖距离数据
	 * 
	 * @param cityId
	 * @param stmt
	 * @param cellResTab
	 * @param rnoThresholds
	 * @return
	 * @author brightming 2014-9-22 下午3:52:04
	 */
	private boolean prepare2GCellIdealCoverDis(long cityId, Statement stmt,
			String cellResTab, List<RnoThreshold> rnoThresholds,
			boolean useIdealCoverDis) {
		int lowestN = 3;
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("CellIdealDisReferenceCellNum".toUpperCase())) {
					lowestN = Integer.parseInt(val);
				}
			}
		}
		if (lowestN <= 0) {
			lowestN = 3;
		}
		if (stmt == null) {
			log.error("准备理想覆盖距离时，未提供数据库连接！");
			return false;
		}

		// List<Map<String, Object>> ress = RnoHelper.commonQuery(stmt,
		// "select count(*) cnt from RNO_CELL_IDEALDIS where city_id="
		// + cityId + " and COVER_ANGLE=120 and REF_CNT="
		// + lowestN);
		// if (ress != null && ress.size() > 0) {
		// long cnt = Long.parseLong(ress.get(0).get("CNT").toString());
		// log.debug("city=" + cityId + "下的小区的理想覆盖距离个数：" + cnt);
		// if (cnt == 0) {
		// 重新运算
		RnoStructAnaV2Impl tool = new RnoStructAnaV2Impl();
		Connection conn = DataSourceConn.initInstance().getConnection();
		ResultInfo result = tool.calculateCellIdealDis(conn, cityId, 120,
				lowestN, true);// 强制计算
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!result.isFlag()) {
			log.error("计算小区的理想覆盖距离出错！");
			return false;
		}
		// }
		// }

		return true;
	}

	/**
	 * 
	 * @title 根据网络中小区的相对位置，我们把目标小区天线主瓣方向120度范围内的最近的3
	 *        个相邻基站到目标基站的距离的均值D作为目标小区的理想覆盖距离， 计算900和1800时分别只考虑同属本频段的站，不考虑室内小区
	 * @param stmt
	 * @param ncsTab
	 * @param cellResTabName
	 * @param lowestN
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午1:22:58
	 * @company 怡创科技
	 * @version 1.2
	 */
	// public boolean calculate2GCellIdealCoverDis(long cityId, Statement stmt,
	// String ncsTab, String cellResTabName,
	// List<RnoThreshold> rnoThresholds) {
	// log.debug("进入dao方法：calculate2GCellIdealCoverDis。stmt=" + stmt
	// + ",ncsTab=" + ncsTab + ",cellResTabName=" + cellResTabName);
	// int lowestN = 3;
	// float strongCorrelCi = 0.05f;
	// if (rnoThresholds != null) {
	// for (RnoThreshold rnoThreshold : rnoThresholds) {
	// String code = rnoThreshold.getCode();
	// String val = rnoThreshold.getDefaultVal();
	// if (code.equals("CellIdealDisReferenceCellNum".toUpperCase())) {
	// lowestN = Integer.parseInt(val);
	// }
	// if (code.equals("SameFreqInterThreshold".toUpperCase())) {
	// strongCorrelCi = Float.parseFloat(val);
	// }
	// }
	// }
	// /*
	// * float lowestN = 3; if (threshold != null) { lowestN =
	// * Float.parseFloat(threshold .getCellIdealDisReferenceCellNum()); }
	// * float strongCorrelCi = 0.05f; if (threshold != null) { strongCorrelCi
	// * = Float.parseFloat(threshold .getSameFreqInterThreshold()); }
	// */
	// if (lowestN <= 0) {
	// lowestN = 3;
	// }
	// if (stmt == null) {
	// log.error("计算理想覆盖距离时，未提供数据库连接！");
	// return false;
	// }
	// if (ncsTab == null || "".equals(ncsTab.trim())) {
	// log.error("计算理想覆盖距离时，未提供ncs数据表名称！");
	// return false;
	// }
	// if (cellResTabName == null || "".equals(cellResTabName.trim())) {
	// log.error("计算理想覆盖距离时，未提供小区结果存放表名称！");
	// return false;
	// }
	// /*
	// * 2014-9-22根据省公司定义，重新写。 理想覆盖距离与ncs无任何关系，只需要有小区经度纬度与方向角即可以计算。
	// * 根据网络中小区的相对位置，我们把目标小区天线主瓣方向120度范围内的最近的3
	// * 个相邻基站到目标基站的距离的均值D作为目标小区的理想覆盖距离，计算900和1800时分别只考虑同属本频段的站，不考虑室内小区。
	// *
	// * // String innerSql =
	// *
	// "SELECT mid4.*, row_number()over(partition BY MID4.cell order by mid4.dis ASC) seq2 from ("
	// * // +
	// *
	// " SELECT MID3.CELL,MID3.NCELL,MID3.N_SITE,MID3.DIS,MID3.S_DIR,MID3.N_DIR AS S_N_ANGEL_DIR,MID3.LOW_EDGE,MID3.HIGH_EDGE ,row_number()over(partition BY MID3.cell,mid3.n_site order by mid3.dis ASC) seq FROM "
	// * // + " ( " // +
	// *
	// " select mid2.*,CELL.bcch as n_bcch ,CELL.SITE AS N_SITE,calculateDirToNorth(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as n_dir,CELL.LONGITUDE as n_lon,cell.LATITUDE as n_lat,FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as dis FROM "
	// * // + " ( " // +
	// *
	// " select mid1.*,cell.bcch as s_bcch,CELL.BEARING as s_dir,CELL.SITE AS S_SITE,(case when CELL.BEARING<60 then 300+CELL.BEARING else CELL.BEARING-60 end) as low_edge,(case when CELL.BEARING>300 then CELL.BEARING-300 else CELL.BEARING+60 end ) as high_edge ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
	// * // + " from( " // +
	// * " select cell,ncell,min(NCELL_INDOOR) as NCELL_INDOOR from " // +
	// * ncsTab // + " where  (CI_DIVIDER/CI_DENOMINATOR) >" // +
	// * strongCorrelCi // + " group by cell,ncell " // + " )mid1 " // +
	// * " inner join cell " // + " on  " // + " ( " // +
	// * " mid1.cell=cell.LABEL and NCELL_INDOOR='N'" // + " ) " // +
	// * " )mid2 " // + " inner join CELL " // + " on " // + " ( " // +
	// * " MID2.ncell=CELL.label " // +
	// *
	// " and (mid2.s_bcch>100 and CELL.bcch>100 or MID2.s_bcch<100 and cell.bcch<100 AND MID2.S_SITE!=CELL.SITE) "
	// * // 计算同频段 // + " ) " // + " order BY cell asc,dis asc " // + " )MID3 "
	// * // +
	// *
	// "WHERE MID3.DIS>0  AND fun_rno_isbetweenlowandhigh(MID3.LOW_EDGE,MID3.HIGH_EDGE,N_DIR)='y' "
	// * // + " )mid4  WHERE seq=1  ORDER BY MID4.CELL,MID4.NCELL "; // // //
	// * 用于输出参考邻区的 sql // String outputSql =
	// *
	// "SELECT mid4.*, row_number()over(partition BY MID4.cell order by mid4.dis ASC) seq2 from ("
	// * // +
	// *
	// " SELECT MID3.CELL,MID3.NCELL,MID3.N_SITE,MID3.DIS,MID3.S_DIR,MID3.N_DIR AS S_N_ANGEL_DIR,MID3.LOW_EDGE,MID3.HIGH_EDGE ,row_number()over(partition BY MID3.cell,mid3.n_site order by mid3.dis ASC) seq FROM "
	// * // + " ( " // +
	// *
	// " select mid2.*,CELL.bcch as n_bcch ,CELL.SITE AS N_SITE,calculateDirToNorth(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as n_dir,CELL.LONGITUDE as n_lon,cell.LATITUDE as n_lat,FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as dis FROM "
	// * // + " ( " // +
	// *
	// " select mid1.*,cell.bcch as s_bcch,CELL.BEARING as s_dir,CELL.SITE AS S_SITE,(case when CELL.BEARING<60 then 300+CELL.BEARING else CELL.BEARING-60 end) as low_edge,(case when CELL.BEARING>300 then CELL.BEARING-300 else CELL.BEARING+60 end ) as high_edge ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
	// * // + " from( " // +
	// * " select cell,ncell,min(NCELL_INDOOR) as NCELL_INDOOR from " // +
	// * ncsTab // + " where  (CI_DIVIDER/CI_DENOMINATOR) >" // +
	// * strongCorrelCi // + " group by cell,ncell " // + " )mid1 " // +
	// * " inner join cell " // + " on  " // + " ( " // +
	// * " mid1.cell=cell.LABEL and NCELL_INDOOR='N'" // + " ) " // +
	// * " )mid2 " // + " inner join CELL " // + " on " // + " ( " // +
	// * " MID2.ncell=CELL.label " // +
	// *
	// " and (mid2.s_bcch>100 and CELL.bcch>100 or MID2.s_bcch<100 and cell.bcch<100 )"
	// * // AND // // MID2.S_SITE!=CELL.SITE) // // "// // // 计算同频段 // + " ) "
	// * // + " order BY cell asc,dis asc " // + " )MID3 " // +
	// * "WHERE MID3.DIS>0  "// AND // //
	// * fun_rno_isbetweenlowandhigh(MID3.LOW_EDGE,MID3.HIGH_EDGE,N_DIR)='y'
	// * // // " // + " )mid4 WHERE seq=1 ORDER BY MID4.CELL,MID4.NCELL ";
	// * // // String sql = "select mid5.cell,avg(dis) as dis from " + "(" +
	// * innerSql // + " )mid5 " + " where seq2<=" + lowestN +
	// * " group by cell";
	// *
	// * // 2014-8-26 :以上sql作废 String sql=""; sql =
	// *
	// "select cell,avg(distance) dis from ( select cell,ncell,DISTANCE,row_number()over(partition BY cell order by DISTANCE ASC) rn "
	// * + " from " + ncsTab +
	// *
	// " where rno_getAngleBtTwoAngle(cell_bearing,CELL_TO_NCELL_DIR) <=60 and cell_site<>ncell_site and CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND CELL_INDOOR=NCELL_INDOOR "
	// * + " and ci_divider/CI_DENOMINATOR>" + strongCorrelCi +
	// * " and CI_DENOMINATOR>0 )mid where rn<=" + lowestN + " group by cell";
	// *
	// *
	// * //2014-9-21 gmh 根据新定义，修改 sql=
	// *
	// " select cell,NCELL_SITE,DISTANCE,row_number()over(partition BY cell,NCELL_SITE order by DISTANCE ASC) rn "
	// * + " from " + ncsTab +
	// * " where rno_getAngleBtTwoAngle(cell_bearing,CELL_TO_NCELL_DIR) <=60 "
	// * +
	// *
	// " and cell_site<>ncell_site and CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND CELL_INDOOR=NCELL_INDOOR "
	// * ;
	// *
	// *
	// * sql="select cell,avg(distance) dis from ( "+sql+" ) mid where rn<="+
	// * lowestN+" group by cell ";
	// *
	// * log.debug("准备计算小区理想覆盖距离的sql=" + sql); sql = "Merge into " +
	// * cellResTabName + " tar using ( " + sql + " ) src  on ( " +
	// *
	// " tar.cell=src.cell ) when matched then update set tar.EXPECTED_COVER_DIS=src.dis"
	// * ;
	// *
	// * log.debug("更新理想覆盖距离的sql=" + sql);
	// *
	// * // 准备statement // Statement stmt = null; // try { // stmt =
	// * conn.createStatement(); // } catch (SQLException e) { //
	// * log.error("计算理想覆盖距离准备stmt出错！"); // e.printStackTrace(); // return
	// * false; // }
	// *
	// * // --查看小区参考的结果 // if (log.isInfoEnabled()) { // log.debug("查看参考邻区情况："
	// * + outputSql); // List<Map<String, Object>> midRes =
	// * RnoHelper.commonQuery(stmt, // outputSql); // //
	// *
	// log.debug("cell,ncell,n_site,dis,S_DIR,S_N_ANGEL_DIR,LOW_EDGE,HIGH_EDGE"
	// * ); // // for (Map<String, Object> one : midRes) { // //
	// * log.debug(one.get("CELL") + "," + one.get("NCELL") + // //
	// * ","+one.get("N_SITE")+"," // // + // //
	// * one.get("DIS")+","+one.get("S_DIR"
	// * )+","+one.get("S_N_ANGEL_DIR")+","+one
	// * .get("LOW_EDGE")+","+one.get("HIGH_EDGE")); // // } // // String[]
	// * titles = { "CELL", "NCELL", "邻区site", "dis", "主小区方向角", // "主小区到邻区夹角",
	// * "角度允许范围下限", "角度允许上限" }; // String[] keys = { "CELL", "NCELL",
	// * "N_SITE", "DIS", "S_DIR", // "S_N_ANGEL_DIR", "LOW_EDGE", "HIGH_EDGE"
	// * }; // // List<String[]> titless = new ArrayList<String[]>(); //
	// * titless.add(titles); // List<String[]> keyss = new
	// * ArrayList<String[]>(); // keyss.add(keys); // // String tempNeiPath =
	// * "/tmp/neighbourRef.xlsx"; // ByteArrayOutputStream baos =
	// * FileTool.putDataOnExcelOutputStream( // Arrays.asList("参考邻区"),
	// * titless, keyss, // Arrays.asList(midRes)); // InputStream is = new
	// * ByteArrayInputStream(baos.toByteArray()); // // String
	// * cellClusterPath = dir + "ncs_res_" + taskId + ".xls"; //
	// * log.debug("保存参考邻区数据文件：" + tempNeiPath); // boolean ok =
	// * FileTool.saveInputStream(tempNeiPath, is); // log.debug("保存参考邻区数据文件："
	// * + tempNeiPath + ",保存结果：" + ok); // midRes = null; // baos = null; //
	// * } try { long t1=System.currentTimeMillis(); int rows =
	// * stmt.executeUpdate(sql); long t2=System.currentTimeMillis();
	// * log.debug("更新理想覆盖距离的行数：" + rows+"，耗时："+(t2-t1)+"ms"); //
	// * log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>更新理想覆盖距离后  开始打印" // +
	// * cellResTabName + "表数据"); // printTmpTable(cellResTabName, stmt); //
	// * log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<结束打印" + cellResTabName // +
	// * "表数据"); } catch (SQLException e) { e.printStackTrace();
	// * log.error("更新理想覆盖距离时sql出错：" + sql); return false; } finally { // try
	// * { // stmt.close(); // } catch (SQLException e) { //
	// * e.printStackTrace(); // } } return true;
	// */
	//
	// }

	/**
	 * 
	 * @title 计算小区的被干扰系数、干扰源系数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:07:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCellInterferFactor(Connection conn,
			String ncsTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		// System.out
		// .println("进入calculate2GCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法.conn:"
		// + conn + ", ncsNcellTabName:" + ncsTabName);
		boolean flag = calculate2GBeInterfered(conn, ncsTabName, cellResTab,
				rnoThresholds);
		if (!flag) {
			return flag;
		}
		flag = calculate2GSrcInterfered(conn, ncsTabName, cellResTab,
				rnoThresholds);
		return flag;

	}

	/**
	 * 
	 * @title 计算被干扰系数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param groupBy
	 * @param factor
	 * @param name
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:10:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean calculate2GBeInterfered(Connection conn, String ncsTabName,
			String cellResTab, List<RnoThreshold> rnoThresholds) {
		String optimalDis = "15000", CaOrCiInterLowestFactor = "0.02";
		/*
		 * if (threshold != null) { optimalDis =
		 * threshold.getInterFactorMostDistant(); CaOrCiInterLowestFactor =
		 * threshold .getInterFactorSameAndAdjFreqMinimumThreshold(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("InterFactorMostDistant".toUpperCase())) {
					optimalDis = val;
				}
				if (code.equals("InterFactorSameAndAdjFreqMinimumThreshold"
						.toUpperCase())) {
					CaOrCiInterLowestFactor = val;
				}
			}
		}
		String statistics = "sum((CI*SAME_FREQ_CNT+CA*ADJ_FREQ_CNT)/(decode(CELL_FREQ_CNT,0,null,CELL_FREQ_CNT)))";
		boolean flag = false;
		Statement stmt = null;
		String mergeSql = "";
		try {
			/*
			 * String innerSelectSql = "select " + groupBy +
			 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(CI_DENOMINATOR) CI,sum(CA_DIVIDER)/sum(CA_DENOMINATOR) CA from "
			 * + ncsTabName + " where CI_DENOMINATOR>0 and distance<=" +
			 * optimalDis + " and (CI_DIVIDER/CI_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor + " and (CA_DIVIDER/CA_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor +
			 * "  group by CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT  HAVING SUM(CI_DENOMINATOR)>0"
			 * ; //@author chao.xj 2014-9-12 下午4:50:20 修改 innerSelectSql =
			 * "select " + groupBy +
			 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,CI_DIVIDER/CI_DENOMINATOR CI,CA_DIVIDER/CA_DENOMINATOR CA from "
			 * + ncsTabName + " where CI_DENOMINATOR>0 and distance<=" +
			 * optimalDis + " and (CI_DIVIDER/CI_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor + " and (CA_DIVIDER/CA_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor; String selectSql = "select " + groupBy +
			 * "," + statistics + " " + factor + " from (" + innerSelectSql +
			 * ") group by " + groupBy;
			 */

			// 2014-9-19 gmh 修改
			String innerSelectSql = "select cell,"
					+ "sum(((CI_DIVIDER/CI_DENOMINATOR)*SAME_FREQ_CNT+ (CA_DIVIDER/CA_DENOMINATOR)*ADJ_FREQ_CNT)/CELL_FREQ_CNT) be_inter from "
					+ ncsTabName
					+ " where CI_DENOMINATOR>=1 and CA_DENOMINATOR>=1 and CELL_FREQ_CNT>=1"
					+ " group by cell";
			mergeSql = "merge into " + cellResTab + " targ using ("
					+ innerSelectSql + ") src on( targ.CELL=src.cell"
					+ ") when matched then update set targ.BE_INTERFER"
					+ "=src.be_inter";
			log.debug("被干扰系数mergesql:" + mergeSql);
			stmt = conn.createStatement();
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(mergeSql);
			long t2 = System.currentTimeMillis();
			log.debug("计算被干扰系数的行数：" + rows + "，耗时：" + (t2 - t1) + "ms");
			flag = true;
		} catch (SQLException e) {
			log.error("更新被干扰系数失败,sql=" + mergeSql);
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭stmt失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * @title 计算干扰源系数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param groupBy
	 * @param factor
	 * @param name
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:11:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean calculate2GSrcInterfered(Connection conn,
			String ncsTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		String optimalDis = "15000", CaOrCiInterLowestFactor = "0.02";
		/*
		 * if (threshold != null) { optimalDis =
		 * threshold.getInterFactorMostDistant(); CaOrCiInterLowestFactor =
		 * threshold .getInterFactorSameAndAdjFreqMinimumThreshold(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("InterFactorMostDistant".toUpperCase())) {
					optimalDis = val;
				}
				if (code.equals("InterFactorSameAndAdjFreqMinimumThreshold"
						.toUpperCase())) {
					CaOrCiInterLowestFactor = val;
				}
			}
		}
		String statistics = "sum((CI*SAME_FREQ_CNT+CA*ADJ_FREQ_CNT)/(decode(NCELL_FREQ_CNT,0,null,NCELL_FREQ_CNT)))";
		boolean flag = false;
		Statement stmt = null;
		String mergeSql = "";
		try {
			/*
			 * String innerSelectSql = "select " + groupBy +
			 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(CI_DENOMINATOR) CI,sum(CA_DIVIDER)/sum(CA_DENOMINATOR) CA from "
			 * + ncsTabName + " where  CA_DENOMINATOR>0  and distance<=" +
			 * optimalDis + " and (CI_DIVIDER/CI_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor + " and (CA_DIVIDER/CA_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor +
			 * "  group by CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT  HAVING SUM(CA_DENOMINATOR)>0"
			 * ; //@author chao.xj 2014-9-12 下午4:34:23 修改 innerSelectSql =
			 * "select " + groupBy +
			 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,CI_DIVIDER/CI_DENOMINATOR CI,CA_DIVIDER/CA_DENOMINATOR CA from "
			 * + ncsTabName + " where  CA_DENOMINATOR>0  and distance<=" +
			 * optimalDis + " and (CI_DIVIDER/CI_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor + " and (CA_DIVIDER/CA_DENOMINATOR)>" +
			 * CaOrCiInterLowestFactor; String selectSql = "select " + groupBy +
			 * "," + statistics + " " + factor + " from (" + innerSelectSql +
			 * ") group by " + groupBy;
			 * 
			 * 
			 * 
			 * 
			 * mergeSql = "merge into " + cellResTab + " targ using (" +
			 * selectSql + ") temp on(targ.CELL=temp." + groupBy +
			 * ") when matched then update set targ." + factor + "=temp." +
			 * factor;
			 */

			// 2014-9-19 gmh 修改
			String innerSelectSql = "select ncell,"
					+ "sum(((CI_DIVIDER/CI_DENOMINATOR)*SAME_FREQ_CNT +(CA_DIVIDER/CA_DENOMINATOR)*ADJ_FREQ_CNT)/NCELL_FREQ_CNT) src_inter from "
					+ ncsTabName
					+ " where CI_DENOMINATOR>=1 and CA_DENOMINATOR>=1 and NCELL_FREQ_CNT>=1"
					+ " group by ncell";
			mergeSql = "merge into " + cellResTab + " targ using ("
					+ innerSelectSql + ") src on( targ.CELL=src.ncell"
					+ ") when matched then update set targ.SRC_INTERFER"
					+ "=src.src_inter";

			log.debug("干扰源系数mergesql:" + mergeSql);
			stmt = conn.createStatement();
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(mergeSql);
			long t2 = System.currentTimeMillis();
			log.debug("计算干扰源系数的行数：" + rows + "，耗时：" + (t2 - t1) + "ms");
			flag = true;
		} catch (SQLException e) {
			log.error("更新干扰源系数失败,sql=" + mergeSql);
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭stmt失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * @title
	 * @param stmt
	 * @param ncsTab
	 * @param cellResTabName
	 * @param adjustFactor
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午1:27:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCellOverCoverFactor(Statement stmt,
			String ncsTab, String cellResTabName,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入dao方法：calculate2GCellOverCoverFactor。stmt=" + stmt
				+ ",ncsTab=" + ncsTab + ",cellResTabName=" + cellResTabName
				+ ",rnoThresholds=" + rnoThresholds);
		float adjustFactor = 1.6f;
		float strongCorrelCi = 0.05f;
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("OverShootingIdealDisMultiple".toUpperCase())) {
					adjustFactor = Float.parseFloat(val);
				}
				if (code.equals("SameFreqInterThreshold".toUpperCase())) {
					strongCorrelCi = Float.parseFloat(val);
				}
			}
		}
		/*
		 * if (threshold != null) { adjustFactor = Float.parseFloat(threshold
		 * .getOverShootingIdealDisMultiple()); }
		 * 
		 * if (threshold != null) { strongCorrelCi = Float.parseFloat(threshold
		 * .getSameFreqInterThreshold()); }
		 */
		if (stmt == null) {
			log.error("计算过覆盖系数时，未提供数据库连接！");
			return false;
		}
		if (ncsTab == null || "".equals(ncsTab.trim())) {
			log.error("计算过覆盖系数时，未提供ncs数据表名称！");
			return false;
		}
		if (cellResTabName == null || "".equals(cellResTabName.trim())) {
			log.error("计算过覆盖系数时，未提供小区结果存放表名称！");
			return false;
		}

		// 准备statement
		// Statement stmt = null;
		// try {
		// stmt = conn.createStatement();
		// } catch (SQLException e) {
		// log.error("计算过覆盖系数准备stmt出错！");
		// e.printStackTrace();
		// return false;
		// }

		String sql = "merge into "
				+ cellResTabName
				+ " tar using( "
				+ " select mid2.cell,max(FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE)) as dis FROM "
				+ " ( "
				+ " select mid1.* ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
				+ " from( "
				+ " select cell,ncell  from "
				+ ncsTab
				+ " where  (CI_DIVIDER/CI_DENOMINATOR) >"
				+ strongCorrelCi
				+ " group by cell,ncell "
				+ " )mid1 "
				+ " inner join cell "
				+ " on  "
				+ " ( "
				+ " mid1.cell=cell.LABEL "
				+ " ) "
				+ " )mid2 "
				+ " inner join CELL "
				+ " on "
				+ " ( "
				+ " MID2.ncell=CELL.label "
				+ " ) "
				+ " group by cell "
				+ " order BY cell asc,dis asc "
				+ " ) src  "
				+ " on (tar.cell=src.cell) "
				+ " when matched then update set tar.OVERSHOOTING_FACT=(case when tar.EXPECTED_COVER_DIS=0 then null else src.dis/(tar.EXPECTED_COVER_DIS*"
				+ adjustFactor + ") end)";

		// --2014-8-26 :以上sql作废

		sql = "merge into "
				+ cellResTabName
				+ " tar using( select cell,distance from ("
				+ " select cell,distance,row_number()over(partition BY cell order by DISTANCE desc) rn from  "
				+ ncsTab
				+ " where ci_divider/CI_DENOMINATOR>"
				+ strongCorrelCi
				+ " and CI_DENOMINATOR>0 )where rn=1 ) src "
				+ " on(tar.cell=src.cell) when matched then update set tar.OVERSHOOTING_FACT=(case when tar.EXPECTED_COVER_DIS=0 then null else src.distance/(tar.EXPECTED_COVER_DIS*"
				+ adjustFactor + ") end) ";

		// 2014-9-23 根据新定义更新
		// 具体参照需求调研中的公式说明。
		sql = "select ncs.ncell,"
				+ " sum(ncs.ci_divider/ncs.CI_DENOMINATOR * (case when cellres.EXPECTED_COVER_DIS >ncs.distance then 1 else round(ncs.distance/cellres.EXPECTED_COVER_DIS,2) end))/sum(ncs.ci_divider/ncs.CI_DENOMINATOR) val "
				+ " from rno_2g_ncs_ana_t ncs,rno_ncs_cell_ana_result_mid cellres "
				+ " where ncs.ci_divider>=1 and ncs.ncell=cellres.cell and ncs.CI_DENOMINATOR>=1 and cellres.EXPECTED_COVER_DIS>0 group by ncs.ncell";

		sql = "merge into "
				+ cellResTabName
				+ " tar using ( "
				+ sql
				+ " ) src "
				+ " on(tar.cell=src.ncell) when matched then update set tar.OVERSHOOTING_FACT=src.val";
		log.debug("计算过覆盖系数的sql=" + sql);
		try {
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			log.debug("更新过覆盖系数影响的行数：" + rows + ",耗时：" + (t2 - t1) + "ms");
			// log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>更新过覆盖系数后  开始打印"
			// + cellResTabName + "表数据");
			// printTmpTable(cellResTabName, stmt);
			// log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<结束打印" + cellResTabName
			// + "表数据");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算过覆盖系数时出错sql！" + sql);
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}

		return true;
	}

	/**
	 * 
	 * @title 计算网内干扰系数 小区网内干扰系数=∑_i〖〖(CO〗_si*〖NCO〗_si/N_s
	 *        〗+〖ADJ〗_si*〖NADJ〗_si/N_s )
	 * @param stmt
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param result
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:19:59
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCellNetInterFactor(Statement stmt,
			String ncsNcellTabName, String cellResTab,
			Map<String, String> result, List<RnoThreshold> rnoThresholds) {
		log.debug("开始计算网内干扰系数。,ncsNcellTabName=" + ncsNcellTabName
				+ ",cellResTab=" + cellResTab);
		String optimalDis = "15000", CaOrCiInterLowestFactor = "0.05";
		/*
		 * if (threshold != null) { optimalDis =
		 * threshold.getInterFactorMostDistant(); CaOrCiInterLowestFactor =
		 * threshold .getInterFactorSameAndAdjFreqMinimumThreshold(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("InterFactorMostDistant".toUpperCase())) {
					optimalDis = val;
				}
				if (code.equals("InterFactorSameAndAdjFreqMinimumThreshold"
						.toUpperCase())) {
					CaOrCiInterLowestFactor = val;
				}
			}
		}
		String sql = "select CELL,sum((CI_DIVIDER/CI_DENOMINATOR)*(same_freq_cnt/cell_freq_cnt)+(CA_DIVIDER/CA_DENOMINATOR)*(adj_freq_cnt/cell_freq_cnt)) AS CELL_NET_INTERFER from "
				+ ncsNcellTabName
				+ "  WHERE "
				+ "  cell_freq_cnt<>0 and (CI_DIVIDER/CI_DENOMINATOR)>"
				+ CaOrCiInterLowestFactor
				+ " and (CA_DIVIDER/CA_DENOMINATOR)>"
				+ CaOrCiInterLowestFactor
				+ " and distance<="
				+ optimalDis
				+ " GROUP BY CELL";

		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on (TAR.CELL=SRC.CELL) when matched then update set tar.CELL_NET_INTERFER=SRC.CELL_NET_INTERFER";
		log.debug("计算网内干扰的sql=" + sql);
		try {
			int cnt = stmt.executeUpdate(sql);
			log.debug("计算网内干扰系数，影响记录：" + cnt);

		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算小区的网内干扰出错！sql=" + sql);
			if (result != null) {
				result.put("计算小区网内干扰", "出错");
			}
		}
		if (result != null) {
			result.put("计算小区网内干扰", "成功");
		}
		return false;
	}

	/**
	 * 
	 * @title 计算网络结构指数
	 * @param conn
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:29:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GNetworkStrucFactor(Connection conn,
			String ncsNcellTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入calculate2GNetworkStrucFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		float GSM900_FEQ_CNT = 95, GSM1800_FEQ_CNT = 124;
		/*
		 * if (threshold != null) { GSM900_FEQ_CNT =
		 * Float.parseFloat(threshold.getGsm900CellFreqNum()); GSM1800_FEQ_CNT =
		 * Float.parseFloat(threshold .getGsm1800CellFreqNum()); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("Gsm900CellFreqNum".toUpperCase())) {
					GSM900_FEQ_CNT = Float.parseFloat(val);
				}
				if (code.equals("Gsm1800CellFreqNum".toUpperCase())) {
					GSM1800_FEQ_CNT = Float.parseFloat(val);
				}
			}
		}
		boolean flag = true;

		// // 更新gsm900
		//
		// boolean flag = calculate2GNfOrRcf(conn, ncsNcellTabName, cellResTab,
		// "CELL", "NCELL_FREQ_CNT", "NET_STRUCT_FACTOR", "BCCH<100",
		// "网络结构指数(gsm900)", GSM900_FEQ_CNT);
		//
		//
		// if (!flag) {
		// return flag;
		// }
		// // 更新gsm1800
		// flag = calculate2GNfOrRcf(conn, ncsNcellTabName, cellResTab, "CELL",
		// "NCELL_FREQ_CNT", "NET_STRUCT_FACTOR", "BCCH>100",
		// "网络结构指数(gsm1800)", GSM1800_FEQ_CNT);
		// RnoConstant.BusinessConstant.GSM1800_FEQ_CNT

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		String sql = "";
		sql = " SELECT CELL,VAL/((case WHEN CELL_FREQ_SECTION='GSM900' THEN "
				+ GSM900_FEQ_CNT
				+ " ELSE "
				+ GSM1800_FEQ_CNT
				+ " END)) val FROM "
				+ " ( select cell,sum((CI_DIVIDER/CI_DENOMINATOR) * NCELL_FREQ_CNT) AS VAL,min(CELL_FREQ_SECTION) AS CELL_FREQ_SECTION "
				+ " from "
				+ ncsNcellTabName
				+ " where CI_DENOMINATOR>0  AND  cell_freq_section=ncell_freq_section group by cell)";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on (tar.cell=src.cell) when matched then update set tar.NET_STRUCT_FACTOR=src.val";
		log.debug("更新网络结构指数的sql=" + sql);
		long t1 = System.currentTimeMillis();
		long t2 = t1;
		try {
			int rows = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("更新网络结构指数影响行数：" + rows + ",耗时：" + (t2 - t1) + "ms");
		} catch (SQLException e) {
			log.error("更新网络结构指数失败！sql=" + sql);
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		return flag;

	}

	/**
	 * 
	 * @title 计算网络结构指数或冗余覆盖指数:
	 * @param conn
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param groupBy
	 * @param field
	 * @param factor
	 * @param gsm
	 * @param name
	 * @param wholeSecFreqCnt
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:35:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	// private boolean calculate2GNfOrRcf_f(Connection conn, String
	// ncsNcellTabName,
	// String cellResTab, String groupBy, String field, String factor,
	// String gsm, String name, float wholeSecFreqCnt) {
	// log.debug("conn:" + conn + ",  ncsNcellTabName:"
	// + ncsNcellTabName);
	// // String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
	// // flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName,
	// // cellResTab,"CELL",
	// // "NCELL_FREQ_CNT","NET_STRUCT_FACTOR","BCCH>100","网络结构指数(gsm1800)");
	//
	// String statistics = "sum(rn.CI * rn." + field + "/?)";
	// String innerSelectSql = get2GCiSql(ncsNcellTabName, groupBy);
	// String selectSql1 = "select  rn." + groupBy + " " + groupBy + ","
	// + statistics + " " + factor + " from (" + innerSelectSql
	// + ") rn inner join CELL c on rn." + groupBy
	// + " = c.LABEL where c.";
	// String selectSql2 = " group by rn." + groupBy;
	// boolean flag = false;
	// PreparedStatement pstatement = null;
	// // 更新网络结构指数gsm900和gsm1800
	// String mergeSql = "";
	// try {
	// String selectSql = selectSql1 + gsm + selectSql2;
	// mergeSql = get2GMergeSql(cellResTab, selectSql, factor, groupBy);
	// // log.debug("mergeSql:" + mergeSql);
	// log.debug(name + "mergeSql:" + mergeSql);
	// pstatement = conn.prepareStatement(mergeSql);
	// pstatement.setFloat(1, wholeSecFreqCnt);
	// // pstatement.setLong(2, ncsDescId);
	// pstatement.executeUpdate();
	// flag = true;
	// } catch (SQLException e) {
	// log.error("更新" + name + "出错sql:" + mergeSql);
	// log.error(e.getStackTrace());
	// e.printStackTrace();
	// flag = false;
	// } finally {
	// if (null != pstatement) {
	// try {
	// pstatement.close();
	// } catch (SQLException e) {
	// log.error("关闭pstatement失败");
	// log.error(e.getStackTrace());
	// }
	// }
	// }
	//
	// return flag;
	// }

	/**
	 * 
	 * @title 构建2Gci SELECT sql
	 * @param ncsNcellTabName
	 * @param groupBy
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:43:43
	 * @company 怡创科技
	 * @version 1.2
	 */
	private String get2GCiSql(String ncsNcellTabName, String groupBy) {
		/*
		 * return "select " + groupBy +
		 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(CI_DENOMINATOR) CI from "
		 * + ncsNcellTabName +
		 * " group by CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT HAVING SUM(CI_DENOMINATOR) >0"
		 * ;
		 */
		// @author chao.xj 2014-9-12 下午5:21:44 修改
		return "select "
				+ groupBy
				+ ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,CI_DIVIDER/CI_DENOMINATOR CI from "
				+ ncsNcellTabName + " where CI_DENOMINATOR >0";
	}

	/**
	 * 
	 * @title 构建2Gci MERGE sql
	 * @param targetTable
	 * @param selectSql
	 * @param factor
	 * @param groupBy
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:50:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	private String get2GMergeSql(String targetTable, String selectSql,
			String factor, String groupBy) {
		return "merge into " + targetTable + " targ using (" + selectSql
				+ ") temp on(targ.CELL=temp." + groupBy
				+ ") when matched then update set targ." + factor + "=temp."
				+ factor;
		// + " when not matched then insert (NCS_ID,CELL," + factor
		// + ") values(" + ncsDescId + ",temp." + groupBy + ",temp."
		// + factor + ")";
	}

	/**
	 * 
	 * @title 计算冗余覆盖指数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:55:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GRedundantCoverFacotr(Connection conn,
			String ncsNcellTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		float GSM900_FEQ_CNT = 95, GSM1800_FEQ_CNT = 124;
		/*
		 * if (threshold != null) { GSM900_FEQ_CNT =
		 * Float.parseFloat(threshold.getGsm900CellFreqNum()); GSM1800_FEQ_CNT =
		 * Float.parseFloat(threshold .getGsm1800CellFreqNum()); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("Gsm900CellFreqNum".toUpperCase())) {
					GSM900_FEQ_CNT = Float.parseFloat(val);
				}
				if (code.equals("Gsm1800CellFreqNum".toUpperCase())) {
					GSM1800_FEQ_CNT = Float.parseFloat(val);
				}
			}
		}
		// // 更新gsm900
		// boolean flag = calculate2GNfOrRcf(conn, ncsNcellTabName, cellResTab,
		// "NCELL", "CELL_FREQ_CNT", "REDUNT_COVER_FACT", "BCCH<100",
		// "网络结构指数(gsm900)", GSM900_FEQ_CNT);
		// if (!flag) {
		// return flag;
		// }
		// // 更新gsm1800
		// flag = calculate2GNfOrRcf(conn, ncsNcellTabName, cellResTab, "NCELL",
		// "CELL_FREQ_CNT", "REDUNT_COVER_FACT", "BCCH>100",
		// "网络结构指数(gsm1800)", GSM1800_FEQ_CNT);

		// --2014-8-26 补充
		boolean flag = true;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		String sql = "";
		/*
		 * sql = " SELECT CELL,VAL/((case WHEN CELL_FREQ_SECTION='GSM900' THEN "
		 * + GSM900_FEQ_CNT + " ELSE " + GSM1800_FEQ_CNT + " END)) val FROM " +
		 * " ( select Ncell AS CELL,sum((CI_DIVIDER/CI_DENOMINATOR) * NCELL_FREQ_CNT) AS VAL,min(NCELL_FREQ_SECTION) AS CELL_FREQ_SECTION "
		 * + " from " + ncsNcellTabName +
		 * " where CI_DENOMINATOR>0 group by Ncell)";
		 */
		// 2014-9-19 gmh修改
		sql = "select ncell,sum((CI_DIVIDER/CI_DENOMINATOR)*CELL_FREQ_CNT/(case WHEN CELL_FREQ_SECTION='GSM900' THEN "
				+ GSM900_FEQ_CNT
				+ " ELSE "
				+ GSM1800_FEQ_CNT
				+ " END)) val from "
				+ ncsNcellTabName
				+ " where CI_DENOMINATOR>=1 and CELL_INDOOR=NCELL_INDOOR AND NCELL_FREQ_SECTION=CELL_FREQ_SECTION group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on (tar.cell=src.ncell) when matched then update set tar.redunt_cover_fact=src.val";
		log.debug("更新冗余覆盖指数的sql=" + sql);
		/*
		 * String testsql="select Ncell AS CELL,"
		 * +" sum((CI_DIVIDER / CI_DENOMINATOR) * NCELL_FREQ_CNT) AS VAL,"
		 * +" min(NCELL_FREQ_SECTION) AS CELL_FREQ_SECTION"
		 * +" from RNO_2G_NCS_ANA_T" +" where CI_DENOMINATOR > 0"
		 * +"  group by Ncell"; printTmpTable1(testsql, stmt);
		 */

		try {
			long t1 = System.currentTimeMillis();
			int rows = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			log.debug("更新冗余覆盖指数影响行数：" + rows + ",耗时：" + (t2 - t1) + "ms");
		} catch (SQLException e) {
			log.error("更新冗余覆盖指数失败！sql=" + sql);
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		return flag;
	}

	/**
	 * 
	 * @title 计算2G重叠覆盖度
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午3:02:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GOverlapCoverFactor(Connection conn,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculate2GOverlapCoverFactor(Connection conn, long ncsId,String ncsNcellTabName)方法.conn:"
				+ conn + ", ncsNcellTabName:" + ncsNcellTabName);
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		// String factor = "OVERLAP_COVER";
		// String groupBy = "CELL";
		// String statistics = "sum(CI)+1";
		// String innerSelectSql = get2GCiSql(ncsNcellTabName, groupBy);
		// String selectSql = "select  rn." + groupBy + " " + groupBy + ","
		// + statistics + " " + factor + " from (" + innerSelectSql
		// + ") rn group by rn." + groupBy;
		// String mergeSql = get2GMergeSql(cellResTab, selectSql, factor,
		// groupBy);
		//
		// 2014-9-19 gmh 修改
		String selectSql = "select CELL,sum(CI_DIVIDER/CI_DENOMINATOR)+1 overlap from "
				+ ncsNcellTabName
				+ " where CELL_INDOOR=NCELL_INDOOR AND CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND CI_DENOMINATOR >=1 group by cell ";
		String mergeSql = "merge into " + cellResTab + " tar using ( "
				+ selectSql + " ) src on ( tar.cell=src.cell ) when "
				+ " matched then update set tar.OVERLAP_COVER=overlap";

		// log.debug("mergeSql:" + mergeSql);
		log.debug("重叠覆盖度mergeSql:" + mergeSql);
		PreparedStatement pStatement = null;
		boolean flag = false;
		try {
			pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			long t1 = System.currentTimeMillis();
			int rows = pStatement.executeUpdate();
			long t2 = System.currentTimeMillis();
			log.debug("计算重叠覆盖度，影响记录数：" + rows + ",耗时：" + (t2 - t1) + "ms");
			flag = true;
		} catch (SQLException e) {
			log.error("更新重叠覆盖度出错");
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * @title 计算2g小区检测次数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午3:07:12
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GDetectCnt(Connection conn,
			String ncsNcellTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		log.debug("conn:" + conn + ",  ncsNcellTabName:" + ncsNcellTabName);
		String testNumIdealCoverMutiple = "1.6", testNumLowestCi = "0.02";
		/*
		 * if (threshold != null) { testNumIdealCoverMutiple = threshold
		 * .getCellCheckTimesIdealDisMultiple(); testNumLowestCi = threshold
		 * .getCellCheckTimesSameFreqInterThreshold(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("CellCheckTimesIdealDisMultiple".toUpperCase())) {
					testNumIdealCoverMutiple = val;
				}
				if (code.equals("CellDetectCIThreshold".toUpperCase())) {// 小区检测次数的最低c/i门限
					testNumLowestCi = val;
				}
			}
		}
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("计算小区检测次数方法中，准备数据库statement出错！");
			e.printStackTrace();
			return false;
		}

		// ---包含室分小区的情况---//
		String sql = "select mid1.ncell,count(*) as cnt "
				+ " FROM ( select cell,ncell,distance from " + ncsNcellTabName
				+ " where (CI_DIVIDER/CI_DENOMINATOR)>" + testNumLowestCi
				+ " and distance<10000000000 "
				+ " group by cell,ncell,distance )mid1 "
				+ " inner join (select cell,EXPECTED_COVER_DIS from "
				+ cellResTab
				+ " )mid2 on(MID1.ncell=mid2.cell and MID1.distance>"
				+ testNumIdealCoverMutiple
				+ "*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";
		// @author chao.xj 2014-9-12 下午5:39:35 修改
		sql = "select mid1.ncell,count(*) as cnt "
				+ " FROM ( select cell,ncell,distance from " + ncsNcellTabName
				+ " where (CI_DIVIDER/CI_DENOMINATOR)>" + testNumLowestCi
				+ " and CI_DENOMINATOR>0 and distance<10000000000 "
				+ " group by cell,ncell,distance )mid1 "
				+ " inner join (select cell,EXPECTED_COVER_DIS from "
				+ cellResTab
				+ " )mid2 on(MID1.ncell=mid2.cell and MID1.distance>"
				+ testNumIdealCoverMutiple
				+ "*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";

		// 2014-9-21 gmh 修改
		// 2014-9-22 再增加条件：
		// 设定被评估小区为S，在NCS统计中能够检测到S小区的小区数为N，其中有M个小区（小区在S理想覆盖距离1.6倍以外同时小区方向沿S覆盖方位180度方向内），检测到S小区同时Cois>门限（默认为2%），则取定M为S小区的监测小区个数，监测小区个数越多则S的过覆盖嫌疑越大。
		String dir = " (case when CELL_TO_NCELL_DIR>=180 then CELL_TO_NCELL_DIR-180 else CELL_TO_NCELL_DIR+180 end) ";
		String low = "(case when " + dir + "<90 then 270+" + dir + " else "
				+ dir + "-90 end)";
		String high = "(case when " + dir + ">270 then " + dir + "-270 else "
				+ dir + "+90 end )";

		String lhedge = "FUN_RNO_ISBETWEENLOWANDHIGH(" + low + "," + high
				+ ",NCELL_BEARING)='y'";
		sql = "select ncell,count(ncell) cnt from  "
				+ " ( "
				+ " select t1.ncell,t1.cell,t1.distance from "
				+ ncsNcellTabName
				+ " t1,"
				+ cellResTab
				+ " t2 "
				+ " where (CI_DIVIDER/CI_DENOMINATOR)>"
				+ testNumLowestCi
				+ " and CI_DENOMINATOR>=1 and distance<10000000000 and t1.distance>t2.EXPECTED_COVER_DIS*"
				+ testNumIdealCoverMutiple
				// 小区方向沿S覆盖方位180度方向内
				+ " and " + lhedge + " and t1.cell=t2.cell "
				+ " ) group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.DETECT_CNT=src.cnt";

		int affectCnt = 0;
		log.debug("计算包含室分小区在内的小区检测数:sql=" + sql);
		try {
			long t1 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			log.debug("(包含室分小区)计算包含室分小区在内的小区检测数更新数量：" + affectCnt + ",耗时："
					+ (t2 - t1) + "ms");
			// log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>(包含室分小区)更新小区检测数后  开始打印"
			// + cellResTab + "表数据");
			// printTmpTable(cellResTab, stmt);
			// log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<结束打印" + cellResTab +
			// "表数据");
		} catch (SQLException e) {
			log.error("计算包含室分小区在内的小区检测数时出错！sql=" + sql);
			e.printStackTrace();
		}

		// -------不包含室分小区------------//
		sql = "select mid1.ncell,count(*) as cnt "
				+ " FROM ( select cell,ncell,distance from " + ncsNcellTabName
				+ " where  (CI_DIVIDER/CI_DENOMINATOR)>" + testNumLowestCi
				+ " and distance<10000000000 and CELL_INDOOR='N' "
				+ " group by cell,ncell,distance )mid1 "
				+ " inner join (select cell,EXPECTED_COVER_DIS from "
				+ cellResTab
				+ "  )mid2 on(MID1.ncell=mid2.cell and MID1.distance>"
				+ testNumIdealCoverMutiple
				+ "*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";
		// @author chao.xj 2014-9-12 下午5:44:21 修改
		// sql = "select mid1.ncell,count(*) as cnt "
		// + " FROM ( select cell,ncell,distance from " + ncsNcellTabName
		// + " where  (CI_DIVIDER/CI_DENOMINATOR)>" + testNumLowestCi
		// +
		// " and CI_DENOMINATOR>0 and distance<10000000000 and CELL_INDOOR='N' "
		// + " group by cell,ncell,distance )mid1 "
		// + " inner join (select cell,EXPECTED_COVER_DIS from "
		// + cellResTab
		// + "  )mid2 on(MID1.ncell=mid2.cell and MID1.distance>"
		// + testNumIdealCoverMutiple
		// + "*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";

		// 2014-9-21 gmh 修改
		sql = "select ncell,count(ncell) cnt from  "
				+ " ( "
				+ " select t1.ncell,t1.cell,t1.distance from "
				+ ncsNcellTabName
				+ " t1,"
				+ cellResTab
				+ " t2 "
				+ " where CELL_INDOOR='N' and (CI_DIVIDER/CI_DENOMINATOR)>"
				+ testNumLowestCi
				+ " and CI_DENOMINATOR>=1 and distance<10000000000 and t1.distance>t2.EXPECTED_COVER_DIS*"
				+ testNumIdealCoverMutiple + " and " + lhedge
				+ " and t1.cell=t2.cell " + " ) group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.DETECT_CNT_EXINDR=src.cnt";

		log.debug("计算排除室分小区在外的小区检测数:sql=" + sql);

		try {
			long t1 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			long t2 = System.currentTimeMillis();
			log.debug("(不包含室分小区)计算排除室分小区在外的小区检测数更新数量：" + affectCnt + ",耗时："
					+ (t2 - t1) + "ms");
			// log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>(不包含室分小区)更新小区检测数后  开始打印"
			// + cellResTab + "表数据");
			// printTmpTable(cellResTab, stmt);
			// log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<结束打印" + cellResTab +
			// "表数据");
		} catch (SQLException e) {
			log.debug("计算排除室分小区在外的小区检测数时出错！sql=" + sql);
			log.error("计算排除室分小区在外的小区检测数时出错！sql=" + sql);
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 
	 * @title 关联邻小区数 邻小区的NCS测量报告中，服务小区在邻小区测量报告中出现且信号强度差>-12dB的比例 ≥ 5%，
	 *        可认为该邻小区是主小区的关联邻小区。 需要计算2个结果：一个是包含室分小区，一个是不包含室分小区
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午3:30:36
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GStrongAssNcellCnt(Connection conn,
			String ncsNcellTabName, String cellResTab,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入方法：calculate2GStrongAssNcellCnt。conn=" + conn
				+ ",ncsNcellTabName=" + ncsNcellTabName + ",cellResTab");
		if (conn == null) {
			log.error("方法：calculate2GStrongAssNcellCnt的参数无效！");
			return false;
		}
		String strongCorrelCi = "0.03";
		/*
		 * if (threshold != null) { strongCorrelCi =
		 * threshold.getSameFreqInterThreshold(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("RELATIONNCELLCITHRESHOLD".toUpperCase())) {
					strongCorrelCi = val;
				}
			}
		}

		long t1 = System.currentTimeMillis();
		long t2 = System.currentTimeMillis();
		// String sql = "select NCELL,COUNT(*) AS CNT from " + ncsNcellTabName
		// + " where RNO_NCS_DESC_ID=" + ncsDescId
		// + " and INTERFER>0.05  GROUP BY NCELL";
		// 2014-7-13 gmh修正错误
		String sql = "select ncell,count(ncell) as CNT from ( "
				+ " select ncell,cell,sum(CI_DIVIDER)/sum(CI_DENOMINATOR) from "
				+ ncsNcellTabName
				+ " where cell_freq_section =ncell_freq_section group by ncell,cell "
				+ " having sum(CI_DIVIDER)/sum(CI_DENOMINATOR)>"
				+ strongCorrelCi + " " + ") " + " group by ncell";
		// @author chao.xj 2014-9-12 下午5:49:51 修改
		sql = "select ncell,count(ncell) as CNT from ( "
				+ " select ncell,cell from " + ncsNcellTabName
				+ " where cell_freq_section =ncell_freq_section and "
				+ " CI_DIVIDER/CI_DENOMINATOR>" + strongCorrelCi
				+ " and CI_DENOMINATOR>0 " + ") " + " group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.INTERFER_NCELL_CNT=src.cnt";

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("calculateStrongAssNcellCnt方法中，准备数据库statement出错！");
			e.printStackTrace();
			return false;
		}

		int affectCnt = 0;
		log.debug("计算包含室分小区在内的关联邻小区数:sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("计算包含室分小区在内的关联邻小区数更新数量：" + affectCnt + ",耗时：" + (t2 - t1)
					+ "ms");
		} catch (SQLException e) {
			log.error("计算包含室分小区在内的关联邻小区数时出错！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// -------不包含室分小区------------//
		// sql = "select NCELL,COUNT(*) AS CNT from " + ncsNcellTabName
		// + " where RNO_NCS_DESC_ID=" + ncsDescId
		// + " and INTERFER>0.05 and CELL_INDOOR='N' GROUP BY NCELL";

		// 2014-7-13 gmh修正错误
		sql = "select ncell,count(ncell) AS CNT from ( "
				+ " select ncell,cell,sum(CI_DIVIDER)/sum(CI_DENOMINATOR) from "
				+ ncsNcellTabName
				+ " where cell_freq_section =ncell_freq_section and CELL_INDOOR='N' "
				+ " group by ncell,cell "
				+ " having sum(CI_DIVIDER)/sum(CI_DENOMINATOR)>"
				+ strongCorrelCi + " " + ") " + " group by ncell";
		// @author chao.xj 2014-9-12 下午5:56:48 修改
		sql = "select ncell,count(ncell) AS CNT from ( "
				+ " select ncell,cell from "
				+ ncsNcellTabName
				+ " where cell_freq_section =ncell_freq_section and CELL_INDOOR='N' "
				+ " and CI_DIVIDER/CI_DENOMINATOR>" + strongCorrelCi
				+ " and CI_DENOMINATOR>0 " + ") " + " group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.INTERFER_NCE_CNT_EXINDR=src.cnt";
		log.debug("计算排除室分小区在外的关联邻小区数:sql=" + sql);
		try {
			t1 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("计算排除室分小区在外的关联邻小区数更新数量：" + affectCnt + ",耗时：" + (t2 - t1)
					+ "ms");
		} catch (SQLException e) {
			log.error("计算排除室分小区在外的关联邻小区数时出错！耗时：" + (t2 - t1) + "ms,sql=" + sql);
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/**
	 * 
	 * @title 小区覆盖分量
	 * @param conn
	 * @param ncsNcellTabName
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午3:47:29
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCellCover(Connection conn,
			String ncsNcellTabName, String clusterTab, String clusterCellTab,
			String cellResTab, List<RnoThreshold> rnoThresholds) {
		log.debug("conn:" + conn + ",ncsNcellTabName:" + ncsNcellTabName);
		String betweenCellIdealDisFactor = "1.6";
		/*
		 * if (threshold != null) { betweenCellIdealDisFactor = threshold
		 * .getBetweenCellIdealDisMultiple(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("BetweenCellIdealDisMultiple".toUpperCase())) {
					betweenCellIdealDisFactor = val;
				}
			}
		}
		PreparedStatement pStatement = null;
		boolean flag = false;
		StringBuffer selectSql = new StringBuffer();
		String mergeSql2 = "";
		try {
			String factor = "COVER_FACTOR";
			String groupBy = "NCELL";
			// 查询小区在簇内的覆盖系数
			// String cfSelectSql =
			// "select t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3."+groupBy+", max(t3.CELL_COVER_FACTOR) "+factor+" from (select rn.RNO_NCS_DESC_ID,rncc.CLUSTER_ID, rncc.CELL, rncc.NCELL, case when rn.DISTANCE/(1.6*rncar.EXPECTED_COVER_DIS)>= 1 then (rn.DISTANCE*rn.CI)/(1.6*rncar.EXPECTED_COVER_DIS) else 0 end CELL_COVER_FACTOR from (select RNO_NCS_DESC_ID,CELL,NCELL,sum(CI_DIVIDER)/sum(REPARFCN) CI,DISTANCE from RNO_NCS where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,DISTANCE) rn inner join (select ID,RNO_NCS_DESC_ID from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID = ? ) rnc on (rn.RNO_NCS_DESC_ID = rnc.RNO_NCS_DESC_ID) inner join (select t1.CLUSTER_ID CLUSTER_ID,t1.CELL CELL,t2.CELL NCELL from RNO_NCS_CLUSTER_CELL t1 inner join RNO_NCS_CLUSTER_CELL t2 on t1.CLUSTER_ID = t2.CLUSTER_ID where t1.CELL != t2.CELL order by t1.CLUSTER_ID) rncc on(rnc.ID = rncc.CLUSTER_ID and rn.NCELL = rncc.NCELL and rn.CELL=rncc.CELL) inner join ( select NCS_ID,CELL,EXPECTED_COVER_DIS from RNO_NCS_CELL_ANA_RESULT where NCS_ID=?) rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and rncc.NCELL = rncar.CELL)) t3 group by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3."+groupBy+" order by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID";
			selectSql.append("select t3.CLUSTER_ID,t3.");
			selectSql.append(groupBy);
			selectSql.append(", max(t3.CELL_COVER_FACTOR) ");
			selectSql.append(factor);
			selectSql
					.append(" from (select rncc.CLUSTER_ID, rncc.CELL, rncc.NCELL, (case when EXPECTED_COVER_DIS<>0 then (case when rn.DISTANCE/("
							+ betweenCellIdealDisFactor
							+ "*rncar.EXPECTED_COVER_DIS)>= 1 then (rn.DISTANCE*rn.CI)/("
							+ betweenCellIdealDisFactor
							+ "*rncar.EXPECTED_COVER_DIS) else 0 end) else null end) CELL_COVER_FACTOR from (  select CELL,NCELL,(case when reparfcn=0 then null else cd/REPARFCN end)as CI,DISTANCE FROM ( select CELL,NCELL,sum(CI_DIVIDER) as cd,sum(CI_DENOMINATOR) as reparfcn,DISTANCE from "
							+ ncsNcellTabName
							+ " group by CELL,NCELL,DISTANCE)) rn inner join (select ID from "
							+ clusterTab
							+ " ) rnc on (rn.ID = rnc.CLUSTER_ID) inner join (select t1.CLUSTER_ID CLUSTER_ID,t1.CELL CELL,t2.CELL NCELL from "
							+ clusterCellTab
							+ " t1 inner join "
							+ clusterCellTab
							+ " t2 on t1.CLUSTER_ID = t2.CLUSTER_ID where t1.CELL != t2.CELL order by t1.CLUSTER_ID) rncc on(rnc.ID = rncc.CLUSTER_ID and rn.NCELL = rncc.NCELL and rn.CELL=rncc.CELL) inner join ( select NCS_ID,CELL,EXPECTED_COVER_DIS from "
							+ cellResTab
							+ " ) rncar on (rncc.NCELL = rncar.CELL)) t3 group by t3.CLUSTER_ID,t3.");
			selectSql.append(groupBy);
			selectSql.append(" order by t3.CLUSTER_ID");
			// String cfMergeSql =
			// "merge into RNO_NCS_CLUSTER_CELL targ using ("+cfSelectSql+") temp on (targ.CLUSTER_ID = temp.CLUSTER_ID and targ.CELL = temp."+groupBy+") when matched then update set targ."+factor+" = temp."+factor+" when not matched then insert (CLUSTER_ID,CELL,"+factor+") values(temp.CLUSTER_ID,temp."+groupBy+",temp."+factor+")";
			// 更新小区在簇内的覆盖系数

			mergeSql2 = getMergeSql2(clusterCellTab, selectSql.toString(),
					factor, "CLUSTER_ID", "CLUSTER_ID", groupBy);
			// log.debug(mergeSql);
			log.debug("更新小区在簇内的覆盖系数的sql=" + mergeSql2);
			pStatement = conn.prepareStatement(mergeSql2);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.setLong(3, ncsDescId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			log.error("更新小区在簇内的覆盖系数失败sql:" + mergeSql2);
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);

		try {
			// 查询小区的簇覆盖破坏系数

			String factor = "COVER_DESTROY";
			String groupBy = "CLUSTER_ID";
			selectSql.append("select t1.");
			selectSql.append(groupBy);
			selectSql
					.append(",t1.CELL,case t2.SUM_COVER_FACTOR when 0 then 0 else t1.COVER_FACTOR/t2.SUM_COVER_FACTOR end ");
			selectSql.append(factor);
			selectSql.append(" from (");
			String innerSqlT1 = "select " + groupBy
					+ ",CELL,COVER_FACTOR from " + clusterCellTab + " where "
					+ groupBy + " in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSqlT1);
			selectSql.append(") t1 inner join (");
			String innerSqlT2 = "select " + groupBy
					+ ",sum(COVER_FACTOR) SUM_COVER_FACTOR from (select "
					+ groupBy + ",CELL,COVER_FACTOR from " + clusterCellTab
					+ " where " + groupBy + " in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSqlT2);
			selectSql.append(") group by " + groupBy + ") t2 on t1." + groupBy
					+ " = t2." + groupBy);
			// 更新小区的簇覆盖破坏系数

			String mergeSql = getMergeSql2(clusterCellTab,
					selectSql.toString(), factor, "CLUSTER_ID", "CLUSTER_ID",
					"CELL");
			log.debug("更新小区的簇覆盖破坏系数的sql=" + mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的簇覆盖破坏系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		try {
			String groupBy = "CELL";
			String factor = "CELL_COVER";
			String statistics = "case sum(t2.WEIGHT) when 0 then 0 else sum(t1.COVER_DESTROY*t2.WEIGHT)/sum(t2.WEIGHT) end";

			selectSql.append("select t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy + ",");
			selectSql.append(statistics);
			selectSql.append(" " + factor);
			selectSql.append(" from (");
			String innerSql1 = "select CLUSTER_ID, " + groupBy
					+ ", COVER_DESTROY from " + clusterCellTab
					+ " where CLUSTER_ID in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSql1);
			selectSql.append(") t1 inner join (");
			String innerSql2 = "select ID,RNO_NCS_DESC_ID,WEIGHT from "
					+ clusterTab + " where RNO_NCS_DESC_ID = ?";
			selectSql.append(innerSql2);
			selectSql
					.append(") t2 on (t1.CLUSTER_ID = t2.ID)  group by t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy);
			String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
					factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);
			// log.debug(mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的簇覆盖破坏系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		return flag;
	}

	private String getMergeSql2(String targetTable, String selectSql,
			String factor, String targField, String tempField, String groupBy) {
		StringBuffer mergeSql = new StringBuffer();
		mergeSql.append("merge into ").append(targetTable)
				.append(" targ using (").append(selectSql);
		mergeSql.append(") temp on (targ.").append(targField)
				.append(" = temp.").append(tempField)
				.append(" and targ.CELL = temp.").append(groupBy);
		mergeSql.append(") when matched then update set targ.").append(factor)
				.append(" = temp.").append(factor);
		// mergeSql.append(" when not matched then insert (CELL,")
		// .append(targField).append(",").append(factor)
		// .append(") values(").append("temp.").append(groupBy)
		// .append(",temp.").append(tempField).append(",temp.")
		// .append(factor).append(")");
		return mergeSql.toString();
	}

	private String getMergeSql3(String targetTable, String selectSql,
			String factor, String groupBy) {
		StringBuffer mergeSql = new StringBuffer();
		mergeSql.append("merge into ").append(targetTable)
				.append(" targ using (").append(selectSql);
		mergeSql.append(") temp on (")

		.append("  targ.CELL = temp.").append(groupBy);
		mergeSql.append(") when matched then update set targ.").append(factor)
				.append(" = temp.").append(factor);
		// mergeSql.append(" when not matched then insert (CELL,")
		// .append(targField).append(",").append(factor)
		// .append(") values(").append("temp.").append(groupBy)
		// .append(",temp.").append(tempField).append(",temp.")
		// .append(factor).append(")");
		return mergeSql.toString();
	}

	/**
	 * 
	 * @title 小区容量破坏分量计算
	 * @param conn
	 * @param ncsNcellTabName
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午3:57:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GCapacityDestroy(Connection conn,
			String ncsNcellTabName, String clusterTab, String clusterCellTab,
			String cellResTab, List<RnoThreshold> rnoThresholds) {
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		// String clusterCellTab = "RNO_NCS_CLUSTER_CELL";
		String gsm900CellIdealCapacity = "7", gsm1800CellIdealCapacity = "9";
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("Gsm900CellIdealCapacity".toUpperCase())) {
					gsm900CellIdealCapacity = val;
				}
				if (code.equals("Gsm1800CellIdealCapacity".toUpperCase())) {
					gsm1800CellIdealCapacity = val;
				}
			}
		}
		/*
		 * if (threshold != null) { gsm900CellIdealCapacity =
		 * threshold.getGsm900CellIdealCapacity(); gsm1800CellIdealCapacity =
		 * threshold.getGsm1800CellIdealCapacity(); }
		 */

		StringBuffer selectSql = new StringBuffer();
		boolean flag = false;
		Statement pStatement = null;
		// 更新小区理想容量
		String mergeSql1 = "";
		try {
			String groupBy = "CELL";
			String statistics = "case when c.BCCH<100 then "
					+ gsm900CellIdealCapacity + " when c.BCCH>100 then "
					+ gsm900CellIdealCapacity + " else null end ";
			String factor = "EXPECTED_CAPACITY";
			// String selectSqlString =
			// "select rn.RNO_NCS_DESC_ID, rn."+groupBy+", case when c.BCCH<100 then 7 when c.BCCH>100 then 9 else null end "+factor+" from (select RNO_NCS_DESC_ID, "+groupBy+" from RNO_NCS where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID, "+groupBy+") rn inner join CELL c on (rn."+groupBy+" = c.LABEL)";
			String innerSql = "select  " + groupBy + " from " + ncsNcellTabName
					+ " group by  " + groupBy;
			selectSql.append("select  rn.").append(groupBy + ",")
					.append(statistics + factor);
			selectSql
					.append(" from(")
					.append(innerSql)
					.append(") rn inner join "
							+ "( "
							+ " select * from ( "
							+ " select label,bcch,row_number() over(partition by label order by bcch) as seq from cell "
							+ " ) where seq=1  )  c on (rn.").append(groupBy)
					.append(" = c.LABEL)");
			mergeSql1 = getMergeSql3(cellResTab, selectSql.toString(), factor,
					groupBy);
			log.debug("更新小区理想容量sql: " + mergeSql1);
			pStatement = conn.createStatement();
			// pStatement.setLong(1, ncsDescId);
			// pStatement.executeUpdate();
			pStatement.executeUpdate(mergeSql1);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的理想容量失败sql:" + mergeSql1);
			e.printStackTrace();
			flag = false;
			// 将using的源表数据查询出来看看结果
			String tmpSql = "select rn.RNO_NCS_DESC_ID, rn.CELL,case when c.BCCH<100 then 7 when c.BCCH>100 then 9 else null end EXPECTED_CAPACITY from(select RNO_NCS_DESC_ID, CELL from "
					+ ncsNcellTabName
					+ " "

					+ " group by RNO_NCS_DESC_ID, CELL) rn inner join (  select * from (  select label,bcch,row_number() over(partition by label order by bcch) as seq from cell  ) where seq=1  )  c on (rn.CELL = c.LABEL)";
			List<Map<String, Object>> tmpRes = RnoHelper.commonQuery(
					pStatement, tmpSql);
			log.error("更新小区的理想容量失败时，源表的数据为：\n" + tmpRes);
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区容量系数
		try {

			String mergeSql = "UPDATE RNO_NCS_CELL_ANA_RESULT_MID SET CAPACITY_DESTROY_FACT=(CASE WHEN FREQ_CNT<EXPECTED_CAPACITY THEN 0 ELSE FREQ_CNT/EXPECTED_CAPACITY END)";
			log.debug("更新小区容量破坏系数mergeSql: " + mergeSql);
			pStatement = conn.createStatement();// conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.executeUpdate();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区容量系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区的簇容量破坏系数
		try {
			selectSql
					.append("merge INTO RNO_NCS_CLUSTER_CELL_MID targ USING ")
					.append("(SELECT dat1.cluster_id,")
					.append("dat1.cell,")
					.append("dat1.CAPACITY_DESTROY_FACT/(decode(dat2.WHOLE_CAPACITY_DESTROY_FACT,0,null,dat2.WHOLE_CAPACITY_DESTROY_FACT)) AS CAPACITY_DESTROY ")
					.append("FROM ")
					.append("(SELECT clscell2.cluster_id,")
					.append("clscell2.cell,")
					.append("cellres2.CAPACITY_DESTROY_FACT ")
					.append("FROM RNO_NCS_CLUSTER_CELL_MID clscell2 ")
					.append("INNER JOIN RNO_NCS_CELL_ANA_RESULT_MID cellres2 ")
					.append("ON(clscell2.cell=cellres2.cell) ")
					.append(")dat1 ")
					.append("INNER JOIN ")
					.append("(SELECT cluster_id, ")
					.append("SUM(CAPACITY_DESTROY_FACT) AS WHOLE_CAPACITY_DESTROY_FACT ")
					.append("FROM ")
					.append("(SELECT clscell.CLUSTER_ID,")
					.append("clscell.cell,")
					.append("cellres1.CAPACITY_DESTROY_FACT ")
					.append("FROM RNO_NCS_CLUSTER_CELL_MID clscell ")
					.append("INNER JOIN RNO_NCS_CELL_ANA_RESULT_MID cellres1 ")
					.append("ON(clscell.cell=cellres1.cell) ")
					.append(") ")
					.append("GROUP BY cluster_id ")
					.append(")dat2 ON(dat1.cluster_id=dat2.cluster_id) ")
					.append(")src ON(targ.cluster_id   =src.cluster_id AND targ.cell=src.cell) ")
					.append("WHEN matched THEN ")
					.append("UPDATE SET targ.CAPACITY_DESTROY=src.CAPACITY_DESTROY");
			String mergeSql = selectSql.toString();
			log.debug("更新小区的簇容量破坏系数mergeSql: " + mergeSql);
			// pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.setLong(3, ncsDescId);
			// pStatement.setLong(4, ncsDescId);
			// pStatement.executeUpdate();
			pStatement = conn.createStatement();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("小区的簇容量破坏系数失败");
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					e.printStackTrace();
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区容量破坏分量
		try {
			String groupBy = "CELL";
			String factor = "CAPACITY_DESTROY";
			String statistics = "case sum(t2.WEIGHT) when 0 then 0 else sum(t1.CAPACITY_DESTROY*t2.WEIGHT)/sum(t2.WEIGHT) end";

			selectSql.append("select t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy + ",");
			selectSql.append(statistics);
			selectSql.append(" " + factor);
			selectSql.append(" from (");
			String innerSql1 = "select CLUSTER_ID, " + groupBy
					+ ", CAPACITY_DESTROY from " + clusterCellTab
					+ " where CLUSTER_ID in (select ID from " + clusterTab
					+ " )";
			selectSql.append(innerSql1);
			selectSql.append(") t1 inner join (");
			String innerSql2 = "select ID,RNO_NCS_DESC_ID,WEIGHT from "
					+ clusterTab;
			selectSql.append(innerSql2);
			selectSql
					.append(") t2 on (t1.CLUSTER_ID = t2.ID)  group by t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy);
			String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
					factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);
			log.debug("更新小区容量破坏分量mergeSql: " + mergeSql);
			// pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.executeUpdate();
			pStatement = conn.createStatement();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区容量破坏分量失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		return flag;
	}

	
	/**
	 * 获取小区的概要信息进行缓存
	 * @param stmt
	 * @return
	 * @author brightming
	 * 2014-10-15 下午3:04:37
	 */
	private Map<String, CachedCellData> prepareCachedCellData(Statement stmt){
		String cell="";
		String cellSql = "select cell,name,cell_freq_section,bcch,cell_freq_cnt from rno_cell_city_t";
		Map<String, CachedCellData> cachedCellInfos = new HashMap<String, CachedCellData>();
		ResultSet rs=null;
		try {
			rs = stmt.executeQuery(cellSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			CachedCellData cachedCell = null;
			while (rs.next()) {
				cachedCell = new CachedCellData();
				cachedCell.setCell(rs.getString(1));
				cachedCell.setName(rs.getString(2));
				cachedCell.setFreqSection(rs.getString(3));
				cachedCell.setBcch(rs.getInt(4));
				cachedCell.setFreqCnt(rs.getInt(5));
				cachedCellInfos.put(rs.getString(1), cachedCell);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 2014-10-15 读取mrr表的数据，得到6,7级质量的数据
		String sql = "SELECT CELL,UL_QUAL_DETAIL,DL_QUAL_DETAIL FROM RNO_2G_MRR_ANA_T";
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String ulQual = "", dlQual = "";
			String[] quals = null;
			String[] valpart = null;
			CachedCellData cachedCell = null;
			while (rs.next()) {

				cell = rs.getString(1);
				if (cell == null || "".equals(cell.trim())) {
					continue;
				}
				cachedCell = cachedCellInfos.get(cell);
				if (cachedCell == null) {
					cachedCell = new CachedCellData();
					cachedCell.setCell(cell);
					cachedCellInfos.put(cell, cachedCell);
				}
				ulQual = rs.getString(2);
				dlQual = rs.getString(3);
				// 分解
				// 上行
				quals = ulQual.split(",");
				if (quals != null && quals.length == 8) {
					// 质量等级为0~7，共8个
					//取等级6
					try {
						valpart = quals[6].split("=");
						if (valpart != null && valpart.length == 2) {
							cachedCell.setUlQual6Cnt(Integer
									.parseInt(valpart[1]));
						}
					} catch (Exception e) {
					}
					//取等级7
					try {
						valpart = quals[7].split("=");
						if (valpart != null && valpart.length == 2) {
							cachedCell.setUlQual7Cnt(Integer
									.parseInt(valpart[1]));
						}
					} catch (Exception e) {
					}
				}
				
				// 下行
				quals = dlQual.split(",");
				if (quals != null && quals.length == 8) {
					// 质量等级为0~7，共8个
					//取等级6
					try {
						valpart = quals[6].split("=");
						if (valpart != null && valpart.length == 2) {
							cachedCell.setDlQual6Cnt(Integer
									.parseInt(valpart[1]));
						}
					} catch (Exception e) {
					}
					//取等级7
					try {
						valpart = quals[7].split("=");
						if (valpart != null && valpart.length == 2) {
							cachedCell.setDlQual7Cnt(Integer
									.parseInt(valpart[1]));
						}
					} catch (Exception e) {
					}
				}

			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		return cachedCellInfos;
	}
	
	
	/**
	 * 
	 * @title 计算最大连通簇 ncsTabName:邻区测量信息所在的表 targTable:计算得到的连通簇的存放表 threshold:门限值
	 * @param conn
	 * @param ncsTabName
	 * @param ncsIds
	 * @param clusterTable
	 * @param clusterCellTabName
	 * @param clusterCellRelaTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午4:22:06
	 * @company 怡创科技
	 * @version 1.2
	 */
	public ResultInfo calculate2GConnectedCluster(Connection conn,
			String ncsTabName, String clusterTable, String clusterCellTabName,
			String clusterCellRelaTab, List<RnoThreshold> rnoThresholds,
			RnoStructAnaJobRec jobRec,Map<String, CachedCellData> cachedCellInfos) {

		log.debug("进入方法：calculate2GConnectedCluster.connection=" + conn
				+ ",ncsTabName=" + ncsTabName + ",clusterTable=" + clusterTable
				+ ",clusterCellTabName=" + clusterCellTabName
				+ ",rnoThresholds=" + rnoThresholds);
		float threshold = 0.05f;
		/*
		 * if (thresholdObj != null) { threshold = Float.parseFloat(thresholdObj
		 * .getSameFreqInterThreshold()); }
		 */
		ResultInfo resultInfo=new ResultInfo(true);
		
		int gsm900FreqCnt = 94;
		int gsm1800FreqCnt = 124;

		String cityName = jobRec.getCityName();

		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("SameFreqInterThreshold".toUpperCase())) {
					threshold = Float.parseFloat(val);
				} else if ("GSM900CELLFREQNUM".equalsIgnoreCase(code)) {
					try {
						gsm900FreqCnt = Integer.parseInt(val);
					} catch (Exception e) {
						gsm900FreqCnt = 94;
					}
				} else if ("GSM1800CELLFREQNUM".equalsIgnoreCase(code)) {
					try {
						gsm1800FreqCnt = Integer.parseInt(val);
					} catch (Exception e) {
						gsm1800FreqCnt = 124;
					}
				}
			}
		}
		if (gsm900FreqCnt <= 0) {
			gsm900FreqCnt = 94;
		}
		if (gsm1800FreqCnt <= 0) {
			gsm1800FreqCnt = 124;
		}
		if (conn == null || ncsTabName == null || clusterTable == null
				|| "".equals(ncsTabName.trim())
				|| "".equals(clusterTable.trim())) {
			log.error("calculate2GConnectedCluster的参数不合法！connection=" + conn
					+ ",ncsTabName=" + ncsTabName + ",clusterTable="
					+ clusterTable + ",clusterCellTabName="
					+ clusterCellTabName + ",threshold=" + threshold);

			resultInfo.setFlag(false);
			return resultInfo;
		}
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			resultInfo.setFlag(false);
			return resultInfo;
		}
		String sql = "";
		ResultSet rs = null;
		String cell, ncell;
		double ci;

		long st = System.currentTimeMillis();
		long et;
		long t1, t2;

		t1 = System.currentTimeMillis();

		sql = "select cell,ncell from "
				+ ncsTabName
				+ " where CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND (CI_DIVIDER / CI_DENOMINATOR)>="
				+ threshold;
		// @author chao.xj 2014-9-29 下午12:02:12
		sql = "select cell,ncell,(CI_DIVIDER / CI_DENOMINATOR) ci from "
				+ ncsTabName
				+ " where CELL_INDOOR=NCELL_INDOOR AND CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND (CI_DIVIDER / CI_DENOMINATOR)>="
				+ threshold;
		log.debug(">>>>获取干扰度大于指定门限且排除室分小区的测量数据,sql=" + sql);
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		}

		// 用于计算省内连通簇的数据
		HashSet<String> initVertexs = new HashSet<String>();
		HashSet<Edge> edges = new HashSet<Edge>();
		Map<String, HashSet<String>> nodeToNeis = new HashMap<String, HashSet<String>>();
		// @author chao.xj 2014-9-29 上午11:56:19 集团标
		// 用于计算集团连通簇的数据
		HashSet<String> initVertexsGroup = new HashSet<String>();
		HashSet<Edge> edgesGroup = new HashSet<Edge>();
		Map<String, HashSet<String>> nodeToNeisGroup = new HashMap<String, HashSet<String>>();

		try {
			HashSet<String> neis = null;
			// @author chao.xj 2014-9-29 下午12:11:58
			HashSet<String> neisGroup = null;
			Edge oneEdge = null;
			while (rs.next()) {
				cell = rs.getString(1);
				ncell = rs.getString(2);
				ci = rs.getDouble(3);
				if (cell.equals(ncell)) {
					continue;
				}
				if (cell == null || ncell == null) {
					log.warn("邻区匹配不完整的数据！出现为空的情况！cell=" + cell + ",ncell="
							+ ncell);
					continue;
				}
				// 增加边
				oneEdge = new Edge(cell, ncell);
				edges.add(oneEdge);
				// 增加顶点
				initVertexs.add(cell);
				initVertexs.add(ncell);
				// 增加相邻关系
				if (!nodeToNeis.containsKey(cell)) {
					neis = new HashSet<String>();
					nodeToNeis.put(cell, neis);
				}
				neis = nodeToNeis.get(cell);
				neis.add(ncell);

				if (!nodeToNeis.containsKey(ncell)) {
					neis = new HashSet<String>();
					nodeToNeis.put(ncell, neis);
				}
				neis = nodeToNeis.get(ncell);
				neis.add(cell);
				// log.debug("==================中间数据输出开始====================");
				// if (ci > 0.02 && ci < 0.03) {
				// log.debug("cell=" + cell + ",ncell=" + ncell + ",ci=" + ci);
				// }
				// log.debug("==================中间数据输出结束====================");

				// @author chao.xj 2014-9-29 下午12:08:22 集团标
				if (ci >= 0.03) {
					// 增加边
					edgesGroup.add(oneEdge);
					// 增加顶点
					initVertexsGroup.add(cell);
					initVertexsGroup.add(ncell);
					// 增加相邻关系
					if (!nodeToNeisGroup.containsKey(cell)) {
						neisGroup = new HashSet<String>();
						nodeToNeisGroup.put(cell, neisGroup);
					}
					neisGroup = nodeToNeisGroup.get(cell);
					neisGroup.add(ncell);

					if (!nodeToNeisGroup.containsKey(ncell)) {
						neisGroup = new HashSet<String>();
						nodeToNeisGroup.put(ncell, neisGroup);
					}
					neisGroup = nodeToNeisGroup.get(ncell);
					neisGroup.add(cell);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resultInfo.setFlag(false);
			resultInfo.setAttach(null);
			return resultInfo;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		t2 = System.currentTimeMillis();
		log.debug("获取ncs对应的最小联通簇、整理小区所在的簇编号完成。共有:" + initVertexs.size()
				+ "小区数据，" + edges.size() + "条边。耗时：" + (t2 - t1) + "ms");

		// 开始合并最小簇为最大簇
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>" + cityName + " 开始计算省内最大联通簇.候选簇有：" + edges.size()
				+ " 个。");
		MaximalClique calculator = new MaximalClique();
		calculator.setEdges(edges);
		calculator.setInitVertexs(initVertexs);
		calculator.setNodeToNeis(nodeToNeis);
		List<Set<String>> clusters = calculator.calculateMaximalClique();
		edges = null;
		initVertexs = null;
		nodeToNeis = null;
		calculator = null;
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<" + cityName + " 计算完省内连通簇。耗时：" + (t2 - t1)
				+ "ms.簇个数：" + clusters.size());

		// @author chao.xj 2014-9-29 上午11:58:55 集团标
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>" + cityName + " 开始计算集团最大联通簇.候选簇有："
				+ edgesGroup.size() + " 个。");
		MaximalClique calculatorGroup = new MaximalClique();
		calculatorGroup.setEdges(edgesGroup);
		calculatorGroup.setInitVertexs(initVertexsGroup);
		calculatorGroup.setNodeToNeis(nodeToNeisGroup);
		List<Set<String>> clustersGroup = calculatorGroup
				.calculateMaximalClique();
		edgesGroup = null;
		initVertexsGroup = null;
		nodeToNeisGroup = null;
		calculatorGroup = null;
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<" + cityName + " 集团最大联通簇计算完成。耗时：" + (t2 - t1)
				+ "ms，簇个数：" + clustersGroup.size());

		t1 = t2;

		// 准备路径
		String tmpDir = jobRec.getTmpDir();
		String provFullPath = tmpDir + "/" + cityName + " 连通簇（省内标准）.csv";

		// 保存省内簇信息
		log.debug("开始输出省内连通簇信息。簇个数:" + clusters.size());
		t1 = System.currentTimeMillis();
		boolean ok = saveClusterInfo(gsm900FreqCnt, gsm1800FreqCnt, cachedCellInfos,
				clusters, provFullPath, cityName);
		t2 = System.currentTimeMillis();
		clusters = null;
		log.debug("完成省内标准文件,结果：" + ok + ",耗时：" + (t2 - t1) + "ms");

		// 保存集团内簇信息
		log.debug("开始输出集团内连通簇信息。簇个数:" + clustersGroup.size());
		String groupFilePath = tmpDir + "/" + cityName
				+ " 连通簇（集团标准）（大于等于0.03）.csv";
		t1 = System.currentTimeMillis();
		ok = saveClusterInfo(gsm900FreqCnt, gsm1800FreqCnt, cachedCellInfos,
				clustersGroup, groupFilePath, cityName);
		t2 = System.currentTimeMillis();
		clustersGroup = null;
		log.debug("完成集团生成标准文件,结果：" + ok + ",耗时：" + (t2 - t1) + "ms");

		et = System.currentTimeMillis();

		log.debug("<<<<<<<<<完成处理连通簇数据。耗时：" + (et - st) + "ms");
		
		return resultInfo;
	}

	// cell,name,cell_freq_section,bcch,cell_freq_cnt
	public class CachedCellData {
		String cell;
		String name;
		int bcch;
		String tch;
		String freqSection;

		int freqCnt = -1;

		int inClusterCnt = 0;// 属于几个簇
		int inBadClusterCnt = 0;// 属于几个问题簇

		int ulQual6Cnt = 0;// 上行质量6的数量
		int ulQual7Cnt = 0;
		int dlQual6Cnt = 0;// 下行质量6的数量
		int dlQual7Cnt = 0;

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getBcch() {
			return bcch;
		}

		public void setBcch(int bcch) {
			this.bcch = bcch;
		}

		public String getTch() {
			return tch;
		}

		public void setTch(String tch) {
			this.tch = tch;
		}

		public String getFreqSection() {
			return freqSection;
		}

		public void setFreqSection(String freqSection) {
			this.freqSection = freqSection;
		}

		public int getFreqCnt() {
			return freqCnt;
		}

		public void setFreqCnt(int freqCnt) {
			this.freqCnt = freqCnt;
		}

		public int getInClusterCnt() {
			return inClusterCnt;
		}

		public void setInClusterCnt(int inClusterCnt) {
			this.inClusterCnt = inClusterCnt;
		}

		public int getInBadClusterCnt() {
			return inBadClusterCnt;
		}

		public void setInBadClusterCnt(int inBadClusterCnt) {
			this.inBadClusterCnt = inBadClusterCnt;
		}

		public void increInClusterCnt() {
			inClusterCnt++;
		}

		public void increInBadClusterCnt() {
			inBadClusterCnt++;
		}

		public int getUlQual6Cnt() {
			return ulQual6Cnt;
		}

		public void setUlQual6Cnt(int ulQual6Cnt) {
			this.ulQual6Cnt = ulQual6Cnt;
		}

		public int getUlQual7Cnt() {
			return ulQual7Cnt;
		}

		public void setUlQual7Cnt(int ulQual7Cnt) {
			this.ulQual7Cnt = ulQual7Cnt;
		}

		public int getDlQual6Cnt() {
			return dlQual6Cnt;
		}

		public void setDlQual6Cnt(int dlQual6Cnt) {
			this.dlQual6Cnt = dlQual6Cnt;
		}

		public int getDlQual7Cnt() {
			return dlQual7Cnt;
		}

		public void setDlQual7Cnt(int dlQual7Cnt) {
			this.dlQual7Cnt = dlQual7Cnt;
		}

	}

	/**
	 * 按格式输出簇信息
	 * 
	 * @param gsm900FreqCnt
	 * @param gsm1800FreqCnt
	 * @param cellMap
	 * @param clusters
	 * @param fullPath
	 * @param bw
	 * @param cityName
	 * @return
	 * @throws IOException
	 * @author brightming 2014-9-29 下午3:14:34
	 */
	private boolean saveClusterInfo(int gsm900FreqCnt, int gsm1800FreqCnt,
			Map<String, CachedCellData> cellMap, List<Set<String>> clusters,
			String fullPath, String cityName) {
		Set<String> oneClusCells;
		long clusterId;
		CachedCellData cachedCell;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fullPath), "gbk"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		Date date = new Date();
		String dateStr = new DateUtil().format_yyyyMMdd(date);
		// 输出标题
		try {
			bw.write("时间,地市,簇ID,小区名,小区中文名,频段,区域类别,小区载频,簇内载波数（含BCCH）,簇tch载频数,可用tch频点数,簇约束因子,连通簇上行67级质量占比,连通簇下行67级质量占比");
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存连通簇标题头的时候出错！");
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		int cluTchFreqCnt = 0;// 簇内小区tch频点总数
		int cluAllFreqCnt = 0;// 簇内小区所有频点数量（包含bcch）
		String freqSection = "";// 频段类型
		boolean is900 = true;// 是否900频段
		int availFreqCnt = gsm900FreqCnt;// 某频段可用频点数
		int startClusterId = 1;
		boolean badCluster = false;
		int totalUlQual67Cnt=0;//总的上行质量为6，7的数量
		int totalDlQual67Cnt=0;//总的下行质量为6，7的数量
		List<CachedCellData> cluCellList = new ArrayList<CachedCellData>();
		
		for (int c = 0; c < clusters.size(); c++) {
			oneClusCells = clusters.get(c);
			// 清空
			clusterId = (startClusterId++);
			cluTchFreqCnt = 0;
			cluAllFreqCnt = 0;
			cluCellList.clear();
			freqSection = "";
			badCluster = false;
			totalUlQual67Cnt=0;
			totalDlQual67Cnt=0;

			// 时间 地市 簇ID 小区名 小区中文名 频段 区域类别 小区载频 簇内载波数（含BCCH） 簇tch载频数 可用tch频点数
			// 簇约束因子 连通簇上行67级质量占比 连通簇下行67级质量占比

			try {
				for (String cl : oneClusCells) {
					cachedCell = (CachedCellData) cellMap.get(cl);
					if (cachedCell == null) {
						cachedCell=new CachedCellData();
						cachedCell.setFreqSection("-");
						cachedCell.setFreqCnt(0);
						cachedCell.setUlQual6Cnt(0);
						cachedCell.setUlQual7Cnt(0);
						cachedCell.setDlQual6Cnt(0);
						cachedCell.setDlQual7Cnt(0);
						
						cellMap.put(cl, cachedCell);
//						continue;
					}
					
					freqSection = cachedCell.getFreqSection();
					cluTchFreqCnt += cachedCell.getFreqCnt() - 1;// 只要tch频点数
					cluAllFreqCnt += cachedCell.getFreqCnt();
					totalUlQual67Cnt+=cachedCell.getUlQual6Cnt()+cachedCell.getUlQual7Cnt();
					totalDlQual67Cnt+=cachedCell.getDlQual6Cnt()+cachedCell.getDlQual7Cnt();
					cluCellList.add(cachedCell);
				}
				if ("GSM900".equalsIgnoreCase(freqSection)) {
					is900 = true;
					availFreqCnt = gsm900FreqCnt;
				} else {
					is900 = false;
					availFreqCnt = gsm1800FreqCnt;
				}
				// 满足一定条件的才输出
				if (is900 && cluTchFreqCnt >= 36 || !is900
						&& cluTchFreqCnt >= 51) {
					if (is900 && cluAllFreqCnt >= 60 || !is900
							&& cluAllFreqCnt >= 80) {
						badCluster = true;
					}
					for (CachedCellData oneCell : cluCellList) {
						bw.write(dateStr + "," + cityName + "," + clusterId
								+ "," + oneCell.getCell() + ","
								+ oneCell.getName() + "," + freqSection + ",-,"
								+ (oneCell.getFreqCnt()) + ","
								+ (cluTchFreqCnt + oneClusCells.size()) + ","
								+ cluTchFreqCnt + "," + availFreqCnt + ","
								+ (cluTchFreqCnt * 1.0f / availFreqCnt)+","
								+(totalUlQual67Cnt>0?(oneCell.getUlQual6Cnt()+oneCell.getUlQual7Cnt())*1.0f/totalUlQual67Cnt:"NaN")+","
								+(totalDlQual67Cnt>0?(oneCell.getDlQual6Cnt()+oneCell.getDlQual7Cnt())*1.0f/totalDlQual67Cnt:"NaN")
								);
						bw.newLine();

						oneCell.increInClusterCnt();// 所属簇计数器加1
						// 问题簇
						if (badCluster) {
							oneCell.increInBadClusterCnt();
						}
					}

				}
				// 准备输出
			} catch (Exception e) {
				e.printStackTrace();
				log.error("保存连通簇数据时出错！");
			}
		}

		try {
			bw.flush();
			bw.close();
		} catch (Exception e) {

		}
		return true;
	}

	/**
	 * 
	 * @title 合并最小联通簇为最大联通簇
	 * @param clusterCells
	 * @param cellToClusterIds
	 * @param cellNamePairs
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午4:28:23
	 * @company 怡创科技
	 * @version 1.2
	 * 
	 *          private static List<List> mergeToLargest2GCluster(
	 *          List<List<String>> clusterCells, Map<String, List<Integer>>
	 *          cellToClusterIds, List<String> cellNamePairs) {
	 * 
	 *          // log.debug("clusterCells size =" + clusterCells.size() // +
	 *          " --> " + clusterCells); // log.debug("cellToClusterIds size ="
	 *          + // cellToClusterIds.size() // + " --> " + cellToClusterIds);
	 * 
	 *          log.debug("进入方法：mergeToLargest2GCluster.clusterCells大小：" +
	 *          clusterCells.size() + ",cellToClusterIds大小：" +
	 *          cellToClusterIds.size()); List<Set<String>> clusters = new
	 *          ArrayList<Set<String>>();// 簇内小区 // List<List<List<String>>>
	 *          resRelations = new // ArrayList<List<List<String>>>();
	 * 
	 *          long t1 = System.currentTimeMillis(); boolean alreadyIn =
	 *          false;// 是否已经被包含在某个最大联通簇内 Set<String> left = null; Set<String>
	 *          right = null; List<Integer> ids1 = null; List<Integer> ids2 =
	 *          null; String a = "", b = ""; // 对每个最小簇（只有2个元素进行处理） for (int i =
	 *          0; i < clusterCells.size(); i++) { List<String> cells =
	 *          clusterCells.get(i);// cells[0]是主小区，cells[1]是干扰小区 //
	 *          log.debug("检查关系："+cells); if (cells.size() != 2) { continue; }
	 *          alreadyIn = false; for (Set<String> big : clusters) { if
	 *          (big.containsAll(cells)) { alreadyIn = true; break; } } if
	 *          (alreadyIn) { // log.debug(cells + "已被包含，跳过"); continue; } //
	 *          开始计算 a = cells.get(0); b = cells.get(1); ids1 =
	 *          cellToClusterIds.get(a); ids2 = cellToClusterIds.get(b);
	 * 
	 *          List<List<String>> curRelas = null;// 当前分析的一对最小联通簇对应的干扰关系
	 *          List<String> re1 = null; curRelas = new
	 *          ArrayList<List<String>>(); if (cellNamePairs.contains(a + b)) {
	 *          re1 = new ArrayList<String>(); re1.add(a); re1.add(b);
	 *          curRelas.add(re1); } if (cellNamePairs.contains(b + a)) { re1 =
	 *          new ArrayList<String>(); re1.add(b); re1.add(a);
	 *          curRelas.add(re1); } if (ids1.size() == 1 || ids2.size() == 1) {
	 *          // a小区或b小区只被一个最小联通簇包含，那么这个组合不可能再与其他小区联合为更大的簇了 clusters.add(new
	 *          HashSet<String>(cells)); // 添加该簇对应的干扰关系 //
	 *          resRelations.add(curRelas); continue; }
	 * 
	 *          left = new HashSet<String>(); for (int ci : ids1) {
	 *          left.addAll(clusterCells.get(ci)); } right = new
	 *          HashSet<String>(); for (int ci : ids2) {
	 *          right.addAll(clusterCells.get(ci)); }
	 * 
	 *          // 求交集 left.retainAll(right); left.removeAll(cells);
	 * 
	 *          // 此时left集合中的cell，是与当前最小簇里的两个小区都相关的，但是，left里的小区，本身是否两两相关，需要进一步判断
	 *          List<String> forCal = new ArrayList<String>(left); int size =
	 *          forCal.size(); List<String> finalRes = new
	 *          ArrayList<String>();// 最终两两相关的 String c1, c2; boolean
	 *          isContained = false; // List<List<String>> eachPairExtend = new
	 *          // ArrayList<List<String>>(); List<String> maybe = null;
	 *          List<List<String>> maybes = null; List<List<String>> maybeRela =
	 *          null;// 每个可能分支对应的干扰关系 List<List<List<String>>> maybeRelas =
	 *          null;// 所有可能分支对应的干扰关系 if (size >= 1) {//
	 *          size最少都为2：即交集中起码包含上面的a小区，b小区 for (int k1 = 0; k1 < size; k1++) {
	 *          c1 = forCal.get(k1); if (a.equals(c1) || b.equals(c1)) {
	 *          continue; } // log.debug("检查"+forCal+"中的节点："+c1); //
	 *          需要找出最大可能相关的集合 // maybes是以c1为根的所有可能联通树 maybes = new
	 *          ArrayList<List<String>>(); maybeRelas = new
	 *          ArrayList<List<List<String>>>();// 每个簇都会对应若干对干扰关系 //
	 *          以C1为根构建树.当前只有一个分支，该分支只有一个节点：根节点 maybe = new ArrayList<String>();
	 *          maybe.add(c1); maybes.add(maybe);
	 * 
	 *          List<String> rl = new ArrayList<String>(); maybeRela = new
	 *          ArrayList<List<String>>(); List<List<String>> c1WithAb = new
	 *          ArrayList<List<String>>(); if (cellNamePairs.contains(a + c1)) {
	 *          rl = new ArrayList<String>(); rl.add(a); rl.add(c1);
	 *          c1WithAb.add(rl); } if (cellNamePairs.contains(c1 + a)) { rl =
	 *          new ArrayList<String>(); rl.add(c1); rl.add(a);
	 *          c1WithAb.add(rl); } if (cellNamePairs.contains(b + c1)) { rl =
	 *          new ArrayList<String>(); rl.add(b); rl.add(c1);
	 *          c1WithAb.add(rl); } if (cellNamePairs.contains(c1 + b)) { rl =
	 *          new ArrayList<String>(); rl.add(c1); rl.add(b);
	 *          c1WithAb.add(rl); } maybeRelas.add(new ArrayList(c1WithAb));
	 * 
	 *          for (int k2 = k1 + 1; k2 < size; k2++) { c2 = forCal.get(k2); if
	 *          (a.equals(c2) || b.equals(c2)) { continue; } //
	 *          log.debug("检查"+forCal+"中的节点："+c1+" 的后续节点:"+c2);
	 *          List<List<String>> relaWithC1C2 = new
	 *          ArrayList<List<String>>();// 干扰关系,对应一个分支 List<String> temp =
	 *          null; if (cellNamePairs.contains(c1 + c2)) { temp = new
	 *          ArrayList<String>(); temp.add(c1); temp.add(c2);
	 *          relaWithC1C2.add(temp); } if (cellNamePairs.contains(c2 + c1)) {
	 *          temp = new ArrayList<String>(); temp.add(c2); temp.add(c1);
	 *          relaWithC1C2.add(temp); }
	 * 
	 *          List<List<String>> relaWithAb = new ArrayList<List<String>>();//
	 *          干扰关系,对应一个分支 if (cellNamePairs.contains(a + c2)) { temp = new
	 *          ArrayList<String>(); temp.add(a); temp.add(c2);
	 *          relaWithAb.add(temp); } if (cellNamePairs.contains(c2 + a)) {
	 *          temp = new ArrayList<String>(); temp.add(c2); temp.add(a);
	 *          relaWithAb.add(temp); } if (cellNamePairs.contains(c2 + b)) {
	 *          temp = new ArrayList<String>(); temp.add(c2); temp.add(b);
	 *          relaWithAb.add(temp); } if (cellNamePairs.contains(b + c2)) {
	 *          temp = new ArrayList<String>(); temp.add(b); temp.add(c2);
	 *          relaWithAb.add(temp); } if (relaWithC1C2.size() > 0) { //
	 *          c1,c2有关系 int assCnt = 0; // 判断c1,c2这个组合，是否已经被包含在基于a，b小区计算出来的分支里面
	 *          // 所有分支都要算，每一个分支到时都可能会成为一个簇（除非已经被包含） List<List<String>>
	 *          tempRelaForCal = null; for (int mi = 0; mi < maybes.size();
	 *          mi++) { maybe = maybes.get(mi);//
	 *          取一个分支，每个分支包含至少2个小区（每一个分支都会对应一组干扰关系） maybeRela =
	 *          maybeRelas.get(mi);// 该分支对应的干扰关系 tempRelaForCal = new
	 *          ArrayList<List<String>>(); boolean ass = true; for (String mc :
	 *          maybe) {// 对应分支里的所有元素都检查是否与c2有关系 // 需要和里面的所有都关联 if
	 *          (cellNamePairs.contains(c2 + mc)) { tempRelaForCal.add(Arrays
	 *          .asList(c2, mc)); } if (cellNamePairs.contains(mc + c2)) {
	 *          tempRelaForCal.add(Arrays .asList(mc, c2)); } if
	 *          (!cellNamePairs.contains(c2 + mc) && !cellNamePairs.contains(mc
	 *          + c2)) { ass = false; break; } } // 检查完一个分支的情况 if (ass) { //
	 *          只有都关联，才能进入这个组 maybe.add(c2);// c2满足进入这个分支的条件
	 *          maybeRela.addAll(tempRelaForCal);// 该分支对应的干扰关系也增加
	 *          maybeRela.addAll(new ArrayList(relaWithAb));// 与a，b的关系 assCnt++;
	 *          } } // 所有分支检查完 if (assCnt == 0) { // 如果没有和任何一个组里的所有小区能两两关联，则自创一个
	 *          // 独立分支 maybe = new ArrayList<String>(); maybe.add(c1);
	 *          maybe.add(c2); maybes.add(maybe);
	 * 
	 *          // 干扰关系 maybeRela = new ArrayList<List<String>>();
	 *          maybeRela.addAll(new ArrayList(relaWithC1C2));// c1与c2之间的
	 *          maybeRela.addAll(new ArrayList(relaWithAb));// c2与a，b的关系
	 *          maybeRela.addAll(new ArrayList(c1WithAb));// c1与a，b之间的
	 *          maybeRelas.add(maybeRela); } } } //
	 *          判断maybe是否已经被eachPairExtend的某个包含 for (int ki = 0; ki <
	 *          maybes.size(); ki++) { // 由最小联通簇产生的所有分支树：maybes //
	 *          判断每个分支是否已经被需要返回的集合包含，如果没有被包含，则要加入该返回集合 maybe = maybes.get(ki);
	 *          maybe.addAll(cells);// 别忘了这个cells源头。 maybeRela =
	 *          maybeRelas.get(ki);// 该分支对应的干扰关系 maybeRela.addAll(curRelas);//
	 *          别忘了这个最小联通簇产生的关联关系。 for (Set<String> ext : clusters) { if
	 *          (ext.containsAll(maybe)) { isContained = true; break; } } if
	 *          (!isContained) { // 未被任何包含，加入 clusters.add(new
	 *          HashSet<String>(maybe)); // resRelations.add(maybeRela);// 干扰关系
	 *          } } // log.debug("检查得到："+maybes); } }
	 * 
	 *          } long t2 = System.currentTimeMillis();
	 *          log.debug("mergeToLargest2GCluster。耗时：" + (t2 - t1) + "ms"); //
	 *          log.debug("耗时：" + (t2 - t1) + "ms . cluster result=" // +
	 *          clusters);
	 * 
	 *          // Map<String,Object> res=new HashMap<String,Object>();
	 *          List<List> res = new ArrayList<List>(); res.add(clusters); //
	 *          res.add(resRelations); return res; }
	 * 
	 *          /**
	 * 
	 * @title 更新2g小区Mrr指标
	 * @param conn
	 * @param cellMrrAnaTab
	 *            rno_2g_mrr_ana_t
	 * @param cellResTab
	 *            rno_ncs_cell_ana_result_mid
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午6:00:50
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean update2GCellMrrIndex(Connection conn, String cellMrrAnaTab,
			String cellResTab) {
		log.debug("进入update2GCellMrrIndex conn,String cellMrrAnaTab,String cellResTab："
				+ conn + "----" + cellMrrAnaTab + "-----" + cellResTab);
		Statement stmt = null;
		String mergeSql = "";
		boolean flag = false;
		long t1 = System.currentTimeMillis();
		try {
			mergeSql = "merge into " + cellResTab + " t1				" + "using "
					+ cellMrrAnaTab + " t2                             "
					+ "on (t1.cell = t2.cell)                                "
					+ "when matched then                                     "
					+ "  update                                              "
					+ "     set t1.ul_quality       = t2.ul_quality,         "
					+ "         t1.dl_quality       = t2.dl_quality,         "
					+ "         t1.ul_poor_quality  = t2.ul_poor_quality,    "
					+ "         t1.dl_poor_quality  = t2.dl_poor_quality,    "
					+ "         t1.ul_coverage      = t2.ul_coverage,        "
					+ "         t1.dl_coverage      = t2.dl_coverage,        "
					+ "         t1.ul_poor_coverage = t2.ul_poor_coverage,   "
					+ "         t1.dl_poor_coverage = t2.dl_poor_coverage,   "
					+ "         t1.ta_average       = t2.ta_average          ";
			log.debug("更新小区mrr指标mergesql:" + mergeSql);
			stmt = conn.createStatement();
			stmt.executeUpdate(mergeSql);
			flag = true;
		} catch (SQLException e) {
			log.error("更新小区mrr指标失败,sql=" + mergeSql);
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭stmt失败");
					e.printStackTrace();
				}
			}
		}

		long t2 = System.currentTimeMillis();
		log.debug("更新小区mrr指标，结果" + flag + ",耗时:" + (t2 - t1) + "ms");
		return flag;
	}

	/**
	 * 
	 * @title 计算簇约束因子
	 * @param conn
	 * @param areaId
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15下午03:22:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GClusterConstrain(Statement stmt,
			String clusterTab, String clusterCellTab,
			List<RnoThreshold> rnoThresholds) {
		log.debug("calculate2GClusterConstrain(Connection conn,long areaId, String clusterTab, String clusterCellTab)"
				+ stmt + " " + clusterTab + " " + clusterCellTab + " ");
		float gsm900CellFreqNum = 95;
		float gsm1800CellFreqNum = 124;
		

		
		/*
		 * if (threshold != null) { gsm900CellFreqNum =
		 * Float.parseFloat(threshold .getGsm900CellFreqNum());
		 * gsm1800CellFreqNum = Float.parseFloat(threshold
		 * .getGsm1800CellFreqNum()); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("Gsm900CellFreqNum".toUpperCase())) {
					gsm900CellFreqNum = Float.parseFloat(val);
				}
				if (code.equals("Gsm1800CellFreqNum".toUpperCase())) {
					gsm1800CellFreqNum = Float.parseFloat(val);
				}
			}
		}
		boolean flag = false;
		// Map<Long, Boolean> ncsidtobool = new HashMap<Long, Boolean>();
		// for (int i = 0; i < ncsIds.size(); i++) {
		// String ncsidString = ncsIds.get(i).toString();
		// log.debug("ncsidString:"+ncsidString);
		// List<Long> clusteridList = this.getClusterIdsByNcsDescId(stmt,
		// Long.parseLong(ncsidString), clusterTab);
		String sql = "";
		// for (int j = 0; j < clusteridList.size(); j++) {
		// String clusteridString = clusteridList.get(j).toString();
		try {

			sql = "merge into "
					+ clusterTab
					+ " tar using "
					+ "("
					+ " SELECT mid1.cluster_id,mid1.totalcnt/mid2.section as factor  ,mid2.freq_type from"
					+ "("
					+ " select cluster_id,sum(freq_cnt) as totalcnt  from "
					+ clusterCellTab
					+ " where CLUSTER_ID in (select id from "
					+ clusterTab
					+ ") group by cluster_id"
					+ " )mid1"
					+ " inner JOIN "
					+ " ( "

					+ "select CLUSTER_ID,SECTION,FREQ_TYPE "
					+ " from "
					+ " ("
					+ " select cluster_id,section ,FREQ_TYPE,row_number() over(partition by cluster_id order by  count(*)  desc nulls last) as seq "// --count(*)
					+ " from "
					+ " ( "
					+ " select mid5.*,"
					+ "(case when cell.CELL_FREQ_SECTION='GSM1800' then "
					+ gsm1800CellFreqNum
					+ " else "
					+ gsm900CellFreqNum
					+ " end)"
					+ " as section ,"
					// +"(case when cell.bcch>100 then 'GSM1800' else 'GSM900' end) as FREQ_TYPE "
					+ "cell.CELL_FREQ_SECTION as FREQ_TYPE"
					+ " FROM "
					+ "( "
					+ " select cluster_id,cell from "
					+ clusterCellTab
					+ " where CLUSTER_ID in (select id from "
					+ clusterTab
					+ ") "
					+ " )mid5 "
					+ " inner join rno_cell_city_t cell on mid5.cell=cell.cell order by cluster_id "
					+ " ) "
					+ " GROUP BY cluster_id,section,FREQ_TYPE "
					+ " ) "
					+ " where seq=1 "
					+ " )mid2 "
					+ " on(mid1.cluster_id=mid2.cluster_id) "
					+ " ) src "
					+ " on (tar.id=src.cluster_id) when matched then update set tar.control_factor=SRC.factor,tar.FREQ_SECTION=src.freq_type";

			// @author chao.xj 2014-9-10 下午4:34:54 修改
			sql = "merge into "
					+ clusterTab
					+ " tar using "
					+ "("
					+ " SELECT mid1.cluster_id,mid1.totalcnt/mid2.section as factor  ,mid2.freq_type from"
					+ "("
					+ " select cluster_id,sum(freq_cnt) as totalcnt  from "
					+ clusterCellTab
					+ " rncc where exists (select 1 from "
					+ clusterTab
					+ "  rnc where rnc.id=rncc.CLUSTER_ID) group by cluster_id"
					+ " )mid1"
					+ " inner JOIN "
					+ " ( "

					+ "select CLUSTER_ID,SECTION,FREQ_TYPE "
					+ " from "
					+ " ("
					+ " select cluster_id,section ,FREQ_TYPE,row_number() over(partition by cluster_id order by  count(*)  desc nulls last) as seq "// --count(*)
					+ " from "
					+ " ( "
					+ " select mid5.*,"
					+ "(case when cell.CELL_FREQ_SECTION='GSM1800' then "
					+ gsm1800CellFreqNum
					+ " else "
					+ gsm900CellFreqNum
					+ " end)"
					+ " as section ,"
					// +"(case when cell.bcch>100 then 'GSM1800' else 'GSM900' end) as FREQ_TYPE "
					+ "cell.CELL_FREQ_SECTION as FREQ_TYPE"
					+ " FROM "
					+ "( "
					+ " select cluster_id,cell from "
					+ clusterCellTab
					+ " rncc where exists (select 1 from "
					+ clusterTab
					+ " rnc where rnc.id=rncc.CLUSTER_ID) "
					+ " )mid5 "
					+ " inner join rno_cell_city_t cell on mid5.cell=cell.cell order by cluster_id "
					+ " ) "
					+ " GROUP BY cluster_id,section,FREQ_TYPE "
					+ " ) "
					+ " where seq=1 "
					+ " )mid2 "
					+ " on(mid1.cluster_id=mid2.cluster_id) "
					+ " ) src "
					+ " on (tar.id=src.cluster_id) when matched then update set tar.control_factor=SRC.factor,tar.FREQ_SECTION=src.freq_type";
			log.debug("计算簇约束因子的sql=" + sql);
			stmt.executeUpdate(sql);
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("计算簇约束因子时出现错误的sql:" + sql);
			e.printStackTrace();
		} finally {
			// this.resourcesClose();
		}
		// }
		// }
		log.debug("退出calculate2GClusterConstrain flag:" + flag);

		return flag;
	}

	/**
	 * 
	 * @title 通过NCS描述ID取得簇ID集合信息
	 * @param stmt
	 * @param ncsId
	 * @param clusterTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-21下午2:28:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Long> getClusterIdsByNcsDescId(Statement stmt, String clusterTab) {
		log.debug("进入getClusterIdsByNcsDescId(final List<Long> ncsId)"
				+ " stmt:" + stmt);

		// TODO Auto-generated method stub
		List<Long> clusteridList = new ArrayList<Long>();
		// String
		// sql="SELECT ID from RNO_NCS_CLUSTER WHERE RNO_NCS_DESC_ID in("+ncsId+")";
		String sql = "SELECT ID from " + clusterTab;
		ResultSet rs = null;
		try {
			// ps = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				clusteridList.add(rs.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// this.resourcesClose();
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		log.debug("退出getClusterIdsByNcsDescId clusteridList:" + clusteridList);
		return clusteridList;

	}

	/**
	 * 
	 * @title 计算簇权重
	 * @param conn
	 * @param ncsTab
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 * @param stsIds
	 * @param forceRecalculte
	 *            true
	 * @param badClusterThreshold
	 *            簇约束因子的门限，大于0.5f此值，表示有问题，也就是需要计算的簇
	 * @return
	 * @author chao.xj
	 * @date 2014-8-21下午3:01:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculate2GNcsClusterWeight(Connection conn, String ncsTab,
			String clusterTab, String clusterCellTab, List<Long> stsIds,
			boolean forceRecalculte, float badClusterThreshold) {

		log.debug("进入方法：calculate2GNcsClusterWeight.conn=" + conn + ",ncsTab="
				+ ncsTab + ",,stsIds=" + stsIds + ",forceRecalculte="
				+ forceRecalculte);
		boolean flag = false;
		if (conn == null || ncsTab == null || "".equals(ncsTab.trim())
				|| stsIds == null || stsIds.isEmpty()) {
			log.error("方法calculate2GNcsClusterWeight的参数无效！不能为空！conn=" + conn
					+ ",ncsTab=" + ncsTab + ",stsIds=" + stsIds
					+ ",forceRecalculte=" + forceRecalculte);
			return flag;
		}

		long st = System.currentTimeMillis();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("calculate2GNcsClusterWeight方法中，准备statement时出错！");
			return flag;
		}

		// ------------获取ncs的详情-----------------//
		String sql = "";
		// List<Long> nIds = new ArrayList<Long>();

		// Map<Long, Boolean> result = new HashMap<Long, Boolean>();
		log.debug("开始处理ncs列表...");
		// ----------开始循环处理-----------//
		/*
		 * for (int i = 0; i < ncsIds.size(); i++) { long nid = ncsIds.get(i);
		 * log.debug("获取nid=" + nid + "下的簇信息");
		 */

		// 整个ncs的簇一起计算
		sql = "select cluster_id,ci_divider/reparfcn +ca_divider/reparfcn as WEIGHT FROM(select cluster_id,sum(ci_divider) as ci_divider,sum(ca_divider) as ca_divider,min(reparfcn) reparfcn FROM "
				+ " (select ncs.*,mid3.cluster_id FROM(select cell,ncell, ci_divider ,ca_divider,ci_denominator reparfcn from "
				+ ncsTab
				+ " where ci_denominator>0 ) ncs inner JOIN "
				+ " (select mid1.*,mid2.ncell from(select cluster_id,cell from "
				+ clusterCellTab
				+ " where cluster_id in(select id from "
				+ clusterTab
				+ " ))mid1 "
				+ " left JOIN(select cluster_id,cell as ncell from "
				+ clusterCellTab
				+ " where cluster_id in(select id from "
				+ clusterTab
				+ " ))mid2 "
				+ " ON ( mid1.cluster_id=mid2.cluster_id and mid1.cell<>mid2.ncell))mid3 on(ncs.cell=mid3.cell and ncs.ncell=mid3.ncell) order by MID3.cluster_id) group by cluster_id)";

		// @author chao.xj 2014-9-4 下午2:58:03 修改
		sql = "select cluster_id,ci_divider/reparfcn +ca_divider/reparfcn as WEIGHT FROM(select cluster_id,sum(ci_divider) as ci_divider,sum(ca_divider) as ca_divider,min(reparfcn) reparfcn FROM "
				+ " (select ncs.*,mid3.cluster_id FROM(select cell,ncell, ci_divider ,ca_divider,ci_denominator reparfcn from "
				+ ncsTab
				+ " where ci_denominator>0 ) ncs inner JOIN "
				+ " (select mid1.*,mid2.ncell from(select cluster_id,cell from "
				+ clusterCellTab
				+ " rncc where exists (select 1 from "
				+ clusterTab
				+ " rnc where rnc.id=rncc.cluster_id))mid1 "
				+ " left JOIN(select cluster_id,cell as ncell from "
				+ clusterCellTab
				+ " rncc where exists (select 1 from "
				+ clusterTab
				+ " rnc where rnc.id=rncc.cluster_id))mid2 "
				+ " ON ( mid1.cluster_id=mid2.cluster_id and mid1.cell<>mid2.ncell))mid3 on(ncs.cell=mid3.cell and ncs.ncell=mid3.ncell) order by MID3.cluster_id) group by cluster_id)";

		sql = "merge into "
				+ clusterTab
				+ " tar using ("
				+ sql
				+ " ) src on (tar.id=src.cluster_id) when matched then update set tar.WEIGHT=src.WEIGHT";
		log.debug("计算cluster表：" + clusterTab + " 的簇权重的sql：" + sql);
		try {
			stmt.executeUpdate(sql);
			flag = true;
		} catch (SQLException e) {
			log.error("计算簇权重进sql语句出错：" + sql);
			e.printStackTrace();
		}
		// }

		long et = System.currentTimeMillis();
		log.debug("退出方法：calculate2GNcsClusterWeight。耗时：" + (et - st)
				+ "ms。result=" + flag);
		return flag;
	}

	/**
	 * 
	 * @title 自动优化：挑选问题小区
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-8-21下午3:40:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean pick2GCellsWithProblem(Statement stmt, long cityId,
			List<RnoThreshold> rnoThresholds) {
		log.debug("进入方法：pickCellsWithProblem。stmt=" + stmt);
		String betweenCellIdealDisFactor = "1.6";
		/*
		 * if (threshold != null) { betweenCellIdealDisFactor = threshold
		 * .getBetweenCellIdealDisMultiple(); }
		 */
		if (rnoThresholds != null) {
			for (RnoThreshold rnoThreshold : rnoThresholds) {
				String code = rnoThreshold.getCode();
				String val = rnoThreshold.getDefaultVal();
				if (code.equals("BetweenCellIdealDisMultiple".toUpperCase())) {
					betweenCellIdealDisFactor = val;
				}
			}
		}
		String sql = "";
		int affectCnt = 0;
		long t1;
		long t2;
		// ---------关联邻小区数>20 问题类型1----------//
		String fields1 = "CELL";// ,EXPECTED_COVER_DIS,OVERSHOOTING_FACT,INTERFER_NCELL_CNT,INTERFER_NCE_CNT_EXINDR,CAPACITY_DESTROY_FACT";
		sql = "insert into RNO_NCS_PROBLEM_CELL_MID("
				+ fields1
				+ ",PROBLEM_TYPE,PROBLEM_DESC) select "
				+ fields1
				+ ",1,'[过多关联邻小区]' from RNO_NCS_CELL_ANA_RESULT_MID where INTERFER_NCELL_CNT>20";
		log.debug("筛选关联邻小区数大于20的问题小区。sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("筛选关联邻小区数大于20的问题小区。耗时：" + (t2 - t1) + "ms.影响数量为："
					+ affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选关联邻小区数大于20的问题小区时出错！sql=" + sql);
			return false;
		}

		// watchValue("--获取关联邻小区大于20后",stmt);

		// ---------------列出问题簇小区 问题类型2------------//
		// 找问题簇id
		sql = "select mid1.cluster_id "
				+ " FROM( "
				+ " select CLUSTER_ID,sum(FREQ_CNT) as cnt from RNO_NCS_CLUSTER_CELL_MID mid1 group by CLUSTER_ID "
				+ " )mid1 "
				+ " left join RNO_NCS_CLUSTER_MID mid2 "
				+ " on  "
				+ " mid1.cluster_id=mid2.id "
				+ " where  "
				+ " (cnt>60 and freq_section='GSM900' or cnt>80 and freq_section='GSM1800')";
		// 处于问题簇里的小区筛选出来
		sql = "SELECT distinct(CELL) FROM RNO_NCS_CLUSTER_CELL_MID WHERE CLUSTER_ID IN ("
				+ sql.toUpperCase() + ")";
		// 插入分析表
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID tar using ("
				+ sql
				+ ") SRC ON (tar.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+2-BITAND(TAR.PROBLEM_TYPE,2)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[问题簇小区]' WHEN NOT MATCHED THEN INSERT (CELL,PROBLEM_DESC) VALUES (SRC.CELL,'[问题簇小区]')";

		log.debug("筛选问题簇小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选问题簇小区。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选问题簇小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取问题簇小区后",stmt);
		// ------关键小区-----------------//
		// （1）覆盖分量top10，且关联邻小区数大于20。问题类型：4 ——肯定是在第一步找过覆盖小区时得到的小区的子集
		sql = "SELECT CELL FROM (SELECT CELL,avg(CELL_COVER) as CELL_COVER  FROM RNO_NCS_CELL_ANA_RESULT_MID WHERE ROWNUM<=10 group by CELL ORDER BY CELL_COVER DESC ) ";
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING ("
				+ sql
				+ ") SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+4-BITAND(TAR.PROBLEM_TYPE,4)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[覆盖分量排在TOP10]'";

		log.debug("筛选覆盖分量top10，且关联邻小区数大于20的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选覆盖分量top10，且关联邻小区数大于20的小区。耗时：" + (t2 - t1)
					+ "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选覆盖分量top10，且关联邻小区数大于20的小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取覆盖分量top10小区后",stmt);
		// （2）小区容量分量TOP10% 问题类型：8
		sql = "SELECT CELL FROM (SELECT CELL, avg(CAPACITY_DESTROY) as CAPACITY_DESTROY  FROM RNO_NCS_CELL_ANA_RESULT_MID WHERE ROWNUM<=10 group by cell ORDER BY CAPACITY_DESTROY DESC ) ";
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING ("
				+ sql
				+ ") SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+8-BITAND(TAR.PROBLEM_TYPE,8)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[容量分量排在TOP10]'";

		log.debug("筛选容量分量top10的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选容量分量top10的小区。耗时：" + (t2 - t1) + "ms。影响数量为："
					+ affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选容量分量top10的小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取容量分量top10小区后",stmt);

		// -------匹配小区基本信息----//
		// 包括：中文名、小区经度、小区纬度、天线挂高、天线下倾、是否室分、垂直半功率角、频段
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING (SELECT * FROM (SELECT LABEL,NAME,LONGITUDE,LATITUDE,ANT_HEIGH,DOWNTILT,INDOOR_CELL_TYPE,ANT_HALF_POWER,(CASE WHEN BCCH<100 THEN 'GSM900' WHEN BCCH>100 THEN 'GSM1800' END) FREQ_SECTION,row_number() over(partition by label order by bcch nulls last) as seq FROM CELL) WHERE SEQ=1) SRC ON (TAR.CELL=SRC.LABEL) WHEN MATCHED THEN UPDATE SET TAR.CELLNAME=SRC.NAME,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.ANT_HEIGHT=SRC.ANT_HEIGH,TAR.DOWNTILT=SRC.DOWNTILT,TAR.INDOOR_CELL_TYPE=SRC.INDOOR_CELL_TYPE,TAR.ANT_HALF_POWER=SRC.ANT_HALF_POWER,TAR.FREQ_SECTION=SRC.FREQ_SECTION";
		log.debug("匹配小区基本信息，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("匹配小区基本信息。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("匹配小区基本信息时出错！sql=" + sql);
			return false;
		}

		// ---------匹配小区的结构分析结果---//
		// 包括：理想覆盖距离、过覆盖系数、关联邻小区数、不含室分的关联邻小区数、容量分量、覆盖分量
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING RNO_NCS_CELL_ANA_RESULT_MID SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.EXPECTED_COVER_DIS=SRC.EXPECTED_COVER_DIS,TAR.OVERSHOOTING_FACT=SRC.OVERSHOOTING_FACT,TAR.INTERFER_NCELL_CNT=SRC.INTERFER_NCELL_CNT,TAR.INTERFER_NCE_CNT_EXINDR=SRC.INTERFER_NCE_CNT_EXINDR,TAR.CAPACITY_DESTROY=SRC.CAPACITY_DESTROY,TAR.CELL_COVER=SRC.CELL_COVER";
		log.debug("匹配小区的结构分析结果，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("匹配小区的结构分析结果。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("匹配小区的结构分析结果时出错！sql=" + sql);
			return false;
		}

		// ---------计算理想覆盖下倾----//
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID SET IDEAL_DOWNTILT=ATAN(ANT_HEIGHT/EXPECTED_COVER_DIS*"
				+ betweenCellIdealDisFactor
				+ ")+ANT_HALF_POWER/2 WHERE EXPECTED_COVER_DIS>0";
		log.debug("计算理想下倾角，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算理想下倾角。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算理想下倾角时出错！sql=" + sql);
			return false;
		}

		// -------补充话务情况-----//
		// ----需要最近一周的最忙时的话务----//
		// ---找各个小区最近的话务时间，再减去7天作为时间段。如果要准确匹配，必须要及时导入话统信息---//
		// String
		// selectCell="select distinct(cell) from (SELECT distinct(cell) FROM rno_ncs_mid UNION 	SELECT distinct(ncell) FROM rno_ncs_mid)	where UPPER(cell) not like 'NOT%' ".toUpperCase();

		// 找出所有的区域的最新话统时间
		// 得到各区域的最新话统时间到减去一周的时间范围内，所有的话统描述id。字段并没有指定含area_id
		sql = "SELECT distinct(des.sts_desc_id) as sts_desc_id "
				+ " FROM rno_sts_descriptor des "
				+ " LEFT JOIN "
				+ " (SELECT AREA_ID, "
				+ " MAX(STS_DATE) latest_DATE, "
				+ " MAX(STS_DATE)-7 AS early_date "
				+ " FROM RNO_STS_DESCRIPTOR "
				+ " WHERE AREA_ID IN "
				+ " (SELECT AREA_ID FROM SYS_AREA WHERE PARENT_ID="
				+ cityId
				+ " ) "
				+ " GROUP BY AREA_ID "
				+ " ) mid "
				+ " ON (des.area_id=mid.area_id "
				+ " AND des.sts_date BETWEEN mid.early_date AND mid.latest_date)";

		// 转移到话统临时表
		sql = "INSERT INTO RNO_TEMP_STS " + " ( " + " STS_ID, "
				+ " DESCRIPTOR_ID, " + " CELL, " + " DECLARE_CHANNEL_NUMBER, "
				+ " AVAILABLE_CHANNEL_NUMBER, " + " CARRIER_NUMBER, "
				+ " RESOURCE_USE_RATE, " + " TRAFFIC " + " ) "
				+ " SELECT STS_ID, " + " DESCRIPTOR_ID, " + " CELL, "
				+ " DECLARE_CHANNEL_NUMBER, " + " AVAILABLE_CHANNEL_NUMBER, "
				+ " CARRIER_NUMBER, " + " RESOURCE_USE_RATE, " + " TRAFFIC "
				+ " FROM rno_sts " + " WHERE DESCRIPTOR_ID IN " + " ( " + sql
				+ " ) ";

		log.debug("转移合适范围的小区话统数据到话统临时表，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("转移合适范围的小区话统数据到话统临时表，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("转移合适范围的小区话统数据到话统临时表时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// 只保留每个小区的最大无线资源利用率记录
		sql = "merge INTO RNO_TEMP_STS tar USING "
				+ " (SELECT cell, "
				+ " MAX(RESOURCE_USE_RATE) AS RESOURCE_USE_RATE "
				+ " FROM RNO_TEMP_STS "
				+ " GROUP BY cell "
				+ " )src ON (tar.cell=src.cell ) "
				+ " WHEN matched THEN "
				+ " UPDATE SET tar.NET_TYPE=NULL "
				+ " DELETE WHERE (tar.RESOURCE_USE_RATE<src.RESOURCE_USE_RATE) ";
		log.debug("保留话统临时表的最大无线资源利用率，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("保留话统临时表的最大无线资源利用率，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("保留话统临时表的最大无线资源利用率时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// 去除话统临时表的重复的小区
		sql = " merge INTO RNO_TEMP_STS tar USING "
				+ " (SELECT CELL, "
				+ " STS_ID "
				+ " FROM "
				+ " (SELECT cell, "
				+ " sts_id, "
				+ " row_number() over(partition BY cell order by sts_id) AS seq "
				+ " FROM RNO_TEMP_STS " + " ) " + " WHERE SEQ        =1 "
				+ " ) src ON(tar.cell=src.cell) " + " WHEN matched THEN "
				+ " UPDATE SET tar.NET_TYPE=NULL "
				+ " DELETE WHERE (tar.sts_id<>src.sts_id) ";
		log.debug("去除话统临时表的重复的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("去除话统临时表的重复的小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("去除话统临时表的重复的小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// --更新问题小区的话统数据---//
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING RNO_TEMP_STS SRC ON(TAR.CELL=SRC.CELL) "
				+ " WHEN MATCHED THEN "
				+ " UPDATE "
				+ " SET TAR.DECLARE_CHANNEL_NUMBER=SRC.DECLARE_CHANNEL_NUMBER, "
				+ " TAR.AVAILABLE_CHANNEL_NUMBER=SRC.AVAILABLE_CHANNEL_NUMBER, "
				+ " TAR.CARRIER_NUMBER          =SRC.CARRIER_NUMBER, "
				+ " TAR.RESOURCE_USE_RATE       =SRC.RESOURCE_USE_RATE, "
				+ " TAR.TRAFFIC                 =SRC.TRAFFIC ";
		log.debug("更新问题小区的话统数据，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新问题小区的话统数据，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("更新问题小区的话统数据时出错了！sql=" + sql);
			e.printStackTrace();
			// 看rno_temp_sts的表的内容
			sql = "select cell,count(*) from RNO_TEMP_STS group by cell having count(*)>1";
			List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
			for (Map<String, Object> one : res) {
				log.error("----输出RNO_TEMP_STS的信息：");
				for (String k : one.keySet()) {
					log.error(k + "=" + one.get(k) + ",");
				}
			}
			return false;
		}

		// ---计算问题小区六强关联邻区的话务情况----//
		sql = "merge INTO RNO_NCS_PROBLEM_CELL_MID tar USING "
				+ " (SELECT cell, "
				+ " AVG(resource_use_rate) AS AVG_NCELL_USE_RATE "
				+ " FROM "
				+ " (SELECT mid1.cell, "
				+ " mid1.ncell, "
				+ " mid2.RESOURCE_USE_RATE "
				+ " FROM "
				+ " (SELECT ncell AS cell, "
				+ " cell        AS ncell "
				+ " FROM "
				+ " (SELECT ncell, "
				+ " cell, "
				+ " interfer , "
				+ " row_number () over (partition BY ncell order by interfer DESC) rn "
				+ " FROM rno_ncs_mid " + " ) " + " WHERE rn  <=6 "
				+ " AND ncell IN "
				+ " (SELECT cell FROM RNO_NCS_PROBLEM_CELL_MID " + " ) "
				+ " )mid1 " + " LEFT JOIN rno_temp_sts mid2 "
				+ " ON(mid1.ncell=mid2.cell) " + " ) " + " GROUP BY cell "
				+ " )src ON (tar.cell=src.cell) " + " WHEN matched THEN "
				+ " UPDATE SET tar.AVG_NCELL_USE_RATE=src.AVG_NCELL_USE_RATE ";
		log.debug("计算问题小区六强关联邻区的话务情况，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算问题小区六强关联邻区的话务情况，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("计算问题小区六强关联邻区的话务情况时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// ---生成优化建议---//
		// 1、下倾角调整建议
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE=" + " CASE "
				+ " WHEN DOWNTILT<IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[调整为理想覆盖下倾]' "
				+ " WHEN DOWNTILT>IDEAL_DOWNTILT "
				+ " AND DOWNTILT<=2*IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[实际下倾加2~5度]' "
				+ " WHEN DOWNTILT>2*IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[检查或更换天线]' " + " END";
		log.debug("生成下倾角调整建议，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("生成下倾角调整建议，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("生成下倾角调整建议时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}
		// ------查看效果---//
		// sql="select cell,DOWNTILT,IDEAL_DOWNTILT,MESSAGE FROM RNO_NCS_PROBLEM_CELL_MID WHERE DOWNTILT>IDEAL_DOWNTILT "
		// + " AND DOWNTILT<=2*IDEAL_DOWNTILT";
		// List<Map<String, Object>> res;
		// Object v;
		// watchValue("--查看下倾角调整建议后",stmt);

		// 2、根据话务情况产生建议
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE= " + " CASE "
				+ " WHEN RESOURCE_USE_RATE<1 " + " THEN MESSAGE "
				+ " ||'[减容]' " + " WHEN (FREQ_SECTION     ='GSM900' "
				+ " AND CARRIER_NUMBER     >7 "
				+ " OR FREQ_SECTION        ='GSM1800' "
				+ " AND CARRIER_NUMBER     >9) "
				+ " AND AVG_NCELL_USE_RATE<=1.4 " + " THEN MESSAGE "
				+ " ||'[话务均衡，减容]' " + " WHEN (FREQ_SECTION    ='GSM900' "
				+ " AND CARRIER_NUMBER    >7 "
				+ " OR FREQ_SECTION       ='GSM1800' "
				+ " AND CARRIER_NUMBER    >9) "
				+ " AND AVG_NCELL_USE_RATE>1.4 " + " THEN MESSAGE "
				+ " ||'[加向或新建站，分流话务]'" + " ELSE MESSAGE" + " END";
		log.debug("调整高话务小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("调整高话务小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("调整高话务小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// watchValue("--调整高话务小区建议后",stmt);
		// 3、高站且高配小区
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE=MESSAGE "
				+ " ||'[制定容量下沉方案]' " + " WHERE ANT_HEIGHT    >50 "
				+ " AND (FREQ_SECTION  ='GSM900' " + " AND CARRIER_NUMBER >7 "
				+ " OR FREQ_SECTION    ='GSM1800' " + " AND CARRIER_NUMBER >9)";
		log.debug("调整高站且高配小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("调整高站且高配小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("调整高站且高配小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// ---更新使用的话统时间段---//

		// watchValue("--全部结束后",stmt);

		return true;
	}

	/**
	 * 输出结构分析的结果
	 * 
	 * 输出两份文件： 1、供用户下载的压缩包 包含：结构指标、连通簇、簇内小区、簇内干扰
	 * 在临时工作目录，先输出到各自的文件，然后进行压缩，最后将压缩包转移到指定地方 中间有任何一个输出错误都不要中断，尽量输出多点内容。
	 * 2、供程序读取数据的文件 在临时工作目录，先输出，然后再转移到指定地方
	 * 
	 * @param stmt
	 * @param jobRec
	 * @return
	 * @author brightming 2014-8-26 上午11:17:23
	 */
	private ResultInfo saveStructAnaResult(Statement stmt,
			RnoStructAnaJobRec jobRec,Map<String, CachedCellData> cachedCellInfos) {

		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setFlag(true);

		boolean ok;
		// ---------输出结果------//
		log.debug("准备输出结构分析结果...");

		String tmpDir = jobRec.getTmpDir();
		String cityName = jobRec.getCityName();

		// 输出过覆盖信息
		outputOverShootingCellDetails(stmt, jobRec);

		try {
			// 只读文件的临时存储
			String rdFileName = jobRec.getReadByProgFileName() == null ? jobRec
					.getJobId() + "_rdFile.ro" : jobRec.getReadByProgFileName();
			String tmpRdFilePath = tmpDir + rdFileName;
			BufferedWriter bw = null;
			try {
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(tmpRdFilePath), "utf-8");
				bw = new BufferedWriter(write);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 供用户下载的文件的临时存储
			String tmpCellresPath = tmpDir + "/" + cityName + " 小区结构指标.csv";
			// String tmpClusterPath = tmpDir + "连通簇信息.csv";

			// ---------------------------------------------//
			// 输出结构指标数据
			resultInfo = outputCellStructResult(stmt, tmpCellresPath, bw,cachedCellInfos);
			long cellresLineCnt = (Long) resultInfo.getAttach();

			// 输出连通簇数据
			// resultInfo = outputClusterResult(stmt, tmpClusterPath, bw);
			long clustLineCnt = (Long) resultInfo.getAttach();

			// /关闭输出流
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 将用户文件进行压缩，并转移到最终目录
			String realUserDataName = jobRec.getDownLoadFileName();
			if (StringUtils.isBlank(realUserDataName)) {
				realUserDataName = jobRec.getCityName() + "_结构分析指标.zip";
			}
			String realDataDir = jobRec.getResultDir();
			if (!StringUtils.endsWith(realDataDir, "/")
					&& !StringUtils.endsWith(realDataDir, "\\")) {
				realDataDir += "/";
			}
			String realUserDataFullPath = realDataDir + realUserDataName;
			log.debug("结构分析结果供用户下载的结果的最终保存路径：" + realUserDataFullPath);

			ok = ZipFileHandler.zip(tmpDir, "**" + rdFileName,
					realUserDataFullPath);
			log.debug("结构分析结果供用户下载的结果保存情况：" + ok);

			// 将只读文件进行整改，在第一行输出指示哪段内容在哪行的信息
			String realRdFileFullPath = realDataDir + rdFileName;
			try {
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(realRdFileFullPath), "utf-8");
				bw = new BufferedWriter(write);
			} catch (Exception e) {
				log.error("准备转移结构分析的只读结果时，准备输出流失败！");
				e.printStackTrace();

			}
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(tmpRdFilePath), "utf-8"));
			} catch (Exception e) {
				log.error("准备转移结构分析的只读结果时，准备输入流失败！");
				e.printStackTrace();
			}

			// 写
			try {
				bw.write("sep=##,cellres=2-" + (cellresLineCnt + 1)
						+ ",cluster=" + (cellresLineCnt + 2) + "-"
						+ (cellresLineCnt + 2 + clustLineCnt - 1));
				bw.newLine();
				String line = "";
				while ((line = br.readLine()) != null) {
					bw.write(line);
					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 删除临时目录
			try {
				FileTool.deleteDir(tmpDir);
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			// System.gc();
		}
		return resultInfo;

	}

	
	class OutputDbDataFormat{
		String fromTable="";
		String fieldSql="";
		String fieldCode="";
		String outputTitle="";
		public String getFieldSql() {
			return fieldSql;
		}
		public void setFieldSql(String fieldSql) {
			this.fieldSql = fieldSql;
		}
		public String getFieldCode() {
			return fieldCode;
		}
		public void setFieldCode(String fieldCode) {
			this.fieldCode = fieldCode;
		}
		public String getOutputTitle() {
			return outputTitle;
		}
		public void setOutputTitle(String outputTitle) {
			this.outputTitle = outputTitle;
		}
		public String getFromTable() {
			return fromTable;
		}
		public void setFromTable(String fromTable) {
			this.fromTable = fromTable;
		}
		
	}
	
	/**
	 * 读取数据的输出格式
	 * @return
	 * @author brightming
	 * 2014-10-15 下午3:36:53
	 */
	public List<OutputDbDataFormat> readDbOutputFormatFromXml(String fileName) {
		List<OutputDbDataFormat> dbCfgs = new ArrayList<OutputDbDataFormat>();
		InputStream in=null;
		try {
			in = new FileInputStream(new File(
					RnoStructureAnalysisDaoImplV2.class.getResource(
							fileName).getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
				Element e = (Element) o;
				OutputDbDataFormat dt = new OutputDbDataFormat();
				for (Object obj : e.elements()) {
					Element e1 = (Element) obj;
					String key = e1.getName();
					String val = e1.getTextTrim();
					if ("fieldSql".equals(key)) {
						dt.setFieldSql(val);
					} else if ("fieldCode".equals(key)) {
						dt.setFieldCode(val);
					} else if ("outputTitle".equals(key)) {
						dt.setOutputTitle(val);
					} else if ("fromTable".equals(key)) {
						dt.setFromTable(val);
					}
				}
				dbCfgs.add(dt);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dbCfgs;
	}
	
	//
	/**
	 * 输出小区结构指标信息
	 * 
	 * @param stmt
	 * @param tmpCellresPath
	 * @param rdOs
	 * @return
	 * @author brightming 2014-8-26 下午1:59:13
	 */
	private ResultInfo outputCellStructResult(Statement stmt,
			String tmpCellresPath, BufferedWriter rdOs,Map<String, CachedCellData> cachedCellInfos) {

		ResultInfo result = new ResultInfo();
		result.setFlag(true);
		
		// 1、小区分析结果临时数据
		
		List<OutputDbDataFormat> cellResOutFormats=readDbOutputFormatFromXml("structOutputCellResFormat.xml");
		
		String innerSql=" select ";
		String[] cellResTitles = new String[cellResOutFormats.size()];//RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES;
		String[] cellResKeys =  new String[cellResOutFormats.size()];//RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS;
		OutputDbDataFormat fmt=null;
		for(int i=0;i<cellResOutFormats.size();i++){
			fmt=cellResOutFormats.get(i);
			cellResTitles[i]=fmt.getOutputTitle();
			cellResKeys[i]=fmt.getFieldCode();
			if("RNO_NCS_CELL_ANA_RESULT_MID".equalsIgnoreCase(fmt.getFromTable())){
				innerSql+=fmt.getFieldSql()+",";
			}
		}
		if(innerSql.endsWith(",")){
			innerSql=innerSql.substring(0,innerSql.length()-1);
		}else{
			innerSql+=" * ";
		}
		innerSql+=" from RNO_NCS_CELL_ANA_RESULT_MID";
		
		String sql = "SELECT mid1.*,"
				+ "  cell_t.name      AS chineseName,"
				+ " cell_t.longitude AS lng,"
				+ " cell_t.latitude  AS lat "
				+ " FROM rno_cell_city_t cell_t inner JOIN "
				+ " (" 
//				+ "SELECT CELL AS cell," + "  FREQ_CNT,"
//				+ "  ROUND(BE_INTERFER,5) BE_INTERFER , "
//				+ "  ROUND(NET_STRUCT_FACTOR,5) NET_STRUCT_FACTOR, "
//				+ "  ROUND(REDUNT_COVER_FACT,5) REDUNT_COVER_FACT, "
//				+ "  ROUND(OVERLAP_COVER,5) OVERLAP_COVER, "
//				+ "  ROUND(SRC_INTERFER,5) SRC_INTERFER, "
//				+ "  ROUND(OVERSHOOTING_FACT,5) OVERSHOOTING_FACT, "
//				+ "  DETECT_CNT, " + "  DETECT_CNT_EXINDR, "
//				+ "  ROUND(EXPECTED_COVER_DIS,5) EXPECTED_COVER_DIS, "
//				+ "  INTERFER_NCELL_CNT, " + "  INTERFER_NCE_CNT_EXINDR, "
//				+ "  ROUND(CELL_COVER,5) CELL_COVER, "
//				+ "  ROUND(CAPACITY_DESTROY,5) CAPACITY_DESTROY, "
//				+ "  ROUND(UL_QUALITY,5) as UL_QUALITY, "
//				+ "  ROUND(DL_QUALITY,5) as DL_QUALITY, "
//				+ "  ROUND(UL_POOR_QUALITY,5) as UL_POOR_QUALITY, "
//				+ "  ROUND(DL_POOR_QUALITY,5) as DL_POOR_QUALITY, "
//				+ "  ROUND(UL_COVERAGE,5) as UL_COVERAGE, "
//				+ "  ROUND(DL_COVERAGE,5) as DL_COVERAGE, "
//				+ "  ROUND(UL_POOR_COVERAGE,5) as UL_POOR_COVERAGE, "
//				+ "  ROUND(DL_POOR_COVERAGE,5) as DL_POOR_COVERAGE, "
//				+ "  ROUND(TA_AVERAGE,5) as TA_AVERAGE "
//				+ "  FROM RNO_NCS_CELL_ANA_RESULT_MID " 
				+innerSql
				+ "  ORDER BY cell "
				+")mid1 " + "  ON(mid1.cell=cell_t.cell)";
		/*sql = "SELECT mid1.*,"
				+ "  cell_t.name      AS chineseName,"
				+ " 0 AS lng,"
				+ " 0  AS lat "
				+ " FROM rno_cell_city_t cell_t inner JOIN "
				+ " (" 
				+innerSql
				+ "  ORDER BY cell "
				+")mid1 " + "  ON(mid1.cell=cell_t.cell)";*/
		log.debug("获取小区分析结果临时数据sql:" + sql);
		long t1 = System.currentTimeMillis();
		List<Map<String, Object>> cellres =  new ArrayList<Map<String, Object>>();
		try {
//			cellres = RnoHelper.commonQuery(stmt, sql);
			ResultSet rs = null;
			String cell="";
			CachedCellData cachedCell=null;
			try {
				rs = stmt.executeQuery(sql);
				ResultSetMetaData meta = rs.getMetaData();
				int columnCnt = meta.getColumnCount();
				List<String> labels = new ArrayList<String>();
				for (int i = 1; i <= columnCnt; i++) {
					labels.add(meta.getColumnLabel(i));
				}
				Map<String, Object> one = null;
				int i = 0;
				while (rs.next()) {
					one = new HashMap<String, Object>();
					cell=null;
					for (i = 1; i <= columnCnt; i++) {
						one.put(labels.get(i-1), rs.getObject(i));
						if(cell==null && "CELL".equalsIgnoreCase(labels.get(i-1))){
							cell=(String)rs.getObject(i);
						}
					}
					cachedCell=cachedCellInfos.get(cell);
					if(cachedCell!=null){
						one.put("IN_CLUSTER_CNT", cachedCell.getInClusterCnt());
						one.put("IN_BAD_CLUSTER_CNT", cachedCell.getInBadClusterCnt());
					}
					cellres.add(one);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error("小区分析结果临时数据sql:" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("获取结构分析结果中的小区指标失败！");
			return result;
		}
		long t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取小区结构指标的耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		// 保存到指定位置，清除资源
		// 输出程序只读文件
		Long lineCnt = saveRdDataToDest(cellResKeys, cellres, rdOs);

		// 输出用户文件
		boolean ok = saveUserData(cellResTitles, cellResKeys, cellres,
				tmpCellresPath);
		result.setFlag(ok);
		result.setAttach(lineCnt);// 保存行数
		return result;
	}

	/**
	 * 输出连通簇信息
	 * 
	 * @param stmt
	 * @param tmpClusterPath
	 * @param bw
	 * @return
	 * @author brightming 2014-8-26 下午2:06:04
	 */
	private ResultInfo outputClusterResult(Statement stmt,
			String tmpClusterPath, BufferedWriter rdOs) {

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		String sql = "select mid1.*,round(mid2.control_factor,5) control_factor,round(MID2.weight,5) weight FROM( select \"CLUSTER_ID\",COUNT(cell) cellcnt ,sum(freq_cnt) as total_freq_cnt ,wm_concat(cell) as sectors from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID "
				+ ") group by CLUSTER_ID "
				+ " )mid1 INNER JOIN ( select id,control_factor,weight from RNO_NCS_CLUSTER_MID "
				+ " )mid2 " + " on mid1.cluster_id=MID2.id";

		String[] titles = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_TITLES;
		// key
		String[] keys = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_KEYS;

		long t1 = System.currentTimeMillis();
		List<Map<String, Object>> clustRes = RnoHelper.commonQuery(stmt, sql);
		long t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取连通簇,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		// 输出只读内容
		long lineCnt = saveRdDataToDest(keys, clustRes, rdOs);
		result.setAttach(lineCnt);

		// 输出用户内容
		boolean ok = saveUserData(titles, keys, clustRes, tmpClusterPath);
		if (!ok) {
			result.setFlag(false);
			result.setMsg("输出供用户查看的连通簇内容失败！");
		}
		return result;
	}

	private void printTmpTable(String tab, Statement stmt) {
		// TODO Auto-generated method stub
		String sql = "select * from " + tab;
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			log.debug(tab + "\r\n---数据如下：");
			for (Map<String, Object> one : res) {
				log.debug(one);
			}
			log.debug(tab + "---数据展示完毕\r\n");
		} else {
			log.debug(tab + "---数据为空！");
		}
	}

	private void printTmpTable1(String sql, Statement stmt) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			log.debug("\r\n---数据如下：");
			for (Map<String, Object> one : res) {
				log.debug(one);
			}
			log.debug("---数据展示完毕\r\n");
		} else {
			log.debug("---数据为空！");
		}
	}

	/**
	 * 输出用户文件
	 * 
	 * @param titles
	 * @param keys
	 * @param datas
	 * @param saveFullPath
	 * @return
	 * @author brightming 2014-8-26 下午2:09:22
	 */
	private boolean saveUserData(String[] titles, String[] keys,
			List<Map<String, Object>> datas, String saveFullPath) {

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath), "gbk"));
			StringBuffer buf = new StringBuffer();
			// 输出标题
			for (int i = 0; i < keys.length; i++) {
				buf.append(titles[i]).append(",");
			}
			buf.deleteCharAt(buf.length() - 1);
			bw.write(buf.toString());
			bw.newLine();

			// 输出内容
			Object val = null;
			for (Map<String, Object> rec : datas) {
				buf.setLength(0);
				for (String k : keys) {
					val = rec.get(k);
					if (val == null) {
						val = "";
					}
					buf.append(val.toString()).append(",");
				}
				bw.write(buf.toString());
				bw.newLine();
			}

			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * 输出只读文件，在末尾增加
	 * 
	 * @param cellResKeys
	 * @param cellres
	 * @param rdOs
	 * @return
	 * @author brightming 2014-8-26 下午1:45:38
	 */
	private long saveRdDataToDest(String[] keys,
			List<Map<String, Object>> datas, BufferedWriter rdOs) {
		String fieldSep = "##";
		String fieldEndStr = "__End";

		long cnt = 0;
		if (datas == null || datas.size() == 0) {
			return cnt;
		}
		int keyLen = keys.length;
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < keyLen; j++) {
			buf.append(keys[j]);
			buf.append(fieldSep);
		}
		// 加上保护末尾
		buf.append(fieldEndStr);
		try {
			rdOs.write(buf.toString());
			rdOs.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cnt++;

		// 输出内容
		Object val = null;
		for (Map<String, Object> rec : datas) {
			buf.setLength(0);
			for (int i = 0; i < keyLen; i++) {
				val = rec.get(keys[i]);
				if (val == null) {
					val = "";
				}
				buf.append(val.toString());
				buf.append(fieldSep);
			}
			buf.append(fieldEndStr);
			try {
				rdOs.write(buf.toString());
				rdOs.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cnt++;
		}

		return cnt;
	}

	/**
	 * 
	 * @title 截断表数据
	 * @param stmt
	 * @param tableName
	 * @return
	 * @author chao.xj
	 * @date 2014-9-5上午10:04:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean truncateTable(Statement stmt, String tableName) {

		String sql = "truncate table " + tableName;

		boolean flag = false;
		try {
			flag = stmt.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}
		return flag;
	}

	/**
	 * 
	 * @title 保存NCS的分析结果到excel表 由于目标数据量过于庞大，此方法不建议使用！
	 * @param stmt
	 * @param savePath
	 * @param taskId
	 * @throws IOException
	 * @author chao.xj
	 * @date 2014-8-21下午5:56:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	@Deprecated
	private void saveNcsAnaResInExcel(Statement stmt, String savePath,
			long taskId) throws IOException {
		long t1;
		long t2;
		String sql = "";
		boolean ok;
		// ---------输出结果------//

		// 除了输出一份到excel以外，还输出到一份文本文件
		List<String> sheetNames = new ArrayList<String>();
		List<String[]> titless = new ArrayList<String[]>();
		List<String[]> keyss = new ArrayList<String[]>();
		List<List<Map<String, Object>>> cellress = new ArrayList<List<Map<String, Object>>>();

		// outputTime.getYear()
		// 将临时表的内容输出，输出到excel
		// 1、小区分析结果临时数据

		sql = "select mid1.*,mid2.name as \"chineseName\",mid2.longitude as \"lng\",mid2.latitude as \"lat\",mid2.lnglats as \"allLngLats\"  "
				+ " FROM( select CELL as \"cell\",FREQ_CNT, round(BE_INTERFER,5) BE_INTERFER ,round(NET_STRUCT_FACTOR,5) NET_STRUCT_FACTOR,round(REDUNT_COVER_FACT,5) REDUNT_COVER_FACT,round(OVERLAP_COVER,5) OVERLAP_COVER,round(SRC_INTERFER,5) SRC_INTERFER,round(OVERSHOOTING_FACT,5) OVERSHOOTING_FACT,DETECT_CNT,DETECT_CNT_EXINDR,round(EXPECTED_COVER_DIS,5) EXPECTED_COVER_DIS,INTERFER_NCELL_CNT,INTERFER_NCE_CNT_EXINDR,round(CELL_COVER,5) CELL_COVER,round(CAPACITY_DESTROY,5) CAPACITY_DESTROY "
				+ " from RNO_NCS_CELL_ANA_RESULT_MID "
				+ " order by cell "
				+ " )mid1 left join ( select * from (SELECT label,name,longitude,latitude,lnglats ,row_number() over(partition by label order by name) as seq FROM cell )where seq=1)mid2 on(mid1.\"cell\"=mid2.label) ";

		log.debug("1、小区分析结果临时数据sql:" + sql);
		// 输出到excel
		String[] cellResTitles = new String[RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES.length - 1];
		String[] cellResKeys = new String[RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS.length - 1];
		// 2014-6-13 gmh excel不输出“范围”信息
		int idx = 0;
		for (int m = 0; m < RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES.length; m++) {
			if ("范围".equals(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES[m])) {
				continue;
			}
			cellResTitles[idx] = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES[m];
			cellResKeys[idx] = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS[m];
			idx++;
		}
		t1 = System.currentTimeMillis();
		List<Map<String, Object>> cellres = null;
		try {
			cellres = RnoHelper.commonQuery(stmt, sql);
		} catch (Exception e) {
			log.error("1、小区分析结果临时数据sql:" + sql);
			e.printStackTrace();
		}
		t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取小区结构指标的耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		// 2、连通簇数据
		// 标题
		sql = "select mid1.*,round(mid2.control_factor,5) control_factor,round(MID2.weight,5) weight FROM( select \"CLUSTER_ID\",COUNT(cell) cellcnt ,sum(freq_cnt) as total_freq_cnt ,wm_concat(cell) as sectors from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID "
				+ ") group by CLUSTER_ID "
				+ " )mid1 INNER JOIN ( select id,control_factor,weight from RNO_NCS_CLUSTER_MID "
				+ " )mid2 " + " on mid1.cluster_id=MID2.id";
		// ClusterID（簇ID） Count（小区数） Trxs（频点数/载波数） 簇约束因子 簇权重 Sectors（小区列表）
		String[] clusterTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_TITLES;
		// {"ClusterID（簇ID）","Count（小区数）","Trxs（频点数/载波数）","簇约束因子","簇权重","Sectors（小区列表）"};
		// key
		String[] clusterKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_KEYS;
		// {"CLUSTER_ID","CELLCNT","TOTAL_FREQ_CNT","CONTROL_FACTOR","WEIGHT","SECTORS"};
		t1 = System.currentTimeMillis();
		List<Map<String, Object>> clustRes = RnoHelper.commonQuery(stmt, sql);
		t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取连通簇,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		// 3、连通簇小区数据
		// 标题
		String[] clusterCellTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_TITLES;
		// {"簇ID","小区名","小区中文名","小区载频","簇TCH载频数"};
		// key
		String[] clusterCellKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_KEYS;
		// {"CLUSTER_ID","CELL","NAME","FREQ_CNT","TOTAL_FREQ_CNT"};
		sql = "select mid3.*,CELL.name FROM (select mid1.*,mid2.total_freq_cnt from "
				+ " (select cluster_id,cell,freq_cnt from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID "
				+ ") order by cluster_id)mid1 "
				+ " inner join ( select \"CLUSTER_ID\",sum(freq_cnt) as total_freq_cnt from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID "
				+ ") group by CLUSTER_ID )mid2 "
				+ " on (mid1.cluster_id=mid2.cluster_id) "
				+ " )mid3 left join (select label,min(name) as name from cell group by label) cell on mid3.cell=cell.label ";
		t1 = System.currentTimeMillis();
		List<Map<String, Object>> clustCellRes = RnoHelper.commonQuery(stmt,
				sql);
		t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取连通簇内小区,耗时：" + (t2 - t1) + "ms.  sql=" + sql);
		// 4、簇内干扰关系
		String[] interTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_TITLES;
		// {"主小区","簇编号","簇内小区载波数","干扰小区"};
		String[] interKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_KEYS;
		sql = "select mid1.*,mid2.total_freq_cnt FROM "
				+ " (SELECT CLUSTER_ID,CELL,NCELL FROM RNO_NCS_CLUSTER_CELL_RELA_MID WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER_mid "
				+ ")"
				+ " )mid1 INNER JOIN "
				+ " ( SELECT CLUSTER_ID,SUM(FREQ_CNT) as total_freq_cnt FROM RNO_NCS_CLUSTER_CELL_MID WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER_mid "
				+ ") " + " group by cluster_id )mid2 "
				+ " ON(MID1.cluster_id=MID2.cluster_id) ";
		t1 = System.currentTimeMillis();
		List<Map<String, Object>> clustCellRelaRes = RnoHelper.commonQuery(
				stmt, sql);
		t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取簇小区干扰关系,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		// 自动优化结果
		String[] suggestTs = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_TITLES;
		String[] suggestKs = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_KEYS;

		sql = "select \"CELL\",\"CELLNAME\",round(EXPECTED_COVER_DIS,5) as \"EXPECTED_COVER_DIS\",round(OVERSHOOTING_FACT,5) as \"OVERSHOOTING_FACT\",\"INTERFER_NCELL_CNT\",\"INTERFER_NCE_CNT_EXINDR\",round(CELL_COVER,5) as \"CELL_COVER\",round(CAPACITY_DESTROY,5) as \"CAPACITY_DESTROY\",\"LONGITUDE\",\"LATITUDE\",round(ANT_HEIGHT,5) as \"ANT_HEIGHT\",round(DOWNTILT,5) as \"DOWNTILT\",round(IDEAL_DOWNTILT,5) as \"IDEAL_DOWNTILT\",\"INDOOR_CELL_TYPE\",\"DECLARE_CHANNEL_NUMBER\",\"AVAILABLE_CHANNEL_NUMBER\",\"CARRIER_NUMBER\",round(RESOURCE_USE_RATE,5) as \"RESOURCE_USE_RATE\",round(TRAFFIC,5) as \"TRAFFIC\",\"PROBLEM_DESC\",\"MESSAGE\",\"STS_TIME\" FROM RNO_NCS_PROBLEM_CELL_MID";
		t1 = System.currentTimeMillis();
		List<Map<String, Object>> optSuggestion = RnoHelper.commonQuery(stmt,
				sql);
		t2 = System.currentTimeMillis();
		log.debug("汇总分析，获取自动优化建议,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

		List<String> dataTypes = new ArrayList<String>();// 数据的分类，
		// 准备输出
		dataTypes.add(RnoConstant.ReportConstant.CELLRES);
		dataTypes.add(RnoConstant.ReportConstant.CLUSTER);
		dataTypes.add(RnoConstant.ReportConstant.CLUSTERCELL);
		dataTypes.add(RnoConstant.ReportConstant.CLUSTERCELLRELA);
		dataTypes.add(RnoConstant.ReportConstant.OPTSUGGESTION);

		titless.add(cellResTitles);
		titless.add(clusterTs);
		titless.add(clusterCellTs);
		titless.add(interTs);
		titless.add(suggestTs);

		keyss.add(cellResKeys);
		keyss.add(clusterKs);
		keyss.add(clusterCellKs);
		keyss.add(interKs);
		keyss.add(suggestKs);

		cellress.add(cellres);
		cellress.add(clustRes);
		cellress.add(clustCellRes);
		cellress.add(clustCellRelaRes);
		cellress.add(optSuggestion);

		sheetNames.add(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET);
		sheetNames.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET);
		sheetNames
				.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET);
		sheetNames
				.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET);

		sheetNames
				.add(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_SHEET);

		// 准备保存到文本文件，以供程序读取
		log.debug("准备输出供程序读取的分析结果。。。");
		String txtFilePath = savePath
				+ "/"
				+ taskId
				+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX;
		List<String[]> fullKeyss = new ArrayList<String[]>(keyss);
		fullKeyss.remove(0);
		fullKeyss.add(0,
				RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS);
		log.debug("txtFilePath=" + txtFilePath);
		long cnt = FileTool.saveDataInTxtFile(txtFilePath, dataTypes,
				fullKeyss, cellress);
		log.debug("保存记录数量：" + cnt);
		// 保存到excel，供用户下载
		boolean saveres = FileTool.saveDataInExcelFile(savePath, sheetNames,
				titless, keyss, cellress);
		log.debug("保存到了excel文件：" + savePath);
		log.debug("保存结果：" + saveres);
		titless = null;
		keyss = null;
		cellress = null;
		sheetNames = null;

		System.gc();
	}

	private ResultInfo outputProvinceConstrainResult(
			Map<Long, List<CellLonLat>> cellListAttrs,
			Map<Long, Integer> clusterIdToCluTchCnt, String tmpDir) {
		log.debug("进入outputConstrainResult(Map<String, ClusterCell> cellAttrs)");
		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		// 确定临时目录存在
		File tmpDirFile = new File(tmpDir);
		if (!tmpDirFile.exists()) {
			tmpDirFile.mkdirs();
		}

		String tmpProvinceConstrainPath = tmpDir + "省内标准.csv";
		log.debug("tmpClusterPath:" + tmpProvinceConstrainPath);
		// 输出用户内容
		boolean ok = saveConstrainData(cellListAttrs, tmpProvinceConstrainPath,
				clusterIdToCluTchCnt);
		if (!ok) {
			result.setFlag(false);
			result.setMsg("输出供用户查看的连通簇内容失败！");
		}
		return result;
	}

	private ResultInfo outputGroupConstrainResult(
			Map<Long, List<CellLonLat>> cellListAttrs,
			Map<Long, Integer> clusterIdToCluTchCnt, String tmpDir) {
		log.debug("进入outputConstrainResult(Map<String, ClusterCell> cellAttrs)");
		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		// 确定临时目录存在
		File tmpDirFile = new File(tmpDir);
		if (!tmpDirFile.exists()) {
			tmpDirFile.mkdirs();
		}
		String tmpClusterPath = tmpDir + "集团标准.csv";
		log.debug("tmpClusterPath:" + tmpClusterPath);
		// 输出用户内容
		boolean ok = saveConstrainData(cellListAttrs, tmpClusterPath,
				clusterIdToCluTchCnt);
		if (!ok) {
			result.setFlag(false);
			result.setMsg("输出供用户查看的连通簇内容失败！");
		}
		return result;
	}

	private boolean saveConstrainData(
			Map<Long, List<CellLonLat>> cellListAttrs, String saveFullPath,
			Map<Long, Integer> clusterIdToCluTchCnt) {

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveFullPath), "gbk"));
			StringBuffer buf = new StringBuffer();
			// 输出标题
			List<String> titles = Arrays.asList("时间", "地市", "簇ID", "小区名",
					"小区中文名", "频段", "区域类别", "小区载频", "簇tch载频数", "可用tch频点数",
					"簇约束因子");
			for (String str : titles) {
				buf.append(str).append(",");
			}
			buf.deleteCharAt(buf.length() - 1);
			bw.write(buf.toString());
			bw.newLine();
			// 输出内容
			Object val = null;
			Iterator<Entry<Long, List<CellLonLat>>> iterator = cellListAttrs
					.entrySet().iterator();
			for (Long cluId : cellListAttrs.keySet()) {
				List<CellLonLat> clusterCells = cellListAttrs.get(cluId);
				for (CellLonLat clusterCell : clusterCells) {
					buf.setLength(0);
					if (clusterCell == null) {
						continue;
					}
					String cellFreqSection = clusterCell.getFreqSection();

					double cluTchFreqCnt = Double
							.parseDouble(clusterIdToCluTchCnt.get(cluId)
									.toString());
					// double
					// availableFreqCnt=Double.parseDouble(Integer.valueOf(clusterCell.getAvailableFreqCnt()).toString());
					double availableFreqCnt = 0;
					if ("GSM900".equals(cellFreqSection)) {
						availableFreqCnt = 95;
						if (cluTchFreqCnt < 36) {
							continue;
						}
					}
					if ("GSM1800".equals(cellFreqSection)) {
						availableFreqCnt = 124;
						if (cluTchFreqCnt < 51) {
							continue;
						}
					}
					double constrain = cluTchFreqCnt / availableFreqCnt;

					buf.append(sdf1.format(new Date())).append(",");
					buf.append("东莞").append(",");
					// buf.append(clusterCell.getClusterId()).append(",");
					buf.append(cluId).append(",");
					buf.append(clusterCell.getCell()).append(",");
					buf.append(clusterCell.getName()).append(",");
					buf.append(cellFreqSection).append(",");
					buf.append("一般城区").append(",");
					buf.append(clusterCell.getFreqCnt()).append(",");
					buf.append(cluTchFreqCnt).append(",");
					buf.append(availableFreqCnt).append(",");
					buf.append(constrain);
					bw.write(buf.toString());
					bw.newLine();
				}
			}

			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static void main(String[] args) {

		Connection conn = DataSourceConn.initInstance().getConnection();
		RnoStructureAnalysisDaoImplV2 analysisDaoImplV2 = new RnoStructureAnalysisDaoImplV2();
		try {
			Statement stmt = conn.createStatement();
			/*
			 * analysisDaoImplV2.calculate2GClusterConstrain(stmt,
			 * "RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID", null);
			 */
			// analysisDaoImplV2.calculate2GCellIdealCoverDis(1, stmt,
			// "RNO_2G_NCS_ANA_T", "RNO_NCS_CELL_ANA_RESULT_MID", null);
			List<Map<String, Object>> aa = RnoHelper.commonQuery(stmt,
					"select * from cell where area_id=260");
			for (Map<String, Object> map : aa) {
				System.out
						.println("-----------------------开始--------------------------");
				for (String key : map.keySet()) {
					System.out.println(key + "=======" + map.get(key));
				}
				System.out
						.println("-----------------------结束--------------------------");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * List<String[]> titless = new ArrayList<String[]>(); List<String[]>
		 * keyss = new ArrayList<String[]>(); List<String> names = new
		 * ArrayList<String>();
		 * 
		 * String[] titles =
		 * RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_TITLES; String[]
		 * keys = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_KEYS;
		 * titless.add(titles); keyss.add(keys); names.add("cluster");
		 * 
		 * titles = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES;
		 * keys = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS;
		 * titless.add(titles); keyss.add(keys); names.add("cellres");
		 * 
		 * titles =
		 * RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_TITLES; keys
		 * = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_KEYS;
		 * titless.add(titles); keyss.add(keys); names.add("clustercell");
		 * 
		 * titles =
		 * RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_TITLES;
		 * keys =
		 * RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_KEYS;
		 * titless.add(titles); keyss.add(keys); names.add("clustercellrela");
		 * 
		 * String name = "";
		 * 
		 * for (int out = 0; out < titless.size(); out++) { keys =
		 * keyss.get(out); titles = titless.get(out); name = names.get(out);
		 * log.debug("<type name=\"" + name + "\"></type>"); for (int i = 0; i <
		 * keys.length; i++) { log.debug("<output key=\"" + keys[i] +
		 * "\" title=\"" + titles[i] + "\"></output>"); } }
		 */
	}

	private void chktablecount1(String pre, Statement stmt) {
		// ---检查eri ncs 临时表的数据情况
		String chksql = " select count(cell) from ( select  cell,                                                  "
				+ "          NCELL,                                                "
				+ "        MIN(DEFINED_NEIGHBOUR) DEFINED_NEIGHBOUR,       "
				+ "         min(CELL_LON) CELL_LON,                                          "
				+ "         min(CELL_LAT) CELL_LAT,                                          "
				+ "         min(NCELL_LON) NCELL_LON,                                        "
				+ "         min(NCELL_LAT) NCELL_LAT,                                        "
				+ "         min(CELL_FREQ_CNT) CELL_FREQ_CNT,                                "
				+ "         min(NCELL_FREQ_CNT) NCELL_FREQ_CNT,                              "
				+ "         min(SAME_FREQ_CNT) SAME_FREQ_CNT,                                "
				+ "         min(ADJ_FREQ_CNT) ADJ_FREQ_CNT,                                  "
				+ "         sum(reparfcn) CI_DENOMINATOR,                                    "
				+ "         sum(reparfcn) CA_DENOMINATOR,                                    "
				+ "         sum(ci_divider) ci_divider,                                      "
				+ "         sum(ca_divider) ca_divider,                                      "
				+ "         min(NCELLS) NCELLS,                                              "
				+ "         min(CELL_BEARING) CELL_BEARING,                                  "
				+ "         min(CELL_INDOOR) CELL_INDOOR,                                    "
				+ "         min(NCELL_INDOOR) NCELL_INDOOR,                                  "
				+ "         min(CELL_DOWNTILT) CELL_DOWNTILT,                                "
				+ "         min(CELL_SITE),                                                  "
				+ "         min(NCELL_SITE) NCELL_SITE,                                      "
				+ "         min(CELL_FREQ_SECTION) CELL_FREQ_SECTION,                        "
				+ "         min(NCELL_FREQ_SECTION) NCELL_FREQ_SECTION,                      "
				+ "         min(DISTANCE) DISTANCE,                                        "
				+ "          min(CELL_TO_NCELL_DIR) CELL_TO_NCELL_DIR"
				+ "    from rno_2g_eri_ncs_t "
				+ "   group by (cell, ncell)  "
				+ " )";

		List chkres = RnoHelper.commonQuery(stmt, chksql);
		log.debug(pre + "前检查eri ncs的数量：" + chkres);

	}

	/**
	 * 输出集团过覆盖信息
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-9-29 下午12:26:17
	 */
	private ResultInfo outputOverShootingCellDetails(Statement stmt,
			RnoStructAnaJobRec jobRec) {

		ResultInfo result = new ResultInfo(true);

		String cityName = jobRec.getCityName();

		String sql = "select count(cell) as cnt from RNO_2G_NCS_ANA_T where  CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND CI_DIVIDER/CI_DENOMINATOR>0.03";
		List<Map<String, Object>> cntInfo = RnoHelper.commonQuery(stmt, sql);
		long cnt = -1;
		if (cntInfo != null && cntInfo.size() > 0) {
			try {
				cnt = Long.parseLong(cntInfo.get(0).get("CNT").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cnt <= 0) {
			result.setFlag(false);
			result.setMsg("没有ncs测量数据");
			return result;
		}

		String fullPath = jobRec.getTmpDir() + "/" + cityName
				+ " 集团过覆盖小区的明细表.csv";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fullPath, true), "gbk"));
			bw.write("地市,主小区,邻小区,同频相关系数");
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备输出集团过覆盖小区的明细表时出错！");
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
			return result;
		}
		int pageSize = 100000;

		ResultSet rs = null;

		log.debug(">>>>>>>>>>>>>>>" + cityName + " 准备输出输出集团过覆盖小区的明细信息，共有记录："
				+ cnt + "条...");
		long t1 = System.currentTimeMillis();
		for (int row = 0; row < cnt; row++) {
			/*
			 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
			 */
			sql = "select * "
					+ "from (select * "
					+ " from (select CELL ,NCELL ,CI_DIVIDER/CI_DENOMINATOR AS CI, row_number() OVER(ORDER BY null) AS row_number "
					+ " from  RNO_2G_NCS_ANA_T  t where  CELL_FREQ_SECTION=NCELL_FREQ_SECTION AND CI_DIVIDER/CI_DENOMINATOR>0.03 ) p "
					+ " where p.row_number > " + row + ") q "
					+ " where rownum <=" + pageSize;
			try {
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					//
					try {
						bw.write(cityName + ",");
						bw.write(rs.getString("CELL") + ",");
						bw.write(rs.getString("NCELL") + ",");
						bw.write(rs.getDouble("CI") * 100 + "");
						bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			row += pageSize - 1;// 步长
		}
		long t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<<" + cityName + " 完成输出输出集团过覆盖小区的明细信息，耗时："
				+ (t2 - t1) + "ms");

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * 输出重叠覆盖度 这个比较耗时
	 * 
	 * ！！！该方法需要在转移数据到临时表rno_2g_eri_ncs_T、rno_2g_hw_ncs_T以后，就进行 ！！！否则以上两个表的数据会被清空
	 * 
	 * @param stmt
	 * @param jobRec
	 * @return
	 * @author brightming 2014-9-30 下午2:23:16
	 */
	private ResultInfo outputCellOverlapDetails(boolean isEri, Statement stmt,
			RnoStructAnaJobRec jobRec) {

		ResultInfo result = new ResultInfo(true);

		DateUtil dateUtil = new DateUtil();

		long st = System.currentTimeMillis();
		String cityName = jobRec.getCityName();
		Date begMeaT = jobRec.getBegMeaTime();
		Date endMeaT = jobRec.getEndMeaTime();

		if (begMeaT == null || endMeaT == null) {
			log.error(cityName + " 由于分析的测试数据的开始时间和结束时间不明确，无法输出重叠覆盖度");
			result.setFlag(false);
			result.setMsg("由于分析的测试数据的开始时间和结束时间不明确，无法输出重叠覆盖度");
			return result;
		}

		String sql = "";

		// --准备输出路径
		String fullPath_sep = jobRec.getTmpDir() + "/" + cityName
				+ " 重叠覆盖度（分开信道查询）.csv";
		String fullPath_tog = jobRec.getTmpDir() + "/" + cityName
				+ " 重叠覆盖度（合并信道查询）.csv";

		BufferedWriter bw_sep = null;
		BufferedWriter bw_tog = null;
		
		long sqlTime=0;//sql执行时间
		long javaTime=0;//java执行时间
		long otTime=0;//输出到文件的时间
		long getDataTime=0;//获取resultset数据时间
		long fillDataTime=0;//填充到对象的时间
		long t1=0,t2=0;
		long tt1=0,tt2=0;
		try {
			// 分开信道查询的输出
			bw_sep = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fullPath_sep, true), "gbk"));
			if (!jobRec.getWorkResultFiles().contains("overlap")) {
				bw_sep.write("地市,Sector_ID,SUMTIMESRELSS,SUMREPARFCN,小区重叠覆盖度");
				bw_sep.newLine();
			}

			// 合并信道查询的输出
			bw_tog = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fullPath_tog, true), "gbk"));
			if (!jobRec.getWorkResultFiles().contains("overlap")) {
				bw_tog.write("地市,Sector_ID,SUMTIMESRELSS,SUMREPARFCN,小区重叠覆盖度");
				bw_tog.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备输出重叠覆盖度时出错！");
			if (bw_sep != null) {
				try {
					bw_sep.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
			if (bw_tog != null) {
				try {
					bw_tog.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
			return result;
		}

		int pageSize = 10000 * 100;
		ResultSet rs = null;

		// 外层key为小区标识名称，内存key为时间段，
		Map<String, Map<String, NcsChgrData>> cellMeaDatas = new HashMap<String, Map<String, NcsChgrData>>();
		String cell = "";
		int chgr = 0;
		long reparfcn = 0;
		long ciDivider = 0;
		Date meaT = null;
		String meatStr = "";

		Map<String, NcsChgrData> oneTimeRange = null;
		NcsChgrData oneChgr = null;

		String preCell = null;
		long cnt = -1;
		if (isEri) {
			jobRec.getWorkResultFiles().add("overlap");
			// ---输出爱立信数据
			log.debug("准备输出爱立信重叠覆盖度数据。");
			// 先看总量
			sql = "select count(*) CNT from rno_2g_eri_ncs_T ";
			List<Map<String, Object>> cntInfo = RnoHelper
					.commonQuery(stmt, sql);
			if (cntInfo != null && cntInfo.size() > 0
					&& cntInfo.get(0).get("CNT") != null) {
				try {
					cnt = Long.parseLong(cntInfo.get(0).get("CNT").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (cnt <= 0) {
				result.setFlag(false);
				result.setMsg("没有爱立信ncs测量数据，无法输出重叠覆盖度。");
				if (bw_sep != null) {
					try {
						bw_sep.close();
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
				if (bw_tog != null) {
					try {
						bw_tog.close();
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
				return result;
			}

			// 分页获取数据，完善小区数据
			for (int row = 0; row < cnt; row++) {
				/*
				 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
				 */
				sql = "select * "
						+ "from (select * "
						+ " from (select CELL,CHGR,CI_DIVIDER,REPARFCN,MEA_TIME,row_number() OVER(ORDER BY CELL) AS row_number "
						+ " from  rno_2g_eri_ncs_T  t ) p "
						+ " where p.row_number > " + row + ") q "
						+ " where rownum <=" + pageSize;
				try {
					t1=System.currentTimeMillis();
					rs = stmt.executeQuery(sql);
					t2=System.currentTimeMillis();
					sqlTime+=t2-t1;
					log.debug("分页查询eri过覆盖数据：起始row="+row+"，耗时："+(t2-t1)+"ms");
					t1=System.currentTimeMillis();
					while (rs.next()) {
						//
						tt1=System.currentTimeMillis();
						cell = rs.getString("CELL");
						chgr = rs.getInt("CHGR");
						reparfcn = rs.getLong("REPARFCN");
						meaT = rs.getTimestamp("MEA_TIME");
						ciDivider = rs.getLong("CI_DIVIDER");
						tt2=System.currentTimeMillis();
						getDataTime+=(tt2-tt1);
						if (meaT == null) {
							continue;
						}

						tt1=System.currentTimeMillis();
						// 该小区是否已有数据
						oneTimeRange = cellMeaDatas.get(cell);
						if (oneTimeRange == null) {
							oneTimeRange = new HashMap<String, NcsChgrData>();
							cellMeaDatas.put(cell, oneTimeRange);
						}
						// 该小区是否已有该时间段的数据
						meatStr = dateUtil.format_yyyyMMddHH(meaT);
						oneChgr = oneTimeRange.get(meatStr);
						if (oneChgr == null) {
							oneChgr = new NcsChgrData();
							oneTimeRange.put(meatStr, oneChgr);
						}
						// 增加该时间段的该chgr的数据
						oneChgr.addChgrData(chgr, reparfcn, ciDivider);
						
						tt2=System.currentTimeMillis();
						fillDataTime+=(tt2-tt1);
						
						if (preCell == null) {
							preCell = cell;
						} else if (!cell.equals(preCell)) {
							// 当前处理的小区与之前的不一样，可以将之前的小区进行输出了。
							oneTimeRange = cellMeaDatas.get(preCell);
							if (oneTimeRange != null) {
								tt1=System.currentTimeMillis();
								outputOneCellOverlapInner(cityName, bw_sep,
										bw_tog, oneTimeRange, preCell);
								tt2=System.currentTimeMillis();
								otTime+=(tt2-tt1);
							}
							// 已输出，清除
							cellMeaDatas.remove(preCell);
							preCell = cell;
						}
					}
					t2=System.currentTimeMillis();
					javaTime+=t2-t1;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				row += pageSize - 1;// 步长
			}

			// 输出最后一个小区的
			tt1=System.currentTimeMillis();
			outputOneCellOverlapInner(cityName, bw_sep, bw_tog,
					cellMeaDatas.get(cell), cell);
			tt2=System.currentTimeMillis();
			otTime+=(tt2-tt1);

		} else {
			log.debug("准备输出华为重叠覆盖度数据。");
			jobRec.getWorkResultFiles().add("overlap");
			// 先看总量
			sql = "select count(*) CNT from rno_2g_hw_ncs_T ";
			List<Map<String, Object>> cntInfo = RnoHelper
					.commonQuery(stmt, sql);
			
			if (cntInfo != null && cntInfo.size() > 0
					&& cntInfo.get(0).get("CNT") != null) {
				try {
					cnt = Long.parseLong(cntInfo.get(0).get("CNT").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (cnt <= 0) {
				result.setFlag(false);
				result.setMsg("没有华为ncs测量数据，无法输出重叠覆盖度。");
				if (bw_sep != null) {
					try {
						bw_sep.close();
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
				if (bw_tog != null) {
					try {
						bw_tog.close();
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
				return result;
			}

			// 分页获取数据，完善小区数据
			for (int row = 0; row < cnt; row++) {
				/*
				 * int endPage=i+pageSize-1; if(endPage>=cnt){ endPage=cnt-1; }
				 */
				sql = "select * "
						+ "from (select * "
						+ " from (select CELL,0 as CHGR,(s361-s369) AS CI_DIVIDER,S3013 as REPARFCN,MEA_TIME,row_number() OVER(ORDER BY CELL) AS row_number "
						+ " from  rno_2g_hw_ncs_T  t ) p "
						+ " where p.row_number > " + row + ") q "
						+ " where rownum <=" + pageSize;
				try {
					t1=System.currentTimeMillis();
					rs = stmt.executeQuery(sql);
					t2=System.currentTimeMillis();
					sqlTime+=t2-t1;
					log.debug("分页查询hw过覆盖数据：起始row="+row+"，耗时："+(t2-t1)+"ms");
					t1=System.currentTimeMillis();
					while (rs.next()) {
						//
						cell = rs.getString("CELL");
						chgr = rs.getInt("CHGR");
						reparfcn = rs.getLong("REPARFCN");
						meaT = rs.getTimestamp("MEA_TIME");
						ciDivider = rs.getLong("CI_DIVIDER");
						if (meaT == null) {
							continue;
						}

						// 该小区是否已有数据
						if(cellMeaDatas.containsKey(cell)){
						oneTimeRange = cellMeaDatas.get(cell);
						}
//						if (oneTimeRange == null) {
						else{
							oneTimeRange = new HashMap<String, NcsChgrData>();
							cellMeaDatas.put(cell, oneTimeRange);
						}
						// 该小区是否已有该时间段的数据
						meatStr = dateUtil.format_yyyyMMddHH(meaT);
						if(oneTimeRange.containsKey(meatStr)){
						oneChgr = oneTimeRange.get(meatStr);
						}
//						if (oneChgr == null) {
						else{
							oneChgr = new NcsChgrData();
							oneTimeRange.put(meatStr, oneChgr);
						}
						// 增加该时间段的该chgr的数据
						oneChgr.addChgrData(chgr, reparfcn, ciDivider);
						if (preCell == null) {
							preCell = cell;
						} else if (!cell.equals(preCell)) {
							// 当前处理的小区与之前的不一样，可以将之前的小区进行输出了。
							oneTimeRange = cellMeaDatas.get(preCell);
							if (oneTimeRange != null) {
								outputOneCellOverlapInner(cityName, bw_sep,
										bw_tog, oneTimeRange, preCell);
							}
							// 已输出，清除
							cellMeaDatas.remove(preCell);
							preCell = cell;
						}
					}
					t2=System.currentTimeMillis();
					javaTime+=t2-t1;
							
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rs != null)
							rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				row += pageSize - 1;// 步长
			}

			// 输出最后一个小区的
			outputOneCellOverlapInner(cityName, bw_sep, bw_tog,
					cellMeaDatas.get(cell), cell);

		}

		cellMeaDatas = null;
		try {
			bw_sep.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw_tog.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long et = System.currentTimeMillis();
		log.debug(cityName + " 重叠覆盖度输出完成。查询记录数量："+cnt+",每页数量:"+pageSize+",分页次数:"+Math.ceil(cnt*1.0d/pageSize)+",总耗时：" + (et - st) + "ms。sql执行耗时："+sqlTime+"ms,java耗时："+javaTime+"ms.细节：获取resultSet内数据时间："+getDataTime+"ms,填充到相应对象的时间： "+fillDataTime+"ms,输出到文件的时间："+(otTime)+"ms");
		return result;

	}

	private void outputOneCellOverlapInner(String cityName,
			BufferedWriter bw_sep, BufferedWriter bw_tog,
			Map<String, NcsChgrData> oneTimeRange, String cell) {
		long sumTimesReless = 0;//
		long sumRepafrcn_sep = 0;// 分开信道查询
		long sumRepafrcn_tog = 0;// 合并信道查询

		if (oneTimeRange == null) {
			return;
		}
		for (NcsChgrData val : oneTimeRange.values()) {
			sumTimesReless += val.getTimesrelss();
			sumRepafrcn_sep += val.getLargestTotalTimes();
			for (long rep : val.getChgrLargestNum().values()) {
				sumRepafrcn_tog += rep;
			}
		}
		// 输出“分开信道查询”结果
		try {
			bw_sep.write(cityName
					+ ","
					+ cell
					+ ","
					+ sumTimesReless
					+ ","
					+ sumRepafrcn_sep
					+ ","
					+ (sumRepafrcn_sep == 0 ? " " : (sumTimesReless * 1.0f)
							/ sumRepafrcn_sep / 6));
			bw_sep.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 输出“合并信道查询”结果
		try {
			bw_tog.write(cityName
					+ ","
					+ cell
					+ ","
					+ sumTimesReless
					+ ","
					+ sumRepafrcn_tog
					+ ","
					+ (sumRepafrcn_tog == 0 ? " " : (sumTimesReless * 1.0f)
							/ sumRepafrcn_tog / 6));
			bw_tog.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class NcsChgrData {
		// 各频率组的最大值，key为频率组编号
		Map<Integer, Long> chgrLargestNum = new HashMap<Integer, Long>();
		// 所有频率组的最大值
		long largestTotalTimes = 0;

		// 累计的分子
		long timesrelss;

		public long getTimesrelss() {
			return timesrelss;
		}

		public void setTimesrelss(long timesrelss) {
			this.timesrelss = timesrelss;
		}

		public Map<Integer, Long> getChgrLargestNum() {
			return chgrLargestNum;
		}

		public void setChgrLargestNum(Map<Integer, Long> chgrLargestNum) {
			this.chgrLargestNum = chgrLargestNum;
		}

		public long getLargestTotalTimes() {
			return largestTotalTimes;
		}

		public void setLargestTotalTimes(long largestTotalTimes) {
			this.largestTotalTimes = largestTotalTimes;
		}

		public void addChgrData(int chgr, long repafrcn, long timerelss) {
			if (!chgrLargestNum.containsKey(chgr)) {
				chgrLargestNum.put(chgr, repafrcn);
			} else {
				if (chgrLargestNum.get(chgr) < repafrcn) {
					chgrLargestNum.put(chgr, repafrcn);
				}
			}

			//
			this.timesrelss += timerelss;// 直接累加
			if (largestTotalTimes < repafrcn) {
				largestTotalTimes = repafrcn;
			}
		}
	}

}
