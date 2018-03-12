package com.iscreate.op.action.system;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysPermissionType;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleRelaPermission;
import com.iscreate.op.pojo.system.SysRoleType;
import com.iscreate.op.service.system.SysDictionaryService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysPermissionService;
import com.iscreate.op.service.system.SysRoleService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TreeListHelper;

/**
 * 系统管理
 * 
 * @author sunny
 * 
 */
public class SystemManageAction {
	private Log log = LogFactory.getLog(this.getClass());
	private final String pageKind = "page", functionKind = "function",
			processKind = "process";

	// ----注入-----//
	/** 新权限注入* */
	private SysPermissionService sysPermissionService;
	private SysRoleService sysRoleService;
	private SysOrgUserService sysOrgUserService;
	private SysUserRelaPermissionService sysUserRelaPermissionService;
	// 成员变量

	// ----页面变量----//
	private String subPageName, tabVal, resourceKind;
	private String pageResourceTree, functionResourceTree, processResourceTree;


	private String superResourceTypeName, superResourceTypeCode, moduleCode,
			superResourceCode, resource_type_code;
	private long superResourceId, resourceId;

	// 角色相关
	private final String roleTypeCode_userGroupRole = "usergrouprole",pmRoleTypeCode_orgRole = "PM_orgrole",iosmRoleTypeCode_orgRole = "IOSM_orgrole",rnoRoleTypeCode_orgRole = "RNO_orgrole",roleTypeCode_businessRole = "businessrole";
	private long role_type_id, super_role_id, role_ass_org_id,
			role_ass_businessmodule_id;

	List<SysRoleType> usergroupRoles;
	private List<SysRole> orgRoles, bizRoles,iosmOrgRoles,rnoOrgRoles;

	// 角色列表html代码
	private String usergroupRoleUlStr, orgRoleUlStr,rnoOrgRoleUlStr,iosmOrgRoleUlStr,businessRoleUlStr;
	// 组织角色、业务角色tab下的用户群下拉框html
	private String orgrole_usergroupStr, businessrole_usergroupStr;
	// 组织角色tab下的组织级别下拉框html
	private String orgrole_orgtemplateStr;

	// 业务角色tab下的“业务模块”下拉框html
	private String businessrole_bizmoduleStr;
	// 各角色tab下新增角色区域的角色类型的select的html
	private String usergrouproleRoleTypeStr, orgroleRoleTypeStr,
			businessroleRoleTypeStr;

	// 操作的角色kind
	private String operRoleKind;
	private long role_id;
	// 授权时选择的操作的id
	private String oper_ids;// 以逗号分隔

	// 账号管理相关
	private String userId;


	private SysPermission sysPermission = new SysPermission();

	private SysRole sysRole = new SysRole();
	private SysRoleRelaPermission sysRoleRelaPermission = new SysRoleRelaPermission() ;
	private String parentId;// 权限类型父id
	private String permissionId;// 权限id
	private String roleId;// 角色Id
	private String perAccess;//权限
	
	
	private String permissionIds;// 权限id字符串
	
	//yuan.yw
	private SysDictionaryService sysDictionaryService;
	private String systemCodes;
	private List<Map<String,Object>> systemList;

	public String getPerAccess() {
		return perAccess;
	}

