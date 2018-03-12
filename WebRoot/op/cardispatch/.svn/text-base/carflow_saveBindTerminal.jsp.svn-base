<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%-- 基本css --%>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css"
			href="../../jslib/jquery/css/jquery.treeview.css"></link>
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" href="css/carflow_saveTerminal.css"
			type="text/css" />
		<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
		<%-- 基本js --%>
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js">
</script>
		<script type="text/javascript" src="../../js/public.js">
</script>
		<script type="text/javascript"
			src="../../jslib/jquery/jquery.treeview.js">
</script>
		<script type="text/javascript" src="../../jslib/common.js">
</script>
		<script type="text/javascript" src="../jslib/generateTree.js">
</script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js">
</script>
		<script type="text/javascript" src="js/util/input.js">
</script>
		<script type="text/javascript" src="../../jslib/date/date.js">
</script>

		<%-- 控件 --%>
		<script src="js/tool/new_formcheck.js">
</script>
		<script src="js/tool/tablePage.js">
</script>
		<script src="js/util/urlutil.js">
</script>
		<script type="text/javascript" src="js/class/car.js">
</script>
		<script type="text/javascript" src="js/class/driver.js">
</script>
		<script type="text/javascript" src="js/class/terminal.js">
</script>
		<script>
var driverId = "";
var bizunitInstanceId = "";
var carId = "";
$(document).ready(function() {
	driverId = "${param.driverId}";
	bizunitInstanceId = "${param.bizunitInstanceId}";
	carId = "${param.carId}";
})
</script>

		<script type="text/javascript"
			src="js/obj/carflow_saveTerminal_obj.js">
</script>
		<script type="text/javascript" src="js/carflow_saveTerminal.js">
