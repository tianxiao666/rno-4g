package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Hw2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Hw2GCellQueryCond;
import com.iscreate.op.action.rno.model.Hw2GNcellQueryCond;

public interface Rno2GHwCellManageDao {
	/**
	 * 
	 * @title 通过区域查询BSC
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:03:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscByCityId(long cityId);
	/**
	 * 
	 * @title 查询符合条件的华为2G小区的描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:05:24
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwCellDescCnt(final Hw2GCellDescQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的华为2G 小区的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-27上午10:06:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHwCellDescByPage(
			final Hw2GCellDescQueryCond cond,final Page page);
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
	 * @title 通过组装sql语句输出华为相关数据
	 * @param comSql
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:29:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwData(String comSql);
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