	public void setPerAccess(String perAccess) {
		this.perAccess = perAccess;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public String getSuperResourceTypeName() {
		return superResourceTypeName;
	}

	public void setSuperResourceTypeName(String superResourceTypeName) {
		this.superResourceTypeName = superResourceTypeName;
	}

	public String getSuperResourceTypeCode() {
		return superResourceTypeCode;
	}

	public void setSuperResourceTypeCode(String superResourceTypeCode) {
		this.superResourceTypeCode = superResourceTypeCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}


	public String getSubPageName() {
		return subPageName;
	}

	public void setSubPageName(String subPageName) {
		this.subPageName = subPageName;
	}

	public String getPageResourceTree() {
		return pageResourceTree;
	}

	public void setPageResourceTree(String pageResourceTree) {
		this.pageResourceTree = pageResourceTree;
	}

	public String getFunctionResourceTree() {
		return functionResourceTree;
	}

	public void setFunctionResourceTree(String functionResourceTree) {
		this.functionResourceTree = functionResourceTree;
	}

	public String getProcessResourceTree() {
		return processResourceTree;
	}

	public void setProcessResourceTree(String processResourceTree) {
		this.processResourceTree = processResourceTree;
	}

	

	public String getTabVal() {
		return tabVal;
	}

	public void setTabVal(String tabVal) {
		this.tabVal = tabVal;
	}

	public String getResourceKind() {
		return resourceKind;
	}

	public void setResourceKind(String resourceKind) {
		this.resourceKind = resourceKind;
	}

	public String getSuperResourceCode() {
		return superResourceCode;
	}

	public void setSuperResourceCode(String superResourceCode) {
		this.superResourceCode = superResourceCode;
	}

	public String getResource_type_code() {
		return resource_type_code;
	}

	public void setResource_type_code(String resourceTypeCode) {
		resource_type_code = resourceTypeCode;
	}

	public long getSuperResourceId() {
		return superResourceId;
	}

	public void setSuperResourceId(long superResourceId) {
		this.superResourceId = superResourceId;
	}

	
	public String getUsergroupRoleUlStr() {
		return usergroupRoleUlStr;
	}

	public void setUsergroupRoleUlStr(String usergroupRoleUlStr) {
		this.usergroupRoleUlStr = usergroupRoleUlStr;
	}

	public String getOrgRoleUlStr() {
		return orgRoleUlStr;
	}

	public void setOrgRoleUlStr(String orgRoleUlStr) {
		this.orgRoleUlStr = orgRoleUlStr;
	}

	public String getBusinessRoleUlStr() {
		return businessRoleUlStr;
	}

	public void setBusinessRoleUlStr(String businessRoleUlStr) {
		this.businessRoleUlStr = businessRoleUlStr;
	}

	public long getRole_type_id() {
		return role_type_id;
	}

	public void setRole_type_id(long roleTypeId) {
		role_type_id = roleTypeId;
	}

	public long getSuper_role_id() {
		return super_role_id;
	}

	public void setSuper_role_id(long superRoleId) {
		super_role_id = superRoleId;
	}

	public long getRole_ass_org_id() {
		return role_ass_org_id;
	}

	public void setRole_ass_org_id(long roleAssOrgId) {
		role_ass_org_id = roleAssOrgId;
	}

	public long getRole_ass_businessmodule_id() {
		return role_ass_businessmodule_id;
	}

	public void setRole_ass_businessmodule_id(long roleAssBusinessmoduleId) {
		role_ass_businessmodule_id = roleAssBusinessmoduleId;
	}

	public List<SysRole> getOrgRoles() {
		return orgRoles;
	}

	public void setOrgRoles(List<SysRole> orgRoles) {
		this.orgRoles = orgRoles;
	}

	public List<SysRole> getBizRoles() {
		return bizRoles;
	}

	public void setBizRoles(List<SysRole> bizRoles) {
		this.bizRoles = bizRoles;
	}

	public String getUserGroupRole() {
		return roleTypeCode_userGroupRole;
	}

	public String getOrgrole_usergroupStr() {
		return orgrole_usergroupStr;
	}

	public void setOrgrole_usergroupStr(String orgroleUsergroupStr) {
		orgrole_usergroupStr = orgroleUsergroupStr;
	}

	public String getBusinessrole_usergroupStr() {
		return businessrole_usergroupStr;
	}

	public void setBusinessrole_usergroupStr(String businessroleUsergroupStr) {
		businessrole_usergroupStr = businessroleUsergroupStr;
	}

	public String getBusinessrole_bizmoduleStr() {
		return businessrole_bizmoduleStr;
	}

	public void setBusinessrole_bizmoduleStr(String businessroleBizmoduleStr) {
		businessrole_bizmoduleStr = businessroleBizmoduleStr;
	}

	public String getOrgrole_orgtemplateStr() {
		return orgrole_orgtemplateStr;
	}

	public void setOrgrole_orgtemplateStr(String orgroleOrgtemplateStr) {
		orgrole_orgtemplateStr = orgroleOrgtemplateStr;
	}

	public String getUsergrouproleRoleTypeStr() {
		return usergrouproleRoleTypeStr;
	}

	public void setUsergrouproleRoleTypeStr(String usergrouproleRoleTypeStr) {
		this.usergrouproleRoleTypeStr = usergrouproleRoleTypeStr;
	}

	public String getOrgroleRoleTypeStr() {
		return orgroleRoleTypeStr;
	}

	public void setOrgroleRoleTypeStr(String orgroleRoleTypeStr) {
		this.orgroleRoleTypeStr = orgroleRoleTypeStr;
	}

	public String getBusinessroleRoleTypeStr() {
		return businessroleRoleTypeStr;
	}

	public void setBusinessroleRoleTypeStr(String businessroleRoleTypeStr) {
		this.businessroleRoleTypeStr = businessroleRoleTypeStr;
	}


	public String getOperRoleKind() {
		return operRoleKind;
	}

	public void setOperRoleKind(String operRoleKind) {
		this.operRoleKind = operRoleKind;
	}

	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long roleId) {
		role_id = roleId;
	}

