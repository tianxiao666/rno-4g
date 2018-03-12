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

		<title>小区Co-BSIC干扰分析</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<%@include file="commonheader.jsp"%>
		<script type="text/javascript" src="js/rnocobsicgiscelldisplay.js"></script>
		<script type="text/javascript" src="js/rnocobsicanalyse.js"></script>

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
//tab选项卡
			tab("div_tab", "li", "onclick");//项目服务范围类别切换

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
		<div class="loading_cover" style="display: none">
			<div class="cover"></div>
			<h4 class="loading">
				正在加载
				<em class="loading_fb">小区或干扰分析中</em>,请稍侯...
			</h4>
		</div>
		<%-- ==============================================  --%>
		<div class="div_left_main" style="width: auto">
			<div class="div_left_content">
				<%-- <div class="div_left_top">小区Co-BSIC干扰分析</div> --%>
				<div style="padding-bottom: 0px; padding-top: 0px">
					<div class="map_hd" style="padding-bottom: 0px">
						<div class="head_box clearfix" style="padding-bottom: 0px">
							<div class="dialog2 draggable ui-draggable">
								<div style="padding: 5px">

									<form id="conditionForm" method="post">
										<span style="padding-top: 0px">区域：</span> 省：
										<select name="provinceId" class="required" id="provinceId">
											<%-- option value="-1">请选择</option --%>
											<s:iterator value="provinceAreas" id="onearea">
												<option value="<s:property value='#onearea.area_id' />">
													<s:property value="#onearea.name" />
												</option>
											</s:iterator>
										</select>
										市：
										<select name="cityId" class="required" id="cityId">
											<s:iterator value="cityAreas" id="onearea">
												<option value="<s:property value='#onearea.area_id' />">
													<s:property value="#onearea.name" />
												</option>
											</s:iterator>
										</select>
										区：
										<select name="areaId" class="required" id="queryCellAreaId">
											<s:iterator value="countryAreas" id="onearea">
												<option value="<s:property value='#onearea.area_id' />">
													<s:property value="#onearea.name" />
												</option>
											</s:iterator>
												<option value="-1">全部</option>
										</select>
										bcch：
										<input type="text" name="bcch" id="bcch" style="width: 50px" class="required" onkeyup="value=value.replace(/[^\d]/g,'')"/>
										bsic：
										<input type="text" name="bsic" id="bsic" style="width: 50px"  
											class="canclear  required" onkeyup="value=value.replace(/[^\d]/g,'')"/>
										<input type="button" name="interferquery" id="interferquery"
											value="干扰查询" />
										<input type="button" name="wholenetinterferquery" id="wholenetinterferquery"
											value="全网干扰查询" />
										<input  type="button" id="trigger" name="trigger" value=""/>
										<div id="hiddenAreaLngLatDiv" style="display: none">
											<s:iterator value="countryAreas" id="onearea">
												<input type="hidden"
													id="areaid_<s:property value='#onearea.area_id' />"
													value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
											</s:iterator>
										</div>
										<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
										<input type="hidden" id="hiddenLng"
											value="<s:property value='centerPoint.lng' />" />
										<input type="hidden" id="hiddenLat"
											value="<s:property value='centerPoint.lat' />" />
										<input type="hidden" id="hiddenPageSize" name="page.pageSize"
											value="100" />
										<input type="hidden" id="hiddenCurrentPage"
											name="page.currentPage" value="1" />
										<input type="hidden" id="hiddenTotalPageCnt" />
										<input type="hidden" id="hiddenTotalCnt" />
									</form>
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
						<div class="resource_list_icon">
							<a href="#" class="switch"></a>
							<a href="#" class="switch_hidden"></a>
							<div class="shad_v"></div>
						</div>

						<div class="resource_list_box" style="height: 100%;">
							<div id="div_tab" class="div_tab divtab_menu250px">
								<ul>
									<li class="selected" style="width: 80px" >
										小区数据配置
									</li>
									<li class="" style="width: 80px" >
										小区基本信息
									</li>
									<li style="width: 60px" class="selected2">
										小区干扰组
									</li>
								</ul>
							</div>
							<div id="div_tab_0">
								<table class="main-table1 half-width" id="selecttable">
								<tr><td class='menuTd' style='width: 30%'>重选数据/恢复默认</td><td><input type="button" name="showCellConfigBtn" id="showCellConfigBtn"
										value="重新选择" /><input type="button" name="restoreDefaultBtn" id="restoreDefaultBtn"
										value="恢复默认" style="margin-left: 4px" /></td></tr>
								</table>
								<div  style="margin-top: 0px;display: block;">
								<table id="loadRefreshlistTable3" class="greystyle-standard"
									width="100%">
									<th>标题</th>
									<th>方案名称</th>
									<th>上传时间</th>
									<th style="width: 10%">操作
									</th>
								</table>
								</div>
								<div id="cellConfigAnalysisBtnDiv">
								<input id="cellConfigConfirmSelectionAnalysisBtn" class="selectOneItem" type="button" data="cellConfig" value="加载小区到地图" />
								</div>
							</div>
							<div id="div_tab_1"  style="display: none">
								<div class="resource_list">
									<table class="main-table1 half-width">
										<tr>
											<td class="menuTd" style="width: 20%">
												小区名
											</td>
											<td id='showCellLabelId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												小区中文名
											</td>
											<td id='showCellNameId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												LAC
											</td>
											<td id='showCellLacId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												CI
											</td>
											<td id='showCellCiId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												BCCH
											</td>
											<td id='showCellBcchId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												TCH
											</td>
											<td id='showCellTchId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												BSIC
											</td>
											<td id='showCellBsicId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												天线方向
											</td>
											<td id='showCellAzimuthId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												天线下倾
											</td>
											<td id='showCellDownId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												基站类型
											</td>
											<td id='showCellBtsTypeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												天线高度
											</td>
											<td id='showCellAntHeightId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												天线类型
											</td>
											<td id='showCellAntTypeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												经度
											</td>
											<td id='showCellLngId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												纬度
											</td>
											<td id='showCellLatId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												覆盖范围
											</td>
											<td id='showCellCoverareaId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">
												频段
											</td>
											<td id='showCellFreqSectionId'></td>
										</tr>
									</table>
								</div>
							</div>
							<div id="div_tab_2" style="display: none">
								<table class="main-table1 half-width" id="interfertable">
								<tr><td class='menuTd' style='width: 30%'>bcch&bsic</td><td align="center">干扰小区组</td></tr>
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
		<%--选择小区配置弹出窗口 --%>
			<div id="reSelCellConfig_Dialog" class="dialog2 draggable"
				style="display:none;left: 60px;top: 60px">
				<div class="dialog_header">
					<div class="dialog_title">选择小区配置数据</div>
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn"
							onclick="$('#reSelCellConfig_Dialog').hide();$('.colordialog2').hide();"></div>
					</div>
				</div>
				<div class="dialog_content" style="width:650px; height:300px; background:#f9f9f9;overflow-x:hidden">
				
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
