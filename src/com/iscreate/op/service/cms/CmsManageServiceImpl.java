package com.iscreate.op.service.cms;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;

import com.iscreate.op.constant.CmsConstant;
import com.iscreate.op.constant.TreeConstant;
import com.iscreate.op.dao.cms.CmsManageDao;
import com.iscreate.op.dao.system.SysAccountDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.dao.system.SysUserRelaOrgDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.cms.CmsInfoitem;
import com.iscreate.op.pojo.cms.CmsInfoitemAppraisal;
import com.iscreate.op.pojo.cms.CmsInforelease;
import com.iscreate.op.pojo.cms.CmsReportProjectAppraisal;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.email.pojo.EmailSenderInfo;
import com.iscreate.plat.email.service.EmailServiceImpl;
import com.iscreate.plat.tools.FileHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tree.TreeNode;


public class CmsManageServiceImpl implements CmsManageService {
	
	private CmsManageDao cmsManageDao;
	private DataDictionaryService dataDictionaryService;
	private BizMessageService bizMessageService;
	private SysOrgUserService sysOrgUserService;

	private SysOrganizationDao sysOrganizationDao;//组织dao du.hw
	private SysUserRelaOrgDao sysUserRelaOrgDao;//用户关联 du.hw
	private SysAccountService sysAccountService;
		
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	private SysAccountDao sysAccountDao;//账号  du.hw

	

	public SysAccountDao getSysAccountDao() {
		return sysAccountDao;
	}

	public void setSysAccountDao(SysAccountDao sysAccountDao) {
		this.sysAccountDao = sysAccountDao;
	}

	public SysUserRelaOrgDao getSysUserRelaOrgDao() {
		return sysUserRelaOrgDao;
	}

	public void setSysUserRelaOrgDao(SysUserRelaOrgDao sysUserRelaOrgDao) {
		this.sysUserRelaOrgDao = sysUserRelaOrgDao;
	}

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

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
	public CmsManageDao getCmsManageDao() {
		return cmsManageDao;
	}
	public void setCmsManageDao(CmsManageDao cmsManageDao) {
		this.cmsManageDao = cmsManageDao;
	}
	

	/**
	 * 批量删除公告信息
	* @date Nov 12, 201210:14:41 AM
	* @Description: TODO 
	* @param @param ids
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInforelease(String[] ids){
		boolean isSuccess=false;
		try {
			boolean flag=false;
			int deleteCount=0;
			try {
				if(ids!=null && ids.length>0){
					for(String id:ids){
						CmsInforelease cms = new CmsInforelease();
						Long valueOf = Long.valueOf(id);
						cms.setId(valueOf);
						try {
							CmsInforelease inforeleasePoJoById = this.cmsManageDao.getInforeleasePoJoById(valueOf);
							if(inforeleasePoJoById != null ){
								
								boolean deleteCmsInforelease = cmsManageDao.deleteCmsInforelease(cms);
								if(deleteCmsInforelease == true){
									deleteCount++;
								}
							}
						} catch (org.hibernate.StaleStateException e) {
							// TODO: handle exception
						}
					}
				}
				if(deleteCount>0){
					flag=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
		
	}
	
	
	/**
	 * 批量删除公告信息
	* @date Nov 12, 201210:14:41 AM
	* @Description: TODO 
	* @param @param ids
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInfoItem(String[] ids){
		boolean isSuccess=false;
		try {
			boolean flag=false;
			int deleteCount=0;
			try {
				if(ids!=null && ids.length>0){
					for(String id:ids){
						CmsInfoitem cms = new CmsInfoitem();
						Long valueOf = Long.valueOf(id);
						cms.setId(valueOf);
						try {
							CmsInfoitem infoItemPoJoById = this.cmsManageDao.getInfoItemPoJoById(valueOf);
							if(infoItemPoJoById != null ){
								
								boolean deleteCmsInfoItem = cmsManageDao.deleteCmsInfoItem(cms);
								if(deleteCmsInfoItem == true){
									
									deleteCount++;
								}
							}
						} catch (org.hibernate.StaleStateException e) {
							// TODO: handle exception
						}
					}
				}
				if(deleteCount>0){
					flag=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return flag;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
		
	}
	
	
	/**
	 * 根据userId和tab类型查询公告发布消息
	 * @param userId
	 * @param tabType
	 * @return
	 */
	public List<Map<String, Object>> getInfoReleaseListByTabType(String userId, String tabType){
		List<Map<String,Object>> infoReleaseListByCondition = getInfoReleaseListByCondition(userId,tabType);
		if(infoReleaseListByCondition != null){
			for(Map<String,Object> map:infoReleaseListByCondition){
				if(map.get("status") != null){
					Integer status = Integer.valueOf(map.get("status").toString());
					map.put("statusName", getStatusName(status));
				}
			}
		}
		return infoReleaseListByCondition;
	}
	
	private String getStatusName(int status){
		if(status == CmsConstant.CMS_DRAFT){
			return "初稿";
		}else if(status == CmsConstant.CMS_WAIT4APPROVAL){
			return "待审核";
		}else if(status == CmsConstant.CMS_RELEASEED){
			return "已发布";
		}else if(status == CmsConstant.CMS_OVERTIME){
			return "已过期";
		}else if(status == CmsConstant.CMS_RELEASECANCEL){
			return "发布取消";
		}else if(status == CmsConstant.CMS_REJECT){
			return "发布驳回";
		}else{
			return "";
		}
	}
	
