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

<title>小区GIS查找</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

	<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rnogiscellsearch.js"></script>
<script type="text/javascript" src="js/cityMapGrid.js"></script>
<script type="text/javascript" src="jslib/lib_mapgrid.js"></script>

<link rel="stylesheet" type="text/css"
	href="../../jslib/dialog/dialog.css" />
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
			$(".resource_list_box").hide("fast");
		})
		$(".switch_hidden").click(function() {
			$(this).hide();
			$(".switch").show();
			$(".resource_list_icon").animate({
				right : '286px'
			}, 'fast');
			$(".resource_list_box").show("fast");
		})
		$(".zy_show").click(function() {
			$(".search_box_alert").slideToggle("fast");
		})

		$(".queryButton").click(function() {
			//$("#searchDiv").slideToggle();
			$("#searchDiv").toggle();
		});
	})
</script>


<style type="text/css">
.queryButton { /*background-image: url(../../images/search.png);*/
	background-color: #4A8BF4;
	display: inline-block;
	height: 25px;
	vertical-align: middle;
	width: auto;
	overflow: visible;
	font-size: 12px;
}
.loadButton { /*background-image: url(../../images/search.png);*/
	background-color: #4A8BF4;
	display: inline-block;
	height: 25px;
	vertical-align: middle;
	width: auto;
	overflow: visible;
	font-size: 12px;
}
.showCellNameButton { /*background-image: url(../../images/search.png);*/
	background-color: #4A8BF4;
	display: inline-block;
	height: 25px;
	vertical-align: middle;
	width: auto;
	overflow: visible;
	font-size: 12px;
}
</style>

</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在查找 <em class="loading_fb">小区</em>,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">地图查找</div> --%>
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div class="map_hd" style="padding-bottom: 0px">
					<div class="head_box clearfix" style="padding-bottom: 0px">
						<div class="dialog2 draggable ui-draggable">
							<div style="padding: 5px">
								<form id="conditionForm" method="post">
									<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
									<input type="hidden" id="hiddenLng"
										value="<s:property value='centerPoint.lng' />" /> <input
										type="hidden" id="hiddenLat"
										value="<s:property value='centerPoint.lat' />" /> <input
										type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="100" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> <input type="hidden"
										id="hiddenTotalPageCnt" /> <input type="hidden"
										id="hiddenTotalCnt" /> 省：<select name="provinceId"
										class="required" id="provinceId">
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
										<option value="-1">全部</option>
									</select>
									 <input type="button" class="queryButton"  value="打开搜索面板"　style="font-size: 12px"></input>
												<input  type="button" id="trigger" name="trigger" value=""/>
									 <input  type="button" id="showCellName" class="showCellNameButton" value="显示小区名字"/>
									 <input  type="button" id="loadgiscell" class="loadButton" value="加载小区"/>		
									 <span id="loadingCellTip"></span>

								</form>
								<div id="hiddenAreaLngLatDiv" style="display:none">
									<s:iterator value="countryAreas" id="onearea">
										<input type="hidden"
											id="areaid_<s:property value='#onearea.area_id' />"
											value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
									</s:iterator>
								</div>


								<div id="searchDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">小区查找</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#searchDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										搜索条件： <select id="conditionType">
											<option value="cell">CELL</option>
											<option value="chineseName">小区中文名</option>
											<option value="site">SITE</option>
											<option value="lac">LAC</option>
											<option value="ci">CI</option>
										</select> <input type="text" value="" id="conditionValue" /> <input
											type="button" id="searchCellBtn" value="搜小区" /><br /> 输入邻区：
										<input type="text" id="cellForNcell" /><input type="button"
											id="searchNcellBtn" value="搜邻区" /> <br /> 输入频点： <input
											type="text" id="freqValue" /><input type="button"
											id="searchFreqBtn" value="搜频点" /> <br /> <br /> <input
											type="button" value="清除搜索结果" id="clearSearchResultBtn" />

									</div>
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
					<div class="resource_list_icon">
						<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
						<div class="shad_v"></div>
					</div>
					<div class="resource_list_box" style="height: 100%;">
						<div class="resource_list">
							<table class="main-table1 half-width">
								<tr>
									<td class="menuTd" style="width: 20%">小区名</td>
									<td id='showCellLabelId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">小区中文名</td>
									<td id='showCellNameId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">LAC</td>
									<td id='showCellLacId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">CI</td>
									<td id='showCellCiId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">BCCH</td>
									<td id='showCellBcchId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">TCH</td>
									<td id='showCellTchId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">BSIC</td>
									<td id='showCellBsicId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线方向</td>
									<td id='showCellAzimuthId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线下倾</td>
									<td id='showCellDownId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">基站类型</td>
									<td id='showCellBtsTypeId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线高度</td>
									<td id='showCellAntHeightId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线类型</td>
									<td id='showCellAntTypeId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">经度</td>
									<td id='showCellLngId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">纬度</td>
									<td id='showCellLatId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">覆盖范围</td>
									<td id='showCellCoverareaId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">频段</td>
									<td id='showCellFreqSectionId'></td>
								</tr>
							</table>
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
