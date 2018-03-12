
package com.iscreate.plat.networkresource.structure.instance;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;

/**
 * 获取entitygroups中的aet名称
 * @author joe
 *
 */
class BizLogic_GetAetNameOfAetg implements StructureBizLogic<String[]> {

	private String aetgName;
	Map<String, StructureModuleEntityGroup> groups;

	public BizLogic_GetAetNameOfAetg(String aetgName) {
		this.aetgName = aetgName;
	}

	/**
	 * 获取aetg下的Aet名称
	 */
	public String[] bizLogic(LogicEnvironment logicEnv) {
		// 获取结构模板对象
		groups = logicEnv.get(StructureModule.class).getModuleEntityGroups();
		List<String> names =new ArrayList<String>();
		getAllAetNames(aetgName, new HashSet<String>(), names);
		String[] aets = new String[names.size()];
		names.toArray(aets);
		return aets;
	}

	private void getAllAetNames(String aetgName, Set<String> cache,
			List<String> aetNames) {
		if (cache.contains(aetgName)) {
			return;
		}
		cache.add(aetgName);
		StructureModuleEntityGroup group = groups.get(aetgName);
		if (group != null) {
			List<String> names = group.getAets();
			for (String name : names) {
				if (groups.containsKey(name)) {
					getAllAetNames(name, cache, aetNames);
				} else {
					aetNames.add(name);
				}
			}
		}
	}
}

