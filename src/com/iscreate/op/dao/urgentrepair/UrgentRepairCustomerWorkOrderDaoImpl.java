package com.iscreate.op.dao.urgentrepair;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevice2gworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairDevicetdworkorder;

public class UrgentRepairCustomerWorkOrderDaoImpl implements UrgentRepairCustomerWorkOrderDao{
    private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public Serializable saveUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder){
		return hibernateTemplate.save(urgentrepairCustomerworkorder);
	}
	
	/**
	 * 更新客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairCustomerWorkOrder(UrgentrepairCustomerworkorder urgentrepairCustomerworkorder){
		hibernateTemplate.update(urgentrepairCustomerworkorder);
	}
	
	/**
	 * 根据id获取客户工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairCustomerworkorder getUrgentrepairCustomerWorkorderById(long id){
		return hibernateTemplate.get(UrgentrepairCustomerworkorder.class, id);
	}
	
	/**
	 * 根据工单号获取工单
	 */
	public List<UrgentrepairCustomerworkorder> getUrgentrepairCustomerWorkorderByWoId(final String woId){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairCustomerworkorder>>() {
			public List<UrgentrepairCustomerworkorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairCustomerworkorder.class);
				criteria.add(
						Restrictions.eq("woId", woId));
						
				List<UrgentrepairCustomerworkorder> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
				return null;
			}

		});
	}
	
	
	/**
	 * 保存2g客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void saveUrgentRepairDevice2gWorkOrder(UrgentrepairDevice2gworkorder urgentrepairDevice2gworkorder){
		hibernateTemplate.save(urgentrepairDevice2gworkorder);
	}
	
	/**
	 * 更新2g客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairDevice2gWorkOrder(UrgentrepairDevice2gworkorder urgentrepairDevice2gworkorder){
		hibernateTemplate.update(urgentrepairDevice2gworkorder);
	}
	
	/**
	 * 根据条件集合获取2g工单
	 */
	public List<UrgentrepairDevice2gworkorder> getUrgentrepairDevice2gWorkorderByParams(final Map<String,String> params){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairDevice2gworkorder>>() {
			public List<UrgentrepairDevice2gworkorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairDevice2gworkorder.class);
				for(Map.Entry<String, String> entry:params.entrySet()){
					String key=entry.getKey();
					String value=entry.getValue();
					criteria.add(Restrictions.eq(key, value));
				}		
				List<UrgentrepairDevice2gworkorder> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据id获取2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevice2gworkorder getUrgentrepairDevice2gWorkorderById(long id){
		return hibernateTemplate.get(UrgentrepairDevice2gworkorder.class, id);
	}
	
	
	
	/**
	 * 保存td客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void saveUrgentRepairDevicetdWorkOrder(UrgentrepairDevicetdworkorder urgentrepairDevicetdworkorder){
		hibernateTemplate.save(urgentrepairDevicetdworkorder);
	}
	
	/**
	 * 更新td客户工单
	 * @param urgentrepairCustomerworkorder
	 */
	public void updateUrgentRepairDevicetdWorkOrder(UrgentrepairDevicetdworkorder urgentrepairDevicetdworkorder){
		hibernateTemplate.update(urgentrepairDevicetdworkorder);
	}
	
	/**
	 * 根据工单号获取td工单
	 */
	public List<UrgentrepairDevicetdworkorder> getUrgentrepairDevicetdWorkorderByParams(final Map<String,String> params){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairDevicetdworkorder>>() {
			public List<UrgentrepairDevicetdworkorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairDevicetdworkorder.class);
				for(Map.Entry<String, String> entry:params.entrySet()){
					String key=entry.getKey();
					String value=entry.getValue();
					criteria.add(Restrictions.eq(key, value));
				}
						
				List<UrgentrepairDevicetdworkorder> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据id获取2g工单
	 * @param Id
	 * @return
	 */
	public UrgentrepairDevicetdworkorder getUrgentrepairDevicetdWorkorderById(long id){
		return hibernateTemplate.get(UrgentrepairDevicetdworkorder.class, id);
	}
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
