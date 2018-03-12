package com.iscreate.plat.networkresource.structure.template;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
  


public class StructurePrimary {
	
	
	
	
	/**
	 * 获取主键
	 * @param EntityType entity类型
	 * @return long
	 */
	public static long getEntityPrimaryKey(String EntityType,Context context){
		long retId = 0;
		if(context == null){
			return 0;
		}
		String uuIdSql = "select "+ResourceCommon.getSelectSqlAttributsString("Tbl_Unique_Uniquetable")+" from tbl_unique_uniquetable where name = '"+EntityType+"_id'";
		//System.out.println("uuIdSql:"+uuIdSql);
		SqlContainer uuIdSqlc = context.createSqlContainer(uuIdSql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Tbl_Unique_Uniquetable");
		long uuIdAeId = 0;
		if(uuIdList != null && uuIdList.size() != 0){
			if(uuIdList.get(0).getValue("code") != null){
				uuIdAeId = new Long(uuIdList.get(0).getValue("code").toString());
			}
		}
		long selectMAXIdAeId = 0;
		if(!"TreeEngine".equals(EntityType) && !"Treenode".equals(EntityType) && !"Tree".equals(EntityType) ){
			String selectMAXIdSql = "";
			////System.out.println(EntityType);
			if("Figure".equals(EntityType)){
				selectMAXIdSql = "select max(figureId) \"max(figureId)\" as maxid from "+EntityType;
			}else{
				selectMAXIdSql = "select max(id)  \"maxid\" from "+EntityType;
			}
			//System.out.println("uuIdSql:"+selectMAXIdSql);
			SqlContainer selectMAXIdSqlc = context.createSqlContainer(selectMAXIdSql);
			List<BasicEntity> selectMAXIdList = context.executeSelectSQL(selectMAXIdSqlc,EntityType);
			if(selectMAXIdList != null && selectMAXIdList.size() != 0){
				if(selectMAXIdList.get(0).getValue("maxid") != null){
					selectMAXIdAeId = new Long(selectMAXIdList.get(0).getValue("maxid").toString());
				}
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
		if("insert".equals(type)){
			long insertId = uuIdAeId +1;
			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");
			tuuAe.setValue("code", insertId);
			tuuAe.setValue("name", EntityType+"_id");
//			String insertSql = "INSERT INTO tbl_unique_uniquetable (code,name) values ("+insertId+",'"+EntityType+"_id');";
//			SqlContainer insertSqlc = context.createSqlContainer(insertSql);
//			context.executeInsertSQL(insertSqlc, "Tbl_Unique_Uniquetable");
			//Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
			Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(tuuAe);
			String sqlString ="insert into Tbl_Unique_Uniquetable("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
			//System.out.println(sqlString);
			SqlContainer sc = context.createSqlContainer(sqlString);
			context.executeInsertSQL(sc, "Tbl_Unique_Uniquetable");
			//context.insert(tuuAe);
			
		}else{
			long updateId = uuIdAeId +1;
			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");
			tuuAe.setValue("code", updateId);
			tuuAe.setValue("name", EntityType+"_id");
//			String insertSql = "UPDATE tbl_unique_uniquetable SET code = "+updateId+" where name = '"+EntityType+"_id';";
//			SqlContainer insertSqlc = context.createSqlContainer(insertSql);
//			context.executeUpdateSQL(insertSqlc, "Tbl_Unique_Uniquetable");
			
			/*Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
			query.add(Restrictions.eq("name", EntityType+"_id"));*/
			//////System.out.println(query.getQuerySqlString());
			String sqlString ="update Tbl_Unique_Uniquetable set "+ResourceCommon.getUpdateAttributesSqlString(tuuAe)+" where name='"+EntityType+"_id'";
			SqlContainer sc = context.createSqlContainer(sqlString);
			//System.out.println(sqlString);
			context.executeUpdateSQL(sc, "Tbl_Unique_Uniquetable");
			//context.update(tuuAe, query);
		}
		////System.out.println(uuIdAeId);
		return uuIdAeId;
	}
	
	
	
	
//	public static void saveCurrentEntityId(){
//		Context context = contextFactory.CreateContext();
//		ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");//以下更新记录
//		tuuAe.setValue("code", entityId);
//		tuuAe.setValue("name", "entityId_id");
//		Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
//		query.add(Restrictions.eq("name", "entityId_id"));
//		context.update(tuuAe, query);
//	}
}
