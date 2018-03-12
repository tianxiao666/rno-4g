package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.cmd.Environment;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.cmd.CompleteTaskCmd;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.dataconfig.FlowNodeInfo;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateCompleteTaskCmd extends CompleteTaskCmd {

	private String userId;
	private Map<String,?> variables;
	
	public IscreateCompleteTaskCmd(String taskId) {
		super(taskId);
	}

	public IscreateCompleteTaskCmd(String taskId, String outcome) {
		super(taskId, outcome);
	}

	public IscreateCompleteTaskCmd(String taskId, String outcome, String userId) {
		super(taskId, outcome);
		this.userId = userId;
	}
	
	public IscreateCompleteTaskCmd(String taskId, String outcome, String userId,Map<String, ?> variables) {
		super(taskId, outcome);
		this.userId = userId;
		this.variables=variables;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	/**
	 * 1、结束jbpm任务 
	 * 2、更改自定义任务状态，
	 * 3、增加操作记录 
	 * 4、获取自定义流程信息
	 * 5、查看jbpm流程是否结束 
	 * 5.1）如果jbpm流程已经结束，则将自定义流程改为结束；
	 * 5.2）如果jbpm流程未结束，找出当前活动的节点，创建自己的任务节点，增加操作记录
	 * 
	 */
	public Void execute(Environment environment) throws Exception {

		DbSession session = EnvironmentImpl.getFromCurrent(DbSession.class);
		FlowTaskInfo ftask = session.get(FlowTaskInfo.class, taskId);

		//1、执行jbpm结束任务
//		outcome = ftask.getToNodeId();
//		if (outcome == null || outcome.trim().equals("")) {
//			outcome = "";
//			outcomeSpecified = false;
//		}else{
//			
//		}
//		outcomeSpecified = true;
		
		if(outcome==null || outcome.trim().equals("")){
			outcome="";
			outcomeSpecified=false;
		}
		Void v = super.execute(environment);
		
		//

		// 2、
		// 更改自己的任务状态
		ftask.setCompleteDate(new Date());
		ftask.setTaskStatus(ProcessConstants.TaskStatus.COMPLETED);

		session.update(ftask);
		
		//3、添加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(new Date());
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 完成任务:"+taskId);
		action.setActionName("结束");
		action.setType(ProcessConstants.RecordObjectType.TASK);
		action.setActionType(ProcessConstants.TaskActionType.COMPLETE);
		
		session.save(action);
		

		//4、获取自定义流程实例信息
		FlowInstanceInfo flowInstanceInfo = session.get(FlowInstanceInfo.class,
				ftask.getInstanceId());
		
		//5、看jbpm流程有无结束
		// 如果是最后一个节点，还要将流程的状态改为结束
		ProcessInstance processInstance = environment.get(
				ExecutionService.class).createProcessInstanceQuery()
				.processInstanceId(ftask.getInstanceId()).uniqueResult();
		if (processInstance == null) {
			flowInstanceInfo.setInstance_status(ProcessConstants.InstanceStatus.END);//置为结束
			flowInstanceInfo.setCurrent_activity_name(ProcessConstants.InstanceStatus.END);
			session.update(flowInstanceInfo);
			return v;
		}


		TaskService ts = environment.get(TaskService.class);
		if (ts == null) {
			throw new WFException("taskService 为空！");
		}

		TaskQuery tq = ts.createTaskQuery();

		if (tq == null) {
			throw new WFException("taskQuery 为空！");
		}
		
		String piid=flowInstanceInfo.getInstance_id();
		Date now = new Date();
		Set<String> names = processInstance.findActiveActivityNames();
//		System.out.println("============= activity name : " + names);
		String activityName = "";
		Session hsession=EnvironmentImpl.getFromCurrent(Session.class);
		if (names != null) {
			for (Iterator iterator = names.iterator(); iterator.hasNext();) {
				activityName = (String) iterator.next();
				flowInstanceInfo.setCurrent_activity_name(activityName);

				List<Task> tasks = tq.activityName(activityName)
				.processInstanceId(piid).list();
		//	    

				 //通过流程定义id和活动名称找到自定义环节
			     Criteria criteria=hsession.createCriteria(FlowNodeInfo.class);
			     criteria.add(Restrictions.eq("flow_Id",flowInstanceInfo.getFlow_id()));
			     criteria.add(Restrictions.eq("node_name", activityName));
			     FlowNodeInfo node=(FlowNodeInfo)criteria.uniqueResult();
			     
			     
			     
				//查询jbpm任务
		        Task t = (tasks == null || tasks.isEmpty()) ? null : tasks.get(0);
		        if(t==null){
		        	 throw new WFException("不存在名称为:"+activityName+" 的任务");
		        }
		        Set<String> outcomes=ts.getOutcomes(t.getId());
			     String ocs="";
			     if(outcomes==null || outcomes.isEmpty()){
			    	 ocs="";
			     }else{
			    	 for (Iterator iterator2 = outcomes.iterator(); iterator2.hasNext();) {
						ocs += (String) iterator2.next()+",";
					}
			    	 ocs=ocs.substring(0,ocs.length()-1);
			     }
			     
			    //增加自定义任务
				FlowTaskInfo task2 = new FlowTaskInfo();
				task2.setFlowId(ftask.getFlowId());
				task2.setInstanceId(ftask.getInstanceId());
				task2.setSendDate(now);
				task2.setSendUserId(userId);
				task2.setTaskId(t.getId());
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
			     //如果是具体到某个人，则直接指派给该人
			     if(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE.equals(acceptorType)){
			    	 if(!acceptorId.contains(",")){
//			    		 task2.setAcceptDate(now);
			    		 task2.setCurrentUserId(acceptorId);
			    		 task2.setTaskStatus(ProcessConstants.TaskStatus.HANDLING);
			    	 }
			     }
				
				session.save(task2);

				//创建任务的记录
				FlowTaskActionRecord action2=new FlowTaskActionRecord();
				action2.setTaskId(t.getId());
				action2.setCreateTime(new Date());
				action2.setCreateUserId(userId);
				action2.setActionContent(userId+" 创建任务:"+t.getId());
				action2.setActionName("创建");
				action2.setActionType(ProcessConstants.TaskActionType.CREATE);
				action2.setInstanceId(piid);
				action2.setType(ProcessConstants.RecordObjectType.TASK);
				session.save(action2);

//				break;
			}
		}else{
			
		}

		return v;
	}

}
