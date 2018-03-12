package com.iscreate.op.service.informationmanage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;

public interface EnterpriseInformationService extends BaseService<InformationEnterprise> {

	public Map<String, Object> findSingleEnterpriseInfo(Long id);

	public Long saveEnterpriseInfo(Map<String,String> param_Map);

	public boolean checkEnterpriseRegisterNumberExists(String registerNumber, String id);



}
