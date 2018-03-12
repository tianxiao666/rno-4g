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

<title>邻区关系分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" type="text/css"
	href="../../jslib/dialog/dialog.css" />
<script type="text/javascript" src="js/rno_plan_ncell_analysis.js"></script>
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
table.ncelltable {
   border: 1px solid #888888;
   border-collapse: collapse;
   font-family: Arial,Helvetica,sans-serif;
   margin-top: 10px;
   width: 100%;
}
table.ncelltable th {
   background-color: #CCCCCC;
   border: 1px solid #888888;
   padding: 5px 15px 5px 5px;
   text-align: left;
   vertical-align: baseline;
}
table.ncelltable td {
   background-color: #f5fafe;
   border: 1px solid #AAAAAA;
   padding: 5px 15px 5px 5px;
   vertical-align: text-top;
}

.title_width{
	/* width:70% */
}

.delete_selected_item{
}

.analysis_list_title{
}

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

		$("#showDiaBtn").click(function() {
			$("#analyze_Dialog").css({
				"top" : (40) + "px",
				"right" : (400) + "px",
				"width" : (300) + "px",
				"z-index" : (30)
			});
			$("#analyze_Dialog").show();
		})
		//关闭弹出框
		/* $(".dialog_closeBtn").click(function() {
			$("#analyze_Dialog").hide();
			//$(".black").hide();
		}) */
	});
</script>

</head>


