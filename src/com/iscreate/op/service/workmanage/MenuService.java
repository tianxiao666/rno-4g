package com.iscreate.op.service.workmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.workmanage.WorkmanageMenu;



public interface MenuService {
	
	
	/**
	 * 新增菜单信息
	 * @param menu
	 */
	public void insertMenu(WorkmanageMenu menu);
	

	/**
	 * 更新菜单信息
	 * @param menu
	 * @return
	 */
	public void updateMenu(WorkmanageMenu menu);
	

	/**
	 * 删除菜单信息
	 * @param id
	 * @return
	 */
	public void deleteMenu(WorkmanageMenu menu);
	

	/**
	 * 根据菜单ID返回菜单节点信息
	 * @param menuId
	 * @return
	 */
	public WorkmanageMenu getMenuEntityByMenuId(String menuId);
	/**
	 * @author du.hw
	 * @create_time 2013-06-07
	 * 根据菜单menucode返回菜单节点信息
	 * @param menuId
	 * @return
	 */
	public WorkmanageMenu getMenuEntityByMenuCode(String menuCode);
	
	
	/**
	 * 根据父节点ID和菜单类型返回其可见的子节点列表
	 * @param parentId
	 * @param menuType
	 * @return
	 */
	public List<WorkmanageMenu> getChildrenMenuListByParentIdAndType(String parentId,String menuType);	
	
	
	
	/**
	 * 获取所有菜单
	 * @return
	 */
	public List<WorkmanageMenu> getAllMenuList();


	/**
	 * 查询菜单信息
	 * @param query
	 * @return
	 */
	public List<Map<String,String>> getMenuList(String executeSql,List<String> params);
	
	
	/**
	 * 根据菜单类型返回其可见的子节点列表
	 * @param menuType
	 * @return
	 */
	public List<WorkmanageMenu> getChildrenMenuListByType(String menuType);
	
	
	/**
	 * 获取用于在门户待办显示的菜单
	 * @return
	 */
	public List<WorkmanageMenu> getPortalMenuList();
	
	
}
