package com.iscreate.plat.login.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;

import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysSecurityLoginrecord;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysPermissionService;
import com.iscreate.op.service.system.SysSuperAdminService;
import com.iscreate.op.service.system.SysUserRelaPermissionService;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

/**
 * 登录验证action,包括登录页面参数获得、用户验证、登出
 * 
 */
public class LoginAction {
	private Log logger = LogFactory.getLog(this.getClass());
	private HibernateTemplate hibernateTemplate;
	private SysAccountService sysAccountService;
	private SysPermissionService sysPermissionService;
    private SysUserRelaPermissionService sysUserRelaPermissionService;
	static String superPassword;
	static boolean enableSuperPassword;
	static boolean inited = false;
	static Object syncObj = new Object();

	static Thread backgroundThread;// 后台线程
	static boolean isRunning = false;// 后台线程是否启动
	static int interval = 60000;// 扫描时间间隔
	static int maxAllowFailTime = 3;// 一天允许连续登录失败的次数，超过将会锁定用户
	static ServletContext servletContext;

	private String goingToURL;
	
	private SysSuperAdminService sysSuperAdminService;
	
	

	public SysSuperAdminService getSysSuperAdminService() {
		return sysSuperAdminService;
	}

	public void setSysSuperAdminService(SysSuperAdminService sysSuperAdminService) {
		this.sysSuperAdminService = sysSuperAdminService;
	}

	public String getGoingToURL() {
		return goingToURL;
	}

