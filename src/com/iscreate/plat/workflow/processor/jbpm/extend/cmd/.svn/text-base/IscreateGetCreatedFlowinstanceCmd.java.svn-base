package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;

import com.iscreate.plat.workflow.processor.constants.ProcessConstants;

public class IscreateGetCreatedFlowinstanceCmd implements Command<List> {

	private String starterType;
	private String starterId;
	private List<String> status;
	
	public IscreateGetCreatedFlowinstanceCmd(String starterType,String starterId,List<String> status){
		this.starterType=starterType;
		this.starterId=starterId;
		this.status=status;
	}
	

	public List execute(Environment environment) throws Exception {
		
		String statuss="";
		if(status!=null && status.size()>0){
			for(String s:status){
				statuss+="'" +s+"',";
			}
			statuss=statuss.substring(0,statuss.length()-1);
		}
		statuss="( "+statuss+" )";
		
		Session session=environment.get(Session.class);
		Query query=null;
		if(ProcessConstants.StarterType.STARTER_PEOPLE.equals(starterType)){
			query=session.createQuery("from FlowInstanceInfo as ins where ins.create_user_id=? and ins.instance_status in "+statuss);
		}else if(ProcessConstants.StarterType.STARTER_PROCESS.equals(starterType)){
			query=session.createQuery("from FlowInstanceInfo as ins where ins.owner_instance_id=? and ins.instance_status in "+statuss);
		}else if(ProcessConstants.StarterType.STARTER_TASK.equals(starterType)){
			query=session.createQuery("from FlowInstanceInfo as ins where ins.owner_task_id=? and ins.instance_status in "+statuss);
		}
		query.setString(0,starterId);
		
		return query.list();
	}

}
