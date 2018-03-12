package com.iscreate.op.pojo.maintain;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.action.ActionHelper;



public class ResourceMaintenance {
	
	
	private static String NETWORK_RESOURCE_MAINTENANCE_RECORDS = "Net_Maintenance_Records";
	
	
	//维护记录唯一标识
	private static String ID = "id";
	
	//业务模块
	private static String BIZ_MODULE = "biz_module";
	
	//操作原因
	private static String OP_CAUSE = "op_cause";
	
	//连接
	private static String LINKURL = "linkurl";
	
	//操作场景
	private static String OP_SCENE = "op_scene";
	
	//操作分类
	private static String OP_CATEGORY = "op_category";
	
	//资源类型
	private static String RES_TYPE = "res_type";
	
	//资源唯一标识
	private static String RES_ID = "res_id";
	
	//资源对象关键字
	private static String RES_KEYINFO = "res_keyinfo";
	
	//详细内容
	private static String CONTENT = "content";
	
	//操作人
	private static String USER_NAME = "user_name";
	
	//操作账号
	private static String USER_ACCOUNT = "user_account";
	
	//终端设备
	private static String SRC_TEMINAL = "src_teminal";
	
	//经度
	private static String LONGITUDE = "longitude";
	
	//维度
	private static String LATITUDE = "latitude";
	
	//记录类型(0.业务调用,1.系统强制,2.其他)
	private static String RECORD_TYPE = "record_type";
	
	//资源记录时间
	private static String OP_TIME = "op_time";
	
	
	
	
	
	private ApplicationEntity applicationEntity;
	
	
	
	public ApplicationEntity getApplicationEntity() {
		return applicationEntity;
	}



	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}



	public ResourceMaintenance(){
		applicationEntity = ActionHelper.getApplicationEntity(NETWORK_RESOURCE_MAINTENANCE_RECORDS);
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



	public String getOp_cause() {
		if(applicationEntity.getValue(this.OP_CAUSE) == null){
			return null;
		}
		return applicationEntity.getValue(this.OP_CAUSE).toString();
	}



	public void setOp_cause(String op_cause) {
		applicationEntity.setValue(this.OP_CAUSE, op_cause);
	}



	public String getLinkurl() {
		if(applicationEntity.getValue(this.LINKURL) == null){
			return null;
		}
		return applicationEntity.getValue(this.LINKURL).toString();
	}



	public void setLinkurl(String linkurl) {
		applicationEntity.setValue(this.LINKURL, linkurl);
	}



	public String getOp_scene() {
		if(applicationEntity.getValue(this.OP_SCENE) == null){
			return null;
		}
		return applicationEntity.getValue(this.OP_SCENE).toString();
	}



	public void setOp_scene(String op_scene) {
		applicationEntity.setValue(this.OP_SCENE, op_scene);
	}



	public String getOp_category() {
		if(applicationEntity.getValue(this.OP_CATEGORY) == null){
			return null;
		}
		return applicationEntity.getValue(this.OP_CATEGORY).toString();
	}



	public void setOp_category(String op_category) {
		applicationEntity.setValue(this.OP_CATEGORY, op_category);
	}



	public String getRes_type() {
		if(applicationEntity.getValue(this.RES_TYPE) == null){
			return null;
		}
		return applicationEntity.getValue(this.RES_TYPE).toString();
	}



	public void setRes_type(String res_type) {
		applicationEntity.setValue(this.RES_TYPE, res_type);
	}



	public long getRes_id() {
		if(applicationEntity.getValue(this.RES_ID) == null){
			return 0;
		}
		return applicationEntity.getValue(this.RES_ID);
	}



	public void setRes_id(long res_id) {
		applicationEntity.setValue(this.RES_ID, res_id);
	}



	public String getRes_keyinfo() {
		if(applicationEntity.getValue(this.RES_KEYINFO) == null){
			return null;
		}
		return applicationEntity.getValue(this.RES_KEYINFO).toString();
	}



	public void setRes_keyinfo(String res_keyinfo) {
		applicationEntity.setValue(this.RES_KEYINFO, res_keyinfo);
	}



	public String getContent() {
		if(applicationEntity.getValue(this.CONTENT) == null){
			return null;
		}
		return applicationEntity.getValue(this.CONTENT).toString();
	}



	public void setContent(String content) {
		applicationEntity.setValue(this.CONTENT, content);
	}



	public String getUser_name() {
		if(applicationEntity.getValue(this.USER_NAME) == null){
			return null;
		}
		return applicationEntity.getValue(this.USER_NAME).toString();
	}



	public void setUser_name(String user_name) {
		applicationEntity.setValue(this.USER_NAME, user_name);
	}



	public String getUser_account() {
		if(applicationEntity.getValue(this.USER_ACCOUNT) == null){
			return null;
		}
		return applicationEntity.getValue(this.USER_ACCOUNT).toString();
	}



	public void setUser_account(String user_account) {
		applicationEntity.setValue(this.USER_ACCOUNT, user_account);
	}



	public String getSrc_teminal() {
		if(applicationEntity.getValue(this.SRC_TEMINAL) == null){
			return null;
		}
		return applicationEntity.getValue(this.SRC_TEMINAL).toString();
	}



	public void setSrc_teminal(String src_teminal) {
		applicationEntity.setValue(this.SRC_TEMINAL, src_teminal);
	}



	public double getLongitude() {
		if(applicationEntity.getValue(this.LONGITUDE) == null){
			return 0f;
		}
		return Double.parseDouble(applicationEntity.getValue(this.LONGITUDE).toString());
	}



	public void setLongitude(double longitude) {
		applicationEntity.setValue(this.LONGITUDE, longitude);
	}



	public double getLatitude() {
		if(applicationEntity.getValue(this.LATITUDE) == null){
			return 0f;
		}
		return Double.parseDouble(applicationEntity.getValue(this.LATITUDE).toString());
	}



	public void setLatitude(double latitude) {
		applicationEntity.setValue(this.LATITUDE, latitude);
	}



	public int getRecord_type() {
		if(applicationEntity.getValue(this.RECORD_TYPE) == null){
			return 0;
		}
		return Integer.parseInt(applicationEntity.getValue(this.RECORD_TYPE).toString());
	}



	public void setRecord_type(int record_type) {
		applicationEntity.setValue(this.RECORD_TYPE, record_type);
	}



	public Date getOp_time() {
		if(applicationEntity.getValue(this.OP_TIME) == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = applicationEntity.getValue(this.OP_TIME).toString();
		Date parse = null;
		try {
			parse = sdf.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return parse;
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

	public void setOp_time(Date op_time) {
		applicationEntity.setValue(this.OP_TIME, op_time);
	}



	
}
