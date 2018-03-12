package com.iscreate.op.action.rno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisAnalysisCellQueryResult;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCellTopN;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoInterferenceAnalysisGisCell;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoInterferenceAnalysisService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.RnoTrafficStaticsService;
import com.opensymphony.xwork2.ActionContext;

/**
 * 总干扰分析Action
 * 
 * @author ou.jh
 * @date Nov 6, 2013
 * @Description: TODO
 * @param
 * @return
 * @throws
 */
public class RnoInterferenceAnalysisAction {

	// -----------类静态-------------//
	private static Log log = LogFactory
			.getLog(RnoInterferenceAnalysisAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全

	// -------------小区展现---------------//
	private int zoom;// 缩放级别
	private Point topLeft;// 左上角
	private Point bottomRight;// 右下角
	private Point centerPoint;// 中心点
	private String label;// 小区名
	private Page page;// 分页类
	private long rank;

	private long areaId;
	private long cellConfigId;// 小区配置id
	private long interConfigId;// 干扰配置id
	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	private List<Area> countryAreas;

	private List<Map<String, Object>> bscEngNameLists;

	// -------注入-------------//
	private RnoCommonService rnoCommonService;
	private RnoResourceManagerService rnoResourceManagerService;
	private RnoTrafficStaticsService rnoTrafficStaticsService;
	private RnoInterferenceAnalysisService rnoInterferenceAnalysisService;

	/**
	 * 初始化总干扰分析页面
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013 11:12:04 AM
	 * @Description: TODO
	 * @param
	 * @throws
	 */
	public String initInterferenceAnalysisPageAction() {
		bscEngNameLists = rnoTrafficStaticsService.getRnoBscEngName();
		initAreaList();

		return "success";
	}

	/**
	 * 初始化用户可见区域
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013 11:11:37 AM
	 * @Description: TODO
	 * @param
	 * @throws
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
	 * 获取指定区域的小区
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013 4:34:35 PM
	 * @Description: TODO
	 * @param
	 * @throws
	 */
	public void getAnalysisGisCellByPageForAjaxAction() {
		// 缩放级别：zoom
		// 区域id:areaId
		// 分页参数：page
		// 范围经纬度:topLeft,bottomRight

		log.info("进入方法：getGisCellByPageForAjaxAction。 zoom=" + zoom
				+ ",areaId=" + areaId + ",cellConfigId=" + cellConfigId
				+ ",interConfigId=" + interConfigId + ",page=" + page);
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");

		List<Area> list = this.rnoResourceManagerService
				.gisfindAreaInSpecLevelListByUserId(loginPerson, "区/县");
		Area area = null;
		log.info("用户" + loginPerson + " 允许查看的区域： " + list);
		// 判断是否在用户允许查看的区域范围内
		boolean ok = false;
		if (list != null) {
			for (Area a : list) {
				if (a.getArea_id() == areaId) {
					ok = true;
					area = a;
					break;
				}
			}
		}

		// 暂时支持这个区域
		// 在session中获取干扰配置
		PlanConfig cellConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, cellConfigId);
		if (cellConfig == null) {
			log.error("小区配置列表中不存在指定的configId=" + cellConfigId + "的分析项！");
		}
		PlanConfig interConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
				interConfigId);
		if (interConfig == null) {
			log.error("干扰分析列表中不存在指定的configId=" + interConfig + "的分析项！");
		}
		int totalCnt = 0;
		GisAnalysisCellQueryResult gisCells = null;
		boolean isTempStorage = false;
		if (ok && cellConfig != null && interConfig != null && area != null
				&& area.getArea_level().equals("区/县")) {
			isTempStorage=cellConfig.isTemp();
			log.info("interConfig.isTemp();"+interConfig.isTemp());
			gisCells = this.rnoInterferenceAnalysisService
					.getRnoAnalysisGisCellByPage(areaId,
							cellConfig.getConfigId(), isTempStorage, page,
							interConfig.getConfigId(), interConfig.isTemp());
			totalCnt = gisCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			gisCells = new GisAnalysisCellQueryResult();
			gisCells.setRnoAnalysisGisCells(null);
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

	private void writeToClient(String result) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("writeToClient出错 ！");
		}
	}

	/**
	 * 在session中获取干扰配置
	 * 
	 * @author ou.jh
	 * @date Nov 8, 2013 1:58:57 PM
	 * @Description: TODO
	 * @param @return
	 * @throws
	 */
	private PlanConfig getInterferencedataInSession(String code, long configId) {
		PlanConfig planConfig = null;
		// 更新session的数据
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();

		List<PlanConfig> exists = (List<PlanConfig>) session.getAttribute(code);
		if (exists != null && !exists.isEmpty()) {
			for (PlanConfig p : exists) {
				// if(p.getType() == DBConstant.INTERFERENCEDATA){
				// planConfig = p;
				// break;
				// }
				if (p.getConfigId() == configId) {
					return p;
				}
			}
		}
		return planConfig;
	}

	/**
	 * top-N 最大干扰小区标注
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013 4:01:26 PM
	 * @Description: TODO
	 * @param @return
	 * @throws
	 */
	public void getRnoGisCellInAreaTopNAction() {
		log.info("进入方法 ： getRnoGisCellInAreaTopNAction.cellConfigId="
				+ cellConfigId + ",interConfigId=" + interConfigId);
		// 在session中获取干扰配置
		PlanConfig cellConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, cellConfigId);
		if (cellConfig == null) {
			log.error("小区配置列表中不存在指定的configId=" + cellConfigId + "的分析项！");
		}
		PlanConfig interConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
				interConfigId);
		if (interConfig == null) {
			log.error("干扰分析列表中不存在指定的configId=" + interConfig + "的分析项！");
		}
		List<RnoAnalysisGisCellTopN> rnoGisCellInAreaTopN = Collections.EMPTY_LIST;
		if (cellConfig != null && interConfig != null) {
			rnoGisCellInAreaTopN = this.rnoInterferenceAnalysisService
					.getRnoGisCellInAreaTopN(cellConfig.getConfigId(),
							cellConfig.isTemp(), interConfig.getConfigId(),
							interConfig.isTemp(), rank, areaId);
		}
		String result = gson.toJson(rnoGisCellInAreaTopN);

		writeToClient(result);
	}

	/**
	 * 小区干扰分析
	 * 
	 * @author ou.jh
	 * @date Nov 11, 2013 4:12:36 PM
	 * @Description: TODO
	 * @param
	 * @throws
	 */
	public void getCellInterferenceAnalysisAction() {
		log.info("进入方法：getCellInterferenceAnalysisAction cellConfigId="
				+ cellConfigId + ",interConfigId=" + interConfigId);
		Map<String, Object> retrunMap = new HashMap<String, Object>();

		// 暂时支持这个区域
		// 在session中获取干扰配置
		PlanConfig cellConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.CELL_LOAD_CONFIG_ID, cellConfigId);
		if (cellConfig == null) {
			log.error("小区配置列表中不存在指定的configId=" + cellConfigId + "的分析项！");
		}
		PlanConfig interConfig = getInterferencedataInSession(
				RnoConstant.SessionConstant.INTERFERENCE_LOAD_CONFIG_ID,
				interConfigId);
		if (interConfig == null) {
			log.error("干扰分析列表中不存在指定的configId=" + interConfig + "的分析项！");
		}

		String result = "{}";
		if (cellConfig != null && interConfig != null) {
			// 小区干扰分析
			RnoInterferenceAnalysisGisCell gisCell = this.rnoInterferenceAnalysisService
					.getCellInterferenceAnalysis(cellConfig.getConfigId(),
							cellConfig.isTemp(), interConfig.getConfigId(),
							interConfig.isTemp(), areaId, this.label);
			retrunMap.put("gisCell", gisCell);
			// 根据CELLLABEL获取干扰邻区
			List<RnoGisCell> interferenceCellByLabel = this.rnoInterferenceAnalysisService
					.getInterferenceCellByLabel(cellConfig.getConfigId(),
							cellConfig.isTemp(), interConfig.getConfigId(),
							interConfig.isTemp(), label);
			retrunMap.put("interferenceCellByLabel", interferenceCellByLabel);
			List<Map<String, Object>> interferenceTCH = this.rnoInterferenceAnalysisService
					.getInterferenceTCH(cellConfig.getConfigId(),
							cellConfig.isTemp(), interConfig.getConfigId(),
							interConfig.isTemp(), areaId, label);
			Set<Long> interferenceTCHs = getInterferenceTCH(interferenceTCH);
			retrunMap.put("interferenceTCHs", interferenceTCHs);
			result = gson.toJson(retrunMap);
			// System.out.println("离开方法：getGisCellByPageForAjaxAction。 输出："+result);
		}
		writeToClient(result);
	}

	/**
	 * 计算干扰频点
	 * 
	 * @author ou.jh
	 * @date Nov 12, 2013 11:37:02 AM
	 * @Description: TODO
	 * @param @param interferenceTCH
	 * @throws
	 */
	private Set<Long> getInterferenceTCH(
			List<Map<String, Object>> interferenceTCHs) {
		Set<Long> set = new HashSet<Long>();
		for (Map<String, Object> interferenceTCH : interferenceTCHs) {
			if (interferenceTCH != null) {
				if (interferenceTCH.get("ICTCH") != null) {
					String[] split = interferenceTCH.get("ICTCH").toString()
							.split(",");
					if (split != null) {
						for (String s : split) {
							if (s != null && !s.equals("")) {
								if (interferenceTCH.get("BCCH") != null
										&& !interferenceTCH.get("BCCH").equals(
												"")) {
									long parseLong = Long.parseLong(s);
									long bcch = Long.parseLong(interferenceTCH
											.get("BCCH").toString());
									// 计算干扰频点
									if (bcch == parseLong
											|| bcch + 1 == parseLong
											|| bcch - 1 == parseLong) {
										set.add(parseLong);
									}
								} 
							} 
						}
					}
				}
			}
		}
		return set;
	}

	public List<Area> getProvinceAreas() {
		return provinceAreas;
	}

	public void setProvinceAreas(List<Area> provinceAreas) {
		this.provinceAreas = provinceAreas;
	}

	public RnoCommonService getRnoCommonService() {
		return rnoCommonService;
	}

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
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

	public List<Map<String, Object>> getBscEngNameLists() {
		return bscEngNameLists;
	}

	public void setBscEngNameLists(List<Map<String, Object>> bscEngNameLists) {
		this.bscEngNameLists = bscEngNameLists;
	}

	public RnoTrafficStaticsService getRnoTrafficStaticsService() {
		return rnoTrafficStaticsService;
	}

	public void setRnoTrafficStaticsService(
			RnoTrafficStaticsService rnoTrafficStaticsService) {
		this.rnoTrafficStaticsService = rnoTrafficStaticsService;
	}

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		RnoInterferenceAnalysisAction.log = log;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		RnoInterferenceAnalysisAction.gson = gson;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public long getAreaId() {
		return areaId;
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

	public RnoInterferenceAnalysisService getRnoInterferenceAnalysisService() {
		return rnoInterferenceAnalysisService;
	}

	public void setRnoInterferenceAnalysisService(
			RnoInterferenceAnalysisService rnoInterferenceAnalysisService) {
		this.rnoInterferenceAnalysisService = rnoInterferenceAnalysisService;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public long getCellConfigId() {
		return cellConfigId;
	}

	public void setCellConfigId(long cellConfigId) {
		this.cellConfigId = cellConfigId;
	}

	public long getInterConfigId() {
		return interConfigId;
	}

	public void setInterConfigId(long interConfigId) {
		this.interConfigId = interConfigId;
	}

}
