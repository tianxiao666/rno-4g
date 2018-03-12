package com.iscreate.op.service.system;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.system.SysAccountDao;
import com.iscreate.op.dao.system.SysOrgUserDao;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysOrgUserPm;
import com.iscreate.plat.system.strategy.PasswordRule;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.system.util.AuthorityPasswordEncoder;

public class SysAccountServiceImpl implements SysAccountService {
	
	
	private Log log = LogFactory.getLog(this.getClass());
	private SysOrgUserDao sysOrgUserDao;
	private CommonDao commonDao;
	private AuthorityPasswordEncoder authorityPasswordEncoder;// 密码加密器
	private PasswordRule passwordRule;// 校验密码是否合符要求的验证器
	private SysAccountDao sysAccountDao;
	/**
	 * 锁住用户
	 */
	public boolean lockUser(String account) throws Exception {
		/*SysAccount accObj = commonDao.geUniqueObjectByPropertyAndValue(
				SysAccount.class, "account", account);*/
		Map<String,Object> accObj = sysAccountDao.getAccountInfoAndStatusByAccount(account);
        SysOrgUser sysOrgUser = sysOrgUserDao.getSysOrgUserByAccount(account);//用户基本信息
        
		if (accObj == null) {
			throw new Exception("不存在账号为：" + account + "的用户");
		}
		// 锁住
		sysOrgUser.setStatus(2);// status:2 为锁住状态
		commonDao.updateObject(sysOrgUser);
		return true;
	}
	
	/**
	 * 解锁用户账号
	 */
	public boolean setNormalUser(String account) throws Exception {
		/*SysAccount accObj = commonDao.geUniqueObjectByPropertyAndValue(
				SysAccount.class, "account", account);*/
		Map<String,Object> accObj = sysAccountDao.getAccountInfoAndStatusByAccount(account);
        SysOrgUser sysOrgUser = sysOrgUserDao.getSysOrgUserByAccount(account);//用户基本信息
		if (accObj == null) {
			throw new Exception("不存在账号为：" + account + "的用户");
		}
		// 解锁
		sysOrgUser.setStatus(1);// status:1 为锁住状态
		commonDao.updateObject(sysOrgUser);
		return true;
	}
	
