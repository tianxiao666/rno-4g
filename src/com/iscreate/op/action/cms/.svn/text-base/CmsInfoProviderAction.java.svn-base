package com.iscreate.op.action.cms;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.cms.CmsInfoProviderService;
import com.iscreate.op.service.cms.CmsManageService;

import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.sso.session.UserInfo;
import com.opensymphony.xwork2.ActionContext;

public class CmsInfoProviderAction {
	// ---注入---//
	private CmsInfoProviderService cmsInfoProviderService;


	// ---变量---//
	private List<Map<String, Object>> announcements;
	private Map<String, Object> announcement;
	private int totalCount = 0;// 总记录数
	private int pageIndex = 0;// 页面索引
	private int pageSize = 5;// 每页记录数
	private int totalPageCount;// 总页数
	private boolean timeAsc = false;// 按时间倒叙
	private long infoId;// 公告id
	private String errorInfo = "";// 出错信息
	
	
	private SysOrgUserService sysOrgUserService;
	
	
	

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	/**
	 * 获取用户可见的有效的公告总数量
	 */
	public void getAllValidAnnouncementForUserCount() {
		String userId = getUserId();
		int count = cmsInfoProviderService
				.getAllValidAnnouncementsCount(userId);

		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("utf-8");
			Writer writer = response.getWriter();
			writer.write(count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取top 5的公告
	 * 
	 * @return Jul 4, 2012 2:59:46 PM gmh
	 */
	public String getTopAnnouncementAction() {
		String result = "success";

		String userId = getUserId();

		// 公告总数
		//totalCount = cmsInfoProviderService
		//		.getAllValidAnnouncementsCount(userId);
		// 计算总页数
		totalPageCount = totalCount / pageSize;
		if (totalCount > totalPageCount * pageSize) {
			totalPageCount++;
		}
		// 公告内容
		announcements = cmsInfoProviderService.getTopNValidAnnouncements(
				userId, pageSize, timeAsc);

		return result;
	}

	/**
	 * 获取某个范围内的指定条数的公告
	 * 
	 * @return Jul 4, 2012 3:58:55 PM gmh
	 */
	public String getRangeAnnouncementAction() {
		String result = "success";

		// 计算记录起点
		int start = pageIndex * pageSize;
		String userId = getUserId();

		// System.out.println("pageIndex= " + pageIndex + ",pageSize = "
		// + pageSize + ",start = " + start + ",totalCount = "
		// + totalCount);

		announcements = cmsInfoProviderService
				.getRanageValidAnnouncements(userId, start, pageSize, timeAsc);
		return result;
	}

	/**
	 * 获取某条公告的详细信息
	 * 
	 * @return Jul 4, 2012 4:08:39 PM gmh
	 */
	public String getAnnouncementDetailAction() {

		String userId = this.getUserId();


		String result = "success";
		announcement = cmsInfoProviderService
				.getAnnouncementDetailById(infoId,userId);
		if(announcement != null && !announcement.isEmpty()){
			if (announcement.get("auditor") == null) {
				announcement.put("auditor", "无");
			}
			announcement
			.put("importancelevel",
					(String) announcement
					.get("importancelevel"));
			
			// 获取用户中文名
			announcement.put("auditor", getStaffUserName((String) announcement
					.get("auditor")));
			announcement.put("releaser", getStaffUserName((String) announcement
					.get("releaser")));
			announcement.put("author", getStaffUserName((String) announcement
					.get("author")));
			if (announcement.get("content") == null) {
				announcement.put("content", "");
			}
			//拼装图片的路径
			String pic="";
			if (announcement.get("pictures") != null) {
				pic = (String) announcement.get("pictures");
				pic=pic.trim();
			} 
			announcement.put("pictures", pic);
		}else{
			result = "fail";
		}
		
		// System.out.println(announcement);
		return result;
	}

	/**
	 * 获取用户id
	 * 
	 * @return Jul 4, 2012 3:24:08 PM gmh
	 */
	private String getUserId() {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> requestMap = (Map<String, Object>) ctx
				.get("request");
		String userId = (String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERID);

		return userId;
	}



	/**
	 * 获取用户中文名
	 * 
	 * @param userId
	 * @return Jul 12, 2012 2:26:03 PM gmh
	 */
	private String getStaffUserName(String userId) {
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//		Staff staffByAccount = this.providerOrganizationService.getStaffByAccount(userId);
		if (sysOrgUserByAccount != null) {
			if (sysOrgUserByAccount.getName() != null) {
				return sysOrgUserByAccount.getName();
			}
		}

		return userId;
	}
	
	

	public CmsInfoProviderService getCmsInfoProviderService() {
		return cmsInfoProviderService;
	}

	public void setCmsInfoProviderService(
			CmsInfoProviderService cmsInfoProviderService) {
		this.cmsInfoProviderService = cmsInfoProviderService;
	}



	public List<Map<String, Object>> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(List<Map<String, Object>> announcements) {
		this.announcements = announcements;
	}

	public Map<String, Object> getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Map<String, Object> announcement) {
		this.announcement = announcement;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public boolean isTimeAsc() {
		return timeAsc;
	}

	public void setTimeAsc(boolean timeAsc) {
		this.timeAsc = timeAsc;
	}

	public long getInfoId() {
		return infoId;
	}

	public void setInfoId(long infoId) {
		this.infoId = infoId;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}


}
