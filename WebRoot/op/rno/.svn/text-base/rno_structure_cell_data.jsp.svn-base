
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>
	<form id="NcsCellDataForm" method="post">
		<input type="hidden" id="hiddenNcsId" class="hiddenNcsIdCls"
			name="ncsId" value="197" /> <input type="hidden" id="hiddenPageSize"
			name="page.pageSize" value="25" /> <input type="hidden"
			id="hiddenCurrentPage" name="page.currentPage" value="1" /> <input
			type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
			id="hiddenTotalCnt" />

	</form>
	<div  >
		<table id="queryCellDataTab" class="greystyle-standard" width="100%">
			<thead>
				<th style="width: 8%">小区名</th>
				<th style="width: 10%">CHGR</th>
				<th style="width: 8%">测量报告总数</th>
				<th style="width: 8%">半速率测量报告</th>
				<th style="width: 8%">未定义邻区测量</th>
				<th style="width: 10%">平均信号强度</th>
				<%-- th style="width: 10%">选择小区</th --%>
			</thead>
		</table>
	</div>
	<div class="paging_div" id="NcsCellDataPageDiv"
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getNcsCellData,'NcsCellDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getNcsCellData,'NcsCellDataForm')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getNcsCellData,'NcsCellDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getNcsCellData,'NcsCellDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getNcsCellData,'NcsCellDataForm')"></a>
	</div>
</body>
</html>
