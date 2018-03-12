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

<title>4g區域網格呈现</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/rno_4g_grid_map_display.js"></script>
<script type="text/javascript" src="js/cityMapGrid.js"></script>
<script type="text/javascript" src="jslib/lib_mapgrid.js"></script>

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
	//console.log("页面glomapid："+glomapid);
	$(function() {
		//console.log("页面glomapid："+glomapid)
		//glomapid =<%=session.getAttribute("mapId") %>;
		//console.log("glomapid:"+glomapid);
		//console.log("glomapid:"+(glomapid==null));
		$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
		//console.log("jspstr:"+glomapid);
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
		
	})
</script>

</head>


<body>
	<div class="loading_cover" style="display: none;position:absolute;z-index:999">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb">小区</em>资源,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">小区GIS呈现</div> --%>
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div class="map_hd" style="padding-bottom: 0px">
					<div class="head_box clearfix" style="padding-bottom: 0px">
						<div class="dialog2 draggable ui-draggable">
							<div style="padding: 5px">

								<form id="conditionForm" method="post">
									<span style="padding-top: 0px">区域：</span> 省：<select
										name="provinceId" class="required" id="provinceId">
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
									</select> <%-- 区：<select name="areaId" class="required" id="queryCellAreaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select> --%>
									<input  type="button" id="loadgisgrid" value="加载网格"/>
								    <input type="button" id="exportgisgridcell" value="导出网格小区数据" onclick="exportExcelFile()" disabled="true"/>
									<span id="loadingCellTip"></span>
									<input  type="button" id="trigger" name="trigger" value=""/>
									<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
									<input type="hidden" id="hiddenLng"
										value="<s:property value='centerPoint.lng' />" /> <input
										type="hidden" id="hiddenLat"
										value="<s:property value='centerPoint.lat' />" /> <input
										type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="100" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> <input type="hidden"
										id="hiddenTotalPageCnt" /> <input type="hidden"
										id="hiddenTotalCnt" />
								</form>
								<div id="hiddenAreaLngLatDiv" style="display:none">
										<s:iterator value="countryAreas" id="onearea">
											<input type="hidden"
												id="areaid_<s:property value='#onearea.area_id' />"
												value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
										</s:iterator>
								</div>
								<%-- 
									<input value="获取zoom" id="btn" type="button"/>
									<input type="text" id="zoomval" size="100"/>
									 --%>
							
							</div>
						</div>
					</div>
				</div>

			</div>

			<%-- 地图--%>
			<div style="padding-top: 0px">
				<div class="map_bd">
					<div id="map_canvas"></div>
					<%--  
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
									<td class="menuTd" style="width: 20%">PCI</td>
									<td id='showCellPciId'></td>
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
									<td class="menuTd" style="width: 20%">天线挂高</td>
									<td id='showCellGroundHeightId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线类型</td>
									<td id='showCellAntennaTypeId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">天线方向</td>
									<td id='showCellAzimuthId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">是否合路</td>
									<td id='showCellIntegratedId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">RRU型号</td>
									<td id='showCellRruverId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">RRU数量</td>
									<td id='showCellRrunumId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">参考信号功率</td>
									<td id='showCellRspowerId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">频段类型</td>
									<td id='showCellBandTypeId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">下行带宽</td>
									<td id='showCellBandId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">下行频点</td>
									<td id='showCellEarfcnId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">覆盖类型</td>
									<td id='showCellCoverTypeId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">覆盖范围</td>
									<td id='showCellCoverRangeId'></td>
								</tr>
							</table>
						</div>

					</div>
					--%>
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
