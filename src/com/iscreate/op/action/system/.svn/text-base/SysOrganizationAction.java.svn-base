package com.iscreate.op.action.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysAreaService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.login.constant.UserInfo;

public class SysOrganizationAction {
	private static Log log = LogFactory.getLog(SysOrganizationAction.class);
	private SysOrganizationService sysOrganizationService;
	private SysAreaService sysAreaService;
	private String enterpriseIds;//企业ids
	private String enterpriseId;
	private String enterpriseType;//企业类型
	private SysOrgUserService sysOrgUserService;
	private String dictionaryType;//所要获取字典的类型
	
	private long orgId;//组织id
	
	private String orgType;//组织类型
	
	private Map<String,String> map;
	
	private String orgJsonStr;
	
	private String account;//账号
	
	private String pageType;//跳转页面类型 server 服务商 operator 客户商
	
	private String orgUserId;//组织人员id
	
	private String pro;//参数判断获取session中的值
	/**
	 * 获取登陆人所属的企业
	 */
	public void getLoginIdBelongEnterpriseTypeAjaxAction(){
		log.info("进入getLoginIdBelongEnterpriseTypeAjaxAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String account = "";
		if("PM".equals(pro)){//项目管理调用
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.PM_USERID);
		}else{
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.USERID);
		}
		
