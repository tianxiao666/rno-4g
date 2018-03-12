package com.iscreate.op.dao.publicinterface;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;

public class WorkOrderAssnetResourceDaoImpl implements WorkOrderAssnetResourceDao{
	private HibernateTemplate hibernateTemplate;
	private static Log log = LogFactory
	.getLog(TaskTracingRecordDaoImpl.class);
	
	/**
	 * 保存网络资源关联表
	 * */
	public Serializable saveWorkOrderAssnetResourceDao(Workorderassnetresource workorderassnetresource){
		return hibernateTemplate.save(workorderassnetresource);
	}
	
	/**
	 * 更新网络资源关联表
	 * */
	public void updateWorkOrderAssnetResourceDao(Workorderassnetresource workorderassnetresource){
		hibernateTemplate.update(workorderassnetresource);
	}
	
	/**
	 * 获取网络资源关联表
	 * @param key 以key为索引查找服务跟踪记录
	 * @param value key对应的值
	 * @return
	 */
	public List<Workorderassnetresource> getWorkOrderAssnetResourceRecordDao(final String key,final Object value){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<Workorderassnetresource>>() {
			public List<Workorderassnetresource> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(Workorderassnetresource.class);
				criteria.add(
						Restrictions.eq(key, value));
						
				List<Workorderassnetresource> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.debug("获取网络资源关联表!");
					return list;
				}
				log.debug("获取网络资源关联表：网络资源关联表不存在!");
				return null;
			}

		});
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
