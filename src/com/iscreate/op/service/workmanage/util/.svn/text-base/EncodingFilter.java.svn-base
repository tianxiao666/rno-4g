package com.iscreate.op.service.workmanage.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;


public class EncodingFilter extends StrutsPrepareAndExecuteFilter implements Filter {
	
	private static final String CHARACTER_ENCODING = "utf-8";
	
	private static final Log logger = LogFactory.getLog(EncodingFilter.class);
	

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		request.setCharacterEncoding(CHARACTER_ENCODING);
		logger.info("response=="+response.getCharacterEncoding());
		if(response.getCharacterEncoding()==null){
			response.setContentType("text/html;charset=" + CHARACTER_ENCODING);
			response.setCharacterEncoding(CHARACTER_ENCODING);			
		}
		
		chain.doFilter(request, response);
	}

	
	@Override
	public void destroy() {
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}