		Map<String,String> map = this.sysOrganizationService.getLoginIdBelongEnterpriseTypeService(account);
		String result = gson.toJson(map);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getLoginIdBelongEnterpriseTypeAjaxAction方法成功，实现了”获取登陆人所属的企业“的功能");
		log.info("退出getLoginIdBelongEnterpriseTypeAjaxAction方法,返回void");
	}
	
	
	
	
	/**
	 * 获取所有最高级组织
	 */
	public void getAllTheTopProviderOrgAction(){
		String json="";
		Gson gson=new Gson();
		List<Map<String, String>> allCustomerTheTopOrgService = this.sysOrganizationService.getAllTheTopProviderOrgService(this.enterpriseType);
		json=gson.toJson(allCustomerTheTopOrgService);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做获取所有最高级组织的操作时返回页面信息出错");
		}
		
	}
	
	/**
	 * 获取多个组织树
	 */
	public void getAllProviderOrgTreeAction(){
		this.sysOrganizationService.getProviderOrgTreeDownwardByEnterpriseIdAjaxService(this.enterpriseIds,this.enterpriseType);//hardcode
	}
	/**
	 * 
	 * @description: 根据所传类型获取组织相关数据字典
	 * @author：     
	 * @return void     
	 * @date：May 8, 2013 11:47:53 AM
	 */
	public void getDictionaryByTypeAction(){
		log.info("进入getDictionaryByTypeAction方法");
		this.sysOrganizationService.getDictionaryAjaxService(this.dictionaryType);
		log.info("执行getDictionaryByTypeAction方法成功，实现了”根据所传类型获取组织相关数据字典“的功能");
		log.info("退出getDictionaryByTypeAction方法,返回void");
	}
	
	
	//------------------组织基本信息
	/**
	 * 根据组织Id显示组织架构
	 */
	public String showProviderOrgByOrgIdAction(){
		log.info("进入showProviderOrgByOrgIdAction方法");
		log.info("传入的参数orgId="+this.orgId);
		log.info("执行showProviderOrgByOrgIdAction方法成功，实现了”根据组织Id显示组织架构“的功能");
		log.info("退出showProviderOrgByOrgIdAction方法,返回String为success");
		return "success";
	}
	
	/**getAllTheTopOrgByOrgIdService
	 * 根据组织Id获取组织
	 */
	public String getProviderOrgByOrgIdAction(){
		log.info("进入getProviderOrgByOrgIdAction方法");
		log.info("参数orgId="+this.orgId);
		this.map = this.sysOrganizationService.getProviderOrgByOrgIdtoMapService(this.orgId);
		if(this.map==null || this.map.size()==0){
			log.info("获取的组织为空");
		}else{
			log.info("成功获取组织");
		}
		log.info("执行getProviderOrgByOrgIdAction方法成功，实现了”根据组织Id获取组织“的功能");
		log.info("退出getProviderOrgByOrgIdAction方法,返回String为success");
		return this.pageType;
	}
	/**getAllTheTopOrgByOrgIdService
	 * 根据组织Id获取所有上级组织
	 */
	public void getAllTopOrgByOrgIdAction(int orgId){
		log.info("getAllTopOrgByOrgIdAction");
		log.info("参数org_id="+this.orgId);
		List<Map<String, Object>> list = this.sysOrganizationService.getAllTheTopOrgByOrgIdService(this.orgId);
		if(list==null || list.size()==0){
			log.info("获取的组织为空");
		}else{
			log.info("成功获取组织");
		}
		log.info("执行getAllTopOrgByOrgIdAction方法成功，实现了”根据组织Id获取组织“的功能");
		log.info("退出getAllTopOrgByOrgIdAction方法,返回String为success");
		String json="";
		Gson gson=new Gson();
		json=gson.toJson(list);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("获取当前组织的所有上级组织返回页面信息出错");
		}
	}
	
	/**
	 * 获取所有没有选择过的企业
	 */
	public void getAllNoChoiceEnterpriseAjaxAction(){
		try {
			this.orgType = URLDecoder.decode(this.orgType,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数orgType转码失败");
			throw new UserDefinedException("参数orgType转码失败");
		}
		String json="";
		Gson gson=new Gson();
		List<Map<String, String>> list = this.sysOrganizationService.getAllNoChoiceEnterpriseService(this.orgType);
		json=gson.toJson(list);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做获取所有没有选择过的企业的操作时返回页面信息出错");
		}
	}
	
	/**
	 * ajax保存组织信息
	 */
	public void saveProviderOrgInfoAjaxAction(){
		log.info("进入saveProviderOrgInfoAjaxAction方法");
		log.info("参数orgJsonStr="+this.orgJsonStr);
		try {
			this.orgJsonStr = URLDecoder.decode(this.orgJsonStr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数orgJsonStr转码失败");
			throw new UserDefinedException("参数orgJsonStr转码失败");
		}
		this.sysOrganizationService.txSaveProviderOrgInfoAjaxService(this.orgJsonStr);
		log.info("执行saveProviderOrgInfoAjaxAction方法成功，实现了”ajax保存组织信息“的功能");
		log.info("退出saveProviderOrgInfoAjaxAction方法,返回void");
	}
	
	/**
	 * ajax修改组织信息
	 */
	public void updateProviderOrgInfoAjaxAction(){
		log.info("进入updateProviderOrgInfoAjaxAction方法");
		log.info("参数orgJsonStr="+this.orgJsonStr);
		try {
			this.orgJsonStr = URLDecoder.decode(this.orgJsonStr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数orgJsonStr转码失败");
			throw new UserDefinedException("参数orgJsonStr转码失败");
		}
		this.sysOrganizationService.txUpdateProviderOrgInfoAjaxService(this.orgJsonStr);
		log.info("执行updateProviderOrgInfoAjaxAction方法成功，实现了”ajax修改组织信息“的功能");
		log.info("退出updateProviderOrgInfoAjaxAction方法,返回void");
	}
	
	/**
	 * ajax删除组织信息
	 */
	public void deleteProviderOrgInfoAjaxAction(){
		log.info("进入deleteProviderOrgInfoAjaxAction方法");
		log.info("参数orgId="+this.orgId);
		this.sysOrganizationService.txDeleteProviderOrgInfoAjaxService(this.orgId);
		log.info("执行deleteProviderOrgInfoAjaxAction方法成功，实现了”ajax删除组织信息“的功能");
		log.info("退出deleteProviderOrgInfoAjaxAction方法,返回void");
	}
	
	/**
	 * ajax根据账号获取可操作组织架构的权限
	 */
	public void getAuthorityByAccountAjaxAction(){
		log.info("进入getAuthorityByAccountAjaxAction方法");
		String userId = "";
		if("PM".equals(pro)){//项目管理调用
			userId = (String) SessionService.getInstance().getValueByKey(UserInfo.PM_USERID);
		}else{
			userId = (String) SessionService.getInstance().getValueByKey(UserInfo.USERID);
		}
		if(userId==null || "".equals(userId)){
			log.info("获取session里的账号为空");
		}else{
			this.sysOrganizationService.getAuthorityByAccountAjaxService(userId);
			log.info("执行getAuthorityByAccountAjaxAction方法成功，实现了”ajax根据账号获取可操作组织架构的权限“的功能");
		}
		log.info("退出getAuthorityByAccountAjaxAction方法,返回void");
	}
	
	/**
	 * 获取最高级组织
	 */
	public void getTheTopProviderOrgAjaxAction(){
		log.info("进入getTheTopProviderOrgAjaxAction方法");
		String account = "";
		if("PM".equals(pro)){//项目管理调用
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.PM_USERID);
		}else{
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.USERID);
		}
		this.sysOrganizationService.getTheTopProviderOrgAjaxService(account);
		log.info("执行getTheTopProviderOrgAjaxAction方法成功，实现了”获取服务商最高级组织“的功能");
		log.info("退出getTheTopProviderOrgAjaxAction方法,返回void");
	}
	
	/**
	 * 根据组织Id获取人员列表数据字典
	 */
	public void getAccountDictionaryByOrgIdAjaxAction(){
		log.info("进入getAccountDictionaryByOrgIdAjaxAction方法");
		log.info("参数orgId="+this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<SysOrgUser> list = this.sysOrganizationService.getUserListByOrgIdService(this.orgId);
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		if(list!=null && list.size()>0){
			for (SysOrgUser staff : list) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("account", staff.getOrgUserId()+"");
				map.put("name", staff.getName());
				listMap.add(map);
			}
		}
		String result = gson.toJson(listMap);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getAccountDictionaryByOrgIdAjaxAction方法成功，实现了”根据组织Id获取账号列表“的功能");
		log.info("退出getAccountDictionaryByOrgIdAjaxAction方法,返回void");
	}
	
	//---------外部接口
	/**
	 * 根据人获取最高级的组织
	 */
	public void getTheTopOrgByAccountAction(){
		log.info("进入getTheTopOrgByAccountAction方法");
		String account = "";
		if("PM".equals(pro)){//项目管理调用
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.PM_USERID);
		}else{
			account = (String) SessionService.getInstance().getValueByKey(UserInfo.USERID);
		}
		if(account==null || "".equals(account)){
			log.info("获取session的用户账号为空");
		}else{
			this.sysOrganizationService.getTopLevelOrgByAccountAjaxService(account);
			log.info("执行getTheTopOrgByAccountAction方法成功，实现了”根据人获取最高级的组织“的功能");
		}
		log.info("退出getTheTopOrgByAccountAction方法,返回void");
	}

	/**
	 * 根据账号获取该账号以下的组织集合
	 */
	public void getOrgTreeDownwardByAccountAction(){
		log.info("进入getOrgTreeDownwardByAccountAction方法");
		log.info("传入的参数account="+this.account);
		this.sysOrganizationService.getOrgTreeDownwardByAccountAjaxService(this.account);
		log.info("执行getOrgTreeDownwardByAccountAction方法成功，实现了”根据账号获取该账号以下的组织集合“的功能");
		log.info("退出getOrgTreeDownwardByAccountAction方法,返回void");
	}
	
