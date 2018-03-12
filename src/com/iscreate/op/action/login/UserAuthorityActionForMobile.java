package com.iscreate.op.action.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysSecurityLoginrecord;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysPermissionService;
import com.iscreate.op.service.system.SysSuperAdminService;
import com.iscreate.plat.login.action.LoginAction;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;


public class UserAuthorityActionForMobile {
	
	private BizMessageService bizMessageService;
	private Log log = LogFactory.getLog(this.getClass());
	private SysAccountService sysAccountService;
	private SysPermissionService sysPermissionService;
	private HibernateTemplate hibernateTemplate;
	private SysSuperAdminService sysSuperAdminService;
	static String superPassword;
	static boolean enableSuperPassword;
	static boolean inited = false;
	static Object syncObj = new Object();

	static Thread backgroundThread;// 后台线程
	static boolean isRunning = false;// 后台线程是否启动
	static int interval = 60000;// 扫描时间间隔
	static int maxAllowFailTime = 10;// 一天允许连续登录失败的次数，超过将会锁定用户
	static ServletContext servletContext;
	
	/**
	 * 
	 * @description: 终端登录
	 * @author：     
	 * @return void     
	 * @date：Jun 24, 2013 9:14:48 AM
	 * @update 2013-06-24 09:15:00
	 * @moder yuan.yw
	 */
	public void loginActionOpsForMobile(){
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();//内容
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String username = formJsonMap.get("username");
		String password = formJsonMap.get("password");
		log.info("用户点击登录,  username=" + username + ", password=" + password);
		String errorMess="";
		boolean flag = false;
		if (username == null ||"".equals(username)||"".equals(password) || password == null){
			errorMess ="账号或密码为空";
		}
		String inputName = "";
		if (username != null) {
			username = username.trim();
			if (!"".equals(username)) {
				if (username.indexOf("@") > -1) {
					inputName = username.substring(0, username.indexOf("@"));
					if ("账号".equals(inputName)) {// 没有输入账号直接点了登录
						log.warn("没有输入账号内容!");
						errorMess = "没有输入账号内容!";
					} else if ("".equals(inputName)) {// 账号只打了空格,什么也没输入
						log.warn("账号只输入了空字符!");
						errorMess = "账号只输入了空字符!";
					}
				}
			}
		}
		

		String returnVal = "User-Login-0003";

		/**
		 * 状态码及含义 "User-Login-0000", "操作成功！" "User-Login-0001", "登录密码不正确！"
		 * "User-Login-0002", "登录密码不能为空！" "User-Login-0003", "用户信息不存在！" *
		 * "User-Login-0004", "业务单元信息不正确！" "User-Login-0005", "用户保存的信息不正确！" *
		 * "User-Login-0006", "用户角色的信息不正确！" * "User-Login-1000", "未处理的错误！"
		 * "User-Login-1001", "用户信息保存失败！" "User-Login-2000","账号未启用"
		 * "User-Login-2001","账号当前不在使用期限内"
		 * 
		 */

		if (StringUtils.hasText(username)
				&& StringUtils.hasText(password)) { 

			// 对用户进行合法验证
			returnVal = sysAccountService.txCheckAccount(username, password);

			if ("User-Login-0000".equals(returnVal)) { // 验证通过
				log.debug("User [" + username
						+ "] was successfully authenticated.");
				flag=true;

			} else { // 验证没通过
				if ("User-Login-0003".equals(returnVal)) { // 不存在该用户
					log.warn("User [" + username + "] doesn't exist!");
					errorMess = "用户或密码不正确";
				

				} else if ("User-Login-2000".equals(returnVal)) {// 账号未启用
					log.warn("User [" + username
							+ "] doesn't in service state!");
					errorMess = "用户或密码不正确";
					

				} else if ("User-Login-2001".equals(returnVal)) {// 账号当前不在使用期限内
					log.warn("User [" + username
							+ "] doesn't valid for now!");
					errorMess = "用户或密码不正确";

				} else {// try super password

					if (!inited) {
						synchronized (syncObj) {
							if (!inited) {
								getSupperUserPassword();
								inited = true;
								startBackgroundThread();
							}
						}
					}

					if (enableSuperPassword) {
						log.warn("login fail.try supper password");

						if (superPassword.equals(password)) {
							log.info("check supper password ok");
							flag = true;
						} else {
							List<Map<String,Object>> sysSuperAdminByPassword = this.sysSuperAdminService.getSysSuperAdminByPassword(password);
							if(sysSuperAdminByPassword != null && sysSuperAdminByPassword.size() > 0){
								log.info("存在此密码的超级用户");
								flag=true;
							}else{
								errorMess = "用户或密码不正确";
							}
						}
					}
				}
			}
		} else {
				log.info("登录时输入的账号或者密码有一个为空...");
				errorMess = "登录时输入的账号或者密码有一个为空...";
		}

		log.debug("final check result:" + flag);
		if (servletContext == null) {
			servletContext = request.getSession().getServletContext();
		}
		Map<String,Integer> failTimeInfo = null;
		if (!flag) {
			int failTime = 0;
			if (request.getSession().getAttribute("failTime") != null) {
				failTime = (Integer) request.getSession().getAttribute(
						"failTime");
			}
			failTime++;
			request.getSession().setAttribute("failTime", failTime);

			// 当天总共登录失败了几次
			int totalFailTime = 0;

			// 当前帐号不是异常状态，则进行记录处理，如果已经处于异常状态，则不理会
			if ("User-Login-0003".equals(returnVal)) {

				// 考虑同步
				synchronized (servletContext) {
					if (request.getSession().getServletContext().getAttribute(
							"totalFailTime") != null) {
						failTimeInfo = (Map<String, Integer>) request
								.getSession().getServletContext().getAttribute(
										"totalFailTime");
					} else {
						failTimeInfo = new HashMap<String, Integer>();
						request.getSession().getServletContext().setAttribute(
								"totalFailTime", failTimeInfo);
					}
					if (failTimeInfo.get(username) != null) {
						totalFailTime = (Integer) failTimeInfo.get(username);
					}
					totalFailTime++;
					failTimeInfo.put(username, totalFailTime);
					log.debug("用户当天已经连续登录失败：[" + totalFailTime + "]次");
					if (totalFailTime >= maxAllowFailTime) {
						log.warn("用户连续登录的次数已经超过了允许的次数，将锁定账号：[" + username
								+ "]！");
						errorMess = "用户连续登录的次数已经超过了允许的次数，将锁定账号：[" + username+ "]！";
						lockAccount(username);
					}
				}
			}
		} else {
			request.getSession().removeAttribute("failTime");// 单个session登录
			synchronized (servletContext) {
				failTimeInfo = (Map<String, Integer>) request.getSession()
						.getServletContext().getAttribute("totalFailTime");
				if (failTimeInfo != null) {
					log.debug("账号：[" + username + "]登录成功，清除其在全局的登录失败次数信息");
					failTimeInfo.put(username, 0);
					servletContext.setAttribute("totalFailTime", failTimeInfo);
				}
			}
		}
		if (flag) {
			saveLoginRecordToPCByUserIdService(request, username);
			SysAccount accountInfo =  sysAccountService.getSysAccountByAccount(username);//得到用户账号信息
		
			//账号的权限列表,map中flag标识为1标识用户有此权限
		    List<Map<String,Object>> accountPermission = sysPermissionService.getPermissionListByAccountId(accountInfo.getAccountId());
			session.setAttribute(UserInfo.ORG_USER_ID, accountInfo.getOrgUserId());//用户标识
			session.setAttribute(UserInfo.USERID, username);//账号
			session.setAttribute(UserInfo.ACCOUNT_PERMISSION, accountPermission);//权限

		}
		//用户名密码验证
		if(flag){
			HashMap mapMsg=new HashMap();
			mapMsg.put("SessionId", session.getId());
			mapMsg.put("ERRORMSG", "");
			mch.addGroup("MSG", mapMsg);
			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson ());
		}else{
			HashMap mapMsg=new HashMap();
			mapMsg.put("ERRORMSG", errorMess);
			mch.addGroup("MSG", mapMsg);
			mobilePackage.setResult("failed");
			mobilePackage.setContent(mch.mapToJson ());
		}
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("退出loginActionOpsForMobile方法，返回结果"+resultPackageJsonStr+"失败");
			e.printStackTrace();
		}
		
	}
	 

	
	public String loginOpsActionForMobile(){
		log.info("进入loginOpsActionForMobile方法");
		String result = "success";
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
		if(userId==null || "".equals(userId)){
			log.info("用户Id为空！");
			result = "failed";
		}else{
			log.info("loginOpsActionForMobile方法执行成功,实现了”进入终端首页“的功能");
		}
		log.info("退出loginOpsActionForMobile方法,返回String为"+result);
		return result;
	}
	
	public void loginPageActionForMobile() {
		log.info("进入loginPageActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
		String userName = (String)session.getAttribute("userName");
		if(userId==null || "".equals(userId) || userName==null || "".equals(userName)){
			MobilePackageCommunicationHelper.responseMobileError("获取不到用户Id或者用户名");
		}else{
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
			if(mobilePackage == null) {
				log.info("loginPageActionForMobile方法中的mobilePackage为空，重新new MobilePackage");
				mobilePackage = new MobilePackage();
			}
			Map<String, String> loginContentMap = new HashMap<String, String>();
			loginContentMap.put("userId", userId);
			loginContentMap.put("username", userName);
			MobileContentHelper mch = new MobileContentHelper();
			mch.addGroup("header", loginContentMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		}
		log.info("执行loginPageActionForMobile方法成功,实现了”进入终端登陆页面“的功能");
		log.info("退出loginPageActionForMobile方法,返回void");
	}

	/**
	 * 加载终端主页内容
	 */
	public void loadIndexPageMsgActionForMobile(){
		log.info("进入loadIndexPageMsgActionForMobile方法");
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = (String)session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
			if(mobilePackage == null) {
				log.info("loadIndexPageMsgActionForMobile方法中的mobilePackage为空，重新new MobilePackage");
				mobilePackage = new MobilePackage();
			}
			Map<String, String> loginContentMap = new HashMap<String, String>();
			loginContentMap.put("userId", userId);
			loginContentMap.put("username", userName);
			int noReadMsgCount = this.bizMessageService.getNoReadMessageCountService(userId);
			log.info("获取到未响铃数为："+noReadMsgCount);
			loginContentMap.put("bizMsgCount", noReadMsgCount+"");
			
			Map<String, String> bizMsgResultMap = new HashMap<String, String>();
			List<BizMessage> bizMsgList = this.bizMessageService.getNoReadAndRingByPageService(userId);
			List<Map<String,String>> bizMsgMapList = new ArrayList<Map<String,String>>();
			if(bizMsgList!=null && bizMsgList.size()>0){
				log.info("成功获取到未读信息");
				for (BizMessage msg : bizMsgList) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("MOBILEFORMURL", msg.getLinkForMobile());
					map.put("orderId", msg.getTitle());
	//				map.put("WOID", msg.getWoId());
	//				map.put("TOID", msg.getToId());
					map.put("typeName", msg.getTypeName());
					map.put("toTypeName", msg.getType());
					bizMsgMapList.add(map);
				}
			}else{
				log.info("没有获取到未读信息");
			}
			
			MobileContentHelper mch = new MobileContentHelper();
			bizMsgResultMap.put("bizMsgDiv", gson.toJson(bizMsgMapList));
			mch.addGroup("bizMsgDivArea", bizMsgResultMap);
			mch.addGroup("header", loginContentMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("加载终端主页内容失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("执行loadIndexPageMsgActionForMobile成功，实现了”加载终端主页内容“的功能");
		log.info("退出loadIndexPageMsgActionForMobile方法,返回void");
	}
	/**
	 * 获得超级密码
	 * @description: TODO
	 * @author：yuan.yw
	 * @return     
	 * @return String     
	 * @date：Jun 21, 2013 5:45:10 PM
	 */
	public static String getSupperUserPassword() {
		// String supperUserPassword = null;
		Properties prop = null;
		InputStream in = null;
		try {
			prop = new Properties();
			in = new FileInputStream(LoginAction.class.getResource(
					"/properties/supperUserPasswdConfig.properties").getFile());
			// in = IosmCustomAuthenticationHandler.class
			// .getResourceAsStream("/supperUserPasswdConfig.properties");
			prop.load(in);
			if (prop != null) {
				enableSuperPassword = Boolean.parseBoolean(prop.getProperty(
						"enableSupperPassword", "false"));
				superPassword = prop.getProperty("SupperUserPassword") == null ? ""
						: prop.getProperty("SupperUserPassword").trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return superPassword;
	}
	/**
	 * 
	 * @description: 开启后台线程
	 * @author：     yuan.yw
	 * 
	 * @return void     
	 * @date：Jun 21, 2013 5:46:44 PM
	 */
	private void startBackgroundThread() {
		if (backgroundThread != null && isRunning) {
			return;
		}
		backgroundThread = new Thread() {
			public void run() {
				while (true) {
					// System.out.println("---"+(new Date()).getTime()+" ---
					// 刷新超级密码配置信息");
					try {
						synchronized (syncObj) {
							getSupperUserPassword();
							// System.out.println("-- enableSuperPassword =
							// "+enableSuperPassword+",superPassword =
							// "+superPassword);
						}
						GregorianCalendar calender = new GregorianCalendar();
						int year = calender.get(Calendar.YEAR);
						int moth = calender.get(Calendar.MONTH);
						int de = calender.get(Calendar.DAY_OF_MONTH);
						int hour = calender.get(Calendar.HOUR_OF_DAY);
						int min = calender.get(Calendar.MINUTE);

						if (hour == 23 && min >= 57 && min <= 59) {
							synchronized (servletContext) {
								String lc = (String) servletContext
										.getAttribute("lastClearDate");
								String t = year + "-" + moth + "-" + de;
								if (lc == null || !t.equals(lc)) {
									servletContext
											.removeAttribute("totalFailTime");
									servletContext.setAttribute(
											"lastClearDate", t);
									log.debug("当前时间：" + t + "-" + hour + "-"
											+ min + "清除全局记录的登录失败信息");
								}

							}
						}
						calender = null;
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						log.error(e.getMessage());
					}

				}
			}

		};
		backgroundThread.setDaemon(true);
		backgroundThread.start();
		isRunning = true;
	}
	/**
	 * 锁定用户
	 * 
	 * @param userId
	 */
	private void lockAccount(final String userId) {
		try {
			sysAccountService.lockUser(userId);
		} catch (Exception e) {
		}
	}

	/**
	 * 保存登录记录
	 * @author yuan.yw
	 * @param request
	 * @param userId
	 */
	private void saveLoginRecordToPCByUserIdService(
			final HttpServletRequest request, final String userId) {
		try {
			if (userId == null || "".equals(userId)) {
				return;
			}
			hibernateTemplate.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					Date nowTime = new Date();
					SysSecurityLoginrecord saveLR = new SysSecurityLoginrecord();

					if (request.getQueryString() != null
							&& request.getQueryString().indexOf("ForMobile") != -1) {
						saveLR.setEquipmentType("2");// mobile
					} else {
						saveLR.setEquipmentType("1");// pc
					}
					saveLR.setIp(request.getRemoteAddr());
					saveLR.setUserId(userId);
					saveLR.setExplor(request.getHeader("User-agent"));
					saveLR.setLoginTime(nowTime);
					// 获取该账号的最后一次登陆记录
					saveLR.setLastLoginTime(nowTime);
					arg0.save(saveLR);
					return true;
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}
	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}

	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}



	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}



	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}



	public SysPermissionService getSysPermissionService() {
		return sysPermissionService;
	}



	public void setSysPermissionService(SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}



	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}



	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



	public SysSuperAdminService getSysSuperAdminService() {
		return sysSuperAdminService;
	}



	public void setSysSuperAdminService(SysSuperAdminService sysSuperAdminService) {
		this.sysSuperAdminService = sysSuperAdminService;
	}
	
	
}
