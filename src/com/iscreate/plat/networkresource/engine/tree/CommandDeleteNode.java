package com.iscreate.plat.networkresource.engine.tree;

import java.util.HashMap;

public class CommandDeleteNode extends Command {

	@Override
	public void execute() {
		if (entity == null || !(entity instanceof Treenode)) {
			return;
		}
		Treenode node = (Treenode) entity;
		String type = node.getNodeName();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put(Treenode.nidkey, node.getNodeId());
		context.deleteTreenode(type, condition);
	}

}
