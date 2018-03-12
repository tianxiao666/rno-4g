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

public class RoutineinspectionQuestionDaoImpl implements RoutineinspectionQuestionDao{
	/************ 依赖注入 ***********/
	private HibernateTemplate hibernateTemplate; 
	private  static final  Log log = LogFactory.getLog(RoutineinspectionQuestionDaoImpl.class);
	
	/**
	 * 保存巡检问题
	 * @param routineinspectionQuestion
	 */
	public long saveRoutineinspectionQuestionDao(RoutineinspectionQuestion routineinspectionQuestion){
		log.info("进入 saveRoutineinspectionQuestionDao");
		log.info("saveRoutineinspectionQuestionDao DAO层 保存巡检问题。");
		Serializable id = hibernateTemplate.save(routineinspectionQuestion);
		log.info("退出 saveRoutineinspectionQuestionDao");
		return Long.parseLong(id.toString());
		
	}
	
	/**
	 * 更新巡检问题
	 * @param routineinspectionQuestion
	 */
	public void updateRoutineinspectionQuestionDao(RoutineinspectionQuestion routineinspectionQuestion){
		log.info("进入 updateRoutineinspectionQuestionDao");
		log.info("updateRoutineinspectionQuestionDao DAO层 更新巡检问题。");
		hibernateTemplate.update(routineinspectionQuestion);
		log.info("退出 updateRoutineinspectionQuestionDao");
		
	}
	
