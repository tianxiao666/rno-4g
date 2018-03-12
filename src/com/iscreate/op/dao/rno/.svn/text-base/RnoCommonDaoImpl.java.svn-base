package com.iscreate.op.dao.rno;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoCommonDaoImpl implements RnoCommonDao {

	private static Log log = LogFactory.getLog(RnoCellDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * 通过区域ID获取指定区域的地图经纬度纠偏集合
	 * 
	 * @author chao.xj
	 * @date 2013-10-24下午02:10:37
	 */

	public List<RnoMapLnglatRelaGps> getSpecialAreaRnoMapLnglatRelaGpsList(
			long areaid, String mapType) {

		log.info("进入方法：getSpecialAreaRnoMapLnglatRelaGpsList。areaId=" + areaid);
		String fieldName = "RESOURCE_USE_RATE";// "TRAFFIC_PER_LINE";

		// final String
		// sql="SELECT DISTINCT(bottomleft),GPS,BAIDU,OFFSET,AREAID,TOPLEFT,TOPRIGHT,BOTTOMRIGHT,MAP_LNGLAT_RELA_GPS_ID from RNO_MAP_LNGLAT_RELA_GPS where areaid='"+areaid+"' ORDER BY bottomleft";
		final String sql = "SELECT * from RNO_MAP_LNGLAT_RELA_GPS WHERE AREAID='"
				+ areaid
				+ "' and MAPTYPE='"
				+ mapType
				+ "' ORDER BY BOTTOMLEFT";
		// System.out.println(sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoMapLnglatRelaGps>>() {
					public List<RnoMapLnglatRelaGps> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						query.addEntity(RnoMapLnglatRelaGps.class);
						List list = query.list();
						/*
						 * for (int i = 0; i < list.size(); i++) {
						 * System.out.println
						 * (((RnoMapLnglatRelaGps)list.get(i)).getGps()); }
						 */
						return list;
					}
				});

	}

	/**
	 * 获取指定类型的数据
	 * 
	 * @param class1
	 * @param idField
	 * @param areaIdField
	 * @param cfids
	 * @param areaIds
	 *            如果areaIds为空，则表示查询全部
	 * @author brightming 2013-11-27 下午2:50:11
	 */
	public List getObjectByIdsInArea(final Class<?> class1,
			final String idField, final String areaIdField,
			final List<Long> configIds, final List<Long> areaIds) {
		if (configIds == null || configIds.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		try {
			return hibernateTemplate.executeFind(new HibernateCallback<List>() {

				public List doInHibernate(Session arg0)
						throws HibernateException, SQLException {

					Criteria criteria = arg0.createCriteria(class1);
					List<Long> cids = configIds;
					if (cids == null) {
						cids = new ArrayList<Long>();
					}
					List<Long> as = areaIds;
					if (as == null) {
						as = new ArrayList<Long>();
					}
					if (areaIds != null && areaIds.size() > 0) {
						criteria.add(Restrictions.and(
								Restrictions.in(idField, cids),
								Restrictions.in(areaIdField, as)));
					} else {
						criteria.add(Restrictions.in(idField, cids));
					}
					return criteria.list();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * 获取城市下所有的bsc
	 * 
	 * @param cityId
	 * @return
	 * @author brightming 2014-2-10 下午3:57:14
	 */
	public List<Map<String, Object>> getAllBscsInCity(final long cityId) {
		log.info("进入方法：getAllBscsInCity. cityId=" + cityId);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				List<Long> subAreas = AuthDsDataDaoImpl
						.getSubAreaIdsByCityId(cityId);
				String areaStrs = "";
				for (Long id : subAreas) {
					areaStrs += id + ",";
				}
				if (areaStrs.length() > 0) {
					areaStrs = areaStrs.substring(0, areaStrs.length()-1);
				}
				if (areaStrs.length() > 0) {
					String sql = "select bsc_id,engname from rno_bsc where bsc_id in (select bsc_id from RNO_BSC_RELA_AREA where area_id in ("
							+ areaStrs + "))";
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					return query.list();
				} else {
					return Collections.EMPTY_LIST;
				}
			}
		});
	}

	/**
	 * 获取指定bsc下的所有小区
	 * 
	 * @param bsc
	 * @return
	 * @author brightming 2014-2-10 下午4:04:41
	 */
	public List<Map<String, Object>> getAllCellsInBsc(final long bscId) {
		log.info("进入方法：getAllCellsInBsc. bscId=" + bscId);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {

				String sql = "select LABEL,name from CELL where bsc_id="
						+ bscId;
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 模糊匹配小区
	 * 
	 * @param cityId
	 * @param cellWord
	 * @return
	 * @author brightming 2014-2-10 下午3:55:04
	 */
	public List<Map<String, Object>> findCellWithPartlyWords(final long cityId,
			final String cellWord) {
		log.info("进入方法：findCellWithPartlyWords。cityId=" + cityId + ",cellWord="
				+ cellWord);
		if (cellWord == null || "".equals(cellWord.trim())) {
			log.warn("传入的搜索小区词是空的！");
			return Collections.EMPTY_LIST;
		}
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				List<Long> subAreas = AuthDsDataDaoImpl
						.getSubAreaIdsByCityId(cityId);
				String areaStrs = "";
				for (Long id : subAreas) {
					areaStrs += id + ",";
				}
				if (areaStrs.length() > 0) {
					areaStrs = areaStrs.substring(0, areaStrs.length()-1);
				}
				if (areaStrs.length() > 0) {
					String sql = "select mid1.*,rno_bsc.engname from (select label,name,bsc_id from cell where area_id in ( "
							+ areaStrs
							+ " ) and label like '%"
							+ cellWord.toUpperCase()
							+ "%')mid1 "
							+ "inner join rno_bsc on mid1.bsc_id=rno_bsc.bsc_id";
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					return query.list();
				} else {
					return Collections.EMPTY_LIST;
				}
			}
		});
	}

	/**
	 * 获取指定城市下的bsc和小区
	 * 
	 * @param cityId
	 * @return key:engname为bsc名称 key:label为cell名称 key:name为cell中文名称
	 * 
	 * @author brightming 2014-2-11 下午12:09:05
	 */
	public List<Map<String, Object>> getBscCellInCity(final long cityId) {
		log.info("进入方法：getBscCellInCity. cityId=" + cityId);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {

				String areaIdStrs=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
				String sql = "select mid1.label,mid1.name, bsc.engname,bsc.manufacturers  from " +
						" ( select distinct(label),name,bsc_id from cell " +
								" where area_id in ("+areaIdStrs+"))mid1 " +
						" left join (select bsc_id,engname,manufacturers from rno_bsc where status='N') bsc" +
							" on mid1.bsc_id=bsc.bsc_id " +
						" order by bsc.engname,mid1.label";
				SQLQuery query = arg0.createSQLQuery(sql);
				log.info("获取指定城市下的bsc和小区,sql="+sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 保存数据上传的记录
	 * 
	 * @param dataRec
	 * @return
	 * @author brightming 2014-8-21 下午3:51:58
	 */
	public long saveDataUploadRec(final RnoDataCollectRec dataRec) {
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				Connection conn = SessionFactoryUtils.getDataSource(
						session.getSessionFactory()).getConnection();
				long dataId = RnoHelper.getNextSeqValue(
						"SEQ_RNO_DATA_COLLECT_ID", conn);
				try{
					conn.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				DateUtil dateUtil=new DateUtil();
				String sql = "insert into RNO_DATA_COLLECT_REC(DATA_COLLECT_ID,UPLOAD_TIME,BUSINESS_TIME,FILE_NAME,ORI_FILE_NAME,ACCOUNT,CITY_ID,BUSINESS_DATA_TYPE,FILE_SIZE,FULL_PATH,FILE_STATUS,JOB_ID) "
						+ "values("
						+ dataId
						+ ",to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(dataRec
								.getUploadTime())
						+ "','yyyy-mm-dd HH24:mi:ss'),to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(dataRec
								.getBusinessTime())
						+ "','yyyy-mm-dd HH24:mi:ss'),"
						+ "'"
						+ dataRec.getFileName()
						+ "','"
						+ dataRec.getOriFileName()
						+ "','"
						+ dataRec.getAccount()
						+ "',"
						+ dataRec.getCityId()
						+ ","
						+ dataRec.getBusinessDataType()
						+ ","
						+ dataRec.getFileSize()
						+ ",'"
						+ dataRec.getFullPath()
						+ "','"
						+ dataRec.getFileStatus()
						+ "',"
						+ dataRec.getJobId() + ")";
				log.debug("保存数据上传记录的sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.executeUpdate();
				return dataId;
			}
		});
	}

	/**
	 * 保存用户的配置
	 * 
	 * @author peng.jm
	 * @date 2014-9-25下午02:51:15
	 */
	public boolean saveUserConfig(final String account, final long cityId) {
		log.info("进入saveUserConfig。 account=" + account + ", cityId=" + cityId);
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = " MERGE INTO RNO_USER_CONFIG t "
						+ "	USING (SELECT '" + account
						+ "' ACCOUNT FROM dual) t2  "
						+ "	  ON ( t.ACCOUNT = t2.ACCOUNT)  "
						+ "	WHEN MATCHED THEN   "
						+ "	  UPDATE SET t.ATTEN_CITY_ID =" + cityId
						+ "	WHEN NOT MATCHED THEN   "
						+ "	  INSERT (ACCOUNT,ATTEN_CITY_ID) " + " VALUES('"
						+ account + "'," + cityId + ") ";
				log.debug("saveUserConfig的sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int num = query.executeUpdate();
				log.debug("saveUserConfig的受影响行数，num=" + num);
				if (num > 0) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	/**
	 * 获取用户配置的默认城市id
	 * 
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-9-25下午04:12:21
	 */
	public long getUserConfigAreaId(final String account) {
		log.info("进入saveUserConfig。 account=" + account);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql = "select atten_city_id from rno_user_config where account='"
						+ account + "'";
				log.debug("getUserConfigAreaId的sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long result = -1;
				if (res != null) {
					result = res.longValue();
				}
				log.debug("getUserConfigAreaId的结果为，cityId=" + result);
				return result;
			}
		});
	}

	/**
	 * 通过areaStr获取所有BSC
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午02:04:29
	 */
	public List<Map<String, Object>> getAllBscByAreaStr(final String areaStr) {
		log.info("进入getAllBscByAreaStr。 areaStr=" + areaStr);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql = "select * from rno_bsc" +
						" where status = 'N' " +
							"and bsc_id in (select bsc_id from rno_bsc_rela_area" +
							" where area_id in("+areaStr+")) order by engname";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 获取指定城市下的LTE enodeb和小区
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-9-14下午2:58:48
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getLteCellInCity(final long cityId) { 
		log.info("进入方法：getLteCellInCity. cityId=" + cityId);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {

				String areaIdStrs=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
				String sql = "select t1.business_cell_id label,		   " 	
						+"       t1.cell_name name,                    "
						+"       t2.enodeb_name engname,               "
						+"       '1' manufacturers                     "
						+"  from (select distinct (business_cell_id),  "
						+"                        enodeb_id,           "
						+"                        cell_name,           "
						+"                        '1' manufacturers    "
						+"          from rno_lte_cell                  "
						+"         where area_id in ("+areaIdStrs+")) t1"
						+"  left join (select enodeb_id, enodeb_name   "
						+"               from rno_lte_enodeb           "
						+"              where area_id in ("+areaIdStrs+")) t2"
						+"    on t1.enodeb_id = t2.enodeb_id           "
						+" order by t2.enodeb_name, t1.business_cell_id" ;
				
				sql =  "select distinct (business_cell_id) label,"
						+"                'TAC_'||tac engname,             "
						+"                cell_name name,          "
						+"                '1' manufacturers        "
						+"  from rno_lte_cell                      "
						+" where area_id in ("+areaIdStrs+") and tac !=0      ";
				SQLQuery query = arg0.createSQLQuery(sql);
				log.info("获取指定城市下的lte enodeb和小区,sql="+sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> cells= query.list();
				return cells;
			}
		});
	}
}
