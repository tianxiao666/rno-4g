<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<title>NCS测量数据</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_structure_ncs_search.js?v=<%=(String)session.getAttribute("version")%>"></script>

</head>

<body>

	<form id="ncsDescDataForm" method="post">
		 <input type="hidden" id="hiddenCurrentPage"
			name="page.currentPage" value="1" /> <input type="hidden"
			id="hiddenTotalPageCnt" /> <input type="hidden"
			id="hiddenTotalCnt" />
		<div>
			<table class="main-table1 half-width"
				style="padding-top: 10px;">
				<tr>
					<td class="menuTd" style="text-align: center"><span
						style="padding-top: 0px">区域</span></td>
					<td class="menuTd" style="text-align: center ">BSC名称</td>
					<td class="menuTd" style="text-align: center ">测试文件名称</td>
					<td class="menuTd" style="text-align: center">频段</td>
					<td class="menuTd" style="text-align: center">测试时间</td>
				</tr>

				<tr>
					<td style="text-align: left">省：<select name="provinceId"
						class="required" id="ncs_provinceId2">
							<%-- option value="-1">请选择</option --%>
							<s:iterator value="provinceAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
					</select> <br />市：<select name="cond['cityId']" class="required"
						id="ncs_cityId2">
							<s:iterator value="cityAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
					</select> <br />区：<select name="cond['AREAID']" class="required"
						id="ncs_areaId2">
							<s:iterator value="countryAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
					</select>
					</td>
					<td style="text-align: left"><input type="text"
						name="cond['bsc']" / style="width:94%"></td>
					<td style="text-align: left"><input type="text"
						name="cond['fileNAME']" / style="width:94%"></td>
					<td style="text-align: left"><select
						name="cond['FREQSECTION']">
							<option value=' '>全部</option>
							<option value='GSM900'>GSM900</option>
							<option value='GSM1800'>GSM1800</option>
					</select></td>
					<td style="text-align: left">从 <s:set name="begtime"
							value="new java.util.Date()" /> <input
						name="cond['begTime']"
						value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
						type="text"
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly class="Wdate input-text" style="width: 132px;" />
						<br />至 <s:set name="endtime" value="new java.util.Date()" />
						<input name="cond['endTime']"
						value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
						type="text"
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
						readonly class="Wdate input-text" style="width: 132px;" />

					</td>

				</tr>
				<tr>

					<td style=" text-align: right" colspan="5">
					每页记录数量:
					<select id="hiddenPageSize" name="page.pageSize">
					<option value="25">每页25条记录</option>
					<option value="50">每页50条记录</option>
					<option value="10000000">不分页</option>
					</select>
					
					<input
						type="button" onclick="" value="查  询" style="width: 90px;"
						id="searchNcsDescBtn" name="search" /></td>
				</tr>
			</table>
		</div>
	</form>
	<%--查询结果  --%>
	<div style="padding-top: 10px">
		<table width="100%">
			<tr>
				<td style="width: 20%">
					<p>
						<font style="font-weight: bold">ncs数据列表</font>
					</p>
				</td>
			</tr>
			<%-- <tr>
				<td style="width: 20%">
					<p>
						<input type="button" onclick="analysisNcsStructure()"
							value="提交汇总计算" /><input type="button" id="ncsprompt" style="color:#ffffff;background:#3399ff;background-color:#3399ff; border:1px ridge;border-style:outset;font-weight:bold" title="点击提示" alt="点击提示" onclick="clickNcsPrompt()"
							value="?" />
						<input type="button" id="rmvSelectedItems" name="rmvSelectedItems" style="margin-left: 60px"
							value="删除选择项" />
					</p>
				</td>
			</tr> --%>

		</table>

	</div>
	<div style="padding-top:10px;max-height:300px;overflow-y:auto">
		<table id="queryNcsResultTab" class="greystyle-standard"
			width="100%">
			<tr>
				<th><input type='checkbox' id='selAllNcsCb' />全选</th>
				<th>文件名称</th>
				<th>BSC</th>
				<th>频段</th>
				<th>开始测量时间</th>
				<th>SEGTIME</th>
				<th>ABSS</th>
				<th>NUMFREQ</th>
				<th>RECTIME</th>
				<%-- <th>RID</th>
				<th>ECNOABSS</th> --%>
				<th>RELSS</th>
				<th>RELSS2</th>
				<th>RELSS3</th>
				<th>RELSS4</th>
				<th>RELSS5</th>
				<%-- <th>操作</th> --%>
			</tr>
		</table>
	</div>
	<div class="paging_div" id='ncsResultPageDiv'
		style="border: 1px solid #ddd">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',doQueryNcsDesc,'ncsDescDataForm')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',doQueryNcsDesc,'ncsDescDataForm')"></a>
		第 <input type="text" id="showCurrentPage"
			class="paging_input_text" value="1" /> 页/<em
			id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
			title="GO"
			onclick="showListViewByPage('num',doQueryNcsDesc,'ncsDescDataForm')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',doQueryNcsDesc,'ncsDescDataForm')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',doQueryNcsDesc,'ncsDescDataForm')"></a>
	</div>



</body>
</html>
