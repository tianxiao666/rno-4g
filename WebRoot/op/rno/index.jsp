<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>







<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>怡创网优云服务平台</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="">
<meta http-equiv="description" content="">

<%@include file="commonheader.jsp"%>

<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css"
	href="../home/resource/css/portal-style.css">
<link rel="stylesheet" type="text/css"
	href="../home/resource/jquery-easyui/themes/default/easyui.css">
<script type="text/javascript"
	src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript"
	src="../home/resource/js/jquery.corner.js"></script>

<script type="text/javascript"
	src="../home/resource/jquery-easyui/jquery.easyui.min.js"></script>

<script type="text/javascript" src="../home/resource/navigation/navi.js"></script>
<script type="text/javascript" src="js/index.js"></script>

<link href="../home/resource/navigation_tab_plugin/css/TabPanel.css"
	rel="stylesheet" type="text/css" />


<style>
/********** 头部 **********/
body {
	font-family: '微软雅黑', Verdana, Geneva, sans-serif
}

.header {
	padding: 0px 10px;
	z-index: 1;
	background: url(../home/resource/images/logo_bg.png) no-repeat;
	margin: 0px auto;
	height: 70px;
	position: relative;
	overflow: hidden;
}

.header_nav {
	position: absolute;
	right: 0px;
	top: 0px;
	height: 30px;
	width: 450px;
}

.logo {
	float: left;
	padding-top: 4px;
	cursor: pointer;
}

.system_name {
	float: left;
	padding-top:10px
}

.now_time {
	position: absolute;
	right: 10px;
	top: 45px;
	font-size: 14px;
}

.jizhong {
	height: 30px;
	line-height: 30px;
	padding-left: 38px;
	vertical-align: top;
	background: url(../home/resource/images/jizhong.png) 6px 4px no-repeat;
}

.tool_bar {
	position: absolute;
	right: 10px;
	top: 0px;
}

.tool_bar span {
	display: inline-block;
	margin-left: 5px;
	line-height: 30px;
	padding-left: 22px;
	cursor: pointer;
	font-size: 12px;
}

.tool_bar span:hover {
	color: #00F;
}

.tool_bar span.message_box {
	background: url(../home/resource/images/message_box.png) 0px 8px
		no-repeat;
}

.tool_bar span.full_screen {
	background: url(../home/resource/images/full_screen.png) 0px 8px
		no-repeat;
	color: #C00;
	font-weight: bold;
}

.tool_bar span.setting {
	background: url(../home/resource/images/setting.png) 0px 8px no-repeat;
}

.tool_bar span.quit {
	background: url(../home/resource/images/quit.png) 0px 8px no-repeat;
}

/********** 主体tab **********/
.main_nav {
	width: 100%;
	margin: 0 auto;
	height: 26px;
	background-position: 0px 0px;
	background: url(../home/resource/images/nav_back.png) 0 0 repeat-x;
}

.firstLevelMenu{
	width: 130px;
	float: left;
	line-height: 26px;
	text-align: center;
	font-size:14px;
	color: #fff;
	background: #fff url(../home/resource/images/nav_back.png) 0 0 repeat-x;
	cursor: pointer;
	display:block
}

.secondLevelMenu{
	font-size:12px;
	padding-left:5px;
	text-align:left;
	color:#000000;
	background:url('../home/resource/images/secondLevelMenu3.png') repeat-x scroll 0 0 #FFFFFF;
}

.firstLevelMenuselected{
	background:url('../home/resource/images/menu2.gif') repeat-x scroll 0 0 #FFFFFF;
}

.secondLevelMenuselected{
	background:url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF;
}

.firstLevelMenuOn{
	background:url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF;
}

.secondLevelMenuOn{
	background:url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF;
}

.main_nav li.main_nav_on {
	background-position: 0px -30px;
	color: #333;
	font-weight: bold;
}

/********** 主体 **********/
.content {
	padding: 4px;
	margin: 0px auto;
	overflow: hidden;
}

