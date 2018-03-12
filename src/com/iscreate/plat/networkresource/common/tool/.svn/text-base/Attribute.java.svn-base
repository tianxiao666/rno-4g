package com.iscreate.plat.networkresource.common.tool;



import java.io.Serializable;

public class Attribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2489506054135184624L;
	String key;
	Object value;

	public Attribute(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 判断属性对象是否可用。如果属性对象中的key为空值，不用可。
	 * @return
	 */
	public boolean availabled() {
		if (this.key == null || this.key.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断两个属性对象的键值对是否相同(值相同，非引用)
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean equals(Attribute attribute) {
		if (attribute == null || !attribute.availabled())
			return false;
		if (!this.key.equals(attribute.key))
			return false;
		if (this.value == null && attribute.value == null)
			return true;
		else if (this.value != null) {
			if (this.value.equals(attribute.value))
				return true;
		}
		return false;
	}
}
