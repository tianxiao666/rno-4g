package com.iscreate.op.action.informationmanage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.service.informationmanage.InformationManageAreaService;
import com.iscreate.op.service.informationmanage.InformationManageNetworkResourceService;
import com.iscreate.op.service.system.SysAreaService;

/**
 * @author andy
 */
public class InformationManageAreaAction {

	/** *********** 依赖注入 ************ */
	private InformationManageAreaService informationManageAreaService;
	
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	
	

	private SysAreaService sysAreaService;

	/** ******* 属性 ******** */
	private Log log = LogFactory.getLog(this.getClass());

	/** ************** action ***************** */

	/**
	 * 根据组织架构id,递归查找上级组织
	 * 
	 */
	public void areaTreeAction() {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap(
				"max", "provinceId","projectId");
		Integer max = Integer.valueOf(requestParamMap.get("max"));
		String provinceId = requestParamMap.get("provinceId");
		String projectId = requestParamMap.get("projectId");
		// Integer.valueOf(provinceId)
		// TODO
		// List<Map<String, Object>> list =
		// informationManageAreaService.getAreaTreeView(max,provinceId);
		List<Map<String, Object>> list = sysAreaService.getAreaTreeList(Integer
				.valueOf(provinceId), max);
		Map<String, String> m2 = new HashMap<String, String>();
		m2.put("projectId", projectId);
		List<Map<String,Object>> findResourceByOrgProjectAreaIdWithOutStatus = informationManageNetworkResourceDao.findResourceByOrgProjectAreaIdWithOutStatus(null, projectId, null);
		Set<Object> areaIdSet = new HashSet<Object>();
		if(findResourceByOrgProjectAreaIdWithOutStatus != null){
			for(Map<String,Object> m:findResourceByOrgProjectAreaIdWithOutStatus){
				areaIdSet.add(m.get("areaId"));
			}
		}
		setNetworkResChecked(list, areaIdSet);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setNetworkResChecked(List<Map<String, Object>> list,Set<Object> set){
		if(list != null){
			for(Map<String, Object> m: list){
				if(set.contains(m.get("areaId"))){
					m.put("checked", true);
				}else{
					m.put("checked", false);
				}
				if(m.get("children") != null){
					List<Map<String, Object>> list1 = (List<Map<String, Object>>)m.get("children");
					setNetworkResChecked(list1,set);
				}
			}
		}else{
			return;
		}
	}

	public InformationManageAreaService getInformationManageAreaService() {
		return informationManageAreaService;
	}

	public void setInformationManageAreaService(
			InformationManageAreaService informationManageAreaService) {
		this.informationManageAreaService = informationManageAreaService;
	}

	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}

	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	

	/** ********* getter setter ********** */

}