	/**
	 * 保存账号
	 */
	public Serializable txSaveAccount(SysAccount account) throws Exception {
		String message = "";
		if (account == null) {
			message = "保存的账号对象不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		if (account.getAccount() == null
				|| "".equals(account.getAccount().trim())) {
			message = "保存的账号中的account字段信息不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		SysAccount exist = commonDao.geUniqueObjectByPropertyAndValue(
				SysAccount.class, "account", account.getAccount());
		if (exist != null) {
			message = "账号：" + account.getAccount() + "已经存在！";
			log.error(message);
			throw new Exception(message);
		}
		// 允许同名，中文名不限制

		// 保存
		if (account.getPassword() == null) {
			message = "账号：" + account.getAccount() + "的密码不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		// 判断是否符合规则要求
		if (passwordRule != null
				&& passwordRule.checkIfMeet(account.getPassword()) != 0) {
			message = "账号：" + account.getAccount() + "的密码不符合要求！"
					+ passwordRule.getPrompWords();
			log.error(message);
			throw new Exception(message);
		}
		// 加密
		account.setPassword(encodePassword(account.getPassword()));

		// // 账号的有效期
		// GregorianCalendar calendar = new GregorianCalendar();
		//
		// if (account.getCreatetime() == null) {
		// calendar.set(Calendar.YEAR, 1970);
		// calendar.set(Calendar.MONTH, 1);
		// calendar.set(Calendar.DAY_OF_MONTH, 1);
		// account.getCreatetime(calendar.getTime());
		// }
		// if (account.getTime_range_end() == null) {
		// calendar.set(Calendar.YEAR, 2020);
		// calendar.set(Calendar.MONTH, 12);
		// calendar.set(Calendar.DAY_OF_MONTH, 31);
		// account.setTime_range_end(calendar.getTime());
		// }
		//设置用户创建时间
		account.setCreatetime(new Date());
		Serializable saveObject = commonDao.saveObject(account);
		return saveObject;
	}
	
	
	

	/**
	 * 检查密码是否一致
	 * 
	 * @param account
	 * @param password
	 * @return 0:ok 1：账号不存在 2: 账号无效 3：账号不在有效范围内 4：密码不对
	 * 
	 */
	public int txCheckPassword(String account, String password) {
		/*SysAccount acc = commonDao.geUniqueObjectByPropertyAndValue(
				SysAccount.class, "account", account);*/
		Map<String,Object> acc = sysAccountDao.getAccountInfoAndStatusByAccount(account);
		SysOrgUser sysOrgUser = sysOrgUserDao.getSysOrgUserByAccount(account);
		// 账号不存在
		if (acc == null) {
			return AuthorityConstant.CheckPasswordResult.NOTEXIST;
		}
		
		// 无效
		if(sysOrgUser == null){
			return AuthorityConstant.CheckPasswordResult.NOTVALID;
		}
		
		// 无效
		if (sysOrgUser.getStatus() == null || sysOrgUser.getStatus() != 1) {
			return AuthorityConstant.CheckPasswordResult.NOTVALID;
		}

		// 解密密码
		String dbPwd = acc.get("PASSWORD").toString();
		String inputPwd = password;
		if (authorityPasswordEncoder != null) {
			inputPwd = authorityPasswordEncoder.encodePassword(password);
		}
		// 匹配密码是否一致
		if (inputPwd != null) {
			if (inputPwd.equals(dbPwd)) {
				return AuthorityConstant.CheckPasswordResult.CHECKOK;
			}
		}
		return AuthorityConstant.CheckPasswordResult.PASSWORDNOTMEET;
	}
	

	/**
	 * 检查账号&密码是否可用
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public String txCheckAccount(final String userName,
			final String inputPassword) {

		int result = txCheckPassword(userName, inputPassword);
		log.debug("login result = " + result);
		if (result == AuthorityConstant.CheckPasswordResult.CHECKOK) {
			return "User-Login-0000";// /登录ok
		} else if (result == AuthorityConstant.CheckPasswordResult.NOTEXIST) {
			return "User-Login-0003";// 用户信息不存在
		} else if (result == AuthorityConstant.CheckPasswordResult.NOTVALID) {
			return "User-Login-2000";// 账号被锁
		} else if (result == AuthorityConstant.CheckPasswordResult.OUTOFTIMERANGE) {
			return "User-Login-2001";// 账号不在使用期
		} else {
			return "User-Login-0001";// /登录密码不正确
		}
	}

	/**
	 * 加密密码
	 * 
	 * @param oriPwd
	 * @return
	 */
	public String encodePassword(String oriPwd) {
		if (authorityPasswordEncoder != null) {
			// //后面带上加密器的编号
			// return
			// passwordEncoder.encodePassword(oriPwd)+"|-|"+passwordEncoder.getType();
			return authorityPasswordEncoder.encodePassword(oriPwd);
		} else {
			return oriPwd;
		}
	}
	
	/**
	 * 检查账号是否可用
	* @date May 14, 2013 9:45:04 AM
	* @Description: TODO 
	* @param @param account
	* @param @return
	* @param @throws Exception        
	* @throws
	 */
	public int txCheckAccount(String account){
		SysAccount exist = commonDao.geUniqueObjectByPropertyAndValue(
				SysAccount.class, "account", account);
		String message = "";
		if (exist != null) {
			message = "账号：" + account + "已经存在！";
			log.info(message);
			return AuthorityConstant.AccountStatus.UNAVAILABLE;
			//throw new Exception(message, ErrorType.DUPLICATED);
		} else {
			return AuthorityConstant.AccountStatus.AVAILABLE;
		}
	}
	
	/**
	 * 检查员工编号是否可用
	* @date May 14, 2013 9:45:04 AM
	* @Description: TODO 
	* @param @param account
	* @param @return
	* @param @throws Exception        
	* @throws
	 */
	public int txCheckUserNumber(String userNumber){
		SysOrgUserPm exist = commonDao.geUniqueObjectByPropertyAndValue(
				SysOrgUserPm.class, "userNumber", userNumber);
		String message = "";
		if (exist != null) {
			message = "员工编号：" + userNumber + "已经存在！";
			log.info(message);
			return AuthorityConstant.AccountStatus.UNAVAILABLE;
			//throw new Exception(message, ErrorType.DUPLICATED);
		} else {
			return AuthorityConstant.AccountStatus.AVAILABLE;
		}
	}
	
	/**
	 * 检测人员账号是否已经占用
	* @author ou.jh
	* @date May 24, 2013 10:52:30 AM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public boolean checkProviderAccountService(String account){
		log.info("进入checkProviderAccountService方法");
		boolean returnBool = true;
		//根据账号获取账号信息
		SysAccount sysAccountByAccount = this.sysAccountDao.getSysAccountByAccount(account);
		if(sysAccountByAccount != null){
			returnBool = true;
		}else{
			returnBool = false;
		}
		log.info("执行checkProviderAccountService方法成功，实现了”检测人员账号是否已经占用“的功能");
		log.info("退出checkProviderAccountService方法,返回boolean");
		return returnBool;
	}
	
	/**
	 * 根据账号获取账号信息
	* @author ou.jh
	* @date May 24, 2013 10:40:08 AM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public SysAccount getSysAccountByAccount(String account){
		log.info("进入getSysAccountByAccount方法");
		SysAccount sysAccountByAccount = this.sysAccountDao.getSysAccountByAccount(account);
		log.info("执行getSysAccountByAccount方法成功，实现了”检测人员与角色关系“的功能");
		log.info("退出getSysAccountByAccount方法,返回SysAccount");
		return sysAccountByAccount;
	}
	
	
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
	public SysAccount getSysAccountByOrgUserId(long orgUserId){
		SysAccount sysAccountByAccount = this.sysAccountDao.getSysAccountByOrgUserId(orgUserId);
		return sysAccountByAccount;
	}
	

	public SysOrgUserDao getSysOrgUserDao() {
		return sysOrgUserDao;
	}

	public void setSysOrgUserDao(SysOrgUserDao sysOrgUserDao) {
		this.sysOrgUserDao = sysOrgUserDao;
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public AuthorityPasswordEncoder getAuthorityPasswordEncoder() {
		return authorityPasswordEncoder;
	}

	public void setAuthorityPasswordEncoder(
			AuthorityPasswordEncoder authorityPasswordEncoder) {
		this.authorityPasswordEncoder = authorityPasswordEncoder;
	}

	public PasswordRule getPasswordRule() {
		return passwordRule;
	}

	public void setPasswordRule(PasswordRule passwordRule) {
		this.passwordRule = passwordRule;
	}

	public SysAccountDao getSysAccountDao() {
		return sysAccountDao;
	}

	public void setSysAccountDao(SysAccountDao sysAccountDao) {
		this.sysAccountDao = sysAccountDao;
	}

}
