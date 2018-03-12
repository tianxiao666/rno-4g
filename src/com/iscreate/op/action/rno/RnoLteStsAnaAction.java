package com.iscreate.op.action.rno;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.G4StsDescQueryCond;
import com.iscreate.op.action.rno.model.LteStsCellQueryResult;
import com.iscreate.op.action.rno.model.LteStsMonitorThresholdResult;
import com.iscreate.op.action.rno.vo.LteStsMonitorIndex;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoLteStsAnaService;
import com.iscreate.op.service.rno.RnoResourceManagerService;
import com.iscreate.op.service.rno.tool.HttpTools;

public class RnoLteStsAnaAction extends RnoCommonAction {

	private static Logger log = LoggerFactory.getLogger(RnoLteStsAnaAction.class);
	
	private long cityId;
	
	/* 数据查询条件 */
	private G4StsDescQueryCond g4StsDescQueryCond;
	
	
	private String cellName;
	
	private List<String> cellNames;
	private String beginTime;
	

	private long areaId;

	private long label;// 小区名

	private long cellId;

	// -------注入-------------//
	private RnoResourceManagerService rnoResourceManagerService;
	private RnoLteStsAnaService rnoLteStsAnaService;
	
	
	//频点类型
	private String freqType;
	
	//地图网格
	private Map<String, String> mapGrid;
	
