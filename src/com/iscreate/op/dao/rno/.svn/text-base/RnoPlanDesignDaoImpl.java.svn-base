package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import bsh.Console;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.NcellAnalysisCondition;
import com.iscreate.op.pojo.rno.PlanConfig;
import com.iscreate.op.pojo.rno.RedundantNCell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoCellStructDesc;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoHandover;
import com.iscreate.op.pojo.rno.RnoHandoverDescriptor;
import com.iscreate.op.pojo.rno.RnoInterferenceDescriptor;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.pojo.rno.RnoNcsDescriptorWrap;
import com.iscreate.op.pojo.system.SysArea;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.sun.istack.internal.FinalArrayList;
import com.sun.org.apache.xpath.internal.operations.And;

public class RnoPlanDesignDaoImpl implements RnoPlanDesignDao {

	private static Log log = LogFactory.getLog(RnoPlanDesignDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;
	
	private RnoStructureAnalysisDao rnoStructureAnalysisDao;

	
	public void setRnoStructureAnalysisDao(
			RnoStructureAnalysisDao rnoStructureAnalysisDao)
	{
		this.rnoStructureAnalysisDao = rnoStructureAnalysisDao;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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
			final long areaId) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoCellDescriptor>>() {
					public List<RnoCellDescriptor> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT * from RNO_CELL_DESCRIPTOR WHERE area_id=? AND name='系统默认配置方案'";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, areaId);
						query.addEntity(RnoCellDescriptor.class);
						return query.list();
					}
				});
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
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoCellDescriptor>>() {
					public List<RnoCellDescriptor> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT * from RNO_CELL_DESCRIPTOR WHERE area_id=? AND temp_storage='Y'";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, areaId);
						query.addEntity(RnoCellDescriptor.class);
						return query.list();
					}
				});
	}

	/*
	 * public List<String> getTempAnalyseSchemeFromRnoCellDesc(final long
	 * areaId){ List<String> names = (List<String>) hibernateTemplate
	 * .executeFind(new HibernateCallback<List<String>>() { public List<String>
	 * doInHibernate(Session arg0) throws HibernateException, SQLException {
	 * //System.out.println(areaId); List<String> names = new
	 * ArrayList<String>(); SQLQuery query = arg0 .createSQLQuery(
	 * "SELECT cell_descriptor_id,(SELECT NAME from AREA WHERE id=?)||NAME from RNO_CELL_DESCRIPTOR WHERE area_id=? AND temp_storage='Y'"
	 * ); query.setLong(0, areaId); query.setLong(1, areaId); List<Object>
	 * objects = query.list(); if (objects == null || objects.size() == 0) {
	 * 
	 * return names; }
	 * 
	 * for (Object o : objects) { names.add( o.toString()); }
	 * 
	 * return names; } }); return names;
	 * 
	 * }
	 */

	/**
	 * 
	 * @description: 统计指定区域范围小区的频率复用情况
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return
	 * @return List<Map<String,Object>>
	 * @date：Nov 7, 2013 11:59:55 AM
	 */
	public List<Map<String, Object>> staticsFreqReuseInfoInArea(
			final PlanConfig selectConfig) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						boolean isTemp = selectConfig.isTemp();
						log.info("isTemp:"+isTemp);
						String cellTable = "cell";
						if (isTemp) {
							cellTable = "rno_temp_cell";// 临时配置小区
						}
						long configId = selectConfig.getConfigId();
						/*String sql = "select c.bcch,c.tch "
								+ " from "
								+ cellTable
								+ " c, "
								+ "       (select sa.area_id "
								+ "         from sys_area sa "
								+ "        connect by prior sa.area_id = sa.parent_id "
								+ "         start with sa.area_id in (select rcd.area_id from rno_cell_descriptor rcd where rcd.cell_descriptor_id=?)) a "
								+ "where c.area_id=a.area_id ";*/
						String sql = "select bcch,tch "
							+ " from "
							+ cellTable 
							+" WHERE CELL_desc_id=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, configId);
						log.info("sql:"+sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 
	 * @description: 根据类型获取选择的小区配置或小区干扰中分析列表中的小区的gis信息
	 * @author：yuan.yw
	 * @param selectConfig
	 * @return
	 * @return List<RnoGisCell>
	 * @date：Nov 8, 2013 10:08:37 AM
	 */
	public List<RnoGisCell> getFreqReuseCellGisInfoFromSelectionList(
			final PlanConfig selectConfig) {
		log.info("进入getFreqReuseCellGisInfoFromSelectionList,selectConfig="
				+ selectConfig);
		if (selectConfig == null) {
			return Collections.EMPTY_LIST;
		}

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<RnoGisCell> gisCells = null;
						boolean isTemp = selectConfig.isTemp();
						log.info("isTemp:"+isTemp);
						String cellTable = "cell";
						if (isTemp) {
							cellTable = "rno_temp_cell";// 临时配置小区
						}
						long configId = selectConfig.getConfigId();
					/*	String sql = "select cell.label               as cell,"
								+ " cell.name                as chineseName,"
								+ " cell.LONGITUDE           as lng,"
								+ "  cell.LATITUDE            as lat,"
								+ "  cell.LNGLATS             as allLngLats,"
								+ "  cell.BEARING             as azimuth,"
								+ "  cell.GSMFREQUENCESECTION as freqType,"
								+ "  cell.SITE                as site,"
								+ " cell.LAC                 as lac,"
								+ "  cell.CI                  as ci,"
								+ "  cell.BCCH                as bcch,"
								+ "  cell.TCH                 as tch"
								+ " from "
								+ cellTable
								+ " cell"
								+ " inner join ("
								+ " select sa.area_id"
								+ " from sys_area sa "
								+ " connect by prior sa.area_id=sa.parent_id "
								+ " start with sa.area_id"
								+ " in"
								+ " (select rcd.area_id from rno_cell_descriptor rcd where rcd.cell_descriptor_id=?)"
								+ " ) area on area.area_id =  cell.area_id";*/
						String sql = "select cell.label               as cell,"
							+ " cell.name                as chineseName,"
							+ " cell.LONGITUDE           as lng,"
							+ "  cell.LATITUDE            as lat,"
							+ "  cell.LNGLATS             as allLngLats,"
							+ "  cell.BEARING             as azimuth,"
							+ "  cell.GSMFREQUENCESECTION as freqType,"
							+ "  cell.SITE                as site,"
							+ " cell.LAC                 as lac,"
							+ "  cell.CI                  as ci,"
							+ "  cell.BCCH                as bcch,"
							+ "  cell.TCH                 as tch"
							+ " from "
							+ cellTable
							+ " cell"
							+" WHERE CELL_desc_id=?";
						log.info("获取选定的分析列表的小区的gis信息的sql：" + sql);
						// System.out.println(sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, configId);
						query.addEntity(RnoGisCell.class);
						gisCells = query.list();
						log.info("getFreqReuseCellGisInfoFromSelectionList获取的结果："
								+ gisCells == null ? 0 : gisCells.size());

						return gisCells;
					}
				});
	}

	/**
	 * 获取区域下的干扰配置
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2013-11-7 下午6:38:34
	 */
	public List<RnoInterferenceDescriptor> getRnoInterferenceDescriptorInArea(
			final long areaId) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoInterferenceDescriptor>>() {

					public List<RnoInterferenceDescriptor> doInHibernate(
							Session arg0) throws HibernateException,
							SQLException {
						Criteria criteria = arg0
								.createCriteria(RnoInterferenceDescriptor.class);
						criteria.add(Restrictions.eq("areaId", areaId));
						return criteria.list();
					}
				});
	}

	/**
	 * 根据id获取RnoInterferenceDescriptor
	 * 
	 * @param id
	 * @return
	 * @author brightming 2013-11-7 下午8:02:26
	 */
	public RnoInterferenceDescriptor getRnoInterferenceDescriptorById(long id) {
		return hibernateTemplate.get(RnoInterferenceDescriptor.class, id);
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
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoCellDescriptor>>() {
					public List<RnoCellDescriptor> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT * from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, configId);
						query.addEntity(RnoCellDescriptor.class);
						return query.list();
					}
				});
	}

	/**
	 * 根据areaid,tempname获取临时方案名称
	 * 
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-6上午09:26:34
	 */
	public List<RnoCellDescriptor> getTempAnalyseSchemeFromRnoCellDesc(
			final long areaId, final String tempname) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoCellDescriptor>>() {
					public List<RnoCellDescriptor> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT * from RNO_CELL_DESCRIPTOR WHERE area_id=? AND name=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, areaId);
						query.setString(1, tempname);
						query.addEntity(RnoCellDescriptor.class);
						return query.list();
					}
				});
	}

	/**
	 * 通过小区ID查询一条小区数据
	 */
	public SysArea queryAreaById(final long id) {
		return hibernateTemplate.get(SysArea.class, id);
	}

	/**
	 * 获取指定区域下的所有临时小区名
	 */
	public List<String> getTempCellNameByAreaId(final Long areaId) {
		List<String> names = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<String> names = new ArrayList<String>();
						SQLQuery query = arg0
								.createSQLQuery("SELECT distinct(label) from RNO_TEMP_CELL WHERE AREA_ID=?");
						query.setLong(0, areaId);
						List<Object> objects = query.list();
						if (objects == null || objects.size() == 0) {
							return names;
						}

						for (Object o : objects) {
							names.add(o.toString());
						}

						return names;
					}
				});
		return names;
	}

	/**
	 * 通过areaId/configids获得BCCH/BSIC相同，ID和label不同的小区集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param bcch
	 * @param bsic
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16上午10:18:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getCoBsicCellsByAreaId(boolean reSelected,
			String areaIds, String configIds, final int bcch, final String bsic) {
		log
				.info("进入getCoBsicCellsByAreaId　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
						+ reSelected
						+ ":"
						+ areaIds
						+ ":"
						+ configIds
						+ ":"
						+ bcch + ":" + bsic);
		String sqlString = "";
		if (reSelected) {
				sqlString = "SELECT LABEL,BCCH,BSIC "
					+ "FROM "
					+ "(select   distinct c1.LABEL,c1.BCCH,C1.BSIC   "
					+ "from   cell   c1,cell   c2   where c1.id<>c2.id and C1.LABEL<>C2.LABEL   and   c1.bcch   =   c2.bcch   and   c1.bsic   =   c2.bsic and C1.AREA_ID  in(SELECT AREA_ID from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID IN ("
					+ configIds
					+ ")) AND C2.AREA_ID  in(SELECT AREA_ID from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID IN ("
					+ configIds + ")) "
					+ "ORDER BY c1.BCCH,C1.BSIC) WHERE BCCH="+bcch+" and BSIC="+bsic;
		} else {
				sqlString = "SELECT LABEL,BCCH,BSIC "
					+ "FROM "
					+ "(select   distinct c1.LABEL,c1.BCCH,C1.BSIC   "
					+ "from   cell   c1,cell   c2   where c1.id<>c2.id and C1.LABEL<>C2.LABEL   and   c1.bcch   =   c2.bcch   and   c1.bsic   =   c2.bsic and C1.AREA_ID in("
					+ areaIds + ") AND C2.AREA_ID in(" + areaIds + ") "
					+ "ORDER BY c1.BCCH,C1.BSIC) WHERE BCCH="+bcch+" and BSIC="+bsic;
		}
		final String sql = sqlString;
		log.info("sql:"+sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						/*
						 * String sql = "SELECT LABEL,BCCH,BSIC " + "FROM " +
						 * "(select distinct c1.LABEL,c1.BCCH,C1.BSIC " + "from
						 * cell c1,cell c2 where c1.id<>c2.id and C1.LABEL<>C2.LABEL
						 * and c1.bcch = c2.bcch and c1.bsic = c2.bsic and
						 * C1.AREA_ID=? AND C2.AREA_ID=? " + "ORDER BY
						 * c1.BCCH,C1.BSIC) WHERE BCCH=? and BSIC=?";
						 */
						SQLQuery query = arg0.createSQLQuery(sql);
						/*
						 * query.setLong(0, areaId); query.setLong(1, areaId);
						 */
						/*query.setInteger(0, bcch);
						query.setInteger(1, bsic);*/
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出getCoBsicCellsByAreaId");
						return query.list();
					}
				});
	}
	/**
	 * 全网范围内：通过areaId/configids获得BCCH/BSIC相同，ID和label不同的小区集合数据
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param bcch
	 * @param bsic
	 * @return
	 * @author chao.xj
	 * @date 2014-4-16上午10:18:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getCoBsicCellsByAreaId(boolean reSelected,
			String areaIds, String configIds) {
		log
				.info("进入getCoBsicCellsByAreaId　boolean reSelected,String areaIds, String configIds, final int bcch, final int bsic："
						+ reSelected
						+ ":"
						+ areaIds
						+ ":"
						+ configIds);
		String sqlString = "";
		if (reSelected) {
			sqlString = "SELECT LABEL,BCCH,BSIC "
				+ "FROM "
				+ "(select   distinct c1.LABEL,c1.BCCH,C1.BSIC   "
				+ "from   cell   c1,cell   c2   where c1.id<>c2.id and C1.LABEL<>C2.LABEL   and   c1.bcch   =   c2.bcch   and   c1.bsic   =   c2.bsic and C1.AREA_ID  in(SELECT AREA_ID from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID IN ("
				+ configIds
				+ ")) AND C2.AREA_ID  in(SELECT AREA_ID from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID IN ("
				+ configIds + ")) "
				+ "ORDER BY c1.BCCH,C1.BSIC)";
		} else {
			sqlString = "SELECT LABEL,BCCH,BSIC "
				+ "FROM "
				+ "(select   distinct c1.LABEL,c1.BCCH,C1.BSIC   "
				+ "from   cell   c1,cell   c2   where c1.id<>c2.id and C1.LABEL<>C2.LABEL   and   c1.bcch   =   c2.bcch   and   c1.bsic   =   c2.bsic and C1.AREA_ID in("
				+ areaIds + ") AND C2.AREA_ID in(" + areaIds + ") "
				+ "ORDER BY c1.BCCH,C1.BSIC)";
		}
		final String sql = sqlString;
		log.info("sql:"+sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						/*
						 * String sql = "SELECT LABEL,BCCH,BSIC " + "FROM " +
						 * "(select distinct c1.LABEL,c1.BCCH,C1.BSIC " + "from
						 * cell c1,cell c2 where c1.id<>c2.id and C1.LABEL<>C2.LABEL
						 * and c1.bcch = c2.bcch and c1.bsic = c2.bsic and
						 * C1.AREA_ID=? AND C2.AREA_ID=? " + "ORDER BY
						 * c1.BCCH,C1.BSIC) WHERE BCCH=? and BSIC=?";
						 */
						SQLQuery query = arg0.createSQLQuery(sql);
						/*
						 * query.setLong(0, areaId); query.setLong(1, areaId);
						 */
						/*query.setInteger(0, bcch);
						query.setInteger(1, bsic);*/
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出getCoBsicCellsByAreaId");
						return query.list();
					}
				});
	}
	/**
	 * 查找指定区域下，id为参数指定的rnoncsdescriptor列表
	 * 
	 * @param configIds
	 * @param areaIds
	 * @return
	 * @author brightming 2013-11-15 上午11:11:03
	 */
	public List<RnoNcsDescriptor> getRnoNcsDescriptorByIdsWithInAreas(
			final List<Long> configIds, final List<Long> areaIds) {
		try {
			return hibernateTemplate
					.executeFind(new HibernateCallback<List<RnoNcsDescriptor>>() {

						public List<RnoNcsDescriptor> doInHibernate(Session arg0)
								throws HibernateException, SQLException {

							Criteria criteria = arg0
									.createCriteria(RnoNcsDescriptor.class);
							List<Long> cids = configIds;
							if (cids == null) {
								cids = new ArrayList<Long>();
							}
							List<Long> as = areaIds;
							if (as == null) {
								as = new ArrayList<Long>();
							}
							criteria.add(Restrictions.and(
									Restrictions.in("rnoNcsDescId", cids),
									Restrictions.in("areaId", as)));
							return criteria.list();
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 分页查询ncs描述信息
	 * 
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-15 上午11:19:55
	 */
	public List<RnoNcsDescriptorWrap> queryNcsDescriptorByPage(Page page,
			Map<String, Object> attachParams) {
		log.info("进入dao方法：queryNcsDescriptorByPage。page=" + page
				+ ",attachParams=" + attachParams);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return Collections.EMPTY_LIST;
		}

		final int startIndex = (page.getCurrentPage() - 1) * page.getPageSize()
				+ 1;
		final int endIndex = startIndex + page.getPageSize();
		String where = getNcsDescQuerySql("a", attachParams);
		log.info("分页查询 ncs desc的查询条件：where=" + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return Collections.EMPTY_LIST;
		}
		final String sql = "select * from (select a.*,TO_CHAR(a.START_TIME,'YYYY-MM-DD HH24:mi:ss') AS startTimeStr,b.name as areaName ,rownum as rn from RNO_NCS_DESCRIPTOR a ,SYS_AREA b WHERE "
				+ where
				+ " and a.AREA_ID=b.AREA_ID) where rn>="
				+ startIndex
				+ " and rn<" + endIndex;
		log.info("分页查询ncs desc 的查询语句：" + sql);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoNcsDescriptor>>() {

					public List<RnoNcsDescriptor> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoNcsDescriptorWrap.class);

						return query.list();
					}
				});
	}

	/**
	 * 将map转换为查询条件语句
	 * 
	 * @param alias
	 *            表的别名
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-15 上午11:43:16
	 */
	private String getNcsDescQuerySql(String alias,
			Map<String, Object> attachParams) {
		if (alias == null || alias.trim().equals("")) {
			alias = "abcdefg";
		}
		List<String> allowFields = Arrays.asList("AREA_ID", "BEGTIME",
				"ENDTIME", "START_TIME", "RELSSN", "ABSS", "NUMFREQ",
				"RECTIME", "NET_TYPE", "VENDOR", "SEGTIME", "STATUS", "NAME");
		String where = "";
		for (String k : attachParams.keySet()) {
			if (!allowFields.contains(k) || attachParams.get(k) == null
					|| "".equals((attachParams.get(k) + "").trim())) {
				continue;
			}
			if ("BEGTIME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + ".START_TIME>=TO_DATE('"
						+ attachParams.get(k) + "','YYYY-MM-DD HH24:mi:ss') ";
			} else if ("ENDTIME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + ".START_TIME<=TO_DATE('"
						+ attachParams.get(k) + "','YYYY-MM-DD HH24:mi:ss') ";
			} else if ("NAME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + "." + k + " like '%"
						+ attachParams.get(k) + "%' ";
			} else {
				where += " and " + alias + "." + k + "='" + attachParams.get(k)
						+ "' ";
			}
		}
		if ("".equals(where)) {
			log.warn("查询条件无效！");
			return null;
		} else {
			where = where.substring(4);
		}
		return where;
	}

	/**
	 * 查询指定条件的ncs 描述数量
	 * 
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-15 上午11:20:39
	 */
	public int getNcsDescriptorCount(Map<String, Object> attachParams) {
		log.info("进入dao方法：getNcsDescriptorCount.attachParams=" + attachParams);
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return 0;
		}
		String where = getNcsDescQuerySql("a", attachParams);
		log.info("查询满足条件的ncs desc总数量的条件：where=" + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return 0;
		}
		final String sql = "select count(RNO_NCS_DESC_ID) from RNO_NCS_DESCRIPTOR a WHERE "
				+ where;
		log.info("查询满足条件的ncs desc的查询语句：" + sql);

		int cnt = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);

				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}

				return cnt;
			}
		});
		log.info("查询到满足条件的ncs desc的数量：" + cnt);
		return cnt;
	}

	/**
	 * 查找指定区域下，id为参数指定的RnoCellStructDesc列表
	 * 
	 * @param configIds
	 * @param areaIds
	 * @return
	 * @author brightming 2013-11-16 下午12:08:56
	 */
	public List<RnoCellStructDesc> getRnoCellStructDescByIdsWithInAreas(
			final List<Long> configIds, final List<Long> areaIds) {
		try {
			return hibernateTemplate
					.executeFind(new HibernateCallback<List<RnoCellStructDesc>>() {

						public List<RnoCellStructDesc> doInHibernate(
								Session arg0) throws HibernateException,
								SQLException {

							Criteria criteria = arg0
									.createCriteria(RnoCellStructDesc.class);
							List<Long> cids = configIds;
							if (cids == null) {
								cids = new ArrayList<Long>();
							}
							List<Long> as = areaIds;
							if (as == null) {
								as = new ArrayList<Long>();
							}
							criteria.add(Restrictions.and(Restrictions.in(
									"rnoCellStructDescId", cids), Restrictions
									.in("areaId", as)));
							return criteria.list();
						}

					});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 获取符合条件的小区结构指标数量
	 * 
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-16 下午12:15:31
	 */
	public int getCellStructDescriptorCount(Map<String, Object> attachParams) {
		log.info("进入dao方法：getCellStructDescriptorCount.attachParams="
				+ attachParams);
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return 0;
		}
		String where = getCellStructDescQuerySql("a", attachParams);
		log.info("查询满足条件的cell struct desc总数量的条件：where=" + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return 0;
		}
		final String sql = "select count(RNO_CELL_STRUCT_DESC_ID) from RNO_CELL_STRUCT_DESC a WHERE "
				+ where;
		log.info("查询满足条件的cell struct desc的查询语句：" + sql);

		int cnt = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);

				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}

				return cnt;
			}
		});
		log.info("查询到满足条件的cell struct desc的数量：" + cnt);
		return cnt;
	}

	/**
	 * 分页查询符合条件的小区结构指标数据
	 * 
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming 2013-11-16 下午12:15:57
	 */
	public List<RnoCellStructDescWrap> queryCellStructDescriptorByPage(
			Page page, Map<String, Object> attachParams) {
		log.info("进入dao方法：queryCellStructDescriptorByPage。page=" + page
				+ ",attachParams=" + attachParams);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return Collections.EMPTY_LIST;
		}

		final int startIndex = (page.getCurrentPage() - 1) * page.getPageSize()
				+ 1;
		final int endIndex = startIndex + page.getPageSize();
		String where = getCellStructDescQuerySql("a", attachParams);
		log.info("分页查询 ncs desc的查询条件：where=" + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return Collections.EMPTY_LIST;
		}
		final String sql = "select * from (select a.*,TO_CHAR(a.TIME,'YYYY-MM-DD HH24:mi:ss') AS timeStr,b.name as areaName ,rownum as rn from RNO_CELL_STRUCT_DESC a ,SYS_AREA b WHERE "
				+ where
				+ " and a.AREA_ID=b.AREA_ID) where rn>="
				+ startIndex
				+ " and rn<" + endIndex;
		log.info("分页查询小区结构指标 desc 的查询语句：" + sql);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoCellStructDescWrap>>() {

					public List<RnoCellStructDescWrap> doInHibernate(
							Session arg0) throws HibernateException,
							SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoCellStructDescWrap.class);

						return query.list();
					}
				});
	}

	private String getCellStructDescQuerySql(String alias,
			Map<String, Object> attachParams) {
		if (alias == null || alias.trim().equals("")) {
			alias = "abcdefg";
		}
		List<String> allowFields = Arrays.asList("AREA_ID", "BEGTIME",
				"ENDTIME", "TIME", "NAME", "STATUS");
		String where = "";
		for (String k : attachParams.keySet()) {
			if (!allowFields.contains(k) || attachParams.get(k) == null
					|| "".equals((attachParams.get(k) + "").trim())) {
				continue;
			}
			if ("BEGTIME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + ".TIME>=TO_DATE('"
						+ attachParams.get(k) + "','YYYY-MM-DD HH24:mi:ss') ";
			} else if ("ENDTIME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + ".TIME<=TO_DATE('"
						+ attachParams.get(k) + "','YYYY-MM-DD HH24:mi:ss') ";
			} else if ("NAME".equals(k) && attachParams.get(k) != null
					&& !"".equals((attachParams.get(k) + "").trim())) {
				where += " and " + alias + ".NAME like '%"
						+ attachParams.get(k) + "%' ";
			} else {
				where += " and " + alias + "." + k + "='" + attachParams.get(k)
						+ "' ";
			}
		}
		if ("".equals(where)) {
			log.warn("查询条件无效！");
			return null;
		} else {
			where = where.substring(4);
		}
		return where;
	}

	/**
	 * 结合参数获取冗余邻区
	 * 
	 * @param cell
	 * @param ncsDescId
	 * @param handoverDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-16 下午4:33:17
	 */
	@SuppressWarnings("unchecked")
	/*public List<RedundantNCell> getRedundantNcell(final String cell,
			final long ncsDescId, final long handoverDescId,
			final long cellStructDescId, final NcellAnalysisCondition condition) {
		log.info("进入：getRedundantNcell。cell=" + cell + ",ncsDescId="
				+ ncsDescId + ",handoverDescId=" + handoverDescId
				+ ",cellStructDescId=" + cellStructDescId);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RedundantNCell>>() {

					public List<RedundantNCell> doInHibernate(final Session arg0)
							throws HibernateException, SQLException {

						final String sql = "select '"
								+ RnoConstant.DataType.RedundantNcellType
								+ "' as NCELLTYPE,mid1.*,MID2.HOVERCNT from "
								+ "(select ncs.cell,ncs.ncell,ncs.reparfcn,ncs.times,ncs.navss,ncs.distance,cellstruct.EXPECTED_COVER_DISTANCE from rno_ncs ncs inner join RNO_CELL_STRUCT cellstruct on ncs.ncell=CELLSTRUCT.cell and CELLSTRUCT.RNO_CELL_STRUCT_DESC_ID="
								+ cellStructDescId
								+ "  where RNO_NCS_DESC_ID="
								+ ncsDescId
								+ " and ncs.cell='"
								+ cell
								+ "' "
								+ "   and NCS.TIMES/NCS.REPARFCN<"
								+ condition.getMinDetectRatio()
								+ " and ncs.navss>"
								+ condition.getMinNavss()// 因为是负数
								+ "   and NCS.DISTANCE*1000>("// 一个单位是米，一个是千米
								+ "  select cellstruct.EXPECTED_COVER_DISTANCE from RNO_CELL_STRUCT cellstruct where cellstruct.RNO_CELL_STRUCT_DESC_ID="
								+ cellStructDescId
								+ " and NCS.ncell=cellstruct.cell"
								+ "  )"
								+ "  and ncs.ncell in(select  ncell from rno_ncell where cell='"
								+ cell
								+ "')"
								+ " )mid1,"
								+ " (select hand.server_cell,HAND.TARGET_CELL,HAND.HOVERCNT from RNO_HANDOVER hand where hand.RNO_HANDOVER_DESC_ID="
								+ handoverDescId
								+ " and HAND.SERVER_CELL='"
								+ cell
								+ "'  and hand.HOVERCNT<"
								+ condition.getMinHoverCnt()
								+ ")mid2"
								+ " where MID1.cell=MID2.SERVER_CELL and MID1.ncell=MID2.TARGET_CELL";
						log.info("查询指定小区：" + cell + "的冗余邻区的sql语句：" + sql);
						// System.out.println("sql=="+sql);
						final SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> list= query.list();
						//System.out.println("list="+list);
						List<RedundantNCell> result=new ArrayList<RedundantNCell>();
						for(Map<String,Object> one:list){
							result.add(getRedundantNCellFromMap(one));
						}
						return result;
					}
				});
	}*/

	/**
	 * 结合参数获取冗余邻区
	 * 
	 * @param cell
	 * @param ncsDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-16 下午4:33:17
	 */
	/*public List<RedundantNCell> getOmitNcell(final String cell,
			final long ncsDescId, final long cellStructDescId,
			final NcellAnalysisCondition condition) {
		log.info("进入：getOmitNcell。cell=" + cell + ",ncsDescId=" + ncsDescId
				+ ",cellStructDescId=" + cellStructDescId);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RedundantNCell>>() {

					public List<RedundantNCell> doInHibernate(final Session arg0)
							throws HibernateException, SQLException {

						final String sql = "select '"
								+ RnoConstant.DataType.OmitNcellType
								+ "' as NCELLTYPE,0 as HOVERCNT ,ncs.cell,ncs.ncell,ncs.reparfcn,ncs.times,ncs.navss,ncs.distance,cellstruct.EXPECTED_COVER_DISTANCE from rno_ncs ncs inner join RNO_CELL_STRUCT cellstruct on ncs.ncell=CELLSTRUCT.cell and CELLSTRUCT.RNO_CELL_STRUCT_DESC_ID="
								+ cellStructDescId
								+ "   where RNO_NCS_DESC_ID="
								+ ncsDescId
								+ " and ncs.cell='"
								+ cell
								+ "' "
								+ "   and NCS.TIMES/NCS.REPARFCN>"
								+ condition.getMinDetectRatio()
								+ " and ncs.navss<"
								+ condition.getMinNavss()
								+ "   and NCS.DISTANCE*200<("// 乘于1000/5
								+ "    select cellstruct.EXPECTED_COVER_DISTANCE from RNO_CELL_STRUCT cellstruct where cellstruct.RNO_CELL_STRUCT_DESC_ID="
								+ cellStructDescId
								+ " and NCS.ncell=cellstruct.cell"
								+ "  )"
								+ "  and ncs.ncell NOT in(select  ncell from rno_ncell where cell='"
								+ cell + "')";
						log.info("查询指定小区：" + cell + "的漏定邻区的sql语句：" + sql);
						//System.out.println(sql);
						final SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> list= query.list();
						//System.out.println("list="+list);
						List<RedundantNCell> result=new ArrayList<RedundantNCell>();
						for(Map<String,Object> one:list){
							result.add(getRedundantNCellFromMap(one));
						}
						return result;
					}
				});
	}*/
	
	/*private RedundantNCell getRedundantNCellFromMap(Map<String,Object> one){
		RedundantNCell item=null;
		item=new RedundantNCell();
		item.setCell(one.get("CELL")+"");
		item.setDistance(Double.parseDouble(one.get("DISTANCE")+""));
		item.setExpectedDistance(Double.parseDouble(one.get("EXPECTED_COVER_DISTANCE")+""));
		item.setHovercnt(Long.parseLong(one.get("HOVERCNT")+""));
		item.setNavss(Double.parseDouble(one.get("NAVSS")+""));
		item.setNcell(one.get("NCELL")+"");
		item.setNcellType(one.get("NCELLTYPE")+"");
		item.setReparfcn(Long.parseLong(one.get("REPARFCN")+""));
		item.setTimes(Long.parseLong(one.get("TIMES")+""));
		
		return item;
	}*/

	/**
	 * 通过区域和话统时间返回切换描述数据的多少
	 * 
	 * @param areaId
	 * @param staticTime
	 * @return
	 * @author chao.xj
	 * @date 2013-11-14上午11:07:45
	 */
	public List<RnoHandoverDescriptor> queryRnoHandoverDescByAreaAndStaticTime(
			final long areaId, final String staticTime) {

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoHandoverDescriptor>>() {
					public List<RnoHandoverDescriptor> doInHibernate(
							Session arg0) throws HibernateException,
							SQLException {
						String sql = "SELECT * from RNO_HANDOVER_DESCRIPTOR WHERE AREA_ID=? and STATICS_TIME=TO_DATE(?, 'yyyy-mm-dd hh24:mi:ss')";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, areaId);
						query.setString(1, staticTime);

						query.addEntity(RnoHandoverDescriptor.class);
						return query.list();
					}
				});

	}

	/**
	 * 保存一个切换统计描述记录对象
	 * 
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14下午12:01:23
	 */
	public void saveOneRnoHandOverDesc(
			RnoHandoverDescriptor rnoHandoverDescriptor) {
		hibernateTemplate.save(rnoHandoverDescriptor);
	}

	/**
	 * 保存一个切换统计记录对象
	 * 
	 * @param rnoHandover
	 * @author chao.xj
	 * @date 2013-11-14下午12:01:23
	 */
	public void saveOneRnoHandOver(RnoHandover rnoHandover) {
		hibernateTemplate.save(rnoHandover);
	}

	/**
	 * 删除一个切换统计描述数据
	 * 
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14上午11:25:21
	 */
	public void deleteOneRnoHandoverDesc(
			RnoHandoverDescriptor rnoHandoverDescriptor) {

		hibernateTemplate.delete(rnoHandoverDescriptor);

	}

	/**
	 * 通过rnohanddescID删除其下所有的切换统计数据
	 * 
	 * @param rnoHandoverDescriptor
	 * @author chao.xj
	 * @date 2013-11-14上午11:53:15
	 */
	public void deleteRnoHandoverByRnoHandDescId(
			final RnoHandoverDescriptor rnoHandoverDescriptor) {

		hibernateTemplate.execute(new HibernateCallback() {
			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub　RnoHandover.java需要用类名
				String sql = "DELETE from RnoHandover WHERE RNO_HANDOVER_DESC_ID="
						+ rnoHandoverDescriptor.getRnoHandoverDescId();
				Query query = arg0.createQuery(sql);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 通过日期等条件语句查找返回不同总数量记录
	 * 
	 * @param date
	 * @param sqlForCount
	 * @return
	 * @author chao.xj
	 * @date 2013-11-16下午09:58:18
	 */
	public int getTotalHODescCount(String date[], boolean sqlForCount,
			long areaId) {
		String sql = "";
		Long count = (long) 0;
		SQLQuery query = null;
		if ((date[0].trim().isEmpty() && !date[1].trim().isEmpty())
				|| (!date[0].trim().isEmpty() && date[1].trim().isEmpty())) {
			// System.out.println("一个为空");
			String dateString = "";
			if (date[0].isEmpty()) {
				dateString = date[1];
			} else {
				dateString = date[0];
			}
			sql = getHODescSql(date, sqlForCount, areaId);
			count = (Long) hibernateTemplate.find(sql).listIterator().next();

			//System.out.println(sql);
		} else if (!date[0].trim().isEmpty() && !date[1].trim().isEmpty()) {
			// (date[0]!=null || !date[0].equals("")) && (date[1]!=null ||
			// !date[1].equals(""))
			// System.out.println("两个均不为空");
			sql = getHODescSql(date, sqlForCount, areaId);
			count = (Long) hibernateTemplate.find(sql).listIterator().next();
			// System.out.println(sql);
		} else if (date[0].trim().isEmpty() && date[1].trim().isEmpty()) {
			// System.out.println("两个为空");
			sql = getHODescSql(date, sqlForCount, areaId);
			count = (Long) hibernateTemplate.find(sql).listIterator().next();
			// System.out.println(start+"--"+end);
			// System.out.println(sql);
		}
		//System.out.println(count);
		return count.intValue();
	}

	/**
	 * 通过日期等条件语句查找返回不同sql语句
	 * 
	 * @param date
	 * @param sqlForCount
	 * @return
	 * @author chao.xj
	 * @date 2013-11-16下午10:00:17
	 */
	public String getHODescSql(String date[], boolean sqlForCount, long areaId) {
		String sql = "";
		SQLQuery query = null;
		if ((date[0].trim().isEmpty() && !date[1].trim().isEmpty())
				|| (!date[0].trim().isEmpty() && date[1].trim().isEmpty())) {
			// System.out.println("一个为空");
			String dateString = "";
			
			if (sqlForCount) {
					if (date[0].isEmpty()) {
					dateString = date[1];
					sql = "SELECT count(*)  from RnoHandoverDescriptor WHERE STATICS_TIME <= TO_DATE('"
						+ dateString + "', 'yyyy-mm-dd') and AREA_ID=" + areaId;

				} else {
					dateString = date[0];
					sql = "SELECT count(*)  from RnoHandoverDescriptor WHERE STATICS_TIME >= TO_DATE('"
						+ dateString + "', 'yyyy-mm-dd') and AREA_ID=" + areaId;
				}
			} else {
				if (date[0].isEmpty()) {
					dateString = date[1];
					//sql = "select * from (SELECT RNO_HANDOVER_DESC_ID,TO_CHAR(STATICS_TIME,'yyyy-MM-dd hh24:mi:ss') STATICS_TIME,AREA_ID,TO_CHAR(CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') CREATE_TIME,TO_CHAR(MOD_TIME,'yyyy-MM-dd hh24:mi:ss') MOD_TIME,STATUS,rownum rn from RNO_HANDOVER_DESCRIPTOR WHERE STATICS_TIME <= TO_DATE(?, 'yyyy-mm-dd') and AREA_ID=?) where rn>=? and rn<=?";
					sql = "select * from (SELECT a.RNO_HANDOVER_DESC_ID,TO_CHAR(a.STATICS_TIME,'yyyy-MM-dd hh24:mi:ss') STATICS_TIME,a.AREA_ID,TO_CHAR(a.CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') CREATE_TIME,TO_CHAR(a.MOD_TIME,'yyyy-MM-dd hh24:mi:ss') MOD_TIME,STATUS,b.NAME areaname,rownum rn from RNO_HANDOVER_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND a.STATICS_TIME <= TO_DATE(?, 'yyyy-mm-dd') and a.AREA_ID=?) where rn>=? and rn<=?";				
				} else {
					dateString = date[0];
					sql = "select * from (SELECT a.RNO_HANDOVER_DESC_ID,TO_CHAR(a.STATICS_TIME,'yyyy-MM-dd hh24:mi:ss') STATICS_TIME,a.AREA_ID,TO_CHAR(a.CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') CREATE_TIME,TO_CHAR(a.MOD_TIME,'yyyy-MM-dd hh24:mi:ss') MOD_TIME,STATUS,b.NAME areaname,rownum rn from RNO_HANDOVER_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND a.STATICS_TIME >= TO_DATE(?, 'yyyy-mm-dd') and a.AREA_ID=?) where rn>=? and rn<=?";
				    //System.out.println(sql);
				}
			}
			// System.out.println(sql);
		} else if (!date[0].trim().isEmpty() && !date[1].trim().isEmpty()) {
			// (date[0]!=null || !date[0].equals("")) && (date[1]!=null ||
			// !date[1].equals(""))
			// System.out.println("两个均不为空");
			if (sqlForCount) {
				sql = "SELECT  count(*)   from RnoHandoverDescriptor WHERE STATICS_TIME BETWEEN TO_DATE('"
						+ date[0]
						+ "00:00:00', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"
						+ date[1]
						+ "23:59:59', 'yyyy-mm-dd hh24:mi:ss') and AREA_ID="
						+ areaId;
			} else {
				sql = "select * from (SELECT a.RNO_HANDOVER_DESC_ID,TO_CHAR(a.STATICS_TIME,'yyyy-MM-dd hh24:mi:ss') STATICS_TIME,a.AREA_ID,TO_CHAR(a.CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') CREATE_TIME,TO_CHAR(a.MOD_TIME,'yyyy-MM-dd hh24:mi:ss') MOD_TIME,STATUS,b.NAME areaname,rownum rn from RNO_HANDOVER_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND a.STATICS_TIME BETWEEN TO_DATE('"
						+ date[0]
						+ "00:00:00', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"
						+ date[1]
						+ "23:59:59', 'yyyy-mm-dd hh24:mi:ss') and a.AREA_ID=?) where rn>=? and rn<=?";
			}
			// System.out.println(sql);
		} else if (date[0].trim().isEmpty() && date[1].trim().isEmpty()) {
			// System.out.println("两个为空");
			if (sqlForCount) {
				sql = "SELECT  count(*)   from RnoHandoverDescriptor WHERE AREA_ID="
						+ areaId;
			} else {
				sql = "select * from (SELECT a.RNO_HANDOVER_DESC_ID,TO_CHAR(a.STATICS_TIME,'yyyy-MM-dd hh24:mi:ss') STATICS_TIME,a.AREA_ID,TO_CHAR(a.CREATE_TIME,'yyyy-MM-dd hh24:mi:ss') CREATE_TIME,TO_CHAR(a.MOD_TIME,'yyyy-MM-dd hh24:mi:ss') MOD_TIME,STATUS,b.NAME areaname,rownum rn from RNO_HANDOVER_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND a.AREA_ID=?) where  rn>=? and rn<=?";
			}
			// System.out.println(start+"--"+end);
			
		}
		// System.out.println(sql);
		return sql;
	}

	/**
	 * 通过区域和日期返回切换描述数据的多少
	 * 
	 * @param areaId
	 * @param date
	 * @return
	 * @author chao.xj
	 * @date 2013-11-14上午11:07:45
	 */
	/*
	 * public List<Map<String,Object>> queryRnoHandoverDescByAreaAndDate(Page
	 * page,final long areaId,final String date[]){
	 * 
	 * final int start = (page.getPageSize() (page.getCurrentPage() - 1) + 1);
	 * final int end = (page.getPageSize() * page.getCurrentPage());
	 * 
	 * return hibernateTemplate .executeFind(new
	 * HibernateCallback<List<Map<String,Object>>>() { public
	 * List<Map<String,Object>> doInHibernate(Session arg0) throws
	 * HibernateException, SQLException { String sql=""; SQLQuery query=null; if
	 * ((date[0].trim().isEmpty() && !date[1].trim().isEmpty()) ||
	 * (!date[0].trim().isEmpty() && date[1].trim().isEmpty())) {
	 * //System.out.println("一个为空"); String dateString=""; if
	 * (date[0].isEmpty()) { dateString=date[1]; }else { dateString=date[0]; }
	 * sql=
	 * "select * from (SELECT RNO_HANDOVER_DESC_ID,STATICS_TIME,AREA_ID,CREATE_TIME,MOD_TIME,STATUS,rownum rn from RNO_HANDOVER_DESCRIPTOR WHERE STATICS_TIME LIKE TO_DATE(?, 'yyyy-mm-dd') and AREA_ID=?) where rn>=? and rn<=?"
	 * ; query = arg0.createSQLQuery(sql); query.setString(0, dateString);
	 * query.setLong(1, areaId); query.setInteger(2, start); query.setInteger(3,
	 * end); //System.out.println(sql); }else if (!date[0].trim().isEmpty() &&
	 * !date[1].trim().isEmpty()) { //(date[0]!=null || !date[0].equals("")) &&
	 * (date[1]!=null || !date[1].equals("")) //System.out.println("两个均不为空");
	 * sql=
	 * "select * from (SELECT RNO_HANDOVER_DESC_ID,STATICS_TIME,AREA_ID,CREATE_TIME,MOD_TIME,STATUS,rownum rn from RNO_HANDOVER_DESCRIPTOR WHERE STATICS_TIME BETWEEN TO_DATE('"
	 * +date[0]+"00:00:00', 'yyyy-mm-dd hh24:mi:ss') and TO_DATE('"+date[1]+
	 * "23:59:59', 'yyyy-mm-dd hh24:mi:ss') and AREA_ID=?) where rn>=? and rn<=?"
	 * ; query = arg0.createSQLQuery(sql); query.setLong(0, areaId);
	 * query.setInteger(1, start); query.setInteger(2, end);
	 * //System.out.println(sql); }else if (date[0].trim().isEmpty() &&
	 * date[1].trim().isEmpty()) { //System.out.println("两个为空"); sql=
	 * "select * from (SELECT RNO_HANDOVER_DESC_ID,STATICS_TIME,AREA_ID,CREATE_TIME,MOD_TIME,STATUS,rownum rn from RNO_HANDOVER_DESCRIPTOR WHERE AREA_ID=?) where  rn>=? and rn<=?"
	 * ; query = arg0.createSQLQuery(sql); query.setLong(0, areaId);
	 * query.setInteger(1, start); query.setInteger(2, end);
	 * //System.out.println(start+"--"+end); //System.out.println(sql); }
	 * //query.addEntity(RnoHandoverDescriptor.class);
	 * query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	 * List<Map<String,Object>> lists=query.list(); return lists; } });
	 * 
	 * }
	 */
	public List<Map<String, Object>> queryRnoHandoverDescByAreaAndDate(
			Page page, final long areaId, final String date[],
			final boolean sqlForCount) {

		final int start = (page.getPageSize() * (page.getCurrentPage() - 1) + 1);
		final int end = (page.getPageSize() * page.getCurrentPage());

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "";
						SQLQuery query = null;
						if ((date[0].trim().isEmpty() && !date[1].trim()
								.isEmpty())
								|| (!date[0].trim().isEmpty() && date[1].trim()
										.isEmpty())) {
							// System.out.println("一个为空");
							String dateString = "";
							if (date[0].isEmpty()) {
								dateString = date[1];
							} else {
								dateString = date[0];
							}
							if (!sqlForCount) {
								sql = getHODescSql(date, sqlForCount, areaId);
								query = arg0.createSQLQuery(sql);
								query.setString(0, dateString);
								query.setLong(1, areaId);
								query.setInteger(2, start);
								query.setInteger(3, end);
							}
							// System.out.println(sql);
						} else if (!date[0].trim().isEmpty()
								&& !date[1].trim().isEmpty()) {
							// (date[0]!=null || !date[0].equals("")) &&
							// (date[1]!=null || !date[1].equals(""))
							// System.out.println("两个均不为空");
							if (!sqlForCount) {
								sql = getHODescSql(date, sqlForCount, areaId);
								query = arg0.createSQLQuery(sql);
								query.setLong(0, areaId);
								query.setInteger(1, start);
								query.setInteger(2, end);
							}
							// System.out.println(sql);
						} else if (date[0].trim().isEmpty()
								&& date[1].trim().isEmpty()) {
							// System.out.println("两个为空");
							if (!sqlForCount) {
								sql = getHODescSql(date, sqlForCount, areaId);
								query = arg0.createSQLQuery(sql);
								query.setLong(0, areaId);
								query.setInteger(1, start);
								query.setInteger(2, end);
							}

							// System.out.println(start+"--"+end);
							// System.out.println(sql);
						}
						// query.addEntity(RnoHandoverDescriptor.class);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> lists = query.list();
						return lists;
					}
				});

	}

	/**
	 * 根据HoDescId获取切换统计描述记录
	 * 
	 * @param HoDescId
	 * @return
	 */
	public List<RnoHandoverDescriptor> getRnoHandoverDescriptorByHoDescId(
			final String HoDescId[]) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoHandoverDescriptor>>() {
					public List<RnoHandoverDescriptor> doInHibernate(
							Session arg0) throws HibernateException,
							SQLException {
						String items = "";
						for (int i = 0; i < HoDescId.length; i++) {
							if (i == HoDescId.length - 1) {
								items += HoDescId[i];
							} else {
								items += HoDescId[i] + ",";
							}
						}
						String sql = "SELECT * from RNO_HANDOVER_DESCRIPTOR WHERE RNO_HANDOVER_DESC_ID in("
								+ items + ")";
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.setString(0, items);
						query.addEntity(RnoHandoverDescriptor.class);
						return query.list();
					}
				});
	}

	/**
	 * 通过区域ID获取一个area对象
	 * 
	 * @param areaid
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午02:41:51
	 */
	public SysArea getAreaobjByareaId(long areaid) {
		System.out.println(areaid);
		return (SysArea) hibernateTemplate.get(SysArea.class, areaid);
	}

	/**
	 * 通过源区和目标小区确认是否是邻区关系
	 * 
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:47:15
	 */
	public boolean queryNcellByoCell(String sourcecell, String targetcell) {
		log.info("进入queryNcellByoCell(String sourcecell, String targetcell)："+sourcecell+":"+targetcell);
		boolean b = false;
		String sql = "from RnoNcell WHERE CELL='" + sourcecell
				+ "' AND NCELL='" + targetcell + "'";
		Iterator iterator = hibernateTemplate.find(sql).listIterator();
		if (iterator.hasNext()) {
			b = true;
		}
		log.info("退出queryNcellByoCell");
		return b;
	}

	/**
	 * 通过传入小区label得到一个小区对象
	 * 
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2013-11-19下午03:59:20
	 */
	public Cell queryCellobjByLabel(String label) {

		String sql = "from Cell WHERE LABEL='" + label + "'";
		Cell cell = (Cell) hibernateTemplate.find(sql).listIterator().next();
		return cell;
	}

	/**
	 * 通过源与目标小区得到其经纬度数据113.4087,23.07341,113.27481,23.04926
	 * 
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午10:51:35
	 */
	public List<String> getLnglatsBySourceCellAndTargetCell(
			final String sourcecell, final String targetcell) {
		log.info("进入getLnglatsBySourceCellAndTargetCell(final String sourcecell, final String targetcell)"+sourcecell+":"+targetcell);
		List<String> lnglats = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						List<String> lnglats = new ArrayList<String>();
						String sql = "SELECT wmsys.wm_concat(LONGITUDE||','||LATITUDE) as  LNGLATS from CELL WHERE LABEL ='"
								+ sourcecell
								+ "' or LABEL='"
								+ targetcell
								+ "'";
						SQLQuery query = arg0.createSQLQuery(sql);
						List<Object> objects = query.list();
						if (objects == null || objects.size() == 0) {
							return lnglats;
						}

						for (Object o : objects) {
							lnglats.add(o.toString());
						}

						return lnglats;
					}

				});
		log.info("退出getLnglatsBySourceCellAndTargetCell");
		return lnglats;
	}

	/**
	 * 通过两个小区查询其共有的邻区
	 * 
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2013-11-20上午11:31:22
	 */
	public List<RnoNcell> queryCommonNcellByTwoCell(final String sourcecell,
			final String targetcell) {
		log.info("进入queryCommonNcellByTwoCell(final String sourcecell,final String targetcell):"+sourcecell+":"+targetcell);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoNcell>>() {

					public List<RnoNcell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						String sql = "SELECT * from RNO_NCELL WHERE NCELL='"
								+ sourcecell
								+ "' AND CELL IN(SELECT CELL from RNO_NCELL WHERE NCELL='"
								+ targetcell + "')";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoNcell.class);
						log.info("退出queryCommonNcellByTwoCell");
						return query.list();
					}
				});
	}

	/**
	 * 获取指定小区配置表的所有小区的冗余邻区
	 * 
	 * @param cellConfig
	 * @param ncsDescId
	 * @param handoverDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-19 下午8:42:39
	 */
	/*public List<RedundantNCell> getAllRedundantCellsByCondition(
			final PlanConfig cellConfig, final long ncsDescId,
			final long handoverDescId, final long cellStructDescId,
			final NcellAnalysisCondition condition) {

		log.info("进入方法：getAllRedundantCellsByCondition.cellConfig="
				+ cellConfig + ",ncsDescId=" + ncsDescId + ",handoverDescId="
				+ handoverDescId + ",cellStructDescId=" + cellStructDescId);
		if (cellConfig == null) {
			log.error("未指明小区配置信息。");
			return Collections.EMPTY_LIST;
		}
		String cellTable = "CELL";
		if (cellConfig.isTemp()) {
			cellTable = "RNO_TEMP_CELL";
		}
		long cellConfigId = cellConfig.getConfigId();
		final String sql = "select mid7.* from "
				+ " (select  'REDUNDANT' as NCELLTYPE,mid5.* from "
				+ " (select mid3.*,MID4.HOVERCNT from "
				+ " (select mid1.*,mid2.EXPECTED_COVER_DISTANCE from "
				+ " (select ncs.cell,ncs.ncell,NCS.TIMES,NCS.REPARFCN,NCS.times/NCS.REPARFCN,NCS.navss ,ncs.distance from RNO_NCS ncs "
				+ " where NCS.RNO_NCS_DESC_ID="
				+ ncsDescId
				+ " and ncs.TIMES/ncs.REPARFCN<"
				+ condition.getMinDetectRatio()
				+ " and ncs.NAVSS>"
				+ condition.getMinNavss()
				+ " )mid1 inner join "
				+ " ( "
				+ " select * from RNO_CELL_STRUCT cellstruct where RNO_CELL_STRUCT_DESC_ID="
				+ cellStructDescId
				+ " )mid2 "
				+ " on  mid1.ncell=mid2.cell and mid1.DISTANCE*1000>mid2.EXPECTED_COVER_DISTANCE "
				+ " )mid3 "
				+ " inner join "
				+ " (select * from RNO_HANDOVER where RNO_HANDOVER_DESC_ID="
				+ handoverDescId
				+ " )mid4 "
				+ " on MID3.cell=MID4.server_cell and MID3.ncell=MID4.target_cell and MID4.HOVERCNT<"
				+ condition.getMinHoverCnt()
				+ " )mid5 "
				+ " inner join (select label from "
				+ cellTable
				+ " where CELL_DESC_ID="
				+ cellConfigId
				+ ")mid6 on MID5.cell=MID6.label "
				+ " )mid7 "
				+ " inner join rno_ncell on mid7.cell=rno_ncell.cell and mid7.ncell=rno_ncell.ncell order by MID7.cell";
		log.info("获取全部的冗余邻区的sql=" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RedundantNCell>>() {

					public List<RedundantNCell> doInHibernate(final Session arg0)
							throws HibernateException, SQLException {

						final SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> list= query.list();
						//System.out.println("list="+list);
						List<RedundantNCell> result=new ArrayList<RedundantNCell>();
						for(Map<String,Object> one:list){
							result.add(getRedundantNCellFromMap(one));
						}
						return result;
					}
				});
	}*/

	/**
	 * 获取指定配置下的所有漏定邻区
	 * 
	 * @param cellConfig
	 * @param ncsDescId
	 * @param cellStructDescId
	 * @param condition
	 * @return
	 * @author brightming 2013-11-20 上午10:37:13
	 */
	@SuppressWarnings("unchecked")
	/*public List<RedundantNCell> getAllOmitCellsByCondition(
			PlanConfig cellConfig, long ncsDescId, long cellStructDescId,
			NcellAnalysisCondition condition) {
		log.info("进入方法：getAllOmitCellsByCondition.cellConfig=" + cellConfig
				+ ",ncsDescId=" + ncsDescId + ",cellStructDescId="
				+ cellStructDescId);
		if (cellConfig == null) {
			log.error("未指明小区配置信息。");
			return Collections.EMPTY_LIST;
		}
		String cellTable = "CELL";
		if (cellConfig.isTemp()) {
			cellTable = "RNO_TEMP_CELL";
		}
		long cellConfigId = cellConfig.getConfigId();
		final String sql = " select mid5.* from "
				+ " ( select 'OMIT' as NCELLTYPE,0 as HANDOVERCNT,mid3.* from "
				+ "  (select mid1.*,mid2.EXPECTED_COVER_DISTANCE from "
				+ "  (select ncs.cell,ncs.ncell,NCS.TIMES,NCS.REPARFCN,NCS.times/NCS.REPARFCN,NCS.navss ,ncs.distance from RNO_NCS ncs "
				+ "  where NCS.RNO_NCS_DESC_ID="
				+ ncsDescId
				+ "   and ncs.TIMES/ncs.REPARFCN>"
				+ condition.getMinDetectRatio()
				+ "   and ncs.NAVSS<"
				+ condition.getMinNavss()
				+ "  )mid1 inner join "
				+ "  ( "
				+ "  select * from RNO_CELL_STRUCT cellstruct where RNO_CELL_STRUCT_DESC_ID="
				+ cellStructDescId
				+ "  )mid2 "
				+ " on  mid1.ncell=mid2.cell and mid1.DISTANCE*200<mid2.EXPECTED_COVER_DISTANCE "
				+ " )mid3"
				+ " inner join (select label from "
				+ cellTable
				+ " where CELL_DESC_ID="
				+ cellConfigId
				+ " )mid4 "
				+ "  on MID3.cell=MID4.label)mid5 "
				+ " left join rno_ncell on MID5.cell=RNO_NCELL.cell and MID5.ncell=RNO_NCELL.ncell "
				+ " where rno_ncell.cell is null order by MID5.cell";

		log.info("获取全部的漏定邻区的sql=" + sql);
		System.out.println(sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RedundantNCell>>() {

					public List<RedundantNCell> doInHibernate(final Session arg0)
							throws HibernateException, SQLException {

						final SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> list= query.list();
						//System.out.println("list="+list);
						List<RedundantNCell> result=new ArrayList<RedundantNCell>();
						for(Map<String,Object> one:list){
							result.add(getRedundantNCellFromMap(one));
						}
						return result;
					}
				});
	}*/
	/**
	 * 获取指定区域和模糊查询条件的小区配置描述的总量
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午02:06:15
	 */
	public int getTotalCellDescCountByArea(final long areaId,final String schemeName,final boolean sysDefault) {
		
		return hibernateTemplate.execute(new HibernateCallback<Integer>(){
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				/*
				 * Liang YJ 2014-03-10 15:13 修改
				 */
				String sql="SELECT count(*) from RNO_CELL_DESCRIPTOR WHERE AREA_ID=? AND DEFAULT_DESCRIPTOR=? AND name LIKE ?";
				String sysDefaultStr = sysDefault == true ? "Y" : "N";
				String schemeNameStr = "%"+schemeName+"%";
				int i=0;
				SQLQuery query=arg0.createSQLQuery(sql);
				query.setLong(0, areaId);
				query.setString(1, sysDefaultStr);
				query.setString(2, schemeNameStr);
				//query.setString(1, schemeName);
				List<Object> inList=query.list();
				if (inList!=null && inList.size()>0) {
					i=Integer.valueOf(inList.get(0).toString());
				}
				return i;
			}
		});
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
			Page page, final String areaIds,final String schemeName,final boolean sysDefault) {

		final int start = (page.getPageSize() * (page.getCurrentPage() - 1) + 1);
		final int end = (page.getPageSize() * page.getCurrentPage());

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT * from (SELECT a.cell_descriptor_id,a.name,TO_CHAR(a.CREATE_TIME,'YYYY-MM-DD HH24:mi:ss') AS CREATE_TIME,a.AREA_ID,b.NAME areaname,rownum rn from RNO_CELL_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND a.AREA_ID in("+areaIds+") AND a.DEFAULT_DESCRIPTOR='"+(sysDefault==true?'Y':'N')+"' AND a.name LIKE '%"+schemeName+"%'  ORDER BY a.CREATE_TIME DESC) WHERE rn>=? and rn<=?";
						SQLQuery query = null;
								query = arg0.createSQLQuery(sql);
//								query.setString(0, areaIds);
								query.setInteger(0, start);
								query.setInteger(1, end);
						
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> lists = query.list();
						return lists;
					}
				});

	}
	/**
	 * 通过小区配置ID的字符串获取小区配置描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellConfigureDescByConfigIds(final String configIds) {
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="SELECT a.cell_descriptor_id,a.name,TO_CHAR(a.CREATE_TIME,'YYYY-MM-DD HH24:mi:ss') AS CREATE_TIME,a.AREA_ID,b.NAME areaname,a.TEMP_STORAGE TEMP_STORAGE from RNO_CELL_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND CELL_DESCRIPTOR_ID  in( "+configIds+" )";
				SQLQuery query=arg0.createSQLQuery(sql);
				//query.setString(0, configIds);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
			
			
		});
	}
	/**
	 * 改造：干扰数据加载页面：从系统加载的获取查询sql语句
	 * @param areaId
	 * @param attachParams
	 * @param isDefault
	 * @param schemeName
	 * @param forcount
	 * @return
	 * @author chao.xj
	 * @date 2013-12-13下午03:13:46
	 */
	public String getLoadInterferenceSql(long areaId,HashMap<String, Object> attachParams,boolean isDefault,String schemeName,boolean forcount) {
		log.info("进入方法：getLoadInterferenceSql areaId="+areaId+",attachParams="
				+attachParams+",isDefault="+isDefault+",schemeName="+schemeName+",forcount"+forcount);
		
		StringBuffer sbf = new StringBuffer(" 1=1 and ");
		String filed="";
		//System.out.println("isDefault:"+isDefault);
		//String sql = "select a.STS_DESC_ID,a.AREA_ID,b.\"NAME\",\"TO_CHAR\"(a.STS_DATE,'YYYY/MM/DD') AS STS_DATE,a.STS_PERIOD from RNO_STS_DESCRIPTOR a,Area b where a.SPEC_TYPE='"+ searchType + "'";
		sbf.append(" a.AREA_ID=b.AREA_ID AND a.AREA_ID="+areaId+" AND a.NET_TYPE='"+attachParams.get("netType")+"' AND a.DEFAULT_DESCRIPTOR='"+(isDefault==true?'Y':'N')+"'");
		/*if (condition.getStsDateStr() != null
				&& !"".equals(condition.getStsDateStr())) {
			sql += " AND a.STS_DATE=\"TO_DATE\"('" + condition.getStsDateStr()
					+ "','YYYY-MM-DD') ";
		}*/
		if (!isDefault) {
			sbf.append(" AND a.NAME LIKE '%"+schemeName+"%'");
		}
		
		String sql="";
		if (forcount) {
			sql = "select count(*) from  RNO_INTERFERENCE_DESCRIPTOR a,SYS_AREA b  where "+sbf.toString();
		}else {
			sql = "select * from (select a.INTER_DESC_ID,a.NAME,a.COLLECT_TYPE,TO_CHAR(a.COLLECT_TIME,'YYYY-MM-DD') COLLECT_TIME,a.DEFAULT_DESCRIPTOR,a.AREA_ID,a.NET_TYPE,b.NAME areaname,rownum rn from  RNO_INTERFERENCE_DESCRIPTOR a,SYS_AREA b  where "+sbf.toString()+") where   rn>=? and rn<=?";
		}
		log.info("getLoadInterferenceSql的sql语句：" + sql);
		//System.out.println(sql);
		return sql;
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
			final String schemeName,final boolean forcount) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = getLoadInterferenceSql(areaId, attachParams, isDefault, schemeName, true);
				SQLQuery query = arg0.createSQLQuery(sql);
				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
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
			final String schemeName,final boolean forcount) {
		log.info("进入方法：queryInterferenceDataByPage(final Page page,final long areaId,final HashMap<String, Object> attachParams,final boolean isDefault,"+
			"final String schemeName,final boolean forcount).page=" + page + ",areaId=" + areaId);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = getLoadInterferenceSql(areaId, attachParams, isDefault, schemeName, false);
						SQLQuery query = arg0.createSQLQuery(sql);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						query.setLong(0, start);
						query.setLong(1, end);
						//System.out.println(start+":"+end);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> stsList = query.list();
						log.info("获取到结果集记录数量："
								+ (stsList == null ? 0 : stsList.size()));
						return stsList;
					}
				});
	}
	/**
	 * 改造：通过干扰配置ID的字符串获取干扰指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getInterferenceDataDescByConfigIds(final String interDescIds) {
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="SELECT a.INTER_DESC_ID,a.NAME,a.TEMP_STORAGE,a.COLLECT_TYPE,TO_CHAR(a.COLLECT_TIME,'YYYY-MM-DD') COLLECT_TIME,a.DEFAULT_DESCRIPTOR,a.AREA_ID,a.NET_TYPE,b.NAME areaname from RNO_INTERFERENCE_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND inter_desc_id  in("+interDescIds+")";
				SQLQuery query=arg0.createSQLQuery(sql);
				//query.setString(0, configIds);
				//System.out.println(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
			
			
		});
	}
	/**
	 * 通过城市id获取其下相应的区域集合
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-23上午11:46:11
	 */
	public List<Map<String, Object>> getSpecialLevalAreaByCityId(final long cityId) {
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){
			
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="select b.* from (select  a.* from sys_area a start with a.area_id in (?) connect by prior  a.area_id= a.parent_id and area_level='区/县') b WHERE area_level='区/县'";
				SQLQuery query=arg0.createSQLQuery(sql);
				query.setLong(0, cityId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 通过小区名获取NCS干扰数据的集合
	 * @title 
	 * @param label
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24上午10:36:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getSpecifyCellInterferenceAnalysis(
			final String label,final long selectId) {
		log.info("进入getSpecifyCellInterferenceAnalysis(final String label,final long selectId)"+label+"-------"+selectId);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						//String sql = "SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt*100 same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt*100 adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance FROM (select CELL,NCELL,sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi,sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi,count(*) 数量,(SELECT TCH FROM CELL WHERE LABEL=CELL) celltch,(SELECT TCH FROM CELL WHERE LABEL=NCELL) ncelltch,(SELECT LONGITUDE FROM CELL WHERE LABEL=CELL) lng1,(SELECT LATITUDE FROM CELL WHERE LABEL=CELL) lat1,(SELECT LONGITUDE FROM CELL WHERE LABEL=NCELL) lng2,(SELECT LATITUDE FROM CELL WHERE LABEL=NCELL) lat2 from RNO_NCS WHERE CELL=? and rno_ncs_desc_id=? group by cell,NCELL) dist) t) t1 WHERE T1.ncellfreqcnt<>0 and t1.cellfreqcnt>0) t2";
						String sql = "SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt*100 same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt*100 adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance " 
								+"FROM (SELECT ncs.*,cellinfo.tch as celltch,cellinfo.longitude as lng1,cellinfo.latitude as lat1,cellinfo.lnglats as lnglats,  "  
		+"ncellinfo.tch as ncelltch,ncellinfo.longitude as lng2,ncellinfo.latitude as lat2  "  
		+"from  "  
		+"(  "  
		+"select cell,ncell,  "  
		+"sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi,  "  
		+"sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi   "  
		+"from RNO_NCS WHERE CELL=? and rno_ncs_desc_id=? group by cell,NCELL having SUM(REPARFCN)>0 "  
		+")ncs  "  
		+"left JOIN  "  
		+"(  "  
		+"  select mid_cell.* from   "  
		+"  (  "  
		+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell  "  
		+"  )mid_cell  "  
		+"  where rn=1  "  
		+")cellinfo  "  
		+"ON  "  
		+"(  "  
		+"ncs.cell=cellinfo.label  "  
		+")  "  
		+"left JOIN  "  
		+"(  "  
		+"select mid_cell.* from   "  
		+"  (  "  
		+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell  "  
		+"  )mid_cell  "  
		+"  where rn=1  "  
		+")ncellinfo  "  
		+"on(ncs.ncell=ncellinfo.label)  ) dist) t) t1 WHERE T1.ncellfreqcnt<>0 and t1.cellfreqcnt>0) t2";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setString(0, label);
						query.setLong(1, selectId);
						log.info("sql:"+sql);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("进入getSpecifyCellInterferenceAnalysis");
						return query.list();
					}
				});
	}
	/**
	 * 通过ncs描述ID获取NCS下的干扰数据集合
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午05:49:27
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getTotalAnalysisGisCellByNcsDescId(
			final long ncsDescId, long startIndex, long cnt) {
		log.info("进入getTotalAnalysisGisCellByNcsDescId(final long ncsDescId, long startIndex, long cnt)"+ncsDescId+","+startIndex+","+cnt);
		
		/*final String sql = "select * "
				+ " from (select *"
				+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from (SELECT t.CELL,t.lnglats,sum(t.cellinteredcoeffi) cellinteredcoeffisum from (SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100 same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100 adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance FROM (select CELL,NCELL,sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi,sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi,count(*) 数量,(SELECT DISTINCT(TCH) FROM CELL WHERE LABEL=CELL) celltch,(SELECT DISTINCT(TCH) FROM CELL WHERE LABEL=NCELL) ncelltch,(SELECT DISTINCT(LONGITUDE) FROM CELL WHERE LABEL=CELL) lng1,(SELECT DISTINCT(LATITUDE) FROM CELL WHERE LABEL=CELL) lat1,(SELECT DISTINCT(lnglats) FROM CELL WHERE LABEL=CELL) lnglats,(SELECT DISTINCT(LONGITUDE) FROM CELL WHERE LABEL=NCELL) lng2,(SELECT DISTINCT(LATITUDE) FROM CELL WHERE LABEL=NCELL) lat2 from RNO_NCS WHERE rno_ncs_desc_id=? group by cell,NCELL) dist) t) t1 WHERE T1.ncellfreqcnt<>0) t2) t GROUP BY t.cell,t.lnglats ORDER BY cellinteredcoeffisum DESC) t) p"
				+ " where p.\"row_number\" > " + startIndex + ") q"
				+ " where rownum <= " + cnt;*/
