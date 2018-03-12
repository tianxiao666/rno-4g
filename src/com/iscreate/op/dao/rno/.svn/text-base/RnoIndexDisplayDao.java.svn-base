package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GFasQueryCond;
import com.iscreate.op.action.rno.model.Eri2GMrrQueryCond;
import com.iscreate.op.action.rno.model.LteStsIndexQueryCond;


public interface RnoIndexDisplayDao {

	/**
	 * 
	 * @title 查询爱立信2G小区MRR指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2014-11-18上午10:22:41
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryEri2GCellMrrIndex(
			final Eri2GMrrQueryCond cond);
	/**
	 * 
	 * @title 查询爱立信2G小区FAS指标
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-2-2上午10:17:27
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryEri2GCellFasIndex(
			final Eri2GFasQueryCond cond);
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:53:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteStsIndex(final LteStsIndexQueryCond cond);
	/**
	 * 根据城市，时间，数据列，小区列，分页查询LTE小区的STS指标
	 * @param cond，page
	 * @return
	 * @author chen.c10
	 * @date 2015-9-18 下午7:48:00
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryLteStsIndexByPage(final LteStsIndexQueryCond cond,final Page page);
	/**
	 * 根据城市，时间，数据列，小区列，查询LTE小区的STS指标总数
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015年9月21日 下午2:37:16
	 * @company 怡创科技
	 * @version V1.0
	 */
	public int queryLteStsIndexCnt(LteStsIndexQueryCond cond);
}