//组织树==================================================================================
	
	/**
	 * ajax根据组织ID向下获取组织树
	 */
	public void getProviderOrgTreeByOrgIdAction(){
		log.info("进入getProviderOrgTreeByOrgIdAction方法");
		log.info("参数orgId="+this.orgId);
		//获取登录人orgUserId
		long orgUserId = Long.parseLong(SessionService.getInstance().getValueByKey(UserInfo.PM_ORG_USER_ID)+"");
		//根据人员id获取人员角色(String) 
		String sysRoles = this.sysOrgUserService.getUserRolesStringByUserId(orgUserId);
		
		if(sysRoles.indexOf("PM_Financial_Accounting")>-1||sysRoles.indexOf("PM_Financial_Manager")>-1||sysRoles.indexOf("PM_President")>-1)
		{
			this.sysOrganizationService.getProviderOrgTreeDownwardByorgIdsAjaxService(16+"");
		}
		else
		{
			this.sysOrganizationService.getProviderOrgTreeDownwardByorgIdsAjaxService(orgId+"");
		}
		
		
		
		log.info("执行getProviderOrgTreeByOrgIdAction方法成功，实现了”ajax根据组织ID向下获取组织树“的功能");
		log.info("退出getProviderOrgTreeByOrgIdAction方法,返回void");
	}
	
	/**
	 * ajax根据企业Id获取该企业的组织树
	 */
	public void getProviderOrgTreeByEnterpriseIdAjaxAction(){
		log.info("进入getProviderOrgTreeByEnterpriseIdAjaxAction方法");
		log.info("参数enterpriseId="+this.enterpriseId);
		this.sysOrganizationService.getProviderOrgTreeDownwardByEnterpriseIdAjaxService(enterpriseId,this.enterpriseType);
		log.info("执行getProviderOrgTreeByEnterpriseIdAjaxAction方法成功，实现了”ajax根据企业Id获取该企业的组织树“的功能");
		log.info("退出getProviderOrgTreeByEnterpriseIdAjaxAction方法,返回void");
	}
	
	
	//----------------
	/**
	 * 获取全部区域树
	 */
	public void getAreaTreeAjaxAction(){
		log.info("进入getAreaTreeAjaxAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String, Object>> list = this.sysAreaService.getAreaTreeList(0,0);
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getAreaTreeAjaxAction方法成功，实现了”获取全部区域树“的功能");
		log.info("退出getAreaTreeAjaxAction方法,返回void");
	}
	
	//---组织与人员关联操作
	/**
	 * ajax根据人员ID删除改账号与组织的关系
	 */
	public void deleteProiderAccountAndOrgAjaxAction(){
		log.info("进入deleteProiderAccountAndOrgAjaxAction方法");
		log.info("参数orgUserId="+this.orgUserId+",orgId="+this.orgId);
		this.sysOrganizationService.txDeleteOrgStaffAjaxService(this.orgUserId,this.orgId);
		log.info("执行deleteProiderAccountAndOrgAjaxAction方法成功，实现了”ajax根据人员ID删除改人员与组织的关系（服务商）“的功能");
		log.info("退出deleteProiderAccountAndOrgAjaxAction方法,返回void");
	}
	
	/**
	 * 创建组织与人员的关系
	 */
	public void createOrgStaffAction(){
		log.info("进入createOrgStaffAction方法");
		log.info("参数orgId="+this.orgId+",orgUserId="+this.orgUserId);
		this.sysOrganizationService.addOrgStaffAjaxService(this.orgId, this.orgUserId);
		log.info("执行createOrgStaffAction方法成功，实现了”创建组织与人员的关系“的功能");
		log.info("退出createOrgStaffAction方法,返回void");
	}
	
	//test action
	
	public void testOrganizationAction(){
		log.info("进入getAreaTreeAjaxAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(this.sysOrganizationService.getOrgListMapDownwardByOrg(orgId));
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getAreaTreeAjaxAction方法成功，实现了”获取全部区域树“的功能");
		log.info("退出getAreaTreeAjaxAction方法,返回void");
	}
	
	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}
	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	public String getEnterpriseIds() {
		return enterpriseIds;
	}

	public void setEnterpriseIds(String enterpriseIds) {
		this.enterpriseIds = enterpriseIds;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getDictionaryType() {
		return dictionaryType;
	}

	public void setDictionaryType(String dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getOrgJsonStr() {
		return orgJsonStr;
	}

	public void setOrgJsonStr(String orgJsonStr) {
		this.orgJsonStr = orgJsonStr;
	}




	public String getEnterpriseId() {
		return enterpriseId;
	}




	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}




	public String getAccount() {
		return account;
	}




	public void setAccount(String account) {
		this.account = account;
	}




	public String getPageType() {
		return pageType;
	}




	public void setPageType(String pageType) {
		this.pageType = pageType;
	}




	public SysAreaService getSysAreaService() {
		return sysAreaService;
	}




	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}




	public String getOrgUserId() {
		return orgUserId;
	}




	public void setOrgUserId(String orgUserId) {
		this.orgUserId = orgUserId;
	}




	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}




	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
}
