package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.List;
import java.util.Map;

import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessInstance;
import org.jbpm.pvm.internal.cmd.CompositeCmd;
import org.jbpm.pvm.internal.cmd.SetTaskVariablesCmd;
import org.jbpm.pvm.internal.repository.RepositoryServiceImpl;

import com.iscreate.plat.workflow.dataconfig.FlowInfo;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;

public class IscreateRepositoryServiceImpl extends RepositoryServiceImpl {

	public NewDeployment createIscreateDeployment(){
		return new IscreateDeploymentImpl(commandService);
	}
	
	public NewDeployment createIscreateDeployment(FlowInfo iscreateFlowDefinition){
		IscreateDeploymentImpl iscreateDeployment=new IscreateDeploymentImpl(commandService);
		iscreateDeployment.setIscreateFlowDefinition(iscreateFlowDefinition);
		return iscreateDeployment;
	}
	
	@Override
	/**
	 * @param  deploymentId
	 *    数据库dbid
	 * 
	 */
	public void deleteDeployment(String deploymentId) {
		commandService.execute(new IscreateDeleteDeploymentCmd(deploymentId));
	}
	
	
	/**
	 * 启动一条流程
	 * @return
	 * Aug 10, 2012 5:30:25 PM
	 * gmh
	 */
	public String startWorkflowInstance(String userId,String processDefinitionId,String starterType,String starterId,FlowInstanceInfo flowInstanceInfo,Map<String,Object> variables,boolean endStarter,String endOutcome,Map<String,Object> endVariables){
		ProcessInstance pi=commandService.execute(new IscreateStartProcessInstanceCmd(userId,processDefinitionId,flowInstanceInfo,variables,endStarter,endOutcome,endVariables));
		//TODO
		return  pi.getId();
	}
	/**
	 * 提交任务
	 * @param taskId
	 * @param outcome
	 * @param variables
	 * Aug 15, 2012 7:19:58 PM
	 * gmh
	 */
	public void completeTask(String taskId, String userId,String outcome, Map<String, ?> variables) {
	    SetTaskVariablesCmd setTaskVariablesCmd = new SetTaskVariablesCmd(taskId);
	    setTaskVariablesCmd.setVariables(variables);
	    CompositeCmd compositeCmd = new CompositeCmd();
	    compositeCmd.addCommand(setTaskVariablesCmd);
	    compositeCmd.addCommand(new IscreateCompleteTaskCmd(taskId, outcome,userId,variables));
	    commandService.execute(compositeCmd);
	  }
	
	/**
	 * 驳回任务
	 * @param taskId
	 * @param userId
	 * Aug 15, 2012 3:56:10 PM
	 * gmh
	 */
	public boolean rejectTask(String taskId,String userId){
		return commandService.execute(new IscreateRejectTaskCmd(taskId,userId));
	}
	
	/**
	 * 领取任务
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 7:19:52 PM
	 * gmh
	 */
	public boolean takeTask(String taskId,String userId){
		return commandService.execute(new IscreateTakeTaskCmd(taskId,userId));
	}
	
	/**
	 * 退回任务
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 7:19:44 PM
	 * gmh
	 */
	public boolean returnTask(String taskId,String userId){
		return commandService.execute(new IscreateReturnTaskCmd(taskId,userId));
	}
	
	/**
	 * 查询任务
	 * @param taskId
	 * @return
	 * Aug 15, 2012 7:19:38 PM
	 * gmh
	 */
	public FlowTaskInfo getTask(String taskId){
		return commandService.execute(new IscreateGetTaskCmd(taskId));
	}
	
	/**
	 * 查询实例
	 * @param instanceId
	 * @return
	 * Aug 15, 2012 7:19:31 PM
	 * gmh
	 */
	public FlowInstanceInfo getIntance(String instanceId){
		return commandService.execute(new IscreateGetInstanceCmd(instanceId));
	}
	
	/**
	 * 否决驳回请求
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 7:19:24 PM
	 * gmh
	 */
	public boolean denyRejectTask(String taskId,String userId){
		return commandService.execute(new IscreateDenyRejectTaskCmd(taskId,userId));
	}
	
	/**
	 * 确认驳回请求
	 * @param taskId
	 * @param userId
	 * @return
	 * Aug 15, 2012 7:19:16 PM
	 * gmh
	 */
	public boolean confirmRejectTask(String taskId,String userId){
		return commandService.execute(new IscreateConfirmRejectTaskCmd(taskId,userId));
	}
	
	public boolean revokeTask(String taskId,String userId){
		return commandService.execute(new IscreateRevokeTaskCmd(taskId,userId));
	}
	
	/**
	 * 结束流程
	 * @param instanceId
	 * @param userId
	 * @return
	 * Aug 17, 2012 11:34:32 AM
	 * gmh
	 */
	public boolean endWorkflowInstance(String instanceId,String userId,String endState){
		return commandService.execute(new IscreateEndProcessInstanceCmd(instanceId,userId,endState));
	}
	
	/**
	 * 找某任务或
	 * @param id
	 * @param type
	 * @return
	 * Aug 17, 2012 2:56:42 PM
	 * gmh
	 */
	public List<FlowInstanceInfo> getCreatedFlowinstance(String type,String id,List<String> status){
		return commandService.execute(new IscreateGetCreatedFlowinstanceCmd(type,id,status));
	}
	
	/**
	 * 重新分配任务处理人
	 * @param taskId
	 * @param oldUserId
	 * @param newUserId
	 * @return
	 * Sep 17, 2012 3:27:45 PM
	 * gmh
	 */
	public boolean reAssignTaskAssignee(String taskId,String oldUserId,String newUserId){
		return commandService.execute(new IscreateReAssignTaskAssigneeCmd(taskId,oldUserId,newUserId));
	}
}
