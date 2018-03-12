package com.iscreate.op.dao.informationmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.informationmanage.InformationEnterprise;

public interface EnterpriseInformationDao extends BaseDao<InformationEnterprise> {

	/************ 属性 **************/
	public List<InformationEnterprise> find(Map paraMap);

	/************ 属性 **************/
	public boolean checkEnterpriseRegisterNumberExists(String registerNumber, String id);

	/**
	 * 根据企业id,获取企业信息
	 * @param enterPriseId - 企业id
	 * @return
	 */
	public abstract Map<String,String> findEnterPriseByEnterPriseId(String enterPriseId);
	
	
	
	
	
}
