<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑用户</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
		<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
<link rel="stylesheet" type="text/css" href="css/architecture.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<style>
	.orgUserManage_standard_budget_title {
    background-color: #F0F8FF;
    border: 1px solid #DDDDDD;
    height: 30px;
    line-height: 30px;
    margin-top: 5px;
    text-align: center;
}


.orgUserManage_main {
    background: none repeat scroll 0 0 #C3D5ED;
    margin: 5px;
    min-width: 500px;
}


.orgUserManage_content {
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #99BCE8;
    min-height: 700px;
    overflow: hidden;
}

.orgUserManage_top {
    background: url("../../images/white-top-bottom.gif") repeat-x scroll 0 -1px rgba(0, 0, 0, 0);
    border-bottom: 1px solid #99BBE8;
    color: #15428B;
    font-weight: bold;
    line-height: 26px;
    padding: 0 5px;
}

.orgUserManage_title {
    padding: 0 10px;
}


</style>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js">
</script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js">
</script>
		<script type="text/javascript" src="jslib/tree_sm.js">
</script>
		<script type="text/javascript" src="../js/tab.js">
</script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>

<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript">

var index = 0;
$(function(){
	$("#password").val("");
	$("#affirmpassword").val("");
	index = $("#hiddenIndex").val();
});
	
	//新增一行
	function addTr(){
		var context = " <tr class=\"posttr\"> "
							+"					<td class=\"role_table_tr\"> "
							+"			<select name=\"sysUserRelaPostList["+index+"].org_id\" id=\"select_orgId_"+index+"\" class=\"orgSelect\"  onchange=\"getPostByOrgType("+index+");\" >	" +	$("#select_orgId").html()  + "</select>"
							+"					</td> "
							+"					<td class=\"role_table_tr\"> "
							+"			<select name=\"sysUserRelaPostList["+index+"].post_code\" class=\"postSelect\"  id=\"post_select_"+index+"\">"
							+"          <option value=\"0\">请选择</option>"
							+"           </select>"
							+"					</td> "
							//+"					<td class=\"role_table_tr\"> "
							//+"                     <input type=\"text\"  name=\"sysUserRelaPostList["+index+"].start_time\"  onFocus='WdatePicker({dateFmt:\"yyyy-MM-dd\"})'/>"
							//+"					</td>"
							//+"					<td class=\"role_table_tr\"> "
							//+"                     <input type=\"text\"  name=\"sysUserRelaPostList["+index+"].end_time\"  onFocus='WdatePicker({dateFmt:\"yyyy-MM-dd\"})'/>"
							//+"					</td>"
							+"					<td class=\"role_table_tr\"> "
							+"                    <a onclick=\"deleteTr(this);\">删除</a> "
							+"					</td>"
							+"			</tr> ";
		index++;					
		$("#show_add_org_p_org_role #trHead").append(context);
	}
	
	function deleteTr(me){
		$(me).parent().parent().remove();
	}
	
	//根据组织类型获取对应岗位
	function getPostByOrgType(i){
		var orgType = $("#select_orgId_"+i+" option:selected").attr("orgType");
		var context = "          <option value=\"0\">请选择</option>";
		$.ajax({
			url : "getPostByOrgTypeAction",
			data : {orgType:orgType},
			dataType : 'json',
			async:false,
			type : 'POST',
			success : function(data) {
				$.each(data,function(k,v){
					context = context+"          <option value=\""+v.CODE+"\">"+v.NAME+"</option>";
				});
				$("#post_select_"+i).html(context);
			}
		});
	}
	
	//新增用户与用户岗位(v1.1.3)
	function updateOrgUserAndPost(){
		var flag = true;
		//检测账号
		 if(providerAccount() == false){
			flag = false;
		}else{
			var account = $("#account").val();
			var enterpriseUrl = $("#account_suffix").html();
			$("#hiddenaccount").val(account+enterpriseUrl);
		}
		if(providerPassword() == false){
			flag = false;
		}
		if(providerName() == false){
			flag = false;
		}
		if(providerEmail() == false){
			flag = false;
		}
		if(providerOrgSelect() == false){
			flag = false;
		}
		if(providerPostSelect() == false){
			flag = false;
		}
		if(providerPhoneNumber() == false){
			flag = false;
		}
		if(flag == false){
			return false;
		}
		//表单AJAX提交
		$("#orgUserForm").ajaxSubmit({
					url : 'updateOrgUserAndPostAction',
					dataType : 'json',
					async:false,
					success : function(data) {
							if(data.orgUserId > 0){
								alert("保存成功");
								window.location.href = "loadUpdateOrgUserViewAction?orgUserId="+data.orgUserId;
							}else{
								alert("保存失败");
							}
					}
		});
	}
	
	//检验密码
	function providerPassword(){
		var password = $("#password").val();
		var affirmpassword = $("#affirmpassword").val();
		var errorStr = "";
		$("#affirmpassword_error").html("");
		//密码提示
		if((password == null || password.replace(/(^\s*)/, "") == "") && (affirmpassword != null && affirmpassword.replace(/(^\s*)/, "") != "")){
			errorStr = "密码不能为空";
			$("#password_error").html(errorStr);
			return false;
		}else if(password.length<6 && (affirmpassword != null && affirmpassword.replace(/(^\s*)/, "") != "")){
			errorStr = "密码不能小于6位数";
			$("#password_error").html(errorStr);
			return false;
		}else if((!(/[0-9]+/).test(password) || !(/[A-Za-z]+/).test(password)) && (affirmpassword != null && affirmpassword.replace(/(^\s*)/, "") != "")){
			errorStr = "密码要包含英文和数字";
			$("#password_error").html(errorStr);
			return false;
		}else if(password.length > 16  && (affirmpassword != null && affirmpassword.replace(/(^\s*)/, "") != "")){
			errorStr = "密码超过16字符";
			$("#password_error").html(errorStr);
			return false;
		}else{
			errorStr = "";
			$("#password_error").html(errorStr);
		}
		if((affirmpassword == null || affirmpassword.replace(/(^\s*)/, "") == "") && (password != null && password.replace(/(^\s*)/, "") != "")){
			$("#affirmpassword_error").html("确认密码不能为空");
			return false;
		}else if(password!=affirmpassword && (password != null && password.replace(/(^\s*)/, "") != "")){
			errorStr = "两次密码不一致";
			$("#password_error").html(errorStr);
			return false;
		}
			$("#password_error").html(errorStr);
			return true;
}
	