</script>

		<title>车辆管理流程3</title>
	</head>

	<body>
		<input type="hidden" value="${param.carId}" name="carId" />
		<input type="hidden" value="${param.driverId}" name="driverId" />
		<div id="container">

			<div class="top_number"></div>

			<div class="main">
				<div class="container-main-table1"
					style="margin: 30px auto; width: 80%; overflow: hidden;">
					<%-- 车辆信息 --%>
					<div class="info_div">
						<div class="content_left" style="margin-right: 10px;">
							<div id="aa"
								style="height: 190px; width: 170px; text-align: center;">
								<div style="height: 190px; width: 170px;">
									<img id="car_info_left_img" column="car#carPic" width="170"
										height="190" editImg="true" />
									<br />
								</div>
							</div>
						</div>
						<div class="content_right">
							<table class="pi_table">
								<tr class="main-table1-tr">
									<td colspan="4" class="main-table1-title">
										<span class="doc_tit">车辆信息</span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										车牌号码：
									</td>
									<td>
										<span column="car#carNumber"></span>
									</td>
									<td class="menuTd">
										车辆类型：
									</td>
									<td>
										<span column="car#carType"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										所属区域：
									</td>
									<td>
										<span column="car#carBizName"></span>
									</td>
									<td class="menuTd">
										载客数量：
									</td>
									<td>
										<span column="car#passengerNumber"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										车龄：
									</td>
									<td>
										<span column="car#carAge"></span>
									</td>
									<td class="menuTd">
										车辆型号：
									</td>
									<td>
										<span column="car#carModel"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										租凭费用：
									</td>
									<td>
										<span column="car#leasePay"></span>
									</td>
									<td class="menuTd">
										保管费用：
									</td>
									<td>
										<span column="car#custodyFee"></span>
									</td>
								</tr>

								<tr>
									<td class="menuTd">
										车辆厂家：
									</td>
									<td>
										<span column="car#carMarker"></span>
									</td>
									<td class="menuTd">
										车载重量：
									</td>
									<td>
										<span column="car#loadWeight"></span>
									</td>
								</tr>
							</table>
						</div>
					</div>

					<%-- 司机信息 --%>
					<div class="info_div">
						<div class="content_left" style="margin-right: 10px;">
							<div id="aa"
								style="height: 190px; width: 170px; text-align: center;">
								<div style="height: 190px; width: 170px;">
									<img id="driver_info_left_img" column="driver#driverPic"
										width="170" height="190" editImg="true" />
									<br />
								</div>
							</div>
						</div>
						<div class="content_right">
							<table class="pi_table">
								<tr class="main-table1-tr">
									<td colspan="4" class="main-table1-title">
										<span class="doc_tit">司机信息</span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										司机姓名：
									</td>
									<td>
										<span column="driver#driverName"></span>
										<input id="driverId" type="hidden" column="driver#driverId" />
									</td>
									<td class="menuTd">
										电话号码：
									</td>
									<td>
										<span column="driver#driverPhone"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										年龄：
									</td>
									<td>
										<span column="driver#driverAge"></span>
									</td>
									<td class="menuTd">
										身份证号：
									</td>
									<td>
										<span column="driver#identificationId"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										所属区域：
									</td>
									<td>
										<span column="driver#driverBizName"></span>
									</td>
									<td class="menuTd">
										IT帐号：
									</td>
									<td>
										<span column="driver#accountId"></span>
									</td>
								</tr>

								<tr>
									<td class="menuTd">
										驾照类型：
									</td>
									<td>
										<span column="driver#driverLicenseType"></span>
									</td>
									<td class="menuTd">
										住址：
									</td>
									<td>
										<span column="driver#driverAddress"></span>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>

				<%-- 终端 --%>
				<div class="container-main-table1"
					style="width: 80%; margin: 0px auto;">
					<div class="tab_1">
						<ul>
							<li class="ontab" style="background: none; border: none;">
								<input type="radio" class="input_radio" name="sijixuanze" />
								选择车载终端
							</li>
							<li style="background: none; border: none;">
								<input type="radio" class="input_radio" name="sijixuanze" />
								添加车载终端
							</li>
						</ul>
					</div>
					<div class="tab_content" id="mobile_table">
						<form id="form"
							action="cardispatchManage!bindCarAndTerminalAjax.action"
							method="post">
							<input type="hidden" class="input_text" id="terminalId"
								column="terminal#terminalId" name="terminalId" />
							<input type="hidden" class="input_text" id="carId" name="carId"
								value="${param.carId}" />
							<input type="hidden" id="carBizId" name="terminal#terminalBizId	"
								value="${param.bizunitInstanceId}" />
						</form>
						<div class="content_m">
							<h4 class="content_m_tit">
								<span class="doc_tit">车载终端信息</span>
								<span class="fr mt2"><input type="button"
										id="mobile_change_btn" value="更换终端" /> </span>
							</h4>
						</div>
						<div class="content_left" style="margin-right: 10px;">
							<div id="aa"
								style="height: 190px; width: 170px; text-align: center;">
								<div style="height: 190px; width: 170px;">
									<img id="mobile_img" src="" width="170" height="190"
										editImg="true" />
									<br />
								</div>
							</div>
						</div>
						<div class="content_right">
							<table class="pi_table">
								<tr>
									<td class="menuTd">
										终端类型：
									</td>
									<td>
										<span column="terminal#mobileType"></span>
									</td>
									<td class="menuTd">
										终端IMEI码：
									</td>
									<td>
										<span column="terminal#clientimei"></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										终端SIM卡号：
									</td>
									<td>
										<span column="terminal#telphoneNo"></span>
									</td>
									<td class="menuTd">
										名称：
									</td>
									<td>
										<span column="terminal#terminalName"></span>
									</td>
								</tr>
								
								<tr>
									<td class="menuTd">
										开通时间：
									</td>
									<td>
										<span column="terminal#launchedTime"></span>
									</td>
									<td class="menuTd">
										到期时间：
									</td>
									<td>
										<span column="terminal#expiredTime"></span>
									</td>
								</tr>
								
								<tr>
									<td class="menuTd">
										所属组织：
									</td>
									<td>
										<span column="terminal#terminalBizName"></span>
									</td>
								</tr>
								<tr>
									<td colspan="4" style="text-align: center;">
										<input type="button" class="input_button prevPageBtn"
											value="上一步" />
										&nbsp;
										<input type="button" id="bindBtn" class="input_submit"
											value="完成" />
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="tab_content"
						style="display: none; overflow: hidden; width: auto;">
						<form id="mobile_img_form" name="img_form"
							action="cardispatchCommon_ajax!getFileURL.action" method="post">

							<div class="content_left" style="margin-right: 10px;">
								<div editDiv="true" id="aa"
									style="height: 200px; width: 170px; text-align: center;">
									<div style="width: 170px; height: 170px;">
										<img class="content_left_img" id="mobile_info_left_img" src=""
											width="170" height="170" editImg="true" />
									</div>
									<input type="button" value="选择图片" />
									<input type="file" id="mobileFile"
										style="z-index: 999; position: relative; left: -75px; top: -24px; filter: alpha(opacity =   0); opacity: 0;"
										name="file" />
								</div>
							</div>

						</form>
						<form id="saveBindCMForm"
							action="cardispatchManage!saveTerminalAndBindCarAjax.action"
							method="post" enctype="multipart/form-data">
							<input type="hidden" id="terminalPic" name="terminal#terminalPic" />
							<input type="hidden" name="terminal#terminalBizId"
								column="car#carBizId" />
							<input type="hidden" name="ajax" value="1" />
							<div class="content_right">
								<table class="pi_table">
									<tr class="main-table1-tr">
										<td colspan="4" class="main-table1-title">
											填写车载终端信息
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											终端类型：
										</td>
										<td>
											<select style="width: 120px;" id="add_mobileType"
												name="terminal#mobileType" checknull="终端类型不能为空">
												<option>
													gt02
												</option>
												<option>
													gt03
												</option>
												<option>
													gt06a
												</option>
											</select>
											<input type="hidden" name="terminal#terminalBizId"
												value="${param.bizunitInstanceId}" />
											<input type="hidden" name="carId" value="${param.carId}" />
										</td>
										<td class="menuTd">
											终端号：
										</td>
										<td>
											<input type="text" class="input_text" id="add_clientimei" 
												name="terminal#clientimei" checknull="终端号不能为空" 
												checknumber="终端号格式不规范" 
												checkajax="{'url':'cardispatchManage!checkTerminalImeiIsExists.action','msg':'终端号已经存在'}" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											终端电话(SIM卡号)：
										</td>
										<td>
											<input type="text" class="input_text" id="add_simCard" 
												name="terminal#telphoneNo" checknull="SIM卡号不能为空" 
												checkphone="SIM卡格式不规范"
												checkajax="{'url':'checkTerminalImeiIsExists','msg':'SIM卡号已经存在'}" />
										</td>
										<td class="menuTd">
											名称：
										</td>
										<td>
											<input type="text" class="input_text"
												name="terminal#terminalName" checkopera="存在特殊字符" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											开通时间：
										</td>
										<td>
											<input type="text" class="input_text"
												name="terminal#launchedTime" id="launchedTime"
												readonly="readonly" />
											<input type="button" class="input_button" value="选择时间"
												onclick="fPopCalendar(event,document.getElementById('launchedTime'),document.getElementById('launchedTime'),true)" />
										</td>
										<td class="menuTd">
											到期时间：
										</td>
										<td>
											<input type="text" class="input_text"
												name="terminal#expiredTime" id="expiredTime"
												readonly="readonly" />
											<input type="button" class="input_button" value="选择时间"
												checkdatecompare="{'msg':'必须大于开通时间','compare':'moreThan','target':'#expiredTime','comparetarget':'#launchedTime'}"
												onclick="fPopCalendar(event,document.getElementById('expiredTime'),document.getElementById('expiredTime'),true)" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											所属组织：
										</td>
										<td colspan="3">
											<input type="text" class="input_text" readonly="readonly"
												column="car#carBizName" />
										</td>
									</tr>
									<tr>
										<td colspan="4" style="text-align: center;">
											<input type="button" class="input_button prevPageBtn"
												value="上一步" />
											&nbsp;
											<input type="button" class="input_submit" value="完成"
												id="mobileAddBindBtn" />
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>



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
						<span class="right_tool_bar"> <input type="text"
								id="mobile_bizName" /> <input type="hidden" id="mobile_bizId" />
							<a href="javascript:void(0)" id="mobile_biz_btn"></a>
							<div id="mobile_treeDiv">
								<%-- 放置组织架构树 --%>
							</div> <select id="mobileType">
								<option value="null">
									类型
								</option>
								<option value="gt02a">
									gt02a
								</option>
								<option value="gt03a">
									gt03a
								</option>
								<option value="gt06">
									gt06
								</option>
							</select> <input type="text" id="mobile_phone" value="" /> <a
							href="javascript:void(0);" class="search_button"
							id="mobile_search_btn"></a> </span>
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



