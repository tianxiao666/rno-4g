package com.iscreate.op.service.rno.mapreduce.pci;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class PciImportMapper extends Mapper<Object, Text, Text, Text> {

	private static Log log = LogFactory	.getLog(PciImportMapper.class);
	Configuration conf = null;
	
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
	
	/** 关联度 **/
	private String assocDegree="";
	
	/** 同站小区 **/
	private String sameStationCells="";
	
	/** 行统计 **/
	private long counter = 0;
	
	/** 开始运行时间 **/
	private long startTimeMillis = 0;
	
	@Override
	public void setup(Context context)
			throws IOException, InterruptedException {
		conf = context.getConfiguration();
		System.out.println("进入到mapper");
		super.setup(context);
	}

	protected void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		//line=cell+"#"+ncellId+"#"+cosi+"#"+cellPci+"#"+ncellPci;
		//CELL##NCELL,COSI;CELL2,CELL3;CELLPCI;NCELLPCI
		//读入格式
		//ex:小区标识 ,	邻区标识,关联度,小区频点,小区PCI,邻区频点,邻区PCI,同站cells
		//修正格式
		//line=cell+"##"+ncellId+","+cosi+";"+cells+";"+cellPci+";"+ncellPci;
		//ncellId+","+cosi+";"+cells+";"+cellPci+";"+ncellPci;
		if (++counter % 10000 == 0) {
			System.out.println("map counter = " + counter);
		}
		String val = value.toString();
		log.debug("查看读取内容："+val);
		String vals[] = val.split(",|\t");
		if(isNumeric(vals[0])){			
			ncellId=vals[1];
			ncellBcch=vals[5];
			cellBcch=vals[3];
			if (cellBcch.equals(ncellBcch) && !"".equals(ncellBcch) && !"".equals(ncellId)){
				cellId=vals[0];
				cellPci=vals[4];
				ncellPci=vals[6];
				assocDegree=vals[2];
				sameStationCells=vals[7].replace("_", ",");
				//context.write(new Text(vals[0]), new Text(vals[1]+","+vals[2]+";"+vals[7].replace("_", ",")+";"+vals[4]+";"+vals[6]));
				context.write(new Text(cellId+","+cellPci+";"+sameStationCells), new Text(ncellId+","+ncellPci+","+assocDegree));
				}
			}
		}
	
	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
		System.out.println("map counter total = " + counter);
		System.out.println("map spent seconds = " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0));
	}
	/**
	 * 
	 * @title 判断是否为数字
	 * @param str
	 * @return
	 * @author chao.xj
	 * @date 2015-5-20下午3:51:12
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public boolean isNumeric(String str)
	{
	Pattern pattern = Pattern.compile("[0-9]*");
	Matcher isNum = pattern.matcher(str);
	if( !isNum.matches() )
	{
	return false;
	}
	return true;
	} 
}
