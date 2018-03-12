package com.iscreate.op.service.rno.mapreduce.matrix;

import java.io.IOException;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

public class G4PciMatrixMapper extends TableMapper<Text, Text> {
	
	private String enodebId=""	;
	private String cityId=""      ;
	private String meaTime=""     ;
	private String cellId =""     ;
	private String cellName  =""  ;
	private String cellBcch =""   ;
	private String cellPci  =""   ;
	private String ncellId  =""   ;
	private String ncell    =""    ;
	private String ncellBcch  ="" ;
	private String ncellPci   ="" ;
	private String timestotal =""  ;
	private String rsrptimes0 =""  ;
	private String rsrptimes1 =""  ;
	private String rsrptimes2 =""  ;
	private String rsrptimes3 =""  ;
	private String rsrptimes4 =""  ;
	Configuration conf = null;
	String [] ncellArr=new String [5];
	private String numerator="";
	private String time1="";
	// 距离
	private String dis="";
	@Override
	protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		//获取配置信息
		conf = context.getConfiguration();
		System.out.println("setup 中的conf能获得吗?="+conf);
		if(conf!=null){
			numerator=conf.get("numerator");
			numerator=numerator.toUpperCase();
		}
		System.out.println("PCIMapper 执行setup中..");
		super.setup(context);
	}
	
	protected void map(ImmutableBytesWritable row, Result value, Context context)
			throws IOException, InterruptedException {
		
		TableSplit split=(TableSplit)context.getInputSplit();
		TableName tableName = split.getTable();
//		System.out.println("表："+tableName.toString());
		NavigableMap<byte[], byte[]> navi = null;
		
		if (("LTE4G_ERI_MR").equals(tableName.toString())) {
			navi=value.getFamilyMap(Bytes.toBytes("MRINFO"));
			enodebId = new String(navi.get("ENODEB_ID".getBytes()));
			cityId = new String(navi.get("CITY_ID".getBytes()));
			meaTime = new String(navi.get("MEA_TIME".getBytes()));
			cellId = new String(navi.get("CELL_ID".getBytes()));
			cellName = new String(navi.get("CELL_NAME".getBytes()));
			cellBcch = new String(navi.get("CELL_BCCH".getBytes()));
			cellPci = new String(navi.get("CELL_PCI".getBytes()));
			ncellId = new String(navi.get("NCELL_ID".getBytes()));
			ncell = new String(navi.get("NCELL".getBytes()));
			ncellBcch = new String(navi.get("NCELL_BCCH".getBytes()));
			ncellPci = new String(navi.get("NCELL_PCI".getBytes()));
			timestotal = new String(navi.get("TIMESTOTAL".getBytes()));
			rsrptimes0 = new String(navi.get("RSRPTIMES0".getBytes()));
			rsrptimes1 = new String(navi.get("RSRPTIMES1".getBytes()));
			rsrptimes2 = new String(navi.get("RSRPTIMES2".getBytes()));
			rsrptimes3 = new String(navi.get("RSRPTIMES3".getBytes()));
			rsrptimes4 = new String(navi.get("RSRPTIMES4".getBytes()));
			dis = new String(navi.get("DISTANCE".getBytes())); 
			time1 = new String(navi.get(numerator.getBytes()));
			//同频累加
		    if(cellBcch.equals(ncellBcch) && !"".equals(ncellBcch) && !"".equals(ncellId) && Double.parseDouble(dis)*1000<5000){
				context.write(new Text(cellId), new Text(ncellId+","+timestotal+","+time1+","+enodebId+","+cityId+","+meaTime+","+cellPci+","+ncellPci+","+cellBcch+","+ncellBcch+",MR"));
			}
		} else if (("LTE4G_ZTE_MR").equals(tableName.toString())) {
			navi=value.getFamilyMap(Bytes.toBytes("MRINFO"));
			enodebId = new String(navi.get("ENODEB_ID".getBytes()));
			cityId = new String(navi.get("CITY_ID".getBytes()));
			meaTime = new String(navi.get("MEA_TIME".getBytes()));
			cellId = new String(navi.get("CELL_ID".getBytes()));
			cellName = new String(navi.get("CELL_NAME".getBytes()));
			cellBcch = new String(navi.get("CELL_BCCH".getBytes()));
			cellPci = new String(navi.get("CELL_PCI".getBytes()));
			ncellId = new String(navi.get("NCELL_ID".getBytes()));
			ncell = new String(navi.get("NCELL".getBytes()));
			ncellBcch = new String(navi.get("NCELL_BCCH".getBytes()));
			ncellPci = new String(navi.get("NCELL_PCI".getBytes()));
			timestotal = new String(navi.get("TIMESTOTAL".getBytes()));
			rsrptimes0 = new String(navi.get("RSRPTIMES0".getBytes()));
			rsrptimes1 = new String(navi.get("RSRPTIMES1".getBytes()));
			rsrptimes2 = new String(navi.get("RSRPTIMES2".getBytes()));
			rsrptimes3 = new String(navi.get("RSRPTIMES3".getBytes()));
			rsrptimes4 = new String(navi.get("RSRPTIMES4".getBytes()));
			time1 = new String(navi.get(numerator.getBytes()));
			dis = new String(navi.get("DISTANCE".getBytes())); 
			//同频累加
		    if(cellBcch.equals(ncellBcch)  && !"".equals(ncellBcch) && !"".equals(ncellId) && Double.parseDouble(dis)*1000<5000){
				context.write(new Text(cellId), new Text(ncellId+","+timestotal+","+time1+","+enodebId+","+cityId+","+meaTime+","+cellPci+","+ncellPci+","+cellBcch+","+ncellBcch+",MR"));
			}
		} 
		
	}
	
	@Override
	protected void cleanup(org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
	}
}
