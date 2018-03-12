package com.iscreate.op.dao.urgentrepair;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.service.workmanage.WorkManageService;

public class UrgentRepairWorkOrderDaoImpl implements UrgentRepairWorkOrderDao{
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存工单
	 * @param urgentrepairWorkorder
	 */
	public Serializable saveUrgentRepairWorkOrder(UrgentrepairWorkorder urgentrepairWorkorder){
		return hibernateTemplate.save(urgentrepairWorkorder);
	}
	
	/**
	 * 更新工单
	 * @param urgentrepairWorkorder
	 */
	public void updateUrgentRepairWorkOrder(UrgentrepairWorkorder urgentrepairWorkorder){
		hibernateTemplate.update(urgentrepairWorkorder);
	}
	
	/**
	 * 根据工单号获取工单
	 * @param woId
	 * @return
	 */
	public List<UrgentrepairWorkorder> getUrgentrepairWorkorderByWoId(final String woId){
  		return this.hibernateTemplate
		.execute(new HibernateCallback<List<UrgentrepairWorkorder>>() {
			public List<UrgentrepairWorkorder> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(UrgentrepairWorkorder.class);
				criteria.add(
						Restrictions.eq("woId", woId));
						
				List<UrgentrepairWorkorder> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据id获取工单
	 * @param woId
	 * @return
	 */
	public UrgentrepairWorkorder getUrgentrepairWorkorderById(long id){
  		return this.hibernateTemplate.get(UrgentrepairWorkorder.class, id);
		
	}
	
	/**
	 * 根据工单责任人账号和工单受理专业获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getWorkOrderListByAccountAndAcceptProfessionalDao(final String account,final String acceptProfessional,final String startTime,final String endTime){
			return hibernateTemplate.executeFind(new HibernateCallback() {  
	            public Object doInHibernate(Session session) throws HibernateException,  
	                    SQLException {  
	            	String sql = "select  * from  wm_workorder as wwo ,  repair_workorder as uwo where wwo.woId = uwo.woId and wwo.currentHandler = "+account+" and uwo.acceptProfessional = "+acceptProfessional+" and createTime between "+startTime+" and "+endTime;
	                Query query = session.createSQLQuery(sql);    
	                List<Map> list = query.list();  
	                return list;  
	            }  
	        });  
	}
	
	/**
	 * 根据组织id获取工单列表
	 * @param account
	 * @param acceptProfessional
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getWorkOrderListByOrgIdsDao(final List<String> orgIds ,final String startTime,final String endTime){
			return hibernateTemplate.executeFind(new HibernateCallback() {  
	            public Object doInHibernate(Session session) throws HibernateException,  
	                    SQLException {  
	            	String orgId="(";
	    			for (int i=0;i<orgIds.size();i++) {
	    				if(i==0){
	    					orgId += "'"+orgIds.get(i)+"'";
	    				}else{
	    					orgId += ",'"+orgIds.get(i)+"'";
	    				}
	    			}
	    			String sql = "select  uwo.acceptProfessional as \"acceptProfessional\",uwo.alarmClearTime as \"alarmClearTime\",uwo.faultOccuredTime as \"faultOccuredTime\" from  wm_workorder  wwo , repair_workorder  uwo where wwo.woId = uwo.woId and wwo.creatorOrgId in "+orgId+") and wwo.createTime >= to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') and wwo.createTime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
	            	SQLQuery query = session.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List list = query.list();
	                return list;  
	            }  
	        });  
	}
	
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//		"spring/*.xml");
//		UrgentRepairWorkOrderDao urgentRepairWorkOrderDao = (UrgentRepairWorkOrderDao) context.getBean("urgentRepairWorkOrderDao");
//		System.out.println("urgentRepairWorkOrderDao==="+urgentRepairWorkOrderDao);
//		UrgentrepairWorkorder urgentrepairWorkorder = new UrgentrepairWorkorder();
//		urgentrepairWorkorder.setAcceptProfessional("111111111111");
//		urgentRepairWorkOrderDao.saveUrgentRepairWorkOrder(urgentrepairWorkorder);
		
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
