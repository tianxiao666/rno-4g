package com.iscreate.plat.networkresource.common.tool;

import java.util.List;

public class Entity extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 962774305355489760L;

	/**
	 * 为通用对象设值的方法，通用设置一个attr对象来实现。<br/>
	 * 通用内部的属性对象引用与设置进来的属性对象attr不是同 一个引用，<br/>
	 * 但它们指向的value对象如果是自定义类型，则是同一引用。<br/>
	 * 
	 * @param attr
	 */
	public Entity setValue(Attribute attr) {
		super.set(attr);
		return this;
	}

	/**
	 * 为通用对象设值的方法
	 * 
	 * @param key
	 * @param value
	 */
	public Entity setValue(String key, Object value) {
		super.set(key, value);
		return this;
	}

	/**
	 * 为通用对象设值的方法，通用设置一个attr对象来实现。<br/>
	 * 通用内部的属性对象引用与设置进来的属性对象attr不是同 一个引用，<br/>
	 * 但它们指向的value对象如果是自定义类型，则是同一引用。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attr
	 * @param addTo
	 */
	public Entity setValue(Attribute attr, Boolean addTo) {
		super.set(attr, addTo);
		return this;
	}

	/**
	 * 为通用对象设值的方法，设置的是一组属性对象。<br/>
	 * 通用内部的属性对象引用与设置进来的属性对象attrs不是同 一个引用，<br/>
	 * 但它们指向的value对象如果是自定义类型，则是同一引用。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attrs
	 * @param addTo
	 */
	public Entity setValue(List<Attribute> attrs, Boolean addTo) {
		super.set(attrs, addTo);
		return this;
	}

	/**
	 * 为通用对象设值的方法。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param key
	 * @param value
	 * @param addTo
	 */
	public Entity setValue(String key, Object value, Boolean addTo) {
		super.set(key, value, addTo);
		return this;
	}

	/**
	 * 该方法会将attr对象深度克隆后加入到通用对象中。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attr
	 * @param addTo
	 */
	public Entity setValueClone(Attribute attr, Boolean addTo) {
		super.setClone(attr, addTo);
		return this;
	}

	/**
	 * 该方法会逐个将attrs里的对象深度克隆后加入到通用对象中。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attrs
	 * @param addTo
	 */
	public Entity setValueClone(List<Attribute> attrs, Boolean addTo) {
		super.setClone(attrs, addTo);
		return this;
	}

	/**
	 * 为通用对象设值方法。该方法会对进入对象的VALUE进行深度克隆。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param key
	 * @param value
	 * @param addTo
	 */
	public Entity setValueClone(String key, Object value, Boolean addTo) {
		super.setClone(key, value, addTo);
		return this;
	}

	/**
	 * 如果通用对象包含中有oldAttr属性对象的引用，将它替换成新的newAttr属性对象的引用。
	 * 
	 * @param oldAttr
	 * @param newAttr
	 */
	public Entity replace(Attribute oldAttr, Attribute newAttr) {
		super.rep(oldAttr, newAttr);
		return this;
	}

	/**
	 * 替换掉通用对象中指定KEY、VALUE对中的VALUE值。
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public Entity replace(String key, Object oldVal, Object newVal) {
		super.rep(key, oldVal, newVal);
		return this;
	}

	/**
	 * 从通用对象中删除包含KEY值的所有属性对象。
	 * 
	 * @param key
	 */
	public void remove(String key) {
		super.remove(key);
	}

	/**
	 * 在通用对象中删除指定的KEY、VALUE对。<br/>
	 * 删除的条件是KEY值相等，value值相等。<br/>
	 * 
	 * @param key
	 * @param value
	 */
	public void remove(String key, Object value) {
		super.remove(key, value);
	}

	/**
	 * 查找属性列表中KEY、VALUE与attr的KEY、VALUE值相同的属性对象引用
	 * 
	 * @param attr
	 */
	public void remove(Attribute attr) {
		super.remove(attr);
	}

	/**
	 * 对过查找属性列表中包含了key值的属性，将它们封装成一个列表返回。
	 * 
	 * @param key
	 * @return
	 */
	public List<Attribute> get(String key) {
		return super.get(key);
	}

	/**
	 * 通过KEY值，在通用对象中查找第一个包含该KEY的属性对象。
	 * 
	 * @param key
	 * @return
	 */
	public Attribute getOne(String key) {
		return super.getOne(key);
	}

	/**
	 * 将对象的值清空。
	 */
	public void clear() {
		super.clear();
	}

	/**
	 * 将Entity通用对象转换成其他类型的通用对象。
	 * 
	 * @param <E>
	 * @param clazz
	 * @return
	 * @throws ClassChangeException
	 */
	public <E> E changeTo(Class<E> clazz) throws ClassChangeException {
		return super.changeFromEntity(clazz, this);
	}
}
