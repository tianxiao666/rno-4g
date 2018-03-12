package com.iscreate.plat.workflow.datainput;

import org.hibernate.Session;

import com.iscreate.plat.workflow.*;

/***
 * 
 *  工作流流转时，数据入库的接口信息
 *  
 *  事务控制交给上一个层面上。
 * 
 * @author iscreate
 *
 */

public interface DataInputServiceBean extends FlowBean {
	public FlowInstanceInfo instanceDataInput(String flowId,
			String InstanceInfoType, String ObjectId,String userId)
			throws WFException ;

	// 任务信息入库
	public FlowTaskInfo doTask(String flowId,
			String nodeType, String ObjectId,String userId)
			throws WFException ;

	public FlowTaskInfo setTaskForUser(FlowTaskInfo task)
			throws WFException ;

	// 处理意见
	public NotionInfo notionDataInput(NotionInfo notionInfo)
			throws WFException ;

	// 送阅读
	public void sendRead(String instanceId, String[] userIds)
			throws WFException ;

	// 实例状态改变
	public FlowInstanceInfo instanceStatusUpdate(FlowInstanceInfo instance
		) throws WFException ;
}
