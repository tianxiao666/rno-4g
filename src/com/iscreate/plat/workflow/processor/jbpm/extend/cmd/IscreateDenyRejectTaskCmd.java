package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateDenyRejectTaskCmd implements Command<Boolean> {

	private String taskId;
	private String userId;
	
	public IscreateDenyRejectTaskCmd(){
		
	}
	
	public IscreateDenyRejectTaskCmd(String taskId,String userId){
		this.taskId=taskId;
		this.userId=userId;
	}
	
	
	public Boolean execute(Environment environment) throws Exception {
		
		DbSession session=environment.get(DbSession.class);
		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
		if(fi==null){
			throw new WFException("不存在id为:"+taskId+"的任务！");
		}
		fi.setTaskStatus(ProcessConstants.TaskStatus.HANDLING);
		
		session.update(fi);
		
		//TODO
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(new Date());
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 否决任务:"+taskId+"的驳回请求");
		action.setActionName("否决驳回");
		action.setActionType(ProcessConstants.TaskActionType.DENY_REJECT);
		action.setInstanceId(fi.getInstanceId());
		action.setType(ProcessConstants.RecordObjectType.TASK);
		session.save(action);
		
		return true;
		
	}

}
