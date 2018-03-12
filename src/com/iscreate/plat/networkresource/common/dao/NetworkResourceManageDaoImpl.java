package com.iscreate.plat.networkresource.common.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.structure.instance.Structure;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleLibrary;

public class NetworkResourceManageDaoImpl implements NetworkResourceManageDao {
	
	private StructureModuleLibrary structureModuleLibrary;
	
	
//	private List<Map<String, Object>> findQueryByHibernate(final String sql){
//		List<Map<String, Object>> list =  this.hibernateTemplate
//		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
//			public List<Map<String, Object>> doInHibernate(Session session)
//					throws HibernateException, SQLException {
//						List<Map<String, Object>> find = null;
//							SQLQuery query = session.createSQLQuery(sql);
//							//org.hibernate.Query query = session.createQuery(sql);
//							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//							find = query.list();
//				return find;
//			}
//		});
//		return list;
//	}
	

	/**
	 * 根据父级资源获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByResourceType(long parentId,String parentType,String resourceType){
		Context context = structureModuleLibrary.createContext();
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+"   and fn.path like '%/"+parentType+"/"+parentId+"/%' ";

		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
		//List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		//return findQueryByHibernate;
	}
	
	
	/**
	 * 根据父级资源集合获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentIds
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByResourceType(List<Long> parentIds,String parentType,String resourceType){
		Context context = structureModuleLibrary.createContext();
		String whereSql ="";
		if(parentIds != null){
			whereSql ="  and ( ";
			for(long pa:parentIds){
				String s = " fn.path like '%/"+parentType+"/"+pa+"/%' or";
				if(whereSql.indexOf(s) >= 0){
					continue;
				}else{
					whereSql = whereSql + " fn.path like '%/"+parentType+"/"+pa+"/%' or";
				}
			}
			whereSql = whereSql.substring(0, whereSql.length() - 2) + " ) ";
		}else{
			return null;
		}
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+whereSql;

		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
		//List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		//return findQueryByHibernate;
	}
	
	/**
	 * 根据父级资源获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByResourceType(long parentId,String parentType,String resourceType){
		String idName = "";
		if(resourceType != null){
			if(resourceType.equals("Sys_Area")){
				idName = "area_id";
			}else{
				idName = "id";
			}
		}else{
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+"   and fn.path like '%/"+parentType+"/"+parentId+"/'||'"+resourceType+"/'||re."+idName+"||'/' ";
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	
	/**
	 * 根据父级资源获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByResourceType(List<Long> parentIds,String parentType,String resourceType){
		String idName = "";
		if(resourceType != null){
			if(resourceType.equals("Sys_Area")){
				idName = "area_id";
			}else{
				idName = "id";
			}
		}else{
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String whereSql ="";
		if(parentIds != null){
			whereSql ="  and ( ";
			for(long pa:parentIds){
				whereSql = whereSql + " fn.path like '%/"+parentType+"/"+pa+"/'||'"+resourceType+"/'||re."+idName+"||'/' or";
			}
			whereSql = whereSql.substring(0, whereSql.length() - 2) + " ) ";
		}else{
			return null;
		}
		if(parentType == null || parentType.equals("")){
			//父级资源类型为空
			return null;
		}
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+whereSql;

		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	
	
	/**
	 * 根据指定区域获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByAreaAndResourceType(long areaId,String resourceType){
		Context context = structureModuleLibrary.createContext();
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+"   and fn.area_path like '%/"+areaId+"/' ";
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
//	/**
//	 * 根据指定区域获取其所属指定类型资源集合
//	* @author ou.jh
//	* @date Jun 21, 2013 2:04:20 PM
//	* @Description: TODO 
//	* @param @param areaId
//	* @param @param resourceType
//	* @param @return        
//	* @throws
//	 */
//	public List<BasicEntity> getResourceByAreaAndResourceType(long areaId,String resourceType){
//		Context context = structureModuleLibrary.createContext();
//		if(resourceType == null || resourceType.equals("")){
//			//目标资源类型为空
//			return null;
//		}
//		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
//						+"  from "+resourceType+" re, Figurenode fn "
//						+" where re.entity_id = fn.entityid "
//						+" and re.entity_type = fn.entitytype "
//						+"   and fn.area_path like '%/"+areaId+"/%' ";
//		SqlContainer sc = context.createSqlContainer(sql);
//		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
//		return list;
//		//hib写法
////		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
////		return findQueryByHibernate;
//	}
	
