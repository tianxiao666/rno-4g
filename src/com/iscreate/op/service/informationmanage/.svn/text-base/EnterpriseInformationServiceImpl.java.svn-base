package com.iscreate.op.service.informationmanage;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.EnterpriseInformationDao;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;

/**
 * 企业信息管理服务类
 * @author andy
 */
@SuppressWarnings("finally")
public class EnterpriseInformationServiceImpl extends BaseServiceImpl<InformationEnterprise> implements EnterpriseInformationService {
	
	/************* 依赖注入 **************/
	EnterpriseInformationDao enterpriseInformationDao;
	private Log log = LogFactory.getLog(this.getClass());
	
	
	
	public boolean checkEnterpriseRegisterNumberExists ( String registerNumber , String id ) {
		boolean exists = false;
		if ( StringUtil.isNullOrEmpty(id) ) {
			id = "0";
		}
		exists = enterpriseInformationDao.checkEnterpriseRegisterNumberExists(registerNumber, id);
		return exists;
	}
	
	
	public Map<String, Object> findSingleEnterpriseInfo ( Long id ) {
		InformationEnterprise enterprise = get(id,false);
		Map<String, Object> enterprise_map = null;
		try {
			enterprise_map = ObjectUtil.object2Map(enterprise, false);
			if( enterprise_map.containsKey("createDate") && enterprise_map.get("createDate") != null ) {
				Date createDate = (Date) enterprise_map.get("createDate");
				String formatDate = DateUtil.formatDate(createDate,"yyyy-MM-dd");
				enterprise_map.put("createDate", formatDate);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			return enterprise_map;
		}
	}
	
	
	public Long saveEnterpriseInfo ( Map<String,String> param_Map ) {
		InformationEnterprise enterprise = null;
		Long id = 0l;
		try {
			enterprise = ObjectUtil.map2Object(param_Map, InformationEnterprise.class);
			id = txinsert(enterprise);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			return id;
		}
	}
	
	
	/*********** getter setter ************/
	public EnterpriseInformationDao getEnterpriseInformationDao() {
		return enterpriseInformationDao;
	}
	public void setEnterpriseInformationDao(
			EnterpriseInformationDao enterpriseInformationDao) {
		this.enterpriseInformationDao = enterpriseInformationDao;
	}
	
}
