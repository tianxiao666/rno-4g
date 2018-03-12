
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>

<body>
	<form id="NcsClusterDataForm" method="post">
		<input type="hidden" id="hiddenNcsId" class="hiddenNcsIdCls"
			name="ncsId" value="197" /> <input type="hidden" id="hiddenPageSize"
			name="page.pageSize" value="25" /> <input type="hidden"
			id="hiddenCurrentPage" name="page.currentPage" value="1" /> <input
			type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
			id="hiddenTotalCnt" />

	</form>
	<div  >
		<table id="queryClusterDataTab" class="greystyle-standard"
			width="100%">
			<thead>
				<th style="width: 8%">簇ID</th>
				<th style="width: 10%">簇约束因子</th>
				<th style="width: 8%">簇权重</th>
				<th style="width: 8%">操作</th>
				<%-- th style="width: 10%">选择小区</th --%>
			</thead>
		</table>
	</div>


	<div class="paging_div" id="NcsClusterDataPageDiv"
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getNcsClusterData,'NcsClusterDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getNcsClusterData,'NcsClusterDataForm')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getNcsClusterData,'NcsClusterDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getNcsClusterData,'NcsClusterDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getNcsClusterData,'NcsClusterDataForm')"></a>
	</div>
</body>
</html>
