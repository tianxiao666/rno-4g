package com.iscreate.op.dao.rno;

import java.io.BufferedOutputStream;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl.SysArea;
import com.iscreate.op.service.rno.parser.vo.G4PciRec;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class Rno4GPciDaoImpl implements Rno4GPciDao {

	private static Log log = LogFactory.getLog(Rno4GPciDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	private DateUtil dateUtil=new DateUtil();
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 
	 * @title 获取符合条件的4g干扰矩阵数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:37:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long get4GInterferMartixCount(Map<String, String> condition) {
		String where = build4GInterferMartixWhere(condition);
		final String sql = "select count(MARTIX_DESC_ID) from RNO_4G_INTER_MARTIX_REC "
				+ where;
		log.info("get4GInterferMartixCount : sql=" + sql);
		// System.out.println("getNcsDescriptorCount : sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = res.longValue();
				log.info("get4GInterferMartixCount sql=" + sql + ",返回结果：" + cnt);
				return cnt;
			}
		});
	}
	/**
	 * 
	 * @title 分页获取符合条件的4g干扰矩阵的详情
	 * @param condition
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:34:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMartixByPage(
			Map<String, String> condition, int startIndex, int cnt) {
		log.info("进入方法：query4GInterferMartixByPage。condition=" + condition
				+ ",startIndex=" + startIndex + ",cnt=" + cnt);
		if (condition == null || condition.isEmpty()) {
			log.info("未传入查询条件");
		}
		if (startIndex < 0 || cnt < 0) {
			return Collections.EMPTY_LIST;
		}
		
		String where = build4GInterferMartixWhere(condition);
		
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
			          + "             			from RNO_4G_INTER_MARTIX_REC t " + where
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

		log.info("退出方法：query4GInterferMartixByPage。返回：" + res.size() + "个记录");
		return res;
	}
	/**
	 * 
	 * @title 构建4G干扰矩阵的查询条件
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午10:39:54
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private String build4GInterferMartixWhere(Map<String, String> condition) {
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
	 * 
	 * @title 分页获取Hbase的Mr数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午11:31:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryMrDataFromHbaseByPage(
			G4MrDescQueryCond cond, Page newPage) {
		String tableName,  startRow,  stopRow;
		
		tableName=cond.buildTable();
		startRow=cond.buildStartRow();
		stopRow=cond.buildStopRow();
		
		List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
		ResultScanner scanner = null;
		// 为分页创建的封装类对象，下面有给出具体属性
		try {
			// 获取最大返回结果数量
			if (newPage.getPageSize() == 0)
				newPage.setPageSize(25);
			if (newPage.getCurrentPage() == 0)
				newPage.setCurrentPage(1);
			// 计算起始页和结束页
			Integer firstPage = (newPage.getCurrentPage() - 1) * newPage.getPageSize();
			Integer endPage = firstPage + newPage.getPageSize();
			// 从表池中取出HBASE表对象
			HTableInterface table = getHbaseTable(tableName);
			if(table == null) {
				return null;
			}
			// 获取筛选对象
			Scan scan = new Scan();
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(stopRow));
			/*// 给筛选对象放入过滤器(true标识分页,具体方法在下面)
			if(getMrDescFilters(true,cond)!=null){
				scan.setFilter(getMrDescFilters(true,cond));
			}*/
			// 缓存1000条数据
			scan.setCaching(1000);
			scan.setCacheBlocks(false);
			scanner = table.getScanner(scan);
			int i = 0;
			List<byte[]> rowList = new LinkedList<byte[]>();
			// 遍历扫描器对象， 并将需要查询出来的数据row key取出
			for (Result r : scanner) {
				String row = Bytes.toString(r.getRow());
				if (i >= firstPage && i < endPage) {
					rowList.add(Bytes.toBytes(row));
				}
				i++;
			}
			// 获取取出的row key的GET对象
			List<Get> getList = new LinkedList<Get>();
	        for (byte[] row : rowList) {
	            Get get = new Get(row);
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
	            getList.add(get);
	        }

			Result[] results = table.get(getList);
			// 遍历结果
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
				
		        Map<String, String> rmap = new LinkedHashMap<String, String>();
		        for (byte[] key : fmap.keySet()) {
		            byte[] value = fmap.get(key);
		            rmap.put(Bytes.toString(key), Bytes.toString(value));
		        }
				mapList.add(rmap);
			}
			
			// 封装分页对象
			newPage.setTotalCnt(i);
			
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
	 * @title 获取Hbase的table
	 * @param tableName
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15上午11:30:03
	 * @company 怡创科技
	 * @version 2.0.1
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
	 * 
	 * @title 检查这周是否计算过4G MR干扰矩阵
	 * @param cityId
	 * @param thisMonday
	 * @param today
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午1:57:05
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> check4GInterMartixThisWeek(long cityId,
			String thisMonday, String today) {
		log.info("进入方法check4GInterMartixThisWeek。areaId=" + cityId +
				",thisMonday=" + thisMonday + ",today=" + today);
		final String sql = " select  to_char(t.create_date, 'yyyy-MM-dd HH24:mi:ss') as CREATE_DATE " +
				" from RNO_4G_INTER_MARTIX_REC  t " +
				" where t.CITY_ID = "+ cityId +
				" and t.CREATE_DATE <= to_date('"+ today +"','yyyy-MM-dd HH24:mi:ss') " +
				" and t.CREATE_DATE >= to_date('"+ thisMonday +"','yyyy-MM-dd HH24:mi:ss') " +
				" and t.WORK_STATUS = '计算完成' and t.STATUS = 'Y' " +
				" order by t.create_date desc";
		
		log.info("检查这周是否计算过 4G MR干扰矩阵: sql=" + sql);
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
	 * 
	 * @title 获取seq的val值
	 * @param seq
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:32:50
	 * @company 怡创科技
	 * @version 2.0.1
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
	 * 
	 * @title 根据条件获取MR的数据记录总量
	 * @param cityId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:48:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryMrDataRecordsCount(long cityId, String startTime,
			String endTime) {
		log.info("进入方法queryNcsDataRecordsCount。cityId="+cityId
				+",startTime="+startTime+",endTime="+endTime);
		
		String tableName,  startRow,  stopRow;
		long num=0;
		DateUtil dateUtil=new DateUtil();
		tableName="LTE4G_MR_DESC";
		long sMill = dateUtil.parseDateArbitrary(startTime).getTime();
		startRow=cityId+"_"+sMill+"_#";
		long eMill = dateUtil.parseDateArbitrary(endTime).getTime();
		stopRow = cityId+"_"+eMill+"_~";
		
		List<Map<String, String>> mapList = new LinkedList<Map<String, String>>();
		ResultScanner scanner = null;
		// 为分页创建的封装类对象，下面有给出具体属性
		try {
			
			// 从表池中取出HBASE表对象
			HTableInterface table = getHbaseTable(tableName);
			if(table == null) {
				return 0;
			}
			// 获取筛选对象
			Scan scan = new Scan();
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(stopRow));
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
//	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CITY_ID"));
//	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MEA_TIME"));
	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("RECORD_COUNT"));
//	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("FACTORY"));
//	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("CREATE_TIME"));
//	            get.addColumn(Bytes.toBytes("DESCINFO"), Bytes.toBytes("MOD_TIME"));
	            getList.add(get);
	        }

			Result[] results = table.get(getList);
			// 遍历结果
			for (Result r : results) {
				Map<byte[], byte[]> fmap = new LinkedHashMap<byte[], byte[]>();
				fmap.putAll(r.getFamilyMap(Bytes.toBytes("DESCINFO")));
				
		        Map<String, String> rmap = new LinkedHashMap<String, String>();
		        for (byte[] key : fmap.keySet()) {
		            byte[] value = fmap.get(key);
		            if(Bytes.toString(key).equals("RECORD_COUNT")){
		            	num+=Long.parseLong(Bytes.toString(value));
		            }
		        }
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return num;
	}
	/**
	 * 
	 * @title 创建MR 4g 干扰矩阵计算任务
	 * @param interMartix
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午2:52:26
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean createMr4GInterMartixRec(Map<String, Object> interMartix) {
		log.info("进入方法createMr4GInterMartixRec。interMartix="+interMartix);

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
		String fileName = interMartix.get("fileName").toString();
		String taskName = interMartix.get("taskName").toString();
		final String sql = "insert into RNO_4G_INTER_MARTIX_REC (" +
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
											"STATUS,FILE_NAME,TASK_NAME) " +
					" values(" + martixRecId + "," + cityId 
							+ ", to_date('" + createDate + "', 'yyyy-MM-dd HH24:mi:ss')" 
							+ ", to_date('" + startMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+ ", to_date('" + endMeaDate + "', 'yyyy-MM-dd HH24:mi:ss')"
							+", " + recordNum + ",'" + type + "','" + workStatus 
							+ "','" + filePath + "'," + jobId + ",'" + status + "','" + fileName + "','"+taskName+"')";
		
		log.info("createMr4GInterMartixRec的sql:"+sql);
		return hibernateTemplate
		.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				int res = query.executeUpdate();
				log.info("退出createMr4GInterMartixRec,受影响行数="+res);
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
	 * 
	 * @title 获取符合条件的mr文件数量
	 * @param condition
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午3:03:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getMrDataCount(Map<String, String> condition) {
		log.info("进入方法getMrDataCount。condition="+condition);
		
		long cityId = Long.parseLong(condition.get("cityId").toString());
		String startTime = condition.get("begTime").toString();
		String endTime = condition.get("endTime").toString();
		
		return queryMrDataRecordsCount(cityId, startTime,
				endTime);
	}
	/**
	 * 
	 * @title 通过statement更新4g干扰矩阵的记录状态
	 * @param stmt
	 * @param martixRecId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午4:19:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean update4GInterMatrixWorkStatusByStmt(Statement stmt, long martixRecId, String workStatus) {
		final String sql = "update RNO_4G_INTER_MARTIX_REC t set t.work_status='"
				+ workStatus + "' " + " where t.martix_desc_id=" + martixRecId;
		log.info("update4GInterMatrixWorkStatusByStmt的sql:" + sql);
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
	 * @title 通过jobId获取4g 干扰矩阵的记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午4:20:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GInterferMatrixRecByJobId(Statement stmt, long jobId) {
		log.info("进入方法query4GInterferMatrixRecByJobId。jobId="+jobId);	
		
		String sql = "select MARTIX_DESC_ID,CITY_ID,START_MEA_DATE,END_MEA_DATE,FILE_PATH,FILE_NAME from RNO_4G_INTER_MARTIX_REC where JOB_ID ="+jobId;
		
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
				map.put("FILE_NAME", rs.getString(6));
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
		log.info("退出方法：query4GInterferMatrixRecByJobId。返回：" + list.size() + "个记录");
		return list;
	}
	/**
	 * 
	 * @title 获取lte MR测量数据的结果集
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午5:12:04
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
	 * @title MR子关联度
	 * @param results
	 * @param numerator
	 * @return
	 * @author chao.xj
	 * @date 2015-4-15下午5:24:53
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
	 * @title 保存原始pci优化 4G 干扰矩阵文件到hdfs文件系统上
	 * @param filePath
	 * @param sumMrRelaDegre
	 * @author chao.xj
	 * @date 2015-4-15下午5:32:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void save4GInterferMatrixInHdfs(String filePath, Map<Integer, List<G4PciRec>> sumMrRelaDegre) {
		String realFilePath = filePath;

		FileSystem fs = null;
		try {
			Configuration conf = new YarnConfiguration();
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			log.error("save4GInterferMatrixInHdfs过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}
		//先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if(fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			log.error("save4GInterferMatrixInHdfs过程：保存文件时，删除原有文件出错！");
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
					//line=cell+"##"+ncellId+","+cosi+";"+cells+";"+cellPci+";"+ncellPci;
					//CELL##NCELL,COSI;
					line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci;;
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
	 * 获取最近十次lte干扰矩阵信息
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> getLatelyLteMatrixByCityId(final long cityId) {
		return hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select martix_desc_id,create_date,task_name,job_id from ( "+
									"	select martix_desc_id," +
											 " to_char( create_date,'yyyy-MM-dd HH24:mi:ss') as create_date," +
											 " task_name," +
											 " job_id "+
									"	  from rno_4g_inter_martix_rec "+
									"	 where city_id = "+cityId+
									"		and work_status='计算完成' "+
									"	 order by create_date desc) "+
									"	 where rownum <= 10 ";
						log.debug("getLatelyLteMatrixByCityId ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}


	/**
	 * 通过job_id获取干扰矩阵
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> getLteMatrixById(final long jobId) {
		return hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select * from rno_4g_inter_martix_rec where job_id ="+jobId;
						log.debug("getLteMatrixById ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}


	/**
	 * 获取同站lte小区和pci
	 * @title 
	 * @return
	 * @author peng.jm
	 * @date 2015年4月13日18:32:31
	 * @company 怡创科技
	 */
	public List<Map<String, String>> getSameStationCellsByLteCellId(
			final String lteCell) {
		//获取服务小区信息
		List<Map<String, Object>> serverCellDetail = hibernateTemplate
		.execute(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql1 = "select * from rno_lte_cell where business_cell_id="+lteCell;
				log.debug("getSameStationCellsByLteCellId,sql1=" + sql1);
				SQLQuery query = arg0.createSQLQuery(sql1);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> rows = query.list();
				return rows;
			}
		});
		if(serverCellDetail.size() <= 0) {
			return Collections.emptyList();
		}
		String cellName = serverCellDetail.get(0).get("BUSINESS_CELL_ID").toString();
		String cellPci = serverCellDetail.get(0).get("PCI").toString();
		final String cellENodeBId = serverCellDetail.get(0).get("ENODEB_ID").toString();
		//获取同站小区信息
		List<Map<String, Object>> cellsDetail = hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql2 = "select * from rno_lte_cell where enodeb_id="
										+cellENodeBId+" and business_cell_id <>"+lteCell;
						log.debug("getSameStationCellsByLteCellId,sql2=" + sql2);
						SQLQuery query = arg0.createSQLQuery(sql2);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
		//整理结果
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String,String> one = new HashMap<String, String>();
		one.put("CELL", cellName);
		one.put("PCI", cellPci);
		result.add(one);
		for (Map<String, Object> map : cellsDetail) {
			one = new HashMap<String, String>();
			cellName = map.get("BUSINESS_CELL_ID").toString();
			cellPci = map.get("PCI").toString();
			one.put("CELL", cellName);
			one.put("PCI", cellPci);
			result.add(one);
		}
		
		return result;
	}


	/**
	 * 转换lte小区与某同站小区的pci
	 * @param cell1
	 * @param pci1
	 * @param cell2
	 * @param pci2
	 * @return
	 */
	public boolean changeLteCellPci(final String cell1, final String pci1, final String cell2,
			final String pci2) {
		return hibernateTemplate
			.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql1 = "update rno_lte_cell set pci='"+pci1
							+"' where business_cell_id='"+cell1+"'"; 
					log.debug("changeLteCellPci.sql1="+sql1);
					String sql2 =  "update rno_lte_cell set pci='"+pci2
							+"' where business_cell_id='"+cell2+"'"; 
					log.debug("changeLteCellPci.sql2="+sql2);
					SQLQuery query1 = arg0.createSQLQuery(sql1);
					int resCnt1 = query1.executeUpdate();
					SQLQuery query2 = arg0.createSQLQuery(sql2);
					int resCnt2 = query2.executeUpdate();
					if (resCnt1 > 0 && resCnt2 > 0) {
						return true;
					} else {
						return false;
					} 
				}
			});
	}
	/**
	 * 
	 * @title 统计4g方位角任务数量
	 * @param cond
	 * @param account
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:32:04
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long get4GAzimuthCalcTaskCount(Map<String, String> cond, String account) {
		log.info("进入方法：get4GAzimuthCalcTaskCount。condition=" + cond + ",account="+account);	
		
		String where = buildStructureAnalysisTaskWhere(cond, account);
		final String sql = "select count(*) "
					+"  from RNO_4G_AZIMUTH_JOB sjob " 
					+" left join rno_job job on job.job_id = sjob.job_id  " + where ;
		log.info("获取4g方位角任务的总数 : sql=" + sql);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				BigDecimal res = (BigDecimal) query.uniqueResult();
				long cnt = 0;
				if(res != null)
					cnt = res.longValue();
				log.info("获取4g方位角任务的总数,返回结果：" + cnt);
				return cnt;
			}
		});
	}
	/**
	 * 
	 * @title 分页获取4g方位角计算任务信息
	 * @param cond
	 * @param account
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:36:43
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthCalcTaskByPage(
			Map<String, String> cond, String account, int startIndex, int cnt) {
		log.info("进入方法：query4GAzimuthCalcTaskByPage。condition=" + cond + ",account="+account
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
			+"	                       to_char(job.launch_time,'yyyy-MM-dd HH24:mi:ss') as launch_time,   "
			+"                         to_char(job.complete_time,'yyyy-MM-dd HH24:mi:ss') as complete_time  "
			+"                      from RNO_4G_AZIMUTH_JOB sjob   "
			+"                      left join rno_job job on job.job_id = sjob.job_id   "
									+ where + ") t1   "
			+"             where rownum <= " + (cnt + startIndex) + " ) t2   "
			+"      where t2.rn > " + startIndex;
		
		log.info("分页获取4g方位角计算任务信息 : sql=" + sql);
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
		log.info("退出方法：query4GAzimuthCalcTaskByPage。返回：" + res.size() + "个记录");
		return res;
	}
	/**
	 * 
	 * @title 通过区域ID获取LTE小区标识对应的小区信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午3:36:43
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
				 +"		 t1.azimuth,						"
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
		String azimuth="";
		for (Map<String, Object> map : lteCells) {
//			enodebId=Integer.parseInt(map.get("ENODEB_ID").toString());
			cellId=map.get("CELL").toString();
			pci=map.get("PCI").toString();
			cellName=map.get("NAME").toString();
			earfcn=map.get("EARFCN").toString();
			azimuth=map.get("AZIMUTH").toString();
			cellToCells.put(cellId, Arrays.asList(cellName,pci,azimuth));
		}
		log.debug("退出getLteCellInfoByCellId(Statement stmt,long cityId)"+cellToCells.size());
		return cellToCells;
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
	/**
	 * 
	 * @title 通过jobId获取4g方位角记录任务信息
	 * @param stmt
	 * @param jobId
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午4:44:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> query4GAzimuthJobRecByJobId(Statement stmt, long jobId) {
		log.info("进入方法query4GAzimuthJobRecByJobId。jobId="+jobId);	
		
		String sql = "select city_id,beg_mea_time,end_mea_time,result_dir,RELA_NUM_TYPE,rd_file_name from RNO_4G_AZIMUTH_JOB where JOB_ID ="+jobId;
		
		log.info("query4GAzimuthJobRecByJobId : sql=" + sql);
		
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
				map.put("RELA_NUM_TYPE", rs.getString("RELA_NUM_TYPE"));
				map.put("RD_FILE_NAME", rs.getString("RD_FILE_NAME"));
				//获取Clob数据
				/*clob = rs.getClob("OPTIMIZE_CELLS");
				inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				inStream.read(c);
				optimizeCells = new String(c);
				map.put("OPTIMIZE_CELLS", optimizeCells);*/
				
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*try {
				inStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("退出方法：query4GAzimuthJobRecByJobId。返回：" + list.size() + "个记录");
		return list;
	}
	/**
	 * 
	 * @title 通过jobid更新4g方位角计算的job状态
	 * @param stmt
	 * @param jobId
	 * @param workStatus
	 * @return
	 * @author chao.xj
	 * @date 2015-4-29下午5:06:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean update4GAzimuthCalcWorkStatusByStmt(Statement stmt, long jobId, String workStatus) {
		final String sql = "update RNO_4G_AZIMUTH_JOB t set t.finish_state='"
				+ workStatus + "' " + " where t.job_id=" + jobId;
		log.info("update4GAzimuthCalcWorkStatusByStmt的sql:" + sql);
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
		List<Map<String, Object>> lteCells=RnoHelper.commonQuery(stmt, sql);
		if(lteCells==null || lteCells.size()==0){
			return null;
		}
		String enodebId="",earfcn="";
		String cellId="";
		for (Map<String, Object> map : lteCells) {
			enodebId=map.get("ENODEB_ID").toString();
			earfcn=map.get("EARFCN").toString();
			cellId=map.get("CELL").toString();
			
			if(enodebToCells.get(enodebId+"_"+earfcn)!=null){
				enodebToCells.put(enodebId+"_"+earfcn, enodebToCells.get(enodebId+"_"+earfcn)+"#"+cellId);
			}else{
				enodebToCells.put(enodebId+"_"+earfcn, cellId);
			}
		}
		return enodebToCells;
	}
	/**
	 * 
	 * @title 获取某个地市 的小区方位角信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-5-11下午1:55:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> queryCellAzimuthByCityId(
			Statement stmt, long cityId) {
		log.info("进入方法queryCellAzimuthByCityId。cityId="+cityId);	
		
		String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);
		
		String sql = "select business_cell_id as cell_id," +
							" azimuth" +
							" from rno_lte_cell where area_id in("+areaIdStr+")";
		log.info("queryCellsByCityId : sql=" + sql);
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				map.put( rs.getString("CELL_ID"), rs.getString("AZIMUTH"));
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
		log.info("退出方法：queryCellAzimuthByCityId。返回：" + map.size() + "个记录");
		return map;
	}
	/**
	 * 
	 * @title 获取某个地市 的小区频段类型信息
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-5-11下午1:55:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<String, String> queryCellBandTypeByCityId(
			Statement stmt, long cityId) {
		log.info("进入方法queryCellBandTypeByCityId。cityId="+cityId);	
		
		String areaIdStr = AuthDsDataDaoImpl
				.getSubAreaAndSelfIdListStrByParentId(cityId);
		
		String sql = "select business_cell_id as cell_id," +
							" band_type" +
							" from rno_lte_cell where area_id in("+areaIdStr+")";
		log.info("queryCellsByCityId : sql=" + sql);
		ResultSet rs = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				map.put(rs.getString("CELL_ID"), rs.getString("BAND_TYPE"));
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
		log.info("退出方法：queryCellBandTypeByCityId。返回：" + map.size() + "个记录");
		return map;
	}
}
