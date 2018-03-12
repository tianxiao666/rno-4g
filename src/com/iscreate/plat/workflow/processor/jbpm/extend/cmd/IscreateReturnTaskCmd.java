package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateReturnTaskCmd implements Command<Boolean> {

	private String taskId;
	private String userId;
	public IscreateReturnTaskCmd(){
		
	}
	public IscreateReturnTaskCmd(String taskId,String userId){
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
		fi.setTaskStatus(ProcessConstants.TaskStatus.INIT);
		fi.setCurrentUserId("");
		fi.setAcceptDate(null);
		
		session.update(fi);
		
		//TODO
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(now);
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 退回任务:"+taskId);
		action.setActionName("退回");
		action.setActionType(ProcessConstants.TaskActionType.RETURN_TASK);
		action.setInstanceId(fi.getInstanceId());
		action.setType(ProcessConstants.RecordObjectType.TASK);
		session.save(action);
		
		return true;
	}
}
