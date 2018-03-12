<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>

<title>NCS小区渲染</title>
<%@include file="commonheader.jsp" %>
<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css" type="text/css" />
<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
<script type="text/javascript" src="js/rno_structure_ncs_render_img.js"></script>
<script type="text/javascript" src="js/cityMapGrid.js"></script>
<script type="text/javascript" src="jslib/lib_mapgrid.js"></script>

<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	background: #EEEEEE;
}
#renderPic {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}#map_canvas {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}
</style>
<script type="text/javascript">
	$(function() {
	
	
		//tab选项卡
		tab("div_tab_ncs_cell", "li", "onclick");//项目服务范围类别切换

		$(".switch").click(function() {
			$(this).hide();
			$(".switch_hidden").show();
			$(".resource_list_icon").animate({
				right : '0px'
			}, 'fast');
			$(".resource_list_box").hide("fast");
		});
		$(".switch_hidden").click(function() {
			$(this).hide();
			$(".switch").show();
			$(".resource_list_icon").animate({
				right : '286px'
			}, 'fast');
			$(".resource_list_box").show("fast");
		});
		$(".zy_show").click(function() {
			$(".search_box_alert").slideToggle("fast");
		})
	});
</script>

</head>


<body>
		<div class="loading_cover" id="loadingRenderDiv" style="display: none">
			<div class="cover"></div>
			<h4 class="loading">
				 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
			</h4>
		</div>
		<div class="loading_cover" id="createRenderDiv" style="display: none">
			<div class="cover"></div>
			<h4 class="loading">
				 <em class="loading_fb" id="createContentId"></em>
			</h4>
		</div>
		<div class="div_left_content">
			<div style="padding-bottom: 0px; padding-top: 0px">
				<div class="map_hd" style="padding-bottom: 0px">
					<div class="head_box clearfix" style="padding-bottom: 0px">
						<div class="dialog2 draggable ui-draggable" style="left: 3px; top: 9px;">
							<div style="padding: 5px">

									<%-- liang 2014.2.19 --%>
									<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="100" /> 
									<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" />
									<input type="hidden" id="hiddenTotalPageCnt" /> 
									<input type="hidden" id="hiddenTotalCnt" />
									<select id="ncsRendererType">
										<option value="BE_INTERFER" selected="selected">被干扰系数</option>
										<option value="NET_STRUCT_FACTOR">网络结构指数</option>	
										<option value="SRC_INTERFER">干扰源系数</option>
										<%--<option value="FREQ_CNT">频点数</option>
										<option value="REDUNT_COVER_FACT">冗余覆盖指数</option>
										<option value="OVERLAP_COVER">重叠覆盖度</option>
										<option value="EXPECTED_COVER_DIS">理想覆盖范围</option>
										<option value="OVERSHOOTING_FACT">过覆盖系数</option>
										<option value="DETECT_CNT">小区检测次数</option>
										<option value="CELL_COVER">小区覆盖分量</option>
										<option value="EXPECTED_CAPACITY">理想容量</option>
										<option value="CAPACITY_DESTROY_FACT">容量破坏系数</option>
										<option value="CAPACITY_DESTROY">小区容量破坏分量</option>
										 --%>
									</select>
									<input value="显示专题图" id="displayRenderImg" type="button" onclick ="displayRenderImg()"/>
									<input  type="button" id="loadgiscell" value="加载小区"/>
									<span id="loadingCellTip"></span>
									<form id="conditionForm" method="post">
										<input type="hidden" id="cityId" name="cityId" value=""/>
										<input type="hidden" id="areaId" name="areaId" value="-1"/>
										<input type="hidden" id="hiddenPageSize" name="page.pageSize"
											value="100" /> 
										<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" /> 
										<input type="hidden" id="hiddenTotalPageCnt" value="-1"/> 
										<input type="hidden" id="hiddenTotalCnt" value="-1"/>
									</form>
								<%--</form> --%>
								<%-- 
									<input value="获取zoom" id="btn" type="button"/>
									<input type="text" id="zoomval" size="100"/>
									 --%>
							</div>
						</div>
					</div>
				</div>

			</div>

			<%-- 渲染图--%>
			<div style="padding-top: 0px">
				<div class="map_bd">
	
					<div id="map_canvas"></div>
								
					<div class="resource_list_icon">
						<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
						<div class="shad_v"></div>
					</div>
					<div class="resource_list_box" style="height: 100%;">
						<div class="resource_list">
							<div id="div_tab_ncs_cell" class="div_tab divtab_menu">
								<ul style="width:100%">
									<li class="selected" style="width:40%">小区结构指标</li>
									<li style="width:30%">小区详情</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content">
							<div id="div_tab_ncs_cell_0">
								<table class="main-table1 half-width" id="ncsDetailTable">
								<tr>
									<td class="menuTd" style="width: 20%">小区</td>
									<td id='showCellId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">小区频点数</td>
									<td id='showFreqCntId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">被干扰系数</td>
									<td id='showBeInterferId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">干扰源系数</td>
									<td id='showSrcInterferId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">网络结构指数</td>
									<td id='showNetStructFactorId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">冗余覆盖指数</td>
									<td id='showReduntCoverFactId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">重叠覆盖度</td>
									<td id='showOverlapCoverId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">理想覆盖范围</td>
									<td id='showExpectedCoverDisId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">过覆盖系数</td>
									<td id='showOvershootingFactId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">小区检测次数</td>
									<td id='showDetectCntId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">小区覆盖分量</td>
									<td id='showCellCoverId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">小区容量破坏分量</td>
									<td id='showCapacityDestroyId'></td>
								</tr>
								<tr>
									<td class="menuTd" style="width: 20%">关联邻小区数</td>
									<td id='showInterferNcellCntId'></td>
								</tr>
							</table>
							</div>
							<div id="div_tab_ncs_cell_1" style="display:none">
								<table class="main-table1 half-width" id="gisCellTable">
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
	<%-- 渲染进度窗 --%>
	<div id="renderProgressDiv"  class="dialog_content" style="display:none; width:300px; background:#f9f9f9">
			<center><span id="progressDesc"></span><br/>
			<span id="threadProgress"></span></center>
	</div>

	<%--分析弹出窗 --%>
	<div id="analyze_Dialog" class="dialog2 draggable"
		style="display:none; width:250px;">
		<div class="dialog_header">
			<div class="dialog_title">NCS专题分析</div>
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
	<%--<div>
		<div class="black"></div>
		<div id="analyzeedit_Dialog" class="dialog2 draggable"
			style="display:none; width:410px;">
			<div class="dialog_header">
				<div class="dialog_title">NCS分析修改</div>
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
	</div>--%>
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
