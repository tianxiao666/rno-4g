<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>4g话统数据管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>

<link href="jslib/jquery-ui-1.11.1.custom/jquery-ui.css"
	rel="stylesheet">
<link
	href="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.css"
	rel="stylesheet">
<script src="jslib/jquery-ui-1.11.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="js/rno_4g_sts_manage.js"></script>
<script
	src="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">
	
</script>

<style type="text/css">
body {
	margin: 50px;
}

.greystyle-standard-noborder tr td {
	padding: 0
}

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

/* 覆盖分页面板的对齐样式 */
.paging_div {
	text-align: left;
}

select {
	width: 100px;
}

.sectionDivCls {
	margin-top: 10px;
}
.importTitle {
	background: url("images/ui-bg_glass_65_ffffff_1x400.png") repeat-x scroll 50% 50% #ffffff;
	border: 1px solid #fbd850;
	color: #eb8f00;
	font-weight: bold;
	padding-left: 2.2em;
	height:30px;
	cursor: pointer;
	border-radius: 5px;
}
.importContent {
	background: url("images/ui-bg_highlight-soft_100_eeeeee_1x100.png") repeat-x scroll 50% top #eeeeee;
	border: 1px solid #dddddd;
	padding: 1em 2.2em;
	border-radius: 5px;
}
</style>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
</script>

<body>
<body>
	<span class='navTitle'><h2>当前位置：业务分析优化 &gt;  网络统计数据管理 </h2></span>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>



	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">网络统计数据导入</a></li>
			<li><a href="#tabs-2">网络统计数据查询 </a></li>
		</ul>
		<div id="tabs-1">
			<div id="listinfoDiv">
					<form id="searchImportForm">
						<select id="provincemenu">
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select> <select id="citymenu" name="attachParams['cityId']">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select>
						<div style="margin: 9px"></div>
						<div id="searchImportDiv" style="height:153px;background:url('images/ui-bg_glass_65_ffffff_1x400.png') repeat-x scroll 50% 50% #ffffff;border:1px solid #fbd850">
							<div style="margin: 9px"></div>
							<input type="hidden" name="fileCode" id='fileCode'
								value="4GSTSDATAFILE" /> <input type="hidden"
								id="hiddenPageSize" name="page.pageSize" value="25" /> <input
								type="hidden" id="hiddenCurrentPage" name="page.currentPage"
								value="1" /> <input type="hidden" id="hiddenTotalPageCnt" /> <input
								type="hidden" id="hiddenTotalCnt" value="-1"
								name="page.totalCnt" />
							<table>
								<tr>

									<td style="padding-left:30px"><label>上传时间 从:</label> <input
										type="text" value="" id="uploadQueryBegDate"
										name="attachParams['begUploadDate']" /> 至：<input type="text"
										value="<s:date name="todaytime" format="yyyy-MM-dd" />"
										id="uploadQueryEndDate" name="attachParams['endUploadDate']" /></td>
									<td style="padding-left:30px"><label>状态:</label></td>
									<td><select id="importstatusmenu"
										name="attachParams['fileStatus']">
											<option value='全部'>全部</option>
											<option value='全部成功'>全部成功</option>
											<option value='部分失败'>部分失败</option>
											<option value='全部失败'>全部失败</option>
											<option value='正在解析'>正在解析</option>
											<option value='等待解析'>等待解析</option>
									</select></td>
									<td style="padding-left:30px"><input type="button"
										id="searchImportBtn" value="查询导入记录"></input></td>
								</tr>
							</table>
						</div>
					</form>
				
					<div style="margin: 9px"></div>
			
					<div id="importTitleDiv" class="importTitle">
						<div style="margin: 9px"></div>
						导入
					</div>
					<div id="importDiv" class="importContent">
						<form id="formImportData" enctype="multipart/form-data"
							method="post">
							<input type="hidden" name="fileCode" id='fileCode'
								value="4GSTSDATAFILE" /> <input type="hidden" name="token"
								id="token" value="" />
							<input type="hidden" id="uploadCityId" name="cityId" value=""/>
							<s:set name="todaytime" value="new java.util.Date()" />
												<input  type="hidden" name="meaTime" class="required" id='meatime'
												value="<s:date name="todaytime" format="yyyy-MM-dd" />"
												type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
												readonly class="Wdate input-text" style="width: 132px;" />
							<div>
								<table class="main-table1 half-width"
									style="margin-left:0;width:50%">
									<tbody>
										<%--  
										<tr>
											<td class="menuTd">测试日期<span class="txt-impt">*</span>
											<td><s:set name="todaytime" value="new java.util.Date()" />
												<input name="meaTime" class="required" id='meatime'
												value="<s:date name="todaytime" format="yyyy-MM-dd" />"
												type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
												readonly class="Wdate input-text" style="width: 132px;" /></td>
										</tr>
										--%>
										<tr>
											<td class="menuTd">网络统计数据文件<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0"><input
												type="file" style="width: 44%;" name="file" id='fileid'
												class="canclear required" /></td>
										</tr>
