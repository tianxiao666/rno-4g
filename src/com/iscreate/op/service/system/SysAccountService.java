package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysAccount;

public interface SysAccountService {
	/**
	 * 锁住用户账号
	 * 
	 * @param account
	 */
	public boolean lockUser(String account) throws Exception;
	
	/**
	 * 解锁用户账号
	 */
	public boolean setNormalUser(String account) throws Exception;
	
	/**
	 * 保存账号
	 * 
	 * @param account
	 */
	public Serializable txSaveAccount(SysAccount account) throws Exception;
	
	/**
	 * 检查员工编号是否可用
	* @date May 14, 2013 9:45:04 AM
	* @Description: TODO 
	* @param @param account
	* @param @return
	* @param @throws Exception        
	* @throws
	 */
	public int txCheckUserNumber(String userNumber);
	
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
	 * 检查密码是否一致
	 * 
	 * @param account
	 * @param password
	 * @return 0:ok 1：账号不存在 2: 账号无效 3：账号不在有效范围内 4：密码不对
	 * 
	 */
	public int txCheckPassword(String account, String password);
	
	/**
	 * 检查账号是否可用
	* @date May 14, 2013 9:45:04 AM
	* @Description: TODO 
	* @param @param account
	* @param @return
	* @param @throws Exception        
	* @throws
	 */
	public int txCheckAccount(String account) ;
	
	/**
	 * 加密密码
	 * 
	 * @param oriPwd
	 * @return
	 */
	public String encodePassword(String oriPwd) ;
	
	/**
	 * 检查账号&密码是否可用
	 * 
	 * @param userName
	 * @param inputPassword
	 * @return
	 */
	public String txCheckAccount(final String userName,
			final String inputPassword);
	
	/**
	 * 检测人员账号是否已经占用
	* @author ou.jh
	* @date May 24, 2013 10:52:30 AM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public boolean checkProviderAccountService(String account);
	
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
	

	
	
}
