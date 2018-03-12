package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

/**
 * 超级用户信息dao
	 * 
	 * @author ou.jh
	 * @date Jun 20, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public interface SysSuperAdminDao {
	/**
	 * 根据密码获取超级用户信息
	* @author ou.jh
	* @date Jun 20, 2013 3:55:52 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getSysSuperAdminByPassword(String password);
}
