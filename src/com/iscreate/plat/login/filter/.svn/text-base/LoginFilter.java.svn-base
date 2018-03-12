package com.iscreate.plat.login.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.system.SysUserLocationServiceImpl;
import com.iscreate.plat.login.constant.UserInfo;

/**
 * 登录拦截过滤器,没有登录将跳至登陆页面
 * 
 */

public class LoginFilter implements Filter {
	private Log logger = LogFactory.getLog(this.getClass());

	static String loginAction = "userLogin";
	static String seperator = "/";
	
	static String pm_loginAction = "op/project/userLogin";

	static String pm_home = "op/project/proIndexAction";
	
	private String noAuthenticateKeyWords;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
//		HttpSession session1 = request.getSession();
//		session1.setAttribute(UserInfo.PM_ORG_USER_ID, 120);//用户标识
//		session1.setAttribute(UserInfo.ORG_USER_ID, 120);//用户标识
		String requestURL = (request.getRequestURL() == null ? "" : request
				.getRequestURL().toString());

		String uri = request.getRequestURI();
		// logger.info("request url is " + requestURL);
		/**
		 * 获取工程URL, 拼接登录URL
		 */
		// value --> ops
		String projectName = request.getContextPath().replace("/", "");

		// 工程域名前缀 如：http://www.iosm.cn
		String domainURL = requestURL.replace(uri, "");

		// 工程域名 如:http://www.iosm.cn/ops
		String projectURL = domainURL + seperator + projectName;

		

		// logger.info("domainURL = " + domainURL + "\n projectURL =" +
		// projectURL
		// + "\n loginURL = " + loginURL);

		/**
		 * 拼接请求中的参数
		 */
		// 地址栏请求链接
		String goingToUrl = requestURL;

		// 请求链接中带的参数
		String queryString = request.getQueryString();

		// 拼接请求链接 包含参数
		StringBuffer sb = new StringBuffer();

		sb.append(goingToUrl);

