package com.iscreate.plat.workflow.processor.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.pvm.internal.cfg.ConfigurationImpl;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.constraint.RelAnalyzerBean;
import com.iscreate.plat.workflow.dataconfig.FlowInfo;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.FlowProcessorBean;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.processor.jbpm.extend.cmd.IscreateRepositoryServiceImpl;

/**
 * 工作流服务 需要利用aop统一进行权限的检查
 * 
 * @author brightming
 * 
 */
public class FlowProcessorBeanImpl implements FlowProcessorBean {

	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 单例存在的
	 */
	private static ProcessEngine processEngine;
	
	static FlowProcessorBeanImpl instance;
	// private static RepositoryService repositoryService;
	// private static ExecutionService executionService;
	// private static TaskService taskService;
	// private static TaskQuery taskQuery;
	// private Session session;
	private RelAnalyzerBean relAnalyzerBean;
	private static IscreateRepositoryServiceImpl iscreateRepositoryServiceImpl;
    
	private FlowProcessorBeanImpl(){
	}
	public synchronized  static FlowProcessorBeanImpl getInstance(){
		if(instance==null){
			instance=new FlowProcessorBeanImpl();
			processEngine=new ConfigurationImpl().setResource("jbpm.cfg.xml").buildProcessEngine();
			iscreateRepositoryServiceImpl = processEngine.get(IscreateRepositoryServiceImpl.class);
		}
		
		return instance;
	}
	public void setProcessEngine(ProcessEngine processEngine) {
		this.processEngine = processEngine;
		this.iscreateRepositoryServiceImpl = processEngine
				.get(IscreateRepositoryServiceImpl.class);
	}

	/* 1、应用列表: */
	public List getApplicationList() throws WFException {
		// TODO
		return null;
	}

