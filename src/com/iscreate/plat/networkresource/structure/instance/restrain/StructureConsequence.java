package com.iscreate.plat.networkresource.structure.instance.restrain;



public class StructureConsequence {
	private String code = "";
	private Object consequence;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getConsequence() {
		return consequence;
	}

	public void setConsequence(Object consequence) {
		this.consequence = consequence;
	}

	public StructureConsequence(String code, Object consequence) {
		this.code = code;
		this.consequence = consequence;
	}

	public StructureConsequence() {

	}

	public String toString() {
		return code + ":" + consequence;
	}
}
