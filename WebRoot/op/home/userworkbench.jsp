<%@ page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>

<script language="javascript">
	$(function(){
		$(".email_list li").hover(function(){
			$(this).addClass("hover")
			},function(){
				$(this).removeClass("hover")
		});
	});

function loadURL(url,parameter,permission_id,name){
	var url = "../../../" + url.replace(/\s+/g,"");
	if(parameter != null && parameter != ""){
		url = url + "?" +  parameter + "&permission_id=" + permission_id + "&workzonesiteName="+name;
	}else{
		url = url;
	}
	window.open(url);
}


</script>

<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<style>
.menuList{margin-top:10px;}
.menuList li{cursor:pointer; border-bottom:1px solid #fff; background:url('../../images/forum.gif') 20px 6px no-repeat; padding-left:42px;line-height:32px; }
.menuList li:hover{border-bottom:1px solid #ccc; background:#eee url('../../images/forum.gif') 6px 6px no-repeat;}

</style>

</head>


<body>
							<div class="menuList">
								<em class="menu_arrow"></em>
									<ul>
									<s:iterator value="menuLinks" var="map">
										<li onclick="loadURL('<s:property value="#map.URL" />','<s:property value="#map.PARAMETER" />','<s:property value="#map.PERMISSION_ID" />','<s:property value="#map.NAME" />')"><s:property value="#map.NAME" /></li>
									</s:iterator>
									</ul>
							</div>
<body>

</html>