	// /**
	// * 发布一条流程
	// * @param zis
	// * @return
	// * @throws WFException
	// * Aug 8, 2012 10:07:42 AM
	// * gmh
	// */
	// public String deployWorkFlow(ZipInputStream zis) throws WFException{
	// return
	// repositoryService.createDeployment().addResourcesFromZipInputStream(zis).deploy();
	// }
	//
	// /* 2、发起流程 */
	// /**
	// * @param flowId
	// * 流程定义id
	// * @param userId
	// * 启动流程用户id
	// * @return
	// * 启动成功，则返回流程实例的id；
	// * 启动失败，则返回null
	// */
	// public String startFlow(String flowId, String userId) throws WFException
	// {
	// return startFlow(flowId,userId,StarterType.STARTER_PEOPLE);
	// }
	//
	// /**
	// *
	// * @param flowId
	// * 流程定义id
	// * @param starterId
	// * 启动者id
	// * @param starterType
	// * 启动者类型
	// * @return
	// * 启动成功：流程实例id
	// * 启动失败：null
	// * @throws WFException
	// * Aug 7, 2012 5:22:24 PM
	// * gmh
	// */
	// public String startFlow(String flowId,String starterId,String
	// starterType) throws WFException {
	//		
	// //启动jbpm流程
	// log.debug("进入startFlow方法.参数： flowId="+flowId+",starterId =
	// "+starterId+",starterType = "+starterType);
	// if(flowId==null || flowId.trim().equals("")){
	// throw new WFException("启动流程未指定流程定义id!");
	// }
	// if(starterId==null || starterId.trim().equals("")){
	// throw new WFException("启动流程未指定启动者id!");
	// }
	// if(starterType==null || starterType.trim().equals("")){
	// throw new WFException("未指明流程启动者类型!");
	// }
	//		
	// //如果是task类型的，判断该task能否启动该流程
	// if(starterType.equals(StarterType.STARTER_TASK)){
	// Task parTask=processEngine.getTaskService().getTask(starterId);
	// if(parTask==null){
	// throw new WFException("启动流程时，指定的任务类型的启动者："+starterId+"不存在!");
	// }
	// //寻找所属流程
	// ProcessInstance parPi=((TaskImpl)parTask).getProcessInstance();
	// if(parPi==null){
	// log.error("任务id = "+starterId+" 没有对应的流程实例。不允许启动流程。");
	// return null;
	// }
	// String pdId=parPi.getProcessDefinitionId();
	// boolean isAllowToStart=checkIfAllowToStart(pdId, flowId);
	// if(!isAllowToStart){
	// log.error("任务 id="+starterId+" 所属的流程不允许启动流程id = "+flowId+" 的流程");
	// return null;
	// }
	// }
	// log.debug("准备启动jbpm工作流,流程定义id="+flowId);
	// ProcessInstance
	// pi=processEngine.getExecutionService().startProcessInstanceById(flowId);
	// if(pi==null){
	// log.error("启动jbpm工作流失败！");
	// return null;
	// }
	// log.debug("成功创建jbpm工作流!流程实例id = "+pi.getId());
	// log.debug("开始保存jbpm外信息到自定义表...");
	// FlowInstanceInfo fii=new FlowInstanceInfo();
	//		
	// fii.setCreate_time(new Date());
	// fii.setCreate_user_id(starterId);
	// fii.setFlow_id(flowId);
	// fii.setInstance_id(pi.getId());
	// fii.setStarter_type(starterType);
	//		
	// //TODO
	// //to be clear
	// fii.setInstance_status("");
	// fii.setInstance_type("");
	// fii.setObject_id("");
	// fii.setSummary("");
	// // session.beginTransaction();
	// Serializable se=session.save(fii);
	// if(se==null){
	// log.error("启动流程时，保存到自定义表的信息保存失败!");
	// //结束之前开启的流程
	// log.error("准备结束开启的jbpm流程...");
	// processEngine.getExecutionService().endProcessInstance(pi.getId(),
	// "ended");
	// log.error("完成结束开启的jbpm流程");
	// log.error("启动工作流失败");
	// return null;
	// }
	// log.debug("成功保存jbpm外信息到自定义表");
	// log.debug("启动工作流成功");
	//		
	// return pi.getId();
	// }
	//	
	// /**
	// * 结束一条流程
	// * @param instanceId
	// * @return
	// * @throws WFException
	// * Aug 8, 2012 10:08:58 AM
	// * gmh
	// */
	// public boolean endWorkflow(String instanceId) throws WFException {
	// log.debug("进入endWorkFlow方法。参数: instanceId = "+instanceId);
	// if(instanceId==null || "".equals(instanceId.trim())){
	// throw new WFException("未指定要结束的流程实例id!");
	// }
	// executionService.endProcessInstance(instanceId, "ended");
	//		
	// //TODO
	// //处理自定义中的信息
	//		
	// return true;
	// }
	//	
	// /* 3、处理任务 */
	// public boolean doTask(String taskId, String userId) throws WFException {
	// log.debug("进入doTask 方法。参数 ： taskId = "+taskId+" , userId = "+userId);
	// if(taskId==null || taskId.trim().equals("")){
	// log.error("未指定要完成的任务的id！");
	// throw new WFException("未指定要完成的任务的id！");
	// }
	//		
	// if(userId==null || userId.trim().equals("")){
	// log.error("未指定做任务的用户！");
	// throw new WFException("未指定做任务的用户！");
	// }
	//		
	// Task task=taskService.getTask(taskId);
	// if(task==null){
	// log.error("不存在id = "+taskId+" 的任务！");
	// return false;
	// }
	//		
	// if(!task.getAssignee().equals(userId)){
	// log.error("任务id="+taskId+",name = "+task.getActivityName()+" 不是用户
	// ："+userId+" 的任务！");
	// }
	//		
	// return true;
	// }
	//
	// /* 4、填写处理意见 */
	// public void writeMes(String taskId, String mes) throws WFException {
	//		
	// }
	//
	// /* 5、提取任务列表 */
	// public List getTasks(String userId, String taskStatus) throws WFException
	// {
	// // TODO
	// return null;
	// }
	//
	// /* 6、督办 */
	// public void supervision(String taskId, String supervisionMes,
	// String supervisionUserId) throws WFException {
	//
	// }
	//
	// /* 7、流程间的约束检测 */
	// public void checkFlowsForRel(String flowId1, String flowId2)
	// throws WFException {
	//
	// }
	//
	// /* 9、送阅 */
	// public void sendRead(String instanceId, List Users) throws WFException {
	//
	// }
	//
	// /* 10、指派任务 */
	// public void setTaskForUser(String flowId, String nodeId, String userId)
	// throws WFException {
	//
	// }
	//	
	// /**
	// *
	// * @return
	// * Aug 7, 2012 5:53:29 PM
	// * gmh
	// */
	// private boolean checkIfAllowToStart(String flowIdA,String flowIdB){
	// Relation ra=null;
	// try {
	// ra = relAnalyzerBean.analyzeFlowRelation(flowIdA, flowIdB);
	// } catch (WFException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return false;
	// }
	// if(ra == FlowRelation.Relation.Relation_1 ||
	// ra == FlowRelation.Relation.Relation_2){
	// return true;
	// }else{
	// return false;
	// }
	// }

