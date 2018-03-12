package com.iscreate.op.service.networkresourcemanage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysAreaService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.tools.InterfaceUtil;

public class StaffOrganizationServiceImpl implements StaffOrganizationService {

	private  static final  Log log = LogFactory.getLog(StaffOrganizationServiceImpl.class);
	
	private ContextFactory contextFactory;
	private SysOrgUserService sysOrgUserService;
	private SysAreaService sysAreaService;
	private SysAccountService sysAccountService;
	
	
	
	
	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	public ContextFactory getContextFactory() {
		return contextFactory;
	}
	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}
	
	
	
	/**
	 * 根据人员ID获取人员所在组织的区域
	 * @param userId
	 * @return
	 */
	public List<BasicEntity> getAreaByUserId(String userId){
		//ou.jh
		List<Map<String,Object>> areaByAccount = this.sysAreaService.getAreaByAccount(userId);
//		List<String> list = this.providerOrganizationService.getOrgAreaByAccountForNetworkresourceService(userId);
		String areaIds = "";
		if(areaByAccount != null && !areaByAccount.isEmpty()){
			for(Map<String,Object> s:areaByAccount){
				areaIds = areaIds + s.get("AREA_ID") + ",";
			}
		}
		List<BasicEntity> uuIdList = null;
		if(areaIds != null && !areaIds.equals("")){
			areaIds = areaIds.substring(0,areaIds.length() - 1);
			String sql = "select "+ResourceCommon.getSelectSqlAttributsString("Sys_Area")+",Sys_Area.area_level  \"level\",Sys_Area.area_id  \"id\" from Sys_Area where area_id in ("+areaIds+")";
			Context context = contextFactory.CreateContext();
			SqlContainer uuIdSqlc = context.createSqlContainer(sql);
			uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		}
		return uuIdList;
	}

	/**
	 * 获取顶级区域节点集合
	 * @return
	 */
	public List<BasicEntity> getTopLevelAreaList(){
		log.info("进入===getTopLevelAreaList方法");
		String sql =" SELECT "+ResourceCommon.getSelectSqlAttributsString("Sys_Area")+",Sys_Area.area_level  \"level\",Sys_Area.area_id  \"id\" FROM Sys_Area "
					+" WHERE Sys_Area.area_level = '省' ";
		//System.out.println("sqlString:"+sql);
		Context context = contextFactory.CreateContext();
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		log.info("提出===getTopLevelAreaList方法 返回值为："+uuIdList);
		return uuIdList;
	}
	
	
	private String getProjectPrefixURL() {
		log.info("进入===getProjectPrefixURL方法");
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String uri = request.getRequestURI().toString();
//		String url = request.getRequestURL().toString();
//		return url.replace(uri, "").trim() + "/";
		String code = "";
		OutLinkingService outLinking = new OutLinkingService();
		code = outLinking.getUrlByProjService("ops");
		log.info("提出===getProjectPrefixURL方法 返回值为："+code);
		return code;
	}
	
	
	
	
	public Map<String,String> getUserByUserId(String userId){
		
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,Map<String,String>> resultMap = new HashMap<String, Map<String,String>>();
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
		SysAccount sysAccountByAccount = this.sysAccountService.getSysAccountByAccount(userId);
//		Account account = this.providerOrganizationService.getAccountByAccountId(userId);
		
		if(sysOrgUserByAccount!=null && sysAccountByAccount != null){
			Map<String,String> accountMap = new HashMap<String, String>();
			accountMap.put("account", sysAccountByAccount.getAccount());
			accountMap.put("name", sysOrgUserByAccount.getName());
			accountMap.put("emailAddress", sysOrgUserByAccount.getEmail());
			accountMap.put("backUpEmailAddress", sysOrgUserByAccount.getBackupemail());
			accountMap.put("mobileEmailAddress", sysOrgUserByAccount.getMobileemail());
			accountMap.put("cellPhoneNumber", sysOrgUserByAccount.getMobile());
			accountMap.put("enterpriseId", sysOrgUserByAccount.getEnterpriseId()+"");
			resultMap.put("Account", accountMap);
				Map<String,String> staffMap = new HashMap<String, String>();
				staffMap.put("name", sysOrgUserByAccount.getName());
				staffMap.put("sex", sysOrgUserByAccount.getGender());
				staffMap.put("account", sysAccountByAccount.getAccount());
				staffMap.put("identityCard", sysOrgUserByAccount.getIdcard());
				staffMap.put("contactPhone", sysOrgUserByAccount.getTel());
				staffMap.put("cellPhoneNumber", sysOrgUserByAccount.getMobile());
				//功能需要字段？ ou.jh
//				staffMap.put("age",sysOrgUserByAccount.getAge()+"");
//				staffMap.put("degree", staff.getDegree());
				staffMap.put("enterpriseId", sysOrgUserByAccount.getEnterpriseId()+"");
				resultMap.put("Staff", staffMap);
		}
		Map<String,String> result_map = null;
		if(resultMap != null){
			result_map = resultMap.get("Staff");
		}
		return result_map;
		
	}
	public SysAreaService getSysAreaService() {
		return sysAreaService;
	}
	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}
}
