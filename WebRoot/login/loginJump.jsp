<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String service = request.getParameter("jump");
	if(service!=null&&service.indexOf("ForMobile")>0){
%>
<%@ include file="/login/mobileLoginView.jsp"%>
<%
	}else{
%>
<%@ include file="/login/loginView.jsp"%>
<%
	}
	
%>