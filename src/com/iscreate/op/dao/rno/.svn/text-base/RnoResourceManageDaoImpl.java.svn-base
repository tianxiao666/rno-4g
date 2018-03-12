package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.Date;
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
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GMrrDescQueryCond;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class RnoResourceManageDaoImpl implements RnoResourceManageDao {

	private static Log log=LogFactory.getLog(RnoResourceManageDaoImpl.class);
	
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 查询符合条件的爱立信ncs的描述记录数
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:39:21
	 */
	public long queryEriNcsDescCnt(final Eri2GNcsDescQueryCond cond){
		log.debug("queryEriNcsDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryEriNcsDescCnt ,where=" + where);
				String sql = "select count(RNO_2G_ERI_NCS_DESC_ID) from rno_2g_eri_ncs_descriptor "
						+ where;
				log.debug("queryEriNcsDescCnt,sql=" + sql);
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
	 * 分页查询符合条件的爱立信ncs的描述记录
	 * @param eri2gNcsDescQueryCond
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:43:26
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEriNcsDescByPage(
			final Eri2GNcsDescQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " RNO_2G_ERI_NCS_DESC_ID,NAME,BSC,FREQ_SECTION,MEA_TIME,RECORD_COUNT,CITY_ID,CREATE_TIME";
						String field_inner = " RNO_2G_ERI_NCS_DESC_ID,NAME,BSC,FREQ_SECTION,TO_CHAR(MEA_TIME,'YYYY-MM-DD HH24:MI:SS') MEA_TIME,RECORD_COUNT,CITY_ID,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME";
						String where = cond.buildWhereCont();
						log.debug("queryEriNcsDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from rno_2g_eri_ncs_descriptor  "
								+ whereResult + " ) where  rn>="+start+" and rn<="+end+" order by mea_time asc,bsc asc,freq_section asc";
						log.debug("queryEriNcsDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 查询符合条件的华为ncs的描述记录数
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:17:37
	 */
	public long queryHwNcsDescCnt(final Eri2GNcsDescQueryCond cond){
		log.debug("queryEriNcsDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryHwNcsDescCnt ,where=" + where);
				String sql = "select count(ID) from RNO_2G_HW_NCS_DESC "
						+ where;
				log.debug("queryHwNcsDescCnt,sql=" + sql);
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
	 * 分页查询符合条件的华为ncs的描述记录
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:17:58
	 */
	public List<Map<String, Object>> queryHwNcsDescByPage(final 
			Eri2GNcsDescQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " ID,MEA_TIME,BSC,PERIOD,RECORD_COUNT,CITY_ID,create_time";
						String field_inner = "ID,BSC,PERIOD,RECORD_COUNT,CITY_ID,TO_CHAR(MEA_TIME,'YYYY-MM-DD HH24:MI:SS') MEA_TIME,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME";
						String where = cond.buildWhereCont();
						log.debug("queryHwNcsDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from RNO_2G_HW_NCS_DESC  "
								+ whereResult + " order by id desc) where  rn>="+start+" and rn<="+end+" order by mea_time asc,bsc asc";
						log.debug("queryHwNcsDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	
	/**
	 * 构建爱立信mrr desc的查询条件
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:58:34
	 */
	private String buildEriMrrDescWhere(Map<String, Object> condition) {
		String where = "";
		String v = "";
		long lv;

		if (condition != null && condition.size() > 0) {
			DateUtil dateUtil=new DateUtil();
			for (String k : condition.keySet()) {
				v = condition.get(k).toString();
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("bsc".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "des.BSC like '%" + v + "%'";
				}
				else if ("mrrMeaBegDate".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "des.MEA_DATE>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("mrrMeaEndDate".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "des.MEA_DATE<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							where += (where.length() == 0 ? " " : " and ")
									+ "des.CITY_ID=" + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (where.length() > 0) {
			where = " where " + where;
		}
		return where;
	}
	
	/**
	 * 构建爱立信fas desc的查询条件
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:05:07
	 */
	private String buildEriFasDescWhere(Map<String, Object> condition) {
		String where = "";
		String v = "";
		long lv;

		if (condition != null && condition.size() > 0) {
			DateUtil dateUtil=new DateUtil();
			for (String k : condition.keySet()) {
				v = condition.get(k).toString();
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("bsc".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "des.BSC like '%" + v + "%'";
				}
				else if ("fasMeaBegDate".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "des.MEA_TIME>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("fasMeaEndDate".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "des.MEA_TIME<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							where += (where.length() == 0 ? " " : " and ")
									+ "des.CITY_ID=" + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (where.length() > 0) {
			where = " where " + where;
		}
		return where;
	}
	
	/**
	 * 计算符合某条件的fas的数量
	 * @param attachParams
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:02:14
	 */
	public long getFasDescriptorCount(final Map<String, Object> attachParams) {
		log.debug("getFasDescriptorCount.attachParams=" + attachParams);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = buildEriFasDescWhere(attachParams);
				String sql = "select count(FAS_DESC_ID) from RNO_ERI_FAS_DESC des "
						+ where;
				log.debug("getFasDescriptorCount,sql=" + sql);
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
	 * 分页查询fas描述信息
	 * @param attachParams
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2015年1月19日18:07:36
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryFasDescriptorByPage(
			final Map<String, Object> attachParams, final int startIndex, final int cnt) {
		//因为区域表的数据源不一样
		long cityId = Long.parseLong(attachParams.get("cityId").toString());
		SysArea sa = AuthDsDataDaoImpl.getSysAreaByAreaId(cityId);
		String name = "";
		if(sa != null){
			name = sa.getName();
		}
		final String cityName = name;
		
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String where = buildEriFasDescWhere(attachParams);
				log.debug("queryFasDescriptorByPage ,where=" + where);

				String sql = "select FAS_DESC_ID," +
								" MEA_TIME," +
								" BSC," +
								" RECORD_NUM as RECORD_COUNT," +
								" cast('" + cityName + "' as varchar2(256)) as CITY_NAME," +
							    " CREATE_TIME as CREATE_TIME " +
					  " from (select FAS_DESC_ID,MEA_TIME,BSC,RECORD_NUM,CREATE_TIME,rownum rn from(" +
					       " select FAS_DESC_ID," +
							 " TO_CHAR(MEA_TIME,'YYYY-MM-DD HH24:MI:SS') as MEA_TIME," +
							 " BSC," +
							 " RECORD_NUM," +
							 " TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') as CREATE_TIME," +
						     " rownum rn " +
						  " from RNO_ERI_FAS_DESC des "
						  		+ where 
						  + " order by des.CREATE_TIME desc)) where  rn>"+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryFasDescriptorByPage ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> rows = query.list();
				//System.out.println(rows);
				return rows;
			}
		});
	}
	/**
	 * 计算符合某条件的mrr的数量
	 * @param attachParams
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:51:07
	 */
	public long getMrrDescriptorCount(final Map<String, Object> attachParams) {
		log.debug("getMrrDescriptorCount.attachParams=" + attachParams);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = buildEriMrrDescWhere(attachParams);

				log.debug("getMrrDescriptorCount ,where=" + where);
				String sql = "select count(ERI_MRR_DESC_ID) from RNO_ERI_MRR_DESCRIPTOR des "
						+ where;
				log.debug("getMrrDescriptorCount,sql=" + sql);
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
	 * 分页查询mrr描述信息
	 * @param attachParams
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:51:12
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMrrDescriptorByPage(
			final Map<String, Object> attachParams, final int startIndex, final int cnt) {
		//因为区域表的数据源不一样
		long cityId = Long.parseLong(attachParams.get("cityId").toString());
		SysArea sa = AuthDsDataDaoImpl.getSysAreaByAreaId(cityId);
		String name = "";
		if(sa != null){
			name = sa.getName();
		}
		final String cityName = name;
		
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String field_out= " ERI_MRR_DESC_ID,MEA_TIME,FILE_NAME,BSC,CITY_ID";
				String field_inner = "ERI_MRR_DESC_ID,TO_CHAR(MEA_DATE,'YYYY-MM-DD HH24:MI:SS') as MEA_TIME,FILE_NAME,BSC,CITY_ID";
				String where = buildEriMrrDescWhere(attachParams);
				log.debug("queryMrrDescriptorByPage ,where=" + where);

				String sql = "select " + field_out + ",cast('" + cityName + "' as varchar2(256)) as CITY_NAME from (select "
						+ field_inner
						+ ", rownum rn from RNO_ERI_MRR_DESCRIPTOR des "
						+ where 
						+ " order by des.ERI_MRR_DESC_ID desc) where  rn>"+startIndex+" and rn<="+(startIndex+cnt)+" order by MEA_TIME asc,BSC asc";
				log.debug("queryMrrDescriptorByPage ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> rows = query.list();
				//System.out.println(rows);
				return rows;
			}
		});
	}

	/**
	 * 通过描述id获取文件的数据量
	 * @param descId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午11:01:15
	 */
	public long getEriMrrFileRecNumByDescId(final long descId) {
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {

				log.debug("getEriMrrFileRecNumByDescId, descId=" + descId);
				String sql = "select count(ERI_MRR_DESC_ID) from RNO_ERI_MRR_DESCRIPTOR ";
				log.debug("getEriMrrFileRecNumByDescId, sql=" + sql);
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
	 * 
	 * @title 查询符合条件的华为mrr的描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:34:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwMrrDescCnt(final Hw2GMrrDescQueryCond cond){
		log.debug("queryHwMrrDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryHwMrrDescCnt ,where=" + where);
				String sql = "select count(mrr_desc_id) from rno_2g_hw_mrr_desc "
						+ where;
				log.debug("queryHwMrrDescCnt,sql=" + sql);
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
	 * 
	 * @title 分页查询符合条件的华为mrr的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:35:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwMrrDescByPage(final 
			Hw2GMrrDescQueryCond  cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " mrr_desc_id,MEA_TIME,BSC,CELL_NUM,CITY_ID,create_time,RATE_TYPE";
						String field_inner = "mrr_desc_id,BSC,CELL_NUM,CITY_ID,TO_CHAR(MEA_date,'YYYY-MM-DD HH24:MI:SS') MEA_TIME,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,RATE_TYPE";
						String where = cond.buildWhereCont();
						log.debug("queryHwMrrDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from rno_2g_hw_mrr_desc  "
								+ whereResult + " order by mrr_desc_id desc) where  rn>="+start+" and rn<="+end+" order by mea_time asc,bsc asc";
						log.debug("queryHwMrrDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}

	/**
	 * 通过描述Id获取爱立信mrr描述信息
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-11下午05:12:23
	 */
	public List<Map<String, Object>> getEriMrrDetailByDescId(final long mrrDescId) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from rno_eri_mrr_descriptor " +
							" where eri_mrr_desc_id = " + mrrDescId;
				log.debug("getEriMrrDetailByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	/**
	 * 获取爱立信mrr文件的小区总数
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:23:27
	 */
	public long getEriMrrCellAndBscCntByDescId(final long mrrDescId,
			final long cityId, final String meaTime) {
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select count(*) from ( "
					+"	select distinct (qua.cell_name) as cell_name, des.bsc as bsc "
					+"	  from rno_eri_mrr_quality qua, rno_eri_mrr_descriptor des "
					+"	 where qua.eri_mrr_desc_id = des.eri_mrr_desc_id "
					+"	   and qua.eri_mrr_desc_id =" + mrrDescId
					+"	   and qua.mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and qua.city_id = " + cityId
					+"	 order by qua.cell_name) ";
				log.debug("getEriMrrCellAndBscCntByDescId, sql=" + sql);
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
	 * 获取爱立信mrr文件的平均TA
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrAverTaByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					+"   select cell_name,case when sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +      "                     
			        +"                 TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +          "                 
			        +"                 TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +     "                 
			        +"                 TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +     "                 
			        +"                 TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +      "                
			        +"                 TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +      "                
			        +"                 TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +      "                
			        +"                 TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +       "               
			        +"                 TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +       "               
			        +"                 TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +       "               
			        +"               TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +         "             
			        +"               TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +         "             
			        +"               TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +         "             
			        +"               TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +         "             
			        +"               TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 + TAVAL75) = 0  "
			        +"            then null	     "
			        +"        else (sum(TAVAL0 + TAVAL1 * 2 + TAVAL2 * 3 + TAVAL3 * 4 + 	"                       
			        +"               TAVAL4 * 5 + TAVAL5 * 6 + TAVAL6 * 7 + TAVAL7 * 8 +	"                    
			        +"               TAVAL8 * 9 + TAVAL9 * 10 + TAVAL10 * 11 +          "                    
			        +"               TAVAL11 * 12 + TAVAL12 * 13 + TAVAL13 * 14 +       "                    
			        +"               TAVAL14 * 15 + TAVAL15 * 16 + TAVAL16 * 17 +       "                    
			        +"               TAVAL17 * 18 + TAVAL18 * 19 + TAVAL19 * 20 +       "                    
			        +"               TAVAL20 * 21 + TAVAL21 * 22 + TAVAL22 * 23 +       "                    
			        +"               TAVAL23 * 24 + TAVAL24 * 25 + TAVAL25 * 26 +      "                     
			        +"               TAVAL26 * 27 + TAVAL27 * 28 + TAVAL28 * 29 +       "                    
			        +"               TAVAL29 * 30 + TAVAL30 * 31 + TAVAL31 * 32 +       "                    
			        +"               TAVAL32 * 33 + TAVAL33 * 34 + TAVAL34 * 35 +       "                    
			        +"               TAVAL35 * 36 + TAVAL36 * 37 + TAVAL37 * 38 +       "                    
			        +"               TAVAL38 * 39 + TAVAL39 * 40 + TAVAL40 * 41 +       "                    
			        +"               TAVAL41 * 42 + TAVAL42 * 43 + TAVAL43 * 44 +       "                    
			        +"               TAVAL44 * 45 + TAVAL45 * 46 + TAVAL46 * 47 +       "                    
			        +"               TAVAL47 * 48 + TAVAL48 * 49 + TAVAL49 * 50 +       "                    
			        +"               TAVAL50 * 51 + TAVAL51 * 52 + TAVAL52 * 53 +       "                    
			        +"               TAVAL53 * 54 + TAVAL54 * 55 + TAVAL55 * 56 +       "                    
			        +"               TAVAL56 * 57 + TAVAL57 * 58 + TAVAL58 * 59 +       "                    
			        +"               TAVAL59 * 60 + TAVAL60 * 61 + TAVAL61 * 62 +       "                    
			        +"               TAVAL62 * 63 + TAVAL63 * 64 + TAVAL64 * 65 +       "                    
			        +"               TAVAL65 * 66 + TAVAL66 * 67 + TAVAL67 * 68 +      "                     
			        +"               TAVAL68 * 69 + TAVAL69 * 70 + TAVAL70 * 71 +     "                      
			        +"               TAVAL71 * 72 + TAVAL72 * 73 + TAVAL73 * 74 +     "                      
			        +"               TAVAL74 * 75 + TAVAL75 * 76)/ sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +  "                         
			        +"                 TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +          "                 
			        +"                 TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +     "                 
			        +"                 TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +     "                 
			        +"                 TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +    "                  
			        +"                 TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +     "                 
			        +"                 TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +     "                 
			        +"                 TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +    "                 
			        +"                 TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +    "
			        +"                 TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +    " 
			        +"                 TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +    "     
			        +"                 TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +    "        
			        +"                 TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +    "         
			        +"                 TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +    "        
			        +"                 TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 + TAVAL75))-1   "     
			        +"            end     aver_ta  "
			        +"   from rno_eri_mrr_ta      "
			        +"    where eri_mrr_desc_id=  " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
			        +"    group by cell_name  "
			        +"    order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrAverTaByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的小区和对应的BSC
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:09
	 */
	public List<Map<String, Object>> queryEriMrrCellAndBscByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					    +" select distinct (qua.cell_name) as cell_name, des.bsc as bsc  "
						+"	  from rno_eri_mrr_quality qua, rno_eri_mrr_descriptor des  "
						+"	 where qua.eri_mrr_desc_id = des.eri_mrr_desc_id  "
						+"	   and qua.eri_mrr_desc_id = " + mrrDescId
						+"	   and qua.mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
						+"	   and qua.city_id = " + cityId
						+"	 order by qua.cell_name  ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrCellAndBscByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的下行通好率
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlQua0t5RateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					+" select cell_name,		"
					+"	       case	"
					+"	         when sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 +	"
					+"	                  rxqualdl5 + rxqualdl6 + rxqualdl7) = 0 then	"
					+"	          null	"
					+"	         else	"
					+"	          sum((rxqualdl0 + rxqualdl1 + rxqualdl2) +	"
					+"	              0.7 * (rxqualdl3 + rxqualdl4 + rxqualdl5)) /	"
					+"	          sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 +	"
					+"	              rxqualdl5 + rxqualdl6 + rxqualdl7)	"
					+"	       end dl_qua0t5_rate	"
					+"	  from rno_eri_mrr_quality	"
					+"	 where eri_mrr_desc_id =" + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+"	 group by cell_name	"
					+"	 order by cell_name	 ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrDlQua0t5RateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的6,7级信号下行质量占比
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlQua6t7RateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					+"	select cell_name,  "
					+"	       case  "
					+"	         when sum(qua.rxqualdl0 + qua.rxqualdl1 + qua.rxqualdl2 +  "
					+"	                  qua.rxqualdl3 + qua.rxqualdl4 + qua.rxqualdl5 +  "
					+"	                  qua.rxqualdl6 + qua.rxqualdl7) = 0 then  "
					+"	          null  "
					+"	         else  "
					+"	          sum(qua.rxqualdl6 + qua.rxqualdl7) /  "
					+"	          sum(qua.rxqualdl0 + qua.rxqualdl1 + qua.rxqualdl2 + qua.rxqualdl3 +  "
					+"	              qua.rxqualdl4 + qua.rxqualdl5 + qua.rxqualdl6 + qua.rxqualdl7)   "
					+"	       end dl_qua6t7_rate  "
					+"	  from rno_eri_mrr_quality qua  "
					+"	 where qua.eri_mrr_desc_id = " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+"	 group by cell_name  "
					+"	 order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrDlQua6t7RateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的下行平均信号强度
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlStrenRateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
						+"	select cell_name, "
					  +"     case "
					  +"       when sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 + "
					  +"                RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 + "
					  +"                RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 + "
					  +"                RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + "
					  +"                RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + "
					  +"              RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + "
					  +"              RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + "
					  +"              RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + "
					  +"              RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + "
					  +"              RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + "
					  +"              RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + "
					  +"              RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + "
					  +"              RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) = 0 then "
					  +"      null "
					  +"     else "
					  +"      sum(RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 + RXLEVDL5 + "
					  +"          RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 + RXLEVDL10 + "
					  +"          RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 + RXLEVDL15 + "
					  +"          RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + RXLEVDL20 + "
					  +"          RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + RXLEVDL25 + "
					  +"          RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + RXLEVDL30 + "
					  +"          RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + RXLEVDL35 + "
					  +"          RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + RXLEVDL40 + "
					  +"          RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + RXLEVDL45 + "
					  +"          RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + RXLEVDL50 + "
					  +"          RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + RXLEVDL55 + "
					  +"          RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + RXLEVDL60 + "
					  +"          RXLEVDL61 + RXLEVDL62) / "
					  +"      sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 + "
					  +"          RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 + "
					  +"          RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 + "
					  +"          RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + "
					  +"          RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + "
					  +"          RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + "
					  +"          RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + "
					  +"          RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + "
					  +"          RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + "
					  +"          RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + "
					  +"          RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + "
					  +"          RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + "
					  +"          RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) "
					  +"   end dl_stren_rate "
					  +" from rno_eri_mrr_strength "
					+"	 where eri_mrr_desc_id = " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+"	 group by cell_name "
					+"	 order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrDlStrenRateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的下行弱信号比例
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrDlWeekSignalByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					+"	select cell_name,   "
					+"       case   "
					+"         when sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +   "
					+"                RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +   "
					+"                RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +   "
					+"                RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +   "
					+"                RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +   "
					+"                RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +   "
					+"                RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +   "
					+"                RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +   "
					+"                RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +   "
					+"                RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +   "
					+"                RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +   "
					+"                RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +   "
					+"                RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) = 0 then   "
					+"        null   "
					+"       else   "
					+"        1 - (sum(RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + RXLEVDL20 +   "
					+"                 RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + RXLEVDL25 +   "
					+"                 RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + RXLEVDL30 +   "
					+"                 RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + RXLEVDL35 +   "
					+"                 RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + RXLEVDL40 +   "
					+"                 RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + RXLEVDL45 +   "
					+"                 RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + RXLEVDL50 +   "
					+"                 RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + RXLEVDL55 +   "
					+"                 RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + RXLEVDL60 +   "
					+"                 RXLEVDL61 + RXLEVDL62 + RXLEVDL63) /   "
					+"        sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +   "
					+"                 RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +   "
					+"                 RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +   "
					+"                 RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +   "
					+"                 RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +   "
					+"                 RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +   "
					+"                 RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +   "
					+"                 RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +   "
					+"                 RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +   "
					+"                 RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +   "
					+"                 RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +   "
					+"                 RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +   "
					+"                 RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63))   "
					+"     	end dl_week_signal   "
					+"	from rno_eri_mrr_strength   "
					+"	where eri_mrr_desc_id =  " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+"	  group by cell_name   "
					+"	  order by cell_name    ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrDlWeekSignalByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的最大TA
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrMaxTaByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql =  "select * from (select t.*,rownum rn from ("
				 +"	 select cell_name, 	"
				 +"      max(greatest(TAVAL0, TAVAL1 , TAVAL2 , TAVAL3 , TAVAL4 , 	"                          
				 +"                   TAVAL5 , TAVAL6 , TAVAL7 , TAVAL8 , TAVAL9 ,    	"                       
				 +"                TAVAL10 , TAVAL11 , TAVAL12 , TAVAL13 , TAVAL14 ,   	"                   
				 +"                TAVAL15 , TAVAL16 , TAVAL17 , TAVAL18 , TAVAL19 ,   	"                   
				 +"                TAVAL20 , TAVAL21 , TAVAL22 , TAVAL23 , TAVAL24 ,  	"                    
				 +"                 TAVAL25 , TAVAL26 , TAVAL27 , TAVAL28 , TAVAL29 ,   "                  
				 +"                TAVAL30 , TAVAL31 , TAVAL32 , TAVAL33 , TAVAL34 ,    "             
				 +"                TAVAL35 , TAVAL36 , TAVAL37 , TAVAL38 , TAVAL39 ,      "                   
				 +"                TAVAL40 , TAVAL41 , TAVAL42 , TAVAL43 , TAVAL44 ,     "                    
				 +"                TAVAL45 , TAVAL46 , TAVAL47 , TAVAL48 , TAVAL49 ,     "                    
				 +"                TAVAL50 , TAVAL51 , TAVAL52 , TAVAL53 , TAVAL54 ,     "                    
				 +"                TAVAL55 , TAVAL56 , TAVAL57 , TAVAL58 , TAVAL59 ,      "                   
				 +"                TAVAL60 , TAVAL61 , TAVAL62 , TAVAL63 , TAVAL64 ,      "                   
				 +"                TAVAL65 , TAVAL66 , TAVAL67 , TAVAL68 , TAVAL69 ,     "                    
				 +"                TAVAL70 , TAVAL71 , TAVAL72 , TAVAL73 , TAVAL74 ,    "
				 +"                TAVAL75)) max_ta   "
				 +"  from rno_eri_mrr_ta   "
				 +" where eri_mrr_desc_id = " + mrrDescId
				+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
				+"	   and city_id = " + cityId
				 +"   group by cell_name   "
				 +"   order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrMaxTaByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的上行通好率
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlQua0t5RateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from (select t.*,rownum rn from ("
					+"	select cell_name,  "
					+"       case  "
					+"         when sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +  "
					+"                  rxqualul5 + rxqualul6 + rxqualul7) = 0 then  "
					+"          null  "
					+"         else  "
					+"          sum((rxqualul0 + rxqualul1 + rxqualul2) +  "
					+"              0.7 * (rxqualul3 + rxqualul4 + rxqualul5)) /  "
					+"          sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +  "
					+"              rxqualul5 + rxqualul6 + rxqualul7)  "
					+"       end ul_qua0t5_rate  "
					+"   from rno_eri_mrr_quality  "
					+" where eri_mrr_desc_id = " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+" 	group by cell_name  "
					+" 	order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrUlQua0t5RateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的6,7级信号上行质量占比
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlQua6t7RateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql =   "select * from (select t.*,rownum rn from ("
					+"	select cell_name,   "
					+"       case   "
					+"         when sum(qua.rxqualul0 + qua.rxqualul1 + qua.rxqualul2 +   "
					+"                  qua.rxqualul3 + qua.rxqualul4 + qua.rxqualul5 +   "
					+"                  qua.rxqualul6 + qua.rxqualul7) = 0 then   "
					+"          null   "
					+"         else   "
					+"          sum(qua.rxqualul6 + qua.rxqualul7) /   "
					+"         sum(qua.rxqualul0 + qua.rxqualul1 + qua.rxqualul2 + qua.rxqualul3 +   "
					+"              qua.rxqualul4 + qua.rxqualul5 + qua.rxqualul6 + qua.rxqualul7)   "
					+"       end ul_qua6t7_rate   "
					+"	  from rno_eri_mrr_quality qua   "
					+"	 where qua.eri_mrr_desc_id = " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+" 		group by cell_name   "
					+" 		order by cell_name   ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrUlQua6t7RateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}


	/**
	 * 获取爱立信mrr文件的上行平均信号强度
	 * @param mrrDescId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-4下午02:24:42
	 */
	public List<Map<String, Object>> queryEriMrrUlStrenRateByDescId(final long mrrDescId,
			final long cityId, final String meaTime, final int startIndex, final int cnt) {
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql =  "select * from (select t.*,rownum rn from ("
					  +"	select cell_name, "
					  +"     case "
					  +"       when sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 + "
					  +"                RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 + "
					  +"                RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 + "
					  +"                RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 + "
					  +"                RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 + "
					  +"              RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 + "
					  +"              RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 + "
					  +"              RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 + "
					  +"              RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 + "
					  +"              RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 + "
					  +"              RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 + "
					  +"              RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 + "
					  +"              RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) = 0 then "
					  +"      null "
					  +"     else "
					  +"      sum(RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 + RXLEVUL5 + "
					  +"          RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 + RXLEVUL10 + "
					  +"          RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 + RXLEVUL15 + "
					  +"          RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 + RXLEVUL20 + "
					  +"          RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 + RXLEVUL25 + "
					  +"          RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 + RXLEVUL30 + "
					  +"          RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 + RXLEVUL35 + "
					  +"          RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 + RXLEVUL40 + "
					  +"          RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 + RXLEVUL45 + "
					  +"          RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 + RXLEVUL50 + "
					  +"          RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 + RXLEVUL55 + "
					  +"          RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 + RXLEVUL60 + "
					  +"          RXLEVUL61 + RXLEVUL62) / "
					  +"      sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 + "
					  +"          RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 + "
					  +"          RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 + "
					  +"          RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 + "
					  +"          RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 + "
					  +"          RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 + "
					  +"          RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 + "
					  +"          RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 + "
					  +"          RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 + "
					  +"          RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 + "
					  +"          RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 + "
					  +"          RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 + "
					  +"          RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) "
					  +"   end ul_stren_rate "
					  +" from rno_eri_mrr_strength "
					+"	 where eri_mrr_desc_id = " + mrrDescId
					+"	   and mea_date = to_date('"+meaTime+"','yyyy-mm-dd hh24:mi:ss') "
					+"	   and city_id = " + cityId
					+"	 group by cell_name "
					+"	 order by cell_name  ) t ) where rn > "+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryEriMrrUlStrenRateByDescId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	public String buildWhereForQueryBsc(Map<String, String> bscQuery) {
		String where = "";
		long cityId = 0;
		long areaId = 0;
		if(bscQuery.get("cityId").toString() != null) {
			cityId = Long.parseLong(bscQuery.get("cityId").toString());
		}

		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		if(areaStr == null) {
//			areaStr += "0";
			//@author chao.xj  2015-4-15 下午3:49:04
			areaStr = "0";
		}
		if(!("").equals(areaStr)) {
			where += " bsc.bsc_id in (select bsc_id from rno_bsc_rela_area where area_id in("+areaStr+")) ";
		}
		if(bscQuery.get("bscEnName").toString() != null) {
			String bscEnName = bscQuery.get("bscEnName").toString();
			if(!("").equals(bscEnName)) {
				where += " and bsc.ENGNAME like '%"+bscEnName+"%' ";
			}
		}
		if(bscQuery.get("manufacturers").toString() != null) {
			String manufacturers = bscQuery.get("manufacturers").toString();
			if(!("").equals(manufacturers)) {
				String s = "0,";
				if("爱立信".contains(manufacturers)) {
					s += "1,";
				}
				if("华为".contains(manufacturers)) {
					s += "2,";
				}
				s = s.substring(0,s.length()-1);
				where += " and bsc.MANUFACTURERS in("+s+")";
			}
		}
		
		if(!("").equals(where)) {
			where = " where " + where;
		}
		return where;
	}
	
	/**
	 * 通过条件获取bsc总数
	 * @param bscQuery
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:18:45
	 */
	public long queryBscCntByCond(Map<String, String> bscQuery) {
		log.debug("queryBscCntByCond. bscQuery=" + bscQuery);
		
		final String where = buildWhereForQueryBsc(bscQuery);
		
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				String sql = "select count(*) from rno_bsc bsc " + where + " and bsc.status='N'";
				log.debug("queryBscCntByCond,sql=" + sql);
				
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
	 * 分页获取bsc信息
	 * @param bscQuery
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:19:03
	 */
	public List<Map<String, Object>> queryBscByPage(
			Map<String, String> bscQuery, final int startIndex, final int cnt) {
		log.debug("queryBscByPage. bscQuery=" + bscQuery + ",startIndex="+startIndex+",cnt="+cnt);
		
		final String where = buildWhereForQueryBsc(bscQuery);
		
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = " select BSC_ID,CHINESENAME,ENGNAME," +
										" case MANUFACTURERS" +
									"      when '1' then cast('爱立信' as varchar(50)) " +
									" 	   when '2' then cast('华为' as varchar(50)) " +
									" 	end as MANUFACTURERS from " +
								" (select BSC_ID,CHINESENAME,ENGNAME,MANUFACTURERS,rownum rn " +
									" from rno_bsc bsc " + where + " and bsc.status='N' order by bsc.ENGNAME) " +
							" where  rn>="+startIndex+" and rn<="+(startIndex+cnt);
				log.debug("queryBscByPage, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		
	}
	
	/**
	 * 判断bsc是否存在与小区有关联关系
	 * @param bscEngName
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10下午03:32:12
	 */
	public boolean isBscRelaToCell(final String bscEngName) {
		log.debug("isBscRelaToCell. bscEngName=" + bscEngName);
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select count(*) from  rno_bsc bsc , cell cell " +
								" where cell.bsc_id = bsc.bsc_id  " +
									" and bsc.engname = '"+bscEngName+"'";
				log.debug("判断bsc是否存在与小区有关联关系, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				boolean result = false;
				if(cnt > 0) {
					result = true;
				}
				return result;
			}
		});
	}
	
	/**
	 * 通过名称删除BSC
	 * @param bscEngName
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10上午10:52:19
	 */
	public boolean deleteBscRelatoAreaByName(final String bscEngName, final String areaStr) {
		log.debug("deleteBscRelatoAreaByName. bscEngName=" + bscEngName + ",areaStr="+areaStr);
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {
				boolean result = false;
				//删除bsc与区域关系数据
				String sql = " delete from rno_bsc_rela_area rela " +
						" where rela.bsc_id in( " +
								" select bsc.bsc_id from rno_bsc bsc " +
									" where bsc.engname ='"+bscEngName+"')" +
							" and rela.area_id in ("+areaStr+")";
				log.debug("删除bsc与区域关系数据, sql=" + sql);
				SQLQuery query1 = session.createSQLQuery(sql);
				int resCnt1 = query1.executeUpdate();
				log.debug("删除bsc与区域关系数据, 受影响行数=" + resCnt1);
				
				//更新bsc的信息状态为“X”，表示已删除
				String sql2 = " update rno_bsc bsc " +
								" set bsc.status = 'X' " +
								" where bsc.engname ='"+bscEngName+"'";
				log.debug("更新bsc数据为已删除状态, sql=" + sql2);
				SQLQuery query2 = session.createSQLQuery(sql2);
				int resCnt2 = query2.executeUpdate();
				log.debug("更新bsc为已删除状态, 受影响行数=" + resCnt2);
				
				if(resCnt1 > 0 && resCnt2 > 0) {
					result = true;
				} else {
					session.beginTransaction().rollback();
				}
				return result;
			}
		});
	}

	/**
	 * 新增单个BSC
	 * @param bscEngName
	 * @param manufacturers
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-16下午06:12:15
	 */
	public boolean addSingleBsc(final String bscEngName,final String manufacturers, final long cityId) {
		log.debug("addSingleBsc. bscEngName=" + bscEngName + ",manufacturers="+manufacturers+",cityId="+cityId);
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {
				boolean result = false;
				//增加BSC
				String sql = "  merge into rno_bsc tar  "+
					"  using (select '"+bscEngName+"' as engname," +
									" '"+manufacturers+"' as manufacturers from dual) temp "+
					"  	on (tar.engname = temp.engname)  "+
					"  	   when matched then update set  "+
					"  	                 tar.manufacturers = temp.manufacturers, "+
					"  	                 tar.status = 'N' "+
					"  	   when not matched then  "+
					"  	   	  insert (tar.bsc_id, tar.chinesename, tar.engname, tar.manufacturers, tar.status) "+
					"  		  values (SEQ_RNO_BSC.NEXTVAL, temp.engname,  temp.engname, temp.manufacturers, 'N')";
				log.debug("增加单个BSC, sql=" + sql);
				SQLQuery query1 = session.createSQLQuery(sql);
				int resCnt1 = query1.executeUpdate();
				log.debug("增加单个BSC, 受影响行数=" + resCnt1);
				
				//增加单个BSC与区域关系
				String sql2 = " insert into rno_bsc_rela_area "+
							  " (bsc_area_id, bsc_id, area_id) "+
							  " select SEQ_RNO_BSC_RELA_AREA.NEXTVAL, bsc_id, "+cityId+
							  "   from rno_bsc "+
							  "  where engname = '"+bscEngName+"'";
				log.debug("增加单个BSC与区域关系, sql=" + sql2);
				SQLQuery query2 = session.createSQLQuery(sql2);
				int resCnt2 = query2.executeUpdate();
				log.debug("增加单个BSC与区域关系, 受影响行数=" + resCnt2);
				
				if(resCnt1 > 0 && resCnt2 > 0) {
					result = true;
				} else {
					session.beginTransaction().rollback();
				}
				return result;
			}
		});
	}

}
