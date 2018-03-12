package com.iscreate.op.dao.rno;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.model.G2NcsQueryCond;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask.TaskInfo;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.rno.parser.vo.G4PciRec;
import com.iscreate.op.service.rno.parser.vo.NcsIndex;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.op.service.rno.tool.UnicodeUtil;

/**
 * @author brightming
 * @version 1.0
 * @created 17-һ��-2014 11:20:37
 */
public class RnoStructureQueryDaoImpl implements RnoStructureQueryDao {

	private static Log log = LogFactory.getLog(RnoStructureQueryDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	private DateUtil dateUtil=new DateUtil();
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public RnoStructureQueryDaoImpl() {

	}

	public void finalize() throws Throwable {

	}

	/**
	 * 分页查询ncs描述信息
	 * 
	 * @param condition
	 * @param startIndex
	 *            startIndex:从0开始
	 * @param cnt
	 *            数量
	 */
	public List<Map<String, Object>> queryNcsDescriptorByPage(
			Map<String, String> condition, long startIndex, long cnt) {
		log.info("进入方法：queryNcsDescriptorByPage。condition=" + condition
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (condition == null || condition.isEmpty()) {
			log.info("未传入查询条件");
		}
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}
		
		String where = buildNcsDescriptorWhere(condition);
		String fields="RNO_NCS_DESC_ID,to_char(start_time,'yyyy-mm-dd HH24:mi:ss') as START_TIME,SEGTIME,RELSSN,ABSS,NUMFREQ,RECTIME,NET_TYPE,VENDOR,AREA_ID,CREATE_TIME,MOD_TIME,STATUS,NAME,BSC,FREQ_SECTION,RECORD_COUNT,FILE_FORMAT,RID,TERM_REASON,ECNOABSS,RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5,NCELLTYPE,NUCELLTYPE,TFDDMRR,NUMUMFI,TNCCPERM_INDICATOR,TNCCPERM_BITMAP,TMBCR,INTERFER_MATRIX_ID,CITY_ID";
		final String sql = "select * "
				+ "from (select * "
				+ " from (select "+fields+", row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from rno_ncs_descriptor t " + where
				+ " order by CREATE_TIME DESC) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ " where rownum <=" + cnt;
		log.info("查询ncs descriptor的sql：" + sql);
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.addEntity(RnoNcsDescriptor.class);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});

		log.info("退出方法：queryNcsDescriptorByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 构建ncs descriptor的查询条件
	 * 
	 * @param condition
	 * @author brightming 2014-1-17 下午12:27:44
	 */
	@Deprecated
	private String buildNcsDescriptorWhere(Map<String, String> condition) {
		String where = "";
		String v = "";
		long lv;
		String cityCond = "";
		boolean hasAreaId = false;
		where+=" STATUS<>'D' ";//D:表示被删除。N：表示正常的但是未计算；H：表示已经计算过结构指标。
		if (condition != null && condition.size() > 0) {
			//改造开始
			if (condition.get("manufacturer")!=null) {
				
				String vendor=condition.get("manufacturer").toString();
				if ("ERICSSON".equals(vendor)) {
					where+=" and VENDOR='"+vendor+"' ";
				}
				if ("HUAWEI".equals(vendor)) {
					where+=" and VENDOR='"+vendor+"' ";			
				}
				/*if ("ALL".equals(vendor)) {
				//不过滤处理		
			}*/
			}
			//改造结束
			for (String k : condition.keySet()) {
				v = condition.get(k);
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("areaId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							hasAreaId = true;
							where += (where.length() == 0 ? " " : " and ")
									+ "area_id=" + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ("bsc".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "BSC like '%" + v + "%'";
				} else if ("filename".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "NAME like '%" + v + "%'";
				} else if ("FREQSECTION".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "FREQ_SECTION = '" + v + "'";
				}

				else if ("begTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "START_TIME>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("endTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "START_TIME<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							cityCond = " ( area_id in (select area_id from sys_area where parent_id="
									+ lv
									+ " ) or (area_id=-1 and CITY_ID="
									+ lv + ") )";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (!hasAreaId && !"".equals(cityCond)) {
			// 没有具体指定某个区，但是有市级别信息
			where += (where.length() == 0 ? " " : " and ") + cityCond;
		}
		if (where.length() > 0) {
			where = " where " + where;
		}
		return where;
	}

	/**
	 * 计算符合某条件的ncs的数量
	 * 
	 * @param condition
	 * @return
	 * @author brightming 2014-1-17 下午12:19:20
	 */
	public long getNcsDescriptorCount(Map<String, String> condition) {
		String where = buildNcsDescriptorWhere(condition);
		final String sql = "select count(RNO_NCS_DESC_ID) from rno_ncs_descriptor "
				+ where;
		log.info("getNcsDescriptorCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getNcsDescriptorCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询ncs cell信息
	 * @param ncsId
	 * @param startIndex
	 *            从0开始
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:14:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsCellDataByPage(final long ncsId,
			long startIndex, long cnt) {
		log.info("进入queryNcsCellDataByPage(final long ncsId," + ncsId
				+ " long startIndex," + startIndex + " long cnt)" + cnt);
		final String sql = "select * "
				+ "from (select * "
				+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from RNO_NCS_CELL_DATA t where RNO_NCS_DESC_ID=?) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ " where rownum <= " + cnt;
		log.info("分页查询ncscell data 的查询语句：" + sql);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, ncsId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出queryNcsCellDataByPage celllist:"
								+ query.list());
						return query.list();
					}
				});
	}

	/**
	 * 
	 * @title 计算ncs下的cell data 数量
	 * @param ncsId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:15:31
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNcsCellCount(final long ncsId) {
		log.info("进入getNcsCellCount(final long ncsId)" + ncsId);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				// TODO Auto-generated method stub
				String sql = "SELECT count(*) from RNO_NCS_CELL_DATA WHERE RNO_NCS_DESC_ID=?";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, ncsId);
				List resList = query.list();
				long cellcount = 0;
				if (resList != null) {
					cellcount = Long.parseLong(resList.get(0).toString());
				}
				log.info("退出getNcsCellCount cellcount：" + cellcount);
				return cellcount;
			}
		});

	}

	/**
	 * 
	 * @title 分页查询ncs ncell信息
	 * @param ncsId
	 * @param startIndex
	 *            从0开始
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:17:20
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsNcellDataByPage(final long ncsId,
			long startIndex, long cnt) {
		log.info("进入queryNcsNcellDataByPage(final long ncsId," + ncsId
				+ " long startIndex," + startIndex + " long cnt)" + cnt);
		final String sql = "select * "
				+ "from (select * "
				+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from RNO_NCS t where RNO_NCS_DESC_ID=?) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ " where rownum <= " + cnt;
		log.info("分页查询ncscell data 的查询语句：" + sql);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, ncsId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出queryNcsNcellDataByPage ncelllist:"
								+ query.list());
						return query.list();
					}
				});

	}

	/**
	 * 
	 * @title 计算ncs下的ncell data 的数量
	 * @param ncsId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:18:06
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNcsNcellDataCount(final long ncsId) {
		log.info("进入getNcsNcellDataCount(final long ncsId)" + ncsId);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				// TODO Auto-generated method stub
				String sql = "SELECT count(*) from RNO_NCS WHERE RNO_NCS_DESC_ID=?";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, ncsId);
				List resList = query.list();
				long ncellcount = 0;
				if (resList != null) {
					ncellcount = Long.parseLong(resList.get(0).toString());
				}
				log.info("退出getNcsNcellDataCount ncellcount：" + ncellcount);
				return ncellcount;
			}
		});
	}

	/**
	 * 分页查询cluster信息
	 * 
	 * @param ncsId
	 *            startIndex:从0开始
	 * @param cnt
	 *            数量
	 */
	public List<Map<String, Object>> getNcsClusterByPage(long ncsId,
			long startIndex, long cnt) {
		log.info("进入方法：getNcsClusterByPage。ncsId=" + ncsId + ",startIndex="
				+ startIndex + ",cnt=" + cnt);

		final String sql = "select * "
				+ "from (select * "
				+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from RNO_NCS_CLUSTER t where RNO_NCS_DESC_ID=" + ncsId
				+ " order by weight,CONTROL_FACTOR desc nulls last) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ " where rownum <=" + cnt;
		log.info("getNcsClusterByPage.sql=" + sql);
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});
		log.info("退出方法：getNcsClusterByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 计算ncs下的cluster的数量
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-1-17 下午12:20:51
	 */
	public long getNcsClusterCount(long ncsId) {
		log.info("进入方法：getNcsClusterCount。ncsId=" + ncsId);
		final String sql = "select count(ID) from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID="
				+ ncsId;
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getNcsClusterCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 查看cluster下的小区
	 * 
	 * @param clusterId
	 */
	public List<Map<String, Object>> getNcsClusterCell(long clusterId) {
		log.info("进入方法：getNcsClusterCell。clusterId=" + clusterId);
		final String sql = "select * from RNO_NCS_CLUSTER_CELL where CLUSTER_ID="
				+ clusterId;
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});
		log.info("退出方法：getNcsClusterCell。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 
	 * @title 分页查询ncs 干扰矩阵信息
	 * @param ncsId
	 * @param startIndex
	 *            从0开始
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:19:20
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsInterferMatrixByPage(
			final long ncsId, long startIndex, long cnt) {
		log.info("进入queryNcsInterferMatrixByPage(final long ncsId," + ncsId
				+ " long startIndex," + startIndex + " long cnt)" + cnt);
		final String sql = "select * "
				+ "from (select * "
				+ " from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from (SELECT * from RNO_INTERFERENCE WHERE DESCRIPTOR_ID IN(SELECT INTER_DESC_ID from RNO_INTERFERENCE_DESCRIPTOR WHERE RNO_NCS_DESC_ID="
				+ ncsId + ")) t) p " + " where p.\"row_number\" > "
				+ startIndex + ") q " + " where rownum <= " + cnt;
		log.info("分页查询ncscell data 的查询语句：" + sql);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						// query.setLong(0, ncsId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出queryNcsInterferMatrixByPage matrixlist:"
								+ query.list());
						return query.list();
					}
				});
	}

	/**
	 * 
	 * @title 计算ncs下的干扰矩阵的数量
	 * @param ncsId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-17下午06:20:02
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNcsInterferMatrixCount(final long ncsId) {
		log.info("进入getNcsInterferMatrixCount(final long ncsId)" + ncsId);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				// TODO Auto-generated method stub
				String sql = "SELECT count(*) from RNO_INTERFERENCE WHERE DESCRIPTOR_ID IN(SELECT INTER_DESC_ID from RNO_INTERFERENCE_DESCRIPTOR WHERE RNO_NCS_DESC_ID=?)";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setLong(0, ncsId);
				List resList = query.list();
				long matrixcount = 0;
				if (resList != null) {
					matrixcount = Long.parseLong(resList.get(0).toString());
				}
				log.info("退出getNcsInterferMatrixCount matrixcount："
						+ matrixcount);
				return matrixcount;
			}
		});
	}

	/**
	 * 统计区域破坏系数
	 * 
	 * @param ncsIds
	 *            统计的数据范围
	 * @return
	 * @author brightming 2014-1-19 下午1:12:29
	 */
	@Deprecated
	public List<Map<String, Object>> queryAreaDamageFactor(List<Long> ncsIds) {
		log.info("进入方法: queryAreaDamageFactor.ncsIds=" + ncsIds);
		if (ncsIds == null || ncsIds.isEmpty()) {
			log.error("dao方法：queryAreaDamageFactor 。传入参数ncsIds为空！");
			return Collections.EMPTY_LIST;
		}

		String ncsStr = RnoHelper.getStrValFromList(ncsIds);
		if ("".equals(ncsStr)) {
			log.error("dao方法：queryAreaDamageFactor 。传入参数ncsIds不包含有效的信息！");
			return Collections.EMPTY_LIST;
		}
		ncsStr = " ( " + ncsStr + " ) ";
		final String sql = "SELECT mid7.area_id,mid7.area_name ,sum(mid7.tot) as val from "
				+ "(select mid1.*,mid6.area_id ,MID6.area_name  from "
				+ "(select  clust.RNO_NCS_DESC_ID,sum(clust.weight) as tot from rno_ncs_cluster clust "
				+ " where clust.RNO_NCS_DESC_ID in  "
				+ ncsStr
				+ " group by clust.RNO_NCS_DESC_ID )mid1 "
				+ " inner join "
				+ " (  select desp.RNO_NCS_DESC_ID,desp.area_id ,SYS_AREA.name as area_name from RNO_NCS_DESCRIPTOR desp inner join SYS_AREA "
				+ " on ( desp.RNO_NCS_DESC_ID in "
				+ ncsStr
				+ " AND DESP.area_id=SYS_AREA.area_id ) "
				+ " )mid6 on "
				+ " (  mid1.RNO_NCS_DESC_ID=mid6.RNO_NCS_DESC_ID )"
				+ " )mid7 group by area_id,area_name";
		log.info("queryAreaDamageFactor的sql=" + sql);
//		System.out.println("sql=" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出queryAreaDamageFactor damage factor:"
								+ query.list());
						return query.list();
					}
				});
	}

	/**
	 * 计算区域归一化干扰水平
	 * 
	 * @param ncsIds
	 *            统计的数据范围
	 * @return
	 * @author brightming 2014-1-19 下午1:12:05
	 */
	@Deprecated
	public List<Map<String, Object>> queryAreaNormalizeFactor(List<Long> ncsIds) {
		log.info("进入方法: queryAreaNormalizeFactor.ncsIds=" + ncsIds);
		if (ncsIds == null || ncsIds.isEmpty()) {
			log.error("dao方法：queryAreaNormalizeFactor 。传入参数ncsIds为空！");
			return Collections.EMPTY_LIST;
		}

		String ncsStr = RnoHelper.getStrValFromList(ncsIds);
		if ("".equals(ncsStr)) {
			log.error("dao方法：queryAreaNormalizeFactor 。传入参数ncsIds不包含有效的信息！");
			return Collections.EMPTY_LIST;
		}
		ncsStr = " ( " + ncsStr + " ) ";
		final String sql = "select SYS_AREA.name as area_name,mid8.* from "
				+ " ( "
				+ " select mid7.area_id,sum(normalize) as val from  "
				+ " ( "
				+ " select mid1.*,mid6.area_id,mid6.cnt,  "
				+ " (case when cnt>0 then (tot/cnt) else null end) as normalize   "
				+ " from    "
				+ " (  "
				+ " select  clust.RNO_NCS_DESC_ID,sum(clust.weight) as tot from rno_ncs_cluster clust "
				+ " where clust.RNO_NCS_DESC_ID in "
				+ ncsStr
				+ " group by clust.RNO_NCS_DESC_ID "
				+ " )mid1 "
				+ " inner join "
				+ " ( "
				+ " select desp.RNO_NCS_DESC_ID,desp.area_id, MID5.cnt "
				+ " from RNO_NCS_DESCRIPTOR desp inner join (select area_id ,count(label) as cnt from cell group by area_id)mid5 "
				+ " on( "
				+ "       desp.RNO_NCS_DESC_ID in "
				+ ncsStr
				+ " and  "
				+ "      desp.area_id=mid5.area_id "
				+ "    ) "
				+ " )mid6 "
				+ " on "
				+ " ( "
				+ "    mid1.RNO_NCS_DESC_ID=mid6.RNO_NCS_DESC_ID "
				+ " ) "
				+ " )mid7 "
				+ " group by area_id "
				+ " )mid8 "
				+ " inner join SYS_AREA on(mid8.area_id=SYS_AREA.area_id)";
		log.info("queryAreaNornalizeFactor的sql=" + sql);
		System.out.println("sql=" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						log.info("退出queryAreaNormalizeFactor normalize factor:"
								+ query.list());
						return query.list();
					}
				});
	}

	/**
	 * 
	 * @title 查询连通簇内的小区信息并输出彼此区间干扰值
	 * @param clusterId
	 * @param cell
	 * @param ncell
	 * @return
	 * @author chao.xj
	 * @date 2014-1-20下午12:25:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*
	 * public Double getNcsClusterCellAndOutputEachOtherInterValue(final long
	 * clusterId,final String cell,final String ncell){
	 * log.info("进入getNcsCellInterferValue(final long clusterId)"+clusterId);
	 * return hibernateTemplate.execute(new HibernateCallback<Double>(){
	 * 
	 * public Double doInHibernate(Session arg0) throws HibernateException,
	 * SQLException { // TODO Auto-generated method stub String sql=
	 * "SELECT sum(interfer) from (SELECT * from RNO_NCS WHERE RNO_NCS_DESC_ID in (SELECT rno_ncs_desc_id from RNO_NCS_CLUSTER WHERE id="
	 * +
	 * clusterId+")) WHERE CELL='"+cell+"' and ncell='"+ncell+"' OR CELL='"+ncell
	 * +"' and ncell='"+cell+"' GROUP BY CELL,ncell"; SQLQuery
	 * query=arg0.createSQLQuery(sql); log.info("sql:"+sql); //query.setLong(0,
	 * clusterId); List resList=query.list(); double interferval=0; if
	 * (resList!=null && resList.size()!=0) {
	 * log.info("resList.get(0).toString():"+resList.get(0).toString());
	 * interferval=Double.parseDouble(resList.get(0).toString().substring(0,
	 * 7));
	 * 
	 * } log.info("退出getNcsCellInterferValue interferval："+interferval); return
	 * interferval; } });
	 * 
	 * }
	 */
	/**
	 * 
	 * @title 查询连通簇内的小区信息并输出彼此区间干扰值
	 * @param clusterId
	 * @param cell
	 * @param ncell
	 * @return
	 * @author chao.xj
	 * @date 2014-1-20下午12:25:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getNcsClusterCellAndOutputEachOtherInterValue(
			final long clusterId, List<Map<String, Object>> clusterCells) {
		log.info("进入getNcsClusterCellAndOutputEachOtherInterValue(final long clusterId)"
				+ clusterId + " clusterCells:" + clusterCells);
		String wheresql = "";
		for (int i = 0; i < clusterCells.size(); i++) {
			String cell = "'" + clusterCells.get(i).get("CELL").toString()
					+ "'";
			String ncell = "";
			for (int j = 0; j < clusterCells.size(); j++) {
				if (j == i) {
					continue;
				} else {
					if (clusterCells.size() != 2
							&& j != clusterCells.size() - 1) {
						ncell += "'"
								+ clusterCells.get(j).get("CELL").toString()
								+ "',";
					} else {
						ncell += "'"
								+ clusterCells.get(j).get("CELL").toString()
								+ "'";
					}
				}
			}
			if (",".equals(ncell.substring(ncell.length() - 1))) {
				// System.out.println(i);
				ncell = ncell.substring(0, ncell.length() - 1);
				log.info("cell:" + cell + " ncell:" + ncell);
				if (i != clusterCells.size() - 1) {
					wheresql += " (cell=" + cell + " and ncell in (" + ncell
							+ ")) or ";
				} else {
					wheresql += " (cell=" + cell + " and ncell in (" + ncell
							+ "))";
				}
			} else {
				log.info("cell:" + cell + " ncell:" + ncell);
				if (i != clusterCells.size() - 1) {
					wheresql += " (cell=" + cell + " and ncell in (" + ncell
							+ ")) or ";
				} else {
					wheresql += " (cell=" + cell + " and ncell in (" + ncell
							+ "))";
				}
			}
		}
		final String sql = "SELECT cell,ncell,sum(interfer) INTERFER from (SELECT * from RNO_NCS WHERE RNO_NCS_DESC_ID in (SELECT rno_ncs_desc_id from RNO_NCS_CLUSTER WHERE id="
				+ clusterId + ")) WHERE " + wheresql + " GROUP BY CELL,ncell";
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						SQLQuery query = arg0.createSQLQuery(sql);
						log.info("sql:" + sql);
						// query.setLong(0, clusterId);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List resList = query.list();

						log.info("退出getNcsClusterCellAndOutputEachOtherInterValue resList："
								+ resList);
						return resList;
					}
				});

	}

	/**
	 * 搜索包含指定bsc的ncs列表
	 * 
	 * @param bsc
	 * @return
	 * @author brightming 2014-2-11 下午1:52:42
	 */
	public List<Map<String, Object>> searchNcsContainCell(String bsc,
			long cityId, String manufacturers) {
		log.info("进入方法：searchNcsContainCell。bsc=" + bsc + ",cityId=" + cityId
				+ ",manufacturers=" + manufacturers);
		
		String sqlStr = "";
		//获取以当天为准的前三十天的日期
		Date d = new Date(); 
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(d);
		int eMonth=calendar.get(Calendar.MONTH)+1;
		long eTime=calendar.getTimeInMillis();
		calendar.add(Calendar.DATE,-30); 
		int sMonth=calendar.get(Calendar.MONTH)+1;
		long sTime=calendar.getTimeInMillis();
		Date d2 = calendar.getTime();
		String eDate = dateUtil.format_yyyyMMddHHmmss(new Date());
		String sDate = dateUtil.format_yyyyMMdd(d2);
		//测试
		//sDate = "2012-12-12";
		
		//
		List<Map<String, Object>> listMaps=new ArrayList<Map<String,Object>>();
		Map<String, Object> map= null;
		//厂家为爱立信
		if(("1").equals(manufacturers)) {
			 
			sqlStr += " select '1' as manufacturers, " +
				"     rno_2g_eri_ncs_desc_id as ncs_id, " +
				"        to_char(mea_time, 'yyyy-MM-dd hh24:mi:ss') as mea_time, " +
				"        rid, " +
				"        (case when relss_sign = '0' then '' else '-' end) || relss as relss, " +
				"        (case when relss2_sign = '0' then ''else '-' end) || relss2 as relss2, " +
				"        (case when relss3_sign = '0' then ''else '-' end) || relss3 as relss3, " +
				"        (case when relss4_sign = '0' then ''else '-' end) || relss4 as relss4, " +
				"        (case when relss5_sign = '0' then ''else '-' end) || relss5 as relss5, " +
				"        ncelltype, " +
				"        numfreq, " +
				"        rectime, " +
				"        term_reason, " +
				"        ecnoabss, " +
				"        nucelltype, " +
				"        tfddmrr, " +
				"        numumfi, " +
				"        record_count " +
				"   from rno_2g_eri_ncs_descriptor " +
				"  where bsc = '" + bsc + "' " +
//				"  rno_2g_eri_ncs_desc_id in " +
//				"        (select DISTINCT (rno_2g_eri_ncs_desc_id) " +
//				"          from rno_2g_eri_ncs " +
//				"         where city_id = " + cityId +
//				"           and cell = '"+cell+"' " +
//				"           and mea_time >= to_date('"+sDate+"','yyyy-MM-dd') " +
//				"           and mea_time <= to_date('"+eDate+"','yyyy-MM-dd HH24:MI:SS')) " +
				"    and status = 'N' " +
				"    and mea_time >= to_date('"+sDate+"','yyyy-MM-dd') " +
				"    and mea_time <= to_date('"+eDate+"','yyyy-MM-dd HH24:MI:SS') " +
				"    and city_id = " + cityId +
				"  order by mea_time ";
			
		} 
		//厂家为华为
		else if(("2").equals(manufacturers)) {
			System.out.println("华为");
			sqlStr += " select '2' as manufacturers, "+
				"      hwdesc.id as ncs_id, "+
				"      to_char(mea_time, 'yyyy-MM-dd hh24:mi:ss') as mea_time, "+
				"      '' as rid, " +
				"      '' as relss, " +
				"      '' as relss2, " +
				"      '' as relss3, " +
				"      '' as relss4, " +
				"      '' as relss5, " +
				"      '' as ncelltype, " +
				"      '' as numfreq, " +
				"      '' as rectime, " +
				"      '' as term_reason, " +
				"      '' as ecnoabss, " +
				"      '' as nucelltype, " +
				"      '' as tfddmrr, " +
				"      '' as numumfi, " +
				"      '' as record_count " +
				"  from rno_2g_hw_ncs_desc hwdesc "+
				"  where bsc = '" + bsc + "' " +
//				" hwdesc.id in "+
//				"       (select DISTINCT (ncs.rno_2ghwncs_desc_id) "+
//				"          from rno_2g_hw_ncs ncs "+
//				"         where ncs.city_id = "+cityId+
//				"           and ncs.cell = '"+cell+"' "+
//				"           and ncs.mea_time >= to_date('"+sDate+"','yyyy-MM-dd') "+
//				"           and ncs.mea_time <= to_date('"+eDate+"','yyyy-MM-dd HH24:MI:SS')) "+
				"   and hwdesc.status = 'N' "+
				"   and hwdesc.city_id = "+cityId+
				"   and hwdesc.mea_time >= to_date('"+sDate+"','yyyy-MM-dd') "+
				"	and hwdesc.mea_time <= to_date('"+eDate+"','yyyy-MM-dd HH24:MI:SS') "+
				" order by mea_time ";
		} else {
			log.error("bsc="+bsc+",缺失厂家信息");
			return Collections.emptyList();
		}
		
		final String sql = sqlStr;
		System.out.println("sql:"+sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						log.info("ncs指标查看中，获取指定bsc的日期信息sql:" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}
	
	/**
	 * 判断在指定的bsc和日期中是否存在ncs数据
	 * @return
	 * @author peng.jm
	 * @date 2014-12-2上午10:29:02
	 */
	public boolean isNcsDataExistedByDateAndBsc(String cell, String ncsId,
			long cityId, String manuf) {
		String sqlStr = "";
		//厂家为爱立信
		if(("1").equals(manuf)) {
			sqlStr += " SELECT count(*)"+
				" 	  from rno_2g_eri_ncs "+
				" 	 where rno_2g_eri_ncs_desc_id = "+ncsId+
				" 	   and cell = '"+cell+"' "+
				" 	   and city_id = "+ cityId +
				" 	   and ((reparfcn >= 1000 and reparfcn < 5000 and ci_divider/reparfcn <=0.5)" +
				"				 or (reparfcn >= 5000)) ";
		} 
		//厂家为华为
		else if(("2").equals(manuf)) {
			sqlStr += "SELECT count(*) "+
				"   from rno_2g_hw_ncs "+
				"  where rno_2ghwncs_desc_id = "+ncsId+
				"    and cell = '"+cell+"' "+
				"    and city_id = "+ cityId+
				"    and ((s3013 >= 1000 and s3013 < 5000 and (s361-s369)/s3013 <=0.5) " +
				" 				or (s3013 >= 5000)) "; 
		} else {
			log.error("小区"+cell+"所属BSC缺失厂家信息");
			return false;
		}
		final String sql = sqlStr;
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				log.info("验证小区某个时间点的ncs信息是否存在。sql:" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = -1;
				boolean flag = false;
				if(res != null) {
					cnt = res.longValue();
				}
				if(cnt > 0) {
					flag = true;
				}
				return flag;
			}
		});
	}

	/**
	 * 
	 * @title 搜索包含指定小区的ncs 的时间列表 hbase
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-12-9下午6:06:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> searchNcsContainCell(G2NcsQueryCond cond) {
		log.info("进入方法：searchNcsContainCell。G2NcsQueryCond=" + cond);
		String manufacturers=cond.getVendor();
		String cell=cond.getCell();
		long cityId=cond.getCityId();
		String sqlStr = "";
		//获取以当天为准的前三十天的日期
		Date d = new Date(); 
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(d);
		//测试值
//		calendar.setTime(dateUtil.to_yyyyMMddDate("2014-10-1"));
		int endYear = calendar.get(Calendar.YEAR);
		int eMonth=calendar.get(Calendar.MONTH)+1;
		long eTime=calendar.getTimeInMillis();
		calendar.add(Calendar.DATE,-30); 
		int begYear = calendar.get(Calendar.YEAR);
		int sMonth=calendar.get(Calendar.MONTH)+1;
		long sTime=calendar.getTimeInMillis();
		Date d2 = calendar.getTime();
		String eDate = dateUtil.format_yyyyMMddHHmmss(new Date());
		String sDate = dateUtil.format_yyyyMMdd(d2);
		//测试
//		sDate = "2014-9-12";
		
		//
		List<Map<String, Object>> listMaps=new ArrayList<Map<String,Object>>();
		Map<String, Object> map= null;
		//厂家为爱立信
		if(!("1").equals(manufacturers)&&!("2").equals(manufacturers)) {
		
			log.error("小区"+cell+"所属BSC缺失厂家信息");
			return Collections.emptyList();
		}
		HTable hTable=null;
		try {
			hTable=new HTable(HadoopXml.getHbaseConfig(),NcsIndex.LTE4G_CELL_MEATIME_T.str);
//			System.out.println(cityId+"_"+"NCS"+"_"+begYear+"_"+cell);
//			System.out.println(cityId+"_"+"NCS"+"_"+endYear+"_"+cell);
			Get get=new Get((cityId+"_"+"NCS"+"_"+begYear+"_"+("2".equals(manufacturers)?UnicodeUtil.enUnicode(cell):cell)).getBytes());
	        Result result = hTable.get(get); 
//	        System.out.println("result:"+result);
	        List<Result> list = new ArrayList<Result>(); 
	        NavigableMap<byte[], byte[]> snavigableMap=null;
	        NavigableMap<byte[], byte[]> enavigableMap=null;
	        NavigableMap<byte[], byte[]> navigableMap=null;
	        if(sMonth==eMonth){
	        	snavigableMap=result.getFamilyMap(String.valueOf(sMonth).getBytes());
	        }else{
	        	snavigableMap=result.getFamilyMap(String.valueOf(sMonth).getBytes());
	        	if(begYear==endYear){
	        		enavigableMap=result.getFamilyMap(String.valueOf(eMonth).getBytes());
	        	}else{
//	        		System.out.println(cityId+"_"+"NCS"+"_"+endYear+"_"+("2".equals(manufacturers)?UnicodeUtil.enUnicode(cell):cell));
	        		get=new Get((cityId+"_"+"NCS"+"_"+endYear+"_"+("2".equals(manufacturers)?UnicodeUtil.enUnicode(cell):cell)).getBytes());
	    	        result = hTable.get(get); 
	    	        enavigableMap=result.getFamilyMap(String.valueOf(eMonth).getBytes());
	        	}
//	        	System.out.println(snavigableMap+"---"+enavigableMap);
	        	/*if(snavigableMap.isEmpty()||!enavigableMap.isEmpty()){
	        		navigableMap=enavigableMap;
	        	}*/
	        	if(snavigableMap==null&&enavigableMap!=null){
	        		navigableMap=enavigableMap;
	        	}
	        	/*if(!snavigableMap.isEmpty()||enavigableMap.isEmpty()){
	        		navigableMap=snavigableMap;
	        	}*/
	        	if(snavigableMap!=null&&enavigableMap==null){
	        		navigableMap=snavigableMap;
	        	}
	        	/*if(!snavigableMap.isEmpty()||!enavigableMap.isEmpty()){
	        		navigableMap=snavigableMap;
	        		navigableMap.putAll(enavigableMap);
	        	}*/
