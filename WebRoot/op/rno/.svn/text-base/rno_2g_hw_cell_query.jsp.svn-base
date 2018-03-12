<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>华为2G小区数据查询</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>

<link href="jslib/jquery-ui-1.11.1.custom/jquery-ui.css"
	rel="stylesheet">
<link
	href="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.css"
	rel="stylesheet">
<script src="jslib/jquery-ui-1.11.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="js/rno_2g_hw_cell_query.js"></script>
<script
	src="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript">

</script>

<style type="text/css">
body {
	margin: 50px;
}

.greystyle-standard-noborder tr td {
	padding: 0
}

.dict_left {
	width: 130px;
	float: left;
	left: 0;
	z-index: 1;
	border: 1px;
	margin: 5px
}

.dict_right {
	width: 100%;
	left: 200px;
	float: left;
	border: 1px;
	margin: 5px
}

/* 覆盖分页面板的对齐样式 */
.paging_div {
	text-align: left;
}

select {
	width: 100px;
}

.sectionDivCls {
	margin-top: 10px;
}
.importTitle {
	background: url("images/ui-bg_glass_65_ffffff_1x400.png") repeat-x scroll 50% 50% #ffffff;
	border: 1px solid #fbd850;
	color: #eb8f00;
	font-weight: bold;
	padding-left: 2.2em;
	height:30px;
	cursor: pointer;
	border-radius: 5px;
}
.importContent {
	background: url("images/ui-bg_highlight-soft_100_eeeeee_1x100.png") repeat-x scroll 50% top #eeeeee;
	border: 1px solid #dddddd;
	padding: 1em 2.2em;
	border-radius: 5px;
}
</style>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
</script>

