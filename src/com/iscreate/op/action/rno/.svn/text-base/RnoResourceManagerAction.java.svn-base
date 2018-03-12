package com.iscreate.op.action.rno;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.G4HoDescQueryCond;
import com.iscreate.op.action.rno.model.G4KpiDescQueryCond;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.action.rno.model.Hw2GMrrDescQueryCond;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoResourceManagerHbaseService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.RnoTrafficStaticsService;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoResourceManagerAction extends RnoCommonAction {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoResourceManagerAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private DateUtil dateUtil = new DateUtil();

	private long areaId;
	private List zoneProvinceLists;// 省份LIST
	private List zoneCountyLists;// 区/县LIST

	// -------------小区展现---------------//
	private int zoom;// 缩放级别
	private Point topLeft;// 左上角
	private Point bottomRight;// 右下角
	//private Point centerPoint;// 中心点
	private String label;// 小区名

	// --------------小区查询 部分-------//
	private List<Area> zoneAreaLists;// 用户可见区/县的列表
	private CellCondition queryCell;// 小区查询条件
	private Page page;// 分页类
	private List<String> coverareas;//覆盖区域
	private List<String> covertypes;//覆盖类型
	private List<String> importancegrade;//重要等级

	// -------------小区修改----------------//
	private long cellId;
	private Cell cell;
	private double longitudeunchange;
	private double latitudeunchange;
	// ------邻区管理----//
	private String ncellIds;// 待删除的邻区关系的id

	// -------注入-------------//
	private RnoResourceManagerService rnoResourceManagerService;
	private RnoTrafficStaticsService rnoTrafficStaticsService;
	private RnoResourceManagerHbaseService rnoResourceManagerHbaseService;
	// ----------文件上传--------------//
	private File upload;// 封装文件域的属性
	private String uploadContentType;
	private String uploadFileName;
	private String savePath;

	// 话务导入
//	private List<Area> provinceAreas;
//	private List<Area> cityAreas;
//	private List<Area> countryAreas;
	//cobsic整改页面变量
	private boolean reSelected;
	private String areaIds;
	private String configIds;
	
	//频点类型
	private String freqType;
	
	//地图网格
	private Map<String, String> mapGrid;
	private String cells;
	private long cityId;
	private Map<String, String> cellParam;
	private String cellType;
	
	//BSC管理
	private Map<String, String> bscQuery;
	private String bscEngName;
	private String manufacturers;
	
	//----------资源查询的条件--------------//
	//--爱立信2g ncs 描述信息
	private Eri2GNcsDescQueryCond eri2GNcsDescQueryCond;
	//--华为2g mrr 描述信息
	private Hw2GMrrDescQueryCond hw2gMrrDescQueryCond;
	//--4g Mr 描述信息
	private G4MrDescQueryCond g4MrDescQueryCond;
	//--4g 	Ho 描述信息
	private G4HoDescQueryCond g4HoDescQueryCond;
	//--4g kpi 描述信息
	private G4KpiDescQueryCond g4KpiDescQueryCond;
	// -----------------get set -------------------------------//

	public G4KpiDescQueryCond getG4KpiDescQueryCond() {
		return g4KpiDescQueryCond;
	}

	public void setG4KpiDescQueryCond(G4KpiDescQueryCond g4KpiDescQueryCond) {
		this.g4KpiDescQueryCond = g4KpiDescQueryCond;
	}

	public long getAreaId() {
		return areaId;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public RnoResourceManagerService getRnoResourceManagerService() {
		return rnoResourceManagerService;
	}

	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}

	public List<Area> getZoneAreaLists() {
		return zoneAreaLists;
	}

	public void setZoneAreaLists(List<Area> zoneAreaLists) {
		this.zoneAreaLists = zoneAreaLists;
	}

	public CellCondition getQueryCell() {
		return queryCell;
	}

	public void setQueryCell(CellCondition queryCell) {
		this.queryCell = queryCell;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getNcellIds() {
		return ncellIds;
	}

	public void setNcellIds(String ncellIds) {
		this.ncellIds = ncellIds;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}

	public long getCellId() {
		return cellId;
	}

	public void setCellId(long cellId) {
		this.cellId = cellId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getSavePath() {
		return ServletActionContext.getRequest().getRealPath(savePath);
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public double getLongitudeunchange() {
		return longitudeunchange;
	}

	public void setLongitudeunchange(double longitudeunchange) {
		this.longitudeunchange = longitudeunchange;
	}

	public double getLatitudeunchange() {
		return latitudeunchange;
	}

	public void setLatitudeunchange(double latitudeunchange) {
		this.latitudeunchange = latitudeunchange;
	}

	public List getZoneProvinceLists() {
		return zoneProvinceLists;
	}

	public void setZoneProvinceLists(List zoneProvinceLists) {
		this.zoneProvinceLists = zoneProvinceLists;
	}

	public List getZoneCountyLists() {
		return zoneCountyLists;
	}

	public void setZoneCountyLists(List zoneCountyLists) {
		this.zoneCountyLists = zoneCountyLists;
	}

	public RnoTrafficStaticsService getRnoTrafficStaticsService() {
		return rnoTrafficStaticsService;
	}

	public void setRnoTrafficStaticsService(
			RnoTrafficStaticsService rnoTrafficStaticsService) {
		this.rnoTrafficStaticsService = rnoTrafficStaticsService;
	}

	

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	public List<String> getCoverareas() {
		return coverareas;
	}

	public void setCoverareas(List<String> coverareas) {
		this.coverareas = coverareas;
	}

	public List<String> getCovertypes() {
		return covertypes;
	}

	public void setCovertypes(List<String> covertypes) {
		this.covertypes = covertypes;
	}

	public List<String> getImportancegrade() {
		return importancegrade;
	}

	public void setImportancegrade(List<String> importancegrade) {
		this.importancegrade = importancegrade;
	}
	public boolean isReSelected() {
		return reSelected;
	}

	public void setReSelected(boolean reSelected) {
		this.reSelected = reSelected;
	}

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public String getConfigIds() {
		return configIds;
	}

	public void setConfigIds(String configIds) {
		this.configIds = configIds;
	}
	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}
	
	public Map<String, String> getMapGrid() {
		return mapGrid;
	}

	public void setMapGrid(Map<String, String> mapGrid) {
		this.mapGrid = mapGrid;
	}

	public String getCells() {
		return cells;
	}

	public void setCells(String cells) {
		this.cells = cells;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public Map<String, String> getCellParam() {
		return cellParam;
	}

	public void setCellParam(Map<String, String> cellParam) {
		this.cellParam = cellParam;
	}

	public Map<String, String> getBscQuery() {
		return bscQuery;
	}

	public void setBscQuery(Map<String, String> bscQuery) {
		this.bscQuery = bscQuery;
	}

	public String getBscEngName() {
		return bscEngName;
	}

	public void setBscEngName(String bscEngName) {
		this.bscEngName = bscEngName;
	}

	public String getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(String manufacturers) {
		this.manufacturers = manufacturers;
	}
	public RnoResourceManagerHbaseService getRnoResourceManagerHbaseService() {
		return rnoResourceManagerHbaseService;
	}

	public void setRnoResourceManagerHbaseService(
			RnoResourceManagerHbaseService rnoResourceManagerHbaseService) {
		this.rnoResourceManagerHbaseService = rnoResourceManagerHbaseService;
	}
	/**
	 * 进入rno所有页面链接列表页面
	 * @return
	 * @author chao.xj
	 * @date 2013-11-21上午09:32:52
	 */
	public String gotoPageListsAction() {
		return "success";
	}
	/**
	 * 初始化小区管理页面所需信息
	 * 
	 * @return Sep 13, 2013 3:05:02 PM gmh
	 */
	public String initCellManagerPageAction() {
		initAreaList();
//		coverareas;//覆盖区域
//		covertypes;//覆盖类型
//		importancegrade;//重要等级
//		coverareas=rnoResourceManagerService.getAllCoverareas();
//		covertypes=rnoResourceManagerService.getAllCovertypes();
//		importancegrade=rnoResourceManagerService.getAllImportancegrades();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}

	/**
	 * 初始化邻区管理所需信息
	 * 
	 * @return Sep 14, 2013 12:05:51 PM gmh
	 */
	public String initNCellManagerPageAction() {
		initAreaList();
		return "success";
	}
	
	/**
	 * 初始化地图展示
	 * 
	 * @return Sep 18, 2013 10:04:49 AM gmh
	 */
	public String initGisCellDisplayAction() {
		initAreaList();

		return "success";
	}

	/**
	 * 
	 * 获取用户的 区/县 区域 Sep 16, 2013 10:02:15 AM gmh
	 */
	private void initUserZoneArea() {
		// 获取用户所在区域信息
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		zoneAreaLists = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "区/县");

	}

	/**
	 * 
	 * 获取用户的 县/区 区域
	 * 
	 * @author chao.xj 2013-10-15下午02:30:32
	 */
	private void initUserCountyArea() {
		// 获取用户所在区域信息
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		zoneCountyLists = this.rnoTrafficStaticsService
				.getCountysInSpecLevelListByUserId(loginPerson, "区/县");

	}

	/**
	 * 
	 * 获取用户的 省 区域
	 * 
	 * @author chao.xj 2013-10-15上午09:59:13
	 */
	private void initUserProvincesArea() {
		// 获取用户所在区域信息
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		zoneProvinceLists = this.rnoTrafficStaticsService
				.getProvincesInSpecLevelListByUserId(loginPerson, "省");

	}

	/**
	 * 获取指定区域下的bsc列表
	 * 
	 * Sep 16, 2013 11:15:14 AM gmh
	 */
	public void getBscsResideInAreaForAjaxAction() {
		log.info("getBscsResideInAreaForAjaxAction。areaId=" + areaId);
		List<RnoBsc> bscs = rnoResourceManagerService
				.getBscsResideInAreaByAreaId(areaId);
		String result = gson.toJson(bscs);
		// System.out.println("getBscsResideInAreaForAjaxAction
		// result==="+result);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("getBscsResideInAreaForAjaxAction！");
		}

	}

	/**
	 * 分页查询小区
	 * 
	 * Sep 16, 2013 10:04:05 AM gmh
	 */
	public void queryCellByPageForAjaxAction() {
		// page,queryCell

		log.info("进入方法：queryCellByPageForAjaxAction。 page=" + page
				+ ",queryCell=" + queryCell);

		// 最小为1
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		// 获取用户能看到的所有区域集合
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "区/县");

		List<Long> allAllowAreaIds = new ArrayList<Long>();
		if (list != null && !list.isEmpty()) {
			long id;
			for (Area one : list) {
				id = one.getArea_id();
				allAllowAreaIds.add(id);
			}
		}

		List<Cell> cells = rnoResourceManagerService.queryCellByPage(page,
				queryCell, allAllowAreaIds);
		int totalCnt =page.getTotalCnt();
		if(page.getCurrentPage()==1 || totalCnt<0){
			totalCnt=rnoResourceManagerService.getTotalQueryCellCnt(
					queryCell.buildSqlWhere(allAllowAreaIds), false);
		}

		// System.out.println("获取过后：queryCellByPageForAjaxAction。
		// page="+page+",queryCell="+queryCell);

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));

		String cellJson = gson.toJson(cells);
		// System.out.println("cell json="+cellJson);

		String pageJson = gson.toJson(newPage);
		// System.out.println("page Json ="+pageJson);
		String result = "{'page':" + pageJson + ",'celllist':" + cellJson + "}";

		log.info("退出方法：queryCellByPageForAjaxAction。 返回：" + result);
		// System.out.println("退出方法：queryCellByPageForAjaxAction。 返回：" +
		// result);
		writeToClient(result);

	}

