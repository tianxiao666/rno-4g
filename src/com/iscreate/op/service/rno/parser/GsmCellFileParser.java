package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.tools.excelhelper.ExcelService;

/**
 * 导入gsm小区数据
 * 
 * @author brightming
 * 
 */
public class GsmCellFileParser extends GsmPlanCellFileParser {

	private static Log log = LogFactory.getLog(GsmCellFileParser.class);

//	// ---spring 注入----//
//	// public MemCachedClient memCached;
//	public ExcelService excelService;
//	public RnoCellDao rnoCellDao;
//	public RnoBscDao rnoBscDao;
//	private RnoCommonService rnoCommonService;
//	private SysAreaDao sysAreaDao;
//	// public IFileParserManager fileParserManager;
//
//	private static List<String> expectTitles = Arrays.asList("地区", "BSC",
//			"cell", "CELLNAME", "LAC", "CI", "BCCH", "BSIC", "TCH",
//			"天线方向", "天线下倾", "基站类型", "天线高度", "天线类型", "LON", "LAT", "覆盖范围","重要等级","覆盖类型");
//
//	private static int titleSize = expectTitles.size();
//	
//	//允许的覆盖范围
//	private static List<String> allowCoverAreas=Arrays.asList("室内覆盖","室外覆盖","混合覆盖");
//	//允许的重要等级
//	private static List<String> allowImportancegrades=Arrays.asList("VIP","非VIP");
//
//	public void setFileParserManager(IFileParserManager fileParserManager) {
//		this.fileParserManager = fileParserManager;
//		super.setFileParserManager(fileParserManager);
//	}
//
//	public void setCache(MemcachedClient memCached) {
//		this.memCached = memCached;
//		super.setMemCached(memCached);
//	}
//
//	public void setExcelService(ExcelService excelService) {
//		this.excelService = excelService;
//	}
//
//	public void setRnoCommonService(RnoCommonService rnoCommonService) {
//		this.rnoCommonService = rnoCommonService;
//	}
//
//	public void setRnoCellDao(RnoCellDao rnoCellDao) {
//		this.rnoCellDao = rnoCellDao;
//	}
//
//	public void setRnoBscDao(RnoBscDao rnoBscDao) {
//		this.rnoBscDao = rnoBscDao;
//	}
//
//	public void setSysAreaDao(SysAreaDao sysAreaDao) {
//		this.sysAreaDao = sysAreaDao;
//	}
//
//	@Override
//	protected boolean parseDataInternal(String token, File file,
//			boolean needPersist, boolean update, long oldConfigId, long areaId,
//			boolean autoload, Map<String, Object> attachParams) {
//		log.debug("进入CellFileParser方法：parseDataInternal。 token=" + token
//				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
//				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);
//		// Date expiry = new Date(System.currentTimeMillis() + 1 * 60 * 60);//
//		// 1h
//
//		long begTime = new Date().getTime();
//
//		// 导入到的目标区域（区/县级别）
//		SysArea country = sysAreaDao.getAreaById(areaId);
//		if(country==null){
//			log.error("不存在id="+areaId+"的区域！");
//			try {
//				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
//						"指定的导入区域不存在！");
//			} catch (TimeoutException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (MemcachedException e) {
//				e.printStackTrace();
//			}
//			return false;
//		}else{
//			log.info("准备导入小区数据到："+country.getName());
//		}
//		// 获取全部数据
//		List<List<String>> allDatas = excelService.getListStringRows(file, 0);
//
//		if (allDatas == null || allDatas.size() < 1) {
//			try {
//				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
//						"文件解析失败！因为文件是空的");
//			} catch (TimeoutException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (MemcachedException e) {
//				e.printStackTrace();
//			}
//			return false;
//		}
//
//		// 检查文件标题
//		boolean titleok = super.checkTitles(expectTitles, allDatas.get(0));
//		if (!titleok) {
//			log.error("上传的小区excel文件的格式不符合格式要求！");
//			try {
//				memCached.set(token, RnoConstant.TimeConstant.TokenTime,
//						"文件解析失败！确保文件的格式为：" + expectTitles);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return false;
//		}
//
//		int total = allDatas.size() - 1;// excel有效记录数
//
//		// 从excel解析得到的。如果是不需要持久化的数据，那么这些需要放置在缓存中
//		List<Cell> allCellsFromExcel = new ArrayList<Cell>();
//
//		// 获取当前已有的小区
//		List<String> dbCells = rnoCellDao.getCellNameByAreaId(areaId);
//		// 获取全部的bsc
//		List<RnoBsc> bscs = rnoBscDao.getAllBsc();
//		Map<String, Long> bscNameToId = new HashMap<String, Long>();
//		if (bscs != null && !bscs.isEmpty()) {
//			for (RnoBsc bs : bscs) {
//				bscNameToId.put(bs.getEngname(), bs.getBscId());
//			}
//		}
//
//		boolean exists = true;
//		List<String> oneData;
//		StringBuilder buf = new StringBuilder();
//
//		// 数据连接
//		Connection connection = DataSourceConn.initInstance().getConnection();
//		try {
//			connection.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		PreparedStatement insertSqlpstmt = null;
//		PreparedStatement updateSqlpstmt = null;
//
//		String insertSql = "insert into cell (ID,BSC_ID,LABEL,NAME,LAC,CI,BCCH,BSIC,TCH,BEARING,DOWNTILT,BTSTYPE,ANT_HEIGH,ANT_TYPE,LONGITUDE,LATITUDE,COVERAREA,CELL_DESC_ID,AREA_ID,LNGLATS,GSMFREQUENCESECTION,IMPORTANCEGRADE,COVERTYPE) "
//				+ " values(SEQ_CELL.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//		String updateSql = "update cell set NAME=?,LAC=?,CI=?,BCCH=?,BSIC=?,TCH=?,BEARING=?,DOWNTILT=?,BTSTYPE=?,ANT_HEIGH=?,ANT_TYPE=?,LONGITUDE=?,LATITUDE=?,COVERAREA=?,LNGLATS=?,GSMFREQUENCESECTION=? ,IMPORTANCEGRADE=?,COVERTYPE=? WHERE label=?";
//		if (needPersist) {
//			try {
//				insertSqlpstmt = connection.prepareStatement(insertSql);
//			} catch (SQLException e) {
//				e.printStackTrace();
//				log.error("在准备插入的statment时出错！");
//				return false;
//			}
//			try {
//				updateSqlpstmt = connection.prepareStatement(updateSql);
//			} catch (SQLException e) {
//				e.printStackTrace();
//				log.error("在准备更新的statment时出错！");
//				return false;
//			}
//		}
//
//		Cell oneCell;
//
//		Long bscId;
//		String bscName = "";
//		boolean hasInsert = false, hasUpdate = false;
//		int index = 1;
//		int insertSuccessCnt = 0, updateSuccessCnt = 0, failCnt = 0;
//		boolean hasError = false;// 是否有错
//
//		// 通过区级area，获取市级area
//		List<Area> cityAreas = rnoCommonService.getSpecialUpperAreas(
//				new long[] { areaId }, "市");
//
//		// 基准点
//		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = null;
//		if (cityAreas != null && cityAreas.size() > 0) {
//			standardPoints = rnoCommonService
//					.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityAreas.get(0).getArea_id(),
//							RnoConstant.DBConstant.MAPTYPE_BAIDU);
//		}
//
//		
//		int areaColumn=0;
//		int bscColumn=1;
//		int cellColumn=2;
//		for (int i = 1; i < allDatas.size(); i++) {
//			oneData = allDatas.get(i);
//			// 产生小区对象
//			oneCell = createCellFromExcelLine(country.getName(),oneData, i, buf, standardPoints);
//			if (oneCell == null) {
//				// 数据不符合要求
//				failCnt++;
//				continue;
//			}
//
//			// 对应的bsc的id
//			bscName = oneData.get(bscColumn);
//			if (bscNameToId.containsKey(bscName)) {
//				bscId = bscNameToId.get(bscName);
//			} else {
//				RnoBsc bsc = new RnoBsc(bscName, bscName);
//				bscId = rnoBscDao.insertBsc(areaId, bsc);
//				bsc.setBscId(bscId);
//				bscNameToId.put(bscName, bscId);
//			}
//			oneCell.setBscId(bscId);
//
//			// areaid
//			oneCell.setAreaId(areaId);
//
//			// 需要持久化
//			if (needPersist) {
//				if (dbCells.contains(oneData.get(cellColumn))) {
//					exists = true;
//				} else {
//					exists = false;
//				}
//				// 开始处理
//				if (exists) {
//					if (update) {
//						hasUpdate = true;// 标记
//						updateSuccessCnt++;
//
//						log.debug("更新现有小区数据。");
//						index = 1;
//
//						// set LAC=?,CI=?,BCCH=?,BSIC=?,TCH=?,
//						try {
//							updateSqlpstmt
//									.setString(index++, oneCell.getName());
//							updateSqlpstmt.setLong(index++, oneCell.getLac());
//							updateSqlpstmt.setLong(index++, oneCell.getCi());
//							updateSqlpstmt.setLong(index++, oneCell.getBcch());
//							updateSqlpstmt.setLong(index++, oneCell.getBsic());
//							updateSqlpstmt.setString(index++, oneCell.getTch());
//
//							// BEARING=?,DOWNTILT=?,BTSTYPE=?,ANT_HEIGH=?,ANT_TYPE=?,
//							updateSqlpstmt.setLong(index++,
//									oneCell.getBearing());
//							updateSqlpstmt.setLong(index++,
//									oneCell.getDowntilt());
//							updateSqlpstmt.setString(index++,
//									oneCell.getBtstype());
//							updateSqlpstmt.setLong(index++,
//									oneCell.getAntHeigh());
//							updateSqlpstmt.setString(index++,
//									oneCell.getAntType());
//
//							// LONGITUDE=?,LATITUDE=?,COVERAREA=?,LNGLATS=?
//							// WHERE label=?
//							updateSqlpstmt.setDouble(index++,
//									oneCell.getLongitude());
//							updateSqlpstmt.setDouble(index++,
//									oneCell.getLatitude());
//							updateSqlpstmt.setString(index++,
//									oneCell.getCoverarea());
//							updateSqlpstmt.setString(index++,
//									oneCell.getLngLats());
//							updateSqlpstmt.setString(index++,
//									oneCell.getGsmfrequencesection());
//							updateSqlpstmt.setString(index++, oneCell.getImportancegrade());
//							updateSqlpstmt.setString(index++, oneCell.getCovertype());
//							updateSqlpstmt.setString(index++,
//									oneCell.getLabel());
//
//							updateSqlpstmt.addBatch();
//						} catch (SQLException e) {
//							e.printStackTrace();
//							hasError = true;
//							break;
//						}
//					}
//				} else {
//					// 插入
//					index = 1;
//					hasInsert = true;// 有插入
//					insertSuccessCnt++;
//					try {
//						// ID,BSC_ID,LABEL,NAME,
//						// 用sequence
//						// insertSqlpstmt.setLong(index++,
//						// structureCommonService
//						// .getEntityPrimaryKey("Cell"));
//						insertSqlpstmt.setLong(index++, oneCell.getBscId());
//						insertSqlpstmt.setString(index++, oneCell.getLabel());
//						insertSqlpstmt.setString(index++, oneCell.getName());
//						// LAC,CI,BCCH,BSIC,TCH,
//						insertSqlpstmt.setLong(index++, oneCell.getLac());
//						insertSqlpstmt.setLong(index++, oneCell.getCi());
//						insertSqlpstmt.setLong(index++, oneCell.getBcch());
//						insertSqlpstmt.setLong(index++, oneCell.getBsic());
//						insertSqlpstmt.setString(index++, oneCell.getTch());
//
//						// BEARING,DOWNTILT,BTSTYPE,ANT_HEIGH,ANT_TYPE,
//						insertSqlpstmt.setLong(index++, oneCell.getBearing());
//						insertSqlpstmt.setLong(index++, oneCell.getDowntilt());
//						insertSqlpstmt.setString(index++, oneCell.getBtstype());
//						insertSqlpstmt.setLong(index++, oneCell.getAntHeigh());
//						insertSqlpstmt.setString(index++, oneCell.getAntType());
//
//						// LONGITUDE,LATITUDE,COVERAREA,CELL_DESC_ID,AREA_ID,LNGLATS
//						insertSqlpstmt.setDouble(index++,
//								oneCell.getLongitude());
//						insertSqlpstmt
//								.setDouble(index++, oneCell.getLatitude());
//						insertSqlpstmt.setString(index++,
//								oneCell.getCoverarea());
//						insertSqlpstmt.setLong(index++, oldConfigId);
//						insertSqlpstmt.setLong(index++, oneCell.getAreaId());
//						insertSqlpstmt.setString(index++, oneCell.getLngLats());
//						insertSqlpstmt.setString(index++,
//								oneCell.getGsmfrequencesection());
//						insertSqlpstmt.setString(index++, oneCell.getImportancegrade());
//						insertSqlpstmt.setString(index++, oneCell.getCovertype());
//						insertSqlpstmt.addBatch();
//
//					} catch (SQLException e) {
//						e.printStackTrace();
//						hasError = true;
//						break;
//					}
//				}
//			} else {
//				// 不需要持久化，置入缓存
//				allCellsFromExcel.add(oneCell);
//			}
//
//			// 更新进度
//			if (i != total) {
//				fileParserManager.updateTokenProgress(token, i * 1.0f / total);
//			}
//		}
//
//		if (!hasError && (hasInsert || hasUpdate)) {
//			if (hasInsert) {
//				try {
//					int[] res1 = insertSqlpstmt.executeBatch();
//					// System.out.println("res1.len="+res1.length);
//					// for(int i=0;i<res1.length;i++){
//					// System.out.println("insert "+i+" --- 结果： "+res1[i]);
//					// }
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (hasUpdate) {
//				try {
//					int[] res2 = updateSqlpstmt.executeBatch();
//					// System.out.println("res2.len="+res2.length);
//					// for(int i=0;i<res2.length;i++){
//					// System.out.println("update "+i+" --- 结果： "+res2[i]);
//					// }
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			// commit
//			try {
//				connection.commit();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//
//		}
//
//		if (insertSqlpstmt != null) {
//			try {
//				insertSqlpstmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		if (updateSqlpstmt != null) {
//			try {
//				updateSqlpstmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		try {
//			connection.setAutoCommit(true);
//			connection.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		boolean ok = !hasError;// 有错就不ok
//		String result = "";
//
//		long endTime = new Date().getTime();
//		if (!hasError) {
//			// 缓存导入结果
//			result = "导入完成。耗时:" + (endTime - begTime) / 1000 + "秒。共 [" + total
//					+ "] 条记录，成功新增 [" + insertSuccessCnt + "] 条记录，更新["
//					+ updateSuccessCnt + "]条记录，出错 [" + failCnt + "] 条记录。<br/>";
//		} else {
//			result = "导入时出错！";
//		}
//		log.info("gsm小区导入的结果：" + result);
//
//		try {
//			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result
//					+ buf.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		allDatas = null;
//		return ok;
//	}
//
//	/**
//	 * 判断数据有效性，如果数据有效，返回创建的小区；否则返回null
//	 * 
//	 * @param oneData
//	 * @param i
//	 * @param buf
//	 * @return Sep 11, 2013 3:43:54 PM gmh
//	 */
//	private Cell createCellFromExcelLine(String country,List<String> oneData, int i,
//			StringBuilder buf,
//			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
//
//		// 字段数据
//		String areaName=null;
//		String bscName = null;
//		Long bscId = null;
//		String label = null;
//		String name = null;
//		Long lac = null;
//		Long ci = null;
//		Long bcch = null;
//		Long bsic = null;
//		String tch = null;
//		Long bearing = null;
//		Long downtilt = null;
//		String btsType = null;
//		Long antHeigh = null;
//		String antType = null;
//		Double longitude = null;
//		Double latitude = null;
//		String coverarea = null;
//		String lngLats = null;
//		String gsmfrequenceSection = null;
//		String importancegrade="";
//		String covertype="";
//		
//		String msg = "";
//		// --------开始 检查数据有效性-----------------------------------------------//
//		if (oneData == null) {
//			msg = "第[" + (i + 1) + "]行错误！数据为空！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		if (oneData.size() < titleSize) {
//			msg = "第[" + (i + 1) + "]行错误！数据不齐全！";
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		
//		int index=0;
//		//区域
//		areaName=oneData.get(index++);
//		if(!country.equals(areaName)){
//			msg = "第[" + (i + 1) + "]行错误！小区所在的不在指定的导入区域！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// 检查数据有效性
//		bscName = oneData.get(index++);
//		if (bscName == null || bscName.trim().isEmpty()) {
//			msg = "第[" + (i + 1) + "]行错误！数据未包含bsc！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//
//		// label
//		label = oneData.get(index++);
//		if (label == null || label.trim().isEmpty()) {
//			msg = "第[" + (i + 1) + "]行错误！数据未包含小区名！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//
//		// name
//		name = oneData.get(index++);
//		if (name == null || name.trim().isEmpty()) {
//			msg = "第[" + (i + 1) + "]行错误！数据未包含小区中文名！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// "LAC", "CI", "ARFCN", "BSIC", "NON_BCCH", "天线方向",
//		// lac
//		try {
//			lac = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据包含错误的lac！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// ci
//		try {
//			ci = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据包含错误的ci！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// bcch
//		try {
//			bcch = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据包含错误的bcch！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// bsic
//		try {
//			bsic = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据包含错误的bsic！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// tch不检查
//		tch = oneData.get(index++);
//		tch=tch==null?"":tch;
//		// 天线方向
//		try {
//			bearing = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据包含错误的天线方向！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//
//		// "天线下倾", "基站类型", "天线高度", "天线类型", "LON", "LAT", "覆盖范围"
//		try {
//			downtilt = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			downtilt = 0l;
//		}
//		btsType = oneData.get(index++);
//		btsType=btsType==null?"":btsType;
//		try {
//			antHeigh = Long.parseLong(oneData.get(index++));
//		} catch (Exception e) {
//			antHeigh = 0l;
//		}
//		antType = oneData.get(index++);
//		antType=antType==null?"":antType;
//		try {
//			longitude = Double.parseDouble(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据未包含经度！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//
//		try {
//			latitude = Double.parseDouble(oneData.get(index++));
//		} catch (Exception e) {
//			msg = "第[" + (i + 1) + "]行错误！数据未包含纬度！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		coverarea = oneData.get(index++);
//		coverarea=coverarea==null?"":coverarea;
//		if(!allowCoverAreas.contains(coverarea)){
//			msg = "第[" + (i + 1) + "]行错误！覆盖范围只能是："+allowCoverAreas;
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		
//		importancegrade=oneData.get(index++);
//		importancegrade=importancegrade==null?"":importancegrade;
//		if(!allowImportancegrades.contains(importancegrade)){
//			msg = "第[" + (i + 1) + "]行错误！重要等级只能是："+allowImportancegrades;
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		
//		covertype=oneData.get(index++);
//		covertype=covertype==null?"":covertype;
//		// --------结束 检查数据有效性-----------------------------------------------//
//
//		// 计算其余的顶点坐标
//		// if (bearing % 360 != 0) {
//		int scatterAngle = 30;
//		int len = 60;
//		if (bcch < 100) {
//			// 900
//			scatterAngle = 30;
//			len = 120;
//			gsmfrequenceSection = "GSM900";
//		} else {
//			scatterAngle = 60;
//			len = 80;
//			gsmfrequenceSection = "GSM1800";
//		}
//
//		// 转为百度坐标计算展示需要的顶点
//		// 先从缓存获取
//		String suf = longitude + "," + latitude;
//		String[] baidulnglat = null;
//		try {
//			// 先从缓存获取
//			baidulnglat = memCached
//					.get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE + suf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为：" + baidulnglat);
//		if (baidulnglat == null) {
//			if (standardPoints != null && standardPoints.size() > 0) {
//				baidulnglat = rnoCommonService.getLngLatCorrectValue(longitude
//						, latitude , standardPoints);
//			} else {
//				log.warn("区域不存在基准点，将使用百度在线接口进行校正。");
//				// 缓存不存在，需要计算
//				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
//						+ "", latitude + "");
//			}
//			// 保存入缓存
//			try {
//				memCached.set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
//						+ suf, RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
//						baidulnglat);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//		}
//		if (baidulnglat != null) {
//			double[][] other = CoordinateHelper.OutputCoordinates(
//					Double.valueOf(baidulnglat[0]),
//					Double.valueOf(baidulnglat[1]), (int) (bearing * 1),
//					scatterAngle, len);
//			lngLats = baidulnglat[0] + "," + baidulnglat[1] + ";" + other[0][0]
//					+ "," + other[0][1] + ";" + other[1][0] + "," + other[1][1];
//		} else {
//			msg = "第[" + (i + 1) + "]行错误！数据包含经纬度坐标有误！";
//			log.warn(msg);
//			buf.append(msg + "<br/>");
//			return null;
//		}
//		// }
//
//		Cell oneCell = new Cell();
//		oneCell.setBscId(bscId);
//		oneCell.setLabel(label);
//		oneCell.setName(name);
//		oneCell.setLac(lac);
//		oneCell.setCi(ci);
//		oneCell.setBcch(bcch);
//		oneCell.setBsic(bsic);
//		oneCell.setTch(tch);
//		oneCell.setBearing(bearing);
//		oneCell.setDowntilt(downtilt);
//		oneCell.setBtstype(btsType);
//		oneCell.setAntHeigh(antHeigh);
//		oneCell.setAntType(antType);
//		oneCell.setLongitude(longitude);
//		oneCell.setLatitude(latitude);
//		oneCell.setCoverarea(coverarea);
//		oneCell.setLngLats(lngLats);
//		oneCell.setImportancegrade(importancegrade);
//		oneCell.setCovertype(covertype);
//		oneCell.setGsmfrequencesection(gsmfrequenceSection);
//
//		return oneCell;
//	}
}
