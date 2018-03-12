package com.iscreate.op.dao.report;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;


import com.iscreate.op.pojo.report.ReportComment;
import com.iscreate.op.pojo.report.ReportMessageMonth;

public class CommentReportDaoImpl implements CommentReportDao{
	
	private HibernateTemplate hibernateTemplate;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}


	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 获取报表评论快照
	* @date Nov 5, 20129:50:23 AM
	* @Description: TODO 
	* @param @param indicators
	* @param @param dimension
	* @param @param bizunitinnstId
	* @param @param statisticaltime
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getReportMessage(final String indicators,final String dimension,final long organizationId,final String statisticaltime){
			List<Map<String, Object>> list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlString ="SELECT  " +
								"r.id \"id\"," +
								"r.indicators \"indicators\"," +
								"r.dimension \"dimension\"," +
								"r.organizationId \"organizationId\"," +
								"to_char(r.statisticaltime,'yyyy-mm-dd hh24:mi:ss') \"statisticaltime\"" +
								" FROM report_message_month r"
								+" WHERE r.indicators = '"+indicators
								+"' AND r.DIMENSION = '"+dimension
								+"' AND r.organizationId = "+organizationId;
					if(statisticaltime != null && !"null".equals(statisticaltime) && !"".equals(statisticaltime)){
						sqlString = sqlString
							+" AND r.statisticaltime = to_date('"+statisticaltime+"','yyyy-mm-dd hh24:mi:ss')";
					}
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
			Map<String, Object> map = null;
			if(list != null && list.size() > 0){
				map = list.get(0);
			}else{
				map  = null;
			}
		return map;
	}
	
	/**
	 * 保存报表评论
	* @date Nov 5, 201210:10:30 AM
	* @Description: TODO 
	* @param @param org
	* @param @return        
	* @throws
	 */
	public Long saveReportComment(ReportComment reportComment){
		Serializable save = hibernateTemplate.save(reportComment);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	
	/**
	 * 保存报表快照
	* @date Nov 5, 201210:10:30 AM
	* @Description: TODO 
	* @param @param org
	* @param @return        
	* @throws
	 */
	public Long saveReportMessageMonth(ReportMessageMonth reportMessageMonth){
		Serializable save = hibernateTemplate.save(reportMessageMonth);
		long parseLong = Long.parseLong(save.toString());
		return parseLong;
	}
	
	/**
	 * 获取指定报表的评论
	 * @param reportmessageid
	 * @return
	 */
	public List<Map<String, Object>> getReportComment(final long reportmessageid){
			List<Map<String, Object>> list =  this.hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session session)
						throws HibernateException, SQLException {
					String sqlString ="SELECT " +
							"r.id \"id\"," +
							"r.reportmessageid \"reportmessageid\"," +
							"r.criticsid \"criticsid\"," +
							"r.criticsname \"criticsname\"," +
							"r.criticstime \"criticstime\"," +
							"r.content \"content\"" +
							" FROM report_comment r "
						+" WHERE r.reportmessageid = "+reportmessageid + " ORDER BY r.criticstime desc";
					SQLQuery query = session.createSQLQuery(sqlString);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List find = query.list();
					return find;
				}
			});
		return list;
	}
}
