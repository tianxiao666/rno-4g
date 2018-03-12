<%@ page import="java.util.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<String> suffixList = (List<String>)request.getAttribute("suffixList");

	String jumpParams = request.getParameter("jump");
	
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String url = basePath+"op/home/userIndexAction.action";
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%--  <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />--%>
<title>IOSM-智能化运维服务管理平台</title>
<link type="text/css"  rel="stylesheet" href="login/css/login.css"/>
<script type="text/javascript" src="jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="login/jslib/loginSlide.js"></script>
<script language="javascript">

$(function(){

	if(self.frameElement){
		var lrul = $("#rurl").val();
		window.top.location =lrul;
	}


	//幻灯片
	$(window).load(function() {
			$(".login_slide").flexslider();
	});

	//对帐号框进行操作
	$(".loginFormTdIpt").focus(function(){    //帐号框获得鼠标焦点
		var txt_value = $(this).val(); //得到当前文本框的值
		if(txt_value == this.defaultValue){
			$(this).val("");//如果符合条件就清空文本框
			}
	});
	$(".loginFormTdIpt").blur(function(){   //帐号框获得鼠标焦点
		var txt_value = $(this).val(); //得到当前文本框 的值
		if(txt_value == ""){
			$(this).val(this.defaultValue);
			}
	});
	
	//对密码框进行操作
	$("#passWordText").focus(function(){   
		$("#passWord").show();
		$("#passWordText").hide();
		$("#passWord").focus();
	});
	$("#passWord").blur(function(){
		if($("#passWord").val()==null || $("#passWord").val()=="" || $("#passWord").val()=="密码"){
			$("#passWord").hide();
			$("#passWordText").show();
		}
	});
	
	//弹出Email后缀名列表
	$(".email_text").click(function(){
		$(".email_list").toggle();
	});
	$(".email_extensions").mouseleave(function(){
		$(".email_list").hide();
	});
	//显示Email后缀名
	$(".email_list li").click(function(){
		$(".email_name").text($(this).text());
		$(".email_list").hide();
	});
	//鼠标经过改变颜色
	$(".email_list li").hover(function(){
		$(this).addClass("hover")
		},function(){
			$(this).removeClass("hover")
	});
	
});

//refresh image code
function refreshImage(){
var dt=new Date();
$("#showImageCode").attr("src","getVerifyImageCodeAction?dt="+dt.getTime());
}

function setInputToHidden(){
	
	var inputName = $("#inputName").val();
	var realSuffix = $("#real_suffix").text();
	
	//alert("inputName = "+inputName+", realSuffix = "+realSuffix);
	if(inputName!=null){
		inputName = inputName.replace(/\s+/g,"");
	}
	if(realSuffix!=null){
		realSuffix = realSuffix.replace(/\s+/g,"");
	}
	$("#userName").val(inputName+realSuffix);
	var userName = $("#userName").val();
	//alert("userName = "+userName);
	
	$("#loginForm").submit();
}


