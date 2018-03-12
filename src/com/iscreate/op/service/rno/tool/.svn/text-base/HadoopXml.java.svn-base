package com.iscreate.op.service.rno.tool;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;


public class HadoopXml {

	public static Configuration getHbaseConfig() {
		Configuration conf=new Configuration();
		conf=HBaseConfiguration.create(conf);
		conf.addResource(new Path(HadoopXml.getHbaseXmlPath()));
//		conf.addResource(new Path(HadoopXml.getCoreXmlPath()));
		return conf;
	}
	public static String getCoreXmlPath() {
//		String path=System.getProperty("user.dir")+"/conf/hadoop/core-site.xml";
//		String path=HadoopXml.class.getResource("/").getPath()+"hadoop/core-site.xml";
		String path=HadoopXml.class.getClassLoader().getResource("core-site.xml").toString();
		return path;
	}
	public static String getHbaseXmlPath() {
//		String path=System.getProperty("user.dir")+"/conf/hbase/hbase-site.xml";
//		String path=HadoopXml.class.getResource("/").getPath()+"hbase/hbase-site.xml";
		String path=HadoopXml.class.getClassLoader().getResource("hbase-site.xml").toString();
		return path;
	}
	public static String getHdfsXmlPath() {
//		String path=System.getProperty("user.dir")+"/conf/hadoop/hdfs-site.xml";
//		String path=HadoopXml.class.getResource("/").getPath()+"hadoop/hdfs-site.xml";
		String path=HadoopXml.class.getClassLoader().getResource("hdfs-site.xml").toString();
		return path;
	}
	public static String getMapredXmlPath() {
//		String path=System.getProperty("user.dir")+"/conf/hadoop/mapred-site.xml";
//		String path=HadoopXml.class.getResource("/").getPath()+"hadoop/mapred-site.xml";
		String path=HadoopXml.class.getClassLoader().getResource("mapred-site.xml").toString();
		return path;
	}
	public static String getYarnXmlPath() {
//		String path=System.getProperty("user.dir")+"/conf/hadoop/yarn-site.xml";
//		String path=HadoopXml.class.getResource("/").getPath()+"hadoop/yarn-site.xml";
		String path=HadoopXml.class.getClassLoader().getResource("yarn-site.xml").toString();
		return path;
	}
	public static void main(String[] args) {
		String path=getYarnXmlPath();
		System.out.println(path);
	}
}
