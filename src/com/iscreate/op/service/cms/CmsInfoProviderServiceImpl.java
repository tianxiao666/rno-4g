package com.iscreate.op.service.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.cms.CmsInfoProviderDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class CmsInfoProviderServiceImpl implements CmsInfoProviderService {
	private CmsInfoProviderDao cmsInfoProviderDao;
	private SysOrgUserService sysOrgUserService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

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

	public CmsInfoProviderDao getCmsInfoProviderDao() {
		return cmsInfoProviderDao;
	}

	public void setCmsInfoProviderDao(CmsInfoProviderDao cmsInfoProviderDao) {
		this.cmsInfoProviderDao = cmsInfoProviderDao;
	}

	

	/**
	 * 获取用户能看的所有的时间有效的公告的数量
	* @date Nov 13, 20129:42:38 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public int getAllValidAnnouncementsCount(String userId){
		int count = 0;
		String sqlIds = getInfoReleaseOfUserCanSee(userId);
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> allValidAnnouncementsCount = cmsInfoProviderDao.getAllValidAnnouncementsCount(sqlIds, strCurrentTime, strCurrentTime);
		if(allValidAnnouncementsCount != null && allValidAnnouncementsCount.size() > 0){
			count = allValidAnnouncementsCount.size();
		}
		return count;
	}
	
	/**
	 * 获取公告指定分页参数的记录
	* @date Nov 13, 20129:50:04 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRanageValidAnnouncements(String userId,
			int start, int length, boolean timeAsc){
		String sqlIds = getInfoReleaseOfUserCanSee(userId);
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		//List<Map<String,Object>> allValidAnnouncementsCount = cmsInfoProviderDao.getAllValidAnnouncementsCountPaging(sqlIds, strCurrentTime, strCurrentTime, start, length, timeAsc);
		List<Map<String,Object>> allValidAnnouncementsCount = cmsInfoProviderDao.getAllValidAnnouncementsCount(sqlIds, strCurrentTime, strCurrentTime, timeAsc);
		return allValidAnnouncementsCount;
	}
	
	
	/**
	 * 获取前面N条记录（最新发布的在前）
	* @date Nov 13, 20129:50:51 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param length
	* @param @param isAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getTopNValidAnnouncements(String userId,
			int length, boolean isAsc) {
		// TODO Auto-generated method stub
		return getRanageValidAnnouncements(userId, 0, length, isAsc);
	}
	
	/**
	 * 获取用户所能看见的公告发布信息
	* @date Nov 13, 20129:19:40 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	private String getInfoReleaseOfUserCanSee(String userId){
		try {
			StringBuffer sqlIds=new StringBuffer("");
			
			//按部门查找
			List<String> releaseBumenList=new ArrayList<String>();
			
			
			//获取发布范围类型是‘部门’的公告信息
			List<Map<String, Object>> entityList = this.cmsInfoProviderDao.getInforeleaseByReleaseScopeType(1);
			//获取当前登录人所在组织
			//List<ProviderOrganization> orgList = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> orgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			
			if(orgList!=null && !orgList.isEmpty() && entityList!=null && !entityList.isEmpty()){
				//循环判断用户所在部门是否在公告的发发布范围内
				for(SysOrg org:orgList){
					String str_orgunitId=String.valueOf(org.getOrgId());
							for(Map<String, Object> map:entityList){
								String infoId=map.get("infoId")==null?"":map.get("infoId").toString();
								
								//获取具体要对部门发布公告信息的范围列表
								String str_releaseScopeList=map.get("releaseScopeList")==null?"":map.get("releaseScopeList").toString();
								String[] releaseScopeList=str_releaseScopeList.split("-");
//								//判断当前人的组织是否在发布的部门范围内
								if(releaseScopeList!=null && releaseScopeList.length>0){
									for(String tempReleaseScope:releaseScopeList){
										if(str_orgunitId.equals(tempReleaseScope)){
											releaseBumenList.add(infoId);
											break;
										}
									}
								}
							}
				}
			}
			
			//按具体人员查找
			List<String> releaseStaffList=new ArrayList<String>();
			//获取发布范围类型是‘人员’的公告信息
			entityList = this.cmsInfoProviderDao.getInforeleaseByReleaseScopeType(2);
			if(entityList!=null && !entityList.isEmpty()){
				for(Map<String, Object> map:entityList){
					String infoId=map.get("infoId")==null?"":map.get("infoId").toString();
					//获取具体要对人员发布公告信息的范围列表
					String str_releaseScopeList=map.get("releaseScopeList")==null?"":map.get("releaseScopeList").toString();
					String[] releaseScopeList=str_releaseScopeList.split("-");
					//判断当前人的组织是否在发布的部门范围内
					if(releaseScopeList!=null && releaseScopeList.length>0){
						for(String tempReleaseScope:releaseScopeList){
							if(userId.equals(tempReleaseScope)){
								releaseStaffList.add(infoId);
								break;
							}
						}
					}
				}
			}
			
			//按职位查找
			List<String> releaseRoleList=new ArrayList<String>();
			//获取发布范围类型是‘职位’的公告信息
			entityList = this.cmsInfoProviderDao.getInforeleaseByReleaseScopeType(3);
			if(orgList!=null && !orgList.isEmpty() && entityList!=null && !entityList.isEmpty()){
				//循环判断用户所在部门是否在公告的发发布范围内
				for(SysOrg org:orgList){
					long id = org.getOrgId();
					String bizIdString = String.valueOf(id);
					//获取当前登录人所在组织
					//ou.jh
					List<SysRole> userRolesByAccount = this.sysOrgUserService.getUserRolesByAccount(userId);
					//List<Role> roleByOrgIdAndAccountService = this.providerOrganizationService.getRoleByOrgIdAndAccountService(id, userId);
					if(userRolesByAccount != null && userRolesByAccount.size() > 0){
						for(SysRole r:userRolesByAccount){
							String role = "";
							if(r != null){
								role = r.getCode();
							}
									if(entityList!=null && !entityList.isEmpty()){
										for(Map<String, Object> map:entityList){
											String infoId=map.get("infoId")==null?"":map.get("infoId").toString();
											//获取具体要对职位发布公告信息的范围列表
											String str_releaseScopeList=map.get("releaseScopeList")==null?"":map.get("releaseScopeList").toString();
											String[] releaseScopeList=str_releaseScopeList.split("-");
											//判断当前人的组织是否在发布的部门范围内
											if(releaseScopeList!=null && releaseScopeList.length>0){
												for(String tempReleaseScope:releaseScopeList){
													if(bizIdString.equals(tempReleaseScope)){
														String str_releaseRole=map.get("releaseRole")==null?"":map.get("releaseRole").toString();
														String[] releaseRole=str_releaseRole.split("-");
														if(releaseRole!=null && releaseRole.length>0){
															for(String release:releaseRole){
																if(!role.equals("") && role.equals(release)){
																	releaseRoleList.add(infoId);
																	break;
																}
															}
														}
													}
												}
											}
									}
							}
						}
					}
				}
			}
			
			
			
			//获取2个集合共有的数据
			releaseBumenList.removeAll(releaseStaffList);
			releaseBumenList.addAll(releaseStaffList);
			releaseBumenList.removeAll(releaseRoleList);
			releaseBumenList.addAll(releaseRoleList);
			if(releaseBumenList!=null && !releaseBumenList.isEmpty()){
				for(String infoId:releaseBumenList){
					sqlIds.append(infoId+",");
				}
				sqlIds.deleteCharAt(sqlIds.length()-1);
				return sqlIds.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	/**
	 * 获取公告的详情
	* @date Nov 13, 20129:59:43 AM
	* @Description: TODO 
	* @param @param infoid
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getAnnouncementDetailById(long infoid,String userId){
		String sqlIds = getInfoReleaseOfUserCanSee(userId);
		String[] split = null;
		if(sqlIds != null && !sqlIds.equals("")){
			split = sqlIds.split(",");
			boolean in = isIn(infoid+"", split);
			if(in == false){
				return null;
			}
		}
		List<Map<String,Object>> announcementDetailById = this.cmsInfoProviderDao.getAnnouncementDetailById(infoid);
		Map<String, Object> result = new HashMap<String, Object>();
		if (announcementDetailById != null) {
			for (Map<String,Object> map : announcementDetailById) {
				Map<String,Object> tmap=map;
//				System.out.println(tmap);
				Date t = null;
				try {
//					System.out.println("格式化前 ： "+(String)tmap.get("lastmodifiedtime"));
					if(tmap.get("lastModifiedTime") != null && !tmap.get("lastModifiedTime").equals("")){
						t = TimeFormatHelper.setTimeFormat(tmap.get("lastModifiedTime").toString());
					}
					String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(t, "yyyy-MM-dd HH:mm:ss");
//					System.out.println("格式化后 :" +sdf.format(t));
					tmap.put("lastModifiedTime", strCurrentTime);
//					System.out.println(tmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				result.putAll(tmap);
				break;
			}
		}

		return result;
	}
	
	
	/**
	 * 获取项目考核的记录
	* @date Dec 18, 201211:42:57 AM
	* @Description: TODO 
	* @param @param userId
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @param releaseScopeType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getCmsReportProjectAppraisals(String userId,
			int start, int length, boolean timeAsc,int releaseScopeType){
		String sqlIds = getInfoReleaseOfUserCanSee(userId);
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		//List<Map<String,Object>> allValidAnnouncementsCount = cmsInfoProviderDao.getAllValidAnnouncementsCountPaging(sqlIds, strCurrentTime, strCurrentTime, start, length, timeAsc);
		List<Map<String,Object>> cmsReportProjectAppraisal = cmsInfoProviderDao.getCmsReportProjectAppraisal(sqlIds, strCurrentTime, strCurrentTime, releaseScopeType, timeAsc);
		return cmsReportProjectAppraisal;
	}
	
	
	private static boolean isIn(String substring, String[] source) {
		if (source == null || source.length == 0) {
		return false;
		}
		for (int i = 0; i < source.length; i++) {
		String aSource = source[i];
		if (aSource.equals(substring)) {
		return true;
		}
		}
		return false;
		}

}
