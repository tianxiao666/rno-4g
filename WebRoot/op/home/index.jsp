<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags" %>







<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>门户首页</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="">
		<meta http-equiv="description" content="">

		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css"
			href="resource/css/portal-style.css">
		<link rel="stylesheet" type="text/css"
			href="resource/jquery-easyui/themes/default/easyui.css">
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<%--<script type="text/javascript" src="portal/resource/js/jquery-1.7.2.min.js"></script> 
	<link rel="stylesheet" type="text/css" href="portal/resource/jquery-ui/ui-lightness/jquery-ui-1.8.19.custom.css">
	<script type="text/javascript" src="portal/resource/js/jquery-ui-1.8.19.custom.min.js"></script> 
	 --%>
		<script type="text/javascript"
			src="resource/js/jquery.corner.js"></script>

		<script type="text/javascript"
			src="resource/jquery-easyui/jquery.easyui.min.js"></script>

		<script type="text/javascript"
			src="resource/navigation/navi.js"></script>
		<script type="text/javascript" src="js/index.js"></script>

		<link href="resource/navigation_tab_plugin/css/TabPanel.css"
			rel="stylesheet" type="text/css" />
	<style>
		/********** 头部 **********/
body{font-family:'微软雅黑',Verdana, Geneva, sans-serif}
.header{padding:0px 10px; z-index:1; background:url(resource/images/logo_bg.png) no-repeat; margin:0px auto; height:70px; position:relative;overflow:hidden;}
.header_nav{position:absolute; right:0px; top:0px; height:30px; width:450px;}
.logo{float:left; padding-top:4px; cursor:pointer;}
.system_name{float:left;}
.now_time{position:absolute; right:10px; top:45px; font-size:14px;}
.jizhong{height:30px; line-height:30px; padding-left:38px; vertical-align:top; background:url(resource/images/jizhong.png) 6px 4px no-repeat;}

