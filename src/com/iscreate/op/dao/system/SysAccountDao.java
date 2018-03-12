package com.iscreate.op.dao.system;

import java.util.Map;

import com.iscreate.op.pojo.system.SysAccount;

public interface SysAccountDao {
	/**
	 * 根据账号获取账号信息
	* @author ou.jh
	* @date May 24, 2013 10:40:08 AM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public SysAccount getSysAccountByAccount(String account);
	/**
	 * 根据账号获取账号信息
	* @author du.hw
	* @createtime 2014-02-27
	* 通过账号获取账号和用户状态
	 */
	public Map<String,Object> getAccountInfoAndStatusByAccount(String account);
	/**
	 * 是否超级用户密码
	 * @param session
	 * @param password
	 * @return
	 */
	public SysAccount isSuperAdminPassword(final String password);
	
	
	/**
	 * 根据登录人账号获取用户信息
	 * 
	 * @date May 9, 2013 10:04:25 AM
	 * @Description: TODO
	 * @param orgUserId
	 * @param
	 * @return
	 * @throws
	 */
	public SysAccount getSysAccountByOrgUserId(long orgUserId);
	
	/**
	 * 根据登录人账号获取用户信息
	 * 
	 * @date May 9, 2013 10:04:25 AM
	 * @Description: TODO
	 * @param orgUserId
	 * @param
	 * @return
	 * @throws
	 */
	public SysAccount getSysRoleByOrgUserId(long orgUserId);
}
