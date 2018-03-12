package com.iscreate.op.dao.routineinspection;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorderQuestion;

public class RoutineinspectionTaskorderQuestionDaoImpl implements RoutineinspectionTaskorderQuestionDao{
	/************ 依赖注入 ***********/
	private HibernateTemplate hibernateTemplate; 
	private  static final  Log log = LogFactory.getLog(RoutineinspectionTaskorderQuestionDaoImpl.class);
	
	/**
	 * 保存巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public long saveRoutineinspectionTaskorderQuestionDao(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion){
		log.info("进入 saveRoutineinspectionTaskorderQuestionDao");
		log.info("saveRoutineinspectionTaskorderQuestionDao DAO层 保存巡检问题与任务的关联关系。");
		Serializable id = hibernateTemplate.save(routineinspectionTaskorderQuestion);
		log.info("退出 saveRoutineinspectionTaskorderQuestionDao");
		return Long.parseLong(id.toString());
		
	}
	
	/**
	 * 更新巡检问题与任务的关联关系
	 * @param routineinspectionTaskorderQuestion
	 */
	public void updateRoutineinspectionTaskorderQuestionDao(RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion){
		log.info("进入 updateRoutineinspectionTaskorderQuestionDao");
		log.info("updateRoutineinspectionTaskorderQuestionDao DAO层 更新巡检问题与任务的关联关系。");
		hibernateTemplate.update(routineinspectionTaskorderQuestion);
		log.info("退出 updateRoutineinspectionTaskorderQuestionDao");
		
	}
	
	/**
	 * 根据问题Id获取问题
	 * @param id
	 * @return
	 */
	public  List<Map> getRoutineinspectionTaskorderQuestionDao(final String id){
		log.info("进入 getRoutineinspectionTaskorderQuestionDao");
		log.info("getRoutineinspectionTaskorderQuestionDao DAO层 根据指定条件查询巡检问题(map数据结构)。");
		log.info("参数 id = "+id);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map>>(){
			public List<Map> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT rtq.toId as \"toId\",rtq.questionId as \"questionId\" FROM insp_taskorder_question rtq WHERE rtq.questionId="+id;
				log.debug("sql语句："+sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出 getRoutineinspectionTaskorderQuestionDao");
				return query.list();
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
