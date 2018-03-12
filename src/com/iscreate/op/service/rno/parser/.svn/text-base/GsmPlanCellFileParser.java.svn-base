package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpSession;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.rno.RnoPlanDesignDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoBscRelaArea;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoPlanDesignService;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.plat.tools.excelhelper.ExcelService;

/**
 * 导入gsm小区数据
 * 
 * @author chao.xj
 * 
 */
public class GsmPlanCellFileParser extends ExcelFileParserBase {

	private static Log log = LogFactory.getLog(GsmPlanCellFileParser.class);

	// ---spring 注入----//
	public RnoBscDao rnoBscDao;
	private SysAreaDao sysAreaDao;
	private static Gson gson = new GsonBuilder().create();// 线程安全
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

	private static List<String> expectTitles = Arrays.asList("地区", "BSC",
			"cell", "CELLNAME", "LAC", "CI", "BCCH", "BSIC", "TCH", "天线方向",
			"天线下倾", "基站类型", "天线高度", "天线类型", "LON", "LAT", "覆盖范围", "重要等级",
			"覆盖类型",
			// 新增部分
			"天线型号", "半功率角", "杆塔类型", "极化类型","是否室内覆盖小区");

	// key为：数据库字段名
	// value为：excel标题名
	private static Map<String, String> dbFieldsToExcelTitles = new HashMap<String, String>() {
		{
			put("NAME", "CELLNAME");
		};
		{
			put("LABEL", "cell");
		};
		{
			put("COVERTYPE", "覆盖类型");
		};
		{
			put("COVERAREA", "覆盖范围");
		};
		{
			put("IMPORTANCEGRADE", "重要等级");
		};
		{
			put("GSMFREQUENCESECTION", "频段类型");
		};
		{
			put("ADDRESS", "地址");
		};
		{
			put("LAC", "LAC");
		};
		{
			put("CI", "CI");
		};
		{
			put("BSIC", "BSIC");
		};
		{
			put("BCCH", "BCCH");
		};
		{
			put("TCH", "TCH");
		};
		{
			put("ANT_MANUFACTORY", "天线厂家");
		};
		{
			put("ANT_TYPE", "天线类型");
		};
		{
			put("ANT_GAIN", "增益");
		};
		{
			put("ANT_HEIGH", "天线高度");
		};
		{
			put("BASETYPE", "站址类型");
		};
		{
			put("DOWNTILT", "天线下倾");
		};
		{
			put("BEARING", "天线方向");
		};
		{
			put("MAX_TX_BS", "MAX_TX_BS");
		};
		{
			put("MAX_TX_MS", "MAX_TX_MS");
		};
		{
			put("ANT_MODEL", "天线型号");
		};
		{
			put("ELECTRICALDOWNTILT", "电调下倾角");
		};
		{
			put("MECHANICALDOWNTILT", "机械下倾角");
		};
		{
			put("LONGITUDE", "LON");
		};
		{
			put("LATITUDE", "LAT");
		};
		{
			put("SITE", "站址名称");
		};
		{
			put("BTSTYPE", "基站类型");
		};
		{
			put("AREA_NAME", "地区");
		};
		{
			put("BSC_NAME", "BSC");
		};
		{
			put("ANT_HALF_POWER", "半功率角");
		};
		{
			put("ANT_POLARIZATION_TYPE", "极化类型");
		};
		{
			put("HEIGHT_ABOVE_SEA", "海拔高度");
		};
		{
			put("TOWER_TYPE", "杆塔类型");
		};
		{
			put("INDOOR_CELL_TYPE","是否室内覆盖小区");
		};
		// {put("BSC_ID","");};
		// {put("AREA_ID","");};
		// {put("LNGLATS","");};
	};
	// excel title到dbfield的映射,由上面的db-》excel title得到
	private static Map<String, String> excelTitlesToDbFields = new HashMap<String, String>();
	// 只存在于中间表，不存在目标表的字段
	private static List<String> fieldsNotExistInTargetTab = Arrays.asList(
			"AREA_NAME", "BSC_NAME");
	// 数据库字段的类型
	Map<String, String> dbFieldsType = new HashMap<String, String>() {
		{
			put("NAME", "String");
		};
		{
			put("LABEL", "String");
		};
		{
			put("COVERTYPE", "String");
		};
		{
			put("COVERAREA", "String");
		};
		{
			put("IMPORTANCEGRADE", "String");
		};
		{
			put("GSMFREQUENCESECTION", "String");
		};
		{
			put("ADDRESS", "String");
		};
		{
			put("LAC", "Long");
		};
		{
			put("CI", "Long");
		};
		{
			put("BSIC", "Long");
		};
		{
			put("BCCH", "Long");
		};
		{
			put("TCH", "String");
		};
		{
			put("ANT_MANUFACTORY", "String");
		};
		{
			put("ANT_TYPE", "String");
		};
		{
			put("ANT_GAIN", "Float");
		};
		{
			put("ANT_HEIGH", "Float");
		};
		{
			put("BASETYPE", "String");
		};
		{
			put("DOWNTILT", "Float");
		};
		{
			put("BEARING", "Float");
		};
		{
			put("MAX_TX_BS", "Float");
		};
		{
			put("MAX_TX_MS", "Float");
		};
		{
			put("ANT_MODEL", "String");
		};
		{
			put("ELECTRICALDOWNTILT", "Float");
		};
		{
			put("MECHANICALDOWNTILT", "Float");
		};
		{
			put("LONGITUDE", "Float");
		};
		{
			put("LATITUDE", "Float");
		};
		{
			put("SITE", "String");
		};
		{
			put("BTSTYPE", "String");
		};
		{
			put("AREA_NAME", "String");
		};
		{
			put("BSC_NAME", "String");
		};
		{
			put("ANT_HALF_POWER", "Float");
		};
		{
			put("ANT_POLARIZATION_TYPE", "String");
		};
		{
			put("HEIGHT_ABOVE_SEA", "Float");
		};
		{
			put("TOWER_TYPE", "String");
		};
		{
			put("INDOOR_CELL_TYPE","String");
		};
		// {put("BSC_ID","");};
		// {put("AREA_ID","");};
		// {put("LNGLATS","");};
	};

