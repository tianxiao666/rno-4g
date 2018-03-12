<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
   
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">


	<script type="text/javascript">
	alert("操作失败");
	var urlAction=<%=request.getAttribute("urlAction")==null?"#":request.getAttribute("urlAction").toString()%>;
	window.opener.location.href=urlAction;
    window.close(); 
	</script>
  </head>
  
  <body>
  ${errorInfo}
  </body>
</html>
