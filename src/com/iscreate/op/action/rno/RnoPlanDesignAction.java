package com.iscreate.op.action.rno;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImpl.CellInterWithDetailInfo;
import com.iscreate.op.pojo.rno.CobsicCells;
import com.iscreate.op.pojo.rno.CobsicCellsExpand;
import com.iscreate.op.pojo.rno.FreqInterIndex;
import com.iscreate.op.pojo.rno.NcellAnalysisCondition;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RedundantNCell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoCellStructDesc;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoInterferenceDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.pojo.rno.StsAnaItemDetail;
import com.iscreate.op.pojo.rno.StsConfig;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoPlanDesignService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.RnoStructureService;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HttpTools;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.opensymphony.xwork2.ActionContext;

public class RnoPlanDesignAction {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoPlanDesignAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf_full = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 注入

	private RnoPlanDesignService rnoPlanDesignService;

	private RnoCommonService rnoCommonService;

	private RnoResourceManagerService rnoResourceManagerService;
	
	private RnoStructureService rnoStructureService;
	// 区域
	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	private List<Area> countryAreas;
	private Point centerPoint;// 中心点

	private PlanConfig planConfig;

	// 页面变量
	private long areaId;
	private long cityId;//邻区匹配和全网分析需要使用cityId
	private long taskId;//ncs分析任务id
	private List<Long> ncsDescIds; 
	private List<Long> handoverDescIds;
	private long configId = -1;
	private long systemconfigure;
	private long tempanalyse;
	private String chareaname;
	private String ennetworkstand;
	private String areaIds;
	
	private String type;// 移除分析列表类型 CELLDATA 小区配置 INTERFERENCEDATA
	private long loadedConfigId;// 配置id
	private long selectId;// 选择分析的配置id
	private Page page;

	private String areaName;

	private String date[];// 日期数组
	private String loaditem;// 加载项数组

	private String configIds;

	private int bcch;
	private String bsic;
	private String schemeName;//方案名称
	private String isDefault;
	private boolean sysDefault;//是否为系统缺省
	private boolean reSelected;//是否为重新选择的小匹配置数据
	// 干扰数据加载
	private List<RnoInterferenceDescriptor> tempRnoInterDesc;
	private RnoInterferenceDescriptor systemInterDesc;

	private HashMap<String, Object> attachParams;
	private String cell;

	// 邻区分析
	private List<PlanConfig> cellConfigList;// 加载到分析列表的小区配置
	private List<PlanConfig> cellStructList;// 加载到分析列表的小区结构指标
	private List<PlanConfig> cellHandoverList;// 加载到分析列表的小区切换
	private List<PlanConfig> ncsList;// 加载到分析列表的ncs

	private NcellAnalysisCondition condition;// 邻区检查的条件
	private long cellStructId, cellConfigId, handoverId, ncsId;

	private long MartixDescId;
	
	private String bcchStr;
	private String tchStr;
	
	
	public long getMartixDescId() {
		return MartixDescId;
	}

	public void setMartixDescId(long martixDescId) {
		MartixDescId = martixDescId;
	}

	public void setRnoStructureService(RnoStructureService rnoStructureService)
	{
		this.rnoStructureService = rnoStructureService;
	}

	public void setCityId(long cityId)
	{
		this.cityId = cityId;
	}

	public void setTaskId(long taskId)
	{
		this.taskId = taskId;
	}

	public List<Long> getNcsDescIds()
	{
		return ncsDescIds;
	}

	public List<Long> getHandoverDescIds()
	{
		return handoverDescIds;
	}

	public void setNcsDescIds(List<Long> ncsDescIds)
	{
		this.ncsDescIds = ncsDescIds;
	}

	public void setHandoverDescIds(List<Long> handoverDescIds)
	{
		this.handoverDescIds = handoverDescIds;
	}

	public RnoPlanDesignService getRnoPlanDesignService() {
		return rnoPlanDesignService;
	}

	public void setRnoPlanDesignService(
			RnoPlanDesignService rnoPlanDesignService) {
		this.rnoPlanDesignService = rnoPlanDesignService;
	}

	public RnoCommonService getRnoCommonService() {
		return rnoCommonService;
	}

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	public List<Area> getProvinceAreas() {
		return provinceAreas;
	}

	public void setProvinceAreas(List<Area> provinceAreas) {
		this.provinceAreas = provinceAreas;
	}

	public List<Area> getCityAreas() {
		return cityAreas;
	}

	public void setCityAreas(List<Area> cityAreas) {
		this.cityAreas = cityAreas;
	}

	public List<Area> getCountryAreas() {
		return countryAreas;
	}