<%-- 										<tr>
											<td class="menuTd">数据类型<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0">
											<select id="factory"
										name="">
											<option value='4GDTDATASERVICEFILE'>数据业务</option>
											<option value='4GDTSWEEPFREQFILE'>扫频业务</option>
											</select>
												</td>
										</tr> --%>
										<%-- <tr>
											<td class="menuTd">区域类型<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0">
											<select id="areaType"
										name="attachParams['areaType']">
											<option value='城区'>城区</option>
											<option value='非城区'>非城区</option>
											<option value='高速'>高速</option>
											</select>
											</td>
										</tr> --%>
									</tbody>

								</table>
							</div>
							<div class="container-bottom" style="padding-top: 10px">
								<table style="width: 60%; margin: auto" align="center">
									<tr>
										<td><input type="button" value="导    入"
											style="width: 90px;" id="importBtn" name="import" /> <br />
											<div id="uploadMsgDiv" style="display:none"></div>	</td>
									</tr>
								</table>
								<div id="importResultDiv" class="container-bottom"
									style="padding-top: 10px">
									<div id="progressInfoDiv" style="width:50%;display:none">
										<h2 id="progressNum">0%</h2>
										<div id="progressbar"></div>
									</div>
								</div>
							</div>
						</form>
					</div>

				<div id="importListDiv" class="sectionDivCls">
					<b>网络统计上传记录列表</b>
					<%--<a onclick='javascript:toggleImport();'>新增导入</a> --%>
					<table id="importListTab" class="greystyle-standard"
						style="width:auto;margin-left:0">
						<thead>
						<tr>
							<th>地市</th>
							<th>上传日期</th>
							<th>文件名称</th>
							<th>文件大小</th>
							<th>开始时间</th>
							<th>完成时间</th>
							<th>上传账号</th>
							<th>状态</th>
						</tr>	
						</thead>
					</table>
					<div class="paging_div" id="ImportRecPageDiv"
						style="border: 1px solid #ddd">
						<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
							条记录
						</span> <a class="paging_link page-first" title="首页"
							onclick="showListViewByPage('first',queryImportDataRec,'searchImportForm','ImportRecPageDiv')"></a>
						<a class="paging_link page-prev" title="上一页"
							onclick="showListViewByPage('back',queryImportDataRec,'searchImportForm','ImportRecPageDiv')"></a>
						第 <input type="text" id="showCurrentPage"
							class="paging_input_text" value="0" /> 页/<em id="emTotalPageCnt">0</em>页
						<a class="paging_link page-go" title="GO"
							onclick="showListViewByPage('num',queryImportDataRec,'searchImportForm','ImportRecPageDiv')">GO</a>
						<a class="paging_link page-next" title="下一页"
							onclick="showListViewByPage('next',queryImportDataRec,'searchImportForm','ImportRecPageDiv')"></a>
						<a class="paging_link page-last" title="末页"
							onclick="showListViewByPage('last',queryImportDataRec,'searchImportForm','ImportRecPageDiv')"></a>
					</div>
				</div>
			</div>
			<%--某项job的报告 --%>
			<div id="reportDiv" style="display:none">
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
						style="width:100px" onclick="javascript:returnToImportList();"><span
						class="ui-icon ui-icon-arrowreturnthick-1-w" style="width:20px"></span>返回列表</li>
				</ul>
				<table id="reportListTab" class="greystyle-standard"
					style="width:auto;margin-left:0">
					<thead>
					<tr>
						<th>阶段</th>
						<th>开始时间</th>
						<th>结束</th>
						<th>结果</th>
						<th>详细信息</th>
					</tr>
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
		<div id="tabs-2">
			<form id="searchDataForm">
				<input type="hidden" name="fileCode" id='fileCode'
					value="4GSTSDATAFILE" /> <input type="hidden"
					id="hiddenPageSize" name="page.pageSize" value="25" /> <input
					type="hidden" id="hiddenCurrentPage" name="page.currentPage"
					value="1" /> <input type="hidden" id="hiddenTotalPageCnt" /> <input
					type="hidden" id="hiddenTotalCnt" value="-1" name="page.totalCnt" />
				<table>
					<tr>
						<td><select id="provincemenu2">
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select> <select id="citymenu2" name="attachParams['cityId']">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select></td>
<%-- 						<td style="padding-left:10px">数据类型：</td>
						<td><select id="factory"
										name="attachParams['dataType']">
											<option value='ALL'>全部</option>
											<option value='DataService'>数据业务</option>
											<option value='SweepFreq'>扫频业务</option>
											</select></td> --%>
						<td style="padding-left:10px">测试时间 从:</td>
						<td><input type="text" value="" id="meaBegDate"
							name="attachParams['meaBegDate']" style="width:135px" /></td>
						<td>至：</td>
						<td><input type="text"
							value="<s:date name="todaytime" format="yyyy-MM-dd" />"
							id="meaEndDate" name="attachParams['meaEndDate']"
							style="width:135px" /></td>
						<td  style="padding-left:10px"><input type="button" id="searchDataBtn" value="查询"></input></td>
					</tr>
				</table>

			</form>
			<div id="dataListDiv" class="sectionDivCls">
				<table id="dataListTab" class="greystyle-standard"
					style="width:auto;margin-left:0">
					<thead>
					<tr>
						<th>地市</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>信息参考模型</th>
						<th>前缀</th>
						<th>SenderName</th>
						<th>供应商名</th>
						<th>任务ID</th>
						<th>测试数据量</th>
						<th>进入系统时间</th>
						</tr>
					</thead>
				</table>
				<div class="paging_div" id="dataListPageDiv"
					style="border: 1px solid #ddd">
					<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
						条记录
					</span> <a class="paging_link page-first" title="首页"
						onclick="showListViewByPage('first',queryDataDescData,'searchDataForm','dataListPageDiv')"></a>
					<a class="paging_link page-prev" title="上一页"
						onclick="showListViewByPage('back',queryDataDescData,'searchDataForm','dataListPageDiv')"></a>
					第 <input type="text" id="showCurrentPage" class="paging_input_text"
						value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
						class="paging_link page-go" title="GO"
						onclick="showListViewByPage('num',queryDataDescData,'searchDataForm','dataListPageDiv')">GO</a>
					<a class="paging_link page-next" title="下一页"
						onclick="showListViewByPage('next',queryDataDescData,'searchDataForm','dataListPageDiv')"></a>
					<a class="paging_link page-last" title="末页"
						onclick="showListViewByPage('last',queryDataDescData,'searchDataForm','dataListPageDiv')"></a>
				</div>
			</div>
		</div>
	</div>



</body>


</html>
