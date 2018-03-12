package com.iscreate.plat.networkresource.common.action;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.AttributeModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.Entity;



public class ActionHelper {

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据：<br/>
	 * <s:property value="#result.abc" /><br/>
	 * 'result'是固定字段, 'abc'是相应的KEY
	 * 
	 * @param entity
	 */
	public static void addBasicEntity(BasicEntity entity) {
		if (entity == null) {
			return;
		}
		String type = "result";
		ServletActionContext.getContext().put(type, entity.toMap());
	}

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据,tag是页面使用的标记字段：<br/>
	 * <s:property value="#tag.abc" /><br/>
	 * 'result'是固定字段, 'abc'是相应的KEY
	 * 
	 * @param entity
	 */
	public static void addBasicEntity(String tag, BasicEntity entity) {
		if (tag == null || tag.isEmpty()) {
			addBasicEntity(entity);
		} else {
			if (entity == null) {
				return;
			}
			String type = tag;
			ServletActionContext.getContext().put(type, entity.toMap());
		}
	}

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据：<br/>
	 * <s:iterator value="#list" id="result"> <br/>
	 * ${result.abc} <br />
	 * ${result.bbc} <br />
	 * </s:iterator>
	 * 
	 * 'list'是固定字段,'result'是取值时使用的ID,相当于后台的pojo<br/>
	 * '${result.abc}'相当于'pojo.key'取出对象相应的值。
	 * 
	 * @param entity
	 */
	public static void addBasicEntity(List<BasicEntity> entity) {
		if (entity == null) {
			return;
		}
		String type = "list";
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (BasicEntity e : entity) {
			result.add(e.toMap());
		}
		ServletActionContext.getContext().put(type, result);
	}

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据：<br/>
	 * <s:iterator value="#list" id="result"> <br/>
	 * ${result.abc} <br />
	 * ${result.bbc} <br />
	 * </s:iterator>
	 * 
	 * 'list'是固定字段,'result'是取值时使用的ID,相当于后台的pojo<br/>
	 * '${result.abc}'相当于'pojo.key'取出对象相应的值。
	 * 
	 * @param entity
	 */
	public static void addApplicationEntity(List<ApplicationEntity> entity) {
		if (entity == null) {
			return;
		}
		String type = "list";
		ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (BasicEntity e : entity) {
			result.add(e.toMap());
		}
		ServletActionContext.getContext().put(type, result);
	}

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据，其中tag字段为页面的ID标记字段：<br/>
	 * <s:iterator value="#tag" id="result"> <br/>
	 * ${result.abc} <br />
	 * ${result.bbc} <br />
	 * </s:iterator>
	 * 
	 * 'result'是取值时使用的ID,相当于后台的pojo<br/>
	 * '${result.abc}'相当于'pojo.key'取出对象相应的值。
	 * 
	 * @param entity
	 */
	public static void addBasicEntity(String tag, List<BasicEntity> entity) {
		if (tag == null || tag.isEmpty()) {
			addBasicEntity(entity);
		} else {
			if (entity == null)
				return;
			ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (BasicEntity e : entity) {
				result.add(e.toMap());
			}
			ServletActionContext.getContext().put(tag, result);
		}
	}

	/**
	 * 使用该方法，JSP页面使用下面的STRUTS标签绑定数据，其中tag字段为页面的ID标记字段：<br/>
	 * <s:iterator value="#tag" id="result"> <br/>
	 * ${result.abc} <br />
	 * ${result.bbc} <br />
	 * </s:iterator>
	 * 
	 * 'result'是取值时使用的ID,相当于后台的pojo<br/>
	 * '${result.abc}'相当于'pojo.key'取出对象相应的值。
	 * 
	 * @param entity
	 */
	public static void addApplicationEntity(String tag,
			List<ApplicationEntity> entity) {
		if (tag == null || tag.isEmpty()) {
			addApplicationEntity(entity);
		} else {
			if (entity == null)
				return;
			ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (BasicEntity e : entity) {
				result.add(e.toMap());
			}
			ServletActionContext.getContext().put(tag, result);
		}
	}

	public static void addParameterValues(String tag, Object values) {
		ServletActionContext.getContext().put(tag, values);
	}

