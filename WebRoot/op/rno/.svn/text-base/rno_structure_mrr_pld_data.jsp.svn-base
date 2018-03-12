
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>
	<form id="MrrPldDataForm" method="post">
		<input type="hidden" id="hiddenMrrId" class="hiddenMrrIdCls"
			name="mrrId" value="" /> 
		<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="25" /> 
		<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" /> 
		<input type="hidden" id="hiddenTotalPageCnt" /> 
		<input type="hidden" id="hiddenTotalCnt" />
	</form>
	
	<div  >
		<table id="queryPldDataTab" class="greystyle-standard" width="100%">
			<thead>
				<th style="width: 8%">小区</th>
				<th style="width: 8%">SUBCELL</th>
				<th style="width: 8%">信道群号</th>
				<th style="width: 8%">路径损耗差异均值</th>

			</thead>
		</table>
	</div>
	<div class="paging_div" id="MrrPldDataPageDiv"
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getMrrPldData,'MrrPldDataForm','MrrPldDataPageDiv')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getMrrPldData,'MrrPldDataForm','MrrPldDataPageDiv')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getMrrPldData,'MrrPldDataForm','MrrPldDataPageDiv')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getMrrPldData,'MrrPldDataForm','MrrPldDataPageDiv')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getMrrPldData,'MrrPldDataForm','MrrPldDataPageDiv')"></a>
	</div>
</body>
</html>
