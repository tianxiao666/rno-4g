package com.iscreate.op.dao.cms;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.cms.CmsInfoitem;
import com.iscreate.op.pojo.cms.CmsInfoitemAppraisal;
import com.iscreate.op.pojo.cms.CmsInforelease;
import com.iscreate.op.pojo.cms.CmsReportProjectAppraisal;
import com.iscreate.plat.tools.TimeFormatHelper;

public class CmsManageDaoImpl implements CmsManageDao {
	private HibernateTemplate hibernateTemplate;
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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
		Serializable save = hibernateTemplate.save(cmsInfoitem);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	
	/**
	 * 保存项目考核数据
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal
	* @param @return        
	* @throws
	 */
	public Long saveCmsReportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal){
		Serializable save = hibernateTemplate.save(cmsReportProjectAppraisal);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	/**
	 * 保存项目考核数据(关联表)
	* @date Nov 12, 20129:52:12 AM
	* @Description: TODO 
	* @param @param cmsInfoitemAppraisal
	* @param @return        
	* @throws
	 */
	public Long saveCmsInfoitemAppraisal(CmsInfoitemAppraisal cmsInfoitemAppraisal){
		Serializable save = hibernateTemplate.save(cmsInfoitemAppraisal);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	
	/**
	 * 修改普通信息
	* @date Nov 12, 201211:45:02 AM
	* @Description: TODO 
	* @param @param cmsInfoitem
	* @param @return        
	* @throws
	 */
	public long updateCmsInfoitem(CmsInfoitem cmsInfoitem){
		long rLong = 1;
		try {
			hibernateTemplate.update(cmsInfoitem);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rLong = 0;
		}
		
		return rLong;
	}
	
	/**
	 * 保存公告信息
	* @date Nov 12, 20129:52:00 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public Long saveCmsInforelease(CmsInforelease cmsInforelease){
		Serializable save = hibernateTemplate.save(cmsInforelease);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	
	/**
	 * 删除公告信息
	* @date Nov 12, 201210:11:15 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInforelease(CmsInforelease cmsInforelease){
		boolean flag = true;
		try {
			hibernateTemplate.delete(cmsInforelease);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 删除公告信息
	* @date Nov 12, 201210:11:15 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public boolean deleteCmsInfoItem(CmsInfoitem cmsInfoitem){
		boolean flag = true;
		try {
			hibernateTemplate.delete(cmsInfoitem);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	
	/**
	 * 查询方法
	* @date Nov 12, 201210:18:26 AM
	* @Description: TODO 
	* @param @param sqlString
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getReportComment(final String sqlString){
			List<Map<String, Object>> list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
		return list;
	}
	

	
	
	/**
	 * 获取普通信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInfoItemById(long infoItemId){
		Map<String, Object> rMap = new HashMap<String, Object>();
		String sql = "select " +
				"c.id \"id\"," +
				"c.category \"category\"," +
				"c.importancelevel \"importancelevel\"," +
				"c.title \"title\"," +
				"c.label \"label\"," +
				"to_char(c.content) \"content\"," +
				"c.pictures \"pictures\"," +
				"c.picture_url \"picture_url\"," +
				"c.attachments \"attachments\"," +
				"c.url \"url\"," +
				"c.author \"author\"," +
				"c.drafttime \"drafttime\"," +
				"c.status \"status\"," +
				"c.infoType \"infoType\"" +
				" from cms_infoitem c where c.id = "+infoItemId;
		List<Map<String,Object>> reportComment = getReportComment(sql);
		if(reportComment != null && !reportComment.isEmpty() && reportComment.size() > 0){
			rMap = reportComment.get(0);
		}
		return rMap;
	}
	
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseByInfoItemId(long infoItemId){
		Map<String, Object> rMap = new HashMap<String, Object>();
		String sql = "select " +
				"c.id \"id\"," +
				"c.infoId \"infoId\"," +
				"c.timeSensibility \"timeSensibility\"," +
				"c.releaseScopeType \"releaseScopeType\"," +
				"c.releaseRole \"releaseRole\"," +
				"c.releaseScopeList \"releaseScopeList\"," +
				"c.releaseDestination \"releaseDestination\"," +
				"c.releaser \"releaser\"," +
				"c.auditor \"auditor\"," +
				"c.releasePeriodStart \"releasePeriodStart\"," +
				"c.releasePeriodEnd \"releasePeriodEnd\"," +
				"c.releasenotes \"releasenotes\"," +
				"c.releaseHistory \"releaseHistory\"," +
				"c.lastModifiedTime \"lastModifiedTime\"," +
				"c.isSentToMsgbox \"isSentToMsgbox\"," +
				"c.isInformedWithsms \"isInformedWithsms\"," +
				"c.releaseScopeStaffName \"releaseScopeStaffName\"," +
				"c.isEmail \"isEmail\"" +
				" from cms_inforelease c where c.infoId = "+infoItemId;
		List<Map<String,Object>> reportComment = getReportComment(sql);
		if(reportComment != null && !reportComment.isEmpty() && reportComment.size() > 0){
			rMap = reportComment.get(0);
		}
		return rMap;
	}
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public Map<String, Object> getInforeleaseById(long infoReleaseId){
		Map<String, Object> rMap = new HashMap<String, Object>();
		String sql = "select " +
				"c.id \"id\"," +
				"c.infoId \"infoId\"," +
				"c.timeSensibility \"timeSensibility\"," +
				"c.releaseScopeType \"releaseScopeType\"," +
				"c.releaseRole \"releaseRole\"," +
				"c.releaseScopeList \"releaseScopeList\"," +
				"c.releaseDestination \"releaseDestination\"," +
				"c.releaser \"releaser\"," +
				"c.auditor \"auditor\"," +
				"c.releasePeriodStart \"releasePeriodStart\"," +
				"c.releasePeriodEnd \"releasePeriodEnd\"," +
				"c.releasenotes \"releasenotes\"," +
				"c.releaseHistory \"releaseHistory\"," +
				"c.lastModifiedTime \"lastModifiedTime\"," +
				"c.isSentToMsgbox \"isSentToMsgbox\"," +
				"c.isInformedWithsms \"isInformedWithsms\"," +
				"c.releaseScopeStaffName \"releaseScopeStaffName\"," +
				"c.isEmail \"isEmail\"" +
				" from cms_inforelease c where c.id = "+infoReleaseId;
		List<Map<String,Object>> reportComment = getReportComment(sql);
		if(reportComment != null && !reportComment.isEmpty() && reportComment.size() > 0){
			rMap = reportComment.get(0);
		}
		return rMap;
	}
	
	/**
	 * 获取普通信息项对象
	 * @param infoItemId
	 * @return
	 */
	public CmsInfoitem getInfoItemPoJoById(long infoItemId){
		String hql = "from CmsInfoitem c where c.id = "+infoItemId;
		CmsInfoitem cmsInfoitem = null;
		List<CmsInfoitem> list = (List<CmsInfoitem>)hibernateTemplate.find(hql);
		if(list != null && !list.isEmpty() && list.size() > 0){
			cmsInfoitem = list.get(0);
		}
		//CmsInfoitem cmsInfoitem = hibernateTemplate.get(CmsInfoitem.class, infoItemId);
		return cmsInfoitem;
	}
	
	/**
	 * 获取公告信息项对象
	 * @param infoItemId
	 * @return
	 */
	public CmsInforelease getInforeleasePoJoById(long infoReleaseId){
		String hql = "CmsInforelease c where c.id = "+infoReleaseId;
//		CmsInforelease cmsInforelease = hibernateTemplate.get(CmsInforelease.class, infoReleaseId);
		CmsInforelease cmsInforelease = null;
		List<CmsInforelease> list = (List<CmsInforelease>)hibernateTemplate.find(hql);
		if(list != null && !list.isEmpty() && list.size() > 0){
			cmsInforelease = list.get(0);
		}
		return cmsInforelease;
	}
	
	/**
	 * 修改公告信息
	* @date Nov 12, 201211:45:02 AM
	* @Description: TODO 
	* @param @param cmsInforelease
	* @param @return        
	* @throws
	 */
	public long updateCmsInforelease(CmsInforelease cmsInforelease){
		long rLong = 1;
		try {
			hibernateTemplate.update(cmsInforelease);
		} catch (Exception e) {
			// TODO: handle exception
			rLong = 0;
		}
		
		return rLong;
	}
	
	
	/**
	 * 构建对应公告信息tab的SqlContainer对象
	 * @param context
	 * @param tabType
	 * @return
	 */
	public String createSqlContainer(String tabType,String userId){
		String sql = "";
		String strCurrentTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd HH:mm:ss");
		sql = "SELECT " +
				"t1.id \"id\"," +
				"t1.infoId \"infoId\"," +
				"t1.timeSensibility \"timeSensibility\"," +
				"t1.releaseScopeType \"releaseScopeType\"," +
				"t1.releaseRole \"releaseRole\"," +
				"t1.releaseScopeList \"releaseScopeList\"," +
				"t1.releaseDestination \"releaseDestination\"," +
				"t1.releaser \"releaser\"," +
				"t1.auditor \"auditor\"," +
				"t1.releasePeriodStart \"releasePeriodStart\"," +
				"t1.releasePeriodEnd \"releasePeriodEnd\"," +
				"t1.releasenotes \"releasenotes\"," +
				"t1.releaseHistory \"releaseHistory\"," +
				"t1.lastModifiedTime \"lastModifiedTime\"," +
				"t1.isSentToMsgbox \"isSentToMsgbox\"," +
				"t1.isInformedWithsms \"isInformedWithsms\"," +
				"t1.releaseScopeStaffName \"releaseScopeStaffName\"," +
				"t1.isEmail \"isEmail\"," +
				"t3.infoType \"infoType\"," +
				"t3.label \"label\"," +
				"t3.category \"category\"," +
				"t3.importancelevel \"importancelevel\"," +
				"t3.status \"status\"," +
				"t3.title \"title\"," +
				"t3.drafttime \"drafttime\"," +
				"s.name \"name\"," +
				"s.account \"account\"" +
		" FROM cms_inforelease t1, cms_infoitem t3 , staff s " +
		" WHERE t1.infoId = t3.id  AND t3.author = s.account  ";
		if(tabType!=null && !"".equals(tabType)){
			if("overReleaseTime".equals(tabType)){		//获取已经过期的公告信息
				sql = sql +
						" and (t3.status=22 or t3.status=23) and t1.releasePeriodEnd< to_date('"+strCurrentTime+"','yyyy-mm-dd hh24:mi:ss') AND  (t1.auditor = '"+userId+"' OR  t1.releaser = '"+userId+"')";
			}else if("draftByMe".equals(tabType)){		//获取由我拟稿的公告信息
//				sql = sql +
//						" and t3.status<>20 and t3.author='"+userId+"'";
				sql = sql +
					" and t3.author='"+userId+"'";
			}else if("waitAudit".equals(tabType)){		//获取待审核公告信息
				sql = sql +
						" and t3.status=21 and t1.auditor='"+userId+"'";
			}else if("allInfo".equals(tabType)){		//获取全部信息
				sql = sql +
						" and t3.status<>20 AND  (t1.auditor = '"+userId+"' OR  t1.releaser = '"+userId+"')";
				
			}else if("latestRelease".equals(tabType)){	//获取最近发布

				Calendar cal =Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				String day_first = TimeFormatHelper.getTimeFormatByFree(cal.getTime(),"yyyy-MM-dd HH:mm:ss");
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DATE, 1);
				cal.add(Calendar.DATE, -1);
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				String day_end = TimeFormatHelper.getTimeFormatByFree(cal.getTime(),"yyyy-MM-dd HH:mm:ss"); 
				
				sql = sql +
					" and (t3.status=22 or t3.status=23) and releasePeriodStart>= to_date('"+day_first+"','yyyy-mm-dd hh24:mi:ss') and releasePeriodStart<=to_date('"+day_end+"','yyyy-mm-dd hh24:mi:ss') AND  (t1.auditor = '"+userId+"' OR  t1.releaser = '"+userId+"')";
			}
			sql = sql + " order by t3.id desc";
		}
		return sql;
	}
	
	/**
	 * 获取由我拟稿的草稿消息
	* @date Nov 19, 20124:32:28 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getDraftInfoItem(String userId){
		String sql = "select " +
				"c.id \"id\"," +
				"c.category \"category\"," +
				"c.importancelevel \"importancelevel\"," +
				"c.title \"title\"," +
				"c.label \"label\"," +
				"to_char(c.content) \"content\"," +
				"c.pictures \"pictures\"," +
				"c.picture_url \"picture_url\"," +
				"c.attachments \"attachments\"," +
				"c.url \"url\"," +
				"c.author \"author\"," +
				"c.drafttime \"drafttime\"," +
				"c.status \"status\"," +
				"c.infoType \"infoType\"," +
				"s.name \"name\"," +
				"s.account \"account\"" +
				" from cms_infoitem  c, staff  s where c.author = s.account and c.status=20 and c.author='"+userId+"'";
		List<Map<String,Object>> reportComment = getReportComment(sql);
		return reportComment;
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
		String sql = "select " +
				"c.id \"id\"," +
				"c.year \"year\","+
				"c.projectName \"projectName\"," +
				"c.company \"company\"," +
				"c.janData \"janData\"," +
				"c.febData \"febData\"," +
				"c.marData \"marData\"," +
				"c.aprData \"aprData\"," +
				"c.mayData \"mayData\"," +
				"c.junData \"junData\"," +
				"c.julData \"julData\"," +
				"c.augData \"augData\"," +
				"c.sepData \"sepData\"," +
				"c.octData \"octData\"," +
				"c.novData \"novData\"," +
				"c.decData \"decData\"," +
				"c.average \"average\"," +
				"c.ranking \"ranking\"," +
				"c.score \"score\""+
				" from cms_proj_report c where c.projectName = '"+projectName+"' and c.year = '"+year+"' order by c.score desc";
		List<Map<String,Object>> reportComment = getReportComment(sql);
		//System.out.println(reportComment);
		return reportComment;
	}
	
	/**
	 * 删除
	* @date 2012-12-19下午02:51:59
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisal(CmsReportProjectAppraisal cmsReportProjectAppraisal){
		this.hibernateTemplate.delete(cmsReportProjectAppraisal);
	}
	
	/**
	 * 删除
	* @date 2012-12-19下午02:51:59
	* @Description: TODO 
	* @param @param cmsReportProjectAppraisal        
	* @throws
	 */
	public void deleteCmsPeportProjectAppraisals(List<CmsReportProjectAppraisal> cmsReportProjectAppraisal){
		this.hibernateTemplate.deleteAll(cmsReportProjectAppraisal);
	}
	
	/**
	 * 根据项目名称与年份查询项目考核
	* @date 2012-12-19上午11:06:40
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<CmsReportProjectAppraisal> getCmsPeportProjectAppraisalHIB(final String projectName,final String year){
		return hibernateTemplate.executeFind(new HibernateCallback<List<CmsReportProjectAppraisal>>(){
			public List<CmsReportProjectAppraisal> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "from CmsReportProjectAppraisal r where r.projectName = '"+projectName+"' and r.year = '"+year+"'";
				Query createQuery = session.createQuery(hql);
				return createQuery.list();
			}
		});
	}
}
