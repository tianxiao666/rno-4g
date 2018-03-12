package com.iscreate.op.service.rno.mapreduce.pci;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PciCalc {
	// 方案优化结束的方式
	private enum PLAN_FINISH_WAY {PLAN_TYPE_ONE, PLAN_TYPE_TWO, SCHEME_TYPE_ONE, SCHEME_TYPE_TWO};

	private static Logger logger = LoggerFactory.getLogger(PciCalc.class);

	// 配置
	private PciConfig config = null;

	PciUtil pciUtil = null;

	/** 当前正在计算的方案 **/
	PlanObj bestPlan = new PlanObj();

	/** 小区与同站其他小区列的映射，同站其他小区已按关联度从大到小排列 **/
	Map<String, List<String>> cellToSameStationOtherCells = new HashMap<String, List<String>>();

	/** 小区与非同站小区列表的映射，非同站小区已按关联度从大到小排列 **/
	Map<String, List<String>> cellToNotSameStationCells = new HashMap<String, List<String>>();

	/** 小区与邻区关联度的映射（包含了同站其他小区） **/
	Map<String, Map<String, Double>> cellToNcellAssocDegree = new HashMap<String, Map<String, Double>>();

	/** 小区与小区总关联度的映射 **/
	Map<String, Double> cellToTotalAssocDegree = new HashMap<String, Double>();

	/** 小区与原PCI的映射 **/
	Map<String, Integer> cellToOriPci = new HashMap<String, Integer>();

	/** 小区与原始总干扰值的映射 **/
	Map<String, Double> cellToOriInterVal = new HashMap<String, Double>();

	/** 对 PCI 的分配次数进行记录，用于干扰值相同时 PCI 的判断依据。**/
	Map<Integer, Integer> pciToAllocAmount = new HashMap<Integer, Integer>();

	// 同站其他小区分配 PCI 的列表，由于在递归中使用，所有必须是静态全局变量
	private static List<String> pciAllocList = null;

	// 收敛方案最终的差值比例或增均方差，用于返回信息
	double lastInterRate = 0.0;
	double lastVariance = 0.0;

	public PciCalc(PciConfig config) {
		this.config = config;

		// 初始化 PCI 分配次数，初始值都为0
		for (int i = 0; i <= 503; i++) {
			pciToAllocAmount.put(i, 0);
		}
	}

	public void setCellToNotSameStationCells(Map<String, List<String>> cellToNotSameStationCells) {
		this.cellToNotSameStationCells = cellToNotSameStationCells;
	}

	public void setCellToSameStationOtherCells(Map<String, List<String>> cellToSameStationOtherCells) {
		this.cellToSameStationOtherCells = cellToSameStationOtherCells;
	}

	public void setCellToNcellAssocDegree(Map<String, Map<String, Double>> cellToNcellAssocDegree) {
		this.cellToNcellAssocDegree = cellToNcellAssocDegree;
	}

	public void setCellToTotalAssocDegree(Map<String, Double> cellToTotalAssocDegree) {
		this.cellToTotalAssocDegree = cellToTotalAssocDegree;
	}

	public void setCellToOriPci(Map<String, Integer> cellToOriPci) {
		this.cellToOriPci = cellToOriPci;
	}

	public void execCalc() {
		// 对所有主小区对应的同站其他小区列表按总关联度从大到小排序
		sortCellToSameStationOtherCells();

		// 对所有主小区按总关联度从大到小排序
		List<String> descAllCellsByAssocDegree = new ArrayList<String>();
		Map<String, Double> descCellToRelaTotVal = sortMapByValue(cellToTotalAssocDegree);
		for (String cellId : descCellToRelaTotVal.keySet()) {
			descAllCellsByAssocDegree.add(cellId.intern());
		}
		logger.info("主小区数量descAllCellsByAssocDegree.size="+descAllCellsByAssocDegree.size());
		// 获取变小区关联度从大到小排序的队列（这里可以排除没有测量数据的变小区）
		List<String> descNeedToAssignCellList = new ArrayList<String>();
		for (String cellId : descAllCellsByAssocDegree) {
			if (config.getCellsNeedtoAssignList().contains(cellId)) {
				descNeedToAssignCellList.add(cellId.intern());
			}
		}
		logger.info("变小区数量descNeedToAssignCellList.size="+descNeedToAssignCellList.size());
		// 在首套方案中先为所有主小区分配原始PCI
		assignOriPciByDescCells(bestPlan, descAllCellsByAssocDegree);
		// 查看分配原始 pci 后 pci 分配资料情况

		// 计算使用原始PCI时各小区的原始干扰值和原始方案的干扰总值
		double oriPciTotalInterVal = calOriPciTotalInterVal();
		logger.info("原始方案总干扰值：oriPciTotalInterVal=" + oriPciTotalInterVal);

		// 首次变小区PCI分配，按变小区关联度大小顺序分配，生成首套方案。
		assignPciToCellsFirstPlan(bestPlan, descNeedToAssignCellList);

		// 计算首套方案的每小区干扰值
		bestPlan.calInterVal(cellToNotSameStationCells);

		// 计算首套方案总干拢值
		double minTotalInterVal = bestPlan.getTotalInterVal();
		logger.info("首套方案总干拢值：minTotalInterVal=" + minTotalInterVal);

		// 缓存第一次方案，并以文件形式保存到hdfs上
		savePlanInHdfs(bestPlan, "backup");

		PlanObj plan = null;
		int f = 1;
		int topIndex = 0;
		boolean isCalFinish = false;

		// 以何种方案结束，用于返回信息。
		PLAN_FINISH_WAY planFinishWay;

		if (config.getSchemeType().equals("ONE")) {
			planFinishWay = PLAN_FINISH_WAY.SCHEME_TYPE_ONE;
		} else {
			planFinishWay = PLAN_FINISH_WAY.SCHEME_TYPE_TWO;
		}

		List<String> minInterValCellList = new ArrayList<String>();

		double lastTotalInterVal = 0.0;

		lastTotalInterVal = minTotalInterVal;

		// 收敛判断
		while (!isConvergence()) {
			logger.info("方案评估循环；{}", f++);

			// 创建新的方案
			plan = bestPlan.clone();

			// 清除需要重新分配的小区,才能重新分配 pci
			// plan.clearCellsInfo(topCellList);

			// 获得 Top 小区表
			List<String> topCellList = getTopCellListByInter(plan);
			logger.debug("topCellList=" + topCellList);

			if (logger.isDebugEnabled()) {
				for (String cellId : topCellList) {
					logger.debug("cellId={}, interVal={}", cellId, plan.getInterValByCell(cellId));
				}
			}

			minInterValCellList.clear();
			minInterValCellList.addAll(topCellList);

			// 重新分配 pci 给 topCellList 的小区
			assignPciToCellsPlan(plan, topCellList);

			// 方案评估，总干拢值越小越好
			while (true) {

				// 计算当前方案的每个小区对应的干扰值
				plan.calInterVal(cellToNotSameStationCells);

				// 当前方案的总干拢值
				double currentTotalInterVal = plan.getTotalInterVal();

				logger.info("循环分配中，最小干扰总值：" + minTotalInterVal);
				logger.info("循环分配中，上次干扰总值：" + lastTotalInterVal);
				logger.info("循环分配中，当前干扰总值：" + currentTotalInterVal);

				if (currentTotalInterVal < minTotalInterVal) {
					// 新方案全网干拢值优于旧方案，替换旧方案
					// 保存总干扰值低于上一次的方案
					savePlanInHdfs(plan, "current");

					// 备份方案文件，一份当前方案文件以current结尾，一份备份文件以backup结尾
					backupPlanFile();

					// 最小总干扰值设置为当前的总干扰值
					minTotalInterVal = currentTotalInterVal;

					// 当前的最好方案
					bestPlan = plan.clone();

					break;
				} else {

					// 产生新的 Top 小区列表（按干扰总值从大到小排序）
					List<String> newTopCellList = getTopCellListByInter(plan);

					if (config.getPlanType().equals("ONE")) {
						// 评估方案1
						// 排名是否有变化
						if (topCellList.size()>0&&isRankingChanged(topCellList, newTopCellList, topIndex, minTotalInterVal,
								lastTotalInterVal, currentTotalInterVal, minInterValCellList)) {
							// 总干扰值更差了，但最差的小区的提名提升了，用新列表，继续对当前位置小区进行优化。
							logger.info("排名有变化，topIndex={}, topCellList={}, newTopCellList={}", topIndex,
									topCellList.get(topIndex), newTopCellList.get(topIndex));

							topCellList = newTopCellList;
							assignPciToCellsPlan(plan, topCellList);
						} else {
							// 总干扰值更差了，并且最差的小区提名没有提升，用回原来列表，并对下一个位置小区进行优化。
							// 当 TOP 列表最后一个都优化后，优化计算结束。
							topIndex++;

							if (topIndex < topCellList.size()) {
								logger.info("排名无变化，用 TOP" + (topIndex + 1) + " 进行计算。");
								assignPciToCellsPlan(plan, topCellList, topCellList.get(topIndex));
							} else {
								logger.info("排名无变化，TopList 已分配完，计算结束。");
								isCalFinish = true;
								planFinishWay = PLAN_FINISH_WAY.PLAN_TYPE_ONE;
								break;
							}
						}
						lastTotalInterVal = currentTotalInterVal;
					} else {
						// 评估方案2
						// 保持原方案，对 TOP2 小区进行优化
						topIndex++;

						if (topIndex < topCellList.size()) {
							logger.info("选用评估方案2，用 TOP" + (topIndex + 1) + " 进行计算。");
							assignPciToCellsPlan(plan, topCellList, topCellList.get(topIndex));
						} else {
							logger.info("选用评估方案2，TopList 已分配完，计算结束。");
							isCalFinish = true;
							planFinishWay = PLAN_FINISH_WAY.PLAN_TYPE_TWO;
							break;
						}
					}

				}
			}
			if (isCalFinish)
				break;
		}

		// 输出当前方案之前，以最佳方案进行邻区核查。
		checkNCellPci(bestPlan, descNeedToAssignCellList);

		// 保存最终方案
		savePlanInHdfs(bestPlan, "current");

		// 保存返回信息
		saveReturnInfoInHdfs(planFinishWay);
	}

	/**
	 *  对所有主小区对应的同站其他小区列表按总关联度从大到小排序
	 */
	private void sortCellToSameStationOtherCells() {
		String tmpCellId = "";
		for (String cellId : cellToSameStationOtherCells.keySet()) {
			for (int i = 0; i < cellToSameStationOtherCells.get(cellId).size(); i++) {
				for (int j = i + 1; j < cellToSameStationOtherCells.get(cellId).size(); j++) {
					String ocellId1 = cellToSameStationOtherCells.get(cellId).get(i);
					String ocellId2 = cellToSameStationOtherCells.get(cellId).get(j);
					if (cellToTotalAssocDegree.get(ocellId1) == null || cellToTotalAssocDegree.get(ocellId2) == null)
						continue;
					if (cellToTotalAssocDegree.get(ocellId1) < cellToTotalAssocDegree.get(ocellId2)) {
						tmpCellId = ocellId1;
						cellToSameStationOtherCells.get(cellId).set(i, ocellId2);
						cellToSameStationOtherCells.get(cellId).set(j, tmpCellId);
					}
				}
			}
		}
	}

	/**
	 * Top 小区列表排名是否有变化
	 * @param topCellList 旧的 Top 小区列表
	 * @param newTopCellList 新的 Top 小区列表
	 * @param minInterValCellList
	 * @param newTotalInterVal
	 * @param minTotalInterVal
	 * @param currentTotalInterVal
	 * @return
	 */
	private boolean isRankingChanged(List<String> topCellList, List<String> newTopCellList, int topIndex,
			double minTotalInterVal, double lastTotalInterVal, double currentTotalInterVal,
			List<String> minInterValCellList) {
		if (topCellList.get(topIndex).equals(newTopCellList.get(topIndex))) {
			// 总干扰值更差了，并且最差的小区提名没有提升，对下一个位置小区进行优化。
			return false;
		} else {
			if (currentTotalInterVal == minTotalInterVal) {
				for (int i = 0; i < newTopCellList.size(); i++) {
					if (!newTopCellList.get(i).equals(minInterValCellList.get(i))) {
						return true;
					}
				}
				return false;
			}

			if (currentTotalInterVal <= lastTotalInterVal) {
				return false;
			}
			// 总干扰值更差了，但最差的小区的提名提升了，继续对当前位置小区进行优化。
			return true;
		}
	}

	/**
	 * 收敛判断
	 * @return 是否停止计算
	 */
	private boolean isConvergence() {
		boolean bool = false;
		if (config.getSchemeType().equals("ONE")) {
			// 收敛方法1: 尾数差异收敛算法 -- top n%小区干扰值与中间n%小区干扰值的差值比例 小于等于m%
			double interRate = getInterRate();
			double defInterRate = config.getDefInterRate();
			lastInterRate = interRate;
			logger.info("TOP {}% 小区与中间 {}% 的干扰差值比例={}%, 给定值={}%", config.getTopRate() * 100, config.getTopRate() * 100,
					interRate, defInterRate * 100);
			if (interRate <= defInterRate) {
				logger.info("【top n%小区干扰总值与中间n%小区干扰总值的差值比例 小于等于m%】，终止继续优化");
				bool = true;
			}
		} else {
			double varianceByCurrentPlan = getVarianceByCurrentPlan();
			double defVariance = config.getDefVariance();
			lastVariance = varianceByCurrentPlan;
			// 收敛方法2: 均方差收敛算法 -- 全网以小区数均分为n份，求均方差，当值小于m
			logger.info("全网小区干扰值方差=" + varianceByCurrentPlan + ", 给定值=" + defVariance);
			if (varianceByCurrentPlan < defVariance) {
				logger.info("【求全网小区干扰值方差，当值小于给定值m】，终止继续优化");
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * 计算使用原始PCI的干扰总值
	 * @return 干扰总值
	 */
	private double calOriPciTotalInterVal() {
		// 计算原始PCI的干扰值
		for (String cellId : cellToOriPci.keySet()) {
			int cellPci = cellToOriPci.get(cellId);
			List<String> ncells = cellToNotSameStationCells.get(cellId);
			double interVal = 0;
			if (ncells != null) {
				for (int i = 0; i < ncells.size(); i++) {
					double modVal = 0;
					Integer oldPci = cellToOriPci.get(ncells.get(i));
					if (oldPci != null && oldPci != -1) {
						if (cellPci % 3 == (oldPci % 3)) {
							modVal += config.getM3r();
						}
						if (cellPci % 6 == (oldPci % 6)) {
							modVal += config.getM6r();
						}
						if (cellPci % 30 == (oldPci % 30)) {
							modVal += config.getM30r();
						}
					}
					double relaVal = 0;
					if (cellToNcellAssocDegree.get(cellId) != null) {
						if (cellToNcellAssocDegree.get(cellId).get(ncells.get(i)) != null) {
							relaVal = cellToNcellAssocDegree.get(cellId).get(ncells.get(i));
						}
					}
					interVal += modVal * relaVal;
				}
			}
			cellToOriInterVal.put(cellId.intern(), interVal);
		}

		// 计算原方案的干扰总值
		double oriTotInterVal = 0.0;
		for (String one : cellToOriInterVal.keySet()) {
			oriTotInterVal += cellToOriInterVal.get(one);
		}
		return oriTotInterVal;
	}

	/**
	 * 获到当前方案的 Top 变小区表（按变小区按干扰值从大到小的列表）
	 * @param plan 当前方案
	 * @return
	 */
	private List<String> getTopCellListByInter(PlanObj plan) {
		// 获取当前方案的干扰值小区排序，从大到小
		Map<String, Double> descCellByInterValMap = sortMapByValue(plan.getCellToInterVal());
		logger.info("当前方案总小区个数为 {} 个",descCellByInterValMap.keySet().size());
		// 获取 TopList 表
		List<String> topCellList = new ArrayList<String>();
		// ToList表是变小区的10%
		int size = config.getCellsNeedtoAssignList().size();
		logger.info("变小区表大小为="+size);
		int num = (int) (config.getTopRate() * size);
		logger.info("允许top小区为*{}*个",num);
		int n = 0;
		for (String cellId : descCellByInterValMap.keySet()) {
			if (n == num)break;
			//是变小区才加进 TopList 列表中
			if (config.getCellsNeedtoAssignList().contains(cellId)) {
				logger.info(" {} 为top小区",cellId);
				topCellList.add(cellId.intern());
				n++;
			}
		}
		logger.info("TOP变小区表大小为="+topCellList.size());
		return topCellList;
	}

	/**
	 * 备份current文件，以backup结尾保存在hdfs的同目录上
	 *
	 * @return
	 * @author peng.jm
	 * @date 2015年3月26日15:43:19
	 */
	private void backupPlanFile() {
		String currentFile = config.getFilePath() + "/" + config.getFileName() + "." + "current";
		String backupFile = config.getFilePath() + "/" + config.getFileName() + "." + "backup";

		FileSystem fs = null;
		try {
			fs = FileSystem.get(config.getConf());
		} catch (IOException e1) {
			logger.error("PciReducer过程：backupPlanFile时打开hdfs系统出错！");
			e1.printStackTrace();
		}

		// 创建对应的path
		Path currentFilePath = new Path(URI.create(currentFile));
		Path backupFilePath = new Path(URI.create(backupFile));

		try {
			// 删除backup文件
			if (fs.exists(backupFilePath)) {
				fs.delete(backupFilePath, false);
			}
			// 将current文件命名成backup文件
			fs.rename(currentFilePath, backupFilePath);
		} catch (IOException e) {
			logger.error("PciReducer过程：backupPlanFile出错！");
			e.printStackTrace();
		}

	}

	/**
	 * 保存方案到hdfs文件系统上
	 *
	 * @param plan
	 *            需要保存的方案
	 * @param suffix
	 *            文件后缀（backup:最后一次成功的；current:当前最新）
	 * @return
	 * @author peng.jm
	 * @date 2015年3月26日14:53:26
	 */
	private void savePlanInHdfs(PlanObj plan, String suffix) {
		String realFilePath = config.getFilePath() + "/" + config.getFileName() + "." + suffix;
		logger.info("把方案写入到HDFS文件:" + realFilePath);

		FileSystem fs = null;
		try {
			fs = FileSystem.get(config.getConf());
		} catch (IOException e1) {
			logger.error("PciReducer过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}

		// 先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if (fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			logger.error("PciReducer过程：保存方案时，删除原有文件出错！");
			e2.printStackTrace();
		}

		// 创建新文件
		Path filePathObj = new Path(URI.create(realFilePath));

		// 创建流
		OutputStream dataOs = null;
		try {
			dataOs = fs.create(filePathObj, true);
		} catch (IOException e1) {
			logger.error("PciReducer过程：创建输出数据文件流出错！");
			e1.printStackTrace();
		}

		// 根据规则写入数据
		DataOutputStream dataDOS = new DataOutputStream(dataOs);
		try {
			// 将MR数据和待优化PCI的小区都并在一起输出
			// MR数据存在原始干扰值，计算干扰值，计算PCI
			// 待优化PCI小区中可能存在着部分小区在这批MR数据中并不存在，所以不能进行计算干扰值和分配PCI
			// 这里处理的方法是将MR数据中的存在的所有小区都输出，然后检查待优化小区哪些是不存在于MR数据中的小区
			// 将这些小区也以默认值输出，默认原干扰值0，计算干扰值0，PCI为-1

			// 检查不存在与MR数据的小区
			List<String> cellsNotInData = new ArrayList<String>();
			for (String cellId : config.getCellsNeedtoAssignList()) {
				if (!plan.getCellToPci().containsKey(cellId)) {
					cellsNotInData.add(cellId);
				}
			}
			int size = plan.getCellToPci().size() + cellsNotInData.size();

			// 总干扰值
			dataDOS.writeDouble(plan.getTotalInterVal());
			// 需要优化的小区数量
			dataDOS.writeInt(size);
			// 写入数据中存在的小区PCI以及干扰值
			for (String cellId : plan.getCellToPci().keySet()) {
				// 小区ID
				dataDOS.writeUTF(cellId);
				// 小区计算PCI
				dataDOS.writeInt(plan.getPciByCell(cellId));
				// 小区原干扰值
				if (cellToOriInterVal.get(cellId) != null) {
					dataDOS.writeDouble(cellToOriInterVal.get(cellId));
				} else {
					dataDOS.writeDouble(0.0);
				}
				// 小区计算干扰值
				dataDOS.writeDouble(plan.getInterValByCell(cellId));
			}
			// 写入数据中不存在的小区默认PCI以及干扰值
			for (String cellId : cellsNotInData) {
				dataDOS.writeUTF(cellId);
				dataDOS.writeInt(-1);
				dataDOS.writeDouble(0.0);
				dataDOS.writeDouble(0.0);
			}

			// 结束符，判断文件是否完整
			dataDOS.writeUTF("finished");
		} catch (IOException e) {
			logger.error("PciReducer过程: pci规划方案文件写入内容时失败！");
			try {
				dataDOS.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try {
			dataDOS.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 保存返回信息文件，用于在页面上显示 PCI 优化是以哪种方式结束计算。
	 * @param planFinishWay 方案优化结束方式
	 */
	private void saveReturnInfoInHdfs(PLAN_FINISH_WAY planFinishWay) {
		String realFilePath = config.getFilePath() + "/" + config.getFileName() + ".info";
		logger.info("把返回信息写入到HDFS文件:" + realFilePath);

		FileSystem fs = null;
		try {
			fs = FileSystem.get(config.getConf());
		} catch (IOException e1) {
			System.out.println("PciReducer过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}

		// 先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if (fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			System.out.println("PciReducer过程：保存返回信息文件时，删除原有文件出错！");
			e2.printStackTrace();
		}

		// 创建新文件
		Path filePathObj = new Path(URI.create(realFilePath));

		// 创建流
		OutputStream dataOs = null;
		try {
			dataOs = fs.create(filePathObj, true);
		} catch (IOException e1) {
			System.out.println("PciReducer过程：创建输出数据文件流出错！");
			e1.printStackTrace();
		}

		// 根据规则写入数据
		DataOutputStream dataDOS = new DataOutputStream(dataOs);
		try {
			String info = "<br>";
			if (config.getPlanType().equals("ONE")) {
				info += "评估方案选择了：方案评估1(三步法)；";
			} else {
				info += "评估方案选择了：方案评估2(两步法)；";
			}

			info += "<br>";

			if (config.getSchemeType().equals("ONE")) {
				info += "收敛方式选择了：方案一(根据Top差值比例)；";
			} else {
				info += "收敛方式选择了：方案二(根据求方差)；";
			}

			info += "<br>优化结束方式：";
			switch (planFinishWay) {
			case PLAN_TYPE_ONE:
				info += "方案评估1(三步法)";
				break;
			case PLAN_TYPE_TWO:
				info += "方案评估2(两步法)";
				break;
			case SCHEME_TYPE_ONE:
				info += "收敛方式1(根据Top差值比例)";
				break;
			case SCHEME_TYPE_TWO:
				info += "收敛方式2(根据求方差)";
				break;
			}

			info += "<br>";

			if (config.getSchemeType().equals("ONE")) {
				info += "最终的差值比例：" + lastInterRate + "，差值比例目标值：" + config.getDefInterRate();
			} else {
				info += "最终的均方差：" + lastVariance + "，均方差目标值" + config.getDefVariance();
			}

			dataDOS.writeUTF(info);

			// 结束符，判断文件是否完整
			dataDOS.writeUTF("finished");
		} catch (IOException e) {
			logger.error("PciReducer过程: pci规划返回信息文件写入内容时失败！");
			try {
				dataDOS.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try {
			dataDOS.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 收敛方式1(根据Top差值比例)
	 */
	private double getInterRate() {
		Map<String, Double> descCellByInterVal = sortMapByValue(bestPlan.getCellToInterVal());
		List<Double> descInterVals = new ArrayList<Double>();
		for (String cellId : descCellByInterVal.keySet()) {
			descInterVals.add(descCellByInterVal.get(cellId));
		}
		// top区间
		int start1 = 0;
		int start2 = (int) (descInterVals.size() * config.getTopRate());
		// 中间区间，如果 top 区间是奇数，则 mid 区间加大1
		int mid1 = (int) (descInterVals.size() / 2 - start2 / 2 - (start2 % 2));
		int mid2 = (int) (descInterVals.size() / 2 + start2 / 2);

		double topInterVal = 0;
		for (int i = start1; i < start2; i++) {
			topInterVal += descInterVals.get(i);
		}
		double midInterVal = 0;
		for (int i = mid1; i < mid2; i++) {
			midInterVal += descInterVals.get(i);
		}

		logger.info("topInterVal={}, midInterVal={}", topInterVal, midInterVal);
		double result = (topInterVal - midInterVal) / midInterVal;
		return result;
	}

	/**
	 * 收敛方式2(根据求方差)
	 */
	private double getVarianceByCurrentPlan() {
		// 获取当前方案的干扰值小区排序，从大到小
		Map<String, Double> descCellByInterValMap = sortMapByValue(bestPlan.getCellToInterVal());

		int size = bestPlan.getCellToInterVal().size();
		int div = size / config.getDivideNumber();
		int mod = size % config.getDivideNumber();

		int tmpDevideNumber = config.getDivideNumber();
		if (mod > 0) {
			div = div + 1;
			tmpDevideNumber = tmpDevideNumber - 1;
		}

		List<Double> tmplist = new ArrayList<Double>();
		for (String cellId : descCellByInterValMap.keySet()) {
			tmplist.add(descCellByInterValMap.get(cellId));
		}

		double totVal = 0;
		List<Double> listTotal = new ArrayList<Double>();
		// 以小区数均分为 n 份
		int k = 0;
		for (int i = 0; i < tmpDevideNumber; i++) {
			double total = 0;
			for (int j = 0; j < div; j++) {
				total += tmplist.get(k);
				k++;
			}
			totVal += total;
			listTotal.add(total);
		}

		if (mod > 0) {
			double total = 0;
			for (int i = tmplist.size() - div; i < tmplist.size(); i++) {
				total += tmplist.get(i);
			}
			totVal += total;
			listTotal.add(total);
		}

		// 平均值
		double averageVal = totVal / config.getDivideNumber();

		double val = 0;
		for (double total : listTotal) {
			double tmpVal = total - averageVal;
			val += tmpVal * tmpVal;
		}
		double result = Math.sqrt(val / size);
		return result;
	}

	/**
	 * 首次变小区PCI分配，按变小区关联度大小顺序分配，生成首套方案。
	 * @param plan 首套方案
	 * @param descNeedToAssignCellList 排序后的变小区列表
	 */
	private void assignPciToCellsFirstPlan(PlanObj plan, List<String> descNeedToAssignCellList) {
		// 已手动分配了PCI的小区列表，防止再被重新分配
		List<String> assignedPciCellsList = new ArrayList<String>();

		for (String cellId : descNeedToAssignCellList) {

			// 如果主小区已分配过，则它的同站小区也都分配过，直接跳过。
			if (assignedPciCellsList.contains(cellId))
				continue;

			// 主小区的非同站小区列表
			List<String> notSameStationCells = cellToNotSameStationCells.get(cellId);

			// 同站其他小区
			List<String> sameStationOtherCells = cellToSameStationOtherCells.get(cellId);

			// 依次将0-503个PCI分配给A1小区的，计算非同站小区总干扰值；
			// 得出最小的一个干扰值对应的PCI，若有几个PCI对应干扰值相同，则选择最小值，赋予A1；
			int bestPci = 0;
			double minInterValue = calNotSameStationCellsTotalInterVal(plan, cellId, 0, notSameStationCells);
			for (int pci = 1; pci <= 503; pci++) {
				double currentInterValue = calNotSameStationCellsTotalInterVal(plan, cellId, pci, notSameStationCells);
				if (currentInterValue < minInterValue) {
					minInterValue = currentInterValue;
					bestPci = pci;
				} else if (currentInterValue == minInterValue) {
					// 当干扰值相等时，以 PCI 已被分配次数 pciToAllocAmount 作为分配的依据
					if (pciToAllocAmount.get(pci) < pciToAllocAmount.get(bestPci)) {
						bestPci = pci;
					}
				}
			}

			// 把主小区PCI加到方案中
			plan.addCellToPci(cellId.intern(), bestPci);

			// 给同站其他小区分配 PCI
			selectBestPciForSameStationOtherCell(plan, cellId, bestPci, notSameStationCells, sameStationOtherCells);

			// 把同站小区添加进已分配 PCI 的列表中
			assignedPciCellsList.add(cellId.intern());
			assignedPciCellsList.addAll(sameStationOtherCells);
		}
	}

	/**
	 * 选择最佳的同站其他小区PCI分配，同站其他小区有可能为0~6个
	 */
	private void selectBestPciForSameStationOtherCell(PlanObj plan, String cellId, int bestPci,
			List<String> notSameStationCells, List<String> sameStationOtherCells) {
		pciUtil = new PciUtil();

		int scellsSize = sameStationOtherCells.size();

		if (scellsSize == 0 || scellsSize > 5) {
			// 没有同站其他小区或者同站其他小区数多于5个，不处理直接返回
			return;
		}

		// 根据A1的PCI获取对应PCI组
		List<Integer> pciGroup = pciUtil.getPciGroup(bestPci);

		// 如果同站小区为4~6个时
		if (scellsSize > 2) {
			int nextPci = bestPci + 3;
			if (nextPci > 503)
				nextPci = 0;
			pciGroup.addAll(pciUtil.getPciGroup(nextPci));
		}

		// 在 PCI 组中去除已分配给 A1 的 PCI 值，这里一定要使用 new Integer，否则会认为 bestPci 是 index。
		pciGroup.remove(new Integer(bestPci));

		pciAllocList = new ArrayList<String>();

		allocSameStationOtherCellsPci("", pciGroup, new ArrayList<Integer>(), scellsSize);

		String bestPciString = "";
		double minInterVal = Double.MAX_VALUE;
		for (String strPics : pciAllocList) {
			String[] picArray = strPics.split(",");
			// 分配PCI
			for (int i = 0; i < scellsSize; i++) {
				plan.addCellToPci(sameStationOtherCells.get(i), Integer.parseInt(picArray[i]));
			}
			double interVal = calNotSameStationCellsTotalInterVal(plan, cellId, bestPci, notSameStationCells);
			if (interVal < minInterVal) {
				minInterVal = interVal;
				bestPciString = strPics;
			}
		}
		String[] bestPciArray = bestPciString.split(",");
		for (int i = 0; i < scellsSize; i++) {
			plan.addCellToPci(sameStationOtherCells.get(i), Integer.parseInt(bestPciArray[i]));
		}
		pciAllocList.clear();
		pciAllocList = null;
	}

	/**
	 * 分配同站其他小区的递归算法
	 * @param strPcis 初始调用时设置为空子符串
	 * @param pcis PCI组
	 * @param tmpPcis 初始调用时设置为空列表 new ArrayList<Integer>()
	 * @param length 同站其他小区的个数（1~5个）
	 */
	private void allocSameStationOtherCellsPci(String strPcis, List<Integer> pcis, List<Integer> tmpPcis, int length) {
		if (length == 0) {
			pciAllocList.add(strPcis.substring(0, strPcis.length() - 1));
			return;
		}
		List<Integer> tmpPics2;
		for (int i = 0; i < pcis.size(); i++) {
			tmpPics2 = new ArrayList<Integer>();
			tmpPics2.addAll(tmpPcis);
			if (!tmpPcis.contains(i)) {
				String str = strPcis + pcis.get(i) + ",";
				tmpPics2.add(i);
				allocSameStationOtherCellsPci(str, pcis, tmpPics2, length - 1);
			}
		}
	}

	/**
	 * 计算非同站小区的总干扰值
	 * @param notSameStationCells 非同站小区
	 * @return 总干扰值
	 */
	private Double calNotSameStationCellsTotalInterVal(PlanObj plan, String cellId, int cellPci,
			List<String> notSameStationCells) {

		double interVal = 0;
		if (notSameStationCells != null) {
			for (String nssCell : notSameStationCells) {
				// 如果未分配有原始PCI，缺省干拢值为0
				double modVal = 0;
				Integer oldPci = plan.getPciByCell(nssCell);
				if (oldPci == null || oldPci == -1) {
					oldPci = cellToOriPci.get(nssCell);
				}
				if (oldPci != null && oldPci != -1) {
					if (cellPci % 3 == (oldPci % 3)) {
						modVal += config.getM3r();
					}
					if (cellPci % 6 == (oldPci % 6)) {
						modVal += config.getM6r();
					}
					if (cellPci % 30 == (oldPci % 30)) {
						modVal += config.getM30r();
					}
				}

				double relaVal = 0;
				if (cellToNcellAssocDegree.get(cellId) != null) {
					if (cellToNcellAssocDegree.get(cellId).get(nssCell) != null) {
						relaVal = cellToNcellAssocDegree.get(cellId).get(nssCell);
					}
				}
				interVal += modVal * relaVal;
			}
		}

		return interVal;
	}

	/**
	 * 在非首套方案中分配PCI
	 * @param plan
	 * @param topList
	 * @param cellId
	 */
	private void assignPciToCellsPlan(PlanObj plan, List<String> topList, String cellId) {

		// 获取主小区的非同站小区列表
		List<String> notSameStationCells = cellToNotSameStationCells.get(cellId);

		// 获取同站其他小区
		List<String> sameStationOtherCells = cellToSameStationOtherCells.get(cellId);

		// 依次将0-503个PCI分配给A1小区的，计算非同站小区总干扰值；
		// 得出最小的一个干扰值对应的PCI，若有几个PCI对应干扰值相同，则选择最小值，赋予A1；
		int bestPci = 0;
		double minInterValue = calNotSameStationCellsTotalInterVal(plan, cellId, 0, notSameStationCells);
		for (int pci = 1; pci <= 503; pci++) {
			double currentInterValue = calNotSameStationCellsTotalInterVal(plan, cellId, pci, notSameStationCells);
			if (currentInterValue < minInterValue) {
				minInterValue = currentInterValue;
				bestPci = pci;
			}
		}

		// 把主小区PCI加到方案中
		plan.addCellToPci(cellId.intern(), bestPci);

		// 给同站其他小区分配 PCI
		selectBestPciForSameStationOtherCell(plan, cellId, bestPci, notSameStationCells, sameStationOtherCells);
	}

	private void assignPciToCellsPlan(PlanObj plan, List<String> topList) {
		if (topList != null && topList.size() > 0) {
			assignPciToCellsPlan(plan, topList, topList.get(0));
		}
	}

	/**
	 * 在首套方案中为所有主小区分配原始PCI
	 * @param plan
	 * @param cellList
	 */
	private void assignOriPciByDescCells(PlanObj plan, List<String> cellList) {
		for (int i = 0; i < cellList.size(); i++) {
			String cellId = cellList.get(i).intern();
			// 为当前小区分配原始PCI
			plan.addCellToPci(cellId, cellToOriPci.get(cellId));
			// 为同站其他小区分配原始PCI
			List<String> ocells = cellToSameStationOtherCells.get(cellId);
			if (ocells != null) {
				for (int j = 0; j < ocells.size(); j++) {
					String ocellId = ocells.get(j).intern();
					if (cellToOriPci.get(ocellId) != null) {
						plan.addCellToPci(ocellId, cellToOriPci.get(ocellId));
					}
				}
			}
		}
	}

	/**
	 * 进行邻区核查，为了保证服务小区跟所有邻区之间的pci不相等，相等就+-30来修正
	 * @param plan 当前方案
	 * @param descCellList 小区与邻区列的映射的列表
	 */
	private void checkNCellPci(PlanObj plan, List<String> descCellList) {
		logger.debug("===>>>checkNCellPci():进入pci+-30循环");
		int count = 0;
		// 检查服务小区，邻区之间是否存在同pci,存在则+30或-30
		for (int i = 0; i < descCellList.size(); i++) {
			List<String> cells = cellToNotSameStationCells.get(descCellList.get(i));
			if (cells == null)
				continue;

			// 最多检查前50个非同站小区
			int maxCheck = cells.size();
			if (maxCheck > 50) {
				maxCheck = 50;
			}

			// 加入服务小区,服务小区不能和其他小区相同pci
			cells.add(descCellList.get(i));

			// 获取小区列所使用的pci
			Map<String, Integer> ncellToPci = new HashMap<String, Integer>();
			for (String c : cells) {
				ncellToPci.put(c.intern(), plan.getPciByCell(c));
			}

			for (int k = 0; k < maxCheck; k++) {
				for (int j = 0; j < maxCheck; j++) {
					count++;
					if (count > 1000) {
						logger.info("保证服务小区跟所有邻区之间的pci不相等,修正循环已执行了1000次，break掉！");
						break;
					}
					logger.debug("pci+-30循环中："+count);
					if (k == j)
						continue;
					// 不需要优化的小区，跳过
					if (!config.getCellsNeedtoAssignList().contains(cells.get(k)))
						continue;
					int pci1 = plan.getPciByCell(cells.get(k));
					int pci2 = plan.getPciByCell(cells.get(j));
					if (pci1 == -1 || pci2 == -1)
						continue;
					if (pci1 == pci2) {
						if (503 >= (pci1 + 30)) {
							pci1 += 30;
							while (ncellToPci.containsValue(pci1)) {
								if (503 < (pci1 + 30))
									break;
								pci1 += 30;
							}
							plan.addCellToPci(cells.get(k), pci1);
							ncellToPci.put(cells.get(k).intern(), pci1);
						} else {
							pci1 -= 30;
							while (ncellToPci.containsValue(pci1)) {
								if (pci1 < 30)
									break;
								pci1 -= 30;
							}
							plan.addCellToPci(cells.get(k), pci1);
							ncellToPci.put(cells.get(k).intern(), pci1);
						}
					}
				}
			}
		}

		logger.debug("===>>>checkNCellPci():退出pci+-30循环");
	}

	/**
	 * map根据value值,从大到小排序
	 *
	 * @param unsortMap
	 * @return
	 * @author peng.jm
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Double> sortMapByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});
		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * 将PCI分成168个组，每组三个PCI。
	 */
	class PciUtil {
		// 按照[0,1,2],[3,4,5]....分组
		List<List<Integer>> pciGroups = new ArrayList<List<Integer>>();

		// 当前获取到第几个pci组
		int step = 0;

		public PciUtil() {
			List<Integer> temp = new ArrayList<Integer>();

			for (int i = 0; i <= 503; i++) {
				temp.add(i);
				if ((i + 1) % 3 == 0) {
					pciGroups.add(temp);
					temp = new ArrayList<Integer>();
				}
			}
		}

		/**
		 * 取到 PCI 所在的分组
		 * @param pci PCI 值
		 * @return 所在组的 PCI 列表
		 */
		public List<Integer> getPciGroup(int pci) {
			List<Integer> result = new ArrayList<Integer>();
			for (Integer one : pciGroups.get(pci / 3)) {
				result.add(one);
			}
			return result;
		}

		/**
		 * 按顺序取到一个 PCI 分组
		 * @return 一组的 PCI 列表
		 */
		public List<Integer> getOneGroup() {
			if (step > 167) {
				step = 0;
			}
			// 创建新的list不影响源数据
			List<Integer> result = new ArrayList<Integer>();
			for (Integer one : pciGroups.get(step)) {
				result.add(one);
			}
			step = step + 1;
			return result;
		}
	}

	/**
	 * PCI优化方案
	 */
	class PlanObj implements Cloneable {

		/** 保存已分配 PCI 的小区 **/
		HashMap<String, Integer> cellToPci = new HashMap<String, Integer>();

		/** 保存已计算干拢值的小区 **/
		HashMap<String, Double> cellToInterVal = new HashMap<String, Double>();

		public HashMap<String, Integer> getCellToPci() {
			return cellToPci;
		}

		/**
		 * 获取已计算干拢值的小区MAP
		 * @return 已计算干拢值的小区MAP
		 */
		public HashMap<String, Double> getCellToInterVal() {
			return cellToInterVal;
		}

		public void setCellToInterVal(HashMap<String, Double> cellToInterVal) {
			this.cellToInterVal = cellToInterVal;
		}

		public void addCellToPci(String cellId, Integer pci) {
			// 进行 PCI 分配次数的记录，先为新的 pci 分配次数累加1
			pciToAllocAmount.put(pci, pciToAllocAmount.get(pci) + 1);

			// 再把旧的 pci 分配次数减1
			if (cellToPci.get(cellId) != null) {
				int oldPci = cellToPci.get(cellId);
				pciToAllocAmount.put(oldPci, pciToAllocAmount.get(oldPci) - 1);
			}
			this.cellToPci.put(cellId.intern(), pci);
		}

		public void addCellToInterVal(String cellId, double interVal) {
			this.cellToInterVal.put(cellId.intern(), interVal);
		}

		public int getPciByCell(String cellId) {
			int pci = -1;
			if (isAssigned(cellId)) {
				pci = cellToPci.get(cellId);
			}
			return pci;
		}

		public double getInterValByCell(String cellId) {
			double result = 0;
			if (cellToInterVal.containsKey(cellId)) {
				result = cellToInterVal.get(cellId);
			}
			return result;
		}

		/**
		 * 是否已分配
		 * @param cellId 小区ID
		 * @return
		 */
		public boolean isAssigned(String cellId) {
			boolean flag = false;
			if (cellToPci.containsKey(cellId)) {
				flag = true;
				if (cellToPci.get(cellId) == -1) {
					flag = false;
				}
			}
			return flag;
		}

		/**
		 * 计算当前方案的每个小区对应的干扰值
		 * @param cellToNcells
		 */
		public void calInterVal(Map<String, List<String>> cellToNcells) {
			for (String cellId : cellToPci.keySet()) {
				int cellPci = cellToPci.get(cellId);
				List<String> ncells = cellToNcells.get(cellId);
				if (ncells == null)
					continue;
				double interVal = 0;
				for (int i = 0; i < ncells.size(); i++) {
					interVal += getModValByPciAndCell(cellId, ncells.get(i), cellPci)
							* getRelaValByCell1AndCell2(cellId, ncells.get(i));
				}

				cellToInterVal.put(cellId.intern(), interVal);
			}
		}

		/**
		 *
		 * @param cellId
		 * @param ncellId
		 * @param cellPci
		 * @return
		 */
		public double getModValByPciAndCell(String cellId, String ncellId, int cellPci) {
			double result = 0;
			Integer ncellPci = getPciByCell(ncellId);
			if (ncellPci == null || ncellPci == -1) {
				ncellPci = cellToOriPci.get(ncellId);
			}
			if (ncellPci != null && ncellPci != -1) {
				if (cellPci % 3 == (ncellPci % 3)) {
					result += config.getM3r();
				}
				if (cellPci % 6 == (ncellPci % 6)) {
					result += config.getM6r();
				}
				if (cellPci % 30 == (ncellPci % 30)) {
					result += config.getM30r();
				}
			}
			return result;
		}

		public double getRelaValByCell1AndCell2(String cell1, String cell2) {
			double interVal = 0;
			if (cellToNcellAssocDegree.get(cell1) != null) {
				if (cellToNcellAssocDegree.get(cell1).get(cell2) != null) {
					interVal = cellToNcellAssocDegree.get(cell1).get(cell2);
				}
			}
			return interVal;
		}

		public double getTotalInterVal() {
			double result = 0;
			for (String cellId : cellToInterVal.keySet()) {
				result += cellToInterVal.get(cellId);
			}
			return result;
		}

		/**
		 * 清除需要重新分配的小区,才能重新分配pci
		 * @param descCellList 小区列表
		 */
		public void clearCellsInfo(List<String> descCellList) {
			if (descCellList != null) {
				for (String cellId : descCellList) {
					// 去除小区pci
					cellToPci.remove(cellId);
					// 去除同站小区pci
					if (cellToSameStationOtherCells.get(cellId) != null) {
						for (String c : cellToSameStationOtherCells.get(cellId)) {
							cellToPci.remove(c);
						}
					}
					/*
					// 去除邻区
					if (cellToMaxNcells.get(cellId) != null) {
						for (String c : cellToMaxNcells.get(cellId)) {
							cellToPci.remove(c);
						}
					}
					*/
				}
			}
		}

		@SuppressWarnings("unchecked")
		public PlanObj clone() {
			PlanObj res = null;
			try {
				res = (PlanObj) super.clone();
				res.cellToPci = (HashMap<String, Integer>) cellToPci.clone();
				res.cellToInterVal = (HashMap<String, Double>) cellToInterVal.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return res;
		}
	}
}
