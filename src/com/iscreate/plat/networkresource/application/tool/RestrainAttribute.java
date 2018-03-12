package com.iscreate.plat.networkresource.application.tool;



/**
 * 数据校验的抽象类。校验的状态如下：<br/>
 * "Object-Restraint-0000":校验成功。<br/>
 * "Object-Restraint-0001":属性定义未存在字典中。<br/>
 * "Object-Restraint-0002":值类型与属性定义类型不一致。<br/>
 * "Object-Restraint-0003":值长度超过属性定义长度。<br/>
 * "Object-Restraint-0004":定义为非空的属性未设值。<br/>
 * "Object-Restraint-0005":属性值与校验规则不匹配。<br/>
 * "Object-Restraint-1000":其他异常<br/>
 * "Object-Restraint-0100":找不到相应字典信息。<br/>
 * "Object-Restraint-1001":没有通过校验。<br/>
 * 
 * @author joe
 * 
 */
abstract class RestrainAttribute {
	protected RestrainAttribute attributeRestraint;
	protected final String successCode = "Object-Restraint-0000";
	protected final String attrMissCode = "Object-Restraint-0001";
	protected final String typeRestrainFailureCode = "Object-Restraint-0002";
	protected final String lengthRestraintFailureCode = "Object-Restraint-0003";
	protected final String nullRestrainFailureCode = "Object-Restraint-0004";
	protected final String regexFailureCode = "Object-Restraint-0005";
	protected final String dicMissCode = "Object-Restraint-0100";
	protected final String exceptionCode = "Object-Restraint-1000";
	protected final String unSuccessCode = "Object-Restraint-1001";

	static final String length = "length";
	static final String type = "type";
	static final String primary = "primary";
	static final String nullable = "nullable";
	static final String regex = "regex";

	public RestrainAttribute(RestrainAttribute attributeRestraint) {
		this.attributeRestraint = attributeRestraint;
	}

	public Consequence kvRestrain(String key, Object value,
			ApplicationModule dic) {
		Consequence consequence;
		if (dic == null) {
			consequence = new Consequence(dicMissCode,
					"找不到相应字典信息，请确认是否已经定义相关字典信息。");
			return consequence;
		}
		if (attributeRestraint != null) {
			consequence = attributeRestraint.kvRestrain(key, value, dic);
			if (!successCode.equals(consequence.getCode())) {
				return consequence;
			}
		}
		consequence = _kvRestrain(key, value, dic);
		return consequence;
	}

	public Consequence restrain(ApplicationEntity ado, ApplicationModule dic) {
		Consequence consequence;
		if (this.attributeRestraint != null) {
			consequence = attributeRestraint.restrain(ado, dic);
			if (!successCode.equals(consequence.getCode())) {
				return consequence;
			}
		}
		consequence = _restrain(ado, dic);
		return consequence;
	}

	protected abstract Consequence _restrain(ApplicationEntity ado,
			ApplicationModule dic);

	protected abstract Consequence _kvRestrain(String key,
			Object value, ApplicationModule dic);
}
