package com.iscreate.op.dao.home;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.home.HomeItem;
import com.iscreate.op.pojo.home.HomeItemRelaRole;

public interface HomeItemDao {
	/**
	 * 根据角色ID与账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 11:16:14 AM
	* @Description: TODO 
	* @param @param roleId
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHmoeItemByRoleIdAndAccount(long roleId , String account);
	
	
	/**
	 * 根据账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 11:16:14 AM
	* @Description: TODO 
	* @param @param roleId
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHmoeItemByAccount(String account);
	/**
	 * 
	 * @description: 获取全部门户组件
	 * @author：yuan.yw
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 6, 2013 3:52:39 PM
	 */
	public List<Map<String,Object>> getAllHomeItemList();
	/**
	 * 
	 * @description: 保存实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializabel     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public Serializable saveEntity(Object entity);
	/**
	 * 
	 * @description: 删除实体
	 * @author：yuan.yw
	 * @param entity     
	 * @return void     
	 * @date：Jun 6, 2013 5:50:47 PM
	 */
	public void deleteEntity(Object entity);
	/**
	 * 
	 * @description:根据id获取门户组件
	 * @author：
	 * @param itemId
	 * @return     
	 * @return HomeItem     
	 * @date：Jun 6, 2013 5:51:15 PM
	 */
	public HomeItem getHomeItemById(String itemId);
	/**
	 * 
	 * @description: 更新实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return void     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void updateEntity(Object entity);
	/**
	 * 
	 * @description: 通过角色id type获取关联的门户组件
	 * @author：yuan.yw
	 * @param roleId 
	 * @param type 
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 7, 2013 10:48:26 AM
	 */
	public List<Map<String,Object>> getRoleHomeItemListByRoleIdAndType(String roleId,String type);
	/**
	 * 
	 * @description: 根据角色id 实体名 组件id 字符串 删除门户相关实体
	 * @author：yuan.yw
	 * @param entityName
	 * @param roleId
	 * @param itemIds
	 * @return     
	 * @return int     
	 * @date：Jun 7, 2013 3:33:03 PM
	 */
	public int deleteEntityForHome(String entityName ,String roleId,String itemIds);
	/**
	 * 
	 * @description:根据角色id 组件id 获取角色组件
	 * @author：yuan.yw
	 * @param roleId
	 * @param itemId
	 * @return     
	 * @return HomeItemRelaRole     
	 * @date：Jun 7, 2013 5:56:27 PM
	 */
	public HomeItemRelaRole getHomeItemByRoleIdAndItemId(String roleId,String itemId);
}
