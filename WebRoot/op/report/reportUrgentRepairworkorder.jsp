<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="css/statements.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="../../css/input.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="js/reportUrgentRepairworkoerder.js"></script>
<script type="text/javascript">

</script>
<title>明细报表</title>
</head>
<body>
<div class="statements_menu_title clearfix">
		    <p class="fl">（${DateTimeString }&nbsp;&nbsp;&nbsp;${orgName }）</p>
			<p class="fr">
			   <span class="text-icon excel" title="导出EXCEL"></span>
			    <%-- <span class="text-icon word"></span>--%>
			   <%--  <span class="text-icon pdf"></span>--%>
			   <span class="text-icon search show_report_settings" title="自定义查询"></span>
			</p>
		</div>
		<%--显示/隐藏报表设置--%>
		<div class="main_output_table report_settings" style="display:none;">
		    <table>
				<tbody>
					<tr>
						<td class="menuTd" style="display: none;">考核组织：</td>
						<td style="display: none;"><input type="text" class="input_text" /><a href="#" class="statements_tree_button select_button"></a>
							<div class="statements_tree">
								<ul id="tree" class="statements_tree_info">
									<li>第一事业部
										<ul>
											<li>123
												<ul>
													<li>123</li>
													<li>123</li>
													<li>123</li>
													<li>123</li>
													<li>123</li>
													<li>123</li>
												</ul>
											</li>
											<li>123</li>
											<li>123</li>
										</ul>
									</li>
								</ul>
								<div class="statements_but">
									<input type="button" class="input_button" value="确定" />
								</div>
							</div>
						</td>
						<td class="menuTd">统计时间：</td>
						<td>
							<input type="text" class="input_text" /><a href="#" class="date_button"></a>&nbsp;到
							<input type="text" class="input_text" /><a href="#" class="date_button"></a>
						</td>
					</tr>
				    <tr style="display: none;">
						<td class="menuTd">报表分组：</td>
						<td>
							<select>
								<option value="故障类型" selected="">故障类型</option>
								<option value="告警级别">告警级别</option>
								<option value="是否紧急故障">是否紧急故障</option>
								<option value="基站类型">基站类型</option>
								<option value="故障发生地市">故障发生地市</option>
							</select>
						</td>
						<td class="menuTd">图表类型：</td>
						<td>
							<input type="radio" checked="" value="radio" name="u42" id="">曲线图
							<input type="radio" checked="" value="radio" name="u42" id="">柱状图
							<input type="radio" checked="" value="radio" name="u42" id="">饼图
							<input type="radio" checked="" value="radio" name="u42" id="">条形图
						</td>
					</tr>
					<tr style="display: none;">
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
					<tr style="display: none;">
						<td class="menuTd vt">已选筛选条件：</td>
						<td colspan="3">
						    <div class="settings_role">
								<ul>
									<li>
										<span>@故障类型</span>
										<span>等于</span>
										<span>#投诉</span>
									</li>
									<li>
										<span>@故障类型</span>
										<span>等于</span>
										<span>#投诉</span>
									</li>
									<li>
										<span>@故障类型</span>
										<span>等于</span>
										<span>#投诉</span>
									</li>
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
						<td colspan="4"><input type="button" class="input_button" value="确定" />&nbsp;<input type="button" class="input_button" value="取消" /></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<%--表单位置--%>
		<div class="statements_main_table">
			<table class="main_table tc" id="main_table_task">
				<tr>
					<th style="width:55px;">工单编号</th>
					<th>平均历时(小时)</th>
					<th>是否超时</th>
					<th>基站类型</th>
					<th>故障类型</th>
					<th>告警级别</th>
					<th>受理专业</th>
					<th>故障大类</th>
					<th>故障原因</th>
					<th>故障发生时间</th>
					<th>故障消除时间</th>
				</tr>
				<s:iterator value="urgentRepairCountList" id="vs" status="st2">
				
				<tr class="main_table_task_tr">
					<td><a href="#" onclick="clickWOID('<s:property value="#vs.WOID"/>');"><s:property value="#vs.WOID"/></a></td>
					<td class="troudleshootingTime"><s:property value="#vs.troudleshootingTimeparseFloat"/></td>
					<s:if test="#vs.ProcessTimeRate == 0">
						<td>是</td>
					</s:if>
					<s:else>
						<td>否</td>
					</s:else>
						<td><s:property value="#vs.baseStationLevel"/></td>	
					<td><s:property value="#vs.faultType"/></td>
					<td><s:property value="#vs.faultLevel"/></td>
					<td><s:property value="#vs.acceptProfessional"/></td>
					<td><s:property value="#vs.faultGenera"/></td>
					<td><s:property value="#vs.faultCause"/></td>
					<td><s:date name="#vs.faultOccuredTime" format="yyyy-MM-dd HH:mm:ss" /></td>
					<td><s:date name="#vs.alarmClearTime" format="yyyy-MM-dd HH:mm:ss" /></td>
					<s:else>
						<td></td>
					</s:else>
					
				</tr>
				</s:iterator>
				
				
				<tr class="tl">
					<td colspan="15">工单数量合计<em class="text_red" id="taskCount"></em>张,处理历时共<em class="text_red" id="timeCount"></em>小时,平均处理历时<em class="text_red" id="timeAverage"></em>小时</td>	
				</tr>
			</table>
			<div id="table-gaging">
			</div>
		</div>
    </div>
   </body>
</html>