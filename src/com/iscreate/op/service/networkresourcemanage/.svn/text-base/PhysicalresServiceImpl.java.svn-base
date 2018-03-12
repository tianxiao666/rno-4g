package com.iscreate.op.service.networkresourcemanage;

import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.log.service.LogService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.common.dao.NetworkResourceManageDao;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModuleLibrary;

public class PhysicalresServiceImpl implements PhysicalresService {
	/**日志*/
	private LogService logService;

	private StructureModuleLibrary structureModuleLibrary;
	
	private StructureCommonService structureCommonService;
	//yuan.yw add
	private NetworkResourceManageDao networkResourceManageDao;
	
	private static final Log log = LogFactory.getLog(PhysicalresServiceImpl.class);
	
	
	
	public NetworkResourceManageDao getNetworkResourceManageDao() {
		return networkResourceManageDao;
	}

	public void setNetworkResourceManageDao(
			NetworkResourceManageDao networkResourceManageDao) {
		this.networkResourceManageDao = networkResourceManageDao;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public StructureModuleLibrary getStructureModuleLibrary() {
		return structureModuleLibrary;
	}

	public void setStructureModuleLibrary(
			StructureModuleLibrary structureModuleLibrary) {
		this.structureModuleLibrary = structureModuleLibrary;
	}
	
	/**
	 * 根据类型和id，查询一个物理资源
	 * @param type
	 * @param id
	 * @return
	 */
	public BasicEntity getPhysicalresById(String type, long id) {
		String idName = "id";
		if(type.equals("Sys_Area")){
			idName = "area_id";
		}else{
			idName = "id";
		}
		log.info("进入getPhysicalresById(String type, long id)，type="+type+",id="+id+",根据类型和id，查询一个物理资源");
		Context context = structureModuleLibrary.createContext();
		/*Query query = context.createQueryBuilder(type);
		query.add(Restrictions.eq("id", id));*/
		String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+idName+"="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> listBe = context.executeSelectSQL(sc,type);
		BasicEntity be = null;
		if(listBe!=null){
			be = listBe.get(0);
			be = ResourceCommon.typeAreaIdAndAreaLevelBas(be);
		}
		log.info("退出getPhysicalresById(String type, long id),查询结果成功");
		//return context.queryEntity(query);
		return be;
	}
	/**
	 * 根据类型和label，查询一个物理资源
	 * @param type
	 * @param label
	 * @return
	 */
	public BasicEntity getPhysicalresByLabel(String type, String label) {
		log.info("进入getPhysicalresByLabel(String type, String label)，type="+type+",label="+label+",根据类型和label，查询一个物理资源");
		Context context = structureModuleLibrary.createContext();
		/*Query query = context.createQueryBuilder(type);
		query.add(Restrictions.eq("label", label));*/
		String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where label='"+label+"'";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> listBe = context.executeSelectSQL(sc,type);
		BasicEntity be = null;
		if(listBe!=null){
			be = listBe.get(0);
			be = ResourceCommon.typeAreaIdAndAreaLevelBas(be);
		}
		log.info("退出getPhysicalresByLabel(String type, String label),查询结果成功");
		//return context.queryEntity(query);
		return be;
	}
	/**
	 * 根据特定条件，查询物理资源
	 * @param type
	 * @param id
	 * @param key
	 * @param condition
	 * @return
	 */
	public List<BasicEntity> getPhysicalresByCondition(String type, String attr, Object condition, String searchCondition) {
		log.info("进入getPhysicalresByCondition(String type, String attr, Object condition, String searchCondition)，type="+type+",attr="+attr+",condition="+condition+",searchCondition="+searchCondition+",根据特定条件，查询物理资源");
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(type);
		//根据条件进行查询(根据条件判断需要精确查找还是模糊查找)
		StringBuffer sf = new StringBuffer();
		if("exactMatch".equals(searchCondition)) {
			//精确查找
			//query.add(Restrictions.eq(attr, condition));
			sf.append(attr+"='"+condition+"'");
		} else if ("indistinctMatch".equals(searchCondition)) {
			//模糊查找
			sf.append("instr("+attr+",'"+condition+"')>0");
			//query.add(Restrictions.like(attr, "%" + condition + "%"));
		}
		List<BasicEntity> listBe =null;
		if(!"".equals(sf+"")){
			String sqlString ="select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type +" where "+sf;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			 listBe = context.executeSelectSQL(sc,type);
		}
		
		log.info("退出getPhysicalresByCondition(String type, String attr, Object condition, String searchCondition),查询结果成功");
		return listBe;
	}
	
	/**
	 * 更新一个物理资源
	 * @param appEntity
	 * @param type
	 */
	public int updatePhysicalres(ApplicationEntity appEntity, String type) {
		log.info("进入updatePhysicalres(ApplicationEntity appEntity, String type)，appEntity="+appEntity+",type="+type+",更新一个物理资源");		
		Context context = structureModuleLibrary.createContext();
		/*Query query = context.createQueryBuilder(type);
		query.add(Restrictions.eq("id", appEntity.getValue("id")));*/
		String sqlString = "update "+type+" set " +ResourceCommon.getUpdateAttributesSqlString(appEntity) +" where id="+appEntity.getValue("id");
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		
		log.info("退出updatePhysicalres(ApplicationEntity appEntity, String type),更新成功");
		return context.executeUpdateSQL(sc, type);
	}
	
	/**
	 * 添加一个物理资源
	 * @param appEntity
	 * @param type
	 */
	public int addPhysicalres(ApplicationEntity appEntity) {
		log.info("进入addPhysicalres(ApplicationEntity appEntity)，appEntity="+appEntity+",添加一个物理资源");
		Context context = structureModuleLibrary.createContext();
		log.info("退出addPhysicalres(ApplicationEntity appEntity)，添加成功");
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(appEntity);

		String sqlString = "insert into "+appEntity.getType()+"("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		return context.executeInsertSQL(sc, appEntity.getType());
	}
	
	/**
	 * 根据不同条件查询资源
	 * @return
	 */
	public List<BasicEntity> searchResourceByCondition(ApplicationEntity searchedAe) {
		log.info("进入searchResourceByCondition(ApplicationEntity searchedAe)，searchedAe="+searchedAe+",根据不同条件查询资源");
		if(searchedAe == null) {
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//Query query = context.createQueryBuilder(searchedAe.getType());
		StringBuffer sf = new StringBuffer();
		for(String key : searchedAe.keyset()) {
			if (searchedAe.getValue(key) != null && !"".equals(searchedAe.getValue(key))) {
				if("_entityId".equals(key) || "_entityType".equals(key)) {
					continue;
				} else {
					if(searchedAe.getValue(key).getClass().getName().indexOf("String") > -1) {
						//当属性为String类型时，进行模糊查询
						//if((searchedAe.getValue(key) + "").indexOf("%") > -1) {
							//条件值存在%，不去做前后模糊查询
							//query.add(Restrictions.like(key, searchedAe.getValue(key)));
						//} else {
							//条件值存在%，进行模糊查询
							//query.add(Restrictions.like(key, "%" + searchedAe.getValue(key) + "%"));
							if(!"".equals(sf+"")){
								sf.append(" and instr("+key+",'"+searchedAe.getValue(key)+"')>0");
							}else{
								sf.append("instr("+key+",'"+searchedAe.getValue(key)+"')>0");
							}
							
						//}
					} else if(searchedAe.getValue(key).getClass().getName().indexOf("Date") > -1){
						//当属性为其他类型时，进行精确查询
						//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
						
						if(!"".equals(sf+"")){
							sf.append(" and "+key+"=to_date('"+sdf.format(searchedAe.getValue(key))+"','yyyy-mm-dd hh24:mi:ss')");
						}else{
							sf.append(key+"=to_date('"+sdf.format(searchedAe.getValue(key))+"','yyyy-mm-dd hh24:mi:ss')");
						}
					}else{
						if(!"".equals(sf+"")){
							sf.append(" and "+key+"='"+searchedAe.getValue(key)+"'");
						}else{
							sf.append(key+"='"+searchedAe.getValue(key)+"'");
						}
					}
				}
			}
		}
		List<BasicEntity> queryList =null;
		if(!"".equals(sf)){
			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(searchedAe.getType())+" from "+ searchedAe.getType()+" where "+sf;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			queryList = context.executeSelectSQL(sc, searchedAe.getType());
		}
		
		

		log.info("退出searchResourceByCondition(ApplicationEntity searchedAe)，查询返回");
		return queryList;
	}
	
	/**
	 * 根据不同条件查询资源个数
	 * @return
	 */
	public int searchResourceCountByCondition(ApplicationEntity searchedAe) {
		log.info("进入searchResourceCountByCondition(ApplicationEntity searchedAe)，searchedAe="+searchedAe+",据不同条件查询资源个数");
		if(searchedAe == null) {
			return 0;
		}
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(searchedAe.getType());
		for(String key : searchedAe.keyset()) {
			if (!"".equals(searchedAe.getValue(key))) {
				if("_entityId".equals(key) || "_entityType".equals(key)) {
					continue;
				} else {
					//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
				}
			}
		}
		log.info("退出searchResourceCountByCondition(ApplicationEntity searchedAe),查询返回");
		return 0;//context.getTotalCountByQuery(query);
	}
	
	/**
	 * 根据不同条件查询资源个数(分页后的个数)
	 * @return
	 */
	public int searchResourceCountByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize) {
		log.info("进入searchResourceCountByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize)，searchedAe="+searchedAe+",pageIndex="+pageIndex+",pageSize="+pageSize+",根据不同条件查询资源个数(分页后的个数)");
		if(searchedAe == null) {
			return 0;
		}
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(searchedAe.getType());
		for(String key : searchedAe.keyset()) {
			if (!"".equals(searchedAe.getValue(key))) {
				if("_entityId".equals(key) || "_entityType".equals(key)) {
					continue;
				} else {
					//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
				}
			}
		}
		//query.setFirstResult((pageIndex - 1) * pageSize);
		//query.setMaxResults(pageSize);
		log.info("退出searchResourceCountByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize)，查询返回");
		return 0;//context.getTotalCountByQuery(query);
	}
	
	/**
	 * 根据不同条件查询资源(分页)
	 * @return
	 */
	public List<BasicEntity> searchResourceByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize) {
		log.info("进入searchResourceByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize)，searchedAe="+searchedAe+",pageIndex="+pageIndex+",pageSize="+pageSize+",根据不同条件查询资源(分页)");
		if(searchedAe == null) {
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(searchedAe.getType());
		for(String key : searchedAe.keyset()) {
			if (!"".equals(searchedAe.getValue(key))) {
				if("_entityId".equals(key) || "_entityType".equals(key)) {
					continue;
				} else {
					//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
				}
			}
		}
		//query.setFirstResult((pageIndex - 1) * pageSize);
		//query.setMaxResults(pageSize);
		log.info("退出searchResourceByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize),查询返回");

		return null;//context.queryList(query);
	}

	/**
	 * 
	 * @description:  取得某个资源面板布局
	 * @author：
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 23, 2012 4:09:26 PM
	 */
	public List<BasicEntity> getResourceEntityList(String type,String resourceId,String resourceType) {
		log.info("进入getResourceEntityList(String type,String resourceId,String resourceType)，type="+type+",resourceId="+resourceId+",resourceType="+resourceType+",取得某个资源面板布局");		
		if(type==null||"".equals(type)){
			log.info("退出getResourceEntityList(String type,String resourceId,String resourceType),查询返回null");			
			return null;
		}else{
			Context context = structureModuleLibrary.createContext();
			//Query query = context.createQueryBuilder(type);
			//query.add(Restrictions.eq("resourceid",resourceId));
			//query.add(Restrictions.eq("resourcetype",resourceType));

			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+ type +" where resourceid="+resourceId+" and resourcetype='"+resourceType+"'";
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> queryList = context.executeSelectSQL(sc, type);
			log.info("退出getResourceEntityList(String type,String resourceId,String resourceType),查询返回");
			return queryList;
		}
	}


	public List<BasicEntity> searchResourceByCondition(
			ApplicationEntity searchedAe, Map<String,String> dateMap) {
		log.info("进入searchResourceByCondition(ApplicationEntity searchedAe, Map<String,String> dateMap)，searchedAe="+searchedAe+",dateMap="+dateMap+",按条件查询资源");		

		if(searchedAe == null) {
			log.info("退出searchResourceByCondition(ApplicationEntity searchedAe, Map<String,String> dateMap)，返回结果null");		

			return null;
		}
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(searchedAe.getType());
		int conditionFlag=0;
		StringBuffer sf = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String key : searchedAe.keyset()) {
			if (searchedAe.getValue(key) != null && !"".equals(searchedAe.getValue(key))) {
				if("_entityId".equals(key) || "_entityType".equals(key)) {
					continue;
				} else {
					if(searchedAe.getValue(key).getClass().getName().indexOf("String") > -1) {
						//当属性为String类型时，进行模糊查询
						//if((searchedAe.getValue(key) + "").indexOf("%") > -1) {
							//条件值存在%，不去做前后模糊查询
							//query.add(Restrictions.like(key, searchedAe.getValue(key)));
						//} else {
							//条件值存在%，进行模糊查询
							//query.add(Restrictions.like(key, "%" + searchedAe.getValue(key) + "%"));
							if(!"".equals(sf+"")){
								sf.append(" and instr("+key+",'"+searchedAe.getValue(key)+"')>0");
							}else{
								sf.append("instr("+key+",'"+searchedAe.getValue(key)+"')>0");
							}
							
						//}
					} else if(searchedAe.getValue(key).getClass().getName().indexOf("Date") > -1){
						//当属性为其他类型时，进行精确查询
						//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
						
						if(!"".equals(sf+"")){
							sf.append(" and "+key+"=to_date('"+sdf.format(searchedAe.getValue(key))+"','yyyy-mm-dd hh24:mi:ss')");
						}else{
							sf.append(key+"=to_date('"+sdf.format(searchedAe.getValue(key))+"','yyyy-mm-dd hh24:mi:ss')");
						}
					}else{
						if(!"".equals(sf+"")){
							sf.append(" and "+key+"='"+searchedAe.getValue(key)+"'");
						}else{
							sf.append(key+"='"+searchedAe.getValue(key)+"'");
						}
					}
					conditionFlag=conditionFlag+1;
				}
				
			}
		}

		//当属性为日期类型时特殊处理
		if(dateMap!=null&&!dateMap.isEmpty()){
			for(String k:dateMap.keySet()){
				String dates= dateMap.get(k).toString();
				String date1= dates.substring(0,dates.indexOf("/"));
				String date2 = dates.substring(dates.indexOf("/")+1,dates.length());
				if((date1!=null && !"".equals(date1)) || (date2!=null && !"".equals(date2)) ){
					conditionFlag=conditionFlag+1;
				}
				if(date1!=null&&!"".equals(date1)){
					if(!"".equals(sf)){
						sf.append(" and "+k+">=to_date('"+date1+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sf.append(k+">=to_date('"+date1+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}
					//query.add(Restrictions.ge(k, date1+" 00:00:00"));
				}
				if(date2!=null&&!"".equals(date2)){
					//query.add(Restrictions.le(k, date2+" 00:00:00"));
					if(!"".equals(sf)){
						sf.append(" and "+k+"<=to_date('"+date2+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sf.append(k+"<=to_date('"+date2+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}
				}
			}	
			
		}
		if(conditionFlag==0){
			log.info("退出searchResourceByCondition(ApplicationEntity searchedAe, Map<String,String> dateMap)，返回结果null");
			return null;
		}else{
			log.info("退出searchResourceByCondition(ApplicationEntity searchedAe, Map<String,String> dateMap)，返回结果");

			String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(searchedAe.getType())+" from "+ searchedAe.getType()+" where "+sf;
			//System.out.println("sqlString:"+sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			List<BasicEntity> queryList = context.executeSelectSQL(sc, searchedAe.getType());


			return queryList;
		}
		
		
	}

	/**
	 * 根据子资源递归查询资源所属区域
	 * @return
	 */
	public ApplicationEntity searchParentAreaResourceForSrc(
			ApplicationEntity searchedAe,String areaName,String linkType) {
		log.info("进入searchParentAreaResourceForSrc(ApplicationEntity searchedAe,String areaName,String linkType)，searchedAe="+searchedAe+",areaName="+areaName+",linkType="+linkType+",根据子资源递归查询资源所属区域");
		ApplicationEntity appe = null;
		if(searchedAe!=null){
			ApplicationEntity[] aes=null;
			if("link".equals(linkType)){
			//	aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.LINK, "networkresourcemanage");
				if("Sys_Area".equals(searchedAe.getValue("_entityType")+"")){
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
				}else{
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, "Sys_Area", AssociatedType.LINK,"networkresourcemanage");
				}
				if(aes!=null&&aes.length>0){
					for(ApplicationEntity ae:aes){
						if(ae.getValue("_entityType")!=null){
							String type=ae.getValue("_entityType").toString();
							String name="";
							if(ae.getValue("name")!=null){
								name=ae.getValue("name");
							}
							if(!(type.equals("Sys_Area") && name.equals(areaName))){
								appe=searchParentAreaResourceForSrc(ae,areaName,linkType);
							}else{
								appe=ae;
								break;
							}
						}
					}
				}
			}else{
				aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
				if(aes!=null&&aes.length>0){
					for(ApplicationEntity ae:aes){
						if(ae.getValue("_entityType")!=null){
							String type=ae.getValue("_entityType").toString();
							String name="";
							if(ae.getValue("name")!=null){
								name=ae.getValue("name");
							}
							if(!(type.equals("Sys_Area") && name.equals(areaName))){
								appe=searchParentAreaResourceForSrc(ae,areaName,linkType);
							}else{
								appe=ae;
								break;
							}
						}
					}
				}
			}
		}

		log.info("退出searchParentAreaResourceForSrc(ApplicationEntity searchedAe,String areaName,String linkType)，返回结果"+appe);

		return appe;
	}

	/**
	 * 
	 * @description: 获得未绑关系的资源
	 * @author：yuan.yw
	 * @param type
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Sep 13, 2012 6:25:23 PM
	 * @updatedate  2013-07-04
	 * @updatereason 网络资源整改
	 */
	public List<BasicEntity> getNoAssociateResource(String type){
		log.info("进入getNoAssociateResource(String type)，type="+type+",获得未绑关系的资源");
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		//String sql = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+type+" where "+type+".ENTITY_ID in(select  entityId from figurenode where  entityType='"+type+"' and id not in ( select rightId from figureline) )";
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(type,"t")+" from "+type+" t,figurenode fn where t.entity_id=fn.entityid and t.entity_type=fn.entitytype and fn.parent_figurenode_id=0";
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				type);
		if(list!=null && list.size()>0){
			log.info("退出getNoAssociateResource(String type)，返回结果"+list);
			return list;
		}else{
			log.info("退出getNoAssociateResource(String type)，返回结果null");
			return null;
		}
	}
	
	/**
	 * 根据子资源递归查询父资源List
	 * @return
	 */
	public List<Map<String,Object>> searchParentResourceForSrc(
			ApplicationEntity searchedAe,String linkType) {
		log.info("进入searchParentResourceForSrc(ApplicationEntity searchedAe,String linkType),searchedAe="+searchedAe+",linkType="+linkType+",根据子资源递归查询父资源List");
		List<Map<String,Object>> listMap  = new ArrayList<Map<String,Object>>();
		if(searchedAe!=null){
			ApplicationEntity[] aes=null;
			if("link".equals(linkType)){
				if("Sys_Area".equals(searchedAe.getValue("_entityType")+"")){
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
				}else{
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, "Sys_Area", AssociatedType.LINK,"networkresourcemanage");
				}
				if(aes!=null&&aes.length>0){
					for(ApplicationEntity ae:aes){
						listMap.add(ae.toMap());
						List<Map<String,Object>> map=searchParentResourceForSrc(
								ae,linkType);
						listMap.addAll(map);
					}
				}
			}else{
				aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
				if(aes!=null&&aes.length>0){
					for(ApplicationEntity ae:aes){
						listMap.add(ae.toMap());
						List<Map<String,Object>> map=searchParentResourceForSrc(
								ae,linkType);
						listMap.addAll(map);
					}
				}
			}
			
		}
		log.info("退出searchParentResourceForSrc(ApplicationEntity searchedAe,String linkType),返回结果"+listMap);
		return listMap;
	}
	/**
	 *取得某个资源类型是否有上级资源类型（GeneralBaseStation基站五大类，区域，站址，机房）中一个
	 * @return
	 */
	public String getDirectestParentResourceType(
			String type){
		log.info("进入getDirectestParentResourceType(String type),type="+type+",取得某个资源类型是否有上级资源类型（GeneralBaseStation基站五大类，区域，站址，机房）中一个");
		String result="";
		String[] aetNames = this.structureCommonService.getAssociatedAetName(type,AssociatedType.PARENT,"networkresourcemanage");
		if(aetNames!=null && aetNames.length>0){
			boolean hassFlag=false;
			boolean hasrFlag=false;
			boolean hasbsFlag=false;
			boolean hasaFlag=false;
			for(String aetName:aetNames){
				if(aetName.indexOf("BaseStation")>-1){
					hasbsFlag=true;
				}else if("Room".equals(aetName)){
					hasrFlag=true;
				}else if("Station".equals(aetName)){
					hassFlag=true;
				}else if("Sys_Area".equals(aetName)){
					hasaFlag=true;
				}else{
					result =  getDirectestParentResourceType(aetName);
				}
			}
			if(hasbsFlag){
				result="BaseStation";
			}else if(hasrFlag){
				result="Room";
			}else if(hassFlag){
				result="Station";
			}else if(hasaFlag){
				result="Sys_Area";
			}else{
				result="Sys_Area";
			}	
		}
		log.info("退出getDirectestParentResourceType(String type),返回结果"+result);
		return result;
	}
	
	/**
	 * 
	 * @description: 某个资源的某个类型的上级资源信息;
	 * @author：     
	 * @return void     
	 * @date：Nov 22, 2012 9:16:40 AM
	 */
	public Map<String,Object>  getDirectestParentResourceInfo(ApplicationEntity searchedAe,String entityType,String linkType){
		log.info("进入getDirectestParentResourceInfo(ApplicationEntity searchedAe,String entityType,String linkType),searchedAe="+searchedAe+",entityType="+entityType+",linkType="+linkType+",某个资源的某个类型的上级资源信息;");
		
		Map<String,Object> resultMap  = new HashMap<String,Object>();
		if(searchedAe!=null){
			ApplicationEntity[] aes=null;
			if("link".equals(linkType)){
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, entityType, AssociatedType.LINK,"networkresourcemanage");
					if(aes!=null&&aes.length>0){
						resultMap = aes[0].toMap();
					}	
			}else{
				
				if("GeneralBaseStation".equals(entityType)){
					String[] types = {"BaseStation","BaseStation_repeater","BaseStation_WLAN","BaseStation_TD","BaseStation_GSM"};
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
					if(aes!=null&&aes.length>0){
						String etype=aes[0].getValue("_entityType")+"";
						boolean flag = false;
						for(String type:types){
							if(etype.equals(type)){
								resultMap = aes[0].toMap();
								flag=true;
								break;
							}
						}
						if(!flag){
							resultMap = getDirectestParentResourceInfo(aes[0],entityType,linkType);
						}
							
					}
				}else{
					aes=this.structureCommonService.getStrutureSelationsApplicationEntity(searchedAe, AssociatedType.PARENT, "networkresourcemanage");
					if(aes!=null&&aes.length>0){
						String type=aes[0].getValue("_entityType")+"";
						if(type.equals(entityType)){
							resultMap = aes[0].toMap();
						}else{
							resultMap = getDirectestParentResourceInfo(aes[0],entityType,linkType);
						}
					}
				}
				
			}	
		}
		log.info("退出getDirectestParentResourceInfo(ApplicationEntity searchedAe,String entityType,String linkType),返回结果"+resultMap);
		return resultMap;
	}
	
