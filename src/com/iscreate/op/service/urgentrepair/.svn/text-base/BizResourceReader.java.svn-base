package com.iscreate.op.service.urgentrepair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;


public class BizResourceReader {
	/**
	 * 获取 指定文件下的指定 类型的资源的映射信息
	 * 根据映射文件获取对应城市的故障大类数据字典，在WorkOrderCommonAction.java 和 UrgentRepairSenceTaskOrderActionForMobile.java,UrgentRepairTechSupportTaskOrderActionForMobile.java 用到
	 * mapping文件 是 acceptProfessional.mapping，faultGenera.mapping，faultType.mapping
	 * @title ResourceReader.java
	 * @description TODO
	 * @param filename
	 * @param resourceType
	 * @return
	 */
	public static String getCityToTreeNameMappingInfo(
			String filename, String resourceType) {

		Properties p=null;
		String value="";
		try {
			InputStream in=BizResourceReader.class.getResourceAsStream(filename);
			BufferedReader br=new BufferedReader(new InputStreamReader(in));
			String str=null;
			p=new Properties();
			p.load(in);
			Set<String> keys=p.stringPropertyNames();
			
			for(String key1 : keys){
				String key = new String(key1.getBytes("ISO-8859-1"),"UTF-8");
				if(resourceType.equals(key)){
					value=new String(p.getProperty(key1).getBytes("ISO-8859-1"),"UTF-8");
					if(value!=null&&!"".equals(value)){
						return value;
					}
				}
				
			}
			
			
			in.close();

		} catch (Exception e) {
			return null;
		}
		return value;
	}
}
