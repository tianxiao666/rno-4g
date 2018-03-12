package com.iscreate.plat.networkresource.structure.template;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;

public class StructureModuleEntityGroup extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6967337999772735486L;
	// 对象的模板名称
	public final static String MY_TYPE = StructureModuleEntityGroup.class
			.getSimpleName();
	// 存储结构模板的属性名称
	public final static String STRUCTURE_MODULE_ID_KEY = "structureModuleId";
	// 组的属性名称
	public final static String STRUCTURE_AETGROUP_KEY = "aetgName";

	public final static String AETS_KEY = "aets";

	private List<String> aets = new ArrayList<String>();

	public StructureModuleEntityGroup() {
		set(DefaultParam.typeKey, MY_TYPE);
	}

	public String getAetgname() {
		return getValue(STRUCTURE_AETGROUP_KEY) + "";
	}

	public void setAetgname(String aetgname) {
		set(STRUCTURE_AETGROUP_KEY, aetgname);
	}

	public List<String> getAets() {
		if (aets.isEmpty()) {
			String aetVal = getValue(AETS_KEY);
			if (aetVal != null) {
				for (String aet : aetVal.split(",")) {
					aets.add(aet);
				}
			}
		}
		return aets;
	}

	public void addAet(String aet) {
		if (aet != null && !aet.trim().isEmpty()) {
			this.aets.add(aet);
			myAet();
		}
	}

	public void removeAet(String aet) {
		this.aets.remove(aet);
		myAet();
	}

	public String getStructureModuleId() {
		return this.getValue(STRUCTURE_MODULE_ID_KEY) + "";
	}

	public void setStructureModuleId(String StructureModuleId) {
		this.set(STRUCTURE_MODULE_ID_KEY, StructureModuleId);
	}

	public static StructureModuleEntityGroup changeFromEntity(BasicEntity be) {
		StructureModuleEntityGroup s = new StructureModuleEntityGroup();
		for (String key : be.keyset()) {
			s.set(key, be.getValue(key));
		}
		return s;
	}

	public String toString() {
		String result = getAetgname() + "-" + getAets();
		return result;
	}

	private void myAet() {
		StringBuilder builder = new StringBuilder();
		for (String aet : aets) {
			builder.append(aet);
			builder.append(",");
		}
		String aet = builder.toString();
		if (aet.length() > 0) {
			aet = aet.substring(0, aet.length() - 1);
		}
		set(AETS_KEY, aet);
	}
}
