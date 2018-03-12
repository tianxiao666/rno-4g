package com.iscreate.op.action.home;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.dao.system.SysPermissionDao;
import com.iscreate.op.pojo.home.HomeItem;
import com.iscreate.op.pojo.home.HomeItemRelaRole;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.service.home.HomeItemService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.plat.home.ComparatorList;
import com.iscreate.sso.session.UserInfo;

public class HomeItemAction {
	private HomeItemService homeItemService;
	
	private SysOrgUser sysOrgUser;
	
	private SysOrgUserService sysOrgUserService;
	
	private SysPermissionDao sysPermissionDao;
	
	private SysUserRelaPermissionService sysUserRelaPermissionService;
	
	private List<Map<String, Object>> permissionModuleList;
	
	private List<Map<String, Object>> menuLinks;
	
	private String json;
	
	private boolean flag;
	
	private Log log = LogFactory.getLog(this.getClass());

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
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

	public HomeItemService getHomeItemService() {
		return homeItemService;
	}

	public void setHomeItemService(HomeItemService homeItemService) {
		this.homeItemService = homeItemService;
	}
	
	/**
	 * 登录我的首页
	* @author ou.jh
	* @date Jun 5, 2013 2:24:48 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String userIndexAction(){
		log.info("进入userIndexAction方法");
		//从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
		System.out.println(request.getSession().getAttribute(com.iscreate.plat.login.constant.UserInfo.ORG_USER_ID));
		long orgUserId =Long.parseLong(request.getSession().getAttribute(com.iscreate.plat.login.constant.UserInfo.ORG_USER_ID).toString());
		//根据登录人账号获取用户信息 
		this.sysOrgUser = this.sysOrgUserService.getSysOrgUserByAccount(userId);
		//判断登录人是否只有拥有系统管理员身份 true有  false没有
		flag = false;
		List<SysRole> userRolesByAccount = this.sysOrgUserService.getUserRolesByAccount(userId);
		if(userRolesByAccount != null ){
			for(SysRole s:userRolesByAccount){
				//判断角色是否为系统管理员
				if(s.getCode().equals("systemManager")){
					flag = true;
					break;
				}else{
					flag = false;
				}
			}
		}else {
			flag = false;
		}
		this.permissionModuleList = this.sysUserRelaPermissionService.getFirstPermissionListByUserId(orgUserId, "IOSM", "IOSM_MenuResource",false);
//		System.out.println(permissionListByUserId);
//		this.permissionModuleList = this.sysPermissionDao.getPermissionModuleByAccount(userId);
		log.info("执行userIndexAction方法成功，实现了”登录我的首页");
		log.info("退出userIndexAction方法,返回success");
		return "success";
	}
	
	/**
	 * 根据账号获取门户组件信息
	* @author ou.jh
	* @date Jun 5, 2013 1:51:22 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getHmoeItemByRoleIdAndAccountAction(){
		log.info("进入getHmoeItemByRoleIdAndAccountAction方法");
		//从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
		this.sysOrgUser = this.sysOrgUserService.getSysOrgUserByAccount(userId);
		//根据账号获取门户组件信息 
		List<Map<String, Object>> hmoeItemByRoleIdAndAccount = this.homeItemService.getHmoeItemByAccount(userId);
		int item_column = 0;
		//筛选重复组件
		Map<String, Map<String, Object>> itemMap = new HashMap<String, Map<String,Object>>();
		if(hmoeItemByRoleIdAndAccount != null && hmoeItemByRoleIdAndAccount.size() > 0){
			for(Map<String, Object> m:hmoeItemByRoleIdAndAccount){
				itemMap.put(m.get("home_item_id").toString(), m);
			}
		}
		//把筛选后的数据转换成list
		List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> m:itemMap.values()){
			itemList.add(m);
		}
		Map<Object, List<Map<String, Object>>> map = new HashMap<Object, List<Map<String, Object>>>();
		//根据组件信息的列进行分组
		if(itemList != null && itemList.size() > 0){
			for(Map<String, Object> m:itemList){
				if(m.get("item_column") != null && !m.get("item_column").equals("")){
					if(map.containsKey(m.get("item_column"))){
						List<Map<String, Object>> list = map.get(m.get("item_column"));
						list.add(m);
					}else{
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						list.add(m);
						map.put(m.get("item_column"), list);
					}
				}
			}
		}
		//对已分组组件按照行进行排序
		for(List<Map<String, Object>> list: map.values()){
			ComparatorList comparatorList = new ComparatorList("item_row");
			Collections.sort(list, comparatorList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		Map<String, Object> reMap = new HashMap<String, Object>();	
		reMap.put("sysOrgUser", sysOrgUser);
		reMap.put("homeItem", map);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reMap);
		log.info("执行getHmoeItemByRoleIdAndAccountAction方法成功，实现了”根据账号获取门户组件信息");
		log.info("退出getHmoeItemByRoleIdAndAccountAction方法,返回void");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据账号获取个人工作台 
	* @author ou.jh
	* @date Jun 8, 2013 11:45:47 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String getPermissionModuleAction(){
		log.info("进入getPermissionModuleAction方法");
		//从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
		this.menuLinks = this.sysPermissionDao.getPermissionWorkPlatByAccount(userId);
		log.info("执行getPermissionModuleAction方法成功，实现了”根据账号获取个人工作台 ");
		log.info("退出getPermissionModuleAction方法,返回void");
		return "success";
	}
	/**
	 * 
	 * @description: 获取全部门户组件
	 * @author：yuan.yw
	 * @return     
	 * @return void     
	 * @date：Jun 6, 2013 3:52:39 PM
	 */
	public void getAllHomeItemListAction() throws IOException {
		log.info("进入 getAllHomeItemListService()，获取全部门户组件");
		List<Map<String,Object>> list = this.homeItemService.getAllHomeItemListService();
		String result = "";
		if (list != null) {
			Gson gson = new Gson();
			Map map = new HashMap();
			map.put("result", list);
			result = gson.toJson(map);
		}
		log.info("getItemList Return JSON= " + result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);

	}
	/**
	 * 
	 * @description: 保存门户组件
	 * @author：yuan.yw
	 * @throws IOException     
	 * @return void     
	 * @date：Jun 6, 2013 5:13:42 PM
	 */
	public void saveHomeItemAction() throws IOException{
		log.info("进入saveHomeItemAction,保存门户组件");
		HttpServletRequest request = ServletActionContext.getRequest();
		String itemJson = request.getParameter("itemJson");
		log.info("参数itemJson="+itemJson);
		if (itemJson == null) {
			return;
		}
		String result = "";
		Gson gson = new Gson();
		Map<String, String> jsonMap = null;
		jsonMap = gson.fromJson(itemJson, new TypeToken<Map<String, String>>() {
		}.getType());
		HomeItem hi = new HomeItem();
		hi.setTitle(jsonMap.get("title"));
		hi.setDefaultheight(Long.parseLong(jsonMap.get("defaultHeight")));
		hi.setDefaultwidth(Long.parseLong(jsonMap.get("defaultWidth")));
		hi.setMaxurl(jsonMap.get("maxUrl"));
		hi.setShowtitle(Integer.parseInt(jsonMap.get("showTitle")));
		hi.setStatus(1);
		hi.setUrl(jsonMap.get("url"));
		String id = jsonMap.get("id");
		Map map = new HashMap();
		// update
		if (id != null && !"".equals(id)) {
			hi.setHomeItemId(Long.parseLong(id));
			this.homeItemService.updateHomeItemService(hi);
			map.put("success", "true");
		}else{
			hi.setCreatetime(new Date());
			Serializable s = this.homeItemService.saveEntityService(hi);
			
			if(s!=null){
				map.put("success", "true");
			}else{
				map.put("success", "false");
			}
		}
		
		result = gson.toJson(map);
		log.info("退出saveHomeItemAction,返回结果result="+result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	/**
	 * 
	 * @description: 删除门户组件
	 * @author：yuan.yw
	 * @throws IOException     
	 * @return void     
	 * @date：Jun 6, 2013 5:13:42 PM
	 */
	public void deleteHomeItemAction() throws IOException{
		log.info("deleteHomeItemAction,删除门户组件");
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		String result = "";
		if (id == null) {
			return;
		}
		String itemId = id;
		log.info("删除门户组件 deleteItemAction(), itemId = " + itemId);
		this.homeItemService.deleteEntityService(itemId);
		Gson gson = new Gson();
		Map map = new HashMap();
		map.put("success", "true");
		result = gson.toJson(map);
		log.info("退出deleteHomeItemAction,返回结果result="+result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	/**
	 * 
	 * @description: 获取全部角色列表
	 * @author：yuan.yw     
	 * @return void     
	 * @throws IOException 
	 * @date：Jun 7, 2013 10:20:55 AM
	 */
	public  void getAllRolesForHomeAction() throws IOException{
		log.info("获取全部角色列表 getAllRolesForHomeAction()");
		List<Map<String,Object>> mapList = this.homeItemService.getAllRolesService();
		
		// new add orgRole for default use
	/*	Map defaultRole = new HashMap();
		defaultRole.put("orgRoleId", -1l);
		defaultRole.put("orgRoleCode", "defaultRole");
		defaultRole.put("orgRoleName", "默认角色");
		defaultRole.put("orgRoleTypeCode", "org_role");
		defaultRole.put("orgRoleType", "组织角色");
		mapLst.add(defaultRole);*/

		String result = "{\"result\":[]}";

		if (mapList != null && !mapList.isEmpty()) {
			Gson gson = new Gson();
			Map map = new HashMap();
			map.put("result", mapList);
			result = gson.toJson(map);
		}
		log.info("getAllRolesForHomeAction Return JSON = " + result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	/**
	 * 
	 * @description: 获取指定角色联的门户组件
	 * @author：yuan.yw
	 * @throws IOException     
	 * @return void     
	 * @date：Jun 7, 2013 10:39:01 AM
	 */
	public void getRoleHomeItemListByRoleIdAction() throws IOException {
		log.info("获取指定角色联的门户组件  getRoleHomeItemListByRoleIdAction()");
		HttpServletRequest request = ServletActionContext.getRequest();
		String roleId = request.getParameter("orgRoleId");
		if (roleId == null) {
			log.warn("选中的角色不存在roleId");
			return;
		}
		long orgRoleId = Long.parseLong(roleId);
		log.info("执行查询, roleId = " + roleId);
		List<Map<String, Object>> roleHomeItems = this.homeItemService.getHomeItemListByRoleIdAndTypeService(roleId,"equals"); 

		String result = "{\"result\":[]}";
		if (roleHomeItems != null) {
			Gson gson = new Gson();
			Map map = new HashMap();
			map.put("result", roleHomeItems);
			result = gson.toJson(map);
		}
		log.info("getRoleHomeItemListByRoleIdAction Return JSON= " + result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	/**
	 * 
	 * @description: 获取不与指定角色关联的门户组件
	 * @author：yuan.yw
	 * @throws IOException     
	 * @return void     
	 * @date：Jun 7, 2013 10:39:01 AM
	 */
	public void getRoleHomeItemListNoAssociateRoleIdAction() throws IOException {
		log.info("获取指定角色联的门户组件  getRoleHomeItemListNoAssociateRoleIdAction()");
		HttpServletRequest request = ServletActionContext.getRequest();
		String roleId = request.getParameter("orgRoleId");
		if (roleId == null) {
			log.warn("选中的角色不存在roleId");
			return;
		}
		long orgRoleId = Long.parseLong(roleId);
		log.info("执行查询, roleId = " + roleId);
		List<Map<String, Object>> roleHomeItems = this.homeItemService.getHomeItemListByRoleIdAndTypeService(roleId,null); 

		String result = "{\"result\":[]}";
		if (roleHomeItems != null) {
			Gson gson = new Gson();
			Map map = new HashMap();
			map.put("result", roleHomeItems);
			result = gson.toJson(map);
		}
		log.info("getRoleHomeItemListNoAssociateRoleIdAction Return JSON= " + result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	/**
	 * 
	 * 删除角色关联门户组件
	 * 
	 * @throws IOException
	 */
	public void deleteRoleHomeItemAction()
			throws IOException {
		log.info("删除角色关联门户组件 deleteRoleHomeItemAction()");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String roleId = request.getParameter("orgRoleId");
		log.info("参数roleId："+roleId);
		log.info("参数json："+json);
		if (json == null) {
			log.warn("传回的用于角色关联门户组件的参数为空");
			return;
		}
		Gson gson = new Gson();
		List<String> jsonMapList = gson.fromJson(json,
				new TypeToken<List<String>>() {
				}.getType());
		StringBuffer itemIds = new StringBuffer();
		if(jsonMapList!=null && !jsonMapList.isEmpty()){
			for(String itemId:jsonMapList){
				itemIds.append(","+itemId);
			}
			int i =this.homeItemService.deleteRoleHomeItemService(roleId,itemIds.substring(1)+"");
			if (i >= 0) {
				ServletActionContext.getResponse().getWriter().write(
						"success:删除成功" );
			} else {
				ServletActionContext.getResponse().getWriter().write(
						"failure:删除失败！");
			}
		}
	}

	/**
	 * 
	 * 保存角色关联门户组件
	 * 
	 * @throws IOException
	 */
	public void saveRoleHomeItemAction()
			throws IOException {
		log.info(" 保存角色关联门户组件 saveRoleHomeItemAction()");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String operateType = request.getParameter("operateType");
		String orgRoleId = request.getParameter("orgRoleId");
		log.info("参数operateType："+operateType);
		log.info("参数json："+json);
		if (json == null) {
			log.warn("传回的用于角色关联门户组件的参数为空");
			return;
		}
		Gson gson = new Gson();
		if("addMore".equals(operateType)&&orgRoleId!=null){//批量增加
			List<String> jsonMapList = gson.fromJson(json,
					new TypeToken<List<String>>() {
					}.getType());
			String result = "success";
			for(String itemId:jsonMapList){
				HomeItemRelaRole hrr = new HomeItemRelaRole();
				hrr.setRoleId(Long.valueOf(orgRoleId));
				hrr.setHomeItemId(Long.valueOf(itemId));
				hrr.setItemRow(0L);
				hrr.setItemColumn(0L);
				hrr.setItemHeight(300L);
				hrr.setItemWidth(300L);
				hrr.setCreatetime(new Date());
				Serializable s = this.homeItemService.saveEntityService(hrr);
				if(s==null){
					result = "failure";
					break;
				}
			}
			ServletActionContext.getResponse().getWriter().write(
			result);
			
		}else if("update".equals(operateType)){
			Map<String,String> jsonMap = gson.fromJson(json,
					new TypeToken<Map<String,String>>() {
					}.getType());
			HomeItemRelaRole hrr = new HomeItemRelaRole();
			hrr.setRoleId(Long.valueOf(jsonMap.get("orgRoleId")+""));
			hrr.setHomeItemId(Long.valueOf(jsonMap.get("id")+""));
			hrr.setItemRow(Long.valueOf(jsonMap.get("itemRow")+""));
			hrr.setItemColumn(Long.valueOf(jsonMap.get("itemColumn")+""));
			hrr.setItemHeight(Long.valueOf(jsonMap.get("itemHeight")+""));
			hrr.setItemWidth(Long.valueOf(jsonMap.get("itemWidth")+""));
			hrr.setCreatetime(new Date());
			this.homeItemService.updateRoleHomeItemService(hrr);
			ServletActionContext.getResponse().getWriter().write(
			"success" );
			
		}else{
			Map<String,String> jsonMap = gson.fromJson(json,
					new TypeToken<Map<String,String>>() {
					}.getType());
			HomeItemRelaRole hrr = new HomeItemRelaRole();
			hrr.setRoleId(Long.valueOf(jsonMap.get("orgRoleId")+""));
			hrr.setHomeItemId(Long.valueOf(jsonMap.get("id")+""));
			hrr.setItemRow(Long.valueOf(jsonMap.get("itemRow")+""));
			hrr.setItemColumn(Long.valueOf(jsonMap.get("itemColumn")+""));
			hrr.setItemHeight(Long.valueOf(jsonMap.get("itemHeight")+""));
			hrr.setItemWidth(Long.valueOf(jsonMap.get("itemWidth")+""));
			hrr.setCreatetime(new Date());
			Serializable s = this.homeItemService.saveEntityService(hrr);
			if(s!=null){
				ServletActionContext.getResponse().getWriter().write(
				"success" );
			}else{
				ServletActionContext.getResponse().getWriter().write(
				"failure" );
			}
		}
		

		
	}
	public SysPermissionDao getSysPermissionDao() {
		return sysPermissionDao;
	}

	public void setSysPermissionDao(SysPermissionDao sysPermissionDao) {
		this.sysPermissionDao = sysPermissionDao;
	}

	public List<Map<String, Object>> getPermissionModuleList() {
		return permissionModuleList;
	}

	public void setPermissionModuleList(
			List<Map<String, Object>> permissionModuleList) {
		this.permissionModuleList = permissionModuleList;
	}

	public List<Map<String, Object>> getMenuLinks() {
		return menuLinks;
	}

	public void setMenuLinks(List<Map<String, Object>> menuLinks) {
		this.menuLinks = menuLinks;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public SysUserRelaPermissionService getSysUserRelaPermissionService() {
		return sysUserRelaPermissionService;
	}

	public void setSysUserRelaPermissionService(
			SysUserRelaPermissionService sysUserRelaPermissionService) {
		this.sysUserRelaPermissionService = sysUserRelaPermissionService;
	}

	

}
