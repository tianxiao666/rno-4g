package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.Mergeable;

import bsh.org.objectweb.asm.Type;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoSts;
import com.iscreate.op.pojo.rno.StsAnaItemDetail;
import com.iscreate.op.pojo.rno.StsConfig;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;

/**
 * 导入gsm小区数据
 * 
 * @author brightming
 * 
 */
/**
 * @author Liang YJ
 * 2014-1-6 上午11:34:15
 */
/**
 * @author Liang YJ
 * 2014-1-6 上午11:34:19
 */
public class GsmAudioTrafficStaticsFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory
			.getLog(GsmAudioTrafficStaticsFileParser.class);
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

	private static String reg1 = "[0-9]{2}0{2}\\-{1}[0-9]{2}0{2}";
	private static Pattern pattern = Pattern.compile(reg1);

	String NET_TYPE = RnoConstant.DBConstant.NET_TYPE_GSM;
	String SPEC_TYPE = RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_AUDIO;

	// ---spring 注入----//
	// public MemCachedClient memCached;
	public ExcelService excelService;
	public RnoBscDao rnoBscDao;

	// static final String STS_TYPE = "GSMAUDIOTRAFFICSTATICSFILE";
	// static final String SPEC_TYPE = "GSM";

	// public IFileParserManager fileParserManager;

	private static List<String> expectTitles = Arrays.asList("DATE", "PERIOD",
			"BSC", "CELL", "小区", "T完好率", "定义信道", "可用信道", "载波数", "无线资源利用率",
			"话务量", "每线话务量", "接通率", "无线接入性", "总掉话", "无线掉话率(不含切换)", "干扰系数",
			"切出成功率", "切入成功率", "切换成功率", "PS无线利用率", "PDCH承载效率（kbps）",
			"数据流量(MByte)", "下行BPDCH复用度", "下行EPDCH复用度", "下行TBF拥塞率");

	private static int titleSize = expectTitles.size();
	
	
	// key为：数据库字段名
	// value为：excel标题名
	private static Map<String, String> dbFieldsToExcelTitles = new HashMap<String, String>()
	{
		
		{
			put("STS_DATE", "DATE");
		};
		{
			put("STS_PERIOD", "PERIOD");
		};
		{
			put("BSC_ID", "BSC");
		};
		{
			put("CELL", "CELL");
		};
		{
			put("CELL_CHINESE_NAME", "小区");
		};
		{
			put("TCH_INTACT_RATE", "T完好率");
		};
		{
			put("DECLARE_CHANNEL_NUMBER", "定义信道");
		};
		{
			put("AVAILABLE_CHANNEL_NUMBER", "可用信道");
		};
		{
			put("CARRIER_NUMBER", "载波数");
		};
		{
			put("RESOURCE_USE_RATE", "无线资源利用率");
		};
		{
			put("TRAFFIC", "话务量");
		};
		{
			put("TRAFFIC_PER_LINE", "每线话务量");
		};
		{
			put("ACCESS_OK_RATE", "接通率");
		};
		{
			put("RADIO_ACCESS", "无线接入性");
		};
		{
			put("DROP_CALL_NUM_TOGETHER", "总掉话");
		};
		{
			put("RADIO_DROP_RATE_NO_HV", "无线掉话率(不含切换)");
		};
		{
			put("ICM", "干扰系数");
		};
		{
			put("HANDOUT_SUC_RATE", "切出成功率");
		};
		{
			put("HANDIN_SUC_RATE", "切入成功率");
		};
		{
			put("HANDOVER_SUC_RATE", "切换成功率");
		};
		{
			put("PS_RADIO_USE_RATE", "PS无线利用率");
		};
		{
			put("PDCH_CARRYING_EFFICIENCY", "PDCH承载效率（kbps）");
		};
		{
			put("DATA_TRAFFIC", "数据流量(MByte)");
		};
		{
			put("DOWNLINK_BPDCH_REUSE", "下行BPDCH复用度");
		};
		{
			put("DOWNLINK_EPDCH_REUSE", "下行EPDCH复用度");
		};
		{
			put("DOWNLINK_TBF_CONG_RATE", "下行TBF拥塞率");
		}
	};
	
	// excel title到dbfield的映射,由上面的db-》excel title得到
	private static Map<String, String> excelTitlesToDbFields = new HashMap<String, String>();
	// 只存在于中间表，不存在目标表的字段
	private static List<String> fieldsNotExistInTargetTab = Arrays.asList(
			"NET_TYPE", "SPEC_TYPE", "STS_DATE","STS_PERIOD","AREA_ID");
	
	// 数据库字段的类型
	Map<String, String> dbFieldsType = new HashMap<String, String>()
	{
		/*{
			put("DESCRIPTOR_ID", "Long");
		};*/
		{
			put("STS_DATE", "Date");
		};
		{
			put("STS_PERIOD", "String");
		};
		{
			put("BSC_ID", "Long");
		};
		{
			put("CELL", "String");
		};
		{
			put("CELL_CHINESE_NAME", "String");
		};
		{
			put("TCH_INTACT_RATE", "Float");
		};
		{
			put("DECLARE_CHANNEL_NUMBER", "Float");
		};
		{
			put("AVAILABLE_CHANNEL_NUMBER", "Float");
		};
		{
			put("CARRIER_NUMBER", "Float");
		};
		{
			put("RESOURCE_USE_RATE", "Float");
		};
		{
			put("TRAFFIC", "Float");
		};
		{
			put("TRAFFIC_PER_LINE", "Float");
		};
		{
			put("ACCESS_OK_RATE", "Float");
		};
		{
			put("RADIO_ACCESS", "Float");
		};
		{
			put("DROP_CALL_NUM_TOGETHER", "Float");
		};
		{
			put("RADIO_DROP_RATE_NO_HV", "Float");
		};
		{
			put("ICM", "Float");
		};
		{
			put("HANDOUT_SUC_RATE", "Float");
		};
		{
			put("HANDIN_SUC_RATE", "Float");
		};
		{
			put("HANDOVER_SUC_RATE", "Float");
		};
		{
			put("PS_RADIO_USE_RATE", "Float");
		};
		{
			put("PDCH_CARRYING_EFFICIENCY", "Float");
		};
		{
			put("DATA_TRAFFIC", "Float");
		};
		{
			put("DOWNLINK_BPDCH_REUSE", "Float");
		};
		{
			put("DOWNLINK_EPDCH_REUSE", "Float");
		};
		{
			put("DOWNLINK_TBF_CONG_RATE", "Float");
		}
	};
	
	// 构建insert语句
	private static String tempTable = "RNO_TEMP_STS";
	private static String insertInoMidTableSql = "";
	private static StringBuffer insertTempBuffer = new StringBuffer();
	// 在insert语句中出现的字段名称
	private static List<String> seqenceDbFields = new ArrayList<String>();
	// 每个db字段在sql出现的位置，从1开始
	private static Map<String, Integer> seqenceDbFieldsPosition = new HashMap<String, Integer>();

	private static Set<String> dbFields = dbFieldsToExcelTitles.keySet();
	static {
		insertInoMidTableSql = "insert into " + tempTable + " (";
		int poi = 1;
		for (String f : dbFields) {
			insertInoMidTableSql += f + ",";
			seqenceDbFields.add(f);
			seqenceDbFieldsPosition.put(f, poi++);

			// excel title 到 db field 的映射
			excelTitlesToDbFields.put(dbFieldsToExcelTitles.get(f), f);
			// if(poi==6){
			// log.info("=======================6666666========="+f+","+dbFieldsToExcelTitles.get(f));
			// }
		}
		// 额外的
		insertInoMidTableSql += "NET_TYPE,SPEC_TYPE,AREA_ID";
		seqenceDbFieldsPosition.put("NET_TYPE", poi++);
		seqenceDbFieldsPosition.put("SPEC_TYPE", poi++);
		seqenceDbFieldsPosition.put("AREA_ID", poi++);
		/*for(String f : fieldsNotExistInTargetTab)
		{
			insertInoMidTableSql += f + ",";
			seqenceDbFields.add(f);
			seqenceDbFieldsPosition.put(f, poi++);
		}*/

		insertInoMidTableSql += " ) values(";
		for (int i = 0; i < dbFields.size(); i++) {
			insertInoMidTableSql += "?,";
		}
		insertInoMidTableSql += "?,?,?)";
		/*for(int i = 0; i < fieldsNotExistInTargetTab.size(); i++)
		{
			insertInoMidTableSql += "?,";
		}
		insertInoMidTableSql += ")";*/

	}
	
	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("BSC","CELL","DATE","PERIOD");

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
		super.setFileParserManager(fileParserManager);
	}

	public void setCache(MemcachedClient memCached) {
		this.memCached = memCached;
		super.setMemCached(memCached);
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	public void setRnoBscDao(RnoBscDao rnoBscDao) {
		this.rnoBscDao = rnoBscDao;
	}

	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入CellFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId
				+ ",autoload=" + autoload + ",attachParams=" + attachParams);
		log.info("进入CellFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId
				+ ",autoload=" + autoload + ",attachParams=" + attachParams);

		// -----------------------前期检验------------------------------------//
		// 获取全部数据
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);

		if (allDatas == null || allDatas.size() < 1) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！因为文件是空的");
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			return false;
		}

		// 2014-1-6 Liang YJ add 将excel标题的序号列出来
		Map<String, Integer> excelTitlesColumn = new HashMap<String, Integer>();
		StringBuilder checkMsg = new StringBuilder();
		int colCount = calculateExcelTitleColumn(allDatas.get(0),
				excelTitlesColumn, checkMsg);
		log.info("计算标题所在列情况，总共有标题列：" + colCount + ",msg=" + checkMsg.toString());
		if (colCount <= 0) {
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					checkMsg.toString());
			return false;
		}
		// 检查必须的字段是否存在
		checkMsg.setLength(0);
		boolean howTitle = checkIfExcelTitleValide(excelTitlesColumn,
				mandatoryExcelTitles, checkMsg);
		log.info("检查标题情况:" + howTitle + ",msg=" + checkMsg.toString());
		if (!howTitle) {
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					checkMsg.toString());
			return false;
		}

		int total = allDatas.size() - 1;// excel有效记录数
		if(total<=0){
			log.error("excel表未包含有效数据");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"excel表未包含有效数据");
			return false;
		}
		log.info("excel总行数：" + (total + 1));
		List<String> notExistExcelTitles = getNotListExcelTitles(excelTitlesColumn);
		// ----------------------准备数据库相关条件-----------------------------------//
		/*Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}*/

		// 创建临时表,提交后删除数据
		//String tempTable = "RNO_TEMP_STS";
		/*
		 * String fieldsDeclare = "NET_TYPE VARCHAR2(16),"
		 * +" SPEC_TYPE VARCHAR2(32)," +" AREA_ID NUMBER NOT NULL," +
		 * "DESCRIPTOR_ID        NUMBER               ," +
		 * "STS_DATE             DATE                 not null," +
		 * "STS_PERIOD           VARCHAR2(16 BYTE)    not null," +
		 * "BSC_ID               NUMBER    not null," +
		 * "CELL                 VARCHAR2(16 BYTE)    not null," +
		 * "CELL_CHINESE_NAME VARCHAR2(32 BYTE)," + "TCH_INTACT_RATE NUMBER, " +
		 * "DECLARE_CHANNEL_NUMBER NUMBER, " +
		 * "AVAILABLE_CHANNEL_NUMBER NUMBER, " + "CARRIER_NUMBER NUMBER, " +
		 * "RESOURCE_USE_RATE NUMBER, " + "TRAFFIC NUMBER, " +
		 * "TRAFFIC_PER_LINE NUMBER, " + "ACCESS_OK_RATE NUMBER, " +
		 * "RADIO_ACCESS NUMBER, " + "DROP_CALL_NUM_TOGETHER NUMBER, " +
		 * "RADIO_DROP_RATE_NO_HV NUMBER, " + "ICM NUMBER, " +
		 * "HANDOUT_SUC_RATE NUMBER, " + "HANDIN_SUC_RATE NUMBER, " +
		 * "HANDOVER_SUC_RATE NUMBER, " + "PS_RADIO_USE_RATE NUMBER, " +
		 * "PDCH_CARRYING_EFFICIENCY NUMBER, " + "DATA_TRAFFIC NUMBER, " +
		 * "DOWNLINK_BPDCH_REUSE NUMBER, " + "DOWNLINK_EPDCH_REUSE NUMBER, " +
		 * "DOWNLINK_TBF_CONG_RATE NUMBER";
		 */
		


		// 会话临时表
		/**
		 * String createTableSql = "create global temporary table " + tempTable +
		 * " (" + fieldsDeclare + ") on commit preserve rows"; String
		 * dropTableSql = "drop table " + tempTable; try { int createResult =
		 * statement .executeUpdate(createTableSql);
		 * log.info("createTableResult=" + createResult); } catch (SQLException
		 * e) { e.printStackTrace(); }
		 */

		// 批量插入
		PreparedStatement insertSqlpstmt = null;
		// 插入到临时表的语句格式
