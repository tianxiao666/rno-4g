<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<link rel="stylesheet" href="../../css/base.css" />
		<link rel="stylesheet" href="../../css/public.css" />
		<link rel="stylesheet" href="../../css/public-table.css" />
		<link rel="stylesheet" href="css/personal_info.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="jslib/selfinfoviewandedit.js"></script>
		<script type="text/javascript">
$(function(){
	if($("#accounthide").val() == "xiangyu@iscreate.com"){
		var a = $("#accounthide").val();
		var k = a.substring(0,a.indexOf("@"));
		$("#aclabel").html(k+"@gz.gd.chinamobile.com");
	}
	else if($("#accounthide").val() == "huangjinguang@iscreate.com"){
		var a = $("#accounthide").val();
		var k = a.substring(0,a.indexOf("@"));
		$("#aclabel").html(k+"@gd.chinamobile.com");
	}
	else{
		$("#aclabel").html($("#accounthide").val());
	}
})
</script>
		<title>个人资料查看修改</title>
	</head>

	<body>
		<div id="header">
			<div class="header-top">
				<h2>
					用户个人资料
				</h2>
			</div>
		</div>
		<form id="personalInfoForm" action="saveSelfServiceInfoForAjaxAction" target="stay" method="post">
			<div id="container">
				<div class="personal_info_title">
					<h4>
						账号基本信息：
					</h4>
				</div>
				<div class="personal_info_content clearfix">
					<div class="personal_info_photo">
						<div class="add_photo">
							点击添加照片
						</div>
						<div class="add_photo_tip">
							请使用真实照片，支持jpg、png格式，尺寸大小不超过240*220像素。
						</div>
					</div>
					<input name="sysOrgUser.orgUserId" type="hidden" value="${sysOrgUser.orgUserId}"/>
					<input name="sysOrgUser.name"  type="hidden" value="${sysOrgUser.name}"/>
					<input  name="sysAccount.account" type="hidden" id="accounthide" value="${sysAccount.account}"/>
					<input  name="sysOrgUser.status" type="hidden" value="${sysOrgUser.status}"/>
					<input  name="sysAccount.accountId" type="hidden" id="accounthide" value="${sysAccount.accountId}"/>
					<input  name="sysOrgUser.enterpriseId" type="hidden" value="${sysOrgUser.enterpriseId}"/>
					<%-- just for copy use --%>
					<input id="hidden_emailAddress" type="hidden" value="${sysOrgUser.email}" />
					<input id="hidden_backUpEmailAddress" type="hidden" value="${sysOrgUser.backupemail}" />
					<input id="hidden_cellPhoneNumber" type="hidden" value="${sysOrgUser.mobile}" />
					<input id="hidden_mobileEmailAddress" type="hidden" value="${sysOrgUser.mobileemail}" />
					<ul class="personal_info_ul">
						<li>
							<span class="li_left">账号<em class="red">*</em>：</span>
							<label id="aclabel">
							</label>
						</li>
						<li>
							<span class="li_left">姓名<em class="red">*</em>：</span>
							<label>
								${sysOrgUser.name}
							</label>
						</li>
						<li>
							<span class="li_left">邮箱<em class="red">*</em>：</span>
							<label class="modified_person_info">${sysOrgUser.email}</label>
							<span class="now_person_info"> <input type="text" id="emailAddress" name="sysOrgUser.email" value="<s:property value="sysOrgUser.email"/>" class="{chineseNotAllowed:true,required:true, email:true, maxlength:64}"/> 
							<label class="repeat_info_right" style="display:none">
									邮箱可以使用
							</label> 
							</span>
						</li>
						<li>
							<span class="li_left">备用邮箱：</span>
							<label class="modified_person_info">${sysOrgUser.backupemail}</label>
							<span class="now_person_info"> <input type="text" id="backUpEmailAddress" name="sysOrgUser.backupemail" value="${sysOrgUser.backupemail}" class="{chineseNotAllowed:true,email:true, maxlength:64}"/> 
						        <label
									class="repeat_info_wrong" style="display:none">
									邮箱格式不正确，请更正
								</label> </span>
						</li>
						<li>
							<span class="li_left">手机号码<em class="red">*</em>：</span>
							<label class="modified_person_info">
								${sysOrgUser.mobile}
							</label>
							<span class="now_person_info"><input type="text" id="cellPhoneNumber" name="sysOrgUser.mobile" value="${sysOrgUser.mobile}" class="{digits:true,required:true,maxlength:11}" />
							</span>
						</li>
						<li>
							<span class="li_left">手机邮箱<em class="red">*</em>：</span>
							<label class="modified_person_info">
								${sysOrgUser.mobileemail}
							</label>
							<span class="now_person_info"> <input type="text" id="mobileEmailAddress" name="sysOrgUser.mobileemail" value="${sysOrgUser.mobileemail}" class="{chineseNotAllowed:true,required:true, email:true, maxlength:64}"/> <label
									class="repeat_info_right"  style="display:none">
									邮箱可以使用
								</label> </span>
						</li>
						
						<li>
							<span class="li_left">状态：</span>
							<label>
							   <s:if test="sysOrgUser.status==1">
							   <input type="checkbox" checked="checked" disabled="disabled" />
							                    有效
							        </s:if>
							        <s:else>
							         <input type="checkbox" disabled="disabled" />
							                   无效
							        </s:else>
							</label>
						</li>
					</ul>
				</div>
				<div class="personal_info_bottom clear">
					<input class="sava_personal_info" name="saveBtn" id="saveBtn" disabled="disabled" type="submit" value="保存" />
					&nbsp;&nbsp;&nbsp;
					<input class="modify_personal_info" name="modifyBtn" id="modifyBtn" type="button" value="修改" />
					&nbsp;&nbsp;&nbsp;
					<input class="cancel_personal_info" name="cancelBtn" id="cancelBtn" disabled="disabled" type="button" value="放弃" />
				</div>
			</div>
		</form>

	</body>
</html>
