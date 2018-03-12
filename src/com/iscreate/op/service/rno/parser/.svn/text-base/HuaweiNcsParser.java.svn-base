package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class HuaweiNcsParser extends ExcelFileParserBase {
	private static Log log = LogFactory.getLog(HuaweiNcsParser.class);
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;
	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao) {
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}
	private static List<String> expectTitles = Arrays.asList("起始时间", "周期",
			"网元名称", "GCELL_NCELL", "BCCH", "BCC,NCC", "NCC",
			"AS360:邻近小区平均信号强度 (毫瓦分贝)", "S389:竞争小区信号处于电平等级3的次数 (无)",
			"S368:服务小区与邻区信号强度差大于邻区干扰电平门限5的测量报告数 (无)",
			"S386:竞争小区信号处于电平等级0的次数 (无)",
			"S363:服务小区与邻区信号强度差小于邻区干扰电平门限1的测量报告数 (无)",
			"S371:邻区与服务小区信号强度差大于相对电平门限的测量报告数 (无)",
			"AS362:服务小区与邻区信号强度差平均值 (毫瓦分贝)", "S387:竞争小区信号处于电平等级1的次数 (无)",
			"S372:邻区信号强度大于绝对电平门限的测量报告数 (无)", "S360:邻近小区信号强度 (毫瓦分贝)",
			"S394:服务小区与邻区信号强度差小于邻区干扰电平门限0的测量报告数 (无)",
			"S364:服务小区与邻区信号强度差大于邻区干扰电平门限1的测量报告数 (无)",
			"S369:服务小区与邻区信号强度差大于邻区干扰电平门限6的测量报告数 (无)",
			"S388:竞争小区信号处于电平等级2的次数 (无)", "S390:竞争小区信号处于电平等级4的次数 (无)",
			"S366:服务小区与邻区信号强度差大于邻区干扰电平门限3的测量报告数 (无)", "S362:服务小区信号强度 (毫瓦分贝)",
			"S361:邻近小区测量报告数目 (无)", "S393:竞争小区信号处于电平等级7的次数 (无)",
			"S365:服务小区与邻区信号强度差大于邻区干扰电平门限2的测量报告数 (无)",
			"S392:竞争小区信号处于电平等级6的次数 (无)", "S391:竞争小区信号处于电平等级5的次数 (无)",
			"S367:服务小区与邻区信号强度差大于邻区干扰电平门限4的测量报告数 (无)",
			"S370:服务小区与邻区信号强度差大于邻区干扰电平门限7的测量报告数 (无)");
	private static Map<String, String> dbFieldsToExcelTitles = new HashMap<String, String>() {
		{
			put("MEA_TIME", "起始时间");
		};
		{
			put("PERIOD", "周期");
		};
		{
			put("BSC_NAME", "网元名称");
		};
		{
			put("CELL", "GCELL_NCELL");
		};
		{
			put("ARFCN", "BCCH");
		};
		{
			put("BCC", "BCC,NCC");
		};
		{
			put("NCC", "NCC");
		};
		{
			put("AS360", "AS360:邻近小区平均信号强度 (毫瓦分贝)");
		};
		{
			put("S389", "S389:竞争小区信号处于电平等级3的次数 (无)");
		};
		{
			put("S368", "S368:服务小区与邻区信号强度差大于邻区干扰电平门限5的测量报告数 (无)");
		};
		{
			put("S386", "S386:竞争小区信号处于电平等级0的次数 (无)");
		};
		{
			put("S363", "S363:服务小区与邻区信号强度差小于邻区干扰电平门限1的测量报告数 (无)");
		};
		{
			put("S371", "S371:邻区与服务小区信号强度差大于相对电平门限的测量报告数 (无)");
		};
		{
			put("AS362", "AS362:服务小区与邻区信号强度差平均值 (毫瓦分贝)");
		};
		{
			put("S387", "S387:竞争小区信号处于电平等级1的次数 (无)");
		};
		{
			put("S372", "S372:邻区信号强度大于绝对电平门限的测量报告数 (无)");
		};
		{
			put("S360", "S360:邻近小区信号强度 (毫瓦分贝)");
		};
		{
			put("S394", "S394:服务小区与邻区信号强度差小于邻区干扰电平门限0的测量报告数 (无)");
		};
		{
			put("S364", "S364:服务小区与邻区信号强度差大于邻区干扰电平门限1的测量报告数 (无)");
		};
		{
			put("S369", "S369:服务小区与邻区信号强度差大于邻区干扰电平门限6的测量报告数 (无)");
		};
		{
			put("S388", "S388:竞争小区信号处于电平等级2的次数 (无)");
		};
		{
			put("S390", "S390:竞争小区信号处于电平等级4的次数 (无)");
		};
		{
			put("S366", "S366:服务小区与邻区信号强度差大于邻区干扰电平门限3的测量报告数 (无)");
		};
		{
			put("S362", "S362:服务小区信号强度 (毫瓦分贝)");
		};
		{
			put("S361", "S361:邻近小区测量报告数目 (无)");
		};
		{
			put("S393", "S393:竞争小区信号处于电平等级7的次数 (无)");
		};
		{
			put("S365", "S365:服务小区与邻区信号强度差大于邻区干扰电平门限2的测量报告数 (无)");
		};
		{
			put("S392", "S392:竞争小区信号处于电平等级6的次数 (无)");
		};
		{
			put("S391", "S391:竞争小区信号处于电平等级5的次数 (无)");
		};
		{
			put("S367", "S367:服务小区与邻区信号强度差大于邻区干扰电平门限4的测量报告数 (无)");
		};
		{
			put("S370", "S370:服务小区与邻区信号强度差大于邻区干扰电平门限7的测量报告数 (无)");
		};
	};

	private static Map<String, String> excelTitlesToDbFields = new HashMap<String, String>();

	Map<String, String> dbFieldsType = new HashMap<String, String>() {
		{
			put("MEA_TIME", "Date");
		};
		{
			put("PERIOD", "Long");
		};
		{
			put("BSC_NAME", "String");
		};
		{
			put("CELL", "String");
		};
		{
			put("ARFCN", "Long");
		};
		{
			put("BCC", "Long");
		};
		{
			put("NCC", "Long");
		};
		{
			put("BSIC","String");//
		};
		{
			put("AS360", "Float");
		};
		{
			put("S389", "Long");
		};
		{
			put("S368", "Long");
		};
		{
			put("S386", "Long");
		};
		{
			put("S363", "Long");
		};
		{
			put("S371", "Long");
		};
		{
			put("AS362", "Float");
		};
		{
			put("S387", "Long");
		};
		{
			put("S372", "Long");
		};
		{
			put("S360", "Float");
		};
		{
			put("S394", "Long");
		};
		{
			put("S364", "Long");
		};
		{
			put("S369", "Long");
		};
		{
			put("S388", "Long");
		};
		{
			put("S390", "Long");
		};
		{
			put("S366", "Long");
		};
		{
			put("S362", "Float");
		};
		{
			put("S361", "Long");
		};
		{
			put("S393", "Long");
		};
		{
			put("S365", "Long");
		};
		{
			put("S392", "Long");
		};
		{
			put("S391", "Long");
		};
		{
			put("S367", "Long");
		};
		{
			put("S370", "Long");
		};
	};
	// 构建insert语句
	private static String midTable = "RNO_2G_HW_NCS_T";
	private static String insertInoMidTableSql = "";
	// 在insert语句中出现的字段名称
	private static List<String> seqenceDbFields = new ArrayList<String>();
	// 每个db字段在sql出现的位置，从1开始
	private static Map<String, Integer> seqenceDbFieldsPosition = new HashMap<String, Integer>();

	private static Set<String> dbFields = dbFieldsToExcelTitles.keySet();
	static {
		insertInoMidTableSql = "insert into " + midTable + " (RNO_NCS_DESC_ID,";
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
		insertInoMidTableSql += "CITY_ID,BSIC";
		seqenceDbFieldsPosition.put("CITY_ID", poi++);
		seqenceDbFieldsPosition.put("BSIC", poi++);

		insertInoMidTableSql += " ) values(100,";
		for (int i = 0; i < dbFields.size(); i++) {
			insertInoMidTableSql += "?,";
		}
		insertInoMidTableSql += "?,?";//CITY_ID,BSIC
		insertInoMidTableSql += ")";
	}

	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("起始时间",
			"周期", "网元名称", "GCELL_NCELL", "BCCH", "BCC,NCC", "NCC");
	private static int titleSize = expectTitles.size();

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		log.debug("进入parseDataInternal方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);

		super.setCachedInfo(token, "正在分析格式有效性...");

		long begTime = new Date().getTime();
		long t1, t2;
		String msg = "";
		if (attachParams == null || attachParams.isEmpty()) {
			msg = "缺少bsc信息、区域信息、频段信息！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
		}

		String city = attachParams.get("cityId") + "";
		long cityId = -1;
		try {
			cityId = Long.parseLong(city);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "城市信息有误！";
			log.error(msg);
			super.setCachedInfo(token, msg);
			return false;
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

		// 获取标题情况
		Map<String, Integer> excelTitlesColumn = new HashMap<String, Integer>();
		StringBuilder checkMsg = new StringBuilder();
		int colCount = calculateExcelTitleColumn(allDatas.get(0),
				dbFieldsToExcelTitles, excelTitlesColumn, checkMsg);
		log.info("计算标题所在列情况，总共有标题列：" + colCount + ",msg=" + checkMsg.toString());
		if (colCount <= 0) {
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					checkMsg.toString());
			return false;
		}
		// 检查必须的字段是否存在
		checkMsg.setLength(0);

		boolean howTitle = baseCheckIfExcelTitleValide(excelTitlesColumn,
				mandatoryExcelTitles, checkMsg);
		log.info("检查标题情况:" + howTitle + ",msg=" + checkMsg.toString());
		if (!howTitle) {
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					checkMsg.toString());
			return false;
		}

		// 获取全集中，未出现在excel表title中的title（这些是允许不出现的）
		log.debug("excelTitlesColumn=" + excelTitlesColumn.keySet());
		List<String> notExistExcelTitles = getNotListExcelTitles(
				excelTitlesColumn, excelTitlesToDbFields);
		log.debug("未出现的标题为（允许不出现）：" + notExistExcelTitles);

		// ---------以上完成了对标题所在位置的判断，与部分必须存在的标题的检验
		int total = allDatas.size() - 1;// excel有效记录数
		if (total <= 0) {
			log.error("excel表未包含有效数据");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"excel表未包含有效数据");
			return false;
		}

		// 准备数据库插入statement
		PreparedStatement PStatement = null;
		log.debug("插入中间表的sql语句为：" + insertInoMidTableSql);
		try {
			PStatement = connection.prepareStatement(insertInoMidTableSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！insertInoMidTableSql="
					+ insertInoMidTableSql);
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}

		super.setCachedInfo(token, "正在逐行分析数据...");
		// --------逐行分析excel数据，转换为导入中间表的sql---//
		int dataSize = allDatas.size();
		StringBuilder bufMsg = new StringBuilder();
		int failCnt = 0, sucCnt = 0;
		int index;
		boolean excelLineOk;
		List<String> oneRow = null;
		String val="";
		String[] cellEles=null;
		for (int j = 1; j < dataSize; j++) {
			excelLineOk = true;
			super.setCachedInfo(token, "正在逐行分析数据...当前处理：" + (j) + "/"
					+ (dataSize - 1));
			oneRow = allDatas.get(j);
			if (oneRow == null || oneRow.size() < colCount) {
				continue;
			}

			// 校验数据有效性
			if (!excelLineOk) {
				log.debug("第[" + (j + 1) + "]行数据无效。");
				bufMsg.append("<br/>");
				failCnt++;
				continue;
			}

			// 转换为sql
			// 对于excel里出现的都进行设置，对于没有在excel出现的，则设置为null
			for (String t : excelTitlesColumn.keySet()) {
				val=oneRow.get(excelTitlesColumn.get(t));
				//将服务小区的名称进行分解
				if("GCELL_NCELL".equals(t)){
					if(StringUtils.isBlank(val)){
						failCnt++;
						msg = "第[" + (j + 1) + "]行的列：[" + t + "]不能为空！";
						log.warn(msg);
						bufMsg.append(msg + "<br/>");
						break;
					}
					//按格式分解 ：小区名称=B52技工学校街道A, 小区索引=221, CGI=46000274721CD
					cellEles=val.split(",|=");
					if(cellEles==null || cellEles.length!=6){
						failCnt++;
						msg = "第[" + (j + 1) + "]行的列：[" + t + "]格式不对！";
						log.warn(msg);
						bufMsg.append(msg + "<br/>");
						break;
					}
					val=cellEles[1];
				}
			
				excelLineOk = setPreparedStatementValue(PStatement,
						excelTitlesToDbFields.get(t),
						val,//oneRow.get(excelTitlesColumn.get(t)),
						dbFieldsType.get(excelTitlesToDbFields.get(t)));
				if (excelLineOk == false) {
					failCnt++;
					msg = "第[" + (j + 1) + "]行的列：[" + t + "]处理出错！";
					log.warn(msg);
					bufMsg.append(msg + "<br/>");
					break;
				}
			}
			if (!excelLineOk) {
				continue;
			}
			// 补充未出现的数据
			if (notExistExcelTitles.size() > 0) {
				for (String s : notExistExcelTitles) {
					excelLineOk = setPreparedStatementValue(PStatement,
							excelTitlesToDbFields.get(s), null,
							dbFieldsType.get(excelTitlesToDbFields.get(s)));
				}
			}
			// 设置只在数据库有，excel没有的相应字段
			//city_id
			index = seqenceDbFieldsPosition.get("CITY_ID");
			try {
				PStatement.setLong(index, cityId);
			} catch (Exception e) {
				e.printStackTrace();
				failCnt++;
				msg = "第[" + (j + 1) + "]行地区信息处理出错！";
				log.warn(msg);
				bufMsg.append(msg + "<br/>");
				continue;
			}
			//bsic
			index = seqenceDbFieldsPosition.get("BSIC");
			val=oneRow.get(excelTitlesColumn.get("NCC"))+oneRow.get(excelTitlesColumn.get("BCC,NCC"));
			try {
				PStatement.setString(index, val);
			} catch (Exception e) {
				e.printStackTrace();
				failCnt++;
				msg = "第[" + (j + 1) + "]行bsic处理出错！";
				log.warn(msg);
				bufMsg.append(msg + "<br/>");
				continue;
			}
			sucCnt++;
			// 添加到批处理
			try {
				PStatement.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
				sucCnt--;
				failCnt++;
			}
		}

		// 准备导入临时表
		super.setCachedInfo(token, "正在执行数据导入...");
		log.info("华为ncs导入，准备阶段：sucCnt=" + sucCnt + ",failCnt=" + failCnt);
		// --------处理中间表-----//
		if (sucCnt == 0) {
			log.warn("导入的excel表的数据统统无效!");
			super.setCachedInfo(token, bufMsg.toString());
			return false;
		}
		try {
			log.debug("准备批量插入：");
			t1 = System.currentTimeMillis();
			PStatement.executeBatch();
			t2 = System.currentTimeMillis();
			log.debug("批量插入完成，耗时：" + (t2 - t1) + "ms.");
		} catch (Exception e) {
			e.printStackTrace();
			msg = "数据保存时出错！code=302";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			try {
				PStatement.close();
			} catch (SQLException e1) {
			}

			return false;
		}
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			msg = "准备处理数据时出错！code=303";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());

			try {
				PStatement.close();
			} catch (SQLException e1) {
			}
			return false;
		}
		String sql = "";
		super.setCachedInfo(token, "正在执行数据整理...");

		DateUtil dateUtil=new DateUtil();
		
		//处理cell相关的信息:
		//cell_freq_cnt,cell_freq_section,
		//cell_lon,cell_lat,cell_bearing,cell_downTILT,
		//CELL_INDOOR,CELL_SITE
		//TODO
		
		// 邻区匹配:由于在匹配时不仅要考虑bsic，bcch，还要考虑和主小区的距离远近，所以，
		//会连同距离信息也计算出来
		//邻区名称，距离等其他信息
		// TODO
		log.info(">>>>>>>>>>>>>>>开始邻区匹配...");
		setTokenInfo(token, "正在进行邻区数据处理");
		t1 = System.currentTimeMillis();
		rnoStructureAnalysisDao
				.matchNcsNcell(connection, "RNO_2G_HW_NCS_T", Arrays.asList(100L),cityId);
		t2 = System.currentTimeMillis();
		log.info("<<<<<<<<<<<<<<<完成邻区匹配。耗时:" + (t2 - t1) + "ms");
		

		
		// 选择生成rno_ncs_desc记录
		sql = "select mea_time,period,bsc_NAME,COUNT(*) AS CNT from " + midTable
				+ " group by mea_time,period,bsc_NAME";
		log.debug("获取包含的描述信息，sql="+sql);
		ResultSet rs = null;
		List<Map<String, Object>> ncsdes = new ArrayList<Map<String, Object>>();
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Date st = new Date(rs.getTimestamp(1).getTime());
				int period = rs.getInt(2);
				String nn = rs.getString(3);

				Map<String, Object> one = new HashMap<String, Object>();
				one.put("st", st);
				one.put("period", period);
				one.put("nn", nn);
				ncsdes.add(one);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (ncsdes.size() > 0) {
			for (Map<String, Object> one : ncsdes) {
				long seq = super.getNextSeqValue("SEQ_RNO_2G_HW_NCS_DESC_ID ",
						connection);
				Date st = (Date) one.get("st");
				String str = dateUtil.format_yyyyMMddHHmmss(st);
				sql = "update " + midTable + " set RNO_NCS_DESC_ID=" + seq
						+ " where to_char(mea_time,'yyyy-mm-dd hh24:mi:ss')='" + str + "' and period="
						+ one.get("period") + " and bsc_NAME='"
						+ one.get("nn") + "'";
				try {
					log.debug("更新一个ncs的desc id，sql="+sql);
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					log.error("更新中间表" + midTable + "的RNO_NCS_DESC_ID失败！sql="
							+ sql);
					e.printStackTrace();
					try {
						stmt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					try {
						PStatement.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					return false;
				}
			}
		}

		String fileName = (String) attachParams.get("fileName");
		// 插入ncs_desc
		sql = "insert into RNO_2G_HW_NCS_DESC(ID,CITY_ID,MEA_TIME,PERIOD,BSC_NAME,REC_COUNT,STATUS,CREATE_TIME,MOD_TIME) " +
				"SELECT  avg(RNO_NCS_DESC_ID),avg(CITY_ID),min(MEA_TIME),PERIOD,BSC_NAME,COUNT(BSC_NAME),MIN('N'),MIN(SYSDATE),MIN(SYSDATE) FROM "
				+ midTable + " group by MEA_TIME,period,BSC_NAME";

		log.debug("插入RNO_2G_HW_NCS_DESC表。。。sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("插入RNO_2G_HW_NCS_DESC表失败！sql=" + sql);
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				PStatement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		// 插入ncs记录正式表
		String fields = "MEA_TIME,CELL,ARFCN,BSIC,DISTANCE,AS360,S389,S368,S386,S363,S371,AS362,S387,S372,S360,S394,S364,S369,S388,S390,S366,S362,S361,S393,S365,S392,S391,S367,S370,NCELL,NCELLS,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_BEARING,CELL_INDOOR,NCELL_INDOOR,CELL_DOWNLIT,CELL_SITE,NCELL_SITE,CELL_FREQ_SECTION,NCELL_FREQ_SECTION";
		sql = "insert into RNO_2G_HW_NCS (RNO_2GHWNCS_DESC_ID," + fields
				+ " ) SELECT RNO_NCS_DESC_ID," + fields + " from "
				+ midTable;
		log.debug("转移到华为ncs正式表。。。sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("转移到华为正式表失败！sql=" + sql);
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				PStatement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("转移到华为ncs正式表成功。耗时：" + (t2 - t1) + "ms");

		long endTime = System.currentTimeMillis();
		t2 = endTime - begTime;// 耗时

		String res = "";// bufMsg.toString();
		res = "导入数据成功！耗时：" + (t2 / 1000) + "s。成功处理：" + sucCnt + "条记录，失败："
				+ failCnt + "。";
		if (failCnt > 0) {
			res += "详情如下：<br/>" + bufMsg.toString();
		}

		super.setCachedInfo(token, res);
		log.debug("导入的最终结果：" + res);
		try {
			PStatement.clearBatch();
		} catch (SQLException e) {
		}
		try {
			PStatement.close();
		} catch (SQLException e) {
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	private boolean setPreparedStatementValue(PreparedStatement statement,
			String dbField, String value, String valueType) {
		if (valueType == null || "".equals(valueType)) {
			return false;
		}

		int poi = seqenceDbFieldsPosition.get(dbField);
		log.debug("set param poi=" + poi + ",dbField=" + dbField + ",value="
				+ value + ",valueType=" + valueType);

		// log.info("设置poi="+poi+" dbField="+dbField+", value="+value+",valueType="+valueType);
		try {
			if ("Long".equals(valueType)) {
				if (value == null || "".equals(value.trim())) {
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
			} else if ("Integer".equals(valueType)) {
				if (value == null || "".equals(value.trim())) {
					statement.setNull(poi, Types.INTEGER);
				} else {
					int l = 0;
					try {
						l = Integer.parseInt(value);
					} catch (Exception e1) {
						e1.printStackTrace();
						statement.setNull(poi, Types.INTEGER);
					}
					statement.setInt(poi, l);
				}
			} else if ("String".equals(valueType)) {
				if (value == null) {
					statement.setNull(poi, Types.VARCHAR);
				} else {
					statement.setString(poi, value);
				}
			} else if ("Double".equals(valueType)) {
				if (value == null || "".equals(value.trim())) {
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
				if (value == null || "".equals(value.trim())) {
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
				if (value == null || "".equals(value.trim())) {
					statement.setNull(poi, Types.DATE);
				} else {
					Date d = RnoHelper.parseDateArbitrary(value);
					if (d == null) {
						log.error("日期值：[" + value + "]不是有效格式！");
						return false;//
					} else {
						statement.setTimestamp(poi, new java.sql.Timestamp(d.getTime()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		DateUtil dateUtil=new DateUtil();
		String str="05/20/2014 20:00:00";
		Date date=dateUtil.parseDateArbitrary(str);
		String ano=dateUtil.format_yyyyMMddHHmmss(date);
		System.out.println(ano);
		
		String val="小区名称=B52技工学校街道A, 小区索引=221, CGI=46000274721CD";
		String[] vals=val.split(",|=");
		System.out.println(vals.length);
		for(String v:vals){
			System.out.println(v);
		}
	}
}
