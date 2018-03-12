package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateEndProcessInstanceCmd implements Command<Boolean> {

	private String instanceId;
	private String userId;
	private String endState;

	public IscreateEndProcessInstanceCmd(String instanceId, String userId,
			String endState) {
		this.instanceId = instanceId;
		this.userId = userId;
		if (endState == null || endState.trim().equals("")) {
			endState = ProcessConstants.InstanceStatus.CANCEL;
		}
		this.endState = endState;
	}


	public Boolean execute(Environment environment) throws Exception {
		// 1、结束jbpm流程
		ExecutionService exeService = environment.get(ExecutionService.class);
//		exeService.endProcessInstance(instanceId, endState);
		DbSession dbSession = environment.get(DbSession.class);
		ExecutionImpl processInstance = (ExecutionImpl) dbSession.findProcessInstanceById(instanceId);
		 processInstance.end(endState);
		Session session = environment.get(Session.class);
		
		Date now = new Date();
		
		// 2、结束该流程相关的自定义的task
		Query query=session.createQuery("from FlowTaskInfo as t where t.instanceId =? and t.taskStatus<>? and t.taskStatus<>?");
		query.setString(0, instanceId);
		query.setString(1, ProcessConstants.TaskStatus.CANCELED);
		query.setString(2, ProcessConstants.TaskStatus.COMPLETED);
		List<FlowTaskInfo> ts=query.list();
		if(ts!=null && ts.size()>0){
			for(FlowTaskInfo t:ts){
				t.setCompleteDate(now);
				t.setTaskStatus(ProcessConstants.TaskStatus.CANCELED);
				session.update(t);
				
				//操作记录
				FlowTaskActionRecord action2=new FlowTaskActionRecord();
				action2.setTaskId(t.getTaskId());
				action2.setCreateTime(now);
				action2.setCreateUserId(userId);
				action2.setActionContent(userId+" 撤销流程导致任务:"+t.getTaskId()+"撤销");
				action2.setActionName("级联撤销");
				action2.setActionType(ProcessConstants.TaskActionType.CASCADE_CANCELED_BY_INSTANCE);
				
				session.save(action2);
			}
		}
		// 3、结束该流程
		FlowInstanceInfo instance = (FlowInstanceInfo) session.get(
				FlowInstanceInfo.class, instanceId);

		instance.setCurrent_activity_name("ended");
		instance.setInstance_status(ProcessConstants.InstanceStatus.CANCEL);
		session.update(instance);

		// 4、添加操作记录——流程
		FlowTaskActionRecord action2=new FlowTaskActionRecord();
		action2.setInstanceId(instanceId);
		action2.setTaskId("");
		action2.setCreateTime(now);
		action2.setCreateUserId(userId);
		action2.setActionContent(userId + "撤销流程");
		action2.setActionName("撤销");
		action2.setActionType(ProcessConstants.InstanceActionType.CANCEL);
		action2.setType(ProcessConstants.RecordObjectType.PROCESS);
		session.save(action2);

		return true;
	}

}