	// 构建insert语句
	private static String midTable = "RNO_MID_CELL";
	private static String insertInoMidTableSql = "";
	// 在insert语句中出现的字段名称
	private static List<String> seqenceDbFields = new ArrayList<String>();
	// 每个db字段在sql出现的位置，从1开始
	private static Map<String, Integer> seqenceDbFieldsPosition = new HashMap<String, Integer>();

	private static Set<String> dbFields = dbFieldsToExcelTitles.keySet();
	static {
		insertInoMidTableSql = "insert into " + midTable + " (";
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
		insertInoMidTableSql += "BSC_ID,AREA_ID,LNGLATS";
		seqenceDbFieldsPosition.put("BSC_ID", poi++);
		seqenceDbFieldsPosition.put("AREA_ID", poi++);
		seqenceDbFieldsPosition.put("LNGLATS", poi++);

		insertInoMidTableSql += " ) values(";
		for (int i = 0; i < dbFields.size(); i++) {
			insertInoMidTableSql += "?,";
		}
		insertInoMidTableSql += "?,?,?";
		insertInoMidTableSql += ")";

	}

	// 必须具备的excel标题名
	private static List<String> mandatoryExcelTitles = Arrays.asList("cell",
			"CELLNAME", "LAC", "CI", "BSIC", "BCCH", "TCH", "LON", "LAT",
			"BSC", "天线方向","是否室内覆盖小区","半功率角","天线高度","站址名称","天线下倾");
	//--2014-3-23 add "是否室内覆盖小区" --2014-3-27 add "半功率角","天线高度"

	// excel标题所在列编号（从0开始），这个要放在方法内，因为每次导入都不同，同时这里的
	// key也就是用户的excel表有的标题
	// private Map<String,Integer> excelTitlesColumn=null;

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

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	public void setRnoBscDao(RnoBscDao rnoBscDao) {
		this.rnoBscDao = rnoBscDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	/**
	 * 2013-12-24 gmh comment: 1、check if area information is provided and valid
	 * 2、extract excel titles from excel file and their column position(start
	 * from 0) 3、check if all mandatory excel titles are ready 4、prepare and
	 * validate variables passed from web browser 5、get all allowed area ids and
	 * area names; 6、begin to handle excel lines: 1)add those valid data into
	 * rno_mid_cell table which is a temporary table meanwhile,collect all the
	 * area names into a list variable: occurAreaNames; 2)update rno_mid_cell
	 * and set all rows bsc_id 3)if is asked to save the data to rno_tmp_cell
	 * table,then: a、add some temporary cell descriptor records according to
	 * collection variable:occurAreaNames b、update the rno_mid_cell table with
	 * descriptor id ,this action may repeat several times,depends on the number
	 * of the element in occurAreaNames c、transfer all data in rno_mid_cell into
	 * rno_temp_cell table 4)if is asked to override the system configure cell
	 * data,then: a、update field desc_id in rno_mid_cell table with system
	 * descriptor id b、find out all area id in rno_mid_cell whose desc_id is
	 * null, c、add some system cell descriptor based on the information from
	 * above; d、update desc_id with new descriptor id whose desc_id is null e、
	 * 4.1) if update flag is set,then: update table cell using rno_mid_cell if
	 * label exists both in cell and rno_mid_cell append table cell using
	 * rno_mid_cell if label only exists in rno_mid_cell 4.2) if update flag is
	 * not set,then: append table cell using rno_mid_cell if label only exists
	 * in rno_mid_cell
	 */
	@Override
	protected boolean parseDataInternal(String token, File file,
			boolean needPersist, boolean update, long oldConfigId, long areaId,
			boolean autoload, Map<String, Object> attachParams) {
		log.debug("进入CellFileParser方法：parseDataInternal。 token=" + token
				+ ",file=" + file + ",needPersist=" + needPersist + ",update="
				+ update + ",oldConfigId=" + oldConfigId + ",areaId=" + areaId);
		// Date expiry = new Date(System.currentTimeMillis() + 1 * 60 * 60);//
		// 1h

		long begTime = new Date().getTime();

		// 2013-12-24 check city id
		// long cityId = -1;
		// try {
		// cityId = Long.parseLong(attachParams.get("cityId").toString());
		// } catch (Exception e) {
		// log.error("无效的城市id！");
		// super.setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
		// "指定的导入区域不存在！");
		// return false;
		// }

		// 当前的areaid的含义是city 的id
		long cityId = areaId;
		// get all country area under the city
		List<Map<String, Object>> allAreas = sysAreaDao
				.getSubAreasInSpecAreaLevel(new long[] { cityId }, "区/县");
		if (allAreas == null || allAreas.isEmpty()) {
			log.error("city=" + cityId + " 下没有区县设置！");
			super.setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"指定的导入区域下不存在区/县，请联系管理员！");
			return false;
		}
		log.info("allAreas=" + allAreas);
		// map area name to area id
		Map<String, Long> areaNameToIds = new HashMap<String, Long>();
		long oneid = -1;
		for (Map<String, Object> one : allAreas) {
			oneid = ((BigDecimal) one.get("AREA_ID")).longValue();
			if (oneid == cityId) {
				continue;
			}
			areaNameToIds.put(one.get("NAME").toString(), oneid);
			
			//2014-3-5 清空相应区域对应缓存小区信息
			try{
				memCached.delete(RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE+oneid);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		log.info("areaNameToIds=" + areaNameToIds);
		// ----------now we have all area and the relations between name to id

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

		// 2013-12-24 gmh add 将excel标题的序号列出来
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
		// 获取全集中，未出现在excel表title中的title（这些是允许不出现的）
		log.info("excelTitlesColumn=" + excelTitlesColumn.keySet());
		List<String> notExistExcelTitles = getNotListExcelTitles(excelTitlesColumn);

		// ---------以上完成了对标题所在位置的判断，与部分必须存在的标题的检验

		int total = allDatas.size() - 1;// excel有效记录数

		if (total <= 0) {
			log.error("excel表未包含有效数据");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"excel表未包含有效数据");
			return false;
		}
		// 从excel解析得到的。如果是不需要持久化的数据，那么这些需要放置在缓存中
		// 获取全部的bsc
		List<RnoBsc> bscs = rnoBscDao.getAllBsc();
		Map<String, Long> bscNameToId = new HashMap<String, Long>();
		if (bscs != null && !bscs.isEmpty()) {
			for (RnoBsc bs : bscs) {
				bscNameToId.put(bs.getEngname(), bs.getBscId());
			}
		}
		

		List<String> oneData;
		StringBuilder buf = new StringBuilder();

		// 数据连接
		// Connection connection =
		// DataSourceConn.initInstance().getConnection();
		// try {
		// connection.setAutoCommit(false);
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }

		// 2013-12-24 插入中间表的语句
		// PreparedStatement insertIntoMidTableStatement = null;
		PreparedStatement PStatement = null;
		log.info("插入中间表的sql语句为：" + insertInoMidTableSql);
		try {
			PStatement = connection.prepareStatement(insertInoMidTableSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}
		
		//获取bscId和areaId的关系
		List<RnoBscRelaArea> rbraList = rnoBscDao.getBscRelaAreaByAreaId(areaId);
		//有bsc,但没有bsc和area关系，插入bsc和area关系记录
		PreparedStatement insertBscAreaStatement = null;
		String insertBscAreaSql = "insert into rno_bsc_rela_area values(SEQ_RNO_BSC_RELA_AREA.NEXTVAL,?,?)";
		try
		{
			insertBscAreaStatement = connection.prepareStatement(insertBscAreaSql);
		} catch (Exception e) {
			log.error("准备插入中间表的preparedStatement时出错！");
			setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
					"系统出错!code=301");
			return false;
		}

		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = null;
		standardPoints = rnoCommonService
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,
						RnoConstant.DBConstant.MAPTYPE_BAIDU);

