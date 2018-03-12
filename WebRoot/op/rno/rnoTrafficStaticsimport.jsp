<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<title>加载话务性能</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/trafficStaticsImport.js"></script>
<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

	})
		$(document).ready(function(){
			//刷新按钮
			$("#refreshLoadedBtn").click();
			$("#radiohide").click(function(){
				$("#importAndLoadBtn").hide();
			});
			$("#radioshow1").click(function(){
				$("#importAndLoadBtn").show();
			});
			$("#radioshow2").click(function(){
				$("#importAndLoadBtn").show();
			});
	$("#queryTrafficStaticBtn").click(function(){
			// 查询提交
			//alert("1");
	$("#form_tab_2").submit();
		//return true;
		
	});
	//校验
	//$("#form_tab_2").validate({
		//submitHandler : function(form) {
	//ajaxSubmit,submit
	$("#form_tab_2").submit(function() {
	     //alert("2");
	 	 //重新初始化分页参数
	     $("#tab_2_hiddenPageSize").val("25");
         $("#tab_2_hiddenCurrentPage").val("1");
         $("#tab_2_hiddenTotalPageCnt").val("0");
         $("#tab_2_hiddenTotalCnt").val("0");
		getTrafficStaticsData("form_tab_2","tab_2_");
		return false;
	});
	//}
	//});
	
	/**
 * 获取小区
 */
function getTrafficStaticsData(formId,tabFlag) {
	$(".loading_cover").css("display", "block");
	/*
	var stsusl="";
	if(formId=="form_tab_2"){
				stsusl="queryRnoCityNetQualityIndexByPageAction";
				}else{
				stsusl="queryRnoStsListByPageAction";
				}
				//console.log(stsusl);*/
				
	$("#"+formId).ajaxSubmit({
				//url : "'"+stsusl+"'",
				url : 'queryCellAudioOrDataDescListByPageAction',
				dataType : 'json',
				success : function(data) {
					//console.log(data);
					showSts(data,tabFlag,formId);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
		}
function showSts(data,tabFlag,formId){
	var page = data.newPage;
	var hodescLists = data.cellaudioordatadescLists;
	var colorarr=["#96C4EB","#DBEBF8"];
	//console.log("page===" + page);
	if (page) {
		var pageSize = page.pageSize ? page.pageSize : 0;
		$("#"+tabFlag+"hiddenPageSize").val(pageSize);

		var currentPage = page.currentPage && pageSize!=0 ? page.currentPage : 1;
		$("#"+tabFlag+"hiddenCurrentPage").val(currentPage);

		var totalPageCnt = page.totalPageCnt ? page.totalPageCnt : 0;
		$("#"+tabFlag+"hiddenTotalPageCnt").val(totalPageCnt);

		var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
		$("#"+tabFlag+"hiddenTotalCnt").val(totalCnt);

		// 跳转
		$("#"+tabFlag+"emTotalCnt").html(totalCnt);
		$("#"+tabFlag+"showCurrentPage").val(currentPage);
		$("#"+tabFlag+"emTotalPageCnt").html(totalPageCnt);

	}
		var table = $("#"+tabFlag+"queryResultTab");
	// 只保留表头
	//table.empty();
	//table.append(getTableHead());
	var i=0;
	if(formId=="form_tab_2"){
	// 只保留表头
	table.empty();
	table.append(getTableHead());
			if (hodescLists) {
		var tr="";
		//var STS_DATE="<s:date name='1999/02/06' format='yyyy-MM-dd'/>";
		//var helloworld = "<s\:text name=\"<s\:date name=\"1999/02/06\" format=\"yyyy-MM-dd\"/>\"/>";
		$.each(hodescLists,function(key,value){
		//alert(new Date(value.STS_DATE));
		    //console.log(i+":"+colorarr[i%2]);
		    //class=\"greystyle-standard-whitetr\"
			tr += "<tr  style=\"background-color:"+colorarr[i%2]+"\">";
			tr += "<td>" + value.AREANAME + "</td>";
			tr += "<td>" + value.NET_TYPE+(value.SPEC_TYPE=='CELLAUDIOINDEX'?'小区语音业务指标':'小区数据业务指标') + "</td>";
			tr += "<td>" + value.STS_DATE + "</td>";
			tr += "<td><input type='checkbox' id='loaditem' name='loaditem' value='"+value.STS_DESC_ID+"'/></td>";
			tr += "</tr>";
			//table.append(tr);
			//console.log(tr);
			i++;
		})
		table.append(tr);
	 }
	}
	}
function getTableHead() {
	//console.log("来了");
	var str = '<th style="width: 10%">区域</th>';
	str += '<th style="width: 8%">指标类型</th>';
	str += '<th style="width: 8%">话统日期</th>';
	str += '<th style="width: 10%">加载项</th>';
	return str;

}
// 跳转
function showListViewByPage(dir,formId,tabFlag) {
	var pageSize =new Number($("#"+tabFlag+"hiddenPageSize").val());
	var currentPage = new Number($("#"+tabFlag+"hiddenCurrentPage").val());
	var totalPageCnt =new Number($("#"+tabFlag+"hiddenTotalPageCnt").val());
	var totalCnt = new Number($("#"+tabFlag+"hiddenTotalCnt").val());

	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $("#"+tabFlag+"showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$("#"+tabFlag+"hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	//获取资源
	getTrafficStaticsData(formId,tabFlag);
}

$("#appendlist").click(function(){
var s='';
var items=$('input[name="loaditem"]:checked');
var len=items.length;
items.each(function(i){
	if(i==len-1){
	s+=$(this).val();
	}else{
    s+=$(this).val()+',';
    }
});
//console.log(s==''?'你还没有选择任何内容！':s);
if(s==''){alert("你还没有选择任何内容！");return;}
var senddata={loaditem:s};
console.log(senddata);
	$.ajax({
				//url : "'"+stsusl+"'",
				url : 'appendRnoHandOverDescListForAjaxAction',
				dataType : 'text',
				type : 'post',
				data:senddata,
				success : function(data) {
					var res=eval("("+data+")");
					alert(res[0]['res']);
					
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
				
		});
		$("#loadtoanalysisBtn").click(function(){
		//console.log("loadtoanalysisBtn入口");
				var s='';
		var items=$('input[name="loaditem"]:checked');
		var len=items.length;
		items.each(function(i){
			if(i==len-1){
			s+=$(this).val();
			}else{
		    s+=$(this).val()+',';
		    }
		});
		//console.log(s==''?'你还没有选择任何内容！':s);
		if(s==''){alert("你还没有选择任何内容！");return;}
		var senddata=s;
		
		//console.log(senddata);
		//return;
	$.ajax({
				//url : "'"+stsusl+"'",
				url : 'addCellAudioOrDataDescToAnalysisListForAjaxAction',
				dataType : 'text',
				type : 'post',
				data:{stsDescIds:senddata},
				success : function(data) {
					var res=eval("("+data+")");
					//alert("写入成功!");
					animateOperTips("添加完成!");
					//刷新按钮
					$("#refreshLoadedBtn").click();
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
		});			
		});
</script>
</head>

<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb"></em>资源,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%-- <div class="div_left_top">加载话务性能</div> --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd;overflow: hidden;width: 61%;float: left">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected" style="width:119px">话统指标导入及加载</li>
							<li style="width:119px">从系统中加载</li>

						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0">
							<form id="formImportCell" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> <input
									type="hidden" name="systemConfig" value="true" />
								<input type="hidden" name="autoload" id="autoload"/>
								<div>
									<table class="main-table1 half-width">
										<tbody>
											<tr>
												<td class="menuTd" >所属地市<span class="txt-impt">*</span>  <br />
												</td>
												<td style="width: 50%; font-weight: normal;" colspan="0">
													省：<select name="provinceId" class="required"
													id="provinceId">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 市：<select name="cityId" class="required" id="cityId">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 区：<select name="areaId" class="required" id="areaId">
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
													</div> <br />
												</td>


											</tr>
											<tr>
												<td class="menuTd" >指标类型<span class="txt-impt">*</span> <br />
												</td>
												<td><input type="radio" name="fileCode" id="radioshow1"
													value="GSMAUDIOTRAFFICSTATICSFILE"
													class="canclear required" /> <label for="3">
														小区语音业务指标 </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="radioshow2"
													value="GSMDATATRAFFICSTATICSFILE"
													class="canclear  required" /> <label for="32">
														小区数据业务指标 </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="radiohide"
													value="GSMCITYNETQUALITYFILE"
													class="canclear  required" /> <label for="32">
														城市网络质量指标 </label> <br /></td>
												
											</tr>
											<tr>
												<td class="menuTd" >指标文件（EXCEL）<span class="txt-impt">*</span> <br />
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width:44%;" name="file"
													class="canclear  required" /> &nbsp; <a href="javascript:void(0);" title="点击下载模板" id="downloadHref">&nbsp;</a>
												</td>
											</tr>
											<tr>
												<td class="menuTd" >重复记录处理方式<span class="txt-impt">*</span> <br />
												</td>
												<td><input type="radio" name="update" id="3"
													value="true" class="canclear  required" /> <label for="3">
														覆盖(更新信息) </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="update" id="32" value="false"
													class="canclear  required" /> <label for="32"> 忽略
												</label> <br /></td>
											</tr>
										</tbody>


									</table>
								</div>
								<div class="container-bottom" style="padding-top: 10px">
									<table style="width: 60%; margin: auto" align="center">
										<tr>
											<td><input type="button" value="导 入，并加载到分析列表"
												id="importAndLoadBtn" name="import" /> <input type="button"
												value="仅   导    入" style="width: 90px;" id="importBtn"
												name="import" /> <br /></td>


										</tr>
									</table>
									<div id="importResultDiv" class="container-bottom"
										style="padding-top: 10px"></div>
									<div id="loadlistDiv">
									<table class="tb-transparent-standard" style="width:100%" id="loadlistTable1">                                                                                                                                     
									</table>
									</div>
								</div>
							</form>
						</div>
						<div id="div_tab_1" style="display:none">
							<div>
							<%-- conditionForm --%>
							<form id="form_tab_2" method="post">
									<input id="tab_2_hiddenPageSize" type="hidden" value="25" name="page.pageSize">
									<input id="tab_2_hiddenCurrentPage" type="hidden" value="1" name="page.currentPage">
									<input id="tab_2_hiddenTotalPageCnt" type="hidden" value="0">
									<input id="tab_2_hiddenTotalCnt" type="hidden" value="0">
								<table class="main-table1 half-width">
									<tbody>
										<tr>
											<td class="menuTd" style="width: 20%">所属地市： <br />
											</td>
											<td style="width: 50%; font-weight: normal;" colspan="0">
												省：<select name="provinceId2" class="required"
												id="provinceId2">
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> 市：<select name="cityId2" class="required" id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> 区：<select name="queryCondition.areaId" class="required" id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>

											</td>


										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">指标类型： <br />
											</td>
											<td><input type="radio" name="searchType" id="3"
												value="CELLAUDIOINDEX" class="canclear required" checked=true />
												<label for="3"> 小区语音业务指标 </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="searchType" id="32"
												value="CELLDATAINDEX" class="canclear  required" />
												<label for="32"> 小区数据业务指标 </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												 <br /></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">日期/时段：<br />
											</td>
											<td style="font-weight: normal;" colspan="0">
											<s:set name="todayDay" value="new java.util.Date()"/>
											<%-- input name="queryCondition.stsDateStr" value="<s:date name="todayDay" format="yyyy-MM-dd" />" type="text"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate required input-text"/ --%>
											始:
<input id="beginTime" class="Wdate required input-text" type="text" value="<s:date name="todayDay" format="yyyy-MM-dd" />" readonly="readonly" onfocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})" name="queryCondition.beginTime"/>
 止:
<input id="latestAllowedTime" class="Wdate required input-text" type="text" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}'})" name="queryCondition.latestAllowedTime"/>
											<select name="queryCondition.stsPeriod">
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
                                            </select>
											</td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">BSC： <br />
											</td>
											<td>
											<%-- input type="text" style="width:90px;" name="queryCondition.engName"/ --%>
											<select name="queryCondition.engName" style="width:90px;">
											<option value="">全部</option>
											<s:iterator value="bscEngNameLists"  id="obj">
											<s:iterator value="obj">
															<option value="<s:property value='value' />">
																<s:property value="value" />
															</option>
															</s:iterator>
														</s:iterator>
											</select>
											</td>
										</tr>
									</tbody>


								</table>
								</form>
							</div>
							<div class="container-bottom" style="padding-top: 10px">
									<table style="width: 60%; margin: auto" align="center">
										<tr>
											<td><%-- input type="button" value="加载到分析列表"
												id="queryAndLoadBtn" name="querybtn" / --%>
												<input type="button" value="查　　询"
												id="queryTrafficStaticBtn" name="querybtn" />
												 </td>
										</tr>
									</table>
							</div>
							<%--查询结果  --%>
														<div style="padding-top:10px">
							<table width="100%">
									<tbody>
									<tr>
									<td style="width:20%">
									<p>
									<font style="font-weight:bold">话统指标</font>
									</p>
									<span style="display:block;margin-top:4px"><input type="button" id="loadtoanalysisBtn" value="添加到分析列表" name="queryAndLoadBtn" /></span>
									</td>
									</tr>
									</tbody>
									</table>
                            <form id="form1" name="form1" method="post">
                            	<table id="tab_2_queryResultTab" class="greystyle-standard"  width="100%">
									<thead >
									 <tr>
                                	 <th style="width: 8%">区域</th>
									<th style="width: 10%">指标类型</th>
									<th style="width: 8%">话统日期</th>
									<th style="width: 10%">加载项</th>
									</tr>                                                                                             
                                    </thead> 
                                </table>
                                  </form>                              
                            </div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="tab_2_emTotalCnt" class="blue">0</em> 条记录</span>
								<a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first','form_tab_2','tab_2_')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back','form_tab_2','tab_2_')"></a> 第
								<input type="text" id="tab_2_showCurrentPage" class="paging_input_text" value="0" />
								页/<em id="tab_2_emTotalPageCnt">0</em>页
								<a class="paging_link page-go" title="GO" onclick="showListViewByPage('num','form_tab_2','tab_2_')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next','form_tab_2','tab_2_')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last','form_tab_2','tab_2_')"></a>
							</div>
							<div style="padding-top: 10px">
								<table width="100%">
									<tr>
										<td style="width: 20%"></td>
									</tr>
								</table>
							</div>
							<div style="padding-top: 10px;text-align:center">
								<table class="tb-transparent-standard" style="width:100%" id="loadlistTable2">                                                                                                        
								</table>
							</div>
							
						</div>
					</div>

				</div>
				<div style="float: left;margin-left: 4px;width: 38%;border: 1px solid #ddd">
							<div style="padding-top: 2px">
								<table width="100%">
									<tr>
										<td style="width: 50%">
											<p>
												<font style="font-weight: bold">加载到分析列表的话统指标</font>
											</p>
											<span style="display: block;margin-top: 3px"><input
												type="button" id="refreshLoadedBtn" onclick="" value="刷新" style="width: 90px;"
												name="search" /></span>
										</td>
										<td style="width: 50%; text-align: right"><input
											type="button" id="removeFromAnalysis" value="从分析列表删除"
											style="width: 90px;" name="search" /></td>
									</tr>

								</table>

							</div>
							<div style="padding-top: 10px">
								<table id="loadRefreshlistTable3" class="greystyle-standard"
									width="100%">
									<thead>
									<tr>
									<th>区域</th>
									<th>指标类型</th>
									<th>话统日期</th>
									<th style="width: 10%"><input type="checkbox"
										onclick="javascript:operAllCheckbox(this,3);" name="selectall" id="2" />
										<label for="1"></label>
									</th>
									</tr>
									</thead>
								</table>
							</div>
							</div>
			</div>
		</div>
	</div>
	<div id="operInfo" style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
            	<table width="100%" style="text-align:center;height:100%">
                	<tr>
                    	<td>
                        	<span id="operTip"></span>
                        </td>
                    </tr>
                </table>
             
            </div>
</body>
</html>