package com.iscreate.op.service.routineinspection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;

public interface RoutineinspectionQuestionService {
	
	/**
	 * 保存巡检问题
	 * @param routineinspectionQuestion
	 */
	public long saveRoutineinspectionQuestion(RoutineinspectionQuestion routineinspectionQuestion);
	
	/**
	 * 更新巡检问题
	 * @param routineinspectionQuestion
	 */
	public void updateRoutineinspectionQuestion(RoutineinspectionQuestion routineinspectionQuestion);
	
	/**
	 *  根据资源获取相关巡检问题列表
	 */
	public List<Map> getRoutineinspectionQuestionListMapByResource(String toId);
	
	/**
	 * 根据主键获取巡检问题详细信息
	 * @param id
	 * @return
	 */
	public Map getRoutineinspectionQuestionInfoById(String id);
	
	/**
	 * 根据主键获取巡检问题
	 * @param id
	 * @return
	 */
	public RoutineinspectionQuestion getRoutineinspectionQuestionById(String id);
	
	/**
	 * 根据组织以及其子组织关联查找关联的问题(分页)
	 * @param handler
	 * @return
	 */
	public Map<String,Object> getRoutineinspectionQuestionListMapByOrg(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,String orgId,String handler);
	
	/**
	 * 根据toId关联查找关联的问题
	 * @param handler
	 * @return
	 */
	public Set<Map> getRoutineinspectionQuestionListMapByToId(String toId);
	
	/**
	 * 查找关联的问题（分页）
	 * @param handler
	 * @return
	 */
	public Map<String,Object> getRoutineinspectionQuestionListMap(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,String orgId);
	
	/**
	 *  根据资源获取相关巡检问题列表,任务已关闭状态
	 */
	public Set<Map> getRoutineinspectionQuestionListMapByCloseStatus(String toId);
	
}
