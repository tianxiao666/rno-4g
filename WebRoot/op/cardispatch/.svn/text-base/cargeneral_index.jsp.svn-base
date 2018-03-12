<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>车辆统一信息查看</title>
		<%-- 基本css --%>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" href="css/calendar_css.css" type="text/css" />
		<link rel="stylesheet" href="css/cargeneral.css" type="text/css"></link>
		<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
		<%-- 基本js --%>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../js/public.js"></script>
		<script type="text/javascript"src="../../jslib/jquery/jquery.treeview.js"></script>
		<script type="text/javascript" src="../../jslib/common.js"></script>
		<script type="text/javascript" src="../jslib/generateTree.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
		<script type="text/javascript" src="js/util/input.js"></script>
		<script type="text/javascript" src="../../jslib/date/date.js"></script>
		
		<%-- 控件 --%>
		<script type="text/javascript" src="js/util/urlutil.js"></script>
		<script type="text/javascript" src="js/util/dateutil.js"></script>
		<script type="text/javascript" src="js/util/objutil.js"></script>
		<script type="text/javascript" src="js/tool/showedit.js"></script>
		<script type="text/javascript" src="js/tool/tablePage.js"></script>
		<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
		<script type="text/javascript" src="js/class/car.js"></script>
		<script type="text/javascript" src="js/class/driver.js"></script>
		<script type="text/javascript" src="js/class/terminal.js"></script>
<style>
	.select_button {background: url(images/select.png) no-repeat scroll;display: inline-block;height: 24px;margin:2px 0 0 -31px;position: absolute;width: 24px;}
</style>
<%-- 页面功能 --%>
<script type="text/javascript">

		var carId = "";
		var isView = "";
		
		
		$(function(){
			carId = "${param.carId}";
			isView = "${param.type}";
			$("#carId").val(carId);
			
			if ( isView ) {
				isView = true;
			} else {
				isView = false;
			}
			//验证
			formcheck({
				"form" : $("#carForm") , 
				"subButton" : $("#sbtn") , 
				"isAjax" : true , 
				"ajaxSuccess" : function( data ){
					alert("操作成功");
				}
			});
			$("#carForm .formcheckspan").attr("editButton","true");
			showedit("#car_div");
		
			if ( isView ) {
				$("#updateBtn").hide();
				$("#driver_change_btn").hide();
				$("#mobile_change_btn").hide();
			}
			$(".tab_menu ul li").each(function(index){
				$(this).click(function(){
					$(".tab_menu ul li").removeClass("ontab");
					$(this).addClass("ontab");
					$(".tab_content").hide();
					$(".tab_content").eq(index).show();
					if ( index == 3 ) {
						calendar.refreshCalendar();
					}
				})
			})
			
			/*tab里面的tab*/
			$(".tab_inside_ul ul li").each(function(index){
				$(this).click(function(){
					$(".tab_inside_ul ul li").removeClass("tab_inside_ontab");
					$(this).addClass("tab_inside_ontab");
					$(".tab_inside_content").hide();
					$(".tab_inside_content").eq(index).show();
				})
			})
			
			/*
			/*input样式*/
			$(":button,:submit").mousedown(function(){
				$(this).addClass("input_button_down");
			});
			$(":button,:submit").mouseup(function(){
				$(this).removeClass("input_button_down");
			});
			
			
			/*弹出alert_div*/
			//更换司机
			$("#driver_change_btn").click(function(){
				$("#driver_div_table tbody").empty();
				$("#driver_alert").fadeIn(200,function(){
					var w = $("#driver_name");
					var left = $(w).offset().left + 3;
					var top = $(w).offset().top+5;
					var height = $(w).height() ;
					$("#driver_name_div").css({
						"height" : height+"px" , 
						"font-size" : ( height - 1 )+"px" , 
						"line-height" : height+"px" , 
						"margin-top" : "5px"
					});
					$(w).blur();
				});
				$("#driver_name").val("");
				driverTablePage.setDataArray(new Array());
				driverTablePage.refreshTable();
				driverTablePage.checkButton();
			})
			
			/*弹出alert_div*/
			//更换终端
			$("#mobile_change_btn").click(function(){
				$("#mobile_div_table tbody").empty();
				$("#mobile_alert").fadeIn(200,function(){
					var w = $("#mobile_phone");
					var left = $(w).offset().left + 3;
					var top = $(w).offset().top+5;
					var height = $(w).height() - 1 ;
					$("#mobile_phone_div").css({
						"height" : height+"px" , 
						"font-size" : ( height - 1 )+"px" , 
						"line-height" : height+"px" , 
						"margin-top" : "5px"
					});
					$(w).blur();
				});
				$("#mobile_phone").val("");
				mobileTablePage.setDataArray(new Array());
				mobileTablePage.refreshTable();
				mobileTablePage.checkButton();
				
				$.ajax({
					"url" : "cardispatchManage_ajax!findMobileBindingList.action" , 
					"type" : "post" ,      
					"data" : { "isFree" : "true" , "terminal#telphoneNo" : $("#mobile_phone").val() , "terminal#terminalBizId" : $("#mobile_bizId").val() , "terminal#mobileType" : $("#mobileType").val() } , 
					"success" : function( result ) {
									result = eval ( result );
									mobileTablePage.setDataArray(result);
									mobileTablePage.refreshTable();
									mobileTablePage.checkButton();
								} 
				});
				
				
			})
			/*关闭alert_div*/
			$(".close_alert_div").click(function(){
				$(this).parents(".alert_div").fadeOut(100);
				$("#driver_name_div").css({"display":"none"});
				$("#mobile_phone_div").css({"display":"none"});
			});
			promptCueDiv($("#driver_name"),"请输入司机姓名");
			promptCueDiv($("#mobile_phone"),"请输入终端imei");
		})
