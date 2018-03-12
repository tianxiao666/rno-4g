package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.rno.RnoTask;

public class RnoTaskDaoImpl implements RnoTaskDao {

	private static Log log = LogFactory.getLog(RnoTaskDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 根据id获取任务
	 * 
	 * @param id
	 * @return
	 * @author brightming 2014-1-23 下午4:00:10
	 */
	public RnoTask getTaskById(final long id) {
		return hibernateTemplate.get(RnoTask.class, id);
	}

	/**
	 * 获取task的参数信息
	 * 
	 * @param taskId
	 * @return
	 * @author brightming 2014-1-23 下午4:00:40
	 */
	public List<Map<String, Object>> getTaskParams(final long taskId) {
		final String sql = "select * from RNO_TASK_PARAM WHERE task_id=?";
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, taskId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出getTaskParams params:" + query.list());
						return query.list();
					}
				});
	}

	/**
	 * 获取task的通用结果
	 * 
	 * @param taskId
	 * @return
	 * @author brightming 2014-1-23 下午4:01:18
	 */
	public List<Map<String, Object>> getTaskCommonResult(final long taskId) {
		final String sql = "select * from RNO_TASK_RESULT WHERE task_id=?";
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, taskId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出getTaskCommonResult params:" + query.list());
						return query.list();
					}
				});
	}

	/**
	 * 保存一个task
	 * 
	 * @param rnoTask
	 * @return 返回任务id
	 * @author brightming 2014-1-23 下午4:02:30
	 */
	public Long saveTask(RnoTask rnoTask) {
		if (rnoTask != null) {
			Long res = (Long) hibernateTemplate.save(rnoTask);
			return res;
		}
		return null;
	}

	/**
	 * 更新任务信息
	 * 
	 * @param rnoTask
	 * @return
	 * @author brightming 2014-1-23 下午4:39:47
	 */
	public void updateTask(RnoTask rnoTask) {
		hibernateTemplate.update(rnoTask);
	}

	/**
	 * 保存任务参数
	 * 
	 * @param taskId
	 * @param paramNameAndValue
	 *            key为参数名，value为参数值 两者都不能为空
	 * @return
	 * @author brightming 2014-1-23 下午4:04:38
	 */
	public int saveTaskParam(final long taskId,
			final List<Map<String, String>> paramNameAndValue) {
		if (paramNameAndValue == null || paramNameAndValue.isEmpty()) {
			return 0;
		}

		return hibernateTemplate.execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "insert into RNO_TASK_PARAM (TASK_ID,PARAM_NAME,PARAM_VALUE) values("
						+ taskId + ",?,?)";

				int cnt = 0;
				int res = 0;
				String v = "";

				Transaction tx = arg0.beginTransaction();

				try {
					tx.begin();
					SQLQuery query = arg0.createSQLQuery(sql);
					for (Map<String, String> one : paramNameAndValue) {
						for (String k : one.keySet()) {
							if (k == null || "".equals(k.trim())) {
								continue;
							}
							v = one.get(k);
							if (v == null) {
								continue;
							}
							query.setString(0, k);
							query.setString(1, v);
							query.executeUpdate();
							res++;

						}
					}
					
					tx.commit();
				} catch (Exception e) {
					e.printStackTrace();
					if (tx != null) {
						tx.rollback();
					}
					res = 0;
				} finally {
					arg0.clear();
				}

				return res;
			}

		});
	}

	/**
	 * 保存结果
	 * 
	 * @param taskId
	 * @param result
	 * @return
	 * @author brightming 2014-1-23 下午4:39:06
	 */
	public int saveTaskResult(final long taskId,
			final Map<String, String> result) {
		if (result == null || result.isEmpty()) {
			return 0;
		}

		return hibernateTemplate.execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String sql = "insert into RNO_TASK_RESULT (TASK_ID,RESULT_NAME,RESULT_VALUE) values("
						+ taskId + ",?,?)";

				int cnt = 0;
				int res = 0;
				String v = "";

				Transaction tx = arg0.beginTransaction();

				try {
					tx.begin();
					SQLQuery query = arg0.createSQLQuery(sql);
					for (String k : result.keySet()) {
						if (k == null || "".equals(k.trim())) {
							continue;
						}
						v = result.get(k);
						if (v == null) {
							continue;
						}
						query.setString(0, k);
						query.setString(1, v);
						cnt++;

					}
					if (cnt > 0) {
						res = query.executeUpdate();
					}
					tx.commit();
				} catch (Exception e) {
					e.printStackTrace();
					if (tx != null) {
						tx.rollback();
					}
					res = 0;
				} finally {
					arg0.clear();
				}

				return res;
			}

		});
	}

	/**
	 * 删除task
	 * 
	 * @param taskId
	 * @author brightming 2014-2-17 上午10:04:31
	 */
	public void deleteTask(final long taskId) {
		hibernateTemplate.execute(new HibernateCallback<Void>() {
			@Override
			public Void doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				// 删除任务
				String sql = "delete from RNO_TASK where TASK_ID=" + taskId;
				SQLQuery query = arg0.createSQLQuery(sql);
				query.executeUpdate();
				// 删除任务参数
				sql = "delete from RNO_TASK_PARAM where task_id=" + taskId;
				query = arg0.createSQLQuery(sql);
				query.executeUpdate();

				// 删除任务结果
				sql = "delete from RNO_TASK_RESULT where task_id=" + taskId;
				query = arg0.createSQLQuery(sql);
				query.executeUpdate();
				return null;
			}

		});
	}

	/**
	 * 检查是否存在已保存的ncs分析任务
	 * @param ncsIdsStr
	 * @author peng.jm
	 * 2014年6月23日9:49:58
	 */
	public List<Map<String, Object>> checkNcsTaskByNcsIds(String ncsIdsStr) {
		log.info("checkNcsTaskByNcsIds:  ncsIdsStr: "+ncsIdsStr);
		final String sql = "select * from RNO_TASK_NCSIDLIST where NCSIDS ='" + ncsIdsStr + "'";

		log.info("检查是否存在已完成的ncs分析任务的sql=" + sql);
		
		return hibernateTemplate
			.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

				public List<Map<String, Object>> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
	
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
					return query.list();
				}
		});
	}

	/**
	 * 保存任务对应的ncsIds列
	 * @param taskId
	 * @param ncsIds
	 * @author peng.jm
	 * @return 成功为true，反之false
	 * 2014年6月23日13:42:42
	 */
	public boolean saveTaskNcsIdList(final Long taskId, final String ncsIds) {
		log.debug("进入方法：saveTaskNcsIdList,taskId=" + taskId + ",ncsIds=" + ncsIds );
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "MERGE INTO RNO_TASK_NCSIDLIST tar" +
							" USING (SELECT " + taskId + " task_id,'" + ncsIds + "' ncsids from dual) t " +
								" ON (tar.task_id = t.task_id) " +
							" WHEN NOT MATCHED THEN INSERT VALUES (t.task_id,t.ncsids)";
				SQLQuery query = arg0.createSQLQuery(sql);
				int resCnt = query.executeUpdate();
				log.debug("saveTaskNcsIdList 受影响行数：" + resCnt);
				// enodeb的信息不处理
				if (resCnt > 0) {
					return true;
				} else {
					return false;
				}
			}

		});
	}

	/**
	 * 通过taskId删除任务跟ncsIds的关联关系  /chao.xj改造：加入mrr
	 * @param taskId
	 * @author peng.jm
	 */
	public void deleteTaskNcsIdListByTaskId(final Long taskId) {
		log.debug("进入方法：deleteTaskNcsIdListByTaskId,taskId=" + taskId);
		hibernateTemplate.execute(new HibernateCallback<Void>() {
			public Void doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "delete from  RNO_TASK_NCSIDLIST where task_id =" + taskId;
				SQLQuery query = arg0.createSQLQuery(sql);
				int resCnt = query.executeUpdate();
				log.debug("deleteTaskNcsIdListByTaskId 删除ncsidlist数量：" + resCnt);
				//@author chao.xj  2014-7-18 上午10:52:32 删除mrrid列表
				String mrrsql = "delete from  RNO_TASK_MRRIDLIST where task_id =" + taskId;
				SQLQuery mrrquery = arg0.createSQLQuery(mrrsql);
				int mrrresCnt = mrrquery.executeUpdate();
				log.debug("deleteTaskNcsIdListByTaskId 删除mrridlist数量：" + mrrresCnt);
				return null;
			}

		});
	}
	/**
	 * 
	 * @title 保存任务对应的mrrIds列
	 * @param taskId
	 * @param mrrIds
	 * @return
	 * @author chao.xj
	 * @date 2014-7-18上午10:27:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean saveTaskMrrIdList(final Long taskId, final String mrrIds) {
		log.debug("进入方法：saveTaskMrrIdList,taskId=" + taskId + ",mrrIds=" + mrrIds );
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "MERGE INTO RNO_TASK_MRRIDLIST tar" +
							" USING (SELECT " + taskId + " task_id,'" + mrrIds + "' mrrids from dual) t " +
								" ON (tar.task_id = t.task_id) " +
							" WHEN NOT MATCHED THEN INSERT VALUES (t.task_id,t.mrrids)";
				SQLQuery query = arg0.createSQLQuery(sql);
				int resCnt = query.executeUpdate();
				log.debug("saveTaskMrrIdList 受影响行数：" + resCnt);
				// enodeb的信息不处理
				if (resCnt > 0) {
					return true;
				} else {
					return false;
				}
			}

		});
	}
	
}
