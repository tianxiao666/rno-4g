package com.iscreate.op.dao.rno;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class RnoNcsDynaCoverageDaoImpl implements RnoNcsDynaCoverageDao {

	private static Log log=LogFactory.getLog(RnoNcsDynaCoverageDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	private DateUtil dateUtil=new DateUtil();
	private Connection conn = null;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}


	/**
	 * 检查小区是华为还是爱立信
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	public String checkCellIsHwOrEri(final String cell) {
		List<Map<String, Object>> res = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = " select manufacturers from rno_bsc " +
								" where bsc_id=(select bsc_id from cell " +
											" where label='"+cell+"')";
						log.debug("checkCellIsHwOrEri ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
		String result = null;
		if(res.size() > 0) {
			result = res.get(0).get("MANUFACTURERS").toString();
		}
		return result;
	}

	/**
	 * 查询爱立信ncs数据，并整理得到需要结果
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEriDataFromOracle(final long cityId,
			final String cell, final String startDate, final String endDate, String RELSS) {

		conn = DataSourceConn.initInstance().getConnection();

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		//long t1 = System.currentTimeMillis();
		List<Map<String,Object>> eriNcsDescInfos = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = " select rno_2g_eri_ncs_desc_id as DESC_ID, "
							+	" 	          RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5 "
							+	" 	      from rno_2g_eri_ncs_descriptor "
							+	" 	     where city_id = "+cityId
							+	" 	       and mea_time>=to_date('"
												+startDate+"','yyyy-MM-dd HH24:mi:ss') "
							+	" 	       and mea_time<=to_date('"
												+endDate+"','yyyy-MM-dd HH24:mi:ss') ";
						log.debug("queryEriNcsDataFromOracle ,查询描述id的sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
		});
		//long t2 = System.currentTimeMillis();
		//System.out.println("获取文件描述信息耗时："+(t2-t1));

//		String descIdStr = "";
//		List<String> descIdList = new ArrayList<String>();
//		for (Map<String, Object> map : eriNcsDescInfos) {
//			descIdStr += map.get("DESC_ID").toString() + ",";
//			descIdList.add(map.get("DESC_ID").toString());
//		}
//		if(("").equals(descIdStr)) {
//			log.debug("动态覆盖图：cityId="+cityId+",cell="+cell+",startDate="
//					+startDate+",endDate="+endDate+",找不到对应的爱立信ncs描述信息！");
//			return Collections.emptyList();
//		}
//		final String descIds = descIdStr.substring(0, descIdStr.length()-1);

		String ncsFields = "CELL,NCELL,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,REPARFCN,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,DISTANCE,INTERFER";
		String sql = "";
		String TIMESRELSS = "";
		String ncsDescId = "";
		String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);

		//t1 = System.currentTimeMillis();
		for (Map<String,Object> ncsDesc : eriNcsDescInfos) {

			ncsDescId = ncsDesc.get("DESC_ID").toString();

			//确定门限值字段
			TIMESRELSS = getEriTimesRelssXByValue(ncsDesc, RELSS);
			//log.debug("动态覆盖图：获取[" + RELSS + "]对应的列为：" + TIMESRELSS);
			if (TIMESRELSS == null || "".equals(TIMESRELSS)) {
				log.debug("动态覆盖图：ncs[" + ncsDescId + "]未获取到相应的[" + RELSS + "]对应的列，将尝试用+0获取");
				TIMESRELSS = getEriTimesRelssXByValue(ncsDesc, "+0");
				log.debug("动态覆盖图：获取[+0]对应的列为：" + TIMESRELSS);
			}
			if("".equals(TIMESRELSS)) {
				continue;
			}

			//转移数据到临时表
			sql = "insert into RNO_2G_NCS_COVER_T(RNO_NCS_DESC_ID," + ncsFields
					+ ")      select rno_2g_eri_ncs_desc_id," + ncsFields
					+	" 	      from rno_2g_eri_ncs "
					+	" 	     where ncell = '"+cell+"'"
					+   "		   and rno_2g_eri_ncs_desc_id ="+ncsDescId
					+	" 	       and distance > 0.1 "
					+	" 	       and reparfcn >=5000 "
					+	" 	       and cell in (select label from cell "
					+	"				  where indoor_cell_type = '室外覆盖' "
					+	"				  and area_id in("+areaIdStr+")) ";
			try {
				//stmt.executeUpdate(sql);
				stmt.addBatch(sql);
				//log.debug("动态覆盖图：转移ncs[" + ncsDescId + "]数据到临时表数据量：" + affectCnt);
			} catch (SQLException e) {
				log.error("动态覆盖图：转移数据ncs[" + ncsDescId + "]到临时表执行失败！sql=" + sql);
				e.printStackTrace();
				continue;
			}

			sql = "update RNO_2G_NCS_COVER_T set interfer="
					+ TIMESRELSS
					+ "/DECODE(reparfcn,0,NULL,reparfcn) where RNO_NCS_DESC_ID ="
					+ ncsDescId;
			try {
				//stmt.executeUpdate(sql);
				stmt.addBatch(sql);
			} catch (SQLException e) {
				log.debug("动态覆盖图：计算interfer出错，sql =" + sql);
				e.printStackTrace();
				continue;
			}
		}
		//t2 = System.currentTimeMillis();
		//System.out.println("提交批处理sql耗时："+(t2-t1));
		//t1 = System.currentTimeMillis();
		try {
			stmt.executeBatch();
		} catch (SQLException e1) {
			e1.printStackTrace();
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
				stmt.close();
				conn.commit();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return Collections.emptyList();
		}
		//t2 = System.currentTimeMillis();
		//System.out.println("执行批处理sql耗时："+(t2-t1));

		sql = " select  interfer as val, "
				+	" 	cell_lon as lng, "
				+	" 	cell_lat as lat, "
				+	"   cell as ncell_id, "
				+	" 	ncell_lon as cell_lng, "
				+	" 	ncell_lat as cell_lat "
				+ 	" from RNO_2G_NCS_COVER_T where interfer<>0 ";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			log.debug("动态覆盖图：计算interfer出错，sql =" + sql);
			e.printStackTrace();
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
				stmt.close();
				conn.commit();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return Collections.emptyList();
		}
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = null;
		try {
			while (rs.next()) {
				map = new HashMap<String, Object>();
				map.put("VAL", rs.getDouble("VAL"));
				map.put("LNG", rs.getDouble("LNG"));
				map.put("LAT", rs.getDouble("LAT"));
				map.put("NCELL_ID", rs.getString("NCELL_ID"));
				map.put("CELL_LNG", rs.getDouble("CELL_LNG"));
				map.put("CELL_LAT", rs.getDouble("CELL_LAT"));
				result.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
				stmt.close();
				conn.commit();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;

//		return hibernateTemplate
//				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
//					public List<Map<String, Object>> doInHibernate(Session arg0)
//							throws HibernateException, SQLException {
//						String sql = " select timesrelss/reparfcn as val, "
//							+	" 	       cell_lon as lng, "
//							+	" 	       cell_lat as lat, "
//							+	" 	       ncell_lon as cell_lng, "
//							+	" 	       ncell_lat as cell_lat "
//							+	" 	 from( select cell,timesrelss,reparfcn,cell_lon,cell_lat,ncell_lon,ncell_lat "
//							+	" 	      from rno_2g_eri_ncs "
//							+	" 	     where ncell = '"+cell+"' "
//							+   "		   and rno_2g_eri_ncs_desc_id in("+descIds+")"
//							+	" 	       and mea_time>=to_date('"
//												+startDate+"','yyyy-MM-dd HH24:mi:ss') "
//							+	" 	       and mea_time<=to_date('"
//												+endDate+"','yyyy-MM-dd HH24:mi:ss') "
//							+	" 	       and distance > 0.1 "
//							+	" 	       and reparfcn >=5000 "
//							+	" 	       and cell in (select label from cell "
//							+	"				  where indoor_cell_type = '室外覆盖' "
//							+	"				  and area_id in("+areaIdStr+")) "
//							+	" 	       and timesrelss <>0) ";
//						log.debug("queryEriNcsDataFromOracle ,查询数据的sql=" + sql);
//						SQLQuery query = arg0.createSQLQuery(sql);
//						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//						List<Map<String, Object>> rows = query.list();
//						return rows;
//					}
//		});
	}

	/**
	 * 根据限值获取爱立信ncs中对应的timesrelss
	 * @param desc
	 * @param relsscons
	 * @return
	 */
	public String getEriTimesRelssXByValue(Map<String,Object> desc,
			String relsscons) {
//		log.debug("进入getTimesRelssXByValue(rnoncsdesc=" + desc
//				+ ",relsscons=" + relsscons + ")");
		String TIMESRELSS = "";
		String relss;
		relss = (Integer.parseInt(desc.get("RELSS_SIGN").toString()) == 0 ? "+" : "-")
				+ desc.get("RELSS").toString();
		if (relsscons.equals(relss)) {
			TIMESRELSS = "TIMESRELSS";
		} else {
			relss = (Integer.parseInt(desc.get("RELSS2_SIGN").toString()) == 0 ? "+" : "-")
					+ desc.get("RELSS2").toString();
			if (relsscons.equals(relss)) {
				TIMESRELSS = "TIMESRELSS2";
			} else {
				relss = (Integer.parseInt(desc.get("RELSS3_SIGN").toString()) == 0 ? "+" : "-")
						+ desc.get("RELSS3").toString();
				if (relsscons.equals(relss)) {
					TIMESRELSS = "TIMESRELSS3";
				} else {
					relss = (Integer.parseInt(desc.get("RELSS4_SIGN").toString()) == 0 ? "+" : "-")
							+ desc.get("RELSS4").toString();
					if (relsscons.equals(relss)) {
						TIMESRELSS = "TIMESRELSS4";
					} else {
						relss = (Integer.parseInt(desc.get("RELSS5_SIGN").toString()) == 0 ? "+" : "-")
								+ desc.get("RELSS5").toString();
						if (relsscons.equals(relss)) {
							TIMESRELSS = "TIMESRELSS5";
						}
					}
				}
			}
		}
//		log.debug("退出getTimesRelssXByValue　TIMESRELSS:" + TIMESRELSS);
		return TIMESRELSS;
	}

	/**
	 * 查询华为ncs数据，并整理得到需要结果
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHwDataFromOracle(final long cityId,
			final String cell, final String startDate, final String endDate, final String RELSS) {

		final String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);

		List<Map<String,Object>> hwNcsDescInfos = hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = " select id as desc_id "
							+	" 	      from rno_2g_hw_ncs_desc "
							+	" 	     where city_id = "+cityId
							+	" 	       and mea_time>=to_date('"
												+startDate+"','yyyy-MM-dd HH24:mi:ss') "
							+	" 	       and mea_time<=to_date('"
												+endDate+"','yyyy-MM-dd HH24:mi:ss') ";
						log.debug("queryHwNcsDataFromOracle ,查询描述id的sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
		});
		String descIdStr = "";
		for (Map<String, Object> map : hwNcsDescInfos) {
			descIdStr += map.get("DESC_ID").toString() + ",";
		}
		if(("").equals(descIdStr)) {
			log.debug("cityId="+cityId+",cell="+cell+",startDate="
					+startDate+",endDate="+endDate+",找不到对应的华为ncs描述信息！");
			return Collections.emptyList();
		}
		final String descIds = descIdStr.substring(0, descIdStr.length()-1);

		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String valStr = "";
						if("-12".equals(RELSS)) {
							valStr = "(s361-s369)/s3013 as val,";
						} else {
							valStr = "(s361-s366)/s3013 as val,";
						}
						String sql = " select  "+valStr
							+	" 	       cell_lon as lng, "
							+	" 	       cell_lat as lat, "
							+	"          cell as ncell_id, "
							+	" 	       ncell_lon as cell_lng, "
							+	" 	       ncell_lat as cell_lat "
							+	" 	 from( select cell,s361,s3013,cell_lon,cell_lat,ncell_lon,ncell_lat "
							+	" 	      from rno_2g_hw_ncs "
							+	" 	     where ncell = '"+cell+"' "
							+   " 		   and rno_2ghwncs_desc_id in ("+descIds+")"
							+	" 	       and distance > 0.1 "
							+	" 	       and s3013 >=5000 "
							+	" 	       and cell in (select label from cell "
							+	"				  where indoor_cell_type = '室外覆盖' "
							+	"				  and area_id in("+areaIdStr+")) "
							+	" 	       and s361 <>0) ";
						log.debug("queryHwNcsDataFromOracle ,查询数据的sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
		});
	}


	private String build2GDirectionAngleTaskWhere(Map<String, String> condition) {
		String where = "";
		String v = "";
		long lv;
		where +=" where STATUS = 'N' ";

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
				}
				else if ("begTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.parseDateArbitrary(v);
					if (dt != null) {
						where += (where.length() == 0 ? " " : " and ")
								+ " CREATE_DATE>=to_date('" + v
								+ "','yyyy-MM-dd HH24:mi:ss') ";
					} else {
						log.warn("传入无效的时间字符：" + v);
					}
				} else if ("endTime".equalsIgnoreCase(k)) {
					Date dt = dateUtil.parseDateArbitrary(v);
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
	 * 按条件获取2g小区方向角计算任务信息总数
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	public long get2GDirectionAngleTaskCount(Map<String, String> cond) {
		String where = build2GDirectionAngleTaskWhere(cond);
		final String sql = "select count(DESC_ID) from RNO_2G_DIRANGLE_REC "
				+ where;
		log.info("get2GDirectionAngleTaskCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("get2GDirectionAngleTaskCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}

	/**
	 * 按条件分页获取2g小区方向角计算任务信息
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> query2GDirectionAngleTaskByPage(
			Map<String, String> cond, int startIndex, int cnt) {
		log.info("进入方法：query2GDirectionAngleTaskByPage。condition=" + cond
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (cond == null || cond.isEmpty()) {
			log.info("未传入查询条件");
		}
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}

		String where = build2GDirectionAngleTaskWhere(cond);

		String fields = "mid.DESC_ID,to_char(mid.CREATE_DATE,'yyyy-mm-dd HH24:mi:ss') as CREATE_DATE," +
				"to_char(mid.START_MEA_DATE,'yyyy-mm-dd HH24:mi:ss') as START_MEA_DATE," +
				"to_char(mid.END_MEA_DATE,'yyyy-mm-dd HH24:mi:ss') as END_MEA_DATE," +
				"mid.RECORD_NUM,mid.WORK_STATUS,mid.FILE_PATH,mid.JOB_ID,mid.STATUS";

		long cityId=-1;
		for(String k : cond.keySet()){
			if("cityId".equalsIgnoreCase(k)){
				try{
					cityId=Long.parseLong(cond.get(k).toString());
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
			          + "             			from RNO_2G_DIRANGLE_REC t " + where
			          + "		 				order by CREATE_DATE desc) t2 )"
			          + " 				where rn <= "+ (cnt + startIndex)
			          + "				  and rn > "+ startIndex +" )  mid "
			          + "  order by mid.CREATE_DATE desc";
		log.info("分页获取2g小区方向角计算任务的sql：" + sql);
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

		log.info("退出方法：query2GDirectionAngleTaskByPage。返回：" + res.size() + "个记录");
		return res;
	}

	/**
	 * 查询对应条件的NCS数据记录数量
	 *
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:02:24
	 */
	public long getNcsDataCount(Map<String, String> cond) {
		log.info("进入方法getNcsDataCount。condition="+cond);

		long cityId = Long.parseLong(cond.get("cityId").toString());
		String startTime = cond.get("begTime").toString();
		String endTime = cond.get("endTime").toString();

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
	 * 根据条件获取NCS的数据记录总数量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:57:59
	 */
	public long queryNcsDataRecordsCount(long cityId, String startMeaDate,
			String endMeaDate) {
		log.info("进入方法queryNcsDataRecordsCount。cityId="+cityId
				+",startTime="+startMeaDate+",endTime="+endMeaDate);
		final String sql = " select sum(cnt) from( "
				+ " 	select sum(record_count) as cnt "
				+ "		  from rno_2g_eri_ncs_descriptor "
				+ "		 where city_id = " + cityId
				+ "		   and MEA_TIME >= "
				+ "		       to_date('"+startMeaDate+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		   and MEA_TIME <= "
				+ "		       to_date('"+endMeaDate+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		 union all "
				+ "		  select sum(record_count) as cnt "
				+ "		          from rno_2g_hw_ncs_desc "
				+ "		         where city_id = " + cityId
				+ "		           and MEA_TIME >= "
				+ "		               to_date('"+startMeaDate+"', 'yyyy-MM-dd HH24:mi:ss') "
				+ "		           and MEA_TIME <= "
				+ "		               to_date('"+endMeaDate+"', 'yyyy-MM-dd HH24:mi:ss')) ";
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
	 * 创建2g小区方向角计算任务
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日17:44:39
	 */
	public boolean create2GDirectionAngleTask(Map<String, Object> task) {
		log.info("进入方法create2GDirectionAngleTask。task="+task);

		long descId = Long.parseLong(task.get("desc_id").toString());
		long cityId = Long.parseLong(task.get("city_id").toString());
		String createDate = task.get("create_date").toString();
		String startMeaDate = task.get("start_mea_date").toString();
		String endMeaDate = task.get("end_mea_date").toString();
		long recordNum = Long.parseLong(task.get("record_num").toString());
		String filePath = task.get("file_path").toString();
		String workStatus = task.get("work_status").toString();
		long jobId = Long.parseLong(task.get("job_id").toString());
		String status = task.get("status").toString();

		final String sql = "insert into RNO_2G_DIRANGLE_REC (" +
											"DESC_ID," +
											"CITY_ID," +
											"CREATE_DATE," +
											"START_MEA_DATE," +
											"END_MEA_DATE," +
											"RECORD_NUM," +
											"WORK_STATUS," +
											"FILE_PATH," +
											"JOB_ID," +
											"STATUS) " +
					" values(" + descId + "," + cityId
							+ ", to_date('" + createDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+ ", to_date('" + startMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+ ", to_date('" + endMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+", " + recordNum + ",'" + workStatus
							+ "','" + filePath + "'," + jobId + ",'" + status + "')";

		log.info("create2GDirectionAngleTask的sql:"+sql);
		return hibernateTemplate
		.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				int res = query.executeUpdate();
				log.info("退出create2GDirectionAngleTask,受影响行数="+res);
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


	public List<Map<String, Object>> queryDirectionAngleTaskByJobId(
			Statement stmt, long jobId) {
		log.info("进入方法queryDirectionAngleTaskByJobId。jobId="+jobId);

		String sql = "select DESC_ID,CITY_ID,START_MEA_DATE,END_MEA_DATE,FILE_PATH "
					+ " from rno_2g_dirangle_rec where JOB_ID ="+jobId;

		log.info("queryDirectionAngleTaskByJobId : sql=" + sql);

		ResultSet rs = null;
		List list = new ArrayList();
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map map = new HashMap();
				map.put("DESC_ID", rs.getLong(1));
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
		log.info("退出方法：queryDirectionAngleTaskByJobId。返回：" + list.size() + "个记录");
		return list;
	}

	public boolean updateDirectionAngleTaskWorkStatusByStmt(Statement stmt,
			long descId, String workStatus) {
		final String sql = "update rno_2g_dirangle_rec t set t.work_status='"
				+ workStatus + "' " + " where t.desc_id=" + descId;
		log.info("updateDirectionAngleTaskWorkStatusByStmt 的sql:" + sql);
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

	public Map<String, Map<String, String>> queryCellsByCityId(
			Statement stmt, long cityId) {
		log.info("进入方法queryCellsByCityId。cityId="+cityId);

		String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);

		String sql = "select name as cell_name," +
							" label as cell_id," +
							" BEARING as azimuth " +
							" from cell where area_id in("+areaIdStr+")";
		log.info("queryCellsByCityId : sql=" + sql);
		ResultSet rs = null;
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String,String>>();
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("CELL_ID", rs.getString("CELL_ID"));
				map.put("CELL_NAME", rs.getString("CELL_NAME"));
				map.put("AZIMUTH", rs.getString("AZIMUTH"));
				maps.put(map.get("CELL_ID"),map);
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
		log.info("退出方法：queryCellsByCityId。返回：" + maps.size() + "个记录");
		return maps;
	}

	/**
	 * 通过指定城市和日期，获取计算2g小区方向角所需的ncs数据（包括华为和爱立信）
	 * @param stmt
	 * @param cityId
	 * @param startMeaDate
	 * @param endMeaDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDataForCalc2GDirectionAngle(
			Statement stmt, final long cityId, final String startMeaDate,
			final String endMeaDate) {

		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> one = null;

		// 先获取条件范围内的爱立信文件信息，判断是哪个字段是RELSS>-12
		String sql = " select rno_2g_eri_ncs_desc_id as DESC_ID, "
			+	" 	          RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5 "
			+	" 	      from rno_2g_eri_ncs_descriptor "
			+	" 	     where city_id = "+cityId
			+	" 	       and mea_time>=to_date('"
								+startMeaDate+"','yyyy-MM-dd HH24:mi:ss') "
			+	" 	       and mea_time<=to_date('"
								+endMeaDate+"','yyyy-MM-dd HH24:mi:ss') ";

		List<Map<String,Object>> eriNcsDescInfos = RnoHelper.commonQuery(stmt, sql);

		String ncsFields = "CELL,NCELL,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,REPARFCN,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,DISTANCE,INTERFER";
		String TIMESRELSS = "";
		String RELSS = "-12";
		String ncsDescId = "";
		final String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);

		//逐个desc文件数据转移
		for (Map<String,Object> ncsDesc : eriNcsDescInfos) {

			ncsDescId = ncsDesc.get("DESC_ID").toString();

			//确定门限值字段
			TIMESRELSS = getEriTimesRelssXByValue(ncsDesc, RELSS);
			//log.debug("动态覆盖图：获取[" + RELSS + "]对应的列为：" + TIMESRELSS);
			if (TIMESRELSS == null || "".equals(TIMESRELSS)) {
				log.debug("计算2g小区方向角：ncs[" + ncsDescId + "]未获取到相应的[" + RELSS + "]对应的列，将尝试用+0获取");
				TIMESRELSS = getEriTimesRelssXByValue(ncsDesc, "+0");
				log.debug("计算2g小区方向角：获取[+0]对应的列为：" + TIMESRELSS);
			}
			if("".equals(TIMESRELSS)) {
				continue;
			}

			//转移数据到临时表
			sql = "insert into RNO_2G_NCS_COVER_T(RNO_NCS_DESC_ID," + ncsFields
					+ ")      select rno_2g_eri_ncs_desc_id," + ncsFields
					+	" 	      from rno_2g_eri_ncs "
					+	" 	     where  rno_2g_eri_ncs_desc_id ="+ncsDescId
					+	" 	       and distance > 0.1 "
					+	" 	       and reparfcn >=5000 "
					+	" 	       and cell in (select label from cell "
					+	"				  where indoor_cell_type = '室外覆盖' "
					+	"				  and area_id in("+areaIdStr+")) ";
			try {
				stmt.addBatch(sql);
			} catch (SQLException e) {
				log.error("计算2g小区方向角：转移数据ncs[" + ncsDescId + "]到临时表执行失败！sql=" + sql);
				e.printStackTrace();
				continue;
			}

			sql = "update RNO_2G_NCS_COVER_T set interfer="
					+ TIMESRELSS
					+ "/DECODE(reparfcn,0,NULL,reparfcn) where RNO_NCS_DESC_ID ="
					+ ncsDescId;
			try {
				//stmt.executeUpdate(sql);
				stmt.addBatch(sql);
			} catch (SQLException e) {
				log.debug("计算2g小区方向角：计算interfer出错，sql =" + sql);
				e.printStackTrace();
				continue;
			}
		}

		try {
			stmt.executeBatch();
		} catch (SQLException e1) {
			e1.printStackTrace();
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
				stmt.close();
				conn.commit();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return result;
		}

		//整理爱立信ncs数据，得出计算所需的数据
		sql = "select sum(one_lng-lng)+lng as lng_diff,  "
			+"	       sum(one_lat-lat)+lat as lat_diff,  "
			+"	       lng,  "
			+"	       lat,  "
			+"	       cell  "
			+"	    from( select ncell as cell,  "
			+"	                 cell as one_cell,  "
			+"	                 ncell_lon as lng,  "
			+"	                 ncell_lat as lat,  "
			+"	                 ncell_lon +  "
			+"	                 interfer * ((cell_lon - ncell_lon) /  "
			+"	                 sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) +  "
			+"	                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat))) as one_lng,  "
			+"	                 ncell_lat +  "
			+"	                 interfer * ((cell_lat - ncell_lat) /  "
			+"	                 sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) +  "
			+"	                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat))) as one_lat  "
			+"	            from RNO_2G_NCS_COVER_T  "
			+"	           where   "
			+"	              (cell_lon - ncell_lon) <> 0  "
			+"	             and (cell_lat - ncell_lat) <> 0)  "
			+"	   group by cell,lng,lat ";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			log.debug("计算2g小区方向角：ERI整理数据出错，sql =" + sql);
			e.printStackTrace();
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return result;
		}
		try {
			while (rs.next()) {
				one = new HashMap<String, Object>();
				one.put("LNG_DIFF", rs.getDouble("LNG_DIFF"));
				one.put("LAT_DIFF", rs.getDouble("LAT_DIFF"));
				one.put("LNG", rs.getDouble("LNG"));
				one.put("LAT", rs.getDouble("LAT"));
				one.put("CELL", rs.getString("CELL"));
				result.add(one);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 清除数据，关闭资源
			try {
				stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
			} catch (Exception e3) {
				e3.printStackTrace();
			}
			return result;
		}

		//清除数据
		try {
			stmt.executeUpdate("truncate table RNO_2G_NCS_COVER_T");
		} catch (Exception e3) {
			e3.printStackTrace();
		}

		//先获取条件范围内的华为文件信息，通过描述id获取数据
		sql = " select id as desc_id "
							+	" 	      from rno_2g_hw_ncs_desc "
							+	" 	     where city_id = "+cityId
							+	" 	       and mea_time>=to_date('"
												+startMeaDate+"','yyyy-MM-dd HH24:mi:ss') "
							+	" 	       and mea_time<=to_date('"
												+endMeaDate+"','yyyy-MM-dd HH24:mi:ss') ";

		List<Map<String,Object>> hwNcsDescInfos = RnoHelper.commonQuery(stmt, sql);

		String descIdStr = "";
		for (Map<String, Object> map : hwNcsDescInfos) {
			descIdStr += map.get("DESC_ID").toString() + ",";
		}
		if(("").equals(descIdStr)) {
			log.debug("计算2g小区方向角：cityId="+cityId+",startDate="
					+startMeaDate+",endDate="+endMeaDate+",找不到对应的华为ncs描述信息！");
			return result;
		}
		final String descIds = descIdStr.substring(0, descIdStr.length()-1);

		//整理华为ncs数据，得出计算所需的数据
		//RELSS>-12取分子为(s361-s369)
		sql = "select sum(one_lng-lng)+lng as lng_diff, "
			+"	       sum(one_lat-lat)+lat as lat_diff, "
			+"	       lng, "
			+"	       lat, "
			+"	       cell "
			+"	    from( select ncell as cell, "
			+"	                 cell as one_cell, "
			+"	                 ncell_lon as lng, "
			+"	                 ncell_lat as lat, "
			+"	                 ncell_lon + "
			+"	                 ((s361-s369)/s3013) * "
			+" 					 ((cell_lon - ncell_lon) / "
			+"	                 sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) + "
			+"	                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat))) as one_lng, "
			+"	                 ncell_lat + "
			+"	                 ((s361-s369)/s3013) * "
			+"	                 ((cell_lat - ncell_lat) / "
			+"	                 sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) + "
			+"	                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat))) as one_lat "
			+"	            from rno_2g_hw_ncs "
			+"	           where  rno_2ghwncs_desc_id in ("+descIds+") "
			+"	             and (cell_lon - ncell_lon) <> 0 "
			+"	             and (cell_lat - ncell_lat) <> 0 "
			+"	             and ncell_lon<>0 "
			+"	             and ncell_lat<>0 "
			+" 	       	     and distance > 0.1 "
			+" 	       		 and s3013 >=5000 "
			+" 	             and cell in (select label from cell "
			+"				  				where indoor_cell_type = '室外覆盖' "
			+"				  				and area_id in("+areaIdStr+")))"
			+"   group by cell,lng,lat  ";

		List<Map<String, Object>> hwData = RnoHelper.commonQuery(stmt, sql);

		//将华为的数据也add进返回结果
		for (Map<String, Object> map : hwData) {
			result.add(map);
		}

		return result;
	}


	/**
	 * 从Hbase查询MR数据，并整理得到动态覆盖图所需的数据集
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	public Map<String,List<Map<String,Object>>> queryDynaCoverDataFromHbaseMrTable(
			long cityId, String lteCellId,
			String startDate, String endDate) {

		//获取cityId的lte小区数据出来，用于判断lte小区是否E频段（需排除E频段）
		List<Map<String,Object>> lteCells = queryLteCellWhichBandIsEByCityId(cityId);
		Map<String,String> cellIdToBandType = new HashMap<String, String>();
		for (Map<String, Object> one : lteCells) {
			cellIdToBandType.put(one.get("BUSINESS_CELL_ID").toString(),
					one.get("BAND_TYPE").toString());
		}

		long sMill = dateUtil.parseDateArbitrary(startDate).getTime();
		long eMill = dateUtil.parseDateArbitrary(endDate).getTime();
		String startRow = cityId+"_"+sMill+"_#";
		String stopRow = cityId+"_"+eMill+"_~";

		List<Map<String,Object>> res1 = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> res2 = new ArrayList<Map<String,Object>>();
		//增加关联度详情信息
		List<Map<String,Object>> resInterDetail = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;

		//MUST_PASS_ALL(条件 AND) MUST_PASS_ONE（条件OR）  修改以邻区作为服务小区CELL_ID－＞NCELL_ID
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes("MRINFO"),
        		Bytes.toBytes("CELL_ID"), CompareOp.EQUAL,
        		Bytes.toBytes(lteCellId)));
		//排除小于100米
		filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes("MRINFO"),
        		Bytes.toBytes("DISTANCE"), CompareOp.GREATER_OR_EQUAL,
        		Bytes.toBytes("0.1")));


		/**获取爱立信MR数据整理**/
		/*List<Map<String, String>> eriData = getDataFromHbase("LTE4G_ERI_MR",
				cityId, startRow, stopRow, filterList, "MRINFO");*/
		//@author chao.xj  2015-5-27 上午11:19:09  修改
		List<Map<String, String>> eriData = query4GCellMrIndexFromHBase("LTE4G_ERI_MR", cityId, lteCellId, startDate, endDate,"LTE4G_NCELL_MEATIME");
		double eriTimesTotal = 0;
		//增加 混频,以此作为分母
		//double mixingSum = 0;
		Map<String, MeaTimeToMixing> cellToMixing=new HashMap<String, MeaTimeToMixing>();
		String cellId,ncellId,meaTime,mixingSum="";
		List<Map<String,String>> eriRemoveList = new ArrayList<Map<String,String>>();
		for (Map<String, String> one : eriData) {
			cellId=one.get("CELL_ID");
			ncellId=one.get("NCELL_ID");
			meaTime=one.get("MEA_TIME");
			mixingSum=one.get("MIXINGSUM");
			if(cellIdToBandType.containsKey(one.get("NCELL_ID"))) {
				//缓存需要移除E频段的lte小区
				eriRemoveList.add(one);
			} else {
				//不是E频段的lte小区，将timesTotal叠加作为分母
				eriTimesTotal += Double.parseDouble(one.get("TIMESTOTAL"));
				if(cellToMixing.get(cellId+"_"+ncellId)!=null){
					MeaTimeToMixing meaTimeToMixing=cellToMixing.get(cellId+"_"+ncellId);
					if(!meaTimeToMixing.getMeaTime().equals(meaTime)){
						String inerMixing=meaTimeToMixing.getMixingSum();
						meaTimeToMixing.setMixingSum(String.valueOf(Double.parseDouble(inerMixing)+Double.parseDouble(mixingSum)));
//						cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
					}
				}else{
					MeaTimeToMixing meaTimeToMixing=new MeaTimeToMixing();
					meaTimeToMixing.setMeaTime(meaTime);
					meaTimeToMixing.setMixingSum(mixingSum);
					cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
				}
			}
		}
		//移除E频段的lte小区
		eriData.removeAll(eriRemoveList);

		//整理ERI的MR数据成所需结果
		for (Map<String, String> one : eriData) {
			double val1 = Double.parseDouble(one.get("RSRPTIMES0"));
			double val2 = Double.parseDouble(one.get("RSRPTIMES2"));

			cellId=one.get("CELL_ID");
			ncellId=one.get("NCELL_ID");
			meaTime=one.get("MEA_TIME");
			mixingSum=one.get("MIXINGSUM");

			eriTimesTotal=Double.parseDouble(cellToMixing.get(cellId+"_"+ncellId).getMixingSum());
			if(eriTimesTotal != 0) {
				//比例1
				map = new HashMap<String, Object>();
				map.put("VAL", val1/eriTimesTotal);
				/*map.put("LNG", one.get("NCELL_LON"));
				map.put("LAT", one.get("NCELL_LAT"));
				map.put("NCELL_ID", one.get("NCELL_ID"));
				map.put("CELL_LNG", one.get("CELL_LON"));
				map.put("CELL_LAT", one.get("CELL_LAT"));*/
				map.put("LNG", one.get("CELL_LON"));
				map.put("LAT", one.get("CELL_LAT"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("CELL_LNG", one.get("NCELL_LON"));
				map.put("CELL_LAT", one.get("NCELL_LAT"));
				res1.add(map);
				//比例2
				map = new HashMap<String, Object>();
				map.put("VAL", val2/eriTimesTotal);
				/*map.put("LNG", one.get("NCELL_LON"));
				map.put("LAT", one.get("NCELL_LAT"));
				map.put("NCELL_ID", one.get("NCELL_ID"));
				map.put("CELL_LNG", one.get("CELL_LON"));
				map.put("CELL_LAT", one.get("CELL_LAT"));*/
				map.put("LNG", one.get("CELL_LON"));
				map.put("LAT", one.get("CELL_LAT"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("CELL_LNG", one.get("NCELL_LON"));
				map.put("CELL_LAT", one.get("NCELL_LAT"));
				res2.add(map);
				//@author chao.xj  2015-6-16 上午10:38:25
				//详情信息
				map = new HashMap<String, Object>();
				map.put("VAL1", val1/eriTimesTotal);
				map.put("VAL2", val2/eriTimesTotal);
				map.put("CELL_ID", one.get("NCELL_ID"));

				map.put("RSRPTIMES0", one.get("RSRPTIMES0"));
				map.put("RSRPTIMES1", one.get("RSRPTIMES1"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("DISTANCE", String.valueOf(Double.parseDouble(one.get("DISTANCE"))*1000));
				resInterDetail.add(map);
			}
		}

		/**获取中兴MR数据整理**/
		/*List<Map<String, String>> zteData = getDataFromHbase("LTE4G_ZTE_MR",
				cityId, startRow, stopRow, filterList, "MRINFO");*/
		//@author chao.xj  2015-5-27 上午11:19:09  修改
		List<Map<String, String>> zteData = query4GCellMrIndexFromHBase("LTE4G_ZTE_MR", cityId, lteCellId, startDate, endDate,"LTE4G_NCELL_MEATIME");
		double zteTimesTotal = 0;
		List<Map<String,String>> zteRemoveList = new ArrayList<Map<String,String>>();
		//清理缓存
		cellToMixing.clear();

		for (Map<String, String> one : zteData) {
			cellId=one.get("CELL_ID");
			ncellId=one.get("NCELL_ID");
			meaTime=one.get("MEA_TIME");
			mixingSum=one.get("MIXINGSUM");
			if(cellIdToBandType.containsKey(one.get("NCELL_ID"))) {
				//缓存移除E频段的lte小区
				zteRemoveList.add(one);
			} else {
				//不是E频段的lte小区，将timesTotal叠加作为分母
				zteTimesTotal += Double.parseDouble(one.get("TIMESTOTAL"));
				if(cellToMixing.get(cellId+"_"+ncellId)!=null){
					MeaTimeToMixing meaTimeToMixing=cellToMixing.get(cellId+"_"+ncellId);
					if(!meaTimeToMixing.getMeaTime().equals(meaTime)){
						String inerMeaTime=meaTimeToMixing.getMixingSum();
						meaTimeToMixing.setMixingSum(String.valueOf(Double.parseDouble(inerMeaTime)+Double.parseDouble(mixingSum)));
//						cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
					}
				}else{
					MeaTimeToMixing meaTimeToMixing=new MeaTimeToMixing();
					meaTimeToMixing.setMeaTime(meaTime);
					meaTimeToMixing.setMixingSum(mixingSum);
					cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
				}
			}
		}
		//移除E频段的lte小区
		zteData.removeAll(zteRemoveList);

		//整理ZTE的MR数据成所需结果
		for (Map<String, String> one : zteData) {
			double val1 = Double.parseDouble(one.get("RSRPTIMES0"));
			double val2 = Double.parseDouble(one.get("RSRPTIMES2"));

			cellId=one.get("CELL_ID");
			ncellId=one.get("NCELL_ID");
			meaTime=one.get("MEA_TIME");
			mixingSum=one.get("MIXINGSUM");

			zteTimesTotal=Double.parseDouble(cellToMixing.get(cellId+"_"+ncellId).getMixingSum());
			if(zteTimesTotal != 0) {
				//比例1
				map = new HashMap<String, Object>();
				map.put("VAL", val1/zteTimesTotal);
				/*map.put("LNG", one.get("NCELL_LON"));
				map.put("LAT", one.get("NCELL_LAT"));
				map.put("NCELL_ID", one.get("NCELL_ID"));
				map.put("CELL_LNG", one.get("CELL_LON"));
				map.put("CELL_LAT", one.get("CELL_LAT"));*/
				map.put("LNG", one.get("CELL_LON"));
				map.put("LAT", one.get("CELL_LAT"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("CELL_LNG", one.get("NCELL_LON"));
				map.put("CELL_LAT", one.get("NCELL_LAT"));
				res1.add(map);
				//比例2
				map = new HashMap<String, Object>();
				map.put("VAL", val2/zteTimesTotal);
				/*map.put("LNG", one.get("NCELL_LON"));
				map.put("LAT", one.get("NCELL_LAT"));
				map.put("NCELL_ID", one.get("NCELL_ID"));
				map.put("CELL_LNG", one.get("CELL_LON"));
				map.put("CELL_LAT", one.get("CELL_LAT"));*/
				map.put("LNG", one.get("CELL_LON"));
				map.put("LAT", one.get("CELL_LAT"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("CELL_LNG", one.get("NCELL_LON"));
				map.put("CELL_LAT", one.get("NCELL_LAT"));
				res2.add(map);
				//@author chao.xj  2015-6-16 上午10:38:25
				//详情信息
				map = new HashMap<String, Object>();
				map.put("VAL1", val1/zteTimesTotal);
				map.put("VAL2", val2/zteTimesTotal);
				map.put("CELL_ID", one.get("NCELL_ID"));

				map.put("RSRPTIMES0", one.get("RSRPTIMES0"));
				map.put("RSRPTIMES1", one.get("RSRPTIMES1"));
				map.put("NCELL_ID", one.get("CELL_ID"));
				map.put("DISTANCE", String.valueOf(Double.parseDouble(one.get("DISTANCE"))*1000));
				resInterDetail.add(map);
			}
		}

		Map<String,List<Map<String,Object>>> result
			= new HashMap<String, List<Map<String,Object>>>();
		result.put("res1", res1);
		result.put("res2", res2);
		result.put("resInterDetail", resInterDetail);
		return result;
	}

	/**
	 * 从Hbase查询数据
	 * @param tableName
	 * @param cityId
	 * @param startRow
	 * @param stopRow
	 * @param filterList 过滤器列，null为不过滤
	 * @param family
	 * @author peng.jm
	 * @return
	 */
	public List<Map<String, String>> getDataFromHbase(String tableName,
			long cityId, String startRow, String stopRow,
			FilterList filterList, String family) {

		List<Map<String, String>> result = new ArrayList<Map<String,String>>();

		// 从表池中取出HBASE表对象
		HTableInterface table = getHbaseTable(tableName);
		if(table == null) {
			return null;
		}
		// 获取筛选对象
		Scan scan = new Scan();
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		// 给筛选对象放入过滤器
		if(filterList != null) {
			scan.setFilter(filterList);
		}
		// 缓存1000条数据
		scan.setCaching(1000);
		scan.setCacheBlocks(false);

		ResultScanner scanner = null;
		try {

			scanner = table.getScanner(scan);
			//===============>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>增加
			int i = 0;
			List<byte[]> rowList = new LinkedList<byte[]>();
			// 遍历扫描器对象， 并将需要查询出来的数据row key取出
			for (Result r : scanner) {
				String row = Bytes.toString(r.getRow());
				System.out.println("row====>>>>>>>>>>>>>>>>"+row);
					rowList.add(Bytes.toBytes(row));
			}
			// 获取取出的row key的GET对象
			List<Get> getList = new LinkedList<Get>();
	        for (byte[] row : rowList) {
	            Get get = new Get(row);
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_ID"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_ID"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("DISTANCE"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_LON"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("CELL_LAT"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_LON"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("NCELL_LAT"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES0"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("RSRPTIMES2"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("TIMESTOTAL"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("SAMEFREQSUM"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("MIXINGSUM"));
	            get.addColumn(Bytes.toBytes("MRINFO"), Bytes.toBytes("MEA_TIME"));
	            getList.add(get);
	        }

			Result[] results = table.get(getList);
			//===============<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			Map<byte[], byte[]> fmap = null;
			Map<String, String> rmap = null;
			NavigableMap<byte[], byte[]> nmap =null;
//			for(Result r : scanner) {
				for(Result r : results) {
//				fmap = new LinkedHashMap<byte[], byte[]>();
				fmap = new TreeMap<byte[], byte[]>(Bytes.BYTES_COMPARATOR);
				fmap.putAll(r.getFamilyMap(Bytes.toBytes(family)));
//				nmap =r.getFamilyMap(Bytes.toBytes(family));

				//@author chao.xj  2015-5-29 下午4:54:40 增加 过滤采样点总数小于1000 MIXINGSUM
	            if(Double.parseDouble(Bytes.toString(fmap.get("MIXINGSUM".getBytes())))<1000){
	            	continue;
	            }
		        rmap = new LinkedHashMap<String, String>();
		        for (byte[] key : fmap.keySet()) {
		            byte[] value = fmap.get(key);

		            rmap.put(Bytes.toString(key), Bytes.toString(value));
		        }
				result.add(rmap);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("退出　getDataFromHbase　表　"+tableName+"=== "+result);
		return result;
	}

	/**
	 * 获取Hbase的table
	 * @param tableName
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
    public HTableInterface getHbaseTable(String tableName) {
        if (StringUtils.isEmpty(tableName))
            return null;
        HTable table = null;
        try {
			Configuration conf = new Configuration();
			conf = HBaseConfiguration.create(conf);
			table = new HTable(conf,tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return table;
    }

    /**
     * 通过cityId获取E频段的lte小区集
     * @param cityId
     * @return
     */
	public List<Map<String, Object>> queryLteCellWhichBandIsEByCityId(final long cityId) {
		log.debug("进入queryLteCellWhichBandIsEByCityId,cityId=" +cityId);
		final String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select * from rno_lte_cell " +
						" where area_id="+cityId+
						"     and band_type='E频段'";
				log.debug("queryLteCellWhichBandIsEByCityId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 *
	 * @title　通过表索引从Hbase查询４gMR指标数据
	 * @param tableName
	 * 表索引为LTE4G_CELL_MEATIME 或  LTE4G_NCELL_MEATIME
	 * @param cityId
	 * @param lteCellId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @author chao.xj
	 * @date 2015-5-27上午11:04:46
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> query4GCellMrIndexFromHBase(
			String dataTabName,long cityId,String lteCellId,String startDate, String endDate,String indexTabName) {

		String cell = lteCellId;
		long meaBegMillis = dateUtil.parseDateArbitrary(startDate).getTime();
		long meaEndMillis = dateUtil.parseDateArbitrary(endDate).getTime();

		//在索引表中符合日期范围的日期集合
		List<String> cellRowKeyList = new ArrayList<String>();
		//HashSet<Integer> monSet = new HashSet<Integer>();

		DateUtil dateUtil = new DateUtil();
		Date meaBegDate = dateUtil.parseDateArbitrary(startDate);
		Date meaEndDate = dateUtil.parseDateArbitrary(endDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(meaBegDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int begYear = cal.get(Calendar.YEAR);
		int begMonth = cal.get(Calendar.MONTH)+1;
		cal.setTime(meaEndDate);
		//monSet.add(cal.get(Calendar.MONTH)+1);
		int endYear = cal.get(Calendar.YEAR);
		int endMonth = cal.get(Calendar.MONTH)+1;

		HTable idxTable = null;
		Result idxRes = null;
		NavigableMap<byte[], byte[]> idxNavMap = null;
		try {
			idxTable = new HTable(HadoopXml.getHbaseConfig(), indexTabName);
			//起始日期
			Get begIdxGet = new Get(Bytes.toBytes(cityId+"_MR_"+begYear+"_"+cell));
			idxRes = idxTable.get(begIdxGet);
			if(idxRes != null && !idxRes.isEmpty()) {
				idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(begMonth+""));
				for (byte[] d : idxNavMap.keySet()) {
					//103_1423398600000_657154_38100_6_38100_18
					long da = Long.parseLong(new String(d).split("_")[1]);
					if(da>=meaBegMillis && da<=meaEndMillis) {
						cellRowKeyList.add(new String(d));
					}
				}
			}
			if(idxNavMap != null && !idxNavMap.isEmpty()) {
				idxNavMap.clear();
			}
			//判断是否在同一个月内 ， 是则不需要再获取结束日期的对应的索引，以免重复
			if(!(begYear == endYear && begMonth == endMonth)) {
				//结束日期
				Get endIdxGet = new Get(Bytes.toBytes(cityId+"_MR_"+endYear+"_"+cell));
				idxRes = idxTable.get(endIdxGet);
				if(idxRes != null && !idxRes.isEmpty()) {
					idxNavMap =  idxRes.getFamilyMap(Bytes.toBytes(endMonth+""));
					for (byte[] d : idxNavMap.keySet()) {
						//103_1423398600000_657154_38100_6_38100_18
						long da = Long.parseLong(new String(d).split("_")[1]);
						if(da>=meaBegMillis && da<=meaEndMillis) {
							cellRowKeyList.add(new String(d));
						}
					}
				}
			}

			idxTable.close();
		} catch (IOException e2) {
			e2.printStackTrace();
			try {
				idxTable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.error("LTE动态覆盖分析中，读取HBase的小区索引数据出错！");
			return Collections.emptyList();
		}
		if(cellRowKeyList.size() == 0) {
			log.error("LTE动态覆盖分析中，该小区不存在数据！");
			return Collections.emptyList();
		}

		//整理需要获取的get列
		List<Get> getList = new ArrayList<Get>();
		for (String d : cellRowKeyList) {
				Get get = new Get(Bytes.toBytes(d));
				getList.add(get);
		}

		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;

		HTable dataTab = null;
		NavigableMap<byte[], byte[]> naviMap=null;
		//System.out.println(getList.size());
		boolean flag=true;
		try {
			dataTab = new HTable(HadoopXml.getHbaseConfig(), dataTabName);
			Result[] ress = dataTab.get(getList);
			for (int i = 0; i < ress.length; i++) {

				Result res = ress[i];
				if(res.isEmpty()) {
					continue;
				}
				naviMap =  res.getFamilyMap(Bytes.toBytes("MRINFO"));
				String sKey = "";
				//排除小于100米
				double dis=Double.parseDouble(Bytes.toString(naviMap.get("DISTANCE".getBytes())));
				//混频即采样点小于１０００不参与计算，过滤
				double mixingSum=Double.parseDouble(Bytes.toString(naviMap.get("MIXINGSUM".getBytes())));
				if(dis<0.1 || mixingSum<1000){
					log.error("dis<0.1------"+dis+" 或 混频总数小于1000==="+mixingSum+"－－则不入集合！");
					continue;
				}
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				//新的　一行创建一个新的MAP对象
				map=new LinkedHashMap<String, String>();
				for(byte[] bKey : naviMap.keySet()) {

					sKey = new String(bKey,"UTF-8");
					byte[] value = naviMap.get(bKey);
					map.put(sKey, Bytes.toString(value));
				}
//				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				result.add(map);
			}
			dataTab.close();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				dataTab.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("LTE动态覆盖分析中，读取HBase数据出错！");
			return Collections.emptyList();
		}
//		System.out.println("退出query4GCellMrIndexFromHBase　result==>>>"+result);
		return result;
	}
	class MeaTimeToMixing{
		String meaTime;
		String mixingSum;
		public String getMeaTime() {
			return meaTime;
		}
		public void setMeaTime(String meaTime) {
			this.meaTime = meaTime;
		}
		public String getMixingSum() {
			return mixingSum;
		}
		public void setMixingSum(String mixingSum) {
			this.mixingSum = mixingSum;
		}

	}
	/**
	 *
	 * @title 获取lte小区形状数据通过区域 即三角形的三点坐标
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-6-15下午1:48:38
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> queryLteCellShapeDataByCityId(final long cityId) {
		log.debug("进入queryLteCellShapeDataEByCityId,cityId=" +cityId);
		final String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		final Map<String, Object> shapes=new HashMap<String, Object>();
		return hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select rlc.business_cell_id cell_id, rlc.longitude, rlc.latitude, rlcms.shape_data"
						+"  from (select business_cell_id, lte_cell_id, longitude, latitude        "
						+"          from rno_lte_cell                                              "
						+"         where area_id in("+areaStr+")) rlc                                         "
						+"  left join rno_lte_cell_map_shape rlcms                                 "
						+"    on (rlc.lte_cell_id = rlcms.lte_cell_id)                             ";
				log.debug("queryLteCellShapeDataEByCityId, sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> shapeDatas=query.list();
				if(shapeDatas!=null){
					for (int i = 0; i < shapeDatas.size(); i++) {
						shapes.put(shapeDatas.get(i).get("CELL_ID").toString(), shapeDatas.get(i).get("SHAPE_DATA"));
					}
				}
				return shapes;
			}
		});
	}
	/**
	 *
	 * @title 获取某个地市 的小区频段类型信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-6-15下午3:06:41
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, Object> queryCellBandTypeByCityId(final long cityId) {
		log.debug("进入queryCellBandTypeByCityId,cityId=" +cityId);
		final String areaStr=AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		final Map<String, Object> map = new HashMap<String, Object>();
		return hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select business_cell_id as cell_id," +
						" band_type" +
						" from rno_lte_cell where area_id in("+areaStr+")";
				log.info("queryCellAzimuthByCityId : sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> datas=query.list();
				if(datas!=null){
					for (int i = 0; i < datas.size(); i++) {
						map.put(datas.get(i).get("CELL_ID").toString(), datas.get(i).get("BAND_TYPE"));
					}
				}
				return map;
			}
		});
	}
}
