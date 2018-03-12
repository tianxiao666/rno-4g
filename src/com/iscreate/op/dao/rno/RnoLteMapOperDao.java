package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoLteCell;


public interface RnoLteMapOperDao {


	/**
	 * 获取指定区/县区域下的小区
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * 2014-5-15 17:12:09
	 */
	public List<RnoLteCell> getRnoLteCellInArea(long areaId);

	/**
	 * 通过Lte小区id获取该小区详情
	 * @author chao.xj
	 * 2015-7-1 14:33:29
	 */
	public RnoLteCell getLteCellById(long lcid);
	/**
	 * 
	 * @title 通过区域id获取Pci总数
	 * @param areaId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:09:34
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getPciCount(final long areaId);
	/**
	 * 
	 * @title 通过区域id获取Pci数据
	 * @param areaId
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:07:53
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Long> getPciList(final long areaId, final int startIndex, final int cnt);
	/**
	 * 
	 * @title 通过pci获取相同pci的小区
	 * @param reuseAreaId
	 * @param reusePci
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:16:26
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getCellsByPci(long reuseAreaId,
			long reusePci);
}
