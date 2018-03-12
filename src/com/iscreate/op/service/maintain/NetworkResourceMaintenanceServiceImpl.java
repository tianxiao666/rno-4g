package com.iscreate.op.service.maintain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.maintain.ServiceMaintenance;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.Query;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.structure.template.StructurePrimary;

public class NetworkResourceMaintenanceServiceImpl implements NetworkResourceMaintenanceService {
	
	public ContextFactory contextFactory;
	

	private StaffOrganizationService staffOrganizationService;
	
	
	private  static final  Log log = LogFactory.getLog(NetworkResourceMaintenanceServiceImpl.class);


	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}



	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}



	public ContextFactory getContextFactory() {
		return contextFactory;
	}



	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}


	/**
	 * 保存资源维护记录(系统强制)
	 * 
	 * @param maintenance
	 */
	public int insertResourceMaintenanceRecordsBySystemForces(ResourceMaintenance maintenance){
		log.info("进入===insertResourceMaintenanceRecordsBySystemForces方法");
		//获取登录人ID
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = "";
		String userName = "";
		if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
			userId = session.getAttribute("userId").toString();
			Map<String, String> userByUserId = this.staffOrganizationService.getUserByUserId(userId);
			if(userByUserId != null){
				userName = userByUserId.get("name");
				////System.out.println(userName);
			}
		}
//		if(session.getAttribute("userName") != null && !"".equals(session.getAttribute("userName"))){	
//			userName = session.getAttribute("userName").toString();
//		}
		Context context = contextFactory.CreateContext();
		maintenance.setOp_time(new Date());
		long reId = StructurePrimary.getEntityPrimaryKey(maintenance.getApplicationEntity().getType(), context);
		maintenance.setId(reId);
		maintenance.setRecord_type(1);
		maintenance.setUser_account(userId);
		maintenance.setUser_name(userName);
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(maintenance.getApplicationEntity());
		String sqlString = "insert into Net_Maintenance_Records ("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int insert = context.executeInsertSQL(sc, "Net_Maintenance_Records");
		//int insert = context.insert(maintenance.getApplicationEntity());
		int retId = 0;
		if(insert > 0){
			retId = Integer.parseInt(reId+"");
		}
		log.info("退出===insertResourceMaintenanceRecordsBySystemForces方法 返回值为："+retId);
		return retId;
	}
	
	/**
	 * 保存资源维护记录(业务使用)
	 * 
	 * @param maintenance
	 */
	public int insertResourceMaintenanceRecordsByBizUse(ResourceMaintenance maintenance){
		log.info("进入===insertResourceMaintenanceRecordsByBizUse方法");
		//获取登录人ID
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = "";
		String userName = "";
		if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
			userId = session.getAttribute("userId").toString();
			Map<String, String> userByUserId = this.staffOrganizationService.getUserByUserId(userId);
			if(userByUserId != null){
				userName = userByUserId.get("name");
				////System.out.println(userName);
			}
		}
