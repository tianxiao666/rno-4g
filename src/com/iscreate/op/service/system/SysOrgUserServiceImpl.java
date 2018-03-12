package com.iscreate.op.service.system;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.dao.common.CommonDao;
import com.iscreate.op.dao.informationmanage.EnterpriseInformationDao;
import com.iscreate.op.dao.system.SysAccountDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.dao.system.SysRoleDao;
import com.iscreate.op.dao.system.SysRoleTypeDao;
import com.iscreate.op.dao.system.SysOrgUserDao;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.project.ProSubJect;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysOrgUserPm;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.system.SysRoleType;
import com.iscreate.op.pojo.system.SysUserRelaOrg;
import com.iscreate.op.pojo.system.SysUserRelaPost;
import com.iscreate.op.pojo.system.SysUserRelaRole;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.plat.system.strategy.PasswordRule;
import com.iscreate.plat.system.util.AuthorityConstant;
import com.iscreate.plat.system.util.AuthorityPasswordEncoder;
import com.iscreate.plat.system.util.Md5PasswordEncoder;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.firstLetter.FirstLetterService;
import com.iscreate.plat.tools.paginghelper.PagingHelper;
import com.iscreate.plat.tree.TreeNode;

public class SysOrgUserServiceImpl implements SysOrgUserService {

	private Log log = LogFactory.getLog(this.getClass());

	private SysOrgUserDao sysOrgUserDao;
	private CommonDao commonDao;
	private AuthorityPasswordEncoder authorityPasswordEncoder;// 密码加密器
	private PasswordRule passwordRule;// 校验密码是否合符要求的验证器
	private FirstLetterService firstLetterService;
	
	private EnterpriseInformationDao enterpriseInformationDao;
	
	private SysOrganizationDao sysOrganizationDao;
	
	private SysRoleDao sysRoleDao;
	
	private SysRoleTypeDao sysRoleTypeDao;
	
	private SysAccountDao sysAccountDao;
	
	private SysAccountService sysAccountService;
	
	private SysDictionaryService sysDictionaryService;
	
	private static long USER_GROUP = 2;
	
	
	
