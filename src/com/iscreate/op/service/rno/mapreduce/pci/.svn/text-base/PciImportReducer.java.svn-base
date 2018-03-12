package com.iscreate.op.service.rno.mapreduce.pci;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PciImportReducer extends Reducer<Text, Text, Text, Text> {

	private static Logger logger= LoggerFactory.getLogger(PciImportReducer.class);
	
	// 配置
	private PciConfig config = null;

	/** 所有主小区 **/
	List<String> cellList = new ArrayList<String>();

	/** 所有邻区，不重复 **/
	List<String> ncellList = new ArrayList<String>();

	/** 小区与同站其他小区列的映射，小区列表依据关联度从大到小排列 **/
	Map<String, List<String>> cellToSameStationOtherCells = new HashMap<String, List<String>>();

	/** 小区与非同站小区列表的映射，小区列表依据关联度从大到小排列 **/
	Map<String, List<String>> cellToNotSameStationCells = new HashMap<String, List<String>>();

	/** 小区与邻区关联度的映射（包含了同站其他小区） **/
	Map<String, Map<String, Double>> cellToNcellAssocDegree = new HashMap<String, Map<String, Double>>();

	/** 小区与小区总关联度的映射 **/
	Map<String, Double> cellToTotalAssocDegree = new HashMap<String, Double>();

	/** 小区与原PCI的映射 **/
	Map<String, Integer> cellToOriPci = new HashMap<String, Integer>();

	// reduce 处理次数统计
	long counter = 0;

	// 处理行数统计
	long lineCounter = 0;

	// 运行开始时间
	private long startTimeMillis = 0;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {

		super.setup(context);

		startTimeMillis = System.currentTimeMillis();

		System.out.println("===>>>PciReducer_setup() 执行中...");
		System.out.println("reduce counter = " + counter);

		// 初始化配置信息
		config = new PciConfig(context);
	}

	/**
	 * key 为 cellId,cellPci;同站小区列表，如 1872362,31;1872361,1872363
	 * values 格式为 ncellId,assocDegree,ncellPci 的集合
	 * 如：1869923,0.004748338081671415,59
	 */
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		if (++counter % 100 == 0) {
			System.out.println("reduce counter = " + counter);
		}
		
		String[] strKeys = key.toString().split(";");
		String[] cellBase = strKeys[0].split(",");		
		String cellId = cellBase[0];

		if (cellList.contains(cellId)) {
			System.out.println("有重复的 cellId 存在，跳过不处理。cellId = " + cellId);
			return;
		} else {
			cellList.add(cellId.intern());
		}

		Cell cell = new Cell(cellId);		
        
		cell.setPci(Integer.parseInt(cellBase[1]));

		// 服务小区pci(cell_pci)
		cellToOriPci.put(cellId.intern(), cell.getPci());

		// 处理同站其他小区
		String strSameStationCells = strKeys[1];

		if (strSameStationCells != null && strSameStationCells.length() != 0) {
			// 1868801,1868802,1868803
			// 把主小区ID从同站小区列表中删除，剩下的就是同站其他小区
			String[] sameStationCells = strSameStationCells.split(",");

			for (String ssCellId : sameStationCells) {
				ssCellId = ssCellId.trim();
				if (!ssCellId.equals(cellId)) {
					cell.getSameStationOtherCells().add(ssCellId.intern());
				}
			}
		}

		if (!mergeMrData(cell, values)) {
			System.out.println("cellObj 被过滤。cellObj = " + cell.getId());
			cell = null;
			return;
		}

		// 添加同站其他小区
		cellToSameStationOtherCells.put(cellId.intern(), cell.getSameStationOtherCells());
		
		Map<String, Double> ncellAssocDegree = new HashMap<String, Double>();

		for (Ncell ncell : cell.getNcells()) {
			// 分解出cell和关联度
			ncellAssocDegree.put(ncell.getId().intern(), ncell.getAssocDegree());
		}

		// 获取邻区与关联度的映射，邻区列
		Map<String, Double> notSameStationCellsAssocDegree = new HashMap<String, Double>();
		List<String> notSameStationCells = new ArrayList<String>();
		for (String ncellId : ncellAssocDegree.keySet()) {
			boolean ok = true;
			for (String sameStatCell : cellToSameStationOtherCells.get(cellId)) {
				if (sameStatCell.equals(ncellId)) {
					ok = false;
				}
			}

			if (ok) {
				notSameStationCellsAssocDegree.put(ncellId.intern(), ncellAssocDegree.get(ncellId));
				notSameStationCells.add(ncellId.intern());
			}
		}

		// 对小区的邻区按关联度从大到小排序
		String tmpNcell = "";
		for (int i = 0; i < notSameStationCells.size(); i++) {
			for (int j = i + 1; j < notSameStationCells.size(); j++) {
				if (notSameStationCellsAssocDegree.get(notSameStationCells.get(i)) < notSameStationCellsAssocDegree.get(notSameStationCells.get(j))) {
					tmpNcell = notSameStationCells.get(i);
					notSameStationCells.set(i, notSameStationCells.get(j).intern());
					notSameStationCells.set(j, tmpNcell.intern());
				}
			}
		}

		// 保存小区与邻区关联度的映射（包含了同站其他小区）
		cellToNcellAssocDegree.put(cellId.intern(), ncellAssocDegree);
		// 保存小区与邻区列表的映射
		cellToNotSameStationCells.put(cellId.intern(), notSameStationCells);
		// 保存小区与总关联度的映射
		cellToTotalAssocDegree.put(cellId.intern(), cell.getTotalAssocDegree());
	}

	@Override
	public void cleanup(Context context) {
		logger.info("reduce spent seconds = " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0));
		logger.info("reduce counter total = " + counter);
		logger.info("reduce line counter total = " + lineCounter);
		
		startTimeMillis = System.currentTimeMillis();

		// 把Pci计算的数据写入到临时文件中，用于分析和调试
		if (logger.isDebugEnabled()) {
			writeObject();
		}

		// 打印日志，输出一些全局变量的值
		logger.info("cellToNcellAssocDegree size=" + cellToNcellAssocDegree.size());
		logger.info("cellToNotSameStationCells size=" + cellToNotSameStationCells.size());
		logger.info("cellToTotalAssocDegree size=" + cellToTotalAssocDegree.size());
		logger.info("cellToSameStationOtherCells size=" + cellToSameStationOtherCells.size());
		logger.info("cellToOriPci size=" + cellToOriPci.size());

		PciCalc pciCalc = new PciCalc(config);
		
		pciCalc.setCellToNcellAssocDegree(cellToNcellAssocDegree);
		pciCalc.setCellToNotSameStationCells(cellToNotSameStationCells);
		pciCalc.setCellToOriPci(cellToOriPci);
		pciCalc.setCellToSameStationOtherCells(cellToSameStationOtherCells);
		pciCalc.setCellToTotalAssocDegree(cellToTotalAssocDegree);
		
		pciCalc.execCalc();

		logger.info("cleanup finished. Spent seconds = " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0));
	}

	/**
	 * 把Pci计算的数据写入到临时文件中，用于分析和调试
	 */
	public void writeObject() {
		try {
			FileOutputStream outStream = new FileOutputStream("/tmp/PciCalc-" + config.getFileName() + ".dat");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);

			objectOutputStream.writeObject(cellToNcellAssocDegree);
			objectOutputStream.writeObject(cellToNotSameStationCells);
			objectOutputStream.writeObject(cellToTotalAssocDegree);
			objectOutputStream.writeObject(cellToSameStationOtherCells);
			objectOutputStream.writeObject(cellToOriPci);
			objectOutputStream.writeObject(config.getCellsNeedtoAssignList());

			outStream.close();
			logger.info("Export PciCalcData.dat Success.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合并 MR 测量数据
	 * @param cell
	 * @param values
	 * @return 返回 false 则 cell 对象被过滤
	 */
	public boolean mergeMrData(Cell cell, Iterable<Text> values) {

		double totalAssocDegree = 0.0;
		
		List<Ncell> ncells = cell.getNcells();
				
		for (Text val : values) {
			String indexs[] = val.toString().split(",");
			String ncellId = indexs[0];
			int pci = Integer.parseInt(indexs[1]);

			lineCounter++;

			if (!ncellList.contains(ncellId)) {
				ncellList.add(ncellId.intern());
				cellToOriPci.put(ncellId.intern(), pci);
			}

			Ncell ncell = new Ncell(ncellId.intern());

			ncell.setPci(pci);
			
			double assocDegree =Double.parseDouble(indexs[2]);
			ncell.setAssocDegree(assocDegree);
			
			totalAssocDegree += assocDegree;
			ncells.add(ncell);
		}

		// 如果总关联度小于“最小总关联度”，则过滤掉
		if (totalAssocDegree < config.getMincorrelation()) {
			System.out.println("cellObj=" + cell.getId() + ",totalAssocDegree=" + totalAssocDegree);
			return false;
		}

		cell.setTotalAssocDegree(totalAssocDegree);

		return true;
	}

	/**
	 * 小区对象
	 * @author scott
	 */
	class Cell {
		private String id;
		private int pci;
		private List<String> sameStationOtherCells;
		private long timesTotals;
		private double totalAssocDegree;
		private List<String> notSameStationCells;
		private Map<String, Double> nssCellsAssocDegree;
		private List<Ncell> ncells;

		public Cell(String id) {
			this.id = id;
			this.pci = -1;
			this.timesTotals = 0;
			this.totalAssocDegree = 0.0;
			this.sameStationOtherCells = new ArrayList<String>();
			this.notSameStationCells = new ArrayList<String>();
			this.nssCellsAssocDegree = new HashMap<String, Double>();
			this.ncells = new ArrayList<Ncell>();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getPci() {
			return pci;
		}

		public void setPci(int pci) {
			this.pci = pci;
		}

		public long getTimesTotals() {
			return timesTotals;
		}

		public void setTimesTotals(long timesTotals) {
			this.timesTotals = timesTotals;
		}

		public List<String> getSameStationOtherCells() {
			return sameStationOtherCells;
		}

		public void setSameStationOtherCells(List<String> sameStationOtherCells) {
			this.sameStationOtherCells = sameStationOtherCells;
		}

		public List<String> getNotSameStationCells() {
			return notSameStationCells;
		}

		public void setNotSameStationCells(List<String> notSameStationCells) {
			this.notSameStationCells = notSameStationCells;
		}

		public double getTotalAssocDegree() {
			return totalAssocDegree;
		}

		public void setTotalAssocDegree(double totalAssocDegree) {
			this.totalAssocDegree = totalAssocDegree;
		}

		public Map<String, Double> getNssCellsAssocDegree() {
			return nssCellsAssocDegree;
		}

		public void setNssCellsAssocDegree(Map<String, Double> nssCellsAssocDegree) {
			this.nssCellsAssocDegree = nssCellsAssocDegree;
		}

		public List<Ncell> getNcells() {
			return ncells;
		}

		public void setNcells(List<Ncell> ncells) {
			this.ncells = ncells;
		}
	}

	/**
	 * 邻区对象
	 * @author scott
	 */
	class Ncell {
		private String id; // 邻区标识
		private int pci; // 邻区PCI
		private long timesTotal; // 测量次数
		private long numerator; // 关联度分子
		private double assocDegree;

		public Ncell(String id) {
			this.id = id;
			this.pci = -1;
			this.timesTotal = 0;
			this.numerator = 0;
			this.assocDegree = 0.0;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getPci() {
			return pci;
		}

		public void setPci(int pci) {
			this.pci = pci;
		}

		public long getTimesTotal() {
			return timesTotal;
		}

		public void setTimesTotal(long timesTotal) {
			this.timesTotal = timesTotal;
		}

		public long getNumerator() {
			return numerator;
		}

		public void setNumerator(long numerator) {
			this.numerator = numerator;
		}

		public double getAssocDegree() {
			return assocDegree;
		}

		public void setAssocDegree(double assocDegree) {
			this.assocDegree = assocDegree;
		}

	}
}