/********** footer ********/
.footer {
	width: 100%;
	height: 30px;
	padding: 8px 0px 0px 0px;
	background: url(../home/resource/images/logo_bg.png) no-repeat;
	line-height: 24px;
	text-align: center;
	position: absolute;
	left: 0px;
	border-top: 1px solid #ccc;
	bottom: 0px;
	margin-top: 2px;
	box-shadow: -2px -2px 3px #aaa;
}

.go_to em,.info em {
	font-weight: bold;
	color: #333;
	margin-right: 4px;
}

.go_to,.info {
	color: #666;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
		//初始化门户主体高度
		windowsResize();
		onLoadOpenHomeTab();


	});
//浏览器大小变化时修改门户主体高度
$(window).resize(function() {
	if (window.timerLayout)
		clearTimeout(window.timerLayout);
	window.timerLayout = null;
	if ($.browser.msie) {
		window.timerLayout = setTimeout(windowsResize, 100);
	} else {
		window.timerLayout = setTimeout(windowsResize, 100);
	}
});

//获取门户主体高度
function windowsResize() {
		var docH = document.documentElement.clientHeight;
		$("#maindiv").css("height", docH - 95 - 40);
}

function openHomeTab(url,me){
	$("#showWorkZoneSiteListDiv li").removeClass("main_nav_on");
	$(me).addClass("main_nav_on");
	$("#maininnerdivif").attr("src",url);
}


function onLoadOpenHomeTab(){
	//$("#showWorkZoneSiteListDiv li").removeClass("main_nav_on");
	//$("#showWorkZoneSiteListDiv li").eq(0).click();
	//console.log("li节点的个数："+$('#showWorkZoneSiteListDiv > ul').length);
	//console.log($("#showWorkZoneSiteListDiv > ul > li").eq(0).children("ul").html());
	$("#showWorkZoneSiteListDiv > ul > li").eq(0).children("ul").children("li").eq(0).click();

}

function openHomeTabSelf(url,me){
	window.open(url);
}

</script>
</head>

<body style="overflow: hidden;">
	<%-- 头部导航 ######## begin  --%>
	<div class="header">
	    <%--<div class="logo">
			<img src="../home/resource/images/iosm-logo2.png" />
		</div> --%>
		<div class="system_name">
			<img src="login/images/isrno_logo.png" />
		</div>
