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

<title>小区GIS呈现</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/rno_4g_sts_monitor_quota_map.js"></script>
<script type="text/javascript" src="js/cityMapGrid.js"></script>
<script type="text/javascript" src="jslib/lib_mapgrid.js"></script>
<%-- <script type="text/javascript" src="jslib/BMapLib/EventWrapper/EventWrapper.js"></script> --%>

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
.resource_list_box{
width:400px;
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
				right : '400px'
			}, 'fast');
			$(".resource_list_box").show("fast");
		})
		$(".zy_show").click(function() {
			$(".search_box_alert").slideToggle("fast");
		})
		
	})
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

	})
</script>

</head>


<body>
	<div style="margin: 5px;padding: 0px;">
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
							
								<%-- 加载小区参数表开始--%>
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
									</select> 区：<select name="areaId" class="required" id="queryCellAreaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
									</select>
									<input  type="button" id="showCellName" class="showCellNameButton" value="地图载入中.."/>
									<input  type="button" id="loadltecell" value="加载小区"/>
									<span id="loadingCellTip"></span><input  type="button" id="showpbcList" value="显示问题小区表"/>
									<span id="loadingProbCellTip"></span>
									<input  type="button" id="trigger" name="trigger" value=""/>
									<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
									<input type="hidden" id="hiddenLng"	value="<s:property value='centerPoint.lng' />" /> 
									<input type="hidden" id="hiddenLat"	value="<s:property value='centerPoint.lat' />" /> 
									<input type="hidden" id="hiddenPageSize" /> 
									<input type="hidden" id="hiddenCurrentPage" /> 
									<input type="hidden" id="hiddenTotalPageCnt" />
									<input type="hidden" id="hiddenTotalCnt" />
								</form>
								<%-- 加载小区参数表结束--%>
								
								<div id="hiddenAreaLngLatDiv" style="display:none">
										<s:iterator value="countryAreas" id="onearea">
											<input type="hidden"	id="areaid_<s:property value='#onearea.area_id' />" 	value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />"/>
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
			<div style="padding: 0px;margin: 0px">
				<div class="map_bd" style="height: 720px;">
					<div id="map_canvas"></div>
					<div class="resource_list_icon" style="height: 650px;">
						<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
						<div class="shad_v"></div>
					</div>
					<div class="resource_list_box" style="height: 650px;overflow:auto">
					<div id="frame" style="border: 1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu" style="margin-top: 5px">
					<ul>
					<li>小区指标详情</li>
					<li class="selected">监控阈值详情</li>
					<li>问题小区表 </li>
					</ul>
					</div>
				<div class="divtab_content">
					<div id="div_tab_0">
						<div class="resource_list" >
							<table id="lteStsCellIndex" class="main-table1 half-width">
							</table>
						</div>
					</div>
					<div id="div_tab_1">
					<div class="resource_list" >
						<div class="div_title_24px_blue">
							<span class="sp_title">说明</span>
						</div>
						<a onclick="$('#thresholdInstructionDiv').show()">监控阈值说明</a><br/>
						<div class="div_title_24px_blue">
							<span class="sp_title">监控阈值</span>
						</div>
						<table id="lteStsCellThreshold" class="main-table1 half-width">
						</table>
						</div>
					</div>
					<div id="div_tab_2" style="display:none">
						<div class="resource_list" >
							<table id="probCellList"  class="main-table1 half-width" > <%-- style="border: medium none; margin: 0 auto 10px;" --%>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
					<%-- 地图高宽--%>
					<div class="htl_map_move">
						<div>
							<iframe frameborder="0" src="" style="border: medium none; width: 1600px; height: 730px;"></iframe>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
		<div id="operInfo" style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
			<table style="text-align:center; height:100%;width:100%">
				<tr>
					<td><span id="operTip"></span></td>
				</tr>
			</table>
		</div>
		<div id="thresholdInstructionDiv" class="dialog2 draggable" style="display:none; left:25%; top: 40%;width: 800px;">
			<div class="dialog_header">
				<div class="dialog_title">监控阈值说明</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#thresholdInstructionDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
				<div>
					<div style="text-align: center">
					当监控阈值满足监控公式条件时，监控项为异常。<br/>
					</div>
					<table>
						<thead>
							<tr>
								<th style="width: 200px">监控项</th>
								<th>监控阈值</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>E-RAB掉线率</td>
								<td>[E-RAB掉线率(小区级)]>=10 and [E-RAB建立成功次数]>20</td>
							</tr>
							<tr>
								<td>E-RAB建立成功率</td>
								<td>[E-RAB建立成功率]&lt;=85 and [E-RAB建立请求次数]>20</td>
							</tr>
							<tr>
								<td>RRC连接建立成功率</td>
								<td>[RRC连接建立成功率]&lt;=85 and [RRC连接建立请求次数]>20</td>
							</tr>							
							<tr>
								<td>零流量零话务</td>
								<td>[RRC连接建立请求次数]>3 and [空口上行业务字节数]+[空口下行业务字节数]=0</td>
							</tr>
							<tr>
								<td>无线接通率</td>
								<td>[RRC连接建立请求次数]>100 and 无线接通率&lt;=70</td>
							</tr>
							<tr>
								<td>CellBar_RRC连接失败次数</td>
								<td>[RRC连接建立成功率]&lt;70 and ([RRC连接建立请求次数]-[RRC连接建立成功次数])>200</td>
							</tr>
							<tr>
								<td>CellBar_掉线次数</td>
								<td>[无线掉线率(小区级)]>50 and [无线掉线次数(小区级)]>200</td>
							</tr>
							<tr>
								<td>RRC连接失败次数</td>
								<td>[RRC连接建立请求次数]-[RRC连接建立成功次数]>100</td>
							</tr>
							<tr>
								<td>掉线次数</td>
								<td>[无线掉线次数(小区级)]>150</td>
							</tr>
							<tr>
								<td>接通失败次数</td>
								<td>([RRC连接建立请求次数]-[RRC连接建立成功次数])+([E-RAB建立请求次数]-[E-RAB建立成功次数])>200</td>
							</tr>
							<tr>
								<td>切换成功率</td>
								<td>[切换成功率]&lt;=60 and [切换请求次数]>100</td>
							</tr>
							<tr>
								<td>切换失败次数</td>
								<td>[切换请求次数]-[切换成功次数]>=150</td>
							</tr>
							<tr>
								<td>无线掉线率</td>
								<td>[无线掉线率(小区级)]>=20 and  [RRC连接建立请求次数]+[E-RAB建立请求次数]>100</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
	    </div>
	</div>
</body>
</html>