	public void setCountryAreas(List<Area> countryAreas) {
		this.countryAreas = countryAreas;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	public PlanConfig getPlanConfig() {
		return planConfig;
	}

	public void setPlanConfig(PlanConfig planConfig) {
		this.planConfig = planConfig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSelectId() {
		return selectId;
	}

	public void setSelectId(long selectId) {
		this.selectId = selectId;
	}

	public String[] getDate() {
		return date;
	}

	public void setDate(String[] date) {
		this.date = date;
	}

	public String getLoaditem() {
		return loaditem;
	}

	public void setLoaditem(String loaditem) {
		this.loaditem = loaditem;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public long getLoadedConfigId() {
		return loadedConfigId;
	}

	public void setLoadedConfigId(long loadedConfigId) {
		this.loadedConfigId = loadedConfigId;
	}

	public List<RnoInterferenceDescriptor> getTempRnoInterDesc() {
		return tempRnoInterDesc;
	}

	public void setTempRnoInterDesc(
			List<RnoInterferenceDescriptor> tempRnoInterDesc) {
		this.tempRnoInterDesc = tempRnoInterDesc;
	}

	public RnoInterferenceDescriptor getSystemInterDesc() {
		return systemInterDesc;
	}

	public void setSystemInterDesc(RnoInterferenceDescriptor systemInterDesc) {
		this.systemInterDesc = systemInterDesc;
	}

	public HashMap<String, Object> getAttachParams() {
		return attachParams;
	}

	public void setAttachParams(HashMap<String, Object> attachParams) {
		this.attachParams = attachParams;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public long getConfigId() {
		return configId;
	}

	public void setConfigId(long configId) {
		this.configId = configId;
	}

	public long getSystemconfigure() {
		return systemconfigure;
	}

	public void setSystemconfigure(long systemconfigure) {
		this.systemconfigure = systemconfigure;
	}

	public long getTempanalyse() {
		return tempanalyse;
	}

	public void setTempanalyse(long tempanalyse) {
		this.tempanalyse = tempanalyse;
	}

	public String getChareaname() {
		return chareaname;
	}

	public void setChareaname(String chareaname) {
		this.chareaname = chareaname;
	}

	public String getEnnetworkstand() {
		return ennetworkstand;
	}

	public void setEnnetworkstand(String ennetworkstand) {
		this.ennetworkstand = ennetworkstand;
	}

	public String getConfigIds() {
		return configIds;
	}

	public void setConfigIds(String configIds) {
		this.configIds = configIds;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public List<PlanConfig> getCellConfigList() {
		return cellConfigList;
	}

	public void setCellConfigList(List<PlanConfig> cellConfigList) {
		this.cellConfigList = cellConfigList;
	}

	public List<PlanConfig> getCellStructList() {
		return cellStructList;
	}

	public void setCellStructList(List<PlanConfig> cellStructList) {
		this.cellStructList = cellStructList;
	}

	public List<PlanConfig> getCellHandoverList() {
		return cellHandoverList;
	}

	public void setCellHandoverList(List<PlanConfig> cellHandoverList) {
		this.cellHandoverList = cellHandoverList;
	}

	public List<PlanConfig> getNcsList() {
		return ncsList;
	}

	public void setNcsList(List<PlanConfig> ncsList) {
		this.ncsList = ncsList;
	}

	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}

	public NcellAnalysisCondition getCondition() {
		return condition;
	}

	public void setCondition(NcellAnalysisCondition condition) {
		this.condition = condition;
	}

	public int getBcch() {
		return bcch;
	}

	public void setBcch(int bcch) {
		this.bcch = bcch;
	}
	public String getBsic() {
		return bsic;
	}
	public void setBsic(String bsic) {
		this.bsic = bsic;
	}
	public long getCellStructId() {
		return cellStructId;
	}

	public void setCellStructId(long cellStructId) {
		this.cellStructId = cellStructId;
	}

	public long getCellConfigId() {
		return cellConfigId;
	}

	public void setCellConfigId(long cellConfigId) {
		this.cellConfigId = cellConfigId;
	}

	public long getHandoverId() {
		return handoverId;
	}

	public void setHandoverId(long handoverId) {
		this.handoverId = handoverId;
	}

	public long getNcsId() {
		return ncsId;
	}

	public void setNcsId(long ncsId) {
		this.ncsId = ncsId;
	}
	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isSysDefault() {
		return sysDefault;
	}

	public void setSysDefault(boolean sysDefault) {
		this.sysDefault = sysDefault;
	}
	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	public boolean isReSelected() {
		return reSelected;
	}

	public void setReSelected(boolean reSelected) {
		this.reSelected = reSelected;
	}
	
	public String getBcchStr() {
		return bcchStr;
	}

	public void setBcchStr(String bcchStr) {
		this.bcchStr = bcchStr;
	}

	public String getTchStr() {
		return tchStr;
	}

	public void setTchStr(String tchStr) {
		this.tchStr = tchStr;
	}

	/**
	 * 
	 * @description: 初始化频率复用分析页面
	 * @author：yuan.yw
	 * @return
	 * @return String
	 * @date：Nov 6, 2013 4:13:26 PM
	 */
	public String initRnoFreqReusePageAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * 初始化用户可见区域
	 * 
	 * @author brightming 2013-10-17 上午11:59:19
	 */
	private void initAreaList() {
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
				"省");
		cityAreas = new ArrayList<Area>();
		countryAreas = new ArrayList<Area>();
		if (provinceAreas != null && provinceAreas.size() > 0) {
			cityAreas = rnoCommonService
					.getSpecialSubAreasByAccountAndParentArea(account,
							provinceAreas.get(0).getArea_id(), "市");
			//通过获取默认城市，然后置顶
			long cfgCityId = rnoCommonService.getUserConfigAreaId(account);
			Area areaTemp = new Area();
			for (int i = 0; i < cityAreas.size(); i++) {
				if(cityAreas.get(i).getArea_id() == cfgCityId) {
					areaTemp  = cityAreas.get(0);
					cityAreas.set(0, cityAreas.get(i));
					cityAreas.set(i, areaTemp);
				}
			}
			if (cityAreas != null && cityAreas.size() > 0) {
				countryAreas = rnoCommonService
						.getSpecialSubAreasByAccountAndParentArea(account,
								cityAreas.get(0).getArea_id(), "区/县");
				if (countryAreas != null && countryAreas.size() > 0) {
					centerPoint = new Point();
					centerPoint.setLng(countryAreas.get(0).getLongitude());
					centerPoint.setLat(countryAreas.get(0).getLatitude());
				}
			}
		}

	}

	/**
	 * 
	 * @description: 获取小区配置加载的分析列表
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 6, 2013 5:23:13 PM
	 */
	public void getCellConfigAnalysisListForAjaxAction() {
		log.info("进入方法：getCellConfigAnalysisListForAjaxAction。");
		List<PlanConfig> planConfigs = null;
		planConfigs = (List<PlanConfig>) SessionService.getInstance()
				.getValueByKey(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		/*
		 * List<PlanConfig> planConfigs = new ArrayList<PlanConfig>(); //测试hard
		 * code PlanConfig p = new PlanConfig(); p.setCollectTime("2013/12/24");
		 * p.setTitle("天河区GSM小区"); p.setConfigId(101); p.setSelected(false);
		 * p.setTemp(false); p.setType("CELLDATA"); p.setName("系统配置");
		 * planConfigs.add(p); p = new PlanConfig();
		 * p.setCollectTime("2013/12/21"); p.setTitle("天河区GSM小区");
		 * p.setConfigId(1); p.setSelected(false); p.setTemp(true);
		 * p.setType("CELLDATA"); p.setName("苦逼是多发点合适的"); planConfigs.add(p);
		 */

		if (planConfigs == null) {
			planConfigs = Collections.EMPTY_LIST;
		}
		String result = gson.toJson(planConfigs);
		log.info("退出方法：getCellConfigAnalysisListForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 
	 * @description: 获取小区干扰加载的分析列表
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 6, 2013 5:23:13 PM
	 */
	public void getCellInterferenceAnalysisListForAjaxAction() {
		log.info("进入方法：getCellInterferenceAnalysisListForAjaxAction。");
		List<PlanConfig> planConfigs = null;
		planConfigs = (List<PlanConfig>) SessionService
				.getInstance()
				.getValueByKey(
						RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
		/*
		 * List<PlanConfig> planConfigs = new ArrayList<PlanConfig>(); //测试hard
		 * code PlanConfig p = new PlanConfig(); p.setCollectTime("2013/12/24");
		 * p.setTitle("天河区GSMd小区"); p.setConfigId(1); p.setSelected(false);
		 * p.setTemp(false); p.setType("INTERFERENCEDATA"); p.setName("系统配d置");
		 * planConfigs.add(p); p = new PlanConfig();
		 * p.setCollectTime("2013/12/21"); p.setTitle("天河区GSMd小区");
		 * p.setConfigId(1); p.setSelected(false); p.setTemp(true);
		 * p.setType("INTERFERENCEDATA"); p.setName("苦逼是多发d点合适的");
		 * planConfigs.add(p);
		 */
		if (planConfigs == null) {
			planConfigs = Collections.EMPTY_LIST;
		}
		String result = gson.toJson(planConfigs);
		log.info("退出方法：getCellInterferenceAnalysisListForAjaxAction。输出："
				+ result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 
	 * @description: 从小区配置或小区干扰加载列表中移除选定的加载项
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 7, 2013 5:22:44 PM
	 */
	public void removeCellAnalysisItemFromListForAjaxAction() {
		log.info("进入方法：removeCellAnalysisItemFromListForAjaxAction。loadedConfigId="
				+ loadedConfigId);

		List<PlanConfig> planConfigs = null;
		String sessionKey = "";
		if ("CELLDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
		} else if ("INTERFERENCEDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID;
		}
		planConfigs = (List<PlanConfig>) SessionService.getInstance()
				.getValueByKey(sessionKey);
		if (planConfigs != null && planConfigs.size() > 0) {
			// boolean isFromQuery = false;
			int i = 0;
			for (; i < planConfigs.size(); i++) {
				if (planConfigs.get(i).getConfigId() == loadedConfigId) {
					break;
				}
			}
			if (i < planConfigs.size()) {
				log.info("移除指定的加载项。");
				planConfigs.remove(i);
				ActionContext ctx = ActionContext.getContext();
				HttpServletRequest request = (HttpServletRequest) ctx
						.get(ServletActionContext.HTTP_REQUEST);
				HttpSession session = request.getSession();
				session.setAttribute(sessionKey, planConfigs);
				// 移除cache
				// isFromQuery = planConfigs.get(i).isFromQuery();
				/*
				 * if (isFromQuery) { try { memCached
				 * .delete(RnoConstant.CacheConstant
				 * .CACHE_STATICS_QUERY_COND_PRE + loadedConfigId); } catch
				 * (Exception e) { e.printStackTrace(); } }
				 */
			}
		}
		log.info("退出方法：removeCellAnalysisItemFromListForAjaxAction。");
		HttpTools.writeToClient("success");
	}

	/**
	 * 
	 * @description: 重选挑选需要分析的小区配置或小区干扰列表 type判断小区配置或小区干扰 selectId 选择配置id
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 8, 2013 9:47:58 AM
	 * 
	 * 
	 */
	@Deprecated
	public void reselectCellAnalysisListForAjaxAction() {
		log.info("进入方法：reselectCellAnalysisListForAjaxAction。 selectId="
				+ selectId);
		List<PlanConfig> planConfigs = null;
		String sessionKey = "";
		if ("CELLDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
		} else if ("INTERFERENCEDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID;
		}
		planConfigs = (List<PlanConfig>) SessionService.getInstance()
				.getValueByKey(sessionKey);
		if (planConfigs != null && planConfigs.size() > 0) {
			for (PlanConfig pc : planConfigs) {
				if (selectId <= 0) {
					pc.setSelected(false);
				} else {
					if (pc.getConfigId() == selectId) {
						pc.setSelected(true);
						break;
					}
				}
			}
			// 更新session的数据
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			HttpSession session = request.getSession();
			session.setAttribute(sessionKey, planConfigs);
		}
		HttpTools.writeToClient("ok");
	}

	/**
	 * 
	 * @description: 根据类型获取选择的小区配置或小区干扰中分析列表中的小区的gis信息
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 8, 2013 9:59:47 AM
	 */
	public void getFreqReuseCellGisInfoFromSelAnaListForAjaxAction() {
		log.info("进入getFreqReuseCellGisInfoFromSelAnaListForAjaxAction。configId="
				+ configId);
		List<PlanConfig> planConfigs = null;
		String sessionKey = "";
//		if ("CELLDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
//		} else if ("INTERFERENCEDATA".equals(type)) {
//			sessionKey = RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID;
//		}
		planConfigs = (List<PlanConfig>) SessionService.getInstance()
				.getValueByKey(sessionKey);
		PlanConfig selectConfig = null;
		if (planConfigs != null && planConfigs.size() > 0) {
			for (int i = 0; i < planConfigs.size(); i++) {
				if (planConfigs.get(i).getConfigId() == configId) {
					selectConfig = planConfigs.get(i);
					planConfigs.get(i).setSelected(true);
				}else{
					planConfigs.get(i).setSelected(false);
				}
			}
		}
		/*
		 * //测试hard code PlanConfig p = new PlanConfig();
		 * p.setCollectTime("2013/12/24"); p.setTitle("天河区GSM小区");
		 * p.setConfigId(101); p.setSelected(false); p.setTemp(false);
		 * p.setType("CELLDATA"); p.setName("系统配置"); selectConfig=p;
		 */

		GisCellQueryResult gisCellResults = null;
		if (selectConfig == null) {
			gisCellResults = new GisCellQueryResult();
			gisCellResults.setTotalCnt(0);
		} else {
			page.setForcedStartIndex(-1);
			gisCellResults = this.rnoPlanDesignService
					.getFreqReuseCellGisInfoFromSelectionList(selectConfig,
							page);
		}
		int totalCnt = gisCellResults.getTotalCnt();

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);
		gisCellResults.setPage(newPage);
		String result = gson.toJson(gisCellResults);
		log.info("退出getFreqReuseCellGisInfoFromSelAnaListForAjaxAction。");
		HttpTools.writeToClient(result);
	}

	/**
	 * 
	 * @description: 统计指定区域范围小区的频率复用情况
	 * @author：yuan.yw
	 * @return void
	 * @date：Nov 7, 2013 11:51:19 AM
	 */
	public void staticsFreqReuseInfoForAjaxAction() {
		log.info("进入方法：staticsFreqReuseInfoForAjaxAction.configId="+configId);
		Map<Integer, Object> freqReuseInfos = null;
		List<PlanConfig> planConfigs = null;
		String sessionKey = "";
//		if ("CELLDATA".equals(type)) {
			sessionKey = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
//		} else if ("INTERFERENCEDATA".equals(type)) {
//			sessionKey = RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID;
//		}
		planConfigs = (List<PlanConfig>) SessionService.getInstance()
				.getValueByKey(sessionKey);
		PlanConfig selectConfig = null;
		if (planConfigs != null && planConfigs.size() > 0) {
			for (int i = 0; i < planConfigs.size(); i++) {
				if (planConfigs.get(i).getConfigId() == configId) {
					selectConfig = planConfigs.get(i);// 选中分析列表
					log.info("selectConfig.isTemp():"+selectConfig.isTemp());
				}
			}
		}
		/*
		 * //测试hard code PlanConfig p = new PlanConfig();
		 * p.setCollectTime("2013/12/24"); p.setTitle("天河区GSM小区");
		 * p.setConfigId(101); p.setSelected(false); p.setTemp(false);
		 * p.setType("CELLDATA"); p.setName("系统配置"); selectConfig=p;
		 */
		freqReuseInfos = this.rnoPlanDesignService
				.staticsFreqReuseInfoInArea(selectConfig);// 获取统计信息
		int totalCount = 0;
		if (freqReuseInfos == null) {
			freqReuseInfos = Collections.EMPTY_MAP;
		}
		totalCount = freqReuseInfos.size();
		Page newPage = new Page();// 当前分页
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(freqReuseInfos.size());
		newPage.setTotalPageCnt(freqReuseInfos.size() / newPage.getPageSize()
				+ (freqReuseInfos.size() % newPage.getPageSize() == 0 ? 0 : 1));
		Map<Integer, Object> rMap = null;
		if (page != null) {// 获取分页统计
			int start = (page.getCurrentPage() - 1) * page.getPageSize();
			if (start >= 0) {
				int toIndex = start + page.getPageSize() - 1;
				if (toIndex > freqReuseInfos.size()) {
					toIndex = freqReuseInfos.size();
				}
				int i = 0;
				rMap = new TreeMap<Integer, Object>();
				for (Integer key : freqReuseInfos.keySet()) {
					if (i >= start && i <= toIndex) {
						rMap.put(key, freqReuseInfos.get(key));
					}
					if (i == toIndex) {
						break;
					}
					i++;
				}
			} else {
				rMap = freqReuseInfos;
			}
		}
		Gson gson = new GsonBuilder().create();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("freqReuseInfos", rMap);
		resultMap.put("page", newPage);
		String result = gson.toJson(resultMap);
		log.info("退出方法：staticsFreqReuseInfoForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);

	}

	/**
	 * 初始化干扰数据导入和加载页面
	 * 
	 * @return
	 * @author brightming 2013-11-7 上午10:49:28
	 */
	public String initPlandesignInterferenceDataImportPageAction() {

		initAreaList();
		if (countryAreas != null && countryAreas.size() > 0) {
			List<RnoInterferenceDescriptor> mids = rnoPlanDesignService
					.getRnoInterferenceDescriptorInArea(countryAreas.get(0)
							.getArea_id());
			if (mids != null && mids.size() > 0) {
				tempRnoInterDesc = new ArrayList<RnoInterferenceDescriptor>();
				for (RnoInterferenceDescriptor one : mids) {
					if ("Y".equals(one.getDefaultDescriptor())) {
						systemInterDesc = one;
					} else {
						tempRnoInterDesc.add(one);
					}
				}
			}
		}
		return "success";
	}

	/**
	 * 从加载的干扰分析列表中删除指定的项
	 * 
	 * @author brightming 2013-11-7 下午4:38:51
	 */
	public void removeInterferenceItemFromLoadedListForAjaxAction() {

		// 更新session的数据
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();

		List<PlanConfig> exists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
		if (exists != null && !exists.isEmpty()) {
			int i = 0;
			for (; i < exists.size(); i++) {
				if (exists.get(i) != null
						&& exists.get(i).getConfigId() == loadedConfigId) {
					break;
				}
			}
			if (i < exists.size()) {
				exists.remove(i);
			}
		}
		HttpTools.writeToClient("success");
	}

	/**
	 * 获取指定区域下的干扰配置列表
	 * 
	 * @author brightming 2013-11-7 下午7:11:26
	 */
	public void getInterferenceDescriptorInAreaForAjaxAction() {
		log.info("进入getInterferenceDescriptorInAreaForAjaxAction。areaId="
				+ areaId);
		List<RnoInterferenceDescriptor> mids = rnoPlanDesignService
				.getRnoInterferenceDescriptorInArea(areaId);
		String result = gson.toJson(mids);
		log.info("进入getInterferenceDescriptorInAreaForAjaxAction，返回：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 将选定的干扰配置加载进分析列表
	 * 
	 * @author brightming 2013-11-7 下午6:52:58
	 */
	public void queryAndLoadInterferenceListForAjaxAction() {
		log.info("进入方法：queryAndLoadInterferenceListForAjaxAction。attachParams="
				+ attachParams);

		long id = -1;
		boolean istemp = true;
		if ("true".equals(attachParams.get("default") + "")) {
			istemp = false;
			try {
				id = Long.parseLong(attachParams.get("systemConfigId") + "");
			} catch (Exception e) {
				e.printStackTrace();
				id = -1;
			}
		} else {
			try {
				id = Long.parseLong(attachParams.get("tempConfigId") + "");
			} catch (Exception e) {
				e.printStackTrace();
				id = -1;
			}
		}
		if (id == -1) {
			HttpTools.writeToClient("{'flag':false,'msg':'未选定方案。'}");
			return;
		}
		RnoInterferenceDescriptor rd = rnoPlanDesignService
				.getRnoInterferenceDescriptorById(id);
		if (rd != null) {
			PlanConfig pc = new PlanConfig();
			pc.setCollectTime(sdf.format(rd.getCollectTime()));
			pc.setConfigId(id);
			pc.setName(rd.getName());
			pc.setSelected(false);
			pc.setTemp(istemp);
			pc.setTitle(areaName + "" + attachParams.get("netType") + "小区干扰数据");
			pc.setType("INTERFERENCEDATA");
            pc.setAreaName(areaName);
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			HttpSession session = request.getSession();

			List<PlanConfig> exists = (List<PlanConfig>) session
					.getAttribute(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);

			if (exists == null) {
				exists = new ArrayList<PlanConfig>();
				session.setAttribute(
						RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
						exists);
			}
			boolean ok = false;
			if (!exists.contains(pc)) {
				ok = true;
				exists.add(pc);
			}
			String result = "";
			if (!ok) {
				result = "{'flag':false,'msg':'该配置项已经添加过了。'}";
			} else {
				result = "{'flag':true,'list':" + gson.toJson(exists) + "}";
			}
			HttpTools.writeToClient(result);
		} else {
			HttpTools.writeToClient("{'flag':false,'msg':'不存在指定的干扰配置项！'}");
		}
	}

	/**
	 * 获取小区配置方案
	 * 
	 * @author chao.xj
	 * @date 2013-11-6上午09:58:02
	 */
	public void queryCellConfigForAjaxAction() {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json");
		response.setCharacterEncoding("utf-8");
		List syscoufigurelist = rnoPlanDesignService
				.getSysCoufigureSchemeFromRnoCellDesc(areaId);
		List tempanalyselists = rnoPlanDesignService
				.getTempAnalyseSchemeFromRnoCellDesc(areaId);
		// var b= { "天河": [{ "lng": "1111", "lat":"2222"}],"海珠": [{ "lng":
		// "3333", "lat":"4444"}]};
		// System.out.println("进入getCellConfigureSchemesForAjaxAction");
		// System.out.println(tempanalyselists.size());
		String result = "[{";
		if (syscoufigurelist != null && syscoufigurelist.size() != 0) {
			RnoCellDescriptor sys = (RnoCellDescriptor) syscoufigurelist.get(0);
			result += "'sys':[{'" + sys.getCellDescriptorId() + "':'"
					+ sys.getName() + "'}],";
		}
		result += "'temp':[{";
		if (tempanalyselists != null && tempanalyselists.size() != 0) {
			for (int i = 0; i < tempanalyselists.size(); i++) {
				RnoCellDescriptor temp = (RnoCellDescriptor) tempanalyselists
						.get(i);
				if (i == 0) {
					result += "'" + temp.getCellDescriptorId() + "':'"
							+ temp.getName() + "'";
				} else {
					result += ",'" + temp.getCellDescriptorId() + "':'"
							+ temp.getName() + "'";
				}
			}
		}
		result += "}]";
		result += "}]";
		/*
		 * String result ="[{"; if (syscoufigurelist!=null &&
		 * syscoufigurelist.size()!=0) { String
		 * sys=syscoufigurelist.get(0).toString(); result +=
		 * "'sys':[{'0':'"+sys+"'}],"; } result+="'temp':[{"; if
		 * (tempanalyselists!=null && tempanalyselists.size()!=0) { for (int i =
		 * 0; i < tempanalyselists.size(); i++) { String
		 * temp=tempanalyselists.get(i).toString(); if
		 * (tempanalyselists.size()==1) { result+="'"+i+"':'"+temp+"'"; }else if
		 * (tempanalyselists.size()-1==i) { result+="'"+i+"':'"+temp+"'"; }else
		 * if (tempanalyselists.size()<=2){ result+="'"+i+"':'"+temp+"',"; }
		 * 
		 * } } result+="}]"; result+="}]";
		 */
		// System.out.println(result);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 将选中的小区配置加入分析列表
	 * 
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6下午06:51:44
	 */
	public void addCellConfigToAnalysisListForAjaxAction() {
		// 输入configId
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		PlanConfig planConfig = new PlanConfig();
		HttpSession session = request.getSession();
		List<RnoCellDescriptor> configlists = rnoPlanDesignService
				.getRnoCellDescByConfigId(configId);
		String collectTime = sdf.format(configlists.get(0).getCreateTime());
		String name = configlists.get(0).getName();
		boolean isTemp = false;
		if (configId == systemconfigure) {
			isTemp = true;
		}
		planConfig.setSelected(false);
		planConfig.setType("CELLDATA");
		planConfig.setTemp(isTemp);
		planConfig.setConfigId(configId);
		planConfig.setCollectTime(collectTime);
		planConfig.setName(name);
		String title = chareaname + ennetworkstand + "小区";
		planConfig.setTitle(title);
		planConfig.setAreaName(chareaname);
		String result = "";
		//PLAN_LOAD_CELL_CONFIG_ID
		List<PlanConfig> lists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		if (lists != null) {
			// System.out.println(((List<PlanConfig>)object).contains(planConfig));
			if (!lists.contains(planConfig)) {
				lists.add(planConfig);
			}
			result = gson.toJson(lists);
		} else {
			List<PlanConfig> planConfiglists = new ArrayList<PlanConfig>();
			planConfiglists.add(planConfig);
			//PLAN_LOAD_CELL_CONFIG_ID
			session.setAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, planConfiglists);
			result = gson.toJson(planConfiglists);
		}

		// String string="[{ lng: 1111}]";
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从小区配置加载列表中移除选定的加载项
	 * 
	 * @author chao.xj
	 * @date 2013-11-7下午05:53:52
	 */
	public void removeCellConfigAnalysisItemFromLoadedListForAjaxAction() {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		List<PlanConfig> lists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getConfigId() == configId) {

				lists.remove(i);
			}
		}
		if (lists != null) {
			session.setAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, lists);
		} else {
			session.removeAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		}
		try {
			response.getWriter().write("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description: 初始化Co-bsic分析页面
	 * @author：chao.xj
	 * @return
	 * @return String
	 * @date：Nov 6, 2013 4:13:26 PM
	 */
	public String initCoBsicAnalyseAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * 
	 * 将获得指定区域/配置ID的Cobsic小区数据通过ajax方式返回页面
	 * 
	 * @author chao.xj
	 * @date 2013-11-12下午03:14:09
	 */
	public void getSpecifyAreaCoBsicCellsForAjaxAction() {
		log
		.info("进入getSpecifyAreaCoBsicCellsForAjaxAction　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
				+ reSelected
				+ ":"
				+ areaIds
				+ ":"
				+ configIds
				+ ":"
				+ bcch + ":" + bsic);
		List<CobsicCells> cobsicLists = rnoPlanDesignService
				.saveCobsicCellsByCoBsicKeys(reSelected,areaIds,configIds, bcch, bsic);
		// List<String>
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> cobsicMap = new HashMap<String, Object>();
		CobsicCells cobsicCells;
		// String interfercells[];
		// System.out.println(cobsicLists);
		// System.out.println(cobsicLists.size());
		List<String> interfercells = new ArrayList();
		List<String> cellLists = null;
		if (cobsicLists != null && cobsicLists.size() != 0) {
			cellLists = cobsicLists.get(0).getCells();
		} else {
			map.put("fail", "不存在cobsic小区!");
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		}
		// System.out.println("cobsicLists.get(0).getCells()="+cellLists.size());
		String labels[] = new String[cellLists.size()];
		for (int i = 0; i < cellLists.size(); i++) {
			labels[i] = cellLists.get(i).toString();
			// System.out.println("labels="+labels[i]);
		}

		for (int i = 0; i < labels.length - 1; i++) {
			// 循环比较两两间是否是邻区关系　或　是否是某小区的共同邻区　且　距离小区2000米
			for (int j = i + 1; j < labels.length; j++) {
				// 判断－循环比较两两间是否是邻区关系　queryNcellByoCell
				boolean isNcell = rnoPlanDesignService.queryNcellByoCell(
						labels[i], labels[j]);
				// System.out.println(labels[i]+","+labels[j]+"---isNcell="+isNcell);
				// 判断－两个小区是否是某小区的共同邻区　queryCommonNcellByTwoCell　
				List<RnoNcell> rnoncellList = rnoPlanDesignService
						.queryCommonNcellByTwoCell(labels[i], labels[j]);
				// System.out.println("rnoncellList="+rnoncellList.size());
				// 判断－距离小区2000米 getDistanceBetweenTheCells
				double distance = rnoPlanDesignService
						.getDistanceBetweenTheCells(labels[i], labels[j]);
				// System.out.println("distance="+distance);
				if ((isNcell || (rnoncellList != null && rnoncellList.size() != 0))
						&& distance < 2000) {
					// (co-bsic的小区是邻区　或　co-bsic的小区是某个小区的共同邻区)　且　距离小于2000米
					// D4TNGS1,PAGNPU1是一组干扰小区
					// System.out.println("干扰小区＝"+labels[i]+","+labels[j]+"------------------------");
//					interfercells.add(labels[i] + "," + labels[j]);
					log.info("isNcell:"+isNcell);
					// (co-bsic的小区是邻区　或　co-bsic的小区是某个小区的共同邻区)　且　距离小于2000米
					// D4TNGS1,PAGNPU1是一组干扰小区
					// System.out.println("干扰小区＝"+labels[i]+","+labels[j]+"------------------------");
					//设置是否邻区：在cobsic集合中设置新的属性：isNcell
					//interfercells.add(isNcell+","+labels[i] + "," + labels[j]);
					if (cobsicMap.containsKey(bcch+","+bsic)) {
						//通过bcch,bsic为key从map中获取已存在的对象集合
						cobsicCells=(CobsicCells)cobsicMap.get(bcch+","+bsic);
						//获取cobsic拓展的组合对象集合
						List<CobsicCellsExpand> cobsicexpanList=cobsicCells.getCombinedCells();
						log.info("cobsicCells.getCells():"+cobsicexpanList);
						//新建cobsic拓展对象
						CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
						cellsExpand.setCombinedCell(labels[i] + "," + labels[j]);
						cellsExpand.setWhetherNcell(isNcell);
						cellsExpand.setWhetherComNcell(false);
						for (int l = 0; l < rnoncellList.size(); l++) {
							log.info("获取共同邻区共多少："+rnoncellList.size());
							cellsExpand.setWhetherComNcell(true);
							cellsExpand.setCommonNcell(rnoncellList.get(l).getCell());
							log.info(labels[i]+","+labels[j]+"的共同邻区是："+rnoncellList.get(l).getCell());
						}
						//为bcch,bsic的所在拓展的对象集合内新增对象
						cobsicexpanList.add(cellsExpand);
						cobsicCells.setCombinedCells(cobsicexpanList);
						cobsicMap.put(bcch+","+bsic, cobsicCells);
					}else {
						cobsicCells=new CobsicCells();
						CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
						cellsExpand.setCombinedCell(labels[i] + "," + labels[j]);
						cellsExpand.setWhetherNcell(isNcell);
						for (int l = 0; l < rnoncellList.size(); l++) {
							log.info("获取共同邻区共多少："+rnoncellList.size());
							cellsExpand.setWhetherComNcell(true);
							cellsExpand.setCommonNcell(rnoncellList.get(l).getCell());
							log.info(labels[i]+","+labels[j]+"的共同邻区是："+rnoncellList.get(l).getCell());
						}
						cobsicCells.setBcch(bcch);
						cobsicCells.setBsic(bsic);
						List<CobsicCellsExpand> list=new ArrayList<CobsicCellsExpand>();
						//String combination="{"+labels[i] + "," + labels[j]+":"+cobsicCells.getCommonNcell()+"}";
						//向拓展cobsic集合中注入数据
						list.add(cellsExpand);
						//设置cobsic集合对象
						cobsicCells.setCombinedCells(list);
						//通过bcch,bsic为key向map中增加cobsic对象集合
						cobsicMap.put(bcch+","+bsic, cobsicCells);
					}
				}
			}
		}
		if (!cobsicMap.isEmpty()) {
			String result = gson.toJson(cobsicMap);
			HttpTools.writeToClient(result);
			return;
		}
		if (interfercells != null && interfercells.size() != 0) {
			// System.out.println("interfercells="+interfercells.size());
			map.put("interfercell", interfercells);
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		} else {
			map.put("fail", "不存在cobsic干扰小区!");
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		}

	}

	/**
	 * 
	 * @description: 初始化小区切换统计指标页面
	 * @author：chao.xj
	 * @return
	 * @return String
	 * @date：Nov 6, 2013 4:13:26 PM
	 */
	public String initChannelSwitchStatisticsAction() {
		initAreaList();// 加载区域相关信息
		return "success";
	}

	/**
	 * 初始化ncs导入加载页面
	 * 
	 * @return
	 * @author brightming 2013-11-14 下午5:06:22
	 */
	public String initNcsImportAndLoadPageAction() {
		initAreaList();

		return "success";
	}

	/**
	 * 从加载的ncs列表中，删除指定的一项
	 * 
	 * @author brightming 2013-11-14 下午7:06:27
	 */
	public void removeNcsItemFromLoadedListForAjaxAction() {
		log.info("进入方法：removeNcsItemFromLoadedListForAjaxAction。configIds="
				+ configIds);
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
		removeFromAnalysisInner(planConfigs, configIds,
				RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
		HttpTools.writeToClient("true");
	}

	/**
	 * 获取已经加载的ncs分析列表
	 * 
	 * @author brightming 2013-11-14 下午7:10:07
	 */
	public void getAllLoadedNcsListForAjaxAction() {
		log.info("进入方法：getAllLoadedNcsListForAjaxAction。");
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);

		String result = "[]";
		if (planConfigs != null) {
			result = gson.toJson(planConfigs);
		}
		log.info("getAllLoadedNcsListForAjaxAction输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 加载一项选定的ncs到
	 * 
	 * @author brightming 2013-11-14 下午7:12:44
	 */
	public void addOneNcsItemToListForAjaxAction() {
		log.info("进入方法：addOneNcsItemToListForAjaxAction。configIds=" + configIds
				+ ",areaName=" + areaName);
		if (configIds != null) {
			String[] confids = configIds.split(",");
			if (confids.length == 0) {
				log.error("未提供需要添加到分析列表的ncs id");
				HttpTools.writeToClient("false");
				return;
			}
			String account = (String) SessionService.getInstance()
					.getValueByKey("userId");
			List<Area> tempAreas = rnoCommonService
					.getSpecialLevalAreaByAccount(account, "区/县");
			List<Long> areaIds = new ArrayList<Long>();
			for (Area ta : tempAreas) {
				areaIds.add(ta.getArea_id());
			}
			List<Long> cfids = new ArrayList<Long>();
			for (String d : confids) {
				try {
					cfids.add(Long.parseLong(d));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<RnoNcsDescriptor> dess = rnoPlanDesignService
					.getRnoNcsDescriptorByIds(cfids, areaIds);

			// 转换为planconfig
			if (dess != null && dess.size() > 0) {
				List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
						.getInstance().getValueByKey(
								RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);

				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					ActionContext ctx = ActionContext.getContext();
					HttpServletRequest request = (HttpServletRequest) ctx
							.get(ServletActionContext.HTTP_REQUEST);
					HttpSession session = request.getSession();
					session.setAttribute(
							RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID,
							planConfigs);
				}
				PlanConfig pc = null;
				String startTime = "";
				for (RnoNcsDescriptor rn : dess) {
					pc = new PlanConfig();
					pc.setAreaName(areaName);
					pc.setConfigId(rn.getRnoNcsDescId());
					pc.setSelected(false);
					pc.setTemp(false);
					pc.setType(RnoConstant.DataType.NCS_DATA);
					try {
						startTime = sdf_full.format(rn.getStartTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
					pc.setName(rn.getName());
					pc.setTitle(rn.getName()==null?"未命名":rn.getName());

					pc.setCollectTime(startTime);
					if (!planConfigs.contains(pc)) {
						planConfigs.add(pc);
					}
				}
			}

			HttpTools.writeToClient("true");
		} else {
			HttpTools.writeToClient("false");
		}
	}

	/**
	 * 查询ncs
	 * 
	 * @author brightming 2013-11-14 下午7:33:34
	 */
	public void queryNcsDescriptorByPageWithConditionForAjaxAction() {
		log.info("进入：queryNcsDescriptorByPageWithConditionForAjaxAction。attachParams="
				+ attachParams + ",page=" + page);

		if (page.getPageSize() <= 0) {
			page.setPageSize(25);
		}
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		int cnt = 0;
		if (page.getTotalCnt() == 0) {
			cnt = rnoPlanDesignService.getNcsDescriptorTotalCnt(attachParams);
		}
		List<RnoNcsDescriptorWrap> rnoDescs = rnoPlanDesignService
				.queryNcsDescriptorByPage(page, attachParams);
		String result1 = gson.toJson(rnoDescs);

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setForcedStartIndex(-1);
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(cnt);

		if (cnt % page.getPageSize() == 0) {
			newPage.setTotalPageCnt(cnt / page.getPageSize());
		} else {
			newPage.setTotalPageCnt(cnt / page.getPageSize() + 1);
		}

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'list':" + result1 + "}";
		log.info("退出queryNcsDescriptorByPageWithConditionForAjaxAction。输出："
				+ result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 初始化小区结构数据导入页面
	 * 
	 * @author brightming 2013-11-16 上午11:58:09
	 */
	public String initCellStructImportAndLoadPageAction() {
		initAreaList();
		return "success";
	}

	/**
	 * 获取已经加载的小区结构分析列表
	 * 
	 * @author brightming 2013-11-16 上午11:59:18
	 */
	public void getAllLoadedCellStructListForAjaxAction() {
		log.info("进入方法：getAllLoadedCellStructListForAjaxAction。");
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);

		String result = "[]";
		if (planConfigs != null) {
			result = gson.toJson(planConfigs);
		}
		log.info("getAllLoadedCellStructListForAjaxAction输出：" + result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 从小区结构指标列表中，删除若干项
	 * 
	 * @author brightming 2013-11-16 下午12:00:29
	 */
	public void removeCellStructItemFromLoadedListForAjaxAction() {
		log.info("进入方法：removeCellStructItemFromLoadedListForAjaxAction。configIds="
				+ configIds);
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);
		removeFromAnalysisInner(planConfigs, configIds,
				RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);
		HttpTools.writeToClient("true");
	}

	/**
	 * 通用的移除方法
	 * 
	 * @author brightming 2013-11-18 上午11:59:57
	 */
	public void removeItemFromLoadedListForAjaxAction() {
		log.info("进入方法：removeItemFromLoadedListForAjaxAction。 type=" + type
				+ ",configId=" + configId);
		if (type == null || "".equals(type.trim()) || configId == -1) {
			log.error("removeItemFromLoadedListForAjaxAction未提供必要的参数！");
			HttpTools.writeToClient("{'flag':false,'msg':'未提供必要的参数！'}");
			return;
		}
		String code = "";
		if ("cellConfig".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
		} else if ("cellStruct".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID;
		} else if ("handover".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID;
		} else if ("ncs".equals(type)) {
			code = RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID;
		}

		log.info("要移除的code=" + code);
		if ("".equals(code)) {
			log.error("提供的带移除的类型type=" + type + "不在范围内！");
			HttpTools.writeToClient("{'flag':false,'msg':'未找到待删除的类型！'}");
			return;
		}

		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(code);
		int i = removeFromAnalysisInner(planConfigs, configId + "", code);
		HttpTools.writeToClient("{'flag':true,'msg':''}");
	}

	/**
	 * 从分析列表中删除指定的若干项
	 * 
	 * @param planConfigs
	 * @param configIds
	 * @author brightming 2013-11-16 下午12:02:29
	 */
	private int removeFromAnalysisInner(List<PlanConfig> planConfigs,
			String configIds, String code) {
		if (configIds == null) {
			log.error("未指明需要删除的ncs id");
			HttpTools.writeToClient("false");
			return 0;
		}
		String[] ids = configIds.split(",");
		if (ids.length == 0) {
			log.error("未指明需要删除的ncs id");
			HttpTools.writeToClient("false");
			return 0;
		}
		int cnt = 0;
		if (planConfigs != null && planConfigs.size() > 0) {
			int i = 0;
			String inid = "";
			List<PlanConfig> del = new ArrayList<PlanConfig>();
			for (String id : ids) {
				for (i = 0; i < planConfigs.size(); i++) {
					inid = planConfigs.get(i).getConfigId() + "";
					if (id.equals(inid)) {
						del.add(planConfigs.get(i));
						break;
					}
				}
			}
			if (del.size() > 0) {
				cnt = del.size();
				log.info("移除指定的加载项。");
				for (int j = 0; j < del.size(); j++) {
					planConfigs.remove(del.get(j));
				}
				ActionContext ctx = ActionContext.getContext();
				HttpServletRequest request = (HttpServletRequest) ctx
						.get(ServletActionContext.HTTP_REQUEST);
				HttpSession session = request.getSession();
				session.setAttribute(code, planConfigs);
			}
		}
		log.info("退出方法：removeFromAnalysisInner。");
		return cnt;
	}

	/**
	 * 添加小区结构指标数据到分析列表
	 * 
	 * @author brightming 2013-11-16 下午12:06:27
	 */
	public void addCellStructItemToListForAjaxAction() {
		log.info("进入方法：addCellStructItemToListForAjaxAction。configIds="
				+ configIds + ",areaName=" + areaName);
		if (configIds != null) {
			String[] confids = configIds.split(",");
			if (confids.length == 0) {
				log.error("未提供需要添加到分析列表的小区结构指标 id");
				HttpTools.writeToClient("false");
				return;
			}
			String account = (String) SessionService.getInstance()
					.getValueByKey("userId");
			List<Area> tempAreas = rnoCommonService
					.getSpecialLevalAreaByAccount(account, "区/县");
			List<Long> areaIds = new ArrayList<Long>();
			for (Area ta : tempAreas) {
				areaIds.add(ta.getArea_id());
			}
			List<Long> cfids = new ArrayList<Long>();
			for (String d : confids) {
				try {
					cfids.add(Long.parseLong(d));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<RnoCellStructDesc> dess = rnoPlanDesignService
					.getRnoCellStructDescriptorByIds(cfids, areaIds);

			// 转换为planconfig
			if (dess != null && dess.size() > 0) {
				List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
						.getInstance()
						.getValueByKey(
								RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);

				if (planConfigs == null) {
					planConfigs = new ArrayList<PlanConfig>();
					ActionContext ctx = ActionContext.getContext();
					HttpServletRequest request = (HttpServletRequest) ctx
							.get(ServletActionContext.HTTP_REQUEST);
					HttpSession session = request.getSession();
					session.setAttribute(
							RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID,
							planConfigs);
				}
				PlanConfig pc = null;
				String startTime = "";
				for (RnoCellStructDesc rn : dess) {
					pc = new PlanConfig();
					pc.setAreaName(areaName);
					pc.setConfigId(rn.getRnoCellStructDescId());
					pc.setSelected(false);
					pc.setTemp(false);
					pc.setType(RnoConstant.DataType.CELLSTRUCT_DATA);
					try {
						startTime = sdf_full.format(rn.getTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
					pc.setName(rn.getName());
					pc.setTitle(rn.getName());

					pc.setCollectTime(startTime);
					if (!planConfigs.contains(pc)) {
						planConfigs.add(pc);
					}
				}
			}

			HttpTools.writeToClient("true");
		} else {
			HttpTools.writeToClient("false");
		}
	}

	/**
	 * 查询cellStructdesc
	 * 
	 * @author brightming 2013-11-16 下午12:12:36
	 */
	public void queryCellStructDescriptorByPageWithConditionForAjaxAction() {
		log.info("进入：queryCellStructDescriptorByPageWithConditionForAjaxAction。attachParams="
				+ attachParams + ",page=" + page);

		if (page.getPageSize() <= 0) {
			page.setPageSize(25);
		}
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		int cnt = 0;
		if (page.getTotalCnt() == 0) {
			cnt = rnoPlanDesignService.getCellStructDescTotalCnt(attachParams);
		}
		List<RnoCellStructDescWrap> rnoDescs = rnoPlanDesignService
				.queryCellStructDescByPage(page, attachParams);
		String result1 = gson.toJson(rnoDescs);

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setForcedStartIndex(-1);
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(cnt);

		if (cnt % page.getPageSize() == 0) {
			newPage.setTotalPageCnt(cnt / page.getPageSize());
		} else {
			newPage.setTotalPageCnt(cnt / page.getPageSize() + 1);
		}

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'list':" + result1 + "}";
		log.info("退出queryCellStructDescriptorByPageWithConditionForAjaxAction。输出："
				+ result);
		HttpTools.writeToClient(result);
	}

	/**
	 * 初始化邻区分析页面
	 * 
	 * @return
	 * @author brightming 2013-11-16 下午5:40:47
	 */
	public String initPlanNcellAnalysisPageAction() {
		initAreaList();
		prepareAllLoadedList();
		return "success";
	}

	/**
	 * 分页查询切换统计数据
	 * 
	 * @author chao.xj
	 * @date 2013-11-16下午10:09:33
	 */
	public void queryRnoHandOverDescListByPageAction() {
		log.info("进入方法：queryRnoHandOverDescListByPageAction。分页查询话务数据 page="
				+ page + ",date=" + this.date);
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		List<Map<String, Object>> HODesclists = rnoPlanDesignService
				.queryRnoHandoverDescByAreaAndDate(page, areaId, date, false);
		/*
		 * for (int i = 0; i < lists.size(); i++) { System.out.println(i); Map
		 * map=lists.get(i); System.out.println(map.get("AREA_ID")); }
		 */
		int totalCnt = this.rnoPlanDesignService.getTotalHODescCount(date,
				true, areaId);
		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		//System.out.println(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		Gson gson = new GsonBuilder().create();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("hodescLists", HODesclists);
		resultMap.put("newPage", newPage);
		String result = gson.toJson(resultMap);
		log.info("退出方法：queryRnoHandOverDescListByPageAction。 返回：" + result);
		HttpTools.writeToClient(result);
		// return "success";
	}

	/**
	 * 
	 * 获取各类型的已经加载的列表
	 * 
	 * @author brightming 2013-11-18 上午10:51:35
	 */
	private void prepareAllLoadedList() {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		// 小区配置数据
		cellConfigList = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		// 小区结构数据
		cellStructList = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);

		// 小区切换数据
		cellHandoverList = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);

		// ncs数据
		ncsList = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
		// System.out.println("ncslist===" + ncsList);
	}

	/**
	 * 选中分析列表中的某个项
	 * 
	 * @author brightming 2013-11-18 上午11:15:14
	 */
	public void selectCheckedAnalysisItemForAjaxAction() {
		log.info("进入：selectCheckedAnalysisItemForAjaxAction。type=" + type
				+ ",configId=" + configId);
		if (type == null || "".equals(type) || configId == -1) {
			log.error("selectCheckedAnalysisItemForAjaxAction未提供的必要的参数！");
			HttpTools.writeToClient("{'flag':false,'msg':'未提供必要的参数'}");
			return;
		}
		String code = "";
		if ("cellConfig".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID;
		} else if ("cellStruct".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID;
		} else if ("handover".equals(type)) {
			code = RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID;
		} else if ("ncs".equals(type)) {
			code = RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID;
		}

		log.info("要选择的code=" + code);
		if ("".equals(code)) {
			log.error("提供的待添加的类型type=" + type + "不在范围内！");
			HttpTools.writeToClient("{'flag':false,'msg':'未找到待选择的类型！'}");
			return;
		}
		int cnt = setItemSelected(code, configId);
		if (cnt == 0) {
			HttpTools.writeToClient("{'flag':false,'msg':'指定的配置项不在分析列表中！'}");
		} else {
			HttpTools.writeToClient("{'flag':true,'msg':'选择成功！'}");
		}
	}

	/**
	 * 将指定的分析项置为选中状态
	 * 
	 * @param code
	 * @param configId
	 * @author brightming 2013-11-18 下午1:54:18
	 */
	private int setItemSelected(String code, long configId) {
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(code);
		if (planConfigs == null || planConfigs.size() == 0) {
			return 0;
		}

		int cnt = 0;
		for (PlanConfig pc : planConfigs) {
			if (pc.getConfigId() == configId) {
				pc.setSelected(true);
				cnt++;
			} else {
				pc.setSelected(false);
			}
		}
		return cnt;
	}

	/**
	 * 分析指定小区的邻区情况，以获得冗余邻区、漏定邻区、和正常邻区的信息
	 * 
	 * @author brightming 2013-11-16 下午5:41:28
	 */
	public void analysisNcellOfCellForAjaxAction() {
		log.info("进入方法：analysisNcellOfCellForAjaxAction.cell=" + cell
				+ ",ncsDescIds=" + ncsDescIds + ",handoverDescIds="
				+ handoverDescIds + ",condition=" + condition
				+ ",areaId=" + areaId + ",cityId=" + cityId);

/*		// String result2 =
		// "{'flag':true,'data':{'allNcells':[{'cell':'P2TQDL2','ncell':'P2TDCL1'},{'cell':'P2TQDL2','ncell':'P2TDCL2'},{'cell':'P2TQDL2','ncell':'P2TDNX1'},{'cell':'P2TQDL2','ncell':'P2THAN1'},{'cell':'P2TQDL2','ncell':'P2THAN3'},{'cell':'P2TQDL2','ncell':'P2THSZ2'},{'cell':'P2TQDL2','ncell':'P2TJTG2'},{'cell':'P2TQDL2','ncell':'P2TJTG3'},{'cell':'P2TQDL2','ncell':'P2TLQJN'},{'cell':'P2TQDL2','ncell':'P2TQDL1'}],";
		// result2 +=
		// "'redundantNcells':[{'cell':'P2TQDL2','ncell':'P2TDCL1'},{'cell':'P2TQDL2','ncell':'P2TDCL2'},{'cell':'P2TQDL3','ncell':'P2TDCL4'},{'cell':'P2TQDL3','ncell':'P2TDCL5'}],";
		// result2 +=
		// "'omitNcells':[{'cell':'P2TQDL2','ncell':'P2THAN3'},{'cell':'P2TQDL2','ncell':'P2THSZ2'}]";
		// result2 += "}}";
		//
		// HttpTools.writeToClient(result2);
		// return;
		// long cellConfigId = -1;
		// long ncsDescId = -1;
		// long cellStructDescId = -1;
		// long cellhandDescId = -1;

		String cellConfigArea = "";
		String ncsCellArea = "";
		String cellStructArea = "";
		String handoverArea = "";

		PlanConfig cellConfig = null;
		PlanConfig ncsConfig = null;
		PlanConfig cellStructConfig = null;
		PlanConfig handoverConfig = null;

		boolean cellConfigAllow = false, ncsAllow = false, cellStructAllow = false, handoverAllow = false;

		String err = "";
		// 首先判断有没有选择必要的分析数据
		if (cellConfigId <= 0) {
			err = "未选择小区配置数据！";
		}
		if (cellStructId <= 0) {
			err += " 未选择小区结构指标数据！";
		}
		if (handoverId <= 0) {
			err += " 未选择小区切换指标数据！";
		}
		if (ncsId <= 0) {
			err += "  未选择ncs测量数据！";
		}
		if (!"".equals(err)) {
			log.error(err);
			HttpTools.writeToClient("{'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 小区配置数据
		List<PlanConfig> pcs = null;
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == cellConfigId) {
					cellConfigAllow = true;
					cellConfigArea = pc.getAreaName();
					cellConfig = pc;
					break;
				}
			}
		}
		if (!cellConfigAllow) {
			err = " 选择的小区配置数据不在分析列表内！";
		}

		// 获取选择的小区干扰信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == ncsId) {
					ncsAllow = true;
					ncsConfig = pc;
					ncsCellArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!ncsAllow) {
			err += "  选取的NCS测量数据不在分析列表内！";
		}

		// 获取选择的小区结构信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == cellStructId) {
					cellStructAllow = true;
					cellStructConfig = pc;
					cellStructArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!cellStructAllow) {
			err += "  选取的小区结构指标数据不在分析列表内！";
		}
		// 获取选择的小区切换信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == handoverId) {
					handoverAllow = true;
					handoverConfig = pc;
					handoverArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!handoverAllow) {
			err += "  选取的小区切换统计数据不在分析列表内！";
		}

		// boolean canAnalysis = true;
		// 任何一个欠缺，都只能展示邻区信息，但是不能计算出是否漏定、冗余
		if (cellConfig == null || ncsConfig == null || cellStructConfig == null
				|| handoverConfig == null) {
			// canAnalysis = false;
			// log.warn("缺失必要的数据分析冗余和漏定关系，只能进行普通的邻区查看。cellConfig=" +
			// cellConfigId
			// + ",ncsDescId=" + ncsDescId + ",cellStructDescId="
			// + cellStructDescId + ",cellhandDescId=" + cellhandDescId);

			log.error(err);
			HttpTools.writeToClient("{'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 是否是同个区域的数据
		if (cellConfigArea == null || cellConfigArea.isEmpty()
				|| ncsCellArea == null || ncsCellArea.isEmpty()
				|| cellStructArea == null || cellStructArea.isEmpty()
				|| handoverArea == null || handoverArea.isEmpty()
				|| !cellConfigArea.equals(ncsCellArea)
				|| !cellConfigArea.equals(cellStructArea)
				|| !cellConfigArea.equals(handoverArea)) {
			err = "所用来分析的小区配置数据、小区结构指标数据、小区切换数据、ncs数据不是同个区域下的，无法进行分析！";
			log.error(err);
			HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 获取邻区
		List<RnoNcell> rnoNcells = rnoResourceManagerService
				.getNcellsByCell(cell);

		condition.setMinDetectRatio(condition.getMinDetectRatio() / 100);
		// 获取冗余
		List<RedundantNCell> redundantNcells = rnoPlanDesignService
				.getRedundantNcellOfCell(cell, cellConfig, handoverConfig,
						cellStructConfig, ncsConfig, condition);
		// 获取漏定
		List<RedundantNCell> oMitNcells = rnoPlanDesignService
				.getOmitNcellOfCell(cell, cellConfig, handoverConfig,
						cellStructConfig, ncsConfig, condition);

		String result = "{'flag':true,'data':{'allNcells':"
				+ gson.toJson(rnoNcells) + ",";
		result += "'redundantNcells':" + gson.toJson(redundantNcells) + ",";
		result += "'omitNcells':" + gson.toJson(oMitNcells);
		result += "}}";
*/
		if(null==ncsDescIds || 0==ncsDescIds.size()){
			String err = "ncs数据不能为空";
			log.error(err);
			HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
			return;	
		}
		//判断是不是全市分析
		if(0>=areaId)
		{
			areaId = cityId;
		}
		condition.setMinDetectRatio(condition.getMinDetectRatio() / 100);
		List<RnoNcell> rnoNcells = rnoResourceManagerService.getNcellsByCell(cell);
		//获取冗余邻区 和漏定邻区
		Map<String, List<RedundantNCell>> redundantAndOmitNcells = null;
		if(taskId != -1)
		{
			List<Object> cellResult = getNcsReportDataForAjaxAction();
			if(null==cellResult || 0==cellResult.size())
			{
				String err = "小区结构数据不能为空";
				log.error(err);
				HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
				return;	
			}
			//redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcellofCell(cell, ncsDescIds, cellResult, handoverDescIds, condition, areaId);
			redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcells(cell, ncsDescIds, cellResult, handoverDescIds, condition, areaId);
		}else{
			//redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcellofCell(cell, ncsDescIds, handoverDescIds, condition, areaId, cityId);
			redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcells(cell, ncsDescIds, handoverDescIds, condition, areaId);
		}
		
		String result = "{'flag':true,'data':{'allNcells':"
				+ gson.toJson(rnoNcells) + ",";
		result += "'redundantNcells':" + gson.toJson(redundantAndOmitNcells.get("redundantNcells")) + ",";
		result += "'omitNcells':" + gson.toJson(redundantAndOmitNcells.get("omitNcells"));
		result += "}}";
		log.info("退出方法：analysisNcellOfCellForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);

	}

	/**
	 * 添加切换统计数据分析列表
	 * 
	 * @author chao.xj
	 * @date 2013-11-16下午10:09:33
	 */
	public void appendRnoHandOverDescListForAjaxAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();

		String items[] = loaditem.split(",");
		List<RnoHandoverDescriptor> hodesclists = rnoPlanDesignService
				.getRnoHandoverDescriptorByHoDescId(items);
		// /
		List<PlanConfig> planconfiglists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		// PlanConfig planConfig=null;
		for (int i = 0; i < hodesclists.size(); i++) {
			RnoHandoverDescriptor oneRnoHODesc = hodesclists.get(i);
			long areaid = oneRnoHODesc.getAreaId();
			SysArea area = rnoPlanDesignService.getAreaobjByareaId(areaid);
			String areaname = area.getName();
			boolean isTemp = false;
			PlanConfig planConfig = new PlanConfig();
			// System.out.println("planconfig=="+planConfig);
			planConfig.setSelected(false);
			planConfig.setType(RnoConstant.DataType.HAND_OVER_DATA);
			planConfig.setTemp(isTemp);
			planConfig.setConfigId(oneRnoHODesc.getRnoHandoverDescId());
			String dateString = sdf_full.format(oneRnoHODesc.getStaticsTime());
			planConfig.setCollectTime(dateString);
			planConfig.setName(areaname + dateString);
			planConfig.setTitle(areaname + dateString);
			planConfig.setAreaName(areaname);

			if (planconfiglists == null) {
				planconfiglists = new ArrayList<PlanConfig>();
				session.setAttribute(
						RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID,
						planconfiglists);
			}
			// if (planconfiglists != null) {
			// for (int j = 0; j < planconfiglists.size(); j++) {
			if (!planconfiglists.contains(planConfig)) {
				planconfiglists.add(planConfig);
			}
			// }
			// } else {
			// List<PlanConfig> pList = new ArrayList<PlanConfig>();
			// pList.add(planConfig);
			// session.setAttribute(
			// RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID,
			// pList);
			// }

		}
		String result = "[{'res':'添加完成'}]";
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void getCoBsicInterferCellForAjaxAction() {

	}

	/**
	 * 分析全网的冗余、漏定邻区
	 * 
	 * @author brightming 2013-11-20 上午11:19:28
	 */
	//Liang YJ 2014-4-30 修改
	public void analysisAllNcellInWholeNetForAjaxAction() {
		log.info("进入方法：analysisAllNcellInWholeNetForAjaxAction。ncsDescIds="
				+ ncsDescIds + ",handoverDescIds=" + handoverDescIds + ",condition="
				+ condition+",areaId="+areaId+",cityId="+cityId);
/*		HttpServletRequest request = ServletActionContext.getRequest ();
		String str = request.getParameter("ncsDescIds");
		Object strName = request.getParameterNames();*/
		/*String cellConfigArea = "";
		String ncsCellArea = "";
		String cellStructArea = "";
		String handoverArea = "";

		PlanConfig cellConfig = null;
		PlanConfig ncsConfig = null;
		PlanConfig cellStructConfig = null;
		PlanConfig handoverConfig = null;

		boolean cellConfigAllow = false, ncsAllow = false, cellStructAllow = false, handoverAllow = false;*/

		// String result2 =
		// "{'flag':true,'data':{'allNcells':[{'cell':'P2TQDL2','ncell':'P2TDCL1'},{'cell':'P2TQDL2','ncell':'P2TDCL2'},{'cell':'P2TQDL2','ncell':'P2TDNX1'},{'cell':'P2TQDL2','ncell':'P2THAN1'},{'cell':'P2TQDL2','ncell':'P2THAN3'},{'cell':'P2TQDL2','ncell':'P2THSZ2'},{'cell':'P2TQDL2','ncell':'P2TJTG2'},{'cell':'P2TQDL2','ncell':'P2TJTG3'},{'cell':'P2TQDL2','ncell':'P2TLQJN'},{'cell':'P2TQDL2','ncell':'P2TQDL1'}],";
		// result2 +=
		// "'redundantNcells':[{'cell':'P2TQDL2','ncell':'P2TDCL1'},{'cell':'P2TQDL2','ncell':'P2TDCL2'},{'cell':'P2TQDL3','ncell':'P2TDCL4'},{'cell':'P2TQDL3','ncell':'P2TDCL5'}],";
		// result2 +=
		// "'omitNcells':[{'cell':'P2TQDL2','ncell':'P2THAN3'},{'cell':'P2TQDL2','ncell':'P2THSZ2'},{'cell':'P2TQDL2','ncell':'P2TQDCW'}]";
		// result2 += "}}";
		//
		// HttpTools.writeToClient(result2);
		// return;

		/*String err = ""; // 首先判断有没有选择必要的分析数据
		if (cellConfigId <= 0) {
			err = "未选择小区配置数据！";
		}
		if (cellStructId <= 0) {
			err += " 未选择小区结构指标数据！";
		} //
		if (handoverId <= 0) { //
			err += " 未选择小区切换指标数据！"; //
		}
		if (ncsId <= 0) {
			err += "  未选择ncs测量数据！";
		}
		if (!"".equals(err)) {
			log.error(err);
			HttpTools.writeToClient("{'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 小区配置数据
		List<PlanConfig> pcs = null;
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == cellConfigId) {
					cellConfigAllow = true;
					cellConfigArea = pc.getAreaName();
					cellConfig = pc;
					break;
				}
			}
		}
		if (!cellConfigAllow) {
			err = " 选择的小区配置数据不在分析列表内！";
		}

		// 获取选择的小区干扰信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.NCS_LOAD_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == ncsId) {
					ncsAllow = true;
					ncsConfig = pc;
					ncsCellArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!ncsAllow) {
			err += "  选取的NCS测量数据不在分析列表内！";
		}

		// 获取选择的小区结构信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_STRUCT_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == cellStructId) {
					cellStructAllow = true;
					cellStructConfig = pc;
					cellStructArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!cellStructAllow) {
			err += "  选取的小区结构指标数据不在分析列表内！";
		}

		// 获取选择的小区切换信息
		pcs = (List<PlanConfig>) SessionService.getInstance().getValueByKey(
				RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		if (pcs != null && pcs.size() > 0) {
			for (PlanConfig pc : pcs) {
				if (pc.getConfigId() == handoverId) {
					handoverAllow = true;
					handoverConfig = pc;
					handoverArea = pc.getAreaName();
					break;
				}
			}
		}
		if (!handoverAllow) {
			err += "  选取的小区切换统计数据不在分析列表内！";
		}

		// 无法进行
		if (cellConfig == null || ncsConfig == null || cellStructConfig == null
				|| handoverConfig == null) {
			log.error(err);
			HttpTools.writeToClient("{'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 是否是同个区域的数据
		if (cellConfigArea == null || cellConfigArea.isEmpty()
				|| ncsCellArea == null || ncsCellArea.isEmpty()
				|| cellStructArea == null || cellStructArea.isEmpty()
				|| !cellConfigArea.equals(ncsCellArea)
				|| !cellConfigArea.equals(cellStructArea)
				|| !cellConfigArea.equals(handoverArea)) {
			err = "所用来分析的小区配置数据、小区结构指标数据、ncs数据、小区切换数据不是同个区域下的，无法进行分析！";
			log.error(err);
			HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
			return;
		}

		// 获取全网冗余邻区
		List<RedundantNCell> redundantNcells = rnoPlanDesignService
				.getAllRedundantCellsByCondition(cellConfig, handoverConfig,
						cellStructConfig, ncsConfig, condition); // 获取全网漏定邻区
		List<RedundantNCell> oMitNcells = rnoPlanDesignService
				.getAllOmitCellsByCondition(cellConfig, cellStructConfig,
						ncsConfig, condition);

		String result = "{'flag':true,'data':{";
		result += "'redundantNcells':" + gson.toJson(redundantNcells) + ",";
		result += "'omitNcells':" + gson.toJson(oMitNcells);
		result += "}}";
*/
		if(null==ncsDescIds || 0==ncsDescIds.size()){
			String err = "ncs数据不能为空";
			log.error(err);
			HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
			return;	
		}
		//判断是不是全市分析
		if(0>=areaId)
		{
			areaId = cityId;
		}
		condition.setMinDetectRatio(condition.getMinDetectRatio() / 100);
		Map<String, List<RedundantNCell>> redundantAndOmitNcells = null;
		if(taskId != -1)
		{
			List<Object> cellResult = getNcsReportDataForAjaxAction();
			if(null==cellResult || 0==cellResult.size())
			{
				String err = "小区结构数据不能为空";
				log.error(err);
				HttpTools.writeToClient("'flag':false,'msg':'" + err + "'}");
				return;	
			}
			//redundantAndOmitNcells = rnoPlanDesignService.getAllRedundantAndOmitCellsByCondition(ncsDescIds, cellResult, handoverDescIds, condition, areaId);
			redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcells(cell, ncsDescIds, cellResult, handoverDescIds, condition, areaId);
		}else{
			//redundantAndOmitNcells = rnoPlanDesignService.getAllRedundantAndOmitCellsByCondition(ncsDescIds, handoverDescIds, condition, areaId,cityId);
			redundantAndOmitNcells = rnoPlanDesignService.getRedundantAndOmitNcells(cell,ncsDescIds, handoverDescIds, condition, areaId);
		}
		String result = "{'flag':true,'data':{";
		result += "'redundantNcells':" + gson.toJson(redundantAndOmitNcells.get("redundantNcells")) + ",";
		result += "'omitNcells':" + gson.toJson(redundantAndOmitNcells.get("omitNcells"));
		result += "}}";
		log.info("退出方法：analysisAllNcellInWholeNetForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
		
	}
	/**
	 * 刷新获取小区配置分析列表
	 * 
	 * @author chao.xj
	 * @date 2013-12-9下午04:25:39
	 */
	public void getCellConfigureAnalysisListForAjaxAction() {
		log.info("进入方法：getCellConfigureAnalysisListForAjaxAction。");
		List<PlanConfig> planConfigs = null;
		planConfigs = (List<PlanConfig>) SessionService
				.getInstance()
				.getValueByKey(
						RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		if (planConfigs == null) {
			planConfigs = Collections.EMPTY_LIST;
		}
		String result = gson.toJson(planConfigs);
		log.info("退出方法：getCellConfigureAnalysisListForAjaxAction。输出："
				+ result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 刷新获取小区切换分析列表
	 * 
	 * @author chao.xj
	 * @date 2013-12-9下午04:25:39
	 */
	public void getCellHandOverAnalysisListForAjaxAction() {
		log.info("进入方法：getCellHandOverAnalysisListForAjaxAction。");
		List<PlanConfig> planConfigs = null;
		planConfigs = (List<PlanConfig>) SessionService
				.getInstance()
				.getValueByKey(
						RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		if (planConfigs == null) {
			planConfigs = Collections.EMPTY_LIST;
		}
		String result = gson.toJson(planConfigs);
		log.info("退出方法：getCellHandOverAnalysisListForAjaxAction。输出："
				+ result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 从小区切换统计加载列表中移除选定的加载项
	 * 
	 * @author chao.xj
	 * @date 2013-11-7下午05:53:52
	 */
	public void removeChannelSwitchAnalysisItemFromLoadedListForAjaxAction() {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		List<PlanConfig> lists = (List<PlanConfig>) session
				.getAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		//System.out.println(configId);
		//System.out.println(lists);
		for (int i = 0; i < lists.size(); i++) {
			//System.out.println(lists.get(i));
			if (lists.get(i).getConfigId() == configId) {

				lists.remove(i);
			}
		}
		if (lists != null) {
			session.setAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID, lists);
		} else {
			session.removeAttribute(RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		}
		try {
			response.getWriter().write("success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 分页查询小区配置描述数据
	 * 
	 * @author chao.xj
	 * @date 2013-11-16下午10:09:33
	 */
	public void queryCellConfigureDescListByPageAction() {
		log.info("进入方法：queryCellConfigureDescListByPageAction。分页查询小区配置描述数据 page="
				+ page+" areaId="+areaIds+" schemeName="+schemeName);
		//System.out.println(page.getCurrentPage()+" "+areaId+" "+schemeName);
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		List<Map<String, Object>> cellDesclists = rnoPlanDesignService
				.queryCellConfigureDescByAreaAndScheme(page, areaIds, schemeName,sysDefault);
		/*
		 * for (int i = 0; i < lists.size(); i++) { System.out.println(i); Map
		 * map=lists.get(i); System.out.println(map.get("AREA_ID")); }
		 */
		log.info("schemeName"+schemeName);
		int totalCnt = this.rnoPlanDesignService.getTotalCellDescCountByArea(areaId, schemeName, sysDefault);
		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		Gson gson = new GsonBuilder().create();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("celldescLists", cellDesclists);
		resultMap.put("newPage", newPage);
		String result = gson.toJson(resultMap);
		log.info("退出方法：queryCellConfigureDescListByPageAction。 返回：" + result);
		HttpTools.writeToClient(result);
		// return "success";
	}
	/**
	 * 改造：将选中的小区配置描述加入分析列表
	 * 
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6下午06:51:44
	 */
	public void addCellConfigDescToAnalysisListForAjaxAction() {
		// 输入configId
		//System.out.println(configIds);
		log.info("进入addCellConfigDescToAnalysisListForAjaxAction configIds:" +configIds);
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		List<Map<String, Object>> configlists = rnoPlanDesignService
				.getCellConfigureDescByConfigIds(configIds);
		//System.out.println(configlists.size());
		String result = "";
		
		for (int i = 0; i < configlists.size(); i++) {
			PlanConfig planConfig = new PlanConfig();
			String CELL_DESCRIPTOR_ID=configlists.get(i).get("CELL_DESCRIPTOR_ID").toString();
			String NAME=configlists.get(i).get("NAME").toString();
			String CREATE_TIME=configlists.get(i).get("CREATE_TIME").toString();
			String AREA_ID=configlists.get(i).get("AREA_ID").toString();
			String AREANAME=configlists.get(i).get("AREANAME").toString();
			String TEMP_STORAGE=configlists.get(i).get("TEMP_STORAGE").toString();
			log.info("TEMP_STORAGE:"+TEMP_STORAGE);
			//System.out.println(CELL_DESCRIPTOR_ID);
			//System.out.println(CELL_DESCRIPTOR_ID+" "+NAME+" "+CREATE_TIME+" "+AREA_ID+" "+AREANAME);
			//planConfig.setTitle(AREANAME+CREATE_TIME);
			planConfig.setTitle(AREANAME);
			planConfig.setName(NAME);
			planConfig.setCollectTime(CREATE_TIME);
			planConfig.setConfigId(Long.parseLong(CELL_DESCRIPTOR_ID));
			planConfig.setAreaName(AREANAME);
			log.info("TEMP_STORAGE==Y?true:false:"+(TEMP_STORAGE.equals("Y")?true:false));
			planConfig.setTemp(TEMP_STORAGE.equals("Y")?true:false);
			//PLAN_LOAD_CELL_CONFIG_ID
			List<PlanConfig> lists = (List<PlanConfig>) session
			.getAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
			if (lists != null) {
				// System.out.println(((List<PlanConfig>)object).contains(planConfig));
				if (!lists.contains(planConfig)) {
					lists.add(planConfig);
				}
				if (i==configlists.size()-1) {
					result = gson.toJson(lists);
				}
			} else {
				List<PlanConfig> planConfiglists = new ArrayList<PlanConfig>();
				planConfiglists.add(planConfig);
				//PLAN_LOAD_CELL_CONFIG_ID
				session.setAttribute(RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, planConfiglists);
				result = gson.toJson(planConfiglists);
			}
		}
		
		// String string="[{ lng: 1111}]";
		try {
			//System.out.println(result);
			log.info("退出addCellConfigDescToAnalysisListForAjaxAction result:"+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 刷新按钮查询统计阶段数据从分析列表
	 * 
	 * @author chao.xj
	 * @date 2013-12-12上午11:00:59
	 */
	public void refreshQueryAndLoadInterferenceListForAjaxAction() {
		log.info("进入方法：refreshQueryAndLoadInterferenceListForAjaxAction。");

		List<StsConfig> exists = (List<StsConfig>) SessionService.getInstance()
				.getValueByKey(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
		String result="";
		if (exists == null) {
			exists=Collections.EMPTY_LIST;
			result = gson.toJson(exists);
		}else {
			result = gson.toJson(exists);
		}
		log.info("result="+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 改造：分页查询干扰指标描述数据
	 * 
	 * @author chao.xj
	 * @date 2013-11-16下午10:09:33
	 */
	public void queryInterferenceDescListByPageAction() {
		log.info("进入方法：queryInterferenceDescListByPageAction。分页查询小区配置描述数据 page="
				+ page+" areaId="+areaId);
		//System.out.println(page.getCurrentPage()+" "+areaId+" "+schemeName);
		//System.out.println(sysDefault);
		
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		/*
		boolean isDefaultFlag=false;
		if (isDefault.equals("true")) {
			isDefaultFlag=true;
		}*/
		//System.out.println("action isDefault:"+isDefault);
		List<Map<String, Object>> interferenceDesclists = rnoPlanDesignService.queryInterferenceDataByPage(page, areaId, attachParams, sysDefault, schemeName, false);
		/*
		 * for (int i = 0; i < lists.size(); i++) { System.out.println(i); Map
		 * map=lists.get(i); System.out.println(map.get("AREA_ID")); }
		 */
		int totalCnt = this.rnoPlanDesignService.getTotalInterferenceCount(areaId, attachParams, sysDefault, schemeName, true);
		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		Gson gson = new GsonBuilder().create();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("interferencedescLists", interferenceDesclists);
		resultMap.put("newPage", newPage);
		String result = gson.toJson(resultMap);
		log.info("退出方法：queryInterferenceDescListByPageAction。 返回：" + result);
		HttpTools.writeToClient(result);
		// return "success";
	}
	/**
	 * 改造：将选中的干扰指标描述加入分析列表
	 * 
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6下午06:51:44
	 */
	public void addInterferenceDescToAnalysisListForAjaxAction() {
		// 输入configId
		//System.out.println(configIds);
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		List<Map<String, Object>> configlists = rnoPlanDesignService.getInterferenceDataDescByConfigIds(configIds);
		//System.out.println(configlists.size());
		String result = "";
		
		for (int i = 0; i < configlists.size(); i++) {
			PlanConfig planConfig = new PlanConfig();
			String INTER_DESC_ID=configlists.get(i).get("INTER_DESC_ID").toString();
			String NAME=configlists.get(i).get("NAME").toString();
			String COLLECT_TIME=configlists.get(i).get("COLLECT_TIME").toString();
			String NET_TYPE=configlists.get(i).get("NET_TYPE").toString();
			String AREANAME=configlists.get(i).get("AREANAME").toString();
			String TEMP_STORAGE=configlists.get(i).get("TEMP_STORAGE").toString();
			//System.out.println(CELL_DESCRIPTOR_ID);
			//System.out.println(CELL_DESCRIPTOR_ID+" "+NAME+" "+CREATE_TIME+" "+AREA_ID+" "+AREANAME);
			planConfig.setTitle(AREANAME+COLLECT_TIME);
			planConfig.setName(NAME);
			planConfig.setCollectTime(COLLECT_TIME);
			planConfig.setConfigId(Long.parseLong(INTER_DESC_ID));
			planConfig.setAreaName(AREANAME);
			planConfig.setTemp(TEMP_STORAGE.equals("Y")?true:false);
			//PLAN_LOAD_CELL_CONFIG_ID
			List<PlanConfig> lists = (List<PlanConfig>) session
			.getAttribute(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
			if (lists != null) {
				// System.out.println(((List<PlanConfig>)object).contains(planConfig));
				if (!lists.contains(planConfig)) {
					lists.add(planConfig);
				}
				if (i==configlists.size()-1) {
					result = gson.toJson(lists);
				}
			} else {
				List<PlanConfig> planConfiglists = new ArrayList<PlanConfig>();
				planConfiglists.add(planConfig);
				//PLAN_LOAD_CELL_CONFIG_ID
				session.setAttribute(RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID, planConfiglists);
				result = gson.toJson(planConfiglists);
			}
		}
		
		// String string="[{ lng: 1111}]";
		try {
			//System.out.println(result);
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 从加载的小区配置Ccs列表中，删除指定的若干项
	 * @author chao.xj
	 * @date 2013-12-18上午11:03:10
	 */
	public void removeCcsItemFromLoadedListForAjaxAction() {
		log.info("进入方法：removeCcsItemFromLoadedListForAjaxAction。configIds="
				+ configIds);
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		removeFromAnalysisInner(planConfigs, configIds,
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID);
		HttpTools.writeToClient("true");
	}
	/**
	 * 改造：从加载的分析列表中删除干扰项
	 * 
	 * @author chao.xj
	 * @date 2013-12-18下午01:48:39
	 */
	public void removeInterferenceItemsFromLoadedListForAjaxAction() {
		log.info("进入方法：removeInterferenceItemsFromLoadedListForAjaxAction。configIds="
				+ configIds);
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
		removeFromAnalysisInner(planConfigs, configIds,
				RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID);
		HttpTools.writeToClient("true");
	}
	/**
	 * 
	 * 改造：从小区切换加载列表中，删除指定的若干项
	 * @author chao.xj
	 * @date 2013-12-18下午02:29:06
	 */
	public void removeHandOverItemFromLoadedListForAjaxAction() {
		log.info("进入方法：removeHandOverItemFromLoadedListForAjaxAction。configIds="
				+ configIds);
		List<PlanConfig> planConfigs = (List<PlanConfig>) SessionService
				.getInstance().getValueByKey(
						RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		removeFromAnalysisInner(planConfigs, configIds,
				RnoConstant.SessionConstant.CELL_HANDOVER_CONFIG_ID);
		HttpTools.writeToClient("true");
	}
	/**
	 * 
	 * 将获得指定区域/配置ID的全网Cobsic小区数据通过ajax方式返回页面
	 * 
	 * @author chao.xj
	 * @date 2013-11-12下午03:14:09
	 */
	public void getSpecifyAreaWholeNetCoBsicCellsForAjaxAction() {
		log
		.info("进入getSpecifyAreaWholeNetCoBsicCellsForAjaxAction　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
				+ reSelected
				+ ":"
				+ areaIds
				+ ":"
				+ configIds);
		List<CobsicCells> cobsicLists = rnoPlanDesignService
				.saveCobsicCellsByCoBsicKeys(reSelected,areaIds,configIds);
		// List<String>
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> cobsicMap = new HashMap<String, Object>();
		CobsicCells cobsicCells;
		// String interfercells[];
		// System.out.println(cobsicLists);
		// System.out.println(cobsicLists.size());
		List<String> interfercells = new ArrayList();
		List<String> cellLists = null;
		String bcch="";
		String bsic="";
		if (cobsicLists != null && cobsicLists.size() != 0) {
			//最外范围循环 bcch,bsic,list<label>
			for (int k = 0; k < cobsicLists.size(); k++) {
				cellLists = cobsicLists.get(k).getCells();
				bcch=cobsicLists.get(k).getBcch()+"";
				bsic=cobsicLists.get(k).getBsic()+"";
				cobsicLists.get(k).getBsic();
				// System.out.println("cobsicLists.get(0).getCells()="+cellLists.size());
				String labels[] = new String[cellLists.size()];
				//某个bcch,bsic对下的label集合对象以label数组方式存储起来
				for (int i = 0; i < cellLists.size(); i++) {
					labels[i] = cellLists.get(i).toString();
					// System.out.println("labels="+labels[i]);
				}
				//某个bcch,bsic对下的label集合对象循环	
				for (int i = 0; i < labels.length - 1; i++) {
					// 循环比较两两间是否是邻区关系　或　是否是某小区的共同邻区　且　距离小区2000米
					for (int j = i + 1; j < labels.length; j++) {
						// 判断－循环比较两两间是否是邻区关系　queryNcellByoCell
						boolean isNcell = rnoPlanDesignService.queryNcellByoCell(
								labels[i], labels[j]);
						// System.out.println(labels[i]+","+labels[j]+"---isNcell="+isNcell);
						// 判断－两个小区是否是某小区的共同邻区　queryCommonNcellByTwoCell　
						List<RnoNcell> rnoncellList = rnoPlanDesignService
								.queryCommonNcellByTwoCell(labels[i], labels[j]);
						// System.out.println("rnoncellList="+rnoncellList.size());
						// 判断－距离小区2000米 getDistanceBetweenTheCells
						double distance = rnoPlanDesignService
								.getDistanceBetweenTheCells(labels[i], labels[j]);
						// System.out.println("distance="+distance);
						if ((isNcell || (rnoncellList != null && rnoncellList.size() != 0))
								&& distance < 2000) {
							log.info("isNcell:"+isNcell);
							// (co-bsic的小区是邻区　或　co-bsic的小区是某个小区的共同邻区)　且　距离小于2000米
							// D4TNGS1,PAGNPU1是一组干扰小区
							// System.out.println("干扰小区＝"+labels[i]+","+labels[j]+"------------------------");
							//设置是否邻区：在cobsic集合中设置新的属性：isNcell
							//interfercells.add(isNcell+","+labels[i] + "," + labels[j]);
							if (cobsicMap.containsKey(bcch+","+bsic)) {
								//通过bcch,bsic为key从map中获取已存在的对象集合
								cobsicCells=(CobsicCells)cobsicMap.get(bcch+","+bsic);
								//获取cobsic拓展的组合对象集合
								List<CobsicCellsExpand> cobsicexpanList=cobsicCells.getCombinedCells();
								log.info("cobsicCells.getCells():"+cobsicexpanList);
								//新建cobsic拓展对象
								CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
								cellsExpand.setCombinedCell(labels[i] + "," + labels[j]);
								cellsExpand.setWhetherNcell(isNcell);
								cellsExpand.setWhetherComNcell(false);
								for (int l = 0; l < rnoncellList.size(); l++) {
									log.info("获取共同邻区共多少："+rnoncellList.size());
									cellsExpand.setWhetherComNcell(true);
									cellsExpand.setCommonNcell(rnoncellList.get(l).getCell());
									log.info(labels[i]+","+labels[j]+"的共同邻区是："+rnoncellList.get(l).getCell());
								}
								//为bcch,bsic的所在拓展的对象集合内新增对象
								cobsicexpanList.add(cellsExpand);
								cobsicCells.setCombinedCells(cobsicexpanList);
								cobsicMap.put(bcch+","+bsic, cobsicCells);
							}else {
								cobsicCells=new CobsicCells();
								CobsicCellsExpand cellsExpand=new CobsicCellsExpand();
								cellsExpand.setCombinedCell(labels[i] + "," + labels[j]);
								cellsExpand.setWhetherNcell(isNcell);
								for (int l = 0; l < rnoncellList.size(); l++) {
									log.info("获取共同邻区共多少："+rnoncellList.size());
									cellsExpand.setWhetherComNcell(true);
									cellsExpand.setCommonNcell(rnoncellList.get(l).getCell());
									log.info(labels[i]+","+labels[j]+"的共同邻区是："+rnoncellList.get(l).getCell());
								}
								cobsicCells.setBcch(Long.parseLong(bcch));
								cobsicCells.setBsic(bsic);
								List<CobsicCellsExpand> list=new ArrayList<CobsicCellsExpand>();
								//String combination="{"+labels[i] + "," + labels[j]+":"+cobsicCells.getCommonNcell()+"}";
								//向拓展cobsic集合中注入数据
								list.add(cellsExpand);
								//设置cobsic集合对象
								cobsicCells.setCombinedCells(list);
								//通过bcch,bsic为key向map中增加cobsic对象集合
								cobsicMap.put(bcch+","+bsic, cobsicCells);
							}
						}
					}
				}
			}
			
		} else {
			map.put("fail", "不存在cobsic小区!");
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		}
		if (!cobsicMap.isEmpty()) {
			String result = gson.toJson(cobsicMap);
			HttpTools.writeToClient(result);
			return;
		}
		if (interfercells != null && interfercells.size() != 0) {
			// System.out.println("interfercells="+interfercells.size());
			map.put("interfercell", interfercells);
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		} else {
			map.put("fail", "不存在cobsic干扰小区!");
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		}
		
	}
	/**
	 * 获取指定小区的干扰分析数据
	 * @title 
	 * @author chao.xj
	 * @date 2014-4-25上午10:05:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getSpecifyCellInterferenceAnalysisAction() {
		Map<String, FreqInterIndex>  freqindexMap=rnoPlanDesignService.getSpecifyCellInterferenceAnalysis(cell,selectId);
		if (!freqindexMap.isEmpty()) {
			String result = gson.toJson(freqindexMap);
			HttpTools.writeToClient(result);
			return;
		}else {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("fail", "不存在该小区的NCS干扰数据!");
			String result = gson.toJson(map);
			// String result = gson.toJson(cobsicLists);
			HttpTools.writeToClient(result);
			return;
		}
	}
	/**
	 * 分页查询总分析ncs cell信息通过NCS描述ID
	 * @title 
	 * @author chao.xj
	 * @date 2014-4-28上午09:48:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getTotalAnalysisGisCellByPageWithNcsDescIdForAjaxAction() {
		log.info("进入：getTotalAnalysisGisCellByPageWithNcsDescIdForAjaxAction。ncsId=" + ncsId
				+ ",page=" + page);

		if (page == null) {
			log.error("方法getTotalAnalysisGisCellByPageWithNcsDescIdForAjaxAction的page参数为空！");
			HttpTools.writeToClient("[]");
			return;
		}
		Page newPage = page.copy();
		List<Map<String, Object>> celldatainter = rnoPlanDesignService.getTotalAnalysisGisCellByNcsDescIdForPage(ncsId, newPage);
				
		String result1 = gson.toJson(celldatainter);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{\"page\":" + pstr + ",\"data\":" + result1 + "}";
		log.info("退出getTotalAnalysisGisCellByPageWithNcsDescIdForAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 通过ncsid获取ncs描述表的区域信息[city,area]
	 * @title 
	 * @author chao.xj
	 * @date 2014-4-29下午01:52:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getNcsAreaInfoByNcsIdForAjaxAction() {
		
		List<Map<String, Object>> list=rnoPlanDesignService.getNcsAreaInfoByNcsId(selectId);
		String result=gson.toJson(list);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 从excel文件中获取ncs分析报告的数据
	 * @author Liang YJ
	 * 2014-4-28 上午9:42:58
	 * @version 1.2.2
	 * @return List<Map<String,Object>>
	 * 每条记录封装成Map<String,Object>对象
	 * 
	 */
	private List<Object> getNcsReportDataForAjaxAction() {
		String dataType = "cellres";
		RnoTask task = null;
		task = rnoStructureService.getTaskById(taskId);
		if (task == null) {
			HttpTools.writeToClient("[]");
			return null;
		}
		DateUtil dateUtil=new DateUtil();
		Timestamp ts = task.getStartTime();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(ts.getTime()));

		String fileSubPath = calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/ncs_res_" + taskId
				+ ".xls";
		String name = (task.getLevelName() == null ? "" : task
				.getLevelName())
				+ "Ncs汇总分析报告("
				+ dateUtil.format_yyyyMMddHHmmss(new Date(ts.getTime()))
				+ ").xls";
		log.info("报告所在的文件：" + fileSubPath + ",将输出的文件名：" + name);
		//super.setFileName(name);

		// 判断文件是否存在
		String file = "/op/rno/ana_result/" + fileSubPath;// 文件下载路径 hardcode
		String realPath = ServletActionContext.getServletContext()
				.getRealPath(file);

		List<Object> res = rnoStructureService.getNcsTaskReport(taskId, realPath,
				dataType);
		//String result = gson.toJson(res);
		//HttpTools.writeToClient(result);
		log.info("退出方法：getNcsReportDataForAjaxAction。返回数据："
				+ (res == null ? 0 : res.size()));
		return res;
	}
	/**
	 * 
	 * @title 获取最近一次ncs任务汇总时间
	 * @author chao.xj
	 * @date 2014-6-25上午10:15:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getLatelyNcsTaskTimeForAjaxAction() {
		log.info("进入getLatelyNcsTaskTimeForAjaxAction()");
		//List<Map<String, Object>> taskcompletetime=rnoPlanDesignService.getTaskIdAndCompleteTimeByAreaId(areaId);
		//String result=gson.toJson(taskcompletetime);
		List<Map<String, Object>> latelyInterferMartixRec = rnoPlanDesignService
				.getLatelyInterferMartixRecByAreaId(areaId);
		String result=gson.toJson(latelyInterferMartixRec);
		log.info("退出getLatelyNcsTaskTimeForAjaxAction()"+result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 
	 * @title 获取小区干扰矩阵数据详情信息
	 * @author chao.xj
	 * @date 2014-6-25上午11:58:49
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void getCellInterferMatrixDataDetailForAjaxAction() {
		log.debug("进入getCellInterferMatrixDataDetailForAjaxAction　cell="+ cell + ", MartixDescId=" + MartixDescId);
		//String dir="";
		//String realpath=rnoPlanDesignService.getInterferMatrixDir(taskId, dir);
		String realpath = rnoPlanDesignService.getInterferMatrixDirById(MartixDescId);
		log.debug("realpath："+realpath);
		CellInterWithDetailInfo cellinterinfo=rnoStructureService.getCellInterDetailInfo(realpath, cell);
		String result=gson.toJson(cellinterinfo);
		log.debug("进入getCellInterferMatrixDataDetailForAjaxAction"+result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 获取小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public void getCellFreqByCellNameForAjaxAction() {
		log.debug("进入getCellFreqByCellNameForAjaxAction　cell="+ cell );
		List<Map<String, Object>> res = rnoPlanDesignService.getCellFreqByCellName(cell);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 更新小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public void updateCellFreqThroughCellNameForAjaxAction() {
		log.debug("进入updateCellFreqThroughCellNameForAjaxAction　cell=" + cell
				+ ", bcch=" + bcchStr + ", tch=" + tchStr);
		boolean flag = rnoPlanDesignService.updateCellFreqThroughCellName(cell, bcchStr, tchStr);
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("flag", flag);
		String result = gson.toJson(res);
		HttpTools.writeToClient(result);
	}
}
