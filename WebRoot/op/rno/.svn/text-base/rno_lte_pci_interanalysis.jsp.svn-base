<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html>
<head>

<title>PCI干扰分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="jslib/farbtastic/farbtastic.css"
	type="text/css" />
	
<script type="text/javascript" src="js/rno_lte_pci_interanalysis.js"></script>
<script type="text/javascript" src="js/trafficstaticsView.js"></script>
<script type="text/javascript" src="jslib/farbtastic/farbtastic.js"></script>
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
} 


.queryButton { 
	background-color: #4A8BF4;
	display: inline-block;
	height: 25px;
	vertical-align: middle;
	width: auto;
	overflow: visible;
	font-size: 12px;
}
.showCellNameButton { 
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

var $ = jQuery.noConflict();
$(function() {
	//tab选项卡
	tab("div_tab", "li", "onclick");//项目服务范围类别切换
})

</script>
</head>


<body>

	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>

	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
		
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
									<li class="selected" id="interPlanLi">PCI优化</li>
									<%-- <li  id="interDataLi">TOP干扰分析</li> --%>
								</ul>
							</div>
						</div>
						<div class="divtab_content" style="overflow-y:auto; overflow-x:auto; width:360px; height:600px">
							<div id="div_tab_0">
								<%-- 标题--%>
								<div class="div_title_24px_blue">
									<span class="sp_title">频段类型</span>
								</div>
								<div style="margin: 3px"></div>
								请选择：<input type="radio" name="freqType" id="freqType" value="D频段"/>D频段　
									    <input type="radio" name="freqType" id="freqType" value="F频段"/>F频段
									     <input type="radio" name="freqType" id="freqType" value="E频段"/>E频段
								　<%--<input type="radio" name="freqType" id="freqType" value="-1" checked="checked"/>全部 --%>
								   　　<br/><br/><input id="loadCellToMap" type="button" value="加载小区信息" name="loadCellToMap" style="margin-right: 0px"/>	
								
								<%-- 标题--%>
								<div class="div_title_24px_blue">
									<span class="sp_title">干扰矩阵选择</span>
								</div>
								<div style="margin: 3px"></div>
								<select name="pciMatrix" class="required" id="pciMatrix">
										
								</select>
								<br/><br/><input id="loadPciMatrix" type="button" value="加载干扰矩阵"
												 name="loadPciMatrix" style="margin-right: 0px"/>	
								<span id="matrixTip"></span>
								
								<div class="div_title_24px_blue">
									<span class="sp_title">图例参考</span>
								</div>
								<div style="margin: 3px"></div>
								<table width="66%" border="0" cellspacing="0" cellpadding="0">
								  <tr>
									<td valign="top">
									    <table width="100%" align="right" cellpadding="0" cellspacing="0" style="border:1px solid #ccc; font-size:12px">
											<tr>
											    <td colspan="2" align="center" style="border:1px solid #ccc;font-weight: bold;">颜色设置</td>
											  </tr>
											  <tr>
											    <td width="37%" align="center" style="border:1px solid #ccc;"></td>
											    <td width="40%" align="center" style="border:1px solid #ccc;">颜色</td>
											  </tr>
											  <tr>
											    <td align="center" style="border:1px solid #ccc;">自身</td>
											    <td style="border:1px solid #ccc;background-color:#10000E"></td>
											  </tr>
											  <tr>
											    <td align="center" style="border:1px solid #ccc;">干扰小区</td>
											    <td style="border:1px solid #ccc;background-color:#458B00"></td>
											  </tr>
											 
									  	</table>
								    </td>
								    <td valign="top">
							
								    </td>
								  </tr>
								</table>
							</div>
							<%-- <div id="div_tab_1">
							</div> --%>
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
			
			<div class="interferDialog" id="interference_dialogId"
				style="display:none;left:30%; top:30%;z-index:30;">
				<div id="dialogHeader" class="interferDialog_header">
					<span id="celllabel"></span>&nbsp;〖<label id="cellname"></label>〗小区干扰分析
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn2"
							onclick="$('#interference_dialogId').hide();$('.black').hide();$('#detailedinterdiv').hide();"></div>
					</div>
				</div>
				<%-- height: auto;overflow-y:auto;height:450px; --%>
				<div id="DialogBody">
					<div class="interferDialog_content" style="background:#FFFFFF;padding: 0px;height: auto;">
						<table id="pciAnalysisTable" width="100%" border="1px"
									 class="greystyle-standard" >
							<tr><td>优化方案</td>
							<td>in干扰值</td>
							<td>out干扰值</td>
							<td>总干扰值</td>
							<td>是否修改</td></tr>

						</table>
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
						<option value="cell">CELLID</option>
						<option value="chineseName">小区名</option>
					<%--<option value="site">SITE</option>
						<option value="lac">LAC</option>
						<option value="ci">CI</option> --%>
					</select> 
					<input type="text" value="" id="conditionValue" /> 
					<input type="button" id="searchCellBtn" value="搜小区" /><br /> 
					<%--输入邻区：<input type="text" id="cellForNcell" />
					<input type="button" id="searchNcellBtn" value="搜邻区" /> <br /> 
					输入频点： <input type="text" id="freqValue" />
					<input type="button" id="searchFreqBtn" value="搜频点" /> <br /><br />  --%>
					<input type="button" value="清除搜索结果" id="clearSearchResultBtn" />
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
