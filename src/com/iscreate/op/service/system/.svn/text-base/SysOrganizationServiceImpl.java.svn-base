package com.iscreate.op.service.system;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.informationmanage.EnterpriseInformationDao;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.informationmanage.ProjectInformationDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.dao.system.SysAreaRelaOrgDao;
import com.iscreate.op.dao.system.SysDictionaryDao;
import com.iscreate.op.dao.system.SysOrgUserDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.dao.system.SysRoleDao;
import com.iscreate.op.dao.system.SysUserRelaOrgDao;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgRelaArea;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysPermission;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysUserRelaOrg;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.TreeListHelper;


public class SysOrganizationServiceImpl  implements SysOrganizationService{
	private static Log log = LogFactory.getLog(SysOrganizationServiceImpl.class);
	private SysOrganizationDao sysOrganizationDao;
	private EnterpriseInformationDao enterpriseInformationDao;

    private SysUserRelaOrgDao sysUserRelaOrgDao;
	private SysOrgUserDao sysOrgUserDao;
	private SysRoleDao sysRoleDao;
	private ProjectInformationDao projectInformationDao;
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	private SysAreaDao sysAreaDao;
	private SysAreaRelaOrgDao sysAreaRelaOrgDao;
	
	private CommonDao commonDao;//公共dao
	private SysDictionaryDao sysDictionaryDao;//数据字典dao
	
	/**
	 * 
	 * @description: 获取登陆人所属的企业
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,String>     
	 * @date：Sep 26, 2013 11:10:59 AM
	 */
	public Map<String,String> getLoginIdBelongEnterpriseTypeService(String account){
		log.info("进入getLoginIdBelongEnterpriseTypeService方法");
		Map<String,String> map = null;
		//判断用户是否为用户管理员
		if(account!=null || "".equals(account)){
			List<SysRole> userAllRoles = this.sysRoleDao.getUserRoles(account);//null; // this.getUserAllRoles(account);  获取角色
			if(userAllRoles!=null && userAllRoles.size()>0){
				for (SysRole role : userAllRoles) {
					if("systemManager".equals(role.getCode())){
						map = new HashMap<String, String>();
						map.put("isCoo", "systemManager");
						return map;
					}
				}	
			}
			InformationEnterprise enterprise = this.sysOrganizationDao.getAccountEnterpriseByAccount(account);
			if(enterprise!=null){
				map = new HashMap<String, String>();
				map.put("id",enterprise.getId()+"");
				String cooperative = enterprise.getCooperative();
				if(cooperative!=null && !"".equals(cooperative)){
					if(InformationConstant.ENTERPRISE_TYPE_SERVER.equals(cooperative)){
						map.put("isCoo", "true");
					}
					else if(InformationConstant.ENTERPRISE_TYPE_CARRIEROPERATOR.equals(cooperative)){
						map.put("isCoo", "false");
					}else{
						return null;
					}
				}else{
					return null;
				}
			}
		}
		//map = new HashMap<String, String>();
		//map.put("isCoo", "systemManager");//hard code
		log.info("执行getLoginIdBelongEnterpriseTypeService方法成功，实现了”获取登陆人所属的企业“的功能");
		log.info("退出getLoginIdBelongEnterpriseTypeService方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getAllSecondOrgListByOrgUserId(long orgUserId){
		log.info("进入getSecondOrgListByOrgUserId方法");
		List<Map<String, Object>> list = this.sysOrganizationDao.getAllSecondOrgListByOrgUserId(orgUserId);
		log.info("执行getSecondOrgListByOrgUserId方法成功，实现了”根据orgUserId获取所在公司的第二级组织“的功能");
		log.info("退出getSecondOrgListByOrgUserId方法,返回List<Map<String, Object>>");
		return list;
	}
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getAllSecondOrgStringByOrgUserId(long orgUserId){
		String org_ids = "";
		log.info("进入getSecondOrgListByOrgUserId方法");
		List<Map<String, Object>> list = this.sysOrganizationDao.getAllSecondOrgListByOrgUserId(orgUserId);
		if(list != null && list.size() > 0){
			for(Map<String, Object> m:list){
				org_ids += m.get("ORG_ID") + ",";
			}
			if(!org_ids.equals("")){
				org_ids = org_ids.substring(0, org_ids.length() - 1);
			}
		}
		log.info("执行getSecondOrgListByOrgUserId方法成功，实现了”根据orgUserId获取所在公司的第二级组织“的功能");
		log.info("退出getSecondOrgListByOrgUserId方法,返回List<Map<String, Object>>");
		return org_ids;
	}
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserSecondOrgListByOrgUserId(long orgUserId){
		log.info("进入getSecondOrgListByOrgUserId方法");
		List<Map<String, Object>> list = this.sysOrganizationDao.getUserSecondOrgByOrgUserId(orgUserId);
		log.info("执行getSecondOrgListByOrgUserId方法成功，实现了”根据orgUserId获取所在公司的第二级组织“的功能");
		log.info("退出getSecondOrgListByOrgUserId方法,返回List<Map<String, Object>>");
		return list;
	}
	
	/**
	 * 根据orgUserId获取所在公司的第二级组织
	* @author ou.jh
	* @date Aug 27, 2013 2:25:11 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public String getUserSecondOrgStringByOrgUserId(long orgUserId){
		String org_ids = "";
		log.info("进入getSecondOrgListByOrgUserId方法");
		List<Map<String, Object>> list = this.sysOrganizationDao.getUserSecondOrgByOrgUserId(orgUserId);
		if(list != null && list.size() > 0){
			for(Map<String, Object> m:list){
				org_ids += m.get("ORG_ID") + ",";
			}
			if(!org_ids.equals("")){
				org_ids = org_ids.substring(0, org_ids.length() - 1);
			}
		}
		log.info("执行getSecondOrgListByOrgUserId方法成功，实现了”根据orgUserId获取所在公司的第二级组织“的功能");
		log.info("退出getSecondOrgListByOrgUserId方法,返回List<Map<String, Object>>");
		return org_ids;
	}
	
	/**
	 * 获取所有最高级组织列表
	 * @return
	 */
	public List<Map<String,String>> getAllTheTopProviderOrgService(String enterpriseType){
		log.info("进入getAllTheTopProviderOrgService(String enterpriseType)，enterpriseType="+enterpriseType+",获取所有最高级组织列表");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<SysOrg> allTheTopProviderOrg =this.sysOrganizationDao.getAllTheTopProviderOrg(enterpriseType);
		if(allTheTopProviderOrg!=null && allTheTopProviderOrg.size()>0){
			for (SysOrg po : allTheTopProviderOrg) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("orgId", po.getOrgId()+"");
				map.put("orgName", po.getName());
				map.put("enterpriseId", po.getEnterpriseId()+"");
				list.add(map);
			}
		}
		log.info("退出getAllTheTopProviderOrgService(String enterpriseType)，返回结果list"+list);
		return list;
	}
	
	/**
	 * 通过组织id获取组织 
	* @author ou.jh
	* @date Nov 4, 2013 3:59:08 PM
	* @Description: TODO 
	* @param @param orgId        
	* @throws
	 */
	public SysOrg getOrgByOrgId(long orgId){
		SysOrg orgByOrgId = this.sysOrganizationDao.getOrgByOrgId(orgId);
		return orgByOrgId;
	}
	
