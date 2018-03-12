package com.iscreate.op.service.cardispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.dao.cardispatch.CardispatchCommonDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.informationmanage.InformationManageAreaService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.networkresource.common.tool.LatLngConversion;
import com.iscreate.plat.tools.map.MapHelper;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchCommonServiceImpl implements CardispatchCommonService {

	/************ 依赖注入 ***********/
	private CardispatchCommonDao cardispatchCommonDao;
	InformationManageAreaService informationManageAreaService;
	private NetworkResourceInterfaceService networkResourceService ; 
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	private Log log = LogFactory.getLog(this.getClass());
	/****************** service *******************/
	
	public List<Map<String, String>> getAllProvince () {
		//获取所有省份
		List<Map<String, String>> allProvince = informationManageAreaService.getAllProvince();
		return allProvince;
	}

	public List<Map<String, String>> getSubArea ( String areaId ) {
		List<Map<String, String>> allProvince = informationManageAreaService.getSubArea(areaId);
		return allProvince;
	}
	
	/**
	 * 根据基站id和类型,获取所在区域
	 * @param baseStationId 基站id
	 * @param baseStationType 基站类型
	 * @return
	 */
	public List<String> getAreaIdListByBaseStationIdAndType ( String baseStationId , String baseStationType  ) {
		List<String> list = new ArrayList<String>();
		Map<String, String> map = networkResourceService.getStationByBaseStationIdAndBaseStationType(baseStationId, baseStationType, "Sys_Area");
		if ( map != null && map.size() > 0 && map.containsKey("id") ) {
			list.add(map.get("id"));
			List<String> parentAreaByAreaId = getParentAreaByAreaId(map.get("id"));
			list.addAll(parentAreaByAreaId);
		}
		return list;
	}
	
	
	
	public List<String> getAreaIdListByStationId ( String stationId ) {
		List<String> list = new ArrayList<String>();
		//获取上级地区
		List<Map<String, String>> parentArea_list = networkResourceService.getResourceService(stationId, "Station", "Sys_Area", "PARENT");
		Map<String,String> area_map = new HashMap<String, String>();
		if ( parentArea_list != null && parentArea_list.size() > 0 ) {
			area_map = parentArea_list.get(0);
			list.add(area_map.get("id"));
			List<String> parentAreaByAreaId = getParentAreaByAreaId(area_map.get("id"));
			list.addAll(parentAreaByAreaId);
		}
		return list;
	}
	
	public List<String> getAreaIdByLoginPerson () {
		List<String> list = new ArrayList<String>();
		//当前登录人信息
		String loginPerson = (String) SessionService.getInstance().getValueByKey("userId");
		List<Map<String, String>> accountArea_list = networkResourceService.getAreaByAccountService(loginPerson);
		Map<String,String> area_map = new HashMap<String, String>();
		if ( accountArea_list != null && accountArea_list.size() > 0 ) {
			area_map = accountArea_list.get(0);
			list.add(area_map.get("id"));
			List<String> parentAreaByAreaId = getParentAreaByAreaId(area_map.get("id"));
			list.addAll(parentAreaByAreaId);
		}
		return list;
	}
	
	
	public List<String> getParentAreaByAreaId ( String areaId ) {
		List<String> list = new ArrayList<String>();
		if ( areaId == null || areaId.isEmpty() ) {
			return list;
		}
		List<Map<String, String>> parentArea_list = networkResourceService.getResourceService(areaId, "Sys_Area", "Sys_Area", "PARENT");
		Map<String,String> area_map = new HashMap<String, String>();
		if ( parentArea_list != null && parentArea_list.size() > 0 ) {
			area_map = parentArea_list.get(0);
			list.add(area_map.get("id"));
			List<String> parentAreaByAreaId = getParentAreaByAreaId(area_map.get("id"));
			list.addAll(parentAreaByAreaId);
		}
		return list;
	}
	
	/**
	 * 根据资源id、类型,获取资源信息
	 * @param resId -- 资源Id
	 * @param resType -- 资源类型
	 * @return 资源信息
	 */
	public Map<String,String> getResoueceInfo ( String resId , String resType ) {
		Map<String, String> resourceByReIdAndReTypeService = this.networkResourceService.getResourceByReIdAndReTypeService(resId, resType);
		return resourceByReIdAndReTypeService;
	}
	
	
	/**
	 * 获取当前登录人的下级组织(不递归)
	 * @return (List<Map<String, String>>) 当前登录人的下级组织
	 */
	public List<Map<String, String>> getUserDownOrg () {
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(accountId);
		//yuan.yw
		List<SysOrg> list = this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		List<Map<String,String>> result_list = new ArrayList<Map<String,String>>();
		for (int i = 0; list != null && i < list.size() ; i++) {
			SysOrg p = list.get(i);
			//List<ProviderOrganization> down_list = providerOrganizationService.getOrgListDownwardByOrg(p.getId());
			//yuan.yw
			List<SysOrg> down_list = this.sysOrganizationService.getOrgListDownwardByOrg(p.getOrgId());
			for (int j = 0; down_list != null && j < down_list.size() ; j++) {
				SysOrg pro = down_list.get(j);
				try {
					Map<String, String> map = ObjectUtil.object2MapString(pro, false);
					result_list.add(map);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return result_list;
	}
	
	
	/**
	 * 
	 * @description: 根据组织id获取子组织列表 (组织id为空，获取当前登录人所在组织)
	 * @author：yuan.yw
	 * @param orgId
	 * @param account
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 4:38:35 PM
	 */
	
	public List<Map<String,Object>> getChildOrgListByOrgId(String orgId,String account){
		log.info("进入getChildOrgListByOrgId(String orgId)，根据组织id获取子组织列表 (组织id为空，获取当前登录人所在组织)");
		log.info("进入getChildOrgListByOrgId(String orgId),参数组织id orgId="+orgId);
		List<Map<String,Object>> resultList=null;
		long id = 0;
		List<SysOrg> orgList=null;
		if(orgId!=null && !"".equals(orgId)){
			id= Long.valueOf(orgId);
			orgList=this.sysOrganizationService.getOrgListDownwardByOrg(id);
			
		}else{
			orgList=this.sysOrganizationService.getOrgByAccountService(account);
		}
		if(orgList!=null && !orgList.isEmpty()){
			resultList = new ArrayList<Map<String,Object>>();
			for(SysOrg org:orgList){
				if(id==org.getOrgId()){
					continue;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("orgId",org.getOrgId()+"");
				map.put("name", org.getName());
				resultList.add(map);
			}
		}
		log.info("退出getChildOrgListByOrgId(String orgId)，返回结果"+resultList);
		return resultList;
	}
	
	/***************** getter setter *******************/
	public CardispatchCommonDao getCardispatchCommonDao() {
		return cardispatchCommonDao;
	}
	public void setCardispatchCommonDao(CardispatchCommonDao cardispatchCommonDao) {
		this.cardispatchCommonDao = cardispatchCommonDao;
	}
	public InformationManageAreaService getInformationManageAreaService() {
		return informationManageAreaService;
	}
	public void setInformationManageAreaService(
			InformationManageAreaService informationManageAreaService) {
		this.informationManageAreaService = informationManageAreaService;
	}
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}
	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}


	
	
}
