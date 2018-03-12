package com.iscreate.op.pojo.rno;

import java.io.Serializable;

public class RnoTableDict implements Serializable {

	private Long id;
	private String tableDbName;
	private String tableDisplayName;
	private String fieldDbName;
	private String fieldDisplayName;
	private Integer displayOrder;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableDbName() {
		return tableDbName;
	}

	public void setTableDbName(String tableDbName) {
		this.tableDbName = tableDbName;
	}

	public String getTableDisplayName() {
		return tableDisplayName;
	}

	public void setTableDisplayName(String tableDisplayName) {
		this.tableDisplayName = tableDisplayName;
	}

	public String getFieldDbName() {
		return fieldDbName;
	}

	public void setFieldDbName(String fieldDbName) {
		this.fieldDbName = fieldDbName;
	}

	public String getFieldDisplayName() {
		return fieldDisplayName;
	}

	public void setFieldDisplayName(String fieldDisplayName) {
		this.fieldDisplayName = fieldDisplayName;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RnoTableDict(Long id, String tableDbName, String tableDisplayName,
			String fieldDbName, String fieldDisplayName, Integer displayOrder,
			String status) {
		super();
		this.id = id;
		this.tableDbName = tableDbName;
		this.tableDisplayName = tableDisplayName;
		this.fieldDbName = fieldDbName;
		this.fieldDisplayName = fieldDisplayName;
		this.displayOrder = displayOrder;
		this.status = status;
	}

	public RnoTableDict() {
		super();
	}

	@Override
	public String toString() {
		return "RnoTableDict [id=" + id + ", tableDbName=" + tableDbName
				+ ", tableDisplayName=" + tableDisplayName + ", fieldDbName="
				+ fieldDbName + ", fieldDisplayName=" + fieldDisplayName
				+ ", displayOrder=" + displayOrder + ", status=" + status + "]";
	}

}
