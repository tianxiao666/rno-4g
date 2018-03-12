package com.iscreate.plat.networkresource.structure.instance.api;


import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;

public interface StructureSearchInterface {
	/**
	 * 获取一个组中的AET NAME
	 * 
	 * @param aetgName
	 *            结构中的一个应用数据对象组名称
	 * @return 该组中的应用数据对象类型列表
	 */
	public String[] getAetNameOfAetg(String aetgName);

	/**
	 * 递归获取指定对象begin下特定条件query的子应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param query
	 *            目标类型的相关查询条件
	 * @return 指定类型的目标应用数据对象
	 */
	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String query,String aetName);

	/**
	 * 递归获取指定对象begin下特定类型desAetName的子应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param desAetName
	 *            目标对象的数据类型
	 * @return 指定类型的目标应用数据对象
	 */
	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String desAetName);

	/**
	 * 查询结构模板，获取某一个应用数据对象在指定的方向上与其关联的其它应用数据对象类型
	 * 
	 * 
	 * @param begin
	 *            应用数据对象
	 * @param type
	 *            关联类型
	 * @return 在关联方向上的应用数据对象列表
	 */
	public String[] getAssociatedAetName(ApplicationEntity begin,
			AssociatedType type);

	/**
	 * 查询结构模板，获取某一个应用数据对象类型在指定的方向上与其关联的其它应用数据对象类型
	 * 
	 * @param currentAetName
	 *            应用数据对象类型
	 * @param type
	 *            关联的方向
	 * @return 在关联方向上的应用数据对象列表
	 */
	public String[] getAssociatedAetName(String currentAetName,
			AssociatedType type);

	/**
	 * 获取指定对象begin的关联方向上的关联对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param type
	 *            关联的类型
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type);

	/**
	 * 获取指定对象begin在关联方向上特定条件query对象的应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param type
	 *            关联的类型
	 * @param query
	 *            目标对象的条件
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type,String query,String aetName);

	/**
	 * 获取指定对象begin在关联方向上特定类型desAetName的应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param desAetName
	 *            目标对象的数据类型
	 * @param type
	 *            关联的类型
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			String desAetName, AssociatedType type);

	/**
	 * 获取该结构对象对应的结构模板
	 * 
	 * @return 该结构实例所引用的结构模板对象
	 */
	public StructureModule getStructureModule();

	/**
	 * 获取应用数据对象start、end之间的路由关系
	 * 
	 * @param start
	 *            起点应用数据对象
	 * @param end
	 *            终点应用数据对象
	 * @return 使用应用数据对象构成的路径列表
	 */
	public List<List<ApplicationEntity>> getTraceRoad(ApplicationEntity start,
			ApplicationEntity end);
}
