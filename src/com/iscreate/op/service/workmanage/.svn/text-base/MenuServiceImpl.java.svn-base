package com.iscreate.op.service.workmanage;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageMenu;



public class MenuServiceImpl implements MenuService {
	
	
	private static final Log logger = LogFactory.getLog(MenuServiceImpl.class);
	private HibernateTemplate hibernateTemplate;
	
	
	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 新增菜单信息
	 * @param menu
	 */
	public void insertMenu(WorkmanageMenu menu) {
		this.hibernateTemplate.save(menu);
	}
	
	/**
	 * 更新菜单信息
	 * @param menu
	 * @return
	 */
	public void updateMenu(WorkmanageMenu menu) {
		this.hibernateTemplate.update(menu);
	}
	
	/**
	 * 删除菜单信息
	 * @param id
	 * @return
	 */
	public void deleteMenu(WorkmanageMenu menu) {
		this.hibernateTemplate.delete(menu);
	}
	
	
	/**
	 * 根据菜单ID返回菜单节点信息
	 * @param menuId
	 * @return
	 */
	public WorkmanageMenu getMenuEntityByMenuId(String menuId) {
		String hql="select o from WorkmanageMenu o where o.menuId=? and o.isShow=1";
		WorkmanageMenu menu=null;
		long menuId_long=Long.valueOf(menuId);
		List list=this.hibernateTemplate.find(hql,menuId_long);
		if(list!=null && !list.isEmpty()){
			menu=(WorkmanageMenu)list.get(0);
		}
		return menu;
	}
	/**
	 * @author du.hw
	 * @create_time 2013-06-07
	 * 根据菜单menucode返回菜单节点信息
	 * @param menuId
	 * @return
	 */
	public WorkmanageMenu getMenuEntityByMenuCode(String menuCode){
		String hql="select o from WorkmanageMenu o where o.menuCode=? and o.isShow=1";
		WorkmanageMenu menu=null;
		List list=this.hibernateTemplate.find(hql,menuCode);
		if(list!=null && !list.isEmpty()){
			menu=(WorkmanageMenu)list.get(0);
		}
		return menu;
	}
	
	/**
	 * 根据父节点ID和菜单类型返回其可见的子节点列表
	 * @param parentId
	 * @param menuType
	 * @return
	 */
	public List<WorkmanageMenu> getChildrenMenuListByParentIdAndType(String parentId,String menuType) {
		List<WorkmanageMenu> list=null;
		String hql="select o from Menu o where o.isShow=1 and o.menuType=? and o.parentId=? order by o.isLeaf asc,o.menuOrder asc";
		list=(List<WorkmanageMenu>)this.hibernateTemplate.find(hql,menuType,Long.valueOf(parentId));
		return list;
	}



	
	
	/**
	 * 获取所有菜单
	 * @return
	 */
	public List<WorkmanageMenu> getAllMenuList() {
		List<WorkmanageMenu> list=null;
		String hql="select o from Menu o where o.isShow=1";
		list=(List<WorkmanageMenu>)this.hibernateTemplate.find(hql);
		return list;
	}

	
	

	/**
	 * 查询菜单信息
	 * @param query
	 * @return
	 */
	public List<Map<String, String>> getMenuList(String executeSql,
			List<String> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	/**
	 * 根据菜单类型返回其可见的子节点列表
	 * @param menuType
	 * @return
	 */
	public List<WorkmanageMenu> getChildrenMenuListByType(String menuType) {
		List<WorkmanageMenu> list  = null;
		if(menuType==null||"".equals(menuType)) return null;
		String hql="select o from WorkmanageMenu o where o.isShow=1 and o.menuType=? order by o.isLeaf asc,o.menuOrder asc";
		list=(List<WorkmanageMenu>)this.hibernateTemplate.find(hql,menuType);
		return list;
	}
	
	/**
	 * 获取用于在门户主页待办显示的菜单
	 * @return
	 */
	public List<WorkmanageMenu> getPortalMenuList(){
		List<WorkmanageMenu> list  = null;
		String hql="select o from WorkmanageMenu o where o.isShow=1 and o.isPortal=1 order by o.isLeaf asc,o.menuOrder asc";
		list=(List<WorkmanageMenu>)this.hibernateTemplate.find(hql);
		return list;
	}


	

}
