package com.iscreate.op.service.rno;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.GisAnalysisCellQueryResult;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.action.rno.model.GisInterferenceCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoCellDaoImpl;
import com.iscreate.op.dao.rno.RnoInterferenceAnalysisDao;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCell;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCellTopN;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoGisCellInterference;
import com.iscreate.op.pojo.rno.RnoInterferenceAnalysisGisCell;
/**
 * 总干扰分析Service
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class RnoInterferenceAnalysisServiceImpl implements RnoInterferenceAnalysisService {
	
	private static Log log = LogFactory.getLog(RnoInterferenceAnalysisService.class);
	private RnoInterferenceAnalysisDao rnoInterferenceAnalysisDao;
	public MemcachedClient memCached;
	
	/**
	 * 分页获取区/县的rnoAnalysisGisCells
	 * @author ou.jh
	 * @date Nov 6, 2013
	 * @Description: TODO
	 * 
	 * @param areaId
	 *            指定区域
	 * @param page
	 *            分页参数
	 * 
	 */
	public GisAnalysisCellQueryResult getRnoAnalysisGisCellByPage(long areaId,long cellConfigId,boolean isCellTempStorage, Page page,long interConfigId,boolean isInterTemp) {
		log.info("进入方法：getRnoAnalysisGisCellByPage 。areaid=" + areaId + ",page=" + page);
		//
		List<RnoAnalysisGisCell> rnoAnalysisGisCells = null;
		String cacheKey = RnoConstant.CacheConstant.CACHE_ANALYSIS_GISCELL_IN_AREA_
				+ cellConfigId;
		try {
			log.info("从缓存中获取->");
			rnoAnalysisGisCells = (List<RnoAnalysisGisCell>) memCached.get(cacheKey);
			log.info("rnoAnalysisGisCells:"+rnoAnalysisGisCells);
			log.info("缓存获取的结果数量为 ："
					+ ((rnoAnalysisGisCells == null) ? 0 : rnoAnalysisGisCells.size()));
			// System.out.println("缓存获取的结果为
			// ："+gisCells==null?0:gisCells.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rnoAnalysisGisCells == null || rnoAnalysisGisCells.size()==0) {
			log.info("从数据库获取->");
			rnoAnalysisGisCells = rnoInterferenceAnalysisDao.getRnoGisCellInArea(areaId, cellConfigId, isCellTempStorage,interConfigId,isInterTemp);
			log.info("从数据库中获取结果数量为：" + rnoAnalysisGisCells == null ? 0 : rnoAnalysisGisCells.size());
			// System.out.println("从数据库中获取结果为:"+gisCells==null?0:gisCells.size());
			//
			log.info("放入缓存->");
			try {
				memCached.set(cacheKey, RnoConstant.TimeConstant.ANALYSISCELLINAREATIME,
						rnoAnalysisGisCells);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Integer totalCnt = 0;
		List<RnoAnalysisGisCell> resultGisCells = new ArrayList<RnoAnalysisGisCell>();
		if (rnoAnalysisGisCells != null && rnoAnalysisGisCells.size() > 0) {
			totalCnt = rnoAnalysisGisCells.size();
			int start = page.getForcedStartIndex()==-1?(page.getCurrentPage() - 1) * page.getPageSize():page.getForcedStartIndex();
//			int end = page.getCurrentPage() * page.getPageSize();
			int end=start+page.getPageSize();
			// /范围[start,end)
			int size = rnoAnalysisGisCells.size();
			if (start < 0) {
				start = 0;
			}
			if (start > size) {
				start = size;
			}
			if (end < 1) {
				end = 1;
			}
			if (end > size) {
				end = size;
			}
			log.info("start==" + start + ",end=" + end);
			// 截取
			resultGisCells = rnoAnalysisGisCells.subList(start, end);

		}

		GisAnalysisCellQueryResult result = new GisAnalysisCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setRnoAnalysisGisCells(resultGisCells);

		return result;
	}
	
	/**
	 * top-N 最大干扰小区标注
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final long rank,final long areaId){
		return this.rnoInterferenceAnalysisDao.getRnoGisCellInAreaTopN(cellConfigId, isCellTempStorage,interConfigId,isInterTemp,rank , areaId);
	}
	
	/**
	 * 小区干扰分析
	* @author ou.jh
	* @date Nov 11, 2013 11:07:08 AM
	* @Description: TODO 
	* @param @param configId
	* @param @param areaId
	* @param @param cellLabel        
	* @throws
	 */
	public RnoInterferenceAnalysisGisCell getCellInterferenceAnalysis(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final long areaId,final String cellLabel){
		List<RnoInterferenceAnalysisGisCell> cellInterferenceAnalysis = this.rnoInterferenceAnalysisDao.getCellInterferenceAnalysis(cellConfigId,isCellTempStorage,interConfigId, isInterTemp,areaId, cellLabel);
		if(cellInterferenceAnalysis != null && cellInterferenceAnalysis.size() > 0){
			return cellInterferenceAnalysis.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据CELLLABEL获取干扰邻区
	* @author ou.jh
	* @date Nov 11, 2013 4:51:44 PM
	* @Description: TODO 
	* @param @param label        
	* @throws
	 */
	public List<RnoGisCell> getInterferenceCellByLabel(final long cellConfigId,final boolean isCellTempStorage,long interConfigId,boolean isInterTemp,final String label){
		List<RnoGisCell> interferenceCellByLabel = this.rnoInterferenceAnalysisDao.getInterferenceCellByLabel(cellConfigId, isCellTempStorage,interConfigId,isInterTemp,label);
		return interferenceCellByLabel;
	}
	
	/**
	 * 根据小区获取干扰频点
	* @author ou.jh
	* @date Nov 12, 2013 11:22:01 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getInterferenceTCH(long cellConfigId,boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String label){
		List<Map<String, Object>> list = this.rnoInterferenceAnalysisDao.getInterferenceTCH(cellConfigId,isCellTemp,interConfigId, isInterTempStorage, areaId, label);
		return list;
	}
	

	public RnoInterferenceAnalysisDao getRnoInterferenceAnalysisDao() {
		return rnoInterferenceAnalysisDao;
	}

	public void setRnoInterferenceAnalysisDao(
			RnoInterferenceAnalysisDao rnoInterferenceAnalysisDao) {
		this.rnoInterferenceAnalysisDao = rnoInterferenceAnalysisDao;
	}

	public MemcachedClient getMemCached() {
		return memCached;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}
}
