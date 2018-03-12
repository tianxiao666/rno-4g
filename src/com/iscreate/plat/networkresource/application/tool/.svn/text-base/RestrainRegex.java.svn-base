package com.iscreate.plat.networkresource.application.tool;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式约束实现类。
 * 
 * @author admin
 * 
 */
class RestrainRegex extends RestrainAttribute {

	public RestrainRegex(RestrainAttribute attributeRestraint) {
		super(attributeRestraint);
	}

	@Override
	protected Consequence _restrain(ApplicationEntity ado,
			ApplicationModule dic) {
		return new Consequence(successCode, "success");
	}

	@Override
	protected Consequence _kvRestrain(String key,
			Object value, ApplicationModule dic) {
		if (value == null) {
			return new Consequence(successCode, "success");
		}
		String t = dic.getAttribute(key).getValue(type);
		String r = dic.getAttribute(key).getValue(regex);
		r = r.trim();
		if (!r.isEmpty() && "java.lang.String".equals(t)) {
			String v = value.toString();
			Pattern p = java.util.regex.Pattern.compile(r);
			Matcher m = p.matcher(v);
			boolean b = m.matches();
			if (!b) {
				String consequence = "模板\"" + dic.getModuleName()
						+ "\"中属性\"" + key + "\"的约束定义为：\"" + r + "\"，与设置的值:\""
						+ v + "\"不符。";
				return new Consequence(regexFailureCode,
						consequence);
			}
		}
		return new Consequence(successCode, "success");
	}

}
