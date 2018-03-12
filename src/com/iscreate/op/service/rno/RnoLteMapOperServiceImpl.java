package com.iscreate.op.service.rno;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.RnoMapGroundSupportAction;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.CellPairs;
import com.iscreate.op.action.rno.model.ConfusionCellResult;
import com.iscreate.op.action.rno.model.LteCellPciAnalysisQueryResult;
import com.iscreate.op.action.rno.model.LteCellPciCollAndConfQueryResult;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoLteCellDao;
import com.iscreate.op.dao.rno.RnoLteMapOperDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.service.system.SysAreaService;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoLteMapOperServiceImpl implements RnoLteMapOperService {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoMapGroundSupportAction.class);
	
	// ---注入----//
	private RnoLteMapOperDao rnoLteMapOperDao;
	private SysAreaService sysAreaService;
	private SysAreaDao sysAreaDao;
	public MemcachedClient memCached;
	private RnoLteCellDao rnoLteCellDao;

	//------------set---------------//
	public void setRnoLteMapOperDao(RnoLteMapOperDao rnoLteMapOperDao) {
		this.rnoLteMapOperDao = rnoLteMapOperDao;
	}

	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}
	public RnoLteCellDao getRnoLteCellDao() {
		return rnoLteCellDao;
	}

	public void setRnoLteCellDao(RnoLteCellDao rnoLteCellDao) {
		this.rnoLteCellDao = rnoLteCellDao;
	}
	/**
	 * 获取用户可访问的指定级别的区域
	 */
	public List<Area> ltefindAreaInSpecLevelListByUserId(String accountId,
			String areaLevel) {
		log.info("进入 ltefindAreaInSpecLevelListByUserId 。 accountId=" + accountId);

		List<Map<String, Object>> user_area_list = sysAreaService
				.getAreaByAccount(accountId);

		log.info("user_area_list==" + user_area_list);
		if (user_area_list == null || user_area_list.size() == 0) {
			return null;
		}

		long[] areaIds = new long[user_area_list.size()];
		int i = 0;
		for (Map<String, Object> usea : user_area_list) {
			areaIds[i++] = Long.parseLong(usea.get("AREA_ID").toString());
		}

		// String area_level = "区/县";
		List<Map<String, Object>> user_area_in_spec_level_list = sysAreaDao
				.getSubAreasInSpecAreaLevel(areaIds, areaLevel);
		List<Area> result = new ArrayList<Area>();
		Area area;
		for (Map<String, Object> one : user_area_in_spec_level_list) {
			area = Area.fromMap(one);
			if (area != null) {
				if (areaLevel.equals(area.getArea_level())) {
					result.add(area);
				}
			}
		}
		return result;	
	}
	
	/**
	 * 分页获取区/县的ltecell
	 * 
	 * @param areaId 指定区域
	 * @param page 分页参数
	 * @author peng.jm
	 * 2014-5-15 16:50:37
	 */
	public LteCellQueryResult getLteCellByPage(long areaId, Page page) {
		log.info("进入方法：getLteCellByPage 。areaid=" + areaId + ",page=" + page);
		//
		List<RnoLteCell> lteCells = null;
		String cacheKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
				+ areaId;
		try {
			log.info("从缓存中获取->");
			lteCells = (List<RnoLteCell>) memCached.get(cacheKey);
			log.info("缓存获取的结果数量为 ："
					+ ((lteCells == null) ? 0 : lteCells.size()));
			// System.out.println("缓存获取的结果为
			// ："+gisCells==null?0:gisCells.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (lteCells == null) {
			log.info("从数据库获取->");
			lteCells = rnoLteMapOperDao.getRnoLteCellInArea(areaId);
			log.info("从数据库中获取结果数量为：" + lteCells == null ? 0 : lteCells.size());
			// System.out.println("从数据库中获取结果为:"+gisCells==null?0:gisCells.size());
			//
			log.info("放入缓存->");
			try {
				memCached.set(cacheKey, RnoConstant.TimeConstant.TokenTime,
						lteCells);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Integer totalCnt = 0;
		List<RnoLteCell> resultLteCells = new ArrayList<RnoLteCell>();
		if (lteCells != null && lteCells.size() > 0) {
			totalCnt = lteCells.size();
			int start = page.getForcedStartIndex()==-1?(page.getCurrentPage() - 1) * page.getPageSize():page.getForcedStartIndex();
//			int end = page.getCurrentPage() * page.getPageSize();
			int end=start+page.getPageSize();
			// /范围[start,end)
			int size = lteCells.size();
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
			resultLteCells = lteCells.subList(start, end);

		}

		LteCellQueryResult result = new LteCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setLteCells(resultLteCells);

		return result;
	}

	/**
	 * 通过Lte小区id获取详细信息
	 * @author chao.xj
	 * 2015-7-1 14:30:23
	 */
	@Override
	public RnoLteCell getLteCellDetail(long lcid) {
		RnoLteCell lte = rnoLteMapOperDao.getLteCellById(lcid);
		return lte;
	}
	/**
	 * 
	 * @title 获取Lte的PCI复用分析数据
	 * @param areaId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:01:57
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public LteCellPciAnalysisQueryResult getPciReuseAnalysisByPage(
			long areaId, Page page) {
		log.info("进入方法：getPciReuseAnalysisByPage 。areaId=" + areaId + ",page=" + page);
		
		LteCellPciAnalysisQueryResult result = new LteCellPciAnalysisQueryResult();
		
		if (page == null) {
			log.warn("方法：queryLteNcellByPage的参数：page 是空！无法查询!");
			return (LteCellPciAnalysisQueryResult) Collections.EMPTY_LIST;
		}
		
		//获取该区域的pci总数与查询List
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoLteMapOperDao.getPciCount(areaId);
			page.setTotalCnt((int) totalCnt);
		}
		//System.out.println(totalCnt);
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Long> pciList = rnoLteMapOperDao.getPciList(areaId, startIndex, cnt);
		//System.out.println(pciList);

		//获取该区域的小区
		List<RnoLteCell> lteCellResult = new ArrayList<RnoLteCell>();
		lteCellResult = rnoLteMapOperDao.getRnoLteCellInArea(areaId);
		
		//获取同pci的小区查询List
		List<List<RnoLteCell>> reuseCellList = new ArrayList<List<RnoLteCell>>();

		List<RnoLteCell> oneReuseCellList = null;
		for (int i = 0; i < pciList.size(); i++) {
			oneReuseCellList = new ArrayList<RnoLteCell>();
			//System.out.println(pciList.get(i));
			for (RnoLteCell rnoLteCell : lteCellResult) {
				//System.out.println(rnoLteCell.getPci());
				if(/*pciList.get(i) == rnoLteCell.getPci() ||*/ pciList.get(i).equals(rnoLteCell.getPci())) {
					//System.out.println(pciList.get(i) +"  " +rnoLteCell.getPci()+" "+rnoLteCell.getChineseName());
					oneReuseCellList.add(rnoLteCell);
				}
			}
			reuseCellList.add(oneReuseCellList);
		}
		//System.out.println(reuseCellList);
		
		//获取复用距离小于小区覆盖距离的查询List
		List<List<CellPairs>> cellPairsList = new ArrayList<List<CellPairs>>();
		
		
		//遍历pci，获取符合的小区对list
		for (int i = 0; i < reuseCellList.size(); i++) {

			List<RnoLteCell> oneList = reuseCellList.get(i);
			List<CellPairs> oneCellPairsList = new ArrayList<CellPairs>();
			
			//遍历单个pci的小区对list，获取符合的小区对
			for (int j = 0; j < oneList.size(); j++) {
				for (int k = j + 1; k < oneList.size(); k++) {
					
					//创建新的小区对
					RnoLteCell cellOne = new RnoLteCell();
					RnoLteCell cellTwo = new RnoLteCell();
					CellPairs oneCellPairs = new CellPairs();
					
					cellOne = oneList.get(j);
					cellTwo = oneList.get(k);
					//计算两个小区的复用距离
					double reuse = LatLngHelper.Distance(
							cellOne.getLng(), cellOne.getLat(),
							cellTwo.getLng(), cellTwo.getLat());
					//暂时设为TRUE
					if(/*cellOne.getCellRadius() > reuse || 
							cellTwo.getCellRadius() > reuse*/ true) {
						if(cellOne != null && !cellOne.equals("") &&
								cellTwo != null && !cellTwo.equals("")){
							oneCellPairs = new CellPairs();
							oneCellPairs.setCellOne(cellOne);
							oneCellPairs.setCellTwo(cellTwo);
						}
						oneCellPairsList.add(oneCellPairs);
						/*System.out.println(oneCellPairs.getCellOne().getBusinessCellId()+"--"+
								oneCellPairs.getCellTwo().getPci()+"--"+
								oneCellPairs.getCellTwo().getBusinessCellId()+"--"+
								oneCellPairs.getCellTwo().getPci());*/
					}
				}
			}
			cellPairsList.add(oneCellPairsList);
		}
		//将查询结果放入result
		result.setPage(page);
		result.setPciList(pciList);
		result.setReuseCellList(reuseCellList);
		result.setCellPairsList(cellPairsList);
		
		return result;
	}
	/**
	 * 
	 * @title 通过pci获取相同pci的小区
	 * @param reuseLcid
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:03:28
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getCellsWithSamePciDetails(
			long reuseLcid) {
		log.debug("进入方法：getOneCellWithSamePciDetails。reuseLcid=" + reuseLcid);
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		//通过id获取主小区
		List<Map<String, Object>> cellsInTheSameEnodeb = rnoLteCellDao.queryLteCellAndCositeCells(reuseLcid);
		Map<String,Object> mainCell = cellsInTheSameEnodeb.get(0);
		//System.out.println(mainCell.get("PCI")+" "+mainCell.get("AREA_ID"));
		
		long pci = ((BigDecimal) mainCell.get("PCI")).longValue();
		long areaId = ((BigDecimal) mainCell.get("AREA_ID")).longValue();
		
		//获取对比小区
		List<Map<String, Object>> otherCells = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> cellResult = rnoLteMapOperDao
			.getCellsByPci(areaId, pci);
	
		for (Map<String, Object> map : cellResult) {
			if(((BigDecimal) map.get("LTE_CELL_ID")).longValue() != reuseLcid) {
				otherCells.add(map);
			}
		}
		
		//将分析结果加入返回集
		result.add(mainCell);
		double mainLng = ((BigDecimal) mainCell.get("LONGITUDE")).longValue();
		double mainLat = ((BigDecimal) mainCell.get("LATITUDE")).longValue();
		for (int i = 0; i < otherCells.size(); i++) {
			Map<String,Object> one = otherCells.get(i);
			double lng = ((BigDecimal) one.get("LONGITUDE")).longValue();
			double lat = ((BigDecimal) one.get("LATITUDE")).longValue();
			//计算两个小区的复用距离
			double reuse = LatLngHelper.Distance(
					mainLng, mainLat,
					lng, lat);
			one.put("reuse", reuse);
			result.add(one);
		}
		//System.out.println(result.get(3).get("reuse"));
		return result;
	}
	/**
	 * 
	 * @title 获取PCI冲突与混淆的小区数据
	 * @param areaId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:05:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public LteCellPciCollAndConfQueryResult getCollAndConfCellWithSamePciByPage(
			long areaId, Page page) {
		log.info("进入方法：getCollAndConfCellWithSamePciByPage 。areaId=" + areaId + ",page=" + page);
		
		LteCellPciCollAndConfQueryResult result = new LteCellPciCollAndConfQueryResult();
		
		if (page == null) {
			log.warn("方法：queryLteNcellByPage的参数：page 是空！无法查询!");
			return (LteCellPciCollAndConfQueryResult) Collections.EMPTY_LIST;
		}
		
		
		/** 获取pci列表 **/
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoLteMapOperDao.getPciCount(areaId);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Long> pciList = rnoLteMapOperDao.getPciList(areaId, startIndex, cnt);
		//System.out.println(pciList);
		
		//小区冲突列
		List<List<RnoLteCell>> collisionList = new ArrayList<List<RnoLteCell>>(); 
		//小区混淆列
		List<List<ConfusionCellResult>> confusionList = new ArrayList<List<ConfusionCellResult>>();
		//小区冲突对list，小区混淆对list
		List<List<CellPairs>> collPairsList = new ArrayList<List<CellPairs>>();
		List<List<CellPairs>> confPairsList = new ArrayList<List<CellPairs>>();
		
		List<Map<String,Object>> oneReuseCellList = null;
		List<RnoLteCell> oneCollisionList = null;
		List<ConfusionCellResult> oneConfusionList = null;
		List<CellPairs> oneCollPairsList = null;
		List<CellPairs> oneConfPairsList = null;
		
		//判断是否满足冲突条件
		boolean flagNCell = false;  //是否邻区
		boolean flagReuse = false;	//是否距离复用
		
		//判断是否满足混淆条件
		boolean flagNCellInOneCell = false;  //是否同时是一个参考小区的邻区
		boolean flagNcellOneReuse = false;	//第一个邻区是否与参考小区距离复用
		boolean flagNcellTwoReuse = false;	//第二个邻区是否与参考小区距离复用
		
		for (int i = 0; i < pciList.size(); i++) {
			
			oneReuseCellList = new ArrayList<Map<String,Object>>();
			oneCollisionList = new ArrayList<RnoLteCell>();
			oneConfusionList = new ArrayList<ConfusionCellResult>();
			oneCollPairsList = new ArrayList<CellPairs>();
			oneConfPairsList = new ArrayList<CellPairs>();
			
			//获取同一个pci的小区
			long pci = pciList.get(i).longValue();
			oneReuseCellList = rnoLteMapOperDao.getCellsByPci(areaId, pci);
			
			/** pci冲突 **/
			//遍历
			for (int j = 0; j < oneReuseCellList.size(); j++) {
				for (int k = 0; k < oneReuseCellList.size(); k++) {
					if(j!=k) {
						//获取要比较的两个小区businessId
						String lteCellId1 =  oneReuseCellList.get(j).get("BUSINESS_CELL_ID").toString();
						String lteCellId2 =  oneReuseCellList.get(k).get("BUSINESS_CELL_ID").toString();
						
						//获取两个小区的坐标
						double cellOneLng = ((BigDecimal) oneReuseCellList.get(j).get("LONGITUDE")).longValue();
						double cellOneLat = ((BigDecimal) oneReuseCellList.get(j).get("LATITUDE")).longValue();
						double cellTwoLng = ((BigDecimal) oneReuseCellList.get(k).get("LONGITUDE")).longValue();
						double cellTwoLat = ((BigDecimal) oneReuseCellList.get(k).get("LATITUDE")).longValue();
						
						//获取两个小区的覆盖半径
						double cellOneRadius = ((BigDecimal) oneReuseCellList.get(j).get("CELL_RADIUS")).longValue();
						double cellTwoRadius = ((BigDecimal) oneReuseCellList.get(k).get("CELL_RADIUS")).longValue();
						
						/*if(lteCellId1 == "1909761" || lteCellId1.equals("1909761")) {
							System.out.println(lteCellId1 + "  "+ lteCellId2);
						}*/
						
						//判断是否是邻区关系
						flagNCell = rnoLteCellDao.getNCellById(lteCellId1, lteCellId2);
						
						//计算两个小区的复用距离
						double reuse = LatLngHelper.Distance(
								cellOneLng, cellOneLat,
								cellTwoLng, cellTwoLat);
							
						if(cellOneRadius > reuse || 
								cellTwoRadius > reuse) {
							flagReuse = true;
						}
						
						//满足两个条件，加入pci冲突
						if(flagNCell && flagReuse) {
							//System.out.println(oneReuseCellList.get(j));
							RnoLteCell cellOne = new RnoLteCell();
							RnoLteCell cellTwo = new RnoLteCell();
							//暂时注释
							/*cellOne = cellOne.MaptoObject(oneReuseCellList.get(j));
							cellTwo = cellTwo.MaptoObject(oneReuseCellList.get(k));*/
							//装进冲突列表
							oneCollisionList.add(cellOne);
							//装进冲突小区对列表
							CellPairs collPairs = new CellPairs();
							collPairs.setCellOne(cellOne);
							collPairs.setCellTwo(cellTwo);
							oneCollPairsList.add(collPairs);
							break;
						}
					}	
				}
			}
			
			/** pci混淆 **/
			//遍历
			for (int j = 0; j < oneReuseCellList.size(); j++) {
				for (int k = j + 1; k < oneReuseCellList.size(); k++) {
					
					//获取要比较的两个小区businessId
					String lteCellId1 =  oneReuseCellList.get(j).get("BUSINESS_CELL_ID").toString();
					String lteCellId2 =  oneReuseCellList.get(k).get("BUSINESS_CELL_ID").toString();
					
					//获取两个小区的坐标
					double cellOneLng = ((BigDecimal) oneReuseCellList.get(j).get("LONGITUDE")).longValue();
					double cellOneLat = ((BigDecimal) oneReuseCellList.get(j).get("LATITUDE")).longValue();
					double cellTwoLng = ((BigDecimal) oneReuseCellList.get(k).get("LONGITUDE")).longValue();
					double cellTwoLat = ((BigDecimal) oneReuseCellList.get(k).get("LATITUDE")).longValue();
					
					//获取两个小区的覆盖半径
					double cellOneRadius = ((BigDecimal) oneReuseCellList.get(j).get("CELL_RADIUS")).longValue();
					double cellTwoRadius = ((BigDecimal) oneReuseCellList.get(k).get("CELL_RADIUS")).longValue();
					
					/*if(lteCellId1 == "1909761" || lteCellId1.equals("1909761")) {
						System.out.println(lteCellId1 + "  "+ lteCellId2);
					}*/
					
					flagNCellInOneCell = rnoLteCellDao.getNCellInOneCellByNcellId(lteCellId1, lteCellId2);
					//通过两个小区id获取这两个小区的serving小区的数据
					if(flagNCellInOneCell) {
						List<Map<String,Object>> servingCell = rnoLteCellDao.getServingCellByNcellId(lteCellId1, lteCellId2);
						
						if(servingCell.size() > 0) {
							//获取serving小区的坐标
							double servingCellLng = ((BigDecimal) servingCell.get(0).get("LONGITUDE")).longValue();
							double servingCellLat = ((BigDecimal) servingCell.get(0).get("LATITUDE")).longValue();
							
							//获取serving小区的覆盖半径
							double servingCellRadius = ((BigDecimal) servingCell.get(0).get("CELL_RADIUS")).longValue();
					
							//计算两个小区与serving小区的复用距离
							double reuseOne = LatLngHelper.Distance(
									cellOneLng, cellOneLat,
									servingCellLng, servingCellLat);
							double reuseTwo = LatLngHelper.Distance(
									cellTwoLng, cellTwoLat,
									servingCellLng, servingCellLat);
							//判断是否两个小区都符合
							if(cellOneRadius > reuseOne || 
									servingCellRadius > reuseOne) {
								flagNcellOneReuse = true;
							}
							if(cellTwoRadius > reuseTwo || 
									servingCellRadius > reuseTwo) {
								flagNcellTwoReuse = true;
							}
							
							//满足三个条件，加入pci混淆
							if(flagNcellOneReuse 
									&& flagNcellTwoReuse) {
								
								ConfusionCellResult oneConfCellResult = new ConfusionCellResult();
								
								RnoLteCell cellOne = new RnoLteCell();
								RnoLteCell cellTwo = new RnoLteCell();
								RnoLteCell mainCell = new RnoLteCell();
								//暂时注释
								/*cellOne = cellOne.MaptoObject(oneReuseCellList.get(j));
								cellTwo = cellTwo.MaptoObject(oneReuseCellList.get(k));
								mainCell = mainCell.MaptoObject(servingCell.get(0));*/
								
								oneConfCellResult.setCellOne(cellOne);
								oneConfCellResult.setCellTwo(cellTwo);
								oneConfCellResult.setMainCell(mainCell);

								oneConfusionList.add(oneConfCellResult);
								
								//装进混淆小区对列表
								CellPairs collPairs = new CellPairs();
								collPairs.setCellOne(cellOne);
								collPairs.setCellTwo(cellTwo);
								oneConfPairsList.add(collPairs);
							}
						}
						
					}	
					
				}
			}
			
			//System.out.println(oneCollAndConfCellList);
			collisionList.add(oneCollisionList);
			confusionList.add(oneConfusionList);
			collPairsList.add(oneCollPairsList);
			confPairsList.add(oneConfPairsList);
		}
		//System.out.println(collisionList);
		
		//将查询结果放入result
		result.setPage(page);
		result.setPciList(pciList);
		result.setCollisionList(collisionList);
		result.setConfusionList(confusionList);
		result.setCollPairsList(collPairsList);
		result.setConfPairsList(confPairsList);
		
		return result;
	}
}
