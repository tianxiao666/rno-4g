package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateRejectTaskCmd implements Command<Boolean> {

	private String taskId;
	private String userId;
	public IscreateRejectTaskCmd(){
		
	}
	public IscreateRejectTaskCmd(String taskId,String userId){
		this.taskId=taskId;
		this.userId=userId;
	}

	public Boolean execute(Environment environment) throws Exception {
		
		DbSession session=environment.get(DbSession.class);
		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
		if(fi==null){
			throw new WFException("不存在id为:"+taskId+"的任务！");
		}
		fi.setTaskStatus(ProcessConstants.TaskStatus.SUSPENDED);
		
		session.update(fi);
		
		//TODO
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(new Date());
		action.setCreateUserId(userId);
		action.setActionContent(userId+" 驳回任务:"+taskId);
		action.setActionName("驳回");
		action.setActionType(ProcessConstants.TaskActionType.REJECT_TASK);
		action.setInstanceId(fi.getInstanceId());
		action.setType(ProcessConstants.RecordObjectType.TASK);
		
		session.save(action);
		
		//发送通知给创建者知道
		
		return true;
	}

}
