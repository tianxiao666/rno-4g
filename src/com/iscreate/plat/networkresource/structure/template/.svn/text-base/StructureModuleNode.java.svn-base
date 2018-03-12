package com.iscreate.plat.networkresource.structure.template;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;

public class StructureModuleNode extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2355862201777898685L;
	// 对象的模板名称
	public final static String MY_TYPE = StructureModuleNode.class
			.getSimpleName();
	// 存储结构模板的属性名称
	public final static String STRUCTURE_MODULE_ID_KEY = "structureModuleId";
	// 左对象模板的属性KEY
	public final static String LEFT_ENTITY_KEY = "leftEntity";
	// 右对象模板的属性KEY
	public final static String RIGHE_ENTITY_KEY = "rightEntity";
	// 关联类型的属性KEY
	public final static String ASSOCIATED_TYPE_KEY = "associatedType";
	// 关联数据的属性KEY
	public final static String CARDINALITY_KEY = "cardinality";

	public StructureModuleNode() {
		set(DefaultParam.typeKey, MY_TYPE);
	}

	public int getCardinality() {
		int cardinality = Integer.parseInt(getValue(CARDINALITY_KEY) + "");
		return cardinality;
	}

	public void setCardinality(int cardinality) {
		set(CARDINALITY_KEY, cardinality);
	}

	public void setLeftEntity(String leftEntity) {
		set(LEFT_ENTITY_KEY, leftEntity);
	}

	public void setRightEntity(String rightEntity) {
		set(RIGHE_ENTITY_KEY, rightEntity);
	}

	public void setAssociatedType(AssociatedType associatedType) {
		set(ASSOCIATED_TYPE_KEY, associatedType.toString());
	}

	public String getLeftEntity() {
		return (getValue(LEFT_ENTITY_KEY) + "").trim();
	}

	public String getRightEntity() {
		return (getValue(RIGHE_ENTITY_KEY) + "").trim();
	}

	public AssociatedType getAssociatedType() {
		String at = (getValue(ASSOCIATED_TYPE_KEY) + "").trim();
		if ("CLAN".equals(at)) {
			return AssociatedType.CLAN;
		} else {
			return AssociatedType.LINK;
		}
	}

	public int getAssociatedRestrain() {
		int cardinality = getValue(CARDINALITY_KEY);
		return cardinality;
	}

	public String toString() {
		String msg = getLeftEntity() + "-" + getRightEntity() + "-"
				+ getAssociatedType() + "-" + getCardinality();
		return msg;
	}

	public String getStructureModuleId() {
		return (getValue(STRUCTURE_MODULE_ID_KEY) + "").trim();
	}

	public void setStructureModuleId(String StructureModuleId) {
		this.set(STRUCTURE_MODULE_ID_KEY, StructureModuleId);
	}

	public static StructureModuleNode changeFromEntity(BasicEntity be) {
		StructureModuleNode node = new StructureModuleNode();
		for (String key : be.keyset()) {
			node.set(key, be.getValue(key));
		}
		return node;
	}
}
