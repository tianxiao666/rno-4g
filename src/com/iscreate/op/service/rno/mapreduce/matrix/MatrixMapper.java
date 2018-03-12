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


public class MatrixMapper extends TableMapper<Text, Text> {
	
	private String distance="";
	private String denominator="";
	private String ci_divider="";
	private String ca_divider="";
	private String isNeighbour="0";
	private String ncell="";
	private String cell="";
	private long farestDis=8;
	Configuration conf = null;
	
	@Override
	public void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		System.out.println("MatrixMapper 执行setup中..");
		//获取配置信息
		conf = context.getConfiguration();
		//获取指定的值
		farestDis = Long.parseLong(conf.get("farestDis"));
		
//		System.out.println("farestDis="+conf.get("farestDis"));
//		System.out.println("farDisWithLargeCI_dis="+conf.get("farDisWithLargeCI_dis"));
//		System.out.println("farDisWithLargeCi_ci="+conf.get("farDisWithLargeCi_ci"));
//		System.out.println("littleSampleWithLargeCI_sample="+conf.get("littleSampleWithLargeCI_sample"));
//		System.out.println("littleSampleWithLargeCI_ci="+conf.get("littleSampleWithLargeCI_ci"));
//		System.out.println("hugeSampleWithTinyCi_ci="+conf.get("hugeSampleWithTinyCi_ci"));
//		System.out.println("leastSampleCnt="+conf.get("leastSampleCnt"));
		super.setup(context);
	}

	protected void map(ImmutableBytesWritable row, Result value, Context context)
			throws IOException, InterruptedException {
		
		TableSplit split=(TableSplit)context.getInputSplit();
		TableName tableName = split.getTable();
//		System.out.println("这个split所属表是："+tableName.toString());
		NavigableMap<byte[], byte[]> navi = value.getFamilyMap(Bytes.toBytes("NCSINFO"));
//		System.out.println("navi.size="+navi.size());
		if((context.getConfiguration().get("2gHwNcsHbaseTable"))
				.equals(tableName.toString())) {
			distance=new String(navi.get("DISTANCE".getBytes()));
			//s3013
			denominator=new String(navi.get("S3013".getBytes()));
			//s361-s369
			ci_divider=String.valueOf(Long.parseLong(new String(navi.get("S361".getBytes())))-Long.parseLong(new String(navi.get("S369".getBytes()))));
			//s361-s366
			ca_divider=String.valueOf(Long.parseLong(new String(navi.get("S361".getBytes())))-Long.parseLong(new String(navi.get("S366".getBytes()))));
//			isNeighbour=new String(navi.get("DEFINED_NEIGHBOUR".getBytes()));
			ncell=new String(navi.get("NCELL".getBytes()));
			cell=new String(navi.get("CELL".getBytes()));
//			System.out.println("华为的数据:"+cell+","+ncell+","+ci_divider + ","
//					+ ca_divider + "," + distance +","+ denominator + "," + isNeighbour);
		} else if((context.getConfiguration().get("2gEriNcsHbaseTable"))
				.equals(tableName.toString())) {
			distance=new String(navi.get("DISTANCE".getBytes()));
			denominator=new String(navi.get("REPARFCN".getBytes()));
			ci_divider=new String(navi.get("CI_DIVIDER".getBytes()));
			ca_divider=new String(navi.get("CA_DIVIDER".getBytes()));
			isNeighbour=new String(navi.get("DEFINED_NEIGHBOUR".getBytes()));
			ncell=new String(navi.get("NCELL".getBytes()));
			cell=new String(navi.get("CELL".getBytes()));
//			System.out.println("爱立信的数据:"+cell+","+ncell+","+ci_divider + ","
//					+ ca_divider + "," + distance +","+ denominator + "," + isNeighbour);
		}

		double dis = Double.parseDouble(distance);
		double deno = Double.parseDouble(denominator);
//		System.out.println("限制条件：dis="+dis+",deno="+deno);
		if (dis <= farestDis && !cell.equals(ncell) && deno >= 1) {
//			System.out.println("数据:"+cell+","+ncell+","+ci_divider + ","
//					+ ca_divider + "," + distance +","+ denominator + "," + isNeighbour);
			context.write(new Text(cell + "," + ncell), new Text(ci_divider
					+ "," + ca_divider + "," + denominator + "," + distance
					+ "," + isNeighbour));
		}
	}
	
	@Override
	protected void cleanup(org.apache.hadoop.mapreduce.Mapper.Context context)
			throws IOException, InterruptedException {
		System.out.println("MatrixMapper 执行cleanup中..");
		super.cleanup(context);
	}
}


