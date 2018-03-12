package com.iscreate.op.action.rno;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.system.SysUserLocationServiceImpl;
import com.iscreate.plat.login.constant.UserInfo;

/**
 * 登录拦截过滤器,没有登录将跳至登陆页面
 * 
 */
public class RnoLoginFilter implements Filter {
	private Log logger = LogFactory.getLog(this.getClass());

	static String loginAction = "rnoUserLogin";
	static String seperator = "/";

	private String noAuthenticateKeyWords;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filter) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

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

		// 登录的链接 如:http://www.iosm.cn/ops/userLogin
		String loginURL = projectURL + seperator + "op"+seperator+"rno"+seperator+loginAction;

		// logger.info("domainURL = " + domainURL + "\n projectURL =" +
		// projectURL
		// + "\n loginURL = " + loginURL);

//		if(requestURL.contains("logout")){
//			logger.info("logout.");
//		}
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

		String userId = "";
		if (session != null) {
			userId = (String) session.getAttribute(UserInfo.USERID);
		} else {
			// logger.info("filter: session is NULL...");
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
								return;
							}
					}
				} else if (requestURL.indexOf(this.noAuthenticateKeyWords) > 0) {
					filter.doFilter(req, res);
					return;
				}
			}

			// 当session中不存在userId时, 只要是跟登录操作有关的请求全部放行
			if (requestURL.contains(loginAction)
					|| requestURL.contains("/rnoAuthenticate")
					|| requestURL.contains("/authenticate")
					|| requestURL.contains("/login/")// jsp页面
					|| requestURL.contains("jslib")// js
					|| requestURL.contains("getVerifyImageCodeAction")
					|| requestURL.contains(projectURL + "/logoutAction")) {
				filter.doFilter(req, res);
				return;
			}else if (requestURL.contains("/ana_result")) {
				filter.doFilter(req, res);
				return;
			} 
			else {
				// 访问的是工程URL,不带jump参数
				if (requestURL.equals(projectURL)
						|| requestURL.equals(projectURL + seperator)) {
					((HttpServletResponse) res).sendRedirect(loginURL);
				} else {
					// 当用户请求了需要登录才能访问的URL时, 重定向到登录页面,URL带上 jump参数(user请求的链接)
					((HttpServletResponse) res).sendRedirect(loginURL
							+ "?jump=" + goingToUrl);
				}
				return;
			}

		} else {
			// 登录成功后,在浏览器地址栏敲登录的链接,将不再显示登录页面,而是显示门户首页信息
			if (requestURL.contains(loginAction)
					|| requestURL.contains("/rnoAuthenticate") || requestURL.equals(projectURL) || requestURL.equals(projectURL + "/")) {
				((HttpServletResponse) res)
						.sendRedirect("op/rno/rnoUserIndexAction.action");
			} else {
				filter.doFilter(req, res);
			}
			return;
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