	public String getOper_ids() {
		return oper_ids;
	}

	public void setOper_ids(String operIds) {
		oper_ids = operIds;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	/**
	 * 初始化系统管理首页
	 * 
	 * @return
	 */
	public String initSystemManageHomePageAction() {
		// 准备角色管理信息
		initRoleManageInfoAction();
		//yuan.yw  系统code
		this.systemList = this.sysDictionaryService.getChildDictionaryListByCode("System");
		return "success";
	}


	/**
	 * 得到和js的编码一致的结果
	 * 
	 * @param component
	 * @param charset
	 * @return
	 */
	public static String encodeURIComponent(String component, String charset) {
		String result = null;
		if (component == null) {
			return "null";
		}
		try {
			result = java.net.URLEncoder.encode(component, charset).replaceAll(
					"\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\+",
					"%20").replaceAll("\\%27", "'").replaceAll("\\%21", "!")
					.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			result = component;
		}

		return result;
	}



	/**
	 * 初始化角色管理信息
	 */
	private void initRoleManageInfoAction() {
		// 形成role的列表
		formRolePageStr();

	}

	/**
	 * 初始化角色管理需要的信息
	 */
	private void formRolePageStr() {
		// 准备用户群列表
		usergroupRoles = sysRoleService.getAllRoleType();
		// 准备角色列表
		orgRoles = sysRoleService.getAllRoleByProCode("PM");
		orgRoleUlStr = formRoleUlStr("role_userGroups_ul",pmRoleTypeCode_orgRole, orgRoles);
		
		
		iosmOrgRoles = sysRoleService.getAllRoleByProCode("IOSM");
		iosmOrgRoleUlStr = formRoleUlStr("role_userGroups_ul",iosmRoleTypeCode_orgRole, iosmOrgRoles);
		
		rnoOrgRoles = sysRoleService.getAllRoleByProCode("RNO");
		rnoOrgRoleUlStr = formRoleUlStr("role_userGroups_ul",rnoRoleTypeCode_orgRole, rnoOrgRoles);
		
		

		// 形成组织角色、业务角色tab下的“用户群”下来框
		orgrole_usergroupStr = formUserGroupListBoxStr(pmRoleTypeCode_orgRole,usergroupRoles);
	
	}

	

	/**
	 * 角色列表的界面
	 * 
	 * @param ulClass
	 *            生成的ul标签的class值
	 * @param clickParam
	 *            携带的参数
	 * @param roles
	 *            待转换的role列表
	 * @return
	 */
	private String formRoleUlStr(String ulClass, String clickParam,List<SysRole> roles) {
		StringBuilder buf = new StringBuilder();
		buf.append("<ul id=\"" + clickParam + "_role_ul\" class=\"" + ulClass
				+ "\">");
		if (roles != null && !roles.isEmpty()) {
			for (int i = 0; i < roles.size(); i++) {
				SysRole role = roles.get(i);
				formRoleLiStr(clickParam, role);
				buf.append(formRoleLiStr(clickParam, role));
			}
		}
		buf.append("</ul>");
		return buf.toString();
	}

	/**
	 * 生成role的li html
	 * 
	 * @param clickParam
	 * @param role
	 * @return
	 */
	private String formRoleLiStr(String clickParam, SysRole role) {
		if (role == null) {
			return "";
		} else {
			return "<li id=\"" + clickParam + "_role_" + role.getRoleId()
					+ "\" onclick=chooseRole(this,\"" + clickParam + "\",\""
					+ role.getRoleId() + "\",\""
					+ encodeURIComponent(role.getCode(), "utf-8") + "\",\""
					+ encodeURIComponent(role.getName(), "utf-8") + "\",\""+role.getProCode()+"\")>"
					+ role.getName() +"(" + role.getCode() + ")</li>";
		}
	}

	/**
	 * 构建用户群下拉框列表
	 * 
	 * @param ownRoleType
	 * @param usergrouproles
	 * @return
	 */
	private String formUserGroupListBoxStr(String ownRoleType,
			List<SysRoleType> usergrouproles) {
		StringBuilder buf = new StringBuilder();
		buf
				.append("<select id='usergroup_roleType' onchange='changeUsergroupRole()'>");
		if (usergrouproles != null && !usergrouproles.isEmpty()) {
			for (SysRoleType r : usergrouproles) {
				buf.append("<option value=\"" + r.getRoleTypeId() + "\">"
						+ r.getName() + "</>");
			}
		}
		buf.append("</select>");
		return buf.toString();
	}



	/**
	 * 获取某类型的角色
	 * 
	 * @throws IOException
	 */
	public void getRolesByTypeForAjaxAction() throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		List<SysRole> roles = null;
		if (role_ass_org_id + "" != null) {
			roles = sysRoleService.getRolesByRoleTypeCode(role_ass_org_id);
		} else {
			response.getWriter().write("{'flag':false,'msg':'不存在角色类型'}");
			return;
		}

		StringBuilder buf = new StringBuilder();
		if (roles != null && !roles.isEmpty()) {
			for (SysRole r : roles) {
				buf.append(formRoleLiStr("orgrole", r));
			}
		}
		// System.out.println("roles size =
		// "+(roles==null?0:roles.size())+",return li:"+buf.toString());

		// 输出
		response.getWriter().write(
				"{'flag':true,'msg':'" + buf.toString() + "'}");

	}