	/**
	 * 根据指定多个区域获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByAreasAndResourceType(List<Long> areaIds,String resourceType){
		Context context = structureModuleLibrary.createContext();
		if(resourceType == null || resourceType.equals("")){
			//目标资源类型为空
			return null;
		}
		String areaId = "";
		if(areaIds != null && areaIds.size() > 0){
			areaId = " and ( ";
			for(long a:areaIds){
				areaId = areaId + " fn.area_path like '%/"+a+"/%' or";
			}
			areaId = areaId.substring(0, areaId.length() - 2) + " ) ";
		}else{
			//区域ID为空
			return null;
		}
		String sql = " select "+ResourceCommon.getSelectSqlAttributsString(resourceType,"re")+" "
						+"  from "+resourceType+" re, Figurenode fn "
						+" where re.entity_id = fn.entityid "
						+" and re.entity_type = fn.entitytype "
						+ areaId;
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, resourceType);
		return list;
		//hib写法
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	
	/**
	 * 根据子级资源获取父级资源
	* @author ou.jh
	* @date Jun 21, 2013 4:29:49 PM
	* @Description: TODO 
	* @param @param childId
	* @param @param childType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildType(long childId ,String childType){
		String idName = "id";
		if(childType !=null && childType.equals("Sys_Area")){
			idName = "area_id";
		}
		Context context = structureModuleLibrary.createContext();
		if(childType == null || childType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select substr(fn.path, "
					 +"             instr(fn.path, '/', -1, 5),length(fn.path)-instr(fn.path, '/', -1, 5)+1) \"path\" "
					 +"  from "+childType+" re, Figurenode fn "
					 +" where re.entity_id = fn.entityid "
					 +"   and re.entity_type = fn.entitytype "
					 +"   and re."+idName+" = "+childId;
		//根据获取到的资源父级路径简析父级资源的资源ID与资源类型
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> findQueryByHibernate=context.executeSelectSQL(sc, childType);
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		if(findQueryByHibernate != null && findQueryByHibernate.size() > 0){
			BasicEntity be = findQueryByHibernate.get(0);
			Object object = be.getValue("path");
			if(object != null && !object.equals("")){
				//简析父级资源的资源ID与资源类型
				String path = object + "";
				//父级资源类型
				String parenetType = path.substring(1, path.indexOf('/',2));
				//父级资源ID
				String parenetId = path.substring(path.indexOf('/',2) + 1, path.indexOf('/',path.indexOf('/',2)+1));
				if(parenetId != null && !parenetId.equals("")){
					long parseParenetId = Long.parseLong(parenetId);
					//根据资源ID与资源类型父级资源实例
					List<BasicEntity> resourceByIdAndReType = getResourceByIdAndReType(parseParenetId, parenetType);
					return resourceByIdAndReType;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
//	/**
//	 * 根据子级区域获取父级区域
//	* @author ou.jh
//	* @date Jun 21, 2013 4:29:49 PM
//	* @Description: TODO 
//	* @param @param childId
//	* @param @param childType
//	* @param @return        
//	* @throws
//	 */
//	public List<BasicEntity> getParentAreaByChildIdAndChildType(long childId ,String childType){
//		Context context = structureModuleLibrary.createContext();
//		if(childType == null || childType.equals("")){
//			//目标资源类型为空
//			return null;
//		}
//		String sql = " select substr(fn.path, "
//					 +"             instr(fn.path, '/', -1, 2)) \"path\" "
//					 +"  from "+childType+" re, Figurenode fn "
//					 +" where re.entity_id = fn.entityid "
//					 +"   and re.entity_type = fn.entitytype "
//					 +"   and re.id = "+childId;
//		//根据获取到的资源父级路径简析父级资源的资源ID与资源类型
//		SqlContainer sc = context.createSqlContainer(sql);
//		List<BasicEntity> findQueryByHibernate=context.executeSelectSQL(sc, childType);
////		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		if(findQueryByHibernate != null && findQueryByHibernate.size() > 0){
//			BasicEntity be = findQueryByHibernate.get(0);
//			Object object = be.getValue("path");
//			if(object != null && !object.equals("")){
//				//简析父级资源的资源ID与资源类型
//				String path = object + "";
//				//父级区域ID
//				String parenetId = path.substring(1, path.indexOf('/',2));
//				if(parenetId != null && !parenetId.equals("")){
//					long parseParenetId = Long.parseLong(parenetId);
//					//根据资源ID与资源类型父级资源实例
//					List<BasicEntity> resourceByIdAndReType = getResourceByIdAndReType(parseParenetId, "Sys_Area");
//					return resourceByIdAndReType;
//				}else{
//					return null;
//				}
//			}else{
//				return null;
//			}
//		}else{
//			return null;
//		}
//	}
	
