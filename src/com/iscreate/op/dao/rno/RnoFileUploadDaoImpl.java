package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.DataUploadQueryCond;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoFileUploadDaoImpl implements RnoFileUploadDao {

	private static Log log = LogFactory.getLog(RnoFileUploadDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 查询符合条件的上传记录
	 * 
	 * @param cond
	 * @return
	 * @author brightming 2014-8-22 上午9:36:59
	 */
	public long queryUploadDataCnt(final DataUploadQueryCond cond) {
		log.debug("queryUploadDataCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryUploadDataCnt ,where=" + where);
				String sql = "select count(DATA_COLLECT_ID) from rno_data_collect_rec "
						+ where;
				log.debug("queryUploadDataCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});

	}

	/**
	 * 分页查询符合条件的上传记录
	 * 
	 * @param cond
	 * @param page
	 * @return
	 * @author brightming 2014-8-22 上午9:37:20
	 */
	public List<RnoDataCollectRec> queryUploadDataByPage(
			final DataUploadQueryCond cond, final Page page) {
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoDataCollectRec>>() {
					public List<RnoDataCollectRec> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " DATA_COLLECT_ID,UPLOAD_TIME,BUSINESS_TIME,FILE_NAME,ORI_FILE_NAME,ACCOUNT,CITY_ID,BUSINESS_DATA_TYPE,FILE_SIZE,FILE_STATUS,JOB_ID,LAUNCH_TIME,COMPLETE_TIME";
						String field_inner = " DATA_COLLECT_ID,TO_CHAR(UPLOAD_TIME,'YYYY-MM-DD HH24:MI:SS') UPLOAD_TIME,TO_CHAR(BUSINESS_TIME,'YYYY-MM-DD HH24:MI:SS') BUSINESS_TIME,FILE_NAME,ORI_FILE_NAME,ACCOUNT,CITY_ID,BUSINESS_DATA_TYPE,FILE_SIZE,FILE_STATUS, rec.JOB_ID JOB_ID, TO_CHAR(job.LAUNCH_TIME, 'YYYY-MM-DD HH24:MI:SS') LAUNCH_TIME, TO_CHAR(job.COMPLETE_TIME, 'YYYY-MM-DD HH24:MI:SS') COMPLETE_TIME ";
						String where = cond.buildWhereCont();
						log.debug("queryUploadDataByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",ROW_NUMBER () OVER (ORDER BY upload_time desc ) AS rn from rno_data_collect_rec rec " +
										" left join rno_job job on (rec.job_id = job.job_id) "
								+ whereResult + " order by upload_time desc) where  rn>="+start+" and rn<="+end;
						log.debug("queryUploadDataByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						List<RnoDataCollectRec> result = new ArrayList<RnoDataCollectRec>();
						if (rows != null && rows.size() > 0) {
							RnoDataCollectRec rec;
							DateUtil dateUtil=new DateUtil();
							for (Map<String, Object> row : rows) {
								rec = RnoHelper.commonInjection(
										RnoDataCollectRec.class, row,dateUtil);
								if (rec != null) {
									result.add(rec);
								}
							}
						}

						return result;
					}
				});
	}
	/**
	 * 查询某job的业务报告数量
	 * @param jobId
	 * @return
	 * @author brightming
	 * 2014-8-22 下午4:32:57
	 */
	public int queryJobReportCnt(final Long jobId){
		log.debug("queryJobReportCnt.jobId=" + jobId);
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select count(report_id) from rno_job_report where report_type=1 and job_id="+jobId;
				log.debug("queryJobReportCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});

	}

	/**
	 * 分页查询某job的业务报告
	 * @param jobId
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-22 下午4:33:20
	 */
	public List<Map<String, Object>> queryJobReportByPage(final Long jobId,
			final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " REPORT_ID,JOB_ID,STAGE,BEG_TIME,END_TIME,STATE,ATT_MSG";
						String field_inner = " REPORT_ID,JOB_ID,STAGE,TO_CHAR(BEG_TIME,'YYYY-MM-DD HH24:MI:SS') BEG_TIME,TO_CHAR(END_TIME,'YYYY-MM-DD HH24:MI:SS') END_TIME,STATE,ATT_MSG ";
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from rno_job_report where report_type=1 and job_id="+jobId
								+" ) where  rn>="+start+" and rn<="+end+" order by REPORT_ID asc";
						log.debug("queryJobReportByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();

						return rows;
					}
				});
	}
}
