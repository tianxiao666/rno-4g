package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.plat.system.util.AuthorityPasswordEncoder;

public class SysAccountDaoImpl  extends BaseDao implements SysAccountDao{
	private HibernateTemplate hibernateTemplate;
	private AuthorityPasswordEncoder authorityPasswordEncoder;
	

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public AuthorityPasswordEncoder getAuthorityPasswordEncoder() {
		return authorityPasswordEncoder;
	}

	public void setAuthorityPasswordEncoder(
			AuthorityPasswordEncoder authorityPasswordEncoder) {
		this.authorityPasswordEncoder = authorityPasswordEncoder;
	}
	
	/**
	 * 是否超级用户密码
	 * 
	 * @param session
	 * @param password
	 * @return
	 */
	public SysAccount isSuperAdminPassword(final String password) {
		if ((password == null) || ("".equals(password))) {
			return null;
		}
		String encodedPwd = password;
		if (this.authorityPasswordEncoder != null) {
			encodedPwd = this.authorityPasswordEncoder.encodePassword(password);
		}
		String hql = "select sa FROM SysAccount AS sa , SuperAdmin AS ss where sa.password = '"
				+ password + "' AND sa.account = ss.account";
		List list = hibernateTemplate.find(hql);
		if (list != null && !list.isEmpty()) {
			return (SysAccount) list.get(0);
		} else {
			return null;
		}

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
	public SysAccount getSysAccountByAccount(String account) {
		String hql = "from SysAccount s where s.account='" + account + "'";
		List<SysAccount> list = (List<SysAccount>) hibernateTemplate.find(hql);
		SysAccount sysAccount = null;
		if (list != null && list.size() > 0) {
			sysAccount = list.get(0);
		}
		return sysAccount;
	}
	/**
	 * 根据账号获取账号信息
	* @author du.hw
	* @createtime 2014-02-27
	* 通过账号获取账号和用户状态
	 */
	public Map<String,Object> getAccountInfoAndStatusByAccount(String account) {
		String sql = "select sa.*,sou.status from sys_account sa inner join sys_org_user sou on sou.org_user_id=sa.org_user_id where sa.account='" + account + "'";
		List<Map<String,Object>> list = this.executeSqlForList(sql, hibernateTemplate);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}else{
		    return null;
		}
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
	public SysAccount getSysAccountByOrgUserId(long orgUserId) {
		String hql = "select s from SysAccount s where s.orgUserId = "
				+ orgUserId;
		List list = hibernateTemplate.find(hql);
		if (list != null && list.size() > 0) {
			SysAccount sysAccount = (SysAccount) list.get(0);
			return sysAccount;
		} else {
			return null;
		}
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
	public SysAccount getSysRoleByOrgUserId(long orgUserId) {
		String hql = "select s from SysRole r, SysUserRelaRole a, SysAccount s where a.accountId = s.accountId and r.roleId = a.roleId and s.orgUserId = "
				+ orgUserId;
		List list = hibernateTemplate.find(hql);
		if (list != null && list.size() > 0) {
			SysAccount sysAccount = (SysAccount) list.get(0);
			return sysAccount;
		} else {
			return null;
		}
	}
}