//		if(session.getAttribute("userName") != null && !"".equals(session.getAttribute("userName"))){	
//			userName = session.getAttribute("userName").toString();
//		}
		Context context = contextFactory.CreateContext();
		maintenance.setOp_time(new Date());
		long reId = StructurePrimary.getEntityPrimaryKey(maintenance.getApplicationEntity().getType(), context);
		maintenance.setId(reId);
		maintenance.setRecord_type(0);
		maintenance.setUser_account(userId);
		maintenance.setUser_name(userName);
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(maintenance.getApplicationEntity());
		String sqlString = "insert into Net_Maintenance_Records ("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int insert = context.executeInsertSQL(sc, "Net_Maintenance_Records");
		//int insert = context.insert(maintenance.getApplicationEntity());
		
		int retId = 0;
		if(insert > 0){
			retId = Integer.parseInt(reId+"");
		}
		log.info("退出===insertResourceMaintenanceRecordsByBizUse方法 返回值为："+retId);
		return retId;
	}
	
	public int insertServiceMaintenance(ServiceMaintenance serviceMaintenance){
		log.info("进入===insertServiceMaintenance方法");
		Context context = contextFactory.CreateContext();
		long reId = StructurePrimary.getEntityPrimaryKey(serviceMaintenance.getApplicationEntity().getType(), context);
		serviceMaintenance.setId(reId);
		Map<String,String> mp = ResourceCommon.getInsertAttributesAndValuesStringMap(serviceMaintenance.getApplicationEntity());
		String sqlString = "insert into Net_Maintenance_Records ("+mp.get("attrStr")+") values("+mp.get("valueStr")+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int insert = context.executeInsertSQL(sc, "Net_Maintenance_Records");
		//int insert = context.insert(serviceMaintenance.getApplicationEntity());
		log.info("进入===insertServiceMaintenance方法 返回值为："+insert);
		return insert;
		
	}
	
	
	/**
	 * 资源维护记录检索查询
	 * @param maintenance
	 * @return List<ResourceMaintenance>
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecords(ResourceMaintenance maintenance){
		log.info("进入===getResourceMaintenanceRecords方法");
		List<ResourceMaintenance> list = null;
		ApplicationEntity aemaintenance = maintenance.getApplicationEntity();
		Map<String, Object> map = aemaintenance.toMap();
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(aemaintenance.getType());
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records ";
		if(map != null && map.size() > 0) {
			sql = sql + " where ";
			log.info("开始循环map");
			for(String key : map.keySet()) {
				if(!key.equals("_entityType") && !key.equals("_entityId")){
					if(map.get(key) != null && !map.get(key).equals("")){
						if(map.get(key).getClass() == Date.class){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String timeString = sdf.format((Date)map.get(key));
							sql = sql + key+" = to_date('"+timeString+"','yyyy-mm-dd hh24:mi:ss') and ";
						}else{
							sql = sql + key+" = '"+map.get(key)+"' and ";
						}
//						//System.out.println(map.get(key).getClass());
					}	
				}
			}
			log.info("结束循环map");
//			//System.out.println(sql.lastIndexOf("and"));
			sql = sql.substring(0, sql.lastIndexOf("and"));
		}
		sql = sql + " ORDER BY id DESC ";
		log.info("sql:"+sql);
		//System.out.println("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecords方法 返回值为："+list);
		return list;
	}

	/**
	 * 根据ID查询资源维护记录
	 * @param id
	 * @return ResourceMaintenance
	 */
	public ResourceMaintenance getResourceMaintenanceRecordsById(long id){
		log.info("进入===getResourceMaintenanceRecordsById方法");
		ResourceMaintenance resourceMaintenance = new ResourceMaintenance();
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(resourceMaintenance.getApplicationEntity().getType());
		//query.add(Restrictions.eq("id", id));
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" from Net_Maintenance_Records where id="+id;
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		List<BasicEntity> listBe = context.executeSelectSQL(sc, "Net_Maintenance_Records");
		BasicEntity entity =null;
		if(listBe!=null){
			entity=listBe.get(0);
			resourceMaintenance.setApplicationEntity(ApplicationEntity.changeFromEntity(entity));
		}
		//int insert = context.executeInsertSQL(sc, "Net_Maintenance_Records");
		//BasicEntity entity = context.queryEntity(query);
		log.info("退出===getResourceMaintenanceRecordsById方法 返回值为："+resourceMaintenance);
		return resourceMaintenance;
	}
	
	
	/**
	 * 根据ID集合查询资源维护记录
	 * @param ids
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByIds(List<Long> ids){
		log.info("进入===getResourceMaintenanceRecordsByIds方法");
		List<ResourceMaintenance> list = null;
		String idString = "";
		if(ids != null && ids.size() > 0){
			log.info("开始循环ids");
			for(long s:ids){
				idString = idString + Long.toString(s) + ",";
			}
			log.info("结束循环ids");
		}
		if(idString != null && !idString.equals("")){
			idString = idString.substring(0,idString.length()-1);
		}
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where id in ("+idString+") ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByIds方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据资源类型与资源ID查询资源维护记录
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResId(String res_type,long res_id){
		log.info("进入===getResourceMaintenanceRecordsByResTypeAndResId方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where res_type = '"+res_type+"' and res_id = "+res_id+" ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByResTypeAndResId方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据资源类型与资源ID与时间查询资源维护记录
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResIdAndTime(String res_type,long res_id,String sTime,String eTime){
		log.info("进入===getResourceMaintenanceRecordsByResTypeAndResIdAndTime方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where res_type = '"+res_type+"' and res_id = "+res_id+" and op_time >= to_date('"+sTime+"','yyyy-mm-dd hh24:mi:ss') and op_time < to_date('"+eTime+"','yyyy-mm-dd hh24:mi:ss') ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByResTypeAndResIdAndTime方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据资源类型查询资源维护记录
	 * @param res_type
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResType(String res_type){
		log.info("进入===getResourceMaintenanceRecordsByResType方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where res_type = '"+res_type+"'  ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByResType方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据资源ID查询资源维护记录
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResId(long res_id){
		log.info("进入===getResourceMaintenanceRecordsByResId方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where res_id = '"+res_id+"'  ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByResId方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据业务模块查询资源维护记录
	 * @param biz_module
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModule(String biz_module){
		log.info("进入===getResourceMaintenanceRecordsByBizModule方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where biz_module = '"+biz_module+"'  ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByBizModule方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据人员账号查询资源维护记录
	 * @param user_account
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByUserAccount(String user_account){
		log.info("进入===getResourceMaintenanceRecordsByUserAccount方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where user_account = '"+user_account+"'  ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByUserAccount方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据时间范围查询资源维护记录
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByOPTimeRange(Date startTime,Date endTime){
		log.info("进入===getResourceMaintenanceRecordsByOPTimeRange方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimeString = sdf.format(startTime);
		String endTimeString = sdf.format(endTime);
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where op_time >= to_date('"+startTimeString+"','yyyy-mm-dd hh24:mi:ss') and op_time <= to_date('"+endTimeString+"','yyyy-mm-dd hh24:mi:ss') ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByOPTimeRange方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据日期查询资源维护记录
	 * @param findDay
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByDay(Date findDay){
		log.info("进入===getResourceMaintenanceRecordsByDay方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String findDayString = sdf.format(findDay);
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where  to_date(op_time,'yyyy-mm-dd') = to_date('"+findDayString+"','yyyy-mm-dd hh24:mi:ss') ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByDay方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据记录类型查询资源维护记录
	 * @param record_type
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByRecordType(int record_type){
		log.info("进入===getResourceMaintenanceRecordsByRecordType方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where  record_type  = '"+record_type+"' ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByRecordType方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据记录类型与业务模块名称查询资源维护记录
	 * @param record_type
	 * @param biz_module
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByRecordTypeAndBizModule(int record_type,String biz_module){
		log.info("进入===getResourceMaintenanceRecordsByRecordTypeAndBizModule方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records where  record_type  = '"+record_type+"' and biz_module = '"+biz_module+"' ORDER BY id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByRecordTypeAndBizModule方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据业务模块与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizProcessCode(String biz_module,String biz_processcode){
		log.info("进入===getResourceMaintenanceRecordsByBizModuleAndBizProcessCode方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records","n")+"  FROM Net_Maintenance_Records  n,service_maintenance  s WHERE  s.biz_module  = '"+biz_module+"' AND s.biz_processcode = '"+biz_processcode+"' AND n.id = s.maintenance_id ORDER BY s.maintenance_id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByBizModuleAndBizProcessCode方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据业务模块与业务信息id查询资源维护记录
	 * @param biz_module
	 * @param biz_rocessId
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessId(String biz_module,String biz_rocessId){
		log.info("进入===getResourceMaintenanceRecordsByBizModuleAndBizRocessId方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records","n")+"  FROM Net_Maintenance_Records  n,service_maintenance  s WHERE  s.biz_module  = '"+biz_module+"' and s.biz_rocessId = '"+biz_rocessId+"' AND n.id = s.maintenance_id  ORDER BY s.maintenance_id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByBizModuleAndBizRocessId方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据业务模块与业务信息id与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @param biz_rocessId
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCode(String biz_module,String biz_processcode,String biz_rocessId){
		log.info("进入===getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCode方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records","n")+"  FROM Net_Maintenance_Records  n,service_maintenance  s WHERE  s.biz_module  = '"+biz_module+"' and s.biz_rocessId = '"+biz_rocessId+"' and s.biz_processcode = '"+biz_processcode+"' AND n.id = s.maintenance_id  ORDER BY s.maintenance_id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCode方法 返回值为："+list);
		return list;
	}
	
	/**
	 * 根据业务模块与业务信息id与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @param biz_rocessId
	 * @param res_type
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype(String biz_module,String biz_processcode,String rocessId,String res_type){
		log.info("进入===getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records","n")+"  FROM Net_Maintenance_Records  n,service_maintenance  s WHERE  s.biz_module  = '"+biz_module+"'  and s.biz_processcode = '"+biz_processcode+"' and n.res_type = '"+res_type+"' and n.res_id = '"+rocessId+"' AND n.id = s.maintenance_id  ORDER BY s.maintenance_id DESC";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype方法 返回值为："+list);
		return list;
	}
	

	/**
	 * 转换List数据类型
	 * @param queryList
	 * @return
	 */
	private List<ResourceMaintenance> convertListType(
			List<BasicEntity> queryList) {
		log.info("进入===convertListType方法");
		List<ResourceMaintenance> list = null;
		if(queryList != null && queryList.size() > 0){
			list = new ArrayList<ResourceMaintenance>();
			log.info("开始循环queryList");
			for(BasicEntity be:queryList){
				ResourceMaintenance resourceMaintenance = new ResourceMaintenance();
				resourceMaintenance.setApplicationEntity(ApplicationEntity.changeFromEntity(be));
				list.add(resourceMaintenance);
			}
			log.info("结束循环queryList");
		}
		log.info("退出===convertListType方法 返回值为:"+list);
		return list;
	}
	
	/**
	 * 资源维护记录检索查询
	 * @param maintenance
	 * @return List<ResourceMaintenance>
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsLIKE(ResourceMaintenance maintenance){
		log.info("进入===getResourceMaintenanceRecordsLIKE方法");
		List<ResourceMaintenance> list = null;
		ApplicationEntity aemaintenance = maintenance.getApplicationEntity();
		Map<String, Object> map = aemaintenance.toMap();
		Context context = contextFactory.CreateContext();
		Query query = context.createQueryBuilder(aemaintenance.getType());
		String sql = "SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+" FROM Net_Maintenance_Records ";
		
		if(map != null && map.size() > 0) {
			sql = sql + " where ";
			log.info("开始循环map");
			for(String key : map.keySet()) {
				if(!key.equals("_entityType") && !key.equals("_entityId")){
					if(map.get(key) != null && !map.get(key).equals("")){
						if(map.get(key).getClass() == Date.class){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String timeString = sdf.format((Date)map.get(key));
							sql = sql + key+" = to_date('"+timeString+"','yyyy-mm-dd hh24:mi:ss') and ";
						}else{
							sql = sql + key+" like '%"+map.get(key)+"%' and ";
						}
//						//System.out.println(map.get(key).getClass());
					}	
				}
			}
			log.info("结束循环map");
//			//System.out.println(sql.lastIndexOf("and"));
			sql = sql.substring(0, sql.lastIndexOf("and"));
		}
		sql = sql + " ORDER BY id DESC ";
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsLIKE方法 返回值为："+list);
		return list;
	}
	
	
	/**
	 * 根据资源类型与资源ID查询资源维护记录（分页）
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResIdLIMIT(String res_type,String res_id,String start, String pageNum){
		log.info("进入===getResourceMaintenanceRecordsByResTypeAndResIdLIMIT方法");
		List<ResourceMaintenance> list = null;
		Context context = contextFactory.CreateContext();
		int end = Integer.parseInt(start)+Integer.parseInt(pageNum);
		String sql = "SELECT * FROM (SELECT "+ResourceCommon.getSelectSqlAttributsString("Net_Maintenance_Records")+",rownum rc FROM Net_Maintenance_Records where res_type = '"+res_type+"' and res_id = "+res_id+" and rownum<= "+end+" ORDER BY id DESC ) nm where rc>"+start;
		log.info("sql:"+sql);
		SqlContainer uuIdSqlc = context.createSqlContainer(sql);
		List<BasicEntity> uuIdList = context.executeSelectSQL(uuIdSqlc,"Net_Maintenance_Records");
		list = convertListType(uuIdList);
		log.info("退出===getResourceMaintenanceRecordsByResTypeAndResIdLIMIT方法 返回值为："+list);
		return list;
	}
}
