package com.iscreate.op.action.workmanage;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.system.SysPermissionDao;
import com.iscreate.op.dao.system.SysRoleDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.workmanage.WorkmanageMenu;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.op.service.workmanage.BizInfoFactoryService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workmanage.util.InterfaceConfLoadUtil;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.tools.GisDispatchJSONTools;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.excelhelper.WriteExcel2003Service;
import com.iscreate.plat.tree.TreeNode;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;
import com.iscreate.sso.session.UserInfo;

public class WorkManageAction {

	private static final Log logger = LogFactory.getLog(WorkManageAction.class);
	
	private BizInfoFactoryService bizInfoFactoryService;
	private DataDictionaryService dataDictionaryService;
	
	private WriteExcel2003Service writeExcel2003Service;
	
	private ServiceBean workFlowService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysRoleDao sysRoleDao;
	private SysUserRelaPermissionService sysUserRelaPermissionService;
	
	
	

	public void setSysUserRelaPermissionService(
			SysUserRelaPermissionService sysUserRelaPermissionService) {
		this.sysUserRelaPermissionService = sysUserRelaPermissionService;
	}

	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	private long treeId;
	
	private String status; //工单 任务单状态
	
	private String resourceId;//资源id
	
	private String resourceType;//资源类型
	
	private String  orderType;//获取工单（workorder）还是任务单（taskorder）
	
	private String searchCondition;//查询条件
	
	private WorkManageService workManageService;
	
	
	private boolean isInitDefaultMenu;
	
	private InputStream queryResultStream;
	private String queryResultName;
	
	
	private Map<String,String> urgentRepairWorkOrderMap;
	
	
	
