package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.system.SysSuperAdminDao;
import com.iscreate.plat.system.util.AuthorityPasswordEncoder;

/**
 * 超级用户信息Service
	 * 
	 * @author ou.jh
	 * @date Jun 20, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class SysSuperAdminServiceImpl implements SysSuperAdminService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private SysSuperAdminDao sysSuperAdminDao;
	
	private AuthorityPasswordEncoder authorityPasswordEncoder;// 密码加密器
	
	

	public AuthorityPasswordEncoder getAuthorityPasswordEncoder() {
		return authorityPasswordEncoder;
	}

	public void setAuthorityPasswordEncoder(
			AuthorityPasswordEncoder authorityPasswordEncoder) {
		this.authorityPasswordEncoder = authorityPasswordEncoder;
	}

	public SysSuperAdminDao getSysSuperAdminDao() {
		return sysSuperAdminDao;
	}

	public void setSysSuperAdminDao(SysSuperAdminDao sysSuperAdminDao) {
		this.sysSuperAdminDao = sysSuperAdminDao;
	}
	
	/**
	 * 根据密码获取超级用户信息
	* @author ou.jh
	* @date Jun 20, 2013 3:55:52 PM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<Map<String, Object>> getSysSuperAdminByPassword(String password){
		//加密密码
		String inputPwd = password;
		if (authorityPasswordEncoder != null) {
			inputPwd = authorityPasswordEncoder.encodePassword(password);
		}
		log.info("进入getSysSuperAdminByPassword方法");
		List<Map<String,Object>> sysSuperAdminByPassword = this.sysSuperAdminDao.getSysSuperAdminByPassword(inputPwd);
		log.info("执行getSysSuperAdminByPassword方法成功，实现了”根据密码获取超级用户信息“的功能");
		log.info("退出getSysSuperAdminByPassword方法,返回List<Map<String,Object>>"+sysSuperAdminByPassword);
		return sysSuperAdminByPassword;
	}
}
