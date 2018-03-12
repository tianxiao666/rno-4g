package com.iscreate.op.action.dynamicformfield;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.iscreate.op.service.dynamicformfield.*;

import com.iscreate.op.service.workmanage.*;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.*;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.iscreate.op.service.workmanage.*;

public class DynamicFormFieldAction {

	private DynamicFormFieldService dynamicFormFieldService;

	public String areaCode;

	public String formCode;

	public String divString;

	public String rowId;

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String testForm() {
		this.divString = dynamicFormFieldService.getDynamicFormFieldDivHtml(
				areaCode, formCode, rowId);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("divString", divString);
		return "success";
	}

	public String testPost() {

		HttpServletRequest request = ServletActionContext.getRequest();
		dynamicFormFieldService.txDynamicFormFieldSubmit(rowId, areaCode,
				formCode, request);
		return "success";
	}

	public DynamicFormFieldService getDynamicFormFieldService() {
		return dynamicFormFieldService;
	}

	public void setDynamicFormFieldService(
			DynamicFormFieldService dynamicFormFieldService) {
		this.dynamicFormFieldService = dynamicFormFieldService;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getDivString() {
		return divString;
	}

	public void setDivString(String divString) {
		this.divString = divString;
	}

}
