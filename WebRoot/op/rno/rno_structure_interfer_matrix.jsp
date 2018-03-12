<%@ page contentType="text/html; charset=utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>
	<form id="NcsInterferMatrixDataForm" method="post">
		<input type="hidden" id="hiddenNcsId" class="hiddenNcsIdCls"
			name="ncsId" value="111" /> <input type="hidden" id="hiddenPageSize"
			name="page.pageSize" value="25" /> <input type="hidden"
			id="hiddenCurrentPage" name="page.currentPage" value="1" /> <input
			type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
			id="hiddenTotalCnt" />
	</form>
	<div >
		<table id="queryInterferMatrixResultTab" class="greystyle-standard"
			width="100%">
			<thead>
				<th style="width: 8%">CELL</th>
				<th style="width: 10%">NCELL</th>
				<th style="width: 8%">CI</th>
				<th style="width: 8%">CA</th>
				<th style="width: 8%">是否邻区</th>
				<th style="width: 10%">距离（km）</th>
				<%-- th style="width: 10%">INTER_ID</th --%>
			</thead>
		</table>
	</div>
	<div class="paging_div" id='NcsInterferMatrixDataPageDiv'
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getNcsInterferMatrixData,'NcsInterferMatrixDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getNcsInterferMatrixData,'NcsInterferMatrixDataForm')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getNcsInterferMatrixData,'NcsInterferMatrixDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getNcsInterferMatrixData,'NcsInterferMatrixDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getNcsInterferMatrixData,'NcsInterferMatrixDataForm')"></a>
	</div>
</body>
</html>
