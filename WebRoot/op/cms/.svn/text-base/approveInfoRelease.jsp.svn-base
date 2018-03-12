<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>审核公告发布信息</title>
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/cms.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<%--<script type="text/javascript" src="../js/orgTreeManage.js" ></script> --%>

<script type="text/javascript" src="js/approveInfoRelease.js"></script>
<style>
#tree2Div{display:none;margin-left:15px;float:left;z-index: 50;background:#fff; border:0px solid red; border-top:none;width: 190px; min-height:186px; text-align:left;margin-top:1px; line-height:15px;}
#tree3Div{display:none;margin-left:15px;float:left;z-index: 50;background:#fff; border:0px solid red; border-top:none;width: 190px; min-height:186px; text-align:left;margin-top:1px; line-height:15px;}

#tree1 span{ cursor:pointer;}
</style>

</head>
<body>

<form action="approveInfoReleaseAction" method="post" target="stay" id="operaForm">
<input type="hidden" id="tabType" value="<s:property value='tabType' />" />
<input type="hidden" id="infoReleaseId" name="infoReleaseId" value="<s:property value='infoReleaseEntity.id' />" />
<input type="hidden" id="infoItemId" name="infoItemId" value="<s:property value='infoReleaseEntity.infoId' />" />
<input type="hidden" id="releaseScopeList" name="releaseScopeList" value="<s:property value='infoReleaseEntity.releaseScopeList' />" />
<s:if test="isApprover==1">
			<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="1" />
</s:if>
<s:if test="isApprover==0">
			<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="3" />
