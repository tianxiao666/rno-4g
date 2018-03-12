package com.iscreate.op.dao.cms;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;




public class CmsInfoProviderDaoImpl implements CmsInfoProviderDao {
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	
	/**
	 * 所有的时间有效的公告的数量
	* @date Nov 12, 20125:12:10 PM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCount(final String sqlIds,final String startTime,final String endTime){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				List find = null;
				if(sqlIds != null && !sqlIds.equals("")){
					String sql = "select t1.lastmodifiedtime \"lastmodifiedtime\",t2.title \"title\",t1.infoId \"infoId\" from cms_inforelease  t1 ,cms_infoitem  t2 where t1.releaseperiodstart <= '"
						+ startTime
						+ "' and t1.releaseperiodend >= '"
						+ endTime
						+ "' and t1.infoId=t2.id and  t2.id in ("+sqlIds+") and t2.status=22 ";// 发布栏目是“公司重要公告”类型,status=22表示已经发布
					SQLQuery query = session.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					find = query.list();
				}
				return find;
			}
		});
	return list;
	}
	
	
	/**
	 * 所有的时间有效的公告(分页)
	* @date Nov 13, 20129:46:45 AM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @param startTime
	* @param @param endTime
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCountPaging(final String sqlIds,final String startTime,final String endTime,final int start,final int length,final boolean timeAsc){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
						List find = null;
						if(sqlIds != null && !sqlIds.equals("")){
							String sql = "select t1.lastmodifiedtime \"lastmodifiedtime\",t2.title \"title\",t1.infoId \"infoId\" from cms_inforelease  t1 ,cms_infoitem  t2 where t1.releaseperiodstart <= '"
								+ startTime
								+ "' and t1.releaseperiodend >= '"
								+ endTime
								+ "' and t1.infoId=t2.id and  t2.id in ("+sqlIds+") and t2.status=22 order by t1.lastmodifiedtime "
								+ (timeAsc ? "asc" : "desc")
								+ " limit "
								+ start
								+ " , "
								+ length;
							SQLQuery query = session.createSQLQuery(sql);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							find = query.list();
					}
				return find;
			}
		});
	return list;
	}
	
	/**
	 * 用户能否查看infoid的公告内容
	* @date Nov 12, 20125:23:07 PM
	* @Description: TODO 
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> allowUserToViewTheAnnouncement(final String startTime,final String endTime){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select t1.lastmodifiedtime \"lastmodifiedtime\",t1.infoId \"infoId\" from cms_inforelease  t1 ,cms_infoitem  t2 where t1.releaseperiodstart <= '"
					+ startTime
					+ "' and t1.releaseperiodend >= '"
					+ endTime + "' and t1.infoId=t2.id and t2.status=22";// 公告类型
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
	return list;
	}
	
	
	/**
	 * 获取发布范围类型的公告信息
	* @date Nov 12, 20125:16:56 PM
	* @Description: TODO 
	* @param @param releaseScopeType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getInforeleaseByReleaseScopeType(final int releaseScopeType){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql="select t1.id \"id\"," +
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
						"t1.isEmail \"isEmail\"" +
						" from cms_inforelease  t1 where t1.releaseScopeType="+releaseScopeType;
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
	
	/**
	 * 根据消息ID获取信息详细内容
	* @date Nov 13, 20129:54:39 AM
	* @Description: TODO 
	* @param @param infoid
	* @param @return        
	* @throws
	 */
	public  List<Map<String, Object>> getAnnouncementDetailById(final long infoid){
		
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select " +
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
						"t2.category \"category\"," +
						"t2.importancelevel \"importancelevel\"," +
						"t2.title \"title\"," +
						"t2.label \"label\"," +
						"to_char(t2.content) \"content\"," +
						"t2.pictures \"pictures\"," +
						"t2.picture_url \"picture_url\"," +
						"t2.attachments \"attachments\"," +
						"t2.url \"url\"," +
						"t2.author \"author\"," +
						"t2.drafttime \"drafttime\"," +
						"t2.status \"status\"," +
						"t2.infoType \"infoType\"" +
						" from cms_inforelease t1 ,cms_infoitem t2 where t1.infoId="
					+ infoid + " and t1.infoId=t2.id ";
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
		
	}
	
	
	/**
	 * 所有的时间有效的公告
	* @date Nov 13, 20129:46:45 AM
	* @Description: TODO 
	* @param @param sqlIds
	* @param @param startTime
	* @param @param endTime
	* @param @param start
	* @param @param length
	* @param @param timeAsc
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllValidAnnouncementsCount(final String sqlIds,final String startTime,final String endTime,final boolean timeAsc){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
						List find = null;
						if(sqlIds != null && !sqlIds.equals("")){
							String sql = "select t1.lastmodifiedtime," +
									"t2.title \"title\"," +
									"t1.infoId \"infoId\"," +
									"s.name \"name\"," +
									"s.account \"account\"" +
									" from cms_inforelease  t1 ,cms_infoitem  t2, staff s  where t1.releaseperiodstart <= to_date('"
								+ startTime
								+ "','yyyy-mm-dd hh24:mi:ss') and t1.releaseperiodend >= to_date('"
								+ endTime
								+ "','yyyy-mm-dd hh24:mi:ss') and t1.infoId=t2.id and  t2.id in ("+sqlIds+") and t2.status=22  AND t1.releaser = s.account order by t1.lastmodifiedtime "
								+ (timeAsc ? "asc" : "desc");
							SQLQuery query = session.createSQLQuery(sql);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							find = query.list();
					}
				return find;
			}
		});
	return list;
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
	public List<Map<String, Object>> getCmsReportProjectAppraisal(final String sqlIds,final String startTime,final String endTime,final int releaseScopeType,final boolean timeAsc){
		List<Map<String, Object>> list =  this.hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "SELECT t3.id \"id\"," +
						"t3.projectName \"projectName\"," +
						"t3.company \"company\"," +
						"t3.janData \"janData\"," +
						"t3.febData \"febData\"," +
						"t3.marData \"marData\"," +
						"t3.aprData \"aprData\"," +
						"t3.mayData \"mayData\"," +
						"t3.junData \"junData\"," +
						"t3.julData \"julData\"," +
						"t3.augData \"augData\"," +
						"t3.sepData \"sepData\"," +
						"t3.octData \"octData\"," +
						"t3.novData \"novData\"," +
						"t3.decData \"decData\"," +
						"t3.average \"average\"," +
						"t3.ranking \"ranking\"," +
						"t3.score \"score\"," +
						"t3.year \"year\"" +
						" FROM cms_inforelease  t1,cms_infoitem_appraisal  t2,cms_report_project_appraisal  t3 "
							+" WHERE t1.releaseScopeType="+releaseScopeType+" AND t1.infoId = t2.infoitemId AND t2.appraisalId = t3.id "
							+" t1.releaseperiodstart <= to_date('" 
							+ startTime + "','yyyy-mm-dd hh24:mi:ss')"
							+" AND t1.releaseperiodend >= to_date('"
							+ endTime + "','yyyy-mm-dd hh24:mi:ss')"
							+ (timeAsc ? "asc" : "desc");
				//String sql="select * from cms_inforelease as t1 where t1.releaseScopeType="+releaseScopeType;
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List find = query.list();
				return find;
			}
		});
		return list;
	}
}
