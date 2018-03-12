package com.iscreate.op.dao.rno;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoNcell;
import com.iscreate.plat.workflow.SessionFactory;
import com.sun.istack.internal.FinalArrayList;

public class RnoCellDaoImpl implements RnoCellDao {

	private static Log log = LogFactory.getLog(RnoCellDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 获取指定区域下的所有小区名
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCellNameByAreaId(final Long areaId) {
		List<String> names = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						List<String> names = new ArrayList<String>();
						SQLQuery query = arg0
								.createSQLQuery("select LABEL from CELL where AREA_ID=?");
						query.setLong(0, areaId);
						List<Object> objects = query.list();
						if (objects == null || objects.size() == 0) {
							return names;
						}

						for (Object o : objects) {
							names.add((String) o);
						}

						return names;
					}
				});
		return names;
	}

	/**
	 * 获取区域的默认配置方案id
	 * 
	 * @param areaId
	 * @return Sep 12, 2013 3:51:24 PM gmh
	 */
	public RnoCellDescriptor getSystemCellDescriptorByAreaId(final Long areaId) {
		return hibernateTemplate
				.execute(new HibernateCallback<RnoCellDescriptor>() {
					public RnoCellDescriptor doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						Criteria criteria = arg0
								.createCriteria(RnoCellDescriptor.class);
						criteria.add(Restrictions.eq("areaId", areaId));
						criteria.add(Restrictions.eq("defaultDescriptor",
								RnoConstant.StatusConstant.Yes));
						List list = criteria.list();
						if (list != null && !list.isEmpty()) {
							return (RnoCellDescriptor) list.get(0);
						} else {
							return null;
						}
					}

				});
	}

	/**
	 * 增加指定区域的默认配置方案
	 * 
	 * @param areaId
	 * @param cellDesc
	 * @return Sep 12, 2013 3:53:52 PM gmh
	 */
	public Long saveSystemCellDecriptorUnderArea(final Long areaId,
			RnoCellDescriptor cellDesc) {
		log.debug("进入saveSystemCellDecriptorUnderArea方法.areaId=" + areaId
				+ ",cellDesc=" + cellDesc);
		if (cellDesc == null || areaId == null) {
			log.error("saveSystemCellDecriptorUnderArea的参数不能为空！");
			return null;
		}
		cellDesc.setAreaId(areaId);
		cellDesc.setDefaultDescriptor(RnoConstant.StatusConstant.Yes);
		Date time = new Date();
		cellDesc.setCreateTime(time);
		cellDesc.setModTime(time);
		cellDesc.setStatus(RnoConstant.StatusConstant.Normal);
		cellDesc.setTempStorage(RnoConstant.StatusConstant.No);

		Long id = (Long) hibernateTemplate.save(cellDesc);
		log.debug("退出saveSystemCellDecriptorUnderArea方法，返回值："
				+ (id == null ? "null" : id.longValue()));
		return id;
	}

	/**
	 * 分页查询指定条件的小区
	 * 
	 * @param page
	 * @param where
	 * @return Sep 16, 2013 3:54:02 PM gmh
	 */
	public List<Cell> queryCellByPage(final Page page, final String where) {
		log.info("进入方法：queryCellByPage .page=" + page + ",where=" + where);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Cell>>() {
					public List<Cell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field = "ID,NAME,LABEL,IFSHARETG,COVERTYPE,COVERAREA,IMPORTANCEGRADE,TAKEOVERDATE,IFPASSCHECKING,PASSCHECKINGDATE,IFTRANSSINGLELINE,GSMFREQUENCESECTION,ADDRESS,LAC,CI,BSIC,BCCH,TCH,ANT_MANUFACTORY,ANT_TYPE,ANT_GAIN,ANT_HEIGH,BASETYPE,DOWNTILT,BEARING,MAX_TX_BS,MAX_TX_MS,ENTITY_ID,ENTITY_TYPE,ANT_MODEL,ELECTRICALDOWNTILT,MECHANICALDOWNTILT,LONGITUDE,LATITUDE,SITE,BSC_ID,BTSTYPE,CELL_DESC_ID,AREA_ID,LNGLATS";
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						String sql = "select " + field + " from (select "
								+ field + ",rownum rn from cell  "
								+ whereResult + " ) where  rn>=? and rn<=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						query.setLong(0, start);
						query.setLong(1, end);

						query.addEntity(Cell.class);
						// query.setResultTransformer(Transformers.aliasToBean(Cell.class));

						List<Cell> cells = query.list();
						// List list = query.list();
						// System.out.println("list size=" + list.size());
						log.info("获取到结果集记录数量："
								+ (cells == null ? 0 : cells.size()));
						return cells;
					}
				});
	}

	/**
	 * 获取符合条件的记录的数量
	 * 
	 * @param where
	 * @return Sep 16, 2013 4:20:57 PM gmh
	 */
	public int getTotalCellCntMeetCondition(final String where) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String w = (where == null || where.trim().isEmpty()) ? ""
						: " where " + where;
				String sql = "select count(label) from CELL " + w;
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
	 * 分页查询满足条件的邻区
	 * 
	 * @param page
	 * @param where
	 * @return Sep 17, 2013 3:47:44 PM gmh
	 */
	public List<RnoNcell> queryNCellByPage(final Page page, final String where) {
		log.info("进入方法：queryNCellByPage .page=" + page + ",where=" + where);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoNcell>>() {
					public List<RnoNcell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// String field =
						// "ID,NAME,LABEL,IFSHARETG,COVERTYPE,COVERAREA,IMPORTANCEGRADE,TAKEOVERDATE,IFPASSCHECKING,PASSCHECKINGDATE,IFTRANSSINGLELINE,GSMFREQUENCESECTION,ADDRESS,LAC,CI,BSIC,BCCH,TCH,ANT_MANUFACTORY,ANT_TYPE,ANT_GAIN,ANT_HEIGH,BASETYPE,DOWNTILT,BEARING,MAX_TX_BS,MAX_TX_MS,ENTITY_ID,ENTITY_TYPE,ANT_MODEL,ELECTRICALDOWNTILT,MECHANICALDOWNTILT,LONGITUDE,LATITUDE,SITE,BSC_ID,BTSTYPE,CELL_DESC_ID,AREA_ID,LNGLATS";
						String field = "NCELL_ID,CELL,NCELL,CS,DIR,BSC_ID";
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						String sql = "select " + field + " from (select "
								+ field + ",rownum rn from rno_ncell  "
								+ whereResult + " ) where  rn>=? and rn<=?";
						SQLQuery query = arg0.createSQLQuery(sql);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						query.setLong(0, start);
						query.setLong(1, end);
						log.info("邻区查询的sql语句：" + sql);
						log.info("start=" + start + ",end=" + end);

						query.addEntity(RnoNcell.class);
						List<RnoNcell> ncells = query.list();
						log.info("获取到结果集记录数量："
								+ (ncells == null ? 0 : ncells.size()));
						return ncells;
					}
				});
	}

	/**
	 * 获取满足条件的邻区关系的数量
	 * 
	 * @param buildNcellQuerySqlWhere
	 * @return Sep 17, 2013 4:33:53 PM gmh
	 */
	public int getTotalNcellCntMeetCondition(
			final String buildNcellQuerySqlWhere) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String w = (buildNcellQuerySqlWhere == null || buildNcellQuerySqlWhere
						.trim().isEmpty()) ? "" : " where "
						+ buildNcellQuerySqlWhere;
				String sql = "select count(NCELL_ID) from RNO_NCELL " + w;
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
	 * 删除id列表指定的邻区
	 * 
	 * @param ncellIds
	 *            Sep 17, 2013 5:40:27 PM gmh
	 */
	public int deleteNcellByIds(final List<Long> allAllowAreaIds,
			final String ncellIds) {
		log.info("进入：deleteNcellByIds。 allAllowAreaIds=" + allAllowAreaIds
				+ ",ncellIds=" + ncellIds);

		int re = hibernateTemplate.execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				StringBuilder buf = new StringBuilder();
				buf.append(" (");
				if (allAllowAreaIds != null && allAllowAreaIds.size() > 0) {
					for (Long id : allAllowAreaIds) {
						if (id == null) {
							continue;
						} else {
							buf.append(id.longValue() + ",");
						}
					}
					if (buf.length() > 3) {
						buf.deleteCharAt(buf.length() - 1);
					}
				}
				buf.append(")");
				String sql = "delete from rno_ncell where ncell_id in ( "
						+ ncellIds
						+ " ) and BSC_ID in ( SELECT BSC_ID FROM RNO_BSC_RELA_AREA WHERE AREA_ID IN "
						+ buf.toString() + ")";
				log.info("删除邻区的sql语句为：" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int result = query.executeUpdate();
				return result;
			}
		});

		log.info("退出：deleteNcellByIds。 删除的记录数：" + re);
		return re;
	}

	/**
	 * 根据小区label获取小区详情
	 * 
	 * @param label
	 * @return Sep 18, 2013 5:51:03 PM gmh
	 */
	public Cell getCellByLabel(final String label) {
		return hibernateTemplate.execute(new HibernateCallback<Cell>() {
			public Cell doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				System.out.println("-----label:"+label);
				Criteria criteria = arg0.createCriteria(Cell.class);
				criteria.add(Restrictions.eq("label", label));
				List<Cell> list = criteria.list();
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				return null;
			}

		});
	}
	
	/**
	 * 通过小区名获取小区详情
	 * chen.c10
	 */
	public RnoLteCell getLteCellByBusCellId(final String busCellId) {
		return hibernateTemplate.execute(new HibernateCallback<RnoLteCell>() {
			public RnoLteCell doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql = " select rlc.CELL_NAME as chineseName," +
						"rlc.LTE_CELL_ID as id," +
				 		"rlc.business_cell_id as cell," +
				 		"rlc.LONGITUDE as lng," +
				 		"rlc.LATITUDE as lat," +
				 		"rlc.PCI as pci," +
				 		"rlc.AZIMUTH as azimuth," +
						"rlc.BAND_TYPE as bandType," +
						"rlc.COVER_TYPE as coverType," +
						"rlc.BAND as band," +
						"rlc.EARFCN as earfcn," +
						"rlc.GROUND_HEIGHT as groundHeight," +
						"rlc.RRUNUM as rrunum," +
						"rlc.RRUVER as rruver," +
						"rlc.ANTENNA_TYPE as antennaType," +
						"rlc.INTEGRATED as integrated," +
						"rlc.RSPOWER as rspower," +
						"rlc.COVER_RANGE as coverRange " +
					" from RNO_LTE_CELL rlc " +
					" where rlc.business_cell_id = "+busCellId;
				log.info("获取指定id的lte小区详情的sql：" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
	//			query.setString(0, busCellId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,Object>> result = query.list();
				Map<String,Object> map2= result.get(0);
				for(String s:map2.keySet()){
					Object v=map2.get(s);
				//	System.out.println("----------------key:"+s+"-----value:"+v);
				}
				Map<String,Object> map;
				List<RnoLteCell> list = new ArrayList<RnoLteCell>();
				for (int i = 0; i < result.size(); i++) {
					map = (Map<String,Object>)result.get(i);
					RnoLteCell lteCell = new RnoLteCell();
					lteCell.setId(((BigDecimal)map.get("ID")).longValue());
					lteCell.setCell(map.get("CELL").toString());
					if(map.get("CHINESENAME")==null){
						lteCell.setChineseName(null);
					} else {
						lteCell.setChineseName(map.get("CHINESENAME").toString());
					}
					if(map.get("LNG")==null){
						lteCell.setLng(null);
					} else {
						lteCell.setLng(((BigDecimal)map.get("LNG")).doubleValue());
					}
					if(map.get("LAT")==null){
						lteCell.setLat(null);
					} else {
						lteCell.setLat(((BigDecimal)map.get("LAT")).doubleValue());
					}
					if(map.get("PCI")==null){
						lteCell.setPci(null);
					} else {
						lteCell.setPci(map.get("PCI").toString());
					}
					if(map.get("AZIMUTH")==null){
						lteCell.setAzimuth(null);
					} else {
						lteCell.setAzimuth(((BigDecimal)map.get("AZIMUTH")).floatValue());
					}
					if(map.get("BANDTYPE")==null){
						lteCell.setBandType(null);
					} else {
						lteCell.setBandType(map.get("BANDTYPE").toString());
					}
					if(map.get("COVERTYPE")==null){
						lteCell.setCoverType(null);
					} else {
						lteCell.setCoverType(map.get("COVERTYPE").toString());
					}
					if(map.get("BAND")==null){
						lteCell.setBand(null);
					} else {
						lteCell.setBand(map.get("BAND").toString());
					}
					if(map.get("EARFCN")==null){
						lteCell.setEarfcn(null);
					} else {
						lteCell.setEarfcn(map.get("EARFCN").toString());
					}
					if(map.get("GROUNDHEIGHT")==null){
						lteCell.setGroundHeight(null);
					} else {
						lteCell.setGroundHeight(((BigDecimal) map.get("GROUNDHEIGHT")).longValue());
					}
					if(map.get("RRUNUM")==null){
						lteCell.setRrunum(null);
					} else {
						lteCell.setRrunum(((BigDecimal) map.get("RRUNUM")).longValue());
					}
					if(map.get("RRUVER")==null){
						lteCell.setRruver(null);
					} else {
						lteCell.setRruver(map.get("RRUVER").toString());
					}
					if(map.get("ANTENNATYPE")==null){
						lteCell.setAntennaType(null);
					} else {
						lteCell.setAntennaType(map.get("ANTENNATYPE").toString());
					}
					if(map.get("INTEGRATED")==null){
						lteCell.setIntegrated(null);
					} else {
						lteCell.setIntegrated(map.get("INTEGRATED").toString());
					}
					if(map.get("RSPOWER")==null){
						lteCell.setRspower(null);
					} else {
						lteCell.setRspower(((BigDecimal) map.get("RSPOWER")).longValue());
					}
					if(map.get("COVERRANGE")==null){
						lteCell.setCoverRange(null);
					} else {
						lteCell.setCoverRange(map.get("COVERRANGE").toString());
					}
					list.add(lteCell);
				}
				if (list != null && list.size() > 0) {
					return list.get(0);
				}
				return null;
			}
		});
	}

	/**
	 * 通过小区ID查询一条小区数据
	 */
	public Cell queryCellById(final long id) {
		return hibernateTemplate.get(Cell.class, id);
	}

	/**
	 * 传入一个小区对象并更新保存
	 */
	public void updateCellInfo(Cell cell) {
		// TODO Auto-generated method stub
		//System.out.println("dao:"+cell.getLngLats());
		hibernateTemplate.saveOrUpdate(cell);
	}

	/**
	 * 获取指定区域下的小区的邻区
	 * @param cell
	 * @return
	 * @author brightming
	 * 2013-9-25 上午11:19:41
	 */
	public List<RnoNcell> getNcellByCell(final String cell) {
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoNcell>>(){
			public List<RnoNcell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				return arg0.createCriteria(RnoNcell.class).add(Restrictions.eq("cell", cell)).list();
			}
		});
	}

	/**
	 * 执行查询
	 * @param sql
	 * @return
	 * @author brightming
	 * 2013-10-30 下午5:40:27
	 */
	public List<Map<String, Object>> executeQuery(final String sql){
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list; 
	}
	/**
	 * 从小区配置描述表中得到最新的小区配置描述标识CELL_DESCRIPTOR_ID
	 */
	public long saveCellDescAndGetId(RnoCellDescriptor rnoCellDescriptor) {
		
		Session session=this.hibernateTemplate.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(rnoCellDescriptor);
		tx.commit();
		Long id=rnoCellDescriptor.getCellDescriptorId();
		
		session.close();
		return id.longValue();
	}
	/**
	 * 获取指定区/县区域下的小区通过配置和区域
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2014-4-15上午11:59:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoGisCell> getRnoGisCellUseConfigIdOrArea(boolean reSelected,
			String areaIds, String configIds) {
		log.info("进入方法getRnoGisCellUseConfigIdOrArea:  boolean reSelected,String areaIds, String configIds: " + reSelected+":"+areaIds+":"+configIds);
		String sqlString = "";
		if (reSelected) {
			sqlString = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch from CELL c where c.AREA_ID  in(SELECT AREA_ID from RNO_CELL_DESCRIPTOR WHERE CELL_DESCRIPTOR_ID IN ("
					+ configIds + "))";
		} else {
			sqlString = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch from CELL c where c.AREA_ID  in("
					+ areaIds + ")";
		}
		final String sql = sqlString;
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						// String sql = "select c.label as cell,c.name as
						// chineseName,c.LONGITUDE as lng,c.LATITUDE as
						// lat,c.LNGLATS as allLngLats,c.BEARING as
						// azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as
						// site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as
						// tch from CELL c where exists (select 1 from SYS_AREA
						// s where c.AREA_ID = s.AREA_ID start with s.AREA_ID =
						// ? connect by prior s.AREA_ID = s.PARENT_ID)";
						log.info("获取指定区域下的小区的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoGisCell.class);
						List<RnoGisCell> gisCells = query.list();
						log.info("getRnoGisCellUseConfigIdOrArea获取的结果："
								+ gisCells == null ? 0 : gisCells.size());
						return gisCells;
					}
				});
	}
	
	
	/**
	 * 获取指定区域指定频点类型的小区的数量
	 * @param areaId
	 * @param freqType
	 * @return
	 * @author brightming
	 * 2014-8-11 上午11:38:08
	 */
	public int getRnoGisCellCntByFreqTypeInArea(final long areaId,final String freqType){
		log.info("进入方法getRnoGisCellCntByFreqTypeInArea:  areaId: "+areaId+",freqType="+freqType);
		String wheresqlString="";
		if ("GSM900".equals(freqType)) {
			wheresqlString=" ( BCCH<100 or BCCH IN(1020,1021,1022,1023,0)) AND ";
		}
		if ("GSM1800".equals(freqType)) {
			wheresqlString=" BCCH>100 AND BCCH<1000 AND ";
		}
		
		List<Long> subAreas = AuthDsDataDaoImpl
				.getSubAreaIdsByCityId(areaId);
		String areaStrs = areaId+",";
		for (Long id : subAreas) {
			areaStrs += id + ",";
		}
		if (areaStrs.length() > 0) {
			areaStrs = areaStrs.substring(0, areaStrs.length()-1);
		}else{
			areaStrs=areaId+"";
		}
		
		String where=wheresqlString+" area_id in ( "+areaStrs+") ";
		return getTotalCellCntMeetCondition(where);
	}
	
	/**
	 * 
	 * @title 获取指定区/县区域下的小区(带频点类型)
	 * @param areaId
	 * @param freqType
	 * @return
	 * @author chao.xj
	 * @date 2014-6-27下午07:22:32
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoGisCell> getAllRnoGisCellByFreqTypeInArea(final long areaId,final String freqType) {
		log.info("进入方法getAllRnoGisCellByFreqTypeInArea:  areaId: "+areaId+",freqType="+freqType);
		String wheresqlString="";
		if ("GSM900".equals(freqType)) {
			wheresqlString=" ( BCCH<100 or BCCH IN(1020,1021,1022,1023,0) AND ";
		}
		if ("GSM1800".equals(freqType)) {
			wheresqlString=" BCCH>100 AND BCCH<1000 AND ";
		}
		final String wheresql=wheresqlString;
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						
						List<Long> subAreas = AuthDsDataDaoImpl
								.getSubAreaIdsByCityId(areaId);
						String areaStrs = areaId+",";
						for (Long id : subAreas) {
							areaStrs += id + ",";
						}
						if (areaStrs.length() > 0) {
							areaStrs = areaStrs.substring(0, areaStrs.length()-1);
						}else{
							areaStrs=areaId+"";
						}
						
						String sql = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch  from CELL c " +
								" where "+wheresql+" c.area_id in ( "+areaStrs+" ) ";//exists (select 1 from SYS_AREA s where c.AREA_ID = s.AREA_ID start with s.AREA_ID = ? connect by prior s.AREA_ID = s.PARENT_ID)";
						log.info("获取指定区域下的小区的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoGisCell.class);
						List<RnoGisCell> gisCells = query.list();
						log.info("getRnoGisCellInArea获取的结果：" + gisCells == null ? 0
								: gisCells.size());
						return gisCells;
					}
				});
	}
	
	/**
	 * 分页获取指定频点类型的小区数据
	 * @param areaId
	 * @param freqType
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-11 上午11:08:29
	 */
	public List<RnoGisCell> getRnoGisCellInAreaByFreqTypeAndPage(final long areaId,
			String freqType, final Page page){
		log.debug("进入方法getRnoGisCellInAreaByFreqTypeAndPage:  areaId: "+areaId+",freqType="+freqType+",PAGE="+page);
		String wheresqlString="";
		if ("GSM900".equals(freqType)) {
			wheresqlString=" ( BCCH<100 or BCCH IN(1020,1021,1022,1023,0)) AND ";
		}
		if ("GSM1800".equals(freqType)) {
			wheresqlString=" BCCH>100 AND BCCH<1000 AND ";
		}
		final String wheresql=wheresqlString;
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						
						
						List<Long> subAreas = AuthDsDataDaoImpl
								.getSubAreaIdsByCityId(areaId);
						String areaStrs = areaId+",";
						for (Long id : subAreas) {
							areaStrs += id + ",";
						}
						if (areaStrs.length() > 0) {
							areaStrs = areaStrs.substring(0, areaStrs.length()-1);
						}else{
							areaStrs=areaId+"";
						}
						
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						
						//rownum从1开始
						String sql = "select * from (select rownum rn,c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch  from CELL c " +
								" where "+wheresql+" area_id in ( " +areaStrs+" ) "//exists (select 1 from SYS_AREA s where c.AREA_ID = s.AREA_ID start with s.AREA_ID = ? connect by prior s.AREA_ID = s.PARENT_ID)";
								+" ) where rn>=? and rn<=? ";
						log.info("分页获取指定区域下的指定频点类型小区的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoGisCell.class);
						query.setLong(0, start);
						query.setLong(1, end);
						
						
						List<RnoGisCell> gisCells = query.list();
						log.info("getRnoGisCellInAreaByFreqTypeAndPage获取的结果：" + gisCells == null ? 0
								: gisCells.size());
						return gisCells;
					}
				});
	}
	
	/**
	 * 获取某个区域下的小区数量
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2014-8-11 上午10:39:34
	 */
	public int getGisCellCntInArea(long areaId){
		return getRnoGisCellCntByFreqTypeInArea(areaId,"");
	}
	
	/**
	 * 获取指定区/县区域下的小区
	 * 
	 * @param areaId
	 * @return Sep 17, 2013 2:02:04 PM gmh
	 */
	public List<RnoGisCell> getRnoGisCellInArea(final long areaId) {
		log.info("进入方法getRnoGisCellInArea:  areaId: "+areaId);
		return getAllRnoGisCellByFreqTypeInArea(areaId, "");
//		return hibernateTemplate
//				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
//					public List<RnoGisCell> doInHibernate(Session arg0)
//							throws HibernateException, SQLException {
//						
//						String sql = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch  from CELL c where exists (select 1 from SYS_AREA s where c.AREA_ID = s.AREA_ID start with s.AREA_ID = ? connect by prior s.AREA_ID = s.PARENT_ID)";
//						log.info("获取指定区域下的小区的sql：" + sql);
//						SQLQuery query = arg0.createSQLQuery(sql);
//						query.addEntity(RnoGisCell.class);
//						query.setLong(0, areaId);
//						List<RnoGisCell> gisCells = query.list();
//						log.info("getRnoGisCellInArea获取的结果：" + gisCells == null ? 0
//								: gisCells.size());
//						return gisCells;
//					}
//				});
	}
	
	
	/**
	 * 分页获取指定区/县区域下的小区
	 * @param areaId
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-11 上午10:22:31
	 */
	public List<RnoGisCell> getRnoGisCellInAreaByPage(final long areaId,final Page page) {
		log.info("进入方法getRnoGisCellInAreaByPage:  areaId: "+areaId);
		return getRnoGisCellInAreaByFreqTypeAndPage(areaId, "", page);
//		return hibernateTemplate
//				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
//					public List<RnoGisCell> doInHibernate(Session arg0)
//							throws HibernateException, SQLException {
//						
//						
//						int start = (page.getPageSize()
//								* (page.getCurrentPage() - 1) + 1);
//						int end = (page.getPageSize() * page.getCurrentPage());
//						
//						//rownum从1开始
//						String sql = "select * from (select rownum rn,c.label as cell,c.name as chineseName,c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ,c.TCH as tch  from CELL c where exists (select 1 from SYS_AREA s where c.AREA_ID = s.AREA_ID start with s.AREA_ID = ? connect by prior s.AREA_ID = s.PARENT_ID)) where rn>=? and rn<=? ";
//						log.info("分页获取指定区域下的小区的sql：" + sql);
//						SQLQuery query = arg0.createSQLQuery(sql);
//						query.addEntity(RnoGisCell.class);
//						query.setLong(0, areaId);
//						query.setLong(1, start);
//						query.setLong(2, end);
//						
//						List<RnoGisCell> gisCells = query.list();
//						log.info("getRnoGisCellInAreaByPage获取的结果：" + gisCells == null ? 0
//								: gisCells.size());
//						return gisCells;
//					}
//				});
	}


	/**
	 * 通过区域id获取其所有子区域（包括自身）
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午04:27:15
	 */
