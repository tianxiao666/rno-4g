package com.iscreate.op.action.informationmanage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.service.informationmanage.InformationManageNetworkResourceService;


@SuppressWarnings("unused")
public class InformationManageNetworkResourceAction {
	
	/************* 依赖注入 *************/
	private InformationManageNetworkResourceService informationManageNetworkResourceService;

	


	/************* 属性 *************/
	private Log log = LogFactory.getLog(this.getClass());
	
	/************* Action *************/
	
	public void findSingleProjectResource () {
		
		
	}
	
	/**
	 * 根据地区id,组织id,项目id,查询所维护的资源
	 * @page - 项目服务范围 - 项目网络资源
	 * @write 操作结果 - 资源集合
	 */
	public void checkProjectResourceIsexists () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("areaId","orgId","projectId");
		ArrayUtil.removeEmpty(requestParamMap);
		Map<String,Map<String,Object>> list = informationManageNetworkResourceService.checkProjectResourceIsexists(requestParamMap);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据地区id,组织id,项目id,查询所维护的资源
	 * @page - 项目服务范围 - 项目网络资源
	 * @write 操作结果 - 资源集合
	 */
	public void checkProjectResourceStateIsexists () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("areaId","orgId","projectId");
		ArrayUtil.removeEmpty(requestParamMap);
		Map<String,Map<String,String>> list = informationManageNetworkResourceService.checkProjectResourceStateIsexists(requestParamMap);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void saveProjectResourceStatus () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("areaId","orgId","projectId","checkedres","nocheckedres");
		
		informationManageNetworkResourceService.txSaveProjectResourceStatus(requestParamMap);
		try {
			ActionUtil.responseWrite(1);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 保存项目区域与网络资源信息
	 * @page - 项目服务范围 - 项目基本信息
	 * @write 操作结果
	 */
	public void saveProjectResource () { 
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("areaId","orgId","projectId","checkedres","nocheckedres");
		informationManageNetworkResourceService.txSaveProjectResource(requestParamMap);
		try {
			ActionUtil.responseWrite(1);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据组织Id获取项目
	 * @author yuan.yw
	 */
	public void getOrgProjectByOrgIdAjaxAction(){
		String orgId = ActionUtil.getParamString("orgId");
		List<Map<String, String>> listMap = null;
		if(!StringUtil.isNullOrEmpty(orgId)){
			listMap = this.informationManageNetworkResourceService.findProjectByCarryOutOrgId(orgId);
		}	
		try {
			ActionUtil.responseWrite(listMap);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/*********** getter setter ***********/
	public InformationManageNetworkResourceService getInformationManageNetworkResourceService() {
		return informationManageNetworkResourceService;
	}
	public void setInformationManageNetworkResourceService(
			InformationManageNetworkResourceService informationManageNetworkResourceService) {
		this.informationManageNetworkResourceService = informationManageNetworkResourceService;
	}
	
	
}