	/**
	 * 根据指定条件查询巡检问题
	 * @param key
	 * @param value
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionList(final String key,final Object value){
		log.info("进入 queryRoutineinspectionQuestionList");
		log.info("queryRoutineinspectionQuestionList DAO层 根据指定条件查询巡检问题。");
		log.info("参数 key = "+key);
		log.info("参数 value = "+value);
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<RoutineinspectionQuestion>>() {
			public List<RoutineinspectionQuestion> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(RoutineinspectionQuestion.class);
				criteria.add(Restrictions.eq(key, value));
						
				List<RoutineinspectionQuestion> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.info("退出 queryRoutineinspectionQuestionList");
					return list;
				}else{
					log.debug(key+"为"+value+"的巡检问题为空");
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据指定条件查询巡检问题by Id
	 * @param key
	 * @param value
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionListById(final long id){
		log.info("进入 queryRoutineinspectionQuestionList");
		log.info("queryRoutineinspectionQuestionList DAO层 根据指定条件查询巡检问题。");
		log.info("参数 id = "+id);
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<RoutineinspectionQuestion>>() {
			public List<RoutineinspectionQuestion> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(RoutineinspectionQuestion.class);
				criteria.add(Restrictions.eq("id", id));
						
				List<RoutineinspectionQuestion> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.info("退出 queryRoutineinspectionQuestionList");
					return list;
				}else{
					log.debug("id为"+id+"的巡检问题为空");
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据指定条件查询巡检问题(map数据结构)
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMap(final String key,final Object value){
		log.info("进入 queryRoutineinspectionQuestionListMap");
		log.info("queryRoutineinspectionQuestionListMap DAO层 根据指定条件查询巡检问题(map数据结构)。");
		log.info("参数 key = "+key);
		log.info("参数 value = "+value);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map>>(){
			public List<Map> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT rq.id as \"id\", rq.seriousLevel as \"seriousLevel\", rq.questionType as \"questionType\", rq.description as \"description\", rq.questionPicture as \"questionPicture\", rq.resourceType as \"resourceType\", rq.resourceId as \"resourceId\", rq.resourceName as \"resourceName\", rq.creator as \"creator\", rq.creatorName as \"creatorName\", rq.creatorOrgId as \"creatorOrgId\", rq.creatorOrgName as \"creatorOrgName\", rq.createTime as \"createTime\", rq.handler as \"handler\", rq.handlerName as \"handlerName\", rq.handleTime as \"handleTime\", rq.handleResult as \"handleResult\", rq.handlePicture as \"handlePicture\", rq.isOver as \"isOver\" FROM insp_question rq WHERE "+key+"="+value;
				log.debug("sql语句："+sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出 queryRoutineinspectionQuestionListMap");
				return query.list();
			}
		});
	}
	
	/**
	 * 根据资源类型和资源标识获取查询巡检问题
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionListByResource(final String resourceType,final String resourceId){
		log.info("进入 queryRoutineinspectionQuestionListByResource");
		log.info("queryRoutineinspectionQuestionListByResource DAO层 根据资源类型和资源标识获取查询巡检问题。");
		log.info("参数 资源类型 = "+resourceType);
		log.info("参数 资源标识 = "+resourceId);
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<RoutineinspectionQuestion>>() {
			public List<RoutineinspectionQuestion> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(RoutineinspectionQuestion.class);
				criteria.add(Restrictions.eq("resourceType", resourceType));
				criteria.add(Restrictions.eq("resourceId", resourceId));	
				criteria.add(Restrictions.eq("isOver", 0));
				List<RoutineinspectionQuestion> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					log.info("退出 queryRoutineinspectionQuestionListByResource");
					return list;
				}else{
					log.debug("类型为"+resourceType+"，标识为"+resourceId+"的资源关联的巡检问题为空");
				}
				return null;
			}

		});
	}
	
	/**
	 * 根据toId获取查询巡检问题
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByToId(final String toId){
		log.info("进入 queryRoutineinspectionQuestionListMapByToId");
		log.info("queryRoutineinspectionQuestionListMapByToId DAO层 根据toId获取查询巡检问题");
		log.info("参数 toId = "+toId);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map>>(){
			public List<Map> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT rq.id as \"id\", rq.seriousLevel as \"seriousLevel\", rq.questionType as \"questionType\", rq.description as \"description\", rq.questionPicture as \"questionPicture\", rq.resourceType as \"resourceType\", rq.resourceId as \"resourceId\", rq.resourceName as \"resourceName\", rq.creator as \"creator\", rq.creatorName as \"creatorName\", rq.creatorOrgId as \"creatorOrgId\", rq.creatorOrgName as \"creatorOrgName\", rq.createTime as \"createTime\", rq.handler as \"handler\", rq.handlerName as \"handlerName\", rq.handleTime as \"handleTime\", rq.handleResult as \"handleResult\", rq.handlePicture as \"handlePicture\", rq.isOver as \"isOver\" FROM insp_question rq,insp_taskorder_question rtq WHERE rtq.toId='"+toId+"' AND rtq.questionId = rq.id";
				log.debug("sql语句："+sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出 queryRoutineinspectionQuestionListMapByToId");
				return query.list();
			}
		});
	}
	
	/**
	 * 根据资源类型和资源标识获取查询巡检问题(map数据结构)
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByResource(final String resourceType,final String resourceId){
		log.info("进入 queryRoutineinspectionQuestionListMapByResource");
		log.info("queryRoutineinspectionQuestionListMapByResource DAO层 根据资源类型和资源标识获取查询巡检问题(map数据结构)。");
		log.info("参数 资源类型 = "+resourceType);
		log.info("参数 资源标识 = "+resourceId);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map>>(){
			public List<Map> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT rq.id as \"id\", rq.seriousLevel as \"seriousLevel\", rq.questionType as \"questionType\", rq.description as \"description\", rq.questionPicture as \"questionPicture\", rq.resourceType as \"resourceType\", rq.resourceId as \"resourceId\", rq.resourceName as \"resourceName\", rq.creator as \"creator\", rq.creatorName as \"creatorName\", rq.creatorOrgId as \"creatorOrgId\", rq.creatorOrgName as \"creatorOrgName\", rq.createTime as \"createTime\", rq.handler as \"handler\", rq.handlerName as \"handlerName\", rq.handleTime as \"handleTime\", rq.handleResult as \"handleResult\", rq.handlePicture as \"handlePicture\", rq.isOver as \"isOver\" FROM insp_question rq WHERE rq.resourceType='"+resourceType+"' AND rq.resourceId = "+resourceId+" AND rq.isOver = 0";
				log.debug("sql语句："+sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出 queryRoutineinspectionQuestionListMapByResource");
				return query.list();
			}
		});
	}
	
	
	/**
	 *  根据组织以及其子组织获取查询关联巡检问题(map数据结构)
	 * @param params
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByOrg(final List<String> params){
		log.info("进入 queryRoutineinspectionQuestionListMapByOrg");
		log.info("queryRoutineinspectionQuestionListMapByOrg DAO层 根据组织以及其子组织获取查询关联巡检问题(map数据结构)");
		log.info("参数 组织id集合 = "+params);
		if(params==null||params.isEmpty()){
			log.error("参数 组织Id集合为空");
			return null;
		}
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map>>(){
			public List<Map> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String orgIds = "(";
				for(int i=0;i<params.size();i++){
					if(i==0){
						orgIds += params.get(0);
					}else{
						orgIds += ","+params.get(i);
					}
					
				}
				orgIds += ")";
				String sql = "SELECT rq.id as \"id\", rq.seriousLevel as \"seriousLevel\", rq.questionType as \"questionType\", rq.description as \"description\", rq.questionPicture as \"questionPicture\", rq.resourceType as \"resourceType\", rq.resourceId as \"resourceId\", rq.resourceName as \"resourceName\", rq.creator as \"creator\", rq.creatorName as \"creatorName\", rq.creatorOrgId as \"creatorOrgId\", rq.creatorOrgName as \"creatorOrgName\", rq.createTime as \"createTime\", rq.handler as \"handler\", rq.handlerName as \"handlerName\", rq.handleTime as \"handleTime\", rq.handleResult as \"handleResult\", rq.handlePicture as \"handlePicture\", rq.isOver as \"isOver\" FROM insp_question rq WHERE rq.creatorOrgId in "+orgIds+"  AND rq.isOver = 0";
				log.debug("sql语句："+sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.info("退出 queryRoutineinspectionQuestionListMapByOrg");
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
