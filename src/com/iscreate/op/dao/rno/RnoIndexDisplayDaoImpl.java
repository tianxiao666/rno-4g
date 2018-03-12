package com.iscreate.op.dao.rno;

import java.sql.SQLException;
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
import com.iscreate.op.action.rno.model.Eri2GFasQueryCond;
import com.iscreate.op.action.rno.model.Eri2GMrrQueryCond;
import com.iscreate.op.action.rno.model.LteStsIndexQueryCond;

public class RnoIndexDisplayDaoImpl implements RnoIndexDisplayDao {
private static Log log=LogFactory.getLog(RnoIndexDisplayDaoImpl.class);
	
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 
	 * @title 查询爱立信2G小区MRR指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-18上午10:22:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellMrrIndex(final Eri2GMrrQueryCond cond){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String table= cond.buildTableCont();
						String field = cond.buildFieldCont();
						String where = cond.buildWhereCont();
						log.debug("queryEri2GCellMrrIndex ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						String sql = "select "+field+" from "+table+" where "+where;
						log.debug("queryEri2GCellMrrIndex ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询爱立信2G小区FAS指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-2-2上午10:16:06
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellFasIndex(final Eri2GFasQueryCond cond){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String table= cond.buildTableCont();
						String field = cond.buildFieldCont();
						String where = cond.buildWhereCont();
						log.debug("queryEri2GCellFasIndex ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						String sql = "select "+field+" from "+table+" where "+where;
						log.debug("queryEri2GCellFasIndex ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:53:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteStsIndex(final LteStsIndexQueryCond cond){
		log.debug("进入方法：queryLteStsIndex ,cond=" + cond);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0) throws HibernateException, SQLException {
						String table= cond.buildTableCont();
						String field = cond.buildFieldCont();
						String where = cond.buildWhereCont();
						String sql =field + table+ where;
						log.debug("queryLteStsIndex ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						//List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
						log.debug("退出方法：queryLteStsIndex ,result=" + rows);
						return rows;
					}
				});
	}
	/**
	 * 根据城市，时间，数据列，小区列，分页查询LTE小区的STS指标
	 * @param cond，page
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:48:00
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteStsIndexByPage(final LteStsIndexQueryCond cond,final Page page){
		log.debug("进入方法：queryLteStsIndexByPage ,cond=" + cond);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0) throws HibernateException, SQLException {
						String table= cond.buildTableCont();
						String field = cond.buildFieldCont();
						String where = cond.buildWhereCont();
						int start = (page.getPageSize()* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql =field+" from(" + field +",rownum rn" + table+ where + ")where  rn>="+start+" and rn<="+end;
						log.debug("queryLteStsIndexByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						//List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
						log.debug("退出方法：queryLteStsIndexByPage ,result=" + rows);
						return rows;
					}
				});
	}
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标总数
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015年9月21日 下午2:37:16
	 * @company 怡创科技
	 * @version V1.0
	 */
	public int queryLteStsIndexCnt(final LteStsIndexQueryCond cond){
		log.debug("进入方法：queryLteStsIndexCnt ,cond=" + cond);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
					public Long doInHibernate(Session arg0) throws HibernateException, SQLException {
						String table= cond.buildTableCont();
						String field = cond.buildFieldCont();
						String where = cond.buildWhereCont();
						String sql = "select count(*) from(" + field + table+ where + ")";
						log.debug("queryLteStsIndexCnt ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						Long cnt = (Long) query.uniqueResult();
						log.debug("退出方法：queryLteStsIndexCnt ,result=" + cnt);
						return cnt;
					}
				}).intValue();
	}
}
