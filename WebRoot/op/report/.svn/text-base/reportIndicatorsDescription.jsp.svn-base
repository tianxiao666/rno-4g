<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="css/statements.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<title>指标说明</title>
<script type="text/javascript">
$(function(){
	//指标说明树
	$("#reportTree").treeview({
		collapsed: false,   // true为默认折叠 false为默认展开
		animated: "fast",  //动画---快
	});
	$("#reportTree li span").each(function(){
		$(this).click(function(){
			$("#reportTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
		var reportType = $("#reportType").text();
		var reportTypes= new Array();
		reportTypes = reportType.split("→");
			$.each(reportTypes,function(k,v){
					$("#"+v).next().show();
			});
	
});

function downloadindicatorDescriptionCHM(){
	window.location.href = "downloadindicatorDescriptionCHMAction?fileName=indicatorDescription.chm";
}
</script>
</head>
<body>
<div class="statements_menu_title clearfix">
		    <p class="fl"><span id="reportType">${reportType }</span></p>
			<p class="fr">
			   <span class="text-icon excel"></span>
			   <span class="text-icon word"></span>
			   <span class="text-icon pdf"></span>
			   <span class="text-icon search show_report_settings"></span>
			</p>
		</div>
		<%--显示/隐藏报表设置--%>
		<div class="main_output_table report_settings" style="display:none;">
		    <table>
				<tbody>
					<tr>
						<td class="menuTd">考核组织：</td>
						<td><input type="text" class="input_text" /><a href="#" class="statements_tree_button select_button"></a>
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
				    <tr>
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
					<tr>
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
		<%--指标说明区域--%>
		<div class="clearfix">
			<div class="report_description_search">
				<select id="" name="">
					<option value="最高" selected="">最高</option>
					<option value="较高">较高</option>
					<option value="普通">普通</option>
					<option value="低">低</option>
				</select>
				<select id="" name="">
					<option value="最高" selected="">最高</option>
					<option value="较高">较高</option>
					<option value="普通">普通</option>
					<option value="低">低</option>
				</select>
				<input type="text" class="input_text" />
				<input type="button" class="input_button" value="快速查询"/>
			</div>
		</div>
		<div class="report_description_top clearfix"><span>指标说明</span><input type="button" class="input_button fr" value="下载指标说明"/><input name=Button onClick=document.all.WebBrowser.ExecWB(4,1) type=button value=另存为><OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WebBrowser width=0></OBJECT> 
		</div>
		
		<div class="report_description">
			<%--
			<ul class="report_description_tree">
				<li><a title="运维生产统计分析指标" id="运维生产统计分析" href="javascript:void(0);">运维生产统计分析指标</a>
					<ul class="">
						<li><a title="故障抢修指标"  id="故障抢修" href="javascript:void(0);">故障抢修指标</a>
							<ul class="">
								<li><a title="故障抢修工单统计分析报表" id="故障抢修工单统计分析报表" href="javascript:void(0);">故障抢修工单统计分析报表</a>
									<ul class="">
										<li><a href="#">【指标描述】：</br>
												1、展示不同行政区工单数量对比情况。</br>
												2、展示不同部门工单数量对比情况。</br>
												3、展示不同故障类型工单数量对比情况。</br>
												4、展示不同级别故障工单数量对比情况。</br>
												5、展示同一部门工单数量变化趋势。</br>
											</a></li>
										<li><a href="#">【指标公式】：工单总数</a></li>
										<li><a href="#">【指标描述】：考核项目组、维护队的故障处理效率。</a></li>
										<li><a href="#">【指标公式】：∑（【故障消除时间】-【故障发生时间】）/工单总数</a></li>
										<li><a href="#">【指标描述】：故障抢修工单从创建到处理完成所经历的时长，通过该指标可以考核维护队故障处理的工作效率。故障抢修工单从创建到处理完成所经历的时长，通过该指标可以考核维护队故障处理的工作效率。</a></li>
										<li><a href="#">【指标公式】：工单处理平均历时  =  ∑（工单最终回复环节【完成时间】-【创建时间】） / 工单总数</a></li>
									</ul>
								</li>
								<li><a title="工单数量" id="工单数量" href="javascript:void(0);">工单数量</a>
									<ul class="">
										<li><a href="#">【指标描述】：</br>
												1、展示不同行政区工单数量对比情况。</br>
												2、展示不同部门工单数量对比情况。</br>
												3、展示不同故障类型工单数量对比情况。</br>
												4、展示不同级别故障工单数量对比情况。</br>
												5、展示同一部门工单数量变化趋势。</br>
											</a></li>
										<li><a href="#">【指标公式】：工单总数</a></li>
									</ul>
								</li>
								<li><a title="工单处理历时" id="工单处理历时"  href="javascript:void(0);">工单处理历时</a>
									<ul class="">
										<li><a href="#">【指标描述】：考核项目组、维护队的故障处理效率。</a></li>
										<li><a href="#">【指标公式】：∑（【故障消除时间】-【故障发生时间】）/工单总数</a></li>
									</ul>
								</li>
								<li><a title="工单处理平均历时" id="工单处理平均历时" href="javascript:void(0);">故障处理及时率</a>
									<ul class="">
										<li><a href="#">【指标描述】：故障抢修工单从创建到处理完成所经历的时长，通过该指标可以考核维护队故障处理的工作效率。故障抢修工单从创建到处理完成所经历的时长，通过该指标可以考核维护队故障处理的工作效率。</a></li>
										<li><a href="#">【指标公式】：工单处理平均历时  =  ∑（工单最终回复环节【完成时间】-【创建时间】） / 工单总数</a></li>
									</ul>
								</li>
							</ul>
						</li>
						<li><a title="巡检作业" id="巡检作业" href="javascript:void(0);">巡检作业</a>
						</li>
					</ul>
				</li>
				<li><a title="网络资源统计分析指标" id="网络资源类指标" href="javascript:void(0);">网络资源类指标</a>
				</li>
				<li><a title="生产资源统计分析指标" id="生产资源统计分析" href="javascript:void(0);">生产资源统计分析指标</a>
					<ul class="">
						<li><a title="人员调度指标" id="人员调度" href="javascript:void(0);">人员调度指标</a>
							<ul class="">
								<li><a title="人员数量" id="人员数量" href="javascript:void(0);">人员数量</a>
									<ul class="">
										<li><a href="#">【指标描述】：</br>
												1、分部门统计资源数量。</br>
												2、统计各部门资源数量变化趋势。</br>
												3、分技能统计资源数量。</br>
												4、分角色统计资源数量。</br>
											</a></li>
										<li><a href="#">【指标公式】：人员总数</a></li>
									</ul>
								</li>
							</ul>
						</li>
						<li><a title="车辆调度指标" id="车辆调度" href="javascript:void(0);">车辆调度指标</a>
							<ul class="">
								<li><a title="车辆数量" id="车辆数量" href="javascript:void(0);">车辆数量</a>
									<ul class="">
										<li><a href="#">【指标描述】：</br>
												1、分部门统计资源数量。</br>
												2、统计各部门资源数量变化趋势。</br>
											</a></li>
										<li><a href="#">【指标公式】：车辆总数</a></li>
									</ul>
								</li>
								<li><a title="车辆费用" id="车辆费用" href="javascript:void(0);">车辆费用</a>
									<ul class="">
										<li><a href="#">【指标描述】：</br>
												1、展示不同费用类型行车费用对比情况。</br>
												2、展示不同车型车辆行车费用对比情况。</br>
											</a></li>
										<li><a href="#">【指标公式】：工单总数</a></li>
									</ul>
								</li>
							</ul>
						</li>
						<li><a title="巡检作业" id="巡检作业" href="javascript:void(0);">∑车辆行车费用</a>
						</li>
					</ul>
				</li>
			</ul>
			--%>
			<ul id="reportTree" class="filetree">
				<li><span class="folder">运维生产类指标</span>
					<ul>
						<li><span class="folder">故障抢修</span>
							<ul>
								<li><span class="zhibiao">工单数量</span>
									<ul>
										<li><span class="file">【指标描述】：工单数量指标描述</span></li>
										<li><span class="file">【指标公式】：工单数量指标公式</span></li>
									</ul>
								</li>
								<li><span class="zhibiao">处理历时</span>
									<ul>
										<li><span class="file">【指标描述】：处理历时指标描述</span></li>
										<li><span class="file">【指标公式】：处理历时指标公式</span></li>
									</ul>
								</li>
								<li><span class="zhibiao">处理及时率</span>
									<ul>
										<li><span class="file">【指标描述】：处理及时率指标描述</span></li>
										<li><span class="file">【指标公式】：处理及时率指标公式</span></li>
									</ul>
								</li>
							</ul>
						</li>
					</ul>
				</li>
				<li><span class="folder">资源类指标</span>
					<ul>
						<li><span class="folder">网络资源</span>
							<ul>
								<li><span class="zhibiao">站址数量</span>
									<ul>
										<li><span class="file">【指标描述】：站址数量指标描述</span></li>
										<li><span class="file">【指标公式】：站址数量指标公式</span></li>
									</ul>
								</li>
								<li><span class="zhibiao">基站数量</span>
									<ul>
										<li><span class="file">【指标描述】：基站数量指标描述</span></li>
										<li><span class="file">【指标公式】：基站数量指标公式</span></li>
									</ul>
								</li>
							</ul>
						</li>
						<li><span class="folder">车辆资源</span>
							<ul>
								<li><span class="zhibiao">车辆数量</span>
									<ul>
										<li><span class="file">【指标描述】：车辆数量指标描述</span></li>
										<li><span class="file">【指标公式】：车辆数量指标公式</span></li>
									</ul>
								</li>
								<li><span class="zhibiao">行车费用</span>
									<ul>
										<li><span class="file">【指标描述】：行车费用指标描述</span></li>
										<li><span class="file">【指标公式】：行车费用指标公式</span></li>
									</ul>
								</li>
							</ul>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</body>
</html>