	/**
	 * 保存用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public Serializable txSaveSysOrgUser(SysOrgUser sysOrgUser)
		throws Exception {
			String message = "";
			if (sysOrgUser == null) {
				message = "保存的用户对象不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if (sysOrgUser.getAddress() == null
//					|| "".equals(sysOrgUser.getAddress().trim())) {
//				message = "保存的账号中的address字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message, ErrorType.NOTVALID);
//			}
//			if(sysOrgUser.getMobile() == null
//					|| "".equals(sysOrgUser.getMobile().trim())){
//				message = "保存的账号中的mobile字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			if(sysOrgUser.getEmail() == null
					|| "".equals(sysOrgUser.getEmail().trim())){
				message = "保存的账号中的email字段信息不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if(sysOrgUser.getMobileemail() == null
//					|| "".equals(sysOrgUser.getMobileemail().trim())){
//				message = "保存的账号中的mobileemail字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			// 状态
			if (sysOrgUser.getStatus() == null) {
				int status = 0; // 无效?
				sysOrgUser.setStatus(status);
			}
			//设置用户创建时间
			Date date = new Date();
			sysOrgUser.setCreatetime(date);
			
			Serializable saveObject = commonDao.saveObject(sysOrgUser);
			return saveObject;
		}
	
	
	/**
	 * 保存用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public Serializable txSaveSysOrgUser(SysOrgUserPm sysOrgUser)
		throws Exception {
			String message = "";
			if (sysOrgUser == null) {
				message = "保存的用户对象不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if (sysOrgUser.getAddress() == null
//					|| "".equals(sysOrgUser.getAddress().trim())) {
//				message = "保存的账号中的address字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message, ErrorType.NOTVALID);
//			}
//			if(sysOrgUser.getMobile() == null
//					|| "".equals(sysOrgUser.getMobile().trim())){
//				message = "保存的账号中的mobile字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
//			if(sysOrgUser.getEmail() == null
//					|| "".equals(sysOrgUser.getEmail().trim())){
//				message = "保存的账号中的email字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
//			if(sysOrgUser.getMobileemail() == null
//					|| "".equals(sysOrgUser.getMobileemail().trim())){
//				message = "保存的账号中的mobileemail字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			// 状态
			//设置用户创建时间
			Date date = new Date();
			sysOrgUser.setCreateTime(date);
			
			Serializable saveObject = commonDao.saveObject(sysOrgUser);
			return saveObject;
		}
	

	/**
	 * 根据组织Id获取人员(分页)
	 * 
	 * @date May 7, 2013 2:14:08 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 *            组织ID
	 * @param
	 * @param currentPage
	 *            查询页面数字
	 * @param
	 * @param pageSize
	 *            总页数
	 * @param
	 * @return
	 * @throws
	 */
	public Map<String, Object> getAccountListToPageByOrgIdService(long orgId,
			int currentPage, int pageSize , String conditions) {
		log.info("进入getAccountListToPageByOrgIdService方法");
		log.info("参数：orgId=" + orgId + ",currentPage=" + currentPage
				+ ",pageSize=" + pageSize);
		// Account acc = (Account)
		// SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
		// Long enterpriseId = acc.getEnterpriseId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		// 根据组织Id获取组织与人员的关系
		// 获取需要分页的总数
		int totalCount = this.sysOrgUserDao
				.getOrgAccountRelationToPageTotalByOrgId(orgId);
		// 利用PagingHelper里的calculatePagingParamService方法计算总页数
		PagingHelper ph = new PagingHelper();
		Map<String, Object> phMap = ph.calculatePagingParamService(totalCount,
				currentPage, pageSize);
		map.put("totalPage", phMap.get("totalPage"));
		currentPage = (Integer) phMap.get("currentPage");
		// 利用Hibernate分页查询
		List<Map<String,Object>> accountToPageByOrgId = this.sysOrgUserDao
				.getAccountToPageByOrgId(orgId, currentPage, pageSize , conditions ,true);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (accountToPageByOrgId != null && accountToPageByOrgId.size() > 0) {
			for (Map<String,Object> a : accountToPageByOrgId) {
//				Long orgUserId = a.getOrgUserId();
				// String type = "";
				// List<SysRole> userRoles =
				// this.getUserRoles(BizAuthorityConstant.USER_GROUP, account);
				// if(userRoles!=null && userRoles.size()>0){
				// type = userRoles.get(0).getName();
				// }
				if (a != null) {
					// s.setCellPhoneNumber(a.getCellPhoneNumber());
					Object birthDay = a.get("birthday");
					if (birthDay != null && !birthDay.equals("")) {
						Date parse = null;
						try {
							parse = sdf1.parse(birthDay+"");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String birth = sdf.format(parse);
						// s.setBirth(birth);
						a.put("birthday", birth);
					}else{
						a.put("birthday", "");
					}
					// s.setType(type);
					// // if(a.getSex()!=null&&!"".equals(a.getSex())){
					// // s.setSex(a.getSex());
					// // }
					// // StaffType staffTypeByTypeCode =
					// this.organizationDao.getStaffTypeByTypeCode(type);
					// s.setAccountObj(a);
					list.add(a);
				}
			}
		}
		map.put("staffList", list);
		log
				.info("执行getAccountListToPageByOrgIdService方法成功，实现了”根据组织Id获取人员(分页)“的功能");
		log
				.info("退出getAccountListToPageByOrgIdService方法,返回Map<String,Object>集合");
		return map;
	}
	
	
	/**
	 * 根据组织Id获取人员
	* @date May 14, 2013 1:38:10 PM
	* @Description: TODO 
	* @param @param enterpriseId
	* @param @param choiceAccountType
	* @param @param orgId
	* @param @param pinyin
	* @param @param conditions
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getSysOrgUserByOrgIdService(long enterpriseId,String choiceAccountType,long orgId ,String pinyin,String conditions) {
		log.info("进入getAccountListToPageByOrgIdService方法");
		// Account acc = (Account)
		// SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
		// Long enterpriseId = acc.getEnterpriseId();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		// 根据组织Id获取组织与人员的关系
		// 获取需要分页的总数
		int totalCount = this.sysOrgUserDao
				.getOrgAccountRelationToPageTotalByOrgId(orgId);
		// 利用PagingHelper里的calculatePagingParamService方法计算总页数
		PagingHelper ph = new PagingHelper();
		//是否过滤人员
		List<Map<String,Object>> accountToPageByOrgId = null;
		if("one".equals(choiceAccountType)){
			accountToPageByOrgId = this.sysOrgUserDao
			.getSysOrgUserByEnterpriseId(enterpriseId,true, conditions);
		}else{
			accountToPageByOrgId = this.sysOrgUserDao
			.getSysOrgUserByEnterpriseId(enterpriseId,false, conditions);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (accountToPageByOrgId != null && accountToPageByOrgId.size() > 0) {
			for (Map<String,Object> a : accountToPageByOrgId) {
//				Long orgUserId = a.getOrgUserId();
				// String type = "";
				// List<SysRole> userRoles =
				// this.getUserRoles(BizAuthorityConstant.USER_GROUP, account);
				// if(userRoles!=null && userRoles.size()>0){
				// type = userRoles.get(0).getName();
				// }
				if (a != null) {
					// s.setCellPhoneNumber(a.getCellPhoneNumber());
					Object birthDay = a.get("birthday");
					if (birthDay != null && !birthDay.equals("")) {
						Date parse = null;
						try {
							parse = sdf1.parse(birthDay+"");
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String birth = sdf.format(parse);
						// s.setBirth(birth);
						a.put("birthday", birth);
					}else{
						a.put("birthday", "");
					}
					// s.setType(type);
					// // if(a.getSex()!=null&&!"".equals(a.getSex())){
					// // s.setSex(a.getSex());
					// // }
					// // StaffType staffTypeByTypeCode =
					// this.organizationDao.getStaffTypeByTypeCode(type);
					// s.setAccountObj(a);
					list.add(a);
				}
			}
		}
		//拼音过滤
		List<Map<String,Object>> filterPinyinList = new ArrayList<Map<String,Object>>();
		if(pinyin != null && !pinyin.trim().equals("") && !pinyin.equals("all")){
			if(list != null){
				log.info("开始对list进行拼音过滤");
				for(Map<String,Object> ma:list){
					Object name = ma.get("name");
					if(name != null && !"".equals(name)){
						String firstLetter = this.firstLetterService.getFirstLetter(name+"");
						if(firstLetter!=null&&!"null".equals(firstLetter)){
							if(firstLetter.equals(pinyin)){
								filterPinyinList.add(ma);
							}
						}
					}
				}
			}else{
				log.info("返回集合list为空");
			}
		}else{
			log.info("不需要拼音过滤");
			filterPinyinList = list;
		}

		log
				.info("执行getAccountListToPageByOrgIdService方法成功，实现了”根据组织Id获取人员(分页)“的功能");
		log
				.info("退出getAccountListToPageByOrgIdService方法,返回List<Map<String,Object>>集合");
		return filterPinyinList;
	}

	// /**
	// * 根据角色类型编号和用户获取角色
	// * @return
	// */
	// private List<SysRole> getUserRoles(final String roleTypeCode,final long
	// account){
	// log.info("进入getUserRoles方法");
	// log.info("参数：account="+account+",roleTypeCode="+roleTypeCode);
	// List<SysRole> list = hibernateTemplate.executeFind(new
	// HibernateCallback<List<SysRole>>(){
	//
	// public List<SysRole> doInHibernate(Session session)
	// throws HibernateException, SQLException {
	// // authorizeService.setSession(session);
	// try {
	// return sysOrgUserDao.getUserRoles(session,roleTypeCode, account);
	// } catch (Exception e) {
	// log.error("返回数据出错");
	// return null;
	// }
	// }
	// });
	// log.info("执行getUserRoles方法成功，实现了”根据角色类型编号和用户获取角色“的功能");
	// log.info("退出getUserRoles方法,返回List<Role>");
	// return list;
	// }

	
	
	

	/**
	 * 根据登录人账号获取用户信息
	 * 
	 * @date May 9, 2013 10:04:25 AM
	 * @Description: TODO
	 * @param
	 * @param session
	 * @param
	 * @param account
	 * @param
	 * @return
	 * @throws
	 */
	public SysOrgUser getSysOrgUserByAccount(String account) {
		return this.sysOrgUserDao.getSysOrgUserByAccount(account);
	}
	
	
	/**
	 * 
	 * 根据组织ID获取人员名称
	 * @param orgId
	 * @date 2013-08-23
	 * @author Li.hb
	 * @return
	 * 
	 */
	public  List<Map<String, Object>> getUserNameByOrgId(String orgId)
	{
		List<Map<String, Object>> map = null;
		
		if("".equals(orgId) || orgId == null)
		{
			return map;
		}
		
		map = this.sysOrgUserDao.getUserNameByOrgId(orgId);
		
		return map;
	}
	
	


	/**
	 * 根据账号获取人员
	 * @param accountId
	 */
	public Map<String,Object> getProviderAccountByAccountIdAjaxService(long orgUserId){
		log.info("进入getProviderAccountByAccountIdAjaxService方法");
		log.info("参数：orgUserId="+orgUserId);
//		Account acc = (Account) SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
//		Long enterpriseId = acc.getEnterpriseId();
		Map<String,Object> map = null;
//		//根据账号获取人员信息
//		SysOrgUser sysStaffByAccount = this.sysOrgUserDao.getSysStaffByAccount(orgUserId);
//		
//		//根据账号获取账号信息
//		SysAccount sysAccountByOrgUserId = this.sysOrgUserDao.getSysAccountByOrgUserId(orgUserId);
		List<Map<String,Object>> sysOrgUserByOrgUserId = this.sysOrgUserDao.getSysOrgUserByOrgUserId(orgUserId);
		if(sysOrgUserByOrgUserId==null || sysOrgUserByOrgUserId.size() == 0){
			return map;
		}else{
			//获取人员详细信息
			map = sysOrgUserByOrgUserId.get(0);
			String timeFormat = "yyyy年MM月";
			map.put("birthday", TimeFormatHelper.getTimeFormatByFree(map.get("birthday"),timeFormat));
			map.put("birthdayStr", TimeFormatHelper.getTimeFormatByDay(map.get("birthday")));
			Object passwordObj = map.get("password");
			String password = "";
			if(passwordObj != null){
				password = passwordObj + "";
			}
			String inputPwd = password;
//			if (authorityPasswordEncoder != null) {
//				inputPwd = authorityPasswordEncoder.encodePassword(password);
//			}
//			map.put("password",inputPwd);
			
			Object account = map.get("account");
			if(account != null){
				//根据人员获取角色
				List<SysRole> sysRoleByAccount = this.sysRoleDao.getUserRoles(account+"");
				map.put("roleIdList", sysRoleByAccount);
			}else{
				log.info("人员账号account为空");
			}
			
		}
		
		
		log.info("执行getProviderAccountByAccountIdAjaxService方法成功，实现了”根据账号获取人员“的功能");
		log.info("退出getProviderAccountByAccountIdAjaxService方法,返回Map<String,Object>"+map);
		return map;
	}
	
	
	/**
	 * 根据角色类型id获取角色
	* @date May 13, 2013 2:52:08 PM
	* @Description: TODO 
	* @param @param roleTypeId
	* @param @return        
	* @throws
	 */
	public List<SysRole> getSysRoleByRoleTypeId(long roleTypeId){
		log.info("进入getSysRoleByRoleTypeId方法");
		List<SysRole> list = null;
		List<SysRole> sysRoleByRoleTypeId = this.sysRoleDao.getSysRoleByRoleTypeId(roleTypeId);
		if(sysRoleByRoleTypeId != null){
			list = sysRoleByRoleTypeId;
		}else{
			list = null;
		}
		log.info("执行getSysRoleByRoleTypeId方法成功，实现了”获取全部角色类型“的功能");
		log.info("退出getSysRoleByRoleTypeId方法,返回List<SysRole>"+list);
		return list;
	}
	
	/**
	 * 获取全部角色类型
	* @date May 13, 2013 4:01:39 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<SysRoleType> getAllSysRoleType(){
		log.info("进入getAllSysRoleType方法");
		List<SysRoleType> allSysRoleType = this.sysRoleTypeDao.getAllRoleType();
		log.info("进入getAllSysRoleType方法成功，实现了”获取全部角色类型“的功能");
		log.info("进入getAllSysRoleType方法");
		return allSysRoleType;
	}

	/**
	 * 获取全部角色
	* @date May 13, 2013 4:01:39 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<SysRole> getAllSysRole(){
		log.info("进入getAllSysRole方法");
		List<SysRole> allSysRole = this.sysRoleDao.getAllRole();
		log.info("进入getAllSysRole方法成功，实现了”获取全部角色类型“的功能");
		log.info("进入getAllSysRole方法");
		return allSysRole;
	}
	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public String txUpdateStaffAndUserRole(String orgJsonStr){
			log.info("进入txUpdateStaffAndUserRole方法");
			log.info("参数：orgJsonStr="+orgJsonStr);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			if(orgJsonStr==null || "".equals(orgJsonStr)){
				log.debug("参数：orgJsonStr为空");

				return "failed";
			}
			Map<String,String> strJson = gson.fromJson(orgJsonStr, new TypeToken<Map<String,String>>(){}.getType());
			//判断该账号是否存在
//			String showAccount = strJson.get("showAccount");
			String orgUserIdString = strJson.get("orgUserId");
			long orgUserId = 0;
			if(orgUserIdString != null && !orgUserIdString.trim().equals("")){
				orgUserId = Long.parseLong(orgUserIdString);
			}else{
				log.info("人员ID为空");
				orgUserId = 0;
				return "failed";
			}

//			Account acc = (Account) SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
//			Long enterpriseId = acc.getEnterpriseId();
			SysOrgUser sysStaffByAccount = this.sysOrgUserDao.getSysStaffByOrgUserId(orgUserId);
			if(sysStaffByAccount==null){
				log.debug("不存在该人员");

				return "failed";
			}
			SysAccount sysAccountByOrgUserId = this.sysAccountDao.getSysAccountByOrgUserId(orgUserId);
			if(sysAccountByOrgUserId==null){
				log.debug("不存在该账号");

				return "failed";
			}
			
			//txSaveAccount(SysAccount account)
			//修改账号
			String account = strJson.get("account");
			sysAccountByOrgUserId.setAccount(account);
			String password = strJson.get("password");
			String sysAcountPassWord = sysAccountByOrgUserId.getPassword();
			if(sysAcountPassWord != null){
				if(!sysAcountPassWord.equals(password)){
					String encodePassword = authorityPasswordEncoder.encodePassword(password);
					if(!sysAcountPassWord.equals(encodePassword)){	
						sysAccountByOrgUserId.setPassword(encodePassword);
					}
				}
			}else{
				log.debug("原账号密码为空");
			}
			String statusString = strJson.get("status");
			int status = 0;
			if(statusString != null && !statusString.trim().equals("")){
				status = Integer.parseInt(statusString);
			}else{
				log.info("状态status转换类型失败");
				status = 0;
			}
			//sysAccountByOrgUserId.setStatus(status);
			//设置更新时间
			sysAccountByOrgUserId.setUpdatetime(new Date());
			try {
				this.txUpdateAccount(sysAccountByOrgUserId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//修改用户信息
			String name = strJson.get("name");
			sysStaffByAccount.setName(name);
		//	long enterpriseId = Long.parseLong(strJson.get("enterpriseId"));
		//	sysStaffByAccount.setEnterpriseId(enterpriseId);
			String gender = strJson.get("gender");
			sysStaffByAccount.setGender(gender);
			String tel = strJson.get("tel");
			if(tel != null && !tel.trim().equals("")){
				sysStaffByAccount.setTel(tel);
			}
			String mobile = strJson.get("mobile");
			sysStaffByAccount.setMobile(mobile);
			String email = strJson.get("email");
			sysStaffByAccount.setEmail(email);
			String mobileemail = strJson.get("mobileemail");
			sysStaffByAccount.setMobileemail(mobileemail);
			String backupemail = strJson.get("backupemail");
			sysStaffByAccount.setBackupemail(backupemail);
			sysStaffByAccount.setStatus(status);
			try {
				this.txUpdateSysOrgUser(sysStaffByAccount);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//修改人员与角色的关系
			String roleIdList = strJson.get("roleIdList");
			if(roleIdList != null && !roleIdList.trim().equals("")){
				String[] split = roleIdList.split(",");
				//根据登录人账号获取角色(为回滚时备用)
				List<SysRole> userRoles = this.sysRoleDao.getUserRoles(account);
				Long accountId = sysAccountByOrgUserId.getAccountId();
				//
				try {
					this.sysOrgUserDao.bulkDeleteSysUserRelaRole(accountId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.info("批量删除角色失败");
					return "failed";
				}
				if(split != null){
					for(String roleIdStr:split){
						long roleId = 0;
						if(roleIdStr != null && !roleIdStr.trim().equals("")){
							roleId = Long.parseLong(roleIdStr);
						}else{
							continue;
						}
						SysUserRelaRole sysUserRelaRole = new SysUserRelaRole();
						sysUserRelaRole.setRoleId(roleId);
						//sysUserRelaRole.setAccountId(accountId);
						sysUserRelaRole.setOrgUserId(sysAccountByOrgUserId.getOrgUserId());
						this.sysOrgUserDao.savaSysUserRelaRole(sysUserRelaRole);
					}
				}else{
					log.info("需要添加的角色ID集合为空");
				}
			}else{
				log.info("roleIdList为空");
			}
			log.info("执行txUpdateStaffAndUserRole方法成功，实现了”ajax修改账号信息“的功能");
			log.info("退出txUpdateStaffAndUserRole方法,返回void");
			return "success";
	}
	
	/**
	 * 修改账号
	* @date May 14, 2013 5:44:48 PM
	* @Description: TODO 
	* @param @param account
	* @param @throws Exception        
	* @throws
	 */
	public void txUpdateAccount(SysAccount account) throws Exception {
		String message = "";
		if (account == null) {
			message = "保存的账号对象不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		if (account.getAccount() == null
				|| "".equals(account.getAccount().trim())) {
			message = "保存的账号中的account字段信息不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		// 允许同名，中文名不限制

		// 保存
		if (account.getPassword() == null) {
			message = "账号：" + account.getAccount() + "的密码不能为空！";
			log.error(message);
			throw new Exception(message);
		}
		// 判断是否符合规则要求
		if (passwordRule != null
				&& passwordRule.checkIfMeet(account.getPassword()) != 0) {
			message = "账号：" + account.getAccount() + "的密码不符合要求！"
					+ passwordRule.getPrompWords();
			log.error(message);
			throw new Exception(message);
		}
		// 加密
		//account.setPassword(authorityPasswordEncoder.encodePassword(account.getPassword()));

		// // 账号的有效期
		// GregorianCalendar calendar = new GregorianCalendar();
		//
		// if (account.getCreatetime() == null) {
		// calendar.set(Calendar.YEAR, 1970);
		// calendar.set(Calendar.MONTH, 1);
		// calendar.set(Calendar.DAY_OF_MONTH, 1);
		// account.getCreatetime(calendar.getTime());
		// }
		// if (account.getTime_range_end() == null) {
		// calendar.set(Calendar.YEAR, 2020);
		// calendar.set(Calendar.MONTH, 12);
		// calendar.set(Calendar.DAY_OF_MONTH, 31);
		// account.setTime_range_end(calendar.getTime());
		// }
		// 状态
		/*if (account.getStatus() == null) {
			int status = 0; // 无效?
			account.setStatus(status);
		}*/
		//设置用户修改时间
		Date date = new Date();
		account.setUpdatetime(date);
		commonDao.updateObject(account);
	}
	
	/**
	 * 修改用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public void txUpdateSysOrgUser(SysOrgUser sysOrgUser)
		throws Exception {
			String message = "";
			if (sysOrgUser == null) {
				message = "保存的用户对象不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if (sysOrgUser.getAddress() == null
//					|| "".equals(sysOrgUser.getAddress().trim())) {
//				message = "保存的账号中的address字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message, ErrorType.NOTVALID);
//			}
//			if(sysOrgUser.getMobile() == null
//					|| "".equals(sysOrgUser.getMobile().trim())){
//				message = "保存的账号中的mobile字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			if(sysOrgUser.getEmail() == null
					|| "".equals(sysOrgUser.getEmail().trim())){
				message = "保存的账号中的email字段信息不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if(sysOrgUser.getMobileemail() == null
//					|| "".equals(sysOrgUser.getMobileemail().trim())){
//				message = "保存的账号中的mobileemail字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			// 状态
			if (sysOrgUser.getStatus() == null) {
				int status = 0; // 无效?
				sysOrgUser.setStatus(status);
			}
			//设置用户修改时间
			Date date = new Date();
			sysOrgUser.setUpdatetime(date);
			
			commonDao.updateObject(sysOrgUser);
		}
	
	
	/**
	 * 修改用户信息
	* @date May 14, 2013 9:14:28 AM
	* @Description: TODO 
	* @param @param sysOrgUser
	* @param @throws Exception        
	* @throws
	 */
	public void txUpdateSysOrgUser(SysOrgUserPm sysOrgUser)
		throws Exception {
			String message = "";
			if (sysOrgUser == null) {
				message = "保存的用户对象不能为空！";
				log.error(message);
				throw new Exception(message);
			}
//			if (sysOrgUser.getAddress() == null
//					|| "".equals(sysOrgUser.getAddress().trim())) {
//				message = "保存的账号中的address字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message, ErrorType.NOTVALID);
//			}
//			if(sysOrgUser.getMobile() == null
//					|| "".equals(sysOrgUser.getMobile().trim())){
//				message = "保存的账号中的mobile字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
//			if(sysOrgUser.getEmail() == null
//					|| "".equals(sysOrgUser.getEmail().trim())){
//				message = "保存的账号中的email字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
//			if(sysOrgUser.getMobileemail() == null
//					|| "".equals(sysOrgUser.getMobileemail().trim())){
//				message = "保存的账号中的mobileemail字段信息不能为空！";
//				log.error(message);
//				throw new Exception(message);
//			}
			// 状态
			if (sysOrgUser.getStatus() == null) {
				// 无效?
				sysOrgUser.setStatus(0l);
			}
			//设置用户修改时间
			Date date = new Date();
			sysOrgUser.setModTime(date);
			
			commonDao.updateObject(sysOrgUser);
		}
	
	
	/**
	 * 获取企业url的数据字典
	* @date May 21, 2013 11:11:58 AM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String,String>> getEnterpriseUrlDictionaryService(){
		log.info("进入getEnterpriseUrlDictionaryService方法");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<InformationEnterprise> findAllList = this.enterpriseInformationDao.find(new HashMap());
		if(findAllList!=null && findAllList.size()>0){
			for (InformationEnterprise ie : findAllList) {
				if(ie.getEnterpriseSuffix()!=null && !"".equals(ie.getEnterpriseSuffix())){
					Map<String,String> map = new HashMap<String, String>();
					map.put("typeCode", ie.getId()+"");
					map.put("name", ie.getEnterpriseSuffix());
					list.add(map);
				}
			}
		}
		log.info("执行getEnterpriseUrlDictionaryService方法成功，实现了”获取企业url的数据字典“的功能");
		log.info("退出getEnterpriseUrlDictionaryService方法,返回List<Map<String,String>>集合");
		return list;
	}
	
	/**
	 * 保存人员基本信息
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveStaffAndUserRole(String orgJsonStr){
		log.info("进入txSaveProviderAccountAjaxService方法");
		log.info("参数：orgJsonStr="+orgJsonStr);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if(orgJsonStr==null || "".equals(orgJsonStr)){
			log.info("参数：orgJsonStr为空");

			return 0;
		}
		Map<String,String> strJson = gson.fromJson(orgJsonStr, new TypeToken<Map<String,String>>(){}.getType());
		//判断该账号是否存在
		long enterpriseId = Long.parseLong(strJson.get("enterpriseId"));
		String account = strJson.get("account");
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserDao.getSysOrgUserByAccount(account);
		if(sysOrgUserByAccount!=null){
			log.debug("存在该账号");

			return 0;
		}
		//txSaveAccount(SysAccount account)
		String statusString = strJson.get("status");
		int status = 0;
		if(statusString != null && !statusString.trim().equals("")){
			status = Integer.parseInt(statusString);
		}else{
			log.info("状态status转换类型失败");
			status = 0;
		}
		
		//保存用户信息
		SysOrgUser sysOrgUser = new SysOrgUser();
		sysOrgUser.setEnterpriseId(enterpriseId);
		sysOrgUser.setStatus(status);
		String name = strJson.get("name");
		sysOrgUser.setName(name);
		String gender = strJson.get("gender");
		sysOrgUser.setGender(gender);
		String tel = strJson.get("tel");
		if(tel != null && !tel.trim().equals("")){
			sysOrgUser.setTel(tel);
		}
		String mobile = strJson.get("mobile");
		sysOrgUser.setMobile(mobile);
		String email = strJson.get("email");
		sysOrgUser.setEmail(email);
		String mobileemail = strJson.get("mobileemail");
		sysOrgUser.setMobileemail(mobileemail);
		String backupemail = strJson.get("backupemail");
		sysOrgUser.setBackupemail(backupemail);
		long orgUserId = 0;
		try {
			Serializable txSaveSysOrgUser = this.txSaveSysOrgUser(sysOrgUser);
			if(txSaveSysOrgUser != null && !txSaveSysOrgUser.equals("")){
				orgUserId = Long.parseLong(txSaveSysOrgUser.toString());
			}
		} catch (Exception e1) {
			log.equals("添加人员基本信息失败");
			e1.printStackTrace();
			return 0;
		}
		
		//保存账号
		SysAccount sysAccount = new SysAccount();
		sysAccount.setAccount(account);
		sysAccount.setOrgUserId(orgUserId);
		String password = strJson.get("password");
		sysAccount.setPassword(password);
		long accountId = 0;
		try {
			Serializable txSaveAccount = this.sysAccountService.txSaveAccount(sysAccount);
			if(txSaveAccount != null && !txSaveAccount.equals("")){
				accountId = Long.parseLong(txSaveAccount.toString());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.equals("保存账号失败");
			e1.printStackTrace();
			return 0;
		}
		
		//添加人员与角色的关系
		String roleIdList = strJson.get("roleIdList");
		String[] split = roleIdList.split(",");
		if(split != null){
			for(String roleIdStr:split){
				long roleId = 0;
				if(roleIdStr != null && !roleIdStr.trim().equals("")){
					roleId = Long.parseLong(roleIdStr);
				}else{
					continue;
				}
				SysUserRelaRole sysUserRelaRole = new SysUserRelaRole();
				sysUserRelaRole.setRoleId(roleId);
				//sysUserRelaRole.setAccountId(accountId);
				sysUserRelaRole.setOrgUserId(orgUserId);
				this.sysOrgUserDao.savaSysUserRelaRole(sysUserRelaRole);
			}
		}else{
			log.info("需要添加的角色ID集合为空");
		}
		
		
		
		
		//检查人员与组织的关系是否存在
		Object orgIdObj = strJson.get("orgId");
		if(orgIdObj != null && !orgIdObj.equals("")){
			long orgId = Long.parseLong(orgIdObj+"");
			if(orgUserId != 0){
				SysUserRelaOrg so = new SysUserRelaOrg();
				so.setOrgId(orgId);
				so.setOrgUserId(orgUserId);
				so.setCreatetime(new Date());
				this.sysOrganizationDao.saveEntity(so);
			}else{
				log.debug("用户ID为空");
			}
		}else{
			log.debug("组织ID为空");
		}
		
		log.info("执行txSaveProviderAccountAjaxService方法成功，实现了”保存人员基本信息“的功能");
		log.info("退出txSaveProviderAccountAjaxService方法,返回Map<String,Object>集合");
		return orgUserId;
	}
	
	
	
	/**
	 * 保存人员基本信息(v1.1.3)
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveOrgUserAndAccount(SysOrgUser sysOrgUser,SysAccount sysAccount){
		log.info("进入txSaveOrgUserAndAccount方法");
		String account = sysAccount.getAccount();
		//判断该账号是否存在
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserDao.getSysOrgUserByAccount(account);
		if(sysOrgUserByAccount!=null){
			log.debug("存在该账号");

			return 0;
		}
		
		long orgUserId = 0;
		try {
			Serializable txSaveSysOrgUser = this.txSaveSysOrgUser(sysOrgUser);
			if(txSaveSysOrgUser != null && !txSaveSysOrgUser.equals("")){
				orgUserId = Long.parseLong(txSaveSysOrgUser.toString());
			}
		} catch (Exception e1) {
			log.equals("添加人员基本信息失败");
			e1.printStackTrace();
			return 0;
		}
		long accountId = 0;
		try {
			sysAccount.setOrgUserId(orgUserId);
			Serializable txSaveAccount = this.sysAccountService.txSaveAccount(sysAccount);
			if(txSaveAccount != null && !txSaveAccount.equals("")){
				accountId = Long.parseLong(txSaveAccount.toString());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.equals("保存账号失败");
			e1.printStackTrace();
			return 0;
		}
		log.info("执行txSaveOrgUserAndAccount方法成功，实现了”保存人员基本信息“的功能");
		log.info("退出txSaveOrgUserAndAccount方法,返回Map<String,Object>集合");
		return orgUserId;
	}
	
	/**
	 * 保存人员基本信息(v1.1.3)
	* @date May 21, 2013 1:57:38 PM
	* @Description: TODO 
	* @param @param orgJsonStr
	* @param @return        
	* @throws
	 */
	public long txSaveOrgUserAndAccount(SysOrgUserPm sysOrgUser,SysAccount sysAccount){
		log.info("进入txSaveOrgUserAndAccount方法");
		String account = sysAccount.getAccount();
		//判断该账号是否存在
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserDao.getSysOrgUserByAccount(account);
		if(sysOrgUserByAccount!=null){
			log.debug("存在该账号");

			return 0;
		}
		//txSaveAccount(SysAccount account)
		/*int status = 0;
			status =Integer.parseInt(sysOrgUser.getStatus()+"");
		
		sysAccount.setStatus(status);*/
		long orgUserId = 0;
		try {
			Serializable txSaveSysOrgUser = this.txSaveSysOrgUser(sysOrgUser);
			if(txSaveSysOrgUser != null && !txSaveSysOrgUser.equals("")){
				orgUserId = Long.parseLong(txSaveSysOrgUser.toString());
			}
		} catch (Exception e1) {
			log.equals("添加人员基本信息失败");
			e1.printStackTrace();
			return 0;
		}
		long accountId = 0;
		try {
			sysAccount.setOrgUserId(orgUserId);
			Serializable txSaveAccount = this.sysAccountService.txSaveAccount(sysAccount);
			if(txSaveAccount != null && !txSaveAccount.equals("")){
				accountId = Long.parseLong(txSaveAccount.toString());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.equals("保存账号失败");
			e1.printStackTrace();
			return 0;
		}
		log.info("执行txSaveOrgUserAndAccount方法成功，实现了”保存人员基本信息“的功能");
		log.info("退出txSaveOrgUserAndAccount方法,返回Map<String,Object>集合");
		return orgUserId;
	}
	
	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public long txUpdateOrgUserAndAccount(SysOrgUser sysOrgUser,SysAccount sysAccount){
			long orgUserId = sysOrgUser.getOrgUserId();

//			Account acc = (Account) SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
//			Long enterpriseId = acc.getEnterpriseId();
			SysOrgUser sysStaffByAccount = this.sysOrgUserDao.getSysStaffByOrgUserId(orgUserId);
			if(sysStaffByAccount==null){
				log.debug("不存在该人员");

				return 0;
			}
			SysAccount sysAccountByOrgUserId = this.sysAccountDao.getSysAccountByOrgUserId(orgUserId);
			if(sysAccountByOrgUserId==null){
				log.debug("不存在该账号");

				return 0;
			}
			
			//txSaveAccount(SysAccount account)
			//修改账号
			sysAccountByOrgUserId.setAccount(sysAccount.getAccount());
			sysAccountByOrgUserId.setUpdatetime(new Date());
			String sysAcountPassWord = sysAccount.getPassword();
			if(sysAcountPassWord != null && !sysAcountPassWord.isEmpty()){
				if(!sysAcountPassWord.equals(sysAccountByOrgUserId.getPassword())){
					String encodePassword = authorityPasswordEncoder.encodePassword(sysAccount.getPassword());
					if(!sysAcountPassWord.equals(encodePassword)){	
						sysAccountByOrgUserId.setPassword(encodePassword);
					}
				}
			}else{
				log.debug("原账号密码为空");
			}
			int status = 0;
			if(sysOrgUser.getStatus() != null){
				status = sysOrgUser.getStatus();
			}
			/*
			sysAccountByOrgUserId.setStatus(status);*/
			//设置更新时间
			sysAccountByOrgUserId.setUpdatetime(new Date());
			try {
				this.txUpdateAccount(sysAccountByOrgUserId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//修改用户信息
			sysStaffByAccount.setName(sysOrgUser.getName());
			sysStaffByAccount.setGender(sysOrgUser.getGender());
			sysStaffByAccount.setMobile(sysOrgUser.getMobile());
			sysStaffByAccount.setEmail(sysOrgUser.getEmail());
			sysStaffByAccount.setStatus(status);
			sysStaffByAccount.setUpdatetime(new Date());
			try {
				this.txUpdateSysOrgUser(sysStaffByAccount);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.info("执行txSaveOrgUserAndAccount方法成功，实现了”ajax修改账号信息“的功能");
			log.info("退出txSaveOrgUserAndAccount方法,返回void");
			return orgUserId;
	}
	
	
	/**
	 * 修改人员基本信息
	* @date May 14, 2013 4:28:14 PM
	* @Description: TODO 
	* @param @param orgJsonStr        
	* @throws
	 */
	public long txUpdateOrgUserAndAccount(SysOrgUserPm sysOrgUser,SysAccount sysAccount){
			long orgUserId = sysOrgUser.getOrgUserId();

//			Account acc = (Account) SessionService.getInstance().getValueByKey(AuthorityConstant.SessionConstant.ACCOUNT_KEY);
//			Long enterpriseId = acc.getEnterpriseId();
			SysOrgUserPm oldSysOrgUserPm = this.getSysOrgUserPOJOByOrgUserId(orgUserId);
			if(oldSysOrgUserPm==null){
				log.debug("不存在该人员");

				return 0;
			}
			SysAccount sysAccountByOrgUserId = this.sysAccountDao.getSysAccountByOrgUserId(orgUserId);
			if(sysAccountByOrgUserId==null){
				log.debug("不存在该账号");

				return 0;
			}
			
			//txSaveAccount(SysAccount account)
			//修改账号
			sysAccountByOrgUserId.setAccount(sysAccount.getAccount());
			sysAccountByOrgUserId.setUpdatetime(new Date());
			String sysAcountPassWord = sysAccount.getPassword();
			if(sysAcountPassWord != null && !sysAcountPassWord.isEmpty()){
				if(!sysAcountPassWord.equals(sysAccountByOrgUserId.getPassword())){
					String encodePassword = authorityPasswordEncoder.encodePassword(sysAccount.getPassword());
					if(!sysAcountPassWord.equals(encodePassword)){	
						sysAccountByOrgUserId.setPassword(encodePassword);
					}
				}
			}else{
				log.debug("原账号密码为空");
			}
//			int status = 0;
			/*if(sysOrgUser.getStatus() != null){
				status = sysOrgUser.getStatus();
			}
			sysAccountByOrgUserId.setStatus(status);*/
			//设置更新时间
			sysAccountByOrgUserId.setUpdatetime(new Date());
			try {
				this.txUpdateAccount(sysAccountByOrgUserId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//修改用户信息
			oldSysOrgUserPm.setAccountAddress(sysOrgUser.getAccountAddress());
			oldSysOrgUserPm = (SysOrgUserPm)pojoToPojo(SysOrgUserPm.class,sysOrgUser, oldSysOrgUserPm);
			
//			oldSysOrgUserPm.setUpdatetime(new Date());
			try {
				this.txUpdateSysOrgUser(oldSysOrgUserPm);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.info("执行txSaveOrgUserAndAccount方法成功，实现了”ajax修改账号信息“的功能");
			log.info("退出txSaveOrgUserAndAccount方法,返回void");
			return orgUserId;
	}
	
	/**
	 * 根据人员账号获取人员角色
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param account        
	* @throws
	 */
	public List<SysRole> getUserRolesByAccount(String account){
		log.info("进入getUserRoles方法");
		List<SysRole> userRoles = this.sysRoleDao.getUserRoles(account);
		log.info("执行getUserRoles方法成功，实现了”根据人员账号获取人员角色“的功能");
		log.info("退出getUserRoles方法,返回List<SysRole>集合");
		return userRoles;
	}
	
	/**
	 * 根据人员id获取人员角色
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param orgUserId      
	* @throws
	 */
	public List<SysRole> getUserRolesByUserId(long orgUserId){
		log.info("进入getUserRoles方法");
		List<SysRole> userRoles = this.sysRoleDao.getUserRoles(orgUserId);
		log.info("执行getUserRoles方法成功，实现了”根据人员id获取人员角色“的功能");
		log.info("退出getUserRoles方法,返回List<SysRole>集合");
		return userRoles;
	}
	
	/**
	 * 根据人员id获取人员角色(String)
	* @author ou.jh
	* @date May 24, 2013 9:43:26 AM
	* @Description: TODO 
	* @param @param orgUserId      
	* @throws
	 */
	public String getUserRolesStringByUserId(long orgUserId){
		log.info("进入getUserRoles方法");
		String codes = "";
		List<SysRole> userRoles = this.sysRoleDao.getUserRoles(orgUserId);
		if(userRoles != null && userRoles.size() > 0){
			for(SysRole s:userRoles){
				codes = codes + "/" +s.getCode();
			}
		}
		codes = codes + "/";
		log.info("执行getUserRoles方法成功，实现了”根据人员id获取人员角色“的功能");
		log.info("退出getUserRoles方法,返回List<SysRole>集合");
		return codes;
	}
	
	
	
	/**
	 * 检测人员与角色关系
	* @author ou.jh
	* @date May 24, 2013 2:25:55 PM
	* @Description: TODO 
	* @param @param account
	* @param @param code
	* @param @return        
	* @throws
	 */
	public boolean checkUserRoleByAccountAndCode(String account,String code){
		log.info("进入checkUserRoleByAccountAndCode方法");
		boolean flag = true;
		List<Map<String,Object>> userRoleByAccountAndCode = this.sysOrgUserDao.getUserRoleByAccountAndCode(account, code);
		if(userRoleByAccountAndCode != null && userRoleByAccountAndCode.size() > 0){
			flag = true;
		}else{
			flag = false;
		}
		log.info("执行checkUserRoleByAccountAndCode方法成功，实现了”检测人员与角色关系“的功能");
		log.info("退出checkUserRoleByAccountAndCode方法,返回boolean");
		return flag;
	}
	
	/**
	 * 根据组织ID与角色类型获取组织下人员列表
	* @author ou.jh
	* @date May 24, 2013 2:44:01 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param roleCode
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUserByOrgIdAndRoleCode(long orgId,String roleCode){
		log.info("进入getUserByOrgIdAndRoleCode方法");
		if(orgId == 0){
			Map<String, Object> dictionaryMapByCode = this.sysDictionaryService.getDictionaryMapByCode("ISCREATE_ORG_ID");
			if(dictionaryMapByCode != null){
				orgId = Long.parseLong(dictionaryMapByCode.get("NAME").toString());
			}else{
				orgId = 0;
			}
		}
		List<Map<String,Object>> userByOrgIdAndRoleCode = this.sysOrgUserDao.getUserByOrgIdAndRoleCode(orgId, roleCode);
		log.info("执行getUserByOrgIdAndRoleCode方法成功，实现了”检测人员与角色关系“的功能");
		log.info("退出getUserByOrgIdAndRoleCode方法,返回List<Map<String, Object>>");
		return userByOrgIdAndRoleCode;
	}
	

	
	/**
	 * 根据组织ID集合获取维护队长
	* @author ou.jh
	* @date May 28, 2013 10:47:52 AM
	* @Description: TODO 
	* @param @param orgIds        
	* @throws
	 */
	public List<Map<String, Object>> getTeamLeaderByOrgIds(List<Long> orgIdsList){
		log.info("进入getTeamLeaderByOrgIds方法");
		log.info("执行getTeamLeaderByOrgIds方法成功，实现了”根据组织ID集合获取维护队长“的功能");
		List<Map<String,Object>> teamLeaderByOrgIds = this.sysOrgUserDao.getUserAndAccountByOrgIds(orgIdsList,"TeamLeader");
		log.info("退出getTeamLeaderByOrgIds方法,返回List<Map<String,Object>>"+teamLeaderByOrgIds);
		return teamLeaderByOrgIds;
	}
	
	
	public SysOrgUserDao getSysOrgUserDao() {
		return sysOrgUserDao;
	}



	public CommonDao getCommonDao() {
		return commonDao;
	}



	public AuthorityPasswordEncoder getAuthorityPasswordEncoder() {
		return authorityPasswordEncoder;
	}



	public PasswordRule getPasswordRule() {
		return passwordRule;
	}



	public FirstLetterService getFirstLetterService() {
		return firstLetterService;
	}



	public EnterpriseInformationDao getEnterpriseInformationDao() {
		return enterpriseInformationDao;
	}



	public SysRoleDao getSysRoleDao() {
		return sysRoleDao;
	}



	public SysRoleTypeDao getSysRoleTypeDao() {
		return sysRoleTypeDao;
	}



	public SysAccountDao getSysAccountDao() {
		return sysAccountDao;
	}



	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}



	/**
	 * 根据组织ID集合与角色类型获取人员
	* @author ou.jh
	* @date May 28, 2013 10:47:52 AM
	* @Description: TODO 
	* @param @param orgIds        
	* @throws
	 */
	public List<Map<String, Object>> getUserAndAccountByOrgIds(List<Long> orgIdsList,String roleCode){
		log.info("进入getUserAndAccountByOrgIds方法");
		log.info("执行getUserAndAccountByOrgIds方法成功，实现了”根据组织ID集合与角色类型获取人员“的功能");
		List<Map<String,Object>> teamLeaderByOrgIds = this.sysOrgUserDao.getUserAndAccountByOrgIds(orgIdsList,roleCode);
		log.info("退出getUserAndAccountByOrgIds方法,返回List<Map<String,Object>>"+teamLeaderByOrgIds);
		return teamLeaderByOrgIds;
	}
	
	
	/**
	 * 获取人员账号获取人员所在企业
	* @author ou.jh
	* @date May 28, 2013 2:17:05 PM
	* @Description: TODO 
	* @param @param account
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getEnterpriseByAccount(String account){
		log.info("进入getEnterpriseByAccount方法");
		log.info("执行getEnterpriseByAccount方法成功，实现了”获取人员账号获取人员所在企业“的功能");
		List<Map<String, Object>> enterpriseByAccount = this.sysOrgUserDao.getEnterpriseByAccount(account);
		log.info("退出getEnterpriseByAccount方法,返回List<Map<String,Object>>"+enterpriseByAccount);
		return enterpriseByAccount;
	}
	

	/**
	 * 根据组织Id获取人员
	 * 
	 * @date May 7, 2013 2:22:37 PM
	 * @Description: TODO
	 * @param
	 * @param orgId
	 * @param
	 * @return
	 * @throws
	 */
	public List<Map<String, Object>> getUserByOrgId(final long orgId){
		log.info("进入getUserByOrgId方法");
		log.info("执行getUserByOrgId方法成功，实现了”根据组织Id获取人员“的功能");
		List<Map<String,Object>> userByOrgId = this.sysOrgUserDao.getUserByOrgId(orgId);
		log.info("退出getUserByOrgId方法,返回List<Map<String,Object>>"+userByOrgId);
		return userByOrgId;
	}
	
	/**
	 * 根据账号获取人员信息
	 * @param account
	 * @return
	 */
	public SysOrgUser getSysStaffByOrgUserId(long orgUserId){
		log.info("进入getSysStaffByOrgUserId方法");
		log.info("执行getSysStaffByOrgUserId方法成功，实现了”根据账号获取人员信息“的功能");
		SysOrgUser sysOrgUser = this.sysOrgUserDao.getSysStaffByOrgUserId(orgUserId);
		log.info("退出getSysStaffByOrgUserId方法,返回SysOrgUser"+sysOrgUser);
		return sysOrgUser;
	}
	
	
	/**
	 * 根据账号获取人员信息(1.2.1)
	 * 
	 * @param account
	 * @return
	 */
	public SysOrgUserPm getSysOrgUserPOJOByOrgUserId(long orgUserId){
		log.info("进入getSysOrgUserPOJOByOrgUserId方法");
		log.info("执行getSysOrgUserPOJOByOrgUserId方法成功，实现了”根据账号获取人员信息“的功能");
		SysOrgUserPm sysOrgUser = this.sysOrgUserDao.getSysOrgUserPOJOByOrgUserId(orgUserId);
		log.info("退出getSysOrgUserPOJOByOrgUserId方法,返回SysOrgUser"+sysOrgUser);
		return sysOrgUser;
	}
	
	/**
	 * 根据用户ID获取账号信息
	* @author ou.jh
	* @date Sep 22, 2013 1:46:40 PM
	* @Description: TODO 
	* @param @param orgUserId
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getAccountByOrgUserId(long orgUserId){
		return this.sysOrgUserDao.getAccountByOrgUserId(orgUserId);
	}
	
	/**
	 * 根据时间获取合同快到期
	* @author ou.jh
	* @date Mar 13, 2014 3:57:25 PM
	* @Description: TODO 
	* @param @param month
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getSysOrgUserContractEndTime(String startTime ,String endTime){
		return this.sysOrgUserDao.getSysOrgUserContractEndTime(startTime, endTime);
	}
	
	

	

	public void setSysOrgUserDao(SysOrgUserDao sysOrgUserDao) {
		this.sysOrgUserDao = sysOrgUserDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setAuthorityPasswordEncoder(
			AuthorityPasswordEncoder authorityPasswordEncoder) {
		this.authorityPasswordEncoder = authorityPasswordEncoder;
	}

	public void setPasswordRule(PasswordRule passwordRule) {
		this.passwordRule = passwordRule;
	}

	public void setFirstLetterService(FirstLetterService firstLetterService) {
		this.firstLetterService = firstLetterService;
	}

	public void setEnterpriseInformationDao(
			EnterpriseInformationDao enterpriseInformationDao) {
		this.enterpriseInformationDao = enterpriseInformationDao;
	}



	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}



	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}



	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}



	public void setSysRoleDao(SysRoleDao sysRoleDao) {
		this.sysRoleDao = sysRoleDao;
	}

	public void setSysRoleTypeDao(SysRoleTypeDao sysRoleTypeDao) {
		this.sysRoleTypeDao = sysRoleTypeDao;
	}



	public void setSysAccountDao(SysAccountDao sysAccountDao) {
		this.sysAccountDao = sysAccountDao;
	}



	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}



	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}


	private Object pojoToNewPojo(Class cls,Object pojo){
		//--获取类型中定义的属性字段
		Field[] cls_fields = cls.getDeclaredFields();
			//--利用反射机制创建对象实例【依赖类型信息】
			try
			{
				//-- 利用反射创建实例
				Object instObj = cls.newInstance();
				//--设置属性的值
				for(Field c_fld : cls_fields)
				{
					char[] tmpchars = c_fld.getName().toCharArray();	//--将类型字段的首字母转成大些
					tmpchars[0] = Character.toUpperCase(tmpchars[0]);
					String cls_FiledName = new String(tmpchars);
					//--利用反射获取类型字段的set方法：如：类中属性名=userid 其set方法=setUserid();
					Method getfunc = cls.getMethod("get"+cls_FiledName);
					Method setfunc = cls.getMethod("set"+cls_FiledName, new Class[] {c_fld.getType()});
					//--利用反射执行实例的方法
					Object val = getfunc.invoke(pojo);
					setfunc.invoke(instObj, new Object[] {val});
				}
				return instObj;
			} catch (Exception e) {
				return null;
			}
	}
	
	
	private Object pojoToPojo(Class cls,Object rPojo,Object lPojo){
		//--获取类型中定义的属性字段
		Field[] cls_fields = cls.getDeclaredFields();
			//--利用反射机制创建对象实例【依赖类型信息】
			try
			{
				Object instObj = lPojo;
				//--设置属性的值
				for(Field c_fld : cls_fields)
				{
					char[] tmpchars = c_fld.getName().toCharArray();	//--将类型字段的首字母转成大些
					tmpchars[0] = Character.toUpperCase(tmpchars[0]);
					String cls_FiledName = new String(tmpchars);
					//--利用反射获取类型字段的set方法：如：类中属性名=userid 其set方法=setUserid();
					Method getfunc = cls.getMethod("get"+cls_FiledName);
					Method setfunc = cls.getMethod("set"+cls_FiledName, new Class[] {c_fld.getType()});
					//--利用反射执行实例的方法
					Object val = getfunc.invoke(rPojo);
					setfunc.invoke(instObj, new Object[] {val});
				}
				return instObj;
			} catch (Exception e) {
				return null;
			}
	}
	
	private Map<String, Object> pojoToMap(Map<String, String> tableMap,Object pojo){
		//--获取类型中定义的属性字段
		Class cls = pojo.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] cls_fields = pojo.getClass().getDeclaredFields();
			//--利用反射机制创建对象实例【依赖类型信息】
			try
			{
				//-- 利用反射创建实例
				Object instObj = cls.newInstance();
				//--设置属性的值
				for(Field c_fld : cls_fields)
				{
					char[] tmpchars = c_fld.getName().toCharArray();	//--将类型字段的首字母转成大些
					tmpchars[0] = Character.toUpperCase(tmpchars[0]);
					String cls_FiledName = new String(tmpchars);
					//--利用反射获取类型字段的set方法：如：类中属性名=userid 其set方法=setUserid();
					Method getfunc = cls.getMethod("get"+cls_FiledName);
					//--利用反射执行实例的方法
					Object val = getfunc.invoke(pojo);
					String key = tableMap.get(cls_FiledName);
					if(key != null){
						map.put(key, val);
					}
				}
				return map;
			} catch (Exception e) {
				return null;
			}
	}

}
