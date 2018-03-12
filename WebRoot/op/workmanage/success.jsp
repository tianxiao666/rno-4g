<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%

	String infoTips=session.getAttribute("infoTips")==null?"":session.getAttribute("infoTips").toString();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>success</title>
  </head>
  
  <body>
  	<div>
    	<%
    		out.println(infoTips);
    	%>
    </div>
  </body>
</html>
