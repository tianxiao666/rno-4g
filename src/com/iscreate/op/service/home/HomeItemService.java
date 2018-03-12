package com.iscreate.op.service.home;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.home.HomeItem;
import com.iscreate.op.pojo.home.HomeItemRelaRole;

public interface HomeItemService {
	
	
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
	public List<Map<String,Object>> getAllHomeItemListService();
	/**
	 * 
	 * @description: 保存实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializabel     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public Serializable saveEntityService(Object entity);
	/**
	 * 
	 * @description: 删除实体
	 * @author：yuan.yw
	 * @param String itemId
	 * @return     void   
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void deleteEntityService(String itemId);
	/**
	 * 
	 * @description: 更新门户组件
	 * @author：yuan.yw
	 * @param HomeItem
	 * @return     
	 * @return void     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void updateHomeItemService(HomeItem entity);
	/**
	 * 
	 * @description: 获取全部角色
	 * @author：yuan.yw    
	 * @return List<Map<String,Object>>    
	 * @date：Jun 7, 2013 10:05:52 AM
	 */
	public List<Map<String,Object>> getAllRolesService();
	/**
	 * 
	 * @description: 通过角色id type获取关联门户组件
	 * @author：yuan.yw
	 * @param roleId
	 * @param type
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 7, 2013 10:44:41 AM
	 */
	public List<Map<String,Object>> getHomeItemListByRoleIdAndTypeService(String roleId,String type);
	/**
	 * 
	 * @description:删除角色关联门户组件
	 * @author：yuan.yw
	 * @param roleId
	 * @param itemIds
	 * @return     
	 * @return int     
	 * @date：Jun 7, 2013 3:26:50 PM
	 */
	public int deleteRoleHomeItemService(String roleId,String itemIds);
	/**
	 * 
	 * @description: 更新角色门户组件
	 * @author：yuan.yw
	 * @param hrr     
	 * @return void     
	 * @date：Jun 7, 2013 5:49:53 PM
	 */
	public void updateRoleHomeItemService(HomeItemRelaRole hrr);
}