//	private void writeToClient(String result) {
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setContentType("text/plain");
//		response.setCharacterEncoding("utf-8");
//
//		try {
//			response.getWriter().write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error("writeToClient出错 ！");
//		}
//	}

	/**
	 * 
	 * 获取指定区域的小区 Sep 17, 2013 11:27:43 AM gmh
	 */
	public void getGisCellByPageForAjaxAction() {
		// 缩放级别：zoom
		// 区域id:areaId
		// 分页参数：page
		// 范围经纬度:topLeft,bottomRight
		long cityId = getCityId();
		log.info("进入方法：getGisCellByPageForAjaxAction。 zoom=" + zoom
				+"cityId: "+cityId+ ",areaId=" + areaId + ",page=" + page);
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, -1==areaId?"市":"区/县");
		Area area = null;
		log.info("用户" + loginPerson + " 允许查看的区域： " + list);
		// 判断是否在用户允许查看的区域范围内
		boolean ok = false;
		if (list != null) {
			//Liang YJ 修改，小区查找增加全市小区查找
			if(-1 == areaId)
			{
				for(Area a : list)
				{
					if (a.getArea_id() == cityId) {
						ok = true;
						area = a;
						break;
					}
				}
			}else{
				for (Area a : list) {
					if (a.getArea_id() == areaId) {
						ok = true;
						area = a;
						break;
					}
				}
			}
		}
		GisCellQueryResult gisCells = null;
		// 暂时支持这个区域
		int totalCnt = 0;
		//log.info("areaLevel: "+area.getArea_level());
		if (ok && area != null && (area.getArea_level().equals("区/县") || area.getArea_level().equals("市"))) {
			if (freqType==null) {
				gisCells = rnoResourceManagerService.getGisCellByPage(area.getArea_id(), page);
			}else {
				gisCells = rnoResourceManagerService.getGisCellByPage(area.getArea_id(), page, freqType);
			}
			totalCnt = gisCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			gisCells = new GisCellQueryResult();
			gisCells.setGisCells(null);
			totalCnt = 0;
		}

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		gisCells.setPage(newPage);

		// String cellJson=gson.toJson(gisCells.getGisCells());
		// //System.out.println("cell json="+cellJson);
		//
		// String pageJson=gson.toJson(newPage);
		// //System.out.println("page Json ="+pageJson);
		// String result="{'page':"+pageJson+",'celllist':"+cellJson+"}";

		String result = gson.toJson(gisCells);
		log.info("离开方法：getGisCellByPageForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + gisCells.getPage());
		// System.out.println("离开方法：getGisCellByPageForAjaxAction。 输出："+result);

		writeToClient(result);
	}
	
	/**
	 * 通过地图网格方式获取小区
	 * @author peng.jm
	 * @date 2014-9-17下午05:54:16
	 */
	public void getGisCellByMapGridForAjaxAction() {
		long cityId = getCityId();
		log.info("进入方法：getGisCellByMapGridForAjaxAction。 freqType=" + freqType
				+",cityId="+cityId+ ",areaId=" + areaId + ",mapGrid=" + mapGrid);
		
		page.setPageSize(Integer.parseInt(mapGrid.get("pageSize").toString()));
		page.setCurrentPage(Integer.parseInt(mapGrid.get("currentPage").toString()));
		page.setTotalPageCnt(Integer.parseInt(mapGrid.get("totalPageCnt").toString()));
		page.setTotalCnt(Integer.parseInt(mapGrid.get("totalCnt").toString()));
		//System.out.println("grid的page=" + page);
		
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, -1==areaId?"市":"区/县");
		Area area = null;
		log.info("用户" + loginPerson + " 允许查看的区域： " + list);
		// 判断是否在用户允许查看的区域范围内
		boolean ok = false;
		if (list != null) {
			//Liang YJ 修改，小区查找增加全市小区查找
			if(-1 == areaId)
			{
				for(Area a : list)
				{
					if (a.getArea_id() == cityId) {
						ok = true;
						area = a;
						break;
					}
				}
			}else{
				for (Area a : list) {
					if (a.getArea_id() == areaId) {
						ok = true;
						area = a;
						break;
					}
				}
			}
		}
		GisCellQueryResult gisCells = null;

		int totalCnt = 0;

		if (ok && area != null && (area.getArea_level().equals("区/县") || area.getArea_level().equals("市"))) {
			if (freqType==null) {
				gisCells = rnoResourceManagerService.getGisCellByMapGrid(area.getArea_id(), mapGrid, page);
			}else {
				gisCells = rnoResourceManagerService.getGisCellByMapGrid(area.getArea_id(), mapGrid, page, freqType);
			}
			totalCnt = gisCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			gisCells = new GisCellQueryResult();
			gisCells.setGisCells(null);
			totalCnt = 0;
		}

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		gisCells.setPage(newPage);
		
		String result = gson.toJson(gisCells);
		log.info("离开方法：getGisCellByMapGridForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + gisCells.getPage());
		
		writeToClient(result);
	}
	
	/**
	 * 通过地图网格方式获取lte小区
	 * @author peng.jm
	 * @date 2015年4月14日12:06:05
	 */
	public void getLteCellByMapGridForAjaxAction() {
		long cityId = getCityId();
		log.info("进入方法：getLteCellByMapGridForAjaxAction。 freqType=" + freqType
				+",cityId="+cityId+ ",areaId=" + areaId + ",mapGrid=" + mapGrid);
		
		page.setPageSize(Integer.parseInt(mapGrid.get("pageSize").toString()));
		page.setCurrentPage(Integer.parseInt(mapGrid.get("currentPage").toString()));
		page.setTotalPageCnt(Integer.parseInt(mapGrid.get("totalPageCnt").toString()));
		page.setTotalCnt(Integer.parseInt(mapGrid.get("totalCnt").toString()));
		//System.out.println("grid的page=" + page);
		
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

//		List<Area> list = this.rnoResourceManagerService
//				.gisfindAreaInSpecLevelListByUserId(loginPerson, -1==areaId?"市":"区/县");
		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "市");
		
		Area area = null;
		log.info("用户" + loginPerson + " 允许查看的区域： " + list);
		// 判断是否在用户允许查看的区域范围内
		boolean ok = false;
		if (list != null) {
//			if(-1 == areaId)
//			{
				for(Area a : list)
				{
					if (a.getArea_id() == cityId) {
						ok = true;
						area = a;
						break;
					}
				}
//			}else{
//				for (Area a : list) {
//					if (a.getArea_id() == areaId) {
//						ok = true;
//						area = a;
//						break;
//					}
//				}
//			}
		}
		LteCellQueryResult lteCells = null;

		int totalCnt = 0;

		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = rnoCommonService
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,
						RnoConstant.DBConstant.MAPTYPE_BAIDU);
		
		if (ok && area != null && (area.getArea_level().equals("区/县") || area.getArea_level().equals("市"))) {
			if (freqType==null) {
				lteCells = rnoResourceManagerService.getLteCellByMapGrid(
						area.getArea_id(), mapGrid, page, "",standardPoints);
			}else {
				lteCells = rnoResourceManagerService.getLteCellByMapGrid(
						area.getArea_id(), mapGrid, page, freqType,standardPoints);
			}
			totalCnt = lteCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			lteCells = new LteCellQueryResult();
			lteCells.setLteCells(null);
			totalCnt = 0;
		}

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		lteCells.setPage(newPage);
		
		String result = gson.toJson(lteCells);
		log.info("离开方法：getLteCellByMapGridForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + lteCells.getPage());
		
		writeToClient(result);
	}
	
	/**
	 * 通过labels获取cell进行小区数据预加载
	 * 
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public void getRelaCellByLabelsAndCityIdForAjaxAction() {
		log.info("进入方法：getRelaCellByLabelsForAjaxAction。 cells=" + cells
				+ ",cityId=" + cityId);
		GisCellQueryResult gisCells = rnoResourceManagerService.getRelaCellByLabels(cells,cityId);
		String result = gson.toJson(gisCells);
		log.info("离开方法：getRelaCellByLabelsForAjaxAction。");
		writeToClient(result);
	}
	
	/**
	 * 通过小区参数获取cell进行小区数据预加载
	 * 
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public void getRelaCellByCellParamAndAreaIdForAjaxAction() {
		log.info("进入getRelaCellByCellParamAndCityIdForAjaxAction方法,cellParam="
				+ cellParam + ",cityId=" + cityId + ",areaId=" + areaId);
		
		if(areaId == 0 || areaId == -1) {
			areaId = cityId;
		}
		
		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = rnoCommonService
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,
						RnoConstant.DBConstant.MAPTYPE_BAIDU);
		
		String result = "";
		if(cellType==null) {
			GisCellQueryResult gisCells = rnoResourceManagerService
					.getRelaCellByCellParamAndAreaId(cellParam, areaId);
			result = gson.toJson(gisCells);
		} else if(("lte").equals(cellType)){
			LteCellQueryResult gisCells = rnoResourceManagerService
					.getLteCellByCellParamAndAreaId(cellParam, cityId,standardPoints);
			result = gson.toJson(gisCells);
		}

		log.info("离开方法：getRelaCellByCellParamAndCityIdForAjaxAction。");
		writeToClient(result);
	}
	
	/**
	 * 通过cell获取其邻区数据及主小区数据进行预加载
	 * 
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public void getNcellDetailsByCellAndAreaIdForAjaxAction() {
		log.info("进入getNcellDetailsByCellandCityIdForAjaxAction方法,cellParam="
				+ cellParam + ",cityId=" + cityId + ",areaId=" + areaId);
		String cell = cellParam.get("cell").toString();
		
		if(areaId == 0 || areaId == -1) {
			areaId = cityId;
		}
		GisCellQueryResult gisCells = rnoResourceManagerService
				.getNcellDetailsByCellAndAreaId(cell, areaId);
		String result = gson.toJson(gisCells);
		log.info("离开方法：getNcellDetailsByCellandCityIdForAjaxAction。");
		writeToClient(result);
	}
	
	/**
	 * 查询邻区
	 * 
	 * Sep 17, 2013 3:11:01 PM gmh
	 */
	public void queryNCellByPageForAjaxAction() {
		// page,queryCell

		log.info("进入方法：queryNCellByPageForAjaxAction。 page=" + page
				+ ",queryCell=" + queryCell);
		// System.out.println("进入方法：queryNCellByPageForAjaxAction。
		// page="+page+",queryCell="+queryCell);

		// 最小为1
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		// 获取用户能看到的所有区域集合
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "区/县");

		List<Long> allAllowAreaIds = new ArrayList<Long>();
		if (list != null && !list.isEmpty()) {
			long id;
			for (Area one : list) {
				id = one.getArea_id();
				allAllowAreaIds.add(id);
			}
		}

		List<RnoNcell> cells = rnoResourceManagerService.queryNCellByPage(page,
				queryCell, allAllowAreaIds);
		int totalCnt = rnoResourceManagerService.getTotalQueryNCellCnt(
				queryCell.buildNcellQuerySqlWhere((allAllowAreaIds)), false);

		// System.out.println("获取过后：queryNCellByPageForAjaxAction。
		// page="+page+",queryCell="+queryCell);

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));

		String cellJson = gson.toJson(cells);
		// System.out.println("cell json="+cellJson);

		String pageJson = gson.toJson(newPage);
		// System.out.println("page Json ="+pageJson);
		String result = "{'page':" + pageJson + ",'celllist':" + cellJson + "}";

		log.info("退出方法：queryNCellByPageForAjaxAction。 返回：" + result);
		// System.out.println("退出方法：queryNCellByPageForAjaxAction。 返回："+result);
		writeToClient(result);

	}

	/**
	 * 删除指定的邻区
	 * 
	 * 输入 page，queryCell ncellIds:逗号分隔的邻区id Sep 17, 2013 5:34:27 PM gmh
	 */
	public void deleteNcellForAjaxAction() {
		log.info("进入方法：deleteNcellForAjaxAction。 page=" + page + ",queryCell="
				+ queryCell + ",ncellIds=" + ncellIds);

		// 最小为1
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		// 获取用户能看到的所有区域集合
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "区/县");

		List<Long> allAllowAreaIds = new ArrayList<Long>();
		if (list != null && !list.isEmpty()) {
			long id;
			for (Area one : list) {
				id = one.getArea_id();
				allAllowAreaIds.add(id);
			}
		}
		int deCnt = 0;
		if (ncellIds != null && !ncellIds.trim().isEmpty()) {
			deCnt = rnoResourceManagerService.deleteNcellByIds(queryCell.getLabel(), allAllowAreaIds,
					ncellIds);
			log.info("删除了：" + deCnt + " 条邻区记录");
		}

		// 重新查询
		List<RnoNcell> cells = rnoResourceManagerService.queryNCellByPage(page,
				queryCell, allAllowAreaIds);
		// 需要重新生成缓存
		int totalCnt = rnoResourceManagerService.getTotalQueryNCellCnt(
				queryCell.buildNcellQuerySqlWhere((allAllowAreaIds)), true);

		// System.out.println("获取过后：queryNCellByPageForAjaxAction。
		// page="+page+",queryCell="+queryCell);

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));

		String cellJson = gson.toJson(cells);
		// System.out.println("cell json="+cellJson);

		String pageJson = gson.toJson(newPage);
		// System.out.println("page Json ="+pageJson);
		String result = "{'deletecnt':" + deCnt + ",'page':" + pageJson
				+ ",'celllist':" + cellJson + "}";
		log.info("退出deleteNcellForAjaxAction。返回：" + result);
		writeToClient(result);
	}

	/**
	 * 获取小区的详情
	 * 
	 * Sep 18, 2013 5:47:24 PM gmh
	 */
	public void getCellDetailForAjaxAction() {
		// 输入：cell
		log.info("进入方法：getCellDetailForAjaxAction。label=" + label);
		Cell cell = rnoResourceManagerService.getCellDetail(label);
		String result = gson.toJson(cell);
		log.info("退出方法:getCellDetailForAjaxAction。 获取的结果：" + result);
		writeToClient(result);
	}
	
	/**
	 * 获取LTE小区的详情
	*@Anthony chen.c10
	 */
	public void getLteCellDetailByBusCellIdForAjaxAction() {
		// 输入：cell
		log.info("进入方法：getLteCellDetailForAjaxAction。label=" + label);
		RnoLteCell lteCell = rnoResourceManagerService.getLteCellDetail(label);
		String result = gson.toJson(lteCell);
		log.info("退出方法:getLteCellDetailForAjaxAction。 获取的结果：" + result);
		writeToClient(result);
	}

	/**
	 * 查询一条小区数据记录返回表面展现
	 * 
	 * @return
	 */
	public String initModifyCellPageAction() {
		log.info("进入initModifyCellPageAction。cellId=" + cellId);
		cell = rnoResourceManagerService.queryGisCellById(cellId);
		log.info("退出initModifyCellPageAction。cell=" + cell);
		return "success";
	}

	/**
	 * 传入cell对象更新小区数据
	 * 
	 * @return
	 */
	public String updateCellInfoByIdAction() {
		log.info("进入updateCellInfoByIdAction.longitudeunchange.latitudeunchange"
				+ longitudeunchange + latitudeunchange);
		String lnglats = "";
		// System.out.println("updateCellInfoByIdAction来了");
		if (longitudeunchange != cell.getLongitude()
				|| latitudeunchange != cell.getLatitude()) {
			// System.out.println(longitudeunchange+"--haha--"+latitudeunchange);
			int ScatterAngle = 30;
			int lenofside = 60;
			if (cell.getBcch() < 100) {
				// GSM900
				ScatterAngle = 30;
				lenofside = 120;
			} else {
				// GSM1800
				ScatterAngle = 60;
				lenofside = 80;
			}
			Long lb = new Long(cell.getBearing());
			int bearing = lb.intValue();
			lnglats = rnoResourceManagerService.getGisPointArray(
					String.valueOf(cell.getLongitude()),
					String.valueOf(cell.getLatitude()), bearing, ScatterAngle,
					lenofside);
			cell.setLngLats(lnglats);
		}
		// System.out.println("外部："+cell.getLngLats());
		Cell cell1=new Cell();
		BeanUtils.copyProperties(cell, cell1);
		boolean b = rnoResourceManagerService.updateCellInfo(cell1);
		// boolean b=rnoResourceManagerService.updateCellInfoBySql(sql);
		if (b) {
			log.info("成功退出updateCellInfoByIdAction。b=" + b);
			return "success";
		} else {
			log.info("失败退出updateCellInfoByIdAction。b=" + b);
			return "fail";
		}
	}

	/**
	 * 初始化加载话务性能页面
	 * 
	 * @return
	 */
	public String initLoadTeleTrafficCapabilityPageAction() {
		initUserZoneArea();

		return "success";
	}

	public String initAreaImportBufferPageAction() {
		initUserZoneArea();

		return "success";
	}
	/**
	 * 获取Gis小区信息通过配置数据或区域数据
	 * @title 
	 * @author chao.xj
	 * @date 2014-4-15上午11:40:55
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getGisCellUseConfigIdOrAreaByPageForAjaxAction() {
		// 缩放级别：zoom
		// 区域id:areaId
		// 分页参数：page
		// 范围经纬度:topLeft,bottomRight
		log.info("进入方法：getGisCellUseConfigIdOrAreaByPageForAjaxAction。 zoom=" + zoom
				+",reSelected,"+reSelected+",areaIds=" + areaIds + ",configIds:"+configIds+",page=" + page);

		GisCellQueryResult gisCells = null;
		// 暂时支持这个区域
		int totalCnt = 0;
		//log.info("areaLevel: "+area.getArea_level());
			gisCells = rnoResourceManagerService.getGisCellUseConfigIdOrAreaByPage(reSelected,areaIds,configIds, page);
			totalCnt = gisCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		gisCells.setPage(newPage);

		// String cellJson=gson.toJson(gisCells.getGisCells());
		// //System.out.println("cell json="+cellJson);
		//
		// String pageJson=gson.toJson(newPage);
		// //System.out.println("page Json ="+pageJson);
		// String result="{'page':"+pageJson+",'celllist':"+cellJson+"}";

		String result = gson.toJson(gisCells);
		log.info("离开方法：getGisCellUseConfigIdOrAreaByPageForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + gisCells.getPage());
		// System.out.println("离开方法：getGisCellByPageForAjaxAction。 输出："+result);

		writeToClient(result);
	}
	/**
	 * 
	 * @title 初始化2G华为小区及MRR信息导入页面
	 * @return
	 * @author chao.xj
	 * @date 2014-8-5上午9:42:58
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String init2GHWCellAndMrrImportPageAction() {
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}

	

//	/**
//	 * 初始化用户可见区域
//	 * 
//	 * @author brightming 2013-10-17 上午11:59:19
//	 */
//	private void initAreaList() {
//		String account = (String) SessionService.getInstance().getValueByKey(
//				"userId");
//		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
//				"省");
//		cityAreas = new ArrayList<Area>();
//		countryAreas = new ArrayList<Area>();
//		if (provinceAreas != null && provinceAreas.size() > 0) {
//			cityAreas = rnoCommonService
//					.getSpecialSubAreasByAccountAndParentArea(account,
//							provinceAreas.get(0).getArea_id(), "市");
//			if (cityAreas != null && cityAreas.size() > 0) {
//				countryAreas = rnoCommonService
//						.getSpecialSubAreasByAccountAndParentArea(account,
//								cityAreas.get(0).getArea_id(), "区/县");
//				if (countryAreas != null && countryAreas.size() > 0) {
//					centerPoint = new Point();
//					centerPoint.setLng(countryAreas.get(0).getLongitude());
//					centerPoint.setLat(countryAreas.get(0).getLatitude());
//				}
//			}
//		}
//
//	}

	/**
	 * 初始化BSC管理所需信息
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30上午11:42:55
	 */
	public String initBscManagerAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 分页查询BSC的信息
	 * @author peng.jm
	 * @date 2014-9-30下午03:12:38
	 */
	public void queryBscByPageForAjaxAction() {
		log.debug("queryBscByPageForAjaxAction。page=" + page + ",bscQuery=" + bscQuery);
		
		Page newPage = page.copy();
		List<Map<String, Object>> bscList = rnoResourceManagerService
				.queryBscByPage(bscQuery, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String dstr = gson.toJson(bscList);
		String result = "{'page':" + pstr + ",'bscList':" + dstr + "}";
		log.debug("退出queryBscByPageForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 通过名称删除BSC
	 * @author peng.jm
	 * @date 2014-10-10上午10:44:43
	 */
	public void deleteBscByNameForAjaxAction() {
		log.debug("deleteBscByNameForAjaxAction。bscEngName=" + bscEngName + ", cityId=" + cityId);
		Map<String, Object> res = rnoResourceManagerService.deleteBscByName(bscEngName, cityId);
		String flag = res.get("flag").toString();
		String msg = "";
		if(res.get("msg") != null) {
			msg = res.get("msg").toString();
		}
		String result = "{'flag':" + flag + ",'msg':'"+msg+"'}";
		log.debug("退出deleteBscByNameForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);		
	}
	
	/**
	 * 新增单个BSC
	 * @author peng.jm
	 * @date 2014-10-16下午06:10:03
	 */
	public void addSingleBscAjaxForAction() {
		log.debug("addSingleBscAjaxForAction。bscEngName=" + bscEngName
				+ ", cityId=" + cityId + ", manufacturers=" + manufacturers);
		boolean flag = false;
		if(bscEngName != null && !("").equals(bscEngName)) {
			Map<String, Object> res = rnoResourceManagerService.addSingleBsc(
					bscEngName, manufacturers, cityId);
			flag = Boolean.parseBoolean(res.get("flag").toString());
		}
		String result = "{'flag':" + flag + "}";
		log.debug("退出deleteBscByNameForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 初始化爱立信fas数据管理页面
	 * @return
	 * @author peng.jm
	 * @date 2015年1月16日10:56:25
	 */
	public String initRnoEriFasManageAction(){
		initAreaList();
//		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
//		coverareas=mid.get("coverarea");
//		covertypes=mid.get("covertype");
//		importancegrade=mid.get("importancegrade");
		return "success";
	}
	
	/**
	 * 查询爱立信fas描述信息
	 * 
	 * @author peng.jm
	 * @date 2014-9-2上午09:32:03
	 */
	public void queryEriFasDescAjaxAction() {
		log.debug("queryEriFasDescAjaxAction.page="+page+",attachParams="+attachParams);
		
		Page newPage = page.copy();
		List<Map<String, Object>> dataRecs = rnoResourceManagerService
				.queryEriFasDescByPage(attachParams, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEriFasDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 初始化2g爱立信mrr数据管理页面
	 * @return
	 * @author peng.jm
	 * 2014年9月1日11:21:51
	 */
	public String initRno2GEriMrrManageAction(){
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	
	/**
	 * 查询爱立信mrr描述信息
	 * 
	 * @author peng.jm
	 * @date 2014-9-2上午09:32:03
	 */
	public void queryEriMrrDescV2AjaxAction() {
		log.debug("queryEriMrrDescV2AjaxAction.page="+page+",attachParams="+attachParams);
		
		Page newPage = page.copy();
		List<Map<String, Object>> dataRecs = rnoResourceManagerService
				.queryEriMrrDescByPage(attachParams, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEriMrrDescV2AjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 查询爱立信Mrr详情信息
	 * 
	 * @author peng.jm
	 * @date 2014-9-2下午05:12:04
	 */
	public void queryEriMrrDetailV2AjaxAction() {
		log.debug("queryEriMrrDetailV2AjaxAction.page="+page+",attachParams="+attachParams);
		
		Page newPage = page.copy();
		List<Map<String, Object>> dataRecs = rnoResourceManagerService
				.queryEriMrrDetailByPage(attachParams, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEriMrrDetailV2AjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 初始化中兴4g切换数据管理页面
	 * @author peng.jm
	 * @date 2015年3月14日11:22:15
	 */
	public String initRno4GZteHoManageAction(){
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	/**
	 * 查询中兴4g切换数据描述信息
	 * 
	 * @author peng.jm
	 * @date 2015年3月14日11:22:15
	 */
	public void queryZte4GHoDescAjaxAction() {
		log.debug("queryZte4GHoDescAjaxAction.page="+page+",attachParams="+attachParams);
		
		String sMeaTime = attachParams.get("meaBegDate").toString();
		long sMill = dateUtil.parseDateArbitrary(sMeaTime).getTime();
		String eMeaTime = attachParams.get("meaEndDate").toString();
		long eMill = dateUtil.parseDateArbitrary(eMeaTime).getTime();
		String cityId = attachParams.get("cityId").toString();
		
		String tableName = "LTE4G_HO_DESC";
		String startRow = cityId+"_"+sMill+"_#";
		String stopRow = cityId+"_"+eMill+"_~";
		Page newPage = page.copy();
		
		List<Map<String, String>> dataRecs = rnoResourceManagerService
				.queryZteHoDataFromHbaseByPage(tableName, startRow, stopRow, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryZte4GHoDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);	
	}
	

	
	/**
	 * 
	 * @title 初始化4G HO数据管理页面
	 * @return
	 */
	public String initRno4GHoManageAction() {
		initAreaList();
		Map<String, List<String>> mid = rnoResourceManagerService
				.getAllCoverareaCovertypeAndImportances();
		coverareas = mid.get("coverarea");
		covertypes = mid.get("covertype");
		importancegrade = mid.get("importancegrade");
		return "success";
	}

	/**
	 * 查询4g切换数据描述信息
	 */
	public void queryHoDescAjaxAction() {
		log.debug("queryHoDescAjaxAction.page="+page+",attachParams="+attachParams);
		
		g4HoDescQueryCond = new G4HoDescQueryCond();
		g4HoDescQueryCond.setFactory((String) attachParams.get("factory"));
		g4HoDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		g4HoDescQueryCond.setMeaBegTime((String) attachParams.get("hoMeaBegDate"));
		g4HoDescQueryCond.setMeaEndTime((String) attachParams.get("hoMeaEndDate"));
		log.debug("queryHoDescAjaxAction.page=" + page + ",attmap="
				+ attachParams + ",queryCond=" + g4HoDescQueryCond);
		
		
		Page newPage = page.copy();
		
		List<Map<String, String>> dataRecs = rnoResourceManagerHbaseService
				.queryHoDataFromHbaseByPage(g4HoDescQueryCond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出query4GHoDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 初始化爱立信4g切换数据管理页面
	 * @author peng.jm
	 * @date 2015年3月14日11:22:15
	 */
	public String initRno4GEriHoManageAction(){
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	
	/**
	 * 查询爱立信4g切换数据描述信息
	 * 
	 * @author peng.jm
	 * @date 2015年3月14日11:22:15
	 */
	public void queryEri4GHoDescAjaxAction() {
		log.debug("queryEri4GHoDescAjaxAction.page="+page+",attachParams="+attachParams);
		
		String sMeaTime = attachParams.get("meaBegDate").toString();
		long sMill = dateUtil.parseDateArbitrary(sMeaTime).getTime();
		String eMeaTime = attachParams.get("meaEndDate").toString();
		long eMill = dateUtil.parseDateArbitrary(eMeaTime).getTime();
		String cityId = attachParams.get("cityId").toString();
		
		String tableName = "LTE4G_HO_DESC";
		String startRow = cityId+"_"+sMill+"_#";
		String stopRow = cityId+"_"+eMill+"_~";
		Page newPage = page.copy();
		
		List<Map<String, String>> dataRecs = rnoResourceManagerService
				.queryEriHoDataFromHbaseByPage(tableName, startRow, stopRow, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEri4GHoDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 初始化2g爱立信ncs数据管理页面
	 * @return
	 * @author brightming
	 * 2014-8-20 下午5:11:37
	 */
	public String initRno2GEriNcsManageAction(){
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	
	/**
	 * 表重新设计以后，相应的查询ncs描述信息的方法
	 * 
	 * @author brightming
	 * 2014-8-22 下午12:37:00
	 */
	public void queryEriNcsDescV2AjaxAction(){

		eri2GNcsDescQueryCond=new Eri2GNcsDescQueryCond();
		eri2GNcsDescQueryCond.setBsc((String)attachParams.get("bsc"));
		eri2GNcsDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2GNcsDescQueryCond.setMeaBegTime((String) attachParams.get("ncsMeaBegDate"));
		eri2GNcsDescQueryCond.setMeaEndTime((String) attachParams.get("ncsMeaEndDate"));
		log.debug("queryNcsDescV2AjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2GNcsDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoResourceManagerService.queryEriNcsDescCnt(eri2GNcsDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoResourceManagerService.queryEriNcsDescByPage(eri2GNcsDescQueryCond, newPage);
		log.debug("EriNcsDesc size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryEriNcsDescV2AjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 初始化华为ncs数据管理页面
	 * @return
	 * @author brightming
	 * 2014-8-24 下午4:05:30
	 */
	public String initRno2GHwNcsManageAction(){
		initAreaList();
		Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
		coverareas=mid.get("coverarea");
		covertypes=mid.get("covertype");
		importancegrade=mid.get("importancegrade");
		return "success";
	}
	
	/**
	 * 查询华为ncs的描述信息
	 * 
	 * @author brightming
	 * 2014-8-24 下午5:01:42
	 */
	public void queryHwNcsDescV2AjaxAction(){

		eri2GNcsDescQueryCond=new Eri2GNcsDescQueryCond();
		eri2GNcsDescQueryCond.setBsc((String)attachParams.get("bsc"));
		eri2GNcsDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		eri2GNcsDescQueryCond.setMeaBegTime((String) attachParams.get("ncsMeaBegDate"));
		eri2GNcsDescQueryCond.setMeaEndTime((String) attachParams.get("ncsMeaEndDate"));
		log.debug("queryHwNcsDescV2AjaxAction.page="+page+",attmap="+attachParams+",queryCond="+eri2GNcsDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoResourceManagerService.queryHwNcsDescCnt(eri2GNcsDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoResourceManagerService.queryHwNcsDescByPage(eri2GNcsDescQueryCond, newPage);
		log.debug("HwNcsDesc size:" + dataRecs==null?0:dataRecs.size());
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryHwNcsDescV2AjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
 * 
 * @title 初始化华为mrr数据管理页面
 * @return
 * @author chao.xj
 * @date 2014-9-2下午1:24:24
 * @company 怡创科技
 * @version 1.2
 */
public String initRno2GHwMrrManageAction(){
	initAreaList();
	Map<String,List<String>> mid=rnoResourceManagerService.getAllCoverareaCovertypeAndImportances();
	coverareas=mid.get("coverarea");
	covertypes=mid.get("covertype");
	importancegrade=mid.get("importancegrade");
	return "success";
}
/**
 * 
 * @title 查询华为mrr的描述信息
 * @author chao.xj
 * @date 2014-9-2下午2:23:29
 * @company 怡创科技
 * @version 1.2
 */
public void queryHwMrrDescV2AjaxAction(){

	hw2gMrrDescQueryCond=new Hw2GMrrDescQueryCond();
	hw2gMrrDescQueryCond.setBsc((String)attachParams.get("bsc"));
	hw2gMrrDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
	hw2gMrrDescQueryCond.setMeaBegTime((String) attachParams.get("ncsMeaBegDate"));
	hw2gMrrDescQueryCond.setMeaEndTime((String) attachParams.get("ncsMeaEndDate"));
	log.debug("queryHwMrrDescV2AjaxAction.page="+page+",attmap="+attachParams+",queryCond="+hw2gMrrDescQueryCond);
	
	int cnt=page.getTotalCnt();
	if(cnt<0){
		cnt=(int)rnoResourceManagerService.queryHwMrrDescCnt(hw2gMrrDescQueryCond);
	}
	
	Page newPage = page.copy();
	newPage.setTotalCnt(cnt);
	
	List<Map<String,Object>> dataRecs = rnoResourceManagerService.queryHwMrrDescByPage(hw2gMrrDescQueryCond, newPage);
	log.debug("HwmrrDesc size:" + dataRecs==null?0:dataRecs.size());
	String datas = gson.toJson(dataRecs);

	int totalCnt = newPage.getTotalCnt();
	newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
			+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
	newPage.setForcedStartIndex(-1);

	String pstr = gson.toJson(newPage);
	String result = "{'page':" + pstr + ",'data':" + datas + "}";
	log.debug("退出queryHwMrrDescV2AjaxAction。输出：" + result);
	HttpTools.writeToClient(result);
}

	/**
	 * 
	 * @title 初始化4G MR数据管理页面
	 * @return
	 * @author chao.xj
	 * @date 2015-3-16下午3:15:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initRno4GMrManageAction() {
		initAreaList();
		Map<String, List<String>> mid = rnoResourceManagerService
				.getAllCoverareaCovertypeAndImportances();
		coverareas = mid.get("coverarea");
		covertypes = mid.get("covertype");
		importancegrade = mid.get("importancegrade");
		return "success";
	}

	/**
	 * 
	 * @title 查询MR的描述信息
	 * @author chao.xj
	 * @date 2015-3-18上午11:02:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryMrDescAjaxAction() {

		g4MrDescQueryCond = new G4MrDescQueryCond();
		g4MrDescQueryCond.setFactory((String) attachParams.get("factory"));
		g4MrDescQueryCond.setCityId(Long.parseLong((String) attachParams
				.get("cityId")));
		g4MrDescQueryCond.setMeaBegTime((String) attachParams
				.get("mrMeaBegDate"));
		g4MrDescQueryCond.setMeaEndTime((String) attachParams
				.get("mrMeaEndDate"));
		log.debug("queryEriMrDescAjaxAction.page=" + page + ",attmap="
				+ attachParams + ",queryCond=" + g4MrDescQueryCond);

		Page newPage = page.copy();

		List<Map<String, String>> dataRecs = rnoResourceManagerHbaseService
				.queryMrDataFromHbaseByPage(g4MrDescQueryCond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryMrDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 初始化4G KPI数据管理页面
	 * @return
	 * @author chao.xj
	 * @date 2015-9-17上午10:25:21
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public String initRno4GKpiManageAction() {
		initAreaList();
		Map<String, List<String>> mid = rnoResourceManagerService
				.getAllCoverareaCovertypeAndImportances();
		coverareas = mid.get("coverarea");
		covertypes = mid.get("covertype");
		importancegrade = mid.get("importancegrade");
		return "success";
	}
	/**
	 * 
	 * @title 查询KPI的描述信息
	 * @author chao.xj
	 * @date 2015-9-17上午10:26:21
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void queryKpiDescAjaxAction() {

		g4KpiDescQueryCond = new G4KpiDescQueryCond();
		g4KpiDescQueryCond.setCityId(Long.parseLong((String) attachParams
				.get("cityId")));
		g4KpiDescQueryCond.setMeaBegTime((String) attachParams
				.get("kpiMeaBegDate"));
		g4KpiDescQueryCond.setMeaEndTime((String) attachParams
				.get("kpiMeaEndDate"));
		log.debug("queryKpiDescAjaxAction.page=" + page + ",attmap="
				+ attachParams + ",queryCond=" + g4KpiDescQueryCond);

		Page newPage = page.copy();

		List<Map<String, String>> dataRecs = rnoResourceManagerHbaseService
				.queryKpiDataFromHbaseByPage(g4KpiDescQueryCond, newPage);

		log.info("计算以后，page=" + newPage);
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String datas = gson.toJson(dataRecs);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryKpiDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
}
