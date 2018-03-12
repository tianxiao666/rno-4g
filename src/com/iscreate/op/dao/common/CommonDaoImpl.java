package com.iscreate.op.dao.common;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class CommonDaoImpl implements CommonDao{
	 private HibernateTemplate hibernateTemplate;
	 
	 /**
	  * 保存对象
	  * @param object
	  * @return
	  */
	 public Serializable saveObject(Object object){
		 return hibernateTemplate.save(object);
	 }
	 
	 /**
	  * 更新对象
	  * @param object
	  */
	 public void updateObject(Object object){
		 hibernateTemplate.update(object);
	 }
	 
	 /**
	  * 根据类，字段名和字段值获取对象列表
	  * @param <T>
	  * @param classz 类
	  * @param properName 表字段名
	  * @param value 字段值
	  * @return
	  */
	 public <T> List<T> geObjectListByPropertyAndValue(final Class<T> classz,final String properName,final Object value){
		 return hibernateTemplate
			.execute(new HibernateCallback<List<T>>() {
				public List<T> doInHibernate(Session session)
						throws HibernateException, SQLException {
					Criteria criteria = session.createCriteria(classz);
					criteria.add(Restrictions.eq(properName, value));
					List list=criteria.list();
					if(list==null || list.isEmpty()){
						return null;
					}else{
						return list;
					}
				}
			});
	 }
	 
	 
	 /**
	  * 根据类，字段名和字段值获取对象列表
	  * @param <T>
	  * @param classz 类
	  * @param properName 表字段名
	  * @param value 字段值
	  * @return
	  */
	 public <T> T geUniqueObjectByPropertyAndValue(final Class<T> classz,final String properName,final Object value){
		 return hibernateTemplate
			.execute(new HibernateCallback<T>() {
				public T doInHibernate(Session session)
						throws HibernateException, SQLException {
					Criteria criteria = session.createCriteria(classz);
					criteria.add(Restrictions.eq(properName, value));
					List list=criteria.list();
					if(list==null || list.isEmpty()){
						return null;
					}else{
						return (T)list.get(0);
					}
				}
			});
	 }
	 
	 /**
	  * 根据对象唯一标识获取对象
	  * @param <T>
	  * @param classz
	  * @param id
	  * @return
	  */
	 public <T> T getObjectById(Class<T> classz ,long id){
		 return hibernateTemplate.get(classz, id);
	 }
	 
	 /**
	  * 删除对象
	  * @param entity
	  */
	 public void deleteObject(String entityName,long id){
		 hibernateTemplate.delete(entityName,id);
	 }
	 
	 /**
	  * 删除对象
	  * @param entity
	  */
	 public void deleteObject(Object entity){
		 hibernateTemplate.delete(entity);
	 }

	 /**
	  * @author du.hw
	  * 查询
	 */
		public List queryList(final String sql) {
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
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	 
	 
}
