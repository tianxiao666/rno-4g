package com.iscreate.op.dao.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class BaseDao {
	/**
	 * @author du
	 * @create 2013-08-30
	 * 通过序列得到序列的下一个值
	 * 注：由于项目管理模块，金额需要加密，不能采用pojo的方式存储，所以需要提前获取插入的主键值
	 * @param seqName
	 * @return
	 */
	public long getNextSeqlValue(String seqName ,HibernateTemplate hibernateTemplate){
		String sql = "select "+seqName+".nextval id from dual";
		Map<String,Object> map = executeSqlForMap(sql,hibernateTemplate);
		if(map != null && map.get("ID") != null){
		    return Long.parseLong(map.get("ID").toString());
		}else{
			return 0;
		}
	}
	/**
	 * 执行更新、插入、删除等sql
	 * @param sql
	 * @return true or false
	 */
	public int executeSql ( final String sql ,HibernateTemplate hibernateTemplate) {
		int execute = hibernateTemplate.execute( new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				int executeUpdate = query.executeUpdate();
				return executeUpdate;
			}
		});
		return execute;
	} 
	
	
	public List<Map<String, Object>> executeSqlForList(final String sqlString ,HibernateTemplate hibernateTemplate) {
		List<Map<String, Object>> list = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						SQLQuery query = session.createSQLQuery(sqlString);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> find = query.list();
						return find;
					}
				});
		return list;
	}
	
	public Map<String, Object> executeSqlForMap(final String sqlString ,HibernateTemplate hibernateTemplate) {
		List<Map<String, Object>> list = hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(
					Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> find = query.list();
				return find;
			}
		});
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
