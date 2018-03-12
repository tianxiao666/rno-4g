package com.iscreate.op.pojo.project;

import java.util.Date;


/**
 * 
 * @filename: ProMonthReimSubject.java
 * @classpath: com.iscreate.op.pojo.project
 * @description: 项目月度报销单科目 pojo
 * @author：yuan.yw
 * @date：2013-12-15 下午3:12:41
 * @version：
 */
public class ProMonthReimSubject {
	private Long month_reim_subject_id;//月度报销科目标识
	private Long month_reim_id;//月度报销标识
	private Long pro_id;  //项目id
	private Date month;   //月份
	private String status;//状态
	private String real_money;//实际产值   
	private Integer user_number;  //人数
	private String value_note;  //产值说明
	private String fee_money;  //金额
	private String serv_type;   //业务类型
	private Long parent_id;//上级id
	private String data_type;//记录类型   申请     审核
	private String note;//备注  总体说明
	private Date create_time;// 创建时间
	

	
	//get and set
	
	
	public Long getMonth_reim_subject_id() {
		return month_reim_subject_id;
	}
	public void setMonth_reim_subject_id(Long month_reim_subject_id) {
		this.month_reim_subject_id = month_reim_subject_id;
	}
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
	public String getReal_money() {
		return real_money;
	}
	public void setReal_money(String real_money) {
		this.real_money = real_money;
	}
	public Integer getUser_number() {
		return user_number;
	}
	public void setUser_number(Integer user_number) {
		this.user_number = user_number;
	}
	public String getValue_note() {
		return value_note;
	}
	public void setValue_note(String value_note) {
		this.value_note = value_note;
	}
	
	public String getFee_money() {
		return fee_money;
	}
	public void setFee_money(String fee_money) {
		this.fee_money = fee_money;
	}
	public String getServ_type() {
		return serv_type;
	}
	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
	}
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

	
	
	
}
