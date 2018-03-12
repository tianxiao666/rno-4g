package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoTask;

public interface RnoTaskDao {

	
	/**
	 * 获取任务
	 * @param id
	 * @return
	 * @author brightming
	 * 2014-1-23 下午4:40:19
	 */
	public RnoTask getTaskById(long id);
	
	/**
	 * 获取task的参数信息
	 * @param taskId
	 * @return
	 * @author brightming
	 * 2014-1-23 下午4:00:40
	 */
	public List<Map<String,Object>> getTaskParams(long taskId);
	
	/**
	 * 获取task的通用结果
	 * @param taskId
	 * @return
	 * @author brightming
	 * 2014-1-23 下午4:01:18
	 */
	public List<Map<String,Object>> getTaskCommonResult(long taskId);
	
	/**
	 * 保存一个task
	 * @param rnoTask
	 * @return
	 * 返回任务id
	 * @author brightming
	 * 2014-1-23 下午4:02:30
	 */
	public Long saveTask(RnoTask rnoTask);
	

    /**
     * 更新任务信息
     * @param rnoTask
     * @return
     * @author brightming
     * 2014-1-23 下午4:39:47
     */
	public void updateTask(RnoTask rnoTask);
	
	/**
	 * 保存任务参数
	 * @param taskId
	 * @param paramNameAndValue
	 * key为参数名，value为参数值
	 * 两者都不能为空
	 * @return
	 * @author brightming
	 * 2014-1-23 下午4:04:38
	 */
	public int saveTaskParam(long taskId,List<Map<String,String>> paramNameAndValue);
	
	/**
	 * 保存结果
	 * @param taskId
	 * @param result
	 * @return
	 * @author brightming
	 * 2014-1-23 下午4:39:06
	 */
	public int saveTaskResult(long taskId,Map<String,String> result);
	
	/**
	 * 删除task
	 * @param taskId
	 * @author brightming
	 * 2014-2-17 上午10:04:31
	 */
	public void deleteTask(long taskId);

	
	/**
	 * 检查是否存在已完成的ncs分析任务
	 * @param ncsIdsStr
	 * @author peng.jm
	 * 2014年6月23日9:49:58
	 */
	public List<Map<String, Object>> checkNcsTaskByNcsIds(String ncsIdsStr);

	/**
	 * 保存任务对应的ncsIds列
	 * @param taskId
	 * @param ncsIds
	 * @author peng.jm
	 * @return
	 * 2014年6月23日13:42:42
	 */
	public boolean saveTaskNcsIdList(Long taskId, String ncsIds);

	/**
	 * 通过taskId删除任务跟ncsIds的关联关系
	 * @param taskId
	 * @author peng.jm
	 */
	public void deleteTaskNcsIdListByTaskId(Long taskId);
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
	public boolean saveTaskMrrIdList(final Long taskId, final String mrrIds);
}
