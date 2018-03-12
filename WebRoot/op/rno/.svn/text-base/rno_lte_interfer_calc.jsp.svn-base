<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>区域PCI翻频方案</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<%@include file="commonheader.jsp"%>
	<script type="text/javascript" src="js/rno_lte_interfer_calc.js?v=<%=(String)session.getAttribute("version")%>"></script>

  <script type="text/javascript">
  $(document).ready(function(){

		var cityIdParam = $("#cityIdParam").val();

		if(cityIdParam == "0" || cityIdParam == 0){

		} else {
			$("#cityId2").val(cityIdParam);
		}

		//默认加载结构分析,需在获取cityId之后
		getStructureTask();
  });
  </script>
  </head>

  <body>
	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>

	<input type="hidden" id="cityIdParam" value="<s:property value="cityId"/>"/>

	<font style="font-weight: bold;">当前位置： PCI优化 > 区域PCI翻频方案</font>
	<br>
	<div id="structureTaskDiv">
		  	<%-- 结构分析任务查询条件 --%>
		    <div style="width: 80%;margin-top: 20px; margin-left: 6%;">
			    <form id="structureTaskForm" method="post">
					<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="25" />
					<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" />
					<input type="hidden" id="hiddenTotalPageCnt" />
					<input type="hidden" id="hiddenTotalCnt" />

				    <table class="main-table1 half-width" style="padding-top: 10px;">
							<tr>
								<td class="menuTd" style="text-align: center"><span
									style="padding-top: 0px">地市</span></td>
								<td class="menuTd" style="text-align: center">任务名称</td>
								<td class="menuTd" style="text-align: center">任务状态</td>
								<td class="menuTd" style="text-align: center">测量时间</td>
								<td class="menuTd" style="text-align: center">任务提交时间</td>
							</tr>
							<tr>
								<td style="text-align: left">
								省：<select name="provinceId"
									class="required" id="provinceId2">
										<%-- option value="-1">请选择</option --%>
										<s:iterator value="provinceAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
								</select>
								<br/>市：<select name="cond['cityId']" class="required"
									id="cityId2">
										<s:iterator value="cityAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
								</select>
								</td>
								<td>
									<input type="text" id="taskName" name="cond['taskName']" />
									<span style="color:red;width:100px;font-family:华文中宋;" id="nameErrorText"></span>
								</td>
								<td style="text-align: left"><select
									name="cond['taskStatus']" id="facturer">
										<option value="ALL">全部</option>
										<option value="LaunchedOrRunning">运行中</option>
										<option value="Succeded">正常完成</option>
										<option value="Fail">异常终止</option>
										<option value="Initiate">排队中</option>
										<option value="Stopping">停止中</option>
										<option value="Stopped">已停止</option>
								</select></td>
								<td>
									<input id="beginTime" name="cond['meaTime']"
										value=""
										type="text"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
										readonly class="Wdate input-text" style="width: 132px;" />
								</td>

								<td style="text-align: left">
									从
									<input id="startSubmitTime" name="cond['startSubmitTime']"
										value="<s:date name="begtime" format="yyyy-MM-01 00:00:00" />"
										type="text"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endSubmitTime.focus();},maxDate:'#F{$dp.$D(\'endSubmitTime\')}'})"
										readonly class="Wdate input-text" style="width: 132px;" />
									<br/>到
									<input id="endSubmitTime" name="cond['endSubmitTime']"
										value="<s:date name="endtime" format="yyyy-MM-dd HH:mm:ss" />"
										type="text"
										onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startSubmitTime\')}'})"
										readonly class="Wdate input-text" style="width: 132px;" />
								</td>
							</tr>
							<tr>
								<td colspan="5" style=" text-align: left;">
									<div style="margin: 11px"></div>
									<input id="searchStructureTask" type="button" name="searchStructureTask"
									 		style="width: 90px; margin-right:70px;" value="查 询" onclick="">

									 <input id="isMine" name="cond['isMine']" class="forcheck" type="checkbox">只看我的任务
								</td>
							</tr>
					</table>
				</form>
			</div>

			<div style="width: 80%;margin-left: 6%;padding-top: 10px">
				<table width="50%">
					<tbody>
						<tr>
							<td style="width: 5%">
								<p><font style="font-weight: bold">干扰计算任务列表</font></p>
							</td>
							<td style="width: 15%">
								<input id="addTask" type="button" name="addTask"
								 style="width: 85px;" value="新增任务" onclick="">
							</td>
						</tr>
					</tbody>
				</table>
			</div>

			<%-- 结构分析详情列表 --%>
			<div style="width: 80%;margin-left: 6%;padding-top: 10px">
				<table id="structureTaskTable" class="greystyle-standard" width="80%">
					<tr>
						<th style="width: 8%">任务名称</th>
						<th style="width: 8%">任务状态</th>
						<th style="width: 8%">分析地区</th>
						<th style="width: 8%">文件数量情况</th>
						<th style="width: 8%">测试时间段</th>
						<th style="width: 8%">启动时间</th>
						<th style="width: 8%">完成时间</th>
						<th style="width: 8%">操作</th>
					</tr>
				</table>
				<form id='downloadPciFileForm' action='downloadPciFileAction' method='post' style="display:none">
				     <input type='input' id='jobId' name='jobId' value='' />
				     <input type='input' id='mrJobId' name='mrJobId' value='' />
				</form>
			</div>
			<div class="paging_div" id="structureTaskPageDiv"
				style="border: 1px solid #ddd; width: 80%;margin-left: 6%;">
				<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
					条记录
				</span> <a class="paging_link page-first" title="首页"
					onclick="showListViewByPage('first',sumbitStructureForm,'structureTaskForm','structureTaskPageDiv')"></a>
				<a class="paging_link page-prev" title="上一页"
					onclick="showListViewByPage('back',sumbitStructureForm,'structureTaskForm','structureTaskPageDiv')"></a>
				第 <input type="text" id="showCurrentPage" class="paging_input_text"
					value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
					class="paging_link page-go" title="GO"
					onclick="showListViewByPage('num',sumbitStructureForm,'structureTaskForm','structureTaskPageDiv')">GO</a>
				<a class="paging_link page-next" title="下一页"
					onclick="showListViewByPage('next',sumbitStructureForm,'structureTaskForm','structureTaskPageDiv')"></a>
				<a class="paging_link page-last" title="末页"
					onclick="showListViewByPage('last',sumbitStructureForm,'structureTaskForm','structureTaskPageDiv')"></a>
			</div>
	</div>

  	<%-- 查看job报告 --%>
  	<div id="reportDiv" style="display:none">
  			<div style="width: 80%;margin-top: 20px; margin-left: 6%;">
				<form id="viewReportForm">
					<input type="hidden" name="attachParams['jobId']" id="hiddenJobId"
						value="" /> <input type="hidden" id="hiddenPageSize"
						name="page.pageSize" value="25" /> <input type="hidden"
						id="hiddenCurrentPage" name="page.currentPage" value="1" /> <input
						type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
						id="hiddenTotalCnt" value="-1" name="page.totalCnt" />
				</form>
				<ul id="icons" class="ui-widget ui-helper-clearfix"
					style="width:100px;cursor:pointer">
					<li class="ui-state-default ui-corner-all" title="返回列表"
						style="width:100px" onclick="javascript:returnToTaskList();"> << 返回列表</li>
				</ul>
				<table id="reportListTab" class="greystyle-standard"
					style="width:100%;margin-left:0">
					<thead>
						<th>阶段</th>
						<th>开始时间</th>
						<th>结束</th>
						<th>结果</th>
						<th>详细信息</th>
					</thead>
				</table>
				<div class="paging_div" id="reportListPageDiv"
					style="border: 1px solid #ddd">
					<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
						条记录
					</span> <a class="paging_link page-first" title="首页"
						onclick="showListViewByPage('first',queryReportData,'viewReportForm','reportListPageDiv')"></a>
					<a class="paging_link page-prev" title="上一页"
						onclick="showListViewByPage('back',queryReportData,'viewReportForm','reportListPageDiv')"></a>
					第 <input type="text" id="showCurrentPage" class="paging_input_text"
						value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
						class="paging_link page-go" title="GO"
						onclick="showListViewByPage('num',queryReportData,'viewReportForm','reportListPageDiv')">GO</a>
					<a class="paging_link page-next" title="下一页"
						onclick="showListViewByPage('next',queryReportData,'viewReportForm','reportListPageDiv')"></a>
					<a class="paging_link page-last" title="末页"
						onclick="showListViewByPage('last',queryReportData,'viewReportForm','reportListPageDiv')"></a>
				</div>
			</div>
	</div>

	<%-- 查看渲染图 --%>
	<div id="renderImgDiv" style="display:none">
	<%-- <div id="renderImgDiv" > --%>
		<div style="width: 94%;margin-top: 20px; margin-left: 3%;">
			<ul id="icons" class="ui-widget ui-helper-clearfix"
				style="width:100px;cursor:pointer">
				<li class="ui-state-default ui-corner-all" title="返回列表"
					style="width:100px" onclick="javascript:returnToTaskList();"> << 返回列表</li>
			</ul>
			<input type="hidden" id="reportNcsTaskId" value="" />
	     	<%@include file="rno_structure_ncs_render_img.jsp"%>
	    </div>
	</div>
  </body>
</html>
