package com.iscreate.op.action.cms;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.CmsConstant;
import com.iscreate.op.dao.cms.CmsManageDao;
import com.iscreate.op.dao.cms.CmsManageDaoImpl;
import com.iscreate.op.pojo.cms.CmsInfoitem;
import com.iscreate.op.pojo.cms.CmsInforelease;
import com.iscreate.op.pojo.cms.CmsReportProjectAppraisal;
import com.iscreate.op.service.cms.CmsManageService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.plat.tools.FileHelper;
import com.iscreate.plat.tools.excelhelper.ExcelService;

import com.iscreate.sso.session.UserInfo;
import com.iscreate.tools.ReadExcelServiceImpl;
import com.opensymphony.xwork2.ActionContext;
public class CmsManageAction {
	private CmsManageService cmsManageService;
	private Map<String, Object> requestMap;
	
	
	private String ids;		//需要删除的信息的拼接后的id
	private String tabType;		//动态加载对应tab的公告信息
	private long orgId;	//组织id
	
	private String releaseBounds;	//发布范围类型
	private String releaseScopeList;	//发布范围列表(id，比如部门id、人员id)，"-"分隔
	private String releaseScopeStaffName;
	private String releaseRole;
	private List<String> selectedCkStaff;
	private int releaseOrApprove;	//1：发布	、2：要审批、3：审批不通过、4.审批通过
	private String radioApprover;	//用户选择的审批人
	private int isApprover;//是否需要再审核
	
	private List<Map<String, Object>> cmsReleaseList;	//cms信息集合
		
	private Map<String, Object> cmsAjaxMap;		//动态加载消息内容后，用来保存信息的map，保存信息及数量
	private List<Map<String, Object>> cmsCategoryList;	//信息类型
	private List<Map<String, Object>> cmsImportantLevel;	//重要级别
	private List<Map<String, Object>> cmsUrgencyLevelList;	//紧急程度
	private List<Map<String,Object>> cmsPortalItemList;	//门户item列表
	private Map<String,Object> infoItemEntity;
	private long infoItemId;	//hardcode
	private long infoReleaseId;
	private List<Map<String,Object>> superiorsList;
	private List<Map<String,Object>> roleList;
	private List<Map<String,Object>> companyLeadersList;
	private Map<String,Object> infoReleaseEntity;
	private String superiorsAccId;
	private String companyLeadersId;
	private int isOperator;
	private int infoReleaseStatus;
	private String roleCode;
	private String infoReleaseStaffId;
	private CmsInfoitem cmsInfoitem = new CmsInfoitem();
	private CmsInforelease cmsInforelease = new CmsInforelease();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String copyInfoItem;
	private List<Map<String, Object>> approverList;		//审核人列表
	private Map<String,Object> defaultApprover;		//当前默认审核人
	private String newDefaultApprover;	//修改当前默认审核人
	
	private String selectUser;	//设置发布权限的用户
	private int selectUserLimit;	//用户设置的发布权限code
	
	private int orgRole;	//角色code
	private int roleLimitLevel;		//角色的发布权限code
	//图片上传
	private File[] uploadPic;
	private String[] uploadPicFileName;
	private String[] uploadPicContentType;
	
	
	//附件上传
	private File[] uploadAttachment;
	private String[] uploadAttachmentFileName;
	private String[] uploadAttachmentContentType;
	
	private String projectName;
	private String year;
	
	
	private String addorUpdate;
	private String Itype;
	
	private List<Map<String,Object>> qualityList;
	
	
	private List<Map<String, Object>> humanResourcesList;
	
	private String qualityId;
	
	private String humanResourcesId;
	
	private List<Map<String, String>> infoReleasesList;
	
	private String picName;
	
	private String picUrl;
	
	
	private String attaName;
	
	
	private String attaUrl;
	
	private List<CmsReportProjectAppraisal> cmsReportProjectAppraisals;
	
	
	//中英文对照
	


	public List<CmsReportProjectAppraisal> getCmsReportProjectAppraisals() {
		return cmsReportProjectAppraisals;
	}


	public void setCmsReportProjectAppraisals(
			List<CmsReportProjectAppraisal> cmsReportProjectAppraisals) {
		this.cmsReportProjectAppraisals = cmsReportProjectAppraisals;
	}


	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getPicName() {
		return picName;
	}


	public void setPicName(String picName) {
		this.picName = picName;
	}


	public CmsManageService getCmsManageService() {
		return cmsManageService;
	}


