package com.iscreate.plat.networkresource.engine.tree;

import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;

/**
 * 保存应用数据对象的命令。保存前先判断应用数据对象是否存在，不存在保存，存在更新。
 * 
 * @author joe
 * 
 */
public class CommandSaveApplicationEntity extends Command {
	@Override
	public void execute() {
		if (entity == null || !(entity instanceof ApplicationEntity)) {
			return;
		}
		ApplicationEntity e = (ApplicationEntity) this.entity;
		// 查找是否存在已经包含了该应用对象的节点对象
		Map<String, Object> condition = new HashMap<String, Object>();
		for (String key : e.primaryKeys()) {
			Object value = e.getValue(key);
			if (value != null)
				condition.put(key, value);
		}
		String type = e.getValue(DefaultParam.typeKey);
		log.debug("查询条件:" + condition);
		ApplicationEntity ae = context.getOneApplicationEntity(type, condition);
		if (ae == null) {
			// 如果不存在该节点对象，将当前节点对象保存
			log.debug("查询结果为空。新增该数据对象。");
			context.saveApplicationEntity(e);
		} else {
			// 如果已经存在该节点对象，替换当前节点对象的nodeId
			e.setId(ae.getId());
			log.debug("查询结果不为空，更新原对象，ID更改为：" + ae.getId() + "。");
			context.updateApplicationEntity(e);
		}

	}

}
