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
		<link rel="stylesheet" type="text/css" href="../../css/base.css">
		<link rel="stylesheet" type="text/css" href="../../css/input.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css"
			href="../../css/public-div-standard.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table-standard.css" />
		<link rel="stylesheet" type="text/css"
			href="../../jslib/paging/iscreate-paging.css" />
		<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../js/tab.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
		<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
		<script type="text/javascript" src="js/calendar.js"></script>
		<script type="text/javascript" src="js/importrnocityquality.js"></script>
		<script type="text/javascript">
			var $ = jQuery.noConflict();
			$(function(){
				//tab选项卡
				tab("div_tab","li","onclick");//项目服务范围类别切换
			
			})
		  function addTime(){
		  	for(var i=0;i<24;i++){
		  	if(i<10){
		  	if(i+1==10){
		  	$("#addtime").append("<option value='0"+i+"00'>0"+i+"00-"+(i+1)+"00</option>");
		  	}else{
		  	$("#addtime").append("<option value='0"+i+"00'>0"+i+"00-0"+(i+1)+"00</option>");
		  	}
		  	}else{
		  	$("#addtime").append("<option value='"+i+"00'>"+i+"00-"+(i+1)+"00</option>");
		  	}
		  	}
		  }
		$(document).ready(function(){
			
			$("#radiohide").click(function(){
				//console.log("gogo");
				$("#importBtn1").hide();
			});
			$("#radioshow1").click(function(){
				//console.log("gogo");
				$("#importBtn1").show();
			});
			$("#radioshow2").click(function(){
				//console.log("gogo");
				$("#importBtn1").show();
			});
		});
		</script>
	</head>

	<body onload="addTime();">
		<div class="loading_cover" style="display: none">
			<div class="cover"></div>
			<h4 class="loading">
				正在加载
				<em class="loading_fb">小区</em>资源,请稍侯...
			</h4>
		</div>
		<div class="div_left_main" style="width: 100%">
			<div class="div_left_content">
				<div class="div_left_top">
					加载话务性能
				</div>
				<div style="padding: 10px">
					<div id="frame" style="border: 1px solid #ddd">
						<div id="div_tab" class="div_tab divtab_menu">
							<ul>
								<li class="selected">
									话统指标导入加载
								</li>
								<li>
									从系统中加载
								</li>

							</ul>
						</div>

						<div class="divtab_content">
							<div id="div_tab_0">
								<div>
									<form id="conditionForm" method="post" menctype="multipart/form-data">
										<input type="hidden" name="needPersist" value="true" />
										<table class="main-table1 half-width"
											style="width: 100%; padding-top: 10px">
											<tbody id="table1">
												<tr>
													<td class="menuTd" style="width: 20%">
														所属地市：
														<br />
													</td>
													<td style="width: 50%; font-weight: bold" colspan="0">
														<select name="areaId" class="required">
															<s:iterator value="zoneAreaLists" id="onearea">
																<option value="<s:property value='#onearea.area_id' />">
																	<s:property value="#onearea.name" />
																</option>
															</s:iterator>
														</select>
														<br />
													</td>


												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														指标类型：
														<br />
													</td>
													<td>
														
															<input type="radio" name="fileCode" id="radioshow1"
																value="GSMCELLFILE" class="canclear required" />
															<label for="3">
																小区语音业务指标
															</label>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="radio" name="fileCode" id="radioshow2"
																value="TDCELLFILE" class="canclear  required" />
															<label for="32">
																小区数据业务指标
															</label>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="radio" name="fileCode" id="radiohide"
																value="GSMCITYNETQUALITYFILE" class="canclear  required" />
															<label for="32">
																城市网络质量指标
															</label>
														
														<br />
													</td>
												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														指标文件(Excel/文本格式)：
														<br />
													</td>
													<td style="width: 50%; font-weight: bold" colspan="0">
														<input type="file" style="width: 75%;" name="file"
															class="required" id="cityindex"/>
														&nbsp;
														<br />
													</td>
												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														重复记录处理方式：
														<br />
													</td>
													<td>
														<input type="radio" name="update" id="3" value="true"
															class="canclear  required"  />
														<label for="3">
															覆盖(更新为当前指标)
														</label>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="radio" name="update" id="32" value="false"
															class="canclear  required" />
														<label for="32">
															忽略(保持原有指标)
														</label>
														<br />
													</td>
												</tr>
											</tbody>
											

										</table>
									</div>
									<div class="container-bottom" style="padding-top: 10px">
										<table style="width: 60%; margin: auto" align="center">
											<tr>
												<td>
													<input type="button" value="导入，并加载到分析列表" style="width: 150px;"
														id="importBtn1" name="import" />
													<input type="button" value="仅导入" style="width: 90px;"
														id="importBtn2" name="import" />
													<br />
												</td>


											</tr>
										</table>
										</table>
									</form>
								</div>
							</div>
							<div id="div_tab_1" style="display: none;">
								<form id="formImportCell" enctype="multipart/form-data">
									<input type="hidden" name="needPersist" value="true" />
									<input type="hidden" name="systemConfig" value="true" />
									<div>
										<table class="main-table1 half-width">
											<tbody>
												<tr>
													<td class="menuTd" style="width: 20%">
														区域：
														<br />
													</td>
													<td style="width: 50%; font-weight: bold" colspan="0">
														<select name="areaId" class="required">
															<s:iterator value="zoneAreaLists" id="onearea">
																<option value="<s:property value='#onearea.area_id' />">
																	<s:property value="#onearea.name" />
																</option>
															</s:iterator>
														</select>
														<br />
													</td>


												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														指标类型：
														<br />
													</td>
													<td>
														
															<input type="radio" name="fileCode" id="3"
																value="GSMCELLFILE" class="canclear required" />
															<label for="3">
																小区语音业务指标
															</label>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="radio" name="fileCode" id="32"
																value="TDCELLFILE" class="canclear  required" />
															<label for="32">
																小区数据业务指标
															</label>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="radio" name="fileCode" id="32"
																value="WLANCELLFILE" class="canclear  required" />
															<label for="32">
																城市网络质量指标
															</label>
														
														<br />
													</td>
												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														日期 / 时段：
														<br />
													</td>
													<td style="width: 50%; font-weight: bold" colspan="0">
														<input type="text"  name="date"
															class="canclear  required" onfocus="HS_setDate(this)" onkeydown="return false;" onpaste="return false"/>
														&nbsp;
														<select class="addtime" id="addtime">
															<option value="-1">全部</option>
														</select>
														<br />
													</td>
												</tr>
												<tr>
													<td class="menuTd" style="width: 20%">
														BSC：
														<br />
													</td>
													<td>
														<input type="text" name="bscname" id="bscname" 
															class="canclear  required"  />
														<br />
													</td>
												</tr>
											</tbody>


										</table>
									</div>
									<div class="container-bottom" style="padding-top: 10px;z-index:-1">
										<table style="width: 60%; margin: auto" align="center">
											<tr>
												<td>
													<input type="button" value="加载到分析列表" style="width: 90px;"
														id="importBtn" name="import" />
													<br />
												</td>


											</tr>
										</table>
										<div id="importResultDiv" class="container-bottom"
											style="padding-top: 10px">


										</div>
								</form>
							</div>
						</div>
					</div>

				</div>



			</div>


		</div>
	</body>