	/**
	 * 根据条件查询公告发布消息
	 * @param userId 用户id
	 * @param tabType 对应tab的公告信息
	 * @return
	 */
	private List<Map<String, Object>> getInfoReleaseListByCondition(String userId,String tabType) {
		//List<BasicEntity> aeList = new ArrayList<BasicEntity>();
		List<Map<String, Object>> mapList=null;
		try {
			
			String useFlag="useQuery";
			
			//根据tabType，判断是用query或写sql，默认是使用query
			if(tabType!=null && !"".equals(tabType)){
				if("overReleaseTime".equals(tabType) || "draftByMe".equals(tabType) 
						|| "waitAudit".equals(tabType) || "allInfo".equals(tabType) || "latestRelease".equals(tabType)){
					useFlag="useSql";
				}
			}
			
			if("useQuery".equals(useFlag)){
			}else if("useSql".equals(useFlag)){
				String sql=this.cmsManageDao.createSqlContainer(tabType,userId);
				mapList = cmsManageDao.getReportComment(sql);
				if(mapList == null){
					mapList = new ArrayList<Map<String,Object>>();
				}else{
					for(Map<String,Object> map :mapList){
						if(map.get("drafttime") != null){
							String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(map.get("drafttime").toString(), "yyyy-MM-dd HH:mm:ss");
							map.put("drafttimeString", strCurrentTime);
							String lastModifiedTime = TimeFormatHelper.getTimeFormatByFree(map.get("lastModifiedTime").toString(), "yyyy-MM-dd HH:mm:ss");
							map.put("lastModifiedTimeString", lastModifiedTime);
						}
					}
				}
				if("draftByMe".equals(tabType)){
					List<Map<String,Object>> draftInfoItem = this.cmsManageDao.getDraftInfoItem(userId);
					if(draftInfoItem != null && !draftInfoItem.isEmpty()){
						mapList.addAll(draftInfoItem);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mapList;
	}
	
	/**
	 * 组装公告发布信息各tabType对应的信息大小
	 * @param tabType
	 * @param resultMap
	 * @param resultList
	 * @return
	 */
	private Map<String,String> formatCmsReleaseCount(String tabType,Map<String,String> resultMap,List<Map<String, Object>> resultList){
		if(resultList!=null && !resultList.isEmpty()){
			int listSize=resultList.size();
			resultMap.put(tabType, String.valueOf(listSize));
		}else{
			resultMap.put(tabType, String.valueOf(0));
		}
		return resultMap;
	}
	
	
	/**
	 * 根据用户id和tab类型，获取对应的公告发布信息数量
	 * @param userId
	 * @return
	 */
	public Map<String,String> getCmsReleaseCountByTabType(String userId){
		Map<String,String> tabTotal=new HashMap<String,String>();
		try {
			//获取待审核
			String tabType="waitAudit";
			List<Map<String, Object>> aeList=getInfoReleaseListByCondition(userId,tabType);
			formatCmsReleaseCount(tabType,tabTotal,aeList);
			
			//获取最近发布
			tabType="latestRelease";
			aeList=getInfoReleaseListByCondition(userId,tabType);
			formatCmsReleaseCount(tabType,tabTotal,aeList);
			
			//获取由我拟稿
			tabType="draftByMe";
			aeList=getInfoReleaseListByCondition(userId,tabType);
			formatCmsReleaseCount(tabType,tabTotal,aeList);
			
			//获取全部信息
			tabType="allInfo";
			aeList=getInfoReleaseListByCondition(userId,tabType);
			formatCmsReleaseCount(tabType,tabTotal,aeList);
			
			//获取已过期
			tabType="overReleaseTime";
			aeList=getInfoReleaseListByCondition(userId,tabType);
			formatCmsReleaseCount(tabType,tabTotal,aeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tabTotal;
	}
	
	/**
	 * 获取上级领导
	 * du.hw修改
	* @date Nov 12, 201211:05:54 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSuperiorsList(String account){
		List<Map<String, Object>> superiorsList = new ArrayList<Map<String,Object>>();
		SysAccount accountInfo = sysAccountDao.getSysAccountByAccount(account);//账号信息
		
		//得到账号的上级标识
		List<Map<String,Object>> list = sysUserRelaOrgDao.getAllTopUserByOrgUserId(accountInfo.getOrgUserId());
		for(Map<String,Object> map:list){//去掉领导中的当前用户
			Map<String, Object> userMap = new HashMap<String, Object>();
			if(!map.get("account").equals(account)){
				 userMap.put("staffId", map.get("account"));
				 userMap.put("staffName", map.get("name"));
				 userMap.put("orgUserId", map.get("orgUserId"));
				 superiorsList.add(userMap);
			}
		}
		return superiorsList;
	}
	
	/**
	 * 获取所有角色
	* @date Nov 12, 201211:08:03 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getAllRoleType(){
		List<SysRole> allSysRole = this.sysOrgUserService.getAllSysRole();
//		List<Role> allOrgRole = this.providerOrganizationService.getAllOrgRoleToRoleService();
//		List<Role> allBizRole = this.providerOrganizationService.getAllBizRoleToRoleService();
		List<Map<String, Object>> roleList = null;
		if(allSysRole != null && allSysRole.size() > 0){
			roleList = new ArrayList<Map<String,Object>>();
			for(SysRole role:allSysRole){
				Map<String, Object> map = new HashMap<String, Object>();
				String name = role.getName();
				String roleCode = role.getCode();
				map.put("name", name);
				map.put("roleCode", roleCode);
				roleList.add(map);
			}
		}
//		if(allBizRole != null && allBizRole.size() > 0){
//			roleList = new ArrayList<Map<String,Object>>();
//			for(Role role:allBizRole){
//				Map<String, Object> map = new HashMap<String, Object>();
//				String name = role.getName();
//				String roleCode = role.getCode();
//				map.put("name", name);
//				map.put("roleCode", roleCode);
//				roleList.add(map);
//			}
//		}
		return roleList;
	}
	
	
	/**
	 * 获取Cms信息类型列表
	 * @return
	 */
	public List<Map<String, Object>> getCmsCategoryList(){
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		List<TreeNode> dictionaryByTreeId = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.CMS_CATEGORY);
		if(dictionaryByTreeId != null && dictionaryByTreeId.size() > 0){
			for(TreeNode tr:dictionaryByTreeId){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tr.getReferenceValue());
				map.put("cmsCategory", tr.getTreeNodeName());
				rList.add(map);
			}
		}
		return rList;
	}
	
	/**
	 * 获取发布的栏目列表
	* @date Nov 12, 201211:22:10 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPortalItemList(){
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		List<TreeNode> dictionaryByTreeId = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.CMS_PORTALITEM);
		if(dictionaryByTreeId != null && dictionaryByTreeId.size() > 0){
			for(TreeNode tr:dictionaryByTreeId){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemCode", tr.getReferenceValue());
				map.put("itemName", tr.getTreeNodeName());
				rList.add(map);
			}
		}
		return rList;
	}
	
	/**
	 * 获取重要级别
	* @date Nov 12, 201211:33:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsImportantLevelList(){
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		List<TreeNode> dictionaryByTreeId = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.CMS_IMPORTANTLEVEL);
		if(dictionaryByTreeId != null && dictionaryByTreeId.size() > 0){
			for(TreeNode tr:dictionaryByTreeId){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tr.getReferenceValue());
				map.put("cmsImportantLevel", tr.getTreeNodeName());
				rList.add(map);
			}
		}
		return rList;
	}
	
	/**
	 * 紧急程度
	* @date Nov 12, 201211:33:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsUrgencyLevelList(){
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		List<TreeNode> dictionaryByTreeId = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.CMS_URGENCYLEVEL);
		if(dictionaryByTreeId != null && dictionaryByTreeId.size() > 0){
			for(TreeNode tr:dictionaryByTreeId){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tr.getReferenceValue());
				map.put("cmsUrgencyLevel", tr.getTreeNodeName());
				rList.add(map);
			}
		}
		return rList;
	}
	
	
	
	/**
	 * 质量安全部
	* @date Nov 12, 201211:26:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getQualitySuperiorsList(){
		List<Map<String, Object>>  superiorsList = new ArrayList<Map<String,Object>>();
		//质量安全部
		List<SysOrg> organizationByOrgNameAndOrgCode = sysOrganizationDao.getOrganizationByOrgNameAndOrgCode("质量安全部", "FunctionDivision");
		SysOrg organization = null;
		if(organizationByOrgNameAndOrgCode != null){
			organization = organizationByOrgNameAndOrgCode.get(0);
		}else{
			return null;
		}
//		SysOrg organization = sysOrganizationDao.getOrgByOrgId(58);
		if(organization != null){
			if(organization.getOrgUserId() != null && !organization.getOrgUserId().equals(0)){
				//ou.jh
				SysAccount accountInfo = sysAccountDao.getSysAccountByOrgUserId(organization.getOrgUserId());
				SysOrgUser sysOrgUserByAccount2 = this.sysOrgUserService.getSysOrgUserByAccount(accountInfo.getAccount());
				SysAccount sysAccountByAccount2 = this.sysAccountService.getSysAccountByAccount(accountInfo.getAccount());
//				Staff staffByAccount2 = providerOrganizationService.getStaffByAccount(organization.getDutyPerson());
				if(sysOrgUserByAccount2 != null && sysAccountByAccount2 != null){
					String name = sysOrgUserByAccount2.getName();
					String account = sysAccountByAccount2.getAccount();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("staffId", account);
					map.put("staffName", name);
					superiorsList.add(map);
				}
			}
		}
		return superiorsList;
	}
	
	
	/**
	 * 人力资源部
	* @date Nov 12, 201211:26:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHumanResourcesSuperiorsList(){
		List<Map<String, Object>>  superiorsList = new ArrayList<Map<String,Object>>();
		//人力资源部
		List<SysOrg> organizationByOrgNameAndOrgCode = sysOrganizationDao.getOrganizationByOrgNameAndOrgCode("人力资源部", "FunctionDivision");
		SysOrg organization = null;
		if(organizationByOrgNameAndOrgCode != null){
			organization = organizationByOrgNameAndOrgCode.get(0);
		}else{
			return null;
		}
//		SysOrg organization = this.sysOrganizationDao.getOrgByOrgId(59);
		if(organization != null){
			if(organization.getOrgUserId() != null && !organization.getOrgUserId().equals(0)){
				//ou.jh
				SysAccount accountInfo = sysAccountDao.getSysAccountByOrgUserId(organization.getOrgUserId());
				SysOrgUser sysOrgUserByAccount2 = this.sysOrgUserService.getSysOrgUserByAccount(accountInfo.getAccount());
				SysAccount sysAccountByAccount2 = this.sysAccountService.getSysAccountByAccount(accountInfo.getAccount());

//				Staff staffByAccount2 = providerOrganizationService.getStaffByAccount(organization.getDutyPerson());
				if(sysOrgUserByAccount2 != null && sysAccountByAccount2 != null){
					String name = sysOrgUserByAccount2.getName();
					String account = sysAccountByAccount2.getAccount();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("staffId", account);
					map.put("staffName", name);
					superiorsList.add(map);
				}
			}
		}
		return superiorsList;
	}
	
	/**
	 * 获取公司领导
	* @date Nov 12, 201211:30:18 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCompanyLeadersList(){
		List<Map<String, Object>>  superiorsList = new ArrayList<Map<String,Object>>();
		//ou.jh
		List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(16);
		//List<Staff> staffListByOrgId = this.providerOrganizationService.getStaffListByOrgIdService(16);
		if(userByOrgId != null && !userByOrgId.isEmpty()){
			for(Map<String,Object> staff:userByOrgId){
				String name = "";
				String account = "";
				if(staff.get("account") != null){
					account = staff.get("account")+"";
				}else{
					continue;
				}
				if(staff.get("name") != null){
					name = staff.get("name")+"";
				}else{
					continue;
				}
					
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("staffId", account);
				map.put("staffName", name);
				superiorsList.add(map);
			}
		}
		return superiorsList;
	}

	/**
	 * 添加或更新普通信息对象
	* @date Dec 4, 201210:53:05 AM
	* @Description: TODO 
	* @param @param addorUpdate
	* @param @param cmsInfoitem
	* @param @param uploadPic
	* @param @param uploadPicFileName
	* @param @param picName
	* @param @param picUrl
	* @param @param attaName
	* @param @param attaUrl
	* @param @param uploadAttachment
	* @param @param uploadAttachmentFileName
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public long saveOrUpdateCmsInfoItem(String addorUpdate,CmsInfoitem cmsInfoitem,File[] uploadPic,String[] uploadPicFileName,String picName,String picUrl,String attaName ,String attaUrl
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId){
		//上传图片
		if(uploadPic!=null && uploadPic.length>0 && uploadPic[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadPic, uploadPicFileName);
			if(picUrl == null){
				picUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			picUrl = picUrl + url;
			
			if(picName == null){
				picName = "";
			}
			String picNameP = "";
			if(uploadPicFileName!=null && uploadPicFileName.length > 0){
				for(String path:uploadPicFileName){
					picNameP=picNameP+path+"#";
				}
			}
			picName = picName + picNameP;
			
//			entity.setValue("pictures", saveFilePaths.get(0).substring(saveFilePaths.get(0).lastIndexOf("upload"),saveFilePaths.get(0).length()));
		}
		cmsInfoitem.setPictures(picName);
		cmsInfoitem.setPicture_url(picUrl);
//		
//		entity.setValue("infoType", "normal");
		//上传附件
		if(uploadAttachment!=null && uploadAttachment.length>0 && uploadAttachment[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadAttachment, uploadAttachmentFileName);
			if(attaUrl == null){
				attaUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			attaUrl = attaUrl + url;
			
			String attachmentPath="";
			if(uploadAttachmentFileName!=null && uploadAttachmentFileName.length > 0){
				for(String path:uploadAttachmentFileName){
					attachmentPath=attachmentPath+path+"#";
				}
			}
			if(attaName == null){
				attaName = "";
			}
			attaName = attaName + attachmentPath;
			//entity.setValue("attachments", attachmentPath);
		}
		cmsInfoitem.setAttachments(attaName);
		cmsInfoitem.setUrl(attaUrl);
		cmsInfoitem.setInfoType("normal");
		long rId = 0;
		if(addorUpdate != null){	
			if(addorUpdate.equals("add")){
				cmsInfoitem.setAuthor(userId);//设置拟稿人
				cmsInfoitem.setStatus(CmsConstant.CMS_DRAFT);	//设为初稿状态
				cmsInfoitem.setDrafttime(new Date());//设置拟稿时间
				rId = this.cmsManageDao.saveCmsInfoitem(cmsInfoitem);
			}else if(addorUpdate.equals("update")){
				rId = cmsInfoitem.getId();
				this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
			}
		}
		return rId;
		
	}
	
	/**
	 * 根据ID获取普通信息
	* @date Nov 12, 201211:55:25 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getInfoItemById(long infoItemId){
		return this.cmsManageDao.getInfoItemById(infoItemId);
	}
	
	/**
	 * 根据ID获取普通信息
	* @date Nov 12, 201211:55:25 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public CmsInfoitem getInfoItemPoJoById(long infoItemId){
		return this.cmsManageDao.getInfoItemPoJoById(infoItemId);
	}
	
	/**
	 * 添加公告消息与更新普通信息
	* @date Nov 12, 20123:17:34 PM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @param releaseOrApprove
	* @param @param userCnName
	* @param @param infoItemId
	* @param @param radioApprover
	* @param @param superiorsAccId
	* @param @param humanResourcesId
	* @param @param qualityId
	* @param @param companyLeadersId
	* @param @param releaseScopeType
	* @param @param releaseScopeList
	* @param @param releaseRole
	* @param @param userId        
	* @throws
	 */
	public String saveCmsInforeleaseAndUpdateCmsInfoItem(CmsInforelease cmsInforelease,int releaseOrApprove,String userCnName,long infoItemId,String radioApprover,String superiorsAccId,
			String humanResourcesId,String qualityId,String companyLeadersId, int releaseScopeType,String releaseScopeList,String releaseRole,
			String userId){
		String returnString = "";
		long updateCmsInfoitem = 0;
		long saveCmsInforelease = 0;
		//判断是发布成功还是要等待审批
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		if(1==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，提交发布");
		}else if(3==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，审批不通过");
		}else if(4==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，审批通过");
		}
		
		//获取信息发布对应的普通的信息，设置其状态
		CmsInfoitem cmsInfoitem = getInfoItemPoJoById(infoItemId);
		
		
		//不为空，即要审批，要选择审批人
		if(radioApprover!=null && !"".equals(radioApprover)){
			String approver="";
			if(CmsConstant.CMS_DIREXTBOX.equals(radioApprover)){	//直接上司
				approver = superiorsAccId;
			}else if(CmsConstant.CMS_INFORELEASEAPPROVER.equals(radioApprover)){		//信息发布审批员
				//approver = this.infoReleaseStaffId;
			}else if(CmsConstant.CMS_HR.equals(radioApprover)){	//人事部
				approver = humanResourcesId;
			}else if(CmsConstant.CMS_QUALITYAPPROVER.equals(radioApprover)){//质量安全部
				approver = qualityId;
			}
			else if(CmsConstant.CMS_LEADER.equals(radioApprover)){	//公司领导
				approver = companyLeadersId;
			}
			cmsInforelease.setAuditor(approver);//设置审核人
			cmsInfoitem.setStatus(CmsConstant.CMS_WAIT4APPROVAL);
			
		}else{
			cmsInfoitem.setStatus(CmsConstant.CMS_RELEASEED);
		}
		updateCmsInfoitem = this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
		saveCmsInforelease = this.cmsManageDao.saveCmsInforelease(cmsInforelease);
		if(cmsInfoitem.getStatus() == CmsConstant.CMS_RELEASEED){
			
			sendBizMessageOrSmsOrEmail(cmsInforelease, releaseScopeType,
					releaseScopeList, releaseRole, userId, cmsInfoitem);
		}
		if(updateCmsInfoitem > 0 && saveCmsInforelease > 0){
			returnString =  "操作成功";
		}else{
			returnString =  "操作失败";
		}
		return returnString;
	}
	
	/**
	 * 推送信息盒子，短信，邮件
	* @date Nov 12, 20123:24:18 PM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @param releaseScopeType
	* @param @param releaseScopeList
	* @param @param releaseRole
	* @param @param userId
	* @param @param cmsInfoitem        
	* @throws
	 */
	private void sendBizMessageOrSmsOrEmail(CmsInforelease cmsInforelease,
			int releaseScopeType, String releaseScopeList, String releaseRole,
			String userId, CmsInfoitem cmsInfoitem) {
		if(cmsInfoitem != null){
			
			if(cmsInfoitem.getStatus() > 0 && cmsInfoitem.getStatus() == CmsConstant.CMS_RELEASEED){
				if(cmsInforelease != null){
					if(cmsInforelease.getIsSentToMsgbox() != null && cmsInforelease.getIsSentToMsgbox()  == 1){
						List<String> arrayList = new ArrayList<String>();
						Set<String> acIdSet = new HashSet<String>();
						getSaffAccount(releaseScopeType, releaseScopeList,
								releaseRole, acIdSet);
						if(acIdSet != null && acIdSet.size() > 0){
							String content = "";
							if(cmsInfoitem.getContent() != null && !cmsInfoitem.getContent().equals("")){
								content = cmsInfoitem.getContent();
							}
							String title = "";
							if(cmsInfoitem.getTitle() != null && !cmsInfoitem.getTitle().equals("")){
								title = cmsInfoitem.getTitle();
							}
							for(String acId:acIdSet){
								//shortMessageService.sendShortMessage(phoneNumber, content);
								//消息盒子
								BizMessage bizMessage = new BizMessage();
								bizMessage.setSendPerson(userId);
								bizMessage.setReceivePerson(acId);
								bizMessage.setContent(content);
								bizMessage.setType("信息发布");
								bizMessage.setTitle(title);
								bizMessage.setFunctionType("Cms");
								bizMessage.setSendTime(new Date());
								bizMessage.setLink("op/cms/getAnnouncementDetailAction?infoId="+cmsInfoitem.getId());
								bizMessageService.txAddBizMessageService(bizMessage,"note");
							}
						}
					}
					
					
				}if(cmsInforelease.getIsInformedWithsms() != null && cmsInforelease.getIsInformedWithsms() > 0){
					if(cmsInforelease.getIsInformedWithsms() == 1){
						//短信通知
						
						List<String> arrayList = new ArrayList<String>();
						Set<String> acIdSet = new HashSet<String>();
						getSaffAccount(releaseScopeType, releaseScopeList,
								releaseRole, acIdSet);
						if(acIdSet != null && acIdSet.size() > 0){
							String content = "";
							if(cmsInfoitem.getContent() != null && !cmsInfoitem.getContent().equals("")){
								content = cmsInfoitem.getContent();
							}
							OutLinkingService outLinkingService = new OutLinkingService();
							Map mailInfoMap = outLinkingService.getEmailSettingInfo();
							
							for(String acId:acIdSet){
								//shortMessageService.sendShortMessage(phoneNumber, content);
								//短信
								//ou.jh
								SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(acId);
//								Account accountByAccountId = this.providerOrganizationService.getAccountByAccountId(acId);
								if(sysOrgUserByAccount != null){
									String mobileEmailAddress = sysOrgUserByAccount.getMobileemail();
									if(mobileEmailAddress != null){
										//发送邮件
										 EmailSenderInfo mailInfo = new EmailSenderInfo();
										 mailInfo.setMailServerHost(mailInfoMap.get("mailServerHost").toString());    
										 mailInfo.setMailServerPort(mailInfoMap.get("mailServerPort").toString());    
										 mailInfo.setValidate(Boolean.parseBoolean(mailInfoMap.get("validate").toString()));    
										 mailInfo.setUserName(mailInfoMap.get("userName").toString());    
										 mailInfo.setPassword(mailInfoMap.get("password").toString());//您的邮箱密码    
										 mailInfo.setFromAddress(mailInfoMap.get("fromAddress").toString());    
										 mailInfo.setContent(new Date().toString());   
										 //这个类主要来发送邮件   
										 EmailServiceImpl ems = new EmailServiceImpl();  
//										 System.out.println(mobileEmailAddress);
//										 mobileEmailAddress = "13580491179@139.com";
								    	 mailInfo.setToAddress(mobileEmailAddress);    
								    	 mailInfo.setSubject(content);    
										 mailInfo.setContent(content);    
//										 boolean sendHtmlMail = ems.sendTextMail(mailInfo);//发送html格式
									}
								}
							}
						}
					}
				}if(cmsInforelease.getIsEmail() != null && cmsInforelease.getIsEmail() > 0){
					if(cmsInforelease.getIsEmail() == 1){
						List<String> arrayList = new ArrayList<String>();
						Set<String> acIdSet = new HashSet<String>();
						getSaffAccount(releaseScopeType, releaseScopeList,
								releaseRole, acIdSet);
						if(acIdSet != null && acIdSet.size() > 0){
							OutLinkingService outLinkingService = new OutLinkingService();
							Map mailInfoMap = outLinkingService.getEmailSettingInfo();
							String content = "";
							if(cmsInfoitem.getContent() != null && !cmsInfoitem.getContent().equals("")){
								content = cmsInfoitem.getContent();
							}
							String title = "";
							if(cmsInfoitem.getTitle() != null && !cmsInfoitem.getTitle().equals("")){
								title = cmsInfoitem.getTitle();
							}
							//发送邮件
							for(String acId:acIdSet){
								//shortMessageService.sendShortMessage(phoneNumber, content);
								//发送邮件
								//ou.jh
								SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(acId);
//								Account accountByAccountId = this.providerOrganizationService.getAccountByAccountId(acId);
								if(sysOrgUserByAccount != null){
									String emailAddress = sysOrgUserByAccount.getEmail();
									if(emailAddress != null){
										//发送邮件
										 EmailSenderInfo mailInfo = new EmailSenderInfo();
										 mailInfo.setMailServerHost(mailInfoMap.get("mailServerHost").toString());    
										 mailInfo.setMailServerPort(mailInfoMap.get("mailServerPort").toString());    
										 mailInfo.setValidate(Boolean.parseBoolean(mailInfoMap.get("validate").toString()));    
										 mailInfo.setUserName(mailInfoMap.get("userName").toString());    
										 mailInfo.setPassword(mailInfoMap.get("password").toString());//您的邮箱密码    
										 mailInfo.setFromAddress(mailInfoMap.get("fromAddress").toString());    
										 mailInfo.setContent(new Date().toString());   
										 //这个类主要来发送邮件   
										 EmailServiceImpl ems = new EmailServiceImpl();  
//										 System.out.println(emailAddress);
//										 emailAddress = "ou.jh@iscreate.com";
								    	 mailInfo.setToAddress(emailAddress);    
								    	 mailInfo.setSubject(title);    
										 mailInfo.setContent(content);    
//										 boolean sendHtmlMail = ems.sendHtmlMail(mailInfo);//发送html格式
									}
								}
							}
						}
						
					}
				}
			}
		}
	}
	
	/**
	 * 按组织，按组织、角色查询人员
	* @date Nov 12, 20123:24:48 PM
	* @Description: TODO 
	* @param @param releaseScopeType
	* @param @param releaseScopeList
	* @param @param releaseRole
	* @param @param acIdSet        
	* @throws
	 */
	private void getSaffAccount(int releaseScopeType, String releaseScopeList,
			String releaseRole, Set<String> acIdSet) {
		if(releaseScopeType == 1){
			//根据部门
			String[] split = releaseScopeList.split("-");
			for(String s:split){
				//System.out.println(s);
				long parseLong = Long.parseLong(s);
				//ou.jh
				List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(parseLong);
//				List<Staff> allStaffListDownwardByOrg = this.providerOrganizationService.getStaffListByOrgIdService(parseLong);
				if(userByOrgId != null && userByOrgId.size() > 0){
					for(Map<String,Object> ac:userByOrgId){
						String account = "";
						if(ac.get("account") != null){
							account = ac.get("account")+"";
						}else{
							continue;
						}
						acIdSet.add(account);
						//System.out.println(ac.getAccountId());
//						acIdSet.add(ac.getAccount());
					}
				}
			}
		}else if(releaseScopeType == 3){
			//根据职位
			String[] splitScope = releaseScopeList.split("-");
			String[] splitRole = releaseRole.split("-");
			for(String sS:splitScope){
				for(String sR:splitRole){
					long parseLong = Long.parseLong(sS);
					//根据组织ID与职位
					//ou.jh
					List<Map<String,Object>> userByOrgIdAndRoleCode = this.sysOrgUserService.getUserByOrgIdAndRoleCode(parseLong, sR);
					//List<Staff> allStaffListDownwardByOrg = this.providerOrganizationService.getStaffByOrgIdAndRoleCodeService(parseLong,sR);
					if(userByOrgIdAndRoleCode != null && userByOrgIdAndRoleCode.size() > 0){
						for(Map<String,Object> ac:userByOrgIdAndRoleCode){
							//System.out.println(ac.getAccountId());
							if(ac.get("account") != null){
								acIdSet.add(ac.get("account")+"");
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 判断是否可通过
	* @date Nov 12, 20123:34:52 PM
	* @Description: TODO 
	* @param @param userId
	* @param @param infoReleaseId
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public int getIsApprover(String userId,long infoReleaseId,long infoItemId){
		int isApprover = 0;
		List<SysOrg> orgListDownwardByOrg = new ArrayList<SysOrg>();
		//List<ProviderOrganization> topLevelOrgByAccount = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			for(SysOrg biz: topLevelOrgByAccount){
				//yuan.yw
				orgListDownwardByOrg.addAll(this.sysOrganizationService.getOrgListDownwardByOrg(biz.getOrgId()));
			}
		}
		Map<String, Object> inforeleaseById = cmsManageDao.getInforeleaseById(infoReleaseId);
		if(inforeleaseById == null){
			inforeleaseById= this.cmsManageDao.getInfoItemById(infoItemId);
		}
		if(inforeleaseById.get("releaseScopeList") != null && !inforeleaseById.get("releaseScopeList").equals("")){
			String scopeList = inforeleaseById.get("releaseScopeList").toString();
			String[] split = scopeList.split("-");
			if(split != null && split.length > 0){
				int length = split.length;
				Set<Long> idSet = new HashSet<Long>();
				for(String s:split){
					long bizId = Long.parseLong(s);
					for(SysOrg b:orgListDownwardByOrg){
						if(bizId == b.getOrgId()){
							//System.out.println(bizId);
							idSet.add(bizId);
						}
					}
				}
				if(length == idSet.size()){
					isApprover = 0;
				}else{
					isApprover = 1;
				}
			}
		}
		return isApprover;
	}
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseById(long infoReleaseId){
		return this.cmsManageDao.getInforeleaseById(infoReleaseId);
	}
	
	/**
	 * 审核信息发布
	* @date Dec 4, 20124:42:54 PM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @param infoItemId
	* @param @param infoReleaseId
	* @param @param releaseOrApprove
	* @param @param userCnName
	* @param @param radioApprover
	* @param @param cmsInforelease
	* @param @param superiorsAccId
	* @param @param humanResourcesId
	* @param @param qualityId
	* @param @param companyLeadersId
	* @param @param releaseScopeList
	* @param @param releaseRole
	* @param @param uploadPic
	* @param @param uploadPicFileName
	* @param @param picName
	* @param @param picUrl
	* @param @param attaName
	* @param @param attaUrl
	* @param @param uploadAttachment
	* @param @param uploadAttachmentFileName
	* @param @param userId        
	* @throws
	 */
	public void approveInfoRelease(CmsInfoitem cmsInfoitem,long infoItemId,long infoReleaseId,int releaseOrApprove,String userCnName,String radioApprover,
			CmsInforelease cmsInforelease,String superiorsAccId,String humanResourcesId,String qualityId,String companyLeadersId,
			String releaseScopeList,String releaseRole,File[] uploadPic,String[] uploadPicFileName,String picName,String picUrl,String attaName ,String attaUrl
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId){
		cmsInfoitem.setId(infoItemId);
		//上传图片
		if(uploadPic!=null && uploadPic.length>0 && uploadPic[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadPic, uploadPicFileName);
			if(picUrl == null){
				picUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			picUrl = picUrl + url;
			
			if(picName == null){
				picName = "";
			}
			String picNameP = "";
			if(uploadPicFileName!=null && uploadPicFileName.length > 0){
				for(String path:uploadPicFileName){
					picNameP=picNameP+path+"#";
				}
			}
			picName = picName + picNameP;
			
//			entity.setValue("pictures", saveFilePaths.get(0).substring(saveFilePaths.get(0).lastIndexOf("upload"),saveFilePaths.get(0).length()));
		}
		cmsInfoitem.setPictures(picName);
		cmsInfoitem.setPicture_url(picUrl);
//		
//		entity.setValue("infoType", "normal");
		//上传附件
		if(uploadAttachment!=null && uploadAttachment.length>0 && uploadAttachment[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadAttachment, uploadAttachmentFileName);
			if(attaUrl == null){
				attaUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			attaUrl = attaUrl + url;
			
			String attachmentPath="";
			if(uploadAttachmentFileName!=null && uploadAttachmentFileName.length > 0){
				for(String path:uploadAttachmentFileName){
					attachmentPath=attachmentPath+path+"#";
				}
			}
			if(attaName == null){
				attaName = "";
			}
			attaName = attaName + attachmentPath;
			//entity.setValue("attachments", attachmentPath);
		}
		cmsInfoitem.setAttachments(attaName);
		cmsInfoitem.setUrl(attaUrl);
		//获取信息发布对象
		//更改发布历史
		String histRecord="";
		String updateStatus="";
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		if(1==releaseOrApprove){
			histRecord=userCnName+"，"+strCurrentTime+"，提交发布";
			updateStatus="21";
		}else if(3==releaseOrApprove){
			histRecord=userCnName+"，"+strCurrentTime+"，审批通过";
			updateStatus="22";
		}else if(4==releaseOrApprove){
			histRecord=userCnName+"，"+strCurrentTime+"，审批不通过";
			updateStatus="25";
		}//不为空，即要审批，要选择审批人
		//不为空，即要审批，要选择审批人
		if(radioApprover!=null && !"".equals(radioApprover)){
			String approver="";
			if(CmsConstant.CMS_DIREXTBOX.equals(radioApprover)){	//直接上司
				approver = superiorsAccId;
			}else if(CmsConstant.CMS_INFORELEASEAPPROVER.equals(radioApprover)){		//信息发布审批员
				//approver = this.infoReleaseStaffId;
			}else if(CmsConstant.CMS_HR.equals(radioApprover)){	//人事部
				approver = humanResourcesId;
			}else if(CmsConstant.CMS_QUALITYAPPROVER.equals(radioApprover)){//质量安全部
				approver = qualityId;
			}
			else if(CmsConstant.CMS_LEADER.equals(radioApprover)){	//公司领导
				approver = companyLeadersId;
			}
			cmsInforelease.setAuditor(approver);//设置审核人
			cmsInfoitem.setStatus(CmsConstant.CMS_WAIT4APPROVAL);
			
		}else{
			cmsInfoitem.setStatus(CmsConstant.CMS_RELEASEED);
		}
		if(cmsInfoitem.getStatus() == CmsConstant.CMS_RELEASEED){
			int releaseScopeType = cmsInforelease.getReleaseScopeType();
			sendBizMessageOrSmsOrEmail(cmsInforelease, releaseScopeType,
					releaseScopeList, releaseRole, userId, cmsInfoitem);
		}
		
		String releaseHist=cmsInforelease.getReleaseHistory();
		
		cmsInforelease.setReleaseHistory(releaseHist+" | "+histRecord);
		this.cmsManageDao.updateCmsInforelease(cmsInforelease);
		
		
		//获取普通信息对象
		//更改状态
		this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
//		
	}
	
	/**
	 * 添加短信消息
	* @date Nov 12, 20124:52:47 PM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @param infoItemId
	* @param @param releaseBounds
	* @param @param roleCode
	* @param @param releaseScopeList
	* @param @param releaseScopeStaffName
	* @param @param userId
	* @param @param userCnName
	* @param @param releaseOrApprove
	* @param @param radioApprover
	* @param @param superiorsAccId
	* @param @param infoReleaseStaffId
	* @param @param humanResourcesId
	* @param @param qualityId
	* @param @param companyLeadersId
	* @param @return        
	* @throws
	 */
	public String addsmsInfo(CmsInforelease cmsInforelease,CmsInfoitem cmsInfoitem,long infoItemId,String releaseBounds,String roleCode,String releaseScopeList,String releaseScopeStaffName,
			String userId,String userCnName,int releaseOrApprove,String radioApprover,String superiorsAccId,String infoReleaseStaffId,String humanResourcesId,
			String qualityId,String companyLeadersId){
		String returnString = "";
		String releaseRole = "";
		long updateCmsInfoitem = 0;
		long saveCmsInforelease = 0;
		//发布范围类型：1.部门  2.人员  3.岗位角色
		int releaseScopeType=1;
		if("radio_department".equals(releaseBounds)){
			releaseScopeType=1;
		}else if("radio_staff".equals(releaseBounds)){
			releaseScopeType=2;

		}else if("radio_role".equals(releaseBounds)){
			releaseScopeType=3;
			releaseRole=roleCode;
		}
		
		cmsInforelease.setInfoId(infoItemId);
		cmsInforelease.setReleaseScopeType(releaseScopeType);
		cmsInforelease.setReleaseScopeList(releaseScopeList);
		cmsInforelease.setReleaseRole(releaseRole);
		cmsInforelease.setReleaseScopeStaffName(releaseScopeStaffName);
		cmsInforelease.setReleaser(userId);
		cmsInforelease.setLastModifiedTime(new Date());
		
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		//判断是发布成功还是要等待审批
		if(1==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，提交发布");
		}else if(3==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，审批不通过");
		}else if(4==releaseOrApprove){
			cmsInforelease.setReleaseHistory(userCnName+"，"+strCurrentTime+"，审批通过");
		}
		
		//获取信息发布对应的普通的信息，设置其状态
		//BasicEntity be=this.cmsManageService.getInfoItemById(this.infoItemId);
		
		Map<String,String> updateMap=new HashMap<String,String>();
		
		//不为空，即要审批，要选择审批人
		if(radioApprover!=null && !"".equals(radioApprover)){
			String approver="";
			if(CmsConstant.CMS_DIREXTBOX.equals(radioApprover)){	//直接上司
				approver = superiorsAccId;
			}else if(CmsConstant.CMS_INFORELEASEAPPROVER.equals(radioApprover)){		//信息发布审批员
				//approver = this.infoReleaseStaffId;
			}else if(CmsConstant.CMS_HR.equals(radioApprover)){	//人事部
				approver = humanResourcesId;
			}else if(CmsConstant.CMS_QUALITYAPPROVER.equals(radioApprover)){//质量安全部
				approver = qualityId;
			}
			else if(CmsConstant.CMS_LEADER.equals(radioApprover)){	//公司领导
				approver = companyLeadersId;
			}
			cmsInforelease.setAuditor(approver);
			cmsInfoitem.setStatus(CmsConstant.CMS_WAIT4APPROVAL);
			
		}else{
			updateMap.put("status", String.valueOf(CmsConstant.CMS_RELEASEED));
			cmsInfoitem.setStatus(CmsConstant.CMS_RELEASEED);
		}
			
		updateCmsInfoitem = this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
		saveCmsInforelease = this.cmsManageDao.saveCmsInforelease(cmsInforelease);
		if(cmsInfoitem.getStatus() == CmsConstant.CMS_RELEASEED){
			
			sendBizMessageOrSmsOrEmail(cmsInforelease, releaseScopeType,
					releaseScopeList, releaseRole, userId, cmsInfoitem);
		}
		if(updateCmsInfoitem > 0 && saveCmsInforelease > 0){
			returnString =  "操作成功";
		}else{
			returnString =  "操作失败";
		}
		return returnString;
	}
	
	/**
	 * 保存普通信息
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @return        
	* @throws
	 */
	public Long saveCmsInfoitem(CmsInfoitem cmsInfoitem){
		return this.cmsManageDao.saveCmsInfoitem(cmsInfoitem);
	}
	
	/**
	 * 用户所在组织下面获取组织ID
	* @date Nov 13, 20123:47:34 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public String getUserOfOrganizationAndReleaseLimit(String userId){
		//List<ProviderOrganization> topLevelOrgByAccount = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		//获取组织
		String biId="-";
		if(topLevelOrgByAccount!=null && !topLevelOrgByAccount.isEmpty()){
			for(SysOrg bi:topLevelOrgByAccount){
				biId+=bi.getOrgId()+"-";
				//List<ProviderOrganization> nextLevelOrgListByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(bi.getOrgId());
				List<Map<String,Object>> nextLevelOrgListByOrg = sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(bi.getOrgId().toString());
				
				if(nextLevelOrgListByOrg != null && !nextLevelOrgListByOrg.isEmpty()){
					for(Map<String,Object> biz:nextLevelOrgListByOrg){
						if(!biz.get("orgId").equals(bi.getOrgId().toString()))
						   biId+=biz.get("orgId")+"-";
					}
				}
			}
			//biId=biId.substring(0,biId.lastIndexOf("-"));
		}
		return biId;
	}
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseByInfoItemId(long infoItemId){
		return this.cmsManageDao.getInforeleaseByInfoItemId(infoItemId);
	}
	
	/**
	 * 驳回
	* @date Dec 7, 201210:08:01 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public long rejectInfoItemByInfoItemId(long infoItemId){
		CmsInfoitem cmsInfoitem = this.cmsManageDao.getInfoItemPoJoById(infoItemId);
		cmsInfoitem.setStatus(CmsConstant.CMS_REJECT);
		long updateCmsInfoitem = this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
		return updateCmsInfoitem;
	}
	
	/**
	 * 保存项目考核数据
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal
	* @param @return        
	* @throws
	 */
	public long saveCmsReportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal){
		Long saveCmsReportProjectAppraisal = this.cmsManageDao.saveCmsReportProjectAppraisal(cmsReportProjectAppraisal);
		return saveCmsReportProjectAppraisal;
	}
	
	/**
	 * 批量保存项目考核数据
	* @date 2012-12-19上午09:24:35
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisals
	* @param @return        
	* @throws
	 */
	public List<Long> saveCmsReportProjectAppraisalS(List<CmsReportProjectAppraisal> cmsReportProjectAppraisals){
		List<Long> rList = new ArrayList<Long>();
		if(cmsReportProjectAppraisals != null && !cmsReportProjectAppraisals.isEmpty()){
			for(int i = 0;i < cmsReportProjectAppraisals.size();i++){
				if(cmsReportProjectAppraisals.get(i) != null){
					CmsReportProjectAppraisal cmsReportProjectAppraisal = cmsReportProjectAppraisals.get(i);
					CmsReportProjectAppraisal cmsr = new CmsReportProjectAppraisal();
					cmsr.setAprData(cmsReportProjectAppraisal.getAprData());
					cmsr.setAprData(cmsReportProjectAppraisal.getAugData());
					cmsr.setAverage(cmsReportProjectAppraisal.getAverage());
					cmsr.setCompany(cmsReportProjectAppraisal.getCompany());
					cmsr.setDecData(cmsReportProjectAppraisal.getDecData());
					cmsr.setFebData(cmsReportProjectAppraisal.getFebData());
					//System.out.println(cmsReportProjectAppraisal.getFebData()+"cmsReportProjectAppraisal.getFebData()");
					cmsr.setJanData(cmsReportProjectAppraisal.getJanData());
					cmsr.setJulData(cmsReportProjectAppraisal.getJulData());
					cmsr.setJunData(cmsReportProjectAppraisal.getJunData());
					cmsr.setMarData(cmsReportProjectAppraisal.getMarData());
					cmsr.setMayData(cmsReportProjectAppraisal.getMayData());
					cmsr.setNovData(cmsReportProjectAppraisal.getNovData());
					cmsr.setOctData(cmsReportProjectAppraisal.getOctData());
					cmsr.setRanking(cmsReportProjectAppraisal.getRanking());
					cmsr.setScore(cmsReportProjectAppraisal.getScore());
					cmsr.setSepData(cmsReportProjectAppraisal.getSepData());
					cmsr.setProjectName(cmsReportProjectAppraisal.getProjectName());
					cmsr.setYear(cmsReportProjectAppraisal.getYear());
					long saveCmsReportProjectAppraisal = saveCmsReportProjectAppraisal(cmsr);
					rList.add(saveCmsReportProjectAppraisal);
				}
			}
		}
		return rList;
	}
	
	
	/**
	 * 获取由我拟稿的草稿消息
	* @date Nov 19, 20124:32:28 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public long saveCmsInfoitemAppraisal(CmsInfoitemAppraisal cmsInfoitemAppraisal){
		return this.cmsManageDao.saveCmsInfoitemAppraisal(cmsInfoitemAppraisal);
	}
	
	
	/**
	 * 添加或更新普通信息对象(项目考核)
	* @date Dec 4, 201210:53:05 AM
	* @Description: TODO 
	* @param @param addorUpdate
	* @param @param cmsInfoitem
	* @param @param uploadPic
	* @param @param uploadPicFileName
	* @param @param picName
	* @param @param picUrl
	* @param @param attaName
	* @param @param attaUrl
	* @param @param uploadAttachment
	* @param @param uploadAttachmentFileName
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public long saveOrUpdateCmsInfoItemReport(List<CmsReportProjectAppraisal> cmsReportProjectAppraisals,String addorUpdate,CmsInfoitem cmsInfoitem,File[] uploadPic,String[] uploadPicFileName,String picName,String picUrl,String attaName ,String attaUrl
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId){
		if(cmsReportProjectAppraisals != null && !cmsReportProjectAppraisals.isEmpty()){
			for(int i = 0;i < cmsReportProjectAppraisals.size();i++){
				if(cmsReportProjectAppraisals.get(i) != null){
					CmsReportProjectAppraisal cmsReportProjectAppraisal = cmsReportProjectAppraisals.get(i);
					String projectName = cmsReportProjectAppraisal.getProjectName();
					String year = cmsReportProjectAppraisal.getYear();
					List<CmsReportProjectAppraisal> cmsPeportProjectAppraisal = this.cmsManageDao.getCmsPeportProjectAppraisalHIB(projectName, year);
					if(cmsPeportProjectAppraisal != null && !cmsPeportProjectAppraisal.isEmpty()){
						this.cmsManageDao.deleteCmsPeportProjectAppraisals(cmsPeportProjectAppraisal);
					}
				}
			}
		}
		List<Long> saveCmsReportProjectAppraisalS = saveCmsReportProjectAppraisalS(cmsReportProjectAppraisals);
		
		//上传图片
		if(uploadPic!=null && uploadPic.length>0 && uploadPic[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadPic, uploadPicFileName);
			if(picUrl == null){
				picUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			picUrl = picUrl + url;
			
			if(picName == null){
				picName = "";
			}
			String picNameP = "";
			if(uploadPicFileName!=null && uploadPicFileName.length > 0){
				for(String path:uploadPicFileName){
					picNameP=picNameP+path+"#";
				}
			}
			picName = picName + picNameP;
			
//			entity.setValue("pictures", saveFilePaths.get(0).substring(saveFilePaths.get(0).lastIndexOf("upload"),saveFilePaths.get(0).length()));
		}
		cmsInfoitem.setPictures(picName);
		cmsInfoitem.setPicture_url(picUrl);
//		
//		entity.setValue("infoType", "normal");
		//上传附件
		if(uploadAttachment!=null && uploadAttachment.length>0 && uploadAttachment[0] != null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cms";
			List<String> saveFilePaths = FileHelper.saveFiles(savePath, uploadAttachment, uploadAttachmentFileName);
			if(attaUrl == null){
				attaUrl = "";
			}
			String url = "";
			//获取上传路径
			if(saveFilePaths!=null && !saveFilePaths.isEmpty()){
				for(String path:saveFilePaths){
					path=path.substring(path.lastIndexOf("upload"),path.length());
					url=url+path+"#";
				}
			}
			attaUrl = attaUrl + url;
			
			String attachmentPath="";
			if(uploadAttachmentFileName!=null && uploadAttachmentFileName.length > 0){
				for(String path:uploadAttachmentFileName){
					attachmentPath=attachmentPath+path+"#";
				}
			}
			if(attaName == null){
				attaName = "";
			}
			attaName = attaName + attachmentPath;
			//entity.setValue("attachments", attachmentPath);
		}
		cmsInfoitem.setAttachments(attaName);
		cmsInfoitem.setUrl(attaUrl);
		cmsInfoitem.setInfoType("report");
		long rId = 0;
		if(addorUpdate != null){	
			if(addorUpdate.equals("add")){
				cmsInfoitem.setAuthor(userId);//设置拟稿人
				cmsInfoitem.setStatus(CmsConstant.CMS_DRAFT);	//设为初稿状态
				cmsInfoitem.setDrafttime(new Date());//设置拟稿时间
				rId = this.cmsManageDao.saveCmsInfoitem(cmsInfoitem);
			}else if(addorUpdate.equals("update")){
				rId = cmsInfoitem.getId();
				this.cmsManageDao.updateCmsInfoitem(cmsInfoitem);
			}
		}
		return rId;
		
	}
	
	/**
	 * 根据项目名称与年份查询项目考核
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsPeportProjectAppraisal(String projectName,String year){
		List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageDao.getCmsPeportProjectAppraisal(projectName, year);
		if(cmsPeportProjectAppraisal != null && !cmsPeportProjectAppraisal.isEmpty()){
			for(Map<String, Object> map:cmsPeportProjectAppraisal){
				if(map != null){
					for(String s:map.keySet()){
						if(map.get(s) == null){
							map.put(s, "");
						}
					}
				}
			}
		}
		return cmsPeportProjectAppraisal;
	}
	
	
	/**
	 * 根据项目名称与年份查询项目考核(报表)
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsPeportProjectAppraisalChar(String projectName,String year){
		List<Map<String,Object>> cmsPeportProjectAppraisal = this.cmsManageDao.getCmsPeportProjectAppraisal(projectName, year);
		if(cmsPeportProjectAppraisal != null && !cmsPeportProjectAppraisal.isEmpty()){
			for(Map<String, Object> map:cmsPeportProjectAppraisal){
				if(map != null){
					for(String s:map.keySet()){
						if(map.get(s) == null || "".equals(map.get(s))){
							map.put(s, "null");
						}
					}
				}
			}
		}
		return cmsPeportProjectAppraisal;
	}
	
	/**
	 * 删除
	* @date 2012-12-19下午02:53:16
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal){
		this.cmsManageDao.deleteCmsPeportProjectAppraisal(cmsReportProjectAppraisal);
	}
	
	
	public DataDictionaryService getDataDictionaryService() {
		return dataDictionaryService;
	}
	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}
	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}
	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}
}