	public Map<String, String> getMapGrid() {
		return mapGrid;
	}
	public void setMapGrid(Map<String, String> mapGrid) {
		this.mapGrid = mapGrid;
	}
	public RnoResourceManagerService getRnoResourceManagerService() {
		return rnoResourceManagerService;
	}
	public void setRnoResourceManagerService(
			RnoResourceManagerService rnoResourceManagerService) {
		this.rnoResourceManagerService = rnoResourceManagerService;
	}
	public String getCellName() {
		return cellName;
	}
	public List<String> getCellNames() {
		return cellNames;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public void setCellNames(List<String> cellNames) {
		this.cellNames = cellNames;
	}
	public RnoLteStsAnaService getRnoLteStsAnaService() {
		return rnoLteStsAnaService;
	}
	public long getCityId() {
		return cityId;
	}
	public G4StsDescQueryCond getG4StsDescQueryCond() {
		return g4StsDescQueryCond;
	}
	public void setRnoLteStsAnaService(
			RnoLteStsAnaService rnoLteStsAnaService) {
		this.rnoLteStsAnaService = rnoLteStsAnaService;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public void setG4StsDescQueryCond(G4StsDescQueryCond g4StsDescQueryCond) {
		this.g4StsDescQueryCond = g4StsDescQueryCond;
	}
	public long getLabel() {
		return label;
	}
	public void setLabel(long label) {
		this.label = label;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * 初始化话统数据管理页面
	 * @return
	 * @author chen.c10
	 * @date 2015-7-17 下午7:29:15
	 * @company 怡创科技
	 * @version V1.0
	 */
	public String initLteStsDataAnaAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 分页查询4G话统数据
	 * 
	 * @author chen.c10
	 * @date 2015-7-16 下午12:11:26
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void queryG4StsDataAjaxAction(){
		log.debug("进入queryG4DtDescAjaxAction");
		g4StsDescQueryCond=new G4StsDescQueryCond();
		//g4StsDescQueryCond.setDataType((String)attachParams.get("dataType"));
		g4StsDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		g4StsDescQueryCond.setMeaBegTime((String) attachParams.get("meaBegDate"));
		g4StsDescQueryCond.setMeaEndTime((String) attachParams.get("meaEndDate"));
		log.debug("queryG4DtDescAjaxAction.page="+page+",attmap="+attachParams+",queryCond="+g4StsDescQueryCond);
		
		int cnt=page.getTotalCnt();
		if(cnt<0){
			cnt=(int)rnoLteStsAnaService.queryG4StsDescCnt(g4StsDescQueryCond);
		}
		
		Page newPage = page.copy();
		newPage.setTotalCnt(cnt);
		
		List<Map<String,Object>> dataRecs = rnoLteStsAnaService.queryG4StsDescByPage(g4StsDescQueryCond, newPage);
		log.debug("size:" + (dataRecs==null?0:dataRecs.size()));
		String datas = gson.toJson(dataRecs);

		int totalCnt = newPage.getTotalCnt();
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(-1);

		String pstr = gson.toJson(newPage);
		String result = "{'page':" + pstr + ",'data':" + datas + "}";
		log.debug("退出queryG4DtDescAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	
	
	public String initLteStsQuotaAnaAction() {
		initAreaList();
		cellNames=rnoLteStsAnaService.queryG4StsCellNameList();
		return "success";
	}
	/**
	 * 查询LTE话统小区详情
	 * 
	 * @author chen.c10
	 * @date 2015-7-30 下午3:36:06
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void queryG4StsQuotaAjaxAction(){
		log.debug("进入queryG4StsQuotaAjaxAction.cellName="+cellId);
		g4StsDescQueryCond=new G4StsDescQueryCond();
		//g4StsDescQueryCond.setDataType((String)attachParams.get("dataType"));
		//g4StsDescQueryCond.setCityId(Long.parseLong((String) attachParams.get("cityId")));
		//g4StsDescQueryCond.setMeaBegTime((String) attachParams.get("meaBegDate"));
		//g4StsDescQueryCond.setMeaEndTime((String) attachParams.get("meaEndDate"));
		//g4StsDescQueryCond.setCellName(cellName);
		g4StsDescQueryCond.setCellId(label);
		g4StsDescQueryCond.setBeginTime(beginTime);
		log.debug("queryG4StsQuotaAjaxAction.queryCond="+g4StsDescQueryCond);
		List<Map<String,Object>> dataRecs = rnoLteStsAnaService.queryG4StsData(g4StsDescQueryCond);
		log.debug("size:" + (dataRecs==null?0:dataRecs.size()));
		String result = "";
		if(dataRecs!=null&&dataRecs.size()>0){
		Map<String,Object> stsData=dataRecs.get(0);
		LteStsMonitorIndex indexData=calLteQuota(stsData);
		LteStsMonitorThresholdResult stsTh = calLteStsTh(indexData);
		String indexInfo = "";
		String thresholdInfo = "";
		indexInfo = gson.toJson(indexData);
		thresholdInfo = gson.toJson(stsTh);
		result = "{'indexInfo':" + indexInfo + ",'thresholdInfo':" + thresholdInfo + "}";
		}
		log.debug("退出queryG4StsQuotaAjaxAction。输出：" + result);
		HttpTools.writeToClient(result);
	}
	/**
	 * 通过网格，分页获取LTE话统小区集
	 * 
	 * @author chen.c10
	 * @date 2015-7-30 下午3:36:51
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getLteCellByMapGridWithStsCellListForAjaxAction(){
		log.info("进入方法：getLteCellByMapGridWithStsCellListForAjaxAction。 freqType=" + freqType
				+",cityId="+cityId+ ",areaId=" + areaId + ",mapGrid=" + mapGrid);
		page= new Page();
		page.setPageSize(Integer.parseInt(mapGrid.get("pageSize").toString()));
		page.setCurrentPage(Integer.parseInt(mapGrid.get("currentPage").toString()));
		page.setTotalPageCnt(Integer.parseInt(mapGrid.get("totalPageCnt").toString()));
		page.setTotalCnt(Integer.parseInt(mapGrid.get("totalCnt").toString()));
		
		String loginPerson = (String) SessionService.getInstance()
				.getValueByKey("userId");
//		List<Area> list = this.rnoResourceManagerService
//				.gisfindAreaInSpecLevelListByUserId(loginPerson, -1==areaId?"市":"区/县");
		List<Area> list = rnoResourceManagerService.gisfindAreaInSpecLevelListByUserId(loginPerson, "市");
		
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
		LteStsCellQueryResult lteCells = null;

		int totalCnt = 0;

		// 基准点
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints = rnoCommonService
				.getSpecialAreaRnoMapLnglatRelaGpsMapList(cityId,	RnoConstant.DBConstant.MAPTYPE_BAIDU);
		if (ok && area != null && (area.getArea_level().equals("区/县") || area.getArea_level().equals("市"))) {
			if (freqType==null) {
				lteCells = rnoLteStsAnaService.getLteCellByMapGridWithStsCellList(
						area.getArea_id(), mapGrid, page, "",standardPoints);
			}else {
				lteCells = rnoLteStsAnaService.getLteCellByMapGridWithStsCellList(
						area.getArea_id(), mapGrid, page, freqType,standardPoints);
			}
			totalCnt = lteCells.getTotalCnt();
			log.info("总的记录数为：" + totalCnt);
		} else {
			log.error("用户【" + loginPerson + "】允许查看的区域为空！");
			lteCells = new LteStsCellQueryResult();
			lteCells.setLteCells(null);
			totalCnt = 0;
		}

		Page newPage = page.copy();
		//newPage.setCurrentPage(page.getCurrentPage());
		//newPage.setPageSize(page.getPageSize());
		newPage.setTotalCnt(totalCnt);
		newPage.setTotalPageCnt(totalCnt / newPage.getPageSize()
				+ (totalCnt % newPage.getPageSize() == 0 ? 0 : 1));
		newPage.setForcedStartIndex(page.getForcedStartIndex());
		lteCells.setPage(newPage);
		
		String result = gson.toJson(lteCells);
		log.info("离开方法：getLteCellByMapGridWithStsCellListForAjaxAction。 输出小区数：" + totalCnt
				+ ",page=" + lteCells.getPage());
		
		writeToClient(result);
	}
	/**
	 * 获取4G话统指标项
	 * 
	 * @author chen.c10
	 * @date 2015-9-17 下午5:13:07
	 * @company 怡创科技
	 * @version V1.0
	 */
	public void getRno4GStsIndexDescForAjaxAction() {
		log.debug("进入方法：getRno4GStsIndexDescForAjaxAction。 " );		
		List<Map<String,Object>> indexList = rnoLteStsAnaService.getRno4GStsIndexDesc();
		String result = gson.toJson(indexList);
		log.debug("离开方法：getRno4GStsIndexDescForAjaxAction。result ="+result );
		writeToClient(result);
	}
	
	private LteStsMonitorIndex calLteQuota(Map<String, Object> p) {
		LteStsMonitorIndex l=new LteStsMonitorIndex();
		l.setCellName(p.get("CELLNAME").toString());
		l.setStartTime(p.get("BEGINTIME").toString());
		l.setEndTime(p.get("ENDTIME").toString());
		
		float rrc_ConnEstabSucc = 0;
		if (Float.valueOf(p.get("RRC_ATTCONNESTAB").toString()) == 0) {
			rrc_ConnEstabSucc = 100;
		} else {
			rrc_ConnEstabSucc = 100* Float.valueOf(p.get("RRC_SUCCCONNESTAB").toString())/ Float.valueOf(p.get("RRC_ATTCONNESTAB").toString());
		}
		l.setRrc_ConnEstabSucc(rrc_ConnEstabSucc);
		
		float erab_EstabSucc = 0;
		if (getF("ERAB_NBRATTESTAB", p) == 0) {
			erab_EstabSucc = 100;
		} else {
			erab_EstabSucc = 100 * getF("ERAB_NBRSUCCESTAB", p)/ getF("ERAB_NBRATTESTAB", p);
		}
		l.setErab_EstabSucc(erab_EstabSucc);
		
		float wireConn = 0;
		if (getF("ERAB_NBRATTESTAB", p) * getF("RRC_ATTCONNESTAB", p) == 0	|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConn = 100;
		} else {
			wireConn = 100 * getF("ERAB_NBRSUCCESTAB", p)/ getF("ERAB_NBRATTESTAB", p)* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConn(wireConn);

		float erab_Drop_CellLevel = 0;
		if (getF("ERAB_NBRSUCCESTAB", p) == 0) {
			erab_Drop_CellLevel = 0;
		} else {
			erab_Drop_CellLevel = 100	* (getF("ERAB_NBRREQRELENB", p)- getF("ERAB_NBRREQRELENB_NORMAL", p) + getF("ERAB_HOFAIL", p))
					/(getF("ERAB_NBRSUCCESTAB", p)+ getF("HO_SUCCOUTINTERENBS1", p)+ getF("HO_SUCCOUTINTERENBX2", p) + getF("HO_ATTOUTINTRAENB", p));
		}
		l.setErab_Drop_CellLevel(erab_Drop_CellLevel);

		float rrc_ConnRebuild = 0;
		if ((getF("RRC_ATTCONNREESTAB", p) + getF("RRC_ATTCONNESTAB", p)) == 0) {
			rrc_ConnRebuild = 0;
		} else {
			rrc_ConnRebuild = 100* getF("RRC_ATTCONNREESTAB", p)	/ (getF("RRC_ATTCONNREESTAB", p) + getF("RRC_ATTCONNESTAB",	p));
		}
		l.setRrc_ConnRebuild(rrc_ConnRebuild);
		
		float switchSucc = 0;
		if ((getF("HO_ATTOUTINTERENBS1", p) + getF("HO_ATTOUTINTERENBX2", p) + getF("HO_ATTOUTINTRAENB", p)) == 0) {
			switchSucc = 100;
		} else {
			switchSucc = 100	* (getF("HO_SUCCOUTINTERENBS1", p)+ getF("HO_SUCCOUTINTERENBX2", p) + getF("HO_SUCCOUTINTRAENB", p))
					/ (getF("HO_ATTOUTINTERENBS1", p)+ getF("HO_ATTOUTINTERENBX2", p) + getF("HO_ATTOUTINTRAENB", p));
		}
		l.setSwitchSucc(switchSucc);

		l.setEmUplinkSerBytes(getF("PDCP_UPOCTUL", p));

		l.setEmDownlinkSerBytes(getF("PDCP_UPOCTDL", p));

		float erab_ConnSuccQCI1 = 0;
		if (getF("ERAB_NBRATTESTAB_1", p) == 0) {
			erab_ConnSuccQCI1 = 100;
		} else {
			erab_ConnSuccQCI1 = 100 * getF("ERAB_NBRSUCCESTAB_1", p)	/ getF("ERAB_NBRATTESTAB_1", p);
		}
		l.setErab_ConnSuccQCI1(erab_ConnSuccQCI1);
		
		float erab_ConnSuccQCI2 = 0;
		if (getF("ERAB_NBRATTESTAB_2", p) == 0) {
			erab_ConnSuccQCI2 = 100;
		} else {
			erab_ConnSuccQCI2 = 100 * getF("ERAB_NBRSUCCESTAB_2", p)	/ getF("ERAB_NBRATTESTAB_2", p);
		}
		l.setErab_ConnSuccQCI2(erab_ConnSuccQCI2);
		
		float erab_ConnSuccQCI3 = 0;
		if (getF("ERAB_NBRATTESTAB_3", p) == 0) {
			erab_ConnSuccQCI3 = 100;
		} else {
			erab_ConnSuccQCI3 = 100 * getF("ERAB_NBRSUCCESTAB_3", p)	/ getF("ERAB_NBRATTESTAB_3", p);
		}
		l.setErab_ConnSuccQCI3(erab_ConnSuccQCI3);
		
		float erab_ConnSuccQCI4 = 0;
		if (getF("ERAB_NBRATTESTAB_4", p) == 0) {
			erab_ConnSuccQCI4 = 100;
		} else {
			erab_ConnSuccQCI4 = 100 * getF("ERAB_NBRSUCCESTAB_4", p)	/ getF("ERAB_NBRATTESTAB_4", p);
		}
		l.setErab_ConnSuccQCI4(erab_ConnSuccQCI4);
		
		float erab_ConnSuccQCI5 = 0;
		if (getF("ERAB_NBRATTESTAB_5", p) == 0) {
			erab_ConnSuccQCI5 = 100;
		} else {
			erab_ConnSuccQCI5 = 100 * getF("ERAB_NBRSUCCESTAB_5", p)	/ getF("ERAB_NBRATTESTAB_5", p);
		}
		l.setErab_ConnSuccQCI5(erab_ConnSuccQCI5);
		
		float erab_ConnSuccQCI6 = 0;
		if (getF("ERAB_NBRATTESTAB_6", p) == 0) {
			erab_ConnSuccQCI6 = 100;
		} else {
			erab_ConnSuccQCI6 = 100 * getF("ERAB_NBRSUCCESTAB_6", p)	/ getF("ERAB_NBRATTESTAB_6", p);
		}
		l.setErab_ConnSuccQCI6(erab_ConnSuccQCI6);
		float erab_ConnSuccQCI7 = 0;
		if (getF("ERAB_NBRATTESTAB_7", p) == 0) {
			erab_ConnSuccQCI7 = 100;
		} else {
			erab_ConnSuccQCI7 = 100 * getF("ERAB_NBRSUCCESTAB_7", p)	/ getF("ERAB_NBRATTESTAB_7", p);
		}
		l.setErab_ConnSuccQCI7(erab_ConnSuccQCI7);
		
		float erab_ConnSuccQCI8 = 0;
		if (getF("ERAB_NBRATTESTAB_8", p) == 0) {
			erab_ConnSuccQCI8 = 100;
		} else {
			erab_ConnSuccQCI8 = 100 * getF("ERAB_NBRSUCCESTAB_8", p)	/ getF("ERAB_NBRATTESTAB_8", p);
		}
		l.setErab_ConnSuccQCI8(erab_ConnSuccQCI8);
		
		float erab_ConnSuccQCI9 = 0;
		if (getF("ERAB_NBRATTESTAB_9", p) == 0) {
			erab_ConnSuccQCI9 = 100;
		} else {
			erab_ConnSuccQCI9 = 100 * getF("ERAB_NBRSUCCESTAB_9", p)	/ getF("ERAB_NBRATTESTAB_9", p);
		}
		l.setErab_ConnSuccQCI9(erab_ConnSuccQCI9);
		
		float wireConnQCI1 = 0;
		if (getF("ERAB_NBRATTESTAB_1", p) == 0
				|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI1 = 100;
		} else {
			wireConnQCI1 = 100 * getF("ERAB_NBRSUCCESTAB_1", p)/ getF("ERAB_NBRATTESTAB_1", p)* getF("RRC_SUCCCONNESTAB", p)	/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI1(wireConnQCI1);
		
		float wireConnQCI2 = 0;
		if (getF("ERAB_NBRATTESTAB_2", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI2 = 100;
		} else {
			wireConnQCI2 = 100 * getF("ERAB_NBRSUCCESTAB_2", p)/ getF("ERAB_NBRATTESTAB_2", p)* getF("RRC_SUCCCONNESTAB", p)	/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI2(wireConnQCI2);
		
		float wireConnQCI3 = 0;
		if (getF("ERAB_NBRATTESTAB_3", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI3 = 100;
		} else {
			wireConnQCI3 = 100 * getF("ERAB_NBRSUCCESTAB_3", p)/ getF("ERAB_NBRATTESTAB_3", p)* getF("RRC_SUCCCONNESTAB", p)	/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI3(wireConnQCI3);
		
		float wireConnQCI4 = 0;
		if (getF("ERAB_NBRATTESTAB_4", p) == 0 || getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI4 = 100;
		} else {
			wireConnQCI4 = 100 * getF("ERAB_NBRSUCCESTAB_4", p)/ getF("ERAB_NBRATTESTAB_4", p)
					* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI4(wireConnQCI4);
		
		float wireConnQCI5 = 0;
		if (getF("ERAB_NBRATTESTAB_5", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI5 = 100;
		} else {
			wireConnQCI5 = 100 * getF("ERAB_NBRSUCCESTAB_5", p)/ getF("ERAB_NBRATTESTAB_5", p)
					* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI5(wireConnQCI5);
		
		float wireConnQCI6 = 0;
		if (getF("ERAB_NBRATTESTAB_6", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI6 = 100;
		} else {
			wireConnQCI6 = 100 * getF("ERAB_NBRSUCCESTAB_6", p)/ getF("ERAB_NBRATTESTAB_6", p)
					* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI6(wireConnQCI6);
		
		float wireConnQCI7 = 0;
		if (getF("ERAB_NBRATTESTAB_7", p) == 0 || getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI7 = 100;
		} else {
			wireConnQCI7 = 100 * getF("ERAB_NBRSUCCESTAB_7", p)	/ getF("ERAB_NBRATTESTAB_7", p)
					* getF("RRC_SUCCCONNESTAB", p)	/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI7(wireConnQCI7);
		float wireConnQCI8 = 0;
		if (getF("ERAB_NBRATTESTAB_8", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI8 = 100;
		} else {
			wireConnQCI8 = 100 * getF("ERAB_NBRSUCCESTAB_8", p)/ getF("ERAB_NBRATTESTAB_8", p)
					* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI8(wireConnQCI8);
		float wireConnQCI9 = 0;
		if (getF("ERAB_NBRATTESTAB_9", p) == 0|| getF("RRC_ATTCONNESTAB", p) == 0) {
			wireConnQCI9 = 100;
		} else {
			wireConnQCI9 = 100 * getF("ERAB_NBRSUCCESTAB_9", p)/ getF("ERAB_NBRATTESTAB_9", p)
					* getF("RRC_SUCCCONNESTAB", p)/ getF("RRC_ATTCONNESTAB", p);
		}
		l.setWireConnQCI9(wireConnQCI9);
		float erab_DropQCI1_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_1", p) + getF("ERAB_NBRSUCCESTAB_1", p) + getF("ERAB_NBRHOINC_1", p)) == 0) {
			erab_DropQCI1_CellLevel = 100;
		} else {
			erab_DropQCI1_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_1", p)- getF("ERAB_NBRREQRELENB_NORMAL_1", p) + getF(	"ERAB_HOFAIL_1", p))
					/ (getF("ERAB_NBRLEFT_1", p)	+ getF("ERAB_NBRSUCCESTAB_1", p) + getF("ERAB_NBRHOINC_1", p));
		}
		l.setErab_DropQCI1_CellLevel(erab_DropQCI1_CellLevel);
		
		float erab_DropQCI2_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_2", p) + getF("ERAB_NBRSUCCESTAB_2", p) + getF("ERAB_NBRHOINC_2", p)) == 0) {
			erab_DropQCI2_CellLevel = 100;
		} else {
			erab_DropQCI2_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_2", p)- getF("ERAB_NBRREQRELENB_NORMAL_2", p) + getF("ERAB_HOFAIL_2", p))
					/ (getF("ERAB_NBRLEFT_2", p)	+ getF("ERAB_NBRSUCCESTAB_2", p) + getF("ERAB_NBRHOINC_2", p));
		}
		l.setErab_DropQCI2_CellLevel(erab_DropQCI2_CellLevel);
		
		float erab_DropQCI3_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_3", p) + getF("ERAB_NBRSUCCESTAB_3", p) + getF("ERAB_NBRHOINC_3", p)) == 0) {
			erab_DropQCI3_CellLevel = 100;
		} else {
			erab_DropQCI3_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_3", p)- getF("ERAB_NBRREQRELENB_NORMAL_3", p) + getF("ERAB_HOFAIL_3", p))
					/ (getF("ERAB_NBRLEFT_3", p)	+ getF("ERAB_NBRSUCCESTAB_3", p) + getF("ERAB_NBRHOINC_3", p));
		}
		l.setErab_DropQCI3_CellLevel(erab_DropQCI3_CellLevel);
		
		float erab_DropQCI4_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_4", p) + getF("ERAB_NBRSUCCESTAB_4", p) + getF("ERAB_NBRHOINC_4", p)) == 0) {
			erab_DropQCI4_CellLevel = 100;
		} else {
			erab_DropQCI4_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_4", p)- getF("ERAB_NBRREQRELENB_NORMAL_4", p) + getF("ERAB_HOFAIL_4", p))
					/ (getF("ERAB_NBRLEFT_4", p)	+ getF("ERAB_NBRSUCCESTAB_4", p) + getF("ERAB_NBRHOINC_4", p));
		}
		l.setErab_DropQCI4_CellLevel(erab_DropQCI4_CellLevel);
		
		float erab_DropQCI5_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_5", p) + getF("ERAB_NBRSUCCESTAB_5", p) + getF("ERAB_NBRHOINC_5", p)) == 0) {
			erab_DropQCI5_CellLevel = 100;
		} else {
			erab_DropQCI5_CellLevel = 100 	* (getF("ERAB_NBRREQRELENB_5", p)- getF("ERAB_NBRREQRELENB_NORMAL_5", p) + getF("ERAB_HOFAIL_5", p))
					/ (getF("ERAB_NBRLEFT_5", p)	+ getF("ERAB_NBRSUCCESTAB_5", p) + getF("ERAB_NBRHOINC_5", p));
		}
		l.setErab_DropQCI5_CellLevel(erab_DropQCI5_CellLevel);
		
		float erab_DropQCI6_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_6", p) + getF("ERAB_NBRSUCCESTAB_6", p) + getF("ERAB_NBRHOINC_6", p)) == 0) {
			erab_DropQCI6_CellLevel = 100;
		} else {
			erab_DropQCI6_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_6", p)- getF("ERAB_NBRREQRELENB_NORMAL_6", p) + getF("ERAB_HOFAIL_6", p))
					/ (getF("ERAB_NBRLEFT_6", p)	+ getF("ERAB_NBRSUCCESTAB_6", p) + getF("ERAB_NBRHOINC_6", p));
		}
		l.setErab_DropQCI6_CellLevel(erab_DropQCI6_CellLevel);
		
		float erab_DropQCI7_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_7", p) + getF("ERAB_NBRSUCCESTAB_7", p) + getF("ERAB_NBRHOINC_7", p)) == 0) {
			erab_DropQCI7_CellLevel = 100;
		} else {
			erab_DropQCI7_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_7", p)- getF("ERAB_NBRREQRELENB_NORMAL_7", p) + getF("ERAB_HOFAIL_7", p))
					/ (getF("ERAB_NBRLEFT_7", p)	+ getF("ERAB_NBRSUCCESTAB_7", p) + getF("ERAB_NBRHOINC_7", p));
		}
		l.setErab_DropQCI7_CellLevel(erab_DropQCI7_CellLevel);
		
		float erab_DropQCI8_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_8", p) + getF("ERAB_NBRSUCCESTAB_8", p) + getF("ERAB_NBRHOINC_8", p)) == 0) {
			erab_DropQCI8_CellLevel = 100;
		} else {
			erab_DropQCI8_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_8", p)- getF("ERAB_NBRREQRELENB_NORMAL_8", p) + getF("ERAB_HOFAIL_8", p))
					/ (getF("ERAB_NBRLEFT_8", p)	+ getF("ERAB_NBRSUCCESTAB_8", p) + getF("ERAB_NBRHOINC_8", p));
		}
		l.setErab_DropQCI8_CellLevel(erab_DropQCI8_CellLevel);
		float erab_DropQCI9_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_9", p) + getF("ERAB_NBRSUCCESTAB_9", p) + getF("ERAB_NBRHOINC_9", p)) == 0) {
			erab_DropQCI9_CellLevel = 100;
		} else {
			erab_DropQCI9_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_9", p)- getF("ERAB_NBRREQRELENB_NORMAL_9", p) + getF("ERAB_HOFAIL_9", p))
					/ (getF("ERAB_NBRLEFT_9", p)	+ getF("ERAB_NBRSUCCESTAB_9", p) + getF("ERAB_NBRHOINC_9", p));
		}
		l.setErab_DropQCI9_CellLevel(erab_DropQCI9_CellLevel);
		
		float erab_DropQCI1 = 0;
		if(getF("ERAB_NBRSUCCESTAB_1",p)==0){
			erab_DropQCI1 = 100;
		}else{
			erab_DropQCI1 = (getF("ERAB_NBRREQRELENB_1",p)-getF("ERAB_NBRREQRELENB_NORMAL_1",p)+getF("ERAB_HOFAIL_1",p))/getF("ERAB_NBRSUCCESTAB_1",p);
		}
		l.setErab_DropQCI1(erab_DropQCI1);
		
		float erab_DropQCI2 = 0;
		if(getF("ERAB_NBRSUCCESTAB_2",p)==0){
			erab_DropQCI2 = 100;
		}else{
			erab_DropQCI2 = (getF("ERAB_NBRREQRELENB_2",p)-getF("ERAB_NBRREQRELENB_NORMAL_2",p)+getF("ERAB_HOFAIL_2",p))/getF("ERAB_NBRSUCCESTAB_2",p);
		}
		l.setErab_DropQCI2(erab_DropQCI2);
		
		float erab_DropQCI3 = 0;
		if(getF("ERAB_NBRSUCCESTAB_3",p)==0){
			erab_DropQCI3 = 100;
		}else{
			erab_DropQCI3 = (getF("ERAB_NBRREQRELENB_3",p)-getF("ERAB_NBRREQRELENB_NORMAL_3",p)+getF("ERAB_HOFAIL_3",p))/getF("ERAB_NBRSUCCESTAB_3",p);
		}
		l.setErab_DropQCI3(erab_DropQCI3);
		
		float erab_DropQCI4 = 0;
		if(getF("ERAB_NBRSUCCESTAB_4",p)==0){
			erab_DropQCI4 = 100;
		}else{
			erab_DropQCI4 = (getF("ERAB_NBRREQRELENB_4",p)-getF("ERAB_NBRREQRELENB_NORMAL_4",p)+getF("ERAB_HOFAIL_4",p))/getF("ERAB_NBRSUCCESTAB_4",p);
		}
		l.setErab_DropQCI4(erab_DropQCI4);
		
		float erab_DropQCI5 = 0;
		if(getF("ERAB_NBRSUCCESTAB_5",p)==0){
			erab_DropQCI5 = 100;
		}else{
			erab_DropQCI5 = (getF("ERAB_NBRREQRELENB_5",p)-getF("ERAB_NBRREQRELENB_NORMAL_5",p)+getF("ERAB_HOFAIL_5",p))/getF("ERAB_NBRSUCCESTAB_5",p);
		}
		l.setErab_DropQCI5(erab_DropQCI5);
		
		float erab_DropQCI6 = 0;
		if(getF("ERAB_NBRSUCCESTAB_6",p)==0){
			erab_DropQCI6 = 100;
		}else{
			erab_DropQCI6 = (getF("ERAB_NBRREQRELENB_6",p)-getF("ERAB_NBRREQRELENB_NORMAL_6",p)+getF("ERAB_HOFAIL_6",p))/getF("ERAB_NBRSUCCESTAB_6",p);
		}
		l.setErab_DropQCI6(erab_DropQCI6);
		
		float erab_DropQCI7 = 0;
		if(getF("ERAB_NBRSUCCESTAB_7",p)==0){
			erab_DropQCI7 = 100;
		}else{
			erab_DropQCI7 = (getF("ERAB_NBRREQRELENB_7",p)-getF("ERAB_NBRREQRELENB_NORMAL_7",p)+getF("ERAB_HOFAIL_7",p))/getF("ERAB_NBRSUCCESTAB_7",p);
		}
		l.setErab_DropQCI7(erab_DropQCI7);
		
		float erab_DropQCI8 = 0;
		if(getF("ERAB_NBRSUCCESTAB_8",p)==0){
			erab_DropQCI8 = 100;
		}else{
			erab_DropQCI8 = (getF("ERAB_NBRREQRELENB_8",p)-getF("ERAB_NBRREQRELENB_NORMAL_8",p)+getF("ERAB_HOFAIL_8",p))/getF("ERAB_NBRSUCCESTAB_8",p);
		}
		l.setErab_DropQCI8(erab_DropQCI8);
		
		float erab_DropQCI9 = 0;
		if(getF("ERAB_NBRSUCCESTAB_9",p)==0){
			erab_DropQCI9 = 100;
		}else{
			erab_DropQCI9 = (getF("ERAB_NBRREQRELENB_9",p)-getF("ERAB_NBRREQRELENB_NORMAL_9",p)+getF("ERAB_HOFAIL_9",p))/getF("ERAB_NBRSUCCESTAB_9",p);
		}
		l.setErab_DropQCI9(erab_DropQCI9);
		
		float emUplinkSerBytesQCI1 = 0;
		emUplinkSerBytesQCI1 =getF("PDCP_UPOCTUL_1",p);
		l.setEmUplinkSerBytesQCI1(emUplinkSerBytesQCI1);
		
		float emUplinkSerBytesQCI2 = 0;
		emUplinkSerBytesQCI2 =getF("PDCP_UPOCTUL_2",p);
		l.setEmUplinkSerBytesQCI2(emUplinkSerBytesQCI2);
		
		float emUplinkSerBytesQCI3 = 0;
		emUplinkSerBytesQCI3 =getF("PDCP_UPOCTUL_3",p);
		l.setEmUplinkSerBytesQCI3(emUplinkSerBytesQCI3);
		
		float emUplinkSerBytesQCI4 = 0;
		emUplinkSerBytesQCI4 =getF("PDCP_UPOCTUL_4",p);
		l.setEmUplinkSerBytesQCI4(emUplinkSerBytesQCI4);
		
		float emUplinkSerBytesQCI5 = 0;
		emUplinkSerBytesQCI5 =getF("PDCP_UPOCTUL_5",p);
		l.setEmUplinkSerBytesQCI5(emUplinkSerBytesQCI5);
		
		float emUplinkSerBytesQCI6 = 0;
		emUplinkSerBytesQCI6 =getF("PDCP_UPOCTUL_6",p);
		l.setEmUplinkSerBytesQCI6(emUplinkSerBytesQCI6);
		
		float emUplinkSerBytesQCI7 = 0;
		emUplinkSerBytesQCI7 =getF("PDCP_UPOCTUL_7",p);
		l.setEmUplinkSerBytesQCI7(emUplinkSerBytesQCI7);
		
		float emUplinkSerBytesQCI8 = 0;
		emUplinkSerBytesQCI8 =getF("PDCP_UPOCTUL_8",p);
		l.setEmUplinkSerBytesQCI8(emUplinkSerBytesQCI8);
		
		float emUplinkSerBytesQCI9 = 0;
		emUplinkSerBytesQCI9 =getF("PDCP_UPOCTUL_9",p);
		l.setEmUplinkSerBytesQCI9(emUplinkSerBytesQCI9);
		
		float emDownlinkSerBytesQCI1 = 0;
		emDownlinkSerBytesQCI1 = getF("PDCP_UPOCTDL_1",p);
		l.setEmDownlinkSerBytesQCI1(emDownlinkSerBytesQCI1);
		
		float emDownlinkSerBytesQCI2 = 0;
		emDownlinkSerBytesQCI2 = getF("PDCP_UPOCTDL_2",p);
		l.setEmDownlinkSerBytesQCI2(emDownlinkSerBytesQCI2);
		
		float emDownlinkSerBytesQCI3 = 0;
		emDownlinkSerBytesQCI3 = getF("PDCP_UPOCTDL_3",p);
		l.setEmDownlinkSerBytesQCI3(emDownlinkSerBytesQCI3);
		
		float emDownlinkSerBytesQCI4 = 0;
		emDownlinkSerBytesQCI4 = getF("PDCP_UPOCTDL_4",p);
		l.setEmDownlinkSerBytesQCI4(emDownlinkSerBytesQCI4);
		
		float emDownlinkSerBytesQCI5 = 0;
		emDownlinkSerBytesQCI5 = getF("PDCP_UPOCTDL_5",p);
		l.setEmDownlinkSerBytesQCI5(emDownlinkSerBytesQCI5);
		
		float emDownlinkSerBytesQCI6 = 0;
		emDownlinkSerBytesQCI6 = getF("PDCP_UPOCTDL_6",p);
		l.setEmDownlinkSerBytesQCI6(emDownlinkSerBytesQCI6);
		
		float emDownlinkSerBytesQCI7 = 0;
		emDownlinkSerBytesQCI7 = getF("PDCP_UPOCTDL_7",p);
		l.setEmDownlinkSerBytesQCI7(emDownlinkSerBytesQCI7);
		
		float emDownlinkSerBytesQCI8 = 0;
		emDownlinkSerBytesQCI8 = getF("PDCP_UPOCTDL_8",p);
		l.setEmDownlinkSerBytesQCI8(emDownlinkSerBytesQCI8);
		
		float emDownlinkSerBytesQCI9 = 0;
		emDownlinkSerBytesQCI9 = getF("PDCP_UPOCTDL_9",p);
		l.setEmDownlinkSerBytesQCI9(emDownlinkSerBytesQCI9);
		
		float wireDrop_CellLevel = 0;
		if(getF("CONTEXT_SUCCINITALSETUP",p)==0){
			wireDrop_CellLevel = 0;
		}else{
			wireDrop_CellLevel = (getF("CONTEXT_ATTRELENB",p)-getF("CONTEXT_ATTRELENB_NORMAL",p))/getF("CONTEXT_SUCCINITALSETUP",p)*100;
		}
		l.setWireDrop_CellLevel(wireDrop_CellLevel);
		
		float erab_EstabSucc_SuccTimes = 0;
		erab_EstabSucc_SuccTimes = getF("ERAB_NBRSUCCESTAB",p);
		l.setErab_EstabSucc_SuccTimes(erab_EstabSucc_SuccTimes);
		
		float erab_EstabSucc_ReqTimes = 0;
		erab_EstabSucc_ReqTimes = getF("ERAB_NBRATTESTAB",p);
		l.setErab_EstabSucc_ReqTimes(erab_EstabSucc_ReqTimes);
		
		float erab_Drop_ReqTimes_CellLevel = 0;
		erab_Drop_ReqTimes_CellLevel = getF("ERAB_NBRSUCCESTAB",p)+getF("HO_SUCCOUTINTERENBS1",p)+getF("HO_SUCCOUTINTERENBX2",p)+getF("HO_ATTOUTINTRAENB",p);
	    l.setErab_Drop_ReqTimes_CellLevel(erab_Drop_ReqTimes_CellLevel);
		
		float switchSucc_SuccTimes = 0;
		switchSucc_SuccTimes = getF("HO_SUCCOUTINTERENBS1",p)+getF("HO_SUCCOUTINTERENBX2",p)+getF("HO_SUCCOUTINTRAENB",p);
		l.setSwitchSucc_SuccTimes(switchSucc_SuccTimes);
		
		float switchSucc_ReqTimes = 0;
		switchSucc_ReqTimes = getF("HO_ATTOUTINTERENBS1",p)+getF("HO_ATTOUTINTERENBX2",p)+getF("HO_ATTOUTINTRAENB",p); 
		l.setSwitchSucc_ReqTimes(switchSucc_ReqTimes);
		
		float wireDrop_ReqTimes_CellLevel = 0;
		wireDrop_ReqTimes_CellLevel = getF("CONTEXT_SUCCINITALSETUP",p);
		l.setWireDrop_ReqTimes_CellLevel(wireDrop_ReqTimes_CellLevel);
		
		float wireConn_ReqTimes = 0;  
		wireConn_ReqTimes = getF("ERAB_NBRATTESTAB",p)*getF("RRC_ATTCONNESTAB",p);
		l.setWireConn_ReqTimes(wireConn_ReqTimes);
		
		float erab_Drop_DropTimes_CellLevel = 0;
		erab_Drop_DropTimes_CellLevel = getF("ERAB_NBRREQRELENB",p)-getF("ERAB_NBRREQRELENB_NORMAL",p)+getF("ERAB_HOFAIL",p);
	    l.setErab_Drop_DropTimes_CellLevel(erab_Drop_DropTimes_CellLevel);
		
		float wireConn_SuccTimes = 0;
		wireConn_SuccTimes = getF("ERAB_NBRSUCCESTAB",p)*getF("RRC_SUCCCONNESTAB",p);
		l.setWireConn_SuccTimes(wireConn_SuccTimes);
		
		float rrc_ConnEstabSucc_SuccTimes = 0;
		rrc_ConnEstabSucc_SuccTimes = getF("RRC_SUCCCONNESTAB",p);
		l.setRrc_ConnEstabSucc_SuccTimes(rrc_ConnEstabSucc_SuccTimes);
		
		float rrc_ConnEstabSucc_ReqTimes = 0;
		rrc_ConnEstabSucc_ReqTimes = getF("RRC_ATTCONNESTAB",p);
		l.setRrc_ConnEstabSucc_ReqTimes(rrc_ConnEstabSucc_ReqTimes);
		
		float wireDrop_DropTimes_CellLevel =0;
		wireDrop_DropTimes_CellLevel = getF("CONTEXT_ATTRELENB",p)-getF("CONTEXT_ATTRELENB_NORMAL",p);
		l.setWireDrop_DropTimes_CellLevel(wireDrop_DropTimes_CellLevel);
		return l;
	}
	private static float getF(String prName,Map<String, Object> p) {
		return Float.valueOf(p.get(prName).toString());		
	}
	/**
	 * 计算LTE话统阈值
	 * @param data
	 * @return
	 * @author chen.c10
	 * @date 2015年9月23日 上午10:20:17
	 * @company 怡创科技
	 * @version V1.0
	 */
	public LteStsMonitorThresholdResult calLteStsTh (LteStsMonitorIndex data){
		LteStsMonitorThresholdResult stsTh= new LteStsMonitorThresholdResult();
		
		//数据准备
		float wireConn =data.getWireConn();
		float wireDrop_CellLevel = data.getWireDrop_CellLevel();
		float erab_EstabSucc = data.getErab_EstabSucc();
		float rrc_ConnEstabSucc = data.getRrc_ConnEstabSucc();
		float emUplinkSerBytes = data.getEmUplinkSerBytes();
		float emDownlinkSerBytes = data.getEmDownlinkSerBytes();
		float switchSucc = data.getSwitchSucc();
		float erab_EstabSucc_SuccTimes = data.getErab_EstabSucc_SuccTimes();
		float erab_EstabSucc_ReqTimes = data.getErab_EstabSucc_ReqTimes();
		float erab_Drop_CellLevel = data.getErab_Drop_CellLevel();
		float switchSucc_SuccTimes = data.getSwitchSucc_SuccTimes();
		float switchSucc_ReqTimes = data.getSwitchSucc_ReqTimes();
		float rrc_ConnEstabSucc_SuccTimes = data.getRrc_ConnEstabSucc_SuccTimes();
		float rrc_ConnEstabSucc_ReqTimes = data.getRrc_ConnEstabSucc_ReqTimes();
		float wireDrop_DropTimes_CellLevel = data.getWireDrop_DropTimes_CellLevel();
		
		stsTh.setCellName(data.getCellName());
		stsTh.setStartTime(data.getStartTime());
		stsTh.setEndTime(data.getEndTime());
		//计算阈值
		boolean cellBar_rrc_Conn_FailTimes_Flag=false;
		if(rrc_ConnEstabSucc_SuccTimes<70&&(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)>200){
			cellBar_rrc_Conn_FailTimes_Flag=true;
		}
	
		boolean cellBar_DropTimes_Flag=false;
		if(wireDrop_CellLevel>50&&wireDrop_DropTimes_CellLevel>200){
			cellBar_DropTimes_Flag=true;
		}		
	
		boolean erab_DropRate_Flag=false;
		if(erab_Drop_CellLevel>=10&&erab_EstabSucc_SuccTimes>20){
			erab_DropRate_Flag=true;
		}
	
		boolean erab_EstabSuccRate_Flag=false;
		if(erab_EstabSucc<=85&&erab_EstabSucc_ReqTimes>20){
			erab_EstabSuccRate_Flag=true;
		}
	
		boolean rrc_ConnEstabSuccRate_Flag=false;
		if(rrc_ConnEstabSucc<=85&&rrc_ConnEstabSucc_ReqTimes>20){
			erab_EstabSuccRate_Flag=true;
		}
	
		boolean rrc_ConnFailTimes_Flag=false;
		if(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes>100){
			rrc_ConnFailTimes_Flag=true;
		}
		
		boolean dropTimes_Flag=false;
		if(wireDrop_DropTimes_CellLevel>150){
			dropTimes_Flag=true;
		}
	
		boolean zeroFlow_ZeroTeltra_Flag=false;
		if(rrc_ConnEstabSucc_ReqTimes>3&&emUplinkSerBytes-emDownlinkSerBytes==0){
			zeroFlow_ZeroTeltra_Flag=true;
		}
	
		boolean switchSuccRate_Flag=false;
		if(switchSucc<=60&&switchSucc_ReqTimes>100){
			switchSuccRate_Flag=true;
		}

	
		boolean wireDropRate_Flag=false;
		if(wireDrop_CellLevel>=20&&rrc_ConnEstabSucc_ReqTimes+erab_EstabSucc_ReqTimes>100){
			wireDropRate_Flag=true;
		}
	
		boolean wireConnRate_Flag=false;
		if(rrc_ConnEstabSucc_ReqTimes>100&&wireConn<=70){
			wireConnRate_Flag=true;
		}
	
		boolean connFailTimes_Flag=false;
		if((rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)+(erab_EstabSucc_ReqTimes-erab_EstabSucc_SuccTimes)>200){
			connFailTimes_Flag=true;
		}		
	
		boolean switchFailTimes_Flag=false;
		if(switchSucc_ReqTimes-switchSucc_SuccTimes>=150){
			switchFailTimes_Flag=true;
		}
		//包装返回结果
		//通用阈值
		stsTh.setErab_DropRate_Flag(erab_DropRate_Flag);
		stsTh.setErab_EstabSuccRate_Flag(erab_EstabSuccRate_Flag);
		stsTh.setRrc_ConnEstabSuccRate_Flag(rrc_ConnEstabSuccRate_Flag);
		stsTh.setZeroFlow_ZeroTeltra_Flag(zeroFlow_ZeroTeltra_Flag);
		stsTh.setWireConnRate_Flag(wireConnRate_Flag);
		//VIP和普通小区有区别
		stsTh.setCellBar_rrc_Conn_FailTimes_Flag(cellBar_rrc_Conn_FailTimes_Flag);
		stsTh.setCellBar_DropTimes_Flag(cellBar_DropTimes_Flag);
		stsTh.setRrc_ConnFailTimes_Flag(rrc_ConnFailTimes_Flag);
		stsTh.setDropTimes_Flag(dropTimes_Flag);
		stsTh.setConnFailTimes_Flag(connFailTimes_Flag);
		stsTh.setSwitchSuccRate_Flag(switchSuccRate_Flag);
		stsTh.setSwitchFailTimes_Flag(switchFailTimes_Flag);
		stsTh.setWireDropRate_Flag(wireDropRate_Flag);
		return stsTh;
		}
	private static String formatData(float data){
		DecimalFormat   df   =new  DecimalFormat("0.##%");
		return df.format(data/100);
	}
}
