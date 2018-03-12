package com.iscreate.op.service.publicinterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.constant.ProjectConstant;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.informationmanage.ProjectInformationDao;
import com.iscreate.op.dao.networkresourcemanage.NetworkResourceQueryDao;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.dao.system.SysAreaRelaOrgDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgRelaArea;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.tools.InterfaceUtil;
import com.iscreate.plat.tools.firstLetter.FirstLetterService;


public class NetworkResourceInterfaceServiceImpl implements NetworkResourceInterfaceService {

	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	private FirstLetterService firstLetterService;
	private List<Map<String, String>> parentAreaList = null;
	private Log log = LogFactory.getLog(this.getClass());
	private String className = "com.iscreate.op.service.publicinterface.NetworkResourceInterfaceServiceImpl";
	private StructureCommonService structureCommonService;
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	private StaffOrganizationService staffOrganizationService;
	public Dictionary dictionary;
	private SysOrgUserService sysOrgUserService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	private SysAreaRelaOrgDao sysAreaRelaOrgDao;//区域与组织关联dao  du.hw
	
	
	private SysOrganizationDao sysOrganizationDao;//组织dao  du.hw
	
	private NetworkResourceQueryDao networkResourceQueryDao;//网络资源查询dao  du.hw
	
	private SysAreaDao sysAreaDao;//区域dao  du.hw
	

	private ProjectInformationDao projectInformationDao;//项目信息 dao
	
	public ProjectInformationDao getProjectInformationDao() {
		return projectInformationDao;
	}


	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysAreaRelaOrgDao getSysAreaRelaOrgDao() {
		return sysAreaRelaOrgDao;
	}

	public void setSysAreaRelaOrgDao(SysAreaRelaOrgDao sysAreaRelaOrgDao) {
		this.sysAreaRelaOrgDao = sysAreaRelaOrgDao;
	}


