package com.iscreate.op.action.informationmanage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.informationmanage.Area;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.informationmanage.Staff;
import com.iscreate.op.service.informationmanage.EnterpriseInformationService;

@SuppressWarnings("unchecked")
public class EnterpriseInformationAction {

	/************* 依赖注入 *************/
	private EnterpriseInformationService enterpriseInformationService;
	
	/********* 属性 *********/
	private Log log = LogFactory.getLog(this.getClass());
	
	/**************** 页面数据 ******************/
	
	private Map pageData_Map = new HashMap();
	
	/********** XML 使用属性 ************/
	private String url;
	

	/************************* Action ******************************/
	
	
	/**
	 * 验证项目编号是否唯一
	 * 
	 * @page - 项目信息管理页面
	 * @write true : 已存在 , false : 不存在
	 */
	public void checkEnterpriseRegisterNumberExists() {
		log.debug(" ProjectInformationAction ->> checkProjectNumberExists ");
		Map<String, String> param_map = ActionUtil.getRequestParamMap("proId", "id","registerNumber");
		boolean exists = enterpriseInformationService.checkEnterpriseRegisterNumberExists(param_map.get("registerNumber"), param_map.get("id"));
		try {
			ActionUtil.responseWrite(exists);
			log.debug(" ProjectInformationAction ->> checkEnterpriseRegisterNumberExists --> write(exists:" + exists + ")");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询所有企业的信息(Ajax)
	 * @page - 企业信息管理页面
	 * @write 所有企业的信息
	 */
	public void findAllEnterpriseInfoAjax () {
		log.debug(" EnterpriseInformationAction ->> findAllEnterpriseInfoAjax ");
		Map<String, String> param_Map = ActionUtil.getRequestParamMap(null);
		List<InformationEnterprise> list = enterpriseInformationService.findByParam(param_Map);
		try {
			ActionUtil.responseWrite(list);
			log.debug(" EnterpriseInformationAction ->> findSingleEnterpriseInfo --> write(list:" + list + ")");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询单个企业的信息
	 * @page - 查看单个企业信息页面
	 * @write 企业的信息
	 */
	public void findSingleEnterpriseInfo () {
		log.debug(" EnterpriseInformationAction ->> findSingleEnterpriseInfo ");
		Map<String, String> param_Map = ActionUtil.getRequestParamMap("id");
		Long id = Long.valueOf(param_Map.get("id"));
		Map<String, Object> enterprise_map = enterpriseInformationService.findSingleEnterpriseInfo(id);
		try {
			ActionUtil.responseWrite(enterprise_map);
			log.debug(" EnterpriseInformationAction ->> findSingleEnterpriseInfo --> write(map:" + enterprise_map.entrySet() + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 添加单个企业对象的信息
	 * @page - 企业信息页面列表 -> 查看企业信息
	 * @write - 操作是否成功
	 */
	public void saveEnterpriseInfo () {
		log.debug(" EnterpriseInformationAction ->> saveEnterpriseInfo ");
		Map<String, String> param_Map = ActionUtil.getRequestParamMapByChoiceMap("Enterprise_save#");
		try {
			Long id = enterpriseInformationService.saveEnterpriseInfo(param_Map);
			ActionUtil.responseWrite(id);
			log.debug(" EnterpriseInformationAction ->> saveEnterpriseInfo --> write(id:" + id + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新单个企业对象的信息
	 * @param - 企业信息页面列表 -> 查看企业信息对话框
	 * @write - 修改是否成功
	 */
	public void updateEnterpriseInfo () {
		log.debug(" EnterpriseInformationAction ->> updateEnterpriseInfo ");
		Map<String, String> param_Map = ActionUtil.getRequestParamMapByChoiceMap("Enterprise_update#");
		try {
			InformationEnterprise enterprise = ObjectUtil.map2Object(param_Map, InformationEnterprise.class);
			int num = enterpriseInformationService.txupdate(enterprise);
			ActionUtil.responseWrite(num);
			log.debug(" EnterpriseInformationAction ->> updateEnterpriseInfo --> write(num:" + num + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除单个企业对象的信息
	 * @param - 企业信息页面列表 -> 查看企业信息对话框
	 * @write - 删除是否成功
	 */
	public void deleteEnterpriseInfo () {
		log.debug(" EnterpriseInformationAction ->> deleteEnterpriseInfo ");
		Map<String, String> param_map = ActionUtil.getRequestParamMap("id");
		Long id = StringUtil.isLong(param_map.get("id"));
		int num = enterpriseInformationService.txremove(id);
		try {
			ActionUtil.responseWrite(num);
			log.debug(" EnterpriseInformationAction ->> deleteEnterpriseInfo --> write(num:" + num + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查找客户,服务商
	 */
	public void findClientServerEnterpriseInfo () {
		log.debug(" EnterpriseInformationAction ->> findClientServerEnterpriseInfo ");
		List<InformationEnterprise> list = enterpriseInformationService.findByParam(null,false);
		Map<String,List<Map<String,Object>>> map = new LinkedHashMap<String, List<Map<String,Object>>>();
		map.put("server", new ArrayList<Map<String,Object>>());
		map.put("client", new ArrayList<Map<String,Object>>());
		
		for (int i = 0; i < list.size(); i++) {
			InformationEnterprise enterprise = list.get(i);
			String cooperative = enterprise.getCooperative();
			if ( cooperative == null || StringUtil.isNullOrEmpty(cooperative) ) {
				continue;
			}
			try {
				if ( cooperative.equals("服务商") ) {
					List<Map<String, Object>> serverList = map.get("server");
					Map<String, Object> enterprise_map;
					enterprise_map = ObjectUtil.object2Map(enterprise, false);
					enterprise_map = formatEnterpriseMap(enterprise_map);
					serverList.add(enterprise_map);
				} else if ( cooperative.equals("运营商") ) {
					List<Map<String, Object>> serverList = map.get("client");
					Map<String, Object> enterprise_map;
					enterprise_map = ObjectUtil.object2Map(enterprise, false);
					enterprise_map = formatEnterpriseMap(enterprise_map);
					serverList.add(enterprise_map);
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			ActionUtil.responseWrite(map);
			log.debug(" EnterpriseInformationAction ->> findClientServerEnterpriseInfo --> write(map:" + map + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/******************* 格式化 *********************/
	public Map<String,Object> formatEnterpriseMap ( Map<String,Object> enterprise ) {
		Map<String,Object> obj_map = new LinkedHashMap<String, Object>();
		for (Iterator<String> it = enterprise.keySet().iterator();it.hasNext();) {
			String key = it.next();
			Object value = enterprise.get(key);
			obj_map.put(key, value);
			if ( value == null ) {
				continue;
			}
			if ( value instanceof Date ) {
				Date date = (Date)value;
				String formatDate = DateUtil.formatDate(date,"yyyy-MM-dd");
				obj_map.put(key, formatDate);
			}
		}
		return obj_map;
	}
	
	
	/*********** getter setter ***********/
	public EnterpriseInformationService getEnterpriseInformationService() {
		return enterpriseInformationService;
	}
	public void setEnterpriseInformationService(
			EnterpriseInformationService enterpriseInformationService) {
		this.enterpriseInformationService = enterpriseInformationService;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map getPageData_Map() {
		return pageData_Map;
	}
	public void setPageData_Map(Map pageData_Map) {
		this.pageData_Map = pageData_Map;
	}




	
	
	
}
