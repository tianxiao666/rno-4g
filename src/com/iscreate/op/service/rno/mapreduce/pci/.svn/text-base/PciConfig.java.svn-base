package com.iscreate.op.service.rno.mapreduce.pci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class PciConfig {
	private Configuration conf;
	private double m3r = 0;
	private double m6r = 0;
	private double m30r = 0;
	private double topRate = 0; // top n%
	private double defInterRate = 0; // 给定的干扰差值比例m
	private double defVariance = 0; // 给定的方差值
	private int divideNumber = 10;

	private String planType = "ONE"; // 评估方案，默认1
	private String schemeType = "ONE"; // 收敛方案，默认1

	private String jobId = "";
	private String filePath = "";
	private String fileName = "";

	/** 权值 **/
	private double samefreqcellcoefweight = 0.8;

	/** 切换比例权值 **/
	private double switchratioweight = 0.2;

	/** 合并后最小的测量总数 **/
	private long minmeasuresum = 0;

	/** 合并后最小的关联度（百分比） **/
	private double mincorrelation = 0.0;

	private List<String> cellsNeedtoAssignList;

	private Map<String, String> enodebToCells;

	@SuppressWarnings("rawtypes")
	public PciConfig(Context context) {
		Configuration conf = context.getConfiguration();
		this.conf = conf;
		this.jobId = context.getJobID().toString();

		// 方案保存的文件路径
		this.filePath = conf.get("RESULT_DIR");
		this.fileName = conf.get("RD_FILE_NAME");

		// 获取需要优化的小区字符串（变小区），格式为“A,B,C....”
		String[] strCells = conf.get("OPTIMIZE_CELLS").split(",");
		this.cellsNeedtoAssignList = new ArrayList<String>();
		for (String cellId : strCells) {
			this.cellsNeedtoAssignList.add(cellId.trim().intern());
		}

		// 获取各个mod的值
		this.m3r = Double.parseDouble(conf.get("cellm3rinterfercoef"));
		this.m6r = Double.parseDouble(conf.get("cellm6rinterfercoef"));
		this.m30r = Double.parseDouble(conf.get("cellm30rinterfercoef"));

		// 获取 top n% 百分比
		this.topRate = Double.parseDouble(conf.get("topncelllist")) * 0.01;

		// 干扰差值比例(m%)
		this.defInterRate = Double.parseDouble(conf.get("convermethod1targetval")) * 0.01;

		// 方差值(m%)
		this.defVariance = Double.parseDouble(conf.get("convermethod2targetval")) * 0.01;

		// 等分数
		this.divideNumber = Integer.parseInt(conf.get("convermethod2scoren"));

		// 评估方案
		this.planType = conf.get("PLAN_TYPE");

		// 收敛方案
		this.schemeType = conf.get("CONVER_TYPE");

		// 同站小区集合（从数据库工参表取到的数据，只和所选地市有关，和测量数据的多少无关）
		String strEnodebToCells = conf.get("enodebToCells");
		if (strEnodebToCells != null && strEnodebToCells.length() > 0) {
			System.out.println("strEnodebToCells length=" + strEnodebToCells.length());
			this.enodebToCells = string2Map(strEnodebToCells);
		}

		// 获取权值
		this.samefreqcellcoefweight = Double.parseDouble(conf.get("samefreqcellcoefweight"));
		this.switchratioweight = Double.parseDouble(conf.get("switchratioweight"));

		// 获取“合并后最小测量总数”和“合并后小于关联度（百分比）”的参数
		this.minmeasuresum = Long.parseLong(conf.get("minmeasuresum"));
		this.mincorrelation = Double.parseDouble(conf.get("mincorrelation")) * 0.01;
	}

	public Configuration getConf() {
		return conf;
	}

	public double getM3r() {
		return m3r;
	}

	public double getM6r() {
		return m6r;
	}

	public double getM30r() {
		return m30r;
	}

	public double getTopRate() {
		return topRate;
	}

	public double getDefInterRate() {
		return defInterRate;
	}

	public double getDefVariance() {
		return defVariance;
	}

	public String getPlanType() {
		return planType;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public String getJobId() {
		return jobId;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public Double getSamefreqcellcoefweight() {
		return samefreqcellcoefweight;
	}

	public Double getSwitchratioweight() {
		return switchratioweight;
	}

	public long getMinmeasuresum() {
		return minmeasuresum;
	}

	public double getMincorrelation() {
		return mincorrelation;
	}

	public List<String> getCellsNeedtoAssignList() {
		return cellsNeedtoAssignList;
	}

	public Map<String, String> getEnodebToCells() {
		return enodebToCells;
	}

	public int getDivideNumber() {
		return divideNumber;
	}

	private Map<String, String> string2Map(String enodebs) {
		Map<String, String> enodebToCells = new HashMap<String, String>();
		String keyarr[] = null;
		String valarr[] = null;
		keyarr = enodebs.split("\\|");
		for (int i = 0; i < keyarr.length; i++) {
			valarr = keyarr[i].split("=");
			enodebToCells.put(valarr[0].intern(), valarr[1].replace("#", ","));
		}
		return enodebToCells;
	}

	@Override
	public String toString() {
		return "m3r = " + m3r + ", m6r = " + m6r + ", m30r = " + m30r + ", topRate = " + topRate
				+ ", defInterRate = " + defInterRate + ", defVariance = " + defVariance + ", planType = "
				+ planType + ", schemeType = " + schemeType + ", samefreqcellcoefweight = "
				+ samefreqcellcoefweight + ", switchratioweight = " + switchratioweight + ", mincorrelation = "
				+ mincorrelation + ", minmeasuresum = " + minmeasuresum;
	}

}