	public void setCmsManageService(CmsManageService cmsManageService) {
		this.cmsManageService = cmsManageService;
	}


	/**
	 * 加载公告信息发布action
	 */
	public String loadInfoReleaseAction(){
		
		try {
			//获取当前登录人
			String userId = (String) SessionService.getInstance().getValueByKey(
			"userId");
			
//			//hardcode测试
//			Map<String,String> tabTotal=new HashMap<String,String>();
//			tabTotal.put("waitAudit", "5");
//			tabTotal.put("latestRelease", "2");
//			tabTotal.put("draftByMe", "1");
//			tabTotal.put("allInfo", "7");
//			tabTotal.put("overReleaseTime", "7");
			
			//获取待审核的公告发布信息
			this.cmsReleaseList = this.cmsManageService.getInfoReleaseListByTabType(userId, this.tabType);
			
			//获取各tabType对应的信息大小
			Map<String,String> tabTotal=this.cmsManageService.getCmsReleaseCountByTabType(userId);
			requestMap = new HashMap<String, Object>();
			requestMap.put("tabTotal", tabTotal);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail"; 
		}
		
		return "success";
	}
	
	
	/**
	 * Ajax加载公告信息发布列表
	 */
	public void loadInfoReleaseByTypeForAjaxAction() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			if(this.tabType != null && !"".equals(this.tabType)) {
				
				this.cmsAjaxMap = new HashMap<String, Object>();
				//获取待审核的公告发布信息
				List<Map<String, Object>> cmsMapList =this.cmsManageService.getInfoReleaseListByTabType(userId, this.tabType);
				
				
				//保cms信息内容集合和信息数量
				this.cmsAjaxMap.put("cmsAjaxList", cmsMapList);
				this.cmsAjaxMap.put("count", cmsMapList.size());
				Gson gson = new Gson();
				String result = gson.toJson(this.cmsAjaxMap);
				//System.out.println("result=="+result);
				response.getWriter().write(result);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 批量删除公告发布消息
	 */
	public void deleteInfoReleaseAction(){
		try {
			if(this.ids != null && !"".equals(this.ids)) {
				String[] idsArr = null;
				if(ids.indexOf("-") > -1) {
					//需要删除的消息在一条以上
					idsArr = this.ids.split("-");
					
				} else {
					//只需要删除一条消息
					idsArr = new String[]{this.ids};
				}
				
				//批量删除消息
				boolean flag = this.cmsManageService.deleteCmsInfoItem(idsArr);
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				String userId = (String) SessionService.getInstance().getValueByKey(
				"userId");
				//获取各tabType对应的信息大小
				Map<String,String> tabTotal=this.cmsManageService.getCmsReleaseCountByTabType(userId);
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("tabTotal", tabTotal);
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				String result = "";
				if(flag) {
					resultMap.put("flag","success");
				} else {
					resultMap.put("flag","error");
				}
				result = gson.toJson(resultMap);
				response.getWriter().write(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 跳转新增普通信息页面
	 * @return
	 */
	public String loadAddInfoItemPageAction(){
		try {
			if(copyInfoItem != null && copyInfoItem.equals("copyInfoItem")){
				this.infoItemEntity = this.cmsManageService.getInfoItemById(this.infoItemId);
			}
			//获取信息类型
			this.cmsCategoryList =this.cmsManageService.getCmsCategoryList();
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			//获取发布的栏目列表
			this.cmsPortalItemList = this.cmsManageService.getPortalItemList();
			superiorsList = new ArrayList<Map<String,Object>>();
			superiorsList = cmsManageService.getSuperiorsList(userId);
			//获取所有角色
			this.roleList = this.cmsManageService.getAllRoleType();
			
			//质量安全部
			this.qualityList = this.cmsManageService.getQualitySuperiorsList();
			//人力资源部
			this.humanResourcesList = this.cmsManageService.getHumanResourcesSuperiorsList();
			
			//公司领导
			this.companyLeadersList = this.cmsManageService.getCompanyLeadersList();

			
			//获取重要级别
			this.cmsImportantLevel = this.cmsManageService.getCmsImportantLevelList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	
	/**
	 * 新建普通信息action
	 * @return
	 */
	public void addInfoItemAction(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			cmsInfoitem.setId(this.infoItemId);
	
			this.infoItemId = this.cmsManageService.saveOrUpdateCmsInfoItem(addorUpdate, cmsInfoitem, uploadPic, uploadPicFileName, picName, picUrl, attaName, attaUrl, uploadAttachment, uploadAttachmentFileName,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(infoItemId);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转新增公告发布信息页面
	 * @return
	 */
	public String loadAddInfoReleasePageAction(){
		//从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
		try {
			//获取紧急程度
			this.cmsUrgencyLevelList = this.cmsManageService.getCmsUrgencyLevelList();
			this.infoItemEntity = this.cmsManageService.getInfoItemById(infoItemId);
			//获取发布的栏目列表
			this.cmsPortalItemList = this.cmsManageService.getPortalItemList();
			superiorsList = new ArrayList<Map<String,Object>>();
			superiorsList = cmsManageService.getSuperiorsList(userId);
			//获取所有角色
			this.roleList = this.cmsManageService.getAllRoleType();
			
			//质量安全部
			this.qualityList = this.cmsManageService.getQualitySuperiorsList();
			//人力资源部
			this.humanResourcesList = this.cmsManageService.getHumanResourcesSuperiorsList();
			
			//公司领导
			this.companyLeadersList = this.cmsManageService.getCompanyLeadersList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	
	/**
	 * 跳转查看普通信息页面
	 * @return
	 */
	public String loadInfoItemPageAction(){
		
		try {
			//获取信息类型
			this.cmsCategoryList = this.cmsManageService.getCmsCategoryList();
			
			//获取重要级别
			this.cmsImportantLevel = this.cmsManageService.getCmsImportantLevelList();
			//根据id获取信息项
			this.infoItemEntity = this.cmsManageService.getInfoItemById(this.infoItemId);
			if(infoItemEntity != null){
				if(infoItemEntity.get("status") != null && !infoItemEntity.get("status").equals("")){
					this.infoReleaseStatus = Integer.valueOf(infoItemEntity.get("status").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	
	/**
	 * 新建公共发布信息action
	 * @return
	 */
	public void addInfoReleaseAction(){
		String returnString = "";
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			String userCnName=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERNAME);
			
			//发布范围类型：1.部门  2.人员  3.岗位角色
			int releaseScopeType=1;
			if("radio_department".equals(this.releaseBounds)){
				releaseScopeType=1;
			}else if("radio_staff".equals(this.releaseBounds)){
				releaseScopeType=2;
//				if(this.selectedCkStaff!=null && !this.selectedCkStaff.isEmpty()){
//					for(String staff:this.selectedCkStaff){
//						this.releaseScopeList+=staff+"-";
//					}
//					
//				}
				//this.releaseScopeList=this.releaseScopeList.substring(0, this.releaseScopeList.lastIndexOf("-"));
				//System.out.println("scopeList=="+this.releaseScopeList);
				
				//this.releaseScopeStaffName=this.releaseScopeStaffName.substring(0, this.releaseScopeStaffName.lastIndexOf("-"));
				//System.out.println("releaseScopeStaffName=="+this.releaseScopeStaffName);
				
			}else if("radio_role".equals(this.releaseBounds)){
				releaseScopeType=3;
				//this.releaseScopeList=this.releaseScopeList.substring(0, this.releaseScopeList.lastIndexOf("-"));
				//System.out.println("scopeList=="+this.releaseScopeList);
				this.releaseRole=this.roleCode;
				//System.out.println("releaseRole=="+this.releaseRole);
			}
			
			
			cmsInforelease.setInfoId(this.infoItemId);
			cmsInforelease.setReleaseScopeType(releaseScopeType);
			cmsInforelease.setReleaseScopeList(this.releaseScopeList);
			cmsInforelease.setReleaseRole(this.releaseRole);
			cmsInforelease.setReleaseScopeStaffName(this.releaseScopeStaffName);
			cmsInforelease.setReleaser(userId);
			cmsInforelease.setLastModifiedTime(new Date());

			
		returnString = this.cmsManageService.saveCmsInforeleaseAndUpdateCmsInfoItem(cmsInforelease, releaseOrApprove, userCnName, infoItemId, 
				radioApprover, superiorsAccId, humanResourcesId, qualityId, 
				companyLeadersId, releaseScopeType, releaseScopeList, releaseRole, userId);
		this.requestMap = new HashMap<String, Object>();
		returnString = "操作成功";
		requestMap.put("urlAction", "\"loadInfoReleaseAction\"");	
		requestMap.put("returnString", returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(requestMap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转审批信息发布页面
	 * @return
	 */
	public String loadApproveInfoReleasePageAction(){
		try {
			//获取信息类型
			this.cmsCategoryList= this.cmsManageService.getCmsCategoryList();
			
			//获取重要级别
			this.cmsImportantLevel =this.cmsManageService.getCmsImportantLevelList();
			
			
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = ServletActionContext.getRequest();
			Map<String, Object> requestMap = (Map<String, Object>) ctx.get("request");
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			//获取紧急程度
			this.cmsUrgencyLevelList = this.cmsManageService.getCmsUrgencyLevelList();
			
			
			//获取门户item列表
			this.cmsPortalItemList =this.cmsManageService.getPortalItemList();
			this.isApprover = this.cmsManageService.getIsApprover(userId, infoReleaseId, infoItemId);
			//System.out.println("isApprover===="+this.isApprover);
			Map<String,Object> tempMap=this.cmsManageService.getInforeleaseById(infoReleaseId);
			if(tempMap != null){
				
				if(tempMap.get("releasePeriodStart")==null){
					tempMap.put("releasePeriodStart","");
				}
				if(tempMap.get("releasePeriodEnd")==null){
					tempMap.put("releasePeriodEnd","");
				}
				if(tempMap.get("releaseHistory")==null){
					tempMap.put("releaseHistory","");
				}
				if(tempMap.get("lastModifiedTime")==null){
					tempMap.put("lastModifiedTime","");
				}
				this.infoReleaseEntity=tempMap;
				
				//判断当前登录人是否当前审核人
				String approver=tempMap.get("auditor")==null?"":tempMap.get("auditor").toString();
				
				if(userId.equals(approver)){
					this.isOperator=1;
				}else{
					this.isOperator=0;
				}
			}
			
			//获取信息发布的状态
			long releaseId=Long.valueOf(tempMap.get("infoId").toString());
			Map<String, Object> infoItemById = this.cmsManageService.getInfoItemById(releaseId);
			this.infoItemEntity = infoItemById;
			int itemStatus=Integer.parseInt(infoItemById.get("status").toString());
			this.infoReleaseStatus=itemStatus;
			
			
			superiorsList = new ArrayList<Map<String,Object>>();
			superiorsList = cmsManageService.getSuperiorsList(userId);
			//获取所有角色
			this.roleList = this.cmsManageService.getAllRoleType();
			
			//质量安全部
			this.qualityList = this.cmsManageService.getQualitySuperiorsList();
			//人力资源部
			this.humanResourcesList = this.cmsManageService.getHumanResourcesSuperiorsList();
			
			//公司领导
			this.companyLeadersList = this.cmsManageService.getCompanyLeadersList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	
	/**
	 * 审核信息发布action
	 * @return
	 */
	public void approveInfoReleaseAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			String userCnName=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERNAME);
			//发布范围类型：1.部门  2.人员  3.岗位角色
			int releaseType=1;
			if("radio_department".equals(this.releaseBounds)){
				releaseType=1;
			}else if("radio_staff".equals(this.releaseBounds)){
				releaseType=2;
//				if(this.selectedCkStaff!=null && !this.selectedCkStaff.isEmpty()){
//					for(String staff:this.selectedCkStaff){
//						this.releaseScopeList+=staff+"-";
//					}
//					
//				}
				//this.releaseScopeList=this.releaseScopeList.substring(0, this.releaseScopeList.lastIndexOf("-"));
				//System.out.println("scopeList=="+this.releaseScopeList);
				
				//this.releaseScopeStaffName=this.releaseScopeStaffName.substring(0, this.releaseScopeStaffName.lastIndexOf("-"));
				//System.out.println("releaseScopeStaffName=="+this.releaseScopeStaffName);
				
			}else if("radio_role".equals(this.releaseBounds)){
				releaseType=3;
				//this.releaseScopeList=this.releaseScopeList.substring(0, this.releaseScopeList.lastIndexOf("-"));
				//System.out.println("scopeList=="+this.releaseScopeList);
				this.releaseRole=this.roleCode;
				//System.out.println("releaseRole=="+this.releaseRole);
			}
			
			cmsInforelease.setId(this.infoReleaseId);
			cmsInforelease.setInfoId(this.infoItemId);
			cmsInforelease.setReleaseScopeType(releaseType);
			cmsInforelease.setReleaseScopeList(this.releaseScopeList);
			cmsInforelease.setReleaseRole(this.releaseRole);
			cmsInforelease.setReleaseScopeStaffName(this.releaseScopeStaffName);
			cmsInforelease.setLastModifiedTime(new Date());
			this.cmsManageService.approveInfoRelease(cmsInfoitem, infoItemId, infoReleaseId, releaseOrApprove, userCnName, radioApprover, cmsInforelease, superiorsAccId, humanResourcesId, qualityId, companyLeadersId, releaseScopeList, releaseRole, uploadPic, uploadPicFileName, picName, picUrl, attaName, attaUrl, uploadAttachment, uploadAttachmentFileName, userId);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String returnString =  "操作成功";
		Map<String, Object> requestMap = new HashMap<String, Object>();
		//requestMap.put("urlAction", "\"loadInfoReleaseAction\"");	
		requestMap.put("returnString", returnString);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(requestMap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加短信信息
	* @date Nov 12, 20124:56:38 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void addsmsInfoAction(){
		String returnString = "";
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			String userCnName=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERNAME);
			cmsInfoitem.setAuthor(userId);//设置拟稿人
			cmsInfoitem.setDrafttime(new Date());//设置拟稿时间
			cmsInfoitem.setStatus(CmsConstant.CMS_DRAFT);//设为初稿状态
			cmsInfoitem.setInfoType("sms");
			long saveCmsInfoitem = this.cmsManageService.saveCmsInfoitem(cmsInfoitem);
			if(saveCmsInfoitem > 0){
				
				//查找新信息的id
				long newItemId=saveCmsInfoitem;
				this.infoItemId=newItemId;
				cmsInfoitem.setId(newItemId);
			}else{
				this.infoItemId=0;
			}
			returnString = this.cmsManageService.addsmsInfo(cmsInforelease,cmsInfoitem, infoItemId, releaseBounds, roleCode, releaseScopeList, releaseScopeStaffName
					, userId, userCnName, releaseOrApprove, radioApprover
					, superiorsAccId, infoReleaseStaffId, humanResourcesId, qualityId, companyLeadersId);
			ActionContext ctx = ActionContext.getContext();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("urlAction", "\"loadInfoReleaseAction\"");	
		requestMap.put("returnString", returnString);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(requestMap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转查看短信信息页面
	 * @return
	 */
	public String loadInfoItemSMSPageAction(){
		
		try {
			//获取信息类型
			this.cmsCategoryList= this.cmsManageService.getCmsCategoryList();
			
			//获取重要级别
			this.cmsImportantLevel =this.cmsManageService.getCmsImportantLevelList();
			
			
			Map<String, Object> inforeleaseById = this.cmsManageService.getInforeleaseById(this.infoReleaseId);
			this.infoReleaseEntity = inforeleaseById;
			long infoId = 0;
			if(inforeleaseById != null){
				if(inforeleaseById.get("infoId") != null && !inforeleaseById.get("infoId").equals("")){
					infoId = Long.parseLong(inforeleaseById.get("infoId").toString());
				}
			}
			//根据id获取信息项
			Map<String, Object> infoItemById = this.cmsManageService.getInfoItemById(infoId);
			this.infoItemEntity=infoItemById;
			this.infoItemId = infoId;
			//获取信息类型
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			//获取紧急程度
			this.cmsUrgencyLevelList =this.cmsManageService.getCmsUrgencyLevelList();
			
			//获取门户item列表
			this.cmsPortalItemList =this.cmsManageService.getPortalItemList();
			
			//获取信息发布对象
			this.isApprover = this.cmsManageService.getIsApprover(userId, infoReleaseId, infoItemId);
			//System.out.println("isApprover===="+this.isApprover);
			Map<String,Object> tempMap=inforeleaseById;
			if(tempMap.get("releasePeriodStart")==null){
				tempMap.put("releasePeriodStart","");
			}
			if(tempMap.get("releasePeriodEnd")==null){
				tempMap.put("releasePeriodEnd","");
			}
			if(tempMap.get("releaseHistory")==null){
				tempMap.put("releaseHistory","");
			}
			if(tempMap.get("lastModifiedTime")==null){
				tempMap.put("lastModifiedTime","");
			}
			this.infoReleaseEntity=tempMap;
			
			//判断当前登录人是否当前审核人
			String approver=inforeleaseById.get("auditor")==null?"":inforeleaseById.get("auditor").toString();
			
			if(userId.equals(approver)){
				this.isOperator=1;
			}else{
				this.isOperator=0;
			}
			
			//获取信息发布的状态
			if(infoItemById != null && !infoItemById.isEmpty()){
				int itemStatus=Integer.parseInt(infoItemById.get("status").toString());
				this.infoReleaseStatus=itemStatus;
			}
			
			
			superiorsList = new ArrayList<Map<String,Object>>();
			superiorsList = new ArrayList<Map<String,Object>>();
			superiorsList = cmsManageService.getSuperiorsList(userId);
			//获取所有角色
			this.roleList = this.cmsManageService.getAllRoleType();
			
			//质量安全部
			this.qualityList = this.cmsManageService.getQualitySuperiorsList();
			//人力资源部
			this.humanResourcesList = this.cmsManageService.getHumanResourcesSuperiorsList();
			
			//公司领导
			this.companyLeadersList = this.cmsManageService.getCompanyLeadersList();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "success";
	}
	
	
	/**
	 * 获取用户所属的组织和发布权限限制
	 */
	public void getUserOfOrganizationAndReleaseLimitAction(){
		try {
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			
			//System.out.println(biId);
			String userOfOrganizationAndReleaseLimit = this.cmsManageService.getUserOfOrganizationAndReleaseLimit(userId);
			String result="{\"userOrg\":\""+userOfOrganizationAndReleaseLimit+"\"}";
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 驳回
	* @date Dec 7, 201210:10:14 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void rejectInfoItemByInfoItemIdAction(){
		long rejectInfoItemByInfoItemId = this.cmsManageService.rejectInfoItemByInfoItemId(infoItemId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(rejectInfoItemByInfoItemId);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 新建普通信息action(项目考核)
	 * @return
	 */
	public void addInfoItemReportAction(){
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			
			//从session获取user
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			cmsInfoitem.setId(this.infoItemId);
	
			this.infoItemId = this.cmsManageService.saveOrUpdateCmsInfoItemReport(cmsReportProjectAppraisals,addorUpdate, cmsInfoitem, uploadPic, uploadPicFileName, picName, picUrl, attaName, attaUrl, uploadAttachment, uploadAttachmentFileName, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(infoItemId);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getCmsPeportProjectAppraisalAction(){
		List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageService.getCmsPeportProjectAppraisalChar(projectName, year);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(cmsPeportProjectAppraisal);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getCmsPeportProjectAppraisalTableAction(){
		List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageService.getCmsPeportProjectAppraisal(projectName, year);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(cmsPeportProjectAppraisal);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> getRequestMap() {
		return requestMap;
	}


	public void setRequestMap(Map<String, Object> requestMap) {
		this.requestMap = requestMap;
	}


	public String getIds() {
		return ids;
	}


	public void setIds(String ids) {
		this.ids = ids;
	}


	public String getTabType() {
		return tabType;
	}


	public void setTabType(String tabType) {
		this.tabType = tabType;
	}


	public long getOrgId() {
		return orgId;
	}


	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}


	public String getReleaseBounds() {
		return releaseBounds;
	}


	public void setReleaseBounds(String releaseBounds) {
		this.releaseBounds = releaseBounds;
	}


	public String getReleaseScopeList() {
		return releaseScopeList;
	}


	public void setReleaseScopeList(String releaseScopeList) {
		this.releaseScopeList = releaseScopeList;
	}


	public String getReleaseScopeStaffName() {
		return releaseScopeStaffName;
	}


	public void setReleaseScopeStaffName(String releaseScopeStaffName) {
		this.releaseScopeStaffName = releaseScopeStaffName;
	}


	public String getReleaseRole() {
		return releaseRole;
	}


	public void setReleaseRole(String releaseRole) {
		this.releaseRole = releaseRole;
	}


	public List<String> getSelectedCkStaff() {
		return selectedCkStaff;
	}


	public void setSelectedCkStaff(List<String> selectedCkStaff) {
		this.selectedCkStaff = selectedCkStaff;
	}


	public int getReleaseOrApprove() {
		return releaseOrApprove;
	}


	public void setReleaseOrApprove(int releaseOrApprove) {
		this.releaseOrApprove = releaseOrApprove;
	}


	public String getRadioApprover() {
		return radioApprover;
	}


	public void setRadioApprover(String radioApprover) {
		this.radioApprover = radioApprover;
	}


	public int getIsApprover() {
		return isApprover;
	}


	public void setIsApprover(int isApprover) {
		this.isApprover = isApprover;
	}


	public List<Map<String, Object>> getCmsReleaseList() {
		return cmsReleaseList;
	}


	public void setCmsReleaseList(List<Map<String, Object>> cmsReleaseList) {
		this.cmsReleaseList = cmsReleaseList;
	}


	public Map<String, Object> getCmsAjaxMap() {
		return cmsAjaxMap;
	}


	public void setCmsAjaxMap(Map<String, Object> cmsAjaxMap) {
		this.cmsAjaxMap = cmsAjaxMap;
	}


	public List<Map<String, Object>> getCmsCategoryList() {
		return cmsCategoryList;
	}


	public void setCmsCategoryList(List<Map<String, Object>> cmsCategoryList) {
		this.cmsCategoryList = cmsCategoryList;
	}


	public List<Map<String, Object>> getCmsImportantLevel() {
		return cmsImportantLevel;
	}


	public void setCmsImportantLevel(List<Map<String, Object>> cmsImportantLevel) {
		this.cmsImportantLevel = cmsImportantLevel;
	}


	public List<Map<String, Object>> getCmsUrgencyLevelList() {
		return cmsUrgencyLevelList;
	}


	public void setCmsUrgencyLevelList(List<Map<String, Object>> cmsUrgencyLevelList) {
		this.cmsUrgencyLevelList = cmsUrgencyLevelList;
	}


	public List<Map<String, Object>> getCmsPortalItemList() {
		return cmsPortalItemList;
	}


	public void setCmsPortalItemList(List<Map<String, Object>> cmsPortalItemList) {
		this.cmsPortalItemList = cmsPortalItemList;
	}


	public Map<String, Object> getInfoItemEntity() {
		return infoItemEntity;
	}


	public void setInfoItemEntity(Map<String, Object> infoItemEntity) {
		this.infoItemEntity = infoItemEntity;
	}


	public long getInfoItemId() {
		return infoItemId;
	}


	public void setInfoItemId(long infoItemId) {
		this.infoItemId = infoItemId;
	}


	public long getInfoReleaseId() {
		return infoReleaseId;
	}


	public void setInfoReleaseId(long infoReleaseId) {
		this.infoReleaseId = infoReleaseId;
	}


	public List<Map<String, Object>> getSuperiorsList() {
		return superiorsList;
	}


	public void setSuperiorsList(List<Map<String, Object>> superiorsList) {
		this.superiorsList = superiorsList;
	}


	public List<Map<String, Object>> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<Map<String, Object>> roleList) {
		this.roleList = roleList;
	}


	public List<Map<String, Object>> getCompanyLeadersList() {
		return companyLeadersList;
	}


	public void setCompanyLeadersList(List<Map<String, Object>> companyLeadersList) {
		this.companyLeadersList = companyLeadersList;
	}


	public Map<String, Object> getInfoReleaseEntity() {
		return infoReleaseEntity;
	}


	public void setInfoReleaseEntity(Map<String, Object> infoReleaseEntity) {
		this.infoReleaseEntity = infoReleaseEntity;
	}


	public String getSuperiorsAccId() {
		return superiorsAccId;
	}


	public void setSuperiorsAccId(String superiorsAccId) {
		this.superiorsAccId = superiorsAccId;
	}


	public String getCompanyLeadersId() {
		return companyLeadersId;
	}


	public void setCompanyLeadersId(String companyLeadersId) {
		this.companyLeadersId = companyLeadersId;
	}


	public int getIsOperator() {
		return isOperator;
	}


	public void setIsOperator(int isOperator) {
		this.isOperator = isOperator;
	}


	public int getInfoReleaseStatus() {
		return infoReleaseStatus;
	}


	public void setInfoReleaseStatus(int infoReleaseStatus) {
		this.infoReleaseStatus = infoReleaseStatus;
	}


	public String getRoleCode() {
		return roleCode;
	}


	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}


	public String getInfoReleaseStaffId() {
		return infoReleaseStaffId;
	}


	public void setInfoReleaseStaffId(String infoReleaseStaffId) {
		this.infoReleaseStaffId = infoReleaseStaffId;
	}


	public CmsInfoitem getCmsInfoitem() {
		return cmsInfoitem;
	}


	public void setCmsInfoitem(CmsInfoitem cmsInfoitem) {
		this.cmsInfoitem = cmsInfoitem;
	}


	public SimpleDateFormat getSdf() {
		return sdf;
	}


	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}


	public List<Map<String, Object>> getApproverList() {
		return approverList;
	}


	public void setApproverList(List<Map<String, Object>> approverList) {
		this.approverList = approverList;
	}


	public Map<String, Object> getDefaultApprover() {
		return defaultApprover;
	}


	public void setDefaultApprover(Map<String, Object> defaultApprover) {
		this.defaultApprover = defaultApprover;
	}


	public String getNewDefaultApprover() {
		return newDefaultApprover;
	}


	public void setNewDefaultApprover(String newDefaultApprover) {
		this.newDefaultApprover = newDefaultApprover;
	}


	public String getSelectUser() {
		return selectUser;
	}


	public void setSelectUser(String selectUser) {
		this.selectUser = selectUser;
	}


	public int getSelectUserLimit() {
		return selectUserLimit;
	}


	public void setSelectUserLimit(int selectUserLimit) {
		this.selectUserLimit = selectUserLimit;
	}


	public int getOrgRole() {
		return orgRole;
	}


	public void setOrgRole(int orgRole) {
		this.orgRole = orgRole;
	}


	public int getRoleLimitLevel() {
		return roleLimitLevel;
	}


	public void setRoleLimitLevel(int roleLimitLevel) {
		this.roleLimitLevel = roleLimitLevel;
	}


	public File[] getUploadPic() {
		return uploadPic;
	}


	public void setUploadPic(File[] uploadPic) {
		this.uploadPic = uploadPic;
	}


	public String[] getUploadPicFileName() {
		return uploadPicFileName;
	}


	public void setUploadPicFileName(String[] uploadPicFileName) {
		this.uploadPicFileName = uploadPicFileName;
	}


	public String[] getUploadPicContentType() {
		return uploadPicContentType;
	}


	public void setUploadPicContentType(String[] uploadPicContentType) {
		this.uploadPicContentType = uploadPicContentType;
	}


	public File[] getUploadAttachment() {
		return uploadAttachment;
	}


	public void setUploadAttachment(File[] uploadAttachment) {
		this.uploadAttachment = uploadAttachment;
	}


	public String[] getUploadAttachmentFileName() {
		return uploadAttachmentFileName;
	}


	public void setUploadAttachmentFileName(String[] uploadAttachmentFileName) {
		this.uploadAttachmentFileName = uploadAttachmentFileName;
	}


	public String[] getUploadAttachmentContentType() {
		return uploadAttachmentContentType;
	}


	public void setUploadAttachmentContentType(String[] uploadAttachmentContentType) {
		this.uploadAttachmentContentType = uploadAttachmentContentType;
	}


	public String getAddorUpdate() {
		return addorUpdate;
	}


	public void setAddorUpdate(String addorUpdate) {
		this.addorUpdate = addorUpdate;
	}


	public String getItype() {
		return Itype;
	}


	public void setItype(String itype) {
		Itype = itype;
	}


	public List<Map<String, Object>> getQualityList() {
		return qualityList;
	}


	public void setQualityList(List<Map<String, Object>> qualityList) {
		this.qualityList = qualityList;
	}


	public List<Map<String, Object>> getHumanResourcesList() {
		return humanResourcesList;
	}


	public void setHumanResourcesList(List<Map<String, Object>> humanResourcesList) {
		this.humanResourcesList = humanResourcesList;
	}


	public String getQualityId() {
		return qualityId;
	}


	public void setQualityId(String qualityId) {
		this.qualityId = qualityId;
	}


	public String getHumanResourcesId() {
		return humanResourcesId;
	}


	public void setHumanResourcesId(String humanResourcesId) {
		this.humanResourcesId = humanResourcesId;
	}


	public List<Map<String, String>> getInfoReleasesList() {
		return infoReleasesList;
	}


	public void setInfoReleasesList(List<Map<String, String>> infoReleasesList) {
		this.infoReleasesList = infoReleasesList;
	}


	public CmsInforelease getCmsInforelease() {
		return cmsInforelease;
	}


	public void setCmsInforelease(CmsInforelease cmsInforelease) {
		this.cmsInforelease = cmsInforelease;
	}


	public String getCopyInfoItem() {
		return copyInfoItem;
	}


	public void setCopyInfoItem(String copyInfoItem) {
		this.copyInfoItem = copyInfoItem;
	}


	public String getAttaName() {
		return attaName;
	}


	public void setAttaName(String attaName) {
		this.attaName = attaName;
	}


	public String getAttaUrl() {
		return attaUrl;
	}


	public void setAttaUrl(String attaUrl) {
		this.attaUrl = attaUrl;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}



	

}
