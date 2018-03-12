<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统计报表</title>
</head>

<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="../../css/input.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/dialog/dialog.css" type="text/css" />
<link rel="stylesheet" href="css/statements.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="js/reportnetworkResourece.js"></script>
<script type="text/javascript" src="js/reportUrgentRepairworkorderCount.js"></script>
<script type="text/javascript" src="js/loadReport.js"></script>
<Style>
#tree2 span{ cursor:pointer;}
#tree2 span:hover{ background:#eee;}
#tree2 {
height:262px;
overflow:auto;
}
#tree3 span{ cursor:pointer;}
#tree3 span:hover{ background:#eee;}
#tree3 {
height:262px;
overflow:auto;
}
</Style>
<script type="text/javascript">

</script>
<input type="hidden" value="${orgId }" id="hiddenorgId"/>
<input type="hidden" id="orgIdText"/>
<input type="hidden" id="orgNameText"/>
<div id="statements_right" class="statements_right" style="display: none"></div>
<div id="statements_left">
<input type="hidden" id="hiddenrowName" value="${rowName }"/>
<input type="hidden" id="hiddenrowNameText" value="${rowNameText }"/>
<input type="hidden" id="hiddenrowValue" value="${rowValue }"/>
<input type="hidden" id="hiddenjudge" value="${judge }"/>
<input type="hidden" id="hiddenorgName" value="${orgName }"/>
<input type="hidden" id="hiddenendTime" value="${endTime }"/>
<input type="hidden" id="hiddenbeginTime" value="${beginTime }"/>
<s:iterator value="urgentRepairworkorderCountList" id="vs" status="st2">
<input type="hidden" class="orgId" value="${vs.orgId }"/>
<input type="hidden" class="orgName" value="${vs.orgName }"/>
<input type="hidden" class="bn" va="${vs.orgId }" value="${vs.orgName }"/>
<input type="hidden" class="workorderCount" value="${vs.wCount }"/>
<input type="hidden" class="ProcessTimeCount" value="${vs.ProcessTimeCount }"/>
<input type="hidden" class="hiddenacceptProfessional" value="${vs.AcceptedTimeRateCount }"/>
<input type="hidden" value="${treeNode.baseStationType }" id="hiddenbaseStationLevel"/>
<input type="hidden" value="${treeNode.acceptProfessional }" id="hiddenacceptProfessional"/>
<input type="hidden" value="${treeNode.faultLevel }" id="hiddenfaultLevel"/>
<input type="hidden" value="${treeNode.faultType }" id="hiddenfaultType"/>
</s:iterator>
<input type="hidden" value="UrgentRepair" id="hidereportType"/>
<div id="selectDivHidden" style="display: none"></div>
	    <div class="statements_menu_title clearfix">
		    <p class="fl"><span>工单数量</span><span id="reportType" style="display: none;">报表统计→运维生产统计分析→故障抢修→工单数量</span></p>
			<p class="fr">
			   <%-- <span class="text-icon explain" onclick="getReportIndicatorsDescription();" title="指标说明"></span> --%>
			   <span class="text-icon excel" title="导出EXCEL"></span>
			   <%-- <span class="text-icon word"></span> --%>
			   <%-- <span class="text-icon pdf"></span> --%>
			   <span class="text-icon search show_report_settings" title="自定义查询"></span>
			</p>
		</div>
		<%--显示/隐藏报表设置--%>	
		<div class="main_output_table report_settings" style="display:none;">
		    <table>
				<tbody>
					<tr>
						<td class="menuTd">考核组织：</td>
						<td><input type="text" class="input_text"  id="orgName1"/><a href="#" id="statements_tree_button2" class="statements_tree_button orgButton"></a>
							<div class="statements_tree  ml71" id="treeDiv2" >
								<div class="statements_but">
									<input type="button" class="input_button" value="确定" />
								</div>
							</div>
						</td>
						<td class="menuTd">统计时间：</td>
						<td>
							<input type="text" class="input_text" value="${beginTime }" id="beginTime"/><a href="#" class="date_button" onclick="fPopCalendar(event,document.getElementById('beginTime'),document.getElementById('beginTime'),true)" ></a>&nbsp;到
							<input type="text" class="input_text" value="${endTime }" id="endTime"/><a href="#" class="date_button" onclick="fPopCalendar(event,document.getElementById('endTime'),document.getElementById('endTime'),true)" ></a>
						</td>
					</tr>
				    <tr>
						<%-- <td class="menuTd">报表分组：</td>
						<td>
							<select>
								<option value="故障类型" selected="">故障类型</option>
								<option value="告警级别">告警级别</option>
								<option value="是否紧急故障">是否紧急故障</option>
								<option value="基站类型">基站类型</option>
								<option value="故障发生地市">故障发生地市</option>
							</select>
						</td>
						 --%>
						<%-- 
						<td class="menuTd">图表类型：</td>
						<td>
							<input type="radio" checked="" value="radio" name="u42" id="">曲线图
							<input type="radio" checked="" value="radio" name="u42" id="">柱状图
							<input type="radio" checked="" value="radio" name="u42" id="">饼图
							<input type="radio" checked="" value="radio" name="u42" id="">条形图
						</td>
						 --%>
					</tr>
					<tr>
						<td class="menuTd">业务数据筛选：</td>
						<td colspan="3">
							<select id="rowName" onchange="showRowValue(this);">
								<option value="baseStationLevel" selected="">基站等级</option>
								<option value="faultType">故障类型</option>
								<option value="faultLevel">故障级别</option>
								<option value="acceptProfessional">受理专业</option>
								<option value="faultGenera">故障大类</option>
							</select>
							<select id="Judge">
								<option value="等于" selected="">等于</option>
								<option value="不等于">不等于</option>
							</select>
							<select id="rowValue">
											
							</select>
							<input type="button" class="input_button" value="添加业务数据筛选条件"  onclick="addrole();"/>
						</td>
					</tr>
					<tr>
						<td class="menuTd vt">已选筛选条件：</td>
						<td colspan="3">
						    <div class="settings_role">
								<ul id="roleul">
									
							    </ul>
							</div>
						</td>
					</tr>
					<tr style="display: none;">
						<td class="menuTd vt">统计指标：<p class="pr10"><input type="checkbox" class="input_checkbox" />全选</p></td>
						<td colspan="3">
						    <div class="settings_target">
								<ul>
									<li class="clearfix">
										<label>工作量考核指标</label>
										<span><input type="checkbox" class="input_checkbox" />工单数</span>
										<span><input type="checkbox" class="input_checkbox" />移动工单IOSM系统派单率</span>
										<span><input type="checkbox" class="input_checkbox" />单位基站故障率</span>
									</li>
									<li class="clearfix">
										<label>工作效率考核指标</label>
										<span><input type="checkbox" class="input_checkbox" />工单及时率</span>
										<span><input type="checkbox" class="input_checkbox" />工单处理平均历时</span>
										<span><input type="checkbox" class="input_checkbox" />超时工单数</span>
									</li>
									<li class="clearfix">
										<label>工作质量考核指标</label>
										<span><input type="checkbox" class="input_checkbox" />工单受理拦截率</span>
										<span><input type="checkbox" class="input_checkbox" />队长处理拦截率</span>
										<span><input type="checkbox" class="input_checkbox" />现场处理拦截率</span>
										<span><input type="checkbox" class="input_checkbox" />技术支援拦截率</span>
										<span><input type="checkbox" class="input_checkbox" />延期解决率</span>
									</li>
							    </ul>
							</div>
						</td>
					</tr>
					<tr class="tc">
						<td colspan="4">
						<form target= "_blank" action="getUrgentRepairWorkorderByrowNameAction" method="post">
						<input type="hidden" id="rowName1" name="rowName"/>
						<input type="hidden" id="rowNameText" name="rowNameText"/>
						<input type="hidden" id="Judge1" name="Judge"/>
						<input type="hidden" id="rowValue1" name="rowValue"/>
						<input type="hidden" id="orgId1" name="orgId"/>
						<input type="hidden" id="orgName2" name="orgName"/>
						<input type="hidden" id="beginTime1" name="beginTime"/>
						<input type="hidden" id="endTime1" name="endTime"/>
						<input type="submit" class="input_button" value="确定" id="settings_btn" />&nbsp;<input type="button" class="input_button" id="hide_settings_btn" value="取消" />
						</form>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	    <div class="statements_search">
			<div style=" text-align: center; font-size: 20px; font-weight:  bolder;">
				故障工单数量统计分析报表
			</div>
		</div>
		<div id="judgeDiv"></div>
		<div class="statements_tab">
			<div class="tab_menu">
				<ul>
					<li class="ontab first_tab" onclick="getReportByStationType(this);">按组织</li>
					<li  onclick="getReportByStationType(this);">按基站类型</li>
					<li  onclick="getReportByStationType(this);">按故障类型</li>
					<li  onclick="getReportByStationType(this);">按告警级别</li>
					<li  onclick="getReportByStationType(this);">按受理专业</li>
					<li  onclick="getReportByStationType(this);">按故障大类</li>
				</ul>		
			</div>
			<div class="tab_container">
				<div class="tab_content" id="orginstDiv">
					<%--报表位置--%>
					<div class="statements_img">
					   <div id="statements_img"></div>
					</div>
				</div>
			</div>
		</div>
		</div>
		<div id="bn" style="display: none;"></div>
