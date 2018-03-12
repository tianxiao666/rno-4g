<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.iscreate.sso.session.UserInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>个人信息</title>
<link rel="stylesheet" type="text/css" href="../../../css/base.css" />
<link rel="stylesheet" type="text/css" href="css/person_info.css" />
<script type="text/javascript">
	function updateUserInfo(){
		window.open("/ops/op/selfservice/loadSelfServiceInfoViewAndEditAction");
	}
	function updateUserPassword(){
		window.open("/ops/op/selfservice/loadSelfServiceChangePasswordAction");
	}
</script>
</head>
<body>
<%
String userId = (String) session
			.getAttribute(UserInfo.CAS_FILTER_USERID);
	String userName = (String) session
			.getAttribute(UserInfo.CAS_FILTER_USERNAME);

 %>
<div class="person_info">
	<div class="person_img">
    	<img src="images/man.jpg" height="100" width="100" />
    </div>
    <div class="person_text">
    	<table>
        	<tr>
            	<td>姓名：</td>
            	<td><s:property value="map.name" /></td>
            </tr>
        	<tr>
            	<td>组织：</td>
            	<td title='<s:property value="map.orgName" />'><s:property value="map.orgName" /></td>
            </tr>
        	<tr>
            	<td>职务：</td>
            	<td title='<s:property value="map.orgRoleName" />'><s:property value="map.orgRoleName" /></td>
            </tr>
            <tr>
            	<td colspan="2"><input type="button" class="blue_button" value="修改信息" onclick="updateUserInfo();" /></td>
            </tr>
            <tr>
            	<td colspan="2"><input type="button" class="blue_button" value="修改密码" onclick="updateUserPassword();" /></td>
            </tr>
        </table>
    </div>
</div>
<div class="login_info">
	<h4>
    	登陆：<span><s:property value="map.loginCount" />次</span>
    	积分：<span><s:property value="map.integral" />分</span>
    </h4>
    
    <p>
    	<s:if test="map.lastLoginTime != null && map.lastLoginTime!='' ">上次登陆时间：<s:property value="map.lastLoginTime" /><br /></s:if>
	    <s:if test="map.ip != null && map.ip!='' ">地址：<s:property value="map.ip" /></s:if>
	</p>
</div>
</body>
</html>