	/** *************************** 新权限 ************************** */

	/**
	 * ajax初始化权限树
	 */
	public void ajaxInitPermissionTreeAction() {
		log.info("进入ajaxInitPermissionTreeAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,Object> resultMap = null;
		try {
			if(this.systemCodes!=null && !this.systemCodes.isEmpty()){//系统项目 code
				resultMap = new HashMap<String,Object>();
				String[] sysCodesArray = this.systemCodes.split(",");
				for(String systemCode:sysCodesArray){
					List<Map<String, Object>> list = sysPermissionService
					.getPermissionListByProCodeAndType(systemCode,systemCode+"_MenuResource");//获取权限
					list = TreeListHelper.getTreeListByDataList(list);//树形结构
					resultMap.put(systemCode, list);//结果map
				}
			}
			String result = gson.toJson(resultMap);

			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			throw new UserDefinedException("返回到jsp页面时出错");
		}

		log.info("执行ajaxInitPermissionTreeAction方法成功，实现了”获取权限树“的功能");
		log.info("退出ajaxInitPermissionTreeAction方法,返回void");

	}

	/**
	 * ajax根据角色初始化权限树
	 */
	public void ajaxInitPermissionTreeByRoleAction() {
		log.info("进入ajaxInitPermissionTreeByRoleAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<Map<String, Object>> list = sysPermissionService.getPermissionTreeByRole(Long.parseLong(roleId));
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			throw new UserDefinedException("返回到jsp页面时出错");
		}

		log.info("执行ajaxInitPermissionTreeByRoleAction方法成功，实现了”获取权限树“的功能");
		log.info("退出ajaxInitPermissionTreeByRoleAction方法,返回void");

	}
	
	
	/**
	 * 根据角色获取该角色对应的权限
	 * @author li.hb
	 * @date 2014-1-14 上午9:42:17
	 * @Description: TODO 
	 * @param         
	 * @throws
	 */
	public void ajaxLoadPermissionTreeByRoleAction()
	{
		List<Map<String, Object>> list = sysPermissionService.getPermissionTreeByRole(Long.parseLong(roleId));

		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * ajax修改权限信息
	 * yuan.yw 2014-01-14
	 */
	public void ajaxModifyPermissionAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if (sysPermission == null) {
			throw new UserDefinedException("没找到需要修改的权限数据！");
		}
		if (sysPermission.getPermissionId() == null) {
			throw new UserDefinedException("没找到需要修改的权限数据！");
		}
		try {
			long pid = sysPermission.getPermissionId();
			SysPermission permission = sysPermissionService
					.getPermissionPmdevById(pid);
			permission.setCode(sysPermission.getCode());
			permission.setEnalbed(sysPermission.getEnalbed());
			permission.setUrl(sysPermission.getUrl());
			permission.setParameter(sysPermission.getParameter());
			permission.setName(sysPermission.getName());
			permission.setTitle(sysPermission.getTitle());
			permission.setIsShowWorkplat(sysPermission.getIsShowWorkplat());
			permission.setModTime(new Date());
			permission.setNote(sysPermission.getNote());
			sysPermissionService.txUpdatePermission(permission);//更新
			try {
				response.getWriter().write("{'flag':true}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':修改权限失败}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}

	/**
	 * ajax添加权限
	 * yuan.yw 2014-01-14
	 */
	public void ajaxAddPermissionAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		if (sysPermission == null) {
			throw new UserDefinedException("没找到需要保存的权限数据！");
		}
		try {
			sysPermission.setCreateTime(new Date());
			String path="";
			if(sysPermission.getParentId()!=null){
				SysPermission parentPermission = sysPermissionService
				.getPermissionPmdevById(sysPermission.getParentId());
				if(parentPermission!=null){
					path = parentPermission.getPath();//上级路径
				}
			}
			
			long perId = sysPermissionService.txAddPermission(sysPermission);//保存
			if("".equals(path)){//组装path
				path = "/"+perId+"/";
			}else{
				path += perId+"/";	
			}
			SysPermission curPermission = sysPermissionService
			.getPermissionPmdevById(perId);
			curPermission.setPath(path);
			sysPermissionService.txUpdatePermission(curPermission);//更新
			try {
				response.getWriter().write("{'flag':true}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':添加权限失败}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}

	/**
	 * ajax根据上级权限类型ID获取下级全部权限类型
	 */
	public void ajaxGetPermissionType() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if (parentId == null || "".endsWith(parentId)) {
			throw new UserDefinedException("获取权限类型出错！");
		}
		try {
			List<SysPermissionType> list = sysPermissionService
					.getPermissionTypeByParentId(Long.parseLong(parentId));
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}
	
	/**
	 * ajax获取第一级全部权限类型
	 */
	public void ajaxGetRootPermissionType() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		try {
			List<SysPermissionType> list = sysPermissionService.getRootPermissionType();
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}

	/**
	 * 删除指定资源
	 * 
	 * @throws IOException
	 */
	public void ajaxDeletePermissionAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		try {

//			sysPermissionService.txDeletePermissionById(Long
//					.parseLong(permissionId));
			/*SysPermission permission = sysPermissionService.getPermissionById(Long
					.parseLong(permissionId));
			sysPermissionService.txDeletepermission(permission);*/
			//yuan.yw 2014-01-14
			/*sysPermission permission = sysPermissionService.getPermissionPmdevById(Long
					.parseLong(permissionId));*/
			if(permissionId!=null&&!"".equals(permissionId)){
				//删除权限及子权限
				this.sysPermissionService.detletSelfAndChildPermissionByPermissionId(Long.valueOf(permissionId));
				//删除用户权限关联关系
				this.sysUserRelaPermissionService.deleteUserRelaPermissionByPermissionId(Long.valueOf(permissionId));
				//删除角色关联权限关系
				this.sysRoleService.deleteRoleRelaPermissionByPermissionId(Long.valueOf(permissionId));
			}
			response.getWriter().write("{'flag':'success','msg':''}");
		} catch (UserDefinedException e) {
			log.error(e.getMsg());
			try {
				response.getWriter().write(
						"{'flag':false,'msg':'" + e.getMsg() + "'}");
			} catch (IOException e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}

	/**
	 * author：wu_jn 
	 * createTime:2013/5/14 删除指定角色
	 * ajax保存角色
	 */
	public void ajaxAddRoleAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		if (sysRole == null) {
			throw new UserDefinedException("没找到需要保存的角色数据！");
		}
		try {
			sysRole.setCreatetime(new Date());
			sysRoleService.txAddRole(sysRole);
			try {
				response.getWriter().write(
						"{'flag':true,'roleId':"+sysRole.getRoleId()+",'msg':'"
								+ formRoleLiStr(sysRole.getProCode()+"_orgrole", sysRole)
								+ "','obj':{'id':'" + sysRole.getRoleId()
								+ "','code':'" + sysRole.getCode()
								+ "','name':'" + sysRole.getName() + "'}}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':'添加角色失败'}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}

	/**
	 * author：wu_jn 
	 * createTime:2013/5/14 删除指定角色
	 * ajax修改角色
	 */
	public void ajaxModifyRoleAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		if (sysRole == null) {
			throw new UserDefinedException("没找到需要修改的角色数据！");
		}
		try {
			long roleId = sysRole.getRoleId();
			SysRole role = sysRoleService.getRoleById(roleId);
			role.setCode(sysRole.getCode());
			role.setName(sysRole.getName());
			role.setUpdatetime(new Date());
			role.setRoleTypeId(sysRole.getRoleTypeId());
			sysRoleService.txUpdateRole(role);
			try {
				response.getWriter().write(
						"{'flag':true,'msg':'" + formRoleLiStr(sysRole.getProCode()+"_orgrole", role)
								+ "'}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':'修改角色失败'}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}

	/**
	 * author：wu_jn 
	 * createTime:2013/5/14 
	 * 删除指定角色
	 * 
	 * @throws IOException
	 */
	public void ajaxDeleteRoleAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		try {

			sysRoleService.txDeleteRoleById(Long.parseLong(roleId));
			
			
			sysPermissionService.deleteRoleRelaPermissionByRoleId(Long.parseLong(roleId));
			
			response.getWriter().write("{'flag':true,'msg':'删除成功'}");
		} catch (UserDefinedException e) {
			log.error(e.getMsg());
			try {
				response.getWriter().write(
						"{'flag':false,'msg':'" + e.getMsg() + "'}");
			} catch (IOException e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}

	/**
	 * author：wu_jn 
	 * createTime:2013/5/14 ajax
	 * 保存角色权限关联表
	 *//*
	public void ajaxSaveRoleRelaPermissionAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		if (sysRoleRelaPermission == null) {
			throw new UserDefinedException("没找到需要保存的角色权限关联数据！");
		}
		try {
			sysPermissionService.deleteRoleRelaPermissionById(Long.parseLong(permissionId),Long.parseLong(roleId));
			//权限不为空的时候添加权限
			if(perAccess != null && !perAccess.equals("")){
				sysRoleRelaPermission = new SysRoleRelaPermission();
				sysRoleRelaPermission.setPermissionId(Long.parseLong(permissionId));
				sysRoleRelaPermission.setRoleId(Long.parseLong(roleId));
				sysRoleRelaPermission.setPerAccess(perAccess);
				sysRoleRelaPermission.setAuthType("permit");
				sysRoleRelaPermission.setCreatetime(new Date());
				sysPermissionService.txAddRoleRelaPermission(sysRoleRelaPermission);
			}
			try {
				response.getWriter().write("{'flag':true,'msg':'保存授权成功'}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':'添加角色失败'}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}*/
	
	
	
	/**
	 * 批量保存角色权限关联表
	 * @author li.hb
	 * @date 2014-1-13 下午3:14:06
	 * @Description: TODO 
	 * @param         
	 * @throws
	 */
	public void ajaxSaveRoleRelaPermissionsAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		
		try {
			//先删除该角色下的所有权限关联关系
			sysPermissionService.deleteRoleRelaPermissionByRoleId(Long.parseLong(roleId));
			//权限不为空的时候添加权限
			if(permissionIds != null && !permissionIds.equals(""))
			{
				String[] permissionId = permissionIds.split(",");
				
				if(permissionId !=null && permissionId.length>0)
				{
					for(int i=0;i<permissionId.length;i++)
					{						
						sysRoleRelaPermission = new SysRoleRelaPermission();
						sysRoleRelaPermission.setPermission_id(Long.parseLong(permissionId[i]));
						sysRoleRelaPermission.setRole_id(Long.parseLong(roleId));
						sysRoleRelaPermission.setStatus("A");
						sysRoleRelaPermission.setCreate_time(new Date());
						sysPermissionService.txAddRoleRelaPermission(sysRoleRelaPermission);
					}
				}
				
				
			}
			try {
				response.getWriter().write("{'flag':true,'msg':'保存授权成功'}");
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			try {
				response.getWriter().write("{'flag':false,'msg':'添加角色失败'}");
			} catch (Exception e1) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		}
	}
	
	

		/**
		 * 
		 * @description: 获取用户名，账号和默认角色
		 * @author：zhang.wy1
		 * @return
		 * @return: String          
		 * @date：2014-1-15 上午11:58:44
		 */
	public String getUserRoleAndPermissionAction(){
		HttpServletRequest  request=  ServletActionContext.getRequest();
		String orgUserId = request.getParameter("orgUserId");
		Map<String,Object> account = new HashMap<String, Object>();
		SysOrgUser orgUser = new SysOrgUser();
		//获取账号和姓名
		account = sysOrgUserService.getAccountByOrgUserId(Long.valueOf(orgUserId));
		orgUser = sysOrgUserService.getSysOrgUserByAccount(account.get("ACCOUNT").toString());		
		//根据用户和系统得到角色
		List<Map<String,Object>>  Roles1 = sysRoleService.getSystemsByUserAndSystem(orgUser.getOrgUserId(), "PM");
		List<Map<String,Object>>  Roles2 = sysRoleService.getSystemsByUserAndSystem(orgUser.getOrgUserId(), "IOSM");
		List<Map<String,Object>>  Roles3 = sysRoleService.getSystemsByUserAndSystem(orgUser.getOrgUserId(), "RNO");
		

		request.setAttribute("account", account);
		request.setAttribute("orgUser", orgUser);
		request.setAttribute("Roles1", Roles1);
		request.setAttribute("Roles2", Roles2);
		request.setAttribute("Roles3", Roles3);
		//根据系统得到角色
		//List<SysRole>  sysRoles= sysRoleService.getrRolesBySystem(systemCode);
		//获取该系统下得所有菜单权限
		//通过角色获取默认开通的权限
		return "success";
	}
	
	
		/**
		 * 
		 * @description: 初始化权限树
		 * @author：zhang.wy1
		 * @return: void          
		 * @date：2014-1-15 上午11:58:12
		 */
	public void ajaxInitPermissionTreeByUserAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String orgUserId = ServletActionContext.getRequest().getParameter("orgUserId");
		String system = ServletActionContext.getRequest().getParameter("system");
		//获取用户和角色的关联的权限
		List<Map<String,Object>> permissions= sysUserRelaPermissionService.getPermissionListByUserId(Long.valueOf(orgUserId), system, system+"_MenuResource", false);
		//构造成树形结构
		List<Map<String,Object>> permissionsTree =TreeListHelper.getTreeListByDataList(permissions);
		String result = gson.toJson(permissionsTree);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}
	/**
	 * ajax根据角色获得权限 
	 */
	public void ajaxInitPermissionTreeByRolesAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String roleids = ServletActionContext.getRequest().getParameter("roleids");
		//根据角色获得对应的权限
		String permissionIds = sysPermissionService.getPermissionIdsByRoleIds(roleids);
		try {
			String result = gson.toJson(permissionIds);
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
		} catch (Exception e) {
			throw new UserDefinedException("返回到jsp页面时出错");
		}
	}
	
		/**
		 * 
		 * @description: 对用户设置角色和权限
		 * @author：zhang.wy1
		 * @return: void          
		 * @date：2014-1-15 下午12:20:29
		 */
	public void setUserRelaPermissionAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		//用户
		String orgUserId = ServletActionContext.getRequest().getParameter("orgUserId");
	    //角色
		String roleids = ServletActionContext.getRequest().getParameter("roleids");		
		//权限
		String permissionids = ServletActionContext.getRequest().getParameter("permissionids");
		int result =0;
		try {
		if(orgUserId!=null&&!orgUserId.equals("")){			
			//设置用户角色
			sysRoleService.updateUserRole(Long.valueOf(orgUserId), roleids);
			//设置用户权限
			result= sysPermissionService.updateUserPermission(Long.valueOf(orgUserId), permissionids);
		}		
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				response.getWriter().write(result);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}

	public void setSysPermissionService(
			SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}

	public SysPermission getSysPermission() {
		return sysPermission;
	}

	public void setSysPermission(SysPermission sysPermission) {
		this.sysPermission = sysPermission;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	public SysRoleRelaPermission getSysRoleRelaPermission() {
		return sysRoleRelaPermission;
	}

	public void setSysRoleRelaPermission(
			SysRoleRelaPermission sysRoleRelaPermission) {
		this.sysRoleRelaPermission = sysRoleRelaPermission;
	}
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}

	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}
    
    
	public String getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}

	public String getSystemCodes() {
		return systemCodes;
	}

	public void setSystemCodes(String systemCodes) {
		this.systemCodes = systemCodes;
	}

	public List<Map<String, Object>> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<Map<String, Object>> systemList) {
		this.systemList = systemList;
	}

	public List<SysRole> getIosmOrgRoles() {
		return iosmOrgRoles;
	}

	public void setIosmOrgRoles(List<SysRole> iosmOrgRoles) {
		this.iosmOrgRoles = iosmOrgRoles;
	}

	public List<SysRole> getRnoOrgRoles() {
		return rnoOrgRoles;
	}

	public void setRnoOrgRoles(List<SysRole> rnoOrgRoles) {
		this.rnoOrgRoles = rnoOrgRoles;
	}

	public String getRnoOrgRoleUlStr() {
		return rnoOrgRoleUlStr;
	}

	public void setRnoOrgRoleUlStr(String rnoOrgRoleUlStr) {
		this.rnoOrgRoleUlStr = rnoOrgRoleUlStr;
	}

	public String getIosmOrgRoleUlStr() {
		return iosmOrgRoleUlStr;
	}

	public void setIosmOrgRoleUlStr(String iosmOrgRoleUlStr) {
		this.iosmOrgRoleUlStr = iosmOrgRoleUlStr;
	}

	public String getPmRoleTypeCode_orgRole() {
		return pmRoleTypeCode_orgRole;
	}

	public String getIosmRoleTypeCode_orgRole() {
		return iosmRoleTypeCode_orgRole;
	}

	public String getRnoRoleTypeCode_orgRole() {
		return rnoRoleTypeCode_orgRole;
	}



	public SysUserRelaPermissionService getSysUserRelaPermissionService() {
		return sysUserRelaPermissionService;
	}

	public void setSysUserRelaPermissionService(
			SysUserRelaPermissionService sysUserRelaPermissionService) {
		this.sysUserRelaPermissionService = sysUserRelaPermissionService;
	}
	
}