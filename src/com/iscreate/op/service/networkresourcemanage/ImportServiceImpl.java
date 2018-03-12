package com.iscreate.op.service.networkresourcemanage;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.OperationType;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;
import com.iscreate.plat.networkresource.structure.instance.Structure;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleLibrary;

public class ImportServiceImpl implements ImportService{
	

	private StructureCommonService structureCommonService;
	private PhysicalresService physicalresService;
	private Dictionary dictionary;
	private XMLAEMLibrary moduleLibrary;
	private StructureModuleLibrary structureModuleLibrary;
	private static final Log log = LogFactory.getLog(ImportServiceImpl.class);
	/**
	 * 
	 * @description: 新增导入
	 * @author：
	 * @param userId 用户id
	 * @param userName 用户名称
	 * @param importEntityType 导入资源类型
	 * @param importModel 导入模式
	 * @param opCause 维护记录起因
	 * @param opScene 维护记录场景
	 * @param rdoAssModel 匹配上级资源规则 （精确 模糊）
	 * @param assType 上级关联类型 （上下级  连接）
	 * @param assEntityType 上级资源类型
	 * @param listMap 导入资源记录list
	 * @param rows 行记录list
	 * @param assIds 上级资源 id 记录list
	 * @return     
	 *      
	 * @date：Jul 4, 2013 4:19:58 PM
	 */
	public  List<Map<String,Object>> importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds){
		log.info("进入importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),新增导入资源数据。");
		log.info("进入importAddResource,userId="+userId);
		log.info("进入importAddResource,userName="+userName);
		log.info("进入importAddResource,importEntityType="+importEntityType);
		log.info("进入importAddResource,importModel="+importModel);
		log.info("进入importAddResource,opCause="+opCause);
		log.info("进入importAddResource,opScene="+opScene);
		log.info("进入importAddResource,rdoAssModel="+rdoAssModel);
		log.info("进入importAddResource,assType="+assType);
		log.info("进入importAddResource,assEntityType="+assEntityType);
		log.info("进入importAddResource,listMap="+listMap);
		log.info("进入importAddResource,rows="+rows);
		log.info("进入importAddResource,assIds="+assIds);
		Connection connection = null;
		PreparedStatement insertSqlpstmt = null;
		PreparedStatement fnSqlpstmt = null;
		PreparedStatement nmrSqlpstmt = null;
		PreparedStatement flSqlpstmt = null;
		PreparedStatement flnmrSqlpstmt = null;
		try{
		    Map<String,Object> assMap = new HashMap<String,Object>(); //上级资源id 与导入资源figurenode id对应map信息
			Map<String,Object> nameMap = new HashMap<String,Object>();//figurenode的id与导入资源id与名称的对应map信息
			Map<String,Object> idAndFnIdMap = new HashMap<String,Object>();//figurenode的id与导入资源id对应map信息
			StringBuffer sf = new StringBuffer();
			StringBuffer qsf = new StringBuffer();//插入资源拼装sql变量
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();//返回结果
			ApplicationEntity importApp = null;//当前导入资源
			ApplicationModule module = ModuleProvider.getModule(importEntityType);//导入资源key模板
			for(String key:module.keyset()){
				 sf.append(","+key);
				 qsf.append(",?");  //获取插入资源的key和value的对应列和值
			}
			if("newAdd".equals(importModel)){
				long curFigId = getFigureId("networkresourcemanage");//当前网络资源figureId
				qsf.append(",?,?");
				StringBuffer insertSql = new StringBuffer("insert into "+importEntityType.toLowerCase());//插入sql
				insertSql = insertSql.append("("+sf.substring(1,sf.length())+",ENTITY_ID,ENTITY_TYPE) values ("+qsf.substring(1,qsf.length())+")");
				//figurenode表的插入sql
				StringBuffer fnSql = new StringBuffer("insert into figurenode(id,figureId,entityId,entityType,birthDate,ENTITY_TYPE,path,parent_figurenode_id) values (?,?,?,?,?,?,?,?)");
				//维护记录的插入sql
				StringBuffer nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,ENTITY_ID,ENTITY_TYPE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				connection = DataSourceConn.initInstance().getConnection();  //new Conn().getConnection();数据源连接
				connection.setAutoCommit(false);
				insertSqlpstmt = connection.prepareStatement(insertSql.toString());
				fnSqlpstmt = connection.prepareStatement(fnSql.toString());
				nmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
				
				for(int index=0;index<rows.size();index++){//循环资源 sql设值
					if(listMap.get(index)!=null){
						Map<String,String> map = listMap.get(index);
						map.put("id", structureCommonService.getEntityPrimaryKey(importEntityType)+"");//主键id
						importApp = module.createApplicationEntity();
						for(String key:map.keySet()){
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(importApp, varType, key, map.get(key));
						}
						int indexFlag=1;
						for(String key:module.keyset()){
							String type = module.getAttribute(key).getValue("type")+"";
							if(map.containsKey(key)){
								if(type.indexOf("String")>=0 ){
									insertSqlpstmt.setString(indexFlag,map.get(key));
								}else if(type.indexOf("Date")>=0){
									if(map.get(key)!=null && !"".equals(map.get(key))){
										//insertSqlpstmt.setObject(indexFlag, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(key)));
										Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(key));
										insertSqlpstmt.setTimestamp(indexFlag, new Timestamp(d.getTime()));
									}else{
										insertSqlpstmt.setNull(indexFlag,Types.DATE);
									}
								 }else{
									 insertSqlpstmt.setObject(indexFlag,map.get(key));
								 }
							}else{
								 insertSqlpstmt.setNull(indexFlag, Types.VARCHAR);
							} 
							indexFlag++;
							 
						}
						insertSqlpstmt.setObject(indexFlag,importApp.getValue("_entityId"));
						insertSqlpstmt.setObject(indexFlag+1,importApp.getValue("_entityType"));
						insertSqlpstmt.addBatch();	//资源插入sql
						//figurenode表插入
						long fId = structureCommonService.getEntityPrimaryKey(Figurenode.MY_TYPE);
						fnSqlpstmt.setLong(1, fId);
						fnSqlpstmt.setLong(2, curFigId);
						fnSqlpstmt.setLong(3,Long.valueOf(""+importApp.getValue("_entityId")));
						fnSqlpstmt.setString(4,importApp.getValue("_entityType")+"");
						fnSqlpstmt.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
						fnSqlpstmt.setString(6, "Figurenode");
						fnSqlpstmt.setString(7,"/"+importApp.getType()+"/"+importApp.getValue("id")+"/");
						fnSqlpstmt.setString(8,"0");
						fnSqlpstmt.addBatch();		
						//维护记录插入
						ResourceMaintenance maintenance = new ResourceMaintenance();
						String resName="";
						if(importApp.getValue("name") != null){
							resName = importApp.getValue("name");
						}else{
							resName = importApp.getValue("label");
						}
						ApplicationEntity mAe = maintenance.getApplicationEntity();
						nmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
						nmrSqlpstmt.setString(2,OperationType.NETWORK);
						nmrSqlpstmt.setString(3,opCause);
						nmrSqlpstmt.setString(4,opScene);
						nmrSqlpstmt.setString(5,OperationType.RESOURCEINSERT);
						nmrSqlpstmt.setString(6,importApp.getValue("_entityType").toString());
						nmrSqlpstmt.setLong(7,(Long)importApp.getValue("id"));
						nmrSqlpstmt.setString(8,resName);
						nmrSqlpstmt.setString(9,userName);
						nmrSqlpstmt.setString(10,userId);
						//nmrSqlpstmt.setObject(11,new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
						nmrSqlpstmt.setTimestamp(11,new Timestamp((new java.util.Date()).getTime()));
						nmrSqlpstmt.setObject(12,0);
						nmrSqlpstmt.setLong(13,Long.valueOf(mAe.getValue("_entityId")+""));
						nmrSqlpstmt.setString(14,mAe.getValue("_entityType")+"");
						nmrSqlpstmt.addBatch();
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("id",importApp.getValue("id"));
						mp.put("row",rows.get(index));
						//resultList.add(mp);
						nameMap.put(fId+"",importApp.getValue("id")+"#"+resName);
						String assId = assIds.get(Integer.valueOf(rows.get(index)))+"";
						if(!"0".equals(assId)){
							if(assMap.containsKey(assId)){
								assMap.put(assId,assMap.get(assId)+","+fId);
							}else{
								assMap.put(assId,fId);
							}
						}else{
							if(rdoAssModel!=null&& !"".equals(rdoAssModel)){
								mp.put("associate", "no");
							}
						}
						resultList.add(mp);
						idAndFnIdMap.put(fId+"",importApp.getValue("id"));
					}
				}
				int[] s = insertSqlpstmt.executeBatch();
				int status =0;
				if(s!=null){
					s = fnSqlpstmt.executeBatch();
				}
				if(s!=null){
					s = nmrSqlpstmt.executeBatch();
					status=1;
				}
				connection.commit();
				if(status>0){
					if(rdoAssModel!=null&& !"".equals(rdoAssModel)){//关联上级资源
						status=0;
						if(!assMap.isEmpty()){
							//获取上级资源相关信息
							List<Map<String,Object>> rList = this.physicalresService.getNodeIdsAndParentNodeIdsMap(assMap, assEntityType);
							Map<String,Object> nodeMap = rList.get(0);//资源figurenode id对应上级资源figurenode id map
							Map<String,Object> pNameMap = rList.get(1);//资源figurenode id对应资源名称 map
							Map<String,Object> pathMap = rList.get(2);//资源figurenode id对应上级资源figurenode path map
							if(nodeMap!=null && !nodeMap.isEmpty()){
								//关联关系操作
								StringBuffer flSql = new StringBuffer("insert into figureline(id,leftId,rightId,linkType,figureId,birthdate,lefttype,righttype,ENTITY_TYPE) values(?,?,?,?,?,?,?,?,?)");
								if("parent".equals(assType)){
									flSql = new StringBuffer("update figurenode set path=?,parent_figurenode_id=? where id=?");
								}
								nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,content,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,ENTITY_ID,ENTITY_TYPE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								flSqlpstmt = connection.prepareStatement(flSql.toString());
								flnmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
								for(String id:nodeMap.keySet()){
									
									if("parent".equals(assType)){
										flSqlpstmt.setString(1, pathMap.get(id)+importEntityType+"/"+idAndFnIdMap.get(id)+"/");
										flSqlpstmt.setString(2, nodeMap.get(id)+"");
										flSqlpstmt.setString(3, id);
										flSqlpstmt.addBatch();
									}else{
										flSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey(Figureline.MY_TYPE));
										flSqlpstmt.setLong(2, Long.valueOf(nodeMap.get(id)+""));
										flSqlpstmt.setLong(3, Long.valueOf(id));
										flSqlpstmt.setString(4, "LINK");
										flSqlpstmt.setLong(5,curFigId);
										flSqlpstmt.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
										flSqlpstmt.setString(7, assEntityType);
										flSqlpstmt.setString(8, importEntityType);
										flSqlpstmt.setString(9, Figureline.MY_TYPE);
										flSqlpstmt.addBatch();
									}
									
									String resNameChinese = "";
									if(assEntityType != null){
										try {
											List<BasicEntity> entry = null;
											entry = dictionary.getEntry(assEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
											resNameChinese = entry.get(0).getValue("display");
										} catch (EntryOperationException e) {
											e.printStackTrace();
										}
									}
									//添加资源维护记录
									ResourceMaintenance maintenance = new ResourceMaintenance();
									String lresName = pNameMap.get(nodeMap.get(id)+"")+"";
									String rresName = (nameMap.get(id)+"").substring((nameMap.get(id)+"").indexOf("#")+1);
									String resId = (nameMap.get(id)+"").substring(0,(nameMap.get(id)+"").indexOf("#"));
									String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
									ApplicationEntity mAe = maintenance.getApplicationEntity();
									flnmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
									flnmrSqlpstmt.setString(2,OperationType.NETWORK);
									flnmrSqlpstmt.setString(3,content);
									flnmrSqlpstmt.setString(4,opCause);
									flnmrSqlpstmt.setString(5,opScene);
									flnmrSqlpstmt.setString(6,OperationType.INSERTLINK);
									flnmrSqlpstmt.setString(7,importEntityType);
									flnmrSqlpstmt.setLong(8,Long.valueOf(resId));
									flnmrSqlpstmt.setString(9,rresName);
									flnmrSqlpstmt.setString(10,userName);
									flnmrSqlpstmt.setString(11,userId);
									///flnmrSqlpstmt.setObject(12,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									flnmrSqlpstmt.setTimestamp(12,new Timestamp((new java.util.Date()).getTime()));
									flnmrSqlpstmt.setObject(13,1);
									flnmrSqlpstmt.setLong(14,Long.valueOf(mAe.getValue("_entityId")+""));
									flnmrSqlpstmt.setString(15,mAe.getValue("_entityType")+"");
									flnmrSqlpstmt.addBatch();
									
								}
								status=0;
								s = flSqlpstmt.executeBatch();
								if(s!=null){
									s = flnmrSqlpstmt.executeBatch();
									status=1;
								}
								connection.commit();
								connection.setAutoCommit(true);
							}
						}
						
					}
			}
			}
			if(resultList.isEmpty()){
				log.info("退出importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),返回结果null");
				return null;
			}else{
				log.info("退出importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),返回结果"+resultList);
				return resultList;
			}
			
		}catch(Exception e){
			log.error("退出importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),发生异常，保存数据失败");
			e.printStackTrace();
			try {
				if(connection!=null){
					connection.rollback();
				}
			} catch (SQLException e1) {
				log.error("退出importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),数据回滚操作失败");
				e1.printStackTrace();
			}
			return null;
		}finally {
			try {
				if(insertSqlpstmt!=null){
					insertSqlpstmt.close();
				}
				if(fnSqlpstmt!=null){
					fnSqlpstmt.close();
				}
				if(nmrSqlpstmt!=null){
					nmrSqlpstmt.close();
				}
				if(flSqlpstmt!=null){
					flSqlpstmt.close();
				}
				if(flnmrSqlpstmt!=null){
					flnmrSqlpstmt.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (Exception ex) {
				log.error("退出importAddResource(String userId,String userName,String importEntityType,String importModel,String opCause,String opScene,String rdoAssModel,String assType,String assEntityType,List<Map<String,String>> listMap,List<String>  rows,List<String> assIds),关闭数据库连接失败");
			}

		}
		
	}

	/**
	 * 
	 * @description: 更新导入
	 * @author：
	 * @param userId 用户id
	 * @param userName 用户名
	 * @param importEntityType 导入资源类型
	 * @param importModel 导入模式
	 * @param opCause 维护记录起因
	 * @param opScene 维护记录场景
	 * @param rdoAssModel 匹配上级资源模式
	 * @param assType 关联关系
	 * @param assEntityType 关联资源类型
	 * @param listMap 导入资源list
	 * @param rows  行记录list
	 * @param assIds 关联资源id list
	 * @param updateIds 更新资源id list
	 * @return     
	 *      
	 * @date：Jul 4, 2013 4:37:39 PM
	 */
	public List<Map<String, Object>> importUpdateResource(String userId,
			String userName, String importEntityType, String importModel,
			String opCause, String opScene, String rdoAssModel, String assType,
			String assEntityType, List<Map<String, String>> listMap,
			List<String> rows, List<String> assIds, List<String> updateIds) {
		log.info("进入importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),更新导入数据.");
		log.info("进入importUpdateResource,userId="+userId);
		log.info("进入importUpdateResource,userName="+userName);
		log.info("进入importUpdateResource,importEntityType="+importEntityType);
		log.info("进入importUpdateResource,importModel="+importModel);
		log.info("进入importUpdateResource,opCause="+opCause);
		log.info("进入importUpdateResource,opScene="+opScene);
		log.info("进入importUpdateResource,rdoAssModel="+rdoAssModel);
		log.info("进入importUpdateResource,assType="+assType);
		log.info("进入importUpdateResource,assEntityType="+assEntityType);
		log.info("进入importUpdateResource,listMap="+listMap);
		log.info("进入importUpdateResource,rows="+rows);
		log.info("进入importUpdateResource,assIds="+assIds);
		log.info("进入importUpdateResource,updateIds="+updateIds);
		//获取登录人ID
		Connection connection = null;
		Statement updateSqlpstmt = null;
		Statement deleteSqlpstmt = null;
		PreparedStatement deletenmrSqlpstmt = null;
		PreparedStatement nmrSqlpstmt = null;
		PreparedStatement flSqlpstmt = null;
		PreparedStatement flnmrSqlpstmt = null;
		try{
			//List<Map<String,String>> listMap = gson.fromJson(this.importMaps,new TypeToken<List<Map<String,String>>>(){}.getType());
			//List<String>  rows = gson.fromJson(this.rowNums,new TypeToken<List<String>>(){}.getType());
			//List<String> assIds = gson.fromJson(this.assRecord,new TypeToken<List<String>>(){}.getType());
			//List<String> updateIds = gson.fromJson(this.importRecord,new TypeToken<List<String>>(){}.getType());
			Map<String,Object> assMap = new HashMap<String,Object>();//上级资源id 与导入资源figurenode id对应map信息
			Map<String,Object> oldParNameMap = new HashMap<String,Object>();//资源figurenode id 与 当前关联资源名称对应map信息
			Map<String,Object> nameMap = new HashMap<String,Object>();//figurenode的id与导入资源id与名称的对应map信息
			Map<String,Object> idAndFnIdMap = new HashMap<String,Object>();//figurenode的id与导入资源id对应map信息
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			ApplicationEntity importApp = null;
			ApplicationModule module = ModuleProvider.getModule(importEntityType);			
			Map<String,Object> updateIdMap = new HashMap<String,Object>();
			Map<String,Object> updateAppMap = null;
			Map<String,Object> fnIdMap = null;
			Map<String,Object> fnMap = new HashMap<String,Object>();
			if("updateAdd".equals(importModel)){
				long curFigId = getFigureId("networkresourcemanage");
				for(String updateId:updateIds){
					updateIdMap.put(updateId, updateId);
				}
				updateAppMap = this.physicalresService.getApplicationEntityMap(updateIdMap, importEntityType);
				fnIdMap = this.physicalresService.getFigureNodeIdMap(updateIdMap, importEntityType);
				if(fnIdMap!=null){
					for(String key:fnIdMap.keySet()){
						fnMap.put(fnIdMap.get(key)+"", fnIdMap.get(key));
					}
				}
				if("parent".equals(assType)){//获取资源figurenode id 与 当前关联资源名称对应map信息
					oldParNameMap = this.physicalresService.getFigureNodeIdParInfoMap(fnMap,"CLAN");
				}else{
					oldParNameMap = this.physicalresService.getFigureNodeIdParInfoMap(fnMap,"LINK");
				}
				//维护记录插入sql
				StringBuffer nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,content,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,ENTITY_ID,ENTITY_TYPE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				connection = DataSourceConn.initInstance().getConnection();  //new Conn().getConnection();数据库连接
				connection.setAutoCommit(false);
				updateSqlpstmt = connection.createStatement();
				nmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
				for(int index=0;index<rows.size();index++){//循环资源 sql设值
					if(listMap.get(index)!=null){
						Map<String,String> map = listMap.get(index);
						String updateId = updateIds.get(index);
						map.put("id", updateIds.get(index));
						importApp = module.createApplicationEntity();
						for(String key:map.keySet()){
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(importApp, varType, key, map.get(key));
						}
						updateSqlpstmt.addBatch(this.physicalresService.updateApplicationSql(importApp, module));
						//BasicEntity updateBe = physicalresService.getPhysicalresById(this.importEntityType,Long.valueOf(updateId));
						ApplicationEntity updateAe = (ApplicationEntity)updateAppMap.get(updateId);
						String updateValue = getUpdateValue(importApp,updateAe);
						String resName = "";
						if(importApp.getValue("name") != null){
							resName = importApp.getValue("name");
						}else{
							resName = importApp.getValue("label");
						}			
						//维护记录
						ResourceMaintenance maintenance = new ResourceMaintenance();
						ApplicationEntity mAe = maintenance.getApplicationEntity();
						nmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
						nmrSqlpstmt.setString(2,OperationType.NETWORK);
						nmrSqlpstmt.setString(3,updateValue);
						nmrSqlpstmt.setString(4,opCause);
						nmrSqlpstmt.setString(5,opScene);
						nmrSqlpstmt.setString(6,OperationType.RESOURCEUPDATE);
						nmrSqlpstmt.setString(7,importApp.getValue("_entityType").toString());
						nmrSqlpstmt.setLong(8,(Long)importApp.getValue("id")); 
						nmrSqlpstmt.setString(9,resName);
						nmrSqlpstmt.setString(10,userName);
						nmrSqlpstmt.setString(11,userId);
						nmrSqlpstmt.setTimestamp(12,new Timestamp((new java.util.Date()).getTime()));
						nmrSqlpstmt.setObject(13,0);
						nmrSqlpstmt.setLong(14,Long.valueOf(mAe.getValue("_entityId")+""));
						nmrSqlpstmt.setString(15,mAe.getValue("_entityType")+"");
						nmrSqlpstmt.addBatch();
						
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("id",importApp.getValue("id"));
						mp.put("row",rows.get(index));
						
						//String fId = this.physicalresService.getFigureNodeId(importApp.getValue("id")+"", this.importEntityType);
						String fId=fnIdMap.get(updateId)+"";
						nameMap.put(fId+"",importApp.getValue("id")+"#"+resName);
						String assId = assIds.get(Integer.valueOf(rows.get(index)))+"";
						if(!"0".equals(assId)&&!"undefined".equals(assId)){
							if(assMap.containsKey(assId)){
								assMap.put(assId,assMap.get(assId)+","+fId);
							}else{
								assMap.put(assId,fId);
							}
						}else{
							if(rdoAssModel!=null&& !"".equals(rdoAssModel)){
								mp.put("associate", "no");
							}
						}
						resultList.add(mp);
						idAndFnIdMap.put(fId+"", importApp.getValue("id"));
					/*	String name = this.physicalresService.getParentNodeInfo(fId);
						if(!"".equals(name)){
							oldParNameMap.put(fId,name);
						}*/
					}
				}
				
				int[] s = updateSqlpstmt.executeBatch();
				int status =0;
				if(s!=null){
					s = nmrSqlpstmt.executeBatch();
					status =1;
				}
				connection.commit();
				if(status>0){
					if(rdoAssModel!=null&& !"".equals(rdoAssModel)){//关联上级
						deleteSqlpstmt = connection.createStatement();
						deletenmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
						if(oldParNameMap!=null && !oldParNameMap.isEmpty()){//删除之前关联关系
							for(String fId:oldParNameMap.keySet()){
								if("parent".equals(assType)){
									//deleteSqlpstmt.addBatch("delete from figureline where rightId="+fId+" and rightType='"+importEntityType+"' and linkType='CLAN'");
								}else{
									deleteSqlpstmt.addBatch("delete from figureline where rightId="+fId+" and righttype='"+importEntityType+" and linkType='LINK'");
								}
								String oldParNameInfo = oldParNameMap.get(fId)+"";
								String deleteParentType = oldParNameInfo.substring(oldParNameInfo.lastIndexOf("#")+1);
								String resNameChinese = "";
								try {
									List<BasicEntity> entry = null;
										entry = dictionary.getEntry(deleteParentType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
										resNameChinese = entry.get(0).getValue("display");
								} catch (EntryOperationException e) {
									e.printStackTrace();
								}
								String lresName = oldParNameInfo.substring(0,oldParNameInfo.lastIndexOf("#"))+"";
								String appInfo = nameMap.get(fId)+"";
								String resId = appInfo.substring(0,appInfo.indexOf("#"));
								String  rresName= appInfo.substring(appInfo.indexOf("#")+1);
								String content = OperationType.DELETE+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
								ResourceMaintenance maintenance = new ResourceMaintenance();
								ApplicationEntity mAe = maintenance.getApplicationEntity();
								deletenmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
								deletenmrSqlpstmt.setString(2,OperationType.NETWORK);
								deletenmrSqlpstmt.setString(3,content);
								deletenmrSqlpstmt.setString(4,opCause);
								deletenmrSqlpstmt.setString(5,opScene);
								deletenmrSqlpstmt.setString(6,OperationType.DELETELINK);
								deletenmrSqlpstmt.setString(7,importEntityType);
								deletenmrSqlpstmt.setLong(8,Long.valueOf(resId));
								deletenmrSqlpstmt.setString(9,rresName);
								deletenmrSqlpstmt.setString(10,userName);
								deletenmrSqlpstmt.setString(11,userId);
								deletenmrSqlpstmt.setTimestamp(12,new Timestamp((new java.util.Date()).getTime()));
								deletenmrSqlpstmt.setObject(13,1);
								deletenmrSqlpstmt.setLong(14,Long.valueOf(mAe.getValue("_entityId")+""));
								deletenmrSqlpstmt.setString(15,mAe.getValue("_entityType")+"");
								deletenmrSqlpstmt.addBatch();
							}
							if(!assMap.isEmpty()){//重新建立关联关系
								List<Map<String,Object>> rList = this.physicalresService.getNodeIdsAndParentNodeIdsMap(assMap, assEntityType);
								Map<String,Object> nodeMap = rList.get(0);//资源figurenode id对应上级资源figurenode id map
								Map<String,Object> pNameMap = rList.get(1);//资源figurenode id对应资源名称 map
								Map<String,Object> pathMap = rList.get(2);//资源figurenode id对应上级资源figurenode path map
								if(nodeMap!=null && !nodeMap.isEmpty()){
									StringBuffer flSql = new StringBuffer("insert into figureline(id,leftId,rightId,linkType,figureId,birthdate,lefttype,righttype,ENTITY_TYPE) values(?,?,?,?,?,?,?,?,?)");
									if("parent".equals(assType)){
										flSql = new StringBuffer("update figurenode set path=?,parent_figurenode_id=? where id=?");
									}
									nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,content,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,ENTITY_ID,ENTITY_TYPE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
									flSqlpstmt = connection.prepareStatement(flSql.toString());
									flnmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
									for(String id:nodeMap.keySet()){
										
										if("parent".equals(assType)){
											flSqlpstmt.setString(1, pathMap.get(id)+importEntityType+"/"+idAndFnIdMap.get(id)+"/");
											flSqlpstmt.setString(2, nodeMap.get(id)+"");
											flSqlpstmt.setString(3, id);
											flSqlpstmt.addBatch();
										}else{
											flSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey(Figureline.MY_TYPE));
											flSqlpstmt.setLong(2, Long.valueOf(nodeMap.get(id)+""));
											flSqlpstmt.setLong(3, Long.valueOf(id));
											flSqlpstmt.setString(4, "LINK");
											flSqlpstmt.setLong(5, curFigId);
											flSqlpstmt.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
											flSqlpstmt.setString(7, assEntityType);
											flSqlpstmt.setString(8, importEntityType);
											flSqlpstmt.setString(9, Figureline.MY_TYPE);
											flSqlpstmt.addBatch();
										}
										
										String resNameChinese = "";
										if(assEntityType != null){
											try {
												List<BasicEntity> entry = null;
												entry = dictionary.getEntry(assEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
												resNameChinese = entry.get(0).getValue("display");
											} catch (EntryOperationException e) {
												e.printStackTrace();
											}
										}
										//添加资源维护记录
										ResourceMaintenance maintenance = new ResourceMaintenance();
										String lresName = pNameMap.get(nodeMap.get(id)+"")+"";
										String rresName = (nameMap.get(id)+"").substring((nameMap.get(id)+"").indexOf("#")+1);
										String resId = (nameMap.get(id)+"").substring(0,(nameMap.get(id)+"").indexOf("#"));
										String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
										ApplicationEntity mAe = maintenance.getApplicationEntity();
										flnmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
										flnmrSqlpstmt.setString(2,OperationType.NETWORK);
										flnmrSqlpstmt.setString(3,content);
										flnmrSqlpstmt.setString(4,opCause);
										flnmrSqlpstmt.setString(5,opScene);
										flnmrSqlpstmt.setString(6,OperationType.INSERTLINK);
										flnmrSqlpstmt.setString(7,importEntityType);
										flnmrSqlpstmt.setLong(8,Long.valueOf(resId));
										flnmrSqlpstmt.setString(9,rresName);
										flnmrSqlpstmt.setString(10,userName);
										flnmrSqlpstmt.setString(11,userId);
										flnmrSqlpstmt.setTimestamp(12,new Timestamp((new java.util.Date()).getTime()));
										flnmrSqlpstmt.setObject(13,1);
										flnmrSqlpstmt.setLong(14,Long.valueOf(mAe.getValue("_entityId")+""));
										flnmrSqlpstmt.setString(15,mAe.getValue("_entityType")+"");
										flnmrSqlpstmt.addBatch();
										
									}
									if(!"parent".equals(assType)){
										s = deleteSqlpstmt.executeBatch();
									}
									s=deletenmrSqlpstmt.executeBatch();
									s = flSqlpstmt.executeBatch();
									s = flnmrSqlpstmt.executeBatch();

									connection.commit();
									connection.setAutoCommit(true);
								}
								
							}
							
						}
						}		
			}	
			}
			if(resultList.isEmpty()){
				log.info("退出importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),返回结果null.");
				return null;
			}else{
				log.info("退出importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),返回结果"+resultList);
				return resultList;
			}
		}catch(Exception e){
			log.error("退出importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),发生异常，保存数据失败.");
			e.printStackTrace();
			try {
				if(connection!=null){
					connection.rollback();
				}
			} catch (SQLException e1) {
				log.error("退出importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),数据回滚操作失败.");
				e1.printStackTrace();
			}
			return null;
		}finally {
			try {
				if(updateSqlpstmt!=null){
					updateSqlpstmt.close();
				}
				if(deleteSqlpstmt!=null){
					deleteSqlpstmt.close();
				}
				if(deletenmrSqlpstmt!=null){
					deletenmrSqlpstmt.close();
				}
				if(nmrSqlpstmt!=null){
					nmrSqlpstmt.close();
				}
				if(flSqlpstmt!=null){
					flSqlpstmt.close();
				}
				if(flnmrSqlpstmt!=null){
					flnmrSqlpstmt.close();
				}
				if(connection!=null){
					connection.close();
				}	
			} catch (Exception ex) {
				log.error("退出importUpdateResource(String userId,String userName, String importEntityType, String importModel,String opCause, String opScene, String rdoAssModel, String assType,String assEntityType, List<Map<String, String>> listMap,List<String> rows, List<String> assIds, List<String> updateIds),数据库连接关闭失败.");
			}

		}
	}


	public List<Map<String, Object>> importDeleteAddResource(
			String userId, String userName, String importEntityType,
			String importModel, String opCause, String opScene,
			String rdoAssModel, String assType, String assEntityType,
			List<Map<String, String>> listMap, List<String> rows,
			List<String> assIds, List<String> updateIds) {
		log.info("进入importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),删除新增导入.");
		log.info("进入importDeleteAddResource,userId="+userId);
		log.info("进入importDeleteAddResource,userName="+userName);
		log.info("进入importDeleteAddResource,importEntityType="+importEntityType);
		log.info("进入importDeleteAddResource,importModel="+importModel);
		log.info("进入importDeleteAddResource,opCause="+opCause);
		log.info("进入importDeleteAddResource,opScene="+opScene);
		log.info("进入importDeleteAddResource,rdoAssModel="+rdoAssModel);
		log.info("进入importDeleteAddResource,assType="+assType);
		log.info("进入importDeleteAddResource,assEntityType="+assEntityType);
		log.info("进入importDeleteAddResource,listMap="+listMap);
		log.info("进入importDeleteAddResource,rows="+rows);
		log.info("进入importDeleteAddResource,assIds="+assIds);
		log.info("进入importDeleteAddResource,updateIds="+updateIds);
		//获取登录人ID
		Connection connection = null;
		PreparedStatement deletenmrSqlpstmt = null;
		PreparedStatement insertSqlpstmt = null;
		PreparedStatement fnSqlpstmt = null;
		PreparedStatement nmrSqlpstmt = null;
		PreparedStatement flSqlpstmt = null;
		PreparedStatement flnmrSqlpstmt = null;
		
		try{
			/*this.importMaps=this.importMaps.replace("\\","\\u005c"); //对\符号处理
			List<Map<String,String>> listMap = gson.fromJson(this.importMaps,new TypeToken<List<Map<String,String>>>(){}.getType());
			List<String>  rows = gson.fromJson(this.rowNums,new TypeToken<List<String>>(){}.getType());
			List<String> assIds = gson.fromJson(this.assRecord,new TypeToken<List<String>>(){}.getType());
			List<String> updateIds = gson.fromJson(this.importRecord,new TypeToken<List<String>>(){}.getType());*/
			Map<String,Object> assMap = new HashMap<String,Object>();
			Map<String,Object> nameMap = new HashMap<String,Object>();
			StringBuffer sf = new StringBuffer();
			StringBuffer qsf = new StringBuffer();
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			ApplicationEntity importApp = null;
			ApplicationModule module = ModuleProvider.getModule(importEntityType);
			Map<String,Object> updateIdMap = new HashMap<String,Object>();
			Map<String,Object> updateAppMap = null;
			for(String key:module.keyset()){
				 sf.append(","+key);
				 qsf.append(",?");
			}
			if("deleteAdd".equals(importModel)){
				long curFigId = getFigureId("networkresourcemanage");
				for(String updateId:updateIds){
					updateIdMap.put(updateId, updateId);
				}
				updateAppMap = this.physicalresService.getApplicationEntityMap(updateIdMap,importEntityType);
				String resChinese = "";
				if(importEntityType != null){
					try {
						List<BasicEntity> entry = null;
							entry = dictionary.getEntry(importEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							resChinese = entry.get(0).getValue("display");
								//System.out.println(infoNameChinese);
					} catch (EntryOperationException e) {
						e.printStackTrace();
					}
				}
				StringBuffer deletenmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,_entityId,_entityType) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				connection = DataSourceConn.initInstance().getConnection();  //new Conn().getConnection();
				connection.setAutoCommit(false);
				deletenmrSqlpstmt = connection.prepareStatement(deletenmrSql.toString());
				for(String key:updateAppMap.keySet()){
					ApplicationEntity ae =(ApplicationEntity)updateAppMap.get(key);
					int result = this.structureCommonService.delEntityByRecursion(ae, "networkresourcemanage");
					if(result>0){
						ResourceMaintenance maintenance = new ResourceMaintenance();
						String resName="";
						if(ae.getValue("name") != null){
							resName = ae.getValue("name");
						}else{
							resName = ae.getValue("label");
						}
						ApplicationEntity mAe = maintenance.getApplicationEntity();
						deletenmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
						deletenmrSqlpstmt.setString(2,OperationType.NETWORK);
						deletenmrSqlpstmt.setString(3,opCause);
						deletenmrSqlpstmt.setString(4,opScene);
						deletenmrSqlpstmt.setString(5,OperationType.RESOURCEDELETE);
						deletenmrSqlpstmt.setString(6,importEntityType);
						deletenmrSqlpstmt.setLong(7,Long.valueOf(key));
						deletenmrSqlpstmt.setString(8,"资源被删除前的关键信息<"+resChinese+">："+resName);
						deletenmrSqlpstmt.setString(9,userName);
						deletenmrSqlpstmt.setString(10,userId);
						deletenmrSqlpstmt.setObject(11,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						deletenmrSqlpstmt.setObject(12,0);
						deletenmrSqlpstmt.setLong(13,Long.valueOf(mAe.getValue("_entityId")+""));
						deletenmrSqlpstmt.setString(14,mAe.getValue("_entityType")+"");
						deletenmrSqlpstmt.addBatch();
					}
				}
				qsf.append(",?,?");
				StringBuffer insertSql = new StringBuffer("insert into "+importEntityType.toLowerCase());
				insertSql = insertSql.append("("+sf.substring(1,sf.length())+",_entityId,_entityType) values ("+qsf.substring(1,qsf.length())+")");
				StringBuffer fnSql = new StringBuffer("insert into figurenode(id,figureId,entityId,entityType,birthDate,_entityType) values (?,?,?,?,?,?)");
				StringBuffer nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,_entityId,_entityType) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				insertSqlpstmt = connection.prepareStatement(insertSql.toString());
				fnSqlpstmt = connection.prepareStatement(fnSql.toString());
				nmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
				
				for(int index=0;index<rows.size();index++){
					if(listMap.get(index)!=null){
						Map<String,String> map = listMap.get(index);
						map.put("id", structureCommonService.getEntityPrimaryKey(importEntityType)+"");
						importApp = module.createApplicationEntity();
						for(String key:map.keySet()){
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(importApp, varType, key, map.get(key));
						}
						int indexFlag=1;
						for(String key:module.keyset()){
							String type = module.getAttribute(key).getValue("type")+"";
							if(map.containsKey(key)){
								if(type.indexOf("String")>=0 ){
									insertSqlpstmt.setString(indexFlag,map.get(key));
								}else if(type.indexOf("Date")>=0){
									if(map.get(key)!=null && !"".equals(map.get(key))){
										insertSqlpstmt.setObject(indexFlag,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get(key)));
									}else{
										insertSqlpstmt.setDate(indexFlag,null);
									}
								 }else{
									 insertSqlpstmt.setObject(indexFlag,map.get(key));
								 }
							}else{
								 insertSqlpstmt.setObject(indexFlag,null);
							} 
							indexFlag++;
							 
						}
						insertSqlpstmt.setObject(indexFlag,importApp.getValue("_entityId"));
						insertSqlpstmt.setObject(indexFlag+1,importApp.getValue("_entityType"));
						insertSqlpstmt.addBatch();						
						long fId = structureCommonService.getEntityPrimaryKey(Figurenode.MY_TYPE);	
						fnSqlpstmt.setLong(1, fId);
						fnSqlpstmt.setLong(2,curFigId);
						fnSqlpstmt.setLong(3,Long.valueOf(""+importApp.getValue("_entityId")));
						fnSqlpstmt.setString(4,importApp.getValue("_entityType")+"");
						fnSqlpstmt.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
						fnSqlpstmt.setString(6, "Figurenode");
						fnSqlpstmt.addBatch();						
						ResourceMaintenance maintenance = new ResourceMaintenance();
						String resName="";
						if(importApp.getValue("name") != null){
							resName = importApp.getValue("name");
						}else{
							resName = importApp.getValue("label");
						}
						ApplicationEntity mAe = maintenance.getApplicationEntity();
						nmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
						nmrSqlpstmt.setString(2,OperationType.NETWORK);
						nmrSqlpstmt.setString(3,opCause);
						nmrSqlpstmt.setString(4,opScene);
						nmrSqlpstmt.setString(5,OperationType.RESOURCEINSERT);
						nmrSqlpstmt.setString(6,importApp.getValue("_entityType").toString());
						nmrSqlpstmt.setLong(7,(Long)importApp.getValue("id"));
						nmrSqlpstmt.setString(8,resName);
						nmrSqlpstmt.setString(9,userName);
						nmrSqlpstmt.setString(10,userId);
						nmrSqlpstmt.setObject(11,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						nmrSqlpstmt.setObject(12,0);
						nmrSqlpstmt.setLong(13,Long.valueOf(mAe.getValue("_entityId")+""));
						nmrSqlpstmt.setString(14,mAe.getValue("_entityType")+"");
						nmrSqlpstmt.addBatch();
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put("id",importApp.getValue("id"));
						mp.put("row",rows.get(index));
						//resultList.add(mp);
						nameMap.put(fId+"",importApp.getValue("id")+"#"+resName);
						String assId = assIds.get(Integer.valueOf(rows.get(index)))+"";
						if(!"0".equals(assId)){
							if(assMap.containsKey(assId)){
								assMap.put(assId,assMap.get(assId)+","+fId);
							}else{
								assMap.put(assId,fId);
							}
						}else{
							if(rdoAssModel!=null&& !"".equals(rdoAssModel)){
								mp.put("associate", "no");
							}
						}
						resultList.add(mp);
						
					}
				}
				deletenmrSqlpstmt.executeBatch();
				int[] s = insertSqlpstmt.executeBatch();
				int status =0;
				if(s!=null){
					s = fnSqlpstmt.executeBatch();
				}
				if(s!=null){
					s = nmrSqlpstmt.executeBatch();
					status=1;
				}
				connection.commit();
				if(status>0){
					if(rdoAssModel!=null&& !"".equals(rdoAssModel)){
						status=0;
						if(!assMap.isEmpty()){
							List<Map<String,Object>> rList = this.physicalresService.getNodeIdsAndParentNodeIdsMap(assMap, assEntityType);
							Map<String,Object> nodeMap = rList.get(0);
							Map<String,Object> pNameMap = rList.get(1);
							if(nodeMap!=null && !nodeMap.isEmpty()){
								StringBuffer flSql = new StringBuffer("insert into figureline(id,leftId,rightId,linkType,figureId,birthdate,lefttype,righttype,_entityType) values(?,?,?,?,?,?,?,?,?)");
								nmrSql = new StringBuffer("insert into net_maintenance_records(id,biz_module,content,op_cause,op_scene,op_category,res_type,res_id,res_keyinfo,user_name,user_account,op_time,record_type,_entityId,_entityType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
								flSqlpstmt = connection.prepareStatement(flSql.toString());
								flnmrSqlpstmt = connection.prepareStatement(nmrSql.toString());
								for(String id:nodeMap.keySet()){
									flSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey(Figureline.MY_TYPE));
									flSqlpstmt.setLong(2, Long.valueOf(nodeMap.get(id)+""));
									flSqlpstmt.setLong(3, Long.valueOf(id));
									if("parent".equals(assType)){
										flSqlpstmt.setString(4, "CLAN");
									}else{
										flSqlpstmt.setString(4, "LINK");
									}
									flSqlpstmt.setLong(5, curFigId);
									flSqlpstmt.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
									flSqlpstmt.setString(7, assEntityType);
									flSqlpstmt.setString(8, importEntityType);
									flSqlpstmt.setString(9, Figureline.MY_TYPE);
									flSqlpstmt.addBatch();
									String resNameChinese = "";
									if(assEntityType != null){
										try {
											List<BasicEntity> entry = null;
											entry = dictionary.getEntry(assEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
											resNameChinese = entry.get(0).getValue("display");
										} catch (EntryOperationException e) {
											e.printStackTrace();
										}
									}
									//添加资源维护记录
									ResourceMaintenance maintenance = new ResourceMaintenance();
									String lresName = pNameMap.get(nodeMap.get(id)+"")+"";
									String rresName = (nameMap.get(id)+"").substring((nameMap.get(id)+"").indexOf("#")+1);
									String resId = (nameMap.get(id)+"").substring(0,(nameMap.get(id)+"").indexOf("#"));
									String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
									ApplicationEntity mAe = maintenance.getApplicationEntity();
									flnmrSqlpstmt.setLong(1, structureCommonService.getEntityPrimaryKey("Net_Maintenance_Records"));
									flnmrSqlpstmt.setString(2,OperationType.NETWORK);
									flnmrSqlpstmt.setString(3,content);
									flnmrSqlpstmt.setString(4,opCause);
									flnmrSqlpstmt.setString(5,opScene);
									flnmrSqlpstmt.setString(6,OperationType.INSERTLINK);
									flnmrSqlpstmt.setString(7,importEntityType);
									flnmrSqlpstmt.setLong(8,Long.valueOf(resId));
									flnmrSqlpstmt.setString(9,rresName);
									flnmrSqlpstmt.setString(10,userName);
									flnmrSqlpstmt.setString(11,userId);
									flnmrSqlpstmt.setObject(12,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									flnmrSqlpstmt.setObject(13,1);
									flnmrSqlpstmt.setLong(14,Long.valueOf(mAe.getValue("_entityId")+""));
									flnmrSqlpstmt.setString(15,mAe.getValue("_entityType")+"");
									flnmrSqlpstmt.addBatch();
									
								}
								status=0;
								s = flSqlpstmt.executeBatch();
								if(s!=null){
									s = flnmrSqlpstmt.executeBatch();
									status=1;
								}
								connection.commit();
								connection.setAutoCommit(true);
							}
							
						}
						
					}
			}	
			}
			if(resultList.isEmpty()){
				log.error("退出importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),返回结果null");
				return null;
			}else{
				log.error("退出importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),返回结果"+resultList);
				return resultList;
			}
		}catch(Exception e){
			log.error("退出importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),发生异常，数据操作保存失败");
			e.printStackTrace();
			try {
				if(connection!=null){
					connection.rollback();
				}
			} catch (SQLException e1) {
				log.error("退出importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),数据操作回滚失败");				
				e1.printStackTrace();
			}
			return null;
		}finally {
			try {
				if(deletenmrSqlpstmt!=null){
					deletenmrSqlpstmt.close();
				}
				if(insertSqlpstmt!=null){
					insertSqlpstmt.close();
				}
				if(fnSqlpstmt!=null){
					fnSqlpstmt.close();
				}
				if(nmrSqlpstmt!=null){
					nmrSqlpstmt.close();
				}
				if(flSqlpstmt!=null){
					flSqlpstmt.close();
				}
				if(flnmrSqlpstmt!=null){
					flnmrSqlpstmt.close();
				}
				if(connection!=null){
					connection.close();
				}	
			} catch (Exception ex) {
				log.error("退出importDeleteAddResource(String userId, String userName, String importEntityType,String importModel, String opCause, String opScene,String rdoAssModel, String assType, String assEntityType,List<Map<String, String>> listMap, List<String> rows,List<String> assIds, List<String> updateIds),数据连接关闭失败");
			}

		}
		
	}
	/**
	 * 
	 * @description: 取得当前环境figureId
	 * @author：
	 * @return     
	 * @return Long     
	 * @date：Feb 2, 2013 10:39:26 AM
	 */
	public Long getFigureId(String myStructure){
		log.info("进入getFigureId(String myStructure),取得当前环境figureId,myStructure="+myStructure);
		StructureModule structureModule = structureModuleLibrary.getStructureModule("PipeAndCable_ReleatedResource");
		Structure structure = structureModule.myStructure(myStructure);
		Figure figure = structure.getCurrentFigure();
		if(figure!=null){
			log.info("退出getFigureId(String myStructure),返回结果"+figure.getFigureId());
			return figure.getFigureId();
		}else{
			log.info("退出getFigureId(String myStructure),返回结果0");
			return 0L;
		}
		
	}
	
	
	public StructureModuleLibrary getStructureModuleLibrary() {
		return structureModuleLibrary;
	}


	public void setStructureModuleLibrary(
			StructureModuleLibrary structureModuleLibrary) {
		this.structureModuleLibrary = structureModuleLibrary;
	}


	//获取修改前后对象属性修改信息
	public String getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe){
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),获取修改前后对象属性修改信息.");
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),originalAe="+originalAe);
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),updateAe="+updateAe);
		String reString = "";
		ApplicationModule module = moduleLibrary.getModule(originalAe.getType());
		for(String key:module.keyset()){
			//System.out.println(originalAe.getValue("width")+ "     " +updateAe.getValue("width"));
			if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
				if(originalAe.getValue(key) == null && updateAe.getValue(key) != null || ("null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) ) || ("".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString = "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+originalAe.getType()+"的属性字段"+key+"的中文字典失败，可能该字典不存在");
						keyString =  "<"+key+">" ;
					}
					reString = reString + keyString + "，修改前：" + " 空值 " + ";修改后： " + updateAe.getValue(key) + " $_$" + (char)10;
				}else if((originalAe.getValue(key) != null && updateAe.getValue(key) == null ) || (!"null".equals(originalAe.getValue(key)) && "null".equals(updateAe.getValue(key)) ) || (!"".equals(originalAe.getValue(key)) && "".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+originalAe.getType()+"的属性字段"+key+"的中文字典失败，可能该字典不存在");
						keyString =  "<"+key+">";
					}
					reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后：" + " 空值 " + " $_$" + (char)10;
				}else if(originalAe.getValue(key) != null && updateAe.getValue(key) != null  && !"null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) && !"".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key))){
					if(!originalAe.getValue(key).equals(updateAe.getValue(key))){
						String keyString = "";
						try {
							List<BasicEntity> entry = null;
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								
							}
							if(entry != null && !entry.isEmpty()) {
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
								}
							}
						} catch (EntryOperationException e) {
							log.error("获取"+originalAe.getType()+"的属性字段"+key+"的中文字典失败，可能该字典不存在");
							keyString =  "<"+key+">" ;
						}
						reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后： " + updateAe.getValue(key) +" $_$" + (char)10;
					}
				}
			}
		}
		log.info("退出getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),返回结果reString="+reString);
		return reString;
	}

	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}


	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}


	public Dictionary getDictionary() {
		return dictionary;
	}



	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}


	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}



	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}



	public PhysicalresService getPhysicalresService() {
		return physicalresService;
	}



	public void setPhysicalresService(PhysicalresService physicalresService) {
		this.physicalresService = physicalresService;
	}
}
