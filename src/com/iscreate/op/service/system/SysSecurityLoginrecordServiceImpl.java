package com.iscreate.op.service.system;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.dao.system.SysSecurityLoginrecordDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysSecurityLoginrecord;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysRoleService;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.opensymphony.xwork2.ActionContext;

public class SysSecurityLoginrecordServiceImpl implements SysSecurityLoginrecordService{

	Logger logger = Logger.getLogger(this.getClass());
	private SysSecurityLoginrecordDao sysSecurityLoginrecordDao;
	private SysOrgUserService sysOrgUserService;
	private SysRoleService sysRoleService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	/**
	 * 获取最后一次的登陆记录
	 * @param userId
	 * @return
	 */
	public SysSecurityLoginrecord getSysSecurityLoginrecordService(String userId){
		SysSecurityLoginrecord sysSecurityLoginrecord = null;
		List<SysSecurityLoginrecord> sysSecurityLoginrecordByUserId = this.sysSecurityLoginrecordDao.getSysSecurityLoginrecordByUserId(userId);
		if(sysSecurityLoginrecordByUserId!=null && sysSecurityLoginrecordByUserId.size()>0){
			//获取最后一次的登陆记录
			sysSecurityLoginrecord = sysSecurityLoginrecordByUserId.get(0);
		}
		return sysSecurityLoginrecord;
	}
	
	/**
	 * 保存登陆记录（PC）
	 * @param userId
	 * @return
	 */
	public boolean saveSysSecurityLoginrecordToPCByUserIdService(String userId){
		boolean isTrue = false;
		try {
			if(userId==null || "".equals(userId)){
				logger.debug("参数：userId为空");
				return isTrue;
			}
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			SysSecurityLoginrecord saveLR = new SysSecurityLoginrecord();
			saveLR.setEquipmentType("1");
			saveLR.setIp(request.getRemoteAddr());
			saveLR.setUserId(userId);
			saveLR.setExplor(request.getHeader("User-agent"));
			saveLR.setLoginTime(new Date());
			//获取该账号的最后一次登陆记录
			SysSecurityLoginrecord lastLoginRecordService = this.getSysSecurityLoginrecordService(userId);
			if(lastLoginRecordService==null){
				this.sysSecurityLoginrecordDao.saveSysSecurityLoginrecord(saveLR);
				isTrue = true;
			}else{
				Date lastLoginTime = lastLoginRecordService.getLoginTime();
				saveLR.setLastLoginTime(lastLoginTime);
				this.sysSecurityLoginrecordDao.saveSysSecurityLoginrecord(saveLR);
				isTrue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			return isTrue;
		}
	}
	
	/**
	 * 保存登陆记录（终端）
	 * @param userId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public boolean saveSysSecurityLoginrecordToMobileByUserIdService(String userId,double longitude,double latitude){
		boolean isTrue = false;
		try {
			if(userId==null || "".equals(userId)){
				logger.debug("参数：userId为空");
				return isTrue;
			}
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			SysSecurityLoginrecord saveLR = new SysSecurityLoginrecord();
			saveLR.setEquipmentType("2");
			saveLR.setIp(request.getRemoteAddr());
			saveLR.setUserId(userId);
			saveLR.setExplor(request.getHeader("User-agent"));
			saveLR.setLoginTime(new Date());
			saveLR.setLongitude(longitude);
			saveLR.setLatitude(latitude);
			//获取该账号的最后一次登陆记录
			SysSecurityLoginrecord lastLoginRecordService = this.getSysSecurityLoginrecordService(userId);
			if(lastLoginRecordService==null){
				this.sysSecurityLoginrecordDao.saveSysSecurityLoginrecord(saveLR);
				isTrue = true;
			}else{
				Date lastLoginTime = lastLoginRecordService.getLoginTime();
				saveLR.setLastLoginTime(lastLoginTime);
				this.sysSecurityLoginrecordDao.saveSysSecurityLoginrecord(saveLR);
				isTrue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			return isTrue;
		}
	}
	
	/**
	 * 修改登出时间
	 * @param userId
	 * @return
	 */
	public boolean updateSysSecurityLoginrecordByUserIdService(String userId){
		boolean isTrue = false;
		try {
			if(userId==null || "".equals(userId)){
				logger.debug("参数：userId为空");
				return isTrue;
			}
			//获取该账号的最后一次登陆记录
			SysSecurityLoginrecord lastLoginRecordService = this.getSysSecurityLoginrecordService(userId);
			if(lastLoginRecordService==null){
				logger.debug("userId="+userId+"没有登陆的记录");
				isTrue = false;
			}else{
				lastLoginRecordService.setLastLogoutTime(new Date());
				this.sysSecurityLoginrecordDao.updateSysSecurityLoginrecord(lastLoginRecordService);
				isTrue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			return isTrue;
		}
	}

	/**
	 * 获取登陆记录信息
	 * @param userId
	 * @return
	 */
	public Map<String,String> getSysSecurityLoginrecordInfoService(String userId){
		Map<String,String> map = new HashMap<String, String>();
		//积分
		map.put("integral", "50");
		if(userId!=null && !"".equals(userId)){
			//获取该账号最高级组织
			//List<ProviderOrganization> topLevelOrgByAccount = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			if(topLevelOrgByAccount!=null && topLevelOrgByAccount.size()>0){
				map.put("orgName", topLevelOrgByAccount.get(0).getName());
			}
			//获取该账号的组织角色
			//ou.jh
			List<SysRole> userRoles = this.sysRoleService.getUserRoles(userId);
//			List<Role> orgRoleToRoleByAccountService = this.providerOrganizationService.getOrgRoleToRoleByAccountService(userId);
			if(userRoles!=null && userRoles.size()>0){
				map.put("orgRoleName", userRoles.get(0).getName());
			}
			//获取该账号的名字
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//			Account accountByAccountId = this.providerOrganizationService.getAccountByAccountId(userId);
			if(sysOrgUserByAccount!=null){
				map.put("name", sysOrgUserByAccount.getName());
			}
			//获取登陆次数
			List<SysSecurityLoginrecord> loginRecordByUserId = this.sysSecurityLoginrecordDao.getSysSecurityLoginrecordByUserId(userId);
			if(loginRecordByUserId!=null && loginRecordByUserId.size()>0){
				map.put("loginCount", loginRecordByUserId.size()+"");
				int count = loginRecordByUserId.size();
				if(count<=1){
					map.put("lastLoginTime", "");
					map.put("ip", "");
				}else{
					Date lastLoginTime = loginRecordByUserId.get(1).getLoginTime();
					if(lastLoginTime==null){
						map.put("lastLoginTime", "");
					}else{
						map.put("lastLoginTime", TimeFormatHelper.getTimeFormatByMin(lastLoginTime));
					}
					map.put("ip", loginRecordByUserId.get(1).getIp());
				}
			}else{
				map.put("loginCount", "0");
				map.put("lastLoginTime", "");
				map.put("ip", "");
			}
		}
		return map;
	}
	



	public SysSecurityLoginrecordDao getSysSecurityLoginrecordDao() {
		return sysSecurityLoginrecordDao;
	}

	public void setSysSecurityLoginrecordDao(
			SysSecurityLoginrecordDao sysSecurityLoginrecordDao) {
		this.sysSecurityLoginrecordDao = sysSecurityLoginrecordDao;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}
	
}
