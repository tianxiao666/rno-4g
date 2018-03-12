<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>LTE邻区关系查询</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<script type="text/javascript" src="js/rno_lte_ncellmanage_query.js"></script>
</head>

<body>
	<div>
		<form id="conditionForm" method="post">
			<%-- 分页信息 --%>
			<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="25" /> 
			<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" /> 
			<input type="hidden" id="hiddenTotalPageCnt" name="page.totalPageCnt" value="-1" /> 
			<input type="hidden" id="hiddenTotalCnt" name="page.totalCnt" value="-1" />
				
			<table class="main-table1 half-width"
				style="width: 100%; padding-top: 10px">
				<tr>
					<td class="menuTd" style="display:none;text-align:center">区域</td>
					<td class="menuTd" style="text-align:center">主小区名称</td>
					<td class="menuTd" style="text-align:center">邻小区名称</td>
					<td class="menuTd" style="text-align:center">主小区site</td>
					<td class="menuTd" style="text-align:center">邻小区site</td>
				</tr>
				<tr>
					<td style="display:none;text-align:left">省：<select class="areaCls"
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
					<td style=" text-align:center"><input type="text" id="queryCellNameId"
						name="queryLteCellCond.cellName" />
						</td>
					<td style="text-align: center"><input type="text" id='queryNCellNameId'
						name="queryLteCellCond.ncellName" />
						</td>
					<td style="text-align: center"><input type="text" id='queryCellSiteId'
						name="queryLteCellCond.cellSite" />
						</td>
					<td style="text-align: center"><input type="text" id='queryNCellSiteId'
						name="queryLteCellCond.ncellSite" />
						</td>
				</tr>

				<tr>
					<td style="width: 10%; text-align:center" colspan="5">
					<span style="color:red;width:100px;font-family:华文中宋;text-align:center" id="nameErrorText1"></span>
					<span style="color:red;width:100px;font-family:华文中宋;text-align:center" id="nameErrorText2"></span><input
						type="submit" onclick="" value="查  询" name="search" />
						</td>
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
						<font style="font-weight: bold">LTE邻区关系信息：</font>
					</p>
				</td>
			</tr>
		</table>
	</div>
	
	<div style="padding-top: 10px">
		<table id="queryResultTab" class="greystyle-standard" width="100%">
		    <th style="width: 8%">主小区名称</th>
			<th style="width: 8%">邻小区名称</th>
			<th style="width: 10%">主小区id</th>
			<th style="width: 8%">主小区eNodeB Id</th>
			<th style="width: 8%">邻小区id</th>
			<th style="width: 8%">邻小区eNodeB Id</th>
			<th style="width: 10%">主小区site Id</th>
			<th style="width: 10%">邻小区site Id</th>
			<th style="width: 10%">操作</th>
		</table>
	</div>
	<div class="paging_div" id="lteCellPageDiv" style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> 
		<a class="paging_link page-first" title="首页"
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
</body>
</html>
