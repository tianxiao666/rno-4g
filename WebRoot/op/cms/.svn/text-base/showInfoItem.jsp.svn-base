<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*;" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<title>新建信息</title>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/showInfoItem.js"></script>

</head>
<body>
<input type="hidden" id="infoReleaseId" value="<s:property value='infoReleaseId' />" />
<input type="hidden" id="infoItemId" value="<s:property value='infoItemId' />" />
<input type="hidden" id="Itype" value="<s:property value='Itype' />" />
<input type="hidden" id="tabType" value="<s:property value='tabType' />" />
<input type="hidden" id="infoReleaseStatus" name="infoReleaseStatus" value="<s:property value='infoReleaseStatus' />" />
	<form action="addInfoItemAction" method="post" id="operaForm">
	<input type="hidden" name="cmsInfoitem.author" value="<s:property value='infoItemEntity.author' />" />
<input type="hidden" name="cmsInfoitem.drafttime" value="<s:property value='infoItemEntity.drafttime' />" />
<input type="hidden" name="cmsInfoitem.infoType" value="<s:property value='infoItemEntity.infoType' />" />
    <%--新建信息进度开始--%>
	<div class="cms_top">发布信息</div>
			<div class="newsMessage">
				<div class="newsMessage_layout">
					<b>发布形式：</b><span><input type="radio" name="1">短信信息</span><span><input type="radio" name="1" checked="checked">普通信息</span>
				</div>
				<%--新建信息进度开始--%>
				<div class="newsMessage_schedule">
					<ul class="ks_steps blue">
						<li class="ks_steps_item current" style="z-index: 500; ">新建信息<div class="trigon"><span class="bor"></span><span class="blo"></span></div></li>
						<li class="ks_steps_item done" style="z-index: 499; ">发布</li>
					</ul>
				</div>
	<%--新建信息进度结束--%>
	
	<input type="hidden" name="infoItemId" value="<s:property value='infoItemId' />" />
	<input type="hidden" name="cmsInfoitem.status" value="<s:property value='infoItemEntity.status' />" />
	<input name="addorUpdate" value="update" type="hidden"/>
	<%--新建信息内容开始--%>
	<div class="newsMessage_content">
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
					<td>
					    <input name="cmsInfoitem.label" type="text" id="nom_label" class="input_text" value="${infoItemEntity.label }" style="width:90%" /><span class="red"></span>
					</td>
				</tr>
				<tr>
					<td class="menuTd">标题/主题<span class="redStar">*</span>：</td>
					<td colspan="6"><input name="cmsInfoitem.title" type="text" class="input_text" id="nom_titlte" value="<s:property value='infoItemEntity.title' />" style="width:90%" /><span class="red"></span></td>
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
		    
		<div class="newsMessage_but">
		    <input  type="submit" class="input_button" onclick="return nomvalidateLimit();" value="下一步" />
		    <input id="btnNext" type="button" class="input_button" value="取消" />
		</div>
	</div>
	<%--新建信息内容结束--%>
	</form>
</body>
</html>