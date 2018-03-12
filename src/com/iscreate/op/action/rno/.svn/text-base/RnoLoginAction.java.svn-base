package com.iscreate.op.action.rno;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.dom4j.DocumentException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.google.gson.Gson;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Point;
import com.iscreate.op.dao.system.SysPermissionDao;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysPermissionService;
import com.iscreate.op.service.system.SysSuperAdminService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.plat.login.action.LoginAction;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;
import com.iscreate.plat.tools.xmlhelper.XmlService;
import com.iscreate.sso.session.UserInfo;

public class RnoLoginAction extends LoginAction {

	private static Log log = LogFactory.getLog(RnoLoginAction.class);

	private SysOrgUser sysOrgUser;

	private SysOrgUserService sysOrgUserService;
	
	private XmlService xmlService;

	private SysPermissionDao sysPermissionDao;

	private List<Map<String, Object>> permissionModuleList;

	private List<Map<String, Object>> menuLinks;
	
	private List<Map<String, Object>> menuList;
	


	private String json;

	private boolean flag;
	//区域
	private List<Area> provinceAreas;
	private List<Area> cityAreas;
	private List<Area> countryAreas;
	private Point centerPoint;// 中心点
	private String areaName;
	// --------注入--------------------//
	protected RnoCommonService rnoCommonService;
	
	public List<Area> getProvinceAreas() {
		return provinceAreas;
	}

	public void setProvinceAreas(List<Area> provinceAreas) {
		this.provinceAreas = provinceAreas;
	}

	public List<Area> getCityAreas() {
		return cityAreas;
	}

	public void setCityAreas(List<Area> cityAreas) {
		this.cityAreas = cityAreas;
	}

	public List<Area> getCountryAreas() {
		return countryAreas;
	}

