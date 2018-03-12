package com.iscreate.plat.networkresource.application.tool;



/**
 * 实体对象状态类,记录实体对象中的所有操作状态
 * 
 * @author joe
 * 
 */
public class Consequence {
	private String code;
	private String consequence;

	public Consequence(String code, String consequence) {
		this.code = code;
		this.consequence = consequence;
	}

	public String getCode() {
		return code;
	}

	public String getConsequence() {
		return consequence;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setConsequence(String consequence) {
		this.consequence = consequence;
	}

	public String toString() {
		return this.code + ":" + consequence;
	}
}