/*	public List<Map<String, Object>> getSubAreaByAreaId(long areaId) {
		log.info("进入方法getSubAreaByAreaId，areaId="+areaId);
		final String sql = "select AREA_ID	"
					+"	  from SYS_AREA s	"
					+"	 start with s.AREA_ID =" + areaId
					+"	connect by prior s.AREA_ID = s.PARENT_ID";
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				log.info("通过区域id获取其所有子区域的sql="+sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
	}*/
	
	/**
	 * 获取指定区域的网格下的指定频点类型的小区数量
	 * @param areaStr 格式：xx,xx,xx
	 * @param blPoint
	 * @param trPoint
	 * @param freqType
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18下午02:36:44
	 */
	public int getRnoGisCellCntByGridInArea(String areaStr, String[] blPoint,
			String[] trPoint, String freqType) {
		log.info("进入方法:getRnoGisCellCntByGridInArea。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint + ", freqType="+ freqType);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
		String wheresqlString = "";
		if ("GSM900".equals(freqType)) {
			wheresqlString = " ( BCCH<100 or BCCH IN(1020,1021,1022,1023,0)) AND ";
		}
		if ("GSM1800".equals(freqType)) {
			wheresqlString = " BCCH>100 AND BCCH<1000 AND ";
		}
		wheresqlString += "  longitude > " + minLng + " and longitude < " + maxLng
						 + " and latitude > " + minLat + " and latitude < " + maxLat + " and ";
		final String where = wheresqlString 
						+ " area_id in ("+areaStr+") ";
		
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String w = (where == null || where.trim().isEmpty()) ? ""
						: " where " + where;
				String sql = "select count(label) from CELL " + w;
				log.info("获取指定区域的网格下的指定频点类型的小区数量的sql：" + sql);
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
	 * 分页获取指定区域的网格下的指定频点类型的小区
	 * @param areaStr 格式：xx,xx,xx
	 * @param freqType
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18下午02:36:55
	 */
	@SuppressWarnings("unchecked")
	public List<RnoGisCell> getRnoGisCellByGridAndPageInArea(final String areaStr, String[] blPoint,
			String[] trPoint, String freqType, final Page page) {
		log.info("进入方法:getRnoGisCellByGridAndPageInArea。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint 
				+ ", freqType="+ freqType+", page="+page);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
		String wheresqlString = "";
		if ("GSM900".equals(freqType)) {
			wheresqlString = " ( BCCH<100 or BCCH IN(1020,1021,1022,1023,0)) AND ";
		}
		if ("GSM1800".equals(freqType)) {
			wheresqlString = " BCCH>100 AND BCCH<1000 AND ";
		}
		wheresqlString += "  longitude > " + minLng + " and longitude < " + maxLng
						 + " and latitude > " + minLat + " and latitude < " + maxLat + " and ";
		wheresqlString += " area_id in ("+areaStr+")";
		final String where = wheresqlString;
	
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoGisCell>>() {
					public List<RnoGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
								
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						
						//rownum从1开始
						String sql = "select * from (select rownum rn,c.label as cell,c.name as chineseName," +
								"c.LONGITUDE as lng,c.LATITUDE as lat,c.LNGLATS as allLngLats," +
								"c.BEARING as azimuth,c.GSMFREQUENCESECTION as freqType ," +
								"c.SITE as site,c.LAC as lac,c.CI as ci,c.BCCH as bcch ," +
								"c.TCH as tch  from CELL c " +
								" where "+where+" ) where rn>="+start+" and rn<="+end;
						log.info("分页获取指定区域的网格下的指定频点类型的小区的start="+start+",end="+end+",sql:" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoGisCell.class);
/*						query.setLong(0, start);
						query.setLong(1, end);*/
						
						List<RnoGisCell> gisCells = query.list();
						log.info("getRnoGisCellByGridAndPageInArea获取的结果：" + gisCells == null ? 0
								: gisCells.size());
						return gisCells;
					}
				});
	}

	/**
	 * 获取额外的giscell用于还没加载到却需要先显示的情况
	 * @param cells
	 * @param areaStr 格式：xx,xx,xx
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public List<RnoGisCell> getRelaCellByLabels(String cells, String areaStr) {
		log.info("进入方法getRelaCellByLabels。");
		final String where = " where c.label in ("+cells+") and c.area_id in ("+areaStr+")";
		
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<RnoGisCell>>() {
			public List<RnoGisCell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng," +
							"c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth," +
							"c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac," +
							"c.CI as ci,c.BCCH as bcch ,c.TCH as tch  " +
								" from CELL c " + where;
				log.info("根据小区label获取小区, sql:" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.addEntity(RnoGisCell.class);
				List<RnoGisCell> gisCells = query.list();
				log.info("getRelaCellByLabels获取的结果：" + gisCells == null ? 0
						: gisCells.size());
				return gisCells;
			}
		});
	}

	/**
	 * 通过小区参数获取cell进行小区数据预加载
	 * @param cellParam
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午07:06:07
	 */
	public List<RnoGisCell> getRelaCellByCellParamAndCityId(
			Map<String, String> cellParam, String areaStr) {
		log.info("进入方法getNcellDetailsByCellandCityId。cellParam="+cellParam+",areaStr="+areaStr);
		
		String sqlwhere = "";
		String val = cellParam.get("value").toString();
		String valType = cellParam.get("valueType").toString();
		if(("cell").equals(valType)) {
			sqlwhere += " where label like '%"+val+"%' and ";
		} else if(("chineseName").equals(valType)) {
			sqlwhere += " where name like '%"+val+"%' and ";
		} else if(("site").equals(valType)) {
			sqlwhere += " where site like '%"+val+"%' and ";
		} else if(("lac").equals(valType)) {
			sqlwhere += " where lac = "+val+" and ";
		} else if(("ci").equals(valType)) {
			sqlwhere += " where ci = "+val+" and ";
		}
		final String where = sqlwhere + " area_id in ("+areaStr+")";
		
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<RnoGisCell>>() {
			public List<RnoGisCell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "select cell,chineseName,lng,lat,allLngLats,azimuth,freqType,site,lac,ci,bcch,tch " +
							" from ( select rownum as rn, c.label as cell,c.name as chineseName,c.LONGITUDE as lng," +
								"c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth," +
								"c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac," +
								"c.CI as ci,c.BCCH as bcch ,c.TCH as tch " +
								" from CELL c " + where + ")  where rn > 0 and rn <= 100";
				log.info("通过小区参数获取cell(限制一百个), sql:" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.addEntity(RnoGisCell.class);
				List<RnoGisCell> gisCells = query.list();
				log.info("getRelaCellByLabels获取的结果：" + gisCells == null ? 0
						: gisCells.size());
				return gisCells;
			}
		});
	}
	
	/**
	 * 通过cell获取其邻区数据及主小区数据进行预加载
	 * @param cell
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午06:51:54
	 */
	public List<RnoGisCell> getNcellDetailsByCellandCityId(String cell,
			String areaStr) {
		log.info("进入方法getNcellDetailsByCellandCityId。cell="+cell+",areaStr="+areaStr);
		final String where = " where (label in (select ncell from rno_ncell" +
								" where cell = '"+cell+"') or " + " label = '"+cell+"')" +
						 " and area_id in ("+areaStr+")";
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<RnoGisCell>>() {
			public List<RnoGisCell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "select c.label as cell,c.name as chineseName,c.LONGITUDE as lng," +
							"c.LATITUDE as lat,c.LNGLATS as allLngLats,c.BEARING as azimuth," +
							"c.GSMFREQUENCESECTION as freqType ,c.SITE as site,c.LAC as lac," +
							"c.CI as ci,c.BCCH as bcch ,c.TCH as tch  " +
								" from CELL c " + where;
				log.info("通过cell获取其邻区数据及主小区数据, sql:" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.addEntity(RnoGisCell.class);
				List<RnoGisCell> gisCells = query.list();
				log.info("getRelaCellByLabels获取的结果：" + gisCells == null ? 0
						: gisCells.size());
				return gisCells;
			}
		});
	}

	/**
	 * 获取lte 小区
	 */
	@SuppressWarnings("unchecked")
	public List<RnoLteCell> getRnoLteCellByGridAndPageInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType, final Page page) {
		log.info("进入方法:getRnoLteCellByGridAndPageInArea。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint 
				+ ", freqType="+ freqType+", page="+page);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
		String wheresqlString = "";
		if ("E频段".equals(freqType)) {
			wheresqlString = " rlc.BAND_TYPE='E频段' AND ";
		}
		if ("D频段".equals(freqType)) {
			wheresqlString = " rlc.BAND_TYPE='D频段' AND ";
		}
		if ("F频段".equals(freqType)) {
			wheresqlString = " rlc.BAND_TYPE='F频段' AND ";
		}
		wheresqlString += "  rlc.longitude > " + minLng + " and rlc.longitude < " + maxLng
						 + " and rlc.latitude > " + minLat + " and rlc.latitude < " + maxLat + " and ";
		wheresqlString += " rlc.area_id in ("+areaStr+")";
		
		final String where = wheresqlString;
	
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoLteCell>>() {
					public List<RnoLteCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
								
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						
						//rownum从1开始
						String sql = "select * from (" +
										"select rownum rn, " +
								            " rlc.LTE_CELL_ID as id,"+
											" rlc.pci as pci," +
											" rlc.business_cell_id as cell," +
											" rlc.CELL_NAME    as chineseName," +
											" rlc.LONGITUDE    as lng," +
											" rlc.LATITUDE     as lat," +
											" rlc.AZIMUTH   as azimuth," +
											" rlcms.MAP_TYPE   as mapType," +
											" rlcms.SHAPE_TYPE as shapeType," +
											" rlcms.SHAPE_DATA as allLngLats" +
								" from  RNO_LTE_CELL rlc," +
								"  		RNO_LTE_CELL_MAP_SHAPE rlcms " +
								" where rlc.LTE_CELL_ID = rlcms.LTE_CELL_ID" +
								"    and "+where+" ) where rn>="+start+" and rn<="+end;
						log.info("分页获取指定区域的网格下的指定频点类型的小区的start="+start+",end="+end+",sql:" + sql);
						//System.out.println(sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> result = query.list();
						
						Map<String,Object> map = null;
						List<RnoLteCell> lteCells = new ArrayList<RnoLteCell>();
						RnoLteCell lteCell = null;

						for (int i = 0; i < result.size(); i++) {
							map = (Map<String,Object>)result.get(i);
							lteCell = new RnoLteCell();
							lteCell.setId(((BigDecimal) map.get("ID")).longValue());
							lteCell.setCell(map.get("CELL").toString());
							lteCell.setLng(((BigDecimal) map.get("LNG")).doubleValue());
							lteCell.setLat(((BigDecimal) map.get("LAT")).doubleValue());
							lteCell.setChineseName(map.get("CHINESENAME").toString());
							if(map.get("AZIMUTH")==null){
								lteCell.setAzimuth(null);
							} else {
								lteCell.setAzimuth(((BigDecimal)map.get("AZIMUTH")).floatValue());
							}
							lteCell.setMapType(map.get("MAPTYPE").toString());
							lteCell.setShapeType(map.get("SHAPETYPE").toString());
							lteCell.setAllLngLats(map.get("ALLLNGLATS").toString());
							lteCell.setPci(map.get("PCI").toString());
							//System.out.println(lteCell.toString());
							lteCells.add(lteCell);
						}
						
						return lteCells;
					}
				});
	}

	/**
	 * 获取lte小区数量
	 */
	public Integer getRnoLteCellCntByGridInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType) {
		log.info("进入方法:getRnoLteCellCntByGridInArea。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint + ", freqType="+ freqType);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
	    
	    String wheresqlString = "";
		if ("E频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='E频段' AND ";
		}
		if ("D频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='D频段' AND ";
		}
		if ("F频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='F频段' AND ";
		}
		wheresqlString += "  longitude > " + minLng + " and longitude < " + maxLng
						 + " and latitude > " + minLat + " and latitude < " + maxLat + " and ";
		wheresqlString += " area_id in ("+areaStr+")";
		final String where = wheresqlString;
		
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String w = (where == null || where.trim().isEmpty()) ? ""
						: " where " + where;
				String sql = "select count(lte_cell_id) from RNO_LTE_CELL " + w;
				log.info("获取指定区域的网格下的指定频点类型的小区数量的sql：" + sql);
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

	@Override
	public List<RnoLteCell> getLteCellByCellParamAndCityId(
			Map<String, String> cellParam, String areaStr) {
		log.info("进入方法getLteCellByCellParamAndCityId。cellParam="+cellParam+",areaStr="+areaStr);

		String sqlwhere = "";
		String val = cellParam.get("value").toString();
		String valType = cellParam.get("valueType").toString();
		if(("cell").equals(valType)) {
			sqlwhere += " rlc.business_cell_id like '%"+val+"%' and ";
		} else if(("chineseName").equals(valType)) {
			sqlwhere += " rlc.CELL_NAME like '%"+val+"%' and ";
		}
		sqlwhere += " rlc.area_id in ("+areaStr+")";
		
		final String where = sqlwhere;
	
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoLteCell>>() {
					public List<RnoLteCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						//rownum从1开始
						String sql = "select  rlc.business_cell_id as cell," +
											" rlc.CELL_NAME    as chineseName," +
											" rlc.LONGITUDE    as lng," +
											" rlc.LATITUDE     as lat," +
											" rlc.AZIMUTH   as azimuth," +
											" rlcms.MAP_TYPE   as mapType," +
											" rlcms.SHAPE_TYPE as shapeType," +
											" rlcms.SHAPE_DATA as allLngLats" +
								" from  RNO_LTE_CELL rlc," +
								"  		RNO_LTE_CELL_MAP_SHAPE rlcms " +
								" where rlc.LTE_CELL_ID = rlcms.LTE_CELL_ID" +
								"    and "+where;
						log.info("预加载lte小区的sql:" + sql);
						//System.out.println(sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> result = query.list();
						
						Map<String,Object> map = null;
						List<RnoLteCell> lteCells = new ArrayList<RnoLteCell>();
						RnoLteCell lteCell = null;

						for (int i = 0; i < result.size(); i++) {
							map = (Map<String,Object>)result.get(i);
							lteCell = new RnoLteCell();
							lteCell.setCell(map.get("CELL").toString());
							lteCell.setLng(((BigDecimal) map.get("LNG")).doubleValue());
							lteCell.setLat(((BigDecimal) map.get("LAT")).doubleValue());
							lteCell.setChineseName(map.get("CHINESENAME").toString());
							if(map.get("AZIMUTH")==null){
								lteCell.setAzimuth(null);
							} else {
								lteCell.setAzimuth(((BigDecimal)map.get("AZIMUTH")).floatValue());
							}
							lteCell.setMapType(map.get("MAPTYPE").toString());
							lteCell.setShapeType(map.get("SHAPETYPE").toString());
							lteCell.setAllLngLats(map.get("ALLLNGLATS").toString());
							
							//System.out.println(lteCell.toString());
							lteCells.add(lteCell);
						}
						return lteCells;
					}
				});
	}
	
}
