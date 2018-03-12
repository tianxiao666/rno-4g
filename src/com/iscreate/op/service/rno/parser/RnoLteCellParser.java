package com.iscreate.op.service.rno.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoLteCellParser extends TxtFileParserBase {

	private static Log log = LogFactory.getLog(RnoLteCellParser.class);

	// ---spring 注入----//
	private SysAreaDao sysAreaDao;
	private AreaLockManager areaLockManager;
	private static Gson gson = new GsonBuilder().create();// 线程安全

	// 导入的3中模式
	private static String OVERWRITE_MODE = "overwrite";
	private static String APPEND_MODE = "append";
	private static String DELETE_MODE = "delete";

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setAreaLockManager(AreaLockManager areaLockManager) {
		this.areaLockManager = areaLockManager;
	}

	// // 只存在于中间表，不存在目标表的字段
	// private static List<String> fieldsNotExistInTargetTab = Arrays.asList(
	// "CELL_ID", "AREA_NAME");

	// 构建insert语句
	private static String midTable = "RNO_LTE_IMPORT_MID";
	private static String insertInoMidTableSql = "";
	//areaId在sql序列中的位置记录
	Map<String, Integer> areaSqlPosition = new HashMap<String, Integer>();	
	String excelAreaIds = "";// excel出现的区域对应的id
	Set<Long> excelAreaIdSet=new HashSet<Long>();
	// 区域名称到id的映射
	Map<String, Long> areaNameToIds = new HashMap<String, Long>();
	//BUSINESS_CELL_ID=BUSINESS_ENODEB_ID||LOCAL_CELLID
	Map<String, String> businessCellIds = new HashMap<String, String>();
	//成功与失败的数量
	int failCnt = 0, sucCnt = 0;
	/**
	 * 
	 * @param token
	 * @param file
	 * @param needPersist
	 * @param update
	 * @param oldConfigId
	 * @param areaId
	 * @param autoload
	 * @param attachParams
	 * @return
	 * @author brightming 2014-5-16 上午10:06:26
	 */
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		log.debug("进入RnoLteCellParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);

		// 导入模式
		String importMode = (String) attachParams.get("mode");
		if (importMode == null || "".equals(importMode.trim())) {
			importMode = OVERWRITE_MODE;
		}
		if (!importMode.equals(OVERWRITE_MODE) && !importMode.equals(APPEND_MODE)
				&& !importMode.equals(DELETE_MODE)) {
			log.error("导入模式[" + importMode + "]无效！");
			super.setCachedInfo(token, "导入模式[" + importMode + "]无效！");
			return false;
		}

		
		super.setCachedInfo(token, "正在分析格式有效性...");
		fileParserManager.updateTokenProgress(token,  "正在分析格式有效性...");
		
		long begTime = new Date().getTime();
		long t1,t2;
		// 当前的areaid的含义是省 的id
		long provinceId = areaId;

		List<Map<String, Object>> allAreas = sysAreaDao
				.getSubAreasInSpecAreaLevel(new long[] { provinceId }, "市");
		if (allAreas == null || allAreas.isEmpty()) {
			log.error("province=" + provinceId + " 下没有市设置！");
			super.setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"指定的导入省不存在任何市区域，请联系管理员！");
			return false;
		}
		log.debug("allAreas=" + allAreas);
		
		long oneid = -1;
		for (Map<String, Object> one : allAreas) {
			oneid = ((BigDecimal) one.get("AREA_ID")).longValue();
			if (oneid == provinceId) {
				continue;
			}
			areaNameToIds.put(one.get("NAME").toString(), oneid);
		}
		log.debug("areaNameToIds=" + areaNameToIds);

		// 获取标题情况
		StringBuilder checkMsg = new StringBuilder();
		Map<String, DBFieldToTitle> dbFieldsToTitles = readDbToTitleCfgFromXml();
	
		// 准备数据库插入statement
		/*PreparedStatement PStatement = null;
		log.debug("插入中间表的sql语句为：" + insertInoMidTableSql);
		try {
			PStatement = connection.prepareStatement(insertInoMidTableSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！insertInoMidTableSql="
					+ insertInoMidTableSql);
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}*/

		
		super.setCachedInfo(token, "正在逐行分析数据...");
		
		// --------逐行分析excel csv数据，转换为导入中间表的sql---//
		StringBuilder bufMsg = new StringBuilder();
		
		int index;
		boolean excelLineOk;
		String msg;
		long oneAreaId;
		String excelAreaIds = "";// excel出现的区域对应的id
		boolean parseOk = false;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			msg = "准备处理数据时出错！code=303";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());

			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}
		parseOk=parseLteCell(token, file, connection, stmt,dbFieldsToTitles,bufMsg);
		if(parseOk){
			msg="解析文件成功!";
			bufMsg.append(msg + "<br/>");
			setCachedInfo(token, bufMsg.toString());
		}else{
			msg="解析文件出现错误!";
			bufMsg.append(msg + "<br/>");
			setCachedInfo(token, bufMsg.toString());
			return false;
		}
		if(!excelAreaIdSet.isEmpty()){
			for(Long id:excelAreaIdSet){
				excelAreaIds += id + ",";
			}
		}
		if (!"".equals(excelAreaIds)) {
			excelAreaIds = excelAreaIds.substring(0, excelAreaIds.length() - 1);
		}

		
		super.setCachedInfo(token, "正在执行数据导入...");
		log.info("lte导入，准备阶段：sucCnt=" + sucCnt + ",failCnt=" + failCnt);
		// --------处理中间表-----//
		if (sucCnt == 0) {
			log.warn("导入的excel表的数据统统无效!");
			super.setCachedInfo(token, bufMsg.toString());
			return false;
		}
		try {
			log.debug("准备批量插入：");
			t1=System.currentTimeMillis();
//			PStatement.executeBatch();
			t2=System.currentTimeMillis();
			log.debug("批量插入完成，耗时："+(t2-t1)+"ms.");
		} catch (Exception e) {
			e.printStackTrace();
			msg = "数据保存时出错！code=302";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/

			return false;
		}

		
		String sql = "";

		
		super.setCachedInfo(token, "正在执行数据整理...");
		// 更新临时表enodeb的存在状态
		// --更新为1--//
		sql = "update "
				+ midTable
				+ " set ENODEB_STATUS=1 where BUSINESS_ENODEB_ID in (select BUSINESS_ENODEB_ID from RNO_LTE_ENODEB where AREA_ID IN ("
				+ excelAreaIds + ") AND STATUS='N')";
		log.info("准备更新临时表enodeb的存在状态为1的sql=" + sql);
		t1=System.currentTimeMillis();
		int affectCnt = 0;
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2=System.currentTimeMillis();
			log.info("更新临时表enodeb的存在状态为1的sql执行成功，影响：" + affectCnt+"，耗时："+(t2-t1)+"ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新临时表enodeb的存在状态为1的sql执行失败！sql=" + sql);
			msg = "处理数据时出错！code=3041";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				stmt.close();
			} catch (SQLException e1) {
			}
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}
		//系统存在，但是标识为删除状态
		sql = "update "
				+ midTable
				+ " set ENODEB_STATUS=3 where BUSINESS_ENODEB_ID in (select BUSINESS_ENODEB_ID from RNO_LTE_ENODEB where AREA_ID IN ("
				+ excelAreaIds + ") AND STATUS='D')";
		log.info("准备更新临时表enodeb的存在状态为1的sql=" + sql);
		t1=System.currentTimeMillis();
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2=System.currentTimeMillis();
			log.info("更新临时表enodeb的存在状态为3的sql执行成功，影响：" + affectCnt+"，耗时："+(t2-t1)+"ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新临时表enodeb的存在状态为3的sql执行失败！sql=" + sql);
			msg = "处理数据时出错！code=3042";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				stmt.close();
			} catch (SQLException e1) {
			}
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}

		// 更新临时表cell的存在状态
		// business_cell_id
		sql = "update " + midTable
				+ " set BUSINESS_CELL_ID=BUSINESS_ENODEB_ID||LOCAL_CELLID";
		log.debug("准备更新lte小区导入临时表的business_cell_id,sql="+sql);
		try {
			t1=System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			t2=System.currentTimeMillis();
			log.info("更新临时表小区的business_cell_id的sql执行成功，影响：" + affectCnt+",耗时:"+(t2-t1)+"ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新临时表小区的business_cell_id的sql执行失败！sql=" + sql);
			msg = "处理数据时出错！code=305";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				stmt.close();
			} catch (SQLException e1) {
			}
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}
		sql = "merge into "
				+ midTable
				+ " tar using (select lte_cell_id,business_cell_id FROM RNO_LTE_CELL WHERE AREA_ID IN ( "
				+ excelAreaIds
				+ " ) AND STATUS='N') src on (tar.BUSINESS_CELL_ID=src.BUSINESS_CELL_ID) WHEN MATCHED THEN UPDATE SET TAR.LTE_CELL_ID=SRC.LTE_CELL_ID,TAR.CELL_STATUS=1";
		log.debug("准备更新临时表小区的存在状态：sql="+sql);
		t1=System.currentTimeMillis();
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2=System.currentTimeMillis();
			log.info("更新临时表小区的小区存在状态的sql执行成功，影响：" + affectCnt+",耗时："+(t2-t1)+"ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新临时表小区的小区存在状态的sql执行失败！sql=" + sql);
			msg = "处理数据时出错！code=3061";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				stmt.close();
			} catch (SQLException e1) {
			}
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}
		//对于系统表中存在，但是被标识为删除状态的，也要一个单独的状态表示：
		sql = "merge into "
				+ midTable
				+ " tar using (select lte_cell_id,business_cell_id FROM RNO_LTE_CELL WHERE AREA_ID IN ( "
				+ excelAreaIds
				+ " ) and status='D') src on (tar.BUSINESS_CELL_ID=src.BUSINESS_CELL_ID) WHEN MATCHED THEN UPDATE SET TAR.LTE_CELL_ID=SRC.LTE_CELL_ID,TAR.CELL_STATUS=3";
		log.debug("准备更新临时表小区的存在状态为3（系统存在但标识为删除状态）：sql="+sql);
		t1=System.currentTimeMillis();
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2=System.currentTimeMillis();
			log.info("准备更新临时表小区的存在状态为3（系统存在但标识为删除状态）执行成功，影响：" + affectCnt+",耗时："+(t2-t1)+"ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("准备更新临时表小区的存在状态为3（系统存在但标识为删除状态）sql执行失败！sql=" + sql);
			msg = "处理数据时出错！code=3062";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				stmt.close();
			} catch (SQLException e1) {
			}
			/*try {
				PStatement.close();
			} catch (SQLException e1) {
			}*/
			return false;
		}
		
		//---打印
