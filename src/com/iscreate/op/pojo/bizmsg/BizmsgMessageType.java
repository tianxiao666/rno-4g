package com.iscreate.op.pojo.bizmsg;

public class BizmsgMessageType {

	private Long id;
	private String typeKey;
	private String typeName;
	private int _order;
	private int status;
	
	public BizmsgMessageType(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int get_order() {
		return _order;
	}

	public void set_order(int _order) {
		this._order = _order;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