	/**
	 * @param classPathResourceName
	 *            类路径命名的jpdl资源文件
	 * @param iscreateFlowDefinition
	 * 
	 * @return 流程定义的id
	 */
	public String deployWorkflow(String classPathResourceName,
			FlowInfo iscreateFlowDefinition, String userId) throws WFException {
		log.debug("进入deployWorkflow方法。参数：classPathResourceName ="
				+ classPathResourceName + ",iscreateFlowDefinition= "
				+ iscreateFlowDefinition + ",userId = " + userId);
		if (classPathResourceName == null
				|| "".equals(classPathResourceName.trim())) {
			log.error("未指定要发布的流程文件的路径！");
			throw new WFException("未指定要发布的流程文件的路径！");
		}
		if (userId == null || userId.trim().equals("")) {
			log.error("未指定发布流程的用户！");
			throw new WFException("未指定发布流程的用户！");
		}
		// TODO
		// 检查userid是否有发布的权限
		log.debug("准备发布流程...");
		iscreateFlowDefinition.setDeployer_id(userId);
		String pdid = processEngine.get(IscreateRepositoryServiceImpl.class)
				.createIscreateDeployment(iscreateFlowDefinition)
				.addResourceFromClasspath(classPathResourceName).deploy();
		if (pdid == null || pdid.equals("")) {
			log.error("流程发布失败！");
		} else {
			log.debug("流程发布成功！流程定义的id为：" + pdid);
		}

		return pdid;
	}

