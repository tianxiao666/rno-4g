package com.iscreate.op.service.rno;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Hw2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GNcellQueryCond;
import com.iscreate.op.service.rno.parser.jobrunnable.HwNcsParserJobRunnable.DBFieldToTitle;

public interface Rno2GHwCellManageService {

	/**
	 * 
	 * @title 通过区域查询BSC/非hibernate
	 * @param stmt
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:14:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Integer> queryBscByCityId(Statement stmt,long cityId);
	/**
	 * 
	 * @title 查询符合条件的华为2G小区描述记录数量
	 * @param hw2gCellDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:18:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwCellDescCnt(Hw2GCellDescQueryCond hw2gCellDescQueryCond);
	/**
	 * 
	 * @title 分页查询符合条件的华为2G小区描述记录
	 * @param hw2GCellDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:19:04
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwCellDescByPage(
			Hw2GCellDescQueryCond hw2GCellDescQueryCond, Page newPage);
	/**
	 * 
	 * @title 获取华为2G小区、邻区字段参数集合
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:05:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public Map<String, Map<String,DBFieldToTitle>> getHw2GCellAndNcellParams();
	/**
	 * 
	 * @title  通过城市ID查询所在地的BSC集合信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:25:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscListByCityId(long cityId);
	/**
	 * 
	 * @title 查询某市最近一个月的华为2G小区的日期信息
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:27:39
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelyOneMonthOfHw2GCellDateInfo(final long cityId);
	/**
	 * 
	 * @title 查询符合条件的华为2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:36:54
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GCellCnt(final Hw2GCellQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:38:33
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GCellByPage(
			final Hw2GCellQueryCond cond,final Page page);
	/**
	 * 
	 * @title 查询符合条件的华为2G小区邻区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:46:11
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHw2GNcellCnt(final Hw2GNcellQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区邻区的记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午11:47:21
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHw2GNcellByPage(
			final Hw2GNcellQueryCond cond,final Page page);
	/**
	 * 
	 * @title 导出华为2G小区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportHw2GCellData(final String path,final Hw2GCellQueryCond cond,final long cnt);
	/**
	 * 
	 * @title 导出华为2G小区邻区结果数据总入口
	 * @param path
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:40:26
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String exportHw2GCellNcellData(final String path,final Hw2GNcellQueryCond cond,final long cnt);
	/**
	 * 
	 * @title 查询华为小区结果文件进度
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
	 * @title 获取华为小区导出任务的文件路径
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
	 * @title 查询某市最近若干个月的华为2G小区的日期信息
	 * @param cityId
	 * @param monthNum 为负整数
	 * @return
	 * @author chao.xj
	 * @date 2014-11-12下午3:36:19
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryLatelySeveralMonthOfHw2GCellDateInfo(final long cityId,final int monthNum);
}
