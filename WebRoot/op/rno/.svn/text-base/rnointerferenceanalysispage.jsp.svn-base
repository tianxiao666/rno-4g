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

<title>总干扰分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css"
	type="text/css" />
<script type="text/javascript" src="js/rnointerferenceanalysis.js"></script>
<%--<script type="text/javascript" src="js/rnointerferenceanalysisView.js"></script> --%>
<script type="text/javascript" src="js/trafficstaticsView.js"></script>
<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
<script type="text/javascript" src="jslib/lib_table_sorter.js"></script>
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

.interferDialog {
	background: #99BBE8;
	border: 1px solid #99BBE8;
	border-radius: 5px 5px 5px 5px;
	font-size: 12px;
	padding: 5px;
	position: absolute;
	z-index: 20;
}

.interferDialog_header {
	color: #15428B;
	font-size: 12px;
	font-weight: bold;
	line-height: 15px;
	padding: 2px 0 4px;
	position: relative;
	cursor: move;
}

.interferDialog_title div {
	cursor: pointer;
	display: block;
	float: right;
	height: 30px;
	margin-left: 2px;
	opacity: 0.8;
	width: 16px;
}

.interferDialog_content {
	border: 1px solid #99BBE8;
	background-color: #fff;
	padding: 10px 0;
}

#DialogBody {
	
	border-top: none;
	padding: 0px;
}

.greystyle-standard .greystyle-standard-red-inter td {
	border: 1px solid #CCC;
	padding: 4px;
	padding-left: 6px;
	color: #000;
	line-height: 21px;
	vertical-align: middle;
	background: #7CEFF7;
	height: 20px
} /*#E0E0E0*/ 

/*.EvenOrOddRow td{  
  background:   expression((this.sectionRowIndex%2)?'white':'#E0E0E0');  
  } */


