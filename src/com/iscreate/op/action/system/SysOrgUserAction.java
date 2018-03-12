package com.iscreate.op.action.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;
import com.iscreate.op.pojo.system.SysUserRelaOrg;
import com.iscreate.op.pojo.system.SysUserRelaPost;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysDictionaryService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysUserRelaOrgService;
import com.iscreate.op.service.system.SysUserRelaPostService;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class SysOrgUserAction {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	//组织ID
	private long orgId;
	
	private int currentPage;
	
	private int pageSize;
	
	private long totalPage;
	
	private List<Map<String, Object>> staffList;
	
	private long enterpriseId;
	
	private String choiceAccountType;
	
	private SysOrgUserService sysOrgUserService;
	
	private long orgUserId;
	
	private String conditions;
	
	private String account;
	
	private String pinyin;
	
	private String orgJsonStr;
	
	private String orgType;
	
	private List<Map<String, Object>> orgList;
	
	
	private SysAccountService sysAccountService;
	
	private SysOrganizationService sysOrganizationService;
	
	private SysDictionaryService sysDictionaryService;
	
	private SysUserRelaPostService sysUserRelaPostService;
	
	private SysUserRelaOrgService sysUserRelaOrgService;
	
	private SysOrgUser sysOrgUser = new SysOrgUser();
	
	private SysAccount sysAccount = new SysAccount();
	
	private List<SysUserRelaPost> sysUserRelaPostList;
	
	private List<Map<String, Object>> sysUserRelaPostMapList;
	
	private Map<String, List<Map<String, Object>>> relaPostMap;
	
	
	public List<Map<String, Object>> getSysUserRelaPostMapList() {
		return sysUserRelaPostMapList;
	}


	public void setSysUserRelaPostMapList(
			List<Map<String, Object>> sysUserRelaPostMapList) {
		this.sysUserRelaPostMapList = sysUserRelaPostMapList;
	}


	public SysOrgUser getSysOrgUser() {
		return sysOrgUser;
	}


	public void setSysOrgUser(SysOrgUser sysOrgUser) {
		this.sysOrgUser = sysOrgUser;
	}


	public SysAccount getSysAccount() {
		return sysAccount;
	}


	public void setSysAccount(SysAccount sysAccount) {
		this.sysAccount = sysAccount;
	}


	public List<SysUserRelaPost> getSysUserRelaPostList() {
		return sysUserRelaPostList;
	}


	public void setSysUserRelaPostList(List<SysUserRelaPost> sysUserRelaPostList) {
		this.sysUserRelaPostList = sysUserRelaPostList;
	}


	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}


	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}


	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}


	public String getOrgJsonStr() {
		return orgJsonStr;
	}


	public void setOrgJsonStr(String orgJsonStr) {
		this.orgJsonStr = orgJsonStr;
	}


	public String getPinyin() {
		return pinyin;
	}


	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getConditions() {
		return conditions;
	}


	public void setConditions(String conditions) {
		this.conditions = conditions;
	}


	public long getOrgUserId() {
		return orgUserId;
	}


	public void setOrgUserId(long orgUserId) {
		this.orgUserId = orgUserId;
	}


	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	/**
	 * 根据组织Id获取人员
	 */
	public String getProviderSystemStaffByOrgIdAction(){
		log.info("进入getProviderSystemStaffByOrgIdAction方法");
		log.info("参数orgId="+this.orgId+",currentPage="+this.currentPage+",pageSize="+this.pageSize+",conditions="+this.conditions);
		String conditionsUTF = "";
		if(conditions != null && !conditions.equals("")){
			try {
				conditionsUTF = URLDecoder.decode(this.conditions,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				log.info(conditions+"编码转换失败");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else{
			conditionsUTF = "";
		}
		Map<String, Object> map = this.sysOrgUserService.getAccountListToPageByOrgIdService(this.orgId,this.currentPage,this.pageSize,conditionsUTF);
		this.totalPage = (Integer)map.get("totalPage");
		this.staffList = (List<Map<String, Object>>)map.get("staffList");
		log.info("执行getProviderStaffByOrgIdAction方法成功，实现了”根据组织Id获取人员(分页)“的功能");
		log.info("退出getProviderStaffByOrgIdAction方法,返回String为success");
		return "success";
	}
	

	/**
	 * 获取人员列表
	 * @return
	 */
	public String getProviderSystemStaffAction(){
		log.info("进入getProviderStaffAction方法");
		log.info("参数enterpriseId="+this.enterpriseId+",choiceAccountType="+this.choiceAccountType+",orgId="+this.orgId);
		String conditionsUTF = "";
		if(conditions != null && !conditions.equals("")){
			try {
				conditionsUTF = URLDecoder.decode(this.conditions,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				log.info(conditions+"编码转换失败");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else{
			conditionsUTF = "";
		}
		this.staffList = this.sysOrgUserService.getSysOrgUserByOrgIdService(enterpriseId, choiceAccountType, orgId, pinyin, conditionsUTF);
		log.info("执行getProviderStaffAction方法成功，实现了”根据企业Id、选择查询类型、组织Id获取人员列表“的功能");
		log.info("退出getProviderStaffAction方法,返回String为success");
		return "success";
	}
	
	/**
	 * 根据账号获取人员信息
	 * @return
	 */
	public void getProviderSystemAccountAjaxAction(){
		log.info("进入getProviderAccountAjaxAction方法");
		log.info("参数orgUserId="+this.orgUserId);
		Map<String, Object> sysOrgUser = this.sysOrgUserService.getProviderAccountByAccountIdAjaxService(this.orgUserId);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		HttpServletResponse response = ServletActionContext.getResponse();
		String result = gson.toJson(sysOrgUser);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getProviderAccountAjaxAction方法成功，实现了”根据账号获取人员信息“的功能");
		log.info("退出getProviderAccountAjaxAction方法,返回void");
	}
	
	
	/**
	 * 获取类型为“用户群”的角色
	* @date May 13, 2013 3:01:00 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getSysRoleByUserGroupAjaxAction(){
		log.info("进入getSysRoleByUserGroupAjaxAction方法");
		List<SysRoleType> allSysRoleType = this.sysOrgUserService.getAllSysRoleType();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		HttpServletResponse response = ServletActionContext.getResponse();
		String result = gson.toJson(allSysRoleType);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getSysRoleByUserGroupAjaxAction方法成功，实现了 获取类型为“用户群”的角色 的功能");
		log.info("退出getSysRoleByUserGroupAjaxAction方法,返回void");
	}
	
	/**
	 * 检查账号是否为可用
	* @date May 14, 2013 9:52:38 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void checkAccountAjaxAction(){
		log.info("进入checkAccountAjaxAction方法");
		int checkAccount = this.sysAccountService.txCheckAccount(this.account);
		String result = "error";
		if(AuthorityConstant.AccountStatus.AVAILABLE == checkAccount){
			//账号为可用
			result = "success";
		}else if(AuthorityConstant.AccountStatus.UNAVAILABLE == checkAccount){
			//账号为不可用
			result = "error";
		}else{
			//账号异常情况
			result = "error";
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行checkAccountAjaxAction方法成功，实现了 获取类型为“用户群”的角色 的功能");
		log.info("退出checkAccountAjaxAction方法,返回void");
	}
	
	/**
	 * 获取全部角色
	* @date May 14, 2013 3:46:59 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getAllSysRoleAction(){
		log.info("进入getAllSysRoleAction方法");
		List<SysRole> allSysRole = this.sysOrgUserService.getAllSysRole();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(allSysRole);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getAllSysRoleAction方法成功，实现了 获取全部角色 的功能");
		log.info("退出getAllSysRoleAction方法,返回void");
	}
	
	
	/**
	 * 修改人员信息与人员角色
	* @date May 14, 2013 5:27:35 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void updateStaffAndUserRoleAction(){
		log.info("进入updateStaffAndUserRoleAction方法");
		try {
			this.orgJsonStr = URLDecoder.decode(this.orgJsonStr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数orgJsonStr转码失败");
			throw new UserDefinedException("参数orgJsonStr转码失败");
		}
		String result = this.sysOrgUserService.txUpdateStaffAndUserRole(orgJsonStr);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行updateStaffAndUserRoleAction方法成功，实现了 修改人员信息与人员角色 的功能");
		log.info("退出updateStaffAndUserRoleAction方法,返回void");
	}
	
	/**
	 * 获取企业url的数据字典
	* @date May 21, 2013 11:12:24 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getSysEnterpriseUrlDictionaryAjaxAction(){
		log.info("进入getEnterpriseUrlDictionaryAjaxAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> listMap = this.sysOrgUserService.getEnterpriseUrlDictionaryService();
		String result = gson.toJson(listMap);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getEnterpriseUrlDictionaryAjaxAction方法成功，实现了”获取企业url的数据字典“的功能");
		log.info("退出getEnterpriseUrlDictionaryAjaxAction方法,返回void");
	}
	
	/**
	 * 创建人员信息与人员角色
	* @date May 21, 2013 1:37:39 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void createStaffAndUserRoleAction(){
		log.info("进入createStaffAndUserRoleAction方法");
		log.info("参数orgJsonStrt="+this.orgJsonStr);
		try {
			this.orgJsonStr = URLDecoder.decode(this.orgJsonStr,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数orgJsonStr转码失败");
			throw new UserDefinedException("参数orgJsonStr转码失败");
		}
		long result = this.sysOrgUserService.txSaveStaffAndUserRole(this.orgJsonStr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String resultString = gson.toJson(result);
		try {
			response.getWriter().write(resultString);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行createStaffAndUserRoleAction方法成功，实现了”创建人员信息与人员角色“的功能");
		log.info("退出createStaffAndUserRoleAction方法,返回void");
	}
	
	/**
	 * 解锁用户账号 
	* @author ou.jh
	* @date Aug 12, 2013 10:28:05 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void setNormalAccountAction(){
		log.info("进入setNormalAccountAction方法");
		log.info("参数account="+this.account);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		boolean flag = false;
		if(this.account != null){
			try {
				flag = this.sysAccountService.setNormalUser(this.account);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				flag = false;
			} finally {
				String resultString = gson.toJson(flag);
				try {
					response.getWriter().write(resultString);
				} catch (Exception e) {
					log.error("返回到状态时出错");
					throw new UserDefinedException("返回到状态时出错");
				}
			}
		}else{
			flag = false;
			String resultString = gson.toJson(flag);
			try {
				response.getWriter().write(resultString);
			} catch (Exception e) {
				log.error("返回到状态时出错");
				throw new UserDefinedException("返回到状态时出错");
			}
		}
		log.info("执行setNormalAccountAction方法成功，实现了”解锁用户账号“的功能");
		log.info("退出setNormalAccountAction方法,返回void");
	}
	
	
	
	/**
	 * 
	 * 根据组织ID获取人员名称
	 * @date 2013-08-23
	 * 
	 * @param orgId  组织ID
	 * @author Li.hb 
	 * @return
	 */
	public void getUserNameByOrgId()
	{
		List<Map<String, Object>> map = this.sysOrgUserService.getUserNameByOrgId(this.orgId+"");
		
		try {
			ActionUtil.responseWrite(map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 加载用户添加页面(v1.1.3)
	* @author ou.jh
	* @date Jan 9, 2014 3:38:13 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String loadAddOrgUserViewAction(){
		//获取iscreate公司于事业部列表 
		this.orgList = this.sysOrganizationService.getIscreateCompanyAndBusinessUnitList();
		return "success";
	}
	
	
	/**
	 * 根据组织类型获取组织对应的岗位(v1.1.3)
	* @author ou.jh
	* @date Jan 10, 2014 9:56:49 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getPostByOrgTypeAction(){
		List<Map<String,Object>> list = this.sysDictionaryService.getSecondSysDictionaryListByCode("'"+this.orgType+"'");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String resultString = gson.toJson(list);
		try {
			response.getWriter().write(resultString);
		} catch (Exception e) {
			log.error("返回到状态时出错");
			throw new UserDefinedException("返回到状态时出错");
		}
		
	}
	
	/**
	 * 新增用户与用户岗位(v1.1.3)
	* @author ou.jh
	* @date Jan 10, 2014 3:34:27 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void addOrgUserAndPostAction(){
		long orgUserId = this.sysOrgUserService.txSaveOrgUserAndAccount(sysOrgUser, sysAccount);
		Set<Long> orgIdSet = new HashSet<Long>();
		if(orgUserId > 0){
			//更新用户和岗位关系状态 
			int statusFlag = this.sysUserRelaPostService.updateSysUserRelaPostStatus("X", orgUserId);
			if(this.sysUserRelaPostList != null && this.sysUserRelaPostList.size() > 0)
			{
				for(SysUserRelaPost sysUserRelaPost:this.sysUserRelaPostList){
					if(sysUserRelaPost != null){
						orgIdSet.add(sysUserRelaPost.getOrg_id());
						//初始化保存用户和岗位关系状态(A正常，X不可用)
						sysUserRelaPost.setStatus("A");
						sysUserRelaPost.setOrg_user_id(orgUserId);
						//保存用户和岗位关系
						this.sysUserRelaPostService.saveSysUserRelaPost(sysUserRelaPost);
					}
				}
				this.sysUserRelaOrgService.updateSysUserRelaOrgStatus("X", orgUserId);
				for(long orgId:orgIdSet){
					SysUserRelaOrg so = new SysUserRelaOrg();
					so.setOrgId(orgId);
					so.setOrgUserId(orgUserId);
					so.setCreatetime(new Date());
					so.setStatus("A");
					this.sysUserRelaOrgService.saveSysUserRelaOrg(so);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		returnMap.put("orgUserId", orgUserId);
		String resultString = gson.toJson(returnMap);
		try {
			response.getWriter().write(resultString);
		} catch (Exception e) {
			log.error("返回到状态时出错");
			throw new UserDefinedException("返回到状态时出错");
		}
	}
	
	/**
	 * 加载用户编辑页面(v1.1.3)
	* @author ou.jh
	* @date Jan 9, 2014 3:38:13 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String loadUpdateOrgUserViewAction(){
		//获取iscreate公司于事业部列表 
		this.sysOrgUser = this.sysOrgUserService.getSysStaffByOrgUserId(orgUserId);
		this.sysAccount = this.sysAccountService.getSysAccountByOrgUserId(orgUserId);
		if(sysAccount != null && sysAccount.getAccount() != null && !sysAccount.getAccount().isEmpty()){
			this.account = sysAccount.getAccount().substring(0, sysAccount.getAccount().indexOf("@iscreate.com"));
		}
		this.orgList = this.sysOrganizationService.getIscreateCompanyAndBusinessUnitList();
		this.sysUserRelaPostMapList = this.sysUserRelaPostService.getSysUserRelaPostListByorgUserId(orgUserId);
		String codes = "";
		if(sysUserRelaPostMapList != null && sysUserRelaPostMapList.size() > 0){
			for(Map<String, Object> map:sysUserRelaPostMapList){
				if(map.get("POST_CODE") != null && !map.get("POST_CODE").toString().isEmpty()){
					codes = codes + "'" + map.get("ORG_TYPE") + "',";
				}
			}
			if(codes != null && !codes.equals("")){
				codes = codes.substring(0, codes.length() - 1);
				List<Map<String,Object>> list = this.sysDictionaryService.getSecondSysDictionaryListByCode(codes);
				if(list != null && list.size() > 0){
					this.relaPostMap = new HashMap<String, List<Map<String,Object>>>();
					for(Map<String,Object> m:list){
						//部门类型code
						String tcode = m.get("THIS_CODE")+"";
						//岗位code
						String code = m.get("CODE")+"";
						String name = m.get("NAME")+"";
						if(this.relaPostMap.containsKey(tcode)){
							Map<String, Object> postMap = new HashMap<String, Object>();
							postMap.put("code", code);
							postMap.put("name", name);
							this.relaPostMap.get(tcode).add(postMap);
						}else{
							List<Map<String, Object>> postList = new ArrayList<Map<String,Object>>();
							Map<String, Object> postMap = new HashMap<String, Object>();
							postMap.put("code", code);
							postMap.put("name", name);
							postList.add(postMap);
							this.relaPostMap.put(tcode,postList);
						}
					}
				}
			}
		}
		return "success";
	}
	
	/**
	 * 编辑用户与用户岗位(v1.1.3)
	* @author ou.jh
	* @date Jan 9, 2014 3:38:13 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void updateOrgUserAndPostAction(){
		long orgUserId = this.sysOrgUserService.txUpdateOrgUserAndAccount(sysOrgUser, sysAccount);
		Set<Long> orgIdSet = new HashSet<Long>();
		if(orgUserId > 0){
			//更新用户和岗位关系状态 
			int statusFlag = this.sysUserRelaPostService.updateSysUserRelaPostStatus("X", orgUserId);
			if(this.sysUserRelaPostList != null && this.sysUserRelaPostList.size() > 0)
			{
				for(SysUserRelaPost sysUserRelaPost:this.sysUserRelaPostList){
					if(sysUserRelaPost != null){
						orgIdSet.add(sysUserRelaPost.getOrg_id());
						//初始化保存用户和岗位关系状态(A正常，X不可用)
						sysUserRelaPost.setStatus("A");
						sysUserRelaPost.setOrg_user_id(orgUserId);
						//保存用户和岗位关系
						this.sysUserRelaPostService.saveSysUserRelaPost(sysUserRelaPost);
					}
				}
				this.sysUserRelaOrgService.updateSysUserRelaOrgStatus("X", orgUserId);
				for(long orgId:orgIdSet){
					SysUserRelaOrg so = new SysUserRelaOrg();
					so.setOrgId(orgId);
					so.setOrgUserId(orgUserId);
					so.setCreatetime(new Date());
					so.setStatus("A");
					this.sysUserRelaOrgService.saveSysUserRelaOrg(so);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		returnMap.put("orgUserId", orgUserId);
		String resultString = gson.toJson(returnMap);
		try {
			response.getWriter().write(resultString);
		} catch (Exception e) {
			log.error("返回到状态时出错");
			throw new UserDefinedException("返回到状态时出错");
		}
	}

	public long getOrgId() {
		return orgId;
	}


	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}


	public long getCurrentPage() {
		return currentPage;
	}


	


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public long getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}




	public List<Map<String, Object>> getStaffList() {
		return staffList;
	}


	public void setStaffList(List<Map<String, Object>> staffList) {
		this.staffList = staffList;
	}


	public long getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public String getChoiceAccountType() {
		return choiceAccountType;
	}


	public void setChoiceAccountType(String choiceAccountType) {
		this.choiceAccountType = choiceAccountType;
	}


	public List<Map<String, Object>> getOrgList() {
		return orgList;
	}


	public void setOrgList(List<Map<String, Object>> orgList) {
		this.orgList = orgList;
	}


	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}


	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}


	public String getOrgType() {
		return orgType;
	}


	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}


	public Map<String, List<Map<String, Object>>> getRelaPostMap() {
		return relaPostMap;
	}


	public void setRelaPostMap(Map<String, List<Map<String, Object>>> relaPostMap) {
		this.relaPostMap = relaPostMap;
	}


	public SysUserRelaPostService getSysUserRelaPostService() {
		return sysUserRelaPostService;
	}


	public void setSysUserRelaPostService(
			SysUserRelaPostService sysUserRelaPostService) {
		this.sysUserRelaPostService = sysUserRelaPostService;
	}


	public SysUserRelaOrgService getSysUserRelaOrgService() {
		return sysUserRelaOrgService;
	}


	public void setSysUserRelaOrgService(SysUserRelaOrgService sysUserRelaOrgService) {
		this.sysUserRelaOrgService = sysUserRelaOrgService;
	}







}
