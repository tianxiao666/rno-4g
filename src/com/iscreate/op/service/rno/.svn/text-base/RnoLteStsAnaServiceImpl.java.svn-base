package com.iscreate.op.service.rno;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4StsDescQueryCond;
import com.iscreate.op.action.rno.model.LteStsCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.rno.RnoLteStsAnaDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoLteCellForPm;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.tool.CoordinateHelper;

public class RnoLteStsAnaServiceImpl implements RnoLteStsAnaService {
	private static Logger log = LoggerFactory.getLogger(RnoLteStsAnaServiceImpl.class);
	
	private RnoCellDao rnoCellDao;

	private RnoLteStsAnaDao rnoLteStsAnaDao;
	
	private MemcachedClient memCached;

	public RnoLteStsAnaDao getRnoLteStsAnaDao() {
		return rnoLteStsAnaDao;
	}
	public void setRnoLteStsAnaDao(RnoLteStsAnaDao rnoLteStsAnaDao) {
		this.rnoLteStsAnaDao = rnoLteStsAnaDao;
	}
public RnoCellDao getRnoCellDao() {
		return rnoCellDao;
	}
	public MemcachedClient getMemCached() {
		return memCached;
	}
	public void setRnoCellDao(RnoCellDao rnoCellDao) {
		this.rnoCellDao = rnoCellDao;
	}
	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}
/**
 * 查询话统数据的记录数
 * @param cond
 * @return
 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#queryG4StsDescCnt(com.iscreate.op.action.rno.model.G4StsDescQueryCond)
 * @author chen.c10
 * @date 2015-7-17 下午7:26:36
 * @company 怡创科技
 * @version V1.0
 */
	public long queryG4StsDescCnt(final G4StsDescQueryCond cond){
		return rnoLteStsAnaDao.queryG4StsDescCnt(cond);
	}
/**
 * 分页查询符合条件的话统数据
 * @param cond
 * @param page
 * @return
 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#queryG4StsDescByPage(com.iscreate.op.action.rno.model.G4StsDescQueryCond, com.iscreate.op.action.rno.Page)
 * @author chen.c10
 * @date 2015-7-17 下午7:26:05
 * @company 怡创科技
 * @version V1.0
 */
	public List<Map<String, Object>> queryG4StsDescByPage(final 
			G4StsDescQueryCond cond,final Page page){
		return rnoLteStsAnaDao.queryG4StsDescByPage(cond, page);
	}
	/**
	 * 查询4G话统数据
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#queryG4PmDate()
	 * @author chen.c10
	 * @date 2015-7-20 下午2:11:46
	 * @company 怡创科技
	 * @version V1.0
	 */
public List<Map<String, Object>> queryG4StsData(G4StsDescQueryCond g4StsDescQueryCond) {
	return rnoLteStsAnaDao.queryG4StsData(g4StsDescQueryCond);
}
/**
 * 查询4G话统小区名列表
 * @return
 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#queryG4StsCellNameList()
 * @author chen.c10
 * @date 2015-7-21 下午2:55:16
 * @company 怡创科技
 * @version V1.0
 */
public List<String> queryG4StsCellNameList(){
	return rnoLteStsAnaDao.queryG4StsCellNameList();
}
/**
 * 通过MapGrid获取小区集合，并通过PmCellList进行筛选
 * @param areaId
 * @param mapGrid
 * @param page
 * @param freqType
 * @param standardPoints
 * @param cellNames
 * @return
 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#getLteCellByMapGridWithPmNameCellList(long, java.util.Map, com.iscreate.op.action.rno.Page, java.lang.String, java.util.Map, java.util.List)
 * @author chen.c10
 * @date 2015-7-22 下午2:21:45
  * @company 怡创科技
 * @version V1.0
 */
