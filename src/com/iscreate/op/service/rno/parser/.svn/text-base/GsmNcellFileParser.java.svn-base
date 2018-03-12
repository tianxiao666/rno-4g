package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoBscRelaArea;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.tools.excelhelper.ExcelService;

public class GsmNcellFileParser extends ExcelFileParserBase {
	private static Log log = LogFactory.getLog(GsmNcellFileParser.class);

	// ---spring 注入----//
	// public MemCachedClient memCached;
	public ExcelService excelService;
	public RnoCellDao rnoCellDao;
	public RnoBscDao rnoBscDao;
	private SysAreaDao sysAreaDao;
	// public IFileParserManager fileParserManager;

	private static List<String> expectTitles = Arrays.asList("BSC", "CELLR",
			"CELL", "CS", "DIR");

	private static int titleSize = expectTitles.size();

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

	public void setRnoCellDao(RnoCellDao rnoCellDao) {
		this.rnoCellDao = rnoCellDao;
	}

	public void setRnoBscDao(RnoBscDao rnoBscDao) {
		this.rnoBscDao = rnoBscDao;
	}

	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	// 数据库字段的类型
	Map<String, String> dbFieldsType = new HashMap<String, String>() {
		{
			put("CELL", "String");
		};
		{
			put("NCELL", "String");
		};
		{
			put("CS", "String");
		};
		{
			put("DIR", "String");
		};
		{
			put("BSC_ID", "Long");
		};
	};

	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("CELLR",
			"CELL", "CS", "DIR", "BSC");

	// key为：数据库字段名
	// value为：excel标题名
	private static Map<String, String> dbFieldsToExcelTitles = new HashMap<String, String>() {
		{
			put("BSC_ID", "BSC");
		};
		{
			put("NCELL", "CELLR");
		};
		{
			put("CELL", "CELL");
		};
		{
			put("CS", "CS");
		};
		{
			put("DIR", "DIR");
		};
	};

	// excel title到dbfield的映射,由上面的db-》excel title得到
	// key:excel文件的title
	// value:dbfield
	private static Map<String, String> excelTitlesToDbFields = new HashMap<String, String>();

	// 构建insert语句
	private static String midTable = "RNO_NCELL_TEMP";
	private static StringBuffer insertSqlTemp = new StringBuffer();
	private static String insertSql = "";
	// 在insert语句中出现的字段名称
	private static List<String> seqenceDbFields = new ArrayList<String>();
	// 每个db字段在sql出现的位置，从1开始,这个应该可以不需要
	private static Map<String, Integer> seqenceDbFieldsPosition = new HashMap<String, Integer>();

