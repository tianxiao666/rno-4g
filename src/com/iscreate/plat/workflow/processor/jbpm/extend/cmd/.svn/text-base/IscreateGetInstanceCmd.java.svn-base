package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.session.DbSession;

import com.iscreate.plat.workflow.datainput.FlowInstanceInfo;

public class IscreateGetInstanceCmd implements Command<FlowInstanceInfo> {

	private String instanceId;
	
	public IscreateGetInstanceCmd(){
		
	}
	public IscreateGetInstanceCmd(String instanceId){
		this.instanceId=instanceId;
	}
	
	public FlowInstanceInfo execute(Environment environment) throws Exception {
		DbSession session = environment.get(DbSession.class);
		return session.get(FlowInstanceInfo.class, instanceId);
	}

}
