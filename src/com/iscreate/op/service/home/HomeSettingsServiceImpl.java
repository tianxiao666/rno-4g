package com.iscreate.op.service.home;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.home.HomeSettingsDao;
import com.iscreate.op.pojo.home.HomeSettings;

public class HomeSettingsServiceImpl implements HomeSettingsService {
	private HomeSettingsDao homeSettingsDao;
	
	private Log log = LogFactory.getLog(this.getClass());

	public HomeSettingsDao getHomeSettingsDao() {
		return homeSettingsDao;
	}

	public void setHomeSettingsDao(HomeSettingsDao homeSettingsDao) {
		this.homeSettingsDao = homeSettingsDao;
	}
	
	/**
	 * 根据用户ID，角色ID，组件ID获取用户个人设置 
	* @author ou.jh
	* @date Jun 6, 2013 3:44:57 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @param homeItemId
	* @param @param roleId
	* @param @return        
	* @throws
	 */
	public List<HomeSettings> getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId(long orgUserId,long homeItemId,long roleId){
		log.info("进入getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId方法");
		List<HomeSettings> homeSettingsList = this.homeSettingsDao.getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId(orgUserId, homeItemId, roleId);
		log.info("执行getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId方法成功，实现了”根据用户ID，角色ID，组件ID获取用户个人设置 ");
		log.info("退出getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId方法,返回List<HomeSettings>");
		return homeSettingsList;
	}
	
	
	/**
	 * 保存用户个人设置 
	* @author ou.jh
	* @date Jun 6, 2013 3:49:38 PM
	* @Description: TODO 
	* @param @param homeSettings
	* @param @return        
	* @throws
	 */
	public Serializable saveHomeSettings(HomeSettings homeSettings){
		log.info("进入saveHomeSettings方法");
		Serializable saveHomeSettings = this.homeSettingsDao.saveHomeSettings(homeSettings);
		log.info("执行saveHomeSettings方法成功，实现了”保存用户个人设置 ");
		log.info("退出saveHomeSettings方法,返回Serializable");
		return saveHomeSettings;
	}
	
	/**
	 * 修改用户个人设置 
	* @author ou.jh
	* @date Jun 6, 2013 3:51:02 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public void updateHomeSettings(HomeSettings homeSettings){
		log.info("进入updateHomeSettings方法");
		this.homeSettingsDao.updateHomeSettings(homeSettings);
		log.info("执行updateHomeSettings方法成功，实现了”修改用户个人设置 ");
		log.info("退出updateHomeSettings方法,返回Serializable");
	}
	
	/**
	 * 删除用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:55:12 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public void deleteHomeSettings(HomeSettings homeSettings){
		log.info("进入updateHomeSettings方法");
		this.homeSettingsDao.deleteHomeSettings(homeSettings);
		log.info("执行deleteHomeSettings方法成功，实现了”修改用户个人设置 ");
		log.info("退出deleteHomeSettings方法,返回Serializable");
	}
}
