package com.iscreate.plat.workflow;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import com.iscreate.plat.workflow.dataconfig.FlowInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.serviceaccess.*;

/*****
 * 代理层
 * 
 * @author iscreate
 *
 */

public class ServiceBeanProxy implements ServiceBean {
	ServiceBean  servic =(ServiceBean)FlowBeanFactory.getFlowBean(BeanType.Bean_Type_Service);
	
	/***************************************************************************
	 * 送阅
	 **************************************************************************/
	public void sendRead(String instanceId, List Users) throws WFException {
		servic.sendRead(instanceId, Users);
	}

	/** 我的应用列表 ** */
	public List<FlowInfo> getMyApplication(String userId) throws WFException  {
		return servic.getMyApplication(userId);
	}

	/** * 我的待办任务 ** */
	public List<FlowTaskInfo> getWaitFlowTask(String userId) throws WFException {
		return servic.getWaitFlowTask(userId);
	}

	/** 我的已办任务 * */
	public List<FlowTaskInfo> getDoneFlowTask(String userId) throws WFException {
		return servic.getDoneFlowTask(userId);
	}

	/** * 任务轨迹 *** */
	public List<FlowTaskInfo> taskTracking(String instanceId)
			throws WFException {
		return servic.taskTracking(instanceId);
	}

	/** * 催办 ** */
	public void supervision(String taskId, String supervisionMes,
			String supervisionUserId) throws WFException {
          servic.supervision(taskId, supervisionMes, supervisionUserId);
	}

	/**
	 * @param classPathResourceName
	 *            类路径命名的jpdl资源文件
	 * @param iscreateFlowDefinition
	 * 
	 * @return 流程定义的id
	 */
	public String deployWorkflow(String classPathResourceName, String userId)
			throws WFException {
		return servic.deployWorkflow(classPathResourceName, userId);
	}

	/**
	 * @param zis
	 * @param iscreateFlowDefinition
	 * @param userId
	 * 
	 * @return
	 * 
	 */
	public String deployWorkflow(ZipInputStream zis, String userId)
			throws WFException {
		return servic.deployWorkflow(zis, userId);
	}

	/**
	 * 取消流程的部署
	 * 
	 * @param flowId
	 *            流程定义id
	 * @param userId
	 *            用户id
	 * @return 是否取消部署成功 Aug 9, 2012 3:55:11 PM gmh
	 */
	public boolean unDeployWorkflow(String flowId, String userId)
			throws WFException {
		return servic.unDeployWorkflow(flowId, userId);
	}

	/***************************************************************************
	 * 启动一条流程
	 * 
	 * @param flowId
	 *            流程定义id
	 * @param userId
	 *            用户
	 * @param starterType
	 *            启动者类型：用户，流程，
	 * @param starterId
	 *            启动者id：用户id，流程id，任务id
	 * @param flowInstanceInfo
	 *            自定义流程实例表
	 * @param variables
	 *            启动流程携带的变量
	 * @return 成功：流程实例id 失败：null Aug 9, 2012 3:57:37 PM gmh
	 **************************************************************************/
	public String startWorkflow(String flowId, String userId,
			String starterType, String starterId, String object_id,
			Map<String, Object> variables) throws WFException {
		return servic.startWorkflow(flowId, userId, starterType, starterId, object_id, variables);
	}

	
	/**
	 * 启动一条流程
	 * 
	 * @param flowId
	 *            流程定义id
	 * @param userId
	 *            用户id
	 * @param starterType
	 *            启动者类型：用户，流程，
	 * @param starterId
	 *            启动者id：用户id，流程id，任务id
	 * @param object_id
	 *            外部对象标识
	 * @param variables
	 *            启动流程携带的变量
	 * @param endStarter
	 *            是否结束启动者。如在一个任务节点启动了一个流程，可以结束该任务。
	 * @param endOutcome
	 *            结束启动者（任务）的时候，指定的流转名称
	 * @param endVariables
	 *            结束启动流程的任务的时候，传入的变量
	 * @return 成功：流程实例id 失败：null Aug 9, 2012 3:57:37 PM gmh
	 */
	public String startWorkflow(String flowId, String userId,
			String starterType, String starterId,
			String object_id, Map<String, Object> variables,
			boolean endStarter, String endOutcome,
			Map<String, Object> endVariables) throws WFException{
		return servic.startWorkflow(flowId, userId, starterType, starterId, object_id, variables,endStarter,endOutcome,endVariables);
	}
	/***************************************************************************
	 * 停止一条运行流程
	 * 
	 * @param instanceId
	 * @param userId
	 * @param endState
	 * @return Aug 9, 2012 3:59:56 PM gmh
	 **************************************************************************/
	public boolean endWorkflowInstance(String instanceId, String userId,
			String endState) throws WFException {
		return servic.endWorkflowInstance(instanceId, userId, endState);
	}

