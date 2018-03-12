package com.iscreate.op.service.rno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.iscreate.op.action.rno.CellCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.action.rno.model.Hw2GMrrDescQueryCond;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.RnoBscDao;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.rno.RnoResourceManageDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.system.SysAreaService;

public class RnoResourceManagerServiceImpl implements RnoResourceManagerService {

	private static Log log = LogFactory
			.getLog(RnoResourceManagerServiceImpl.class);
	// ---注入----//
	private RnoCellDao rnoCellDao;
	private RnoBscDao rnoBscDao;
	private SysAreaService sysAreaService;
	public MemcachedClient memCached;
	private SysAreaDao sysAreaDao;

	//
	private RnoResourceManageDao rnoResourceManageDao;

	public RnoResourceManageDao getRnoResourceManageDao() {
		return rnoResourceManageDao;
	}

	public void setRnoCellDao(RnoCellDao rnoCellDao) {
		this.rnoCellDao = rnoCellDao;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public void setRnoBscDao(RnoBscDao rnoBscDao) {
		this.rnoBscDao = rnoBscDao;
	}

	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setRnoResourceManageDao(
			RnoResourceManageDao rnoResourceManageDao) {
		this.rnoResourceManageDao = rnoResourceManageDao;
	}

	// -------------------------------------------------------------------------------//

	/**
	 * 获取用户可访问的指定级别的区域
	 */
	public List<Area> gisfindAreaInSpecLevelListByUserId(String accountId,
			String areaLevel) {
		log.info("进入 gisfindAreaListByUserId 。 accountId=" + accountId);

		List<Map<String, Object>> user_area_list = sysAreaService
				.getAreaByAccount(accountId);

		HashSet<Long> areaIdSet = new HashSet<Long>();

		for (Map<String, Object> usea : user_area_list) {
			areaIdSet.add(Long.parseLong(usea.get("AREA_ID").toString()));
			// areaIds[i++] = Long.parseLong(usea.get("AREA_ID").toString());
		}

		long[] areaIds = new long[areaIdSet.size()];
		int i = 0;
		Iterator<Long> iter = areaIdSet.iterator();
		while (iter.hasNext()) {
			areaIds[i++] = iter.next();
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
		//
		// System.out.println("gisfindAreaListByUserId result==="
		// + result_user_area_list);
		// log.info("退出 gisfindAreaListByUserId 。 返回 =" +
		// result_user_area_list);
		return result;
	}

	/**
	 * 获取指定区域下的bsc列表
	 * 
	 * @param areaId
	 * @return Sep 16, 2013 11:16:17 AM gmh
	 */
	public List<RnoBsc> getBscsResideInAreaByAreaId(long areaId) {
		log.debug("进入getBscsResideInAreaByAreaId。areaId=" + areaId);
		List<RnoBsc> bscs = rnoBscDao.getBscsResideInArea(areaId);
		log.debug("退出getBscsResideInAreaByAreaId。bscs="
				+ (bscs == null ? 0 : bscs.size()));
		return bscs;
	}

	/**
	 * 分页查询小区
	 * 
	 * @param page
	 * @param condition
	 * @param allAllowAreaIds
	 * @return Sep 16, 2013 3:49:17 PM gmh
	 */
	public List<Cell> queryCellByPage(Page page, CellCondition condition,
			List<Long> allAllowAreaIds) {
		log.info("进入queryCellByPage。page=" + page + ",condition=" + condition
				+ ",allAllowAreaIds=" + allAllowAreaIds);
		if (page == null || condition == null) {
			log.error("queryCellByPage 传递的参数不正确！");
			return Collections.emptyList();
		}

		// 符合条件的记录
		String where = condition.buildSqlWhere(allAllowAreaIds);
		List<Cell> cells = rnoCellDao.queryCellByPage(page, where);

		log.info("退出queryCellByPage。获取结果数量：" + cells == null ? 0 : cells.size());

		return cells;
	}

	/**
	 * 获取满足条件的小区的总数
	 * 
	 * @param conditions
	 * @param rebuildCache
	 *            是否重新生成缓存
	 * @return Sep 16, 2013 5:29:04 PM gmh
	 */
	public int getTotalQueryCellCnt(String conditions, boolean rebuildCache) {
		log.info("进入方法：getTotalQueryCellCnt。conditions=" + conditions
				+ ",rebuildCache=" + rebuildCache);
		// String key = "QueryCell" + conditions.hashCode();
		Integer cnt = null;
		// if (rebuildCache) {
		// log.info("需要删除缓存的符合条件的查询小区数量，重新生成。");
		// try {
		// memCached.delete(key);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// } else {
		// try {
		// cnt = memCached.get(key);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		//
		// if (cnt == null) {
		cnt = rnoCellDao.getTotalCellCntMeetCondition(conditions);
		// try {
		// memCached.set(key,
		// RnoConstant.TimeConstant.CellQueryResultTotalCountTime,
		// cnt);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		return cnt;
	}

	/**
	 * 分页获取区/县的giscell
	 * 
	 * @param areaId
	 *            指定区域
	 * @param page
	 *            分页参数
	 * 
	 * @return Sep 17, 2013 11:54:22 AM gmh
	 */
	public GisCellQueryResult getGisCellByPage(long areaId, Page page) {
		return getGisCellByPage(areaId, page, "");
	}

	/**
	 * 分页查询邻区
	 * 
	 * @param page
	 * @param condition
	 * @param allAllowAreaIds
	 * @return Sep 17, 2013 3:45:01 PM gmh
	 */
	public List<RnoNcell> queryNCellByPage(Page page, CellCondition condition,
			List<Long> allAllowAreaIds) {
		log.info("queryNCellByPage。page=" + page + ",condition=" + condition
				+ ",allAllowAreaIds=" + allAllowAreaIds);
		if (page == null || condition == null) {
			log.error("queryNCellByPage 传递的参数不正确！");
			return Collections.emptyList();
		}

		// 符合条件的记录
		String where = condition.buildNcellQuerySqlWhere(allAllowAreaIds);
		List<RnoNcell> ncells = rnoCellDao.queryNCellByPage(page, where);

		log.info("queryNCellByPage。获取结果数量：" + ncells == null ? 0 : ncells
				.size());

		return ncells;
	}

	/**
	 * 获取满足条件的邻小区的总数
	 * 
	 * @param buildNcellQuerySqlWhere
	 * @param rebuildCache
	 *            是否强制更新cache
	 * @return Sep 17, 2013 4:24:46 PM gmh
	 */
	public int getTotalQueryNCellCnt(String buildNcellQuerySqlWhere,
			boolean rebuildCache) {
		log.info("进入方法：getTotalQueryNCellCnt。buildNcellQuerySqlWhere="
				+ buildNcellQuerySqlWhere + ",rebuildCache=" + rebuildCache);
		String key = "QueryNCell" + buildNcellQuerySqlWhere.hashCode();
		Integer cnt = null;
		if (rebuildCache) {
			log.info("需要删除缓存的符合条件的邻区数量，重新生成。");
			try {
				memCached.delete(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				cnt = memCached.get(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cnt == null) {
			cnt = rnoCellDao
					.getTotalNcellCntMeetCondition(buildNcellQuerySqlWhere);
			try {
				memCached.set(key,
						RnoConstant.TimeConstant.CellQueryResultTotalCountTime,
						cnt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return cnt;
	}

	/**
	 * 删除逗号分隔的邻区id指定的邻区关系
	 * 
	 * @param allAllowAreaIds允许操作的区域
	 * @param ncellIds
	 *            如 1,2,3,4 Sep 17, 2013 5:38:18 PM gmh
	 */
	public int deleteNcellByIds(String cell, List<Long> allAllowAreaIds,
			String ncellIds) {
		log.info("进入deleteNcellByIds。 ncellIds=" + ncellIds);
		int cnt = rnoCellDao.deleteNcellByIds(allAllowAreaIds, ncellIds);
		// String key = RnoConstant.CacheConstant.CACHE_NCELL_OF_CELL_PRE +
		// cell;
		// try {
		// //删除缓存的ncell信息
		// memCached.delete(key);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		log.info("退出deleteNcellByIds。 cnt=" + cnt);
		return cnt;
	}

	/**
	 * 根据小区英文名查找小区详情
	 * 
	 * @param label
	 * @return Sep 18, 2013 5:49:54 PM gmh
	 */
	public Cell getCellDetail(String label) {
		Cell cell = rnoCellDao.getCellByLabel(label);
		return cell;
	}
	/**
	 * 根据小区名查找小区详情
	 * chen.c10
	 */
	public RnoLteCell getLteCellDetail(String busCellId) {
		RnoLteCell lteCell = rnoCellDao.getLteCellByBusCellId(busCellId);
		return lteCell;
	}
	/**
	 * 通过ID查询返回一条小区记录
	 */
	public Cell queryGisCellById(long id) {
		log.info("进入queryGisCellById.id=" + id);
		Cell cell = null;
		try {
			cell = rnoCellDao.queryCellById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("退出queryGisCellById. cell=" + cell);
		return cell;
	}

	/**
	 * 传入一个小区对象并更新保存
	 */
	public boolean updateCellInfo(Cell cell) {
		log.info("进入updateCellInfo. cell=" + cell);
		boolean b = false;
		try {
			rnoCellDao.updateCellInfo(cell);
			b = true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			b = false;
		}
		log.info("退出updateCellInfo. cell=" + b);
		return b;
	}

	/**
	 * 获取指定区域下的小区的邻区
	 * 
	 * @param cell
	 * @return
	 */
	public List<RnoNcell> getNcellsByCell(String cell) {
		log.info("进入getNcellsByCellAndAreaId。cell=" + cell);
		if (cell == null || cell.isEmpty()) {
			log.warn("传入方法getNcellsByCellAndAreaId的参数不能为空！");
			return Collections.EMPTY_LIST;
		}
		log.info("尝试从缓存获取。");
		// String key = RnoConstant.CacheConstant.CACHE_NCELL_OF_CELL_PRE +
		// cell;
		List<RnoNcell> ncells = null;
		// try {
		// ncells = memCached.get(key);
		// log.info("从缓存中获取的小区：" + cell + " 的邻区为：" + ncells);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		if (ncells == null) {
			ncells = rnoCellDao.getNcellByCell(cell);
			// try {
			// log.info("将查询到小区的邻区的结果放入缓存。");
			// memCached.set(key,
			// RnoConstant.TimeConstant.NcellOfSingleCellTime, ncells);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		log.info("退出getNcellsByCellAndAreaId。返回：" + ncells);
		return ncells;
	}

	/**
	 * 通过gps坐标得到百度三点经纬度坐标
	 * 
	 * @return lnglats
	 * @author chao.xj
	 */
	public String getGisPointArray(String gpsLng, String gpsLat, int azimuth,
			int ScatterAngle, int lenofside) {
		// TODO Auto-generated method stub
		log.info("进入getThreePointCoordinate.gpsLng+gpsLat+azimuth+ScatterAngle+lenofside="
				+ gpsLng + gpsLat + azimuth + ScatterAngle + lenofside);
		String baidulnglat[] = CoordinateHelper.changeFromGpsToBaidu(gpsLng,
				gpsLat);
		String lnglats = baidulnglat[0] + "," + baidulnglat[1];
		double[][] dd = CoordinateHelper.OutputCoordinates(
				Double.parseDouble(baidulnglat[0]),
				Double.parseDouble(baidulnglat[1]), azimuth, ScatterAngle,
				lenofside);
		for (int i = 0; i < dd.length; i++) {
			// 组装拼接字符串
			// dao:113.39386431799,23.048738277964;113.39448263674853,23.047925746178667;113.39395828577615,23.04775013185594
			lnglats = lnglats + ";";
			for (int j = 0; j < dd[i].length; j++) {
				// System.out.println("aa"+dd[i][j]);
				if (i == (dd.length - 1)) {
					if (j == dd[i].length - 1) {
						lnglats += dd[i][j];
					} else {
						lnglats += dd[i][j] + ",";
					}
				} else {
					if (j != dd[i].length - 1) {
						lnglats = lnglats + dd[i][j] + ",";
					} else {
						lnglats = lnglats + dd[i][j];
					}
				}

				// System.out.println((dd[0][0])+","+dd[0][1]+","+dd[1][0]+","+dd[1][1]);
			}
		}
		log.info("进入getThreePointCoordinate.lnglats=" + lnglats);
		return lnglats;
	}

	/**
	 * 获取所有的覆盖区域、覆盖类型、重要等级
	 * 
	 * @return
	 * @author brightming 2013-10-30 下午5:18:26
	 */
	public Map<String, List<String>> getAllCoverareaCovertypeAndImportances() {
		String sql = "select t0.coverarea,t3.covertype,t3.importancegrade from (select DISTINCT(coverarea) from cell) t0 full join (select t1.covertype ,t2.importancegrade from (select DISTINCT(covertype),'ct' as ct  from cell)t1 full join (select DISTINCT(importancegrade),'it' as it from cell) t2 on t1.ct=t2.it) t3 on t0.coverarea=t3.covertype";

		List<Map<String, Object>> midresult = rnoCellDao.executeQuery(sql);
		// System.out.println("midresult="+midresult);
		List<String> coverareas = new ArrayList<String>();
		List<String> covertypes = new ArrayList<String>();
		List<String> importance = new ArrayList<String>();

		Map<String, List<String>> result = new HashMap<String, List<String>>();
		result.put("coverarea", coverareas);
		result.put("covertype", covertypes);
		result.put("importancegrade", importance);
		if (midresult != null && midresult.size() > 0) {
			String ca = "";
			String ct = "";
			String it = "";
			for (Map<String, Object> one : midresult) {
				ca = one.get("COVERAREA") + "";
				ct = one.get("COVERTYPE") + "";
				it = one.get("IMPORTANCEGRADE") + "";
				// System.out.println("ca="+ca+",ct="+ct+",it="+it);
				if (!"".equals(ca) && !"null".equals(ca) && !"0".equals(ca)) {
					if (!coverareas.contains(ca)) {
						coverareas.add(ca);
					}
				}
				if (!"".equals(ct) && !"null".equals(ct) && !"0".equals(ct)) {
					if (!covertypes.contains(ct)) {
						covertypes.add(ct);
					}
				}
				if (!"".equals(it) && !"null".equals(it) && !"0".equals(it)) {
					if (!importance.contains(it)) {
						importance.add(it);
					}
				}
			}
		}

		return result;
	}

	public RnoCellDao getRnoCellDao() {
		return rnoCellDao;
	}

	public RnoBscDao getRnoBscDao() {
		return rnoBscDao;
	}

	public SysAreaService getSysAreaService() {
		return sysAreaService;
	}

	public MemcachedClient getMemCached() {
		return memCached;
	}

	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	/**
	 * 分页获取区/县的giscell通过配置或区域
	 * 
	 * @title
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-4-15上午11:55:58
	 * @company 怡创科技
	 * @version 1.2
	 */
	public GisCellQueryResult getGisCellUseConfigIdOrAreaByPage(
			boolean reSelected, String areaIds, String configIds, Page page) {
		log.info("进入方法：getGisCellByPage 。areaid=" + areaIds + ",page=" + page);
		//
		List<RnoGisCell> gisCells = null;

		if (gisCells == null) {
			log.info("从数据库获取->");
			gisCells = rnoCellDao.getRnoGisCellUseConfigIdOrArea(reSelected,
					areaIds, configIds);
			log.info("从数据库中获取结果数量为：" + gisCells == null ? 0 : gisCells.size());
			// System.out.println("从数据库中获取结果为:"+gisCells==null?0:gisCells.size());
			//
		}
		Integer totalCnt = 0;
		List<RnoGisCell> resultGisCells = new ArrayList<RnoGisCell>();
		if (gisCells != null && gisCells.size() > 0) {
			totalCnt = gisCells.size();
			int start = page.getForcedStartIndex() == -1 ? (page
					.getCurrentPage() - 1) * page.getPageSize() : page
					.getForcedStartIndex();
			// int end = page.getCurrentPage() * page.getPageSize();
			int end = start + page.getPageSize();
			// /范围[start,end)
			int size = gisCells.size();
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
			resultGisCells = gisCells.subList(start, end);

		}

		GisCellQueryResult result = new GisCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setGisCells(resultGisCells);

		return result;
	}

	/**
	 * 
	 * @title 分页获取区/县的giscell（带频点类型）
	 * @param areaId
	 * @param page
	 * @param freqType
	 * @return
	 * @author chao.xj
	 * @date 2014-6-27下午07:20:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public GisCellQueryResult getGisCellByPage(long areaId, Page page,
			String freqType) {
		log.info("进入方法：getGisCellByPage 。areaid=" + areaId + ",page=" + page
				+ ",freqType=" + freqType);
		//
		List<RnoGisCell> gisCells = null;
		String cacheKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
				+ freqType + areaId;

		// 2014-8-11 add
		// 默认每页100条
		cacheKey += page.getCurrentPage();

		// 获取total count
		Integer totalCnt = null;
		String totalCountKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
				+ freqType + areaId + "_TotalCnt";

		try {
			totalCnt = (Integer) memCached.get(totalCountKey);
		} catch (Exception e) {

		}
		if (totalCnt == null) {
			// 获取总数
			log.debug("获取区域[" + areaId + "]小区总数量");
			totalCnt = rnoCellDao.getRnoGisCellCntByFreqTypeInArea(areaId,
					freqType);
			try {
				memCached.set(totalCountKey,
						RnoConstant.TimeConstant.TokenTime, totalCnt);
			} catch (Exception e) {
			}
			// page cnt
			String totalPageKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
					+ freqType + areaId + "_TotalPage";
			int pageCnt = totalCnt / page.getPageSize()
					+ (totalCnt % page.getPageSize() == 0 ? 0 : 1);
			try {
				memCached.set(totalPageKey, RnoConstant.TimeConstant.TokenTime,
						pageCnt);
			} catch (Exception e) {
			}

		}
		try {
			log.info("从缓存中获取->");
			gisCells = (List<RnoGisCell>) memCached.get(cacheKey);
			log.info("缓存获取的结果数量为 ："
					+ ((gisCells == null) ? 0 : gisCells.size()));
			// System.out.println("缓存获取的结果为
			// ："+gisCells==null?0:gisCells.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (gisCells == null) {
			log.info("从数据库获取->");
			// gisCells = rnoCellDao.getRnoGisCellInArea(areaId);
			gisCells = rnoCellDao.getRnoGisCellInAreaByFreqTypeAndPage(areaId,
					freqType, page);
			log.info("从数据库中获取结果数量为：" + gisCells == null ? 0 : gisCells.size());
			// System.out.println("从数据库中获取结果为:"+gisCells==null?0:gisCells.size());
			//
			log.info("放入缓存->");
			try {
				memCached.set(cacheKey, RnoConstant.TimeConstant.TokenTime,
						gisCells);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		GisCellQueryResult result = new GisCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setGisCells(gisCells);

		return result;
	}
	/**
	 * 查询符合条件的爱立信fas描述记录
	 * 
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:00:20
	 */
	public List<Map<String, Object>> queryEriFasDescByPage(
			HashMap<String, Object> attachParams, Page page) {
		log.info("进入方法：queryEriFasDescByPage。attachParams=" + attachParams
				+ ",page=" + page);

		if (page == null) {
			log.warn("方法：queryEriFasDescByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoResourceManageDao.getFasDescriptorCount(attachParams);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoResourceManageDao
				.queryFasDescriptorByPage(attachParams, startIndex, cnt);

		return res;
	}
	/**
	 * 查询符合条件的爱立信mrr描述记录
	 * 
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:44:13
	 */
	public List<Map<String, Object>> queryEriMrrDescByPage(
			HashMap<String, Object> attachParams, Page page) {
		log.info("进入方法：queryEriMrrDescByPage。attachParams=" + attachParams
				+ ",page=" + page);

		if (page == null) {
			log.warn("方法：queryEriMrrDescByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoResourceManageDao.getMrrDescriptorCount(attachParams);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoResourceManageDao
				.queryMrrDescriptorByPage(attachParams, startIndex, cnt);
		// 数据量
		// long descId;
		// long count;
		// if(res != null && res.size() > 0) {
		// for (Map<String, Object> one : res) {
		// descId = Long.parseLong((String)one.get("ERI_MRR_DESC_ID"));
		// count = rnoResourceManageDao.getEriMrrFileRecNumByDescId(descId);
		// one.put("REC_NUM", count);
		// }
		// }
		return res;
	}

	/**
	 * 查询爱立信Mrr详情信息
	 * 
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午05:17:42
	 */
	public List<Map<String, Object>> queryEriMrrDetailByPage(
			HashMap<String, Object> attachParams, Page page) {
		log.info("进入方法：queryEriMrrDetailByPage。attachParams=" + attachParams
				+ ",page=" + page);

		if (page == null) {
			log.warn("方法：queryEriMrrDetailByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}

		long mrrDescId = Long.parseLong((String) attachParams.get("mrrDescId"));

		List<Map<String, Object>> mrrDescDetails = rnoResourceManageDao
				.getEriMrrDetailByDescId(mrrDescId);
		long cityId;
		String meaTime = "";
		if (mrrDescDetails != null && mrrDescDetails.size() > 0) {
			if (mrrDescDetails.get(0).get("CITY_ID") != null
					&& mrrDescDetails.get(0).get("MEA_DATE") != null) {
				cityId = Long.parseLong(mrrDescDetails.get(0).get("CITY_ID")
						.toString());
				meaTime = mrrDescDetails.get(0).get("MEA_DATE").toString();
			} else {
				log.warn("mrrDescId=" + mrrDescId + "的描述信息中，cityId或者meaTime为空");
				return Collections.EMPTY_LIST;
			}
		} else {
			log.warn("mrrDescId=" + mrrDescId + ",不存在对应描述信息");
			return Collections.EMPTY_LIST;
		}

		meaTime = meaTime.substring(0, meaTime.indexOf("."));

		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoResourceManageDao.getEriMrrCellAndBscCntByDescId(
					mrrDescId, cityId, meaTime);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> resCellAndBsc = rnoResourceManageDao
				.queryEriMrrCellAndBscByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resUlQua6t7Rate = rnoResourceManageDao
				.queryEriMrrUlQua6t7RateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resDlQua6t7Rate = rnoResourceManageDao
				.queryEriMrrDlQua6t7RateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resUlStrenRate = rnoResourceManageDao
				.queryEriMrrUlStrenRateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resDlStrenRate = rnoResourceManageDao
				.queryEriMrrDlStrenRateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resDlWeekSignal = rnoResourceManageDao
				.queryEriMrrDlWeekSignalByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resAverTa = rnoResourceManageDao
				.queryEriMrrAverTaByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resMaxTa = rnoResourceManageDao
				.queryEriMrrMaxTaByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resUlQua0t5Rate = rnoResourceManageDao
				.queryEriMrrUlQua0t5RateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);
		List<Map<String, Object>> resDlQua0t5Rate = rnoResourceManageDao
				.queryEriMrrDlQua0t5RateByDescId(mrrDescId, cityId, meaTime,
						startIndex, cnt);

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		String cell = "";
		for (int i = 0; i < resCellAndBsc.size(); i++) {
			map = new HashMap<String, Object>();
			cell = resCellAndBsc.get(i).get("CELL_NAME").toString();
			map.put("CELL_NAME", cell);
			map.put("BSC", resCellAndBsc.get(i).get("BSC").toString());
			if (resUlQua6t7Rate.get(i).get("UL_QUA6T7_RATE") == null) {
				map.put("UL_QUA6T7_RATE", "--");
			} else {
				if (cell.equals(resUlQua6t7Rate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("UL_QUA6T7_RATE",
							resUlQua6t7Rate.get(i).get("UL_QUA6T7_RATE"));
				} else {
					map.put("UL_QUA6T7_RATE", "--");
				}
			}
			if (resDlQua6t7Rate.get(i).get("DL_QUA6T7_RATE") == null) {
				map.put("DL_QUA6T7_RATE", "--");
			} else {
				if (cell.equals(resDlQua6t7Rate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("DL_QUA6T7_RATE",
							resDlQua6t7Rate.get(i).get("DL_QUA6T7_RATE"));
				} else {
					map.put("DL_QUA6T7_RATE", "--");
				}
			}
			if (resUlStrenRate.get(i).get("UL_STREN_RATE") == null) {
				map.put("UL_STREN_RATE", "--");
			} else {
				if (cell.equals(resUlStrenRate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("UL_STREN_RATE",
							resUlStrenRate.get(i).get("UL_STREN_RATE"));
				} else {
					map.put("UL_STREN_RATE", "--");
				}
			}
			if (resDlStrenRate.get(i).get("DL_STREN_RATE") == null) {
				map.put("DL_STREN_RATE", "--");
			} else {
				if (cell.equals(resDlStrenRate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("DL_STREN_RATE",
							resDlStrenRate.get(i).get("DL_STREN_RATE"));
				} else {
					map.put("DL_STREN_RATE", "--");
				}
			}
			if (resDlWeekSignal.get(i).get("DL_WEEK_SIGNAL") == null) {
				map.put("DL_WEEK_SIGNAL", "--");
			} else {
				if (cell.equals(resDlWeekSignal.get(i).get("CELL_NAME")
						.toString())) {
					map.put("DL_WEEK_SIGNAL",
							resDlWeekSignal.get(i).get("DL_WEEK_SIGNAL"));
				} else {
					map.put("DL_WEEK_SIGNAL", "--");
				}
			}
			if (resAverTa.get(i).get("AVER_TA") == null) {
				map.put("AVER_TA", "--");
			} else {
				if (cell.equals(resAverTa.get(i).get("CELL_NAME").toString())) {
					map.put("AVER_TA", resAverTa.get(i).get("AVER_TA"));
				} else {
					map.put("AVER_TA", "--");
				}
			}
			if (resMaxTa.get(i).get("MAX_TA") == null) {
				map.put("MAX_TA", "--");
			} else {
				if (cell.equals(resMaxTa.get(i).get("CELL_NAME").toString())) {
					map.put("MAX_TA", resMaxTa.get(i).get("MAX_TA"));
				} else {
					map.put("MAX_TA", "--");
				}
			}
			if (resUlQua0t5Rate.get(i).get("UL_QUA0T5_RATE") == null) {
				map.put("UL_QUA0T5_RATE", "--");
			} else {
				if (cell.equals(resUlQua0t5Rate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("UL_QUA0T5_RATE",
							resUlQua0t5Rate.get(i).get("UL_QUA0T5_RATE"));
				} else {
					map.put("UL_QUA0T5_RATE", "--");
				}
			}
			if (resDlQua0t5Rate.get(i).get("DL_QUA0T5_RATE") == null) {
				map.put("DL_QUA0T5_RATE", "--");
			} else {
				if (cell.equals(resDlQua0t5Rate.get(i).get("CELL_NAME")
						.toString())) {
					map.put("DL_QUA0T5_RATE",
							resDlQua0t5Rate.get(i).get("DL_QUA0T5_RATE"));
				} else {
					map.put("DL_QUA0T5_RATE", "--");
				}
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * 查询符合条件的爱立信ncs描述记录数量
	 * 
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming 2014-8-22 下午1:34:48
	 */
	public long queryEriNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond) {
		return rnoResourceManageDao.queryEriNcsDescCnt(eri2gNcsDescQueryCond);
	}

	/**
	 * 分页查询符合条件的爱立信ncs描述记录
	 * 
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming 2014-8-22 下午1:35:14
	 */
	public List<Map<String, Object>> queryEriNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond, Page newPage) {
		return rnoResourceManageDao.queryEriNcsDescByPage(
				eri2gNcsDescQueryCond, newPage);
	}

	/**
	 * 查询符合条件的华为ncs的描述信息的数量
	 * 
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming 2014-8-24 下午5:15:52
	 */
	public long queryHwNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond) {
		return rnoResourceManageDao.queryHwNcsDescCnt(eri2gNcsDescQueryCond);
	}

	/**
	 * 分页查询符合条件的华为ncs数据
	 * 
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming 2014-8-24 下午5:16:18
	 */
	public List<Map<String, Object>> queryHwNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond, Page newPage) {
		return rnoResourceManageDao.queryHwNcsDescByPage(eri2gNcsDescQueryCond,
				newPage);
	}

	/**
	 * 
	 * @title 查询符合条件的华为mrr的描述信息的数量
	 * @param hw2gMrrDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwMrrDescCnt(Hw2GMrrDescQueryCond hw2gMrrDescQueryCond) {
		return rnoResourceManageDao.queryHwMrrDescCnt(hw2gMrrDescQueryCond);
	}

	/**
	 * 
	 * @title 分页查询符合条件的华为mrr数据
	 * @param hw2gMrrDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwMrrDescByPage(
			Hw2GMrrDescQueryCond hw2gMrrDescQueryCond, Page newPage) {
		return rnoResourceManageDao.queryHwMrrDescByPage(hw2gMrrDescQueryCond,
				newPage);
	}

	/**
	 * 通过区域id获取其所有子区域id
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午04:44:23
	 */
	public String getSubAreaByAreaId(long areaId) {
		
		List<Long> subAreaIds = AuthDsDataDaoImpl.getSubAreaIdsByCityId(areaId);

		//设置数据源
		/*DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		List<Map<String, Object>> areaList = rnoCellDao.getSubAreaByAreaId(areaId);
		//恢复数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);*/
		
		String areaStr = "";
		if(subAreaIds.size() > 0) {
			for (Long one : subAreaIds) {
				areaStr += one + ",";
			}
			//加上自身，以防查询出全部为第四级区域（小区保存最低为第三级区域）
			areaStr += areaId;
			//areaStr = areaStr.substring(0, areaStr.length()-1);
		} else {
			if(areaId != 0) {
				areaStr = areaId + "";
			} else {
				areaStr = "-1";
			}
		}
		return areaStr;
	}
	
	/**
	 * 通过地图网格方式分页获取小区
	 * @param areaId
	 * @param mapGrid
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18上午09:15:13
	 */
	public GisCellQueryResult getGisCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page) {
		return getGisCellByMapGrid(areaId, mapGrid, page, "0");
	}

	/**
	 * 通过地图网格方式分页获取小区（带频点类型）
	 * @param areaId
	 * @param mapGrid
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18上午09:15:13
	 */
	@SuppressWarnings("unchecked")
	public GisCellQueryResult getGisCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page, String freqType) {
		log.info("进入方法：getGisCellByMapGrid 。areaid=" + areaId + ", mapGrid="
				+ mapGrid + " page=" + page + ", freqType=" + freqType);

		List<RnoGisCell> gisCells = null;
		//获取地图网格id
		String mapGridId = mapGrid.get("gridId").toString();
		//获取地图网格的坐标范围
		String minLng = mapGrid.get("minLng").toString();
		String minLat = mapGrid.get("minLat").toString();
		String maxLng = mapGrid.get("maxLng").toString();
		String maxLat = mapGrid.get("maxLat").toString();
		//左下角gps坐标
		String[] blPoint = CoordinateHelper.changeFromBaiduToGps(minLng, minLat);
		//右上角gps坐标
		String[] trPoint = CoordinateHelper.changeFromBaiduToGps(maxLng, maxLat);
		//参数 + 区域id + 网格id + 当前页码
		String cacheKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
				+ freqType + "_" + areaId + "_" + mapGridId + "_" + page.getCurrentPage();

		//单独获取对应的所有区域id
		String areaStr = getSubAreaByAreaId(areaId);
		
		//获取total count
		Integer totalCnt = null;
        String totalCountKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
				+ freqType + "_" + areaId + "_" + mapGridId +"_TotalCnt";
        try{
        	totalCnt = (Integer)memCached.get(totalCountKey);
        }catch(Exception e){
        	
        }
        
        if(totalCnt==null){
        	//获取总数
        	log.debug("获取区域["+areaId+"]的网格["+mapGridId+"]小区总数量");
        	System.out.println("获取区域["+areaId+"]的网格["+mapGridId+"]小区总数量");
        	totalCnt = rnoCellDao.getRnoGisCellCntByGridInArea(areaStr, blPoint,
					trPoint, freqType);
        	try {
				memCached.set(totalCountKey, RnoConstant.TimeConstant.TokenTime,
						totalCnt);
			} catch (Exception e) {
			}
        	//page cnt
        	String totalPageKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
    				+ freqType + "_" + areaId + "_" + mapGridId + "_TotalPage";
            int pageCnt = totalCnt / page.getPageSize()
    				+ (totalCnt % page.getPageSize() == 0 ? 0 : 1);
            try {
            	memCached.set(totalPageKey, RnoConstant.TimeConstant.TokenTime,
            		pageCnt);
            } catch (Exception e) {
			}
    		
        }
        try {
			log.info("从缓存中获取->");
			gisCells = (List<RnoGisCell>) memCached.get(cacheKey);
			log.info("缓存获取的结果数量为 ："
					+ ((gisCells == null) ? 0 : gisCells.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (gisCells == null) {
			log.info("从数据库获取->");
			gisCells = rnoCellDao.getRnoGisCellByGridAndPageInArea(areaStr, blPoint,
					trPoint, freqType, page);
			log.info("从数据库中获取结果数量为：" + gisCells == null ? 0 : gisCells.size());
			log.info("放入缓存->");
			try {
				memCached.set(cacheKey, RnoConstant.TimeConstant.TokenTime,
						gisCells);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		GisCellQueryResult result = new GisCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setGisCells(gisCells);

		return result;
	}

	/**
	 * 获取额外的giscell用于还没加载到却需要先显示的情况
	 * @param cells
	 * @param cityId
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public GisCellQueryResult getRelaCellByLabels(String cells, long cityId) {
		log.info("进入getRelaCellByLabels，cells="+cells+",cityId="+cityId);
		//单独获取对应的所有区域id
		String areaStr = getSubAreaByAreaId(cityId);
		List<RnoGisCell> gisCells = rnoCellDao.getRelaCellByLabels(cells,areaStr);
		GisCellQueryResult result = new GisCellQueryResult();
		result.setGisCells(gisCells);
		return result;
	}

	/**
	 * 通过小区参数获取cell进行小区数据预加载
	 * @param cellParam
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午07:06:07
	 */
	public GisCellQueryResult getRelaCellByCellParamAndAreaId(
			Map<String, String> cellParam, long areaId) {
		log.info("进入getRelaCellByCellParamAndCityId，cellParam="+cellParam+",areaId="+areaId);
		//单独获取对应的所有区域id
		String areaStr = getSubAreaByAreaId(areaId);
		List<RnoGisCell> gisCells = rnoCellDao.getRelaCellByCellParamAndCityId(cellParam,areaStr);
		GisCellQueryResult result = new GisCellQueryResult();
		result.setGisCells(gisCells);
		return result;
	}
	
	/**
	 * 通过cell获取其邻区数据及主小区数据进行预加载
	 * @param cell
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午06:49:57
	 */
	public GisCellQueryResult getNcellDetailsByCellAndAreaId(String cell,
			long areaId) {
		log.info("进入getNcellDetailsByCellandCityId，cell="+cell+",areaId="+areaId);
		//单独获取对应的所有区域id
		String areaStr = getSubAreaByAreaId(areaId);
		List<RnoGisCell> gisCells = rnoCellDao.getNcellDetailsByCellandCityId(cell,areaStr);
		GisCellQueryResult result = new GisCellQueryResult();
		result.setGisCells(gisCells);
		return result;
	}

	/**
	 * 分页查询BSC信息
	 * @param bscQuery
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:15:50
	 */
	public List<Map<String, Object>> queryBscByPage(
			Map<String, String> bscQuery, Page page) {
		log.info("进入方法：queryBscByPage。bscQuery=" + bscQuery + ",page=" + page);

		if (page == null) {
			log.warn("方法：queryBscByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoResourceManageDao.queryBscCntByCond(bscQuery);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoResourceManageDao
				.queryBscByPage(bscQuery, startIndex, cnt);
		return res;
	}

	/**
	 * 通过名称删除BSC
	 * @param bscEngName
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10上午10:52:19
	 */
	public Map<String, Object> deleteBscByName(String bscEngName, long cityId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		//获取该bsc是否与小区存在关联关系
		boolean flag = rnoResourceManageDao.isBscRelaToCell(bscEngName);
		if(flag) {
			result.put("flag", false);
			result.put("msg", "与小区存在关联关系");
		} else {
			//删除
			//单独获取对应的所有区域id
			String areaStr = getSubAreaByAreaId(cityId);
			flag = rnoResourceManageDao.deleteBscRelatoAreaByName(bscEngName, areaStr);
			if(flag) {
				result.put("flag", true);
			} else {
				result.put("flag", false);
				result.put("msg", "数据删除出错！");
			}
		}	
		return result;
	}

	/**
	 * 新增单个BSC
	 * @param bscEngName
	 * @param manufacturers
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-16下午06:12:15
	 */
	public Map<String, Object> addSingleBsc(String bscEngName, String manufacturers, long cityId) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = rnoResourceManageDao.addSingleBsc(bscEngName,manufacturers,cityId);
		if(flag) {
			result.put("flag", true);
		} else {
			result.put("flag", false);
		}
		return result;
	}

	/**
	 * 分页获取Hbase的爱立信切换数据描述表的数据
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
	public List<Map<String, String>> queryEriHoDataFromHbaseByPage(
			String tableName, String startRow, String stopRow, Page newPage) {
		  
		List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
		ResultScanner scanner = null;
		// 为分页创建的封装类对象，下面有给出具体属性
		try {
			// 获取最大返回结果数量
			if (newPage.getPageSize() == 0)
				newPage.setPageSize(25);
			if (newPage.getCurrentPage() == 0)
				newPage.setCurrentPage(1);
			// 计算起始页和结束页
			Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
			Integer endPage = firstPage + newPage.getPageSize();
			// 从表池中取出HBASE表对象
			HTableInterface table = getHbaseTable(tableName);
			if(table == null) {
				return null;
			}
			// 获取筛选对象
			Scan scan = new Scan();
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(stopRow));
			// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
			scan.setFilter(packageEriHoDescFilters());
			// 缓存1000条数据
			scan.setCaching(1000);
			scan.setCacheBlocks(false);
			scanner = table.getScanner(scan);
			int i = 0;
			List<byte[]> rowList = new LinkedList<byte[]>();
			// 遍历扫描器对象， 并将需要查询出来的数据row key取出
			for (Result r : scanner) {
				String row = Bytes.toString(r.getRow());
				if (i >= firstPage && i < endPage) {
					rowList.add(Bytes.toBytes(row));
				}
				i++;
			}
			// 获取取出的row key的GET对象
			List<Get> getList = new LinkedList<Get>();
	        for (byte[] row : rowList) {
	            Get get = new Get(row);
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
	            getList.add(get);
	        }

			Result[] results = table.get(getList);
			// 遍历结果
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
				
		        Map<String, String> rmap = new LinkedHashMap<String, String>();
		        for (byte[] key : fmap.keySet()) {
		            byte[] value = fmap.get(key);
		            rmap.put(Bytes.toString(key), Bytes.toString(value));
		        }
				mapList.add(rmap);
			}
			
			// 封装分页对象
			newPage.setTotalCnt(i);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return mapList;
	}
	
	/**
	 * 封装查询条件
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
    private FilterList packageEriHoDescFilters() {
        FilterList filterList = null;
        // MUST_PASS_ALL(条件 AND) MUST_PASS_ONE（条件OR）
        filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        Filter filter1 = null;
        filter1 = new SingleColumnValueFilter(Bytes.toBytes("DESCINFO"), 
        		Bytes.toBytes("FACTORY"), CompareOp.EQUAL, 
        		Bytes.toBytes("ERI"));
        filterList.addFilter(filter1);

//        if (isPage) {
//            filterList.addFilter(new FirstKeyOnlyFilter());
//        }
        return filterList;
    }
    
    /**
	 * 分页获取Hbase的中兴切换数据描述表的数据
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
	public List<Map<String, String>> queryZteHoDataFromHbaseByPage(
			String tableName, String startRow, String stopRow, Page newPage) {
		  
		List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
		ResultScanner scanner = null;
		// 为分页创建的封装类对象，下面有给出具体属性
		try {
			// 获取最大返回结果数量
			if (newPage.getPageSize() == 0)
				newPage.setPageSize(25);
			if (newPage.getCurrentPage() == 0)
				newPage.setCurrentPage(1);
			// 计算起始页和结束页
			Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
			Integer endPage = firstPage + newPage.getPageSize();
			// 从表池中取出HBASE表对象
			HTableInterface table = getHbaseTable(tableName);
			if(table == null) {
				return null;
			}
			// 获取筛选对象
			Scan scan = new Scan();
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(stopRow));
			// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
			scan.setFilter(packageZteHoDescFilters());
			// 缓存1000条数据
			scan.setCaching(1000);
			scan.setCacheBlocks(false);
			scanner = table.getScanner(scan);
			int i = 0;
			List<byte[]> rowList = new LinkedList<byte[]>();
			// 遍历扫描器对象， 并将需要查询出来的数据row key取出
			for (Result r : scanner) {
				String row = Bytes.toString(r.getRow());
				if (i >= firstPage && i < endPage) {
					rowList.add(Bytes.toBytes(row));
				}
				i++;
			}
			// 获取取出的row key的GET对象
			List<Get> getList = new LinkedList<Get>();
	        for (byte[] row : rowList) {
	            Get get = new Get(row);
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
	            getList.add(get);
	        }

			Result[] results = table.get(getList);
			// 遍历结果
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
				
		        Map<String, String> rmap = new LinkedHashMap<String, String>();
		        for (byte[] key : fmap.keySet()) {
		            byte[] value = fmap.get(key);
		            rmap.put(Bytes.toString(key), Bytes.toString(value));
		        }
				mapList.add(rmap);
			}
			
			// 封装分页对象
			newPage.setTotalCnt(i);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return mapList;
	}
	
	/**
	 * 封装查询条件
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
    private FilterList packageZteHoDescFilters() {
        FilterList filterList = null;
        // MUST_PASS_ALL(条件 AND) MUST_PASS_ONE（条件OR）
        filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        Filter filter1 = null;
        filter1 = new SingleColumnValueFilter(Bytes.toBytes("DESCINFO"), 
        		Bytes.toBytes("FACTORY"), CompareOp.EQUAL, 
        		Bytes.toBytes("ZTE"));
        filterList.addFilter(filter1);
//
//        if (isPage) {
//            filterList.addFilter(new FirstKeyOnlyFilter());
//        }
        return filterList;
    }
    
	/**
	 * 获取Hbase的table
	 * @param tableName
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
    public HTableInterface getHbaseTable(String tableName) {
        if (StringUtils.isEmpty(tableName))
            return null;
        HTable table = null;
        try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			table = new HTable(conf,tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return table;
    }

	/**
	 * 获取lte小区
	 */
	public LteCellQueryResult getLteCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page,
			String freqType,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.info("进入方法：getLteCellByMapGrid 。areaid=" + areaId + ", mapGrid="
				+ mapGrid + " page=" + page + ", freqType=" + freqType);
		List<RnoLteCell> lteCells = null;
		//获取地图网格id
		String mapGridId = mapGrid.get("gridId").toString();
		//获取地图网格的坐标范围
		String minLng = mapGrid.get("minLng").toString();
		String minLat = mapGrid.get("minLat").toString();
		String maxLng = mapGrid.get("maxLng").toString();
		String maxLat = mapGrid.get("maxLat").toString();
		//将网格数据存入缓存，转换坐标为性能瓶颈
		String mapGridCoordinates = null;
		String[] blPoint =  null;
		String[] trPoint = null;
		int changeTimes = 0;
		String mapGridKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
				+ areaId + "_" + mapGridId + "_" + page.getPageSize() + "_MapGrid";
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
			}while(blPoint.length!=2||trPoint.length!=2&&++changeTimes<10);
			
			mapGridCoordinates = blPoint[0] + "," + blPoint[1] + "_" + trPoint[0] + "," + trPoint[1];
			try {
				memCached.set(mapGridKey, RnoConstant.TimeConstant.TokenTime,	mapGridCoordinates);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//参数 + 区域id + 网格id + 当前页码
		String cacheKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
				+ freqType + "_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_" + page.getCurrentPage();

		//单独获取对应的所有区域id
		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(areaId);
		
		//获取total count
		Integer totalCnt = null;
        String totalCountKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
				+ freqType + "_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_TotalCnt";
        try{
        	totalCnt = (Integer)memCached.get(totalCountKey);
        }catch(Exception e){
        	
        }
        
        if(totalCnt==null){
        	//获取总数
        	log.debug("获取区域["+areaId+"]的网格["+mapGridId+"]小区总数量");
        	totalCnt = rnoCellDao.getRnoLteCellCntByGridInArea(areaStr, blPoint,
					trPoint, freqType);
        	try {
				memCached.set(totalCountKey, RnoConstant.TimeConstant.TokenTime,
						totalCnt);
			} catch (Exception e) {
			}
        	//page cnt
        	String totalPageKey = RnoConstant.CacheConstant.CACHE_LTE_GISCELL_IN_AREA_PRE
    				+ freqType + "_" + areaId + "_" + mapGridId + "_" + page.getPageSize() + "_TotalPage";
            int pageCnt = totalCnt / page.getPageSize()
    				+ (totalCnt % page.getPageSize() == 0 ? 0 : 1);
            try {
            	memCached.set(totalPageKey, RnoConstant.TimeConstant.TokenTime,
            		pageCnt);
            } catch (Exception e) {
			}
    		
        }
        try {
			log.info("从缓存中获取->");
			lteCells = (List<RnoLteCell>) memCached.get(cacheKey);
			log.info("缓存获取的结果数量为 ："
					+ ((lteCells == null) ? 0 : lteCells.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (lteCells == null) {
        	log.info("从数据库获取->");
        	lteCells = rnoCellDao.getRnoLteCellByGridAndPageInArea(areaStr, blPoint,
        			trPoint, freqType, page);
        	log.info("从数据库中获取结果数量为：" + lteCells == null ? 0 : lteCells.size());
        	//纠偏基准点
        	for (int i = 0; i < lteCells.size(); i++) {
        		String cellBaiduPoint[] = getBaiduLnglat(lteCells.get(i).getLng(),
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
        			String point[] = getBaiduLnglat(Double.parseDouble(onePoint[0]),
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
		LteCellQueryResult result = new LteCellQueryResult();
		result.setTotalCnt(totalCnt);
		result.setLteCells(lteCells);
		return result;
	}
	
	/**
	 * 获取百度坐标
	 * @param longitude
	 * @param latitude
	 * @param standardPoints
	 * @return
	 */
	private String[] getBaiduLnglat(double longitude, double latitude,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		String[] baidulnglat = null;
//		try {
//			// 先从缓存获取
//			baidulnglat = memCached
//					.get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE + suf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为：" + baidulnglat);
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = CoordinateHelper.getLngLatCorrectValue(longitude
						, latitude , standardPoints);
			} else {
				log.info("区域不存在基准点，将使用百度在线接口进行校正。");
				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
			// 保存入缓存
//			try {
//				memCached.set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
//						+ suf, RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
//						baidulnglat);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		
		return baidulnglat;
	}
	/**
	 * 通过小区参数获取LteCell进行小区数据预加载
	 * @param cellParam
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2015年4月17日11:32:41
	 */
	public LteCellQueryResult getLteCellByCellParamAndAreaId(
			Map<String, String> cellParam, long areaId,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.info("进入 getLteCellByCellParamAndAreaId，cellParam="+cellParam+",areaId="+areaId);
		//单独获取对应的所有区域id
		String areaStr = getSubAreaByAreaId(areaId);
		List<RnoLteCell> gisCells = rnoCellDao.getLteCellByCellParamAndCityId(cellParam,areaStr);
		//纠偏基准点
		for (int i = 0; i < gisCells.size(); i++) {
			String cellBaiduPoint[] = getBaiduLnglat(gisCells.get(i).getLng(),
					gisCells.get(i).getLat(), standardPoints);
			if(cellBaiduPoint != null) {
				gisCells.get(i).setLng(Double.parseDouble(cellBaiduPoint[0]));
				gisCells.get(i).setLat(Double.parseDouble(cellBaiduPoint[1]));
			}
			String allLngLats = gisCells.get(i).getAllLngLats();
			String newLngLats = "";
			for (String one : allLngLats.split(";")) {
				String onePoint[] = one.split(",");
				String point[] = getBaiduLnglat(Double.parseDouble(onePoint[0]),
						Double.parseDouble(onePoint[1]), standardPoints);
				if(cellBaiduPoint != null) {
					newLngLats += point[0]+","+point[1];
				}
				newLngLats += ";";
			}
			newLngLats = newLngLats.substring(0,newLngLats.length()-1);
			gisCells.get(i).setAllLngLats(newLngLats);
		}
		LteCellQueryResult result = new LteCellQueryResult();
		result.setLteCells(gisCells);
		return result;
	}

}
