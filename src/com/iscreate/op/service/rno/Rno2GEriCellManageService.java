package com.iscreate.op.service.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GCellChannelQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;

public interface Rno2GEriCellManageService {

	/**
	 * 
	 * @title 通过区域查询BSC/hibernate
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15上午10:18:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(
			final long cityId);
	/**
	 * 
	 * @title 通过区域查询BSC/非hibernate
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15下午4:38:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(Statement stmt,long cityId);
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区描述记录数量
	 * @param eri2GCellDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:32:46
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEriCellDescCnt(Eri2GCellDescQueryCond eri2GCellDescQueryCond);
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G小区描述记录
	 * @param eri2GCellDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:32:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEriCellDescByPage(
			Eri2GCellDescQueryCond eri2GCellDescQueryCond, Page newPage);
	/**
	 * 
	 * @title 获取爱立信2G小区、信道组、邻区字段参数集合
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午1:56:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Map<String,DBFieldToLogTitle>> getEri2GCellChannelNcellParams();
	/**
	 * 
	 * @title 通过城市ID查询所在地的BSC集合信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午2:12:45
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscListByCityId(long cityId);
	/**
	 * 
	 * @title 查询某市最近一个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-23下午3:21:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfEri2GCellDateInfo(final long cityId);
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellCnt(final Eri2GCellQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G 小区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellByPage(
			final Eri2GCellQueryCond cond,final Page page);
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区信道组的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GCellChannelCnt(final Eri2GCellChannelQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G 小区信道的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GCellChannelByPage(
			final Eri2GCellChannelQueryCond cond,final Page page);
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区邻区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:19:15
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEri2GNcellCnt(final Eri2GNcellQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G 小区邻区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-24上午9:20:38
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEri2GNcellByPage(
			final Eri2GNcellQueryCond cond,final Page page);
	/**
	 * 
	 * @title 导出爱立信2G小区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellData(final String path,final Eri2GCellQueryCond cond,final long cnt);
	/**
	 * 
	 * @title 导出爱立信2G小区信道结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellChannelData(final String path,final Eri2GCellChannelQueryCond cond,final long cnt);
	/**
	 * 
	 * @title 导出爱立信2G小区邻区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportEri2GCellNcellData(final String path,final Eri2GNcellQueryCond cond,final long cnt);
	/**
	 * 
	 * @title 查询爱立信小区结果文件进度
	 * @param token
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:01:40
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Object> queryExportProgress(String token);
	/**
	 * 
	 * @title 获取爱立信小区导出任务的文件路径
	 * @param token
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午3:02:22
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String queryExportTokenFilePath(String token);
	/**
	 * 
	 * @title 确认符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午10:02:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long confirmEri2GCellCnt(final Eri2GCellQueryCond cond);
	/**
	 * 
	 * @title 删除2G爱立信小区重复数据根据日期及市ID
	 * @param stmt
	 * @param meaDate
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午10:20:59
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean rmvEri2GCellRepeatingData(Statement stmt, String meaDate,
			long cityId);
	/**
	 * 
	 * @title 查询某市最近若干个月的爱立信2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfEri2GCellDateInfo(final long cityId,final int monthNum);
}
