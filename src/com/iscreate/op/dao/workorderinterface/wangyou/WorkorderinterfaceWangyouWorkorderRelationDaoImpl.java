package com.iscreate.op.dao.workorderinterface.wangyou;

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
import com.iscreate.op.pojo.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelation;

public class WorkorderinterfaceWangyouWorkorderRelationDaoImpl implements
		WorkorderinterfaceWangyouWorkorderRelationDao {

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 保存IOSM工单与客户工单关系
	 * @param relation
	 */
	public void saveWorkorderinterfaceWangyouWorkorderRelation(WorkorderinterfaceWangyouWorkorderRelation relation){
		this.hibernateTemplate.save(relation);
	}
	


	/**
	 * 按条件查询客户工单与IOSM工单关系对象
	 */
	public List<WorkorderinterfaceWangyouWorkorderRelation> getWorkorderinterfaceWangyouWorkorderRelationList(
			final Map<String, String> params) {
		List<WorkorderinterfaceWangyouWorkorderRelation> list=null;
		list=this.hibernateTemplate
				.execute(new HibernateCallback<List<WorkorderinterfaceWangyouWorkorderRelation>>() {
					
					public List<WorkorderinterfaceWangyouWorkorderRelation> doInHibernate(
							Session session) throws HibernateException,SQLException {
						Criteria criteria = session
								.createCriteria(WorkorderinterfaceWangyouWorkorderRelation.class);
						for(Map.Entry<String, String> entry:params.entrySet()){
							String key=entry.getKey();
							String value=entry.getValue();
							criteria.add(Restrictions.eq(key, value));
						}
						List<WorkorderinterfaceWangyouWorkorderRelation> list=criteria.list();
						return list;
						
					}
				});
		
		return list;
	}

}