	public void setCountryAreas(List<Area> countryAreas) {
		this.countryAreas = countryAreas;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public RnoCommonService getRnoCommonService() {
		return rnoCommonService;
	}

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	public List<Map<String, Object>> getMenuList()
	{
		return menuList;
	}
	
	public void setMenuList(List<Map<String, Object>> menuList)
	{
		this.menuList = menuList;
	}
	public SysOrgUser getSysOrgUser() {
		return sysOrgUser;
	}

	public void setSysOrgUser(SysOrgUser sysOrgUser) {
		this.sysOrgUser = sysOrgUser;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysPermissionDao getSysPermissionDao() {
		return sysPermissionDao;
	}

	public void setSysPermissionDao(SysPermissionDao sysPermissionDao) {
		this.sysPermissionDao = sysPermissionDao;
	}

	public List<Map<String, Object>> getPermissionModuleList() {
		return permissionModuleList;
	}

	public void setPermissionModuleList(
			List<Map<String, Object>> permissionModuleList) {
		this.permissionModuleList = permissionModuleList;
	}

	public List<Map<String, Object>> getMenuLinks() {
		return menuLinks;
	}

	public void setMenuLinks(List<Map<String, Object>> menuLinks) {
		this.menuLinks = menuLinks;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
	public SysSuperAdminService getSysSuperAdminService() {
		return super.getSysSuperAdminService();
	}

	public void setSysSuperAdminService(SysSuperAdminService sysSuperAdminService) {
		super.setSysSuperAdminService( sysSuperAdminService);
		
	}

	public String getGoingToURL() {
		return super.getGoingToURL();
	}

	public void setGoingToURL(String goingToURL) {
		super.setGoingToURL(goingToURL);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		super.setHibernateTemplate(hibernateTemplate);
	}


	public void setSysAccountService(SysAccountService sysAccountService) {
		super.setSysAccountService(sysAccountService);
	}

	public SysPermissionService getSysPermissionService() {
		return super.getSysPermissionService();
	}

	public void setSysPermissionService(SysPermissionService sysPermissionService) {
		super.setSysPermissionService(sysPermissionService);
	}

	public SysAccountService getSysAccountService() {
		return super.getSysAccountService();
	}
	
	

	public XmlService getXmlService()
	{
		return xmlService;
	}

	public void setXmlService(XmlService xmlService)
	{
		this.xmlService = xmlService;
	}

	/**
	 * 登录
	 * @return
	 * @author brightming
	 * 2013-12-11 下午6:49:52
	 */
	public String rnoUserLogin(){
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		super.prepareLoginPageParams();
		HttpServletRequest request = ServletActionContext.getRequest();
		String version = null;
		String projectName = null;
		try
		{
			version = xmlService.getSingleElementValue("sysconfig.xml", "SystemConfig/version");
			projectName = xmlService.getSingleElementValue("sysconfig.xml", "SystemConfig/name");
			//log.info("版本："+projectName+" "+version);
		} catch (DocumentException e)
		{
			log.error("获取版本信息失败");
		}
		request.getSession().setAttribute("version", version);
		request.getSession().setAttribute("projectName", projectName);
		return "success";
	}
	
	public String rnoAuthenticate(){
		String result=super.authenticate();
		if("success".equals(result)){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			String jumpUrl = request.getParameter("jump");
			//log.info("The goingToURL is " + jumpUrl);
			// 执行用户没登录系统前请求的URL回显
			if (jumpUrl != null && !"".equals(jumpUrl)
					&& !"null".equalsIgnoreCase(jumpUrl)) {
				jumpUrl = URLDecoder.decode(jumpUrl);
				setGoingToURL(jumpUrl);
			} else {
				// 没有jump, 跳至门户首页
				setGoingToURL("/op/rno/rnoUserIndexAction.action");
			}
		}
		return result;
	}
	public String rnoUserIndexAction() {
		log.info("进入rnoUserIndexAction方法");
		// 从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERID);
		long orgUserId = Long.parseLong(request.getSession().getAttribute(
				com.iscreate.plat.login.constant.UserInfo.ORG_USER_ID).toString());
		
		//通过账号获取用户可以查询的区域
		String account = (String) SessionService.getInstance().getValueByKey(
					"userId");
		//设置数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
		//首先通过获取默认城市，然后置顶
		long cfgCityId = rnoCommonService.getUserConfigAreaId(account);
		long cfgProvinceId=0;
		if(cfgCityId!=-1){
			
			cfgProvinceId=rnoCommonService.getParentIdByCityId(cfgCityId);
		}
		//再恢复数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		
		provinceAreas = rnoCommonService.getSpecialLevalAreaByAccount(account,
				"省");
		//如果该帐户没有设定过默认区域，哪就默认第一个省份为默认区域
		if(cfgProvinceId==0){
			cfgProvinceId=provinceAreas.get(0).getArea_id();
		}
		//先保存第一个省的对象信息:为了使默认的省排在首位
		Area tmp=provinceAreas.get(0);
		for (int i = 0; i < provinceAreas.size(); i++) {
			//替换次序
			if(provinceAreas.get(i).getArea_id()==cfgProvinceId){
				provinceAreas.set(0, provinceAreas.get(i));
				provinceAreas.set(i, tmp);
				break;
			}
		}
		cityAreas = new ArrayList<Area>();
		countryAreas = new ArrayList<Area>();
		if (provinceAreas != null && provinceAreas.size() > 0) {
			//通过缺省的省获取城市
			cityAreas = rnoCommonService
					.getSpecialSubAreasByAccountAndParentArea(account,
							cfgProvinceId, "市");
			//设置数据源
			DataSourceContextHolder.setDataSourceType(DataSourceConst.rnoDS);
			Area areaTemp = new Area();
			for (int i = 0; i < cityAreas.size(); i++) {
				if(cityAreas.get(i).getArea_id() == cfgCityId) {
					areaTemp  = cityAreas.get(0);
					cityAreas.set(0, cityAreas.get(i));
					cityAreas.set(i, areaTemp);
				}
			}
			if (cityAreas != null && cityAreas.size() > 0) {
				countryAreas = rnoCommonService
						.getSpecialSubAreasByAccountAndParentArea(account,
								cityAreas.get(0).getArea_id(), "区/县");
				if (countryAreas != null && countryAreas.size() > 0) {
					areaName = countryAreas.get(0).getName();
					centerPoint = new Point();
					centerPoint.setLng(countryAreas.get(0).getLongitude());
					centerPoint.setLat(countryAreas.get(0).getLatitude());
				}
			}
		}
		
		//恢复数据源
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		
		// 根据登录人账号获取用户信息
		this.sysOrgUser = this.sysOrgUserService.getSysOrgUserByAccount(userId);
		// 判断登录人是否只有拥有系统管理员身份 true有 false没有
		flag = false;
		List<SysRole> userRolesByAccount = this.sysOrgUserService
				.getUserRolesByAccount(userId);
		if (userRolesByAccount != null) {
			for (SysRole s : userRolesByAccount) {
				// 判断角色是否为系统管理员
				if (s.getCode().equals("systemManager")) {
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
		} else {
			flag = false;
		}
		this.permissionModuleList = super.getSysUserRelaPermissionService().getFirstPermissionListByUserId(orgUserId,"RNO","RNO_MenuResource",false);
		//只要网优工具的
		String code="";
		List<Map> dels=new ArrayList<Map>();
		for(Map<String,Object> one:permissionModuleList){
			code=one.get("CODE")+"";
			
			if(!code.startsWith("RNO") && !code.startsWith("rno")){
				dels.add(one);
			}
		}
		if(dels.size()>0){
			for(Map one:dels){
				permissionModuleList.remove(one);
			}
		}
		long permissionId = ((BigDecimal) permissionModuleList.get(0).get("PERMISSION_ID")).longValue();
		try
		{
			menuList = getChildrenMenuListByParentIdAndType(permissionId);		
		}catch(Exception e)
		{
			log.error("查找导航菜单失败");
			log.error(e.getStackTrace());
		}
		//log.info("导航菜单: "+menuList);
		log.info("执行rnoUserIndexAction方法成功，实现了”登录我的首页");
		log.info("退出rnoUserIndexAction方法,返回success");
		return "success";
	}
	
	
	/**
	 * 根据类型标识返回其下所有第一层子节点
	 * @throws IOException 
	 * @throws IOException
	 */
	public List<Map<String, Object>> getChildrenMenuListByParentIdAndType(long permissionId) throws IOException{
		//String userId = (String) SessionService.getInstance().getValueByKey(UserInfo.CAS_FILTER_USERID);
		long orgUserId =Long.parseLong(SessionService.getInstance().getValueByKey(com.iscreate.plat.login.constant.UserInfo.ORG_USER_ID).toString());
		HttpServletRequest request = ServletActionContext.getRequest();
		
		//获取参数
		//String parentId = request.getParameter("node");
		//String menuType = request.getParameter("menuType");
		//String permission_id=request.getParameter("permission_id");
		//if(parentId==null||"".equals(parentId)) return;
		
		Gson gson = new Gson();
		
		//List<Map<String,Object>> permissionByParentIdccount = this.sysUserRelaPermissionService.getSysPermissionListByOrgUserIdAndParentId(orgUserId, permissionId);
		List<Map<String,Object>> permissionByParentIdccount = this.getSysUserRelaPermissionService().getSysPermissionListByOrgUserIdAndParentId(orgUserId, permissionId);
		Map<String, List<Map<String, Object>>> map = new TreeMap<String, List<Map<String,Object>>>();
		String[] titleStrings = null;
		//数据格式转换
		if(permissionByParentIdccount != null && permissionByParentIdccount.size() > 0){
			titleStrings = new String[permissionByParentIdccount.size()];
			int i = 0;
			int j = 0;
			for(Map<String,Object> m: permissionByParentIdccount){
				if(m != null){
					//设置第一个为默认打开
					if(i == 0){
						m.put("isDefault", "1");
						i++;
					}
					if(m.get("TITLE") == null){
						continue;
					}else if(map.containsKey(m.get("TITLE").toString())){
						List<Map<String, Object>> list = map.get(m.get("TITLE"));
						m.put("text", m.get("NAME"));
						m.put("cls", "forum");
						m.put("leaf", true);
						m.put("expanded", true);
						String url = "../"+m.get("URL");
						if(m.get("PARAMETER") != null && !m.get("PARAMETER").equals("")){
							url = url + "?" +m.get("PARAMETER");
						}else{
							url = url;
						}
						m.put("href", url);
						m.put("iconCls", "icon-forum");
						m.put("hrefTarget", "main_right");
						m.put("id", m.get("permission_id"));
						list.add(m);
						map.put(m.get("TITLE").toString(), list);
					}else{
						titleStrings[j]=m.get("TITLE").toString();
						j++;
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						m.put("text", m.get("NAME"));
						m.put("cls", "forum");
						m.put("leaf", true);
						m.put("expanded", true);
						String url = "../"+m.get("URL");
						if(m.get("PARAMETER") != null && !m.get("PARAMETER").equals("")){
							url = url + "?" +m.get("PARAMETER");
						}else{
							url = url;
						}
						m.put("href", url);
						m.put("iconCls", "icon-forum");
						m.put("hrefTarget", "main_right");
						m.put("id", m.get("permission_id"));
						list.add(m);
						map.put(m.get("TITLE").toString(), list);
					}
				}else{
					continue;
				}
			}
		}
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		if(map != null){
			int i = 0;
			for(String s : titleStrings){
				if(s != null && !s.equals("")){
					Map<String, Object> ma = new HashMap<String, Object>();
					ma.put("text", s);
					ma.put("cls", "forum-ct");
					ma.put("leaf", false);
					ma.put("expanded", true);
					ma.put("iconCls", "forum-parent");
					ma.put("id", i);
					ma.put("children", map.get(s));
					rList.add(ma);
					i++;
				}
			}
		}
		
		return rList;
	/*	String result = "";
		if (rList != null) {			
			result = gson.toJson(rList);
		}*/

	}
}
