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
import com.iscreate.op.action.rno.model.Eri2GCellChannelQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class Rno2GEriCellManageDaoImpl implements Rno2GEriCellManageDao {

private static Log log=LogFactory.getLog(Rno2GEriCellManageDaoImpl.class);
	
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
	 * @date 2014-10-15上午10:18:07
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
	 * @title 查询符合条件的爱立信2G小区的描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:26:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEriCellDescCnt(final Eri2GCellDescQueryCond cond){
		log.debug("queryEriCellDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryEriCellDescCnt ,where=" + where);
				String sql = "select count(ERI_CELL_DESC_ID) from RNO_2G_ERI_CELL_DESC "
						+ where;
				log.debug("queryEriCellDescCnt,sql=" + sql);
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
	 * @title 分页查询符合条件的爱立信2G 小区的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:24:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEriCellDescByPage(
			final Eri2GCellDescQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " ERI_CELL_DESC_ID,MEA_DATE,DATA_TYPE,CELL_NUM,ACCOUNT,CITY_ID,CREATE_TIME";
						String field_inner = " ERI_CELL_DESC_ID,DATA_TYPE,CELL_NUM,ACCOUNT,TO_CHAR(MEA_DATE,'YYYY-MM-DD HH24:MI:SS') MEA_DATE,CITY_ID,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME";
						String where = cond.buildWhereCont();
						log.debug("queryEriCellDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from RNO_2G_ERI_CELL_DESC  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE asc,DATA_TYPE asc";
						log.debug("queryEriCellDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询某市最近一个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午3:21:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfEri2GCellDateInfo(final long cityId) {
		// TODO Auto-generated method stub
		log.debug("进入queryLatelyOneMonthOfEri2GCellDateInfo(long cityId)"+cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select distinct(to_char(mea_date,'yyyy-MM-dd')) mea_date from rno_2g_eri_cell_desc t where t.mea_date >= add_months(sysdate,-1) and city_id="+cityId+" and data_type='CELLDATA'";
				log.debug("queryLatelyOneMonthOfEri2GCellDateInfo, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellCnt(final Eri2GCellQueryCond cond){
		log.debug("queryEri2GCellCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				String from= cond.buildSelectContForCnt();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryEri2GCellCnt ,where=" + where);
				String sql = "select count(t1.eri_cell_desc_id) "+from+"  "
						+ where;
				log.debug("queryEri2GCellCnt,sql=" + sql);
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
	 * @title 分页查询符合条件的爱立信2G 小区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellByPage(
			final Eri2GCellQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= cond.buildFieldOutCont();
						String field_inner = cond.buildFieldInnerAndFromCont();
						String where = cond.buildWhereCont();
						log.debug("queryEriCellDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE DESC";
						log.debug("queryEri2GCellByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区信道组的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellChannelCnt(final Eri2GCellChannelQueryCond cond){
		log.debug("queryEri2GCellChannelCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryEri2GCellChannelCnt ,where=" + where);
				String sql = "select count(eri_cell_desc_id) from rno_2g_eri_cell_ch_group "
						+ where;
				log.debug("queryEri2GCellChannelCnt,sql=" + sql);
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
	 * @title 分页查询符合条件的爱立信2G 小区信道的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellChannelByPage(
			final Eri2GCellChannelQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= cond.buildFieldOutCont();
						String field_inner = cond.buildFieldInnerCont();
						String where = cond.buildWhereCont();
						String from =cond.buildFromCont();
						log.debug("queryEri2GCellChannelByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",row_number() over(partition by null order by MEA_DATE desc) rn from "+from+"  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE DESC";
						log.debug("queryEri2GCellChannelByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区邻区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GNcellCnt(final Eri2GNcellQueryCond cond){
		log.debug("queryEri2GNcellCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryEri2GNcellCnt ,where=" + where);
				String sql = "select count(eri_cell_desc_id) from rno_2g_eri_ncell_param "
						+ where;
				log.debug("queryEri2GNcellCnt,sql=" + sql);
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
	 * @title 分页查询符合条件的爱立信2G 小区邻区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GNcellByPage(
			final Eri2GNcellQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= cond.buildFieldOutCont();
						String field_inner = cond.buildFieldInnerCont();
						String where = cond.buildWhereCont();
						String from =cond.buildFromCont();
						log.debug("queryEri2GNcellByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",row_number() over(partition by null order by MEA_DATE desc) rn from "+from+"  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by MEA_DATE DESC";
						log.debug("queryEri2GNcellByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过组装sql语句输出爱立信相关数据
	 * @param comSql
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:29:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEriData(String comSql) {
		final String sql = comSql;
		log.info("queryEriData。sql="+sql);
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
	 * @title 确认符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午9:58:56
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long confirmEri2GCellCnt(final Eri2GCellQueryCond cond){
		log.debug("confirmEri2GCellCnt.cond=" + cond);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				String from= cond.buildSelectContForCnt();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("confirmEri2GCellCnt ,where=" + where);
				String sql = "select count(eri_cell_desc_id) from rno_2g_eri_cell where mea_date = to_date('"+cond.getMeaDate()+"', 'yyyy-MM-dd') and city_id="+cond.getCityId();
				log.debug("confirmEri2GCellCnt,sql=" + sql);
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
	 * @title 查询某市最近若干个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfEri2GCellDateInfo(final long cityId,final int monthNum) {
		// TODO Auto-generated method stub
		log.debug("进入queryLatelySeveralMonthOfEri2GCellDateInfo(long cityId)"+cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select distinct(to_char(mea_date,'yyyy-MM-dd')) mea_date from rno_2g_eri_cell_desc t where t.mea_date >= add_months(sysdate,-"+monthNum+") and city_id="+cityId+" and data_type='CELLDATA'";
				log.debug("queryLatelySeveralMonthOfEri2GCellDateInfo, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
}