//		sql="select BUSINESS_CELL_ID,BUSINESS_ENODEB_ID,LOCAL_CELLID,CELL_ID,ENODEB_STATUS,CELL_STATUS from "+midTable;
//		List<Map<String,Object>> rest=RnoHelper.commonQuery(stmt, sql);
//		log.debug("临时表的BUSINESS_CELL_ID情况：----");
//		int i=0;
//		for(Map<String,Object> one:rest){
//			msg="";
//			for(String k:one.keySet()){
//				msg+=k+"="+one.get(k)+",";
//			}
//			log.debug((i++)+"---:"+msg);
//		}
//		log.debug("临时表的BUSINESS_CELL_ID情况输出结束。");
//		
		
		// --------操作系统表-----//
		boolean ok;
		if(importMode.equals(OVERWRITE_MODE)){
			ok=importOverwrite(stmt);
		}else if(importMode.equals(APPEND_MODE)){
			ok=importAppend(stmt);
		}else{
			ok=importDelete(stmt);
		}

		log.debug("导入数据操作的执行结果为："+ok);
		long endTime=System.currentTimeMillis();
		t2=endTime-begTime;//耗时
		
		String res="";//bufMsg.toString();
		if(ok){
			res="导入数据成功！耗时："+(t2/1000)+"s。成功处理："+sucCnt+"条记录，失败："+failCnt+"。";
			if(failCnt>0){
				res+="详情如下：<br/>"+bufMsg.toString();
			}
		}else{
			res="导入操作在数据处理时失败！耗时："+(t2/1000)+"s。有效数据数量："+sucCnt+"条，无效数据数量："+failCnt+"。";
			if(failCnt>0){
				res+="无效数据详情如下：<br/>"+bufMsg.toString();
			}
		}
		sucCnt=0;failCnt=0;businessCellIds.clear();
		super.setCachedInfo(token, res);
		log.debug("导入的最终结果："+res);
		/*try {
			PStatement.clearBatch();
		} catch (SQLException e) {
		}
		try {
			PStatement.close();
		} catch (SQLException e) {
		}*/
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

	/**
	 * 新增模式导入
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-5-16 下午4:04:41
	 */
	private boolean importAppend(Statement stmt) {
		log.debug("进入方法：--------importAppend");
		long begTime = System.currentTimeMillis();
		long t1 = System.currentTimeMillis();
		// 将enodeb_status=2的enodeb信息插入到enodeb表
		String enodebFields = "BUSINESS_ENODEB_ID,SUBNET_ID,MANAGENET_ID,ENODEB_NAME,AREA_ID,SITE_STYLE,LONGITUDE,LATITUDE,BUILD_TYPE,STATION_CFG,FRAME_CFG,SPECIAL_FRAME_CFG,MME,MCC,MNC,COVER_AREA,BUILD_PHASE,TDS_SITE_NAME,G2_SITE_NAME,SHARE_TYPE,STATE,OPERATION_TIME,CLUSTER_NUM,BELONGING_OMC,IS_HIGH_SPEED,IS_VIP,COVER_AREA_TYPE,ADDRESS,REMARK";
		String sql = "insert into RNO_LTE_ENODEB (ENODEB_ID," + enodebFields
				+ ",STATUS) select SEQ_LTE_ENODEB_ID.NEXTVAL," + enodebFields
				+ ",'N' from ( SELECT "+enodebFields+",ROW_NUMBER() OVER(partition by business_enodeb_id order by business_enodeb_id desc) RN FROM " + midTable + " where enodeb_status=2) WHERE RN=1";
		log.debug("开始执行将临时表enodeb_status=2的enodeb信息插入到enodeb表，sql=" + sql);
		long t2;
		int affCnt=0;
		try {
			affCnt=stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("将临时表enodeb_status=2的enodeb信息插入到enodeb表执行完成，影响："+affCnt+",耗时："
					+ (t2 - t1) + "ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("将临时表enodeb_status=2的enodeb信息插入到enodeb表出错！sql=" + sql);
			return false;
		}
		// 更新enodeb_id
		sql = "merge into "
				+ midTable
				+ " tar using (select enodeb_id,business_enodeb_id from RNO_LTE_ENODEB WHERE AREA_ID IN (SELECT DISTINCT(AREA_ID) FROM "
				+ midTable
				+ ")) src on (tar.business_enodeb_id=src.business_enodeb_id) when matched then update set tar.enodeb_id=src.enodeb_id";
		log.debug("更新临时表" + midTable + " 的enodeb 的数据库id，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			affCnt=stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("更新临时表" + midTable + " 的enodeb 的数据库id执行完成，影响："+affCnt+",耗时："
					+ (t2 - t1) + "ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新临时表" + midTable + " 的enodeb 的数据库id执行出错！sql=" + sql);
			return false;
		}
		
		
		//将enodeb_status=3的更新到enodeb表
		enodebFields = "BUSINESS_ENODEB_ID,SUBNET_ID,MANAGENET_ID,ENODEB_NAME,AREA_ID,SITE_STYLE,LONGITUDE,LATITUDE,BUILD_TYPE,STATION_CFG,FRAME_CFG,SPECIAL_FRAME_CFG,MME,MCC,MNC,COVER_AREA,BUILD_PHASE,TDS_SITE_NAME,G2_SITE_NAME,SHARE_TYPE,STATE,OPERATION_TIME,CLUSTER_NUM,BELONGING_OMC,IS_HIGH_SPEED,IS_VIP,COVER_AREA_TYPE,ADDRESS,REMARK";
		String enodebUpdateFields = "TAR.STATUS='N',TAR.SUBNET_ID=SRC.SUBNET_ID,TAR.MANAGENET_ID=SRC.MANAGENET_ID,TAR.ENODEB_NAME=SRC.ENODEB_NAME,TAR.AREA_ID=SRC.AREA_ID,TAR.SITE_STYLE=SRC.SITE_STYLE,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.BUILD_TYPE=SRC.BUILD_TYPE,TAR.STATION_CFG=SRC.STATION_CFG,TAR.FRAME_CFG=SRC.FRAME_CFG,TAR.SPECIAL_FRAME_CFG=SRC.SPECIAL_FRAME_CFG,TAR.MME=SRC.MME,TAR.MCC=SRC.MCC,TAR.MNC=SRC.MNC,TAR.COVER_AREA=SRC.COVER_AREA,TAR.BUILD_PHASE=SRC.BUILD_PHASE,TAR.TDS_SITE_NAME=SRC.TDS_SITE_NAME,TAR.G2_SITE_NAME=SRC.G2_SITE_NAME,TAR.SHARE_TYPE=SRC.SHARE_TYPE,TAR.STATE=SRC.STATE,TAR.OPERATION_TIME=SRC.OPERATION_TIME,TAR.CLUSTER_NUM=SRC.CLUSTER_NUM,TAR.BELONGING_OMC=SRC.BELONGING_OMC,TAR.IS_HIGH_SPEED=SRC.IS_HIGH_SPEED,TAR.IS_VIP=SRC.IS_VIP,TAR.COVER_AREA_TYPE=SRC.COVER_AREA_TYPE,TAR.ADDRESS=SRC.ADDRESS,TAR.REMARK=SRC.REMARK";
		String innerQuery = "select "
				+ enodebFields
				+ " from (select "
				+ enodebFields
				+ ",row_number () over (partition by business_enodeb_id order by business_enodeb_id desc) rn from rno_lte_import_mid mid where enodeb_status=3) where rn=1";
		sql = "merge into RNO_LTE_ENODEB TAR USING ("
				+ innerQuery
				+ ") SRC ON(TAR.BUSINESS_ENODEB_ID=SRC.BUSINESS_ENODEB_ID) WHEN MATCHED THEN UPDATE SET "
				+ enodebUpdateFields;
		log.debug("准备用临时表" + midTable + "的数据更新enodeb表(标记为删除状态的重新启用)RNO_LTE_ENODEB的信息：sql="
				+ sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("用临时表" + midTable
					+ "的数据更新enodeb表RNO_LTE_ENODEB(标记为删除状态的重新启用)的信息执行完成，耗时：" + (t2 - t1)
					+ "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用临时表" + midTable
					+ "的数据更新enodeb表RNO_LTE_ENODEB(标记为删除状态的重新启用)的信息时执行出错！sql=" + sql);
			return false;
		}

		// 将cell_status=2的小区信息填入到lte cell表
		String cellFields = "BUSINESS_CELL_ID,AREA_ID,CELL_NAME,LONGITUDE,LATITUDE,BAND_TYPE,COVER_TYPE,LOCAL_CELLID,SECTOR_ID,PCI,TAC,TAL,CELL_RADIUS,BAND,EARFCN,GROUND_HEIGHT,AZIMUTH,DOWNTILT,M_DOWNTILT,E_DOWNTILT,RRUNUM,RRUVER,ANTENNA_TYPE,INTEGRATED,PDCCH,PA,PB,COVER_RANGE,RSPOWER,ENODEB_ID";
		sql = "insert into RNO_LTE_CELL(LTE_CELL_ID," + cellFields
				+ ",STATUS) SELECT SEQ_LTE_CELL_ID.NEXTVAL," + cellFields
				+ ",'N' FROM " + midTable + " where cell_status=2";
		log.debug("准备将临时表" + midTable + "的新出现的lte 小区插入到系统表：RNO_LTE_CELL中，sql="
				+ sql);
		t1 = System.currentTimeMillis();
		try {
			affCnt=stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("将临时表" + midTable
					+ "的新出现的lte 小区插入到系统表：RNO_LTE_CELL中执行完成，影响："+affCnt+",耗时：" + (t2 - t1)
					+ "ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("将临时表" + midTable
					+ "的新出现的lte 小区插入到系统表：RNO_LTE_CELL中时执行出错！sql=" + sql);
			
			sql="select BUSINESS_CELL_ID,BUSINESS_ENODEB_ID,LOCAL_CELLID,CELL_ID from "+midTable;
			List<Map<String,Object>> res=RnoHelper.commonQuery(stmt, sql);
			log.debug("临时表的BUSINESS_CELL_ID情况：----");
			int i=0;
			String msg;
			for(Map<String,Object> one:res){
				msg="";
				for(String k:one.keySet()){
					msg+=k+"="+one.get(k)+",";
				}
				log.debug((i++)+"---:"+msg);
			}
			log.debug("临时表的BUSINESS_CELL_ID情况输出结束。");
			return false;
		}
		
		//处理cell_status=3
		sql="select count(*) cnt from "+midTable+" where CELL_STATUS=3";
		List<Map<String, Object>> cntRes = RnoHelper.commonQuery(stmt, sql);
		if (cntRes == null) {
			log.error("判断是否有系统为删除、临时表有数据小区时（cell_status=3）出错！sql=" + sql);
			return false;
		}
		BigDecimal cnt = (BigDecimal) (cntRes.get(0).get("CNT"));
		log.debug("系统为删除、临时表有数据小区时（cell_status=3）小区的个数为：" + cnt.longValue());
		if (cnt.longValue() > 0) {
			// --更新经纬度数据---//
			// 寻找经纬度有变化的
			String cntSql = "select count(cell.lte_cell_id) AS CNT from rno_lte_cell cell,"
					+ midTable
					+ " mid "
					+ " where mid.cell_status=3 and cell.lte_cell_id=mid.lte_cell_id and (cell.longitude<>mid.longitude or cell.latitude<>mid.latitude or cell.azimuth<>mid.azimuth)";
			log.debug("判断是否有经纬度变化的小区,sql=" + cntSql);
			cntRes = RnoHelper.commonQuery(stmt, cntSql);
			if (cntRes == null) {
				log.error("判断是否有经纬度变化的小区时出错！sql=" + cntSql);
				return false;
			}
			cnt = (BigDecimal) (cntRes.get(0).get("CNT"));
			log.debug("经纬度有变化的小区的个数为：" + cnt.longValue());
			if (cnt.longValue() > 0) {
				log.debug("有" + cnt.longValue() + " 个小区的经纬度产生了变化，需要更新地图形状表");
				sql = "select cell.lte_cell_id,cell.longitude,cell.latitude,cell.azimuth from rno_lte_cell cell,"
						+ midTable
						+ " mid "
						+ " where mid.cell_status=3 and cell.lte_cell_id=mid.lte_cell_id and (cell.longitude<>mid.longitude or cell.latitude<>mid.latitude)";

				// 更新经纬度形状表
				sql = "MERGE INTO RNO_LTE_CELL_MAP_SHAPE TAR USING ( "
						+ sql
						+ " ) SRC ON "
						+ " ( TAR.LTE_CELL_ID=SRC.LTE_CELL_ID AND TAR.MAP_TYPE='E' AND TAR.SHAPE_TYPE='T' ) WHEN MATCHED THEN UPDATE SET TAR.SHAPE_DATA=FUN_RNO_CREATE_TRINGLE_SHAPE(SRC.LONGITUDE,SRC.LATITUDE,SRC.AZIMUTH) ";

				log.debug("准备更新经纬度形状表数据，sql=" + sql);
				t1 = System.currentTimeMillis();
				try {
					stmt.executeUpdate(sql);
					t2 = System.currentTimeMillis();
					log.debug("更新经纬度形状表数据执行完成，耗时：" + (t2 - t1) + "ms");
				} catch (Exception e) {
					e.printStackTrace();
					log.error("更新经纬度形状表数据执行出错！sql=" + sql);
					return false;
				}
			}
			// --更新系统RNO_LTE_CELL表的信息
			cellFields = "BUSINESS_CELL_ID,AREA_ID,CELL_NAME,LONGITUDE,LATITUDE,BAND_TYPE,COVER_TYPE,LOCAL_CELLID,SECTOR_ID,PCI,TAC,TAL,CELL_RADIUS,BAND,EARFCN,GROUND_HEIGHT,AZIMUTH,DOWNTILT,M_DOWNTILT,E_DOWNTILT,RRUNUM,RRUVER,ANTENNA_TYPE,INTEGRATED,PDCCH,PA,PB,COVER_RANGE,RSPOWER,ENODEB_ID";
			String updateFields = "TAR.STATUS='N',TAR.AREA_ID=SRC.AREA_ID,TAR.CELL_NAME=SRC.CELL_NAME,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.BAND_TYPE=SRC.BAND_TYPE,TAR.COVER_TYPE=SRC.COVER_TYPE,TAR.LOCAL_CELLID=SRC.LOCAL_CELLID,TAR.SECTOR_ID=SRC.SECTOR_ID,TAR.PCI=SRC.PCI,TAR.TAC=SRC.TAC,TAR.TAL=SRC.TAL,TAR.CELL_RADIUS=SRC.CELL_RADIUS,TAR.BAND=SRC.BAND,TAR.EARFCN=SRC.EARFCN,TAR.GROUND_HEIGHT=SRC.GROUND_HEIGHT,TAR.AZIMUTH=SRC.AZIMUTH,TAR.DOWNTILT=SRC.DOWNTILT,TAR.M_DOWNTILT=SRC.M_DOWNTILT,TAR.E_DOWNTILT=SRC.E_DOWNTILT,TAR.RRUNUM=SRC.RRUNUM,TAR.RRUVER=SRC.RRUVER,TAR.ANTENNA_TYPE=SRC.ANTENNA_TYPE,TAR.INTEGRATED=SRC.INTEGRATED,TAR.PDCCH=SRC.PDCCH,TAR.PA=SRC.PA,TAR.PB=SRC.PB,TAR.COVER_RANGE=SRC.COVER_RANGE,TAR.RSPOWER=SRC.RSPOWER";
			sql = "merge into RNO_LTE_CELL TAR USING(SELECT "
					+ cellFields
					+ " FROM "
					+ midTable
					+ " WHERE CELL_STATUS=3) SRC ON (TAR.BUSINESS_CELL_ID=SRC.BUSINESS_CELL_ID) WHEN MATCHED THEN UPDATE SET "
					+ updateFields;
			log.debug("准备更新系统表：RNO_LTE_CELL的小区(标记为删除状态的小区重新启用)数据 ,sql=" + sql);
			t1 = System.currentTimeMillis();
			try {
				stmt.executeUpdate(sql);
				t2 = System.currentTimeMillis();
				log.debug("更新系统表：RNO_LTE_CELL的小区数据(标记为删除状态的小区重新启用) 执行完成，耗时：" + (t2 - t1) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("更新系统表：RNO_LTE_CELL的小区数据(标记为删除状态的小区重新启用)时执行出错！sql=" + sql);
				return false;
			}
		}
		
		
		// ----地图上展示的经纬度数据---//
		// 默认为三角形，ge地图数据//
		sql = "insert into RNO_LTE_CELL_MAP_SHAPE(CELL_MAP_SHAPE_ID,LTE_CELL_ID,MAP_TYPE,SHAPE_TYPE,SHAPE_DATA) select SEQ_CELL_MAP_SHAPE_ID.NEXTVAL,LTE_CELL_ID,'E','T',FUN_RNO_CREATE_TRINGLE_SHAPE(LONGITUDE,LATITUDE,AZIMUTH) FROM RNO_LTE_CELL WHERE BUSINESS_CELL_ID IN (SELECT BUSINESS_CELL_ID FROM "
				+ midTable + " WHERE CELL_STATUS=2)";
		log.debug("补充新增小区的经纬度形状数据，sql=" + sql);
		try {
			affCnt=stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("补充新增小区的经纬度形状数据执行完成，影响："+affCnt+",耗时：" + (t2 - t1) + "ms");
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("补充新增小区的经纬度形状数据时执行出错！sql=" + sql);
			return false;
		}
		long endTime = System.currentTimeMillis();
		log.debug("方法importAppend总共耗时：" + (endTime - begTime) + "ms");
		return true;
	}

	/**
	 * 覆盖模式导入
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-5-16 下午4:04:26
	 */
	private boolean importOverwrite(Statement stmt) {
		log.debug("进入方法：importOverwrite");
		long begTime = System.currentTimeMillis(), endTime;
		log.debug("调用importAppend方法");
		boolean ok = importAppend(stmt);
		if (!ok) {
			log.error("处理新增数据时出错！");
			return false;
		}

		long t1, t2;
		// --处理已经在系统中存在的小区数据
		String sql = "";
		// --更新经纬度数据---//
		// 寻找经纬度有变化的
		String cntSql = "select count(cell.lte_cell_id) AS CNT from rno_lte_cell cell,"
				+ midTable
				+ " mid "
				+ " where mid.cell_status=1 and cell.lte_cell_id=mid.lte_cell_id and (cell.longitude<>mid.longitude or cell.latitude<>mid.latitude)";
		log.debug("判断是否有经纬度变化的小区,sql=" + cntSql);
		List<Map<String, Object>> cntRes = RnoHelper.commonQuery(stmt, cntSql);
		if (cntRes == null) {
			log.error("判断是否有经纬度变化的小区时出错！sql=" + cntSql);
			return false;
		}
		BigDecimal cnt = (BigDecimal) (cntRes.get(0).get("CNT"));
		log.debug("经纬度有变化的小区的个数为：" + cnt.longValue());
		if (cnt.longValue() > 0) {
			log.debug("有" + cnt.longValue() + " 个小区的经纬度产生了变化，需要更新地图形状表");
			sql = "select cell.lte_cell_id,cell.longitude,cell.latitude,cell.AZIMUTH from rno_lte_cell cell,"
					+ midTable
					+ " mid "
					+ " where mid.cell_status=1 and cell.lte_cell_id=mid.lte_cell_id and (cell.longitude<>mid.longitude or cell.latitude<>mid.latitude)";
			//更改为
			sql = "select cell.lte_cell_id,mid.longitude,mid.latitude,cell.AZIMUTH from rno_lte_cell cell,"
					+ midTable
					+ " mid "
					+ " where mid.cell_status=1 and cell.lte_cell_id=mid.lte_cell_id and (cell.longitude<>mid.longitude or cell.latitude<>mid.latitude)";
			//打印
//			printTmpTable1(sql, stmt);
			// 更新经纬度形状表
			sql = "MERGE INTO RNO_LTE_CELL_MAP_SHAPE TAR USING ( "
					+ sql
					+ " ) SRC ON "
					+ " ( TAR.LTE_CELL_ID=SRC.LTE_CELL_ID AND TAR.MAP_TYPE='E' AND TAR.SHAPE_TYPE='T' ) WHEN MATCHED THEN UPDATE SET TAR.SHAPE_DATA=FUN_RNO_CREATE_TRINGLE_SHAPE(SRC.LONGITUDE,SRC.LATITUDE,SRC.AZIMUTH) ";

			log.debug("准备更新经纬度形状表数据，sql=" + sql);
			t1 = System.currentTimeMillis();
			try {
				stmt.executeUpdate(sql);
				t2 = System.currentTimeMillis();
				log.debug("更新经纬度形状表数据执行完成，耗时：" + (t2 - t1) + "ms");
			} catch (Exception e) {
				e.printStackTrace();
				log.error("更新经纬度形状表数据执行出错！sql=" + sql);
				return false;
			}
		}

		// --更新系统RNO_LTE_CELL表的信息
		String cellFields = "BUSINESS_CELL_ID,AREA_ID,CELL_NAME,LONGITUDE,LATITUDE,BAND_TYPE,COVER_TYPE,LOCAL_CELLID,SECTOR_ID,PCI,TAC,TAL,CELL_RADIUS,BAND,EARFCN,GROUND_HEIGHT,AZIMUTH,DOWNTILT,M_DOWNTILT,E_DOWNTILT,RRUNUM,RRUVER,ANTENNA_TYPE,INTEGRATED,PDCCH,PA,PB,COVER_RANGE,RSPOWER,ENODEB_ID";
		String updateFields = "TAR.STATUS='N',TAR.AREA_ID=SRC.AREA_ID,TAR.CELL_NAME=SRC.CELL_NAME,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.BAND_TYPE=SRC.BAND_TYPE,TAR.COVER_TYPE=SRC.COVER_TYPE,TAR.LOCAL_CELLID=SRC.LOCAL_CELLID,TAR.SECTOR_ID=SRC.SECTOR_ID,TAR.PCI=SRC.PCI,TAR.TAC=SRC.TAC,TAR.TAL=SRC.TAL,TAR.CELL_RADIUS=SRC.CELL_RADIUS,TAR.BAND=SRC.BAND,TAR.EARFCN=SRC.EARFCN,TAR.GROUND_HEIGHT=SRC.GROUND_HEIGHT,TAR.AZIMUTH=SRC.AZIMUTH,TAR.DOWNTILT=SRC.DOWNTILT,TAR.M_DOWNTILT=SRC.M_DOWNTILT,TAR.E_DOWNTILT=SRC.E_DOWNTILT,TAR.RRUNUM=SRC.RRUNUM,TAR.RRUVER=SRC.RRUVER,TAR.ANTENNA_TYPE=SRC.ANTENNA_TYPE,TAR.INTEGRATED=SRC.INTEGRATED,TAR.PDCCH=SRC.PDCCH,TAR.PA=SRC.PA,TAR.PB=SRC.PB,TAR.COVER_RANGE=SRC.COVER_RANGE,TAR.RSPOWER=SRC.RSPOWER";
		sql = "merge into RNO_LTE_CELL TAR USING(SELECT "
				+ cellFields
				+ " FROM "
				+ midTable
				+ " WHERE CELL_STATUS=1) SRC ON (TAR.BUSINESS_CELL_ID=SRC.BUSINESS_CELL_ID) WHEN MATCHED THEN UPDATE SET "
				+ updateFields;
		log.debug("准备更新系统表：RNO_LTE_CELL的小区数据 ,sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("更新系统表：RNO_LTE_CELL的小区数据 执行完成，耗时：" + (t2 - t1) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新系统表：RNO_LTE_CELL的小区数据 时执行出错！sql=" + sql);
			return false;
		}

		// ---更新enodeb的数据
		String enodebFields = "BUSINESS_ENODEB_ID,SUBNET_ID,MANAGENET_ID,ENODEB_NAME,AREA_ID,SITE_STYLE,LONGITUDE,LATITUDE,BUILD_TYPE,STATION_CFG,FRAME_CFG,SPECIAL_FRAME_CFG,MME,MCC,MNC,COVER_AREA,BUILD_PHASE,TDS_SITE_NAME,G2_SITE_NAME,SHARE_TYPE,STATE,OPERATION_TIME,CLUSTER_NUM,BELONGING_OMC,IS_HIGH_SPEED,IS_VIP,COVER_AREA_TYPE,ADDRESS,REMARK";
		String enodebUpdateFields = "TAR.SUBNET_ID=SRC.SUBNET_ID,TAR.MANAGENET_ID=SRC.MANAGENET_ID,TAR.ENODEB_NAME=SRC.ENODEB_NAME,TAR.AREA_ID=SRC.AREA_ID,TAR.SITE_STYLE=SRC.SITE_STYLE,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.BUILD_TYPE=SRC.BUILD_TYPE,TAR.STATION_CFG=SRC.STATION_CFG,TAR.FRAME_CFG=SRC.FRAME_CFG,TAR.SPECIAL_FRAME_CFG=SRC.SPECIAL_FRAME_CFG,TAR.MME=SRC.MME,TAR.MCC=SRC.MCC,TAR.MNC=SRC.MNC,TAR.COVER_AREA=SRC.COVER_AREA,TAR.BUILD_PHASE=SRC.BUILD_PHASE,TAR.TDS_SITE_NAME=SRC.TDS_SITE_NAME,TAR.G2_SITE_NAME=SRC.G2_SITE_NAME,TAR.SHARE_TYPE=SRC.SHARE_TYPE,TAR.STATE=SRC.STATE,TAR.OPERATION_TIME=SRC.OPERATION_TIME,TAR.CLUSTER_NUM=SRC.CLUSTER_NUM,TAR.BELONGING_OMC=SRC.BELONGING_OMC,TAR.IS_HIGH_SPEED=SRC.IS_HIGH_SPEED,TAR.IS_VIP=SRC.IS_VIP,TAR.COVER_AREA_TYPE=SRC.COVER_AREA_TYPE,TAR.ADDRESS=SRC.ADDRESS,TAR.REMARK=SRC.REMARK";
		String innerQuery = "select "
				+ enodebFields
				+ " from (select "
				+ enodebFields
				+ ",row_number () over (partition by business_enodeb_id order by business_enodeb_id desc) rn from rno_lte_import_mid mid where enodeb_status=2) where rn=1";
		sql = "merge into RNO_LTE_ENODEB TAR USING ("
				+ innerQuery
				+ ") SRC ON(TAR.BUSINESS_ENODEB_ID=SRC.BUSINESS_ENODEB_ID) WHEN MATCHED THEN UPDATE SET "
				+ enodebUpdateFields;
		log.debug("准备用临时表" + midTable + "的数据更新enodeb表RNO_LTE_ENODEB的信息：sql="
				+ sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("用临时表" + midTable
					+ "的数据更新enodeb表RNO_LTE_ENODEB的信息执行完成，耗时：" + (t2 - t1)
					+ "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("用临时表" + midTable
					+ "的数据更新enodeb表RNO_LTE_ENODEB的信息时执行出错！sql=" + sql);
			return false;
		}

		endTime = System.currentTimeMillis();
		log.debug("执行方法：importOverwrite耗时：" + (endTime - begTime) + "ms");
		return true;
	}

	/**
	 * 删除模式导入
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-5-16 下午4:04:59
	 */
	private boolean importDelete(Statement stmt) {
		log.debug("进入方法：importDelete");

		long begTime = System.currentTimeMillis(), endTime;
		long t1, t2;

		String sql = "";
		// --找到并删除在RNO_LTE_CELL表中存在，而在临时表中不存在的小区--//
		sql = "select cell.lte_cell_id from rno_lte_cell cell where cell.business_cell_id not in (select business_cell_id from "
				+ midTable + " mid where cell.area_id=mid.area_id )";
		// 更新状态
		sql = "update RNO_LTE_CELL SET STATUS='D' WHERE LTE_CELL_ID IN (" + sql
				+ ")";
		log.debug("删除在RNO_LTE_CELL表中存在而在临时表不存在的小区数据，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("删除在RNO_LTE_CELL表中存在而在临时表不存在的小区数据执行完成，耗时：" + (t2 - t1)
					+ "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除在RNO_LTE_CELL表中存在而在临时表不存在的小区数据执行出错！sql=" + sql);
			return false;
		}

		// --找到并删除在RNO_LTE_ENODEB表中存在，而在临时表中不存在的ENODEB--//
		sql = "select ENODEB.ENODEB_ID from rno_lte_ENODEB ENODEB where ENODEB.business_ENODEB_id not in (select DISTINCT(business_ENODEB_id) from "
				+ midTable + " mid where ENODEB.area_id=mid.area_id )";
		// 更新状态
		sql = "update RNO_LTE_ENODEB SET STATUS='D' WHERE ENODEB_ID IN (" + sql
				+ ")";
		log.debug("删除在RNO_LTE_ENODEB表中存在而在临时表不存在的ENODEB数据，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("删除在RNO_LTE_ENODEB表中存在而在临时表不存在的ENODEB数据执行完成，耗时："
					+ (t2 - t1) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除在RNO_LTE_ENODEB表中存在而在临时表不存在的ENODEB数据执行出错！sql=" + sql);
			return false;
		}

		// 调用覆盖模式处理
		log.debug("调用覆盖模式处理。");
		boolean ok = importOverwrite(stmt);
		endTime = System.currentTimeMillis();
		log.debug("方法importDelete执行完成，结果：" + ok + ",耗时：" + (endTime - begTime)
				+ "ms");
		return ok;
	}


	/**
	 * 
	 * @title 解析LTE小区数据
	 * @param token
	 * @param f
	 * @param connection
	 * @param stmt
	 * @param cityId
	 * @param dbFieldsToTitles
	 * @return
	 * @author chao.xj
	 * @date 2015-4-3下午2:45:26
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private boolean parseLteCell(String token,
			File f, Connection connection, Statement stmt,
			 Map<String, DBFieldToTitle> dbFieldsToTitles,StringBuilder bufMsg) {

		
		PreparedStatement insertTStmt = null;
		long st = System.currentTimeMillis();

		Date date1 = new Date();
		Date date2 = null;
		
		String tmpFileName=f.getName();
		// 读取文件
		BufferedReader reader = null;
		String charset = null;
		charset = FileTool.getFileEncode(f.getAbsolutePath());
		log.debug(tmpFileName + " 文件编码：" + charset);
		if (charset == null) {
			log.error("文件：" + tmpFileName + ":无法识别的文件编码！");
			date2 = new Date();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime, "文件：" + tmpFileName
					+ ":无法识别的文件编码！");
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
		//忽视掉哪些不具备一定长度的标题
		try {
			String[] sps = new String[1];
			do {
				line = reader.readLine();
				
				if (line == null) {
					sps = new String[] {};
					break;
				}
				sps = line.split(",|\t");
			} while (sps == null || sps.length < 10);
			//再读取下一行才是真正意义上的标题头
			line = reader.readLine();
			log.debug("真正的标题头信息:"+line);
			if (line != null) {
				sps = line.split(",|\t");
			}
			// 读取到标题头
			int fieldCnt = sps.length;
			log.debug("lte cell文件：" + tmpFileName + ",title 为：" + line + ",有"
					+ fieldCnt + "标题");

			// 判断标题的有效性
			Map<Integer, String> pois = new HashMap<Integer, String>();
			int index = -1;
			boolean find = false;
			for (String sp : sps) {
				index++;
				find = false;
				for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
					// log.debug("-----dt==" + dt);
					for (String dtf : dt.titles) {
						if (dt.matchType == 1) {
							if (StringUtils.equals(dtf, sp)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
								find = true;
								dt.setIndex(index);
								pois.put(index, dt.dbField);// 快速记录
								break;
							}
						} else if (dt.matchType == 0) {
							if (StringUtils.startsWith(sp, dtf)) {
								// log.debug("-----find " + sp + "->"
								// + dt);
								find = true;
								dt.setIndex(index);
								pois.put(index, dt.dbField);// 快速记录
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
			for (DBFieldToTitle dt : dbFieldsToTitles.values()) {
				if (dt.isMandatory && dt.index < 0) {
//					msg += "[" + dt.dbField + "]";
					msg +=  dt.getTitles() ;
				}
			}
			if (!StringUtils.isBlank(msg)) {
				log.error("检查lte cell文件：" + tmpFileName + "的标题头有问题！" + msg);
				date2 = new Date();
				bufMsg.append("在文件中缺失以下标题:" + msg+"</br>");
				setCachedInfo(token, "在文件中缺失以下标题:" + msg+"</br>");
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime, "在文件中缺失以下标题:" + msg);
				// connection.releaseSavepoint(savepoint);
				return false;
			}
			// 拼装sql
			insertInoMidTableSql = "insert into " + midTable + " (";
			String ws = "";
			index = 1;
			int poi = 1;
			 
			for (String d : dbFieldsToTitles.keySet()) {
				if (dbFieldsToTitles.get(d).index >= 0) {
					// 只对出现了的进行组sql
					dbFieldsToTitles.get(d).sqlIndex = index++;// 在数据库中的位置
					insertInoMidTableSql += d + ",";
					ws += "?,";
					poi++;
				}
			}
			areaSqlPosition.put("AREA_ID", poi++);
			insertInoMidTableSql += "AREA_ID,";
			insertInoMidTableSql += "ENODEB_STATUS,";
			insertInoMidTableSql += "CELL_STATUS";
			
			 ws+= "?,2,2";// Area_id的,以及默认的enodeb、cell存在状态为2
			if (StringUtils.isBlank(ws)) {
				log.error("没有有效标题数据！");
				return false;
			}
			insertInoMidTableSql += ") values ( " + ws + " )";
			log.debug("lte cell插入临时表的sql=" + insertInoMidTableSql);
			try {
				insertTStmt = connection
						.prepareStatement(insertInoMidTableSql);
			} catch (Exception e) {
				msg = "准备let cell插入的prepareStatement失败";
				log.error("准备lte cell入的prepareStatement失败！sql="
						+ insertInoMidTableSql);
				e.printStackTrace();
				// connection.releaseSavepoint(savepoint);
				return false;
			}

			// 逐行读取数据
			int executeCnt = 0;
			boolean handleLineOk = false;
			long totalDataNum = 0;
			DateUtil dateUtil = new DateUtil();
			do {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				sps = line.split(",|\t");
				/*if (sps.length != fieldCnt) {
					continue;
				}*/
				totalDataNum++;
			
				handleLineOk = handleLteCellLine(sps, fieldCnt, pois,
						dbFieldsToTitles, insertTStmt, dateUtil);

				if (handleLineOk == true) {
					executeCnt++;
					sucCnt++;
				}else {
					failCnt++;
				}
				if (executeCnt > 5000) {
					// 每5000行执行一次
					try {
						insertTStmt.executeBatch();
						insertTStmt.clearBatch();

						executeCnt = 0;
					} catch (SQLException e) {
						e.printStackTrace();
						// connection.rollback(savepoint);
						// 清除数据，关闭资源，返回失败
						try {
							stmt.executeUpdate("truncate table "+midTable);
						} catch (Exception e3) {
							e3.printStackTrace();
						}
						insertTStmt.close();
						connection.commit();
						return false;
					}
				}
			} while (!StringUtils.isBlank(line));
			// 执行
			if (executeCnt > 0) {
				insertTStmt.executeBatch();
				//当数量少的时候提交
			}

			log.debug("lte cell数据文件：" + tmpFileName + "共有：" + totalDataNum
					+ "行记录数据");
			// ----一下进行数据处理----//
			String attMsg = "文件：" + tmpFileName;
			long t1, t2;
			Date d1 = new Date();

			ResultInfo resultInfo = null;
			Date d2 = new Date();
	
			// 一个文件一个提交
