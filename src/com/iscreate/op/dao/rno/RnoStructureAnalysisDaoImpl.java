package com.iscreate.op.dao.rno;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
import com.iscreate.op.action.rno.vo.Threshold;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.CellFreqInterfer;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.pojo.rno.RnoNcs;
import com.iscreate.op.pojo.rno.RnoNcsCell;
import com.iscreate.op.pojo.rno.RnoNcsDescriptor;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.task.ExecutorManager;
import com.iscreate.op.service.rno.tool.DumpHelper;
import com.iscreate.op.service.rno.tool.FileTool;
import com.iscreate.op.service.rno.tool.HadoopXml;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class RnoStructureAnalysisDaoImpl implements RnoStructureAnalysisDao {

	private static Log log = LogFactory
			.getLog(RnoStructureAnalysisDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;
	// 定义私有成员变量
	// private ResultSet rs = null;
	// private PreparedStatement ps = null;
	// private Statement sm = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private RnoTaskDao rnoTaskDao;//注入
	
	public RnoTaskDao getRnoTaskDao() {
		return rnoTaskDao;
	}

	public void setRnoTaskDao(RnoTaskDao rnoTaskDao) {
		this.rnoTaskDao = rnoTaskDao;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * @title 计算簇约束因子
	 * @param conn
	 * @param areaId
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15下午03:22:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<Long, Boolean> calculateClusterConstrain(Statement stmt,
			String clusterTab, String clusterCellTab, List<Long> ncsIds) {
		log.debug("calculateClusterConstrain(Connection conn,long areaId, String clusterTab, String clusterCellTab,List<Long> ncsIds)"
				+ stmt + " " + clusterTab + " " + clusterCellTab + " " + ncsIds);
		// TODO Auto-generated method stub
		/*
		 * String sql=
		 * "UPDATE RNO_NCS_CLUSTER SET CONTROL_FACTOR=(select (LENGTH(REGEXP_REPLACE(REPLACE(t.tch, ',', '@'),  '[^@]+',  ''))+1)/(SELECT CASE FREQ_SECTION WHEN 'GSM900' THEN '95' WHEN 'GSM1800' THEN '374' ELSE NULL END availtchfreqpoint from RNO_NCS_DESCRIPTOR WHERE RNO_NCS_DESC_ID=? ) clusterconstraintfactor from (SELECT WMSYS.WM_CONCAT(TCH) tch from CELL WHERE LABEL IN (SELECT CELL from RNO_NCS_CLUSTER_CELL WHERE CLUSTER_ID IN(?)))  t) WHERE ID IN (?)"
		 * ; try { ps=conn.prepareStatement(sql); } catch (SQLException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		Map<Long, Boolean> ncsidtobool = new HashMap<Long, Boolean>();

		// Statement stmt = null;
		// try {
		// stmt = conn.createStatement();
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// return null;
		// }

		for (int i = 0; i < ncsIds.size(); i++) {
			String ncsidString = ncsIds.get(i).toString();
			// System.out.println("ncsidString:"+ncsidString);
			List<Long> clusteridList = this.getClusterIdsByNcsDescId(stmt,
					Long.parseLong(ncsidString), clusterTab);
			String sql = "";
			// for (int j = 0; j < clusteridList.size(); j++) {
			// String clusteridString = clusteridList.get(j).toString();
			try {
				// String
				// sql="UPDATE RNO_NCS_CLUSTER SET CONTROL_FACTOR=(select (LENGTH(REGEXP_REPLACE(REPLACE(t.tch, ',', '@'),  '[^@]+',  ''))+1)/(SELECT CASE FREQ_SECTION WHEN 'GSM900' THEN '95' WHEN 'GSM1800' THEN '374' ELSE NULL END availtchfreqpoint from RNO_NCS_DESCRIPTOR WHERE RNO_NCS_DESC_ID="+ncsidString+" ) clusterconstraintfactor from (SELECT WMSYS.WM_CONCAT(TCH) tch from CELL WHERE LABEL IN (SELECT CELL from RNO_NCS_CLUSTER_CELL WHERE CLUSTER_ID IN("+clusteridString+")))  t) WHERE ID IN ("+clusteridString+")";
				// String sql = "UPDATE "
				// + clusterTab
				// +
				// " SET CONTROL_FACTOR=(select (LENGTH(REGEXP_REPLACE(REPLACE(t.tch, ',', '@'),  '[^@]+',  ''))+1)/(SELECT CASE FREQ_SECTION WHEN 'GSM900' THEN '"
				// + RnoConstant.BusinessConstant.GSM900_FEQ_CNT
				// + "' WHEN 'GSM1800' THEN '"
				// + RnoConstant.BusinessConstant.GSM1800_FEQ_CNT
				// +
				// "' ELSE NULL END availtchfreqpoint from RNO_NCS_DESCRIPTOR WHERE RNO_NCS_DESC_ID="
				// + ncsidString
				// +
				// " ) clusterconstraintfactor from (SELECT WMSYS.WM_CONCAT(TCH) tch from CELL WHERE LABEL IN (SELECT CELL from "
				// + clusterCellTab + " WHERE CLUSTER_ID IN("
				// + clusteridString + ")))  t) WHERE ID IN ("
				// + clusteridString + ")";

				sql = "merge into "
						+ clusterTab
						+ " tar using "
						+ "("
						+ " SELECT mid1.cluster_id,mid1.totalcnt/mid2.section as factor  ,mid2.freq_type from"
						+ "("
						+ " select cluster_id,sum(freq_cnt) as totalcnt  from "
						+ clusterCellTab
						+ " where CLUSTER_ID in (select id from "
						+ clusterTab
						+ " where RNO_NCS_desc_id="
						+ ncsidString
						+ ") group by cluster_id"
						+ " )mid1"
						+ " inner JOIN "
						+ " ( "

						+ "select CLUSTER_ID,SECTION,FREQ_TYPE "
						+ " from "
						+ " ("
						+ " select cluster_id,section ,FREQ_TYPE,row_number() over(partition by cluster_id order by  count(*)  desc nulls last) as seq "// --count(*)
																																						// as
																																						// cnt
																																						// FROM
						+ " from "
						+ " ( "
						+ " select mid5.*,(case when cell.bcch>100 then 124 else 95 end) as section ,(case when cell.bcch>100 then 'GSM1800' else 'GSM900' end) as FREQ_TYPE "
						+ " FROM "
						+ "( "
						+ " select cluster_id,cell from "
						+ clusterCellTab
						+ " where CLUSTER_ID in (select id from "
						+ clusterTab
						+ " where RNO_NCS_desc_id="
						+ ncsidString
						+ ") "
						+ " )mid5 "
						+ " inner join cell on mid5.cell=cell.label order by cluster_id "
						+ " ) "
						+ " GROUP BY cluster_id,section,FREQ_TYPE "
						+ " ) "
						+ " where seq=1 "

						// + " select cluster_id,avg(section) section FROM "
						// + " ( "
						// +
						// " select mid5.*,(case when cell.bcch>100 then 124 else 95 end) as section FROM "
						// + " ( "
						// + " select cluster_id,cell from "
						// + clusterCellTab
						// + " where CLUSTER_ID in (select id from "
						// + clusterTab
						// + " where RNO_NCS_desc_id="
						// + ncsidString
						// + ") "
						// + " )mid5 "
						// +
						// " inner join cell on mid5.cell=cell.label order by cluster_id "
						// + " ) "
						// + " GROUP BY cluster_id "

						+ " )mid2 "
						+ " on(mid1.cluster_id=mid2.cluster_id) "
						+ " ) src "
						+ " on (tar.id=src.cluster_id) when matched then update set tar.control_factor=SRC.factor,tar.FREQ_SECTION=src.freq_type";
				log.debug("计算簇约束因子的sql=" + sql);
				// ps = conn.prepareStatement(sql);
				// /*
				// * ps.setString(1, ncsidString); ps.setString(2,
				// * clusteridString); ps.setString(3, clusteridString);
				// */
				// // ps.addBatch();
				// ps.executeUpdate();
				stmt.executeUpdate(sql);
				ncsidtobool.put(Long.parseLong(ncsidString), true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				ncsidtobool.put(Long.parseLong(ncsidString), false);
				e.printStackTrace();
			} finally {
				// this.resourcesClose();
			}
			// }
		}
		/*
		 * try { int[] res=ps.executeBatch(); log.debug("执行批处理res:"+res); }
		 * catch (Exception e) { // TODO: handle exception e.printStackTrace();
		 * }finally{ this.resourcesClose(); }
		 */
		log.debug("退出calculateClusterConstrain ncsidtobool:" + ncsidtobool);

		return ncsidtobool;
	}

	/**
	 * 
	 * @title 通过NCS描述ID取得簇ID集合信息 getClusterIdsByNcsDescId
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15下午04:45:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Long> getClusterIdsByNcsDescId(Statement stmt,
			final long ncsId, String clusterTab) {
		log.debug("进入getClusterIdsByNcsDescId(final List<Long> ncsId)" + ncsId
				+ " stmt:" + stmt);

		// TODO Auto-generated method stub
		List<Long> clusteridList = new ArrayList<Long>();
		// String
		// sql="SELECT ID from RNO_NCS_CLUSTER WHERE RNO_NCS_DESC_ID in("+ncsId+")";
		String sql = "SELECT ID from " + clusterTab
				+ " WHERE RNO_NCS_DESC_ID in(" + ncsId + ")";
		ResultSet rs = null;
		try {
			// ps = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				clusteridList.add(rs.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// this.resourcesClose();
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		log.debug("退出getClusterIdsByNcsDescId clusteridList:" + clusteridList);
		return clusteridList;

	}

	/**
	 * 
	 * @title 获取ncsid及areaid字符串
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15下午03:49:14
	 * @company 怡创科技
	 * @version 1.2
	 */
	/*
	 * public Map<String, Object> getNcsIdAndAreaIdString(Connection conn,
	 * List<Long> ncsIds) { Map<String, Object> map=new HashMap<String,
	 * Object>(); List<Long> areaIdsList=this.getAreaIdsByNcsDescId(conn,
	 * ncsIds); // System.out.println("areaIdsList:"+areaIdsList.size());//ok
	 * String areaidString=""; for (int i = 0; i < areaIdsList.size(); i++) { if
	 * (i==0) { areaidString=areaIdsList.get(i).toString(); }else if
	 * (areaIdsList.size()>1) { areaidString+=","+areaIdsList.get(i).toString();
	 * } } String ncsidString="";
	 * 
	 * for (int i = 0; i < ncsIds.size(); i++) { if (i==0) {
	 * ncsidString=ncsIds.get(i).toString(); }else if (ncsIds.size()>1) {
	 * ncsidString+=","+ncsIds.get(i).toString(); } } map.put("areaId",
	 * areaIdsList); map.put("ncsId", ncsidString); return map; }
	 */

	/**
	 * 
	 * @title 计算干扰度
	 * @param conn
	 * @param tabName
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-14上午11:00:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean calculateInterfer(Statement stmt, String tabName,
			List<Long> ncsIds, String ciThreshold, String caThreshold) {
		// TODO Auto-generated method stub
		log.debug("calculateInterfer(Statement stmt:" + stmt + " tabName:"
				+ tabName + " ncsIds:" + ncsIds);
		List<RnoNcsDescriptor> rnoncsdescList = this
				.getRelssFiledRecordListsByNcsIds(stmt, ncsIds);
		// System.out.println("rnoncsdescList:"+rnoncsdescList.size());
		boolean flag = updateNcsInterfer(stmt, rnoncsdescList, ciThreshold,
				tabName, "INTERFER", "CI_DIVIDER");
		log.debug("计算C/I，结果：" + flag);
		// 2014-7-11 gmh将参数+9改为+3
		flag = updateNcsInterfer(stmt, rnoncsdescList, caThreshold, tabName,
				"CA_INTERFER", "CA_DIVIDER");
		log.debug("计算C/A，结果：" + flag);

		// ---2014-3-19---需要判断主小区与邻小区是否是同频段，是同频段，才有可能干扰---//
		rectifyDiffSectionCellsInterfer(stmt, tabName, ncsIds);
		// System.out.println("flag:"+flag);
		return flag;
	}

	/**
	 * 去除不同频段的干扰值，因为不会有干扰
	 * 
	 * @param conn
	 * @param tabName
	 * @param ncsIds
	 * @author brightming 2014-3-19 下午6:29:19
	 */
	private void rectifyDiffSectionCellsInterfer(Statement stmt,
			String tabName, List<Long> ncsIds) {

		log.debug("进入方法：rectifyDiffSectionCellsInterfer。stmt=" + stmt
				+ ",tabname=" + tabName + ",ncsIds=" + ncsIds);
		if (stmt == null || tabName == null || "".equals(tabName.trim())
				|| ncsIds == null || ncsIds.isEmpty()) {
			return;
		}
		// Statement stmt = null;
		try {
			String ids = "";
			for (long id : ncsIds) {
				ids += id + ",";
			}
			ids = ids.substring(0, ids.length() - 1);
			// stmt = conn.createStatement();
			String sql = "merge into "
					+ tabName
					+ " tar using ( "
					+ " select * FROM ( "
					+ " select ncs.*,CELL.bcch from ( "
					+ " select RNO_NCS_DESC_ID,cell,ncell,avg(arfcn) as arfcn from "
					+ tabName
					+ " where RNO_NCS_DESC_ID in ("
					+ ids
					+ ") group by RNO_NCS_DESC_ID,cell,ncell "
					+ " ) ncs "
					+ " left join "
					+ " ( "
					+ " SELECT * FROM( "
					+ " select distinct(label) as label,bcch ,row_number() over(partition by label order by label) as seq from cell "
					+ " ) where seq=1 "
					+ " )cell "
					+ " on (NCS.cell=CELL.label) "
					+ " ) where arfcn>=100 and bcch<100 or arfcn<=100 and bcch>100 ) src "
					+ " on (tar.rno_ncs_desc_id=src.rno_ncs_desc_id and tar.cell=src.cell and tar.ncell=src.ncell) "
					+ " when matched then update set tar.ci_divider=0 ,tar.ca_divider=0 ,tar.interfer=0,tar.ca_interfer=0";

			log.debug("开始执行去除非同频段的小区的干扰系数，sql=" + sql);
			stmt.executeUpdate(sql);
			log.debug("去除非同频段的小区的干扰系数执行完成。");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
	}

	/**
	 * 
	 * @title 更新NCS干扰度
	 * @param conn
	 * @param rnoncsdescList
	 * @param RELSS
	 * @param tabName
	 *            ncs测量表名
	 * @param interResultFieldName
	 *            存储干扰结果字段
	 * @param interDividerFieldName
	 *            存储计算干扰结果的分子值的列名称。（因为分子是要通过遍历ncs描述表才能确定的）
	 * @return
	 * @author chao.xj
	 * @date 2014-1-14下午03:03:14
	 * @company 怡创科技
	 * @version 1.2
	 */
	private boolean updateNcsInterfer(Statement stmt,
			List<RnoNcsDescriptor> rnoncsdescList, String RELSS,
			String tabName, String interResultFieldName,
			String interDividerFieldName) {
		log.debug("进入updateNcsInterfer(Statement stmt,List<RnoNcsDescriptor> rnoncsdescList,String RELSS)"
				+ stmt + " " + rnoncsdescList + " " + RELSS);
		boolean flag = true;
		/*
		 * try { conn.setAutoCommit(false); } catch (SQLException e1) {
		 * e1.printStackTrace(); return flag; }
		 */
		String relss = "";
		String relsscons = RELSS;// 传入相对信号长度值
		String TIMESRELSS = "";

		String sql = "";
		// Statement stmt = null;
		// try {
		// stmt = conn.createStatement();
		// } catch (SQLException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// return false;
		// }

		for (int i = 0; i < rnoncsdescList.size(); i++) {

			TIMESRELSS = getTimesRelssXByValue(rnoncsdescList.get(i), RELSS);
			log.debug("获取[" + RELSS + "]对应的列为：" + TIMESRELSS);
			if (TIMESRELSS == null || "".equals(TIMESRELSS)) {
				log.debug("ncs[" + rnoncsdescList.get(i).getRnoNcsDescId()
						+ "]未获取到相应的[" + RELSS + "]对应的列，将尝试用+0获取");
				TIMESRELSS = getTimesRelssXByValue(rnoncsdescList.get(i), "+0");
				log.debug("获取[+0]对应的列为：" + TIMESRELSS);
			}
			long descid = rnoncsdescList.get(i).getRnoNcsDescId();

			// relss = (rnoncsdescList.get(i).getRelssSign() == 0 ? "+" : "-")
			// + rnoncsdescList.get(i).getRelss();
			// // System.out.println("relss:"+relss);
			// if (relsscons.equals(relss)) {
			// //
			// TIMESRELSS = "TIMESRELSS";
			// } else {
			// relss = (rnoncsdescList.get(i).getRelss2Sign() == 0 ? "+" : "-")
			// + rnoncsdescList.get(i).getRelss2();
			// if (relsscons.equals(relss)) {
			// //
			// TIMESRELSS = "TIMESRELSS2";
			// } else {
			// relss = (rnoncsdescList.get(i).getRelss3Sign() == 0 ? "+"
			// : "-") + rnoncsdescList.get(i).getRelss3();
			// if (relsscons.equals(relss)) {
			// //
			// TIMESRELSS = "TIMESRELSS3";
			// } else {
			// relss = (rnoncsdescList.get(i).getRelss4Sign() == 0 ? "+"
			// : "-")
			// + rnoncsdescList.get(i).getRelss4();
			// if (relsscons.equals(relss)) {
			// //
			// TIMESRELSS = "TIMESRELSS4";
			// } else {
			// relss = (rnoncsdescList.get(i).getRelss5Sign() == 0 ? "+"
			// : "-")
			// + rnoncsdescList.get(i).getRelss5();
			// if (relsscons.equals(relss)) {
			// //
			// TIMESRELSS = "TIMESRELSS5";
			//
			// } else {
			// flag = false;
			// }
			// }
			// }
			// }
			// }
			// String
			// sql="UPDATE RNO_NCS SET INTERFER="+TIMESRELSS+"/reparfcn where RNO_NCS_DESC_ID IN ("+descid+") ";

			if ("".equals(TIMESRELSS)) {
				log.warn("ncs[" + descid + "]未定义门限值：" + RELSS);
				continue;
			}

			sql = "UPDATE "
					+ tabName
					+ " SET "
					+ interResultFieldName
					+ "="
					+ TIMESRELSS
					+ "/DECODE(reparfcn,0,NULL,REPARFCN) where RNO_NCS_DESC_ID ="
					+ descid;

			log.debug("计算干扰值的sql =" + sql);
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				flag = false;
			}
			// 转移作为分子的列到固定列，CI_DIVIDER或CA_DIVIDER
			sql = "UPDATE " + tabName + " SET " + interDividerFieldName + "="
					+ TIMESRELSS + " where RNO_NCS_DESC_ID =" + descid;
			log.debug("转移计算干扰的分子值到固定列：" + interDividerFieldName + " 的 sql="
					+ sql);
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				flag = false;
			}
		}
		// try {
		// stmt.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		log.debug("退出updateNcsInterfer flag:" + flag);
		return flag;
	}

	/**
	 * 
	 * @title 通过ncsIds获取相对信号强度记录集合
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-14下午12:23:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoNcsDescriptor> getRelssFiledRecordListsByNcsIds(
			Statement stmt, List<Long> ncsIds) {
		log.debug("进入getRelssFiledRecordListsByNcsIds Connection stmt:" + stmt
				+ " ,List<Long> ncsIds:" + ncsIds);
		if (ncsIds == null) {
			return null;
		}
		List<RnoNcsDescriptor> al = new ArrayList<RnoNcsDescriptor>();
		String ncsidString = "";

		for (int i = 0; i < ncsIds.size(); i++) {
			if (i == 0) {
				ncsidString = ncsIds.get(i).toString();
			} else if (ncsIds.size() > 1) {
				ncsidString += "," + ncsIds.get(i).toString();
			}
		}
		String sql = "SELECT RNO_NCS_DESC_ID,RELSS_SIGN,RELSS,RELSS2_SIGN,RELSS2,RELSS3_SIGN,RELSS3,RELSS4_SIGN,RELSS4,RELSS5_SIGN,RELSS5,INTERFER_MATRIX_ID,START_TIME,AREA_ID,NET_TYPE,NAME,STATUS from RNO_NCS_DESCRIPTOR WHERE RNO_NCS_DESC_ID in("
				+ ncsidString + ")";
		// System.out.println("ncsidString:"+ncsidString);
		// PreparedStatement psta = null;
		ResultSet res = null;
		try {
			// psta = conn.prepareStatement(sql);
			// psta.setString(1, ncsidString);
			// System.out.println("sql:"+sql);
			// res = psta.executeQuery();

			res = stmt.executeQuery(sql);
			int index = 1;
			while (res.next()) {
				index = 1;
				RnoNcsDescriptor rnoNcsDescriptor = new RnoNcsDescriptor();
				rnoNcsDescriptor.setRnoNcsDescId(res.getLong(index++));
				rnoNcsDescriptor.setRelssSign(res.getLong(index++));
				rnoNcsDescriptor.setRelss(res.getLong(index++));
				rnoNcsDescriptor.setRelss2Sign(res.getLong(index++));
				rnoNcsDescriptor.setRelss2(res.getLong(index++));
				rnoNcsDescriptor.setRelss3Sign(res.getLong(index++));
				rnoNcsDescriptor.setRelss3(res.getLong(index++));
				rnoNcsDescriptor.setRelss4Sign(res.getLong(index++));
				rnoNcsDescriptor.setRelss4(res.getLong(index++));
				rnoNcsDescriptor.setRelss5Sign(res.getLong(index++));
				rnoNcsDescriptor.setRelss5(res.getLong(index++));
				rnoNcsDescriptor.setInterferMatrixId(res.getLong(index++));
				rnoNcsDescriptor.setStartTime(res.getDate(index++));
				rnoNcsDescriptor.setAreaId(res.getLong(index++));
				rnoNcsDescriptor.setNetType(res.getString(index++));
				rnoNcsDescriptor.setName(res.getString(index++));
				rnoNcsDescriptor.setStatus(res.getString(index++));
				al.add(rnoNcsDescriptor);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// this.resourcesClose();// 关闭资源
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.debug("退出getRelssFiledRecordListsByNcsIds al:" + al);
		return al;
	}

	/**
	 * 
	 * @title 计算干扰矩阵 以ncs为单位，独自计算
	 * @param stmt
	 * @param tabName
	 * @param numerator
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15下午06:17:31
	 * @company 怡创科技
	 * @version 1.2
	 */
	@Deprecated
	public boolean calculateInterferMatrix(Statement stmt, String tabName,
			String numerator, List<Long> ncsIds) {
		// TODO Auto-generated method stub
		log.debug("进入calculateInterferMatrix(Connection conn, String tabName,String numerator, List<Long> ncsIds)"
				+ stmt + " " + tabName + " " + numerator + " " + ncsIds);
		List<RnoNcsDescriptor> rnoncsdescList = this
				.getRelssFiledRecordListsByNcsIds(stmt, ncsIds);
		String relss = "";
		String TIMESRELSS = "";
		String P_Mid = "";
		String P_High = "";
		long matrixid = 0;// 干扰矩阵描述ID变量
		boolean flag = false;
		// System.out.println("rnoncsdescList:"+rnoncsdescList.size());
		// -9==P_Mid +9==P_High
		for (int i = 0; i < rnoncsdescList.size(); i++) {
			// relss=(rnoncsdescList.get(i).getRelssSign()==0?"+":"-")+rnoncsdescList.get(i).getRelss();
			matrixid = rnoncsdescList.get(i).getInterferMatrixId();// null的为0
			long descid = rnoncsdescList.get(i).getRnoNcsDescId();// ncs描述ID
			Date startDate = rnoncsdescList.get(i).getStartTime();// 采集日期
			// rnoncsdescList.get(i).getStartTime().toString();
			String sql = "";
			if (matrixid == 0) {
				// 新建一个干扰矩阵描述记录：同时更新ncs的当前干扰矩阵描述ID
				matrixid = this.getNewInterferenceDescId(stmt);// 干扰矩阵描述ID
				Date datefmt = new Date();
				String datetimeString = sdf.format(datefmt);
				// System.out.println("datetimeString:"+datetimeString);
				sql = "INSERT INTO RNO_INTERFERENCE_DESCRIPTOR (INTER_DESC_ID,NAME,COLLECT_TYPE,COLLECT_TIME,TEMP_STORAGE,DEFAULT_DESCRIPTOR,CREATE_TIME,MOD_TIME,STATUS,AREA_ID,NET_TYPE,RNO_NCS_DESC_ID) VALUES("
						+ matrixid
						+ ",'"
						+ rnoncsdescList.get(i).getName()
						+ "','NCS',to_date('"
						+ rnoncsdescList.get(i).getStartTime().toString()
						+ "','yyyy-mm-dd hh24:mi:ss'),'Y','N',to_date('"
						+ datetimeString
						+ "','yyyy-mm-dd hh24:mi:ss'),to_date('"
						+ datetimeString
						+ "','yyyy-mm-dd hh24:mi:ss'),'"
						+ rnoncsdescList.get(i).getStatus()
						+ "',"
						+ rnoncsdescList.get(i).getAreaId()
						+ ",'"
						+ rnoncsdescList.get(i).getNetType()
						+ "',"
						+ rnoncsdescList.get(i).getRnoNcsDescId() + ")";
				try {
					// ps = conn.prepareStatement(sql);
					int a = stmt.executeUpdate(sql);
					flag = true;
					// 应该再更新NCS的干扰矩阵ID
					// System.out.println("update count:"+a);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					flag = false;
					e.printStackTrace();
				} finally {
					// this.resourcesClose();
				}
				// ncs描述id的干扰矩阵描述ID为空的时候处理
				// System.out.println("干扰矩阵ID："+rnoncsdescList.get(i).getInterferMatrixId());
				// System.out.println("relss:"+relss);
				// String relsscons="-9";//传入相对信号长度值
				P_Mid = this.getTimesRelssXByValue(rnoncsdescList.get(i), "-9");
				// System.out.println("descid:"+descid+" TIMESRELSS:"+TIMESRELSS);
				P_High = this
						.getTimesRelssXByValue(rnoncsdescList.get(i), "+9");
				if ("".equals(P_Mid)) {
					P_Mid = "TIMESRELSS4";
					// continue;
				}
				if ("".equals(P_High)) {
					P_High = "TIMESRELSS5";
				}
				// System.out.println("matrixid:"+matrixid);
				// sql
				// sql="INSERT INTO RNO_INTERFERENCE(INTER_ID,DESCRIPTOR_ID,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE) (SELECT seq_RNO_INTERFERENCE.nextval,"+matrixid+",CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,CASE  WHEN  DISTANCE IS NULL THEN 0 END  from (SELECT CELL,ncell INTERFERENCE_CELL,"+P_Mid+"/reparfcn CI,"+P_High+"/reparfcn CA,defined_neighbour IS_NEIGHBOUR,DISTANCE from RNO_NCS WHERE RNO_NCS_DESC_ID="+descid+"))";
				// sql="INSERT INTO RNO_INTERFERENCE(INTER_ID,DESCRIPTOR_ID,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE) (SELECT seq_RNO_INTERFERENCE.nextval,"+matrixid+",CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,CASE  WHEN  DISTANCE IS NULL THEN 0 else distance END  from (SELECT CELL,ncell INTERFERENCE_CELL,"+P_Mid+"/reparfcn CI,"+P_High+"/reparfcn CA,defined_neighbour IS_NEIGHBOUR,DISTANCE from "+tabName+" WHERE RNO_NCS_DESC_ID="+descid+"))";
				// -------2014-1-16 gmh 修改：考虑多个chgr时，需要合并ci，ca
				sql = "INSERT INTO RNO_INTERFERENCE(INTER_ID,DESCRIPTOR_ID,CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,DISTANCE) (SELECT seq_RNO_INTERFERENCE.nextval,"
						+ matrixid
						+ ",CELL,INTERFERENCE_CELL,CI,CA,IS_NEIGHBOUR,CASE  WHEN  DISTANCE IS NULL THEN 0 else distance END  from "
						+ " ( "
						+ " select  mid.cell,mid.INTERFERENCE_CELL ,sum(mid.ci) as ci ,sum(mid.ca) as ca ,MIN(mid.IS_NEIGHBOUR) as IS_NEIGHBOUR,avg(mid.distance) as distance from "
						+ " ( "
						+ " SELECT CELL,ncell INTERFERENCE_CELL,"
						+ P_Mid
						+ "/reparfcn CI,"
						+ P_High
						+ "/reparfcn CA,defined_neighbour IS_NEIGHBOUR,DISTANCE from "
						+ tabName
						+ " WHERE RNO_NCS_DESC_ID="
						+ descid
						+ " )mid "
						+ " group by mid.cell,mid.INTERFERENCE_CELL " + "))";
				// System.out.println("sql="+sql);
				log.debug("计算干扰矩阵的sql=" + sql);
				try {
					// ps = conn.prepareStatement(sql);
					int a = stmt.executeUpdate(sql);
					flag = true;
					// 应该再更新NCS的干扰矩阵ID
					// System.out.println("update count:"+a);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					flag = false;
					e.printStackTrace();
				} finally {
					// this.resourcesClose();
				}
				// 更新NCS描述中的干扰矩阵描述ID
				sql = "UPDATE RNO_NCS_DESCRIPTOR SET INTERFER_MATRIX_ID="
						+ matrixid + " WHERE RNO_NCS_DESC_ID =" + descid;
				try {
					// ps = conn.prepareStatement(sql);
					int a = stmt.executeUpdate(sql);
					flag = true;
					// 应该再更新NCS的干扰矩阵ID
					// System.out.println("update count:"+a);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					flag = false;
					e.printStackTrace();
				} finally {
					// this.resourcesClose();
				}
			} else {
				flag = false;
			}
		}

		log.debug("退出calculateInterferMatrix flag:" + flag);
		return flag;
	}

	/**
	 * 
	 * @title 获取干扰矩阵相对信号强度值
	 * @param rnoncsdescList
	 * @param TIMESRELSS
	 * @param i
	 * @param relsscons
	 * @return
	 * @author chao.xj
	 * @date 2014-1-16下午03:54:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String getTimesRelssXByValue(RnoNcsDescriptor rnoncsdesc,
			String relsscons) {
		log.debug("进入getTimesRelssXByValue(rnoncsdesc=" + rnoncsdesc
				+ ",relsscons=" + relsscons + ")");
		String TIMESRELSS = "";
		String relss;
		relss = (rnoncsdesc.getRelssSign() == 0 ? "+" : "-")
				+ rnoncsdesc.getRelss();
		if (relsscons.equals(relss)) {
			TIMESRELSS = "TIMESRELSS";
		} else {
			relss = (rnoncsdesc.getRelss2Sign() == 0 ? "+" : "-")
					+ rnoncsdesc.getRelss2();
			if (relsscons.equals(relss)) {
				TIMESRELSS = "TIMESRELSS2";
			} else {
				relss = (rnoncsdesc.getRelss3Sign() == 0 ? "+" : "-")
						+ rnoncsdesc.getRelss3();
				if (relsscons.equals(relss)) {
					TIMESRELSS = "TIMESRELSS3";
				} else {
					relss = (rnoncsdesc.getRelss4Sign() == 0 ? "+" : "-")
							+ rnoncsdesc.getRelss4();
					if (relsscons.equals(relss)) {
						TIMESRELSS = "TIMESRELSS4";
					} else {
						relss = (rnoncsdesc.getRelss5Sign() == 0 ? "+" : "-")
								+ rnoncsdesc.getRelss5();
						if (relsscons.equals(relss)) {
							TIMESRELSS = "TIMESRELSS5";
						}
					}
				}
			}
		}
		log.debug("退出getTimesRelssXByValue　TIMESRELSS:" + TIMESRELSS);
		return TIMESRELSS;
	}

	/**
	 * 
	 * @title 获取下一个干扰矩阵描述ID
	 * @param conn
	 * @return
	 * @author chao.xj
	 * @date 2014-1-16下午02:23:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long getNewInterferenceDescId(Statement stmt) {
		log.debug("进入getNewInterferenceDescId(Statement  stmt)" + stmt);
		String sql = "SELECT SEQ_RNO_INTERFERENCE_DESC.nextval id from dual";
		long interdescid = 0;
		ResultSet rs = null;
		try {
			// ps = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				interdescid = rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// this.resourcesClose();
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		log.debug("退出getNewInterferenceDescId interdescid:" + interdescid);
		return interdescid;
	}

	/**
	 * 邻区适配
	 * 
	 * @param conn
	 *            数据库连接
	 * @param tabName
	 *            ncs测量得到的邻区的存放表名
	 * @param ncsIds
	 * @param cityId
	 * @return
	 * @author brightming 2014-1-11 下午3:11:52
	 */
	public Map<Long, Boolean> matchNcsNcell(Connection conn, String tabName,
			List<Long> ncsIds, long cityId) {
		// TODO Auto-generated method stub
		boolean flag = false;
		log.debug("进入dao matchNcsNcell conn:" + conn + " tabName:" + tabName
				+ " ncsIds:" + ncsIds);

		if (conn == null || tabName == null || "".equals(tabName.trim())
				|| ncsIds == null || ncsIds.isEmpty()) {
			log.error("dao matchNcsNcell的方法参数无效！不能为空！conn:" + conn
					+ " tabName:" + tabName + " ncsIds:" + ncsIds);
			return null;
		}

		Map<Long, Boolean> res = new HashMap<Long, Boolean>();
		// 循环处理每个ncs
		for (long ncsId : ncsIds) {
			boolean ok = matchNcsNcellPerNcs(conn, tabName, ncsId, cityId);
			res.put(ncsId, ok);
		}

		return res;
	}

	/**
	 * 处理每一个ncs的邻区情况
	 * 
	 * @param conn
	 * @param tabName
	 * @param ncsId
	 * @param cityId
	 * @return
	 * @author brightming 2014-1-16 下午4:14:59
	 */
	private boolean matchNcsNcellPerNcs(Connection conn, String tabName,
			long ncsId, long cityId) {
		log.debug("进入方法：matchNcsNcellPerNcs。conn=" + conn + ",tabName="
				+ tabName + ",ncsId=" + ncsId);
		if (conn == null || tabName == null || "".equals(tabName.trim())
				|| ncsId <= 0) {
			log.error("dao matchNcsNcellPerNcs方法的参数不能为空！conn=" + conn
					+ ",tabName=" + tabName + ",ncsId=" + ncsId);
			return false;
		}

		// 获取区域信息
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		String sql = "";
		ResultSet rs = null;
		// 用cityid
		// sql =
		// "select area_id,city_id from RNO_NCS_DESCRIPTOR where RNO_NCS_DESC_ID="
		// + ncsId;
		// log.debug("获取ncs的信息，sql=" + sql);
		// long areaId = 0, cityId = 0;
		// try {
		// rs = stmt.executeQuery(sql);
		// while (rs.next()) {
		// areaId = rs.getLong(1);
		// cityId = rs.getLong(2);
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// return false;
		// } finally {
		// try {
		// rs.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// log.debug("获取得到的ncsid=" + ncsId + "对应的areaid为：" + areaId + ",cityId="
		// + cityId);

		// if (areaId <= 0) {
		// log.error("ncsId=" + ncsId + "未有区域信息，无法进行邻区匹配!");
		// return false;
		// }

		// -------------检查该区域下是否有小区信息------------------//
		sql = "select count(label) from cell where area_id in ( select area_id from sys_area where parent_id="
				+ cityId + ")";
		long cellCnt = 0;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cellCnt = rs.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.debug("cityId=" + cityId + ",下有小区数量：" + cellCnt + "个");
		if (cellCnt <= 0) {
			log.error("cityId=" + cityId + "下没有任何小区！不能进行邻区信息匹配！");
			return false;
		}

		int affectCnt = 0;
		// ----2014-3-23 先对需要处理的字段进行初始化----//
		// ncell一定要用这个带一个空白的
		sql = "update "
				+ tabName
				+ " set ncell=' ',NCELL_FREQ_CNT=null,ncells='',distance=null,CELL_FREQ_CNT=null,SAME_FREQ_CNT=null,ADJ_FREQ_CNT=null where RNO_NCS_DESC_ID="
				+ ncsId;
		log.debug("在进行邻区匹配之前，初始化相关的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("初始化字段影响行数：" + affectCnt);
		} catch (SQLException e2) {
			log.error("邻区匹配前的初始化字段操作失败！sql=" + sql);
			e2.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		long t1 = System.currentTimeMillis();
		// ----------用bsic，bcch组合为唯一的小区去更新ncs表的邻区信息----------//
		// ---更新邻区名称、邻区频点数---//
		sql = "merge into "
				+ tabName
				+ " tar using "
				+ "("
				+ " select label,NAME,longitude,latitude,(case when length(to_char(CELL.bsic))=1 then '0'||to_char(CELL.bsic) else to_char(cell.bsic) end) as bsic,cell.bcch "
				+ " ,(case when CELL.tch is null or CELL.tch='null' then 0 else ( case when instr(CELL.tch,CELL.bcch)>0 then (length(CELL.tch)-(length(replace(CELL.tch,','))))+(length(CELL.tch)-length(replace(CELL.tch,';')))+1 else ((length(CELL.tch)-(length(replace(CELL.tch,','))))+(length(CELL.tch)-length(replace(CELL.tch,';'))))+2 end ) end) as freqcnt "
				+ " from cell inner join ("
				+ " select substr(mid.pair,1,instr(mid.pair,'_')-1) as bsic ,substr(mid.pair,instr(mid.pair,'_')+1,length(mid.pair)-instr(mid.pair,'_')) as bcch ,mid.pair,MID.cnt from (select bsic||'_'||bcch as pair,count(label) as cnt from cell where area_id in ( select area_id from sys_area where parent_id="
				+ cityId
				+ ")"
				+ " group by bsic||'_'||bcch having (count(label))=1) mid"
				+ ")mid2 "
				+ " on cell.area_id in ( select area_id from sys_area where parent_id="
				+ cityId
				+ ")"
				+ " and cell.bsic=MID2.bsic and CELL.bcch=MID2.bcch"
				+ ")mid3 "
				+ " on (tar.RNO_NCS_DESC_ID="
				+ ncsId
				+ " and tar.bsic=mid3.bsic and tar.arfcn=mid3.bcch and tar.cell<>mid3.label)" //2014-7-23gmh添加：邻区不能是自己
				+ " when matched then update set tar.ncell=mid3.label,tar.NCELL_FREQ_CNT=mid3.freqcnt,tar.ncells=mid3.name";
		log.debug(">>>>>>>>>>>>>>>>准备用唯一的bsic、bcch组合更新ncs的信息的sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("用唯一的bsic、bcch组合更新ncs的信息影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("用唯一的bsic、bcch组合更新ncs的信息的操作失败！sql=" + sql);
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		long t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<<<<完成用唯一的bsic、bcch组合更新ncs的信息。耗时:" + (t2 - t1)
				+ "ms");

		// ----------------更新距离、同频数、邻频数----------------------//
		t1 = t2;
		sql = "merge into "
				+ tabName
				+ " a "
				+ " using "
				+ "("
				+ " select tar2.*,mid2.longitude as n_lon,mid2.latitude as n_lat ,FUN_RNO_GetDistance(TAR2.s_lon,TAR2.s_lat,MID2.longitude,MID2.latitude) as dis "
				+ ",RNO_GETCOFREQCNT(tar2.cellfreq,mid2.ncellfreq) as SAME_FREQ_CNT,RNO_GETADJACENTFREQCNT(tar2.cellfreq,mid2.ncellfreq) as ADJ_FREQ_CNT ,"
				+ " length(tar2.cellfreq)-(length(replace(tar2.cellfreq,',','')))+1 as CELL_FREQ_CNT "
				+ " FROM"
				+ " ("
				+ " select tar.*,mid.longitude as s_lon,mid.latitude as s_lat,mid.tch as cellfreq  from "
				+ "( "
				+ " select DISTINCT(ncs.cell||ncs.ncell) as tt,ncs.cell,ncs.ncell  from "
				+ tabName
				+ " ncs where RNO_NCS_DESC_ID="
				+ ncsId
				+ " and ncell<>' '"
				+ " )tar "
				+ " left join ("
				+ " select * from "
				+ " ( "
				+ " select distinct(label) as label,tch,longitude,latitude ,row_number() over(partition by label order by label) as seq "
				+ " from cell where area_id  in ( select area_id from sys_area where parent_id="
				+ cityId
				+ ") "
				+ " )where seq=1 "
				// "select label,tch,longitude,latitude from cell where area_id  in ( select area_id from sys_area where parent_id="+
				// cityId + ")"
				+ ") mid "
				+ " on TAR.cell=mid.label "
				+ " )tar2 "
				+ " left join ("
				// +
				// "select label,tch as ncellfreq,longitude,latitude from cell where area_id  in ( select area_id from sys_area where parent_id="+
				// cityId + ")"
				+ " select * from ( "
				+ " select label,tch as ncellfreq,longitude,latitude ,row_number() over(partition by label order by label) as seq from cell where area_id  in ( select area_id from sys_area where parent_id="
				+ cityId
				+ ") "
				+ " ) where seq=1 "

				+ ")mid2 "
				+ " on TAR2.ncell=MID2.label "
				+ " order by cell,ncell "
				+ " )mid10 "
				+ " on(a.RNO_NCS_DESC_ID="
				+ ncsId
				+ " and a.cell=mid10.cell and a.ncell=mid10.ncell) "
				+ " when matched then update set a.CELL_FREQ_CNT=mid10.CELL_FREQ_CNT,a.distance=mid10.dis,a.SAME_FREQ_CNT=mid10.SAME_FREQ_CNT,a.ADJ_FREQ_CNT=mid10.ADJ_FREQ_CNT,a.ncells=a.ncell||'('||a.ncells||',['||mid10.dis||'])'";

		log.debug(">>>>>>>>>>>>>准备更新已有邻区的ncs记录的距离、同频数、邻频数信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新已有邻区的ncs记录的距离、同频数、邻频数影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新已有邻区的ncs记录的距离、同频数、邻频数信息的操作失败！sql=" + sql);
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成更新已有邻区的ncs记录的距离、同频数、邻频数信息。耗时：" + (t2 - t1)
				+ "ms");

		// -----------处理ncs的ncell依旧为空的小区----------------//
		// 先清空第一个临时表
		try {
			stmt.executeUpdate("delete from rno_ncs_match_ncell");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		t1 = System.currentTimeMillis();
		// sql = "insert into rno_ncs_match_ncell(" +
		// "TOKEN,CELL,S_LON,S_LAT,BSIC,ARFCN,LABEL,N_LON,N_LAT," +
		// "DIS,NCELL_NAME,NCELLS,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT "
		// +
		// ") selectMID2.cell||MID2.bsic||MID2.arfcn as token ,"
		// +" mid2.cell,mid2.s_lon,mid2.s_lat ,mid2.bsic,mid2.arfcn "
		// +" ,mid.label,mid.longitude as n_lon,mid.latitude as n_lat ,FUN_RNO_GetDistance(mid.longitude,mid.latitude,mid2.s_lon,mid2.s_lat) as dis "
		// +",MID.name,MID2.CELL_FREQ_CNT,MID.NCELL_FREQ_CNT,RNO_GETCOFREQCNT(mid2.s_tch,mid.n_tch) ,RNO_GETADJACENTFREQCNT(mid2.s_tch,mid.n_tch) from "
		// + " ("
		// +
		// " select MID2.cell||MID2.bsic||MID2.arfcn as token ,mid2.cell,mid2.s_lon,mid2.s_lat ,mid2.bsic,mid2.arfcn "
		// +
		// " ,mid.label,mid.longitude as n_lon,mid.latitude as n_lat ,FUN_RNO_GetDistance(mid.longitude,mid.latitude,mid2.s_lon,mid2.s_lat) as dis "
		// + " from  "
		// + " ( "
		// +
		// "  select distinct(tar.cbb),tar.CELL,TAR.ncell,tar.BSIC,tar.ARFCN ,midcell.longitude as s_lon,midcell.latitude as s_lat "
		// +
		// " FROM (select cell,ncell,bsic,arfcn,cell||bsic||arfcn as cbb from "
		// + tabName
		// + "  where RNO_ncs_DESC_id="
		// + ncsId
		// + " and ncell=' ') TAR  "
		// +
		// " left join (select label,longitude,latitude from cell where area_id="
		// + areaId
		// + ") midcell  "
		// + " on ( tar.cell=midcell.label) "
		// + " ) mid2  "
		// + " left JOIN "
		// +
		// " (SELECT DISTINCT(LABEL),LONGITUDE,LATITUDE,BCCH,(case when length(to_char(CELL.bsic))=1 then '0'||to_char(CELL.bsic) else to_char(cell.bsic) end) as bsic FROM CELL WHERE AREA_ID="
		// + areaId + ") MID "
		// + " ON ( mid2.BSIC=MID.BSIC AND mid2.ARFCN=MID.BCCH) "
		// + " order by mid2.cell,mid2.bsic,mid2.arfcn,dis asc " + " ) "
		// + " Midd";

		sql = "insert into rno_ncs_match_ncell "
				+ " ( "
				+ " TOKEN,CELL,S_LON,S_LAT,BSIC,ARFCN,LABEL,N_LON,N_LAT,DIS "
				+ " ,NCELL_NAME,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT  "
				+ " ) "
				+ " select  "
				+ " MID2.cell||MID2.bsic||MID2.arfcn as token , "
				+ " mid2.cell,mid2.s_lon,mid2.s_lat ,mid2.bsic,mid2.arfcn "
				+ " ,mid.label,mid.longitude as n_lon,mid.latitude as n_lat ,FUN_RNO_GetDistance(mid.longitude,mid.latitude,mid2.s_lon,mid2.s_lat) as dis "
				+ " ,MID.name,MID2.CELL_FREQ_CNT,MID.NCELL_FREQ_CNT,RNO_GETCOFREQCNT(mid2.s_tch,mid.n_tch) ,RNO_GETADJACENTFREQCNT(mid2.s_tch,mid.n_tch) "
				+ " from  "
				+ " ( "
				+ " select distinct(tar.cbb),tar.CELL,TAR.ncell,tar.BSIC,tar.ARFCN ,midcell.longitude as s_lon,midcell.latitude as s_lat ,midcell.CELL_FREQ_CNT ,midcell.tch as s_tch "
				+ " FROM (select cell,ncell,bsic,arfcn,cell||bsic||arfcn as cbb from "
				+ tabName
				+ "  where RNO_ncs_DESC_id="
				+ ncsId
				+ " and (ncell=' ' or ncell is null or ncell='')) TAR  "
				+ " left join (select label,tch,RNO_GET_FREQ_CNT(tch) as CELL_FREQ_CNT,longitude,latitude from cell where area_id  in ( select area_id from sys_area where parent_id="
				+ cityId
				+ ")"
				+ ") midcell "
				+ " on ( tar.cell=midcell.label) "
				+ " ) mid2  "
				+ " left JOIN "
				+ " (SELECT DISTINCT(LABEL) as label,name,tch as n_tch,RNO_GET_FREQ_CNT(tch) as NCELL_FREQ_CNT,LONGITUDE,LATITUDE,BCCH,(case when length(to_char(CELL.bsic))=1 then '0'||to_char(CELL.bsic) else to_char(cell.bsic) end) as bsic FROM CELL WHERE AREA_ID  in ( select area_id from sys_area where parent_id="
				+ cityId + ")" + " ) MID "
				+ " ON ( mid2.BSIC=MID.BSIC AND mid2.ARFCN=MID.BCCH AND mid2.cell<>mid.label) "//2014-7-23 gmh 添加，排除cell=ncell的情况
				+ " order by mid2.cell,mid2.bsic,mid2.arfcn,dis asc";
		log.debug(">>>>>>>>>>>>>准备将ncs中ncell为空的信息与cell表的信息进行组合，存入到中间表中,sql="
				+ sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("将ncs中ncell为空的信息与cell表的信息进行组合，存入到中间表中，影响行数=" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成将ncs中ncell为空的信息与cell表的信息进行组合，存入临时表.耗时："
				+ (t2 - t1) + "ms");

		// ----------设置候选邻区信息-------------//
		sql = "merge into RNO_NCS_MATCH_NCELL tar "
				+ " using "
				+ " ( "
				+ " select TOKEN, WMSYS.WM_CONCAT(LABEL||'('||NCELL_NAME||',['||dis||'])') as ncells from RNO_NCS_MATCH_NCELL group by token "
				+ ")mid "
				+ " on (tar.token=mid.token and mid.ncells<>'(,[10000000000])') "
				+ " when matched then update set tar.ncells=mid.ncells";
		log.debug(">>>>>>>>>>>准备更新候选邻区信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新候选邻区，影响行数：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成更新候选邻区信息，耗时：" + (t2 - t1) + "ms");

		// ---------将第一个临时表的重复内容筛选出到第二个临时表-----//
		// 清空第二个临时表
		try {
			stmt.executeUpdate("delete from rno_ncs_matchncell_mindis");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		t1 = t2;
		sql = "insert into rno_ncs_matchncell_mindis(token,cnt,dis) "
				+ "select token,count(*),min(dis) from rno_ncs_match_ncell group by token having count(*) > 1 order by count(*) desc";
		log.debug(">>>>>>>>>>>将第一个临时表的重复内容筛选到第二个临时表，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成将第一个临时表的重复内容筛选到第二个临时表,耗时：" + (t2 - t1) + "ms");

		// ------------用第二个临时表的数据删除第一个临时表的重复数据----------------------//
		t1 = t2;
		sql = "delete from rno_ncs_match_ncell a where a.dis >" + " ( "
				+ " select min(b.dis) from rno_ncs_matchncell_mindis b "
				+ " where a.token = b.token)";
		log.debug(">>>>>>>>>>>准备用第二个临时表的数据删除第一个临时表的重复数据，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成用第二个临时表的数据删除第一个临时表的重复数据，耗时：" + (t2 - t1)
				+ "ms");

		// ----准将第一个临时表的相同的token、相同的dis的多余记录删除掉----//
		t1 = t2;
		sql = "delete from rno_ncs_match_ncell a "
				+ " where a.rowid  in "
				+ "("
				+ "select a.rowid from rno_ncs_match_ncell a "
				+ " where a.rowid !=("
				+ "select max(b.rowid) from rno_ncs_match_ncell b where a.token = b.token"
				+ ")" + ")";
		log.debug(">>>>>>>>>>>准备删除第一个临时表的token与dis都相同的重复数据，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成删除第一个临时表的token与dis都相同的重复数据，耗时：" + (t2 - t1)
				+ "ms");

		// -----------将仍然没有匹配出来的第一个临时表的ncell名称用规定的命名代替--------------//
		sql = "update rno_ncs_match_ncell set label='NotF_'||ARFCN||'_'||BSIC WHERE LABEL IS NULL OR LABEL =' ' OR LABEL=''";
		log.debug("将临时表中未匹配到邻区名称的记录的邻区名称用默认名称代替，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("不能匹配出来的邻区的个数用NOTF_将afrcn+\"_+\"bcch命名，共有：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		// -------此时，第一个临时表的数据，可以用来更新目标ncs表了------------//
		sql = "merge into "
				+ tabName
				+ " tar using rno_ncs_match_ncell mid "
				+ "on("
				+ " tar.rno_ncs_desc_id="
				+ ncsId
				+ " and tar.cell=mid.cell and tar.bsic=mid.bsic and tar.arfcn=mid.arfcn"
				+ ")"
				+ " when matched then update set "
				+ " tar.ncell=mid.label,tar.distance=mid.dis,tar.NCELLS=mid.NCELLS,tar.CELL_FREQ_CNT=mid.CELL_FREQ_CNT,"
				+ "tar.NCELL_FREQ_CNT=mid.NCELL_FREQ_CNT,tar.SAME_FREQ_CNT=mid.SAME_FREQ_CNT,tar.ADJ_FREQ_CNT=mid.ADJ_FREQ_CNT";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备用第一个临时表rno_ncs_match_ncell的内容，更新目标表："
				+ tabName + "的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("最后，用邻区匹配中间表的数据最终更新目标表的记录，共有：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成用第一个临时表rno_ncs_match_ncell的内容，更新目标表："
				+ tabName + "的信息。耗时：" + (t2 - t1) + "ms");

		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * @title 判断ncs相对应的区域下的小匹配置情况
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-13上午10:32:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int queryNcsSpecifyAreaCellConfige(final String areaids) {
		log.debug("进入queryNcsSpecifyAreaCellConfige(final String areaids)"
				+ areaids);
		// final List<Long> areaIdsList=this.getAreaIdsByNcsDescId(ncsIds);
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {

			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub

				String sql = "SELECT COUNT(*) from CELL WHERE AREA_ID in("
						+ areaids + ")";
				SQLQuery query = arg0.createSQLQuery(sql);
				// query.setLong(0, areaId);
				List reList = query.list();
				int res = 0;
				if (reList != null) {
					res = Integer.parseInt(reList.get(0).toString());
				}
				log.debug("退出queryNcsSpecifyAreaCellConfige  数量结果res:" + res);
				return res;
			}
		});
	}

	/**
	 * 
	 * @title 通过NCS描述ID取得区域ID集合信息
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15上午10:21:14
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Long> getAreaIdsByNcsDescId(Statement stmt,
			final List<Long> ncsIds) {
		log.debug("进入getAreaIdsByNcsDescId(final List<Long> ncsIds)" + ncsIds
				+ " stmt:" + stmt);

		// TODO Auto-generated method stub
		List<Long> areaidList = new ArrayList<Long>();
		if (ncsIds == null) {
			return null;
		}
		String ncsidString = "";
		for (int i = 0; i < ncsIds.size(); i++) {
			if (i == 0) {
				ncsidString = ncsIds.get(i).toString();
			} else if (ncsIds.size() > 1) {
				ncsidString += "," + ncsIds.get(i).toString();
			}
		}

		String sql = "SELECT AREA_ID from RNO_NCS_DESCRIPTOR WHERE rno_ncs_desc_id in("
				+ ncsidString + ")";
		ResultSet rs = null;
		try {
			// ps = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				areaidList.add(rs.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// this.resourcesClose();
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		log.debug("退出getAreaIdsByNcsDescId areaList:" + areaidList);
		return areaidList;

	}

	/**
	 * 
	 * @title 查询ncs的ncell名称是否为空
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15上午10:21:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoNcs> queryNcsNcellNameIsNull(Statement stmt,
			final List<Long> ncsIds) {
		log.debug("进入queryNcsNcellNameIsNull(final List<Long> ncsIds)" + ncsIds
				+ " stmt:" + stmt);
		// final List<Long> areaIdsList=this.getAreaIdsByNcsDescId(ncsIds);
		List<RnoNcs> rnoncsList = new ArrayList<RnoNcs>();
		// TODO Auto-generated method stub
		if (ncsIds == null) {
			return null;
		}
		String ncsidString = "";
		for (int i = 0; i < ncsIds.size(); i++) {
			if (i == 0) {
				ncsidString = ncsIds.get(i).toString();
			} else if (ncsIds.size() > 1) {
				ncsidString += "," + ncsIds.get(i).toString();
			}
		}
		String sql = "SELECT * from RNO_NCS WHERE trim(NCELL) IS NULL OR NCELL IS NULL and RNO_NCS_DESC_ID in("
				+ ncsidString + ")";
		ResultSet rs = null;
		try {
			// ps = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int index = 1;
				RnoNcs rnoNcs = new RnoNcs();
				rnoNcs.setRnoNcsId(rs.getLong(index++));
				rnoNcs.setRnoNcsDescId(rs.getLong(index++));
				rnoNcs.setCell(rs.getString(index++));
				rnoNcs.setBsic(rs.getString(6));
				rnoNcs.setArfcn(rs.getLong(7));
				rnoNcs.setDefinedNeighbour(rs.getString(8));
				rnoncsList.add(rnoNcs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		log.debug("退出queryNcsNcellNameIsNull  res:" + rnoncsList);
		return rnoncsList;

	}

	/**
	 * 
	 * @title 处理Ncs邻区名字为空设置缺省名称
	 * @param conn
	 * @param rnoncsList
	 * @return
	 * @author chao.xj
	 * @date 2014-1-15上午10:21:56
	 * @company 怡创科技
	 * @version 1.2
	 */
	// public boolean handleNcsNcellNameIsNullFillDefaultValue(Connection conn,
	// final List<RnoNcs> rnoncsList) {
	// log.debug("进入handleNcsNcellNameIsNullFillDefaultValue  rnoncsList:"
	// + rnoncsList + " conn:" + conn);
	// boolean flag = false;
	// String vsql =
	// "update RNO_NCS SET NCELL='NOTFOUND_'.ARFCN.'_'.BSIC WHERE RNO_NCS_ID=?";
	// // PreparedStatement pstmt = null;
	// try {
	// ps = conn.prepareStatement(vsql);
	// } catch (SQLException e3) {
	// e3.printStackTrace();
	// return flag;
	// }
	// try {
	// for (int i = 0; i < rnoncsList.size(); i++) {
	// long RnoNcsId = rnoncsList.get(i).getRnoNcsId();
	// ps.setLong(1, RnoNcsId);
	// ps.addBatch();
	// }
	// flag = true;
	// } catch (Exception e) {
	// // TODO: handle exception
	// flag = false;
	// e.printStackTrace();
	// }
	// try {
	// int[] res = ps.executeBatch();
	// flag = true;
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// flag = false;
	// e.printStackTrace();
	// } finally {
	// this.resourcesClose();
	// }
	// log.debug("退出handleNcsNcellNameIsNullFillDefaultValue  res:" + flag);
	// return flag;
	// }

	/**
	 * 
	 * @title 预编译声明及资源关闭函数
	 * @author chao.xj
	 * @date 2014-1-14下午01:30:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	// public void resourcesClose() {
	//
	// try {
	//
	// if (this.rs != null) {
	// this.rs.close();
	// this.rs = null;
	// }
	// if (this.ps != null) {
	// this.ps.close();
	// this.ps = null;
	// }
	// /*
	// * if (this.ct!=null) { this.ct.close(); this.ct=null; }
	// */
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// }

	/**
	 * 计算最大连通簇 ncsTabName:邻区测量信息所在的表 targTable:计算得到的连通簇的存放表 threshold:门限值
	 * 
	 * @param conn
	 * @param ncsTabName
	 *            ncs数据源表
	 * @param ncsIds
	 * @param clusterTable
	 *            连通簇的保存表
	 * @param clusterCellTabName
	 *            连通簇小区的保存表
	 * @param clusterCellRelaTab
	 *            连通簇小区关系表
	 * @param threshold
	 * @return
	 * @author brightming 2014-1-14 下午3:20:30
	 */
	public boolean calculateConnectedCluster(Connection conn,
			String ncsTabName, List<Long> ncsIds, String clusterTable,
			String clusterCellTabName, String clusterCellRelaTab,
			float threshold) {

		log.debug("进入方法：calculateConnectedCluster.connection=" + conn
				+ ",ncsTabName=" + ncsTabName + ",ncsIds=" + ncsIds
				+ ",clusterTable=" + clusterTable + ",clusterCellTabName="
				+ clusterCellTabName + ",threshold=" + threshold);
		if (conn == null || ncsTabName == null || ncsIds == null
				|| clusterTable == null || "".equals(ncsTabName.trim())
				|| "".equals(clusterTable.trim()) || ncsIds.isEmpty()) {
			log.error("calculateConnectedCluster的参数不合法！connection=" + conn
					+ ",ncsTabName=" + ncsTabName + ",ncsIds=" + ncsIds
					+ ",clusterTable=" + clusterTable + ",clusterCellTabName="
					+ clusterCellTabName + ",threshold=" + threshold);

			return false;
		}

		log.debug(">>>>>>>>>开始循环处理ncs数据");
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		String sql = "";
		ResultSet rs = null;
		String cell, ncell;
		// 簇编号，value为簇内小区
		List<List<String>> clusterCells = new ArrayList<List<String>>();
		// 小区所在的簇编号
		Map<String, List<Integer>> cellToClusterIds = new HashMap<String, List<Integer>>();

		int clustId = 0;
		List<Integer> slots = null;
		long st = System.currentTimeMillis();
		long et;
		long t1, t2;
		PreparedStatement pst = null;
		String insertClusCellSql = "insert into " + clusterCellTabName
				+ " (CLUSTER_ID,CELL) values(?,?)";
		PreparedStatement cellRelaPst = null;
		String cellRelaSql = "insert into " + clusterCellRelaTab
				+ "(CLUSTER_ID,CELL,NCELL) VALUES (?,?,?)";
		try {
			pst = conn.prepareStatement(insertClusCellSql);
			cellRelaPst = conn.prepareStatement(cellRelaSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
			log.error("方法calculateConnectedCluster准备PreparedStatement时出错！");
			return false;
		}
		for (long ncsId : ncsIds) {
			try {
				pst.clearBatch();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			// 判断该ncs下是否已经有联通簇
			// int cnt = 0;
			// sql =
			// "select count(ID) FROM "+clusterTable+" where RNO_NCS_DESC_ID="
			// + ncsId;
			// try {
			// rs = stmt.executeQuery(sql);
			// rs.next();
			// cnt = rs.getInt(1);
			// } catch (SQLException e) {
			// e.printStackTrace();
			// } finally {
			// try {
			// rs.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// return false;
			// }
			// }
			// if (cnt > 0) {
			// log.debug("ncsid=[" + ncsId + "]已经计算过最大联通簇，此处将跳过！");
			// // continue;
			// }

			t1 = System.currentTimeMillis();
			// 获取干扰度大于指定门限的测量数据
			// sql = "select CELL,NCELL,INTERFER from " + ncsTabName
			// + " where RNO_NCS_DESC_ID="+ncsId+" and INTERFER>=" + threshold;

			sql = "select substr(tok,1,instr(tok,'_')-1) as cell,substr(tok,instr(tok,'_')+1,length(tok)-instr(tok,'_')) as ncell ,inter "
					+ " FROM( "
					+ " select DISTINCT(tok),sum(interfer) as inter FROM ( "
					+ " select  cell||'_'||ncell as tok,cell,ncell,interfer from "
					+ ncsTabName
					+ " where RNO_NCS_DESC_ID="
					+ ncsId
					+ " and UPPER(CELL_INDOOR)<>'Y' and UPPER(NCELL_INDOOR)<>'Y'"
					+ " ) "
					+ " group by tok "
					+ " having sum(interfer)>="
					+ threshold
					+ " order by inter desc "
					+ " ) "
					+ " order by cell,ncell";

			// ---2014-3-21 排除室分小区---//
			// sql="select cell,ncell,inter from ( "+sql+" ) withintab ";
			log.debug(">>>>获取干扰度大于指定门限且排除室分小区的测量数据,sql=" + sql);
			try {
				rs = stmt.executeQuery(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 一个簇里的小区的名称连接在一起，作为搜索key，如CELLA与CELLB,则对应的key为CELLACELLB
			List<String> cellNameInCluster = new ArrayList<String>();
			try {
				while (rs.next()) {
					cell = rs.getString(1);
					ncell = rs.getString(2);
					if (cell.equals(ncell)) {
						continue;
					}
					if (cell == null || ncell == null) {
						log.warn("邻区匹配不完整的数据！出现为空的情况！cell=" + cell + ",ncell="
								+ ncell);
						continue;
					}
					List<String> pair = new ArrayList<String>();
					pair.add(cell);
					pair.add(ncell);
					clusterCells.add(pair);
					cellNameInCluster.add(cell + ncell);

					// 小区所在簇
					slots = cellToClusterIds.get(cell);
					if (slots == null) {
						slots = new ArrayList<Integer>();
						cellToClusterIds.put(cell, slots);
					}
					slots.add(clustId);

					// 邻区所在簇
					slots = cellToClusterIds.get(ncell);
					if (slots == null) {
						slots = new ArrayList<Integer>();
						cellToClusterIds.put(ncell, slots);
					}
					slots.add(clustId);

					clustId++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			t2 = System.currentTimeMillis();
			log.debug("获取ncs对应的最小联通簇、整理小区所在的簇编号完成。共有:" + clustId + "组数据。耗时："
					+ (t2 - t1) + "ms");

			// 开始合并最小簇为最大簇
			t1 = t2;
			log.debug(">>>>>>>>>>开始合并最大联通簇.候选簇有：" + clusterCells.size() + " 个。");
			List<Set<String>> clusters = null;
			List<List> res = mergeToLargestCluster(clusterCells,
					cellToClusterIds, cellNameInCluster);
			clusters = (List<Set<String>>) res.get(0);
			// 干扰关系
			List<List<List<String>>> resRelations = (List<List<List<String>>>) res
					.get(1);
			t2 = System.currentTimeMillis();
			log.debug("<<<<<<<<<<<最大联通簇计算完成。耗时：" + (t2 - t1) + "ms");

			log.debug(">>>>>>>>>>>准备保存ncsid=" + ncsId + "的最大联通簇结果");
			t1 = t2;

			Set<String> one = null;
			List<List<String>> relas = null;
			long clusterId = -1;
			long startClusterId = 1;
			// 是否需要seq，如果不需要，则id直接递增即可，这里的判断条件固定写
			boolean needSeq = ("RNO_NCS_CLUSTER".equalsIgnoreCase(clusterTable) ? true
					: false);
			for (int c = 0; c < clusters.size(); c++) {
				one = clusters.get(c);
				clusterId = needSeq == true ? RnoHelper.getNextSeqValue("SEQ_"
						+ clusterTable, conn) : (startClusterId++);
				if (clusterId < 0) {
					log.error("准备保存连通簇数据时出错！");
					return false;
				}
				relas = resRelations.get(c);// 关系
				try {
					// 保存簇信息
					stmt.executeUpdate("insert into " + clusterTable
							+ "(ID,RNO_NCS_DESC_ID) VALUES(" + clusterId + ","
							+ ncsId + ")");
					// 开始保存簇内小区
					for (String cl : one) {
						pst.setLong(1, clusterId);
						pst.setString(2, cl);
						pst.addBatch();
					}

					// 保存干扰关系。。。。。
					for (List<String> rel : relas) {
						cellRelaPst.setLong(1, clusterId);
						cellRelaPst.setString(2, rel.get(0));
						cellRelaPst.setString(3, rel.get(1));
						cellRelaPst.addBatch();
					}

				} catch (SQLException e) {
					e.printStackTrace();
					log.error("保存连通簇数据时出错！");
					return false;
				}
			}
			// 执行保存簇内小区信息
			try {
				pst.executeBatch();
				cellRelaPst.executeBatch();
			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error("保存连通簇内小区时出错！");
				return false;
			}

			// 更新其频点数量
			sql = "merge into "
					+ clusterCellTabName
					+ " tar using (select label,RNO_GET_FREQ_CNT(tch)  as FREQ_CNT from ( "
					+ " select label,tch,row_number() over(partition by label order by tch nulls last) as seq from cell "
					// --where area_id in (select area_id from SYS_AREA where
					// parent_id=103)
					+ " ) where seq=1 )mid1 on (tar.CLUSTER_ID IN (SELECT ID FROM "
					+ clusterTable
					+ " WHERE RNO_NCS_DESC_ID="
					+ ncsId
					+ ")  and tar.cell=mid1.label) when matched then update set tar.FREQ_CNT=mid1.FREQ_CNT";
			log.debug("更新簇内小区频点数量的sql=" + sql);
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		et = System.currentTimeMillis();

		log.debug("<<<<<<<<<完成循环处理ncs数据。耗时：" + (et - st) + "ms");
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 合并最小联通簇为最大联通簇
	 * 
	 * @param clusterCells
	 *            最小联通簇
	 * @param cellToClusterIds
	 *            小区与所在最小联通簇的id的对应关系
	 * @return 0: --- 对应的值为List<Set<String>> 是所有簇列表里的小区，每个set为一个簇 1: ---
	 *         簇内小区的干扰关系 List<List<List<String>>>
	 *         每个value都是一个list<list>,内存的list是长度为2的：0的为主小区，1的为干扰小区
	 * @author brightming 2014-1-14 下午1:49:13
	 */
	private static List<List> mergeToLargestCluster(
			List<List<String>> clusterCells,
			Map<String, List<Integer>> cellToClusterIds,
			List<String> cellNamePairs) {

		// System.out.println("clusterCells size =" + clusterCells.size()
		// + " --> " + clusterCells);
		// System.out.println("cellToClusterIds size =" +
		// cellToClusterIds.size()
		// + " --> " + cellToClusterIds);

		log.debug("进入方法：mergeToLargestCluster.clusterCells大小："
				+ clusterCells.size() + ",cellToClusterIds大小："
				+ cellToClusterIds.size());
		List<Set<String>> clusters = new ArrayList<Set<String>>();// 簇内小区
		List<List<List<String>>> resRelations = new ArrayList<List<List<String>>>();

		long t1 = System.currentTimeMillis();
		boolean alreadyIn = false;// 是否已经被包含在某个最大联通簇内
		Set<String> left = null;
		Set<String> right = null;
		List<Integer> ids1 = null;
		List<Integer> ids2 = null;
		String a = "", b = "";
		// 对每个最小簇（只有2个元素进行处理）
		for (int i = 0; i < clusterCells.size(); i++) {
			List<String> cells = clusterCells.get(i);// cells[0]是主小区，cells[1]是干扰小区
			// System.out.println("检查关系："+cells);
			if (cells.size() != 2) {
				continue;
			}
			alreadyIn = false;
			for (Set<String> big : clusters) {
				if (big.containsAll(cells)) {
					alreadyIn = true;
					break;
				}
			}
			if (alreadyIn) {
				log.debug(cells + "已被包含，跳过");
				continue;
			}
			// 开始计算
			a = cells.get(0);
			b = cells.get(1);
			ids1 = cellToClusterIds.get(a);
			ids2 = cellToClusterIds.get(b);

			List<List<String>> curRelas = null;// 当前分析的一对最小联通簇对应的干扰关系
			List<String> re1 = null;
			curRelas = new ArrayList<List<String>>();
			if (cellNamePairs.contains(a + b)) {
				re1 = new ArrayList<String>();
				re1.add(a);
				re1.add(b);
				curRelas.add(re1);
			}
			if (cellNamePairs.contains(b + a)) {
				re1 = new ArrayList<String>();
				re1.add(b);
				re1.add(a);
				curRelas.add(re1);
			}
			if (ids1.size() == 1 || ids2.size() == 1) {
				// a小区或b小区只被一个最小联通簇包含，那么这个组合不可能再与其他小区联合为更大的簇了
				clusters.add(new HashSet<String>(cells));
				// 添加该簇对应的干扰关系
				resRelations.add(curRelas);
				continue;
			}

			left = new HashSet<String>();
			for (int ci : ids1) {
				left.addAll(clusterCells.get(ci));
			}
			right = new HashSet<String>();
			for (int ci : ids2) {
				right.addAll(clusterCells.get(ci));
			}

			// 求交集
			left.retainAll(right);
			left.removeAll(cells);

			// 此时left集合中的cell，是与当前最小簇里的两个小区都相关的，但是，left里的小区，本身是否两两相关，需要进一步判断
			List<String> forCal = new ArrayList<String>(left);
			int size = forCal.size();
			List<String> finalRes = new ArrayList<String>();// 最终两两相关的
			String c1, c2;
			boolean isContained = false;
			// List<List<String>> eachPairExtend = new
			// ArrayList<List<String>>();
			List<String> maybe = null;
			List<List<String>> maybes = null;
			List<List<String>> maybeRela = null;// 每个可能分支对应的干扰关系
			List<List<List<String>>> maybeRelas = null;// 所有可能分支对应的干扰关系
			if (size >= 1) {// size最少都为2：即交集中起码包含上面的a小区，b小区
				for (int k1 = 0; k1 < size; k1++) {
					c1 = forCal.get(k1);
					if (a.equals(c1) || b.equals(c1)) {
						continue;
					}
					// System.out.println("检查"+forCal+"中的节点："+c1);
					// 需要找出最大可能相关的集合
					// maybes是以c1为根的所有可能联通树
					maybes = new ArrayList<List<String>>();
					maybeRelas = new ArrayList<List<List<String>>>();// 每个簇都会对应若干对干扰关系
					// 以C1为根构建树.当前只有一个分支，该分支只有一个节点：根节点
					maybe = new ArrayList<String>();
					maybe.add(c1);
					maybes.add(maybe);

					List<String> rl = new ArrayList<String>();
					maybeRela = new ArrayList<List<String>>();
					List<List<String>> c1WithAb = new ArrayList<List<String>>();
					if (cellNamePairs.contains(a + c1)) {
						rl = new ArrayList<String>();
						rl.add(a);
						rl.add(c1);
						c1WithAb.add(rl);
					}
					if (cellNamePairs.contains(c1 + a)) {
						rl = new ArrayList<String>();
						rl.add(c1);
						rl.add(a);
						c1WithAb.add(rl);
					}
					if (cellNamePairs.contains(b + c1)) {
						rl = new ArrayList<String>();
						rl.add(b);
						rl.add(c1);
						c1WithAb.add(rl);
					}
					if (cellNamePairs.contains(c1 + b)) {
						rl = new ArrayList<String>();
						rl.add(c1);
						rl.add(b);
						c1WithAb.add(rl);
					}
					maybeRelas.add(new ArrayList(c1WithAb));

					for (int k2 = k1 + 1; k2 < size; k2++) {
						c2 = forCal.get(k2);
						if (a.equals(c2) || b.equals(c2)) {
							continue;
						}
						// System.out.println("检查"+forCal+"中的节点："+c1+" 的后续节点:"+c2);
						List<List<String>> relaWithC1C2 = new ArrayList<List<String>>();// 干扰关系,对应一个分支
						List<String> temp = null;
						if (cellNamePairs.contains(c1 + c2)) {
							temp = new ArrayList<String>();
							temp.add(c1);
							temp.add(c2);
							relaWithC1C2.add(temp);
						}
						if (cellNamePairs.contains(c2 + c1)) {
							temp = new ArrayList<String>();
							temp.add(c2);
							temp.add(c1);
							relaWithC1C2.add(temp);
						}

						List<List<String>> relaWithAb = new ArrayList<List<String>>();// 干扰关系,对应一个分支
						if (cellNamePairs.contains(a + c2)) {
							temp = new ArrayList<String>();
							temp.add(a);
							temp.add(c2);
							relaWithAb.add(temp);
						}
						if (cellNamePairs.contains(c2 + a)) {
							temp = new ArrayList<String>();
							temp.add(c2);
							temp.add(a);
							relaWithAb.add(temp);
						}
						if (cellNamePairs.contains(c2 + b)) {
							temp = new ArrayList<String>();
							temp.add(c2);
							temp.add(b);
							relaWithAb.add(temp);
						}
						if (cellNamePairs.contains(b + c2)) {
							temp = new ArrayList<String>();
							temp.add(b);
							temp.add(c2);
							relaWithAb.add(temp);
						}
						if (relaWithC1C2.size() > 0) {
							// c1,c2有关系
							int assCnt = 0;
							// 判断c1,c2这个组合，是否已经被包含在基于a，b小区计算出来的分支里面
							// 所有分支都要算，每一个分支到时都可能会成为一个簇（除非已经被包含）
							List<List<String>> tempRelaForCal = null;
							for (int mi = 0; mi < maybes.size(); mi++) {
								maybe = maybes.get(mi);// 取一个分支，每个分支包含至少2个小区（每一个分支都会对应一组干扰关系）
								maybeRela = maybeRelas.get(mi);// 该分支对应的干扰关系
								tempRelaForCal = new ArrayList<List<String>>();
								boolean ass = true;
								for (String mc : maybe) {// 对应分支里的所有元素都检查是否与c2有关系
									// 需要和里面的所有都关联
									if (cellNamePairs.contains(c2 + mc)) {
										tempRelaForCal.add(Arrays
												.asList(c2, mc));
									}
									if (cellNamePairs.contains(mc + c2)) {
										tempRelaForCal.add(Arrays
												.asList(mc, c2));
									}
									if (!cellNamePairs.contains(c2 + mc)
											&& !cellNamePairs.contains(mc + c2)) {
										ass = false;
										break;
									}
								}
								// 检查完一个分支的情况
								if (ass) {
									// 只有都关联，才能进入这个组
									maybe.add(c2);// c2满足进入这个分支的条件
									maybeRela.addAll(tempRelaForCal);// 该分支对应的干扰关系也增加
									maybeRela.addAll(new ArrayList(relaWithAb));// 与a，b的关系
									assCnt++;
								}
							}
							// 所有分支检查完
							if (assCnt == 0) {
								// 如果没有和任何一个组里的所有小区能两两关联，则自创一个
								// 独立分支
								maybe = new ArrayList<String>();
								maybe.add(c1);
								maybe.add(c2);
								maybes.add(maybe);

								// 干扰关系
								maybeRela = new ArrayList<List<String>>();
								maybeRela.addAll(new ArrayList(relaWithC1C2));// c1与c2之间的
								maybeRela.addAll(new ArrayList(relaWithAb));// c2与a，b的关系
								maybeRela.addAll(new ArrayList(c1WithAb));// c1与a，b之间的
								maybeRelas.add(maybeRela);
							}
						}
					}
					// 判断maybe是否已经被eachPairExtend的某个包含
					for (int ki = 0; ki < maybes.size(); ki++) {
						// 由最小联通簇产生的所有分支树：maybes
						// 判断每个分支是否已经被需要返回的集合包含，如果没有被包含，则要加入该返回集合
						maybe = maybes.get(ki);
						maybe.addAll(cells);// 别忘了这个cells源头。
						maybeRela = maybeRelas.get(ki);// 该分支对应的干扰关系
						maybeRela.addAll(curRelas);// 别忘了这个最小联通簇产生的关联关系。
						for (Set<String> ext : clusters) {
							if (ext.containsAll(maybe)) {
								isContained = true;
								break;
							}
						}
						if (!isContained) {
							// 未被任何包含，加入
							clusters.add(new HashSet<String>(maybe));
							resRelations.add(maybeRela);// 干扰关系
						}
					}
					// System.out.println("检查得到："+maybes);
				}
			}

		}
		long t2 = System.currentTimeMillis();
		log.debug("mergeToLargestCluster。耗时：" + (t2 - t1) + "ms");
		// System.out.println("耗时：" + (t2 - t1) + "ms . cluster result="
		// + clusters);

		// Map<String,Object> res=new HashMap<String,Object>();
		List<List> res = new ArrayList<List>();
		res.add(clusters);
		res.add(resRelations);
		return res;
	}

	/**
	 * 计算ncs下的簇权重
	 * 
	 * @param conn
	 * @param ncsTab
	 *            ncs数据表
	 * @param clusterTab
	 *            连通簇表
	 * @param clusterCellTab
	 *            连通簇小区表
	 * @param ncsIds
	 * @param stsIds
	 * @param forceRecalculte
	 * @param badClusterThreshold
	 *            簇约束因子的门限，大于此值，表示有问题，也就是需要计算的簇
	 * @return
	 * @author brightming 2014-1-15 下午3:16:03
	 */
	public Map<Long, Boolean> calculateNcsClusterWeight(Connection conn,
			String ncsTab, String clusterTab, String clusterCellTab,
			List<Long> ncsIds, List<Long> stsIds, boolean forceRecalculte,
			float badClusterThreshold) {

		log.debug("进入方法：calculateNcsClusterWeight.conn=" + conn + ",ncsTab="
				+ ncsTab + ",ncsIds=" + ncsIds + ",stsIds=" + stsIds
				+ ",forceRecalculte=" + forceRecalculte);
		if (conn == null || ncsTab == null || "".equals(ncsTab.trim())
				|| ncsIds == null || ncsIds.isEmpty() || stsIds == null
				|| stsIds.isEmpty()) {
			log.error("方法calculateNcsClusterWeight的参数无效！不能为空！conn=" + conn
					+ ",ncsTab=" + ncsTab + ",ncsIds=" + ncsIds + ",stsIds="
					+ stsIds + ",forceRecalculte=" + forceRecalculte);
			return null;
		}

		long st = System.currentTimeMillis();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("calculateNcsClusterWeight方法中，准备statement时出错！");
			return null;
		}

		// ------------获取ncs的详情-----------------//
		String sql = "";
		List<Long> nIds = new ArrayList<Long>();
		/*
		 * ResultSet rs = null;
		 * 
		 * String ncsIdStr = ""; for (long nid : ncsIds) { ncsIdStr += nid +
		 * ","; } ncsIdStr = ncsIdStr.substring(0, ncsIdStr.length() - 1); sql =
		 * "select RNO_NCS_DESC_ID,AREA_ID,INTERFER_MATRIX_ID from RNO_NCS_DESCRIPTOR where RNO_NCS_DESC_ID in("
		 * + ncsIdStr + ")";
		 * 
		 * 
		 * List<Long> areaIds = new ArrayList<Long>(); List<Long> matrixIds =
		 * new ArrayList<Long>(); log.debug("准备获取ncs相关的信息。"); try { rs =
		 * stmt.executeQuery(sql); while (rs.next()) { long nid = rs.getLong(1);
		 * long areaid = rs.getLong(2); long matrixid = rs.getLong(3);
		 * 
		 * if (matrixid > 0) { nIds.add(nid); areaIds.add(areaid);
		 * matrixIds.add(matrixid); } } } catch (SQLException e) {
		 * e.printStackTrace(); return null; } finally { try { rs.close(); }
		 * catch (SQLException e) { e.printStackTrace(); } }
		 * 
		 * log.debug("获取到的ncs信息为：ncsIds=" + ncsIds + ",areaIds=" + areaIds +
		 * ",matrixIds=" + matrixIds); if (nIds.isEmpty()) {
		 * log.error("没有满足要求的ncs信息，无法进行计算！"); return null; }
		 */
		Map<Long, Boolean> result = new HashMap<Long, Boolean>();
		log.debug("开始处理ncs列表...");
		// ----------开始循环处理-----------//
		for (int i = 0; i < ncsIds.size(); i++) {
			long nid = ncsIds.get(i);
			log.debug("获取nid=" + nid + "下的簇信息");
			/*
			 * sql = "select ID,WEIGHT from " + clusterTab +
			 * " where RNO_NCS_DESC_ID=" + nid + " and CONTROL_FACTOR>=" +
			 * badClusterThreshold; try { rs = stmt.executeQuery(sql); while
			 * (rs.next()) { long clusterId = rs.getLong(1); float weight =
			 * rs.getFloat(2); if (weight == 0 || (weight != 0 &&
			 * forceRecalculte)) { boolean ok = calculateClusterWeight(conn,
			 * clusterId, areaIds.get(i), stsIds, matrixIds.get(i));
			 * result.put(nid, ok); } } } catch (SQLException e) {
			 * e.printStackTrace();
			 * log.error("calculateNcsClusterWeight方法中，计算ncsid=" + nid +
			 * "的簇权重时出错！"); result.put(nid, false); } finally { try {
			 * rs.close(); } catch (SQLException e) { e.printStackTrace(); } }
			 */

			// 整个ncs的簇一起计算
			sql = "select cluster_id,ci_divider/reparfcn +ca_divider/reparfcn as WEIGHT FROM(select cluster_id,sum(ci_divider) as ci_divider,sum(ca_divider) as ca_divider,sum(reparfcn) as reparfcn FROM "
					+ " (select ncs.*,mid3.cluster_id FROM(select cell,ncell, ci_divider ,ca_divider,reparfcn from "
					+ ncsTab
					+ " where rno_ncs_desc_id="
					+ nid
					+ " ) ncs inner JOIN "
					+ " (select mid1.*,mid2.ncell from(select cluster_id,cell from "
					+ clusterCellTab
					+ " where cluster_id in(select id from "
					+ clusterTab
					+ " where rno_ncs_desc_id="
					+ nid
					+ " ))mid1 "
					+ " left JOIN(select cluster_id,cell as ncell from "
					+ clusterCellTab
					+ " where cluster_id in(select id from "
					+ clusterTab
					+ " where rno_ncs_desc_id="
					+ nid
					+ " ))mid2 "
					+ " ON ( mid1.cluster_id=mid2.cluster_id and mid1.cell<>mid2.ncell))mid3 on(ncs.cell=mid3.cell and ncs.ncell=mid3.ncell) order by MID3.cluster_id) group by cluster_id)";
			sql = "merge into "
					+ clusterTab
					+ " tar using ("
					+ sql
					+ " ) src on (tar.id=src.cluster_id) when matched then update set tar.WEIGHT=src.WEIGHT";
			log.debug("计算cluster表：" + clusterTab + " 的簇权重的sql：" + sql);
			try {
				stmt.executeUpdate(sql);
				result.put(nid, true);
			} catch (SQLException e) {
				e.printStackTrace();
				result.put(nid, false);
			}
		}

		long et = System.currentTimeMillis();
		log.debug("退出方法：calculateNcsClusterWeight。耗时：" + (et - st)
				+ "ms。result=" + result);
		return result;
	}

	/**
	 * 计算簇权重
	 * 
	 * @param conn
	 * @param clusterId
	 *            clusterId
	 * @param ncsTab
	 *            ncs数据表
	 * @param clusterTab
	 *            连通簇表
	 * @param clusterCellTab
	 *            连通簇小区表
	 * @return
	 * @author brightming 2014-2-15 下午4:54:59
	 */
	/*
	 * public boolean calculateClusterWeight(Connection conn, long clusterId,
	 * String ncsTab, String clusterTab, String clusterCellTab) {
	 * log.debug("进入方法：calculateClusterWeight。conn=" + conn + ",clusterId=" +
	 * clusterId + ",ncsTab=" + ncsTab + ",clusterTab=" + clusterTab +
	 * ",clusterCellTab=" + clusterCellTab);
	 * 
	 * // 簇内小区的同频干扰系数和邻频干扰系数之和 String sql = "";
	 * 
	 * sql =
	 * "select sum(ci+ca) as inter from ( select cell,sum(ci_divider/reparfcn) as ci,sum(ca_divider/reparfcn) as ca "
	 * + " from " + ncsTab +
	 * " where cell in (select cell from rno_ncs_cluster_cell where cluster_id=14889) "
	 * +
	 * " and ncell in (select cell from rno_ncs_cluster_cell where cluster_id=14889) "
	 * + " group by cell)"; return true; }
	 */

	/*
	 * public boolean calculateClusterWeight(Connection conn, long clusterId,
	 * long areaId, List<Long> stsIds, long interferMatrixId,String
	 * ncsTab,String clusterTab,String clusterCellTab) {
	 * log.debug("进入方法：calculateClusterWeight。conn=" + conn + ",clusterId=" +
	 * clusterId + ",areaId=" + areaId + ",stsId=" + stsIds +
	 * ",interferMatrixId=" + interferMatrixId); if (conn == null || areaId < 0
	 * || stsIds == null || stsIds.isEmpty() || interferMatrixId < 0) {
	 * log.error("方法calculateClusterWeight参数非法！conn=" + conn + ",clusterId=" +
	 * clusterId + ",areaId=" + areaId + ",stsId=" + stsIds +
	 * ",interferMatrixId=" + interferMatrixId); return false; }
	 * 
	 * long st = System.currentTimeMillis(); Statement stmt = null; try { stmt =
	 * conn.createStatement(); } catch (SQLException e) { e.printStackTrace();
	 * return false; }
	 * 
	 * // --------------清除中间表---------------------// try {
	 * stmt.executeUpdate("delete from RNO_NCS_WEIGHT_MID"); } catch
	 * (SQLException e2) { e2.printStackTrace();
	 * log.error("清除临时表RNO_NCS_WEIGHT_MID数据时出错！"); return false; } String sql =
	 * ""; // ------------将cluster下的小区移动到中间表-----------// sql =
	 * "insert into RNO_NCS_WEIGHT_MID(CLUSTER_ID,CELL1,PER_TRAFFIC_1,FREQ1,COINTER_1,ADJINTER_1,RESOURCE_USE_RATE,CELL2,PER_TRAFFIC_2,FREQ2,COINTER_2,ADJINTER_2,COFREQCNT,ADJFREQCNT,COINTER_TRAFFIC,ADJINTER_TRAFFIC) values("
	 * + clusterId + ",?,0,'',0,0,0,?,0,'',0,0,0,0,0,0)"; PreparedStatement pst
	 * = null; try { pst = conn.prepareStatement(sql); } catch (SQLException e1)
	 * { e1.printStackTrace(); try { stmt.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } return false; }
	 * 
	 * ResultSet rs = null;
	 * 
	 * // 获取cluster下的小区 sql =
	 * "select cell from "+clusterCellTab+" where CLUSTER_ID=" + clusterId;
	 * List<String> cells = new ArrayList<String>(); String cell = ""; try { rs
	 * = stmt.executeQuery(sql); while (rs.next()) { cell = rs.getString(1); //
	 * 需要两两组合 for (String c : cells) { pst.setString(1, cell); pst.setString(2,
	 * c); pst.addBatch();
	 * 
	 * pst.setString(1, c); pst.setString(2, cell); pst.addBatch(); }
	 * cells.add(cell); } } catch (SQLException e) { e.printStackTrace(); try {
	 * pst.clearBatch(); } catch (SQLException e1) { e1.printStackTrace(); } try
	 * { pst.close(); } catch (SQLException e1) { e1.printStackTrace(); }
	 * 
	 * try { stmt.close(); } catch (SQLException e1) { e1.printStackTrace(); }
	 * return false; } finally { try { rs.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * // 插入中间表 try { pst.executeBatch(); } catch (SQLException e) {
	 * e.printStackTrace(); try { pst.close(); } catch (SQLException e1) {
	 * e1.printStackTrace(); } try { stmt.close(); } catch (SQLException e1) {
	 * e1.printStackTrace(); } return false; }
	 * 
	 * // -------------填充频点、话务相关信息---------------// // ---由于计算的简化，先不考虑频点、话务相关信息
	 * 2014-1-16，只需要干扰系数 // 填充小区1频点信息 // sql = //
	 * "merge into RNO_NCS_WEIGHT_MID using (SELECT distinct(label),(case when \"INSTR\"(tch, bcch)>0 then tch  else bcch||','||tch  end)  freq from cell where area_id="
	 * // + areaId // + //
	 * ") mcell on (tar.cell1=mcell.label) when matched then update set tar.FREQ1=mcell.freq"
	 * ; // log.debug("填充cell1频点信息：sql=" + sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * log.error("更新cell1频点信息时出错！sql=" + sql); // e.printStackTrace(); // return
	 * false; // } // // 填充小区2频点信息 // sql = //
	 * "merge into RNO_NCS_WEIGHT_MID using (SELECT distinct(label),(case when \"INSTR\"(tch, bcch)>0 then tch  else bcch||','||tch  end)  freq from cell where area_id="
	 * // + areaId // + //
	 * ") mcell on (tar.cell2=mcell.label) when matched then update set tar.FREQ2=mcell.freq"
	 * ; // log.debug("填充cell2频点信息：sql=" + sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * log.error("更新cell2频点信息时出错！sql=" + sql); // e.printStackTrace(); // return
	 * false; // } // // // 填充话务量相关信息 // String stsIdstr = ""; // for (long id :
	 * stsIds) { // stsIdstr += id + ","; // } // stsIdstr =
	 * stsIdstr.substring(0, stsIdstr.length() - 1); // // cell1 // sql =
	 * "merge into RNO_NCS_WEIGHT_MID tar using " // + //
	 * " (select DISTINCT(cell) ,RESOURCE_USE_RATE ,(CASE WHEN NVL(AVAILABLE_CHANNEL_NUMBER,0)=0 THEN 0 ELSE TrAFFIC/AVAILABLE_CHANNEL_NUMBER END) AS pertraffic FROM RNO_STS where DESCRIPTOR_ID IN ("
	 * // + stsIdstr // + ")) traffic " // + " on(tar.cell1=traffic.cell ) " //
	 * + //
	 * " when matched then update set tar.PER_TRAFFIC_1=traffic.pertraffic,tar.RESOURCE_USE_RATE=traffic.RESOURCE_USE_RATE "
	 * ; // log.debug("填充cell1的无线资源利用率，单载频话务信息sql=" + sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * e.printStackTrace(); // log.error("更新cell1的无线资源利用率，单载频话务信息时出错！sql=" +
	 * sql); // return false; // } // // cell2 // sql =
	 * "merge into RNO_NCS_WEIGHT_MID tar using " // + //
	 * " (select DISTINCT(cell) ,RESOURCE_USE_RATE ,(CASE WHEN NVL(AVAILABLE_CHANNEL_NUMBER,0)=0 THEN 0 ELSE TrAFFIC/AVAILABLE_CHANNEL_NUMBER END) AS pertraffic FROM RNO_STS where DESCRIPTOR_ID IN ("
	 * // + stsIdstr // + ")) traffic " // + " on(tar.cell2=traffic.cell ) " //
	 * + //
	 * " when matched then update set tar.PER_TRAFFIC_2=traffic.pertraffic"; //
	 * log.debug("填充cell2的无线资源利用率，单载频话务信息sql=" + sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * e.printStackTrace(); // log.error("更新cell2的无线资源利用率，单载频话务信息时出错！sql=" +
	 * sql); // return false; // }
	 * 
	 * // 填充同频、邻频干扰因子 // cell1受cell2的干扰 sql =
	 * "merge into RNO_NCS_WEIGHT_MID tar using " +
	 * " (select distinct(cell||'_'||interference_cell) , cell,interference_cell as ncell ,ci,ca from RNO_INTERFERENCE where descriptor_id="
	 * + interferMatrixId + ") interm " +
	 * " on (tar.cell1=interm.cell and tar.cell2=interm.ncell) when matched then update set tar.cointer_1=interm.ci,tar.adjinter_1=interm.ca"
	 * ; log.debug("更新cell1受cell2的同频、邻频干扰因子。sql=" + sql);
	 * System.out.println("更新cell1受cell2的同频、邻频干扰因子。sql=" + sql); try {
	 * stmt.executeUpdate(sql); } catch (SQLException e) { e.printStackTrace();
	 * log.error("更新cell1受cell2的同频、邻频干扰因子时出错！sql=" + sql); return false; } //
	 * cell2受cell1的干扰 sql = "merge into RNO_NCS_WEIGHT_MID tar using " +
	 * " (select distinct(cell||'_'||interference_cell) , cell,interference_cell as ncell ,ci,ca from RNO_INTERFERENCE where descriptor_id="
	 * + interferMatrixId + ") interm " +
	 * " on (tar.cell2=interm.cell and tar.cell1=interm.ncell) when matched then update set tar.cointer_2=interm.ci,tar.adjinter_2=interm.ca"
	 * ; log.debug("更新cell12受cell1的同频、邻频干扰因子。sql=" + sql); try {
	 * stmt.executeUpdate(sql); } catch (SQLException e) { e.printStackTrace();
	 * log.error("更新cell2受cell1的同频、邻频干扰因子时出错！sql=" + sql); return false; }
	 * 
	 * // //---------------计算同频数、邻频数----------------// //
	 * -------------暂时不需要这些计算过程--------------// // sql=
	 * "update RNO_NCS_WEIGHT_MID set COFREQCNT=RNO_GETCOFREQCNT(FREQ1,FREQ2),ADJFREQCNT=RNO_GETADJACENTFREQCNT(FREQ1,FREQ2)"
	 * ; // log.debug("更新同频数量，邻区数量的sql："+sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * e.printStackTrace(); // log.error("更新同频数量、邻频数量时出错！sql=" + sql); // return
	 * false; // } // // //---------------计算同频话务量、邻频话务量-------------// // sql=
	 * "update RNO_NCS_WEIGHT_MID set COINTER_TRAFFIC=(PER_TRAFFIC_1*COINTER_1+PER_TRAFFIC_2*COINTER_2)*COFREQCNT,ADJFREQCNT=(PER_TRAFFIC_1*ADJINTER_1+PER_TRAFFIC_2*ADJINTER_2)"
	 * ; // log.debug("更新同频、邻频干扰话务量，sql="+sql); // try { //
	 * stmt.executeUpdate(sql); // } catch (SQLException e) { //
	 * e.printStackTrace(); // log.error("更新同频、邻频干扰话务量时出错！sql=" + sql); //
	 * return false; // } // //
	 * //----------汇总同频干扰话务量、邻区干扰话务量到RNO_NCS_CLUSTER_CELL表----------------// //
	 * sql=
	 * "merge into RNO_NCS_CLUSTER_CELL tar using (select cell1 as cell,sum(COINTER_TRAFFIC) as cotraffic,sum(ADJFREQCNT) as adjtraffic from RNO_NCS_WEIGHT_MID group by cell1) mid "
	 * // + // "on (tar.CLUSTER_ID="+clusterId+
	 * " and tar.cell=mid.cell) when matched then update set tar.COINTER_TRAFFIC=mid.cotraffic,tar.ADJINTER_TRAFFIC=mid.adjtraffic "
	 * // + //
	 * "when not matched then insert (CLUSTER_ID,CELL,COINTER_TRAFFIC,ADJINTER_TRAFFIC) values("
	 * +clusterId+",mid.cell,mid.cotraffic,mid.adjtraffic)"; //
	 * log.debug("汇总同频、邻频干扰话务量，sql="+sql); // try { // stmt.executeUpdate(sql);
	 * // } catch (SQLException e) { // e.printStackTrace(); //
	 * log.error("汇总同频、邻频干扰话务量时出错！sql=" + sql); // return false; // }
	 * 
	 * // ----干扰概率 sql = "merge into "+clusterCellTab+
	 * " tar using(select cell1 as cell,sum(COINTER_1) cval,sum(ADJINTER_1) as adjval from RNO_NCS_WEIGHT_MID where CLUSTER_ID="
	 * +clusterId+" group by cell1) mid " + " on (tar.CLUSTER_ID=" + clusterId +
	 * " and tar.cell=mid.cell) when matched then update set tar.CINTER_FACTOR=mid.cval,tar.ADJINTER_FACTOR=mid.adjval"
	 * ; log.debug("统计簇内各小区所受的干扰总概率之和.sql=" + sql); try {
	 * stmt.executeUpdate(sql); } catch (SQLException e1) {
	 * e1.printStackTrace(); return false; } // ------计算簇权重------------// //
	 * sql=
	 * "merge into RNO_NCS_CLUSTER tar using (select cluster_id,sum((cointer_traffic+adjinter_traffic)*cinter_factor) as weight from RNO_NCS_CLUSTER_CELL where cluster_id="
	 * +clusterId+" group by cluster_id)mid " // + //
	 * " on (tar.ID=mid.cluster_id) when matched then update set tar.WEIGHT=mid.weight"
	 * ; // sql = "merge into "+clusterTab+
	 * " tar using(select distinct(cluster_id),sum(COINTER_1+ADJINTER_1) as val from RNO_NCS_WEIGHT_MID group by cluster_id ) mid"
	 * + " on (tar.ID=" + clusterId + " and mid.cluster_id=" + clusterId +
	 * ") when matched then update set tar.WEIGHT=mid.val";
	 * log.debug("计算簇权重，sql=" + sql); try { stmt.executeUpdate(sql); } catch
	 * (SQLException e) { e.printStackTrace(); log.error("计算簇权重时出错！sql=" + sql);
	 * return false; }
	 * 
	 * long et = System.currentTimeMillis();
	 * 
	 * log.debug("退出方法：calculateClusterWeight。耗时：" + (et - st) + "ms"); return
	 * true; }
	 */

	public static void main_1(String[] args) {

		System.out
				.println(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_KEYS.length);
		System.out
				.println(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_TITLES.length);
		List<List<String>> clustcell = new ArrayList<List<String>>();

		List<String> one = null;
		one = new ArrayList<String>();
		one.add("a");
		one.add("b");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("c");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("f");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("l");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("m");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("k");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("a");
		one.add("z");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("c");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("l");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("k");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("m");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("z");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("b");
		one.add("f");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("m");
		one.add("f");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("f");
		one.add("z");
		clustcell.add(one);

		one = new ArrayList<String>();
		one.add("m");
		one.add("z");
		clustcell.add(one);
		//
		one = new ArrayList<String>();
		one.add("c");
		one.add("z");
		clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("l");
		// one.add("m");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("a");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("b");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("l");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("m");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("c");
		// clustcell.add(one);
		//
		// one = new ArrayList<String>();
		// one.add("f");
		// one.add("z");
		// clustcell.add(one);

		Map<String, List<Integer>> cellToIds = new HashMap<String, List<Integer>>();

		List<String> cells = Arrays.asList("a", "b", "c", "k", "l", "m", "z",
				"f");
		for (String c : cells) {
			List<Integer> ls = cellToIds.get(c);
			if (ls == null) {
				ls = new ArrayList<Integer>();
				cellToIds.put(c, ls);
			}
			for (int i = 0; i < clustcell.size(); i++) {
				List<String> o = clustcell.get(i);
				if (o.contains(c)) {
					ls.add(i);
				}
			}

		}

		List<String> key = new ArrayList<String>();
		for (List<String> o : clustcell) {
			key.add(o.get(0) + o.get(1));
		}

		List<Set<String>> cluster = null;
		List<List> ss = mergeToLargestCluster(clustcell, cellToIds, key);
		cluster = ss.get(0);
		List<List<List<String>>> inters = ss.get(1);
		for (int i = 0; i < cluster.size(); i++) {
			Set<String> s = cluster.get(i);
			// System.out.println("------ : " + s+"    \n对应干扰关系:");
			List<List<String>> inter = inters.get(i);
			for (List<String> inte : inter) {
				System.out.print(inte.get(0) + "->" + inte.get(1) + ",");
			}
			System.out.println("\n-----------------------\n");
		}

		// List<List<String>> res2 = getConnected(
		// Arrays.asList("k", "l", "c", "m", "f", "z"), key);
		// for (List<String> r : res2) {
		// System.out.println("---- " + r);
		// }
	}

	private static List<List<String>> getConnected(List<String> nodes,
			List<String> edges) {
		List<List<String>> res = new ArrayList<List<String>>();

		List<List<String>> trees = new ArrayList<List<String>>();
		List<List<String>> temptrees = new ArrayList<List<String>>();
		for (int oi = 0; oi < nodes.size(); oi++) {
			String n = nodes.get(oi);
			trees = new ArrayList<List<String>>();
			List<String> ss = new ArrayList<String>();
			ss.add(n);
			trees.add(ss);
			for (int ii = oi + 1; ii < nodes.size(); ii++) {
				String n2 = nodes.get(ii);
				for (int i = 0; i < trees.size(); i++) {
					List<String> branch = trees.get(i);
					boolean ass = true;
					int j = 0;
					for (j = 0; j < branch.size(); j++) {
						if (!edges.contains(n2 + branch.get(j))
								&& !edges.contains(branch.get(j) + n2)) {
							ass = false;
							break;
						}
					}
					if (ass) {
						branch.add(n2);
					} else {
						List<String> sub = branch.subList(0, j);
						sub.add(n2);
						temptrees.add(sub);
					}
				}
			}
			res.addAll(trees);
		}

		return res;
	}

	/**
	 * 准备频点干扰计算的元数据
	 * 
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author brightming 2014-2-13 上午11:55:20
	 */
	public long prepareFreqInterferMetaData(Connection conn, List<Long> ncsIds) {
		return prepareFreqInterferMetaData(conn, ncsIds, null, null);
	}

	/**
	 * 准备频点干扰计算的元数据,只需要部分cell
	 * 
	 * @param conn
	 * @param ncsIds
	 * @param cells
	 * @return
	 * @author brightming 2014-2-13 上午11:56:25
	 */
	public long prepareFreqInterferMetaData(Connection conn, List<Long> ncsIds,
			List<String> cells, String srcTabName) {
		log.debug("进入方法：prepareFreqInterferMetaData。conn=" + conn + ",ncsIds="
				+ ncsIds + ",cells=" + cells + ",srcTabName=" + srcTabName);
		if (conn == null) {
			log.error("prepareFreqInterferMetaData：未提供有效的数据库连接，不能执行操作！");
			return 0;
		}
		if (ncsIds == null || ncsIds.size() == 0) {
			log.error("prepareFreqInterferMetaData：未指定需要计算频点干扰的ncs范围，不能执行操作！");
			return 0;
		}

		if (srcTabName == null || "".equals(srcTabName.trim())) {
			srcTabName = "rno_ncs";// 默认取正式表的数据
		}
		String ncsIdStr = "";
		for (Long id : ncsIds) {
			ncsIdStr += id + ",";
		}
		ncsIdStr = ncsIdStr.substring(0, ncsIdStr.length() - 1);

		String cids = "";
		if (cells != null && !cells.isEmpty()) {
			for (String c : cells) {
				cids += "'" + c + "',";
			}
			cids = cids.substring(0, cids.length() - 1);
		}
		// 准备statement
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("prepareFreqInterferMetaData准备stmt出错！");
			e.printStackTrace();
			return 0;
		}

		// 转移到中间表
		String sql = "select MID.*,CELL.tch "
				+ " FROM("
				+ " select CELL,NCELL,SUM(CI_DIVIDER)/sum(reparfcn) as ci,sum(CA_DIVIDER)/sum(reparfcn) as ca,min(DEFINED_NEIGHBOUR) as DEFINED_NEIGHBOUR  from "
				+ srcTabName
				+ " where rno_ncs_desc_id in ( "
				+ ncsIdStr
				+ " ) "// 同频干扰度或邻频干扰度起码有一个不为0的记录
				+ ((cids == null || "".equals(cids.trim())) ? ""
						: " and cell in ( " + cids + ") ")
				+ " and cell_freq_cnt>0 and ncell_freq_cnt>0 group by cell,ncell  "
				+ " having sum(reparfcn)>0 "// 防止分母为0
				+ " and (SUM(CI_DIVIDER)/sum(reparfcn)>0 or sum(CA_DIVIDER)/sum(reparfcn)>0) "// ci或ca不为0的
				+ " )mid " + " inner join CELL " + " on(mid.cell=cell.label)";
		sql = "insert into RNO_NCS_FREQ_META(CELL,NCELL,CI,CA,DEFINED_NEIGHBOUR,CELL_FREQ) "
				+ sql;
		log.debug("转移到RNO_NCS_FREQ_META的sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("prepareFreqInterferMetaData准备转移待计算数据到中间表时出错！");
			return 0;
		}

		// 完善邻区频点信息
		// 防止小区表有重复的小区数据
		String cellSql = "(select cellraw.label,cellraw.tch from (select label,tch,row_number() over(partition by label order by tch nulls last) rn  from cell) cellraw where rn=1) cell";
		sql = "merge into RNO_NCS_FREQ_META tar using  "
				+ cellSql
				+ "  on ( tar.ncell=cell.label ) when matched then update set tar.NCELL_FREQ=cell.tch";
		log.debug("完善RNO_NCS_FREQ_META表的邻区频点信息的sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("prepareFreqInterferMetaData准备完善RNO_NCS_FREQ_META表的邻区频点信息时出错！");
			return 0;
		}

		// 计算数量
		sql = "select count(*) from RNO_NCS_FREQ_META";
		ResultSet rs = null;
		long res = 0;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		log.debug("退出prepareFreqInterferMetaData方法。返回：" + res);
		return res;
	}

	/**
	 * 在具有计算频点干扰所需数据的基础上计算频点干扰
	 * 
	 * @param conn
	 * @return gson格式的数据 {
	 *         'total':'','cells':[{'cell':'xxx','inter':[{'freq':'xx','ci':'','
	 *         c a ' : ' ' } , { . . } ] } ] }
	 * @author brightming 2014-2-13 下午12:33:54
	 */
	public CellFreqInterferList calculateFreqInterferResult(Connection conn) {
		log.debug("进入方法：calculateFreqInterferResult。conn=" + conn);
		if (conn == null) {
			log.error("calculateFreqInterferResult未带有效的数据库连接！无法进行计算！");
			return null;
		}

		// 准备statement
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("calculateFreqInterferResult准备stmt出错！");
			e.printStackTrace();
			return null;
		}

		// 获取所有的小区列表
		String sql = "select distinct(cell) from RNO_NCS_FREQ_META";
		ResultSet rs = null;
		List<String> allCells = new ArrayList<String>();
		String cell = null;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cell = rs.getString(1);
				if (cell != null) {
					allCells.add(cell);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 获取小区的计算数据进行计算，每次100个小区
		List<String> subCells = null;
		int size = allCells.size();
		int from = 0, to = 0;
		String cid = "";

		String ncell = "", cellFreq = "", ncellFreq = "";
		double ci, ca;
		to = from + 100;
		List<CellFreqInterfer> cellFreqRess = new ArrayList<CellFreqInterfer>();
		do {
			cid = "";
			if (to > size) {
				to = size;
			}
			if (from == to) {
				break;
			}
			subCells = allCells.subList(from, to);
			for (int i = 0; i < subCells.size(); i++) {
				cid += "'" + subCells.get(i) + "',";
			}
			cid = cid.substring(0, cid.length() - 1);
			sql = "select CELL,NCELL,CELL_FREQ,NCELL_FREQ,CI,CA from RNO_NCS_FREQ_META where cell in ( "
					+ cid + " ) order by cell";
			log.debug("获取RNO_NCS_FREQ_META表的部分小区的meta信息的sql=" + sql);
			List<String> ncellFreqs;
			List<Double> cis, cas;
			String preCell;
			CellFreqInterfer cellFreqRes;
			try {
				rs = stmt.executeQuery(sql);
				preCell = null;
				cell = "";
				ncellFreqs = new ArrayList<String>();
				cis = new ArrayList<Double>();
				cas = new ArrayList<Double>();
				while (rs.next()) {
					cell = rs.getString(1);
					ncell = rs.getString(2);
					cellFreq = rs.getString(3);
					ncellFreq = rs.getString(4);
					ci = rs.getDouble(5);
					ca = rs.getDouble(6);

					if (preCell == null || preCell.equals(cell)) {
						ncellFreqs.add(ncellFreq);
						cis.add(ci);
						cas.add(ca);
						preCell = cell;
					} else {
						// 对于一个cell进行计算
						cellFreqRes = calculateCellFreqRes(cell, cellFreq,
								ncellFreqs, cis, cas);
						if (cellFreqRes != null) {
							cellFreqRess.add(cellFreqRes);
						}
						// 重新初始化容器
						ncellFreqs.clear();
						cis.clear();
						cas.clear();

						ncellFreqs.add(ncellFreq);
						cis.add(ci);
						cas.add(ca);
						preCell = cell;
					}

				}
				// 最后一批次的处理
				cellFreqRes = calculateCellFreqRes(cell, cellFreq, ncellFreqs,
						cis, cas);
				if (cellFreqRes != null) {
					cellFreqRess.add(cellFreqRes);
				}

				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			from = to;
			to = from + 100;
		} while (true);

		CellFreqInterferList result = new CellFreqInterferList();
		double total = 0;
		for (CellFreqInterfer one : cellFreqRess) {
			total += one.getSum();
		}
		result.setCells(cellFreqRess);
		result.setTotal(total);

		return result;
	}

	/**
	 * 计算某个小区的各频点的干扰情况
	 * 
	 * @param cell
	 * @param cellFreq
	 * @param ncellFreqs
	 * @param cis
	 * @param cas
	 * @return
	 * @author brightming 2014-2-13 下午2:09:53
	 */
	private CellFreqInterfer calculateCellFreqRes(String cell, String cellFreq,
			List<String> ncellFreqs, List<Double> cis, List<Double> cas) {
		// TODO Auto-generated method stub
		if (cell == null || "".equals(cell.trim()) || cellFreq == null
				|| "".equals(cellFreq.trim()) || ncellFreqs == null
				|| ncellFreqs.isEmpty() || cis == null || cis.isEmpty()
				|| cas == null || cas.isEmpty()) {
			log.error("calculateCellFreqRes参数有误！cell=" + cell + ",cellFreq="
					+ cellFreq + ",ncellFreqs=" + ncellFreqs + ",cis=" + cis
					+ ",cas=" + cas);
			return null;
		}
		if (ncellFreqs.size() != cis.size() || ncellFreqs.size() != cas.size()) {
			log.error("传递的参数数量不同！ncellFreqs.size=" + ncellFreqs.size()
					+ ",cis.size=" + cis.size() + ",cas.size=" + cas.size());
			return null;
		}
		long t1 = System.currentTimeMillis(), t2;
		int start_900 = 1, end_900 = 94;
		int start_1800 = 512, end_1800 = 635;
		int start = 1, end = 1;

		String[] cfs = cellFreq.split(",");
		try {
			int freq = Integer.parseInt(cfs[0]);
			if (freq < 100) {
				start = start_900;
				end = end_900;
			} else {
				start = start_1800;
				end = end_1800;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		int freqCnt = end - start + 1;
		int[] cfsint = new int[freqCnt];
		double[] cfsci = new double[freqCnt];// 对应频点的ci累计值
		double[] cfsca = new double[freqCnt];// 对应频点的ca累计值
		// 固定，假设小区有这么多频点
		for (int i = 0; i < freqCnt; i++) {
			cfsint[i] = start + i;// Integer.parseInt(cfs[i]);
			cfsci[i] = 0;
			cfsca[i] = 0;
		}
		// 对cfsint排序
		int size = ncellFreqs.size();
		String ncellFreq = "";
		double ci = 0, ca = 0;

		for (int i = 0; i < size; i++) {
			ncellFreq = ncellFreqs.get(i);
			String[] nfs = ncellFreq.split(",");

			ci = cis.get(i);
			ca = cas.get(i);
			// 每个频点都要进行和每个邻频点进行比较
			for (int j = 0; j < freqCnt; j++) {
				for (String nf : nfs) {
					if ((cfsint[j] + "").equals(nf)) {
						// 同频干扰系数
						cfsci[j] += ci;
					}
					if (((cfsint[j] + 1) + "").equals(nf)
							|| ((cfsint[j] - 1) + "").equals(nf)) {
						// 邻频干扰系数
						cfsca[j] += ca;
					}

				}
			}
		}
		List<Map<String, Object>> inter = new ArrayList<Map<String, Object>>();
		Map<String, Object> one = null;
		double sum = 0;
		for (int i = 0; i < freqCnt; i++) {
			one = new HashMap<String, Object>();
			one.put("freq", cfsint[i]);
			one.put("ci", cfsci[i]);
			one.put("ca", cfsca[i]);
			sum += cfsci[i] + cfsca[i];
			inter.add(one);
		}
		CellFreqInterfer result = new CellFreqInterfer();
		result.setCell(cell);
		result.setSum(sum);
		result.setInter(inter);

		t2 = System.currentTimeMillis();
		log.debug("calculateCellFreqRes耗时：" + (t2 - t1) + "ms");
		return result;
	}

	/**
	 * 计算小区理想覆盖距离 主小区覆盖方向120度内最近的3个（可设置）非同站且同频段的关联邻小区的平均距离。 邻区也不能同频段
	 * 小区理想覆盖距离=(∑_(i=1)^N▒至邻近基站距离)/N
	 * 上式中同一结构层是指只考虑相同层次的小区，例如900小区只考虑周边最近的N个900基站的距离，而不考虑1800基站或者其他底层站、室内站。
	 * 
	 * @param conn
	 * @param ncsTab
	 *            ncs数据源表
	 * @param ncsId
	 *            需要计算的ncsid
	 * @param cellResTabName
	 *            计算出来的小区结果存放表
	 * @param lowestN
	 *            考虑的n个小区
	 * @return
	 * @author brightming 2014-2-17 上午11:28:02
	 */
	public boolean calculateCellIdealCoverDis(Statement stmt, String ncsTab,
			long ncsId, String cellResTabName, int lowestN) {
		log.debug("进入dao方法：calculateCellIdealCoverDis。stmt=" + stmt
				+ ",ncsTab=" + ncsTab + ",ncsId=" + ncsId + ",cellResTabName="
				+ cellResTabName);
		if (stmt == null) {
			log.error("计算理想覆盖距离时，未提供数据库连接！");
			return false;
		}
		if (ncsTab == null || "".equals(ncsTab.trim())) {
			log.error("计算理想覆盖距离时，未提供ncs数据表名称！");
			return false;
		}
		if (cellResTabName == null || "".equals(cellResTabName.trim())) {
			log.error("计算理想覆盖距离时，未提供小区结果存放表名称！");
			return false;
		}

		String innerSql = "SELECT mid4.*, row_number()over(partition BY MID4.cell order by mid4.dis ASC) seq2 from ("
				+ " SELECT MID3.CELL,MID3.NCELL,MID3.N_SITE,MID3.DIS,MID3.S_DIR,MID3.N_DIR AS S_N_ANGEL_DIR,MID3.LOW_EDGE,MID3.HIGH_EDGE ,row_number()over(partition BY MID3.cell,mid3.n_site order by mid3.dis ASC) seq FROM "
				+ " ( "
				+ " select mid2.*,CELL.bcch as n_bcch ,CELL.SITE AS N_SITE,calculateDirToNorth(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as n_dir,CELL.LONGITUDE as n_lon,cell.LATITUDE as n_lat,FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as dis FROM "
				+ " ( "
				+ " select mid1.*,cell.bcch as s_bcch,CELL.BEARING as s_dir,CELL.SITE AS S_SITE,(case when CELL.BEARING<60 then 300+CELL.BEARING else CELL.BEARING-60 end) as low_edge,(case when CELL.BEARING>300 then CELL.BEARING-300 else CELL.BEARING+60 end ) as high_edge ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
				+ " from( "
				+ " select cell,ncell,min(NCELL_INDOOR) as NCELL_INDOOR from "
				+ ncsTab
				+ " where RNO_NCS_DESC_ID="
				+ ncsId
				+ " and interfer >0.05 group by cell,ncell "
				+ " )mid1 "
				+ " inner join cell "
				+ " on  "
				+ " ( "
				+ " mid1.cell=cell.LABEL and NCELL_INDOOR='N'"
				+ " ) "
				+ " )mid2 "
				+ " inner join CELL "
				+ " on "
				+ " ( "
				+ " MID2.ncell=CELL.label "
				+ " and (mid2.s_bcch>100 and CELL.bcch>100 or MID2.s_bcch<100 and cell.bcch<100 AND MID2.S_SITE!=CELL.SITE) "// 计算同频段
				+ " ) "
				+ " order BY cell asc,dis asc "
				+ " )MID3 "
				+ "WHERE MID3.DIS>0  AND fun_rno_isbetweenlowandhigh(MID3.LOW_EDGE,MID3.HIGH_EDGE,N_DIR)='y' "
				+ " )mid4  WHERE seq=1  ORDER BY MID4.CELL,MID4.NCELL ";

		// 用于输出参考邻区的 sql
		String outputSql = "SELECT mid4.*, row_number()over(partition BY MID4.cell order by mid4.dis ASC) seq2 from ("
				+ " SELECT MID3.CELL,MID3.NCELL,MID3.N_SITE,MID3.DIS,MID3.S_DIR,MID3.N_DIR AS S_N_ANGEL_DIR,MID3.LOW_EDGE,MID3.HIGH_EDGE ,row_number()over(partition BY MID3.cell,mid3.n_site order by mid3.dis ASC) seq FROM "
				+ " ( "
				+ " select mid2.*,CELL.bcch as n_bcch ,CELL.SITE AS N_SITE,calculateDirToNorth(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as n_dir,CELL.LONGITUDE as n_lon,cell.LATITUDE as n_lat,FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE) as dis FROM "
				+ " ( "
				+ " select mid1.*,cell.bcch as s_bcch,CELL.BEARING as s_dir,CELL.SITE AS S_SITE,(case when CELL.BEARING<60 then 300+CELL.BEARING else CELL.BEARING-60 end) as low_edge,(case when CELL.BEARING>300 then CELL.BEARING-300 else CELL.BEARING+60 end ) as high_edge ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
				+ " from( "
				+ " select cell,ncell,min(NCELL_INDOOR) as NCELL_INDOOR from "
				+ ncsTab
				+ " where RNO_NCS_DESC_ID="
				+ ncsId
				+ " and interfer >0.05 group by cell,ncell "
				+ " )mid1 "
				+ " inner join cell "
				+ " on  "
				+ " ( "
				+ " mid1.cell=cell.LABEL and NCELL_INDOOR='N'"
				+ " ) "
				+ " )mid2 "
				+ " inner join CELL "
				+ " on "
				+ " ( "
				+ " MID2.ncell=CELL.label "
				+ " and (mid2.s_bcch>100 and CELL.bcch>100 or MID2.s_bcch<100 and cell.bcch<100 )"// AND
																									// MID2.S_SITE!=CELL.SITE)
																									// "//
																									// 计算同频段
				+ " ) "
				+ " order BY cell asc,dis asc "
				+ " )MID3 "
				+ "WHERE MID3.DIS>0  "// AND
										// fun_rno_isbetweenlowandhigh(MID3.LOW_EDGE,MID3.HIGH_EDGE,N_DIR)='y'
										// "
				+ " )mid4  WHERE seq=1  ORDER BY MID4.CELL,MID4.NCELL ";

		String sql = "select mid5.cell,avg(dis) as dis from " + "(" + innerSql
				+ " )mid5 " + " where seq2<=" + lowestN + " group by cell";
		log.debug("准备计算小区理想覆盖距离的sql=" + sql);
		sql = "Merge into "
				+ cellResTabName
				+ " tar using ( "
				+ sql
				+ " ) src  on ( tar.ncs_id="
				+ ncsId
				+ " and tar.cell=src.cell ) when matched then update set tar.EXPECTED_COVER_DIS=src.dis";
		log.debug("更新理想覆盖距离的sql=" + sql);

		// 准备statement
		// Statement stmt = null;
		// try {
		// stmt = conn.createStatement();
		// } catch (SQLException e) {
		// log.error("计算理想覆盖距离准备stmt出错！");
		// e.printStackTrace();
		// return false;
		// }

		// --查看小区参考的结果
		if (log.isInfoEnabled()) {
			log.debug("查看参考邻区情况：" + outputSql);
			List<Map<String, Object>> midRes = RnoHelper.commonQuery(stmt,
					outputSql);
			// log.debug("cell,ncell,n_site,dis,S_DIR,S_N_ANGEL_DIR,LOW_EDGE,HIGH_EDGE");
			// for (Map<String, Object> one : midRes) {
			// log.debug(one.get("CELL") + "," + one.get("NCELL") +
			// ","+one.get("N_SITE")+","
			// +
			// one.get("DIS")+","+one.get("S_DIR")+","+one.get("S_N_ANGEL_DIR")+","+one.get("LOW_EDGE")+","+one.get("HIGH_EDGE"));
			// }

			String[] titles = { "CELL", "NCELL", "邻区site", "dis", "主小区方向角",
					"主小区到邻区夹角", "角度允许范围下限", "角度允许上限" };
			String[] keys = { "CELL", "NCELL", "N_SITE", "DIS", "S_DIR",
					"S_N_ANGEL_DIR", "LOW_EDGE", "HIGH_EDGE" };

			List<String[]> titless = new ArrayList<String[]>();
			titless.add(titles);
			List<String[]> keyss = new ArrayList<String[]>();
			keyss.add(keys);

			String tempNeiPath = "/tmp/neighbourRef.xlsx";
			ByteArrayOutputStream baos = FileTool.putDataOnExcelOutputStream(
					Arrays.asList("参考邻区"), titless, keyss,
					Arrays.asList(midRes));
			InputStream is = new ByteArrayInputStream(baos.toByteArray());
			// String cellClusterPath = dir + "ncs_res_" + taskId + ".xls";
			log.debug("保存参考邻区数据文件：" + tempNeiPath);
			boolean ok = FileTool.saveInputStream(tempNeiPath, is);
			log.debug("保存参考邻区数据文件：" + tempNeiPath + ",保存结果：" + ok);
			midRes = null;
			baos = null;
		}
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}
		return true;
	}

	/**
	 * 计算过覆盖系数 为： 主小区到最远关联邻小区的距离/(主小区理想覆盖距离*系数)
	 * 
	 * @param stmt
	 * @param ncsTab
	 * @param ncsId
	 * @param cellResTabName
	 * @param adjustFactor
	 *            调节系数，1.6-2.0 ,默认1.6
	 * @return
	 * @author brightming 2014-2-17 下午2:26:30
	 */
	public boolean calculateCellOverCoverFactor(Statement stmt, String ncsTab,
			long ncsId, String cellResTabName, float adjustFactor) {
		log.debug("进入dao方法：calculateCellOverCoverFactor。stmt=" + stmt
				+ ",ncsTab=" + ncsTab + ",ncsId=" + ncsId + ",cellResTabName="
				+ cellResTabName + ",adjustFactor=" + adjustFactor);
		if (stmt == null) {
			log.error("计算过覆盖系数时，未提供数据库连接！");
			return false;
		}
		if (ncsTab == null || "".equals(ncsTab.trim())) {
			log.error("计算过覆盖系数时，未提供ncs数据表名称！");
			return false;
		}
		if (cellResTabName == null || "".equals(cellResTabName.trim())) {
			log.error("计算过覆盖系数时，未提供小区结果存放表名称！");
			return false;
		}

		// 准备statement
		// Statement stmt = null;
		// try {
		// stmt = conn.createStatement();
		// } catch (SQLException e) {
		// log.error("计算过覆盖系数准备stmt出错！");
		// e.printStackTrace();
		// return false;
		// }

		String sql = "merge into "
				+ cellResTabName
				+ " tar using( "
				+ " select mid2.cell,max(FUN_RNO_GetDistance(s_lon,s_lat,CELL.LONGITUDE,cell.LATITUDE)) as dis FROM "
				+ " ( "
				+ " select mid1.* ,CELL.LONGITUDE as s_lon,CELL.LATITUDE as s_lat "
				+ " from( "
				+ " select cell,ncell  from "
				+ ncsTab
				+ " where RNO_NCS_DESC_ID="
				+ ncsId
				+ " and interfer >0.05 group by cell,ncell "
				+ " )mid1 "
				+ " inner join cell "
				+ " on  "
				+ " ( "
				+ " mid1.cell=cell.LABEL "
				+ " ) "
				+ " )mid2 "
				+ " inner join CELL "
				+ " on "
				+ " ( "
				+ " MID2.ncell=CELL.label "
				+ " ) "
				+ " group by cell "
				+ " order BY cell asc,dis asc "
				+ " ) src  "
				+ " on (tar.cell=src.cell) "
				+ " when matched then update set tar.OVERSHOOTING_FACT=(case when tar.EXPECTED_COVER_DIS=0 then null else src.dis/(tar.EXPECTED_COVER_DIS*"
				+ adjustFactor + ") end)";

		log.debug("计算过覆盖系数的sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算过覆盖系数时出错！");
			return false;
		} finally {
			// try {
			// stmt.close();
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }
		}

		return true;
	}

	/**
	 * 计算小区相关指标的总入口
	 * 
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 * @param params
	 * @return
	 * @author brightming 2014-2-17 下午12:01:17
	 */
	public boolean calculateCellRes(Connection conn, String ncsTab,
			String cellResTab, String clusterTab, String clusterCellTab,
			List<Long> ncsIds, Map<String, Object> params) {
		try {
			for (long ncsId : ncsIds) {
				calculateCellResForEachNcs(conn, ncsTab,
						cellResTab, clusterTab, clusterCellTab, ncsId, params);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 计算每个ncs的小区结果信息
	 * 
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsId
	 * @param params
	 * @return
	 * @author brightming 2014-2-17 下午12:26:46
	 */
	private boolean calculateCellResForEachNcs(Connection conn, String ncsTab,
			String cellResTab, String clusterTab, String clusterCellTab,
			long ncsId, Map<String, Object> params) {

		// 判断cellResTab表中是否有ncs相关的小区信息
		String sql = "select count(*) from " + cellResTab + " where NCS_ID="
				+ ncsId;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("计准备stmt出错！");
			e.printStackTrace();
			return false;
		}

		ResultSet rs = null;
		long cnt = -1;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cnt = rs.getLong(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}

		if (cnt <= 0) {
			// 还没有数据，需要插入小区数据
			// sql = "insert into "
			// + cellResTab
			// +
			// " (CELL,NCS_ID,FREQ_CNT) SELECT distinct(CELL),RNO_NCS_DESC_ID,min(CELL_FREQ_CNT) FROM "
			// + ncsTab + " where RNO_NCS_DESC_ID=" + ncsId
			// + " group by RNO_NCS_DESC_ID,cell";
			//
			sql = "insert into " + cellResTab
					+ " (CELL,NCS_ID,FREQ_CNT) SELECT CELL," + ncsId
					+ ",min(CELL_FREQ_CNT) FROM " + ncsTab
					+ " where RNO_NCS_DESC_ID=" + ncsId + " group by cell";
			log.debug("将ncs数据[" + ncsTab + "]转移到小区分析结果表[" + cellResTab
					+ "]的sql=" + sql);
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("转移小区相关数据到小区分析结果表时出错！");
				try {
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		}

		// -------------计算小区理想覆盖距离--------------------------//
		int lowestN = 3;
		if (params != null && params.containsKey("IDEAL_DIS_NCELL_CNT")) {// 理想覆盖距离考虑的邻区数量
			try {
				lowestN = (Integer) params.get("IDEAL_DIS_NCELL_CNT");
			} catch (Exception e) {
				e.printStackTrace();
				lowestN = 3;
			}
		}
		if (lowestN <= 0) {
			lowestN = 3;
		}
		this.calculateCellIdealCoverDis(stmt, ncsTab, ncsId, cellResTab,
				lowestN);

		// --------------过覆盖系数------------------------//
		this.calculateCellOverCoverFactor(stmt, ncsTab, ncsId, cellResTab, 1.6f);

		// -------------计算干扰系数----------------------//
		this.calculateCellInterferFactor(conn, ncsId, ncsTab, cellResTab);

		// --------------计算网内干扰系数-------------------//
		this.calculateCellNetInterFactor(stmt, ncsId, ncsTab, cellResTab, null);

		// -------------网络结构指数----------------------//
		this.calculateNetworkStrucFactor(conn, ncsId, ncsTab, cellResTab);

		// ------------冗余覆盖度--------------------------//
		this.calculateRedundantCoverFacotr(conn, ncsId, ncsTab, cellResTab);

		// ------------重叠覆盖度--------------------------//
		this.calculateOverlapCoverFactor(conn, ncsId, ncsTab, cellResTab);

		// ------------小区检测次数------------------------//
		this.calculateDetectCnt(conn, ncsId, ncsTab, cellResTab);

		// -------------关联邻小区数-------------------------//
		this.calculateStrongAssNcellCnt(conn, ncsId, ncsTab, cellResTab);

		// -----------小区覆盖分量------------------------//
		this.calculateCellCover(conn, ncsId, ncsTab, clusterTab,
				clusterCellTab, cellResTab);
		// ------------小区容量分量-----------------------//
		this.calculateCapacityDestroy(conn, ncsId, ncsTab, clusterTab,
				clusterCellTab, cellResTab);

		// 输出原始文件信息
		dumpRnoNcsMidData(stmt);
		return true;
	}

	/**
	 * 输出rno_ncs_mid表的原始信息
	 * 
	 * @param stmt
	 * @author brightming 2014-7-13 下午3:28:25
	 */
	private void dumpRnoNcsMidData(Statement stmt) {
		if (!DumpHelper.needDump("struct-ana")) {
			return;
		}
		String preKey = System.currentTimeMillis() + "";
		String dir = DumpHelper.getDumpDir("struct-ana");
		if (StringUtils.isEmpty(dir)) {
			dir = "/tmp/";
		}

		String[] keyArray = { "RNO_NCS_DESC_ID", "CELL", "CHGR", "BSIC",
				"ARFCN", "DEFINED_NEIGHBOUR", "RECTIMEARFCN", "REPARFCN",
				"TIMES", "NAVSS", "TIMES1", "NAVSS1", "TIMES2", "NAVSS2",
				"TIMES3", "NAVSS3", "TIMES4", "NAVSS4", "TIMES5", "NAVSS5",
				"TIMES6", "NAVSS6", "TIMESRELSS", "TIMESRELSS2", "TIMESRELSS3",
				"TIMESRELSS4", "TIMESRELSS5", "TIMESABSS", "TIMESALONE",
				"NCELL", "DISTANCE", "INTERFER", "CA_INTERFER", "NCELLS",
				"CELL_FREQ_CNT", "NCELL_FREQ_CNT", "SAME_FREQ_CNT",
				"ADJ_FREQ_CNT", "CI_DIVIDER", "CA_DIVIDER", "CELL_INDOOR",
				"NCELL_INDOOR" };
		String[] titleArray = { "RNO_NCS_DESC_ID", "CELL", "CHGR", "BSIC",
				"ARFCN", "DEFINED_NEIGHBOUR", "RECTIMEARFCN", "REPARFCN",
				"TIMES", "NAVSS", "TIMES1", "NAVSS1", "TIMES2", "NAVSS2",
				"TIMES3", "NAVSS3", "TIMES4", "NAVSS4", "TIMES5", "NAVSS5",
				"TIMES6", "NAVSS6", "TIMESRELSS", "TIMESRELSS2", "TIMESRELSS3",
				"TIMESRELSS4", "TIMESRELSS5", "TIMESABSS", "TIMESALONE",
				"NCELL", "DISTANCE", "INTERFER", "CA_INTERFER", "NCELLS",
				"CELL_FREQ_CNT", "NCELL_FREQ_CNT", "SAME_FREQ_CNT",
				"ADJ_FREQ_CNT", "CI_DIVIDER", "CA_DIVIDER", "CELL_INDOOR",
				"NCELL_INDOOR" };

		String countsql = "SELECT count(*) count from rno_ncs_mid";
		List<Map<String, Object>> countList = RnoHelper.commonQuery(stmt,
				countsql);
		String countString = countList.get(0).get("COUNT").toString();
		double count = Double.parseDouble(countString);
		log.debug("count总记录数：" + count);
		int pageSize = 50000;
		int totalpage = (int) Math.ceil(count / pageSize);// 进位取整
		log.debug("totalpage:" + totalpage);
		String path = dir + "ncs_mid_data-" + preKey + ".csv";// /rnodata/ana_result/ncs.xlsx
		String lineSepa = System.getProperty("line.separator");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			log.error("无法正确构建dump的输出流，无法完成RNO_NCS_MID信息的dump：dir=" + dir);
			return;
		}

		// 输出标题
		int length = titleArray.length;
		try {
			for (int j = 0; j < length; j++) {
				writer.write(titleArray[j] + "\t");
			}
			writer.write(lineSepa);// 换行
		} catch (Exception e) {
			e.printStackTrace();
			log.error("无法输出rno_ncs_mid的标题头，dump无法进行！");
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			return;
		}
		// 输出数据
		log.debug("生成ncs csv dump信息。。。。。。。。。。。。。。。。。。。。。。开始");
		for (int k = 0; k < totalpage; k++) {
			log.debug("第" + (k + 1) + "页RNO_NCS_MID dump信息");
			double intervalue = pageSize;
			if (k == totalpage - 1) {
				double aa = Math.floor(count / pageSize);// 四舍五入
				intervalue = count - aa * pageSize;
			}
			String datasql = "select *"
					+ " from (select *"
					+ "       from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
					+ "               from rno_ncs_MID t) p "
					+ "      where p.\"row_number\" > " + k * pageSize + ") q "
					+ "  where rownum <= " + intervalue;
			List<Map<String, Object>> dataList = RnoHelper.commonQuery(stmt,
					datasql);
			// log.debug("dataList.size():" + dataList.size());

			try {
				// 输出查询出的数据
				if (dataList != null && !dataList.isEmpty()) {
					int keyArrayLength = keyArray.length;
					int dataListSize = dataList.size();
					for (int i = 0; i < dataListSize; i++) {// 每行数据
						Map<String, Object> map = dataList.get(i);
						for (int j = 0; j < keyArrayLength; j++) {
							String curValue = map.get(keyArray[j]) + "";
							writer.write(curValue + "\t");
						}
						writer.write(lineSepa);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("dump rno_ncs_mid信息时遇到错误，输出不完整！当前输出第" + k + "页，每页"
						+ pageSize + "条记录");
				break;
			}
		}

		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 以ncs为单位，计算小区的被干扰系数、干扰源系数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @return
	 * @author Liang YJ 2014-2-10 上午11:13:07
	 */
	public boolean calculateCellInterferFactor(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法.conn:"
				+ conn
				+ ", ncsId:"
				+ ncsDescId
				+ ", ncsNcellTabName:"
				+ ncsNcellTabName);
		boolean flag = calculateBeInterfered(conn, ncsDescId,
				ncsNcellTabName, "CELL", "BE_INTERFER", "被干扰系数", cellResTab);
		if (!flag) {
			return flag;
		}
		flag = calculateSrcInterfered(conn, ncsDescId,
				ncsNcellTabName, "NCELL", "SRC_INTERFER", "干扰源系数", cellResTab);
		return flag;

	}

	/**
	 * 计算网内干扰系数 小区网内干扰系数=∑_i〖〖(CO〗_si*〖NCO〗_si/N_s 〗+〖ADJ〗_si*〖NADJ〗_si/N_s )
	 * 
	 * @return
	 * @author brightming 2014-7-16 下午2:06:37
	 */
	public boolean calculateCellNetInterFactor(Statement stmt, long ncsDescId,
			String ncsNcellTabName, String cellResTab,
			Map<String, String> result) {
		log.debug("开始计算网内干扰系数。ncsDescId=" + ncsDescId + ",ncsNcellTabName="
				+ ncsNcellTabName + ",cellResTab=" + cellResTab);

		String sql = "select CELL,sum(interfer*(same_freq_cnt/cell_freq_cnt)+ca_interfer*(adj_freq_cnt/cell_freq_cnt)) AS CELL_NET_INTERFER from "
				+ ncsNcellTabName
				+ "  WHERE  RNO_NCS_DESC_ID="
				+ ncsDescId
				//2014-7-23 gmh增加：距离因素、同频、邻频干扰系数的考虑
				+ " and cell_freq_cnt<>0 and interfer>0.05 and ca_interfer>0.05 and distance<=15000 GROUP BY CELL";
		
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on (TAR.CELL=SRC.CELL) when matched then update set tar.CELL_NET_INTERFER=SRC.CELL_NET_INTERFER";
		log.debug("计算网内干扰的sql=" + sql);
		try {
			int cnt = stmt.executeUpdate(sql);
			log.debug("计算网内干扰系数，影响记录：" + cnt);
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算小区的网内干扰出错！sql=" + sql);
			if (result != null) {
				result.put("计算小区网内干扰", "出错");
			}
		}
		if (result != null) {
			result.put("计算小区网内干扰", "成功");
		}
		return false;
	}

	/**
	 * 以ncs为单位，网络结构指数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @return
	 * @author Liang YJ 2014-2-11 上午10:43:07
	 */
	public boolean calculateNetworkStrucFactor(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		// 更新gsm900
		boolean flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName,
				cellResTab, "CELL", "NCELL_FREQ_CNT", "NET_STRUCT_FACTOR",
				"BCCH<100", "网络结构指数(gsm900)",
				RnoConstant.BusinessConstant.GSM900_FEQ_CNT);
		if (!flag) {
			return flag;
		}
		// 更新gsm1800
		flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName, cellResTab,
				"CELL", "NCELL_FREQ_CNT", "NET_STRUCT_FACTOR", "BCCH>100",
				"网络结构指数(gsm1800)", RnoConstant.BusinessConstant.GSM1800_FEQ_CNT);
		return flag;

	}

	/**
	 * 以ncs为单位，冗余覆盖指数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @return
	 * @author Liang YJ 2014-2-11 15:30:07
	 */
	public boolean calculateRedundantCoverFacotr(Connection conn,
			long ncsDescId, String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		// 更新gsm900
		boolean flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName,
				cellResTab, "NCELL", "CELL_FREQ_CNT", "REDUNT_COVER_FACT",
				"BCCH<100", "网络结构指数(gsm900)",
				RnoConstant.BusinessConstant.GSM900_FEQ_CNT);
		if (!flag) {
			return flag;
		}
		// 更新gsm1800
		flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName, cellResTab,
				"NCELL", "CELL_FREQ_CNT", "REDUNT_COVER_FACT", "BCCH>100",
				"网络结构指数(gsm1800)", RnoConstant.BusinessConstant.GSM1800_FEQ_CNT);
		return flag;
	}

	/**
	 * 重叠覆盖度
	 * 
	 * @param conn
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @return
	 * @author Liang YJ 2014-2-11 15:30:07
	 */
	public boolean calculateOverlapCoverFactor(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		log.debug("conn:" + conn + ", ncsId:" + ncsDescId
				+ ", ncsNcellTabName:" + ncsNcellTabName);
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		String factor = "OVERLAP_COVER";
		String groupBy = "CELL";
		String statistics = "sum(CI)+1";
		String innerSelectSql = getCiSql(ncsNcellTabName, groupBy);
		String selectSql = "select rn.RNO_NCS_DESC_ID RNO_NCS_DESC_ID, rn."
				+ groupBy + " " + groupBy + "," + statistics + " " + factor
				+ " from (" + innerSelectSql
				+ ") rn group by rn.RNO_NCS_DESC_ID,rn." + groupBy;
		String mergeSql = getMergeSql(cellResTab, selectSql, factor, groupBy,
				ncsDescId);
		// System.out.println("mergeSql:" + mergeSql);
		log.debug("重叠覆盖度mergeSql:" + mergeSql);
		PreparedStatement pStatement = null;
		boolean flag = false;
		try {
			pStatement = conn.prepareStatement(mergeSql);
			pStatement.setLong(1, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			log.error("更新重叠覆盖度出错");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}

	/**
	 * 小区检测次数
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 *            小区结果表
	 * @return
	 * @author Liang YJ 2014-2-12 9:53:25
	 */
	public boolean calculateDetectCnt(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入calculateCellInterferFactor(Connection conn, long ncsId,String ncsNcellTabName)方法");
		log.debug("conn:" + conn + ", ncsId:" + ncsDescId
				+ ", ncsNcellTabName:" + ncsNcellTabName);
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		/*
		 * String factor = "DETECT_CNT"; String groupBy = "NCELL"; String
		 * statistics = "count(*)"; String innerSelectSql =
		 * "select RNO_NCS_DESC_ID," + groupBy +
		 * ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,DISTANCE,sum(CI_DIVIDER)/sum(REPARFCN) CI from "
		 * + ncsNcellTabName +
		 * " where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,DISTANCE"
		 * ; String selectSql = "select rn.RNO_NCS_DESC_ID RNO_NCS_DESC_ID, rn."
		 * + groupBy + " " + groupBy + "," + statistics + " " + factor +
		 * " from (" + innerSelectSql + ") rn inner join " + cellResTab +
		 * " rncar on rncar.NCS_ID=rn.RNO_NCS_DESC_ID and rncar.CELL=rn." +
		 * groupBy +
		 * " where rncar.EXPECTED_COVER_DIS*1.6<rn.DISTANCE and rn.CI>0.05 group by rn.RNO_NCS_DESC_ID, rn."
		 * + groupBy; String mergeSql = getMergeSql(cellResTab, selectSql,
		 * factor, groupBy, ncsDescId); // System.out.println("mergeSql:" +
		 * mergeSql); log.debug("重叠覆盖度mergeSql:" + mergeSql); PreparedStatement
		 * pstatement = null; boolean flag = false; try { pstatement =
		 * conn.prepareStatement(mergeSql); pstatement.setLong(1, ncsDescId);
		 * pstatement.executeUpdate(); flag = true; } catch (SQLException e) {
		 * log.error("更新小区检测次数"); log.error(e.getStackTrace());
		 * e.printStackTrace(); flag = false; } finally { if (null !=
		 * pstatement) { try { pstatement.close(); } catch (SQLException e) {
		 * log.error("关闭pstatement失败"); log.error(e.getStackTrace()); } } }
		 * return flag;
		 */

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("计算小区检测次数方法中，准备数据库statement出错！");
			e.printStackTrace();
			return false;
		}

		// ---包含室分小区的情况---//
		String sql = "select mid1.ncell,count(*) as cnt "
				+ " FROM ( select cell,ncell,distance from "
				+ ncsNcellTabName
				+ " where RNO_NCS_DESC_ID="
				+ ncsDescId
				+ " and INTERFER>0.05 and distance<>10000000000 "
				+ " group by cell,ncell,distance )mid1 "
				+ " inner join (select cell,EXPECTED_COVER_DIS from "
				+ cellResTab
				+ " where NCS_ID="
				+ ncsDescId
				+ " )mid2 on(MID1.ncell=mid2.cell and MID1.distance>1.6*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";

		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.DETECT_CNT=src.cnt";

		int affectCnt = 0;
		log.debug("计算包含室分小区在内的小区检测数:sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算包含室分小区在内的小区检测数更新数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("计算包含室分小区在内的小区检测数时出错！sql=" + sql);
			e.printStackTrace();
		}

		// -------不包含室分小区------------//
		sql = "select mid1.ncell,count(*) as cnt "
				+ " FROM ( select cell,ncell,distance from "
				+ ncsNcellTabName
				+ " where RNO_NCS_DESC_ID="
				+ ncsDescId
				+ " and INTERFER>0.05 and distance<>10000000000 and CELL_INDOOR='N' "
				+ " group by cell,ncell,distance )mid1 "
				+ " inner join (select cell,EXPECTED_COVER_DIS from "
				+ cellResTab
				+ " where NCS_ID="
				+ ncsDescId
				+ " )mid2 on(MID1.ncell=mid2.cell and MID1.distance>1.6*MID2.EXPECTED_COVER_DIS) group by mid1.ncell";

		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.DETECT_CNT_EXINDR=src.cnt";

		log.debug("计算排除室分小区在外的小区检测数:sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算排除室分小区在外的小区检测数更新数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("计算排除室分小区在外的小区检测数时出错！sql=" + sql);
			e.printStackTrace();
		}

		// sql = "select cell,DETECT_CNT,DETECT_CNT_EXINDR from "
		// + cellResTab;
		// List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		// for (Map<String, Object> one : res) {
		// System.out.println("cell=" + one.get("CELL")
		// + ",DETECT_CNT=" + one.get("DETECT_CNT")
		// + ",DETECT_CNT_EXINDR="
		// + one.get("DETECT_CNT_EXINDR"));
		// }
		return true;

	}

	/**
	 * 关联邻小区数 邻小区的NCS测量报告中，服务小区在邻小区测量报告中出现且信号强度差>-12dB的比例 ≥ 5%，
	 * 可认为该邻小区是主小区的关联邻小区。 需要计算2个结果：一个是包含室分小区，一个是不包含室分小区
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 *            小区结果表
	 * @return
	 * @author brightming 2014-3-24 上午11:40:35
	 */
	public boolean calculateStrongAssNcellCnt(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab) {
		log.debug("进入方法：calculateStrongAssNcellCnt。conn=" + conn
				+ ",ncsDescId=" + ncsDescId + ",ncsNcellTabName="
				+ ncsNcellTabName + ",cellResTab");
		if (conn == null) {
			log.error("方法：calculateStrongAssNcellCnt的参数无效！");
			return false;
		}
		// String sql = "select NCELL,COUNT(*) AS CNT from " + ncsNcellTabName
		// + " where RNO_NCS_DESC_ID=" + ncsDescId
		// + " and INTERFER>0.05  GROUP BY NCELL";
		// 2014-7-13 gmh修正错误
		String sql = "select ncell,count(ncell) as CNT from ( "
				+ " select ncell,cell,avg(INTERFER) from " + ncsNcellTabName
				+ " where RNO_NCS_DESC_ID= " + ncsDescId
				+ " group by ncell,cell " + " having avg(INTERFER)>0.05 "
				+ ") " + " group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.INTERFER_NCELL_CNT=src.cnt";

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.error("calculateStrongAssNcellCnt方法中，准备数据库statement出错！");
			e.printStackTrace();
			return false;
		}

		int affectCnt = 0;
		log.debug("计算包含室分小区在内的关联邻小区数:sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算包含室分小区在内的关联邻小区数更新数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("计算包含室分小区在内的关联邻小区数时出错！sql=" + sql);
			e.printStackTrace();
		}

		// -------不包含室分小区------------//
		// sql = "select NCELL,COUNT(*) AS CNT from " + ncsNcellTabName
		// + " where RNO_NCS_DESC_ID=" + ncsDescId
		// + " and INTERFER>0.05 and CELL_INDOOR='N' GROUP BY NCELL";

		// 2014-7-13 gmh修正错误
		sql = "select ncell,count(ncell) AS CNT from ( "
				+ " select ncell,cell,avg(INTERFER) from " + ncsNcellTabName
				+ " where RNO_NCS_DESC_ID= " + ncsDescId
				+ "  and CELL_INDOOR='N' " + " group by ncell,cell "
				+ " having avg(INTERFER)>0.05 " + ") " + " group by ncell";
		sql = "merge into "
				+ cellResTab
				+ " tar using ("
				+ sql
				+ ") src on ( tar.cell=src.ncell ) when matched then update set tar.INTERFER_NCE_CNT_EXINDR=src.cnt";
		log.debug("计算排除室分小区在外的关联邻小区数:sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算排除室分小区在外的关联邻小区数更新数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("计算排除室分小区在外的关联邻小区数时出错！sql=" + sql);
			e.printStackTrace();
		}

		return true;

	}

	/**
	 * 
	 * @author Liang YJ
	 * @date2014-2-12 下午6:11:57
	 * @param targetTable
	 *            被更新的表
	 * @param selectSql
	 *            数据源sql
	 * @param factor
	 *            被更新的字段
	 * @param groupBy
	 *            数查询的group by 字段
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @return String 表合成的sql
	 */
	private String getMergeSql(String targetTable, String selectSql,
			String factor, String groupBy, long ncsDescId) {
		return "merge into "
				+ targetTable
				+ " targ using ("
				+ selectSql
				+ ") temp on(targ.NCS_ID=temp.RNO_NCS_DESC_ID and targ.CELL=temp."
				+ groupBy + ") when matched then update set targ." + factor
				+ "=temp." + factor;
		// + " when not matched then insert (NCS_ID,CELL," + factor
		// + ") values(" + ncsDescId + ",temp." + groupBy + ",temp."
		// + factor + ")";
	}

	private String getMergeSql2(String targetTable, String selectSql,
			String factor, String targField, String tempField, String groupBy) {
		StringBuffer mergeSql = new StringBuffer();
		mergeSql.append("merge into ").append(targetTable)
				.append(" targ using (").append(selectSql);
		mergeSql.append(") temp on (targ.").append(targField)
				.append(" = temp.").append(tempField)
				.append(" and targ.CELL = temp.").append(groupBy);
		mergeSql.append(") when matched then update set targ.").append(factor)
				.append(" = temp.").append(factor);
		// mergeSql.append(" when not matched then insert (CELL,")
		// .append(targField).append(",").append(factor)
		// .append(") values(").append("temp.").append(groupBy)
		// .append(",temp.").append(tempField).append(",temp.")
		// .append(factor).append(")");
		return mergeSql.toString();
	}

	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-2-13 上午11:32:15
	 * @param ncsNcellTabName
	 *            数据源表
	 * @param groupBy
	 *            RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,
	 *            CELL_FREQ_CNT,NCELL_FREQ_CNT中的某个字段
	 * @return
	 */
	private String getCiAndCaSql(String ncsNcellTabName, String groupBy) {
		return "select RNO_NCS_DESC_ID,"
				+ groupBy
				+ ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(REPARFCN) CI,sum(CA_DIVIDER)/sum(REPARFCN) CA from "
				+ ncsNcellTabName
				+ " where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT  HAVING SUM(REPARFCN)>0";
	}

	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-2-13 上午11:32:15
	 * @param ncsNcellTabName
	 *            数据源表
	 * @param groupBy
	 *            RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,
	 *            CELL_FREQ_CNT,NCELL_FREQ_CNT中的某个字段
	 * @return
	 */
	private String getCiSql(String ncsNcellTabName, String groupBy) {
		return "select RNO_NCS_DESC_ID,"
				+ groupBy
				+ ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(REPARFCN) CI from "
				+ ncsNcellTabName
				+ " where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT HAVING SUM(REPARFCN) >0";
	}

	/**
	 * 计算网络结构指数或冗余覆盖指数
	 * 
	 * @author Liang YJ
	 * @date 2014-2-13 下午4:15:38
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param groupBy
	 *            RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,
	 *            CELL_FREQ_CNT,NCELL_FREQ_CNT中的某个字段
	 * @param field
	 *            参与统计的某个字段如CELL_FREQ_CNT或NCELL_FREQ_CNT
	 * @param factor
	 *            目标表被更新的字段
	 * @param gsm
	 *            表CELL中BCCH字段的值，如果BCCH<100,则gsm="BCCH<100",否则gsm="BCC>100";
	 * @param name
	 *            统计方法名
	 * @return flag 如果更新成功 flag = true,否则flag=false
	 */
	private boolean calculateNfOrRcf(Connection conn, long ncsDescId,
			String ncsNcellTabName, String cellResTab, String groupBy,
			String field, String factor, String gsm, String name,
			int wholeSecFreqCnt) {
		log.debug("conn:" + conn + ", ncsId:" + ncsDescId
				+ ", ncsNcellTabName:" + ncsNcellTabName);
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		// flag = calculateNfOrRcf(conn, ncsDescId, ncsNcellTabName,
		// cellResTab,"CELL",
		// "NCELL_FREQ_CNT","NET_STRUCT_FACTOR","BCCH>100","网络结构指数(gsm1800)");

		String statistics = "sum(rn.CI * rn." + field + "/?)";
		String innerSelectSql = getCiSql(ncsNcellTabName, groupBy);
		String selectSql1 = "select rn.RNO_NCS_DESC_ID RNO_NCS_DESC_ID, rn."
				+ groupBy + " " + groupBy + "," + statistics + " " + factor
				+ " from (" + innerSelectSql + ") rn inner join CELL c on rn."
				+ groupBy + " = c.LABEL where c.";
		String selectSql2 = " group by rn.RNO_NCS_DESC_ID,rn." + groupBy;
		boolean flag = false;
		PreparedStatement pstatement = null;
		// 更新网络结构指数gsm900和gsm1800
		try {
			String selectSql = selectSql1 + gsm + selectSql2;
			String mergeSql = getMergeSql(cellResTab, selectSql, factor,
					groupBy, ncsDescId);
			// System.out.println("mergeSql:" + mergeSql);
			log.debug(name + "mergeSql:" + mergeSql);
			pstatement = conn.prepareStatement(mergeSql);
			pstatement.setInt(1, wholeSecFreqCnt);
			pstatement.setLong(2, ncsDescId);
			pstatement.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			log.error("更新" + name + "出错");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != pstatement) {
				try {
					pstatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		return flag;
	}

	/**
	 * 计算被干扰系数
	 * @param conn
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @param groupBy
	 * @param factor
	 * @param name
	 * @param cellResTab
	 * @return
	 * @author brightming
	 * 2014-7-30 下午3:11:01
	 */
	private boolean calculateBeInterfered(Connection conn,
			long ncsDescId, String ncsNcellTabName,String groupBy,
			String factor, String name, String cellResTab){
		String statistics = "sum((CI*SAME_FREQ_CNT+CA*ADJ_FREQ_CNT)/(decode(CELL_FREQ_CNT,0,null,CELL_FREQ_CNT)))";
		boolean flag = false;
		Statement stmt = null;
		String mergeSql="";
		try {
			String innerSelectSql = "select RNO_NCS_DESC_ID,"
					+ groupBy
					+ ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(REPARFCN) CI,sum(CA_DIVIDER)/sum(REPARFCN) CA from "
					+ ncsNcellTabName
					+ " where RNO_NCS_DESC_ID = "+ncsDescId+" and REPARFCN>0 and distance<=15000 and interfer>0.02 and ca_interfer>0.02  group by RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT  HAVING SUM(REPARFCN)>0";
			String selectSql = "select RNO_NCS_DESC_ID," + groupBy + ","
					+ statistics + " " + factor + " from (" + innerSelectSql
					+ ") group by RNO_NCS_DESC_ID," + groupBy;
			mergeSql = "merge into "
					+ cellResTab
					+ " targ using ("
					+ selectSql
					+ ") temp on(targ.NCS_ID=temp.RNO_NCS_DESC_ID and targ.CELL=temp."
					+ groupBy + ") when matched then update set targ." + factor
					+ "=temp." + factor;
			log.debug("被干扰系数mergesql:" + mergeSql);
			stmt = conn.createStatement();
			stmt.executeUpdate(mergeSql);
			flag = true;
		} catch (SQLException e) {
			log.error("更新被干扰系数失败,sql="+mergeSql);
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭stmt失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}
	
	/**
	 * 计算干扰源系数
	 * @param conn
	 * @param ncsDescId
	 * @param ncsNcellTabName
	 * @param groupBy
	 * @param factor
	 * @param name
	 * @param cellResTab
	 * @return
	 * @author brightming
	 * 2014-7-30 下午3:12:40
	 */
	private boolean  calculateSrcInterfered(Connection conn,
			long ncsDescId, String ncsNcellTabName,String groupBy,
			String factor, String name, String cellResTab){
		String statistics = "sum((CI*SAME_FREQ_CNT+CA*ADJ_FREQ_CNT)/(decode(NCELL_FREQ_CNT,0,null,NCELL_FREQ_CNT)))";
		boolean flag = false;
		Statement stmt = null;
		String mergeSql="";
		try {
			String innerSelectSql = "select RNO_NCS_DESC_ID,"
					+ groupBy
					+ ",SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT,sum(CI_DIVIDER)/sum(REPARFCN) CI,sum(CA_DIVIDER)/sum(REPARFCN) CA from "
					+ ncsNcellTabName
					+ " where RNO_NCS_DESC_ID = "+ncsDescId+"  and REPARFCN>0  and distance<=15000 and interfer>0.02 and ca_interfer>0.02  group by RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_FREQ_CNT,NCELL_FREQ_CNT  HAVING SUM(REPARFCN)>0";
			String selectSql = "select RNO_NCS_DESC_ID," + groupBy + ","
					+ statistics + " " + factor + " from (" + innerSelectSql
					+ ") group by RNO_NCS_DESC_ID," + groupBy;
			mergeSql = "merge into "
					+ cellResTab
					+ " targ using ("
					+ selectSql
					+ ") temp on(targ.NCS_ID=temp.RNO_NCS_DESC_ID and targ.CELL=temp."
					+ groupBy + ") when matched then update set targ." + factor
					+ "=temp." + factor;
			log.debug("干扰源系数mergesql:" + mergeSql);
			stmt = conn.createStatement();
			stmt.executeUpdate(mergeSql);
			flag = true;
		} catch (SQLException e) {
			log.error("更新干扰源系数失败,sql="+mergeSql);
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭stmt失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}
	
	
	/**
	 * 计算网络结构指数或冗余覆盖指数
	 * 
	 * @author Liang YJ
	 * @date 2014-2-13 下午4:15:38
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param groupBy
	 *            RNO_NCS_DESC_ID,CELL,NCELL,SAME_FREQ_CNT,ADJ_FREQ_CNT,
	 *            CELL_FREQ_CNT,NCELL_FREQ_CNT中的某个字段
	 * @param field
	 *            参与统计的某个字段如CELL_FREQ_CNT或NCELL_FREQ_CNT
	 * @param factor
	 *            目标表被更新的字段
	 * @param name
	 *            统计方法名
	 * @return flag 如果更新成功 flag = true,否则flag=false
	 */
    @Deprecated
	private boolean calculateBeInterferOrSrcInterfer(Connection conn,
			long ncsDescId, String ncsNcellTabName, String groupBy,
			String factor, String name, String cellResTab) {
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		String statistics = "sum((CI*SAME_FREQ_CNT+CA*ADJ_FREQ_CNT)/(decode(CELL_FREQ_CNT*NCELL_FREQ_CNT,0,null,CELL_FREQ_CNT*NCELL_FREQ_CNT)))";
		boolean flag = false;
		PreparedStatement pStatement = null;
		try {
			String innerSelectSql = getCiAndCaSql(ncsNcellTabName, groupBy);
			String selectSql = "select RNO_NCS_DESC_ID," + groupBy + ","
					+ statistics + " " + factor + " from (" + innerSelectSql
					+ ") group by RNO_NCS_DESC_ID," + groupBy;
			String mergeSql = getMergeSql(cellResTab, selectSql, factor,
					groupBy, ncsDescId);
			log.debug("被干扰系数mergesql:" + mergeSql);
			// System.out.println("mergeSql1:" + mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			pStatement.setLong(1, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			log.error("更新被干扰系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}
		return flag;
	}

	/**
	 * 小区覆盖分量
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param clusterTab
	 *            连通簇表
	 * @param clusterCellTab
	 *            连通簇小区表
	 * @param cellResTab
	 *            小区结果表
	 * @return
	 * @author Liang YJ 2014-2-14 17:49:25
	 */
	public boolean calculateCellCover(Connection conn, long ncsDescId,
			String ncsNcellTabName, String clusterTab, String clusterCellTab,
			String cellResTab) {
		log.debug("conn:" + conn + ", ncsId:" + ncsDescId
				+ ", ncsNcellTabName:" + ncsNcellTabName);
		/*
		 * select t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3.NCELL,
		 * max(t3.CELL_COVER_FACTOR) COVER_FACTOR from (select
		 * rn.RNO_NCS_DESC_ID,rncc.CLUSTER_ID, rncc.CELL, rncc.NCELL, case when
		 * rn.DISTANCE/(1.6*rncar.EXPECTED_COVER_DIS)>= 1 then
		 * (rn.DISTANCE*rn.CI)/(1.6*rncar.EXPECTED_COVER_DIS) else 0 end
		 * CELL_COVER_FACTOR from (select
		 * RNO_NCS_DESC_ID,CELL,NCELL,sum(CI_DIVIDER)/sum(REPARFCN) CI,DISTANCE
		 * from RNO_NCS where RNO_NCS_DESC_ID = 362 group by
		 * RNO_NCS_DESC_ID,CELL,NCELL,DISTANCE) rn inner join (select
		 * ID,RNO_NCS_DESC_ID from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID = 362 )
		 * rnc on (rn.RNO_NCS_DESC_ID = rnc.RNO_NCS_DESC_ID) inner join (select
		 * t1.CLUSTER_ID CLUSTER_ID,t1.CELL CELL,t2.CELL NCELL from
		 * RNO_NCS_CLUSTER_CELL t1 inner join RNO_NCS_CLUSTER_CELL t2 on
		 * t1.CLUSTER_ID = t2.CLUSTER_ID where t1.CELL != t2.CELL order by
		 * t1.CLUSTER_ID) rncc on(rnc.ID = rncc.CLUSTER_ID and rn.NCELL =
		 * rncc.NCELL and rn.CELL=rncc.CELL) inner join ( select
		 * NCS_ID,CELL,EXPECTED_COVER_DIS from RNO_NCS_CELL_ANA_RESULT where
		 * NCS_ID=362) rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and
		 * rncc.NCELL = rncar.CELL)) t3 group by
		 * t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3.NCELL order by
		 * t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID
		 */
		// String clusterCellTab = "RNO_NCS_CLUSTER_CELL";
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		PreparedStatement pStatement = null;
		boolean flag = false;
		StringBuffer selectSql = new StringBuffer();
		try {
			String factor = "COVER_FACTOR";
			String groupBy = "NCELL";
			// 查询小区在簇内的覆盖系数
			// String cfSelectSql =
			// "select t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3."+groupBy+", max(t3.CELL_COVER_FACTOR) "+factor+" from (select rn.RNO_NCS_DESC_ID,rncc.CLUSTER_ID, rncc.CELL, rncc.NCELL, case when rn.DISTANCE/(1.6*rncar.EXPECTED_COVER_DIS)>= 1 then (rn.DISTANCE*rn.CI)/(1.6*rncar.EXPECTED_COVER_DIS) else 0 end CELL_COVER_FACTOR from (select RNO_NCS_DESC_ID,CELL,NCELL,sum(CI_DIVIDER)/sum(REPARFCN) CI,DISTANCE from RNO_NCS where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,DISTANCE) rn inner join (select ID,RNO_NCS_DESC_ID from RNO_NCS_CLUSTER where RNO_NCS_DESC_ID = ? ) rnc on (rn.RNO_NCS_DESC_ID = rnc.RNO_NCS_DESC_ID) inner join (select t1.CLUSTER_ID CLUSTER_ID,t1.CELL CELL,t2.CELL NCELL from RNO_NCS_CLUSTER_CELL t1 inner join RNO_NCS_CLUSTER_CELL t2 on t1.CLUSTER_ID = t2.CLUSTER_ID where t1.CELL != t2.CELL order by t1.CLUSTER_ID) rncc on(rnc.ID = rncc.CLUSTER_ID and rn.NCELL = rncc.NCELL and rn.CELL=rncc.CELL) inner join ( select NCS_ID,CELL,EXPECTED_COVER_DIS from RNO_NCS_CELL_ANA_RESULT where NCS_ID=?) rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and rncc.NCELL = rncar.CELL)) t3 group by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3."+groupBy+" order by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID";
			selectSql.append("select t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3.");
			selectSql.append(groupBy);
			selectSql.append(", max(t3.CELL_COVER_FACTOR) ");
			selectSql.append(factor);
			selectSql
					.append(" from (select rn.RNO_NCS_DESC_ID,rncc.CLUSTER_ID, rncc.CELL, rncc.NCELL, (case when EXPECTED_COVER_DIS<>0 then (case when rn.DISTANCE/(1.6*rncar.EXPECTED_COVER_DIS)>= 1 then (rn.DISTANCE*rn.CI)/(1.6*rncar.EXPECTED_COVER_DIS) else 0 end) else null end) CELL_COVER_FACTOR from (  select RNO_NCS_DESC_ID,CELL,NCELL,(case when reparfcn=0 then null else cd/REPARFCN end)as CI,DISTANCE FROM ( select RNO_NCS_DESC_ID,CELL,NCELL,sum(CI_DIVIDER) as cd,sum(REPARFCN) as reparfcn,DISTANCE from "
							+ ncsNcellTabName
							+ " where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID,CELL,NCELL,DISTANCE)) rn inner join (select ID,RNO_NCS_DESC_ID from "
							+ clusterTab
							+ " where RNO_NCS_DESC_ID = ? ) rnc on (rn.RNO_NCS_DESC_ID = rnc.RNO_NCS_DESC_ID) inner join (select t1.CLUSTER_ID CLUSTER_ID,t1.CELL CELL,t2.CELL NCELL from "
							+ clusterCellTab
							+ " t1 inner join "
							+ clusterCellTab
							+ " t2 on t1.CLUSTER_ID = t2.CLUSTER_ID where t1.CELL != t2.CELL order by t1.CLUSTER_ID) rncc on(rnc.ID = rncc.CLUSTER_ID and rn.NCELL = rncc.NCELL and rn.CELL=rncc.CELL) inner join ( select NCS_ID,CELL,EXPECTED_COVER_DIS from "
							+ cellResTab
							+ " where NCS_ID=?) rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and rncc.NCELL = rncar.CELL)) t3 group by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID,t3.");
			selectSql.append(groupBy);
			selectSql.append(" order by t3.RNO_NCS_DESC_ID,t3.CLUSTER_ID");
			// String cfMergeSql =
			// "merge into RNO_NCS_CLUSTER_CELL targ using ("+cfSelectSql+") temp on (targ.CLUSTER_ID = temp.CLUSTER_ID and targ.CELL = temp."+groupBy+") when matched then update set targ."+factor+" = temp."+factor+" when not matched then insert (CLUSTER_ID,CELL,"+factor+") values(temp.CLUSTER_ID,temp."+groupBy+",temp."+factor+")";
			// 更新小区在簇内的覆盖系数
			/*
			 * mergeSql.append("merge into RNO_NCS_CLUSTER_CELL targ using (");
			 * mergeSql.append(selectSql.toString()); mergeSql.append(
			 * ") temp on (targ.CLUSTER_ID = temp.CLUSTER_ID and targ.CELL = temp."
			 * ); mergeSql.append(groupBy);
			 * mergeSql.append(") when matched then update set targ.");
			 * mergeSql.append(factor); mergeSql.append(" = temp.");
			 * mergeSql.append(factor);
			 * mergeSql.append(" when not matched then insert (CLUSTER_ID,CELL,"
			 * ); mergeSql.append(factor);
			 * mergeSql.append(") values(temp.CLUSTER_ID,temp.");
			 * mergeSql.append(groupBy); mergeSql.append(",temp.");
			 * mergeSql.append(factor); mergeSql.append(")");
			 */
			String mergeSql = getMergeSql2(clusterCellTab,
					selectSql.toString(), factor, "CLUSTER_ID", "CLUSTER_ID",
					groupBy);
			// System.out.println(mergeSql);
			log.debug("更新小区在簇内的覆盖系数的sql=" + mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			pStatement.setLong(1, ncsDescId);
			pStatement.setLong(2, ncsDescId);
			pStatement.setLong(3, ncsDescId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			log.error("更新小区在簇内的覆盖系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);

		try {
			// 查询小区的簇覆盖破坏系数
			// select t1.CLUSTER_ID,t1.CELL,case t2.SUM_COVER_FACTOR when 0 then
			// 0 else t1.COVER_FACTOR/t2.SUM_COVER_FACTOR end COVER_DESTROY from
			// (select CLUSTER_ID,CELL,COVER_FACTOR from RNO_NCS_CLUSTER_CELL
			// where CLUSTER_ID in (select ID from RNO_NCS_CLUSTER where
			// RNO_NCS_DESC_ID = 362)) t1 inner join (select
			// CLUSTER_ID,sum(COVER_FACTOR) SUM_COVER_FACTOR from (select
			// CLUSTER_ID,CELL,COVER_FACTOR from RNO_NCS_CLUSTER_CELL where
			// CLUSTER_ID in (select ID from RNO_NCS_CLUSTER where
			// RNO_NCS_DESC_ID = 362)) group by CLUSTER_ID) t2 on t1.CLUSTER_ID
			// = t2.CLUSTER_ID
			String factor = "COVER_DESTROY";
			String groupBy = "CLUSTER_ID";
			selectSql.append("select t1.");
			selectSql.append(groupBy);
			selectSql
					.append(",t1.CELL,case t2.SUM_COVER_FACTOR when 0 then 0 else t1.COVER_FACTOR/t2.SUM_COVER_FACTOR end ");
			selectSql.append(factor);
			selectSql.append(" from (");
			String innerSqlT1 = "select " + groupBy
					+ ",CELL,COVER_FACTOR from " + clusterCellTab + " where "
					+ groupBy + " in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSqlT1);
			selectSql.append(") t1 inner join (");
			String innerSqlT2 = "select " + groupBy
					+ ",sum(COVER_FACTOR) SUM_COVER_FACTOR from (select "
					+ groupBy + ",CELL,COVER_FACTOR from " + clusterCellTab
					+ " where " + groupBy + " in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSqlT2);
			selectSql.append(") group by " + groupBy + ") t2 on t1." + groupBy
					+ " = t2." + groupBy);
			// 更新小区的簇覆盖破坏系数
			// merge into RNO_NCS_CLUSTER_CELL targ using () temp on
			// (targ.CLUSTER_ID = temp.CLUSTER_ID and targ.CELL=temp.CELL) when
			// matched then update set targ.COVER_DESTROY = temp.COVER_DESTROY
			// when not matched then insert (CLUSTER_ID,CELL,COVER_DESTROY)
			// values(temp.CLUSTER_ID,temp.CELL,temp.COVER_DESTROY);
			// merge into RNO_NCS_CLUSTER_CELL targ using () temp on
			// (targ.CLUSTER_ID = temp.CLUSTER_ID and targ.CELL=temp.CELL) when
			// matched then update set targ.COVER_DESTROY = temp.COVER_DESTROY
			// when not matched then insert (CLUSTER_ID,CELL,COVER_DESTROY)
			// values(temp.CLUSTER_ID,temp.CELL,temp.COVER_DESTROY);
			String mergeSql = getMergeSql2(clusterCellTab,
					selectSql.toString(), factor, "CLUSTER_ID", "CLUSTER_ID",
					"CELL");
			log.debug("更新小区的簇覆盖破坏系数的sql=" + mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			pStatement.setLong(1, ncsDescId);
			pStatement.setLong(2, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的簇覆盖破坏系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		try {
			String groupBy = "CELL";
			String factor = "CELL_COVER";
			String statistics = "case sum(t2.WEIGHT) when 0 then 0 else sum(t1.COVER_DESTROY*t2.WEIGHT)/sum(t2.WEIGHT) end";
			// select t2.RNO_NCS_DESC_ID, t1.CELL,case sum(t2.WEIGHT) when 0
			// then 0 else sum(t1.COVER_DESTROY*t2.WEIGHT)/sum(t2.WEIGHT) end
			// CELL_COVER from (select CLUSTER_ID, CELL, COVER_DESTROY from
			// RNO_NCS_CLUSTER_CELL where CLUSTER_ID in (select ID from
			// RNO_NCS_CLUSTER where RNO_NCS_DESC_ID = 362)) t1 inner join
			// (select ID,RNO_NCS_DESC_ID,WEIGHT from RNO_NCS_CLUSTER where
			// RNO_NCS_DESC_ID = 362) t2 on (t1.CLUSTER_ID = t2.ID) group by
			// t2.RNO_NCS_DESC_ID, t1.CELL

			selectSql.append("select t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy + ",");
			selectSql.append(statistics);
			selectSql.append(" " + factor);
			selectSql.append(" from (");
			String innerSql1 = "select CLUSTER_ID, " + groupBy
					+ ", COVER_DESTROY from " + clusterCellTab
					+ " where CLUSTER_ID in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = ?)";
			selectSql.append(innerSql1);
			selectSql.append(") t1 inner join (");
			String innerSql2 = "select ID,RNO_NCS_DESC_ID,WEIGHT from "
					+ clusterTab + " where RNO_NCS_DESC_ID = ?";
			selectSql.append(innerSql2);
			selectSql
					.append(") t2 on (t1.CLUSTER_ID = t2.ID)  group by t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy);
			String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
					factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);
			// System.out.println(mergeSql);
			pStatement = conn.prepareStatement(mergeSql);
			pStatement.setLong(1, ncsDescId);
			pStatement.setLong(2, ncsDescId);
			pStatement.executeUpdate();
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的簇覆盖破坏系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		return flag;
	}

	/**
	 * 小区容量破坏分量计算
	 * 
	 * @param conn
	 *            数据库连接
	 * @param ncsDescId
	 *            RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 *            ncs 测量得到的邻区的数据表名称
	 * @param clusterTab
	 *            连通簇表
	 * @param clusterCellTab
	 *            连通簇小区表
	 * @param cellResTab
	 *            小区结果表
	 * @return
	 * @author Liang YJ 2014-2-15 16:49:25
	 */
	public boolean calculateCapacityDestroy(Connection conn, long ncsDescId,
			String ncsNcellTabName, String clusterTab, String clusterCellTab,
			String cellResTab) {
		// String cellResTab = "RNO_NCS_CELL_ANA_RESULT";
		// String clusterCellTab = "RNO_NCS_CLUSTER_CELL";
		StringBuffer selectSql = new StringBuffer();
		boolean flag = false;
		Statement pStatement = null;
		// 更新小区理想容量
		try {
			String groupBy = "CELL";
			String statistics = "case when c.BCCH<100 then 7 when c.BCCH>100 then 9 else null end ";
			String factor = "EXPECTED_CAPACITY";
			// String selectSqlString =
			// "select rn.RNO_NCS_DESC_ID, rn."+groupBy+", case when c.BCCH<100 then 7 when c.BCCH>100 then 9 else null end "+factor+" from (select RNO_NCS_DESC_ID, "+groupBy+" from RNO_NCS where RNO_NCS_DESC_ID = ? group by RNO_NCS_DESC_ID, "+groupBy+") rn inner join CELL c on (rn."+groupBy+" = c.LABEL)";
			String innerSql = "select RNO_NCS_DESC_ID, " + groupBy + " from "
					+ ncsNcellTabName + " where RNO_NCS_DESC_ID = " + ncsDescId
					+ " group by RNO_NCS_DESC_ID, " + groupBy;
			selectSql.append("select rn.RNO_NCS_DESC_ID, rn.")
					.append(groupBy + ",").append(statistics + factor);
			selectSql
					.append(" from(")
					.append(innerSql)
					.append(") rn inner join "
							+ "( "
							+ " select * from ( "
							+ " select label,bcch,row_number() over(partition by label order by bcch) as seq from cell "
							+ " ) where seq=1  )  c on (rn.").append(groupBy)
					.append(" = c.LABEL)");
			String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
					factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);
			log.debug("更新小区理想容量sql: " + mergeSql);
			pStatement = conn.createStatement();
			// pStatement.setLong(1, ncsDescId);
			// pStatement.executeUpdate();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区的理想容量失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			// 将using的源表数据查询出来看看结果
			String tmpSql = "select rn.RNO_NCS_DESC_ID, rn.CELL,case when c.BCCH<100 then 7 when c.BCCH>100 then 9 else null end EXPECTED_CAPACITY from(select RNO_NCS_DESC_ID, CELL from RNO_NCS_MID where RNO_NCS_DESC_ID = "
					+ ncsDescId
					+ " group by RNO_NCS_DESC_ID, CELL) rn inner join (  select * from (  select label,bcch,row_number() over(partition by label order by bcch) as seq from cell  ) where seq=1  )  c on (rn.CELL = c.LABEL)";
			List<Map<String, Object>> tmpRes = RnoHelper.commonQuery(
					pStatement, tmpSql);
			log.error("更新小区的理想容量失败时，源表的数据为：\n" + tmpRes);
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区容量系数
		try {
			// select rn.RNO_NCS_DESC_ID, rn.CELL, case when rn.CELL_FREQ_CNT <
			// rncar.EXPECTED_CAPACITY then 0 else
			// rn.CELL_FREQ_CNT/rncar.EXPECTED_CAPACITY end
			// CAPACITY_DESTROY_FACT from (select
			// RNO_NCS_DESC_ID,CELL,CELL_FREQ_CNT from RNO_NCS where
			// RNO_NCS_DESC_ID = 381 group by
			// RNO_NCS_DESC_ID,CELL,CELL_FREQ_CNT) rn inner join (select
			// NCS_ID,CELL,EXPECTED_CAPACITY from RNO_NCS_CELL_ANA_RESULT where
			// NCS_ID = 381) rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and
			// rn.CELL = rncar.CELL)

			// String groupBy = "CELL";
			// String factor = "CAPACITY_DESTROY_FACT";
			// String statistics =
			// "case when rn.CELL_FREQ_CNT < rncar.EXPECTED_CAPACITY then 0 else rn.CELL_FREQ_CNT/rncar.EXPECTED_CAPACITY end";
			// String innerSql1 = "select RNO_NCS_DESC_ID," + groupBy
			// + ",CELL_FREQ_CNT from " + ncsNcellTabName
			// + " where RNO_NCS_DESC_ID = " + ncsDescId
			// + " group by RNO_NCS_DESC_ID," + groupBy + ",CELL_FREQ_CNT";
			// String innerSql2 = "select NCS_ID," + groupBy
			// + ",EXPECTED_CAPACITY from " + cellResTab
			// + " where NCS_ID =" + ncsDescId + " ";
			// selectSql.append("select rn.RNO_NCS_DESC_ID, rn.").append(groupBy)
			// .append(",").append(statistics).append(" ").append(factor);
			// selectSql
			// .append(" from (")
			// .append(innerSql1)
			// .append(") rn inner join (")
			// .append(innerSql2)
			// .append(") rncar on (rn.RNO_NCS_DESC_ID = rncar.NCS_ID and rn.")
			// .append(groupBy).append("= rncar.").append(groupBy)
			// .append(")");
			// String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
			// factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);

			String mergeSql = "UPDATE RNO_NCS_CELL_ANA_RESULT_MID SET CAPACITY_DESTROY_FACT=(CASE WHEN FREQ_CNT<EXPECTED_CAPACITY THEN 0 ELSE FREQ_CNT/EXPECTED_CAPACITY END)";
			log.debug("更新小区容量破坏系数mergeSql: " + mergeSql);
			pStatement = conn.createStatement();// conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.executeUpdate();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区容量系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区的簇容量破坏系数
		try {
			// String factor = "CAPACITY_DESTROY";
			// String statistics =
			// "case t2.SUM_CAPACITY_DESTROY_FACT when 0 then 0 else CAPACITY_DESTROY_FACT/t2.SUM_CAPACITY_DESTROY_FACT end";
			// String groupBy = "CELL";
			// selectSql
			// .append("select rncc.CLUSTER_ID,rncar.")
			// .append(groupBy)
			// .append(",rncar.CAPACITY_DESTROY_FACT from (select ID from "
			// + clusterTab
			// + " where RNO_NCS_DESC_ID = "
			// + ncsDescId
			// + " ) rnc inner join (select CLUSTER_ID,")
			// .append(groupBy)
			// .append(" from "
			// + clusterCellTab
			// + ") rncc on(rnc.ID = rncc.CLUSTER_ID) inner join (select ")
			// .append(groupBy)
			// .append(",CAPACITY_DESTROY_FACT from " + cellResTab
			// + " where NCS_ID = " + ncsDescId
			// + ") rncar on (rncc.").append(groupBy)
			// .append(" = rncar.").append(groupBy).append(")");
			// String innerSql1 = selectSql.toString();
			// selectSql.setLength(0);
			// selectSql
			// .append("select CLUSTER_ID,sum(CAPACITY_DESTROY_FACT) SUM_CAPACITY_DESTROY_FACT from (select rncc.CLUSTER_ID,rncar.CAPACITY_DESTROY_FACT from (select ID from "
			// + clusterTab
			// + " where RNO_NCS_DESC_ID = "
			// + ncsDescId
			// + ") rnc inner join (select CLUSTER_ID,")
			// .append(groupBy)
			// .append(" from "
			// + clusterCellTab
			// + ") rncc on(rnc.ID = rncc.CLUSTER_ID) inner join (select ")
			// .append(groupBy)
			// .append(",CAPACITY_DESTROY_FACT from " + cellResTab
			// + " where NCS_ID = " + ncsDescId
			// + ") rncar on (rncc.").append(groupBy)
			// .append(" = rncar.").append(groupBy)
			// .append(")) group by CLUSTER_ID");
			// String innerSql2 = selectSql.toString();
			// selectSql.setLength(0);
			// selectSql.append("select t1.CLUSTER_ID, t1.").append(groupBy)
			// .append(",").append(statistics).append(" ").append(factor)
			// .append(" from (").append(innerSql1)
			// .append(") t1 left join (").append(innerSql2)
			// .append(") t2 on (t1.CLUSTER_ID = t2.CLUSTER_ID)");
			// String mergeSql = getMergeSql2(clusterCellTab,
			// selectSql.toString(), factor, "CLUSTER_ID", "CLUSTER_ID",
			// groupBy);

			selectSql
					.append("merge INTO RNO_NCS_CLUSTER_CELL_MID targ USING ")
					.append("(SELECT dat1.cluster_id,")
					.append("dat1.cell,")
					.append("dat1.CAPACITY_DESTROY_FACT/(decode(dat2.WHOLE_CAPACITY_DESTROY_FACT,0,null,dat2.WHOLE_CAPACITY_DESTROY_FACT)) AS CAPACITY_DESTROY ")
					.append("FROM ")
					.append("(SELECT clscell2.cluster_id,")
					.append("clscell2.cell,")
					.append("cellres2.CAPACITY_DESTROY_FACT ")
					.append("FROM RNO_NCS_CLUSTER_CELL_MID clscell2 ")
					.append("INNER JOIN RNO_NCS_CELL_ANA_RESULT_MID cellres2 ")
					.append("ON(clscell2.cell=cellres2.cell) ")
					.append(")dat1 ")
					.append("INNER JOIN ")
					.append("(SELECT cluster_id, ")
					.append("SUM(CAPACITY_DESTROY_FACT) AS WHOLE_CAPACITY_DESTROY_FACT ")
					.append("FROM ")
					.append("(SELECT clscell.CLUSTER_ID,")
					.append("clscell.cell,")
					.append("cellres1.CAPACITY_DESTROY_FACT ")
					.append("FROM RNO_NCS_CLUSTER_CELL_MID clscell ")
					.append("INNER JOIN RNO_NCS_CELL_ANA_RESULT_MID cellres1 ")
					.append("ON(clscell.cell=cellres1.cell) ")
					.append(") ")
					.append("GROUP BY cluster_id ")
					.append(")dat2 ON(dat1.cluster_id=dat2.cluster_id) ")
					.append(")src ON(targ.cluster_id   =src.cluster_id AND targ.cell=src.cell) ")
					.append("WHEN matched THEN ")
					.append("UPDATE SET targ.CAPACITY_DESTROY=src.CAPACITY_DESTROY");
			String mergeSql = selectSql.toString();
			log.debug("更新小区的簇容量破坏系数mergeSql: " + mergeSql);
			// pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.setLong(3, ncsDescId);
			// pStatement.setLong(4, ncsDescId);
			// pStatement.executeUpdate();
			pStatement = conn.createStatement();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("小区的簇容量破坏系数失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		selectSql.setLength(0);
		// 更新小区容量破坏分量
		try {
			String groupBy = "CELL";
			String factor = "CAPACITY_DESTROY";
			String statistics = "case sum(t2.WEIGHT) when 0 then 0 else sum(t1.CAPACITY_DESTROY*t2.WEIGHT)/sum(t2.WEIGHT) end";

			selectSql.append("select t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy + ",");
			selectSql.append(statistics);
			selectSql.append(" " + factor);
			selectSql.append(" from (");
			String innerSql1 = "select CLUSTER_ID, " + groupBy
					+ ", CAPACITY_DESTROY from " + clusterCellTab
					+ " where CLUSTER_ID in (select ID from " + clusterTab
					+ " where RNO_NCS_DESC_ID = " + ncsDescId + " )";
			selectSql.append(innerSql1);
			selectSql.append(") t1 inner join (");
			String innerSql2 = "select ID,RNO_NCS_DESC_ID,WEIGHT from "
					+ clusterTab + " where RNO_NCS_DESC_ID =  " + ncsDescId;
			selectSql.append(innerSql2);
			selectSql
					.append(") t2 on (t1.CLUSTER_ID = t2.ID)  group by t2.RNO_NCS_DESC_ID, t1.");
			selectSql.append(groupBy);
			String mergeSql = getMergeSql2(cellResTab, selectSql.toString(),
					factor, "NCS_ID", "RNO_NCS_DESC_ID", groupBy);
			log.debug("更新小区容量破坏分量mergeSql: " + mergeSql);
			// pStatement = conn.prepareStatement(mergeSql);
			// pStatement.setLong(1, ncsDescId);
			// pStatement.setLong(2, ncsDescId);
			// pStatement.executeUpdate();
			pStatement = conn.createStatement();
			pStatement.executeUpdate(mergeSql);
			flag = true;
		} catch (Exception e) {
			log.error("更新小区容量破坏分量失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("关闭pstatement失败");
					log.error(e.getStackTrace());
				}
			}
		}

		return flag;
	}

	/**
	 * 根据ncsDescId查询相关记录
	 * 
	 * @author Liang YJ
	 * @date 2014-2-18 下午5:46:37
	 * @param ncsId
	 *            数据源表中的NCS_ID字段
	 * @return
	 */
	public List<RnoNcsCell> getNcsCellByNcsDescId(long ncsDescId) {
		String sql = "select rncar.NCS_ID as ncsId, c.LABEL as cell, c.NAME as chineseName, c.LONGITUDE as lng, c.LATITUDE as lat, c.LNGLATS as allLngLats, rncar.FREQ_CNT as freqCnt, rncar.BE_INTERFER as beInterfer, rncar.NET_STRUCT_FACTOR as netStructFactor,rncar.SRC_INTERFER as srcInterfer, rncar.REDUNT_COVER_FACT as reduntCoverFact, rncar.OVERLAP_COVER as overlapCover, rncar.EXPECTED_COVER_DIS as expectedCoverDis, rncar.OVERSHOOTING_FACT as overshootingFact, rncar.DETECT_CNT as detectCnt, rncar.CELL_COVER as cellCover, rncar.EXPECTED_CAPACITY as expectedCapacity, rncar.CAPACITY_DESTROY_FACT as capacityDestroyFact,rncar.CAPACITY_DESTROY as capacityDestroy from RNO_NCS_CELL_ANA_RESULT rncar inner join CELL c on c.label = rncar.CELL where rncar.NCS_ID = ?";
		Connection conn = DataSourceConn.initInstance().getConnection();
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		List<RnoNcsCell> ncsCellList = new ArrayList<RnoNcsCell>();
		try {
			pStatement = conn.prepareStatement(sql);
			pStatement.setLong(1, ncsDescId);
			resultSet = pStatement.executeQuery();
			while (resultSet.next()) {
				RnoNcsCell ncsCell = new RnoNcsCell(resultSet.getLong("ncsId"),
						resultSet.getString("cell"),
						resultSet.getString("chineseName"),
						resultSet.getDouble("lng"), resultSet.getDouble("lat"),
						resultSet.getString("allLngLats"),
						resultSet.getInt("freqCnt"),
						resultSet.getDouble("beInterfer"),
						resultSet.getDouble("srcInterfer"),
						resultSet.getDouble("netStructFactor"),
						resultSet.getDouble("reduntCoverFact"),
						resultSet.getDouble("overlapCover"),
						resultSet.getDouble("expectedCoverDis"),
						resultSet.getDouble("overshootingFact"),
						resultSet.getDouble("detectCnt"),
						resultSet.getDouble("cellCover"),
						resultSet.getDouble("expectedCapacity"),
						resultSet.getDouble("capacityDestroyFact"),
						resultSet.getDouble("capacityDestroy"));
				ncsCellList.add(ncsCell);
			}
		} catch (SQLException e) {
			log.error("getNcsCellByNcsDescId查询失败");
			log.error(e.getStackTrace());
			e.printStackTrace();
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error("getNcsCellByNcsDescId关闭ResultSet失败");
					log.error(e.getStackTrace());
				}
			}
			if (null != pStatement) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					log.error("getNcsCellByNcsDescId关闭PreparedStatement失败");
					log.error(e.getStackTrace());
				}
			}
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("getNcsCellByNcsDescId关闭Connection失败");
					log.error(e.getStackTrace());
				}
			}
		}
		log.debug("getNcsCellByNcsId结果,共：" + ncsCellList.size() + "条记录");
		return ncsCellList;
	}

	/**
	 * 自动匹配ncs文件对应的bsc、频段
	 * 
	 * @param connection
	 * @param ncsTabName
	 *            ncs邻区测量数据源名称
	 * @param ncsDescTabName
	 *            ncs描述表名称，将被更新
	 * @param ncsIds
	 * @author brightming 2014-2-27 下午4:33:01
	 */
	public boolean matchNcsBscAndFreqSection(Connection connection,
			String ncsTabName, String ncsDescTabName, List<Long> ncsIds) {

		log.debug("进入方法：matchNcsBscAndFreqSection.connection=" + connection
				+ ",ncsTabName=" + ncsTabName + ",ncsDescTabName="
				+ ncsDescTabName + ",ncsIds=" + ncsIds);
		if (connection == null) {
			log.error("matchNcsBscAndFreqSection的参数中未提供有效的数据库连接！");
			return false;
		}
		if (ncsIds == null || ncsIds.isEmpty()) {
			log.error("未指明ncs范围，不执行匹配。");
			return true;
		}
		String ncsIdStr = "";
		for (long id : ncsIds) {
			ncsIdStr += id + ",";
		}
		ncsIdStr = ncsIdStr.substring(0, ncsIdStr.length() - 1);
		String sql = " SELECT rno_ncs_desc_id,freq_SECTION,BSC from  "
				+ " ( "
				+ " select rno_ncs_desc_id,freq_SECTION,BSC,rank() over (partition by rno_ncs_desc_id order by cnt desc ) as seq  FROM "
				+ " ( "
				+ " select rno_ncs_desc_id,freq_SECTION,BSC,count(*) as cnt from "
				+ " ( "
				+ " select mid2.rno_ncs_desc_id,mid2.freq_SECTION ,RNO_BSC.ENGNAME AS BSC "
				+ " from "
				+ " ( "
				+ " select mid.rno_ncs_desc_id,mid.s_cell,(case when CELL.bcch>100 then 'GSM1800' ELSE 'GSM900' END) AS FREQ_SECTION,CELL.bsc_id from "
				+ " ( " + " select rno_ncs_desc_id,cell as s_cell from "
				+ ncsTabName + " where rno_ncs_desc_id in ( " + ncsIdStr
				+ " ) GROUP BY rno_ncs_desc_id,cell " + " )mid "
				+ " inner join cell  " + " on (mid.s_cell=cell.label) "
				+ " )mid2 " + " inner join rno_bsc  "
				+ " on (mid2.bsc_id=RNO_BSC.bsc_id) " + " ) "
				+ " GROUP BY rno_ncs_desc_id,freq_SECTION,BSC "
				+ " order by rno_ncs_desc_id,cnt desc nulls last " + " ) "
				+ " ) " + " where seq=1 ";
		sql = "merge into "
				+ ncsDescTabName
				+ " tar using ("
				+ sql
				+ ") src on(tar.rno_ncs_desc_id=src.rno_ncs_desc_id) when matched then update set tar.FREQ_SECTION=src.FREQ_SECTION,tar.BSC=src.BSC";
		log.debug("更新ncs的bsc、频段信息的sql=" + sql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return true;
	}

	// /**
	// *
	// * @param conn
	// * @param clusterTabName
	// * @return
	// * @author brightming
	// * 2014-3-21 下午12:01:04
	// */
	// public List<Map<String,Object>> queryAreaDamageFactor(Connection
	// conn,String clusterTabName,List<Long>){
	//
	// }

	/**
	 * 将指定的NCS数据，从RNO_NCS表转移到RNO_NCS表
	 * 
	 * 2014-7-11 gmh： 转移的过程中，为了能够支持用户自定义的同频、邻频干扰门限，需要转移一个ncs，就计算一次同频、邻频的分子
	 * 而在进行ncs解析的时候，就不需要做这件事了。
	 * 
	 * @param stmt
	 * @param ncsIds
	 * @param saveNcsId
	 *            转移过临时表后，统一的ncsid
	 * @return
	 * @author brightming 2014-3-23 上午10:45:32
	 */
	public boolean transferNcsToTempTable(Statement stmt, List<Long> ncsIds,
			long saveNcsId, Map<String, String> configs,
			Map<String, String> result) {
		log.debug("进入方法：transferNcsToTempTable。stmt=" + stmt + ",ncsIds="
				+ ncsIds);
		String ncsIdStr = RnoHelper.getStrValFromList(ncsIds);
		String ncsFields = "CELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,NCELL,DISTANCE,INTERFER,CA_INTERFER,NCELLS,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CI_DIVIDER,CA_DIVIDER";
		String sql = "insert  into RNO_NCS_MID(RNO_NCS_DESC_ID," + ncsFields
				+ ") select " + saveNcsId + "," + ncsFields// 固定ncsId，
				+ " from rno_ncs where RNO_NCS_DESC_ID in ( " + ncsIdStr + " )";

		// --2014-7-11 重新处理，每个一次
		Connection conn = null;
		String ciThreshold = "-12";
		String caThreshold = "+3";
		try {
			conn = stmt.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
			if (result != null) {
				result.put("wrong",
						"转移Ncs数据到临时分析表时，由于无法获取相应的connection，无法继续进行！");
			}
			return false;
		}
		// 判断门限值
		if (configs != null) {
			if (!StringUtils.isEmpty(configs.get("CITHRESHOLD"))) {
				ciThreshold = configs.get("CITHRESHOLD");
			}
			if (!StringUtils.isEmpty(configs.get("CATHRESHOLD"))) {
				caThreshold = configs.get("CATHRESHOLD");
			}

		}
		for (long ncsId : ncsIds) {
			sql = "insert  into RNO_NCS_MID(RNO_NCS_DESC_ID," + ncsFields
					+ ") select RNO_NCS_DESC_ID ," + ncsFields
					+ " from rno_ncs where RNO_NCS_DESC_ID =" + ncsId;

			log.debug("转移ncs数据到临时表的sql=" + sql);
			try {
				int affectCnt = stmt.executeUpdate(sql);
				log.debug("转移ncs[" + ncsId + "]数据到临时表数据量：" + affectCnt);
			} catch (SQLException e) {
				log.error("转移数据ncs[" + ncsId + "]到临时表执行失败！sql=" + sql);
				if (result != null) {
					result.put("wrong", "转移数据ncs[" + ncsId + "]到临时表执行失败！sql="
							+ sql);
				}
				e.printStackTrace();
				return false;
			}

			// 计算相应的干扰
			calculateInterfer(stmt, "RNO_NCS_MID", Arrays.asList(ncsId),
					ciThreshold, caThreshold);

		}

		// 最后统一将ncsid变换为新的同一个id
		sql = "UPDATE RNO_NCS_MID SET RNO_NCS_DESC_ID=" + saveNcsId;
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("更新分析表的ncs为统一的ncs_desc_id时出错！sql=" + sql);
			if (result != null) {
				result.put("wrong", "更新分析表的ncs为统一的ncs_desc_id时出错！sql=" + sql);
			}
			return false;
		}
		return true;
	}

	/**
	 * 标识RNO_NCS_MID中的小区、邻区是否室分的信息字段
	 * 
	 * @param stmt
	 * @return
	 * @author brightming 2014-3-23 上午10:53:29
	 */
	public boolean markIsIndoorFlag(Statement stmt) {
		log.debug("进入方法：markIsIndoorFlag.stmt=" + stmt);

		int affectCnt = 0;
		// ---主小区---//
		String sql = "merge into RNO_NCS_MID tar "
				+ " using "
				+ " ( select DISTINCT(label),min(INDOOR_CELL_TYPE) AS indoor from cell group by label ) src "
				+ " on (tar.cell=src.label) "
				+ " when matched then update set tar.cell_indoor=(case when src.indoor='"
				+ RnoConstant.CellType.INDOOR_CELL.getName()
				+ "' then 'Y' else 'N' end)";
		log.debug("更新rno_ncs_mid表的主小区cell是否室分的sql：" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新主小区是否是室分小区，影响数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新rno_ncs_mid的小区的室分信息时出错！sql=" + sql);
			e.printStackTrace();
			return false;
		}
		// --邻区--//
		sql = "merge into RNO_NCS_MID tar "
				+ " using "
				+ " ( select DISTINCT(label),min(INDOOR_CELL_TYPE) AS indoor from cell group by label ) src "
				+ " on (tar.ncell=src.label) "
				+ " when matched then update set tar.ncell_indoor=(case when src.indoor='"
				+ RnoConstant.CellType.INDOOR_CELL.getName()
				+ "' then 'Y' else 'N' end)";
		log.debug("更新rno_ncs_mid表的邻区NCELL是否室分的sql：" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新邻小区是否是室分小区，影响数量：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新rno_ncs_mid的邻小区的室分信息时出错！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// --看更新结果---//
		// sql="select cell,ncell,cell_indoor,ncell_indoor from rno_ncs_mid";
		// List<Map<String,Object>> res=RnoHelper.commonQuery(stmt, sql);
		// for(Map<String,Object> one:res){
		// System.out.println("cell="+one.get("CELL")+",NCELL="+one.get("NCELL")+",CELL_INDOOR="+one.get("CELL_INDOOR")+",ncell_indoor="+one.get("NCELL_INDOOR"));
		// }
		return true;

	}

	/**
	 * 挑选问题小区
	 * 
	 * @param stmt
	 * @param cityId
	 *            城市id
	 * @author brightming 2014-3-24 下午4:28:26
	 */
	public boolean pickCellsWithProblem(Statement stmt, long cityId) {
		log.debug("进入方法：pickCellsWithProblem。stmt=" + stmt);
		String sql = "";
		int affectCnt = 0;
		long t1;
		long t2;
		// ---------关联邻小区数>20 问题类型1----------//
		String fields1 = "CELL";// ,EXPECTED_COVER_DIS,OVERSHOOTING_FACT,INTERFER_NCELL_CNT,INTERFER_NCE_CNT_EXINDR,CAPACITY_DESTROY_FACT";
		sql = "insert into RNO_NCS_PROBLEM_CELL_MID("
				+ fields1
				+ ",PROBLEM_TYPE,PROBLEM_DESC) select "
				+ fields1
				+ ",1,'[过多关联邻小区]' from RNO_NCS_CELL_ANA_RESULT_MID where INTERFER_NCELL_CNT>20";
		log.debug("筛选关联邻小区数大于20的问题小区。sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			affectCnt = stmt.executeUpdate(sql);
			t2 = System.currentTimeMillis();
			log.debug("筛选关联邻小区数大于20的问题小区。耗时：" + (t2 - t1) + "ms.影响数量为："
					+ affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选关联邻小区数大于20的问题小区时出错！sql=" + sql);
			return false;
		}

		// watchValue("--获取关联邻小区大于20后",stmt);

		// ---------------列出问题簇小区 问题类型2------------//
		// 找问题簇id
		sql = "select mid1.cluster_id "
				+ " FROM( "
				+ " select CLUSTER_ID,sum(FREQ_CNT) as cnt from RNO_NCS_CLUSTER_CELL_MID mid1 group by CLUSTER_ID "
				+ " )mid1 "
				+ " left join RNO_NCS_CLUSTER_MID mid2 "
				+ " on  "
				+ " mid1.cluster_id=mid2.id "
				+ " where  "
				+ " (cnt>60 and freq_section='GSM900' or cnt>80 and freq_section='GSM1800')";
		// 处于问题簇里的小区筛选出来
		sql = "SELECT distinct(CELL) FROM RNO_NCS_CLUSTER_CELL_MID WHERE CLUSTER_ID IN ("
				+ sql.toUpperCase() + ")";
		// 插入分析表
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID tar using ("
				+ sql
				+ ") SRC ON (tar.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+2-BITAND(TAR.PROBLEM_TYPE,2)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[问题簇小区]' WHEN NOT MATCHED THEN INSERT (CELL,PROBLEM_DESC) VALUES (SRC.CELL,'[问题簇小区]')";

		log.debug("筛选问题簇小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选问题簇小区。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选问题簇小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取问题簇小区后",stmt);
		// ------关键小区-----------------//
		// （1）覆盖分量top10，且关联邻小区数大于20。问题类型：4 ——肯定是在第一步找过覆盖小区时得到的小区的子集
		sql = "SELECT CELL FROM (SELECT CELL,avg(CELL_COVER) as CELL_COVER  FROM RNO_NCS_CELL_ANA_RESULT_MID WHERE ROWNUM<=10 group by CELL ORDER BY CELL_COVER DESC ) ";
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING ("
				+ sql
				+ ") SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+4-BITAND(TAR.PROBLEM_TYPE,4)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[覆盖分量排在TOP10]'";

		log.debug("筛选覆盖分量top10，且关联邻小区数大于20的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选覆盖分量top10，且关联邻小区数大于20的小区。耗时：" + (t2 - t1)
					+ "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选覆盖分量top10，且关联邻小区数大于20的小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取覆盖分量top10小区后",stmt);
		// （2）小区容量分量TOP10% 问题类型：8
		sql = "SELECT CELL FROM (SELECT CELL, avg(CAPACITY_DESTROY) as CAPACITY_DESTROY  FROM RNO_NCS_CELL_ANA_RESULT_MID WHERE ROWNUM<=10 group by cell ORDER BY CAPACITY_DESTROY DESC ) ";
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING ("
				+ sql
				+ ") SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.PROBLEM_TYPE=(TAR.PROBLEM_TYPE+8-BITAND(TAR.PROBLEM_TYPE,8)),TAR.PROBLEM_DESC=TAR.PROBLEM_DESC||'[容量分量排在TOP10]'";

		log.debug("筛选容量分量top10的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("筛选容量分量top10的小区。耗时：" + (t2 - t1) + "ms。影响数量为："
					+ affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("筛选容量分量top10的小区时出错！sql=" + sql);
			return false;
		}
		// watchValue("--获取容量分量top10小区后",stmt);

		// -------匹配小区基本信息----//
		// 包括：中文名、小区经度、小区纬度、天线挂高、天线下倾、是否室分、垂直半功率角、频段
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING (SELECT * FROM (SELECT LABEL,NAME,LONGITUDE,LATITUDE,ANT_HEIGH,DOWNTILT,INDOOR_CELL_TYPE,ANT_HALF_POWER,(CASE WHEN BCCH<100 THEN 'GSM900' WHEN BCCH>100 THEN 'GSM1800' END) FREQ_SECTION,row_number() over(partition by label order by bcch nulls last) as seq FROM CELL) WHERE SEQ=1) SRC ON (TAR.CELL=SRC.LABEL) WHEN MATCHED THEN UPDATE SET TAR.CELLNAME=SRC.NAME,TAR.LONGITUDE=SRC.LONGITUDE,TAR.LATITUDE=SRC.LATITUDE,TAR.ANT_HEIGHT=SRC.ANT_HEIGH,TAR.DOWNTILT=SRC.DOWNTILT,TAR.INDOOR_CELL_TYPE=SRC.INDOOR_CELL_TYPE,TAR.ANT_HALF_POWER=SRC.ANT_HALF_POWER,TAR.FREQ_SECTION=SRC.FREQ_SECTION";
		log.debug("匹配小区基本信息，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("匹配小区基本信息。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("匹配小区基本信息时出错！sql=" + sql);
			return false;
		}

		// ---------匹配小区的结构分析结果---//
		// 包括：理想覆盖距离、过覆盖系数、关联邻小区数、不含室分的关联邻小区数、容量分量、覆盖分量
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING RNO_NCS_CELL_ANA_RESULT_MID SRC ON (TAR.CELL=SRC.CELL) WHEN MATCHED THEN UPDATE SET TAR.EXPECTED_COVER_DIS=SRC.EXPECTED_COVER_DIS,TAR.OVERSHOOTING_FACT=SRC.OVERSHOOTING_FACT,TAR.INTERFER_NCELL_CNT=SRC.INTERFER_NCELL_CNT,TAR.INTERFER_NCE_CNT_EXINDR=SRC.INTERFER_NCE_CNT_EXINDR,TAR.CAPACITY_DESTROY=SRC.CAPACITY_DESTROY,TAR.CELL_COVER=SRC.CELL_COVER";
		log.debug("匹配小区的结构分析结果，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("匹配小区的结构分析结果。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("匹配小区的结构分析结果时出错！sql=" + sql);
			return false;
		}

		// ---------计算理想覆盖下倾----//
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID SET IDEAL_DOWNTILT=ATAN(ANT_HEIGHT/EXPECTED_COVER_DIS*1.6)+ANT_HALF_POWER/2 WHERE EXPECTED_COVER_DIS>0";
		log.debug("计算理想下倾角，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算理想下倾角。耗时：" + (t2 - t1) + "ms。影响数量为：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("计算理想下倾角时出错！sql=" + sql);
			return false;
		}

		// -------补充话务情况-----//
		// ----需要最近一周的最忙时的话务----//
		// ---找各个小区最近的话务时间，再减去7天作为时间段。如果要准确匹配，必须要及时导入话统信息---//
		// String
		// selectCell="select distinct(cell) from (SELECT distinct(cell) FROM rno_ncs_mid UNION 	SELECT distinct(ncell) FROM rno_ncs_mid)	where UPPER(cell) not like 'NOT%' ".toUpperCase();

		// 找出所有的区域的最新话统时间
		// 得到各区域的最新话统时间到减去一周的时间范围内，所有的话统描述id。字段并没有指定含area_id
		sql = "SELECT distinct(des.sts_desc_id) as sts_desc_id "
				+ " FROM rno_sts_descriptor des "
				+ " LEFT JOIN "
				+ " (SELECT AREA_ID, "
				+ " MAX(STS_DATE) latest_DATE, "
				+ " MAX(STS_DATE)-7 AS early_date "
				+ " FROM RNO_STS_DESCRIPTOR "
				+ " WHERE AREA_ID IN "
				+ " (SELECT AREA_ID FROM SYS_AREA WHERE PARENT_ID="
				+ cityId
				+ " ) "
				+ " GROUP BY AREA_ID "
				+ " ) mid "
				+ " ON (des.area_id=mid.area_id "
				+ " AND des.sts_date BETWEEN mid.early_date AND mid.latest_date)";

		// 转移到话统临时表
		sql = "INSERT INTO RNO_TEMP_STS " + " ( " + " STS_ID, "
				+ " DESCRIPTOR_ID, " + " CELL, " + " DECLARE_CHANNEL_NUMBER, "
				+ " AVAILABLE_CHANNEL_NUMBER, " + " CARRIER_NUMBER, "
				+ " RESOURCE_USE_RATE, " + " TRAFFIC " + " ) "
				+ " SELECT STS_ID, " + " DESCRIPTOR_ID, " + " CELL, "
				+ " DECLARE_CHANNEL_NUMBER, " + " AVAILABLE_CHANNEL_NUMBER, "
				+ " CARRIER_NUMBER, " + " RESOURCE_USE_RATE, " + " TRAFFIC "
				+ " FROM rno_sts " + " WHERE DESCRIPTOR_ID IN " + " ( " + sql
				+ " ) ";

		log.debug("转移合适范围的小区话统数据到话统临时表，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("转移合适范围的小区话统数据到话统临时表，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("转移合适范围的小区话统数据到话统临时表时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// 只保留每个小区的最大无线资源利用率记录
		sql = "merge INTO RNO_TEMP_STS tar USING "
				+ " (SELECT cell, "
				+ " MAX(RESOURCE_USE_RATE) AS RESOURCE_USE_RATE "
				+ " FROM RNO_TEMP_STS "
				+ " GROUP BY cell "
				+ " )src ON (tar.cell=src.cell ) "
				+ " WHEN matched THEN "
				+ " UPDATE SET tar.NET_TYPE=NULL "
				+ " DELETE WHERE (tar.RESOURCE_USE_RATE<src.RESOURCE_USE_RATE) ";
		log.debug("保留话统临时表的最大无线资源利用率，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("保留话统临时表的最大无线资源利用率，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("保留话统临时表的最大无线资源利用率时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// 去除话统临时表的重复的小区
		sql = " merge INTO RNO_TEMP_STS tar USING "
				+ " (SELECT CELL, "
				+ " STS_ID "
				+ " FROM "
				+ " (SELECT cell, "
				+ " sts_id, "
				+ " row_number() over(partition BY cell order by sts_id) AS seq "
				+ " FROM RNO_TEMP_STS " + " ) " + " WHERE SEQ        =1 "
				+ " ) src ON(tar.cell=src.cell) " + " WHEN matched THEN "
				+ " UPDATE SET tar.NET_TYPE=NULL "
				+ " DELETE WHERE (tar.sts_id<>src.sts_id) ";
		log.debug("去除话统临时表的重复的小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("去除话统临时表的重复的小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("去除话统临时表的重复的小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// --更新问题小区的话统数据---//
		sql = "MERGE INTO RNO_NCS_PROBLEM_CELL_MID TAR USING RNO_TEMP_STS SRC ON(TAR.CELL=SRC.CELL) "
				+ " WHEN MATCHED THEN "
				+ " UPDATE "
				+ " SET TAR.DECLARE_CHANNEL_NUMBER=SRC.DECLARE_CHANNEL_NUMBER, "
				+ " TAR.AVAILABLE_CHANNEL_NUMBER=SRC.AVAILABLE_CHANNEL_NUMBER, "
				+ " TAR.CARRIER_NUMBER          =SRC.CARRIER_NUMBER, "
				+ " TAR.RESOURCE_USE_RATE       =SRC.RESOURCE_USE_RATE, "
				+ " TAR.TRAFFIC                 =SRC.TRAFFIC ";
		log.debug("更新问题小区的话统数据，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新问题小区的话统数据，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("更新问题小区的话统数据时出错了！sql=" + sql);
			e.printStackTrace();
			// 看rno_temp_sts的表的内容
			sql = "select cell,count(*) from RNO_TEMP_STS group by cell having count(*)>1";
			List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
			for (Map<String, Object> one : res) {
				log.error("----输出RNO_TEMP_STS的信息：");
				for (String k : one.keySet()) {
					log.error(k + "=" + one.get(k) + ",");
				}
			}
			return false;
		}

		// ---计算问题小区六强关联邻区的话务情况----//
		sql = "merge INTO RNO_NCS_PROBLEM_CELL_MID tar USING "
				+ " (SELECT cell, "
				+ " AVG(resource_use_rate) AS AVG_NCELL_USE_RATE "
				+ " FROM "
				+ " (SELECT mid1.cell, "
				+ " mid1.ncell, "
				+ " mid2.RESOURCE_USE_RATE "
				+ " FROM "
				+ " (SELECT ncell AS cell, "
				+ " cell        AS ncell "
				+ " FROM "
				+ " (SELECT ncell, "
				+ " cell, "
				+ " interfer , "
				+ " row_number () over (partition BY ncell order by interfer DESC) rn "
				+ " FROM rno_ncs_mid " + " ) " + " WHERE rn  <=6 "
				+ " AND ncell IN "
				+ " (SELECT cell FROM RNO_NCS_PROBLEM_CELL_MID " + " ) "
				+ " )mid1 " + " LEFT JOIN rno_temp_sts mid2 "
				+ " ON(mid1.ncell=mid2.cell) " + " ) " + " GROUP BY cell "
				+ " )src ON (tar.cell=src.cell) " + " WHEN matched THEN "
				+ " UPDATE SET tar.AVG_NCELL_USE_RATE=src.AVG_NCELL_USE_RATE ";
		log.debug("计算问题小区六强关联邻区的话务情况，sql=" + sql);
		t1 = System.currentTimeMillis();
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("计算问题小区六强关联邻区的话务情况，耗时：" + (t2 - t1) + "ms。影响数据:"
					+ affectCnt);
		} catch (SQLException e) {
			log.error("计算问题小区六强关联邻区的话务情况时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// ---生成优化建议---//
		// 1、下倾角调整建议
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE=" + " CASE "
				+ " WHEN DOWNTILT<IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[调整为理想覆盖下倾]' "
				+ " WHEN DOWNTILT>IDEAL_DOWNTILT "
				+ " AND DOWNTILT<=2*IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[实际下倾加2~5度]' "
				+ " WHEN DOWNTILT>2*IDEAL_DOWNTILT "
				+ " THEN MESSAGE||'[检查或更换天线]' " + " END";
		log.debug("生成下倾角调整建议，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("生成下倾角调整建议，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("生成下倾角调整建议时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}
		// ------查看效果---//
		// sql="select cell,DOWNTILT,IDEAL_DOWNTILT,MESSAGE FROM RNO_NCS_PROBLEM_CELL_MID WHERE DOWNTILT>IDEAL_DOWNTILT "
		// + " AND DOWNTILT<=2*IDEAL_DOWNTILT";
		// List<Map<String, Object>> res;
		// Object v;
		// watchValue("--查看下倾角调整建议后",stmt);

		// 2、根据话务情况产生建议
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE= " + " CASE "
				+ " WHEN RESOURCE_USE_RATE<1 " + " THEN MESSAGE "
				+ " ||'[减容]' " + " WHEN (FREQ_SECTION     ='GSM900' "
				+ " AND CARRIER_NUMBER     >7 "
				+ " OR FREQ_SECTION        ='GSM1800' "
				+ " AND CARRIER_NUMBER     >9) "
				+ " AND AVG_NCELL_USE_RATE<=1.4 " + " THEN MESSAGE "
				+ " ||'[话务均衡，减容]' " + " WHEN (FREQ_SECTION    ='GSM900' "
				+ " AND CARRIER_NUMBER    >7 "
				+ " OR FREQ_SECTION       ='GSM1800' "
				+ " AND CARRIER_NUMBER    >9) "
				+ " AND AVG_NCELL_USE_RATE>1.4 " + " THEN MESSAGE "
				+ " ||'[加向或新建站，分流话务]'" + " ELSE MESSAGE" + " END";
		log.debug("调整高话务小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("调整高话务小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("调整高话务小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// watchValue("--调整高话务小区建议后",stmt);
		// 3、高站且高配小区
		sql = "UPDATE RNO_NCS_PROBLEM_CELL_MID " + " SET MESSAGE=MESSAGE "
				+ " ||'[制定容量下沉方案]' " + " WHERE ANT_HEIGHT    >50 "
				+ " AND (FREQ_SECTION  ='GSM900' " + " AND CARRIER_NUMBER >7 "
				+ " OR FREQ_SECTION    ='GSM1800' " + " AND CARRIER_NUMBER >9)";
		log.debug("调整高站且高配小区，sql=" + sql);
		t1 = t2;
		try {
			t2 = System.currentTimeMillis();
			affectCnt = stmt.executeUpdate(sql);
			log.debug("调整高站且高配小区，耗时：" + (t2 - t1) + "ms。影响数据:" + affectCnt);
		} catch (SQLException e) {
			log.error("调整高站且高配小区时出错了！sql=" + sql);
			e.printStackTrace();
			return false;
		}

		// ---更新使用的话统时间段---//

		// watchValue("--全部结束后",stmt);

		return true;
	}

	private void watchValue(String title, Statement stmt) {
		String sql;
		sql = "select * from RNO_NCS_PROBLEM_CELL_MID";
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		System.out.println(title + ",共有：" + res.size());
		Object v;
		res = RnoHelper.commonQuery(stmt, sql);
		for (Map<String, Object> one : res) {
			System.out.println("----");
			for (String key : one.keySet()) {
				v = one.get(key);
				if (v instanceof BigDecimal) {
					System.out.print(key + "=" + ((BigDecimal) v).floatValue()
							+ ",");
				} else {
					System.out.print(key + "=" + v + ",");
				}
				// System.out.println(key+"="+()?((BigDecimal)one.get(key)).floatValue():one.get(key));
			}
			System.out.println("---");
		}
	}

	/**
	 * 汇总分析的入口调用
	 * 
	 * @param connection
	 * @param stmt
	 * @param savePath
	 *            包含完整文件名的最终保存路径
	 * @param cityId
	 * @param oriNcsIds
	 *            需要处理的原始的ncsid列表
	 * @return flag:true/false msg:如果flag为false，则为详细的失败消息
	 * @author brightming 2014-3-27 下午4:57:28
	 */
	public Map<String, Object> doNcsAnalysis(Connection connection,
			Statement stmt, String savePath, long cityId, List<Long> oriNcsIds,
			long taskId) {
		long t1;
		long t2;
		String sql;

		Map<String, Object> result = new HashMap<String, Object>();

		long staticNcsId = 100L;
		List<Long> staticNcsIds = new ArrayList<Long>();
		staticNcsIds.add(staticNcsId);

		boolean ok = transferNcsToTempTable(stmt, oriNcsIds, staticNcsId, null,
				null);
		if (!ok) {
			result.put("flag", false);
			result.put("msg", "无法将目标范围数据转移到分析表，无法进行运算！");
			return result;
		}

		// --2014-6-16 gmh comment out begin---//
		// do the match job at parse step

		// ---匹配邻区----//
		// log.debug("汇总ncs>>>>>>>>>>>>>>>开始匹配邻区信息...");
		// t1 = System.currentTimeMillis();
		// matchNcsNcell(connection, "RNO_NCS_MID", staticNcsIds, cityId);
		// t2 = System.currentTimeMillis();
		// log.debug("汇总ncs<<<<<<<<<<<<<<<完成匹配邻区信息，结果:" + ok + ",耗时：" + (t2 -
		// t1));
		// --2014-6-16 gmh comment out end---//

		// 匹配室分信息
		log.debug("汇总ncs>>>>>>>>>>>>>>>开始标识临时分析表中的小区、邻区的室分信息...");
		t1 = System.currentTimeMillis();
		ok = markIsIndoorFlag(stmt);
		t2 = System.currentTimeMillis();
		log.debug("汇总ncs<<<<<<<<<<<<<<<完成标识临时分析表中的小区、邻区的室分信息，结果:" + ok + ",耗时："
				+ (t2 - t1));
		if (!ok) {
			result.put("flag", false);
			result.put("msg", "无法标识小区的室分信息，无法进行运算！");
			return result;
		}
		// 判断线程是否被停止
		// System.out.println("线程的id："+ExecutorManager.getThreadIdByTaskId(taskId));
		// System.out.println("线程的状态："+ExecutorManager.getThread(ExecutorManager.getThreadIdByTaskId(taskId)).isInterrupted());
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		// --2014-6-16 gmh comment out begin---//
		// interfer value has been calculated at parse step

		// 计算干扰度
		// log.debug("ncs>>>>>>>>>>>>>>>开始计算干扰度...");
		// t1 = t2;
		// calculateInterfer(connection, "RNO_NCS_MID", staticNcsIds);
		// t2 = System.currentTimeMillis();
		// log.debug("ncs<<<<<<<<<<<<<<<完成计算干扰度。耗时:" + (t2 - t1) + "ms");
		// --2014-6-16 gmh comment out end---//

		// 计算干扰矩阵---暂时不算，没发现有什么特别用处
		// rnoStructureAnalysisDao.calculateInterferMatrix(connection,
		// "RNO_NCS_MID", "", firmNcsIds);
		// 计算簇
		log.debug("汇总>>>>>>>>>>>>>>>开始计算最大连通簇...");
		t1 = System.currentTimeMillis();
		calculateConnectedCluster(connection, "RNO_NCS_MID", staticNcsIds,
				"RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID",
				"RNO_NCS_CLUSTER_CELL_RELA_MID", 0.05f);
		t2 = System.currentTimeMillis();
		log.debug("汇总<<<<<<<<<<<<<<<完成计算最大连通簇。耗时:" + (t2 - t1) + "ms");
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		// 计算簇约束因子
		log.debug("汇总>>>>>>>>>>>>>>>开始计算簇约束因子...");
		t1 = t2;
		// 注意其area_id参数未起作用
		calculateClusterConstrain(stmt, "RNO_NCS_CLUSTER_MID",
				"RNO_NCS_CLUSTER_CELL_MID", staticNcsIds);
		t2 = System.currentTimeMillis();
		log.debug("汇总<<<<<<<<<<<<<<<完成计算簇约束因子。耗时:" + (t2 - t1) + "ms");
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		// 簇权重
		log.debug("汇总>>>>>>>>>>>>>>>开始计算簇权重...");
		t1 = t2;
		calculateNcsClusterWeight(connection, "RNO_NCS_MID",
				"RNO_NCS_CLUSTER_MID", "RNO_NCS_CLUSTER_CELL_MID",
				staticNcsIds, Arrays.asList(-1L, -2L), true, 0.5f);
		t2 = System.currentTimeMillis();
		log.debug("汇总<<<<<<<<<<<<<<<完成计算簇权重。耗时:" + (t2 - t1) + "ms");
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		// 结构指数计算
		log.debug("汇总>>>>>>>>>>>>>>>开始计算结构指数...");
		t1 = t2;
		calculateCellRes(connection, "RNO_NCS_MID",
				"RNO_NCS_CELL_ANA_RESULT_MID", "RNO_NCS_CLUSTER_MID",
				"RNO_NCS_CLUSTER_CELL_MID", staticNcsIds, null);
		t2 = System.currentTimeMillis();
		log.debug("汇总<<<<<<<<<<<<<<<完成计算结构指数。耗时:" + (t2 - t1) + "ms");
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		// ---2014-6-16 gmh add begin -- 计算干扰矩阵 --//
		log.debug(">>>>>>>>>>>>准备干扰矩阵所需数据...");
		long cnt = prepareFreqInterferMetaData(connection, staticNcsIds, null,
				"RNO_NCS_MID");
		log.debug("<<<<<<<<<<<干扰矩阵所需数据准备完毕，数量：" + cnt);
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}
		// CellFreqInterferList intermatrix = null;
		log.debug(">>>>>>>>>>>开始计算干扰矩阵.....");
		if (cnt > 0) {

			File file = new File(savePath);
			if (file.isDirectory() && !file.exists()) {
				file.mkdirs();
			} else if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			String fileName = file.getName();
			String parentPath = file.getParentFile().getAbsolutePath();
			//
			String pre = fileName;
			// 去除后缀名
			if (fileName.lastIndexOf(".") > 0) {
				pre = fileName.substring(0, fileName.lastIndexOf("."));
			}
			// 重新补充为全路径名
			pre = parentPath + File.separator + pre;
			String idxFilePath = pre
					+ RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
			String dataFilePath = pre
					+ RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;
			calculateFreqInterferMatrixResult(stmt, idxFilePath, dataFilePath);
			// FileTool.saveInputStream("d:/intermatrix.txt",
			// new ByteArrayInputStream(intermatrix.toString().getBytes()));
			log.debug("<<<<<<<<干扰矩阵计算完毕。");
		}
		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}
		// --2014-6-16 gmh add end -- 计算干扰矩阵--//

		// ---------自动优化----//
		log.debug("汇总ncs>>>>>>>>>>>>>>>开始产生自动优化建议...");
		t1 = t2;
		pickCellsWithProblem(stmt, cityId);
		t2 = System.currentTimeMillis();
		log.debug("汇总ncs<<<<<<<<<<<<<<<完成自动优化建议生成。耗时:" + (t2 - t1) + "ms");

		// 判断线程是否被停止
		if (ExecutorManager.isTheTaskThreadInterrupted(taskId)) {
			log.error("ID=[" + taskId + "]的任务被停止了！");
			result.put("flag", "cancel");
			result.put("msg", "取消了分析任务");
			return result;
		}

		Date outputTime = new Date();
		try {
			// 保存分析结果到excel
			saveNcsAnaResInExcel(stmt, savePath, staticNcsId,taskId);
			result.put("flag", true);
			result.put("msg", "完成");
			return result;

		} catch (Exception e) {
			e.printStackTrace();

			result.put("flag", false);
			result.put("msg", "保存汇总结果时失败!");
			return result;
		}
	}

	/**
 * 保存NCS的分析结果到excel表 由于目标数据量过于庞大，此方法不建议使用！
 * 
 * @param stmt
 * @param savePath
 * @param staticNcsId
 * @throws IOException
 * @author brightming 2014-6-12 下午2:03:48
 * @author chao.xj  2014-7-24 上午11:17:16 增加 taskId参数
 */
@Deprecated
private void saveNcsAnaResInExcel(Statement stmt, String savePath,
		long staticNcsId,long taskId) throws IOException {
	long t1;
	long t2;
	String sql="";
	boolean ok;
	// ---------输出结果------//

	// 除了输出一份到excel以外，还输出到一份文本文件
	List<String> sheetNames = new ArrayList<String>();
	List<String[]> titless = new ArrayList<String[]>();
	List<String[]> keyss = new ArrayList<String[]>();
	List<List<Map<String, Object>>> cellress = new ArrayList<List<Map<String, Object>>>();

	//@author chao.xj  2014-7-24 上午11:17:16
	List<Map<String, Object>> params = rnoTaskDao.getTaskParams(taskId);
	log.info("task id=" + taskId + "的参数情况：" + params);
	List<Long> oriMrrIds = new ArrayList<Long>();
	String mrrIds = "",v;
	for (Map<String, Object> one : params) {
		if (one == null || one.isEmpty()) {
			continue;
		}
		if("MRRID".equals(one.get("PARAM_NAME") + "")){
			v = (String) one.get("PARAM_VALUE");
			if (v != null && !"".equals(v.trim())) {
				mrrIds += v + ",";
				oriMrrIds.add(Long.parseLong(v));
			}
			
		}
	}
	if (mrrIds.length() > 0) {
		mrrIds = mrrIds.substring(0, mrrIds.length() - 1);
	}
	log.debug("mrrIds:"+mrrIds);
	String mrrsql="select t1.cell_name,"
	+"       t1.sumul0t5 / decode(t1.sumul0t7, 0, 1, t1.sumul0t7) ulquality,						"
	+"       t1.sumdl0t5 / decode(t1.sumdl0t7, 0, 1, t1.sumdl0t7) dlquality,                        "
	+"       t1.sumul6t7 / decode(t1.sumul0t7, 0, 1, t1.sumul0t7) ulpoorquality,                    "
	+"       t1.sumdl6t7 / decode(t1.sumdl0t7, 0, 1, t1.sumdl0t7) dlpoorquality,                    "
	+"       t2.sumullev10t63 / decode(t2.sumullev0t63, 0, 1, t2.sumullev0t63) ulcoverage,          "
	+"       t2.sumdllev16t63 / decode(t2.sumdllev0t63, 0, 1, t2.sumdllev0t63) dlcoverage,          "
	+"       1 -                                                                                    "
	+"       t2.sumullev10t63 / decode(t2.sumullev0t63, 0, 1, t2.sumullev0t63) ulpoorcoverage,      "
	+"       1 -                                                                                    "
	+"       t2.sumdllev16t63 / decode(t2.sumdllev0t63, 0, 1, t2.sumdllev0t63) dlpoorcoverage,      "
	+"       t3.ta_average                                                                          "
	+"  from (select cell_name,                                                                     "
	+"               sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +                "
	+"                   rxqualul5) sumul0t5,                                                       "
	+"               sum(rxqualul0 + rxqualul1 + rxqualul2 + rxqualul3 + rxqualul4 +                "
	+"                   rxqualul5 + rxqualul6 + rxqualul7) sumul0t7,                               "
	+"               sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 +                "
	+"                   rxqualdl5) sumdl0t5,                                                       "
	+"               sum(rxqualdl0 + rxqualdl1 + rxqualdl2 + rxqualdl3 + rxqualdl4 +                "
	+"                   rxqualdl5 + rxqualdl6 + rxqualdl7) sumdl0t7,                               "
	+"               sum(rxqualul6 + rxqualul7) sumul6t7,                                           "
	+"               sum(rxqualdl6 + rxqualdl7) sumdl6t7                                            "
	+"          from rno_eri_mrr_quality                                                            "
	+"         where ERI_MRR_DESC_ID in ("+mrrIds+")                                                        "
	+"         group by cell_name) t1,                                                              "
	+"       (select cell_name,                                                                     "
	+"               sum(RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                "
	+"                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                "
	+"                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                "
	+"                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                "
	+"                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                "
	+"                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                "
	+"                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                "
	+"                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                "
	+"                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                "
	+"                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                "
	+"                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) sumullev10t63,              "
	+"               sum(RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 + RXLEVDL20 +                "
	+"                   RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 + RXLEVDL25 +                "
	+"                   RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 + RXLEVDL30 +                "
	+"                   RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 + RXLEVDL35 +                "
	+"                   RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 + RXLEVDL40 +                "
	+"                   RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 + RXLEVDL45 +                "
	+"                   RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 + RXLEVDL50 +                "
	+"                   RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 + RXLEVDL55 +                "
	+"                   RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 + RXLEVDL60 +                "
	+"                   RXLEVDL61 + RXLEVDL62 + RXLEVDL63) sumdllev16t63,                          "
	+"               sum(RXLEVUL0 + RXLEVUL1 + RXLEVUL2 + RXLEVUL3 + RXLEVUL4 +                     "
	+"                   RXLEVUL5 + RXLEVUL6 + RXLEVUL7 + RXLEVUL8 + RXLEVUL9 +                     "
	+"                   RXLEVUL10 + RXLEVUL11 + RXLEVUL12 + RXLEVUL13 + RXLEVUL14 +                "
	+"                   RXLEVUL15 + RXLEVUL16 + RXLEVUL17 + RXLEVUL18 + RXLEVUL19 +                "
	+"                   RXLEVUL20 + RXLEVUL21 + RXLEVUL22 + RXLEVUL23 + RXLEVUL24 +                "
	+"                   RXLEVUL25 + RXLEVUL26 + RXLEVUL27 + RXLEVUL28 + RXLEVUL29 +                "
	+"                   RXLEVUL30 + RXLEVUL31 + RXLEVUL32 + RXLEVUL33 + RXLEVUL34 +                "
	+"                   RXLEVUL35 + RXLEVUL36 + RXLEVUL37 + RXLEVUL38 + RXLEVUL39 +                "
	+"                   RXLEVUL40 + RXLEVUL41 + RXLEVUL42 + RXLEVUL43 + RXLEVUL44 +                "
	+"                   RXLEVUL45 + RXLEVUL46 + RXLEVUL47 + RXLEVUL48 + RXLEVUL49 +                "
	+"                   RXLEVUL50 + RXLEVUL51 + RXLEVUL52 + RXLEVUL53 + RXLEVUL54 +                "
	+"                   RXLEVUL55 + RXLEVUL56 + RXLEVUL57 + RXLEVUL58 + RXLEVUL59 +                "
	+"                   RXLEVUL60 + RXLEVUL61 + RXLEVUL62 + RXLEVUL63) sumullev0t63,               "
	+"               sum(RXLEVDL0 + RXLEVDL1 + RXLEVDL2 + RXLEVDL3 + RXLEVDL4 +                     "
	+"                   RXLEVDL5 + RXLEVDL6 + RXLEVDL7 + RXLEVDL8 + RXLEVDL9 +                     "
	+"                   RXLEVDL10 + RXLEVDL11 + RXLEVDL12 + RXLEVDL13 + RXLEVDL14 +                "
	+"                   RXLEVDL15 + RXLEVDL16 + RXLEVDL17 + RXLEVDL18 + RXLEVDL19 +                "
	+"                   RXLEVDL20 + RXLEVDL21 + RXLEVDL22 + RXLEVDL23 + RXLEVDL24 +                "
	+"                   RXLEVDL25 + RXLEVDL26 + RXLEVDL27 + RXLEVDL28 + RXLEVDL29 +                "
	+"                   RXLEVDL30 + RXLEVDL31 + RXLEVDL32 + RXLEVDL33 + RXLEVDL34 +                "
	+"                   RXLEVDL35 + RXLEVDL36 + RXLEVDL37 + RXLEVDL38 + RXLEVDL39 +                "
	+"                   RXLEVDL40 + RXLEVDL41 + RXLEVDL42 + RXLEVDL43 + RXLEVDL44 +                "
	+"                   RXLEVDL45 + RXLEVDL46 + RXLEVDL47 + RXLEVDL48 + RXLEVDL49 +                "
	+"                   RXLEVDL50 + RXLEVDL51 + RXLEVDL52 + RXLEVDL53 + RXLEVDL54 +                "
	+"                   RXLEVDL55 + RXLEVDL56 + RXLEVDL57 + RXLEVDL58 + RXLEVDL59 +                "
	+"                   RXLEVDL60 + RXLEVDL61 + RXLEVDL62 + RXLEVDL63) sumdllev0t63                "
	+"          from rno_eri_mrr_strength                                                           "
	+"         where ERI_MRR_DESC_ID in ("+mrrIds+")                                                        "
	+"         group by cell_name) t2,                                                              "
	+"       (select t1.cell_name,                                                                  "
	+"               t1.ta_numerator /                                                              "
	+"               decode(t1.ta_denominator, 0, 1, t1.ta_denominator)-1 ta_average                  "
	+"          from (select cell_name,                                                             "
	+"                       sum(TAVAL0 + TAVAL1 * 2 + TAVAL2 * 3 + TAVAL3 * 4 +                    "
	+"                           TAVAL4 * 5 + TAVAL5 * 6 + TAVAL6 * 7 + TAVAL7 * 8 +                "
	+"                           TAVAL8 * 9 + TAVAL9 * 10 + TAVAL10 * 11 +                          "
	+"                           TAVAL11 * 12 + TAVAL12 * 13 + TAVAL13 * 14 +                       "
	+"                           TAVAL14 * 15 + TAVAL15 * 16 + TAVAL16 * 17 +                       "
	+"                           TAVAL17 * 18 + TAVAL18 * 19 + TAVAL19 * 20 +                       "
	+"                           TAVAL20 * 21 + TAVAL21 * 22 + TAVAL22 * 23 +                       "
	+"                           TAVAL23 * 24 + TAVAL24 * 25 + TAVAL25 * 26 +                       "
	+"                           TAVAL26 * 27 + TAVAL27 * 28 + TAVAL28 * 29 +                       "
	+"                           TAVAL29 * 30 + TAVAL30 * 31 + TAVAL31 * 32 +                       "
	+"                           TAVAL32 * 33 + TAVAL33 * 34 + TAVAL34 * 35 +                       "
	+"                           TAVAL35 * 36 + TAVAL36 * 37 + TAVAL37 * 38 +                       "
	+"                           TAVAL38 * 39 + TAVAL39 * 40 + TAVAL40 * 41 +                       "
	+"                           TAVAL41 * 42 + TAVAL42 * 43 + TAVAL43 * 44 +                       "
	+"                           TAVAL44 * 45 + TAVAL45 * 46 + TAVAL46 * 47 +                       "
	+"                           TAVAL47 * 48 + TAVAL48 * 49 + TAVAL49 * 50 +                       "
	+"                           TAVAL50 * 51 + TAVAL51 * 52 + TAVAL52 * 53 +                       "
	+"                           TAVAL53 * 54 + TAVAL54 * 55 + TAVAL55 * 56 +                       "
	+"                           TAVAL56 * 57 + TAVAL57 * 58 + TAVAL58 * 59 +                       "
	+"                           TAVAL59 * 60 + TAVAL60 * 61 + TAVAL61 * 62 +                       "
	+"                           TAVAL62 * 63 + TAVAL63 * 64 + TAVAL64 * 65 +                       "
	+"                           TAVAL65 * 66 + TAVAL66 * 67 + TAVAL67 * 68 +                       "
	+"                           TAVAL68 * 69 + TAVAL69 * 70 + TAVAL70 * 71 +                       "
	+"                           TAVAL71 * 72 + TAVAL72 * 73 + TAVAL73 * 74 +                       "
	+"                           TAVAL74 * 75 + TAVAL75 * 76) ta_numerator,                         "
	+"                       sum(TAVAL0 + TAVAL1 + TAVAL2 + TAVAL3 + TAVAL4 +                       "
	+"                           TAVAL5 + TAVAL6 + TAVAL7 + TAVAL8 + TAVAL9 +                       "
	+"                           TAVAL10 + TAVAL11 + TAVAL12 + TAVAL13 + TAVAL14 +                  "
	+"                           TAVAL15 + TAVAL16 + TAVAL17 + TAVAL18 + TAVAL19 +                  "
	+"                           TAVAL20 + TAVAL21 + TAVAL22 + TAVAL23 + TAVAL24 +                  "
	+"                           TAVAL25 + TAVAL26 + TAVAL27 + TAVAL28 + TAVAL29 +                  "
	+"                           TAVAL30 + TAVAL31 + TAVAL32 + TAVAL33 + TAVAL34 +                  "
	+"                           TAVAL35 + TAVAL36 + TAVAL37 + TAVAL38 + TAVAL39 +                  "
	+"                           TAVAL40 + TAVAL41 + TAVAL42 + TAVAL43 + TAVAL44 +                  "
	+"                           TAVAL45 + TAVAL46 + TAVAL47 + TAVAL48 + TAVAL49 +                  "
	+"                           TAVAL50 + TAVAL51 + TAVAL52 + TAVAL53 + TAVAL54 +                  "
	+"                           TAVAL55 + TAVAL56 + TAVAL57 + TAVAL58 + TAVAL59 +                  "
	+"                           TAVAL60 + TAVAL61 + TAVAL62 + TAVAL63 + TAVAL64 +                  "
	+"                           TAVAL65 + TAVAL66 + TAVAL67 + TAVAL68 + TAVAL69 +                  "
	+"                           TAVAL70 + TAVAL71 + TAVAL72 + TAVAL73 + TAVAL74 +                  "
	+"                           TAVAL75) ta_denominator                                            "
	+"                  from rno_eri_mrr_ta                                                         "
	+"                  where ERI_MRR_DESC_ID in ("+mrrIds+")                                               "
	+"                 group by cell_name) t1) t3                                                   "
	+" where t1.cell_name = t2.cell_name and t1.cell_name=t3.cell_name                             ";
	// outputTime.getYear()
	// 将临时表的内容输出，输出到excel
	// 1、小区分析结果临时数据
	if(!"".equals(mrrIds)){
		sql = "select mid1.*,mid2.name as \"chineseName\",mid2.longitude as \"lng\",mid2.latitude as \"lat\",mid2.lnglats as \"allLngLats\",mrrtab.*  "
				+ " FROM( select CELL as \"cell\",FREQ_CNT, round(BE_INTERFER,5) BE_INTERFER ,round(NET_STRUCT_FACTOR,5) NET_STRUCT_FACTOR,round(REDUNT_COVER_FACT,5) REDUNT_COVER_FACT,round(OVERLAP_COVER,5) OVERLAP_COVER,round(SRC_INTERFER,5) SRC_INTERFER,round(OVERSHOOTING_FACT,5) OVERSHOOTING_FACT,DETECT_CNT,DETECT_CNT_EXINDR,round(EXPECTED_COVER_DIS,5) EXPECTED_COVER_DIS,INTERFER_NCELL_CNT,INTERFER_NCE_CNT_EXINDR,round(CELL_COVER,5) CELL_COVER,round(CAPACITY_DESTROY,5) CAPACITY_DESTROY "
				+ " from RNO_NCS_CELL_ANA_RESULT_MID where NCS_ID="
				+ staticNcsId
				+ " order by cell "
				+ " )mid1 left join ( select * from (SELECT label,name,longitude,latitude,lnglats ,row_number() over(partition by label order by name) as seq FROM cell )where seq=1)mid2 on(mid1.\"cell\"=mid2.label) "
				+"left join ("+mrrsql+") mrrtab on (mid1.\"cell\"=mrrtab.cell_name)";
		}else {
			sql = "select mid1.*,mid2.name as \"chineseName\",mid2.longitude as \"lng\",mid2.latitude as \"lat\",mid2.lnglats as \"allLngLats\"  "
					+ " FROM( select CELL as \"cell\",FREQ_CNT, round(BE_INTERFER,5) BE_INTERFER ,round(NET_STRUCT_FACTOR,5) NET_STRUCT_FACTOR,round(REDUNT_COVER_FACT,5) REDUNT_COVER_FACT,round(OVERLAP_COVER,5) OVERLAP_COVER,round(SRC_INTERFER,5) SRC_INTERFER,round(OVERSHOOTING_FACT,5) OVERSHOOTING_FACT,DETECT_CNT,DETECT_CNT_EXINDR,round(EXPECTED_COVER_DIS,5) EXPECTED_COVER_DIS,INTERFER_NCELL_CNT,INTERFER_NCE_CNT_EXINDR,round(CELL_COVER,5) CELL_COVER,round(CAPACITY_DESTROY,5) CAPACITY_DESTROY "
					+ " from RNO_NCS_CELL_ANA_RESULT_MID where NCS_ID="
					+ staticNcsId
					+ " order by cell "
					+ " )mid1 left join ( select * from (SELECT label,name,longitude,latitude,lnglats ,row_number() over(partition by label order by name) as seq FROM cell )where seq=1)mid2 on(mid1.\"cell\"=mid2.label) ";	
		}
	log.debug("1、小区分析结果临时数据sql:"+sql);
	// 输出到excel
	String[] cellResTitles = new String[RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES.length - 1];
	String[] cellResKeys = new String[RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS.length - 1];
	// 2014-6-13 gmh excel不输出“范围”信息
	int idx = 0;
	for (int m = 0; m < RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES.length; m++) {
		if ("范围".equals(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES[m])) {
			continue;
		}
		cellResTitles[idx] = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_TITLES[m];
		cellResKeys[idx] = RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS[m];
		idx++;
	}
	t1 = System.currentTimeMillis();
	List<Map<String, Object>> cellres=null;
	try {
		cellres = RnoHelper.commonQuery(stmt, sql);
	} catch (Exception e) {
		log.error("1、小区分析结果临时数据sql:"+sql);
		e.printStackTrace();
	}
	t2 = System.currentTimeMillis();
	log.debug("汇总分析，获取小区结构指标的耗时：" + (t2 - t1) + "ms.  sql=" + sql);

	// 2、连通簇数据
	// 标题
	sql = "select mid1.*,round(mid2.control_factor,5) control_factor,round(MID2.weight,5) weight FROM( select \"CLUSTER_ID\",COUNT(cell) cellcnt ,sum(freq_cnt) as total_freq_cnt ,wm_concat(cell) as sectors from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID where rno_ncs_desc_id="
			+ staticNcsId
			+ ") group by CLUSTER_ID "
			+ " )mid1 INNER JOIN ( select id,control_factor,weight from RNO_NCS_CLUSTER_MID where rno_ncs_desc_id="
			+ staticNcsId + " )mid2 " + " on mid1.cluster_id=MID2.id";
	// ClusterID（簇ID） Count（小区数） Trxs（频点数/载波数） 簇约束因子 簇权重 Sectors（小区列表）
	String[] clusterTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_TITLES;
	// {"ClusterID（簇ID）","Count（小区数）","Trxs（频点数/载波数）","簇约束因子","簇权重","Sectors（小区列表）"};
	// key
	String[] clusterKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET_KEYS;
	// {"CLUSTER_ID","CELLCNT","TOTAL_FREQ_CNT","CONTROL_FACTOR","WEIGHT","SECTORS"};
	t1 = System.currentTimeMillis();
	List<Map<String, Object>> clustRes = RnoHelper.commonQuery(stmt, sql);
	t2 = System.currentTimeMillis();
	log.debug("汇总分析，获取连通簇,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

	// 3、连通簇小区数据
	// 标题
	String[] clusterCellTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_TITLES;
	// {"簇ID","小区名","小区中文名","小区载频","簇TCH载频数"};
	// key
	String[] clusterCellKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET_KEYS;
	// {"CLUSTER_ID","CELL","NAME","FREQ_CNT","TOTAL_FREQ_CNT"};
	sql = "select mid3.*,CELL.name FROM (select mid1.*,mid2.total_freq_cnt from "
			+ " (select cluster_id,cell,freq_cnt from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID where rno_ncs_desc_id="
			+ staticNcsId
			+ ") order by cluster_id)mid1 "
			+ " inner join ( select \"CLUSTER_ID\",sum(freq_cnt) as total_freq_cnt from RNO_NCS_CLUSTER_CELL_MID where CLUSTER_ID in (select id from rno_ncs_cluster_MID where rno_ncs_desc_id="
			+ staticNcsId
			+ ") group by CLUSTER_ID )mid2 "
			+ " on (mid1.cluster_id=mid2.cluster_id) "
			+ " )mid3 left join (select label,min(name) as name from cell group by label) cell on mid3.cell=cell.label ";
	t1 = System.currentTimeMillis();
	List<Map<String, Object>> clustCellRes = RnoHelper.commonQuery(stmt,
			sql);
	t2 = System.currentTimeMillis();
	log.debug("汇总分析，获取连通簇内小区,耗时：" + (t2 - t1) + "ms.  sql=" + sql);
	// 4、簇内干扰关系
	String[] interTs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_TITLES;
	// {"主小区","簇编号","簇内小区载波数","干扰小区"};
	String[] interKs = RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET_KEYS;
	sql = "select mid1.*,mid2.total_freq_cnt FROM "
			+ " (SELECT CLUSTER_ID,CELL,NCELL FROM RNO_NCS_CLUSTER_CELL_RELA_MID WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER_mid where rno_ncs_desc_id="
			+ staticNcsId
			+ ")"
			+ " )mid1 INNER JOIN "
			+ " ( SELECT CLUSTER_ID,SUM(FREQ_CNT) as total_freq_cnt FROM RNO_NCS_CLUSTER_CELL_MID WHERE CLUSTER_ID in (select id from RNO_NCS_CLUSTER_mid where rno_ncs_desc_id="
			+ staticNcsId + ") " + " group by cluster_id )mid2 "
			+ " ON(MID1.cluster_id=MID2.cluster_id) ";
	t1 = System.currentTimeMillis();
	List<Map<String, Object>> clustCellRelaRes = RnoHelper.commonQuery(
			stmt, sql);
	t2 = System.currentTimeMillis();
	log.debug("汇总分析，获取簇小区干扰关系,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

	// 自动优化结果
	String[] suggestTs = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_TITLES;
	String[] suggestKs = RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_KEYS;

	sql = "select \"CELL\",\"CELLNAME\",round(EXPECTED_COVER_DIS,5) as \"EXPECTED_COVER_DIS\",round(OVERSHOOTING_FACT,5) as \"OVERSHOOTING_FACT\",\"INTERFER_NCELL_CNT\",\"INTERFER_NCE_CNT_EXINDR\",round(CELL_COVER,5) as \"CELL_COVER\",round(CAPACITY_DESTROY,5) as \"CAPACITY_DESTROY\",\"LONGITUDE\",\"LATITUDE\",round(ANT_HEIGHT,5) as \"ANT_HEIGHT\",round(DOWNTILT,5) as \"DOWNTILT\",round(IDEAL_DOWNTILT,5) as \"IDEAL_DOWNTILT\",\"INDOOR_CELL_TYPE\",\"DECLARE_CHANNEL_NUMBER\",\"AVAILABLE_CHANNEL_NUMBER\",\"CARRIER_NUMBER\",round(RESOURCE_USE_RATE,5) as \"RESOURCE_USE_RATE\",round(TRAFFIC,5) as \"TRAFFIC\",\"PROBLEM_DESC\",\"MESSAGE\",\"STS_TIME\" FROM RNO_NCS_PROBLEM_CELL_MID";
	t1 = System.currentTimeMillis();
	List<Map<String, Object>> optSuggestion = RnoHelper.commonQuery(stmt,
			sql);
	t2 = System.currentTimeMillis();
	log.debug("汇总分析，获取自动优化建议,耗时：" + (t2 - t1) + "ms.  sql=" + sql);

	List<String> dataTypes = new ArrayList<String>();// 数据的分类，
	// 准备输出
	dataTypes.add(RnoConstant.ReportConstant.CELLRES);
	dataTypes.add(RnoConstant.ReportConstant.CLUSTER);
	dataTypes.add(RnoConstant.ReportConstant.CLUSTERCELL);
	dataTypes.add(RnoConstant.ReportConstant.CLUSTERCELLRELA);
	dataTypes.add(RnoConstant.ReportConstant.OPTSUGGESTION);

	titless.add(cellResTitles);
	titless.add(clusterTs);
	titless.add(clusterCellTs);
	titless.add(interTs);
	titless.add(suggestTs);

	keyss.add(cellResKeys);
	keyss.add(clusterKs);
	keyss.add(clusterCellKs);
	keyss.add(interKs);
	keyss.add(suggestKs);

	cellress.add(cellres);
	cellress.add(clustRes);
	cellress.add(clustCellRes);
	cellress.add(clustCellRelaRes);
	cellress.add(optSuggestion);

	sheetNames.add(RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET);
	sheetNames.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_SHEET);
	sheetNames
			.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_SHEET);
	sheetNames
			.add(RnoConstant.ReportConstant.NCS_REPORT_CLUSTER_CELL_RELA_SHEET);

	sheetNames
			.add(RnoConstant.ReportConstant.NCS_REPORT_OPT_SUGGESTION_SHEET);

	// --2014-6-12 gmh comment out---//
	// ByteArrayOutputStream baos = FileTool.putDataOnExcelOutputStream(
	// sheetNames, titless, keyss, cellress);
	// InputStream is = new ByteArrayInputStream(baos.toByteArray());
	// // String cellClusterPath = dir + "ncs_res_" + taskId + ".xls";
	// log.debug("保存簇的数据文件：" + savePath);
	// ok = FileTool.saveInputStream(savePath, is);
	// log.debug("保存簇的数据文件：" + savePath + ",保存结果：" + ok);
	// is.close();
	// baos.close();

	// 准备保存到文本文件，以供程序读取
	log.debug("准备输出供程序读取的分析结果。。。");
	String txtFilePath = savePath
			+ RnoConstant.ReportConstant.NCS_REPORT_FILE_FOR_PROG_READ_SUFFIX;
	List<String[]> fullKeyss = new ArrayList<String[]>(keyss);
	fullKeyss.remove(0);
	fullKeyss.add(0,
			RnoConstant.ReportConstant.NCS_REPORT_CELL_RES_SHEET_KEYS);
	long cnt = FileTool.saveDataInTxtFile(txtFilePath, dataTypes,
			fullKeyss, cellress);
	log.debug("保存记录数量：" + cnt);
	// 保存到excel，供用户下载
	boolean saveres = FileTool.saveDataInExcelFile(savePath, sheetNames,
			titless, keyss, cellress);
	log.debug("保存到了excel文件：" + savePath);
	log.debug("保存结果：" + saveres);
	titless = null;
	keyss = null;
	cellress = null;
	sheetNames = null;

	System.gc();
}

	/**
	 * 
	 * @title 通过codekey->codevalue(BE_INTERFER,NET_STRUCT_FACTOR)获取渲染配置颜色等信息
	 * @param titleToKeys
	 * @param codeKey
	 * @return
	 * @author chao.xj
	 * @date 2014-5-16下午03:47:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getRnoTrafficRendererConfig(
			final String curCodeValue) {
		log.info("进入getRnoTrafficRendererConfig   curCodeValue:" + curCodeValue);
		// final String codeValue=titleToKeys.get(codeKey+"").toString();
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						String sql = "SELECT * from RNO_TRAFFIC_RENDERER_CONFIG WHERE TRAFFIC_TYPE IN(SELECT TRAFFIC_INDEX_ID from RNO_TRAFFIC_INDEX_TYPE WHERE CODE=?)  order by MIN_VALUE ASC";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setString(0, curCodeValue);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> relList = query.list();
						log.info("退出getRnoTrafficRendererConfig relist"
								+ relList);
						return relList;
					}
				});
	}

	/**
	 * 在具有计算频点干扰所需数据的基础上计算频点干扰
	 * 
	 * @param stmt
	 * @author brightming 2014-2-13 下午12:33:54
	 */
	public boolean calculateFreqInterferMatrixResult(Statement stmt,
			String saveIdxPath, String saveDataPath) {
		log.debug("进入方法：calculateFreqInterferMatrixResult。stmt=" + stmt
				+ ",saveIdxPath=" + saveIdxPath + ",saveDataPath="
				+ saveDataPath);
		
		boolean result = true;
		
		if (stmt == null) {
			log.error("calculateFreqInterferMatrixResult未带有效的数据库连接！无法进行计算！");
			return false;
		}

		// 获取所有的小区列表
		String sql = "select distinct(cell) from RNO_2G_NCS_ANA_T";
		ResultSet rs = null;
		List<String> allCells = new ArrayList<String>();
		String cell = null;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cell = rs.getString(1);
				if (cell != null) {
					allCells.add(cell);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("sql执行出错，sql="+sql);
			return false;
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

		File idxFile = new File(saveIdxPath);
		File dataFile = new File(saveDataPath);
		CellInterferDetailReadWriter writeTool = new CellInterferDetailReadWriter(
				idxFile, dataFile);

		// 打开输出流
		result = writeTool.open();
		if(!result) {
			return result;
		}
		// 输出小区总数量
		result = writeTool.writeSize(allCells.size());
		if(!result) {
			return result;
		}
		
		log.debug("获取到中间表：RNO_2G_NCS_ANA_T有：[" + allCells.size() + "]个小区");
		// 获取小区的计算数据进行计算，每次100个小区
		List<String> subCells = null;
		int size = allCells.size();
		int from = 0, to = 0;
		String cid = "";
		
		to = from + 100;
		Map<String, CellInterferMatrixInfo> cellInterferMatrixInfos = new HashMap<String, CellInterferMatrixInfo>();
		//Map<String, CellInterWithDetailInfo> cellInterDetails = new HashMap<String, CellInterWithDetailInfo>();
		
		do {
			cellInterferMatrixInfos.clear();
			cid = "";
			if (to > size) {
				to = size;
			}
			if (from == to) {
				break;
			}
			subCells = allCells.subList(from, to);
			for (int i = 0; i < subCells.size(); i++) {
				cid += "'" + subCells.get(i) + "',";
			}
			cid = cid.substring(0, cid.length() - 1);

//			// 此处计算为in干扰——order by cell，下面需要继续计算out干扰
//			sql = "select CELL,NCELL,CELL_FREQ,NCELL_FREQ,CI,CA,DEFINED_NEIGHBOUR from RNO_NCS_FREQ_META where cell in ( "
//					+ cid + " ) order by cell";
//			log.debug("获取RNO_NCS_FREQ_META表的部分小区的meta信息的sql=" + sql);
//			try {
//				rs = stmt.executeQuery(sql);
//				doCalculateCellsInterfer(rs, InterDir.In, cellInterDetails);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//
//			// // 计算out干扰,ORDER BY NCELL
//			sql = "select NCELL AS CELL,CELL as NCELL,NCELL_FREQ AS CELL_FREQ,CELL_FREQ AS NCELL_FREQ,CI,CA,DEFINED_NEIGHBOUR from RNO_NCS_FREQ_META where cell in ( "
//					+ cid + " ) order by NCELL";
//			log.debug("获取RNO_NCS_FREQ_META表的部分小区的meta信息的sql=" + sql);
//			try {
//				rs = stmt.executeQuery(sql);
//				doCalculateCellsInterfer(rs, InterDir.Out, cellInterDetails);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}

			// --2014-7-31 peng.jm add start -- 干扰矩阵输出格式调整--//
			//查出小区干扰矩阵信息
			sql = "select CELL,NCELL,CI_DIVIDER/CI_DENOMINATOR as CI,CA_DIVIDER/CA_DENOMINATOR as CA,DEFINED_NEIGHBOUR from RNO_2G_NCS_ANA_T where cell in ( "
				+ cid + " ) and CELL<>NCELL order by cell";
			log.debug("获取RNO_2G_NCS_ANA_T表的部分小区的meta信息的sql=" + sql);
			try {
				rs = stmt.executeQuery(sql);
				result = setCellInterDetail(rs, cellInterferMatrixInfos);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("sql执行出错，sql="+sql);
				return false;
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			if(!result) {
				log.error("setCellInterDetail执行出错！");
				return result;
			}
			
			//查出cell影响到的所有小区
			sql = "select t.NCELL as cell, rno_concat_clob(t.CELL) as ncells from RNO_2G_NCS_ANA_T t where t.NCELL in ( "
				+ cid + " ) AND t.CELL<>t.NCELL group by t.NCELL";
			log.debug("获取RNO_2G_NCS_ANA_T表的部分小区的meta信息的sql=" + sql);
			try {
				rs = stmt.executeQuery(sql);
				result = setInterCells(rs, cellInterferMatrixInfos);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("sql执行出错，sql="+sql);
				result = false;
			} finally {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
			if(!result) {
				log.error("setInterCells执行出错！");
				return result;
			}
			
			// 输出
			//writeTool.write(cellInterDetails.values());
			result = writeTool.write(cellInterferMatrixInfos.values());
			if(!result) {
				return result;
			}
			// --2014-7-31 peng.jm add end -- 干扰矩阵输出格式调整--//
			
			from = to;
			to = from + 100;
		} while (true);

		writeTool.close();
		return result;
	}
	
	/**
	 * 加入小区会影响到的所有小区队列
	 * @param rs
	 * @param cellInterferDetails
	 * @return
	 * @author peng.jm
	 * @date 2014-8-2上午09:37:57
	 */
	private boolean setInterCells(ResultSet rs,
			Map<String, CellInterferMatrixInfo> cellInterferMatrixInfos) {
		String cell = "";
		String interCells = "";
		CellInterferMatrixInfo oneCellInter;
		Reader inStream = null;
		java.sql.Clob clob;
		
		try {
			while(rs.next()) {
				cell = rs.getString("CELL");
				clob = rs.getClob("NCELLS");
				inStream = clob.getCharacterStream();
				char[] c = new char[(int) clob.length()];
				try {
					inStream.read(c);
					//data是读出并需要返回的数据，类型是String
					interCells = new String(c);
					inStream.close();
				} catch (IOException e) {
					try {
						inStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				
				if(cell==null || StringUtils.isEmpty(cell)){
					log.error("计算干扰矩阵时，cell为空！");
					continue;
				}
//				if(interCells==null || StringUtils.isEmpty(interCells)){
//					log.error("计算干扰矩阵时，interCells为空！");
//					continue;
//				}
				oneCellInter = cellInterferMatrixInfos.get(cell);
				if (oneCellInter == null) {
					continue;
				} else {
					oneCellInter.setInterCells(interCells);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 初始化小区与邻区的干扰信息对象列
	 * @param rs
	 * @param existCellInterDetails
	 *        已经存在的小区干扰队列数据。
	 *        对于某个小区，以此来找，找不到，就新建一个，并加入到此对立，找到了，则直接使用该队列的数据，并完善它
	 * @return
	 * @author peng.jm
	 * @date 2014-7-31下午06:03:29
	 */
	private boolean setCellInterDetail(ResultSet rs,
			Map<String, CellInterferMatrixInfo> existCellInterferMatrixInfos) {
		
		String cell = "", ncell = "", isNeigh = "";
		double ci, ca;

		CellInterferMatrixInfo oneCellInter;
		List<InterferDetail> interferDetails;
		InterferDetail interferDetail;
		
		try {

			while (rs.next()) {
				cell = rs.getString(1);
				ncell = rs.getString(2);
				ci = rs.getDouble(3);
				ca = rs.getDouble(4);
				isNeigh = rs.getString(5);
					
				if(cell==null || StringUtils.isEmpty(cell)){
					log.error("计算干扰矩阵时，cell为空！");
					continue;
				}
				if(ncell==null || StringUtils.isEmpty(ncell)){
					log.error("计算干扰矩阵时，cell="+cell+"的邻区为空！");
					continue;
				}
				if(isNeigh==null || StringUtils.isEmpty(isNeigh)){
					isNeigh="0";
					continue;
				}
				oneCellInter = existCellInterferMatrixInfos.get(cell);
				if (oneCellInter == null) {
					oneCellInter = new CellInterferMatrixInfo(cell);
					existCellInterferMatrixInfos.put(cell, oneCellInter);
				}
				
				interferDetails = oneCellInter.getInterDetails();
				interferDetail = new InterferDetail();
				interferDetail.setNcellName(ncell);
				interferDetail.setIsNeighbour(isNeigh);
				interferDetail.setCi(ci);
				interferDetail.setCa(ca);
				interferDetails.add(interferDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 计算一批次的小区的干扰情况
	 * 
	 * @param rs
	 *            包含数据源的数据集合 使用完以后，不需要关闭，由调用方进行提供、关闭 包含固定的7列： 1列：cell名称 =
	 *            rs.getString(1); 2列：ncell名称 = rs.getString(2);
	 *            3列：逗号分隔的小区频点cellFreq = rs.getString(3); 4列：逗号分隔的邻区频点ncellFreq
	 *            = rs.getString(4); 5列：ci值 = rs.getDouble(5); 6列：ca值 =
	 *            rs.getDouble(6); 7列：是否有定义邻区关系isNeigh = rs.getString(7);
	 * @param inOrOutType
	 *            干扰方向
	 * @param existCellInterDetails
	 *            已经存在的小区干扰队列数据。
	 *            对于某个小区，以此来找，找不到，就新建一个，并加入到此对立，找到了，则直接使用该队列的数据，并完善它
	 * @return
	 * @author brightming 2014-6-17 下午3:43:51
	 */
//	private boolean doCalculateCellsInterfer(ResultSet rs,
//			InterDir inOrOutType,
//			Map<String, CellInterWithDetailInfo> existCellInterDetails) {
//
//		String cell = "", ncell = "", cellFreq = "", ncellFreq = "", isNeigh = "";
//		double ci, ca;
//		List<String> ncells;
//		List<String> ncellFreqs;
//		List<String> isNeighbours;
//		List<Double> cis, cas;
//		String preCell;
//		CellInterWithDetailInfo oneCellInter;
//		List<CellInterWithDetailInfo> cellsInterInfos = new ArrayList<CellInterWithDetailInfo>();
//		try {
//			// rs = stmt.executeQuery(sql);
//			preCell = null;
//			cell = "";
//			ncells = new ArrayList<String>();
//			ncellFreqs = new ArrayList<String>();
//			cis = new ArrayList<Double>();
//			cas = new ArrayList<Double>();
//			isNeighbours = new ArrayList<String>();
//			while (rs.next()) {
//				cell = rs.getString(1);
//				ncell = rs.getString(2);
//				cellFreq = rs.getString(3);
//				ncellFreq = rs.getString(4);
//				ci = rs.getDouble(5);
//				ca = rs.getDouble(6);
//				isNeigh = rs.getString(7);
//
//				if (preCell == null || preCell.equals(cell)) {
//					ncells.add(ncell);
//					ncellFreqs.add(ncellFreq);
//					cis.add(ci);
//					cas.add(ca);
//					isNeighbours.add(isNeigh);
//					preCell = cell;
//				} else {
//					// 对于一个cell进行计算——计算in 干扰
//					oneCellInter = existCellInterDetails.get(cell);
//					if (oneCellInter == null) {
//						oneCellInter = new CellInterWithDetailInfo(cell);
//						existCellInterDetails.put(cell, oneCellInter);
//					}
//					calculateCellFreqResWithEveryDetail(cell, cellFreq, ncells,
//							ncellFreqs, cis, cas, isNeighbours, inOrOutType,
//							oneCellInter);
//
//					// 重新初始化容器
//					ncells.clear();
//					ncellFreqs.clear();
//					cis.clear();
//					cas.clear();
//					isNeighbours.clear();
//
//					ncells.add(ncell);
//					ncellFreqs.add(ncellFreq);
//					cis.add(ci);
//					cas.add(ca);
//					isNeighbours.add(isNeigh);
//					preCell = cell;
//				}
//
//			}
//			// 最后一批次的处理
//			oneCellInter = existCellInterDetails.get(cell);
//			if (oneCellInter == null) {
//				oneCellInter = new CellInterWithDetailInfo(cell);
//				existCellInterDetails.put(cell, oneCellInter);
//			}
//			calculateCellFreqResWithEveryDetail(cell, cellFreq, ncells,
//					ncellFreqs, cis, cas, isNeighbours, inOrOutType,
//					oneCellInter);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		} finally {
//		}
//
//		return true;
//	}

	/**
	 * 详细计算指定的小区的频点的干扰情况
	 * 
	 * @param cell
	 *            主小区名称
	 * @param cellFreq
	 *            小区的频点，以逗号分隔
	 * @param ncells
	 *            有相关的邻区（未必有定义相邻关系）
	 * @param ncellFreqs
	 *            邻区的频点，以逗号分隔
	 * @param cis
	 *            主小区与邻区的ci值
	 * @param cas
	 *            主小区与邻区的ca值
	 * @param isNeighbours
	 *            主小区与邻区是否有定义邻区关系
	 * @param inOrOutType
	 *            干扰的方向：in表示主小区受到邻区的干扰，out表示主小区对邻区产生干扰
	 * @return
	 * @author brightming 2014-6-16 下午5:08:43
	 */
	private boolean calculateCellFreqResWithEveryDetail(String cell,
			String cellFreq, String cellBcch, String cellSite, List<String> ncells, 
			List<String> ncellNames, List<String> ncellSites, List<String> ncellFreqs,
			List<Double> cis, List<Double> cas, List<String> isNeighbours,
			InterDir inOrOutType, CellInterWithDetailInfo cellInterInfo) {
		if (cell == null || "".equals(cell.trim()) || cellFreq == null
				|| "".equals(cellFreq.trim()) || ncellFreqs == null
				|| ncellFreqs.isEmpty() || cis == null || cis.isEmpty()
				|| cas == null || cas.isEmpty()) {
			log.error("calculateCellFreqResWithEveryDetail参数有误！cell=" + cell
					+ ",cellFreq=" + cellFreq + ",ncellFreqs=" + ncellFreqs
					+ ",cis=" + cis + ",cas=" + cas + ",isNeighbours="
					+ isNeighbours);
			return false;
		}
		if (ncellFreqs.size() != cis.size() || ncellFreqs.size() != cas.size()
				|| ncellFreqs.size() != isNeighbours.size()) {
			log.error("传递的参数数量不同！ncellFreqs.size=" + ncellFreqs.size()
					+ ",cis.size=" + cis.size() + ",cas.size=" + cas.size()
					+ ",isNeighbours.size=" + isNeighbours.size());
			return false;
		}
		long t1 = System.currentTimeMillis(), t2;
		int start_900 = 1, end_900 = 94;
		int start_1800 = 512, end_1800 = 635;
		int start = 1, end = 1;

		String[] cfs = cellFreq.split(",");
		try {
			int freq = Integer.parseInt(cfs[0]);
			if (freq < 100) {
				start = start_900;
				end = end_900;
			} else {
				start = start_1800;
				end = end_1800;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		int freqCnt = end - start + 1;
		int[] cfsint = new int[freqCnt];
		String[] cfsType = new String[freqCnt];// 频点类型：bcch，tch还是都不是
		double[] cfsci = new double[freqCnt];// 对应频点的ci累计值
		double[] cfsca = new double[freqCnt];// 对应频点的ca累计值
		// 固定，假设小区有这么多频点
		for (int i = 0; i < freqCnt; i++) {
			cfsint[i] = start + i;// Integer.parseInt(cfs[i]);
			cfsci[i] = 0;
			cfsca[i] = 0;
			// 判断类型
			for (String cf : cfs) {
				// 如果这些待判断的频点在小区的频点列表里
				if ((cfsint[i] + "").equals(cf)) {
					cfsType[i] = "tch";
					break;
				}
			}
			if((cfsint[i] + "").equals(cellBcch)) {
				cfsType[i] = "bcch";
			}
		}
		// 对cfsint排序
		int size = ncellFreqs.size();// 邻区数量
		String ncellFreq = "";
		double ci = 0, ca = 0;
		String isNei;
		FreqInterInfo freqInterInfo;
		InterDetail interDetail;
		CiCaType cicaType;

		Map<Integer, FreqInterInfo> cacheFreqInterInfos = new HashMap<Integer, FreqInterInfo>();
		//-- peng.jm add 2014年8月5日15:05:58
		List<FreqInterInfo> fiis = cellInterInfo.getFreqInterInfos();
		if(fiis.size() > 0) {
			for (FreqInterInfo fii : fiis) {
				cacheFreqInterInfos.put(fii.getFreq(), fii);
			}
		}

		double val = 0f;
		int ncellIdx = -1;
		List<CellAbst> cellAbsts = cellInterInfo.getCellAbsts();
		String isSameSite = "";
		String ncellSite = "";
		for (int i = 0; i < size; i++) {
			// 对于所有邻区都要比较
			ncellFreq = ncellFreqs.get(i);
			//@author chao.xj  2014-7-24 下午1:37:20
			if (ncellFreq==null) {
				continue;
			}
			String[] nfs = ncellFreq.split(",");

			ci = cis.get(i);
			ca = cas.get(i);
			isNei = isNeighbours.get(i);
			ncellSite = ncellSites.get(i);
			
			// 判断邻区是否存在该小区的邻区信息中
			ncellIdx = cellInterInfo.getCellAbstIdx(ncells.get(i));
			if (ncellIdx == -1) {
				if(ncellSite.equals(cellSite)) {
					isSameSite = "YES";
				} else {
					isSameSite = "NO";
				}
				cellAbsts.add(new CellAbst(ncells.get(i), ncellNames.get(i),
						isSameSite, isNei));
				ncellIdx = cellAbsts.size() - 1;
			}
			// 每个频点都要进行和每个邻频点进行比较
			for (int j = 0; j < freqCnt; j++) {
				// 每一个频点都要构造
				// 由于是以邻区为主循环，所以，这里的频点可能会已经处理过
				if (cacheFreqInterInfos.containsKey(cfsint[j])) {
					freqInterInfo = cacheFreqInterInfos.get(cfsint[j]);
				} else {
					freqInterInfo = new FreqInterInfo(cfsint[j], cfsType[j]);
					cacheFreqInterInfos.put(cfsint[j], freqInterInfo);
				}
				// 判断邻区的频点
				for (int k = 0; k < nfs.length; k++) {// 邻小区的全部频点
					cicaType = null;
					if ((cfsint[j] + "").equals(nfs[k])) {
						// 同频干扰系数
						cfsci[j] += ci;
						cicaType = CiCaType.Ci;
						val = ci;

					} else if (((cfsint[j] + 1) + "").equals(nfs[k])) {
						// 邻频干扰系数，上邻频
						cfsca[j] += ca;
						cicaType = CiCaType.UpCa;
						val = ca;
					} else if (((cfsint[j] - 1) + "").equals(nfs[k])) {
						// 下邻频
						cicaType = CiCaType.LowCa;
						val = ca;
					}
					if (cicaType != null && val > 0) {
						log.debug("freq:" + cfsint[j] + " of cell:" + cell
								+ " has relation:" + cicaType.getName()
								+ " with ncell:" + ncells.get(i));
						try {
							interDetail = new InterDetail(cicaType.getName(),
									val, ncellIdx, Integer.parseInt(nfs[k]));
							if (InterDir.In == inOrOutType) {
								freqInterInfo.addInTypeInterDetail(interDetail);
							} else {
								freqInterInfo
										.addOutTypeInterDetail(interDetail);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
		
		List<FreqInterInfo> freqInterInfos = cellInterInfo.getFreqInterInfos();
		
		for (FreqInterInfo fii : cacheFreqInterInfos.values()) {
			// 如果该频点确实有干扰，才加入，否则不加入，以节省空间
			//if (fii.getInTotal() > 0 || fii.getOutTotal() > 0) {
				//判断是否有重复频点
				for (int k = 0; k < freqInterInfos.size(); k++) {
					if(freqInterInfos.get(k).getFreq() == fii.getFreq()){
						freqInterInfos.remove(k);
					}
				}
				cellInterInfo.addFreqInterInfo(fii);
			//}
		}
		t2 = System.currentTimeMillis();
		log.debug("calculateCellFreqResWithEveryDetail耗时：" + (t2 - t1) + "ms");
		return true;
	}

	

	/**
	 * 对应的邻区干扰信息详情对象
	 * @author peng.jm
	 * @date 2014-7-31 下午04:41:16
	 */
	public static class InterferDetail {
		String ncellName;
		String isNeighbour;
		Double ci;
		Double ca;
		public String getNcellName() {
			return ncellName;
		}
		public void setNcellName(String ncellName) {
			this.ncellName = ncellName;
		}
		public String getIsNeighbour() {
			return isNeighbour;
		}
		public void setIsNeighbour(String isNeighbour) {
			this.isNeighbour = isNeighbour;
		}
		public Double getCi() {
			return ci;
		}
		public void setCi(Double ci) {
			this.ci = ci;
		}
		public Double getCa() {
			return ca;
		}
		public void setCa(Double ca) {
			this.ca = ca;
		}

		@Override
		public String toString() {
			return "InterferDetail [ncellName=" + ncellName
					+", isNeighbour=" + isNeighbour + ", ci=" + ci + ", ca="
					+ ca + "]";
		}
	}
	
	/**
	 * 主小区与邻区的干扰矩阵对象
	 * @author peng.jm
	 * @date 2014-7-31 下午04:37:46
	 */
	public static class CellInterferMatrixInfo {

		String cellName;
		//idx
		String interCells = "";

		//data
		int ncellNum;
		List<InterferDetail> interDetails = new ArrayList<InterferDetail>();
		
		public CellInterferMatrixInfo(String preCell) {
			this.cellName = preCell;
		}
		
		public String getCellName() {
			return cellName;
		}
		
		public int getNcellNum() {
			return ncellNum;
		}
		public void setNcellNum(int ncellNum) {
			this.ncellNum = ncellNum;
		}
		public String getInterCells() {
			return interCells;
		}
		public void setInterCells(String interCells) {
			this.interCells = interCells;
		}

		public List<InterferDetail> getInterDetails() {
			return interDetails;
		}

		public void setInterDetails(List<InterferDetail> interDetails) {
			this.interDetails = interDetails;
		}

		public void addInterDetails(List<InterferDetail> interList) {
			interDetails.addAll(interList);
		}
		
		public void addInterDetail(InterferDetail interDetail) {
			interDetails.add(interDetail);
		}
		
		public boolean write(DataOutputStream dataWriter){
		    boolean result = true;
			try {
				// 1、 输出cellname
				dataWriter.writeUTF(this.getCellName());
				// 2、 输出相关的邻小区的数量
				List<InterferDetail> interDetails = this.getInterDetails();
				dataWriter.writeInt(interDetails.size());
				// 3、 循环输出相关邻区的信息
				if (interDetails.size() > 0) {
					for (int k = 0; k < interDetails.size(); k++) {
						// 干扰邻区名
						dataWriter.writeUTF(interDetails.get(k).getNcellName());
						//是否邻区
						dataWriter.writeUTF(interDetails.get(k).getIsNeighbour());
						// CI
						dataWriter.writeDouble(interDetails.get(k).getCi());
						// CA
						dataWriter.writeDouble(interDetails.get(k).getCa());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
			return result;
		}

		/**
		 * 对象自带读取方法
		 * @param dataDis 输入流
		 * @param interCells 被影响的小区
		 * @return 返回一个对象，里面保存有与影响小区对应的CI,CA值
		 * @throws IOException
		 * @author peng.jm
		 * @date 2014-8-4下午03:00:08
		 */
		public static CellInterferMatrixInfo read(DataInputStream dataDis,
				String interCells) throws IOException {
			
			// 1、 读取cellname
			String cellName = dataDis.readUTF();
			CellInterferMatrixInfo cimi = new CellInterferMatrixInfo(cellName);
			// 2、 读取相关的邻小区的数量
			int ncellNum = dataDis.readInt();
			cimi.setNcellNum(ncellNum);		
			// 3、 循环读取相关邻区的信息
			String ncell;
			String isNeighbour;
			double ci, ca;
			List<InterferDetail> interDetails = new ArrayList<InterferDetail>();
			InterferDetail one = null;
			if (ncellNum > 0) {
				for (int k = 0; k < ncellNum; k++) {
					// 干扰邻区名
					ncell = dataDis.readUTF();
					// 是否邻区
					isNeighbour = dataDis.readUTF();
					// CI
					ci = dataDis.readDouble();
					// CA
					ca = dataDis.readDouble();
					one = new InterferDetail();
					one.setNcellName(ncell);
					one.setIsNeighbour(isNeighbour);
					one.setCi(ci);
					one.setCa(ca);
					interDetails.add(one);
				}
			}
			cimi.addInterDetails(interDetails);
			//保存被影响的小区
			cimi.setInterCells(interCells);
			
			return cimi;
		}
		
		public static CellInterferMatrixInfo read(RandomAccessFile raf,
				String interCells) throws IOException {
			// 1、 读取cellname
			String cellName = raf.readUTF();
			CellInterferMatrixInfo cimi = new CellInterferMatrixInfo(cellName);
			// 2、 读取相关的邻小区的数量
			int ncellNum = raf.readInt();
			cimi.setNcellNum(ncellNum);		
			// 3、 循环读取相关邻区的信息
			String ncell;
			String isNeighbour;
			double ci, ca;
			List<InterferDetail> interDetails = new ArrayList<InterferDetail>();
			InterferDetail one = null;
			if (ncellNum > 0) {
				for (int k = 0; k < ncellNum; k++) {
					// 干扰邻区名
					ncell = raf.readUTF();
					// 是否邻区
					isNeighbour = raf.readUTF();
					// CI
					ci = raf.readDouble();
					// CA
					ca = raf.readDouble();
					one = new InterferDetail();
					one.setNcellName(ncell);
					one.setIsNeighbour(isNeighbour);
					one.setCi(ci);
					one.setCa(ca);
					interDetails.add(one);
				}
			}
			cimi.addInterDetails(interDetails);
			//保存被影响的小区
			cimi.setInterCells(interCells);
			
			return cimi;
		}

		@Override
		public String toString() {
			return "CellInterferMatrixInfo [cellName=" + cellName
					+ ", ncellNum=" + ncellNum + ", interDetails="
					+ interDetails + "]";
		}
	}
	
	/**
	 * 获取指定小区的干扰矩阵数据
	 * 
	 * @param dir
	 * @param cellName
	 * @return
	 * @author brightming 2014-6-18 下午5:37:57
	 */
	public CellInterWithDetailInfo getCellInterDetailInfo(String dir,
			String cellName) {
		String idxFilePath = dir
				+ RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
		String dataFilePath = dir
				+ RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;
		
		//idxFilePath = "hdfs:///rno_data/matrix/ncs_res_99.interferMatrix.Idx";
		//dataFilePath = "hdfs:///rno_data/matrix/ncs_res_99.interferMatrix.Data";
		
		// --2014-8-2 peng.jm add start -- 干扰矩阵输出格式调整--//
		
		//初始化变量
		List<Map<String, Object>> cellObjs = new ArrayList<Map<String,Object>>();
		String cellFreq = "";
		String cellBcch = "";
		String cellSite = "";
		
		List<String> ncells = new ArrayList<String>();
		List<String> ncellNames = new ArrayList<String>();
		List<String> ncellFreqs = new ArrayList<String>();
		List<String> ncellSites = new ArrayList<String>();
		List<Double> cis = new ArrayList<Double>();
		List<Double> cas = new ArrayList<Double>();
		List<String> isNeighbours = new ArrayList<String>();
		List<Map<String, Object>> ncellObjs = new ArrayList<Map<String,Object>>();
		String ncellStrs = "";
		
		//获取主小区bcch与tch
		cellObjs = queryCellsDetailsByCellNames("'" + cellName + "'");
		if(cellObjs !=null && cellObjs.size() > 0) {
			if(cellObjs.get(0).get("BCCH") != null) {
				cellBcch = cellObjs.get(0).get("BCCH").toString();
			}
			if(cellObjs.get(0).get("TCH") != null) {
				cellFreq = cellObjs.get(0).get("TCH").toString();
			}
			if(cellObjs.get(0).get("SITE") != null) {
				cellSite = cellObjs.get(0).get("SITE").toString();
			}
		}
		
		CellInterWithDetailInfo cellInterInfo = new CellInterWithDetailInfo(cellName, cellBcch, cellFreq);
		
		//计算in
		CellInterferMatrixInfo cellInterMatrixIn = CellInterferDetailReadWriter
				.readDirIn(idxFilePath, dataFilePath, cellName);
		if(cellInterMatrixIn != null) {
			//System.out.println("cellInterMatrixIn="+cellInterMatrixIn);
			//获取ncells，cis，cas，isNeighbours
			List<InterferDetail> interDetailsIn = cellInterMatrixIn.getInterDetails();
			for (InterferDetail one : interDetailsIn) {
				if(one.getNcellName().indexOf("NotF")==0) {
					continue;
				}
				ncells.add(one.getNcellName());
				ncellStrs += "'"+one.getNcellName() + "',";
				cis.add(one.getCi());
				cas.add(one.getCa());
				isNeighbours.add(one.getIsNeighbour());
			}
			//获取所有ncell的小区详情
			if(!(("").equals(ncellStrs)) && ncellStrs != null) {
				ncellStrs = ncellStrs.substring(0, ncellStrs.length()-1); //去最后一个逗号
				ncellObjs = queryCellsDetailsByCellNames(ncellStrs);
			}
			if(ncellObjs != null && ncellObjs.size() > 0) {
				//循环匹配出相应信息
				for (String ncell : ncells) {
					for (Map<String, Object> one : ncellObjs) {
						if(ncell.equals(one.get("LABEL").toString())) {
							//获取ncell描述名
							if(one.get("NAME") != null) {
								ncellNames.add(one.get("NAME").toString());
							} else {
								ncellNames.add("");
							}
							//获取ncell的freq
							if(one.get("TCH") != null) {
								ncellFreqs.add(one.get("TCH").toString());
							} else {
								ncellFreqs.add("");
							}
							//获取ncell的site
							if(one.get("SITE") != null) {
								ncellSites.add(one.get("SITE").toString());
							} else {
								ncellSites.add("");
							}
						}
					}
				}
			}
			calculateCellFreqResWithEveryDetail(cellName, cellFreq, cellBcch, cellSite,
					ncells, ncellNames, ncellSites, ncellFreqs, cis, cas, isNeighbours,
					InterDir.In, cellInterInfo);
		}

		//clear
		ncells.removeAll(ncells);
		ncellNames.removeAll(ncellNames);
		ncellSites.removeAll(ncellSites);
		ncellFreqs.removeAll(ncellFreqs);
		cis.removeAll(cis);
		cas.removeAll(cas);
		isNeighbours.removeAll(isNeighbours);
		ncellStrs = "";
		
		//计算out
		CellInterferMatrixInfo cellInterMatrixOut = CellInterferDetailReadWriter
			.readDirOut(idxFilePath, dataFilePath, cellName);
		if(cellInterMatrixOut != null) {
			//获取ncells，cis，cas，isNeighbours
			List<InterferDetail> interDetailsOut = cellInterMatrixOut.getInterDetails();
			for (InterferDetail one : interDetailsOut) {
				if(one.getNcellName().indexOf("NotF")==0) {
					continue;
				}
				ncells.add(one.getNcellName());
				ncellStrs += "'"+one.getNcellName() + "',";
				cis.add(one.getCi());
				cas.add(one.getCa());
				isNeighbours.add(one.getIsNeighbour());
			}
			//获取所有ncell的小区详情
			if(!(("").equals(ncellStrs)) && ncellStrs != null) {
				ncellStrs = ncellStrs.substring(0, ncellStrs.length()-1); //去最后一个逗号
				ncellObjs = queryCellsDetailsByCellNames(ncellStrs);
			}
			if(ncellObjs != null && ncellObjs.size() > 0) {
				//循环匹配出相应信息
				for (String ncell : ncells) {
					for (Map<String, Object> one : ncellObjs) {
						if(ncell.equals(one.get("LABEL").toString())) {
							//获取ncell描述名
							if(one.get("NAME") != null) {
								ncellNames.add(one.get("NAME").toString());
							} else {
								ncellNames.add("");
							}
							//获取ncell的freq
							if(one.get("TCH") != null) {
								ncellFreqs.add(one.get("TCH").toString());
							} else {
								ncellFreqs.add("");
							}
							//获取ncell的site
							if(one.get("SITE") != null) {
								ncellSites.add(one.get("SITE").toString());
							} else {
								ncellSites.add("");
							}
						}
					}
				}
			}
					
			calculateCellFreqResWithEveryDetail(cellName, cellFreq, cellBcch, cellSite,
					ncells, ncellNames, ncellSites, ncellFreqs, cis, cas, isNeighbours,
					InterDir.Out, cellInterInfo);
		}
		//System.out.println(cellInterInfo.getFreqInterInfos());
		if(cellInterMatrixIn == null && cellInterMatrixOut == null) {
			return null;
		} else {
			return cellInterInfo;
		}
		// --2014-8-2 peng.jm add end -- 干扰矩阵输出格式调整--//
		
//		return CellInterDetailReadWriter.read(idxFilePath, dataFilePath,
//				cellName);
	}
	
	/**
	 * 通过cellNames获取所包含的所有小区详情
	 * @param cellStrs
	 * @return
	 * @author peng.jm
	 * @date 2014-8-12下午04:25:07
	 */
	private List<Map<String, Object>> queryCellsDetailsByCellNames(String cellStrs) {

		String fields = "BCCH,TCH,SITE,NAME, ";
		
		final String sql = "select * from " 
						+ " (select * from (select distinct (label) as label, " + fields 
						+ " row_number() over(partition by label order by label) as seq from cell) " 
						+ "  where seq=1) t where t.label in (" + cellStrs + ")";
		
		log.debug("方法queryCellsDetailsByCellNames的 sql="+sql);
		return hibernateTemplate.execute(new HibernateCallback<List<Map<String, Object>>>() {

			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	

	public static class InterDetail {
		String ciOrCa;// ca,ci
		double value;
		// String ncell;
		// String isNei;
		int ncellIdx;
		int affectFreq;

		public InterDetail() {
		}

		public InterDetail(String ciOrCa, double value, int ncellIdx,
				int affectFreq) {
			this.ciOrCa = ciOrCa;
			this.value = value;
			this.ncellIdx = ncellIdx;
			this.affectFreq = affectFreq;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public int getNcellIdx() {
			return ncellIdx;
		}

		public void setNcellIdx(int ncellIdx) {
			this.ncellIdx = ncellIdx;
		}

		public int getAffectFreq() {
			return affectFreq;
		}

		public void setAffectFreq(int affectFreq) {
			this.affectFreq = affectFreq;
		}

		public String getCiOrCa() {
			return ciOrCa;
		}

		public void setCiOrCa(String ciOrCa) {
			this.ciOrCa = ciOrCa;
		}

		@Override
		public String toString() {
			return "InterDetail [ciOrCa=" + ciOrCa + ", value=" + value
					+ ", ncellIdx=" + ncellIdx + ", affectFreq=" + affectFreq
					+ "]";
		}

		public void write(DataOutputStream dataWriter) throws IOException {
			// 输出干扰频点
			dataWriter.writeInt(this.getAffectFreq());
			// 输出干扰邻区的索引
			dataWriter.writeInt(this.getNcellIdx());
			// 输出干扰类型
			dataWriter.writeUTF(this.getCiOrCa());
			// 输出干扰值
			dataWriter.writeDouble(this.getValue());
		}

		public static InterDetail read(DataInputStream dataReader)
				throws IOException {
			InterDetail idt = new InterDetail();
			// 读取干扰频点
			idt.setAffectFreq(dataReader.readInt());
			// 读取干扰邻区的索引
			idt.setNcellIdx(dataReader.readInt());
			// 读取干扰类型
			idt.setCiOrCa(dataReader.readUTF());
			// 读取干扰值
			idt.setValue(dataReader.readDouble());
			return idt;
		}

	}

	public static class FreqInterInfo {
		int freq;
		String freqType;// 该频点是否是指定小区的bcch，tch，如果都不是，则空白
		List<InterDetail> in = new ArrayList<InterDetail>();
		List<InterDetail> out = new ArrayList<InterDetail>();;
		double inTotal;
		double outTotal;

		public FreqInterInfo() {

		}

		public FreqInterInfo(int freq, String freqType) {
			this.freq = freq;
			this.freqType = freqType == null ? "" : freqType;
		}

		public void addInTypeInterDetail(InterDetail id) {
			if (id != null) {
				in.add(id);
				inTotal += id.getValue();
			}
		}

		public void addOutTypeInterDetail(InterDetail id) {
			if (id != null) {
				out.add(id);
				outTotal += id.getValue();
			}
		}

		public int getFreq() {
			return freq;
		}

		public void setFreq(int freq) {
			this.freq = freq;
		}

		public List<InterDetail> getIn() {
			return in;
		}

		public void setIn(List<InterDetail> in) {
			this.in = in;
		}

		public List<InterDetail> getOut() {
			return out;
		}

		public void setOut(List<InterDetail> out) {
			this.out = out;
		}

		public double getInTotal() {
			return inTotal;
		}

		public void setInTotal(double inTotal) {
			this.inTotal = inTotal;
		}

		public double getOutTotal() {
			return outTotal;
		}

		public void setOutTotal(double outTotal) {
			this.outTotal = outTotal;
		}

		public String getFreqType() {
			return freqType;
		}

		public void setFreqType(String freqType) {
			this.freqType = freqType;
		}

		@Override
		public String toString() {
			if (in.isEmpty() && out.isEmpty()) {
				return "";
			} else
				return "FreqInterInfo [freq=" + freq + ", freqType=" + freqType
						+ ", in=" + in + ", out=" + out + ", inTotal="
						+ inTotal + ", outTotal=" + outTotal + "]";
		}

		public void write(DataOutputStream dataWriter) throws IOException {
			// 输出频点
			dataWriter.writeInt(this.getFreq());
			// 输出频点类型
			dataWriter.writeUTF(this.getFreqType());
			// 输出in干扰值
			dataWriter.writeDouble(this.getInTotal());
			// 输出out干扰值
			dataWriter.writeDouble(this.getOutTotal());
			// 输出in干扰数量
			dataWriter.writeInt(in.size());
			// 循环输出in干扰详情
			if (in.size() > 0) {
				for (int j = 0; j < in.size(); j++) {
					in.get(j).write(dataWriter);
				}
			}

			// 输出out干扰数量
			dataWriter.writeInt(out.size());
			// 循环输出out干扰详情
			if (out.size() > 0) {
				for (int j = 0; j < out.size(); j++) {
					out.get(j).write(dataWriter);
				}
			}
		}

		public static FreqInterInfo read(DataInputStream dataReader)
				throws IOException {

			FreqInterInfo fii = new FreqInterInfo();

			// 读取频点
			fii.setFreq(dataReader.readInt());
			// 读取频点类型
			fii.setFreqType(dataReader.readUTF());
			// 读取in干扰值
			fii.setInTotal(dataReader.readDouble());
			// 读取out干扰值
			fii.setOutTotal(dataReader.readDouble());
			// 读取in干扰数量
			int inSize = dataReader.readInt();
			// 循环读取in干扰详情
			if (inSize > 0) {
				for (int j = 0; j < inSize; j++) {
					fii.addInTypeInterDetail(InterDetail.read(dataReader));
				}
			}

			// 读取out干扰数量
			int outSize = dataReader.readInt();
			// 循环读取out干扰详情
			if (outSize > 0) {
				for (int j = 0; j < outSize; j++) {
					fii.addOutTypeInterDetail(InterDetail.read(dataReader));
				}
			}
			return fii;
		}
	}

	public static class CellAbst {
		String cellName;
		String cellDescName;
		String isSameSite;
		String isNei;

		public CellAbst() {
		}

		public CellAbst(String cn, String iN) {
			this.cellName = cn;
			this.isNei = iN;
		}
		
		public CellAbst(String cn, String cdn, String iss, String iN) {
			this.cellName = cn;
			this.cellDescName = cdn;
			this.isSameSite = iss;
			this.isNei = iN;
		}

		public String getCellName() {
			return cellName;
		}

		public void setCellName(String cellName) {
			this.cellName = cellName;
		}

		public String getIsNei() {
			return isNei;
		}

		public void setIsNei(String isNei) {
			this.isNei = isNei;
		}

		public String getCellDescName() {
			return cellDescName;
		}

		public void setCellDescName(String cellDescName) {
			this.cellDescName = cellDescName;
		}

		public String getIsSameSite() {
			return isSameSite;
		}

		public void setIsSameSite(String isSameSite) {
			this.isSameSite = isSameSite;
		}

		@Override
		public String toString() {
			return "CellAbst [cellName=" + cellName + ", isNei=" + isNei
					+ ", cellDescName=" + cellDescName + ", isSameSite=" + isSameSite +"]";
		}

	}
	

	
	/**
	 * 带有详细干扰数值，频点、方向等小区干扰对象
	 * 
	 * @author brightming
	 * 
	 */
	public static class CellInterWithDetailInfo {
		String cellName;
		String cellBcch;
		String cellFreq;

		List<FreqInterInfo> freqInterInfos = new ArrayList<FreqInterInfo>();

		List<CellAbst> cellAbsts = new ArrayList<CellAbst>();

		public CellInterWithDetailInfo() {
		}

		public CellInterWithDetailInfo(String cellName) {
			this.cellName = cellName;
		}
		public CellInterWithDetailInfo(String cellName, String cellBcch, String cellFreq) {
			this.cellName = cellName;
			this.cellBcch = cellBcch;
			this.cellFreq = cellFreq;
		}
		
		public String getCellName() {
			return cellName;
		}

		public void setCellName(String cellName) {
			this.cellName = cellName;
		}

		public String getCellBcch() {
			return cellBcch;
		}

		public void setCellBcch(String cellBcch) {
			this.cellBcch = cellBcch;
		}

		public String getCellFreq() {
			return cellFreq;
		}

		public void setCellFreq(String cellFreq) {
			this.cellFreq = cellFreq;
		}

		public List<FreqInterInfo> getFreqInterInfos() {
			return freqInterInfos;
		}

		public void setFreqInterInfos(List<FreqInterInfo> freqInterInfos) {
			this.freqInterInfos = freqInterInfos;
		}

		public void addFreqInterInfo(FreqInterInfo fii) {
			this.freqInterInfos.add(fii);
		}

		public List<CellAbst> getCellAbsts() {
			return cellAbsts;
		}

		public void setCellAbsts(List<CellAbst> cellAbsts) {
			this.cellAbsts = cellAbsts;
		}

		public void addCellAbst(CellAbst cellAbst) {
			cellAbsts.add(cellAbst);
		}

		public int getCellAbstIdx(String cell) {
			for (int i = 0; i < cellAbsts.size(); i++) {
				if (cellAbsts.get(i).getCellName().equals(cell)) {
					return i;
				}
			}
			return -1;
		}

		@Override
		public String toString() {
			return "CellInterWithDetailInfo [cellAbsts=" + cellAbsts
					+ ", cellName=" + cellName + ", freqInterInfos="
					+ freqInterInfos + "]";
		}

		public void write(DataOutputStream dataWriter) throws IOException {
			// 1、 输出cellname
			dataWriter.writeUTF(this.getCellName());
			// 2、 输出相关的邻小区的数量
			dataWriter.writeInt(this.getCellAbsts().size());
			// 3、 循环输出相关邻区的信息
			List<CellAbst> cabs = this.getCellAbsts();
			if (cabs.size() > 0) {
				for (int k = 0; k < cabs.size(); k++) {
					// 干扰邻区名
					dataWriter.writeUTF(cabs.get(k).getCellName());
					// 干扰邻区是否与主小区有邻区关系
					dataWriter.writeUTF(cabs.get(k).getIsNei());
				}
			}
			// 4、 输出频点数量
			List<FreqInterInfo> fiis = this.getFreqInterInfos();
			dataWriter.writeInt(fiis.size());
			// 5、 循环输出频点干扰信息
			if (fiis.size() > 0) {
				for (int k = 0; k < fiis.size(); k++) {
					fiis.get(k).write(dataWriter);
				}
			}
		}

		public static CellInterWithDetailInfo read(DataInputStream dataReader)
				throws IOException {

			CellInterWithDetailInfo cidi = new CellInterWithDetailInfo();

			// 1、 读取cellname
			cidi.setCellName(dataReader.readUTF());
			// 2、 读取相关的邻小区的数量
			int cellAbstCnt = dataReader.readInt();
			// 3、 循环读取相关邻区的信息
			String ncell;
			String isnei;
			if (cellAbstCnt > 0) {
				for (int k = 0; k < cellAbstCnt; k++) {
					// 干扰邻区名
					ncell = dataReader.readUTF();
					// 干扰邻区是否与主小区有邻区关系
					isnei = dataReader.readUTF();
					cidi.addCellAbst(new CellAbst(ncell, isnei));
				}
			}
			// 4、 读取频点数量
			int freqcnt = dataReader.readInt();
			// 5、 循环读取频点干扰信息
			if (freqcnt > 0) {
				for (int k = 0; k < freqcnt; k++) {
					cidi.addFreqInterInfo(FreqInterInfo.read(dataReader));
				}
			}

			return cidi;
		}

	}

	// ci还是ca
	public static enum CiCaType {
		Ci("ci"), // 与指定频点相同的频点
		UpCa("uca"), // 比指定频点高的邻频
		LowCa("lca");// 比指定频点低的邻频
		private String name;

		private CiCaType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	// 干扰方向是in，还是out
	public static enum InterDir {
		In("in"), Out("out");
		private String name;

		private InterDir(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	

	public static class CellInterferDetailReadWriter {
		boolean isOpen = false;
		File idxFile;
		File dataFile;
		DataOutputStream idxWriter;// 索引文件的writer
		DataOutputStream dataWriter;// 数据文件的writer
		
		public CellInterferDetailReadWriter(File idxFile, File dataFile) {
			this.idxFile = idxFile;
			this.dataFile = dataFile;
		}
		
		public boolean open() {
			
			boolean result = true;
			
			if (isOpen) {
				return false;
			}
			FileOutputStream idxFos, dataFos;
			try {
				isOpen = true;
				// 索引相关
				idxFos = new FileOutputStream(idxFile);
				BufferedOutputStream idxBos = new BufferedOutputStream(idxFos);
				idxWriter = new DataOutputStream(idxBos);

				// 数据文件相关
				dataFos = new FileOutputStream(dataFile);
				BufferedOutputStream dataBos = new BufferedOutputStream(dataFos);
				dataWriter = new DataOutputStream(dataBos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				isOpen = false;
				result = false;
				try {
					idxWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					result = false;
				}
				try {
					dataWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					result = false;
				}
			}
			return result;
		}

		public boolean close() {
			boolean result = true;
			if (!isOpen) {
				result = false;
			}
			try {
				idxWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
			try {
				dataWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
			isOpen = false;
			return result;
		}

		public boolean writeSize(int size) {
			boolean result = true;
			if (!isOpen) {
				result = open();
			}
			// 先在索引文件上写小区数量
			try {
				idxWriter.writeInt(size);
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
			
			return result;
		}
		
		public boolean write(Collection<CellInterferMatrixInfo> cidis) {
			boolean result = true;
			if (!isOpen) {
				open();
			}
			if (cidis == null) {
				result = false;
			}
			for (CellInterferMatrixInfo cii : cidis) {
				result = write(cii);
			}
			return result;
		}

		private boolean write(CellInterferMatrixInfo cellInterferMatrixInfo) {
			boolean result = true;
			if (cellInterferMatrixInfo == null) {
				result = false;
			}
			try {
				dataWriter.flush();
				long currentLen = dataFile.length();// 也是该cellInterferDetailInfo的输出偏移值

				if(cellInterferMatrixInfo.getCellName()==null || StringUtils.isEmpty(cellInterferMatrixInfo.getCellName())){
					log.error("!!!!!干扰矩阵的cellname为空！");	
					result = false;
				}
				// 输出内容
				result = cellInterferMatrixInfo.write(dataWriter);
				// 输出到索引文件
				idxWriter.writeUTF(cellInterferMatrixInfo.getCellName());
				idxWriter.writeLong(currentLen);
				String interCells = cellInterferMatrixInfo.getInterCells();
				if(interCells==null || StringUtils.isEmpty(interCells)) {
					interCells = "";
				}
				idxWriter.writeUTF(interCells);
				
				// flush
				idxWriter.flush();
				dataWriter.flush();

			} catch (Exception e) {
				e.printStackTrace();
				result = false;
			}
			return result;
		}
		
		/**
		 * 读取某个小区的in干扰信息
		 * @param idxFilePath
		 * @param dataFilePath
		 * @param cell
		 * @return
		 * @author peng.jm
		 * @date 2014-8-1下午07:05:32
		 */
		public static CellInterferMatrixInfo readDirIn(String idxFilePath,
				String dataFilePath, String cell) {
			
			File idxFileR = FileTool.getFile(idxFilePath);
			File dataFileR = FileTool.getFile(dataFilePath);
			
			/****** Hbase start *****/
//			URI dataFileUri = null;
//			URI idxFileUri = null;
//			try {
//				dataFileUri = URI.create(dataFilePath);
//				idxFileUri = URI.create(idxFilePath);
//			} catch (Exception e) {
//				log.error("查找HDFS上的干扰矩阵文件时，路径出错！");
//				log.error("dataFilePath="+dataFilePath);
//				log.error("idxFilePath="+idxFilePath);
//				return null;
//			}
//			Path dataPathObj = new Path(dataFileUri);
//			Path idxPathObj = new Path(idxFileUri);
//			
//			FileSystem fs = null;
//			try {
//				fs = FileSystem.get(HadoopXml.getHbaseConfig());
//				if (!fs.exists(dataPathObj) || !fs.exists(idxPathObj)) {
//					log.error("Hdfs上的索引文件或数据文件不存在！idxFile=" + idxFilePath
//							+ ",dataFile=" + dataFilePath);
//					return null;
//				}
//			} catch (Exception e1) {
//				e1.printStackTrace();
//				log.error("查找干扰矩阵文件时，打开hdfs文件系统出错！");
//				return null;
//			}
//			
//			FSDataInputStream idxDis = null;
//			FSDataInputStream dataDis = null;
			/****** Hbase end *****/
			
			if (idxFileR==null||dataFileR==null
					||!idxFileR.exists() || !dataFileR.exists()) {
				log.error("指定的索引文件或数据文件不存在！idxFile=" + idxFilePath
						+ ",dataFile=" + dataFilePath);
				return null;
			}
			DataInputStream idxDis = null;
			DataInputStream dataDis = null;
			CellInterferMatrixInfo result = null;

			try {
				/****** Hbase start *****/
//				idxDis = fs.open(idxPathObj);
//				dataDis = fs.open(dataPathObj);
				/****** Hbase end *****/
				FileInputStream idxFis = new FileInputStream(idxFileR);
				FileInputStream dataFis = new FileInputStream(dataFileR);
				
				BufferedInputStream idxBis = new BufferedInputStream(idxFis);
				idxDis = new DataInputStream(idxBis);
				
				int cellCnt = idxDis.readInt();
				log.debug("索引文件中记录的小区数量：" + cellCnt);
				//System.out.println("索引文件中记录的小区数量："+cellCnt);
				String cellname;
				long pos = -1;
				boolean find = false;
				String interCells = "";
				
				for (int i = 0; i < cellCnt; i++) {
					cellname = idxDis.readUTF();
					pos = idxDis.readLong();
					interCells = idxDis.readUTF();
					
					if (cellname.equals(cell)) {
						find = true;
						break;
					}
				}
				if (!find) {
					log.error("in:未找到小区：" + cell + "的干扰记录");
					return null;
				}
				//System.out.println("小区："+cell+"的干扰数据的偏移量为："+pos);

				// 准备读取数据
				BufferedInputStream dataBis = new BufferedInputStream(dataFis);
				dataDis = new DataInputStream(dataBis);
				dataDis.skip(pos);// 跳过指定字节
				result = CellInterferMatrixInfo.read(dataDis, interCells);
			
			}catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					idxDis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (dataDis != null)
						dataDis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				try {
//					fs.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}

			return result;		
		}
	
		/**
		 * 读取某个小区的out干扰信息
		 * @param idxFilePath
		 * @param dataFilePath
		 * @param cell
		 * @return
		 * @author peng.jm
		 * @date 2014-8-2下午05:52:42
		 */
		public static CellInterferMatrixInfo readDirOut(String idxFilePath,
				String dataFilePath, String cell) {
			File idxFileR = FileTool.getFile(idxFilePath);
			File dataFileR = FileTool.getFile(dataFilePath);
			/****** Hbase start *****/
//			URI dataFileUri = null;
//			URI idxFileUri = null;
//			try {
//				dataFileUri = URI.create(dataFilePath);
//				idxFileUri = URI.create(idxFilePath);
//			} catch (Exception e) {
//				log.error("查找HDFS上的干扰矩阵文件时，路径出错！");
//				log.error("dataFilePath="+dataFilePath);
//				log.error("idxFilePath="+idxFilePath);
//				return null;
//			}
//			Path dataPathObj = new Path(dataFileUri);
//			Path idxPathObj = new Path(idxFileUri);
//			
//			FileSystem fs = null;
//			try {
//				fs = FileSystem.get(HadoopXml.getHbaseConfig());
//				if (!fs.exists(dataPathObj) || !fs.exists(idxPathObj)) {
//					log.error("Hdfs上的索引文件或数据文件不存在！idxFile=" + idxFilePath
//							+ ",dataFile=" + dataFilePath);
//					return null;
//				}
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				log.error("查找干扰矩阵文件时，打开hdfs文件系统出错！");
//				return null;
//			}
			/****** Hbase end *****/
			
			if (idxFileR==null||dataFileR==null
					||!idxFileR.exists() || !dataFileR.exists()) {
				log.error("指定的索引文件或数据文件不存在！idxFile=" + idxFilePath
						+ ",dataFile=" + dataFilePath);
				return null;
			}
			
			//DataInputStream idxDis = null;
			RandomAccessFile idxRaf = null;
			RandomAccessFile dataRaf = null;
			/****** Hbase start *****/
//			FSDataInputStream idxDis = null;
//			FSDataInputStream dataDis = null;
			/****** Hbase end *****/
			//初始化结果集对象
			CellInterferMatrixInfo result = new CellInterferMatrixInfo(cell); 
			InterferDetail resInterDetail = null;
			
			try {
				//FileInputStream idxFis = new FileInputStream(idxFileR);
				//BufferedInputStream idxBis = new BufferedInputStream(idxFis);
				//idxDis = new DataInputStream(idxBis);
				idxRaf = new RandomAccessFile(idxFileR, "r");
				/****** Hbase start *****/
//				idxDis=fs.open(idxPathObj);
//				dataDis=fs.open(dataPathObj);
				/****** Hbase end *****/
				
				int cellCnt;
				String cellname;
				long posTemp = -1;
				String interCells;
				//标记起始位置，最大读取为流长度
				//idxDis.mark(idxDis.available()+1);
				
				//在索引文件读取affectCells
				cellCnt = idxRaf.readInt();
				log.debug("索引文件中记录的小区数量：" + cellCnt);
				//System.out.println("索引文件中记录的小区数量："+cellCnt);
				
				boolean findAffectCell = false;
				List<String> affectCells = new ArrayList<String>();

				for (int i = 0; i < cellCnt; i++) {
					cellname = idxRaf.readUTF();
					posTemp = idxRaf.readLong();
					interCells = idxRaf.readUTF();
					if(cellname.equals(cell)) {
						if(("").equals(interCells) || interCells == null) {
							break;
						}
						String[] interCellsStr = interCells.split(",");
						for (String one : interCellsStr) {
							affectCells.add(one);
						}
						findAffectCell = true;
					}
				}
				if (!findAffectCell) {
					log.error("out:未找到被" + cell + "小区干扰的小区");
					return null;
				}
				//System.out.println("小区："+cell+"的干扰数据的偏移量为："+pos);

				//重置回到标记
				//idxDis.reset();
				idxRaf.seek(0);
				/****** Hbase start *****/
//				idxDis.seek(0);
				/****** Hbase end *****/
				//在索引文件读取affectCells的pos
				List<Map<String, Object>> cellPoses = new ArrayList<Map<String,Object>>();
				Map<String, Object> cellPos = null;
				boolean findPos = false;
				cellCnt = idxRaf.readInt();
				log.debug("索引文件中记录的小区数量：" + cellCnt);
				
				for (int i = 0; i < cellCnt; i++) {
					cellname = idxRaf.readUTF();
					posTemp = idxRaf.readLong();
					interCells = idxRaf.readUTF();
					for (String one : affectCells) {

						if(cellname.equals(one)) {
							cellPos = new HashMap<String, Object>();
							cellPos.put("cellName", cellname);
							cellPos.put("pos", posTemp);
							cellPoses.add(cellPos);
							findPos = true;
						}
					}
				}
				if (!findPos) {
					log.error("未找到受：" + cell + "小区干扰的小区在索引文件中保存的位置信息");
					return null;
				}
				//System.out.println(cellPoses);
				
				//在数据文件读取affectCells跟cell对应的CI，CA
				dataRaf = new RandomAccessFile(dataFileR, "r"); //只读
				
				List<InterferDetail> interDetails;
				CellInterferMatrixInfo cellInterferMatrixInfo = null;
				String ncellName;
				double ci, ca;
				String isNeighbour;
				
				for (Map<String, Object> one : cellPoses) {
						
					posTemp = Long.parseLong(one.get("pos").toString());
					cellname = one.get("cellName").toString();
					//跳转到对应位置
					dataRaf.seek(posTemp);
					/****** Hbase start *****/
//					dataDis.seek(posTemp);
					/****** Hbase end *****/
					//只关注取回的CI，CA
					cellInterferMatrixInfo = CellInterferMatrixInfo.read(dataRaf, ""); 
					
					//System.out.println(cellInterferMatrixInfo);
					interDetails = cellInterferMatrixInfo.getInterDetails();
					for (InterferDetail interDetail : interDetails) {
						ncellName = interDetail.getNcellName();

						ci = interDetail.getCi();
						ca = interDetail.getCa();
						isNeighbour = interDetail.getIsNeighbour();

						if(cell.equals(ncellName)) {
							resInterDetail = new InterferDetail();
							resInterDetail.setNcellName(cellname);
							resInterDetail.setCi(ci);
							resInterDetail.setCa(ca);
							resInterDetail.setIsNeighbour(isNeighbour);
							result.addInterDetail(resInterDetail);
						}
					}
					//System.out.println(result);
					//回到起点
					dataRaf.seek(0);
					/****** Hbase start *****/
//					dataDis.seek(0);
					/****** Hbase end *****/
				}

			}catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					idxRaf.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (dataRaf != null)
						dataRaf.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				try {
//					fs.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}

			return result;		
		}
	}
	
	public static class CellInterDetailReadWriter {
		boolean isOpen = false;
		File idxFile;
		File dataFile;
		DataOutputStream idxWriter;// 索引文件的writer
		DataOutputStream dataWriter;// 数据文件的writer

		public CellInterDetailReadWriter(File idxFile, File dataFile) {
			this.idxFile = idxFile;
			this.dataFile = dataFile;
		}

		public boolean open() {
			if (isOpen) {
				return false;
			}
			FileOutputStream idxFos, dataFos;
			try {
				isOpen = true;
				// 索引相关
				idxFos = new FileOutputStream(idxFile);
				BufferedOutputStream idxBos = new BufferedOutputStream(idxFos);
				idxWriter = new DataOutputStream(idxBos);

				// 数据文件相关
				dataFos = new FileOutputStream(dataFile);
				BufferedOutputStream dataBos = new BufferedOutputStream(dataFos);
				dataWriter = new DataOutputStream(dataBos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				isOpen = false;
				try {
					idxWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					dataWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			return true;
		}

		public void close() {
			if (!isOpen) {
				return;
			}
			try {
				idxWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				dataWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isOpen = false;
		}

		public void writeSize(int size) {
			if (!isOpen) {
				open();
			}
			// 先在索引文件上写小区数量
			try {
				idxWriter.writeInt(size);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void write(Collection<CellInterWithDetailInfo> cidis) {
			if (!isOpen) {
				open();
			}
			if (cidis == null) {
				return;
			}
			for (CellInterWithDetailInfo cii : cidis) {
				write(cii);
			}
		}

		private void write(CellInterWithDetailInfo cellInterDetailInfo) {
			if (cellInterDetailInfo == null) {
				return;
			}
			try {
				dataWriter.flush();
				long currentLen = dataFile.length();// 也是该cellInterDetailInfo的输出偏移值

				// 输出内容
				cellInterDetailInfo.write(dataWriter);
				// 输出到索引文件
				idxWriter.writeUTF(cellInterDetailInfo.getCellName());
				idxWriter.writeLong(currentLen);

				// flush
				idxWriter.flush();
				dataWriter.flush();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 读取指定的某个小区的干扰详情
		 * 
		 * @param idxFilePath
		 * @param dataFilePath
		 * @param cell
		 * @return
		 * @author brightming 2014-6-18 下午1:32:03
		 */
		public static CellInterWithDetailInfo read(String idxFilePath,
				String dataFilePath, String cell) {
			File idxFileR = new File(idxFilePath);
			File dataFileR = new File(dataFilePath);

			if (!idxFileR.exists() || !dataFileR.exists()) {
				log.error("指定的索引文件或数据文件不存在！idxFile=" + idxFilePath
						+ ",dataFile=" + dataFilePath);
				return null;
			}
			DataInputStream idxDis = null;
			DataInputStream dataDis = null;
			CellInterWithDetailInfo result = null;
			try {
				FileInputStream idxFis = new FileInputStream(idxFileR);
				FileInputStream dataFis = new FileInputStream(dataFileR);

				BufferedInputStream idxBis = new BufferedInputStream(idxFis);
				idxDis = new DataInputStream(idxBis);
				int cellCnt = idxDis.readInt();
				log.debug("索引文件中记录的小区数量：" + cellCnt);
				// System.out.println("索引文件中记录的小区数量："+cellCnt);
				String cellname;
				long pos = -1;
				boolean find = false;
				for (int i = 0; i < cellCnt; i++) {
					cellname = idxDis.readUTF();
					pos = idxDis.readLong();
					if (cellname.equals(cell)) {
						find = true;
						break;
					}
				}
				if (!find) {
					log.error("未找到小区：" + cell + "的干扰记录");
					return null;
				}
				// System.out.println("小区："+cell+"的干扰数据的偏移量为："+pos);

				// 准备读取数据
				BufferedInputStream dataBis = new BufferedInputStream(dataFis);
				dataDis = new DataInputStream(dataBis);
				dataDis.skip(pos);// 跳过指定字节
				result = CellInterWithDetailInfo.read(dataDis);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					idxDis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (dataDis != null)
						dataDis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}

	
	/**
	 * 计算NCS干扰矩阵
	 * @param connection
	 * @param stmt
	 * @param martixRecId
	 * @param savePath
	 * @param cityId
	 * @param startMeaDate
	 * @param endMeaDate
	 * @param worker
	 * @param report
	 * @author peng.jm
	 * @date 2014-8-18下午03:28:46
	 */
	/*public boolean calculateInterferMartix(Connection connection,
			Statement stmt, long martixRecId, String savePath, long cityId,
			String startMeaDate, String endMeaDate, JobWorker worker,
			JobReport report) {

		boolean result = true;
		
		//初始化条件对象
		StructAnaTaskInfo structAnaTaskInfo = new StructAnaTaskInfo();
		structAnaTaskInfo.setCityId(cityId);
		structAnaTaskInfo.setStartDate(RnoHelper.to_yyyyMMddHHmmssDate(startMeaDate));
		structAnaTaskInfo.setEndDate(RnoHelper.to_yyyyMMddHHmmssDate(endMeaDate));
	
		Map<String,String> tableMap = new HashMap<String, String>();
		boolean flag = false;
		//报告阶段信息
		report.setStage("计算干扰矩阵的数据处理");
		//转移NCS数据到临时表
		log.debug(">>>>>>>>>>>>转移数据到临时表...");
		tableMap.put("sourceTab", "RNO_2G_ERI_NCS");
		tableMap.put("targetTab", "RNO_2G_ERI_NCS_T");
		tableMap.put("descTab", "RNO_2G_ERI_NCS_DESCRIPTOR");
		flag = transfer2GEriNcsToTempTable(stmt, structAnaTaskInfo, tableMap);
		log.debug("爱立信NCS数据转移到临时表结果="+flag);
		
		tableMap.put("sourceTab", "RNO_2G_HW_NCS");
		tableMap.put("targetTab", "RNO_2G_HW_NCS_T");
		tableMap.put("descTab", "RNO_2G_HW_NCS_DESC");
		flag = transfer2GHwNcsToTempTable(stmt, structAnaTaskInfo, tableMap);
		log.debug("华为NCS数据转移到临时表结果="+flag);
		log.debug("<<<<<<<<<<<转移数据到临时表完毕...");
		
		//准备数据
		log.debug(">>>>>>>>>>>>准备干扰矩阵所需数据...");
		tableMap.put("tmperincssourceTab", "RNO_2G_ERI_NCS_T");
		tableMap.put("tmphwncssourceTab", "RNO_2G_HW_NCS_T");
		tableMap.put("ncsanatargetTab", "RNO_2G_NCS_ANA_T");
		flag = transfer2GNcsToUnifyAnaTable(stmt, structAnaTaskInfo, tableMap);
		log.debug("临时表的NCS数据转移到汇总分析表结果="+flag);
		log.debug("<<<<<<<<<<<干扰矩阵所需数据准备完毕...");
		//保存报告信息
		if(flag) {
			report.setBegTime(new Date());
			report.setEndTime(new Date());
			report.setFinishState("成功");
			report.setAttMsg("");
			worker.addJobReport(report);
		} else {
			report.setBegTime(new Date());
			report.setEndTime(new Date());
			report.setFinishState("失败");
			report.setAttMsg("");
			worker.addJobReport(report);
		}

		//报告阶段信息
		report.setStage("生成干扰矩阵");
		//计算干扰矩阵
		log.debug(">>>>>>>>>>>开始计算干扰矩阵.....");
		if (flag) {
			
			File file = new File(savePath);
			if (file.isDirectory() && !file.exists()) {
				file.mkdirs();
			} else if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			String idxFilePath = savePath + File.separator + "ncs_res_" + martixRecId
					+ RnoConstant.ReportConstant.INTERMATRIXIDXSUFFIX;
			String dataFilePath = savePath + File.separator + "ncs_res_" + martixRecId
					+ RnoConstant.ReportConstant.INTERMATRIXDATASUFFIX;
			
			result = calculateFreqInterferMatrixResult(stmt, idxFilePath, dataFilePath);
			//保存报告信息
			if(result) {
				report.setBegTime(new Date());
				report.setEndTime(new Date());
				report.setFinishState("成功");
				report.setAttMsg("");
				worker.addJobReport(report);
			} else {
				report.setBegTime(new Date());
				report.setEndTime(new Date());
				report.setFinishState("失败");
				report.setAttMsg("");
				worker.addJobReport(report);
			}
			log.debug("<<<<<<<<干扰矩阵计算完毕。");
		}
		
		return result;
	}*/
	
	public static void main(String[] args) throws Exception {
		// File file = new File("d:/mytest.txt");
		// FileOutputStream fos = new FileOutputStream(file);
		// BufferedOutputStream bos = new BufferedOutputStream(fos);
		// DataOutputStream dos = new DataOutputStream(bos);
		// dos.writeInt(1);
		// dos.flush();
		// System.out.println("len="+file.length());
		// dos.writeChars("check");
		// dos.flush();
		// System.out.println("len="+file.length());
		// dos.close();

		//
//		String idxFilePath = "d:/intermatrixIdx.txt";
//		String dataFilePath = "d:/intermatrixData.txt";
//		String cell = "GQWLEEZ";
//		CellInterWithDetailInfo result = CellInterDetailReadWriter.read(
//				idxFilePath, dataFilePath, cell);
//		System.out.println("result=" + result);
//
//		String savePath = "D:\\Workspaces\\MyEclipse2013\\.metadata\\.me_tcat\\webapps\\ops\\op\\rno\\ana_result\\2014\\6\\ncs_res_124.xls";
//		File file = new File(savePath);
//		String fileName = file.getName();
//		String parentPath = file.getParentFile().getAbsolutePath();
//		//
//		String pre = fileName;
//		// 去除后缀名
//		if (fileName.lastIndexOf(".") > 0) {
//			pre = fileName.substring(0, fileName.lastIndexOf("."));
//		}
//		// 重新补充为全路径名
//		pre = parentPath + File.separator + pre;
//
//		System.out.println("pre=" + pre);
//
//		savePath = "D:\\Workspaces\\MyEclipse2013\\.metadata\\.me_tcat\\webapps\\ops\\op\\rno\\ana_result\\2014\\6\\ncs_res_124xls";
//		file = new File(savePath);
//		fileName = file.getName();
//		parentPath = file.getParentFile().getAbsolutePath();
//		//
//		pre = fileName;
//		// 去除后缀名
//		if (fileName.lastIndexOf(".") > 0) {
//			pre = fileName.substring(0, fileName.lastIndexOf("."));
//		}
//		// 重新补充为全路径名
//		pre = parentPath + File.separator + pre;
//		System.out.println("pre=" + pre);
//
//		System.out.println(File.separator);

		//String idxFilePath = "D:/测试/ncs_res_180.interferMatrix.Idx";
		//String dataFilePath = "D:/测试/ncs_res_180.interferMatrix.Data";
		String idxFilePath = "D:/测试/ncs_res_36.interferMatrix.Idx";
		String dataFilePath = "D:/测试/ncs_res_36.interferMatrix.Data";

//		File idxFile = new File(idxFilePath);
//		File dataFile = new File(dataFilePath);
		String cell = "DQVHBQ3";//S3ACHS7,S3AMKL6，S3AXZY7


		CellInterferMatrixInfo result = CellInterferDetailReadWriter.readDirIn(
				idxFilePath, dataFilePath, cell);
		System.out.println("result=" + result);
		CellInterferMatrixInfo result1 = CellInterferDetailReadWriter.readDirOut(
				idxFilePath, dataFilePath, cell);
		System.out.println("result1=" + result1);
		
	}

	/**
	 * 自动匹配mrr文件对应的bsc
	 * @param connection
	 * @param mrrTabName
	 * mrr信号质量记录表名称
	 * @param mrrDescTabName
	 * mrr描述表名称，将被更新
	 * @param mrrId
	 * @author peng.jm
	 * @date 2014-7-23上午09:44:58
	 */
	public boolean matchMrrBsc(Connection connection, String mrrTabName,
			String mrrDescTabName, String mrrId) {

		log.debug("进入方法：matchMrrBsc.connection=" + connection
				+ ",mrrTabName=" + mrrTabName + ",mrrDescTabName="
				+ mrrDescTabName + ",mrrId=" + mrrId);
		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			return false;
		}
		if (mrrId == null || mrrId.isEmpty()) {
			log.error("未指明mrr范围，不执行匹配。");
			return true;
		}

		String sql = "select eri_mrr_desc_id,bsc from " +
				" ( select eri_mrr_desc_id,bsc,rank() over (partition by eri_mrr_desc_id order by cnt desc) as seq from " +
				" ( select eri_mrr_desc_id,bsc,count(*) as cnt from " +
				" ( select mid2.eri_mrr_desc_id,rno_bsc.engname as bsc from " +
                "              ( select mid.eri_mrr_desc_id,mid.cell_name,cell.bsc_id from " +
                "                     (select eri_mrr_desc_id,cell_name as cell_name from " + 
                							mrrTabName + " where eri_mrr_desc_id = " + mrrId + ") mid " +
                "                  inner join cell on(mid.cell_name=cell.label) " +
                "               ) mid2 " +
                "            inner join rno_bsc on(mid2.bsc_id = rno_bsc.bsc_id) " +
                "           ) " +
                "      group by  eri_mrr_desc_id,bsc " +
                "      order by eri_mrr_desc_id,cnt desc nulls last " +
                "    ) " +
                ")  " +
                " where seq = 1";
		sql = "merge into "
				+ mrrDescTabName
				+ " tar using ("
				+ sql
				+ ") src on(tar.eri_mrr_desc_id=src.eri_mrr_desc_id) " 
				+ " when matched then update set tar.bsc=src.bsc";
		log.debug("更新mrr描述表的bsc的sql=" + sql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return true;
	}

	/**
	 * 检查mrr文件对应区域是否符合要求
	 * @param mrrId
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23上午11:47:26
	 */
	public boolean checkMrrArea(Connection connection, long mrrId, long cityId) {
		log.debug("进入方法：checkMrrArea, mrrId=" + mrrId + ",cityId=" + cityId);
		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			return false;
		}
		String sql = "select * from sys_area  where sys_area.parent_id = " + cityId
				+ " and sys_area.area_id in "
				+ "    (select rno_bsc_rela_area.area_id from rno_bsc_rela_area where rno_bsc_rela_area.bsc_id in "
				+ "          (select bsc_id from "
				+ "                ( select eri_mrr_desc_id,bsc_id,rank() over (partition by eri_mrr_desc_id order by cnt desc) as seq from "
				+ "                     ( select eri_mrr_desc_id,bsc_id,count(*) as cnt from "
				+ "                          (select mid2.eri_mrr_desc_id,rno_bsc.bsc_id from "
				+ "                                ( select mid.eri_mrr_desc_id,mid.cell_name,cell.bsc_id from "
				+ "                                      (select eri_mrr_desc_id,cell_name as cell_name from RNO_ERI_MRR_TA_TEMP " +
																"where eri_mrr_desc_id = "+ mrrId +") mid "
				+ "                                 inner join cell on(mid.cell_name=cell.label) "
				+ "                                 ) mid2 "
				+ "                              inner join rno_bsc on(mid2.bsc_id = rno_bsc.bsc_id) "
				+ "                            ) "
				+ "                          group by  eri_mrr_desc_id,bsc_id "
				+ "                          order by eri_mrr_desc_id,cnt desc nulls last "
				+ "                      ) "
				+ "                 ) "
				+ "         where seq=1) "
				+ "    )";
		log.info("checkMrrArea的sql：" + sql);
		Statement stmt = null;
		try {
			//按照缺省方式打开的ResultSet不支持结果集cursor的回滚
			//如果想要完成操作，要在生成Statement对象时加入如下两个参数
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.last(); //移到最后一行
			int length = rs.getRow();
			if(length > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return false;
	}
}
