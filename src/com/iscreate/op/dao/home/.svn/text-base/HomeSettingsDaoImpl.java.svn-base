package com.iscreate.op.dao.home;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.home.HomeSettings;

public class HomeSettingsDaoImpl implements HomeSettingsDao {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 保存用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:26:31 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public Serializable saveHomeSettings(HomeSettings homeSettings){
		Serializable save = this.hibernateTemplate.save(homeSettings);
		return save;
	}
	
	/**
	 * 修改用户个人设置
	* @author ou.jh
	* @date Jun 6, 2013 3:28:47 PM
	* @Description: TODO 
	* @param @param homeSettings        
	* @throws
	 */
	public void updateHomeSettings(HomeSettings homeSettings){
		this.hibernateTemplate.update(homeSettings);
	}
	
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
	public List<HomeSettings> getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId(long orgUserId,long homeItemId,long roleId){
		String hql = "from HomeSettings hs where hs.orgUserId="+orgUserId+" and hs.homeItemId = "+homeItemId+" and hs.roleId = "+roleId;
		List<HomeSettings> hmeSettingsList = (List<HomeSettings>)this.hibernateTemplate.find(hql);
		return hmeSettingsList;
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
		this.hibernateTemplate.delete(homeSettings);
	}
}
