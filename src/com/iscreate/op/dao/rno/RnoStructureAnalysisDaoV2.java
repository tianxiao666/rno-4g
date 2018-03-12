package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.vo.StructAnaTaskInfo;
import com.iscreate.op.action.rno.vo.Threshold;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.pojo.rno.RnoStructAnaJobRec;
import com.iscreate.op.pojo.rno.RnoStructureAnalysisTask;
import com.iscreate.op.pojo.rno.RnoThreshold;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.client.JobWorker;
import com.iscreate.op.service.rno.task.structana.RnoStructAnaJobRunnable;

public interface RnoStructureAnalysisDaoV2 {

	/**
	 * 
	 * @title 转移2G小区数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:sourceTab,targetTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:28:00
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GCellToTempTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	/**
	 * 
	 * @title 转移2G爱立信NCS数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:sourceTab,targetTab,descTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:30:12
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public ResultInfo transfer2GEriNcsToTempTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	/**
	 * 
	 * @title 转移2G华为NCS数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:sourceTab,targetTab,descTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:32:57
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public ResultInfo transfer2GHwNcsToTempTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	
	/**
	 * 
	 * @title 转移2G华为Mrr数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:hratesourceTab,hratetargetTab,fratesourceTab,fratetargetTab,descTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:37:43
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GHwMrrToTempTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	/**
	 * 
	 * @title 转移2G爱立信Mrr数据到临时表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:qualitysourceTab,qualitytargetTab,strengthsourceTab,strengthtargetTab,tasourceTab,tatargetTab,descTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午3:52:31
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriMrrToTempTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	/**
	 * 
	 * @title 转移2G华为、爱立信mrr数据到统一分析表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:tmpqualitysourceTab,tmpstrengthsourceTab,tmptasourceTab,tmphratesourceTab,tmpfratesourceTab,mrranatargetTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午4:55:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriMrrToUnifyAnaTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	
	public boolean transfer2GHwMrrToUnifyAnaTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap);
	/**
	 * 
	 * @title 转移2G华为、爱立信ncs数据到统一分析表
	 * @param stmt
	 * @param anaTaskInfo
	 * @param tableMap key:tmperincssourceTab,tmphwncssourceTab,ncsanatargetTab
	 * @return
	 * @author chao.xj
	 * @date 2014-8-18下午5:23:00
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean transfer2GEriNcsToUnifyAnaTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap,List<RnoThreshold> rnoThresholds,RnoStructAnaJobRec jobRec);
	
	public boolean transfer2GHwNcsToUnifyAnaTable(Statement stmt,StructAnaTaskInfo anaTaskInfo,Map<String, String> tableMap,List<RnoThreshold> rnoThresholds,RnoStructAnaJobRec jobRec);
	/**
	 * 
	 * @title 计算小区相关指标的总入口
	 * @param conn
	 * @param threshold
	 * @param tableMap
	 *        key:ncsTab,cellResTab,clusterTab,clusterCellTab,cellMrrAnaTab
	 *        val:RNO_2G_NCS_ANA_T,RNO_NCS_CELL_ANA_RESULT_MID,RNO_NCS_CLUSTER_MID,RNO_NCS_CLUSTER_CELL_MID,rno_2g_mrr_ana_t
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20上午11:36:26
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public boolean calculate2GCellRes(long cityId,JobWorker worker,JobReport report,Connection conn,Map<String, String> tableMap,List<RnoThreshold> rnoThresholds,boolean useIdealCoverDis);
	/**
	 * 
	 * @title 
	 * 		计算小区理想覆盖距离 主小区覆盖方向120度内最近的3个（可设置）非同站且同频段的关联邻小区的平均距离。 邻区也不能同频段
	 * 		小区理想覆盖距离=(∑_(i=1)^N▒至邻近基站距离)/N
	 * 		上式中同一结构层是指只考虑相同层次的小区，例如900小区只考虑周边最近的N个900基站的距离，而不考虑1800基站或者其他底层站、室内站。
	 * @param stmt
	 * @param ncsTab
	 * @param cellResTabName
	 * @param lowestN
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午1:22:58
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public boolean calculate2GCellIdealCoverDis(long cityId,Statement stmt, String ncsTab,
//			String cellResTabName, List<RnoThreshold> rnoThresholds);
	/**
	 * 
	 * @title 计算小区的被干扰系数、干扰源系数
	 * @param conn
	 * @param ncsNcellTabName
	 * @param cellResTab
	 * @param threshold
	 * @return
	 * @author chao.xj
	 * @date 2014-8-20下午2:07:10
	 * @company 怡创科技
	 * @version 1.2
	 */
//	public boolean calculate2GCellInterferFactor(Connection conn,
//			String ncsNcellTabName, String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算过覆盖系数 为： 主小区到最远关联邻小区的距离/(主小区理想覆盖距离*系数)
//	 * @param stmt
//	 * @param ncsTab
//	 * @param cellResTabName
//	 * @param adjustFactor
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午1:27:35
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GCellOverCoverFactor(Statement stmt, String ncsTab,String cellResTabName, List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算网络结构指数
//	 * @param conn
//	 * @param ncsDescId
//	 * @param ncsNcellTabName
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午2:29:02
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GNetworkStrucFactor(Connection conn,
//			String ncsNcellTabName, String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算冗余覆盖指数
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午2:55:11
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GRedundantCoverFacotr(Connection conn,
//			String ncsNcellTabName, String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算2G重叠覆盖度
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param cellResTab
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午3:02:52
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GOverlapCoverFactor(Connection conn,
//			String ncsNcellTabName, String cellResTab);
//	/**
//	 * 
//	 * @title 计算2g小区检测次数
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午3:07:12
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GDetectCnt(Connection conn,
//			String ncsNcellTabName, String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 
//	 * 		关联邻小区数 邻小区的NCS测量报告中，服务小区在邻小区测量报告中出现且信号强度差>-12dB的比例 ≥ 5%，
//	 * 		可认为该邻小区是主小区的关联邻小区。 需要计算2个结果：一个是包含室分小区，一个是不包含室分小区
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午3:30:36
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GStrongAssNcellCnt(Connection conn, 
//			String ncsNcellTabName, String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 小区覆盖分量
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param clusterTab
//	 * @param clusterCellTab
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午3:47:29
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GCellCover(Connection conn,
//			String ncsNcellTabName, String clusterTab, String clusterCellTab,
//			String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 小区容量破坏分量计算
//	 * @param conn
//	 * @param ncsNcellTabName
//	 * @param clusterTab
//	 * @param clusterCellTab
//	 * @param cellResTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午3:57:38
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GCapacityDestroy(Connection conn,
//			String ncsNcellTabName, String clusterTab, String clusterCellTab,
//			String cellResTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算最大连通簇 ncsTabName:邻区测量信息所在的表 targTable:计算得到的连通簇的存放表 threshold:门限值
//	 * @param conn
//	 * @param ncsTabName
//	 * @param ncsIds
//	 * @param clusterTable
//	 * @param clusterCellTabName
//	 * @param clusterCellRelaTab
//	 * @param threshold
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-20下午4:22:06
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GConnectedCluster(Connection conn,
//			String ncsTabName,  String clusterTable,
//			String clusterCellTabName, String clusterCellRelaTab,
//			List<RnoThreshold> rnoThresholdsObj,RnoStructAnaJobRec jobRec);
//	/**
//	 * 
//	 * @title 计算簇约束因子
//	 * @param conn
//	 * @param areaId
//	 * @param clusterTab
//	 * @param clusterCellTab
//	 * @param ncsIds
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-1-15下午03:22:40
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GClusterConstrain(Statement stmt,
//			String clusterTab, String clusterCellTab,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 计算簇权重
//	 * @param conn
//	 * @param ncsTab
//	 * @param clusterTab
//	 * @param clusterCellTab
//	 * @param ncsIds
//	 * @param stsIds
//	 * @param forceRecalculte
//	 * 			true
//	 * @param badClusterThreshold
//	 * 			 簇约束因子的门限，大于0.5f此值，表示有问题，也就是需要计算的簇
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-21下午3:01:04
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean calculate2GNcsClusterWeight(Connection conn,
//			String ncsTab, String clusterTab, String clusterCellTab,
//			List<Long> stsIds, boolean forceRecalculte,
//			float badClusterThreshold);
//	/**
//	 * 
//	 * @title 自动优化：挑选问题小区
//	 * @param stmt
//	 * @param cityId
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-21下午3:40:03
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public boolean pick2GCellsWithProblem(Statement stmt, long cityId,List<RnoThreshold> rnoThresholds);
//	/**
//	 * 
//	 * @title 汇总分析的入口调用
//	 * @param connection
//	 * @param stmt
//	 * @param savePath
//	 * 			包含完整文件名的最终保存路径
//	 * @param anaTaskInfo
//	 * @param taskId
//	 * @return
//	 * @author chao.xj
//	 * @date 2014-8-21下午5:58:45
//	 * @company 怡创科技
//	 * @version 1.2
//	 */
//	public Map<String, Object> do2GStructAnalysis(Connection connection,
//			Statement stmt, String savePath, StructAnaTaskInfo anaTaskInfo,
//			long taskId);
//	
//	/**
//	 * 结构分析入口
//	 * @param worker
//	 * @param connection
//	 * @param jobRec
//	 * @param threshold
//	 * @return
//	 * @author brightming
//	 * 2014-8-26 下午5:16:49
//	 */
//	public ResultInfo do2GStructAnalysis(JobWorker worker,Connection connection, 
//			RnoStructAnaJobRec jobRec, List<RnoThreshold> rnoThresholds);
	/**
	 * 
	 * @title  结构分析入口
	 * @param worker
	 * @param connection
	 * @param jobRec
	 * @param analysisTask
	 * @return
	 * @author chao.xj
	 * @date 2014-9-10下午12:27:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public ResultInfo do2GStructAnalysis(RnoStructAnaJobRunnable worker,Connection connection, 
			RnoStructAnaJobRec jobRec, RnoStructureAnalysisTask analysisTask);
}