	public WorkManageService getWorkManageService() {
		return workManageService;
	}


	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}




	public void setWriteExcel2003Service(WriteExcel2003Service writeExcel2003Service) {
		this.writeExcel2003Service = writeExcel2003Service;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getResourceId() {
		return resourceId;
	}


	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}


	public String getResourceType() {
		return resourceType;
	}


	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}


	public String getOrderType() {
		return orderType;
	}


	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public String getSearchCondition() {
		return searchCondition;
	}


	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}


	public void setBizInfoFactoryService(BizInfoFactoryService bizInfoFactoryService) {
		this.bizInfoFactoryService = bizInfoFactoryService;
	}
	
	
	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}

	public void setTreeId(long treeId) {
		this.treeId = treeId;
	}

	
	public InputStream getQueryResultStream() {
		return this.queryResultStream;
	}


	public void setQueryResultStream(InputStream queryResultStream) {
		this.queryResultStream = queryResultStream;
	}
	
	public String getQueryResultName() {
		Date date = new Date(System.currentTimeMillis());
		try {
			this.setQueryResultName(TimeFormatHelper.getTimeFormatByDay(date)
					+ new String("导出结果".getBytes("GBK"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return queryResultName;
	}
	

	public void setQueryResultName(String queryResultName) {
		this.queryResultName = queryResultName;
	}
	
	public Map<String, String> getUrgentRepairWorkOrderMap() {
		return urgentRepairWorkOrderMap;
	}

	public void setUrgentRepairWorkOrderMap(
			Map<String, String> urgentRepairWorkOrderMap) {
		this.urgentRepairWorkOrderMap = urgentRepairWorkOrderMap;
	}


	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}


	/**
	 * 工作管理页面入口action
	 * @return
	 */
	public String workManagePageIndexAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String type = request.getParameter("type");
		String workzonesiteName=request.getParameter("workzonesiteName");
		String workzoneUrl=request.getParameter("url");
		String permission_id=request.getParameter("permission_id");
		boolean isNewUrl=false;
		try {
			if(workzonesiteName!=null){
				workzonesiteName=new String(workzonesiteName.getBytes("ISO8859-1"),"UTF-8");
			}else{
				workzonesiteName="导航菜单";
			}
			if(workzoneUrl!=null){
				isNewUrl=true;
				workzoneUrl=new String(workzoneUrl.getBytes("ISO8859-1"),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.info("进入工作管理页面时，【工作区间名称】或者【工作区间url】编码构造错误");
		}
		
		workzonesiteName="'"+workzonesiteName+"'";
		workzoneUrl="'"+workzoneUrl+"'";
		
		request.setAttribute("userId", userId);
		request.setAttribute("type", type);
		request.setAttribute("workzonesiteName", workzonesiteName);
		request.setAttribute("workzoneUrl", workzoneUrl);
		request.setAttribute("isNewUrl", isNewUrl);
		request.setAttribute("permission_id", permission_id);
		return "success";
	}
	
	
	
	
	/**
	 * 根据类型标识返回其下所有第一层子节点
	 * @throws IOException 
	 * @throws IOException
	 */
	public void getChildrenMenuListByParentIdAndTypeAction() throws IOException{
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		long orgUserId =Long.parseLong(SessionService.getInstance().getValueByKey(com.iscreate.plat.login.constant.UserInfo.ORG_USER_ID).toString());
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//获取参数
		String parentId = request.getParameter("node");
		String menuType = request.getParameter("menuType");
		String permission_id=request.getParameter("permission_id");
		if(parentId==null||"".equals(parentId)) return;
		
		Gson gson = new Gson();
		
//		//获取用户可以访问的资源列表
//		Map<String, List<PrevilidgeResource>> userPrevilidgeResource=authorityAccessController.getSubElements(userId, BizAuthorityConstant.RESOURCETYPE_PAGE, "workManagePageIndexAction", BizAuthorityConstant.RESOURCETYPE_MENU, null);
//		
//		//System.out.println("user:"+userId+" access resource:");
//		//System.out.println(canAccessResources);
//		
//		List<PrevilidgeResource> view_previlidgeResource=null;
//		List<PrevilidgeResource> forbid_previlidgeResource=null;
//		
//		if(userPrevilidgeResource!=null && !userPrevilidgeResource.isEmpty()){
//			view_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.VIEW);	//获取授权可以看到的资源
//		}
//		
//		if(userPrevilidgeResource!=null && !userPrevilidgeResource.isEmpty()){
//			forbid_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.FORBIDDEN);	//获取没有授权的资源
//		}
//		
//		//根据菜单类型，获取对应的菜单节点
//		List<WorkmanageMenu> menuList = this.bizInfoFactoryService.getMenuService().getChildrenMenuListByType(menuType);
//		
//		//过滤用户可以访问的资源
//		menuList=this.filterUserCanAccessMenu(view_previlidgeResource,forbid_previlidgeResource, menuList);
//		
//		
//		List<Map<String,Object>> menuLists = new ArrayList<Map<String,Object>>();
//		Map<String,Object> itemMap = new HashMap<String,Object>();
//		
//		boolean isExpanded = false;
//		isInitDefaultMenu=false;
//		
//		if(menuList!=null && !menuList.isEmpty()){
//			for(WorkmanageMenu menu:menuList){
//				String parentId_ = menu.getParentId()+"";
//				String menuId_ = menu.getMenuId()==null?"":menu.getMenuId()+"";
//				
//				//第一层
//				if(parentId_!=null && !"".equals(parentId_) && parentId.equals(parentId_)){				
//
//					//叶子节点
//					if(1==menu.getIsLeaf().intValue()){
////						itemMap.put("id", String.valueOf(menu.getMenuId()));
////						itemMap.put("text", menu.getMenuName());
////						itemMap.put("href", menu.getMenuLink());
////						itemMap.put("hrefTarget", "main_right");
////						itemMap.put("expanded", true);	//默认都展开
////						
////						//---定义节点样式---begin---
////						itemMap.put("leaf", true);
////						itemMap.put("cls", "forum");
////						itemMap.put("iconCls", "icon-forum");
////						//---定义节点样式---end---
////						
////						menuLists.add(itemMap);
//					
//					//非叶子节点
//					}else if(0==menu.getIsLeaf().intValue()){
//						itemMap = new HashMap<String,Object>();
//						itemMap.put("id", menu.getMenuId()+"");
//						itemMap.put("text", menu.getMenuName());
//						
//						itemMap.put("children", getChildrenTreeNodeForExtjs(menuList,menuId_));
//						
//						//默认都展开
//						itemMap.put("expanded", true);		
//						//---定义节点样式---begin---
//						itemMap.put("leaf", false);
//						itemMap.put("cls", "forum-ct");
//						itemMap.put("iconCls", "forum-parent");
//						//---定义节点样式---end---
//						
//						if(((List)itemMap.get("children")).size()!=0){
//							menuLists.add(itemMap);
//						}
//					}
//				}
//			}
//		}
		long parent_id = 0;
		if(permission_id != null && !permission_id.trim().equals("")){
			parent_id = Long.parseLong(permission_id);
		}else{
			return;
		}
		List<Map<String,Object>> permissionByParentIdccount = this.sysUserRelaPermissionService.getSysPermissionListByOrgUserIdAndParentId(orgUserId, parent_id);
		Map<String, List<Map<String, Object>>> map = new TreeMap<String, List<Map<String,Object>>>();
		String[] titleStrings = null;
		//数据格式转换
		if(permissionByParentIdccount != null && permissionByParentIdccount.size() > 0){
			titleStrings = new String[permissionByParentIdccount.size()];
			int i = 0;
			int j = 0;
			for(Map<String,Object> m: permissionByParentIdccount){
				if(m != null){
					//设置第一个为默认打开
					if(i == 0){
						m.put("isDefault", "1");
						i++;
					}
					if(m.get("TITLE") == null){
						continue;
					}else if(map.containsKey(m.get("TITLE").toString())){
						List<Map<String, Object>> list = map.get(m.get("TITLE"));
						m.put("text", m.get("NAME"));
						m.put("cls", "forum");
						m.put("leaf", true);
						m.put("expanded", true);
						String url = "../"+m.get("URL");
						if(m.get("PARAMETER") != null && !m.get("PARAMETER").equals("")){
							url = url + "?" +m.get("PARAMETER");
						}else{
							url = url;
						}
						m.put("href", url);
						m.put("iconCls", "icon-forum");
						m.put("hrefTarget", "main_right");
						m.put("id", m.get("permission_id"));
						list.add(m);
						map.put(m.get("TITLE").toString(), list);
					}else{
						titleStrings[j]=m.get("TITLE").toString();
						j++;
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						m.put("text", m.get("NAME"));
						m.put("cls", "forum");
						m.put("leaf", true);
						m.put("expanded", true);
						String url = "../"+m.get("URL");
						if(m.get("PARAMETER") != null && !m.get("PARAMETER").equals("")){
							url = url + "?" +m.get("PARAMETER");
						}else{
							url = url;
						}
						m.put("href", url);
						m.put("iconCls", "icon-forum");
						m.put("hrefTarget", "main_right");
						m.put("id", m.get("permission_id"));
						list.add(m);
						map.put(m.get("TITLE").toString(), list);
					}
				}else{
					continue;
				}
			}
		}
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		if(map != null){
			int i = 0;
			for(String s : titleStrings){
				if(s != null && !s.equals("")){
					Map<String, Object> ma = new HashMap<String, Object>();
					ma.put("text", s);
					ma.put("cls", "forum-ct");
					ma.put("leaf", false);
					ma.put("expanded", true);
					ma.put("iconCls", "forum-parent");
					ma.put("id", i);
					ma.put("children", map.get(s));
					rList.add(ma);
					i++;
				}
			}
		}
		String result = "";
		if (rList != null) {			
			result = gson.toJson(rList);
		}
		
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	
	
	/**
	 * 获取菜单的子节点
	 * @param allMenuList
	 * @param menuId
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private List<Map<String,Object>> getChildrenTreeNodeForExtjs(List<WorkmanageMenu> allMenuList,String menuId) throws UnsupportedEncodingException{
		List<Map<String,Object>> childrenList = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> itemMap = null;
		String parentId = null;
		String nodeMenuId = null;
		String prefix = getProjectPrefixURL();
		logger.info("工程前缀：" + prefix);
		
		
		//long startTime = System.currentTimeMillis();
		if(allMenuList!=null && !allMenuList.isEmpty()){
			for(WorkmanageMenu menu:allMenuList){
				parentId =menu.getParentId()==null?"":menu.getParentId()+"";
				nodeMenuId =menu.getMenuId()==null?"":menu.getMenuId()+"";
				
				if(parentId.equals(menuId)){	
					itemMap = new HashMap<String,Object>();
					String menuId_ =menu.getMenuId()==null?"":menu.getMenuId()+"";
					String isLeaf = menu.getIsLeaf()==null?"":menu.getIsLeaf()+"";
					
					if("1".equals(isLeaf)){
						itemMap.put("id",menu.getMenuId());
						itemMap.put("text",menu.getMenuName());
						
						//---定义节点样式---begin---
						itemMap.put("leaf", true);
						itemMap.put("cls", "forum");
						itemMap.put("iconCls", "icon-forum");
						//---定义节点样式---end---
						
						if("workDispatch".equals(menu.getMenuType())){
							itemMap.put("isDefault", menu.getIsDefault()==null?"0": menu.getIsDefault());
							if( menu.getIsDefault()!=null && menu.getIsDefault()!=0){
								//itemMap.put("cls", "forum x-tree-selected");
							}
						}else if("routineInspection".equals(menu.getMenuType())){
							String menuCode=menu.getMenuCode();
							if(!isInitDefaultMenu){
								if("routineInspection_managePlan".equals(menuCode)){
									itemMap.put("isDefault", "1");
									isInitDefaultMenu=true;
								}else if("routineInspection_pendingTask".equals(menuCode)){
									itemMap.put("isDefault", "1");
									isInitDefaultMenu=true;
								}
							}
						}else{
							if(!isInitDefaultMenu){
								itemMap.put("isDefault", "1");
								//itemMap.put("cls", "forum x-tree-selected");
								isInitDefaultMenu=true;
							}
						}
						
						String menuLink = "";
						if(menu.getMenuLink()!=null){
							
							//动态获取链接的域名
							menuLink = menu.getMenuLink();
							menuLink = menuLink.trim();
							menuLink = replaceUrl(prefix, menuLink);
							
							if(menu.getMenuLink().indexOf("?")==-1){
								
								itemMap.put("href", menuLink +"?menuId="+menu.getMenuId());
							}else{
								itemMap.put("href", menuLink +"&menuId="+menu.getMenuId());
							}
						}
//						if("carDispatch_applyCar".equals(menu.getMenuCode()) || "carDispatch_countGpsMileage".equals(menu.getMenuCode())
//							|| "routineInspection_createPlan".equals(menu.getMenuCode())){
//							itemMap.put("hrefTarget", "newWindow"+menu.getMenuId());
//						}else{
//							itemMap.put("hrefTarget", "main_right");
//						}
						
						if("carDispatch_applyCar".equals(menu.getMenuCode()) || "carDispatch_countGpsMileage".equals(menu.getMenuCode())){
								itemMap.put("hrefTarget", "newWindow"+menu.getMenuId());
							}else{
								itemMap.put("hrefTarget", "main_right");
							}
						
//						itemMap.put("hrefTarget", "main_right");
						itemMap.put("expanded", true);	//默认都展开
						
						childrenList.add(itemMap);
						
					}else if("0".equals(isLeaf)){
						itemMap = new HashMap<String,Object>();
						itemMap.put("id",menu.getMenuId()==null?"":menu.getMenuId()+"");
						itemMap.put("text",menu.getMenuName()==null?"":menu.getMenuName());
						
						itemMap.put("children", this.getChildrenTreeNodeForExtjs(allMenuList,menuId_));		
						
						//默认都展开
						itemMap.put("expanded", true);
						//---定义节点样式---begin---
						itemMap.put("leaf", false);
						itemMap.put("cls", "forum-ct");
						itemMap.put("iconCls", "forum-parent");
						//---定义节点样式---end---
						
						if(((List)itemMap.get("children")).size()!=0){
							childrenList.add(itemMap);
						}
					}
				}
			}
		}
		
		//long endTime = System.currentTimeMillis();
		//logger.debug("------------构造菜单树共耗时："+(endTime-startTime)+"ms----------------");
		
		return childrenList;
	}

	/**
	 * 获取工程URL前缀
	 * 
	 * @return
	 */
	private String getProjectPrefixURL() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String uri = request.getRequestURI().toString();
		String url = request.getRequestURL().toString();
		return url.replace(uri, "").trim() + "/";
	}

	private String replaceUrl(String prefix, String value) {
		if (value == null || "".equals(value.trim())) {
			return "";
		}
		if (value.matches("\\$\\{.*\\}\\/*.*")) {
			// _log.info("替换${}为：" + prefix);
			value = value.replaceFirst("\\$\\{.*\\}\\/", prefix);
		}

		 logger.info("替换后：" + value);
		return value;
	}
	
	
	
	
	
	
	/**
	 * 跳转创建抢修工单页面
	 * @throws UnsupportedEncodingException 
	 */
	public String loadCreateUrgentRepairWorkOrderPageAction() throws UnsupportedEncodingException{
		HttpServletRequest request = ServletActionContext.getRequest();
//		//读取配置信息
//		String createUrWorkOrderUrl = InterfaceConfLoadUtil.getConfigValueFromProperties("loadCreateUrgentRepairWorkOrderPageActionURL");
//		//logger.info("createUrWorkOrderUrl=="+createUrWorkOrderUrl);
//		request.setAttribute("createUrWorkOrderUrl", "'"+createUrWorkOrderUrl+"'");
		return "success";
	}
	

	/**
	 * 更改工单ReadStatus状态
	 */
	public void updateWorkOrderReadStatusAction() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String workOrderId = request.getParameter("WOID");
		if(workOrderId==null||"".equals(workOrderId)){
			return;
		}
		boolean isSuccess = false;
		isSuccess =this.bizInfoFactoryService.getWorkOrderHandleService().updateWorkOrderReadStatusProcess(workOrderId, 1);
		String result = "";
		Gson gson = new Gson();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", isSuccess);		
		result = gson.toJson(map);
		result = new String(result.getBytes("UTF-8"), "ISO8859-1");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	/**
	 * 更改任务ReadStatus状态
	 */
	public void updateTaskOrderReadStatusAction() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String taskOrderId = request.getParameter("TOID");
		
		if(taskOrderId==null || "".equals(taskOrderId)){
			return;
		}
		
		boolean isSuccess = false;
		isSuccess =this.bizInfoFactoryService.getTaskOrderHandleService().updateTaskOrderReadStatusProcess(taskOrderId, 1);
		
		String result = "";
		Gson gson = new Gson();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", isSuccess);		
		result = gson.toJson(map);
		result = new String(result.getBytes("UTF-8"), "ISO8859-1");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	
	/**
	 * 获取下拉框数据action
	 * @throws IOException
	 */
	public void getSelectDataListAction() throws IOException {

		HttpServletRequest request = ServletActionContext.getRequest();
		List<Map> list=null;
		try {
			String selectFlag=request.getParameter("selectFlag");
			if("workOrderStatus".equals(selectFlag)){	//工单状态列表
				list = this.bizInfoFactoryService.getCommonQueryService().getAllWorkorderStatusList();
			}else  if("taskOrderStatus".equals(selectFlag)){	//任务单状态列表
				list = this.bizInfoFactoryService.getCommonQueryService().getAllTaskorderStatusList();
			}
			
			String result = "";
			if (list != null && !list.isEmpty()) {
				Gson gson = new Gson();
				Map map = new HashMap();
				map.put("result", list);
				result = gson.toJson(map);
			}
			//result = new String(result.getBytes("utf-8"), "iso-8859-1");
			ServletActionContext.getResponse().getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转门户待办页面action
	 * @return
	 */
	public String loadPortalPendingPageAction(){
		return "success";
	}
	
	
	/**
	 * 获取某个用户的待办工单列表
	 * 
	 * @return
	 */
	public void getPendingWorkOrdersByUserId() throws IOException {

		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		Integer count = null;
		
		if(userId==null || start==null || limit==null || orderName==null || order==null) return;

		Map returnMap = this.bizInfoFactoryService.getCommonQueryService().commonQueryService(start,limit, "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,
				"and 1=1 and \"currentHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.WORKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.WORKORDER_END);
				
		String result = "";
		if(returnMap!=null){
			List<Map> mapList = (List<Map>)returnMap.get("entityList");
			count = (Integer)returnMap.get("count");
			
			Map map = new HashMap();
			map.put("totalCount", count);
			map.put("result", mapList);
			
			Gson gson=new Gson();
			result = gson.toJson(map);
		}
		
		//result = new String(result.getBytes("utf-8"), "iso-8859-1");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	
	/**
	 * 获取某个用户的跟踪工单列表
	 * 
	 * @return
	 */
	public void getTrackWorkOrdersByUserId() throws IOException {

		HttpServletRequest request = ServletActionContext.getRequest();

		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		Integer count = null;
		
		if(userId==null||start==null||limit==null||orderName==null||order==null) return;

		
		Map returnMap =this.bizInfoFactoryService.getCommonQueryService().commonQueryService(start,limit, "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,
						"and 1=1 and \"creator\"='"+userId+"' and \"status\"="+WorkManageConstant.WORKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.WORKORDER_END);
				
		String result = "";
		if(returnMap!=null){
			List<Map> mapList = (List<Map>)returnMap.get("entityList");
			count = (Integer)returnMap.get("count");
			
			Map map = new HashMap();
			map.put("totalCount", count);
			map.put("result", mapList);
			
			Gson gson=new Gson();
			result = gson.toJson(map);
		}
		//result = new String(result.getBytes("utf-8"), "iso-8859-1");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	/**
	 * 获取某个用户的监督工单列表
	 * 
	 * @return
	 */
	public void getSuperviseWorkOrdersByUserId() throws IOException {

		HttpServletRequest request = ServletActionContext.getRequest();

		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		Integer count = null;
		
		if(userId==null||start==null||limit==null||orderName==null||order==null) return;

		
		Map returnMap =this.bizInfoFactoryService.getCommonQueryService().commonQueryService(start,limit, "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,
						"and 1=1");
				
		String result = "";
		if(returnMap!=null){
			List<Map> mapList = (List<Map>)returnMap.get("entityList");
			count = (Integer)returnMap.get("count");
			
			Map map = new HashMap();
			map.put("totalCount", count);
			map.put("result", mapList);
			
			Gson gson=new Gson();
			result = gson.toJson(map);
		}
		//result = new String(result.getBytes("utf-8"), "iso-8859-1");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	
	
//	/**
//	 * 过滤用户可以访问的菜单列表
//	 * @param view_previlidgeResource
//	 * @param forbid_previlidgeResource
//	 * @param menuList
//	 * @return
//	 */
//	public List<WorkmanageMenu> filterUserCanAccessMenu(List<PrevilidgeResource> view_previlidgeResource,List<PrevilidgeResource> forbid_previlidgeResource,List<WorkmanageMenu> menuList){
//		
//		List<WorkmanageMenu> canSeeMenuList=new ArrayList<WorkmanageMenu>();
//		
//		Map<String,WorkmanageMenu> menuMap=new HashMap<String,WorkmanageMenu>();
//		if(menuList!=null && !menuList.isEmpty()){
//			//把菜单list转化难为map集合
//			for(WorkmanageMenu menu:menuList){
//				menuMap.put(String.valueOf(menu.getMenuId()), menu);
//			}
//			
//			//遍历用户受控，并且能看见的资源
//			if(view_previlidgeResource!=null && !view_previlidgeResource.isEmpty()){
//				for(PrevilidgeResource pr:view_previlidgeResource){
//					String userDefineId=pr.getUserDefineId();
//					if(menuMap.containsKey(userDefineId)){
//						canSeeMenuList.add(menuMap.get(userDefineId));
//					}
//				}
//			}
//			
//			//遍历用户受控，但不能看见的资源
//			if(forbid_previlidgeResource!=null && !forbid_previlidgeResource.isEmpty()){
//				for(PrevilidgeResource pr:forbid_previlidgeResource){
//					String userDefineId=pr.getUserDefineId();
//					for(int i=0;i<menuList.size();i++){
//						String menuId=String.valueOf(menuList.get(i).getMenuId());
//						if(userDefineId.equals(menuId)){
//							menuList.remove(i);
//						}
//					}
//				}
//			}
//			
//			//对用户受控，能看见的资源与默认能看见的资源做并集
//			canSeeMenuList.removeAll(menuList);
//			canSeeMenuList.addAll(menuList);
//		}
//		
//		return canSeeMenuList;
//	}
	
	
	/**
	 * 加载创建抢修类型工单页面资源action
	 * @throws IOException 
	 */
	public void loadCreateUrgentRepairWorkOrderPageResourceAction() throws IOException{
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		Gson gson=new Gson();
		
		String json="";
//		
//		//获取用户可以访问的资源列表
////		Map<String, List<PrevilidgeResource>> canAccessResources=authorityAccessController.getSubElements(userId, BizAuthorityConstant.RESOURCETYPE_PAGE, "loadCreateUrgentRepairWorkOrderPageAction", BizAuthorityConstant.RESOURCETYPE_BUTTON, null);
////		System.out.println("user:"+userId+" access resource:");
////		System.out.println(canAccessResources);
//		
////		List<PrevilidgeResource> view_previlidgeResource=null;canAccessResources.get(AuthorityConstant.OperationCode.VIEW);
//		List<Map> view_resourceList=new ArrayList<Map>();
//		if(view_previlidgeResource!=null && !view_previlidgeResource.isEmpty()){
//			for(PrevilidgeResource pr:view_previlidgeResource){
//				String btnId=pr.getCode();
//				String btnName=pr.getName();
//				Map<String,String> params=gson.fromJson(pr.getUserDefineId(), new TypeToken<Map<String, String>>(){}.getType());
//				String btnLink=params.get("btnLink");
//				String btnOmType=params.get("btnOmType");
//				
//				Map tempMap=new HashMap();
//				tempMap.put("btnId", btnId);
//				tempMap.put("btnName", btnName);
//				tempMap.put("btnLink", btnLink);
//				tempMap.put("btnOmType",btnOmType);
//				tempMap.put("isDisable", "0");
//				view_resourceList.add(tempMap);
//			}
//		}
//		
//		//这里取巧
////		List<PrevilidgeResource> cannot_edit_previlidgeResource=canAccessResources.get(AuthorityConstant.OperationCode.FORBIDDEN);
//		List<Map> cannot_edit_resourceList=new ArrayList<Map>();
//		if(cannot_edit_previlidgeResource!=null && !cannot_edit_previlidgeResource.isEmpty()){
//			for(PrevilidgeResource pr:cannot_edit_previlidgeResource){
//				String btnId=pr.getCode();
//				String btnName=pr.getName();
//				Map<String,String> params=gson.fromJson(pr.getUserDefineId(), new TypeToken<Map<String, String>>(){}.getType());
//				String btnOmType=params.get("btnOmType");
//				
//				Map tempMap=new HashMap();
//				tempMap.put("btnId", btnId);
//				tempMap.put("btnName", btnName);
//				tempMap.put("btnOmType",btnOmType);
//				tempMap.put("isDisable", "1");
//				cannot_edit_resourceList.add(tempMap);
//			}
//		}
//		
//		Map<String,List<Map>> jsonMap=new HashMap<String,List<Map>>();
//		jsonMap.put("view_buttons", view_resourceList);
//		jsonMap.put("forbidden_buttons", cannot_edit_resourceList);
		
//		json=gson.toJson(jsonMap);
		ServletActionContext.getResponse().getWriter().write(json);
	}
	
	
	/**
	 * 获取某个用户的工单草稿列表
	 * 
	 * @return
	 */
	public void getDraftWorkOrdersByUserId() throws IOException {

		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		Integer count = null;
		
		if(userId==null || start==null || limit==null || orderName==null || order==null) return;

		Map returnMap = this.bizInfoFactoryService.getCommonQueryService().commonQueryService(start,limit, "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,
				"and 1=1 AND \"currentHandler\"='"+userId+"' and \"status\"=1");
				
		String result = "";
		if(returnMap!=null){
			List<Map> mapList = (List<Map>)returnMap.get("entityList");
			count = (Integer)returnMap.get("count");
			
			Map map = new HashMap();
			map.put("totalCount", count);
			map.put("result", mapList);
			
			Gson gson=new Gson();
			result = gson.toJson(map);
		}
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	/**
	 * 获取目标类型数据字典
	 * @throws IOException 
	 */
	public void getDictionaryData() throws IOException{
		PrintWriter pw=ServletActionContext.getResponse().getWriter();
		Gson gson=new Gson();
		String json="";
		List<TreeNode> treeNodeList=this.dataDictionaryService.getDictionaryByTreeIdService(treeId);
		if(treeNodeList!=null && !treeNodeList.isEmpty()){
			json=gson.toJson(treeNodeList);
			pw.write(json);
		}else{
			json=gson.toJson(new ArrayList<TreeNode>());
			pw.write(json);
		}
	}
	
	
	/**
	 * 加载门户抢修工单标签资源
	 * @throws IOException 
	 */
	public void loadPortalUrgentRepairWorkOrderTabResourceAction() throws IOException{
		PrintWriter pw=ServletActionContext.getResponse().getWriter();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
//		
//		//获取用户可以访问的资源列表
//		Map<String, List<PrevilidgeResource>> userPrevilidgeResource=authorityAccessController.getSubElements(userId, BizAuthorityConstant.RESOURCETYPE_PAGE, "workManagePageIndexAction", BizAuthorityConstant.RESOURCETYPE_MENU, null);
//		List<PrevilidgeResource> view_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.VIEW);	//获取可以访问，且可以看到的资源
//		List<PrevilidgeResource> forbid_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.FORBIDDEN);	//获取可以访问，但不可以看到的资源
//		
//		List<WorkmanageMenu> menuList=this.bizInfoFactoryService.getMenuService().getPortalMenuList();
//		
//		//过滤用户可以访问的资源
//		menuList=this.filterUserCanAccessMenu(view_previlidgeResource,forbid_previlidgeResource, menuList);
//		
//		Map<String,List<Map<String,String>>> result_config_map=new LinkedHashMap<String, List<Map<String,String>>>();
//		if(menuList!=null && !menuList.isEmpty()){
//			Gson gson=new Gson();
//			String json="";
//			for(WorkmanageMenu menu:menuList){
//				String resultConfig=menu.getResultConfig();
//				if(resultConfig!=null && !resultConfig.isEmpty()){
//					List<Map<String,String>> result_config_list=gson.fromJson(resultConfig,new TypeToken<List<Map<String,String>>>() {}.getType());
//					result_config_list.get(0).put("menuCode", menu.getMenuCode());
//					result_config_list.get(0).put("menuName", menu.getMenuName());
//					result_config_map.put(String.valueOf(menu.getMenuId()), result_config_list);
////					result_config_map.put(menu.getMenuCode(), result_config_list);
//				}
//			}
//			json=gson.toJson(result_config_map);
//			pw.write(json);
//		}
		//门户临时修改代码
		Gson gson=new Gson();
		String json="";
		List<SysRole> userRoles = this.sysRoleDao.getUserRoles(userId);
		Set<String> codeSet = new HashSet<String>();
		if(userRoles != null && userRoles.size() > 0){
			for(SysRole sr:userRoles){
				String code = sr.getCode();
				codeSet.add(code);
			}
			if(codeSet != null && codeSet.size() > 0){
				for(String st:codeSet){
					if(st.equals(BizAuthorityConstant.ROLE_MAINTENANCEWORKER) || st.equals(BizAuthorityConstant.ROLE_TEAMMEMBER)){
						json = "{\"3\":"
							   +"[{\"resultItemId\":\"workorder_grid\",\"resultItemType\":\"grid\",\"queryEntityName\":\"V_WM_URGENTREPAIR_WORKORDER\","
							   +"\"resultField\":\"\\\"id\\\",\\\"woId\\\", \\\"woType\\\",\\\"woTitle\\\",\\\"creator\\\",\\\"creatorName\\\",\\\"createTime\\\","
							   +"\\\"creatorOrgId\\\",\\\"status\\\",\\\"statusName\\\",\\\"formUrl\\\",\\\"currentHandler\\\",\\\"currentHandlerName\\\","
							   +"\\\"requireCompleteTime\\\",\\\"isRead\\\"\",\"queryAction\":\"commonQueryAction.action\",\"autoQueryTime\":\"1000*60*3\","
							   +"\"queryCondition\":\"and 1\u003d1 and \\\"currentHandler\\\"\u003d{USERID} and \\\"status\\\"\u003c\u003e6 and \\\"status\\\"\u003c\u003e7\",\"sortName\":\"\\\"createTime\\\"\","
							   +"\"sortType\":\"desc\",\"resultItemScript\":\"op/workmanage/jslib/commonQueryResultScript.js\",\"resultTabActiveAction\":\"\",\"resultRowClickFunc\":\"\","
							   +"\"resultRowDbClickFunc\":\"workOrderRowDbClickAction\",\"resultRowViewFunc\":\"workOrderRowResultView\",\"resultLimit\":\"10\",\"menuCode\":\"urgentRepair_pendingWorkOrder\",\"menuName\":\"待办工单\"}]"
							   +",\"4\":[{\"resultItemId\":\"workorder_grid\",\"resultItemType\":\"grid\",\"queryEntityName\":\"V_WM_URGENTREPAIR_WORKORDER\","
							   +"\"resultField\":\"\\\"id\\\",\\\"woId\\\", \\\"woType\\\",\\\"woTitle\\\",\\\"creator\\\",\\\"creatorName\\\",\\\"createTime\\\""
							   +",\\\"creatorOrgId\\\",\\\"status\\\",\\\"statusName\\\",\\\"formUrl\\\",\\\"currentHandler\\\",\\\"currentHandlerName\\\",\\\"requireCompleteTime\\\",\\\"isRead\\\"\","
							   +"\"queryAction\":\"commonQueryAction.action\",\"autoQueryTime\":\"1000*60*3\",\"queryCondition\":\"and 1\u003d1 and \\\"creator\\\"\u003d{USERID} and "
							   +"\\\"status\\\"\u003d6 and \\\"status\\\"\u003c\u003e7\",\"sortName\":\"\\\"createTime\\\"\",\"sortType\":\"desc\",\"resultItemScript\":"
							   +"\"op/workmanage/jslib/commonQueryResultScript.js\",\"resultTabActiveAction\":\"\",\"resultRowClickFunc\":\"\",\"resultRowDbClickFunc\":"
							   +"\"workOrderRowDbClickAction\",\"resultRowViewFunc\":\"workOrderRowResultView\",\"resultLimit\":\"10\",\"menuCode\":\"urgentRepair_traceWorkOrder\","
							   +"\"menuName\":\"跟踪工单\"}]}";
					}else{
						   json = "{\"3\":"
								   +"[{\"resultItemId\":\"workorder_grid\",\"resultItemType\":\"grid\",\"queryEntityName\":\"V_WM_URGENTREPAIR_WORKORDER\","
								   +"\"resultField\":\"\\\"id\\\",\\\"woId\\\", \\\"woType\\\",\\\"woTitle\\\",\\\"creator\\\",\\\"creatorName\\\",\\\"createTime\\\","
								   +"\\\"creatorOrgId\\\",\\\"status\\\",\\\"statusName\\\",\\\"formUrl\\\",\\\"currentHandler\\\",\\\"currentHandlerName\\\","
								   +"\\\"requireCompleteTime\\\",\\\"isRead\\\"\",\"queryAction\":\"commonQueryAction.action\",\"autoQueryTime\":\"1000*60*3\","
								   +"\"queryCondition\":\"and 1\u003d1 and \\\"currentHandler\\\"\u003d{USERID} and \\\"status\\\"\u003c\u003e6 and \\\"status\\\"\u003c\u003e7\",\"sortName\":\"\\\"createTime\\\"\","
								   +"\"sortType\":\"desc\",\"resultItemScript\":\"op/workmanage/jslib/commonQueryResultScript.js\",\"resultTabActiveAction\":\"\",\"resultRowClickFunc\":\"\","
								   +"\"resultRowDbClickFunc\":\"workOrderRowDbClickAction\",\"resultRowViewFunc\":\"workOrderRowResultView\",\"resultLimit\":\"10\",\"menuCode\":\"urgentRepair_pendingWorkOrder\",\"menuName\":\"待办工单\"}]"
								   +",\"4\":[{\"resultItemId\":\"workorder_grid\",\"resultItemType\":\"grid\",\"queryEntityName\":\"V_WM_URGENTREPAIR_WORKORDER\","
								   +"\"resultField\":\"\\\"id\\\",\\\"woId\\\", \\\"woType\\\",\\\"woTitle\\\",\\\"creator\\\",\\\"creatorName\\\",\\\"createTime\\\""
								   +",\\\"creatorOrgId\\\",\\\"status\\\",\\\"statusName\\\",\\\"formUrl\\\",\\\"currentHandler\\\",\\\"currentHandlerName\\\",\\\"requireCompleteTime\\\",\\\"isRead\\\"\","
								   +"\"queryAction\":\"commonQueryAction.action\",\"autoQueryTime\":\"1000*60*3\",\"queryCondition\":\"and 1\u003d1 and \\\"creator\\\"\u003d{USERID} and "
								   +"\\\"status\\\"\u003d6 and \\\"status\\\"\u003c\u003e7\",\"sortName\":\"\\\"createTime\\\"\",\"sortType\":\"desc\",\"resultItemScript\":"
								   +"\"op/workmanage/jslib/commonQueryResultScript.js\",\"resultTabActiveAction\":\"\",\"resultRowClickFunc\":\"\",\"resultRowDbClickFunc\":"
								   +"\"workOrderRowDbClickAction\",\"resultRowViewFunc\":\"workOrderRowResultView\",\"resultLimit\":\"10\",\"menuCode\":\"urgentRepair_traceWorkOrder\","
								   +"\"menuName\":\"跟踪工单\"}]"
								   +",\"5\":[{\"resultItemId\":\"workorder_grid\",\"resultItemType\":\"grid\",\"queryEntityName\":\"V_WM_URGENTREPAIR_WORKORDER\","
								   +"\"resultField\":\"\\\"id\\\",\\\"woId\\\", \\\"woType\\\",\\\"woTitle\\\",\\\"creator\\\",\\\"creatorName\\\",\\\"createTime\\\",\\\"creatorOrgId\\\",\\\"status\\\",\\\"statusName\\\","
								   +"\\\"formUrl\\\",\\\"currentHandler\\\",\\\"currentHandlerName\\\",\\\"requireCompleteTime\\\",\\\"isRead\\\"\",\"queryAction\":\"commonQueryAction.action\","
								   +"\"autoQueryTime\":\"1000*60*3\",\"queryCondition\":\"and 1\u003d1 and \\\"status\\\"\u003c\u003e7 \",\"sortName\":\"\\\"createTime\\\"\",\"sortType\":\"desc\","
								   +"\"resultItemScript\":\"op/workmanage/jslib/commonQueryResultScript.js\",\"resultTabActiveAction\":\"\",\"resultRowClickFunc\":\"\",\"resultRowDbClickFunc\":\"workOrderRowDbClickAction\""
								   +",\"resultRowViewFunc\":\"workOrderRowResultView\",\"resultLimit\":\"10\",\"filterBiz\":\"true\",\"filterProp\":\"orderOwnerOrgId\",\"menuCode\":\"urgentRepair_superviseWorkOrder\",\"menuName\":\"监督工单\"}]}";

					}
				}
			}
			pw.write(json);
		}else{
			pw.write(json);
		}
	}
	
	
	
	/**
	 * 
	 * @description: 获取工单 任务单
	 * @author：yuan.yw
	 * @return void     
	 * @date：Dec 3, 2012 2:08:34 PM
	 */
	public void getWorkOrdersByResourceIdByStatusAction(){
		StringBuilder json = new StringBuilder();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		List<Map> resultList = null;
		json.append("[");
		String bizTypeCode = "all";
		String taskOrderType="all";
		String sqlString ="";
		
		
		
		if(searchCondition!=null && !searchCondition.isEmpty()){
			try {
				this.searchCondition=new String(searchCondition.getBytes("ISO8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if ("workOrder".equals(orderType)) {
			if ("current".equals(status)) {
				sqlString += " and t2.\"status\" <> "+WorkManageConstant.WORKORDER_END;
			} else if ("history".equals(status)) {
				sqlString += " and t2.\"status\" = "+WorkManageConstant.WORKORDER_END;	
			}
			if (!"null".equals(searchCondition) && searchCondition != null && !"".equals(searchCondition)) {
				String conditionSqlString = " and (t2.\"woId\" like '%"+ searchCondition+ "%' or t2.\"woTitle\" like '%"+ searchCondition+ "%')";
				sqlString += conditionSqlString;
			}
			resultList=this.workManageService.getWorkOrderListByResourceTypeAndResourceId(resourceType, resourceId, bizTypeCode, taskOrderType, sqlString);
		} else {
			if ("current".equals(status)) {
				sqlString += " and t1.\"status\" <> "+WorkManageConstant.WORKORDER_END; 
			} else if ("history".equals(status)) {
				sqlString += " and t1.\"status\" = "+WorkManageConstant.WORKORDER_END;	
			}
			if (!"null".equals(searchCondition) && searchCondition != null && !"".equals(searchCondition)) {
				String conditionSqlString = " and (t1.\"toId\" like '%"+ searchCondition+ "%' or t1.\"toTitle\" like '%"+ searchCondition+ "%')";
				sqlString += conditionSqlString;
			}
			resultList=this.workManageService.getTaskOrderListByResourceTypeAndResourceId(resourceType, resourceId, bizTypeCode, taskOrderType, sqlString);
		}
		if (resultList != null && resultList.size() > 0) {
			if("workOrder".equals(orderType)){
				for (Map<String,Object> wo : resultList) {
					json.append(GisDispatchJSONTools.getWorkOrderJson(wo));
					json.append(",");
				}
			}else{
				for (Map<String,Object> wo : resultList) {
					json.append(GisDispatchJSONTools.getTaskOrderJson(wo));
					json.append(",");
				}
			}
			json.append("{'totalCount':"+resultList.size()+"}");
		}
		json.append("]");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 导出查询结果到excel文件中
	 */
	public String exportQueryResultToExcelAction(){
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		List<Map<String,Object>> resultMapList=getAllWorkOrders(userId);
		
		Map<String,String> columnHeadChineseMap=InterfaceConfLoadUtil.getExportOrderMapping("exportOrderMapping.xml");
		if(resultMapList!=null && !resultMapList.isEmpty()){
		
			if(urgentRepairWorkOrderMap!=null && !urgentRepairWorkOrderMap.isEmpty()){
				
//				List<String> biaotou=new ArrayList<String>();
				Map<String,String> columnHeadMap=new HashMap<String,String>();
				for(Map.Entry<String, String> entry:urgentRepairWorkOrderMap.entrySet()){
					String key=entry.getKey();
					if(columnHeadChineseMap.containsKey(key)){
//						biaotou.add();
						columnHeadMap.put(key, columnHeadChineseMap.get(key));
					}else{
						//biaotou.add(key);
					}
				}
				this.queryResultStream=writeExcel2003Service.creatExcel2003InputStreamForMap(columnHeadMap,resultMapList);
				
//				Map<String,Object> map=resultMapList.get(0);
//				if(map!=null && !map.isEmpty()){
//					for(Map.Entry<String, Object> entry:map.entrySet()){
//						String key=entry.getKey();
//						if(columnHeadChineseMap.containsKey(key)){
////							biaotou.add();
//							columnHeadMap.put(key, columnHeadChineseMap.get(key));
//						}else{
//							//biaotou.add(key);
//						}
//					}
//				}
			}
		}
		return "success";
	}
	
	
	
	private List<Map<String,Object>> getAllWorkOrders(String userId){
		List<Map<String,Object>> list=null;
		String queryCondition=null;
		
		List<String> orgIdList=new ArrayList<String>();
		//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		if(topOrgList!=null && !topOrgList.isEmpty()){
			for(SysOrg org:topOrgList){
				//根据组织向下获取所有下属组织架构(服务商组织)
				//List<ProviderOrganization> childOrgList=this.providerOrganizationService.getOrgListDownwardByOrg(org.getId());
				List<SysOrg> childOrgList=this.sysOrganizationService.getOrgListDownwardByOrg(org.getOrgId());
				if(childOrgList!=null && !childOrgList.isEmpty()){
					for(SysOrg childOrg:childOrgList){
						orgIdList.add(childOrg.getOrgId()+"");
					}
				}
			}
		}
		StringBuffer sb_orgIds=new StringBuffer("");
		//组装条件
		for(String tempId:orgIdList){
			sb_orgIds.append(tempId+",");
		}
		sb_orgIds.delete(sb_orgIds.length()-1, sb_orgIds.length());
		if(sb_orgIds!=null && !"".equals(sb_orgIds.toString())){
			queryCondition =" and \"orderOwnerOrgId\" in ("+sb_orgIds.toString()+")";
		}
		
		Map<String,Object> nopage_returnMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(null, null, "\"createTime\"", "DESC", "V_WM_URGENTREPAIR_WORKORDER", null, queryCondition);
		if(nopage_returnMap!=null){
			list = (List<Map<String,Object>>)nopage_returnMap.get("entityList");
		}
		
		if (list != null && !list.isEmpty()) {
			//----在记录中增加当前时间与半小时后时间---begin--
			java.util.Calendar calendar = Calendar.getInstance();
			String nowTime =TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
			calendar.add(Calendar.MINUTE, 30);
			String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
			
			List<Map<String,Object>> noPage_newMapList = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> itemMap:list){
				itemMap.put("_NOWTIME", nowTime);
				itemMap.put("_HALFLATERTIME", halfLaterTime);
				
				//格式化时间----------- begin ------
				if(itemMap.get("createTime")!=null){
					itemMap.put("createTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("createTime")));
				}
				
				if(itemMap.get("requireCompleteTime")!=null){
					itemMap.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("requireCompleteTime")));
				}
				
				if(itemMap.get("assignTime")!=null){
					itemMap.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("assignTime")));
				}
				if(itemMap.get("planUseCarTime")!=null){
					itemMap.put("planUseCarTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("planUseCarTime")));
				}
				//格式化时间----------- end --------
				
				
				//格式化数据导出时显示的格式-------- begin --------
				
				//故障处理结果
				if(itemMap.get("faultDealResult")!=null){
					if("1".equals(itemMap.get("faultDealResult"))){
						itemMap.put("faultDealResult", "已解决");
					}else{
						itemMap.put("faultDealResult", "未解决");
					}
				}
				
				
				//告警清除时间
				if(itemMap.get("isAlarmClear")!=null){
					if("0".equals(itemMap.get("isAlarmClear"))){
						itemMap.put("isAlarmClear", "否");
					}else{
						itemMap.put("isAlarmClear", "是");
					}
				}
				
				//是否影响业务
				if(itemMap.get("sideeffectService")!=null){
					if("0".equals(itemMap.get("sideeffectService"))){
						itemMap.put("sideeffectService", "否");
					}else{
						itemMap.put("sideeffectService", "是");
					}
				}
				
				//是否超时
				if(itemMap.get("requireCompleteTime")!=null){
					
					long currentTime=calendar.getTime().getTime();
					long requireCompleteTime=(TimeFormatHelper.setTimeFormat(itemMap.get("requireCompleteTime").toString())).getTime();
					
					if(currentTime<requireCompleteTime){
						itemMap.put("isOverTime", "否");
					}else{
						itemMap.put("isOverTime", "是");
					}
				}
				
				//抢修客户工单的是否影响业务
				if(itemMap.get("customerSideeffectService")!=null){
					if("-1".equals(itemMap.get("customerSideeffectService"))){
						itemMap.put("customerSideeffectService", "");
					}else if("0".equals(itemMap.get("customerSideeffectService"))){
						itemMap.put("customerSideeffectService", "否");
					}else if("1".equals(itemMap.get("customerSideeffectService"))){
						itemMap.put("customerSideeffectService", "是");
					}
				}
				
				//回复人
				if(itemMap.get("lastReplyPeopleName")!=null){
					itemMap.put("lastReplyPeopleName", itemMap.get("lastReplyPeopleName").toString());
				}
				
				//回复时间
				if(itemMap.get("finalCompleteTime")!=null){
					itemMap.put("finalReplyTime", itemMap.get("finalCompleteTime").toString());
				}
				
				
				if(itemMap.get("woId")!=null){
					String woId=itemMap.get("woId").toString();
					
					List<Map> taskOrderList=this.workManageService.getTaskOrderListByWoId(woId);
					if(taskOrderList!=null && !taskOrderList.isEmpty()){
						for(Map map:taskOrderList){
							if(map.get("toId")!=null){
								if(map.get("currentHandlerOrgId")!=null){
									Map<String,String> orgMap = sysOrganizationService.getProviderOrgByOrgIdtoMapService(Long.valueOf(map.get("currentHandlerOrgId").toString()));
									if(orgMap!=null){
										itemMap.put("orderOwnerOrgId", orgMap.get("name"));
									}
								}
								
								itemMap.put("orderOwner", map.get("currentHandlerName"));
							}
						}
					}else{
						
						//没有关联的任务单
						itemMap.put("orderOwnerOrgId", "");
						itemMap.put("orderOwner","");
					}
				}
				
				
				
				//格式化数据导出时显示的格式-------- end --------
				
				noPage_newMapList.add(itemMap);
			}
			//----在记录中增加当前时间与半小时后时间---end--
		}
		return list;
	}
	
	
	
	
	/**
	 * 跳转门户巡检待办页面
	 * @return
	 */
	public String loadPortalRoutineInspectionPendingPageAction(){
		return "success";
	}
	
	
	/**
	 * 获取巡检待办任务
	 * @throws IOException
	 */
	public void getRoutineInspectionPendingTaskOrderAction() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		
		
		List<FlowTaskInfo> taskInfoList=new ArrayList<FlowTaskInfo>();
		
		//获取当前登录人所属的组织
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
		//yuan.yw
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);

		if(orgList!=null && !orgList.isEmpty()){
			for(SysOrg org:orgList){
				List<FlowTaskInfo> groupTaskList=null;
				try {
					groupTaskList=(List<FlowTaskInfo>)this.workFlowService.queryShareTasks(ProcessConstants.AcceptorType.ACCEPTOR_ORGROLE, String.valueOf(org.getOrgId()), ProcessConstants.TaskStatus.INIT);
				} catch (WFException e) {
					e.printStackTrace();
					logger.error("根据组织【"+org.getOrgId()+"】，获取对应的群组任务失败");
				}
				if(groupTaskList!=null && !groupTaskList.isEmpty()){
					taskInfoList.addAll(groupTaskList);
				}
			}
		}
		
		//获取用户个人的任务
		List<FlowTaskInfo> personalTaskList=null;
		try {
			personalTaskList = (List<FlowTaskInfo>)this.workFlowService.queryOwnTasks(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE, userId, ProcessConstants.TaskStatus.HANDLING);
		} catch (WFException e) {
			e.printStackTrace();
			logger.error("获取用户【"+userId+"】，的个人任务失败");
		}
		
		if(personalTaskList!=null && !personalTaskList.isEmpty()){
			taskInfoList.addAll(personalTaskList);
		}
		
		if(taskInfoList!=null && !taskInfoList.isEmpty()){
			Map<String,FlowTaskInfo> flowTaskInfoMap=new HashMap<String,FlowTaskInfo>();
			for(FlowTaskInfo tempFlowTaskInfo:taskInfoList){
				String flowInstanceId=tempFlowTaskInfo.getInstanceId();
				if(flowInstanceId!=null && !"".equals(flowInstanceId)){
					flowTaskInfoMap.put(flowInstanceId, tempFlowTaskInfo);
				}
			}
			
			String orgIds="";
			if(orgList!=null && !orgList.isEmpty()){
				for(SysOrg org:orgList){
					orgIds=orgIds+org.getOrgId()+",";
				}
				orgIds=orgIds.substring(0, orgIds.length()-1);
			}
			
			String queryCondition="and 1=1 and \"status\"<>"+WorkManageConstant.TASKORDER_INSPECT_CLOSED;
			
			if(orgIds!=null && !orgIds.isEmpty()){
				queryCondition=queryCondition+" and \"orgId\" in ("+orgIds+")";
			}
			
			List<Map<String, Object>> returnList = null;
			int count = 0;
			
			Map returnMap = this.bizInfoFactoryService.getCommonQueryService().commonQueryService(start,limit, "\"assignTime\"", "desc", "V_WM_INSP_PENDINGTASKORDER", null,
					queryCondition);
			
			String result = "";
			if(returnMap!=null){
				returnList = (List<Map<String, Object>>)returnMap.get("entityList");
				count = (Integer)returnMap.get("count");
				
				if (returnList != null && !returnList.isEmpty()) {
					
					for(Map<String,Object> itemMap:returnList){
						
						
						String processInstanceId=itemMap.get("processInstId")==null?"":itemMap.get("processInstId").toString();
						
						if(flowTaskInfoMap.containsKey(processInstanceId)){
							//格式化时间----------- begin ------
							if(itemMap.get("createTime")!=null){
								itemMap.put("createTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("createTime")));
							}
							
							if(itemMap.get("requireCompleteTime")!=null){
								itemMap.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("requireCompleteTime")));
							}
							
							if(itemMap.get("assignTime")!=null){
								itemMap.put("assignTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("assignTime")));
							}
							if(itemMap.get("taskPlanBeginTime")!=null){
								itemMap.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("taskPlanBeginTime")));
							}
							
							if(itemMap.get("taskPlanEndTime")!=null){
								itemMap.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("taskPlanEndTime")));
							}
							//格式化时间----------- end --------
						}
					}
				}
				
				
				Map map = new HashMap();
				map.put("totalCount", count);
				map.put("result", returnList);
				
				Gson gson=new Gson();
				result = gson.toJson(map);
			}
			
			ServletActionContext.getResponse().getWriter().write(result);
		}
	}


	
	
}
