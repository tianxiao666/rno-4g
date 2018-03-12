package com.iscreate.plat.networkresource.engine.tree;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

abstract class Command {
	Log log = LogFactory.getLog(getClass());
	BasicEntity entity;
	Context context;

	public void setExecuteTag(Context context, BasicEntity entity) {
		this.entity = entity;
		this.context = context;
	}

	public abstract void execute();
}
