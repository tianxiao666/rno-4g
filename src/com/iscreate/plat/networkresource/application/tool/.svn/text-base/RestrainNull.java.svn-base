package com.iscreate.plat.networkresource.application.tool;



/**
 * 空值约束实现类。
 * 
 * @author admin
 * 
 */
class RestrainNull extends RestrainAttribute {

	public RestrainNull(RestrainAttribute attributeRestraint) {
		super(attributeRestraint);
	}

	protected Consequence _restrain(ApplicationEntity ado, ApplicationModule dic) {
		StringBuilder builder = new StringBuilder();
		boolean unsuccess = false;
		builder.append("数据对象：\"" + dic.getModuleName() + "\"中");
		for (String key : dic.keyset()) {
			AttributeModule ad = dic.getAttribute(key);
			boolean p = ad.getValue(primary);
			boolean n = ad.getValue(nullable);
			if (!n || p) {
				if (!ado.containKey(key)) {
					unsuccess = true;
					builder.append(",属性\"");
					builder.append(key);
					builder.append("\"值不能为空");
				}
			}
		}
		if (unsuccess) {
			return new Consequence(unSuccessCode, builder.toString());
		} else {
			return new Consequence(successCode, "success");
		}
	}

	@Override
	protected Consequence _kvRestrain(String key, Object value,
			ApplicationModule dic) {
		if (!dic.containKey(key)) {
			String consequence = "属性\"" + key + "\"在模板\""
					+ dic.getModuleName() + "\"中并没有定义。";
			return new Consequence(attrMissCode, consequence);
		}
		AttributeModule ad = dic.getAttribute(key);
		boolean p = ad.getValue(primary);
		boolean n = ad.getValue(nullable);
		if (!n || p) {
			if (value == null) {
				String consequence = "属性\"" + key + "\"在模板\""
						+ dic.getModuleName() + "\"中定义为非空，与设置的空值不符。";
				return new Consequence(nullRestrainFailureCode,
						consequence);
			}
		}
		String consequence = "success";
		return new Consequence(successCode, consequence);
	}

}
