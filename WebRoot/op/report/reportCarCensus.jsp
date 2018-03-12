<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>车辆数量统计报表</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
		<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
		<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>
		<link rel="stylesheet" type="text/css" href="css/statements.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
		<style>
			#preBiz_btn{border-radius:20px;}
			.li {background-attachment: scroll;
			    background-clip: border-box;
			    background-color: #EEEEEE; 
			    background-image: none;
			    background-origin: padding-box;
			    background-position: 0 0;
			    background-repeat: repeat;
			    background-size: auto auto;
			    border-left-color-ltr-source: physical;
			    border-left-color-rtl-source: physical;
			    border-left-color-value: #EEEEEE;
			    border-left-style-ltr-source: physical;
			    border-left-style-rtl-source: physical;
			    border-left-style-value: solid;
			    border-left-width-ltr-source: physical;
			    border-left-width-rtl-source: physical;
			    border-left-width-value: 1px;
			    border-right-color-ltr-source: physical;
			    border-right-color-rtl-source: physical;
			    border-right-color-value: #CCCCCC;
			    border-right-style-ltr-source: physical;
			    border-right-style-rtl-source: physical;
			    border-right-style-value: solid;
			    border-right-width-ltr-source: physical;
			    border-right-width-rtl-source: physical;
			    border-right-width-value: 1px;
			    border-top-color: #CCCCCC;
			    border-top-style: solid;
			    border-top-width: 1px;
			    cursor: pointer;
			    float: left;
			    line-height: 23px;
			    padding-bottom: 0;
			    padding-left: 3px;
			    padding-right: 3px;
			    padding-top: 0;
			    position: relative;
			    text-align: center;
			    width: 70px;
			    margin-left: 78px;
		}
		.lititle{-moz-border-bottom-colors: none;
			    -moz-border-left-colors: none;
			    -moz-border-right-colors: none;
			    -moz-border-top-colors: none;
			    background-color: #EEEEEE;
			    border-color: #99BBE8;
			    border-style: solid;
			    border-width: 1px;
			    border-image-outset: 0 0 0 0;
			    border-image-repeat: stretch stretch;
			    border-image-slice: 100% 100% 100% 100%;
			    border-image-source: none;
			    border-image-width: 1 1 1 1;
			    border-color-ltr-source: physical;
			    border-color-rtl-source: physical;
			    border-color-value: #99BBE8;
			    border-style-ltr-source: physical;
			    border-style-rtl-source: physical;
			    border-style-value: solid;
			    border-width-ltr-source: physical;
			    border-width-rtl-source: physical;
			    border-width-value: 1px;
			    border-color-ltr-source: physical;
			    border-color-rtl-source: physical;
			    border-color-value: #99BBE8;
			    border-style-ltr-source: physical;
			    border-style-rtl-source: physical;
			    border-style-value: solid;
			    border-width-ltr-source: physical;
			    border-width-rtl-source: physical;
			    border-width-value: 1px;
			    border-top-color: #99BBE8;
			    border-top-style: solid;
			    border-top-width: 1px;
			    cursor: pointer;
			    float: left;
			    font-weight: bold;
			    line-height: 24px;
			    padding-bottom: 0;
			    padding-left: 3px;
			    padding-right: 3px;
			    padding-top: 0;
			    position: relative;
			    text-align: center;
			    width: 100px;
		}	    
		</style>
		
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
		<script type="text/javascript" src="../js/leftMenu.js"></script>
		<script type="text/javascript" src="js/statements.js"></script>
		<script type="text/javascript" src="../js/tab.js" ></script>
		<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
		<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
		<script type="text/javascript" src="js/report.js"></script>
		<script type="text/javascript" src="../cardispatch/js/util/dateutil.js"></script>
		<script type="text/javascript" src="../cardispatch/js/util/objutil.js"></script>
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
				tab("compareTab","li","onclick");//组织类别切换
				
			})
		</script>
		
		<script>
			var defOrgId = "16"; 
			var defOrgName = "";
			var preOrgId = "16";
			var preOrgName = "";
			var thisOrgId = "";
			var thisOrgName = "";
			var chart = null;
			var orgId_info = {};
			var orgTitle = "车辆数量统计(" + "怡创科技" + ")";
			
			$(document).ready(function(){
				//报表放大按钮
				$("#container_big").click(function(){
					addDivChar($("#container_copy_big"));
				}); 
				$("#container_project_big").click(function(){
					addDivChar($("#container_project_copy_big"));
				});
				
				$("#cover_div").css({"display":"none"});
				
				$.ajax({
					"url" : "carCensusReport_ajax!getLoginUserBizId.action" , 
					"type" : "post" , 
					"async" : true , 
					"success" : function( data ){
						data = eval("(" + data + ")");
						defOrgId = data.orgId;
						defOrgName = data.name;
						thisOrgId = defOrgId;
						thisOrgName = defOrgName;
						preOrgId = defOrgId;
						preOrgName = defOrgName;
						orgTitle = "车辆数量统计(" + defOrgName + "," + $(".time_tool_main").text() + ")";
						createReportByBiz(defOrgId);
					}
				});
				//初始化时间
				now = new Date();
				now.addMonths(-1);
				year = now.getFullYear();
				month = now.getMonth() + 1 ;
				$("#year_span").text(year);
				$("#month_span").text(month);
				
				$("#statements_back").click(function(){
					if ( thisOrgId == preOrgId ) {
						alert("已无上级组织");
						return;
					}
					thisOrgName = preOrgName;
					thisOrgId = preOrgId;
					orgTitle = "车辆数量统计(" + preOrgName + "," + $(".time_tool_main").text() + ")";
					createReportByBiz(thisOrgId);
					//查询上级组织
				});
				$(".time_tool_left").click(function(){
					now.addMonths(-1);
					year = now.getFullYear();
					month = now.getMonth()+1;
					$("#year_span").text(year);
					$("#month_span").text(month);
					createReportByBiz(thisOrgId);
				});
				$(".time_tool_right").click(function(){
					now.addMonths(1);
					year = now.getFullYear();
					month = now.getMonth()+1;
					$("#year_span").text(year);
					$("#month_span").text(month);
					createReportByBiz(thisOrgId);
				});
				$("#reload_contrast_biz").click(function(){
					orgTitle = "车辆数量统计(" + defOrgName + "," + $(".time_tool_main").text() + ")";
					createReportByBiz(defOrgId);
				});
				
				$("#reload_contrast_project").click(function(){
					createReportByProject();
				});
			})
			
			//按组织id,查询车辆信息集合
			function createReportByBiz ( orgId ) {
				if ( !orgId || orgId == "" ) {
					orgId = defOrgId;
				}
				showCover();
				$.ajax({
					"url" : "carCensusReport_ajax!findPreOrgByNextOrgId.action" , 
					"type" : "post" , 
					"data" : { "orgId" : thisOrgId } , 
					"success" : function ( result ) {
						if ( result ) {
							var data = eval( "(" + result + ")");
							preOrgId = data["id"];
							preOrgName = data["name"];
						}
					}
				});
				$.ajax({
					"url" : "carCensusReport_ajax!censusCarCountByBizAction.action" , 
					"type" : "post" , 
					"data" : { "carBizId" : orgId } , 
					"success" : function ( result ) {
						orgId_info = {};
						var data = eval( "(" + result + ")");
						var columnName = [];
						var obj = { "name" : "车辆数量" , "data" : [] };
						var carCountArr = [];
						for ( var key in data ) {
							columnName.push(key);
							var info = data[key];
							for ( var key_info in info ) {
								if ( key_info == "count" ) {
									carCountArr.push(info[key_info]);
								} else if ( key_info == "info" ) {
									orgId_info[key] = info[key_info];
								}
							}
						}
						obj["data"] = carCountArr;
						obj = [obj];
						//yAxisTitle - y提示
						//容器id
						//X标题数组
						//数据
						columnBasic(orgTitle,"(点击图形区域,可向下钻取)","车辆数量(辆)","container",columnName,obj);
						columnBasic(orgTitle,"(点击图形区域,可向下钻取)","车辆数量(辆)","container_copy_big",columnName,obj);
						//报表评论
						getReportComment("carCountReportComment", "org", orgId, null);
						checkOrgBackBtnIsShow();
						hideCover();
					}
				});
				
			}
			
			function clickShowMore(key,da1){
				var orgInfo = orgId_info[key];
				if ( !!orgInfo["isLast"] ) {
					alert("没有下级组织");
				} else {
					orgTitle = "车辆数量统计(" + orgInfo["name"] + "," + $(".time_tool_main").text() + ")";
					thisOrgId = orgInfo["id"];
					thisOrgName = orgInfo["name"];
					createReportByBiz(orgInfo["id"]);
				}
			}
			
			function clickGetUrgentRepairworkorderReport() {
				getReportComment("carReportComment", "org", thisOrgId, null);
			} 
			
			function showCover () {
				if ( $("#cover_div").is(":visible") ) {
					return;
				}
				var width = $("#statements_right_div").width();
				var height = $("#statements_right_div").height();
				var left = $("#statements_right_div").offset().left;
				var top = $("#statements_right_div").offset().top;
				$("#cover_div").css({
					"width" : width + "px" , 
					"height" : height + "px" , 
					"top" : top + "px" , 
					"left" : left + "px"
				});
				$("#cover_back_div").css({
					"width" : width + "px" , 
					"height" : height + "px" , 
					"top" : 0 + "px" , 
					"left" : 0 + "px" , 
					"background-color" : "white" , 
					"opacity" : 0.8
				});
				var loadingLeft = width / 2 - $("#cover_loading_div").width() / 2 ;
				var loadingTop = height / 2 - $("#cover_loading_div").height() / 2 ;
				$("#cover_loading_div").css({
					"top" : loadingTop + "px" , 
					"left" : loadingLeft + "px" 
				});
				$("#cover_div").fadeIn("fast");
				$("#cover_div").css({"display":"block"});
				
			}
			
			function hideCover () {
				$("#cover_div").fadeOut("fast");
			}
			
			
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
			
			//验证返回上级组织按钮是否显示
			function checkOrgBackBtnIsShow () {
				if ( thisOrgId == defOrgId ) {
					 $("#statements_back").hide();
				} else {
					$("#statements_back").show();
				}
			}
		</script>
		
		<%-- 按项目 --%>
		<script>
			function createReportByProject () {
				showCover();
				var time = $("#year_span").text() + "-" + $("#month_span").text() ;
				$.ajax({
					"url" : "carCensusReport_ajax!censusCarCountByProjectAction.action" , 
					"type" : "post" , 
					"data" : { "time" : time } , 
					"success" : function ( result ) {
						orgId_info = {};
						var data = eval( "(" + result + ")");
						var columnName = [];
						var obj = { "name" : "车辆数量" , "data" : [] };
						var carCountArr = [];
						for ( var key in data ) {
							var info = data[key];
							for ( var key_info in info ) {
								if ( key_info == "count" ) {
									carCountArr.push(info[key_info]);
								} else if ( key_info == "info" ) {
									orgId_info[key] = info[key_info];
									var cname = info[key_info]["NAME"];
									columnName.push(cname);
								}
							}
						}
						obj["data"] = carCountArr;
						obj = [obj];
						//yAxisTitle - y提示
						//容器id
						//X标题数组
						//数据
						
						columnBasic(orgTitle,"(点击图形区域,可向下钻取)","车辆数量(辆)","container_project",columnName,obj);
						columnBasic(orgTitle,"(点击图形区域,可向下钻取)","车辆数量(辆)","container_project_copy_big",columnName,obj);
						//报表评论
						//getReportComment("carCountProjectReportComment", "org", orgId, null);
						checkOrgBackBtnIsShow();
						hideCover();
					}
				});
			}
		</script>
		
		<%-- 放大报表 --%>
		<script>
		   
		   function addDivChar( div ){
		   		var report_statements_full = document.getElementById("report_statements_full_newNode");
			    var chartext = $(div).html();
		   		if(report_statements_full != null){
		   			var newNode = document.getElementById ("report_statements_full_info") ;
		   			newNode.innerHTML = chartext;
		   		} else {
			   		var newNode = document.createElement("div");
			   		newNode.id = "report_statements_full_newNode";
			   		var context = 	"<link rel='stylesheet' type='text/css'" +
									"href='op/report/css/statements.css'>" + 
			   						"<div class='statements_full' id='report_statements_full' style='display: block; z-index: 2000;'>"
										+"<div class='statements_full_info' id='report_statements_full_info'>"
											+chartext
										+"</div>"
										+"<div onclick='window.top.document.body.removeChild(window.top.document.getElementById(\"report_statements_full_newNode\"));' class='statements_full_close'>×</div>"
									+"</div>";
					newNode.innerHTML = context;
					window.top.document.body.appendChild(newNode);
		   		}
		   }
		</script>
	</head>
	
	<body>
		<%--主体开始--%>
		<div id="statements_content">
		    
			
			<%--右边报表开始--%>
			<div class="statements_right" id="statements_right_div">
			    <div class="statements_menu_title clearfix">
				    <p class="fl">车辆数量统计</p>
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
					<span class="time_tool_left"></span>
					<span class="time_tool_main">
						<span id="year_span"></span>年
						<span id="month_span"></span>月
					</span>
					<span class="time_tool_right"></span>
				</div>
				
				<div id="compareTab" class="compareTab_menu">
					<ul class="clearfix">
						<li class="selected">对比</li>
						<li class="lititle" title="不同统计周期的趋势分析(通常是按月)">环比</li>
						<li class="lititle" title="不同年度的同期趋势分析">同比</li>
					</ul>
				</div>
				<div class="compare_info">
					<div id="compareTab_0">
						<div class="statements_tab">
							<div class="tab_menu">
								<ul>
									<li class="ontab first_tab" id="reload_contrast_biz" >按组织</li>
									<li id="reload_contrast_project" >按项目</li>
								</ul>
							</div>
							<div class="tab_container">
								<%-- 按组织 --%>
								<div class="tab_content">
									<%--报表图形--%>
									<div class="statements_select1">
										<span id="statements_back" class="statements_back" style="" title="返回上级"></span>
										<select>
											<option selected="" value="全部">全部</option>
										</select>
									</div>
									<em class="st_full_screen" id="container_big" ></em>
									<div class="statements_img" id="container" >
									</div>
								</div>
								<%-- 按项目 --%>
								<div class="tab_content" style="display:none;">
									<%--报表图形--%>
									<em class="st_full_screen" id="container_project_big" ></em>
									<div class="statements_img" id="container_project" >
									</div>
								</div>
								<div class="statements_table_top" style="height: 26px;" id="statements_table_top">
									<span style="float: left;"><a onclick="showOrHideStationTableDiv(this);"><em>[+]</em>展开表格</a></span>
								</div>
								<%--表单位置--%>
								<div class="statements_main_table" id="stationTableDiv" style="display: none;">
									123
								</div>
								<div id="reportComment"></div>
								<span id="reportType" style="display:none">车辆统计</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<%--右边报表结束--%>
		</div>
		<%--主体结束--%>
		<%-- 遮罩层 --%>
		<div id="cover_div" style="z-index:999; position:absolute; ">
			<div id="cover_back_div" style="position:absolute; "></div>
			<div id="cover_loading_div" style="position:absolute; " ><img id="cover_loading_img" src="image/loading.gif"></img>正在读取数据</div>
		</div>
		
		<%-- 报表放大 --%>
		<div id="container_copy_big" style="height: 500px; width: 1000px; display: none;"></div>
		<div id="container2_copy_big" style="height: 500px; width: 1000px; display: none;"></div>
		<div id="container_project_copy_big" style="height: 500px; width: 1000px; display: none;"></div>
	</body>
</html>