</s:if>
<input type="hidden" id="isOperator" name="isOperator" value="<s:property value='isOperator' />" />
<input type="hidden" id="infoReleaseStatus" name="infoReleaseStatus" value="<s:property value='infoReleaseStatus' />" />
<input type="hidden" id="releaseScopeStaffName" name="releaseScopeStaffName" value="<s:property value='infoReleaseEntity.releaseScopeStaffName' />" />
<input type="hidden" name="cmsInfoitem.author" value="<s:property value='infoItemEntity.author' />" />
<input type="hidden" name="cmsInfoitem.drafttime" value="<s:property value='infoItemEntity.drafttime' />" />
<input type="hidden" name="cmsInfoitem.infoType" value="<s:property value='infoItemEntity.infoType' />" />
<input type="hidden" name="cmsInforelease.releaser" value="<s:property value='infoReleaseEntity.releaser' />" />
<input type="hidden" name="cmsInforelease.auditor" value="<s:property value='infoReleaseEntity.auditor' />" />
	<%--主体开始--%>
    <%--新建信息进度开始--%>
	<div class="cms_top">发布信息</div>
			<div class="newsMessage">
				<div class="newsMessage_layout">
					<b>发布形式：</b><span><input type="radio" name="1" disabled="disabled">短信信息</span><span><input type="radio" name="1" checked="checked" disabled="disabled">普通信息</span>
				</div>
				<%-- <div class="newsMessage_schedule">
					<ul class="ks_steps blue">
						<li class="ks_steps_item done" style="z-index: 500; ">新建信息<div class="trigon"><span class="bor"></span><span class="blo"></span></div></li>
						<li class="ks_steps_item current" style="z-index: 499; ">发布</li>
					</ul>
				</div> --%>
	<%--新建信息进度结束--%>
	<%--新建信息内容开始--%>
	<div class="newsMessage_content" id="newsMessage_content" style="display: none;">
		<table class="main_output_table">
			<tbody>
				<tr>
					<td class="menuTd">信息类型<span class="redStar">*</span>：</td>
					<td>
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
					<td class="menuTd">重要级别：</td>
					<td>
						<select id="cmsImportantLevel" name="cmsInfoitem.importancelevel">
							<s:iterator value="cmsImportantLevel" id="level">
								<option value="<s:property value='#level.cmsImportantLevel'/>" <s:if test="#level.cmsImportantLevel==infoItemEntity.importancelevel">selected="selected"</s:if>><s:property value='#level.cmsImportantLevel'/></option>
							</s:iterator>
							<%-- <option value="最高" selected="">最高</option>
							<option value="较高">较高</option>
							<option value="普通">普通</option>
							<option value="低">低</option> --%>
						</select>
					</td>
					<td class="menuTd">文件号：</td>
					<td  class="w300">
					    <input name="cmsInfoitem.label" type="text" class="input_text" value="${infoItemEntity.label }" style="width:90%" /><span class="red"></span>
					</td>
				</tr>
				<tr>
					<td class="menuTd">标题/主题<span class="redStar">*</span>：</td>
					<td colspan="6"><input name="cmsInfoitem.title" id="nom_titlte" type="text" class="input_text" value="<s:property value='infoItemEntity.title' />" style="width:90%" /><span class="red"></span></td>
				</tr>
				<tr>
					<td class="menuTd vt">正文<span class="redStar">*</span>：</td>
					<td colspan="6">
					    <textarea name="cmsInfoitem.content" id="nom_content" class="input-text" style="width:90%" >${infoItemEntity.content }</textarea><span class="red"></span>
						<%-- <input type="button" class="input_button vt mt2" value="高级" /> --%>
					</td>
				</tr>
			</tbody>
		</table>
		<table class="main_output_table advanced_mode" id="main_output_table">
			<tbody>
				<tr>
					<td class="menuTd vt">图片：</td>
					<td colspan="6">
						    <input type="file" id="uploadPic" name="uploadPic"/>
							<input type="hidden" id="picName" name="picName" value="${infoItemEntity.pictures }"/>
							<input type="hidden" id="picUrl" name="picUrl" value="${infoItemEntity.picture_url }"/>
							<div class="img_list" id="pica">
							</div> 
						</td>
				</tr>
				<tr>
					<td class="menuTd vt">附件：</td>
					<td colspan="6" >
						<div id="uploadAttachment_div">
							<%-- <input style="cursor:pointer;" type="file" id="uploadAttachment_file" size="1" > --%>
							<div><a href="javascript:void(0)" id="addAttachment" style="text-decoration:underline;">添加附件</a></div>
						</div>
						<input type="hidden" id="attaName" name="attaName" value="${infoItemEntity.attachments }"/>
						<input type="hidden" id="attaUrl" name="attaUrl" value="${infoItemEntity.url } "/>
						<div class="img_list" id="attr">
						</div> 
					</td>
				</tr>
			</tbody>
		</table>
		<a href="#" onclick="clickShowMore('advanced_mode_but1');" class="advanced_mode_but" id="advanced_mode_but1" style="display:block;"><em>▼</em>&nbsp;更多内容</a>
		<a href="#" onclick="clickShowMore('advanced_mode_but2');" class="advanced_mode_but" id="advanced_mode_but2" style="display:none;"><em>▲</em>&nbsp;更多内容</a>
	</div>
	<%--新建信息内容结束--%>
	<%-- 查看页面 --%>
	<div class="newsMessage_content" id="newsMessage_content_is" style="display: none;">
		<table class="main_output_table">
			<tbody>
				<tr>
					<td class="menuTd">信息类型<span class="redStar">*</span>：</td>
					<td>
							${infoItemEntity.category }
							<%-- <option value="发文" selected="">发文</option>
							<option value="公告">公告</option>
							<option value="通知">通知</option>
							<option value="预警">预警</option>
							<option value="预报">预报</option>
							<option value="调查统计">调查统计</option>
							<option value="任务下达">任务下达</option>
							<option value="总结">总结</option>
							<option value="信件">信件</option> --%>
					</td>
					<td class="menuTd">重要级别：</td>
					<td>
							${infoItemEntity.importancelevel }
							<%-- <option value="最高" selected="">最高</option>
							<option value="较高">较高</option>
							<option value="普通">普通</option>
							<option value="低">低</option> --%>
					</td>
					<td class="menuTd">文件号：</td>
					<td>
					    ${infoItemEntity.label }
					</td>
				</tr>
				<tr>
					<td class="menuTd">标题/主题<span class="redStar">*</span>：</td>
					<td colspan="6">
					${infoItemEntity.title }
					</td>
				</tr>
				<tr>
					<td class="menuTd vt">正文<span class="redStar">*</span>：</td>
					<td colspan="6">
					    ${infoItemEntity.content }
					</td>
				</tr>
			</tbody>
		</table>
		<table class="main_output_table">
			<tbody>
				<tr>
					<td class="menuTd vt">图片：</td>
					<td colspan="6">
							<div class="img_list" id="pica_is">
							</div> 
						</td>
				</tr>
				<tr>
					<td class="menuTd vt">附件：</td>
					<td colspan="6" >
						<div>
							<%-- <input style="cursor:pointer;" type="file" id="uploadAttachment_file" size="1" > --%>
							<div></div>
						</div>
						<div class="img_list" id="attr_is">
						</div> 
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%--新建信息内容开始--%>
	<div class="newsMessage_content clearfix">
		<div class="newsMessage_range">
		 <table class="main_output_table">
				<tbody>
					<tr>
						<td class="menuTd vt" style="width: 60px;">发布范围：</td>
						<td colspan="4">
						    <div class="clearfix">
								<div class="fl">
									<input type="radio" value="radio_department" name="releaseBounds" <s:if test="1==infoReleaseEntity.releaseScopeType">checked</s:if>  onclick="clickReleaseBounds();" />按部门
									<span style="display:none;"><input type="radio" value="radio_staff" name="releaseBounds" <s:if test="2==infoReleaseEntity.releaseScopeType">checked</s:if>/>按人员</span>
									<input type="radio" value="radio_role" name="releaseBounds" <s:if test="3==infoReleaseEntity.releaseScopeType">checked</s:if>  onclick="clickReleaseBounds();"  id="releaseBounds_raido"/>按岗位角色
									<select id="roleCode" name="roleCode"  value="" <s:if test="3!=infoReleaseEntity.releaseScopeType"> style="display:none;"</s:if> >
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
							    
							    <div id="tree2Div">
							    	<ul>可选人员列表</ul>
							    </div>
							    <div id="tree3Div">
							    	<ul>已选人员列表</ul>
							    </div>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
			<div class="newsMessage_more">
			<table class="main_output_table">
				<tbody>
					<tr>
						<td class="menuTd">紧急程度：</td>
						<td>
							<select id="cmsUrgencyLevel" name="cmsInforelease.timeSensibility">
								<s:iterator value="cmsUrgencyLevelList" id="cmsUL">
									<option value="<s:property value='#cmsUL.cmsUrgencyLevel'/>" <s:if test="#cmsUL.cmsUrgencyLevel==infoReleaseEntity.timeSensibility">selected="selected"</s:if>><s:property value='#cmsUL.cmsUrgencyLevel'/></option>
								</s:iterator>
							</select>
						</td>
						<td class="menuTd">发布栏目：</td>
						<td>
							<select id="cmsReleaseDest" name="cmsInforelease.releaseDestination">
								<s:iterator value="cmsPortalItemList" id="portalItem">
										<option value="<s:property value='#portalItem.itemCode'/>" <s:if test="#portalItem.itemCode==infoReleaseEntity.releaseDestination">selected="selected"</s:if>><s:property value='#portalItem.itemName'/></option>
								</s:iterator>
							</select>
							<%--
								<option value="1319795382257" <s:if test="1319795382257==infoReleaseEntity.releaseDestination">selected="selected"</s:if>>公司重要公告</option>
								<option value="1319795382258" <s:if test="1319795382258==infoReleaseEntity.releaseDestination">selected="selected"</s:if>>图片新闻</option>
								<option value="1319795382259" <s:if test="1319795382259==infoReleaseEntity.releaseDestination">selected="selected"</s:if>>天气预报</option>
								<option value="1319795382260" <s:if test="1319795382260==infoReleaseEntity.releaseDestination">selected="selected"</s:if>>常用通迅录</option>
								<option value="1319795382254" <s:if test="1319795382254==infoReleaseEntity.releaseDestination">selected="selected"</s:if>>当前任务统计</option>
							 --%>
						</td>
					</tr>
					<tr>
						<td class="menuTd">发布周期：</td>
						<td colspan="4">
							<input id="releasePeriodStart" name="cmsInforelease.releasePeriodStart" type="text" value="<s:property value='infoReleaseEntity.releasePeriodStart' />"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
							&nbsp;到
							<input id="releasePeriodEnd" name="cmsInforelease.releasePeriodEnd" type="text" value="<s:property value='infoReleaseEntity.releasePeriodEnd' />"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
						</td>
					</tr>
					<tr>
						<td class="menuTd vt">发布脚注：</td>
						<td colspan="4">
							<textarea name="cmsInforelease.releasenotes" class="input-text" style="width:90%" >${infoReleaseEntity.releasenotes }</textarea>
							<p>
								<input type="checkbox" class="input_checkbox" id="ckBizBox" name="cmsInforelease.isSentToMsgbox" <s:if test="1==infoReleaseEntity.isSentToMsgbox">checked</s:if> value="<s:property value='infoReleaseEntity.isSentToMsgbox' />" />推送到消息盒子
								<input type="checkbox" class="input_checkbox" id="ckSms" name="cmsInforelease.isInformedWithsms" <s:if test="1==infoReleaseEntity.isInformedWithsms">checked</s:if> value="<s:property value='infoReleaseEntity.isInformedWithsms' />" />短信通知
								<input type="checkbox" class="input_checkbox" id="ckSEM" name="cmsInforelease.isEmail" <s:if test="1==infoReleaseEntity.isEmail">checked</s:if> value="<s:property value='infoReleaseEntity.isEmail' />" />邮件
							</p>
						</td>
					</tr>
					<tr>
						<%-- <td class="menuTd vt">发布人：</td>
						<td><input type="text" class="input_text" name="T_CMS_InfoRelease#releaser" /></td>
						<td class="menuTd vt">最后变更时间：</td>
						<td>
							<input id="lastModifiedTime" type="text" value="<s:property value='infoReleaseEntity.lastModifiedTime' />" />
							<input type="button" value="时间" onclick="fPopCalendar(event,document.getElementById('lastModifiedTime'),document.getElementById('lastModifiedTime'),true)" />
						</td> --%>
						<td class="menuTd vt">最后变更时间：</td>
						<td colspan="4">
							<label><s:property value='infoReleaseEntity.lastModifiedTime' /></label>
							<input id="lastModifiedTime" type="hidden" value="<s:property value='infoReleaseEntity.lastModifiedTime' />" />
							<%-- <input type="button" value="时间" onclick="fPopCalendar(event,document.getElementById('lastModifiedTime'),document.getElementById('lastModifiedTime'),true)" /> --%>
						</td>
					</tr>
					<tr>
						<td class="menuTd vt">发布历史：</td>
						<td colspan="4">
							<span id="releaseHistoryspan"></span>
							<textarea id="releaseHistoryspanhide" name="cmsInforelease.releaseHistory" style="display: none;"><s:property value='infoReleaseEntity.releaseHistory' /></textarea>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
			
		</div>
		<%-- <div>由于超出当前发布人的发布权限范围，请选择审批人（可逐级网上审批，直到最终审批人具权，也可选择公司指定审批人或公司领导审批）</div>--%>
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
		<s:if test="isApprover==0">
			<div id="limitDiv" style="display: none"></div>
		</s:if>
	    	
						
					
		<div class="newsMessage_but">
		<s:if test="infoReleaseStatus == 21">
			<s:if test="isApprover==1">
			<input id="btnReleaseInfo" type="submit" onclick="return clickCheckedapprover();" class="input_button" value="发布" />
		</s:if>
		<s:if test="isApprover==0">
			<input id="btnApprove" type="submit" class="input_button" onclick="return nomvalidateLimit(); return clickCheckedapprover();" value="通过" />
		</s:if>
			<input type="button" id="btnReject" class="input_button release_but" value="驳回" />
		</s:if>
		<s:else>
		   	<input type="button" id="btnPrevLook" class="input_button" value="返回查看" />
		</s:else>	
			<input id="isApprover" name="isApprover" value="<s:property value="isApprover"/>" type="hidden"/>
		</div>
	<%--新建信息内容结束--%>
	
<%--主体结束--%>
</form>
</body>
</html>