//	        	System.out.println(snavigableMap+":"+snavigableMap!=null);
	        	if(snavigableMap!=null && enavigableMap!=null){
	        		navigableMap=snavigableMap;
	        		navigableMap.putAll(enavigableMap);
	        	}
	        	/*if(snavigableMap.isEmpty()||enavigableMap.isEmpty()){
	        	}*/
	        	/*if(snavigableMap==null&&enavigableMap==null){
	        	}*/
	        }
	        long keyDate=0;
	        if(navigableMap!=null){
	        	
	        	for (byte[] key:navigableMap.keySet()) {
	        		
	        		//将30天以外的日期再次进行排除
	        		keyDate=Long.parseLong(new String(key));
//	        		System.out.println("keyDate:"+keyDate+"----sTime:"+sTime+"----------eTime:"+eTime);
	        		calendar.setTimeInMillis(keyDate);
//        			System.out.println(dateUtil.format_yyyyMMddHHmmss(calendar.getTime()));
	        		if(keyDate<eTime&&keyDate>sTime ){
	        			map= new HashMap<String, Object>();
	        			map.put("MANUFACTURERS", Long.parseLong(manufacturers));
//	    		  map.put("NCS_ID", 2209);
	        			calendar.setTimeInMillis(keyDate);
//	        			System.out.println(dateUtil.format_yyyyMMddHHmmss(calendar.getTime()));
	        			map.put("MEA_TIME", dateUtil.format_yyyyMMddHHmmss(calendar.getTime()));
	        			listMaps.add(map); 
	        		}
	        	}
	        }
	     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				hTable.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 return listMaps;
	}
	/**
	 * 获取指定小区在指定 ncs里的测量信息
	 * 
	 * @param ncsId
	 * @param cell
	 * @return
	 * @author brightming 2014-2-11 下午2:15:13
	 */
	public List<Map<String, Object>> getCellNcsInfo(final long ncsId,
			final String cell, final long cityId, String manufacturers) {
		log.info("进入方法：getCellNcsInfo。ncsId=" + ncsId + ",cell=" + cell
				+ ",cityId=" + cityId + ",manufacturers="+manufacturers);
		
		String sqlStr = "";
		//厂家为爱立信
		if(("1").equals(manufacturers)) {
			sqlStr += " SELECT CELL, "+
				" 	       NCELL, "+
				" 	       ncells, "+
				" 	       BSIC, "+
				"		   ARFCN, "	+
				" 	       sum(REPARFCN) as REPARFCN1, "+
				" 	       sum(TIMES) / sum(REPARFCN) as topsix, "+
				" 	       sum(TIMES1 + TIMES2) / sum(REPARFCN) as toptwo, "+
				" 	       sum(timesrelss2)/ sum(REPARFCN) as cellrate, "+
				" 	       sum(timesabss) / sum(REPARFCN) AS abssrate, "+
				" 	       sum(timesalone) / sum(REPARFCN) AS alonerate, "+
				" 	       min(defined_neighbour) AS defined_neighbour1, "+
				" 	       case when min(distance)=1000000 then 0 else min(distance) end  AS distance1 "+
				" 	  from rno_2g_eri_ncs "+
				" 	 where rno_2g_eri_ncs_desc_id = "+ncsId+
				" 	   and cell = '"+cell+"' "+
				" 	   and city_id = "+ cityId +
				" 	   and ((reparfcn >= 1000 and reparfcn < 5000 and ci_divider/reparfcn <=0.5) or (reparfcn >= 5000)) "+
				" 	 group by CELL, NCELL, ncells, BSIC, ARFCN  "+
				" 	 order by sum(TIMES) / sum(REPARFCN) desc ";
		} 
		//厂家为华为
		else if(("2").equals(manufacturers)) {
			sqlStr += "SELECT CELL, "+
				" 	       NCELL, "+
				"        ncells, "+
				"        BSIC, "+
				"        ARFCN, "+
				"        sum(s3013) as REPARFCN1, "+
				"        sum(s361) / sum(s3013) as topsix, "+
				"        sum(s374 + s375) / sum(s3013) as toptwo, "+
				"        sum(s361 - s366)/ sum(s3013) as cellrate, "+
				"        sum(s372) / sum(s3013) AS abssrate, "+
				"        sum(s374) / sum(s3013) AS alonerate, "+
				"        case when min(distance)=1000000 then 0 else min(distance) end AS distance1 "+
				"   from rno_2g_hw_ncs "+
				"  where rno_2ghwncs_desc_id = "+ncsId+
				"    and cell = '"+cell+"' "+
				"    and city_id = "+ cityId+
				"    and ((s3013 >= 1000 and s3013 < 5000 and (s361-s369)/s3013 <=0.5) or (s3013 >= 5000)) "+
				"  group by CELL, NCELL, ncells, BSIC, ARFCN "+
				"  order by sum(s361) / sum(s3013) desc "; 
		} else {
			log.error("小区"+cell+"所属BSC缺失厂家信息");
			return Collections.emptyList();
		}
		final String sql = sqlStr;
		
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						log.info("ncs指标查看中，获取小区某个时间点的ncs信息sql:" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
		});
	}

	/**
	 * 获取单个ncs的结构指标数据
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-2-14 下午3:33:42
	 */
	public List<Map<String, Object>> getSingleNcsStructAnaRes(long ncsId) {
		log.info("进入方法：getSingleNcsStructAnaRes。ncsId=" + ncsId);

		// final String sql =
		// "select mid1.*,RNO_GET_FREQ_CNT(mid2.tch) as FREQ_CNT FROM (select * from RNO_NCS_CELL_ANA_RESULT where NCS_ID="
		// + ncsId +
		// " order by cell )mid1 left join (select tch,label from cell where area_id in (select area_id from sys_area where parent_id=(select city_id from rno_ncs_descriptor where rno_ncs_desc_id="+ncsId+")))mid2 on (mid1.cell=mid2.label)";
		// final String sql =
		// "select * from RNO_NCS_CELL_ANA_RESULT where NCS_ID=" + ncsId +
		// " order by cell";
		// final String sql =
		// "select rncar.NCS_ID as \"ncsId\", c.LABEL as \"cell\", c.NAME as \"chineseName\", c.LONGITUDE as \"lng\", c.LATITUDE as \"lat\", c.LNGLATS as \"allLngLats\", rncar.FREQ_CNT as \"freqCnt\", rncar.BE_INTERFER as \"beInterfer\", rncar.NET_STRUCT_FACTOR as \"netStructFactor\",rncar.SRC_INTERFER as \"srcInterfer\", rncar.REDUNT_COVER_FACT as \"reduntCoverFact\", rncar.OVERLAP_COVER as \"overlapCover\", rncar.EXPECTED_COVER_DIS as \"expectedCoverDis\", rncar.OVERSHOOTING_FACT as \"overshootingFact\", rncar.DETECT_CNT as \"detectCnt\", rncar.CELL_COVER as \"cellCover\", rncar.EXPECTED_CAPACITY as \"expectedCapacity\", rncar.CAPACITY_DESTROY_FACT as \"capacityDestroyFact\",rncar.CAPACITY_DESTROY as \"capacityDestroy\" from RNO_NCS_CELL_ANA_RESULT rncar inner join CELL c on c.label = rncar.CELL where rncar.NCS_ID = ?";
		// final String sql =
		// "select rncar.NCS_ID, c.LABEL as \"cell\", c.NAME as \"chineseName\", c.LONGITUDE as \"lng\", c.LATITUDE as \"lat\", c.LNGLATS as \"allLngLats\", rncar.FREQ_CNT, rncar.BE_INTERFER, rncar.NET_STRUCT_FACTOR, rncar.SRC_INTERFER, rncar.REDUNT_COVER_FACT, rncar.OVERLAP_COVER, rncar.EXPECTED_COVER_DIS, rncar.OVERSHOOTING_FACT, rncar.DETECT_CNT, rncar.CELL_COVER, rncar.EXPECTED_CAPACITY, rncar.CAPACITY_DESTROY_FACT, rncar.CAPACITY_DESTROY from RNO_NCS_CELL_ANA_RESULT rncar inner join CELL c on c.label = rncar.CELL where rncar.NCS_ID = ?";
		final String sql = " select rncar.NCS_ID, c.LABEL as \"cell\", c.NAME as \"chineseName\", c.LONGITUDE as \"lng\", c.LATITUDE as \"lat\", c.LNGLATS as \"allLngLats\", rncar.FREQ_CNT, rncar.BE_INTERFER  , rncar.NET_STRUCT_FACTOR  , rncar.SRC_INTERFER, rncar.REDUNT_COVER_FACT, rncar.OVERLAP_COVER, rncar.EXPECTED_COVER_DIS, rncar.OVERSHOOTING_FACT, rncar.DETECT_CNT, rncar.CELL_COVER, rncar.EXPECTED_CAPACITY, rncar.CAPACITY_DESTROY_FACT, rncar.CAPACITY_DESTROY from (select * from RNO_NCS_CELL_ANA_RESULT where NCS_ID =?)rncar inner join CELL c on c.label = rncar.CELL";
		final long temp = ncsId;
		log.info("获取单个ncs下的结构指标的sql:" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setLong(0, temp);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 获取单个ncs的cluster信息 包括： ClusterID（簇ID） Count（小区数） Trxs（频点数/载波数） 簇约束因子 簇权重
	 * Sectors（小区列表）
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-2-14 下午6:03:35
	 */
	public List<Map<String, Object>> getSingleNcsClusterListInfo(long ncsId) {
		log.info("进入方法：getSingleNcsClusterListInfo.ncsId=" + ncsId);

		final String sql = "select mid1.*,mid2.control_factor,MID2.weight FROM( select \"CLUSTER_ID\",COUNT(cell) cellcnt ,sum(freq_cnt) as total_freq_cnt ,wm_concat(cell) as sectors from RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select id from rno_ncs_cluster where rno_ncs_desc_id="
				+ ncsId
				+ ") group by CLUSTER_ID "
				+ " )mid1 INNER JOIN ( select id,control_factor,weight from RNO_NCS_CLUSTER where rno_ncs_desc_id="
				+ ncsId + " )mid2 " + " on mid1.cluster_id=MID2.id";
		log.info("获取单个ncs下的簇概要指标的sql:" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 获取单个ncs的簇内小区信息 包括： 簇ID 小区名 小区中文名 小区载频 簇TCH载频数
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-2-14 下午6:04:33
	 */
	public List<Map<String, Object>> getSingleNcsClusterCellListInfo(long ncsId) {
		log.info("进入方法：getSingleNcsClusterCellListInfo.ncsId=" + ncsId);
		final String sql = "select mid3.*,CELL.name FROM (select mid1.*,mid2.total_freq_cnt from "
				+ " (select cluster_id,cell,freq_cnt from RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select id from rno_ncs_cluster where rno_ncs_desc_id="
				+ ncsId
				+ ") order by cluster_id)mid1 "
				+ " inner join ( select \"CLUSTER_ID\",sum(freq_cnt) as total_freq_cnt from RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select id from rno_ncs_cluster where rno_ncs_desc_id="
				+ ncsId
				+ ") group by CLUSTER_ID )mid2 "
				+ " on (mid1.cluster_id=mid2.cluster_id) "
				+ " )mid3 left join cell on mid3.cell=cell.label ";
		log.info("获取单个ncs下的簇内小区情况的sql:" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 获取簇内小区的关联信息 包括： 主小区 小区层 簇编号 簇内小区载波数 干扰小区
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-2-14 下午6:05:21
	 */
	public List<Map<String, Object>> getSingleNcsClusterCellRelationInfo(
			long ncsId) {

		// final String
		// sql="select ncs.* ,mid.cluster_id ,mid.total_freq_cnt from "
		// +" ( select cell,ncell from rno_ncs ncs where RNO_NCS_DESC_ID="+ncsId
		// +" )ncs "
		// +" inner join "
		// +" ( select cluster_id,sum(freq_cnt) as total_freq_cnt, wm_concat(cell) as cells from "
		// +" ( select cluster_id,''''||cell||'''' as cell ,freq_cnt from "
		// +" rno_ncs_cluster_cell where cluster_id in "
		// +" (select id from RNO_NCS_CLUSTER where rno_ncs_desc_id="+ncsId+") "
		// +" ) group by cluster_id )mid on (INSTR(mid.cells,ncs.cell)>0 and instr(mid.cells,ncs.ncell)>0) order BY CLUSTER_ID,cell";
		//
		final String sql = "select mid1.*,mid2.total_freq_cnt FROM "
				+ " (SELECT CLUSTER_ID,CELL,NCELL FROM RNO_NCS_CLUSTER_CELL_RELA WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER where rno_ncs_desc_id="
				+ ncsId
				+ ")"
				+ " )mid1 INNER JOIN "
				+ " ( SELECT CLUSTER_ID,SUM(FREQ_CNT) as total_freq_cnt FROM RNO_NCS_CLUSTER_CELL WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER where rno_ncs_desc_id="
				+ ncsId + ") " + " group by cluster_id )mid2 "
				+ " ON(MID1.cluster_id=MID2.cluster_id) ";
		log.info("获取单个ncs下的簇内小区关联情况的sql:" + sql);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}

	/**
	 * 查询满足条件的ncs汇总任务数量
	 * 
	 * @param account
	 * @param condition
	 * @return
	 * @author brightming 2014-2-18 下午3:40:06
	 */
	public long getNcsTaskCount(String account, Map<String, String> condition) {
		log.info("进入方法：getNcsTaskCount。account=" + account + ",condition="
				+ condition);
		if (condition == null) {
			condition = new HashMap<String, String>();
		}
		condition.put("creator", account);
		String where = buildNcsTaskWhere(condition);
		final String sql = "select count(TASK_ID) from RNO_TASK " + where;
		log.info("getNcsTaskCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getNcsTaskCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页获取满足条件的ncs汇总任务
	 * 
	 * @param account
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author brightming 2014-2-18 下午3:40:32
	 */
	public List<Map<String, Object>> queryNcsTaskByPage(String account,
			Map<String, String> condition, int startIndex, int cnt) {
		log.info("进入方法：queryNcsTaskByPage。account=" + account + ",condition="
				+ condition + ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (condition == null) {
			condition = new HashMap<String, String>();
		}
		condition.put("creator", account);
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}
		String where = buildNcsTaskWhere(condition);
		/*final String sql = "select task.*,task_ncs_count.cnt from (select * from (select *  from (select t.TASK_ID,t.task_name, t.description,to_char(t.start_time,'yyyy-mm-dd hh24:mi:ss') as start_time, to_char(t.complete_time,'yyyy-mm-dd hh24:mi:ss') as complete_time,t.task_going_status,t.result,t.data_level,t.level_name,row_number() OVER(ORDER BY null) AS \"row_number\" from rno_task t "
				+ where
				+ " order by t.start_time desc) p  where p.\"row_number\" >"
				+ startIndex
				+ " ) q  where rownum <="
				+ cnt
				+ ") task left join ( "
				+ " SELECT TASK_ID,COUNT(TASK_ID) as cnt from "
				+ " ( "
				+ " select * from  rno_task_param where param_name='NCSID' "
				+ " )group by task_id "
				+ " )task_ncs_count "
				+ " on (task.task_id=task_ncs_count.task_id)";*/
		//@author chao.xj  2014-7-23 下午4:17:00 改造分页查询sql语句
		final String sql = "select task.*,task_ncs_count.cnt,task_mrr_count.mrrcnt from (select * from (select *  from (select t.TASK_ID,t.task_name, t.description,to_char(t.start_time,'yyyy-mm-dd hh24:mi:ss') as start_time, to_char(t.complete_time,'yyyy-mm-dd hh24:mi:ss') as complete_time,t.task_going_status,t.result,t.data_level,t.level_name,row_number() OVER(ORDER BY null) AS \"row_number\" from rno_task t "
				+ where
				+ " order by t.start_time desc) p  where p.\"row_number\" >"
				+ startIndex
				+ " ) q  where rownum <="
				+ cnt
				+ ") task left join ( "
				+ " SELECT TASK_ID,COUNT(TASK_ID) as cnt from "
				+ " ( "
				+ " select * from  rno_task_param where param_name='NCSID' "
				+ " )group by task_id "
				+ " )task_ncs_count "
				+ " on (task.task_id=task_ncs_count.task_id)"
				+ " left join (SELECT TASK_ID, COUNT(TASK_ID) as mrrcnt "
			    + "  from (select * from rno_task_param where param_name = 'MRRID') "
			    +"    group by task_id) task_mrr_count "
			    +" on (task.task_id = task_mrr_count.task_id) ";
		log.info("查询ncs descriptor的sql：" + sql);
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});

		log.info("退出方法：queryNcsDescriptorByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 构建查询ncs任务的where部分
	 * 
	 * @param condition
	 *            key: creator begTime endTime taskGoingStatus
	 * @return
	 * @author brightming 2014-2-18 下午3:43:10
	 */
	private String buildNcsTaskWhere(Map<String, String> condition) {
		String where = "";
		String v = "";
		if (condition != null && condition.size() > 0) {
			for (String k : condition.keySet()) {
				v = condition.get(k);
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("creator".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "CREATOR='" + v + "'";

				} else if ("taskGoingStatus".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "TASK_GOING_STATUS='" + v + "'";
				} else if ("begTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "START_TIME>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("endTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "START_TIME<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if("taskName".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "TASK_NAME like '%" + v + "%'";
				} else if("taskDesc".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "DESCRIPTION like '%" + v + "%'";
				}
			}
		}

		if (where.length() > 0) {
			where = " where " + where;
		}
		return where;
	}

	/**
	 * 删除指定的ncs数据
	 * 
	 * @param ncsId
	 * @param isPhysicalDeletion
	 *            是否物理删除 true：从数据库中真正删除数据 false：只是标识删除
	 * @return
	 * @author brightming 2014-2-27 下午5:43:58
	 */
	public boolean deleteNcsDataById(final long ncsId,
			boolean isPhysicalDeletion) {

		if (isPhysicalDeletion) {
			log.info("对指定的ncs[" + ncsId + "]进行物理删除。");
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				@Override
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql = "";
					// 先删除这些
					// 簇小区
					sql = "delete from RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select ID from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID="
							+ ncsId + ")";
					log.info("删除ncs=" + ncsId + "对应的簇小区信息。sql=" + sql);
					SQLQuery query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// 干扰矩阵
					sql = "delete from RNO_INTERFERENCE where DESCRIPTOR_ID in (select INTER_DESC_ID from RNO_INTERFERENCE_DESCRIPTOR where rno_ncs_desc_id="
							+ ncsId + ")";
					log.info("删除ncs=" + ncsId + "对应的干扰矩阵信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// 再统一删除这些
					// 未报告测量数据,NCS umts小区测量数据,NCSumts邻区测量数据,
					String tabName[] = { "RNO_NCS_GSM_NOT_REPORT",
							"RNO_NCS_UMTS_CELL_DATA", "RNO_NCS_UMTS_NCELL",
							"RNO_NCS_UMFIS_NOT_REPORT", "RNO_NCS_CELL_DATA",
							"RNO_NCS", "RNO_NCS_CLUSTER",
							"RNO_INTERFERENCE_DESCRIPTOR" };//
					for (String tn : tabName) {
						sql = "delete from " + tn + " where rno_ncs_desc_id="
								+ ncsId;
						log.info("删除ncs=" + ncsId + "对应的" + tn + "信息。sql="
								+ sql);
						query = arg0.createSQLQuery(sql);
						query.executeUpdate();
					}
					// RNO_NCS_CELL_ANA_RESULT,RNO_NCS_PARAMS
					sql = "delete from RNO_NCS_CELL_ANA_RESULT where ncs_id="
							+ ncsId;
					log.info("删除ncs=" + ncsId + "对应的小区分析结果信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();

					sql = "delete from RNO_NCS_PARAMS where ncs_id=" + ncsId;
					log.info("删除ncs=" + ncsId + "对应的参数配置信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// RNO_NCS_DESCRIPTOR
					sql = "delete from RNO_NCS_DESCRIPTOR where RNO_NCS_DESC_ID="
							+ ncsId;
					log.info("删除ncs=" + ncsId + "对应的描述信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();

					return true;
				}

			});
		} else {
			log.info("对指定的ncs[" + ncsId + "]进行标记删除。");
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				@Override
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {

					String sql = "update RNO_NCS_DESCRIPTOR set STATUS='D' where RNO_NCS_DESC_ID="
							+ ncsId;
					SQLQuery query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					return true;
				}
			});
		}
		return true;
	}
	/**
	 * 按逗号分隔的ncs id标记删除ncs记录
	 * @title 
	 * @param ncsId
	 * @param isPhysicalDeletion
	 * 是否物理删除 true：从数据库中真正删除数据 false：只是标识删除
	 * @return
	 * @author chao.xj
	 * @date 2014-3-27上午11:41:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean deleteNcsSelectedDataById(final String ncsIds,
			boolean isPhysicalDeletion) {

		if (isPhysicalDeletion) {
			log.info("对指定的ncs[" + ncsIds + "]进行物理删除。");
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				@Override
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql = "";
					// 先删除这些
					// 簇小区
					sql = "delete from RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select ID from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID in ("
							+ ncsIds + "))";
					log.info("删除ncs=" + ncsIds + "对应的簇小区信息。sql=" + sql);
					SQLQuery query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// 干扰矩阵
					sql = "delete from RNO_INTERFERENCE where DESCRIPTOR_ID in (select INTER_DESC_ID from RNO_INTERFERENCE_DESCRIPTOR where rno_ncs_desc_id in ("
							+ ncsIds + "))";
					log.info("删除ncs=" + ncsIds + "对应的干扰矩阵信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// 再统一删除这些
					// 未报告测量数据,NCS umts小区测量数据,NCSumts邻区测量数据,
					String tabName[] = { "RNO_NCS_GSM_NOT_REPORT",
							"RNO_NCS_UMTS_CELL_DATA", "RNO_NCS_UMTS_NCELL",
							"RNO_NCS_UMFIS_NOT_REPORT", "RNO_NCS_CELL_DATA",
							"RNO_NCS", "RNO_NCS_CLUSTER",
							"RNO_INTERFERENCE_DESCRIPTOR" };//
					for (String tn : tabName) {
						sql = "delete from " + tn + " where rno_ncs_desc_id in ("
								+ ncsIds+")";
						log.info("删除ncs=" + ncsIds + "对应的" + tn + "信息。sql="
								+ sql);
						query = arg0.createSQLQuery(sql);
						query.executeUpdate();
					}
					// RNO_NCS_CELL_ANA_RESULT,RNO_NCS_PARAMS
					sql = "delete from RNO_NCS_CELL_ANA_RESULT where ncs_id in ("
							+ ncsIds+")";
					log.info("删除ncs=" + ncsIds + "对应的小区分析结果信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();

					sql = "delete from RNO_NCS_PARAMS where ncs_id in (" + ncsIds+")";
					log.info("删除ncs=" + ncsIds + "对应的参数配置信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					// RNO_NCS_DESCRIPTOR
					sql = "delete from RNO_NCS_DESCRIPTOR where RNO_NCS_DESC_ID in ("
							+ ncsIds+")";
					log.info("删除ncs=" + ncsIds + "对应的描述信息。sql=" + sql);
					query = arg0.createSQLQuery(sql);
					query.executeUpdate();

					return true;
				}

			});
		} else {
			log.info("对指定的ncs[" + ncsIds + "]进行标记删除。");
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				@Override
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {

					String sql = "update RNO_NCS_DESCRIPTOR set STATUS='D' where RNO_NCS_DESC_ID in ("
							+ ncsIds+")";
					SQLQuery query = arg0.createSQLQuery(sql);
					query.executeUpdate();
					return true;
				}
			});
		}
		return true;
	}
	/**
	 * 获取ncs的描述信息
	 * 
	 * @param ncsId
	 * @return
	 * @author brightming 2014-3-7 上午11:34:13
	 */
	public List<Map<String, Object>> getNcsDescriptorDataById(final long ncsId) {
		return hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {

						String sql = "select * from RNO_NCS_DESCRIPTOR where RNO_NCS_DESC_ID="
								+ ncsId;
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
	}
	
	/**
	 * 更新指定的ncs desc为指定的状态
	 * @param ncsId
	 * @param string
	 * @return
	 * @author brightming
	 * 2014-5-21 下午4:46:16
	 */
	public boolean updateNcsDescStatus(final long ncsId,final String status){
		if(!Arrays.asList("A","N","H","D").contains(status)){
			log.error("指定的状态：["+status+"]无效！");
			return false;
		}
	    return	hibernateTemplate.execute(new HibernateCallback<Boolean>(){
			@Override
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				arg0.createSQLQuery("UPDATE RNO_NCS_DESCRIPTOR SET STATUS='"+status+"' WHERE RNO_NCS_DESC_ID="+ncsId).executeUpdate();
				return true;
			}
			
		});
	}
	
	/**
	 * 查询指定ncs desc的状态
	 * @param ids
	 * @return
	 * @author brightming
	 * 2014-5-21 下午5:22:49
	 */
	public List<Map<String,Object>> queryNcsDescStatus(final String ids){
		if(ids==null || "".equals(ids.trim())){
			return Collections.EMPTY_LIST;
		}
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>(){
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				String idarr=ids;
				if(ids.endsWith(",")){
					idarr=ids.substring(0, ids.length()-1);
				}
				String sql="select RNO_NCS_DESC_ID,STATUS FROM RNO_NCS_DESCRIPTOR WHERE RNO_NCS_DESC_ID IN ("+idarr+")";
				SQLQuery query =arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 通过任务id获取已存在的ncs分析任务
	 * @author peng.jm 
	 * 2014年6月23日17:48:21
	 */
	/*public List<Map<String, Object>> queryOldNcsTaskByTaskId(final long taskId) {

		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>(){
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
			
				String sql="select task.*, task_ncs_count.cnt  from ( " +
						" select t.TASK_ID,  t.task_name,  t.description, " +
								" to_char(t.start_time, 'yyyy-mm-dd hh24:mi:ss') as start_time, " +
								" to_char(t.complete_time,   'yyyy-mm-dd hh24:mi:ss') as complete_time, " +
								" t.task_going_status,  t.result,  t.data_level,  t.level_name " +
						"  from rno_task t " +
						" where  t.task_id = " + taskId + ") task " +
						" left join (SELECT TASK_ID, COUNT(TASK_ID) as cnt " +
						" from (select * from rno_task_param where param_name = 'NCSID') group by task_id) task_ncs_count " +
						" on (task.task_id =  task_ncs_count.task_id)";
				log.info("通过任务id获取已成功完成的ncs分析任务。sql=" + sql);
				SQLQuery query =arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}*/

	/**
	 * 通过NcsIds获取已存在的ncs分析任务
	 * @author peng.jm 
	 */
	public List<Map<String, Object>> queryOldNcsTaskByNcsIds(final String ncsIds) {
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>(){
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
			
				String sql="select task.*, task_ncs_count.cnt  from ( " +
						" select t.TASK_ID,  t.task_name,  t.description, " +
								" to_char(t.start_time, 'yyyy-mm-dd hh24:mi:ss') as start_time, " +
								" to_char(t.complete_time,   'yyyy-mm-dd hh24:mi:ss') as complete_time, " +
								" t.task_going_status,  t.result,  t.data_level,  t.level_name " +
						"  from rno_task t " +
						" where  t.task_id in (select rtn.task_id from rno_task_ncsidlist rtn where rtn.ncsids ='" + ncsIds + "') ) task " +
						" left join (SELECT TASK_ID, COUNT(TASK_ID) as cnt " +
						" from (select * from rno_task_param where param_name = 'NCSID') group by task_id) task_ncs_count " +
						" on (task.task_id =  task_ncs_count.task_id)";
				log.info("通过ncsIds获取已存在的ncs分析任务。sql=" + sql);
				SQLQuery query =arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 通过ncs或mrr的以逗号分割的id字符串查询其最早测量时间与最后测量时间
	 * @param ids
	 * @param ncsOrMrrFlag
	 * @return
	 * @author chao.xj
	 * @date 2014-7-16上午11:35:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryNcsOrMrrTimeSpanByIds(String ids,String ncsOrMrrFlag) {
		log.debug("进入queryNcsOrMrrTimeSpanByIds(String ids,String ncsOrMrrFlag)"+ids+"----"+ncsOrMrrFlag);
		String sqlString="";
		//--2014-8-19 临时解决 in里面太多项的问题--//
		if(StringUtils.isBlank(ids)){
			return Collections.EMPTY_LIST;
		}
		String[] idarr=ids.split(",");
		if(idarr.length==0){
			return Collections.EMPTY_LIST;
		}
		List<Long> idlongarr=new ArrayList<Long>();
		for(String id:idarr){
			try{
			idlongarr.add(Long.parseLong(id));
			}catch(Exception e){
				
			}
		}
		if(idlongarr.size()==0){
			return Collections.EMPTY_LIST;
		}
		Collections.sort(idlongarr);
		if ("ncs".equals(ncsOrMrrFlag)) {
//			sqlString="select  to_char(min(start_time),'yyyy-mm-dd hh24:Mi:ss')||' - '||to_char(max(start_time),'yyyy-mm-dd hh24:Mi:ss') timespan from rno_ncs_descriptor where rno_ncs_desc_id in("+ids+")";
			sqlString="select  to_char(min(start_time),'yyyy-mm-dd hh24:Mi:ss')||' - '||to_char(max(start_time),'yyyy-mm-dd hh24:Mi:ss') timespan from rno_ncs_descriptor where rno_ncs_desc_id >="+idlongarr.get(0)+" and rno_ncs_desc_id<="+idlongarr.get(idlongarr.size()-1);
		}
		if ("mrr".equals(ncsOrMrrFlag)) {
//			sqlString="select  to_char(min(MEA_DATE),'yyyy-mm-dd hh24:Mi:ss')||' - '||to_char(max(MEA_DATE),'yyyy-mm-dd hh24:Mi:ss') timespan from rno_eri_mrr_descriptor where ERI_MRR_DESC_ID in("+ids+")";
			sqlString="select  to_char(min(MEA_DATE),'yyyy-mm-dd hh24:Mi:ss')||' - '||to_char(max(MEA_DATE),'yyyy-mm-dd hh24:Mi:ss') timespan from rno_eri_mrr_descriptor where ERI_MRR_DESC_ID >="+idlongarr.get(0)+" and ERI_MRR_DESC_ID<="+idlongarr.get(idlongarr.size()-1);
		}
		final String sql=sqlString;
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>(){
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				log.info("通过Ids获取ncs或mrr的最早时间与最晚时间形成时间跨度。sql=" + sql);
				SQLQuery query =arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				log.debug("退出queryNcsOrMrrTimeSpanByIds(String ids,String ncsOrMrrFlag)"+query.list());
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 计算符合某条件的mrr的数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:36:28
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getMrrDescriptorCount(Map<String, String> condition) {
		String where = buildMrrDescriptorWhere(condition);
		final String sql = "select count(ERI_MRR_DESC_ID) from rno_eri_mrr_descriptor "
				+ where;
		log.info("getMrrDescriptorCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrDescriptorCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}
	/**
	 * 
	 * @title 构建mrr descriptor的查询条件
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:39:13
	 * @company 怡创科技
	 * @version 1.2
	 */
	private String buildMrrDescriptorWhere(Map<String, String> condition) {
		String where = "";
		String v = "";
		long lv;
		String cityCond = "";
		boolean hasAreaId = false;
		where +=" 1=1 ";
		if (condition != null && condition.size() > 0) {
			//改造开始
			/*if (condition.get("manufacturer")!=null) {
				
				String vendor=condition.get("manufacturer").toString();
				if ("ERICSSON".equals(vendor)) {
					where+=" and VENDOR='"+vendor+"' ";
				}
				if ("HUAWEI".equals(vendor)) {
					where+=" and VENDOR='"+vendor+"' ";			
				}
				if ("ALL".equals(vendor)) {
				//不过滤处理		
			}
			}*/
			//改造结束
			for (String k : condition.keySet()) {
				v = condition.get(k);
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("areaId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							hasAreaId = true;
							where += (where.length() == 0 ? " " : " and ")
									+ "area_id=" + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ("bsc".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "BSC like '%" + v + "%'";
				} else if ("filename".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "FILE_NAME like '%" + v + "%'";
				}
				else if ("begTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "MEA_DATE>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("endTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ "MEA_DATE<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							cityCond = " ( area_id in (select area_id from sys_area where parent_id="
									+ lv
									+ " ) or (area_id=-1 and CITY_ID="
									+ lv + ") )";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (!hasAreaId && !"".equals(cityCond)) {
			// 没有具体指定某个区，但是有市级别信息
			where += (where.length() == 0 ? " " : " and ") + cityCond;
		}
		if (where.length() > 0) {
			where = " where " + where;
		}
		return where;
	}
	/**
	 * 
	 * @title 分页查询mrr描述信息
	 * @param condition
	 * @param startIndex 从0开始
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午9:45:17
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryMrrDescriptorByPage(
			Map<String, String> condition, long startIndex, long cnt) {
		log.info("进入方法：queryMrrDescriptorByPage。condition=" + condition
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (condition == null || condition.isEmpty()) {
			log.info("未传入查询条件");
		}
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}
		
		String where = buildMrrDescriptorWhere(condition);
		String fields="ERI_MRR_DESC_ID,to_char(MEA_DATE,'yyyy-mm-dd HH24:mi:ss') as MEA_DATE,FILE_NAME,BSC,AREA_ID,CITY_ID";
		/*final String sql = "select * "
				+ "from (select * "
				+ " from (select "+fields+", row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from rno_eri_mrr_descriptor t " + where
				+ " order by MEA_DATE DESC) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ " where rownum <=" + cnt;*/
		//@author chao.xj  2014-7-24 下午5:00:26
		final String sql = "select q.*,t2.NAME CITYNAME,case when t3.name is null then '全部' else t3.name end AREANAME "
				+ "from (select * "
				+ " from (select "+fields+", row_number() OVER(ORDER BY null) AS \"row_number\" "
				+ " from rno_eri_mrr_descriptor t " + where
				+ " order by MEA_DATE DESC) p "
				+ " where p.\"row_number\" > " + startIndex + ") q "
				+ "  left join (select * from sys_area) t2 on(t2.AREA_ID=q.city_id) "
				+ "  left join (select * from sys_area) t3 on(t3.AREA_ID=q.area_id) "
				+ " where rownum <=" + cnt;
		log.info("查询MRR descriptor的sql：" + sql);
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.addEntity(RnoNcsDescriptor.class);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});

		log.info("退出方法：queryMrrDescriptorByPage。返回：" + res.size() + "个记录");
		return res;
	}
	/**
	 * 
	 * @title 删除指定的mrr数据
	 * @param mrrId
	 * @return
	 * @author chao.xj
	 * @date 2014-7-23上午10:29:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean deleteMrrDataById(final long mrrId) {

			log.info("对指定的mrr[" + mrrId + "]进行物理删除。");
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				@Override
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql = "";
					// 先删除这些
					
					// RNO_NCS_DESCRIPTOR
					sql = "delete from rno_eri_mrr_descriptor where ERI_MRR_DESC_ID="
							+ mrrId;
					SQLQuery query = arg0.createSQLQuery(sql);
					log.info("删除mrr=" + mrrId + "对应的描述信息。sql=" + sql);
					query.executeUpdate();

					return true;
				}

			});
		
		return true;
	}


	/**
	 * 获取符合条件的mrr管理总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrAdmCount(long mrrId) {
		final String sql = "select count(ADM_ID) from rno_eri_mrr_adm adm " 
					+ " where adm.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrAdmCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrAdmCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr管理信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrAdmByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrAdmByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		String fields = "FILE_FORMAT,to_char(START_DATE,'yyyy-mm-dd HH24:mi:ss') as START_DATE,RECORD_INFO,RID,TOTAL_TIME,MEASURE_LIMIT,MEASURE_SIGN,MEASURE_INTERVAL,MEASURE_TYPE,MEASURING_LINK,MEASURE_LIMIT2,MEASURE_LIMIT3,MEASURE_LIMIT4,CONNECTION_TYPE,DTM_FILTER";
		final String sql = "select * from (select * from (select " + fields + ", rownum as rn "
                + " from rno_eri_mrr_adm where eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrAdmByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrAdmByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr信号强度总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrStrenCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_STRENGTH stren " 
			+ " where stren.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrStrenCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrStrenCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr信号强度信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrStrenByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrStrenByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select stren.*, rownum as rn "
                + " from RNO_ERI_MRR_STRENGTH stren where stren.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrStrenByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrStrenByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr信号质量总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrQualiCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_QUALITY quali " 
			+ " where quali.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrQualiCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrQualiCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr信号质量信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrQualiByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrQualiByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select quali.*, rownum as rn "
                + " from RNO_ERI_MRR_QUALITY quali where quali.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrQualiByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrQualiByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr传输功率信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPowerCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_POWER power " 
			+ " where power.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrPowerCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrPowerCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr传输功率信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPowerByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrPowerByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select power.*, rownum as rn "
                + " from RNO_ERI_MRR_POWER power where power.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrPowerByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrPowerByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr实时预警信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrTaCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_TA ta " 
			+ " where ta.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrTaCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrTaCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr实时预警信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrTaByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrTaByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select ta.*, rownum as rn "
                + " from RNO_ERI_MRR_TA ta where ta.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrTaByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrTaByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr路径损耗信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPlCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_PL pl " 
			+ " where pl.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrPlCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrPlCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr路径损耗信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPlByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrPlByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select pl.*, rownum as rn "
                + " from RNO_ERI_MRR_PL pl where pl.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrPlByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrPlByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr路径损耗差异信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrPldCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_PLD pld " 
			+ " where pld.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrPldCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrPldCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr路径损耗差异信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrPldByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrPldByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select pld.*, rownum as rn "
                + " from RNO_ERI_MRR_PLD pld where pld.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrPldByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrPldByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId获取mrr的上下行FER信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrFerCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_FER fer " 
			+ " where fer.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrFerCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrFerCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId获取mrr的测量结果信息总数
	 * @param mrrId
	 * @return
	 * @author peng.jm
	 * @date 2014年7月24日15:10:24
	 */
	public long getMrrMeaCount(long mrrId) {
		final String sql = "select count(*) from RNO_ERI_MRR_MEA_RESULTS mea " 
			+ " where mea.eri_mrr_desc_id=" + mrrId;
		log.info("getMrrMeaCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getMrrMeaCount返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 通过mrrId分页获取mrr的上下行FER信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrFerByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrFerByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select fer.*, rownum as rn "
                + " from RNO_ERI_MRR_FER fer where fer.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrFerByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrFerByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过mrrId分页获取mrr测量结果信息
	 * @param mrrId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-7-24下午03:21:35
	 */
	public List<Map<String, Object>> queryMrrMeaByPage(long mrrId,
			int startIndex, int cnt) {
		log.info("进入方法：queryMrrMeaByPage。mrrId=" + mrrId
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		final String sql = "select * from (select * from (select mea.*, rownum as rn "
                + " from RNO_ERI_MRR_MEA_RESULTS mea where mea.eri_mrr_desc_id =" + mrrId + ") "
                + " where rn <= " + (cnt + startIndex) + ") t "
                + " where t.rn > " + startIndex;
		log.info("queryMrrMeaByPage : sql=" + sql);
		
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {

			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryMrrMeaByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 通过区域id获取区域的经纬度范围
	 * @param areaId
	 * @return 
	 * @author peng.jm
	 * @date 2014-7-29上午10:48:33
	 */
	public Map<String, Object> getAreaRangeByAreaId(String areaId) {
		log.info("进入方法：getAreaRangeByAreaId。areaId=" + areaId);
		Map<String, Object> result = new HashMap<String, Object>();
		final String sql = "select * from rno_area_range where area_id =" + areaId;
		log.info("getAreaRangeByAreaId的sql:" + sql);
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
	
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(res.size()>0) {
			result = res.get(0);
		}
		return result;
	}
	/**
	 * 获取符合条件的干扰矩阵数量
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2014-8-15下午04:33:46
	 */
	public long getInterferMartixCount(Map<String, String> condition) {
		String where = buildInterferMartixWhere(condition);
		final String sql = "select count(MARTIX_DESC_ID) from RNO_INTERFER_MARTIX_REC "
				+ where;
		log.info("getInterferMartixCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getInterferMartixCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页获取符合条件的干扰矩阵的详情
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-15下午04:34:06
	 */
	public List<Map<String, Object>> queryInterferMartixByPage(
			Map<String, String> condition, int startIndex, int cnt) {
		log.info("进入方法：queryInterferMartixByPage。condition=" + condition
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (condition == null || condition.isEmpty()) {
			log.info("未传入查询条件");
		}
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}
		
		String where = buildInterferMartixWhere(condition);
		
		String fields = "mid.MARTIX_DESC_ID,to_char(mid.CREATE_DATE,'yyyy-mm-dd HH24:mi:ss') as CREATE_DATE," +
				"to_char(mid.START_MEA_DATE,'yyyy-mm-dd HH24:mi:ss') as START_MEA_DATE," +
				"to_char(mid.END_MEA_DATE,'yyyy-mm-dd HH24:mi:ss') as END_MEA_DATE," +
				"mid.RECORD_NUM,mid.TYPE,mid.WORK_STATUS,mid.FILE_PATH,mid.JOB_ID,mid.STATUS";
	
		long cityId=-1;
		for(String k:condition.keySet()){
			if("cityId".equalsIgnoreCase(k)){
				try{
					cityId=Long.parseLong(condition.get(k).toString());
				}catch(Exception e){
					
				}
				break;
			}
		}
		SysArea sa=AuthDsDataDaoImpl.getSysAreaByAreaId(cityId);
		String name="";
		if(sa!=null){
			name=sa.getName();
		}
		final String sql = "select cast('" + name + "' as varchar2(256)) as AREA_NAME, "+fields+" from ( "
			          + " select * from (select t2.* ,rownum as rn" 
			          + " 				 from (select t.* "
			          + "             			from RNO_INTERFER_MARTIX_REC t " + where
			          + "		 				order by CREATE_DATE desc) t2 )"
			          + " 				where rn <= "+ (cnt + startIndex) 
			          + "				  and rn > "+ startIndex +" )  mid "
			          + "  order by mid.CREATE_DATE desc";
		log.info("分页获取干扰矩阵的sql：" + sql);
		List<Map<String, Object>> res = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.addEntity(RnoNcsDescriptor.class);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}

				});

		log.info("退出方法：queryInterferMartixByPage。返回：" + res.size() + "个记录");
		return res;
	}

	private String buildInterferMartixWhere(Map<String, String> condition) {
		String where = "";
		String v = "";
		long lv;
		where +=" where STATUS = 'Y' ";
		
		if (condition != null && condition.size() > 0) {

			//改造结束
			for (String k : condition.keySet()) {
				v = condition.get(k);
				if (v == null || "".equals(v.trim())) {
					continue;
				}
				if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(v);
						if (lv > 0) {
							where += (where.length() == 0 ? " " : " and ")
									+ "CITY_ID=" + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("interMartixType".equalsIgnoreCase(k)) {
					if(!"ALL".equals(v)) {
						where += (where.length() == 0 ? " " : " and ")
						+ "TYPE = '" + v + "'";
					}
				} 
				else if ("begTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ " CREATE_DATE>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("endTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ " CREATE_DATE<=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				}
			}
		}
		return where;
	}

	/**
	 * 检查这周是否计算过NCS干扰矩阵
	 * @author peng.jm
	 * @date 2014-8-16上午11:01:47
	 */
	public List<Map<String, Object>> checkInterMartixThisWeek(long cityId,
			String thisMonday, String today) {
		log.info("进入方法checkInterMartixThisWeek。areaId=" + cityId +
				",thisMonday=" + thisMonday + ",today=" + today);
		final String sql = " select  to_char(t.create_date, 'yyyy-MM-dd HH24:mi:ss') as CREATE_DATE " +
				" from RNO_INTERFER_MARTIX_REC  t " +
				" where t.CITY_ID = "+ cityId +
				" and t.CREATE_DATE <= to_date('"+ today +"','yyyy-MM-dd HH24:mi:ss') " +
				" and t.CREATE_DATE >= to_date('"+ thisMonday +"','yyyy-MM-dd HH24:mi:ss') " +
				" and t.WORK_STATUS = '计算完成' and t.STATUS = 'Y' " +
				" order by t.create_date desc";
		
		log.info("检查这周是否计算过NCS干扰矩阵: sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 检查是否存在正在计算的干扰矩阵
	 * @param cityId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author peng.jm
	 * @date 2014-8-19下午03:57:13
	 */
	public boolean isCalculatingInterMartixThisArea(long cityId, String thisMonday, String today) {
		log.info("进入方法isCalculatingInterMartixThisArea。areaId=" + cityId
				+ ",thisMonday=" + thisMonday + ",today=" + today);
		
		final String sql = " select count(*) from RNO_INTERFER_MARTIX_REC  t " +
					" where t.CITY_ID = "+ cityId +
					" and t.CREATE_DATE <= to_date('"+ today +"','yyyy-MM-dd HH24:mi:ss') " +
					" and t.CREATE_DATE >= to_date('"+ thisMonday +"','yyyy-MM-dd HH24:mi:ss') " +
					" and t.WORK_STATUS = '正在计算' or t.WORK_STATUS = '排队中'" +
					" and t.STATUS = 'Y'";
		log.info("检查是否存在正在计算的干扰矩阵: sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				boolean result = false;
				if(cnt > 0) {
					result = true;
				}
				log.info("isCalculatingInterMartixThisArea sql=" + sql + ",返回结果：" + result);
				return result;
			}
		});
	}
	/**
	 * 获取符合条件的ncs文件数量
	 * @param condition
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午02:17:20
	 */
	public long getNcsDataCount(Map<String, String> condition) {
		log.info("进入方法getNcsDataCount。condition="+condition);
		
		long cityId = Long.parseLong(condition.get("cityId").toString());
		String startTime = condition.get("begTime").toString();
		String endTime = condition.get("endTime").toString();
		
		final String sql = "   select count(*) from ( "
			+ "     select eri.city_id    as city_id,   "
			+ "                  eri.bsc        as bsc, "
			+ "                  eri.name       as file_name, "
			+ "                  eri.mea_time   as mea_time "
			+ "             from RNO_2G_ERI_NCS_DESCRIPTOR eri "
			+ "            where eri.CITY_ID = " + cityId
			+ "             and eri.MEA_TIME <= "
			+ "                 to_date('" + endTime + "', 'yyyy-MM-dd HH24:mi:ss') "
			+ "              and eri.MEA_TIME >= "
			+ "              to_date('" + startTime + "', 'yyyy-MM-dd HH24:mi:ss') "
			+ "        union all "
			+ "        select rhnd.city_id as city_id, "
			+ "               rhnd.bsc as bsc, "
			+ "               '' as file_name, "
			+ "              rhnd.mea_time as mea_time "
			+ "         from RNO_2G_HW_NCS_DESC rhnd "
			+ "        where rhnd.CITY_ID = " + cityId
			+ "          and rhnd.MEA_TIME <= "
			+ "              to_date('" + endTime + "', 'yyyy-MM-dd HH24:mi:ss') "
			+ "          and rhnd.MEA_TIME >= "
			+ "              to_date('" + startTime + "', 'yyyy-MM-dd HH24:mi:ss')) ";
		
		log.info("getNcsDataCount : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("getNcsDataCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页获取符合条件的ncs记录
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午02:17:40
	 */
	public List<Map<String, Object>> queryNcsDataByPageAndCond(
			Map<String, String> condition, int startIndex, int cnt) {
		log.info("进入方法queryNcsDataByPageAndCond。condition="+condition);
		
		long cityId = Long.parseLong(condition.get("cityId").toString());
		String startTime = condition.get("begTime").toString();
		String endTime = condition.get("endTime").toString();
		
		SysArea sa=AuthDsDataDaoImpl.getSysAreaByAreaId(cityId);
		String name="";
		if(sa!=null){
			name=sa.getName();
		}
		final String sql = "select * from ( select * from ( "
                    + "  select t2.*,rownum as rn from ( "
                    + "         select cast('" + name + "' as varchar2(256)) as name,t1.bsc,t1.file_name,to_char(t1.mea_time,'yyyy-mm-dd HH24:mi:ss') as mea_time from ( "
                    + "                  select eri.city_id    as city_id, "
                    + "                           eri.bsc        as bsc, "
                    + "                           eri.name       as file_name, "
                    + "                          eri.mea_time  as mea_time "
                    + "                     from RNO_2G_ERI_NCS_DESCRIPTOR eri "
                    + "                     where eri.CITY_ID = " + cityId
                    + "                       and eri.MEA_TIME <= "
                    + "                           to_date('" + endTime + "', 'yyyy-MM-dd HH24:mi:ss') "
                    + "                       and eri.MEA_TIME >= "
                    + "                           to_date('" + startTime + "', 'yyyy-MM-dd HH24:mi:ss') "
                    + "                    union all "
                    + "                    select rhnd.city_id as city_id, "
                    + "                           rhnd.bsc as bsc, "
                    + "                           '' as file_name, "
                    + "                           rhnd.mea_time as mea_time "
                    + "                      from RNO_2G_HW_NCS_DESC rhnd "
                    + "                     where rhnd.CITY_ID = " + cityId
                    + "                       and rhnd.MEA_TIME <= "
                    + "                           to_date('" + endTime + "', 'yyyy-MM-dd HH24:mi:ss') "
                    + "                       and rhnd.MEA_TIME >= "
                    + "                           to_date('" + startTime + "', 'yyyy-MM-dd HH24:mi:ss')) t1 "
                    + "             ) t2 ) t3 "
                    + "         where t3.rn <= "+ (cnt + startIndex) +") t4 "
                    + "    where t4.rn > "+ startIndex;

		log.info("queryNcsDataByPageAndCond : sql=" + sql);
	
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		log.info("退出方法：queryNcsDataByPageAndCond。返回：" + res.size() + "个记录");
		return res;
	}
	
	/**
	 * 根据条件获取NCS的数据记录总量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:57:59
	 */
	public long queryNcsDataRecordsCount(long cityId, String startTime,
			String endTime) {
		log.info("进入方法queryNcsDataRecordsCount。cityId="+cityId
				+",startTime="+startTime+",endTime="+endTime);
		final String sql = " select sum(cnt) from( "
				+ " 	select sum(record_count) as cnt "
				+ "		  from rno_2g_eri_ncs_descriptor "
				+ "		 where city_id = " + cityId
				+ "		   and MEA_TIME >= "
				+ "		       to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		   and MEA_TIME <= "
				+ "		       to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		 union all "
				+ "		  select sum(record_count) as cnt "
				+ "		          from rno_2g_hw_ncs_desc "
				+ "		         where city_id = " + cityId
				+ "		           and MEA_TIME >= "
				+ "		               to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		           and MEA_TIME <= "
				+ "		               to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')) ";
		log.info("根据条件获取NCS的数据记录总量 : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("返回结果：" + cnt);
				return cnt;
			}
		});
	}
	
	/**
	 * 获取seq的值
	 * @param string  seq名称
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午06:28:31
	 */
	public long getNextSeqValue(String seq) {
		final String sql = "select "+seq+".NEXTVAL as id from dual";
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long seqNum = res.longValue();
				log.info("getNextSeqValue sql=" + sql + ",返回结果：" + seqNum);
				return seqNum;
			}
		});
	}
	
	/**
	 * 创建NCS干扰矩阵计算任务
	 * @param interMartix
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午06:38:05
	 */
	public boolean createNcsInterMartixRec(Map<String, Object> interMartix) {
		log.info("进入方法createNcsInterMartixDesc。interMartix="+interMartix);

		long martixRecId = Long.parseLong(interMartix.get("martix_rec_id").toString());
		long cityId = Long.parseLong(interMartix.get("city_id").toString());
		String createDate = interMartix.get("create_date").toString();
		String startMeaDate = interMartix.get("start_mea_date").toString();
		String endMeaDate = interMartix.get("end_mea_date").toString();
		long recordNum = Long.parseLong(interMartix.get("record_num").toString());
		String filePath = interMartix.get("file_path").toString();
		String type = interMartix.get("type").toString();
		String workStatus = interMartix.get("work_status").toString();
		long jobId = Long.parseLong(interMartix.get("job_id").toString());
		String status = interMartix.get("status").toString();
		
		final String sql = "insert into RNO_INTERFER_MARTIX_REC (" +
											"MARTIX_DESC_ID," +
											"CITY_ID," +
											"CREATE_DATE," +
											"START_MEA_DATE," +
											"END_MEA_DATE," +
											"RECORD_NUM," +
											"TYPE," +
											"WORK_STATUS," +
											"FILE_PATH," +
											"JOB_ID," +
											"STATUS) " +
					" values(" + martixRecId + "," + cityId 
							+ ", to_date('" + createDate + "', 'yyyy-MM-dd HH24:mi:ss')" 
							+ ", to_date('" + startMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+ ", to_date('" + endMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+", " + recordNum + ",'" + type + "','" + workStatus 
							+ "','" + filePath + "'," + jobId + ",'" + status + "')";
		
		log.info("createNcsInterMartixDesc的sql:"+sql);
		return hibernateTemplate
		.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				int res = query.executeUpdate();
				log.info("退出createNcsInterMartixDesc,受影响行数="+res);
				boolean result = false;
				if (res > 0) {
					result = true;
				} else {
					result = false;
				} 
				return result;
			}
		});
	}
	/**
	 * 更新干扰矩阵的记录状态
	 * @param martixRecId
	 * @param workStatus
	 * @author peng.jm
	 * @date 2014-8-18下午02:57:10
	 */
	public boolean updateInterMartixWorkStatus(long martixRecId, String workStatus) {
		
		final String sql = "update rno_interfer_martix_rec t set t.work_status='"+workStatus+"' " +
					" where t.martix_desc_id=" + martixRecId;
		log.info("updateInterMartixWorkStatus的sql:"+sql);
		return hibernateTemplate
		.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				int res = query.executeUpdate();
				log.info("退出updateInterMartixWorkStatus,受影响行数="+res);
				boolean result = false;
				if (res > 0) {
					result = true;
				} else {
					result = false;
				} 
				return result;
			}
		});
	}
	
	/**
	 * 通过statement更新干扰矩阵的记录状态
	 * @param stmt
	 * @param martixRecId
	 * @param workStatus
	 * @return
	 * @author peng.jm
	 * @date 2014-8-20下午07:13:37
	 */
	public boolean updateInterMatrixWorkStatusByStmt(Statement stmt, long martixRecId, String workStatus) {
		final String sql = "update rno_interfer_martix_rec t set t.work_status='"
				+ workStatus + "' " + " where t.martix_desc_id=" + martixRecId;
		log.info("updateInterMartixWorkStatus的sql:" + sql);
		boolean result = false;
		try {
			int rows = stmt.executeUpdate(sql);
			if (rows > 0) {
				result = true;
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}
	/**
	 * 通过jobId获取干扰矩阵的记录任务信息
	 * @param jobId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-20下午02:17:50
	 */
	public List<Map<String, Object>> queryInterferMatrixRecByJobId(Statement stmt, long jobId) {
		log.info("进入方法queryInterferMatrixRecByJobId。jobId="+jobId);	
		
		String sql = "select MARTIX_DESC_ID,CITY_ID,START_MEA_DATE,END_MEA_DATE,FILE_PATH from RNO_INTERFER_MARTIX_REC where JOB_ID ="+jobId;
		
		log.info("queryInterferMatrixRecByJobId : sql=" + sql);
		
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map map = new HashMap();
				map.put("MARTIX_DESC_ID", rs.getLong(1));
				map.put("CITY_ID", rs.getString(2));
				map.put("START_MEA_DATE", rs.getString(3));
				map.put("END_MEA_DATE", rs.getString(4));
				map.put("FILE_PATH", rs.getString(5));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("退出方法：queryInterferMatrixRecByJobId。返回：" + list.size() + "个记录");
		return list;
	}
	/**
	 * 保存对应jobId的结构分析任务信息
	 * @param jobId
	 * @param dlFileName
	 * @param rdFileName
	 * @param resultDir
	 * @param finishState
	 * @param createTime
	 * @param modTime
	 * @param taskInfo
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午05:26:13
	 */
	public boolean saveStructureAnalysisTask(long jobId, String dlFileName,
			String rdFileName, String resultDir, String finishState,
			String createTime, String modTime, TaskInfo taskInfo) {
		log.info("进入方法saveStructureAnalysisTask。jobId="+jobId+",dlFileName="+dlFileName
				+",rdFileName="+rdFileName+",resultDir="+resultDir+",finishState="+finishState
				+",finishState="+finishState+",modTime="+modTime+",taskInfo="+taskInfo);

		String begMeaTime = taskInfo.getStartTime();
		String endMeaTime = taskInfo.getEndTime();
		long cityId = taskInfo.getCityId();

		final String sql = "insert into rno_strucana_job	(JOB_ID," +
											"BEG_MEA_TIME," +
											"END_MEA_TIME," +
											"CITY_ID," +
											"DL_FILE_NAME," +
											"RD_FILE_NAME," +
											"RESULT_DIR," +
											"FINISH_STATE," +
											"STATUS," +
											"CREATE_TIME," +
											"MOD_TIME)"
						+ "	    values											"
						+ "		  ("+jobId+",											"
						+ "		   to_date('"+begMeaTime+"', 'yyyy-MM-dd HH24:mi:ss'),	"
						+ "		   to_date('"+endMeaTime+"', 'yyyy-MM-dd HH24:mi:ss'),	"
						+ "		   "+cityId+",											"
						+ "		   '"+dlFileName+"',											"
						+ "		   '"+rdFileName+"',											"
						+ "		   '"+resultDir+"',											"
						+ "		   '"+finishState+"',											"
						+ "		   'N',												"
						+ "		   to_date('"+createTime+"', 'yyyy-MM-dd HH24:mi:ss'),"
						+ "		   to_date('"+modTime+"', 'yyyy-MM-dd HH24:mi:ss'))";
		
		log.info("保存结构分析任务信息的sql:"+sql);
		return hibernateTemplate
			.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					SQLQuery query = arg0.createSQLQuery(sql);
					int res = query.executeUpdate();
					log.info("退出saveStructureAnalysisTask,受影响行数="+res);
					boolean result = false;
					if (res > 0) {
						result = true;
					} else {
						result = false;
					} 
					return result;
				}
			});
	}

	/**
	 * 保存对应jobId的门限值信息
	 * @param jobId
	 * @param paramType
	 * @param paramCode
	 * @param paramVal
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午05:26:38
	 */
	public boolean saveThresholdWithJobId(long jobId, String paramType, String paramCode, String paramVal) {
		log.info("进入方法saveThresholdWithJobId。jobId=" + jobId + ",paramType="
				+ paramType + ",paramCode=" + paramCode + ",paramVal=" + paramVal);

		final String sql = "insert into RNO_STRUCANA_JOB_PARAM (JOB_ID," +
											"PARAM_TYPE," +
											"PARAM_CODE," +
											"PARAM_VAL) values("
							+jobId+",'"+paramType+"',"+"'"+paramCode+"',"+"'"+paramVal+"')";
		
		log.info("保存对应jobId的门限值信息的sql:"+sql);
		return hibernateTemplate
			.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					SQLQuery query = arg0.createSQLQuery(sql);
					int res = query.executeUpdate();
					log.info("退出saveThresholdWithJobId,受影响行数="+res);
					boolean result = false;
					if (res > 0) {
						result = true;
					} else {
						result = false;
					} 
					return result;
				}
			});
	}
	/**
	 * 获取结构分析任务的总数
	 * @param condition
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:51:38
	 */
	public long getStructureAnalysisTaskCount(Map<String, String> condition, String account) {
		log.info("进入方法：getStructureAnalysisTaskCount。condition=" + condition + ",account="+account);	
		
		String where = buildStructureAnalysisTaskWhere(condition, account);
		final String sql = "select count(*) "
					+"  from RNO_STRUCANA_JOB sjob " 
					+" left join rno_job job on job.job_id = sjob.job_id  " + where ;
		log.info("获取结构分析任务的总数 : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("获取结构分析任务的总数,返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页获取结构分析任务的信息
	 * @param condition
	 * @param account
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午06:51:59
	 */
	public List<Map<String, Object>> queryStructureAnalysisTaskByPage(
			Map<String, String> condition, String account, int startIndex, int cnt) {
		log.info("进入方法：queryStructureAnalysisTaskByPage。condition=" + condition + ",account="+account
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		
		String where = buildStructureAnalysisTaskWhere(condition, account);
		final String sql = "select *      "
			+"   from (select t1.*,rownum as rn    "
			+"	             from ( select sjob.job_id as job_id,   "
			+"	                       job.job_name as job_name,   "
			+"	                       job.job_running_status as job_running_status,   "
//			+"	                       area.name as city_name,   "
			+"	                       to_char(sjob.beg_mea_time,'yyyy-MM-dd HH24:mi:ss') as beg_mea_time,   "
			+"	                       to_char(sjob.end_mea_time,'yyyy-MM-dd HH24:mi:ss') as end_mea_time,   "
			+"	                       sjob.dl_file_name as dl_file_name,   "
			+"	                       sjob.rd_file_name as rd_file_name,   "
			+"	                       sjob.result_dir as result_dir,   "
			+"	                       to_char(job.launch_time,'yyyy-MM-dd HH24:mi:ss') as launch_time,   "
			+"                         to_char(job.complete_time,'yyyy-MM-dd HH24:mi:ss') as complete_time  "
			+"                      from rno_strucana_job sjob   "
			+"                      left join rno_job job on job.job_id = sjob.job_id   "
//			+"                      left join  sys_area area on area.area_id = sjob.city_id "
									+ where + ") t1   "
			+"             where rownum <= " + (cnt + startIndex) + " ) t2   "
			+"      where t2.rn > " + startIndex;
		
		log.info("分页获取结构分析任务的信息 : sql=" + sql);
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(res!=null && res.size()>0){
			String cityName="";
			for(String k:condition.keySet()){
				if("cityId".equalsIgnoreCase(k)){
					Map<String,Object> area=AuthDsDataDaoImpl.getAreaData(Long.parseLong(condition.get(k).toString()));
					if(area!=null){
						cityName=(String)area.get("NAME");
					}
				}
			}
			for(Map<String, Object> one:res){
				one.put("CITY_NAME", cityName);
			}
		}
		log.info("退出方法：queryStructureAnalysisTaskByPage。返回：" + res.size() + "个记录");
		return res;
	}
	
	private String buildStructureAnalysisTaskWhere(Map<String, String> condition, String account) {
		String where = "";
		String val = "";
		long lv;
		String cityCond = "";

		if(condition.get("isMine") != null) {
			where += " job.creator='"+account+"'";
		}
		if (condition != null && condition.size() > 0) {
			//改造结束
			for (String k : condition.keySet()) {
				val = condition.get(k);
				if (val == null || "".equals(val.trim())) {
					continue;
				}
				if ("cityId".equalsIgnoreCase(k)) {
					try {
						lv = Long.parseLong(val);
						if (lv > 0) {
							where += (where.length() == 0 ? " " : " and ")
									+ "sjob.city_id = " + lv;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ("taskName".equalsIgnoreCase(k)) {
					where += (where.length() == 0 ? " " : " and ")
							+ "job.job_name like '%" + val + "%'";
				} else if ("taskStatus".equalsIgnoreCase(k)) {
					if(!("ALL").equals(val)) {
						if(("LaunchedOrRunning").equals(val)) {
							where += (where.length() == 0 ? " " : " and ")
							+ "(job.job_running_status = 'Launched' " +
									" or job.job_running_status ='Running')";
						} else {
							where += (where.length() == 0 ? " " : " and ")
							+ "job.job_running_status = '" + val + "'";
						}
					}
				} else if ("meaTime".equalsIgnoreCase(k)) {
					Date dt = RnoHelper.parseDateArbitrary(val);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
						  	+ "  sjob.beg_mea_time <= to_date('" + val + "','yyyy-MM-dd') "
						  	+ "	and sjob.end_mea_time >= to_date('" + val + "','yyyy-MM-dd') ";
					} else {
						log.warn("传入无效的时间字符：" + val);
					}
				} else if ("startSubmitTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(val);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
							+ " job.create_time >= to_date('" 
							+ val + "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + val);
					}
				} else if ("endSubmitTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.to_yyyyMMddHHmmssDate(val);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
							+ " job.create_time <= to_date('" 
							+ val + "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + val);
					}
				}
			}
		}
		where = " where " + where + " order by sjob.create_time desc nulls last";
		return where;
	}

	/***
	 * 获取爱立信数据的BSC个数
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:33
	 */
	public List<Map<String, Object>> getEriDataBscNumByDate(long cityId, String startTime, String endTime) {
		final String sql = "select count(bsc) as bsc_num,mea_time from(      "
				+"  select distinct(bsc),to_char(ncs.mea_time, 'yyyy-mm-dd') mea_time   "
				+"        from rno_2g_eri_ncs_descriptor ncs      "
				+"       where ncs.city_id="+cityId
				+"         and ncs.mea_time <=      "
				+"             to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')      "
				+"         and ncs.mea_time >=      "
				+"             to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') )  group by mea_time ";
//				+"      union all      "
//				+"      select mrr.bsc      "
//				+"        from rno_eri_mrr_descriptor mrr      "
//				+"       where mrr.city_id="+cityId
//				+"         and mrr.mea_date <=      "
//				+"             to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')      "
//				+"         and mrr.mea_date >=      "
//				+"             to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss')) )";
		log.info("getEriDataBscNumByOneDay : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信Ncs的文件数量
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:38
	 */
	public List<Map<String, Object>> getEriNcsFileNumByDate(long cityId, String startTime, String endTime) {
		final String sql = "select count(rno_2g_eri_ncs_desc_id) as ncsfile_num,mea_time from(   "
						+"   select ncs.rno_2g_eri_ncs_desc_id, to_char(ncs.mea_time, 'yyyy-mm-dd') as mea_time   "
						+"	  from rno_2g_eri_ncs_descriptor ncs    "
						+"	 where ncs.city_id="+cityId
						+"     and ncs.mea_time <=    "
						+"	       to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')    "
						+"	   and ncs.mea_time >=     "
						+"	       to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss')) group by mea_time";
		log.info("getEriNcsFileNumByOneDay : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信Mrr的文件数量
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:41
	 */
	public List<Map<String, Object>> getEriMrrFileNumByDate(long cityId, String startTime, String endTime) {
		final String sql = "select count(eri_mrr_desc_id) as mrrfile_num,mea_time from(   "
			+"  select mrr.eri_mrr_desc_id,to_char(mrr.mea_date, 'yyyy-mm-dd') as mea_time "
			+"  from rno_eri_mrr_descriptor mrr    "
			+"	 where mrr.city_id="+cityId
			+"     and mrr.mea_date <=    "
			+"	       to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')    "
			+"	   and mrr.mea_date >=     "
			+"	       to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss')) group by mea_time";
		log.info("getEriMrrFileNumByOneDay : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取华为数据的BSC个数
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午03:42:33
	 */
	public List<Map<String, Object>> getHWDataBscNumByDate(long cityId, String startTime, String endTime) {
		final String sql = "select count(bsc) as bsc_num,mea_time from (   "
				+"  select distinct(bsc),to_char(mea_time, 'yyyy-mm-dd') mea_time     "
				+"        from rno_2g_hw_ncs_desc ncs      "
				+"       where ncs.city_id="+cityId
				+"         and ncs.mea_time <=      "
				+"             to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')      "
				+"         and ncs.mea_time >=      "
				+"             to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss') )  group by mea_time  ";
//			+"      union all      "
//			+"      select mrr.bsc      "
//			+"        from rno_2g_hw_mrr_desc mrr      "
//			+"       where mrr.city_id="+cityId
//			+"         and mrr.mea_date <=      "
//			+"             to_date('"+date+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss')      "
//			+"         and mrr.mea_date >=      "
//			+"             to_date('"+date+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss')))";
		log.info("getHWDataBscNumByOneDay : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 通过城市id和日期范围查询爱立信用于结构分析的数据详情
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getEriDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime) {
		log.info("进入方法：getEriDataDetailsByCityIdAndDate。cityId=" + cityId 
				+ ",startTime=" + startTime +",endTime=" + endTime);
		String startDate = startTime.substring(0, startTime.indexOf(" "));
		String endDate = endTime.substring(0, startTime.indexOf(" "));
		String areaIdStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		final String sql = "select to_char(datetime,'yyyy-MM-dd') as datetime, "															
					+ " 	       cell_num, "
					+ "		       ncs_num, "
					+ "		       mrr_num, "
					+ "		       bsc_num, "
					+ "		       ncsfile_num, "
					+ "		       mrrfile_num "
					+ "		  from (select to_date('"+startDate+"', 'yyyy-mm-dd') + rownum - 1 as datetime "
					+ "		          from dual "
					+ "		        connect by level <= 10) "
					+ "     left outer join (select count(*) as cell_num "
					+ "                   from cell "
					+ "                  where cell.area_id in ( "+areaIdStr+" ) "
					+ "                        ) on 1 = 1 "
					+ "     left outer join (select count(cell) as ncs_num, mea_time "
					+ "                   from (select distinct (cell) as cell, "
					+ "                                 to_char(ncs.mea_time, 'yyyy-mm-dd') as mea_time "
					+ "                           from rno_2g_eri_ncs ncs "
					+ "                          where city_id =" + cityId
					+ "                            and mea_time <= "
					+ "                                to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') "
					+ "                            and mea_time >= "
					+ "                                to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')) "
					+ "                  group by mea_time) t1 on t1.mea_time = "
					+ "                                           to_char(datetime, 'yyyy-MM-dd') "
					+ "     left outer join (select count(cell) as mrr_num, mea_time "
					+ "                   from (select distinct (cell_name) as cell, "
					+ "                                         to_char(mea_date, 'yyyy-mm-dd') as mea_time "
					+ "                           from rno_eri_mrr_fer "
					+ "                          where city_id =" + cityId
					+ "                            and mea_date <= "
					+ "                                to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') "
					+ "                            and mea_date >= "
					+ "                                to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')) "
					+ "                  group by mea_time) t2 on t2.mea_time = "
					+ "                                           to_char(datetime, 'yyyy-MM-dd') "
					+ "     left outer join (select count(*) as bsc_num "
					+ "                   from (select distinct (bsc_id) "
					+ "                           from cell "
					+ "                          where cell.area_id in "
					+ "                                ("+areaIdStr+" ))) on 1 = 1 "
					+ "     left outer join (select count(*) as ncsfile_num, mea_time "
					+ "                   from (select rno_2g_eri_ncs_desc_id, "
					+ "                                to_char(mea_time, 'yyyy-mm-dd') as mea_time "
					+ "                           from rno_2g_eri_ncs_descriptor "
					+ "                          where city_id =" + cityId
					+ "                            and mea_time <= "
					+ "                                to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') "
					+ "                            and mea_time >= "
					+ "                                to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')) "
					+ "                  group by mea_time) t3 on t3.mea_time = "
					+ "                                           to_char(datetime, 'yyyy-MM-dd') "
					+ "    left outer join (select count(*) as mrrfile_num, mea_time "
					+ "                   from (select eri_mrr_desc_id, "
					+ "                                to_char(mea_date, 'yyyy-mm-dd') as mea_time "
					+ "                           from rno_eri_mrr_descriptor "
					+ "                          where city_id =" + cityId
					+ "                            and mea_date <= "
					+ "                                to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') "
					+ "                            and mea_date >= "
					+ "                                to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss')) "
					+ "                  group by mea_time) t4 on t4.mea_time = "
					+ "                                           to_char(datetime, 'yyyy-MM-dd') "
					+ "	 where datetime between "
					+ "	       to_date('"+startDate+"', 'yyyy-MM-dd') and "
					+ "	       to_date('"+endDate+"', 'yyyy-MM-dd')  "
					+ "	 order by datetime asc        ";
		
		log.info("查询爱立信用于结构分析的数据详情的sql:" + sql);
		
		return hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 通过城市id和日期范围查询华为用于结构分析的数据详情
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-26下午02:52:34
	 */
	public List<Map<String, Object>> getHWDataDetailsByCityIdAndDate(
			long cityId, String startTime, String endTime) {
		log.info("进入方法：getHWDataDetailsByCityIdAndDate。cityId=" + cityId 
				+ ",startTime=" + startTime +",endTime=" + endTime);
		String startDate = startTime.substring(0, startTime.indexOf(" "));
		String endDate = endTime.substring(0, startTime.indexOf(" "));
		String areaIdStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		final String sql = "select to_char(datetime, 'yyyy-MM-dd') as datetime,   	"
			+ "         	  cell_num,   	 "
			+ "	              ncs_cell_num,   	"
			+ "	              mrr_cell_num,   	"
			+ "	              bsc_num   	"
			+ "	         from (select to_date('"+startDate+"', 'yyyy-mm-dd') + rownum - 1 as datetime   	"
			+ "	                 from dual   	"
			+ "	               connect by level <= 10)   	"
			+ "	         left outer join (select count(*) as cell_num   	"
			+ "	                            from cell   	"
			+ "	                           where cell.area_id in   	"
			+ "	                                 ("+areaIdStr+")) on 1 = 1   	"
			+ "	         left outer join (select count(cell) as ncs_cell_num, mea_time   	"
			+ "	                            from (select distinct (cell) as cell,    	"
			+ "	                                         to_char(mea_time, 'yyyy-mm-dd') as mea_time   	"
			+ "	                                    from rno_2g_hw_ncs ncs   	"
			+ "	                                   where city_id =" + cityId
			+ "	                                     and mea_time <=   	"
			+ "	                                         to_date('"+endTime+"',   	"
			+ "	                                                 'yyyy-MM-dd HH24:mi:ss')   	"
			+ "	                                     and mea_time >=   	"
			+ "	                                         to_date('"+startTime+"',   	"
			+ "	                                                 'yyyy-MM-dd HH24:mi:ss'))   	"
			+ "	                           group by mea_time) t1    	"
			+ "	                    on t1.mea_time = to_char(datetime, 'yyyy-MM-dd')   	"
			+ "	         left outer join (select count(*) as mrr_cell_num, mea_time from(   	"
			+ "	                              select distinct (cell),to_char(mea_date, 'yyyy-mm-dd') as mea_time   	"
			+ "	                                from (select cell, mea_date   	"
			+ "	                                        from rno_2g_hw_mrr_frate   	"
			+ "	                                       where city_id =" + cityId
			+ "	                                         and mea_date <=   	"
			+ "	                                             to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')   	"
			+ "	                                         and mea_date >=   	"
			+ "	                                             to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss')   	"
			+ "	                                      union all   	"
			+ "	                                      select cell, mea_date   	"
			+ "	                                        from rno_2g_hw_mrr_hrate   	"
			+ "	                                       where city_id =" + cityId
			+ "	                                         and mea_date <=   	"
			+ "	                                             to_date('"+endTime+"', 'yyyy-MM-dd HH24:mi:ss')   	"
			+ "	                                         and mea_date >=   	"
			+ "	                                             to_date('"+startTime+"', 'yyyy-MM-dd HH24:mi:ss'))   	"
			+ "	                             ) group by mea_time) t2    	"
			+ "	                   on t2.mea_time = to_char(datetime, 'yyyy-MM-dd')   	"
			+ "	         left outer join (select count(*) as bsc_num   	"
			+ "	                            from (select distinct (bsc_id)   	"
			+ "	                                    from cell   	"
			+ "	                                   where cell.area_id in   	"
			+ "	                                         ("+areaIdStr+"))) on 1 = 1   	"
			+ "	        where datetime between   	"
			+ "	              to_date('"+startDate+"', 'yyyy-MM-dd') and   	"
			+ "	              to_date('"+endDate+"', 'yyyy-MM-dd')   	"
			+ "	        order by datetime asc";
		
		log.info("查询华为用于结构分析的数据详情的sql:" + sql);
		
		return hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 通过jobId查询对应的结果分析信息
	 * @param jobId
	 * @return
	 * @author peng.jm
	 * @date 2014-8-27下午06:19:50
	 */
	public List<Map<String, Object>> getStructureTaskByJobId(long jobId) {
		log.info("进入方法：getStructureTaskByJobId。jobId=" + jobId);

		final String sql = "select * from rno_strucana_job where job_id=" + jobId;
		log.info("getStructureTaskByJobId的sql:" + sql);
		return hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 通过模块类型获取阈值门限对象
	 * @param modType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-15下午4:41:48
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*public Threshold getThresholdByModType(String modType) {
		log.info("进入方法：getThresholdByModType。modType=" + modType);
		Map<String, Object> result = new HashMap<String, Object>();
		final String sql = "select id,order_num,module_type,code,desc_info,to_char(default_val,'FM990.00') DEFAULT_VAL from rno_threshold where module_type='"+modType+"' ";
		log.info("getThresholdByModType的sql:" + sql);
		Threshold res = hibernateTemplate.execute(new HibernateCallback<Threshold>() {
			@Override
			public Threshold doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				//query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list=query.list();
				log.debug("list:"+list.size());
				Threshold threshold=new Threshold();
				for (int i = 0; i < list.size(); i++) {
					Map map=(Map)list.get(i);
					String code=map.get("CODE").toString();
					String val=map.get("DEFAULT_VAL").toString();
					String descInfo=map.get("DESC_INFO").toString();
					
					if("betweencellidealdismultiple".toUpperCase().equals(code)){
						threshold.setBetweenCellIdealDisMultiple(val);
						threshold.setBetweenCellIdealDisMultipleDescInfo(descInfo);
					}
					if("cellCheckTimesIdealDisMultiple".toUpperCase().equals(code)){
						threshold.setCellCheckTimesIdealDisMultiple(val);
						threshold.setCellCheckTimesIdealDisMultipleDescInfo(descInfo);
					}
					if("cellCheckTimesSameFreqInterThreshold".toUpperCase().equals(code)){
						threshold.setCellCheckTimesSameFreqInterThreshold(val);
						threshold.setCellCheckTimesSameFreqInterThresholdDescInfo(descInfo);
					}
					if("cellIdealDisReferenceCellNum".toUpperCase().equals(code)){
						threshold.setCellIdealDisReferenceCellNum(val);
						threshold.setCellIdealDisReferenceCellNumDescInfo(descInfo);
					}
					if("dlCoverMinimumSignalStrengthThreshold".toUpperCase().equals(code)){
						threshold.setDlCoverMinimumSignalStrengthThreshold(val);
						threshold.setDlCoverMinimumSignalStrengthThresholdDescInfo(descInfo);
					}
					if("gsm1800CellFreqNum".toUpperCase().equals(code)){
						threshold.setGsm1800CellFreqNum(val);
						threshold.setGsm1800CellFreqNumDescInfo(descInfo);
					}
					if("gsm1800CellIdealCapacity".toUpperCase().equals(code)){
						threshold.setGsm1800CellIdealCapacity(val);
						threshold.setGsm1800CellIdealCapacityDescInfo(descInfo);
					}
					if("gsm900CellFreqNum".toUpperCase().equals(code)){
						threshold.setGsm900CellFreqNum(val);
						threshold.setGsm900CellFreqNumDescInfo(descInfo);
					}
					if("gsm900CellIdealCapacity".toUpperCase().equals(code)){
						threshold.setGsm900CellIdealCapacity(val);
						threshold.setGsm900CellIdealCapacityDescInfo(descInfo);
					}
					if("interFactorMostDistant".toUpperCase().equals(code)){
						threshold.setInterFactorMostDistant(val);
						threshold.setInterFactorMostDistantDescInfo(descInfo);
					}
					if("interFactorSameAndAdjFreqMinimumThreshold".toUpperCase().equals(code)){
						threshold.setInterFactorSameAndAdjFreqMinimumThreshold(val);
						threshold.setInterFactorSameAndAdjFreqMinimumThresholdDescInfo(descInfo);
					}
					if("overShootingIdealDisMultiple".toUpperCase().equals(code)){
						threshold.setOverShootingIdealDisMultiple(val);
						threshold.setOverShootingIdealDisMultipleDescInfo(descInfo);
					}
					if("sameFreqInterThreshold".toUpperCase().equals(code)){
						threshold.setSameFreqInterThreshold(val);
						threshold.setSameFreqInterThresholdDescInfo(descInfo);
					}
					if("ulCoverMinimumSignalStrengthThreshold".toUpperCase().equals(code)){
						threshold.setUlCoverMinimumSignalStrengthThreshold(val);
						threshold.setUlCoverMinimumSignalStrengthThresholdDescInfo(descInfo);
					}
				}
				return threshold;
			}
		});
		return res;
	}*/
/*	public Threshold getThresholdByModType(String modType) {
		log.info("进入方法：getAreaRangeByAreaId。areaId=" + modType);
		Map<String, Object> result = new HashMap<String, Object>();
		final String sql = "select id,order_num,module_type,code,desc_info,to_char(default_val,'FM990.00') DEFAULT_VAL from rno_threshold where module_type='"+modType+"' ";
		log.info("getAreaRangeByAreaId的sql:" + sql);
		List<Map<String, Object>> list = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
	
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		Threshold threshold=new Threshold();
		System.out.println("list大小："+list.size());
		for (int i = 0; i < list.size(); i++) {
			Map map=list.get(i);
			String descinfo=map.get("CODE").toString();
			String val=map.get("DEFAULT_VAL").toString();
			log.debug("descinfo:"+descinfo+"的值是："+val);
			System.out.println("descinfo:"+descinfo+"的值是："+val);
			System.out.println("betweencellidealdismultiple.toUpperCase():"+"betweencellidealdismultiple".toUpperCase()+"----descinfo:"+descinfo);
			if("betweencellidealdismultiple".toUpperCase().equals(descinfo)){
				threshold.setBetweenCellIdealDisMultiple(val);
				System.out.println("betweencellidealdismultiple"+val);
			}
			if("cellCheckTimesIdealDisMultiple".toUpperCase().equals(descinfo)){
				threshold.setCellCheckTimesIdealDisMultiple(val);
			}
			if("cellCheckTimesSameFreqInterThreshold".toUpperCase().equals(descinfo)){
				threshold.setCellCheckTimesSameFreqInterThreshold(val);
			}
			if("cellIdealDisReferenceCellNum".toUpperCase().equals(descinfo)){
				threshold.setCellIdealDisReferenceCellNum(val);
			}
			if("dlCoverMinimumSignalStrengthThreshold".toUpperCase().equals(descinfo)){
				threshold.setDlCoverMinimumSignalStrengthThreshold(val);
			}
			if("gsm1800CellFreqNum".toUpperCase().equals(descinfo)){
				threshold.setGsm1800CellFreqNum(val);
			}
			if("gsm1800CellIdealCapacity".toUpperCase().equals(descinfo)){
				threshold.setGsm1800CellIdealCapacity(val);
			}
			if("gsm900CellFreqNum".toUpperCase().equals(descinfo)){
				threshold.setGsm900CellFreqNum(val);
			}
			if("gsm900CellIdealCapacity".toUpperCase().equals(descinfo)){
				threshold.setGsm900CellIdealCapacity(val);
			}
			if("interFactorMostDistant".toUpperCase().equals(descinfo)){
				threshold.setInterFactorMostDistant(val);
			}
			if("interFactorSameAndAdjFreqMinimumThreshold".toUpperCase().equals(descinfo)){
				threshold.setInterFactorSameAndAdjFreqMinimumThreshold(val);
			}
			if("overShootingIdealDisMultiple".toUpperCase().equals(descinfo)){
				threshold.setOverShootingIdealDisMultiple(val);
			}
			if("sameFreqInterThreshold".toUpperCase().equals(descinfo)){
				threshold.setSameFreqInterThreshold(val);
			}
			if("ulCoverMinimumSignalStrengthThreshold".toUpperCase().equals(descinfo)){
				threshold.setUlCoverMinimumSignalStrengthThreshold(val);
			}
		}
		return threshold;
	}*/
	/**
	 * 
	 * @title 通过城市ID获取该区域下的系统小区数量
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16上午10:37:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*public int getAreaCellNumByCityId(long cityId) {
		log.info("进入方法：getAreaCellNumByCityId。cityId=" + cityId);
		final String sql =  "select count(distinct label) syscellcount							"
				+"  from cell                                                           "
				+" where area_id in (select t1.area_id                                  "
				+"                     from (select a.*                                 "
				+"                             from sys_area a                          "
				+"                            start with a.area_id in ("+cityId+")              "
				+"                           connect by prior a.area_id = a.parent_id   "
				+"                                  and area_level = '区/县') t1        "
				+"                    where t1.area_level <> '市')                      ";
		log.info("getAreaCellNumByCityId的sql:" + sql);
		int res = hibernateTemplate
			.execute(new HibernateCallback<Integer>() {
	
			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list=query.list();
				int result = 0;
				if(list!=null) {
					result=Integer.parseInt(((Map)list.get(0)).get("COUNT").toString());
				}
				return result;
			}
		});
		log.info("退出方法：getAreaCellNumByCityId。count=" + res);
		return res;
	}*/
	/**
	 * 
	 * @title 获取结构分析汇总信息为某一天
	 * @param cityId
	 * @param meaDate
	 * @param dateType HW 华为  ERI 爱立信
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午2:31:52
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getStructAnaSummaryInfoForOneDay(long cityId,String meaDate,String dateType) {
		log.info("进入方法：getStructAnaSummaryInfoForOneDay。cityId,String meaDate,String dateType=" + cityId+"---"+meaDate+"---"+dateType);
		Map<String, Object> result = new HashMap<String, Object>();
		final String sql = buildSqlForStructAnaOneDay(cityId, meaDate, dateType);
		log.info("getStructAnaSummaryInfoForOneDay的sql:" + sql);
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
	
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(res.size()>0) {
			result = res.get(0);
		}
		return result;
	}
	/**
	 * 
	 * @title 构建sql语句为某一天结构分析汇总信息
	 * @param cityId
	 * @param meaDate
	 * @param dateType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午2:31:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String buildSqlForStructAnaOneDay(long cityId,String meaDate,String dateType) {
		String sql ="";
		if (dateType.equals("ERI")) {
			sql =    "select *																				"			
					+"  from (select count(distinct label) syscellcount                                     "
					+"          from cell                                                                   "
					+"         where area_id in (select t1.area_id                                          "
					+"                             from (select a.*                                         "
					+"                                     from sys_area a                                  "
					+"                                    start with a.area_id in ("+cityId+")                      "
					+"                                   connect by prior a.area_id = a.parent_id           "
					+"                                          and area_level = '区/县') t1                "
					+"                            where t1.area_level <> '市')                              "
					+"           and asciistr(label) not like '%\\%'),                                       "
					+"       (select count(distinct cell) ncscellcount                                      "
					+"          from rno_ncs                                                                "
					+"         where rno_ncs_desc_id in                                                     "
					+"               (select rno_ncs_desc_id                                                "
					+"                  from rno_ncs_descriptor                                             "
					+"                 where to_char(start_time, 'yyyy-MM-dd') = '"+meaDate+"'               "
					+"                   and city_id = "+cityId+")),                                                "
					+"       (select count(distinct bsc_id) ncsofbsccount                                   "
					+"          from cell                                                                   "
					+"         where label in (select distinct cell                                         "
					+"                           from rno_ncs                                               "
					+"                          where rno_ncs_desc_id in                                    "
					+"                                (select rno_ncs_desc_id                               "
					+"                                   from rno_ncs_descriptor                            "
					+"                                  where to_char(start_time, 'yyyy-MM-dd') =           "
					+"                                        '"+meaDate+"'                                  "
					+"                                    and city_id = "+cityId+"))),                              "
					+"       (select count(distinct name) ncsfilecount                                      "
					+"          from rno_ncs_descriptor                                                     "
					+"         where to_char(start_time, 'yyyy-MM-dd') = '"+meaDate+"'                       "
					+"           and city_id = "+cityId+"),                                                         "
					+"       (select count(distinct file_name) mrrfilecount                                 "
					+"          from rno_eri_mrr_descriptor                                                 "
					+"         where to_char(mea_date, 'yyyy-MM-dd') = '"+meaDate+"'                         "
					+"           and city_id = "+cityId+"),                                                         "
					+"       (select count(distinct cell_name) mrrcellcount                                 "
					+"          from rno_eri_mrr_quality                                                    "
					+"         where eri_mrr_desc_id in                                                     "
					+"               (select eri_mrr_desc_id                                                "
					+"                  from rno_eri_mrr_descriptor                                         "
					+"                 where to_char(mea_date, 'yyyy-MM-dd') = '"+meaDate+"'                 "
					+"                   and city_id = "+cityId+")),                                                "
					+"       (select count(distinct bsc_id) mrrofbsccount                                   "
					+"          from cell                                                                   "
					+"         where bsc_id is not null                                                     "
					+"           and label in                                                               "
					+"               (select distinct cell_name                                             "
					+"                  from rno_eri_mrr_quality                                            "
					+"                 where eri_mrr_desc_id in                                             "
					+"                       (select eri_mrr_desc_id                                        "
					+"                          from rno_eri_mrr_descriptor                                 "
					+"                         where to_char(mea_date, 'yyyy-MM-dd') = '"+meaDate+"'         "
					+"                           and city_id = "+cityId+"))),                                       "
					+"       (select count(distinct bsc_id) sysofbsccount                                   "
					+"          from cell                                                                   "
					+"         where bsc_id is not null                                                     "
					+"           and area_id in (select t1.area_id                                          "
					+"                             from (select a.*                                         "
					+"                                     from sys_area a                                  "
					+"                                    start with a.area_id in ("+cityId+")                      "
					+"                                   connect by prior a.area_id = a.parent_id           "
					+"                                          and area_level = '区/县') t1                "
					+"                            where t1.area_level <> '市')                              "
					+"           and asciistr(label) not like '%\\%')                                        ";
		}else if(dateType.equals("HW")){
			sql= "select *																				"
					+"  from                                                                                "
					+"  (select count(distinct label) syscellcount                                          "
					+"          from cell                                                                   "
					+"         where area_id in (select t1.area_id                                          "
					+"                             from (select a.*                                         "
					+"                                     from sys_area a                                  "
					+"                                    start with a.area_id in ("+cityId+")                      "
					+"                                   connect by prior a.area_id = a.parent_id           "
					+"                                          and area_level = '区/县') t1                "
					+"                            where t1.area_level <> '市')                              "
					+"           and asciistr(label)  like '%\\%'),                                          "
					+"  (select count(distinct cell) ncscellcount                                           "
					+"          from rno_2g_hw_ncs                                                          "
					+"         where rno_2ghwncs_desc_id in                                                 "
					+"               (select id                                                             "
					+"                  from rno_2g_hw_ncs_desc                                             "
					+"                 where to_char(mea_time, 'yyyy-MM-dd') = '"+meaDate+"'                 "
					+"                   and city_id = "+cityId+")) ,                                               "
					+"       (select count(distinct bsc_id) ncsofbsccount                                   "
					+"          from cell                                                                   "
					+"         where label in (select distinct cell                                         "
					+"                           from rno_2g_hw_ncs                                         "
					+"                          where rno_2ghwncs_desc_id in                                "
					+"                                (select id                                            "
					+"                  from rno_2g_hw_ncs_desc                                             "
					+"                 where to_char(mea_time, 'yyyy-MM-dd') = '"+meaDate+"'                 "
					+"                   and city_id = "+cityId+"))),                                               "
					+"       (select count(distinct cell) mrrcellcount                                      "
					+"          from rno_2g_hw_mrr_frate                                                    "
					+"         where mrr_desc_id in                                                         "
					+"               (select mrr_desc_id                                                    "
					+"                  from rno_2g_hw_mrr_desc                                             "
					+"                 where to_char(mea_date, 'yyyy-MM-dd') = '"+meaDate+"'                 "
					+"                   and city_id = "+cityId+")),                                               "
					+"                   (select count(mrr_desc_id) mrrofbsccount                           "
					+"                  from rno_2g_hw_mrr_desc                                             "
					+"                 where to_char(mea_date, 'yyyy-MM-dd') = '"+meaDate+"'                 "
					+"                   and city_id = "+cityId+"),                                                "
					+"                   (select count(distinct bsc_id) sysofbsccount                       "
					+"          from cell                                                                   "
					+"         where bsc_id is not null                                                     "
					+"           and area_id in (select t1.area_id                                          "
					+"                             from (select a.*                                         "
					+"                                     from sys_area a                                  "
					+"                                    start with a.area_id in ("+cityId+")                      "
					+"                                   connect by prior a.area_id = a.parent_id           "
					+"                                          and area_level = '区/县') t1                "
					+"                            where t1.area_level <> '市')                              "
					+"           and asciistr(label)  like '%\\%')                                           ";
		}
		
		return sql;
	}
	/**
	 * 
	 * @title 获取结构分析汇总信息为某时间段
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param dateType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午5:28:53
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> getStructAnaSummaryInfoForTimeRange(long cityId,String startDate,String endDate,String dateType) {
		log.info("进入方法：getStructAnaSummaryInfoForTimeRange。cityId,String startDate,String endDate,String dateType=" + cityId+"---"+startDate+"---"+endDate+"---"+dateType);
		Map<String, Object> result = new HashMap<String, Object>();
		final String sql = buildSqlForStructAnaTimeRange(cityId, startDate, endDate, dateType);
		log.info("getStructAnaSummaryInfoForTimeRange的sql:" + sql);
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
	
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(res.size()>0) {
			result = res.get(0);
		}
		return result;
	}
	/**
	 * 
	 * @title 构建sql语句为某时间段结构分析汇总信息
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param dateType
	 * @return
	 * @author chao.xj
	 * @date 2014-8-16下午4:42:20
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String buildSqlForStructAnaTimeRange(long cityId,String startDate,String endDate,String dateType){
		String sql="";
		if (dateType.equals("ERI")) {
			 sql="select *																				"
					 +"  from (select count(distinct label) syscellcount                                     "
					 +"          from cell                                                                   "
					 +"         where area_id in (select t1.area_id                                          "
					 +"                             from (select a.*                                         "
					 +"                                     from sys_area a                                  "
					 +"                                    start with a.area_id in ("+cityId+")                      "
					 +"                                   connect by prior a.area_id = a.parent_id           "
					 +"                                          and area_level = '区/县') t1                "
					 +"                            where t1.area_level <> '市')                              "
					 +"           and asciistr(label) not like '%\\%'),                                       "
					 +"       (select count(distinct cell) ncscellcount                                      "
					 +"          from rno_ncs                                                                "
					 +"         where rno_ncs_desc_id in (select rno_ncs_desc_id                             "
					 +"                                     from rno_ncs_descriptor                          "
					 +"                                    where to_char(start_time, 'yyyy-MM-dd') between   "
					 +"                                          '"+startDate+"' and '"+endDate+"'               "
					 +"                                      and city_id = "+cityId+")),                             "
					 +"       (select count(distinct cell_name) mrrcellcount                                 "
					 +"          from rno_eri_mrr_quality                                                    "
					 +"         where eri_mrr_desc_id in (select eri_mrr_desc_id                             "
					 +"                                     from rno_eri_mrr_descriptor                      "
					 +"                                    where to_char(mea_date, 'yyyy-MM-dd') between     "
					 +"                                          '"+startDate+"' and '"+endDate+"'               "
					 +"                                      and city_id = "+cityId+"))                              ";
		}else if(dateType.equals("HW")){
				sql= " select *																			"			
						+"   from (select count(distinct label) syscellcount                                "
						+"           from cell                                                              "
						+"          where area_id in (select t1.area_id                                     "
						+"                              from (select a.*                                    "
						+"                                      from sys_area a                             "
						+"                                     start with a.area_id in ("+cityId+")                 "
						+"                                    connect by prior a.area_id = a.parent_id      "
						+"                                           and area_level = '区/县') t1           "
						+"                             where t1.area_level <> '市')                         "
						+"            and asciistr(label) like '%\\%'),                                      "
						+"        (select count(distinct cell) ncscellcount                                 "
						+"           from rno_2g_hw_ncs                                                     "
						+"          where rno_2ghwncs_desc_id in                                            "
						+"                (select id                                                        "
						+"                   from rno_2g_hw_ncs_desc                                        "
						+"                  where to_char(mea_time, 'yyyy-MM-dd') between '"+startDate+"' and  "
						+"                        '"+endDate+"'                                              "
						+"                    and city_id = "+cityId+")),                                           "
						+"        (select count(distinct cell) mrrcellcount                                 "
						+"           from rno_2g_hw_mrr_frate                                               "
						+"          where mrr_desc_id in (select mrr_desc_id                                "
						+"                                  from rno_2g_hw_mrr_desc                         "
						+"                                 where to_char(mea_date, 'yyyy-MM-dd') between    "
						+"                                       '"+startDate+"' and '"+endDate+"'              "
						+"                                   and city_id = "+cityId+"))                            ";
		}
		return sql;
	}
	/**
 * 
 * @title 通过模块类型获取阈值门限对象集合
 * @param moduleType
 * @return
 * @author chao.xj
 * @date 2014-8-28上午10:52:09
 * @company 怡创科技
 * @version 1.2
 */
/*public List<RnoThreshold> getThresholdsByModuleType(String moduleType) {
	log.info("进入方法：getThresholds。moduleType=" + moduleType
			);
	final String sql = "select * from rno_threshold where module_type='"+moduleType+"'";
	List<RnoThreshold> res = hibernateTemplate
			.execute(new HibernateCallback<List<RnoThreshold>>() {

				@Override
				public List<RnoThreshold> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					SQLQuery query = arg0.createSQLQuery(sql);
					query.addEntity(RnoThreshold.class);
					return query.list();
				}

			});

	log.info("退出方法：getThresholds。返回：" + res.size() + "个记录");
	return res;
}*/
/**
 * 
 * @title 通过模块类型获取阈值门限对象集合
 * @param moduleType
 * @return
 * @author chao.xj
 * @date 2014-8-31上午11:54:03
 * @company 怡创科技
 * @version 1.2
 */
public List<RnoThreshold> getThresholdsByModuleType(
		final String moduleType) {
	log.info("进入方法：getThresholdsByModuleType。moduleType=" + moduleType
			);
	return hibernateTemplate
			.executeFind(new HibernateCallback<List<RnoThreshold>>() {
				public List<RnoThreshold> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql= "select * from rno_threshold where module_type='"+moduleType+"' order by order_num";
					
					log.debug("getThresholdsByModuleType ,sql=" + sql);
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List<Map<String, Object>> rows = query.list();
					List<RnoThreshold> result = new ArrayList<RnoThreshold>();
					if (rows != null && rows.size() > 0) {
						RnoThreshold rec;
						DateUtil dateUtil=new DateUtil();
						for (Map<String, Object> row : rows) {
							rec = RnoHelper.commonInjection(
									RnoThreshold.class, row,dateUtil);
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
 * 
 * @title 获取指定小区在指定 ncs里的测量信息
 * @param cond
 * @return
 * @author chao.xj
 * @date 2014-12-8下午5:06:02
 * @company 怡创科技
 * @version 1.2
 */
public List<Map<String, Object>> getCellNcsInfo(G2NcsQueryCond cond) {
	log.info("进入方法：getCellNcsInfo。G2NcsQueryCond=" + cond);
	
	List<Map<String, Object>> listMaps=new ArrayList<Map<String,Object>>();
	Map<String, Object> map=null;
	NavigableMap<byte[], byte[]> naviMap=null;
	String manufacturers=cond.getVendor();
	String cell=cond.getCell();
	String ncsDateTime=cond.getNcsDateTime();
	long cityId=cond.getCityId();
	Calendar calendar=Calendar.getInstance(); 
	calendar.setTime(dateUtil.parseDateArbitrary(ncsDateTime));
	String startRow=cityId+"_"+calendar.getTimeInMillis()+"_"+("2".equals(manufacturers)?UnicodeUtil.enUnicode(cell):cell);
	String stopRow=startRow+"~";
	long reparfcn;
	long ci_divider;
	
	String ncell="";
	List<Map<String, Object>> ncellList=getNcellInfoByCell(cond.getCell());
	Map<String, String> ncells=new HashMap<String, String>();
	for (int i = 0; i < ncellList.size(); i++) {
		ncell=ncellList.get(i).get("NCELL").toString();
		ncells.put(ncell, ncell);
	}
	ncell="";
	//厂家为爱立信
	if(("1").equals(manufacturers)) {
//		System.out.println("爱立信小区:"+startRow);
		try {
			HTable hTable=new HTable(HadoopXml.getHbaseConfig(),NcsIndex.ERI_NCS_T.str);
//			Filter filter=new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(startRow));
//			Filter filter=new PrefixFilter(Bytes.toBytes(startRow));
//		    Scan scan=new Scan(startRow.getBytes(),filter);
//		    Scan scan=new Scan(startRow.getBytes());
//		    scan.setFilter(filter);
			Scan scan=new Scan(startRow.getBytes(), stopRow.getBytes());
		    ResultScanner scanner = hTable.getScanner(scan);
//		    int i=0;
		    for (Result r : scanner) {
//		    	i++;
//	            System.out.println(i+"--"+r);
	            naviMap=r.getFamilyMap(NcsIndex.NCS_CF.str.getBytes());
	            
	            reparfcn=Long.valueOf(new String(naviMap.get("REPARFCN".getBytes())));
	            ci_divider=Long.valueOf(new String(naviMap.get("CI_DIVIDER".getBytes())));
	            if(reparfcn<1000 || (reparfcn>=5000 && (ci_divider/reparfcn>0.5))){
	            	continue;
	            }
	            map=new HashMap<String, Object>();
	            ncell=new String(naviMap.get("NCELL".getBytes()));
	            if(ncells.get(ncell)!=null){
	            	map.put("DEFINED_NEIGHBOUR1", "1");	
	            }else{
		            //min(defined_neighbour) AS defined_neighbour1,
					map.put("DEFINED_NEIGHBOUR1", new String(naviMap.get("DEFINED_NEIGHBOUR".getBytes())));	            	
	            }
				map.put("CELL", new String(naviMap.get("CELL".getBytes())));
				map.put("NCELL", ncell);
				map.put("NCELLS", new String(naviMap.get("NCELLS".getBytes())));
				map.put("BSIC", new String(naviMap.get("BSIC".getBytes())));
				map.put("ARFCN", new String(naviMap.get("ARFCN".getBytes())));
				//sum(REPARFCN) as REPARFCN1,
				map.put("REPARFCN1", new String(naviMap.get("REPARFCN".getBytes())));
				//sum(TIMES) / sum(REPARFCN) as topsix,
				map.put("TOPSIX", Float.parseFloat(new String(naviMap.get("TIMES".getBytes())))/Float.parseFloat(new String(naviMap.get("REPARFCN".getBytes()))));
				//sum(TIMES1 + TIMES2) / sum(REPARFCN) as toptwo,
				map.put("TOPTWO", (Float.parseFloat(new String(naviMap.get("TIMES1".getBytes())))+Float.parseFloat(new String(naviMap.get("TIMES2".getBytes()))))/Float.parseFloat(new String(naviMap.get("REPARFCN".getBytes()))));
				//sum(timesrelss2) / sum(REPARFCN) as cellrate,
				map.put("CELLRATE", Float.parseFloat(new String(naviMap.get("TIMESRELSS2".getBytes())))/Float.parseFloat(new String(naviMap.get("REPARFCN".getBytes()))));
				//sum(timesabss) / sum(REPARFCN) AS abssrate,
				map.put("ABSSRATE", Float.parseFloat(new String(naviMap.get("TIMESABSS".getBytes())))/Float.parseFloat(new String(naviMap.get("REPARFCN".getBytes()))));
				// sum(timesalone) / sum(REPARFCN) AS alonerate,
				map.put("ALONERATE", Float.parseFloat(new String(naviMap.get("TIMESALONE".getBytes())))/Float.parseFloat(new String(naviMap.get("REPARFCN".getBytes()))));
				
				//when min(distance) = 1000000 then 0  else  min(distance)
				map.put("DISTANCE1", Float.parseFloat(new String(naviMap.get("DISTANCE".getBytes())))==1000000?0:Float.parseFloat(new String(naviMap.get("DISTANCE".getBytes()))));
				listMaps.add(map);
				/*for (byte[] key : naviMap.keySet()) {
					System.out.println(new String(key)+"---"+new String(naviMap.get(key)));
					map=new HashMap<String, Object>();
					map.put(new String(key), new String(naviMap.get(key)));
					listMaps.add(map);
				}*/
	        }  
	        scanner.close(); 
	     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	//厂家为华为
	else if(("2").equals(manufacturers)) {
//		System.out.println("华为小区:"+startRow);
		try {
			HTable hTable=new HTable(HadoopXml.getHbaseConfig(),NcsIndex.HW_NCS_T.str);
//			Filter filter=new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(startRow));
			/*Filter filter=new PrefixFilter(Bytes.toBytes(startRow));
//		    Scan scan=new Scan(startRow.getBytes(),filter);
		    Scan scan=new Scan(startRow.getBytes());
		    scan.setFilter(filter);*/
		    Scan scan=new Scan(startRow.getBytes(), stopRow.getBytes());
		    ResultScanner scanner = hTable.getScanner(scan);
//		    int i=0;
		    for (Result r : scanner) {
//		    	i++;
//	            System.out.println(i+"--"+r);
	            naviMap=r.getFamilyMap(NcsIndex.NCS_CF.str.getBytes());
	            
	            reparfcn=Long.valueOf(new String(naviMap.get("S3013".getBytes())));
	            ci_divider=Long.valueOf(new String(naviMap.get("CI_DIVIDER".getBytes())));
	            if(reparfcn<1000 || (reparfcn>=5000 && (ci_divider/reparfcn>0.5))){
	            	continue;
	            }
	            
	            map=new HashMap<String, Object>();
//	            System.out.println(new String(naviMap.get("NCELL".getBytes()),"utf-8"));
	            ncell=new String(naviMap.get("NCELL".getBytes()),"utf-8");
	            if(ncells.get(ncell)!=null){
	            	map.put("DEFINED_NEIGHBOUR1", "1");	
	            }else{
		            //min(defined_neighbour) AS defined_neighbour1,
					map.put("DEFINED_NEIGHBOUR1", "0");	            	
	            }
				map.put("CELL", new String(naviMap.get("CELL".getBytes()),"utf-8"));
				map.put("NCELL", ncell);
				map.put("NCELLS", new String(naviMap.get("NCELLS".getBytes()),"utf-8"));
				map.put("BSIC", new String(naviMap.get("BSIC".getBytes())));
				map.put("ARFCN", new String(naviMap.get("ARFCN".getBytes())));
				//sum(s3013) as REPARFCN1
				map.put("REPARFCN1", new String(naviMap.get("S3013".getBytes())));
				//sum(s361) / sum(s3013) as topsix
				map.put("TOPSIX", Float.parseFloat(new String(naviMap.get("S361".getBytes())))/Float.parseFloat(new String(naviMap.get("S3013".getBytes()))));
				//sum(s374 + s375) / sum(s3013) as toptwo
				map.put("TOPTWO", (naviMap.get("S374".getBytes())==null?0:(Float.parseFloat(new String(naviMap.get("S374".getBytes()))))+(naviMap.get("S375".getBytes())==null?0:(Float.parseFloat(new String(naviMap.get("S375".getBytes()))))))/Float.parseFloat(new String(naviMap.get("S3013".getBytes()))));
				//sum(s361 - s366)/ sum(s3013) as cellrate
				map.put("CELLRATE", (Float.parseFloat(new String(naviMap.get("S361".getBytes())))-Float.parseFloat(new String(naviMap.get("S366".getBytes()))))/Float.parseFloat(new String(naviMap.get("S3013".getBytes()))));
				//sum(s372) / sum(s3013) AS abssrate
				map.put("ABSSRATE", Float.parseFloat(new String(naviMap.get("S372".getBytes())))/Float.parseFloat(new String(naviMap.get("S3013".getBytes()))));
				//sum(s374) / sum(s3013) AS alonerate
				map.put("ALONERATE", (naviMap.get("S374".getBytes())==null?0:(Float.parseFloat(new String(naviMap.get("S374".getBytes())))))/Float.parseFloat(new String(naviMap.get("S3013".getBytes()))));
				//when min(distance) = 1000000 then 0  else  min(distance)
				map.put("DISTANCE1", Float.parseFloat(new String(naviMap.get("DISTANCE".getBytes())))==1000000?0:Float.parseFloat(new String(naviMap.get("DISTANCE".getBytes()))));
				listMaps.add(map);
	        }  
	        scanner.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else {
		log.error("小区"+cell+"所属BSC缺失厂家信息");
		return Collections.emptyList();
	}
	return listMaps;
}
/**
 * 
 * @title ncs指标查看中，获取某小区的邻区信息
 * @param cell
 * @return
 * @author chao.xj
 * @date 2015-3-10上午9:39:27
 * @company 怡创科技
 * @version 2.0.1
 */
public List<Map<String, Object>> getNcellInfoByCell(
		final String cell) {
	log.info("进入方法：getNcellInfoByCell。cell=" + cell);
	
	
	final String sql = "select ncell from rno_ncell where cell = '"+cell+"'";
	
	return hibernateTemplate
			.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
				@Override
				public List<Map<String, Object>> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					log.info("ncs指标查看中，获取某小区的邻区信息sql:" + sql);
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					return query.list();
				}
	});
}
	/**
	 * 
	 * @title 通过区域及起始时间和厂家,及数据类型从Hbase获取MR数据描述记录情况
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @param factory
	 * ERI,ZTE
	 * @param dataType
	 * HO,MR
	 * @return
	 * @author chao.xj
	 * @date 2015-3-27下午4:33:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getDataDescRecordFromHbase(long cityId,
			String startTime, String endTime, String factory,String dataType) {

		DateUtil dateUtil = new DateUtil();
		long sMill = dateUtil.parseDateArbitrary(startTime).getTime();
		String startRow = cityId + "_" + sMill + "_#";
		long eMill = dateUtil.parseDateArbitrary(endTime).getTime();
		String stopRow = cityId + "_" + eMill + "_~";

		String tabName="";
		if(dataType.equals("HO")){
			tabName="LTE4G_HO_DESC";
		}else if(dataType.equals("MR")){
			tabName="LTE4G_MR_DESC";
		}else{
			return Collections.EMPTY_LIST;
		}
		HTable table = null;
		
		List<Map<String, Object>> mapList = new LinkedList<Map<String, Object>>();
		ResultScanner scanner = null;
		try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			table = new HTable(conf, tabName);
			
			// 获取筛选对象
			Scan scan = new Scan();
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(stopRow));
			// 过滤
			SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
					Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"),
					CompareOp.EQUAL, Bytes.toBytes(factory));
			filter1.setFilterIfMissing(true);
			scan.setFilter(filter1);

			// 缓存1000条数据
			scan.setCaching(1000);
			scan.setCacheBlocks(false);
		
			scanner = table.getScanner(scan);
			int i = 0;
			List<byte[]> rowList = new LinkedList<byte[]>();
			// 遍历扫描器对象， 并将需要查询出来的数据row key取出
			for (Result r : scanner) {
				String row = Bytes.toString(r.getRow());
				rowList.add(Bytes.toBytes(row));
			}
			// 获取取出的row key的GET对象
			List<Get> getList = new LinkedList<Get>();
			for (byte[] row : rowList) {
				Get get = new Get(row);
				get.addColumn(Bytes.toBytes("DESCINFO"),
						Bytes.toBytes("MEA_TIME"));
				get.addColumn(Bytes.toBytes("DESCINFO"),
						Bytes.toBytes("RECORD_COUNT"));
				getList.add(get);
			}

			Result[] results = null;
			
			results = table.get(getList);

			// 遍历结果
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));

				Map<String, Object> rmap = new LinkedHashMap<String, Object>();
				for (byte[] key : fmap.keySet()) {
					byte[] value = fmap.get(key);
					rmap.put(Bytes.toString(key), Bytes.toString(value));
				}
				mapList.add(rmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return mapList;
	}
	/**
	 * 
	 * @title 通过jobId获取pci规划记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-3-30上午10:14:31
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryPciPlanJobRecByJobId(Statement stmt, long jobId) {
		log.info("进入方法queryPciPlanJobRecByJobId。jobId="+jobId);	
		
		String sql = "select city_id,beg_mea_time,end_mea_time,result_dir,PLAN_TYPE,CONVER_TYPE,RELA_NUM_TYPE,OPTIMIZE_CELLS,rd_file_name from rno_lte_pci_job where JOB_ID ="+jobId;
		
		log.info("queryPciPlanJobRecByJobId : sql=" + sql);
		
		ResultSet rs = null;
		List list = new ArrayList();
		Clob clob = null;
		Reader inStream = null;
		String optimizeCells = "";
		
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map map = new HashMap();
				map.put("CITY_ID", rs.getString("CITY_ID"));
				map.put("BEG_MEA_TIME", rs.getString("BEG_MEA_TIME"));
				map.put("END_MEA_TIME", rs.getString("END_MEA_TIME"));
				map.put("RESULT_DIR", rs.getString("RESULT_DIR"));
				map.put("PLAN_TYPE", rs.getString("PLAN_TYPE"));
				map.put("CONVER_TYPE", rs.getString("CONVER_TYPE"));
				map.put("RELA_NUM_TYPE", rs.getString("RELA_NUM_TYPE"));
				map.put("RD_FILE_NAME", rs.getString("RD_FILE_NAME"));
				//获取Clob数据
				clob = rs.getClob("OPTIMIZE_CELLS");
				inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				optimizeCells = new String(c);
				map.put("OPTIMIZE_CELLS", optimizeCells);
				
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("退出方法：queryPciPlanJobRecByJobId。返回：" + list.size() + "个记录");
		return list;
	}

	/**
	 * 统计pci自动规划任务数量
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public long getPciAnalysisTaskCount(Map<String, String> cond, String account) {
		log.info("进入方法：getPciAnalysisTaskCount。condition=" + cond + ",account="+account);	
		
		String where = buildStructureAnalysisTaskWhere(cond, account);
		final String sql = "select count(*) "
					+"  from rno_lte_pci_job sjob " 
					+" left join rno_job job on job.job_id = sjob.job_id  " + where ;
		log.info("获取PCI规划任务的总数 : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = 0;
				if(res != null)
					cnt = res.longValue();
				log.info("获取PCI规划任务的总数,返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页获取pci自动规划任务信息
	 * 
	 * @author peng.jm
	 * @date 2015年3月31日15:23:10
	 */
	public List<Map<String, Object>> queryPciPlanTaskByPage(
			Map<String, String> cond, String account, int startIndex, int cnt) {
		log.info("进入方法：queryPciPlanTaskByPage。condition=" + cond + ",account="+account
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		
		String where = buildStructureAnalysisTaskWhere(cond, account);
		final String sql = "select *      "
			+"   from (select t1.*,rownum as rn    "
			+"	             from ( select sjob.job_id as job_id,   "
			+"	                       job.job_name as job_name,   "
			+"	                       job.job_running_status as job_running_status,   "
			+"	                       to_char(sjob.beg_mea_time,'yyyy-MM-dd HH24:mi:ss') as beg_mea_time,   "
			+"	                       to_char(sjob.end_mea_time,'yyyy-MM-dd HH24:mi:ss') as end_mea_time,   "
			+"	                       sjob.dl_file_name as dl_file_name,   "
			+"	                       sjob.rd_file_name as rd_file_name,   "
			+"	                       sjob.result_dir as result_dir,   "
			+"	                       sjob.mr_job_id as mr_job_id,   "
			+"	                       to_char(job.launch_time,'yyyy-MM-dd HH24:mi:ss') as launch_time,   "
			+"                         to_char(job.complete_time,'yyyy-MM-dd HH24:mi:ss') as complete_time  "
			+"                      from rno_lte_pci_job sjob   "
			+"                      left join rno_job job on job.job_id = sjob.job_id   "
									+ where + ") t1   "
			+"             where rownum <= " + (cnt + startIndex) + " ) t2   "
			+"      where t2.rn > " + startIndex;
		
		log.info("分页获取pci自动规划任务信息 : sql=" + sql);
		List<Map<String, Object>> res = hibernateTemplate
			.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
		if(res!=null && res.size()>0){
			String cityName="";
			for(String k:cond.keySet()){
				if("cityId".equalsIgnoreCase(k)){
					Map<String,Object> area =
							AuthDsDataDaoImpl.getAreaData(Long.parseLong(cond.get(k).toString()));
					if(area!=null){
						cityName=(String)area.get("NAME");
					}
				}
			}
			for(Map<String, Object> one:res){
				one.put("CITY_NAME", cityName);
			}
		}
		log.info("退出方法：queryPciPlanTaskByPage。返回：" + res.size() + "个记录");
		return res;
	}
	/**
	 * 
	 * @title 通过statement更新PCI规划的job状态
	 * @param stmt
	 * @param jobId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-3-30上午10:40:40
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean updatePciPlanWorkStatusByStmt(Statement stmt, long jobId, String workStatus) {
		final String sql = "update rno_lte_pci_job t set t.finish_state='"
				+ workStatus + "' " + " where t.job_id=" + jobId;
		log.info("updatePciPlanWorkStatusByStmt的sql:" + sql);
		boolean result = false;
		try {
			int rows = stmt.executeUpdate(sql);
			if (rows > 0) {
				result = true;
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}
	/**
	 * 
	 * @title MR子关联度
	 * @param results
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31上午10:42:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Map<Integer, List<G4PciRec>> getMrRelaDegree(Result[] results,String numerator) {
		// 遍历结果.
//		System.out.println("results:"+results.length);
		if(numerator==null || "".equals(numerator)){
			numerator="RSRPTIMES1";
		}
		numerator=numerator.toUpperCase();
		Map<Integer, Double> cellToTimes=new HashMap<Integer, Double>();//小区标识到总测量报告数据
		Map<Integer, List<G4PciRec>> cellToNcellObj=new HashMap<Integer, List<G4PciRec>>();//小区标识到邻区集合，一对多的关系
		List<G4PciRec> ncells=null;
		int cellId=0;
		int ncellId=0;
		double timesTotal=0;
		double time1=0;
		int enodebId=0;
		int cellPci=0;
		int ncellPci=0;
		int cellBcch=0;
		int ncellBcch=0;
		boolean flag=true;
		DecimalFormat df=new DecimalFormat("#.#######");
		//第一次整理数据据
		for (Result r : results) {
			Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
			fmap.putAll(r.getFamilyMap(Bytes.toBytes("MRINFO")));
//				System.out.println("==============================================");
		    for (byte[] key : fmap.keySet()) {
		        byte[] value = fmap.get(key);
		        
		        if(Bytes.toString(key).equals(numerator)){
		        	time1=Integer.parseInt(Bytes.toString(value));
		        }
		        if(Bytes.toString(key).equals("CELL_ID")){
		        	//Bytes.toString(fmap.get("CELL_ID"))
		        	 cellId=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("NCELL_ID")){
		        	 if(!Bytes.toString(value).equals("")){
		        		 ncellId=Integer.parseInt(Bytes.toString(value)); 
		        	 }
		        }else if(Bytes.toString(key).equals("TIMESTOTAL")){
		        	 timesTotal=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("ENODEB_ID")){
		        	enodebId=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("CELL_PCI")){
		        	cellPci=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("NCELL_PCI")){
		        	ncellPci=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("CELL_BCCH")){
		        	cellBcch=Integer.parseInt(Bytes.toString(value));
		        }else if(Bytes.toString(key).equals("NCELL_BCCH")){
		        	if("".equals(Bytes.toString(value))){
		        		flag=false;
		        		break;
		        	}
		        	ncellBcch=Integer.parseInt(Bytes.toString(value));
		        }
		    }
		    if(!flag){
		    	continue;
		    }
		    //同频累加
			if(cellBcch!=ncellBcch){
				continue;
			}
			//算出某小区总的timestotal测量报告数据 迭加
			if(cellToTimes.containsKey(cellId)){
				cellToTimes.put(cellId, cellToTimes.get(cellId)+timesTotal);
			}else{
				cellToTimes.put(cellId, timesTotal);
			}
			//获取某邻区的RSRPtimes1  可设置相对数值2(建议值：-3)
			if(cellToNcellObj.containsKey(cellId)){
//					cellToNcellObj.put(cellId, );
				ncells=cellToNcellObj.get(cellId);
				ncells.add(new G4PciRec(ncellId, time1, 0, enodebId, cellPci,ncellPci,cellBcch, ""));
			}else{
				ncells=new ArrayList<G4PciRec>();
				ncells.add(new G4PciRec(ncellId, time1, 0, enodebId, cellPci, ncellPci,cellBcch,""));
				cellToNcellObj.put(cellId, ncells);
			}
		}
		//遍历邻区MAP
		//第二次整理数据获取到某小区对应邻区的子关联度
		for (Integer cell : cellToTimes.keySet()) {
			ncells=cellToNcellObj.get(cell);
			timesTotal=cellToTimes.get(cell);
			for (int i = 0; i < ncells.size(); i++) {
				time1=ncells.get(i).getRsrpTimes1();
				ncells.get(i).setCosi(time1/timesTotal);
			}
		}
		return cellToNcellObj;
	}
	/**
	 * 
	 * @title HO子关联度
	 * @param results
	 * @param factory
	 * ZTE,ERI
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午2:12:58
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Map<Integer, List<G4PciRec>> getHoRelaDegree(Result[] results,String factory) {
		// 遍历结果.
//		System.out.println("results:"+results.length);
		//pmHoExeAttLteIntraF 同频切换出请求次数
		Map<Integer, Double> cellToTimes=new HashMap<Integer, Double>();//小区标识到总测量报告数据
		Map<Integer, List<G4PciRec>> cellToNcellObj=new HashMap<Integer, List<G4PciRec>>();//小区标识到邻区集合，一对多的关系
		List<G4PciRec> ncells=null;
		String id="";
		String enodebId="";
		String ncell="";
		String [] ncellArr=new String [5];
		int cellId=0;
		int ncellId=0;
		int cellPci=0;
		int ncellPci=0;
		double timesTotal=0;
		double time1=0;
		int cellBcch=0;
		int ncellBcch=0;
		DecimalFormat df=new DecimalFormat("#.#######");
		if("ZTE".equals(factory)){
			//第一次整理数据据
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("HOINFO")));
//					System.out.println("==============================================");
			    for (byte[] key : fmap.keySet()) {
			        byte[] value = fmap.get(key);
			        
			        if(Bytes.toString(key).equals("CELLID")){
			        	//Bytes.toString(fmap.get("CELL_ID"))
			        	id=Bytes.toString(value);
//			        	 cellId=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("ENODEBID")){
			        	 if(!Bytes.toString(value).equals("")){
			        		 enodebId=Bytes.toString(value); 
			        	 }
			        }else if(Bytes.toString(key).equals("NCELL")){
			        	 if(!Bytes.toString(value).equals("")){
			        		 ncell=Bytes.toString(value); 
			        	 }
			        }else if(Bytes.toString(key).equals("PMHOPREPATTLTEINTRAF")){
			        	time1=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("CELL_PCI")){
			        	cellPci=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("NCELL_PCI")){
			        	ncellPci=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("CELL_BCCH")){
			        	cellBcch=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("NCELL_BCCH")){
			        	ncellBcch=Integer.parseInt(Bytes.toString(value));
			        }
			    }
			  //同频累加
			    if(cellBcch!=ncellBcch){
					continue;
				}
			    cellId=Integer.parseInt(enodebId+id);
			    //0:460:00:168158:1
			    ncellArr=ncell.split(":");
			    ncellId=Integer.parseInt(ncellArr[3]+ncellArr[4]);
				//算出某小区总的timestotal测量报告数据 迭加
				if(cellToTimes.containsKey(cellId)){
					cellToTimes.put(cellId, cellToTimes.get(cellId)+time1);
				}else{
					cellToTimes.put(cellId, time1);
				}
				//获取某邻区的RSRPtimes1  可设置相对数值2(建议值：-3)
				if(cellToNcellObj.containsKey(cellId)){
//						cellToNcellObj.put(cellId, );
					ncells=cellToNcellObj.get(cellId);
					ncells.add(new G4PciRec(ncellId, time1, 0, Integer.parseInt(enodebId), cellPci, ncellPci,cellBcch,""));
				}else{
					ncells=new ArrayList<G4PciRec>();
					ncells.add(new G4PciRec(ncellId, time1, 0, Integer.parseInt(enodebId), cellPci, ncellPci,cellBcch,""));
					cellToNcellObj.put(cellId, ncells);
				}
			}
		}else if("ERI".equals(factory)){
			//第一次整理数据据
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("HOINFO")));
//					System.out.println("==============================================");
			    for (byte[] key : fmap.keySet()) {
			        byte[] value = fmap.get(key);
			        
			        if(Bytes.toString(key).equals("BUS_CELL_ID")){
			        	//Bytes.toString(fmap.get("CELL_ID"))
//			        	id=Bytes.toString(value);
			        	 cellId=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("EUTRANCELLRELATION")){
			        	 if(!Bytes.toString(value).equals("")){
			        		 ncell=Bytes.toString(value); 
			        	 }
			        }else if(Bytes.toString(key).equals("PMHOEXEATTLTEINTRAF")){
			        	time1=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("CELL_PCI")){
			        	cellPci=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("ENODEBID")){
			        	 if(!Bytes.toString(value).equals("")){
			        		 enodebId=Bytes.toString(value); 
			        	 }
			        }else if(Bytes.toString(key).equals("NCELL_PCI")){
			        	ncellPci=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("CELL_BCCH")){
			        	cellBcch=Integer.parseInt(Bytes.toString(value));
			        }else if(Bytes.toString(key).equals("NCELL_BCCH")){
			        	ncellBcch=Integer.parseInt(Bytes.toString(value));
			        }
			    }
			    //同频累加
			    if(cellBcch!=ncellBcch){
					continue;
				}
			    //4600-657256-11
			    ncellArr=ncell.split("-");
			    ncellId=Integer.parseInt(ncellArr[1]+ncellArr[2]);
				//算出某小区总的timestotal测量报告数据 迭加
				if(cellToTimes.containsKey(cellId)){
					cellToTimes.put(cellId, cellToTimes.get(cellId)+time1);
				}else{
					cellToTimes.put(cellId, time1);
				}
				//获取某邻区的RSRPtimes1  可设置相对数值2(建议值：-3)
				if(cellToNcellObj.containsKey(cellId)){
//						cellToNcellObj.put(cellId, );
					ncells=cellToNcellObj.get(cellId);
					ncells.add(new G4PciRec(ncellId, time1, 0, Integer.parseInt(enodebId), cellPci,ncellPci, cellBcch,""));
				}else{
					ncells=new ArrayList<G4PciRec>();
					ncells.add(new G4PciRec(ncellId, time1, 0, Integer.parseInt(enodebId), cellPci,ncellPci, cellBcch,""));
					cellToNcellObj.put(cellId, ncells);
				}
			}
			
		}
		//遍历邻区MAP
		//第二次整理数据获取到某小区对应邻区的子关联度
		for (Integer cell : cellToTimes.keySet()) {
			ncells=cellToNcellObj.get(cell);
			timesTotal=cellToTimes.get(cell);
			for (int i = 0; i < ncells.size(); i++) {
				time1=ncells.get(i).getRsrpTimes1();
				ncells.get(i).setCosi(time1/timesTotal);
			}
		}
		return cellToNcellObj;
	}
	/**
	 * 
	 * @title 通过城市ID获取从基站标识到lte小区的映射集合
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午3:23:10
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> getEnodebIdForCellsMap(Statement stmt,long cityId) {
		
		Map<String, String> enodebToCells=new HashMap<String, String>();
		String sql= "select t1.business_cell_id cell,			"
				 +"      t1.cell_name name,                 "
				 +"      t1.pci,                            "
				 +"      t1.earfcn,                         "
				 +"      t2.business_enodeb_id ENODEB_ID    "
				 +" from rno_lte_cell t1                    "
				 +" left join rno_lte_enodeb t2             "
				 +"   on t1.enodeb_id = t2.enodeb_id        "
				 +"where t1.area_id =                     "+cityId;
		System.out.println("获取基站的sql语句="+sql);
		List<Map<String, Object>> lteCells=RnoHelper.commonQuery(stmt, sql);
		System.out.println("获取基站的条数为="+lteCells.size());
		if(lteCells==null || lteCells.size()==0){
			return null;
		}
		String enodebId="",earfcn="";
		String cellId="";
		for (Map<String, Object> map : lteCells) {
			enodebId=map.get("ENODEB_ID").toString();
			if(enodebId==null)System.out.println("enodebId为空");
			earfcn=map.get("EARFCN").toString();
			if(earfcn==null)System.out.println("earfcn为空");
			cellId=map.get("CELL").toString();
			if(cellId==null)System.out.println("cellId为空");
			
			if(enodebToCells.get(enodebId+"_"+earfcn)!=null){
				enodebToCells.put(enodebId+"_"+earfcn, enodebToCells.get(enodebId+"_"+earfcn)+"#"+cellId);
			}else{
				enodebToCells.put(enodebId+"_"+earfcn, cellId);
			}
		}
		System.out.println("离开方法getEnodebIdForCellsMap，enodebToCells数量为="+enodebToCells.size());
		return enodebToCells;
	}
	/**
	 * 
	 * @title 获取lte MR测量数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午4:01:13
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Result[] getMrResults(String tableName,String startRow,String stopRow) {
		 HTable table=null;
		try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			table = new HTable(conf,tableName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner scanner = null;
		// 缓存1000条数据
		scan.setCaching(10000);
		scan.setCacheBlocks(false);
		try {
			scanner = table.getScanner(scan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<byte[]> rowList = new LinkedList<byte[]>();
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出
		for (Result r : scanner) {
			String row = Bytes.toString(r.getRow());
				rowList.add(Bytes.toBytes(row));
		}
		// 获取取出的row key的GET对象
		List<Get> getList = new LinkedList<Get>();
		log.debug("rowList 大小="+rowList.size());
       for (byte[] row : rowList) {
           Get get = new Get(row);
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("ENODEB_ID"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CITY_ID"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("MEA_TIME"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_ID"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_NAME"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_BCCH"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_PCI"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_ID"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_BCCH"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_PCI"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("TIMESTOTAL"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES0"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES1"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES2"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES3"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES4"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_BCCH"));
           get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_BCCH"));
           getList.add(get);
       }

		Result[] results=null;
		try {
			results = table.get(getList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				getList.clear();
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * 
	 * @title 获取lte HO切换数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param factory
	 * ZTE,ERI
	 * @return
	 * @author chao.xj
	 * @date 2015-3-31下午4:00:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Result[] getHoResults(String tableName,String startRow,String stopRow,String factory) {
		 HTable table=null;
		try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			table = new HTable(conf,tableName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		ResultScanner scanner = null;
		// 缓存1000条数据
		scan.setCaching(1000);
		scan.setCacheBlocks(false);
		try {
			scanner = table.getScanner(scan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<byte[]> rowList = new LinkedList<byte[]>();
		// 遍历扫描器对象， 并将需要查询出来的数据row key取出
		for (Result r : scanner) {
			String row = Bytes.toString(r.getRow());
				rowList.add(Bytes.toBytes(row));
		}
		// 获取取出的row key的GET对象
		List<Get> getList = new LinkedList<Get>();
      for (byte[] row : rowList) {
          Get get = new Get(row);
          if("ERI".equals(factory)){
        	  get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("EUTRANCELLTDD"));
        	  get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("ENODEBID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CITY_ID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("MEA_TIME"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("BUS_CELL_ID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("EUTRANCELLRELATION"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOEXEATTLTEINTRAF"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_PCI"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_BCCH"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("NCELL_BCCH"));
          }else if("ZTE".equals(factory)){
        	  get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("ENODEBID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CITY_ID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("MEA_TIME"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELLID"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("NCELL"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("PMHOPREPATTLTEINTRAF"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_PCI"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("CELL_BCCH"));
              get.addColumn(Bytes.toBytes("HOINFO"), Bytes.toBytes("NCELL_BCCH"));
          }
          getList.add(get);
      }

		Result[] results=null;
		try {
			results = table.get(getList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				getList.clear();
				table.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return results;
	}
	/**
	 * 
	 * @title 添加mapreduce的jobid
	 * @param stmt
	 * @param jobId
	 * @param mrJobId
	 * @author chao.xj
	 * @date 2015-3-31下午5:05:17
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void addMapReduceJobId(Statement stmt,long jobId,String mrJobId) {
		String sql="update rno_lte_pci_job set MR_JOB_ID='"+mrJobId+"' WHERE JOB_ID="+jobId;
		try {
			stmt.execute(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @title 保存原始PCI文件到hdfs文件系统上
	 * @param filePath
	 * @param sumMrRelaDegre
	 * @author chao.xj
	 * @date 2015-3-31下午7:36:37
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void saveOriPciInHdfs(String filePath, Map<Integer, List<G4PciRec>> sumMrRelaDegre) {
		String realFilePath = filePath;

		FileSystem fs = null;
		try {
			Configuration conf = new YarnConfiguration();
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			System.out.println("saveOriPciInHdfs过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}
		//先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if(fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			System.out.println("saveOriPciInHdfs过程：保存文件时，删除原有文件出错！");
			e2.printStackTrace();
		}
		//创建新文件
		Path filePathObj = new Path(URI.create(realFilePath));
		//创建流
		OutputStream dataOs= null;
		try {
			dataOs = fs.create(filePathObj, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BufferedOutputStream bo=null ;
		String line="";
		int ncellId=0;
		double cosi=0;
		int cellPci=0;
		int ncellPci=0;
		String cells="";
		List<G4PciRec> mrPciRecs=null;
		try {
			
			bo = new BufferedOutputStream(dataOs);
			for (Integer cell : sumMrRelaDegre.keySet()) {
				mrPciRecs=sumMrRelaDegre.get(cell);
				//G4PciRec向其对象中填充服务小区的同站其他小区信息
				for (G4PciRec g4PciRec : mrPciRecs) {
					cells=g4PciRec.getSameSiteOtherCell();
					cellPci=g4PciRec.getCellPci();
					cosi=g4PciRec.getCosi();
					ncellId=g4PciRec.getNcell_id();
					ncellPci=g4PciRec.getNcellPci();
					//CELL##NCELL,COSI;CELL2,CELL3;CELLPCI;NCELLPCI
					line=cell+"##"+ncellId+","+cosi+";"+cells+";"+cellPci+";"+ncellPci;
					bo.write(Bytes.toBytes(line+"\n"));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-9下午2:34:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, List<String>> getLteCellInfoByCellId(Statement stmt,long cityId) {
		log.debug("进入方法体getLteCellInfoByCellId(Statement stmt,long cityId)"+stmt+","+cityId);
		Map<String,  List<String>> cellToCells=new HashMap<String,  List<String>>();
		String sql= "select t1.business_cell_id cell,			"
				 +"      t1.cell_name name,                 "
				 +"      t1.pci,                            "
				 +"      t1.earfcn,                         "
				 +"      t2.business_enodeb_id ENODEB_ID    "
				 +" from rno_lte_cell t1                    "
				 +" left join rno_lte_enodeb t2             "
				 +"   on t1.enodeb_id = t2.enodeb_id        "
				 +"where t1.area_id =                     "+cityId;
		List<Map<String, Object>> lteCells=RnoHelper.commonQuery(stmt, sql);
		if(lteCells==null || lteCells.size()==0){
			return null;
		}
		int enodebId=0;
		String cellId="";
		String pci="";
		String cellName="";
		String earfcn="";
		for (Map<String, Object> map : lteCells) {
//			enodebId=Integer.parseInt(map.get("ENODEB_ID").toString());
			cellId=map.get("CELL").toString();
			pci=map.get("PCI").toString();
			cellName=map.get("NAME").toString();
			earfcn=map.get("EARFCN").toString();
			cellToCells.put(cellId, Arrays.asList(cellName,pci,earfcn));
		}
		log.debug("退出getLteCellInfoByCellId(Statement stmt,long cityId)"+cellToCells.size());
		return cellToCells;
	}
}