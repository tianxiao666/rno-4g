package com.iscreate.op.action.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.logsHelper.ShowLogServiceImpl;

public class ShowLogAction {

	private int size;
	private int totalSize;
	
	/**
	 * 获取Log
	 */
	public void loadLogAction(){
		String json="";
		Gson gson=new Gson();
		this.totalSize = this.size + this.totalSize;
		ShowLogServiceImpl s = new ShowLogServiceImpl();
		Map<String,Object> map = new HashMap<String, Object>();
		List<String> showLogService = s.showLogService(this.totalSize);
		map.put("totalSize", this.totalSize);
		map.put("list", showLogService);
		json=gson.toJson(map);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("获取Log的操作时返回页面信息出错");
		}
	}
	
	/**
	 * 清空Log数据
	 */
	public void cleanLogAction(){
		ShowLogServiceImpl.list = null;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
}
