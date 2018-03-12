package com.iscreate.plat.networkresource.structure.instance;


import java.util.Map;

import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestrainCode;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;


class Restrain_AppTemplateName extends StructureRestraint {

	public Restrain_AppTemplateName(StructureRestraint structureRestraint) {
		super(structureRestraint);
	}

	private String aetName;
	private StructureModule structureModule;

	public void set(String aetName, StructureModule structureModule) {
		this.aetName = aetName;
		this.structureModule = structureModule;
	}

	protected StructureConsequence _restrain() {
		Map<String, StructureModuleEntity> es = structureModule
				.getModuleEntitys();
		StructureConsequence consequece;
		if (es.keySet().contains(aetName)) {
			consequece = new StructureConsequence(
					StructureRestrainCode.SUCCESS_CODE, "success");
		} else {
			consequece = new StructureConsequence(
					StructureRestrainCode.APP_MODULE_NOT_FOUNDED, "'" + aetName
							+ "'定义不存在结构'"
							+ structureModule.getStructureModuleName() + "'");
		}
		return consequece;
	}

}
