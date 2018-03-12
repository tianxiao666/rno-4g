<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
   <%--  <base href="<%=basePath%>"> --%>
    
    <title>性能类指标管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="This is my page" />
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="css/jquery.treeview.css" />
<script type="text/javascript" src="jslib/tree/jquery.treeview.js"></script>
<script type="text/javascript" src="js/rno_4g_sts_index_display_query.js?v=<%=(String)session.getAttribute("version")%>"></script>
  </head>
  
  <body>
  	<div>
  	<h2><span class='navTitle'>当前位置：KPI指标查询 &gt;  性能类KPI指标 </span></h2>
  	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...<br/>
			 <span id="progressDesc"></span>
		</h4>
	</div>
	</div>
	

    <div class="div_left_main">
		<div class="div_left_content">
			<%-- <div class="div_left_top">话务性能查询</div> --%>
			
			
			<%-- 切换标签 --%>
			<div style="padding:10px">
				<div id="frame" style="border:1px solid #ddd">
<%-- 					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">小区语音业务指标</li>
							<li>小区数据业务指标</li>
							<li>城市网络质量指标</li>

						</ul>
					</div> --%>


					<%--标签内容 --%>
					<div class="divtab_content">
					
					<%-- 小区语音业务指标 --%>
						<div id="div_tab_0">
						
						
							<%-- 参数配置 --%>
							<div>
								<form id="form_tab_0"  action="searchLteStsCellIndexForAjaxAction" method="post">
									<input type="hidden" id="tab_0_hiddenPageSize" name="page.pageSize" value="25" />
									<input type="hidden" id="tab_0_hiddenCurrentPage" name="page.currentPage" value="1" />
									<input type="hidden" id="tab_0_hiddenTotalPageCnt" />
									<input type="hidden" id="tab_0_hiddenTotalCnt" />
									<table class="main-table1 half-width" style="padding-top:10px">
										<tr>
										<th class="menuTd" style="text-align:center">指标设置<%-- [<a href="initRnoReportTemplateManagePageAction">管理模板</a>] --%></th>
										<th class="menuTd" style="text-align:center">区域设置</th>
										<th class="menuTd" style="text-align:center">时间设置</th>
										<th class="menuTd" style="text-align:center">对象设置</th>
											<%-- <th class="menuTd" style="text-align:center">日期</th> --%>
											<%-- <th class="menuTd" style="text-align:center">时段</th> --%>
											<%-- <th class="menuTd" style="text-align:center">BSC</th>
											<th class="menuTd" style="text-align:center">小区名称</th> --%>
											<%-- <th class="menuTd" style="text-align:center">汇总方式</th> --%>
										</tr>
										<tr>
										<td style="text-align:center;">
										<input type="hidden" id="indexHiddenStr" name="attachParams.indexColumnStr" />
										<input type="hidden" id="indexHiddenNameStr" name="attachParams.indexColumnNameStr" />
										<a id="indexSet" onclick="$('#selectIndexDiv').show();initIndexDiv();">指标设置</a><br/>
										<%-- 或选择已有模板：<br/>
										 <select name="rptTemplateId" id="rptTemplateId" >
										 <option value="-1">默认模板</option>
										 </select>  --%>
										</td>
										<td style="text-align:center;">
										<%-- 省：<select name="provinceId" class="required" id="provinceId">
													<option value="-1">请选择</option>
													<s:iterator value="zoneProvinceLists" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />
											市：<select name="cityId" class="required" id="cityId">
													<option value="-1">请选择</option>
											</select> <br />
											区：<select name="queryCondition.areaId" class="required" id="areaId">
													<option value="-1">请选择</option>
											</select> --%>
											<div>
											省：<select name="attachParams.provinceId" id="provinceId" style="width:80px;">
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											市：<select name="attachParams.cityId" id="cityId" style="width:100px;">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</div>
											</td>
											<td style="text-align:center;">
											<s:set name="todayDay" 	value="new java.util.Date()" /> <%-- input name="queryCondition.stsDate" value="<s:date name="todayDay" format="yyyy-MM-dd" />" type="text" style="width:90%" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/ --%>
												始: <input id="beginTime" class="Wdate required input-text" type="text"	name="attachParams.begTime"  value="<s:date name="todayDay" format="yyyy-MM-dd HH:mm:ss" />"  readonly="readonly"
												onfocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"/>
												止: <input id="latestAllowedTime" class="Wdate required input-text" name="attachParams.endTime"	type="text"  value="<s:date name="todayDay" format="yyyy-MM-dd HH:mm:ss" />"   readonly="readonly"	
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"/>
											</td>

		<%-- 									<td>
											<select name="queryCondition.stsPeriod">
													<option value="">全部</option>
													<option value="0000-0100">0000-0100</option>
													<option value="0100-0200">0100-0200</option>
													<option value="0200-0300">0200-0300</option>
													<option value="0300-0400">0300-0400</option>
													<option value="0400-0500">0400-0500</option>
													<option value="0500-0600">0500-0600</option>
													<option value="0600-0700">0600-0700</option>
													<option value="0700-0800">0700-0800</option>
													<option value="0800-0900">0800-0900</option>
													<option value="0900-1000">0900-1000</option>
													<option value="1000-1100">1000-1100</option>
													<option value="1100-1200">1100-1200</option>
													<option value="1200-1300">1200-1300</option>
													<option value="1300-1400">1300-1400</option>
													<option value="1400-1500">1400-1500</option>
													<option value="1500-1600">1500-1600</option>
													<option value="1600-1700">1600-1700</option>
													<option value="1700-1800">1700-1800</option>
													<option value="1800-1900">1800-1900</option>
													<option value="1900-2000">1900-2000</option>
													<option value="2000-2100">2000-2100</option>
													<option value="2100-2200">2100-2200</option>
													<option value="2200-2300">2200-2300</option>
													<option value="2300-0000">2300-0000</option>
											</select>
											</td> --%>
											
