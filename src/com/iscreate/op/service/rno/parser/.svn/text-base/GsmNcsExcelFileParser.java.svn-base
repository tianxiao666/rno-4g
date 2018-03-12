package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoNcs;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

/**
 * ncs文件的解析器
 * 
 * @author brightming
 * 
 */
public class GsmNcsExcelFileParser extends ExcelFileParserBase {
	private static Log log = LogFactory.getLog(GsmNcsExcelFileParser.class);

	// ---spring 注入----//
	private SysAreaDao sysAreaDao;

	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 导入模板规定的标题头
	private static List<String> expectTitles = Arrays.asList("cell", "chgr",
			"bsic", "ARFCN", "defined?", "RECTIMEARFCN", "REPARFCN", "TIMES",
			"NAVSS", "TIMES1", "NAVSS1", "TIMES2", "NAVSS2", "TIMES3",
			"NAVSS3", "TIMES4", "NAVSS4", "TIMES5", "NAVSS5", "TIMES6",
			"NAVSS6", "TIMESRELSS", "TIMESRELSS2", "TIMESRELSS3",
			"TIMESRELSS4", "TIMESRELSS5", "TIMESABSS", "TIMESALONE", "ncell",
			"distance(km)");

	private static int titleSize = expectTitles.size();

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入GsmNcsFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);

		long begTime = System.currentTimeMillis();
		long t1 = 0, t2 = 0;

		// 导入到的目标区域（区/县级别）
		SysArea country = sysAreaDao.getAreaById(areaId);
		if (country == null) {
			log.error("不存在id=" + areaId + "的区域！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"指定的导入区域不存在！");
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			return false;
		} else {
			log.info("准备导入NCS数据到：" + country.getName());
		}
		fileParserManager.updateTokenProgress(token, 0.02f);
		// 获取全部数据
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);
		fileParserManager.updateTokenProgress(token, 0.10f);
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

		// 检查文件标题
		boolean titleok = super.checkTitles(expectTitles, allDatas.get(0));
		if (!titleok) {
			log.error("上传的NCS excel文件的格式不符合格式要求！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！确保文件的格式为：" + expectTitles);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		fileParserManager.updateTokenProgress(token, 0.15f);
		int total = allDatas.size() - 1;// excel有效记录数

		// 用户必须输入：开始测量时间，记录时长，记录的时间间隔
		// 用户可以输入：负的信号强度门限，绝对信号强度，每次扫描记录的频点个数，网络制式，厂家
		Date startTime = null;
		boolean hasError = false;
		String errMsg = "";
		try {
			startTime = sdf1.parse(attachParams.get("startTime") + "");
		} catch (ParseException e1) {
			e1.printStackTrace();
			hasError = true;
			errMsg += "必须提供开始测量时间！<br/>";
		}
		long rectime = -1;
		try {
			rectime = Long.parseLong(attachParams.get("rectime") + "");
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
			errMsg += "必须提供记录时长！<br/>";
		}
		String segtime = (String) attachParams.get("segtime");
		if (segtime == null || "".equals(segtime.trim())) {
			hasError = true;
			errMsg += "必须提供记录的时间间隔！<br/>";
		}
		log.info("startTime=" + startTime + ",rectime=" + rectime + ",segtime="
				+ segtime);

		if (hasError) {
			log.error(errMsg);
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！原因：" + errMsg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		// 其他可选的输入信息
		String netType = attachParams.get("netType") + "";
		long relssn = 0;
		try {
			relssn = Long.parseLong(attachParams.get("relssn") + "");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		long abss = 0;
		try {
			abss = Long.parseLong(attachParams.get("abss") + "");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		long numfreq = 0;
		try {
			numfreq = Long.parseLong(attachParams.get("numfreq") + "");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		String vendor = attachParams.get("vendor") + "";

		// 准备数据库相关的工具
		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(connection, token, "访问出错！code=501");
			return false;
		}

		int errorCnt = 0;
		int successCnt = 0;

		// ncs描述id
		long descId = -1;
		String vsql = "select SEQ_RNO_NCS_DESCRIPTOR.NEXTVAL as id from dual";
		PreparedStatement pstmt =null;
		try {
			pstmt=(PreparedStatement) connection
					.prepareStatement(vsql);
		} catch (SQLException e3) {
			e3.printStackTrace();
			fail(connection, token, "数据处理出错！code=505");
			return false;
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			rs.next();
			descId= rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=506");
			return false;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		Date now = new Date();
		String createTime = sdf1.format(now);
		String name=attachParams.get("name")+"";
		// 插入描述表的语句
		String startTimeStr = sdf1.format(startTime);
		String insertDescriptor = "insert into RNO_NCS_DESCRIPTOR (RNO_NCS_DESC_ID,NAME,START_TIME,SEGTIME,RECTIME,AREA_ID,CREATE_TIME,MOD_TIME,STATUS,NET_TYPE,RELSSN,ABSS,NUMFREQ,VENDOR) values ("+descId+",'"+name+"',TO_DATE('"
				+ startTimeStr
				+ "','yyyy-mm-dd HH24:mi:ss'),'"
				+ segtime
				+ "',"
				+ rectime
				+ ","
				+ areaId
				+ ",TO_DATE('"
				+ createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),TO_DATE('"
				+ createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'N','"
				+ netType
				+ "',"
				+ relssn
				+ "," + abss + "," + numfreq + ",'" + vendor + "')";

		log.info("------------准备插入ncs描述信息：");

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(insertDescriptor);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=502");
			return false;
		}

		// 执行
		try {
			boolean ok=statement.execute();
			log.info("插入ncs描述信息的结果："+ok);
		} catch (SQLException e2) {
			e2.printStackTrace();
			fail(connection, token, "数据处理出错！code=503");
			return false;
		}
//		
		log.info("-----------完成插入ncs描述信息，得到descId=" + descId);

		// 插入详细表的语句
		String fields = "RNO_NCS_ID,RNO_NCS_DESC_ID,CELL,NCELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,DISTANCE";
		String insertIntoDetailTable = "insert into RNO_NCS ( "
				+ fields
				+ " ) values (SEQ_RNO_NCS.NEXTVAL,"
				+ descId
				+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement insertSqlpstmt = null;
		try {
			insertSqlpstmt = connection.prepareStatement(insertIntoDetailTable);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=503");
			return false;
		}

		// 1、新建ncs描述
		// 2、插入详细的ncs信息
		StringBuilder buf = new StringBuilder();
		RnoNcs rnoncs = null;
		int index = 1;
		log.info("----------开始处理excel行：");
		t1 = System.currentTimeMillis();
		for (int i = 1; i < allDatas.size(); i++) {
			fileParserManager.updateTokenProgress(token,
					0.15f + (i * 1.0f / total) * 0.5f);
			rnoncs = createRnoNcsFromExcelLine(country.getName(),
					allDatas.get(i), i, buf);
			if (rnoncs == null) {
				errorCnt++;
				continue;
			}
			successCnt++;
			index = 1;
			try {
				// CELL,NCELL,CHGR,BSIC,ARFCN,
				insertSqlpstmt.setString(index++, rnoncs.getCell());
				insertSqlpstmt.setString(index++, rnoncs.getNcell());
				insertSqlpstmt.setLong(index++, rnoncs.getChgr());
				insertSqlpstmt.setString(index++, rnoncs.getBsic());
				insertSqlpstmt.setLong(index++, rnoncs.getArfcn());
				// DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,
				insertSqlpstmt.setString(index++, rnoncs.getDefinedNeighbour());
				insertSqlpstmt.setLong(index++, rnoncs.getRectimearfcn());
				insertSqlpstmt.setLong(index++, rnoncs.getReparfcn());
				insertSqlpstmt.setLong(index++, rnoncs.getTimes());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss());
				// TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,
				insertSqlpstmt.setLong(index++, rnoncs.getTimes1());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss1());
				insertSqlpstmt.setLong(index++, rnoncs.getTimes2());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss2());
				insertSqlpstmt.setLong(index++, rnoncs.getTimes3());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss3());
				// TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,
				insertSqlpstmt.setLong(index++, rnoncs.getTimes4());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss4());
				insertSqlpstmt.setLong(index++, rnoncs.getTimes5());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss5());
				insertSqlpstmt.setLong(index++, rnoncs.getTimes6());
				insertSqlpstmt.setLong(index++, rnoncs.getNavss6());
				// TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,
				insertSqlpstmt.setLong(index++, rnoncs.getTimesrelss());
				insertSqlpstmt.setLong(index++, rnoncs.getTimesrelss2());
				insertSqlpstmt.setLong(index++, rnoncs.getTimesrelss3());
				insertSqlpstmt.setLong(index++, rnoncs.getTimesrelss4());
				insertSqlpstmt.setLong(index++, rnoncs.getTimesrelss5());
				// TIMESABSS,TIMESALONE,DISTANCE
				insertSqlpstmt.setLong(index++, rnoncs.getTimesabss());
				insertSqlpstmt.setLong(index++, rnoncs.getTimesalone());
				insertSqlpstmt.setDouble(index++, rnoncs.getDistance());

				insertSqlpstmt.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				successCnt--;
			}
		}
		t2 = System.currentTimeMillis();
		log.info("----------excel行处理完毕。耗时：" + (t2 - t1) + "ms. successCnt="
				+ successCnt + ",errorCnt=" + errorCnt);

		log.info("----------准备执行批处理");
		t1 = System.currentTimeMillis();
		try {
			int[] re1 = insertSqlpstmt.executeBatch();
			log.info("批处理插入的结果：" + re1);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=504");
			return false;
		}
		fileParserManager.updateTokenProgress(token, 0.85f);
		t2 = System.currentTimeMillis();
		log.info("----------批处理处理完成。耗时：" + (t2 - t1));

		// 提交
		if (successCnt > 0) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			statement.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			insertSqlpstmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		fileParserManager.updateTokenProgress(token, 0.90f);

		t2=System.currentTimeMillis();
		
		// 判断是有加载到分析列表
		List<PlanConfig> planConfigs = null;
		if(autoload){
			log.info("需要加载到分析列表。准备加载到分析列表。");
			PlanConfig pc = new PlanConfig();
			pc.setConfigId(descId);
			pc.setAreaName(country.getName());
			pc.setSelected(false);
			pc.setTemp(false);
			pc.setType(RnoConstant.DataType.NCS_DATA);
			pc.setName(name);
			pc.setTitle(country.getName() + name);
			
			pc.setCollectTime(attachParams.get("startTime")+"");

			planConfigs = addToAnalysisList(attachParams, pc);
		}
		
		String errmsg=buf.toString();
		if(!"".equals(errmsg)){
			errmsg="详情如下：<br/>"+errmsg;
		}
		t2=System.currentTimeMillis();
		String result="{'msg':'导入共处理："+total+"行数据。成功："+successCnt+"条，失败："+errorCnt+"条。耗时："+((t2-begTime)>1000?((t2-begTime)/1000+"s"):((t2-begTime)+"ms"))+"。"+errmsg+"'";
		if (autoload) {
			String conList = super.gson.toJson(planConfigs);
			result += ",'list':" + conList;
		}
		result += "}";
		try {
			 memCached.set(token, RnoConstant.TimeConstant.TokenTime,
					result);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return true;
	}
	
	private List<PlanConfig> addToAnalysisList(
			Map<String, Object> attachParams, PlanConfig pc) {
		List<PlanConfig> planConfigs = null;
		if (attachParams == null) {
			log.error("未带有session信息，不能支持添加到分析列表！");
		} else {
			HttpSession session = (HttpSession) attachParams.get("session");
			if (session == null) {
				log.error("session为空！");
			} else {
				log.info("准备添加到分析列表");
				planConfigs = (ArrayList<PlanConfig>) session
						.getAttribute(RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
				log.info("当前session里的干扰分析列表：" + planConfigs);
				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					session.setAttribute(
							RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID,
							planConfigs);
				}
				if (!planConfigs.contains(pc)) {
					planConfigs.add(pc);
				}
				session.setAttribute(
						RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID,
						planConfigs);
			}
		}
		return planConfigs;
	}

	private static int getIndexOfTitle(String title) {
		return expectTitles.indexOf(title);
	}

	private RnoNcs createRnoNcsFromExcelLine(String country,
			List<String> oneData, int i, StringBuilder buf) {
		String msg = "";
		if (oneData == null || oneData.size() < titleSize) {
			msg = "第【" + (i + 1) + "】行的数据有缺失！该行只有["+(oneData==null?0:oneData.size())+"]列数据，应该有：["+titleSize+"]列数据！<br/>";
			buf.append(msg);
			log.error(msg);
			return null;
		}

		String cell;
		String ncell;
		Long chgr;
		String bsic;
		Long arfcn;
		String definedNeighbour;
		Long rectimearfcn;
		Long reparfcn;
		Long times;
		Long navss;
		Long times1;
		Long navss1;
		Long times2;
		Long navss2;
		Long times3;
		Long navss3;
		Long times4;
		Long navss4;
		Long times5;
		Long navss5;
		Long times6;
		Long navss6;
		Long timesrelss;
		Long timesrelss2;
		Long timesrelss3;
		Long timesrelss4;
		Long timesrelss5;
		Long timesabss;
		Long timesalone;
		Double distance;

		int index = 0;
		// cell
		cell = oneData.get(index++);
		if (cell == null || "".equals(cell.trim())) {
			msg = "第【" + (i + 1) + "】行缺失小区信息！<br/>";
			buf.append(msg);
			return null;
		}
		cell = cell.trim();

		// chgr
		try {
			chgr = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			chgr = 0L;
			msg = "第【" + (i + 1) + "】行的CHGR需要是数字！";
			buf.append(msg);
			return null;
		}

		// bsic
		bsic = oneData.get(index++);
		bsic = bsic == null ? "" : bsic.trim();

		// arfcn
		try {
			arfcn = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的ARFCN需要是数字！";
			buf.append(msg);
			// arfcn=0L;
			return null;
		}

		// defined
		definedNeighbour = oneData.get(index++);
		definedNeighbour = definedNeighbour == null ? "" : definedNeighbour
				.trim();

		// rectimearfcn
		try {
			rectimearfcn = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的RECTIMEARFCN需要是数字！";
			buf.append(msg);
			return null;
		}

		// REPARFCN
		try {
			reparfcn = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的REPARFCN需要是数字！";
			buf.append(msg);
			return null;
		}

		// times
		try {
			times = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES需要是数字！";
			buf.append(msg);
			return null;
		}

		// NAVSS
		try {
			navss = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS需要是数字！";
			buf.append(msg);
			return null;
		}

		// times1
		try {
			times1 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES1需要是数字！";
			buf.append(msg);
			return null;
		}

		// navss1
		try {
			navss1 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS1需要是数字！";
			buf.append(msg);
			return null;
		}

		// times2
		try {
			times2 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES2需要是数字！";
			buf.append(msg);
			return null;
		}
		// navss2
		try {
			navss2 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS2需要是数字！";
			buf.append(msg);
			return null;
		}

		// times3
		try {
			times3 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES3需要是数字！";
			buf.append(msg);
			return null;
		}
		// navss3
		try {
			navss3 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS3需要是数字！";
			buf.append(msg);
			return null;
		}

		// times4
		try {
			times4 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES4需要是数字！";
			buf.append(msg);
			return null;
		}
		// navss4
		try {
			navss4 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS4需要是数字！";
			buf.append(msg);
			return null;
		}

		// times5
		try {
			times5 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES5需要是数字！";
			buf.append(msg);
			return null;
		}
		// navss5
		try {
			navss5 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS5需要是数字！";
			buf.append(msg);
			return null;
		}

		// times6
		try {
			times6 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMES6需要是数字！";
			buf.append(msg);
			return null;
		}
		// navss6
		try {
			navss6 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的NAVSS6需要是数字！";
			buf.append(msg);
			return null;
		}

		// TIMESRELSS
		try {
			timesrelss = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESRELSS需要是数字！";
			buf.append(msg);
			return null;
		}

		// TIMESRELSS2
		try {
			timesrelss2 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESRELSS2需要是数字！";
			buf.append(msg);
			return null;
		}
		// TIMESRELSS3
		try {
			timesrelss3 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESRELSS3需要是数字！";
			buf.append(msg);
			return null;
		}
		// TIMESRELSS4
		try {
			timesrelss4 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESRELSS4需要是数字！";
			buf.append(msg);
			return null;
		}

		// TIMESRELSS5
		try {
			timesrelss5 = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESRELSS5需要是数字！";
			buf.append(msg);
			return null;
		}

		// TIMESABSS
		try {
			timesabss = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESABSS需要是数字！";
			buf.append(msg);
			return null;
		}

		// TIMESALONE
		try {
			timesalone = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的TIMESALONE需要是数字！";
			buf.append(msg);
			return null;
		}
		// NCELL
		ncell = oneData.get(index++);
		if (ncell == null || "".equals(ncell.trim())) {
			msg = "第【" + (i + 1) + "】行的NCELL不能为空！";
			buf.append(msg);
			return null;
		}
		ncell = ncell == null ? "" : ncell.trim();

		// distance
		try {
			distance = Double.parseDouble(oneData.get(index++));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第【" + (i + 1) + "】行的distance需要是数字！";
			buf.append(msg);
			return null;
		}

		RnoNcs rnoncs = new RnoNcs();
		rnoncs.setArfcn(arfcn);
		rnoncs.setBsic(bsic);
		rnoncs.setCell(cell);
		rnoncs.setChgr(chgr);
		rnoncs.setDefinedNeighbour(definedNeighbour);
		rnoncs.setDistance(distance);
		rnoncs.setNavss(navss);
		rnoncs.setNavss1(navss1);
		rnoncs.setNavss2(navss2);
		rnoncs.setNavss3(navss3);
		rnoncs.setNavss4(navss4);
		rnoncs.setNavss5(navss5);
		rnoncs.setNavss6(navss6);
		rnoncs.setNcell(ncell);
		rnoncs.setRectimearfcn(rectimearfcn);
		rnoncs.setReparfcn(reparfcn);
		rnoncs.setTimes(times);
		rnoncs.setTimes1(times1);
		rnoncs.setTimes2(times2);
		rnoncs.setTimes3(times3);
		rnoncs.setTimes4(times4);
		rnoncs.setTimes5(times5);
		rnoncs.setTimes6(times6);
		rnoncs.setTimesabss(timesabss);
		rnoncs.setTimesalone(timesalone);
		rnoncs.setTimesrelss(timesrelss);
		rnoncs.setTimesrelss2(timesrelss2);
		rnoncs.setTimesrelss3(timesrelss3);
		rnoncs.setTimesrelss4(timesrelss4);
		rnoncs.setTimesrelss5(timesrelss5);

		return rnoncs;

	}
}
