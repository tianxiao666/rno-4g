<%@ page contentType="text/html; charset=utf-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>
	<form id="NcsNCellDataForm" method="post">
		<input type="hidden" id="hiddenNcsId" class="hiddenNcsIdCls"
			name="ncsId" value="197" /> <input type="hidden" id="hiddenPageSize"
			name="page.pageSize" value="25" /> <input type="hidden"
			id="hiddenCurrentPage" name="page.currentPage" value="1" /> <input
			type="hidden" id="hiddenTotalPageCnt" /> <input type="hidden"
			id="hiddenTotalCnt" />
	</form>
	<div >
		<table id="queryNCellResultTab" class="greystyle-standard"
			width="100%">
			<thead>
				<th style="width: 8%">小区名</th>
				<th style="width: 8%">邻区名</th>
				<th style="width: 10%">CHGR</th>
				<th style="width: 10%">BSIC</th>
				<th style="width: 10%">ARFCN</th>
				<th style="width: 10%">是否定义了邻区关系</th>
				<th style="width: 10%">RECTIMEARFCN</th>
				<th style="width: 10%">REPARFCN</th>
				<th style="width: 10%">TIMES</th>
				<th style="width: 10%">NAVSS</th>
				<th style="width: 10%">TIMES1</th>
				<th style="width: 10%">NAVSS1</th>
				<th style="width: 10%">TIMES2</th>
				<th style="width: 10%">NAVSS2</th>
				<th style="width: 10%">TIMES3</th>
				<th style="width: 10%">NAVSS3</th>
				<th style="width: 10%">TIMES4</th>
				<th style="width: 10%">NAVSS4</th>
				<th style="width: 10%">TIMES5</th>
				<th style="width: 10%">NAVSS5</th>
				<th style="width: 10%">TIMES6</th>
				<th style="width: 10%">NAVSS6</th>
				<th style="width: 8%">TIMESRELSS</th>
				<th style="width: 8%">TIMESRELSS2</th>
				<th style="width: 8%">TIMESRELSS3</th>
				<th style="width: 8%">TIMESRELSS4</th>
				<th style="width: 8%">TIMESRELSS5</th>
				<th style="width: 8%">TIMESABSS</th>
				<th style="width: 8%">TIMESALONE</th>
				<th style="width: 10%">距离（km）</th>
				<th style="width: 10%">干扰度</th>
				<%-- th style="width: 10%">选择邻区</th --%>
			</thead>
		</table>
	</div>
	<div class="paging_div" id='NcsNCellDataPageDiv'
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getNcsNcellData,'NcsNCellDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getNcsNcellData,'NcsNCellDataForm')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getNcsNcellData,'NcsNCellDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getNcsNcellData,'NcsNCellDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getNcsNcellData,'NcsNCellDataForm')"></a>
	</div>
</body>
</html>
