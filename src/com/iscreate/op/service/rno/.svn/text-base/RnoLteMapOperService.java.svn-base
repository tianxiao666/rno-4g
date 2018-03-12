package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.LteCellPciAnalysisQueryResult;
import com.iscreate.op.action.rno.model.LteCellPciCollAndConfQueryResult;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.pojo.rno.RnoLteCell;

public interface RnoLteMapOperService {

	/**
	 * 获取用户可访问的指定级别的区域
	 */
	public List<Area> ltefindAreaInSpecLevelListByUserId(String accountId,
			String areaLevel);

	/**
	 * 分页获取区/县的ltecell
	 * 
	 * @param areaId 指定区域
	 * @param page 分页参数
	 * @author peng.jm
	 * 2014-5-15 16:50:37
	 */
	public LteCellQueryResult getLteCellByPage(long areaId, Page page);

	/**
	 * 通过Lte小区id获取详细信息
	 * @author chao.xj
	 * 2015-7-1 14:30:23
	 */
	public RnoLteCell getLteCellDetail(long lcid);
	/**
	 * 
	 * @title 获取Lte的PCI复用分析数据
	 * @param areaId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:01:57
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public LteCellPciAnalysisQueryResult getPciReuseAnalysisByPage(
			long areaId, Page page);
	/**
	 * 
	 * @title 通过pci获取相同pci的小区
	 * @param reuseLcid
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:03:28
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getCellsWithSamePciDetails(
			long reuseLcid);
	/**
	 * 
	 * @title 获取PCI冲突与混淆的小区数据
	 * @param areaId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-1上午11:05:32
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public LteCellPciCollAndConfQueryResult getCollAndConfCellWithSamePciByPage(
			long areaId, Page page);
}