	/**
	 * 前台的表单中只需要绑定KEY，后台得到的entity便以该KEY为键。<br/>
	 * <input name="name" value="abc" /><br/>
	 * 则后台代码如下：<br/>
	 * BasicEntity e = ActionHelper.getBasicEntity();<br/>
	 * String value = e.getValue("name");<br/>
	 * 
	 * @return
	 */
	public static BasicEntity getBasicEntity() {
		Map<String, Object> map = ServletActionContext.getContext()
				.getParameters();
		Entity e = new Entity();
		for (String key : map.keySet()) {
			e.setValue(key, ((String[]) map.get(key))[0]);
		}
		return e;
	}

	public static BasicEntity getBasicEntity(String moduleName) {
		Log log = LogFactory.getLog(ActionHelper.class);
		if (moduleName == null || moduleName.isEmpty()) {
			log.debug("模板：'" + moduleName + "'名称为空。");
			return getBasicEntity();
		}
		ApplicationModule am = ModuleProvider.getModule(moduleName);
		if (am == null) {
			log.debug("没有找到模板：'" + moduleName + "'。");
			return getBasicEntity();
		}
		ApplicationEntity entity = am.createApplicationEntity();
		log.debug("开始为模板'" + moduleName + "'生成实例。");
		Map<String, Object> map = ServletActionContext.getContext()
				.getParameters();
		for (String key : map.keySet()) {
			log.debug("开始获取属性'" + key + "'的值。");
			String value = ((String[]) map.get(key))[0];
			AttributeModule attr = am.getAttribute(key);
			if (attr == null) {
				log.debug("属性'" + key + "'不存在模板'" + moduleName + "'中。");
				continue;
			}
			String type = attr.getValue("type");
			addValueTo(entity, type, key, value);
		}
		return entity;
	}

	/**
	 * 前台的表单中只需要绑定KEY,后台得到的entity便以该KEY为键,返回应用数据对象<br/>
	 * <input name="Station#name" value="abc" /><br/>
	 * 则后台代码如下：<br/>
	 * ApplicationEntity e = ActionHelper.getApplicationEntity("Station");<br/>
	 * String value = e.getValue("name");<br/>
	 * 
	 * @return
	 */
	public static ApplicationEntity getApplicationEntity(String moduleName) {
		Log log = LogFactory.getLog(ActionHelper.class);
		if (moduleName == null || moduleName.isEmpty()) {
			log.debug("模板：'" + moduleName + "'名称为空。");
			return null;
		}
		ApplicationModule am = ModuleProvider.getModule(moduleName);
		if (am == null) {
			log.debug("没有找到模板：'" + moduleName + "'。");
			return null;
		}
		ApplicationEntity entity = am.createApplicationEntity();
		log.debug("开始为模板'" + moduleName + "'生成实例。");
		Map<String, Object> map = ServletActionContext.getContext()
				.getParameters();
		for (String k : map.keySet()) {
			if (k.indexOf("#") <= 0) {
				continue;
			}
			String m = k.substring(0, k.indexOf("#"));
			String key = k.substring(k.indexOf("#") + 1);
			if (!moduleName.trim().equals(m.trim())) {
				continue;
			}
			log.debug("开始获取属性'" + key + "'的值。");
			String value = ((String[]) map.get(k))[0];
			AttributeModule attr = am.getAttribute(key);
			if (attr == null) {
				log.debug("属性'" + key + "'不存在模板'" + moduleName + "'中。");
				continue;
			}
			String type = attr.getValue("type");
			addValueTo(entity, type, key, value);
		}
		return entity;
	}

	public static String[] getParameterValues(String key) {
		Object val = ServletActionContext.getContext().getParameters().get(key);
		if (val == null) {
			return new String[] {};
		} else {
			return (String[]) val;
		}
	}

	private static void addValueTo(ApplicationEntity entity, String type,
			String key, String value) {
		Log log = LogFactory.getLog(ActionHelper.class);
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
		} else if ("java.lang.Boolean".equals(type)) {
			try {
				boolean v = Boolean.parseBoolean(value);
				log.debug("添加属性值'" + key + "'='" + v + "'结果:"
						+ entity.setValue(key, v).toString());
			} catch (Exception e) {
				// e.printStackTrace();
				log.debug("转换属性值'" + value + "'为java.lang.Double失败。");
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
		} else if ("java.util.Date".equals(type)) {
			try {
				if(value!=null && value.indexOf(":")<0){
					value = value +" 00:00:00"; 
				}
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
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
