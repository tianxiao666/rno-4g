﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>G4 DT分析图</title>

<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
			<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

				<%@include file="commonheader.jsp"%>
				<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css"
					type="text/css" />
				<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
				<script type="text/javascript" src="js/rno_4g_dt_analysis_map.js"></script>

				<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	background: #EEEEEE;
}

#map_canvas {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}
#sampleDetailTable td{
    padding:1px;
.fileCls {
	cursor: pointer
}
}
;
</style>
		<script type="text/javascript">
			//百度地图和谷歌地图切换，默认是百度地图
			var glomapid='<%=session.getAttribute("mapId") %>';
			$(document).ready(function() {
				$(function() {
					//tab选项卡
					tab("div_tab", "li", "onclick");//项目服务范围类别切换

					$(".switch").click(function() {
						$(this).hide();
						$(".switch_hidden").show();
						$(".resource_list_icon").animate({
							right : '0px'
						}, 'fast');
						$(".resource_list300_box").hide("fast");
					})
					$(".switch_hidden").click(function() {
						$(this).hide();
						$(".switch").show();
						$(".resource_list_icon").animate({
							right : '380px'
						}, 'fast');
						$(".resource_list300_box").show("fast");
					})
					$(".zy_show").click(function() {
						$(".search_box_alert").slideToggle("fast");
					})
				})
			});
		</script>
