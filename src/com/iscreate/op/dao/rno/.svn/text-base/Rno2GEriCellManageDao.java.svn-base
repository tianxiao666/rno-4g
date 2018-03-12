package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GCellChannelQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellDescQueryCond;
import com.iscreate.op.action.rno.model.Eri2GCellQueryCond;
import com.iscreate.op.action.rno.model.Eri2GNcellQueryCond;

public interface Rno2GEriCellManageDao {

	
	/**
	 * 
	 * @title 通过区域查询BSC
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2014-10-15上午10:18:07
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryBscByCityId(
			final long cityId);
	/**
	 * 
	 * @title 查询符合条件的爱立信2G小区的描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:26:03
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryEriCellDescCnt(final Eri2GCellDescQueryCond cond);
	/**
	 * 
	 * @title 分页查询符合条件的爱立信2G 小区的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-10-16下午10:24:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryEriCellDescByPage(
			final Eri2GCellDescQueryCond cond,final Page page);
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
	 * @title 通过组装sql语句输出爱立信相关数据
	 * @param comSql
	 * @return
	 * @author chao.xj
	 * @date 2014-11-8下午2:29:05
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEriData(String comSql);
	/**
	 * 
	 * @title 确认符合条件的爱立信2G小区的数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-7上午9:59:37
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long confirmEri2GCellCnt(final Eri2GCellQueryCond cond);
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
