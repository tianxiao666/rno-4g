<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>修改密码</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="../../css/base.css" />
<link rel="stylesheet" href="../../css/public.css" />
<link rel="stylesheet" href="../../css/public-table.css" />
<link rel="stylesheet" href="css/personal_info.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js">
</script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>

<script type="text/javascript" src="jslib/changepassword.js"></script>

  </head>
  
  <body>
     <div id="header">
        <div class="header-top"><h2>用户账号密码修改</h2></div>
    </div>
    
    <div id="container">
        <div class="personal_info_title">
        	<h4>用户帐号密码：</h4>
        </div>
        <form id="changePasswordForm" action="saveNewAccountPasswordForAjaxAction" method="post">
            <div class="personal_info_content clearfix">
                <ul class="personal_info_ul">
                    <li>
                        <span class="li_left">输入原密码<em class="red">*</em>：</span>
                        <label><input type="password" name="oldPassword" id="a" class="required"/></label>
                    </li>
                    <li>
                        <span class="li_left">输入新密码<em class="red">*</em>：</span>
                        <label><input type="password" id="pwd1" name="newPassword" class="required"/></label>
                    </li>
                    <li>
                        <span class="li_left">重新输入新密码<em class="red">*</em>：</span>
                        <label><input type="password" name="again" id="pwd2" class="required"/></label>
                    </li>
                </ul>
            </div>
            <div class="personal_info_bottom clear">
                <input type="submit" value="保存" />&nbsp;&nbsp;&nbsp;
                <input type="reset" value="重置" />
            </div>
        </form>
    </div>
    
  </body>
</html>
