<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>LTE小区查询</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<script type="text/javascript" src="js/rno_lte_cellmanage_query.js"></script>

<style type="text/css">
div#lteCellDetailDiv {
	width: 500px;
	position: absolute;
	/*background: none repeat scroll 0 0 #70e1ff;*/
	top: 50%;
	left: 38%;
	margin-left: -100px;
	margin-top: -100px;
	padding:10px 10px;
}
</style>
</head>

<body>
	<div>
		<form id="conditionForm" method="post">
			<input type="hidden" id="hiddenPageSize" name="page.pageSize"
				value="25" /> <input type="hidden" id="hiddenCurrentPage"
				name="page.currentPage" value="1" /> <input type="hidden"
				id="hiddenTotalPageCnt" name="page.totalPageCnt" value="-1" /> <input
				type="hidden" id="hiddenTotalCnt" name="page.totalCnt" value="-1" />
			<table class="main-table1 half-width"
				style="width: 100%; padding-top: 10px">

				<tr>
					<td class="menuTd" style="text-align:center">区域</td>
					<td class="menuTd" style="text-align:center">eNodeB名称</td>
					<td class="menuTd" style="text-align:center">LTE CELL名称</td>
					<td class="menuTd" style="text-align:center">小区PCI</td>
				</tr>
				<tr>

					<td>省：<select class="areaCls"
						name="queryLteCellCond.provinceId" class="required" id="queryProvinceId"
						style="width:70px">
							<option value="-1">全部</option>
							<s:iterator value="provinceAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
					</select><br /> 市：<select class="areaCls" name="queryLteCellCond.cityId"
						class="required" id="queryCityId" style="width:70px">
						  <option value="-1">全部</option>
							<%-- <s:iterator value="cityAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator> --%>
					</select>
					<%-- <br />区：<select class="areaCls" name="queryCell.areaId"
						class="required" id="queryCellAreaId" style="width:70px">
							<option value="-1" selected="true">全部</option>
							<s:iterator value="countryAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
					</select> --%>
					</td>

					<td style=" text-align:center"><input type="text" id="queryEnodebNameId"
						name="queryLteCellCond.enodebName" /></td>
					<td style="text-align: center"><input type="text"
						name="queryLteCellCond.cellName" id='queryCellNameId'/></td>
					<td style="width: 10%; text-align: center"><input type="text"
						name="queryLteCellCond.pci" id="queryCellPciId"/></td>

				</tr>

				<tr>

					<td style="width: 10%; text-align: center" colspan="4">
					<span style="color:red;width:100px;font-family:华文中宋;text-align:center" id="nameErrorText1"></span>
					<span style="color:red;width:100px;font-family:华文中宋;text-align:center" id="nameErrorText2"></span><input
						type="submit" onclick="" value="查  询" name="search" /></td>
				</tr>
			</table>
		</form>
	</div>
	<%--查询结果  --%>
	<div style="padding-top: 10px">
		<table width="100%">
			<tr>
				<td style="width: 20%">
					<p>
						<font style="font-weight: bold">LTE小区信息表：</font>
					</p>
				</td>


			</tr>

		</table>

	</div>
	<div style="padding-top: 10px">
		<table id="queryResultTab" class="greystyle-standard" width="100%">
		    <th style="width: 8%">地区</th>
			<th style="width: 8%">eNodeb名称</th>
			<th style="width: 10%">小区中文名</th>
			<th style="width: 8%">小区PCI</th>
			<th style="width: 8%">小区带宽</th>
			<th style="width: 8%">小区下行频点</th>
			<th style="width: 10%">发射功率</th>

			<th style="width: 10%">操作</th>

		</table>
	</div>
	<div class="paging_div" id="lteCellPageDiv" style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first')"></a> <a
			class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back')"></a> 第 <input type="text"
			id="showCurrentPage" class="paging_input_text" value="1" /> 页/<em
			id="emTotalPageCnt">0</em>页 <a class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num')">GO</a> <a
			class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next')"></a> <a
			class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last')"></a>
	</div>

	<%--详情面板 --%>
	<div id="lteCellDetailDiv" class="dialog2" style="display:none;right: 450px; top: 32%; left: 30%; z-index: 30" >
	  <div class="dialog_header">
			<div class="dialog_title">
					LTE小区详情查看
			</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn"
					onclick="$('#lteCellDetailDiv').hide()"></div>
			</div>
		</div>
	   <div class="dialog_content">
		   <div style="font-weight: bold;margin-bottom:5px">
		   eNodeB名称：<span id="VIEW_ENODEB_NAME"></span>
		   ->小区名称：<span id="VIEW_CELL_NAME"></span>
		   </div>
		   <input type="button" value="同站下一小区详情" id="nextCellDetailBtn" />
		   <div>
	   </div>
	   <table class="greystyle-standard" id="viewCellDetailTable">

	   </table>

	   </div>
	</div>
	<%-- Lte小区信息编辑框 start--%>
	<div id="editLteCellMessage" class="dialog2"
			style="display:none;width: 700px; right: 450px; top: 32%; z-index: 30; left: 30%;">
		<div class="dialog_header">
			<div class="dialog_title">
					LTE小区信息编辑
			</div>
			<div class="dialog_tool">
				<div class="dialog_tool_close dialog_closeBtn"
					onclick="$('#editLteCellMessage').hide()"></div>
			</div>
		</div>
		<div class="dialog_content" style="width:698px; background:#f9f9f9">
			<div style="font-weight: bold;margin-bottom:5px">
				eNodeB名称：
				<span id="editVIEW_ENODEB_NAME"></span>
				&nbsp;&nbsp;&nbsp;->小区名称：
				<span id="editVIEW_CELL_NAME"></span>
			</div>
			<form id="lteCellDetailForm">
				<input type="hidden" id="lteCellIdForEdit" name="lteCellId" value=""/>
				<table id="editCellDetailTable" border="1" align="center" class="main-table1 half-width">

				</table>
				&nbsp;&nbsp;&nbsp;
				<input id="btnUpdate" type="button" value="修 改" name="btnUpdate">
				&nbsp;&nbsp;&nbsp;
				<input id="btnReset" type="button" value="重 置" name="btnReset">
			</form>
		</div>
	</div>
	<%-- Lte小区信息编辑框 end--%>
</body>
</html>
