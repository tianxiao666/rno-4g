package com.iscreate.plat.networkresource.structure.template;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;

public class StructureModuleEntity extends BasicEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2674222536978541044L;

	// 对象的模板名称
	public final static String MY_TYPE = StructureModuleEntity.class
			.getSimpleName();
	// 存储结构模板的属性名称
	public final static String STRUCTURE_MODULE_ID_KEY = "structureModuleId";
	// 存储应用数据对象的属性名称
	public final static String AET_KEY = "aet";

	public StructureModuleEntity() {
		this.set(DefaultParam.typeKey, MY_TYPE);
	}

	public String getAet() {
		return this.getValue(AET_KEY) + "";
	}

	public void setAet(String aet) {
		this.set(AET_KEY, aet);
	}

	public String getStructureModuleId() {
		return this.getValue(STRUCTURE_MODULE_ID_KEY) + "";
	}

	public void setStructureModuleId(String StructureModuleId) {
		this.set(STRUCTURE_MODULE_ID_KEY, StructureModuleId);
	}
	public static StructureModuleEntity changeFromEntity(BasicEntity be) {
		StructureModuleEntity s = new StructureModuleEntity();
		for (String key : be.keyset()) {
			s.set(key, be.getValue(key));
		}
		return s;
	}
	
	public String toString(){
		return this.getAet();
	}
}
