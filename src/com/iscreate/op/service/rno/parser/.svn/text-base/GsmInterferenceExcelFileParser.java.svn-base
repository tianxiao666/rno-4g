package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoInterference;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;


//干扰矩阵
public class GsmInterferenceExcelFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory
			.getLog(GsmInterferenceExcelFileParser.class);

	// ---spring 注入----//
	// public MemCachedClient memCached;
	public ExcelService excelService;
	private SysAreaDao sysAreaDao;

	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 地区 主小区 干扰小区 同频干扰系数 邻频干扰系数 是否邻区 距离(千米)
	private static List<String> expectTitles = Arrays.asList("地区", "主小区",
			"干扰小区", "同频干扰系数", "邻频干扰系数", "是否邻区", "距离(千米)");

	private static int titleSize = expectTitles.size();

	// public void setFileParserManager(IFileParserManager fileParserManager) {
	// this.fileParserManager = fileParserManager;
	// }
	//
	// public void setCache(MemcachedClient memCached) {
	// this.memCached = memCached;
	// }

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		log.debug("进入GsmInterferenceExcelFileParser方法：parseDataInternal。 token="
				+ token
				+ ",file="
				+ file
				+ ",needPersist="
				+ needPersist
				+ ",update="
				+ update
				+ ",oldConfigId="
				+ oldConfigId
				+ ",areaId=" + areaId);

		// 如果update为true，则需要更新系统干扰配置表
		// 如果update为false，则直接保存到临时方案表

		long begTime = new Date().getTime();

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
			log.info("准备导入干扰数据到：" + country.getName());
		}
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

		// 检查文件标题
		boolean titleok = super.checkTitles(expectTitles, allDatas.get(0));
		if (!titleok) {
			log.error("上传的干扰excel文件的格式不符合格式要求！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！确保文件的格式为：" + expectTitles);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		int total = allDatas.size() - 1;// excel有效记录数
		attachParams.put("collectType","干扰矩阵");
		if (update) {
			return toOverwriteSystemInterference(token, file, autoload,
					attachParams, allDatas, areaId, country.getName());
		} else {
			return saveAsTmpSchema(token, file, autoload, attachParams,
					allDatas, areaId, country.getName());
		}
	}

	/**
	 * 覆盖系统配置
	 * 
	 * @param token
	 * @param file
	 * @param autoload
	 * @param attachParams
	 * @param allDatas
	 * @param areaName
	 * @return
	 * @author brightming 2013-11-6 下午3:23:09
	 */
	private boolean toOverwriteSystemInterference(String token, File file,
			boolean autoload, Map<String, Object> attachParams,
			List<List<String>> allDatas, long areaId, String areaName) {

		// 1、插入中间表
		// 2、找到该区域的默认干扰配置项
		// 如果没有，则插入一条，并获取其id
		// 3、更新中间表的配置id
		// 4、
		// 如果该区域的系统干扰配置已经存在，则需要用干扰详细表的inter id更新中间表的inter id
		// 然后：
		// 用带有inter_id的中间表的数据更新正式表的数据；
		// 对于没有inter id的中间表数据，则直接插入到正式表

		String descTable = "RNO_INTERFERENCE_DESCRIPTOR";
		String midTable = "RNO_INTERFERENCE_TEMP_TABLE";
		String targetTable = "RNO_INTERFERENCE";

		String netType = attachParams.get("netType") + "";// "GSM";
		String collectType = attachParams.get("collectType") + "";// "MR";
		String collectTime = attachParams.get("collectTime") + "";
		String name = "系统现网干扰";// attachParams.get("name")+"";
		Date now = new Date();
		String createTime = sdf1.format(now);
		String insertIntoMidTable = "insert into "
				+ midTable
				+ "(NAME,NET_TYPE,COLLECT_TYPE,COLLECT_TIME,TEMP_STORAGE,AREA_ID,CREATE_TIME,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE) VALUES('"
				+ name + "','" + netType + "','" + collectType + "',to_date('"
				+ collectTime + "','yyyy-mm-dd HH24:mi:ss'),'Y'," + areaId
				+ ",TO_DATE('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),?,?,?,?,?,?)";

		// 插入描述表的语句
		String insertDescriptorSql = "insert into "
				+ descTable
				+ "(INTER_DESC_ID,NAME,NET_TYPE,COLLECT_TYPE,COLLECT_TIME,TEMP_STORAGE,DEFAULT_DESCRIPTOR,CREATE_TIME,MOD_TIME,STATUS,AREA_ID) VALUES("
				+ "SEQ_RNO_INTERFERENCE_DESC.NEXTVAL,'" + name + "','"
				+ netType + "','" + collectType + "',to_date('" + collectTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'N','Y',to_date('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),to_date('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'N'," + areaId + ")";

		// 插入小区临时干扰数据表（目标表）
		String interFields = "DESCRIPTOR_ID,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE";
		String appendNewRecordSql = "insert into " + targetTable + "(INTER_ID,"
				+ interFields + ") " + " select "
				+ " SEQ_RNO_INTERFERENCE.NEXTVAL," + interFields + " FROM "
				+ midTable + " tb WHERE NVL(INTER_ID,0)=0";

		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		int errorCnt = 0;
		int successCnt = 0;

		// 1、插入中间表

		PreparedStatement insertMidSqlpstmt = null;
		try {
			insertMidSqlpstmt = connection.prepareStatement(insertIntoMidTable);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=401");
			return false;
		}

		int rowIndex = 1;
		RnoInterference ri = null;
		StringBuilder buf = new StringBuilder();
		int index = 1;
		int total = allDatas.size() - 1;
		for (; rowIndex < allDatas.size(); rowIndex++) {
			ri = createFromExcelLine(areaName, allDatas.get(rowIndex),
					rowIndex, buf);
			if (ri == null) {
				errorCnt++;
				continue;
			}
			successCnt++;
			index = 1;
			try {
				insertMidSqlpstmt.setString(index++, ri.getCell());
				insertMidSqlpstmt.setString(index++, ri.getInterferenceCell());
				insertMidSqlpstmt.setDouble(index++, ri.getCi());
				insertMidSqlpstmt.setDouble(index++, ri.getCa());
				insertMidSqlpstmt.setString(index++, ri.getIsNeighbour());
				insertMidSqlpstmt.setDouble(index++, ri.getDistance());

				insertMidSqlpstmt.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				errorCnt++;
				successCnt--;
			}

			fileParserManager.updateTokenProgress(token, rowIndex * 1.0f
					/ total * 0.5f);
		}

		log.info("成功创建了插入中间表的批处理语句：" + successCnt + "条，失败：" + errorCnt + "条");
		if (successCnt == 0) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						buf.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			fail(connection, token, "上传的excel文件未包含有效的数据 ！");
			return false;
		}
		log.info("准备插入中间表。。。。");
		try {
			int[] r = insertMidSqlpstmt.executeBatch();
			log.info("批处理插入中间表的结果：" + r);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=402");
			return false;
		}
		
		fileParserManager.updateTokenProgress(token, 0.6f);

		// 2、找到该区域的默认干扰配置项
		log.info("~~~~~~~~~~~准备获取区域" + areaName + "下的系统干扰项。");
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=403");
			return false;
		}
		String findExistsDescriptorSql = "select INTER_DESC_ID from RNO_INTERFERENCE_DESCRIPTOR where AREA_ID="
				+ areaId + " and DEFAULT_DESCRIPTOR='Y'";
		log.info("寻找区域：" + areaName + "下的系统干扰配置sql：" + findExistsDescriptorSql);
		fileParserManager.updateTokenProgress(token, 0.65f);
		
		ResultSet rs = null;
		long existDescriptorId = -1;// 系统干扰描述项id
		boolean existDescriptor = false;// 是否已经存在系统干扰项
		try {
			rs = statement.executeQuery(findExistsDescriptorSql);
			while (rs.next()) {
				existDescriptor = true;
				existDescriptorId = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=404");
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		fileParserManager.updateTokenProgress(token, 0.7f);
		log.info("区域：" + areaName + "下是否已经存在系统干扰项？" + existDescriptor);
		if (!existDescriptor) {
			// 插入系统干扰描述项
			log.info("准备插入区域" + areaName + "的系统干扰描述项。");
			try {
				log.info("创建新的系统干扰项的sql=" + insertDescriptorSql);
				statement.executeUpdate(insertDescriptorSql);
			} catch (SQLException e) {
				e.printStackTrace();
				fail(connection, token, "数据处理出错！code=405");
				return false;
			}
			log.info("新的系统干扰项创建完成，准备获取其id。");
			try {
				log.info("获取新创建的干扰项的id的sql="+findExistsDescriptorSql);
				rs = statement.executeQuery(findExistsDescriptorSql);
				while (rs.next()) {
					existDescriptorId = rs.getLong(1);
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				fail(connection, token, "数据处理出错！code=404");
				try {
					statement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}

		
		// 经过此处理以后，干扰描述项的id不能为-1
		if (existDescriptorId == -1) {
			log.error("数据处理出错！code=406");
			fail(connection, token, "数据处理出错！code=406");
			return false;
		}
		log.info("~~~~~~~~~~~完成获取区域" + areaName + "下的系统干扰项。其id="
				+ existDescriptorId);

		// 3、更新中间表的配置id
		log.info("----------准备更新中间表的系统描述项id。id=" + existDescriptorId);
		String sql = "update " + midTable + " set DESCRIPTOR_ID="
				+ existDescriptorId;
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=407");
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			
		}
		log.info("----------完成更新中间表的系统描述项id");
		fileParserManager.updateTokenProgress(token, 0.8f);
		// 4、
		// 如果该区域的系统干扰配置已经存在，则需要用干扰详细表的inter id更新中间表的inter id
		// 然后：
		// 用带有inter_id的中间表的数据更新正式表的数据；
		// 对于没有inter id的中间表数据，则直接插入到正式表
		log.info("~~~~~~~~~~~~准备移动中间表的数据到目标表：" + targetTable);
		sql = "merge into "
				+ midTable
				+ " ta using "
				+ targetTable
				+ " tb on (ta.CELL=tb.CELL AND ta.INTERFERENCE_CELL=tb.INTERFERENCE_CELL) when matched then update set ta.INTER_ID=tb.INTER_ID";
		log.info("用系统干扰详细内容的id更新中间表的interid的sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=408");
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			
		}
		fileParserManager.updateTokenProgress(token, 0.85f);

		// 用中间表的数据更新目标表的数据
		sql = "merge into "
				+ targetTable
				+ " ta using "
				+ midTable
				+ " tb on (ta.INTER_ID=tb.INTER_ID) when matched then update set ta.CI=tb.CI,ta.CA=tb.CA,ta.IS_NEIGHBOUR=tb.IS_NEIGHBOUR,ta.DISTANCE=tb.DISTANCE";
		log.info("用中间表的数据更新目标表的数据的sql=" + sql);
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=409");
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			
		}

		fileParserManager.updateTokenProgress(token, 0.88f);
		// 将中间表的新数据加入到目标表
		try {
			statement.executeUpdate(appendNewRecordSql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=410");
			try {
				statement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			
		}
		fileParserManager.updateTokenProgress(token, 0.9f);
		log.info("~~~~~~~~~~~~完成移动中间表的数据到目标表：" + targetTable);

		List<PlanConfig> planConfigs = null;
		if (autoload) {
			log.info("需加载到分析列表。");
			PlanConfig pc = new PlanConfig();
			pc.setConfigId(existDescriptorId);
			pc.setSelected(false);
			pc.setTemp(false);
			pc.setType("INTERFERENCEDATA");
			pc.setName(name);
			pc.setTitle(areaName + netType + "小区干扰");
			pc.setCollectTime(collectTime);

			planConfigs = addToAnalysisList(attachParams, pc);
		}
		
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			insertMidSqlpstmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


		fileParserManager.updateTokenProgress(token, 0.95f);
		String result = "{'msg':'处理excel记录：" + (total) + "条。成功：" + successCnt
				+ "条，失败：" + errorCnt + "条。<br/>详情如下： " + buf.toString();
		result += "'";

		if (autoload) {
			String conList = super.gson.toJson(planConfigs);
			result += ",'list':" + conList;
		}
		result += "}";
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 导入临时方案表
	 * 
	 * @param token
	 * @param file
	 * @param autoload
	 * @param attachParams
	 * @param allDatas
	 * @param areaName
	 * @return
	 * @author brightming 2013-11-6 下午3:25:58
	 */
	private boolean saveAsTmpSchema(String token, File file, boolean autoload,
			Map<String, Object> attachParams, List<List<String>> allDatas,
			long areaId, String areaName) {

		// 总体处理过程：
		// 1、插入中间表
		// 2、创建描述项
		// 3、更新中间表的描述项id
		// 4、将中间表的数据移动到目标表
		// 5、看情况是否加载

		String descTable = "RNO_INTERFERENCE_DESCRIPTOR";
		String midTable = "RNO_INTERFERENCE_TEMP_TABLE";
		String targetTable = "RNO_TEMP_INTERFERENCE";

		String netType = attachParams.get("netType") + "";// "GSM";
		String collectType = attachParams.get("collectType") + "";// "MR";
		String collectTime = attachParams.get("collectTime") + "";
		String name = attachParams.get("name") + "";
		Date now = new Date();
		String createTime = sdf1.format(now);
		String insertIntoMidTable = "insert into "
				+ midTable
				+ "(NAME,NET_TYPE,COLLECT_TYPE,COLLECT_TIME,TEMP_STORAGE,AREA_ID,CREATE_TIME,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE) VALUES('"
				+ name + "','" + netType + "','" + collectType + "',to_date('"
				+ collectTime + "','yyyy-mm-dd HH24:mi:ss'),'Y'," + areaId
				+ ",TO_DATE('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),?,?,?,?,?,?)";

		// 插入描述表的语句
		String insertDescriptorSql = "insert into "
				+ descTable
				+ "(INTER_DESC_ID,NAME,NET_TYPE,COLLECT_TYPE,COLLECT_TIME,TEMP_STORAGE,DEFAULT_DESCRIPTOR,CREATE_TIME,MOD_TIME,STATUS,AREA_ID) VALUES("
				+ "SEQ_RNO_INTERFERENCE_DESC.NEXTVAL,'" + name + "','"
				+ netType + "','" + collectType + "',to_date('" + collectTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'Y','N',to_date('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),to_date('" + createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'N'," + areaId + ")";

		// 插入小区临时干扰数据表（目标表）
		String interFields = "DESCRIPTOR_ID,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE";
		String appendNewRecordSql = "insert into " + targetTable + "(INTER_ID,"
				+ interFields + ") " + " select "
				+ " SEQ_RNO_TEMP_INTERFERENCE.NEXTVAL," + interFields
				+ " FROM " + midTable;

		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		int errorCnt = 0;
		int successCnt = 0;

		// 1、插入中间表

		PreparedStatement insertMidSqlpstmt = null;
		try {
			insertMidSqlpstmt = connection.prepareStatement(insertIntoMidTable);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=100");
			return false;
		}

		int rowIndex = 1;
		RnoInterference ri = null;
		StringBuilder buf = new StringBuilder();
		int index = 1;
		int total = allDatas.size() - 1;
		for (; rowIndex < allDatas.size(); rowIndex++) {
			ri = createFromExcelLine(areaName, allDatas.get(rowIndex),
					rowIndex, buf);
			if (ri == null) {
				errorCnt++;
				continue;
			}
			successCnt++;
			index = 1;
			try {
				insertMidSqlpstmt.setString(index++, ri.getCell());
				insertMidSqlpstmt.setString(index++, ri.getInterferenceCell());
				insertMidSqlpstmt.setDouble(index++, ri.getCi());
				insertMidSqlpstmt.setDouble(index++, ri.getCa());
				insertMidSqlpstmt.setString(index++, ri.getIsNeighbour());
				insertMidSqlpstmt.setDouble(index++, ri.getDistance());

				insertMidSqlpstmt.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				errorCnt++;
				successCnt--;
			}

			fileParserManager.updateTokenProgress(token, rowIndex * 1.0f
					/ total * 0.5f);
		}

		log.info("成功创建了插入中间表的批处理语句：" + successCnt + "条，失败：" + errorCnt + "条");
		if (successCnt == 0) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						buf.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			fail(connection, token, "上传的excel文件未包含有效的数据 ！");
			return false;
		}
		log.info("准备插入中间表。。。。");
		try {
			int[] r = insertMidSqlpstmt.executeBatch();
			log.info("批处理插入中间表的结果：" + r);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=300");
			return false;
		}

		fileParserManager.updateTokenProgress(token, 0.55f);
		// 2、创建描述项
		log.info("-----------准备创建新的描述项。");
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=301");
			return false;
		}
		try {
			statement.executeUpdate(insertDescriptorSql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=302");
			return false;
		}
		log.info("-----------新的描述项创建完毕。");

		// 3、更新中间表的描述项id
		log.info("~~~~~~~准备更新中间标的描述项id");
		String updateMidTableDescriptorId = "merge into "
				+ midTable
				+ " tt USING RNO_INTERFERENCE_DESCRIPTOR tb on(\"TO_CHAR\"(tt.CREATE_TIME,'YYYY-MM-DD HH24:mi:ss')=\"TO_CHAR\"(tb.CREATE_TIME,'YYYY-MM-DD HH24:mi:ss') AND tt.AREA_ID=tb.AREA_ID AND tt.NET_TYPE=tb.NET_TYPE AND tt.NAME=tb.NAME)"
				+ " when matched THEN update set tt.DESCRIPTOR_ID=tb.INTER_DESC_ID";
		log.info("用新创建的干扰矩阵描述项id更新中间表的DESCRIPTOR_ID的sql语句："
				+ updateMidTableDescriptorId);
		try {
			int updateNewDataDescriptorIdCnt = statement
					.executeUpdate(updateMidTableDescriptorId);
			log.info("用新创建的干扰矩阵描述项id更新中间表的DESCRIPTOR_ID的记录数量："
					+ updateNewDataDescriptorIdCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=303");
			return false;
		}
		log.info("~~~~~~~中间标的描述项id更新完毕");
		fileParserManager.updateTokenProgress(token, 0.6f);
		// 4、将中间表的数据移动到目标表
		log.info("-----------获取新创建的描述项id：");
		long newDescriptorId = -1;
		String sql = "select distinct(DESCRIPTOR_ID) from " + midTable;
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				newDescriptorId = rs.getLong(1);
				log.info("获取得到的新创建的干扰描述项id=" + newDescriptorId);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=304");
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
		log.info("-----------获取新创建的描述项id完成。其id=" + newDescriptorId);
		fileParserManager.updateTokenProgress(token, 0.7f);

		log.info("~~~~~~~~~~~~准备将中间表的数据移动到目标表：" + targetTable);
		log.info("sql="+appendNewRecordSql);
		try {
			statement.executeUpdate(appendNewRecordSql);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=305");
			return false;
		}
		log.info("~~~~~~~~~~~~完成移动中间表的数据到目标表：" + targetTable);

		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			insertMidSqlpstmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		fileParserManager.updateTokenProgress(token, 0.8f);
		// 5、看情况是否加载
		List<PlanConfig> planConfigs = null;
		if (autoload) {
			log.info("需加载到分析列表。");
			PlanConfig pc = new PlanConfig();
			pc.setConfigId(newDescriptorId);
			pc.setSelected(false);
			pc.setTemp(true);
			pc.setType("INTERFERENCEDATA");
			pc.setName(name);
			pc.setTitle(areaName + netType + "小区干扰");
			pc.setCollectTime(collectTime);

			planConfigs = addToAnalysisList(attachParams, pc);
		}
		fileParserManager.updateTokenProgress(token, 0.9f);
		String result = "{'msg':'处理excel记录：" + (total) + "条。成功：" + successCnt
				+ "条，失败：" + errorCnt + "条。<br/>详情如下： " + buf.toString();
		result += "'";

		if (autoload) {
			String conList = super.gson.toJson(planConfigs);
			result += ",'list':" + conList;
		}
		result += "}";
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result);
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
						.getAttribute(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
				log.info("当前session里的干扰分析列表：" + planConfigs);
				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					session.setAttribute(
							RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
							planConfigs);
				}
				if (!planConfigs.contains(pc)) {
					planConfigs.add(pc);
				}
				session.setAttribute(
						RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
						planConfigs);
			}
		}
		return planConfigs;
	}

	private RnoInterference createFromExcelLine(String country,
			List<String> oneData, int i, StringBuilder buf) {

		String msg = "";

		String area = "";
		String cell = "";
		String intercell = "";
		double ci = 0;
		double ca = 0;
		String isNeighbour = "";
		double distance = 0;
		try {
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

			area = oneData.get(0);
			if (area == null || !area.equals(country)) {
				msg = "第[" + (i + 1) + "]行错误！地区信息不正确，应该为：" + country + " ！";
				buf.append(msg + "<br/>");
				return null;
			}
			cell = oneData.get(1);
			if (cell == null || cell.trim().equals("")) {
				msg = "第[" + (i + 1) + "]行错误！未包含小区信息 ！";
				buf.append(msg + "<br/>");
				return null;
			}

			intercell = oneData.get(2);
			if (intercell == null || intercell.trim().equals("")) {
				msg = "第[" + (i + 1) + "]行错误！未包含干扰小区信息 ！";
				buf.append(msg + "<br/>");
				return null;
			}

			try {
				ci = Double.parseDouble(oneData.get(3));
			} catch (Exception e3) {
				msg = "第[" + (i + 1) + "]行错误！非法的同频干扰系数 ！";
				buf.append(msg + "<br/>");
				return null;
			}

			try {
				ca = Double.parseDouble(oneData.get(4));
			} catch (Exception e3) {
				msg = "第[" + (i + 1) + "]行错误！非法的邻频干扰系数 ！";
				buf.append(msg + "<br/>");
				return null;
			}

			isNeighbour = oneData.get(5);
			if (isNeighbour == null || "".equals(isNeighbour.trim())) {
				isNeighbour = "N";
			} else {
				if ("false".equalsIgnoreCase(isNeighbour)) {
					isNeighbour = "N";
				} else {
					isNeighbour = "Y";
				}
			}

			try {
				distance = Double.parseDouble(oneData.get(6));
			} catch (Exception e3) {
				e3.printStackTrace();
				msg = "第[" + (i + 1) + "]行错误！包含无效的距离数据 ！";
				buf.append(msg + "<br/>");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "第[" + (i + 1) + "]行错误！包含无效数据 ！";
			buf.append(msg + "<br/>");
			return null;
		}

		RnoInterference ri = new RnoInterference();
		ri.setCa(ca);
		ri.setCi(ci);
		ri.setCell(cell);
		ri.setDistance(distance);
		ri.setInterferenceCell(intercell);
		ri.setIsNeighbour(isNeighbour);

		return ri;
	}

//	/**
//	 * 中间执行出错后的处理
//	 * 
//	 * @param connection
//	 * @author brightming 2013-11-6 下午4:22:21
//	 */
//	private void fail(Connection connection, String token, String errMsg) {
//		log.error("中间出错！msg=" + errMsg);
//		try {
//			connection.rollback();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			connection.setAutoCommit(true);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
