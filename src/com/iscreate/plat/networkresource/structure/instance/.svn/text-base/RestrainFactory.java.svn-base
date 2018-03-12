package com.iscreate.plat.networkresource.structure.instance;


import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;

class RestrainFactory {

	public static StructureRestraint getAetNameRestrain(String aetName,
			StructureModule structureModule) {
		Restrain_AppTemplateName res = new Restrain_AppTemplateName(null);
		res.set(aetName, structureModule);
		return res;
	}

	public static StructureRestraint getAetNameRestrain(
			StructureModule structureModule, String... aetName) {
		Restrain_AppTemplateName res = null;
		Restrain_AppTemplateName nextRes = null;
		for (String aet : aetName) {
			nextRes = new Restrain_AppTemplateName(res);
			nextRes.set(aet, structureModule);
			res = nextRes;
		}
		return res;
	}
 
	public static StructureRestraint getAetNameRestrain(
			ApplicationEntity begin, StructureModule structureModule) {
		Restrain_AppTemplate res = new Restrain_AppTemplate(null);
		res.set(begin, structureModule);
		return res;
	}

	public static StructureRestraint getAetAssociationRestrain(
			ApplicationEntity begin, String desAetName, AssociatedType type,
			Structure structure) {
		Restrain_AppTemplate leftAetRes = new Restrain_AppTemplate(null);
		leftAetRes.set(begin, structure.getStructureModule());
		Restrain_AppTemplateName rightAetRes = new Restrain_AppTemplateName(
				leftAetRes);
		rightAetRes.set(desAetName, structure.getStructureModule());
		Restrain_Association associate = new Restrain_Association(rightAetRes);
		associate.set(begin, desAetName, type, structure);
		return associate;
	}

	public static StructureRestraint getAetAssociationRestrain(
			ApplicationEntity begin, ApplicationEntity end,
			AssociatedType type, Structure structure) {
		Restrain_AppTemplate leftAetRes = new Restrain_AppTemplate(null);
		leftAetRes.set(begin, structure.getStructureModule());
		Restrain_AppTemplate rightAetRes = new Restrain_AppTemplate(leftAetRes);
		rightAetRes.set(end, structure.getStructureModule());
		Restrain_Association associate = new Restrain_Association(rightAetRes);
		associate.set(begin, end, type, structure);
		return associate;
	}

}
