package com.iscreate.op.service.workmanage;

import java.util.List;

import com.iscreate.op.pojo.workmanage.WorkmanageField;

public interface FieldService {

	
	
	/**
	 * 根据类型标识返回查询条件类型的字段列表
	 * @param categoryFlag
	 * @return
	 */
	public List<WorkmanageField> getInputFieldsByCategoryFlag(String categoryFlag);
	
	/**
	 * 根据类型标识返回查询结果类型的字段列表
	 * @param categoryFlag
	 * @return
	 */
	public List<WorkmanageField> getResultFieldsByCategoryFlag(String categoryFlag);
}
