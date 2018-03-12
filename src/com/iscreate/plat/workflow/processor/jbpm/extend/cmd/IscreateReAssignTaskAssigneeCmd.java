package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.Date;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskActionRecord;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateReAssignTaskAssigneeCmd implements Command<Boolean> {

	private String taskId;
	private String oldUserId;
	private String newUserId;
	
	public IscreateReAssignTaskAssigneeCmd(String taskId,String oldUserId,String newUserId){
		this.taskId=taskId;
		this.oldUserId=oldUserId;
		this.newUserId=newUserId;
	}
	
	
	public Boolean execute(Environment environment) throws Exception {
		DbSession session=environment.get(DbSession.class);
		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
		if(fi==null){
			throw new WFException("不存在id为:"+taskId+"的任务！");
		}
		
		
		//任务状态:只有在处理中和暂停中可以重新分配
		if(!ProcessConstants.TaskStatus.HANDLING.equals(fi.getTaskStatus())
				&& !ProcessConstants.TaskStatus.SUSPENDED.equals(fi.getTaskStatus())){
			throw new WFException("任务:"+fi.getTaskName()+"当前状态为："+fi.getTaskStatus()+" 不可以重新指派处理人！");
		}
		//是否是当前处理人执行这个操作
		if(!oldUserId.equals(fi.getCurrentUserId())){
			throw new WFException("任务:"+fi.getTaskName()+"当前处理人不是："+oldUserId+" ,所以"+oldUserId+"不可以执行该任务重新指派处理人的操作！");
		}
		
		//重新指派
		fi.setCurrentUserId(newUserId);
		session.update(fi);
		
		//加操作记录
		FlowTaskActionRecord action=new FlowTaskActionRecord();
		action.setTaskId(taskId);
		action.setCreateTime(new Date());
		action.setCreateUserId(oldUserId);
		action.setActionContent(oldUserId+" 将任务:"+taskId+"["+fi.getTaskName()+"] 转给:"+newUserId);
		action.setActionName("转派");
		action.setActionType(ProcessConstants.TaskActionType.REASSIGEN);
		action.setInstanceId(fi.getInstanceId());
		action.setType(ProcessConstants.RecordObjectType.TASK);
		session.save(action);
		
		return true;
		
	}

}
