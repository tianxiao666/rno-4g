package com.iscreate.op.service.selfservice;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysSecurityBizlog;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysSecurityBizlogService;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.system.util.AuthorityPasswordEncoder;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class SelfServiceImpl implements SelfService {

	Logger log = Logger.getLogger(this.getClass());
	// ----注入------//
	private HibernateTemplate hibernateTemplate;
	private SysSecurityBizlogService sysSecurityBizlogService;
	
	private SysAccountService sysAccountService;
	
	private SysOrgUserService sysOrgUserService;
	
	private AuthorityPasswordEncoder authorityPasswordEncoder;// 密码加密器
	
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}

	public void setSysSecurityBizlogService(
			SysSecurityBizlogService sysSecurityBizlogService) {
		this.sysSecurityBizlogService = sysSecurityBizlogService;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



	

	/**
	 * 修改密码
	 * 
	 * @param account
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public boolean saveAccountNewPassword(final String account,
			final String oldPassword, final String newPassword) {
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {
				if (AuthorityConstant.CheckPasswordResult.CHECKOK != sysAccountService
						.txCheckPassword(account, oldPassword)) {
					throw new UserDefinedException("原始密码不正确");
				}
				try {
					SysAccount sysAccountByAccount = sysAccountService.getSysAccountByAccount(account);
//					 加密
					 sysAccountByAccount.setPassword(authorityPasswordEncoder.encodePassword(newPassword));
//					sysAccountByAccount.setPassword(newPassword);
					sysOrgUserService.txUpdateAccount(sysAccountByAccount);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
//					throw new UserDefinedException(e.getMessage());
				}
			}

		});
	}

	public AuthorityPasswordEncoder getAuthorityPasswordEncoder() {
		return authorityPasswordEncoder;
	}

	public void setAuthorityPasswordEncoder(
			AuthorityPasswordEncoder authorityPasswordEncoder) {
		this.authorityPasswordEncoder = authorityPasswordEncoder;
	}

	public SysSecurityBizlogService getSysSecurityBizlogService() {
		return sysSecurityBizlogService;
	}
}
