package com.iscreate.plat.networkresource.structure.template;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

class StructureModuleBuilder {

	public void increaseEntity(StructureModule module,
			StructureModuleEntity entity) {
		if (entity == null || entity.getAet() == null
				|| entity.getAet().isEmpty()) {
			return;
		}
		Map<String, StructureModuleEntity> moduleEntitys = module.moduleEntitys;
		if (!moduleEntitys.containsKey(entity.getAet())) {
			moduleEntitys.put(entity.getAet(), entity);
		}
	}

	public void decreaseEntity(StructureModule module,
			StructureModuleEntity entity) {
		if (entity == null || entity.getAet().isEmpty()) {
			return;
		}
		module.moduleEntitys.remove(entity.getAet());
	}

	public void increaseEntityGroup(StructureModule module,
			StructureModuleEntityGroup entityGroup) {
		if (entityGroup == null || entityGroup.getAetgname().isEmpty()) {
			return;
		}
		Map<String, StructureModuleEntityGroup> moduleEntityGroups = module.moduleEntityGroups;
		moduleEntityGroups.put(entityGroup.getAetgname(), entityGroup);
	}

	public void decreaseEntityGroup(StructureModule module,
			StructureModuleEntityGroup entityGroup) {
		if (entityGroup == null || entityGroup.getAetgname().isEmpty()) {
			return;
		}
		module.moduleEntityGroups.remove(entityGroup.getAetgname());
	}

	/**
	 * 为结构模板增加一个关联节点
	 * 
	 * @param module
	 * @param node
	 */
	public void increaseRelation(StructureModule module,
			StructureModuleNode node) {
		if (node == null || node.getLeftEntity().isEmpty()
				|| node.getRightEntity().isEmpty()) {
			return;
		}
		boolean typesame = false, entitysame = false;
		for (StructureModuleNode mn : module.moduleNodes) {
			typesame = false;
			entitysame = false;
			if (mn.getAssociatedType().equals(node.getAssociatedType())) {
				typesame = true;
			}
			if (mn.getLeftEntity().equals(node.getLeftEntity())
					&& mn.getRightEntity().equals(node.getRightEntity())) {
				entitysame = true;
			} else if (mn.getRightEntity().equals(node.getLeftEntity())
					&& mn.getLeftEntity().equals(node.getRightEntity())) {
				entitysame = true;
			}
			if (typesame && entitysame) {
				return;
			}
		}
		// 在模板中加入节点的引用
		module.moduleNodes.add(node);
		// 翻译数据节点
		translateAetgToAet(module, node);
	}

	public void decreaseRelation(StructureModule module,
			StructureModuleNode node) {
		if (node == null || node.getLeftEntity().isEmpty()
				|| node.getRightEntity().isEmpty()) {
			return;
		}
		boolean typesame = false, entitysame = false;
		StructureModuleNode exist = null;
		for (StructureModuleNode mn : module.moduleNodes) {
			typesame = false;
			entitysame = false;
			if (mn.getAssociatedType().equals(node.getAssociatedType())) {
				typesame = true;
			}
			if (mn.getLeftEntity().equals(node.getLeftEntity())
					&& mn.getRightEntity().equals(node.getRightEntity())) {
				entitysame = true;
			} else if (mn.getRightEntity().equals(node.getLeftEntity())
					&& mn.getLeftEntity().equals(node.getRightEntity())) {
				entitysame = true;
			}
			if (typesame && entitysame) {
				exist = mn;
				break;
			}
		}
		if (exist != null) {
			module.moduleNodes.remove(exist);
		}
	}

	/**
	 * 增加视图的引用
	 * 
	 * @param module
	 * @param view
	 */
	public void increaseView(StructureModule module, StructureModule view) {
		view.asView(module.getStructureModuleName());
		String viewkey = view.getStructureModuleName();
		module.views.put(viewkey, view);
	}

	public void decreaseView(StructureModule module, StructureModule view) {
		String viewkey = view.getStructureModuleName();
		module.views.remove(viewkey);
	}

	/**
	 * 如果关系节点中包含有aetg，将aetg名转换成aet名称
	 * 
	 * @param module
	 * @param existNode
	 */
	private void translateAetgToAet(StructureModule module,
			StructureModuleNode existNode) {
		String leftkey = existNode.getLeftEntity();
		String rightkey = existNode.getRightEntity();
		boolean hasleft = module.moduleEntityGroups.containsKey(leftkey);
		boolean hasright = module.moduleEntityGroups.containsKey(rightkey);
		if (hasleft && hasright) {
			List<String> leftaets = module.moduleEntityGroups.get(leftkey)
					.getAets();
			List<String> rightaets = module.moduleEntityGroups.get(rightkey)
					.getAets();
			for (String leftaet : leftaets) {
				for (String rightaet : rightaets) {
					StructureModuleNode newnode = StructureModuleNode
							.changeFromEntity((BasicEntity) existNode.clone());
					newnode.setLeftEntity(leftaet);
					newnode.setRightEntity(rightaet);
					increaseRelation(module, newnode);
				}
			}
		} else {
			if (hasleft) {
				List<String> aets = module.moduleEntityGroups.get(leftkey)
						.getAets();
				for (String aet : aets) {
					StructureModuleNode newnode = StructureModuleNode
							.changeFromEntity((BasicEntity) existNode.clone());
					newnode.setLeftEntity(aet);
					increaseRelation(module, newnode);
				}
			}
			if (hasright) {
				List<String> aets = module.moduleEntityGroups.get(rightkey)
						.getAets();
				for (String aet : aets) {
					StructureModuleNode newnode = StructureModuleNode
							.changeFromEntity((BasicEntity) existNode.clone());
					newnode.setRightEntity(aet);
					increaseRelation(module, newnode);
				}
			}
		}
	}
}
