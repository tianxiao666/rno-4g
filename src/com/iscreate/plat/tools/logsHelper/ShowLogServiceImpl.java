package com.iscreate.plat.tools.logsHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

public class ShowLogServiceImpl {
	
	public static List<String> list = null;
	
	/**
	 * 获取Log
	 * @param size
	 * @return
	 */
	public List<String> showLogService(int size){
		List<String> returnList = new ArrayList<String>();
		if(this.list==null || this.list.size()==0){
			this.list = new ArrayList<String>();
			String prefixPath = ServletActionContext.getServletContext().getRealPath("");
			prefixPath += "/logs/ops.log";
			try {
				FileReader fr = new FileReader(prefixPath);
				BufferedReader br = new BufferedReader(fr);
				int b;
			    while((b=br.read()) != -1){
			    	//System.out.println(br.readLine());
			    	this.list.add(br.readLine());
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int totalCount = this.list.size();
		if(size >= totalCount){
			returnList = this.list;
		}else{
			for(int i=(totalCount-size);i<totalCount;i++){
				returnList.add(this.list.get(i));
			}
		}
		return returnList;
	}
}
