package com.iscreate.plat.login.filter;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.iscreate.plat.login.imageCode.*;

public class ImageCodeGeneratorFilter implements Filter {

	// 生成图片的类名
	static String imageCodeGeneratorClass = null;
	static ImageCodeGenerator imageCodeGenerator;
	static int codeLength = 4;
	static Log log=LogFactory.getLog(ImageCodeGeneratorFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (arg0 instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) arg0;
			HttpServletResponse response = (HttpServletResponse) arg1;
			String uri = request.getRequestURI();
			if (uri.endsWith("getVerifyImageCodeAction")) {
				log.debug("need verify code . 需要获取验证码");
				if (imageCodeGenerator == null) {
					log.error("not find verify code generator ! 未配置验证码生成器！");
					throw new ServletException("not find verify code generator ! 未配置验证码生成器！");
				}
				CodeContainer container = null;
				try{
					container=imageCodeGenerator.creatCode();
					log.debug("generate verify code successfully. 验证码获取成功");
					response.setHeader("Pragma", "No-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
					// 表明生成的响应是图片
					response.setContentType("image/jpeg");
					request.getSession(true).setAttribute("imageCode",
							container.getCheckCodeStr());
					log.debug("output verify code... 输出验证码...");
					ImageIO.write(container.getBuffImg(), "JPEG", response
							.getOutputStream());
				}catch(Exception e){
					log.error("fail to generate verify code ! 验证码获取失败！");
					e.printStackTrace();
					return;
				}
			} else {
				arg2.doFilter(arg0, arg1);
			}
		} else {
			arg2.doFilter(arg0, arg1);
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
		imageCodeGeneratorClass = arg0
				.getInitParameter("imageCodeGeneratorClass");
		if (imageCodeGeneratorClass != null) {
			try {
				imageCodeGenerator = (ImageCodeGenerator) Class.forName(
						imageCodeGeneratorClass).newInstance();

				try {
					if (arg0.getInitParameter("codeLength") != null) {
						this.codeLength = Integer.parseInt(arg0
								.getInitParameter("codeLength"));
					}
				} catch (Exception e) {
					codeLength = 4;
				}
				imageCodeGenerator.setCodeLength(codeLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (imageCodeGenerator == null) {
				throw new ServletException("无法初始化：" + imageCodeGeneratorClass
						+ " 类对象！");
			}
		}

	}

}
