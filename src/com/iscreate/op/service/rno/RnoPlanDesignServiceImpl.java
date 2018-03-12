package com.iscreate.op.service.rno;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.jbpm.test.Db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoPlanDesignDao;
import com.iscreate.op.dao.rno.RnoTaskDao;
import com.iscreate.op.pojo.rno.AdjFreqInterDetailed;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.CobsicCells;
import com.iscreate.op.pojo.rno.FreqInterIndex;
import com.iscreate.op.pojo.rno.FreqReuseInfo;
import com.iscreate.op.pojo.rno.NcellAnalysisCondition;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RedundantNCell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoCellStructDesc;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoInterferenceDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;
import com.iscreate.op.pojo.rno.RnoTask;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoPlanDesignServiceImpl implements RnoPlanDesignService {
	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoPlanDesignServiceImpl.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	private RnoPlanDesignDao rnoPlanDesignDao;
	private RnoTaskDao rnoTaskDao;
	private MemcachedClient memCached;

	public RnoPlanDesignDao getRnoPlanDesignDao() {
		return rnoPlanDesignDao;
	}

	public void setRnoPlanDesignDao(RnoPlanDesignDao rnoPlanDesignDao) {
		this.rnoPlanDesignDao = rnoPlanDesignDao;
	}

	public MemcachedClient getMemCached() {
		return memCached;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public RnoTaskDao getRnoTaskDao() {
		return rnoTaskDao;
	}

	public void setRnoTaskDao(RnoTaskDao rnoTaskDao) {
		this.rnoTaskDao = rnoTaskDao;
	}

	/**
	 * 根据areaid获取系统配置方案名称
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getSysCoufigureSchemeFromRnoCellDesc(
			final Long areaId) {

		return rnoPlanDesignDao.getSysCoufigureSchemeFromRnoCellDesc(areaId);
	}

	/**
	 * 根据areaid获取临时方案名称
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getTempAnalyseSchemeFromRnoCellDesc(
			final long areaId) {

		return rnoPlanDesignDao.getTempAnalyseSchemeFromRnoCellDesc(areaId);
	}

	/**
	 * 根据configId获取RNO_CELL_DESCRIPTOR
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getRnoCellDescByConfigId(final long configId) {

		return rnoPlanDesignDao.getRnoCellDescByConfigId(configId);
	}

	/**
	 * 
	 * @description: 统计指定区域范围小区的频率复用情况
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return
	 * @return Map<Integer,Object>
	 * @date：Nov 7, 2013 11:59:55 AM
	 */
	public Map<Integer, Object> staticsFreqReuseInfoInArea(
			PlanConfig selectConfig) {// 暂时实现功能
		String cachekey = RnoConstant.CacheConstant.STATICS_REPORT_FREQREUSE_IN_LIST_PRE
				+ selectConfig.getConfigId();
		Map<Integer, Object> resultMap = null;// 返回结果resultMap
		if (selectConfig == null) {
			return null;
		}
		// 看缓存是否有
		try {
			resultMap = memCached.get(cachekey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultMap == null) {
			List<Map<String, Object>> freqReuseLists = this.rnoPlanDesignDao
					.staticsFreqReuseInfoInArea(selectConfig);// 查询bcch tch
			if (freqReuseLists == null || !freqReuseLists.isEmpty()) {
				resultMap = new TreeMap<Integer, Object>();// 返回结果resultMap
															// treeMap 排序
				for (Map<String, Object> map : freqReuseLists) {// 遍历结果
					String bcch = map.get("BCCH") + "";// bcch 频点
					String tch = map.get("TCH") + "";// tch 频点
					if (!"null".equals(bcch) && !"".equals(bcch)) {// bcch 频点复用
						int key = Integer.valueOf(bcch);
						if (resultMap.containsKey(key)) {
							FreqReuseInfo freInfo = (FreqReuseInfo) resultMap
									.get(key);
							freInfo.setBcchCount(freInfo.getBcchCount() + 1);
						} else {
							FreqReuseInfo freInfo = new FreqReuseInfo();
							freInfo.setFreq(Integer.valueOf(bcch));
							freInfo.setBcchCount(1);
							freInfo.setTchCount(0);
							resultMap.put(freInfo.getFreq(), freInfo);
						}
					}
					if (!"null".equals(tch) && !"".equals(tch)) {// tch 频点复用
						if (tch != null && !"".equals(tch)) {
							String[] tchArr = tch.split(",");
							for (String tchValue : tchArr) {
								int key = Integer.valueOf(tchValue);
								if (resultMap.containsKey(key)) {
									FreqReuseInfo freInfo = (FreqReuseInfo) resultMap
											.get(key);
									freInfo.setTchCount(freInfo.getTchCount() + 1);
								} else {
									FreqReuseInfo freInfo = new FreqReuseInfo();
									freInfo.setFreq(Integer.valueOf(tchValue));
									freInfo.setTchCount(1);
									freInfo.setBcchCount(0);
									resultMap.put(freInfo.getFreq(), freInfo);
								}
							}
						}
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * @description: 根据类型获取选择的小区配置或小区干扰中分析列表中的小区的gis信息
	 * @author：yuan.yw
	 * @param selectConfig
	 * @param page
	 * @return
	 * @return GisCellQueryResult
	 * @date：Nov 8, 2013 10:08:37 AM
	 */
	public GisCellQueryResult getFreqReuseCellGisInfoFromSelectionList(
			PlanConfig selectConfig, Page page) {
		log.info("进入getFreqReuseCellGisInfoFromSelectionList。 selectConfig="
				+ selectConfig + ",page=" + page);

		List<RnoGisCell> gisCells = null;
		if (selectConfig == null) {
			return null;
		}

		GisCellQueryResult result = new GisCellQueryResult();
		result.setTotalCnt(0);

		String key = RnoConstant.CacheConstant.STATICS_FREQREUSE_GISCELL_IN_SELECTION_LIST_PRE
				+ selectConfig.getConfigId();

		// 看缓存是否有
		try {
			gisCells = memCached.get(key);
			log.info("从缓存获取得到的小区干扰或小区配置待分析列表的小区数量："
					+ (gisCells == null ? gisCells : gisCells.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (gisCells == null) {
			// 从数据库加载
			gisCells = this.rnoPlanDesignDao
					.getFreqReuseCellGisInfoFromSelectionList(selectConfig);
			try {
				memCached
						.set(key,
								RnoConstant.TimeConstant.TRAFFICSTSSELCONFIGURESTSCELLTIME,
								gisCells);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (gisCells != null) {
			result.setTotalCnt(gisCells.size());
		}
		if (page != null) {
			int start = page.calculateStart();
			if (start > gisCells.size()) {
				start = gisCells.size() - 1;
			}
			if (start < 0) {
				start = 0;
			}
			int toIndex = start + page.getPageSize();
			if (toIndex > gisCells.size()) {
				toIndex = gisCells.size();
			}
			gisCells = gisCells.subList(start, toIndex);
		}

		result.setGisCells(gisCells);
		log.info("退出getFreqReuseCellGisInfoFromSelectionList。 返回：" + gisCells);

		return result;
	}

	/**
	 * 获取区域下的干扰配置
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2013-11-7 下午6:42:49
	 */
	public List<RnoInterferenceDescriptor> getRnoInterferenceDescriptorInArea(
			long areaId) {
		return rnoPlanDesignDao.getRnoInterferenceDescriptorInArea(areaId);
	}

	/**
	 * 根据获取RnoInterferenceDescriptor
	 * 
	 * @param id
	 * @return
	 * @author brightming 2013-11-7 下午8:01:44
	 */
	public RnoInterferenceDescriptor getRnoInterferenceDescriptorById(long id) {
		return rnoPlanDesignDao.getRnoInterferenceDescriptorById(id);
	}

	/**
	 * 通过相同bcchbsic的组合cobsic下有两个或多个label,从而保存CobsicCells对象集合数据
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-11下午04:57:56
	 */
	public List<CobsicCells> saveCobsicCellsByCoBsicKeys(boolean reSelected,String areaIds,String configIds,int bcch,String bsic) {
		log
		.info("进入saveCobsicCellsByCoBsicKeys　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
				+ reSelected
				+ ":"
				+ areaIds
				+ ":"
				+ configIds
				+ ":"
				+ bcch + ":" + bsic);
		List<CobsicCells> coblists = new ArrayList<CobsicCells>();
		List<Map<String, Object>> list = rnoPlanDesignDao
				.getCoBsicCellsByAreaId(reSelected,areaIds,configIds,bcch,bsic);
		log.info("CoBsicCellslist.size():"+list.size());
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			String listbcch = map.get("BCCH").toString();
			String listbsic = map.get("BSIC").toString();
			String label = map.get("LABEL").toString();
			List a = Arrays.asList(label);
			List arrayList = new ArrayList(a);
			CobsicCells onecobsicCell = new CobsicCells();
			onecobsicCell.setBcch(Long.parseLong(listbcch));
			onecobsicCell.setBsic(listbsic);
			onecobsicCell.setCells(arrayList);
			log.info("coblists.size():"+coblists.size());
			for (int j = 0; j < coblists.size(); j++) {
				boolean bcchbool = listbcch.equals(Long.toString(coblists.get(j)
						.getBcch()));
				boolean bsicbool = listbsic.equals(coblists.get(j)
						.getBsic());
				if (bcchbool && bsicbool) {
					coblists.get(j).getCells().add(label);
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
			if (!flag) {
				coblists.add(onecobsicCell);
			}
		}
		log.info("退出saveCobsicCellsByCoBsicKeys　coblists："+coblists);
		return coblists;
	}
	/**
	 * 全网范围内：通过相同bcchbsic的组合cobsic下有两个或多个label,从而保存CobsicCells对象集合数据
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-11下午04:57:56
	 */
	public List<CobsicCells> saveCobsicCellsByCoBsicKeys(boolean reSelected,String areaIds,String configIds) {
		log
		.info("进入saveCobsicCellsByCoBsicKeys　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
				+ reSelected
				+ ":"
				+ areaIds
				+ ":"
				+ configIds);
		List<CobsicCells> coblists = new ArrayList<CobsicCells>();
		List<Map<String, Object>> list = rnoPlanDesignDao
				.getCoBsicCellsByAreaId(reSelected,areaIds,configIds);
		log.info("CoBsicCellslist.size():"+list.size());
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			String listbcch = map.get("BCCH").toString();
			String listbsic = map.get("BSIC").toString();
			String label = map.get("LABEL").toString();
			List a = Arrays.asList(label);
			List arrayList = new ArrayList(a);
			CobsicCells onecobsicCell = new CobsicCells();
			onecobsicCell.setBcch(Long.parseLong(listbcch));
			onecobsicCell.setBsic(listbsic);
			onecobsicCell.setCells(arrayList);
			log.info("coblists.size():"+coblists.size());
			for (int j = 0; j < coblists.size(); j++) {
				boolean bcchbool = listbcch.equals(Long.toString(coblists.get(j)
						.getBcch()));
				boolean bsicbool = listbsic.equals(coblists.get(j)
						.getBsic());
				if (bcchbool && bsicbool) {
					coblists.get(j).getCells().add(label);
					flag = true;
				} else {
					flag = false;
				}
			}
			if (!flag) {
				coblists.add(onecobsicCell);
			}
		}
		log.info("退出saveCobsicCellsByCoBsicKeys　coblists："+coblists);
		return coblists;
	}
	/**
	 * 获取指定id的RnoNcsDescriptor对象
	 * 
	 * @param configIds
	 * @param areaids
	 *            用户允许查看的area id
	 * @return
	 * @author brightming 2013-11-14 下午7:17:06
	 */
	public List<RnoNcsDescriptor> getRnoNcsDescriptorByIds(
			List<Long> configIds, List<Long> areaIds) {
		return rnoPlanDesignDao.getRnoNcsDescriptorByIdsWithInAreas(configIds,
				areaIds);
	}

	/**
	 * 分页查询ncs描述信息
	 * 
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-15 上午11:08:48
	 */
	public List<RnoNcsDescriptorWrap> queryNcsDescriptorByPage(Page page,
			HashMap<String, Object> attachParams) {

		return rnoPlanDesignDao.queryNcsDescriptorByPage(page, attachParams);
	}

	/**
	 * 获取满足条件的ncs desc的数量
	 * 
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-15 下午12:03:13
	 */
	public int getNcsDescriptorTotalCnt(HashMap<String, Object> attachParams) {
		return rnoPlanDesignDao.getNcsDescriptorCount(attachParams);
	}

	/**
	 * 获取指定id的RnoCellStructDesc对象
	 * 
	 * @param configIds
	 * @param areaIds
	 * @return
	 * @author brightming 2013-11-16 下午12:07:05
	 */
	public List<RnoCellStructDesc> getRnoCellStructDescriptorByIds(
			List<Long> configIds, List<Long> areaIds) {
		return rnoPlanDesignDao.getRnoCellStructDescByIdsWithInAreas(configIds,
				areaIds);
	}

	/**
	 * 查询符合条件的小区结构指标数据的数量
	 * 
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-16 下午12:13:55
	 */
	public int getCellStructDescTotalCnt(HashMap<String, Object> attachParams) {
		return rnoPlanDesignDao.getCellStructDescriptorCount(attachParams);
	}

	/**
	 * 分页获取符合条件的小区结构指标数据
	 * 
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-16 下午12:14:15
	 */
	public List<RnoCellStructDescWrap> queryCellStructDescByPage(Page page,
			HashMap<String, Object> attachParams) {
		return rnoPlanDesignDao.queryCellStructDescriptorByPage(page,
				attachParams);
	}

	/**
	 * 获取指定小区的冗余邻区
	 * 
	 * @param cell
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming 2013-11-18 下午4:49:13
	 */
/*	public List<RedundantNCell> getRedundantNcellOfCell(String cell,
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition) {
		log.info("进入service方法：getRedundantNcell。cell=" + cell + ",cellConfig="
				+ cellConfig + ",handoverConfig=" + handoverConfig
				+ ",cellStructConfig=" + cellStructConfig + ",ncsConfig="
				+ ncsConfig + ",condition=" + condition);
		if(cell==null || cellConfig==null || handoverConfig==null || cellStructConfig==null|| ncsConfig==null || condition==null){
			log.error("service方法：getRedundantNcell传入的参数无效！必须都不为null！");
			return Collections.EMPTY_LIST;
			
		}
		return rnoPlanDesignDao.getRedundantNcell(cell, ncsConfig.getConfigId(), handoverConfig.getConfigId(), cellStructConfig.getConfigId(), condition);
	}*/
	
	/**
	 * 获取指定小区的漏定邻区
	 * @param cell
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-18 下午4:55:16
	 */
/*	public List<RedundantNCell> getOmitNcellOfCell(String cell,
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition){
		log.info("进入service方法：getOmitNcellOfCell。cell=" + cell + ",cellConfig="
				+ cellConfig + ",handoverConfig=" + handoverConfig
				+ ",cellStructConfig=" + cellStructConfig + ",ncsConfig="
				+ ncsConfig + ",condition=" + condition);
		if(cell==null || cellConfig==null || handoverConfig==null || cellStructConfig==null|| ncsConfig==null || condition==null){
			log.error("service方法：getOmitNcellOfCell传入的参数无效！必须都不为null！");
			return Collections.EMPTY_LIST;
		}
		return rnoPlanDesignDao.getOmitNcell(cell, ncsConfig.getConfigId(), cellStructConfig.getConfigId(), condition);
	}*/
	
	/**
	 * 通过区域和日期返回切换描述数据的多少
	 * @param areaId
	 * @param date
	 * @return
	 * @author chao.xj
	 * @date 2013-11-14上午11:07:45
	 */
	public List<Map<String,Object>> queryRnoHandoverDescByAreaAndDate(Page page,long areaId,String date[],boolean sqlForCount)
	{
		return rnoPlanDesignDao.queryRnoHandoverDescByAreaAndDate(page, areaId, date, sqlForCount);
		
	}
	/**
	 * 通过日期等条件语句查找返回不同总数量记录
	 * @param date
	 * @param sqlForCount
	 * @return
	 * @author chao.xj
	 * @date 2013-11-16下午09:58:18
	 */
	public int getTotalHODescCount(String date[],boolean sqlForCount,long areaId)
	{
		return rnoPlanDesignDao.getTotalHODescCount(date, sqlForCount, areaId);
	}
	/**
	 * 根据HoDescId获取切换统计描述记录
	 * @param HoDescId
	 * @return
	 */
	public List<RnoHandoverDescriptor> getRnoHandoverDescriptorByHoDescId(final String HoDescId[]){
		
		return rnoPlanDesignDao.getRnoHandoverDescriptorByHoDescId(HoDescId);
	}
	/**
	 * 通过区域ID获取一个area对象
	 * @param areaid
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午02:41:51
	 */
	public SysArea getAreaobjByareaId(long areaid){
		
		return rnoPlanDesignDao.getAreaobjByareaId(areaid);
	}
	/**
	 * 通过源区和目标小区确认是否是邻区关系
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:47:15
	 */
	public boolean queryNcellByoCell(String sourcecell,String targetcell) 
	{
		return rnoPlanDesignDao.queryNcellByoCell(sourcecell, targetcell);
	}
	/**
	 * 通过传入小区label得到一个小区对象
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:59:20
	 */
	public Cell queryCellobjByLabel(String label){
		
		return rnoPlanDesignDao.queryCellobjByLabel(label);
	}
	/**
	 * 获取小区间的距离
	 * @param lnglats
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午10:56:40
	 */
	public double getDistanceBetweenTheCells(String sourcecell, String targetcell){
		List<String> lnglats=rnoPlanDesignDao.getLnglatsBySourceCellAndTargetCell(sourcecell, targetcell);
		String lnglat[]=lnglats.get(0).split(",");
		return LatLngHelper.Distance(Double.parseDouble(lnglat[0]), Double.parseDouble(lnglat[1]), Double.parseDouble(lnglat[2]), Double.parseDouble(lnglat[3]));	
	}
	/**
	 * 通过两个小区查询其共有的邻区
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午11:31:22
	 */
	public List<RnoNcell> queryCommonNcellByTwoCell(final String sourcecell,final String targetcell){
		
		return rnoPlanDesignDao.queryCommonNcellByTwoCell(sourcecell, targetcell);
	}
	/**
	 * 获取全网冗余邻区
	 * @param cellConfig
	 * @param handoverConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-20 上午11:24:13
	 */
/*	public List<RedundantNCell> getAllRedundantCellsByCondition(
			PlanConfig cellConfig, PlanConfig handoverConfig,
			PlanConfig cellStructConfig, PlanConfig ncsConfig,
			NcellAnalysisCondition condition){
		log.info("进入方法：getAllRedundantCellsByCondition.cellConfig="+cellConfig+",handoverConfig="+handoverConfig+",cellStructConfig="+cellStructConfig+",ncsConfig="+ncsConfig+",condition="+condition);
		return rnoPlanDesignDao.getAllRedundantCellsByCondition(cellConfig, ncsConfig.getConfigId(), handoverConfig.getConfigId(), cellStructConfig.getConfigId(), condition);
	}*/
	
	/**
	 * 获取全网漏定邻区
	 * @param cellConfig
	 * @param cellStructConfig
	 * @param ncsConfig
	 * @param condition
	 * @return
	 * @author brightming
	 * 2013-11-20 上午11:24:31
	 */
/*	public List<RedundantNCell> getAllOmitCellsByCondition(
			PlanConfig cellConfig, PlanConfig cellStructConfig,
			PlanConfig ncsConfig, NcellAnalysisCondition condition){
		log.info("进入方法：getAllOmitCellsByCondition.cellConfig="+cellConfig+",cellStructConfig="+cellStructConfig+",ncsConfig="+ncsConfig+",condition="+condition);
		
		return rnoPlanDesignDao.getAllOmitCellsByCondition(cellConfig, ncsConfig.getConfigId(), cellStructConfig.getConfigId(), condition);
	}*/
	/**
	 * 获取指定区域和模糊查询条件的小区配置描述的总量
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午02:06:15
	 */
	public int getTotalCellDescCountByArea(final long areaId,final String schemeName,final boolean sysDefault){
		
		return rnoPlanDesignDao.getTotalCellDescCountByArea(areaId, schemeName, sysDefault);
	}
	/**
	 * 通过区域和方案名分页查询小区配置描述数据
	 * @param page
	 * @param areaId
	 * @param schemeName
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午02:30:01
	 */
	public List<Map<String, Object>> queryCellConfigureDescByAreaAndScheme(
			Page page, final String areaIds,final String schemeName,final boolean sysDefault){
		
		return rnoPlanDesignDao.queryCellConfigureDescByAreaAndScheme(page, areaIds, schemeName,sysDefault);
	}
	/**
	 * 通过小区配置ID的字符串获取小区配置描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellConfigureDescByConfigIds(final String configIds){
		
		return rnoPlanDesignDao.getCellConfigureDescByConfigIds(configIds);
	}
	/**
	 * 改造：获取符合条件的干扰数据的数量
	 * @param areaId
	 * @param attachParams
	 * @param isDefault
	 * @param schemeName
	 * @param forcount
	 * @return
	 * @author chao.xj
	 * @date 2013-12-13下午03:54:34
	 */
	public int getTotalInterferenceCount(final long areaId,final HashMap<String, Object> attachParams,final boolean isDefault,
			final String schemeName,final boolean forcount)
	{
		return rnoPlanDesignDao.getTotalInterferenceCount(areaId, attachParams, isDefault, schemeName, forcount);
	}
	/**
	 * 改造：查询干扰数据通过分页方式
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午07:03:17
	 */
	public List<Map<String,Object>> queryInterferenceDataByPage(final Page page,final long areaId,final HashMap<String, Object> attachParams,final boolean isDefault,
			final String schemeName,final boolean forcount)
			{
			return rnoPlanDesignDao.queryInterferenceDataByPage(page, areaId, attachParams, isDefault, schemeName, forcount);
		
			}
	/**
	 * 改造：通过干扰配置ID的字符串获取干扰指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getInterferenceDataDescByConfigIds(final String interDescIds){
		
		return rnoPlanDesignDao.getInterferenceDataDescByConfigIds(interDescIds);
	}
	/**
	 * 通过城市id获取其下相应的区域集合
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-23上午11:46:11
	 */
	public List<Map<String, Object>> getSpecialLevalAreaByCityId(final long cityId)
	{
		
		return rnoPlanDesignDao.getSpecialLevalAreaByCityId(cityId);
	}
	/**
	 * 通过小区名获取NCS干扰指标数据的集合
	 * @title 
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24上午10:46:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	@Override
	public Map<String, FreqInterIndex> getSpecifyCellInterferenceAnalysis(
			String label,long selectId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> cellinterList=rnoPlanDesignDao.getSpecifyCellInterferenceAnalysis(label,selectId);
		List celltchList=new ArrayList();
		DecimalFormat df=new DecimalFormat("#.0000");
//		List<FreqInterIndex> freqinterindexList=new ArrayList<FreqInterIndex>();
		Map<String, FreqInterIndex>  freqinterMap= new HashMap<String, FreqInterIndex>();
		for (int i = 0; i < cellinterList.size(); i++) {
			double samefreqcoeffi=Double.parseDouble(cellinterList.get(i).get("SAME_FREQ_INTER_COEFFI").toString());
			double adjfreqcoeffi=Double.parseDouble(cellinterList.get(i).get("ADJ_FREQ_INTER_COEFFI").toString());
			int cellfreqcnt=Integer.parseInt(cellinterList.get(i).get("CELLFREQCNT").toString());
			int ncellfreqcnt=Integer.parseInt(cellinterList.get(i).get("NCELLFREQCNT").toString());
			String cell=cellinterList.get(i).get("CELL").toString();
			String ncell=cellinterList.get(i).get("NCELL").toString();
			double distance=Double.parseDouble(cellinterList.get(i).get("DISTANCE").toString());
			List ncelltchList=new ArrayList();
			//cell字符串转换集合
			if(i==0){
				String celltchstr=cellinterList.get(i).get("CELLTCH").toString();
				StringTokenizer cellst=new StringTokenizer(celltchstr,",");
				while (cellst.hasMoreTokens()) {
					celltchList.add(cellst.nextToken());
				}
			}
			//ncell字符串转换集合
			String ncelltchstr=cellinterList.get(i).get("NCELLTCH").toString();
			StringTokenizer ncellst=new StringTokenizer(ncelltchstr,",");
			while (ncellst.hasMoreTokens()) {
				ncelltchList.add(ncellst.nextToken());
			}
			for (int j = 0; j < celltchList.size(); j++) {
//				System.out.println("cellinterList ooo:"+celltchList.get(j));
				int cellfreq =Integer.parseInt(celltchList.get(j).toString());
				//同频数
				int samefreq=0;
				//邻频数
				int adjfreq=0;
//				System.out.println("进入ncelltchList.size()循环：－－－－－－－－－－－－－－－－－－－－－－－－开始");
				for (int k = 0; k < ncelltchList.size(); k++) {
//					System.out.println("ncelltchList:"+ncelltchList.get(k));
					int ncellfreq =Integer.parseInt(ncelltchList.get(k).toString());
					//同频
					if (cellfreq==ncellfreq) {
//						System.out.println("同频："+cellinterList.get(i).get("CELL")+":"+cellinterList.get(i).get("NCELL"));
						samefreq++;
//						System.out.println("samefreq++:"+samefreq);
					}
					//邻频
					if (cellfreq==(ncellfreq+1) || cellfreq==(ncellfreq-1)) {
//						System.out.println("邻频："+cellinterList.get(i).get("CELL")+":"+cellinterList.get(i).get("NCELL"));
						adjfreq++;
//						System.out.println("adjfreq++:"+adjfreq);
					}
					if (k!=(ncelltchList.size()-1)) {
//						System.out.println("进入continue");
						continue;
					}else {
//						System.out.println("进入ncelltchList.size()循环：－－－－－－－－－－－－－－－－－－－－－－－－结束");
						//同频数
						int samefreqagain=0;
						//邻频数
						int adjfreqagain=0;
						for (int m = 0; m < ncelltchList.size(); m++) {
//							System.out.println("ncelltchList:"+ncelltchList.get(k));
							int ncellfreqagain =Integer.parseInt(ncelltchList.get(m).toString());
							//同频
							if (cellfreq==ncellfreqagain) {
//								System.out.println("同频："+cellinterList.get(i).get("CELL")+":"+cellinterList.get(i).get("NCELL"));
								samefreqagain++;
								
								//in
								//同频入干扰因子
								double samefreqintofactor=samefreqcoeffi*samefreq/ncellfreqcnt*100;
								//out
								//同频出干扰因子
								double samefreqoutfactor=samefreqcoeffi*samefreq/cellfreqcnt*100;
								//in
								//邻频入干扰因子
								double adjfreqintofactor=adjfreqcoeffi*adjfreq/ncellfreqcnt*100;
								//out
								//邻频出干扰因子
								double adjfreqoutfactor=adjfreqcoeffi*adjfreq/cellfreqcnt*100;
								
								//interfer into
								double interinto=new Double( df.format( samefreqintofactor+adjfreqintofactor ) );
								//interfer out
								double interout=new Double( df.format( samefreqoutfactor+adjfreqoutfactor ) );
//								BigDecimal bd = new BigDecimal(adjfreqoutfactor);  
//								System.out.println(ncell+"--ncellfreq:"+ncellfreqagain+"--adjfreq:"+adjfreq+"--samefreq:"+samefreq+"samefreqoutfactor:"+samefreqoutfactor+"adjfreqoutfactor:"+bd.toPlainString());
								//total inter
								double totalinter=new Double( df.format( interinto+interout ) );
								//cell+","+cellfreq-->cellfreq
								if (freqinterMap.containsKey(cellfreq+"")) {
									FreqInterIndex freqInterIndex=(FreqInterIndex)freqinterMap.get(cellfreq+"");
									List<AdjFreqInterDetailed>  adjfreqinterList=freqInterIndex.getAdjFreqInterDetaList();
									AdjFreqInterDetailed adjfreqinterdetailobj=new AdjFreqInterDetailed();
									adjfreqinterdetailobj.setNcell(ncell);
									adjfreqinterdetailobj.setNcellTch(ncellfreqagain);
									adjfreqinterdetailobj.setCellInteredInto(interinto);
									adjfreqinterdetailobj.setCellInterOut(interout);
									adjfreqinterdetailobj.setTotalInter(totalinter);
									adjfreqinterdetailobj.setDistance(distance);
									adjfreqinterList.add(adjfreqinterdetailobj);
									freqInterIndex.setAdjFreqInterDetaList(adjfreqinterList);
//									freqinterMap.put(cell+","+cellfreq, freqInterIndex);
									freqinterMap.put(cellfreq+"", freqInterIndex);
								}else {
									FreqInterIndex freqInterIndex=new FreqInterIndex();
									freqInterIndex.setLabel(cell);
									freqInterIndex.setCellTch(String.valueOf(cellfreq));
									AdjFreqInterDetailed adjFreqInterDetailed=new AdjFreqInterDetailed();
									adjFreqInterDetailed.setNcell(ncell);
									adjFreqInterDetailed.setNcellTch(ncellfreqagain);
									adjFreqInterDetailed.setCellInteredInto(interinto);
									adjFreqInterDetailed.setCellInterOut(interout);
									adjFreqInterDetailed.setTotalInter(totalinter);
									adjFreqInterDetailed.setDistance(distance);
									List<AdjFreqInterDetailed> adjfreqinterList=new ArrayList<AdjFreqInterDetailed>();
									adjfreqinterList.add(adjFreqInterDetailed);
									freqInterIndex.setAdjFreqInterDetaList(adjfreqinterList);
//									freqinterMap.put(cell+","+cellfreq, freqInterIndex);
									freqinterMap.put(cellfreq+"", freqInterIndex);
								}
							}
							//邻频
							if (cellfreq==(ncellfreqagain+1) || cellfreq==(ncellfreqagain-1)) {
//								System.out.println("邻频："+cellinterList.get(i).get("CELL")+":"+cellinterList.get(i).get("NCELL"));
								adjfreqagain++;
								//in
								//同频入干扰因子
								double samefreqintofactor=samefreqcoeffi*samefreq/ncellfreqcnt*100;
								//out
								//同频出干扰因子
								double samefreqoutfactor=samefreqcoeffi*samefreq/cellfreqcnt*100;
								//in
								//邻频入干扰因子
								double adjfreqintofactor=adjfreqcoeffi*adjfreq/ncellfreqcnt*100;
								//out
								//邻频出干扰因子
								double adjfreqoutfactor=adjfreqcoeffi*adjfreq/cellfreqcnt*100;
								
								//interfer into
								double interinto=new Double( df.format( samefreqintofactor+adjfreqintofactor ) );
								//interfer out
								double interout=new Double( df.format( samefreqoutfactor+adjfreqoutfactor ) );
//								BigDecimal bd = new BigDecimal(adjfreqoutfactor);  
//								System.out.println(ncell+"--ncellfreq:"+ncellfreqagain+"--adjfreq:"+adjfreq+"--samefreq:"+samefreq+"samefreqoutfactor:"+samefreqoutfactor+"adjfreqoutfactor:"+bd.toPlainString());
								//total inter
								double totalinter=new Double( df.format( interinto+interout ) );
								//cell+","+cellfreq-->cellfreq
								if (freqinterMap.containsKey(cellfreq+"")) {
									FreqInterIndex freqInterIndex=(FreqInterIndex)freqinterMap.get(cellfreq+"");
									List<AdjFreqInterDetailed>  adjfreqinterList=freqInterIndex.getAdjFreqInterDetaList();
									AdjFreqInterDetailed adjfreqinterdetailobj=new AdjFreqInterDetailed();
									adjfreqinterdetailobj.setNcell(ncell);
									adjfreqinterdetailobj.setNcellTch(ncellfreqagain);
									adjfreqinterdetailobj.setCellInteredInto(interinto);
									adjfreqinterdetailobj.setCellInterOut(interout);
									adjfreqinterdetailobj.setTotalInter(totalinter);
									adjfreqinterdetailobj.setDistance(distance);
									adjfreqinterList.add(adjfreqinterdetailobj);
									freqInterIndex.setAdjFreqInterDetaList(adjfreqinterList);
//									freqinterMap.put(cell+","+cellfreq, freqInterIndex);
									freqinterMap.put(cellfreq+"", freqInterIndex);
								}else {
									FreqInterIndex freqInterIndex=new FreqInterIndex();
									freqInterIndex.setLabel(cell);
									freqInterIndex.setCellTch(String.valueOf(cellfreq));
									AdjFreqInterDetailed adjFreqInterDetailed=new AdjFreqInterDetailed();
									adjFreqInterDetailed.setNcell(ncell);
									adjFreqInterDetailed.setNcellTch(ncellfreqagain);
									adjFreqInterDetailed.setCellInteredInto(interinto);
									adjFreqInterDetailed.setCellInterOut(interout);
									adjFreqInterDetailed.setTotalInter(totalinter);
									adjFreqInterDetailed.setDistance(distance);
									List<AdjFreqInterDetailed> adjfreqinterList=new ArrayList<AdjFreqInterDetailed>();
									adjfreqinterList.add(adjFreqInterDetailed);
									freqInterIndex.setAdjFreqInterDetaList(adjfreqinterList);
//									freqinterMap.put(cell+","+cellfreq, freqInterIndex);
									freqinterMap.put(cellfreq+"", freqInterIndex);
								}
							}
							/*if (samefreqagain!=0 || adjfreqagain!=0) {
								System.out.println("samefreqagain"+samefreqagain+"--adjfreqagain:"+adjfreqagain);
							}*/
						}
					}
				}
				
					
			}
		}
		return freqinterMap;
	}
	/**
	 * 分页查询总分析ncs cell信息通过NCS描述ID
	 * @title 
	 * @param ncsDescId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午06:15:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTotalAnalysisGisCellByNcsDescIdForPage(long ncsDescId,
			Page page){
		log.info("进入getTotalAnalysisGisCellByNcsDescIdForPage(long ncsDescId," + ncsDescId + " Page page)"
				+ page);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		if (page.getPageSize() <= 0) {
			page.setPageSize(25);
		}
		if (page.getCurrentPage() <= 0) {
			page.setCurrentPage(1);
		}
		final int totalCnt = page.getTotalCnt();
		if (totalCnt <= 0) {
			long count = rnoPlanDesignDao.getNcsInfoCount(ncsDescId);
			log.info("NcsCellCount:" + count);
			page.setTotalCnt((int) count);
		}
		// final int startIndex = (page.getCurrentPage() - 1) *
		// page.getPageSize()+ 1;
		final int startIndex = page.calculateStart();
		// final int endIndex = startIndex + page.getPageSize();
		final int cnt = page.getPageSize();// 页面大小
		List<Map<String, Object>> ncscelldataList = rnoPlanDesignDao.getTotalAnalysisGisCellByNcsDescId(ncsDescId, startIndex, cnt);
		log.info("退出getTotalAnalysisGisCellByNcsDescIdForPage:" + ncscelldataList);
		return ncscelldataList;
	}
	/**
	 * 通过ncsid获取ncs描述表的区域信息[city,area]
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-29下午01:40:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getNcsAreaInfoByNcsId(final long ncsDescId){
		return rnoPlanDesignDao.getNcsAreaInfoByNcsId(ncsDescId);
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param cityId
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>>getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入service方法：getRedundantNcell。cell=" + cell +  ",ncsDescIds="
				+ ncsDescIds+ ",handoverDescIds=" + handoverDescIds
				 + ",condition=" + condition + ",areaId="+areaId);

		return rnoPlanDesignDao.getRedundantAndOmitNcells(cell, ncsDescIds, handoverDescIds, condition, areaId);
		
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午3:09:03
	 * @param cell
	 * @param ncsDescIds
	 * @param cellResult
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @return result
	 * @description 根据参数查询小区的冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>>getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入service方法：getRedundantNcell。cell=" + cell +  ",ncsDescIds="
				+ ncsDescIds+ ",handoverDescIds=" + handoverDescIds
				 + ",condition=" + condition + ",areaId="+areaId);
		
		return rnoPlanDesignDao.getRedundantAndOmitNcells(cell, ncsDescIds, cellResult, handoverDescIds, condition, areaId);
		
	}

	/**
	 * 
	 * @title 
	 * @param taskId
	 * @param dir
	 * @return 获取干扰矩阵文件索引数据目录
	 * @author chao.xj
	 * @date 2014-6-24下午06:35:44
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String getInterferMatrixDir(long taskId,String dir) {
		String dirString="";
//		String taskidString="";
//		String completetimeString="";
//		String yearmonthString="";
		
//		List<Map<String, Object>> taskidandcompltime=rnoPlanDesignDao.getTaskIdAndCompleteTimeByAreaId(areaId);
//		if (taskidandcompltime!=null&&taskidandcompltime.size()!=0) {
//			taskidString=taskidandcompltime.get(0).get("TASK_ID").toString();
//			completetimeString=taskidandcompltime.get(0).get("COMPLETE_TIME").toString();
//			yearmonthString=taskidandcompltime.get(0).get("YEARMONTH").toString();
//			String file = "/op/rno/ana_result/" + yearmonthString+"/ncs_res_"+taskidString;// 文件下载路径
//			dirString = ServletActionContext.getServletContext().getRealPath(
//					file);
//		}
		if(taskId == -1) {
			return dirString;
		}
		RnoTask task = rnoTaskDao.getTaskById(taskId);
		java.sql.Timestamp createTime = task.getStartTime();
		Calendar calendar = new GregorianCalendar();
		Date dt=new Date(createTime.getTime());
		calendar.setTime(dt);
		String file = "/op/rno/ana_result/" + calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH) + 1) + "/ncs_res_" + taskId;
		dirString = ServletActionContext.getServletContext().getRealPath(
				file);

		return dirString;
	}
	
	/**
	 * 获取干扰矩阵文件的目录
	 * @param martixDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午05:28:05
	 */
	public String getInterferMatrixDirById(long martixDescId) {
		String dir = "";
		if(martixDescId == -1) {
			return dir;
		}
		List<Map<String, Object>> res = rnoPlanDesignDao.getMartixDirById(martixDescId);
		if(res != null && res.size() > 0) {
			dir = res.get(0).get("FILE_PATH").toString();
		}
		dir = dir + "/ncs_res_" + martixDescId;
		//dir = ServletActionContext.getServletContext().getRealPath(dir);
		return dir;
	}
	/**
	 * 196 2014-06-24 12:12:09　2014\6
	 * @title 获取taskid及成功完成时间信息通过区域id
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午06:14:42
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTaskIdAndCompleteTimeByAreaId(final long areaId){
		return rnoPlanDesignDao.getTaskIdAndCompleteTimeByAreaId(areaId);
	}
	/**
	 * 获取区域最新的NCS干扰矩阵
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午04:52:44
	 */
	public List<Map<String, Object>> getLatelyInterferMartixRecByAreaId(
			long areaId) {
		return rnoPlanDesignDao.getLatelyInterferMartixRecByAreaId(areaId);
	}
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午5:01:37
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param cityId
	 * @return result
	 * @description 根据条件全网分析冗余邻区和漏定邻区
	 */
/*	public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, long cityId)
	{
		log.info("进入service方法：getAllRedundantAndOmitCellsByCondition ncsDescIds="+ncsDescIds+"handoverDescIds="+handoverDescIds+",condition="+condition+",areaId="+areaId+",cityId="+cityId);
		return rnoPlanDesignDao.getAllRedundantAndOmitCellsByCondition(ncsDescIds, handoverDescIds, condition, areaId, cityId);
	}*/
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午5:01:37
	 * @param ncsDescIds
	 * @param cellResult
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @return result
	 * @description 根据条件全网分析冗余邻区和漏定邻区
	 */
/*	public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds,List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入service方法：getAllRedundantAndOmitCellsByCondition ncsDescIds="+ncsDescIds+"handoverDescIds="+handoverDescIds+",condition="+condition+",areaId="+areaId);
		return rnoPlanDesignDao.getAllRedundantAndOmitCellsByCondition(ncsDescIds, cellResult, handoverDescIds, condition, areaId);
		
	}*/

	/**
	 * 获取小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public List<Map<String, Object>> getCellFreqByCellName(String cell) {
		return rnoPlanDesignDao.getCellFreqByCellName(cell);
	}

	/**
	 * 更新小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public boolean updateCellFreqThroughCellName(String cell, String bcch, String tch) {
		return rnoPlanDesignDao.updateCellFreqThroughCellName(cell, bcch, tch);
	}
	
	
	
}
