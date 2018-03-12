package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.lang.reflect.Field;

import org.jbpm.pvm.internal.cmd.CommandService;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.jbpm.pvm.internal.util.ReflectUtil;

import com.iscreate.plat.workflow.dataconfig.FlowInfo;
import com.iscreate.plat.workflow.datainput.IscreateFlowDefinition;

public class IscreateDeploymentImpl extends DeploymentImpl {

	private FlowInfo iscreateFlowDefinition;

	public FlowInfo getIscreateFlowDefinition() {
		return iscreateFlowDefinition;
	}

	public void setIscreateFlowDefinition(
			FlowInfo iscreateFlowDefinition) {
		this.iscreateFlowDefinition = iscreateFlowDefinition;
	}

	public IscreateDeploymentImpl() {

	}

	public IscreateDeploymentImpl(CommandService commandService) {
		this.commandService = commandService;
	}

	/**
	 * 覆盖父类的方法
	 */
	public String deploy() {
		return commandService.execute(new IscreateDeployCmd(this.getParent(),
				this.iscreateFlowDefinition));
	}

	public DeploymentImpl getParent() {

		DeploymentImpl parent = new DeploymentImpl();

//		protected long dbid;
//		  protected String name;
//		  protected long timestamp;
//		  protected String state = Deployment.STATE_ACTIVE;
//		  protected Map<String, Lob> resources;
//
//		  protected CommandService commandService;
//		  protected Map<String, Object> objects;
//		  protected Set<DeploymentProperty> objectProperties;
		  
//		parent.setDbid(this.getDbid());
//		parent.setName(this.getName());
//		parent.setTimestamp(this.getTimestamp());
//		
//		Field field=ReflectUtil.findField(parent.getClass(),"resources");
//		Field field2=ReflectUtil.findField(this.getClass(), "resources");
//		ReflectUtil.set(field, parent, ReflectUtil.get(field2, this));
		
		String[] fieldNames=new String[]{"dbid","name","timestamp","resources","commandService","objects","objectProperties"};
		
//		Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
//		if (fields != null) {
			for (String fieldName:fieldNames) {
				Field field =ReflectUtil.findField(parent.getClass(), fieldName);
				Field field2 = ReflectUtil
						.findField(this.getClass(), fieldName);
				if (field2 != null) {
//					 boolean accessFlag = field2.isAccessible();
					 
		                // 修改访问控制权限
					 field2.setAccessible(true);

					Object value=ReflectUtil
					.get(field2, this);
					ReflectUtil.set(field, parent,value );
//					System.out.println("fieldName = "+fieldName+",value = "+(value==null?"null":value.toString())+",accessFlag = "+accessFlag);
				}
			}
//		}

		return parent;
	}
}
