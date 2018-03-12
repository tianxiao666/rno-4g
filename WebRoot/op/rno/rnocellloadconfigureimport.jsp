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

<title>加载小区配置</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/cellLoadConfigureImport.js"></script>
<script type="text/javascript" src="js/cellLoadConfigureImportAdd.js"></script>
<%-- 
<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab1", "li", "onclick");//项目服务范围类别切换

	})
	window.onchange=function(){
	//getSysValue();getTempValue();
	}
	$(function(){
	//刷新按钮
	$("#refreshLoadedBtn").click();
	$("#queryCellConfigureBtn").click(function(){
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
		getCellConfigureData("form_tab_2","tab_2_");
		return false;
	});
	//}
	//});
	
	/**
 * 获取小区
 */
function getCellConfigureData(formId,tabFlag) {
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
				url : 'queryCellConfigureDescListByPageAction',
				dataType : 'json',
				success : function(data) {
					//判断是否能获取console
					if(window.console) {
						console.log(data);
					}
					showSts(data,tabFlag,formId);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
		}
function showSts(data,tabFlag,formId){
	var page = data.newPage;
	var hodescLists = data.celldescLists;
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
			tr += "<td>" + value.AREANAME + value.CREATE_TIME +"</td>";
			tr += "<td>" + value.NAME + "</td>";
			tr += "<td>" + value.CREATE_TIME + "</td>";
			tr += "<td><input type='checkbox' id='loaditem' name='loaditem' value='"+value.CELL_DESCRIPTOR_ID+"'/></td>";
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
	var str = '<th style="width: 10%">标题</th>';
	str += '<th style="width: 8%">方案名称</th>';
	str += '<th style="width: 8%">上传时间</th>';
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
	getCellConfigureData(formId,tabFlag);
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
				url : 'addCellConfigDescToAnalysisListForAjaxAction',
				dataType : 'text',
				type : 'post',
				data:{configIds:senddata},
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
</script> --%>
</head>

<body onload="getSysCoufigureAndTempAnalyse();">
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb"></em>资源,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%-- <div class="div_left_top">加载小区配置</div> --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd;overflow: hidden;float: left;width: 60%">
					<div id="div_tab1" class="div_tab divtab_menu">
						<ul>
							<li class="selected" style="width:119px" id="div_tab_0_li">小区配置导入及加载</li>
							<li style="width:119px" id="div_tab_1_li">从系统中加载</li>
						</ul>
					</div>

					<div class="divtab_content" style="float: left;width: 100%">
						<%-- div_tab_0_con --%>
						<div id="div_tab1_0">
							<form id="formImportCell" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> <input
									type="hidden" name="systemConfig" value="true" />
									<input type="hidden" name="update" value="true" />
								<div>
									<table class="main-table1 half-width">
										<tbody>
											<tr>
												<td class="menuTd" style="width: 20%">所属地市： <br />
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
												</select> 市：<select name="areaId" class="required" id="cityId">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												<%--
												 区：<select name="areaId" class="required" id="areaId">
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												 --%>
												<br />
												</td>


											</tr>
											<tr>
												<td class="menuTd" style="width: 20%">网络制式： <br />
												</td>
												<td><input type="radio" name="fileCode" id="3"
													value="GSMPLANCELLFILE"
													class="canclear required"/> <label for="3">
														GSM </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="32"
													value="TDPLANCELLFILE"
													class="canclear  required" /> <label for="32">
														TD </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="32"
													value="WLANPLANCELLFILE"
													class="canclear  required" /> <label for="32">
														WLAN </label> <br /></td>
												
											</tr>
											<tr>
												<td class="menuTd" style="width: 20%">小区配置文件(Excel)：<br />
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width:44%;" name="file"
													class="canclear  required" /> &nbsp; <a href="javascript:void(0);" title="点击下载模板" id="downloadHref">&nbsp;</a>
												</td>
											</tr>
											<tr>
												<td class="menuTd" style="width: 20%">存储方式： <br />
												</td>
												<td><input type="radio" name="attachParams['isTemp']" id="tempanalyse"
													value="true" class="canclear  required" checked="checked"/> <label for="3">
														临时分析，命名为</label><input type="text" name="attachParams['tempname']" id="tempname"/>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="attachParams['isTemp']" id="32" value="false"
													class="canclear  required" /> <label for="32"> 覆盖系统配置
												</label> <br /></td>
											</tr>
										</tbody>


									</table>
								</div>
								<div class="container-bottom" style="padding-top: 10px">
									<table style="width: 60%; margin: auto" align="center">
										<tr>
											<td>
											<input type="hidden" name="autoload" id="autoload"/>
											<input type="button" value="导 入，并加载到分析列表"
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
						<div id="div_tab1_1" style="display:none;">
							<div style="float: left;width: 100%">
							<div>
							<form id="form_tab_2" method="post">
							       <input id="tab_2_hiddenPageSize" type="hidden" value="25" name="page.pageSize"/>
									<input id="tab_2_hiddenCurrentPage" type="hidden" value="1" name="page.currentPage"/>
									<input id="tab_2_hiddenTotalPageCnt" type="hidden" value="0"/>
									<input id="tab_2_hiddenTotalCnt" type="hidden" value="0"/>
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
											</select> 区：<select name="areaId" class="required" id="areaId2" onchange="getSysCoufigureAndTempAnalyse();">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
													<option value="-1">全部</option>
											</select>

											</td>


										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">网络制式： <br />
											</td>
											<td><input type="radio" name="searchType" id="3"
												value="GSM" class="canclear required" checked="checked" />
												<label for="3"> GSM </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="searchType" id="32"
												value="TD" class="canclear  required" />
												<label for="32"> TD </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="searchType" id="32"
												value="WLAN" class="canclear  required" />
												<label for="32"> WLAN </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												 <br /></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">小区配置方案：<br />
											</td>
											<td style="font-weight: normal;" colspan="0">
											<input type="radio" name="sysDefault" id="systemradio"
												value="true" class="canclear required" checked="checked" />
												<label for="3"> 系统配置方案 </label><select name="scheme" id="systemconfigure" onchange="getSysValue();">
												
												</select>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="sysDefault" id="tempradio"
												value="false" class="canclear  required" />
												<label for="32"> 临时分析方案 </label><%-- select name="scheme" id="tempanalyse" onchange="getTempValue();"></select --%>
											</td>
										</tr>
										<tr><td class="menuTd">方案名称：</td><td><input type="text" name="schemeName" id="schemeName" class="required" value=""/></td></tr>
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
												id="queryCellConfigureBtn" name="querybtn" /> </td>
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
									<font style="font-weight:bold">小区描述配置指标</font>
									</p>
									<span style="display:block;margin-top:4px;"><input type="button" id="loadtoanalysisBtn" value="添加到分析列表" name="queryAndLoadBtn" /></span>
									</td>
									</tr>
									</tbody>
									</table>
                            <form id="form1" name="form1" method="post">
                            	<table id="tab_2_queryResultTab" class="greystyle-standard"  width="100%">
									<thead >
									   <tr>
                                	 <th style="width: 8%">标题</th>
									<th style="width: 10%">方案名称</th>
									<th style="width: 8%">上传时间</th>
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
							

				</div>
				<div style="float: left;margin-left: 4px;width: 38%;border: 1px solid #ddd">
							<div style="padding-top: 2px">
								<table width="100%">
									<tr>
										<td style="width: 50%">
											<p>
												<font style="font-weight: bold">加载到分析列表的小区配置</font>
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
									<th>标题</th>
									<th>方案名称</th>
									<th>上传时间</th>
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
            	<table height="100%" width="100%" style="text-align:center">
                	<tr>
                    	<td>
                        	<span id="operTip"></span>
                        </td>
                    </tr>
                </table>
             
            </div>
</body>
</html>