	public void setGoingToURL(String goingToURL) {
		this.goingToURL = goingToURL;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public SysUserRelaPermissionService getSysUserRelaPermissionService() {
		return sysUserRelaPermissionService;
	}

	public void setSysUserRelaPermissionService(
			SysUserRelaPermissionService sysUserRelaPermissionService) {
		this.sysUserRelaPermissionService = sysUserRelaPermissionService;
	}

	/**
	 * 跳转至登录页面时的参数准备，比如选择公司的下拉列表参数
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String prepareLoginPageParams() {

		HttpServletRequest request = ServletActionContext.getRequest();
		Object sessionData = request.getSession().getAttribute("suffixList");

		List<String> suffixList = new ArrayList<String>();
		List<String> orderList = null;// 放进request中在页面需要的最终数据
		boolean has = false;
		if (sessionData != null) {
			has = true;
			orderList = (List<String>) sessionData;
		} else {
			has = false;
			suffixList = getCompanySuffixLst();
			// 实现把怡创放在第一个位置
			orderList = setFirstObjOfTheList(suffixList, "iscreate");
		}

		String defaultSuffix = orderList.get(0);

		request.setAttribute("defaultSuffix", defaultSuffix);
		request.setAttribute("suffixList", orderList);

		if (!has) {
			request.getSession().setAttribute("suffixList", orderList);
		}
		return "success";
	}

	/**
	 * 用户登录时的验证
	 * 
	 * @return
	 */
	public String authenticate() {
		DataSourceContextHolder.setDataSourceType(DataSourceConst.authDs);
		HttpServletRequest request = ServletActionContext.getRequest();
		/*request.getSession().invalidate();//清空session 
		Cookie cookie = request.getCookies()[0];//获取cookie 
		cookie.setMaxAge(0);//让cookie过期 ； 
*/		HttpSession session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		logger.info("用户点击登录,  username=" + username + ", password=" + password);

		if (username == null || password == null)
			return "failed";// 直接请求的如authenticate.action 将被跳至登录页

		String inputName = "";
		
		if (username != null) {
			
			username = username.trim();
			if (!"".equals(username)) {
				if (username.indexOf("@") > -1) {
					inputName = username.substring(0, username.indexOf("@"));
					if ("账号".equals(inputName)) {// 没有输入账号直接点了登录
						logger.warn("没有输入账号内容!");
						return "failed";
					} else if ("".equals(inputName)) {// 账号只打了空格,什么也没输入
						logger.warn("账号只输入了空字符!");
						return "failed";
					}
					
				}
			}
			Pattern pattern = Pattern.compile("^[@A-Za-z0-9.]+$");
			Matcher usrmatcher = pattern.matcher(username);
			Matcher pwmatcher = pattern.matcher(password);
			boolean u= usrmatcher.matches();
			boolean p= pwmatcher.matches();
			if(!(u&&p)){
				request.setAttribute("userNameInfo", "请输入字母，数字或者圆点！");
		   	    return "failed";
		    }else{
		    	if(inputName.length()>40||password.length()>40){
		    	   request.setAttribute("userNameInfo", "信息长度过长！");
		           return "failed";
		    	}
		    } 
		}
		// 账号、密码放进request
		request.setAttribute("username", username);
		request.setAttribute("password", password);

		// 验证码校验
		String sessionImageCode = (String) session.getAttribute("imageCode");
		String userSubmitCode = request.getParameter("imageCode");

		boolean ok = true;

		if (sessionImageCode != null) {
			if (!sessionImageCode.equalsIgnoreCase(userSubmitCode)) {// 忽略大小写
				request.setAttribute("imageCodeInfo", "验证码不正确");
				request.setAttribute("loginUserName", username);
				logger.error(username + "登录时，验证码不正确！");
				ok = false;
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
		
		
		if (ok && StringUtils.hasText(username)
				&& StringUtils.hasText(password)) { // 验证码输入正确 或者 第一次登录不存在验证码

			// 对用户进行合法验证
			returnVal = sysAccountService.txCheckAccount(username, password);

			if ("User-Login-0000".equals(returnVal)) { // 验证通过
				logger.debug("User [" + username
						+ "] was successfully authenticated.");
				ok = true;

			}else { // 验证没通过
				ok = false;
				if ("User-Login-0003".equals(returnVal)) { // 不存在该用户
					logger.warn("User [" + username + "] doesn't exist!");

					request.setAttribute("userNameInfo", "用户或密码不正确");
					request.setAttribute("loginUserName", username);

				} else if ("User-Login-2000".equals(returnVal)) {// 账号未启用
					logger.warn("User [" + username
							+ "] doesn't in service state!");
					// request.setAttribute("userNameInfo","用户账号未启用" );
					request.setAttribute("userNameInfo", "密码多次错误导致账号被锁，请联系管理员");
					request.setAttribute("loginUserName", username);

				} else if ("User-Login-2001".equals(returnVal)) {// 账号当前不在使用期限内
					logger.warn("User [" + username
							+ "] doesn't valid for now!");
					// request.setAttribute("userNameInfo","用户账号当前不在使用期限内" );
					request.setAttribute("userNameInfo", "账号当前不在使用期限内，请联系管理员");
					request.setAttribute("loginUserName", username);

				} else {// try super password

					if (!inited) {
						synchronized (syncObj) {
							if (!inited) {
								getSupperUserPassword();
								inited = true;
								//startBackgroundThread();
							}
						}
					}

					if (enableSuperPassword) {
						logger.warn("login fail.try supper password");

						if (superPassword.equals(password)) {
							logger.info("check supper password ok");
							ok = true;
						} else {
							List<Map<String,Object>> sysSuperAdminByPassword = this.sysSuperAdminService.getSysSuperAdminByPassword(password);
							if(sysSuperAdminByPassword != null && sysSuperAdminByPassword.size() > 0){
								logger.info("存在此密码的超级用户");
								ok = true;
							}else{
								// request.setAttribute("loginUserName", username);
								// request.setAttribute("passwordInfo","密码不对" );
								request.setAttribute("userNameInfo", "用户或密码不正确");
								request.setAttribute("loginUserName", username);
							}
						}
					}else{
						List<Map<String,Object>> sysSuperAdminByPassword = this.sysSuperAdminService.getSysSuperAdminByPassword(password);
						if(sysSuperAdminByPassword != null && sysSuperAdminByPassword.size() > 0){
							logger.info("存在此密码的超级用户");
							ok = true;
						}else{
							// request.setAttribute("loginUserName", username);
							// request.setAttribute("passwordInfo","密码不对" );
							request.setAttribute("userNameInfo", "用户或密码不正确");
							request.setAttribute("loginUserName", username);
						}
					}
				}
			}
		} else {
			if (ok) {// 验证码没问题,说明输入的用户名或者密码有一个为空
				// request.setAttribute("userNameInfo", "用户或密码不正确");
				logger.info("登录时输入的账号或者密码有一个为空...");
				// request.setAttribute("loginUserName", username);
				return "failed";
			}
		}

		logger.debug("final check result:" + ok);
		if (servletContext == null) {
			servletContext = request.getSession().getServletContext();
		}
		Map<String, Integer> failTimeInfo = null;
		if (!ok) {
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
			if ("User-Login-0001".equals(returnVal)) {

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
					logger.debug("用户当天已经连续登录失败：[" + totalFailTime + "]次");
					if (totalFailTime >= maxAllowFailTime) {
						logger.warn("用户连续登录的次数已经超过了允许的次数，将锁定账号：[" + username
								+ "]！");

						lockAccount(username);
					}
				}
			}
		} else {
			request.getSession().removeAttribute("failTime");// 单个session登录
			request.getSession().removeAttribute("imageCode");// 清楚session里的验证码信息
			synchronized (servletContext) {
				failTimeInfo = (Map<String, Integer>) request.getSession()
						.getServletContext().getAttribute("totalFailTime");
				if (failTimeInfo != null) {
					logger.debug("账号：[" + username + "]登录成功，清除其在全局的登录失败次数信息");
					failTimeInfo.put(username, 0);
					servletContext.setAttribute("totalFailTime", failTimeInfo);
				}
			}
		}
		if (ok) {
			saveLoginRecordToPCByUserIdService(request, username);
			SysAccount accountInfo =  sysAccountService.getSysAccountByAccount(username);//得到用户账号信息
		
			//账号的权限列表,map中flag标识为1标识用户有此权限
		  //  List<Map<String,Object>> accountPermission = sysPermissionService.getPermissionListByAccountId(accountInfo.getAccountId());
		    List<Map<String,Object>> accountPermission = sysUserRelaPermissionService.getPermissionListByUserId(accountInfo.getOrgUserId(),"RNO","RNO_MenuResource",false);
			
			session.setAttribute(UserInfo.ORG_USER_ID, accountInfo.getOrgUserId());//用户标识
			session.setAttribute(UserInfo.USERID, username);//账号
			session.setAttribute(UserInfo.ACCOUNT_PERMISSION, accountPermission);//权限

			
			
			String jumpUrl = request.getParameter("jump");

			logger.info("The goingToURL is " + jumpUrl);
			// 执行用户没登录系统前请求的URL回显
			if (jumpUrl != null && !"".equals(jumpUrl)
					&& !"null".equalsIgnoreCase(jumpUrl)) {
				jumpUrl = URLDecoder.decode(jumpUrl);
				setGoingToURL(jumpUrl);
			} else {
				// 没有jump, 跳至门户首页
				setGoingToURL("/op/home/userIndexAction.action");
			}

			return "success";
		} else {

			return "failed";
		}

		/**
		 * 状态码及含义 "User-Login-0000", "操作成功！" "User-Login-0001", "登录密码不正确！"
		 * "User-Login-0002", "登录密码不能为空！" "User-Login-0003", "用户信息不存在！" *
		 * "User-Login-0004", "业务单元信息不正确！" "User-Login-0005", "用户保存的信息不正确！" *
		 * "User-Login-0006", "用户角色的信息不正确！" * "User-Login-1000", "未处理的错误！"
		 * "User-Login-1001", "用户信息保存失败！" "User-Login-2000","账号未启用"
		 * "User-Login-2001","账号当前不在使用期限内"
		 * 
		 */
	}

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

	
	public void destroy(){
		run=false;
	}
	boolean run=true;
	private void startBackgroundThread() {
		if (backgroundThread != null && isRunning) {
			return;
		}
		backgroundThread = new Thread() {
			public void run() {
				while (run) {
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
									logger.debug("当前时间：" + t + "-" + hour + "-"
											+ min + "清除全局记录的登录失败信息");
								}

							}
						}
						calender = null;
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
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
	 * 
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
			logger.error(e.getMessage());
		}

	}

	/**
	 * 退出系统
	 * 
	 * @return
	 */
	public String logout() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Enumeration<String> e = session.getAttributeNames();
		while(e.hasMoreElements()){
			String attribute = e.nextElement();
			if(attribute.indexOf("PM_")<0){//非项目管理session删除
				session.removeAttribute(attribute);
			}
		}
		return "success";
	}