.tool_bar{position:absolute; right:10px; top:0px;}
.tool_bar span{ display:inline-block;margin-left:5px; line-height:30px; padding-left:22px; cursor:pointer; font-size:12px;}
.tool_bar span:hover{ color:#00F;}
.tool_bar span.message_box{ background:url(resource/images/message_box.png) 0px 8px no-repeat;}
.tool_bar span.full_screen{background:url(resource/images/full_screen.png) 0px 8px no-repeat; color:#C00; font-weight:bold;}
.tool_bar span.setting{background:url(resource/images/setting.png) 0px 8px no-repeat;}
.tool_bar span.quit{background:url(resource/images/quit.png) 0px 8px no-repeat;}

/********** 主体tab **********/
.main_nav{width:100%; margin:0 auto; height:26px; background-position: 0px 0px; background:url(resource/images/nav_back.png) 0 0 repeat-x;}
.main_nav li{ width:88px;float:left; line-height:26px; text-align:center; color:#fff; background:#fff url(resource/images/nav_back.png) 0 0 repeat-x; cursor:pointer; }
.main_nav li.main_nav_on{background-position:0px -30px; color:#333; font-weight:bold;}

/********** 主体 **********/
.content{padding:4px; margin:0px auto; overflow:hidden;}

/********** footer ********/
.footer{ width:100%; height:30px; padding:8px 0px 0px 0px; background:url(resource/images/logo_bg.png) no-repeat; line-height:24px; text-align:center; position:absolute; left:0px; border-top:1px solid #ccc; bottom:0px; margin-top:2px; box-shadow:-2px -2px 3px #aaa;}
.go_to em,.info em{font-weight:bold; color:#333; margin-right:4px;}
.go_to,.info{color:#666;}
	
</style>

<script type="text/javascript">
$(function(){
	//初始化消息盒子
	$("#messageContentIframeSrc").val("http://"+window.location.host+"/ops/op/bizmsg/loadBizMessageAction");
		//初始化门户主体高度
		windowsResize();
		onLoadOpenHomeTab();
	})
	
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
	$("#showWorkZoneSiteListDiv li").eq(0).click();
}

function openHomeTabSelf(url,me){
	window.open(url);
}
</script>
</head>

	<body style="overflow: hidden;">
	<%-- 头部导航 ######## begin  --%>
		<div class="header">
    	<div class="logo">
        	<img src="resource/images/iosm-logo2.png" />
        </div>
    	<div class="system_name">
        	<img src="resource/images/iosm.png" />
        </div>
        <div class="header_nav">
            <div class="tool_bar">
            	<em class="f-bold">${sysOrgUser.name }，欢迎您！</em>
                <span style="cursor:pointer;" class="message_box" onclick="javascript:openMessageBox();">消息盒</span>
                <span class="setting"  onclick="javascript:gotoUrl('../selfservice/selfservicehomepage.jsp')">设置</span>
                <span class="quit"><a href="../../logoutAction.action">退出</a></span>
            </div>
        </div>
        <div class="now_time">
        	<%-- 2012年12月14日 星期五 16:36:54 --%>
        	<div id="nowTime">
				
			</div>
        </div>
    </div>
								
		
<%-- 头部导航 ######## end  --%>


<div id="container">

	
		<div class="logo_div" id="logo_div">

			
		</div>

		<div class="main_nav" id="showWorkZoneSiteListDiv">
			<ul id="workZoneSiteHiddenDisplay_1318580408994" style='display:block'>
			<s:if test="flag != true">
				<li class="main_nav_on" onclick="javascript:openHomeTab('homeIndex.jsp',this)">我的首页</li>
			</s:if>
			<s:iterator value="permissionModuleList" var="map">
				<s:if test="#map.FLAG == 1">
					<s:if test="#map.NAME == '集中调度'">
						<li onclick="javascript:openHomeTabSelf('../../../<s:property value='#map.URL' />',this)"><s:property value='#map.NAME' /></li>
				</s:if>
				<s:else>
					<s:if test="#map.PARAMETER != null && #map.PARAMETER != 'null' && #map.PARAMETER != ''">
						<li onclick="javascript:openHomeTab('../../../<s:property value='#map.URL' />?<s:property value='#map.PARAMETER' />&permission_id=<s:property value='#map.PERMISSION_ID' />&workzonesiteName=<s:property value='#map.NAME' />',this)"><s:property value='#map.NAME' /></li>
					</s:if>
					<s:else>
						<li onclick="javascript:openHomeTab('../../../<s:property value='#map.URL' />?permission_id=<s:property value='#map.PERMISSION_ID' />&workzonesiteName=<s:property value='#map.NAME' />',this)"><s:property value='#map.NAME' /></li>
					</s:else>
				</s:else>
				</s:if>
			</s:iterator>
			</ul>
		</div>



		<div id="maindiv" style=" height: 600px;">
				<iframe scrolling="anto" id="maininnerdivif" frameborder="0" src="" style="width: 100%; height: 100%;" id="workZonesiteFrame1318581008644" name="workZonesiteFrame1318581008644">
				</iframe>
			<%-----各工作空间对应工作区--end-----%>

		</div>

</div>
		<%-- ----友情链接及底部版权信息区--begin---- --%>
		<div id="footermaindiv">
			
			<div class="footer">
		    	<ul>
		        	<li class="info">
		            	智能化运维服务管理平台IOSM v2.0 &nbsp;&nbsp; <a href="../../download/qr_code.html"  style="text-decoration: underline;color:#666; " target="_blank"> 客户端APK下载 </a> &nbsp;&nbsp;  维护热线020-28817300-201&nbsp; Copyright © 广东怡创科技股份有限公司 |  <a href="http://www.miitbeian.gov.cn/" target="_blank" style="text-decoration: underline;color:#666; ">粤ICP备12023904号</a>&nbsp;&nbsp;  
		            </li>
		           
		        </ul>
		    </div>
		</div>
		<%-- ----友情链接及底部版权信息区--end---- --%>

		<%-- 2012-4-17 gmh 注释 begin --%>
		<%-- ----消息盒------------begin-------- --%>
		<%--
<div style="display:none;">
<div id="messgeBox_" title="我的消息盒" style="padding:0px;top:30px;width:400px;height:240px;">	
<p align="center" style="font-size:16px;"">我的消息盒</p>
<div align="center">
<fieldset style="padding:5px 5px 5px 5px;width:300px;height:100px;text-align:left;font-size:16px;">	  	
	新的待办任务<span id="newPendingTaskOrderCount"></span><br>
	新的待办工单<span id="newPendingWorkOrderCount"></span><br>
	快超时任务<span id="willTimeoutPendingTaskOrderCount"></span><br>
	快超时工单<span id="willTimeoutPendingWorkOrderCount"></span><br>
  </fieldset>
  </div>
</div>
</div>
--%>
		<%-- -----消息盒------------end-------- --%>
		<%-- 2012-4-17 gmh 注释 end --%>

		<%-- 2012-4-17 gmh 添加 消息盒子部分  begin  --%>
		<div id="messageBoxOutDiv"
			style="display: block; position: absolute; left: -1000px; top: -1000px;">
			<input type="hidden" id="messageContentIframeSrc"
				value="" />
			<div id="messgeBox_" title="我的消息盒"
				style="padding: 5px; top: 30px; width: 750px; height: 280px;">
			</div>
		</div>
		<%-- 2012-4-17 gmh 添加 消息盒子部分  end  --%>

	</body>
</html>
