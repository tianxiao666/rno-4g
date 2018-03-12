package com.iscreate.plat.networkresource.structure.template.execution;

import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.execution.Execution;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

public class QueryStructureModuleByName implements Execution<StructureModule> {

	private long domainId;
	private String structureModuleName;
	private ContextFactory contextFactory;

	public QueryStructureModuleByName(long domainId, String structureModuleName) {
		this.domainId = domainId;
		this.structureModuleName = structureModuleName;
	}

	public StructureModule doExecution(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
		StructureModule module = loadStructureModule();
		return module;
	}

	private StructureModule loadStructureModule() {
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(StructureModule.MY_TYPE);
		//query.add(Restrictions.eq(StructureModule.DOMAIN_KEY, domainId));
		//query.add(Restrictions.eq(StructureModule.STRUCTURE_MODULE_NAME_KEY,
			//	structureModuleName));
		BasicEntity be =null;// context.queryEntity(query);
		if (be != null) {
			StructureModule module = StructureModule.changeFromEntity(be);
			loadStructureModuleEntity(module);
			loadStructureModuleEntityGroup(module);
			loadStructureModuleNode(module);
			// 加载视图
			loadStructureModuleView(module);
			return module;
		} else {
			return null;
		}
	}

	private void loadStructureModuleEntity(StructureModule module) {
		String moduleId = module.getValue(DefaultParam.idKey);
		Context context = contextFactory.CreateContext();
		/*Query query = context.createQueryBuilder(StructureModuleEntity.MY_TYPE);
		query.add(Restrictions.eq(
				StructureModuleEntity.STRUCTURE_MODULE_ID_KEY, moduleId));*/
		List<BasicEntity> bes = null;//context.queryList(query);
		if (bes != null) {
			for (BasicEntity be : bes) {
				StructureModuleEntity s = StructureModuleEntity
						.changeFromEntity(be);
				module.increaseEntity(s);
			}
		}
	}

	private void loadStructureModuleEntityGroup(StructureModule module) {
		String moduleId = module.getValue(DefaultParam.idKey);
		Context context = contextFactory.CreateContext();
		/*Query query = context
				.createQueryBuilder(StructureModuleEntityGroup.MY_TYPE);
		query.add(Restrictions.eq(
				StructureModuleEntityGroup.STRUCTURE_MODULE_ID_KEY, moduleId));*/
		List<BasicEntity> bes = null;//context.queryList(query);
		if (bes != null) {
			for (BasicEntity be : bes) {
				StructureModuleEntityGroup s = StructureModuleEntityGroup
						.changeFromEntity(be);
				module.increaseEntityGroup(s);
			}
		}
	}

	private void loadStructureModuleNode(StructureModule module) {
		String moduleId = module.getValue(DefaultParam.idKey);
		Context context = contextFactory.CreateContext();
		/*Query query = context.createQueryBuilder(StructureModuleNode.MY_TYPE);
		query.add(Restrictions.eq(StructureModuleNode.STRUCTURE_MODULE_ID_KEY,
				moduleId));*/
		List<BasicEntity> bes = null;//context.queryList(query);
		if (bes != null) {
			for (BasicEntity be : bes) {
				StructureModuleNode s = StructureModuleNode
						.changeFromEntity(be);
				module.increaseRelation(s);
			}
		}
	}

	private void loadStructureModuleView(StructureModule module) {
		Context context = contextFactory.CreateContext();
		/*Query query = context.createQueryBuilder(StructureModule.MY_TYPE);
		query.add(Restrictions.eq(StructureModule.DOMAIN_KEY, domainId));
		query.add(Restrictions.eq(StructureModule.STRUCTURE_PARENT_NAME_KEY,
				module.getStructureModuleName()));*/
		List<BasicEntity> bes = null;//context.queryList(query);
		for (BasicEntity be : bes) {
			StructureModule view = StructureModule.changeFromEntity(be);
			loadStructureModuleEntity(view);
			loadStructureModuleEntityGroup(view);
			loadStructureModuleNode(view);
			module.increaseView(view);
		}
	}
}