<%-- 											<td style="text-align:left">
											<input type="text" name="queryCondition.engName" />
											</td> --%>

											<td style="text-align:center">
											<input type="hidden" id="cellHiddenNameStr" name="attachParams.cellNameStr" />
											<input type="hidden" id="cellHiddenLabelStr" name="attachParams.cellLabelStr" />
											小区: <input id="cellStr" type="text" style="width:200px;" readonly="readonly" onclick="$('#selectCellDiv').show()"/>
										<%-- 或选择单小区：<br/>
											<input value="小区英文名" title="小区英文名" type="text" style="color:grey" class="encell"
												onfocus="cellInputFocus(this)" onblur="cellInputBlur(this)" />
												<br />
											<input name="queryCondition.cell" value="" type="hidden" />
												<input value="小区中文名" title="小区中文名" type="text"
												style="color:grey" class="chcell"
												onfocus="cellInputFocus(this)" onblur="cellInputBlur(this)" />
												<input name="queryCondition.cellChineseName" value=""
												type="hidden" /> --%>
											</td>
<%-- 											<td><select id="audioStsType" name="queryCondition.stsType">
													<option value="default">不汇总</option>
													<option value="sum">单天求和</option>
													<option value="avg">单天平均</option>
													<option value="max">单天最大值</option>
													<option value="min">单天最小值</option>
												</select>
											</td> --%>
										</tr>
										
										<tr>
											<td style="text-align:right" colspan="7">
											<input type="submit" onclick="" value="查  询" style="width:90px;" name="search" />
											</td>
										</tr>
									</table>
								</form>
								<form id='downloadFileForm' action='downloadLteStsCellIndexFileAction' method='post' style="display:none">
								<input type="hidden" id="cellNameStrForDownload"  name="attachParams.cellNameStrForDownload" />
								<input type="hidden" id="indexStrForDownload" name="attachParams.indexStrForDownload" />
								<input type="hidden" id="indexNameStrForDownload" name="attachParams.indexNameStrForDownload"  />
								<input type="hidden" id="cityIdForDownload"  name="attachParams.cityIdForDownload" />
								<input type="hidden" id="begTimeForDownload"  name="attachParams.begTimeForDownload" />
								<input type="hidden" id="endTimeForDownload"  name="attachParams.endTimeForDownload" />
								</form>
							</div>
							<%--查询结果  --%>
							<div>
							<div style="padding-top:10px">
								<table width="100%">
									<tr>
										<td style="width:80%">
											<p>
												<font style="font-weight:bold">话务性能指标：</font>
											</p>
										</td>
										<td style="text-align:right">
										<input type="button" onclick="downloadIndexDataFile()" value="导出数据"  style="width:120px;" name="load" />
																					<%-- onclick="exportData('form_tab_0')" value="导出数据" --%>
										</td>

									</tr>

								</table>
								<form id="export_form_tab_0" method="post"
									action="exportRnoStsListAction">
									<input type="hidden" name="searchType" value="CELL_VIDEO" />
								</form>
								<%-- 导出话务数据form --%>
							</div>
							<div style="padding-top:10px;overflow:auto">
								<form id="form1" name="form1" method="post">
									<table id="tab_0_queryResultTab" class="greystyle-standard"
										width="100%">
										<thead>
										<tr>
										<th style="min-width: 120px">起始时间</th>
										<th style="min-width: 120px">结束时间</th>
										<th style="min-width: 180px">小区名称</th>
