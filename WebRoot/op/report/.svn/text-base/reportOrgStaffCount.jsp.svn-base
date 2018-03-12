<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织人员数量统计</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>
<link rel="stylesheet" type="text/css" href="css/statements.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script type="text/javascript" src="js/report.js"></script>
<script type="text/javascript" src="js/reportOrgStaffCount.js"></script>
<script type="text/javascript" src="../js/tab.js" ></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript">
$(function(){ 
	//统计组织树
	$("#statisticsTree").treeview({
		collapsed: true,   // true为默认折叠 false为默认展开
		animated: "fast"  //动画---快
	});
	$("#statisticsTree li span").each(function(){
		$(this).click(function(){
			$("#statisticsTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//显示-隐藏服务商组织树
	$(".statistics_treeButton").click(function(){
		$("#statisticsTree").slideToggle("fast");
	});
	
	//统计组织树
	$("#checkTree").treeview({
		collapsed: true,   // true为默认折叠 false为默认展开
		animated: "fast"  //动画---快
	});
	$("#checkTree li span").each(function(){
		$(this).click(function(){
			$("#checkTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//显示-隐藏服务商组织树
	$(".check_treeButton").click(function(){
		$("#checkTree").slideToggle("fast");
	});
	
	//tab选项卡
	//tab("compareTab","li","onclick");//组织类别切换
})
</script>
</head>
<body>
<%--主体开始--%>
<div id="statements_content">
	<%--右边报表开始--%>
	<div class="statements_right">
	    <div class="statements_menu_title clearfix">
		    <p class="fl">人员数统计</p>
		    <input type="hidden" id="orgNameText" value="人员数统计"/>
			<p class="fr">
			   <span title="指标说明" class="text-icon explain"></span>
			   <span title="报表评论" class="text-icon comment_list comment_dialog_show"></span>
			   <span title="导出EXCEL" class="text-icon excel"></span>
			   <span title="自定义查询" class="text-icon search show_report_settings"></span>
			</p>
		</div>
		<%--显示/隐藏报表设置--%>
		<div class="main_output_table report_settings" style="display:none;">
		    <table>
				<tbody>
					<tr>
						<td class="menuTd">考核组织：</td>
						<td><input type="text" class="input_text" /><a href="#" class="check_treeButton orgButton"></a>
							<ul id="checkTree" class="filetree checkTree">
								<li><span class="folder">第一事业部</span>
									<ul>
										<li><span class="folder">海珠一体化项目</span>
											<ul>
												<li><span class="folder">东部片区</span>
													<ul>
														<li><span class="file">东一片区</span></li>
														<li><span class="file">东二片区</span></li>
													</ul>
												</li>
												<li><span class="folder">中部片区</span></li>
												<li><span class="folder">西部片区</span></li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</td>
						<td class="menuTd">统计时间：</td>
						<td>
							<input type="text" class="input_text" /><a href="#" class="dateButton"></a>&nbsp;到
							<input type="text" class="input_text" /><a href="#" class="dateButton"></a>
						</td>
					</tr>
					<tr>
						<td class="menuTd">业务数据筛选：</td>
						<td colspan="3">
							<select>
								<option value="故障类型" selected="">故障类型</option>
								<option value="告警级别">告警级别</option>
								<option value="是否紧急故障">是否紧急故障</option>
								<option value="基站类型">基站类型</option>
								<option value="故障发生地市">故障发生地市</option>
							</select>
							<select>
								<option value="等于" selected="">等于</option>
								<option value="不等于">不等于</option>
							</select>
							<select>
								<option value="传输故障" selected="">传输故障</option>
								<option value="投诉">投诉</option>
								<option value="A2">A2</option>
								<option value="A2">A2</option>
								<option value="轮询">轮询</option>
							</select>
							<input type="button" class="input_button" value="提交考核结果" />
						</td>
					</tr>
					<tr>
						<td class="menuTd vt">已选筛选条件：</td>
						<td colspan="3">
						    <div class="settings_role">
								<ul>
									<li class="clearfix">
										<p class="settings_role_info">
											<span>@故障类型</span>
											<span>等于</span>
											<span>#投诉</span>
										</p>
										<a class="settings_role_del">×</a>
									</li>
									<li class="clearfix">
										<p class="settings_role_info">
											<span>@故障类型</span>
											<span>等于</span>
											<span>#投诉</span>
										</p>
										<a class="settings_role_del">×</a>
									</li>
									<li class="clearfix">
										<p class="settings_role_info">
											<span>@故障类型</span>
											<span>等于</span>
											<span>#投诉</span>
										</p>
										<a class="settings_role_del">×</a>
									</li>
							    </ul>
							</div>
						</td>
					</tr>
					<tr class="tc">
						<td colspan="4"><input type="button" class="input_button" value="确定" />&nbsp;<input type="button" class="input_button" value="取消" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="statements_search_date">
			<p>&lt;周期&gt;</p>
			<span class="time_tool_left" onclick="getLastMonth()"></span>
			<span class="time_tool_main" id="DateTimeSpan">2012年12月</span>
			<span class="time_tool_right" onclick="getNextMonth()"></span>
		</div>
		
		<div id="compareTab" class="compareTab_menu">
			<ul class="clearfix">
				<li id="compareLi" class="selected">对比</li>
				<li id="chainLi" title="不同统计周期的趋势分析(通常是按月)">环比</li>
				<li id="sameCompareLi" title="不同年度的同期趋势分析">同比</li>
			</ul>
		</div>
		<div class="tab_menu">
			<ul>
				<li class="ontab first_tab" onclick="getCurUserReportInfo()">按组织</li>
				<li id="projectLi" onclick="getOrgProjectReportInfo()">按项目</li>
			</ul>
		</div>
		<div class="statements_select" style="top:151px;z-index:15;margin-right:30px;">
			<span onclick="clickReturnReport();" style="display:none;" title="返回上级" id="statements_back" class="statements_back"></span>
		</div>
		<div class="tab_container">
			<div id="compareTab_0" class="statements_img_half">
				<div class="statements_tab">
					<div class="tab_container">
						<div class="tab_content">
							<div class="statements_classify" >
								<span class="statements_classify_title"><input type="checkbox" id="classifyCheckBox"/>分类统计</span>
								<div class="statements_classify_info" style="display: none;">
									<input type="radio" name="1" checked="checked"/><em>按人员技能</em>
								</div>
							</div>
							<%--报表图形--%>
							<div class="statements_img">
								<em onclick="addDivChar();" class="st_full_screen"></em>
								<div id="orgStaffCountDiv" style="height: 280px;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<br />
			<%--报表详情--%>
			<div id="reportDetailDiv">
				<div class="statements_table_top">
					<span>
						<a href="javascript:void(0);" id="tableControl"><em>[+]</em>展开表格</a>
					</span>
					<span id="countDetailSpan"></span>
				</div>
				<table id="reportDetailTable" class="main_table tc" style="display: none;">
						
				</table>
			</div>
			<%--报表评论--%>
			<div class="statements_comment">
				<%--评论位置--%>
				<div class="statements_comment" id="reportComment" name="reportComment">
										
				</div>
			</div>	
		</div>
	</div>
	<%--右边报表结束--%>
</div>
<%-- 报表隐藏域（放大版） 开始 --%>
<div id="ireportDiv" style="height: 500px; width: 1000px; display: none;"></div>
<%-- 报表隐藏域（放大版） 结束 --%>
<%--主体结束--%>
</body>
</html>