	/**
	 * 撤销一条流程
	 * 
	 * @param flowInstanceId
	 *            流程实例id
	 * @param userId
	 *            用户id
	 * @param description
	 *            撤销原因
	 * @return Aug 9, 2012 4:05:21 PM gmh
	 */
	public boolean revokeWorkflowInstance(String flowInstanceId, String userId,
			String description) throws WFException {
		return servic.revokeWorkflowInstance(flowInstanceId, userId, description);
	}

	/**
	 * 完成一项任务
	 * 
	 * @param taskId
	 *            任务id
	 * @param outcome
	 *            流出线
	 * @param userId
	 *            用户id
	 * @param variables
	 *            提交变量
	 * @return Aug 9, 2012 4:06:39 PM gmh
	 */
	public boolean completeTask(String taskId, String outcome, String userId,
			Map<String, Object> variables) throws WFException {
		return servic.completeTask(taskId, outcome, userId, variables);
	}

	/***************************************************************************
	 * 暂停
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:52:56 AM gmh
	 **************************************************************************/
	public boolean rejectTask(String taskId, String userId) throws WFException {
		return servic.rejectTask(taskId, userId);
	}

	/**
	 * 撤销一个任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:54:37 AM gmh
	 */
	public boolean revokeTask(String taskId, String userId) throws WFException {
		return servic.revokeTask(taskId, userId);
	}

	/**
	 * 确认暂停
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:57:28 AM gmh
	 */
	public boolean confirmRejectTask(String taskId, String userId)
			throws WFException {
		return servic.confirmRejectTask(taskId, userId);
	}

	/**
	 * 否决暂停，任务继续
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 12:00:59 PM gmh
	 */
	public boolean denyRejectTask(String taskId, String userId)
			throws WFException {
		return servic.denyRejectTask(taskId, userId);
	}

	// /**
	// * 重分配任务接收者
	// * @param taskId
	// * @param assigneeType
	// * @param assignee
	// * @return
	// * Aug 9, 2012 4:10:20 PM
	// * gmh
	// */
	// public boolean reassignTaskAssignee(String taskId,String
	// assigneeType,String assignee)throws WFException;

	/**
	 * 获取任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 9, 2012 4:11:09 PM gmh
	 */
	public boolean takeTask(String taskId, String userId) throws WFException {
		return servic.takeTask(taskId, userId);
	}

	/**
	 * 返还任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 9, 2012 4:11:37 PM gmh
	 */
	public boolean returnTask(String taskId, String userId) throws WFException {
		return servic.returnTask(taskId, userId);
	}

	/**
	 * 查询某owner的某状态的专属任务
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return Aug 9, 2012 4:12:29 PM gmh
	 */
	public List queryOwnTasks(String ownerType, String ownerId, String status)
			throws WFException {
		return servic.queryOwnTasks(ownerType, ownerId, status);
	}

	/**
	 * 查询某owner的与其他owner共享的任务
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return Aug 9, 2012 4:13:26 PM gmh
	 */
	public List queryShareTasks(String ownerType, String ownerId, String status)
			throws WFException {
		return servic.queryShareTasks(ownerType, ownerId, status);
	}

	public boolean changeTaskAssignee(String taskId, String oldUserId,
			String newUserId) throws WFException {
		return servic.changeTaskAssignee(taskId, oldUserId, newUserId);
	}
	
	

}
