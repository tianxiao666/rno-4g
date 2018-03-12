package com.iscreate.op.dao.publicinterface;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.publicinterface.Tasktracerecord;

public class TaskTracingRecordDaoImpl implements TaskTracingRecordDao {
	private HibernateTemplate hibernateTemplate;
	private static Log log = LogFactory
	.getLog(TaskTracingRecordDaoImpl.class);
	/**
	 * 保存服务跟踪记录
	 * */
	public void saveTaskTracingRecordDao(Tasktracerecord tasktracerecord)
	{		
		hibernateTemplate.save(tasktracerecord);
	}
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key 以key为索引查找服务跟踪记录
	 * @param value key对应的值
	 * @return
	 */
	public List<Tasktracerecord> getTasktracerecordListDao(final String key,final Object value){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<Tasktracerecord>>() {
			public List<Tasktracerecord> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(Tasktracerecord.class);
				criteria.add(
						Restrictions.eq(key, value));
				criteria.addOrder(Order.desc("handleTime"));
				List<Tasktracerecord> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.debug("获取服务跟踪记录列表成功!");
					return list;
				}
				log.debug("获取服务跟踪记录列表失败：服务跟踪记录不存在!");
				return null;
			}

		});
	}
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key 以key为索引查找服务跟踪记录
	 * @param value key对应的值
	 * @return
	 */
	public List<Tasktracerecord> getTasktracerecordListDao(final String key,final Object value,final String handleWay){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<Tasktracerecord>>() {
			public List<Tasktracerecord> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(Tasktracerecord.class);				
				criteria.add(
						Restrictions.eq(key, value));
				criteria.add(
						Restrictions.like("handleWay", handleWay));		
				List<Tasktracerecord> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.debug("获取服务跟踪记录列表成功!");
					return list;
				}
				log.debug("获取服务跟踪记录列表失败：服务跟踪记录不存在!");
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
	

