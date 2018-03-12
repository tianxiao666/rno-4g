package com.iscreate.op.service.workmanage;

import java.util.List;
import java.util.Map;

public interface CommonQueryService {

	
	/**
	 * 公共查询
	 * @param queryEntityName 查询实体名称
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String queryEntityName,String conditionString);
	
	
	
	
	/**
	 * 公共查询
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 查询输入条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String queryEntityName,Map<String,String> inputCondition);
	
	
	
	/**
	 * 公共查询
	 * @param orderName	排序列
	 * @param order 升序or降序
	 * @param queryEntityName 查询实体名称
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String orderName,String order,String queryEntityName,String conditionString);
	
	
	
	/**
	 * 公共查询
	 * @param orderName	排序列
	 * @param order 升序or降序
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 查询输入条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(String orderName,String order,String queryEntityName,Map<String,String> inputCondition);
	
	
	/**
	 * 公共查询
	 * @param start 记录开始下标
	 * @param limit 记录每页显示数量
	 * @param orderName 排序列
	 * @param order 顺序or降序
	 * @param queryEntityName 查询实体名称
	 * @param inputCondition 表单输入条件
	 * @param conditionString 附加条件
	 * @return
	 */
	public Map<String,Object> commonQueryService(final String start,final String limit,String orderName,String order,String queryEntityName,Map<String,String> inputCondition,String conditionString);
	
	
	
	
	/**
	 * 获取工单定义的所有状态列表
	 * @return
	 */
	public List<Map> getAllWorkorderStatusList();
	
	
	
	/**
	 * 获取任务单定义的所有状态列表
	 * @return
	 */
	public List<Map> getAllTaskorderStatusList();
	
	
}