<body>
<body>
   <div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在查找 <em class="loading_fb"></em>,请稍侯...
		</h4>
	</div>
	<span class='navTitle'><h2>当前位置：资源管理 > 小区数据查询 > 华为2G小区数据查询</h2></span>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
			<span id="progressDesc"></span>
		</h4>
	</div>



	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">cell</a></li>
			<li><a href="#tabs-2">ncell </a></li>
		</ul>
		<div id="tabs-1">
					<form id="searchCellForm">
					<input type="hidden" id="cellParam" name="attachParams['cellParam']" value="" />
					<input type="hidden" id="cellBsc" name="attachParams['cellBsc']" value="" />
					<input type="hidden" id="cellDate" name="attachParams['cellDate']" value="" />
					<input type="hidden" id="cellForCell" name="attachParams['cellForCell']" value="" />
						<select id="provincemenu">
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select> <select id="citymenu" name="attachParams['cityId']">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select>
						<div style="margin: 9px"></div>
						<div id="searchImportDiv" style="height:153px;background:url('images/ui-bg_glass_65_ffffff_1x400.png') repeat-x scroll 50% 50% #ffffff;border:1px solid #fbd850">
							<div style="margin: 9px"></div>
							<%--  input type="hidden" name="fileCode" id='fileCode'
								value="2GERICELLFILE" /--%> <input type="hidden"
								id="hiddenPageSize" name="page.pageSize" value="25" /> <input
								type="hidden" id="hiddenCurrentPage" name="page.currentPage"
								value="1" /> <input type="hidden" id="hiddenTotalPageCnt" /> <input
								type="hidden" id="hiddenTotalCnt" value="-1"
								name="page.totalCnt" />
							<table>
								<tr>
									<td style="padding-left:30px">所选区域:<input type="text" id="area1" name="" value="" readonly="readonly"/></td>
									<td style="padding-left:30px"><label>日期:</label>
									<select name="attachParams['cell_targetDate']" id="cell_targetDate" style="width:100px;">
									<option>ALL</option>
                                    </select>
									</td><td><input type="button" 　style="font-size: 12px" value="更多" class="cell_moreDate"></td>
									<td style="padding-left:30px">BSC:</td>
									<td><select name="cell_targetBsc" id="cell_targetBsc" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="cell_moreBsc"></td>
							<td style="padding-left:30px">CELL_NAME:</td>
									<td><select name="attachParams['cell_targetCell']" id="cell_targetCell" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="cell_moreCell"><span id="cell_CellErr" style="color: red;"></span></td>
							<td style="padding-left:30px">参数名:</td>
									<td><select name="cell_targetParam" id="cell_targetParam" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="cell_moreParam"></td>
									
									<td style="padding-left:30px"><input type="button"
										id="searchCellBtn" value="查询记录"></input></td>
									<td style="padding-left:30px"><input type="button"
										id="exportCellBtn" value="导出查询结果"></input></td>
								</tr>
							</table>
						</div>
					</form>
			<div id="cellListDiv" class="sectionDivCls">
			<div style="width:1388px; position: relative;overflow:auto;" class="divtab_content" id="">
				<table id="cellListTab" class="greystyle-standard"
					style="width:auto;margin-left:0">
					<thead>
						<th>地市</th>
							<th>日期</th>
							<th>BSC</th>
							<th>CELL</th>
							<th>参数1</th>
							<th>参数2</th>
							<th>参数3</th>
							<th>参数4</th>
					</thead>
				</table>
				</div>
				<div class="paging_div" id="cellListPageDiv"
					style="border: 1px solid #ddd">
					<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
						条记录
					</span> <a class="paging_link page-first" title="首页"
						onclick="showListViewByPage('first',queryHw2GCellData,'searchCellForm','cellListPageDiv')"></a>
					<a class="paging_link page-prev" title="上一页"
						onclick="showListViewByPage('back',queryHw2GCellData,'searchCellForm','cellListPageDiv')"></a>
					第 <input type="text" id="showCurrentPage" class="paging_input_text"
						value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
						class="paging_link page-go" title="GO"
						onclick="showListViewByPage('num',queryHw2GCellData,'searchCellForm','cellListPageDiv')">GO</a>
					<a class="paging_link page-next" title="下一页"
						onclick="showListViewByPage('next',queryHw2GCellData,'searchCellForm','cellListPageDiv')"></a>
					<a class="paging_link page-last" title="末页"
						onclick="showListViewByPage('last',queryHw2GCellData,'searchCellForm','cellListPageDiv')"></a>
				</div>
			</div>
		</div>

		<div id="tabs-2">
			<form id="searchNcellForm">
				<input type="hidden" name="fileCode" id='fileCode'
					value="2GERICELLFILE" /> <input type="hidden"
					id="hiddenPageSize" name="page.pageSize" value="25" /> <input
					type="hidden" id="hiddenCurrentPage" name="page.currentPage"
					value="1" /> <input type="hidden" id="hiddenTotalPageCnt" /> <input
					type="hidden" id="hiddenTotalCnt" value="-1" name="page.totalCnt" />
					
					<input type="hidden" id="ncellBsc" name="attachParams['ncellBsc']" value="" />
					<input type="hidden" id="ncellParam" name="attachParams['ncellParam']" value="" />
					<input type="hidden" id="ncellDate" name="attachParams['ncellDate']" value="" />
					<input type="hidden" id="ncellForCell" name="attachParams['ncellForCell']" value="" />
					<input type="hidden" id="ncellForNcell" name="attachParams['ncellForNcell']" value="" />
				<table>
					<tr>
						<td><select id="provincemenu3">
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select> <select id="citymenu3" name="attachParams['cityId']">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select></td>
						<td style="padding-left:30px"><label>日期:</label>
									<select name="attachParams['ncell_targetDate']" id="ncell_targetDate" style="width:100px;">
									<option>ALL</option>
                                    </select>
									</td><td><input type="button" 　style="font-size: 12px" value="更多" class="ncell_moreDate"></td>
									<td style="padding-left:30px">BSC:</td>
									<td><select name="ncell_targetBsc" id="ncell_targetBsc" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="ncell_moreBsc"></td>
							<td style="padding-left:30px">CELL:</td>
									<td><select name="attachParams['ncell_targetCell']" id="ncell_targetCell" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="ncell_moreCell"><span id="ncell_CellErr" style="color: red;"></span></td>
							<td style="padding-left:30px">NCELL:</td>
									<td><select name="attachParams['ncell_targetNcell']" id="ncell_targetNcell" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="ncell_moreNcell"><span id="ncell_NcellErr" style="color: red;"></span></td>
							<td style="padding-left:30px">参数名:</td>
									<td><select name="attachParams['ncell_targetParam']" id="ncell_targetParam" style="width:100px;">
									<option>ALL</option>
							</select></td><td><input type="button" 　style="font-size: 12px" value="更多" class="ncell_moreParam"></td>
									
									<td style="padding-left:30px"><input type="button"
										id="searchNcellBtn" value="查询记录"></input></td>
									<td style="padding-left:30px"><input type="button"
										id="exportNcellBtn" value="导出查询结果"></input></td>
					</tr>
				</table>

			</form>
			<div id="ncellListDiv" class="sectionDivCls">
			<div style="width:1388px; position: relative;overflow:auto;" class="divtab_content" id="">
				<table id="ncellListTab" class="greystyle-standard"
					style="width:auto;margin-left:0">
					<thead>
						<th>地市</th>
							<th>日期</th>
							<th>BSC</th>
							<th>CELL</th>
							<th>参数1</th>
							<th>参数2</th>
							<th>参数3</th>
							<th>参数4</th>
					</thead>
				</table>
				</div>
				<div class="paging_div" id="ncellListPageDiv"
					style="border: 1px solid #ddd">
					<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
						条记录
					</span> <a class="paging_link page-first" title="首页"
						onclick="showListViewByPage('first',queryHw2GNcellData,'searchNcellForm','ncellListPageDiv')"></a>
					<a class="paging_link page-prev" title="上一页"
						onclick="showListViewByPage('back',queryHw2GNcellData,'searchNcellForm','ncellListPageDiv')"></a>
					第 <input type="text" id="showCurrentPage" class="paging_input_text"
						value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
						class="paging_link page-go" title="GO"
						onclick="showListViewByPage('num',queryHw2GNcellData,'searchNcellForm','ncellListPageDiv')">GO</a>
					<a class="paging_link page-next" title="下一页"
						onclick="showListViewByPage('next',queryHw2GNcellData,'searchNcellForm','ncellListPageDiv')"></a>
					<a class="paging_link page-last" title="末页"
						onclick="showListViewByPage('last',queryHw2GNcellData,'searchNcellForm','ncellListPageDiv')"></a>
				</div>
			</div>
		</div>
	</div>
	<%-- ============================================tab1 窗口开始=================================================== --%>
								<%-- cell下的更多日期 --%>
								<div id="cell_dateWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">日期选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#cell_dateWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="cell_waitforselDate" size="9" multiple id="cell_waitforselDate" style="width:200px; overflow:hidden">
                                	</select>
										<input type="button" id="cell_loadMoreDate" name="" style="margin-top:8px;" value="加载更多日期" />
											</td>
											<td>
											<input type="button" id="cell_selAllDate" name="" value="选择全部"  /><br><br>
											<input type="button" id="cell_partOfSelDate" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="cell_partOfDelDate" name="" value=" << 删除"  /><br><br>
											<input type="button" id="cell_delAllDate" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="cell_selectedDate" size="9" multiple id="cell_selectedDate" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="cell_confirmDate" name="" value="确定" />　　　　<input type="button" id="cell_cancelDate" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- cell下的更多BSC --%>
								<div id="cell_bscWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">BSC选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#cell_bscWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="cell_waitforselBsc" size="9" multiple id="cell_waitforselBsc" style="width:200px; overflow:hidden">
                                	</select>
										
											</td>
											<td>
											<input type="button" id="cell_selAllBsc" name="" value="选择全部"  /><br><br>
											<input type="button" id="cell_partOfSelBsc" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="cell_partOfDelBsc" name="" value=" << 删除"  /><br><br>
											<input type="button" id="cell_delAllBsc" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="cell_selectedBsc" size="9" multiple id="cell_selectedBsc" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="cell_confirmBsc" name="" value="确定" />　　　　<input type="button" id="cell_cancelBsc" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- cell下的更多cell --%>
								<div id="cell_cellWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">CELL输入窗口</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#cell_cellWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table style="width: 366px; ">
											<tr>
											 <td style="padding-bottom: 8px;"><span style="padding-right: 200px"><font style=" color: red">请输入小区英文名，以","隔开</font></span><input type="button" id="cell_confirmCell" name="" value="确定" /></td>
											 </tr>
											 <tr>
											 <td><textarea name="cell_cellInput" id="cell_cellInput" cols="45" rows="30"></textarea></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- cell下的更多param --%>
								<div id="cell_paramWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">参数选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#cell_paramWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="cell_waitforselParam" size="9" multiple id="cell_waitforselParam" style="width:200px; overflow:hidden">
                                	</select>
										
											</td>
											<td>
											<input type="button" id="cell_selAllParam" name="" value="选择全部"  /><br><br>
											<input type="button" id="cell_partOfSelParam" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="cell_partOfDelParam" name="" value=" << 删除"  /><br><br>
											<input type="button" id="cell_delAllParam" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="cell_selectedParam" size="9" multiple id="cell_selectedParam" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="cell_confirmParam" name="" value="确定" />　　　　<input type="button" id="cell_cancelParam" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