//			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// try {
			// connection.rollback(savepoint);
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
			try {
				insertTStmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			/*try {
				stmt.executeUpdate("truncate table "+midTable);
			} catch (Exception e3) {
				e3.printStackTrace();
			}*/

			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (insertTStmt != null) {
				try {
					insertTStmt.close();
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
	 * @return
	 * @author chao.xj
	 * @date 2015-4-3下午2:45:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private boolean handleLteCellLine(String[] sps, int expectFieldCnt,
			Map<Integer, String> pois, Map<String, DBFieldToTitle> dbFtts,
			PreparedStatement insertTempStatement,  DateUtil dateUtil) {
		// log.debug("handleHwNcsLine--sps="+sps+",expectFieldCnt="+expectFieldCnt+",pois="+pois+",dbFtts="+dbFtts);
		long cityId=0;
		
		if (sps == null) {
			return false;
		}
		String dbField = "";
		DBFieldToTitle dt = null;
		String areaName = "";
		String eNodeBID="",localCellID="",businessCellId="";
		long areaId=0;
		for (int i = 0; i < expectFieldCnt; i++) {
			dbField = pois.get(i);// 该位置对应的数据库字段
			// log.debug(i+" -> dbField="+dbField);
			if (dbField == null) {
				continue;
			}

			dt = dbFtts.get(dbField);// 该数据库字段对应的配置信息
			if (dbField.equals("AREA_NAME")) {
				areaName = sps[i];
				if(areaNameToIds.get(areaName)!=null){
					areaId=areaNameToIds.get(areaName);
				}else{
					return false;
				}
				//添加系统中存在的区域集合
				excelAreaIdSet.add(areaId);
				try {
					insertTempStatement.setLong(areaSqlPosition.get("AREA_ID"), areaId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dbField.equals("BUSINESS_ENODEB_ID")) {
				//eNodeBID
				eNodeBID = sps[i];
			}
			if (dbField.equals("LOCAL_CELLID")) {
				//Local CellID
				localCellID = sps[i];
			}
			if (dt != null) {
				if(sps.length-1<i){
					setIntoPreStmt(insertTempStatement, dt, null, dateUtil);
				}else{
					setIntoPreStmt(insertTempStatement, dt, sps[i], dateUtil);
				}
			}
		}
		businessCellId=eNodeBID+localCellID;
		if(businessCellIds.get(businessCellId)!=null){
			return false;
		}else{
			businessCellIds.put(businessCellId, businessCellId);
		}
		try {
			insertTempStatement.addBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	private void setIntoPreStmt(PreparedStatement pstmt, DBFieldToTitle dt,
			String val, DateUtil dateUtil) {
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
					Date date = dateUtil.parseDateArbitrary(val);
					if (date != null) {
						pstmt.setTimestamp(index,
								new java.sql.Timestamp(date.getTime()));
					} else {
						pstmt.setNull(index, java.sql.Types.DATE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					log.error("hwncs parse date fail:date str=" + val);
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
		}else if (StringUtils.startsWithIgnoreCase("Double", type)) {
			if (!StringUtils.isBlank(val) && isNumeric(val)) {
				try {
					pstmt.setDouble(index, Double.parseDouble(val));
				} catch (NumberFormatException e) {
				} catch (SQLException e) {
				}
			} else {
				try {
					pstmt.setNull(index, Types.DOUBLE);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * @title 从xml配置 文件中读取lte cell 数据库到excel的映射关系
	 * @return
	 * @author chao.xj
	 * @date 2015-4-2上午11:26:44
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, DBFieldToTitle> readDbToTitleCfgFromXml() {
		Map<String, DBFieldToTitle> dbCfgs = new TreeMap<String, DBFieldToTitle>();
		try {
			InputStream in = new FileInputStream(new File(
					RnoLteCellParser.class.getResource(
							"lteCellDbToTitles.xml").getPath()));
			SAXReader reader = new SAXReader();
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			for (Object o : root.elements()) {
				Element e = (Element) o;
				DBFieldToTitle dt = new DBFieldToTitle();
				for (Object obj : e.elements()) {
					Element e1 = (Element) obj;
					String key = e1.getName();
					String val = e1.getTextTrim();
					if ("name".equals(key)) {
						dt.setDbField(val);
					}
					if ("type".equals(key)) {
						dt.setDbType(val);
					}
					if ("essential".equals(key)) {
						if (StringUtils.equals(val, "1")) {
							// mandaroty
							dt.setMandatory(true);
						} else {
							dt.setMandatory(false);
						}
					}
					if ("match".equals(key)) {
						dt.setMatchType(Integer.parseInt(val));
					}
					if ("exceltitle".equals(key)) {
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
		return dbCfgs;
	}
	public static class DBFieldToTitle {
		String dbField;
		int matchType;// 0：模糊匹配，1：精确匹配
		int index = -1;// 在文件中出现的位置，从0开始
		boolean isMandatory = true;// 是否强制要求出现
		private String dbType;// 类型，Number，String,Date
		List<String> titles = new ArrayList<String>();

		int sqlIndex = -1;// 在sql语句中的位置

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
			return "DBFieldToTitle [dbField=" + dbField + ", matchType="
					+ matchType + ", index=" + index + ", isMandatory="
					+ isMandatory + ", dbType=" + dbType + ", titles=" + titles
					+ "]";
		}

	}
	public  boolean isNumeric(String num) {
		   try {   
		    Double.parseDouble(num);
		    return true;
		   } catch (NumberFormatException e) {
		    return false;
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
}
