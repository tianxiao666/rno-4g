package com.iscreate.op.action.networkresourcemanage;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.service.networkresourcemanage.GisDispatchAreaResourceService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.op.service.publicinterface.SessionService;

public class GisDispatchAreaResourceAction {
	
	/** 依赖注入 ************************/
	private GisDispatchAreaResourceService gisDispatchAreaService;
	private StaffOrganizationService staffOrganizationService;
	
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
		List<Map<String, Object>> list = this.gisDispatchAreaService.gisfindAreaListByUserId(loginPerson);
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
		HttpServletRequest request = ServletActionContext.getRequest();
		String order = request.getParameter("order");
		String name = request.getParameter("name");
		//String order = p_map.get("order");
		Map<String, Map<String, Object>> result_map = null;
		if ( order == null ) {
		} else if (order.equals("按省份") ) {
			result_map = gisDispatchAreaService.cityListByProvince(name);
		} else {
			result_map = gisDispatchAreaService.cityListByCity(name);
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
		HttpServletRequest request = ServletActionContext.getRequest();
		String areaId = request.getParameter("areaId");
		List<Map<String, Object>> list = gisDispatchAreaService.gisSubAreaList(areaId);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @description: 获取用户所属区域及其父区域id String List
	 * @author：  yuan.yw   
	 * @return void     
	 * @date：Mar 6, 2013 9:57:24 AM
	 */
	public void  getUserParentAreaAndAreaIdsListAction(){
		//获取session记载的登录用户
		String loginPerson = (String) SessionService.getInstance().getValueByKey("userId");
		List<String> list = gisDispatchAreaService.getUserParentAreaAndAreaIdsList(loginPerson);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	

	
	/** getter setter *******************************/
	public GisDispatchAreaResourceService getGisDispatchAreaService() {
		return gisDispatchAreaService;
	}
	public void setGisDispatchAreaService(
			GisDispatchAreaResourceService gisDispatchAreaService) {
		this.gisDispatchAreaService = gisDispatchAreaService;
	}
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}
	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}
	
	
	
}