		if (queryString != null) {
			sb.append("?" + queryString);
		}
		goingToUrl = URLEncoder.encode(sb.toString());
		/**
		 * 获取session,判断是否登录
		 */
		HttpSession session = ((HttpServletRequest) req).getSession(false);
//		System.out.println(requestURL);
		//检验跳转PM项目
		boolean skipPMURL = skipPMURL(req, res, filter, requestURL, projectURL, goingToUrl, session);
		if(skipPMURL != false){
//			System.out.println("judgeSystemLogin");
			return;
		}
		//检验是否登录已经IOSM系统
		boolean judgeSystemLogin = judgeSystemLogin(req, res, filter, requestURL, projectURL, goingToUrl, session);
		if(judgeSystemLogin != false){
//			System.out.println("judgeSystemLogin");
			return;
		}
		//检验是否登录已经PM系统
		boolean judgePMLogin = judgePMLogin(req, res, filter, requestURL, projectURL,goingToUrl, session);
		if(judgePMLogin != false){
//			System.out.println("judgePMLogin");
			return;
		}
//		//访问system目录判断是否登录PM或者IOSM系统
		boolean judgeIOSMLogin = judgeIOSMLogin(req, res, filter, requestURL, projectURL,goingToUrl, session);
		if(judgeIOSMLogin != false){
//			System.out.println("judgeIOSMLogin");
			return;
		}
		//检验是否放行文件
		boolean judgeFileLogin = judgeFileLogin(req, res, filter, requestURL, projectURL, goingToUrl, session);
		if(judgeFileLogin != false){
//			System.out.println("judgeFileLogin");
			return;
		}
	}
	
	/**
	 * 检验是否登录已经IOSM系统
	* @author ou.jh
	* @date Sep 26, 2013 11:14:50 AM
	* @Description: TODO 
	* @param @param req
	* @param @param res
	* @param @param filter
	* @param @param requestURL
	* @param @param projectURL
	* @param @param goingToUrl
	* @param @param session
	* @param @throws IOException
	* @param @throws ServletException        
	* @throws
	 */
	private boolean judgeSystemLogin(ServletRequest req, ServletResponse res,
			FilterChain filter, String requestURL, String projectURL,
			 String goingToUrl, HttpSession session)
			throws IOException, ServletException {
		if(requestURL != null && (requestURL.indexOf("op/system") >= 0 || requestURL.indexOf("op/sysdictionary") >= 0 || requestURL.indexOf("op/organization") >= 0)  && !isPass(requestURL)){
			String pmUserId = "";
			String iosmUserId = "";
			if (session != null && session.getAttribute(UserInfo.PM_ORG_USER_ID) != null) {
				pmUserId = session.getAttribute(UserInfo.PM_ORG_USER_ID).toString();
			}else{
				pmUserId = "";
			}
			if (session != null && session.getAttribute(UserInfo.ORG_USER_ID) != null) {
				iosmUserId = session.getAttribute(UserInfo.ORG_USER_ID).toString();
			}else{
				iosmUserId = "";
			}
			if(pmUserId.equals("") && iosmUserId.equals("")){
				judgeIOSMLogin(req, res, filter, requestURL, projectURL, goingToUrl, session);
				return true;
			}else{
				filter.doFilter(req, res);
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 检验是否登录已经PM系统
	* @author ou.jh
	* @date Sep 26, 2013 11:14:06 AM
	* @Description: TODO 
	* @param @param req
	* @param @param res
	* @param @param filter
	* @param @param requestURL
	* @param @param projectURL
	* @param @param goingToUrl
	* @param @param session
	* @param @throws IOException
	* @param @throws ServletException        
	* @throws
	 */
	private boolean judgePMLogin(ServletRequest req, ServletResponse res,
			FilterChain filter, String requestURL, String projectURL,
			 String goingToUrl, HttpSession session)
			throws IOException, ServletException {
		if(requestURL != null && 
				(requestURL.indexOf("/upload") >= 0 || requestURL.indexOf("op/project") >= 0 || requestURL.indexOf("op/cardispatch") >= 0)
				&& !isPass(requestURL)){
			String loginURL = projectURL + seperator + pm_loginAction;
			String userId = "";
			
			if (session != null && session.getAttribute(UserInfo.PM_ORG_USER_ID) != null) {
				userId = session.getAttribute(UserInfo.PM_ORG_USER_ID).toString();
			} else {
			}
			if(userId == null || userId.equals("")){
				if (requestURL.contains(pm_loginAction)
						|| requestURL.contains("/authenticate")
						|| requestURL.contains("/login/")// jsp页面
						|| requestURL.contains("jslib")// js
						|| requestURL.contains("getVerifyImageCodeAction")
						|| requestURL.contains("/logoutAction")
						|| requestURL.contains("jslib")
						) {
					filter.doFilter(req, res);
					return true;
				} else{
					((HttpServletResponse) res).sendRedirect(loginURL
							+ "?jump=" + goingToUrl);
					return true;
				}
			}else{
				filter.doFilter(req, res);
				return true;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 检验跳转PM项目
	* @author ou.jh
	* @date Sep 26, 2013 11:14:06 AM
	* @Description: TODO 
	* @param @param req
	* @param @param res
	* @param @param filter
	* @param @param requestURL
	* @param @param projectURL
	* @param @param goingToUrl
	* @param @param session
	* @param @throws IOException
	* @param @throws ServletException        
	* @throws
	 */
	private boolean skipPMURL(ServletRequest req, ServletResponse res,
			FilterChain filter, String requestURL, String projectURL,
			 String goingToUrl, HttpSession session)
			throws IOException, ServletException {
		if(requestURL != null && requestURL.indexOf(projectURL+"/pm") >= 0 && !isPass(requestURL)){
			String loginURL = projectURL + seperator + pm_home;
					((HttpServletResponse) res).sendRedirect(loginURL);
					return true;
		}else{
			return false;
		}
	}

	/**
	 * 访问system目录判断是否登录PM或者IOSM系统
	* @author ou.jh
	* @date Sep 26, 2013 11:14:43 AM
	* @Description: TODO 
	* @param @param req
	* @param @param res
	* @param @param filter
	* @param @param requestURL
	* @param @param projectURL
	* @param @param goingToUrl
	* @param @param session
	* @param @throws IOException
	* @param @throws ServletException        
	* @throws
	 */
	private boolean judgeIOSMLogin(ServletRequest req, ServletResponse res,
			FilterChain filter, String requestURL, String projectURL,
			String goingToUrl, HttpSession session)
			throws IOException, ServletException {
		// 登录的链接 如:http://www.iosm.cn/ops/userLogin
		String loginURL = projectURL + seperator + loginAction;
		if(requestURL != null && requestURL.indexOf("op/project") < 0  && !isPass(requestURL)){
			String userId = "";
			if (session != null) {
				userId = (String) session.getAttribute(UserInfo.USERID);
			} else {
				// logger.info("filter: session is NULL...");
			}
			if(requestURL.contains("ForMobile")){//保存终端当前登录位置
				new SysUserLocationServiceImpl().saveSysUserLocationServiceForMobile(req,res);
			}
			if (userId == null || "".equals(userId)) {
				// 配置文件中的例外请求 都放行,不需要登录?
				// http://127.0.0.1:8080/ops/resourcewebservice/getResByAreasRecursionAction?areaId=1492&selectReType=GeneralBaseStation
				if ((this.noAuthenticateKeyWords != null)
						&& (!"".equals(this.noAuthenticateKeyWords))) {
					if (this.noAuthenticateKeyWords.indexOf(";") > 0) {
						String[] noAuthenticateKeyWordss = this.noAuthenticateKeyWords
								.split(";");
						if (noAuthenticateKeyWordss != null) {
							for (String keyWord : noAuthenticateKeyWordss)
								if (requestURL.indexOf(keyWord) > 0) {
									filter.doFilter(req, res);
									return true;
								}
						}
					} else if (requestURL.indexOf(this.noAuthenticateKeyWords) > 0) {
						filter.doFilter(req, res);
						return true;
					}
				}

				// 当session中不存在userId时, 只要是跟登录操作有关的请求全部放行
				if (requestURL.contains(loginAction)
						|| requestURL.contains(projectURL + "/authenticate")
						|| requestURL.contains(projectURL + "/login/")// jsp页面
						|| requestURL.contains("jslib")// js
						|| requestURL.contains("getVerifyImageCodeAction")
						|| requestURL.contains(projectURL + "/logoutAction")) {
					filter.doFilter(req, res);
					return true;
				} else if(!requestURL.contains(".apk") &&!requestURL.contains("getTerminalUpdateVersionInfoActionForMobile") &&!requestURL.contains("loginActionOpsForMobile") && requestURL.contains("ForMobile")){//终端访问 登录超时 yuan.yw add  2013-06-26
					res.setCharacterEncoding("UTF-8");
					res.setContentType("text/json");			
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					Map<String,Object> result = new HashMap<String,Object>();
					result.put("loginFlag", "false");
					result.put("loginInfo", "登录已超时");
					String resultPackageJsonStr = gson.toJson(result);
					try {
						res.getWriter().write(resultPackageJsonStr);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				}else {
					// 访问的是工程URL,不带jump参数
					if (requestURL.equals(projectURL)
							|| requestURL.equals(projectURL + seperator)) {
						((HttpServletResponse) res).sendRedirect(loginURL);
					}else if(requestURL.contains(".apk") || requestURL.contains("getTerminalUpdateVersionInfoActionForMobile")){
						filter.doFilter(req, res);
						return true;
					} else {
						// 当用户请求了需要登录才能访问的URL时, 重定向到登录页面,URL带上 jump参数(user请求的链接)
						((HttpServletResponse) res).sendRedirect(loginURL
								+ "?jump=" + goingToUrl);
						return true;
					}
				}

			} else {
				// 登录成功后,在浏览器地址栏敲登录的链接,将不再显示登录页面,而是显示门户首页信息
				if (requestURL.contains(loginAction)
						|| requestURL.contains(projectURL + "/authenticate") || requestURL.equals(projectURL) || requestURL.equals(projectURL + "/")) {
					((HttpServletResponse) res)
							.sendRedirect("op/home/userIndexAction.action");
					return true;
				} else {
					filter.doFilter(req, res);
					return true;
				}
			}
		}else{
			return false;
		}
		return false;
		
	}
	
	/**
	 * 检验是否放行文件
	* @author ou.jh
	* @date Sep 26, 2013 11:14:06 AM
	* @Description: TODO 
	* @param @param req
	* @param @param res
	* @param @param filter
	* @param @param requestURL
	* @param @param projectURL
	* @param @param goingToUrl
	* @param @param session
	* @param @throws IOException
	* @param @throws ServletException        
	* @throws
	 */
	private boolean judgeFileLogin(ServletRequest req, ServletResponse res,
			FilterChain filter, String requestURL, String projectURL,
			 String goingToUrl, HttpSession session)
			throws IOException, ServletException {
		if(requestURL != null && isPass(requestURL)){
				filter.doFilter(req, res);
				return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 文件放行
	* @author ou.jh
	* @date Sep 26, 2013 1:53:58 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	private boolean isPass(String url){
		if(url == null){
			return false;
		}else if(url.contains("/js") || url.contains("/jslib") || url.contains("/css") || url.contains("/image")){
			return true;
		}else{
			return false;
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		Properties prop = null;
		InputStream in = null;
		try {
			prop = new Properties();
			in = getClass().getResourceAsStream(
					"/properties/NoAuthenticate.properties");
			prop.load(in);
			if (prop != null) {
				this.noAuthenticateKeyWords = (prop
						.getProperty("NoAuthenticate.KeyWord") == null ? null
						: prop.getProperty("NoAuthenticate.KeyWord").trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("IO 读写异常!");
		}

	}

}