//检测姓名
function providerName(){
	var errorStr = "";
	//名字提示
	if($("#name").val() == null || $("#name").val().replace(/(^\s*)/, "") == ""){
		errorStr = "姓名不能为空";
		$("#name_error").html(errorStr);
		return false;
	}else if($("#name").val().length > 20){
		errorStr = "姓名超过20字符";
		$("#name_error").html(errorStr);
		return false;
	}else{
		$("#name_error").html(errorStr);
		return true;
	}
}

//检测邮箱
function providerEmail(){
	var errorStr = "";
	//邮箱提示
	if($("#email").val() == null || $("#email").val().replace(/(^\s*)/, "") == ""){
		errorStr = "邮箱不能为空";
		$("#email_error").html(errorStr);
		return false;
	}else if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test($("#email").val())){
		errorStr = "邮箱输入格式错误";
		$("#email_error").html(errorStr);
		return false;
	}else if($("#email").val().length > 50){
		errorStr = "邮箱地址超过50字符";
		$("#email_error").html(errorStr);
		return false;
	}else{
		$("#email_error").html(errorStr);
		return true;
	}
}
	
//检测手机号码
function providerPhoneNumber(){
	var errorStr = "";
	//手机号码提示
	if($("#phoneNumber").val() == null || $("#phoneNumber").val().replace(/(^\s*)/, "") == ""){
		$("#phoneNumber_error").html(errorStr);
		return true;
	}else if(!(/^(0|[1-9]\d*)$/).test($("#phoneNumber").val())){
		errorStr = "输入格式错误";
		$("#phoneNumber_error").html(errorStr);
		return false;
	}else if($("#phoneNumber").val().length > 11){
		errorStr = "超过11字符";
		$("#phoneNumber_error").html(errorStr);
		return false;
	}else{
		$("#phoneNumber_error").html(errorStr);
		return true;
	}
}
	
	
	//检测账号
