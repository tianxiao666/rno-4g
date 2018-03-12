package com.iscreate.plat.networkresource.application.tool;



class RestraintFactory {

	private final static RestrainAttribute restrainAttribute = new RestrainRegex(
			new RestrainLength(new RestrainType(new RestrainNull(null))));

	public static RestrainAttribute getAttributeRestraint() {
		return restrainAttribute;
	}
}
