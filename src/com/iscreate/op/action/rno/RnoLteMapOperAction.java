package com.iscreate.op.action.rno;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.LteCellPciAnalysisQueryResult;
import com.iscreate.op.action.rno.model.LteCellPciCollAndConfQueryResult;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.action.rno.model.LteCellUpdateResult;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoLteCellManageService;
import com.iscreate.op.service.rno.RnoLteMapOperService;
import com.iscreate.op.service.rno.tool.HttpTools;


public class RnoLteMapOperAction extends RnoCommonAction {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoMapGroundSupportAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	
	//-----地图初始化-----//
	private Point centerCityPoint;//获取到市级别，不获取到县级别
	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	
	//-----在地图分页加载lte小区-----//
	private long cityId;
	private String zoom;
	private	Page page;
	private String areaName;
	
	//-----点击lte小区查看详情-----//
	private long lcid; //Lte小区id
	
	//-----lte小区信息编辑------//
	private Long lteCellId;
	private LteCellUpdateResult lteCellUpdateResult;
	
	//-----pci复用分析------//
	private long areaIdForPciAnalysis;
	private long reuseLcid;
	
	//-----pci冲突与混淆分析-----//
	private long areaIdForCollAndConfAnalysis;
	
	// -------注入-------------//
	private RnoLteMapOperService rnoLteMapOperService;
	private RnoLteCellManageService rnoLteCellManageService;
	
	// -----------------get set -------------------------------//
	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public RnoLteMapOperService getRnoLteMapOperService() {
		return rnoLteMapOperService;
	}

	public void setRnoLteMapOperService(RnoLteMapOperService rnoLteMapOperService) {
		this.rnoLteMapOperService = rnoLteMapOperService;
	}
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Point getCenterCityPoint() {
		return centerCityPoint;
	}

