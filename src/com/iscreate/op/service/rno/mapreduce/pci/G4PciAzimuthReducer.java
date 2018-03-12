package com.iscreate.op.service.rno.mapreduce.pci;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class G4PciAzimuthReducer extends Reducer<Text,Text,Text,Text>{
	
	private static Logger log=Logger.getLogger("MyDFSClient");
	Configuration conf = null;
	private String numerator="";
	
	//MR 方向角汇总
	Map<Integer, G4Azimuth> sumG4Azimuth=new HashMap<Integer, G4Azimuth>();
	

	//判断汇总后的MR
	//到目前为止汇总后的MR肯定是有数据的，哪么只要关注汇总HO是否有数据就Ok
	List<G4PciRec> mrPciRecs=null;
	int curAzimuth=0;
	String sameSiteOtherCell = "";
    String splitStr="";
    Map<Integer, Integer> mixed=new HashMap<Integer, Integer>();//存储MR与HO存在的交集
    int hoNcell=0;
    int mrNcell=0;
    double hoCosi=0;
    double mrCosi=0;
	//小区标识到当前小区方位角的映射
	Map<String, String> cellIdToAzimuth=null;
	//文件路径
	String pciOriPath="";
	@Override
	protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		//获取配置信息
		conf = context.getConfiguration();
		numerator=conf.get("numerator");
		String arr[]=conf.getStrings("cellIdToAzimuth");
//		System.out.println("cellIdToAzimuth arr[0]==="+arr[0]);
		if(arr!=null && arr.length!=0){
			cellIdToAzimuth=string2Map(arr[0]);
		}
//		System.out.println("cellIdToAzimuth 大小==="+cellIdToAzimuth.size());
		pciOriPath=conf.get("writeFilePath");
		log.error("进入  G4PciAzimuthReducer");
	}
	
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		//将cell      对应的ncell 分母，分子，，进行迭加整理
		long t1=System.currentTimeMillis();
		//cellId+"_ZTE_MR"
	    //String cellStr[]=key.toString().split("_");
		int cellId=Integer.parseInt(key.toString());
		//MR 方向角
		Map<Integer, G4Azimuth> g4Azimuth=null;
		MrObj mrObj = new MrObj();
//		System.out.println("cellId====="+cellId);
		for (Text val : values) {
//			System.out.println("ncell ==="+val);
			String indexs[]=val.toString().split(",");
			if(indexs[indexs.length-1].equals("MR")){
//				System.out.println("cell="+key.toString()+"---------"+indexs[indexs.length-1]+"-ncells="+val.toString());
				mrObj.sumMrData(cellId, indexs);
			 }
		}
			//获取MR方位角
			g4Azimuth=mrObj.getG4Azimuth(cellId);
		/*	System.out.println("g4Azimuth===="+g4Azimuth);
			System.out.println("g4Azimuth===="+g4Azimuth.size());
			System.out.println("g4Azimuth.get(cellId)  是否为空:"+g4Azimuth.get(cellId));
			System.out.println(g4Azimuth.get(cellId).getCellName()+"--"+g4Azimuth.get(cellId).getCellId()+"--"+g4Azimuth.get(cellId).getCurAzimuth()+"--"+g4Azimuth.get(cellId).getCalAzimuth());
		*/	
		//放入缓存
		sumG4Azimuth.putAll(g4Azimuth);
		long t2=System.currentTimeMillis();