	private static Set<String> dbFields = dbFieldsToExcelTitles.keySet();
	static {
		// insertSql = "insert into " + table + " (";
		insertSqlTemp.append("insert into " + midTable + " (");
		int poi = 1;
		for (String f : dbFields) {
			// insertSql += f + ",";
			insertSqlTemp.append(f + ",");
			// System.out.println("dfFields:" + f);
			seqenceDbFields.add(f);
			seqenceDbFieldsPosition.put(f, poi++);

			// excel title 到 db field 的映射
			excelTitlesToDbFields.put(dbFieldsToExcelTitles.get(f), f);
			// if(poi==6){
			// log.info("=======================6666666========="+f+","+dbFieldsToExcelTitles.get(f));
			// }
		}

		// insertSql += " ) values(";
		insertSqlTemp.deleteCharAt(insertSqlTemp.length() - 1);
		insertSqlTemp.append(") values (");
		for (int i = 0; i < dbFields.size(); i++) {
			// insertSql += "?,";
			insertSqlTemp.append("?,");
			// System.out.println("seqenceDbFields: "+seqenceDbFields.get(i));
		}
		insertSqlTemp.deleteCharAt(insertSqlTemp.length() - 1);
		insertSqlTemp.append(")");
		insertSql = insertSqlTemp.toString();
		// insertSql += ")";

	}

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("GsmNcellFileParser：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);

		// 如果是update为true，说明要不旧的邻区数据干掉，否则，就是在之前的基础上添加不重复的数据
		// 在追加的情况下：rno_ncell的主键为cell+ncell，用这个来进行约束，不插入重复
		// 在全覆盖的情况下(update为true):把表里的主小区全部列举出来，批量删除其邻区，再执行增加邻区
		// 获取全部数据

		setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
				"正在解析邻区文件");
		// ------------------验证excel文件中的记录是否合法，将合法的记录写入中间表--------------
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);

		if (allDatas == null || allDatas.size() < 1) {
			try {
				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
						"文件解析失败！因为文件是空的");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		// 检查文件标题
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
		List<String> notExistExcelTitles = getNotListExcelTitles(excelTitlesColumn);
		int total = allDatas.size() - 1;// excel有效记录数
		List<String> oneData;
		StringBuilder buf = new StringBuilder();
		// 获取数据库中所有的bsc
		List<RnoBsc> bscs = rnoBscDao.getAllBsc();
		Map<String, Long> bscNameToId = new HashMap<String, Long>();
		if (bscs != null && !bscs.isEmpty()) {
			for (RnoBsc bs : bscs) {
				bscNameToId.put(bs.getEngname(), bs.getBscId());
			}
		}

		// 新建bsc
		String insertBscSql = "insert into rno_bsc (BSC_ID,CHINESENAME,ENGNAME) values(?,?,?)";
		PreparedStatement bscStatement = null;
		try {
			bscStatement = connection.prepareStatement(insertBscSql);
		} catch (SQLException e2) {
			log.info("获取bscStatement失败");
			log.info(e2.getStackTrace());
		}
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！code=303");
			return false;
		}

		// 获取bscId和areaId的关系
		List<RnoBscRelaArea> rbraList = rnoBscDao
				.getBscRelaAreaByAreaId(areaId);
		// 新建bsc和area的关系
		PreparedStatement pStatement = null;
		PreparedStatement insertBscAreaStatement = null;
		String insertBscAreaSql = "insert into rno_bsc_rela_area values(SEQ_RNO_BSC_RELA_AREA.NEXTVAL,?,?)";
		log.info("插入中间表的sql语句为：" + insertSql);
		try {
			pStatement = connection.prepareStatement(insertSql);
			insertBscAreaStatement = connection
					.prepareStatement(insertBscAreaSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}

		int insertSuccessCnt = 0, updateSuccessCnt = 0, failCnt = 0;
		Set<String> ncellPairs=new HashSet<String>();//用来排除重复邻区关系的
		String msg = "";
		log.info("插入中间表的sql=" + insertSql);
		List<String> cellList = new ArrayList<String>();
		int exethreshold = 0;// 每达到3000条就执行batch insert
		for (int i = 1; i < allDatas.size(); i++) {
			super.fileParserManager.updateTokenProgress(token, i / total
					* 0.85f);
			oneData = allDatas.get(i);
			if (oneData == null || oneData.size() < mandatoryExcelTitles.size()) {
				msg = "第[" + (i + 1) + "]行数据不齐全！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			Long bscId;
			String bsc;
			String cellr;
			String cell;
			String dir;
			String cs;
			// 检查cell是否合法
			cell = oneData.get(excelTitlesColumn.get("CELL"));
			if (cell == null || cell.trim().isEmpty()) {
				assembleMsg(buf, i, cell);
				failCnt++;
				continue;
			} else {
				if (!cellList.contains(cell)) {
					cellList.add(cell);
				}
			}
			// 检查cellr是否合法
			cellr = oneData.get(excelTitlesColumn.get("CELLR"));
			if (cellr == null || cellr.trim().isEmpty()) {
				assembleMsg(buf, i, cellr);
				failCnt++;
				continue;
			}
			//2014-10-21 gmh 新增
			if(ncellPairs.contains(cell+cellr)){
				msg = "第[" + (i + 1) + "]行邻区关系："+cell+" - "+cellr+" 重复！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			ncellPairs.add(cell+cellr);
			// 检查dir是否合法
			dir = oneData.get(excelTitlesColumn.get("DIR"));
			if (dir == null || dir.trim().isEmpty()) {
				assembleMsg(buf, i, dir);
				failCnt++;
				continue;
			}
			// 检查cs是否合法
			cs = oneData.get(excelTitlesColumn.get("CS"));
			if (cs == null || cs.trim().isEmpty()) {
				assembleMsg(buf, i, cs);
				failCnt++;
				continue;
			}
			// 检查bsc是否合法
			bsc = oneData.get(excelTitlesColumn.get("BSC"));
			if (null != bsc && !bsc.trim().isEmpty()) {
				bscId = bscNameToId.get(bsc);
				// 如果未创建bsc
				if (null == bscId) {
					bscId = AddBsc(statement, bscStatement, bscNameToId, buf,
							bsc, i);
					// 更新bsc不成功
					if (null == bscId) {
						failCnt++;
						continue;
					}
				}
				boolean flag = validateBscRelaArea(connection,
						insertBscAreaStatement, rbraList, bscId, areaId, buf,
						bsc, i);
				if (flag) {
					oneData.set(excelTitlesColumn.get("BSC"), bscId.toString());
				} else {
					failCnt++;
					continue;
				}

			} else {
				assembleMsg(buf, i, bsc);
				failCnt++;
				continue;
			}

			// 设置语句
			// 对于excel里出现的都进行设置，对于没有在excel出现的，则设置为null
			boolean excelLineOk = false;
			for (String t : excelTitlesColumn.keySet()) {
				excelLineOk = setPreparedStatementValue(pStatement,
						excelTitlesToDbFields.get(t),
						oneData.get(excelTitlesColumn.get(t)),
						dbFieldsType.get(excelTitlesToDbFields.get(t)));
				if (excelLineOk == false) {
					failCnt++;
					log.warn("第[" + (i + 1) + "]行的列：[" + t + "]处理出错！");
					break;
				}
			}

			// 补充未出现的数据
			if (notExistExcelTitles.size() > 0) {
				for (String s : notExistExcelTitles) {
					excelLineOk = setPreparedStatementValue(pStatement,
							excelTitlesToDbFields.get(s), null,
							dbFieldsType.get(excelTitlesToDbFields.get(s)));
					if (excelLineOk == false) {
						failCnt++;
						break;
					}
				}
			}
			if (excelLineOk == true) {
				try {
					pStatement.addBatch();
					insertSuccessCnt++;
				} catch (SQLException e) {
					log.info("添加批处理失败");
					log.info(e.getStackTrace());
					failCnt++;
				}
			}
			exethreshold++;
			if (exethreshold >= 3000) {
				try {
					pStatement.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					pStatement.clearBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				exethreshold = 0;
			}

		}
		// log.info("fateCount: "+fateCount);
		// 执行
		if (exethreshold > 0) {
			// 插入中间表
			try {
				pStatement.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				msg = "数据保存时出错！code=302";
				log.error(msg);
				buf.append(msg + "<br/>");
				return false;
			}
		}
		if (null != insertBscAreaStatement) {
			try {
				insertBscAreaStatement.close();
			} catch (SQLException e) {
				log.info("关闭insertBscAreaStatement失败");
				log.info(e.getStackTrace());
			}
		}
		if (null != bscStatement) {
			try {
				bscStatement.close();
			} catch (SQLException e) {
				log.info("关闭bscStatement失败");
				log.info(e.getStackTrace());
			}
		}

		ncellPairs=null;
		// --------------------------合并中间表和目标表---------------------------
		String targetTable = "RNO_NCELL";
//		if (update)// 覆盖(删除已有邻区关系)
//		{
			// 删除cell的所有邻区关系
			// String deleteCountSql = "select count(*) from " + targetTable +
			// " where cell =?";
			/*String deleteOldDataSql = "delete from " + targetTable
					+ " where cell = ?";
			PreparedStatement updateSqlpstmt = null;
			try {
				updateSqlpstmt = connection.prepareStatement(deleteOldDataSql);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("在准备更新的statment时出错！");
				return false;
			}
			exethreshold = 0;
			for (String cell : cellList) {
				try {
					updateSqlpstmt.setString(1, cell);
					updateSqlpstmt.addBatch();
				} catch (Exception e) {
					e.printStackTrace();
					log.error("在准备更新的statment时出错！");
					return false;
				}
				exethreshold++;
				if (exethreshold > 3000) {
					try {
						updateSqlpstmt.executeBatch();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						updateSqlpstmt.clearBatch();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					exethreshold = 0;
				}

			}
			if (exethreshold > 0) {
				try {
					updateSqlpstmt.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					log.error("在准备更新的statment时出错！");
					return false;
				}
			}
//		}
*/
		

		String targetSequence = "SEQ_RNO_NCELL";
//		String mergeSql = "merge into " + targetTable + " targ using "
//				+ midTable
//				+ " mid on(targ.CELL = mid.CELL and targ.NCELL = mid.NCELL) ";
//		// 对于覆盖(更新),cell和ncell都相同记录则对目标表更新
//		/*
//		 * if(update) { mergeSql += "when matched then update set "; for(String
//		 * field : dbFields) { if(!"CELL".equalsIgnoreCase(field) &&
//		 * !"NCELL".equalsIgnoreCase(field)) { mergeSql += "targ." + field + "="
//		 * + "mid." + field + ","; } } mergeSql = mergeSql.substring(0,
//		 * mergeSql.length()-1); //mergeSql += ")";
//		 * 
//		 * }
//		 */
//		// 新的记录则插入目标表
//		mergeSql += " when not matched then insert (NCELL_ID,";
//		for (String field : dbFields) {
//			mergeSql += field + ",";
//		}
//		mergeSql = mergeSql.substring(0, mergeSql.length() - 1);
//		mergeSql += ") values (" + targetSequence + ".NEXTVAL,";
//		for (String field : dbFields) {
//			mergeSql += "mid." + field + ",";
//		}
//		mergeSql = mergeSql.substring(0, mergeSql.length() - 1);
//		mergeSql += ")";

		
		//----2014-8-28，以上sql作废，直接插入
		
		long cityId=-1L;
		String condStr="";
		int ac=0;
		int condCnt=0;
		try{
			//此处的areaid是区/县的id
			//拿到city id
			List<Map<String,Object>> ress=sysAreaDao.getParentAreaListByAreaId(areaId);
			if(ress!=null&&ress.size()>0){
				for(Map<String,Object> res:ress){
					if("市".equals(res.get("areaLevel"))){
						cityId=Long.parseLong(res.get("areaId").toString());
						break;
					}
				}
			}
			
//			cityId=Long.parseLong(sysAreaDao.getParentAreaByAreaId(areaId).get("AREA_ID").toString());
			List<Map<String, Object>> aas=sysAreaDao.getAreaList(Integer.parseInt(cityId+""), 1);
			if(aas!=null&&aas.size()>0){
				for(Map<String, Object> aa:aas){
					if(aa.get("AREA_ID")!=null && !StringUtils.isBlank(aa.get("AREA_ID").toString())){
						if(ac==0){
							condStr+=(condCnt==0?"":" or ")+" rela.area_id in ( "+aa.get("AREA_ID").toString()+",";
						}else{
							condStr+=aa.get("AREA_ID").toString()+",";
						}
						ac++;
					}
					if(ac>900){
						condStr=condStr.substring(0, condStr.length()-1);
						condStr+=" )";
						condCnt++;
						ac=0;
					}
				}
			}
			if(ac>0){
				condStr=condStr.substring(0, condStr.length()-1);
				condStr+=" )";
			}
		}catch(Exception e){
			e.printStackTrace();
			condStr="";
		}
//		if(update){
			//更新，删除旧数据
			String deleteSql="";
			if(condStr.length()>0){
				deleteSql="delete from "+targetTable+" tab1 where exists  ( select 1 from rno_bsc_rela_area rela where rela.bsc_id=tab1.bsc_id and ( "+condStr+" ))";
			}else{
				deleteSql="delete from "+targetTable+" tab1 where exists ( select 1 from rno_bsc_rela_area rela where rela.bsc_id=tab1.bsc_id and rela.area_id ="+areaId+")";
			}
			
			try {
				updateSuccessCnt=statement.executeUpdate(deleteSql);
				log.debug("删除旧有邻区数据量： "+updateSuccessCnt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
//		}
		
		String mergeSql="insert into "+targetTable+" ( NCELL_ID,";
		for (String field : dbFields) {
			mergeSql += field + ",";
		}
		mergeSql = mergeSql.substring(0, mergeSql.length() - 1);
		mergeSql += " ) select " + targetSequence + ".NEXTVAL,";
		for (String field : dbFields) {
			mergeSql +=field + ",";
		}
		mergeSql = mergeSql.substring(0, mergeSql.length() - 1);
		mergeSql += " from "+midTable;
		
		
		log.info("总的用中间表[" + midTable + "]对目标表[" + targetTable + "]更新、插入语句为："
				+ mergeSql);
		try {
			updateSuccessCnt = statement.executeUpdate(mergeSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			msg = "执行用中间表[" + midTable + "]的数据更新目标表[" + targetTable
					+ "]的时候出错！sql=" + mergeSql;
			log.error(msg);
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"数据处理出错！邻区关系有重复！");
			return false;
		}
		String result = "";

		result = "导入完成。共 [" + total + "]条记录，成功 [" + updateSuccessCnt
				+ "]条记录，出错 [" + failCnt + "]条记录。<br/>";
		log.info(result);
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result
					+ buf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 创建邻区对象
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 14, 2013 1:07:28 PM gmh
	 */
	private RnoNcell createNCellFromExcelLine(List<String> oneData, int i,
			StringBuilder buf) {
		String msg = "";
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
		/*
		 * BSC CELLR CELL CS DIR ZHM05B1 S5ALSZ7 S5ASHL7 NO MUTUAL
		 */
		RnoNcell ncellObj;
		String bsc, ncell, cell, cs, dir;
		int index = 0;
		bsc = oneData.get(index++);//
		if (bsc == null || bsc.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含BSC数据！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		ncell = oneData.get(index++);
		if (ncell == null || ncell.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含邻小区名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		cell = oneData.get(index++);
		if (cell == null || cell.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含主小区名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		cs = oneData.get(index++);
		if (cs == null || cs.trim().isEmpty()) {
			cs = "NO";
		}
		dir = oneData.get(index++);
		if (dir == null || dir.trim().isEmpty()) {
			dir = "MUTUAL";
		}

		ncellObj = new RnoNcell();
		ncellObj.setCell(cell);
		ncellObj.setCs(cs);
		ncellObj.setDir(dir);
		ncellObj.setNcell(ncell);

		return ncellObj;
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
					// TODO
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
	 * 
	 * @author Liang YJ
	 * @date 2014-1-28 下午12:54:31
	 * @param buf
	 * @param i
	 * @param title
	 */
	private void assembleMsg(StringBuilder buf, int i, String title) {
		String msg = "第[" + (i + 1) + "]行的" + title + "没有值";
		log.error(msg);
		buf.append(msg + "<br/>");
	}

	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-28 上午11:25:41
	 * @param conn
	 * @param bscStatement
	 * @param buf
	 * @param bsc
	 * @param i
	 * @return bscId
	 * @description 向rno_bsc插入engname=bsc的记录
	 * 
	 */
	private Long AddBsc(Statement stmt, PreparedStatement bscStatement,
			Map<String, Long> bscNameToId, StringBuilder buf, String bsc, int i) {
//		Long bscId = super.getNextSeqValue("SEQ_RNO_BSC", connection);
		Long bscId =RnoHelper.getNextSeqValue("SEQ_RNO_BSC", stmt);
		try {
			bscStatement.setLong(1, bscId);
			bscStatement.setString(2, bsc);
			bscStatement.setString(3, bsc);
			bscStatement.executeUpdate();
			log.info("更新了" + bsc);
			bscNameToId.put(bsc, bscId);
			return bscId;
		} catch (SQLException e) {
			String msg = "插入第[" + (i + 1) + "]行的BSC错,系统中没有该BSC";
			log.error(msg);
			buf.append(msg + "<br/>");
			log.info(e.getStackTrace());
			bscId = null;
			return bscId;
		}
	}

	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-8 下午2:33:39
	 * @param conn
	 * @param insertBscAreaStatement
	 * @param rbraList
	 * @param bscId
	 * @param areaId
	 * @param buf
	 * @param bsc
	 * @param i
	 * @return
	 */
	private boolean validateBscRelaArea(Connection conn,
			PreparedStatement insertBscAreaStatement,
			List<RnoBscRelaArea> rbraList, Long bscId, long areaId,
			StringBuilder buf, String bsc, int i) {
		boolean flag = true;
		for (RnoBscRelaArea rbra : rbraList) {
			if (bscId.equals(rbra.getBscId())) {
				return flag;
			}
		}
		if (flag) {
			try {
				insertBscAreaStatement.setLong(1, bscId);
				insertBscAreaStatement.setLong(2, areaId);
				int cnt = insertBscAreaStatement.executeUpdate();
				log.info("更新了" + bsc + "和" + areaId);
				RnoBscRelaArea rbra = new RnoBscRelaArea();
				rbra.setBscId(bscId);
				rbra.setAreaId(areaId);
				rbraList.add(rbra);
				return flag;
			} catch (SQLException e) {
				String msg = "插入第[" + (i + 1) + "]行的BSC出错,系统中没有该BSC与AREA关系";
				log.error(msg);
				buf.append(msg + "<br/>");
				log.info(e.getStackTrace());
				return !flag;
			}
		}
		return !flag;
	}

}
