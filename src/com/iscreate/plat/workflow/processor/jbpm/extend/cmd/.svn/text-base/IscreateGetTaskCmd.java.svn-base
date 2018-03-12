package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.datainput.FlowTaskInfo;

public class IscreateGetTaskCmd implements Command<FlowTaskInfo> {

	private String taskId;
	public IscreateGetTaskCmd(){
		
	}
	
	public IscreateGetTaskCmd(String taskId){
		this.taskId=taskId;
	}
	
	public FlowTaskInfo execute(Environment environment) throws Exception {
		
		DbSession session = environment.get(DbSession.class);
		return session.get(FlowTaskInfo.class, taskId);
	}

}
