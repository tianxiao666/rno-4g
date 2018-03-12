package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.informationmanage.EnterpriseInformationService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;

public class CardispatchForeignAction {
	
	/************* 依赖注入 *************/

	private EnterpriseInformationService enterpriseInformationService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	private SysOrgUserService sysOrgUserService;// ou.jh
	
	

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	/********* 属性 *********/
	private Log log = LogFactory.getLog(this.getClass());
	
	/**************** action ******************/
	
	/**
	 * 获取当前登录人组织集合
	 * (响应) 组织Id
	 */
	public void getLoginUserBiz () {
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> list =  this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		try {
			if ( list != null && list.size() > 0 ) {
				ActionUtil.responseWrite(list.get(0),false);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前登录人信息
	 */
	public void getLoginUserInfo () {
		//获取当前登录人的信息
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(accountId);
//		Staff staff = providerOrganizationService.getStaffByAccount(accountId);
		Map<String,String> userMap=null;
		if(sysOrgUserByAccount!=null){
			userMap = new HashMap<String,String>();
			userMap.put("name", sysOrgUserByAccount.getName());
			userMap.put("account", accountId);
		}
		
		try {
			//Map<String, String> map = ObjectUtil.object2MapString(userMap, false);
			ActionUtil.responseWrite(userMap,false);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取当前登录人 , 所在企业
	 */
	public void findLoginUserEnterprise () {
		//获取当前登录人的信息
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(accountId);
//		Staff staff = providerOrganizationService.getStaffByAccount(accountId);
		long enterpriseId = sysOrgUserByAccount.getEnterpriseId();
		InformationEnterprise enterprise = null;
		if ( enterpriseId != 0 ) {
			enterprise = this.enterpriseInformationService.get(enterpriseId);
		} else {
			String error_string = String.format("登录人:(%s),企业后缀为空!", accountId);
			log.error(error_string);
			enterprise = new InformationEnterprise();
		}
		try {
			ActionUtil.responseWrite(enterprise,false);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	public EnterpriseInformationService getEnterpriseInformationService() {
		return enterpriseInformationService;
	}
	public void setEnterpriseInformationService(
			EnterpriseInformationService enterpriseInformationService) {
		this.enterpriseInformationService = enterpriseInformationService;
	}
	
	
	
	
	
	
	
	
	
	
	
}
