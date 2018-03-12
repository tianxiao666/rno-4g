package com.iscreate.plat.networkresource.structure.instance;


import java.util.Collection;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestrainCode;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;


class Restrain_Association extends StructureRestraint {

	public Restrain_Association(StructureRestraint structureRestraint) {
		super(structureRestraint);
	}

	private String leftAetName, rightAetName;
	// 关联类型
	private AssociatedType type;
	// 结构模板的引用
	private StructureModule structureModule;
	// 左节点AE
	private ApplicationEntity begin;
	// 结构实例的引用
	private Structure structure;

	public Restrain_Association set(ApplicationEntity begin, String desAetName,
			AssociatedType type, Structure structure) {
		this.begin = begin;
		this.leftAetName = begin.getType();
		this.rightAetName = desAetName;
		this.type = type;
		this.structure = structure;
		this.structureModule = structure.getStructureModule();
		return this;
	}

	public Restrain_Association set(ApplicationEntity begin,
			ApplicationEntity end, AssociatedType type, Structure structure) {
		this.begin = begin;
		this.leftAetName = begin.getType();
		this.rightAetName = end.getType();
		this.type = type;
		this.structure = structure;
		this.structureModule = structure.getStructureModule();
		return this;
	}

	@Override
	protected StructureConsequence _restrain() {
		Collection<StructureModuleNode> nodes = structureModule
				.getModuleNodes();
		StructureModuleNode node = null;
		// 遍历结构模板中所有的关系节点，查找关联定义节点
		for (StructureModuleNode n : nodes) {
			if (n.getAssociatedType() == type) {
				if (n.getLeftEntity().equals(leftAetName)
						&& n.getRightEntity().equals(rightAetName)) {
					node = n;
					break;
				}
				// 这个判断没有break，可以保证正向的关系节点总是优先
				if (type == AssociatedType.LINK) {
					if (n.getLeftEntity().equals(rightAetName)
							&& n.getRightEntity().equals(leftAetName)) {
						node = n;
					}
				}
			}
		}
		// 如果没有找到该定义节点，则返回 关联未定义的状态码
		if (node == null) {
			return new StructureConsequence(
					StructureRestrainCode.ASSOCIATION_NOT_DEFINE, leftAetName
							+ "与" + rightAetName + "的'" + type + "'关系在结构模板'"
							+ structureModule.getStructureModuleName()
							+ "'中未定义。");
		}

		// 如果ae的关系定义值小于0,直接返回成功状态
		int cardinality = node.getCardinality();
		if (cardinality < 0) {
			return new StructureConsequence(StructureRestrainCode.SUCCESS_CODE,
					"success");
		}
		// 如果ae的关系定义为0，直接返回失败状态
		if (cardinality == 0) {
			return new StructureConsequence(
					StructureRestrainCode.MAX_CARDINALITY, leftAetName + "与"
							+ rightAetName + "的'" + node.getAssociatedType()
							+ "'实例关系已经达到上限");
		}
		String leftAetName = node.getLeftEntity();
		String rightAetName = node.getRightEntity();
		String beginAetName = begin.getType();

		ApplicationEntity[] aes;
		// 正向判断left ae与多个少right ae连接
		if (beginAetName.equals(leftAetName)) {
			if (node.getAssociatedType() == AssociatedType.CLAN) {
				aes = structure.doBizLogic(new Structure_BizLogic_GetAssociatedEntity(
						begin, rightAetName, AssociatedType.CHILD));
			} else {
				aes = structure.doBizLogic(new Structure_BizLogic_GetAssociatedEntity(
						begin, rightAetName, AssociatedType.LINK));
			}
			if (aes != null && aes.length >= cardinality) {
				return new StructureConsequence(
						StructureRestrainCode.MAX_CARDINALITY, leftAetName
								+ "与" + rightAetName + "的'"
								+ node.getAssociatedType() + "'实例关系已经达到上限");
			} else {
				return new StructureConsequence(
						StructureRestrainCode.SUCCESS_CODE, "success");
			}
		}
		// 逆向判断 right ae 与left ae是否连接
		else {
			if (node.getAssociatedType() == AssociatedType.CLAN) {
				aes = structure.doBizLogic(new Structure_BizLogic_GetAssociatedEntity(
						begin, leftAetName, AssociatedType.PARENT));
			} else {
				aes = structure.doBizLogic(new Structure_BizLogic_GetAssociatedEntity(
						begin, leftAetName, AssociatedType.LINK));
			}
			// 只要存在 一个连接，则到达上线
			if (aes != null && aes.length >= 1) {
				return new StructureConsequence(
						StructureRestrainCode.MAX_CARDINALITY, leftAetName
								+ "与" + rightAetName + "的'"
								+ node.getAssociatedType() + "'实例关系已经达到上限");
			} else {
				return new StructureConsequence(
						StructureRestrainCode.SUCCESS_CODE, "success");
			}
		}
	}

}
