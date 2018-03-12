<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<title>用户自定义统计模板</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" type="text/css"
	href="../../jslib/dialog/dialog.css" />
<script type="text/javascript" src="js/rno_report_manage.js"></script>
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	/*background: #EEEEEE;*/
}

.dict_left {
	float: left;
	border: 1px;
	margin: 5px
}

.dict_right {
	float: left;
	border: 1px;
	margin: 5px
}
.dispNameCls{
    cursor:pointer;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(function() {
			//tab选项卡
			tab("div_tab", "li", "onclick");//项目服务范围类别切换
		});

		
	});

	
</script>

</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			操作正在进行 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto;background:#ffffff">
		<div id="div_tab" class="div_tab divtab_menu">
			<ul>
				<li class="selected">新增报表模板</li>
				<li>报表模板查询</li>

			</ul>
		</div>

		<div class="divtab_content">
			<div id="div_tab_0">
				<div class="dict_left" style="float: left">
					
					<table class="main-table1 half-width">
						<tr>
							<th>备选报表头</th>
						</tr>
						<s:iterator value="rnoTableDicts" var="one">
							<tr>
								<td><span class="dispNameCls"><s:property
											value="#one.fieldDisplayName" /></span> <input type="hidden"
									value="<s:property value='#one.fieldDbName' />" /></td>
							</tr>
						</s:iterator>
					</table>
				</div>
				<div class="dict_right" style="float: left">
				<%--  action="saveReportTemplateForAjaxAction" --%>
					<form id="formTemplate" method="post">
						<span style="display:block;margin-bottom:6px">报表名称： <input type="text"
							name="reportTemplate.reportName" class="required"/> </span> <div style="display:block;margin-bottom:6px">当前选择表头：<span
							id="curFieldName"></span></div><span style="display:block;margin-bottom:6px">报表中显示的名称：<input type="text"
							id="hiddenForDisplayName" /> <input type="hidden"
							id="hiddenDbField" /> </span><span style="display:block;margin-bottom:6px"> <input type="button" value="添加"
							onclick="addOneField();" /> </span> <span style="display:block;margin-bottom:6px">已选报表头</span>
						<table id="repTempTab" class="main-table1 half-width">
							<tr>
								<th>顺序</th>
								<th>选择表头</th>
								<th>显示名称</th>
								<th>操作</th>
							</tr>
							<tr id="btnTr">
								<td colspan="4"><input id="saveTemplate" type="submit" value="保存模板" /></td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<div id="div_tab_1" style="display:none;">
			<form id="ReportTemplateDataForm" method="post">
			 
			<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="3" /> 
			<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" />
			<input type="hidden" id="hiddenTotalPageCnt" /> 
			<input type="hidden" id="hiddenTotalCnt" />
			<input type="hidden" id="hiddenRptName" class="hiddenRptNameCls" name="reportName" />
			<input type="hidden" id="hiddenDispName" class="hiddenDispNameCls" name="displayName" />
			</form>
			<table class="main-table1 half-width" style="padding-top: 10px;">
			<tr>
			<th>报表名</th>
			<th>字段名</th>
			</tr>
			<tr  align="center">
			<td><input type="text" id="reportName" name="reportName"  /></td>
			<td><input type="text" id="displayName" name="displayName"  /></td>
			</tr>
			<tr>
			<td colspan="2" style=" text-align: right;"><input id="searchRptTempBtn" type="button" name="search" style="width: 90px;" value="查 询"></td>
			</tr>
			</table>
			<span style="display:block;margin-bottom:4px;font-weight: bold;">报表模板列表</span>
			<div style="float: left;width: 60%">
			<table id="queryReportTemplateDataTab" class="greystyle-standard" width="100%">
			<tr>
				<th style="width: 8%">报表名</th>
				<th style="width: 10%">表名</th>
				<th style="width: 8%">作者</th>
				<th style="width: 8%">创建时间</th>
				<th style="width: 8%">修改时间</th>
				<%-- th style="width: 8%">状态</th --%>
				<%-- th style="width: 8%">应用范围</th --%>
				<%-- th style="width: 8%">范围值</th --%>
				<th style="width: 8%">操作</th>
			</tr>
			</table>
				<div class="paging_div" id='ReportTemplateDataPageDiv'
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getReportTemplateRecordData,'ReportTemplateDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getReportTemplateRecordData,'ReportTemplateDataForm')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getReportTemplateRecordData,'ReportTemplateDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getReportTemplateRecordData,'ReportTemplateDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getReportTemplateRecordData,'ReportTemplateDataForm')"></a>
	</div>
			</div>
			
			<div style="float:left;width: 37%;margin-left: 6px">
			<table id="RptTemplateDetailTab" class="greystyle-standard" width="100%" cellspacing="0" cellpadding="0" border="1px" style="font-size:12px;">
			<tr>
				<th style="width: 8%">表字段</th>
				<th style="width: 10%">中文名</th>
				<th style="width: 8%">显示顺序</th>
			</tr>
			</table>
			</div>
			</div>
		</div>
	</div>



	<div id="operSuc"
		style="display:none; top:40px;left:400px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span> 操作成功</span></td>
			</tr>
		</table>

	</div>
		 <div id="operInfo" style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
            	<table height="100%" width="100%" style="text-align:center">
                	<tr>
                    	<td>
                        	<span id="operTip"></span>
                        </td>
                    </tr>
                </table>
             
            </div>
</body>
</html>
