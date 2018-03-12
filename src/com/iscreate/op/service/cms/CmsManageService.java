package com.iscreate.op.service.cms;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cms.CmsInfoitem;
import com.iscreate.op.pojo.cms.CmsInfoitemAppraisal;
import com.iscreate.op.pojo.cms.CmsInforelease;
import com.iscreate.op.pojo.cms.CmsReportProjectAppraisal;

public interface CmsManageService {
	/**
	 * 批量删除公告信息
	* @date Nov 12, 201210:14:41 AM
	* @Description: TODO 
	* @param @param ids
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInforelease(String[] ids);
	
	
	/**
	 * 根据userId和tab类型查询公告发布消息
	 * @param userId
	 * @param tabType
	 * @return
	 */
	public List<Map<String, Object>> getInfoReleaseListByTabType(String userId, String tabType);
	
	
	/**
	 * 根据用户id和tab类型，获取对应的公告发布信息数量
	 * @param userId
	 * @return
	 */
	public Map<String,String> getCmsReleaseCountByTabType(String userId);
	
	/**
	 * 获取上级领导
	* @date Nov 12, 201211:05:54 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSuperiorsList(String userId);
	
	
	/**
	 * 获取所有角色
	* @date Nov 12, 201211:08:03 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getAllRoleType();
	
	/**
	 * 获取Cms信息类型列表
	 * @return
	 */
	public List<Map<String, Object>> getCmsCategoryList();
	
	/**
	 * 获取发布的栏目列表
	* @date Nov 12, 201211:22:10 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPortalItemList();
	
	/**
	 * 获取重要级别
	* @date Nov 12, 201211:33:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsImportantLevelList();
	
	/**
	 * 质量安全部
	* @date Nov 12, 201211:26:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getQualitySuperiorsList();
	

	/**
	 * 人力资源部
	* @date Nov 12, 201211:26:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getHumanResourcesSuperiorsList();
	
	/**
	 * 获取公司领导
	* @date Nov 12, 201211:30:18 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCompanyLeadersList();
	
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
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId);
	
	/**
	 * 紧急程度
	* @date Nov 12, 201211:33:45 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsUrgencyLevelList();
	
	/**
	 * 根据ID获取普通信息
	* @date Nov 12, 201211:55:25 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getInfoItemById(long infoItemId);
	
	/**
	 * 根据ID获取普通信息
	* @date Nov 12, 201211:55:25 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public CmsInfoitem getInfoItemPoJoById(long infoItemId);
	
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
			String userId);
	
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
	public int getIsApprover(String userId,long infoReleaseId,long infoItemId);
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseById(long infoReleaseId);
	
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
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId);
	
	/**
	 * 保存普通信息
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @return        
	* @throws
	 */
	public Long saveCmsInfoitem(CmsInfoitem cmsInfoitem);
	
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
			String qualityId,String companyLeadersId);
	
	/**
	 * 用户所在组织下面获取组织ID
	* @date Nov 13, 20123:47:34 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public String getUserOfOrganizationAndReleaseLimit(String userId);
	
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseByInfoItemId(long infoItemId);
	
	/**
	 * 驳回
	* @date Dec 7, 201210:08:01 AM
	* @Description: TODO 
	* @param @param infoItemId
	* @param @return        
	* @throws
	 */
	public long rejectInfoItemByInfoItemId(long infoItemId);
	
	
	/**
	 * 保存项目考核数据
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal
	* @param @return        
	* @throws
	 */
	public long saveCmsReportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal);
	
	
	/**
	 * 批量保存项目考核数据
	* @date 2012-12-19上午09:24:35
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisals
	* @param @return        
	* @throws
	 */
	public List<Long> saveCmsReportProjectAppraisalS(List<CmsReportProjectAppraisal> cmsReportProjectAppraisals);
	
	/**
	 * 获取由我拟稿的草稿消息
	* @date Nov 19, 20124:32:28 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public long saveCmsInfoitemAppraisal(CmsInfoitemAppraisal cmsInfoitemAppraisal);
	
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
			,File[] uploadAttachment,String[] uploadAttachmentFileName,String userId);
	
	/**
	 * 根据项目名称与年份查询项目考核
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsPeportProjectAppraisal(String projectName,String year);
	
	/**
	 * 删除
	* @date 2012-12-19下午02:53:16
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal);
	
	/**
	 * 根据项目名称与年份查询项目考核(报表)
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsPeportProjectAppraisalChar(String projectName,String year);
	
	/**
	 * 批量删除公告信息
	* @date Nov 12, 201210:14:41 AM
	* @Description: TODO 
	* @param @param ids
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInfoItem(String[] ids);
}

