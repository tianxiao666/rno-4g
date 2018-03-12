package com.iscreate.plat.networkresource.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.AttributeModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.action.ActionHelper;

public class MobileUtil {
	/**
	 * 将一个jmap转换为ae对象
	 * @param moduleName 模板名称
	 * @param params 参数map
	 * @return
	 */
	public static ApplicationEntity getAeFromMap(String moduleName, Map<String ,String> params) {
		if(params == null || params.size() == 0) {
			return null;
		}
		ApplicationModule module = ModuleProvider.getModule(moduleName);
		if(module == null) {
			return null;
		}
		ApplicationEntity ae = module.createApplicationEntity();
		for(String key : params.keySet()) {
			AttributeModule attribute = module.getAttribute(key);
			if(attribute != null && !"".equals(attribute)) {
				String type = module.getAttribute(key).getValue("type");
				if(type != null && !"".equals(type)) {
					addValueTo(ae, type, key, params.get(key));
				}
			}
		}
		return ae;
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
