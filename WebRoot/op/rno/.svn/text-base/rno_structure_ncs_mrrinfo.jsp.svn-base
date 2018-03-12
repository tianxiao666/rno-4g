<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>结构分析任务mrr信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%> 
<script type="text/javascript" src="js/rno_structure_ncs_analysis.js?v=<%=(String)session.getAttribute("version")%>"></script>
  <style type="text/css">
     
  </style>
  <script type="text/javascript">
  $(document).ready(function(){
  	backReponse();
  	ncsToMrrPageAreaReponse();
  });
  //预览页面上一步的响应方法
  function backReponse(){
  	var mrrAreaCoverage=$("#mrrAreaCoverage").val();
	if ($.trim(mrrAreaCoverage)==""){
		return;
	}
	if ($.trim(mrrAreaCoverage)!="") {
		$("#cityId2 option").each(function(i,obj){
		var city=$.trim($(obj).text());
		if(mrrAreaCoverage==city){
			$(obj).attr("selected", "selected");
			$("#searchMrrDescBtn").click();
			return false;
		}
		});
		var mrrIds=$("#mrrIds").val();
		var mrridarr=mrrIds.split(",");
		$.each(mrridarr,function(i,val){
			//console.log(i+"--"+val);
			$(".forcheck").each(function(){
				console.log(this);
				var id=$(this).attr('id');
				if(id==val){
					$(this).attr('checked','checked');
				}
			});
		});
	}
	
}
  //为了保持mrr与ncs区域相一致
  function ncsToMrrPageAreaReponse(){
	  var mrrAreaCoverage=$("#mrrAreaCoverage").val();
	  if ($.trim(mrrAreaCoverage)==""){
			
		  var ncsAreaCoverage=$("#ncsAreaCoverage").val();
			if ($.trim(ncsAreaCoverage)!="") {
				$("#cityId2 option").each(function(i,obj){
				var city=$.trim($(obj).text());
				if(ncsAreaCoverage==city){
					$(obj).attr("selected", "selected");
					$("#searchMrrDescBtn").click();
					return false;
				}
				});
			}
	  }
  }
  </script>
  </head>
  
  <body>
  <input type="hidden" id="ncsAreaCoverage" value="<s:property value="#session.NCSTASKINFO.ncsInfo.ncsAreaCoverage"/>"/>
  <input type="hidden" id="mrrAreaCoverage" value="<s:property value="#session.NCSTASKINFO.mrrInfo.mrrAreaCoverage"/>"/>
  <input type="hidden" id="mrrIds" value="<s:property value="#session.NCSTASKINFO.mrrInfo.mrrIds"/>"/>
  	<a href="initNcsAnalysisPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br>
  	 <font style="font-weight: bold;">任务信息>>Ncs文件选择>><font style="color: #31FF81;">Mrr文件选择</font>>>提交任务</font>
  	<br>
  		
     <div style="width: 100%;margin-top: 20px" id="selectNcsDataDiv">
    <table style="width:600px;margin: 0px auto;">
    <tr><td style="width: 149px; ">选择Mrr数据</td><td><input type="button" id="mrrInfoPreStep" name="" value="<上一步 ""/></td><td><input type="button" id="mrrInfoNextStep" name="" value="下一步 >"/></td></tr>
    </table>
    </div>
    
    					<div class="divtab_content">
						<div id="div_tab_0">
							<form id="mrrDescDataForm" method="post">
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
											<td class="menuTd" style="text-align: center ">厂家</td>
											<td class="menuTd" style="text-align: center ">BSC名称</td>
											<td class="menuTd" style="text-align: center ">测试文件名称</td>
											<td class="menuTd" style="text-align: center">测试时间</td>
										</tr>

										<tr>
											<td style="text-align: left">省：<select name="provinceId"
												class="required" id="provinceId2">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />市：<select name="cond['cityId']" class="required"
												id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />区：<select name="cond['AREAID']" class="required"
												id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
											<td style="text-align: left"><select
												name="cond['manufacturer']" id="facturer">
													<option value='ERICSSON'>爱立信</option>
													<option value='HUAWEI'>华为</option>
													<option value='ALL'>全部</option>
											</select></td>
											<td style="text-align: left"><input type="text"
												name="cond['bsc']" / style="width:100%"></td>
											<td style="text-align: left"><input type="text"
												name="cond['fileNAME']" / style="width:100%"></td>
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

											<td style=" text-align: right" colspan="6">
											每页记录数量:
											<select id="hiddenPageSize" name="page.pageSize">
											<option value="25">每页25条记录</option>
											<option value="50">每页50条记录</option>
											<option value="10000000">不分页</option>
											</select>
											
											<input
												type="button" onclick="" value="查  询" style="width: 90px;"
												id="searchMrrDescBtn" name="search" /></td>
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
												
											</p>
										</td>
									</tr>
									<tr>
										<td style="width: 20%">
											<p>
											
												<input type="button" id="mrrConfirmSel"
													value="确定选择" />
											</p>
										</td>
									</tr>

								</table>

							</div>
							
							<%--peng.jm 加入   start--%>
							<div id="subAnalysisNcsStructure" class="dialog2 draggable ui-draggable" style="display: none;">
								<div class="dialog_header">
									<div class="dialog_title">
											提交汇总计算任务
									</div>
								</div>
								<div class="dialog_content" >
										<table style="width:100%;">
											<tr>
												<td align="right"> 
													任务名称：<br/>
													<span id="nameErrorText" style="color:red;width:100px;font-family:华文中宋;"></span>
												</td>
												<td> 
													<textarea id="taskName" style="width: 330px; height: 55px; "></textarea>
													<span id="nameError"  style="color:red;width:100px;font-family:华文中宋;"></span>
												</td>
											</tr>
											<tr>
												<td align="right"> 
													任务描述：<br/>
													<span id="descErrorText" style="color:red;width:100px;font-family:华文中宋;"></span>
												</td>
												<td> <br/>
													<textarea id="taskDescription" style="width: 330px; height: 184px; "></textarea>
													<span id="descError"  style="color:red;width:100px;font-family:华文中宋;"></span>
												</td>
											</tr>
										</table><br/>
										<table style="width:100%;">
											<tr>
												<td>&nbsp;&nbsp;
													<input type="button" id="submitNcsTask" style="" onclick='subAnalysisNcsStructure()' value="确定"/>
												</td>
												<td>
													<input type="button" id="submitNcsTask" style="" onclick='closeNcsTaskForm()' value="取消"/>
												</td>
											</tr>
										</table>
								</div>
							</div>
							<div id="taskSubmitError" class="dialog2 draggable ui-draggable" style="display: none;">
								<div class="dialog_header">
									<div class="dialog_title">
									</div>
									<div class="dialog_tool">
										<div class="dialog_tool_close dialog_closeBtn"
											onclick="$('#taskSubmitError').hide()"></div>
									</div>
								</div>
								<div class="dialog_content">
									<center>任务提交失败！</center>
									原因：<span id="taskSubmitErrorCause"></span>
									,请联系系统管理员解决！
								</div>
							</div>
							<div id="choseOldNcsTask" class="dialog2 draggable ui-draggable" style="display: none;">
								<div class="dialog_header">
									<div class="dialog_title">
											提示
									</div>
									<div class="dialog_tool">
										<div class="dialog_tool_close dialog_closeBtn"
											onclick="$('#choseOldNcsTask').hide()"></div>
									</div>
								</div>
								<input type="hidden" id="oldNcsTaskId" value="" />
								<div class="dialog_content">
									<span id="spanOldNcsTaskTip"></span></br>
									<table style="width:100%;">
										<tr>
											<td>&nbsp;&nbsp;
												<input type="button" id="showOldNcsTask" style="" onclick='showOldNcsTask()' value="查看旧任务信息"/>
											</td>
											<td>
												<input type="button" id="repeatNcsTask" style="" onclick='subRepeatNcsTask()' value="重复提交"/>
											</td>
										</tr>
									</table>
								</div>
							</div>
							
							<div id="showOldNcsTaskDetail" class="dialog2 draggable ui-draggable" style="display: none;">
								<div class="dialog_header">
									<div class="dialog_title">
										旧任务信息	
									</div>
									<div class="dialog_tool">
										<div class="dialog_tool_close dialog_closeBtn"
											onclick="$('#showOldNcsTaskDetail').hide()"></div>
									</div>
								</div>
								<div class="dialog_content">
									<form id='getOldTaskReportForm' action='exportNcsAnalysisReportAction' method='post' style="display:none">
							     		<input type='input' id='oldNcsTaskIdForm' name='oldNcsTaskIdForm' value='' />
									</form>
									<table id="showOldNcsTaskTab" class="oldTaskTable" width="80%">
										<tr>
											<th>任务名称</th>
										    <th>汇总范围</th>
										    <th>汇总区域名称</th>
										    <th>分析的NCS文件数量</th>
											<th>启动时间</th>
											<th>完成时间</th>
											<th>任务状态</th>
											<th>操作</th>
											<th>任务描述</th>
										</tr>
									</table>
								</div>						
							</div>
							<%--peng.jm 加入   end--%>
							
							<div style="padding-top: 10px">
								<table id="queryMrrResultTab" class="greystyle-standard"
									width="100%">
									<tr>
										<th><input type='checkbox' id='selAllNcsCb' />全选</th>
										<th>文件名称</th>
										<th>BSC</th>
										<th>测量时间</th>
										<th>城市</th>
										<TH>区域</TH>
										<th>操作</th>
									</tr>
								</table>
							</div>
							<div class="paging_div" id='mrrResultPageDiv'
								style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
									条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first',doQueryMrrDesc,'mrrDescDataForm')"></a>
								<a class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back',doQueryMrrDesc,'mrrDescDataForm')"></a>
								第 <input type="text" id="showCurrentPage"
									class="paging_input_text" value="1" /> 页/<em
									id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
									title="GO"
									onclick="showListViewByPage('num',doQueryMrrDesc,'mrrDescDataForm')">GO</a>
								<a class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next',doQueryMrrDesc,'mrrDescDataForm')"></a>
								<a class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last',doQueryMrrDesc,'mrrDescDataForm')"></a>
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
