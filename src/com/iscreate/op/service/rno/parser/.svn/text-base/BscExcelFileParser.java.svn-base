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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.plat.tools.excelhelper.ExcelService;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class BscExcelFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory.getLog(BscExcelFileParser.class);

	// ---spring 注入----//
	public ExcelService excelService;
	public RnoCellDao rnoCellDao;
	public RnoBscDao rnoBscDao;
	private SysAreaDao sysAreaDao;

	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("省名称", "市名称",
			"BSC名称", "厂家");
	
	private static Map<String, String> fieldsToExcelTitles = new HashMap<String, String>() {
		{
			put("PROVINCE", "省名称");
		};
		{
			put("CITY", "市名称");
		};
		{
			put("BSC", "BSC名称");
		};
		{
			put("MANUFACTURERS", "厂家");
		};
	};
	
	private static String insertSql = " insert into RNO_BSC_TEMP (CITY_ID,BSC,MANUFACTURERS) values (?,?,?)";

	public ExcelService getExcelService() {
		return excelService;
	}

	public void setExcelService(ExcelService excelService) {
		this.excelService = excelService;
	}

	public RnoCellDao getRnoCellDao() {
		return rnoCellDao;
	}

	public void setRnoCellDao(RnoCellDao rnoCellDao) {
		this.rnoCellDao = rnoCellDao;
	}

	public RnoBscDao getRnoBscDao() {
		return rnoBscDao;
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

	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {

		log.debug("进入BscExcelFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);
		
		// 导入模式
		String importMode = (String) attachParams.get("mode");
		if (importMode == null || "".equals(importMode.trim())) {
			importMode = "overwrite";
		}
		if (!importMode.equals("overwrite") && !importMode.equals("delete")) {
			log.error("导入模式[" + importMode + "]无效！");
			super.setCachedInfo(token, "导入模式[" + importMode + "]无效！");
			return false;
		}
		
		super.setCachedInfo(token, "正在分析格式有效性...");
		long begTime = new Date().getTime();
		
		List<List<String>> allDatas = excelService.getListStringRows(file, 0);
		
		if (allDatas == null || allDatas.size() < 1) {
			try {
				super.setCachedInfo(token, "文件解析失败！因为文件是空的");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return false;
		}
		
		StringBuilder checkMsg = new StringBuilder();
		
		// 获取标题情况
		Map<String, Integer> excelTitlesColumn = new HashMap<String, Integer>();
		int colCount = super.calculateExcelTitleColumn(allDatas.get(0),
				fieldsToExcelTitles, excelTitlesColumn, checkMsg);
		log.info("计算标题所在列情况，总共有标题列：" + colCount + ",msg=" + checkMsg.toString());
		if (colCount <= 0) {
			super.setCachedInfo(token, checkMsg.toString());
			return false;
		}
		
		// 检查必须的字段是否存在
		checkMsg.setLength(0);
		boolean howTitle = baseCheckIfExcelTitleValide(excelTitlesColumn,
				mandatoryExcelTitles, checkMsg);
		log.info("检查标题情况:" + howTitle + ",msg=" + checkMsg.toString());
		if (!howTitle) {
			super.setCachedInfo(token, checkMsg.toString());
			return false;
		}
		
		//判断是否存在有效数据
		int total = allDatas.size() - 1;// excel有效记录数
		if (total <= 0) {
			log.error("excel表未包含有效数据");
			super.setCachedInfo(token, "excel表未包含有效数据");
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
			oneRow = allDatas.get(j);
			if (oneRow == null || oneRow.size() < colCount) {
				log.debug("第[" + (j + 1) + "]行数据不全。");
				continue;
			}

			// 校验数据有效性
			excelLineOk = checkDataValid((j + 1), oneRow, excelTitlesColumn,
					 bufMsg, areaId);
			if (!excelLineOk) {
				log.debug("第[" + (j + 1) + "]行数据无效。");
				bufMsg.append("<br/>");
				failCnt++;
				continue;
			}

			// 转换为sql
			for (String title : excelTitlesColumn.keySet()) {
				excelLineOk = setPreparedStatementValue(PStatement, title,
						oneRow.get(excelTitlesColumn.get(title)));
				if (excelLineOk == false) {
					failCnt++;
					msg = "第[" + (j + 1) + "]行的列：[" + title + "]处理出错！";
					log.warn(msg);
					bufMsg.append(msg + "<br/>");
					break;
				}
			}
			if (!excelLineOk) {
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
		
		super.setCachedInfo(token, "正在执行数据导入...");
		log.info("BSC数据导入，准备阶段：sucCnt=" + sucCnt + ",failCnt=" + failCnt);

		long t1,t2;
		// --------处理中间表-----//
		if (sucCnt == 0) {
			log.warn("导入的excel表的数据统统无效!");
			super.setCachedInfo(token, bufMsg.toString());
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
		
		super.setCachedInfo(token, "正在执行数据整理...");
		
		// --------操作系统表-----//
		
		//获取区域id列
		String areaStr = "";
		List<Map<String, Object>> aas=sysAreaDao.getAreaList(Integer.parseInt(areaId+""), 1);
		if(aas!=null&&aas.size()>0){
			for(Map<String, Object> aa:aas){
				if(aa.get("AREA_ID")!=null && !StringUtils.isBlank(aa.get("AREA_ID").toString())){
					areaStr += aa.get("AREA_ID").toString()+",";
				}
			}
		}
		areaStr += areaId;
		
		boolean ok = false;
		
		if(("delete").equals(importMode)){
			ok = importDelete(stmt, areaStr, bufMsg);
		} else if(("overwrite").equals(importMode)) {
			ok = importOverWrite(stmt);
		}
		
		if(!ok){
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		log.debug("导入数据操作的执行结果为："+ok);
		long endTime = System.currentTimeMillis();
		t2 = endTime - begTime;//耗时
		
		String res = "";//bufMsg.toString();
		if(ok){
			res = "导入数据成功！耗时："+(t2/1000)+"s。成功处理："+sucCnt+"条记录，失败："+failCnt+"。";
			if(failCnt >= 0){
				res += "详情如下：<br/>"+bufMsg.toString();
			}
		}else{
			res = "导入操作在数据处理时失败！耗时："+(t2/1000)+"s。有效数据数量："+sucCnt+"条，无效数据数量："+failCnt+"。";
			if(failCnt > 0){
				res += "无效数据详情如下：<br/>"+bufMsg.toString();
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
	 * 设置值到批处理sql中
	 * @param statement
	 * @param title
	 * @param string
	 * @return
	 * @author peng.jm
	 * @date 2014-10-8下午04:31:34
	 */
	private boolean setPreparedStatementValue(PreparedStatement statement,
			String title, String value) {
		
		try {
			if ("市名称".equals(title)) {
				if (value == null || "".equals(value.trim())) {
					statement.setNull(1, Types.NUMERIC);
				} else {
					//通过城市名获取其id
					Map<String,Object> area = sysAreaDao.getAreaByAreaName(value);
					long areaId = 0;
					if(area.get("AREA_ID") != null) {
						areaId = Long.parseLong(area.get("AREA_ID").toString());
					}
					statement.setLong(1, areaId);
				}
			} else if ("BSC名称".equals(title)) {
				if (value == null || "".equals(value.trim())) {
					statement.setNull(2, Types.VARCHAR);
				} else {
					statement.setString(2, value);
				}
			} else if ("厂家".equals(title)) {
				if (value == null || "".equals(value.trim())) {
					statement.setNull(3, Types.VARCHAR);
				} else {
					String v = "";
					if(("爱立信").equals(value)) {
						v = "1";
					} else if(("华为").equals(value)) {
						v = "2";
					}
					if(v == "") {
						statement.setNull(3, Types.CHAR);
					} else {
						statement.setString(3, v);
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
	 * 检查数据是否有效
	 * @param lineNum
	 * @param oneRow
	 * @param excelTitlesColumn
	 * @param bufMsg
	 * @return
	 * @author peng.jm 2014年6月9日11:57:47
	 */
	private boolean checkDataValid(int lineNum, List<String> oneRow,
			Map<String, Integer> excelTitlesColumn, StringBuilder bufMsg, long areaId) {
		
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
			
			if (key.equals("省名称")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，省名称不能为空！");
					return res;
				}
			} else if (key.equals("市名称")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，市名称不能为空！");
					return res;
				} else {
					//通过城市名获取其id
					Map<String,Object> area = sysAreaDao.getAreaByAreaName(val);
					long aId = 0;
					if(area.get("AREA_ID") != null) {
						aId = Long.parseLong(area.get("AREA_ID").toString());
					}
					if(aId != areaId) {
						res = false;
						bufMsg.append("第[" + lineNum + "]行，市名称不对应！");
						return res;
					}
				}
			} else if (key.equals("BSC名称")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，BSC名称不能为空！");
					return res;
				}
			} else if (key.equals("厂家")) {
				if (val.equals("")) {
					res = false;
					bufMsg.append("第[" + lineNum + "]行，厂家不能为空！");
					return res;
				}
			} 
		}
		return res;
	}
	
	/**
	 * 删除模式导入数据到系统表
	 * @param stmt
	 * @param areaStr
	 * @param bufMsg
	 * @return
	 * @author peng.jm 2014年6月9日15:26:05
	 */
	private boolean importDelete(Statement stmt, String areaStr, StringBuilder bufMsg) {
		
		//获取系统中存在，临时表中不存在且有小区关联的BSC
		String sql = " select engname from ( "+
			"         	  select count(cell.id) as num ,  "+
			" 	               bsc.engname as engname "+
			" 		        from cell cell, rno_bsc bsc "+
			" 		        where cell.bsc_id = bsc.bsc_id "+
			" 		            and bsc.engname not in (select bsc from rno_bsc_temp) "+
			" 					and bsc.bsc_id in (select bsc_id from rno_bsc_rela_area  "+
			" 									where area_id in ("+areaStr+")) "+
			"					and bsc.status = 'N' "+
			" 			    group by (bsc.engname)) "+
			" 		    where num > 0 ";
		log.debug("获取系统中存在，临时表中不存在且有小区关联的BSC ,sql=" + sql);
		List<String> bscList = new ArrayList<String>();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				if(rs.getString("ENGNAME") != null) {
					bscList.add(rs.getString("ENGNAME"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取系统中存在，临时表中不存在且有小区关联的BSC出错！sql=" + sql);
			return false;
		}
		String cond = "";
		String bscStr = "";
		String bscMsg = "";
		
		if(bscList.size() > 0) {
			for (int i = 0; i < bscList.size(); i++) {
				bscStr += "'"+bscList.get(i)+"',";
				bscMsg += "'"+bscList.get(i)+"', ";
				if(i%10 == 0 && i >= 10) {
					bscMsg += "<br/>";
				}
			}
			bscStr = bscStr.substring(0, bscStr.length() - 1);
			cond = " and bsc.engname not in ("+bscStr+")";
			bufMsg.append("无法删除以下BSC["+bscMsg+"]<br/>原因：与小区存在关联关系<br/>");
		}

		//将RNO_BSC表中存在，临时表中不存在的BSC状态更新为‘X’，表示已删除
		sql = " update rno_bsc bsc set bsc.status = 'X'" +
						" where bsc.engname not in (select bsc from rno_bsc_temp)" +
							" and bsc.bsc_id in (" +
								" select bsc_id from rno_bsc_rela_area " +
									" where area_id in ("+areaStr+"))" + cond;
		log.debug("准备更新存在系统中,但不存在导入数据中的BSC状态为“X” ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
			log.debug("更新存在系统中,但不存在导入数据中的BSC状态为“X” ");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新RNO_BSC表状态为“X” 出错！sql=" + sql);
			return false;
		}	
		
		//将对应bsc与区域关系数据删除
		sql = " delete from rno_bsc_rela_area rela " +
				" where rela.bsc_id in( " +
						"select bsc.bsc_id from rno_bsc bsc " +
							" where bsc.engname not in (select bsc from rno_bsc_temp)" +
							  " and bsc.bsc_id in (select bsc_id from rno_bsc_rela_area " +
											" where area_id in ("+areaStr+"))" + 
							    cond + ")";
		log.debug("将对应bsc与区域关系数据删除 ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("将对应bsc与区域关系数据删除出错！sql=" + sql);
			return false;
		}
		
		//调用增量模式
		boolean ok = importOverWrite(stmt);
		if(!ok){
			return false;
		}
		
		return true;
	}

	/**
	 * 增量模式导入数据到系统表
	 * @param stmt
	 * @return
	 * @author peng.jm 2014年6月9日15:26:05
	 */
	private boolean importOverWrite(Statement stmt) {
		
		//更新RNO_BSC表
		String sql = " MERGE INTO RNO_BSC TAR " +
						" USING (SELECT * FROM RNO_BSC_TEMP) TEMP " +
						" ON (TAR.ENGNAME = TEMP.BSC) " +
					 " WHEN MATCHED THEN UPDATE SET " +
						   	  "  TAR.CHINESENAME = TEMP.BSC, " +
						      "  TAR.MANUFACTURERS = TEMP.MANUFACTURERS, " +
						      "  TAR.STATUS = 'N' " +
					 " WHEN NOT MATCHED THEN " +
						      " INSERT (TAR.BSC_ID, TAR.CHINESENAME, TAR.ENGNAME, TAR.MANUFACTURERS, TAR.STATUS) " +
							  " VALUES (SEQ_RNO_BSC.NEXTVAL, TEMP.BSC, TEMP.BSC, TEMP.MANUFACTURERS, 'N')";
		log.debug("准备更新系统表：RNO_BSC ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
			log.debug("更新系统表：RNO_BSC执行完成");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新系统表：RNO_BSC时执行出错！sql=" + sql);
			return false;
		}
		
		//更新RNO_BSC_RELA_AREA
		sql = " MERGE INTO RNO_BSC_RELA_AREA TAR " +
						" USING (select t.city_id as CITY_ID,  " +
						" 		        bsc.bsc_id as BSC_ID " +
						" 		  from rno_bsc bsc,  " +
						" 			   rno_bsc_temp t " +
						" 		where t.bsc = bsc.engname) TEMP " +
						" ON (TAR.AREA_ID = TEMP.CITY_ID AND TAR.BSC_ID = TEMP.BSC_ID) " +
			 " WHEN NOT MATCHED THEN " +
			      " INSERT (TAR.BSC_AREA_ID, TAR.BSC_ID, TAR.AREA_ID) " +
				  " VALUES (SEQ_RNO_BSC_RELA_AREA.NEXTVAL, TEMP.BSC_ID, TEMP.CITY_ID)";
		log.debug("准备更新系统表：RNO_BSC_RELA_AREA ,sql=" + sql);
		try {
			stmt.executeUpdate(sql);
			log.debug("更新系统表：RNO_BSC_RELA_AREA执行完成");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新系统表：RNO_BSC_RELA_AREA时执行出错！sql=" + sql);
			return false;
		}
		return true;
	}
}
