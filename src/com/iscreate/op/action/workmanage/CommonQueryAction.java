package com.iscreate.op.action.workmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.workmanage.WorkmanageField;
import com.iscreate.op.pojo.workmanage.WorkmanageMenu;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.CommonQueryService;
import com.iscreate.op.service.workmanage.FieldService;
import com.iscreate.op.service.workmanage.MenuService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;
import com.iscreate.sso.session.UserInfo;

public class CommonQueryAction{

	
	Logger logger = Logger.getLogger(this.getClass());
	
	private MenuService menuService;
	private FieldService fieldService;
	private CommonQueryService commonQueryService;
	private ServiceBean workFlowService;
	private WorkManageService workManageService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	
	private List<Map<String,String>> entityList;
	
	
	public List<Map<String, String>> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Map<String, String>> entityList) {
		this.entityList = entityList;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}

	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}
	


	

	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	/**
	 * 公共查询公共入口
	 * @return
	 */
	public String commonQueryIndexAction() {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		//String menuId = request.getParameter("menuId");
		String menuCode = request.getParameter("menucode");//du.hw修改
		logger.info("in class CommonQueryAction,in methodcommonQueryIndexAction()");
		
		if(menuCode==null || "".equals(menuCode)){
			logger.info("菜单menuCode===="+menuCode);
			return "fail";
		}
		
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		//获取菜单配置信息
		WorkmanageMenu menu=null;
		//menu=this.menuService.getMenuEntityByMenuId(menuId);
		menu=this.menuService.getMenuEntityByMenuCode(menuCode);
		
		String categoryFlag=menu.getCategoryFlag();	//菜单页面对应field标识
		String inputConfig=menu.getInputConfig();
		String inputButtonConfig=menu.getInputButtonConfig();
		String resultConfig=menu.getResultConfig();
		String queryPage=menu.getQueryPage();
		
		Gson gson=new Gson();
		
		List<Map<String,String>> input_config_list  = null;	//input查询条件显示配置
		List<Map<String,String>> input_button_list=null;	//input查询按钮显示配置
		List<Map<String,String>> result_config_list=null;;	//result结果显示配置
		List<WorkmanageField> input_field_list=null;	//查询类型字段field
		List<WorkmanageField> result_field_list=null;	//结果类型字段field
		
		
		//转化显示的格式
		if(inputConfig!=null && !inputConfig.isEmpty()){
			input_config_list=gson.fromJson(inputConfig,new TypeToken<List<Map<String,String>>>() {}.getType());
		}
		if(inputButtonConfig!=null && !inputButtonConfig.isEmpty()){
			input_button_list=gson.fromJson(inputButtonConfig,new TypeToken<List<Map<String,String>>>() {}.getType());
		}
		
		if(resultConfig!=null && !resultConfig.isEmpty()){
			result_config_list=gson.fromJson(resultConfig,new TypeToken<List<Map<String,String>>>() {}.getType());
		}
		
		
		//如果菜单页面是carDispatch
		if(menu!=null && "carDispatch".equals(menu.getMenuType())){
//			Map<String, List<PrevilidgeResource>> userPrevilidgeResource=authorityAccessController.getSubElements(userId, BizAuthorityConstant.RESOURCETYPE_PAGE, "wm_carDispatchWorkOrder", BizAuthorityConstant.RESOURCETYPE_MENU, null);
//			List<PrevilidgeResource> view_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.VIEW);	//获取可以访问，且可以看到的资源
//			List<PrevilidgeResource> forbid_previlidgeResource=userPrevilidgeResource.get(AuthorityConstant.OperationCode.FORBIDDEN);	//获取可以访问，但不可以看到的资源
//			
//			result_config_list=this.filterUserCanAccessMenuList(view_previlidgeResource,forbid_previlidgeResource,result_config_list);
		}
		
		
		//根据categoryFlag，获取所需要的field
		input_field_list=this.fieldService.getInputFieldsByCategoryFlag(categoryFlag);
		result_field_list=this.fieldService.getResultFieldsByCategoryFlag(categoryFlag);
		
		
		request.setAttribute("menu", menu);
		request.setAttribute("input_config_list", input_config_list);
		request.setAttribute("input_button_list", input_button_list);
		request.setAttribute("result_config_list", result_config_list);
		request.setAttribute("input_field_list", input_field_list);
		request.setAttribute("result_field_list", result_field_list);
		request.setAttribute("queryPage", queryPage);
		
		return "success";
	}
	
	
	
	
	/**
	 * 公共查询查询逻辑
	 * @throws IOException
	 */
	public void commonQueryAction() throws IOException {
		
		String result = "";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
			
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String orderName = request.getParameter("sort");
			String order = request.getParameter("dir");
			
			
			String queryEntityName = request.getParameter("queryEntityName");	//查询实体名
			String queryCondition = request.getParameter("queryCondition");	//附加SQL查询条件
			String queryParams = request.getParameter("queryParams");//查询条件输入表单数据
			String menuType = request.getParameter("menuType");
			
			Map<String,String> queryParamsMap = new HashMap<String,String>();
			
			List<Map<String, Object>> returnList = null;
			int count = 0;
			Gson gson = new Gson();
			
			if(userId==null||start==null||limit==null||orderName==null||order==null){
				logger.info("用户账号或分页参数不能为空");
			}

			if(queryCondition!=null && !"".equals(queryCondition)){
				queryCondition = queryCondition.replaceAll("\\{USERID\\}","'"+userId+"'");
			}
			
			if(queryParams!=null&&!"".equals(queryParams)){
				queryParamsMap = gson.fromJson(queryParams,new TypeToken<Map<String,String>>() {}.getType());			
			}
			
			//过滤当前登录用户所能看到的工单或任务单
			String filterBiz = request.getParameter("filterBiz");
			String filterProp = request.getParameter("filterProp");
			if("true".equals(filterBiz)){
				List<String> orgIdList=new ArrayList<String>();
				
				//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
				//yuan.yw
				List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
				if(topOrgList!=null && !topOrgList.isEmpty()){
					for(SysOrg org:topOrgList){
						//根据组织向下获取所有下属组织架构(服务商组织)
						//List<ProviderOrganization> childOrgList=this.providerOrganizationService.getOrgListDownwardByOrg(org.getId());
						//yuan.yw
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
					queryCondition =queryCondition+" and \""+filterProp+"\" in ("+sb_orgIds.toString()+")";
				}
				
				
				//增加组织的查询条件
				if(queryParamsMap.get(filterProp)!=null){
					String filterPropValue=queryParamsMap.get(filterProp).toString();
//					System.out.println("filterPropValue=="+filterPropValue);
					
					orgIdList=new ArrayList<String>();
					
					//判断组织是否具有子组织
					//List<ProviderOrganization> withChildOrgList=this.providerOrganizationService.getNextProviderOrgByOrgIdService(Long.valueOf(filterPropValue));
					//yuan.yw
					List<SysOrg> withChildOrgList=this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(Long.valueOf(filterPropValue), "child");
					if(withChildOrgList==null || withChildOrgList.isEmpty()){
						orgIdList.add(filterPropValue);
					}else{
						Map<String,String> orgMap = sysOrganizationService.getProviderOrgByOrgIdtoMapService(Long.valueOf(filterPropValue));
						if(orgMap!=null){
							//根据组织向下获取所有下属组织架构(服务商组织)
							//List<ProviderOrganization> childOrgList=this.providerOrganizationService.getOrgListDownwardByOrg(selectOrg.getId());
							//yuan.yw
							List<SysOrg> childOrgList=this.sysOrganizationService.getOrgListDownwardByOrg(Long.parseLong(orgMap.get("id")));
							if(childOrgList!=null && !childOrgList.isEmpty()){
								for(SysOrg childOrg:childOrgList){
									orgIdList.add(childOrg.getOrgId()+"");
								}
							}
						}
					}
					
					sb_orgIds=new StringBuffer("");
					//组装条件
					for(String tempId:orgIdList){
						sb_orgIds.append(tempId+",");
					}
					sb_orgIds.delete(sb_orgIds.length()-1, sb_orgIds.length());
					if(sb_orgIds!=null && !"".equals(sb_orgIds.toString())){
						queryCondition =queryCondition+" and \""+filterProp+"\" in ("+sb_orgIds.toString()+")";
					}
				}
			}
			
			//增加是否超时的查询条件
			if(queryParamsMap.get("isOverTime")!=null && !"".equals(queryParamsMap.get("isOverTime").toString())){
				String isOverTime=queryParamsMap.get("isOverTime").toString();
				if("yes".equals(isOverTime)){
					queryCondition =queryCondition+" and \"requireCompleteTime\" < SYSDATE";
				}else{
					queryCondition =queryCondition+" and \"requireCompleteTime\" > SYSDATE";
				}
			}
			
			
			Map<String,Object> returnMap = this.commonQueryService.commonQueryService(start, limit, orderName, order, queryEntityName, queryParamsMap, queryCondition);
			
			if(returnMap!=null){
				returnList = (List<Map<String,Object>>)returnMap.get("entityList");
				count = (Integer)returnMap.get("count");
			}
			
			if (returnList != null && !returnList.isEmpty()) {
				//----在记录中增加当前时间与半小时后时间---begin--
				java.util.Calendar calendar = Calendar.getInstance();
				String nowTime =TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
				calendar.add(Calendar.MINUTE, 30);
				String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
				
				List<Map<String,Object>> newMapList = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> itemMap:returnList){
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
					
					newMapList.add(itemMap);
				}
				//----在记录中增加当前时间与半小时后时间---end--
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("totalCount", count);
				map.put("result", newMapList);
				result = gson.toJson(map);
				
			}else{
				List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("totalCount", 0);
				map.put("result", mapList);
				result = gson.toJson(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//logger.info(result);
		
//		result="#@&*(*&%3245";
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
		
	}
	
	
	
	
	public void commonQueryGroupAction() throws IOException{
		
		String result = "";
		
		//获取当前登录人
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String orderName = request.getParameter("sort");
		String order = request.getParameter("dir");
		
		
		String queryEntityName = request.getParameter("queryEntityName");	//查询实体名
		String queryCondition = request.getParameter("queryCondition");	//附加SQL查询条件
		String queryParams = request.getParameter("queryParams");//查询条件输入表单数据
		String menuType = request.getParameter("menuType");
		
		Map<String,String> queryParamsMap = new HashMap<String,String>();
		Gson gson = new Gson();
		
		if(userId==null || start==null || limit==null){
			logger.info("用户账号或分页参数不能为空");
		}

		if(queryCondition!=null && !"".equals(queryCondition)){
			queryCondition = queryCondition.replaceAll("\\{USERID\\}","\""+userId+"\"");
		}
		
		if(queryParams!=null&&!"".equals(queryParams)){
			queryParamsMap = gson.fromJson(queryParams,new TypeToken<Map<String,String>>() {}.getType());			
		}
		
		List<FlowTaskInfo> taskInfoList=new ArrayList<FlowTaskInfo>();
		
		//获取当前登录人所属的组织
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
		//yuan.yw
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
		if(orgList!=null && !orgList.isEmpty()){
			for(SysOrg org:orgList){
//				System.out.println("org=="+org.getId());
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
//				String taskId=tempFlowTaskInfo.getTaskId();
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
			
			if(orgIds!=null && !orgIds.isEmpty()){
				queryCondition=queryCondition+" and \"orgId\" in ("+orgIds+")";
			}
			
			List<Map<String, Object>> returnList = null;
			int count = 0;
			
			
			Map<String,Object> returnMap = this.commonQueryService.commonQueryService(start, limit, orderName, order, queryEntityName, queryParamsMap, queryCondition);
			if(returnMap!=null){
				returnList = (List<Map<String,Object>>)returnMap.get("entityList");
				count = (Integer)returnMap.get("count");
			}
			
			if (returnList != null && !returnList.isEmpty()) {
				//----在记录中增加当前时间与半小时后时间---begin--
				java.util.Calendar calendar = Calendar.getInstance();
				String nowTime =TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
				calendar.add(Calendar.MINUTE, 30);
				String halfLaterTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());
				
				List<Map<String,Object>> newMapList = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> itemMap:returnList){
					
//					String currentTaskId=itemMap.get("currentTaskId")==null?"":itemMap.get("currentTaskId").toString();
					String processInstanceId=itemMap.get("processInstId")==null?"":itemMap.get("processInstId").toString();
					
					
					if(flowTaskInfoMap.containsKey(processInstanceId)){
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
						if(itemMap.get("taskPlanBeginTime")!=null){
							itemMap.put("taskPlanBeginTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("taskPlanBeginTime")));
						}
						
						if(itemMap.get("taskPlanEndTime")!=null){
							itemMap.put("taskPlanEndTime", TimeFormatHelper.getTimeFormatBySecond(itemMap.get("taskPlanEndTime")));
						}
						//格式化时间----------- end --------
						newMapList.add(itemMap);
					}
				}
				//----在记录中增加当前时间与半小时后时间---end--
				
				
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("totalCount", count);
				map.put("result", newMapList);
				
				//返回符合前台展现的格式的数据
				result = gson.toJson(map);
			}
		}
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}
	
	
	
	
	
	
//	the select code
//	fieldTemp.append("new Ext.form.ComboBox({\n");
//	fieldTemp.append("id:'"+fieldName+"_combobox',\n");
//	fieldTemp.append("fieldLabel:'"+fieldLabel+"',\n");
//	//fieldTemp.append("store:"+field_name+"_store,\n");
//	fieldTemp.append("anchor:'85%',\n");
//	fieldTemp.append("name:'"+fieldName+"',\n");
//	fieldTemp.append("hiddenName:'"+fieldName+"',\n");
//	//fieldTemp.append("hiddenName:'cao"+field_name+"',\n");
//	//fieldTemp.append("hiddenName:'shit',\n");
//	//fieldTemp.append("valueField: '"+field_name_value+"',\n");
//	//fieldTemp.append("displayField: '"+field_name_show+"',\n");
//	fieldTemp.append("typeAhead: true,\n");
//	fieldTemp.append("mode:'local',\n");
//	fieldTemp.append("forceSelection: true,\n");
//	fieldTemp.append("triggerAction: 'all',\n");
//	fieldTemp.append("emptyText:'请选择...',\n");
//	fieldTemp.append("selectOnFocus:true\n");
//	fieldTemp.append("}),\n");
//	
//	fieldTemp.append("{\n");
//	fieldTemp.append("xtype:'hidden',\n");
//	fieldTemp.append("name: '"+fieldName+"_OPERATOR',\n");
//	fieldTemp.append("value:'"+fieldOperator+"',\n");
//	fieldTemp.append("},\n");	
//	fieldTemp.append("{\n");
//	fieldTemp.append("xtype:'hidden',\n");
//	fieldTemp.append("name: '"+fieldName+"_TYPE',\n");
//	fieldTemp.append("value:'"+fieldType+"',\n");
//	fieldTemp.append("},\n");

	


}
