package com.iscreate.op.pojo.gisdispatch;

import java.io.Serializable;

public class GisDispatch_GraphElementType implements Serializable  {
	
	private int id;
	private String type;
	private String defaultIcon;
	private int position;
	private int gl_id;
	private String name;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefaultIcon() {
		return defaultIcon;
	}
	public void setDefaultIcon(String defaultIcon) {
		this.defaultIcon = defaultIcon;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getGl_id() {
		return gl_id;
	}
	public void setGl_id(int gl_id) {
		this.gl_id = gl_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
