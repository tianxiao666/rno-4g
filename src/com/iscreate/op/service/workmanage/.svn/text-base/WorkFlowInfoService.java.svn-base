package com.iscreate.op.service.workmanage;

import java.util.List;

import com.iscreate.plat.workflow.datainput.FlowTaskInfo;

public interface WorkFlowInfoService {

	
	
	/**
	 * che.yd 根据流程实例id，获取对应的当前节点信息
	 */
	public FlowTaskInfo getTaskInfoByProcessInstanceId(String processInstanceId);

	/**
	 * che.yd 判断当前任务是什么类型，个人任务or群组任务
	 * 
	 * @param taskId
	 * @return true为群组任务；false为个人任务
	 */
	public boolean judegeTaskIsGroup(String taskId);
	
	
	/**
	 * 判断流程是否已经结束
	 * @param piId 流程实例id
	 * @return
	 */
	public boolean judgeProcessIsEnd(String piId);
}