<%-- ============================================tab1 窗口结束=================================================== --%>

<%-- ============================================tab2 窗口开始=================================================== --%>
								<%-- ncell下的更多日期 --%>
								<div id="ncell_dateWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">日期选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#ncell_dateWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="ncell_waitforselDate" size="9" multiple id="ncell_waitforselDate" style="width:200px; overflow:hidden">
                                	</select>
										<input type="button" id="ncell_loadMoreDate" name="" style="margin-top:8px;" value="加载更多日期" />
											</td>
											<td>
											<input type="button" id="ncell_selAllDate" name="" value="选择全部"  /><br><br>
											<input type="button" id="ncell_partOfSelDate" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="ncell_partOfDelDate" name="" value=" << 删除"  /><br><br>
											<input type="button" id="ncell_delAllDate" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="ncell_selectedDate" size="9" multiple id="ncell_selectedDate" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="ncell_confirmDate" name="" value="确定" />　　　　<input type="button" id="ncell_cancelDate" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- ncell下的更多BSC --%>
								<div id="ncell_bscWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">BSC选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#ncell_bscWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="ncell_waitforselBsc" size="9" multiple id="ncell_waitforselBsc" style="width:200px; overflow:hidden">
                                	</select>
										
											</td>
											<td>
											<input type="button" id="ncell_selAllBsc" name="" value="选择全部"  /><br><br>
											<input type="button" id="ncell_partOfSelBsc" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="ncell_partOfDelBsc" name="" value=" << 删除"  /><br><br>
											<input type="button" id="ncell_delAllBsc" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="ncell_selectedBsc" size="9" multiple id="ncell_selectedBsc" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="ncell_confirmBsc" name="" value="确定" />　　　　<input type="button" id="ncell_cancelBsc" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- ncell下的更多cell --%>
								<div id="ncell_cellWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">CELL输入窗口</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#ncell_cellWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table style="width: 366px; ">
											<tr>
											 <td style="padding-bottom: 8px;"><span style="padding-right: 200px"><font style=" color: red">请输入小区英文名，以","隔开</font></span><input type="button" id="ncell_confirmCell" name="" value="确定" /></td>
											 </tr>
											 <tr>
											 <td><textarea name="ncell_cellInput" id="ncell_cellInput" cols="45" rows="30"></textarea></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- ncell下的更多param --%>
								<div id="ncell_paramWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">参数选择列表</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#ncell_paramWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table width="500px" style="width: 509px; ">
											<tr>
											<td style="width: 200px; ">
													 未选择参数
                                            <select name="ncell_waitforselParam" size="9" multiple id="ncell_waitforselParam" style="width:200px; overflow:hidden">
                                	</select>
										
											</td>
											<td>
											<input type="button" id="ncell_selAllParam" name="" value="选择全部"  /><br><br>
											<input type="button" id="ncell_partOfSelParam" name="" value="选择  >>"  /><br><br><br>
											<input type="button" id="ncell_partOfDelParam" name="" value=" << 删除"  /><br><br>
											<input type="button" id="ncell_delAllParam" name="" value="删除全部"  />
											</td>
											<td style="width: 200px; ">已选择参数
											<select name="ncell_selectedParam" size="9" multiple id="ncell_selectedParam" style="width:200px; overflow:hidden">
                             
                                	</select>
											</td>
											</tr>
											</tr>
											 <tr align="center">
											 <td colspan="3"><input type="button" id="ncell_confirmParam" name="" value="确定" />　　　　<input type="button" id="ncell_cancelParam" name="" value="取消" /></td>
											 </tr>
											</table>

									</div>
								</div>
								<%-- ncell下的更多ncell --%>
								<div id="ncell_ncellWinDiv" class="dialog2 draggable"
									style="display:none; ">
									<div class="dialog_header">
										<div class="dialog_title">NCELL输入窗口</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#ncell_ncellWinDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content"
										style="background:#f9f9f9;padding:10px">
										    <table style="width: 366px; ">
											<tr>
											 <td style="padding-bottom: 8px;"><span style="padding-right: 200px"><font style=" color: red">请输入邻区英文名，以","隔开</font></span><input type="button" id="ncell_confirmNcell" name="" value="确定" /></td>
											 </tr>
											 <tr>
											 <td><textarea name="ncell_ncellInput" id="ncell_ncellInput" cols="45" rows="30"></textarea></td>
											 </tr>
											</table>

									</div>
								</div>
<%-- ============================================tab2 窗口结束=================================================== --%>
<form id="downloadHwCellDataFileForm"
									action="downloadHw2GCellDataFileAction" method="post">
									<input type="hidden" id="token" name="token" value=""  />
								</form>
</body>


</html>