function providerAccount(){
	var errorStr = "";
	var flagStr = "";
	var account = $("#account").val();
	var enterpriseUrl = $("#account_suffix").html();
	if($("#account").val() == null || $("#account").val().replace(/(^\s*)/, "") == ""){
		errorStr = "账号不能为空";
		$("#account_error").html(errorStr);
		$("#account_flag").html(flagStr);
		return false;
	}
	if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
		errorStr = "账号输入格式错误";
		$("#account_error").html(errorStr);
		$("#account_flag").html(flagStr);
		return false;
	}
	if(account.length > 20){
			errorStr = "账号超过20字符";
			$("#account_error").html(errorStr);
			$("#account_flag").html(flagStr);
			return false;
		}
	account = account + enterpriseUrl;
	var flag = false;
	$.ajax({
			url : "../system/checkAccountAjaxAction",
			data : {"account":account},
			async:false,
			type : 'POST',
			success : function(data) {
				if(data=="success"){
					flagStr = "该账号可用";
					flag =  true;
				}else{
					if($("#provideraccount").val() == account){
						flagStr = "该账号可用";
						flag =  true;
					}else{
						errorStr = "该账号不可用";
						flag =  false;
					}
				}
			}
		});
		
		$("#account_error").html(errorStr);
		$("#account_flag").html(flagStr);
		return flag;
}


//检测所属部门
function providerOrgSelect(){
	var flag = true;
	var errorStr = "";
	$(".orgSelect").each(function(){
		if($(this).val() == "0" || $(this).val() == 0){
			errorStr = "请选择所属部门";
			$(this).focus();
			flag = false;
		}
	});
	$("#org_error").html(errorStr);
	return flag;
}


//检测所属岗位
function providerPostSelect(){
	var flag = true;
	var errorStr = "";
	$(".postSelect").each(function(){
		if($(this).val() == "0" || $(this).val() == 0){
			errorStr = "请选择所属岗位";
			$(this).focus();
			flag = false;
		}
	});
	$("#post_error").html(errorStr);
	return flag;
}

	
function checkProviderAccount(){
	providerAccount();
}
	
	
function checkProviderPassword(){
	providerPassword();
}

function checkProviderName(){
	providerName();
}

function checkProviderEmail(){
	providerEmail();
}

