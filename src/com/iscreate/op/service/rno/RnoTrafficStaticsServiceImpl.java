package com.iscreate.op.service.rno;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoReportDao;
import com.iscreate.op.dao.rno.RnoStsDao;
import com.iscreate.op.dao.rno.RnoTrafficStaticsDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.RnoCityQuality;
import com.iscreate.op.pojo.rno.RnoCityqulDetail;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoStsResult;
import com.iscreate.op.pojo.rno.StsConfig;
import com.iscreate.op.service.system.SysAreaService;

public class RnoTrafficStaticsServiceImpl implements RnoTrafficStaticsService {

	private static Log log = LogFactory
			.getLog(RnoTrafficStaticsServiceImpl.class);

	private static Comparator stsConfigCompartor = new Comparator<StsConfig>() {
		public int compare(StsConfig o1, StsConfig o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}
			if (o1.getConfigId() > o2.getConfigId()) {
				return 1;
			}
			return -1;
		}
	};

	// ------------------------------注入---------------------------//
	private RnoTrafficStaticsDao rnoTrafficStaticsDao;
	private MemcachedClient memCached;
	private RnoStsDao rnoStsDao;// 话务dao
	private SysAreaDao sysAreaDao;// 系统区域
	private SysAreaService sysAreaService;//
	private RnoReportDao rnoReportDao;
	private HibernateTemplate hibernateTemplate;
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setRnoTrafficStaticsDao(
			RnoTrafficStaticsDao rnoTrafficStaticsDao) {
		this.rnoTrafficStaticsDao = rnoTrafficStaticsDao;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public void setRnoStsDao(RnoStsDao rnoStsDao) {
		this.rnoStsDao = rnoStsDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}
	public RnoReportDao getRnoReportDao() {
		return rnoReportDao;
	}

	public void setRnoReportDao(RnoReportDao rnoReportDao) {
		this.rnoReportDao = rnoReportDao;
	}
	/**
	 * 
	 * @description: 分页查询话务数据
	 * @author：yuan.yw
	 * @param page
	 *            分页vo
	 * @param queryCondition
	 *            条件vo
	 * @param searchType
	 *            指标类型
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Oct 10, 2013 2:01:35 PM
	 */
	public List<Map<String, Object>> queryStsByPage(Page page,
			StsCondition queryCondition, String searchType) {
		log.info("进入 queryStsByPage(Page page,StsCondition queryCondition, String searchType)。page="
				+ page
				+ ",queryCondition="
				+ queryCondition
				+ ",searchType="
				+ searchType);
		if (page == null || queryCondition == null) {
			log.error("queryStsByPage(Page page,StsCondition queryCondition, String searchType)传递的参数不正确！");
			return Collections.emptyList();
		}
		List<Map<String, Object>> stsList = null;
		if ("CITY_QUALITY".equals(searchType)) {
			stsList = this.rnoStsDao.queryStsByCityQuaByPage(page,
					queryCondition);// 城市网络质量指标查询数据
		} else {
			boolean isAudio=false;
			if("CELL_VIDEO".equals(searchType)){
				isAudio=true;
			}else if("CELL_DATA".equals(searchType)){
				isAudio=false;
			}else{
				log.error("searchType="+searchType+"是未知类型，不予查询！");
				return Collections.EMPTY_LIST;
			}
			stsList = rnoStsDao.queryStsByCellVideoOrDataByPage(page,
					queryCondition,isAudio);// 小区语音业务指标或小区数据业务指标获取数据
			//this.setCellDataToMemCached(queryCondition, false);// 小区语音业务指标或小区数据业务指标查询话务数据，将小区英文信息缓存入MemCached(加载分析列表使用)
		}
		if (stsList == null) {
			log.info("退出 queryStsByPage(Page page,StsCondition queryCondition, String searchType)。获取结果数量：" + 0);
		} else {
			log.info("退出 queryStsByPage(Page page,StsCondition queryCondition, String searchType)。获取结果数量："
					+ stsList.size());
		}

		return stsList;
	}

	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标查询话务数据，将小区英文信息缓存入MemCached(加载分析列表使用)
	 * @author：yuan.yw
	 * @param queryCondition
	 * @param rebuildCache
	 *            是否重新生成缓存
	 * @return void
	 * @date：Oct 11, 2013 9:40:04 AM
	 */
	public List<Map<String, Object>> setCellDataToMemCached(
			StsCondition queryCondition, boolean rebuildCache,boolean isAudio) {
		log.info("进入方法：setCellDataToMemCached(StsCondition queryCondition)。queryCondition="
				+ queryCondition);
		List<Map<String, Object>> cellList = null;
		String key = "QueryStsCell" + queryCondition.toString().hashCode()+isAudio;
		if (rebuildCache) {
			log.info("需要删除缓存的符合条件的小区英文信息，重新生成。");
			try {
				memCached.delete(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				cellList = memCached.get(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cellList == null) {
			cellList = this.rnoStsDao
					.getCellQueryByCellVideoOrData(queryCondition,isAudio);
			try {
				memCached.set(key,
						RnoConstant.TimeConstant.CellQueryResultTotalCountTime,
						cellList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cellList;
	}

	/**
	 * 
	 * @description: 获取符合条件话务数据数量
	 * @author：yuan.yw
	 * @param queryCondition
	 *            条件vo
	 * @param searchType
	 *            指标类型
	 * @param rebuildCache
	 *            是否重新生成缓存
	 * @return
	 * @return int
	 * @date：Oct 10, 2013 2:02:07 PM
	 */
	public int getTotalStsCount(StsCondition queryCondition, String searchType,
			boolean rebuildCache,boolean isAudio) {
		log.info("进入方法：getTotalStsCount(StsCondition queryCondition, String searchType,boolean rebuildCache)。queryCondition="
				+ queryCondition
				+ ",rebuildCache="
				+ rebuildCache
				+ ",searchType=" + searchType);
		String key = "QuerySts" + queryCondition.toString().hashCode()+isAudio;
		Integer cnt = null;
		if (rebuildCache) {
			log.info("需要删除缓存的符合条件的查询话务数据数量，重新生成。");
			try {
//				memCached.delete(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
//				cnt = memCached.get(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (cnt == null) {
			if ("CITY_QUALITY".equals(searchType)) {
				cnt = this.rnoStsDao.getTotalQueryStsByCityQua(queryCondition);// 城市网络质量指标查询数据
			} else {
				cnt = this.rnoStsDao
						.getTotalQueryStsByCellVideoOrData(queryCondition,isAudio);// 小区语音业务指标或小区数据业务指标获取数据
			}
			try {
				/*memCached.set(key,
						RnoConstant.TimeConstant.CellQueryResultTotalCountTime,
						cnt);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}

	/**
	 * 得到用户权限内的省份数据集合
	 * 
	 * @return　areaList
	 */
	public List<Map<String, Object>> getUserSpecLevelProvinces(long[] area_ids,
			String area_level) {
		/*
		 * String area="\"LEVEL\""; String sql =
		 * "SELECT * from AREA WHERE "+area+"='"+areaLevel+"'";
		 * List<Map<String,Object>> areaList =
		 * (List<Map<String,Object>>)sysAreaDao.executeSqlForObject(sql); return
		 * areaList;
		 */
		log.info("进入方法：getUserSpecLevelProvinces(long[] area_ids,String area_level)。area_ids="
				+ area_ids + ",area_level=" + area_level);
		if (area_ids.length == 0) {
			return Collections.EMPTY_LIST;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < area_ids.length; i++) {
			buf.append(area_ids[i] + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		String sql = "select distinct area_id,name,area_level,path,longitude,latitude,parent_id from sys_area a  start with a.area_id in "
				+ buf.toString()
				+ " connect by a.area_id=prior a.parent_id and area_level='"
				+ area_level + "'";
		// System.out.println(sql);
		// select a.* from sys_area a start with a.area_id in
		// (88,187,109,317,318) connect by a.area_id=prior　a.parent_id and
		// area_level='省'
		List<Map<String, Object>> areaList = (List<Map<String, Object>>) sysAreaDao
				.executeSqlForObject(sql);
		log.info("退出getUserSpecLevelProvinces。 返回省份areaList：" + areaList);
		return areaList;
	}

	/**
	 * 得到用户权限内的市区数据集合
	 * 
	 * @return　areaList
	 */
	public List<Map<String, Object>> getUserSpecLevelCityAreas(long[] area_ids,
			String area_level, long area_id) {
		/*
		 * String area="\"LEVEL\""; String sql =
		 * "SELECT * from AREA WHERE "+area+"='"+areaLevel+"'";
		 * List<Map<String,Object>> areaList =
		 * (List<Map<String,Object>>)sysAreaDao.executeSqlForObject(sql); return
		 * areaList;
		 */
		log.info("进入方法：getUserSpecLevelCityAreas(long[] area_ids,String area_level,long area_id)。area_ids="
				+ area_ids + ",area_level=" + area_level + ",area_id" + area_id);
		if (area_ids.length == 0) {
			return Collections.EMPTY_LIST;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < area_ids.length; i++) {
			buf.append(area_ids[i] + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		String sql = "select b.* from (select  a.* from sys_area a start with a.area_id in ("
				+ area_id
				+ ") connect by prior  a.area_id= a.parent_id and area_level='"
				+ area_level
				+ "' AND AREA_ID　in "
				+ buf.toString()
				+ ") b  where area_level='" + area_level + "'";
		List<Map<String, Object>> areaList = (List<Map<String, Object>>) sysAreaDao
				.executeSqlForObject(sql);
		log.info("退出getUserSpecLevelCityAreas。 返回市区areaList：" + areaList);
		return areaList;
	}

	/**
	 * 得到用户权限内的县/区数据集合
	 * 
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author chao.xj 2013-10-15下午02:45:00
	 */
	public List<Map<String, Object>> getUserSpecLevelCountys(long[] area_ids,
			String area_level) {
		/*
		 * String area="\"LEVEL\""; String sql =
		 * "SELECT * from AREA WHERE "+area+"='"+areaLevel+"'";
		 * List<Map<String,Object>> areaList =
		 * (List<Map<String,Object>>)sysAreaDao.executeSqlForObject(sql); return
		 * areaList;
		 */
		log.info("进入方法：getUserSpecLevelCountys(long[] area_ids,String area_level)。area_ids="
				+ area_ids + ",area_level=" + area_level);
		if (area_ids.length == 0) {
			return Collections.EMPTY_LIST;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < area_ids.length; i++) {
			buf.append(area_ids[i] + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		String sql = "select distinct area_id,name,area_level,path,longitude,latitude,parent_id from sys_area a  start with a.area_id in "
				+ buf.toString()
				+ " connect by prior a.area_id=a.parent_id and area_level='"
				+ area_level + "'";
		// System.out.println(sql);
		// select a.* from sys_area a start with a.area_id in
		// (88,187,109,317,318) connect by a.area_id=prior　a.parent_id and
		// area_level='省'
		List<Map<String, Object>> areaList = (List<Map<String, Object>>) sysAreaDao
				.executeSqlForObject(sql);
		log.info("退出getUserSpecLevelCountys。 返回县区areaList：" + areaList);
		return areaList;
	}

	/**
	 * 获取用户可访问的指定级别的省份区域
	 */
	public List<Area> getProvincesInSpecLevelListByUserId(String accountId,
			String areaLevel) {

		log.info("进入方法：getProvincesInSpecLevelListByUserId(String accountId,String areaLevel)。accountId="
				+ accountId + ",areaLevel=" + areaLevel);
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
			// System.out.println(usea.get("AREA_ID").toString());
			// System.out.println(usea.get("NAME").toString());
		}

		// String area_level = "区/县";
		List<Map<String, Object>> user_area_in_spec_level_list = this
				.getUserSpecLevelProvinces(areaIds, areaLevel);
		List<Area> result = new ArrayList<Area>();
		Area area;
		for (Map<String, Object> one : user_area_in_spec_level_list) {
			area = Area.fromMap(one);
			if (area != null) {
				if (areaLevel.equals(area.getArea_level())) {
					// System.out.println("\r\n");
					// System.out.println(area.getName());
					result.add(area);
				}
			}
		}

		//
		// System.out.println("gisfindAreaListByUserId result==="
		// + result_user_area_list);
		// log.info("退出 gisfindAreaListByUserId 。 返回 =" +
		// result_user_area_list);
		log.info("退出getProvincesInSpecLevelListByUserId。 返回县区area：" + result);
		return result;
	}

	/**
	 * 获取用户可访问的指定级别的市区区域
	 */
	public String getCityAreasInSpecLevelListByUserId(String accountId,
			String areaLevel, long areaId) {

		log.info("进入方法：getCityAreasInSpecLevelListByUserId(String accountId,String areaLevel,long areaId)。accountId="
				+ accountId + ",areaLevel=" + areaLevel + ",areaId" + areaId);
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
		List<Map<String, Object>> user_area_in_spec_level_list = this
				.getUserSpecLevelCityAreas(areaIds, areaLevel, areaId);
		int k = 0;
		int j = user_area_in_spec_level_list.size();
		String result = "[";
		for (Map<String, Object> usea : user_area_in_spec_level_list) {
			long area = Long.parseLong(usea.get("AREA_ID").toString());
			// areaIds[i++] = Long.parseLong(usea.get("AREA_ID").toString());
			String AREA_ID = usea.get("AREA_ID").toString();
			String NAME = usea.get("NAME").toString();
			if (k == j - 1) {
				result += "{'" + AREA_ID + "':'" + NAME + "'}";
			} else {
				result += "{'" + AREA_ID + "':'" + NAME + "'},";
			}
			k++;
		}
		result += "]";
		log.info("退出getCityAreasInSpecLevelListByUserId。 返回市区JSON：" + result);
		return result;
	}

	/**
	 * 获取用户可访问的指定级别的县/区区域
	 */
	public List<Area> getCountysInSpecLevelListByUserId(String accountId,
			String areaLevel) {
		log.info("进入方法：getCountysInSpecLevelListByUserId(String accountId,String areaLevel)。accountId="
				+ accountId + ",areaLevel=" + areaLevel);

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
			// System.out.println(usea.get("AREA_ID").toString());
			// System.out.println(usea.get("NAME").toString());
		}

		// String area_level = "区/县";
		List<Map<String, Object>> user_area_in_spec_level_list = this
				.getUserSpecLevelProvinces(areaIds, areaLevel);
		List<Area> result = new ArrayList<Area>();
		Area area;
		for (Map<String, Object> one : user_area_in_spec_level_list) {
			area = Area.fromMap(one);
			if (area != null) {
				if (areaLevel.equals(area.getArea_level())) {
					// System.out.println(area.getName());
					result.add(area);
				}
			}
		}

		//
		// System.out.println("gisfindAreaListByUserId result==="
		// + result_user_area_list);
		// log.info("退出 gisfindAreaListByUserId 。 返回 =" +
		// result_user_area_list);
		log.info("退出getCountysInSpecLevelListByUserId。 返回县区area：" + result);
		return result;
	}

	/**
	 * 传入城市质量指标对象保存
	 */
	public void saveRnoCityQuality(RnoCityQuality rnoCityQuality) {
		// TODO Auto-generated method stub
		this.rnoTrafficStaticsDao.saveRnoCityQuality(rnoCityQuality);
	}

	/**
	 * 传入城市质量指标详情对象保存
	 */
	public void saveRnoCityQulDetail(RnoCityqulDetail rnoCityqulDetail) {
		// TODO Auto-generated method stub
		this.rnoTrafficStaticsDao.saveRnoCityQulDetail(rnoCityqulDetail);
	}

	/**
	 * 获取选定的分析列表的小区的gis信息
	 * 
	 * @param selConfigs
	 *            选定的分析列表
	 * @param page
	 *            分页参数
	 * @return
	 * @author brightming 2013-10-11 下午5:33:38
	 */
	public GisCellQueryResult getGisCellInfoFromSelectionList(
			List<StsConfig> selConfigs, Page page) {
		log.info("进入getGisCellInfoFromSelectionList。 selConfigs=" + selConfigs
				+ ",page=" + page);

		GisCellQueryResult result = new GisCellQueryResult();
		result.setTotalCnt(0);
		List<RnoGisCell> gisCells = null;
		if (selConfigs == null || selConfigs.isEmpty()) {
			return result;
		}


		Collections.sort(selConfigs, stsConfigCompartor);
		String key = RnoConstant.CacheConstant.STATICS_GISCELL_IN_SELECTION_LIST_PRE;
		for (StsConfig sc : selConfigs) {
			key += sc.getConfigId() + "_";
		}
		// 看缓存是否有
		try {
			gisCells = memCached.get(key);
			log.info("从缓存获取得到的话务统计待分析列表的小区数量："
					+ (gisCells == null ? gisCells : gisCells.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (gisCells == null) {
			// 从数据库加载
			gisCells = rnoTrafficStaticsDao
					.getGisCellInfoFromSelectionList(selConfigs);
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
		log.info("退出getGisCellInfoFromSelectionList。 返回：" + gisCells);

		return result;
	}

	/**
	 * 统计指定类型的利用率或数量
	 * 
	 * @param stsCode
	 * @param selConfigs
	 * @return
	 * @author brightming 2013-10-14 上午11:09:38
	 */
	public List<RnoStsResult> staticsResourceUtilizationRateInSelList(
			String stsCode, List<StsConfig> selConfigs) {
		log.info("进入staticsResourceUtilizationRateInSelList。 selConfigs="
				+ selConfigs + ",stsCode=" + stsCode);

		if (selConfigs == null || selConfigs.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		if (stsCode == null || stsCode.trim().isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Collections.sort(selConfigs, stsConfigCompartor);
		String key = RnoConstant.CacheConstant.STATICS_STSCODE_PRE + stsCode;
		for (StsConfig sc : selConfigs) {
			key += sc.getConfigId() + "_";
		}

		List<RnoStsResult> stsResults = null;
		try {
			stsResults = memCached.get(key);
			log.info("从缓存获取得到的" + stsCode + "的话务统计结果："
					+ (stsResults == null ? stsResults : stsResults.size()));
//			System.out.println("从缓存获取得到的" + stsCode + "的话务统计结果："
//					+ (stsResults == null ? stsResults : stsResults.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (stsResults == null) {
			// 从数据库统计
			String fieldName = getStsFieldNameByStsCode(stsCode);
			if (fieldName == null) {
				log.error("系统不支持对stsCode=[" + stsCode + "]的统计！");
				return null;
			}
			stsResults = rnoTrafficStaticsDao.stsSpecFieldInSelConfig(
					fieldName, selConfigs);
			try {
				memCached
						.set(key,
								RnoConstant.TimeConstant.TRAFFICSTSSELCONFIGURESTSCELLTIME,
								stsResults);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return stsResults;
	}

	/**
	 * 通过编码获取统计
	 * 
	 * TODO:转移到配置文件
	 * 
	 * @param stsCode
	 * @return
	 * @author brightming 2013-10-14 上午11:25:59
	 */
	private String getStsFieldNameByStsCode(String stsCode) {
		log.info("进入方法： getStsFieldNameByStsCode。 stsCode=" + stsCode);
		if (stsCode == null || stsCode.isEmpty()) {
			return null;
		}
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(this.getClass()
					.getResource("/properties/rnoStsCodeToField.properties")
					.getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		String fieldName = prop.getProperty(stsCode);
		log.info("退出方法 : getStsFieldNameByStsCode. 返回： " + fieldName);
//		System.out.println("退出方法 : getStsFieldNameByStsCode. 返回： " + fieldName);
		return fieldName;
	}

	/**
	 * 获取某类型的小区
	 * 
	 * @param cellType
	 * @param selConfigs
	 * @return
	 * @author brightming 2013-10-16 下午4:17:34
	 */
	public List<RnoStsResult> staticsSpecialCellInSelList(String cellType,
			List<StsConfig> selConfigs) {
		log.info("进入staticsSpecialCellInSelList。 selConfigs=" + selConfigs
				+ ",cellType=" + cellType);

		if (selConfigs == null || selConfigs.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		if (cellType == null || cellType.trim().isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		Collections.sort(selConfigs, stsConfigCompartor);
		String key = RnoConstant.CacheConstant.STATICS_SPECIALINDEXCELLTYPE_PRE
				+ cellType;
		for (StsConfig sc : selConfigs) {
			key += sc.getConfigId() + "_";
		}

		List<RnoStsResult> stsResults = null;
		try {
			stsResults = memCached.get(key);
			log.info("从缓存获取得到的" + cellType + "的话务统计结果："
					+ (stsResults == null ? stsResults : stsResults.size()));
//			System.out.println("从缓存获取得到的" + cellType + "的话务统计结果："
//					+ (stsResults == null ? stsResults : stsResults.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (stsResults == null) {
			// 从数据库统计
			stsResults = rnoTrafficStaticsDao.selectSpecialCellInSelConfig(
					cellType, selConfigs);
			try {
				memCached
						.set(key,
								RnoConstant.TimeConstant.TRAFFICSTSSELCONFIGURESTSCELLTIME,
								stsResults);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return stsResults;
	}

	/**
	 * 获取指定区域的忙小区
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2013-10-21 下午2:29:13
	 */
	public List<RnoStsResult> staticsHeavyLoadCellInArea(long areaId) {
		log.info("进入方法：staticsHeavyLoadCellInArea。 areaId=" + areaId);

		if (areaId <= 0) {
			return Collections.EMPTY_LIST;
		}
		List<RnoStsResult> sts = null;
//		String key = RnoConstant.CacheConstant.STATICS_HEAVYLOAD_CELL_IN_AREA_PRE
//				+ areaId;
//		try {
//			sts = memCached.get(key);
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (MemcachedException e) {
//			e.printStackTrace();
//		}
		if (sts == null) {
			sts = rnoTrafficStaticsDao.staticsHeavyLoadCellWithinArea(areaId);
//			try {
//				memCached
//						.set(key,
//								RnoConstant.TimeConstant.TAFFFICSTATICSHEAVYLOADCELLINAREATIME,
//								sts);
//			} catch (TimeoutException e) {
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (MemcachedException e) {
//				e.printStackTrace();
//			}
		}

		return sts;
	}

	/**
	 * 统计某指定小区的邻区的情况
	 * 
	 * @param cell
	 * @param areaId
	 * @return
	 * @author brightming 2013-10-21 下午2:42:39
	 */
	public List<RnoStsResult> staticsSpecialCellsNcellStsInfo(String cell,
			long areaId) {
		log.info("进入方法：staticsSpecialCellsNcellStsInfo。 cell=" + cell
				+ ",areaId=" + areaId);
		List<RnoStsResult> sts = rnoTrafficStaticsDao
				.staticsSpecialCellsNcellStsInfo(cell, areaId);
		return sts;

	}
	
	/**
	 * 查询符合条件的话务统计时间段数据
	 * @param page
	 * @param searchType
	 * @param queryCondition
	 * @return
	 * @author brightming
	 * 2013-10-24 下午3:14:32
	 */
	public List<StsConfig> queryStsPeriodByQueryCondition(Page page,
			String searchType, StsCondition queryCondition){
		log.info("进入 service  queryStsPeriodByQueryCondition。page="+page+",searchType="+searchType+",queryCondition="+queryCondition);
		return rnoTrafficStaticsDao.queryStsConfigByCondition(searchType, queryCondition);
	}
	/**
	 * 
	 * @description:获取 导出查询话务数据 输入流 
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return InputStream     
	 * @date：Oct 29, 2013 9:43:28 AM
	 */
	public InputStream exportQueryRnoStsList(StsCondition queryCondition,boolean isAudio){
		log.info("进入InputStream exportQueryRnoStsList(StsCondition queryCondition),queryCondition="+queryCondition);
		List<Map<String,Object>> rnoStsList = this.rnoStsDao.queryStsByVideoOrDataCondition(queryCondition,isAudio);
		String[] titleArray = {"DATE","PERIOD","BSC",
								"CELL","小区","T完好率",
								"定义信道","可用信道","载波数",
								"无线资源利用率","话务量","每线话务量",
								"接通率","无线接入性","总掉话",
								"无线掉话率(不含切换)","干扰系数","切出成功率",
								"切入成功率","切换成功率","PS无线利用率",
								"PDCH承载效率（kbps）","数据流量(MByte)","下行BPDCH复用度",
								"下行EPDCH复用度","下行TBF拥塞率"};//标题列
		String[] keyArray =  {"STS_DATE","STS_PERIOD","ENGNAME",
								"CELL","CELL_CHINESE_NAME","TCH_INTACT_RATE",
								"DECLARE_CHANNEL_NUMBER","AVAILABLE_CHANNEL_NUMBER","CARRIER_NUMBER",
								"RESOURCE_USE_RATE","TRAFFIC","TRAFFIC_PER_LINE",
								"ACCESS_OK_RATE","RADIO_ACCESS","DROP_CALL_NUM_TOGETHER",
								"RADIO_DROP_RATE_NO_HV","ICM","HANDOUT_SUC_RATE",
								"HANDIN_SUC_RATE","HANDOVER_SUC_RATE","PS_RADIO_USE_RATE",
								"PDCH_CARRYING_EFFICIENCY","DATA_TRAFFIC","DOWNLINK_BPDCH_REUSE",
								"DOWNLINK_EPDCH_REUSE","DOWNLINK_TBF_CONG_RATE"};//数据 map key 属性 
		ByteArrayOutputStream out = this.putDataOnExcelOutputStream(titleArray,keyArray,rnoStsList); //导出文件输出流
		return new ByteArrayInputStream(out.toByteArray());
	}
	/**
	 * 
	 * @description: 生成导出文件流
	 * @author：yuan.yw
	 * @param titleArray 表头标题列
	 * @param keyArray 属性字段key
	 * @param dataList 数据List
	 * @return     
	 * @return ByteArrayOutputStream     
	 * @date：Oct 29, 2013 11:07:15 AM
	 */
	private ByteArrayOutputStream putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList) {  
		  log.info("进入putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流。");
		  ByteArrayOutputStream os =  new ByteArrayOutputStream();
		  try {   
	    	//XSSFWorkbook wb = new XSSFWorkbook();//创建工作簿
	        //SXSSFWorkbook  
	    	Workbook wb = new SXSSFWorkbook(100);
			Sheet sheet = wb.createSheet("sheet0");//创建excel sheet 
				
			//创建一个样式
			CellStyle style = wb.createCellStyle();
			//居中对齐
			style.setAlignment(CellStyle.ALIGN_CENTER );
			//设定单元格背景颜色
			style.setFillForegroundColor(HSSFColor.AQUA.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND); 

		   //设置表头的内容
		   if(titleArray!=null && titleArray.length>0){
			   int length = titleArray.length;
			   Row row = sheet.createRow(0);		//sheet 创建一行
			   Cell cell=null;
			   for(int j=0;j<length;j++)
			   {   
				   cell = row.createCell(j);  //sheet 创建一列 
				   cell.setCellValue(titleArray[j]);//列设值
				   cell.setCellStyle(style); //列样式
			   }
			 //输出查询出的数据
			   if(dataList!=null && !dataList.isEmpty() && keyArray!=null && keyArray.length>0){
				   int keyArrayLength = keyArray.length;
				   int dataListSize = dataList.size();
				   for(int i=0;i<dataListSize;i++){//每行数据
					  row = sheet.createRow(i+1);		//sheet 创建一行 
					  Map<String,Object> map = dataList.get(i);
					  for(int j=0;j<keyArrayLength;j++){
						  String curValue = map.get(keyArray[j])+"";
						  cell = row.createCell(j);  //sheet 创建一列 
						  cell.setCellValue(curValue);//列设值
					  }
				   }
			   }
		   }
			  wb.write(os); 
			  os.flush();
			  os.close();
			  log.info("退出putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流成功");
	      } catch (Exception e) {   
	    	  e.printStackTrace();
	    	  log.error("退出putDataOnExcelOutputStream(String[] titleArray,String[] keyArray,List<Map<String, Object>> dataList)，生成excel文件流失败，exception："+e.getMessage());   
	      }   
	      
	      return os;
	}
	/**
	 * 通过RNO_STS表中的BSC_ID获取RNO_BSC的ENGNAME
	 * @return
	 * @author chao.xj
	 * @date 2013-11-4下午02:00:59
	 */
	public List<Map<String, Object>> getRnoBscEngName(){
		
		return rnoTrafficStaticsDao.getRnoBscEngName();
	}
	/**
	 * 改造：查询小区语音或数据业务话统通过分页方式
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午07:03:17
	 */
	public List<Map<String,Object>> queryCellAudioOrDataStsByPage(final Page page, final StsCondition queryCondition,final String searchType)
	{
		
		return rnoTrafficStaticsDao.queryCellAudioOrDataStsByPage(page, queryCondition, searchType);
	}
	/**
	 * 改造：获取符合条件的小区语音或小区数据的数量
	 * @param searchType
	 * @param queryCondition
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午06:18:25
	 */
	public int getTotalCellAudioOrDataCount(final String searchType,
			final StsCondition queryCondition)
	{
		return rnoTrafficStaticsDao.getTotalCellAudioOrDataCount(searchType, queryCondition);
	}
	/**
	 * 改造：通过小区语音和数据话统配置ID的字符串获取话统指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellAudioOrDataDescByConfigIds(final String configIds)
	{
		return rnoTrafficStaticsDao.getCellAudioOrDataDescByConfigIds(configIds);
	}
	/**
	 * 通过小区查询邻区
	 * @param cell
	 * @return
	 * @author chao.xj
	 * @date 2013-12-26下午03:16:28
	 */
	public List<Map<String,Object>> queryNcellByCell(final String cell){
		
		return rnoTrafficStaticsDao.queryNcellByCell(cell);
	}
	/**
	 * 获取 导出查询话统指标数据 输入流 
	 * @param queryCondition
	 * @param isAudio
	 * @param rptTemplateId 通过模板ID判断缺省还是自主定义选择
	 * @return
	 * @author chao.xj
	 * @date 2014-1-9上午09:42:29
	 */
	public InputStream exportQueryRnoStsList(StsCondition queryCondition,boolean isAudio,long rptTemplateId){
		log.info("进入InputStream exportQueryRnoStsList(StsCondition queryCondition),queryCondition="+queryCondition+" isAudio:"+isAudio+" rptTemplateId:"+rptTemplateId);
		String[] titleArray = null;
		String[] keyArray = null;
		List<Map<String,Object>> rnoStsList = new ArrayList<Map<String, Object>>();
		if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()) || "max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
		{
			List<String> statisticTitleList = new ArrayList<String>();
			List<String> statisticFieldList = new ArrayList<String>();
			rnoStsList = rnoReportDao.getRnoStsResultList(queryCondition,isAudio,rptTemplateId,statisticFieldList,statisticTitleList);
			log.info("rnoStsList: "+rnoStsList);
			int n=statisticTitleList.size();
			log.info("titleArray.length:"+n);
			titleArray=(String[])statisticTitleList.toArray(new String[n]);
			keyArray=(String[])statisticFieldList.toArray(new String[n]);
		}else
		{	
			rnoStsList = this.rnoStsDao.queryStsByVideoOrDataCondition(queryCondition,isAudio);
			String[] defaultTitleArray = {"DATE","PERIOD","BSC",
					"CELL","小区","T完好率",
					"定义信道","可用信道","载波数",
					"无线资源利用率","话务量","每线话务量",
					"接通率","无线接入性","总掉话",
					"无线掉话率(不含切换)","干扰系数","切出成功率",
					"切入成功率","切换成功率","PS无线利用率",
					"PDCH承载效率（kbps）","数据流量(MByte)","下行BPDCH复用度",
					"下行EPDCH复用度","下行TBF拥塞率"};//标题列
			String[] defaultKeyArray =  {"STS_DATE","STS_PERIOD","ENGNAME",
					"CELL","CELL_CHINESE_NAME","TCH_INTACT_RATE",
					"DECLARE_CHANNEL_NUMBER","AVAILABLE_CHANNEL_NUMBER","CARRIER_NUMBER",
					"RESOURCE_USE_RATE","TRAFFIC","TRAFFIC_PER_LINE",
					"ACCESS_OK_RATE","RADIO_ACCESS","DROP_CALL_NUM_TOGETHER",
					"RADIO_DROP_RATE_NO_HV","ICM","HANDOUT_SUC_RATE",
					"HANDIN_SUC_RATE","HANDOVER_SUC_RATE","PS_RADIO_USE_RATE",
					"PDCH_CARRYING_EFFICIENCY","DATA_TRAFFIC","DOWNLINK_BPDCH_REUSE",
					"DOWNLINK_EPDCH_REUSE","DOWNLINK_TBF_CONG_RATE"};//数据 map key 属性 
			
			if (rptTemplateId<=0) {
				titleArray =defaultTitleArray;
				keyArray =defaultKeyArray;
			}else {
				List titleList=new ArrayList();
				titleList.add("DATE");
				titleList.add("PERIOD");
				titleList.add("BSC");
				titleList.add("CELL");
				List keyList=new ArrayList();
				keyList.add("STS_DATE");
				keyList.add("STS_PERIOD");
				keyList.add("ENGNAME");
				keyList.add("CELL");
				//titleArray=new String[]{"DATE","PERIOD","BSC","CELL"};
				//keyArray=new String[]{"STS_DATE","STS_PERIOD","ENGNAME","CELL"};
				
//			RnoReportDao rnoReportDao=new RnoReportDaoImpl();
//			log.info("创建rnoReportDao实例:"+rnoReportDao);
				List<Map<String, Object>> rpttemplateLists=rnoReportDao.getStsRptTemplateFiledByTemplateId(rptTemplateId);
				for (int i = 0; i < rpttemplateLists.size(); i++) {
					titleList.add(rpttemplateLists.get(i).get("DISPLAY_NAME").toString());
					keyList.add(rpttemplateLists.get(i).get("TABLE_FIELDS").toString());
				}
				int n=titleList.size();
				log.info("titleArray.length:"+n);
				titleArray=(String[])titleList.toArray(new String[n]);
				keyArray=(String[])keyList.toArray(new String[n]);
			}
		}
		ByteArrayOutputStream out = this.putDataOnExcelOutputStream(titleArray,keyArray,rnoStsList); //导出文件输出流
		return new ByteArrayInputStream(out.toByteArray());
	}
	


}