//		String filed="";
	/*	final String sql = "select * "
			+ " from (select *"
			+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
			+ " from (SELECT t.CELL,t.lnglats,sum(t.cellinteredcoeffi) cellinteredcoeffisum from (SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100 same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100 adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance FROM (" +
					"" +
					"" +
					") t) p"
			+ " where p.\"row_number\" > " + startIndex + ") q"
			+ " where rownum <= " + cnt;*/
		
		
		final String sql= "select *  from (select * from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\"  " 
		+"from (SELECT t.CELL,t.lnglats,sum(t.cellinteredcoeffi) cellinteredcoeffisum from   "  
		+"(SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi   "  
		+"from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100 same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100 adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt adj_freq_intersource_factor   "  
		+"FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt   "  
		+"from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance   "  
		+"FROM (  "  
		+"SELECT ncs.*,cellinfo.tch as celltch,cellinfo.longitude as lng1,cellinfo.latitude as lat1,cellinfo.lnglats as lnglats,  "  
		+"ncellinfo.tch as ncelltch,ncellinfo.longitude as lng2,ncellinfo.latitude as lat2  "  
		+"from  "  
		+"(  "  
		+"select cell,ncell,  "  
		+"sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi,  "  
		+"sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi   "  
		+"from RNO_NCS WHERE rno_ncs_desc_id="+ncsDescId+" group by cell,NCELL having SUM(REPARFCN)>0 "  
		+")ncs  "  
		+"left JOIN  "  
		+"(  "  
		+"  select mid_cell.* from   "  
		+"  (  "  
		+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell  "  
		+"  )mid_cell  "  
		+"  where rn=1  "  
		+")cellinfo  "  
		+"ON  "  
		+"(  "  
		+"ncs.cell=cellinfo.label  "  
		+")  "  
		+"left JOIN  "  
		+"(  "  
		+"select mid_cell.* from   "  
		+"  (  "  
		+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell  "  
		+"  )mid_cell  "  
		+"  where rn=1  "  
		+")ncellinfo  "  
		+"on(ncs.ncell=ncellinfo.label)  "  
		+") dist) t) t1 WHERE T1.ncellfreqcnt<>0  and t1.cellfreqcnt>0) t2) t GROUP BY t.cell,t.lnglats ORDER BY cellinteredcoeffisum DESC) t) p where p.\"row_number\" > "+startIndex+") q where rownum <= "+cnt  ;

		log.info("分页查询ncscell data 的查询语句：" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						// String sql = "SELECT t.CELL,sum(t.cellinteredcoeffi)
						// cellinteredcoeffisum from (SELECT
						// t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt
						// cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt
						// ncellintersourcecoeffi from (SELECT
						// t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100
						// same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100
						// adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt
						// same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt
						// adj_freq_intersource_factor FROM (SELECT
						// t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH)
						// cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH,
						// t.NCELLTCH)
						// adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch)
						// ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt
						// from (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1,
						// dist.LNG2, dist.LAT2) distance FROM (select
						// CELL,NCELL,sum(TIMESRELSS)/sum(REPARFCN)
						// same_freq_inter_coeffi,sum(TIMESRELSS2)/sum(REPARFCN)
						// adj_freq_inter_coeffi,count(*) 数量,(SELECT TCH FROM
						// CELL WHERE LABEL=CELL) celltch,(SELECT TCH FROM CELL
						// WHERE LABEL=NCELL) ncelltch,(SELECT LONGITUDE FROM
						// CELL WHERE LABEL=CELL) lng1,(SELECT LATITUDE FROM
						// CELL WHERE LABEL=CELL) lat1,(SELECT LONGITUDE FROM
						// CELL WHERE LABEL=NCELL) lng2,(SELECT LATITUDE FROM
						// CELL WHERE LABEL=NCELL) lat2 from RNO_NCS WHERE
						// rno_ncs_desc_id=? group by cell,NCELL) dist) t) t1
						// WHERE T1.ncellfreqcnt<>0) t2) t GROUP BY t.cellSELECT
						// t.CELL,sum(t.cellinteredcoeffi) cellinteredcoeffisum
						// from (SELECT
						// t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt
						// cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt
						// ncellintersourcecoeffi from (SELECT
						// t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100
						// same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100
						// adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt
						// same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt
						// adj_freq_intersource_factor FROM (SELECT
						// t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH)
						// cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH,
						// t.NCELLTCH)
						// adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch)
						// ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt
						// from (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1,
						// dist.LNG2, dist.LAT2) distance FROM (select
						// CELL,NCELL,sum(TIMESRELSS)/sum(REPARFCN)
						// same_freq_inter_coeffi,sum(TIMESRELSS2)/sum(REPARFCN)
						// adj_freq_inter_coeffi,count(*) 数量,(SELECT TCH FROM
						// CELL WHERE LABEL=CELL) celltch,(SELECT TCH FROM CELL
						// WHERE LABEL=NCELL) ncelltch,(SELECT LONGITUDE FROM
						// CELL WHERE LABEL=CELL) lng1,(SELECT LATITUDE FROM
						// CELL WHERE LABEL=CELL) lat1,(SELECT LONGITUDE FROM
						// CELL WHERE LABEL=NCELL) lng2,(SELECT LATITUDE FROM
						// CELL WHERE LABEL=NCELL) lat2 from RNO_NCS WHERE
						// rno_ncs_desc_id=824 group by cell,NCELL) dist) t) t1
						// WHERE T1.ncellfreqcnt<>0) t2) t GROUP BY t.cell ORDER
						// BY cellinteredcoeffisum DESC";
						SQLQuery query = arg0.createSQLQuery(sql);
						//query.setLong(0, ncsDescId);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出getTotalAnalysisGisCellByNcsDescId");
						return query.list();
					}
				});
	}
	/**
	 * 通过ncs描述ID获取NCS干扰总数量
	 * @title 
	 * @param ncsDescId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-24下午06:06:36
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNcsInfoCount(final long ncsDescId) {
		log.info("进入getNcsInfoCount(final long ncsDescId)" + ncsDescId);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				// TODO Auto-generated method stub
				//String sql = "SELECT count(*) count from (SELECT t.CELL,sum(t.cellinteredcoeffi) cellinteredcoeffisum from (SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100 same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100 adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance FROM (select CELL,NCELL,sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi,sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi,count(*) 数量,(SELECT DISTINCT(TCH) FROM CELL WHERE LABEL=CELL) celltch,(SELECT DISTINCT(TCH) FROM CELL WHERE LABEL=NCELL) ncelltch,(SELECT DISTINCT(LONGITUDE) FROM CELL WHERE LABEL=CELL) lng1,(SELECT DISTINCT(LATITUDE) FROM CELL WHERE LABEL=CELL) lat1,(SELECT DISTINCT(LONGITUDE) FROM CELL WHERE LABEL=NCELL) lng2,(SELECT DISTINCT(LATITUDE) FROM CELL WHERE LABEL=NCELL) lat2 from RNO_NCS WHERE rno_ncs_desc_id=? group by cell,NCELL) dist) t) t1 WHERE T1.ncellfreqcnt<>0) t2) t GROUP BY t.cell ORDER BY cellinteredcoeffisum DESC)";
				String sql="SELECT count(*) count from (SELECT t.CELL,sum(t.cellinteredcoeffi) cellinteredcoeffisum from (SELECT t2.*,(same_freq_intered_factor+adj_freq_intered_factor)/T2.cellfreqcnt cellinteredcoeffi,(same_freq_intered_factor+adj_freq_intered_factor)/T2.ncellfreqcnt ncellintersourcecoeffi from (SELECT t1.*,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.ncellfreqcnt*100 same_freq_intered_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.ncellfreqcnt*100 adj_freq_intered_factor,t1.same_freq_inter_coeffi*t1.cofreqcnt/t1.cellfreqcnt same_freq_intersource_factor,t1.adj_freq_inter_coeffi*t1.adjacentfreqcnt/t1.cellfreqcnt adj_freq_intersource_factor FROM (SELECT t.*,RNO_GETCOFREQCNT(t.CELLTCH, t.NCELLTCH) cofreqcnt,RNO_GETADJACENTFREQCNT(t.CELLTCH, t.NCELLTCH) adjacentfreqcnt,RNO_GET_FREQ_CNT(t.ncelltch) ncellfreqcnt,RNO_GET_FREQ_CNT(t.celltch) cellfreqcnt from  (SELECT dist.*,GETDISTANCE(dist.LNG1, dist.LAT1, dist.LNG2, dist.LAT2) distance FROM "
				+"( "
				+"	SELECT ncs.*,cellinfo.tch as celltch,cellinfo.longitude as lng1,cellinfo.latitude as lat1,cellinfo.lnglats as lnglats, "
				+"ncellinfo.tch as ncelltch,ncellinfo.longitude as lng2,ncellinfo.latitude as lat2 "
				+"from "
				+"( "
				+"select cell,ncell, "
				+"sum(TIMESRELSS)/sum(REPARFCN) same_freq_inter_coeffi, "
				+"sum(TIMESRELSS2)/sum(REPARFCN) adj_freq_inter_coeffi  "
				+"from RNO_NCS WHERE rno_ncs_desc_id="+ncsDescId+" group by cell,NCELL having SUM(REPARFCN)>0"
				+")ncs "
				+"left JOIN "
				+"( "
				+"  select mid_cell.* from  "
				+"  ( "
				+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell "
				+"  )mid_cell "
				+"  where rn=1 "
				+")cellinfo "
				+"ON "
				+"( "
				+"ncs.cell=cellinfo.label "
				+") "
				+" "
				+"left JOIN "
				+"( "
				+"select mid_cell.* from  "
				+"  ( "
				+"      select label,tch,longitude,latitude,lnglats,row_number() over(partition by label order by tch nulls last) rn from cell "
				+"  )mid_cell "
				+"  where rn=1 "
				+")ncellinfo "
				+"on(ncs.ncell=ncellinfo.label) "
				+") dist) t) t1 WHERE T1.ncellfreqcnt<>0  and t1.cellfreqcnt>0) t2) t GROUP BY t.cell ORDER BY cellinteredcoeffisum DESC) ";
				SQLQuery query = arg0.createSQLQuery(sql);
				//query.setLong(0, ncsDescId);
				List resList = query.list();
				long cellcount = 0;
				if (resList != null) {
					cellcount = Long.parseLong(resList.get(0).toString());
				}
				log.info("退出getNcsInfoCount cellcount：" + cellcount);
				return cellcount;
			}
		});

	}
	/**
	 * 通过ncsid获取ncs描述表的区域信息[city,area]
	 * @title 
	 * @param selectId
	 * @return
	 * @author chao.xj
	 * @date 2014-4-29下午01:47:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getNcsAreaInfoByNcsId(
			final long selectId) {
		log.info("进入getNcsAreaInfoByNcsId(final long selectId)"+selectId);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						String sql = "SELECT (SELECT name from area WHERE id=city_id) city,(SELECT name from area WHERE id=AREA_ID) area from RNO_NCS_DESCRIPTOR   WHERE rno_ncs_desc_id=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, selectId);
						log.info("sql:"+sql);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("进入getNcsAreaInfoByNcsId");
						return query.list();
					}
				});
	}
	

	/**
	 * 根据条件查询某个小区的冗余邻区
	 * @author Liang YJ
	 * 2014-4-30 上午10:44:03
	 * @param cell
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 * 
	 */
	/*private List<RedundantNCell> getRedundantNcell(String cell, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getRedundantNcell。cell=" + cell + ",handoverDescIds=" + handoverDescIds
				+ ",condition=" + condition+",areaId"+areaId);
		

		final double times = 1.6;//主小区理想覆盖距离的倍数
		//判断是否有小区切换数据
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS, ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql += " from (select * from (select "+getNcsField()+" from "+ncsTab+" where CELL=? and "+getCellSql(ncsTab, "CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ? ) rnm inner join ("
			+ "select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where CELL=? and "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?) ";
		
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "inner join (select * from(select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where SERVER_CELL=? and RNO_HANDOVER_DESC_ID in(";
			StringBuffer sb = new StringBuffer(sql);
			for(int i=0; i < handoverDescIds.size(); i++)
			{
				sb.append("?,"); 
			}
			sql = sb.deleteCharAt(sb.length()-1).toString();
			sql += ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? ) handOver on (rnm.NCELL = handOver.TARGET_CELL)";
		}
		log.info("查找冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		ResultSet resultSet  = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, cell);
			pStatement.setLong(2, areaId);
			pStatement.setDouble(3, condition.getMinNavss());
			pStatement.setDouble(4, condition.getMinDetectRatio());
			pStatement.setString(5, cell);
			pStatement.setLong(6, areaId);
			pStatement.setDouble(7, times);
			if(null!=handoverDescIds && 0<handoverDescIds.size())
			{
				pStatement.setString(8, cell);
				int index = 9;
				for(int i=0; i<handoverDescIds.size(); i++)
				{
					pStatement.setLong(index++, handoverDescIds.get(i));
					
				}
				pStatement.setLong(index++, areaId);
				pStatement.setDouble(index,condition.getMinHoverCnt());
			}
			resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			
		} catch (SQLException e)
		{
			log.error("查询冗余邻区失败");
			log.error(e.getStackTrace());
		}

		log.info(cell+"的冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	

	*//**
	 * 根据条件查询某个小区的冗余邻区
	 * @author Liang YJ
	 * 2014-4-30 上午10:53:48
	 * @param cell
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getRedundantNcell(String cell, List<Long> ncsDescIds,  List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getRedundantNcell。cell=" + cell + ",ncsDescIds="+ncsDescIds+",handoverDescIds=" + handoverDescIds
				+ ",condition=" + condition+",areaId"+areaId);
		

		final double times = 1.6;//主小区理想覆盖距离的倍数

		//判断是否有小区切换数据
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS, ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql += " from (select * from (select "+getNcsField()+" from "+ncsTab+" where CELL=? and RNO_NCS_DESC_ID in (";
		
		StringBuffer sb = new StringBuffer(sql);
		for(int i=0; i<ncsDescIds.size(); i++)
		{
			sb.append("?,");
		}
		sql = sb.deleteCharAt(sb.length()-1).toString();
		sql += ") and "+getCellSql(ncsTab, "CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ? ) rnm inner join ("
				+ "select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where CELL=? and "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?) ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "inner join (select * from(select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where SERVER_CELL=? and RNO_HANDOVER_DESC_ID in(";
			sb = new StringBuffer(sql);
			for(int i=0; i < handoverDescIds.size(); i++)
			{
				sb.append("?,"); 
			}
			sql = sb.deleteCharAt(sb.length()-1).toString();
			sql += ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? ) handOver on (rnm.NCELL = handOver.TARGET_CELL)";
		}
		log.info("查找冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index = 0;
			pStatement.setString(++index, cell);
			for(int i=0;i<ncsDescIds.size();i++)
			{
				pStatement.setLong(++index, ncsDescIds.get(i));
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			pStatement.setString(++index, cell);
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			if(null!=handoverDescIds && 0<handoverDescIds.size())
			{
				pStatement.setString(++index, cell);
				for(int j=0; j<handoverDescIds.size(); j++)
				{
					pStatement.setLong(++index, handoverDescIds.get(j));
				}
				pStatement.setLong(++index, areaId);
				pStatement.setDouble(++index, condition.getMinHoverCnt());
			}
			
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
		} catch (SQLException e)
		{
			log.error("查询冗余邻区失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
		}

		log.info(cell+"的冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件查询某个小区的漏定邻区
	 * @author Liang YJ
	 * 2014-4-30 上午10:58:33
	 * @param cell
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getOmitNcell(final String cell, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getOmitNcell。cell=" + cell + ",condition=" + condition + ",areaId="+areaId);

		final double times = 5;//主小区理想覆盖距离的倍数

		String sql = "select '"+RnoConstant.DataType.OmitNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.DISTANCE, rnm.NAVSS, rnm.DETECT_RATIO, rncarm.EXPECTED_COVER_DIS, -1 as HOVERCNT from " 
					+ "(select * from (select "+getNcsField()+" from "+ncsTab+" where CELL=? and "+getCellSql(ncsTab, "CELL")+" and not "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL ) where NAVSS  <  ? and DETECT_RATIO  >  ?) rnm inner join " 
					+ "(select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where CELL=? and "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.DISTANCE  <  rncarm.EXPECTED_COVER_DIS*?)";
		// 
		log.info("查找漏定邻区sql: "+sql);
		PreparedStatement pStatement = null;
		ResultSet resultSet  = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, cell);
			pStatement.setLong(2, areaId);
			pStatement.setDouble(3, condition.getMinNavss());
			pStatement.setDouble(4, condition.getMinDetectRatio());
			pStatement.setString(5, cell);
			pStatement.setLong(6, areaId);
			pStatement.setDouble(7, times);
			resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			
		} catch (SQLException e)
		{
			log.error("查询漏定邻区失败");
			log.error(e.getStackTrace());
			//e.printStackTrace();
		}

		log.info(cell+"的漏定邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件查询某小区的漏定邻区
	 * @author Liang YJ
	 * 2014-4-30 上午11:01:17
	 * @param cell
	 * @param ncsDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getOmitNcell(final String cell, List<Long> ncsDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getOmitNcell。cell=" + cell + ",ncsDescIds="+ncsDescIds+",condition=" + condition + ",areaId="+areaId);

		final double times = 5;//主小区理想覆盖距离的倍数

		String sql = "select '"+RnoConstant.DataType.OmitNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.DISTANCE, rnm.NAVSS, rnm.DETECT_RATIO, rncarm.EXPECTED_COVER_DIS, -1 as HOVERCNT from " 
					+ "(select * from (select "+getNcsField()+" from "+ncsTab+" where CELL=? and RNO_NCS_DESC_ID in (";
		StringBuffer sb = new StringBuffer(sql);
		for(int i=0; i<ncsDescIds.size(); i++)
		{
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sql = sb.toString()+") and "+getCellSql(ncsTab, "CELL")+" and not "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS  <  ? and DETECT_RATIO  >  ?) rnm inner join " 
					+ "(select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where CELL=? and "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.DISTANCE  <  rncarm.EXPECTED_COVER_DIS*?)";
		log.info("查找漏定邻区sql: "+sql);
		PreparedStatement pStatement = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index=0;
			pStatement.setString(++index, cell);
			for(int i=0;i<ncsDescIds.size();i++)
			{
				pStatement.setLong(++index, ncsDescIds.get(i));
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			pStatement.setString(++index, cell);
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
			
		} catch (SQLException e)
		{
			log.error("查询漏定邻区失败");
			log.error(e.getStackTrace());
		}

		log.info(cell+"的漏定邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件全网分析冗余邻区
	 * @author Liang YJ
	 * 2014-4-30 上午11:06:16
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getAllRedundantCellsByCondition(List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入dao方法getAllRedundantCellsByCondition： handoverDescIds="+handoverDescIds+",condition="+condition+",areaId="+areaId);

		final double times = 1.6;//主小区理想覆盖距离的倍数
		
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS,rnm.DETECT_RATIO,rnm.DISTANCE,rncarm.EXPECTED_COVER_DIS, ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql	+= " from (select * from (select "+getNcsField()+" from "+ncsTab+" where "+getCellSql(ncsTab,"CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ?) rnm "
			+ "inner join (select CELL,EXPECTED_COVER_DIS from "+cellResTab+" where "+getCellSql(cellResTab,"CELL")+") rncarm on (rnm.CELL=rncarm.CELL and rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?)" ;

		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += " inner join (select * from (select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where RNO_HANDOVER_DESC_ID in(";
			StringBuffer sb = new StringBuffer(sql);
			for(int i=0; i<handoverDescIds.size(); i++)
			{
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sql = sb.toString() + ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? )  handOver on (rnm.CELL = handOver.SERVER_CELL and rnm.NCELL = handOver.TARGET_CELL)";	
		}
		sql += " order by rnm.CELL";
		log.info("全网分析冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		ResultSet resultSet  = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			pStatement.setLong(1, areaId);
			pStatement.setDouble(2, condition.getMinNavss());
			pStatement.setDouble(3, condition.getMinDetectRatio());
			pStatement.setLong(4, areaId);
			pStatement.setDouble(5, times);
			if(null!=handoverDescIds && 0<handoverDescIds.size())
			{
				int index = 6;
				for(int i=0; i<handoverDescIds.size(); i++)
				{
					pStatement.setLong(index, handoverDescIds.get(i));
					index++;
				}
				pStatement.setLong(index++, areaId);
				pStatement.setDouble(index,condition.getMinHoverCnt());
			}
			resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
			
		} catch (SQLException e)
		{
			log.error("全网分析冗余邻区失败");
			log.error(e.getStackTrace());
		}

		log.info("全网冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件全网分析冗余邻区
	 * @author Liang YJ
	 * 2014-4-30 上午11:09:21
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getAllRedundantCellsByCondition(List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入dao方法getAllRedundantCellsByCondition： ncsDescIds="+ncsDescIds+"handoverDescIds="+handoverDescIds+"condition="+condition+",areaId="+areaId);

		final double times = 1.6;//主小区理想覆盖距离的倍数
		
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS,rnm.DETECT_RATIO,rnm.DISTANCE,rncarm.EXPECTED_COVER_DIS, ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql	+= " from (select * from (select "+getNcsField()+" from "+ncsTab+" where RNO_NCS_DESC_ID in(";
		
		StringBuffer sb = new StringBuffer(sql);
		for(int i=0; i<ncsDescIds.size(); i++)
		{
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sql = sb.toString()+") and "+getCellSql(ncsTab,"CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ?) rnm "
			+ "inner join (select CELL,EXPECTED_COVER_DIS from "+cellResTab+" where "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.CELL=rncarm.CELL and rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?)" ;
		
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += " inner join (select * from (select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where RNO_HANDOVER_DESC_ID in(";
			sb = new StringBuffer(sql);
			for(int i=0; i<handoverDescIds.size(); i++)
			{
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sql = sb.toString() + ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? )  handOver on (rnm.CELL = handOver.SERVER_CELL and rnm.NCELL = handOver.TARGET_CELL)";	
		}
		sql += " order by rnm.CELL";
		log.info("全网分析冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index=0;
			for(int i=0 ;i<ncsDescIds.size(); i++)
			{
				pStatement.setLong(++index, ncsDescIds.get(i));
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			if(null!=handoverDescIds && 0<handoverDescIds.size())
			{
				for(int j=0; j<handoverDescIds.size(); j++)
				{
					pStatement.setLong(++index, handoverDescIds.get(j));
				}
				pStatement.setLong(++index, areaId);
				pStatement.setDouble(++index, condition.getMinHoverCnt());
			}
			
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
			
		} catch (SQLException e)
		{
			log.error("全网分析冗余邻区失败");
			log.error(e.getStackTrace());
		}

		log.info("全网冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件全网分析漏定邻区
	 * @author Liang YJ
	 * 2014-4-30 上午11:11:00
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getAllOmitCellsByCondition(NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入dao方法getAllOmitCellsByCondition： ,areaId="+areaId+",condition="+condition);
		
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		final double times = 5;//主小区理想覆盖距离的倍数

		String sql = "select '"+RnoConstant.DataType.OmitNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS , -1 as HOVERCNT from "
					+ " (select * from (select "+getNcsField()+" from "+ncsTab+" where "+getCellSql(ncsTab, "CELL")+" and not "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS < ? and DETECT_RATIO > ?) rnm inner join "
					+ " (select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.CELL=rncarm.CELL and rnm.DISTANCE < rncarm.EXPECTED_COVER_DIS*?) order by rnm.CELL";
		log.info("全网分析漏定邻区sql: "+sql);
		PreparedStatement pStatement = null;
		ResultSet resultSet  = null;
		try
		{
			pStatement = conn.prepareStatement(sql);
			pStatement.setLong(1, areaId);
			pStatement.setDouble(2, condition.getMinNavss());
			pStatement.setDouble(3, condition.getMinDetectRatio());
			pStatement.setLong(4, areaId);
			pStatement.setDouble(5, times);
			resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
		} catch (SQLException e)
		{
			log.error("全网分析漏定邻区失败");
			log.error(e.getStackTrace());
		}

		log.info("全网漏定邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	*//**
	 * 根据条件全网分析漏定邻区
	 * @author Liang YJ
	 * 2014-4-30 上午11:13:37
	 * @param ncsDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 *//*
	private List<RedundantNCell> getAllOmitCellsByCondition(List<Long> ncsDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入dao方法getAllOmitCellsByCondition：ncsDescIds="+ncsDescIds+",areaId="+areaId+",condition="+condition);
		
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		final double times = 5;//主小区理想覆盖距离的倍数
		
		String sql = "select '"+RnoConstant.DataType.OmitNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS , -1 as HOVERCNT from "
					+ " (select * from (select "+getNcsField()+" from "+ncsTab+" where RNO_NCS_DESC_ID in (";
		StringBuffer sb = new StringBuffer(sql);
		for(int i=0; i<ncsDescIds.size(); i++)
		{
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sql = sb.toString()+") and "+getCellSql(ncsTab, "CELL")+" and not "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS < ? and DETECT_RATIO > ?) rnm inner join "
					+ " (select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.CELL=rncarm.CELL and rnm.DISTANCE < rncarm.EXPECTED_COVER_DIS*?) order by rnm.CELL";
		log.info("全网分析漏定邻区sql: "+sql);
		PreparedStatement pStatement = null;
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index=0;
			for(int i=0;i<ncsDescIds.size();i++)
			{
				pStatement.setLong(++index, ncsDescIds.get(i));
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
		} catch (SQLException e)
		{
			log.error("全网分析漏定邻区失败");
			log.error(e.getStackTrace());
		}

		log.info("全网漏定邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}*/
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-5-7 下午4:51:55
	 * @param cell
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 */
/*	private List<RedundantNCell> getRedundantNcellByCondition(String cell, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getRedundantNcell。cell=" + cell + ",handoverDescIds=" + handoverDescIds
				+ ",condition=" + condition+",areaId"+areaId);
		

		final double times = 1.6;//主小区理想覆盖距离的倍数
		boolean cellFlag = null != cell && !"".equals(cell.trim());
		//判断是否有小区切换数据
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS, ";
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql += " from (select * from (select "+getNcsField()+" from "+ncsTab+" where ";
		if(cellFlag){
			sql += "CELL=?";
		}
		sql += " and "+getCellSql(ncsTab, "CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ? ) rnm inner join ("
			+ "select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where ";
		if(cellFlag){
			sql += "CELL=?";
		}
		sql +=" and "+getCellSql(cellResTab, "CELL")+") rncarm on (rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?) ";
		
		if(null!=handoverDescIds && 0<handoverDescIds.size())
		{
			sql += "inner join (select * from(select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where ";
			if(cellFlag){
				sql += "SERVER_CELL=?";
			}
			sql += " and RNO_HANDOVER_DESC_ID in(";
			StringBuffer sb = new StringBuffer(sql);
			for(int i=0; i < handoverDescIds.size(); i++)
			{
				sb.append("?,"); 
			}
			sql = sb.deleteCharAt(sb.length()-1).toString();
			sql += ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? ) handOver on (rnm.NCELL = handOver.TARGET_CELL)";
		}
		log.info("查找冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		ResultSet resultSet  = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index = 0;
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			if(null!=handoverDescIds && 0<handoverDescIds.size())
			{
				if(cellFlag){				
					pStatement.setString(++index, cell);
				}
				for(int i=0; i<handoverDescIds.size(); i++)
				{
					pStatement.setLong(++index, handoverDescIds.get(i));
					
				}
				pStatement.setLong(++index, areaId);
				pStatement.setDouble(++index,condition.getMinHoverCnt());
			}
			resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			
		} catch (SQLException e)
		{
			log.error("查询冗余邻区失败");
			log.error(e.getStackTrace());
		}

		log.info(cell+"的冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}*/
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-5-7 下午5:02:19
	 * @param cell
	 * @param ncsDescIds
	 * @param handoverDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 */
	private List<RedundantNCell> getRedundantNcellsByCondition(String cell, List<Long> ncsDescIds,  List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getRedundantNcell。cell=" + cell + ",ncsDescIds="+ncsDescIds+",handoverDescIds=" + handoverDescIds
				+ ",condition=" + condition+",areaId"+areaId);
		

		final double times = 1.6;//主小区理想覆盖距离的倍数
		boolean cellFlag = null != cell && !"".equals(cell.trim());
		boolean ncsFlag = null != ncsDescIds && 0 != ncsDescIds.size();
		boolean handoverFlag = null != handoverDescIds && 0 != handoverDescIds.size();
		StringBuffer sb = null;
		//判断是否有小区切换数据
		String sql = "select '"+RnoConstant.DataType.RedundantNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.NAVSS, rnm.DETECT_RATIO, rnm.DISTANCE, rncarm.EXPECTED_COVER_DIS, ";
		if(handoverFlag)
		{
			sql += "handOver.HOVERCNT"; 
		}else
		{
			sql += "-1 as HOVERCNT";
		}
		
		sql += " from (select * from (select "+getNcsField()+" from "+ncsTab+" where ";
		if(cellFlag){
			sql += "CELL=? and ";
		}
		if(ncsFlag){			
			sql += "RNO_NCS_DESC_ID in (";
			
			sb = new StringBuffer(sql);
			for(int i=0; i<ncsDescIds.size(); i++)
			{
				sb.append("?,");
			}
			sql = sb.deleteCharAt(sb.length()-1).toString() + ") and ";
		}
		sql += getCellSql(ncsTab, "CELL")+" and "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS > ? and DETECT_RATIO < ? ) rnm inner join ("
				+ "select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where ";
		if(cellFlag){
			sql += "CELL=? and ";
		}
		sql += getCellSql(cellResTab, "CELL")+") rncarm on (";
		if(!cellFlag){
			sql += "rnm.CELL=rncarm.CELL and ";
		}
		sql += "rnm.DISTANCE > rncarm.EXPECTED_COVER_DIS*?) ";
		if(handoverFlag)
		{
			sql += "inner join (select * from(select SERVER_CELL,TARGET_CELL,avg(HOVERCNT) as HOVERCNT from RNO_HANDOVER where ";
			if(cellFlag){
				sql += "SERVER_CELL=? and ";
			}
			sql += "RNO_HANDOVER_DESC_ID in(";
			sb = new StringBuffer(sql);
			for(int i=0; i < handoverDescIds.size(); i++)
			{
				sb.append("?,"); 
			}
			sql = sb.deleteCharAt(sb.length()-1).toString();
			sql += ") and "+getCellSql("RNO_HANDOVER","SERVER_CELL")+" and "+getNcellSql("RNO_HANDOVER", "SERVER_CELL", "TARGET_CELL")+" group by SERVER_CELL,TARGET_CELL) where HOVERCNT < ? ) handOver on (";
			if(!cellFlag){
				sql += "rnm.CELL = handOver.SERVER_CELL and ";
			}
			sql += "rnm.NCELL = handOver.TARGET_CELL)";
		}
		if(!cellFlag){
			sql += " order by rnm.CELL";
		}
		
		log.info("查找冗余邻区sql: "+sql);
		PreparedStatement pStatement = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index = 0;
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			if(ncsFlag){	
				for(int i=0;i<ncsDescIds.size();i++)
				{
					pStatement.setLong(++index, ncsDescIds.get(i));
				}
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			if(handoverFlag)
			{
				if(cellFlag){				
					pStatement.setString(++index, cell);
				}
				for(int j=0; j<handoverDescIds.size(); j++)
				{
					pStatement.setLong(++index, handoverDescIds.get(j));
				}
				pStatement.setLong(++index, areaId);
				pStatement.setDouble(++index, condition.getMinHoverCnt());
			}
			
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
		} catch (SQLException e)
		{
			log.error("查询冗余邻区失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
		}

		log.info("冗余邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-5-7 下午5:30:07
	 * @param cell
	 * @param ncsDescIds
	 * @param condition
	 * @param areaId
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @return
	 */
	private List<RedundantNCell> getOmitNcellsByCondition(String cell, List<Long> ncsDescIds, NcellAnalysisCondition condition, long areaId, Connection conn, String ncsTab, String cellResTab)
	{
		log.info("进入：getOmitNcell。cell=" + cell + ",ncsDescIds="+ncsDescIds+",condition=" + condition + ",areaId="+areaId);

		final double times = 5;//主小区理想覆盖距离的倍数
		boolean cellFlag = null != cell && !"".equals(cell.trim());
		boolean ncsFlag = null != ncsDescIds && 0 != ncsDescIds.size();
		StringBuffer sb = null;
		String sql = "select '"+RnoConstant.DataType.OmitNcellType + "' as NCELLTYPE, rnm.CELL, rnm.NCELL, rnm.DISTANCE, rnm.NAVSS, rnm.DETECT_RATIO, rncarm.EXPECTED_COVER_DIS, -1 as HOVERCNT from " 
					+ "(select * from (select "+getNcsField()+" from "+ncsTab+" where ";
		if(cellFlag){
			sql += "CELL=? and ";
		}
		if(ncsFlag){			
			sql += "RNO_NCS_DESC_ID in (";
			
			sb = new StringBuffer(sql);
			for(int i=0; i<ncsDescIds.size(); i++)
			{
				sb.append("?,");
			}
			sql = sb.deleteCharAt(sb.length()-1).toString() + ") and ";
		}
		sql += getCellSql(ncsTab, "CELL")+" and not "+getNcellSql(ncsTab, "CELL", "NCELL")+" group by CELL,NCELL) where NAVSS  <  ? and DETECT_RATIO  >  ?) rnm inner join " 
					+ "(select CELL, EXPECTED_COVER_DIS from "+cellResTab+" where ";
		if(cellFlag){
			sql += "CELL=? and ";
		}
		sql += getCellSql(cellResTab, "CELL")+") rncarm on (";
		if(!cellFlag){
			sql += "rnm.CELL=rncarm.CELL and ";
		}
		sql += "rnm.DISTANCE < rncarm.EXPECTED_COVER_DIS*?)";
		if(!cellFlag){
			sql += "order by rnm.CELL";
		}
		log.info("查找漏定邻区sql: "+sql);
		PreparedStatement pStatement = null;
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			pStatement = conn.prepareStatement(sql);
			int index=0;
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			if(ncsFlag){	
				for(int i=0;i<ncsDescIds.size();i++)
				{
					pStatement.setLong(++index, ncsDescIds.get(i));
				}
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, condition.getMinNavss());
			pStatement.setDouble(++index, condition.getMinDetectRatio());
			if(cellFlag){				
				pStatement.setString(++index, cell);
			}
			pStatement.setLong(++index, areaId);
			pStatement.setDouble(++index, times);
			ResultSet resultSet = pStatement.executeQuery();
			redundantNCells = getRedundantNCellsFromResultSet(resultSet);
			pStatement.close();
			
		} catch (SQLException e)
		{
			log.error("查询漏定邻区失败");
			log.error(e.getStackTrace());
		}

		log.info("漏定邻区的个数："+redundantNCells.size());
		//log.info("redundantNCells: "+redundantNCells);
		return redundantNCells;
	}

	
	/**
	 * 据ncsDescIds将ncs测量数据转移到rno_ncs_mid中，并匹配邻区，匹配室分，计算干扰度以及理想覆盖距离
	 * @author Liang YJ
	 * 2014-4-30 上午11:18:58
	 * @param conn
	 * @param ncsDescIds
	 * @param cityId
	 * @param ncsTab
	 * @param cellResTab
	 * @param saveNcsId
	 */
	private void calculateNcs(Statement stmt, List<Long> ncsDescIds, String ncsTab, String cellResTab, long saveNcsId)
	{
		System.out.println("进入calculateNcs: stmt:"+stmt+",ncsDescIds: "+ncsDescIds+",ncsTab="+ncsTab+",cellResTab="+cellResTab+",saveNcsId: "+saveNcsId);
		List<Long> staticNcsIds = new ArrayList<Long>();
		staticNcsIds.add(saveNcsId);
//		Statement stmt = null;
//		try
//		{
//			stmt = conn.createStatement();
//		} catch (SQLException e)
//		{
//			log.error("创建statement失败");
//			log.error(e.getStackTrace());
//		}
		boolean flag = false;
		log.info("将ncs测量数据转移到rno_ncs_mid中，ncsDescIds = "+ ncsDescIds);
		flag = rnoStructureAnalysisDao.transferNcsToTempTable(stmt, ncsDescIds, saveNcsId,null,null);
		//log.info("邻区匹配,cityId="+cityId);
		//Map<Long,Boolean> result = rnoStructureAnalysisDao.matchNcsNcell(conn, ncsTab,staticNcsIds, cityId);
		log.info("匹配室分");
		flag = rnoStructureAnalysisDao.markIsIndoorFlag(stmt);
		log.info("计算干扰度");
		flag = rnoStructureAnalysisDao.calculateInterfer(stmt, ncsTab,staticNcsIds,"-12","+3");
		log.info("计算理想覆盖距离");
		String sql = "insert into "
				+ cellResTab
				+ " (CELL,NCS_ID,FREQ_CNT) SELECT CELL,"+saveNcsId+",min(CELL_FREQ_CNT) FROM "
				+ ncsTab + " where RNO_NCS_DESC_ID=" + saveNcsId
				+ " group by cell";
		log.info("将ncs数据[" + ncsTab + "]转移到小区分析结果表[" + cellResTab
				+ "]的sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("转移小区相关数据到小区分析结果表时出错！");
//			try {
//				stmt.close();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
		}
		int lowestN = 3;//选取离主小区120°内最近的三个邻小区计算
		rnoStructureAnalysisDao.calculateCellIdealCoverDis(stmt, ncsTab, saveNcsId, cellResTab, lowestN);
	}
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午12:50:04
	 * @param resultSet
	 * @return redundantNCells
	 * @description 将从数据库查询出的所有冗余或漏定邻区的记录封装成List<RedundantNCell>对象
	 */
	private List<RedundantNCell> getRedundantNCellsFromResultSet(ResultSet resultSet)
	{
		List<RedundantNCell> redundantNCells = new ArrayList<RedundantNCell>();
		try
		{
			while(resultSet.next())
			{
				redundantNCells.add(getRedundantNCellFromResultSet(resultSet));
			}
		} catch (SQLException e)
		{
			log.error(e.getStackTrace());
		}
		return redundantNCells;
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * 2014-4-21 下午12:44:42
	 * @param resultSet
	 * @return redundantNcell
	 * @description 将一条由数据库查询得到的冗余或漏定邻区的记录封装成一个RedundantNCELL对象
	 */
	private RedundantNCell getRedundantNCellFromResultSet(ResultSet resultSet)
	{
		RedundantNCell redundantNCell = new RedundantNCell();
		try
		{
			redundantNCell.setCell(resultSet.getString("CELL"));
			redundantNCell.setNcell(resultSet.getString("NCELL"));
			redundantNCell.setNcellType(resultSet.getString("NCELLTYPE"));
			redundantNCell.setDistance(resultSet.getDouble("DISTANCE"));
			redundantNCell.setExpectedCoverDis(resultSet.getDouble("EXPECTED_COVER_DIS"));
			redundantNCell.setNavss(resultSet.getDouble("NAVSS"));
			redundantNCell.setDetectRatio(resultSet.getDouble("DETECT_RATIO"));
			redundantNCell.setHovercnt(resultSet.getDouble("HOVERCNT"));
		} catch (SQLException e)
		{
			log.error(e.getStackTrace());
		}
		return redundantNCell;
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
	 * @return result
	 * @description 根据参数查询冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>> getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入dao方法  getRedundantAndOmitNcellofCell cell="+cell+",ncsDescIds="+ncsDescIds+",handoverDescIds="+handoverDescIds+"condition="+condition+"areaId="+areaId);
		Connection conn = DataSourceConn.initInstance().getConnection();
		
		try
		{
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			log.error("connection设置非自动提交事务失败");
			log.error(e.getStackTrace());
		}
		Statement stmt=null;
		try {
			stmt=conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		//邻区匹配、室分匹配、计算干扰度、理想覆盖距离
		final long saveNcsId = 100l;//rno_ncs_mid的临时id,代替ncs数据原有的描述id
		String ncsTab = "RNO_NCS_MID";
		String cellResTab = "RNO_NCS_CELL_ANA_RESULT_MID";
		calculateNcs(stmt, ncsDescIds, ncsTab,cellResTab, saveNcsId);
		Map<String,List<RedundantNCell>> result = new HashMap<String, List<RedundantNCell>>();
		/*List<RedundantNCell> redundantNCells = getRedundantNcell(cell, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcell(cell, condition, areaId, conn, ncsTab, cellResTab);*/
		List<RedundantNCell> redundantNCells = getRedundantNcellsByCondition(cell, null, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcellsByCondition(cell, null, condition, areaId, conn, ncsTab, cellResTab);
		
		try {
			if(stmt!=null){
				stmt.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try
		{
			conn.commit();
		} catch (SQLException e)
		{
			log.error("查询冗余邻区和漏定邻区，提交事务失败");
		}finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				log.info("dao:getRedundantAndOmitNcellofCell方法关闭connection失败");
			}
		}
		result.put("redundantNcells", redundantNCells);
		result.put("omitNcells", omitNCells);
		return result;
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
	 * @return result
	 * @description 根据参数查询冗余邻区和漏定邻区
	 */
	public Map<String, List<RedundantNCell>> getRedundantAndOmitNcells(String cell, List<Long> ncsDescIds, List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入dao方法  getRedundantAndOmitNcellofCell cell="+cell+",ncsDescIds="+ncsDescIds+",cellResult的记录数"+cellResult.size()+"handoverDescIds="+handoverDescIds+"condition="+condition+",areaId="+areaId);
		Connection conn = DataSourceConn.initInstance().getConnection();
		
		try
		{
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			log.error("connection设置非自动提交事务失败");
			log.error(e.getStackTrace());
		}
		String ncsTab = "RNO_NCS";
		String cellResTab = "RNO_NCS_CELL_ANA_RESULT_MID";
		transferNscResult(cellResult, conn, cellResTab);
		/*List<RedundantNCell> redundantNCells = getRedundantNcell(cell, ncsDescIds, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcell(cell, ncsDescIds, condition, areaId, conn, ncsTab, cellResTab);*/
		List<RedundantNCell> redundantNCells = getRedundantNcellsByCondition(cell, ncsDescIds, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcellsByCondition(cell, ncsDescIds, condition, areaId, conn, ncsTab, cellResTab);
		
		try
		{
			conn.commit();
		} catch (SQLException e)
		{
			log.error("查询冗余邻区和漏定邻区，提交事务失败");
		}finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				log.info("dao:getRedundantAndOmitNcellofCell方法关闭connection失败");
			}
		}
		Map<String,List<RedundantNCell>> result = new HashMap<String, List<RedundantNCell>>();
		result.put("redundantNcells", redundantNCells);
		result.put("omitNcells", omitNCells);
		return result;
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
	/*public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId, long cityId)
	{
		log.info("进入dao方法 : getAllRedundantAndOmitCellsByCondition ncsDescids="+ncsDescIds+",handoverDescIds="+handoverDescIds+",condition="+condition+",areaId="+areaId+",cityId="+cityId);
		Connection conn = DataSourceConn.initInstance().getConnection();
		
		try
		{
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			log.error("connection设置非自动提交事务失败");
		}
		//邻区匹配、室分匹配、计算干扰度、理想覆盖距离
		final long saveNcsId = 100l;//rno_ncs_mid的临时id,代替ncs数据原有的描述id
		String ncsTab = "RNO_NCS_MID";
		String cellResTab = "RNO_NCS_CELL_ANA_RESULT_MID";
		calculateNcs(conn, ncsDescIds, cityId, ncsTab,cellResTab, saveNcsId);
		Map<String,List<RedundantNCell>> result = new HashMap<String, List<RedundantNCell>>();
		List<RedundantNCell> redundantNCells = getAllRedundantCellsByCondition(handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getAllOmitCellsByCondition(condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> redundantNCells = getRedundantNcellsByCondition(null, null, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcellsByCondition(null, null, condition, areaId, conn, ncsTab, cellResTab);
		try
		{
			conn.commit();
		} catch (SQLException e)
		{
			log.error("全网分析冗余邻区和漏定邻区，提交事务失败");
		}finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				log.info("dao:getAllRedundantAndOmitCellsByCondition方法关闭connection失败");
			}
		}
		result.put("redundantNcells", redundantNCells);
		result.put("omitNcells", omitNCells);
		return result;
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
	 * @param cityId
	 * @return result
	 * @description 根据条件全网分析冗余邻区和漏定邻区
	 */
/*	public Map<String, List<RedundantNCell>> getAllRedundantAndOmitCellsByCondition(List<Long> ncsDescIds, List<Object> cellResult, List<Long> handoverDescIds, NcellAnalysisCondition condition, long areaId)
	{
		log.info("进入dao方法：getAllRedundantAndOmitCellsByCondition ncsIds="+ncsDescIds+"cellResult的个数："+cellResult.size()+"handoverIds="+handoverDescIds+",condition="+condition+",areaId="+areaId);

		Connection conn = DataSourceConn.initInstance().getConnection();
		try
		{
			conn.setAutoCommit(false);
		} catch (SQLException e)
		{
			log.error("connection设置非自动提交事务失败");
			log.error(e.getStackTrace());
		}
		String ncsTab = "RNO_NCS";
		String cellResTab = "RNO_NCS_CELL_ANA_RESULT_MID";
		//将小区分析结果放入临时表
		transferNscResult(cellResult, conn, cellResTab);
		List<RedundantNCell> redundantNCells = getAllRedundantCellsByCondition(ncsDescIds, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getAllOmitCellsByCondition(ncsDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> redundantNCells = getRedundantNcellsByCondition(null, ncsDescIds, handoverDescIds, condition, areaId, conn, ncsTab, cellResTab);
		List<RedundantNCell> omitNCells = getOmitNcellsByCondition(null, ncsDescIds, condition, areaId, conn, ncsTab, cellResTab);
		try
		{
			conn.commit();
		} catch (SQLException e)
		{
			log.error("全网分析冗余邻区和漏定邻区，提交事务失败");
		}finally
		{
			try
			{
				conn.close();
			} catch (SQLException e)
			{
				log.info("dao:getAllRedundantAndOmitCellsByCondition方法关闭connection失败");
			}
		}
		Map<String,List<RedundantNCell>> result = new HashMap<String, List<RedundantNCell>>();
		result.put("redundantNcells", redundantNCells);
		result.put("omitNcells", omitNCells);
		return result;
		
	}*/
	
	/**
	 * 将ncs分析结果放入cellResTab中
	 * @author Liang YJ
	 * 2014-4-28 上午10:06:13
	 * @param cellResult
	 * 小区分析结果记录
	 * @param conn
	 * 数据库连接
	 * @param cellResTab
	 * 小区分析结果临时表
	 */
	private void  transferNscResult(List<Object> cellResult,Connection conn, String cellResTab)
	{
		String sql = "insert into "+cellResTab+" (CELL,EXPECTED_COVER_DIS) values (?,?)";
		PreparedStatement pStatement = null;
		try
		{
			pStatement = conn.prepareStatement(sql);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			log.error("插入小区分析结果失败");
			log.error(e.getStackTrace());
		}
		int count = 0;
		for(int i=0; i<cellResult.size(); i++)
		{
			Map<String,Object> row = (Map<String, Object>)cellResult.get(i);  
			try
			{
				String expectedCoverDis = (String)row.get("EXPECTED_COVER_DIS");
				if(null==expectedCoverDis || "null".equalsIgnoreCase(expectedCoverDis.trim()) || "".equals(expectedCoverDis.trim()))
				{
					continue;
				}
				pStatement.setString(1, (String)row.get("cell"));
				pStatement.setDouble(2, Double.parseDouble(expectedCoverDis));
				pStatement.addBatch();
				count++;
				if(i>=300)
				{
					pStatement.executeBatch();
				}
			} catch (SQLException e)
			{
				log.error("插入小区分析结果失败");
				log.error(e.getStackTrace());
			}
			
		}
		try
		{
			pStatement.executeBatch();
		} catch (SQLException e)
		{
			log.error("插入小区分析结果失败");
			log.error(e.getStackTrace());
		}finally
		{
			try
			{
				pStatement.close();
			} catch (SQLException e)
			{
				log.info("dao:transferNscResult方法关闭PrepareStatement失败");
			}
		}
		log.info("共导入"+count+"条小区结构记录");
	}
	
	/**
	 * 判断cell是否在某个区域下
	 * @author Liang YJ
	 * 2014-5-6 下午6:40:48
	 * @param tabName
	 * @param field
	 * @return
	 */
	private String getCellSql(String tabName, String field)
	{
		return "exists(select 1 from CELL where "+tabName+"."+field+"=CELL.LABEL and CELL.AREA_ID in(select AREA_ID from SYS_AREA start with SYS_AREA.AREA_ID = ? connect by prior SYS_AREA.AREA_ID = SYS_AREA.PARENT_ID))";
	}
	
	/**
	 * 判断某个某对小区是否具有邻区关系
	 * @author Liang YJ
	 * 2014-5-6 下午6:41:18
	 * @param tabName
	 * @param field1
	 * @param field2
	 * @return
	 */
	private String getNcellSql(String tabName, String field1, String field2)
	{
		return "exists(select 1 from RNO_NCELL where RNO_NCELL.CELL = "+tabName+"."+field1+" and RNO_NCELL.NCELL = "+tabName+"."+field2+")";
	}
	
	/**
	 * 获取ncs数据统计字段
	 * @author Liang YJ
	 * 2014-5-6 下午6:41:52
	 * @return
	 */
	private String getNcsField()
	{
		return "CELL, NCELL, AVG(NAVSS) NAVSS, SUM(TIMES)/SUM(REPARFCN) DETECT_RATIO, avg(DISTANCE) DISTANCE";
	}

	/**
	 * 
	 * @title 通过区域ID获取区域级别信息
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午05:12:47
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getAreaLevelByAreaId(final long areaId) {
		// TODO Auto-generated method stub
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "SELECT AREA_LEVEL from SYS_AREA WHERE AREA_ID=?";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, areaId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		
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
	public List<Map<String, Object>> getTaskIdAndCompleteTimeByAreaId(final long areaId) {
		// TODO Auto-generated method stub
//		final String sql="SELECT task_id,COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID=? AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1";
		log.debug("进入getTaskIdAndCompleteTimeByAreaId(final long areaId)"+areaId);
		final String sql=getNcsTaskQuerySql(areaId);
		log.debug("sql:"+sql);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				//query.setLong(0, areaId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.debug("退出getTaskIdAndCompleteTimeByAreaId(final long areaId)"+query.list().size());
				return query.list();
			}
		});
		
	}
	/**
	 * 获取区域最新的NCS干扰矩阵的id与计算时间
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午04:52:44
	 */
	public List<Map<String, Object>> getLatelyInterferMartixRecByAreaId(
			long areaId) {
		log.debug("进入getLatelyInterferMartixRecByAreaId 。 areaId="+areaId);
		final String sql = "select MARTIX_DESC_ID, "
						+"	       to_char(CREATE_DATE, 'yyyy-MM-dd hh24:mi:ss') as CREATE_DATE "
						+"	  from RNO_INTERFER_MARTIX_REC "
						+"	 where WORK_STATUS = '计算完成' "
						+"	   and STATUS = 'Y' "
						+"     and CITY_ID = " + areaId 
						+"	 order by CREATE_DATE desc nulls last ";
		log.debug("sql:"+sql);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.debug("退出getLatelyInterferMartixRecByAreaId . size="+query.list().size());
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 判断该ID是否为市
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-24下午05:20:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean whetherCity(long areaId) {
		log.debug("进入whetherCity(long areaId)"+areaId);
		boolean flag=false;
		List<Map<String, Object>> arealevelList=getAreaLevelByAreaId(areaId);
		String arealevel="";
		if (arealevelList!=null && arealevelList.size()!=0) {
			arealevel=arealevelList.get(0).get("AREA_LEVEL").toString();
		}
		if ("市".equals(arealevel)) {
			flag=true;
		}
		log.debug("退出whetherCity(long areaId)　flag:"+flag);
		return flag;
	}
	/**
	 * 
	 * @title 获取NCS任务查询SQL语句
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-25上午09:36:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	private String getNcsTaskQuerySql(long areaId){
		log.info("进入getNcsTaskQuerySql(long areaId)"+areaId);
		String sql="";
		if (whetherCity(areaId)) {
			sql="SELECT task_id,to_char(COMPLETE_TIME,'yyyy-MM-dd hh24:mi:ss') COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID="+areaId+" AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC nulls last) t1 WHERE ROWNUM=1";
		}else {
			//获取areadata,citydata的true false
			List<Map<String, Object>> whetherExistData=judgeAreaAndCityWhetherExistData(areaId);
			String areadata="0";
			String citydata="0";
			if (whetherExistData!=null&&whetherExistData.size()!=0) {
				areadata=whetherExistData.get(0).get("AREADATA").toString();
				citydata=whetherExistData.get(0).get("CITYDATA").toString();
			}
			//1.当区/县为空的时候而市不为空则用市
			if("0".equals(areadata)&&"1".equals(citydata)){
				List<Map<String, Object>> parentidlist=getParentId(areaId);
				areaId=Long.parseLong(parentidlist.get(0).get("PARENT_ID").toString());
			}
			//2.当区/县不空的时候而县为空则用区/县,均为空的时候不处理
			//不需处理
			//3.当市与区/县均不为空的情况下比较哪个时间最近，时间最近的录取应用
			//以下是市与区/县均不为空的情况下
			//选择的是区/县
			if("1".equals(areadata)&&"1".equals(citydata)){
				
				List<Map<String, Object>> selList=getLatelyAreaIdBetweenCityAndAreaCompleteTimeByAreaId(areaId);
				if (selList!=null&&selList.size()!=0) {
					areaId=Long.parseLong(selList.get(0).get("SELID").toString());
				}
			}
			sql="SELECT task_id,to_char(COMPLETE_TIME,'yyyy-MM-dd hh24:mi:ss') COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID="+areaId+" AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1";
		}
		log.info("退出getNcsTaskQuerySql(long areaId)"+sql);
		return sql;
	}
	/**
	 * 
	 * @title 在市与区/县间比较获取最新的任务完成时间，并返回该ID
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-6-30上午11:50:58
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getLatelyAreaIdBetweenCityAndAreaCompleteTimeByAreaId(final long areaId) {
		// TODO Auto-generated method stub
//		final String sql="SELECT task_id,COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID=? AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1";
		log.debug("进入getLatelyAreaIdBetweenCityAndAreaCompleteTimeByAreaId(final long areaId)"+areaId);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql="SELECT CASE WHEN t1.COMPLETE_TIME>t2.COMPLETE_TIME THEN t1.level_id ELSE t2.level_id END selid,t1.COMPLETE_TIME areaId_Com_Time,t2.COMPLETE_TIME cityId_Com_Time,t1.level_id areaId,t2.level_id cityId from (SELECT COMPLETE_TIME,level_id from (SELECT * from RNO_TASK WHERE LEVEL_ID="+areaId+" AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1) t1,(SELECT COMPLETE_TIME,level_id from (SELECT * from RNO_TASK WHERE LEVEL_ID in(SELECT PARENT_ID from SYS_AREA WHERE AREA_ID="+areaId+") AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1) t2";
				SQLQuery query = arg0.createSQLQuery(sql);
				//query.setLong(0, areaId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.debug("退出getLatelyAreaIdBetweenCityAndAreaCompleteTimeByAreaId(final long areaId)"+query.list().size());
				return query.list();
			}
		});
		
	}
	/**
	 * 
	 * @title 判断市或区/县是否存在数据
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-7-1下午2:47:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> judgeAreaAndCityWhetherExistData(final long areaId) {
		// TODO Auto-generated method stub
//		final String sql="SELECT task_id,COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID=? AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1";
		log.info("进入judgeAreaAndCityWhetherExistData(final long areaId)"+areaId);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql="select t1.*, t2.*															  "
						+" from (SELECT case                                                           "
						+"                when count(*) = 0 then                                       "
						+"                 '0'                                                     "
						+"                else                                                         "
						+"                 '1'                                                      "
						+"              end areadata                                                   "
						+"         from (SELECT *                                                      "
						+"                 from RNO_TASK                                               "
						+"                WHERE LEVEL_ID = "+areaId+"                                         "
						+"                  AND TASK_GOING_STATUS = '成功完成'                         "
						+"                ORDER BY COMPLETE_TIME DESC) t1                              "
						+"        WHERE ROWNUM = 1) t1,                                                "
						+"      (SELECT case                                                           "
						+"                when count(*) = 0 then                                       "
						+"                 '0'                                                     "
						+"                else                                                         "
						+"                 '1'                                                      "
						+"              end citydata                                                   "
						+"         from (SELECT *                                                      "
						+"                 from RNO_TASK                                               "
						+"                WHERE LEVEL_ID in                                            "
						+"                      (select parent_id from sys_area where area_id = "+areaId+")   "
						+"                  AND TASK_GOING_STATUS = '成功完成'                         "
						+"                ORDER BY COMPLETE_TIME DESC) t1                              "
						+"        WHERE ROWNUM = 1) t2                                                 ";
				SQLQuery query = arg0.createSQLQuery(sql);
				//query.setLong(0, areaId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出judgeAreaAndCityWhetherExistData(final long areaId)"+query.list().size());
				return query.list();
			}
		});
		
	}
	/**
	 * 
	 * @title 通过区域ID获取父区域ID
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-7-1下午3:41:43
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getParentId(final long areaId) {
		// TODO Auto-generated method stub
//		final String sql="SELECT task_id,COMPLETE_TIME,to_char(COMPLETE_TIME,'yyyy')||'/'||to_number(to_char(COMPLETE_TIME,'MM')) as yearmonth from (SELECT * from RNO_TASK WHERE LEVEL_ID=? AND TASK_GOING_STATUS='成功完成' ORDER BY  COMPLETE_TIME DESC) t1 WHERE ROWNUM=1";
		log.info("进入getParentId(final long areaId)"+areaId);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql="select parent_id from sys_area where area_id="+areaId;
				SQLQuery query = arg0.createSQLQuery(sql);
				//query.setLong(0, areaId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出getParentId(final long areaId)"+query.list().size());
				return query.list();
			}
		});
		
	}

	/**
	 * 获取小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public List<Map<String, Object>> getCellFreqByCellName(String cell) {
		log.info("进入getCellFreqByCellName,cell="+cell);
		
		final String sql="select cell.bcch, cell.tch from cell where cell.label='"+cell+"'";
		
		log.info("getCellFreqByCellName的sql:"+sql);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出getCellFreqByCellName,result.size()="+query.list().size());
				return query.list();
			}
		});
	}

	/**
	 * 更新小区的bcch与tch
	 * @author peng.jm
	 * @date 2014-8-13下午05:21:58
	 */
	public boolean updateCellFreqThroughCellName(String cell, String bcch, String tch) {
		log.debug("进入updateCellFreqThroughCellName　cell=" + cell
				+ ", bcch=" + bcch + ", tch=" + tch);
		int bcchNum = Integer.parseInt(bcch);
		
		final String sql = "update cell set cell.bcch=" + bcchNum + ",cell.tch='"
				+ tch + "' where cell.label='" + cell + "'";
		
		log.info("getCellFreqByCellName的sql:"+sql);
		return hibernateTemplate
		.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				int res = query.executeUpdate();
				log.info("退出getCellFreqByCellName,受影响行数="+res);
				boolean result = false;
				if (res > 0) {
					result = true;
				} else {
					result = false;
				} 
				return result;
			}
		});
	}

	/**
	 * 通过id获取干扰矩阵的目录
	 * @param martixDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午05:32:10
	 */
	public List<Map<String, Object>> getMartixDirById(long martixDescId) {
		log.info("进入getMartixDirById, martixDescId="+martixDescId);
		
		final String sql="select FILE_PATH from RNO_INTERFER_MARTIX_REC" +
				" where MARTIX_DESC_ID =" + martixDescId;
		
		log.info("getMartixDirById的sql:"+sql);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出getMartixDirById,result.size()="+query.list().size());
				return query.list();
			}
		});
	}
}
