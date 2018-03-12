package com.iscreate.plat.networkresource.application.tool;



import java.util.Set;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;


/**
 * 应用数据模板类
 * 
 * @author joe
 * 
 */
public class ApplicationModule extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708603457646992554L;

	private final String moduleKey = "_moduleName";

	/**
	 * 带参数的构造函数，传入的是数据模板的名称
	 * 
	 * @param dictionaryName
	 */
	public ApplicationModule(String dictionaryName) {
		set(moduleKey, dictionaryName);
	}

	/**
	 * 为数据模板设置属性模板对象
	 * 
	 * @param attr
	 */
	void setAttribute(AttributeModule attr) {
		set(attr.getAttributeName(), attr);
	}

	void removeAttribute(String key) {
		super.remove(key);
	}

	/**
	 * 通过属性名称获取属性模板，如果属性名称不存在，返回空值。
	 * 
	 * @param attrName
	 * @return
	 */
	public AttributeModule getAttribute(String attrName) {
		AttributeModule attr = this.getValue(attrName);
		return attr;
	}

	/**
	 * 获取对象模板的名称
	 * 
	 * @return
	 */
	public String getModuleName() {
		String name = this.getValue(moduleKey);
		return name;
	}

	/**
	 * 获取模板中属性的名称集合
	 */
	public Set<String> keyset() {
		Set<String> set = super.keyset();
		set.remove(moduleKey);
		return set;
	}

	/**
	 * 通过模板创建一个应用数据对象
	 * 
	 * @return
	 */
	public ApplicationEntity createApplicationEntity() {
		return new ApplicationEntity(this);
	}

	public void clear(){
		super.clear();
	}
}
