<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
		String jumpParams = request.getParameter("jump");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>广东怡创科技股份有限公司</title>
		<%-- <link href="mobileLogin/css/base.css" type="text/css" rel="stylesheet" />		
		<link href="mobileLogin/css/terminal.css" type="text/css" rel="stylesheet" />
		 --%>
		 <link href="login/css/mobilelogin.css" type="text/css" rel="stylesheet" />
		 <script type="text/javascript" src="jslib/jquery/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="login/jslib/cas.js"></script>
<script type="text/javascript">
 function setInputToHidden(){
	var inputName = $("#inputName").val();
	var realSuffix = $("#enterpriseSuffix").val();
	//alert("inputName = "+inputName+", realSuffix = "+realSuffix);
	if(inputName!=null){
		inputName = inputName.toString().trim();
	}
	if(realSuffix!=null){
		realSuffix = realSuffix.toString().trim();
	}
	$("#userName").val(inputName+realSuffix);
	var userName = $("#userName").val();
	//alert("userName = "+userName);
}
        
</script>
	</head>
<body>
    <div id="login">
	    <div class="login-logo"></div>
	    <form:form method="post" id="loginForm" cssClass="fm-v clearfix" action="authenticate.action" htmlEscape="true">
	        <div class="login-list">
	       				 <% 
						   String userNameInfo="";
						   String passwordInfo="";  
						   String oriUserName="";
						   if(request.getAttribute("userNameInfo")!=null){
						      userNameInfo=(String)request.getAttribute("userNameInfo");
						   }
						    if(request.getAttribute("passwordInfo")!=null){
						       passwordInfo=(String)request.getAttribute("passwordInfo");
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
						    %>
	        <span class="errorTootip"><%=userNameInfo%></span>
		        <div class="login-input">
				    <div class="user">
				    
						<div class="user-name">帐号</div>
						<div class="user-text">
				            <input type="search" name="inputName" id="inputName" class="infotext" value="<%=oriUserName%>" >
			            </div>
			            
			            <div class="email-text" >
				            <select id="enterpriseSuffix" name="enterpriseFlag" >
									<c:forEach var="suffix" items="${suffixList}">
									  <c:choose>
										<c:when test="${suffix eq defaultSuffix}">
												<option value="${suffix}" selected="selected">${suffix}</option>
										</c:when>
										<c:otherwise>
											<option  value="${suffix}" >${suffix}</option>
										</c:otherwise>
									 </c:choose>	
									</c:forEach>
							</select>
			            </div>
					</div>
					<div class="password">
						<div class="password-name">密码</div>
						<div class="password-text">
				            <input type="password" name="password" id="" class="infotext" value="" >
				            <%=passwordInfo %>
			            </div>
					</div>
					<input type="hidden" id="userName" name="username" value=""/>
					<input type="hidden" name="_eventId" value="submit" />	
					<input type="hidden" id="jump" name="jump" value="<%=jumpParams%>"/>
				</div>
				<div class="login-button">
				    <input type="submit" name="submit" class="login-but" value="登录" onclick="setInputToHidden();" />
				</div>
				<div class="login-tel">
				    <p>维护热线：15710708694</p>
					<p>Copyright ? 广东怡创科技股份有限公司</p>
				</div>
		    </div>
		</form:form>
	</div>
</body>
</html>
