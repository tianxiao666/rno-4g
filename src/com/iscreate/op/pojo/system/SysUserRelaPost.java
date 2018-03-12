package com.iscreate.op.pojo.system;

import java.util.Date;

public class SysUserRelaPost {
	private long org_user_id;
	private String post_type;
	private String post_code;
	private long org_id;
	private String status;
	private Date start_time;
	private Date end_time;
	private Date create_time;
	private Date mod_time;
	
	public SysUserRelaPost(){
		
	}
	
	
	public long getOrg_user_id() {
		return org_user_id;
	}
	public void setOrg_user_id(long org_user_id) {
		this.org_user_id = org_user_id;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(long org_id) {
		this.org_id = org_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStart_time() {
		return start_time;
	}
	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
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