<%-- 											<th style="width: 8%">DATE</th>
											<th style="width: 10%">PERIOD</th>
											<th style="width: 8%">BSC</th>
											<th style="width: 8%">CELL</th>
											<th style="width: 8%">小区</th>
											<th style="width: 10%">tch信道完好率</th>
											<th style="width: 10%">定义信道数</th>
											<th style="width: 10%">可用信道数</th>
											<th style="width: 10%">载波数</th>
											<th style="width: 10%">无线资源利用率</th>
											<th style="width: 10%">话务量</th>
											<th style="width: 10%">每线话务量</th> --%>
											</tr>
										</thead>
									</table>
								</form>
							</div>
							<div id="page_div_0" class="paging_div" style="border: 1px solid #ddd">
							<span>
								<span class="mr10">共 <em id="tab_0_emTotalCnt" class="blue">0</em> 条记录</span>
								<a class="paging_link page-first" title="首页" onclick="showListViewByPage('first','','form_tab_0','page_div_0','tab_0_')"></a>
								<a class="paging_link page-prev" title="上一页" onclick="showListViewByPage('back','','form_tab_0','page_div_0','tab_0_')"></a>
								<span>
								第 <input type="text" id="tab_0_showCurrentPage" class="paging_input_text" value="0" /> 页&frasl;
								<em id="tab_0_emTotalPageCnt">0</em>页 
								</span>
								<a class="paging_link page-go" title="GO" onclick="showListViewByPage('num','','form_tab_0','page_div_0','tab_0_')">GO</a>
								<a class="paging_link page-next" title="下一页" onclick="showListViewByPage('next','','form_tab_0','page_div_0','tab_0_')"></a>
								<a class="paging_link page-last" title="末页" onclick="showListViewByPage('last','','form_tab_0','page_div_0','tab_0_')"></a>
								</span>
							</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table style="text-align:center;height:100%;width:100%;">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
	    
	    
	    <%-- 指标设置弹窗 --%>
	    <div id="selectIndexDiv" class="dialog2 draggable" style="display:none; left:15%; top: 20%;width: 900px;">
			<div class="dialog_header">
				<div class="dialog_title">指标设置</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#selectIndexDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
<%-- 				省：<select name="provinceId" id="provinceId" style="width:80px;">
					<s:iterator value="provinceAreas" id="onearea">
						<option value="<s:property value='#onearea.area_id' />">
							<s:property value="#onearea.name" />
						</option>
					</s:iterator>
				</select>
				市：<select name="cityId" id="cityId" style="width:100px;">
					<s:iterator value="cityAreas" id="onearea">
						<option value="<s:property value='#onearea.area_id' />">
							<s:property value="#onearea.name" />
						</option>
					</s:iterator>
				</select><br/> --%>
				<div style="float: left;">
					<select size="10" id="defaultIndex" 
							style="width: 300px; height: 300px;" multiple="multiple" ondblclick="PutRightOneClk('defaultIndex','selectedIndex')">
					</select>
				</div>
				<div style="float: left; padding: 0 10 0 10;">
					<input type="button" value="&gt;" id="PutRightOne"
						onclick="PutRightOneClk('defaultIndex','selectedIndex')" /><br /><br />
					<input type="button" value="&lt;" id="PutLeftOne"
						onclick="PutLeftOneClk('defaultIndex','selectedIndex')" /><br /><br />
						<input type="button" value="&gt;&gt;" id="PutRightAll"
						onclick="PutRightAllClk('defaultIndex','selectedIndex')" /><br /><br />
					<input type="button" value="&lt;&lt;" id="PutLeftAll"
						onclick="PutLeftAllClk('defaultIndex','selectedIndex')" /><br /><br />
					<input type="button" value="确认" id="submitIndex"
						onclick="sumIndex()" /><br /><br />
				</div>
				<div>
					<select size="10" id="selectedIndex"
							style="width: 300px; height: 300px;" multiple="multiple" ondblclick="PutLeftOneClk('defaultIndex','selectedIndex')">
					</select>
				</div>
		    </div>
	    </div>
	    <%-- 设置小区弹窗 --%>
	    <div id="selectCellDiv" class="dialog2 draggable" style="display:none; left:25%; top: 40%;width: 690px;">
			<div class="dialog_header">
				<div class="dialog_title">选择小区</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#selectCellDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
				<%-- 省：<select name="provinceId" id="provinceId2" style="width:80px;">
					<s:iterator value="provinceAreas" id="onearea">
						<option value="<s:property value='#onearea.area_id' />">
							<s:property value="#onearea.name" />
						</option>
					</s:iterator>
				</select>
				市：<select name="cityId" id="cityId2" style="width:100px;">
					<s:iterator value="cityAreas" id="onearea">
						<option value="<s:property value='#onearea.area_id' />">
							<s:property value="#onearea.name" />
						</option>
					</s:iterator>
				</select><br/> --%>
				<div style="float: left;">
					<select size="10" id="defaultCell" 
							style="width: 300px; height: 200px;" multiple="multiple" ondblclick="PutRightOneClk('defaultCell','selectedCell')">
					</select>
				</div>
				<div style="float: left; padding: 0 10 0 10;">
					<input type="button" value="&gt;" id="PutRightOne"
						onclick="PutRightOneClk('defaultCell','selectedCell')" /><br /><br />
					<input type="button" value="&lt;" id="PutLeftOne"
						onclick="PutLeftOneClk('defaultCell','selectedCell')" /><br /><br />
					<input type="button" value="&lt;&lt;" id="PutLeftAll"
						onclick="PutLeftAllClk('defaultCell','selectedCell')" /><br /><br />
					<input type="button" value="确认" id="submitCell"
						onclick="sumCell()" /><br /><br />
				</div>
				<div>
					<select size="10" id="selectedCell"
							style="width: 300px; height: 200px;" multiple="multiple" ondblclick="PutLeftOneClk('defaultCell','selectedCell')">
					</select>
				</div>
		    </div>
	    </div>
  </body>
</html>
