package com.iscreate.op.action.gisdispatch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.service.gisdispatch.GisDispatchAreaService;

import com.iscreate.op.service.publicinterface.SessionService;

public class GisDispatchAreaAction {
	
	/** 依赖注入 ************************/
	private GisDispatchAreaService gisDispatchAreaService;

	
	/** 属性 ****************/
	private Log log = LogFactory.getLog(this.getClass());
	private Map<String,String> p_map = new HashMap<String, String>();
	
	
	

	/** Action *****************************************************/
	
	/**
	 * 获取当前登录人
	 */
	public void gisLoginUserAreaListAction () {
		//获取session记载的登录用户
		String loginPerson = (String) SessionService.getInstance().getValueByKey("userId");	
		List<Map<String, String>> list = this.gisDispatchAreaService.gisfindAreaListByUserId(loginPerson);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 城市列表(按省份)
	 */
	public void gisCityListAction () {
		String order = p_map.get("order");
		Map<String, Map<String, Object>> result_map = null;
		if ( order.equals("按省份") ) {
			result_map = gisDispatchAreaService.cityListByProvince(p_map.get("name"));
		} else {
			result_map = gisDispatchAreaService.cityListByCity(p_map.get("name"));
		}
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 城市列表(按省份)
	 */
	public void gisSubAreaListAction () {
		
		List<Map<String, String>> list = gisDispatchAreaService.gisSubAreaList(p_map.get("areaId"));
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	

	
	public GisDispatchAreaService getGisDispatchAreaService() {
		return gisDispatchAreaService;
	}

	public void setGisDispatchAreaService(
			GisDispatchAreaService gisDispatchAreaService) {
		this.gisDispatchAreaService = gisDispatchAreaService;
	}
	public Map<String, String> getP_map() {
		return p_map;
	}
	public void setP_map(Map<String, String> p_map) {
		this.p_map = p_map;
	}
	
	
	
	
	
}
