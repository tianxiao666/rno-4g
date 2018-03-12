package com.iscreate.op.pojo.project;

public class ProBudget {
	private long prod_budget_fee_id;
	private long pro_sub_id;
	private String status;
	private long fee_first_type_id;
	private long fee_second_type_id;
	private String fee_second_name;
	private long fee_third_type_id;
	private String fee_money;
	private String price_money;
	private String cycle_month;
	private String note;
	
	public long getProd_budget_fee_id() {
		return prod_budget_fee_id;
	}

	public void setProd_budget_fee_id(long prod_budget_fee_id) {
		this.prod_budget_fee_id = prod_budget_fee_id;
	}



	public long getPro_sub_id() {
		return pro_sub_id;
	}

	public void setPro_sub_id(long pro_sub_id) {
		this.pro_sub_id = pro_sub_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getFee_first_type_id() {
		return fee_first_type_id;
	}

	public void setFee_first_type_id(long fee_first_type_id) {
		this.fee_first_type_id = fee_first_type_id;
	}

	public long getFee_second_type_id() {
		return fee_second_type_id;
	}

	public void setFee_second_type_id(long fee_second_type_id) {
		this.fee_second_type_id = fee_second_type_id;
	}

	public long getFee_third_type_id() {
		return fee_third_type_id;
	}

	public void setFee_third_type_id(long fee_third_type_id) {
		this.fee_third_type_id = fee_third_type_id;
	}

	public String getFee_money() {
		return fee_money;
	}

	public void setFee_money(String fee_money) {
		this.fee_money = fee_money;
	}

	public String getPrice_money() {
		return price_money;
	}

	public void setPrice_money(String price_money) {
		this.price_money = price_money;
	}

	public String getCycle_month() {
		return cycle_month;
	}

	public void setCycle_month(String cycle_month) {
		this.cycle_month = cycle_month;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ProBudget(){
		
	}

	public String getFee_second_name() {
		return fee_second_name;
	}

	public void setFee_second_name(String fee_second_name) {
		this.fee_second_name = fee_second_name;
	}

	
	
	
	
}
