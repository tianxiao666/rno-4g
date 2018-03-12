package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateTakeTaskCmd implements Command<Boolean> {

	private String taskId;
	private String userId;
	public IscreateTakeTaskCmd(){
		
	}
	public IscreateTakeTaskCmd(String taskId,String userId){
		this.taskId=taskId;
		this.userId=userId;
	}

	public Boolean execute(Environment environment) throws Exception {
		
		DbSession session=environment.get(DbSession.class);
		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
		if(fi==null){
			throw new WFException("不存在id为:"+taskId+"的任务！");
		}
		Date now=new Date();
		fi.setTaskStatus(ProcessConstants.TaskStatus.HANDLING);
		fi.setCurrentUserId(userId);
		fi.setAcceptDate(now);
		
		session.update(fi);
		
		//TODO
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(now);
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 领取任务:"+taskId);
		action.setActionName("领取");
		action.setActionType(ProcessConstants.TaskActionType.TAKE_TASK);
		action.setInstanceId(fi.getInstanceId());
		action.setType(ProcessConstants.RecordObjectType.TASK);
		
		session.save(action);
		
		return true;
	}
}
