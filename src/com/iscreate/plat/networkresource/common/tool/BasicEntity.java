package com.iscreate.plat.networkresource.common.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BasicEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6405320215699833665L;

	protected List<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * 为通用对象设值的方法，通用设置一个attr对象来实现。<br/>
	 * 通用内部的属性对象引用与设置进来的属性对象attr不是同 一个引用，<br/>
	 * 但它们指向的value对象如果是自定义类型，则是同一引用。<br/>
	 * 
	 * @param attr
	 */
	protected void set(Attribute attr) {
		if (attr == null || !attr.availabled())
			return;
		this.remove(attr.key);
		Attribute a = new Attribute(attr.key, attr.value);
		this.attributes.add(a);
	}

	/**
	 * 为通用对象设值的方法
	 * 
	 * @param key
	 * @param value
	 */
	protected void set(String key, Object value) {
		if (key == null || key.isEmpty()) {
			return;
		}
		Attribute attr = new Attribute(key, value);
		this.remove(key);
		this.attributes.add(attr);
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
	protected void set(Attribute attr, Boolean addTo) {
		if (addTo) {
			if (attr == null || !attr.availabled())
				return;
			Attribute a = new Attribute(attr.key, attr.value);
			this.attributes.add(a);
		} else {
			this.set(attr);
		}
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
	protected void set(List<Attribute> attrs, Boolean addTo) {
		if (attrs == null || attrs.size() == 0)
			return;
		if (addTo) {
			for (Attribute attr : attrs) {
				if (attr == null)
					continue;
				if (attr.availabled()) {
					Attribute a = new Attribute(attr.key, attr.value);
					this.attributes.add(a);
				}
			}
		} else {
			for (Attribute attr : attrs) {
				this.set(attr);
			}
		}
	}

	/**
	 * 为通用对象设值的方法。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param key
	 * @param value
	 * @param addTo
	 */
	protected void set(String key, Object value, Boolean addTo) {
		if (addTo) {
			if (key == null || key.isEmpty()) {
				return;
			}
			Attribute attr = new Attribute(key, value);
			this.attributes.add(attr);
		} else {
			this.set(key, value);
		}
	}

	/**
	 * 该方法会将attr对象深度克隆后加入到通用对象中。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attr
	 * @param addTo
	 */
	protected void setClone(Attribute attr, Boolean addTo) {
		if (attr == null || !attr.availabled())
			return;
		Object a = this.deepClone(attr);
		if (a == null)
			return;
		Attribute newAttr = (Attribute) a;
		if (!addTo)
			this.remove(attr.key);
		this.attributes.add(newAttr);
	}

	/**
	 * 该方法会逐个将attrs里的对象深度克隆后加入到通用对象中。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param attrs
	 * @param addTo
	 */
	protected void setClone(List<Attribute> attrs, Boolean addTo) {
		if (attrs == null || attrs.size() == 0)
			return;
		if (addTo) {
			for (Attribute attr : attrs) {
				if (attr == null)
					continue;
				if (attr.availabled()) {
					Attribute a = (Attribute) this.deepClone(attr);
					this.attributes.add(a);
				}
			}
		} else {
			for (Attribute attr : attrs) {
				this.setClone(attr, addTo);
			}
		}
	}

	/**
	 * 为通用对象设值方法。该方法会对进入对象的VALUE进行深度克隆。<br/>
	 * addTo参数：TRUE新值追加到属性列表之后，FALSE新值覆盖原KEY指定的值。<br/>
	 * 
	 * @param key
	 * @param value
	 * @param addTo
	 */
	protected void setClone(String key, Object value, Boolean addTo) {
		if (key == null || key.isEmpty())
			return;
		Object o = null;
		if (value != null) {
			o = this.deepClone(value);
		}
		this.set(key, o, addTo);
	}

	/**
	 * 如果通用对象包含中有oldAttr属性对象的引用，将它替换成新的newAttr属性对象的引用。
	 * 
	 * @param oldAttr
	 * @param newAttr
	 */
	protected void rep(Attribute oldAttr, Attribute newAttr) {
		if (oldAttr == null || !oldAttr.availabled())
			return;
		if (newAttr == null || !newAttr.availabled())
			return;
		for (Attribute attr : this.attributes) {
			if (attr.equals(oldAttr))
				attr = newAttr;
		}
	}

	/**
	 * 替换掉通用对象中指定KEY、VALUE对中的VALUE值。
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	protected void rep(String key, Object oldVal, Object newVal) {
		if (key == null || key.isEmpty())
			return;
		for (Attribute attr : this.attributes) {
			if (attr.value.equals(oldVal))
				attr.setValue(newVal);
		}
	}

	/**
	 * 从通用对象中删除包含KEY值的所有属性对象。
	 * 
	 * @param key
	 */
	protected void remove(String key) {
		if (key == null || key.isEmpty())
			return;
		List<Attribute> tmp = new ArrayList<Attribute>();
		for (Attribute attr : this.attributes) {
			if (attr.key.equals(key)) {
				tmp.add(attr);
			}
		}
		this.attributes.removeAll(tmp);
	}

	/**
	 * 在通用对象中删除指定的KEY、VALUE对。<br/>
	 * 删除的条件是KEY值相等，value值相等。<br/>
	 * 
	 * @param key
	 * @param value
	 */
	protected void remove(String key, Object value) {
		if (key == null || key.isEmpty())
			return;
		Attribute attr = new Attribute(key, value);
		this.remove(attr);
	}

	/**
	 * 查找属性列表中KEY、VALUE与attr的KEY、VALUE值相同的属性对象引用
	 * 
	 * @param attr
	 */
	protected void remove(Attribute attr) {
		if (attr == null || attr.availabled())
			return;
		List<Attribute> attrs = new ArrayList<Attribute>();
		for (Attribute a : attributes) {
			if (a.equals(attr))
				attrs.add(a);
		}
		this.attributes.removeAll(attrs);
	}

	/**
	 * 对过查找属性列表中包含了key值的属性，将它们封装成一个列表返回。
	 * 
	 * @param key
	 * @return
	 */
	protected List<Attribute> get(String key) {
		List<Attribute> attrs = new ArrayList<Attribute>();
		if (key == null)
			return attrs;
		for (Attribute attr : this.attributes) {
			if (key.equals(attr.key)) {
				attrs.add(attr);
			}
		}
		return attrs;
	}

	/**
	 * 通过KEY值，在通用对象中查找第一个包含该KEY的属性对象。
	 * 
	 * @param key
	 * @return
	 */
	protected Attribute getOne(String key) {
		if (key == null)
			return null;
		for (Attribute a : this.attributes) {
			if (key.equals(a.key)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * 通过KEY值，在通用对象中查找该KEY所对应的VALUE值。
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getValue(String key) {
		if (key == null)
			return null;
		List result = new ArrayList();
		for (Attribute attr : this.attributes) {
			if (attr.key.equals(key))
				result.add(attr.value);
		}
		if (result.size() == 1) {
			return (T) result.get(0);
		} else if (result.size() > 1) {
			return (T) result;
		} else {
			return null;
		}
	}

	// /**
	// * 获取通用对象的所有键集。该方法用于存储，继承该通用对象的其他实例，<br/>
	// * 可以通用重构该方法，将所有希望系统存储的键返回。<br/>
	// * 如果该通用对象中多个键是相同的，那么在该集合中仅算一个。<br/>
	// *
	// * @return
	// */
	// public Set<String> allKeys() {
	// Set<String> s = new HashSet<String>();
	// for (Attribute attr : this.attributes) {
	// s.add(attr.key);
	// }
	// return s;
	// }

	/**
	 * 获取通用对象的键集。<br/>
	 * 如果该通用对象中多个键是相同的，那么在该集合中仅算一个。<br/>
	 * 
	 * @return
	 */
	public Set<String> keyset() {
		Set<String> s = new HashSet<String>();
		for (Attribute attr : this.attributes) {
			s.add(attr.key);
		}
		return s;
	}

	/**
	 * 获取该通用对象的KEY的个数。
	 * 
	 * @return
	 */
	public int keySize() {
		return this.keyset().size();
	}

	/**
	 * 将通用对象中所有的值转换成一个LIST对象。
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List values() {
		List list = new ArrayList();
		for (Attribute attr : this.attributes) {
			list.add(attr.value);
		}
		return list;
	}

	/**
	 * 计算通用对象中存储的值的个数。<br/>
	 * 如果对象中同一个KEY有多个值，那么每个值都会在计数内。<br/>
	 * 
	 * @return
	 */
	public int valueSize() {
		return this.values().size();
	}

	/**
	 * 判断通用对象中是否已经包含了指定的KEY值
	 * 
	 * @param key
	 * @return
	 */
	public boolean containKey(String key) {
		if (key == null || key.isEmpty())
			return false;
		for (Attribute attr : this.attributes) {
			if (key.equals(attr.key))
				return true;
		}
		return false;
	}

	/**
	 * 判断通用对象中是否已经包含了指定的KEY、VALUE对。<br/>
	 * 判断方法：遍历所有属性对象，如果该对象中的KEY值相等与VALUE值相等，返回TRUE。<br/>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean containKV(String key, Object value) {
		if (key == null || key.isEmpty())
			return false;
		if (!this.containKey(key)) {
			return false;
		}
		Attribute attr = new Attribute(key, value);
		for (Attribute a : this.attributes) {
			if (a.equals(attr)) {
				return true;
			}
		}
		Object v2 = this.getValue(key);
		if (v2 == null && value == null) {
			return true;
		} else if (v2 != null && value != null) {
			if (v2.equals(value))
				return true;
			// 如果要判断的值 value是一个列表，从通用对象中拿出来的值v2也是一个列表，
			// 那么需要value列表中的所有元素都与v2列表中某个元素相等，则该实体包含
			// 这个键值对。
			if ((v2 instanceof Collection) && (value instanceof Collection)) {
				boolean contain = true;
				Collection<Object> c1 = (Collection<Object>) value;
				Collection<Object> c2 = (Collection<Object>) v2;
				for (Object o1 : c1) {
					contain = false;
					for (Object o2 : c2) {
						if (o1.equals(o2)) {
							contain = true;
							break;
						}
					}
					if (!contain)
						return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 将对象的值清空。
	 */
	protected void clear() {
		this.attributes.clear();
	}

	/**
	 * 通用对象的克隆方法，该方法是深度克隆。<br/>
	 * 克隆方法为先序列化成字节流，再反序列化为对象。<br/>
	 */
	public Object clone() {
		Object o = this.deepClone(this);
		if (o == null) {
			return null;
		}
		return o;
	}

	/**
	 * 判断两个对象是否相等。<br/>
	 * 获取两个对象的属性列表，判断它们的键值对是否都相同。<br/>
	 * 
	 * @param entity
	 * @return
	 */
	public boolean equals(BasicEntity entity) {
		if (entity == null)
			return false;
		boolean equal = true;
		for (Attribute a1 : this.attributes) {
			equal = false;
			for (Attribute a2 : entity.attributes) {
				if (a1.equals(a2)) {
					equal = true;
					break;
				}
			}
			if (!equal)
				return false;
		}
		return true;
	}

	/**
	 * 将自身转换成一个MAP对象
	 * 
	 * @return
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : this.keyset()) {
			map.put(key, this.getValue(key));
		}
		return map;
	}

	/**
	 * 从一个MAP对象转换成一个通用对象
	 * 
	 * @param map
	 * @return
	 */
	public static BasicEntity fromMap(Map<String, ?> map) {
		BasicEntity e = new BasicEntity();
		if (map == null)
			return e;
		for (String key : map.keySet()) {
			e.set(key, map.get(key));
		}
		return e;
	}

	/**
	 * 通过序列化与反序列化深度克隆一个对象
	 * 
	 * @param o
	 * @return
	 */
	private Object deepClone(Object o) {
		Object newo = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			byte[] bs = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			ObjectInputStream ois = new ObjectInputStream(bais);
			newo = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return newo;
	}

	public void dump() {
		Log log = LogFactory.getLog(getClass());
		log.debug("当前应用数据对象的信息如下：");
		log.debug("-----------------------------------------");
		for (String key : keyset()) {
			log.debug(key + ":" + getValue(key));
		}
		log.debug("-----------------------------------------");
	}

	@SuppressWarnings("unchecked")
	protected static <E> E changeFromEntity(Class<E> clazz, Entity entity)
			throws ClassChangeException {
		if (clazz == null)
			return null;
		if (entity == null)
			return null;
		if (BasicEntity.class.isAssignableFrom(clazz)) {
			try {
				BasicEntity be = (BasicEntity) clazz.newInstance();
				be.attributes = entity.attributes;
				return (E) be;
			} catch (InstantiationException e) {
				String message = "类：\"" + clazz.toString() + "\"必须包含一个无参构造函数。";
				ClassChangeException cce = new ClassChangeException(message);
				throw cce;
			} catch (IllegalAccessException e) {
				String message = "类：\"" + clazz.toString()
						+ "\"的无参构造函数无法访问，请确认其访问权限为public。";
				ClassChangeException cce = new ClassChangeException(message);
				throw cce;
			}
		} else {
			String message = "类：\"" + clazz.toString()
					+ "\"不是通用对象:\"BasicEntity\"的一个子类!该转换方法实现的是从通"
					+ "用对象\"Entity\"转换成其他通用对象的过程。";
			ClassChangeException cce = new ClassChangeException(message);
			throw cce;
		}
	}
}
