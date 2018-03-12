package com.iscreate.op.pojo.maintain;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.action.ActionHelper;





public class ServiceMaintenance {
	
	//维护记录唯一标识
	private static String ID = "id";
	
	//业务模块
	private static String BIZ_MODULE = "biz_module";
	
	private static String SERVICE_MAINTENANCE = "Service_Maintenance";
	//业务信息唯一标识
	private static String BIZ_PROCESSCODE = "biz_processcode";
	
	//业务信息唯一标识
	private static String BIZ_PROCESSID = "biz_processId";
	
	//资源维护记录ID
	private static String MAINTENANCE_ID = "maintenance_id";
	
	private ApplicationEntity applicationEntity;
	
	
	
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}



	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
	
	
	public String getBiz_processcode() {
		if(applicationEntity.getValue(this.BIZ_PROCESSCODE) == null){
			return null;
		}
		return applicationEntity.getValue(this.BIZ_PROCESSCODE).toString();
	}



	public void setBiz_processcode(String biz_processcode) {
		applicationEntity.setValue(this.BIZ_PROCESSCODE, biz_processcode);
	}



	public long getBiz_rocessId() {
		if(applicationEntity.getValue(this.BIZ_PROCESSID) == null){
			return 0;
		}
		return Long.parseLong(applicationEntity.getValue(this.BIZ_PROCESSID).toString());
	}



	public void setBiz_processId(long biz_processId) {
		applicationEntity.setValue(this.BIZ_PROCESSID, biz_processId);
	}
	
	public ServiceMaintenance(){
		applicationEntity = ActionHelper.getApplicationEntity(SERVICE_MAINTENANCE);
	}
	
	
	public String getBiz_module() {
		if(applicationEntity.getValue(this.BIZ_MODULE) == null){
			return null;
		}
		return applicationEntity.getValue(this.BIZ_MODULE).toString();
	}



	public void setBiz_module(String biz_module) {
		applicationEntity.setValue(this.BIZ_MODULE, biz_module);
	}
	
	
	public void setId(long Id){
		applicationEntity.setValue(this.ID, Id);
	}

	public long getId(){
		if(applicationEntity.getValue(this.ID) == null){
			return 0;
		}
		return Long.parseLong(applicationEntity.getValue(this.ID).toString());
	}
	
	public void setMaintenance_id(long maintenance_id){
		applicationEntity.setValue(this.MAINTENANCE_ID, maintenance_id);
	}

	public long getMaintenance_id(){
		if(applicationEntity.getValue(this.MAINTENANCE_ID) == null){
			return 0;
		}
		return Long.parseLong(applicationEntity.getValue(this.MAINTENANCE_ID).toString());
	}

}
