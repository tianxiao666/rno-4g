package com.iscreate.op.service.rno.mapreduce.matrix;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.iscreate.op.service.rno.parser.vo.G4PciRec;

public class G4PciMatrixReducer extends Reducer<Text,Text,Text,Text>{
	
	
	Configuration conf = null;
	private String numerator="";
	
	//MR HO关联度汇总
	Map<Integer, List<G4PciRec>> sumMrRelaDegre=new HashMap<Integer, List<G4PciRec>>();
	

	//判断汇总后的MR
	//到目前为止汇总后的MR肯定是有数据的，哪么只要关注汇总HO是否有数据就Ok
	List<G4PciRec> mrPciRecs=null;
	String cells="";
	String sameSiteOtherCell = "";
    String splitStr="";
    Map<Integer, Integer> mixed=new HashMap<Integer, Integer>();//存储MR与HO存在的交集
    int hoNcell=0;
    int mrNcell=0;
    double hoCosi=0;
    double mrCosi=0;
	//同站小区判断条件变更由 enodeb->enodeb+earfcn
	Map<String, String> enodebToCells=null;
	//文件路径
	String pciOriPath="";
	@Override
	protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		//获取配置信息
		conf = context.getConfiguration();
		numerator=conf.get("numerator");
		String arr[]=conf.getStrings("enodebToCells");
		if(arr!=null && arr.length!=0){
			enodebToCells=string2Map(arr[0]);
		}
		pciOriPath=conf.get("pciOriPath");
	}
	
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		//将cell      对应的ncell 分母，分子，，进行迭加整理
		long t1=System.currentTimeMillis();
		//cellId+"_ZTE_MR"
	    //String cellStr[]=key.toString().split("_");
		int cellId=Integer.parseInt(key.toString());
		MrObj mrObj = new MrObj();
		//MR关联度
		Map<Integer, List<G4PciRec>> mrRelaDegre=null;
		//ho关联度汇总
		Map<Integer, List<G4PciRec>> hoRelaDegre=null;
//		System.out.println("cellId====="+cellId);
		for (Text val : values) {
//			System.out.println("ncell ==="+val);
			String indexs[]=val.toString().split(",");
			if(indexs[indexs.length-1].equals("MR")){
//				System.out.println("cell="+key.toString()+"---------"+indexs[indexs.length-1]+"-ncells="+val.toString());
				mrObj.sumMrData(cellId, indexs);
			 }
		}
			//补充MR关联度信息
			mrRelaDegre=mrObj.getMrRelaDegree(cellId);
		
				mrPciRecs=mrRelaDegre.get(cellId);
				//G4PciRec向其对象中填充服务小区的同站其他小区信息
				String cell = String.valueOf(cellId);
				String mcell = ",("+cell+",)+";
				String lcell = "^"+cell+",";
				String rcell = ","+cell+"$";
				for (G4PciRec g4PciRec : mrPciRecs) {
//					System.out.println("enodebToCells 的大小为:"+enodebToCells.size());
//					System.out.println(cellId +"同站其他小区 =>   "+g4PciRec.getCellEnodebId()+"_"+g4PciRec.getCellBcch());
					cells=enodebToCells.get(g4PciRec.getCellEnodebId()+"_"+g4PciRec.getCellBcch());
//					System.out.println(cellId +"同站其他小区 =>   "+cells);
					if(cells!=null && cells.length()!=0){
						//02400012,02400013,02400014
						sameSiteOtherCell = cells.replaceAll(mcell, ",");
						sameSiteOtherCell = sameSiteOtherCell.replaceFirst(lcell, "");
						sameSiteOtherCell = sameSiteOtherCell.replaceFirst(rcell, "");
//				        sameSiteOtherCell = sameSiteOtherCell.substring(0, sameSiteOtherCell.length()-1);
				        //补充同站小区
				        g4PciRec.setSameSiteOtherCell(sameSiteOtherCell);
				        //将关联度乘以权值不再乘权值
//				        g4PciRec.setCosi(g4PciRec.getCosi());
					}
				}
		//放入缓存
		sumMrRelaDegre.putAll(mrRelaDegre);
		long t2=System.currentTimeMillis();
