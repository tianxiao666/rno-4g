package com.iscreate.plat.networkresource.application.tool;


import java.util.Set;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public class AttributeModule extends BasicEntity {

	private static final long serialVersionUID = -7895128770010509199L;

	private final String attributeKey = "_attribute";

	private AttributeModule() {

	}

	public AttributeModule(String attributeName) {
		set(attributeKey, attributeName);
	}

	/**
	 * 为属性对象设值
	 * 
	 * @param <T>
	 * @param key
	 * @param value
	 */
	<T> void setAttributeInfo(String key, T value) {
		set(key, value);
	}


	/**
	 * 获取属性模板的名称
	 * 
	 * @return
	 */
	public String getAttributeName() {
		String name = this.getValue(attributeKey);
		return name;
	}

	/**
	 * 属性模板中所有约束的键集。
	 */
	public Set<String> moduleKeyset() {
		Set<String> set = super.keyset();
		set.remove(attributeKey);
		return set;
	}

	public static AttributeModule changeFromEntity(BasicEntity basic) {
		AttributeModule am = new AttributeModule();
		for (String key : basic.keyset()) {
			am.set(key, basic.getValue(key));
		}
		return am;
	}
}
