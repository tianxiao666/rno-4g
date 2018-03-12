package com.iscreate.op.action.selfservice;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.dao.system.SysAccountDao;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.selfservice.SelfService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.sso.session.UserInfo;

public class SelfServiceAction {

	Logger log = Logger.getLogger(this.getClass());
	// ---注入-----//
	private SelfService selfService;

	private SysAccount sysAccount = new SysAccount();
	
	private SysOrgUser sysOrgUser = new SysOrgUser();
	
	private SysOrgUserService sysOrgUserService;
	
	private SysAccountService sysAccountService;
	
	private SysAccountDao sysAccountDao;
	
	private String oldPassword, newPassword;

	public SelfService getSelfService() {
		return selfService;
	}

	public void setSelfService(SelfService selfService) {
		this.selfService = selfService;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String loadSelfServiceHomePageAction(){
		return "success";
	}
	
	/**
	 * 加载账号信息页面
	 * 
	 * @return
	 */
	public String loadSelfServiceInfoViewAndEditAction() {
		String userId = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(UserInfo.CAS_FILTER_USERID);
		this.sysOrgUser = sysOrgUserService.getSysOrgUserByAccount(userId);
		this.sysAccount = sysAccountService.getSysAccountByAccount(userId);
		//account = selfService.getAccountDetail(userId);
		return "success";
	}

	/**
	 * 加载密码修改页面
	 * 
	 * @return
	 */
	public String loadSelfServiceChangePasswordAction() {
		return "success";
	}
	
	/**
	 * 修改账号基本信息，不包括密码
	 * @throws IOException 
	 */
	public void saveSelfServiceInfoForAjaxAction() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		boolean ok =false;
		try{
			sysOrgUserService.txUpdateSysOrgUser(sysOrgUser);
			Long orgUserId = sysOrgUser.getOrgUserId();
			SysAccount sysAccountByAccount = sysAccountDao.getSysAccountByOrgUserId(orgUserId);
			sysAccountByAccount.setAccount(sysAccount.getAccount());
			sysOrgUserService.txUpdateAccount(sysAccountByAccount);
			ok = true;
//			ok=selfService.modifyAccountInfo(account);
			response.getWriter().write("{'flag':"+ok+",'msg':''}");
		}catch(Exception e){
			response.getWriter().write("{'flag':false,'msg':'修改失败'}");
		}
	}

	/**
	 * 修改密码 
	 * --------------------------------- 
	 * 输入      oldPassword旧密码
	 *       newPassword新密码
	 * 输出      json
	 * ---------------------------------
	 * @throws IOException 
	 */
	public void saveNewAccountPasswordForAjaxAction() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		
		String userId = (String) ServletActionContext.getRequest().getSession()
		.getAttribute(UserInfo.CAS_FILTER_USERID);
		
		boolean ok =false;
		try{
			ok=selfService.saveAccountNewPassword(userId, oldPassword, newPassword);
			response.getWriter().write("{'flag':"+ok+"}");
		}catch(UserDefinedException e){
			log.error(e.getMsg());
			response.getWriter().write("{'flag':false,'msg':'"+e.getMsg()+"'}");
		}
	}

	
	public SysAccount getSysAccount() {
		return sysAccount;
	}

	public void setSysAccount(SysAccount sysAccount) {
		this.sysAccount = sysAccount;
	}

	public SysOrgUser getSysOrgUser() {
		return sysOrgUser;
	}

	public void setSysOrgUser(SysOrgUser sysOrgUser) {
		this.sysOrgUser = sysOrgUser;
	}

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

	public SysAccountDao getSysAccountDao() {
		return sysAccountDao;
	}

	public void setSysAccountDao(SysAccountDao sysAccountDao) {
		this.sysAccountDao = sysAccountDao;
	}
}
