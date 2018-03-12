package com.iscreate.plat.networkresource.structure.instance;


import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestrainCode;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;

class Restrain_AppTemplate extends StructureRestraint {

	public Restrain_AppTemplate(StructureRestraint structureRestraint) {
		super(structureRestraint);
	}

	private ApplicationEntity entity;
	private StructureModule structureModule;

	public void set(ApplicationEntity entity, StructureModule structureModule) {
		this.entity = entity;
		this.structureModule = structureModule;
	}

	public StructureConsequence _restrain() {
		Map<String, StructureModuleEntity> es = structureModule
				.getModuleEntitys();
		StructureConsequence consequece;
		if (entity != null && es.keySet().contains(entity.getType())) {
			consequece = new StructureConsequence(
					StructureRestrainCode.SUCCESS_CODE, "success");
		} else {
			consequece = new StructureConsequence(
					StructureRestrainCode.APP_MODULE_NOT_FOUNDED, "'"
							+ (entity == null ? "null" : entity.getType())
							+ "'定义不存在结构'"
							+ structureModule.getStructureModuleName() + "'");
		}
		return consequece;
	}

}
