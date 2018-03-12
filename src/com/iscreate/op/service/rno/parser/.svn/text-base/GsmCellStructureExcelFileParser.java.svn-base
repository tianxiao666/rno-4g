package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.iscreate.op.pojo.rno.RnoCellStruct;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class GsmCellStructureExcelFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory
			.getLog(GsmCellStructureExcelFileParser.class);

	// ---spring 注入----//
	private SysAreaDao sysAreaDao;

	private static SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 导入模板规定的标题头
	private static List<String> expectTitles = Arrays.asList("地区", "时间", "小区",
			"小区层", "叠加簇权值", "干扰系数", "网络结构指数", "冗余覆盖指数", "重叠覆盖度", "干扰源系数",
			"过覆盖系数", "小区检测次数", "理想覆盖距离", "综合话务量", "话音话务量", "数据等效话务量",
			"下行质差话务比例(%)", "上行质差话务比例(%)", "下行质量(%)", "上行质量(%)", "上行干扰系数",
			"上行底噪(%)", "弱覆盖比例(%)", "平均Ta");

	private static int titleSize = expectTitles.size();

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入GsmCellStructureExcelFileParser方法：parseDataInternal。 token="
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

		// 1、插入描述符
		// 2、准备批处理
		// 3、执行批处理
		// 4、处理加载

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
			log.info("准备导入小区结构数据到：" + country.getName());
		}
		fileParserManager.updateTokenProgress(token, 0.02f);
		// 获取全部数据
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);

		fileParserManager.updateTokenProgress(token, 0.30f);
		if (allDatas == null || allDatas.size() < 2) {
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
			log.error("上传的小区结构 excel文件的格式不符合格式要求！");
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！确保文件的格式为：" + expectTitles);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		fileParserManager.updateTokenProgress(token, 0.35f);
		int validSize = allDatas.size() - 1;// excel有效记录数

		// 1、描述符的插入
		List<String> oneData = allDatas.get(1);
		String timeStr = "";
		try {
			timeStr = oneData.get(1);
		} catch (Exception e) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"第2行数据有问题，终止导入！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		}
		java.util.Date time = null;
		try {
			time = sdf1.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"第2行数据时间有问题，终止导入！");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return false;
		}

		Connection connection = DataSourceConn.initInstance().getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
			fail(connection, token, "访问出错！code=601");
			return false;
		}

		int errorCnt = 0;
		int successCnt = 0;

		// 获取sequence
		long descId = -1;
		String vsql = "select SEQ_RNO_CELL_STRUCT_DESC.NEXTVAL as id from dual";
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(vsql);
		} catch (SQLException e3) {
			e3.printStackTrace();
			fail(connection, token, "数据处理出错！code=602");
			return false;
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
			rs.next();
			descId = rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=603");
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

		java.util.Date now = new java.util.Date();
		String name=attachParams.get("name")+"";
		name=name.trim();
		if("".equals(name)){
			name=country.getName()+ " "
				+ timeStr
				+ "小区结构数据";
		}
		String createTime = sdf1.format(now);
		// 插入描述表的语句
		String insertDescriptor = "insert into RNO_CELL_STRUCT_DESC (RNO_CELL_STRUCT_DESC_ID,NAME,TIME,AREA_ID,CREATE_TIME,MOD_TIME,STATUS) values ("
				+ descId
				+ ",'"
				+ name
				+ "',TO_DATE('"
				+ timeStr
				+ "','YYYY-MM-DD HH24:mi:ss'),"
				+ areaId
				+ ",TO_DATE('"
				+ createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),TO_DATE('"
				+ createTime
				+ "','yyyy-mm-dd HH24:mi:ss'),'N')";

		log.info("------------准备插入小区结构描述信息：");
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(insertDescriptor);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=604");
			return false;
		}

		// 执行
		try {
			boolean ok = statement.execute();
			log.info("插入小区结构描述信息的结果：" + ok);
		} catch (SQLException e2) {
			e2.printStackTrace();
			fail(connection, token, "数据处理出错！code=605");
			return false;
		}
		//
		log.info("-----------完成插入小区结构描述信息，得到descId=" + descId);
		fileParserManager.updateTokenProgress(token, 0.4f);
		
		// 插入详细表的语句
		String fields = "RNO_CELL_STRUCT_ID,RNO_CELL_STRUCT_DESC_ID,CELL,CELL_LEVEL,OVERLAP_CLUSTER_WEIGHT,INTERFERENCE_COEFFICIENT,NET_STRUCT_INDEX,REDUNDANCE_COVER_INDEX,OVERLAP_COVER,INTER_SOURCE_COEFFICIENT,OVERSHOOTING_COEFFICIENT,CELL_DETECT_CNT,EXPECTED_COVER_DISTANCE,COMPREHENSIVE_TRAFFIC,AUDIO_TRAFFIC,DATA_TRAFFIC,DWN_BAD_TRAFFIC_RATIO,UP_BAD_TRAFF_RATIO,DWN_QUALITY,UP_QUALITY,UP_INTERFERENCE_COEFFICIENT,UP_BASE_NOISE,COVER_LIMITED_RATIO,TA_MEAN";
		String insertIntoDetailTable = "insert into RNO_CELL_STRUCT ( "
				+ fields
				+ " ) values (SEQ_RNO_CELL_STRUCT.NEXTVAL,"
				+ descId
				+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement insertSqlpstmt = null;
		try {
			insertSqlpstmt = connection.prepareStatement(insertIntoDetailTable);
		} catch (SQLException e) {
			e.printStackTrace();
			fail(connection, token, "数据处理出错！code=606");
			return false;
		}

		StringBuilder buf = new StringBuilder();
		RnoCellStruct cellStruct = null;
		int index = 1;
		log.info("----------开始处理excel行：");
		t1 = System.currentTimeMillis();
		for (int i = 1; i < allDatas.size(); i++) {
			fileParserManager.updateTokenProgress(token,
					0.4f + (i * 1.0f / validSize) * 0.5f);
			cellStruct = createRnoCellStructFromExcelLine(country.getName(),
					allDatas.get(i), i, buf);
			if (cellStruct == null) {
				errorCnt++;
				continue;
			}
			successCnt++;
			index = 1;
			
			try{
				//CELL,CELL_LEVEL,OVERLAP_CLUSTER_WEIGHT,INTERFERENCE_COEFFICIENT,NET_STRUCT_INDEX,
				insertSqlpstmt.setString(index++, cellStruct.getCell());
				insertSqlpstmt.setLong(index++, cellStruct.getCellLevel());
				insertSqlpstmt.setDouble(index++, cellStruct.getOverlapClusterWeight());
				insertSqlpstmt.setDouble(index++, cellStruct.getInterferenceCoefficient());
				insertSqlpstmt.setDouble(index++, cellStruct.getNetStructIndex());
				//REDUNDANCE_COVER_INDEX,OVERLAP_COVER,INTER_SOURCE_COEFFICIENT,OVERSHOOTING_COEFFICIENT,CELL_DETECT_CNT,
				insertSqlpstmt.setDouble(index++, cellStruct.getRedundanceCoverIndex());
				insertSqlpstmt.setDouble(index++, cellStruct.getOverlapCover());
				insertSqlpstmt.setDouble(index++, cellStruct.getInterSourceCoefficient());
				insertSqlpstmt.setDouble(index++, cellStruct.getOvershootingCoefficient());
				insertSqlpstmt.setLong(index++, cellStruct.getCellDetectCnt());
				//EXPECTED_COVER_DISTANCE,COMPREHENSIVE_TRAFFIC,AUDIO_TRAFFIC,DATA_TRAFFIC,DWN_BAD_TRAFFIC_RATIO,
				insertSqlpstmt.setDouble(index++, cellStruct.getExpectedCoverDistance());
				insertSqlpstmt.setDouble(index++, cellStruct.getComprehensiveTraffic());
				insertSqlpstmt.setDouble(index++, cellStruct.getAudioTraffic());
				insertSqlpstmt.setDouble(index++, cellStruct.getDataTraffic());
				insertSqlpstmt.setDouble(index++, cellStruct.getDwnBadTrafficRatio());
				//UP_BAD_TRAFF_RATIO,DWN_QUALITY,UP_QUALITY,UP_INTERFERENCE_COEFFICIENT,UP_BASE_NOISE,
				insertSqlpstmt.setDouble(index++, cellStruct.getUpBadTraffRatio());
				insertSqlpstmt.setDouble(index++, cellStruct.getDwnQuality());
				insertSqlpstmt.setDouble(index++, cellStruct.getUpQuality());
				insertSqlpstmt.setDouble(index++, cellStruct.getUpInterferenceCoefficient());
				insertSqlpstmt.setDouble(index++, cellStruct.getUpBaseNoise());
				//COVER_LIMITED_RATIO,TA_MEAN
				insertSqlpstmt.setDouble(index++, cellStruct.getCoverLimitedRatio());
				insertSqlpstmt.setDouble(index++, cellStruct.getTaMean());
				
				insertSqlpstmt.addBatch();
			}catch(Exception e){
				successCnt--;
				errorCnt++;
				e.printStackTrace();
			}
		}
		
		//准备导入
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
			fail(connection, token, "数据处理出错！code=607");
			return false;
		}
		fileParserManager.updateTokenProgress(token, 0.95f);
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

		fileParserManager.updateTokenProgress(token, 0.95f);
		
		List<PlanConfig> planConfigs = null;
		if(autoload && successCnt>0){
			log.info("需要加载到分析列表。准备加载到分析列表。");
			PlanConfig pc = new PlanConfig();
			pc.setAreaName(country.getName());
			pc.setConfigId(descId);
			pc.setSelected(false);
			pc.setTemp(false);
			pc.setType(RnoConstant.DataType.CELLSTRUCT_DATA);
			pc.setName(name);
			pc.setTitle(name);
			
			pc.setCollectTime(timeStr);

			planConfigs = addToAnalysisList(attachParams, pc);
		}
		
		String result="{'msg':'导入共处理："+validSize+"行数据。成功："+successCnt+"条，失败："+errorCnt+"条。耗时："+(t2-begTime)/1000+"s。";
		if(buf.length()>0){
			result+="详情如下：<br/>"+buf.toString()+"'";
		}else{
			result+="'";
		}
		if (autoload && successCnt>0) {
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
						.getAttribute(RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);
				log.info("当前session里的小区结构分析列表：" + planConfigs);
				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					session.setAttribute(
							RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID,
							planConfigs);
				}
				if (!planConfigs.contains(pc)) {
					planConfigs.add(pc);
				}
				session.setAttribute(
						RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID,
						planConfigs);
			}
		}
		return planConfigs;
	}
	private RnoCellStruct createRnoCellStructFromExcelLine(String country,
			List<String> oneData, int i, StringBuilder buf) {

		String msg = "";
		if (oneData == null || oneData.size() < titleSize) {
			msg = "第【" + (i + 1) + "】行的数据有缺失！该行只有["
					+ (oneData == null ? 0 : oneData.size()) + "]列数据，应该有：["
					+ titleSize + "]列数据！<br/>";
			buf.append(msg);
			log.error(msg);
			return null;
		}

		String areaName;
		Date time;
		String timeStr;
		String cell;
		long cellLevel;
		double overlapClusterWeight;
		double interferenceCoefficient;
		double netStructIndex;
		double redundanceCoverIndex;
		double overlapCover;
		double interSourceCoefficient;
		double overshootingCoefficient;
		long cellDetectCnt;
		double expectedCoverDistance;
		double comprehensiveTraffic;
		double audioTraffic;
		double dataTraffic;
		double dwnBadTrafficRatio;
		double upBadTraffRatio;
		double dwnQuality;
		double upQuality;
		double upInterferenceCoefficient;
		double upBaseNoise;
		double coverLimitedRatio;
		double taMean;

		int index = 0;
		try {
			// 地区
			areaName = oneData.get(index++);
			if (!country.equals(areaName)) {
				msg = "第【" + (i + 1) + "】数据的地区有误，应该为：[" + country + "]<br/>";
				log.error(msg);
				buf.append(msg);
				return null;
			}

			// 时间
			timeStr = oneData.get(index++);

			// 小区
			cell = oneData.get(index++);
			if (cell == null || "".equals(cell.trim())) {
				msg = "第【" + (i + 1) + "】数据的小区为空！<br/>";
				log.error(msg);
				buf.append(msg);
				return null;
			}

			// 小区层
			try {
				cellLevel = Long.parseLong(oneData.get(index++));
			} catch (Exception e) {
				cellLevel = 0;
			}

			// 叠加簇权值
			try {
				overlapClusterWeight = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				overlapClusterWeight = 0;
			}

			// 干扰系数
			try {
				interferenceCoefficient = Double.parseDouble(oneData
						.get(index++));
			} catch (Exception e) {
				interferenceCoefficient = 0;
			}

			// 网络结构指数
			try {
				netStructIndex = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				netStructIndex = 0;
			}

			// 冗余覆盖指数
			try {
				redundanceCoverIndex = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				redundanceCoverIndex = 0;
			}

			// 重叠覆盖度
			try {
				overlapCover = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				overlapCover = 0;
			}

			// 干扰源系数
			try {
				interSourceCoefficient = Double.parseDouble(oneData
						.get(index++));
			} catch (Exception e) {
				interSourceCoefficient = 0;
			}

			// 过覆盖系数
			try {
				overshootingCoefficient = Double.parseDouble(oneData
						.get(index++));
			} catch (Exception e) {
				overshootingCoefficient = 0;
			}

			// 小区建成次数
			try {
				cellDetectCnt = Long.parseLong(oneData.get(index++));
			} catch (Exception e) {
				cellDetectCnt = 0;
			}

			// 离线覆盖距离
			try {
				expectedCoverDistance = Double
						.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				expectedCoverDistance = 0;
			}

			// 综合话务量
			try {
				comprehensiveTraffic = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				comprehensiveTraffic = 0;
			}

			// 话音话务量
			try {
				audioTraffic = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				audioTraffic = 0;
			}

			// 数据等效话务量
			try {
				dataTraffic = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				dataTraffic = 0;
			}

			// 下行质差话务比例
			try {
				dwnBadTrafficRatio = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				dwnBadTrafficRatio = 0;
			}

			// 上行质差话务比例
			try {
				upBadTraffRatio = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				upBadTraffRatio = 0;
			}

			// 下行质量
			try {
				dwnQuality = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				dwnQuality = 0;
			}

			// 上行质量
			try {
				upQuality = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				upQuality = 0;
			}

			// 上行干扰系数
			try {
				upInterferenceCoefficient = Double.parseDouble(oneData
						.get(index++));
			} catch (Exception e) {
				upInterferenceCoefficient = 0;
			}

			// 上行底噪
			try {
				upBaseNoise = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				upBaseNoise = 0;
			}

			// 弱覆盖比例
			try {
				coverLimitedRatio = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				coverLimitedRatio = 0;
			}

			// 评价ta
			try {
				taMean = Double.parseDouble(oneData.get(index++));
			} catch (Exception e) {
				taMean = 0;
			}

			RnoCellStruct struct = new RnoCellStruct();
			struct.setAudioTraffic(audioTraffic);
			struct.setCell(cell);
			struct.setCellDetectCnt(cellDetectCnt);
			struct.setCellLevel(cellLevel);
			struct.setComprehensiveTraffic(comprehensiveTraffic);
			struct.setCoverLimitedRatio(coverLimitedRatio);
			struct.setDataTraffic(dataTraffic);
			struct.setDwnBadTrafficRatio(dwnBadTrafficRatio);
			struct.setDwnQuality(dwnQuality);
			struct.setExpectedCoverDistance(expectedCoverDistance);
			struct.setInterferenceCoefficient(interferenceCoefficient);
			struct.setInterSourceCoefficient(interSourceCoefficient);
			struct.setNetStructIndex(netStructIndex);
			struct.setOverlapClusterWeight(overlapClusterWeight);
			struct.setOverlapCover(overlapCover);
			struct.setOvershootingCoefficient(overshootingCoefficient);
			struct.setRedundanceCoverIndex(redundanceCoverIndex);
			struct.setTaMean(taMean);
			struct.setUpBadTraffRatio(upBadTraffRatio);
			struct.setUpBaseNoise(upBaseNoise);
			struct.setUpInterferenceCoefficient(upInterferenceCoefficient);
			struct.setUpQuality(upQuality);

			return struct;

		} catch (Exception e) {
			return null;
		}
	}
}