//		System.out.println("reduce 规整时间为---"+(t2-t1)/1000+"秒!");
	}
	
	@Override
	protected void cleanup(org.apache.hadoop.mapreduce.Reducer.Context context)
			throws InterruptedException, IOException {
//		String pciOriPath="hdfs:///rno_data/pcioridata/aa/"+UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("sumMrRelaDegre 最终大小:"+sumMrRelaDegre.size());	
		
		System.out.println("pciOriPath 文件路径是:"+pciOriPath);
		save4GInterferMatrixInHdfs(pciOriPath,sumMrRelaDegre);
	}
	
	
	class G4PciRec {

		private int ncell_id;//邻区标识
		private double RsrpTimes1;//主小区减去邻小区信号强度   可设置相对数值2(建议值：-3)
		private double cosi;//服务小区到某一邻区的关联度
		private int cellEnodebId;//小区归属站点
		private int cellPci;//小区PCI
		private int ncellPci;//邻区PCI
		private int cellBcch;//小区频点
		private int ncellBcch;//邻区频点


		private String sameSiteOtherCell;//cell2,cell3
		public double getCosi() {
			return cosi;
		}

		public void setCosi(double cosi) {
			this.cosi = cosi;
		}

		public int getNcell_id() {
			return ncell_id;
		}

		public void setNcell_id(int ncell_id) {
			this.ncell_id = ncell_id;
		}
		public double getRsrpTimes1() {
			return RsrpTimes1;
		}

		public void setRsrpTimes1(double rsrpTimes1) {
			RsrpTimes1 = rsrpTimes1;
		}
		public int getCellEnodebId() {
			return cellEnodebId;
		}

		public void setCellEnodebId(int cellEnodebId) {
			this.cellEnodebId = cellEnodebId;
		}

		public int getCellPci() {
			return cellPci;
		}

		public void setCellPci(int cellPci) {
			this.cellPci = cellPci;
		}

		public String getSameSiteOtherCell() {
			return sameSiteOtherCell;
		}

		public void setSameSiteOtherCell(String sameSiteOtherCell) {
			this.sameSiteOtherCell = sameSiteOtherCell;
		}
		public int getNcellPci() {
			return ncellPci;
		}

		public void setNcellPci(int ncellPci) {
			this.ncellPci = ncellPci;
		}
		public int getCellBcch() {
			return cellBcch;
		}

		public void setCellBcch(int cellBcch) {
			this.cellBcch = cellBcch;
		}
		public int getNcellBcch() {
			return ncellBcch;
		}

		public void setNcellBcch(int ncellBcch) {
			this.ncellBcch = ncellBcch;
		}
		public G4PciRec(int ncell_id, double rsrpTimes1, double cosi,
				int cellEnodebId, int cellPci, int ncellPci, int cellBcch,
				String sameSiteOtherCell,int ncellBcch) {
			super();
			this.ncell_id = ncell_id;
			RsrpTimes1 = rsrpTimes1;
			this.cosi = cosi;
			this.cellEnodebId = cellEnodebId;
			this.cellPci = cellPci;
			this.ncellPci = ncellPci;
			this.cellBcch = cellBcch;
			this.sameSiteOtherCell = sameSiteOtherCell;
			this.ncellBcch = ncellBcch;
		}
	}
	class MrObj {
		private Map<Integer, List<G4PciRec>> cellToNcellObj=new HashMap<Integer, List<G4PciRec>>();//小区标识到邻区集合，一对多的关系
		private Map<Integer, Double> cellToTimes=new HashMap<Integer, Double>();//小区标识到总测量报告数据
		//ncellId+","+timestotal+","+time1+","+enodebId+","+cityId+","+meaTime+","+cellPci+","+ncellPci+","+cellBcch+","+ncellBcch+","+"MR";
		public Map<Integer, List<G4PciRec>> sumMrData(int key,
				String indexs[]) {
			// 遍历结果.
			// System.out.println("results:"+results.length);
			if ("".equals(numerator)) {
				numerator = "RSRPTIMES1";
			}
			numerator = numerator.toUpperCase();

			List<G4PciRec> ncells = null;
			int cellId = 0;
			int ncellId = 0;
			double timesTotal = 0;
			double time1 = 0;
			int enodebId = 0;
			int cellPci = 0;
			int ncellPci = 0;
			int cellBcch = 0;
			int ncellBcch = 0;
			boolean flag = true;
			DecimalFormat df = new DecimalFormat("#.#######");
			// 第一次整理数据据
			time1 = Integer.parseInt(indexs[2]);

			cellId = key;
			if (!indexs[0].equals("")) {
				ncellId = Integer.parseInt(indexs[0]);
			}
			timesTotal = Integer.parseInt(indexs[1]);
			enodebId = Integer.parseInt(indexs[3]);
			cellPci = Integer.parseInt(indexs[6]);
			ncellPci = Integer.parseInt(indexs[7]);
			cellBcch = Integer.parseInt(indexs[8]);
			ncellBcch = Integer.parseInt(indexs[9]);
			// 同频累加
			if (cellBcch == ncellBcch && !"".equals(indexs[9])) {
				// 算出某小区总的timestotal测量报告数据 迭加
				if (cellToTimes.containsKey(cellId)) {
					cellToTimes.put(cellId, cellToTimes.get(cellId)
							+ timesTotal);
//					System.out.println("cellToTimes.containsKey(cellId)包含："+cellId+"---"+timesTotal);
				} else {
					cellToTimes.put(cellId, timesTotal);
//					System.out.println("cellToTimes.containsKey(cellId)不包含："+cellId+"---"+timesTotal);
				}
				// 获取某邻区的RSRPtimes1 可设置相对数值2(建议值：-3)
				if (cellToNcellObj.containsKey(cellId)) {
					// cellToNcellObj.put(cellId, );
					ncells = cellToNcellObj.get(cellId);
					ncells.add(new G4PciRec(ncellId, time1, 0, enodebId,
							cellPci, ncellPci, cellBcch, "",ncellBcch));
				} else {
					ncells = new ArrayList<G4PciRec>();
					ncells.add(new G4PciRec(ncellId, time1, 0, enodebId,
							cellPci, ncellPci, cellBcch, "",ncellBcch));
					cellToNcellObj.put(cellId, ncells);
				}
			}
//			System.out.println("sumMrData结束   cellToTimes的大小:"+cellToTimes.size());
			return cellToNcellObj;
		}
		
		public Map<Integer, List<G4PciRec>> getMrRelaDegree(int key) {
			
//			System.out.println("getMrRelaDegree开始    cellToTimes的大小:"+cellToTimes.size());
			double timesTotal = 0;
			double time1 = 0;
			int enodebId = 0;
			int cellPci = 0;
			int ncellPci = 0;
			int cellBcch = 0;
			int ncellBcch = 0;
			List<G4PciRec> ncells = null;
			//遍历邻区MAP
			//第二次整理数据获取到某小区对应邻区的子关联度
				ncells=cellToNcellObj.get(key);
//				System.out.println("getMrRelaDegree key=---="+key);
				if(cellToTimes.get(key)!=null){
					timesTotal=cellToTimes.get(key);
				}
				
				if(ncells!=null){
					for (int i = 0; i < ncells.size(); i++) {
						time1=ncells.get(i).getRsrpTimes1();
						ncells.get(i).setCosi(time1/timesTotal);
					}
				}
			return cellToNcellObj;
		}
	}

	/**
	 * 
	 * @title 保存原始pci优化 4G 干扰矩阵文件到hdfs文件系统上
	 * @param filePath
	 * @param sumMrRelaDegre
	 * @author chao.xj
	 * @date 2015-4-15下午5:32:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void save4GInterferMatrixInHdfs(String filePath, Map<Integer, List<G4PciRec>> sumMrRelaDegre) {
		String realFilePath = filePath;

		FileSystem fs = null;
		try {
//			Configuration conf = new YarnConfiguration();
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			System.err.println("save4GInterferMatrixInHdfs过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}
		//先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if(fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			System.err.println("save4GInterferMatrixInHdfs过程：保存文件时，删除原有文件出错！");
			e2.printStackTrace();
		}
		//创建新文件
		Path filePathObj = new Path(URI.create(realFilePath));
		//创建流
		OutputStream dataOs= null;
		try {
			dataOs = fs.create(filePathObj, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		BufferedOutputStream bo=null ;
		String line="";
		int ncellId=0;
		double cosi=0;
		int cellPci=0;
		int ncellPci=0;
		String cells="";
		int cellBcch=0;
		int ncellBcch=0;
		List<G4PciRec> mrPciRecs=null;
		try {
			
			bo = new BufferedOutputStream(dataOs);
			for (Integer cell : sumMrRelaDegre.keySet()) {
				mrPciRecs=sumMrRelaDegre.get(cell);
				//G4PciRec向其对象中填充服务小区的同站其他小区信息
				for (G4PciRec g4PciRec : mrPciRecs) {
					cells=g4PciRec.getSameSiteOtherCell();
					cellPci=g4PciRec.getCellPci();
					cosi=g4PciRec.getCosi();
					ncellId=g4PciRec.getNcell_id();
					ncellPci=g4PciRec.getNcellPci();
					cellBcch=g4PciRec.getCellBcch();
					ncellBcch=g4PciRec.getNcellBcch();
					//CELL##NCELL,COSI;CELL2,CELL3;CELLPCI;NCELLPCI
					//line=cell+"##"+ncellId+","+cosi+";"+cells+";"+cellPci+";"+ncellPci;
					//CELL##NCELL,COSI;
					//原
//					line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci;
					//修改:增加同站小区 +"#"+cells+"#"+cellBcch+"#"+ncellBcch
//					line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci+"#"+cells+"#"+cellBcch+"#"+ncellBcch;
					bo.write(Bytes.toBytes(cell.toString()));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(ncellId)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(cosi)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(cellPci)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(ncellPci)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(cells)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(cellBcch)));
					bo.write(Bytes.toBytes("#"));
					bo.write(Bytes.toBytes(String.valueOf(ncellBcch)));
					bo.write(Bytes.toBytes("\n"));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static Map<String, String> string2Map(String enodebs) {
		
	    Map<String, String> enodebToCells=new HashMap<String, String>();
		String keyarr[]=null;
		String valarr[]=null;
		System.out.println("enodebs====="+enodebs);
		keyarr=enodebs.split("\\|");
		for (int i = 0; i < keyarr.length; i++) {
			System.out.println("keyarr["+i+"]===="+keyarr[i]);
			valarr=keyarr[i].split("=");
			System.out.println("valarr:--"+valarr[0]+"---"+valarr[1].replace("#", ","));
			enodebToCells.put(valarr[0],valarr[1].replace("#", ","));
		}
	return enodebToCells;
	}
}
