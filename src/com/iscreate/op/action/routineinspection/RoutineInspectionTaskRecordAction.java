package com.iscreate.op.action.routineinspection;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;
import com.iscreate.op.service.routineinspection.RoutineInspectionTaskRecordService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class RoutineInspectionTaskRecordAction {

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskRecordService routineInspectionTaskRecordService;
	private String TOID;
	private String WOID;
	private String eType;
	private String eId;
	private String tempIdJsonStr;
	private String valueJsonStr;
	private String remarkJsonStr;

	/**
	 * 根据专业获取巡检资源
	 */
	public void getRoutineInspectionResourceByProfessionAction(){
		String json="";
		Gson gson=new Gson();
		Map<String, List<Map<String, String>>> list = this.routineInspectionTaskRecordService.getRoutineInspectionResourceByProfessionAndToIdService(null, this.TOID , "pc");
		json=gson.toJson(list);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC获取巡检记录模板操作时返回页面信息出错");
		}
	}
	
	/**
	 * 获取巡检记录模板
	 */
	public void getRoutineInspectionRecordTemplateAction(){
		String json="";
		Gson gson=new Gson();
		List<Map<String,String>> list = this.routineInspectionTaskRecordService.getRoutineInspectionRecordTemplateByReIdAndReTypeAndWoIdService(null, eType, this.WOID , "pc");
		json=gson.toJson(list);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC获取巡检记录模板操作时返回页面信息出错");
		}
	}
	
	/**
	 * 获取巡检内容实例
	 */
	public void getRoutineInspectionRecordExampleAction(){
		String json="";
		Gson gson=new Gson();
		List<Map<String,String>> list = this.routineInspectionTaskRecordService.getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdService(this.eId, this.eType, this.TOID);
		json=gson.toJson(list);
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC获取巡检记录模板操作时返回页面信息出错");
		}
	}
	
	/**
	 * 保存巡检内容
	 */
	public void saveRoutineInspectionRecordAction(){
		String json="";
		Gson gson=new Gson();
		boolean result = this.routineInspectionTaskRecordService.txSaveRoutineInspectionRecordService(this.eId, this.eType, this.WOID, this.TOID, tempIdJsonStr, valueJsonStr, remarkJsonStr);
		json=gson.toJson(result+"");
		try {
			ServletActionContext.getResponse().getWriter().write(json);
		} catch (IOException e) {
			throw new UserDefinedException("做PC获取巡检记录模板操作时返回页面信息出错");
		}
	}
	
	public RoutineInspectionTaskRecordService getRoutineInspectionTaskRecordService() {
		return routineInspectionTaskRecordService;
	}
	public void setRoutineInspectionTaskRecordService(
			RoutineInspectionTaskRecordService routineInspectionTaskRecordService) {
		this.routineInspectionTaskRecordService = routineInspectionTaskRecordService;
	}
	public String getTOID() {
		return TOID;
	}
	public void setTOID(String toid) {
		TOID = toid;
	}

	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public String getEType() {
		return eType;
	}

	public void setEType(String type) {
		eType = type;
	}

	public String getEId() {
		return eId;
	}

	public void setEId(String id) {
		eId = id;
	}

	public String getTempIdJsonStr() {
		return tempIdJsonStr;
	}

	public void setTempIdJsonStr(String tempIdJsonStr) {
		this.tempIdJsonStr = tempIdJsonStr;
	}

	public String getValueJsonStr() {
		return valueJsonStr;
	}

	public void setValueJsonStr(String valueJsonStr) {
		this.valueJsonStr = valueJsonStr;
	}

	public String getRemarkJsonStr() {
		return remarkJsonStr;
	}

	public void setRemarkJsonStr(String remarkJsonStr) {
		this.remarkJsonStr = remarkJsonStr;
	}
	
}