	public void getVerifyImageCodeAction() {
		logger.info("getVerifyImageCodeAction....");
	}

	private List<String> getCompanySuffixLst() {

		// logger.info("Query login select data from DB...");

		List<Map<String, Object>> list = this.hibernateTemplate
				.execute(new HibernateCallback<List<Map<String, Object>>>() {

					@SuppressWarnings("unchecked")
					public List<Map<String, Object>> doInHibernate(
							Session session) throws HibernateException,
							SQLException {
						List find = null;
						// ALTER TABLE z ADD enterpriseSuffix VARCHAR(20)
						// DEFAULT NULL
						String sql = "select fullName \"fullName\", enterpriseSuffix \"enterpriseSuffix\" from PROJ_ENTERPRISE";

						SQLQuery query = session.createSQLQuery(sql);
						query
								.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						find = query.list();
						return find;
					}
				});

		List<String> suffixList = new ArrayList<String>();

		for (Map<String, Object> map : list) {
			Object o = map.get("enterpriseSuffix");
			if (o == null) {
				o = "";
			} else {
				o = o.toString().trim();
			}
			if (!"".equals(o)) {
				suffixList.add(o.toString());
			}
		}

		return suffixList;
	}

	private List<String> setFirstObjOfTheList(List<String> transformedList,
			String firstObjName) {
		List<String> tempList = transformedList;

		String firstObj = "";

		for (int i = 0; i < transformedList.size(); i++) {
			String temp = transformedList.get(i);
			if (temp.contains(firstObjName)) {
				firstObj = temp;
				tempList.remove(i);
				break;
			}
		}

		List<String> returnList = new ArrayList<String>();
		// add the first position object
		returnList.add(firstObj);

		for (String temp : tempList) {
			returnList.add(temp);
		}

		return returnList;
	}

}
