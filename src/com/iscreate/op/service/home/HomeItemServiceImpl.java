package com.iscreate.op.service.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.home.HomeItemDao;
import com.iscreate.op.dao.system.SysRoleDao;
import com.iscreate.op.pojo.home.HomeItem;
import com.iscreate.op.pojo.home.HomeItemRelaRole;
import com.iscreate.op.pojo.system.SysRole;

/**
 * 门户组件信息SERVICE
	 * 
	 * @author ou.jh
	 * @date Jun 5, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class HomeItemServiceImpl implements HomeItemService{
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private HomeItemDao homeItemDao;
	private SysRoleDao sysRoleDao;
	

	
	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	public HomeItemDao getHomeItemDao() {
		return homeItemDao;
	}

	public void setHomeItemDao(HomeItemDao homeItemDao) {
		this.homeItemDao = homeItemDao;
	}

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
	public List<Map<String, Object>> getHmoeItemByRoleIdAndAccount(long roleId , String account){
		log.info("进入getHmoeItemByRoleIdAndAccount方法");
		List<Map<String,Object>> hmoeItemByRoleIdAndOrgUserId = this.homeItemDao.getHmoeItemByRoleIdAndAccount(roleId, account);
		log.info("执行getHmoeItemByRoleIdAndAccount方法成功，实现了”根据角色ID与用户ID获取门户组件信息");
		log.info("退出getHmoeItemByRoleIdAndAccount方法,返回List<Map<String, Object>>");
		return hmoeItemByRoleIdAndOrgUserId;
	}
	
	
	/**
	 * 根据账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 11:16:14 AM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHmoeItemByAccount(String account){
		log.info("进入getHmoeItemByRoleIdAndAccount方法");
		List<Map<String,Object>> hmoeItemByRoleIdAndOrgUserId = this.homeItemDao.getHmoeItemByAccount(account);
		log.info("执行getHmoeItemByRoleIdAndAccount方法成功，实现了”根据账号获取门户组件信息");
		log.info("退出getHmoeItemByRoleIdAndAccount方法,返回List<Map<String, Object>>");
		return hmoeItemByRoleIdAndOrgUserId;
	}
	/**
	 * 
	 * @description: 获取全部门户组件
	 * @author：yuan.yw
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jun 6, 2013 3:52:39 PM
	 */
	public List<Map<String,Object>> getAllHomeItemListService(){
		log.info("进入 getAllHomeItemListService()，获取全部门户组件");
		List<Map<String,Object>> resultList = this.homeItemDao.getAllHomeItemList();
		log.info("退出getAllHomeItemListService()，返回结果resultList="+resultList);
		return resultList;
		
	}
	
	/**
	 * 
	 * @description: 保存实体
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Serializabel     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public Serializable saveEntityService(Object entity){
		log.info("进入 saveEntityService(Object entity)，保存实体");
		log.info("参数entity："+entity);
		Serializable s = this.homeItemDao.saveEntity(entity);
		log.info("退出saveEntityService(Object entity)，返回结果s="+s);
		return s;
	}
	/**
	 * 
	 * @description: 删除实体
	 * @author：yuan.yw
	 * @param itemId
	 * @return     void     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void deleteEntityService(String itemId){
		log.info("进入 deleteEntityService(String itemId)，删除实体");
		log.info("参数itemId："+itemId);
		HomeItem hi = this.homeItemDao.getHomeItemById(itemId);
		if(hi!=null){
			this.homeItemDao.deleteEntity(hi);
		}
		log.info("退出deleteEntityService(String itemId)");
	}
	/**
	 * 
	 * @description: 更新门户组件
	 * @author：yuan.yw
	 * @param HomeItem
	 * @return     
	 * @return void     
	 * @date：Jun 6, 2013 5:21:54 PM
	 */
	public void updateHomeItemService(HomeItem entity){
		log.info("进入 updateHomeItemService(HomeItem entity)，更新更新门户组件");
		log.info("参数entity："+entity);
		HomeItem hi = this.homeItemDao.getHomeItemById(entity.getHomeItemId()+"");
		if(hi!=null){
			hi.setMaxurl(entity.getMaxurl());
			hi.setShowtitle(entity.getShowtitle());
			hi.setTitle(entity.getTitle());
			hi.setStatus(entity.getStatus());
			hi.setUpdatetime(new Date());
			hi.setUrl(entity.getUrl());
			this.homeItemDao.updateEntity(hi);
		}
		log.info("退出updateHomeItemService(HomeItem entity)");
	}
	
	/**
	 * 
	 * @description: 获取全部角色
	 * @author：yuan.yw    
	 * @return List<Map<String,Object>>    
	 * @date：Jun 7, 2013 10:05:52 AM
	 */
	public List<Map<String,Object>> getAllRolesService(){
		log.info("进入getAllRolesService()，获取全部角色");
		List<SysRole> roleList =  this.sysRoleDao.getAllRole();
		List<Map<String,Object>> resultList = null;
		if(roleList!=null && !roleList.isEmpty()){
			resultList = new ArrayList<Map<String,Object>>();
			for(SysRole r:roleList){
				Map<String,Object> mp = new HashMap<String,Object>();
				mp.put("orgRoleTypeCode", "org_role");
				mp.put("orgRoleId", r.getRoleId());
				mp.put("orgRoleTypeId", r.getRoleTypeId());
				mp.put("orgRoleCode", r.getCode());
				mp.put("orgRoleName", r.getName());
				mp.put("orgRoleType","组织角色");
				resultList.add(mp);
			}

		}
		log.info("退出getAllRolesService(),返回resultList="+resultList);
		return resultList;
	}
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
	public List<Map<String,Object>> getHomeItemListByRoleIdAndTypeService(String roleId,String type){
		log.info("进入getHomeItemListByRoleIdAndTypeService()，通过角色id type获取关联门户组件");
		List<Map<String,Object>> resultList =  this.homeItemDao.getRoleHomeItemListByRoleIdAndType(roleId,type);
		log.info("退出getHomeItemListByRoleIdAndTypeService(),返回resultList="+resultList);
		return resultList;
	}
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
	public int deleteRoleHomeItemService(String roleId,String itemIds){
		log.info("进入getHomeItemListByRoleIdAndTypeService()，通过角色id type获取关联门户组件");
		int i =  this.homeItemDao.deleteEntityForHome("HomeItemRelaRole",roleId,itemIds);
		if(i>0){
			i=this.homeItemDao.deleteEntityForHome("HomeSettings",roleId,itemIds);
		}
		log.info("退出getHomeItemListByRoleIdAndTypeService(),返回i="+i);
		return i;
	}
	/**
	 * 
	 * @description: 更新角色门户组件
	 * @author：yuan.yw
	 * @param hrr     
	 * @return void     
	 * @date：Jun 7, 2013 5:49:53 PM
	 */
	public void updateRoleHomeItemService(HomeItemRelaRole hrr){
		log.info("进入 updateRoleHomeItemService(HomeItemRelaRole hrr)，更新更新门户组件");
		log.info("参数entity："+hrr);
		HomeItemRelaRole h = this.homeItemDao.getHomeItemByRoleIdAndItemId(hrr.getRoleId()+"",hrr.getHomeItemId()+"");
		if(h!=null){
			h.setItemColumn(hrr.getItemColumn());
			h.setItemRow(hrr.getItemRow());
			h.setItemHeight(hrr.getItemHeight());
			h.setItemWidth(hrr.getItemWidth());
			h.setUpdatetime(new Date());
			this.homeItemDao.updateEntity(h);
		}
		log.info("退出updateRoleHomeItemService(HomeItemRelaRole hrr)");
	}
}
