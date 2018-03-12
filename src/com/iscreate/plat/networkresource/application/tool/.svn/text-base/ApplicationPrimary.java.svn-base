package com.iscreate.plat.networkresource.application.tool;



import java.util.Arrays;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

public class ApplicationPrimary {
	
private static boolean isInit = false;
	
	private static ContextFactory contextFactory;

	private static long entityId = 0;
	
	public ContextFactory getContextFactory() {
		return contextFactory;
	}
	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}
	private void init()
	{
		this.getCurrentEntityId();
		isInit = true;
	}
	
	/**
	 * 
	 * 
	 * @description:生成当前_entityId
	 * @author：
	 * @return     
	 * @return long     
	 * @date：Jul 31, 2012 4:56:12 PM
	 */
	public static long getCurrentEntityId(){
//		long _entityId=0;
//		if(contextFactory == null){
//			return 0;
//		}
//		
//		Context context = contextFactory.CreateContext();
//		String uuIdSql = "select * from tbl_unique_uniquetable where name = 'entityId_id'";// Tbl_Unique_Uniquetable查询当前_entityId最大值，木有要设置插入。
//		SqlContainer uuIdSqlc = context.createSqlContainer(uuIdSql);
//		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Tbl_Unique_Uniquetable");
//		long uuIdAeId = 0;
//		if(uuIdList != null && uuIdList.size() != 0){
//			if(uuIdList.get(0).getValue("code") != null){
//				uuIdAeId = new Long(uuIdList.get(0).getValue("code").toString());
//			}
//		}
//		String entityMaxEntityIdSql = "select max(_entityId) as maxid from "+EntityType;
//		SqlContainer selectMAXIdSqlc = context.createSqlContainer(entityMaxEntityIdSql);
//		List<BasicEntity> selectMAXIdList = context.executeSelectSQL(selectMAXIdSqlc,EntityType);
//		long entityMaxIdAeId = 0;
//		if(selectMAXIdList != null && selectMAXIdList.size() != 0){
//			if(selectMAXIdList.get(0).getValue("maxid") != null){//当前查询表中_entityId最大值
//				entityMaxIdAeId = new Long(selectMAXIdList.get(0).getValue("maxid").toString());
//			}
//		}
//		String figurenodeMaxEntityIdSql = "select max(entityId) as maxid from figurenode";
//		SqlContainer figurenodeMAXIdSqlc = context.createSqlContainer(figurenodeMaxEntityIdSql);
//		List<BasicEntity> figurenodeMaxIdList = context.executeSelectSQL(figurenodeMAXIdSqlc,"Figurenode");
//		long figurenodeMaxIdAeId = 0;
//		if(figurenodeMaxIdList != null && figurenodeMaxIdList.size() != 0){
//			if(figurenodeMaxIdList.get(0).getValue("maxid") != null){//表figurenode中_entityId最大值
//				figurenodeMaxIdAeId = new Long(figurenodeMaxIdList.get(0).getValue("maxid").toString());
//			}
//		}
//		if(uuIdAeId==0){//Tbl_Unique_Uniquetable无记录时
//			if(entityMaxIdAeId>=figurenodeMaxIdAeId){
//				_entityId=entityMaxIdAeId+1;
//			}else{
//				_entityId=figurenodeMaxIdAeId+1;
//			}
//			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");//以下插入记录
//			tuuAe.setValue("code", _entityId);
//			tuuAe.setValue("name", "entityId_id");
//			context.insert(tuuAe);
//		}else{//Tbl_Unique_Uniquetable有记录时
//			long[] idArray = {uuIdAeId,entityMaxIdAeId,figurenodeMaxIdAeId};
//			Arrays.sort(idArray);
//			_entityId=idArray[2]+1;//取最大的值
//			ApplicationEntity tuuAe = ActionHelper.getApplicationEntity("Tbl_Unique_Uniquetable");//以下更新记录
//			tuuAe.setValue("code", _entityId);
//			tuuAe.setValue("name", "entityId_id");
//			Query query = context.createQueryBuilder("Tbl_Unique_Uniquetable");
//			query.add(Restrictions.eq("name", "entityId_id"));
//			context.update(tuuAe, query);
//			
//		}
//		
//		return _entityId;
		if(isInit == false){
			long _entityId=0;
			if(contextFactory == null){
				return 0;
			}
			
			Context context = contextFactory.CreateContext();
			String uuIdSql = "select "+ResourceCommon.getSelectSqlAttributsString("Tbl_Unique_Uniquetable")+" from tbl_unique_uniquetable where name = 'entityId_id'";// Tbl_Unique_Uniquetable查询当前_entityId最大值，木有要设置插入。
			////System.out.println("uuIdSql:"+uuIdSql);
			SqlContainer uuIdSqlc = context.createSqlContainer(uuIdSql);
			List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Tbl_Unique_Uniquetable");
			long uuIdAeId = 0;
			if(uuIdList != null && uuIdList.size() != 0){
				if(uuIdList.get(0).getValue("code") != null){
					uuIdAeId = new Long(uuIdList.get(0).getValue("code").toString());
				}
			}
		
			String figurenodeMaxEntityIdSql = "select max(entityId)  \"maxid\" from figurenode";
			////System.out.println("figurenodeMaxEntityIdSql:"+figurenodeMaxEntityIdSql);
			SqlContainer figurenodeMAXIdSqlc = context.createSqlContainer(figurenodeMaxEntityIdSql);
			List<BasicEntity> figurenodeMaxIdList = context.executeSelectSQL(figurenodeMAXIdSqlc,"Figurenode");
			long figurenodeMaxIdAeId = 0;
			if(figurenodeMaxIdList != null && figurenodeMaxIdList.size() != 0){
				if(figurenodeMaxIdList.get(0).getValue("maxid") != null){//表figurenode中_entityId最大值
					figurenodeMaxIdAeId = new Long(figurenodeMaxIdList.get(0).getValue("maxid").toString());
				}
			}
			if(uuIdAeId==0){//Tbl_Unique_Uniquetable无记录时
					_entityId=figurenodeMaxIdAeId+1;
				entityId = _entityId;
				
			}else{//Tbl_Unique_Uniquetable有记录时
				long[] idArray = {uuIdAeId,figurenodeMaxIdAeId};
				Arrays.sort(idArray);
				_entityId=idArray[1]+1;//取最大的值
				entityId = _entityId;
				
				
			}
		}else {
			entityId++;
		}
		
		
		return entityId;
	}
}
