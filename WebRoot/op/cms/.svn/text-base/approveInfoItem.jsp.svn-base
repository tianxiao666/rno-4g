<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*;" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>


<title>新建信息</title>

<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/cms.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />

<STYLE type="text/css">
#uploadAttachment_span{ position:relative;}
#uploadAttachment_file{ position:absolute; z-index:100;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}
</STYLE>

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/approveInfoItem.js"></script>




</head>
<body>
<input type="hidden" id="tabType" value="<s:property value='tabType' />" />
    <%--新建信息进度开始--%>
	<div class="cms_top">发布信息</div>
			<div class="newsMessage">
				<div class="newsMessage_layout">
					<b>发布形式：</b><span><input type="radio" name="1" onclick="clickNormalorSMS('smsdiv','normaldiv');" readonly="readonly" checked="checked" disabled="disabled">短信信息</span><span><input type="radio" name="1"  readonly="readonly" disabled="disabled">普通信息</span>
				</div>
				<%--新建信息进度开始--%>
	
	
	<%-- 短信 --%>
	<div  id="smsdiv">
	<s:if test="isApprover==1">
						<form action="approveInfoReleaseAction" method="post"  target="stay" id="operaForm">
					</s:if>
					<s:if test="isApprover==0">
						<form action="approveInfoReleaseAction" method="post"  target="stay" id="operaForm">
					</s:if>
	<input type="hidden" id="infoReleaseId" name="infoReleaseId" value="<s:property value='infoReleaseEntity.id' />" />
<input type="hidden" id="infoItemId" name="infoItemId" value="<s:property value='infoReleaseEntity.infoId' />" />
<input type="hidden" id="releaseScopeList" name="releaseScopeList" value="<s:property value='infoReleaseEntity.releaseScopeList' />" />
<s:if test="isApprover==1">
			<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="1" />
</s:if>
<s:if test="isApprover==0">
			<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="3" />
