package com.iscreate.op.dao.home;

import java.io.Serializable;
import java.util.List;

import com.iscreate.op.pojo.home.HomeSettings;

public interface HomeSettingsDao {
	/**
	 * 保存用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:26:31 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public Serializable saveHomeSettings(HomeSettings homeSettings);
	
	/**
	 * 修改用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:28:47 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public void updateHomeSettings(HomeSettings homeSettings);
	
	/**
	 * 根据用户ID，角色ID，组件ID获取用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:34:14 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @param homeItemId
	* @param @param roleId
	* @param @return        
	* @throws
	 */
	public List<HomeSettings> getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId(long orgUserId,long homeItemId,long roleId);
	
	/**
	 * 删除用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:55:12 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public void deleteHomeSettings(HomeSettings homeSettings);
}