	/**
	 * 更新一个表记录
	 *
	 * 
	 */
	public int updatePhysicalres(ApplicationEntity appEntity, String type,String attr) {
		log.info("进入updatePhysicalres(ApplicationEntity appEntity, String type,String attr),appEntity="+appEntity+",type="+type+",attr="+attr+",更新一个表记录");
		Context context = structureModuleLibrary.createContext();
		//Query query = context.createQueryBuilder(type);
		//query.add(Restrictions.eq(attr, appEntity.getValue(attr)));

		String sqlString = "update  "+type+" set "+ ResourceCommon.getUpdateAttributesSqlString(appEntity)+" where "+attr+"='"+appEntity.getValue(attr)+"'";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		
		log.info("退出updatePhysicalres(ApplicationEntity appEntity, String type,String attr),更新返回");
		return context.executeUpdateSQL(sc, type);
	}
	/**
	 * 
	 * @description: 获取id最大值
	 * @author：
	 * @param type
	 * @return     
	 * @return int     
	 * @date：Dec 11, 2012 9:52:33 AM
	 */
	public long getMaxIdFromTable(String type){
		log.info("进入getMaxIdFromTable(String type),type="+type+",获取id最大值");

		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		String sql = "select max(id) \"max(id)\" from "+type;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				type);
		if(list!=null && list.size()>0){
			BasicEntity be = list.get(0);
			log.info("退出getMaxIdFromTable(String type),返回结果"+be.getValue("max(id)"));
			return Long.valueOf(be.getValue("max(id)")+"");
		}else{
			log.info("退出getMaxIdFromTable(String type),返回结果0");
			return 0;
		}
	}
	/**
	 * 
	 * @description: 获得未绑关系的资源
	 * @author：
	 * @param type entityId
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Sep 13, 2012 6:25:23 PM
	 */
	public List<BasicEntity> getNoAssociateResource(String type,String entityId){
		log.info("进入getNoAssociateResource(String type,String entityId),type="+type+",entityId="+entityId+",获得未绑关系的资源");
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		String sql = "select  entityId \"entityId\" from figurenode where entityId="+entityId+" and  entityType='"+type+"' and id not in ( select rightId from figureline) ";
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				type);
		if(list!=null && list.size()>0){
			log.info("退出getNoAssociateResource(String type,String entityId),返回结果"+list);
			return list;
		}else{
			log.info("退出getNoAssociateResource(String type,String entityId),返回结果null");
			return null;
		}
	}
	/**
	 * 
	 * @description: 获取相关资源某种父资源的figurenode Id
	 * @author：
	 * @param ids
	 * @param map
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 21, 2013 9:08:23 AM
	 */
	public Map<String, Object> getSomeParentTypeNodesId(String ids,
			Map<String, Object> map, String parentType) {
		log.info("进入getSomeParentTypeNodesId(String ids,Map<String, Object> map, String parentType),ids="+ids+",map="+map+",parentType="+parentType+",获取相关资源某种父资源的figurenode Id");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		//Query q = context.createQueryBuilder(Figureline.MY_TYPE);
		//q.add(Restrictions.in("rightId",ids.split(",")));
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or rightId in("+idStr.substring(1)+")");
				}else{
					conStr.append(" rightId in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				
			}
			i++;
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+ Figureline.MY_TYPE +" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		list = context.executeSelectSQL(sc, Figureline.MY_TYPE);
		//list = context.queryList(q);
		if(list!=null && !list.isEmpty()){
			String leftId="";
			String rightId="";
			String leftType="";
			Map<String,Object> mp = new HashMap<String,Object>();
			String idsString="";
			StringBuffer sf = new StringBuffer();
			for(BasicEntity be:list){
				leftId = be.getValue("leftId");
				leftType = be.getValue("lefttype");
				rightId = be.getValue("rightId");
				if(parentType.indexOf(leftType)>=0){
					if(resultMap.containsKey(leftId)){
						resultMap.put(leftId,resultMap.get(leftId)+","+map.get(rightId));
					}else{
						resultMap.put(leftId, map.get(rightId));
					}
					
				}else{	
					if(mp.containsKey(leftId)){
						mp.put(leftId, mp.get(leftId)+","+map.get(rightId));
					}else{
						mp.put(leftId, map.get(rightId));
					}
					sf.append(","+leftId);
				}
			}
			idsString = sf.toString();
			if(mp!=null && !mp.isEmpty()){
				idsString =idsString.substring(1, idsString.length());
				Map<String,Object> searchMap = getSomeParentTypeNodesId(idsString,mp,parentType);
				if(searchMap!=null && !searchMap.isEmpty()){
					for(String key:searchMap.keySet()){
						resultMap.put(key, searchMap.get(key));
					}
				}
			}
		}
		log.info("退出getSomeParentTypeNodesId(String ids,Map<String, Object> map, String parentType),返回结果"+resultMap);
		return resultMap;
	}

	/**
	 * 
	 * @description:获取相关资源某种父资源的entity Map
	 * @author：
	 * @param ids
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 21, 2013 10:14:44 AM
	 */
	public Map<String, Object> getSomeParentTypeApplicationEntitysMapByIds(
			String ids,String currentType, String parentType) {
		log.info("进入getSomeParentTypeApplicationEntitysMapByIds(String ids,String currentType, String parentType) ,ids="+ids+",currentType="+currentType+",parentType="+parentType+",获取相关资源某种父资源的entity Map");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		//Query q = context.createQueryBuilder(currentType);
		//q.add(Restrictions.in("id",ids.split(",")));
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or id in("+idStr.substring(1)+")");
				}else{
					conStr.append(" id in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(currentType)+" from "+ currentType +" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		list = context.executeSelectSQL(sc, currentType);
		//list = context.queryList(q);
		Map<String,Object> rMp = new HashMap<String,Object>();
		ids="";
		if(list!=null && list.size()>0){
			StringBuffer sf = new StringBuffer();
			for(BasicEntity be:list){
				rMp.put(be.getValue("_entityId")+"", be.getValue("id"));
				sf.append(","+be.getValue("_entityId"));
			}
			ids = sf.toString();
		}
		ids=ids.substring(1,ids.length());
		//q = context.createQueryBuilder(Figurenode.MY_TYPE);
		//q.add(Restrictions.in("entityId",ids.split(",")));
		idArr = ids.split(",");
		idStr = new StringBuffer();
		conStr = new StringBuffer();
		i=0;
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or entityId in("+idStr.substring(1)+")");
				}else{
					conStr.append(" entityId in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+ Figurenode.MY_TYPE +" where "+conStr;
		//System.out.println("sqlString:"+sqlString);
		sc = context.createSqlContainer(sqlString);
		list = context.executeSelectSQL(sc, Figurenode.MY_TYPE);
		//list = context.queryList(q);
		if(list!=null && list.size()>0){
			String idStrings ="";
			Map<String,Object> resultMp = new HashMap<String,Object>();
			String enId = "";
			StringBuffer sf = new StringBuffer();
			for(BasicEntity be:list){
				sf.append(","+be.getValue("id"));
				enId = be.getValue("entityId")+"";
				if(rMp.containsKey(enId)){
					resultMp.put(be.getValue("id")+"",rMp.get(enId));
				}
			}
			idStrings = sf.toString();
			idStrings = idStrings.substring(1, idStrings.length());
			String parType=parentType;
			if("GeneralBaseStation".equals(parentType)){
				parType = "BaseStation,BaseStation_repeater,BaseStation_WLAN,BaseStation_TD,BaseStation_GSM";
			}
			Map<String,Object> rMap =getSomeParentTypeNodesId(idStrings,resultMp,parType);
			if(rMap!=null && !rMap.isEmpty()){
				idStrings = "";
				sf = new StringBuffer();
				for(String key : rMap.keySet()){
					sf.append(","+key);
				}
				idStrings = sf.toString();
				idStrings = idStrings.substring(1, idStrings.length());
				list = new ArrayList<BasicEntity>();
				//q = context.createQueryBuilder(Figurenode.MY_TYPE);				
				//q.add(Restrictions.in("id",idStrings.split(",")));
				//list = context.queryList(q);
				idArr = idStrings.split(",");
				idStr = new StringBuffer();
				conStr = new StringBuffer();
				i=0;
				for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
					idStr.append(","+idArr[index]);
					if(i>=30||index==idArr.length-1){
						if(!"".equals(conStr+"")){
							conStr.append(" or id in("+idStr.substring(1)+")");
						}else{
							conStr.append(" id in("+idStr.substring(1)+")");
						}
						idStr = new StringBuffer();
						//i++;
					}
					i++;
				}
				sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from "+ Figurenode.MY_TYPE +" where "+conStr;
				//System.out.println("sqlString:"+sqlString);
				sc = context.createSqlContainer(sqlString);
				list = context.executeSelectSQL(sc, Figurenode.MY_TYPE);
				if(list!=null && list.size()>0){
					Map<String,Object> tMp = new HashMap<String,Object>();
					idStrings = "";
					String id="";
					String entityId="";
					sf = new StringBuffer();
					for(BasicEntity b:list){
						id = b.getValue("id")+"";
						entityId = b.getValue("entityId")+"";
						if(rMap.containsKey(id)){
							tMp.put(entityId,rMap.get(id));
						}	
						sf.append(","+entityId);
					}
					idStrings = sf.toString();
					idStrings = idStrings.substring(1, idStrings.length());
					idArr = idStrings.split(",");
					idStr = new StringBuffer();
					conStr = new StringBuffer();
					i=0;
					for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
						idStr.append(","+idArr[index]);
						if(i>=30||index==idArr.length-1){
							if(!"".equals(conStr+"")){
								conStr.append(" or ENTITY_ID in("+idStr.substring(1)+")");
							}else{
								conStr.append(" ENTITYID in("+idStr.substring(1)+")");
							}
							idStr = new StringBuffer();
							//i++;
						}
						i++;
					}
					list = new ArrayList<BasicEntity>();
					if("GeneralBaseStation".equals(parentType)){
						String[] types = {"BaseStation","BaseStation_repeater","BaseStation_WLAN","BaseStation_TD","BaseStation_GSM"};
						for(String type:types){
							//q = context.createQueryBuilder(type);
							//q.add(Restrictions.in("_entityId",idStrings.split(",")));
							//List<BasicEntity> rlist = context.queryList(q);
							sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(type)+" from "+ type +" where "+conStr;
							//System.out.println("sqlString:"+sqlString);
							sc = context.createSqlContainer(sqlString);
							List<BasicEntity> rlist = context.executeSelectSQL(sc, Figurenode.MY_TYPE);
							if(rlist!=null&& !rlist.isEmpty()){
								list.addAll(rlist);
							}
						}
						
					}else{
						//q = context.createQueryBuilder(parentType);
						//q.add(Restrictions.in("_entityId",idStrings.split(",")));
						//list = context.queryList(q);
						sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(parentType)+" from "+ parentType +" where "+conStr;
						sc = context.createSqlContainer(sqlString);
						List<BasicEntity> rlist = context.executeSelectSQL(sc, parentType);
					}
					if(list!=null && list.size()>0){
						for(BasicEntity by:list){
							entityId = by.getValue("_entityId")+"";
							if(tMp.containsKey(entityId)){
								String tIds = tMp.get(entityId)+"";
								String[] iArray = null;
								if(tIds.indexOf(",")>=0){
									iArray = tIds.split(",");
									for(String iStr: iArray){
										resultMap.put(iStr,by.toMap());
									}
								}else{
									resultMap.put(tIds,by.toMap());
								}
							}
						}
					}
				}
			}
			
		}else{
			resultMap=null;
		}
		log.info("退出getSomeParentTypeApplicationEntitysMapByIds(String ids,String currentType, String parentType) ,返回结果"+resultMap);

		return resultMap;
		
	}

	/**
	 * 
	 * @description:查询资源对应的上级figurenode id map信息
	 * @author：
	 * @param ids
	 * @param currentType
	 * @param parentType
	 * @return     
	 *      
	 * @date：Jul 4, 2013 11:47:40 AM
	 * @updatedate 2013-07-04
	 * @updatereason 网络资源整改
	 */
	public Map<String, Object> getSomeParentTypeIdsMapByIds(String ids,
			String currentType, String parentType) {
		log.info("进入getSomeParentTypeApplicationEntitysMapByIds(String ids,String currentType, String parentType) ,ids="+ids+",currentType="+currentType+",parentType="+parentType+",获取资源实例父资源实例Id Map");
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Context context = structureModuleLibrary.createContext();
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or c.id in("+idStr.substring(1)+")");
				}else{
					conStr.append("c.id in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sql = "";
		if(parentType.equals("Sys_Area")){
			sql = "select  p.area_id \"id\" from figurenode fn,"+currentType+" c,figurenode f,"+parentType+" p where "+conStr+" and f.id=fn.parent_figurenode_id and f.entityid=p.entity_id and fn.entityId=c.ENTITY_ID  ";	
		}else{
			sql = "select  p.id \"id\" from figurenode fn,"+currentType+" c,figurenode f,"+parentType+" p where "+conStr+" and f.id=fn.parent_figurenode_id and f.entityid=p.entity_id and fn.entityId=c.ENTITY_ID  ";
		}
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		if(list!=null && list.size()>0){
			for(BasicEntity be:list){
				if(!resultMap.containsKey(be.getValue("id")+"")){
					resultMap.put(be.getValue("id")+"",be.getValue("id"));
				}
			}
		}
		
		if(resultMap.isEmpty()){
			log.info("进入getSomeParentTypeIdsMapByIds(String ids,String currentType, String parentType),返回结果null");
			return null;
		}else{
			log.info("进入getSomeParentTypeIdsMapByIds(String ids,String currentType, String parentType),返回结果"+resultMap);
			return resultMap;
		}
		
	}


	public int insertEntitysToDataTable(String entityType, String sql) {
		log.info("进入insertEntitysToDataTable(String entityType, String sql)，entityType="+entityType+",sql="+sql+",更新插入数据");
		int status = 0;
		
		Connection connection = DataSourceConn.initInstance().getConnection();  //new Conn().getConnection();
		Statement state = null;
		try {
			state = connection.createStatement();
			status = state.executeUpdate(sql);
			log.info("退出insertEntitysToDataTable(String entityType, String sql)，更新数据成功");
		} catch (Exception ex) {
			log.error("退出insertEntitysToDataTable(String entityType, String sql)，更新数据发生异常");
			ex.printStackTrace();
		} finally {
			try {
				state.close();
				connection.close();
			} catch (Exception ex) {
				log.error("退出insertEntitysToDataTable(String entityType, String sql)，关闭数据库连接失败");
			}

		}
		
		return status;
		
	}

	/**
	 * 
	 * @description: 获取资源id 和figurenode id 对应map 信息
	 * @author：
	 * @param mp
	 * @param parentType
	 * @return     
	 *      
	 * @date：Jul 4, 2013 1:53:01 PM
	 * @updateauthor yuan.yw
	 * @updatedate 2013-07-14
	 * @updatereason 增加或取到 pathMap
	 */
	public List<Map<String,Object>> getNodeIdsAndParentNodeIdsMap(
			Map<String, Object> mp, String parentType) {
		log.info("进入getNodeIdsAndParentNodeIdsMap(Map<String, Object> mp, String parentType)，mp="+mp+",parentType="+parentType+",获取资源实例父资源实例Id Map");
		StringBuffer sf = new StringBuffer();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMap = null;
		Map<String,Object> parNameMap = null;
		Map<String,Object> pathMap = null;
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		for(String key:mp.keySet()){
			sf.append(","+key);
		}
		String ids = sf.substring(1, sf.length()).toString();
		Context context = structureModuleLibrary.createContext();
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		String idName="";
		if(parentType.equals("Sys_Area")){
			idName="area_id";
		}else{
			idName="id";
		}
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or "+idName+" in("+idStr.substring(1)+")");
				}else{
					conStr.append(" "+idName+" in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(parentType)+" from "+parentType+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,parentType);
		if(list!=null && list.size()>0){
			Map<String,Object> rp = new HashMap<String,Object>();
			String id = "";
			String entityId = "";
			sf = new StringBuffer();
			Map<String,Object> parMap = new HashMap<String,Object>();
			for(BasicEntity be:list){
				id = be.getValue(idName)+"";
				entityId = be.getValue("_entityId");
				if(mp.containsKey(id)){
					rp.put(entityId,mp.get(id));
				}
				sf.append(","+entityId);
				if(be.getValue("name")!=null){
					parMap.put(entityId, be.getValue("name"));
				}else{
					parMap.put(entityId, be.getValue("label"));
				}
			}
			if(!rp.isEmpty()){
				ids = sf.substring(1, sf.length()).toString();
				context = structureModuleLibrary.createContext();
				idArr = ids.split(",");
				idStr = new StringBuffer();
				conStr = new StringBuffer();
				i=0;
				for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
					idStr.append(","+idArr[index]);
					if(i>=30||index==idArr.length-1){
						if(!"".equals(conStr+"")){
							conStr.append(" or entityId in("+idStr.substring(1)+")");
						}else{
							conStr.append(" entityId in("+idStr.substring(1)+")");
						}
						idStr = new StringBuffer();
						//i++;
					}
					i++;
				}
				sql = "select id \"id\",entityId \"entityId\",path \"path\" from figurenode where "+conStr;
				//System.out.println("sqlString:"+sql);
				sqlContainer = context.createSqlContainer(sql);
				list = context.executeSelectSQL(sqlContainer,Figurenode.MY_TYPE);
				
				if(list!=null && !list.isEmpty()){
					 resultMap = new HashMap<String,Object>();
					 parNameMap = new HashMap<String,Object>();
					 pathMap = new HashMap<String,Object>();
					for(BasicEntity b:list){
						id = b.getValue("id")+"";
						entityId = b.getValue("entityId");
						if(rp.containsKey(entityId)){
							String str = rp.get(entityId)+"";
							String[] arr = str.split(",");
							for(String s:arr){
								resultMap.put(s, id);
								pathMap.put(s, b.getValue("path"));
							}
						}
						if(parMap.containsKey(entityId)){
							parNameMap.put(id, parMap.get(entityId));
						}
					}
				}
			}
		}
		
		resultList.add(resultMap);
		resultList.add(parNameMap);
		resultList.add(pathMap);
		log.info("退出getNodeIdsAndParentNodeIdsMap(Map<String, Object> mp, String parentType)，返回结果"+resultList);
		return resultList;
	}
	
	/**
	 * 
	 * @description: sql语句更新entity String
	 * @author：
	 * @return     
	 * @return int     
	 * @date：Sep 26, 2012 5:18:34 PM
	 */
	public String updateApplicationSql(ApplicationEntity app,ApplicationModule module){
		log.info("进入updateApplicationSql(ApplicationEntity app,ApplicationModule module)，app="+app+",module="+module+",sql语句更新entity String");
		String updateString = "";
		Map<String, Object> moduleMap = module.toMap();
		
		for(String key:moduleMap.keySet()){
			if(app.containKey(key)){
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
						updateString += key+"='"+v+"',";
					}
					
				}else{
					updateString += key+"="+app.getValue(key)+",";
				}
			}
			/*else{
				updateString += key+"="+null+",";
			}*/
		}
		updateString=updateString.substring(0,updateString.lastIndexOf(","));
		String sql = "update "+app.getValue("_entityType")+" set "+updateString+" where id="+app.getValue("id");
		//System.out.println("sqlString:"+sql);
		log.info("退出updateApplicationSql(ApplicationEntity app,ApplicationModule module)，返回结果sql="+sql);
		return sql;
	}
	
	public Map<String,Object> getApplicationEntityMap(Map<String,Object> updateIdMap,String currentType){
		log.info("进入getApplicationEntityMap(Map<String,Object> updateIdMap,String currentType)，updateIdMap="+updateIdMap+",currentType="+currentType+",获取资源实例 map");
		StringBuffer sf = new StringBuffer();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		for(String key:updateIdMap.keySet()){
			sf.append(","+key);
		}
		String ids = sf.substring(1,sf.length())+"";
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or id in("+idStr.substring(1)+")");
				}else{
					conStr.append(" id in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(currentType)+" from "+currentType+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				currentType);
		if(list!=null && list.size()>0){
			for(BasicEntity be:list){
				String id = be.getValue("id")+"";
				if(updateIdMap.containsKey(id)){
					resultMap.put(id, ApplicationEntity.changeFromEntity(be));
				}
			}
		}
		if(resultMap.isEmpty()){
			log.info("退出getApplicationEntityMap(Map<String,Object> updateIdMap,String currentType)，返回结果null");
			return null;
		}else{
			log.info("退出getApplicationEntityMap(Map<String,Object> updateIdMap,String currentType)，返回结果"+resultMap);
			return resultMap;
		}
	}

	
	public Map<String,Object> getFigureNodeIdMap(Map<String,Object> updateIdMap,String currentType){
		log.info("进入getFigureNodeIdMap(Map<String,Object> updateIdMap,String currentType)，updateIdMap="+updateIdMap+",currentType="+currentType+",获取资源实例 figureId map");
		
		StringBuffer sf = new StringBuffer();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		for(String key:updateIdMap.keySet()){
			sf.append(","+key);
		}
		String ids = sf.substring(1,sf.length())+"";
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		String idName = "id";
		if(currentType.equals("Sys_Area")){
			idName = "area_id";
		}else{
			idName = "id";
		}
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or "+idName+" in("+idStr.substring(1)+")");
				}else{
					conStr.append(" "+idName+" in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(currentType)+" from "+currentType+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				currentType);
		if(list!=null && list.size()>0){
			Map<String,Object> rMap = new HashMap<String,Object>();
			sf = new StringBuffer();
			for(BasicEntity be:list){
				String entityId= be.getValue("_entityId")+"";
				String id = be.getValue("id")+"";
				if(updateIdMap.containsKey(id)){
					rMap.put(entityId, updateIdMap.get(id));
				}
				sf.append(","+entityId);
			}
			ids = sf.substring(1,sf.length())+"";
			idArr = ids.split(",");
			idStr = new StringBuffer();
			conStr = new StringBuffer();
			i=0;
			for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
				idStr.append(","+idArr[index]);
				if(i>=30||index==idArr.length-1){
					if(!"".equals(conStr+"")){
						conStr.append(" or entityId in("+idStr.substring(1)+")");
					}else{
						conStr.append("  entityId in("+idStr.substring(1)+")");
					}
					idStr = new StringBuffer();
					//i++;
				}
				i++;
			}
			sql="select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from figurenode where "+conStr;
			//System.out.println("sqlString:"+sql);
			sqlContainer = context.createSqlContainer(sql);
			list = context.executeSelectSQL(sqlContainer,
					Figurenode.MY_TYPE);
			if(list!=null && list.size()>0){
				for(BasicEntity be:list){
					String entityId= be.getValue("entityId")+"";
					String id = be.getValue("id")+"";
					if(rMap.containsKey(entityId)){
						resultMap.put(rMap.get(entityId)+"", id);
					}
				}
			}	
			
		}
		if(resultMap.isEmpty()){
			log.info("退出getFigureNodeIdMap(Map<String,Object> updateIdMap,String currentType)，返回结果null");
			return null;
		}else{
			log.info("退出getFigureNodeIdMap(Map<String,Object> updateIdMap,String currentType)，返回结果"+resultMap);
			return resultMap;
		}
	}

	
	/**
	 * 
	 * @description: 根据figurenode Id map信息获取资源父级资源的相关信息map
	 * @author：
	 * @param fnIdMap
	 * @param assType
	 * @return     
	 *      
	 * @date：Jul 4, 2013 3:27:14 PM
	 * @updatedate 2013-07-04
	 * @update 网络资源整改
	 */
	public Map<String,Object> getFigureNodeIdParInfoMap(Map<String,Object> fnIdMap,String assType){
		log.info("进入getFigureNodeIdParInfoMap(Map<String,Object> fnIdMap,String assType)，fnIdMap="+fnIdMap+",assType="+assType+",获取父资源实例 figureId map");
		StringBuffer sf = new StringBuffer();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		for(String key:fnIdMap.keySet()){
			sf.append(","+key);
		}
		String ids = sf.substring(1,sf.length())+"";
		String[] idArr = ids.split(",");
		StringBuffer idStr = new StringBuffer();
		StringBuffer conStr = new StringBuffer();
		int i=0;
		String tableIdAlias = "fn.id";
		if(!assType.equals("CLAN")){
			tableIdAlias = "rightid";
		}
		for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
			idStr.append(","+idArr[index]);
			if(i>=30||index==idArr.length-1){
				if(!"".equals(conStr+"")){
					conStr.append(" or "+tableIdAlias+" in("+idStr.substring(1)+")");
				}else{
					conStr.append(" "+tableIdAlias+" in("+idStr.substring(1)+")");
				}
				idStr = new StringBuffer();
				//i++;
			}
			i++;
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from figureline  where "+conStr+" and linkType='"+assType+"'";
		if(assType.equals("CLAN")){
			sql = "select f.id \"leftId\",f.entitytype \"lefttype\",fn.id \"rightId\" from figurenode f,figurenode fn  where "+conStr+" and fn.parent_figurenode_id=f.id";
		}
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		if(list!=null && list.size()>0){
			Map<String,Object> rMap = new HashMap<String,Object>();
			Map<String,String> parTypeMap = new HashMap<String,String>();
			sf = new StringBuffer();
			for(BasicEntity be:list){
				String  leftId= be.getValue("leftId")+"";
				String rightId = be.getValue("rightId")+"";
				String parentType = be.getValue("lefttype");
				if(!parTypeMap.containsKey(parentType)){
					parTypeMap.put(parentType, parentType);
				}
				if(fnIdMap.containsKey(rightId)){
					if(rMap.containsKey(leftId)){
						rMap.put(leftId,rMap.get(leftId)+","+rightId);
					}else{
						rMap.put(leftId, rightId);
					}
					
				}
				sf.append(","+leftId);
			}
			ids = sf.substring(1,sf.length())+"";
			idArr = ids.split(",");
			idStr = new StringBuffer();
			conStr = new StringBuffer();
			i=0;
			for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
				idStr.append(","+idArr[index]);
				if(i>=30||index==idArr.length-1){
					if(!"".equals(conStr+"")){
						conStr.append(" or id in("+idStr.substring(1)+")");
					}else{
						conStr.append(" id in("+idStr.substring(1)+")");
					}
					idStr = new StringBuffer();
					//i++;
				}
				i++;
			}
			sql="select "+ResourceCommon.getSelectSqlAttributsString(Figurenode.MY_TYPE)+" from figurenode where "+conStr;
			//System.out.println("sqlString:"+sql);
			sqlContainer = context.createSqlContainer(sql);
			list = context.executeSelectSQL(sqlContainer,
					Figurenode.MY_TYPE);
			if(list!=null && list.size()>0){
				Map<String,Object> rMap1= new HashMap<String,Object>();
				sf = new StringBuffer();
				for(BasicEntity be:list){
					String entityId= be.getValue("entityId")+"";
					String id = be.getValue("id")+"";
					if(rMap.containsKey(id)){
						if(!rMap1.containsKey(id)){
							rMap1.put(entityId, rMap.get(id));
						}
					}
					sf.append(","+entityId);
				}
				ids = sf.substring(1,sf.length())+"";
				idArr = ids.split(",");
				idStr = new StringBuffer();
				conStr = new StringBuffer();
				i=0;
				for(int index=0;index<idArr.length;index++){//oracle 字符长度超过1000处理
					idStr.append(","+idArr[index]);
					if(i>=30||index==idArr.length-1){
						if(!"".equals(conStr+"")){
							conStr.append(" or ENTITY_ID in("+idStr.substring(1)+")");
						}else{
							conStr.append(" ENTITY_ID in("+idStr.substring(1)+")");
						}
						idStr = new StringBuffer();
						//i++;
					}
					i++;
				}
				for(String parType:parTypeMap.keySet()){
					sql = "select "+ResourceCommon.getSelectSqlAttributsString(parType)+" from "+parType+" where "+conStr;
					//System.out.println("sqlString:"+sql);
					sqlContainer = context.createSqlContainer(sql);
					list = context.executeSelectSQL(sqlContainer,
							parType);
					if(list!=null && list.size()>0){
						for(BasicEntity be:list){
							String entityId= be.getValue("_entityId")+"";
							String name = "";
							if(be.getValue("name")!=null){
								name = be.getValue("name")+"#"+parType;
							}else{
								name = be.getValue("label")+"#"+parType;
							}
							if(rMap1.containsKey(entityId)){
								String fIds=rMap1.get(entityId)+"";
								for(String fId:fIds.split(",")){
									resultMap.put(fId,name);
								}
								
							}

						}
					}
				}
			}	
			
		}
		if(resultMap.isEmpty()){
			log.info("退出getFigureNodeIdParInfoMap(Map<String,Object> fnIdMap,String assType)，返回结果null");
			return null;
		}else{
			log.info("退出getFigureNodeIdParInfoMap(Map<String,Object> fnIdMap,String assType)，返回结果"+resultMap);
			return resultMap;
		}
	}

	
	public String getSearchSqlString(ApplicationEntity searchedAe,
			Map<String, String> dateMap) {
		log.info("进入getSearchSqlString(ApplicationEntity searchedAe, Map<String,String> dateMap)，searchedAe="+searchedAe+",dateMap="+dateMap+",获取搜索条件Sql String");		

		if(searchedAe == null) {
			log.info("退出getSearchSqlString(ApplicationEntity searchedAe, Map<String,String> dateMap)，返回结果null");		
			return null;
		}
		StringBuffer result = new StringBuffer();
		
		int conditionFlag=0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String key : searchedAe.keyset()) {
			if (searchedAe.getValue(key) != null && !"".equals(searchedAe.getValue(key))) {
				String value = searchedAe.getValue(key)+"";
				value=value.trim();
				if(value!=null && value!=""){
					if("_entityId".equals(key) || "_entityType".equals(key)) {
						continue;
					} else {
						
						if(searchedAe.getValue(key).getClass().getName().indexOf("String") > -1) {
							//当属性为String类型时，进行模糊查询
							//if((searchedAe.getValue(key) + "").indexOf("%") > -1) {
								//条件值存在%，不去做前后模糊查询
								//query.add(Restrictions.like(key, searchedAe.getValue(key)));
							//} else {
								//条件值存在%，进行模糊查询
								//query.add(Restrictions.like(key, "%" + searchedAe.getValue(key) + "%"));

							result.append(" and instr(stype."+key+",'"+value+"')>0");
								
							//}
						} else if(searchedAe.getValue(key).getClass().getName().indexOf("Date") > -1){
							//当属性为其他类型时，进行精确查询
							//query.add(Restrictions.eq(key, searchedAe.getValue(key)));
							
							result.append(" and "+key+"=to_date('"+sdf.format(value)+"','yyyy-mm-dd hh24:mi:ss')");
							
						}else{
							
							result.append(" and "+key+"='"+value+"'");
							
						}
					}
				}
			}
		}

		//当属性为日期类型时特殊处理
		if(dateMap!=null&&!dateMap.isEmpty()){
			for(String k:dateMap.keySet()){
				String dates= dateMap.get(k).toString();
				String date1= dates.substring(0,dates.indexOf("/"));
				String date2 = dates.substring(dates.indexOf("/")+1,dates.length());
				if((date1!=null && !"".equals(date1)) || (date2!=null && !"".equals(date2)) ){
					conditionFlag=conditionFlag+1;
				}
				if(date1!=null&&!"".equals(date1)){

					result.append(" and stype."+k+">=to_date('"+date1+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					
				}
				if(date2!=null&&!"".equals(date2)){
					result.append(" and stype."+k+"<=to_date('"+date2+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					
				}
			}	
			
		}
		String s = result+"";
		/*if(s!=null&&!"".equals(s)){
			s = s;
		}*/
		log.info("退出getSearchSqlString(ApplicationEntity searchedAe, Map<String,String> dateMap)，返回结果result="+s);
		return s;
		
	}

	
	public String getFigureNodeIdById(String id, String type) {
		log.info("进入getFigureNodeIdById(String id, String type)，id="+id+",type="+type+",获取资源实例 figurenode Id");
		String idName = "id";
		if(type.equals("Sys_Area")){
			idName = "area_id";
		}else{
			idName = "id";
		}
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context context = structureModuleLibrary.createContext();
		String sql = "select id \"id\" from figurenode where entityId in(select ENTITY_ID from "+type+" where "+type+"."+idName+"="+id+")";
		//System.out.println(sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		list = context.executeSelectSQL(sqlContainer,
				Figurenode.MY_TYPE);
		if(list!=null && list.size()>0){
			String resultId = list.get(0).getValue("id")+"";
			log.info("退出getFigureNodeIdById(String id, String type)，返回结果:"+resultId );
			return resultId ;
		}else{
			log.info("退出getFigureNodeIdById(String id, String type)，返回结果null");
			return null;
		}
	}

	/**
	 * 
	 * @description: 据sql分页查询资源
	 * @author：yuan.yw
	 * @param type
	 * @param sql
	 * @param pageSize
	 * @param pageIndex
	 * @return     
	 * @return List<Map<String,Object>>
	 * @date：Feb 21, 2013 9:43:57 AM
	 * @updatedate:2013-07-2
	 * @updatereason 网络资源整改
	 */
	public List<Map<String,Object>> searchResourceBySql(String type,String sql,String countSql,String pageSize,String pageIndex,String orderType){
		log.info("进入searchResourceBySql(String type,String sql,String pageSize,String pageIndex),type="+type+",sql="+sql+",pageSize="+pageSize+",pageIndex="+pageIndex+",据sql分页查询资源");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Context context = structureModuleLibrary.createContext();
		String sqlString = sql;
		if(pageSize!=null && !"".equals(pageSize)&&!"0".equals(pageSize)){
			//int curRecord = Integer.valueOf(pageIndex)-1;
			int curRecord = Integer.valueOf(pageIndex);
			//curRecord=curRecord*Integer.parseInt(pageSize);
			//sql +=" limit "+curRecord+","+pageSize;
			if(orderType!=null && !"".equals(orderType)){
				sqlString ="select t.* from ("+sql+"  order by stype."+orderType+") t  where rc>"+((curRecord-1)*Integer.parseInt(pageSize))+" and rc<="+((curRecord)*Integer.parseInt(pageSize));
			}else{
				sqlString ="select t.* from ("+sql+") t  where rc>"+((curRecord-1)*Integer.parseInt(pageSize)) +" and rc<="+((curRecord)*Integer.parseInt(pageSize));
				
			}
		}else{
			if(orderType!=null && !"".equals(orderType)){
				sqlString =sqlString + " order by stype."+orderType+"";				
			}
		}
		//System.out.println(sqlString);
		SqlContainer sqlContainer = context.createSqlContainer(sqlString);
		List<BasicEntity> resultList = context.executeSelectSQL(sqlContainer,
				type);
		sqlContainer = context.createSqlContainer(countSql);
		List<BasicEntity> countList = context.executeSelectSQL(sqlContainer,
				type);
		if(resultList!=null && !resultList.isEmpty()){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("entityList", resultList);
			for(BasicEntity be:countList){
				map.put("count", be.getValue("count"));
			}
			list.add(map);
		}
		if(list!=null && list.size()>0){
			log.info("退出searchResourceBySql(String type,String sql,String pageSize,String pageIndex)，返回结果:"+list );
			return list ;
		}else{
			log.info("退出searchResourceBySql(String type,String sql,String pageSize,String pageIndex)，返回结果null");
			return null;
		}
	}

	/**
	 * 
	 * @description: 根据id及类型获取父资源
	 * @author：yuan.yw
	 * @param childId
	 * @param childType
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 3, 2013 10:51:27 AM
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildType(long childId, String childType){
		return this.networkResourceManageDao.getParentResourceByChildIdAndChildType(childId, childType);
	}
	/**
	 * 
	 * @description: 根据父级资源集合获取其所属指定类型资源集合
	 * @author：yuan.yw
	 * @param parentIds
	 * @param parentType
	 * @param resourceType
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 3, 2013 10:52:41 AM
	 */
	public List<BasicEntity> getResourceByResourceType(List<Long> parentIds,String parentType,String resourceType){
		return this.networkResourceManageDao.getResourceByResourceType(parentIds, parentType, resourceType);
	}

	
	
}