	public void setCenterCityPoint(Point centerCityPoint) {
		this.centerCityPoint = centerCityPoint;
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
	
	public long getLcid() {
		return lcid;
	}

	public void setLcid(long lcid) {
		this.lcid = lcid;
	}
	
	public RnoLteCellManageService getRnoLteCellManageService() {
		return rnoLteCellManageService;
	}

	public void setRnoLteCellManageService(
			RnoLteCellManageService rnoLteCellManageService) {
		this.rnoLteCellManageService = rnoLteCellManageService;
	}

	public LteCellUpdateResult getLteCellUpdateResult() {
		return lteCellUpdateResult;
	}

	public void setLteCellUpdateResult(LteCellUpdateResult lteCellUpdateResult) {
		this.lteCellUpdateResult = lteCellUpdateResult;
	}

	public Long getLteCellId() {
		return lteCellId;
	}

	public void setLteCellId(Long lteCellId) {
		this.lteCellId = lteCellId;
	}
	public long getAreaIdForPciAnalysis() {
		return areaIdForPciAnalysis;
	}

	public void setAreaIdForPciAnalysis(long areaIdForPciAnalysis) {
		this.areaIdForPciAnalysis = areaIdForPciAnalysis;
	}

	public long getReuseLcid() {
		return reuseLcid;
	}

	public void setReuseLcid(long reuseLcid) {
		this.reuseLcid = reuseLcid;
	}

	public long getAreaIdForCollAndConfAnalysis() {
		return areaIdForCollAndConfAnalysis;
	}

	public void setAreaIdForCollAndConfAnalysis(long areaIdForCollAndConfAnalysis) {
		this.areaIdForCollAndConfAnalysis = areaIdForCollAndConfAnalysis;
	}
	/**
	 * 初始化lte地图页面
	 * @return
	 * @author peng.jm
	 * 2014-5-15 15:35:44
	 */
	public String initRnoLteMapOperAction(){
		initAreaListForCity();
		return "success";
	}
	
	/**
	 * 根据区域id获取Lte小区数据
	 * @return
	 * @author peng.jm
	 * 2014-5-15 15:39:07
	 */
	public void getLteCellByPageForAjaxAction(){
		
		
		long cityId = getCityId();
		log.info("进入方法：getLteCellByPageForAjaxAction。 zoom=" + zoom
				+"cityId: "+cityId+ ",page=" + page);
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
		//System.out.println(cityId);
		/*List<Area> list = this.rnoLteMapOperService
				.ltefindAreaInSpecLevelListByUserId(loginPerson, -1==cityId?"市":"区/县");*/
		List<Area> list = this.rnoLteMapOperService
		.ltefindAreaInSpecLevelListByUserId(loginPerson, "市");
		Area area = null;
		log.info("用户" + loginPerson + " 允许查看的区域： " + list.get(0).getName());
		// 判断是否在用户允许查看的区域范围内
		boolean ok = false;
		if (list != null) {
			for(Area a : list)
			{
				if (a.getArea_id() == cityId) {
					ok = true;
					area = a;
					break;
				}
			}
			//Liang YJ 修改，小区查找增加全市小区查找
			/*if(-1 == cityId)
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
					if (a.getArea_id() == cityId) {
						ok = true;
						area = a;
						break;
					}
				}
			}*/
		}
		
		//GisCellQueryResult gisCells = null;
		LteCellQueryResult lteCellQueryResult = null;
		
		// 暂时支持这个区域
		int totalCnt = 0;
		//log.info("areaLevel: "+area.getArea_level());
		if (ok && area != null && (area.getArea_level().equals("区/县") || area.getArea_level().equals("市"))) {
			lteCellQueryResult = rnoLteMapOperService.getLteCellByPage(area.getArea_id(), page);
			totalCnt = lteCellQueryResult.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			lteCellQueryResult = new LteCellQueryResult();
			lteCellQueryResult.setLteCells(null);
			totalCnt = 0;
		}

		Page newPage = new Page();
		newPage.setCurrentPage(page.getCurrentPage());
		newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		lteCellQueryResult.setPage(newPage);

		String result = gson.toJson(lteCellQueryResult);
		//System.out.println(result);
		//输出例子：{"totalCnt":1,"lteCells":[{"lcid":1083334.0,"lng":113.02553999999999,"lat":23.695546999999998,"chineseName":"I1海关3",
		//  "mapType":"E","shapeType":"T","shapeData":"113.02554,23.695547;113.024777769936948240016200625161089029,23.69624499070357251697368637082983092274;
		//         113.025261014407024651625585488595101468,23.69650046887933625328180384570080102166"}],
		// "page":{"totalPageCnt":1,"pageSize":100,"currentPage":1,"totalCnt":1,"forcedStartIndex":-1}}
		
		log.info("离开方法：getLteCellByPageForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + lteCellQueryResult.getPage());

		writeToClient(result);
	}
	
	/**
	 * 将获取的json信息传到客户端
	 */
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
	 * 初始化AreaList到市级别
	 * @return
	 * @author peng.jm
	 * 2014-5-15 15:35:44
	 */
	public void initAreaListForCity() {
		String account = (String) SessionService.getInstance().getValueByKey(
				"userId");
		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
				"省");
		cityAreas = new ArrayList<Area>();
		if (provinceAreas != null && provinceAreas.size() > 0) {
			cityAreas = rnoCommonService
					.getSpecialSubAreasByAccountAndParentArea(account,
							provinceAreas.get(0).getArea_id(), "市");	
				if (cityAreas != null && cityAreas.size() > 0) {
					areaName = cityAreas.get(0).getName();
					centerCityPoint = new Point();
					centerCityPoint.setLng(cityAreas.get(0).getLongitude());
					centerCityPoint.setLat(cityAreas.get(0).getLatitude());
				}
		}

	}
	
	/**
	 * 
	 * @title 通过Lte小区id获取展示在地图的部分信息
	 * @author chao.xj
	 * @date 2015-7-1下午4:53:03
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void getLteCellDetailForAjaxAction() {
		// 输入：lcid
		log.info("进入方法：getLteCellDetailForAjaxAction。lcid=" + lcid);
		//Cell cell = rnoResourceManagerService.getCellDetail(label);
		RnoLteCell lteCell = rnoLteMapOperService.getLteCellDetail(lcid);
		String result = gson.toJson(lteCell);
		log.info("退出方法:getLteCellDetailForAjaxAction。 获取的结果：" + result);
		writeToClient(result);
	}
	
	/**
	 * 查询指定id的lte小区以及同站的其他小区的详情
	 * @return
	 * @author peng.jm
	 * 2014-5-21 18:24:07
	 */
	public void getLteCellAndCositeCellsDetailForAjaxAction() {
		log.debug("进入方法：getLteCellAndCositeCellsDetailForAjaxAction。lteCellId="
				+ lcid);
		List<Map<String, Object>> cells = rnoLteCellManageService
						.queryLteCellAndCositeCells(lcid);
		String operation_time = "";
		if(!("").equals(cells.get(0).get("OPERATION_TIME")) && cells.get(0).get("OPERATION_TIME") != null){
			operation_time =sdf_SIMPLE.format(cells.get(0).get("OPERATION_TIME"));
		}
		//System.out.println(cells.get(0).get("OPERATION_TIME"));
		//System.out.println(operation_time);
		//sdf_full
		cells.get(0).put("OPERATION_TIME", operation_time);
		String result = gson.toJson(cells);
		log.debug("getLteCellAndCositeCellsDetailForAjaxAction输出 ：" + result);
		HttpTools.writeToClient(result);
	}
	
	/**
	 * 地图页面的lte信息更新
	 * @return
	 * @author peng.jm
	 * 2014-5-22 14:27:16
	 * @throws UnsupportedEncodingException 
	 */
	public void updateLteCellDetailsForAjaxAction() throws UnsupportedEncodingException {
		log.debug("进入方法：updateLteCellDetailsForAjaxAction");
		Map<String,Object> lteCell = lteCellUpdateResult.resultToMap();
		boolean resultFromServer = rnoLteCellManageService.modifyLteCellDetail(lteCellId, lteCell);
		log.debug("updateLteCellDetailsForAjaxActiond ：" + resultFromServer);
		if(resultFromServer){
			HttpTools.writeToClient("{'flag':true}");
		}else{
			HttpTools.writeToClient("{'flag':false}");
		}
	}
	/**
	 * 
	 * @title 获取Lte的PCI复用分析数据
	 * @author chao.xj
	 * @date 2015-7-1上午10:51:54
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void getPciReuseAnalysisByPageForAjaxAction() {
		log.debug("进入方法：getPciReuseAnalysisByPageForAjaxAction。areaId=" + areaIdForPciAnalysis + ",page=" + page);
		
		Page newPage = page.copy();
		LteCellPciAnalysisQueryResult pciAnalysisResult = rnoLteMapOperService
				.getPciReuseAnalysisByPage(areaIdForPciAnalysis, 
						newPage);
		
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);
		pciAnalysisResult.setPage(newPage);	
		
		//System.out.println(pciAnalysisResult.getCellPairsList().get(6).get(0));
		//System.out.println(pciAnalysisResult.getReuseCellList().get(6).get(0));
		
		String result = "{'page':"+gson.toJson(pciAnalysisResult.getPage())+",'pciList':"+
						gson.toJson(pciAnalysisResult.getPciList())+",'reuseCellList':"+
						gson.toJson(pciAnalysisResult.getReuseCellList())+",'cellPairsList':"+
						gson.toJson(pciAnalysisResult.getCellPairsList())+"}";
		
		//String result = gson.toJson(pciAnalysisResult);
		//System.out.println(gson.toJson(pciAnalysisResult.getReuseCellList()));
		writeToClient(result);
	}
	
	/**
	 * 
	 * @title 通过pci获取相同pci的小区,以及与参考小区的覆盖距离
	 * @author chao.xj
	 * @date 2015-7-1上午10:52:06
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void getCellsWithSamePciDetailsForAjaxAction() {
		log.debug("进入方法：getOneCellWithSamePciDetailsForAjaxAction。reuseLcid=" + reuseLcid);
		
		List<Map<String,Object>> cellResult = rnoLteMapOperService
			.getCellsWithSamePciDetails(reuseLcid);
		String result = gson.toJson(cellResult);
		//System.out.println(result);
		writeToClient(result);
	}
	
	/**
	 * 
	 * @title 获取PCI冲突与混淆的小区数据
	 * @author chao.xj
	 * @date 2015-7-1上午10:52:17
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void getCollAndConfCellWithSamePciByPageForAjaxAction() {
		log.debug("getCollAndConfCellWithSamePciByPageForAjaxAction。areaId=" + areaIdForCollAndConfAnalysis + ",page=" + page);
		
		Page newPage = page.copy();
		LteCellPciCollAndConfQueryResult pciCollAndConfResult = rnoLteMapOperService
			.getCollAndConfCellWithSamePciByPage(areaIdForCollAndConfAnalysis,
					newPage);
		
		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);
		
		pciCollAndConfResult.setPage(newPage);	
		
		String result = "{'page':"+gson.toJson(pciCollAndConfResult.getPage())+",'pciList':"+
							gson.toJson(pciCollAndConfResult.getPciList())+",'collisionList':"+
							gson.toJson(pciCollAndConfResult.getCollisionList())+",'confusionList':"+
							gson.toJson(pciCollAndConfResult.getConfusionList())+",'collPairsList':"+
							gson.toJson(pciCollAndConfResult.getCollPairsList())+",'confPairsList':"+
							gson.toJson(pciCollAndConfResult.getConfPairsList())+"}";
		//System.out.println(gson.toJson(pciCollAndConfResult.getCollisionList()));
		writeToClient(result);
	}
}
