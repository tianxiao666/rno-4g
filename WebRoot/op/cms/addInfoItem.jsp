<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*;" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/addInfoItem.js"></script>
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/cms.css" type="text/css" />
<STYLE type="text/css">
#uploadAttachment_span{ position:relative;}
#uploadAttachment_file{ position:absolute; z-index:100;opacity:0;filter:alpha(opacity=0); margin-top:-5px;}
</STYLE>
</head>
<body>
<input type="hidden" id="infoType" value="${infoItemEntity.infoType  }"/>
    <%--新建信息进度开始--%>
	<div class="cms_top">发布信息</div>
			<div class="newsMessage">
				<div class="newsMessage_layout">
				<s:if test="infoItemEntity.infoType == 'sms'">
					<b>发布形式：</b><span><input class="smsnor" type="radio" name="1" checked="checked" value="sms" onclick="clickNormalorSMS('smsdiv','normaldiv','reportdiv');">短信信息</span><span><input type="radio" name="1"  value="nor"  class="smsnor"  onclick="clickNormalorSMS('normaldiv','smsdiv','reportdiv');">普通信息</span><span><input type="radio" name="1"  value="report"  class="smsnor" onclick="clickNormalorSMS('reportdiv','normaldiv','smsdiv');">统计表格</span>
				</s:if>
				<s:else>
					<b>发布形式：</b><span><input class="smsnor" type="radio" name="1" value="sms" onclick="clickNormalorSMS('smsdiv','normaldiv','reportdiv');">短信信息</span><span><input type="radio" name="1"  value="nor"  checked="checked"  class="smsnor" onclick="clickNormalorSMS('normaldiv','smsdiv','reportdiv');">普通信息</span><span><input type="radio" name="1"  value="report"  class="smsnor" onclick="clickNormalorSMS('reportdiv','normaldiv','smsdiv');">统计表格</span>
				</s:else>
				</div>
				<%--新建信息进度开始--%>
				<div class="newsMessage_schedule" id="newsMessage_schedule_no">
					<ul class="ks_steps blue">
						<li class="ks_steps_item current">新建信息<div class="trigon"><span class="bor"></span><span class="blo"></span></div></li>
						<li class="ks_steps_item done">发布</li>
					</ul>
				</div>
	<%--新建信息进度结束--%>
	<s:if test="infoItemEntity.infoType == 'sms'">
		<div  id="normaldiv" style="display:  none;">
	</s:if>
	<s:else>
		<div  id="normaldiv" >
	</s:else>
	<form action="addInfoItemAction" method="post" id="operaForm" onsubmit="return nomvalidateLimit();" enctype="multipart/form-data">
	<input name="addorUpdate" value="add" type="hidden"/>
	
	<%--新建信息内容开始--%>
	<div class="newsMessage_content" >
		
		
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
								<option value="<s:property value='#level.cmsImportantLevel'/>" <s:if test="#level.cmsImportantLevel==infoItemEntity.importanceLevel">selected="selected"</s:if>><s:property value='#level.cmsImportantLevel'/></option>
							</s:iterator>
							<%-- <option value="最高" selected="">最高</option>
							<option value="较高">较高</option>
							<option value="普通">普通</option>
							<option value="低">低</option> --%>
						</select>
					</td>
					<td class="menuTd">文件号：</td>
					<td class="w300">
					    <input name="cmsInfoitem.label" type="text" id="nom_label" class="input_text" style="width:90%" /><span class="red"></span>
					</td>
				</tr>
				<tr>
					<td class="menuTd">标题/主题<span class="redStar">*</span>：</td>
					<td colspan="6"><input name="cmsInfoitem.title" type="text" class="input_text" style="width:90%" id="nom_titlte" value="${infoItemEntity.title }"/><span class="red"></span></td>
				</tr>
				<tr>
					<td class="menuTd vt">正文<span class="redStar">*</span>：</td>
					<td colspan="6">
					    <textarea name="cmsInfoitem.content" class="input-text" style="width:90%" id="nom_content">${infoItemEntity.content }</textarea><span class="red"></span>
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
					    <%-- <a href="#">添加图片</a>
						<input type="checkbox" class="input_checkbox" />在线图片,超链接<input type="text" class="input_text ml10" style="width:78%;" />
						<div class="img_list" style="display:none;">
						    <ul>
							    <li><span></span>评估.gif(1.91k) <a href="#">在线预览</a> <a href="#">删除</a></li>
								<li><span></span>评估.gif(1.91k) <a href="#">在线预览</a> <a href="#">删除</a></li>
							</ul>
						</div> --%>
					</td>
				</tr>
				<tr>
					<td class="menuTd vt">附件：</td>
					<td colspan="6" >
						<div id="uploadAttachment_div">
							<%-- <input style="cursor:pointer;" type="file" id="uploadAttachment_file" size="1" > --%>
							<div><a href="javascript:void(0)" id="addAttachment" style="text-decoration:underline;">添加附件</a></div>
						</div>
						
					</td>
				</tr>
				<%-- <tr>
					<td class="menuTd vt">外部链接：</td>
					<td colspan="6"><input type="text" class="input_text" style="width:90%" /></td>
				</tr> --%>
			</tbody>
		</table>
		<a href="#" onclick="clickShowMore('advanced_mode_but1');" class="advanced_mode_but" id="advanced_mode_but1" style="display:block;"><em>▼</em>&nbsp;更多内容</a>
		<a href="#" onclick="clickShowMore('advanced_mode_but2');" class="advanced_mode_but" id="advanced_mode_but2" style="display:none;"><em>▲</em>&nbsp;更多内容</a>
		    
		<div class="newsMessage_but">
					<input id="btnNext" type="submit" class="input_button" value="下一步" />
					<input type="button" class="" value="取消" onclick="clickBack();"/>
				</div>
	</div>
	<%--新建信息内容结束--%>
	
	</form>
	</div>
	
	
	<%-- 短信 --%>
	<s:if test="infoItemEntity.infoType == 'sms'">
		<div  id="smsdiv">
	</s:if>
	<s:else>
		<div  id="smsdiv"  style="display:  none;">
	</s:else>
	<form action="addsmsInfoAction" method="post" onsubmit="return validateLimit()" target="stay" id="operaForm1">
	<input type="hidden" id="infoItemId" name="infoItemId" value="<s:property value='infoItemId' />" />
	<input type="hidden" id="releaseScopeList" name="releaseScopeList" />
	<input type="hidden" id="releaseOrApprove" name="releaseOrApprove" value="1" />
	<input type="hidden" id="releaseScopeStaffName" name="releaseScopeStaffName" />
	<input name="cmsInforelease.isSentToMsgbox" value="0" type="hidden"/>
	<input name="cmsInforelease.isInformedWithsms" value="1" type="hidden"/>
	<input name="cmsInforelease.isEmail" value="0" type="hidden"/>
	<div class="newsMessage_content clearfix">
					<table class="main_output_table">
						<tbody>
							<tr>
								<td class="menuTd">信息类型<span class="redStar">*</span>：</td>
								<td colspan="4">
									<select id="cmsCategorysms" name="cmsInfoitem.category">
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
									<textarea id="textarea_c" name="cmsInfoitem.content" class="input-text" style="width:90%" >${infoItemEntity.content }</textarea><span class="red"></span>
								</td>
							</tr>
							<tr>
								<td class="menuTd vt">发布范围：</td>
						<td colspan="4">
						    <div class="clearfix">
								<p>
									<input type="radio" checked value="radio_department" name="releaseBounds" onclick="clickReleaseBounds();">按部门
									<span style="display:none;"><input type="radio" value="radio_staff" name="releaseBounds">按人员</span>
									<input type="radio" value="radio_role" name="releaseBounds"  id="releaseBounds_raido" onclick="clickReleaseBounds();">按岗位角色
									<select id="roleCode" name="roleCode" style="display:none;">
										<s:iterator value="roleList" id="roleItem">
											<option value="<s:property value='#roleItem.roleCode'/>"><s:property value='#roleItem.name'/></option>
										</s:iterator>
									</select>
								</p>
								
							</div>
							    <%--放树框架的容器--%>
							    <ul id="treeDiv" style="width: 400px;"  class="filetree orgTree treeview"></ul>
						</td>
					</tr>
						</tbody>
					</table>
				</div>
				<div id="limitDiv"  class="newsMessage_approver">
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
					<input type="submit" class="" value="发布" />
					<input type="button" class="" value="取消"  onclick="clickBack();"/>
				</div>
	</form>
	</div>
	
	
	<s:if test="infoItemEntity.infoType == 'report'">
		<div  id="reportdiv">
	</s:if>
	<s:else>
		<div  id="reportdiv" style="display:  none;">
	</s:else>
	<form action="addInfoItemReportAction" method="post" id="operaForm2">
	<input name="addorUpdate" value="add" type="hidden"/>
	
	<%--新建信息内容开始--%>
	<div class="newsMessage_content" >
		
		
		<table class="main_output_table">
			<tbody>
				<tr>
					<td class="menuTd">信息类型<span class="redStar">*</span>：</td>
					<td>
						<select id="cmsCategory" name="cmsInfoitem.category">
							<%--<s:iterator value="cmsCategoryList" id="cate">
								<option value="<s:property value='#cate.cmsCategory'/>" <s:if test="#cate.cmsCategory==infoItemEntity.category">selected="selected"</s:if>><s:property value='#cate.cmsCategory'/></option>
							</s:iterator>--%>
								<option value="项目考核排名" selected="">项目考核排名</option>
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
								<option value="<s:property value='#level.cmsImportantLevel'/>" <s:if test="#level.cmsImportantLevel==infoItemEntity.importanceLevel">selected="selected"</s:if>><s:property value='#level.cmsImportantLevel'/></option>
							</s:iterator>
							<%-- <option value="最高" selected="">最高</option>
							<option value="较高">较高</option>
							<option value="普通">普通</option>
							<option value="低">低</option> --%>
						</select>
					</td>
					<td class="menuTd">文件号：</td>
					<td>
					    <input name="cmsInfoitem.label"  id="reportlabel" type="text" class="input_text" style="width:90%" /><span class="red"></span>
					</td>
				</tr>
				<tr>
					<td class="menuTd">标题/主题<span class="redStar">*</span>：</td>
					<td colspan="6"><input name="cmsInfoitem.title" type="text" id="reportTitle" class="input_text" style="width:90%" value="${infoItemEntity.title }"/><span class="red"></span></td>
				</tr>
				<tr>
					<td class="menuTd vt">正文<span class="redStar">*</span>：</td>
					<td colspan="6">
					    <textarea name="cmsInfoitem.content" id="reportcontent" class="input-text" style="width:90%">${infoItemEntity.content }</textarea><span class="red"></span>
						<input type="button" class="input_button vt mt2" value="高级" />
					</td>
				</tr>
				<div id="nowTable2" style="display: none">
										
				</div>
				</form>
					<tr>
								<td class="menuTd">统计数据：</td>
								<td colspan="6">
									<form id="excel_form" action="loadReoprtExcelAction" method="post">
										<input type="file" name="uploadReportAttachment" onchange="loadReoprtExcel();"/>
									<span class="checkTable"><em class="checkTable_icon"></em><em class="checkTable_text" onclick="showDialong();">查看现有表格</em></span>
									</form>
								</td>
								
					</tr>
					<td class="menuTd"></td>
								<td colspan="6">
									<div class="nowTable" id="nowTable">
									</div>
								</td>
							</tr>
			</tbody>
		</table>  
		<div class="newsMessage_but">
					<input id="btnNext" type="button" onclick="submitButton();" class="input_button" value="下一步" />
					<input type="button" class="" value="取消" onclick="clickBack();"/>
				</div>
	</div>
	<%--新建信息内容结束--%>
	<div class="searchTable_dialog">
		<div class="dialog" id="searchTable_Dialog" style="display: none;">
					<div class="dialog_header">
						<div class="dialog_title">搜索现有表格：项目考核排名</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn" onclick="hideDialong();"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div class="searchTable_info">
							<p>
								项目：<select name="" id="projectName">
										<option value="广州基站维护">广州基站维护</option>
										<option value="广州直放站室分维护">广州直放站室分维护</option>
										<option value="广州传输管线代维">广州传输管线代维</option>
										<option value="韶关基站代维">韶关基站代维</option>
										<option value="深圳基站代维">深圳基站代维</option>
										<option value="深圳传输代维">深圳传输代维</option>
										<option value="潮州直放站">潮州直放站</option>
										<option value="潮州传输代维">潮州传输代维</option>
										<option value="汕头基站代维">汕头基站代维</option>
										<option value="汕头传输代维">汕头传输代维</option>
										<option value="汕尾传输代维">汕尾传输代维</option>
										<option value="梅州基站代维">梅州基站代维</option>
										<option value="梅州直放站代维">梅州直放站代维</option>
										<option value="梅州传输代维">梅州传输代维</option>
										<option value="揭阳基站代维">揭阳基站代维</option>
										<option value="揭阳传输代维">揭阳传输代维</option>
										<option value="河源基站代维">河源基站代维</option>
										<option value="惠州直放站">惠州直放站</option>
										<option value="惠州基站代维">惠州基站代维</option>
										<option value="惠州传输代维">惠州传输代维</option>
										<option value="东莞本地传输网代维">东莞本地传输网代维</option>
									</select>
								<em class="redStar">*</em>
							</p>
							<p>
								年度：<select name="" id="year">
										
									</select>
								<em class="redStar">*</em>
							</p>
						</div>
						<div class="dialog_but">
							<button class="aui_state_highlight" type="button" onclick="getCmsPeportProjectAppraisal();">确定</button>
							<button class="aui_state_highlight" type="button" onclick="hideDialong();">取消</button>
						</div>
					</div>
				</div>
				<div class="black" id="black"></div>
			</div>
</body>
</html>