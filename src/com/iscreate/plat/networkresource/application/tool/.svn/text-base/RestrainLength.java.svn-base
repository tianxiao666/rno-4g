package com.iscreate.plat.networkresource.application.tool;



/**
 * 属性长度约束实现类。
 * 
 * @author admin
 * 
 */
class RestrainLength extends RestrainAttribute {

	public RestrainLength(RestrainAttribute attributeRestraint) {
		super(attributeRestraint);
	}

	@Override
	protected Consequence _restrain(ApplicationEntity ado, ApplicationModule dic) {
		return new Consequence(successCode, "success");
	}

	@Override
	protected Consequence _kvRestrain(String key, Object value,
			ApplicationModule dic) {
		if (value == null) {
			return new Consequence(successCode, "success");
		}
		int len = dic.getAttribute(key).getValue(length);
		String t = dic.getAttribute(key).getValue(type);
		if (len > 0 && "java.lang.String".equals(t)) {
			String v = value.toString();
			if (v.length() > len) {
				String consequence = "模板\"" + dic.getModuleName()
						+ "\"属性:\"" + key + "\"定义的长度：\"" + len + "\"小于值的长度：\""
						+ v.length() + "\"。";
				return new Consequence(lengthRestraintFailureCode,
						consequence);
			}
		}
		return new Consequence(successCode, "success");
	}

}
