package com.iscreate.plat.networkresource.structure.template.execution;


import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.Query;
import com.iscreate.plat.networkresource.engine.figure.execution.Execution;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

public class SaveOrUpdateStructureModule implements Execution<Void> {

	private StructureModule module;
	private String structureModuleName;
	private String versionDate = "";
	private ContextFactory contextFactory;

	public SaveOrUpdateStructureModule(StructureModule module) {
		this.module = module;
		structureModuleName = module.getStructureModuleName();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
		versionDate = format.format(new Date());
	}

	public Void doExecution(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
		saveOrUpdateStructureModule();
		return null;
	}

	/**
	 * 保存或者更新结构模板本身
	 */
	private void saveOrUpdateStructureModule() {
		// 查找是否有相同的模板
		Context context = contextFactory.CreateContext();
		Query query = context.createQueryBuilder(StructureModule.MY_TYPE);
		/*query.add(Restrictions.eq(StructureModule.DOMAIN_KEY,
				module.getValue(StructureModule.DOMAIN_KEY)));
		query.add(Restrictions.eq(StructureModule.STRUCTURE_MODULE_NAME_KEY,
				module.getStructureModuleName()));*/
		BasicEntity be = null;//context.queryEntity(query);
		if (be != null) {
			Entity e = (Entity) be;
			e.setValue(StructureModule.STRUCTURE_MODULE_NAME_KEY,
					structureModuleName + "-" + versionDate);
			//context.update(be, query);
		}
	//	context.insert(module);
		replaceStructureModuleEntity(module);
		replaceStructureModuleEntityGroup(module);
		replaceStructureModuleNode(module);
		replaceStructureModuleView();
	}

	/**
	 * 保存或者更新模板中的应用数据对象引用
	 */
	private void replaceStructureModuleEntity(StructureModule module) {
		Context context = contextFactory.CreateContext();
		Map<String, StructureModuleEntity> es = module.getModuleEntitys();
		for (StructureModuleEntity e : es.values()) {
			e.setStructureModuleId(module.getValue(DefaultParam.idKey) + "");
			//context.insert(e);
		}
	}

	/**
	 * 保存或者更新应用数据对象组引用
	 */
	private void replaceStructureModuleEntityGroup(StructureModule module) {
		Context context = contextFactory.CreateContext();
		Map<String, StructureModuleEntityGroup> groups = module
				.getModuleEntityGroups();
		for (StructureModuleEntityGroup group : groups.values()) {
			group.setStructureModuleId(module.getValue(DefaultParam.idKey) + "");
			//context.insert(group);
		}
	}

	/**
	 * 保存或者更新应用数据对象节点
	 */
	private void replaceStructureModuleNode(StructureModule module) {
		Context context = contextFactory.CreateContext();
		// 增加模板
		Collection<StructureModuleNode> nodes = module.getModuleNodes();
		for (StructureModuleNode node : nodes) {
			node.setStructureModuleId(module.getValue(DefaultParam.idKey) + "");
			//context.insert(node);
		}
	}

	private void replaceStructureModuleView() {
		Collection<StructureModule> views = module.getModuleViews().values();
		for (StructureModule view : views) {
			// 更新VIEW接口
			Context context = contextFactory.CreateContext();
			/*Query query = context.createQueryBuilder(StructureModule.MY_TYPE);
			query.add(Restrictions.eq(StructureModule.DOMAIN_KEY,
					module.getValue(StructureModule.DOMAIN_KEY)));
			query.add(Restrictions.eq(
					StructureModule.STRUCTURE_PARENT_NAME_KEY,
					structureModuleName));
			query.add(Restrictions.eq(
					StructureModule.STRUCTURE_MODULE_NAME_KEY,
					view.getStructureModuleName()));*/
			BasicEntity be = null;//context.queryEntity(query);
			if (be != null) {
				Entity e = (Entity) be;
				e.setValue(StructureModule.STRUCTURE_MODULE_NAME_KEY,
						view.getStructureModuleName() + "-" + versionDate);
				e.setValue(StructureModule.STRUCTURE_PARENT_NAME_KEY,
						structureModuleName + "-" + versionDate);
				//context.update(be, query);
			}
			//context.insert(view);
			replaceStructureModuleEntity(view);
			replaceStructureModuleEntityGroup(view);
			replaceStructureModuleNode(view);
		}
	}
}
