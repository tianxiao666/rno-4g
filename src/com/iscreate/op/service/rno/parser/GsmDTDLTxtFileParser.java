package com.iscreate.op.service.rno.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoDtDescriptor;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.opensymphony.xwork2.FileManager;

/**
 * 鼎力设备的路测结果的解析类
 * 
 * @author brightming
 * 
 */
public class GsmDTDLTxtFileParser extends TxtFileParserBase {
	private static Log logger = LogFactory.getLog(GsmDTDLTxtFileParser.class);
	private static SimpleDateFormat sdfYyyymmddhhmmss = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static int minSecInterval=2;//采样的最小时间间隔（秒为单位）
	// 插入中间表的sql语句的顺序和类型的关系
	private static Map<Integer, String> columnType = new HashMap<Integer, String>() {
		{
			put(1, "String");// SAMPLE_TIME
			put(2, "Float");// LONGITUDE
			put(3, "Float");// LATITUDE
			put(4, "Integer");// ALTITUDE
			put(5, "Integer");// SPEED
			put(6, "String");// MAP_LNGLAT
			put(7, "Integer");// LAC
			put(8, "Integer");// CI
			put(9, "Float");// Distance
			put(10, "Integer");// RXLEVSUB
			put(11, "Integer");// RXQUALSUB
			put(12, "Integer");// NCELL_COUNT
			//
			put(13, "Integer");// NCELL_BCCH_1
			put(14, "String");// NCELL_BSIC_1
			put(15, "Integer");// NCELL_RXLEV_1

			put(16, "Integer");// NCELL_BCCH_2
			put(17, "String");// NCELL_BSIC_2
			put(18, "Integer");// NCELL_RXLEV_2

			put(19, "Integer");// NCELL_BCCH_3
			put(20, "String");// NCELL_BSIC_3
			put(21, "Integer");// NCELL_RXLEV_3

			put(22, "Integer");// NCELL_BCCH_4
			put(23, "String");// NCELL_BSIC_4
			put(24, "Integer");// NCELL_RXLEV_4

			put(25, "Integer");// NCELL_BCCH_5
			put(26, "String");// NCELL_BSIC_5
			put(27, "Integer");// NCELL_RXLEV_5

			put(28, "Integer");// NCELL_BCCH_6
			put(29, "String");// NCELL_BSIC_6
			put(30, "Integer");// NCELL_RXLEV_6

		}
	};

	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		// 解析过程：
		/**
		 * 1、解析log文件，得到头部文件信息 2、得到用RegReport声明的格式 3、得到各种数据 4、判断是否需要形成一个新的采样点
		 * 5、满足判断条件，组合成一个采样点的数据
		 * 
		 * 
		 * 
		 * 
		 */

