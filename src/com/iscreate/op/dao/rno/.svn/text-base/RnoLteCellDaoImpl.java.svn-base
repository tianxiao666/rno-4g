package com.iscreate.op.dao.rno;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoLteCellDaoImpl implements RnoLteCellDao {
	private static Log log = LogFactory.getLog(RnoLteCellDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 计算满足要求的小区总数量
	 * 
	 * @param cond
	 * @return
	 * @author brightming 2014-5-19 下午2:03:25
	 */
	public long getLteCellCount(LteCellQueryCondition cond) {
		String where = cond.buildCellQueryCond("CELL");
		final String sql = "select count(CELL.LTE_CELL_ID) from RNO_LTE_CELL CELL"
				+ (!"".equals(where) ? " where " + where : "");
		log.debug("getLteCellCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getLteCellCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页查询LTE小区信息
	 * 
	 * @param cond
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author brightming 2014-5-19 下午2:01:57
	 */
	public List<Map<String, Object>> queryLteCellByPage(
			LteCellQueryCondition cond, long startIndex, long cnt) {

		log.debug("进入dao方法：queryLteCellByPage。cond=" + cond + ",startIndex="
				+ startIndex + ",cnt=" + cnt);
		long cityId=cond.getCityId();
		//@author chao.xj  2015-6-11 上午10:19:56
		//设置权限数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		String sysAreaSql="select name from SYS_AREA where area_id="+cityId;
		List<Map<String, Object>> areaNames=executeSqlForObject(sysAreaSql);
		String areaName="";
		if(areaNames!=null && areaNames.size()!=0){
			areaName=areaNames.get(0).get("NAME").toString();
		}
		//设置rno数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
//		String fields = "AREA.NAME as AREA_NAME,ENODEB.ENODEB_NAME,CELL.LTE_CELL_ID,CELL.CELL_NAME,CELL.ENODEB_ID,CELL.BAND,CELL.EARFCN,CELL.PCI,CELL.RSPOWER";
		//@author chao.xj  2015-6-11 上午10:24:39
		String fields = "'"+areaName+"' as AREA_NAME,ENODEB.ENODEB_NAME,CELL.LTE_CELL_ID,CELL.CELL_NAME,CELL.ENODEB_ID,CELL.BAND,CELL.EARFCN,CELL.PCI,CELL.RSPOWER";

		String where = cond.buildCellQueryCond("MIDCELL");
		if (!"".equals(where)) {
			where = " WHERE " + where;
		}
		
		/*final String sql = "SELECT "
				+ fields
				+ " FROM ("
				+ "select * FROM ( SELECT MIDCELL.AREA_ID,MIDCELL.LTE_CELL_ID,MIDCELL.CELL_NAME,MIDCELL.ENODEB_ID,MIDCELL.BAND,MIDCELL.EARFCN,MIDCELL.PCI,MIDCELL.RSPOWER,ROW_NUMBER() OVER(ORDER BY NULL) RN from RNO_LTE_CELL MIDCELL "
				+ where + " order by midcell.lte_cell_id asc) WHERE RN>"
				+ startIndex + " and RN<=" + (startIndex + cnt) + " ) CELL"
				+ " LEFT JOIN RNO_LTE_ENODEB ENODEB "
				+ " ON(CELL.ENODEB_ID=ENODEB.ENODEB_ID) "
				+ " LEFT JOIN SYS_AREA AREA "
				+ " ON(CELL.AREA_ID=AREA.AREA_ID)";*/
		//@author chao.xj  2015-6-11 上午10:23:58
		final String sql = "SELECT "
				+ fields
				+ " FROM ("
				+ "select * FROM ( SELECT MIDCELL.AREA_ID,MIDCELL.LTE_CELL_ID,MIDCELL.CELL_NAME,MIDCELL.ENODEB_ID,MIDCELL.BAND,MIDCELL.EARFCN,MIDCELL.PCI,MIDCELL.RSPOWER,ROW_NUMBER() OVER(ORDER BY NULL) RN from RNO_LTE_CELL MIDCELL "
				+ where + " order by midcell.lte_cell_id asc) WHERE RN>"
				+ startIndex + " and RN<=" + (startIndex + cnt) + " ) CELL"
				+ " LEFT JOIN RNO_LTE_ENODEB ENODEB "
				+ " ON(CELL.ENODEB_ID=ENODEB.ENODEB_ID) ";

		log.debug("分页查询lte小区的sql=" + sql);
		Connection connection = DataSourceConn.initInstance().getConnection();
		Statement stmt=null;
		List<Map<String, Object>> cells=null;
		try {
			stmt=connection.createStatement();
			cells=RnoHelper.commonQuery(stmt, sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
			try {
				if(stmt!=null){
					stmt.close();
					connection.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		} 
		return cells;
		/*return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.debug("退出queryLteCellByPage ltecelllist:"
								+ query.list());
						return query.list();
					}
				});*/
	}

	/**
	 * 获取小区详情
	 * 
	 * @param lteCellId
	 * @return
	 * @author brightming 2014-5-19 下午1:41:05
	 */
	public Map<String, Object> getLteCellDetail(final long lteCellId) {
		log.debug("getLteCellDetail.lteCellId=" + lteCellId);
		List<Map<String, Object>> res = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						String sql = "select cell.*,ENODEB.*,SHAPE.* from (select * FROM RNO_LTE_CELL WHERE LTE_CELL_ID="
								+ lteCellId
								+ ") "
								+ " left join RNO_LTE_ENODEB ENODEB ON (CELL.ENODEB_ID=ENODEB.ENODEB_ID)"
								+ " LEFT JOIN RNO_LTE_CELL_MAP_SHAPE SHAPE ON (CELL.LTE_CELL_ID=SHAPE.LTE_CELL_ID AND SHAPE.MAP_TYPE='E' AND SHAPE.SHAPE_TYPE='T')";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

						return query.list();
					}
				});

		Map lteCell = null;
		if (res != null && !res.isEmpty()) {
			lteCell = res.get(0);
		}
		log.debug("LteCellId=[" + lteCellId + "]的小区:" + lteCell);
		return lteCell;
	}

	/**
	 * 修改lte小区信息
	 * 
	 * @param lteCellId
	 * @param lteCell
	 * @return
	 * @author brightming 2014-5-19 下午1:42:05
	 */
	public boolean modifyLteCellDetail(final long lteCellId,
			final Map<String, Object> lteCell) {
		log.debug("进入方法：modifyLteCellDetail.lteCellId=" + lteCellId);

		return hibernateTemplate
			.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					
					String lteCellSql = " update rno_lte_cell rlc set rlc.local_cellid ="+lteCell.get("LOCAL_CELLID")+", " +
											" rlc.pci ="+lteCell.get("PCI")+",rlc.longitude ="+lteCell.get("LONGITUDE")+",rlc.latitude ="+lteCell.get("LATITUDE")+", " +
											" rlc.ground_height ="+lteCell.get("GROUND_HEIGHT")+", rlc.azimuth ="+lteCell.get("AZIMUTH")+",rlc.band_type ='"+lteCell.get("BAND_TYPE")+"'," +
											" rlc.cover_type ='"+lteCell.get("COVER_TYPE")+"',rlc.m_downtilt ="+lteCell.get("M_DOWNTILT")+",rlc.rspower ="+lteCell.get("RSPOWER")+"," +
											" rlc.e_downtilt ='"+lteCell.get("E_DOWNTILT")+"',rlc.tac ='"+lteCell.get("TAC")+"',rlc.rrunum ="+lteCell.get("RRUNUM")+"," +
											" rlc.tal ="+lteCell.get("TAL")+",  rlc.rruver ='"+lteCell.get("RRUVER")+"',rlc.cell_radius ="+lteCell.get("CELL_RADIUS")+"," +
											" rlc.antenna_type ='"+lteCell.get("ANTENNA_TYPE")+"',rlc.band ='"+lteCell.get("BAND")+"',    rlc.integrated ='"+lteCell.get("INTEGRATED")+"'," +
											" rlc.earfcn ='"+lteCell.get("EARFCN")+"',  rlc.pdcch ="+lteCell.get("PDCCH")+",  rlc.pa ="+lteCell.get("PA")+", " +
											" rlc.pb ="+lteCell.get("PB")+"," +" rlc.downtilt ="+lteCell.get("DOWNTILT")+" " +
										" where rlc.lte_cell_id =" + lteCellId; 
					log.debug("lteCellSql="+lteCellSql);
					String enodebSql = "   update rno_lte_enodeb enodeb set enodeb.site_style = '"+lteCell.get("SITE_STYLE")+"'," +
											" enodeb.station_cfg = '"+lteCell.get("STATION_CFG")+"', enodeb.frame_cfg ='"+lteCell.get("FRAME_CFG")+"',enodeb.special_frame_cfg='"+lteCell.get("SPECIAL_FRAME_CFG")+"'," +
											" enodeb.is_vip = '"+lteCell.get("IS_VIP")+"', enodeb.state = '"+lteCell.get("STATE")+"', enodeb.operation_time = to_date('"+lteCell.get("OPERATION_TIME")+"','yyyy-MM-dd'), enodeb.build_type = '"+lteCell.get("BUILD_TYPE")+"'" +
											" where enodeb.enodeb_id in( select rlc.enodeb_id " +
											" from rno_lte_cell rlc where rlc.lte_cell_id = "+lteCellId+" )";
					log.debug("enodebSql="+enodebSql);
					SQLQuery query1 = arg0.createSQLQuery(lteCellSql);
					int resCnt1 = query1.executeUpdate();
					SQLQuery query2 = arg0.createSQLQuery(enodebSql);
					int resCnt2 = query2.executeUpdate();
					if (resCnt1 > 0 || resCnt2 > 0) {
						return true;
					} else {
						return false;
					} 
				}
			});
	}

	/**
	 * 查询指定小区的详情，以及与该小区同站的其他小区的详情。 第一个为目标小区，后面的为其他的小区。
	 * 
	 * @param lteCellId
	 * @return
	 * @author brightming 2014-5-19 下午3:55:54
	 */
	public List<Map<String, Object>> queryLteCellAndCositeCells(
			final long lteCellId) {
		log.debug("进入方法：queryLteCellAndCositeCells.lteCellId=" + lteCellId);
		List<Map<String, Object>> res = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						String sql = "SELECT cell.*,"
								+ " enodeb.* FROM "
								+ " (SELECT *  FROM rno_lte_cell "
								+ " WHERE enodeb_id =  (SELECT enodeb_id FROM rno_lte_cell WHERE lte_CELL_ID="
								+ lteCellId + ") "
								+ " )cell LEFT JOIN rno_lte_enodeb enodeb "
								+ " ON(cell.enodeb_id=enodeb.enodeb_id)";

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

						List<Map<String, Object>> res = query.list();
						if (res != null && res.size() > 0) {
							// 调整顺序
							List<Map<String, Object>> fin = new ArrayList<Map<String, Object>>();
							for (Map<String, Object> one : res) {
								if (lteCellId == ((BigDecimal) one
										.get("LTE_CELL_ID")).longValue()) {
									fin.add(0, one);
								} else {
									fin.add(one);
								}
							}
							res = fin;
						}
						log.debug("queryLteCellAndCositeCells.lteCellId="
								+ lteCellId + ",返回结果：" + res);
						return res;
					}
				});

		return res;
	}

	/**
	 * 删除指定的lte 小区
	 * 
	 * @param ids
	 * @return
	 * @author brightming 2014-5-21 上午11:25:46
	 */
	public boolean deleteRnoLteCellByIds(final String ids) {
		log.debug("进入方法：deleteRnoLteCellByIds,ids=" + ids);
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "UPDATE RNO_LTE_CELL SET STATUS='D' WHERE LTE_CELL_ID IN ("
						+ ids + ")";
				SQLQuery query = arg0.createSQLQuery(sql);
				int resCnt = query.executeUpdate();
				log.debug("deleteRnoLteCellByIds 删除数量：" + resCnt);
				// enodeb的信息不处理
				if (resCnt > 0) {
					return true;
				} else {
					return false;
				}
			}

		});
	}
	/**
	 * 
	 * @title 执行sql语句
	 * @param sqlString
	 * @return
	 * @author chao.xj
	 * @date 2015-6-10上午11:10:42
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List executeSqlForObject(final String sqlString) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List find = query.list();
						return find;
					}
				});
		return list;
	}
	/**
	 * 
	 * @title 计算满足要求的邻区总数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:05:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getLteNcellCount(LteCellQueryCondition cond) {
		String where = cond.buildNCellQueryCond("NCELL");
		final String sql = "select count(NCELL.LTE_NCELL_RELA_ID) from RNO_LTE_NCELL NCELL " +
				" where " + where;
		log.debug("getLteNcellCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getLteNcellCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询LTE邻区信息
	 * @param cond
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:05:11
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteNcellByPage(
			LteCellQueryCondition cond, int startIndex, int cnt) {

		log.debug("进入dao方法：queryLteNcellByPage。cond=" + cond + ",startIndex="
				+ startIndex + ",cnt=" + cnt);
		
		String fields = "NCELL.LTE_NCELL_RELA_ID as LTE_NCELL_RELA_ID, NCELL.LTE_CELL_NAME as LTE_CELL_NAME, NCELL.LTE_NCELL_NAME as LTE_NCELL_NAME," +
					"NCELL.LTE_CELL_ID as LTE_CELL_ID, NCELL.LTE_CELL_ENODEB_ID as LTE_CELL_ENODEB_ID," +
					"NCELL.LTE_NCELL_ID as LTE_NCELL_ID, NCELL.LTE_NCELL_ENODEB_ID as LTE_NCELL_ENODEB_ID," +
					"NCELL.LTE_CELL_SITE_ID as LTE_CELL_SITE_ID, NCELL.LTE_NCELL_SITE_ID as LTE_NCELL_SITE_ID ";
		
		String where = cond.buildNCellQueryCond("MIDCELL");
		where = " WHERE " + where;

		final String sql = "SELECT " 
					+ fields
					+ " FROM (select * FROM ( SELECT MIDCELL.LTE_NCELL_RELA_ID,MIDCELL.LTE_CELL_NAME," +
								"MIDCELL.LTE_NCELL_NAME,MIDCELL.LTE_CELL_ID,MIDCELL.LTE_CELL_ENODEB_ID," +
								"MIDCELL.LTE_NCELL_ID,MIDCELL.LTE_NCELL_ENODEB_ID,MIDCELL.LTE_CELL_SITE_ID," +
								"MIDCELL.LTE_NCELL_SITE_ID,ROW_NUMBER() OVER(ORDER BY NULL) RN from RNO_LTE_NCELL MIDCELL "
					+ where + " order by MIDCELL.LTE_NCELL_RELA_ID asc) WHERE RN>"
					+ startIndex + " and RN<=" + (startIndex + cnt) + " ) NCELL ";
			

		log.debug("分页查询lte邻区关系的sql=" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.debug("queryLteNcellByPage lteNcellList:"
								+ query.list());
						return query.list();
					}
				});
	}

	/**
	 * 
	 * @title 删除指定的lte邻区 id
	 * @param ids
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:07:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean deleteRnoLteNcellByIds(final String ids) {
		log.debug("进入方法：deleteRnoLteNcellByIds,ids=" + ids);
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "DELETE FROM RNO_LTE_NCELL WHERE LTE_NCELL_RELA_ID ="+ ids;
				SQLQuery query = arg0.createSQLQuery(sql);
				int resCnt = query.executeUpdate();
				log.debug("deleteRnoLteNcellByIds 删除数量：" + resCnt);
				// enodeb的信息不处理
				if (resCnt > 0) {
					return true;
				} else {
					return false;
				}
			}

		});
	}
	/**
	 * 
	 * @title 判断两个小区是否为邻区关系
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:20:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean getNCellById(
			String lteCellId1, String lteCellId2) {
		log.info("getNCellById:  lteCellId1: "+lteCellId1+",lteCellId2: "+lteCellId2);
		
		final String sql = "select *  from rno_lte_ncell rln " +
				" where (rln.lte_cell_id = '"+lteCellId1+"' and rln.lte_ncell_id = '"+lteCellId2+"') " +
					" or (rln.lte_cell_id = '"+lteCellId2+"' and rln.lte_ncell_id = '"+lteCellId1+"')";

		log.debug("判断两个小区是否为邻区关系的sql=" + sql);
		
		List<Map<String,Object>> resultList = hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		
		boolean result = false;
		if(resultList.size() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * 
	 * @title 判断两个小区是否跟同一个小区存在邻区关系
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:20:36
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean getNCellInOneCellByNcellId(
			String lteCellId1, String lteCellId2) {
		log.info("getNCellInOneCellByNcellId:  lteCellId1: "+lteCellId1+",lteCellId2: "+lteCellId2);
		
		final String sql = " select rln.lte_cell_id from rno_lte_ncell rln " +
						" where  rln.lte_ncell_id = '"+lteCellId1+"'  " +
						" and  rln.lte_cell_id in  (select lte_cell_id from rno_lte_ncell " +
									" where lte_ncell_id = '"+lteCellId2+"')";

		log.debug("判断两个小区是否跟同一个小区存在邻区关系的sql=" + sql);
		
		List<Map<String,Object>> resultList = hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		
		boolean result = false;
		if(resultList.size() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 * 
	 * @title 通过两个小区id获取这两个小区的serving小区的数据
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:21:01
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getServingCellByNcellId(
			String lteCellId1, String lteCellId2) {
		log.info("getServingCellByNcellId:  lteCellId1: "+lteCellId1+",lteCellId2: "+lteCellId2);
		
		final String sql = "select * from rno_lte_cell where business_cell_id in ( " +
							" select rln.lte_cell_id from rno_lte_ncell rln " +
							" where  rln.lte_ncell_id = '"+lteCellId1+"'  " +
							" and  rln.lte_cell_id in  (select lte_cell_id from rno_lte_ncell " +
										" where lte_ncell_id = '"+lteCellId2+"'))";

		log.debug("通过两个小区id获取这两个小区的serving小区的数据的sql=" + sql);
		
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
}
