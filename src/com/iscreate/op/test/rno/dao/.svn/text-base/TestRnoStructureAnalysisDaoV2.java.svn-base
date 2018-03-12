package com.iscreate.op.test.rno.dao;

import java.text.SimpleDateFormat;
import java.util.Properties;

import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoV2;
import com.iscreate.op.dao.rno.RnoStructureQueryDao;
import com.iscreate.op.test.rno.TestBase;


public class TestRnoStructureAnalysisDaoV2 extends TestBase {
	RnoStructureAnalysisDaoV2 anaDao;
	RnoStructureQueryDao queryDao;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		anaDao=(RnoStructureAnalysisDaoV2)super.context.getBean("rnoStructureAnalysisDaoV2");
		assertNotNull(anaDao);
		queryDao=(RnoStructureQueryDao)super.context.getBean("rnoStructureQueryDao");
		assertNotNull(anaDao);
	}
	public void testQueryAreaDamageFactor(){
		/*System.out.println("进入testQueryAreaDamageFactor方法");
		Threshold threshold=queryDao.getThresholdByModType("STRUCTANA");
		StructAnaTaskInfo anaTaskInfo=new StructAnaTaskInfo();
		anaTaskInfo.setThreshold(threshold);
		Date startDate;
		Date endDate;
		RnoStructAnaJobRec jobRec=new RnoStructAnaJobRec();
		try {
			startDate = formatter.parse("2014-08-20 00:00:00");
			endDate=formatter.parse("2014-08-20 23:59:59");
			anaTaskInfo.setStartDate(startDate);
			anaTaskInfo.setEndDate(endDate);
			anaTaskInfo.setCityId(104);
			
			
			jobRec.setCityId(104L);
			jobRec.setBegMeaTime(startDate);
			jobRec.setEndMeaTime(endDate);
			jobRec.setJobId(100L);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection conn =  DataSourceConn.initInstance().getConnection();
		assertNotNull(conn);
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Statement stmt=null;
		try {
			stmt=conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			Map<String, Object> map=anaDao.do2GStructAnalysis(conn, stmt, "d:/tmp/ana/", anaTaskInfo,1);
			Iterator<Entry<String, Object>> iterator=map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry=iterator.next();
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				
			}
			LocalJobWorker worker=new LocalJobWorker();
			
			ResultInfo res=anaDao.do2GStructAnalysis(worker, conn, jobRec, threshold);
			System.out.println("res:"+res);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				conn.commit();
				conn.setAutoCommit(true);
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
			try {
			conn.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("退出testQueryAreaDamageFactor方法");*/
	}
	
	public static void main(String[] args) {
		Properties props=System.getProperties(); //系统属性
		 System.out.println("默认的临时文件路径："+props.getProperty("java.io.tmpdir"));
	}
}

//package com.iscreate.op.test.rno.dao;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Properties;
//
//import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
//import com.iscreate.op.action.rno.vo.Threshold;
//import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoV2;
//import com.iscreate.op.dao.rno.RnoStructureQueryDao;
//import com.iscreate.op.pojo.rno.ResultInfo;
//import com.iscreate.op.pojo.rno.RnoStructAnaJobRec;
//import com.iscreate.op.service.rno.job.client.JobWorker;
//import com.iscreate.op.service.rno.job.client.LocalJobWorker;
//import com.iscreate.op.test.rno.TestBase;
//import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
//
//
//public class TestRnoStructureAnalysisDaoV2 extends TestBase {
//	RnoStructureAnalysisDaoV2 anaDao;
//	RnoStructureQueryDao queryDao;
//	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	@Override
//	protected void setUp() throws Exception {
//		// TODO Auto-generated method stub
//		super.setUp();
//		anaDao=(RnoStructureAnalysisDaoV2)super.context.getBean("rnoStructureAnalysisDaoV2");
//		assertNotNull(anaDao);
//		queryDao=(RnoStructureQueryDao)super.context.getBean("rnoStructureQueryDao");
//		assertNotNull(anaDao);
//	}
//	public void testQueryAreaDamageFactor(){
//		System.out.println("进入testQueryAreaDamageFactor方法");
//		Threshold threshold=queryDao.getThresholdByModType("STRUCTANA");
//		StructAnaTaskInfo anaTaskInfo=new StructAnaTaskInfo();
//		anaTaskInfo.setThreshold(threshold);
//		Date startDate;
//		Date endDate;
//		RnoStructAnaJobRec jobRec=new RnoStructAnaJobRec();
//		try {
//			startDate = formatter.parse("2014-08-20 00:00:00");
//			endDate=formatter.parse("2014-08-20 23:59:59");
//			anaTaskInfo.setStartDate(startDate);
//			anaTaskInfo.setEndDate(endDate);
//			anaTaskInfo.setCityId(104);
//			
//			
//			jobRec.setCityId(104L);
//			jobRec.setBegMeaTime(startDate);
//			jobRec.setEndMeaTime(endDate);
//			jobRec.setJobId(100L);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Connection conn =  DataSourceConn.initInstance().getConnection();
//		assertNotNull(conn);
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		Statement stmt=null;
//		try {
//			stmt=conn.createStatement();
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//
//		try {
//			/*Map<String, Object> map=anaDao.do2GStructAnalysis(conn, stmt, "d:/tmp/ana/", anaTaskInfo,1);
//			Iterator<Entry<String, Object>> iterator=map.entrySet().iterator();
//			while (iterator.hasNext()) {
//				Entry<String, Object> entry=iterator.next();
//				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//				
//			}*/
//			LocalJobWorker worker=new LocalJobWorker();
//			
//			ResultInfo res=anaDao.do2GStructAnalysis(worker, conn, jobRec, threshold);
//			System.out.println("res:"+res);
//		} catch (Exception e) {
//			// TODO: handle exception
//			try {
//				conn.commit();
//				conn.setAutoCommit(true);
//			} catch (Exception e2) {
//				// TODO: handle exception
//				e2.printStackTrace();
//			}
//			e.printStackTrace();
//		}
//			try {
//			conn.commit();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		System.out.println("退出testQueryAreaDamageFactor方法");
//	}
//	
//	public static void main(String[] args) {
//		Properties props=System.getProperties(); //系统属性
//		 System.out.println("默认的临时文件路径："+props.getProperty("java.io.tmpdir"));
//	}
//}

