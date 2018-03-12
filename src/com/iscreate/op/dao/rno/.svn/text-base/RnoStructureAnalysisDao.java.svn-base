package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.net.common.PredefineConstant.data_type;
import com.iscreate.op.dao.rno.RnoStructureAnalysisDaoImpl.CellInterWithDetailInfo;
import com.iscreate.op.pojo.rno.CellFreqInterferList;
import com.iscreate.op.pojo.rno.RnoNcsCell;
import com.iscreate.op.service.rno.job.JobReport;
import com.iscreate.op.service.rno.job.client.JobWorker;

public interface RnoStructureAnalysisDao {

	/**
	 * 邻区适配
	 * @param conn
	 * 数据库连接
	 * @param tabName
	 * ncs测量得到的邻区的存放表名
	 * @param ncsIds
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-1-11 下午3:11:52
	 */
	public Map<Long, Boolean> matchNcsNcell(Connection conn, String tabName,
			List<Long> ncsIds,long cityId);
	
	
	/**
	 * 计算干扰度
	 * tabName:ncs邻区测量信息存放的表（临时表或正式表）
	 * numerator:分子列
	 * 
	 * @param stmt
	 * @param tabName
	 * @param ncsIds
	 */
	public boolean calculateInterfer(Statement stmt, String tabName,
			List<Long> ncsIds,String ciThreshold,String caThreshold);

