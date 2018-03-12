package com.iscreate.op.dao.dynamicformfield;

import java.util.List;

import com.iscreate.op.pojo.dynamicformfield.*;

public interface DynamicFormFieldDao {
	/***
	 *  查询控件信息
	 *  
	 * @param formCode 动态表单的编码
	 * @return
	 */
	public  List<FormControl> queryFormControl(String formCode);
	
	
	
	
	/***
	 * 
	 * 查询控件属性值
	 * @param controlId  控件id  
	 * @return
	 */
	public  List<FormControlAttribute> queryFormControlAttribute(String controlId);
	
	
	/***
	 *  查询一个控件的值
	 * @param controlId
	 * @return
	 */
	public  List<FormValue>   queryFormValue(String rowId,String formCodes);
	
	
	/***
	 * 
	 * 动态表单的信息入库
	 * 
	 * @param value
	 */
	public  void  submitFormValue(FormValue value);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
