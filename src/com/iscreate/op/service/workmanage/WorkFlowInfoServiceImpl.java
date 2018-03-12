package com.iscreate.op.service.workmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;

public class WorkFlowInfoServiceImpl implements WorkFlowInfoService {

	
	private static final Log logger = LogFactory.getLog(WorkFlowInfoServiceImpl.class);
	
	
	private HibernateTemplate hibernateTemplate;
	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * che.yd 根据流程实例id，获取对应的当前节点信息
	 */
	public FlowTaskInfo getTaskInfoByProcessInstanceId(String processInstanceId) {
		FlowTaskInfo taskInfo = null;
		String sql = "select t2 from FlowInstanceInfo t1,FlowTaskInfo t2 where t1.current_activity_name=t2.nodeId "
			+ "and t1.instance_id=t2.instanceId and t2.completeDate is NULL and t1.instance_id=?";
		
		List<FlowTaskInfo> taskList=null;
		try {
			taskList = this.hibernateTemplate.find(sql,processInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取流程实例对应的当前节点信息时，数据库链接错误或者sql编写有误");
		}
		if (taskList != null && !taskList.isEmpty()) {
			taskInfo = taskList.get(0);
		}
		return taskInfo;
	}

	/**
	 * che.yd 判断当前任务是什么类型，个人任务or群组任务
	 * 
	 * @param taskId
	 * @return true为群组任务；false为个人任务
	 */
	public boolean judegeTaskIsGroup(String taskId) {
		boolean flag = false;
		String sql = "select t2 from FlowTaskInfo t2 where t2.taskId=?";
		List<FlowTaskInfo> taskList = null;
		FlowTaskInfo taskInfo = null;
		try {
			taskList=this.hibernateTemplate.find(sql,taskId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取流程实例对应的当前节点信息时，数据库链接错误或者sql编写有误");
		}
		if (taskList != null && !taskList.isEmpty()) {
			taskInfo = taskList.get(0);
		}
		if(taskInfo!=null){
			if (taskInfo.getCurrentUserId() == null || "".equals(taskInfo.getCurrentUserId())) {
				flag = true;
			}
		}
		return flag;
	}
	
	
	/**
	 * 判断流程是否已经结束
	 * @param piId 流程实例id
	 * @return
	 */
	public boolean judgeProcessIsEnd(String piId){
		
		boolean isEnd=false;
		String sql = "select t1 from FlowInstanceInfo t1 where t1.instance_id=?";
		List<FlowInstanceInfo> list = null;
		FlowInstanceInfo flowInstanceInfo = null;
		try {
			list = this.hibernateTemplate.find(sql,piId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取流程实例对应的当前节点信息时，数据库链接错误或者sql编写有误");
		}
		
		if (list != null && !list.isEmpty()) {
			flowInstanceInfo = list.get(0);
		}
		if(flowInstanceInfo!=null && "ended".equals(flowInstanceInfo.getInstance_status())){
			isEnd=true;
		}
		
		return isEnd;
	}
}
