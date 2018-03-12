<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <head>
    
    <title>信息发布</title>
    <link rel="stylesheet" href="../../css/public.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="../../css/base.css"/>
	<link rel="stylesheet" type="text/css" href="css/cms.css"/>
	<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>
	<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
	<link rel="stylesheet" href="../../css/base.css" type="text/css" />
	<link rel="stylesheet" href="css/cms.css" type="text/css" />
	<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
	<link href="../../jslib/dialog/dialog.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
	<script type="text/javascript" src="../js/leftMenu.js"></script>
	<script type="text/javascript" src="js/cmsIndex.js"></script>
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
  </head>
  
 <body>
	<%--主体开始--%>
	<div class="cms_main">
		<%--左边菜单栏开始--%>
		<div class="leftMenu">
			<div class="leftMenu_top">
				<span>信息发布管理</span>
			</div>
			<ul class="leftLevelMenu_info">
				<li id="addInfo" onclick="clickAddInfoItem(this)"><span></span>新发布</li>
				<li id="showInfo" onclick="clickApproveInfoRelease(this)" class="menu_selected"><span></span>信息列表</li>
			</ul>
		</div>
		<%--显示/隐藏左边栏--%>
		<div class="menu_title_tool">
			<div class="tool"></div>
		</div>
		<%--左边菜单栏结束--%>
		
		<%--右边工作区开始--%>
		<div class="cms_content" id="right_content">
			
		</div>
		<%--右边工作区结束--%>
	</div>
</body>
</html>