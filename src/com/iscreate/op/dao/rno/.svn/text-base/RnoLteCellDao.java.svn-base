package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;

public interface RnoLteCellDao {
	
	
	/**
	 * 计算满足要求的小区总数量
	 * @param cond
	 * @return
	 * @author brightming
	 * 2014-5-19 下午2:03:25
	 */
	public long getLteCellCount(LteCellQueryCondition cond);
	
	
	
	/**
	 * 分页查询LTE小区信息
	 * @param cond
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author brightming
	 * 2014-5-19 下午2:01:57
	 */
	public List<Map<String, Object>> queryLteCellByPage(LteCellQueryCondition cond,
			long startIndex,long cnt);
	/**
	 * 获取小区详情
	 * @param lteCellId
	 * @return
	 * @author brightming
	 * 2014-5-19 下午1:41:05
	 */
	public Map<String,Object> getLteCellDetail(long lteCellId);
	
	/**
	 * 修改lte小区信息
	 * @param lteCellId
	 * @param lteCell
	 * @return
	 * @author brightming
	 * 2014-5-19 下午1:42:05
	 */
	public boolean modifyLteCellDetail(long lteCellId,Map<String,Object> lteCell);



	/**
	 * 查询指定小区的详情，以及与该小区同站的其他小区的详情。
	 * 第一个为目标小区，后面的为其他的小区。
	 * @param lteCellId
	 * @return
	 * @author brightming
	 * 2014-5-19 下午3:55:54
	 */
	public List<Map<String, Object>> queryLteCellAndCositeCells(long lteCellId);



	/**
	 * 删除指定的lte 小区
	 * @param ids
	 * @return
	 * @author brightming
	 * 2014-5-21 上午11:25:46
	 */
	public boolean deleteRnoLteCellByIds(String ids);
	/**
	 * 
	 * @title 计算满足要求的邻区总数量
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:05:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getLteNcellCount(LteCellQueryCondition cond);
	/**
	 * 
	 * @title 分页查询LTE邻区信息
	 * @param cond
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:05:11
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteNcellByPage(
			LteCellQueryCondition cond, int startIndex, int cnt);
	/**
	 * 
	 * @title 删除指定的lte邻区 id
	 * @param ids
	 * @return
	 * @author chao.xj
	 * @date 2015-6-30下午3:07:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean deleteRnoLteNcellByIds(final String ids);
	/**
	 * 
	 * @title 判断两个小区是否为邻区关系
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:20:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean getNCellById(
			String lteCellId1, String lteCellId2);
	/**
	 * 
	 * @title 判断两个小区是否跟同一个小区存在邻区关系
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:20:36
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean getNCellInOneCellByNcellId(
			String lteCellId1, String lteCellId2);
	/**
	 * 
	 * @title 通过两个小区id获取这两个小区的serving小区的数据
	 * @param lteCellId1
	 * @param lteCellId2
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:21:01
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getServingCellByNcellId(
			String lteCellId1, String lteCellId2);
}
