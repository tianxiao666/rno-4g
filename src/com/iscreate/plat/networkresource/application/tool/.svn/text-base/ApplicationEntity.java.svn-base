package com.iscreate.plat.networkresource.application.tool;





import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;




public class ApplicationEntity extends BasicEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6709653189490317869L;

	private final String moduleKey = "_entityModule";

	private final String idKey = DefaultParam.idKey;
	private final String typeKey = DefaultParam.typeKey;
	
	

	public ApplicationEntity() {

	}

	public ApplicationEntity(ApplicationModule module) {
		if (module != null) {
			super.set(moduleKey, module);
			super.set(typeKey, module.getModuleName());
			if(module.getModuleName().equals("Tbl_Unique_Uniquetable")){
				super.set(idKey,UUID.randomUUID().toString().replace("-", ""));
			}else{
				super.set(idKey, ApplicationPrimary.getCurrentEntityId());
			}
		}
	}

	/**
	 * 应用数据对象的设值方法，先对设置的值进行校验，如果符合模板定义，则将值引用添加到对象中。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Consequence setValue(String key, Object value) {
		RestrainAttribute ar = RestraintFactory.getAttributeRestraint();
		ApplicationModule module = getModule();
		Consequence sc = ar.kvRestrain(key, value, module);
		if (sc.getCode().equals(ar.successCode))
			super.set(key, value);
		return sc;
	}

	/**
	 * 应用数据对象的设值方法，先对设置的值进行校验，如果符合模板定义，则将值添加到对象中。
	 * 
	 * @param key
	 * @param value
	 * @param addTo
	 * @return
	 */
	public Consequence setValueClone(String key, Object value, Boolean addTo) {
		RestrainAttribute ar = RestraintFactory.getAttributeRestraint();
		ApplicationModule module = getModule();
		Consequence sc = ar.kvRestrain(key, value, module);
		if (sc.getCode().equals(ar.successCode))
			super.setClone(key, value, addTo);
		return sc;
	}

	/**
	 * 将相应KEY的值引用从对象中删除
	 */
	public void remove(String key) {
		super.remove(key);
	}

	/**
	 * 清空对象的属性值
	 */
	public void clear() {
		for (String key : keyset()) {
			super.remove(key);
		}
	}

	/**
	 * 获取对象的ID值
	 * 
	 * @return
	 */
	public long getId() {
		if (containKey(idKey))
			return Long.parseLong(getValue(idKey)+"");
		else
			return 0;
	}

	/**
	 * 设置对象的ID值
	 * 
	 * @param id
	 */
	public void setId(long id) {
		if (id == 0)
			return;
		super.set(idKey,id);
	}

	/**
	 * 获取数据对象所引用的模板名称
	 * 
	 * @return
	 */
	public String getType() {
		ApplicationModule module = getModule();
		if (module == null)
			return "";
		return module.getModuleName();
	}

	/**
	 * 返回属性的定义信息，用一个MAP对象去返回
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, Object> getAttributeDefination(String key) {
		ApplicationModule module = getModule();
		if (module == null || !module.containKey(key)) {
			return new HashMap<String, Object>();
		}
		AttributeModule attr = module.getAttribute(key);
		return attr.toMap();
	}

	/**
	 * 校验数据对象是否与模板定义一致
	 * 
	 * @return
	 */
	public boolean verify() {
		RestrainAttribute restrain = RestraintFactory.getAttributeRestraint();
		ApplicationModule module = getModule();
		Consequence consequence = restrain.restrain(this, module);
		if (consequence.getCode().equals(restrain.successCode)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取属性的KEY值
	 * 
	 * @return
	 */
	public Set<String> keyset() {
		Set<String> set = super.keyset();
		set.remove(moduleKey);
		return set;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Set<String> attrkeys() {
		ApplicationModule module = getModule();
		if (module == null) {
			Set<String> set = super.keyset();
			set.remove(moduleKey);
			set.remove(idKey);
			set.remove(typeKey);
			return set;
		} else {
			return module.keyset();
		}
	}

	/**
	 * 在对象中查找模板对象。如果模板对象不存在，则通过模板名称去ModuleProvider中去申请。<br/>
	 * 由于持久化应用对象到库的时候不会保存模板对象，仅保存模板名称。<br/>
	 * 因此，在重新加载这个对象的时候，需要根据该名称去重新加载模板对象。<br/>
	 * 
	 * @return
	 */
	private ApplicationModule getModule() {
		if (this.containKey(moduleKey)) {
			ApplicationModule am = getValue(moduleKey);
			return am;
		}
		if (this.containKey(typeKey)) {
			String type = getValue(typeKey);
			ApplicationModule am = ModuleProvider.getModule(type);
			super.set(moduleKey, am);
			return am;
		} else {
			return null;
		}
	}

	/**
	 * 从应用数据对象中将所有关键的KEY值取出。
	 * 
	 * @return
	 */
	public Set<String> primaryKeys() {
		ApplicationModule tm = this.getModule();
		HashSet<String> keys = new HashSet<String>();
		for (String key : tm.keyset()) {
			boolean primary = tm.getAttribute(key).getValue(
					RestrainAttribute.primary);
			if (primary) {
				keys.add(key);
			}
		}
		return keys;
	}

	/**
	 * 从Entity对象转换成为ApplicationEntity对象
	 * 
	 * @param entity
	 * @return
	 */
	public static ApplicationEntity changeFromEntity(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		String moduletype = entity.getValue(DefaultParam.typeKey);
		ApplicationEntity e = null;
		if (moduletype == null || moduletype.isEmpty()) {
			e = new ApplicationEntity();
		} else {
			e = ModuleProvider.getModule(moduletype).createApplicationEntity();
		}
		Log log = LogFactory.getLog(ApplicationEntity.class);
		log.debug("开始将通用对象转换成ApplicationEntity.");
		for (String key : entity.keyset()) {
			if (key.equals(DefaultParam.typeKey)
					|| key.equals(DefaultParam.idKey)) {
				e.set(key, entity.getValue(key));
			} else {
				try {
					String type = e.getModule().getAttribute(key)
							.getValue("type");
					String value = entity.getValue(key) + "";
					addValueTo(e, type, key, value);
				} catch (NullPointerException ex) {
					log.debug("当前应用数据对象：'"
							+ entity.getValue(DefaultParam.typeKey) + "'没有属性'"
							+ key + "'。");
				}
			}
		}
		//临时判断
		if(e != null && e.getType().equals("Sys_Area")){
			e.set("id", entity.getValue("id") + "");
		}
		return e;
	}
	
	/**
	 * 从Entity对象转换成为ApplicationEntity对象(全部属性转换，ApplicationEntity对象无此属性默认设置String) yuan.yw
	 * 
	 * @param entity
	 * @return
	 */
	public static ApplicationEntity changeFromEntityForAll(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		String moduletype = entity.getValue(DefaultParam.typeKey);
		ApplicationEntity e = null;
		if (moduletype == null || moduletype.isEmpty()) {
			e = new ApplicationEntity();
		} else {
			e = ModuleProvider.getModule(moduletype).createApplicationEntity();
		}
		Log log = LogFactory.getLog(ApplicationEntity.class);
		log.debug("开始将通用对象转换成ApplicationEntity.");
		for (String key : entity.keyset()) {
			if (key.equals(DefaultParam.typeKey)
					|| key.equals(DefaultParam.idKey)) {
				e.set(key, entity.getValue(key));
			} else {
				try {
					String type = e.getModule().getAttribute(key)
							.getValue("type");
					String value = entity.getValue(key) + "";
					addValueTo(e, type, key, value);
				} catch (NullPointerException ex) {
					e.set(key, entity.getValue(key));
				}
			}
		}
		return e;
	}
	
	private static void addValueTo(ApplicationEntity entity, String type,
			String key, String value) {
		Log log = LogFactory.getLog(ApplicationEntity.class);
		if ("java.lang.Integer".equals(type)) {
			try {
				int v = Integer.parseInt(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Integer失败。");
			}
		} else if ("java.lang.Long".equals(type)) {
			try {
				long v = Long.parseLong(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Long失败。");
			}
		} else if ("java.lang.Float".equals(type)) {
			try {
				float v = Float.parseFloat(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Float失败。");
			}
		} else if ("java.lang.Double".equals(type)) {
			try {
				double v = Double.parseDouble(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Double失败。");
			}
		} else if ("java.lang.Boolean".equals(type)) {
			try {
				boolean v = Boolean.parseBoolean(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Double失败。");
			}
		} else if ("java.util.Date".equals(type)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:SS");
				Date v = sdf.parse(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (ParseException e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.util.Date失败。");
			}
		} else {

			log.debug("添加属性值'" + key + "'='" + value + "'结果:"
					+ entity.setValue(key, value).toString());
		}
	}
}
