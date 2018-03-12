package com.iscreate.plat.workflow;

public enum BeanType {

	Bean_Type_DataInputService("DataInputServiceBean"), Bean_Type_FlowProcessor(
			"FlowProcessorBean"), Bean_Type_Service("ServiceBean"), Bean_Type_Manager(
			"ManagerBean"), Bean_Type_ExtDataConfig("ExtDataConfigBean"), Bean_Type_RelAnalyzer(
			"RelAnalyzerBean");

	String type;

	BeanType(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
	
	
}
