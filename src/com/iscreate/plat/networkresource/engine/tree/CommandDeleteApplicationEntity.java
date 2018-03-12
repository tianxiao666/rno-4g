package com.iscreate.plat.networkresource.engine.tree;
import java.util.HashMap;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;



public class CommandDeleteApplicationEntity extends Command {

	@Override
	public void execute() {
		if (entity == null || !(entity instanceof ApplicationEntity)) {
			return;
		}
		ApplicationEntity e = (ApplicationEntity) entity;
		String type = e.getType();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put(DefaultParam.idKey, e.getId());
		context.deleteApplicationEntity(type, condition);
	}

}
