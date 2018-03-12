<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/cms.css" type="text/css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/addInfoRelease.js"></script>


<style>
#tree2Div{display:none;margin-left:15px;float:left;z-index: 50;background:#fff; border:0px solid red; border-top:none;width: 190px; min-height:186px; text-align:left;margin-top:1px; line-height:15px;}
#tree3Div{display:none;margin-left:15px;float:left;z-index: 50;background:#fff; border:0px solid red; border-top:none;width: 190px; min-height:186px; text-align:left;margin-top:1px; line-height:15px;}

#tree1 span{ cursor:pointer;}
</style>
<script type="text/javascript">
</script>

</head>
<body>
<form action="addInfoReleaseAction" method="post" onsubmit="return validateLimit()" target="stay" id="operaForm">
<input type="hidden" id="infoItemId" name="infoItemId" value="<s:property value='infoItemId' />" />
<input type="hidden" id="cmsCategory" name="cmsCategory" value="<s:property value='infoItemEntity.category' />" />
<input type="hidden" id="releaseScopeList" name="releaseScopeList" />
<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="1" />
<input type="hidden" id="releaseScopeStaffName" name="releaseScopeStaffName" />
	<%--主体开始--%>
    <%--新建信息进度开始--%>
	<div class="cms_top">发布信息</div>
			<div class="newsMessage">
				<div class="newsMessage_layout">
					<b>发布形式：</b><span><input type="radio" name="1" disabled="disabled">短信信息</span><span><input type="radio" name="1" checked="checked" disabled="disabled">普通信息</span>
				</div>
				<div class="newsMessage_schedule">
					<ul class="ks_steps blue">
						<li class="ks_steps_item done" style="z-index: 500; ">新建信息<div class="trigon"><span class="bor"></span><span class="blo"></span></div></li>
						<li class="ks_steps_item current" style="z-index: 499; ">发布</li>
					</ul>
				</div>
	<%--新建信息进度结束--%>
	
	<%--新建信息内容开始--%>
	<div class="newsMessage_content clearfix">
		<div class="newsMessage_range">
		 <table class="main_output_table">
				<tbody>
					<tr>
						<td class="menuTd vt" style="width: 60px;">发布范围：</td>
						<td colspan="4">
						    <div class="clearfix">
								<p>
									<input type="radio" checked="checked" value="radio_department" name="releaseBounds" onclick="clickReleaseBounds();">按部门
									<span style="display:none;"><input type="radio" value="radio_staff" name="releaseBounds">按人员</span>
									<input type="radio" value="radio_role" name="releaseBounds" id="releaseBounds_raido" onclick="clickReleaseBounds();">按岗位角色
									<select id="roleCode" name="roleCode"  style="display:none;">
										<s:iterator value="roleList" id="roleItem">
											<option value="<s:property value='#roleItem.roleCode'/>"><s:property value='#roleItem.name'/></option>
										</s:iterator>
									</select>
								</p>
								
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
									<option value="<s:property value='#cmsUL.cmsUrgencyLevel'/>"><s:property value='#cmsUL.cmsUrgencyLevel'/></option>
								</s:iterator>
								<%-- <option value="最高" selected="">最高</option>
								<option value="较高">较高</option>
								<option value="普通">普通</option>
								<option value="低">低</option> --%>
							</select>
						</td>
						<td class="menuTd">发布栏目：</td>
						<td>
							<select id="cmsReleaseDest" name="cmsInforelease.releaseDestination">
								<s:iterator value="cmsPortalItemList" id="portalItem">
									<option value="<s:property value='#portalItem.itemCode'/>"><s:property value='#portalItem.itemName'/></option>
								</s:iterator>
								<%-- <option value="1319795382257" selected="selected">公司重要公告</option>
								<option value="1319795382258">图片新闻</option>
								<option value="1319795382259">天气预报</option>
								<option value="1319795382260">常用通迅录</option>
								<option value="1319795382254">当前任务统计</option> --%>
							</select>
						</td>
					</tr>
					<tr>
						<td class="menuTd">发布周期：</td>
						<td colspan="4">
							<input id="releasePeriodStart" name="cmsInforelease.releasePeriodStart" type="text" class="input_text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
							<span class="red"></span>&nbsp;到
							<input id="releasePeriodEnd" name="cmsInforelease.releasePeriodEnd" type="text" class="input_text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
							<span class="red"></span>
						</td>
					</tr>
					<tr>
						<td class="menuTd vt">发布脚注：</td>
						<td colspan="4">
							<textarea name="cmsInforelease.releasenotes" class="input-text" style="width:90%" ></textarea>
							<p>
								<input type="checkbox" class="input_checkbox" id="ckBizBox" name="cmsInforelease.isSentToMsgbox" value="0" />推送到消息盒子
								<input type="checkbox" class="input_checkbox" id="ckSms" name="cmsInforelease.isInformedWithsms" value="0" />短信通知
								<input type="checkbox" class="input_checkbox" id="ckSEM" name="cmsInforelease.isEmail" value="0" />邮件
							</p>
						</td>
					</tr>
					<tr>
						<%--  <td class="menuTd vt">发布人：</td>
						<td><input type="text" class="input_text" name="T_CMS_InfoRelease#releaser" /></td>
						<td class="menuTd vt">最后变更时间：</td>
						<td>
							<input id="lastModifiedTime" type="text" />
							<input type="button" value="时间" onclick="fPopCalendar(event,document.getElementById('lastModifiedTime'),document.getElementById('lastModifiedTime'),true)" />
						</td> --%>
						<%-- td class="menuTd vt">最后变更时间：</td>
						<td colspan="4">
							<input id="lastModifiedTime" type="text" />
							<input type="button" value="时间" onclick="fPopCalendar(event,document.getElementById('lastModifiedTime'),document.getElementById('lastModifiedTime'),true)" />
						</td> --%>
					</tr>
					<tr>
						<td class="menuTd vt">发布历史：</td>
						<td colspan="4">
							<label id="releaseHistory" name="cmsInforelease.releaseHistory" class="input-text" style="width:100%; border: none; background: none;" ></label>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
			
		</div>
	    	<%-- <div>由于超出当前发布人的发布权限范围，请选择审批人（可逐级网上审批，直到最终审批人具权，也可选择公司指定审批人或公司领导审批）</div>--%>
	    	<div id="limitDiv" class="newsMessage_approver">
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
		<div class="newsMessage_but">
			<input id="btnReleaseInfo" type="submit" class="input_button" onclick="return validateLimit();" value="发布" />
		    <input id="btnPrevLook" type="button"  class="input_button release_but" value="返回查看" />
		</div>
	<%--新建信息内容结束--%>
</form>
</body>
</html>