	/**
	 * 根据企业Ids，类型获取组织树(根节点树)
	 * @param enterpriseId
	 * @param enterpriseType
	 */
	public void getProviderOrgTreeDownwardByEnterpriseIdAjaxService(String enterpriseIds,String enterpriseType){
		log.info("进入getCustomerOrgTreeDownwardByEnterpriseIdAjaxService方法");
		log.info("参数enterpriseIds="+enterpriseIds);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		//Set<Map<String,Object>> set = new HashSet<Map<String,Object>>();
		List<Map<String,Object>> set = new ArrayList<Map<String,Object>>();
		if(enterpriseIds!=null && !"".equals(enterpriseIds)){
			List<Map<String,Object>> orgList = this.sysOrganizationDao.getSelfAndChildOrgsListByEnterpriseId(enterpriseIds,enterpriseType);
			
			if(orgList!=null && !orgList.isEmpty()){
				set = TreeListHelper.getTreeListByDataList(orgList);
			}				
		}
		String result = gson.toJson(set);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回jsp页面时出错");
			throw new UserDefinedException("返回jsp页面时出错");
		}
		log.info("执行getCustomerOrgTreeDownwardByEnterpriseIdAjaxService方法成功，实现了”根据企业Ids，类型获取组织树“的功能");
		log.info("退出getCustomerOrgTreeDownwardByEnterpriseIdAjaxService方法,返回void");
	}
	/**
	 * @author du.hw
	 * @craete_time:2013-05-31
	 * 重组组织列表结构
	 * 
	 */
	
	/**
	 * 根据组织Ids获取组织树
	 * @param orgIds
	 * 
	 */
	public void getProviderOrgTreeDownwardByorgIdsAjaxService(String orgIds){
		log.info("进入getProviderOrgTreeDownwardByorgIdsAjaxService(String orgIds)方法,orgIds="+orgIds);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		//Set<Map<String,Object>> set = new HashSet<Map<String,Object>>();
		List<Map<String,Object>> set = new ArrayList<Map<String,Object>>();
		if(orgIds!=null && !"".equals(orgIds)){
			String[] array = orgIds.split(",");
			if(array!=null&&array[0]!=""){
				List<Map<String,Object>> orgList = this.sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(orgIds);
				if(orgList!=null && !orgList.isEmpty()){
					set = TreeListHelper.getTreeListByDataList(orgList);
					/*for(String orgId:array){
						Map<String,Object> poService = getOrgTreeDownwardMapByOrgList(orgList,orgId);
						if(poService!=null && poService.size()>0){
							set.add(poService);
						}
					}*/
				}				
			}
		}
		String result = gson.toJson(set);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回jsp页面时出错");
			throw new UserDefinedException("返回jsp页面时出错");
		}
		log.info("执行getProviderOrgTreeDownwardByorgIdsAjaxService(String orgIds)方法成功，实现了”根据企业Ids，类型获取组织树“的功能");
		log.info("退出getProviderOrgTreeDownwardByorgIdsAjaxService(String orgIds)方法,返回void");
	}
	/**
	 * 根据组织id,组织列表list获取组织树数据格式<Map>
	 * @param orgId
	 * @return
	 */
	/*public Map<String,Object> getOrgTreeDownwardMapByOrgList(List<Map<String,Object>> orgList,String orgId){
		log.info("进入getOrgTreeDownwardMapByOrgList(List<SysOrg> orgList,String orgId),根据组织id,组织列表list获取组织树数据格式<Map>,递归");
		log.info("进入getOrgTreeDownwardMapByOrgList(List<SysOrg> orgList,String orgId),orgList="+orgList+",orgId="+orgId);
		Map<String,Object> orgTree = new HashMap<String,Object>();
		List<Map<String,Object>> orgTreeNode = new ArrayList<Map<String,Object>>();
		if(orgList!=null && !orgList.isEmpty() && orgId!=null && !"".equals(orgId)&&!"0".equals(orgId)){
			Map<String,Object> org = null;
			String currentOrgName = "";
			long currentOrgId = 0;
			long currentorgParentId = 0;
			for(int i=0;i<orgList.size();i++){
				org = orgList.get(i);
				currentOrgName = org.get("name")+"";
				if(org.get("orgId") != null && !org.get("orgId").equals("") && !org.get("orgId").equals("null")){
					currentOrgId = Long.valueOf(org.get("orgId")+"");
				}
				if(org.get("parentId") != null && !org.get("parentId").equals("") && !org.get("parentId").equals("null")){
					currentorgParentId = Long.valueOf(org.get("parentId")+"");
				}
				if(orgId.equals(currentOrgId+"")){
					orgTree.put("orgName", currentOrgName);
					orgTree.put("orgId", currentOrgId);
					//orgList.remove(i);
					break;
				}
				
			}
			for(int i=0;i<orgList.size();i++){
				org = orgList.get(i);
				if(org.get("orgId") != null && !org.get("orgId").equals("") && !org.get("orgId").equals("null")){
					currentOrgId = Long.valueOf(org.get("orgId")+"");
				}
				if(org.get("parentId") != null && !org.get("parentId").equals("") && !org.get("parentId").equals("null")){
					currentorgParentId = Long.valueOf(org.get("parentId")+"");
				}
				if(orgId.equals(currentorgParentId+"")){
					Map<String,Object>  treeNodeMap =  this.getOrgTreeDownwardMapByOrgList(orgList, currentOrgId+"");
					if(treeNodeMap!=null){
						orgTreeNode.add(treeNodeMap);
					}
				}
			}
			if(orgTreeNode!=null && orgTreeNode.size()>0){
				orgTree.put("childTree", orgTreeNode);
			}
		}
		log.info("退出getOrgTreeDownwardMapByOrgList(List<SysOrg> orgList,String orgId)，返回结果orgTree="+orgTree);
		return orgTree;
	}*/
	/**
	 * 
	 * @description: 根据所传类型获取组织相关数据字典
	 * @author：     
	 * @return void     
	 * @date：May 8, 2013 11:47:53 AM
	 */
	public void getDictionaryAjaxService(String dictionaryType){
		log.info("进入getDictionaryAjaxService(String dictionaryType),dictionaryType="+dictionaryType+",根据所传类型获取组织相关数据字典");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = "";
		if("".equals(dictionaryType)){
			List<InformationEnterprise> findAllList = this.enterpriseInformationDao.find(new HashMap());
			if(findAllList!=null && findAllList.size()>0){
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				for (InformationEnterprise ie : findAllList) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("typeCode", ie.getId()+"");
					map.put("name", ie.getFullName());
					map.put("coo", ie.getCooperative());
					list.add(map);
				}
				result = gson.toJson(list);
			}
		}else{
			List<Map<String,Object>> list = null;
			if("orgType".equals(dictionaryType)){//组织类型  yua.yw 修改于2014-01-20
				List<Map<String,Object>> stList = this.sysDictionaryDao.getChildDictionaryListByCode("ORG_TYPE");
				if(stList!=null && !stList.isEmpty()){
					 list = new ArrayList<Map<String,Object>>();
					 for(Map<String,Object> st:stList){
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("typeCode", st.get("CODE"));
						map.put("name", st.get("NAME"));
						map.put("id",st.get("DATA_TYPE_ID"));
						list.add(map);
					 }
				 }
			}else{//其他
				List<Map<String,Object>> stList = this.sysOrganizationDao.getDictionaryForOrg(dictionaryType); 
				 if(stList!=null && !stList.isEmpty()){
					 list = new ArrayList<Map<String,Object>>();
					 for(Map<String,Object> st:stList){
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("typeCode", st.get("code"));
						map.put("name", st.get("name"));
						map.put("id",st.get("orgTypeId"));
						list.add(map);
					 }
				 }
			}
			 result = gson.toJson(list);
		}
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getDictionaryAjaxService(String dictionaryType)方法成功，实现了”根据所传类型获取组织相关数据字典“的功能");
		log.info("退出getDictionaryAjaxService(String dictionaryType)方法,返回void");
	}
	
	/**
	 * 根据组织Id获取组织信息(组织架构详细信息,上级ID为中文)
	 * @param orgId
	 * @return
	 */
	public Map<String,String> getProviderOrgByOrgIdtoMapService(long orgId){
		log.info("进入getProviderOrgByOrgIdtoMapService方法");
		log.info("参数orgId="+orgId);
		Map<String,String> map = new HashMap<String, String>();
		Map<String,Object> po = this.sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		if(po!=null){
			map.put("parentOrgName",this.chickStr(po.get("parentOrgName")+""));
			map.put("parentOrgId", this.chickStr(po.get("parentId")+""));
			map.put("id", this.chickStr(po.get("orgId")+""));
			map.put("address", this.chickStr(po.get("address")+""));
			map.put("contactPhone", this.chickStr(po.get("tel")+""));
			map.put("dutyPerson", this.chickStr(po.get("orgUserId")+""));
			map.put("dutyPersonName",this.chickStr(po.get("dutyPersonName")+""));
			map.put("dutyPersonPhone", this.chickStr(po.get("orgUserTel")+""));
			map.put("encampment","");
			map.put("name",this.chickStr(po.get("name")+""));
			map.put("orgAttribute",this.chickStr(po.get("orgAttribute")+""));
			map.put("orgDuty",this.chickStr(po.get("description")+""));
			map.put("type", this.chickStr(po.get("type")+""));
			map.put("enterpriseName",this.chickStr( po.get("enterpriseName")+""));
			map.put("enterpriseId",this.chickStr( po.get("enterpriseId")+""));
			map.put("latitude", this.chickStr(po.get("latitude")+""));
			map.put("longitude",this.chickStr(po.get("longitude")+""));
			map.put("status",this.chickStr(po.get("status")+""));
			if(po.get("createTime")!=null){
				map.put("foundationTime", TimeFormatHelper.getTimeFormatBySecond(po.get("createTime")+""));
			}
		}
			String areaIds = "";
			List<Map<String,Object>> areaList = this.sysAreaDao.getAreaListByOrgId(orgId+"");//区域列表
			if(areaList!=null && !areaList.isEmpty()){
				Map<String,Object> areaMap = new HashMap<String,Object>();
				for(Map<String,Object> mp :areaList){
					String path = "";
					String areaId =  mp.get("areaId")+"";
					areaIds +=","+areaId;
					String areaName = mp.get("name")+"";
					if(mp.get("path")!=null){
						path =  mp.get("path")+"";
						path=path.replace("/"+areaId+"/", "/"+areaName+"/");//替换path中id值为区域名
					}
					areaMap.put(areaId, path);
					
				}
				List<Map<String,Object>> parentAreaList = this.sysAreaDao.getParentAreaListByAreaIds(areaIds.substring(1));//父区域列表
				if(parentAreaList!=null && !parentAreaList.isEmpty()){
					for(Map<String,Object> mp:parentAreaList){
						for(String key:areaMap.keySet()){
							String path = areaMap.get(key)+"";
							if(path!=null && !"".equals(path)){
								areaMap.put(key,path.replace("/"+mp.get("areaId")+"/", "/"+mp.get("name")+"/"));//替换path中id值为区域名
							}
						}
					}
				}
				String[] areaIdArray  = areaIds.substring(1).split(",");
				StringBuffer areaPathNameStr = new StringBuffer();
				for(String areaId:areaIdArray){
					String areaPathName="";
					if(areaMap.get(areaId)!=null){
						areaPathName=areaMap.get(areaId)+"";
						areaPathName = areaPathName.substring(1,areaPathName.length()-1);
					}
					areaPathNameStr.append(","+areaPathName);
				}
				map.put("areaId",areaIds.substring(1));
				map.put("areaName",areaPathNameStr.substring(1));
			}else{
				map.put("areaId","");
				map.put("areaName","");
			}
			
		
		log.info("执行getProviderOrgByOrgIdtoMapService方法成功，实现了”根据组织Id获取组织架构(组织架构详细信息,上级ID为中文)“的功能");
		log.info("退出getProviderOrgByOrgIdtoMapService方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 获取所有未选中的企业
	 * @return
	 */
	public List<Map<String,String>> getAllNoChoiceEnterpriseService(String orgType){
		log.info("进入getAllNoChoiceEnterpriseService方法");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//获取所有企业
		List<InformationEnterprise> allList = this.sysOrganizationDao.getNoHasOrgEnterpriseMessageByType(orgType);
		if(allList!=null && allList.size()>0){
			for (InformationEnterprise ie : allList) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("typeCode", ie.getId()+"");
				map.put("name", ie.getFullName());
				map.put("coo", ie.getCooperative());
				list.add(map);
			}
		}
		log.info("执行getAllNoChoiceEnterpriseService方法成功，实现了”获取企业的数据字典“的功能");
		log.info("退出getAllNoChoiceEnterpriseService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 保存组织
	 * @param orgJsonStr
	 */
	public void txSaveProviderOrgInfoAjaxService(String orgJsonStr){
		log.info("进入txSaveProviderOrgInfoAjaxService方法");
		log.info("参数orgJsonStr="+orgJsonStr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if(orgJsonStr==null || "".equals(orgJsonStr)){
			log.info("参数：orgJsonStr为空");
			String result = gson.toJson("failed");
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
			return;
		}
		Map<String,String> strJson = gson.fromJson(orgJsonStr, new TypeToken<Map<String,String>>(){}.getType());
		//保存组织
		SysOrg po = new SysOrg();
		long parentOrgId = 0;
		if(!"0".equals(strJson.get("parentOrgId"))){
			if(!"".equals(strJson.get("orgId"))&&!"null".equals(strJson.get("orgId"))){
				parentOrgId = Long.parseLong(strJson.get("orgId"));
			}
		}
		
		String name = strJson.get("name");
		po.setParentId(parentOrgId);
		if(!"".equals(strJson.get("type"))&&!"null".equals(strJson.get("type"))){
			//po.setOrgTypeId(Long.parseLong(strJson.get("type")));
			po.setOrgTypeId(0L);
			po.setOrgType(strJson.get("type"));
		}
		
		po.setName(name);
		//po.setEncampment(strJson.get("encampment"));
		po.setAddress(strJson.get("address"));
		po.setStatus(1);
		po.setDescription(strJson.get("orgDuty"));
		if(!"".equals(strJson.get("dutyPerson"))&&!"null".equals(strJson.get("dutyPerson"))){
			po.setOrgUserId(Long.parseLong(strJson.get("dutyPerson")));
		}
		po.setOrgUserTel(strJson.get("dutyPersonPhone"));
		po.setTel(strJson.get("contactPhone"));
		if(!"".equals(strJson.get("orgAttribute"))&&!"null".equals(strJson.get("orgAttribute"))){
			po.setBusinessTypeId(Long.parseLong(strJson.get("orgAttribute")));
		}

		if(!"".equals(strJson.get("enterpriseId"))&&!"null".equals(strJson.get("enterpriseId"))){
			po.setEnterpriseId(Long.parseLong(strJson.get("enterpriseId")));
		}
		po.setCreatetime(new Date());
		String longitude = strJson.get("longitude");
		if(longitude==null || "".equals(longitude) || this.checkStrIsEnglish(longitude)){
			log.info("经度不全是数字");
		}else{
			po.setLongitude(Double.parseDouble(longitude));
		}
		String latitude = strJson.get("latitude");
		if(latitude==null || "".equals(latitude) || this.checkStrIsEnglish(latitude)){
			log.info("纬度不全是数字");
		}else{
			po.setLatitude(Double.parseDouble(latitude));
		}
		Serializable idValue = this.sysOrganizationDao.saveEntity(po);
		if(idValue!=null){
			long id = Long.parseLong(idValue+"");
			SysOrg o = this.sysOrganizationDao.getOrgByOrgId(id);
			if(o!=null){
				boolean addPermissionFlag = false;//是否将部门添加为权限资源 yuan.yw 2014-01-20
				if("0".equals(parentOrgId+"")){
					o.setPath("/"+id+"/");
				}else{
					SysOrg p = this.sysOrganizationDao.getOrgByOrgId(parentOrgId);
					o.setPath(p.getPath()+id+"/");
					if(p.getParentId()==null){
						addPermissionFlag = true;
					}
				}
				this.sysOrganizationDao.updateEntity(o);
				if(addPermissionFlag){
					this.addPermissionByOrg(o);//添加部门为权限资源 yuan.yw 2014-01-20
				}
				if(!"".equals(strJson.get("areaId"))&&!"null".equals(strJson.get("areaId"))){
					String areaIds = strJson.get("areaId");
					String areaNames = strJson.get("areaName");
					String[] areaIdArr = areaIds.split(",");
					String[] areaNameArr = areaNames.split(",");
					int index = 0; 
					for(String areaId:areaIdArr){
						SysOrgRelaArea sa = new SysOrgRelaArea(); 
						sa.setAreaId(Long.parseLong(areaId));
						sa.setDescription("组织"+name+"与区域"+areaNameArr[index]+"关联");
						sa.setOrgId(id);
						this.sysOrganizationDao.saveEntity(sa);
						index++;
					}
					
				}
				
			}
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行txSaveProviderOrgInfoAjaxService方法成功，实现了”保存组织“的功能");
		log.info("退出txSaveProviderOrgInfoAjaxService方法,返回void");
	}
	/**
	 * 
	 * @description: 添加部门权限资源(本class类中使用)
	 * @author：yuan.yw
	 * @param org
	 * @return     
	 * @return long     
	 * @date：Jan 15, 2014 12:00:24 PM
	 */
	public  long addPermissionByOrg(SysOrg org){
		long orgPermissionId=0L;
		SysPermission permission = new SysPermission();
		permission.setProCode("PM");
		permission.setName(org.getName());
		permission.setCode("PM_Org"+org.getOrgId());
		permission.setType("PM_DataResource");
		permission.setServId(org.getOrgId());
		permission.setServType("PM_Org");
		permission.setParentId(0L);
		permission.setCreateTime(new Date());
		permission.setEnalbed(1);
		//添加部门权限资源
		Serializable s=this.commonDao.saveObject(permission);
		if(s!=null){
			orgPermissionId = Long.valueOf(s+"");
			permission=this.commonDao.getObjectById(SysPermission.class, orgPermissionId);
			if(permission!=null){//更新path路径
				permission.setPath("/"+orgPermissionId+"/");
				this.commonDao.updateObject(permission);
			}
		}
		return orgPermissionId;
	}
	/**
	 * 修改组织
	 * @param orgJsonStr
	 */
	public void txUpdateProviderOrgInfoAjaxService(String orgJsonStr){
		log.info("进入txUpdateProviderOrgInfoAjaxService方法");
		log.info("参数orgJsonStr="+orgJsonStr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if(orgJsonStr==null || "".equals(orgJsonStr)){
			log.info("参数：orgJsonStr为空");
			String result = gson.toJson("failed");
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				log.error("返回到jsp页面时出错");
				throw new UserDefinedException("返回到jsp页面时出错");
			}
			return;
		}
		Map<String,String> strJson = gson.fromJson(orgJsonStr, new TypeToken<Map<String,String>>(){}.getType());
		long orgId = 0;
		if(!"".equals(strJson.get("orgId"))&&!"null".equals(strJson.get("orgId"))){
			orgId = Long.parseLong(strJson.get("orgId"));
		}
		long parentOrgId = 0;
		SysOrg po = this.sysOrganizationDao.getOrgByOrgId(orgId);
		if(!"".equals(strJson.get("parentOrgId"))&&!"null".equals(strJson.get("parentOrgId"))){
			parentOrgId = Long.parseLong(strJson.get("parentOrgId"));
		}
		long parentId=0;
		if(po.getParentId()!=null){
			parentId = po.getParentId();
		}
		
		po.setOrgId(orgId);
		if(!"".equals(strJson.get("type"))&&!"null".equals(strJson.get("type"))){
			//po.setOrgTypeId(Long.parseLong(strJson.get("type")));
			po.setOrgTypeId(0L);
			po.setOrgType(strJson.get("type"));
		}
		
		String name = strJson.get("name");
		po.setName(name);
		//po.setEncampment(strJson.get("encampment"));
		po.setAddress(strJson.get("address"));
		po.setStatus(1);
		po.setDescription(strJson.get("orgDuty"));
		if(!"".equals(strJson.get("dutyPerson"))&&!"null".equals(strJson.get("dutyPerson"))){
			po.setOrgUserId(Long.parseLong(strJson.get("dutyPerson")));
		}
		po.setOrgUserTel(strJson.get("dutyPersonPhone"));
		po.setTel(strJson.get("contactPhone"));
		if(!"".equals(strJson.get("orgAttribute"))&&!"null".equals(strJson.get("orgAttribute"))){
			po.setBusinessTypeId(Long.parseLong(strJson.get("orgAttribute")));
		}

		if(!(parentId+"").equals(parentOrgId+"")){
			SysOrg p = this.sysOrganizationDao.getOrgByOrgId(parentOrgId);
			if(p!=null){
				po.setPath(p.getPath()+orgId+"/");
			}
			po.setParentId(parentOrgId);
		}
		String longitude = strJson.get("longitude");
		if(longitude==null || "".equals(longitude) || this.checkStrIsEnglish(longitude)){
			log.info("经度不全是数字");
		}else{
			po.setLongitude(Double.parseDouble(longitude));
		}
		String latitude = strJson.get("latitude");
		if(latitude==null || "".equals(latitude) || this.checkStrIsEnglish(latitude)){
			log.info("纬度不全是数字");
		}else{
			po.setLatitude(Double.parseDouble(latitude));
		}
		//根据组织ID修改服务商组织
		this.sysOrganizationDao.updateEntity(po);
		boolean flag = this.sysAreaRelaOrgDao.deleteSysOrgRelaAreaByOrgId(po.getOrgId());
		if(flag && !"".equals(strJson.get("areaId"))&&!"null".equals(strJson.get("areaId"))){
			String areaIds = strJson.get("areaId");
			String areaNames = strJson.get("areaName");
			String[] areaIdArr = areaIds.split(",");
			String[] areaNameArr = areaNames.split(",");
			int index = 0; 
			for(String areaId:areaIdArr){
				SysOrgRelaArea s = new SysOrgRelaArea(); 
				s.setAreaId(Long.parseLong(areaId));
				s.setDescription("组织"+name+"与区域"+areaNameArr[index]+"关联");
				s.setOrgId(po.getOrgId());
				this.sysOrganizationDao.saveEntity(s);
				index++;
			}
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行txUpdateProviderOrgInfoAjaxService方法成功，实现了”修改组织“的功能");
		log.info("退出txUpdateProviderOrgInfoAjaxService方法,返回void");
	}
	
	
	/**
	 * 删除组织
	 * @param orgId
	 */
	public void txDeleteProviderOrgInfoAjaxService(long orgId){
		log.info("进入txDeleteProviderOrgInfoAjaxService方法");
		log.info("参数orgId="+orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		SysOrg po = this.sysOrganizationDao.getOrgByOrgId(orgId);
		List<SysOrgRelaArea> saList = this.sysOrganizationDao.getSysOrgRelaAreaByorgId(orgId);
		List<SysUserRelaOrg> srList = this.sysOrganizationDao.getSysUserRelaOrgByorgId(orgId);
		//List<SysOrgAttributeExt> seList = this.sysOrganizationDao.getSysOrgAttributeExtByorgId(orgId);
		if(po!=null){
			this.sysOrganizationDao.deleteEntity(po);
			if(saList!=null && !saList.isEmpty()){
				for(SysOrgRelaArea sa:saList){
					this.sysOrganizationDao.deleteEntity(sa);
				}
			}
			if(srList!=null && !srList.isEmpty()){
				for(SysUserRelaOrg sr:srList){
					this.sysOrganizationDao.deleteEntity(sr);
				}
			}
		/*	if(seList!=null && !seList.isEmpty()){
				for(SysOrgAttributeExt se:seList){
					this.sysOrganizationDao.deleteEntity(se);
				}
			}*/
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行txDeleteProviderOrgInfoAjaxService方法成功，实现了”删除组织“的功能");
		log.info("退出txDeleteProviderOrgInfoAjaxService方法,返回void");
	}
	/**
	 * 根据账号获取可操作组织架构的权限
	 * @param account
	 */
	public void getAuthorityByAccountAjaxService(String account){
		log.info("进入getAuthorityByAccountAjaxService方法");
		log.info("参数account="+account);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		//根据账号获取其组织ids
		Set<String> set = new HashSet<String>();
		List<Map<String,Object>> OrgByAccount = this.sysOrganizationDao.getAuthorityOrgByAccount(account);
		if(OrgByAccount!=null && OrgByAccount.size()>0){
			for (Map<String,Object> po : OrgByAccount) {
				long orgId = Long.valueOf(po.get("orgId")+"");
				set.add(orgId+"");
				
			}
		}
		String result = gson.toJson(set);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getAuthorityByAccountAjaxService方法成功，实现了”根据账号获取可操作组织架构的权限“的功能");
		log.info("退出getAuthorityByAccountAjaxService方法,返回void");
	}
	
	/**
	 * 
	 * @description: 获取最高级组织
	 * @author：yuan.yw
	 * @param account     
	 * @return void     
	 * @date：Sep 26, 2013 11:16:45 AM
	 */
	public void getTheTopProviderOrgAjaxService(String account){
		log.info("进入getTheTopProviderOrgAjaxService方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,String> map = new HashMap<String, String>();
		//根据企业Id获取最高级的服务商组织
		List<Map<String,Object>> orgList = this.sysOrganizationDao.getTheTopOrgByAccount(account);
		if(orgList!=null && orgList.size()>0){
			Map<String,Object> po = orgList.get(0);
			if(po!=null){
				map.put("id", po.get("orgId")+"");
				map.put("treeNodeName", po.get("name")+"");
				map.put("referenceValue", po.get("orgId")+"");
			}
		}
		String result = gson.toJson(map);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		
		log.info("执行getAuthorityByAccountAjaxService方法成功，实现了”获取最高级组织树(当前登陆人)“的功能");
		log.info("退出getAuthorityByAccountAjaxService方法,返回void");
	}
	/**
	 * 根据人员Ajax获取最高组织
	 * @param account
	 */
	public void getTopLevelOrgByAccountAjaxService(String account){
		log.info("进入getOrgByAccountAndRoleCode方法");
		log.info("参数account="+account);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,Object>> topLevelOrgByAccount = this.sysOrganizationDao.getAssociateTopOrgByAccount(account);
		String result = gson.toJson(topLevelOrgByAccount);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getOrgByAccountAndRoleCode方法成功，实现了”根据用户名获取其最高级的组织的上级组织“的功能");
		log.info("退出getOrgByAccountAndRoleCode方法,返回void");
	}

	/**
	 * @authr:duhw
	 * @create_time:2013-05-11
	 * 根据组织标识得到当前组织的所有父组织
	 * @org_id：组织标识
	 * @return map
	 */
	public List<Map<String,Object>> getAllTheTopOrgByOrgIdService(long org_id){
		log.info("getAllTheTopOrgByOrgIdService(int org_type)，org_id="+org_id+",获取所有上级组织列表");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list =this.sysOrganizationDao.getAllTopOrgListByOrgId(org_id);//获取当前组织的所有上级组织
		log.info("退出getAllTheTopOrgByOrgIdService(int org_id)，返回结果list"+list);
		return list;
	}
	/**
	 * @author:duhw
	 * 根据用户标识获取所有组织架构树（包括上级组织和下级组织）
	 * 注：此树是完整的架构树，从根节点到最底层的节点
	 * @param org_user_id:用户标识
	 * @return List<Map>
	 */
	public List<Map<String, Object>> getAllTreeOrgByUserIdService(long org_user_id){
		log.info("getAllTreeOrgByUserIdService(long org_user_id)，org_user_id="+org_user_id+",获取当前用户");
		//获取当前组织的所有上下级组织
		List<Map<String,Object>> orgList = this.sysUserRelaOrgDao.getAllTreeOrgByOrgUserId(org_user_id);
		
		ArrayList<ArrayList<Map<String,Object>>> list = new ArrayList<ArrayList<Map<String,Object>>>();//存放节点列表
		ArrayList<Map<String,Object>>  mapList = new ArrayList<Map<String,Object>>();//存放不同级的单个节点
		for(int i=0;i<8;i++){//1定义最大数的级数，可以多，但是不可少(此处定义了8级)
			list.add(i, new ArrayList<Map<String,Object>>());
			mapList.add(i,new TreeMap<String,Object>());
		}
		
		//判断当前组织的级别
		int orgGrade = 0;//区分当前组织的级别
		if(orgList!=null && orgList.size() > 0){//存在子节点数据
			String path[] = ((String) orgList.get(0).get("PATH")).split("/");
			orgGrade = path.length-1;
			if(orgGrade < 1){//数据不合法
				return null;
			}
		}else{
			return null;
		}
		int currentLevel = 0;
		for(int i=0;i<orgList.size();i++){//重新组合组织树形结构
		   Map<String,Object> sysOrg = reBuild(orgList.get(i));//对一条区域信息map的key值重新组合
		   orgList.set(i, sysOrg);//重新赋值给组织列表
		   String path[] = ((String) orgList.get(i).get("path")).split("/");
		   int pathLength = path.length;
		   currentLevel = pathLength-2;//当前节点所在树的级别，0是最顶端
		   for(int j=list.size()-1;j>currentLevel;j--){
			    if(list.get(j) != null && list.get(j).size() > 0){
			    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
			    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
			    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
			    	list.set(j, new ArrayList<Map<String,Object>>());//当前节点已经加到上级节点中,删除当前子节点
			     }   
		   }
		   mapList.add(currentLevel,orgList.get(i));
		   list.get(currentLevel).add(orgList.get(i));
		}
		for(int j=list.size()-1;j>0;j--){
		    if(list.get(j) != null && list.get(j).size() > 0){
		    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
		    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
		    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
		     }   
	   }
		log.info("退出getAllTreeOrgByUserIdService(long org_user_id)，返回结果list"+list);
		return list.get(0);
		
	}
	/**
	 * ajax根据账号获取该账号以下的组织树
	 * @author yuan.yw
	 * @param account
	 */
	public void getOrgTreeDownwardByAccountAjaxService(String account){
		log.info("进入getOrgTreeDownwardByAccountAjaxService方法");
		log.info("参数account="+account);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result="";
		//获取当前账号组织的所有下级组织
		List<Map<String,Object>> orgList = this.sysUserRelaOrgDao.getAllTreeOrgByAccount(account);
		
		ArrayList<ArrayList<Map<String,Object>>> list = new ArrayList<ArrayList<Map<String,Object>>>();//存放节点列表
		ArrayList<Map<String,Object>>  mapList = new ArrayList<Map<String,Object>>();//存放不同级的单个节点
		for(int i=0;i<8;i++){//1定义最大数的级数，可以多，但是不可少(此处定义了8级)
			list.add(i, new ArrayList<Map<String,Object>>());
			mapList.add(i,new TreeMap<String,Object>());
		}
		
		//判断当前组织的级别
		int orgGrade = 0;//区分当前组织的级别
		if(orgList!=null && orgList.size() > 0){//存在子节点数据
			String path[] = ((String) orgList.get(0).get("PATH")).split("/");
			orgGrade = path.length-1;
			if(orgGrade < 1){//数据不合法
				result="";
			}
		}else{
			result="";
		}
		int currentLevel = 0;
		for(int i=0;i<orgList.size();i++){//重新组合组织树形结构
		   Map<String,Object> sysOrg = reBuild(orgList.get(i));//对一条区域信息map的key值重新组合
		   orgList.set(i, sysOrg);//重新赋值给组织列表
		   String path[] = ((String) orgList.get(i).get("path")).split("/");
		   int pathLength = path.length;
		   currentLevel = pathLength-2;//当前节点所在树的级别，0是最顶端
		   for(int j=list.size()-1;j>currentLevel;j--){
			    if(list.get(j) != null && list.get(j).size() > 0){
			    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
			    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
			    	list.get(j-1).get(lastList.size()-1).put("children", currentList);
			    	list.set(j, new ArrayList<Map<String,Object>>());//当前节点已经加到上级节点中,删除当前子节点
			     }   
		   }
		   mapList.add(currentLevel,orgList.get(i));
		   list.get(currentLevel).add(orgList.get(i));
		}
		for(int j=list.size()-1;j>0;j--){
		    if(list.get(j) != null && list.get(j).size() > 0){
		    	List<Map<String,Object>> lastList = list.get(j-1);//上级节点
		    	List<Map<String,Object>> currentList = list.get(j);//当前级节点
		    	if(lastList.size()-1>=0&&lastList.size()-1<list.get(j-1).size()){
		    		list.get(j-1).get(lastList.size()-1).put("children", currentList);
		    	}
		    	result = gson.toJson(list.get(j));
		     }   
	   }

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行getOrgTreeDownwardByAccountAjaxService方法成功，实现了”ajax根据账号获取该账号以下的组织树“的功能");
		log.info("退出getOrgTreeDownwardByAccountAjaxService方法,返回void");
	}
	
	/**
	 * 
	 * @description: 通过组织id获取人员列表
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrgUser>     
	 * @date：May 11, 2013 6:50:43 PM
	 */
	public List<SysOrgUser> getUserListByOrgIdService(long orgId){
		log.info("进入getUserListByOrgIdService(String orgId)方法");
		log.info("参数orgId="+orgId);
		List<SysOrgUser> su = this.sysOrganizationDao.getUserListByOrgId(orgId);
		log.info("执行getUserListByOrgIdService方法成功，实现了”通过组织id获取人员列表“的功能");
		log.info("退出getUserListByOrgIdService方法,返回List<SysOrgUser> su ="+su);
		return su;
	}
	
	
	/**
	 * 创建组织与人员的关系
	 */
	public void addOrgStaffAjaxService(long orgId, String orgUserId){
		log.info("进入addOrgStaffAjaxService(long orgId, String orgUserId)方法");
		log.info("参数orgUserId="+orgUserId);
		log.info("参数orgId="+orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if(!"".equals(orgUserId)){
			String[] orgUIds =  orgUserId.split(",");
			if(orgUIds!=null){
				for(String userId:orgUIds){
					SysUserRelaOrg so = new SysUserRelaOrg();
					so.setOrgId(orgId);
					so.setOrgUserId(Long.valueOf(userId));
					so.setCreatetime(new Date());
					so.setStatus("A");
					this.sysOrganizationDao.saveEntity(so);
				}
			}
			
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行addOrgStaffAjaxService(long orgId, String orgUserId)方法成功，实现了”创建组织与人员的关系“的功能");
		log.info("退出addOrgStaffAjaxService(long orgId, String orgUserId)方法,返回void");
	}
	
	/**
	 * 创建组织与人员的关系
	 */
	public void addOrgStaffService(long orgId, String orgUserId){
		log.info("进入addOrgStaffAjaxService(long orgId, String orgUserId)方法");
		log.info("参数orgUserId="+orgUserId);
		log.info("参数orgId="+orgId);
		if(!"".equals(orgUserId)){
			String[] orgUIds =  orgUserId.split(",");
			if(orgUIds!=null){
				for(String userId:orgUIds){
					SysUserRelaOrg so = new SysUserRelaOrg();
					so.setOrgId(orgId);
					so.setOrgUserId(Long.valueOf(userId));
					so.setCreatetime(new Date());
					this.sysOrganizationDao.saveEntity(so);
				}
			}
			
		}
		log.info("执行addOrgStaffAjaxService(long orgId, String orgUserId)方法成功，实现了”创建组织与人员的关系“的功能");
		log.info("退出addOrgStaffAjaxService(long orgId, String orgUserId)方法,返回void");
	}
	
	
	/**
	 * ajax根据人员ID删除改账号与组织的关系
	 */
	public void txDeleteOrgStaffAjaxService(String orgUserId,long orgId){
		log.info("进入txDeleteOrgStaffAjaxService(String orgUserId,long orgId)方法");
		log.info("参数orgId="+orgId);
		log.info("参数orgUserId="+orgUserId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<SysUserRelaOrg> srList = this.sysUserRelaOrgDao.getSysUserRelaOrgListByOrgUserIdsAndOrgId(orgId, orgUserId);
		if(srList!=null){
			for(SysUserRelaOrg sr:srList){
				this.sysOrganizationDao.deleteEntity(sr);
			}
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回到jsp页面时出错");
			throw new UserDefinedException("返回到jsp页面时出错");
		}
		log.info("执行txDeleteOrgStaffAjaxService(String orgUserId,long orgId)方法成功，实现了”ajax根据orgUserId删除改人员与组织的关系“的功能");
		log.info("退出txDeleteOrgStaffAjaxService(String orgUserId,long orgId)方法,返回void");
	}
	
	/**
	 * 
	 * @description: 根据组织Id向下获取项目
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,String>>     
	 * @date：May 27, 2013 11:04:29 AM
	 */
	public List<Map<String, String>> getProjectToDownwardOrgByOrgId(long orgId){
		log.info("进入getProjectToDownwardOrgByOrgId方法");
		log.info("参数orgId="+orgId);
		List<Map<String,String>> list = null;
		List<Map<String,Object>> downwardOrg = this.sysOrganizationDao.getDownwardOrgListByOrgId(orgId);//获取本身及下级组织列表
		StringBuffer sf = new StringBuffer();
		String orgIds = "";
		if(downwardOrg!=null && downwardOrg.size()>0){
			for (Map<String,Object> co : downwardOrg) {
				if(co.get("orgId")!=null){
					sf.append(","+co.get("orgId"));
				}
			}
			orgIds = sf.substring(1)+"";
			List<Map<String, String>> findProjectByOrgId = projectInformationDao.findProjectListByOrgIds(orgIds);//根据组织id 字符串获取项目列表
			if(findProjectByOrgId!=null && findProjectByOrgId.size()>0){
				list = findProjectByOrgId;
			}	
		}
		if(!(list!=null && list.size()>0)){
			list = null;
		}
		log.info("执行getProjectToDownwardOrgByOrgId方法成功，实现了”根据组织Id向下获取项目“的功能");
		log.info("退出getProjectToDownwardOrgByOrgId方法,返回List<Map<String, String>>集合");
		return list;
	}
	
	
	/**
	 * 
	 * @description: 获取最高级组织 当前登录人
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return Map<String,String>     
	 * @date：May 27, 2013 11:50:16 AM
	 */
	public Map<String,String> getTopOrgService(String account){
		log.info("进入getTopOrgService方法");
		Map<String,String> map = null;
		List<Map<String,Object>> resultList =  this.sysOrganizationDao.getTheTopOrgByAccount(account);
		if(resultList!=null && !resultList.isEmpty()){
			Map<String,Object> orgMp = resultList.get(0);
			map = new HashMap<String, String>();
			map.put("id", orgMp.get("orgId")+"");
			map.put("name", orgMp.get("name")+"");	
		}
		log.info("执行getTopOrgService方法成功，实现了”获取最高级组织（默认当前登录人）“的功能");
		log.info("退出getTopOrgService方法,返回Map<String,String>集合");
		return map;
	}
	
	/**
	 * 
	 * @description: 根据项目Id获取该项目所属的组织
	 * @author：yuan.yw
	 * @param projectId 项目id
	 * @return     
	 * @return List<Map<String,String>>     
	 * @date：May 27, 2013 2:02:43 PM
	 */
	public List<Map<String,String>> getOrgByProjectIdService(String projectId){
		log.info("进入getOrgByProjectIdService方法");
		log.info("参数projectId="+projectId);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//项目资源相关列表
		List<Map<String, String>> projectResList = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType( projectId , null, null);
		List<String> orgIds = null;
		if(projectResList!=null && projectResList.size()>0){
			orgIds = new ArrayList<String>();
			for (Map<String, String> map : projectResList) {
				if(map.get("orgId")!=null){
					orgIds.add(""+map.get("orgId"));
				}
			}
			List<SysOrg> orgList = this.sysOrganizationDao.getSysOrgListByOrgIds(orgIds);//获取组织列表
			if(orgList!=null && !orgList.isEmpty()){
				for(SysOrg o:orgList){
					Map<String,String> map = new HashMap<String, String>();
					map.put("id", o.getOrgId()+"");
					map.put("name", o.getName());
					list.add(map);
				}
			}
		}
		log.info("执行getOrgByProjectIdService方法成功，实现了”根据项目Id获取该项目所属的组织“的功能");
		log.info("退出getOrgByProjectIdService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 
	 * @description: 根据组织Id获取上一级或下一级的组织
	 * @author：yuan.yw
	 * @param orgId 组织id
	 * @param level 级别  child 下一级 parent 上一级
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 27, 2013 3:02:07 PM
	 */
	public List<SysOrg> getUpOrNextLevelOrgByOrgIdService(long orgId,String level){
		log.info("进入getUpOrNextLevelOrgByOrgIdService方法");
		log.info("参数orgId="+orgId);
		log.info("参数level="+level);
		List<SysOrg> list = this.sysOrganizationDao.getUpOrNextLevelOrgListByOrgIdAndLevleType(orgId, level);
		log.info("执行getUpOrNextLevelOrgByOrgIdService方法成功，实现了”根据组织Id获取上一级或下一级的组织“的功能");
		log.info("退出getUpOrNextLevelOrgByOrgIdService方法,返回List<SysOrg>集合");
		return list;
	}
	
	/**
	 * 
	 * @description:根据所在组织架构和组织类型 级别类型 向下或向上 获取组织架构列表
	 * @author：yuan.yw
	 * @param orgId
	 * @param typeCode
	 * @param levelType  child 向下 parent 向上
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 27, 2013 4:04:37 PM
	 */
	public List<Map<String,Object>> getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(
			long orgId, String typeCode,String levelType){
		log.info("进入getOrgListDownwardOrUpwardByOrgTypeAndOrg方法");
		log.info("参数orgId="+orgId+",typeCode="+typeCode+",levelType="+levelType);
		List<Map<String,Object>> result = this.sysOrganizationDao.getOrgListDownwardOrUpwardByOrgTypeAndOrgId(orgId+"", typeCode, levelType);
		log.info("执行getOrgListDownwardOrUpwardByOrgTypeAndOrgId方法成功，实现了”根据所在组织架构和组织类型 级别类型 向下或向上 获取组织架构列表“的功能");
		log.info("退出getOrgListDownwardOrUpwardByOrgTypeAndOrgId方法,返回List<SysOrg>集合");
		return result;
		
	}
	
	/**
	 * 
	 * @description: 根据组织向下获取所有下属组织架构 SysOrg
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 10:04:55 AM
	 */
	public List<SysOrg> getOrgListDownwardByOrg(long orgId){
		log.info("进入getOrgListDownwardByOrg方法");
		log.info("参数orgId="+orgId);
		List<Map<String,Object>> resultList = this.sysOrganizationDao.getDownwardOrgListByOrgId(orgId);
		List<SysOrg> result = null;
		if(resultList!=null && !resultList.isEmpty()){
			result = new ArrayList<SysOrg>();
			for(Map<String,Object> map : resultList){
				SysOrg so = this.reBuildMapToOrg(map);
				result.add(so);
			}
		}
		log.info("执行getOrgListDownwardByOrg方法成功，实现了”根据组织向下获取所有下属组织架构“的功能");
		log.info("退出getOrgListDownwardByOrg方法,返回List<SysOrg>集合");
		return result;
	}
	/**
	 * 根据组织向下获取所有下属组织架构 Map
	 * @description: TODO
	 * @author：yuan.yw
	 * @param orgId
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 28, 2013 10:04:55 AM
	 */
	public List<Map<String,Object>> getOrgListMapDownwardByOrg(long orgId){
		log.info("进入getOrgListMapDownwardByOrg方法");
		log.info("参数orgId="+orgId);
		List<Map<String,Object>> result = this.sysOrganizationDao.getDownwardOrgListByOrgId(orgId);
		log.info("执行getOrgListMapDownwardByOrg方法成功，实现了”根据组织向下获取所有下属组织架构 map“的功能");
		log.info("退出getOrgListMapDownwardByOrg方法,返回List<Map<String,Object>集合");
		return result;
	}
	
	/**
	 * 
	 * @description: 根据组织Id和组织属性获取下一级的所属组织
	 * @author：yuan.yw
	 * @param orgId
	 * @param orgAttribute
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 28, 2013 1:09:45 PM
	 */
	public List<Map<String,Object>> getProviderOrgNextByOrgIdAndOrgAttrService(long orgId,String orgAttribute){
		log.info("进入getProviderOrgNextByOrgIdAndOrgAttrService方法");
		log.info("参数：orgId="+orgId+",orgAttribute="+orgAttribute);
		List<Map<String,Object>> list = this.sysOrganizationDao.getUpOrNextLevelOrgListByOrgIdAndLevleTypeAndOrgAttribute(orgId, "child", orgAttribute);
		log.info("执行getProviderOrgNextByOrgIdAndOrgAttrService方法成功，实现了”根据组织Id和组织属性获取下一级的所属组织“的功能");
		log.info("退出getProviderOrgNextByOrgIdAndOrgAttrService方法,返回List<ProviderOrganization>集合");
		return list;
	}
	
	/**
	 * 
	 * @description: 根据组织向上获取所有上级组织架构
	 * @author：
	 * @param orgId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 2:20:08 PM
	 */
	public List<SysOrg> getOrgListUpwardByOrg(long orgId) {
		log.info("进入getOrgListUpwardByOrg方法");
		log.info("参数orgId="+orgId);
		List<Map<String,Object>> resultList = this.sysOrganizationDao.getAllTopOrgListByOrgId(orgId);
		List<SysOrg> result = null;
		if(resultList!=null && !resultList.isEmpty()){
			result = new ArrayList<SysOrg>();
			for(Map<String,Object> map : resultList){
				SysOrg so = this.reBuildMapToOrg(map);
				result.add(so);
			}
		}
		log.info("执行getOrgListUpwardByOrg方法成功，实现了”根据组织向上获取所有上级组织架构“的功能");
		log.info("退出getOrgListUpwardByOrg方法,返回List<SysOrg>集合");
		return result;
	}
	
	/**
	 * 
	 * @description: 根据组织类型和人员获取组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @param typeCode
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:16:37 PM
	 */
	public List<SysOrg> getOrgByAccountAndOrgType(
			String accountId, String typeCode){
		log.info("进入getOrgByAccountAndOrgType方法");
		log.info("参数accountId="+accountId+",typeCode="+typeCode);
		List<Map<String,Object>> orgList = this.sysOrganizationDao.getOrgListByAccountAndOrgType(accountId,typeCode); 
		List<SysOrg> result = null;
		if(orgList!=null && !orgList.isEmpty()){
			result = new ArrayList<SysOrg>();
			for(Map<String,Object> mp:orgList){
				SysOrg so = this.reBuildMapToOrg(mp);
				result.add(so);
			}
		}
		log.info("执行getOrgByAccountAndOrgType方法成功，实现了”根据组织类型和人员获取组织架构“的功能");
		log.info("退出getOrgByAccountAndOrgType方法,返回List<SysOrg>集合");
		return result;
	}
	
	/**
	 * 
	 * @description: 根据人员获取其所属关联组织最高级组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:58:30 PM
	 */
	public List<SysOrg> getTopLevelOrgByAccount(String accountId) {
		log.info("进入getTopLevelOrgByAccount方法");
		log.info("参数accountId="+accountId);
		List<Map<String,Object>> resultListMap = this.sysOrganizationDao.getAssociateTopOrgByAccount(accountId);
		List<SysOrg> result = new ArrayList<SysOrg>();
		if(resultListMap!=null && !resultListMap.isEmpty()){
			result = new ArrayList<SysOrg>();
			for(Map<String,Object> mp:resultListMap){
				SysOrg so = this.reBuildMapToOrg(mp);
				result.add(so);
			}
		}
		log.info("执行getTopLevelOrgByAccount方法成功，实现了”根据人员获取其所属最高级组织架构“的功能");
		log.info("退出getTopLevelOrgByAccount方法,返回List<S>集合");
		return result;
	}
	
	
	/**
	 * 
	 * @description: 根据人员获取其所属关联组织最高级组织架构
	 * @author：yuan.yw
	 * @param accountId
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 28, 2013 3:58:30 PM
	 */
	public List<SysOrg> getTopLevelOrgByOrgUserId(long orgUserId) {
		log.info("进入getTopLevelOrgByOrgUserId方法");
		log.info("参数orgUserId="+orgUserId);
		List<Map<String,Object>> resultListMap = this.sysOrganizationDao.getAssociateTopOrgByOrgUserId(orgUserId);
		List<SysOrg> result = new ArrayList<SysOrg>();
		if(resultListMap!=null && !resultListMap.isEmpty()){
			result = new ArrayList<SysOrg>();
			for(Map<String,Object> mp:resultListMap){
				SysOrg so = this.reBuildMapToOrg(mp);
				result.add(so);
			}
		}
		log.info("执行getTopLevelOrgByOrgUserId方法成功，实现了”根据人员获取其所属最高级组织架构“的功能");
		log.info("退出getTopLevelOrgByOrgUserId方法,返回List<S>集合");
		return result;
	}
	
	/**
	 * 
	 * @description: 根据组织id 角色类型 级别参数(组织向上、向下) 获取人员列表
	 * @author：yuan.yw
	 * @param orgId 组织id
	 * @param roleCode 角色类型
	 * @param  level  upward 向上 downward 向下 all 向上及向下  downOrUpward 向下或向上
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 29, 2013 10:52:10 AM
	 */
	public List<Map<String,Object>> getProviderStaffByOrgIdAndRoleCode(long orgId,String roleCode,String level){
		log.info("进入getProviderStaffByOrgIdAndRoleCode方法");
		log.info("参数orgId="+orgId+",roleCode="+roleCode);
		List<Map<String,Object>> list =this.sysOrganizationDao.getProviderStaffByOrgIdAndRoleCode(orgId, roleCode, level);
		log.info("执行getProviderStaffByOrgIdAndRoleCode方法成功，实现了”根据组织id 角色类型 级别参数(组织向上、向下) 获取人员列表“的功能");
		log.info("退出getProviderStaffByOrgIdAndRoleCode方法,返回List<Staff>集合");
		return list;
	}
	/**
	 * 
	 * @description: 根据角色类型和账号获取组织架构
	 * @author：yuan.yw
	 * @param account
	 * @param roleCode
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：May 29, 2013 2:49:49 PM
	 */
	public List<Map<String,Object>> getOrgByAccountAndRoleCode(
			 String account,String roleCode){
		log.info("进入getOrgByAccountAndRoleCode方法");
		log.info("参数account="+account+",roleCode="+roleCode);
		List<Map<String,Object>> result = this.sysOrganizationDao.getOrgByAccountAndRoleCode(account, roleCode);
		log.info("执行getOrgByAccountAndRoleCode方法成功，实现了”根据角色类型和账号获取组织架构“的功能");
		log.info("退出getOrgByAccountAndRoleCode方法,返回List<ProviderOrganization>集合");
		return result;
		
	}
	/**
	 * 
	 * @description: 根据账号获取关联组织
	 * @author：yuan.yw
	 * @param account
	 * @return     
	 * @return List<SysOrg>     
	 * @date：May 29, 2013 3:32:06 PM
	 */
	public List<SysOrg> getOrgByAccountService(String account){
		log.info("进入getOrgByAccountService方法");
		log.info("参数account="+account);
		List<SysOrg> list = this.sysOrganizationDao.getOrgByAccount(account);
		log.info("执行getOrgByAccountService方法成功，实现了”根据人员获取组织“的功能");
		log.info("退出getOrgByAccountService方法,返回List<ProviderOrganization>集合");
		return list;
	}
	/**
	 * 
	 * @description: 根据组织Id和组织属性 级别（上一级 下一级 向上 向下） 获取相关组织
	 * @author：yuan.yw
	 * @param orgId  组织id
	 * @param orgAttribute 组织属性
	 * @param level 级别  up 上一级 next 下一级 upward 向上 downward 向下
	 * @return     
	 * @return List<Map<String,Object>     
	 * @date：May 29, 2013 4:15:48 PM
	 */
	public List<Map<String,Object>> getProviderOrgByOrgIdAndOrgAttrByLevelService(long orgId,String orgAttribute,String level){
		log.info("进入getProviderOrgByOrgIdAndOrgAttrByLevelService方法");
		log.info("参数：orgId="+orgId+",orgAttribute="+orgAttribute+",level="+level);
		List<Map<String,Object>> list = this.sysOrganizationDao.getProviderOrgByOrgIdAndOrgAttrByLevel(orgId, orgAttribute, level);
		log.info("执行getProviderOrgByOrgIdAndOrgAttrByLevelService方法成功，实现了”根据组织Id和组织属性 级别（上一级 下一级 向上 向下） 获取相关组织“的功能");
		log.info("退出getProviderOrgByOrgIdAndOrgAttrByLevelService方法,返回List<Map<String,Object>集合");
		return list;
	}
	
	/**
	 * 
	 * @description: 所有组织Map
	 * @author：yuan.yw
	 * @return     
	 * @return Map<String,SysOrg>     
	 * @date：May 30, 2013 11:24:01 AM
	 */
	public Map<String,SysOrg> getAllOrgToMap(){
		log.info("进入getAllOrgToMap()方法");
		Map<String,SysOrg> map = new HashMap<String, SysOrg>();
		//获取所有可用的服务商组织
		List<SysOrg> allProviderOrg = this.sysOrganizationDao.getAllProviderOrg();
		if(allProviderOrg!=null && !allProviderOrg.isEmpty()){
			for (SysOrg po : allProviderOrg) {
				map.put(po.getOrgId()+"", po);
			}
		}
		log.info("退出getAllOrgToMap()方法，返回"+map);
		return map;
	}
	/***
	 * 
	 * @description: 根据组织ids获取组织树
	 * @author：yuan.yw
	 * @param orgIds
	 * @return     
	 * @return List<Map>     
	 * @date：May 30, 2013 11:52:51 AM
	 */
	public List<Map<String,Object>> getOrgTreeDownwardByorgIds(String orgIds){
		log.info("进入getOrgTreeDownwardByorgIds(String orgIds)方法,orgIds="+orgIds);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(orgIds!=null && !"".equals(orgIds)){
			String[] array = orgIds.split(",");
			if(array!=null&&array[0]!=""){
				List<Map<String,Object>> orgList = this.sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(orgIds);
				if(orgList!=null && !orgList.isEmpty()){
					result = TreeListHelper.getTreeListByDataList(orgList);
					/*for(String orgId:array){
						Map poService = getOrgTreeDownwardMapByOrgList(orgList,orgId);
						if(poService!=null && poService.size()>0){
							result.add(poService);
						}
					}*/
				}				
			}
		}
		
		log.info("执行getOrgTreeDownwardByorgIds(String orgIds)方法成功，实现了”根据组织Ids，类型获取组织树“的功能");
		log.info("退出getOrgTreeDownwardByorgIds(String orgIds)方法,返回List<Map>");
		return result;
	}
	
	//判断该字符串是否为英文字母
	private boolean checkStrIsEnglish(String str){
		log.info("进入checkStrIsEnglish方法");
		log.info("参数：str="+str);
		boolean result = str.matches(".*[a-zA-Z]+.*");
		log.info("执行checkStrIsEnglish方法成功，实现了”判断该字符串是否为英文字母“的功能");
		log.info("退出checkStrIsEnglish方法,返回boolean="+result);
		return result;
	}
	
	//判断字符串空值时返回出来的形式
	private String chickStr(String str){
		if(str==null || "null".equals(str) || "undefined".equals(str)){
			str = "";
		}
		return str;
	}
	
	//GETTER AND SETTER
	
	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public EnterpriseInformationDao getEnterpriseInformationDao() {
		return enterpriseInformationDao;
	}

	public void setEnterpriseInformationDao(
			EnterpriseInformationDao enterpriseInformationDao) {
		this.enterpriseInformationDao = enterpriseInformationDao;
	}


	public SysUserRelaOrgDao getSysUserRelaOrgDao() {
		return sysUserRelaOrgDao;
	}

	

	public void setSysUserRelaOrgDao(SysUserRelaOrgDao sysUserRelaOrgDao) {
		this.sysUserRelaOrgDao = sysUserRelaOrgDao;
	}



	
	
	
	public SysOrgUserDao getSysOrgUserDao() {
		return sysOrgUserDao;
	}

	public void setSysOrgUserDao(SysOrgUserDao sysOrgUserDao) {
		this.sysOrgUserDao = sysOrgUserDao;
	}

	public ProjectInformationDao getProjectInformationDao() {
		return projectInformationDao;
	}

	public void setProjectInformationDao(ProjectInformationDao projectInformationDao) {
		this.projectInformationDao = projectInformationDao;
	}
	
	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}

	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}
	
	
	
	public SysAreaDao getSysAreaDao() {
		return sysAreaDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}
	
	public SysAreaRelaOrgDao getSysAreaRelaOrgDao() {
		return sysAreaRelaOrgDao;
	}

	public void setSysAreaRelaOrgDao(SysAreaRelaOrgDao sysAreaRelaOrgDao) {
		this.sysAreaRelaOrgDao = sysAreaRelaOrgDao;
	}

	/**
	   * @author:duhw
	   * @create_time:2013-05-11
	   * 转换map的key与SysDao中的定义名称一样
	   * map：需要转换的map
	   * @return map
	   */
		public Map<String,Object> reBuild(Map map){
			Map<String,Object> returnMap = new TreeMap<String,Object>();
			returnMap.put("orgId", map.get("ORG_ID"));
			returnMap.put("name", map.get("NAME"));
			returnMap.put("tel", map.get("TEL"));
			returnMap.put("mobile", map.get("MOBILE"));
			returnMap.put("email", map.get("EMAIL"));
			returnMap.put("address", map.get("ADDRESS"));
			returnMap.put("description", map.get("DESCRIPTION"));
			returnMap.put("parentId", map.get("PARENT_ID"));
			returnMap.put("path", map.get("PATH"));
			returnMap.put("orgTypeId", map.get("ORG_TYPE_ID"));
			returnMap.put("businessTypeId", map.get("BUSINESS_TYPE_ID"));
			returnMap.put("businessTypeId", map.get("BUSINESS_TYPE_ID"));
			returnMap.put("status", map.get("STATUS"));
			returnMap.put("orgUserId", map.get("ORG_USER_ID"));
			returnMap.put("orgUserTel", map.get("ORG_USER_TEL"));
			returnMap.put("status", map.get("STATUS"));
			returnMap.put("longitude", map.get("LONGITUDE"));
			returnMap.put("latitude", map.get("LATITUDE"));
			returnMap.put("enterpriseId", map.get("ENTERPRISE_ID"));
			return returnMap;
		}
		/**
		   * @author:yuan.yw
		   * @create_time:2013-05-27
		   * 转换maptoSysOrg
		   * map：需要转换的map
		   * @return SysOrg
		   */
			public SysOrg reBuildMapToOrg(Map map){
				SysOrg so = new SysOrg();
				if(map.get("orgId")!=null){
					so.setOrgId(Long.valueOf(map.get("orgId")+""));	
				}
				if(map.get("name")!=null){
					so.setName(map.get("name")+"");
				}
				if(map.get("tel")!=null){
					so.setTel(map.get("tel")+"");
									
				}
				if(map.get("mobile")!=null){
					so.setMobile(map.get("mobile")+"");
				}
				if( map.get("email")!=null){
					so.setEmail(map.get("email")+"");
					
				}
				if(map.get("address")!=null){
					so.setAddress(map.get("address")+"");
				}
				if(map.get("description")!=null){
					so.setDescription(map.get("description")+"");
				}
				if( map.get("parentId")!=null){
					so.setParentId(Long.valueOf(map.get("parentId")+""));
				}
				if(map.get("path")!=null){
					so.setPath(map.get("path")+"");
				}
				if(map.get("orgTypeId")!=null){
					so.setOrgTypeId(Long.valueOf(map.get("orgTypeId")+""));
				}
				if(map.get("businessTypeId")!=null){
					so.setBusinessTypeId(Long.valueOf(map.get("businessTypeId")+""));
				}
				if(map.get("status")!=null){
					so.setStatus(Integer.valueOf(map.get("status")+""));
				}
				if(map.get("orgUserId")!=null){
					so.setOrgUserId(Long.valueOf(map.get("orgUserId")+""));
				}
				if(map.get("orgUserTel")!=null){
					so.setOrgUserTel(map.get("ORG_USER_TEL")+"");
				}
				if(map.get("longitude")!=null){
					so.setLongitude(Double.valueOf(map.get("longitude")+""));
				}
				if(map.get("latitude")!=null){
					so.setLatitude(Double.valueOf(map.get("latitude")+""));	
				}
				if(map.get("enterpriseId")!=null){
					so.setEnterpriseId(Long.valueOf(map.get("enterpriseId")+""));
					
				}
				return so;
			}
			
			public List<Map<String,Object>> getUserIdByRoleCode(
					String code) {
				return this.sysOrganizationDao.getUserIdByRoleCode(code) ;
			}
			
			
			

			public List<Map<String,Object>> getUserIdByRoleCode(long orgId,String code){
				return this.sysOrganizationDao.getUserIdByRoleCode(orgId, code);
			}
			
			
			
			public List<Map<String,Object>> getUserIdByPostCode(long orgId,String code){
				return this.sysOrganizationDao.getUserIdByPostCode(orgId, code);
			}
			
			
			/**
			 * 获取iscreate公司于事业部列表
			* @author ou.jh
			* @date Jan 9, 2014 2:06:30 PM
			* @Description: TODO 
			* @param @return        
			* @throws
			 */
			public List<Map<String, Object>> getIscreateCompanyAndBusinessUnitList(){
				return this.sysOrganizationDao.getIscreateCompanyAndBusinessUnitList();
			}
			/**
			 * 
			 * @description: 获取iscreate公司信息
			 * @author：yuan.yw
			 * @return     
			 * @return Map<String,Object>     
			 * @date：Jan 10, 2014 9:51:20 AM
			 */
			public Map<String,Object> getIscreateCompanyMap(){
				return this.sysOrganizationDao.getIscreateCompanyMap();
			}

			public CommonDao getCommonDao() {
				return commonDao;
			}

			public void setCommonDao(CommonDao commonDao) {
				this.commonDao = commonDao;
			}

			public SysDictionaryDao getSysDictionaryDao() {
				return sysDictionaryDao;
			}

			public void setSysDictionaryDao(SysDictionaryDao sysDictionaryDao) {
				this.sysDictionaryDao = sysDictionaryDao;
			}
			
}
