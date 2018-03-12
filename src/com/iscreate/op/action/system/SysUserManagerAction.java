package com.iscreate.op.action.system;

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
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysDictionaryService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysUserManagerService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.tools.paginghelper.PagingHelper;

public class SysUserManagerAction 
{
	
	private static Log log = LogFactory.getLog(SysUserManagerAction.class);
	/**
	 * bean 注射
	 */
	private SysOrganizationService sysOrganizationService;
	private SysUserManagerService sysUserManagerService;
	//用户
	private SysOrgUserService sysOrgUserService;
	//用户资源权限关系 yuan.yw
	private SysUserRelaPermissionService sysUserRelaPermissionService;
	//数据字典yuan.yw
	private SysDictionaryService sysDictionaryService;
	
	private long orgUserId;//用户id
	//结果集
	private Map<String,Object> pageMap;
	private String dataPermissionIds;//数据资源id字符串
	private String showType;//页面显示操作（ view 查看详情）
	
	/**
	 * 部门信息集合
	 */
	private List<Map<String, Object>> sysOrgList;
	
	/**
	 * 用户名或账号
	 */
	private String userNameOrAccount;
	
	/**
	 * 部门ID
	 */
	private long orgId;
	
	/**
	 * 列表查询List
	 */
	private List<Map<String, Object>> userList ;
	
	/**
	 * 当前页数
	 */
	private int currentPage;
	
	/**
	 * 总页数
	 */
	private int pageSize;
	
	/**
	 * 数据总数
	 */
	private long  total;
	
	
	
	/**
	 * 用户管理列表基本查询
	 * @author li.hb
	 * @date 2014-1-9 下午3:07:54
	 * @Description: TODO 
	 * @param @return        
	 * @throws
	 */
	public String findSysUserManagerInfoAction()
	{
		//获取登录人orgUserId
		long orgUserId = Long.parseLong(SessionService.getInstance().getValueByKey(UserInfo.ORG_USER_ID)+"");
		
		//根据orgUserId获取所在公司的第二级组织 
		this.sysOrgList = this.sysOrganizationService.getIscreateCompanyAndBusinessUnitList();
		
		return "success"; 
	}
	
	
	/**
	 * 用户管理列表查询
	 * @author li.hb
	 * @date 2014-1-9 下午3:08:07
	 * @Description: TODO 
	 * @param @return        
	 * @throws
	 */
	public String findSysUserManagerListAction()
	{
		
		
		this.userList = sysUserManagerService.findSysUserManagerListService(userNameOrAccount, orgId,currentPage,pageSize);
		
		
		int count = sysUserManagerService.findSysUserManagerCountService(userNameOrAccount, orgId);
		
		if(count > 0){
			PagingHelper ph = new PagingHelper();
			//计算分页 
			Map<String, Object> phMap = ph.calculatePagingParamService(count,currentPage, pageSize);
			this.total = Long.parseLong(phMap.get("totalPage").toString());
		}
		
		return "success";
	}


