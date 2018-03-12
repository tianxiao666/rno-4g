<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>话务性能查询</title>

<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_sts_manager.js"></script>
<script type="text/javascript" src="js/provincecityareacascade.js"></script>
<%
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<script type="text/javascript">
var tab0_formArray=null;//全局表单数组
var tab1_formArray=null;//全局表单数组
	function animateOperTips(tip) {
		$("#operInfo").find("#operTip").html(tip);
		animateInAndOut("operInfo", 500, 500, 2000);
	}
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换
	loadevent();
		// 查询提交
		$("#form_tab_0")
				.submit(
						function() {
							//重新初始化分页参数
							if ($(
									"#form_tab_0 input[name='queryCondition.stsDate']")
									.val() == "") {
								alert("请输入日期!")
								return false;
							}
							$("#tab_0_hiddenPageSize").val("25");
							$("#tab_0_hiddenCurrentPage").val("1");
							$("#tab_0_hiddenTotalPageCnt").val("0");
							$("#tab_0_hiddenTotalCnt").val("0");
							var cellValue = $("#form_tab_0 .encell").val();
							var cellChValue = $("#form_tab_0 .chcell").val();
							if (cellValue != "小区英文名") {
								$(
										"#form_tab_0 input[name='queryCondition.cell']")
										.val(cellValue);
							} else {
								$(
										"#form_tab_0 input[name='queryCondition.cell']")
										.val("");
							}
							if (cellChValue != "小区中文名") {
								$(
										"#form_tab_0 input[name='queryCondition.cellChineseName']")
										.val(cellChValue);
							} else {
								$(
										"#form_tab_0 input[name='queryCondition.cellChineseName']")
										.val("");
							}
							getStsData("form_tab_0", "tab_0_");
							getFormDataToExportForm("form_tab_0");//获取查询form表单里的数据 添加到导出form表单里
							return false;
						});

		/**
		 *触发省市区域联动事件
		 */
		function loadevent() {
			getSubAreas("form_tab_0 #provinceId","form_tab_0 #cityId","form_tab_0 #areaId");
			getSubAreas("form_tab_1 #provinceId","form_tab_1 #cityId","form_tab_1 #areaId");
			getSubAreas("form_tab_2 #provinceId","form_tab_2 #cityId",null);
		}
		// 查询提交
		$("#form_tab_1")
				.submit(
						function() {
							//重新初始化分页参数
							if ($(
									"#form_tab_1 input[name='queryCondition.stsDate']")
									.val() == "") {
								alert("请输入日期!")
								return false;
							}
							$("#tab_1_hiddenPageSize").val("25");
							$("#tab_1_hiddenCurrentPage").val("1");
							$("#tab_1_hiddenTotalPageCnt").val("0");
							$("#tab_1_hiddenTotalCnt").val("0");
							var cellValue = $("#form_tab_1 .encell").val();
							var cellChValue = $("#form_tab_1 .chcell").val();
							if (cellValue != "小区英文名") {
								$(
										"#form_tab_1 input[name='queryCondition.cell']")
										.val(cellValue);
							} else {
								$(
										"#form_tab_1 input[name='queryCondition.cell']")
										.val("");
							}
							if (cellChValue != "小区中文名") {
								$(
										"#form_tab_1 input[name='queryCondition.cellChineseName']")
										.val(cellChValue);
							} else {
								$(
										"#form_tab_1 input[name='queryCondition.cellChineseName']")
										.val("");
							}
							getStsData("form_tab_1", "tab_1_");
							getFormDataToExportForm("form_tab_1");//获取查询form表单里的数据 添加到导出form表单里
							return false;
						});
		// 查询提交
		$("#form_tab_2").submit(function() {
			//重新初始化分页参数
			$("#tab_2_hiddenPageSize").val("25");
			$("#tab_2_hiddenCurrentPage").val("1");
			$("#tab_2_hiddenTotalPageCnt").val("0");
			$("#tab_2_hiddenTotalCnt").val("0");
			getStsData("form_tab_2", "tab_2_");
			return false;
		});
	})

	/**
	 *获取查询form表单里的数据 添加到导出form表里
	 **/
	function getFormDataToExportForm(formId) {
	 	var formArray;
		if(formId=='form_tab_0'){
		tab0_formArray = $("#" + formId).formToArray();//form表单提交条件数组对象
		formArray=tab0_formArray;
		}
		if(formId=='form_tab_1'){
		tab1_formArray = $("#" + formId).formToArray();//form表单提交条件数组对象
		formArray=tab1_formArray;
		}
		if (formArray) {
			var name = "";
			var curValue = "";
			var inputHtmlContent = "";
			var exportFormArray = $("#export_" + formId).formToArray();
			name = exportFormArray[0].name;
			curValue = exportFormArray[0].value;
			inputHtmlContent += "<input type='hidden' value='"+curValue+"' name='"+name+"' />";//添加searchType
			for ( var i = 0; i < formArray.length; i++) {//遍历
				name = formArray[i].name;//名称
				curValue = formArray[i].value;//值
				if (name.indexOf("queryCondition.") >= 0 || name=="rptTemplateId") {//获取name以“queryCondition”前缀的值
					inputHtmlContent += "<input type='hidden' value='"+curValue+"' name='"+name+"' />";//隐藏域
				}
			}
			//console.log("inputHtmlContent:"+inputHtmlContent);
			$("#export_" + formId).html(inputHtmlContent);//添加到exportform
		}
	}
	/**
	 *导出数据
	 **/
	function exportData(formId) {
		//var formHtml = $("#export_" + formId).html();
		var formArray;
		if(formId=='form_tab_1'){
		formArray=tab1_formArray;
		}
		if(formId=='form_tab_0'){
		formArray=tab0_formArray;
		}
		if (formArray == null) {
			alert("请先查询数据！");
			return false;
		}
		$("#export_" + formId).submit();//提交
	}
	/**
	 * 获取小区
	 */
	function getStsData(formId, tabFlag) {
		$(".loading_cover").css("display", "block");
		/*
		var stsusl="";
		if(formId=="form_tab_2"){
					stsusl="queryRnoCityNetQualityIndexByPageAction";
					}else{
					stsusl="queryRnoStsListByPageAction";
					}
					//console.log(stsusl);*/
			//旧：queryRnoStsListByPageAction
			//新：queryStsByRptTemplateForAjaxAction
			var action;
			var sendData;
			var id;
			if(formId == "form_tab_0"){
					id=$("#rptTemplateId").find("option:selected").val();
					
					if(id<0){
					action="queryRnoStsListByPageAction";
					}else{
					action="queryStsByRptTemplateForAjaxAction";
					//sendData={'rptTemplateId':id};
					}
				}
			if(formId == "form_tab_1"){
					id=$("#rptTemplateId2").find("option:selected").val();
					if(id<0){
					action="queryRnoStsListByPageAction";
					}else{
					action="queryStsByRptTemplateForAjaxAction";
					//sendData={'rptTemplateId':id};
					}
				}
		//console.log("action:"+action);
		$("#" + formId).ajaxSubmit({
			//url : "'"+stsusl+"'",
			//旧：queryRnoStsListByPageAction
			//新：queryStsByRptTemplateForAjaxAction
			
			url : action,
			data:sendData,
			dataType : 'json',
			success : function(data) {
				//console.log(data.stsList.length==0);
				if (data.stsList.length == 0) {
					//清空之前的查询结果
					showSts(data, tabFlag, formId);
					animateOperTips("该条件下没有数据。");
				} else {
					showSts(data, tabFlag, formId);
				}
			},
			complete : function() {
				$(".loading_cover").css("display", "none");
			}
		});

	}
	function showSts(data, tabFlag, formId) {
		var page = data.newPage;
		var stsList = data.stsList;
		var colorarr = [ "#96C4EB", "#DBEBF8" ];
		//console.log("page===" + page);
		if (page) {
			var pageSize = page.pageSize ? page.pageSize : 0;
			$("#" + tabFlag + "hiddenPageSize").val(pageSize);

			var currentPage = page.currentPage && pageSize != 0 ? page.currentPage
					: 1;
			$("#" + tabFlag + "hiddenCurrentPage").val(currentPage);

			var totalPageCnt = page.totalPageCnt ? page.totalPageCnt : 0;
			$("#" + tabFlag + "hiddenTotalPageCnt").val(totalPageCnt);

			var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
			$("#" + tabFlag + "hiddenTotalCnt").val(totalCnt);

			// 跳转
			$("#" + tabFlag + "emTotalCnt").html(totalCnt);
			$("#" + tabFlag + "showCurrentPage").val(currentPage);
			$("#" + tabFlag + "emTotalPageCnt").html(totalPageCnt);

		}

		var table = $("#" + tabFlag + "queryResultTab");
		// 只保留表头
		//table.empty();
		//table.append(getTableHead());
		var i = 0;
		if (formId == "form_tab_2") {
			// 只保留表头
			table.empty();
			table.append(getTableHead2());
			if (stsList) {
				var tr = "";
				//var STS_DATE="<s:date name='1999/02/06' format='yyyy-MM-dd'/>";
				//var helloworld = "<s\:text name=\"<s\:date name=\"1999/02/06\" format=\"yyyy-MM-dd\"/>\"/>";
				$.each(stsList, function(key, value) {
					//alert(new Date(value.STS_DATE));
					//console.log(i+":"+colorarr[i%2]);
					//class=\"greystyle-standard-whitetr\"
					tr += "<tr  style=\"background-color:" + colorarr[i % 2]
							+ "\">";
					tr += "<td>" + value.STATIC_TIME + "</td>";
					tr += "<td>" + value.GRADE + "</td>";
					tr += "<td>" + value.NAME + "</td>";
					tr += "<td>" + value.SCORE + "</td>";
					tr += "<td>" + value.INDEX_CLASS + "</td>";
					tr += "<td>" + value.INDEX_NAME + "</td>";
					tr += "<td>" + value.INDEX_VALUE + "</td>";
					tr += "</tr>";
					//table.append(tr);
					//console.log(tr);
					i++;
				})
				table.append(tr);
			}
		} else {
			// 只保留表头
			table.empty();
			if(formId == "form_tab_0"){
			table.append(getTableHead("form_tab_0","rptTemplateId"));
			}
			if(formId == "form_tab_1"){
			table.append(getTableHead("form_tab_1","rptTemplateId2"));
			}
			if (stsList.length > 0) {
				if(formId == "form_tab_0" && ("min"==$("#audioStsType").val() || "max"==$("#audioStsType").val() || "sum"==$("#audioStsType").val() || "avg"==$("#audioStsType").val()))
				{
					//console.log("开始GSM语音统计");
					//调整表格字段
					adjustTitle(formId,tabFlag,stsList[0]);
					//显示汇总后的数据
					showStatisticData(formId,table,stsList,colorarr);
					//console.log("统计完成");
					return;
				}
				if(formId == "form_tab_1" && ("max"==$("#dataStsType").val() || "min"==$("#dataStsType").val() || "sum"==$("#dataStsType").val() || "avg"==$("#dataStsType").val()))
				{
					//console.log("开始GSM数据统计");
					//调整表格字段
					adjustTitle(formId,tabFlag,stsList[0]);
					//显示汇总后的数据
					showStatisticData(formId,table,stsList,colorarr);
					//console.log("统计完成");
					return;
				}
				var tr = "";
				//var STS_DATE="<s:date name='1999/02/06' format='yyyy-MM-dd'/>";
				//var helloworld = "<s\:text name=\"<s\:date name=\"1999/02/06\" format=\"yyyy-MM-dd\"/>\"/>";
				$.each(stsList, function(key, value) {
					//alert(new Date(value.STS_DATE));
					//console.log(value.CELL);
					//class=\"greystyle-standard-whitetr\" 
					if(formId == "form_tab_0"){
					var id=$("#rptTemplateId").find("option:selected").val();
					if(id<0){
					tr += "<tr bgcolor=\"" + colorarr[i % 2] + "\">";
					tr += "<td>" + (value.STS_DATE ? value.STS_DATE : "") + "</td>";
					tr += "<td>" + (value.STS_PERIOD ? value.STS_PERIOD : "") + "</td>";
					tr += "<td>" + (value.ENGNAME ? value.ENGNAME : "") + "</td>";
					tr += "<td>" + value.CELL + "</td>";
					tr += "<td>" + (value.CELL_CHINESE_NAME ? value.CELL_CHINESE_NAME : "") + "</td>";
					tr += "<td>" + (value.TCH_INTACT_RATE ? value.TCH_INTACT_RATE : "")+ "</td>";
					tr += "<td>" + (value.DECLARE_CHANNEL_NUMBER ? value.DECLARE_CHANNEL_NUMBER : "")+ "</td>";
					tr += "<td>" + (value.AVAILABLE_CHANNEL_NUMBER ? value.AVAILABLE_CHANNEL_NUMBER : "")+ "</td>";
					tr += "<td>" + (value.CARRIER_NUMBER ? value.CARRIER_NUMBER : "") + "</td>";
					tr += "<td>" + (value.RESOURCE_USE_RATE ? value.RESOURCE_USE_RATE : "") + "</td>";
					tr += "<td>" + (value.TRAFFIC ? value.TRAFFIC : "")+ "</td>";
					tr += "<td>" + (value.TRAFFIC_PER_LINE ? value.TRAFFIC_PER_LINE : "")+ "</td>";
					tr += "</tr>";
					}else{
						tr += "<tr>";
						tr += "<td>" + (value.STS_DATE ? value.STS_DATE : "") + "</td>";
						tr += "<td>" + (value.STS_PERIOD ? value.STS_PERIOD : "")+ "</td>";
						tr += "<td>" + (value.ENGNAME ? value.ENGNAME : "")+ "</td>";
						tr += "<td>" + value.CELL + "</td>";
						for(var i=0;i<form_tab_0_selectedtabfileds.length;i++){
							//console.log("form_tab_0_selectedtabfileds[i]:"+form_tab_0_selectedtabfileds[i]);
							var filed=form_tab_0_selectedtabfileds[i];
							tr += "<td>" + (value[filed] ? value[filed] : "") + "</td>";
						}
						tr += "</tr>";
					}
				}
				if(formId == "form_tab_1"){
					var id=$("#rptTemplateId2").find("option:selected").val();
					if(id<0){
					tr += "<tr bgcolor=\"" + colorarr[i % 2] + "\">";
					tr += "<td>" + value.STS_DATE + "</td>";
					tr += "<td>" + value.STS_PERIOD + "</td>";
					tr += "<td>" + value.ENGNAME + "</td>";
					tr += "<td>" + value.CELL + "</td>";
					tr += "<td>" + value.CELL_CHINESE_NAME + "</td>";
					tr += "<td>" + value.TCH_INTACT_RATE + "</td>";
					tr += "<td>" + value.DECLARE_CHANNEL_NUMBER + "</td>";
					tr += "<td>" + value.AVAILABLE_CHANNEL_NUMBER + "</td>";
					tr += "<td>" + value.CARRIER_NUMBER + "</td>";
					tr += "<td>" + value.RESOURCE_USE_RATE + "</td>";
					tr += "<td>" + value.TRAFFIC + "</td>";
					tr += "<td>" + value.TRAFFIC_PER_LINE + "</td>";
					tr += "</tr>";
					}else{
						tr += "<tr>";
						tr += "<td>" + value.STS_DATE + "</td>";
						tr += "<td>" + value.STS_PERIOD + "</td>";
						tr += "<td>" + value.ENGNAME + "</td>";
						tr += "<td>" + value.CELL + "</td>";
						for(var i=0;i<form_tab_1_selectedtabfileds.length;i++){
							
							var filed=form_tab_1_selectedtabfileds[i];
							tr += "<td>" + value[filed] + "</td>";
						}
						tr += "</tr>";
					}
				}
					//table.append(tr);
					//i++;
				});
				table.append(tr);
			}
		}

	}
	// 表头
	function getTableHead(formid,rptselectedid) {
		//console.log("来了");
		var id=$("#"+formid+" #"+rptselectedid).find("option:selected").val();
		if(id<0){
		var str = defaulttemplate;
		}else{
			if(formid=="form_tab_0"){
				var str=form_tab_0_selectedtemplate;
			}else{
				var str=form_tab_1_selectedtemplate;
			}
		}
		/*var str = '<th style="width: 8%">DATE</th>';
		str += '<th style="width: 10%">PERIOD</th>';
		str += '<th style="width: 8%">BSC</th>';
		str += '<th style="width: 8%">CELL</th>';
		str += '<th style="width: 8%">小区</th>';
		str += '<th style="width: 10%">tch信道完好率</th>';
		str += '<th style="width: 10%">定义信道数</th>';
		str += '<th style="width: 10%">可用信道数</th>';
		str += '<th style="width: 10%">载波数</th>';
		str += '<th style="width: 10%">无线资源利用率</th>';
		str += '<th style="width: 10%">话务量</th>';
		str += '<th style="width: 10%">每线话务量</th>';*/
		return str;

	}
	function getTableHead2() {
		//console.log("来了");
		var str = '<th style="width: 8%">STATIC_TIME(指标日期)</th>';
		str += '<th style="width: 10%">GRADE(类别)</th>';
		str += '<th style="width: 8%">AREA_NAME(区域)</th>';
		str += '<th style="width: 8%">SCORE(得分)</th>';
		str += '<th style="width: 8%">INDEX_CLASS(指标类别)</th>';
		str += '<th style="width: 10%">INDEX_NAME(指标名称)</th>';
		str += '<th style="width: 10%">INDEX_VALUE(指标值)</th>';
		return str;

	}
	// 跳转
	function showListViewByPage(dir, formId, tabFlag) {
		var pageSize = new Number($("#" + tabFlag + "hiddenPageSize").val());
		var currentPage = new Number($("#" + tabFlag + "hiddenCurrentPage")
				.val());
		var totalPageCnt = new Number($("#" + tabFlag + "hiddenTotalPageCnt")
				.val());
		var totalCnt = new Number($("#" + tabFlag + "hiddenTotalCnt").val());

		if (dir === "first") {
			if (currentPage <= 1) {
				return;
			} else {
				$("#" + tabFlag + "hiddenCurrentPage").val("1");
			}
		} else if (dir === "last") {
			if (currentPage >= totalPageCnt) {
				return;
			} else {
				$("#" + tabFlag + "hiddenCurrentPage").val(totalPageCnt);
			}
		} else if (dir === "back") {
			if (currentPage <= 1) {
				return;
			} else {
				$("#" + tabFlag + "hiddenCurrentPage").val(currentPage - 1);
			}
		} else if (dir === "next") {
			if (currentPage >= totalPageCnt) {
				return;
			} else {
				$("#" + tabFlag + "hiddenCurrentPage").val(currentPage + 1);
			}
		} else if (dir === "num") {
			var userinput = $("#" + tabFlag + "showCurrentPage").val();
			if (isNaN(userinput)) {
				alert("请输入数字！")
				return;
			}
			if (userinput > totalPageCnt || userinput < 1) {
				alert("输入页面范围不在范围内！");
				return;
			}
			$("#" + tabFlag + "hiddenCurrentPage").val(userinput);
		} else {
			return;
		}
		//获取资源
		getStsData(formId, tabFlag);
	}
	/**
	 *小区 英文名 中文名 输入查询条件input onfocus事件
	 */
	function cellInputFocus(me) {
		var val = $(me).val();
		if ($(me).attr("class") == "encell") {
			if (val == "小区英文名") {
				$(me).val("");
			}
		} else {
			if (val == "小区中文名") {
				$(me).val("");
			}
		}
		$(me).css("color", "");
	}

	/**
	 *小区 英文名 中文名 输入查询条件input onblur事件
	 */
	function cellInputBlur(me) {
		var val = $(me).val();
		if ($(me).attr("class") == "encell") {
			if ($.trim(val) == "") {
				$(me).val("小区英文名").css("color", "grey");
			}
		} else {
			if ($.trim(val) == "") {
				$(me).val("小区中文名").css("color", "grey");
			}
		}

	}
	/**
	 *触发省市区域联动事件
	 */
	function loadevent() {
		getSubAreas("#form_tab_0 #provinceId", "#form_tab_0 #cityId","#form_tab_0 #areaId");
		getSubAreas("#form_tab_1 #provinceId", "#form_tab_1 #cityId","#form_tab_1 #areaId");
		getSubAreas("#form_tab_2 #provinceId", "#form_tab_2 #cityId", null);
	}
