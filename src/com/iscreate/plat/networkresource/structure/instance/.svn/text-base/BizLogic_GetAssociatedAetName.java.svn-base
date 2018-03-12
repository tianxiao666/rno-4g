package com.iscreate.plat.networkresource.structure.instance;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

class BizLogic_GetAssociatedAetName implements StructureBizLogic<String[]> {

	private String aetName;
	private AssociatedType type;
	private StructureModule module;

	public BizLogic_GetAssociatedAetName(String aetName, AssociatedType type) {
		this.aetName = aetName;
		this.type = type;
	}

	/**
	 * 获取关联的Aet名称
	 */
	public String[] bizLogic(LogicEnvironment logicEnv) {
		// 获取结构模板对象
		module = logicEnv.get(StructureModule.class);
		// 如果传入的是一个组名，获取该组名的关联对象的名称
		List<String> nextAet = new ArrayList<String>();
		if (module.getModuleEntityGroups().containsKey(aetName)) {
			String[] myAets = new BizLogic_GetAetNameOfAetg(aetName)
					.bizLogic(logicEnv);
			for(String myAet: myAets){
				getAssociateAet(myAet, nextAet);
			}
		}else{
			getAssociateAet(aetName, nextAet);
		}
		String[] result = new String[nextAet.size()];
		nextAet.toArray(result);
		return result;
	}

	private void getAssociateAet(String name, List<String> nextAet) {
		List<StructureModuleNode> nodes = module.getModuleNodes();
		// 组信息
		Set<String> groupnames = module.getModuleEntityGroups().keySet();
		// 定义关联的Aet列表
		for (StructureModuleNode node : nodes) {
			AssociatedType mytype = node.getAssociatedType();
			String leftentity = node.getLeftEntity();
			String rightentity = node.getRightEntity();
			if ((groupnames.contains(leftentity))
					|| (groupnames.contains(rightentity))) {
				continue;
			}
			// 所查询的状态枚举
			switch (type) {
			case CLAN:
				if (mytype == AssociatedType.CLAN) {
					if (leftentity.equals(name)) {
						nextAet.add(rightentity);
					} else if (rightentity.equals(name)) {
						nextAet.add(leftentity);
					}
				}
				break;
			case CHILD:
				if (mytype == AssociatedType.CLAN) {
					if (leftentity.equals(name)) {
						nextAet.add(rightentity);
					}
				}
				break;
			case PARENT:
				if (mytype == AssociatedType.CLAN) {
					if (rightentity.equals(name)) {
						nextAet.add(leftentity);
					}
				}
				break;
			case LINK:
				if (mytype == AssociatedType.LINK) {
					if (leftentity.equals(name)) {
						nextAet.add(rightentity);
					} else if (rightentity.equals(name)) {
						nextAet.add(leftentity);
					}
				}
				break;
			case BACKWORD:
				if (mytype == AssociatedType.LINK) {
					if (rightentity.equals(name)) {
						nextAet.add(leftentity);
					}
				}
				break;
			case FORWORD:
				if (mytype == AssociatedType.LINK) {
					if (leftentity.equals(name)) {
						nextAet.add(rightentity);
					}
				}
				break;
			default:
				if (leftentity.equals(name)) {
					nextAet.add(rightentity);
				} else if (rightentity.equals(name)) {
					nextAet.add(leftentity);
				}
				break;
			}
		}
	}
}