	/**
	 * 
	 * @description: 用户数据范围设置页面action
	 * @author：yuan.yw
	 * @return     
	 * @return String     
	 * @date：Jan 10, 2014 9:32:33 AM
	 */
	public String viewUserDataRangeAction(){
		log.info("进入viewUserDataRangeAction，用户数据范围设置页面action");
		Map<String,Object> userMap = this.sysOrgUserService.getAccountByOrgUserId(orgUserId);
		List<Map<String,Object>> systemList = this.sysDictionaryService.getChildDictionaryListByCode("System");//项目系统名 list
		List<Map<String,Object>> permissionList = new ArrayList<Map<String,Object>>();
		Map<String,Object> topOrgMap = new HashMap<String,Object>();
		this.pageMap = new HashMap<String,Object>(); //结果集
		this.pageMap.put("systemList",systemList);
		this.pageMap.put("userMap",userMap);
		Map<String,Object> permissionMap = new HashMap<String,Object>();
		if(systemList!=null && !systemList.isEmpty()){//有项目系统
			for(Map<String,Object> system:systemList){
				String code = system.get("CODE")+"";
				if("PM".equals(code)){
					//广东怡创map
					topOrgMap = this.sysOrganizationService.getIscreateCompanyMap();
					this.pageMap.put("topOrgMap", topOrgMap);
					//获取数据资源权限list
					permissionList = this.sysUserRelaPermissionService.getSysPermissionListByProCodeAndTypeAndServType(code, "PM_DataResource", "PM_Org", this.orgUserId);
					permissionMap.put(code, permissionList);
				}else{//其他项目数据权限
					permissionList = new ArrayList<Map<String,Object>>();
					permissionMap.put(code, permissionList);
				}
			}
			this.pageMap.put("permissionMap", permissionMap);
		}
		log.info("退出viewUserDataRangeAction，跳转页面");
		return "success";
		
	}
	/**
	 * 
	 * @description: 保存用户数据范围
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jan 10, 2014 10:16:02 AM
	 */
	public void  saveUserDataRangeAction(){
		log.info("进入saveUserDataRangeAction,保存用户数据范围");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = "";
		if(this.orgUserId<0){
			result="用户id不正确，保存用户数据范围失败。";
		}
		//删除已关联的用户数据范围 现在只考虑PM 系统的
		boolean flag = this.sysUserRelaPermissionService.deleteUserPMDataPermissionByProCodeAndTypeAndServType("PM", "PM_DataResource", "'PM_Org','PM_Project'", this.orgUserId);
		if(!flag){
			result="删除用户旧数据范围出错，保存用户数据范围失败。";
		}else{
			if(this.dataPermissionIds!=null && !"".equals(dataPermissionIds)){
				//保存数据
				String[] idArr = this.dataPermissionIds.split(",");
				for(String permissionId:idArr){
					this.sysUserRelaPermissionService.savePMUserRelaData(orgUserId, Long.valueOf(permissionId));
				}
			}
			result="success";
		}
		try {
			response.getWriter().write(gson.toJson(result));//ajax返回
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行saveUserDataRangeAction方法成功，实现了”保存用户数据范围“的功能");
 		log.info("退出saveUserDataRangeAction方法,返回void");
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public long getTotal() {
		return total;
	}


	public void setTotal(long total) {
		this.total = total;
	}

	
	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}


	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}


	public SysUserManagerService getSysUserManagerService() {
		return sysUserManagerService;
	}


	public void setSysUserManagerService(SysUserManagerService sysUserManagerService) {
		this.sysUserManagerService = sysUserManagerService;
	}


	


	public SysUserRelaPermissionService getSysUserRelaPermissionService() {
		return sysUserRelaPermissionService;
	}


	public void setSysUserRelaPermissionService(
			SysUserRelaPermissionService sysUserRelaPermissionService) {
		this.sysUserRelaPermissionService = sysUserRelaPermissionService;
	}


	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}


	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}


	public List<Map<String, Object>> getSysOrgList() {
		return sysOrgList;
	}


	public void setSysOrgList(List<Map<String, Object>> sysOrgList) {
		this.sysOrgList = sysOrgList;
	}


	public String getUserNameOrAccount() {
		return userNameOrAccount;
	}


	public void setUserNameOrAccount(String userNameOrAccount) {
		this.userNameOrAccount = userNameOrAccount;
	}


	public long getOrgId() {
		return orgId;
	}


	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}


	public List<Map<String, Object>> getUserList() {
		return userList;
	}


	public void setUserList(List<Map<String, Object>> userList) {
		this.userList = userList;
	}


	public long getOrgUserId() {
		return orgUserId;
	}


	public void setOrgUserId(long orgUserId) {
		this.orgUserId = orgUserId;
	}


	public Map<String, Object> getPageMap() {
		return pageMap;
	}


	public void setPageMap(Map<String, Object> pageMap) {
		this.pageMap = pageMap;
	}


	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}


	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	public String getDataPermissionIds() {
		return dataPermissionIds;
	}


	public void setDataPermissionIds(String dataPermissionIds) {
		this.dataPermissionIds = dataPermissionIds;
	}


	public String getShowType() {
		return showType;
	}


	public void setShowType(String showType) {
		this.showType = showType;
	}

	
	
	
	
	
}
