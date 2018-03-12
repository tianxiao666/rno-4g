package com.iscreate.plat.login.controller;

import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.system.SysPermissionService;
import com.iscreate.op.service.system.SysOrgUserService;

public class AccessHandler {

	SysOrgUserService sysOrgUserService;

	SysPermissionService sysPermissionService;

	

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public void setSysPermissionService(
			SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}

	public boolean canAccessResource(String account, String uri) {
		return sysPermissionService.txCanAccessResource(account, uri);
	}

	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}



}
