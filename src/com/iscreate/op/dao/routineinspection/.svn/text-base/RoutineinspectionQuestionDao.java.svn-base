package com.iscreate.op.dao.routineinspection;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;

public interface RoutineinspectionQuestionDao {

	/**
	 * 保存巡检问题
	 * @param routineinspectionQuestion
	 */
	public long saveRoutineinspectionQuestionDao(RoutineinspectionQuestion routineinspectionQuestion);
	
	/**
	 * 更新巡检问题
	 * @param routineinspectionQuestion
	 */
	public void updateRoutineinspectionQuestionDao(RoutineinspectionQuestion routineinspectionQuestion);
	
	/**
	 * 根据指定条件查询巡检问题
	 * @param key
	 * @param value
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionList(final String key,final Object value);
	
	/**
	 * 根据指定条件查询巡检问题(map数据结构)
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMap(final String key,final Object value);
	
	/**
	 * 根据指定条件查询巡检问题by Id
	 * @param key
	 * @param value
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionListById(final long id);
	
	/**
	 * 根据资源类型和资源标识获取查询巡检问题
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<RoutineinspectionQuestion> queryRoutineinspectionQuestionListByResource(final String resourceType,final String resourceId);
	
	/**
	 * 根据资源类型和资源标识获取查询巡检问题(map数据结构)
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByResource(final String resourceType,final String resourceId);
	
	
	/**
	 * 根据toId获取查询巡检问题
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByToId(final String toId);
	
	/**
	 *  根据组织以及其子组织获取查询关联巡检问题(map数据结构)
	 * @param params
	 * @return
	 */
	public List<Map> queryRoutineinspectionQuestionListMapByOrg(final List<String> params);
}