</script>


	</head>

	<body>

		<div class="p_container">
			<%--主体开始--%>
			<div class="pi_right">
				<div class="pi_menu_title clearfix">
					<p class="fl">
						车辆统一信息查看
					</p>
				</div>

				<%-- content开始 --%>
				<div class="pi_right_content">
					<div class="tab1">
						<div class="tab_menu">
							<ul>
								<li class="ontab">
									基本信息
								</li>
								<li>
									用车任务
								</li>
								<li>
									行车费用
								</li>
								<li>
									排班表
								</li>
							</ul>
						</div>

						<div class="tab_container">

							<%-- 车辆基本信息 --%>
							<div class="tab_content" id="car_info_div">
								<%@ include file="cargeneral_carInfo.jsp"%>
							</div>

							<div class="tab_content" style="display: none;" id="task_info_div">
								<%@ include file="cargeneral_task.jsp"%>
							</div>

							<div class="tab_content" style="display: none;" id="pay_info_div">
								<%@ include file="cargeneral_pay.jsp"%>
							</div>

							<div class="tab_content" style="display: none;">
								<%@ include file="cargeneral_linkCarDutyPage.jsp"%>
							</div>
						</div>
					</div>
					<%-- content结束 --%>
				</div>
				<%--主体结束--%>
			</div>




			<%-- 司机弹出窗口开始 --%>
			<div class="alert_div" id="driver_alert">
				<div class="alert_div_cover"></div>
				<div class="alert_div_container">
					<div class="alert_div_top">
						<div class="alert_div_top_middle">
							<div style="float: left;">
								司机选择
							</div>
							<div class="alert_div_close close_alert_div"></div>
						</div>
					</div>
					<div class="alert_div_main">
						<h4 class="content_m_tit">
							<span class="right_tool_bar">  
								<input type="hidden" id="driver_bizId" />
								<input type="text" id="driver_bizName" /><a class="areaButton selectWorkPlaceButton" id="driver_biz_btn" title="选择组织"></a>
								<div id="driver_treeDiv">
									<%-- 放置组织架构树 --%>
								</div>
								<select id="driver_license_type">
									<option value="null">
										驾驶照类型
									</option>
									<option value="A牌">
										A牌
									</option>
									<option value="B牌">
										B牌
									</option>
									<option value="C牌">
										C牌
									</option>
								</select>
								<input type="text" id="driver_name" value="" />
								<a class="search_button" id="driver_search_btn"></a>
							</span>
						</h4>
						<div class="table_div" id="driver_dialog_div">
							<table class="pi_table2 tc hover_tr" id="driver_div_table">
								<thead>
									<tr>
										<th>
											司机姓名
										</th>
										<th>
											所属组织
										</th>
										<th>
											联系方式
										</th>
										<th>
											驾驶照
										</th>
										<th>
											操作
										</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<%-- 司机弹出窗口结束 --%>



			<%-- 终端弹出窗口开始 --%>
			<div class="alert_div" id="mobile_alert">
				<div class="alert_div_cover"></div>
				<div class="alert_div_container">
					<div class="alert_div_top">
						<div class="alert_div_top_middle">
							<div style="float: left;">
								终端选择
							</div>
							<div class="alert_div_close close_alert_div"></div>
						</div>
					</div>
					<div class="alert_div_main">
						<h4 class="content_m_tit">
							<span class="right_tool_bar">
								<input type="hidden" id="mobile_bizId" />
								<input type="text" id="mobile_bizName" /><a class="areaButton selectWorkPlaceButton" id="mobile_biz_btn" title="选择组织"></a>
								<div id="mobile_treeDiv">
									<%-- 放置组织架构树 --%>
								</div> 
								<select id="mobileType">
									<option value="null">
										型号
									</option>
									<option value="gt02">
										gt02
									</option>
									<option value="GT02A">
										gt02a
									</option>
									<option value="gt03">
										gt03
									</option>
									<option value="gt06">
										gt06
									</option>
									
								</select>
								<input type="text" id="mobile_phone" value="" />
								<a class="search_button" id="mobile_search_btn"></a>
							</span>
						</h4>
						<div class="table_div" id="mobile_dialog_div">
							<table class="pi_table2 tc hover_tr" id="mobile_div_table">
								<thead>
									<tr>
										<th>
											型号
										</th>
										<th>
											所属组织
										</th>
										<th>
											IMEI码
										</th>
										<th>
											手机号码
										</th>
										<th>
											操作
										</th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<%-- 终端弹出窗口结束 --%>
	</body>
</html>
