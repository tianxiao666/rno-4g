package com.iscreate.plat.networkresource.common.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.dao.networkresourcemanage.NetworkResourceQueryDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.dao.NetworkResourceManageDao;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.CompareOriginal;
import com.iscreate.plat.networkresource.common.tool.LatLngConversion;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.structure.instance.Structure;
import com.iscreate.plat.networkresource.structure.instance.StructureView;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleLibrary;
import com.iscreate.uniqueutil.Unique;
/**
 * StructureCommonServiceImpl
 * @author Jasmin
 *
 */
public class StructureCommonServiceImpl implements StructureCommonService{
	
	private StructureModuleLibrary structureModuleLibrary;
	private XMLAEMLibrary moduleLibrary;
	private HibernateTemplate hibernateTemplate;
	//private NetworkResourceDao networkResourceDao;
	private SysAreaDao sysAreaDao;//区域dao
	private NetworkResourceQueryDao networkResourceQueryDao;//网络资源查询dao
	
	private NetworkResourceManageDao networkResourceManageDao;
	
	
	
	private  static final  Log log = LogFactory.getLog(StructureCommonServiceImpl.class);
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}

	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}


	public StructureModuleLibrary getStructureModuleLibrary() {
		return structureModuleLibrary;
	}

	public void setStructureModuleLibrary(
			StructureModuleLibrary structureModuleLibrary) {
		this.structureModuleLibrary = structureModuleLibrary;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, ApplicationEntity[]>
	 */
	public Map<String, ApplicationEntity[]> getStrutureSelationsMap(ApplicationEntity entity,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsMap方法");
		Map<String, ApplicationEntity[]> map = new HashMap<String, ApplicationEntity[]>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		log.info("获取资源类型分组 associatedAetNames:"+associatedAetNames);
		if(associatedAetNames != null){
			log.info("开始循环associatedAetNames");
			for(String associatedAetName:associatedAetNames){
				//List<ApplicationEntity> list = null;
				//ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
				//list = new ArrayList<ApplicationEntity>(Arrays.asList(associatedEntity));
				long id = 0;
				String entityType = "";
				if(entity != null){
					id = Long.parseLong(entity.getValue("id")+"");
					entityType = entity.getValue("_entityType")+"";
				}else{
					return null;
				}
				List<BasicEntity> blist = null;
				String aetName = "";
				//根据不同的关系类型获取对应资源
				if(associatedType != null){
					if(associatedType.equals(AssociatedType.PARENT)){
						//关系类型为父
						blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType, associatedAetName);
						if(blist != null && blist.size() > 0){
							aetName = blist.get(0).getValue("_entityType");
						}else{
							continue;
						}
					}else if(associatedType.equals(AssociatedType.CHILD)){
						//关系类型为子
						blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}else if(associatedType.equals(AssociatedType.LINK)){
						//关系类型为关联
						blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}
				}else{
					return null;
				}
				if(blist != null){
					//转换数据类型
					ApplicationEntity[] typeConversionArrayByBasicEntity = typeConversionArrayByBasicEntity(blist);
					map.put(aetName, typeConversionArrayByBasicEntity);
				}else{
					continue;
				}
			}
			log.info("结束循环associatedAetNames");
		}
		log.info("退出===getStrutureSelationsMap方法 返回值为:"+map);
		return map;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public Map<String, List<Map<String, Object>>> getStrutureSelationsReturnMap(ApplicationEntity entity,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsReturnMap方法");
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		log.info("获取资源类型分组 associatedAetNames:"+associatedAetNames);
		if(associatedAetNames != null){
			log.info("开始循环associatedAetNames");
			for(String associatedAetName:associatedAetNames){
//				//List<ApplicationEntity> list = null;
//				Map[] maps = new Map[]{};
//				List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
//				ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
//				int i = 0;
//				for(ApplicationEntity a: associatedEntity){
//					lists.add(a.toMap());
//				}
//				//list = new ArrayList<ApplicationEntity>(Arrays.asList(associatedEntity));
//				map.put(associatedAetName, lists);
				long id = 0;
				String entityType = "";
				if(entity != null){
					id = Long.parseLong(entity.getValue("id")+"");
					entityType = entity.getValue("_entityType")+"";
				}else{
					return null;
				}
				List<BasicEntity> blist = null;
				String aetName = "";
				//根据不同的关系类型获取对应资源
				if(associatedType != null){
					if(associatedType.equals(AssociatedType.PARENT)){
						//关系类型为父
						blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,associatedAetName);
						if(blist != null && blist.size() > 0){
							aetName = blist.get(0).getValue("_entityType");
						}else{
							continue;
						}
					}else if(associatedType.equals(AssociatedType.CHILD)){
						//关系类型为子
						blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}else if(associatedType.equals(AssociatedType.LINK)){
						//关系类型为关联
						blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}
				}else{
					return null;
				}
				if(blist != null){
					//转换数据类型
					List<Map<String,Object>> typeConversionMapListByBasicEntity = typeConversionMapListByBasicEntity(blist);
					map.put(aetName, typeConversionMapListByBasicEntity);
				}else{
					continue;
				}
			}
			log.info("结束循环associatedAetNames");
		}
		log.info("提出===getStrutureSelationsReturnMap方法 返回值为:"+map);
		return map;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * * @param resourcesTypes 资源的类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public Map<String, List<Map<String, Object>>> getStrutureSelationsReturnMap(ApplicationEntity entity,String[] resourcesTypes,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsReturnMap方法");
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		if(resourcesTypes != null){
			log.info("开始循环resourcesTypes");
			for(String associatedAetName:resourcesTypes){
//				//List<ApplicationEntity> list = null;
//				Map[] maps = new Map[]{};
//				List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
//				ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
//				int i = 0;
//				for(ApplicationEntity a: associatedEntity){
//					
//					Map<String, Object> map2 = a.toMap();
//					map2.put("type", a.getType());
//					lists.add(map2);
//				}
//				//list = new ArrayList<ApplicationEntity>(Arrays.asList(associatedEntity));
//				map.put(associatedAetName, lists);
				long id = 0;
				String entityType = "";
				if(entity != null){
					id = Long.parseLong(entity.getValue("id")+"");
					entityType = entity.getValue("_entityType")+"";
				}else{
					return null;
				}
				List<BasicEntity> blist = null;
				String aetName = "";
				//根据不同的关系类型获取对应资源
				if(associatedType != null){
					if(associatedType.equals(AssociatedType.PARENT)){
						//关系类型为父
						blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,associatedAetName);
						if(blist != null && blist.size() > 0){
							aetName = blist.get(0).getValue("_entityType");
						}else{
							continue;
						}
					}else if(associatedType.equals(AssociatedType.CHILD)){
						//关系类型为子
						blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}else if(associatedType.equals(AssociatedType.LINK)){
						//关系类型为关联
						blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}
				}else{
					return null;
				}
				if(blist != null){
					//转换数据类型
					List<Map<String,Object>> typeConversionMapListByBasicEntity = typeConversionMapListByBasicEntity(blist);
					List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
					for(Map<String,Object> m: typeConversionMapListByBasicEntity){
						
						Map<String, Object> map2 = m;
						m.put("type", m.get("_entityType"));
						lists.add(map2);
					}
					map.put(aetName, lists);
				}else{
					continue;
				}
			}
			log.info("结束循环resourcesTypes");
			
		}
		log.info("提出===getStrutureSelationsReturnMap方法 返回值为:"+map);
		return map;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * * @param resourcesTypes 资源的类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>> 
	 */
	public List<Map<String, Object>> getStrutureSelationsReturnList(ApplicationEntity entity,String[] resourcesTypes,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsReturnList方法");
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		if(resourcesTypes != null){
			log.info("开始循环resourcesTypes");
			for(String associatedAetName:resourcesTypes){
//				//List<ApplicationEntity> list = null;
//				Map[] maps = new Map[]{};
//				ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
//				int i = 0;
//				for(ApplicationEntity a: associatedEntity){
//					Map<String, Object> map2 = a.toMap();
//					map2.put("type", a.getType());
//					lists.add(map2);
//				}
//				//list = new ArrayList<ApplicationEntity>(Arrays.asList(associatedEntity));
				long id = 0;
				String entityType = "";
				if(entity != null){
					id = Long.parseLong(entity.getValue("id")+"");
					entityType = entity.getValue("_entityType")+"";
				}else{
					return null;
				}
				List<BasicEntity> blist = null;
				String aetName = "";
				//根据不同的关系类型获取对应资源
				if(associatedType != null){
					if(associatedType.equals(AssociatedType.PARENT)){
						//关系类型为父
						blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,associatedAetName);
						if(blist != null && blist.size() > 0){
							aetName = blist.get(0).getValue("_entityType");
						}else{
							continue;
						}
					}else if(associatedType.equals(AssociatedType.CHILD)){
						//关系类型为子
						blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}else if(associatedType.equals(AssociatedType.LINK)){
						//关系类型为关联
						blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, associatedAetName);
						aetName = associatedAetName;
					}
				}else{
					return null;
				}
				if(blist != null){
					//转换数据类型
					List<Map<String,Object>> typeConversionMapListByBasicEntity = typeConversionMapListByBasicEntity(blist);
					for(Map<String,Object> m: typeConversionMapListByBasicEntity){
						
						Map<String, Object> map2 = m;
						m.put("type", m.get("_entityType"));
						lists.add(map2);
					}
				}else{
					continue;
				}
			}
			log.info("结束循环resourcesTypes");
		}
		log.info("退出===getStrutureSelationsReturnList方法 返回值为："+lists);
		return lists;
	}
	
	
	
	
	/**
	 * 根据不同对象查询其不同关系资源实例集合
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public Map<String, List<Map<String, Object>>> getStrutureSelationsApplicationMapList(ApplicationEntity entity,String resourcesType,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsApplicationMapList方法");
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
//		ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,resourcesType, associatedType);
//		if(associatedEntity != null){
//			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
//			log.info("开始循环associatedEntity");
//			for(ApplicationEntity a: associatedEntity){
//				lists.add(a.toMap());
//			}
//			log.info("结束循环associatedEntity");
//			map.put(resourcesType, lists);
//		}
		long id = 0;
		String entityType = "";
		if(entity != null){
			id = Long.parseLong(entity.getValue("id")+"");
			entityType = entity.getValue("_entityType")+"";
		}else{
			return null;
		}
		List<BasicEntity> blist = null;
		String aetName = "";
		//根据不同的关系类型获取对应资源
		if(associatedType != null){
			if(associatedType.equals(AssociatedType.PARENT)){
				//关系类型为父
				blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,resourcesType);
				if(blist != null && blist.size() > 0){
					aetName = blist.get(0).getValue("_entityType");
				}else{
					return null;
				}
			}else if(associatedType.equals(AssociatedType.CHILD)){
				//关系类型为子
				blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, resourcesType);
				aetName = resourcesType;
			}else if(associatedType.equals(AssociatedType.LINK)){
				//关系类型为关联
				blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, resourcesType);
				aetName = resourcesType;
			}
		}else{
			return null;
		}
		if(blist != null){
			//转换数据类型
			List<Map<String,Object>> typeConversionMapListByBasicEntity = typeConversionMapListByBasicEntity(blist);
				map.put(resourcesType, typeConversionMapListByBasicEntity);
		}else{
			return null;
		}
		log.info("退出===getStrutureSelationsApplicationMapList方法 返回值为:"+map);
		return map;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源实例数组
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getStrutureSelationsApplicationMap(ApplicationEntity entity,String resourcesType,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsApplicationMap方法");
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
//		ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,resourcesType, associatedType);
//		if(associatedEntity != null){
//			log.info("开始循环associatedEntity");
//			for(ApplicationEntity a: associatedEntity){
//				lists.add(a.toMap());
//			}
//			log.info("结束循环associatedEntity");
//		}
		long id = 0;
		String entityType = "";
		if(entity != null){
			id = Long.parseLong(entity.getValue("id")+"");
			entityType = entity.getValue("_entityType")+"";
		}else{
			return null;
		}
		List<BasicEntity> blist = null;
		String aetName = "";
		//根据不同的关系类型获取对应资源
		if(associatedType != null){
			if(associatedType.equals(AssociatedType.PARENT)){
				//关系类型为父
				blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,resourcesType);
				if(blist != null && blist.size() > 0){
					aetName = blist.get(0).getValue("_entityType");
				}else{
					return null;
				}
			}else if(associatedType.equals(AssociatedType.CHILD)){
				//关系类型为子
				blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, resourcesType);
				aetName = resourcesType;
			}else if(associatedType.equals(AssociatedType.LINK)){
				//关系类型为关联
				blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, resourcesType);
				aetName = resourcesType;
			}
		}else{
			return null;
		}
		if(blist != null){
			//转换数据类型
			List<Map<String,Object>> typeConversionMapListByBasicEntity = typeConversionMapListByBasicEntity(blist);
			lists = typeConversionMapListByBasicEntity;
		}else{
			return null;
		}
		log.info("结束===getStrutureSelationsApplicationMap方法 返回值为:"+lists);
		return lists;
	}
	
	/**
	 * 根据不同对象查询其不同关系指定类型资源实例数组
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public ApplicationEntity[] getStrutureSelationsApplicationEntity(ApplicationEntity entity,String resourcesType,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsApplicationEntity方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
//		ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,resourcesType, associatedType);
		ApplicationEntity[] associatedEntity = null;
		long id = 0;
		String entityType = "";
		if(entity != null){
			id = Long.parseLong(entity.getValue("id")+"");
			entityType = entity.getValue("_entityType")+"";
		}else{
			return null;
		}
		List<BasicEntity> blist = null;
		//根据不同的关系类型获取对应资源
		if(associatedType != null){
			if(associatedType.equals(AssociatedType.PARENT)){
				//关系类型为父
				blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,resourcesType);
				if(blist != null && blist.size() > 0){
				}else{
					return null;
				}
			}else if(associatedType.equals(AssociatedType.CHILD)){
				//关系类型为子
				blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, resourcesType);
			}else if(associatedType.equals(AssociatedType.LINK)){
				//关系类型为关联
				blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, resourcesType);
			}
		}else{
			return null;
		}
		if(blist != null){
			//转换数据类型
			associatedEntity = typeConversionArrayByBasicEntity(blist);
			//lists = typeConversionMapListByBasicEntity;
		}else{
			return null;
		}
		log.info("退出===getStrutureSelationsApplicationEntity方法 返回值为："+associatedEntity);
		return associatedEntity;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public ApplicationEntity getStrutureSelationsEntity(ApplicationEntity entity,String resourcesType,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsEntity方法");
		ApplicationEntity appEntity = new ApplicationEntity();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		long id = 0;
		String entityType = "";
		if(entity != null){
			id = Long.parseLong(entity.getValue("id")+"");
			entityType = entity.getValue("_entityType")+"";
		}else{
			return null;
		}
		List<BasicEntity> blist = null;
		//根据不同的关系类型获取对应资源
		if(associatedType != null){
			if(associatedType.equals(AssociatedType.PARENT)){
				//关系类型为父
				blist = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,resourcesType);
			}else if(associatedType.equals(AssociatedType.CHILD)){
				//关系类型为子
				blist = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, resourcesType);
			}else if(associatedType.equals(AssociatedType.LINK)){
				//关系类型为关联
				blist = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, resourcesType);
			}
		}else{
			return null;
		}
		if(blist != null){
			//转换数据类型
			List<ApplicationEntity> applicationEntityList = typeConversionApplicationEntityListByBasicEntity(blist);
			if(applicationEntityList != null && applicationEntityList.size() > 0){
				appEntity = applicationEntityList.get(0);
			}
		}
		log.info("退出===getStrutureSelationsEntity方法 返回值为:"+appEntity);
		return appEntity;
	}
	

	/**
	 * 根据不同对象查询其不同关系资源
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public ApplicationEntity[] getStrutureSelationsApplicationEntity(ApplicationEntity entity,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsApplicationEntity方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		ApplicationEntity[] associatedEntity = null;
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		List<BasicEntity> blist = new ArrayList<BasicEntity>();
		log.info("获取资源类型分组 associatedAetNames:"+associatedAetNames);
		if(associatedAetNames != null){
			log.info("开始循环associatedAetNames");
			for(String associatedAetName:associatedAetNames){
				long id = 0;
				String entityType = "";
				if(entity != null){
					id = Long.parseLong(entity.getValue("id")+"");
					entityType = entity.getValue("_entityType")+"";
				}else{
					return null;
				}
				//根据不同的关系类型获取对应资源
				if(associatedType != null){
					if(associatedType.equals(AssociatedType.PARENT)){
						//关系类型为父
						List<BasicEntity> basicEntityList = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(id, entityType,associatedAetName);
						if(basicEntityList != null){
							blist.addAll(basicEntityList);
						}
					}else if(associatedType.equals(AssociatedType.CHILD)){
						//关系类型为子
						List<BasicEntity> basicEntityList = this.networkResourceManageDao.getChildrenResourceByResourceType(id, entityType, associatedAetName);
						if(basicEntityList != null){
							blist.addAll(basicEntityList);
						}
					}else if(associatedType.equals(AssociatedType.LINK)){
						//关系类型为关联
						List<BasicEntity> basicEntityList = this.networkResourceManageDao.getLINKResourceByReIdAndReTypeAndSelectType(id, entityType, associatedAetName);
						if(basicEntityList != null){
							blist.addAll(basicEntityList);
						}
					}
				}else{
					return null;
				}
				if(blist != null){
					//转换数据类型
					ApplicationEntity[] applicationEntitys = typeConversionArrayByBasicEntity(blist);
					associatedEntity = applicationEntitys;
				}
			}
		}
//		ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity, associatedType);
		
		log.info("退出===getStrutureSelationsApplicationEntity方法 返回值为："+associatedEntity);
		return associatedEntity;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return String[];
	 */
	public String[] getStrutureSelationsArray(ApplicationEntity entity,AssociatedType associatedType,String myStructure){
		log.info("进入===getStrutureSelationsArray方法");
		/*StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		return associatedAetNames;*/
		String[] strutureSelationsArray = getStrutureSelationsArray(entity, associatedType, null, myStructure);
		log.info("退出===getStrutureSelationsArray方法 返回值为："+strutureSelationsArray);
		return strutureSelationsArray;
	}
	

	/**
	 * 根据不同对象查询其不同关系资源类型数组(指定视图)
	 * @param entity需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return
	 */
	public String[] getStrutureSelationsArray(ApplicationEntity entity,AssociatedType associatedType, String viewName, String myStructure) {
		log.info("进入===getStrutureSelationsArray方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = null;
		if(viewName != null && !"".equals(viewName)) {
			StructureView structureView = structure.myView(viewName);
			if(structureView != null){
				associatedAetNames = structureView.getAssociatedAetName(entity, associatedType);
			}else{
				associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
			}
			
		} else {
			associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		}
		log.info("进入===getStrutureSelationsArray方法 返回值为："+associatedAetNames);
		return associatedAetNames;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMap(ApplicationEntity entity,AssociatedType associatedType,String addLink, String myStructure) {
		log.info("进入===getStrutureSelationsTypeMap方法");
		List<Map<String,Object>> strutureSelationsTypeMap = getStrutureSelationsTypeMap(entity, associatedType, addLink, null, myStructure);
		log.info("退出===getStrutureSelationsTypeMap方法 返回值为："+strutureSelationsTypeMap);
		return strutureSelationsTypeMap;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMap(ApplicationEntity entity,AssociatedType associatedType,String addLink, String viewName, String myStructure){
		log.info("进入===getStrutureSelationsTypeMap方法");
		List<Map<String,Object>> strutureSelationsTypeMap = getStrutureSelationsTypeMap(entity,null,associatedType,addLink, viewName, myStructure);
		log.info("退出===getStrutureSelationsTypeMap方法 返回值为："+strutureSelationsTypeMap);
		return strutureSelationsTypeMap;
	}
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMap(ApplicationEntity entity,String[] searchTypes,AssociatedType associatedType,String addLink, String viewName, String myStructure){
		log.info("进入===getStrutureSelationsTypeMap方法");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		/*Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);*/
		Context ctx = structureModule.createContext();
		String[] associatedAetNames = null;
		if(searchTypes != null&&!"null".equals(searchTypes)&&!"".equals(searchTypes)){
			associatedAetNames = searchTypes;
		}else{
			if(viewName != null&&!"null".equals(viewName)&&!"".equals(viewName)) {
				associatedAetNames = getStrutureSelationsArray(entity, associatedType, viewName, myStructure);
			} else {
				associatedAetNames = getStrutureSelationsArray(entity, associatedType, myStructure);
			}
		}
		log.info("associatedAetNames："+associatedAetNames);
		if(associatedAetNames != null){
			log.info("开始循环associatedAetNames");
			for(String associatedAetName:associatedAetNames){
				log.info("associatedAetName:"+associatedAetName);
				Map<String, Object> map = new HashMap<String, Object>();
				/**************************获取类型同时，获取数量的代码段*********************************/
				int count=0;
				//查询
				String hql = "";
				String selStr = ResourceCommon.getSelectSqlAttributsString("Figurenode","fn");
				if(addLink!=null){//addLink不为空 要获得link child关联类型数量
					//hql="select "+selStr+" from figurenode,figureline,figure where figure.figureId= figureline.figureId and figure.figureName='PipeAndCable_ReleatedResource-"+myStructure+"' and figurenode.entityType='"+associatedAetName+"' and figurenode.id=figureline.rightId and figureline.leftId=(select id from figurenode where entityId='"+entity.getValue("_entityId")+"')";
					hql = "select "+selStr+" from figurenode fn,figurenode f  where fn.entitytype='"+associatedAetName+"' and fn.parent_figurenode_id=f.id and f.path like '%/"+entity.getType()+"/"+entity.getValue("id")+"/'";
					log.info("hql:"+hql);
					SqlContainer sq= ctx.createSqlContainer(hql);
					List<BasicEntity> s = ctx.executeSelectSQL(sq,"Figurenode");
					if(s!=null&&s.size()>1){
						count += s.size()-1;
					}
					selStr = ResourceCommon.getSelectSqlAttributsString("Figurenode");
					hql="select "+selStr+" from figurenode,figureline,figure where figure.figureId= figureline.figureId and figure.figureName='PipeAndCable_ReleatedResource-"+myStructure+"' and figurenode.entityType='"+associatedAetName+"' and figurenode.id=figureline.leftId and figureline.rightId=(select id from figurenode where entityId='"+entity.getValue("_entityId")+"') and figureline.linkType='LINK'";
					log.info("hql:"+hql);
					sq= ctx.createSqlContainer(hql);
					s = ctx.executeSelectSQL(sq,"Figurenode");
					if(s!=null&&s.size()!=0){
						count += s.size();
					}
					////////System.out.println(addLink+"---");
					////////System.out.println(count+"---");
				}else{//addLink为空 获得 child关联类型数量
					//hql="select "+selStr+" from figurenode,figureline,figure where figure.figureId= figureline.figureId and figure.figureName='PipeAndCable_ReleatedResource-"+myStructure+"' and figurenode.entityType='"+associatedAetName+"' and figurenode.id=figureline.rightId and figureline.leftId=(select id from figurenode where entityId='"+entity.getValue("_entityId")+"')";
					hql = "select "+selStr+" from figurenode fn,figurenode f  where fn.entitytype='"+associatedAetName+"' and fn.parent_figurenode_id=f.id and f.path like '%/"+entity.getType()+"/"+entity.getValue("id")+"/'";
					log.info("hql:"+hql);
					SqlContainer sq= ctx.createSqlContainer(hql);
					List<BasicEntity> s = ctx.executeSelectSQL(sq,"Figurenode");
					if(s!=null&&s.size()>1){
						count += s.size()-1;
					}
				}
				////////System.out.println(count+"*****");
				map.put("count", count);
				/*Query query =ctx.createQueryBuilder(associatedAetName);
				ApplicationEntity[] associatedEntity=structure.getAssociatedEntity(entity, associatedType, query);
				//////System.out.println(associatedEntity.length);
				//ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
				
				
				
				int count = 0;
				if(associatedEntity != null){
					count = associatedEntity.length;
				}
				/*
				//需要获取link资源的情况(求出child和link的数量总和)
				if(addLink != null) {
					if(associatedType == AssociatedType.CHILD) {
						AssociatedType assType = AssociatedType.LINK;
						ApplicationEntity[] assLinkEntity = structure.getAssociatedEntity(entity,associatedAetName, assType);
						if(assLinkEntity != null) {
							count += assLinkEntity.length;
						}
					} else if(associatedType == AssociatedType.LINK) {
						AssociatedType assType = AssociatedType.CHILD;
						ApplicationEntity[] assLinkEntity = structure.getAssociatedEntity(entity,associatedAetName, assType);
						if(assLinkEntity != null) {
							count += assLinkEntity.length;
						}
					}
				}
				map.put("count", count);*/
				
				map.put("type", associatedAetName);
				if(isChildTypeOfEntity(entity,associatedAetName,myStructure)) {
					map.put("assType", "child");
				} else if(isLinkTypeOfEntity(entity,associatedAetName,myStructure)) {
					map.put("assType", "link");
				}
				list.add(map);
			}
			log.info("结束循环associatedAetNames");
		}
		log.info("退出===getStrutureSelationsTypeMap方法 返回值为："+list);
		return list;
		
	}
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMap(ApplicationEntity entity,AssociatedType associatedType, String myStructure) {
		log.info("进入===getStrutureSelationsTypeMap方法");
		List<Map<String,Object>> strutureSelationsTypeMap = getStrutureSelationsTypeMap(entity, associatedType, null, myStructure);
		log.info("退出===getStrutureSelationsTypeMap方法 返回值为："+strutureSelationsTypeMap);
		return strutureSelationsTypeMap;
	}
	
	/**
	 * 建立两个对象之间的关系
	 * @param leftAe 关联资源对象
	 * @param rightAe 被关联资源对象
	 * @param associatedType 需要创建的关系类型
	 * @param myStructure Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public int createAssociatedRelation(ApplicationEntity leftAe, ApplicationEntity rightAe, AssociatedType associatedType,String myStructure) {
		log.info("进入===createAssociatedRelation方法");
		int result = 1;
		try {
			StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
			Structure structure = structureModule.myStructure(myStructure);
			structure.createAssociation(leftAe, rightAe, associatedType);
			result = structure.store();
		} catch (Exception e) {
			log.error("创建对象关系失败");
			e.printStackTrace();
			result = 0;
		}
		log.info("退出===createAssociatedRelation方法 返回值为："+result);
		return result;
	}
	
	/**
	 * 根据ID与类型查询对象属性
	 * @param entityType 需要查询的资源类型
	 * @param id 需要查询的资源ID
	 * @return ApplicationEntity
	 */
	public ApplicationEntity getSectionEntity(String entityType,String id){
		log.info("进入===getSectionEntity方法");
		//Context createContext = structureModuleLibrary.createContext();
		//StructureModule structureModule = structureModuleLibrary.getStructureModule("Pipe&Cable_ReleatedResource");
		//Structure structure = structureModule.myStructure("networkresourcemanage");
		if(id == null || "".equals(id)){
			log.info("退出===getSectionEntity方法 返回值为：null");
			return null;
		}
		try {
			Context context = structureModuleLibrary.createContext();
			/*Query query = context.createQueryBuilder(entityType);
			query.add(Restrictions.eq("id", id));*/
//			String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(entityType)+" from "+entityType+" where id="+id;
//			
//			SqlContainer sc = context.createSqlContainer(sqlString);
//			List<BasicEntity> beList = context.executeSelectSQL(sc, entityType);
			long longId = Long.parseLong(id);
			List<BasicEntity> beList = this.networkResourceManageDao.getResourceByIdAndReType(longId, entityType);
			//System.out.println("beList:"+beList);
			BasicEntity queryEntity =null;
			if(beList!=null){
				queryEntity = beList.get(0);
			}
			//BasicEntity queryEntity = context.queryEntity(query);
			if(queryEntity==null){
				log.info("退出===getSectionEntity方法 返回值为：null");
				return null;
			}
			//System.out.println(queryEntity.getValue("name"));
			ApplicationEntity returnFiberSection = ApplicationEntity.changeFromEntity(queryEntity);
			log.info("退出===getSectionEntity方法 返回值为："+returnFiberSection);
			
			return  returnFiberSection;
		} catch (Exception e) {
			log.error("根据ID与类型查询对象属性获取失败");
			e.printStackTrace();
			log.info("退出===getSectionEntity方法 返回值为：null");
			return null;
		}
	}
	
	/**
	 * 删除两个对象间的关系
	 * @param leftAe 左资源
	 * @param rightAe 右资源
	 * @param associatedType 需要删除的关系类型
	 * @return int 1为成功 0为失败
	 */
	public int delStrutureAssociation(ApplicationEntity leftAe,ApplicationEntity rightAe,AssociatedType associatedType,String myStructure){
		log.info("进入===delStrutureAssociation方法");
		int result = 1;
		try {
			StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
			Structure structure = structureModule.myStructure(myStructure);
			structure.delAssociation(leftAe, rightAe, associatedType);
			result = structure.store();
		} catch (Exception e) {
			log.error("删除对象关系失败");
			e.printStackTrace();
			result = 0;
		}
		log.info("退出===delStrutureAssociation方法 返回值为："+result);
		return result;
	}
	
	/**
	 * 删除对象的关系(递归删除)
	 * @param appEntity 需要删除关系的资源对象
	 * @param myStructure  Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public int delEntityByRecursion(ApplicationEntity appEntity, String myStructure) {
		log.info("进入===delEntityByRecursion方法");
		int result = 1;
		try {
			StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
			Structure structure = structureModule.myStructure(myStructure);
			structure.delApplicationEntityRecursion(appEntity);
			result = structure.store();
		} catch (RuntimeException e) {
			log.error("删除对象的关系(递归删除)失败");
			e.printStackTrace();
			result = 0;
		}
		log.info("退出===delEntityByRecursion方法 返回值为："+result);
		return result;
	}
	
	/**
	 * 删除对象的关系(递归删除)
	 * @param appEntity 需要删除关系的资源对象
	 * @param myStructure Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public int delEntityByRecursionOnly(ApplicationEntity appEntity, String myStructure) {
		log.info("进入===delEntityByRecursionOnly方法");
		int result = 1;
		try {
			StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
			Structure structure = structureModule.myStructure(myStructure);
			structure.delApplicationEntityOnly(appEntity);
			result = structure.store();
		} catch (RuntimeException e) {
			log.error("删除对象的关系失败");
			e.printStackTrace();
			result = 0;
		}
		log.info("进入===delEntityByRecursionOnly方法 返回值为："+result); 
		return result;
	}
	
	
	/**
	 * 根据不同类型返回不同关联类型数组
	 * @param infoType 输入类型
	 * @param associatedType 关联关系 
	 * @param myStructure
	 * @return
	 */
	public String[] getAssociatedAetName(String infoType,AssociatedType associatedType,String myStructure){
		log.info("进入===getAssociatedAetName方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(infoType, associatedType);
		log.info("associatedAetNames:"+associatedAetNames);
		log.info("退出===getAssociatedAetName方法 返回值为:"+associatedAetNames);
		return associatedAetNames;
	}
	
	
	
	/**
	 * 保存对象信息
	 * @param entity 需要保存的资源对象
	 * @return int 1为成功 0为失败
	 */
	public int saveInfoEntity(ApplicationEntity entity, String myStructure){
		log.info("进入===saveInfoEntity方法");
		int status = 1;
		try {
			/*Context context = structureModuleLibrary.createContext();
			status=context.insert(entity);	*/
			StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
			Structure structure = structureModule.myStructure(myStructure);
			structure.addApplicationEntityToStructure(entity);
			status = structure.store();
		} catch (Exception e) {
			log.error("保存对象信息失败");
			// TODO: handle exception
			e.printStackTrace();
			status = 0;
		}
		log.info("退出===saveInfoEntity方法 返回值为："+status);
		return status;
	}
	
	/**
	 * 更新对象信息
	 * @param entity 需要更新的资源对象
	 * @return int 1为成功 0为失败
	 */
	public int updateInfoEntity(ApplicationEntity entity){
		log.info("进入===updateInfoEntity方法");
		int status = 1;
		try {
			Context context = structureModuleLibrary.createContext();
			/*Query query = context.createQueryBuilder(entity);
			query.add(Restrictions.eq("id", entity.getValue("id")));*/
			String sqlString = "update "+entity.getType()+" set "+ResourceCommon.getUpdateAttributesSqlString(entity)+" where id = "+entity.getValue("id");
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			status = context.executeUpdateSQL(sc, entity.getType());
			//status=context.update(entity, query);
		} catch (Exception e) {
			log.error("更新对象信息失败");
			// TODO: handle exception
			e.printStackTrace();
			status = 0;
		}
		log.info("退出===updateInfoEntity方法 返回值为："+status);
		return status;
	}
	
	
	/**
	 * 删除对象信息
	 * @param infoName 需要更新的资源对象的类型
	 * @param id 需要删除的资源ID
	 * @return int 1为成功 0为失败
	 */
	
	public int deleteFiberCoreEntity(String infoName,String id){
		log.info("进入===deleteFiberCoreEntity方法");
		Context context = structureModuleLibrary.createContext();
		/*Query query = context.createQueryBuilder(infoName);
		query.add(Restrictions.eq("id", id));*/
		String sqlString ="delete from "+infoName+" where id="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int deleteCount = context.executeDeleteSQL(sc,infoName);
		//int deleteCount = context.delete(query);	
		log.info("退出===deleteFiberCoreEntity方法 返回值为："+deleteCount);
		return deleteCount;
	}
	
	/**
	 * 递归获取子级entity(同一关系)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedType 关系
	 * @param myStructure structure名字
	 * @param index 数组下标
	 * @return
	 */
	private ApplicationEntity[] getAppArrsByRecursionForInsideUse(
			ApplicationEntity[] appArrs, String[] typeArrs, AssociatedType associatedType, String myStructure, int index) {
		log.info("进入===getAppArrsByRecursionForInsideUse方法");
		if(appArrs == null || appArrs.length == 0 
				|| typeArrs == null || typeArrs.length == 0 
				||associatedType == null || myStructure == null || "".equals(myStructure)) {
			return null;
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		if(appArrs != null && appArrs.length > 0) {
			//根据关系，获取相应的entity，放在list中
			List<ApplicationEntity> list = new ArrayList<ApplicationEntity>();
			for(ApplicationEntity app : appArrs) {
				ApplicationEntity[] getAppArrs = getStrutureSelationsApplicationEntity(app, typeArrs[index], associatedType,myStructure);
				if(getAppArrs != null && getAppArrs.length > 0) {
					log.info("开始循环getAppArrs");
					for (ApplicationEntity getApp : getAppArrs) {
						list.add(getApp);
					}
					log.info("结束循环getAppArrs");
				}
			}
			//获取到最后一个类型时，即最后一层的entity，跳出递归
			if(!list.isEmpty()) {
				index++;
				ApplicationEntity[] appResultArrs = new ApplicationEntity[list.size()];
				log.info("开始循环list");
				for(int i = 0; i < list.size(); i++) {
					appResultArrs[i] = list.get(i);
				}
				log.info("结束循环list");
				if(index == typeArrs.length) {
					log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为：null"+appResultArrs);
					return appResultArrs;
				} else {
					ApplicationEntity[] finalAppResultArrs = getAppArrsByRecursionForInsideUse(appResultArrs, typeArrs, associatedType, myStructure, index);
					log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为："+finalAppResultArrs);
					return finalAppResultArrs;
				}
			} else {
				log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为：null");
				return null;
			}
		}
		log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为：null");
		return null;
	}
	
	/**
	 * 递归获取子级entity(不同关系，通过关系数组进行匹配)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedTypeArrs 关系数组
	 * @param myStructure structure名字
	 * @param index 数组下标
	 * @return
	 */
	private ApplicationEntity[] getAppArrsByRecursionForInsideUse(
			ApplicationEntity[] appArrs, String[] typeArrs, AssociatedType[] associatedTypeArrs, String myStructure, int index) {
		log.info("进入===getAppArrsByRecursionForInsideUse方法");
		if(appArrs == null || appArrs.length == 0 
				|| typeArrs == null || typeArrs.length == 0 
				||associatedTypeArrs == null || associatedTypeArrs.length == 0 
				|| typeArrs.length != associatedTypeArrs.length 
				|| myStructure == null || "".equals(myStructure)) {
			return null;
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		if(appArrs != null && appArrs.length > 0) {
			//根据关系，获取相应的entity，放在list中
			List<ApplicationEntity> list = new ArrayList<ApplicationEntity>();
			for(ApplicationEntity app : appArrs) {
				ApplicationEntity[] getAppArrs = getStrutureSelationsApplicationEntity(app, typeArrs[index], associatedTypeArrs[index],myStructure);
				if(getAppArrs != null && getAppArrs.length > 0) {
					log.info("开始循环getAppArrs");
					for (ApplicationEntity getApp : getAppArrs) {
						list.add(getApp);
					}
					log.info("结束循环getAppArrs");
				}
			}
			//获取到最后一个类型时，即最后一层的entity，跳出递归
			if(!list.isEmpty()) {
				index++;
				ApplicationEntity[] appResultArrs = new ApplicationEntity[list.size()];
				log.info("开始循环list");
				for(int i = 0; i < list.size(); i++) {
					appResultArrs[i] = list.get(i);
				}
				log.info("结束循环list");
				if(index == typeArrs.length) {
					log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为："+appResultArrs);
					return appResultArrs;
				} else {
					ApplicationEntity[] finalAppResultArrs = getAppArrsByRecursionForInsideUse(appResultArrs, typeArrs, associatedTypeArrs, myStructure, index);
					log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为："+finalAppResultArrs);
					return finalAppResultArrs;
				}
			} else {
				log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为：null");
				return null;
			}
		}
		log.info("退出===getAppArrsByRecursionForInsideUse方法 返回值为：null");
		return null;
	}
	
	/**
	 * 递归获取子级entity(同一关系)
	 * @param app 根节点entity
	 * @param typeArrs 类型数组
	 * @param associatedType 关系
	 * @param myStructure structure名字
	 * @return
	 */
	public ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity app, String[] typeArrs, AssociatedType associatedType, String myStructure) {
		log.info("进入===getAppArrsByRecursion方法");
		int index  = 0;
		ApplicationEntity[] appArrsByRecursionForInsideUse = getAppArrsByRecursionForInsideUse(new ApplicationEntity[]{app}, typeArrs, associatedType, myStructure, index);
		log.info("退出===getAppArrsByRecursion方法 返回值为："+appArrsByRecursionForInsideUse);
		return appArrsByRecursionForInsideUse;
	}
	
	/**
	 * 递归获取子级entity(同一关系)
	 * @param app 根节点entity
	 * @param typeArrs 类型数组
	 * @param associatedTypes 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity app, String[] typeArrs, AssociatedType[] associatedTypeArrs, String myStructure) {
		log.info("进入===getAppArrsByRecursion方法");
		int index  = 0;
		ApplicationEntity[] appArrsByRecursionForInsideUse = getAppArrsByRecursionForInsideUse(new ApplicationEntity[]{app}, typeArrs, associatedTypeArrs, myStructure, index);
		log.info("退出===getAppArrsByRecursion方法 返回值为："+appArrsByRecursionForInsideUse);
		return appArrsByRecursionForInsideUse;
	}
	
	/**
	 * 递归获取子级entity(同一关系)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedType 关系
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity[] appArrs, String[] typeArrs, AssociatedType associatedType, String myStructure) {
		log.info("进入===getAppArrsByRecursion方法");
		int index  = 0;
		ApplicationEntity[] appArrsByRecursionForInsideUse = getAppArrsByRecursionForInsideUse(appArrs, typeArrs, associatedType, myStructure, index);
		log.info("退出===getAppArrsByRecursion方法 返回值为："+appArrsByRecursionForInsideUse);
		return appArrsByRecursionForInsideUse;
	}
	
	/**
	 * 递归获取子级entity(不同关系)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedTypes 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity[] appArrs, String[] typeArrs, AssociatedType[] associatedTypeArrs, String myStructure) {
		log.info("进入===getAppArrsByRecursion方法");
		int index  = 0;
		ApplicationEntity[] appArrsByRecursionForInsideUse = getAppArrsByRecursionForInsideUse(appArrs, typeArrs, associatedTypeArrs, myStructure, index);
		log.info("退出===getAppArrsByRecursion方法 返回值为："+appArrsByRecursionForInsideUse);
		return appArrsByRecursionForInsideUse;
	}
	
	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursionForSrc(ApplicationEntity app, String type, String myStructure) {
		log.info("进入===getAppArrsByRecursionForSrc方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		long id = 0;
		String entityType = "";
		if(app != null){
			id = Long.parseLong(app.getValue("id")+"");
			entityType = app.getValue("_entityType");
		}
		List<BasicEntity> blist = this.networkResourceManageDao.getResourceByResourceType(id, entityType, type);
		ApplicationEntity[] applicationEntityRecursion = this.typeConversionArrayByBasicEntity(blist);
		//ApplicationEntity[] applicationEntityRecursion = structure.getApplicationEntityRecursion(app, type);
		log.info("退出===getAppArrsByRecursionForSrc方法"+applicationEntityRecursion);
		return applicationEntityRecursion;			
	}
	
	/**
	 * 递归获取指定entity下特定类型的子应用数据对象(相同类型)
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursionForSrcSameType(ApplicationEntity app, String type, String myStructure) {
		log.info("进入===getAppArrsByRecursionForSrcSameType方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		ApplicationEntity[] applicationEntityRecursion = null;
		if(app != null){
//			if(app.getValue("_entityType").equals(type)){
//				ArrayList<ApplicationEntity> arrayList = new ArrayList<ApplicationEntity>();
//					List<ApplicationEntity> resourceRecursion = getResourceRecursion(type, structure,
//							app,arrayList);
//					if(resourceRecursion != null && resourceRecursion.size() > 0){
//						int size =  resourceRecursion.size();
//						applicationEntityRecursion = (ApplicationEntity[])resourceRecursion.toArray(new ApplicationEntity[size]);
//					}
//			}else{
//				applicationEntityRecursion = structure.getApplicationEntityRecursion(app, type);			
//			}
			long id = 0;
			String entityType = "";
			if(app != null){
				id = Long.parseLong(app.getValue("id")+"");
				entityType = app.getValue("_entityType");
			}
			List<BasicEntity> blist = this.networkResourceManageDao.getResourceByResourceType(id, entityType, type);
			applicationEntityRecursion = this.typeConversionArrayByBasicEntity(blist);
			log.info("退出===getAppArrsByRecursionForSrc方法"+applicationEntityRecursion);
		}
		log.info("退出===getAppArrsByRecursionForSrcSameType方法"+applicationEntityRecursion);
		return applicationEntityRecursion;
	}

	private List<ApplicationEntity> getResourceRecursion(String type, Structure structure,
			ApplicationEntity app,ArrayList<ApplicationEntity> arrayList) {
		log.info("进入===getResourceRecursion方法");
		long id = 0;
		String entityType = "";
		if(app != null){
			id = Long.parseLong(app.getValue("id")+"");
			entityType = app.getValue("_entityType");
		}
		List<BasicEntity> blist = this.networkResourceManageDao.getResourceByResourceType(id, entityType, type);
		ApplicationEntity[] recursion = this.typeConversionArrayByBasicEntity(blist);
//		ApplicationEntity[] recursion =  structure.getApplicationEntityRecursion(app, type);
		if(recursion != null && recursion.length > 0){
			log.info("开始循环recursion");
			for(ApplicationEntity hae:recursion){
				arrayList.add(hae);
				getResourceRecursion(type, structure,hae,arrayList);
			}
			log.info("结束循环recursion");
		}
		log.info("退出===getResourceRecursion方法 返回值为："+arrayList);
		return arrayList;
	}
	
	/**
	 * 获取structure下的所有entity的类型
	 * @param myStructure structure名字
	 * @return List<String> 
	 */
	public List<String> getAllEntityTypes(String myStructure) {
		log.info("进入===getAllEntityTypes方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Map<String, StructureModuleEntity> moduleEntityMap = structure.getStructureModule().getModuleEntitys();
		List<String> typeList = new ArrayList<String>();
		log.info("开始循环moduleEntityMap");
		for (String key : moduleEntityMap.keySet()) {
			StructureModuleEntity structureModuleEntity = moduleEntityMap.get(key);
			typeList.add(structureModuleEntity.getAet());
		}
		log.info("结束循环moduleEntityMap");
		log.info("退出===getAllEntityTypes方法 返回值为："+typeList);
		return typeList;
	}
	
	/**
	 * AE set值时进行类型转换
	 * @param entity
	 * @param type
	 * @param key
	 * @param value
	 */
	public void addValueTo(ApplicationEntity entity, String type,
			String key, String value) {
		log.info("进入===addValueTo方法");
		Log log = LogFactory.getLog(ActionHelper.class);
		if ("java.lang.Integer".equals(type)) {
			try {
				int v = Integer.parseInt(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Integer失败。");
			}
		} else if ("java.lang.Long".equals(type)) {
			try {
				long v = Long.parseLong(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Long失败。");
			}
		} else if ("java.lang.Float".equals(type)) {
			try {
				float v = Float.parseFloat(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Float失败。");
			}
		} else if ("java.lang.Boolean".equals(type)) {
			try {
				boolean v = Boolean.parseBoolean(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Double失败。");
			}
		} else if ("java.lang.Double".equals(type)) {
			try {
				double v = Double.parseDouble(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Double失败。");
			}
		} else if ("java.util.Date".equals(type)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:SS");
				Date v = sdf.parse(value);
				////System.out.println(v+"--------");
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.util.Date失败。");
			}
		} else {
			log.debug("添加属性值'" + key + "'='" + value + "'结果:"
					+ entity.setValue(key, value).toString());
		}
		log.info("退出===addValueTo方法");
	}
	
	
	/**
	 * 根据不同的类型获取不同对象查询
	 * @param modelString
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getResVenifyTaskEntityList(String modelString,String id){
		log.info("进入===getResVenifyTaskEntityList方法");
		//Context createContext = structureModuleLibrary.createContext();
		//StructureModule structureModule = structureModuleLibrary.getStructureModule("Pipe&Cable_ReleatedResource");
		//Structure structure = structureModule.myStructure("networkresourcemanage");
		try {
			Context context = structureModuleLibrary.createContext();
			/*Query query = context.createQueryBuilder(modelString);
			query.add(Restrictions.eq("task_id", id));*/
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(modelString)+" from "+modelString+" where task_id="+id;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> list=context.executeSelectSQL(sc, modelString);
			//List<BasicEntity> list=context.queryList(query);
			if(list==null){
				log.info("退出===getResVenifyTaskEntityList方法 返回值为：null");
				return null;
			}else{
				if(list.isEmpty()){
					log.info("退出===getResVenifyTaskEntityList方法 返回值为：null");
					return null;
				}
			}
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			log.info("开始循环list");
			for(BasicEntity b:list){
				listMap.add(b.toMap());
			}
			log.info("结束循环list");
			log.info("退出===getResVenifyTaskEntityList方法 返回值为："+listMap);
			return listMap;
		} catch (Exception e) {
			log.error("获取资源任务失败");
			e.printStackTrace();
			log.info("退出===getResVenifyTaskEntityList方法 返回值为：null");
			return null;
		}
	}
	
	/**
	 * 根据不同的类型获取不同对象查询
	 * @param modelString
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getResVenifyTaskEntityListByStatus(String modelString,String task_status){
		//Context createContext = structureModuleLibrary.createContext();
		//StructureModule structureModule = structureModuleLibrary.getStructureModule("Pipe&Cable_ReleatedResource");
		//Structure structure = structureModule.myStructure("networkresourcemanage");
		log.info("进入===getResVenifyTaskEntityListByStatus方法");
		try {
			Context context = structureModuleLibrary.createContext();
			//Query query = context.createQueryBuilder(modelString);
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(modelString)+" from "+modelString;
			
			if(!"all".equals(task_status)){	
				//query.add(Restrictions.eq("task_status", task_status));
				sqlString += " where task_status='"+task_status+",";
			}
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> list=context.executeSelectSQL(sc, modelString);
			//List<BasicEntity> list=context.queryList(query);
			if(list==null){
				log.info("退出===getResVenifyTaskEntityListByStatus方法 返回值为：null");
				return null;
			}else{
				if(list.isEmpty()){
					log.info("退出===getResVenifyTaskEntityListByStatus方法 返回值为：null");
					return null;
				}
			}
			List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
			log.info("开始循环list");
			for(BasicEntity b:list){
				listMap.add(b.toMap());
			}
			log.info("结束循环list");
			log.info("退出===getResVenifyTaskEntityListByStatus方法 返回值为："+listMap);
			return listMap;
		} catch (Exception e) {
			log.error("获取资源任务失败");
			e.printStackTrace();
			log.info("退出===getResVenifyTaskEntityListByStatus方法 返回值为：null");
			return null;
		}
	}
	
	/**
	 * 递归获取子级entity，并进行关系绑定
	 * @param taskId 任务id
	 * @param user 操作人 
	 * @param aeArrs ae数组
	 * @param associatedType 关联类型
	 * @param myStructure structure名字
	 */
	public void bindTaskAndEntityAssByRecursion(long taskId, String user, ApplicationEntity[] aeArrs, AssociatedType associatedType, String myStructure) {
		log.info("进入===bindTaskAndEntityAssByRecursion方法");
		if(aeArrs == null || aeArrs.length == 0) {
			log.info("退出===bindTaskAndEntityAssByRecursion方法");
			return;
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		//保存父资源查询下的所有子资源
		List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
		log.info("开始循环aeArrs");
		for(ApplicationEntity ae : aeArrs) {
			//查询当前资源的子资源
			ApplicationEntity[] searchAssAppArrs = structure.getAssociatedEntity(ae, associatedType);
			if(searchAssAppArrs != null && searchAssAppArrs.length > 0) {
				log.info("开始循环searchAssAppArrs");
				for(ApplicationEntity searchAe : searchAssAppArrs) {
					if(ae != null && searchAe != null) {
						//父节点id和type
						long parentEntityId = Long.parseLong(ae.getValue("id").toString());
						String parentEntityType = ae.getType();
						//关联节点的id和type
						long currentEntityId = Long.parseLong(searchAe.getValue("id").toString());
						String currentEntityType = searchAe.getType();
						
						ApplicationEntity taskAeRelApp = moduleLibrary.getModule("Task_AE_rel").createApplicationEntity();
						taskAeRelApp.setValue("id", Unique.nextLong("task_ae_rel_id"));
						taskAeRelApp.setValue("task_id", taskId);
						taskAeRelApp.setValue("a_ae_id", parentEntityId);
						taskAeRelApp.setValue("a_ae_type", parentEntityType);
						taskAeRelApp.setValue("z_ae_id", currentEntityId);
						taskAeRelApp.setValue("z_ae_type", currentEntityType);
						if(associatedType == AssociatedType.CHILD) {
							taskAeRelApp.setValue("rel_type", "child");
						} else if(associatedType == AssociatedType.LINK) {
							taskAeRelApp.setValue("rel_type", "link");
						}
						taskAeRelApp.setValue("user", user);
						//添加entity之间的关系
						addTaskAeRelAss(taskAeRelApp);
					}
					aeList.add(searchAe);
				}
				log.info("结束循环searchAssAppArrs");
			}
		}
		log.info("结束循环aeArrs");
		if(aeList.isEmpty()) {
			log.info("退出===bindTaskAndEntityAssByRecursion方法");
			return;
		}
		ApplicationEntity[] searchAllAeArrs = new ApplicationEntity[aeList.size()];
		log.info("开始循环searchAllAeArrs");
		for(int i = 0; i < searchAllAeArrs.length; i++) {
			searchAllAeArrs[i] = aeList.get(i);
		}
		log.info("结束循环searchAllAeArrs");
		//递归继续查找子资源，进行关系绑定
		bindTaskAndEntityAssByRecursion(taskId, user, searchAllAeArrs, associatedType, myStructure);
	}
	
	/**
	 * 递归获取子级entity，并进行关系绑定
	 * @param taskId 任务id
	 * @param user 操作人
	 * @param ae ae实例
	 * @param associatedType 关联类型
	 * @param myStructure structure名字
	 */
	public void bindTaskAndEntityAssByRecursion(long taskId, String user, ApplicationEntity ae, AssociatedType associatedType, String myStructure) {
		log.info("进入===bindTaskAndEntityAssByRecursion方法");
		bindTaskAndEntityAssByRecursion(taskId, user, new ApplicationEntity[]{ae}, associatedType, myStructure);
		log.info("退出 ===bindTaskAndEntityAssByRecursion方法");
	}
	
	
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMapToMobile(ApplicationEntity entity,AssociatedType associatedType,String addLink, String myStructure){
		log.info("进入===getStrutureSelationsTypeMapToMobile方法");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] associatedAetNames = structure.getAssociatedAetName(entity, associatedType);
		if(associatedAetNames != null){
			log.info("开始循环associatedAetNames");
			for(String associatedAetName:associatedAetNames){
				Map<String, Object> map = new HashMap<String, Object>();
				ApplicationEntity[] associatedEntity = getStrutureSelationsApplicationEntity(entity, associatedAetName, associatedType, myStructure);
//				ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(entity,associatedAetName, associatedType);
				int count = 0;
				if(associatedEntity != null){
					count = associatedEntity.length;
					//list = new ArrayList<ApplicationEntity>(Arrays.asList(associatedEntity));				
				}
				
				//需要获取link资源的情况(求出child和link的数量总和)
				if(addLink != null) {
					if(associatedType == AssociatedType.CHILD) {
						AssociatedType assType = AssociatedType.LINK;
						ApplicationEntity[] assLinkEntity = getStrutureSelationsApplicationEntity(entity, associatedAetName, assType, myStructure);
						//ApplicationEntity[] assLinkEntity = structure.getAssociatedEntity(entity,associatedAetName, assType);
						if(assLinkEntity != null) {
							count += assLinkEntity.length;
						}
					} else if(associatedType == AssociatedType.LINK) {
						AssociatedType assType = AssociatedType.CHILD;
						ApplicationEntity[] assLinkEntity = getStrutureSelationsApplicationEntity(entity, associatedAetName, assType, myStructure);
//						ApplicationEntity[] assLinkEntity = structure.getAssociatedEntity(entity,associatedAetName, assType);
						if(assLinkEntity != null) {
							count += assLinkEntity.length;
						}
					}
				}
				map.put("type", associatedAetName);
				map.put("count", count);
				if(associatedType == AssociatedType.CHILD) {
					map.put("assType", "child");
				} else if(associatedType == AssociatedType.LINK) {
					map.put("assType", "link");
				}
				list.add(map);
			}
			log.info("结束循环associatedAetNames");
		}
		log.info("退出===getStrutureSelationsTypeMapToMobile方法"+list);
		return list;
		
	}
	
	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String,Object>> getStrutureSelationsTypeMapToMobile(ApplicationEntity entity,AssociatedType associatedType, String myStructure) {
		log.info("进入===getStrutureSelationsTypeMapToMobile方法");
		List<Map<String,Object>> strutureSelationsTypeMapToMobile = getStrutureSelationsTypeMapToMobile(entity, associatedType, null, myStructure);
		log.info("退出===getStrutureSelationsTypeMapToMobile方法 返回值为："+strutureSelationsTypeMapToMobile);
		return strutureSelationsTypeMapToMobile;
	}
	
	/**
	 * 根据entityGroupName递归获取子级entity(不同关系)
	 * @param app 根节点entity
	 * @param entityGroupName entityGroup名字
	 * @param associatedType 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByEntityGroup(
			ApplicationEntity app, String entityGroupName, AssociatedType[] associatedTypeArrs, String myStructure){
		log.info("进入===getAppArrsByEntityGroup方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityGroupNames = structure.getAetNameOfAetg(entityGroupName);
		ApplicationEntity[] appArrsByRecursion = getAppArrsByRecursion(app, entityGroupNames, associatedTypeArrs, myStructure);
		log.info("进入===getAppArrsByEntityGroup方法 返回值为："+appArrsByRecursion);
		return appArrsByRecursion;
	}
	
	
	/**
	 * 对象检索，支持指定类型查询、不指定类型基于名称模糊查询
	 * @param entityType
	 * @param map
	 * @param myStructure
	 * @param isEntityGroup
	 * @return
	 */
	private List<List<ApplicationEntity>> getEntityList(String entityType,Map<String,Object> map, String myStructure,boolean isEntityGroup){
		log.info("进入===getEntityList方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Context context = structure.createContext();
		String[] entityTypes = null;
		if(isEntityGroup){
			
			entityTypes = structure.getAetNameOfAetg(entityType);
		}else{
			if(entityType == null){
				
				List<String> allEntityNameList = getAllEntityTypes(myStructure);
				if(allEntityNameList != null && !allEntityNameList.isEmpty()){
					entityTypes = new String[allEntityNameList.size()];
					log.info("开始循环entityTypes");
					for(int i = 0; i < entityTypes.length; i++) {
						entityTypes[i] = allEntityNameList.get(i);
					}
					log.info("结束循环entityTypes");	
				}
		
			}else{
				entityTypes = new String[]{entityType};
			}
		}

		//Query query = null;
		List<List<ApplicationEntity>> list = new ArrayList<List<ApplicationEntity>>();
		log.info("开始循环entityTypes");
		for(String type:entityTypes){
			//query = context.createQueryBuilder(type);
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+ type;
			//System.out.println("sqlString:"+sqlString);
			if(map != null && map.size() > 0) {
				log.info("开始循环map");
				StringBuffer str = new StringBuffer();
				int index=0;
				for(String key : map.keySet()) {
					String value = "";
					if(map.get(key)!=null){
						value = map.get(key).toString().trim();
					}
					if(index==0){
						str.append("instr("+key+",'"+value+"')>0");
					}else{
						str.append(" and instr("+key+",'"+value+"')>0");
					}
					
					//query.add(Restrictions.like(key, map.get(key)));
				}
				sqlString +=" where "+str;
				log.info("开始循环map");
			}
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> queryList = context.executeSelectSQL(sc, type);
			//List<BasicEntity> queryList = context.queryList(query);
			List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
			if(queryList != null){
				log.info("开始循环queryList");
				for(BasicEntity be:queryList){
					aeList.add(ApplicationEntity.changeFromEntity(be));
				}
				log.info("开始循环queryList");
				list.add(aeList);
			}
		}
		log.info("结束循环entityTypes");	
		log.info("退出===getEntityList方法 返回值为："+list);
		return list;

	}
	
	/**
	 * 对象检索,不指定类型或指定类型
	 * @param entityType 资源类型
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>> 
	 */
	public List<List<ApplicationEntity>> getEntityListByEntity(String entityType,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByEntity方法");
		List<List<ApplicationEntity>> entityList = getEntityList(entityType,map,myStructure,false);
		log.info("退出===getEntityListByEntity方法 返回值为："+entityList);
		return entityList;
	}
	
	/**
	 * 对象检索,指定EntityGroup
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListByAetg(String AetgName,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByAetg方法");
		List<List<ApplicationEntity>> entityList = getEntityList(AetgName,map,myStructure,true);
		log.info("退出===getEntityListByAetg方法 返回值为："+entityList);
		return entityList;
	}
	
	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListByEntityTypeRoAetg(String AetgName,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByEntityTypeRoAetg方法");
		List<List<ApplicationEntity>> entityList = new ArrayList<List<ApplicationEntity>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityTypes = structure.getAetNameOfAetg(AetgName);
		if(entityTypes.length == 0){
			entityList = getEntityList(AetgName,map,myStructure,false);
		}else{
			entityList = getEntityList(AetgName,map,myStructure,true);
		}
		log.info("退出===getEntityListByEntityTypeRoAetg方法 返回值为："+entityList);
		return entityList;
	}
	
	/**
	 * 根据坐标，指定半径查询指定查询条件指定资源类型数据
	 * @param AetgName Entity类型
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public List<ApplicationEntity> getEntityListByGPSDistance(String AetgName,double distance,double longitude, double latitude,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByGPSDistance方法");
		Map<String,Object> areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRound(latitude, longitude, distance);
		List<List<ApplicationEntity>> entityList = getEntityListByGPS(AetgName,map,myStructure,false,areaLeftRightTopBottom);
		List<ApplicationEntity> results = new ArrayList<ApplicationEntity>();
		if(entityList != null){
			List<ApplicationEntity> resultList = new ArrayList<ApplicationEntity>();
			log.info("开始循环entityList");
			for(List<ApplicationEntity> list:entityList){	
				resultList = list;
				log.info("开始循环resultList");
				for (ApplicationEntity be : resultList) {
							results.add(be);
				}
				log.info("结束循环resultList");
			}
			log.info("结束循环entityList");
		}
		log.info("退出===getEntityListByGPSDistance方法 返回值为："+results);
		return results;
	}
	
	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据
	 * @param AetgName 资源组类型(可以不指定)
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public List<ApplicationEntity> getEntityListByAetgAndGPSDistance(String AetgName,double distance,double longitude, double latitude,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByAetgAndGPSDistance方法");
		Map<String,Object> areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRound(latitude, longitude, distance);
		List<List<ApplicationEntity>> entityList = getEntityListGPSByEntityTypeRoAetg(AetgName,map,areaLeftRightTopBottom,myStructure);
		List<ApplicationEntity> results = new ArrayList<ApplicationEntity>();
		if(entityList != null){
			List<ApplicationEntity> resultList = new ArrayList<ApplicationEntity>();
			log.info("开始循环entityList");
			for(List<ApplicationEntity> list:entityList){	
				resultList = list;
				log.info("开始循环resultList");
				for (ApplicationEntity be : resultList) {
							results.add(be);
				}
				log.info("结束循环resultList");
			}
			log.info("结束循环entityList");
		}
		log.info("退出===getEntityListByAetgAndGPSDistance方法 返回值为："+results);
		return results;
	}
	
	
	public boolean isChildTypeOfEntity(ApplicationEntity entity,String type, String myStructure){
		log.info("进入===isChildTypeOfEntity方法");
		boolean result = false;
		String[] strutureSelationsArray = getStrutureSelationsArray(entity, AssociatedType.CHILD, myStructure);
		log.info("开始循环strutureSelationsArray");
		for(String str:strutureSelationsArray){
			if(str.equals(type)){
				result = true;
			}
		}
		log.info("结束循环strutureSelationsArray");
		log.info("退出===isChildTypeOfEntity方法 返回值为："+result);
		return result;
	}
	
	public boolean isLinkTypeOfEntity(ApplicationEntity entity,String type, String myStructure){
		log.info("进入===isLinkTypeOfEntity方法");
		boolean result = false;
		String[] strutureSelationsArray = getStrutureSelationsArray(entity, AssociatedType.LINK, myStructure);
		log.info("开始循环strutureSelationsArray");
		for(String str:strutureSelationsArray){
			if(str.equals(type)){
				result = true;
			}
		}
		log.info("结束循环strutureSelationsArray");
		log.info("退出===isLinkTypeOfEntity方法 返回值为："+result);
		return result;
	}
	
	
	/**
	 * 增加一个任务和多个资源的绑定(绑定任务和资源的同时，记录多个资源之间的关系)
	 * @param entity
	 * @return
	 */
	public int addTaskAeRelAss(ApplicationEntity entity) {
		log.info("进入===addTaskAeRelAss方法");
		if(entity == null) {
			return 0;
		}
		Context context = structureModuleLibrary.createContext();
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(entity);
		String sqlString = "insert into  "+entity.getType()+"("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int insert = context.executeInsertSQL(sc, entity.getType());
		//int insert = context.insert(entity);
		log.info("退出===addTaskAeRelAss方法 返回值为："+insert);
		return insert;
	}
	
	/**
	 * 根据类型和label，查询一个物理资源
	 * @param type
	 * @param label
	 * @return
	 */
	public BasicEntity getPhysicalresByLabel(String type, String label) {
		log.info("进入===getPhysicalresByLabel方法");
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(type);
		//query.add(Restrictions.eq("label", label));

		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+ type +" where label='"+label+"'";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> queryList = context.executeSelectSQL(sc, type);
		BasicEntity queryEntity =null;
		if(queryList!=null){
			queryEntity = queryList.get(0);
		}

		//BasicEntity queryEntity = context.queryEntity(query);
		log.info("退出===getPhysicalresByLabel方法 返回值为："+queryEntity);
		return queryEntity;
	}
	
	/**
	 * 对象检索，支持指定类型查询、不指定类型基于名称模糊查询(GPS圆环)
	 * @param entityType
	 * @param map
	 * @param myStructure
	 * @param isEntityGroup
	 * @param mapByGPS
	 * @param areaIdsList
	 * @return
	 */
	private List<List<ApplicationEntity>> getEntityListRingByGPS(String entityType,Map<String,Object> map, String myStructure,boolean isEntityGroup,Map<String,Object> mapByGPS,List<String> areaIdsList){
		log.info("进入===getEntityListRingByGPS方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Context context = structure.createContext();
		String[] entityTypes = null;
		if(isEntityGroup){
			
			entityTypes = structure.getAetNameOfAetg(entityType);
		}else{
			if(entityType == null){
				
				List<String> allEntityNameList = getAllEntityTypes(myStructure);
				if(allEntityNameList != null && !allEntityNameList.isEmpty()){
					entityTypes = new String[allEntityNameList.size()];
					log.info("开始循环entityTypes");
					for(int i = 0; i < entityTypes.length; i++) {
						entityTypes[i] = allEntityNameList.get(i);
					}
					log.info("结束循环entityTypes");
					
				}
		
			}else{
				entityTypes = new String[]{entityType};
			}
		}
		StringBuffer sf = new StringBuffer();
		if(areaIdsList!=null && areaIdsList.size()>0){
			for(String areaId:areaIdsList){
				sf.append(","+areaId);
			}
		}
		//Query query = null;
		List<List<ApplicationEntity>> list = new ArrayList<List<ApplicationEntity>>();
		log.info("开始循环entityTypes");
		for(String type:entityTypes){
			//query = context.createQueryBuilder(type);
			List<Criterion> listCriterion = new ArrayList<Criterion>();
			String hql = "";
			if(map != null && map.size() > 0) {
				hql = " and ( ";
				log.info("开始循环map");
				for(String key : map.keySet()) {
					//hql = hql + key + "instr('"+key+"','"+map.get(key)+"')>0 like '"+map.get(key)+"' or ";
					hql = hql + key + " like '"+map.get(key)+"' or ";
					//Criterion like = Restrictions.like(key, map.get(key));
				}
				if(hql != null && hql != ""){
					hql = hql.substring(0,hql.length() - 3);
				}
				hql = hql + ")";
				log.info("结束循环map");
			}
			String sql = "";
			String bsString ="bs.longitude \"longitude\",bs.latitude \"latitude\" ";
			if(type.equals("Room")){
				sql = "select "+bsString+","+ResourceCommon.getSelectSqlAttributsString("Room","r")+" from room  r,v_station_room  bs,v_room v where r.id = bs.roomId and v.roomId=bs.roomId";
				
			}else{
				if(type.indexOf("BaseStation")>=0){
					sql = "select "+ResourceCommon.getSelectSqlAttributsString(type,"bs")+" from v_baseStation  v,"+type+" bs where bs.id=v.baseStationId";
				}else{
					sql = "select "+ResourceCommon.getSelectSqlAttributsString(type,"bs")+" from v_"+ type +"  v,"+type+" bs where bs.id=v."+type+"Id";
				}
				
			}
			if(!"".equals(sf+"")){
				sql +=" and  v.areaId in ("+sf.substring(1)+")";
			}
			if(mapByGPS != null && mapByGPS.size() > 0){
			/*	if(type.equals("Room")){
					sql = sql + " and ";
				}else{
					sql = sql + " where ";
				}*/
//				Criterion between = Restrictions.between("longitude", mapByGPS.get("outerleft"), mapByGPS.get("outerright"));
//				Criterion between2 = Restrictions.between("latitude", mapByGPS.get("outerbottom"), mapByGPS.get("outertop"));
//				Criterion lator = Restrictions.or(Restrictions.gt("latitude", mapByGPS.get("innertop")),Restrictions.lt("latitude", mapByGPS.get("innerbottom")));
//				Criterion longor = Restrictions.or(Restrictions.gt("longitude", mapByGPS.get("innerright")),Restrictions.lt("longitude", mapByGPS.get("innerleft")));
//				Criterion and = Restrictions.and(between, between2);
//				Criterion or = Restrictions.or(lator, longor);
//				query.add(Restrictions.and(and, or));
				
				sql = sql + " and ((bs.longitude between "+mapByGPS.get("outerleft")+" and "+mapByGPS.get("outerright")+" ) "
							+" and (bs.latitude between "+mapByGPS.get("outerbottom")+" and "+mapByGPS.get("outertop")+" )) "
							+" and (((bs.latitude>"+mapByGPS.get("innertop")+") or (bs.latitude<"+mapByGPS.get("innerbottom")+")) or ((bs.longitude>"+mapByGPS.get("innerright")+") or (bs.longitude<"+mapByGPS.get("innerleft")+"))) ";
			}
			//select * from BaseStation where ((longitude between 113.29945052377133 and 113.33534947622869 ) 
			//and (latitude between 23.063363567881627 and 23.099336432118374 )) 
			//and (((latitude>"23.08135") or (latitude<"23.08135")) or ((longitude>"113.3174") or (longitude<"113.3174")))
//			String sql = query.getQuerySqlString();
			sql = sql + hql;
			
			//sql = "SELECT bs.*,r.* FROM room AS r,associate_station_room AS bs WHERE r.id = bs.roomId";
			////System.out.println(sql);
			List<BasicEntity> queryList = null;
			List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
			try {
				//System.out.println("sql:"+sql);
				SqlContainer createSqlContainer = context.createSqlContainer(sql);
				if(type.equals("Room")){
					queryList = context.executeSelectSQL(createSqlContainer,"Room");
					if(queryList != null){
						for(BasicEntity be:queryList){
							aeList.add(ApplicationEntity.changeFromEntityForAll(be));
						}
						list.add(aeList);
					}
				}else{
					queryList = context.executeSelectSQL(createSqlContainer,type);
					if(queryList != null){
						for(BasicEntity be:queryList){
							aeList.add(ApplicationEntity.changeFromEntity(be));
						}
						list.add(aeList);
					}
				}
				//queryList = context.queryList(query);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("获取数据出错");
			}
			
			
		}
		log.info("结束循环entityTypes");
		log.info("退出===getEntityListRingByGPS方法 返回值为："+list);
		return list;

	}
	
	/**
	 * 对象检索，支持指定类型查询、不指定类型基于名称模糊查询(GPS)
	 * @param entityType
	 * @param map
	 * @param myStructure
	 * @param isEntityGroup
	 * @param mapByGPS
	 * @return
	 */
	private List<List<ApplicationEntity>> getEntityListByGPS(String entityType,Map<String,Object> map, String myStructure,boolean isEntityGroup,Map<String,Object> mapByGPS){
		log.info("进入===getEntityListByGPS方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Context context = structure.createContext();
		String[] entityTypes = null;
		if(isEntityGroup){
			
			entityTypes = structure.getAetNameOfAetg(entityType);
		}else{
			if(entityType == null){
				
				List<String> allEntityNameList = getAllEntityTypes(myStructure);
				if(allEntityNameList != null && !allEntityNameList.isEmpty()){
					entityTypes = new String[allEntityNameList.size()];
					log.info("开始循环entityTypes");
					for(int i = 0; i < entityTypes.length; i++) {
						entityTypes[i] = allEntityNameList.get(i);
					}
					log.info("结束循环entityTypes");
					
				}
		
			}else{
				entityTypes = new String[]{entityType};
			}
		}

	//	Query query = null;
		List<List<ApplicationEntity>> list = new ArrayList<List<ApplicationEntity>>();
		log.info("开始循环entityTypes");
		for(String type:entityTypes){
			//query = context.createQueryBuilder(type);
			StringBuffer conStr = new StringBuffer();
			if(map != null && map.size() > 0) {
				log.info("开始循环map");
				for(String key : map.keySet()) {
					//query.add(Restrictions.like(key, map.get(key)));
					if(!"".equals(conStr+"")){
						conStr.append(" and "+key+"='"+map.get(key)+"'");
					}else{
						conStr.append(key+"='"+map.get(key)+"'");
					}
				}
				log.info("结束循环map");
			}
			if(mapByGPS != null && mapByGPS.size() > 0){
					//query.add(Restrictions.between("longitude", mapByGPS.get("left"), mapByGPS.get("right")));
					//query.add(Restrictions.between("latitude", mapByGPS.get("bottom"), mapByGPS.get("top")));
					if(!"".equals(conStr+"")){
						conStr.append(" and  (longitude between "+mapByGPS.get("left")+" and "+mapByGPS.get("right")+") and (latitude between "+mapByGPS.get("bottom")+" and "+mapByGPS.get("top")+")");
					}else{
						conStr.append(" longitude between "+mapByGPS.get("left")+" and "+mapByGPS.get("right")+" and (latitude between "+mapByGPS.get("bottom")+" and "+mapByGPS.get("top")+")");
					}
			}
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type +" where "+conStr;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> queryList = context.executeSelectSQL(sc, type);
			//List<BasicEntity> queryList = context.queryList(query);
			List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
			if(queryList != null){
				log.info("开始循环queryList");
				for(BasicEntity be:queryList){
					aeList.add(ApplicationEntity.changeFromEntity(be));
				}
				log.info("结束循环queryList");
				list.add(aeList);
			}
		}
		log.info("结束循环entityTypes");
		log.info("退出===getEntityListByGPS方法 返回值为："+list);
		return list;

	}
	
	
	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(GPS)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param mapByGPS
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListGPSByEntityTypeRoAetg(String AetgName,Map<String,Object> map,Map<String,Object> mapByGP, String myStructure){
		log.info("进入===getEntityListGPSByEntityTypeRoAetg方法");
		List<List<ApplicationEntity>> entityList = new ArrayList<List<ApplicationEntity>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityTypes = structure.getAetNameOfAetg(AetgName);
		if(entityTypes.length == 0){
			entityList = getEntityListByGPS(AetgName,map,myStructure,false,mapByGP);
		}else{
			entityList = getEntityListByGPS(AetgName,map,myStructure,true,mapByGP);
		}
		log.info("退出===getEntityListGPSByEntityTypeRoAetg方法 返回值为："+entityList);
		return entityList;
	}
	
	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(GPS圆环)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param mapByGPS
	 * @param areaIdsList 权限区域id
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListGPSRingByEntityTypeRoAetg(String AetgName,Map<String,Object> map,Map<String,Object> mapByGP,List<String> areaIdsList, String myStructure){
		log.info("进入===getEntityListGPSRingByEntityTypeRoAetg方法");
		List<List<ApplicationEntity>> entityList = new ArrayList<List<ApplicationEntity>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityTypes = structure.getAetNameOfAetg(AetgName);
		if(entityTypes.length == 0){
			entityList = getEntityListRingByGPS(AetgName,map,myStructure,false,mapByGP,areaIdsList);
		}else{
			entityList = getEntityListRingByGPS(AetgName,map,myStructure,true,mapByGP,areaIdsList);
		}
		log.info("退出===getEntityListGPSRingByEntityTypeRoAetg方法 返回值为:"+entityList);
		return entityList;
	}
	

	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(分页)
	 * @param AetgName 资源组类型(可以不指定)
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param start 开始下标
	 * @param end 结束下标
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public List<ApplicationEntity> getEntityListByAetgAndGPSDistanceAndPaging(String AetgName,double distance,double longitude, double latitude,Map<String,Object> map,int start,int end, String myStructure){
		log.info("进入===getEntityListByAetgAndGPSDistanceAndPaging方法");
		List<ApplicationEntity> entityListByAetgAndGPSDistance = getEntityListByAetgAndGPSDistance(AetgName, distance, longitude, latitude, map, myStructure);
		List<ApplicationEntity> list = null;
		if(entityListByAetgAndGPSDistance != null && entityListByAetgAndGPSDistance.size() != 0){
			list =  new ArrayList<ApplicationEntity>();
			int lsitSize = entityListByAetgAndGPSDistance.size();
			if(end >= lsitSize){
				end = lsitSize;
			}
			for(;start <= end;start++){
				list.add(entityListByAetgAndGPSDistance.get(start));
			}
		}
		log.info("退出===getEntityListByAetgAndGPSDistanceAndPaging方法 返回值为:"+list);
		return list;
	}
	
	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)
	 * @param AetgName 资源组类型(可以不指定)
	 * @param innerDistance 内半径
	 * @param outerDistance 外半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param areaIdsList 权限区域id
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public List<ApplicationEntity> getEntityListRingByAetgAndGPSDistance(String AetgName,double innerDistance,double outerDistance,double longitude, double latitude,Map<String,Object> map,List<String> areaIdsList, String myStructure){
		log.info("进入===getEntityListRingByAetgAndGPSDistance方法");
		Map areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRing(latitude, longitude, innerDistance, outerDistance);
		List<List<ApplicationEntity>> entityList = getEntityListGPSRingByEntityTypeRoAetg(AetgName,map,areaLeftRightTopBottom,areaIdsList,myStructure);
		List<ApplicationEntity> results = new ArrayList<ApplicationEntity>();
		if(entityList != null && entityList.size() != 0){
			List<ApplicationEntity> resultList = new ArrayList<ApplicationEntity>();
			log.info("开始循环entityList");
			for(List<ApplicationEntity> list:entityList){	
				resultList = list;
				log.info("开始循环resultList");
				for (ApplicationEntity be : resultList) {
							results.add(be);
				}
				log.info("结束循环resultList");
			}
			log.info("结束循环entityList");
		}
		log.info("退出===getEntityListRingByAetgAndGPSDistance方法 返回值为："+results);
		return results;
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
	 * @param areaIdsList 权限区域id list
	 * @param myStructure structure名字
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getEntityListRingByAetgAndGPSDistanceAndPaging(String AetgName,double innerDistance,double outerDistance,double longitude, double latitude,Map<String,Object> map,int start,int end,List<String> areaIdsList, String myStructure){
		log.info("进入===getEntityListRingByAetgAndGPSDistanceAndPaging方法");
		List<ApplicationEntity> entityListByAetgAndGPSDistance = getEntityListRingByAetgAndGPSDistance(AetgName, innerDistance, outerDistance, longitude, latitude, map,areaIdsList,myStructure);
		List<Map<String,Object>> list = null;
		List<Map<String,Object>> listMap = null;
		if(entityListByAetgAndGPSDistance != null && entityListByAetgAndGPSDistance.size() != 0){
			list =  new ArrayList<Map<String,Object>>();
			listMap = sortToList(entityListByAetgAndGPSDistance,latitude,longitude);
			int lsitSize = listMap.size();
			if(end >= lsitSize){
				end = lsitSize -1;
			}
			log.info("开始循环start");
			for(;start <= end;start++){
				if(AetgName != null && AetgName.equals("Room")){
					listMap.get(start).put("_entityType", "Room");
				}
				listMap.get(start).put("totalCount", lsitSize);
				list.add(listMap.get(start));
			}
			log.info("结束循环start");
		}
		log.info("退出===getEntityListRingByAetgAndGPSDistanceAndPaging方法 返回值为："+list);
		return list;
	}
	
	private List<Map<String,Object>> sortToList(List<ApplicationEntity> list,double latitude,double longitude){
		////System.out.println("list size = "+list.size());
		log.info("进入===sortToList方法");
		List<Double> listDouble = new ArrayList<Double>();
		List<Map<String,Object>> forList = null; 
		List<Map<String,Object>> reList = null;
		if(list != null && list.size() != 0){		
		reList = new ArrayList<Map<String,Object>>();
		forList = new ArrayList<Map<String,Object>>();
		log.info("开始循环list");
		for(ApplicationEntity ae:list){
//			////System.out.println("-- " +ae.getValue("name").toString());
			////System.out.println(ae.getValue("name"));
			if(ae.getValue("longitude") != null && ae.getValue("longitude") != "" && ae.getValue("latitude") != null && ae.getValue("latitude") != "" ){
				String resultlongitudeString1 = ae.getValue("longitude").toString();
				double longitude1 = Double.valueOf(resultlongitudeString1);
				String resultlatitudeString1 = ae.getValue("latitude").toString();
				double latitude1 = Double.valueOf(resultlatitudeString1);
				double distance = getDistance(latitude1, longitude1, latitude, longitude);
				Map<String,Object> map = ae.toMap();
				map.put("GPSsum", distance);
				String chinese = getChinese(map.get("_entityType")+"");
				map.put("chineseType", chinese);
//			if(!"Station".equals(map.get("_entityType")+"")){
				forList.add(map);
//			}
			}
		}
		log.info("结束循环list");
		Collections.sort(forList, new CompareOriginal(longitude,latitude));
		
	}
		log.info("退出===sortToList方法 返回值为:"+forList);
		return forList;
	}
	
	public static double getDistance(double lat1, double lng1, double lat2, 

            double lng2) { 
		log.info("进入===getDistance方法");
        double radLat1 = rad(lat1); 

        double radLat2 = rad(lat2); 

        double a = radLat1 - radLat2; 

        double b = rad(lng1) - rad(lng2); 

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) 

                + Math.cos(radLat1) * Math.cos(radLat2) 

                * Math.pow(Math.sin(b / 2), 2))); 

        s = s * EARTH_RADIUS; 
        s = (int)(s * 1000);
        log.info("退出===getDistance方法"+s);
        return s; 

    }
	
	 private static double EARTH_RADIUS = 6378.137; 

	 

	    private static double rad(double d) { 
	    	log.info("进入===rad方法");
	    	double returnDouble = d * Math.PI / 180.0;
	    	log.info("退出===rad方法");
	        return returnDouble; 
	    } 
	    
	private String getChinese(String entityText){
		log.info("进入===getChinese方法");
		Map<String, String> map = new HashMap<String, String>();
		map.put("ManWell", "人井");
		map.put("MarkPost", "标桩");
		map.put("FiberCrossCabinet", "光交接箱");
		map.put("FiberDistributionCabinet", "光分纤箱");
		map.put("FiberTerminalCase", "终端盒");
		map.put("BaseStation", "基站");
		map.put("Station", "站址");
		map.put("BaseStation_GSM", "GSM基站");
		map.put("BaseStation_repeater", "直放站");
		map.put("BaseStation_TD", "TD基站");
		map.put("BaseStation_WLAN", "WLAN基站");
		map.put("Pole", "电杆");
		map.put("HangWall", "挂墙点");
		String string = map.get(entityText);
		log.info("退出===getChinese方法 返回值为:"+string);
		return string;
	}
	
	
	/**
	 * 对象检索，支持指定类型查询、不指定类型基于名称精确
	 * @param entityType
	 * @param map
	 * @param myStructure
	 * @param isEntityGroup
	 * @return
	 */
	private List<List<ApplicationEntity>> getEntityListAccurate(String entityType,Map<String,Object> map, String myStructure,boolean isEntityGroup){
		log.info("进入===getEntityListAccurate方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Context context = structure.createContext();
		String[] entityTypes = null;
		if(isEntityGroup){
			
			entityTypes = structure.getAetNameOfAetg(entityType);
		}else{
			if(entityType == null){
				
				List<String> allEntityNameList = getAllEntityTypes(myStructure);
				if(allEntityNameList != null && !allEntityNameList.isEmpty()){
					entityTypes = new String[allEntityNameList.size()];
					log.info("开始循环entityTypes");
					for(int i = 0; i < entityTypes.length; i++) {
						entityTypes[i] = allEntityNameList.get(i);
					}
					log.info("结束循环entityTypes");
					
				}
		
			}else{
				entityTypes = new String[]{entityType};
			}
		}

		//Query query = null;
		List<List<ApplicationEntity>> list = new ArrayList<List<ApplicationEntity>>();
		log.info("开始循环entityTypes");
		for(String type:entityTypes){
			//query = context.createQueryBuilder(type);
			StringBuffer conStr = new StringBuffer();
			if(map != null && map.size() > 0) {
				for(String key : map.keySet()) {
					if(!"".equals(conStr+"")){
						conStr.append(" and "+key+"='"+map.get(key)+"'");
					}else{
						conStr.append(key+"='"+map.get(key)+"'");
					}
				}
			}
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type +" where "+conStr;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> queryList = context.executeSelectSQL(sc, type);
			//List<BasicEntity> queryList = context.queryList(query);
			List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
			if(queryList != null){
				log.info("开始循环queryList");
				for(BasicEntity be:queryList){
					aeList.add(ApplicationEntity.changeFromEntity(be));
				}
				log.info("结束循环queryList");
				list.add(aeList);
			}
		}
		log.info("结束循环entityTypes");
		log.info("退出===getEntityListAccurate方法 返回值为："+list);
		return list;

	}
	
	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(精确)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListByEntityTypeRoAetgAccurate(String AetgName,Map<String,Object> map, String myStructure){
		log.info("进入===getEntityListByEntityTypeRoAetgAccurate方法");
		List<List<ApplicationEntity>> entityList = new ArrayList<List<ApplicationEntity>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityTypes = structure.getAetNameOfAetg(AetgName);
		if(entityTypes.length == 0){
			entityList = getEntityListAccurate(AetgName,map,myStructure,false);
		}else{
			entityList = getEntityListAccurate(AetgName,map,myStructure,true);
		}
		log.info("退出===getEntityListByEntityTypeRoAetgAccurate方法 返回值为:"+entityList);
		return entityList;
	}
	
	/**
	 * 获取主键
	 * @param EntityType entity类型
	 * @return long
	 */
	public long getEntityPrimaryKey(String EntityType){
		log.info("进入===getEntityPrimaryKey方法");
		long retId = 0;
		Context context = structureModuleLibrary.createContext();
		String uuIdSql = "select "+ResourceCommon.getSelectSqlAttributsString("Tbl_Unique_Uniquetable")+" from tbl_unique_uniquetable where name = '"+EntityType+"_id'";
		log.info("uuIdSql:"+uuIdSql);
		//System.out.println("uuIdSql:"+uuIdSql);
		SqlContainer uuIdSqlc = context.createSqlContainer(uuIdSql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Tbl_Unique_Uniquetable");
		long uuIdAeId = 0;
		if(uuIdList != null && uuIdList.size() != 0){
			if(uuIdList.get(0).getValue("code") != null){
				uuIdAeId = new Long(uuIdList.get(0).getValue("code").toString());
			}
		}
		String selectMAXIdSql = "select max(id) \"maxid\" from "+EntityType;
		log.info("selectMAXIdSql:"+selectMAXIdSql);
		SqlContainer selectMAXIdSqlc = context.createSqlContainer(selectMAXIdSql);
		List<BasicEntity> selectMAXIdList = context.executeSelectSQL(selectMAXIdSqlc,EntityType);
		long selectMAXIdAeId = 0;
		if(selectMAXIdList != null && selectMAXIdList.size() != 0){
			if(selectMAXIdList.get(0).getValue("maxid") != null){
				selectMAXIdAeId = new Long(selectMAXIdList.get(0).getValue("maxid").toString());
			}
		}
		String type = "";
		if(selectMAXIdAeId == 0 && uuIdAeId == 0){
			uuIdAeId = 1;
			type = "insert";
		}else if(selectMAXIdAeId >= uuIdAeId && uuIdAeId != 0){
			uuIdAeId = selectMAXIdAeId + 1;
			type = "update";
		}else if(selectMAXIdAeId >= uuIdAeId && uuIdAeId == 0){ 
			uuIdAeId = selectMAXIdAeId + 1;
			type = "insert";
		}else{
			uuIdAeId = uuIdAeId;
			type = "update";
		}
		if(type == "insert"){
			log.info("进入添加");
			long insertId = uuIdAeId +1;
			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");
			tuuAe.setValue("code", insertId);
			tuuAe.setValue("name", EntityType+"_id");
//			String insertSql = "INSERT INTO tbl_unique_uniquetable (code,name) values ("+insertId+",'"+EntityType+"_id');";
//			SqlContainer insertSqlc = context.createSqlContainer(insertSql);
//			context.executeInsertSQL(insertSqlc, "Tbl_Unique_Uniquetable");
			Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(tuuAe);
			String sqlString ="insert into Tbl_Unique_Uniquetable("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
			//System.out.println(sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			context.executeInsertSQL(sc, "Tbl_Unique_Uniquetable");
			//Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
			//context.insert(tuuAe);
			
		}else{
			log.info("进入更新");
			long updateId = uuIdAeId +1;
			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");
			tuuAe.setValue("code", updateId);
			tuuAe.setValue("name", EntityType+"_id");
//			String insertSql = "UPDATE tbl_unique_uniquetable SET code = "+updateId+" where name = '"+EntityType+"_id';";
//			SqlContainer insertSqlc = context.createSqlContainer(insertSql);
//			context.executeUpdateSQL(insertSqlc, "Tbl_Unique_Uniquetable");
			
			//Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
			//query.add(Restrictions.eq("name", EntityType+"_id"));
			String sqlString ="update Tbl_Unique_Uniquetable set "+ResourceCommon.getUpdateAttributesSqlString(tuuAe)+" where name='"+EntityType+"_id'";
			SqlContainer sc = context.createSqlContainer(sqlString);
			//System.out.println(sqlString);
			context.executeUpdateSQL(sc, "Tbl_Unique_Uniquetable");
	
			////System.out.println(query.getQuerySqlString());
			//context.update(tuuAe, query);
		}
		log.info("退出===getEntityPrimaryKey方法 返回值为："+uuIdAeId);
		return uuIdAeId;
	}
	
	/**
	 * 获取资源分组数组
	 * @param entityGroupName
	 * @param myStructure
	 * @return
	 */
	public String[] getAetNameOfAetg(String entityGroupName, String myStructure){
		log.info("进入===getAetNameOfAetg方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] aetNameOfAetg = structure.getAetNameOfAetg(entityGroupName);
		log.info("退出===getAetNameOfAetg方法 返回值为:"+aetNameOfAetg);
		return aetNameOfAetg;
	}




	
	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public List<ApplicationEntity> getAppArrsByRecursionForSrcByAtg(ApplicationEntity app, String type, String myStructure) {
		log.info("进入===getAetNameOfAetg方法");
		List<ApplicationEntity> list = new ArrayList<ApplicationEntity>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		String[] entityTypes = null;	
			entityTypes = structure.getAetNameOfAetg(type);
			if(entityTypes != null){
				log.info("开始循环entityTypes");
				for(String s:entityTypes){
					ApplicationEntity[] appArrsByRecursionForSrc = getAppArrsByRecursionForSrc(app, s, myStructure);
					if(appArrsByRecursionForSrc != null){
						for(ApplicationEntity ap:appArrsByRecursionForSrc){
							list.add(ap);
						}
					}
				}
				log.info("结束循环entityTypes");
			}
		log.info("退出===getAetNameOfAetg方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByAreas(List<String> areaId,String resourceType){
		log.info("进入===getStationByAreas方法");
		String areaIds = "";
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds = s + ",";
			}
			log.info("结束循环areaId");
			areaIds = areaIds.substring(0,areaIds.length() -1);
		}
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString(resourceType,"bs")+" FROM v_basestation  aab,"+resourceType+"  bs WHERE stationId = bs.id AND areaId in ("+areaIds+")  GROUP BY stationId";
		log.info("sql:"+sql);
		//System.out.println("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,resourceType);
		if(uuIdList!=null&&!uuIdList.isEmpty()){
			list.addAll(uuIdList);
		}
		log.info("退出===getStationByAreas方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据基站ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByBaseStation(String baseStationId,String baseStationType,String resourceType){
		log.info("进入===getStationByBaseStation方法");
		String areaIds = "";
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		//基站ID转换类型
		long baseStationIdLong = 0;
		if(baseStationId != null && !baseStationId.equals("")){
			baseStationIdLong = Long.parseLong(baseStationId);
		}else{
			return null;
		}
		//获取基站父级资源
		List<BasicEntity> parentList = this.networkResourceManageDao.getParentResourceByChildIdAndChildType(baseStationIdLong, baseStationType);
		if(parentList!=null&&!parentList.isEmpty()){
			BasicEntity basicEntity = parentList.get(0);
			//基站父级ID转换类型
			long parentIdLong = 0;
			String parentType = basicEntity.getValue("_entityType");
			if(baseStationId != null && !baseStationId.equals("")){
				parentIdLong = Long.parseLong(basicEntity.getValue("id")+"");
			}else{
				return null;
			}
			//根据基站的父级资源获取站址
			List<BasicEntity> stationList = this.networkResourceManageDao.getParentResourceByChildIdAndChildTypeAndParentType(parentIdLong, parentType, resourceType);
			if(stationList != null && !stationList.isEmpty()){
				list.addAll(stationList);
			}
		}
		log.info("退出===getStationByBaseStation方法 返回值为:"+list);
		return list;
	}
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByAreas(List<String> areaId,String resourceType){
		log.info("进入===getBaseStationByAreas方法");
		List<Long> areaIds = new ArrayList<Long>();
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds.add(Long.parseLong(s));
			}
			log.info("结束循环areaId");
		}
		String[] entityGroupNames = null;
		if("Station".equals(resourceType)){
			resourceType = "GeneralBaseStation";
		}
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			for(String s:entityGroupNames){
				List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area",s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					list.addAll(uuIdList);
				}
			}
		}else{
			List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area",resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				list.addAll(uuIdList);
			}
		}
		log.info("退出===getBaseStationByAreas方法 返回值为:"+list);
		return list;
	}
	

	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByArea(String areaId,String resourceType){
		log.info("进入===getStationByArea方法");
		if(areaId==null||"".equals(areaId)){
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString(resourceType,"bs")+" FROM v_basestation  aab,"+resourceType+"  bs WHERE "+resourceType+"Id = bs.id AND areaId ="+areaId;
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,resourceType);
		log.info("退出===getStationByArea方法 返回值为:"+uuIdList);
		return uuIdList;
		
	}

	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByArea(String areaId,String resourceType) {
		log.info("进入===getBaseStationByArea方法");
		if(areaId==null||"".equals(areaId)){
			return null;
		}
		String[] entityGroupNames = null;
		if("Station".equals(resourceType)){
			resourceType = "GeneralBaseStation";
		}
		long areaIdLong = 0;
		if(areaId != null && !areaId.equals("")){
			areaIdLong = Long.parseLong(areaId);
		}else{
			return null;
		}
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			log.info("开始循环entityGroupNames");
			for(String s:entityGroupNames){
				List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIdLong, "Sys_Area",s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					list.addAll(uuIdList);
				}
			}
			log.info("结束循环entityGroupNames");
		}else{
			List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIdLong, "Sys_Area",resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				list.addAll(uuIdList);
			}
		}
		log.info("退出===getBaseStationByArea方法 返回值为："+list);
		return list;
	}

	/**
	 * 根据区域ID与定义小区条件查询小区
	 * @param areaId 区域ID
	 * @param columnName 查询列名
	 * @param columnValue 查询条件
	 * @param myStructure 
	 * @return ApplicationEntity
	 */
	public ApplicationEntity getCellOfChildAreaByAreaId(String areaId,String columnName,String columnValue,String myStructure){
		log.info("进入===getCellOfChildAreaByAreaId方法");
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		ApplicationEntity sectionEntity = getSectionEntity("Sys_Area", areaId);
		if(sectionEntity == null){
			return null;
		}
		ApplicationEntity[] associatedEntity = getStrutureSelationsApplicationEntity(sectionEntity, "Sys_Area", AssociatedType.CHILD, myStructure);
		//ApplicationEntity[] associatedEntity = structure.getAssociatedEntity(sectionEntity,"Sys_Area", AssociatedType.CHILD);
		
		String areaIds = sectionEntity.getValue("id")+",";
		if(associatedEntity != null && associatedEntity.length > 0){
			log.info("开始循环associatedEntity");
			for(ApplicationEntity ae:associatedEntity){
				areaIds = areaIds + ae.getValue("id")+",";
			}
			log.info("结束循环associatedEntity");
		}
		areaIds = areaIds.substring(0,areaIds.length() -1);
		Context context = structureModuleLibrary.createContext();
		String cellString ="aac.cellid \"cellid\",aac.cellname \"cellname\",aac.cellType \"cellType\",aac.stationid \"stationid\", aac.stationName \"stationName\",aac.areaid \"areaid\", aac.areaName \"areaName\",c.id \"id\",c.name \"name\",c.longitude \"longitude\",c.latitude \"latitude\"";
			
		String sql = "SELECT "+cellString+" FROM v_cell  aac,cell c WHERE c."+columnName+" = '"+columnValue+"' AND c.id = aac.cellid AND aac.areaid IN ("+areaIds+")";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Cell");
		ApplicationEntity app = null;
		if(uuIdList != null && uuIdList.size() > 0){
			app = ApplicationEntity.changeFromEntity(uuIdList.get(0));
		}
		log.info("退出===getCellOfChildAreaByAreaId方法 返回值为："+app);
		return app;
	}
	
	//获取是否位于区域
	public boolean getLocatedArea(String startAreaId , String EndAreaId ){
		log.info("进入===getLocatedArea方法");
		ApplicationEntity start = getSectionEntity("Sys_Area", startAreaId);
		ApplicationEntity strutureSelationsEntity = getStrutureSelationsEntity(start,"Sys_Area",AssociatedType.PARENT,"networkresourcemanage");
		if(strutureSelationsEntity != null){
			if(strutureSelationsEntity.getValue("id") != null && !"".equals(strutureSelationsEntity.getValue("id"))){
				String id = strutureSelationsEntity.getValue("id").toString();
				if(EndAreaId.equals(id)){
					log.info("退出===getLocatedArea方法 返回值为：true");
					return true;
				}else{
					boolean locatedArea = getLocatedArea(id , EndAreaId );
					log.info("退出===getLocatedArea方法 返回值为："+locatedArea);
					return locatedArea;
				}
			}else{
				log.info("退出===getLocatedArea方法 返回值为：false");
				return false;
			}
		}else{
			log.info("退出===getLocatedArea方法 返回值为：false");
			return false;
		}
	}
	
	
	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public BasicEntity getAreaByStation(String resourceId,String resourceType){
		log.info("进入===getAreaByStation方法");
		if(resourceId==null||"".equals(resourceId)){
			log.info("退出===getAreaByStation方法 返回值为：null");
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Sys_Area","bs")+" FROM v_basestation  aab,AREA  bs WHERE areaid = bs.id AND basestationId = "+resourceId + " and basestationtype = '" + resourceType + "'";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		BasicEntity re = null;
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		if(uuIdList != null && uuIdList.size() > 0){
			re = uuIdList.get(0);
		}
		log.info("退出===getAreaByStation方法 返回值为："+re);
		return re;
		
	}
	
	
	/**
	 * 
	 * @description: 利用sql语句更新entity数据
	 * @author：
	 * @return     
	 * @return int     
	 * @date：Sep 26, 2012 5:18:34 PM
	 */
	public int updateApplicationEntityBySql(ApplicationEntity app,ApplicationModule module){
		log.info("进入===updateApplicationEntityBySql方法");
		String updateString = "";
		Map<String, Object> moduleMap = module.toMap();
		log.info("开始循环moduleMap");
		for(String key:moduleMap.keySet()){
			if(app.containKey(key)){
				if(app.getValue(key)!=null &&!"".equals(app.getValue(key))){
					String type = module.getAttribute(key).getValue("type")+"";
					if(type.equals("java.lang.String")||type.equals("java.util.Date")){
						String v="";
						if(type.equals("java.util.Date")){
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:SS");
								v = sdf.format(app.getValue(key));
								v="to_date('"+v+"','yyyy-mm-dd hh24:mi:ss')";
								updateString += key+"="+v+",";
							} catch (Exception e) {
								v= app.getValue(key);
							}
						}else{
							v= app.getValue(key);
							if(key.equals("level")){
								updateString += "\"LEVEL\"='"+v+"',";
							}else if(key.equals("size")){
								updateString += "\"SIZE\"='"+v+"',";
							}else{
								updateString += key+"='"+v+"',";
							}
							
						}
						
					}else{
						if(key.equals("level")){
							updateString += "\"LEVEL\"='"+app.getValue(key)+"',";
						}else if(key.equals("size")){
							updateString += "\"SIZE\"='"+app.getValue(key)+"',";
						}else{
							updateString += key+"='"+app.getValue(key)+"',";
						}
						//updateString += key+"="+app.getValue(key)+",";
					}
				}
				
			}/*else{
				updateString += key+"="+null+",";
			}*/
		}
		log.info("结束循环moduleMap");
		updateString=updateString.substring(0,updateString.lastIndexOf(","));
		Context context = structureModuleLibrary.createContext();
		String sql = "update "+app.getValue("_entityType")+" set "+updateString+" where id="+app.getValue("id");
		log.info("sql:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		int executeUpdateSQL = context.executeUpdateSQL(sqlContainer, app.getValue("_entityType")+"");
		log.info("退出===updateApplicationEntityBySql方法 返回值为："+executeUpdateSQL);
		return executeUpdateSQL;
	}
	
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByStation(String stationId,String resourceType){
		log.info("进入===getBaseStationByStation方法");
		String[] entityGroupNames = null;
		if("Station".equals(resourceType)){
			resourceType = "GeneralBaseStation";
		}
		long stationIdLong = 0;
		if(stationId != null && !stationId.equals("")){
			stationIdLong = Long.parseLong(stationId);
		}else{
			return null;
		}
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			log.info("开始循环entityGroupNames");
			for(String s:entityGroupNames){
				List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(stationIdLong, "Station",s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					list.addAll(uuIdList);
				}
				
			}
			log.info("结束循环entityGroupNames");
		}else{
			List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(stationIdLong, "Station",resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				list.addAll(uuIdList);
			}
		}
		log.info("退出===getBaseStationByStation方法 返回值为："+list);
		return list;
	}
	
	

	
	
	public int getBaseStationCountByArea(List<String> areaIdList){
		log.info("进入===getBaseStationCountByArea方法");
		int count = 0;
		String areaIds = "";
		if(areaIdList != null && !areaIdList.isEmpty()){
			log.info("开始循环areaIdList");
			for(String s:areaIdList){
				areaIds = areaIds + s +",";
			}
			log.info("结束循环areaIdList");
			areaIds = areaIds.substring(0, areaIds.lastIndexOf(","));
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		Context context = structureModuleLibrary.createContext();
		String bStr = "basestationId \"basestationId\",basestationName \"basestationName\",stationid \"stationid\",stationName \"stationName\",areaid \"areaid\",basestationType \"basestationType\"" ;
		String sql = "SELECT "+bStr+" FROM v_basestation WHERE areaid IN ("+areaIds+")";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		if(uuIdList != null && !uuIdList.isEmpty()){
			count = uuIdList.size();
		}
		log.info("退出===getBaseStationCountByArea方法 返回值为："+count);
		return count;
	}
	
	private List<Map<String, Object>> findQueryByHibernate(final String sql){
		log.info("进入===findQueryByHibernate方法");
		List<Map<String, Object>> list =  this.hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session)
					throws HibernateException, SQLException {
						List<Map<String, Object>> find = null;
							SQLQuery query = session.createSQLQuery(sql);
							//org.hibernate.Query query = session.createQuery(sql);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							find = query.list();
				return find;
			}
		});
		log.info("退出===findQueryByHibernate方法 返回值为:"+list);
		return list;
	}
	
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getBaseStationByAreasByHibernate(List<String> areaId,String resourceType){
		log.info("进入===getBaseStationByAreasByHibernate方法");
		List<Long> areaIds = new ArrayList<Long>();
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds.add(Long.parseLong(s));
			}
			log.info("结束循环areaId");
		}
		String[] entityGroupNames = null;
		if("Station".equals(resourceType)){
			resourceType = "GeneralBaseStation";
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			log.info("开始循环entityGroupNames");
			for(String s:entityGroupNames){
				List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area",s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					for(BasicEntity b:uuIdList){
						list.add(b.toMap());
					}
				}
			}
			log.info("结束循环entityGroupNames");
		}else{
			List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area",resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				for(BasicEntity b:uuIdList){
					list.add(b.toMap());
				}
			}
		}
		log.info("退出===getBaseStationByAreasByHibernate方法 返回值为:"+list);
		return list;
	}
	
	
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getBaseStationByStationByHibernate(String stationId,String resourceType){
		log.info("进入===getBaseStationByStationByHibernate方法");
		String[] entityGroupNames = null;
		if("Station".equals(resourceType)){
			resourceType = "GeneralBaseStation";
		}
		long stationIdLong = 0;
		if(stationId != null && !stationId.equals("")){
			stationIdLong = Long.parseLong(stationId);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			log.info("开始循环entityGroupNames");
			for(String s:entityGroupNames){
				
				List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(stationIdLong, "Station",s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					for(BasicEntity b:uuIdList){
						list.add(b.toMap());
					}
				}
				
			}
			log.info("结束循环entityGroupNames");
		}else{
		
			List<BasicEntity> uuIdList = this.networkResourceManageDao.getResourceByResourceType(stationIdLong, "Station",resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				for(BasicEntity b:uuIdList){
					list.add(b.toMap());
				}
			}
		}
		log.info("退出===getBaseStationByStationByHibernate方法 返回值为："+list);
		return list;
	}

	public int getCellCountByArea(List<String> areaIdList){
		log.info("进入===getCellCountByArea方法");
		int count = 0;
		String areaIds = "";
		if(areaIdList != null && !areaIdList.isEmpty()){
			log.info("开始循环areaIdList");
			for(String s:areaIdList){
				areaIds = areaIds + s +",";
			}
			log.info("结束循环areaIdList");
			areaIds = areaIds.substring(0, areaIds.lastIndexOf(","));
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		Context context = structureModuleLibrary.createContext();
		String cellString ="aac.cellid \"cellid\",aac.cellname \"cellname\",aac.cellType \"cellType\",aac.stationid \"stationid\", aac.stationName \"stationName\",aac.areaid \"areaid\", aac.areaName \"areaName\"";
		
		String sql = "SELECT stationid FROM v_cell  WHERE areaid in ("+areaIds+") GROUP BY stationid";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		if(uuIdList != null && !uuIdList.isEmpty()){
			count = uuIdList.size();
		}
		log.info("退出===getCellCountByArea方法 返回值为："+count);
		return count;
	}
	
	
	public int getStationCountByArea(List<String> areaIdList){
		log.info("进入===getStationCountByArea方法");
		int count = 0;
		String areaIds = "";
		if(areaIdList != null && !areaIdList.isEmpty()){
			log.info("开始循环areaIdList");
			for(String s:areaIdList){
				areaIds = areaIds + s +",";
			}
			log.info("结束循环areaIdList");
			areaIds = areaIds.substring(0, areaIds.lastIndexOf(","));
		}
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		Context context = structureModuleLibrary.createContext();
		String sStr = "stationId \"stationId\",areaId \"areaId\",stationName \"stationName\",stationType \"stationType\",areaName \"areaName\",resId \"resId\",resName \"resName\"";
		String sql = "SELECT "+sStr+" FROM v_station WHERE areaid in ("+areaIds+")";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Sys_Area");
		if(uuIdList != null && !uuIdList.isEmpty()){
			count = uuIdList.size();
		}
		log.info("退出===getStationCountByArea方法 返回值为："+count);
		return count;
	}
	
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getBaseStationByName(String name){
		log.info("进入===getBaseStationByName方法");
		String sql = "SELECT  * FROM (SELECT basestationId \"id\",basestationName \"name\",basestationType \"basestationType\",stationid \"stationid\" FROM v_basestation)  a WHERE a.stationname = '"+name+"'";
		log.info("sql:"+sql);
		List<Map<String, Object>> findQueryByHibernate = findQueryByHibernate(sql);
		Map<String, Object> map = null;
		if(findQueryByHibernate != null && findQueryByHibernate.size() > 0){
			map = findQueryByHibernate.get(0);
			
		}
		log.info("退出===getBaseStationByName方法 返回值为："+map);
		return map;
	}
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getCellByName(String name){
		log.info("进入===getCellByName方法");
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Cell")+" FROM Cell where name = '"+name+"'";
		log.info("sql:"+sql);
		List<Map<String, Object>> findQueryByHibernate = findQueryByHibernate(sql);
		Map<String, Object> map = null;
		if(findQueryByHibernate != null && findQueryByHibernate.size() > 0){
			map = findQueryByHibernate.get(0);
		}
		log.info("结束===getCellByName方法 返回值为："+map);
		return map;
	}
	
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getRoomByAreasByHibernate(List<String> areaId,String resourceType){
		log.info("进入===getRoomByAreasByHibernate方法");
		String areaIds = "";
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds = areaIds + s + ",";
			}
			log.info("结束循环areaId");
			areaIds = areaIds.substring(0,areaIds.length() -1);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String sql = "SELECT * FROM (SELECT  roomId \"id\",stationid \"stationid\",stationName \"stationName\",roomType \"roomType\",areaid \"areaid\",areaName \"areaName\", roomName \"name\"  FROM v_room) a  WHERE a.\"areaid\" in ("+areaIds+")";
			log.info("sql:"+sql);
			List<Map<String, Object>> findQueryByHibernate = findQueryByHibernate(sql);
			if(findQueryByHibernate!=null&&!findQueryByHibernate.isEmpty()){
				list.addAll(findQueryByHibernate);
			}
		
		log.info("退出===getRoomByAreasByHibernate方法 返回值为:"+list);
		return list;
	}
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getResByAreasByHibernate(List<String> areaId,String resourceType,Map<String, String> cmap){
		log.info("进入===getResByAreasByHibernate方法");
		String areaIds = "";
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds = areaIds + s + ",";
			}
			log.info("结束循环areaId");
			areaIds = areaIds.substring(0,areaIds.length() -1);
		}
		String hql = "";
		if(cmap != null && cmap.size() > 0){
			hql = " and ( ";
			log.info("开始循环map");
			for(String key : cmap.keySet()) {
				hql = hql + " bs." + key + " like '"+cmap.get(key)+"' or ";
				//Criterion like = Restrictions.like(key, map.get(key));
			}
			if(hql != null && hql != ""){
				hql = hql.substring(0,hql.length() - 3);
			}
			hql = hql + ")";
		}
		log.info("结束循环map");
		String[] entityGroupNames = null;
		List<BasicEntity> listB = new ArrayList<BasicEntity>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			for(String s:entityGroupNames){
				Context context = structureModuleLibrary.createContext();
				String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString(s,"bs")+",aab.areaId \"areaId\" FROM v_basestation  aab,"+s+"  bs WHERE basestationId = bs.id AND areaId in ("+areaIds+") AND basestationtype = '"+s+"'" + hql;
				log.info("sql:"+sql);
				SqlContainer uuIdSqlc = context.createSqlContainer(sql);
				List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,s);
				if(uuIdList!=null&&!uuIdList.isEmpty()){
					listB.addAll(uuIdList);
				}
				
			}
		}else{
			Context context = structureModuleLibrary.createContext();
			String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString(resourceType,"bs")+",aab.areaId \"areaId\" FROM v_"+resourceType+"  aab,"+resourceType+"  bs WHERE bs.id = aab.resId and areaId  in ("+areaIds+")" + hql;
			log.info("sql:"+sql);
			SqlContainer uuIdSqlc = context.createSqlContainer(sql);
			List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,resourceType);
			if(uuIdList!=null&&!uuIdList.isEmpty()){
				listB.addAll(uuIdList);
			}
		}
		if(listB != null && listB.size() > 0){
			for(BasicEntity b:listB){
				list.add(b.toMap());
			}
		}
		log.info("退出===getResByAreasByHibernate方法 返回值为:"+list);
		return list;
	}
	
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getResByAreasHibernate(List<String> areaId,String resourceType,Map<String, String> cmap){
		log.info("进入===getResByAreasHibernate方法");
		List<Long> areaIds = new ArrayList<Long>();
		if(areaId != null){
			log.info("开始循环areaId");
			for(String s:areaId){
				areaIds.add(Long.parseLong(s));
			}
			log.info("结束循环areaId");
		}
		log.info("结束循环map");
		String[] entityGroupNames = null;
		List<BasicEntity> listB = new ArrayList<BasicEntity>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure("networkresourcemanage");
		entityGroupNames = structure.getAetNameOfAetg(resourceType);
		if(entityGroupNames != null && entityGroupNames.length > 0){
			for(String s:entityGroupNames){
				List<BasicEntity> resourceByResourceType = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area", s);
				if(resourceByResourceType != null && resourceByResourceType.size() > 0){
					for(BasicEntity b:resourceByResourceType){
						Map<String, Object> map = b.toMap();
						list.add(map);
					}
				}
			}
		}else{
			List<BasicEntity> resourceByResourceType = this.networkResourceManageDao.getResourceByResourceType(areaIds, "Sys_Area", resourceType);
			if(resourceByResourceType != null && resourceByResourceType.size() > 0){
				for(BasicEntity b:resourceByResourceType){
					Map<String, Object> map = b.toMap();
					list.add(map);
				}
			}
		}
		
		log.info("退出===getResByAreasHibernate方法 返回值为:"+list);
		return list;
	}
	
