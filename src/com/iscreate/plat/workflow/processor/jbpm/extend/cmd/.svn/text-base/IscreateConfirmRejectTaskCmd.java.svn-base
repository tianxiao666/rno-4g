package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.ProcessEngine;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateConfirmRejectTaskCmd implements Command<Boolean> {

	private String taskId;
	private String userId;
	
	public IscreateConfirmRejectTaskCmd(){
		
	}
	
	public IscreateConfirmRejectTaskCmd(String taskId,String userId){
		this.taskId=taskId;
		this.userId=userId;
	}
	

	public Boolean execute(Environment environment) throws Exception {
		
		DbSession session=environment.get(DbSession.class);
		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
		if(fi==null){
			throw new WFException("不存在id为:"+taskId+"的任务！");
		}
		//撤销
		fi.setTaskStatus(ProcessConstants.TaskStatus.CANCELED);
		
		session.update(fi);
		
		//将流程结束
		ProcessEngine pe = (ProcessEngine)environment.get(ProcessEngine.class);
		String piid=fi.getInstanceId();
		
		//jbpm流程
//		ProcessInstance processInstance=pe.getExecutionService().createProcessInstanceQuery().processInstanceId(piid).uniqueResult();
	    ExecutionImpl processInstance = (ExecutionImpl) session.findProcessInstanceById(piid);
	    if(processInstance==null){
			throw new WFException("不存在流程实例id为:"+piid+"的实例对象");
		}
	    processInstance.end(ProcessConstants.InstanceStatus.CANCEL);
		
	    //自定义流程
	    FlowInstanceInfo instance=session.get(FlowInstanceInfo.class,piid);
	    if(instance!=null){
	    	instance.setInstance_status(ProcessConstants.InstanceStatus.CANCEL);
	    	session.update(instance);
	    }
		//TODO
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(new Date());
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 确认任务:"+taskId+"的驳回请求");
		action.setActionName("确认驳回");
		action.setActionType(ProcessConstants.TaskActionType.CONFIRM_REJECT);
		action.setInstanceId(piid);
		action.setType(ProcessConstants.RecordObjectType.TASK);
		session.save(action);
		
		return true;
		
	}

}
