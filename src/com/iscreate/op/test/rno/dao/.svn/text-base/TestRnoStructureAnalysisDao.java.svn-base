package com.iscreate.op.test.rno.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDao;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.test.rno.TestBase;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class TestRnoStructureAnalysisDao extends TestBase {
	RnoStructureAnalysisDao dao;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dao = (RnoStructureAnalysisDao) super.context
				.getBean("rnoStructureAnalysisDao");
		assertNotNull(dao);
	}

	// public void testMatchNcell(){
	// Connection conn=DataSourceConn.initInstance().getConnection();
	// assertNotNull(conn);
	// try {
	// conn.setAutoCommit(false);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// List<Long> ncsIds=new ArrayList<Long>();
	// ncsIds.add(199L);
	// String tabName="rno_ncs";
	// Map res=dao.matchNcsNcell(conn, tabName, ncsIds);
	// System.out.println("res="+res);
	// try {
	// conn.commit();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// try {
	// conn.setAutoCommit(true);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

	// public void testCalculateInterfer() {
	// Connection conn = DataSourceConn.initInstance().getConnection();
	// assertNotNull(conn);
	// List<Long> ncsIds = new ArrayList<Long>();
	// ncsIds.add(199L);
	// dao.calculateInterfer(conn, "rno_ncs", ncsIds);
	// }

	// public void testCalculateInterferMatrix(){
	// Connection conn = DataSourceConn.initInstance().getConnection();
	// assertNotNull(conn);
	// dao.calculateInterferMatrix(conn, tabName, numerator, ncsIds);
	// }

	/**
	 * 最大连通簇
	 * 
	 * @author brightming 2014-1-16 下午6:08:37
	 */
//	public void testCalculateConnectedCluster() {
//		Connection conn = DataSourceConn.initInstance().getConnection();
//		assertNotNull(conn);
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Long> ncsIds = new ArrayList<Long>();
//		ncsIds.add(199L);
//		String ncsTabName = "rno_ncs";
//		String clusterTab = "RNO_NCS_CLUSTER";
//		boolean ok = dao.calculateConnectedCluster(conn, ncsTabName, ncsIds,
//				clusterTab, 0.5f);
//		System.out.println("res==" + ok);
//
//		if (ok) {
//			try {
//				conn.rollback();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				conn.rollback();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		try {
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
	/**
	 * 测试簇约束因子
	 * 
	 * @author brightming
	 * 2014-1-16 下午6:40:36
	 */
//	public void testCalculateConnectedCluster() {
//		Connection conn = DataSourceConn.initInstance().getConnection();
//		assertNotNull(conn);
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Long> ncsIds = new ArrayList<Long>();
//		ncsIds.add(199L);
//		String clusterTab = "RNO_NCS_CLUSTER";
//		Map<Long,Boolean> ok = dao.calculateClusterConstrain(conn, 215, clusterTab, "RNO_NCS_CLUSTER_CELL", ncsIds);
//		System.out.println("res==" + ok);
//
//		try {
//			conn.commit();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 测试干扰矩阵
	 * 
	 * @author brightming
	 * 2014-1-16 下午6:46:26
	 */
//	public void testCalculateInterferMatrix() {
//		Connection conn = DataSourceConn.initInstance().getConnection();
//		assertNotNull(conn);
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Long> ncsIds = new ArrayList<Long>();
//		ncsIds.add(199L);
//		String ncsTab="rno_ncs";
//        Boolean  ok = dao.calculateInterferMatrix(conn, ncsTab, "", ncsIds);
//		System.out.println("res==" + ok);
//
//		try {
//			conn.commit();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 测试计算簇权重
	 */
//	public void testCalculateNcsClusterWeight() {
//		Connection conn = DataSourceConn.initInstance().getConnection();
//		assertNotNull(conn);
//		try {
//			conn.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Long> ncsIds = new ArrayList<Long>();
//		ncsIds.add(199L);
//		String ncsTab="rno_ncs";
//        Map<Long, Boolean>  ok = dao.calculateNcsClusterWeight(conn, ncsTab, ncsIds, Arrays.asList(1L,2L), true, 0.2f);
//		System.out.println("res==" + ok);
//
//		try {
//			conn.commit();
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			conn.setAutoCommit(true);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public void testCalculateFreqInterferResult(){
		Connection conn = DataSourceConn.initInstance().getConnection();
		assertNotNull(conn);
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		List<Long> ncsIds = new ArrayList<Long>();
		ncsIds.add(352L);
		List<String> cells=new ArrayList<String>();
		long cnt=1;//dao.prepareFreqInterferMetaData(conn, ncsIds, cells);
		System.out.println("cnt="+cnt);
		CellFreqInterferList result=dao.calculateFreqInterferResult(conn);
		
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
		Gson gson=new Gson();
		String interfer=gson.toJson(result);
		System.out.println("interfer=="+interfer);
	}
}