		int insertSuccessCnt = 0, updateSuccessCnt = 0, failCnt = 0;
		// 循环处理excel 行
		Long bscId;
		String bscName = "";
		boolean excelLineOk = true;
		String areaName = "";
		Long oneAreaId = null;
		int index = 1;
		double longitude, latitude;
		int scatterAngle = 30;
		int len = 60;
		double bearing = 0;
		double[][] other;
		//chao.xj
		double[][] googleearth;
		String lngLats;
		Map<String, Long> lackBscNames = new HashMap<String, Long>();// 系统缺少的bsc名称
		Map<String, List<Long>> lackBscNameToAreaIds = new HashMap<String, List<Long>>();// 缺失的bsc与区域的对应关系
		List<Long> areaids;
		Map<Long, Long> allAreaInExcel = new HashMap<Long, Long>();// excel中出现的所有的合法区域，key为areaId，value为对应的描述id，将在后面补充
		String msg = "";
		log.info("插入中间表的sql=" + insertInoMidTableSql);
		// int total=allDatas.size();
		List<String> allCells = new ArrayList<String>();// 所有出现的小区
		String cell = "";
		List<String> indoorCellTypes=RnoConstant.CellType.getNameArray();
		String indoorType="",tch="";
		for (int i = 1; i <= total; i++) {
			super.fileParserManager.updateTokenProgress(token, i / total
					* 0.85f);
			oneData = allDatas.get(i);
			if (oneData == null || oneData.size() < colCount) {
				msg = "第[" + (i + 1) + "]行数据不齐全！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			// 判断BSC是否在系统中存在，不存在则这行数据非法
			bscName = oneData.get(excelTitlesColumn.get("BSC"));
			if (bscName == null || "".equals(bscName)) {
				msg = "第[" + (i + 1) + "]行数据的BSC为空！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			bscId = bscNameToId.get(bscName);
			if (bscId == null) {
				msg = "第[" + (i + 1) + "]行数据的BSC在系统中不存在！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			// 判断小区是否合法
			cell = oneData.get(excelTitlesColumn.get("cell"));
			if (cell == null || "".equals(cell.trim())) {
				msg = "第[" + (i + 1) + "]行数据的小区名为空！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			cell = cell.trim();
			// 判断小区是否出现过
			if (allCells.contains(cell)) {
				msg = "第[" + (i + 1) + "]行数据的小区名[" + cell + "]重复！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			allCells.add(cell);
			// 检查area是否合法
			areaName = oneData.get(excelTitlesColumn.get("地区"));
			if (areaName != null) {
				areaName = areaName.trim();
			}
			oneAreaId = areaNameToIds.get(areaName);
			if (oneAreaId == null) {
				msg = "第[" + (i + 1) + "]行所在地区[" + areaName + "]不在指定城市下！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			
			//2014-7-17 gmh 判断小区频点是否用逗号分隔
			tch=oneData.get(excelTitlesColumn.get("TCH"));
			tch=tch==null?"":tch.trim();
			if ("".equals(tch)) {
				msg = "第[" + (i + 1) + "]行tch信息为空！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			//替换空格、分号、tab键为逗号
			tch=tch.replaceAll("[' '|'&'|';'|'；'|'，'|'\t'|'\\['||'\\]']{1,}", ",");
			tch=tch.replaceAll("[',']{2,}",",");
			if(tch.endsWith(",")){
				if(tch.length()>1){
					tch=tch.substring(0, tch.length()-1);
				}else{
					tch="";
				}
			}
			if(tch.startsWith(",") ){
				if(tch.length()>1){
					tch=tch.substring(1, tch.length());
				}else{
					tch="";
				}
			}
			//有些excel格式对于数据
			if(tch.indexOf(",")<0 && tch.length()>4){//最大的单独频点为千级别，4位数
				msg = "第[" + (i + 1) + "]行tch信息格式不合法！";
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			//重新设置回去
			oneData.set(excelTitlesColumn.get("TCH"), tch);
			//---2014-3-23 判断“是否室内覆盖小区”内容是否合法---//
			indoorType = oneData.get(excelTitlesColumn.get("是否室内覆盖小区"));
			if(!indoorCellTypes.contains(indoorType)){
				msg = "第[" + (i + 1) + "]行填写的\"是否室内覆盖小区\"的内容[" + indoorType + "]无效！该列只能填写"+indoorCellTypes;
				log.error(msg);
				buf.append(msg + "<br/>");
				failCnt++;
				continue;
			}
			// 设置语句
			// 对于excel里出现的都进行设置，对于没有在excel出现的，则设置为null
			excelLineOk = true;
			for (String t : excelTitlesColumn.keySet()) {
				excelLineOk = setPreparedStatementValue(PStatement,
						excelTitlesToDbFields.get(t),
						oneData.get(excelTitlesColumn.get(t)),
						dbFieldsType.get(excelTitlesToDbFields.get(t)));
				if (excelLineOk == false) {
					failCnt++;
					msg = "第[" + (i + 1) + "]行的列：[" + t + "]处理出错！";
					log.warn(msg);
					buf.append(msg + "<br/>");
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
			// 处理额外的BSC_ID,AREA_ID,LNGLATS信息
			// 2013-12-25 continue
			if (excelLineOk == true) {
				try {
					index = seqenceDbFieldsPosition.get("BSC_ID");
					// 获取bsc的id
					bscName = oneData.get(excelTitlesColumn.get("BSC"));
					bscId = bscNameToId.get(bscName);
					if (bscId == null) {
						/*PStatement.setNull(index, Types.NUMERIC);
						if (!lackBscNames.containsKey(bscName)) {
							lackBscNames.put(bscName, null);
						}
						if (!lackBscNameToAreaIds.containsKey(bscName)) {
							lackBscNameToAreaIds.put(bscName,
									new ArrayList<Long>());
						}
						areaids = lackBscNameToAreaIds.get(bscName);
						if (!areaids.contains(oneAreaId)) {
							areaids.add(oneAreaId);
						}*/
					} else {
						//在bscId存在的情况下验证bsc和area的关系是否存在，如果不存在则添加
						validateBscRelaArea(connection, insertBscAreaStatement,rbraList, bscId, oneAreaId,buf, bscName, i);
						PStatement.setLong(index, bscId);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					msg = "第[" + (i + 1) + "]行的bsc信息有错！";
					log.error(msg);
					buf.append(msg + "<br/>");
					failCnt++;
					continue;
				}

				try {
					// 设置area_id
					index = seqenceDbFieldsPosition.get("AREA_ID");
					PStatement.setLong(index, oneAreaId);

				} catch (Exception e2) {
					e2.printStackTrace();
					msg = "第[" + (i + 1) + "]行的区域信息有错！";
					log.error(msg);
					buf.append(msg + "<br/>");
					failCnt++;
					continue;
				}

				try {
					// 设置LNGLATS
					// 转为百度坐标计算展示需要的顶点
					// 先从缓存获取
					longitude = Double.parseDouble(oneData
							.get(excelTitlesColumn.get("LON")));
					latitude = Double.parseDouble(oneData.get(excelTitlesColumn
							.get("LAT")));
					String suf = longitude + "," + latitude;
					String[] baidulnglat = super.getBaiduLnglat(longitude,
							latitude, standardPoints);
					// try {
					// // 先从缓存获取
					// baidulnglat = memCached
					// .get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
					// + suf);
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为："
					// + baidulnglat);
					// if (baidulnglat == null) {
					// if (standardPoints != null && standardPoints.size() > 0)
					// {
					// baidulnglat = rnoCommonService
					// .getLngLatCorrectValue(longitude, latitude,
					// standardPoints);
					// } else {
					// log.warn("区域不存在基准点，将使用百度在线接口进行校正。");
					// // 缓存不存在，需要计算
					// baidulnglat = CoordinateHelper
					// .changeFromGpsToBaidu(longitude + "",
					// latitude + "");
					// }
					// // 保存入缓存
					// try {
					// memCached
					// .set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
					// + suf,
					// RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
					// baidulnglat);
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// }

					if (baidulnglat != null) {
						scatterAngle = 30;
						len = 60;
						int bcch = Integer.parseInt(oneData
								.get(excelTitlesColumn.get("BCCH")));

						String gsmfrequenceSection = null;
						if (excelTitlesColumn.get("频段类型") != null) {
							gsmfrequenceSection = oneData.get(excelTitlesColumn
									.get("频段类型"));
						}

						if (bcch < 100) {
							// 900
							scatterAngle = 30;
							len = 120;
							if (gsmfrequenceSection == null
									|| "".equals(gsmfrequenceSection)) {
								gsmfrequenceSection = "GSM900";
							}
						} else {
							scatterAngle = 60;
							len = 80;
							if (gsmfrequenceSection == null
									|| "".equals(gsmfrequenceSection)) {
								gsmfrequenceSection = "GSM1800";
							}
						}
						bearing = Double.parseDouble(oneData
								.get(excelTitlesColumn.get("天线方向")));
						other = CoordinateHelper.OutputCoordinates(
								Double.valueOf(baidulnglat[0]),
								Double.valueOf(baidulnglat[1]),
								(int) (bearing * 1), scatterAngle, len);
						/*lngLats = baidulnglat[0] + "," + baidulnglat[1] + ";"
								+ other[0][0] + "," + other[0][1] + ";"
								+ other[1][0] + "," + other[1][1];*/
						//chao.xj 改造
						//lngLats
						String baidu = baidulnglat[0] + "," + baidulnglat[1] + ";"
						+ other[0][0] + "," + other[0][1] + ";"
						+ other[1][0] + "," + other[1][1];
						googleearth = CoordinateHelper.OutputCoordinates(
								longitude, latitude, (int) (bearing * 1),
								scatterAngle, len);
						String google = longitude + "," + latitude
								+ ";" + googleearth[0][0] + "," + googleearth[0][1] + ";"
								+ googleearth[1][0] + "," + googleearth[1][1];
						lngLats="{\"baidu\": \""+baidu+"\",\"googleearth\":\""+google+"\"}";
					} else {
						msg = "第[" + (i + 1) + "]行错误！数据包含经纬度坐标有误！";
						log.warn(msg);
						buf.append(msg + "<br/>");
						failCnt++;
						continue;
					}

					// 设置
					index = seqenceDbFieldsPosition.get("LNGLATS");
					PStatement.setString(index, lngLats);
				} catch (Exception e3) {
					e3.printStackTrace();
					msg = "第[" + (i + 1) + "]行的经纬度信息有错！";
					log.error(msg);
					buf.append(msg + "<br/>");
					failCnt++;
					continue;
				}

				insertSuccessCnt++;
				if (!allAreaInExcel.containsKey(oneAreaId)) {
					allAreaInExcel.put(oneAreaId, -1L);
				}
				// 添加到批处理
				try {
					PStatement.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					insertSuccessCnt--;
					failCnt++;
				}
			}
		}

		if (insertSuccessCnt > 0) {

			// 插入中间表
			try {
				PStatement.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				msg = "数据保存时出错！code=302";
				log.error(msg);
				buf.append(msg + "<br/>");
				return false;
			}
			// ------到此为止，exel数据已经导入中间表，并且具有areaid，部分bscid，不存在的bsc将要新建。

			Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						"数据处理出错！code=303");
				return false;
			}
			// 新建bsc，及与区域的关系
			if (lackBscNames.size() > 0) {
				String insertBscSql = "insert into RNO_BSC(BSC_ID,CHINESENAME,ENGNAME) VALUES(?,?,?)";
				// PreparedStatement insertBscStatement = null;
				try {
					PStatement = connection.prepareStatement(insertBscSql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "准备处理bsc的时候出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=304");
					return false;
				}
				for (String bscn : lackBscNames.keySet()) {
					bscId = super.getNextSeqValue("SEQ_RNO_BSC", connection);
					lackBscNames.put(bscn, bscId);
					try {
						PStatement.setLong(1, bscId);
						PStatement.setString(2, bscn);
						PStatement.setString(3, bscn);
						PStatement.addBatch();
					} catch (SQLException e) {
						e.printStackTrace();
						msg = "构造bsc批处理语句的时候出错！";
						log.error(msg);
						setCachedInfo(token,
								RnoConstant.TimeConstant.TokenTime,
								"数据处理出错！code=305");
						return false;
					}

				}
				// 执行插入新的bsc
				try {
					PStatement.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "执行bsc批处理语句的时候出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=306");
					return false;
				}

				// 创建与area的关系
				String insertBscToAreaSql = "insert into RNO_BSC_RELA_AREA(BSC_AREA_ID,BSC_ID,AREA_ID) VALUES(SEQ_RNO_BSC_RELA_AREA.NEXTVAL,?,?)";

				try {
					PStatement = connection
							.prepareStatement(insertBscToAreaSql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "准备创建bsc与区域的关系的时候出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=307");
					return false;
				}
				for (String bscn : lackBscNames.keySet()) {
					bscId = lackBscNames.get(bscn);
					log.info("bsc name=" + bscn + ",bscId=" + bscId);
					if (bscId == null) {
						log.error("bsc：" + bscn + "对应的Id为空！");
						// failCnt++;
						// insertSuccessCnt--;
						// buf.append("bsc："+bscn+"创建不成功！<br/>");
						continue;
					}

					for (Long areaid : lackBscNameToAreaIds.get(bscn)) {
						try {
							if (areaid == null) {
								log.error("bsc：" + bscn + "没有与任何区域对应！");
								continue;
							}
							PStatement.setLong(1, bscId);
							PStatement.setLong(2, areaid);
							PStatement.addBatch();
						} catch (SQLException e) {
							e.printStackTrace();
							msg = "构建bsc与区域的关系的批处理时候出错！";
							log.error(msg);
							setCachedInfo(token,
									RnoConstant.TimeConstant.TokenTime,
									"数据处理出错！code=308");
							return false;
						}

					}
					// 更新中间表的bsc_id情况
					try {
						statement.executeUpdate("update " + midTable
								+ " SET BSC_ID=" + bscId + " where BSC_NAME='"
								+ bscn + "'");
					} catch (SQLException e) {
						e.printStackTrace();
						msg = "更新中间表的新增的bsc_id字段的时候出错！";
						log.error(msg);
						setCachedInfo(token,
								RnoConstant.TimeConstant.TokenTime,
								"数据处理出错！code=309");
						return false;
					}
				}
				// 执行插入
				try {
					PStatement.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "执行批处理增加bsc与区域的关系的时候出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=310");
					return false;
				}
			}

			// -----到目标表的处理
			boolean isTemp = false;
			if (attachParams != null && attachParams.containsKey("isTemp")) {
				try {
					isTemp = Boolean.parseBoolean(attachParams.get("isTemp")
							+ "");
				} catch (Exception e) {
					e.printStackTrace();
					isTemp = false;
				}
			}

			// if(update==false){
			// //有特定关联关系
			// isTemp=1;
			// }

			if (isTemp == false) {
				// 系统配置
				// 更新desc id
				String sql = "merge into  "
						+ midTable
						+ " mid using RNO_CELL_DESCRIPTOR descp on (mid.AREA_ID=descp.AREA_ID AND descp.DEFAULT_DESCRIPTOR='Y' AND descp.STATUS='N') when matched then update set mid.CELL_DESC_ID=descp.CELL_DESCRIPTOR_ID";
				log.info("执行用系统descid更新中间表的cell_desc_id。sql=" + sql);
				try {
					statement.execute(sql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "执行用系统descid更新中间表的cell_desc_id的时候出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=311");
					return false;
				}

				// 获取中间表的cell_desc_id为空的
				sql = "select distinct(AREA_ID) from " + midTable
						+ " where nvl(CELL_DESC_ID,0)=0";
				ResultSet rs = null;
				try {
					rs = statement.executeQuery(sql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "执行获取中间表的cell_desc_id为空的数据时出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=312");
					return false;
				}
				allAreaInExcel.clear();// -----清空
				try {
					while (rs.next()) {
						oneAreaId = rs.getLong("AREA_ID");
						allAreaInExcel.put(oneAreaId, -1L);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "处理从中间表获取的cell_desc_id为空的数据时出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=313");
					return false;
				}
				log.info("获取到的没有系统描述信息的area id情况：" + allAreaInExcel.keySet());
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// 页面传递的配置名称
			String tempName = "";
			if (isTemp == true) {
				tempName = attachParams.get("tempname") == null ? "未命名的配置表"
						: attachParams.get("tempname").toString();
			} else {
				tempName = "系统默认配置方案";
			}
			if ("".equals(tempName)) {
				tempName = "未命名的配置表";
			}
			// 创建描述id
			String insertCellDescSql = "insert into RNO_CELL_DESCRIPTOR(CELL_DESCRIPTOR_ID,NAME,TEMP_STORAGE,DEFAULT_DESCRIPTOR,CREATE_TIME,MOD_TIME,STATUS,AREA_ID) VALUES("
					+ "?,'"
					+ tempName
					+ "','"
					+ (isTemp == true ? "Y" : "N")
					+ "','"
					+ (isTemp == true ? "N" : "Y")
					+ "',sysdate,sysdate,'N',?)";

			log.info("创建描述信息的sql:" + insertCellDescSql);
			// ---准备补充系统配置信息
			if (allAreaInExcel.size() > 0) {
				log.info("准备补充新的描述信息。");
				try {
					PStatement = connection.prepareStatement(insertCellDescSql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "准备插入新的描述信息的statement时出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=314");
					return false;
				}
				for (Long oneareaid : allAreaInExcel.keySet()) {
					long descid = super.getNextSeqValue(
							"SEQ_RNO_CELL_DESCRIPTOR", connection);
					allAreaInExcel.put(oneareaid, descid);
					try {
						log.info("area=" + oneareaid + ",对应descid=" + descid);
						PStatement.setLong(1, descid);
						PStatement.setLong(2, oneareaid);
						PStatement.addBatch();
					} catch (SQLException e) {
						e.printStackTrace();
						msg = "增加插入新的描述信息的批处理语句时出错！";
						log.error(msg);
						setCachedInfo(token,
								RnoConstant.TimeConstant.TokenTime,
								"数据处理出错！code=315");
						return false;
					}

					// 更新描述id
					try {
						String s = "update " + midTable + " set CELL_DESC_ID="
								+ descid + " where AREA_ID=" + oneareaid;
						log.info("更新中间表的的cell des id的sql：" + s);
						statement.executeUpdate(s);
					} catch (SQLException e) {
						e.printStackTrace();
						msg = "更新中间表的描述id时出错！";
						log.error(msg);
						setCachedInfo(token,
								RnoConstant.TimeConstant.TokenTime,
								"数据处理出错！code=316");
						return false;
					}
				}
				try {
					PStatement.executeBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "执行批处理增加描述信息时出错！";
					log.error(msg);
					setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
							"数据处理出错！code=317");
					return false;
				}
			}

			//对于此次导入没有的区域id，将其描述信息删除——
			List<Long> subAreas = AuthDsDataDaoImpl
					.getSubAreaIdsByCityId(cityId);
			String areaStrs = cityId+",";
			for (Long id : subAreas) {
				areaStrs += id + ",";
			}
			if (areaStrs.length() > 0) {
				areaStrs = areaStrs.substring(0, areaStrs.length()-1);
			}else{
				areaStrs=cityId+"";
			}
			
			// ---到此为止，中间表的cell_desc_id都不为空，bsc_id都不为空，可以开始处理转移到目标表的动作了
			
			//——认为每一次都是全量全新导入  2014-9-5 先把整个市的数据删除。
			String dSql="delete from cell where area_id in ( "+areaStrs+" )";
			try {
				statement.executeUpdate(dSql);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			dSql="delete from rno_cell_descriptor where area_id in ( "+areaStrs+" ) and area_id not in (select area_id from rno_mid_cell)";
			try {
				statement.executeUpdate(dSql);
			}catch(Exception e){
				e.printStackTrace();
				log.error("删除旧的desc的sql="+dSql);
			}
			
			
			String targetTable = "CELL";
			String targetSeq = "SEQ_CELL";
			if (isTemp == true) {
				// 到临时分析表
				log.info("到临时方案表。");
				targetTable = "RNO_TEMP_CELL";
				targetSeq = "SEQ_RNO_TEMP_CELL";
			}

			String sql = "merge into "
					+ targetTable
					+ " targ using "
					+ midTable
					+ " mid on (targ.CELL_DESC_ID=mid.CELL_DESC_ID AND targ.AREA_ID=mid.AREA_ID AND targ.LABEL=mid.LABEL) ";
			Set<String> fieldsOnlyExistsInTarget = new HashSet(dbFields);
			fieldsOnlyExistsInTarget.removeAll(fieldsNotExistInTargetTab);
			Set<String> forUpdateFields = new HashSet(fieldsOnlyExistsInTarget);
			forUpdateFields.remove("LABEL");
			if (update) {
				log.info("对于重复数据，需用当前excel的数据更新系统的数据");
				sql += " when matched then update set ";
				for (String f : forUpdateFields) {
					sql += "targ." + f + "=mid." + f + ",";
				}
				// BSC_ID,LNGLATS
				sql += "targ.BSC_ID=mid.BSC_ID,targ.LNGLATS=mid.LNGLATS";

				log.info("用中间表[" + midTable + "]的数据更新目标表[" + targetTable
						+ "]的数据的sql 部分为：" + sql);
			}
			// 对于不重复数据，则插入

			String fs = "";
			String vs = "";
			for (String f : fieldsOnlyExistsInTarget) {
				fs += f + ",";
				vs += "mid." + f + ",";
			}
			fs += "BSC_ID,AREA_ID,LNGLATS,CELL_DESC_ID";
			vs += "mid.BSC_ID,mid.AREA_ID,mid.LNGLATS,mid.CELL_DESC_ID";
			sql += " when not matched then insert (ID," + fs + ") values("
					+ targetSeq + ".NEXTVAL," + vs + ")";

			log.info("总的用中间表[" + midTable + "]对目标表[" + targetTable
					+ "]更新、插入语句为：" + sql);
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
				msg = "执行用中间表[" + midTable + "]的数据更新目标表[" + targetTable
						+ "]的时候出错！sql=" + sql;
				log.error(msg);
				setCachedInfo(token, RnoConstant.TimeConstant.TokenTime,
						"数据处理出错！code=318");
				return false;
			}

			// ====================持久化完成＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

			allDatas = null;
			if (autoload) {
				// 获取所有的描述信息
				sql = "SELECT DESCP2.*,'city' AS AREA_NAME  FROM (select descp.CELL_DESCRIPTOR_ID,descp.NAME,descp.TEMP_STORAGE,descp.DEFAULT_DESCRIPTOR,descp.CREATE_TIME,descp.AREA_ID FROM RNO_CELL_DESCRIPTOR descp WHERE descp.CELL_DESCRIPTOR_ID IN "
						+ " (Select DISTINCT(CELL_DESC_ID) FROM "
						+ midTable
						+ ") and descp.area_id in ("+areaStrs+" )) DESCP2";
				log.info("获取涉及到的描述信息的sql:" + sql);
				ResultSet rs = null;
				boolean ok = true;
				try {
					rs = statement.executeQuery(sql);
				} catch (SQLException e) {
					e.printStackTrace();
					msg = "获取中间表涉及的描述id的信息时出错！";
					log.error(msg);
					ok = false;
				}
				if (ok) {
					PlanConfig planConfig = null;
					List<PlanConfig> wantpcs = new ArrayList<PlanConfig>();
					try {
						while (rs.next()) {
							long descId = rs.getLong("CELL_DESCRIPTOR_ID");
							String descName = rs.getString("NAME");
							String tmpS = rs.getString("TEMP_STORAGE");
							log.info("tmpS:"+tmpS);
							String isdef = rs.getString("DEFAULT_DESCRIPTOR");
							Date cd = rs.getTimestamp("CREATE_TIME");
							log.info("CREATE_TIME:"+cd);
							long arid = rs.getLong("AREA_ID");
							String arname = rs.getString("AREA_NAME");
							String collectTime = df.format(cd);
							log.info("collectTime:"+collectTime);
							planConfig = new PlanConfig();
							planConfig.setSelected(false);
							planConfig.setType("CELLDATA");
//							planConfig.setTemp(isTemp == true ? true : false);
							planConfig.setTemp(tmpS.equals("Y") ? true : false);
							planConfig.setConfigId(descId);
							planConfig.setCollectTime(collectTime);
							planConfig.setName(descName);
							// String title=area.getName()+"GSM小区";
							//String title = arname + collectTime;
							String title = arname;
							planConfig.setTitle(title);
							planConfig.setAreaName(arname);

							wantpcs.add(planConfig);

						}
					} catch (SQLException e) {
						e.printStackTrace();
					}

					addCellConfigToAnalysisList(attachParams, wantpcs);
				}
			}
			if (PStatement != null) {
				try {
					PStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(null != insertBscAreaStatement)
			{
				try
				{
					insertBscAreaStatement.close();
				}catch(SQLException e)
				{
					log.info(e.getStackTrace());
				}
			}
		} else {
			// 没有一条数据合法的
			log.warn("excel里数据都不合法！");
		}

		String result = "";

		long endTime = new Date().getTime();
		// 缓存导入结果
		result = "导入完成。耗时:" + (endTime - begTime) / 1000 + "秒。共 [" + total
				+ "] 条记录，成功处理 [" + insertSuccessCnt + "] 条记录，出错 [" + failCnt
				+ "] 条记录。<br/>";

		if (buf.length() > 0) {
			result += "详情：<br/>" + buf.toString();
		}
		log.info("gsm小区导入的结果：" + result);
		try {
			memCached.set(token, RnoConstant.TimeConstant.TokenTime, result
					);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 清空缓存
		log.info("清除市级下小区的缓存key："
				+ RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE + areaId);
		for (long id : areaNameToIds.values()) {
			try {
				memCached
						.delete(RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
								+ id);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void addCellConfigToAnalysisList(Map<String, Object> attachParams,
			List<PlanConfig> pcs) {

		if (pcs == null || pcs.isEmpty()) {
			return;
		}

		HttpSession session = (HttpSession) attachParams.get("session");
		List<PlanConfig> lists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);

		if (lists == null) {
			lists = new ArrayList<PlanConfig>();
			session.setAttribute(
					RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, lists);
		}

		for (PlanConfig pc : pcs) {
			if (!lists.contains(pc)) {
				lists.add(pc);
			}
		}
	}

	/**
	 * 判断数据有效性，如果数据有效，返回创建的小区；否则返回null
	 * 
	 * @param oneData
	 * @param i
	 * @param buf
	 * @return Sep 11, 2013 3:43:54 PM gmh
	 */
	private Cell createCellFromExcelLine(String country, List<String> oneData,
			int i, StringBuilder buf,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		// 字段数据
		String areaName = null;
		String bscName = null;
		Long bscId = null;
		String label = null;
		String name = null;
		Long lac = null;
		Long ci = null;
		Long bcch = null;
		Long bsic = null;
		String tch = null;
		Long bearing = null;
		Long downtilt = null;
		String btsType = null;
		Long antHeigh = null;
		String antType = null;
		Double longitude = null;
		Double latitude = null;
		String coverarea = null;
		String lngLats = null;
		String gsmfrequenceSection = null;

		String msg = "";
		// --------开始 检查数据有效性-----------------------------------------------//
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

		int index = 0;
		// 区域
		areaName = oneData.get(index++);
		if (!country.equals(areaName)) {
			msg = "第[" + (i + 1) + "]行错误！小区所在的不在指定的导入区域！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// 检查数据有效性
		bscName = oneData.get(index++);
		if (bscName == null || bscName.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含bsc！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// label
		label = oneData.get(index++);
		if (label == null || label.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含小区名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// name
		name = oneData.get(index++);
		if (name == null || name.trim().isEmpty()) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含小区中文名！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// "LAC", "CI", "ARFCN", "BSIC", "NON_BCCH", "天线方向",
		// lac
		try {
			lac = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的lac！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// ci
		try {
			ci = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的ci！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// bcch
		try {
			bcch = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的bcch！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// bsic
		try {
			bsic = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的bsic！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// tch不检查
		tch = oneData.get(index++);
		// 天线方向
		try {
			bearing = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据包含错误的天线方向！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		// "天线下倾", "基站类型", "天线高度", "天线类型", "LON", "LAT", "覆盖范围"
		try {
			downtilt = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			downtilt = 0l;
		}
		btsType = oneData.get(index++);
		try {
			antHeigh = Long.parseLong(oneData.get(index++));
		} catch (Exception e) {
			antHeigh = 0l;
		}
		antType = oneData.get(index++);

		try {
			longitude = Double.parseDouble(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含经度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}

		try {
			latitude = Double.parseDouble(oneData.get(index++));
		} catch (Exception e) {
			msg = "第[" + (i + 1) + "]行错误！数据未包含纬度！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		coverarea = oneData.get(index++);
		// --------结束 检查数据有效性-----------------------------------------------//

		// 计算其余的顶点坐标
		// if (bearing % 360 != 0) {
		int scatterAngle = 30;
		int len = 60;
		if (bcch < 100) {
			// 900
			scatterAngle = 30;
			len = 120;
			gsmfrequenceSection = "GSM900";
		} else {
			scatterAngle = 60;
			len = 80;
			gsmfrequenceSection = "GSM1800";
		}

		// 转为百度坐标计算展示需要的顶点
		// 先从缓存获取
		String suf = longitude + "," + latitude;
		String[] baidulnglat = null;
		try {
			// 先从缓存获取
			baidulnglat = memCached
					.get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE + suf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为：" + baidulnglat);
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = rnoCommonService.getLngLatCorrectValue(longitude,
						latitude, standardPoints);
			} else {
				log.warn("区域不存在基准点，将使用百度在线接口进行校正。");
				// 缓存不存在，需要计算
				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
			// 保存入缓存
			try {
				memCached.set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
						+ suf, RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
						baidulnglat);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
		}
		if (baidulnglat != null) {
			double[][] other = CoordinateHelper.OutputCoordinates(
					Double.valueOf(baidulnglat[0]),
					Double.valueOf(baidulnglat[1]), (int) (bearing * 1),
					scatterAngle, len);
			/*lngLats = baidulnglat[0] + "," + baidulnglat[1] + ";" + other[0][0]
					+ "," + other[0][1] + ";" + other[1][0] + "," + other[1][1];*/
			//chao.xj 改造
			String baidu = baidulnglat[0] + "," + baidulnglat[1] + ";"
			+ other[0][0] + "," + other[0][1] + ";"
			+ other[1][0] + "," + other[1][1];
			double[][] googleearth = CoordinateHelper.OutputCoordinates(
					longitude, latitude, (int) (bearing * 1),
					scatterAngle, len);
			String google = longitude + "," + latitude
					+ ";" + googleearth[0][0] + "," + googleearth[0][1] + ";"
					+ googleearth[1][0] + "," + googleearth[1][1];
			lngLats="{\"baidu\": \""+baidu+"\",\"googleearth\":\""+google+"\"}";
		} else {
			msg = "第[" + (i + 1) + "]行错误！数据包含经纬度坐标有误！";
			log.warn(msg);
			buf.append(msg + "<br/>");
			return null;
		}
		// }

		Cell oneCell = new Cell();
		oneCell.setBscId(bscId);
		oneCell.setLabel(label);
		oneCell.setName(name);
		oneCell.setLac(lac);
		oneCell.setCi(ci);
		oneCell.setBcch(bcch);
		oneCell.setBsic(bsic);
		oneCell.setTch(tch);
		oneCell.setBearing(bearing);
		oneCell.setDowntilt(downtilt);
		oneCell.setBtstype(btsType);
		oneCell.setAntHeigh(antHeigh);
		oneCell.setAntType(antType);
		oneCell.setLongitude(longitude);
		oneCell.setLatitude(latitude);
		oneCell.setCoverarea(coverarea);
		oneCell.setLngLats(lngLats);
		oneCell.setGsmfrequencesection(gsmfrequenceSection);

		return oneCell;
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
	private boolean validateBscRelaArea(Connection conn, PreparedStatement insertBscAreaStatement,List<RnoBscRelaArea> rbraList, Long bscId, long areaId,StringBuilder buf, String bsc, int i)
	{
		boolean flag = true;
		for(RnoBscRelaArea rbra : rbraList)
		{
			if(bscId.equals(rbra.getBscId()))
			{
				return flag;
			}
		}
		if(flag)
		{
			try
			{
				insertBscAreaStatement.setLong(1, bscId);
				insertBscAreaStatement.setLong(2, areaId);
				int cnt = insertBscAreaStatement.executeUpdate();
				log.info("更新了"+bsc+"和"+areaId);
				RnoBscRelaArea rbra = new RnoBscRelaArea();
				rbra.setBscId(bscId);
				rbra.setAreaId(areaId);
				rbraList.add(rbra);
				return flag;
			}catch (SQLException e)
			{
				String msg = "插入第[" + (i+1) + "]行的BSC出错,系统中没有该BSC与AREA关系";
				log.error(msg);
				buf.append(msg + "<br/>");
				log.info(e.getStackTrace());
				return !flag;
			}
		}
		return !flag;
	}

}
