package com.iscreate.op.pojo.project;

import java.util.Date;
/**
 * 
 * @filename: ProMonthReimDetail.java
 * @classpath: com.iscreate.op.pojo.project
 * @description: 项目月度报销费用明细表
 * @author：yuan.yw
 * @date：Dec 16, 2013 1:36:28 PM
 * @version：
 */
public class ProMonthReimDetail {
	private Long month_reim_detail_id;//标识
	private Long month_reim_fee_id;//费用表id  
	private String status;   //状态
	private Long org_user_id;   //经办人
	private Long create_user_id; //创建人
	private Date fee_date;   //费用发生日期
	private String fee_reim_money;   //金额
	private String note;  //备注
	private Date  create_time; //创建时间
	private Date  mod_time;//修改时间
	private String feeIdString;//   其他费用 子项需要先插入数据字典 需要特殊处理（other1）
	private String typePath;
	
	private String isDelete;// 标识是否要删除记录字典 true false
	
	private String org_user_name;//经手人名称
	private Long parent_id;//上级关联id
	private String data_type;//记录类型
	//最低级类型
	private String type;
	//第一级类型
	private String firstType;
	
	public ProMonthReimDetail() {
		
	}
	//get and set
	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public Long getMonth_reim_detail_id() {
		return month_reim_detail_id;
	}
	public void setMonth_reim_detail_id(Long month_reim_detail_id) {
		this.month_reim_detail_id = month_reim_detail_id;
	}
	public Long getMonth_reim_fee_id() {
		return month_reim_fee_id;
	}
	public void setMonth_reim_fee_id(Long month_reim_fee_id) {
		this.month_reim_fee_id = month_reim_fee_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getOrg_user_id() {
		return org_user_id;
	}
	public void setOrg_user_id(Long org_user_id) {
		this.org_user_id = org_user_id;
	}
	public Date getFee_date() {
		return fee_date;
	}
	public void setFee_date(Date fee_date) {
		this.fee_date = fee_date;
	}
	public String getFee_reim_money() {
		return fee_reim_money;
	}
	public void setFee_reim_money(String fee_reim_money) {
		this.fee_reim_money = fee_reim_money;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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

	public String getFeeIdString() {
		return feeIdString;
	}

	public void setFeeIdString(String feeIdString) {
		this.feeIdString = feeIdString;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public Long getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(Long create_user_id) {
		this.create_user_id = create_user_id;
	}
	public String getTypePath() {
		return typePath;
	}
	public void setTypePath(String typePath) {
		this.typePath = typePath;
	}
	public String getOrg_user_name() {
		return org_user_name;
	}
	public void setOrg_user_name(String org_user_name) {
		this.org_user_name = org_user_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFirstType() {
		return firstType;
	}
	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}
	
	

	
	
	
	
}