</script>
</head>

<body style="min-width: 1024px">
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb">话务</em>数据,请稍侯...
		</h4>
	</div>
	<div class="div_left_main">
		<div class="div_left_content">
			<%-- <div class="div_left_top">话务性能查询</div> --%>
			<div style="padding:10px">
				<div id="frame" style="border:1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">小区语音业务指标</li>
							<li>小区数据业务指标</li>
							<li>城市网络质量指标</li>

						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0">
							<div>
								<form id="form_tab_0" method="post">
									<input type="hidden" name="searchType" value="CELL_VIDEO" /> <input
										type="hidden" id="tab_0_hiddenPageSize" name="page.pageSize"
										value="25" /> <input type="hidden"
										id="tab_0_hiddenCurrentPage" name="page.currentPage" value="1" />
									<input type="hidden" id="tab_0_hiddenTotalPageCnt" /> <input
										type="hidden" id="tab_0_hiddenTotalCnt" />
									<table class="main-table1 half-width" style="padding-top:10px">

										<tr>
										<td class="menuTd" style="text-align:center">统计模板[<a href="initRnoReportTemplateManagePageAction">管理模板</a>]</td>
										<td class="menuTd" style="text-align:center">地市</td>
											<td class="menuTd" style="text-align:center">日期</td>
											<td class="menuTd" style="text-align:center">时段</td>
											<td class="menuTd" style="text-align:center">BSC</td>
											<td class="menuTd" style="text-align:center">小区名称</td>
											<td class="menuTd" style="text-align:center">汇总方式</td>
										</tr>
										<tr>
										<td>
										 <select name="rptTemplateId" id="rptTemplateId" >
										    <%-- <option value="-1">默认模板</option> --%>
										 </select>
										</td>
										<td>省：<select name="provinceId" class="required"
												id="provinceId">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="zoneProvinceLists" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />市：<select name="cityId" class="required" id="cityId">
													<%-- option value="-1">请选择</option --%>
											</select> <br />区：<select name="queryCondition.areaId"
												class="required" id="areaId">
													<%-- option value="-1">请选择</option --%>
											</select></td>
											<td style="text-align:left"><s:set name="todayDay"
													value="new java.util.Date()" /> <%-- input name="queryCondition.stsDate" value="<s:date name="todayDay" format="yyyy-MM-dd" />" type="text" style="width:90%" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/ --%>
												始: <input id="beginTime" class="Wdate required input-text"
												type="text"
												value="<s:date name="todayDay" format="yyyy-MM-dd" />"
												readonly=""
												onfocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"
												name="queryCondition.beginTime"> <br />止: <input
													id="latestAllowedTime" class="Wdate required input-text"
													type="text" readonly=""
													onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}'})"
													name="queryCondition.latestAllowedTime"></td>

											<td><select name="queryCondition.stsPeriod">
													<option value="">全部</option>
													<option value="0000-0100">0000-0100</option>
													<option value="0100-0200">0100-0200</option>
													<option value="0200-0300">0200-0300</option>
													<option value="0300-0400">0300-0400</option>
													<option value="0400-0500">0400-0500</option>
													<option value="0500-0600">0500-0600</option>
													<option value="0600-0700">0600-0700</option>
													<option value="0700-0800">0700-0800</option>
													<option value="0800-0900">0800-0900</option>
													<option value="0900-1000">0900-1000</option>
													<option value="1000-1100">1000-1100</option>
													<option value="1100-1200">1100-1200</option>
													<option value="1200-1300">1200-1300</option>
													<option value="1300-1400">1300-1400</option>
													<option value="1400-1500">1400-1500</option>
													<option value="1500-1600">1500-1600</option>
													<option value="1600-1700">1600-1700</option>
													<option value="1700-1800">1700-1800</option>
													<option value="1800-1900">1800-1900</option>
													<option value="1900-2000">1900-2000</option>
													<option value="2000-2100">2000-2100</option>
													<option value="2100-2200">2100-2200</option>
													<option value="2200-2300">2200-2300</option>
													<option value="2300-0000">2300-0000</option>
											</select></td>
											
											<td style="text-align:left"><input type="text"
												name="queryCondition.engName" /></td>

											<td style="text-align:left"><input value="小区英文名"
												title="小区英文名" type="text" style="color:grey" class="encell"
												onfocus="cellInputFocus(this)" onblur="cellInputBlur(this)" />
												<br />
											<input name="queryCondition.cell" value="" type="hidden" />
												<input value="小区中文名" title="小区中文名" type="text"
												style="color:grey" class="chcell"
												onfocus="cellInputFocus(this)" onblur="cellInputBlur(this)" />
												<input name="queryCondition.cellChineseName" value=""
												type="hidden" /></td>
											<td><select id="audioStsType" name="queryCondition.stsType">
													<option value="default">不汇总</option>
													<option value="sum">单天求和</option>
													<option value="avg">单天平均</option>
													<option value="max">单天最大值</option>
													<option value="min">单天最小值</option>
												</select>
											</td>
										</tr>
										
										<tr>
											<td style="text-align:right" colspan="7"><input
												type="submit" onclick="" value="查  询" style="width:90px;"
												name="search" /></td>
										</tr>
									</table>
								</form>
							</div>
							<%--查询结果  --%>
							<div style="padding-top:10px">
								<table width="100%">
									<tr>
										<td style="width:80%">
											<p>
												<font style="font-weight:bold">话务性能指标：</font>
											</p>
										</td>
										<td style="text-align:right"><input type="button"
											onclick="exportData('form_tab_0')" value="导出数据"
											style="width:120px;" name="load" /></td>

									</tr>

								</table>
								<form id="export_form_tab_0" method="post"
									action="exportRnoStsListAction">
									<input type="hidden" name="searchType" value="CELL_VIDEO" />
								</form>
								<%-- 导出话务数据form --%>
							</div>
							<div style="padding-top:10px;overflow:auto">
								<form id="form1" name="form1" method="post">
									<table id="tab_0_queryResultTab" class="greystyle-standard"
										width="100%">
										<thead>
											<%-- <th style="width: 8%">DATE</th>
											<th style="width: 10%">PERIOD</th>
											<th style="width: 8%">BSC</th>
											<th style="width: 8%">CELL</th>
											<th style="width: 8%">小区</th>
											<th style="width: 10%">tch信道完好率</th>
											<th style="width: 10%">定义信道数</th>
											<th style="width: 10%">可用信道数</th>
											<th style="width: 10%">载波数</th>
											<th style="width: 10%">无线资源利用率</th>
											<th style="width: 10%">话务量</th>
											<th style="width: 10%">每线话务量</th> --%>
										</thead>
									</table>
								</form>
							</div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="tab_0_emTotalCnt"
									class="blue">0</em> 条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first','form_tab_0','tab_0_')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back','form_tab_0','tab_0_')"></a>
								第 <input type="text" id="tab_0_showCurrentPage"
									class="paging_input_text" value="0" /> 页/<em
									id="tab_0_emTotalPageCnt">0</em>页 <a
									class="paging_link page-go" title="GO"
									onclick="showListViewByPage('num','form_tab_0','tab_0_')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next','form_tab_0','tab_0_')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last','form_tab_0','tab_0_')"></a>
							</div>
						</div>
						<div id="div_tab_1" style="display:none;">
							<div>
								<form id="form_tab_1" method="post">
									<input type="hidden" name="searchType" value="CELL_DATA" /> <input
										type="hidden" id="tab_1_hiddenPageSize" name="page.pageSize"
										value="25" /> <input type="hidden"
										id="tab_1_hiddenCurrentPage" name="page.currentPage" value="1" />
									<input type="hidden" id="tab_1_hiddenTotalPageCnt" /> <input
										type="hidden" id="tab_1_hiddenTotalCnt" />
									<table class="main-table1 half-width"
										style="padding-top:10px">


										<tr>
										    <td class="menuTd" style="text-align:center">统计模板[<a href="initRnoReportTemplateManagePageAction">管理模板</a>]</td>
										    <td class="menuTd" style="text-align:center">地市</td>
											<td class="menuTd" style="text-align:center">日期</td>
											<td class="menuTd" style="text-align:center">时段</td>
											<td class="menuTd" style="text-align:center">BSC</td>
											<td class="menuTd" style="text-align:center">小区名称</td>
											<td class="menuTd" style="text-align:center">汇总方式</td>
										</tr>


										<tr>
										<td>
										
										 <select name="rptTemplateId" id="rptTemplateId2">
										     <%-- <option value="-1">默认模板</option> --%>
										 </select>
										</td>
										   <td>省：<select name="provinceId" class="required"
												id="provinceId" >
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="zoneProvinceLists" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />市：<select name="cityId" class="required" id="cityId"
												>
													<%-- option value="-1">请选择</option --%>
											</select> <br />区：<select name="queryCondition.areaId"
												class="required" id="areaId" >
													<%-- option value="-1">请选择</option --%>
											</select></td>
											<td style="text-align:left"><s:set name="todayDay"
													value="new java.util.Date()" /> <%-- input name="queryCondition.stsDate" value="<s:date name="todayDay" format="yyyy-MM-dd" />" type="text" style="width:90%" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/ --%>
												始: <input id="beginTime" class="Wdate required input-text"
												type="text"
												value="<s:date name="todayDay" format="yyyy-MM-dd" />"
												readonly=""
												onfocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"
												name="queryCondition.beginTime"><br/>止: <input
												id="latestAllowedTime" class="Wdate required input-text"
												type="text" readonly=""
												onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}'})"
												name="queryCondition.latestAllowedTime"></td>
											<td style="text-align:left"><select
												name="queryCondition.stsPeriod">
													<option value="">全部</option>
													<option value="0000-0100">0000-0100</option>
													<option value="0100-0200">0100-0200</option>
													<option value="0200-0300">0200-0300</option>
													<option value="0300-0400">0300-0400</option>
													<option value="0400-0500">0400-0500</option>
													<option value="0500-0600">0500-0600</option>
													<option value="0600-0700">0600-0700</option>
													<option value="0700-0800">0700-0800</option>
													<option value="0800-0900">0800-0900</option>
													<option value="0900-1000">0900-1000</option>
													<option value="1000-1100">1000-1100</option>
													<option value="1100-1200">1100-1200</option>
													<option value="1200-1300">1200-1300</option>
													<option value="1300-1400">1300-1400</option>
													<option value="1400-1500">1400-1500</option>
													<option value="1500-1600">1500-1600</option>
													<option value="1600-1700">1600-1700</option>
													<option value="1700-1800">1700-1800</option>
													<option value="1800-1900">1800-1900</option>
													<option value="1900-2000">1900-2000</option>
													<option value="2000-2100">2000-2100</option>
													<option value="2100-2200">2100-2200</option>
													<option value="2200-2300">2200-2300</option>
													<option value="2300-0000">2300-0000</option>
											</select></td>

											

											<td style="text-align:left"><input type="text"
												name="queryCondition.engName" /></td>
											<td style="text-align:left"><input value="小区英文名"
												title="小区英文名" type="text" style="color:grey"
												class="encell" onfocus="cellInputFocus(this)"
												onblur="cellInputBlur(this)" /> <input
												name="queryCondition.cell" value="" type="hidden" /><br />
												<input value="小区中文名" title="小区中文名" type="text"
												style="color:grey" class="chcell"
												onfocus="cellInputFocus(this)" onblur="cellInputBlur(this)" />
												<input name="queryCondition.cellChineseName" value=""
												type="hidden" /></td>
											<td><select id="dataStsType" name="queryCondition.stsType">
													<option value="default">不汇总</option>
													<option value="sum">单天求和</option>
													<option value="avg">单天平均</option>
													<option value="max">单天最大值</option>
													<option value="min">单天最小值</option>
												</select>
											</td>
										</tr>
										<tr>
											<td style="text-align:right" colspan="7"><input
												type="submit" onclick="" value="查  询" style="width:90px;"
												name="search" /></td>
										</tr>
									</table>
								</form>

							</div>
							<%--查询结果  --%>
							<div style="padding-top:10px">
								<table width="100%">
									<tr>
										<td style="width:80%">
											<p>
												<font style="font-weight:bold">话务性能指标：</font>
											</p>
										</td>
										<td style="text-align:right"><input type="button"
											onclick="exportData('form_tab_1')" value="导出数据"
											style="width:120px;" name="load" /></td>

									</tr>

								</table>
								<form id="export_form_tab_1" method="post"
									action="exportRnoStsListAction">
									<input type="hidden" name="searchType" value="CELL_DATA" />
								</form>
								<%-- 导出话务数据form --%>
							</div>
							<div style="padding-top:10px;overflow:auto">
								<form id="form1" name="form1" method="post">
									<table id="tab_1_queryResultTab" class="greystyle-standard"
										width="100%">
										<thead>
											<%-- <th style="width: 8%">DATE</th>
											<th style="width: 10%">PERIOD</th>
											<th style="width: 8%">BSC</th>
											<th style="width: 8%">CELL</th>
											<th style="width: 8%">小区</th>
											<th style="width: 10%">tch信道完好率</th>
											<th style="width: 10%">定义信道数</th>
											<th style="width: 10%">可用信道数</th>
											<th style="width: 10%">载波数</th>
											<th style="width: 10%">无线资源利用率</th>
											<th style="width: 10%">话务量</th>
											<th style="width: 10%">每线话务量</th> --%>
										</thead>
									</table>
								</form>
							</div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="tab_1_emTotalCnt"
									class="blue">0</em> 条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first','form_tab_1','tab_1_')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back','form_tab_1','tab_1_')"></a>
								第 <input type="text" id="tab_1_showCurrentPage"
									class="paging_input_text" value="0" /> 页/<em
									id="tab_1_emTotalPageCnt">0</em>页 <a
									class="paging_link page-go" title="GO"
									onclick="showListViewByPage('num','form_tab_1','tab_1_')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next','form_tab_1','tab_1_')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last','form_tab_1','tab_1_')"></a>
							</div>


						</div>
						<div id="div_tab_2" style="display:none">
							<div>
								<form id="form_tab_2" method="post">
									<input type="hidden" name="searchType" value="CITY_QUALITY" />
									<input type="hidden" id="tab_2_hiddenPageSize"
										name="page.pageSize" value="25" /> <input type="hidden"
										id="tab_2_hiddenCurrentPage" name="page.currentPage" value="1" />
									<input type="hidden" id="tab_2_hiddenTotalPageCnt" /> <input
										type="hidden" id="tab_2_hiddenTotalCnt" />
									<table class="main-table1 half-width"
										style="padding-top:10px;width:50%;margin-left:0">
										
										<tr>
										<td  class="menuTd" style="text-align:center;">类别</td>
										<td  class="menuTd" style="text-align:center;">地市</td>
										<td  class="menuTd" style="text-align:center;">日期</td>
										</tr>
										<tr>
											
											<td style="text-align:left"><select
												name="queryCondition.grade">
													<option value="">全部</option>
													<option value="一类">一类</option>
													<option value="二类">二类</option>
													<option value="三类">三类</option>
											</select></td>
											
										   <td>省：<select
													name="provinceId" class="required" id="provinceId"
													>
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="zoneProvinceLists" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select><br/>市：<select name="queryCondition.cityId"
													class="required" id="cityId" >
														<%-- option value="-1">请选择</option --%>
												</select> <%-- 区：<select name="queryCondition.areaId" class="required" id="areaId">
										</select> --%></td>
											
											<td style="text-align:left">
												<%-- input name="queryCondition.staticTime" value="" type="text" style="width:90%" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/ --%>
												始:<input type="text" id="beginTime"
												name="queryCondition.beginTime"
												onFocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"
												readonly class="Wdate required input-text"
												value="<%=dateFormat.format(new Date())%>" /> <br/>止:<input
												type="text" id="latestAllowedTime"
												name="queryCondition.latestAllowedTime"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}'})"
												readonly class="Wdate required input-text" />

											</td>
										</tr>
										<tr>
											<td style="text-align:right" colspan="3"><input
												type="submit" onclick="" value="查  询" style="width:90px;"
												name="search" /></td>
										</tr>
									</table>
								</form>
							</div>
							<%--查询结果  --%>
							<div style="padding-top:10px">
								<table width="100%">
									<tr>
										<td style="width:20%">
											<p>
												<font style="font-weight:bold">话务性能指标：</font>
											</p>
										</td>


									</tr>

								</table>

							</div>
							<div style="padding-top:10px">
								<form id="form1" name="form1" method="post">
									<table id="tab_2_queryResultTab" class="greystyle-standard"
										width="100%">
										<thead>
											<th style="width: 8%">STATIC_TIME(指标日期)</th>
											<th style="width: 10%">GRADE(类别)</th>
											<th style="width: 8%">AREA_NAME(区域)</th>
											<th style="width: 8%">SCORE(得分)</th>
											<th style="width: 8%">INDEX_CLASS(指标类别)</th>
											<th style="width: 10%">INDEX_NAME(指标名称)</th>
											<th style="width: 10%">INDEX_VALUE(指标值)</th>
										</thead>
									</table>
								</form>
							</div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="tab_2_emTotalCnt"
									class="blue">0</em> 条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first','form_tab_2','tab_2_')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back','form_tab_2','tab_2_')"></a>
								第 <input type="text" id="tab_2_showCurrentPage"
									class="paging_input_text" value="0" /> 页/<em
									id="tab_2_emTotalPageCnt">0</em>页 <a
									class="paging_link page-go" title="GO"
									onclick="showListViewByPage('num','form_tab_2','tab_2_')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next','form_tab_2','tab_2_')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last','form_tab_2','tab_2_')"></a>
							</div>
						</div>

					</div>
				</div>

			</div>
		</div>
	</div>
	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
</body>
</html>