		String midTable = "RNO_GSM_DT_MID";// "RNO_TEST_DT";
		String finalTable = "RNO_GSM_DT_SAMPLE";
		long t1 = System.currentTimeMillis();
		long ta=t1,tb=t1;
		// 1、读取log
		List<String> insertIntoSampleMidTabParas = null;
		DTFileInfo dtFileInfo = new DTFileInfo();
		tb=System.currentTimeMillis();
		logger.info(">>>>>>>>>>开始解析log文件解析");
		try {
			insertIntoSampleMidTabParas = extract(file, dtFileInfo, token, 0.5f);
		} catch (IOException e) {
			e.printStackTrace();
			super.setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"文件处理失败！");
			return false;
		}
		if (insertIntoSampleMidTabParas == null
				|| insertIntoSampleMidTabParas.size() == 0) {
			logger.warn("没有提取到dt数据！请确认是合法的dt文件！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"没有提取到dt数据！请确认是合法的dt文件！");
			return false;
		}
		logger.info("<<<<<<<<<<log文件解析完成，耗时："+(tb-ta)+"ms");

		long descId = -1;
		descId = super.getNextSeqValue("SEQ_RNO_DT_DESCRIPTOR", connection);
		if (descId == -1) {
			logger.error("获取描述表的id出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=700");
			return false;
		}

		PreparedStatement pstatement = null;

		String insertIntoMidTabSql = "insert into "
				+ midTable
				+ "(SAMPLE_TIME,LONGITUDE,LATITUDE,ALTITUDE,SPEED,MAP_LNGLAT,LAC,CI,DISTANCE,RXLEVSUB,RXQUALSUB,"
				+ "NCELL_COUNT,NCELL_BCCH_1,NCELL_BSIC_1,NCELL_RXLEV_1,NCELL_BCCH_2,NCELL_BSIC_2,NCELL_RXLEV_2,"
				+ "NCELL_BCCH_3,NCELL_BSIC_3,NCELL_RXLEV_3,NCELL_BCCH_4,NCELL_BSIC_4,NCELL_RXLEV_4 ,"
				+ "NCELL_BCCH_5,NCELL_BSIC_5,NCELL_RXLEV_5,NCELL_BCCH_6,NCELL_BSIC_6,NCELL_RXLEV_6)"
				+ " VALUES(TO_DATE(?" + ",'YYYY-MM-DD HH24:mi:ss'),"
				+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?" + ")";
		//
		try {
			pstatement = connection.prepareStatement(insertIntoMidTabSql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("准备批处理插入中间表的preparedstatement出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=701");
			return false;
		}

		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = null;
		long cityId = -1;
		try {
			cityId = Long.parseLong(attachParams.get("cityId") + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(">>>>>>>>>>准备获取矫正经纬度的矩阵：");
		ta=System.currentTimeMillis();
		if (cityId != -1) {
			standardPoints = rnoCommonService
					.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,
							RnoConstant.DBConstant.MAPTYPE_BAIDU);
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<获取矫正经纬度的矩阵耗时："+(tb-ta)+"ms");
		String[] ps = null;
		String p = null;
		String[] lnglats = null;
		String type = null;
		int columnIndex = 0;
		int successCnt = 0;
		int failCnt = 0;
		logger.info(">>>>>>>>>>准备生成批处理语句：");
		ta=System.currentTimeMillis();
		for (String line : insertIntoSampleMidTabParas) {
			ps = line.split(";");
			if (ps.length != 30) {
				logger.warn("参数无效！内容为：" + line);
				failCnt++;
				continue;
			}
			try {
				for (int i = 0; i < ps.length; i++) {
					columnIndex = i + 1;
					p = ps[i];
					if (i == 5) {// 特殊位置
						// map_lnglat需要进行转换，需要用longitude，latitude来进行百度转换
						lnglats = super.getBaiduLnglat(
								Double.parseDouble(ps[1]),
								Double.parseDouble(ps[2]), standardPoints);
						p = lnglats[0] + "," + lnglats[1];
					}
					type = columnType.get(columnIndex);
					if ("Integer".equals(type)) {
						pstatement.setInt(columnIndex, Integer.parseInt(p));
					} else if ("String".equals(type)) {
						pstatement.setString(columnIndex, p);
					} else if ("Float".equals(type)) {
						pstatement.setFloat(columnIndex, Float.parseFloat(p));
					} else if ("Long".equals(type)) {
						pstatement.setLong(columnIndex, Long.parseLong(p));
					} else {
						logger.warn("未知数据类型：" + type + ",丢弃该数据！p=" + p);
						failCnt++;
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("columnIndex=" + columnIndex);
				failCnt++;
				continue;
			}
			try {
				pstatement.addBatch();
				successCnt++;
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("add batch 失败！");
				failCnt++;
			}
		}
		
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<生成批处理语句耗时："+(tb-ta)+"ms");

		ta=tb;
		logger.info(">>>>>>>>>>>准备执行批处理语句：");
		try {
			pstatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {

			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=701");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<执行批处理语句耗时："+(tb-ta)+"ms");
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=702");
			return false;
		}

		// 更新中间表描述id
		logger.info(">>>>>>>>>>>>准备更新中间表的描述id信息：");
		String sql = "update " + midTable + " set RNO_DT_DESCRIPTOR_ID="
				+ descId;
		logger.info("sql=" + sql);
		ta=System.currentTimeMillis();
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=703");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新中间表的描述id信息。耗时："+(tb-ta)+"ms");

		// 更新中间表服务小区相关信息
		// 更新服务小区的名称，是否室分，小区gps经纬度
		logger.info(">>>>>>>>>>>>准备更新中间表服务小区的名称，是否室分，小区gps经纬度信息：");
		sql = "merge into "
				+ midTable
				+ " dt USING CELL ON (CELL.lac=dt.lac and cell.ci=dt.ci) WHEN MATCHED THEN UPDATE set dt.cell=CELL.label,dt.cell_indoor=decode(CELL.coverarea,'室内小区','Y','室分小区','Y','室分覆盖','Y','N'),dt.cell_longitude=decode(CELL.longitude,null,'0','','0',CELL.longitude),dt.cell_latitude=decode(CELL.latitude,null,'0','','0',CELL.latitude)";
		logger.info("sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=704");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新中间表服务小区的名称，是否室分，小区gps经纬度信息。耗时："+(tb-ta)+"ms");

		// --更新服务小区到采样点的夹角
		logger.info(">>>>>>>>>>>>准备更新中间表服务小区到采样点的夹角信息：");
		sql = "update "
				+ midTable
				+ " set angle=ATAN(abs((LONGITUDE-CELL_LONGITUDE)/(LATITUDE-CELL_LATITUDE)))*57.295779513082195 where CELL_LONGITUDE<>0 and CELL_LATITUDE<>0 and CELL_LATITUDE<>LATITUDE";
		logger.info("sql=" + sql);
		ta=System.currentTimeMillis();
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7051");
			return false;
		}

		// 第二象限
		sql = "update "
				+ midTable
				+ " set angle=(180-angle) where LONGITUDE>CELL_LONGITUDE and LATITUDE<CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7052");
			return false;
		}
		// 第三象限
		sql = "update "
				+ midTable
				+ " set angle=(180+angle) where LONGITUDE<CELL_LONGITUDE and LATITUDE<CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7053");
			return false;
		}

		// 第四象限
		sql = "update "
				+ midTable
				+ " set angle=(360-angle) where LONGITUDE<CELL_LONGITUDE and LATITUDE>CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7054");
			return false;
		}
		// --90度
		sql = "update "
				+ midTable
				+ " set angle=90 where longitude>=CELL_LONGITUDE and latitude=CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7055");
			return false;
		}
		// --270度
		sql = "update "
				+ midTable
				+ " set angle=270 where longitude<CELL_LONGITUDE and latitude=CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7056");
			return false;
		}
		// --0度
		sql = "update "
				+ midTable
				+ " set angle=0 where longitude=CELL_LONGITUDE and latitude>=CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7056");
			return false;
		}
		// --180度
		sql = "update "
				+ midTable
				+ " set angle=180 where longitude=CELL_LONGITUDE and latitude<CELL_LATITUDE";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=7057");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新中间表服务小区到采样点的夹角信息。耗时："+(tb-ta)+"ms");

		// ---更新服务小区到采样点的距离
		logger.info(">>>>>>>>>>>>准备更新服务小区到采样点的距离信息：");
		ta=tb;
		sql = "update "
				+ midTable
				+ " set DISTANCE=2 * 6371000 * asin(sqrt((sin(((CELL_LATITUDE - LATITUDE)* 0.017444444444444446) / 2.0)) * (sin(((CELL_LATITUDE - LATITUDE)* 0.017444444444444446) / 2.0)) + cos((CELL_LATITUDE * 0.017444444444444446)) * cos((LATITUDE * 0.017444444444444446)) * (sin(((CELL_LONGITUDE - LONGITUDE) * 0.017444444444444446) / 2.0)) * (sin(((CELL_LONGITUDE - LONGITUDE) * 0.017444444444444446) / 2.0)))) where distance=0 and nvl(CELL_LATITUDE,0)<>0 and nvl(CELL_LONGITUDE,0)<>0";
		logger.info("sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=706");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新服务小区到采样点的距离信息。耗时："+(tb-ta)+"ms");

		// 、准备创建描述信息

		logger.info(">>>>>>>>>>>>准备插入dt描述表信息：");
		String name = "";
		ta=tb;
		if (attachParams != null) {
			name = attachParams.get("name") + "";
		}
		if (name == null || "".equals(name.trim())) {
			name = dtFileInfo.getFileInfoName();
		}
		sql = "insert into RNO_DT_DESCRIPTOR (DT_DESC_ID,NAME,NET_MODE,TYPE,TEST_DATE,CREATE_TIME,MOD_TIME,STATUS,AREA_ID,DTLOG_VERSION,VENDOR,DEVICE,VERSION) values("
				+ descId
				+ ",'"
				+ name
				+ "','"
				+ dtFileInfo.getNetMode()
				+ "','"
				+ dtFileInfo.getType()
				+ "',to_date('"
				+ dtFileInfo.getYyyymmddhhmiss()
				+ "','yyyymmddHH24miss'),sysdate,sysdate,'N',"
				+ areaId
				+ ",'"
				+ dtFileInfo.getDtLogVersion()
				+ "','"
				+ dtFileInfo.getVendor()
				+ "','"
				+ dtFileInfo.getDevice()
				+ "','"
				+ dtFileInfo.getVersion() + "')";
		logger.info("sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=708");

			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成插入dt描述表信息。耗时:"+(tb-ta)+"ms");
		super.fileParserManager.updateTokenProgress(token, 0.85f);

		// --准备计算平均距离
		logger.info(">>>>>>>>>>>>准备更新服务小区到所有由其进行主覆盖的采样点的平均距离信息：");
		sql = "merge into "
				+ midTable
				+ " dt using (select distinct(cell),avg(distance) as avgdistance from "
				+ midTable + " where distance<>0 group by cell) mid1 "
				+ " on (dt.cell=mid1.cell)  when matched then update "
				+ " set dt.avg_distance=mid1.avgdistance";

		ta=System.currentTimeMillis();
		logger.info("sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=709");
			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新服务小区到所有由其进行主覆盖的采样点的平均距离信息。耗时："+(tb-ta)+"ms");

		// ---准备更新邻区信息
		logger.info(">>>>>>>>>>>>准备更新邻区名称、是否室内小区信息：");
		ta=tb;
		for (int i = 1; i < 7; i++) {
			logger.info("更新邻区" + i + ":");
			sql = "merge into "
					+ midTable
					+ " dt  "
					+ "   using "
					+ "(select label,bcch,bsic,max(coverarea) as coverarea,mid1.cell as serverCell  from cell inner join (select RNO_NCELL.cell,ncell from rno_ncell inner join "
					+ midTable
					+ " on RNO_NCELL.cell="
					+ midTable
					+ ".cell) mid1 "
					+ "  on CELL.label=MID1.ncell group by mid1.cell,label,bcch,bsic ) cellinfo "
					+ " on "
					+ " (dt.cell=cellinfo.serverCell and dt.ncell_bcch_"
					+ i
					+ "=cellinfo.bcch "
					+ " and dt.ncell_bsic_"
					+ i
					+ "=cellinfo.bsic "
					+ "  and nvl(dt.ncell_bcch_"
					+ i
					+ ",0)<>0 "
					+ " and nvl(dt.ncell_bsic_"
					+ i
					+ ",0)<>0) "
					+ " when matched then "
					+ "  update set dt.ncell_name_"
					+ i
					+ "=CELLinfo.label,dt.ncell_indoor_"
					+ i
					+ "=decode(CELLinfo.coverarea,'室内小区','Y','室分小区','Y','室分覆盖','Y','N') ";
			logger.info("sql=" + sql);

			try {
				statement.executeUpdate(sql);
				super.fileParserManager.updateTokenProgress(token,
						i * 0.05f + 0.50f);
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					pstatement.close();
				} catch (Exception e2) {
				}
				try {
					statement.close();
				} catch (Exception e2) {
				}
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						"数据处理出错！code=707");
				return false;
			}
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成更新邻区名称、是否室内小区信息。耗时："+(tb-ta)+"ms");

		// -------------临时表的数据处理完毕--------------//

		// 将中间表的数据迁移到正式表
		logger.info(">>>>>>>>>>>>准备将采样数据从中间表移动到正式表：");
		ta=tb;
		String fields = "RNO_DT_DESCRIPTOR_ID,SAMPLE_TIME,LONGITUDE,LATITUDE,ALTITUDE,SPEED,MAP_LNGLAT,LAC,CI,CELL,CELL_LONGITUDE,CELL_LATITUDE,RXLEVSUB,RXQUALSUB,DISTANCE,ANGLE,NCELL_BCCH_1,NCELL_BSIC_1,NCELL_RXLEV_1,NCELL_NAME_1,NCELL_INDOOR_1,NCELL_BCCH_2,NCELL_BSIC_2,NCELL_RXLEV_2,NCELL_NAME_2,NCELL_INDOOR_2,NCELL_BCCH_3,NCELL_BSIC_3,NCELL_RXLEV_3,NCELL_NAME_3,NCELL_INDOOR_3,NCELL_BCCH_4,NCELL_BSIC_4,NCELL_RXLEV_4,NCELL_NAME_4,NCELL_INDOOR_4,NCELL_BCCH_5,NCELL_BSIC_5,NCELL_RXLEV_5,NCELL_NAME_5,NCELL_INDOOR_5,NCELL_BCCH_6,NCELL_BSIC_6,NCELL_RXLEV_6,NCELL_NAME_6,NCELL_INDOOR_6,CELL_INDOOR,NCELL_COUNT,AVG_DISTANCE";
		sql = "insert into RNO_GSM_DT_SAMPLE (RNO_GSM_DT_SAMPLE_ID," + fields
				+ ") " + " select " + " SEQ_RNO_GSM_DT_SAMPLE.NEXTVAL,"
				+ fields + " FROM " + midTable + " tb";
		logger.info("sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				pstatement.close();
			} catch (Exception e2) {
			}
			try {
				statement.close();
			} catch (Exception e2) {
			}
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=708");

			return false;
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<完成将采样数据从中间表移动到正式表。耗时："+(tb-ta)+"ms");

		// 开始处理其他
		long t2 = System.currentTimeMillis();
		logger.info("数据处理入库共耗时：" + (t2 - t1) + "ms.总共得到的采样点："
				+ insertIntoSampleMidTabParas.size() + ",有效的数量：" + successCnt
				+ ",无效的个数：" + failCnt);
		ta=tb;
		List<PlanConfig> pcs = null;
		if (autoload) {
			PlanConfig pc = new PlanConfig();
			pc.setAreaName(attachParams == null ? "" : attachParams.get("name")
					+ "");
			pc.setCollectTime(dtFileInfo.getYyyymmddhhmiss());
			pc.setConfigId(descId);
			pc.setName(name);
			RnoDtDescriptor desc = new RnoDtDescriptor();
			desc.setAreaId(areaId);
			desc.setDevice(dtFileInfo.getDevice());
			desc.setDtDescId(descId);
			desc.setDtlogVersion(dtFileInfo.getDtLogVersion());
			desc.setName(name);
			desc.setNetMode(dtFileInfo.getNetMode());
			try {
				desc.setTestDate(sdfYyyymmddhhmmss.parse(dtFileInfo
						.getYyyymmddhhmiss()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			desc.setType(dtFileInfo.getType());
			desc.setVendor(dtFileInfo.getVendor());
			desc.setVersion(dtFileInfo.getVersion());

			pc.setObj(desc);
			pc.setSelected(false);
			pc.setTemp(false);
			pc.setTitle(name);
			pc.setType(dtFileInfo.getType());

			pcs = addToAnalysisList(RnoConstant.SessionConstant.DT_CONFIG_ID,
					attachParams, pc);
		}
		String result = "{'msg':'共采集到："
				+ successCnt
				+ "个有效采样点数据。耗时："
				+ ((t2 - t1) > 1000 ? ((t2 - t1) / 1000 + "s")
						: ((t2 - t1) + "ms")) + "'";
		if (autoload) {
			String conList = super.gson.toJson(pcs);
			result += ",'list':" + conList;
		}
		result += "}";
		logger.info("输出结果：" + result);
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tb=System.currentTimeMillis();
		logger.info("<<<<<<<<<<<<<<<<<< 完成收尾工作，耗时："+(tb-ta)+"ms");
		return true;
	}

	/**
	 * @param args
	 * @author brightming 2013-11-25 上午10:29:34
	 * @throws IOException
	 */
	public List<String> extract(File file, DTFileInfo dtFileInfo, String token,
			float ratio) throws IOException {

		// 获取文件行数
		int totalLine = getTxtLineNumber(file);
		if (totalLine == 0) {
			logger.info("传入的文件是空的！");
			return null;
		}

		List<String> batchIntoMidTableSql = new ArrayList<String>();

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));

		BufferedReader reader = new BufferedReader(new InputStreamReader(bis,
				"utf-8"), 1024);

		// 关注的关键字以及各关键字关注的字段
		Map<String, List<String>> keywordsAndValueNames = new HashMap<String, List<String>>() {
			{
				put("GPS", Arrays.asList("Longitude", "Latitude", "Altitude",
						"Speed"));
				put("SC", Arrays.asList("MCC", "MNC", "LAC", "CI",
						"HitsDistance"));
				put("DM", Arrays.asList("RxLevSub", "RxQualSub"));
				put("NC", Arrays.asList("Ni_BCCH", "Ni_BSIC", "Ni_RxLev"));
				put("DAY", Arrays.asList("YY", "MN", "DD"));
				put("HOUR", Arrays.asList("HH"));
				put("MIN", Arrays.asList("MN"));
				put("SEC", Arrays.asList("SS"));
			}
		};

		// 允许为空的默认值
		Map<String, Map<String, String>> defaultValues = new HashMap<String, Map<String, String>>() {
			{
				put("SC", new HashMap<String, String>() {
					{
						put("HitsDistance", "0");
					}
				});
			}
		};

		// 某关键字的某value在一行中以tab键分隔以后的位置（从0开始）
		Map<String, Map<String, Integer>> keywordValueIndexs = new HashMap<String, Map<String, Integer>>();

		// 关键字的value的值
		Map<String, Map<String, String>> keywordValueValues = new HashMap<String, Map<String, String>>();

		String fileInfo;

		String day = "";
		int oldHour = -1, hour = -1;
		int oldMin = -1, min = -1;
		int oldSec = -1, sec = -1;

		Double lng = -1d, lat = -1d, altitude = -1d, speed = -1d;
		String[] gpsarr;

		String sc = "";
		String dm = "";
		String nc = "";

		String line = "";
		int lineCnt = 0;
		long t1 = System.currentTimeMillis();
		boolean canOutput = false;
		String output = "";
		long outputCnt = 0;
		String[] temps;
		String keyword = null;
		Set<String> keywords = keywordsAndValueNames.keySet();
		boolean needDeal = false;

		String log = "";
		String sql = "";
		int currentRow = 0;
		while ((line = reader.readLine()) != null) {
			currentRow++;
			super.fileParserManager.updateTokenProgress(token, currentRow
					/ (totalLine * 1f) * ratio);
			needDeal = false;
			log = "";
			try {
				if (line.startsWith("RegReport\t")) {
					temps = line.split("\t");
					if (temps.length < 2) {
						continue;
					}
					keyword = temps[1];
					if (keywordsAndValueNames.containsKey(keyword)) {
						List<String> valueNames = keywordsAndValueNames
								.get(keyword);
						Map<String, Integer> valueCols = keywordValueIndexs
								.get(keyword);
						if (valueCols == null) {
							valueCols = new HashMap<String, Integer>();
							keywordValueIndexs.put(keyword, valueCols);
						}
						for (int i = 2; i < temps.length; i++) {// 从第2个开始才是字段
							if (valueNames.contains(temps[i])) {
								valueCols.put(temps[i], i - 1);// 记录某关键字下的某字段的位置
							}
						}
					}
					continue;
				}

				if (line.startsWith("FileInfo\t")) {
					fileInfo = line;
					temps = line.split("\t");
					dtFileInfo.setDtLogVersion(temps[1]);
					dtFileInfo.setVendor(temps[2]);
					dtFileInfo.setVersion(temps[3]);
					dtFileInfo.setNetMode(temps[4]);
					dtFileInfo.setTime(temps[5]);
					dtFileInfo.setDevice(temps[6]);
					dtFileInfo.setType(temps[7]);
					continue;
				}
				if (line.startsWith("DAY\t")) {
					day = line.replaceAll("\t", "-");
					hour = -1;
					min = -1;
					sec = -1;

					oldHour = -1;
					oldMin = -1;
					oldSec = -1;

					needDeal = true;
					// continue;
				}
				if (line.startsWith("HOUR\t")) {
					// log = "hour change.原来为：" + hour + ":" + min + ":" + sec
					// + ",更新以后为：";
					hour = Integer.parseInt(line.split("\t")[1]);
					// log += hour + ":" + min + ":" + sec + ",最后一次输出为：" +
					// oldHour
					// + ":" + oldMin + ":" + oldSec + "\r\n";
					oldMin = -1;
					oldSec = -1;

					min = -1;
					sec = -1;
					needDeal = true;
					// continue;
				}
				if (line.startsWith("MIN\t")) {
					// log = "min change.原来为：" + hour + ":" + min + ":" + sec
					// + ",更新以后为：" + hour + ":";
					min = Integer.parseInt(line.split("\t")[1]);
					// log += min + ":" + sec + ",最后一次输出为：" + oldHour + ":"
					// + oldMin + ":" + oldSec + "\r\n";
					oldSec = -1;
					sec = -1;
					needDeal = true;
					// continue;
				}
				if (line.startsWith("SEC\t")) {
					// log = "sec change.原来为：" + hour + ":" + min + ":" + sec
					// + ",更新以后为：" + hour + ":" + min + ":";
					sec = Integer.parseInt(line.split("\t")[1]);
					// log += sec + ",最后一次输出为：" + oldHour + ":" + oldMin + ":"
					// + oldSec + "\r\n";
					needDeal = true;
					// continue;
				}
				if (line.startsWith("SC\t")) {
					sc = line.replaceAll("\t", ":");
					needDeal = true;
					// continue;
				}
				if (line.startsWith("NC\t")) {
					nc = line.replaceAll("\t", ":");
					needDeal = true;
					// continue;
				}
				if (line.startsWith("GPS\t")) {
					needDeal = true;
					gpsarr = line.split("\t");

					lng = Double.parseDouble(gpsarr[1]);
					lat = Double.parseDouble(gpsarr[2]);
					altitude = Double.parseDouble(gpsarr[3]);
					speed = Double.parseDouble(gpsarr[4]);
				}
				if (line.startsWith("DM\t")) {
					dm = line.replaceAll("\t", ":");
					needDeal = true;
					// continue;
				}

				if (!needDeal) {
					continue;
				}

				log = "";

				// 提取信息
				temps = (line + "	end").split("\t");
				keyword = temps[0];
				Map<String, String> values = keywordValueValues.get(keyword);
				if (values == null) {
					values = new HashMap<String, String>();
					keywordValueValues.put(keyword, values);
				}
				Map<String, Integer> valueCols = keywordValueIndexs
						.get(keyword);
				if (valueCols == null) {
					logger.error("关键字：" + keyword + " 没有进行声明！");
					continue;
				}
				for (String value : valueCols.keySet()) {
					values.put(value, temps[valueCols.get(value)]);// 将对应的field的值提取出来
				}
				// 是否要输出
				if (sec != -1) {
					if (oldSec == -1) {
						canOutput = true;
					} else if (hour > oldHour || min > oldMin
							|| (sec - oldSec >= minSecInterval)) {
						canOutput = true;
					} else {
						log = "不满足输出要求：hour : " + hour + ":" + min + ":" + sec
								+ ",oldHour :" + oldHour + ":" + oldMin + ":"
								+ oldSec + "\r\n";
					}
				}
				if (canOutput) {
					if (checkValueOk(keywordsAndValueNames, keywordValueValues,
							defaultValues)) {
						// 输出
						// output = "time="
						// + (day + " " + hour + ":" + min + ":" + sec)
						// + ",lng=" + lng + ",lat=" + lat + ",speed="
						// + speed + ",altitude=" + altitude + ",sc=" + sc
						// + ",dm=" + dm + ",nc=" + nc;
						canOutput = false;

						// log = ">>>>>输出前时间情况：当前得到的时间：" + hour + ":" + min +
						// ":"
						// + sec + ", 上一次输出的时间情况：oldHour： " + hour + ":"
						// + min + ":" + oldSec + "\r\n";

						oldHour = hour;
						oldMin = min;
						oldSec = sec;
						// writerlog.write("sql----------  " +);
						sql = formBatchInsertSql(keywordValueValues);
						if (sql != null && !"".equals(sql)) {
							batchIntoMidTableSql.add(sql);
						}
						outputCnt++;
					}
				}

			} catch (Exception e) {
				logger.error("error! line:" + (lineCnt + 1) + ". content=["
						+ line + "]");
				e.printStackTrace();
			} finally {
				lineCnt++;
			}

		}
		long t2 = System.currentTimeMillis();
		reader.close();
		logger.info("total input line cnt=" + lineCnt + ",output line cnt="
				+ outputCnt + ",cost time:" + (t2 - t1));
		logger.info("keywordValueIndexs=" + keywordValueIndexs);
		logger.info("keywordValueValues=" + keywordValueValues);

		return batchIntoMidTableSql;
	}

	/**
	 * 检查各项值是否完整
	 * 
	 * @param keywordValueValues
	 *            当前的各项值的情况
	 * @param defaultValues
	 *            各项允许为空的值的默认值
	 * @return
	 * @author brightming 2013-11-25 下午3:00:38
	 */
	private boolean checkValueOk(
			Map<String, List<String>> keywordsAndValueNames,
			Map<String, Map<String, String>> keywordValueValues,
			Map<String, Map<String, String>> defaultValues) {

		if (keywordValueValues == null) {
			return false;
		}
		Map<String, String> currentKv = null;
		Map<String, String> defaultKv = null;
		String v = null;
		boolean create = false;
		for (String k : keywordsAndValueNames.keySet()) {
			create = false;
			currentKv = keywordValueValues.get(k);
			if (currentKv == null) {
				if (!defaultValues.containsKey(k)) {
					return false;
				} else {
					create = true;
					currentKv = new HashMap<String, String>();
					keywordValueValues.put(k, currentKv);
				}
			}
			defaultKv = defaultValues == null ? null : defaultValues.get(k);
			for (String f : currentKv.keySet()) {
				v = currentKv.get(f);
				if (v == null || "".equals(v.trim())) {
					if (defaultKv != null && defaultKv.containsKey(f)) {
						v = defaultKv.get(f);
					}
					if (v != null && !"".equals(v.trim())) {
						// System.out.println("用默认值："+v+",设置关键字："+k+"的字段："+f);
						currentKv.put(f, v);
					} else {
						// System.err
						// .println("设置关键字：" + k + "的字段：" + f + "不满足要求。");
						if (create) {
							keywordValueValues.remove(k);
						}
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 形成批处理的插入语句
	 * 
	 * @param keywordValueValues
	 * @return
	 * @author brightming 2013-11-25 下午4:05:47
	 * @throws IOException
	 */
	// Map<String, List<String>> keywordsAndValueNames = new HashMap<String,
	// List<String>>() {
	// {
	// put("GPS", Arrays.asList("Longitude", "Latitude", "Altitude",
	// "Speed"));
	// put("SC", Arrays.asList("MCC", "MNC", "LAC", "CI",
	// "HitsDistance"));
	// put("DM", Arrays.asList("RxLevSub", "RxQualSub"));
	// put("NC", Arrays.asList("Ni_BCCH", "Ni_BSIC", "Ni_RxLev"));
	// put("DAY", Arrays.asList("YY", "MN", "DD"));
	// put("HOUR", Arrays.asList("HH"));
	// put("MIN", Arrays.asList("MN"));
	// put("SEC", Arrays.asList("SS"));
	// }
	// };
	public String formBatchInsertSql(
			Map<String, Map<String, String>> keywordValueValues)
			throws IOException {
		String sql = "";

		String parameters = "";
		// day,hour,min,sec
		String time = keywordValueValues.get("DAY").get("YY") + "-"
				+ keywordValueValues.get("DAY").get("MN") + "-"
				+ keywordValueValues.get("DAY").get("DD") + " "
				+ keywordValueValues.get("HOUR").get("HH") + ":"
				+ keywordValueValues.get("MIN").get("MN") + ":"
				+ keywordValueValues.get("SEC").get("SS");

		parameters += time + ";";
		// gps
		try {
			String lng = keywordValueValues.get("GPS").get("Longitude");
			String lat = keywordValueValues.get("GPS").get("Latitude");
			String altitude = keywordValueValues.get("GPS").get("Altitude");
			String speed = keywordValueValues.get("GPS").get("Speed");

			String maplnglat = lng + "," + lat;

			parameters += lng + ";" + lat + ";" + altitude + ";" + speed + ";"
					+ maplnglat + ";";

			// sc
			String lac = keywordValueValues.get("SC").get("LAC");
			String ci = keywordValueValues.get("SC").get("CI");
			String distance = keywordValueValues.get("SC").get("HitsDistance");

			parameters += lac + ";" + ci + ";" + distance + ";";

			// dm
			String rxlevSub = keywordValueValues.get("DM").get("RxLevSub");
			String rxqualSub = keywordValueValues.get("DM").get("RxQualSub");

			parameters += rxlevSub + ";" + rxqualSub + ";";

			// nc
			String ncbcch = keywordValueValues.get("NC").get("Ni_BCCH")
					+ " END";
			String ncbsic = keywordValueValues.get("NC").get("Ni_BSIC")
					+ " END";
			String ncrxlev = keywordValueValues.get("NC").get("Ni_RxLev")
					+ " END";
			String[] ncbccharr = ncbcch.split(" ");
			String[] ncbsicarr = ncbsic.split(" ");
			String[] ncrxlevarr = ncrxlev.split(" ");

			if (ncbccharr.length != ncbsicarr.length
					|| ncbccharr.length != ncrxlevarr.length) {
				return "";
			}

			String ncinfo = "";
			int ncellCount = 0;
			for (int i = 0; i < ncbccharr.length - 1; i++) {// 最后一个不要
				if (ncbccharr[i].trim().isEmpty()) {
					// System.err.println("邻区信息空");
					ncinfo += "0,0,0,";
					continue;
				}
				ncellCount++;
				ncinfo += ncbccharr[i] + "," + ncbsicarr[i] + ","
						+ ncrxlevarr[i] + ",";
			}
			if (ncellCount == 0) {
				return "";
			}
			ncinfo = ncinfo.substring(0, ncinfo.length() - 1);

			parameters += ncellCount + ";";
			parameters += ncinfo.replaceAll(",", ";");

			// sql =
			// "insert into RNO_GSM_DT_MID(SAMPLE_TIME,LONGITUDE,LATITUDE,ALTITUDE,SPEED,MAP_LNGLAT,LAC,CI,DISTANCE,RXLEVSUB,RXQUALSUB,"
			// +
			// "NCELL_COUNT,NCELL_BCCH_1,NCELL_BSIC_1,NCELL_RXLEV_1,NCELL_BCCH_2,NCELL_BSIC_2,NCELL_RXLEV_2,"
			// +
			// "NCELL_BCCH_3,NCELL_BSIC_3,NCELL_RXLEV_3,NCELL_BCCH_4,NCELL_BSIC_4,NCELL_RXLEV_4 ,"
			// +
			// "NCELL_BCCH_5,NCELL_BSIC_5,NCELL_RXLEV_5,NCELL_BCCH_6,NCELL_BSIC_6,NCELL_RXLEV_6)"
			// + " VALUES(TO_DATE('"
			// + time
			// + "','YYYY-MM-DD HH24:mi:ss'),"
			// + lng
			// + ","
			// + lat
			// + ","
			// + altitude
			// + ","
			// + speed
			// + ",'"
			// + maplnglat
			// + "',"
			// + lac
			// + ","
			// + ci
			// + ","
			// + distance
			// + ","
			// + rxlevSub
			// + ","
			// + rxqualSub + "," + ncellCount + "," + ncinfo + ")";

			return parameters;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抛异常是，keywordValueValues=" + keywordValueValues);
			return "";
		}
	}

	private List<PlanConfig> addToAnalysisList(String code,
			Map<String, Object> attachParams, PlanConfig pc) {
		List<PlanConfig> planConfigs = null;
		if (attachParams == null) {
			logger.error("未带有session信息，不能支持添加到分析列表！");
		} else {
			HttpSession session = (HttpSession) attachParams.get("session");
			if (session == null) {
				logger.error("session为空！");
			} else {
				logger.info("准备添加到分析列表");
				planConfigs = (ArrayList<PlanConfig>) session
						.getAttribute(code);
				logger.info("当前session里的分析列表：" + planConfigs);
				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					session.setAttribute(code, planConfigs);
				}
				if (!planConfigs.contains(pc)) {
					planConfigs.add(pc);
				}
				session.setAttribute(code, planConfigs);
			}
		}
		return planConfigs;
	}

	class DTFileInfo {
		String dtLogVersion;
		String vendor;
		String version;
		String netMode;
		String time;// 格式：yyyymmddhhmmsskkk
		String device;
		String type;

		public String getYyyymmddhhmiss() {
			return time.substring(0, time.length() - 3);
		}

		public String getFileInfoName() {
			return vendor + " " + getYyyymmddhhmiss() + " " + netMode + " "
					+ type;
		}

		public String getDtLogVersion() {
			return dtLogVersion;
		}

		public void setDtLogVersion(String dtLogVersion) {
			this.dtLogVersion = dtLogVersion;
		}

		public String getVendor() {
			return vendor;
		}

		public void setVendor(String vendor) {
			this.vendor = vendor;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getNetMode() {
			return netMode;
		}

		public void setNetMode(String netMode) {
			this.netMode = netMode;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getDevice() {
			return device;
		}

		public void setDevice(String device) {
			this.device = device;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

}