@SuppressWarnings("unchecked")
public LteStsCellQueryResult getLteCellByMapGridWithStsCellList(long areaId,
		Map<String, String> mapGrid, Page page, String freqType,
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
	log.info("进入方法：getLteCellByMapGrid 。areaid=" + areaId + ", mapGrid="
			+ mapGrid + " page=" + page + ", freqType=" + freqType);
	List<RnoLteCellForPm> lteCells = new ArrayList<RnoLteCellForPm>();
	//刷新缓存
	String version = "v2";
	//获取地图网格id
	String mapGridId = mapGrid.get("gridId").toString();
	//获取地图网格的坐标范围
	String minLng = mapGrid.get("minLng").toString();
	String minLat = mapGrid.get("minLat").toString();
	String maxLng = mapGrid.get("maxLng").toString();
	String maxLat = mapGrid.get("maxLat").toString();
	//将网格坐标存入缓存
	String mapGridCoordinates = null;
	String[] blPoint =  null;
	String[] trPoint = null;
	int changeTimes = 0;
	String mapGridKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
			+ "STS_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_" + version + "_MapGrid";
	try {
		mapGridCoordinates = (String) memCached.get(mapGridKey);
	} catch (Exception e) {
		e.printStackTrace();
	}
	if(mapGridCoordinates!=null){
		blPoint = mapGridCoordinates.split("_")[0].split(",");
		trPoint = mapGridCoordinates.split("_")[1].split(",");	
	}
	if(blPoint==null||trPoint==null||blPoint.length!=2||trPoint.length!=2){
		do{
			//左下角gps坐标
			blPoint = CoordinateHelper.changeFromBaiduToGps(minLng, minLat);
			//右上角gps坐标
			trPoint = CoordinateHelper.changeFromBaiduToGps(maxLng, maxLat);
		}while((blPoint==null||trPoint==null)&&++changeTimes<5);
		if(blPoint!=null&&trPoint!=null){
			mapGridCoordinates = blPoint[0] + "," + blPoint[1] + "_" + trPoint[0] + "," + trPoint[1];
			try {
				memCached.set(mapGridKey, RnoConstant.TimeConstant.TokenTime,	mapGridCoordinates);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//参数 + 区域id + 网格id + 当前页码
	String cacheKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
			+ "STS_" + freqType + "_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_" + page.getCurrentPage() + "_" + version;

	//单独获取对应的所有区域id
	String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(areaId);
	//获取total count
	Integer totalCnt = null;
    String totalCountKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
    		+ "STS_" + freqType + "_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_" + version + "_TotalCnt";
    try{
    	totalCnt = (Integer)memCached.get(totalCountKey);
    }catch(Exception e){
    	
    }
    if(totalCnt==null){
    	//获取总数
    	log.debug("获取区域["+areaId+"]的网格["+mapGridId+"]小区总数量");
    	totalCnt = rnoLteStsAnaDao.getRnoLteStsCellCntByGridInArea(areaStr, blPoint,
				trPoint, freqType);
    	try {
			memCached.set(totalCountKey, RnoConstant.TimeConstant.TokenTime,
					totalCnt);
		} catch (Exception e) {
		}
//    	//page cnt
//    	String totalPageKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
//				+ freqType + "_" + areaId + "_" + mapGridId + "_TotalPage";
//        int pageCnt = totalCnt / page.getPageSize()
//				+ (totalCnt % page.getPageSize() == 0 ? 0 : 1);
//        try {
//        	memCached.set(totalPageKey, RnoConstant.TimeConstant.TokenTime,
//        		pageCnt);
//        } catch (Exception e) {
//		}
		
    }
    try {
		log.info("从缓存中获取->");
		lteCells = (List<RnoLteCellForPm>) memCached.get(cacheKey);
		log.info("缓存获取的结果数量为 ："
				+ ((lteCells == null) ? 0 : lteCells.size()));
	} catch (Exception e) {
		e.printStackTrace();
	}
    int faultCnt=0;
	List<String> lteCellNames=null;
	Map<String,List<RnoLteCellForPm>> lteStsCells = null;
    if(lteCells==null){
    	lteCells = new ArrayList<RnoLteCellForPm>();
		log.info("从数据库获取->");
		lteCellNames = rnoLteStsAnaDao.getRnoLteCellByGridAndPageInAreaWithSts(areaStr, blPoint,trPoint, page);		
		lteStsCells = rnoLteStsAnaDao.getRnoLteCellForStsByCellNameList(lteCellNames);
		
		List<RnoLteCellForPm> pmCells =null;
		RnoLteCellForPm lteCell = null;
		List<Date> dates = null;
		List<Boolean> flags = null;
		//for(String cellName:lteCellNames){
		//pmCells= rnoLteStsAnaDao.getRnoLteCellForPmByCellName(cellName);
		for(String cellName:lteStsCells.keySet()){
			pmCells= lteStsCells.get(cellName);
		
		lteCell = new RnoLteCellForPm();
		dates = new ArrayList<Date>();
		flags = new ArrayList<Boolean>();
		boolean cellBar_rrc_Conn_FailTimes_OverFlag=false;
		boolean cellBar_DropTimes_OverFlag=false;
		boolean erab_DropRate_OverFlag=false;
		boolean erab_EstabSuccRate_OverFlag=false;
		boolean rrc_ConnEstabSuccRate_OverFlag=false;
		boolean rrc_ConnFailTimes_OverFlag=false;
		boolean dropTimes_OverFlag=false;
		boolean zeroFlow_ZeroTeltra_OverFlag=false;
		boolean switchSuccRate_OverFlag=false;
		boolean wireDropRate_OverFlag=false;
		boolean wireConnRate_OverFlag=false;		
		boolean connFailTimes_OverFlag=false;
		boolean cellBar_rrc_Conn_FailTimes_4Times_OverFlag=false;
		boolean cellBar_DropTimes_4Times_OverFlag=false;
		boolean rrc_ConnFailTimes_4Times_OverFlag=false;
		boolean dropTimes_4Times_OverFlag=false;
		boolean switchSuccRate_4Times_OverFlag=false;
		boolean switchFailTimes_OverFlag=false;
		boolean wireDropRate_4Times_OverFlag=false;
		
		int cellBar_rrc_Conn_FailTimes_Cnt=0;
		int cellBar_DropTimes_Cnt=0;
		int erab_DropRate_Cnt=0;
		int erab_EstabSuccRate_Cnt=0;
		int rrc_ConnEstabSuccRate_Cnt=0;
		int rrc_ConnFailTimes_Cnt=0;
		int dropTimes_Cnt=0;
		int zeroFlow_ZeroTeltra_Cnt=0;
		int switchSuccRate_Cnt=0;
		int wireDropRate_Cnt=0;
		int wireConnRate_Cnt=0;
		int connFailTimes_Cnt=0;
		int cellBar_rrc_Conn_FailTimes_4Times_Cnt=0;
		int cellBar_DropTimes_4Times_Cnt=0;
		int rrc_ConnFailTimes_4Times_Cnt=0;
		int dropTimes_4Times_Cnt=0;
		int switchSuccRate_4Times_Cnt=0;
		int switchFailTimes_Cnt=0;
		int wireDropRate_4Times_Cnt=0;
		
		for(RnoLteCellForPm pmCell:pmCells){
			
			dates.add(pmCell.getBeginTime()); 
			if(lteCell.getId()==null) lteCell.setId(pmCell.getId());
			if(lteCell.getCell()==null) lteCell.setCell(pmCell.getCell());
			if(lteCell.getLng()==null) lteCell.setLng(pmCell.getLng());
			if(lteCell.getLat()==null) lteCell.setLat(pmCell.getLat());
			if(lteCell.getChineseName()==null) lteCell.setChineseName(pmCell.getChineseName());
			if(lteCell.getAzimuth()==null) lteCell.setAzimuth(pmCell.getAzimuth());
			if(lteCell.getMapType()==null) lteCell.setMapType(pmCell.getMapType());
			if(lteCell.getShapeType()==null) lteCell.setShapeType(pmCell.getShapeType());
			if(lteCell.getAllLngLats()==null) lteCell.setAllLngLats(pmCell.getAllLngLats());
			if(lteCell.getPci()==null) lteCell.setPci(pmCell.getPci());
			
			float wireConn = 100;
			if (pmCell.getErab_NbrAttEstab() * pmCell.getRrc_AttConnEstab() != 0	&& pmCell.getRrc_AttConnEstab() != 0) {
				wireConn = 100 * pmCell.getErab_NbrSuccEstab()/ pmCell.getErab_NbrAttEstab() * pmCell.getRrc_SuccConnEstab() / pmCell.getRrc_AttConnEstab();
			}
			float wireDrop_CellLevel = 0;
			if(pmCell.getContext_SuccInitalSetup()!=0){
				wireDrop_CellLevel = (pmCell.getContext_AttRelEnb()-pmCell.getContext_AttRelEnb_Normal())/pmCell.getContext_SuccInitalSetup()*100;
			}
			float erab_EstabSucc = 100;
			if (pmCell.getErab_NbrAttEstab() != 0) {
				erab_EstabSucc = 100 * pmCell.getErab_NbrSuccEstab()/ pmCell.getErab_NbrAttEstab();
			}
			float rrc_ConnEstabSucc = 100;
			if (pmCell.getRrc_AttConnEstab() != 0) {
				rrc_ConnEstabSucc = 100* pmCell.getRrc_SuccConnEstab()/ pmCell.getRrc_AttConnEstab();
			}
			float emUplinkSerBytes = pmCell.getPdcp_UpOctUl();
			float emDownlinkSerBytes = pmCell.getPdcp_UpOctDl();
			float switchSucc=100;
			if ((pmCell.getHo_AttOutInterEnbS1() + pmCell.getHo_AttOutInterEnbX2() + pmCell.getHo_AttOutIntraEnb()) != 0) {
				switchSucc = 100	* (pmCell.getHo_SuccOutInterEnbS1()+ pmCell.getHo_SuccOutInterEnbX2() + pmCell.getHo_SuccOutIntraEnb())
						/ (pmCell.getHo_AttOutInterEnbS1()+ pmCell.getHo_AttOutInterEnbX2() + pmCell.getHo_AttOutIntraEnb());
			}
			float erab_EstabSucc_SuccTimes = pmCell.getErab_NbrSuccEstab();
			float erab_EstabSucc_ReqTimes = pmCell.getErab_NbrAttEstab();
			float erab_Drop_CellLevel = 0;
			if (pmCell.getErab_NbrSuccEstab() != 0) {
				erab_Drop_CellLevel = 100	* (pmCell.getErab_NbrReqRelEnb()- pmCell.getErab_NbrReqRelEnb_Normal() + pmCell.getErab_HoFail())
						/(pmCell.getErab_NbrSuccEstab()+ pmCell.getHo_SuccOutInterEnbS1()+ pmCell.getHo_SuccOutInterEnbX2() + pmCell.getHo_AttOutIntraEnb());
			}
			float switchSucc_SuccTimes = pmCell.getHo_SuccOutInterEnbS1()+pmCell.getHo_SuccOutInterEnbX2()+pmCell.getHo_SuccOutIntraEnb();
			float switchSucc_ReqTimes = pmCell.getHo_AttOutInterEnbS1()+pmCell.getHo_AttOutInterEnbX2()+pmCell.getHo_AttOutIntraEnb();
			//float wireDrop_ReqTimes_CellLevel = pmCell.getContext_SuccInitalSetup();
			//float wireConn_ReqTimes = pmCell.getErab_NbrAttEstab()+pmCell.getRrc_AttConnEstab();
			//float erab_Drop_DropTimes_CellLevel = pmCell.getErab_NbrReqRelEnb()-pmCell.getErab_NbrReqRelEnb_Normal()+pmCell.getErab_HoFail();
			//float wireConn_SuccTimes = pmCell.getErab_NbrSuccEstab()*pmCell.getRrc_SuccConnEstab();
			float rrc_ConnEstabSucc_SuccTimes = pmCell.getRrc_SuccConnEstab();
			float rrc_ConnEstabSucc_ReqTimes = pmCell.getRrc_AttConnEstab();
			float wireDrop_DropTimes_CellLevel = pmCell.getContext_AttRelEnb()-pmCell.getContext_AttRelEnb_Normal();
			
				
			boolean cellBar_rrc_Conn_FailTimes_Flag=false;
			if(rrc_ConnEstabSucc_SuccTimes<70&&(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)>200){
				cellBar_rrc_Conn_FailTimes_Flag=true;
			}
			if(cellBar_rrc_Conn_FailTimes_Flag){
				if(++cellBar_rrc_Conn_FailTimes_Cnt==2){
					cellBar_rrc_Conn_FailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_rrc_Conn_FailTimes_Cnt=0;
			}
			
			boolean cellBar_DropTimes_Flag=false;
			if(wireDrop_CellLevel>50&&wireDrop_DropTimes_CellLevel>200){
				cellBar_DropTimes_Flag=true;
			}
			if(cellBar_DropTimes_Flag){
				if(++cellBar_DropTimes_Cnt==2){
					cellBar_DropTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_DropTimes_Cnt=0;
			}			
			
			boolean erab_DropRate_Flag=false;
			if(erab_Drop_CellLevel>=10&&erab_EstabSucc_SuccTimes>20){
				erab_DropRate_Flag=true;
			}
			if(erab_DropRate_Flag){
				if(++erab_DropRate_Cnt==2){
					erab_DropRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				erab_DropRate_Cnt=0;
			}
			
			boolean erab_EstabSuccRate_Flag=false;
			if(erab_EstabSucc<=85&&erab_EstabSucc_ReqTimes>20){
				erab_EstabSuccRate_Flag=true;
			}
			if(erab_EstabSuccRate_Flag){
				if(++erab_EstabSuccRate_Cnt==2){
					erab_EstabSuccRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				erab_EstabSuccRate_Cnt=0;
			}
			
			boolean rrc_ConnEstabSuccRate_Flag=false;
			if(rrc_ConnEstabSucc<=85&&rrc_ConnEstabSucc_ReqTimes>20){
				erab_EstabSuccRate_Flag=true;
			}
			if(rrc_ConnEstabSuccRate_Flag){
				if(++rrc_ConnEstabSuccRate_Cnt==2){
					rrc_ConnEstabSuccRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				rrc_ConnEstabSuccRate_Cnt=0;
			}
			
			boolean rrc_ConnFailTimes_Flag=false;
			if(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes>100){
				rrc_ConnFailTimes_Flag=true;
			}
			if(rrc_ConnFailTimes_Flag){
				if(++rrc_ConnFailTimes_Cnt==2){
					rrc_ConnFailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				rrc_ConnFailTimes_Cnt=0;
			}
			
			boolean dropTimes_Flag=false;
			if(wireDrop_DropTimes_CellLevel>150){
				dropTimes_Flag=true;
			}
			if(dropTimes_Flag){
				if(++dropTimes_Cnt==2){
					dropTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				dropTimes_Cnt=0;
			}
			
			boolean zeroFlow_ZeroTeltra_Flag=false;
			if(rrc_ConnEstabSucc_ReqTimes>3&&emUplinkSerBytes-emDownlinkSerBytes==0){
				zeroFlow_ZeroTeltra_Flag=true;
			}
			if(zeroFlow_ZeroTeltra_Flag){
				if(++zeroFlow_ZeroTeltra_Cnt==2){
					zeroFlow_ZeroTeltra_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				zeroFlow_ZeroTeltra_Cnt=0;
			}
			
			boolean switchSuccRate_Flag=false;
			if(switchSucc<=60&&switchSucc_ReqTimes>100){
				switchSuccRate_Flag=true;
			}
			if(switchSuccRate_Flag){
				if(++switchSuccRate_Cnt==2){
					switchSuccRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				switchSuccRate_Cnt=0;
			}
			
			boolean wireDropRate_Flag=false;
			if(wireDrop_CellLevel>=20&&rrc_ConnEstabSucc_ReqTimes+erab_EstabSucc_ReqTimes>100){
				wireDropRate_Flag=true;
			}
			if(wireDropRate_Flag){
				if(++wireDropRate_Cnt==2){
					wireDropRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				wireDropRate_Cnt=0;
			}
			
			boolean wireConnRate_Flag=false;
			if(rrc_ConnEstabSucc_ReqTimes>100&&wireConn<=70){
				wireConnRate_Flag=true;
			}
			if(wireConnRate_Flag){
				if(++wireConnRate_Cnt==2){
					wireConnRate_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				wireConnRate_Cnt=0;
			}
			boolean commFlag=(cellBar_rrc_Conn_FailTimes_Flag||cellBar_DropTimes_Flag||erab_DropRate_Flag||erab_EstabSuccRate_Flag||rrc_ConnEstabSuccRate_Flag||rrc_ConnFailTimes_Flag||dropTimes_Flag||zeroFlow_ZeroTeltra_Flag||switchSuccRate_Flag||wireDropRate_Flag||wireConnRate_Flag);
			boolean vipFlag = false;
			if(pmCell.getImpotGrade()==null){
				
			boolean cellBar_rrc_Conn_FailTimes_4Times_Flag=false;
			if(rrc_ConnEstabSucc_SuccTimes<70&&(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)>300){
				cellBar_rrc_Conn_FailTimes_4Times_Flag=true;
			}
			if(cellBar_rrc_Conn_FailTimes_4Times_Flag){
				if(++cellBar_rrc_Conn_FailTimes_4Times_Cnt==4){
					cellBar_rrc_Conn_FailTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_rrc_Conn_FailTimes_4Times_Cnt=0;
			}
			
			boolean cellBar_DropTimes_4Times_Flag=false;
			if(wireDrop_CellLevel>50&&wireDrop_DropTimes_CellLevel>300){
				cellBar_DropTimes_4Times_Flag=true;
			}
			if(cellBar_DropTimes_4Times_Flag){
				if(++cellBar_DropTimes_4Times_Cnt==4){
					cellBar_DropTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_DropTimes_4Times_Cnt=0;
			}			
						
			boolean rrc_ConnFailTimes_4Times_Flag=false;
			if(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes>200){
				rrc_ConnFailTimes_4Times_Flag=true;
			}
			if(rrc_ConnFailTimes_4Times_Flag){
				if(++rrc_ConnFailTimes_4Times_Cnt==4){
					rrc_ConnFailTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				rrc_ConnFailTimes_4Times_Cnt=0;
			}
			
			boolean dropTimes_4Times_Flag=false;
			if(wireDrop_DropTimes_CellLevel>200){
				dropTimes_4Times_Flag=true;
			}
			if(dropTimes_4Times_Flag){
				if(++dropTimes_4Times_Cnt==4){
					dropTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				dropTimes_4Times_Cnt=0;
			}
			
			boolean connFailTimes_Flag=false;
			if((rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)+(erab_EstabSucc_ReqTimes-erab_EstabSucc_SuccTimes)>200){
				connFailTimes_Flag=true;
			}
			if(connFailTimes_Flag){
				if(++connFailTimes_Cnt==4){
					connFailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				connFailTimes_Cnt=0;
			}			
			
			boolean switchSuccRate_4Times_Flag=false;
			if(switchSucc<=60&&switchSucc_ReqTimes>100){
				switchSuccRate_4Times_Flag=true;
			}
			if(switchSuccRate_4Times_Flag){
				if(++switchSuccRate_4Times_Cnt==4){
					switchSuccRate_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				switchSuccRate_4Times_Cnt=0;
			}
			
			boolean switchFailTimes_Flag=false;
			if(switchSucc_ReqTimes-switchSucc_SuccTimes>=150){
				switchFailTimes_Flag=true;
			}
			if(switchFailTimes_Flag){
				if(++switchFailTimes_Cnt==4){
					switchFailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				switchFailTimes_Cnt=0;
			}
			
			boolean wireDropRate_4Times_Flag=false;
			if(wireDrop_CellLevel>=20&&rrc_ConnEstabSucc_ReqTimes+erab_EstabSucc_ReqTimes>100){
				wireDropRate_4Times_Flag=true;
			}
			if(wireDropRate_4Times_Flag){
				if(++wireDropRate_4Times_Cnt==4){
					wireDropRate_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				wireDropRate_4Times_Cnt=0;
			}
			vipFlag=(cellBar_rrc_Conn_FailTimes_4Times_Flag||cellBar_DropTimes_4Times_Flag||rrc_ConnFailTimes_4Times_Flag||dropTimes_4Times_Flag||connFailTimes_Flag||switchSuccRate_4Times_Flag||switchFailTimes_Flag||wireDropRate_4Times_Flag);
		}else{
			
			boolean cellBar_rrc_Conn_FailTimes_4Times_Flag=false;
			if(rrc_ConnEstabSucc_SuccTimes<70&&(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)>300){
				cellBar_rrc_Conn_FailTimes_4Times_Flag=true;
			}
			if(cellBar_rrc_Conn_FailTimes_4Times_Flag){
				if(++cellBar_rrc_Conn_FailTimes_4Times_Cnt==2){
					cellBar_rrc_Conn_FailTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_rrc_Conn_FailTimes_4Times_Cnt=0;
			}
			
			boolean cellBar_DropTimes_4Times_Flag=false;
			if(wireDrop_CellLevel>50&&wireDrop_DropTimes_CellLevel>300){
				cellBar_DropTimes_4Times_Flag=true;
			}
			if(cellBar_DropTimes_4Times_Flag){
				if(++cellBar_DropTimes_4Times_Cnt==2){
					cellBar_DropTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				cellBar_DropTimes_4Times_Cnt=0;
			}			
						
			boolean rrc_ConnFailTimes_4Times_Flag=false;
			if(rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes>200){
				rrc_ConnFailTimes_4Times_Flag=true;
			}
			if(rrc_ConnFailTimes_4Times_Flag){
				if(++rrc_ConnFailTimes_4Times_Cnt==2){
					rrc_ConnFailTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				rrc_ConnFailTimes_4Times_Cnt=0;
			}
			
			boolean dropTimes_4Times_Flag=false;
			if(wireDrop_DropTimes_CellLevel>200){
				dropTimes_4Times_Flag=true;
			}
			if(dropTimes_4Times_Flag){
				if(++dropTimes_4Times_Cnt==2){
					dropTimes_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				dropTimes_4Times_Cnt=0;
			}
			
			boolean connFailTimes_Flag=false;
			if((rrc_ConnEstabSucc_ReqTimes-rrc_ConnEstabSucc_SuccTimes)+(erab_EstabSucc_ReqTimes-erab_EstabSucc_SuccTimes)>200){
				connFailTimes_Flag=true;
			}
			if(connFailTimes_Flag){
				if(++connFailTimes_Cnt==2){
					connFailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				connFailTimes_Cnt=0;
			}			
			
			boolean switchSuccRate_4Times_Flag=false;
			if(switchSucc<=60&&switchSucc_ReqTimes>100){
				switchSuccRate_4Times_Flag=true;
			}
			if(switchSuccRate_4Times_Flag){
				if(++switchSuccRate_4Times_Cnt==2){
					switchSuccRate_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				switchSuccRate_4Times_Cnt=0;
			}
			
			boolean switchFailTimes_Flag=false;
			if(switchSucc_ReqTimes-switchSucc_SuccTimes>=150){
				switchFailTimes_Flag=true;
			}
			if(switchFailTimes_Flag){
				if(++switchFailTimes_Cnt==2){
					switchFailTimes_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				switchFailTimes_Cnt=0;
			}
			
			boolean wireDropRate_4Times_Flag=false;
			if(wireDrop_CellLevel>=20&&rrc_ConnEstabSucc_ReqTimes+erab_EstabSucc_ReqTimes>100){
				wireDropRate_4Times_Flag=true;
			}
			if(wireDropRate_4Times_Flag){
				if(++wireDropRate_4Times_Cnt==2){
					wireDropRate_4Times_OverFlag=true;
					flags.add(true);
					break;
				}
			}else{
				wireDropRate_4Times_Cnt=0;
			}						
			vipFlag=(cellBar_rrc_Conn_FailTimes_4Times_Flag||cellBar_DropTimes_4Times_Flag||rrc_ConnFailTimes_4Times_Flag||dropTimes_4Times_Flag||connFailTimes_Flag||switchSuccRate_4Times_Flag||switchFailTimes_Flag||wireDropRate_4Times_Flag);
		}
			flags.add(commFlag||vipFlag);
		}
		
		boolean thFlag = (cellBar_rrc_Conn_FailTimes_OverFlag||cellBar_DropTimes_OverFlag||erab_DropRate_OverFlag||erab_EstabSuccRate_OverFlag||rrc_ConnEstabSuccRate_OverFlag||rrc_ConnFailTimes_OverFlag||dropTimes_OverFlag||zeroFlow_ZeroTeltra_OverFlag||switchSuccRate_OverFlag||wireDropRate_OverFlag||wireConnRate_OverFlag||connFailTimes_OverFlag||cellBar_rrc_Conn_FailTimes_4Times_OverFlag||cellBar_DropTimes_4Times_OverFlag||rrc_ConnFailTimes_4Times_OverFlag||dropTimes_4Times_OverFlag||switchSuccRate_4Times_OverFlag||switchFailTimes_OverFlag||wireDropRate_4Times_OverFlag);
		if(thFlag) faultCnt++;
		lteCell.setThFlag(thFlag);
		lteCell.setBeginTimes(dates);
		lteCell.setFlags(flags);
		lteCells.add(lteCell);
/*		for(RnoLteCellForPm lteCell:lteCells){
			for(String pmCellName:cellNames){
				if(lteCell.getChineseName()==pmCellName||lteCell.getChineseName().equals(pmCellName)){
					pmCells.add(lteCell);
				}
			}
		}
		lteCells.clear();
		lteCells.addAll(pmCells);
		pmCells.clear();*/
		}
		log.info("从数据库中获取结果数量为：" + (lteCells == null ? 0 : lteCells.size()));
		//纠偏基准点
		for (int i = 0; i < lteCells.size(); i++) {
			String cellBaiduPoint[] = CoordinateHelper.getBaiduLnglat(lteCells.get(i).getLng(),
					lteCells.get(i).getLat(), standardPoints);
			if(cellBaiduPoint != null) {
				lteCells.get(i).setLng(Double.parseDouble(cellBaiduPoint[0]));
				lteCells.get(i).setLat(Double.parseDouble(cellBaiduPoint[1]));
			}
			String[] allLngLats = lteCells.get(i).getAllLngLats().trim().split(";");
			if(allLngLats.length<3) continue;
			String newLngLats = "";
			for (String one : allLngLats) {
				String onePoint[] = one.trim().split(",");
				if(onePoint.length<2) continue;
				String point[] = CoordinateHelper.getBaiduLnglat(Double.parseDouble(onePoint[0]),
						Double.parseDouble(onePoint[1]), standardPoints);
				if(cellBaiduPoint != null) {
					newLngLats += point[0]+","+point[1];
				}
				newLngLats += ";";
			}
			newLngLats = newLngLats.substring(0,newLngLats.length()-1);
			lteCells.get(i).setAllLngLats(newLngLats);
			}
		log.info("放入缓存->");
		try {
			memCached.set(cacheKey, RnoConstant.TimeConstant.TokenTime,
					lteCells);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	LteStsCellQueryResult result = new LteStsCellQueryResult();
	result.setFaultCnt(faultCnt);
	result.setTotalCnt(totalCnt);
	result.setLteCells(lteCells);
	return result;
	}

	/**
	 * 获取4G话统指标项 
	 * @return
	 * @see com.iscreate.op.service.rno.RnoLteStsAnaService#getRno4GStsIndexDesc()
	 * @author chen.c10
	 * @date 2015-9-17 下午5:03:45
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> getRno4GStsIndexDesc() {
		return rnoLteStsAnaDao.getRno4GStsIndexDesc();
	}
}