<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			操作正在进行 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<%-- ==============================================  --%>
	<div class="div_left_main" style="width: auto">
		<div class="div_left_content">
			<%-- <div class="div_left_top">邻区关系分析</div> --%>
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
									<li class="selected">邻区关系分析</li>
									<li>邻区分析结果</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content" style="overflow-y:auto; overflow-x:auto; width:360px; height:600px;">
							<div id="div_tab_0">
								<input type="button" id="showDiaBtn" value="打开分析面板" />
								<%-- 小区配置数据--%>
								<%-- <div class="div_title_24px_blue title_width" id="cellConfigTitle">
									<span class="sp_title analysis_list_title"> 小区配置数据</span>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="cellConfigContent">
									<table class="tb-transparent-standard" style="width:100%"
										id="cellConfigAnalysisListTable">
										<s:iterator value="cellConfigList" id="one" status="st">
											<s:if test="#st.Odd">
												<tr class="tb-tr-bg-warmwhite">
											</s:if>
											<s:else>
												<tr class="tb-tr-bg-coldwhite">
											</s:else>
											<td class="bd-right-white"><span><s:property
														value="#one.areaName" /></span></td>
											<td class="bd-right-white"><span><s:property
														value="#one.title" /></span></td>
											<td class="td-standard-date bd-right-white"><span><s:property
														value="#one.collectTime" /></span></td>
											<td class="bd-right-white"><input type="radio"
												value="<s:property value='#one.configId' />"
												name="cellConfigRadio" class="forselect"
												<s:if test="#one.isSelected()">checked=true</s:if>>
												<label for="checkbox"></label></td>
											<td id="<s:property value='#one.configId' />"
												data="cellConfig"><input type="button" value="移除"
												class="removebtn"></td>

											</tr>
										</s:iterator>
									</table>
									<div id="cellConfigAnalysisBtnDiv"
										<s:if test="%{cellConfigList.size>0}" ></s:if>
										<s:else>style="display:none"</s:else>>
										<input type="button" value="加载小区到地图" data="cellConfig"
											class="selectOneItem"
											id="cellConfigConfirmSelectionAnalysisBtn" />
									</div>
								</div> --%>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								
								<%-- NCS数据--%>
								<div class="div_title_24px_blue title_width" id="ncsTitle">
									<span class="sp_title analysis_list_title">NCS测量数据</span>&nbsp;&nbsp;&nbsp;<input type="button" value="搜索数据" id="ncs_search"/>
								</div>
								<div class="div_transparentstandard_nopaddming" id="ncsContent">
									<table class="tb-transparent-standard" style="width:100%" id="ncsAnalysisListTable">
										<%-- <tr class="tb-tr-bg-warmwhite"> --%>
												<%-- <tr class="tb-tr-bg-coldwhite"> 每一行的颜色都不同--%>
											<%-- <td class="bd-right-white"><span></span></td>
											<td class="bd-right-white td_nowrap"><span></span></td>
											<td class="bd-right-white"><input type="radio"
												value="" name="ncsRadio"
												class="forselect"
												</td>
											<td id="" data="ncs"><input
												type="button" value="移除" class="removebtn"></td> --%>

											<%-- </tr> --%>
									</table>
								</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								
								<%-- 小区结构指标数据--%>
								<div class="div_title_24px_blue title_width" id="cellStructTitle">
									<span class="sp_title analysis_list_title"> 小区结构指标数据</span>&nbsp;&nbsp;&nbsp;<input type="button" value="搜索数据" id="structure_search"/>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="cellStructContent">
									<table class="tb-transparent-standard" style="width:100%"
										id="cellStructAnalysisListTable">
										
									</table>
									<%-- 
									<div id="cellStructAnalysisBtnDiv"
										<s:if test="%{cellStructList.size>0}" ></s:if>
										<s:else>style="display:none"</s:else>>
										<input type="button" value="确定" data="cellStruct"
											class="selectOneItem"
											id="cellStructConfirmSelectionAnalysisBtn" />
									</div>
									--%>
								</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								
								<%-- 小区切换数据--%>
								<div class="div_title_24px_blue title_width" id="handoverTitle">
									<span class="sp_title analysis_list_title">小区切换测量数据</span>&nbsp;&nbsp;&nbsp;<input type="button" value="搜索数据" id="handover_search"/>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="handoverContent">
									<table class="tb-transparent-standard" style="width:100%" id="handoverAnalysisListTable">
										
									</table>
								</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>


							</div>
							<%--邻区类型列表 --%>
							<div id="div_tab_1" style="display:none">
							<input type="button" id="clearLinesBtn" value="清除连线" />
							<input type="button" id="clearAnalysisResultBtn" value="清除分析结果" style="margin-left:20px"/>
								<div class="div_title_24px_blue" id="redundantNcellTitle">
									<span class="sp_title"> 冗余邻区</span>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="redundantNcellContent" style="overflow-y:auto">
									<table class="ncelltable" id="redundantNcellTable">
									  <tr >
									  <td style="padding:5px">主小区</td>
									  <td>邻区</td>
									  <td>测量比例</td>
									  <td>切出尝试次数</td>
									  <td>主小区理想覆盖距离</td>
									  <td>距离主小区距离</td>
									  <td>平均信号强度</td>
									  </tr>
									</table>
									
									</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								<div class="div_title_24px_blue" id="omitNcellTitle">
									<span class="sp_title"> 漏定邻区</span>
								</div>
								<div class="div_transparentstandard_nopaddming"
									id="omitNcellContent" style="overflow-y:auto">
									<table class="ncelltable" id="omitNcellTable">
									  <tr >
									  <td style="padding:5px">主小区</td>
									  <td>邻区</td>
									  <td>测量比例</td>
									  <td>主小区理想覆盖距离</td>
									  <td>距离主小区距离</td>
									  <td>平均信号强度</td>
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
		</div>
	</div>

	<%--分析弹出窗 --%>
	<div id="analyze_Dialog" class="dialog2 draggable"
		style="display:none; width:300px;">
		<div class="dialog_header">
			<div class="dialog_title">邻区关系分析</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn"
					onclick="$('#analyze_Dialog').hide()"></div>
					<%-- ;$('.colordialog2').hide(); --%>
			</div>
		</div>
		<form id="checkNcellForm" method="post">
			<div class="dialog_content" style="width:300px; background:#f9f9f9">
				<div class="div_title_24px_blue" id="conditionTitle">
					<span class="sp_title"> 参数设定</span>
				</div>
				<div id="conditionContent">
					<table>
						<tbody>
							<tr>
								<td style="text-align:right">最低测量比例：</td>
								<td><input type="text" name="condition.minDetectRatio"
									value="30" class="number required" />%</td>
							</tr>
							<tr>
								<td style="text-align:right">最小信号强度：</td>
								<td><input type="text" name="condition.minNavss" value="85"
									class="digits required" /></td>
							</tr>
							<tr>
								<td style="text-align:right">切出尝试次数门限：</td>
								<td><input type="text" name="condition.minHoverCnt"
									value="50" class="digits required" /></td>
							</tr>
						</tbody>
					</table>

				</div>
			</div>
		</form>
		<%-- 输入小区的区域 --%>
		<div id="ncellAnalysisContent" style="width:300px; background:#f9f9f9">
		<div class="div_title_24px_blue" id="ncellAnalysisTitle" >
			<span class="sp_title"> 邻区关系检查</span>
		</div>
			<table style="margin:10px auto">
				<tbody>
					<tr>
						<td>待检查小区：</td>
						<td><input type="text" name="cell" id="serverCell" /><br />
						</td>
						<td><input type="button" value="小区检查" id="checkCellBtn" /></td>
					</tr>
					<tr>
						<td colspan="3">
							<hr style="border-top:1px dashed #cccccc; height:1px" ></hr>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="text-align:right"><input type="button"
							value="全网检查" id="checkWholeNetBtn" style="flow:right" /></td>
					</tr>
				</tbody>
			</table>
			<br />


		</div>

		<div id="colorExampleContent" style="text-align; width:300px; background:#f9f9f9">
		<div class="div_title_24px_blue" id="colorExampleTitle">
			<span class="sp_title"> 图例</span>
		</div>
			<table style="margin:10px auto">
				<tbody>
					<tr style="margin:10px auto">
						<td
							style="background-color:#FFFF00;border:1px solid #CCC; width:20px;height:20px"></td>
						<td style="text-align:left">漏配邻区</td>
						<td style="width:5px"></td>
						<td
							style="background-color:#00CC00;border:1px solid #CCC; width:20px;height:20px"></td>
						<td style="text-align:left">合理邻区</td>
					</tr>
					<tr>
						<td style="height:10px" colspan="3"></td>
					</tr>
					<tr style="margin:10px auto">
						<td
							style="background-color:#A1A1A1;border:1px solid #CCC; width:20px;height:20px"></td>
						<td style="text-align:left">冗余邻区</td>
						<td style="width:5px"></td>
						<td
							style="background-color:#66FFFF;border:1px solid #CCC; width:20px;height:20px"></td>
						<td style="text-align:left">自己</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<%-- ncs记录查询 --%>
	<div id="ncs_Dialog" class="dialog2 draggable" style="display:none; width:1150px; top:40px; right:140px; z-index:30;">
		<div class="dialog_header">
			<div class="dialog_title">NCS测量数据</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn" id="close_ncs_Dialog"
					onclick="$('#ncs_Dialog').hide();"></div><%-- $('.colordialog2').hide(); --%>
			</div>
		</div>
		<%@include file="rno_structure_ncs_search.jsp"%>
	</div>
	
	<%-- 小区结构记录查询 --%>
	<div id="structure_Dialog" class="dialog2 draggable" style="display:none; width:750px; top:40px; right:400px; z-index:30;">
		<div class="dialog_header">
			<div class="dialog_title">小区结构指标数据</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn" id="close_ncs_Dialog"
					onclick="$('#structure_Dialog').hide();"></div><%-- $('.colordialog2').hide(); --%>
			</div>
		</div>
		<%@include file="rno_structure_ncs_result_search.jsp"%>
	</div>
	
	<%-- 小区切换数据查询 --%>
	<div id="handover_Dialog" class="dialog2 draggable" style="display:none; width:750px; top:40px; right:400px; z-index:30;">
		<div class="dialog_header">
			<div class="dialog_title">小区切换测量数据</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn" id="close_ncs_Dialog"
					onclick="$('#handover_Dialog').hide();"></div><%-- $('.colordialog2').hide(); --%>
			</div>
		</div>
		<%@include file="rno_channel_switch_statistics_import.jsp"%>
	</div>
	<div id="divColorDiv"></div>
	
	<div id="operSuc" style="display:none; top:40px;left:400px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
            	<table height="100%" width="100%" style="text-align:center">
                	<tr>
                    	<td>
                        	<span> 操作成功</span>
                        </td>
                    </tr>
                </table>
             
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
