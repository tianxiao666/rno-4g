package com.iscreate.op.service.informationmanage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.ProjectConstant;
import com.iscreate.op.service.outlinking.OutLinkingService;
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

/**
 * @author andy
 *
 */
public class InformationNetworkServiceImpl implements InformationNetworkService {
	
	private Log log = LogFactory.getLog(this.getClass());
	private StructureCommonService structureCommonService;
	private QuickSort<Map<String,Object>> quickSort;//排序
	public Dictionary dictionary;
	
	
	/**
	 * 递归获取父区域（包含传入的Id）
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	/**
	 * 递归获取父区域（包含传入的Id）
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaWithSelfByAreaIdService(String areaId,String areaType){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> resourceByReIdAndReTypeService = getResourceByReIdAndReTypeService(areaId,areaType);
		if(resourceByReIdAndReTypeService!=null && resourceByReIdAndReTypeService.size()>0){
			list.add(resourceByReIdAndReTypeService);
			List<Map<String,String>> subList = getRecursionParentAreaByAreaIdService(areaId,areaType);
			if(subList!=null && subList.size()>0){
				list.addAll(subList);
			}
		}
		return list;
	}
	
	/**
	 * 根据资源Id和资源类型获取资源
	 * @param resourceId
	 * @param resourceType
	 * @return
	 */
	public Map<String,String> getResourceByReIdAndReTypeService(String resourceId,String resourceType){
		Map<String, Map<String, String>> baseFacilityToMapService = this.getBaseFacilityToMapService(resourceId,resourceType);
		if(baseFacilityToMapService!=null && baseFacilityToMapService.containsKey("entity")){
			return baseFacilityToMapService.get("entity");
		}
		return null;
	}
	
	
	/**
	 * 根据资源Id和资源类型获取基础设施的信息
	 * @param resourceId
	 * @param resourceType
	 * @return Map<String,Map<String,String>>
	 */
	public Map<String,Map<String,String>> getBaseFacilityToMapService(String resourceId,String resourceType){
		
		String reType = resourceType;
		String reId = resourceId;
		
		log.info("进入===getBaseFacilityToMapService方法");
		ApplicationModule module = ModuleProvider.getModule(reType);
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(reType, reId);
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		Map<String, Map<String, Object>> maps = new HashMap<String, Map<String,Object>>();
		if(sectionEntity !=null){
			Map<String, Object> map = new HashMap<String, Object>();
			map = module.toMap();
			Map<String, Object> sectionMap = sectionEntity.toMap();
			Map<String, Object> infoMapChineseMap = new HashMap<String, Object>();
			String infoName = reType;
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
		
		Map<String,Map<String,String>> result_map = null;
		Gson gson = new Gson();
		String json = gson.toJson(maps);
		result_map = gson.fromJson(json, new TypeToken<Map<String,Map<String,String>>>(){}.getType());
		
		return result_map;
	}
	
	
	/**
	 * 递归获取父区域（不包含传入的Id）
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaByAreaIdService(String areaId,String areaType){
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
		return list;
	}
	
	/**
	 * 根据区域Id获取父级区域
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public Map<String,String> getParentAreaByAreaId(String areaId,String areaType){
		
		
		String reType = areaType;
		String reId = areaId;
		String selectReType = "Sys_Area";
		String associatedType = "PARENT";
		log.info("进入===getParentAreaByAreaId方法");
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(reType, reId);
		AssociatedType type = null;
		if("CHILD".equals(associatedType)){
			type = AssociatedType.CHILD;
		}else if("PARENT".equals(associatedType)){
			type = AssociatedType.PARENT;
		}else if("LINK".equals(associatedType)){
			type = AssociatedType.LINK;
		}
		ApplicationEntity[] strutureSelationsApplicationEntity = structureCommonService.getStrutureSelationsApplicationEntity(sectionEntity,selectReType, type, "networkresourcemanage");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(strutureSelationsApplicationEntity != null && strutureSelationsApplicationEntity.length > 0){
			log.info("开始循环strutureSelationsApplicationEntity");
			for(ApplicationEntity ae:strutureSelationsApplicationEntity){
				list.add(ae.toMap());
			}
			log.info("结束循环strutureSelationsApplicationEntity");
		}
		
		Map<String , String> result_map = new HashMap<String,String> ();
		Map<String,Object> map = new HashMap<String,Object> ();
		
		
		if ( list != null && list.size() > 0 ) {
			map = list.get(0);
			Gson gson = new Gson();
			String json = gson.toJson(map);
			result_map = gson.fromJson(json, new TypeToken<Map<String , String> >(){}.getType());
		}
		
		return result_map;
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

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	
//	private String getProjectPrefixURL() {
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String uri = request.getRequestURI().toString();
//		String url = request.getRequestURL().toString();
//		return url.replace(uri, "").trim() + "/";
//	}
	
	
	
	
	
}
