package com.iscreate.op.service.workmanage.util;

/**
 * @author che.yd
 */
public class CommonQueryUtil {


	/**
	 * 转换SQL操作符
	 * @param formOperator
	 * @return
	 */
	public static String getSqlOperator(String formOperator){
		String sqlOperator=null;
		if(formOperator!=null){
			formOperator = formOperator.trim();
			if("=".equals(formOperator)){
				sqlOperator = "=";
			}
			if("like".equals(formOperator)){
				sqlOperator = "like";
			}
			if(">=".equals(formOperator)){
				sqlOperator = ">=";
			}
			if("<=".equals(formOperator)){
				sqlOperator = "<=";
			}
			if("!=".equals(formOperator)){
				sqlOperator = "<>";
			}
		}
		return sqlOperator;
	}
	
	
	
	
}
