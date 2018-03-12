package com.iscreate.op.action.rno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.RnoTrafficStaticsService;

public class RnoMapGroundSupportAction {
	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoMapGroundSupportAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全

	// ----ioc---//
	private RnoResourceManagerService rnoResourceManagerService;
	private RnoTrafficStaticsService rnoTrafficStaticsService;
	private RnoCommonService rnoCommonService;

	// ----页面变量---//
	private long areaId;
	private Page page;// 分页类
	private List<Area> zoneAreaLists;// 用户可见区/县的列表
	private List zoneProvinceLists;//省份LIST
	private List zoneCountyLists;//区/县LIST
	
	private int zoom;// 缩放级别
	private Point centerPoint;// 中心点
	private String label;// 小区名
	
	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	private List<Area> countryAreas;
	
	
	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Area> getZoneAreaLists() {
		return zoneAreaLists;
	}

	public void setZoneAreaLists(List<Area> zoneAreaLists) {
		this.zoneAreaLists = zoneAreaLists;
	}

	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}
	
	public List getZoneProvinceLists() {
		return zoneProvinceLists;
	}

	public List getZoneCountyLists() {
		return zoneCountyLists;
	}

	public void setRnoTrafficStaticsService(
			RnoTrafficStaticsService rnoTrafficStaticsService) {
		this.rnoTrafficStaticsService = rnoTrafficStaticsService;
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

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	/**
	 * 初始化区域导入内存页面
	 * 
	 * @return
	 * @author chao.xj 2013-10-16下午04:18:22
	 */
	public String initMapSpotDataImportPageAction() {
		initUserProvincesArea();
		initUserCountyArea();
		return "success";
	}
	/**
	 * 进入地图操作页面
	 * 
	 * @return
	 */
	public String initRnoMapSearchAction() {
		initAreaList();

		return "success";
	}

	/**
	 * 
	 * 获取用户的 县/区 区域
	 * @author chao.xj
	 * 2013-10-15下午02:30:32
	 */
	private void initUserCountyArea() {
		// 获取用户所在区域信息
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		zoneCountyLists = this.rnoTrafficStaticsService.getCountysInSpecLevelListByUserId(loginPerson, "区/县");

	}
	/**
	 * 
	 * 获取用户的 省 区域
	 * @author chao.xj
	 * 2013-10-15上午09:59:13
	 */
	private void initUserProvincesArea() {
		// 获取用户所在区域信息
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		zoneProvinceLists = this.rnoTrafficStaticsService.getProvincesInSpecLevelListByUserId(loginPerson, "省");
		
	}	
	
	/**
	 * 搜索指定小区的邻区
	 */
	public void searchNcellByCellForAjaxAction(){
		log.info("进入方法：searchNcellByCellForAjaxAction.label="+label);
		List<RnoNcell> ncells=rnoResourceManagerService.getNcellsByCell(label);
		String result=gson.toJson(ncells);
		log.info("退出方法：searchNcellByCellForAjaxAction。返回："+result);
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
			log.error("getBscsResideInAreaForAjaxAction！");
		}
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
}
