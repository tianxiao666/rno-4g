package com.iscreate.op.service.rno.mapreduce.pci;

import java.io.IOException;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableSplit;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

public class PciMapper extends TableMapper<Text, Text> {

	private Configuration conf = null;

	/** 关联度分子字段 **/
	private String numeratorField = "";

	/** 关联度分子的值 **/
	private String numerator = "";

	/** 基站标识 **/
	private String enodebId = "";

	/** 小区标识 **/
	private String cellId = "";

	/** 小区频点 **/
	private String cellBcch = "";

	/** 小区 PCI **/
	private String cellPci = "";

	/** 邻区标识 **/
	private String ncellId = "";

	/** 邻区频点 **/
	private String ncellBcch = "";

	/** 邻区 PCI **/
	private String ncellPci = "";

	/** 测量次数 **/
	private String timestotal = "";
	
	/**  混频	 **/
	private String mixingSum = "";
	
	/** 测量时间 **/
	private String meaTime="" ;

	/** 行统计 **/
	private long counter = 0;

	/** 过滤后的同频行数统计 **/
	private long sameFreqCounter = 0;

	// 运行开始时间
	private long startTimeMillis = 0;

	// 距离
	private String dis="";
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		// 获取配置信息
		conf = context.getConfiguration();

		if (conf != null) {
			numeratorField = conf.get("numeratorField");

			if (numeratorField == null || "".equals(numeratorField)) {
				numeratorField = "RSRPTIMES1";
			} else {
				numeratorField = numeratorField.toUpperCase();
			}
		}

		super.setup(context);

		System.out.println("map counter = " + counter);
		startTimeMillis = System.currentTimeMillis();
	}

	protected void map(ImmutableBytesWritable row, Result value, Context context) throws IOException,
			InterruptedException {

		TableSplit split = (TableSplit) context.getInputSplit();

		String tableName = split.getTable().toString();

		NavigableMap<byte[], byte[]> navi = null;

		if (++counter % 10000 == 0) {
			System.out.println("map counter = " + counter);
		}

		if (tableName.endsWith("MR")) {
			// MR 数据（中兴或爱立信的）
			navi = value.getFamilyMap(Bytes.toBytes("MRINFO"));
			cellBcch = new String(navi.get("CELL_BCCH".getBytes()));
			ncellBcch = new String(navi.get("NCELL_BCCH".getBytes()));
			ncellId = new String(navi.get("NCELL_ID".getBytes()));
			dis = new String(navi.get("DISTANCE".getBytes())); 
			// 过滤非同频数据
			if (cellBcch.equals(ncellBcch) && !"".equals(ncellBcch) && !"".equals(ncellId) && Double.parseDouble(dis)*1000<5000) {
				enodebId = new String(navi.get("ENODEB_ID".getBytes()));
				cellId = new String(navi.get("CELL_ID".getBytes()));
				cellPci = new String(navi.get("CELL_PCI".getBytes()));
				ncellPci = new String(navi.get("NCELL_PCI".getBytes()));
				timestotal = new String(navi.get("TIMESTOTAL".getBytes()));
				numerator = new String(navi.get(numeratorField.getBytes()));
				mixingSum = new String(navi.get("MIXINGSUM".getBytes()));
				meaTime = new String(navi.get("MEA_TIME".getBytes()));

				// 以 cellId,cellPci,enodebId_cellBcch 作为 key
				String strKey = cellId + "," + cellPci + "," + enodebId + "_" + cellBcch;
				String strValue = ncellId + "," + ncellPci + "," + timestotal + "," + numerator + "," +mixingSum + "," + meaTime;

				sameFreqCounter++;

				context.write(new Text(strKey), new Text(strValue));
			}

		}

	}

	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		super.cleanup(context);
		System.out.println("map counter total = " + counter);
		System.out.println("same freq total = " + sameFreqCounter);
		System.out.println("map spent seconds = " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0));
	}
}