	/**
	 * 根据子级资源获取父级资源
	* @author ou.jh
	* @date Jun 21, 2013 4:29:49 PM
	* @Description: TODO 
	* @param @param childId
	* @param @param childType
	* @param @param ParentType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildTypeAndParentType(long childId ,String childType,String parentType){
		String idName = "id";
		if(childType !=null && childType.equals("Sys_Area")){
			idName = "area_id";
		}
		Context context = structureModuleLibrary.createContext();
		if(childType == null || childType.equals("")){
			//目标资源类型为空
			return null;
		}
		String sql = " select substr(fn.path, "
					 +"             instr(fn.path, '/', -1, 5),length(fn.path)-instr(fn.path, '/', -1, 5)+1) \"path\" "
					 +"  from "+childType+" re, Figurenode fn "
					 +" where re.entity_id = fn.entityid "
					 +"   and re.entity_type = fn.entitytype "
					 +"   and re."+idName+" = "+childId;
		//根据获取到的资源父级路径简析父级资源的资源ID与资源类型
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> findQueryByHibernate=context.executeSelectSQL(sc, childType);
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
		if(findQueryByHibernate != null && findQueryByHibernate.size() > 0){
			BasicEntity be = findQueryByHibernate.get(0);
			Object object = be.getValue("path");
			if(object != null && !object.equals("")){
				//简析父级资源的资源ID与资源类型
				String path = object + "";
				if(getStrCharSize(path, "/") < 5){
					return null;
				}
				//父级资源类型
				String pType = path.substring(1, path.indexOf('/',2));
				if(pType == null || !pType.equals(parentType)){
					return null;
				}
				//父级资源ID
				String parenetId = path.substring(path.indexOf('/',2) + 1, path.indexOf('/',path.indexOf('/',2)+1));
				if(parenetId != null && !parenetId.equals("")){
					long parseParenetId = Long.parseLong(parenetId);
					//根据资源ID与资源类型父级资源实例
					List<BasicEntity> resourceByIdAndReType = getResourceByIdAndReType(parseParenetId, pType);
					return resourceByIdAndReType;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	/**
	 * 根据资源ID与资源类型获取资源实例
	* @author ou.jh
	* @date Jun 21, 2013 4:45:21 PM
	* @Description: TODO 
	* @param @param id
	* @param @param reType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByIdAndReType(long reId , String reType){
		String idName = "id";
		if(reType !=null && reType.equals("Sys_Area")){
			idName = "area_id";
		}else if(reId==0 || reType==null || "".equals(reType)){//yuan.yw 2013-8-13 参数检查判断
			return null;
		}
		Context context = structureModuleLibrary.createContext();
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(reType,"re")+" from "+reType+" re where re."+idName+" = "+reId;
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, reType);
		return list;
		//hib
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	
	/**
	 * 根据资源类型获取资源实例
	* @author ou.jh
	* @date Jun 21, 2013 4:45:21 PM
	* @Description: TODO 
	* @param @param id
	* @param @param reType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByReType(String reType){
		Context context = structureModuleLibrary.createContext();
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(reType,"re")+" from "+reType+" re ";
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, reType);
		return list;
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	/**
	 * 根据资源ID与资源类型获取指定类型的关联关系资源（大部分为逻辑网资源）
	* @author ou.jh
	* @date Jun 25, 2013 10:55:01 AM
	* @Description: TODO 
	* @param @param reId 资源ID
	* @param @param reType 资源类型
	* @param @param selectType 关联关系资源类型
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getLINKResourceByReIdAndReTypeAndSelectType(long reId,String reType,String selectType){
		String idName = "id";
		if(reType !=null && reType.equals("Sys_Area")){
			idName = "area_id";
		}
		Context context = structureModuleLibrary.createContext();
		String sql =
					"	select "+ResourceCommon.getSelectSqlAttributsString(selectType,"re")+" "
					+"	  from "+selectType+" re "
					+"	 where re.entity_Id in "
					+"	       (select fn.entityid "
					+"	          from figureline fl, figurenode fn, figurenode fn2, "+reType+" re "
					+"	         where fl.linktype = 'LINK' "
					+"	           and (fl.leftid = fn.id or fl.rightid = fn.id) "
					+"	           and (fl.lefttype = fn.entitytype or fl.righttype = fn.entitytype) "
					+"	           and fn.entitytype = '"+selectType+"' "
					+"	           and re.entity_type = fn2.entitytype "
					+"	           and re.entity_id = fn2.entityid "
					+"	           and (fl.leftid = fn2.id or fl.rightid = fn2.id) "
					+"	           and (fl.lefttype = fn2.entitytype or fl.righttype = fn2.entitytype) "
					+"	           and re."+idName+" = "+reId+") ";
		SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, selectType);
		return list;
		//hib
//		List<Map<String,Object>> findQueryByHibernate = this.findQueryByHibernate(sql);
//		return findQueryByHibernate;
	}
	
	/**
	 * 基站统计监控
	* @author ou.jh
	* @date Jul 4, 2013 5:31:07 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getGPSBaseStationReportByResourceType(List<Long> areaIds){
		Context context = structureModuleLibrary.createContext();
		String whereSql ="";
		if(areaIds != null){
			whereSql ="  and ( ";
			for(long pa:areaIds){
				String s = " s.path like '%/Sys_Area/"+pa+"/%' or";
				if(whereSql.indexOf(s) >= 0){
					continue;
				}else{
					whereSql = whereSql + " s.path like '%/Sys_Area/"+pa+"/%' or";
				}
			}
			whereSql = whereSql.substring(0, whereSql.length() - 2) + " ) ";
		}else{
			return null;
		}
		String[] ss = new String[]{"BaseStation","BaseStation_GSM","Basestation_Repeater","Basestation_TD","Basestation_WLAN"};  
	String sql = "	select \"routineCount\", "
				+"	       \"unfinishedTasks\", "
				+"	       \"baseStationName\", "
				+"	       \"baseStationType\", "
				+"	       \"baseStationId\", "
				+"	       \"stationName\", "
				+"	       \"stationType\", "
				+"	       \"stationId\" "
				+"	  from (select \"routineCount\", "
				+"	               \"unfinishedTasks\", "
				+"	               \"baseStationName\", "
				+"	               \"baseStationType\", "
				+"	               \"baseStationId\", "
				+"	               \"stationName\", "
				+"	               \"stationType\", "
				+"	               \"stationId\" "
				+"	          from ( ";
					
	      for(String s:ss){
	    	  sql = sql	  +"	select ro.s \"routineCount\", "
	    	  			  +"	                         un.s \"unfinishedTasks\", "
						  +"	                         s.\"baseStationName\", "
						  +"	                         s.\"baseStationType\", "
						  +"	                         s.\"baseStationId\", "
						  +"	                         s.\"stationName\", "
						  +"	                         s.\"stationType\", "
					      +"	                         s.\"stationId\" "
	    	  			  +"	from (select     re.name           \"baseStationName\", "
						  +"	    re.entity_type           \"baseStationType\", "
					      +"	    re.id           \"baseStationId\", "
				    	  +"         s.name        \"stationName\", "
				    	  +"        s.entity_type \"stationType\", "
				    	  +"          s.id          \"stationId\", "
				    	  +"			fn.path"			
						  +"   from "+s+" re, "
						  +"             Figurenode fn, "
						  +"              Figurenode fn1, "
						  +"              Figurenode fn2, "
						  +"              Station s "
						  +"        where re.entity_id = fn.entityid "
						  +"          and re.entity_type = fn.entitytype "
						  +"          and fn1.id = fn.parent_figurenode_id "
						  +"          and fn1.entitytype = 'Room' "
						  +"          and fn2.id = fn1.parent_figurenode_id "
						  +"          and fn2.entitytype = 'Station' "
						  +"          and s.entity_id = fn2.entityid ";
	    	  sql = sql + " ) s "
	    	  +"	      left join (select count(t1.woId) s, "
	    	  +"                  t1.networkresourceid, "
	    	  +"                  t1.networkresourcetype "
	    	  +"             from wm_workorderassnetresource  t1, "
	    	  +"                  V_WM_URGENTREPAIR_WORKORDER t2 "
	    	  +"            where t1.woId is not null "
	    	  +"              and t1.woId = t2.\"woId\" "
	    	  +"              and t2.\"status\" <> 7 "
	    	  +"              and t2.\"requireCompleteTime\" < sysdate "
	    	  +"            group by t1.networkresourceid, "
	    	  +"                    t1.networkresourcetype) ro on s.\"baseStationType\" = "
	    	  +"                                                  ro.networkresourcetype "
	    	  +"                                               and s.\"baseStationId\" = "
	    	  +"                                                  ro.networkresourceid "
	    	  +" left join (select count(t1.woId) s, "
	    	  +"                  t1.networkresourceid, "
	    	  +"                  t1.networkresourcetype "
	    	  +"             from wm_workorderassnetresource  t1, "
	    	  +"                  V_WM_URGENTREPAIR_WORKORDER t2 "
	    	  +"           where t1.woId is not null "
	    	  +"              and t1.woId = t2.\"woId\" "
	    	  +"              and t2.\"status\" <> 7 "
	    	  +"            group by t1.networkresourceid, "
	    	  +"                     t1.networkresourcetype) un on s.\"baseStationType\" = "
	    	  +"                                                   un.networkresourcetype "
	    	  +"                                              and s.\"baseStationId\" = "
	    	  +"                                                  un.networkresourceid  "
	    	  + whereSql +" union";
	      }  

    	  sql = sql.substring(0,sql.length() - 5);
    	  sql = sql + " ) order by \"unfinishedTasks\" desc) ";
	      SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, "Sys_Area");
		return list;
	}
	
	
	/**
	 * 全资源统计
	* @author ou.jh
	* @date Jul 4, 2013 5:31:07 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getGPSReportReportByResourceType(List<Long> areaIds){
		Context context = structureModuleLibrary.createContext();
		String whereSql ="";
		if(areaIds != null){
			whereSql ="  where ( ";
			for(long pa:areaIds){
				String s = " s.path like '%/Sys_Area/"+pa+"/%' or";
				if(whereSql.indexOf(s) >= 0){
					continue;
				}else{
					whereSql = whereSql + " s.path like '%/Sys_Area/"+pa+"/%' or";
				}
			}
			whereSql = whereSql.substring(0, whereSql.length() - 2) + " ) ";
		}else{
			return null;
		}
		String[] ss = new String[]{"BaseStation","BaseStation_GSM","Basestation_Repeater","Basestation_TD","Basestation_WLAN"};  
	String sql = "	select \"baseStationName\", "
				+"	       \"baseStationType\", "
				+"	       \"baseStationId\", "
				+"	       \"stationName\", "
				+"	       \"stationType\", "
				+"	       \"stationId\" "
				+"	  from (select \"baseStationName\", "
				+"	               \"baseStationType\", "
				+"	               \"baseStationId\", "
				+"	               \"stationName\", "
				+"	               \"stationType\", "
				+"	               \"stationId\" "
				+"	          from ( ";
					
	      for(String s:ss){
	    	  sql = sql	  +"	select s.\"baseStationName\", "
						  +"	                         s.\"baseStationType\", "
						  +"	                         s.\"baseStationId\", "
						  +"	                         s.\"stationName\", "
						  +"	                         s.\"stationType\", "
					      +"	                         s.\"stationId\" "
	    	  			  +"	from (select     re.name           \"baseStationName\", "
						  +"	    re.entity_type           \"baseStationType\", "
					      +"	    re.id           \"baseStationId\", "
				    	  +"         s.name        \"stationName\", "
				    	  +"        s.entity_type \"stationType\", "
				    	  +"          s.id          \"stationId\", "
				    	  +"			fn.path"			
						  +"   from "+s+" re, "
						  +"             Figurenode fn, "
						  +"              Figurenode fn1, "
						  +"              Figurenode fn2, "
						  +"              Station s "
						  +"        where re.entity_id = fn.entityid "
						  +"          and re.entity_type = fn.entitytype "
						  +"          and fn1.id = fn.parent_figurenode_id "
						  +"          and fn1.entitytype = 'Room' "
						  +"          and fn2.id = fn1.parent_figurenode_id "
						  +"          and fn2.entitytype = 'Station' "
						  +"          and s.entity_id = fn2.entityid ";
	    	  sql = sql + " ) s "
	    	  + whereSql +" union";
	      }  

    	  sql = sql.substring(0,sql.length() - 5);
    	  sql = sql + " ) ) ";
	      SqlContainer sc = context.createSqlContainer(sql);
		List<BasicEntity> list=context.executeSelectSQL(sc, "Sys_Area");
		return list;
	}
	
	/**
	 * 获取字符串中字符的出现次数
	* @author ou.jh
	* @date Jul 2, 2013 2:02:53 PM
	* @Description: TODO 
	* @param @param str
	* @param @param st
	* @param @return        
	* @throws
	 */
	private int getStrCharSize(String str,String st){
		int count = 0;
		for(int i = 0; i < str.length(); i++) {
			String temp = str.substring(i, i + 1);
			if(st.equals(temp)) {
				count++;
			}
		}
		return count;
	}

	public StructureModuleLibrary getStructureModuleLibrary() {
		return structureModuleLibrary;
	}

	public void setStructureModuleLibrary(
			StructureModuleLibrary structureModuleLibrary) {
		this.structureModuleLibrary = structureModuleLibrary;
	}
}
