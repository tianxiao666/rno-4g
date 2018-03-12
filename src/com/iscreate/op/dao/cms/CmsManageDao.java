package com.iscreate.op.dao.cms;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cms.CmsInfoitem;
import com.iscreate.op.pojo.cms.CmsInfoitemAppraisal;
import com.iscreate.op.pojo.cms.CmsInforelease;
import com.iscreate.op.pojo.cms.CmsReportProjectAppraisal;

public interface CmsManageDao {
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
	 * 保存公告信息
	* @date Nov 12, 20129:52:00 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public Long saveCmsInforelease(CmsInforelease cmsInforelease);
	
	
	/**
	 * 删除公告信息
	* @date Nov 12, 201210:11:15 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInforelease(CmsInforelease cmsInforelease);
	
	/**
	 * 查询方法
	* @date Nov 12, 201210:18:26 AM
	* @Description: TODO 
	* @param @param sqlString
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getReportComment(final String sqlString);
	
	
	/**
	 * 获取普通信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInfoItemById(long infoItemId);
	
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseByInfoItemId(long infoItemId);
	
	/**
	 * 修改普通信息
	* @date Nov 12, 201211:45:02 AM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @return        
	* @throws
	 */
	public long updateCmsInfoitem(CmsInfoitem cmsInfoitem);
	
	/**
	 * 获取普通信息项对象
	 * @param infoItemId
	 * @return
	 */
	public CmsInfoitem getInfoItemPoJoById(long infoItemId);
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseById(long infoReleaseId);
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public CmsInforelease getInforeleasePoJoById(long infoReleaseId);
	
	/**
	 * 修改公告信息
	* @date Nov 12, 201211:45:02 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public long updateCmsInforelease(CmsInforelease cmsInforelease);
	
	
	/**
	 * 构建对应公告信息tab的SqlContainer对象
	 * @param context
	 * @param tabType
	 * @return
	 */
	public String createSqlContainer(String tabType,String userId);
	
	/**
	 * 获取由我拟稿的草稿消息
	* @date Nov 19, 20124:32:28 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getDraftInfoItem(String userId);
	
	
	/**
	 * 保存项目考核数据
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal
	* @param @return        
	* @throws
	 */
	public Long saveCmsReportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal);
	
	/**
	 * 保存项目考核数据(关联表)
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsInfoitemAppraisal
	* @param @return        
	* @throws
	 */
	public Long saveCmsInfoitemAppraisal(CmsInfoitemAppraisal cmsInfoitemAppraisal);
	
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
	* @date 2012-12-19下午02:51:59
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal);
	
	/**
	 * 删除
	* @date 2012-12-19下午02:51:59
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisals(List<CmsReportProjectAppraisal> cmsReportProjectAppraisal);
	
	/**
	 * 根据项目名称与年份查询项目考核
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<CmsReportProjectAppraisal> getCmsPeportProjectAppraisalHIB(final String projectName,final String year);
	
	/**
	 * 删除公告信息
	* @date Nov 12, 201210:11:15 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInfoItem(CmsInfoitem cmsInfoitem);
}
