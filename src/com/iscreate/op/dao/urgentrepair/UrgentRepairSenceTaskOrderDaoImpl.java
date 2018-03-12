package com.iscreate.op.dao.urgentrepair;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;

public class UrgentRepairSenceTaskOrderDaoImpl implements UrgentRepairSenceTaskOrderDao{
private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存现场任务单
	 * @param urgentrepairSencetaskorder
	 */
	public Serializable saveUrgentRepairSenceTaskOrder(UrgentrepairSencetaskorder urgentrepairSencetaskorder){
		return hibernateTemplate.save(urgentrepairSencetaskorder);
	}
	
	/**
	 * 更新现场任务单
	 * @param urgentrepairSencetaskorder
	 */
	public void updateUrgentRepairSenceTaskOrder(UrgentrepairSencetaskorder urgentrepairSencetaskorder){
		hibernateTemplate.update(urgentrepairSencetaskorder);
	}
	
	/**
	 * 根据id获取现场任务单
	 * @param id
	 * @return
	 */
	public UrgentrepairSencetaskorder getUrgentRepairSenceTaskOrderById(long id){
		return hibernateTemplate.get(UrgentrepairSencetaskorder.class, id);
	}
	
	/**
	 * 根据任务单号获取任务单
	 */
	public List<UrgentrepairSencetaskorder> getUrgentRepairSenceTaskOrderByToId(final String toId){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairSencetaskorder>>() {
			public List<UrgentrepairSencetaskorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairSencetaskorder.class);
				criteria.add(
						Restrictions.eq("toId", toId));
						
				List<UrgentrepairSencetaskorder> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
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