function checkProviderPhoneNumber(){
	providerPhoneNumber();
}
</script>
	</head>

	<body>
		<%--主体开始--%>
		<div class="orgUserManage_main">
		<form method="post" action="updateOrgUserAndPostAction" id="orgUserForm">
			<div class="orgUserManage_content">
				<div class="orgUserManage_top">
					编辑用户
				</div>
				<div class="orgUserManage_title">
			           	<div class="orgUserManage_standard_budget_title">
			           		<h4>编 辑 用 户</h4>
			       		 </div>
			        </div>
				<%--左边工作区开始--%>
				<div class="user_accountInfo" style="width: 100%;">
					<div class="accountInfo" style="padding: 40px;">
					<div style="width: 600px;">
						<table>
							<tr height="40px;">
								<td style="width: 110px;">
									帐号：<span class="redStar" style="float:right; padding-right:8px;">*</span>
								</td>
								<td  style="width: 200px;"><input class="required input-text" id="account" type="text" onblur="checkProviderAccount();" value="<s:property value="account"/>"/>
										<input class="required input-text" type="hidden" name="sysOrgUser.orgUserId" autocomplete="off" value="<s:property value="sysOrgUser.orgUserId"/>"/>
								</td>
								<td style="width: 300px;">
									&nbsp;&nbsp;<span id="account_suffix">@iscreate.com</span>
										<input class="required input-text" name="sysAccount.account" id="hiddenaccount" type="hidden" value="" />
										<input class="required input-text" id="provideraccount" type="hidden" value="<s:property value="sysAccount.account"/>" />
										&nbsp;&nbsp;<a href="javascript:checkProviderAccount();">检查唯一性</a>
										&nbsp;&nbsp;<span id="account_error" class="red"></span><span id="account_flag" style="color:#008000;"></span>
								</td>
							</ltr>
							<tr height="40px;">
								<td>
									密码：
								</td>
								<td ><input class="required input-text" style="width: 200px;" type="password" id="password" name="sysAccount.password" autocomplete="off"  value="" onblur="checkProviderPassword();"/>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="password_error" class="red"></span></div>
								</td>
							</tr>
							<tr height="40px;">
								<td>
									确认密码：
								</td>
								<td ><input class="required input-text" style="width: 200px;" type="password" id="affirmpassword" autocomplete="off"  value="" onblur="checkProviderPassword();"/>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="affirmpassword_error" class="red"></span></div>
								</td>
							</tr>
							<tr height="40px;">
								<td>
									姓名：<span class="redStar" style="float:right; padding-right:8px;">*</span>
								</td>
								<td ><input class="required input-text" name="sysOrgUser.name" id="name" type="text"  value="<s:property value="sysOrgUser.name"/>" onblur="checkProviderName();"/>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="name_error" class="red"></span></div>
								</td>
							</tr>
							 <tr height="40px;">
								<td>
									邮箱：<span class="redStar" style="float:right; padding-right:8px;">*</span>
								</td>
								<td><input class="required input-text" name="sysOrgUser.email" type="text" onblur="checkProviderEmail();" id="email"  value="<s:property value="sysOrgUser.email"/>"/>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="email_error" class="red"></span></div>
								</td>
							</tr>
							<tr height="40px;">
								<td>
									性别：
								</td>
								<td>
								<s:if test="sysOrgUser.gender == 'male'">
									<input type="radio" name="sysOrgUser.gender" checked="checked" value="male"/>男
								</s:if>
								<s:else>
									<input type="radio" name="sysOrgUser.gender" value="male"/>男
								</s:else>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<s:if test="sysOrgUser.gender == 'female'">
									<input type="radio" name="sysOrgUser.gender" checked="checked" value="female"/>女
								</s:if>
								<s:else>
									<input type="radio" name="sysOrgUser.gender" value="female"/>女
								</s:else>
								</td>
							</tr>
							<tr height="40px;">
								<td>
									手机号：
								</td>
								<td ><input class="required input-text"  name="sysOrgUser.mobile" id="phoneNumber"  type="text" value="<s:property value="sysOrgUser.mobile"/>" onblur="checkProviderPhoneNumber();"/>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="phoneNumber_error" class="red"></span></div>
								</td>
							</tr>
							<tr height="40px;">
								<td>
									状态：
								</td>
								<td >
								<s:if test="sysOrgUser.status == 1">
									<input type="checkbox" checked="checked"  name="sysOrgUser.status"  value="1"/>有效
								</s:if>
								<s:else>
									<input type="checkbox"  name="sysOrgUser.status"  value="1"/>有效
								</s:else>
								<span style="float:right;"><a href="javascript:addTr();">新增一行</a></span>
								</td>
								<td>
									<div>&nbsp;&nbsp;<span id="org_error" class="red"></span>&nbsp;&nbsp;<span id="post_error" class="red"></span></div>
								</td>
							</tr>
							
						</table>
						<div class="endow_role_info">
								<div class="role_table">
									<table id="show_add_org_p_org_role" class="posttable">
										<tbody id='trHead'>
											<tr class="role_title"> 
											<td class="role_table_tr" style="min-width: 120px;">
													所属部门
												</td>
											<td class="role_table_tr" style="min-width: 120px;">
													所属岗位
												</td>
												<%-- <td class="role_table_tr" style="min-width: 120px;">
													开始时间
												</td>
												<td class="role_table_tr" style="min-width: 120px;">
													结束时间
												</td> --%>
												<td class="role_table_tr" style="width: 30px;">
													操作
												</td>
											</tr>
											<s:set var ="total" value="0"/>
											<s:if test="sysUserRelaPostMapList != null">
						                    	<s:iterator id="map" value="sysUserRelaPostMapList" status="st">
						                    			<tr class="posttr"> 
															<td class="role_table_tr">
															<select name="sysUserRelaPostList[<s:property value="#total"/>].org_id" id="select_orgId_<s:property value="#total"/>" class="orgSelect"  onchange="getPostByOrgType(<s:property value="#total"/>);" >
																	<option value="0" orgType="0">
													               		请选择
													               	</option>
													                     	<s:iterator id="orgmap" value="orgList" status="st1">
													                     		<s:if test="#map.ORG_ID == #orgmap.ORG_ID">
														                     		<option orgType="<s:property value="#orgmap.ORG_TYPE"/>" selected="selected" value="<s:property value="#orgmap.ORG_ID"/>">
														                     			<s:property value="#orgmap.NAME"/>
														               				</option>
													               				</s:if>
													               				<s:else>
													               					<option orgType="<s:property value="#orgmap.ORG_TYPE"/>" value="<s:property value="#orgmap.ORG_ID"/>">
														                     			<s:property value="#orgmap.NAME"/>
														               				</option>
													               				</s:else>
													                </s:iterator>
															</select>
																	</td> 
																		<td class="role_table_tr"> 
																<select name="sysUserRelaPostList[<s:property value="#total"/>].post_code" class="postSelect"  id="post_select_<s:property value="#total"/>">
																		<c:forEach var="relaPost" items="${relaPostMap}" varStatus="st21">
																			<c:if test="${relaPost.key == map.ORG_TYPE}" >  
																			<option value="0">请选择</option>
																			<c:forEach var="postmap" items="${relaPost.value}" varStatus="st2">
																				<c:choose>
																					<c:when  test="${map.POST_CODE == postmap.code}">
																						<option value="${postmap.code}" selected="selected" >${postmap.name}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${postmap.code}">${postmap.name}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																			</c:if>
																		</c:forEach>
																		
													          			
													          			
													           </select>
																		</td> 
																		
																		<td class="role_table_tr"> 
													                    <a onclick="deleteTr(this);">删除</a> 
																		</td>
																</tr> 
														<s:set var ="total" value="#total+1"/>
						                    	</s:iterator>
	                    					</s:if>
								
										</tbody>
									</table>
									<input id="hiddenIndex" type="hidden" value="<s:property value="#total"/>"/>
									</div>
									</div>
									<div style="text-align: center; padding: 10px;">
									<input value="保存" type="button" onclick="updateOrgUserAndPost();"/>
									</div>
					</div>
					</div>
				</div>
				<%--左边工作区结束--%>
				
			</div>
		</div>
		</form>
			<select id="select_orgId"   style="display: none;" >
               	<option value="0" orgType="0" selected="selected">
               		请选择
               	</option>
                     	<s:iterator id="map" value="orgList" status="st">
                     		<option orgType="<s:property value="#map.ORG_TYPE"/>" value="<s:property value="#map.ORG_ID"/>">
                     			<s:property value="#map.NAME"/>
               		</option>
                </s:iterator>
		   </select>
		 
		 </div>
	</body>
</html>
