package com.iscreate.op.pojo.project;

import java.util.Date;

public class ProYearPlanInfo {

	private long pro_year_plan_id;
	private long pro_id;
	private String status;
	private String month;
	private float budget_value_money;
	private float budget_cost_money;
	private long user_number;
	private long create_user_id;
	private Date create_time;
	private Date mod_time;
	
	
	
	public long getPro_id() {
		return pro_id;
	}
	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getPro_year_plan_id() {
		return pro_year_plan_id;
	}
	public void setPro_year_plan_id(long pro_year_plan_id) {
		this.pro_year_plan_id = pro_year_plan_id;
	}
	public float getBudget_value_money() {
		return budget_value_money;
	}
	public void setBudget_value_money(float budget_value_money) {
		this.budget_value_money = budget_value_money;
	}
	public float getBudget_cost_money() {
		return budget_cost_money;
	}
	public void setBudget_cost_money(float budget_cost_money) {
		this.budget_cost_money = budget_cost_money;
	}
	public long getUser_number() {
		return user_number;
	}
	public void setUser_number(long user_number) {
		this.user_number = user_number;
	}
	public long getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(long create_user_id) {
		this.create_user_id = create_user_id;
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
	
	
	
}
