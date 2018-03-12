<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
<script type="text/javascript" src="js/report.js"></script>
<%--<script type="text/javascript" src="../../jslib/date/date.js"></script>--%>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/networkResourceViewDivPage.js"></script>
<script type="text/javascript" src="js/reportnetworkResourece.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script type="text/javascript" src="../../jslib/dialog/drag.js"></script>

<script type="text/javascript" src="js/reportUrgentRepairworkorderStatistics.js"></script>
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
//显示报表表格
	function showOrHideStationTableDiv(me){
		if($(me).html() == "<em>[+]</em>展开表格"){
			$(me).html("<em>[-]</em>展开表格");
			$("#stationTableDiv").show();
		}else{
			$(me).html("<em>[+]</em>展开表格");
			$("#stationTableDiv").hide();
		}
	}
	

	
	
</script>
<style type="text/css">
.statements_classify{float:left;}
.statements_select1{ float:right;margin: 10px;}
.statements_back{background:url("images/statements_back.png") no-repeat; width:16px; height:16px; display:inline-block; cursor:pointer;margin: 0 3px;}
</style>
<input type="hidden" id="orgIdText"/>
<input type="hidden" id="orgNameText"/>
<input type="hidden" id="timehidden" value="${param.time }"/>
<input type="hidden" value="${orgId }" id="orgId"/>
<input type="hidden" value="${orgName }" id="orgName"/>
<input type="hidden" value="${orgId }" id="orgIdT"/>
<input type="hidden" value="${orgName }" id="orgNameT"/>
<input type="hidden" value="" id="hiddenbeginTime"/>
<input type="hidden" value="" id="hiddenendTime"/>
<input type="hidden" value="UrgentRepair" id="hidereportType"/>
<input type="hidden" value="${treeNode.baseStationType }" id="hiddenbaseStationLevel"/>
<input type="hidden" value="${treeNode.acceptProfessional }" id="hiddenacceptProfessional"/>
<input type="hidden" value="${treeNode.faultLevel }" id="hiddenfaultLevel"/>
<input type="hidden" value="${treeNode.faultType }" id="hiddenfaultType"/>
<div id="selectDivHidden" style="display: none"></div>
	    <div class="statements_menu_title clearfix">
		    <p class="fl"><span>抢修工单数/历时/及时率统计</span><span id="reportType" style="display: none;">报表统计→运维生产统计分析→故障抢修→故障抢修工单统计分析报表</span></p>
			<p class="fr">
			   <span class="text-icon explain" onclick="getReportIndicatorsDescription();" title="指标说明"></span>
			   <span class="text-icon comment_list" onclick="showComment();" title="报表评论"></span>
			   <span class="text-icon excel" title="导出EXCEL"></span>
			   <%-- <span class="text-icon word"></span>--%>
			   <%-- <span class="text-icon pdf"></span>--%>
			   <span class="text-icon search show_report_settings" title="自定义查询"></span>
			</p>
		</div>
		<%--显示/隐藏报表设置--%>	
		<div class="main_output_table report_settings" style="display:none;">
		    <table>
				<tbody>
					<tr>
						<td class="menuTd">考核组织：</td>
						<td><input type="text" class="input_text"  id="orginnstName1"/><a href="#" id="statements_tree_button2" class="statements_tree_button orgButton"></a>
							<div class="statements_tree  ml71" id="treeDiv2" >
								<div class="statements_but">
									<input type="button" class="input_button" value="确定" />
								</div>
							</div>
						</td>
						<td class="menuTd">统计时间：</td>
						<td>
							<input type="text" class="input_text" value="" id="beginTime"/><a href="#" class="date_button" onclick="fPopCalendar(event,document.getElementById('beginTime'),document.getElementById('beginTime'),true)" ></a>&nbsp;到
							<input type="text" class="input_text" value="" id="endTime"/><a href="#" class="date_button" onclick="fPopCalendar(event,document.getElementById('endTime'),document.getElementById('endTime'),true)" ></a>
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
						<input type="hidden" id="orginnstId1" name="orgId"/>
						<input type="hidden" id="orginnstName2" name="orgName"/>
						<input type="hidden" id="beginTime1" name="beginTime"/>
						<input type="hidden" id="endTime1" name="endTime"/>
						<input type="submit" class="input_button" value="确定" id="settings_btn" />&nbsp;<input type="button" class="input_button" id="hide_settings_btn" value="取消" />
						</form>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	    <%-- <div class="statements_search">--%>
		    <%-- <span>统计组织：</span>
			<input type="text" class="input_text" id="orginnstName"/><a href="#" id="statements_tree_button1" class="statements_tree_button orgButton"></a>
			<div class="statements_tree ml71" id="treeDiv">
				<div class="statements_but">
					<input type="button" class="input_button" value="确定" />
				</div>
			</div>
			<input type="button" class="input_button" value="确认" onclick="clickGetUrgentRepairworkorderReport();"/>
			--%>
			<div class="statements_search_date">
				<p><周期></p>
				<span class="time_tool_left" onclick="getlastMonth();"></span>
				<span id="DateTimeSpan" class="time_tool_main"></span><input type="hidden" id="hiddenYear" value=""/><input type="hidden" id="hiddenMonth" value=""/>
				<span class="time_tool_right" onclick="getNextMonth();"></span>
			</div>
			<div id="compareTab" class="compareTab_menu">
				<ul class="clearfix">
					<li id="Dselected" class="selected" onclick="clickGetUrgentRepairContrast();clickClearFix(this);">对比</li>
					<li onclick="clickGetUrgentRepairRing();clickClearFix(this);" title="不同统计周期的趋势分析(通常是按月)" id="Ring">环比</li>
					<li onclick="clickGetUrgentRepairPeriod();clickClearFix(this);" title="不同年度的同期趋势分析" id="Period">同比</li>
				</ul>
			</div>
		<%--</div>--%>
		<div class="statements_tab">
			<div class="tab_menu">
				<ul>
					<li class="ontab first_tab" onclick="getReportByorginst(this);">按组织</li>
					<li  onclick="getReportByorginst(this);" style="display: none;">按基站类型</li>
					<li  onclick="getReportByorginst(this);" style="display: none;">按故障类型</li>
					<li  onclick="getReportByorginst(this);" style="display: none;">按告警级别</li>
					<li  onclick="getReportByorginst(this);" style="display: none;">按受理专业</li>
					<li  onclick="getReportByorginst(this);" style="display: none;">按故障大类</li>
					<li onclick="getReportByorginst(this);">按项目</li>
				</ul>		
			</div>
			<div class="tab_container">
				<div class="tab_content" id="stationTypeDiv" style="display:none;">
					<div class="tab_menu_type">
					    <a href="#">柱图</a>
						<a href="#">线图</a>
						<a href="#">饼图</a>
					</div>
					<%--报表位置--%>
					<div class="statements_img">
					   <div id="statements_img"></div>
					</div>
					<%--表单位置--%>
					<div class="statements_main_table" id="statements_main_table">
						
					</div>
				</div>
				<div class="tab_content" id="orginstDiv" >
					<%--<div class="tab_menu_type">
					    <a href="#">柱图</a>
						<a href="#">线图</a>
						<a href="#">饼图</a>
					</div>--%>
					<div class="clearfix">
						<div class="statements_classify" id="statements_classify">
							<span class="statements_classify_title"><input type="checkbox" id="typecheckbox" onclick="getReportByorginstByCheckbox();"/>分类统计</span>
							<div class="statements_classify_info" id="uradiodiv" style="display: none;">
								<input type="radio" name="1" onclick="getReportByorginstByRadio();" class="uradio" value="BaseStationLevel" checked="checked" title="按基站类型"/><em>按基站类型</em>
								<input type="radio" name="1" onclick="getReportByorginstByRadio();" class="uradio" value="FaultType" title="按故障类型"/><em>按故障类型</em>
								<input type="radio" name="1" onclick="getReportByorginstByRadio();" class="uradio" value="FaultLevel" title="按故障类型"/><em>按告警级别</em>
								<input type="radio" name="1" onclick="getReportByorginstByRadio();" class="uradio" value="AcceptProfessional" title="按受理专业"/><em>按受理专业</em>
								<input type="radio" name="1" onclick="getReportByorginstByRadio();" class="uradio" value="FaultGenera" title="按故障大类"/><em>按故障大类</em>
							</div>
						</div>
						<div class="statements_select1">
							<span class="statements_back" id="statements_back" title="返回上级" style="display:none;" onclick="clickReturnReport();"></span>
							<input type="hidden" id="returnOrgId" value="${orgId }" />
							<input type="hidden" id="thisOrgId" value="${orgId }" />
							<select>
								<option selected="" value="全部">全部</option>
							</select>
						</div>
					</div>
					<%--报表位置--%>
					<div class="clearfix">
						<div class="statements_img_half1" id="statements_img_half1">
							<em class="st_full_screen" onclick="addDiv('pieBasicDiv');"></em>
							<div id="pieBasicDiv"></div>
						</div>
						<div class="statements_img_half2" id="statements_img_half2">
							<em class="st_full_screen" onclick="addDiv('columnBasicDiv');"></em>
							<div id="columnBasicDiv"></div>
						</div>
						<div class="statements_img_half" id="sDivstatements_img_half" style="display: none;">
							<div id="sDiv"></div>
						</div>
					</div>
					<div class="statements_table_top" style="height: 26px;" id="statements_table_top">
						<span style="float: left;"><a onclick="showOrHideStationTableDiv(this);"><em>[+]</em>展开表格</a></span>
						<span style="float: left;">工单总数：<em id="uCount"></em></span>
						<span style="float: left;">平均工单数量：<em id="upCount"></em></span>
						<span style="float: left;">工单处理历时：<em id="uProcessTime"></em></span>
					</div>
					<%--表单位置--%>
					<div class="statements_main_table" id="stationTableDiv" style="display: none;">
						
					</div>
				</div>
			</div>
			<%--评论位置--%>
			<div class="statements_comment" id="reportComment" name="reportComment">
									
			</div>
		</div>
		<div id="ireportB" style="height: 500px; width: 1000px; display: none;"></div>