	/**
	 * 计算干扰矩阵
	 * 
	 * @param stmt
	 * @param tabName
	 * @param numerator
	 * @param ncsIds
	 */
	public boolean calculateInterferMatrix(Statement stmt, String tabName, String numerator, List<Long> ncsIds);

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
			String clusterCellTabName,String clusterCellRelaTab, float threshold);
	/**
	 * 计算簇约束因子
	 * clusterTab:String,连通簇所在表名
	 * clusterCellTab:String,连通簇的小区所在的表名
	 * ncsIds:List<Long>，ncsid
	 * 
	 * @param stmt
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 */
	public Map<Long,Boolean> calculateClusterConstrain(Statement stmt,String clusterTab, String clusterCellTab, List<Long> ncsIds);



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
			float badClusterThreshold) ;
	
	
	/**
	 * 以ncs为单位，计算小区的被干扰系数、干扰源系数
	 * @param conn
	 * 数据库连接
	 * @param ncsId
	 * ncs id
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果保存表
	 * @return
	 * @author brightming
	 * 2014-2-10 上午11:13:07
	 */
	public boolean calculateCellInterferFactor(Connection conn,long ncsId,String ncsNcellTabName,String cellResTab);
	
	/**
	 * 计算网络结构指数
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author brightming
	 * 2014-2-10 上午11:15:43
	 */
	public boolean calculateNetworkStrucFactor(Connection conn,long ncsDescId,String ncsNcellTabName,String cellResTab);
	
	/**
	 * 计算冗余覆盖指数
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author brightming
	 * 2014-2-10 上午11:17:25
	 */
	public boolean calculateRedundantCoverFacotr(Connection conn,long ncsDescId,String ncsNcellTabName,String cellResTab);
	
	/**
	 * 计算重叠覆盖度
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author Liang YJ
	 * 2014-2-11 17:35:25
	 */
	public boolean calculateOverlapCoverFactor(Connection conn,long ncsDescId,String ncsNcellTabName,String cellResTab);
	
	/**
	 * 小区检测次数
	 * 需要计算2个结果：一个是包含室分小区，一个是不包含室分小区
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author Liang YJ
	 * 2014-2-12 9:53:25
	 */
	public boolean calculateDetectCnt(Connection conn,long ncsDescId,String ncsNcellTabName,String cellResTab);
	
	
	/**
	 * 关联邻小区数
	 * 需要计算2个结果：一个是包含室分小区，一个是不包含室分小区
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author brightming
	 * 2014-3-24 上午11:40:35
	 */
	public boolean calculateStrongAssNcellCnt(Connection conn,long ncsDescId,String ncsNcellTabName,String cellResTab);
	
	
	/**
	 * 准备频点干扰计算的元数据
	 * @param conn
	 * @param ncsIds
	 * @return
	 * @author brightming
	 * 2014-2-13 上午11:55:20
	 */
	public long prepareFreqInterferMetaData(Connection conn,List<Long> ncsIds);
	
	/**
	 * 准备频点干扰计算的元数据,只需要部分cell
	 * @param conn
	 * @param ncsIds
	 * @param cells
	 * @param srcTabName
	 * 源表名称
	 * @return
	 * @author brightming
	 * 2014-2-13 上午11:56:25
	 */
	public long prepareFreqInterferMetaData(Connection conn,List<Long> ncsIds,List<String> cells,String srcTabName);
	
	/**
	 * 在具有计算频点干扰所需数据的基础上计算频点干扰
	 * @param conn
	 * @return
	 * gson格式的数据
	 * {'total':'','cells':[{'cell':'xxx','inter':[{'freq':'xx','ci':'','ca':''},{..}]}]}
	 * @author brightming
	 * 2014-2-13 下午12:33:54
	 */
	public CellFreqInterferList calculateFreqInterferResult(Connection conn);

	/**
	 * 计算小区理想覆盖距离 主小区覆盖方向120度内最近的3个（可设置）非同站且同频段的关联邻小区的平均距离。
	 * 小区理想覆盖距离=(∑_(i=1)^N▒至邻近基站距离)/N
	 * 上式中同一结构层是指只考虑相同层次的小区，例如900小区只考虑周边最近的N个900基站的距离，而不考虑1800基站或者其他底层站、室内站。
	 * 
	 * @param stmt
	 * @param ncsTab
	 *            ncs数据源表
	 * @param ncsId
	 *            需要计算的ncsid
	 * @param cellResTabName
	 *            计算出来的小区结果存放表
	 * @param lowestN
	 *        考虑的n个小区
	 * @return
	 * @author brightming 2014-2-17 上午11:28:02
	 */
	public boolean calculateCellIdealCoverDis(Statement stmt, String ncsTab,
			long ncsId, String cellResTabName,int lowestN);
	
	
	/**
	 * 计算过覆盖系数
	 * @param conn
	 * @param ncsTab
	 * @param ncsId
	 * @param cellResTabName
	 * @param adjustFactor
	 * @return
	 * @author brightming
	 * 2014-2-17 下午2:26:30
	 */
	public boolean calculateCellOverCoverFactor(Statement stmt, String ncsTab,
			long ncsId, String cellResTabName,float adjustFactor);
	
	/**
	 * 计算小区相关指标的总入口
	 * @param conn
	 * @param ncsTab
	 * @param cellResTab
	 * @param clusterTab
	 * @param clusterCellTab
	 * @param ncsIds
	 * @param params
	 * @return
	 * @author brightming
	 * 2014-2-17 下午12:01:17
	 */
	public boolean calculateCellRes(Connection conn,String ncsTab,String cellResTab,String clusterTab,String clusterCellTab,List<Long> ncsIds,Map<String,Object> params);
	
	

	/**
	 * 小区容量破坏分量计算
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param clusterTab
	 * 连通簇表
	 * @param clusterCellTab
	 * 连通簇小区表
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author Liang YJ
	 * 2014-2-15 16:49:25
	 */
	public boolean calculateCapacityDestroy(Connection conn, long ncsDescId,
			String ncsNcellTabName,String clusterTab,String clusterCellTab,String cellResTab);

	/**
	 * 小区覆盖分量
	 * @param conn
	 * 数据库连接
	 * @param ncsDescId
	 * RNO_NCS_DESC_ID字段
	 * @param ncsNcellTabName
	 * ncs 测量得到的邻区的数据表名称
	 * @param clusterTab
	 * 连通簇表
	 * @param clusterCellTab
	 * 连通簇小区表
	 * @param cellResTab
	 * 小区结果表
	 * @return
	 * @author Liang YJ
	 * 2014-2-14 17:49:25
	 */
	public boolean calculateCellCover(Connection conn, long ncsDescId,
			String ncsNcellTabName,String clusterTab,String clusterCellTab,String cellResTab);
	

	/**
	 * 根据ncsDescId查询相关记录
	 * @author Liang YJ
	 * @date 2014-2-18 下午5:46:37
	 * @param ncsId
	 * 	数据源表中的NCS_ID字段
	 * @return
	 */
	public List<RnoNcsCell> getNcsCellByNcsDescId(long ncsId);


	/**
	 * 自动匹配ncs文件对应的bsc、频段
	 * @param connection
	 * @param ncsTabName
	 * ncs邻区测量数据源名称
	 * @param ncsDescTabName
	 * ncs描述表名称，将被更新
	 * @param ncsIds
	 * @author brightming
	 * 2014-2-27 下午4:33:01
	 */
	public boolean matchNcsBscAndFreqSection(Connection connection, String ncsTabName,
			String ncsDescTabName, List<Long> ncsIds);
	
	/**
	 * 将指定的NCS数据，从RNO_NCS表转移到RNO_NCS表
	 * @param stmt
	 * @param ncsIds
	 * @param saveNcsId
	 * 转移过临时表后，统一的ncsid
	 * @return
	 * @author brightming
	 * 2014-3-23 上午10:45:32
	 */
	public boolean transferNcsToTempTable(Statement stmt, List<Long> ncsIds,
			long saveNcsId,Map<String,String> configs,Map<String,String> result);
	
	/**
	 * 标识RNO_NCS_MID中的小区、邻区是否室分的信息字段
	 * @param stmt
	 * @return
	 * @author brightming
	 * 2014-3-23 上午10:53:29
	 */
	public boolean markIsIndoorFlag(Statement stmt);
	
	/**
	 * 挑选问题小区
	 * @param stmt
	 * @param cityId
	 * 城市id
	 * @author brightming
	 * 2014-3-24 下午4:28:26
	 */
	public boolean pickCellsWithProblem(Statement stmt,long cityId);
	
	/**
	 * 汇总分析的入口调用
	 * @param connection
	 * @param stmt
	 * @param savePath
	 * 最终的保存路径
	 * @param cityId
	 * @param oriNcsIds
	 * 需要处理的原始的ncsid列表
	 * @return
	 * flag:true/false
	 * msg:如果flag为false，则为详细的失败消息
	 * @author brightming
	 * 2014-3-27 下午4:57:28
	 */
	public Map<String, Object> doNcsAnalysis(Connection connection,
			Statement stmt, String savePath, long cityId, List<Long> oriNcsIds,long taskId);
	
	/**
	 * 获取指定小区的干扰矩阵数据
	 * @param dir
	 * @param cellName
	 * @return
	 * @author brightming
	 * 2014-6-18 下午5:37:57
	 */
	public CellInterWithDetailInfo getCellInterDetailInfo(String dir,String cellName);

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
	public List<Map<String, Object>> getRnoTrafficRendererConfig(String curCodeValue);

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
/*	public boolean calculateInterferMartix(Connection connection,
			Statement stmt, long martixRecId, String savePath, long cityId,
			String startMeaDate, String endMeaDate, JobWorker worker,
			JobReport report);*/
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
			String mrrDescTabName, String mrrId);


	/**
	 * 检查mrr文件对应区域是否符合要求
	 * @param mrrId
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23上午11:47:26
	 */
	public boolean checkMrrArea(Connection connection, long mrrId, long cityId);

}
