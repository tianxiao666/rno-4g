package com.iscreate.plat.networkresource.structure.instance.api;



import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public interface StructureModifyInterface {
	/**
	 * 将entity加入到结构实例当中
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence addApplicationEntityToStructure(
			ApplicationEntity entity);

	/**
	 * 创建两个应用数据对象的关系
	 * 
	 * @param leftAe
	 *            应用数据对象
	 * @param rightAe
	 *            应用数据对象
	 * @param type
	 *            两个对象的关联类型
	 * @return 状态对象
	 */
	public StructureConsequence createAssociation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType type);

	/**
	 * 单独删除一个应用数据对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityOnly(
			ApplicationEntity entity);

	/**
	 * 删除一个应用数据对象其下所有的子对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityRecursion(
			ApplicationEntity entity);

	/**
	 * 删除一个应用数据对象及其下所有的子对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityRecursionByFource(
			ApplicationEntity entity);

	/**
	 * 删除两个对象之间的关系
	 * 
	 * @param leftAe
	 *            应用数据对象
	 * @param rightAe
	 *            应用数据对象
	 * @param type
	 *            两个对象的关联类型
	 * @return 状态对象
	 */
	public StructureConsequence delAssociation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType type);

	/**
	 * 将结构的数据对象保存起来
	 */
	public int store();
}
