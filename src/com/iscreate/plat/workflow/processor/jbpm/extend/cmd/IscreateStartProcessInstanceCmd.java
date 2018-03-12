package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.cmd.StartProcessInstanceCmd;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.dataconfig.FlowNodeInfo;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateStartProcessInstanceCmd extends StartProcessInstanceCmd {

	private Logger log=Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FlowInstanceInfo flowInstanceInfo;
	protected Map<String, ?> variables,endVariables;
	private String endOutcome="";
	private boolean endStarter=false;
	private String userId;
	
	public IscreateStartProcessInstanceCmd(String userId,String processDefinitionId,
			Map<String, ?> variables, String executionKey) {
		super(processDefinitionId, variables, executionKey);
		this.variables=variables;
		this.userId=userId;
		
	}
	public IscreateStartProcessInstanceCmd(String userId,String processDefinitionId,
			Map<String, ?> variables, String executionKey,boolean endStarter) {
		super(processDefinitionId, variables, executionKey);
		this.userId=userId;
		this.variables=variables;
		this.endStarter=endStarter;
	}

	public  IscreateStartProcessInstanceCmd(String userId,String processDefinitionId,FlowInstanceInfo flowInstanceInfo,Map<String,Object> variables){
		super(processDefinitionId, variables, null);
		this.userId=userId;
		this.flowInstanceInfo=flowInstanceInfo;
		this.variables=variables;
	}
	
	public  IscreateStartProcessInstanceCmd(String userId,String processDefinitionId,FlowInstanceInfo flowInstanceInfo,Map<String,Object> variables,boolean endStarter,String endOutcome,Map<String,?> endVariables){
		super(processDefinitionId, variables, null);
		this.userId=userId;
		this.flowInstanceInfo=flowInstanceInfo;
		this.variables=variables;
		this.endStarter=endStarter;
		this.endOutcome=endOutcome;
		this.endVariables=endVariables;
	}
	
	
	public ProcessInstance execute(Environment environment) throws Exception {
		ProcessInstance processInstance= super.execute(environment);
		
		if(endStarter){//结束启动者
			//启动者是任务,结束该任务
			if(ProcessConstants.StarterType.STARTER_TASK.equals(flowInstanceInfo.getCreator_type())){
				String taskId=flowInstanceInfo.getOwner_task_id();
				IscreateCompleteTaskCmd cc=new IscreateCompleteTaskCmd(taskId, endOutcome, userId,endVariables) ;
				cc.execute(environment);
			}
		}
		//TODO
		//完善自定义表的信息
		 Session session = environment.get(Session.class);
		 
		 //自定义流程信息表
		 log.debug("填充FlowInstanceInfo");
		 String activityName="";
		 flowInstanceInfo.setInstance_id(processInstance.getId());
		 flowInstanceInfo.setInstance_status(ProcessConstants.InstanceStatus.RUNNING);
		 Set<String> names=processInstance.findActiveActivityNames();
		 
		 if(names!=null){
			for (Iterator iterator = names.iterator(); iterator.hasNext();) {
				activityName= (String) iterator.next();
				flowInstanceInfo.setCurrent_activity_name(activityName);
				break;
			}
		 }
	     session.save(flowInstanceInfo);
	     
	     //自定义任务信息表
	     String piid=processInstance.getId();
	     String pdid=processInstance.getProcessDefinitionId();
	     Date now=new Date();
	     String userId=flowInstanceInfo.getCreate_user_id();
	     
	     //通过流程定义id和活动名称找到自定义环节
	     Criteria criteria=session.createCriteria(FlowNodeInfo.class);
	     criteria.add(Restrictions.eq("flow_Id",pdid));
	     criteria.add(Restrictions.eq("node_name", activityName));
	     FlowNodeInfo node=(FlowNodeInfo)criteria.uniqueResult();
	     
	     
	     //起始节点
	     FlowTaskInfo task=new FlowTaskInfo();
	     task.setFlowId(pdid);
	     task.setInstanceId(piid);
	     task.setSendDate(now);
	     task.setSendUserId(userId);
	     task.setTaskId(piid+"_"+now.getTime()+"");
	     task.setTaskStatus(ProcessConstants.TaskStatus.COMPLETED);
	     task.setToNodeId("");//开始环节  TODO  暂空
	     task.setAcceptDate(now);
	     task.setCompleteDate(now);
	     task.setTaskName("开始");
	     
	     session.save(task);
	     
	     //当前节点
	     ProcessEngine pe = (ProcessEngine)environment.get(ProcessEngine.class);
	     TaskService ts=environment.get(TaskService.class);
	     if(ts==null){
	    	 throw new WFException("taskService 为空！");
	     }
	     
	     TaskQuery tq=ts.createTaskQuery();
	     
	     if(tq==null){
	    	 throw new WFException("taskQuery 为空！");
	     }
	    List<Task> tasks=tq.activityName(activityName).processInstanceId(piid).list();
//	    
	     
	     Task t=(tasks==null||tasks.isEmpty())?null:tasks.get(0);
	     if(t==null){
	    	 throw new WFException("不存在名称为:"+activityName+" 的任务");
	    	 
	     }
	     
	     Set<String> outcomes=ts.getOutcomes(t.getId());
	     String ocs="";
	     if(outcomes==null || outcomes.isEmpty()){
	    	 ocs="";
	     }else{
	    	 for (Iterator iterator = outcomes.iterator(); iterator.hasNext();) {
				ocs += (String) iterator.next()+",";
			}
	    	 ocs=ocs.substring(0,ocs.length()-1);
	     }
	     FlowTaskInfo task2=new FlowTaskInfo();
	     task2.setFlowId(pdid);
	     task2.setInstanceId(piid);
	     task2.setSendDate(now);
	     task2.setSendUserId(userId);
	     task2.setTaskId(t.getId());
//	     task2.setTaskStatus(((TaskImpl)t).getState());
	     task2.setTaskStatus(ProcessConstants.TaskStatus.INIT);
	     task2.setToNodeId(ocs);
	     task2.setNodeId(node.getNode_id());
	     task2.setTaskName(activityName);
	     task2.setCompleteDate(null);
	     //获取指定的任务接收人
	     String acceptorType=variables.get(ProcessConstants.VariableKey.ASSIGNEE_TYPE)==null?"":(String)variables.get(ProcessConstants.VariableKey.ASSIGNEE_TYPE);
	     String acceptorId=variables.get(ProcessConstants.VariableKey.ASSIGNEE_ID)==null?"":(String)variables.get(ProcessConstants.VariableKey.ASSIGNEE_ID);
	     //1、由变量里面得到
	     if(!acceptorType.equals("") && !acceptorId.equals("")){
	    	 task2.setAcceptorType(acceptorType);
	    	 String tId=acceptorId;
	    	 if(!tId.startsWith(",")){
	    		 tId=","+tId;
	    	 }
	    	 if(!tId.endsWith(",")){
	    		 tId+=",";
	    	 }
	    	 task2.setAcceptorUserId(tId);
	    	 task2.setAcceptDate(now);
	     }
	     
	     //2、从环节里得到
	     
	     //3、都无，则不填
	     
	     //任务接收者类型是人,且只有一个人，那么直接把这个人作为当前任务的处理人，并且
	     //任务状态改为:处理中
	     if(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE.equals(acceptorType)){
	    	 if(!acceptorId.contains(",")){
//	    		 task2.setAcceptDate(now);
	    		 task2.setCurrentUserId(acceptorId);
	    		 task2.setTaskStatus(ProcessConstants.TaskStatus.HANDLING);
	    	 }
	     }
	     
//	     System.out.println("===================== completedate = "+task2.getCompleteDate());
		 session.save(task2);
		 
		 
		//加操作记录
			FlowTaskActionRecord action=new FlowTaskActionRecord();
			action.setInstanceId(piid);
			action.setTaskId(t.getId());
			action.setCreateTime(now);
			action.setCreateUserId(userId);
			action.setActionContent(flowInstanceInfo.create_user_id+" 创建任务"+t.getId());
			action.setActionName("创建");
			action.setActionType(ProcessConstants.TaskActionType.CREATE);
			action.setType(ProcessConstants.RecordObjectType.TASK);
			session.save(action);
		 
			FlowTaskActionRecord ir = new FlowTaskActionRecord();
			ir.setActionContent(userId + "创建流程");
			ir.setActionType(ProcessConstants.InstanceActionType.CREATE);
			ir.setActionName("创建");
			ir.setTaskId("");
			ir.setCreateTime(now);
			ir.setCreateUserId(userId);
			ir.setInstanceId(piid);
			ir.setType(ProcessConstants.RecordObjectType.PROCESS);
			
			session.save(ir);
			
	     return processInstance;
	}
	

}
