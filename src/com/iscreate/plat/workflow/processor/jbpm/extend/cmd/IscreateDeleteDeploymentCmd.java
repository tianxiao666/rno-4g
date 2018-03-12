package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;

import java.lang.reflect.Field;

import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.cmd.DeleteDeploymentCmd;
import org.jbpm.pvm.internal.session.DbSession;
import org.jbpm.pvm.internal.util.ReflectUtil;

import com.iscreate.plat.workflow.dataconfig.FlowInfo;

public class IscreateDeleteDeploymentCmd extends DeleteDeploymentCmd {

	public IscreateDeleteDeploymentCmd(String deploymentId) {
		super(deploymentId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(Environment environment) throws Exception {
		Object obj = super.execute(environment);

		// TODO
		// 修改自定义信息
		DbSession session = environment.get(DbSession.class);
		Field field = ReflectUtil.findField(this.getClass(), "deploymentId");
		field.setAccessible(true);
		Object value = ReflectUtil.get(field, this);

		FlowInfo fi=session.get(FlowInfo.class, value);
		fi.setFlow_status("cancel");
		session.update(fi);
		// session.get(FlowInfo.class, );

		return obj;

	}
}