	public void setProjectInformationDao(ProjectInformationDao projectInformationDao) {
		this.projectInformationDao = projectInformationDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	

	private QuickSort<Map<String,Object>> quickSort;//排序
	
	/**
	 * 根据组织Id和资源类型获取基站信息
	 * @param orgId
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,Object>> getNetResourceByOrgIdAndResourceType(long orgId,String resourceType){
		log.info("进入getNetResourceByOrgIdAndResourceType方法");
		log.info("参数orgId="+orgId+",resourceType="+resourceType);
		List<Map<String,String>> resourceList = new ArrayList<Map<String,String>>();
		//根据组织Id和组织类型获取组织与区域的关系
		//List<Map<String,String>> oarList = this.providerOrganizationService.findAreaIdByProjectOrgIdResType(null,orgId+"",resourceType);
		//yuan.yw
		List<Map<String,String>> oarList = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(null,orgId+"",resourceType);
		if(oarList==null || oarList.size()<=0){
			return null;
		}
		//将组织管辖的区，区域资源放到set中
		Set<String> oraSet = new HashSet<String>();
		for(Map<String,String> oar : oarList){
			oraSet.add(oar.get("areaId"));
		}
		
		//拼接区域Id
		List<String> areaIds = new ArrayList<String>();
		if(oraSet!=null && oraSet.size()>0){
			for (String areaId : oraSet) {
				areaIds.add(areaId);
			}
		}
//		areaIdStr = areaIdStr.substring(0,areaIdStr.length()-1);
		List<Map<String, Object>> reList  = null;
		//获取环境代码
		String code = this.getNetworkResourceUrl();
		//获取网络资源的action
//		code = "http://192.168.206.229:8080/networkresourcemanage/";
//		String interfaceURL = code + "resourcewebservice/getBaseStationByAreaAction?areaId="+areaIdStr+"&resourceType="+resourceType;
//		String interfaceURL = "http://192.168.206.211:8080/networkresourcemanage/resourcewebservice/getBaseStationByAreaAction?areaId=1492,1493&resourceType=GeneralBaseStation";
		try {
//			res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
			reList  = structureCommonService.getBaseStationByAreasByHibernate(areaIds, resourceType);
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		} 
		log.info("执行getNetResourceByOrgIdAndResourceType方法成功，实现了”根据组织Id和资源类型获取基站信息“的功能");
		log.info("退出getNetResourceByOrgIdAndResourceType方法,返回List<Map<String,String>>集合");
		return reList;
	}
	
	
	/**
	 * 根据区域集合获取的基站集合
	* @author ou.jh
	* @date Jul 4, 2013 4:00:38 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getBaseStationByAreas(List<String> areaIds,String resourceType){
		log.info("进入getBaseStationByAreas方法");
		List<Map<String,Object>> baseStationByAreasByHibernate = structureCommonService.getBaseStationByAreasByHibernate(areaIds, resourceType);
		log.info("执行getBaseStationByAreas方法成功，实现了”根据区域集合获取的基站集合“的功能");
		log.info("退出getBaseStationByAreas方法,返回List<Map<String,String>>集合"+baseStationByAreasByHibernate);
		return baseStationByAreasByHibernate;
	}

	/**
	 * 根据区域Id和类型获取基站
	 * @param areaId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getBaseStationByAreaIdAndReType(String areaId,String reType){
		log.info("进入getBaseStationByAreaIdAndReType方法");
		log.info("参数areaId="+areaId+",reType="+reType);
		List<Map<String,String>> resourceList = new ArrayList<Map<String,String>>();
		//获取环境代码
		String code = this.getNetworkResourceUrl();
		//获取网络资源的action
//		code = "http://192.168.206.229:8080/networkresourcemanage/";
//		String interfaceURL = code + "resourcewebservice/getBaseStationByAreaAction?areaId="+areaId+"&resourceType="+reType;
//		String interfaceURL = "http://192.168.206.211:8080/networkresourcemanage/resourcewebservice/getBaseStationByAreaAction?areaId=1492,1493&resourceType=GeneralBaseStation";
		try {
//			res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
			List<String> areaIds = new ArrayList<String>();
			if(areaId == null || areaId.equals("")){
				log.info("areaId为空");
				return null;
			}else{
				areaIds.add(areaId);
			}
			List<Map<String, Object>> reList  = structureCommonService.getBaseStationByAreasByHibernate(areaIds, reType);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String res = gson.toJson(reList);
			if(res != null && !res.equals("")){
				resourceList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}else{
				log.info("reList为空");
			}
			//resourceList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getBaseStationByAreaIdAndReType方法成功，实现了”根据区域Id和类型获取基站“的功能");
		log.info("退出getBaseStationByAreaIdAndReType方法,返回List<Map<String,String>>集合");
		return resourceList;
	}
	
	
	/**
	 * 根据区域Id和类型获取资源
	 * @param areaId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceByAreaIdAndReType(String areaId,String reType){
		log.info("进入getResourceByAreaIdAndReType方法");
		log.info("参数areaId="+areaId+",reType="+reType);
		List<Map<String,String>> resourceList = new ArrayList<Map<String,String>>();
		//获取网络资源的action
//		code = "http://192.168.206.233:8080/networkresourcemanage/";
//		String interfaceURL = code + "resourcewebservice/getResByAreasRecursionAction?areaId="+areaId+"&selectReType="+reType;
//		String interfaceURL = "http://192.168.206.211:8080/networkresourcemanage/resourcewebservice/getBaseStationByAreaAction?areaId=1492,1493&resourceType=GeneralBaseStation";
//		String res = "";
		try {
//			res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
//			GsonBuilder builder = new GsonBuilder();
//			Gson gson = builder.create();
//			resourceList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			
			
			
			List<String> arIds = new ArrayList<String>();
			if(areaId != null && !areaId.equals("")){
				String[] split = areaId.split(",");
				if(split != null && split.length > 0){
					for(String s : split){
						arIds.add(s);
					}
				}
			}else{
				log.info("areaId为空");
				return null;
			}
			List<Map<String, Object>> reList = structureCommonService.getResByAreasHibernate(arIds, reType,null);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String res = gson.toJson(reList);
			if(res != null && !res.equals("")){
				resourceList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}else{
				log.info("reList为空");
			}
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getResourceByAreaIdAndReType方法成功，实现了”根据区域Id和类型获取资源“的功能");
		log.info("退出getResourceByAreaIdAndReType方法,返回List<Map<String,String>>集合");
		return resourceList;
	}
	
	
	/**
	 * 根据基站Id和和资源类型获取组织架构
	 * @param baseStationId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public List<SysOrg> getOrgByBaseStationIdAndOrgTypeAndResourceType(long baseStationId,String orgType,String resourceType){
		log.info("进入getOrgByBaseStationIdAndOrgTypeAndResourceType方法");
		log.info("参数baseStationId="+baseStationId+",orgType="+orgType+",resourceType="+resourceType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "networkresourcemanage/resourcewebservice/getAreaByBaseStationAction?baseStationId="+baseStationId+"&baseStationType="+resourceType;
//		String interfaceURL = "http://192.168.206.211:8080/networkresourcemanage/resourcewebservice/getAreaByBaseStationAction?baseStationId="+baseStationId+"&baseStationType="+resourceType;
		Map<String,String> map = new HashMap<String, String>();
		try {
			String reId = "";
			if(baseStationId > 0){
				reId = baseStationId + "";
			}else{
				log.info("baseStationId不存在");
				return null;
			}
			BasicEntity areaByStation = structureCommonService.getAreaByStation(reId, resourceType);
			Map<String, Object> areaByStationMap = null;
			if(areaByStation != null){
				areaByStationMap = areaByStation.toMap();
			}
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String res = gson.toJson(areaByStationMap);
			if(res != null && !res.equals("")){
				map = gson.fromJson(res,new TypeToken<Map<String,String>>(){}.getType());
			}else{
				log.info("res为空");
			}
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		//List<ProviderOrganization> list = new ArrayList<ProviderOrganization>();
		//Set<ProviderOrganization> set = new HashSet<ProviderOrganization>();
		
		List<SysOrg> list = new ArrayList<SysOrg>();
		Set<SysOrg> set = new HashSet<SysOrg>();
		if(map!=null){
			String areaId = map.get("id");
			//List<String> findOrgIdByAreaId = this.providerOrganizationService.findOrgIdByAreaId(areaId);
			//通过区域标识得到关联的组织
			List<SysOrgRelaArea> findOrgIdByAreaId = sysAreaRelaOrgDao.getOrgListByAreaId(Long.parseLong((String)areaId));
			if(findOrgIdByAreaId!=null && findOrgIdByAreaId.size()>0){
				for (SysOrgRelaArea org : findOrgIdByAreaId) {
					long orgId = org.getOrgId();
					SysOrg po = sysOrganizationDao.getOrgByOrgId(orgId);
					set.add(po);
				}
			}else{
				set = getUpRecursiveAreaToOrgByAreaIdService(areaId);
			}
		}
		if(set!=null && set.size()>0){
			list.addAll(set);
		}
		log.info("执行getOrgByBaseStationIdAndOrgTypeAndResourceType方法成功，实现了”根据基站Id和和资源类型获取组织架构“的功能");
		log.info("退出getOrgByBaseStationIdAndOrgTypeAndResourceType方法,返回List<ProviderOrganization>集合");
		return list;
	}
	
	/**
	 * 向上递归获取区域相关联的组织
	 * @param areaId
	 */
	private Set<SysOrg> getUpRecursiveAreaToOrgByAreaIdService(String areaId){
		log.info("进入getUpRecursiveAreaToOrgByAreaIdService方法");
		log.info("参数areaId="+areaId);
		
		Set<SysOrg> set = new HashSet<SysOrg>();
		Map<String,Object> area = sysAreaDao.getParentAreaByAreaId(Long.parseLong((String)areaId));
		if(area != null){
				//通过区域标识得到关联的组织
				List<SysOrgRelaArea> orgList = sysAreaRelaOrgDao.getOrgListByAreaId(Long.parseLong((String)area.get("areaId")));
		        if(orgList != null && orgList.size() > 0){
		        	for (SysOrgRelaArea org : orgList) {
						long orgId = org.getAreaId();
						SysOrg po = sysOrganizationDao.getOrgByOrgId(orgId);
						set.add(po);
					}
		        	return set;
		        }else{
		        	getUpRecursiveAreaToOrgByAreaIdService((String)area.get("areaId"));
		        }
		}
		return set;
		
	}
	
	/**
	 * 根据组织Id和资源类型获取其父区域
	 * @param orgId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public Map<String,String> getParentAreaByOrgIdAndReType(long orgId,String orgType,String resourceType){
		log.info("进入getParentAreaByOrgIdAndReType方法");
		log.info("参数orgId="+orgId+",orgType="+orgType+",resourceType="+resourceType);
		List<Map<String, String>> areaList = getArea(orgId,orgType,resourceType,"PARENT");
		log.info("执行getParentAreaByOrgIdAndReType方法成功，实现了”根据组织Id和资源类型获取其父区域“的功能");
		log.info("退出getParentAreaByOrgIdAndReType方法,返回Map<String,String>集合");
		if(areaList!=null && areaList.size()>0){
			return areaList.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据组织Id和资源类型获取其子区域
	 * @param orgId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,String>> getChildrenAreaByOrgIdAndReType(long orgId,String orgType,String resourceType){
		log.info("进入getChildrenAreaByOrgIdAndReType方法");
		log.info("参数orgId="+orgId+",orgType="+orgType+",resourceType="+resourceType);
		List<Map<String,String>> list = getArea(orgId,orgType,resourceType,"CHILD");
		log.info("执行getChildrenAreaByOrgIdAndReType方法成功，实现了”根据组织Id和资源类型获取其子区域“的功能");
		log.info("退出getChildrenAreaByOrgIdAndReType方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 根据基站Id和基站类型获取站址
	 */
	public Map<String,String> getStationByBaseStationIdAndBaseStationType(String baseStationId,String stationType,String selectReType){
		log.info("进入getStationByBaseStationIdAndBaseStationType方法");
		log.info("参数baseStationId="+baseStationId+",stationType="+stationType+",selectReType="+selectReType);
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resourcewebservice/getStationByBaseStationAction?baseStationId="+baseStationId+"&baseStationType="+stationType+"&selectReType="+selectReType;
//		String res = "";
		Map<String,String> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<BasicEntity> stationByBaseStation = structureCommonService.getStationByBaseStation(baseStationId, stationType, selectReType);
			Map<String, Object> stationByBaseStationMap = new HashMap<String, Object>();
			if(stationByBaseStation != null && stationByBaseStation.size() > 0){
				stationByBaseStationMap = stationByBaseStation.get(0).toMap();
			}
			//res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
			String res = gson.toJson(stationByBaseStationMap);
			if(res != null && !res.equals("")){
				map = gson.fromJson(res,new TypeToken<Map<String,String>>(){}.getType());
			}else{
				log.info("res为空");
			}
		}catch(Exception e){
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getStationByBaseStationIdAndBaseStationType方法成功，实现了”根据基站Id和基站类型获取站址“的功能");
		log.info("退出getStationByBaseStationIdAndBaseStationType方法,返回Map<String,String>集合");
		return map;
	}
	
	/**
	 * 根据站址Id获取区域
	 * @param stationId
	 * @return
	 */
	public Map<String,String> getAreaByStationId(String stationId,String stationType){
		log.info("进入getAreaByStationId方法");
		log.info("参数stationId="+stationId+",stationType="+stationType);
		String code = this.getNetworkResourceUrl();
		//String interfaceURL = code + "resourcewebservice/getResourceByAssociatedTypeAction?reId="+stationId+"&reType="+stationType+"&selectReType=Area&associatedType=PARENT";
//		String res = "";
		Map<String,String> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<Map<String, Object>> listObj = getResourceByAssociatedType(
					stationId, stationType,"Sys_Area",AssociatedType.PARENT);
			String res = gson.toJson(listObj);
			List<Map<String,String>> list = null;
			if(res != null && !res.equals("")){
				list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}else{
				return null;
			}
			if(list!=null && list.size()>0){
				map = list.get(0);
			}else{
				return map;
			}
		}catch(Exception e){
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getAreaByStationId方法成功，实现了”根据站址Id获取区域“的功能");
		log.info("退出getAreaByStationId方法,返回Map<String,String>集合");
		return map;
	}

	private List<Map<String, Object>> getResourceByAssociatedType(
			String stationId, String stationType,String selectType ,AssociatedType asociantedType) {
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(stationType, stationId);
		Gson gson = new Gson();
		//System.out.println(gson.toJson(sectionEntity));
		ApplicationEntity[] strutureSelationsApplicationEntity = structureCommonService.getStrutureSelationsApplicationEntity(sectionEntity,selectType, asociantedType, "networkresourcemanage");
		//System.out.println(sectionEntity);
		List<Map<String, Object>> listObj = new ArrayList<Map<String,Object>>();
		if(strutureSelationsApplicationEntity != null && strutureSelationsApplicationEntity.length > 0){
			log.info("开始循环strutureSelationsApplicationEntity");
			for(ApplicationEntity ae:strutureSelationsApplicationEntity){
				listObj.add(ae.toMap());
			}
			log.info("结束循环strutureSelationsApplicationEntity");
		}
		//System.out.println(gson.toJson(listObj));
		return listObj;
	}
	
	/**
	 * 根据区域Id获取父级区域
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public Map<String,String> getParentAreaByAreaId(String areaId,String areaType){
		log.info("进入getParentAreaByAreaId方法");
		log.info("参数areaId="+areaId+",areaType="+areaType);
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resourcewebservice/getResourceByAssociatedTypeAction?reId="+areaId+"&reType="+areaType+"&selectReType=Area&associatedType=PARENT";
//		String res = "";
		Map<String,String> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<Map<String,Object>> listObj = getResourceByAssociatedType(
					areaId, areaType,"Sys_Area",AssociatedType.PARENT);
			String res = gson.toJson(listObj);
			List<Map<String,String>> list = null;
			if(res != null && !res.equals("")){
				list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}else{
				return null;
			}
			if(list!=null && list.size()>0){
				map = list.get(0);
			}else{
				return map;
			}
		}catch(Exception e){
			log.info("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getParentAreaByAreaId方法成功，实现了”根据区域Id获取父级区域“的功能");
		log.info("退出getParentAreaByAreaId方法,返回Map<String,String>集合");
		return map;
	}
	
	/**
	 * 递归获取父区域（包含传入的Id）
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaWithSelfByAreaIdService(String areaId,String areaType){
		log.info("进入getRecursionParentAreaWithSelfByAreaIdService方法");
		log.info("参数areaId="+areaId+",areaType="+areaType);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> resourceByReIdAndReTypeService = getResourceByReIdAndReTypeService(areaId,areaType);
		if(resourceByReIdAndReTypeService!=null && resourceByReIdAndReTypeService.size()>0){
			list.add(resourceByReIdAndReTypeService);
			List<Map<String,String>> subList = getRecursionParentAreaByAreaIdService(areaId,areaType);
			if(subList!=null && subList.size()>0){
				list.addAll(subList);
			}
		}
		log.info("执行getRecursionParentAreaWithSelfByAreaIdService方法成功，实现了”递归获取父区域（包含传入的Id）“的功能");
		log.info("退出getRecursionParentAreaWithSelfByAreaIdService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 递归获取父区域（不包含传入的Id）
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaByAreaIdService(String areaId,String areaType){
		log.info("进入getRecursionParentAreaByAreaIdService方法");
		log.info("参数areaId="+areaId+",areaType="+areaType);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> parentAreaByAreaId = getParentAreaByAreaId(areaId,areaType);
		if(parentAreaByAreaId!=null && parentAreaByAreaId.size()>0){
			list.add(parentAreaByAreaId);
			String subAreaId = parentAreaByAreaId.get("id");
			List<Map<String,String>> subList = getRecursionParentAreaByAreaIdService(subAreaId,areaType);
			if(subList!=null && subList.size()>0){
				list.addAll(subList);
			}
		}
		log.info("执行getRecursionParentAreaByAreaIdService方法成功，实现了”递归获取父区域（不包含传入的Id）“的功能");
		log.info("退出getRecursionParentAreaByAreaIdService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 获取资源
	 * @param reId
	 * @param reType
	 * @param selectReType
	 * @param associatedType
	 * @return
	 */
	public List<Map<String,String>> getResourceService(String reId,String reType,String selectReType,String associatedType){
		log.info("进入getResourceService方法");
		log.info("参数reId="+reId+",reType="+reType+",selectReType="+selectReType+",associatedType="+associatedType);
//		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resourcewebservice/getResourceByAssociatedTypeAction?reId="+reId+"&reType="+reType+"&selectReType="+selectReType+"&associatedType="+associatedType;
//		String res = "";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> list = null;
		try {
			AssociatedType type = null;
			if("CHILD".equals(associatedType)){
				type = AssociatedType.CHILD;
			}else if("PARENT".equals(associatedType)){
				type = AssociatedType.PARENT;
			}else if("LINK".equals(associatedType)){
				type = AssociatedType.LINK;
			}
			
			List<Map<String, Object>> listObj = getResourceByAssociatedType(
					reId, reType,selectReType,type);
			//System.out.println(listObj);
			String res = gson.toJson(listObj);
//			res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
			if(res != null && !res.equals("")){
				list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}else{
				return null;
			}
			//list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			
		}catch(Exception e){
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getResourceService方法成功，实现了”获取资源“的功能");
		log.info("退出getResourceService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 根据资源Id和资源类型获取基础设施的信息
	 * @param resourceId
	 * @param resourceType
	 * @return Map<String,Map<String,String>>
	 */
	public Map<String,Map<String,String>> getBaseFacilityToMapService(String resourceId,String resourceType){
		log.info("进入getBaseFacilityToMapService方法");
		log.info("参数resourceId="+resourceId+",resourceType="+resourceType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resourcewebservice/getResourceByreIdAndreTypeAction?reId="+resourceId+"&reType="+resourceType;
		ApplicationModule module = ModuleProvider.getModule(resourceType);
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(resourceType, resourceId);
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		Map<String, Map<String, Object>> maps = new HashMap<String, Map<String,Object>>();
		if(sectionEntity !=null){
			Map<String, Object> map = new HashMap<String, Object>();
			map = module.toMap();
			Map<String, Object> sectionMap = sectionEntity.toMap();
			Map<String, Object> infoMapChineseMap = new HashMap<String, Object>();
			String infoName = resourceType;
			log.info("开始循环map");
			for(String key : map.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						entry = dictionary.getEntry(key + "," + infoName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							infoMapChineseMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
						}
					}
					
				} catch (EntryOperationException e) {
					log.error("获取数据字典失败");
					e.printStackTrace();
				}
			}
			log.info("结束循环map");
			Map<String,Object> sortedMap = quickSort.sortMap(infoMapChineseMap,orderIdMap);
			log.info("开始循环sectionMap");
			for(String k :sectionMap.keySet()){
				if(sectionMap.get(k) != null && sectionMap.get(k).getClass().toString().equals("class java.util.Date")){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
						String format = sdf.format(sectionMap.get(k));
						sectionMap.put(k, format);
				}
			}
			log.info("结束循环sectionMap");
			Map<String, Object> chineseTypeNameMap = new HashMap<String, Object>();
			try {
				List<BasicEntity> entry = null;
				entry = dictionary.getEntry(infoName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				chineseTypeNameMap.put("chineseTypeName", entry.get(0).getValue("display"));		
			} catch (EntryOperationException e) {
				log.error("获取数据字典失败");
				e.printStackTrace();
			}
			maps.put("entity", sectionMap);
			maps.put("dictionary", sortedMap);
			maps.put("chineseTypeNameMap", chineseTypeNameMap);
		}
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,Map<String,String>> baseFacilityMap = null;
		try {
			res = gson.toJson(maps);
//			res = com.iscreate.plat.tools.InterfaceUtil.httpPostClientReuqest(interfaceURL);
			baseFacilityMap = gson.fromJson(res,new TypeToken<Map<String,Map<String,String>>>(){}.getType());
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getBaseFacilityToMapService方法成功，实现了”根据资源Id和资源类型获取基础设施的信息“的功能");
		log.info("退出getBaseFacilityToMapService方法,返回Map<String,Map<String,String>>集合");
		return baseFacilityMap;
	}
	
	/**
	 * 根据资源Id和资源类型获取资源
	 * @param resourceId
	 * @param resourceType
	 * @return
	 */
	public Map<String,String> getResourceByReIdAndReTypeService(String resourceId,String resourceType){
		log.info("进入getResourceByReIdAndReTypeService方法");
		log.info("参数resourceId="+resourceId+",resourceType="+resourceType);
		Map<String, Map<String, String>> baseFacilityToMapService = this.getBaseFacilityToMapService(resourceId,resourceType);
		log.info("执行getResourceByReIdAndReTypeService方法成功，实现了”根据资源Id和资源类型获取资源“的功能");
		log.info("退出getResourceByReIdAndReTypeService方法,返回Map<String,String>集合");
		if(baseFacilityToMapService!=null && baseFacilityToMapService.containsKey("entity")){
			return baseFacilityToMapService.get("entity");
		}
		return null;
	}
	
	/**
	 * 获取基站列表
	 * @param orgId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getListBaseStationService(long orgId,String stationType){
		log.info("进入getListBaseStationService方法");
		log.info("参数orgId="+orgId+",stationType="+stationType);
		//Map<String,String> areaIdMap = new HashMap<String, String>();
		List<Map<String,String>> baseStationList = null;
		Set<Map<String,String>> baseStationSet = new HashSet<Map<String,String>>();
		List<Map<String,Object>> areaListByOrgId = this.sysAreaDao.getAreaListByOrgId(orgId+"");
		//areaIdMap = this.getAreaIdByOrgIdAndReType(orgId,stationType);
		String areaIds = "";
		if(areaListByOrgId!=null&&areaListByOrgId.size()>0){
			for (Map<String,Object> m : areaListByOrgId) {
				areaIds += m.get("areaId")+",";
			}
			areaIds = areaIds.substring(0,areaIds.length()-1);
		}
		
		if(!"".equals(areaIds)){
			
			List<Map<String, String>> baseStationByAreaIdAndReType = this.getResourceByAreaIdAndReType(areaIds,stationType);
			if(baseStationByAreaIdAndReType!=null && baseStationByAreaIdAndReType.size()>0){
				baseStationSet.addAll(baseStationByAreaIdAndReType);
			}
		}
		if(baseStationSet!=null && baseStationSet.size()>0){
			baseStationList = new ArrayList<Map<String,String>>();
			baseStationList.addAll(baseStationSet);
		}
		log.info("执行getListBaseStationService方法成功，实现了”获取基站列表“的功能");
		log.info("退出getListBaseStationService方法,返回List<Map<String,String>>集合");
		return baseStationList;
	}
	
	/**
	 * 根据拼音获取基站列表
	 * @param orgId
	 * @param stationType
	 * @param pinyin
	 * @return
	 */
	public List<Map<String,String>> getListPinyinBaseStationService(long orgId,String stationType,String pinyin){
		log.info("进入getListPinyinBaseStationService方法");
		log.info("参数orgId="+orgId+",stationType="+stationType+",pinyin="+pinyin);
		List<Map<String,String>> baseStationList = null;
		List<Map<String,String>> list = this.getListBaseStationService(orgId, stationType);
		if(pinyin!=null&&!"".equals(pinyin)&&!"null".equals(pinyin)&&!"all".equals(pinyin) && list!=null && list.size()>0){
			baseStationList = new ArrayList<Map<String,String>>();
			for(Map<String,String> map:list){
				String name = map.get("name");
				if(name != null && !"".equals(name)){
					String firstLetter = this.firstLetterService.getFirstLetter(name);
					if(firstLetter!=null&&!"null".equals(firstLetter)){
						if(firstLetter.equals(pinyin)){
							baseStationList.add(map);
						}
					}
				}
			}
		}else if("all".equals(pinyin)){
			baseStationList = list;
		}
		log.info("执行getListPinyinBaseStationService方法成功，实现了”根据拼音获取基站列表“的功能");
		log.info("退出getListPinyinBaseStationService方法,返回List<Map<String,String>>集合");
		return baseStationList;
	}
	
	/**
	 * 根据模糊查找基站列表
	 * @param orgId
	 * @param stationType
	 * @param fuzzy
	 * @return
	 */
	public List<Map<String,String>> getListFuzzyBaseStationService(long orgId,String stationType,String fuzzy){
		log.info("进入getListFuzzyBaseStationService方法");
		log.info("参数orgId="+orgId+",stationType="+stationType+",fuzzy="+fuzzy);
		List<Map<String,String>> baseStationList = null;
		List<Map<String,String>> list = this.getListBaseStationService(orgId, stationType);
		if(list!=null && list.size()>0){
			baseStationList = new ArrayList<Map<String,String>>();
			for (Map<String, String> map : list) {
				if(map.get("name").indexOf(fuzzy)!=-1){
					baseStationList.add(map);
				}
			}
		}
		log.info("执行getListFuzzyBaseStationService方法成功，实现了”根据模糊查找基站列表“的功能");
		log.info("退出getListFuzzyBaseStationService方法,返回List<Map<String,String>>集合");
		return baseStationList;
	}
	
	/**
	 * 根据资源Id和资源类型获取基础设施的信息
	 * @param resourceId
	 * @param resourceType
	 */
	public void getBaseFacilityService(String resourceId,String resourceType){
		log.info("进入getBaseFacilityService方法");
		log.info("参数resourceId="+resourceId+",resourceType="+resourceType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,Map<String,String>> baseFacilityMap = this.getBaseFacilityToMapService(resourceId, resourceType);
		try {
			if(baseFacilityMap!=null && baseFacilityMap.containsKey("entity")){
				baseFacilityMap.get("entity").remove("_entityId");
				baseFacilityMap.get("entity").remove("_entityType");
				Map<String,String> map = new HashMap<String, String>();
				map.put("size", baseFacilityMap.get("dictionary").size()+"");
				baseFacilityMap.put("size", map);
			}
			String result = gson.toJson(baseFacilityMap);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
		}
		log.info("执行getBaseFacilityService方法成功，实现了”根据资源Id和资源类型获取基础设施的信息“的功能");
		log.info("退出getBaseFacilityService方法,返回void");
	}
	
	/**
	 * 根据工单Id，资源Id，资源类型获取资源维护信息
	 * @param woId
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceMaintainRecordByWoIdService(String woId,String reId,String reType){
		log.info("进入getResourceMaintainRecordByWoIdService方法");
		log.info("参数woId="+woId+",reId="+reId+",reType="+reType);
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/maintain/loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAndRIdAndRtypeAction?bizModule=urgentrepair&bizProcessCode="+woId+"&resourceId="+reId+"&resourceType="+reType;
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> resourceMaintainRecordList = null;
		try {
			List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
			List<ResourceMaintenance> list = networkResourceMaintenanceService.getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype("urgentrepair", woId, reId, reType);
			if(list != null && list.size() > 0){
				log.info("开始循环list");
				for(ResourceMaintenance re:list){
					Map<String, Object> map = re.getApplicationEntity().toMap();
					for(String key:map.keySet()){
						if("null".equals(map.get(key))){
							map.put(key, " ");
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String format = sdf.format(map.get("op_time"));
					map.put("op_time", format);
					List<BasicEntity> entry;
					try {
						entry = dictionary.getEntry(re.getRes_type() + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							map.put("divtionaryReType", entry.get(0).getValue("display"));
							
						}
					} catch (EntryOperationException e) {
						log.error("获取数据字典失败 key"+re.getRes_type());
						map.put("divtionaryReType", re.getRes_type());
					}
					relist.add(map);
				}
				log.info("结束循环list");
			}
			res = gson.toJson(relist);
			resourceMaintainRecordList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
		}catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getResourceMaintainRecordByWoIdService方法成功，实现了”根据工单Id，资源Id，资源类型获取资源维护信息“的功能");
		log.info("退出getResourceMaintainRecordByWoIdService方法,返回List<Map<String,String>>集合");
		return resourceMaintainRecordList;
	}
	
	/**
	 * 根据资源Id，资源类型获取资源维护信息
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceMaintainRecordService(String reId,String reType){
		log.info("进入getResourceMaintainRecordService方法");
		log.info("参数reId="+reId+",reType="+reType);
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/maintain/loadNetworkResourceMaintenanceBybizModuleAndbizReIdAndReTypeAction?resourceId="+reId+"&resourceType="+reType;
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> resourceMaintainRecordList = new ArrayList<Map<String,String>>();
		try {
			List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
			Long valueOf = Long.valueOf(reId);
			List<ResourceMaintenance> list = networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResId(reType,valueOf);
			if(list != null && list.size() > 0){
				log.info("开始循环list");
				for(ResourceMaintenance re:list){
					Map<String, Object> map = re.getApplicationEntity().toMap();
					for(String key:map.keySet()){
						if("null".equals(map.get(key))){
							map.put(key, " ");
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String format = sdf.format(map.get("op_time"));
					map.put("op_time", format);
					List<BasicEntity> entry;
					try {
						entry = dictionary.getEntry(re.getRes_type() + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							map.put("divtionaryReType", entry.get(0).getValue("display"));
							
						}
					} catch (EntryOperationException e) {
						log.error("获取数据字典失败 key"+re.getRes_type());
						map.put("divtionaryReType", re.getRes_type());
					}
					relist.add(map);
				}
				log.info("结束循环list");
			}
			res = gson.toJson(relist);
			if(res != null && !"".equals(res)){
				resourceMaintainRecordList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			}
		}catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getResourceMaintainRecordService方法成功，实现了”根据资源Id，资源类型获取资源维护信息“的功能");
		log.info("退出getResourceMaintainRecordService方法,返回List<Map<String,String>>集合");
		return resourceMaintainRecordList;
	}
	
	/**
	 * 根据资源Id，资源类型获取处理过的资源维护信息（Map里的isBelong标记了是否属于该工单的资源）
	 * @param woId
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getHandleResourceMaintainRecordService(String woId,String reId,String reType){
		log.info("进入getHandleResourceMaintainRecordService方法");
		log.info("参数reId="+reId+",reType="+reType);
		List<Map<String,String>> resourceMaintainRecordList = new ArrayList<Map<String,String>>();
		//根据资源Id和资源类型获取资源维护记录
		List<Map<String, String>> resourceMaintainRecordService = this.getResourceMaintainRecordService(reId,reType);
		//根据工单Id和资源Id和资源类型获取资源维护记录
		List<Map<String, String>> resourceMaintainRecordByWoIdService = this.getResourceMaintainRecordByWoIdService(woId,reId,reType);
		Map<String,String> woIdMap = new HashMap<String, String>();
		Map<Integer,Map<String,String>> treeMap = new TreeMap<Integer, Map<String,String>>().descendingMap();
		if(resourceMaintainRecordByWoIdService!=null && resourceMaintainRecordByWoIdService.size()>0){
			for(Map<String,String> map :resourceMaintainRecordByWoIdService){
				woIdMap.put(map.get("id"), map.get("id"));
			}
		}
		if(resourceMaintainRecordService!=null && resourceMaintainRecordService.size()>0){
			for (Map<String, String> map : resourceMaintainRecordService) {
				String id = map.get("id");
				if(woIdMap.containsKey(id)){
					map.put("isBelong", "true");
				}else{
					map.put("isBelong", "false");
				}
				treeMap.put(Integer.parseInt(id), map);
			}
			for (Integer key : treeMap.keySet()) {
				resourceMaintainRecordList.add(treeMap.get(key));
			}
		}
		log.info("执行getHandleResourceMaintainRecordService方法成功，实现了”根据资源Id，资源类型获取处理过的资源维护信息“的功能");
		log.info("退出getHandleResourceMaintainRecordService方法,返回List<Map<String,String>>集合");
		return resourceMaintainRecordList;
	}
	
	/**
	 * 获取资源维护记录
	 */
	public void getResourceMaintainRecordAjaxService(String woId,String reId,String reType){
		log.info("进入getResourceMaintainRecordAjaxService方法");
		log.info("参数woId="+woId+",reId="+reId+",reType="+reType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			List<Map<String, String>> handleResourceMaintainRecordService = this.getHandleResourceMaintainRecordService(woId,reId,reType);
			if(handleResourceMaintainRecordService!=null && handleResourceMaintainRecordService.size()>0){
				for(Map<String,String> map :handleResourceMaintainRecordService){
					map.put("resourceType", map.get("divtionaryReType"));
					map.put("resourceId", map.get("res_id"));
					map.put("maintainOperation", map.get("op_cause"));
					String content = map.get("content");
					map.put("maintainContent", content);
					map.put("maintainer", map.get("user_name"));
	//				Date date = new Date(map.get("op_time")+"");
					map.put("maintainTime", map.get("op_time"));
					list.add(map);
				}
			}
 			String result = gson.toJson(list);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("json化后response.getWriter().write()出错！");
		}
		log.info("执行getResourceMaintainRecordAjaxService方法成功，实现了”获取资源维护记录“的功能");
		log.info("退出getResourceMaintainRecordAjaxService方法,返回void");
	}
	
	/**
	 * 根据基站Id和基站类型获取站址
	 */
	public void getStationByBaseStationIdAndBaseStationTypeService(String baseStationId,String stationType){
		log.info("进入getStationByBaseStationIdAndBaseStationTypeService方法");
		log.info("参数baseStationId="+baseStationId+",stationType="+stationType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			Map<String,String> map = getStationByBaseStationIdAndBaseStationType(baseStationId, stationType, "Station");
			String result = gson.toJson(map);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("json化后response.getWriter().write()出错！");
		}
		log.info("执行getStationByBaseStationIdAndBaseStationTypeService方法成功，实现了”根据基站Id和基站类型获取站址“的功能");
		log.info("退出getStationByBaseStationIdAndBaseStationTypeService方法,返回void");
	}
	
	/**
	 * 根据站址Id和类型获取所有的父级的区域
	 * @param stationId
	 * @param stationType
	 */
	public void getAllAreaByBaseStationIdService(String stationId,String stationType){
		log.info("进入getAllAreaByBaseStationIdService方法");
		log.info("参数stationId="+stationId+",stationType="+stationType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			Map<String, String> map = this.getAreaByStationId(stationId, stationType);
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			if(map!=null && map.containsKey("id")){
				list.add(map);
				list.addAll(getAllArea(map.get("id")));
			}
			String result = gson.toJson(list);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("json化后response.getWriter().write()出错！");
		}
		log.info("执行getAllAreaByBaseStationIdService方法成功，实现了”根据站址Id和类型获取所有的父级的区域“的功能");
		log.info("退出getAllAreaByBaseStationIdService方法,返回void");
	}
	
	/**
	 * 根据站址Id和类型获取所有的父级的区域
	 * @param stationId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getAllAreaByStationIdAndStationTypeService(String stationId,String stationType){
		log.info("进入getAllAreaByStationIdAndStationTypeService方法");
		log.info("参数stationId="+stationId+",stationType="+stationType);
		Map<String, String> map = this.getAreaByStationId(stationId, stationType);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(map!=null && map.containsKey("id")){
			list.add(map);
			list.addAll(getAllArea(map.get("id")));
		}
		log.info("执行getAllAreaByStationIdAndStationTypeService方法成功，实现了”根据站址Id和类型获取所有的父级的区域“的功能");
		log.info("退出getAllAreaByStationIdAndStationTypeService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 根据工单ID获取基础设置信息
	 * @param woId
	 */
	public void getBaseFacilityByWoIdService(String woId){
		log.info("进入getBaseFacilityByWoIdService方法");
		log.info("参数woId="+woId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<Workorderassnetresource> list = this.workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
			String result = gson.toJson(list);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.info("json化后response.getWriter().write()出错！");
		}
		log.info("执行getBaseFacilityByWoIdService方法成功，实现了”根据工单ID获取基础设置信息“的功能");
		log.info("退出getBaseFacilityByWoIdService方法,返回void");
	}
	
	/**
	 * 根据人员获取其所属的区域信息
	 * @param account
	 * @return
	 */
	public List<Map<String,String>> getAreaByAccountService(String account){
		log.info("进入getAreaByAccountService方法");
		log.info("参数account="+account);
		List<Map<String,String>> list = null;
		Set<String> areaIdList = null;
		//根据账号获取组织Id
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(account);
//		Account accountByAccountId = this.providerOrganizationService.getAccountByAccountId(account);
		if(sysOrgUserByAccount==null){
			return list;
		}
		List<SysOrg> orgByAccountService = this.sysOrganizationService.getOrgByAccountService(account);
//		List<OrgAccountRelation> osrList = this.providerOrganizationDao.getOrgAccountRelationByAccount(account);
		if(orgByAccountService!=null && orgByAccountService.size()>0){
			areaIdList = new HashSet<String>();
			for (SysOrg osr : orgByAccountService) {
				long orgId = osr.getOrgId();
				//根据组织Id获取区域Id
				Map<String, String> areaIdByOrgIdAndReType = this.getAreaIdByOrgIdAndReType(orgId,null);
				if(areaIdByOrgIdAndReType!=null){
					for (String areaId : areaIdByOrgIdAndReType.keySet()) {
						areaIdList.add(areaId);
					}
				}/*
				List<Org_Area_Resource> oarList = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(orgId+"");
				if(oarList!=null && oarList.size()>0){
					for (Org_Area_Resource oar : oarList) {
						areaIdList.add(oar.getAreaId());
					}
				}*/
			}
		}
		
		//根据区域Id获取区域信息
		if(areaIdList!=null && areaIdList.size()>0){
			list = new ArrayList<Map<String,String>>();
			for (String areaId : areaIdList) {
				Map<String, Map<String, String>> baseFacilityToMapService = this.getBaseFacilityToMapService(areaId, "Sys_Area");
				if(baseFacilityToMapService!=null && baseFacilityToMapService.containsKey("entity")){
					list.add(baseFacilityToMapService.get("entity"));
				}
			}
		}
		log.info("执行getAreaByAccountService方法成功，实现了”根据人员获取其所属的区域信息“的功能");
		log.info("退出getAreaByAccountService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 根据组织Id获取站址
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> getStationByOrgIdService(long orgId){
		log.info("进入getStationByOrgIdService方法");
		log.info("参数orgId="+orgId);
		Set<String> areaIdList = null;
		//根据组织Id获取区域Id
		Map<String, String> areaIdByOrgIdAndReType = this.getAreaIdByOrgIdAndReType(orgId, "Station");
		if(areaIdByOrgIdAndReType!=null){
			areaIdList = new HashSet<String>();
			for (String areaId : areaIdByOrgIdAndReType.keySet()) {
				areaIdList.add(areaId);
			}
		}
		/*
		List<Org_Area_Resource> oarList = this.providerOrganizationDao.getOrg_Area_ResourceByOrgIdAndOrgType(orgId+"", OrganizationConstant.PROVIDE_ORGANIZATION,"Station");
		if(oarList!=null && oarList.size()>0){
			areaIdList = new HashSet<String>();
			for (Org_Area_Resource oar : oarList) {
				areaIdList.add(oar.getAreaId());
			}
		}*/
		
		//根据区域获取站址
		Set<Map<String,String>> set = null;
		if(areaIdList!=null && areaIdList.size()>0){
			set = new HashSet<Map<String,String>>();
			for (String areaId : areaIdList) {
				List<Map<String, String>> resourceService = this.getResourceService(areaId, "Sys_Area", "Station", "CHILD");
				set.addAll(resourceService);
			}
		}
		
		//把set转成list
		List<Map<String,String>> list = null;
		if(set!=null && set.size()>0){
			list = new ArrayList<Map<String,String>>();
			list.addAll(set);
		}
		log.info("执行getStationByOrgIdService方法成功，实现了”根据组织Id获取站址“的功能");
		log.info("退出getStationByOrgIdService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 根据站址Id和基站类型获取基站信息
	 * @param stationId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getStationByStationIdAndBaseStationTypeService(String stationId,String baseStationType){
		log.info("进入getStationByStationIdAndBaseStationTypeService方法");
		log.info("参数stationId="+stationId+",baseStationType="+baseStationType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/getBaseStationByStationAction?reId="+stationId+"&selectReType="+baseStationType;
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> stationList = null;
		try {
			List<Map<String, Object>> list = structureCommonService.getBaseStationByStationByHibernate(stationId, baseStationType);
			res = gson.toJson(list);
			stationList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
			e.printStackTrace();
		}
		log.info("执行getStationByStationIdAndBaseStationTypeService方法成功，实现了”根据站址Id和基站类型获取基站信息“的功能");
		log.info("退出getStationByStationIdAndBaseStationTypeService方法,返回List<Map<String,String>>集合");
		return stationList;
	}
	
	/**
	 * 获取顶级区域
	 * @return
	 */
	public List<Map<String,String>> getTopAreaService(){
		log.info("进入getTopAreaService方法");
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/getParentAreaAction";
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> areaList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("level", "省");
			List<Map<String, Object>> listByMap = new ArrayList<Map<String,Object>>();
			List<BasicEntity> entityListByEntity = this.staffOrganizationService.getTopLevelAreaList();
			if(entityListByEntity != null){
				for(BasicEntity ap:entityListByEntity){
					listByMap.add(ap.toMap());
				}
			}
			res = gson.toJson(listByMap);
			areaList = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getTopAreaService方法成功，实现了”获取顶级区域“的功能");
		log.info("退出getTopAreaService方法,返回List<Map<String,String>>集合");
		return areaList;
	}
	
	/**
	 * 根据区域Id获取站址数量
	 * @param areaIds
	 * @return
	 */
	public String getStationCountByAreaService(String areaIds){
		log.info("进入getStationCountByAreaService方法");
		log.info("参数areaIds="+areaIds);
		//获取环境代码
//		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/getStationCountByAreaAction?areaId="+areaIds;
		String res = "";
		try {
			int count = 0;
			if(areaIds != null && !areaIds.equals("")){
				String[] split = areaIds.split(",");
				List<String> areaIdList = new ArrayList<String>();
				if(split != null && split.length > 0){
					for(String s : split){
						areaIdList.add(s);
					}
				}
				count = this.structureCommonService.getStationCountByArea(areaIdList);
			}
			//res = com.iscreate.plat.tools.InterfaceUtil.httpPostClientReuqest(interfaceURL);
			res = count+"";
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getStationCountByAreaService方法成功，实现了”获取顶级区域“的功能");
		log.info("退出getStationCountByAreaService方法,返回String为："+res);
		return res;
	}
	
	/**
	 * 根据区域Id获取基站数量
	 * @param areaIds
	 * @return
	 */
	public String getBaseStationCountByAreaService(String areaIds){
		log.info("进入getBaseStationCountByAreaService方法");
		log.info("参数areaIds="+areaIds);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/getBaseStationCountByAreaAction?areaId="+areaIds;
		String res = "";
		try {
			int count = 0;
			if(areaIds != null && !areaIds.equals("")){
				String[] split = areaIds.split(",");
				List<String> areaIdList = new ArrayList<String>();
				if(split != null && split.length > 0){
					for(String s : split){
						areaIdList.add(s);
					}
				}
				count = this.structureCommonService.getBaseStationCountByArea(areaIdList);
			}
			//res = com.iscreate.plat.tools.InterfaceUtil.httpPostClientReuqest(interfaceURL);
			res = count+"";
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getBaseStationCountByAreaService方法成功，实现了”根据区域Id获取基站数量“的功能");
		log.info("退出getBaseStationCountByAreaService方法,返回String为："+res);
		return res;
	}
	
	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)分页
	 * @param AetgName 资源组类型(可以不指定)
	 * @param innerDistance 内半径
	 * @param outerDistance 外半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param start 开始下标
	 * @param end 结束下标
	 * @param myStructure structure名字
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getEntityListRingByAetgAndGPSDistanceAndPagingService(String AetgName,double innerDistance,double outerDistance,double longitude, double latitude,Map<String,Object> map,int start,int end, String myStructure){
		log.info("进入getEntityListRingByAetgAndGPSDistanceAndPagingService方法");
		log.info("参数AetgName="+AetgName+",innerDistance="+innerDistance+",outerDistance="+outerDistance+",longitude="+longitude+",latitude="+latitude+",map="+map+",start="+start+",end="+end+",myStructure="+myStructure);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		code = "http://192.168.206.229:8080/networkresourcemanage/";
//		String interfaceURL = code + "resource/getEntityListRingByAetgAndGPSDistanceAndPagingAction?AetgName="+AetgName+"&innerDistance="+innerDistance+"&outerDistance="+outerDistance+"&longitude="+longitude+"&latitude="+latitude+"&start="+start+"&end="+end;
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> list = null;
		try {
			//yuan.yw add 对用户区域进行筛选
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId =(String)session.getAttribute("userId");
			List<BasicEntity> userAreaIds = this.staffOrganizationService.getAreaByUserId(userId);
			List<String> areaIdsList = null;
			if(userAreaIds!=null && userAreaIds.size()>0){
				areaIdsList = new ArrayList<String>();
				for(BasicEntity be:userAreaIds){
					areaIdsList.add(be.getValue("id")+"");
					ApplicationEntity[] apList = this.structureCommonService.getAppArrsByRecursionForSrcSameType(ApplicationEntity.changeFromEntity(be), "Sys_Area", "networkresourcemanage");
					if(apList!=null && apList.length>0){
						for(ApplicationEntity ap:apList){
							areaIdsList.add(ap.getValue("id")+"");
						}
					}
				}
			}
			String[] splitcondition = null;
			
			log.info("进入===getEntityListRingByAetgAndGPSDistanceAndPagingAction方法");
			List<Map<String,Object>> entityListRingByAetgAndGPSDistanceAndPaging = this.structureCommonService.getEntityListRingByAetgAndGPSDistanceAndPaging(AetgName, innerDistance, outerDistance, longitude, latitude, null, start, end,areaIdsList, "networkresourcemanage");
			
			res = gson.toJson(entityListRingByAetgAndGPSDistanceAndPaging);
			list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			List<Map<String,String>> list1 = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			list = list1;
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getEntityListRingByAetgAndGPSDistanceAndPagingService方法成功，实现了”根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)分页“的功能");
		log.info("退出getEntityListRingByAetgAndGPSDistanceAndPagingService方法,返回List<Map<String, String>>");
		return list;
	}
	
	/**
	 * 根据区域Id和资源类型获取资源（报表）
	 * @param areaIds
	 * @param reTypes
	 * @return
	 */
	public Map<String,String> getReportResourceListService(String areaIds,String reTypes){
		log.info("进入getReportResourceListService方法");
		log.info("参数areaIds="+areaIds+",reTypes="+reTypes);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "resource/getReportResourceListAction?areaId="+areaIds+"&reTypes="+reTypes;
		String res = "";
		Map<String,String> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			List<String> a = new ArrayList<String>();
			Map<String, String> reportResourceList = new HashMap<String,String>();
			if(areaIds != null && !areaIds.equals("")){
				ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity("Sys_Area", areaIds);
				ApplicationEntity[] appArrsByRecursionForSrcSameType = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity, "Sys_Area", "networkresourcemanage");
				if(appArrsByRecursionForSrcSameType != null && appArrsByRecursionForSrcSameType.length > 0){
					log.info("开始循环appArrsByRecursionForSrcSameType");
					for(ApplicationEntity ae:appArrsByRecursionForSrcSameType){
						String aeid = ae.getValue("id")+"";
						a.add(aeid);
					}
					log.info("结束循环appArrsByRecursionForSrcSameType");
				}
					a.add(areaIds);
			}
			if(reTypes != null && !reTypes.equals("")){
				String[] split = reTypes.split(",");
				log.info("开始循环split");
				for(String s:split){
					if(s.equals("Station")){
						int count =  this.structureCommonService.getStationCountByArea(a);
						//System.out.println(count);
						reportResourceList.put(s, count+"");
					}else if(s.equals("Cell")){
						int cellCountByArea = this.structureCommonService.getCellCountByArea(a);
						reportResourceList.put(s, cellCountByArea+"");
					}else{
						List<Map<String,Object>> baseStationByAreasByHibernate = this.structureCommonService.getBaseStationByAreasByHibernate(a, s);
						int count = 0;
						if(baseStationByAreasByHibernate != null && !baseStationByAreasByHibernate.isEmpty()){
							count = baseStationByAreasByHibernate.size();
						}
						reportResourceList.put(s, count+"");
					}
					
				}
				log.info("结束循环split");
			}
			res = gson.toJson(reportResourceList);
			map = gson.fromJson(res,new TypeToken<Map<String,String>>(){}.getType());
		}catch(Exception e){
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getReportResourceListService方法成功，实现了”根据区域Id和资源类型获取资源（报表）“的功能");
		log.info("退出getReportResourceListService方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 根据资源名和资源类型获取资源信息
	 * @param baseStationName
	 * @return
	 */
	public Map<String,String> getResourceByReNameAndReTypeService(String reName,String reType){
		log.info("进入getResourceByReNameAndReTypeService方法");
		log.info("参数reName="+reName+",reType="+reType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		String interfaceURL = code + "getBaseStationByNameAction?baseStationName="+reName+"&baseStationType="+reType;
		String res = "";
		Map<String,String> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			Map<String,Object> vMap = new HashMap<String, Object>();
			vMap.put("name", reName);
			List<List<ApplicationEntity>> entityListByAetg = this.structureCommonService.getEntityListByEntityTypeRoAetg(reType,vMap, "networkresourcemanage");
			Map<String,Object> baseStationByName = new HashMap<String, Object>();
			if(entityListByAetg != null && !entityListByAetg.isEmpty()){
				List<ApplicationEntity> list = entityListByAetg.get(0);
				if(list != null && !list.isEmpty()){
					ApplicationEntity applicationEntity = list.get(0);
					if(applicationEntity != null){
						baseStationByName = applicationEntity.toMap();
					}
				}
			}
			res = gson.toJson(baseStationByName);
			map = gson.fromJson(res,new TypeToken<Map<String,String>>(){}.getType());
		}catch(Exception e){
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getResourceByReNameAndReTypeService方法成功，实现了”根据资源名和资源类型获取资源信息“的功能");
		log.info("退出getResourceByReNameAndReTypeService方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 根据条件获取其向下的资源(包括本身资源)
	 * @param reId
	 * @param reType
	 * @param selectReType
	 * @return
	 */
	public List<Map<String,String>> getDownwardOnSelfResourceService(String reId,String reType,String selectReType){
		log.info("进入getDownwardOnSelfResourceService方法");
		log.info("参数reId="+reId+",reType="+reType+",selectReType="+selectReType);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> resourceByReIdAndReTypeService = this.getResourceByReIdAndReTypeService(reId, reType);
		if(resourceByReIdAndReTypeService!=null && resourceByReIdAndReTypeService.containsKey("id")){
			list.add(resourceByReIdAndReTypeService);
			List<Map<String, String>> downwardResourceService = this.getDownwardResourceService(reId,reType,selectReType);
			if(downwardResourceService!=null && downwardResourceService.size()>0){
				list.addAll(downwardResourceService);
			}
		}
		log.info("执行getDownwardOnSelfResourceService方法成功，实现了”根据条件获取其向下的资源(包括本身资源)“的功能");
		log.info("退出getDownwardOnSelfResourceService方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 根据条件获取其向下的资源(不包括本身资源)
	 * @param reId(当前资源Id)
	 * @param reType(当前资源类型)
	 * @param selectReType(需要获取的资源)
	 * @return
	 */
	public List<Map<String,String>> getDownwardResourceService(String reId,String reType,String selectReType){
		log.info("进入getDownwardResourceService方法");
		log.info("参数reId="+reId+",reType="+reType+",selectReType="+selectReType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		code = "http://192.168.206.229:8080/networkresourcemanage/";
//		String interfaceURL = code + "resource/getAppArrsByRecursionForSrcAction?reId="+reId+"&reType="+reType+"&selectReType="+selectReType;
		String res = "";
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,String>> list = null;
		try {
			ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity(reType, reId);
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			if(reType.equals(selectReType)){
				ApplicationEntity[] appArrsByRecursionForSrcSameType = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity,selectReType,"networkresourcemanage");
				if(appArrsByRecursionForSrcSameType != null){
					log.info("开始循环appArrsByRecursionForSrcSameType");
					for(ApplicationEntity app:appArrsByRecursionForSrcSameType){
						if(app != null){
							Map<String, Object> map = app.toMap();
							listMap.add(map);
						}
					}
					log.info("结束循环appArrsByRecursionForSrcSameType");
				}
			}else{
				ApplicationEntity[] appArrsByRecursionForSrc = this.structureCommonService.getAppArrsByRecursionForSrc(sectionEntity, selectReType, "networkresourcemanage");
				if(appArrsByRecursionForSrc != null){
					log.info("开始循环appArrsByRecursionForSrc");
					for(ApplicationEntity app:appArrsByRecursionForSrc){
						if(app != null){
							Map<String, Object> map = app.toMap();
							listMap.add(map);
						}
					}
					log.info("结束循环appArrsByRecursionForSrc");
				}
			}
			res = gson.toJson(listMap);
			list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			List<Map<String,String>> list1 = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			list = list1;
		} catch (Exception e) {
			log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
		}
		log.info("执行getDownwardResourceService方法成功，实现了”根据条件获取其向下的资源(不包括本身资源)“的功能");
		log.info("退出getDownwardResourceService方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 根据专业获取设备
	 * @param profession
	 * @return
	 */
	public Map<String,List<Map<String,String>>> getEquipmentByProfessionAndReIdAndReTypeService(String profession,String reId,String reType){
		log.info("进入getEquipmentByProfessionAndReIdAndReTypeService方法");
		log.info("参数profession="+profession+",reId="+reId+",reType="+reType);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
		//profession = "Power,Wireless,Transmission";
		//code = "http://192.168.206.233:8080/networkresourcemanage/";
//		String interfaceURL = code + "resourcewebservice/getResourceEntitysByAetgsAction?reType="+reType+"&reId="+reId+"&aetgs="+profession;
		String res = "";
		Map<String,List<Map<String,String>>> map = null;
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			if(profession!=null && !"".equals(profession)){
				Map<String,Object> resultMap = new HashMap<String,Object>();
				ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity(reType, reId);
				String[] split = profession.split(",");
				if(split != null && split.length > 0){
					for(String s : split){
						List<Map<String,Object>> rList = new ArrayList<Map<String,Object>>();
						String[] aetNames=null;
						String chineseName="";
						
						aetNames=structureCommonService.getAetNameOfAetg(s,"networkresourcemanage");
						if(aetNames!=null&&!"".equals(aetNames)){
							for(String aetName:aetNames){
								List<BasicEntity> entry1 = null;
								try {
									entry1 = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
									if(entry1 != null && !entry1.isEmpty()) {
										chineseName = entry1.get(0).getValue("display").toString();
									}
								} catch (EntryOperationException e) {
									log.error("获取"+aetName+"中文字典失败，可能该字典不存在");
									e.printStackTrace();
									chineseName = aetName;
								}
								ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity, aetName, "networkresourcemanage");
								if(apps!=null){
									for(ApplicationEntity ap:apps){
										Map<String,Object> rMap = ap.toMap();
										rMap.put("chineseTypeName", chineseName);
										rList.add(rMap);
									}
								}
							}
						}
						if(rList!=null && !rList.isEmpty()){
							quickSort.sort(rList, "name");
						}
						resultMap.put(s, rList);
					}
				}
			res = gson.toJson(resultMap);
			}
			map = gson.fromJson(res,new TypeToken<Map<String,List<Map<String,String>>>>(){}.getType());
		}catch(Exception e){
			log.error(this.className+"类中getEquipmentByProfessionAndReIdAndReTypeService方法，获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
			return map;
		}
		log.info("执行getEquipmentByProfessionAndReIdAndReTypeService方法成功，实现了”根据专业获取设备“的功能");
		log.info("退出getEquipmentByProfessionAndReIdAndReTypeService方法,返回List<Map<String,String>>");
		return map;
	}
	
	/**
	 * 根据组织Id获取巡检机房(不是直接挂到一个维护队)
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> getRoutineInspectionRoomByOrgIdService(long orgId){
		log.info("进入getRoutineInspectionRoomByOrgIdService方法");
		log.info("参数orgId="+orgId);
		//获取传入的orgId以下的组织
		Map<String,Long> orgIdMap = new HashMap<String, Long>();
		Map<String,Map<String,Object>> orgObjMap = new HashMap<String, Map<String,Object>>();
		//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(orgId);
		//yuan.yw
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getOrgListMapDownwardByOrg(orgId);
		if(orgListDownwardByOrg!=null && orgListDownwardByOrg.size()>0){
			for (Map<String,Object> po : orgListDownwardByOrg) {
				orgIdMap.put(po.get("orgId")+"", Long.valueOf(po.get("orgId")+""));
				orgObjMap.put(po.get("orgId")+"", po);
			}
		}
		Map<String,String> totalMap = new HashMap<String, String>();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> areaIdMap = new HashMap<String, String>();
//		Map<String,Map<String,Map<String,String>>> areaOrgMap = new HashMap<String,Map<String,Map<String,String>>>();
		if(orgIdMap!=null && orgIdMap.size()>0){
			List<String> downwardOrgIdList = new ArrayList<String>();
			for (String key : orgIdMap.keySet()) {
				downwardOrgIdList.add(key);
			}
			//根据组织Id获取区域
			List<String> areaIdDao = this.informationManageNetworkResourceDao.findAreaIdByOrgIdResourceTypeWithStatus(downwardOrgIdList, "Station", 1);
			String areaIds = "";
			List<String> areaIdList = new ArrayList<String>();
			if(areaIdDao!=null && areaIdDao.size()>0){
				for (String areaId : areaIdDao) {
					if(!areaIdMap.containsKey(areaId)){
						areaIdMap.put(areaId, areaId);
						areaIdList.add(areaId);
						areaIds += areaId + ",";
					}
				}
				if(!"".equals(areaIds)){
					areaIds = areaIds.substring(0, areaIds.length()-1);
				}
			}
			
			Map<String,Map<String,String>> areaOrgMap = new HashMap<String, Map<String,String>>();
			List<Map<String, String>> areaOrgList = this.informationManageNetworkResourceDao.findOrgIdByAreaIdResourceTypeWithStatus(areaIdList, "Station", 1);
			if(areaOrgList!=null && !areaOrgList.isEmpty()){
				for (Map<String, String> map : areaOrgList) {
					String areaId = map.get("areaId");
					String orgIdStr = map.get("orgId");
					if(orgObjMap.containsKey(orgIdStr)){
						if(areaOrgMap.containsKey(areaId)){
							areaOrgMap.get(areaId).put(orgIdStr, orgObjMap.get(orgIdStr).get("name")+"");
						}else{
							Map<String,String> orgStrMap = new HashMap<String, String>();
							orgStrMap.put(orgIdStr,orgObjMap.get(orgIdStr).get("name")+"");
							areaOrgMap.put(areaId, orgStrMap);
						}
					}
				}
			}
			
			
				//根据区域获取机房
				if(areaIds!=null && !"".equals(areaIds)){
					List<Map<String,String>> roomList = this.getRoomByAreaIdsService(areaIds);
					if(roomList!=null && roomList.size()>0){
						for (Map<String,String> map : roomList) {
							String areaId = map.get("areaId");
							String id = map.get("id");
							if(totalMap.containsKey(id)){
								continue;
							}
							totalMap.put(id,id);
							if(areaId!=null && !"".equals(areaId)){
								Map<String,String> map2 = areaOrgMap.get(areaId);
								if(map2!=null && map2.size()>0){
//									boolean isHas = false;
									for (String key : map2.keySet()) {
										if(!OrganizationConstant.ORGANIZATION_MAINTENANCETEAM.equals(orgObjMap.get(key).get("type")+"")){
											map.put("orgId", key);
											map.put("orgName", map2.get(key));
//											isHas = true;
										}
										if(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM.equals(orgObjMap.get(orgId+"").get("type")+"")){
											map.put("orgId", orgId+"");
											map.put("orgName", map2.get(orgId+""));
										}
									}
									list.add(map);
								}
							}
						}
					}
				}
//			}
		}
		log.info("执行getRoutineInspectionRoomByOrgIdService方法成功，实现了”根据组织Id获取巡检机房“的功能");
		log.info("退出getRoutineInspectionRoomByOrgIdService方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 根据组织Id获取巡检机房(直接挂到一个维护队)
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getRoutineInspectionRoomByOrgIdToMaintenanceTeamService(long orgId){
		log.info("进入getRoutineInspectionRoomByOrgIdToMaintenanceTeamService方法");
		log.info("参数orgId="+orgId);
		//获取传入的orgId以下的组织
		Map<String,Long> orgIdMap = new HashMap<String, Long>();
		Map<String,Object> orgObjMap = new HashMap<String, Object>();
		//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(orgId);
		
		List<Map<String,Object>> orgList = sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(orgId+"");
		
		if(orgList!=null && orgList.size()>0){
			for (Map<String,Object> po : orgList) {
				orgIdMap.put( po.get("orgId")+"", Long.parseLong(po.get("orgId")+""));
			    orgObjMap.put(po.get("orgId")+"",  po);
			}
		}
		Map<String,String> totalMap = new HashMap<String, String>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,String> areaIdMap = new HashMap<String, String>();
//		Map<String,Map<String,Map<String,String>>> areaOrgMap = new HashMap<String,Map<String,Map<String,String>>>();
		if(orgIdMap!=null && orgIdMap.size()>0){
			List<String> downwardOrgIdList = new ArrayList<String>();
			for (String key : orgIdMap.keySet()) {
				downwardOrgIdList.add(key);
			}
			//根据组织Id获取区域
			List<String> areaIdDao = this.informationManageNetworkResourceDao.findAreaIdByOrgIdResourceTypeWithStatus(downwardOrgIdList, "Station", 1);
			
			String areaIds = "";
			List<String> areaIdList = new ArrayList<String>();
			if(areaIdDao!=null && areaIdDao.size()>0){
				for (String areaId : areaIdDao) {
					if(!areaIdMap.containsKey(areaId)){
						areaIdMap.put(areaId, areaId);
						areaIdList.add(areaId);
						areaIds += areaId + ",";
					}
				}
				if(!"".equals(areaIds)){
					areaIds = areaIds.substring(0, areaIds.length()-1);
				}
			}
			
			Map<String,Map<String,String>> areaOrgMap = new HashMap<String, Map<String,String>>();
			List<Map<String, String>> areaOrgList = this.informationManageNetworkResourceDao.findOrgIdByAreaIdResourceTypeWithStatus(areaIdList, "Station", 1);
			if(areaOrgList!=null && !areaOrgList.isEmpty()){
				for (Map<String, String> map : areaOrgList) {
					String areaId = map.get("areaId");
					String orgIdStr = map.get("orgId");
					if(orgObjMap.containsKey(orgIdStr)){
						if(areaOrgMap.containsKey(areaId)){
							Map<String,Object> orgMap = (Map<String,Object>)orgObjMap.get(orgIdStr);
							areaOrgMap.get(areaId).put(orgIdStr, (String) orgMap.get("name"));
						}else{
							Map<String,String> orgStrMap = new HashMap<String, String>();
							Map<String,Object> orgMap = (Map<String,Object>)orgObjMap.get(orgIdStr);
							orgStrMap.put(orgIdStr,(String)orgMap.get("name"));
							areaOrgMap.put(areaId, orgStrMap);
						}
					}
				}
			}
			/*
			List<String> orgIdList = this.informationManageNetworkResourceDao.findOrgIdByAreaIdResourceTypeWithStatus(areaIdList, "Station", 1);
			if(areaIdMap!=null && areaIdMap.size()>0){
				for (String areaId : areaIdDao) {
					
				}
			}
			
			
			for (String key1 : orgIdMap.keySet()) {
				if(areaIdMap!=null && areaIdMap.size()>0){
					for (String areaId : areaIdDao) {
//						List<String> orgIdList = this.informationManageNetworkResourceDao.findOrgIdByAreaIdResourceTypeWithStatus(areaId, "Station", 1);
						if(orgIdList!=null && orgIdList.size()>0){
							Map<String,Map<String,String>> orgMap = new HashMap<String, Map<String,String>>();
							for (String areaOrgId : orgIdList) {
								long subOrgId = 0;
								try {
									subOrgId = Long.parseLong(areaOrgId);
								} catch (Exception e) {
									log.error(this.className+"类里getRoutineInspectionRoomByOrgIdService方法中组织Id为："+areaOrgId+" 转换成long类型出错");
									continue;
								}
								if(!orgIdMap.containsKey(areaOrgId)){
									continue;
								}
								ProviderOrganization po = this.providerOrganizationService.getOrgByOrgIdService(subOrgId);
								if(po!=null){
									//获取属于维护队的组织
									if(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM.equals(po.getType())){
										Map<String,String> upMap = new HashMap<String, String>();
										List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(po.getId());
										if(upProviderOrgByOrgIdService!=null && upProviderOrgByOrgIdService.size()>0){
											upMap.put(upProviderOrgByOrgIdService.get(0).getId()+"", upProviderOrgByOrgIdService.get(0).getName());
											orgMap.put(po.getId()+"", upMap);
										}else{
											upMap.put(po.getId()+"", po.getName());
											orgMap.put(po.getId()+"", upMap);
										}
									}
								}
							}
							areaOrgMap.put(areaId, orgMap);
						}
					}
				}
				*/
			
				//根据区域获取机房
				if(areaIds!=null && !"".equals(areaIds)){
					//List<Map<String,String>> roomList = this.getRoomByAreaIdsService(areaIds);
					
					List<Map<String,Object>> roomList = networkResourceQueryDao.getRoomListByAreaIds(areaIds);
					if(roomList!=null && roomList.size()>0){
						for (Map<String,Object> map : roomList) {
							String areaId = map.get("areaId").toString();
							String id = map.get("id").toString();
							if(totalMap.containsKey(id)){
								continue;
							}
							totalMap.put(id,id);
							if(areaId!=null && !"".equals(areaId)){
								Map<String,String> map2 = areaOrgMap.get(areaId);
								if(map2!=null && map2.size()>0){
									for (String key : map2.keySet()) {
										Map<String,Object> orgMap = (Map<String, Object>) orgObjMap.get(key);
										if(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM.equals(orgMap.get("orgAttrbuteType"))){
											if(!map.containsKey(key)){
												map.put("orgId", key);
												map.put("orgName", map2.get(key));
											}
										}
									}
									list.add(map);
								}
							}
						}
					}
				}
//			}
		}
		log.info("执行getRoutineInspectionRoomByOrgIdToMaintenanceTeamService方法成功，实现了”根据组织Id获取巡检机房“的功能");
		log.info("退出getRoutineInspectionRoomByOrgIdToMaintenanceTeamService方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 根据区域获取机房
	 * @param areaIds
	 * @return
	 */
	public List<Map<String,String>> getRoomByAreaIdsService(String areaIds){
		log.info("进入getRoomByAreaIdsService方法");
		log.info("参数areaIds="+areaIds);
		//获取环境代码
		String code = this.getNetworkResourceUrl();
//		profession = "Power,Wireless,Transmission";
//		code = "http://192.168.206.233:8080/networkresourcemanage/";
		String interfaceURL = code + "getResByAreasAction?areaId="+areaIds+"&selectReType=Room";
		String res = "";
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
			list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
		}catch(Exception e){
			log.error(this.className+"类中getRoomByAreaIdsService方法，获取网络资源外部接口失败! 网络资源的path为："+interfaceURL);
			return list;
		}
		log.info("执行getRoomByAreaIdsService方法成功，实现了”根据区域获取机房“的功能");
		log.info("退出getRoomByAreaIdsService方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 获取外部链接
	 */
	public void getNetworkResourceUrlService(){
		log.info("进入getNetworkResourceUrlService方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String url = this.getNetworkResourceUrl();
		try {
			String result = gson.toJson(url);
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("json化后response.getWriter().write()出错！");
		}
		log.info("执行getNetworkResourceUrlService方法成功，实现了”获取外部链接“的功能");
		log.info("退出getNetworkResourceUrlService方法,返回void");
	}
	
	/**
	 * 根据环境代码获取对应的网络资源外部链接
	 * @return
	 */
	public String getNetworkResourceUrl(){
		log.info("进入getNetworkResourceUrl方法");
//		System.out.println(getProjectPrefixURL());
		String code = "";
		OutLinkingService outLinking = new OutLinkingService();
		code = outLinking.getUrlByProjService(ProjectConstant.OPS);
//		code = getProjectPrefixURL()+ProjectConstant.NETWORKRESOURCEMANAGE+"/";
//		code = "http://testag1.iosm.cn/"+ProjectConstant.NETWORKRESOURCEMANAGE+"/";
		log.info("执行getNetworkResourceUrl方法成功，实现了”根据环境代码获取对应的网络资源外部链接“的功能");
		log.info("退出getNetworkResourceUrl方法,返回String为："+code);
		return code;
	}
	
	private String getProjectPrefixURL() {
		log.info("进入getProjectPrefixURL方法");
		HttpServletRequest request = ServletActionContext.getRequest();
		String uri = request.getRequestURI().toString();
		String url = request.getRequestURL().toString();
		String subUrl = url.replace(uri, "").trim() + "/";
		log.info("执行getProjectPrefixURL方法成功，实现了”根据环境代码获取对应的网络资源外部链接“的功能");
		log.info("退出getProjectPrefixURL方法,返回String为："+subUrl);
		return subUrl;
	}


	//树==============================================================================
	
	/**
	 * ajax获取区域树（从顶级开始查找）
	 */
	public void getTopAreaTreeAjaxService(){
		log.info("进入getTopAreaTreeAjaxService方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Set<Map> set = new HashSet<Map>();
		set.add(this.getTopAreaTreeService());
		String result = gson.toJson(set);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回时出错");
			throw new UserDefinedException("返回时出错");
		}
		log.info("执行getTopAreaTreeAjaxService方法成功，实现了”ajax获取区域树（从顶级开始查找）“的功能");
		log.info("退出getTopAreaTreeAjaxService方法,返回void");
	}
	
	/**
	 * 获取区域树（从顶级开始查找）
	 */
	private Map getTopAreaTreeService(){
		log.info("进入getTopAreaTreeService方法");
		Map map = new HashMap();
		map.put("orgId", "0");
		map.put("orgName", "中国");
		List<Map<String, String>> topAreaService = this.getTopAreaService();
		List<Map> list = new ArrayList<Map>();
		if(topAreaService!=null && topAreaService.size()>0){
			for (Map<String,String> map2 : topAreaService){
				list.add(this.getAreaTreeDownwardByAreaIdService(map2.get("id")+"", map2.get("_entityType")+""));
			}
		}
		map.put("childTree", list);
		log.info("执行getTopAreaTreeService方法成功，实现了”获取区域树（从顶级开始查找）“的功能");
		log.info("退出getTopAreaTreeService方法,返回Map");
		return map;
	}
	
	/**
	 * 根据区域Id和区域类型向下获取区域
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	private Map getAreaTreeDownwardByAreaIdService(String areaId,String areaType){
		log.info("进入getAreaTreeDownwardByAreaIdService方法");
		log.info("参数areaId="+areaId+",areaType="+areaType);
		Map areaTree = new HashMap();
		List<Map> areaTreeNode = new ArrayList<Map>();
		Map<String, String> resourceService = this.getResourceByReIdAndReTypeService(areaId,areaType);
		if(resourceService!=null && resourceService.containsKey("id")){
			areaTree.put("orgId", resourceService.get("id"));
			areaTree.put("orgName", resourceService.get("name"));
			List<Map<String, String>> resourceService2 = this.getResourceService(resourceService.get("id"),resourceService.get("_entityType"),"Sys_Area","CHILD");
			if(resourceService2==null || resourceService2.size()<=0){
				return areaTree;
			}else{
				for (Map<String, String> map2 : resourceService2) {
					String subAreaId = map2.get("id");
					String subType = map2.get("_entityType");
					// 递归调用
					Map subTreeNode = this.getAreaTreeDownwardByAreaIdService(
							subAreaId, subType);
					if (subTreeNode != null) {
						areaTreeNode.add(subTreeNode);
					}
				}
				areaTree.put("childTree", areaTreeNode);
			}
		}
		log.info("执行getAreaTreeDownwardByAreaIdService方法成功，实现了”根据区域Id和区域类型向下获取区域“的功能");
		log.info("退出getAreaTreeDownwardByAreaIdService方法,返回Map");
		return areaTree;
	}
	
	//==========================================================内部方法

	/**
	 * 根据组织Id和资源类型获取区域Id的集合
	 */
	private Map<String,String> getAreaIdByOrgIdAndReType(long orgId,String reType){
		log.info("进入getAreaIdByOrgIdAndReType方法");
		log.info("参数orgId="+orgId+",reType="+reType);
		Map<String,String> areaIdMap = null;
		//ProviderOrganization po = this.providerOrganizationDao.getProviderOrganizationByOrgId(orgId);
		Map<String,Object> po = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		if(po==null){
			return areaIdMap;
		}
		
		areaIdMap = new HashMap<String, String>();
		if(OrganizationConstant.ORGANIZATION_BUSINESSDIVISION.equals(po.get("orgAttributeType")) || OrganizationConstant.ORGANIZATION_COMPANY.equals(po.get("orgAttributeType"))){
			//公司或事业部
			//获取其下的负责项目的组织
			//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getOrgListDownwardByOrg(orgId);
		    List<Map<String,Object>> orgList = sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(orgId+"");
			
		    
			if(orgList!=null && orgList.size()>0){
				for (Map<String,Object> orgMap : orgList) {
					long subOrgId = Long.parseLong((String)orgMap.get("orgId"));
					//List<Map<String, String>> findProjectByOrgId = this.providerOrganizationService.findProjectByOrgId(subOrgId+"");
					//yuan.yw
					List<Map<String, String>> findProjectByOrgId = this.projectInformationDao.findProjectByOrgId(subOrgId+"");
					if(findProjectByOrgId!=null && findProjectByOrgId.size()>0){
						for (Map<String, String> projectMap : findProjectByOrgId) {
							String projectId = projectMap.get("id");
							//List<Map<String, String>> findAreaIdByProjectOrgIdResType = this.providerOrganizationService.findAreaIdByProjectOrgIdResType(projectId,null,reType);
							//yuan.yw
							List<Map<String,String>> findAreaIdByProjectOrgIdResType = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(projectId,null,reType);
							if(findAreaIdByProjectOrgIdResType!=null && findAreaIdByProjectOrgIdResType.size()>0){
								for (Map<String, String> reMap : findAreaIdByProjectOrgIdResType) {
									areaIdMap.put(reMap.get("areaId"), reMap.get("areaId"));
								}
							}
						}
					}
				}
			}
		}else if(OrganizationConstant.ORGANIZATION_FUNCTIONDIVISION.equals(po.get("orgAttributeType"))){
			//职能部门
			//向上获取一级的组织，然后向下获取一级的组织
			//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
			//yuan.yw
			List<SysOrg> upProviderOrgByOrgIdService =this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId, "parent");
			if(upProviderOrgByOrgIdService != null && upProviderOrgByOrgIdService.size()>0){
				SysOrg upPo = upProviderOrgByOrgIdService.get(0);
				long upOrgId = upPo.getOrgId();
				//List<ProviderOrganization> nextProviderOrgByOrgIdService = this.providerOrganizationService.getNextProviderOrgByOrgIdService(upOrgId);
				List<SysOrg> nextProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(upOrgId, "child");
				if(nextProviderOrgByOrgIdService!=null && nextProviderOrgByOrgIdService.size()>0){
					for (SysOrg nextPo : nextProviderOrgByOrgIdService) {
						long nextOrgId = nextPo.getOrgId();
						//List<Map<String, String>> findProjectByOrgId = this.providerOrganizationService.findProjectByOrgId(nextOrgId+"");
						//yua.yw	
						List<Map<String, String>> findProjectByOrgId = this.projectInformationDao.findProjectByOrgId(nextOrgId+"");
						if(findProjectByOrgId!=null && findProjectByOrgId.size()>0){
							for (Map<String, String> projectMap : findProjectByOrgId) {
								String projectId = projectMap.get("id");
								//List<Map<String, String>> findAreaIdByProjectOrgIdResType = this.providerOrganizationService.findAreaIdByProjectOrgIdResType(projectId,null,reType);
								//yuan.yw
								List<Map<String,String>> findAreaIdByProjectOrgIdResType = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(projectId,null,reType);
							
								if(findAreaIdByProjectOrgIdResType!=null && findAreaIdByProjectOrgIdResType.size()>0){
									for (Map<String, String> reMap : findAreaIdByProjectOrgIdResType) {
										areaIdMap.put(reMap.get("areaId"), reMap.get("areaId"));
									}
								}
							}
						}
					}
				}
			}
		}else{
			//项目组或以下
			//List<Map<String, String>> findAreaIdByProjectOrgIdResType = this.providerOrganizationService.findAreaIdByProjectOrgIdResType(null,orgId+"",reType);
			//yuan.yw
			List<Map<String,String>> findAreaIdByProjectOrgIdResType = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(null,orgId+"",reType);
		
			if(findAreaIdByProjectOrgIdResType!=null && findAreaIdByProjectOrgIdResType.size()>0){
				for (Map<String, String> reMap : findAreaIdByProjectOrgIdResType) {
					areaIdMap.put(reMap.get("areaId"), reMap.get("areaId"));
				}
			}
		}
		log.info("执行getAreaIdByOrgIdAndReType方法成功，实现了”根据组织Id和资源类型获取区域Id的集合“的功能");
		log.info("退出getAreaIdByOrgIdAndReType方法,返回Map<String,String>");
		return areaIdMap;
	}
	
	private List<Map<String,String>> getAllArea(String areaId){
		log.info("进入getAllArea方法");
		log.info("参数areaId="+areaId);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> parentAreaByAreaId = getParentAreaByAreaId(areaId, "Sys_Area");
		if(parentAreaByAreaId!=null && !"".equals(parentAreaByAreaId.get("id"))){
			String id = parentAreaByAreaId.get("id");
			list.add(parentAreaByAreaId);
			list.addAll(getAllArea(id));
		}
		log.info("执行getAllArea方法成功，实现了”根据区域Id获取父区域“的功能");
		log.info("退出getAllArea方法,返回List<Map<String,String>>");
		return list;
	}
	
	/**
	 * 获取父或子区域
	 * @param orgId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,String>> getArea(long orgId,String orgType,String resourceType,String associatedType){
		log.info("进入getArea方法");
		log.info("参数orgId="+orgId+",orgType="+orgType+",resourceType="+associatedType);
		List<Map<String,String>> areaList = null;
		Map<String, String> areaIdByOrgIdAndReType = this.getAreaIdByOrgIdAndReType(orgId, resourceType);
//		List<Org_Area_Resource> oarList = this.providerOrganizationDao.getOrg_Area_ResourceByOrgIdAndOrgType(orgId+"", orgType, resourceType);
		if(areaIdByOrgIdAndReType != null){
			areaList = new ArrayList<Map<String,String>>();
			for (String areaId : areaIdByOrgIdAndReType.keySet()) {
				//获取环境代码
//				String code = this.getNetworkResourceUrl();
//				String interfaceURL = code + "resourcewebservice/getResourceByAssociatedTypeAction?reId="+areaId+"&reType=Area&selectReType=Area&associatedType="+associatedType;
				try {
					AssociatedType type = null;
					if("CHILD".equals(associatedType)){
						type = AssociatedType.CHILD;
					}else if("PARENT".equals(associatedType)){
						type = AssociatedType.PARENT;
					}else if("LINK".equals(associatedType)){
						type = AssociatedType.LINK;
					}
					List<Map<String, Object>> listObj = getResourceByAssociatedType(
							areaId, "Sys_Area","Sys_Area",type);
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					String res = gson.toJson(listObj);
//					res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
//					if(res != null && !res.equals("")){
//						list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
//					}else{
//						return null;
//					}
//					res = InterfaceUtil.httpPostClientReuqest(interfaceURL);
					List<Map<String,String>> list = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
					if(list!=null && list.size()>0){
						for (Map<String, String> map : list) {
							Map<String,String> areaMap = new HashMap<String, String>();
							areaMap.put("reId", map.get("id"));
							areaMap.put("reName", map.get("name"));
							areaMap.put("level", map.get("level"));
							areaMap.put("longitude", map.get("longitude"));
							areaMap.put("latitude", map.get("latitude"));
							areaList.add(areaMap);
						}
					}
				} catch (Exception e) {
					log.error("获取网络资源外部接口失败! 网络资源的path为：");//+interfaceURL);
				}
			}
//			Org_Area_Resource oar = oarList.get(0);
//			String areaId = oar.getAreaId();
		}
		log.info("执行getArea方法成功，实现了”获取父或子区域“的功能");
		log.info("退出getArea方法,返回List<Map<String,String>>");
		return areaList;
	}
	
	/**
	 * 获取用户物理区域
	 * @param account 用户帐号
	 * @return
	 */
	public Map getUserAreaService(String account) {
		log.info("进入getUserAreaService方法");
		log.info("参数account="+account);
		List<String> operationalAreaList = null;
		Set<String> operationalChildAreaList = new HashSet<String>();
		Set<String> oAreaList = new HashSet<String>();
		//根据登录人ID获取登录人所在组织管理的区域集合
		List<Map<String, String>> areaByUserId = this.getAreaByAccountService(account);
		Map<String, String> userAreaMap = null;
		Map<String, List<Map<String, String>>> childArea = new HashMap<String, List<Map<String,String>>>();
		if(areaByUserId != null && areaByUserId.size() > 0){
			operationalAreaList = new ArrayList<String>();
			for(Map<String, String> be :areaByUserId){
				String areaId = be.get("id")+"";
				//保存可选择当前节点
				operationalAreaList.add(areaId);	
				oAreaList.add(areaId);
				//保存可选择当前子节点
				operationalChildAreaList.add(areaId);
				
				parentAreaList = new ArrayList<Map<String, String>>();
				//根据区域获取其子级区域
				List<Map<String, String>> appArrsByRecursion = this.getResourceService(areaId, "Sys_Area", "Sys_Area", "CHILD");
				if(appArrsByRecursion != null && !appArrsByRecursion.isEmpty()){
					for(Map<String, String> a:appArrsByRecursion){
						String aareaId = a.get("id")+"";
						oAreaList.add(aareaId);
						//保存可选择当前子节点
						operationalChildAreaList.add(aareaId);
						//根据区域获取其子级区域
						List<Map<String, String>> appArrsByRecursions = this.getResourceService(aareaId, "Sys_Area", "Sys_Area", "CHILD");
						if(appArrsByRecursions != null && !appArrsByRecursions.isEmpty()){
							for(Map<String, String> aee:appArrsByRecursions){
								String arId = aee.get("id")+"";
								//保存可选择当前子节点
								operationalChildAreaList.add(arId);
							}
						}
					}
				}
				userAreaMap = be;
				//保存当前区域到parentAreaList
				parentAreaList.add(be);
				//递归查询当前区域的父级区域
				getParentArea(areaId);
				if(parentAreaList != null && parentAreaList.size() >0){
					for(Map<String, String> ma:parentAreaList){
						String maareaId = ma.get("id")+"";
						operationalAreaList.add(maareaId);
						operationalChildAreaList.add(maareaId);
						//根据区域获取其子级区域
						List<Map<String, String>> appArrsByChild = this.getResourceService(maareaId, "Sys_Area", "Sys_Area", "CHILD");
						if(appArrsByChild != null && !appArrsByChild.isEmpty()){
							List<Map<String, String>> resultList = null;
							resultList = new ArrayList<Map<String,String>>();
							for(Map<String, String> aee:appArrsByChild){
								resultList.add(aee);
							}
							//添加子级区域到子区域集合
							childArea.put(ma.get("id").toString(), resultList);
						}
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("parentArea", parentAreaList);
		resultMap.put("childArea", childArea);
		resultMap.put("userAreaMap", userAreaMap);
		resultMap.put("operationalAreaList", operationalAreaList);
		resultMap.put("operationalChildAreaList", operationalChildAreaList);
		resultMap.put("oAreaList", oAreaList);
		log.info("执行getUserAreaService方法成功，实现了”获取用户物理区域“的功能");
		log.info("退出getUserAreaService方法,返回Map");
		return resultMap;
	}
	//递归获取父级区域
	private void getParentArea(String areaId){
		log.info("进入getParentArea方法");
		log.info("参数areaId="+areaId);
		List<Map<String, String>> appArrsByRecursion = this.getResourceService(areaId, "Sys_Area", "Sys_Area", "PARENT");
		if(appArrsByRecursion != null && !appArrsByRecursion.isEmpty()){
			parentAreaList.add(0,appArrsByRecursion.get(0));
			String aId = appArrsByRecursion.get(0).get("id");
			getParentArea(aId);
			log.info("执行getParentArea方法成功，实现了”递归获取父级区域“的功能");
			log.info("退出getParentArea方法,返回void");
		}else{
			log.info("返回值为空");
			return;
		}
	}
	
	


	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}

	public FirstLetterService getFirstLetterService() {
		return firstLetterService;
	}

	public void setFirstLetterService(FirstLetterService firstLetterService) {
		this.firstLetterService = firstLetterService;
	}
	
	
	
	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}

	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}

	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}

	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	public NetworkResourceQueryDao getNetworkResourceQueryDao() {
		return networkResourceQueryDao;
	}


	public void setNetworkResourceQueryDao(
			NetworkResourceQueryDao networkResourceQueryDao) {
		this.networkResourceQueryDao = networkResourceQueryDao;
	}


	
	
}
