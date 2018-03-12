<%@ page contentType="text/html; charset=utf-8" %>


<%

String userId=(String)request.getAttribute("userId");
String type = (String)request.getAttribute("type");
String workzonesiteName=request.getAttribute("workzonesiteName")==null?"":request.getAttribute("workzonesiteName").toString();
String workzoneUrl=request.getAttribute("workzoneUrl")==null?"":request.getAttribute("workzoneUrl").toString();
String isNewUrl=request.getAttribute("isNewUrl")==null?"":request.getAttribute("isNewUrl").toString();
String permission_id =request.getAttribute("permission_id")==null?"":request.getAttribute("permission_id").toString();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>您好!<%=userId %>|通用首页</title>
<%@include file="common.jsp"%>
<meta http-equiv=Content-Type content=text/html;charset=utf-8>

<link href="jslib/extjs/plugin/forum/forum.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

var workzonesiteName=<%=workzonesiteName%>
var workzoneUrl=<%=workzoneUrl%>
var isNewUrl=<%=isNewUrl%>

</script>
<script type="text/javascript" src="../ops/jslib/jquery/jquery-1.6.2.min.js"></script>
<script src="op/workmanage/jslib/workManagePageIndex.js" type="text/javascript"></script>

</head>
<body>
<input type="hidden" id="type" name="type" value="<%=type%>"/>
<input type="hidden" id="permission_id" name="type" value="<%=permission_id%>"/>
</body>
</html>