</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在进行 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">DT专题分析</div> --%>
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div class="map_hd" style="padding-bottom: 0px">
					<div class="head_box clearfix" style="padding-bottom: 0px">
						<div class="dialog2 draggable ui-draggable">
							<div style="padding: 5px">

								<form id="conditionForm" method="post">
									省：<select name="provinceId" class="required" id="provinceId">
										<%-- option value="-1">请选择</option --%>
										<s:iterator value="provinceAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> 市：<select name="cityId" class="required" id="cityId">
										<s:iterator value="cityAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> 区：<select name="showareaId" class="required"
										id="queryCellAreaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select>
									<input  type="button" id="trigger" name="trigger" value=""/>
									<input type="hidden" id="realPostAreaId" name="areaId" /> <input
										type="hidden" id="displaycell" name="displaycell" value="显示小区" />
									<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
									<input type="hidden" id="hiddenLng"
										value="<s:property value='centerPoint.lng' />" /> <input
										type="hidden" id="hiddenLat"
										value="<s:property value='centerPoint.lat' />" /> <input
										type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="100" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> <input type="hidden"
										name="page.forcedStartIndex" id="hiddenForcedStartIndex" /> <input
										type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
										id="hiddenTotalCnt" />

								</form>
								<div id="hiddenAreaLngLatDiv" style="display:none">
									<s:iterator value="countryAreas" id="onearea">
										<input type="hidden"
											id="areaid_<s:property value='#onearea.area_id' />"
											value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
									</s:iterator>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>

			<%-- 地图--%>
			<div style="padding-top: 0px">
				<div class="map_bd">
					<div id="map_canvas"></div>
					<div class="resource_list_icon" style="right:380px">
						<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
						<div class="shad_v"></div>
					</div>
					<div class="resource_list300_box" style="height:100%;">
						<div class="resource_list300">
							<div id="div_tab" class="div_tab divtab_menu">
								<ul>
									<li class="selected">DT分析</li>
									<li>加载数据</li>
									<li id="sampleDetailLi">采样点详情</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content">
							<div id="div_tab_0">
								<input type="hidden" id="showRenderColorBtn" value="显示渲染图例">
									<%--查询结果  --%> <%-- 标题--%>
									<div class="div_title_24px_blue">
										<span class="sp_title"> DT任务</span>
									</div>

									<div class="div_transparentstandard_nopaddming"
										id="analysisListDiv">
										<table class="tb-transparent-standard" style="width:100%"
											id="analysisListTable">
											<%-- 分析列表数据 --%>
										</table>
										<div id="analysisBtnDiv" style="display:none">
											<input type="button" value="确定"
												id="confirmSelectionAnalysisBtn" />&nbsp;&nbsp;&nbsp;<input
												type="checkbox" id="showWithCellCheck" checked="checked" />同步显示小区
										</div>
									</div> <%-- --%>
									<div class="div-m-split-10px"></div> <%--分析应用 --%>
									<div class="div_transparent_standard_border">
										<div class="div_title_24px_grey sp_title_left">
											<span style="font-style: italic;"> 弱覆盖</span>
										</div>
										<div id="analysisDiv">
											<input type="button" id="weakcoveragebtn"
												value='弱覆盖' />
												<%-- onclick="javascript:commondtstatics('weakcoverage','staticsResourceUtilizationRateForAjaxAction',null,staticsWeakCoverage);"  --%>
										</div>
										<div class="div_title_24px_grey sp_title_left">
											<span style="font-style: italic;"> 过覆盖</span>
										</div>
										<div id="analysisDiv">
											 <input type="button"
												id="radioresourceutilizationbtn"
												value='室分外泄' />
										</div>
										<div class="div_title_24px_grey sp_title_left">
											<span style="font-style: italic;"> 异常覆盖</span>
										</div>
										<div id="analysisDiv">
											<input type="button" id="cellsignaloverlapbtn"
												value='重叠覆盖' />
										</div>
									</div>
							</div>
							<div id="div_tab_1" style="display:none">
							<form id="searchDtForm">
								<table class="main-table1 half-width"
									style="margin-left:0;width:100%">
									<tbody>
										<tr>
											<td class="menuTd">区域<span class="txt-impt">*</span></td>
											<td>
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
											</td>
										</tr>
										<tr>
											<td class="menuTd">测试日期<span class="txt-impt">*</span>
											<td>
											<input type="text"
										value="<s:date name="%{new java.util.Date()}" format="yyyy-MM-dd" />"
										id="dtMeaBegDate" name="attachParams['dtMeaBegDate']" />
											</td>
										</tr>
										
										<tr>
											<td class="menuTd">数据类型<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0">
											<select id="factory"
										name="attachParams['dataType']">
											<option value='ALL'>全部</option>
											<option value='DataService'>数据业务</option>
											<option value='SweepFreq'>扫频业务</option>
											</select>
												</td>
										</tr>
										<tr>
											<td class="menuTd">区域类型<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0">
											<select id="areaType"
										name="attachParams['areaType']">
											<option value='ALL'>全部</option>
											<option value='城区'>城区</option>
											<option value='非城区'>非城区</option>
											<option value='高速'>高速</option>
											</select>
											</td>
										</tr>
										<tr>
											<td class="menuTd" colspan="2" align="center">
											<input type="button" name="" id="queryDtBtn" value="查询DT" />
											</td>
										</tr>
									</tbody>

								</table>
								</form>
								<div id="dtListDiv" style="overflow-y:scroll; overflow-x:scroll; width:100%; height:397px" >
									<table id="dtListTab" class="greystyle-standard"
										style="width:auto;margin-left:0">
										<thead>
											<%--  <th>测量时间</th>--%>
											<th>数据类型</th>
											<th>区域类型</th>
											<th>文件名</th>
											<%--<th>测试数据量</th>--%>
											<th><input type="button" name="" id="loadDtData" value="加载" /></th>
										</thead>
									</table>
								</div>
							</div>
							<div id="div_tab_2" style="display:none">
								<table class="main-table1 half-width" id="sampleDetailTable">
									<tr>
										<td class="menuTd" style="width:20%">时间</td>
										<td id="tdSampleTime"></td>
									</tr>
									<tr>
										<td class="menuTd" >主覆盖小区</td>
										<td id="tdServerCell" ></td>
									</tr>
									<tr>
										<td class="menuTd" >RSRP</td>
										<td id="tdCellRsrp"></td>
										</t>
									</tr>
									<tr>
										<td class="menuTd" >RS_SINR</td>
										<td id="tdCellRsSinr"></td>
									</tr>
									<tr>
										<td class="menuTd" >邻区</td>
										<td id="tdNcells"></td>
									</tr>
									<tr>
										<td class="menuTd" >邻区RSRP</td>
										<td id="tdNcellRsrps"></td>
									</tr>
									<tr>
										<td class="menuTd" >采样点与服务小区距离</td>
										<td id="tdServerCellToSampleDis"></td>
									</tr>
									<tr>
										<td class="menuTd" >区域类型</td>
										<td id="tdAreaType"></td>
									</tr>
									<tr>
										<td class="menuTd" >频点类型</td>
										<td id="tdCellEarfcn"></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<%-- 地图高宽--%>
					<div class="htl_map_move">
						<div>
							<iframe frameborder="none" src=""
								style="border: medium none; width: 1600px; height: 650px;">
							</iframe>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

	<%--分析弹出窗 --%>
	<div id="analyze_Dialog" class="dialog2 draggable"
		style="display:none; width:250px;">
		<div class="dialog_header">
			<div class="dialog_title">DT专题分析</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn"
					onclick="$('#analyze_Dialog').hide();$('.colordialog2').hide();"></div>
			</div>
		</div>
		<div class="dialog_content" style="width:300px; background:#f9f9f9">
			<table class="tb-transparent-standard" style="width:300px"
				id="trafficTable">

			</table>

		</div>
	</div>
	<%--分析修改弹出窗 --%>
	<div>
		<div class="black"></div>
		<div id="analyzeedit_Dialog" class="dialog2 draggable"
			style="display:none; width:410px;">
			<div class="dialog_header">
				<div class="dialog_title">DT专题分析修改</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn2"
						onclick="$('#analyzeedit_Dialog').hide();$('.black').hide();$('.colordialog2').hide();"></div>
				</div>
			</div>
			<div class="dialog_content" style="width:410px; background:#f9f9f9">
				<form action="updateOrAddrnoTrafficRendererAction" id="rendererForm"
					method="post">
					<table class="tb-transparent-standard" style="width:350px "
						id="analyzeedit_trafficTable">

					</table>
				</form>
			</div>
		</div>
	</div>
	<div id="divColorDiv"></div>
	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
</body>
</html>
