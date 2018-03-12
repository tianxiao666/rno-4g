<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>NCS分析结果查询</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_structure_ncs_result_search.js?v=<%=(String)session.getAttribute("version")%>"></script>


<style type="text/css">
.dict_left {
	width: 130px;
	float: left;
	left: 0;
	z-index: 1;
	border: 1px;
	margin: 5px
}

.dict_right {
	width: 100%;
	left: 200px;
	float: left;
	border: 1px;
	margin: 5px
}
</style>
</head>

<body>
	
		<form id="ncsTaskDataForm" method="post">
			<input type="hidden" id="hiddenPageSize" name="page.pageSize"
				value="25" /> <input type="hidden" id="hiddenCurrentPage"
				name="page.currentPage" value="1" /> <input type="hidden"
				id="hiddenTotalPageCnt" /> <input type="hidden"
				id="hiddenTotalCnt" />
			<div>
				<table class="main-table1 half-width"
					style="padding-top: 10px;margin:0px;width:100%">
					<tr>
						<td class="menuTd" style="text-align: center"><span
							style="padding-top: 0px">任务状态</span></td>
						<td class="menuTd" style="text-align: center">任务提交时间</td>
					</tr>
					<tr>
						<td><select name="cond['taskGoingStatus']">
								<option value="">全部</option>
								<option value="运行中">运行中</option>
								<option value="失败">失败</option>
								<option value="成功完成">成功完成</option>
	
	
						</select></td>
						<td style="text-align: left">从 <s:set name="begtime"
								value="new java.util.Date()" /> <input
							name="cond['begTime']"
							value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
							type="text"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							readonly class="Wdate input-text" style="width: 132px;" /> <br />至
							<s:set name="endtime" value="new java.util.Date()" /> <input
							name="cond['endTime']"
							value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
							type="text"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							readonly class="Wdate input-text" style="width: 132px;" />
	
						</td>
					</tr>
					<tr>
						<td style=" text-align: right" colspan="2"><input
							type="button" onclick="" value="查  询" style="width: 90px;"
							id="searchTaskBtn" name="search" /></td>
					</tr>
				</table>
			</div>
		</form>
		<div style="max-height:350px;overflow-x:auto">
			<table id="queryTaskResultTab" class="greystyle-standard"
				width="100%">
				<tr>
					<th>选项</th>
				    <th>汇总范围</th>
				    <th>汇总区域名称</th>
				    <th>分析的NCS文件数量</th>
					<th>启动时间</th>
					<th>完成时间</th>
					<th>任务状态</th>
					<%--<th>操作</th>--%>
					</tr>
			</table>
			<%--<form id='getReportForm' action='exportNcsAnalysisReportAction' method='post' style="display:none">
			     <input type='input' id='taskId' name='taskId' value='' />
			</form>--%></div>
		<div class="paging_div" id='ncsTaskResultPageDiv'
			style="border: 1px solid #ddd">
			<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
				条记录
			</span> <a class="paging_link page-first" title="首页"
				onclick="showListViewByPage('first',doQueryNcsTask,'ncsTaskDataForm')"></a>
			<a class="paging_link page-prev" title="上一页"
				onclick="showListViewByPage('back',doQueryNcsTask,'ncsTaskDataForm')"></a>
			第 <input type="text" id="showCurrentPage"
				class="paging_input_text" value="1" /> 页/<em
				id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
				title="GO"
				onclick="showListViewByPage('num',doQueryNcsTask,'ncsTaskDataForm')">GO</a>
			<a class="paging_link page-next" title="下一页"
				onclick="showListViewByPage('next',doQueryNcsTask,'ncsTaskDataForm')"></a>
			<a class="paging_link page-last" title="末页"
				onclick="showListViewByPage('last',doQueryNcsTask,'ncsTaskDataForm')"></a>
		</div>

					
</body>
</html>