	/**
	 * @param zis
	 * @param flowDefinition
	 * @param userId
	 * 
	 * @return
	 * 
	 */
	public String deployWorkflow(ZipInputStream zis, FlowInfo flowDefinition,
			String userId) {
		// processEngine.getRepositoryService().createDeployment().addResourcesFromZipInputStream(zis).deploy();
		return processEngine.get(IscreateRepositoryServiceImpl.class)
				.createIscreateDeployment(flowDefinition)
				.addResourcesFromZipInputStream(zis).deploy();
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
		// TODO
		// 检查用户是否有权限删除流程定义
		try {
			processEngine.get(IscreateRepositoryServiceImpl.class)
					.deleteDeployment(flowId);
		} catch (Exception e) {
			throw new WFException("该流程定义:" + flowId + " 关联的任务未执行完，不能删除!");
		}
		return true;
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
	 * @param flowInstanceInfo
	 *            自定义流程实例表
	 * @param variables
	 *            启动流程携带的变量
	 * @return 成功：流程实例id 失败：null Aug 9, 2012 3:57:37 PM gmh
	 */
	public String startWorkflow(String flowId, String userId,
			String starterType, String starterId,
			FlowInstanceInfo flowInstanceInfo, Map<String, Object> variables)
			throws WFException{
		return startWorkflow(flowId,userId,starterType,starterId,flowInstanceInfo,variables,false,null,null);
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
			throws WFException {
		// TODO
		// 权限检查
		// 检查是否存在flowid的流程
		// 检查starter是否能启动该流程

		ProcessDefinition pd = processEngine.getRepositoryService()
				.createProcessDefinitionQuery().processDefinitionId(flowId)
				.uniqueResult();
		// log.debug("=======================#################### pd =
		// "
		// + pd);
		if (pd == null) {
			log.error("不存在流程定义为:" + flowId + "的流程");
			throw new WFException("不存在流程定义为:" + flowId + "的流程");
		}
		if (flowInstanceInfo == null) {
			flowInstanceInfo = new FlowInstanceInfo();
		}
		flowInstanceInfo.setCreator_type(starterType);
		flowInstanceInfo.setCreate_time(new Date());
		flowInstanceInfo.setCreate_user_id(userId);
		if (ProcessConstants.StarterType.STARTER_PROCESS.equals(starterType)) {
			// flowInstanceInfo.setOwner(starterId);
			flowInstanceInfo.setOwner_instance_id(starterId);
		} else if (ProcessConstants.StarterType.STARTER_TASK
				.equals(starterType)) {
			flowInstanceInfo.setOwner_task_id(starterId);
			// 找task对应的instance id
			FlowTaskInfo fi = iscreateRepositoryServiceImpl.getTask(starterId);
			if (fi == null) {
				throw new WFException("启动者类型是：任务。该任务：" + starterId + "没有对应的流程！");

			}
			flowInstanceInfo.setOwner_instance_id(fi.getInstanceId());
		}
		flowInstanceInfo.setFlow_id(flowId);

		
		return iscreateRepositoryServiceImpl.startWorkflowInstance(userId,flowId,
				starterType, starterId, flowInstanceInfo, variables,endStarter,endOutcome,endVariables);
	}

	/**
	 * 停止一条运行流程
	 * 
	 * @param instanceId
	 * @param userId
	 * @param endState
	 * @return Aug 9, 2012 3:59:56 PM gmh
	 */
	public boolean endWorkflowInstance(String instanceId, String userId,
			String endState) throws WFException {
		log.debug("进入 endWorkflowInstance 方法。参数: instanceId = " + instanceId
				+ ",userId = " + userId);

		StringBuilder msg = new StringBuilder();
		if (!canEndWorkflowInstance(instanceId, userId, msg)) {
			throw new WFException(msg.toString());
		}

		return iscreateRepositoryServiceImpl.endWorkflowInstance(instanceId,
				userId, endState);
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
			String description) {
		// TODO
		return false;
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
		log
				.debug("进入completeTask方法。参数：taskId = " + taskId + ",outcome = "
						+ outcome + ",userId = " + userId + ",variables = "
						+ variables);
		// TODO
		// 检查是否是该user的任务
		boolean ownTask = false;
		List<FlowTaskInfo> tasks = (List<FlowTaskInfo>) queryOwnTasks(
				ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE, userId,
				ProcessConstants.TaskStatus.HANDLING);
		FlowTaskInfo task = null;
		if (tasks != null) {
			for (int i = 0; i < tasks.size(); i++) {
				task = tasks.get(i);
				log.debug(" ====== " + task.getTaskId());
				if (task.getTaskId().equals(taskId)) {
					ownTask = true;
					break;
				}
			}
		}

		if (!ownTask) {
			throw new WFException("task ：" + taskId + ",不是用户:" + userId
					+ " 的任务！");
		}

		
		
		// 该任务如果是流程的最后一个节点
		StringBuilder msg = new StringBuilder();
		// 验证该任务派发的子流程是否已经结束，
		//TODO
		boolean ok = canCompleteTask(taskId, userId, msg);

		if (!ok) {
			throw new WFException(msg.toString());
		}
		iscreateRepositoryServiceImpl.completeTask(taskId, userId, outcome,
				variables);

		return true;
	}

	/**
	 * 拒绝任务（即驳回）
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:52:56 AM gmh
	 */
	public boolean rejectTask(String taskId, String userId) throws WFException {
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待驳回的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定待驳回任务的人！");
		}
		if (!canBeRejected(taskId, userId)) {
			throw new WFException("用户:" + userId + ",不能驳回任务:" + taskId);
		}
		iscreateRepositoryServiceImpl.rejectTask(taskId, userId);
		return true;
	}

	/**
	 * 撤销一个任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:54:37 AM gmh
	 */
	public boolean revokeTask(String taskId, String userId) throws WFException {
		log.debug("进入方法：revokeTask。参数:taskId = " + taskId + ",userId = "
				+ userId);
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待撤销的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定撤销任务的人！");
		}

		StringBuilder msg = new StringBuilder();
		if (!canRevokeTask(taskId, userId, msg)) {
			throw new WFException("用户:" + userId + " 不能撤销任务:" + taskId
					+ " ！原因：" + msg.toString());
		}

		return iscreateRepositoryServiceImpl.revokeTask(taskId, userId);

	}

