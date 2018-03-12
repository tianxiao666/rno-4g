package com.iscreate.plat.networkresource.engine.tree;

import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;

/**
 * 保存节点的命令。保存前先判断节点是否存在，不存在保存，存在更新。
 * 
 * @author joe
 * 
 */
public class CommandSaveNode extends Command {

	public void execute() {
		if (entity == null || !(entity instanceof Treenode)) {
			return;
		}
		Treenode n = (Treenode) this.entity;
		ApplicationEntity e = n.getApplicationEntity();
		if (e == null) {
			return;
		}
		// 查找是否存在已经包含了该应用对象的节点对象
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put(Treenode.eidkey, e.getId());
		condition.put(Treenode.entkey, e.getType());
		condition.put(Treenode.treeidkey, n.getValue(Treenode.treeidkey));
		String type = n.getNodeName();
		Treenode t = context.getOneTreenode(type, condition);
		if (t == null) {
			// 如果不存在该节点对象，将当前节点对象保存
			context.saveTreenode(n);
		} else {
			// 如果已经存在该节点对象，替换当前节点对象的nodeId
			n.setNodeId(t.getNodeId());
			context.updateTreenode(n);
		}
	}

}
