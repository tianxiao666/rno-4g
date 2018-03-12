package com.iscreate.plat.networkresource.application.tool;



import java.lang.reflect.Array;

/**
 * 类型约束实现类。
 * 
 * @author admin
 * 
 */
class RestrainType extends RestrainAttribute {

	public RestrainType(RestrainAttribute attributeRestraint) {
		super(attributeRestraint);
	}

	@Override
	protected Consequence _restrain(ApplicationEntity ado, ApplicationModule dic) {
		return new Consequence(successCode, "success");
	}

	@Override
	protected Consequence _kvRestrain(String key, Object value,
			ApplicationModule dic) {
		// 如果属性为空，能不进行类型检测
		if (value == null) {
			return new Consequence("Object-State-0000", "success");
		}
		Class<?> cs = null;
		String t = dic.getAttribute(key).getValue(type);
		try {
			cs = Class.forName(t);
		} catch (ClassNotFoundException e) {
			String consequence = "模板\"" + dic.getModuleName()
					+ "\"下定义的属性\"" + t + "\"类型不正确.";
			return new Consequence(typeRestrainFailureCode,
					consequence);
		}
		Class<?> valueClass = value.getClass();
		try {
			if (valueClass.isArray()) {
				Object o = Array.get(value, 0);
				valueClass = o.getClass();
			}
		} catch (Exception e) {
			String consequence = "模板\"" + dic.getModuleName() + "\"校验获取值\""
					+ value + "\"的类型时出错：" + e.toString();
			return new Consequence(exceptionCode, consequence);
		}
		if (cs.isAssignableFrom(valueClass)) {
			return new Consequence(successCode, "success");
		} else {
			String consequence = "模板\"" + dic.getModuleName() + "\"属性:\""
					+ key + "\"定义的类型：\"" + t + "\"与值类型：\""
					+ valueClass.getName() + "\"不匹配。";
			return new Consequence(typeRestrainFailureCode,
					consequence);
		}
	}
}