	/**
	 * 确认驳回
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 11:57:28 AM gmh
	 */
	public boolean confirmRejectTask(String taskId, String userId)
			throws WFException {

		log.debug("进入方法：confirmRejectTask。参数:taskId = " + taskId + ",userId = "
				+ userId);
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待确认驳回的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定待确认驳回任务的人！");
		}

		if (!canConfirmRejectTask(taskId, userId)) {
			throw new WFException("用户:" + userId + " 不能确认任务:" + taskId
					+ "的驳回请求！");
		}

		return iscreateRepositoryServiceImpl.confirmRejectTask(taskId, userId);

	}

	/**
	 * 否决驳回，任务继续
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 12:00:59 PM gmh
	 */
	public boolean denyRejectTask(String taskId, String userId)
			throws WFException {

		log.debug("进入方法：denyRejectTask。参数:taskId = " + taskId + ",userId = "
				+ userId);
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待否决驳回的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定待否决驳回任务的人！");
		}

		if (!canDenyRejectTask(taskId, userId)) {
			throw new WFException("用户:" + userId + " 不能否决任务:" + taskId
					+ "的驳回请求！");
		}

		return iscreateRepositoryServiceImpl.denyRejectTask(taskId, userId);

	}

	// /**
	// * 重分配任务接收者
	// *
	// * @param taskId
	// * @param assigneeType
	// * @param assignee
	// * @return Aug 9, 2012 4:10:20 PM gmh
	// */
	// public boolean reassignTaskAssignee(String taskId, String assigneeType,
	// String assignee) throws WFException {
	// // TODO
	// return false;
	// }

	/**
	 * 获取任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 9, 2012 4:11:09 PM gmh
	 */
	public boolean takeTask(String taskId, String userId) throws WFException {
		log
				.debug("进入方法：takeTask。参数:taskId = " + taskId + ",userId = "
						+ userId);
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待领取的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定待领取任务的人！");
		}

		if (!canTakeTask(taskId, userId)) {
			throw new WFException("用户:" + userId + " 不是任务:" + taskId + "的候选人！");
		}

		iscreateRepositoryServiceImpl.takeTask(taskId, userId);
		return true;
	}

	/**
	 * 返还任务
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 9, 2012 4:11:37 PM gmh
	 */
	public boolean returnTask(String taskId, String userId) throws WFException {

		log.debug("进入方法：returnTask。参数:taskId = " + taskId + ",userId = "
				+ userId);
		if (taskId == null || taskId.trim().equals("")) {
			throw new WFException("待退回的任务id为空！");
		}
		if (userId == null || userId.trim().equals("")) {
			throw new WFException("未指定待退回任务的人！");
		}

		StringBuilder msg = new StringBuilder();
		if (!canReturnTask(taskId, userId, msg)) {
			// throw new WFException("用户:" + userId + " 不是任务:" + taskId +
			// "的负责人！");
			throw new WFException(msg.toString());
		}

		return iscreateRepositoryServiceImpl.returnTask(taskId, userId);

	}

	/**
	 * 查询某owner的某状态的专属任务
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return Aug 9, 2012 4:12:29 PM gmh
	 */
	public List queryOwnTasks(String ownerType, String ownerId, String status) {
		// TODO

		Session session = processEngine.get(SessionFactory.class).openSession();
		if (ownerType.equals(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE)) {
			Query query = session
					.createQuery("select flowTaskInfo from FlowTaskInfo as flowTaskInfo where flowTaskInfo.taskStatus=? and flowTaskInfo.currentUserId=?");
			query.setString(0, status);
			query.setString(1, ownerId);
			return query.list();
		}

		else {
			return Collections.emptyList();
		}
	}

	/**
	 * 查询某owner的与其他owner共享的任务
	 * 
	 * @param ownerType
	 * @param ownerId
	 * @param status
	 * @return Aug 9, 2012 4:13:26 PM gmh
	 */
	public List queryShareTasks(String ownerType, String ownerId, String status) {
		Session session = processEngine.get(SessionFactory.class).openSession();
		Query query = session
				.createQuery("select flowTaskInfo from FlowTaskInfo as flowTaskInfo where flowTaskInfo.taskStatus =:st AND flowTaskInfo.acceptorType= :ownertype and flowTaskInfo.acceptorUserId like :ownerId and  (flowTaskInfo.currentUserId is null or flowTaskInfo.currentUserId is empty)");
		query.setString("st", status);
		query.setString("ownertype", ownerType);
		query.setString("ownerId", "%," + ownerId + ",%");
//		query.setParameter(3, null, Hibernate.STRING);
//		log.debug(query.getQueryString());
		List result=null;
		result=query.list();
		
		if (ownerType.equals(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE)) {
			//TODO
			// 找人的
			// 1、找到人所有的角色
			// 循环找该角色的某状态的共享任务

			//2、合并上面找到的
			
		} 
		
		return result;
	}

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
	public boolean changeTaskAssignee(String taskId,String oldUserId,String newUserId) throws WFException{
		
		log.debug("进入reAssignTaskAssignee。taskId="+taskId+",oriUserId="+oldUserId+",newUserId="+newUserId);
		if(taskId==null || "".equals(taskId.trim())){
			throw new WFException("任务id不能为空！");
		}
		if(oldUserId==null || "".equals(oldUserId.trim())){
			throw new WFException("任务的当前处理人必须指定！");
		}
		if(newUserId==null || "".equals(newUserId.trim())){
			throw new WFException("任务的新的处理人必须指定！");
		}
		
		return iscreateRepositoryServiceImpl.reAssignTaskAssignee(taskId, oldUserId, newUserId);
		
	}
	
	/**
	 * 判断某任务是否能结束
	 * 
	 * @param taskId
	 * @return Aug 14, 2012 6:51:24 PM gmh
	 */
	private boolean canCompleteTask(String taskId, String userId,
			StringBuilder msg) {
		if (userId == null || userId.trim().equals("")) {
			msg.append("用户id为空！");
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			msg.append("任务id为空！");
			return false;
		}

		FlowTaskInfo fi = iscreateRepositoryServiceImpl.getTask(taskId);
		if (fi == null) {
			log.error("不存在id为：" + taskId + "的任务");
			msg.append("不存在id为：" + taskId + "的任务");
			return false;
		}

		// 正在处理，当前责任人
		if (!userId.equals(fi.getCurrentUserId())) {
			msg.append("用户：" + userId + " 不是任务:" + taskId + " 的当前负责人！");
			return false;
		}
		if (!ProcessConstants.TaskStatus.HANDLING.equals(fi.getTaskStatus())) {
			msg.append("任务:" + taskId + " 不在可完成的阶段！");
			return false;
		}

		// 判断有没有该任务启动的还没有完成的又必须等待其先完成的子任务
		boolean needToWait = false;
		List<String> status = new ArrayList<String>();
		status.add(ProcessConstants.InstanceStatus.PAUSE);
		status.add(ProcessConstants.InstanceStatus.RUNNING);
		List<FlowInstanceInfo> ins = iscreateRepositoryServiceImpl
				.getCreatedFlowinstance(
						ProcessConstants.StarterType.STARTER_TASK, taskId,
						status);
		// TODO
		if (ins != null && ins.size()>0) {
			log.debug("未结束的子任务:");
			for (FlowInstanceInfo in : ins) {
				// 如果流程未结束，看是否待结束的task和该流程有等待关系
				log.debug("------------- " + in.getInstance_id());
				/*
				 * try { Relation
				 * r=relAnalyzerBean.analyzeFlowRelation(fi.getFlowId(),
				 * in.getFlow_id()); if(r.compareTo(Relation.Relation_1)==0){
				 * needToWait=true; break; } } catch (WFException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
				 needToWait=true;//TODO hard code
			}
		}


		if (needToWait) {
			msg.append("task：" + taskId + "创建的同步子流程未全部结束，该task不允许结束！");
			return false;
		}
		return true;

	}

	/**
	 * 该任务能否被该用户驳回
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 3:49:37 PM gmh
	 */
	private boolean canBeRejected(String taskId, String userId) {
		if (userId == null || userId.trim().equals("")) {
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			return false;
		}

		FlowTaskInfo fi = iscreateRepositoryServiceImpl.getTask(taskId);
		if (fi == null) {
			log.error("不存在id为：" + taskId + "的任务");
			return false;
		}
		if (userId.equals(fi.getCurrentUserId())
				&& ProcessConstants.TaskStatus.HANDLING.equals(fi
						.getTaskStatus())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断用户是否是指定任务的候选人
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 5:46:23 PM gmh
	 */
	private boolean canTakeTask(String taskId, String userId) {
		if (userId == null || userId.trim().equals("")) {
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			return false;
		}

		FlowTaskInfo fi = iscreateRepositoryServiceImpl.getTask(taskId);
		if (fi == null) {
			log.error("不存在id为：" + taskId + "的任务");
			return false;
		}

		if (!ProcessConstants.TaskStatus.INIT.equals(fi.getTaskStatus())) {
			log.debug("任务:" + taskId + "已经有负责人！");
			return false;
		}

		// TODO
		// 判断是否是候选人
		return true;
	}

	/**
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 6:30:48 PM gmh
	 */
	private boolean canReturnTask(String taskId, String userId,
			StringBuilder msg) {
		if (userId == null || userId.trim().equals("")) {
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			return false;
		}

		FlowTaskInfo fi = iscreateRepositoryServiceImpl.getTask(taskId);
		if (fi == null) {
			log.error("不存在id为：" + taskId + "的任务");
			msg.append("不存在id为：" + taskId + "的任务");
			return false;
		}
		// 处在handling，且是当前负责人
		if (!ProcessConstants.TaskStatus.HANDLING.equals(fi.getTaskStatus())) {
			msg.append("任务：" + taskId + "不在可返回状态");
			return false;
		}

		if (!userId.equals(fi.getCurrentUserId())) {
			msg.append("用户：" + userId + " 不是任务:" + taskId + "的负责人！");
			return false;
		}

		// TODO

		return true;
	}

	/**
	 * 用户能否否决某个任务的驳回请求
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 6:58:33 PM gmh
	 */
	private boolean canDenyRejectTask(String taskId, String userId) {
		if (userId == null || userId.trim().equals("")) {
			log.error("用户id为空！");
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			log.error("任务id为空！");
			return false;
		}

		FlowTaskInfo ft = iscreateRepositoryServiceImpl.getTask(taskId);
		if (ft == null) {
			log.error("不存在id为：" + taskId + "的任务！");
			return false;
		}

		// 任务是否处于暂停状态
		if (!ProcessConstants.TaskStatus.SUSPENDED.equals(ft.getTaskStatus())) {
			log.error("任务：" + taskId + "不是处于暂停状态，不能进行否决驳回操作！");
			return false;
		}

		FlowInstanceInfo fi = iscreateRepositoryServiceImpl.getIntance(ft
				.getInstanceId());
		if (fi == null) {
			log.error("不存在id为：" + ft.getInstanceId() + "的流程！");
			return false;
		}

		// 创建者
		if (userId.equals(fi.getCreate_user_id())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 用户能否确认驳回请求
	 * 
	 * @param taskId
	 * @param userId
	 * @return Aug 15, 2012 7:16:48 PM gmh
	 */
	private boolean canConfirmRejectTask(String taskId, String userId) {
		if (userId == null || userId.trim().equals("")) {
			log.error("用户id为空！");
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			log.error("任务id为空！");
			return false;
		}

		FlowTaskInfo ft = iscreateRepositoryServiceImpl.getTask(taskId);
		if (ft == null) {
			log.error("不存在id为：" + taskId + "的任务！");
			return false;
		}

		// 任务是否处于暂停状态
		if (!ProcessConstants.TaskStatus.SUSPENDED.equals(ft.getTaskStatus())) {
			log.error("任务：" + taskId + "不是处于暂停状态，不能进行确认驳回请求的操作！");
			return false;
		}

		FlowInstanceInfo fi = iscreateRepositoryServiceImpl.getIntance(ft
				.getInstanceId());
		if (fi == null) {
			log.error("不存在id为：" + ft.getInstanceId() + "的流程！");
			return false;
		}

		// 创建者
		if (userId.equals(fi.getCreate_user_id())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean canRevokeTask(String taskId, String userId,
			StringBuilder msg) {
		if (userId == null || userId.trim().equals("")) {
			log.error("用户id为空！");
			msg.append("用户id为空！");
			return false;
		}
		if (taskId == null || taskId.trim().equals("")) {
			log.error("任务id为空！");
			msg.append("任务id为空！");
			return false;
		}

		FlowTaskInfo ft = iscreateRepositoryServiceImpl.getTask(taskId);
		if (ft == null) {
			log.error("不存在id为：" + taskId + "的任务！");
			msg.append("不存在id为：" + taskId + "的任务！");
			return false;
		}

		// 任务是否处于未领取状态
		if (!ProcessConstants.TaskStatus.INIT.equals(ft.getTaskStatus())) {
			log.error("任务：" + taskId + "不是处于"
					+ ProcessConstants.TaskStatus.INIT + "状态，不能进行撤销的操作！");
			msg.append("任务：" + taskId + "不是处于"
					+ ProcessConstants.TaskStatus.INIT + "状态，不能进行撤销的操作！");
			return false;
		}

		FlowInstanceInfo fi = iscreateRepositoryServiceImpl.getIntance(ft
				.getInstanceId());
		if (fi == null) {
			log.error("不存在id为：" + ft.getInstanceId() + "的流程！");
			msg.append("不存在id为：" + ft.getInstanceId() + "的流程！");
			return false;
		}

		// 创建者
		if (userId.equals(fi.getCreate_user_id())) {
			return true;
		} else {
			msg.append("用户:" + userId + " 不是任务：" + taskId + "所在流程的创建者");
			return false;
		}
	}

	/**
	 * 是否能结束流程
	 * 
	 * @param instanceId
	 * @param userId
	 * @param msg
	 * @return Aug 17, 2012 11:12:00 AM gmh
	 */
	private boolean canEndWorkflowInstance(String instanceId, String userId,
			StringBuilder msg) {
		if (instanceId == null || instanceId.trim().equals("")) {
			msg.append("未指定要结束的流程实例id！");
			return false;
		}
		if (userId == null || userId.trim().equals("")) {
			msg.append("未指定要结束流程实例的用户！");
			return false;
		}
		FlowInstanceInfo instance = iscreateRepositoryServiceImpl
				.getIntance(instanceId);
		if (instance == null) {
			msg.append("不存在id为：" + instanceId + "的流程实例！");
			return false;
		}
		// 1、流程是否处于可结束状态
		if (ProcessConstants.InstanceStatus.CANCEL.equals(instance
				.getInstance_status())
				|| ProcessConstants.InstanceStatus.END.equals(instance
						.getInstance_status())) {
			msg.append("流程：" + instanceId + "已经结束，不可再执行撤销动作！");
			return false;
		}
		// 2、流程是否是该user创建的
		if (!userId.equals(instance.getCreate_user_id())) {
			msg.append("存在id为：" + instanceId + "的流程实例,不是用户:" + userId
					+ "创建的，不可由该用户结束！");
			return false;
		}

		return true;
	}
}