//		System.out.println("reduce 规整时间为---"+(t2-t1)/1000+"秒!");
	}
	
	@Override
	protected void cleanup(org.apache.hadoop.mapreduce.Reducer.Context context)
			throws InterruptedException, IOException {
//		String pciOriPath="hdfs:///rno_data/pcioridata/aa/"+UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("sumG4Azimuth 最终大小:"+sumG4Azimuth.size());	
		
		System.out.println("pciOriPath 文件路径是:"+pciOriPath);
		save4GAzimuthInHdfs(pciOriPath,sumG4Azimuth);
	}
	
	
	class G4PciRec {

		private String cellName;//小区名称
		private int ncell_id;//邻区标识
		private double RsrpTimes1;//主小区减去邻小区信号强度   可设置相对数值2(建议值：-3)
		private double cosi;//服务小区到某一邻区的关联度
		
		private int curAzimuth;//当前系统小区方位角
		
		private double cellLon;
		private double cellLat;
		private double ncellLon;
		private double ncellLat;
		public String getCellName() {
			return cellName;
		}
		public void setCellName(String cellName) {
			this.cellName = cellName;
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
		public double getCosi() {
			return cosi;
		}
		public void setCosi(double cosi) {
			this.cosi = cosi;
		}
		public int getCurAzimuth() {
			return curAzimuth;
		}
		public void setCurAzimuth(int curAzimuth) {
			this.curAzimuth = curAzimuth;
		}
		public double getCellLon() {
			return cellLon;
		}
		public void setCellLon(double cellLon) {
			this.cellLon = cellLon;
		}
		public double getCellLat() {
			return cellLat;
		}
		public void setCellLat(double cellLat) {
			this.cellLat = cellLat;
		}
		public double getNcellLon() {
			return ncellLon;
		}
		public void setNcellLon(double ncellLon) {
			this.ncellLon = ncellLon;
		}
		public double getNcellLat() {
			return ncellLat;
		}
		public void setNcellLat(double ncellLat) {
			this.ncellLat = ncellLat;
		}
		public G4PciRec(String cellName, int ncell_id, double rsrpTimes1,
				double cosi, int curAzimuth, double cellLon, double cellLat,
				double ncellLon, double ncellLat) {
			super();
			this.cellName = cellName;
			this.ncell_id = ncell_id;
			RsrpTimes1 = rsrpTimes1;
			this.cosi = cosi;
			this.curAzimuth = curAzimuth;
			this.cellLon = cellLon;
			this.cellLat = cellLat;
			this.ncellLon = ncellLon;
			this.ncellLat = ncellLat;
		}
	}
	class MrObj {
		private Map<Integer, List<G4PciRec>> cellToNcellObj=new HashMap<Integer, List<G4PciRec>>();//小区标识到邻区集合，一对多的关系
//		private Map<Integer, Double> cellToTimes=new HashMap<Integer, Double>();//小区标识到总测量报告数据
		private Map<String, MeaTimeToMixing> cellToMixing=new HashMap<String, MeaTimeToMixing>();
		//ncellId+","+timestotal+","+time1+","+enodebId+","+cityId+","+meaTime+","+cellPci+","+ncellPci+","+cellBcch+","+ncellBcch+","+"MR";
		//更改为
		//ncellId+","+timestotal+","+time1+","+enodebId+","+cityId+","+meaTime+","+cellLon+","+cellLat+","+ncellLon+","+ncellLat+","+cellName+",MR"
		private Map<Integer, G4Azimuth> cellToAzimuthInfo=new HashMap<Integer, G4Azimuth>();//输出方向角
		public Map<Integer, List<G4PciRec>> sumMrData(int key,
				String indexs[]) {
			// 遍历结果.
			// System.out.println("results:"+results.length);
			//cell=1868801---------MR-ncells=1868802,1628,1628,186880,93,2015/4/10 6:45,112.853,23.2121,112.853,23.2121,佛山三水西南广东商学院二D-ZLH-1,mixingSum,MR
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
			double cellLon = 0;
			double cellLat = 0;
			double ncellLon = 0;
			double ncellLat = 0;
			String cellName="";
			double mixingSum=0;
			String meaTime="";
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
			meaTime = indexs[5];
			cellLon = Double.parseDouble(indexs[6]);
			cellLat = Double.parseDouble(indexs[7]);
			ncellLon = Double.parseDouble(indexs[8]);
			ncellLat = Double.parseDouble(indexs[9]);
			cellName = indexs[10];
			mixingSum = Double.parseDouble(indexs[11]);
			if (!"".equals(indexs[9])) {
				// 算出某小区总的timestotal测量报告数据 迭加
				/*if (cellToTimes.containsKey(cellId)) {
					cellToTimes.put(cellId, cellToTimes.get(cellId)
							+ timesTotal);
//					System.out.println("cellToTimes.containsKey(cellId)包含："+cellId+"---"+timesTotal);
				} else {
					cellToTimes.put(cellId, timesTotal);
//					System.out.println("cellToTimes.containsKey(cellId)不包含："+cellId+"---"+timesTotal);
				}*/
				//@author chao.xj  2015-6-4 上午10:51:06 修改
				if(cellToMixing.get(cellId+"_"+ncellId)!=null){
					MeaTimeToMixing meaTimeToMixing=cellToMixing.get(cellId+"_"+ncellId);
					if(!meaTimeToMixing.getMeaTime().equals(meaTime)){
						String inerMixing=meaTimeToMixing.getMixingSum();
						meaTimeToMixing.setMixingSum(String.valueOf(Double.parseDouble(inerMixing)+mixingSum));
//						cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
					}
				}else{
					MeaTimeToMixing meaTimeToMixing=new MeaTimeToMixing();
					meaTimeToMixing.setMeaTime(meaTime);
					meaTimeToMixing.setMixingSum(String.valueOf(mixingSum));
					cellToMixing.put(cellId+"_"+ncellId, meaTimeToMixing);
				}
				
				// 获取某邻区的RSRPtimes1 可设置相对数值2(建议值：-3)
				if (cellToNcellObj.containsKey(cellId)) {
					// cellToNcellObj.put(cellId, );
					ncells = cellToNcellObj.get(cellId);
					ncells.add(new G4PciRec(cellName,ncellId, time1, 0, curAzimuth, cellLon, cellLat, ncellLon, ncellLat));
				} else {
					ncells = new ArrayList<G4PciRec>();
					ncells.add(new G4PciRec(cellName,ncellId, time1, 0, curAzimuth, cellLon, cellLat, ncellLon, ncellLat));
					cellToNcellObj.put(cellId, ncells);
				}
			}
//			System.out.println("sumMrData结束   cellToTimes的大小:"+cellToTimes.size());
			return cellToNcellObj;
		}
		
		public Map<Integer, G4Azimuth> getG4Azimuth(int key) {
			
//			System.out.println("getMrRelaDegree开始    cellToTimes的大小:"+cellToTimes.size());
			double timesTotal = 0;
			double time1 = 0;
			double cosi=0;
			double one_lng=0;
			double one_lat=0;
			double cell_lon=0;
			double cell_lat=0;
			double ncell_lon=0;
			double ncell_lat=0;
			List<G4PciRec> ncells = null;
			double sum_lng=0;
			double sum_lat=0;
			double lngDiff=0;
			double latDiff=0;
			double sinV=0;
			int azimuth=0;
			//遍历邻区MAP
			//第二次整理数据获取到某小区对应邻区的子关联度
				ncells=cellToNcellObj.get(key);
//				System.out.println("getMrRelaDegree key=---="+key);
				/*if(cellToTimes.get(key)!=null){
					timesTotal=cellToTimes.get(key);
				}*/
				
				if(ncells!=null){
					System.out.println("进入   ncells!=null------"+ncells.size());
					for (int i = 0; i < ncells.size(); i++) {
						time1=ncells.get(i).getRsrpTimes1();
						//@author chao.xj  2015-6-4 上午10:58:42 修改分母
						timesTotal=Double.parseDouble(cellToMixing.get(key+"_"+ncells.get(i).getNcell_id()).getMixingSum());
						cosi=time1/timesTotal;
						ncells.get(i).setCosi(cosi);
						cell_lon=ncells.get(i).getCellLon();
						cell_lat=ncells.get(i).getCellLat();
						ncell_lon=ncells.get(i).getNcellLon();
						ncell_lat=ncells.get(i).getNcellLat();
						if(cell_lon==ncell_lon && cell_lat==ncell_lat){
							continue;
						}
//						System.out.println(ncells.get(i).getCellName()+"--cell_lon=="+cell_lon+"---cell_lat=="+cell_lat+"---ncell_lon=="+ncell_lon+"---ncell_lat=="+ncell_lat);
						//获取邻区经纬度偏移坐标点
						one_lng =cell_lon+cosi * ((ncell_lon - cell_lon) /  
				                 Math.sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) + 
				                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat)));
						one_lat=cell_lat+cosi * ((ncell_lat - cell_lat) /  
				                 Math.sqrt((cell_lon - ncell_lon) * (cell_lon - ncell_lon) +  
				                       (cell_lat - ncell_lat) * (cell_lat - ncell_lat)));
						System.out.println("one_lng=="+one_lng+"---one_lat=="+one_lat);
						//sum(one_lng-lng) 矢量相加
						sum_lng+=(one_lng-cell_lon);
						//sum(one_lat-lat) 矢量相加
						sum_lat+=(one_lat-cell_lat);
					}
					//确认主小区矢量坐标A撇
					cell_lon=ncells.get(0).getCellLon();
					cell_lat=ncells.get(0).getCellLat();
//					System.out.println("sum_lng=="+sum_lng+"---sum_lat=="+sum_lat);
					lngDiff=sum_lng+cell_lon;
					latDiff=sum_lat+cell_lat;
//					System.out.println("lngDiff=="+lngDiff+"---latDiff=="+latDiff);
//					System.out.println("cell_lon=="+cell_lon+"---cell_lat=="+cell_lat);
					//过滤分母为0的
					if((lngDiff-cell_lon)!=0 && (latDiff-cell_lat)!=0) {
						//按照不同象限判断角度大小，正北为0，顺时针增加
						//第一象限
						if(lngDiff>cell_lon && latDiff>=cell_lat) {
//							System.out.println("第一象限");
							sinV = (lngDiff - cell_lon)
									/ (Math.sqrt((lngDiff - cell_lon) * (lngDiff - cell_lon)
											+ (latDiff - cell_lat) * (latDiff - cell_lat)));
							azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
						}
						//第二象限
						else if(lngDiff<=cell_lon && latDiff>cell_lat) {
//							System.out.println("第二象限");
							sinV = (lngDiff - cell_lon)
									/ (Math.sqrt((lngDiff - cell_lon) * (lngDiff - cell_lon)
											+ (latDiff - cell_lat) * (latDiff - cell_lat)));
							azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
							//asin在第二象限算出来的值为负值，改为正
							azimuth = -azimuth;
							//正北为0且以顺时针方向增加
							azimuth = 360 - azimuth;
						}
						//第三象限
						else if(lngDiff<cell_lon && latDiff<=cell_lat) {
//							System.out.println("第三象限");
							sinV = (lngDiff - cell_lon)
									/ (Math.sqrt((lngDiff - cell_lon) * (lngDiff - cell_lon)
											+ (latDiff - cell_lat) * (latDiff - cell_lat)));
							azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
							//asin在第三象限算出来的值为负值，改为正
							azimuth = -azimuth;
							//正北为0且以顺时针方向增加
							azimuth = 180 + azimuth;
						}
						//第四象限
						else if(lngDiff>=cell_lon && latDiff<cell_lat) {
//							System.out.println("第四象限");
							sinV = (lngDiff - cell_lon)
									/ (Math.sqrt((lngDiff - cell_lon) * (lngDiff - cell_lon)
											+ (latDiff - cell_lat) * (latDiff - cell_lat)));
							azimuth = (int)Math.round(Math.toDegrees(Math.asin(sinV)));
							//正北为0且以顺时针方向增加
							azimuth = 180 - azimuth;
						}
						cellToAzimuthInfo.put(key, new G4Azimuth(ncells.get(0).getCellName(), key, (cellIdToAzimuth.get(String.valueOf(key))==null?0:Integer.parseInt(cellIdToAzimuth.get(String.valueOf(key)))), azimuth, azimuth-ncells.get(0).getCurAzimuth()));
					}
				}
				
			return cellToAzimuthInfo;
		}
	}

	/**
	 * 
	 * @title 保存原始pci优化 4G 方位角到hdfs文件系统上
	 * @param filePath
	 * @param sumG4Azimuth
	 * @author chao.xj
	 * @date 2015-4-15下午5:32:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public void save4GAzimuthInHdfs(String filePath, Map<Integer, G4Azimuth> sumG4Azimuth) {
		String realFilePath = filePath;

		FileSystem fs = null;
		try {
//			Configuration conf = new YarnConfiguration();
			fs = FileSystem.get(conf);
		} catch (IOException e1) {
			System.err.println("save4GAzimuthInHdfs过程：打开hdfs文件系统出错！");
			e1.printStackTrace();
		}
		//先删除原有文件
		Path oldFilePath = new Path(URI.create(realFilePath));
		try {
			if(fs.exists(oldFilePath)) {
				fs.delete(oldFilePath, false);
			}
		} catch (IOException e2) {
			System.err.println("save4GAzimuthInHdfs过程：保存文件时，删除原有文件出错！");
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
		int cellId=0;
		int  diffAzimuth=0;
		int curAzimuth=0;
		int calAzimuth=0;
		String cellName="";
		
		G4Azimuth g4Azimuth=null;
		try {
			
			bo = new BufferedOutputStream(dataOs);
			for (Integer cell : sumG4Azimuth.keySet()) {
				g4Azimuth=sumG4Azimuth.get(cell);
					//line=cellName+"#"+cellId+"#"+curAzimuth+"#"+calAzimuth+"#"+diffAzimuth;
					//CELL##NCELL,COSI;
					cellName=g4Azimuth.getCellName();
					cellId=g4Azimuth.getCellId();
					curAzimuth=g4Azimuth.getCurAzimuth();
					calAzimuth=g4Azimuth.getCalAzimuth();
					diffAzimuth=Math.abs(curAzimuth-calAzimuth);
					diffAzimuth=diffAzimuth>180?360-diffAzimuth:diffAzimuth;
					line=cellName+"#"+cellId+"#"+curAzimuth+"#"+calAzimuth+"#"+diffAzimuth;
					bo.write(Bytes.toBytes(line+"\n"));
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
//		System.out.println("enodebs====="+enodebs);
		keyarr=enodebs.split("\\|");
		for (int i = 0; i < keyarr.length; i++) {
//			System.out.println("keyarr["+i+"]===="+keyarr[i]);
			valarr=keyarr[i].split("=");
//			System.out.println("valarr:--"+valarr[0]+"---"+valarr[1].replace("#", ","));
			enodebToCells.put(valarr[0],valarr[1].replace("#", ","));
		}
	return enodebToCells;
	}
	class G4Azimuth {

		private String cellName;//小区名称
		private int cellId;//小区标识
		private int curAzimuth;//当前系统小区方位角
		private int calAzimuth;//当前系统小区方位角
		private int diffAzimuth;//方向角差值
		public String getCellName() {
			return cellName;
		}
		public void setCellName(String cellName) {
			this.cellName = cellName;
		}
		public int getCellId() {
			return cellId;
		}
		public void setCellId(int cellId) {
			this.cellId = cellId;
		}
		public int getCurAzimuth() {
			return curAzimuth;
		}
		public void setCurAzimuth(int curAzimuth) {
			this.curAzimuth = curAzimuth;
		}
		public int getCalAzimuth() {
			return calAzimuth;
		}
		public void setCalAzimuth(int calAzimuth) {
			this.calAzimuth = calAzimuth;
		}
		public int getDiffAzimuth() {
			return diffAzimuth;
		}
		public void setDiffAzimuth(int diffAzimuth) {
			this.diffAzimuth = diffAzimuth;
		}
		public G4Azimuth(String cellName, int cellId, int curAzimuth,
				int calAzimuth, int diffAzimuth) {
			super();
			this.cellName = cellName;
			this.cellId = cellId;
			this.curAzimuth = curAzimuth;
			this.calAzimuth = calAzimuth;
			this.diffAzimuth = diffAzimuth;
		}
		
	}
	class MeaTimeToMixing{
		String meaTime;
		String mixingSum;
		public String getMeaTime() {
			return meaTime;
		}
		public void setMeaTime(String meaTime) {
			this.meaTime = meaTime;
		}
		public String getMixingSum() {
			return mixingSum;
		}
		public void setMixingSum(String mixingSum) {
			this.mixingSum = mixingSum;
		}
		
	}		
}
