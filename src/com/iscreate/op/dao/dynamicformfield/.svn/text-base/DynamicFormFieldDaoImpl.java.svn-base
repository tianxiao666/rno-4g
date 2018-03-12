package com.iscreate.op.dao.dynamicformfield;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.dynamicformfield.FormControl;
import com.iscreate.op.pojo.dynamicformfield.FormControlAttribute;
import com.iscreate.op.pojo.dynamicformfield.FormValue;


public class DynamicFormFieldDaoImpl implements DynamicFormFieldDao{
	
	private HibernateTemplate hibernateTemplate;
	/***
	 *  查询控件信息
	 *  
	 * @param formCode 动态表单的编码
	 * @return
	 */
	public  List<FormControl> queryFormControl(String formCode){		
		String hql = "from FormControl r where r.formCode='"+formCode+"' order by  r.orderStr   ";
		return (List<FormControl>)hibernateTemplate.find(hql);
	}
	
	
	
	
	/***
	 * 
	 * 查询控件属性值
	 * @param controlId  控件id  
	 * @return
	 */
	public  List<FormControlAttribute> queryFormControlAttribute(String controlId){
		String hql = "from FormControlAttribute r where r.controlId='"+controlId+"'";
		return (List<FormControlAttribute>)hibernateTemplate.find(hql);
	}
	
	
	/***
	 *  查询一个控件的值
	 * @param controlId
	 * @return
	 */
	public  List  queryFormValue(String rowId,String formCode){
		String hql = "from FormValue p where p.rowId='"+rowId+"'  and p.formCode='"+formCode+"'   ";
		List<FormValue> list = (List<FormValue>)hibernateTemplate.find(hql);
		return list;
	}
	
	
	/***
	 * 
	 * 动态表单的信息入库
	 * 
	 * @param value
	 */
	public  void  submitFormValue(FormValue value){
	
		hibernateTemplate.saveOrUpdate(value);
	
	}




	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}




	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	

}