//	/**
//	 * 根据父级资源获取其所属指定类型资源集合
//	* @author ou.jh
//	* @date Jun 21, 2013 1:55:08 PM
//	* @Description: TODO 
//	* @param @param parentId
//	* @param @param parentType
//	* @param @param resourceType
//	* @param @return        
//	* @throws
//	 */
//	public List<Map<String,Object>> getResourceByResourceType(long parentId,String parentType,String resourceType){
//		log.info("进入getResourceByResourceType方法");
//		List<Map<String,Object>> resourceByResourceType = this.networkResourceDao.getResourceByResourceType(parentId, parentType, resourceType);
//		log.info("执行getResourceByResourceType方法成功，实现了”根据父级资源获取其所属指定类型资源集合“的功能");
//		log.info("退出getResourceByResourceType方法,返回List<Map<String,Object>>"+resourceByResourceType);
//		return resourceByResourceType;
//	}
//	
//	
//	/**
//	 * 根据指定区域获取其所属指定类型资源集合 
//	* @author ou.jh
//	* @date Jun 21, 2013 2:06:29 PM
//	* @Description: TODO 
//	* @param @return        
//	* @throws
//	 */
//	public List<Map<String,Object>> getResourceByAreaAndResourceType(long areaId,String resourceType){
//		log.info("进入getResourceByAreaAndResourceType方法");
//		List<Map<String,Object>> resourceByResourceType = this.networkResourceDao.getResourceByAreaAndResourceType(areaId, resourceType);
//		log.info("执行getResourceByAreaAndResourceType方法成功，实现了”根据指定区域获取其所属指定类型资源集合“的功能");
//		log.info("退出getResourceByAreaAndResourceType方法,返回List<Map<String,Object>>"+resourceByResourceType);
//		return resourceByResourceType;
//	}
	/**
	 * 
	 * @description: 根据坐标，资源类型，条件，账户，分页获取资源记录相关信息（按距当前位置距离排序）（适用 站址 机房 基站）
	 * @author：yuan.yw
	 * @param AetName 资源类型
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 条件map
	 * @param start 开始记录
	 * @param end  结束记录
	 * @param account 账户
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jun 25, 2013 12:08:38 PM
	 */
	public  Map<String,Object> getResourceWithPagingByAetNameAndGPSDistanceAndAccount(
			String AetName, double longitude, double latitude, Map<String, Object> map,
			int start, int end, String account){
		Map<String,Object>  resultMap =  new HashMap<String,Object>();
		StringBuffer sf = new StringBuffer();
		if(account!=null && !"".equals(account)){
			String areaName = "";
			Map<String,Object> mp = this.sysAreaDao.getAreaListInCityLevelByAccount(account);//市级区域
			if(mp!=null && !mp.isEmpty()){
				sf.append(","+mp.get("AREA_ID"));
				areaName = mp.get("NAME")+"";
				
				String areaIds = sf+"";
				if(!"".equals(areaIds)){
					areaIds = areaIds.substring(1);
				}
				if(AetName.indexOf("BaseStation")>=0){
					AetName = "BaseStation";
				}
				for(int distance = 3000;distance<=15000;distance=distance+3000){
					Map areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRing(latitude, longitude, 0, distance);
					//查询
					List<Map<String,Object>> list =this.networkResourceQueryDao.getResourceWithPagingByAetNameAndGPSMapAndAreaIds(AetName, areaLeftRightTopBottom, map, areaIds, longitude, latitude, start, end);
					if(list!=null && list.size()>0){
						int count = Integer.parseInt(list.get(0).get("totalCount")+"");
						Gson gson = new GsonBuilder().create();
						if(count>0){
							resultMap = list.get(0);
							resultMap.put("entityList",gson.toJson(list.get(0).get("entityList")));
							resultMap.put("totalCount", count+"");
							resultMap.put("currentArea",areaName );
							break;
						}
					}
				}
			}
			
		}else{
			resultMap.put("currentArea","广州市" );
		}
		return resultMap;
	}
	
	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}
	

	public NetworkResourceQueryDao getNetworkResourceQueryDao() {
		return networkResourceQueryDao;
	}

	public void setNetworkResourceQueryDao(
			NetworkResourceQueryDao networkResourceQueryDao) {
		this.networkResourceQueryDao = networkResourceQueryDao;
	}

	public List<Map<String, Object>> getResourceByAreaAndResourceType(
			long areaId, String resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> getResourceByResourceType(long parentId,
			String parentType, String resourceType) {
		// TODO Auto-generated method stub
		return null;
	}

	

	public NetworkResourceManageDao getNetworkResourceManageDao() {
		return networkResourceManageDao;
	}

	public void setNetworkResourceManageDao(
			NetworkResourceManageDao networkResourceManageDao) {
		this.networkResourceManageDao = networkResourceManageDao;
	}

	/**
	 * 转换BasicEntity为ApplicationEntity(List)
	* @author ou.jh
	* @date Jun 28, 2013 10:13:56 AM
	* @Description: TODO 
	* @param @param blist
	* @param @return        
	* @throws
	 */
	private List<ApplicationEntity> typeConversionApplicationEntityListByBasicEntity(List<BasicEntity> blist){
		List<ApplicationEntity> list = new ArrayList<ApplicationEntity>();
		if(blist != null && blist.size() > 0){
			for(BasicEntity b:blist){
				ApplicationEntity changeFromEntity = ApplicationEntity.changeFromEntity(b);
				ApplicationEntity typeAreaIdAndAreaLevelApp = ResourceCommon.typeAreaIdAndAreaLevelApp(changeFromEntity);
				list.add(typeAreaIdAndAreaLevelApp);
			}
			ResourceCommon.typeAreaIdAndAreaLevelApp(list);
		}else{
			return null;
		}
		return list;
	}
	
	/**
	 * 转换BasicEntity为ApplicationEntity(Array)
	* @author ou.jh
	* @date Jun 28, 2013 10:13:56 AM
	* @Description: TODO 
	* @param @param blist
	* @param @return        
	* @throws
	 */
	private ApplicationEntity[] typeConversionArrayByBasicEntity(List<BasicEntity> blist){
		ApplicationEntity[] ApplicationEntitys = null;
		if(blist != null && blist.size() > 0){
			ApplicationEntitys = new ApplicationEntity[blist.size()];
			int i = 0;
			for(BasicEntity b:blist){
				ApplicationEntity changeFromEntity = ApplicationEntity.changeFromEntity(b);
				ApplicationEntitys[i] = ResourceCommon.typeAreaIdAndAreaLevelApp(changeFromEntity);
				i++;
			}
		}else{
			return null;
		}
		return ApplicationEntitys;
	}
	
	
	/**
	 * 转换BasicEntity为Map(List)
	* @author ou.jh
	* @date Jun 28, 2013 10:13:56 AM
	* @Description: TODO 
	* @param @param blist
	* @param @return        
	* @throws
	 */
	private List<Map<String, Object>> typeConversionMapListByBasicEntity(List<BasicEntity> blist){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(blist != null && blist.size() > 0){
			for(BasicEntity b:blist){
				ApplicationEntity changeFromEntity = ApplicationEntity.changeFromEntity(b);
				ApplicationEntity typeAreaIdAndAreaLevelApp = ResourceCommon.typeAreaIdAndAreaLevelApp(changeFromEntity);
				list.add(typeAreaIdAndAreaLevelApp.toMap());
			}
		}else{
			return null;
		}
		return list;
	}
	
//	public NetworkResourceDao getNetworkResourceDao() {
//		return networkResourceDao;
//	}
//
//	public void setNetworkResourceDao(NetworkResourceDao networkResourceDao) {
//		this.networkResourceDao = networkResourceDao;
//	}
}
