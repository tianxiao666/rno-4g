package com.iscreate.op.service.rno.mapreduce.matrix;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class MatrixReducer extends Reducer<Text,Text,Text,Text>{
	
	private TempObj tempObj = new TempObj();
	Configuration conf = null;
	
	@Override
	public void setup(Context context)
			throws IOException, InterruptedException {
		System.out.println("MatrixReducer 执行setup中..");
		super.setup(context);
		//获取配置信息
		conf = context.getConfiguration();
	}
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		//将cell—ncell 对应的ci分子分母，ca分子分母，distance，isNei进行叠加整理
		for (Text val : values) {
			tempObj.addKV(key.toString(), val.toString());
		}
	}
	
	@Override
	public void cleanup(Context context)
			  {
		System.out.println("MatrixReducer 执行cleanup中..");
		//获取符合门限值条件的数据
		Map<String, Map<String, String>> res = tempObj.getCiCaMap();
		
		//组成小区NCS干扰矩阵数据对象
		MatrixNcsObj obj = new MatrixNcsObj();
		List<NcellObj> ncellObjs = null;
		NcellObj ncellObj = null;
		List<String> outCellList = null;
		
		for(String key : res.keySet()) {
			// k[0]表示cell，k[1]表示ncell
			String k[] = key.split(",");
			//构建小区跟out的列表的映射
			if(obj.getOutNcellsMap().get(k[1]) == null) {
				outCellList = new ArrayList<String>();
				outCellList.add(k[0]);
				obj.getOutNcellsMap().put(k[1], outCellList);
			} else {
				outCellList = obj.getOutNcellsMap().get(k[1]);
				outCellList.add(k[0]);
				obj.getOutNcellsMap().put(k[1], outCellList);
			}
			//构建小区跟in的详情的映射
			if(obj.getInNcellDetailsMap().get(k[0]) == null) {
				ncellObjs = new ArrayList<MatrixReducer.NcellObj>();
				ncellObj = new NcellObj();
				ncellObj.setNcellName(k[1]);
				ncellObj.setCi(res.get(key).get("ci"));
				ncellObj.setCa(res.get(key).get("ca"));
				ncellObj.setIsNei(res.get(key).get("isNei"));
				ncellObjs.add(ncellObj);
				obj.getInNcellDetailsMap().put(k[0], ncellObjs);
			} else {
				ncellObjs = obj.getInNcellDetailsMap().get(k[0]);
				ncellObj = new NcellObj();
				ncellObj.setNcellName(k[1]);
				ncellObj.setCi(res.get(key).get("ci"));
				ncellObj.setCa(res.get(key).get("ca"));
				ncellObj.setIsNei(res.get(key).get("isNei"));
				ncellObjs.add(ncellObj);
				obj.getInNcellDetailsMap().put(k[0], ncellObjs);
			}
		}
		
		//在hdfs创建索引文件和数据文件，并跟据obj的数据写入
		String dataPath = conf.get("dataPath");
		String idxPath = conf.get("idxPath");

		Path dataPathObj = new Path(URI.create(dataPath));
		Path idxPathObj = new Path(URI.create(idxPath));

		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			System.out.println("MatrixReducer过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}

		OutputStream dataOs= null;
		OutputStream idxOs= null;
		try {
			dataOs = fs.create(dataPathObj, true);
			idxOs = fs.create(idxPathObj, true);
		} catch (IOException e1) {
			System.out.println("MatrixReducer过程：创建输出数据文件流出错！");
			e1.printStackTrace();
		}
		
		DataOutputStream dataDtos = new DataOutputStream(dataOs);
		DataOutputStream idxDtos = new DataOutputStream(idxOs);
		//用于计算文件长度位置缓存的中间量
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		DataOutputStream posDtos = new DataOutputStream(baos); 
		
		//先在索引文件写入小区数量
		try {
			idxDtos.writeInt(obj.getInNcellDetailsMap().size());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	
		Map<String,List<NcellObj>> in = obj.getInNcellDetailsMap();
		
		for (String cell : in.keySet()) {
			try {
				
				List<NcellObj> inNcellObjs = in.get(cell);
				//索引
				idxDtos.writeUTF(cell);
				idxDtos.writeLong(baos.size());
				idxDtos.writeUTF(obj.getOutStrByCell(cell));
				//数据
				dataDtos.writeUTF(cell);
				dataDtos.writeInt(inNcellObjs.size());
				posDtos.writeUTF(cell); //用于记录长度
				posDtos.writeInt(inNcellObjs.size()); 
				for (NcellObj one : inNcellObjs) {
					dataDtos.writeUTF(one.getNcellName());
					dataDtos.writeUTF(one.getIsNei());
					dataDtos.writeDouble(Double.parseDouble(one.getCi()));
					dataDtos.writeDouble(Double.parseDouble(one.getCa()));
					//用于记录长度
					posDtos.writeUTF(one.getNcellName());
					posDtos.writeUTF(one.getIsNei());
					posDtos.writeDouble(Double.parseDouble(one.getCi()));
					posDtos.writeDouble(Double.parseDouble(one.getCa()));
				}

			} catch (IOException e) {
				System.out.println("MatrixReducer过程: matrixData文件写入内容失败！");
				try {
					dataDtos.close();
					idxDtos.close();
					posDtos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		try {
			dataDtos.close();
			idxDtos.close();
			posDtos.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	class TempObj {
		//key = "cell,ncell"
		//value = "ci的分子，ca的分子，distance，cica的分母，isNei"组成的map
		private Map<String,Map<String,String>> resMap = new HashMap<String, Map<String,String>>();
		
		public void addKV(String key,String val) {
			String v[] = val.split(",");
			if(resMap.get(key) == null) {
				Map<String,String> vals = new HashMap<String, String>();
				vals.put("CI_DIVIDER", v[0]);
				vals.put("CA_DIVIDER", v[1]);
				vals.put("DENOMINATOR", v[2]);
				vals.put("DISTANCE", v[3]);
				vals.put("isNei", v[4]);
				resMap.put(key, vals);
			} else {
				Map<String,String> vals = resMap.get(key);
				vals.put("CI_DIVIDER",
						(Double.parseDouble(vals.get("CI_DIVIDER"))+ Double.parseDouble(v[0]))+"");
				vals.put("CA_DIVIDER",
						(Double.parseDouble(vals.get("CA_DIVIDER"))+ Double.parseDouble(v[1]))+"");
				vals.put("DISTANCE",
						(Math.min(Double.parseDouble(vals.get("DISTANCE")),Double.parseDouble(v[2])))+"");
				vals.put("DENOMINATOR",
						(Double.parseDouble(vals.get("DENOMINATOR"))+ Double.parseDouble(v[3]))+"");
				vals.put("isNei",
						(Math.min(Integer.parseInt(vals.get("isNei")),Integer.parseInt(v[4])))+"");
				resMap.put(key, vals);
			}
		}

		/**
		 * 过滤不符合条件的数据,并整理成ci，ca形式
		 * @return map包含ci,ca,isNei
		 */
		public Map<String, Map<String, String>> getCiCaMap() {
			Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();
			
			String farDisWithLargeCI_dis = conf.get("farDisWithLargeCI_dis");
			String farDisWithLargeCi_ci = conf.get("farDisWithLargeCi_ci");
			String littleSampleWithLargeCI_sample = conf.get("littleSampleWithLargeCI_sample");
			String littleSampleWithLargeCI_ci = conf.get("littleSampleWithLargeCI_ci");
			String hugeSampleWithTinyCi_ci = conf.get("hugeSampleWithTinyCi_ci");
			String leastSampleCnt = conf.get("leastSampleCnt");
		
			for (String key : resMap.keySet()) {
				Map<String,String> one = resMap.get(key);
				//距离远，ci太大的不要
				if (Double.parseDouble(one.get("DISTANCE")) >= Double
						.parseDouble(farDisWithLargeCI_dis)
						&& (Double.parseDouble(one.get("CI_DIVIDER")) / Double
								.parseDouble(one.get("DENOMINATOR"))) >= Double
								.parseDouble(farDisWithLargeCi_ci)
						&& Integer.parseInt(one.get("isNei")) == 1) {
					continue;
				}
				//采样少，ci太大的不要
				if (Double.parseDouble(one.get("DENOMINATOR")) <= Double
						.parseDouble(littleSampleWithLargeCI_sample)
						&& (Double.parseDouble(one.get("CI_DIVIDER")) / Double
								.parseDouble(one.get("DENOMINATOR"))) >= Double
								.parseDouble(littleSampleWithLargeCI_ci)) {
					continue;
				}
				//ci太小的不要
				if (Double.parseDouble(one.get("CI_DIVIDER")) / Double
						.parseDouble(one.get("DENOMINATOR")) <= Double
						.parseDouble(hugeSampleWithTinyCi_ci)) {
					continue;
				}
				//采样太少，或者距离太远的不要
				if (Double.parseDouble(one.get("DENOMINATOR")) < Double
						.parseDouble(leastSampleCnt)) {
					continue;
				}
				Map<String,String> m = new HashMap<String, String>();
				m.put("ci",(Double.parseDouble(one.get("CI_DIVIDER")) / Double
								.parseDouble(one.get("DENOMINATOR"))) + "");
				m.put("ca", (Double.parseDouble(one.get("CA_DIVIDER")) / Double
								.parseDouble(one.get("DENOMINATOR"))) + "");
				m.put("isNei", one.get("isNei"));
				//符合条件的存起来
				result.put(key, m);
			}
			
			System.out.println("MatrixReducer过程 ：符合条件的kv对有："+result.size());
			
			return result;
		}
	}
	
	class MatrixNcsObj {
		//In
		private Map<String,List<NcellObj>> inNcellDetailsMap = new HashMap<String, List<NcellObj>>();
		//Out
		private Map<String,List<String>> outNcellsMap = new HashMap<String, List<String>>();
		
		public Map<String, List<NcellObj>> getInNcellDetailsMap() {
			return inNcellDetailsMap;
		}
		public void setInNcellDetailsMap(Map<String, List<NcellObj>> inNcellDetailsMap) {
			this.inNcellDetailsMap = inNcellDetailsMap;
		}
		public Map<String, List<String>> getOutNcellsMap() {
			return outNcellsMap;
		}
		public void setOutNcellsMap(Map<String, List<String>> outNcellsMap) {
			this.outNcellsMap = outNcellsMap;
		}
		/**
		 * 获取cell对其产生干扰的小区列
		 * @param cell
		 * @return A，B，C，.....
		 */
		public String getOutStrByCell(String cell) {
			List<String> ncells = outNcellsMap.get(cell);
			String ncellsStr = "";
			if(ncells == null) {
				return ncellsStr;
			}
			for (String ncell : ncells) {
				ncellsStr += ncell + ",";
			}
			if(!("").equals(ncellsStr)) {
				ncellsStr = ncellsStr.substring(0,ncellsStr.length()-1);
			}
			return ncellsStr;
		}
	}
	
	class NcellObj {
		private String ncellName;
		private String ci;
		private String ca;
		private String isNei;
		public String getNcellName() {
			return ncellName;
		}
		public void setNcellName(String ncellName) {
			this.ncellName = ncellName;
		}
		public String getCi() {
			return ci;
		}
		public void setCi(String ci) {
			this.ci = ci;
		}
		public String getCa() {
			return ca;
		}
		public void setCa(String ca) {
			this.ca = ca;
		}
		public String getIsNei() {
			return isNei;
		}
		public void setIsNei(String isNei) {
			this.isNei = isNei;
		}
	}
	
}

