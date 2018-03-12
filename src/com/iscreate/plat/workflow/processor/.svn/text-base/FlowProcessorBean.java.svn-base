package com.iscreate.plat.workflow.processor;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import com.iscreate.plat.workflow.FlowBean;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.dataconfig.FlowInfo;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;

public interface FlowProcessorBean extends FlowBean {
//	/* 1、应用列表: */
//	public List getApplicationList() throws WFException;
//
//	/*2、发起流程 */
//	public String startFlow(String flowId,String userId) throws WFException;
//
//	/*3、处理任务 */
//	public void doTask(String taskId,String userId) throws WFException;
//
//	/* 4、填写处理意见*/
//	public void writeMes(String taskId, String mes) throws WFException;
//
//	/*5、提取任务列表*/
//	public List getTasks(String userId,String taskStatus) throws WFException;
//
//	/*6、督办*/
//	public void supervision(String taskId,String supervisionMes,String supervisionUserId) throws WFException;
//
//	/*7、流程间的约束检测*/
//	public void checkFlowsForRel(String flowId1, String flowId2)
//			throws WFException;
//
//	/* 9、送阅*/
//	public void sendRead(String instanceId, List Users) throws WFException;
//
//	/*10、指派任务*/
//	public void setTaskForUser(String flowId, String nodeId, String userId)
//			throws WFException;

	//public List getApplicationList() throws WFException;
	
	/**
	 * @param classPathResourceName
	 *        类路径命名的jpdl资源文件
	 * @param iscreateFlowDefinition
	 * 
	 * @return 流程定义的id
	 */
	public String deployWorkflow(String classPathResourceName,FlowInfo iscreateFlowDefinition,String userId) throws WFException;
	
	/**
	 * @param zis
	 * @param iscreateFlowDefinition
	 * @param userId
	 * 
	 * @return
	 * 
	 */
	public String deployWorkflow(ZipInputStream zis,FlowInfo iscreateFlowDefinition,String userId)throws WFException;
	
	/**
	 * 取消流程的部署
	 * @param flowId
	 *    流程定义id
	 * @param userId
	 *    用户id
	 * @return
	 *    是否取消部署成功
	 * Aug 9, 2012 3:55:11 PM
	 * gmh
	 */
	public boolean unDeployWorkflow(String flowId,String userId)throws WFException;
	
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
	 * @param flowInstanceInfo
	 *            自定义流程实例表
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
			FlowInstanceInfo flowInstanceInfo, Map<String, Object> variables,boolean endStarter,String endOutcome,Map<String,Object> endVariables)
			throws WFException;
	
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
	 * @param flowInstanceInfo
	 *            自定义流程实例表
	 * @param variables
	 *            启动流程携带的变量
	 * @return 成功：流程实例id 失败：null Aug 9, 2012 3:57:37 PM gmh
	 */
	public String startWorkflow(String flowId, String userId,
			String starterType, String starterId,
			FlowInstanceInfo flowInstanceInfo, Map<String, Object> variables)
			throws WFException;
	
	
	/**
	 * 停止一条运行流程
	 * @param instanceId
	 * @param userId
	 * @param endState
	 * @return
	 * Aug 9, 2012 3:59:56 PM
	 * gmh
	 */
	public boolean endWorkflowInstance(String instanceId,String userId,String endState) throws WFException;
	
	
	/**
	 * 撤销一条流程
	 * @param flowInstanceId
	 *       流程实例id
	 * @param userId
	 *       用户id
	 * @param description
	 *       撤销原因
	 * @return
	 * Aug 9, 2012 4:05:21 PM
	 * gmh
	 */
	public boolean revokeWorkflowInstance(String flowInstanceId,String userId,String description)throws WFException;
	
	/**
	 * 完成一项任务
	 * @param taskId
	 *     任务id
	 * @param outcome
	 *     流出线
	 * @param userId
	 *     用户id
	 * @param variables
	 *     提交变量
	 * @return
	 * Aug 9, 2012 4:06:39 PM
	 * gmh
	 */
	public boolean completeTask(String taskId,String outcome
			,String userId,Map<String,Object> variables)  throws WFException;
	
	/**
	 * 拒绝任务（即驳回）
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 11:52:56 AM
	 * gmh
	 */
	public boolean rejectTask(String taskId,String userId)throws WFException;
	
	/**
	 * 撤销一个任务
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 11:54:37 AM
	 * gmh
	 */
	public boolean revokeTask(String taskId,String userId)throws WFException;
	
	/**
	 * 确认驳回
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 11:57:28 AM
	 * gmh
	 */
	public boolean confirmRejectTask(String taskId,String userId)throws WFException;
	
	/**
	 * 否决驳回，任务继续
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 12:00:59 PM
	 * gmh
	 */
	public boolean denyRejectTask(String taskId,String userId)throws WFException;
	
//	/**
//	 * 重分配任务接收者
//	 * @param taskId
//	 * @param assigneeType
//	 * @param assignee
//	 * @return
//	 * Aug 9, 2012 4:10:20 PM
//	 * gmh
//	 */
//	public boolean reassignTaskAssignee(String taskId,String assigneeType,String assignee)throws WFException;
	
	/**
	 * 获取任务
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 9, 2012 4:11:09 PM
	 * gmh
	 */
	public boolean takeTask(String taskId,String userId) throws WFException;
	
	/**
	 * 返还任务
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 9, 2012 4:11:37 PM
	 * gmh
	 */
	public boolean returnTask(String taskId,String userId)throws WFException;
	
	/**
	 * 查询某owner的某状态的专属任务
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return
	 * Aug 9, 2012 4:12:29 PM
	 * gmh
	 */
	public List queryOwnTasks(String ownerType,String ownerId,String status)throws WFException;
	
	/**
	 * 查询某owner的与其他owner共享的任务
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return
	 * Aug 9, 2012 4:13:26 PM
	 * gmh
	 */
	public List queryShareTasks(String ownerType,String ownerId,String status)throws WFException;
	
	/**
	 * 更换某个任务的任务执行人
	 * 只允许任务的当前执行人，可以做此操作；
	 * @param taskId
	 * @param oldUserId
	 *        任务的当前处理人
	 * @param newUserId
	 *        准备转派的新的处理人
	 * @return
	 * Sep 17, 2012 2:54:34 PM
	 * gmh
	 */
	public boolean changeTaskAssignee(String taskId,String oldUserId,String newUserId) throws WFException;
	
}
