
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body>
	<form id="MrrAdmDataForm" method="post">
		<input type="hidden" id="hiddenMrrId" class="hiddenMrrIdCls"
			name="mrrId" value="" /> 
		<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="25" /> 
		<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" /> 
		<input type="hidden" id="hiddenTotalPageCnt" /> 
		<input type="hidden" id="hiddenTotalCnt" />
	</form>
	
	<div  >
		<table id="queryAdmDataTab" class="greystyle-standard" width="100%">
			<thead>
				<th style="width: 8%">文件格式</th>
				<th style="width: 10%">记录日期</th>
				<th style="width: 8%">RECORD_INFO</th>
				<th style="width: 8%">RID</th>
				<th style="width: 8%">TTIME</th>
				<th style="width: 10%">MEASLIM</th>
				<th style="width: 10%">MEASLIM +/-</th>
				<th style="width: 10%">MEASINT</th>
				<th style="width: 10%">MEASTYPE</th>
				<th style="width: 10%">MEASLINK</th>
				<th style="width: 10%">MEASLIM2</th>
				<th style="width: 10%">MEASLIM3</th>
				<th style="width: 10%">MEASLIM4</th>
				<th style="width: 10%">CONTYPE</th>
				<th style="width: 10%">DTMFILTER</th>

			</thead>
		</table>
	</div>
	<div class="paging_div" id="MrrAdmDataPageDiv"
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',getMrrAdmData,'MrrAdmDataForm','MrrAdmDataPageDiv')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',getMrrAdmData,'MrrAdmDataForm','MrrAdmDataPageDiv')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',getMrrAdmData,'MrrAdmDataForm','MrrAdmDataPageDiv')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',getMrrAdmData,'MrrAdmDataForm','MrrAdmDataPageDiv')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',getMrrAdmData,'MrrAdmDataForm','MrrAdmDataPageDiv')"></a>
	</div>
</body>
</html>