</script>
</head>
<body>
	<input type="hidden" value="<%=url%>"  id="rurl"/>

	<div class="login_main">
		<div class="login_inner clearfix">
			<div class="login_logo"></div>
			<div class="login_header"></div>
			<div class="version_number"><em>IOSM V2.0</em></div>
			<div class="login_slide">
				<ul class="slides">
					<li><img src="login/images/1.png" /></li>
					<li><img src="login/images/2.png" /></li>
					<li><img src="login/images/3.png" /></li>
					<li><img src="login/images/4.png" /></li>
				</ul>
			</div>
			<div class="login_block">
				<div class="login_title">欢迎登录</div>
				<div class="login_formbg"></div>
				<div class="login_form">
					<form:form method="post"  id="loginForm" cssClass="fm-v clearfix"  action="authenticate.action"  htmlEscape="true">
						<% 
						   String userNameInfo="";
						   String passwordInfo="";  
						   String imageCodeInfo="";
						   String oriUserName="账号";
						   if(request.getAttribute("userNameInfo")!=null){
						      userNameInfo=(String)request.getAttribute("userNameInfo");
						   }
						    if(request.getAttribute("passwordInfo")!=null){
						       passwordInfo=(String)request.getAttribute("passwordInfo");
						    }
						    if(request.getAttribute("imageCodeInfo")!=null){
						       imageCodeInfo=(String)request.getAttribute("imageCodeInfo");
						    }
						   %>
						    <%if(request.getAttribute("loginUserName")!=null){
						      oriUserName=(String)request.getAttribute("loginUserName");
						       if(oriUserName.contains("@")){
							        String selectedSuffix ="";
							      	 int firstIndex = oriUserName.indexOf("@");
							      	 int lastIndex = oriUserName.lastIndexOf("@");
							      	 selectedSuffix = oriUserName.substring(lastIndex,oriUserName.length());
							     	 request.setAttribute("defaultSuffix",selectedSuffix);
							      	 oriUserName = oriUserName.substring(0,lastIndex);
						      	
						      }
						    }
						    
						    //failTime
						    //第一次登录不需要显示验证码，只有登录失败以后才显示验证码
						    int failTime=0;
						    if(request.getSession().getAttribute("failTime")!=null){
						       failTime=(Integer)request.getSession().getAttribute("failTime");
						    }
						    %>
						<em class="errorTip"><%=userNameInfo%></em>
						<div class="login_formIpt" style="z-index: 1000;">
							<input type="text" name="inputName" id="inputName" title="请输入帐号" class="loginFormTdIpt userNameIpt" value="<%=oriUserName%>" />
					        <div class="email_extensions">
					        	<label class="email_text"><em class="email_name" id="real_suffix">${defaultSuffix}</em><em class="em_arrow"></em></label>
								<div class="email_list">
									<em class="email_arrow"></em>
									<ul>
									<c:forEach var="suffix" items="${suffixList}">
										<li>${suffix}</li>
									</c:forEach>
									</ul>
								</div>
							</div>
						</div>
						<div class="login_formIpt">
							<input type="password" name="password" id="passWord" title="请输入密码" class="loginFormTdIpt"/>
							<input type="text" name="" id="passWordText" title="请输入密码" class="loginFormTdIpt" value="密码">
						</div>
						<% if(failTime>0){ %>  
						<div class="login_formIpt verificationCode">
							<input type="text" name="imageCode" id="verificationCode" title="请输入验证码" class="loginFormTdIpt" value="验证码">
							<label class="img">
							    <%
							      long nowTime=new Date().getTime();
							     %>
								<img id="showImageCode" title="看不清？点击换一张！" src="getVerifyImageCodeAction?dt=<%=nowTime%>" style="cursor: pointer" onclick="refreshImage();"/>
							</label>
							<em class="errorTipVc"><%=imageCodeInfo%></em>
						</div>
						<% } %>
						<%--  
						<div class="loginFormCheck" >
							<input type="checkbox" class="" title="两周内自动登录">&nbsp;两周内自动登录&nbsp;
						</div>
						--%>
						<div class="loginFormIptWiotTh">
							<button type="submit" class="btn btn_login" onclick="setInputToHidden();">登&nbsp;录</button>
						</div>
						<input type="hidden" id="userName" name="username" value=""/>
						<input type="hidden" id="jump" name="jump" value="<%=jumpParams%>"/>
						
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<div class="login_footer">
		<div class="footer_inner">
			<a target="_blank" href="#" class="footer_logo"></a>
			<a target="_blank" href="#" class="footer_kx">
				<img alt="可信网站，身份验证" src="login/images/knet.png">
			</a>
			<div class="footer_nav">
			 	<a target="_blank" href="http://www.iscreate.com/">关于怡创</a>
				<a href="#">客户服务</a>
				<a href="#">隐私政策</a>
				<a href="download/qr_code.html" target="_blank">APK下载</a>
				|<span class="copyright">智能化运维服务管理平台IOSM v2.0</span>
				<span class="copyright">广东怡创科技股份有限公司版权所有 &copy; 2010-2012</span>
			</div>
		</div>
	</div>
	
<body>

</html>