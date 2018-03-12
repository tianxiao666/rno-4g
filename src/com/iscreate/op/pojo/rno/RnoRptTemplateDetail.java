package com.iscreate.op.pojo.rno;

import java.io.Serializable;

public class RnoRptTemplateDetail {

	private Long id;
	private Long templateId;
	private String tableFields;
	private String displayName;
	private Integer displayOrder;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getTableFields() {
		return tableFields;
	}
	public void setTableFields(String tableFields) {
		this.tableFields = tableFields;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public RnoRptTemplateDetail(Long id, Long templateId, String tableFields,
			String displayName, Integer displayOrder) {
		super();
		this.id = id;
		this.templateId = templateId;
		this.tableFields = tableFields;
		this.displayName = displayName;
		this.displayOrder = displayOrder;
	}
	public RnoRptTemplateDetail() {
		super();
	}
	@Override
	public String toString() {
		return "RnoRptTemplateDetail [id=" + id + ", templateId=" + templateId
				+ ", tableFields=" + tableFields + ", displayName="
				+ displayName + ", displayOrder=" + displayOrder + "]";
	}
	
	
	

}
