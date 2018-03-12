package com.iscreate.op.service.maintain;

import java.util.Date;
import java.util.List;

import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.maintain.ServiceMaintenance;

public interface NetworkResourceMaintenanceService {

	/**
	 * 保存资源维护记录(系统强制)
	 * 
	 * @param maintenance
	 */
	public abstract int insertResourceMaintenanceRecordsBySystemForces(
			ResourceMaintenance maintenance);

	/**
	 * 保存资源维护记录(业务使用)
	 * 
	 * @param maintenance
	 */
	public int insertResourceMaintenanceRecordsByBizUse(ResourceMaintenance maintenance);
	
	
	/**
	 * 资源维护记录检索查询
	 * @param maintenance
	 * @return List<ResourceMaintenance>
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecords(
			ResourceMaintenance maintenance);

	/**
	 * 根据ID查询资源维护记录
	 * @param id
	 * @return ResourceMaintenance
	 */
	public abstract ResourceMaintenance getResourceMaintenanceRecordsById(
			long id);

	/**
	 * 根据ID集合查询资源维护记录
	 * @param ids
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByIds(
			List<Long> ids);

	/**
	 * 根据资源类型与资源ID查询资源维护记录
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResId(
			String res_type, long res_id);
	
	/**
	 * 根据资源类型与资源ID与时间查询资源维护记录
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResIdAndTime(String res_type,long res_id,String sTime,String eTime);

	/**
	 * 根据资源类型查询资源维护记录
	 * @param res_type
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByResType(
			String res_type);

	/**
	 * 根据资源ID查询资源维护记录
	 * @param res_id
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByResId(
			long res_id);

	/**
	 * 根据业务模块查询资源维护记录
	 * @param biz_module
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModule(
			String biz_module);

	/**
	 * 根据人员账号查询资源维护记录
	 * @param user_account
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByUserAccount(
			String user_account);

	/**
	 * 根据时间范围查询资源维护记录
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByOPTimeRange(
			Date startTime, Date endTime);

	/**
	 * 根据日期查询资源维护记录
	 * @param findDay
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByDay(
			Date findDay);

	/**
	 * 根据记录类型查询资源维护记录
	 * @param record_type
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByRecordType(
			int record_type);

	/**
	 * 根据记录类型与业务模块名称查询资源维护记录
	 * @param record_type
	 * @param biz_module
	 * @return
	 */
	public abstract List<ResourceMaintenance> getResourceMaintenanceRecordsByRecordTypeAndBizModule(
			int record_type, String biz_module);
	
	
	/**
	 * 根据业务模块与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizProcessCode(String biz_module,String biz_processcode);
	
	/**
	 * 根据业务模块与业务信息id查询资源维护记录
	 * @param biz_module
	 * @param biz_rocessId
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessId(String biz_module,String biz_rocessId);
	
	
	/**
	 * 根据业务模块与业务信息id与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @param biz_rocessId
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCode(String biz_module,String biz_processcode,String biz_rocessId);
	
	
	/**
	 * 根据业务模块与业务信息id与业务信息唯一标识查询资源维护记录
	 * @param biz_module
	 * @param biz_processcode
	 * @param biz_rocessId
	 * @param res_type
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype(String biz_module,String biz_processcode,String rocessId,String res_type);

	public int insertServiceMaintenance(ServiceMaintenance serviceMaintenance);
	
	/**
	 * 资源维护记录检索查询
	 * @param maintenance
	 * @return List<ResourceMaintenance>
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsLIKE(ResourceMaintenance maintenance);
	
	/**
	 * 根据资源类型与资源ID查询资源维护记录（分页）
	 * @param res_type
	 * @param res_id
	 * @return
	 */
	public List<ResourceMaintenance> getResourceMaintenanceRecordsByResTypeAndResIdLIMIT(String res_type,String res_id,String start, String pageNum);

}