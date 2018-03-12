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

<title>频率复用分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<%@include file="commonheader.jsp"%>
<%--  
<link rel="stylesheet" type="text/css" href="../../css/base.css">
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css">
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css"
	href="../../css/public-div-standard.css">
<link rel="stylesheet" type="text/css"
	href="../../css/public-table-standard.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-tb-std.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-span-std.css" />
<link rel="stylesheet" type="text/css"
	href="../../jslib/paging/iscreate-paging.css" />

<link rel="stylesheet" type="text/css" href="css/source.css" />

<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />
--%>


<style>
.main-table1 .menuTd{
text-align:right;
}
.txt-err, .txt-impt {
color: #C00;
font-size: 14px;
}
</style>
<%--  
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../js/tab.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="jslib/layer/layer.min.js "></script>
<script type="text/javascript" src="js/selftools.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=1.5&ak=85E6658a58abb433b32f6505997a0c25"></script>
<script type="text/javascript" src="js/rendererrule.js"></script>
<script type="text/javascript" src="jslib/libgiscelldisplay.js"></script>
<script type="text/javascript" src="jslib/libareacascade.js"></script>
<script type="text/javascript" src="jslib/lib_gis_dt_sample_display.js"></script>
<script type="text/javascript" src="jslib/libfileupload.js"></script>
<script type="text/javascript" src="js/cellRelaModel.js"></script>
<script type="text/javascript" src="js/commontimedtask.js"></script> 
--%> 
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../jslib/dialog/dialog.css" />
<script type="text/javascript"
	src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript"
	src="../../jslib/highcharts/exporting.src.js"></script>
<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css"
	type="text/css" />
<script type="text/javascript" src="js/freqreuse.js"></script>
<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
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
;
</style>
<script type="text/javascript">
var glomapid='<%=session.getAttribute("mapId") %>';
	$(function() {
$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
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
</script>

</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb">小区</em>资源,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">频率复用分析</div> --%>
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
									</select> 区：<select name="areaId" class="required" id="queryCellAreaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> 
									<input  type="button" id="trigger" name="trigger" value=""/>
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
										id="hiddenTotalCnt" /> <input type="hidden" value=""
										name="type">
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
									<li class="selected">规划优化</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content">
							<div id="div_tab_0">

								<%--查询结果  --%>
								<%-- 标题--%>
								<div class="div_title_24px_blue">
									<span class="sp_title"> 小区配置</span>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="analysisListDiv_cellconfig">
									<table class="tb-transparent-standard" style="width:100%"
										id="analysisListTable_cellconfig">

									</table>
									<div id="showCellBtnDiv" style="display:none">
										<input type="button" value="显示" id="showCellBtn" />
									</div>
								</div>
								<%-- --%>
								<%--
								<%-- 标题--%>
								<div class="div_title_24px_blue">
									<span class="sp_title"> 小区干扰</span>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="analysisListDiv_cellinterference">
									<table class="tb-transparent-standard" style="width:100%"
										id="analysisListTable_cellinterference">

									</table>
									<div id="analysisBtnDiv" style="display:none">
										<input type="button" value="确定"
											id="confirmSelectionAnalysisBtn" />
									</div>
								</div>
								 --%>
								<div class="div-m-split-10px"></div>
								<%--分析应用 --%>
								<div class="div_transparent_standard_border">
									<div class="div_title_24px_grey sp_title_left">
										<span class="sp_title"> 分析应用</span>
									</div>

									<div id="analysisDiv" style="text-align:center">
										<br> <input type="button" id="reportbtn"
											onclick="reportFreqReuse();" value='频点复用统计' />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="button"
											id="analyzebtn" onclick="analyzeFreqReuse();"
											value='频点复用分布标记' /> <br> <span>&nbsp;</span>
									</div>
								</div>
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

	<%--统计窗口 --%>
	<div id="report_Dialog" class="dialog2"
		style="display:none;height:510px;top: 40px; right: 298px; z-index: 30;">
		<div class="dialog_header">
			<div class="dialog_title">频点复用统计</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn"
					onclick="$('#report_Dialog').hide();"></div>
			</div>
		</div>
		<form id="reportForm" method="post">
			<input type="hidden" id="reportPageSize" name="page.pageSize"
				value="20" /> <input type="hidden" id="reportCurrentPage"
				name="page.currentPage" value="0" /> <input type="hidden"
				id="reportTotalCount" /> <input type="hidden" id="reportPageCount" />
			<input type="hidden" id="reportId" name="id" /> <input
				type="hidden" id="reportType" name="type" />
		</form>
		<div class="dialog_content" id="report_Div"
			style="width:900px;height:422px; background:#f9f9f9"></div>
		<br>
		<div class="paging_div" style="border: 1px solid #ddd">
			<span class="mr10"><a href="javascript:void(0);"
				onclick="reportFreqByPage('all')">显示全部</a></span> <span class="mr10">每页
				20 条记录,共 <em id="emTotalCnt" class="blue">0</em> 条记录
			</span> <a class="paging_link page-first" title="首页"
				onclick="reportFreqByPage('first')"></a> <a
				class="paging_link page-prev" title="上一页"
				onclick="reportFreqByPage('back')"></a> 第 <input type="text"
				id="showCurrentPage" class="paging_input_text" value="0" /> 页/<em
				id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
				title="GO" onclick="reportFreqByPage('num')">GO</a> <a
				class="paging_link page-next" title="下一页"
				onclick="reportFreqByPage('next')"></a> <a
				class="paging_link page-last" title="末页"
				onclick="reportFreqByPage('last')"></a>
		</div>
		<div class="loading_cover" id="reportCoverDiv"
			style="display: none;position:absolute">
			<div class="cover"></div>
			<h4 class="loading">正在加载频点复用统计数据,请稍侯...</h4>
		</div>
	</div>
	<div>
		<%--频点复用分析弹出窗 --%>
		<div id="analyzeedit_Dialog" class="dialog2"
			style="display:none;right: 174px;text-align: center;top:242px;width: 183px; z-index: 30;">
			<div class="dialog_header">
				<div class="dialog_title">频点复用分析</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn2"
						onclick="$('#analyzeedit_Dialog').hide();$('.black').hide();"></div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9">
				<div>
					频点：<input type="text" name="freq_value"
						onchange="checkNumber(this);" onkeyup="checkNumber(this);"
						onafterpaste="checkNumber(this);" autocomplete="off" />
				</div>
				<br>
				<div>
					<input type="button" name="freq_value" value="标记"
						onclick="markCellForFreqReuse();" />&nbsp;&nbsp;&nbsp;<input
						type="button" name="freq_value" value="清除"
						onclick="clearMarkCellForFreqReuse();" />
				</div>
			</div>
		</div>
	</div>


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