.queryButton { /*background-image: url(../../images/search.png);*/
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
<script type="text/javascript">
var glomapid='<%=session.getAttribute("mapId") %>';
	$(function() {
		$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
		
	});
	
var $ = jQuery.noConflict();
$(function() {
	//tab选项卡
	tab("div_tab", "li", "onclick");//项目服务范围类别切换
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
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	<%--<input type="button" onclick='javascript:testloadbusycell();' value='加载忙小区' /> --%>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">总干扰分析</div> --%>
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
									</select> 区：<select name="areaId" class="required" id="areaId">
										<s:iterator value="countryAreas" id="onearea">
											<option value="<s:property value='#onearea.area_id' />">
												<s:property value="#onearea.name" />
											</option>
										</s:iterator>
										<option value="-1">全部</option>
									</select>
									<input type="button" class="queryButton"  value="打开搜索面板"　style="font-size: 12px"></input>
									 <input  type="button" id="showCellName" class="showCellNameButton" value="显示小区名字"/>
									<span id="loadingCellTip"></span>
									<input  type="button" id="trigger" name="trigger" value=""/>
									<div id="hiddenAreaLngLatDiv" style="display:none">
										<s:iterator value="countryAreas" id="onearea">
											<input type="hidden"
												id="areaid_<s:property value='#onearea.area_id' />"
												value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
										</s:iterator>
									</div>

									<input type="hidden" id="hiddenZoom" name="zoom" value="16" />
									<input type="hidden" id="hiddenLng"
										value="<s:property value='centerPoint.lng' />" /> <input
										type="hidden" id="hiddenLat"
										value="<s:property value='centerPoint.lat' />" /> <input
										type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="100" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> <input type="hidden"
										id="hiddenForcedStartIndex" name="page.forcedStartIndex"
										value="-1" /> <input type="hidden" id="hiddenTotalPageCnt" />
									<input type="hidden" id="hiddenTotalCnt" />
								</form>

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
									<li class="selected" id="interPlanLi">规划优化</li>
									<li id="interDetailsLi">干扰信息</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content" style="overflow-y:auto; overflow-x:auto; width:360px; height:600px">
							<div id="div_tab_0">

								<%--查询结果  --%>
								<%-- 标题--%>
								<%-- input type="button" value="显示渲染图例" id="showRenderColorBtn" /--%>
								<input type="button" value="显示小区干扰窗口" id="showCellInterWinBtn" />
								<%--input type="button" value="定位小区" id="locatecell" /--%>
								<div class="div_title_24px_blue">
									<span class="sp_title"> 频点类型选择</span>
								</div>
								<div style="margin: 3px"></div>
								请选择：<input type="radio" name="freqType" id="freqType" value="GSM900"/>GSM900　<input type="radio" name="freqType" id="freqType" value="GSM1800"/>GSM1800　<input type="radio" name="freqType" id="freqType" value="-1" checked="checked"/>全部
								   　　<br/><br/><input id="loadCellToMap" type="button" value="加载小区信息" name="loadCellToMap" style="margin-right: 0px"/>
									  <span id="loadingStatus"></span>
								<div class="div_transparentstandard_nopaddming"
									id="analysisListDiv_cellconfig">
									<table class="tb-transparent-standard" style="width:100%"
										id="analysisListTable_cellconfig">

									</table>
									<%-- 只有具有小区配置和分析列表时，才可见 --%>
									<div id="showCellBtnDiv" style="display:none">
										<input type="button" value="显示小区" id="showCellBtn" />
									</div>
								</div>
								<%-- --%>
								<%-- 标题--%>
								<div class="div_title_24px_blue">
									<span class="sp_title">最新干扰矩阵计算时间</span>
								</div>
								<div style="margin: 3px"></div>
								<span id="latelytasktime"></span>
								<%-- <input type="hidden" id="latelyTaskId" value=""/>  --%>
								<input type="hidden" id="latelyMartixDescId" value=""/>
								<div class="div_title_24px_blue">
									<span class="sp_title">图例参考</span>
								</div>
								<div style="margin: 3px"></div>
								<table width="66%" border="0" cellspacing="0" cellpadding="0">
								  <tr>
									<td valign="top">
									    <table width="100%" align="right" cellpadding="0" cellspacing="0" style="border:1px solid #ccc; font-size:12px">
											<tr>
											    <td colspan="2" align="center" style="border:1px solid #ccc;font-weight: bold;">与选定频点的同邻频小区颜色设置</td>
											  </tr>
											  <tr>
											    <td width="37%" align="center" style="border:1px solid #ccc;">同邻频情况</td>
											    <td width="40%" align="center" style="border:1px solid #ccc;">颜色</td>
											  </tr>
											  <tr>
											    <td align="center" style="border:1px solid #ccc;">同频</td>
											    <td style="border:1px solid #ccc; background-color: #CE110F">
												   <%-- <table width="20px" height="20px" style="background-color:#CE110F">
												    <tr><td style="border:1px solid #CCC;"></td></tr>
												    </table>
												     --%> 
											    </td>
											  </tr>
											  <tr>
											    <td align="center" style="border:1px solid #ccc;">邻频</td>
											    <td style="border:1px solid #ccc;background-color:#E9D713"></td>
											  </tr>
											  <tr>
											    <td align="center" style="border:1px solid #ccc;">自身</td>
											    <td style="border:1px solid #ccc;background-color:#10000E"></td>
											  </tr>
									  	</table>
								    </td>
								    <td valign="top">
							
								    </td>
								  </tr>
								</table>
							</div>
							
							<div id="div_tab_1"  style="display: none;">

								<div class="div_title_24px_blue">
									<span class="sp_title">小区连线颜色设置</span>
								</div>
								<div style="margin: 3px"></div>
								<table width="60%" align="left" cellpadding="0" cellspacing="0" style=" border:1px solid #ccc;font-size:12px">
									  <tr>
									    <td align="center" width="37%" style="border:1px solid #ccc;">干扰值范围</td>
									    <td align="center" width="42%" style="border:1px solid #ccc;">颜色</td>
									  </tr>
									  <tr>
									    <td align="center" style="border:1px solid #ccc;">小于0.05</td>
									    <td style="border:1px solid #ccc;background-color:#0080FF"></td>
									  </tr>
									  <tr>
									    <td align="center" style="border:1px solid #CCC;">大于等于0.05</td>
									    <td style="border:1px solid #ccc;background-color:#AE0000"></td>
									  </tr>
								</table> 
								<div style="margin: 80px"></div>
								
								<div style="margin: 3px"></div>
								<div class="div_transparent_standard_border" style="margin: 3px;" id="listdisplay">
									<div class="div_title_24px_grey sp_title_left"  style="background-color: #bdd3ef">
										<span class="sp_title" id="interDetailTitle">小区〖〗的 干扰情况</span>
									</div>
									<div style="margin: 3px"></div>
									<table width="100%" border="1" cellspacing="0" cellpadding="0" style=" font-size:12px" id="listdistab">
										  <tr>
										    <td width="97" style="border:1px solid #ccc;height: 24px;font-weight:bold;">干扰邻区</td>
										    <td width="95" style="border:1px solid #ccc;height: 24px;font-weight:bold;">同频干扰系数</td>
										    <td width="100" style="border:1px solid #ccc;height: 24px;font-weight:bold;">邻频干扰系数</td>
										    <td width="100" style="border:1px solid #ccc;height: 24px;font-weight:bold;">是否邻区</td>
										  </tr>
									</table>
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

			<%--分析弹出窗 --%>
			<div id="analyze_Dialog" class="dialog2 draggable"
				style="display: none;">
				<div class="dialog_header">
					<div class="dialog_title">渲染图例</div>
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

			<%--选择NCS详情弹出窗口 --%>
			<div id="reSelNcs_Dialog" class="dialog2 draggable"
				style="display:none;left: 60px;top: 60px">
				<div class="dialog_header">
					<div class="dialog_title">NCS详情查看</div>
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn"
							onclick="$('#reSelNcs_Dialog').hide();$('.colordialog2').hide();"></div>
					</div>
				</div>
				<div class="dialog_content" id="ncs_dialog_content" style="width:1133px; height:300px; background:#f9f9f9;overflow-x:hidden">
				
				</div>
			</div>
			<%--分析弹出窗 --%>
			<div class="interferDialog" id="interference_dialogId"
				style="display:none; top:40px;z-index:30;">
				<div id="dialogHeader" class="interferDialog_header">
					<span id="celllabel"></span>&nbsp;〖<label id="cellname"></label>〗PCI智能优化
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn2"
							onclick="$('#interference_dialogId').hide();$('.black').hide();$('#detailedinterdiv').hide();"></div>
					</div>
				</div>
				<%-- height: auto;overflow-y:auto;height:450px; --%>
				<div id="DialogBody">
					<div class="interferDialog_content" style="background:#FFFFFF;padding: 0px;height: auto;">
		
						<%-- 填充开始  --%>
							<div style="width: 370px; float: left; height:500px;overflow-y:auto;">
								<div style="width:100%;height: auto;text-align: center; padding-top: 6px;">
									<span id="cellFreq" style="font-weight: bold;">bcch:&nbsp;&nbsp;tch:</span>
								</div>
								<table id="freqintersituation" width="100%" border="1px"
									 class="greystyle-standard" >
									<tr>
										<th align="center">
											小区频点
										</th>
										<th align="center">
											干扰-in
										</th>
										<th align="center">
											干扰-out
										</th>
										<th align="center">
											干扰-total
										</th>
										<th align="center">
											频点类型
										</th>
									</tr>
									
								</table>
							</div>
							<div id="detailedinterdiv" style="width: 550px; float: left;display: none;margin-left: 2px; height:500px;overflow-y:auto;">
								<div style="width:100%;height: 24px;text-align: center; padding-top: 6px;font-weight: bold;">
									详细干扰情况〖频点<label id="cellfreqlabel"></label>〗
								</div>
								<table id="detailedintersituation" width="100%" border="1px" class="greystyle-standard"
									cellpadding="0" cellspacing="0">
									<%-- <tr>
										<td colspan="7" align="center" style="font-weight: bold;">
											详细干扰情况〖频点<label id="cellfreqlabel"></label>〗
										</td>
									</tr> --%>
									<tr>
										<th align="center">
											邻区
										</th>
										<th align="center">
											频点
										</th>
										<th align="center">
											干扰-in
										</th>
										<th align="center">
											干扰-out
										</th>
										<th align="center">
											干扰-total
										</th>
										<th align="center">
											是否邻区
										</th>
										<th align="center">
											是否同站
										</th>
										<th align="center">
											邻频关系
										</th>
									</tr>
									
								</table>
							</div>
							<%-- 填充结束 --%>
							<%-- <div style="display: block"> </div>--%> <%--here--%> 
							<div style="font: 0px/0px sans-serif;clear: both;display: block"></div>
							
					</div>
				</div>
			</div>
			
			<%-- 频点编辑窗 --%>
			<div class="interferDialog" id="freq_dialogId"
				style="display:none; top:270px;z-index:30;left：500px;">
				<div id="freq_dialogHeader" class="interferDialog_header">
					<label>频点编辑</label>
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn2"
							onclick="$('#freq_dialogId').hide();"></div>
					</div>
				</div>
				<%-- height: auto;overflow-y:auto;height:450px; --%>
				<div id="freq_DialogBody">
					<div class="interferDialog_content" style="background:#FFFFFF;padding: 0px;height: auto;">
							<div style="width: 450px; float: left;">
								<input type="hidden" id="freqCellName" value=""/>
								<table width="100%" border="1px" class="greystyle-standard">
								<tr>
									<td>bcch</td>
									<td><input id="cellBcch" type="text" style="width: 100px;"/>
									</td>
								</tr>
								<tr>
									<td>tch（半角逗号分隔）</td>
									<td><input id="cellTch" type="text" style="width: 300px;"/>
									</td>
								</tr>
								<tr>
									<td colspan="2" align="right">
										<input type="button" onclick="updateCellFreq()" value="确定"/>
										<input type="button" onclick="$('#freq_dialogId').hide();" value="取消"/>
									</td>
								</tr>
								</table>
							</div>
					</div>
				</div>
			</div>
			<%-- 小区查找窗口 --%>
			<div id="searchDiv" class="dialog2 draggable"
				style="display:none; left: 55px; top: 74px;">
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
					</select> 
					<input type="text" value="" id="conditionValue" /> 
					<input type="button" id="searchCellBtn" value="搜小区" /><br /> 
					输入邻区：<input type="text" id="cellForNcell" />
					<input type="button" id="searchNcellBtn" value="搜邻区" /> <br /> 
					输入频点： <input type="text" id="freqValue" />
					<input type="button" id="searchFreqBtn" value="搜频点" /> <br /><br /> 
					<input type="button" value="清除搜索结果" id="clearSearchResultBtn" />
				</div>
			</div>
			
			<%--分析修改弹出窗 --%>
			<div>
				<div class="black"></div>
				<div id="analyzeedit_Dialog" class="dialog2 draggable"
					style="display:none;width:362px">
					<div class="dialog_header">
						<div class="dialog_title">话备性能专题分析修改</div>
						<div class="dialog_tool">
							<div class="dialog_tool_close dialog_closeBtn2"
								onclick="$('#analyzeedit_Dialog').hide();$('.black').hide();$('.colordialog2').hide();"></div>
						</div>
					</div>
					<div class="dialog_content" style="width:360px; background:#f9f9f9">
						<form action="updateOrAddrnoTrafficRendererAction"
							id="rendererForm" method="post">
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
