package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.dao.rno.RnoStructAnaV2Impl.CellForMatch;
import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.service.rno.parser.MrrParserContext;
import com.iscreate.op.service.rno.parser.vo.NcsAdmRecord;

public interface RnoStructAnaV2 {

	/**
	 * 准备处理爱立信Ncs分析所需要的城市数据信息
	 * @param stmt
	 * @param tabName
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-8-19 上午11:58:11
	 */
	public ResultInfo prepareEriCityCellData(Statement stmt,String tabName,long cityId);
	
	/**
	 * 邻区匹配
	 * @param stmt
	 * @param ncsTabName
	 * @param cellTab
	 * @param ncsId
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-8-19 下午12:46:29
	 */
	public ResultInfo matchEriNcsNcell(Statement stmt, String ncsTabName,String cellTab,
			 long cityId);
	
	
	/**
	 * 自动匹配bsc和频段信息
	 * @param stmt
	 * @param ncsTabName
	 * @param cellTab
	 * @param descTable
	 * @param ncsId
	 * @return
	 * @author brightming
	 * 2014-8-19 下午12:46:21
	 */
	public ResultInfo matchEriNcsBscAndFreqSection(Statement stmt,String ncsTabName,String cellTab, String descTable, long ncsId);
	
	/**
	 * 准备c/i,c/a分子
	 * @param statement
	 * @param ncsTable
	 * @param admRec
	 * @return
	 * @author brightming
	 * 2014-8-20 下午2:21:58
	 */
	public ResultInfo prepareCIAndCADivider(Statement statement, String ncsTable,NcsAdmRecord admRec) ;

	/**
	 * 华为ncs中的邻区匹配
	 * @param stmt
	 * @param string
	 * @param string2
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-8-23 下午6:45:29
	 */
	public ResultInfo matchHwNcsNcell(Statement stmt, String string,
			String string2, long cityId);

	/**
	 * 自动匹配bsc和频段
	 * @param stmt
	 * @param string
	 * @param string2
	 * @return
	 * @author brightming
	 * 2014-8-23 下午6:46:05
	 */
	public ResultInfo matchHwNcsBscAndFreqSection(Statement stmt,
			String string, String string2);

	/**
	 * 计算华为ncs的c/i,c/a分子
	 * @param stmt
	 * @param string
	 * @return
	 * @author brightming
	 * 2014-8-23 下午6:46:39
	 */
	public ResultInfo prepareHwCIAndCADivider(Statement stmt, String string);
	
	
	/**
	 * 生成华为ncs描述信息
	 * @param conn
	 * @param stmt
	 * @param ncsTab
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-8-24 下午3:06:44
	 */
	public ResultInfo generateHwNcsDescRec(Connection conn, Statement stmt,
			String ncsTab, long cityId);

	/**
	 * 生成华为ncs的描述id
	 * @param rateType
	 * FULL,HALF
	 * @param mrrtab
	 * @param descTab
	 * @param stmt
	 * @param conn
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-8-25 下午6:03:56
	 */
	public ResultInfo generateHwMrrDesc(String rateType,String mrrtab,String descTab, Statement stmt,Connection conn,long cityId);

	/**
	 * 检查爱立信mrr文件对应区域是否符合要求
	 * @param mrrId
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23上午11:47:26
	 */
	public boolean checkMrrArea(Connection connection, long mrrId, long cityId);
	/**
	 * 检查华为mrr文件对应区域是否符合要求
	 * @param connection
	 * @param context
	 * @return
	 * @author peng.jm
	 * @date 2014-12-8下午01:56:26
	 */
	public boolean checkHwMrrArea(Connection connection,
			MrrParserContext context);
	/**
	 * 检查爱立信fas文件对应区域是否符合要求
	 * @param connection
	 * @param context
	 * @return
	 * @author peng.jm
	 * @date 2015年1月16日17:28:57
	 */
	public boolean checkEriFasArea(Connection connection, long mrrId, long cityId);
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
	public ResultInfo matchMrrBsc(Connection connection, String mrrTabName,
			String mrrDescTabName, String mrrId);
	/**
	 * 自动匹配fas文件对应的bsc
	 * @param connection
	 * @param fasTabName
	 * @param fasDescTabName
	 * @param fasId
	 * @author peng.jm
	 * @date 2015年1月19日10:26:01
	 */
	public ResultInfo matchFasBsc(Connection connection, String fasTabName,
			String fasDescTabName, String fasId);	
	/**
	 * 清除邻区匹配的缓存数据
	 * 
	 * @author brightming
	 * 2014-9-15 下午4:24:11
	 */
	public void clearMatchCellContext();
	
	/**
	 * 获取邻区匹配所需要的信息
	 * @param stmt
	 * @param city_id
	 * @return
	 * @author brightming
	 * 2014-9-15 下午4:24:32
	 */
	public CellForMatch getMatchCellContext(Statement stmt,long city_id);
	
	
	/**
	 * 根据小区基础信息，计算小区理想覆盖距离
	 * 
	 * @param stmt
	 * @param cityId
	 * @param coverAngle
	 *            服务小区覆盖方向的多少度覆盖范围，默认120
	 * @param refCellCnt
	 *            参考的临近基站数量，默认3
	 * @param force
	 * 				是否强制计算
	 * @return
	 * @author brightming 2014-9-22 上午11:45:00
	 */
	public ResultInfo calculateCellIdealDis(Connection conn, long cityId,int coverAngle,int refCellCnt,boolean force);
}
