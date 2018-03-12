package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GNcellQueryCond;

public class Rno2GHwCellManageDaoImpl implements Rno2GHwCellManageDao {

private static Log log=LogFactory.getLog(Rno2GHwCellManageDaoImpl.class);
	
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 
	 * @title 通过区域查询BSC
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:03:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscByCityId(long cityId) {
		// TODO Auto-generated method stub
		log.debug("进入queryBscByCityId(long cityId)" +cityId);
		final String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select bsc_id,engname from rno_bsc where bsc_id in(select bsc_id from rno_bsc_rela_area where area_id in("+areaStr+"))";
				log.debug("queryBscByCityId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 查询符合条件的华为2G小区的描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:05:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwCellDescCnt(final Hw2GCellDescQueryCond cond){
		log.debug("queryHwCellDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryHwCellDescCnt ,where=" + where);
				String sql = "select count(HW_CELL_DESC_ID) from RNO_2G_HW_CELL_DESC "
						+ where;
				log.debug("queryHwCellDescCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:06:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHwCellDescByPage(
			final Hw2GCellDescQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " HW_CELL_DESC_ID,MEA_DATE,DATA_TYPE,CELL_NUM,ACCOUNT,CITY_ID,CREATE_TIME";
						String field_inner = " HW_CELL_DESC_ID,DATA_TYPE,CELL_NUM,ACCOUNT,TO_CHAR(MEA_DATE,'YYYY-MM-DD HH24:MI:SS') MEA_DATE,CITY_ID,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME";
						String where = cond.buildWhereCont();
						log.debug("queryHwCellDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from RNO_2G_HW_CELL_DESC  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE asc,DATA_TYPE asc";
						log.debug("queryHwCellDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询某市最近一个月的华为2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:27:39
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfHw2GCellDateInfo(final long cityId) {
		// TODO Auto-generated method stub
		log.debug("进入queryLatelyOneMonthOfHw2GCellDateInfo(long cityId)"+cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select distinct(to_char(mea_date,'yyyy-MM-dd')) mea_date from rno_2g_hw_cell_desc t where t.mea_date >= add_months(sysdate,-1) and city_id="+cityId+" and data_type='CELLDATA'";
				log.debug("queryLatelyOneMonthOfHw2GCellDateInfo, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 查询符合条件的华为2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:36:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GCellCnt(final Hw2GCellQueryCond cond){
		log.debug("queryHw2GCellCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryHw2GCellCnt ,where=" + where);
				String sql = "select count(hw_cell_desc_id) from rno_2g_hw_cell t1 "
						+ where;
				log.debug("queryHw2GCellCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:38:33
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GCellByPage(
			final Hw2GCellQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= cond.buildFieldOutCont();
						String field_inner = cond.buildFieldInnerCont();
						String where = cond.buildWhereCont();
						String from =cond.buildFromCont();
						log.debug("queryHw2GCellByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",row_number() over(partition by null order by t1.MEA_DATE desc) rn from "+from+"  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE asc";
						log.debug("queryHw2GCellByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询符合条件的华为2G小区邻区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:46:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GNcellCnt(final Hw2GNcellQueryCond cond){
		log.debug("queryHw2GNcellCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryHw2GNcellCnt ,where=" + where);
				String sql = "select count(hw_cell_desc_id) from rno_2g_hw_ncell t1"
						+ where;
				log.debug("queryHw2GNcellCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区邻区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:47:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GNcellByPage(
			final Hw2GNcellQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= cond.buildFieldOutCont();
						String field_inner = cond.buildFieldInnerCont();
						String where = cond.buildWhereCont();
						String from =cond.buildFromCont();
						log.debug("queryHw2GNcellByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",row_number() over(partition by null order by MEA_DATE desc) rn from "+from+"  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE asc";
						log.debug("queryHw2GNcellByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过组装sql语句输出华为相关数据
	 * @param comSql
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:29:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwData(String comSql) {
		final String sql = comSql;
		log.info("queryHwData。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
				}	
			});
	}
	/**
	 * 
	 * @title 查询某市最近若干个月的华为2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfHw2GCellDateInfo(final long cityId,final int monthNum) {
		// TODO Auto-generated method stub
		log.debug("进入queryLatelySeveralMonthOfHw2GCellDateInfo(long cityId)"+cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select distinct(to_char(mea_date,'yyyy-MM-dd')) mea_date from rno_2g_hw_cell_desc t where t.mea_date >= add_months(sysdate,-"+monthNum+") and city_id="+cityId+" and data_type='CELLDATA'";
				log.debug("queryLatelySeveralMonthOfHw2GCellDateInfo, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
}
