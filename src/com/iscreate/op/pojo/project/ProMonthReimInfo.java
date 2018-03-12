package com.iscreate.op.pojo.project;

import java.util.Date;

/**
 * 
 * @filename: ProMonthReimInfo.java
 * @classpath: com.iscreate.op.pojo.project
 * @description: 项目月度报销基本信息
 * @author：yuan.yw
 * @date：2013-12-15 下午3:21:20
 * @version：
 */

public class ProMonthReimInfo {
	private Long month_reim_id;//月度报销id
	private Long pro_id;  //项目id
	private Date month;   //月份
	private String status;   //状态 
	private String xianjin_status;//现金状态
	private String noxianjin_status;  //非现金状态
	private String plan_whole_status;  //统筹费用（不包括油费）状态
	private String fuel_status;//油费状态
	private String xianjin_link;//现金运作费当前环节
	private String noxianjin_link;  //非现金运作费当前环节
	private String plan_whole_link;  //统筹（除油费）当前环节
	private String fuel_link;//油费当前环节
	private Long apply_user_id;   //申请人
	private Date create_time;   //创建时间
	private Date mod_time;//修改时间
	private Date apply_time;//申请时间
	
	//get and set
	public Long getMonth_reim_id() {
		return month_reim_id;
	}
	public void setMonth_reim_id(Long month_reim_id) {
		this.month_reim_id = month_reim_id;
	}
	public Long getPro_id() {
		return pro_id;
	}
	public void setPro_id(Long pro_id) {
		this.pro_id = pro_id;
	}
	public Date getMonth() {
		return month;
	}
	public void setMonth(Date month) {
		this.month = month;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getXianjin_status() {
		return xianjin_status;
	}
	public void setXianjin_status(String xianjin_status) {
		this.xianjin_status = xianjin_status;
	}
	public String getNoxianjin_status() {
		return noxianjin_status;
	}
	public void setNoxianjin_status(String noxianjin_status) {
		this.noxianjin_status = noxianjin_status;
	}
	public String getPlan_whole_status() {
		return plan_whole_status;
	}
	public void setPlan_whole_status(String plan_whole_status) {
		this.plan_whole_status = plan_whole_status;
	}
	public String getFuel_status() {
		return fuel_status;
	}
	public void setFuel_status(String fuel_status) {
		this.fuel_status = fuel_status;
	}
	public String getXianjin_link() {
		return xianjin_link;
	}
	public void setXianjin_link(String xianjin_link) {
		this.xianjin_link = xianjin_link;
	}
	public String getNoxianjin_link() {
		return noxianjin_link;
	}
	public void setNoxianjin_link(String noxianjin_link) {
		this.noxianjin_link = noxianjin_link;
	}
	public String getPlan_whole_link() {
		return plan_whole_link;
	}
	public void setPlan_whole_link(String plan_whole_link) {
		this.plan_whole_link = plan_whole_link;
	}
	public String getFuel_link() {
		return fuel_link;
	}
	public void setFuel_link(String fuel_link) {
		this.fuel_link = fuel_link;
	}
	public Long getApply_user_id() {
		return apply_user_id;
	}
	public void setApply_user_id(Long apply_user_id) {
		this.apply_user_id = apply_user_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getMod_time() {
		return mod_time;
	}
	public void setMod_time(Date mod_time) {
		this.mod_time = mod_time;
	}
	public Date getApply_time() {
		return apply_time;
	}
	public void setApply_time(Date apply_time) {
		this.apply_time = apply_time;
	}


	
}
