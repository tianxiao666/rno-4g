package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import org.jbpm.api.Deployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.DeployCmd;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.processengine.ProcessEngineImpl;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.session.RepositorySession;

import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.dataconfig.FlowInfo;

public class IscreateDeployCmd extends DeployCmd  {

	FlowInfo iscreateFlowDefinition;
	public IscreateDeployCmd(DeploymentImpl deployment) {
		super(deployment);
	}
	
	public IscreateDeployCmd(DeploymentImpl deployment,FlowInfo iscreateFlowDefinition) {
		super(deployment);
		this.iscreateFlowDefinition=iscreateFlowDefinition;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute(Environment environment) throws Exception {
		if(iscreateFlowDefinition==null){
			throw new WFException("流程定义对象为空！");
		}
		
		String deploymentId= super.execute(environment);
//		System.out.println("inner  definitionId = "+definitionId);
//		System.out.println("=============2=========");
		
		RepositorySession repositorySession = EnvironmentImpl.getFromCurrent(RepositorySession.class);

		ProcessDefinition existingProcesses = repositorySession.createProcessDefinitionQuery()
        .deploymentId(deploymentId).uniqueResult();
	    
//		for(ProcessDefinition pd:existingProcesses){
//			System.out.println("id= "+pd.getId()+",deploymentId= "+pd.getDeploymentId()+",key="+pd.getKey()+",name= "+pd.getName());
//		}
		//TODO
		//这里处理自定义的流程定义信息
		iscreateFlowDefinition.setDeployment_id(deploymentId);
		iscreateFlowDefinition.setFlow_id(existingProcesses.getId());
		ProcessEngineImpl pg=environment.get(ProcessEngineImpl.class);
		
		iscreateFlowDefinition.setFlow_name(existingProcesses.getName());
		Deployment dp = EnvironmentImpl.getFromCurrent(RepositoryService.class).createDeploymentQuery().deploymentId(deploymentId).uniqueResult();
		System.out.println(dp.getState());
		iscreateFlowDefinition.setFlow_status(dp.getState());
		DbSession dbSession = environment.get(DbSession.class); 
		dbSession.save(iscreateFlowDefinition);
//		System.out.println("deploy iscreatedefinition info:"+iscreateFlowDefinition.toString());
		return deploymentId;
	}
	
}
