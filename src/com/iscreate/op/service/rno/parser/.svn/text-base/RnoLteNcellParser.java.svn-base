package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.PreparedStatement;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoLteNcellParser extends ExcelFileParserBase {

	private static Log log = LogFactory.getLog(RnoLteNcellParser.class);

	private static List<String> expectTitles = Arrays.asList("Serving Cell Name",
			"Neighbor Cell Name", "Serving cellId", "Serving eNBId", "Neighbor eNBId",
			"Neighbor cellId", "Serving Site ID", "Neighbor Site ID");
	
	// key为：数据库字段名
	// value为：excel标题名
	private static Map<String, String> dbFieldsToExcelTitles = new HashMap<String, String>() {
		{
			put("LTE_CELL_NAME", "Serving Cell Name");
		};
		{
			put("LTE_NCELL_NAME", "Neighbor Cell Name");
		};
		{
			put("LTE_CELL_ID", "Serving cellId");
		};
		{
			put("LTE_CELL_ENODEB_ID", "Serving eNBId");
		};
		{
			put("LTE_NCELL_ENODEB_ID", "Neighbor eNBId");
		};
		{
			put("LTE_NCELL_ID", "Neighbor cellId");
		};
		{
			put("LTE_CELL_SITE_ID", "Serving Site ID");
		};
		{
			put("LTE_NCELL_SITE_ID", "Neighbor Site ID");
		};
	};
	
	// excel title到dbfield的映射,由上面的db-》excel title得到
	//key:excel文件的title
	//value:dbfield
	private static Map<String, String> excelTitlesToDbFields = new HashMap<String, String>();
	
	// 数据库字段的类型
	Map<String, String> dbFieldsType = new HashMap<String, String>() {
		{
			put("LTE_CELL_NAME", "String");
		};
		{
			put("LTE_NCELL_NAME", "String");
		};
		{
			put("LTE_CELL_ID", "String");
		};
		{
			put("LTE_CELL_ENODEB_ID", "String");
		};
		{
			put("LTE_NCELL_ENODEB_ID", "String");
		};
		{
			put("LTE_NCELL_ID", "String");
		};
		{
			put("LTE_CELL_SITE_ID", "String");
		};
		{
			put("LTE_NCELL_SITE_ID", "String");
		};
	};
	
	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("Serving Cell Name",
			"Neighbor Cell Name", "Serving cellId", "Serving eNBId", "Neighbor eNBId",
			"Neighbor cellId");
	
	// 构建insert语句
	private static String midTable = "RNO_LTE_NCELL_TEMP";
	private static StringBuffer insertSqlTemp = new StringBuffer();
	private static String insertSql = "";
	// 在insert语句中出现的字段名称
	private static List<String> seqenceDbFields = new ArrayList<String>();
	// 每个db字段在sql出现的位置，从1开始,这个应该可以不需要
	private static Map<String, Integer> seqenceDbFieldsPosition = new HashMap<String, Integer>();

	private static Set<String> dbFields = dbFieldsToExcelTitles.keySet();
	static
	{
		//insertSql = "insert into " + table + " (";
		insertSqlTemp.append("insert   /*+ append */  into "+midTable+" (");
		int poi = 1;
		for (String f : dbFields) {
			//insertSql += f + ",";
			insertSqlTemp.append(f + ",");
			//System.out.println("dfFields:" + f);
			seqenceDbFields.add(f);
			seqenceDbFieldsPosition.put(f, poi++);

			// excel title 到 db field 的映射
			excelTitlesToDbFields.put(dbFieldsToExcelTitles.get(f), f);
			// if(poi==6){
			// log.info("=======================6666666========="+f+","+dbFieldsToExcelTitles.get(f));
			// }
		}

		//insertSql += " ) values(";
		insertSqlTemp.deleteCharAt(insertSqlTemp.length()-1);
		insertSqlTemp.append(") values (");
		for (int i = 0; i < dbFields.size(); i++) {
			//insertSql += "?,";
			insertSqlTemp.append("?,");
			//System.out.println("seqenceDbFields: "+seqenceDbFields.get(i));
		}
		insertSqlTemp.deleteCharAt(insertSqlTemp.length()-1);
		insertSqlTemp.append(")");
		insertSql = insertSqlTemp.toString();
		//insertSql += ")";
		//insertSql:"insert into RNO_LTE_NCELL_TEMP(*,*,*,....) values (?,?,?,.....)"
	}
	
	
	/**
	 * @author peng.jm
	 * 2014年6月9日11:13:19
	 */
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		log.debug("进入RnoLteNcellParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);
		
		super.setCachedInfo(token, "正在分析格式有效性...");
		fileParserManager.updateTokenProgress(token,  "正在分析格式有效性...");
		
		long begTime = new Date().getTime();
		
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
		
		// 获取标题情况
		Map<String, Integer> excelTitlesColumn = new HashMap<String, Integer>();
		StringBuilder checkMsg = new StringBuilder();
		int colCount = super.calculateExcelTitleColumn(allDatas.get(0),
				dbFieldsToExcelTitles, excelTitlesColumn, checkMsg);
		log.info("计算标题所在列情况，总共有标题列：" + colCount + ",msg=" + checkMsg.toString());
		if (colCount <= 0) {
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					checkMsg.toString());
			fileParserManager.updateTokenProgress(token,  checkMsg.toString());
			return false;
		}
		
		// 检查必须的字段是否存在
		checkMsg.setLength(0);
		
		boolean howTitle = baseCheckIfExcelTitleValide(excelTitlesColumn,
				mandatoryExcelTitles, checkMsg);
		log.info("检查标题情况:" + howTitle + ",msg=" + checkMsg.toString());
		if (!howTitle) {
			if(checkMsg.length()!=0){
				
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						checkMsg.toString());
			}else {
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						 "必要的字段不存在！");
			}
			fileParserManager.updateTokenProgress(token,  "必要的字段不存在！");
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
			fileParserManager.updateTokenProgress(token, "excel表未包含有效数据");
			return false;
		}
		
		// 准备数据库插入statement
		PreparedStatement PStatement = null;
		log.debug("插入中间表的sql语句为：" + insertSql);
		try {
			PStatement = connection.prepareStatement(insertSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！insertSql="
					+ insertSql);
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			fileParserManager.updateTokenProgress(token, "系统出错!code=301");
			return false;
		}
		
		super.setCachedInfo(token, "正在逐行分析数据...");
		
		// --------逐行分析excel数据，转换为导入中间表的sql---//
		StringBuilder bufMsg = new StringBuilder();
		int failCnt = 0, sucCnt = 0;
		int index;
		boolean excelLineOk;
		String msg;
		int dataSize = allDatas.size();
		List<String> oneRow = null;	
		for (int j = 1; j < dataSize; j++) {
			super.setCachedInfo(token, "正在逐行分析数据...当前处理："+j+"/"+(dataSize-1));
			fileParserManager.updateTokenProgress(token, 1f);
			oneRow = allDatas.get(j);
			if (oneRow == null || oneRow.size() < colCount) {
				continue;
			}

			// 校验数据有效性
			excelLineOk = checkDataValid((j + 1), oneRow, excelTitlesColumn,
					 bufMsg);
			if (!excelLineOk) {
				log.debug("第[" + (j + 1) + "]行数据无效。");
				bufMsg.append("<br/>");
				failCnt++;
				continue;
			}

			// 转换为sql
			// 对于excel里出现的都进行设置，对于没有在excel出现的，则设置为null
			for (String t : excelTitlesColumn.keySet()) {
				excelLineOk = setPreparedStatementValue(PStatement,
						excelTitlesToDbFields.get(t),
						oneRow.get(excelTitlesColumn.get(t)),
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
		
		super.setCachedInfo(token, "正在执行数据导入...");
		fileParserManager.updateTokenProgress(token, 1f);
		log.info("lte邻区关系导入，准备阶段：sucCnt=" + sucCnt + ",failCnt=" + failCnt);
		long t1,t2;
		// --------处理中间表-----//
		if (sucCnt == 0) {
			log.warn("导入的excel表的数据统统无效!");
			super.setCachedInfo(token, bufMsg.toString());
			fileParserManager.updateTokenProgress(token, 1f);
			return false;
		}
		try {
			log.debug("准备批量插入：");
			t1=System.currentTimeMillis();
			PStatement.executeBatch();
			t2=System.currentTimeMillis();
			log.debug("批量插入完成，耗时："+(t2-t1)+"ms.");
		} catch (Exception e) {
			e.printStackTrace();
			msg = "数据保存时出错！code=302";
			log.error(msg);
			bufMsg.append(msg + "<br/>");
			super.setCachedInfo(token, bufMsg.toString());
			fileParserManager.updateTokenProgress(token, 1f);
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
			fileParserManager.updateTokenProgress(token, 1f);
			try {
				PStatement.close();
			} catch (SQLException e1) {
			}
			return false;
		}
		String sql = "";
		
		super.setCachedInfo(token, "正在执行数据整理...");
		fileParserManager.updateTokenProgress(token, 1f);
		// --------操作系统表-----//
		boolean ok;
		
		ok = importDataForSysTable(stmt);
		
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
		
		super.setCachedInfo(token, res);
		log.debug("导入的最终结果："+res);
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

	/**
	 * 检查数据是否有效
	 * @param lineNum
	 * @param oneRow
	 * @param excelTitlesColumn
	 * @param bufMsg
	 * @return
	 * @author peng.jm 2014年6月9日11:57:47
	 */
	private boolean checkDataValid(int lineNum, List<String> oneRow,
			Map<String, Integer> excelTitlesColumn, StringBuilder bufMsg) {
		
		String val;
		Integer col;
		boolean res = true;
		
		for (String key : excelTitlesColumn.keySet()) {
			col = excelTitlesColumn.get(key);
			if (col == null) {
				continue;
			}
			val = oneRow.get(col);
			val = val == null ? "" : val.trim();
			
			if (key.equals("Serving Cell Name")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Serving Cell Name不能为空！");
					return res;
				}
			} else if (key.equals("Neighbor Cell Name")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Neighbor Cell Name不能为空！");
					return res;
				}
			} else if (key.equals("Serving cellId")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Serving cellId不能为空！");
					return res;
				}
			} else if (key.equals("Serving eNBId")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Serving eNBId不能为空！");
					return res;
				}
			} else if (key.equals("Neighbor eNBId")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Neighbor eNBId不能为空！");
					return res;
				}
			} else if (key.equals("Neighbor cellId")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，Neighbor cellId不能为空！");
					return res;
				}
			}
		}
		return res;
	}

	/**
	 * 设置值到批处理sql中
	 * @param statement
	 * @param dbField
	 * @param value
	 * @param valueType
	 * @return
	 * @author peng.jm 2014年6月9日13:46:51
	 */
	private boolean setPreparedStatementValue(PreparedStatement statement,
			String dbField, String value, String valueType) {
		
		if (valueType == null || "".equals(valueType)) {
			return false;
		}

		int poi = seqenceDbFieldsPosition.get(dbField);
		log.debug("set param poi="+poi+",dbField="+dbField+",value="+value+",valueType="+valueType);
				
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
			}else if ("Integer".equals(valueType)) {
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
			}  
			else if ("String".equals(valueType)) {
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
						statement.setDate(poi, new java.sql.Date(d.getTime()));
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
	 * 从临时表导入到系统表
	 * @param stmt
	 * @return
	 * @author peng.jm 2014年6月9日15:26:05
	 */
	private boolean importDataForSysTable(Statement stmt) {
		log.debug("进入方法：importDataForSysTable");
		
		String sql = "MERGE INTO RNO_LTE_NCELL TAR " +
						" USING (SELECT * fROM RNO_LTE_NCELL_TEMP) TEMP " +
						" ON (TAR.LTE_CELL_ID = TEMP.LTE_CELL_ID and TAR.LTE_NCELL_ID = TEMP.LTE_NCELL_ID) " +
					 " WHEN MATCHED THEN UPDATE SET " +
							  "	 TAR.LTE_CELL_NAME  = TEMP.LTE_CELL_NAME, " +
						   	  "  TAR.LTE_NCELL_NAME      = TEMP.LTE_NCELL_NAME, " +
						      "  TAR.LTE_CELL_ENODEB_ID  = TEMP.LTE_CELL_ENODEB_ID, " +
						      "  TAR.LTE_NCELL_ENODEB_ID = TEMP.LTE_NCELL_ENODEB_ID, " +
						      "  TAR.LTE_CELL_SITE_ID    = TEMP.LTE_CELL_SITE_ID, " +
						      "  TAR.LTE_NCELL_SITE_ID   = TEMP.LTE_NCELL_SITE_ID " +
					 " WHEN NOT MATCHED THEN " +
						      " INSERT (TAR.LTE_NCELL_RELA_ID, TAR.LTE_CELL_NAME, TAR.LTE_NCELL_NAME, TAR.LTE_CELL_ID," +
										" TAR.LTE_NCELL_ID,TAR.LTE_CELL_ENODEB_ID,  TAR.LTE_NCELL_ENODEB_ID, TAR.LTE_CELL_SITE_ID," +
										" TAR.LTE_NCELL_SITE_ID ) " +
							  " VALUES  (SEQ_LTE_NCELL_ID.NEXTVAL, TEMP.LTE_CELL_NAME,TEMP.LTE_NCELL_NAME, TEMP.LTE_CELL_ID, " +
								"TEMP.LTE_NCELL_ID, TEMP.LTE_CELL_ENODEB_ID,TEMP.LTE_NCELL_ENODEB_ID, " +
								"TEMP.LTE_CELL_SITE_ID,TEMP.LTE_NCELL_SITE_ID )";
		log.debug("准备更新系统表：RNO_LTE_NCELL ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
			log.debug("更新系统表：RNO_LTE_NCELL执行完成");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新系统表：RNO_LTE_NCELL时执行出错！sql=" + sql);
			return false;
		}
		return true;
	}
}