<%-- 		<input type="button" value="TEST" 	style="display:block;position:relative; margin-left:40%; margin-top: 5px;" 
		onclick="$('#maininnerdivif').attr('src','../../../ops/op/rno/initRnoLteMapOperAction');"/> --%>
		<div class="header_nav">
			<div style="display:block; width:280px; height:30px;
						position:relative; margin-left:200px; margin-top: 5px;">
				<div id="userConfigDiv" style="solid #99bbe8; border-radius:5px; margin: 1px; padding: 0px; width: 360px; height: 33px;
									 position: absolute; top: 30px; left: -333px;z-index: 999;">
						<div style="margin-top:3px;"></div>
						<input id="saveUserConfigBtn" type="button" style="margin-top:1px;" value="设置默认"/>
						 &nbsp;默认区域：
						 <select id="provincemenu">
							<s:iterator value="provinceAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select>
						<select id="citymenu" name="attachParams['cityId']">
							<s:iterator value="cityAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select>
				</div>
			</div>
			<div class="tool_bar">
				<em class="f-bold">${sysOrgUser.name }，欢迎您！</em> <span class="quit"><a
					href="rnoLogoutAction.action">退出</a></span>
			</div>
		</div>
		<div class="now_time">
			<%-- 2012年12月14日 星期五 16:36:54 --%>
			<div id="nowTime"></div>
		</div>
	</div>


	<%-- 头部导航 ######## end  --%>


	<div id="container">

		<div class="logo_div" id="logo_div"></div>

		<div class="main_nav" id="showWorkZoneSiteListDiv" style="position:absolute;z-index:30;padding:0 5px">
 			<%-- <ul id="workZoneSiteHiddenDisplay_1318580408994"
				style='display:block'>
				<s:iterator value="permissionModuleList" var="map">
					<s:if
						test="#map.PARAMETER != null && #map.PARAMETER != 'null' && #map.PARAMETER != ''">
						<li
							onclick="javascript:openHomeTab('../../../<s:property value='#map.URL' />?<s:property value='#map.PARAMETER' />&permission_id=<s:property value='#map.PERMISSION_ID' />&workzonesiteName=<s:property value='#map.NAME' />',this)"><s:property
								value='#map.NAME' /></li>
					</s:if>
					<s:else>
						<li
							onclick="javascript:openHomeTab('../../../<s:property value='#map.URL' />?permission_id=<s:property value='#map.PERMISSION_ID' />&workzonesiteName=<s:property value='#map.NAME' />',this)"><s:property
								value='#map.NAME' /></li>
					</s:else>
				</s:iterator>
			</ul> --%>
			<%-- Liang YJ 2014-4-1 9:00 修改 --%>
			<%--  <ul class="main_nav li">
				<s:iterator value="menuList" var="map">
					 <s:if test="!#map.leaf">
					 	<li onmouseleave="hideChildrenMenu(this)"><div style="width:100%" onmouseenter="showChildrenMenu(this)" ><s:property value="#map.text"/></div>
							<ul id="<s:property value="#map.text"/>" style="display:none">
								<s:iterator value="#map.children" var="children">
								<li id="<s:property value="#children.text"/>" style="font-size:15px;color:#000000;background:url('../home/resource/images/secondLevelMenu3.png') repeat-x scroll 0 0 #FFFFFF" onclick="showContent('../../../<s:property value='#children.URL' />',this)" onmouseenter="changeMenuStyle(this)" onmouseleave="revertMenuStyle(this)"><s:property value="#children.text"/></li>
								</s:iterator>
							</ul>
						</li>
					</s:if>
				</s:iterator>
			</ul>  --%>
			<ul id="navigationMenu">
				<s:iterator value="menuList" var="map">
					 <s:if test="!#map.leaf">
					 	<li id="<s:property value="#map.text"/>" class="firstLevelMenu"><div><s:property value="#map.text"/></div>
							<ul style="display:none">
								<s:iterator value="#map.children" var="children">
								<li id="<s:property value="#children.text"/>" class="secondLevelMenu"><s:property value="#children.text"/><input type="hidden" value="../../../<s:property value='#children.URL' />"></li>
								</s:iterator>
							</ul>
						</li>
					</s:if>
				</s:iterator>
			</ul>
		</div>


		<%-- Liang YJ 2014-4-1 修改  之前有两个id id="workZonesiteFrame1318581008644" --%>
		<div id="maindiv" style="position:absolute;height:600px;top:100px;width:100%">
			<iframe scrolling="anto" id="maininnerdivif" frameborder="0" src=""
				style="width: 100%; height: 100%;"
				name="workZonesiteFrame1318581008644"> </iframe>
			<%-----各工作空间对应工作区--end-----%>

		</div>

	</div>
	<%-- ----友情链接及底部版权信息区--begin---- --%>
	<div id="footermaindiv">

		<div class="footer">
			<ul>
				<li class="info"><%=(String)session.getAttribute("projectName")+" "+(String)session.getAttribute("version")%>&nbsp;&nbsp; <a
					href="fileDownloadAction?fileName=rno4g_user_manual.zip"
					style="text-decoration: underline;color:#666; " target="_blank">
						使用说明书下载 </a> &nbsp;&nbsp; 维护热线020-28817300-201&nbsp; Copyright ©
					广东怡创科技股份有限公司 | <a href="http://www.miitbeian.gov.cn/"
					target="_blank" style="text-decoration: underline;color:#666; ">粤ICP备12023904号</a>&nbsp;&nbsp;
				</li>

			</ul>
		</div>
	</div>
	<%-- ----友情链接及底部版权信息区--end---- --%>

</body>
</html>
