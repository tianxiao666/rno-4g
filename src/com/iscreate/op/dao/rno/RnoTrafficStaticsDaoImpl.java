package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.RnoCityQuality;
import com.iscreate.op.pojo.rno.RnoCityqulDetail;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoStsResult;
import com.iscreate.op.pojo.rno.StsAnaItemDetail;
import com.iscreate.op.pojo.rno.StsConfig;

public class RnoTrafficStaticsDaoImpl implements RnoTrafficStaticsDao {

	private static Log log = LogFactory.getLog(RnoCellDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 通过传入城市质量指标对象保存入库
	 */
	public void saveRnoCityQuality(RnoCityQuality rnoCityQuality) {
		hibernateTemplate.saveOrUpdate(rnoCityQuality);
	}

	/**
	 * 通过传入城市质量指标详情对象保存入库
	 */
	public void saveRnoCityQulDetail(RnoCityqulDetail rnoCityqulDetail) {
		hibernateTemplate.saveOrUpdate(rnoCityqulDetail);
	}

	/**
	 * 通过地区名得到区域ID
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getAreaIdByName(final String name) {
		log.info("进入：getAreaIdByName。 name=" + name);
		List<String> names = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<String> names = new ArrayList<String>();
						SQLQuery query = arg0
								.createSQLQuery("SELECT ID from AREA where NAME LIKE '"+name+"%'");
						//query.setString(0, name);
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
		log.info("退出：getAreaIdByName。 得到AreaId集合：" + names);
		return names;
	}

	/**
	 * 从网络质量指标表中得到最新的CityQulId
	 */
	public List<String> getMaxCityQulIdFromRNOCityQuality() {
		log.info("进入：getMaxCityQulIdFromRNOCityQuality:");
		List<String> names = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<String> names = new ArrayList<String>();
						SQLQuery query = arg0
								.createSQLQuery("SELECT MAX(RNO_CITY_QUALITY.CITYQUL_ID) FROM RNO_CITY_QUALITY");
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
		log.info("退出：getMaxCityQulIdFromRNOCityQuality。 得到MaxCityQulId集合："
				+ names);
		return names;
	}

	/**
	 * 通过区域和发生的日期从网络质量指标表中得到CityQulId
	 */
	public List<String> getCityQulIdByAreaAndDate(final Long areaId,
			final String staticTime) {
		log.info("进入：getCityQulIdByAreaAndDate.areaId+staticTime:" + areaId
				+ staticTime);
		List<String> names = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<String> names = new ArrayList<String>();
						SQLQuery query = arg0
								.createSQLQuery("SELECT cityqul_id from RNO_CITY_QUALITY WHERE area_id=? and static_time=to_date('"
										+ staticTime
										+ "','yyyy-mm-dd hh24:mi:ss')");
						query.setLong(0, areaId.longValue());
						// query.setDate(1, staticTime);
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
		log.info("退出：getCityQulIdByAreaAndDate。 得到CityQulId集合：" + names);
		return names;
	}

	/**
	 * 通过CityQulId删除一条网络质量指标数据
	 * 
	 * @param CityQulId
	 * @return
	 */
	public int deleteCityQualityById(final Long CityQulId) {
		log.info("进入：deleteCityQualityById。 CityQulId=" + CityQulId);

		int re = hibernateTemplate.execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "DELETE from RNO_CITY_QUALITY WHERE CITYQUL_ID=?";
				log.info("删除邻区的sql语句为：" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, CityQulId.longValue());
				int result = query.executeUpdate();
				return result;
			}
		});

		log.info("退出：deleteCityQualityById。 删除的记录数：" + re);
		return re;
	}

	/**
	 * 通过CityQulId删除若干条网络质量指标详情数据
	 * 
	 * @param CityQulId
	 * @return
	 */
	public int deleteCityQualityDetailById(final Long CityQulId) {
		log.info("进入：deleteCityQualityDetailById。 CityQulId=" + CityQulId);

		int re = hibernateTemplate.execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "DELETE from RNO_CITYQUL_DETAIL WHERE CITYQUL_ID=?";
				log.info("删除邻区的sql语句为：" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, CityQulId.longValue());
				int result = query.executeUpdate();
				return result;
			}
		});

		log.info("退出：deleteCityQualityDetailById。 删除的记录数：" + re);
		return re;
	}

	/**
	 * 获取指定分析列表中的小区的gis信息
	 * 
	 * @param selConfigs
	 * @return
	 * @author brightming 2013-10-11 下午5:51:35
	 */
	// TODO 需要整合查询语句的数据
	public List<RnoGisCell> getGisCellInfoFromSelectionList(
			final List<StsConfig> selConfigs) {
		log.info("进入getGisCellInfoFromSelectionList,selConfigs=" + selConfigs);
		if (selConfigs == null || selConfigs.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<RnoGisCell> gisCells = null;

						StringBuilder buf = new StringBuilder();
						for (StsConfig sc : selConfigs) {
							if (!sc.isFromQuery()) {
								buf.append(sc.getConfigId() + ",");
							}
						}
						if (buf.length() > 1) {
							buf.deleteCharAt(buf.length() - 1);
						}

						String sql = "select cell.label as cell,cell.name as chineseName,cell.LONGITUDE as lng,cell.LATITUDE as lat,cell.LNGLATS as allLngLats,cell.BEARING as azimuth,cell.GSMFREQUENCESECTION as freqType ,cell.SITE as site,cell.LAC as lac,cell.CI as ci,cell.BCCH as bcch ,cell.TCH as tch from cell inner join (select distinct(cell) as cn from rno_sts where DESCRIPTOR_ID IN ("
								+ buf.toString()
								+ ") group by cell) sts on cell.label=sts.cn";
						log.info("获取选定的分析列表的小区的gis信息的sql：" + sql);
						// System.out.println(sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoGisCell.class);
						gisCells = query.list();
						log.info("getGisCellInfoFromSelectionList获取的结果："
								+ gisCells == null ? 0 : gisCells.size());

						return gisCells;
					}
				});

	}

	/**
	 * 统计给的字段的情况，统计范围由selConfigs指定
	 * 
	 * @param fieldName
	 * @param selConfigs
	 * @return
	 * @author brightming 2013-10-14 上午11:46:28
	 */
	// TODO 需要整合查询语句的小区
	public List<RnoStsResult> stsSpecFieldInSelConfig(final String fieldName,
			final List<StsConfig> selConfigs) {
		log.info("进入stsSpecFieldInSelConfig,fieldName=" + fieldName
				+ ",selConfigs=" + selConfigs);
		if (selConfigs == null || selConfigs.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoStsResult>>() {
					public List<RnoStsResult> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<RnoStsResult> stsResults = null;

						StringBuilder buf = new StringBuilder();
						for (StsConfig sc : selConfigs) {
							if (!sc.isFromQuery()) {
								buf.append(sc.getConfigId() + ",");
							}
						}
						if (buf.length() > 1) {
							buf.deleteCharAt(buf.length() - 1);
						}

						String sql = "select distinct(cell),avg("
								+ fieldName
								+ ") as avgValue,max("
								+ fieldName
								+ ") as maxValue,min("
								+ fieldName
								+ ") as minValue,count("
								+ fieldName
								+ ") as cnt from rno_sts where DESCRIPTOR_ID IN ("
								+ buf.toString() + ") group by cell";
						log.info("获取选定的分析列表的小区的统计信息的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);

						query.addEntity(RnoStsResult.class);
						stsResults = query.list();
						log.info("stsSpecFieldInSelConfig获取的结果：" + stsResults == null ? 0
								: stsResults.size());

						return stsResults;

					}
				});
	}

	/**
	 * 获取符合指定条件的小区，以及其相应的指标值
	 * 
	 * @param needCellType
	 *            veryidlecell 超闲小区
	 *            小区配置大于2载频；小区无线利用率最大时，每线话务量小于0.15Erl，或PDCH承载速率小于3kbps的小区。
	 *            overloadcell 超忙小区 指20点 每线话务量大于0.9erl的小区数。 highuseradiocell
	 *            高无线利用率小区 指每天20点 无线资源利用率90%以上的小区数。 highcongindatacell 数据高拥塞率小区
	 *            下行TBF拥塞率大于5%的小区，取每天21：00-24：00的平均值，剔除小区下行数据业务总流量小于100KB的时段。
	 *            badlyicmcell 高干扰小区
	 *            干扰系数=（icm4+icm5）/(ICM1+ICM2+……+ICM5)>30，高干扰小区
	 * 
	 * @param selConfigs
	 * @return
	 * @author brightming 2013-10-16 上午11:27:43
	 */
	public List<RnoStsResult> selectSpecialCellInSelConfig(String needCellType,
			List<StsConfig> selConfigs) {
		log.info("进入selectSpecialCellInSelConfig,needCellType=" + needCellType
				+ ",selConfigs=" + selConfigs);
		if (selConfigs == null || selConfigs.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		if (needCellType == null || needCellType.isEmpty()) {
			log.error("未指明需要统计的小区类型 ！无法进行统计！");
			return Collections.EMPTY_LIST;
		}

		StringBuilder buf = new StringBuilder();
		for (StsConfig sc : selConfigs) {
			if (!sc.isFromQuery()) {
				buf.append(sc.getConfigId() + ",");
			}
		}
		if (buf.length() > 1) {
			buf.deleteCharAt(buf.length() - 1);
		}

		final String sql;
		// 超闲小区
		if ("veryidlecell".equals(needCellType)) {
			sql = "select DISTINCT(cell) as cell ,max(TRAFFIC_PER_LINE) as maxValue,avg(TRAFFIC_PER_LINE) as avgValue,min(TRAFFIC_PER_LINE) as minValue,count(TRAFFIC_PER_LINE) as cnt from rno_sts  sts INNER JOIN RNO_STS_DESCRIPTOR  descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID and DESCP.STS_DESC_ID in ("
					+ buf.toString()
					+ ") and STS.CARRIER_NUMBER>2 GROUP BY cell having (max(TRAFFIC_PER_LINE)<0.15 OR max(PDCH_CARRYING_EFFICIENCY)<3)";
		} else if ("overloadcell".equals(needCellType)) {
			// 超忙小区
			sql = "select DISTINCT(cell),max(TRAFFIC_PER_LINE) maxValue,avg(TRAFFIC_PER_LINE) avgValue,MIN(TRAFFIC_PER_LINE) minValue,COUNT(TRAFFIC_PER_LINE) cnt from rno_sts  sts INNER JOIN RNO_STS_DESCRIPTOR  descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID AND descp.STS_DESC_ID IN ("
					+ buf.toString()
					+ ") and descp.STS_PERIOD='2000-2100' group by cell having(max(TRAFFIC_PER_LINE)>0.9)";
		} else if ("highuseradiocell".equals(needCellType)) {
			// 高无线利用率小区
			sql = "select DISTINCT(cell),max(RESOURCE_USE_RATE) maxValue,avg(RESOURCE_USE_RATE) avgValue,MIN(RESOURCE_USE_RATE) minValue,COUNT(RESOURCE_USE_RATE) cnt from rno_sts  sts INNER JOIN RNO_STS_DESCRIPTOR  descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID AND descp.STS_DESC_ID IN ("
					+ buf.toString()
					+ ") and descp.STS_PERIOD='2000-2100' group by cell having(avg(RESOURCE_USE_RATE)>90)";
		} else if ("highcongindatacell".equals(needCellType)) {
			// 数据高拥塞
			sql = "select DISTINCT(cell),max(DOWNLINK_TBF_CONG_RATE) as maxValue,avg(DOWNLINK_TBF_CONG_RATE) as avgValue ,min(DOWNLINK_TBF_CONG_RATE) as minValue,count(DOWNLINK_TBF_CONG_RATE) as cnt FROM  RNO_STS sts INNER JOIN RNO_STS_DESCRIPTOR descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID AND descp.STS_DESC_ID IN ("
					+ buf.toString()
					+ ") AND descp.STS_PERIOD in ('2100-2200','2200-2300','2300-0000') and (1024*STS.DATA_TRAFFIC)>100 group by cell having max(DOWNLINK_TBF_CONG_RATE)>5";
		} else if ("badlyicmcell".equals(needCellType)) {
			sql = "select DISTINCT(cell),max(ICM) AS maxValue,min(ICM) as minValue,avg(ICM) as avgValue,count(ICM) as cnt from RNO_STS sts INNER JOIN RNO_STS_DESCRIPTOR descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID AND descp.STS_DESC_ID IN ("
					+ buf.toString() + ") group by cell having (avg(ICM)>30)";
		} else {
			log.error("未定义类型为【" + needCellType + "】的小区统计公式，无法统计！");
			return Collections.EMPTY_LIST;
		}

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoStsResult>>() {

					public List<RnoStsResult> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoStsResult.class);
						return query.list();
						// query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						// List<Map<String,Object>> stsList = query.list();
						//
						// System.out.println(stsList);
						// return null;
					}

				});
	}

	/**
	 * 获取某个区域的繁忙小区
	 * 
	 * @param areaId
	 * @return
	 * @author brightming 2013-10-19 下午4:10:48
	 */
	public List<RnoStsResult> staticsHeavyLoadCellWithinArea(final long areaId) {
		log.info("进入方法：staticsHeavyLoadCellWithinArea。areaId=" + areaId);
		final String sql = "select DISTINCT(cell),max(TRAFFIC_PER_LINE) maxValue,avg(TRAFFIC_PER_LINE) avgValue,MIN(TRAFFIC_PER_LINE) minValue,COUNT(TRAFFIC_PER_LINE) cnt from rno_sts  sts INNER JOIN RNO_STS_DESCRIPTOR  descp on sts.DESCRIPTOR_ID=descp.STS_DESC_ID and descp.AREA_ID ="
				+ areaId
				+ " and descp.STS_PERIOD='2000-2100' group by cell having(max(TRAFFIC_PER_LINE)>0.9)";
		log.info("获取区域：" + areaId + "内的繁忙小区的sql：" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoStsResult>>() {
					public List<RnoStsResult> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoStsResult.class);
						return query.list();
					}
				});
	}

	/**
	 * 获取指定小区的邻区的话务情况
	 * 
	 * @param cell
	 * @param areaId
	 * @return
	 * @author brightming 2013-10-19 下午4:19:29
	 */
	public List<RnoStsResult> staticsSpecialCellsNcellStsInfo(String cell,
			long areaId) {
		log.info("进入方法：staticsSpecialCellsNcellStsInfo。cell=" + cell
				+ ",areaId=" + areaId);
		String fieldName = "RESOURCE_USE_RATE";// "TRAFFIC_PER_LINE";

		final String sql = "select DISTINCT(RNO_STS.cell),max("
				+ fieldName
				+ ") maxValue,avg("
				+ fieldName
				+ ") avgValue,MIN("
				+ fieldName
				+ ") minValue,COUNT("
				+ fieldName
				+ ") cnt from RNO_STS inner join RNO_NCELL ON RNO_STS.CELL=RNO_NCELL.NCELL AND RNO_NCELL.CELL='"
				+ cell
				+ "' inner join RNO_STS_DESCRIPTOR on RNO_STS.DESCRIPTOR_ID=RNO_STS_DESCRIPTOR.STS_DESC_ID and RNO_STS_DESCRIPTOR.AREA_ID="
				+ areaId + " group by RNO_STS.cell";
		log.info("获取区域：" + areaId + "内的繁忙小区" + cell + "的邻区的话务情况的sql：" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoStsResult>>() {
					public List<RnoStsResult> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoStsResult.class);
						return query.list();
					}
				});
	}

	/**
	 * 查询指定条件的时间段统计数据
	 * 
	 * @param searchType
	 * @param condition
	 * @return
	 * @author brightming 2013-10-24 下午3:59:39
	 */
	public List<StsConfig> queryStsConfigByCondition(String searchType,
			StsCondition condition) {
		log.info("进入方法：queryStsConfigByCondition。searchType=" + searchType
				+ ",condition=" + condition);
		if (condition == null) {
			log.warn("未传入查询条件！");
			return Collections.EMPTY_LIST;
		}
		String sql = "select a.STS_DESC_ID,a.AREA_ID,b.\"NAME\",\"TO_CHAR\"(a.STS_DATE,'YYYY/MM/DD') AS STS_DATE,a.STS_PERIOD from RNO_STS_DESCRIPTOR a,Area b where a.SPEC_TYPE='"
				+ searchType + "'";

		if (condition.getStsDateStr() != null
				&& !"".equals(condition.getStsDateStr())) {
			sql += " AND a.STS_DATE=\"TO_DATE\"('" + condition.getStsDateStr()
					+ "','YYYY-MM-DD') ";
		}
		sql += " AND a.AREA_ID=b.ID AND a.AREA_ID=" + condition.getAreaId();

		if(condition.getEngName()!=null && !condition.getEngName().equals("")){
			sql+=" AND exists (select RNO_BSC_RELA_AREA.AREA_ID FROM RNO_BSC_RELA_AREA,RNO_BSC WHERE RNO_BSC.ENGNAME='"+condition.getEngName()+"' AND RNO_BSC.BSC_ID=RNO_BSC_RELA_AREA.BSC_ID)";
		}
		if (condition.getStsPeriod() != null
				&& !"".equals(condition.getStsPeriod().trim())) {
			sql += " and a.STS_PERIOD='" + condition.getStsPeriod() + "'";
		}
		log.info("queryStsConfigByCondition的sql语句：" + sql);
		//System.out.println(sql);
		final String s = sql;
		List<Map<String, Object>> mid = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					//@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(s);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> stsList = query.list();
						return stsList;
					}
				});

		List<StsConfig> configs = new ArrayList<StsConfig>();
		if (mid != null && !mid.isEmpty()) {
			StsConfig sc = null;
			StsAnaItemDetail item = null;
			String period = null;
			for (Map<String, Object> one : mid) {
				sc = new StsConfig();
				sc.setConfigId(Long.parseLong(one.get("STS_DESC_ID") + ""));
				sc.setFromQuery(false);
				sc.setSelected(false);
				sc.setStsCondition(null);
				item = new StsAnaItemDetail();
				item.setAreaId(Long.parseLong(one.get("AREA_ID") + ""));
				item.setAreaName(one.get("NAME") + "");
				item.setStsDate(one.get("STS_DATE") + "");
				if (RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_AUDIO
						.equals(searchType)) {
					item.setStsType("小区语音业务指标");
				} else if (RnoConstant.DBConstant.STS_SPEC_TYPE_GSM_DATA
						.equals(searchType)) {
					item.setStsType("小区数据业务指标");
				}
				period = one.get("STS_PERIOD") + "";
				item.setPeriodType(getPeriodType(period));

				sc.setStsAnaItemDetail(item);
				configs.add(sc);
			}
		}

		return configs;
	}

	/**
	 * 根据输入获取是小时指标还是天指标
	 * 
	 * @param period
	 * @return
	 * @author brightming 2013-10-19 上午11:39:02
	 */
	private String getPeriodType(String period) {
		if (period == null || period.trim().isEmpty()) {
			return "不规则指标";
		}

		String[] ps = period.split("-");
		if (ps.length == 2) {
			String s1 = ps[0];
			String s2 = ps[1];
			int i1 = 0;
			int i2 = 0;
			try {
				i1 = Integer.parseInt(s1);
				i2 = Integer.parseInt(s2);
				if ((i2 - i1) == 100 || (i1 == 2300 && i2 == 0)) {
					return "小时指标";
				} else {
					return "天指标";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "不规则指标";
			}
		}

		return "不规则指标";

	}
	/**
	 * 通过RNO_STS表中的BSC_ID获取RNO_BSC的ENGNAME
	 * @return
	 * @author chao.xj
	 * @date 2013-11-4下午02:00:59
	 */
	public List<Map<String, Object>> getRnoBscEngName(){
		List<Map<String, Object>> list = hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				String sqlString="SELECT ENGNAME from RNO_BSC WHERE BSC_ID IN(SELECT BSC_ID from RNO_STS)";
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> find = query.list();
				return find;
			}
		});
		if(list != null && list.size() > 0){
			return list;
		}else{
			return null;
		}
	}	
	/**
	 * 改造：加载话务性能页面：从系统加载的获取查询sql语句
	 * @param searchType
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午05:35:07
	 */
	public String getLoadTrafficStaticsSql(String searchType,
			StsCondition condition,boolean forcount) {
		log.info("进入方法：getLoadTrafficStaticsSql searchType="+searchType+",condition="+condition);
		if (condition == null) {
			log.warn("未传入查询条件！");
			return Collections.EMPTY_LIST.toString();
		}
		StringBuffer sbf = new StringBuffer(" 1=1 and ");
		String filed="";
		
		//String sql = "select a.STS_DESC_ID,a.AREA_ID,b.\"NAME\",\"TO_CHAR\"(a.STS_DATE,'YYYY/MM/DD') AS STS_DATE,a.STS_PERIOD from RNO_STS_DESCRIPTOR a,Area b where a.SPEC_TYPE='"+ searchType + "'";
		sbf.append(" a.SPEC_TYPE='"+ searchType + "'");
		/*if (condition.getStsDateStr() != null
				&& !"".equals(condition.getStsDateStr())) {
			sql += " AND a.STS_DATE=\"TO_DATE\"('" + condition.getStsDateStr()
					+ "','YYYY-MM-DD') ";
		}*/
		if(condition.getBeginTime()!=null && !condition.getBeginTime().equals("") && condition.getLatestAllowedTime()!=null && !condition.getLatestAllowedTime().equals("")){
			//System.out.println("来了");
			sbf.append(" and to_char(STS_DATE,'yyyy-MM-dd') BETWEEN '"+sdf.format(condition.getBeginTime())+"' AND '"+sdf.format(condition.getLatestAllowedTime())+"'");
			//to_char(STATIC_TIME,'yyyy-MM-dd') BETWEEN '2013-07-28' AND '2013-07-30'
		}else {
			//System.out.println("又来了");
			if(condition.getBeginTime()!=null && !condition.getBeginTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')>='"+sdf.format(queryCondition.getBeginTime())+"'");
				sbf.append( " and a.STS_DATE>=to_date('"+sdf.format(condition.getBeginTime())+"','yyyy-mm-dd')");
			}
			if(condition.getLatestAllowedTime()!=null && !condition.getLatestAllowedTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')<='"+sdf.format(queryCondition.getBeginTime())+"'");
				sbf.append( " and a.STS_DATE<=to_date('"+sdf.format(condition.getLatestAllowedTime())+"','yyyy-mm-dd')");
			}
		}
		sbf.append( " AND a.AREA_ID=b.ID AND a.AREA_ID=" + condition.getAreaId());

		if(condition.getEngName()!=null && !condition.getEngName().equals("")){
			sbf.append(" AND exists (select RNO_BSC_RELA_AREA.AREA_ID FROM RNO_BSC_RELA_AREA,RNO_BSC WHERE RNO_BSC.ENGNAME='"+condition.getEngName()+"' AND RNO_BSC.BSC_ID=RNO_BSC_RELA_AREA.BSC_ID)");
		}
		if (condition.getStsPeriod() != null
				&& !"".equals(condition.getStsPeriod().trim())) {
			sbf.append( " and a.STS_PERIOD='" + condition.getStsPeriod() + "'");
		}
		String sql="";
		if (forcount) {
			sql = "select count(*) from RNO_STS_DESCRIPTOR a,Area b where "+sbf.toString();
		}else {
			sql = "select * from (select a.STS_DESC_ID,a.net_type,a.spec_type,a.AREA_ID,b.\"NAME\" areaname,\"TO_CHAR\"(a.STS_DATE,'YYYY/MM/DD') AS STS_DATE,a.STS_PERIOD,rownum rn from RNO_STS_DESCRIPTOR a,Area b where "+sbf.toString()+") where   rn>=? and rn<=?";
		}
		log.info("getLoadTrafficStaticsSql的sql语句：" + sql);
		//System.out.println(sql);
		return sql;
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
			final StsCondition queryCondition) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = getLoadTrafficStaticsSql(searchType, queryCondition, true);
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
	 * 改造：查询小区语音或数据业务话统通过分页方式
	 * @param page
	 * @param queryCondition
	 * @param searchType
	 * @return
	 * @author chao.xj
	 * @date 2013-12-12下午07:03:17
	 */
	public List<Map<String,Object>> queryCellAudioOrDataStsByPage(final Page page, final StsCondition queryCondition,final String searchType) {
		log.info("进入方法：queryStsByCityQuaByPage(final Page page, final StsCondition queryCondition).page=" + page + ",queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = getLoadTrafficStaticsSql(searchType, queryCondition, false);
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
	 * 改造：通过小区语音和数据话统配置ID的字符串获取话统指标描述数据
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2013-12-10下午05:46:52
	 */
	public List<Map<String, Object>> getCellAudioOrDataDescByConfigIds(final String configIds) {
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="SELECT a.STS_DESC_ID,a.spec_type,a.net_type,TO_CHAR(a.sts_date,'YYYY-MM-DD') AS STS_DATE,TO_CHAR(a.CREATE_TIME,'YYYY-MM-DD HH24:mi:ss') AS CREATE_TIME,a.AREA_ID,a.STS_PERIOD,b.NAME areaname from RNO_STS_DESCRIPTOR a,SYS_AREA b WHERE a.AREA_ID=b.AREA_ID AND STS_DESC_ID  in("+configIds+")";
				SQLQuery query=arg0.createSQLQuery(sql);
				//query.setString(0, configIds);
				//System.out.println(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
			
			
		});
	}
	/**
	 * 通过小区查询邻区
	 * @param cell
	 * @return
	 * @author chao.xj
	 * @date 2013-12-26下午03:16:28
	 */
	public List<Map<String,Object>> queryNcellByCell(final String cell){
		log.info("进入方法：queryNcellByCell(final String cell),cell=" + cell);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "SELECT NCELL from RNO_NCELL WHERE cell='"+cell+"'";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> ncellList = query.list();
						log.info("获取到结果集记录数量："
								+ (ncellList == null ? 0 : ncellList.size()));
						return ncellList;
					}
				});
	}
}
