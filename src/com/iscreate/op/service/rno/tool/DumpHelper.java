package com.iscreate.op.service.rno.tool;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class DumpHelper {

	/**
	 * 判断指定模块是否需要dump
	 * @param code
	 * @return
	 * @author brightming
	 * 2014-7-11 上午10:03:39
	 */
	public static boolean needDump(String code){
		if(StringUtils.isEmpty(code)){
			return false;
		}
		Properties prop=getProperty();
		if(prop==null){
			return false;
		}
		String val=prop.getProperty(code);
		return Boolean.parseBoolean(val);
	}

	/**
	 * 获取指定code的dump信息的存储路径
	 * @param code
	 * @return
	 * @author brightming
	 * 2014-7-13 上午10:25:09
	 */
	public static String getDumpDir(String code) {
		Properties prop=getProperty();
		return prop.getProperty(code+"-dir");
	}
	
	private static Properties getProperty(){
		InputStream is=null;
		Properties prop = null;
		try {
			//需要实时读取
			is=new BufferedInputStream(new FileInputStream(DumpHelper.class.getResource("/dump/dumpconfig.cfg").getPath()));
			prop=new Properties(); 
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prop;
	}
	
	public static void main(String[] args) {
		System.out.println(StringUtils.isEmpty(getDumpDir("struct-ana")));
	}
}