</s:if>
<input type="hidden" name="cmsInfoitem.author" value="<s:property value='infoItemEntity.author' />" />
<input type="hidden" name="cmsInfoitem.drafttime" value="<s:property value='infoItemEntity.drafttime' />" />
<input type="hidden" name="cmsInfoitem.infoType" value="<s:property value='infoItemEntity.infoType' />" />
<input type="hidden" name="cmsInforelease.releaser" value="<s:property value='infoReleaseEntity.releaser' />" />
<input type="hidden" name="cmsInforelease.auditor" value="<s:property value='infoReleaseEntity.auditor' />" />
<input type="hidden" id="isOperator" name="isOperator" value="<s:property value='isOperator' />" />
<input type="hidden" id="infoReleaseStatus" name="infoReleaseStatus" value="<s:property value='infoReleaseStatus' />" />
<input type="hidden" id="releaseScopeStaffName" name="releaseScopeStaffName" value="<s:property value='infoReleaseEntity.releaseScopeStaffName' />" />
	<div class="newsMessage_content clearfix">
					<table class="main_output_table">
						<tbody>
							<tr>
								<td class="menuTd">信息类型<span class="redStar">*</span>：</td>
								<td colspan="4">
									<select id="cmsCategory" name="cmsInfoitem.category">
										<s:iterator value="cmsCategoryList" id="cate">
											<option value="<s:property value='#cate.cmsCategory'/>" <s:if test="#cate.cmsCategory==infoItemEntity.category">selected="selected"</s:if>><s:property value='#cate.cmsCategory'/></option>
										</s:iterator>
										<%-- <option value="发文" selected="">发文</option>
										<option value="公告">公告</option>
										<option value="通知">通知</option>
										<option value="预警">预警</option>
										<option value="预报">预报</option>
										<option value="调查统计">调查统计</option>
										<option value="任务下达">任务下达</option>
										<option value="总结">总结</option>
										<option value="信件">信件</option> --%>
									</select>
								</td>
							</tr>
							<tr>
								<td class="menuTd">信息内容<span class="redStar">*</span>：</td>
								<td colspan="4">
									<textarea name="cmsInfoitem.content" id="textarea_c" type="text" class="input_text" ><s:property value='infoItemEntity.content' /></textarea><span class="red"></span>
								</td>
							</tr>
							<tr>
								<td class="menuTd vt">发布范围：</td>
						<td colspan="4">
						    <div class="clearfix">
								<div class="fl">
									<input type="radio" value="radio_department" name="releaseBounds" <s:if test="1==infoReleaseEntity.releaseScopeType">checked</s:if> onclick="clickReleaseBounds();"/>按部门
									<span style="display:none;"><input type="radio" value="radio_staff" name="releaseBounds" <s:if test="2==infoReleaseEntity.releaseScopeType">checked</s:if>/>按人员</span>
									<input type="radio" value="radio_role" name="releaseBounds" <s:if test="3==infoReleaseEntity.releaseScopeType">checked</s:if>   id="releaseBounds_raido" onclick="clickReleaseBounds();" />按岗位角色
									<select id="roleCode" name="roleCode"  value="<s:property value='infoReleaseEntity.releaseRole'/>" <s:if test="3!=infoReleaseEntity.releaseScopeType"> style="display:none;"</s:if>>
										<s:iterator value="roleList" id="roleItem">
											<option value="<s:property value='#roleItem.roleCode'/>"><s:property value='#roleItem.name'/></option>
										</s:iterator>
										<option value=""></option>
									</select>
									<input type="hidden" value="<s:property value='infoReleaseEntity.releaseRole' />" id="hideRole"/>
								</div>
							</div>
							    <%--放树框架的容器--%>
							    <ul id="treeDiv"  class="filetree orgTree treeview"></ul>
						</td>
					</tr>
						</tbody>
					</table>
				</div>
				<s:if test="isApprover==1">
					<div id="limitDiv" class="newsMessage_approver" style="display: none;">
				
				
						<h3>* 超出发布权限范围：请选择审批人。</h3>
						<div class="newsMessage_approverRadio">
							<input class="nod" type="radio" checked="" value="directBox"  onclick="clickNodRadio('superiorsAccId');" name="radioApprover">直接上级
							<select class="nod" id="superiorsAccId" name="superiorsAccId" style="display: none;">
										<s:iterator value="superiorsList" id="superiorsItem">
											<option value="<s:property value='#superiorsItem.staffId'/>"><s:property value='#superiorsItem.staffName'/></option>
										</s:iterator>
							</select>
							<input class="nod" type="radio" checked=""   onclick="clickNodRadio('qualityId');"  value="qualityApprover" name="radioApprover">质量安全部
							<select class="nod" id="qualityId" name="qualityId" style="display: none;">
										<s:iterator value="qualityList" id="qualityItem">
											<option value="<s:property value='#qualityItem.staffId'/>"><s:property value='#qualityItem.staffName'/></option>
										</s:iterator>
							</select>
							<input class="nod" type="radio" checked=""   onclick="clickNodRadio('humanResourcesId');"  value="hr" name="radioApprover">人力资源部
							<select class="nod" id="humanResourcesId" name="humanResourcesId" style="display: none;">
										<s:iterator value="humanResourcesList" id="humanResourcesItem">
											<option value="<s:property value='#humanResourcesItem.staffId'/>"><s:property value='#humanResourcesItem.staffName'/></option>
										</s:iterator>
							</select>
							<input class="nod" type="radio" checked=""  onclick="clickNodRadio('companyLeadersId');"  value="leader" name="radioApprover">公司领导
							<select class="nod" id="companyLeadersId" name="companyLeadersId" style="display: none;">
										<s:iterator value="companyLeadersList" id="companyLeadersItem">
											<option value="<s:property value='#companyLeadersItem.staffId'/>"><s:property value='#companyLeadersItem.staffName'/></option>
										</s:iterator>
							</select>
						</div>
					</div>
					</s:if>
					<input name="cmsInforelease.isInformedWithsms" type="hidden" value="1"/>
				<div class="newsMessage_but">
					<s:if test="isApprover==1">
						<input id="btnReleaseInfo" type="submit" class="input_button" onclick="return clickCheckedapprover();" value="发布" />
					</s:if>
					<s:if test="isApprover==0">
						<input id="btnApprove" type="submit" class="input_button" onclick="return clickCheckedapprover();" value="通过" />
					</s:if>
						<input id="isApprover" name="isApprover" value="<s:property value="isApprover"/>" type="hidden"/>
						<input type="button" id="btnPrevLook" class="input_button" value="返回查看" />
					   <%--  <input id="btnReject" type="button" class="input_button" value="驳回" />--%> 
						
				</div>
	</form>
	</div>
</body>
</html>