//		String insertTmpTableSql = "insert into "
//				+ tempTable
//				+ "(NET_TYPE,SPEC_TYPE,AREA_ID,STS_DATE,STS_PERIOD, BSC_ID, CELL, CELL_CHINESE_NAME, TCH_INTACT_RATE, DECLARE_CHANNEL_NUMBER, AVAILABLE_CHANNEL_NUMBER, CARRIER_NUMBER, RESOURCE_USE_RATE, TRAFFIC, TRAFFIC_PER_LINE, ACCESS_OK_RATE, RADIO_ACCESS, DROP_CALL_NUM_TOGETHER, "
//				+ " RADIO_DROP_RATE_NO_HV, ICM, HANDOUT_SUC_RATE, HANDIN_SUC_RATE, HANDOVER_SUC_RATE, PS_RADIO_USE_RATE, PDCH_CARRYING_EFFICIENCY, DATA_TRAFFIC, DOWNLINK_BPDCH_REUSE, DOWNLINK_EPDCH_REUSE, DOWNLINK_TBF_CONG_RATE) "
//				+ " values (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		log.info("插入临时表的sql语句格式：" + insertInoMidTableSql);
		try {
			insertSqlpstmt = connection.prepareStatement(insertInoMidTableSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}
		// -------------------循环处理数据------------------------------------------------//
		List<String> oneData = null;

		List<RnoBsc> bscs = rnoBscDao.getAllBsc();
		Map<String, Long> bscNameToId = new HashMap<String, Long>();
		log.info("bscNameToId.size: "+bscNameToId.size());
		if (bscs != null && !bscs.isEmpty()) {
			for (RnoBsc bs : bscs) {
				bscNameToId.put(bs.getEngname(), bs.getBscId());
			}
		}

		int index = 0;
		TmpSts tmpSts = null;
		int insertSuccessCnt = 0,  failCnt = 0;
		String msg = "";
		StringBuilder buf = new StringBuilder();
		log.info("插入中间表的sql=" + insertInoMidTableSql);
		log.info("allDatas.size: " + allDatas.size());
		log.info("开始循环");
		for (int i = 1; i < allDatas.size(); i++)
		{
			log.info("cicle: "+i);
			super.fileParserManager.updateTokenProgress(token, i/total*0.85f);
			oneData = allDatas.get(i);
			if(oneData != null)
			{
				log.info("oneData:"+oneData);
			}
			if (oneData == null || oneData.size() < mandatoryExcelTitles.size()) {
				msg = "第[" + (i+1) + "]行数据不齐全！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
			}
			// 填充

			// NET_TYPE,SPEC_TYPE,AREA_ID,STS_DATE,STS_PERIOD, BSC_ID, CELL,
			// CELL_CHINESE_NAME, TCH_INTACT_RATE, DECLARE_CHANNEL_NUMBER,
			// AVAILABLE_CHANNEL_NUMBER, CARRIER_NUMBER, RESOURCE_USE_RATE,
			// TRAFFIC, TRAFFIC_PER_LINE, ACCESS_OK_RATE, RADIO_ACCESS,
			// DROP_CALL_NUM_TOGETHER, "
			// RADIO_DROP_RATE_NO_HV, ICM, HANDOUT_SUC_RATE, HANDIN_SUC_RATE,
			// HANDOVER_SUC_RATE, PS_RADIO_USE_RATE, PDCH_CARRYING_EFFICIENCY,
			// DATA_TRAFFIC, DOWNLINK_BPDCH_REUSE, DOWNLINK_EPDCH_REUSE,
			// DOWNLINK_TBF_CONG_RATE
			
			/*String netType;
			String specType;*/
			String stsDateStr;
			java.sql.Date stsDate;
			String stsPeriod;
			Long descriptorId;
			Long bscId;
			String bsc;
			String cell;
			/*Float tchIntactRate;
			Float declareChannelNumber;
			Float availableChannelNumber;
			Float carrierNumber;
			Float resourceUseRate;
			Float traffic;
			Float trafficPerLine;
			Float accessOkRate;
			Float radioAccess;
			Float dropCallNumTogether;
			Float radioDropRateNoHv;
			Float icm;
			Float handoutSucRate;
			Float handinSucRate;
			Float handoverSucRate;
			Float psRadioUseRate;
			Float pdchCarryingEfficiency;
			Float dataTraffic;
			Float downlinkBpdchReuse;
			Float downlinkEpdchReuse;
			Float downlinkTbfCongRate;*/
			
			// 检查cell是否合法
			log.info("excelTitleColumn: "+ excelTitlesColumn);
			cell = oneData.get(excelTitlesColumn.get("CELL"));
			log.info("cell: "+cell);
			if (null == cell || cell.trim().isEmpty())
			{
				msg = "第[" + (i+1) + "]行所的CELL没有值";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			// 检查bsc是否合法
			bsc = oneData.get(excelTitlesColumn.get("BSC"));
			if (null != bsc && !bsc.trim().isEmpty()) {
				bscId = bscNameToId.get(bsc);
				log.info("bssId: "+bscId);
				oneData.set(excelTitlesColumn.get("BSC"), bscId.toString());
			}
			else
			{
				msg = "第[" + (i+1) + "]行所的BSC没有值";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			
			// 检查date是否合法
			stsDateStr = oneData.get(excelTitlesColumn.get("DATE"));
			if (null == stsDateStr || stsDateStr.trim().isEmpty())
			{
				msg = "第[" + (i+1) + "]行所的DATE没有值";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			else if (!isValidDate(stsDateStr))
			{
				msg = "第[" + (i+1) + "]行所的DATE值不合法";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			
			// 检查date是否合法
			stsPeriod = oneData.get(excelTitlesColumn.get("PERIOD"));
			if (null == stsPeriod || stsPeriod.trim().isEmpty())
			{
				msg = "第[" + (i+1) + "]行所的DATE没有值";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}

			// 设置语句
			// 对于excel里出现的都进行设置，对于没有在excel出现的，则设置为null
			boolean excelLineOk = false;
			log.info("excelTitleColumn.keySet: "+excelTitlesColumn.keySet());
			for (String t : excelTitlesColumn.keySet())
			{
					excelLineOk = setPreparedStatementValue(insertSqlpstmt,
							excelTitlesToDbFields.get(t),
							oneData.get(excelTitlesColumn.get(t)),
							dbFieldsType.get(excelTitlesToDbFields.get(t)));
					if (excelLineOk == false) {
						failCnt++;
						log.warn("第[" + (i+1) + "]行的列：[" + t + "]处理出错！");
						break;
					}
			}
			// 补充未出现的数据
			if (notExistExcelTitles.size() > 0) {
				for (String s : notExistExcelTitles) {
					excelLineOk = setPreparedStatementValue(insertSqlpstmt,
							excelTitlesToDbFields.get(s), null,
							dbFieldsType.get(excelTitlesToDbFields.get(s)));
					if (excelLineOk == false) {
						failCnt++;
						break;
					}
				}
			}
			try
			{
				insertSqlpstmt.setString(seqenceDbFieldsPosition.get("NET_TYPE"),NET_TYPE);
			} catch (SQLException e1)
			{
				e1.printStackTrace();
				excelLineOk = false;
				failCnt++;
				log.warn("第[" + (i+1) + "]行的NET_TYPE处理出错！");
				continue;
			}
			try
			{
				insertSqlpstmt.setString(seqenceDbFieldsPosition.get("SPEC_TYPE"),SPEC_TYPE);
			} catch (SQLException e1)
			{
				e1.printStackTrace();
				excelLineOk = false;
				failCnt++;
				log.warn("第[" + (i+1) + "]行的SPEC_TYPE处理出错！");
				continue;
			}
			try
			{
				insertSqlpstmt.setLong(seqenceDbFieldsPosition.get("AREA_ID"), areaId);
			} catch (SQLException e1)
			{
				e1.printStackTrace();
				excelLineOk = false;
				failCnt++;
				log.warn("第[" + (i+1) + "]行的AREA_ID处理出错！");
				continue;
			}
			
			
			if(excelLineOk == true)
			{
				try {
					insertSqlpstmt.addBatch();
					insertSuccessCnt++;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					failCnt++;
				}
			}
		}

		// --------------------插入临时表-------------------//
		log.info("开始插入临时表");
		// 执行插入临时表
		if (insertSuccessCnt > 0) {
			// 执行
			try {
				insertSqlpstmt.executeBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				msg = "数据保存时出错！code=302";
				log.error(msg);
				buf.append(msg + "<br/>");
				return false;
			}
		}

		// --------------------更新临时表--------------------//
		// 更新临时表，将已有的描述符id写上
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<StsConfig> existsDescriptors = new ArrayList<StsConfig>();// 已经存在的时间段的descriptor_id的集合
		String updateTmpSql = "update "
				+ tempTable
				+ " set DESCRIPTOR_ID=(select STS_DESC_ID from RNO_STS_DESCRIPTOR where TO_CHAR("
				+ tempTable
				+ ".STS_DATE,'YYYY-MM-DD')=TO_CHAR(RNO_STS_DESCRIPTOR.STS_DATE,'YYYY-MM-DD') AND "
				+ tempTable
				+ ".STS_PERIOD=RNO_STS_DESCRIPTOR.STS_PERIOD AND RNO_STS_DESCRIPTOR.AREA_ID="
				+ tempTable + ".AREA_ID AND RNO_STS_DESCRIPTOR.NET_TYPE="
				+ tempTable + ".NET_TYPE AND RNO_STS_DESCRIPTOR.SPEC_TYPE="
				+ tempTable + ".SPEC_TYPE)";
		int existCnt = 0;
		log.info("更新临时表，将已有的描述符id写上的sql=" + updateTmpSql);
		try {
			existCnt = statement.executeUpdate(updateTmpSql);
			log.info("更新临时表已有时间段的数据项的数量： " + existCnt);

			// 准备获取已有的配置id
			if (existCnt > 0 && autoload) {
				getDescDetailForLoad(tempTable, statement, existsDescriptors);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 无法进行下去了，进行清除。
			// TODO
			fail(connection);
			return false;
		}

		fileParserManager.updateTokenProgress(token, 0.6f);
		
		// ----------1、插入新的时间段------------//
		// 插入新的时间段的标识语句
		String insertIntoStsDesc = "insert into RNO_STS_DESCRIPTOR(STS_DESC_ID,NET_TYPE,SPEC_TYPE,STS_DATE,STS_PERIOD,CREATE_TIME,MOD_TIME,STATUS,AREA_ID) VALUES(SEQ_RNO_STS_DESCRIPTOR.NEXTVAL,'"+NET_TYPE+"','"+SPEC_TYPE+"',?,?,?,?,'N',"
				+ areaId + ")";
		PreparedStatement insertDescStatement = null;
		// 创建新的描述项
		String createNewDescSql = "select distinct \"TO_CHAR\"(STS_DATE,'YYYY-MM-DD'),STS_PERIOD,AREA_ID  from "
				+ tempTable + " where DESCRIPTOR_ID is null";
		ResultSet newDescResultSet = null;
		int newDescCnt = 0;// 新增的时间段描述数量
		try {
			newDescResultSet = statement.executeQuery(createNewDescSql);
			String tmpDateStr = null;
			String period = "";
			java.sql.Timestamp createTime = new java.sql.Timestamp(
					new Date().getTime());
			java.sql.Date stsDate = null;
			while (newDescResultSet.next()) {
				if (insertDescStatement == null) {
					// 准备preparestatement
					insertDescStatement = connection
							.prepareStatement(insertIntoStsDesc);
				}
				newDescCnt++;
				tmpDateStr = newDescResultSet.getString(1);
				stsDate = java.sql.Date.valueOf(tmpDateStr);
				period = newDescResultSet.getString(2);

				insertDescStatement.setDate(1, stsDate);
				insertDescStatement.setString(2, period);
				insertDescStatement.setTimestamp(3, createTime);
				insertDescStatement.setTimestamp(4, createTime);
				insertDescStatement.addBatch();
			}
			if (insertDescStatement != null) {
				int[] insertNewDescResult = insertDescStatement.executeBatch();
				log.info("插入新的时间段描述的结果： " + insertNewDescResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection);
			return false;
		} finally {
			if (newDescResultSet != null) {
				try {
					newDescResultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (insertDescStatement != null) {
				try {
					insertDescStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		fileParserManager.updateTokenProgress(token, 0.9f);
		
		// ----------2、用得到的时间段的描述id更新临时表---//
		log.info("新增：" + newDescCnt + "个时间段描述！");
		if (newDescCnt > 0)
		{
			String updateNewDataDescriptorId = "merge into "
					+ tempTable
					+ " tt USING RNO_STS_DESCRIPTOR tb on(\"TO_CHAR\"(tt.STS_DATE,'YYYY-MM-DD')=\"TO_CHAR\"(tb.STS_DATE,'YYYY-MM-DD') AND tt.AREA_ID=tb.AREA_ID AND tt.STS_PERIOD=tb.STS_PERIOD AND tt.NET_TYPE=tb.NET_TYPE AND tt.SPEC_TYPE=tb.SPEC_TYPE)"
					+ " when matched THEN update set tt.DESCRIPTOR_ID=tb.STS_DESC_ID";
			log.info("用新创建的sts描述项id更新临时表的DESCRIPTOR_ID的sql语句："
					+ updateNewDataDescriptorId);
			try
			{
				int updateNewDataDescriptorIdCnt = statement
						.executeUpdate(updateNewDataDescriptorId);
				log.info("用新创建的sts描述项id更新临时表的DESCRIPTOR_ID的记录数量："
						+ updateNewDataDescriptorIdCnt);
			} catch (SQLException e)
			{
				e.printStackTrace();
				fail(connection);
				return false;
			}

			// 获取新增的时间段的描述id
			if (autoload) {
				try {
					getDescDetailForLoad(tempTable, statement, existsDescriptors);
				} catch (SQLException e) {
					e.printStackTrace();
					fail(connection);
					return false;
				}
			}
			fileParserManager.updateTokenProgress(token, 0.95f);
		}
		
		//-------------3.向目标表插入新数据----------------------//
		//Liang YJ 2014.1.7 增加
		Statement mergeStatement = null;
		try {
			mergeStatement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=303");
			return false;
		}
		String targetTable = "RNO_STS";
		String targetSequence = "SEQ_RNO_STS";
		String mergeSql = "merge into " + targetTable + " targ using " + tempTable 
		+ " mid on(targ.CELL = mid.CELL and targ.DESCRIPTOR_ID = mid.DESCRIPTOR_ID and targ.BSC_ID = mid.BSC_ID) ";
		//覆盖(更新信息)
		if(update)
		{
			mergeSql += "when matched then update set ";
			for(String field : dbFields)
			{
				if(!"STS_DATE".equalsIgnoreCase(field) && !"STS_PERIOD".equalsIgnoreCase(field) && !"CELL".equalsIgnoreCase(field) && !"BSC_ID".equalsIgnoreCase(field))
				{
					mergeSql += "targ." + field + "=" + "mid." + field + ",";
				}
			}
			mergeSql = mergeSql.substring(0,mergeSql.length()-1);
		}
		mergeSql += " when not matched then insert(STS_ID,";
		for(String field : dbFields)
		{
			if(!"STS_DATE".equalsIgnoreCase(field) && !"STS_PERIOD".equalsIgnoreCase(field))
			{
				mergeSql += field + ",";
			}
		}
		mergeSql += "DESCRIPTOR_ID)values(" + targetSequence + ".NEXTVAL,";
		for(String field : dbFields)
		{
			if(!"STS_DATE".equalsIgnoreCase(field) && !"STS_PERIOD".equalsIgnoreCase(field))
			{
				mergeSql += "mid." + field + ",";
			}
		}
		mergeSql += "mid.DESCRIPTOR_ID)";
		int updateSuccessCnt = 0;
		try
		{
			updateSuccessCnt = mergeStatement.executeUpdate(mergeSql);
		} catch (SQLException e1)
		{
			e1.printStackTrace();
			msg = "执行用中间表[" + tempTable + "]的数据更新目标表[" + targetTable
					+ "]的时候出错！sql=" + mergeSql;
			log.error(msg);
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=318");
			return false;
		}
		
		// ------------判断 是否加入自动配置---------//

		if (autoload) {
			//System.out.println("autoload="+autoload);
			log.info("需要自动加载到分析列表");
			if (existsDescriptors.size() > 0) {
				//System.out.println("existsDescriptors.size()="+existsDescriptors.size());
				if (attachParams == null) {
					log.error("未带有session信息，不能支持添加到分析列表！");
				} else {
					HttpSession session = (HttpSession) attachParams
							.get("session");
					if (session == null) {
						log.error("session为空！");
					} else {
						log.info("准备添加到分析列表");
						List<StsConfig> stsConfigs = null;
						stsConfigs = (ArrayList<StsConfig>) session
								.getAttribute(RnoConstant.SessionConstant.STS_LOAD_CONFIG_ID);
						if (stsConfigs == null) {
							stsConfigs = new ArrayList<StsConfig>();
						}
						for(StsConfig sc:existsDescriptors){
							if(!stsConfigs.contains(sc)){
								stsConfigs.add(sc);
							}
						}
						session.setAttribute(
								RnoConstant.SessionConstant.STS_LOAD_CONFIG_ID,
								stsConfigs);
					}
				}
			}
		}
		
		String result = "{'msg':'共["
				+ total+"]条记录，成功更新[" + updateSuccessCnt+"]条,失败[" + failCnt + "]条。<br/>";
		result += buf.toString()+"'";
		
		if(autoload){
			String conList=super.gson.toJson(existsDescriptors);
			result+=",'list':"+conList;
		}
		result+="}";
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	/**
	 * 将临时表里存在的desc提取出来
	 * 
	 * @param tmpTable
	 * @param statement
	 * @param existsDescriptors
	 * @throws SQLException
	 * @author brightming 2013-10-19 上午11:58:01
	 */
	private void getDescDetailForLoad(String tmpTable, Statement statement,
			List<StsConfig> existsDescriptors) throws SQLException {
		String getExistDescIdSql = "select a.STS_DESC_ID,a.AREA_ID,b.\"NAME\",\"TO_CHAR\"(a.STS_DATE,'YYYY/MM/DD'),a.STS_PERIOD from RNO_STS_DESCRIPTOR a,AREA b where a.AREA_ID=b.ID and a.STS_DESC_ID in (  select distinct(DESCRIPTOR_ID) from "
				+ tmpTable + ")";
		log.info("获取已存在的时间段的配置id的sql：" + getExistDescIdSql);
		ResultSet tmpSet = null;
		tmpSet = statement.executeQuery(getExistDescIdSql);
		StsConfig sc = null;
		StsAnaItemDetail item = null;
		String period = "";
		while (tmpSet.next()) {
			sc = new StsConfig();
			sc.setConfigId(tmpSet.getLong(1));
			sc.setFromQuery(false);
			sc.setSelected(false);
			sc.setStsCondition(null);

			item = new StsAnaItemDetail();
			item.setAreaId(tmpSet.getLong(2));
			item.setAreaName(tmpSet.getString(3));
			item.setStsDate(tmpSet.getString(4));
			if (SPEC_TYPE
					.equals(RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_AUDIO)) {
				item.setStsType("GSM小区语音业务指标");
			} else if (SPEC_TYPE
					.equals(RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_DATA)) {
				item.setStsType("GSM小区数据业务指标");
			}
			period = tmpSet.getString(5);
			item.setPeriodType(getPeriodType(period));

			sc.setStsAnaItemDetail(item);
			existsDescriptors.add(sc);
		}
		tmpSet.close();
	}

	/**
	 * 根据输入获取是小时指标还是天指标
	 * 
	 * @param period
	 * @return
	 * @author brightming 2013-10-19 上午11:39:02
	 */
	private String getPeriodType(String period) {
		if (period == null || period.trim().isEmpty()) {
			return "不规则指标";
		}

		String[] ps = period.split("-");
		if (ps.length == 2) {
			String s1 = ps[0];
			String s2 = ps[1];
			int i1 = 0;
			int i2 = 0;
			try {
				i1 = Integer.parseInt(s1);
				i2 = Integer.parseInt(s2);
				if ((i2 - i1) == 100 || (i1 == 2300 && i2 == 0)) {
					return "小时指标";
				} else {
					return "天指标";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "不规则指标";
			}
		}

		return "不规则指标";

	}

	/**
	 * 中间执行出错后的处理
	 * 
	 * @param connection
	 * @author brightming 2013-10-18 下午6:28:12
	 */
	private void fail(Connection connection) {
		log.error("中间出错！");
		try {
			connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	/**
	 * 获取set语句
	 * 
	 * @param indexFields
	 *            以","分隔的字段名
	 * @param ta
	 *            表a的名称
	 * @param tb
	 *            表b的名称
	 * @return
	 * @author brightming 2013-10-18 下午4:55:59
	 */
	private String getSetSql(String indexFields, String ta, String tb) {
		String[] ifs = indexFields.split(",");
		StringBuilder buf = new StringBuilder();
		for (String s : ifs) {
			buf.append(ta + "." + s + "=" + tb + "." + s + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();

	}

	/**
	 * * 判断数据有效性，如果数据有效，返回创建的话统数据；否则返回null
	 * 
	 * @author ou.jh
	 * @date Oct 10, 2013 11:53:34 AM
	 * @Description: TODO
	 * @param @param oneData
	 * @param @param i
	 * @param @param buf
	 * @param @param bscsResideInArea
	 * @param @param areaId
	 * @param @return
	 * @throws
	 */
	private TmpSts createRnoStsFromExcelLine(List<String> oneData, int i,
			StringBuilder buf, Map<String, Long> bscsResideInArea, Long areaId) {
		Long bscId = null;
		String bsc = null;
		String cell = null;
		java.sql.Date stsDate = null;
		String stsPeriod = null;
		String cellChineseName = null;
		Float tchIntactRate = null;
		Float declareChannelNumber = null;
		Float availableChannelNumber = null;
		Float carrierNumber = null;
		Float resourceUseRate = null;
		Float traffic = null;
		Float trafficPerLine = null;
		Float accessOkRate = null;
		Float radioAccess = null;
		Float dropCallNumTogether = null;
		Float radioDropRateNoHv = null;
		Float icm = null;
		Float handoutSucRate = null;
		Float handinSucRate = null;
		Float handoverSucRate = null;
		Float psRadioUseRate = null;
		Float pdchCarryingEfficiency = null;
		Float dataTraffic = null;
		Float downlinkBpdchReuse = null;
		Float downlinkEpdchReuse = null;
		Float downlinkTbfCongRate = null;
		boolean insertRnoBsc = true;
		String msg = "";
		// --------开始 检查数据有效性-----------------------------------------------//
		if (oneData == null) {
			msg = "第[" + (i + 1) + "]行错误！数据为空！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		if (oneData.size() < titleSize) {
			msg = "第[" + (i + 1) + "]行错误！数据不齐全！";
			buf.append(msg + "<br/>");
			return null;
		}

		// -------------检查数据有效性-----------------------//
		// 统计日期
		if (oneData.get(0) == null) {
			msg = "第[" + (i + 1) + "]行错误！未包含统计日期！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		//log.info("stsDate=" + oneData.get(0));
		try {
			stsDate = new java.sql.Date(sdf1.parse(oneData.get(0)).getTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
			try {
				stsDate = new java.sql.Date(sdf2.parse(oneData.get(0))
						.getTime());
			} catch (ParseException e) {
				
				try{
					stsDate=new java.sql.Date(sdf3.parse(oneData.get(0)).getTime());
				}catch(ParseException e2){
					e.printStackTrace();
					msg = "第[" + (i + 1) + "]行错误！统计时间的格式不对！";
					log.warn(msg);
					buf.append(msg + "<br/>");
					return null;
				}
			}
		}
		// stsDate = java.sql.Date.valueOf(oneData.get(0) + "");

		// 统计时间段
		stsPeriod = oneData.get(1);
		if (stsPeriod == null || stsPeriod.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！未包含统计时间段！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		if (!pattern.matcher(stsPeriod).find()) {
			msg = "第[" + (i + 1) + "]行错误！统计时间段不符合格式要求:xx00-xx00！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// 检查数据有效性
		bsc = oneData.get(2);
		if (bsc == null || bsc.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含bsc！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// 判断BSC是否存在
		insertRnoBsc = false;
		if (!bscsResideInArea.containsKey(bsc)) {
			insertRnoBsc = true;
		} else {
			bscId = bscsResideInArea.get(bsc);
		}

		if (insertRnoBsc) {
			RnoBsc rnoBsc = new RnoBsc();
			rnoBsc.setEngname(bsc);
			rnoBsc.setChinesename(bsc);
			msg = "新建'" + bsc + "'bsc";
			log.info(msg);
			bscId = this.rnoBscDao.insertBsc(areaId, rnoBsc); // 此处是会提交的，不在事务内。
			rnoBsc.setBscId(bscId);
			bscsResideInArea.put(bsc, bscId);
		}
		// cell
		cell = oneData.get(3);
		if (cell == null || cell.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含小CELL！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// cellChineseName
		cellChineseName = oneData.get(4);
		if (cellChineseName == null || cellChineseName.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含小区！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// tchIntactRate
		try {
			if (oneData.get(5) == null || oneData.get(5).trim().equals("")) {
				tchIntactRate = null;
			} else if (oneData.get(5) != null
					&& !oneData.get(5).trim().equals("")) {
				tchIntactRate = Float.parseFloat(oneData.get(5));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的T完好率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// declareChannelNumber
		try {
			if (oneData.get(6) == null || oneData.get(6).trim().equals("")) {
				declareChannelNumber = null;
			} else if (oneData.get(6) != null
					&& !oneData.get(6).trim().equals("")) {
				declareChannelNumber = Float.parseFloat(oneData.get(6));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的定义信道！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// availableChannelNumber
		try {
			if (oneData.get(7) == null || oneData.get(7).trim().equals("")) {
				availableChannelNumber = null;
			} else if (oneData.get(7) != null
					&& !oneData.get(7).trim().equals("")) {
				availableChannelNumber = Float.parseFloat(oneData.get(7));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的可用信道！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// carrierNumber
		try {
			if (oneData.get(8) == null || oneData.get(8).trim().equals("")) {
				carrierNumber = null;
			} else if (oneData.get(8) != null
					&& !oneData.get(8).trim().equals("")) {
				carrierNumber = Float.parseFloat(oneData.get(8));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的载波数！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// resourceUseRate
		try {
			if (oneData.get(9) == null || oneData.get(9).trim().equals("")) {
				resourceUseRate = null;
			} else if (oneData.get(9) != null
					&& !oneData.get(9).trim().equals("")) {
				resourceUseRate = Float.parseFloat(oneData.get(9));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的无线资源利用率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// traffic
		try {
			if (oneData.get(10) == null || oneData.get(10).trim().equals("")) {
				traffic = null;
			} else if (oneData.get(10) != null
					&& !oneData.get(10).trim().equals("")) {
				traffic = Float.parseFloat(oneData.get(10));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的话务量！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// trafficPerLine
		try {
			if (oneData.get(11) == null || oneData.get(11).trim().equals("")) {
				trafficPerLine = null;
			} else if (oneData.get(11) != null
					&& !oneData.get(11).trim().equals("")) {
				trafficPerLine = Float.parseFloat(oneData.get(11));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的每线话务量！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// accessOkRate
		try {
			if (oneData.get(12) == null || oneData.get(12).trim().equals("")) {
				accessOkRate = null;
			} else if (oneData.get(12) != null
					&& !oneData.get(12).trim().equals("")) {
				accessOkRate = Float.parseFloat(oneData.get(12));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的接通率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// radioAccess
		try {
			if (oneData.get(13) == null || oneData.get(13).trim().equals("")) {
				radioAccess = null;
			} else if (oneData.get(13) != null
					&& !oneData.get(13).trim().equals("")) {
				radioAccess = Float.parseFloat(oneData.get(13));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的无线接入性！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// dropCallNumTogether
		try {
			if (oneData.get(14) == null || oneData.get(14).trim().equals("")) {
				dropCallNumTogether = null;
			} else if (oneData.get(14) != null
					&& !oneData.get(14).trim().equals("")) {
				dropCallNumTogether = Float.parseFloat(oneData.get(14));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的总掉话！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// radioDropRateNoHv
		try {
			if (oneData.get(15) == null || oneData.get(15).trim().equals("")) {
				radioDropRateNoHv = null;
			} else if (oneData.get(15) != null
					&& !oneData.get(15).trim().equals("")) {
				radioDropRateNoHv = Float.parseFloat(oneData.get(15));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的无线掉话率（不含切换）！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// icm
		try {
			if (oneData.get(16) == null || oneData.get(16).trim().equals("")) {
				icm = null;
			} else if (oneData.get(16) != null
					&& !oneData.get(16).trim().equals("")) {
				icm = Float.parseFloat(oneData.get(16));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的干扰系数！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// handoutSucRate
		try {
			if (oneData.get(17) == null || oneData.get(17).trim().equals("")) {
				handoutSucRate = null;
			} else if (oneData.get(17) != null
					&& !oneData.get(17).trim().equals("")) {
				handoutSucRate = Float.parseFloat(oneData.get(17));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的切出成功率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// handinSucRate
		try {
			if (oneData.get(18) == null || oneData.get(18).trim().equals("")) {
				handinSucRate = null;
			} else if (oneData.get(18) != null
					&& !oneData.get(18).trim().equals("")) {
				handinSucRate = Float.parseFloat(oneData.get(18));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的切入成功率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// handoverSucRate
		try {
			if (oneData.get(19) == null || oneData.get(19).trim().equals("")) {
				handoverSucRate = null;
			} else if (oneData.get(19) != null
					&& !oneData.get(19).trim().equals("")) {
				handoverSucRate = Float.parseFloat(oneData.get(19));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的切入成功率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// psRadioUseRate
		try {
			if (oneData.get(20) == null || oneData.get(20).trim().equals("")) {
				psRadioUseRate = null;
			} else if (oneData.get(20) != null
					&& !oneData.get(20).trim().equals("")) {
				psRadioUseRate = Float.parseFloat(oneData.get(20));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的PS无线利用率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// pdchCarryingEfficiency
		try {
			if (oneData.get(21) == null || oneData.get(21).trim().equals("")) {
				pdchCarryingEfficiency = null;
			} else if (oneData.get(21) != null
					&& !oneData.get(21).trim().equals("")) {
				pdchCarryingEfficiency = Float.parseFloat(oneData.get(21));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的PDCH承载效率（kbps）！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// dataTraffic
		try {
			if (oneData.get(22) == null || oneData.get(22).trim().equals("")) {
				dataTraffic = null;
			} else if (oneData.get(22) != null
					&& !oneData.get(22).trim().equals("")) {
				dataTraffic = Float.parseFloat(oneData.get(22));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的数据流量（Mbyte）！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// downlinkBpdchReuse
		try {
			if (oneData.get(23) == null || oneData.get(23).trim().equals("")) {
				downlinkBpdchReuse = null;
			} else if (oneData.get(23) != null
					&& !oneData.get(23).trim().equals("")) {
				downlinkBpdchReuse = Float.parseFloat(oneData.get(23));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的下行BPDCH复用度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// downlinkEpdchReuse
		try {
			if (oneData.get(24) == null || oneData.get(24).trim().equals("")) {
				downlinkEpdchReuse = null;
			} else if (oneData.get(24) != null
					&& !oneData.get(24).trim().equals("")) {
				downlinkEpdchReuse = Float.parseFloat(oneData.get(24));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的下行EPDCH复用度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// downlinkTbfCongRate
		try {
			if (oneData.get(25) == null || oneData.get(25).trim().equals("")) {
				downlinkTbfCongRate = null;
			} else if (oneData.get(25) != null
					&& !oneData.get(25).trim().equals("")) {
				downlinkTbfCongRate = Float.parseFloat(oneData.get(25));
			}
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的下行TBF拥塞率！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		TmpSts rnoSts = new TmpSts();
		rnoSts.setAreaId(areaId);
		rnoSts.setStsDate(stsDate);
		rnoSts.setStsPeriod(stsPeriod);
		// rnoSts.setDescriptorId(descriptorId);
		rnoSts.setBscId(bscId);
		rnoSts.setCell(cell);
		rnoSts.setCellChineseName(cellChineseName);
		rnoSts.setTchIntactRate(tchIntactRate);
		rnoSts.setDeclareChannelNumber(declareChannelNumber);
		rnoSts.setAvailableChannelNumber(availableChannelNumber);
		rnoSts.setCarrierNumber(carrierNumber);
		rnoSts.setResourceUseRate(resourceUseRate);
		rnoSts.setTraffic(traffic);
		rnoSts.setTrafficPerLine(trafficPerLine);
		rnoSts.setAccessOkRate(accessOkRate);
		rnoSts.setRadioAccess(radioAccess);
		rnoSts.setDropCallNumTogether(dropCallNumTogether);
		rnoSts.setRadioDropRateNoHv(radioDropRateNoHv);
		rnoSts.setIcm(icm);
		rnoSts.setHandinSucRate(handinSucRate);
		rnoSts.setHandoutSucRate(handoutSucRate);
		rnoSts.setHandoverSucRate(handoverSucRate);
		rnoSts.setPsRadioUseRate(psRadioUseRate);
		rnoSts.setPdchCarryingEfficiency(pdchCarryingEfficiency);
		rnoSts.setDataTraffic(dataTraffic);
		rnoSts.setDownlinkBpdchReuse(downlinkBpdchReuse);
		rnoSts.setDownlinkEpdchReuse(downlinkEpdchReuse);
		rnoSts.setDownlinkTbfCongRate(downlinkTbfCongRate);

		return rnoSts;
	}

	class TmpSts {
		private Long areaId;
		private Long descriptorId;
		private Long bscId;
		private java.sql.Date stsDate;
		private String stsPeriod;
		private String cell;
		private String cellChineseName;
		private Float tchIntactRate;
		private Float declareChannelNumber;
		private Float availableChannelNumber;
		private Float carrierNumber;
		private Float resourceUseRate;
		private Float traffic;
		private Float trafficPerLine;
		private Float accessOkRate;
		private Float radioAccess;
		private Float dropCallNumTogether;
		private Float radioDropRateNoHv;
		private Float icm;
		private Float handoutSucRate;
		private Float handinSucRate;
		private Float handoverSucRate;
		private Float psRadioUseRate;
		private Float pdchCarryingEfficiency;
		private Float dataTraffic;
		private Float downlinkBpdchReuse;
		private Float downlinkEpdchReuse;
		private Float downlinkTbfCongRate;

		public Long getAreaId() {
			return areaId;
		}

		public void setAreaId(Long areaId) {
			this.areaId = areaId;
		}

		public Long getDescriptorId() {
			return descriptorId;
		}

		public void setDescriptorId(Long descriptorId) {
			this.descriptorId = descriptorId;
		}

		public Long getBscId() {
			return bscId;
		}

		public void setBscId(Long bscId) {
			this.bscId = bscId;
		}

		public java.sql.Date getStsDate() {
			return stsDate;
		}

		public void setStsDate(java.sql.Date stsDate) {
			this.stsDate = stsDate;
		}

		public String getStsPeriod() {
			return stsPeriod;
		}

		public void setStsPeriod(String stsPeriod) {
			this.stsPeriod = stsPeriod;
		}

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public String getCellChineseName() {
			return cellChineseName;
		}

		public void setCellChineseName(String cellChineseName) {
			this.cellChineseName = cellChineseName;
		}

		public Float getTchIntactRate() {
			return tchIntactRate;
		}

		public void setTchIntactRate(Float tchIntactRate) {
			this.tchIntactRate = tchIntactRate;
		}

		public Float getDeclareChannelNumber() {
			return declareChannelNumber;
		}

		public void setDeclareChannelNumber(Float declareChannelNumber) {
			this.declareChannelNumber = declareChannelNumber;
		}

		public Float getAvailableChannelNumber() {
			return availableChannelNumber;
		}

		public void setAvailableChannelNumber(Float availableChannelNumber) {
			this.availableChannelNumber = availableChannelNumber;
		}

		public Float getCarrierNumber() {
			return carrierNumber;
		}

		public void setCarrierNumber(Float carrierNumber) {
			this.carrierNumber = carrierNumber;
		}

		public Float getResourceUseRate() {
			return resourceUseRate;
		}

		public void setResourceUseRate(Float resourceUseRate) {
			this.resourceUseRate = resourceUseRate;
		}

		public Float getTraffic() {
			return traffic;
		}

		public void setTraffic(Float traffic) {
			this.traffic = traffic;
		}

		public Float getTrafficPerLine() {
			return trafficPerLine;
		}

		public void setTrafficPerLine(Float trafficPerLine) {
			this.trafficPerLine = trafficPerLine;
		}

		public Float getAccessOkRate() {
			return accessOkRate;
		}

		public void setAccessOkRate(Float accessOkRate) {
			this.accessOkRate = accessOkRate;
		}

		public Float getRadioAccess() {
			return radioAccess;
		}

		public void setRadioAccess(Float radioAccess) {
			this.radioAccess = radioAccess;
		}

		public Float getDropCallNumTogether() {
			return dropCallNumTogether;
		}

		public void setDropCallNumTogether(Float dropCallNumTogether) {
			this.dropCallNumTogether = dropCallNumTogether;
		}

		public Float getRadioDropRateNoHv() {
			return radioDropRateNoHv;
		}

		public void setRadioDropRateNoHv(Float radioDropRateNoHv) {
			this.radioDropRateNoHv = radioDropRateNoHv;
		}

		public Float getIcm() {
			return icm;
		}

		public void setIcm(Float icm) {
			this.icm = icm;
		}

		public Float getHandoutSucRate() {
			return handoutSucRate;
		}

		public void setHandoutSucRate(Float handoutSucRate) {
			this.handoutSucRate = handoutSucRate;
		}

		public Float getHandinSucRate() {
			return handinSucRate;
		}

		public void setHandinSucRate(Float handinSucRate) {
			this.handinSucRate = handinSucRate;
		}

		public Float getHandoverSucRate() {
			return handoverSucRate;
		}

		public void setHandoverSucRate(Float handoverSucRate) {
			this.handoverSucRate = handoverSucRate;
		}

		public Float getPsRadioUseRate() {
			return psRadioUseRate;
		}

		public void setPsRadioUseRate(Float psRadioUseRate) {
			this.psRadioUseRate = psRadioUseRate;
		}

		public Float getPdchCarryingEfficiency() {
			return pdchCarryingEfficiency;
		}

		public void setPdchCarryingEfficiency(Float pdchCarryingEfficiency) {
			this.pdchCarryingEfficiency = pdchCarryingEfficiency;
		}

		public Float getDataTraffic() {
			return dataTraffic;
		}

		public void setDataTraffic(Float dataTraffic) {
			this.dataTraffic = dataTraffic;
		}

		public Float getDownlinkBpdchReuse() {
			return downlinkBpdchReuse;
		}

		public void setDownlinkBpdchReuse(Float downlinkBpdchReuse) {
			this.downlinkBpdchReuse = downlinkBpdchReuse;
		}

		public Float getDownlinkEpdchReuse() {
			return downlinkEpdchReuse;
		}

		public void setDownlinkEpdchReuse(Float downlinkEpdchReuse) {
			this.downlinkEpdchReuse = downlinkEpdchReuse;
		}

		public Float getDownlinkTbfCongRate() {
			return downlinkTbfCongRate;
		}

		public void setDownlinkTbfCongRate(Float downlinkTbfCongRate) {
			this.downlinkTbfCongRate = downlinkTbfCongRate;
		}

	}

	public static void main(String[] args) {
		// Date ud = new Date();
		// java.sql.Date sd = new java.sql.Date(ud.getTime());
		// System.out.println("ud=" + ud + ",sd=" + sd);
		// String ds = "2013-10-15";
		// sd = java.sql.Date.valueOf(ds);
		// System.out.println("sd=" + sd);

		// String reg1="^(\\d+)|([0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3})$";
		String reg1 = "[0-9]{2}0{2}\\-{1}[0-9]{2}0{2}";
		Pattern p = Pattern.compile(reg1);
		String a1 = "00:00:00,234";
		String a2 = "1200-2300";
		System.out.println(p.matcher(a1).find());
		System.out.println(p.matcher(a2).find());
		System.out.println(a1.matches(reg1));
		System.out.println(a2.matches(reg1));
	}
	
	/**
	 * 计算excel title的字段名
	 * 
	 * @param titles
	 * @param excelTitlesColumn
	 * @author brightming 2013-12-24 下午3:37:38
	 */
	private int calculateExcelTitleColumn(List<String> titles,
			Map<String, Integer> excelTitlesColumn, StringBuilder msg) {
		if (titles == null || titles.size() == 0) {
			msg.append("标题为空");
			return -1;
		}
		int col = 0;
		if (dbFieldsToExcelTitles == null || dbFieldsToExcelTitles.isEmpty()) {
			msg.append("系统配置出错！");
			return -1;
		}

		Collection<String> allAllowedExcelTitles = dbFieldsToExcelTitles
				.values();

		String oneTitle = null;
		for (col = 0; col < titles.size(); col++) {
			oneTitle = titles.get(col);
			if (oneTitle == null || "".equals(oneTitle.trim())) {
				// 遇到空格或null就退出
				return col;
			}
			oneTitle = oneTitle.trim();
			if (allAllowedExcelTitles.contains(oneTitle)) {
				if (excelTitlesColumn.containsKey(oneTitle)) {
					// 标题重复
					msg.append("excel标题重复");
					return -2;
				}
				excelTitlesColumn.put(oneTitle, col);
			}
		}
		return col;
	}
	
	/**
	 * 检查必须要去的excel标题是否存在
	 * 
	 * @param excelTitlesColumn
	 * @param mandatoryExcelTitles2
	 * @param msg
	 * @return
	 * @author brightming 2013-12-24 下午3:55:19
	 */
	private boolean checkIfExcelTitleValide(
			Map<String, Integer> excelTitlesColumn,
			List<String> mandatoryExcelTitles2, StringBuilder msg) {

		if (mandatoryExcelTitles2 == null || mandatoryExcelTitles2.isEmpty()) {
			return true;

		}
		if (excelTitlesColumn == null || excelTitlesColumn.isEmpty()) {
			return false;
		}

		boolean ok = true;
		for (String one : mandatoryExcelTitles2) {
			if (!excelTitlesColumn.containsKey(one)
					|| excelTitlesColumn.get(one) == null
					|| excelTitlesColumn.get(one) < 0) {
				msg.append(one + ",");
				ok = false;
			}
		}
		if (ok == false) {
			msg.append("excel缺少以下有效标题：" + msg.substring(0, msg.length() - 1));
		}
		return ok;
	}
	
	
	/**
	 * 设置值
	 * 
	 * @param statement
	 * @param dbField
	 *            数据库字段名称
	 * @param value
	 *            数据库该字段的值
	 * @param valueType
	 *            数据库该字段的类型
	 * @author brightming 2013-12-24 下午5:49:38
	 */
	private boolean setPreparedStatementValue(PreparedStatement statement,
			String dbField, String value, String valueType) {
		log.info("dbField: "+dbField+",value: "+value+",valueType: "+valueType);
		if (valueType == null || "".equals(valueType)) {
			return false;
		}

		int poi = seqenceDbFieldsPosition.get(dbField);
		// log.info("设置poi="+poi+" dbField="+dbField+", value="+value+",valueType="+valueType);
		try {
			if ("Long".equals(valueType)) {
				if (value == null) {
					statement.setNull(poi, Types.NUMERIC);
				} else {
					long l = 0;
					try {
						l = Long.parseLong(value);
					} catch (Exception e1) {
						e1.printStackTrace();
						statement.setNull(poi, Types.NUMERIC);
					}
					statement.setLong(poi, l);
				}
			} else if ("String".equals(valueType)) {
				statement.setString(poi, value);
			} else if ("Double".equals(valueType)) {
				if (value == null) {
					statement.setNull(poi, Types.DOUBLE);
				} else {
					double l = 0;
					try {
						l = Double.parseDouble(value);
					} catch (Exception e1) {
						e1.printStackTrace();
						statement.setNull(poi, Types.NUMERIC);
					}
					statement.setDouble(poi, l);
				}
			} else if ("Float".equals(valueType)) {
				if (value == null) {
					statement.setNull(poi, Types.FLOAT);
				} else {
					float l = 0;
					try {
						l = Float.parseFloat(value);
					} catch (Exception e1) {
						e1.printStackTrace();
						statement.setNull(poi, Types.FLOAT);
					}
					statement.setFloat(poi, l);
				}
			} else if ("Date".equals(valueType)) {
				if (value == null) {
					statement.setNull(poi, Types.DATE);
				} else {
					try {
						statement.setDate(poi, new java.sql.Date(sdf1.parse(value).getTime()));
					} catch (ParseException e1) {
						e1.printStackTrace();
						try {
							statement.setDate(poi, new java.sql.Date(sdf2.parse(value).getTime()));
						} catch (ParseException e2) {
							e2.printStackTrace();
							try{
								statement.setDate(poi, new java.sql.Date(sdf3.parse(value).getTime()));
							}catch(ParseException e3){
								e3.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 获取excel中未提供的标题
	 * 
	 * @param excelTitlesColumn
	 * @return
	 * @author brightming 2013-12-25 下午6:00:59
	 */
	private List<String> getNotListExcelTitles(
			Map<String, Integer> excelTitlesColumn) {
		List<String> re = new ArrayList<String>();
		for (String s : excelTitlesToDbFields.keySet()) {
			if (!excelTitlesColumn.containsKey(s)) {
				re.add(s);
			}
		}
		return re;
	}
	
	/**
	 * 判断execl中的date是否合法
	 * 
	 * @param inDate
	 * @return
	 */
	private boolean isValidDate(String inDate) {
		if (inDate == null)
			return false;
	    if (inDate.trim().length() != sdf1.toPattern().length())
	    	return false;
	    sdf1.setLenient(false);
	    try {
	    	//解析是否有效日期
	    	sdf1.parse(inDate.trim());
	    }
	    catch (ParseException pe) {
	    	return false;
	    }
	    return true;
	  }
}
