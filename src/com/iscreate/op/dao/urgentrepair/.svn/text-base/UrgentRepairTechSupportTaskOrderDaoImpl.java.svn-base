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

import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;

public class UrgentRepairTechSupportTaskOrderDaoImpl implements UrgentRepairTechSupportTaskOrderDao{
private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存专家任务单
	 * @param urgentrepairTechsupporttaskorder
	 */
	public Serializable saveUrgentRepairTechSupportTaskOrder(UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder){
		return hibernateTemplate.save(urgentrepairTechsupporttaskorder);
	}
	
	/**
	 * 更新专家任务单
	 * @param urgentrepairTechsupporttaskorder
	 */
	public void updateUrgentRepairTechSupportTaskOrder(UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder){
		hibernateTemplate.update(urgentrepairTechsupporttaskorder);
	}
	
	/**
	 * 根据id获取专家任务单
	 * @param toId
	 * @return
	 */
	public UrgentrepairTechsupporttaskorder getUrgentRepairTechSupportTaskOrderById(long id){
		return hibernateTemplate.get(UrgentrepairTechsupporttaskorder.class, id);
	}
	
	/**
	 * 根据任务单号获取任务单
	 */
	public List<UrgentrepairTechsupporttaskorder> getUrgentRepairTechSupportTaskOrderByToId(final String toId){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairTechsupporttaskorder>>() {
			public List<UrgentrepairTechsupporttaskorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairTechsupporttaskorder.class);
				criteria.add(
						Restrictions.eq("toId", toId));
						
				List<UrgentrepairTechsupporttaskorder> list = criteria.list();
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
