<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%-- 基本css --%>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
		<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" href="css/carflow_saveDriver.css" type="text/css" />
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
		<script src="js/tool/new_formcheck.js"></script>
		<script src="js/tool/tablePage.js"></script>
		<script src="js/util/urlutil.js"></script>
		<script type="text/javascript" src="js/class/car.js"></script>
		<script type="text/javascript" src="js/class/driver.js"></script>
		<script>
			var carId = null;
			var bizunitInstanceId = null;
			$(document).ready(function(){
				
				carId = ${param.carId};
				bizunitInstanceId = ${param.bizunitInstanceId};
			})
		</script>
		<script type="text/javascript" src="js/obj/carflow_saveDriver_obj.js"></script>
		<script type="text/javascript" src="js/carflow_saveDriver.js"></script>
		
		
		<script type="text/javascript">
			
		</script>
		<title>车辆管理流程2</title>
	</head>

	<body>
		<div id="container">
			<div class="top_number"></div>
			<input type="hidden" value="${param.carId}" id="carId" />
			<input type="hidden" value="${param.bizunitInstanceId}"
				id="bizunitInstanceId" />
			<div class="main">

				<%-- 车辆信息 begin --%>
				<div class="container-main-table1" style="margin: 30px auto; width:80%; overflow:hidden;">
					<div>
						<div class="content_left" style="margin-right: 10px;">
							<div style="height: 195px; width: 170px; text-align: center;">
								<div style="height: 195px; width: 170px;">
									<img id="car_info_left_img" column="car#carPic" width="170"
										height="195" editImg="true" />
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
										<span column="car#carNumber" ></span>
									</td>
									<td class="menuTd">
										车辆类型：
									</td>
									<td>
										<span column="car#carType" ></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										所属区域：
									</td>
									<td>
										<span column="car#carBizName" ></span>
									</td>
									<td class="menuTd">
										载客数量：
									</td>
									<td>
										<span column="car#passengerNumber" ></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										车龄：
									</td>
									<td>
										<span column="car#carAge" ></span>
									</td>
									<td class="menuTd">
										车辆型号：
									</td>
									<td>
										<span column="car#carModel" ></span>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										租凭费用：
									</td>
									<td>
										<span column="car#leasePay" ></span>
									</td>
									<td class="menuTd">
										保管费用：
									</td>
									<td>
										<span column="car#custodyFee" ></span>
									</td>
								</tr>
								
								<tr>
									<td class="menuTd">
										车辆厂家：
									</td>
									<td>
										<span column="car#carMarker" ></span>
									</td>
									<td class="menuTd">
										车载重量：
									</td>
									<td>
										<span column="car#loadWeight" ></span>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>

				<%-- 车辆信息 end --%>

				<%-- 绑定司机信息 --%>
				<div class="container-main-table1" style="margin: 30px auto; width: 80%;">


					<%-- 选择div --%>
					<div class="tab_1">
						<ul>
							<li class="ontab" style="background: none; border: none;">
								<input type="radio" checked="checked" class="input_radio"
									name="sijixuanze" />
								选择司机
							</li>
							<li style="background: none; border: none;">
								<input type="radio" class="input_radio" name="sijixuanze" />
								添加司机
							</li>
						</ul>
					</div>

					<%-- 司机绑定 begin --%>
					<div class="tab_content" style="height: 250px;">
						<form action="cardispatchManage!bindCarAndDriverAjax.action" method="post"
							id="bindCDForm" style="overflow-x: hidden; width: auto;" enctype="multipart/form-data">
							<input type="hidden" value="${param.carId}" name="carId" />
							<input type="hidden" value="${param.bizunitInstanceId}" id="add_bizId" name="driver_bizId" />
							<input type="hidden" class="input_text" name="driverId" column="driver#driverId" value='${param.driverId}' />
								
								
							<div class="content_left">
								<div class="img_div" style="height:190px; width: 170px; text-align: center;">
									<div style="height: 190px; width: 170px;">
										<img id="driver_img" column="driver#driverPic" class="content_left_img"
											src="" width="170" height="190" />
										<br />
									</div>
								</div>
							</div>
							<div class="content_right">
								<table class="pi_table">
									<tr>
										<td colspan="4">
											<div class="content_m">
													<span class="doc_tit">司机信息</span>
													<span class="fr mt2"><input type="button"
															id="driver_change_btn" value="更换司机" class="show_alert_div" />
													</span>
											</div>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											姓名：
										</td>
										<td>
											<span column="driver#driverName" ></span>
										</td>
										<td class="menuTd">
											电话：
										</td>
										<td>
											<span column="driver#driverPhone" ></span>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											身份证：
										</td>
										<td>
											<span column="driver#identificationId"></span>
										</td>
										<td class="menuTd">
											年龄：
										</td>
										<td>
											<span column="driver#driverAge" ></span>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											驾照：
										</td>
										<td>
											<span column="driver#driverLicenseType" ></span>
										</td>
										<td class="menuTd">
											所属组织：
										</td>
										<td class="blue">
											<span column="driver#driverBizName" ></span>
											<input type="hidden" value="24" column="driver#driverBizId"  />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											系统账号：
										</td>
										<td>
											<span column="driver#accountId" id="staffId_span"></span>
										</td>
										<td class="menuTd">
											住址：
										</td>
										<td>
											<span column="driver#driverAddress" id="address_span"></span>
										</td>
									</tr>
									<td colspan="4" style="text-align: center;">
										<input type="button" class="input_button backStepButton" value="上一步" />
										&nbsp;
										<input type="button" class="input_submit" value="提交" id="bindCDButton" />
									</td>
								</table>
							</div>
						</form>
					</div>

					<%-- 添加司机 begin  --%>
					<div class="tab_content" style="display: none;overflow-x: hidden; width: auto;">
						<form id="driver_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post">
							<div class="content_left" style="margin-right:10px; ">
								<div editDiv="true" id="aa" style="height:200px; width:170px; text-align:center;" >
									<div style="width:170px; height:170px;">
										<img class="content_left_img" id="driver_info_left_img" src=""
											width="170" height="170" editImg="true" />
									</div>
									<input type="button" value="选择图片" />
									<input type="file" id="driverFile" style="z-index:999;position:relative;left:-75px; top:-24px; filter:alpha(opacity=0);opacity:0;" name="file" />
								</div>	
							</div>
						</form>
						<form action="cardispatchManage!saveDriverAndBindCarAjax.action" id="saveBindCDForm" method="post">
							<input type="hidden" name="driver#driverPic" id="driverPic" />
							<input type="hidden" value="1" name="sub" />
							<input type="hidden" value="${param.carId}" id="add_carId" name="carId" />
							<%-- <input type="hidden" value="${param.bizunitInstanceId}" name="bizunitInstanceId"/>  --%>
							<div class="content_right">
								<table class="pi_table">
									<tr class="main-table1-tr">
										<td colspan="4" class="main-table1-title">
											<span class="doc_tit">填写司机信息</span>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											司机姓名：
										</td>
										<td>
											<input type="text" class="input_text" id="add_driverName" checkopera="存在特殊字符" checkchinese="只能输入中文" 
												name="driver#driverName" checknull="司机姓名不能为空" promtInfo="司机姓名不能为空"  />
										</td>
										<td class="menuTd">
											电话号码：
										</td>
										<td>
											<input type="text" class="input_text" id="add_driverPhone"
												checkphone="电话格式不规范"
												name="driver#driverPhone" checknull="电话号码不能为空" promtInfo="电话号码不能为空" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											年龄：
										</td>
										<td>
											<input type="text" class="input_text" number="keypress" id="add_driverAge"
												name="driver#driverAge" checknumber="请输入数字!"/>
										</td>
										<td class="menuTd">
											身份证号：
										</td>
										<td>
											<input type="text" class="input_text" id="add_driverIdCard" checkopera="存在特殊字符" 
												checkidcard="身份证格式不规范" 
												name="driver#identificationId" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											所属区域：
										</td>
										<td>
	
											<input type="hidden" name="driver#driverBizId" id="bizunitText" column="car#carBizId"
												value="${param.bizunitInstanceId}" />
											<input type="text" class="input_text" readonly="readonly" column="car#carBizName"
												id="bizunitNameText" column="car#carBizName" />
											&nbsp;
										</td>
										<td class="menuTd">
											IT帐号：
										</td>
										<td>
											<input type="text" class="input_text" id="ITAccountIdText" checknull="&nbsp;&nbsp; IT账号不能为空" checkajax="{'method':'checkDriverAccount',msg:'&nbsp;&nbsp; IT账号已经存在了'}" checkregex="{'reg' : '/^[A-z]+[.][A-z]+\\d*$/' , 'msg' : '&nbsp;&nbsp; IT账号命名不规范'}"/>
											<input type="hidden"  name="driver#accountId" id="accountId_hidden" />
											<span id="accountSuffix"></span>
											<input type="hidden" checknull="&nbsp;&nbsp; IT账号不能为空" checkajax="{'method':'checkDriverAccount',msg:'&nbsp;&nbsp; IT账号已经存在了'}" checkregex="{'reg' : '/^[A-z]+[.][A-z]+\\d*$/' , 'msg' : '&nbsp;&nbsp; IT账号命名不规范'}" checkTarget="#ITAccountIdText" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											驾照类型：
										</td>
										<td>
											<input type="text" class="input_text"
												name="driver#driverLicenseType" checkopera="存在特殊字符" />
										</td>
										<td class="menuTd">
											住址：
										</td>
										<td>
											<input type="text" class="input_text" style="width: 480px;"
												name="driver#driverAddress" checkopera="存在特殊字符" />
										</td>
									</tr>
									<tr>
										<td colspan="4" style="text-align: center;">
											<input type="button" class="input_button backStepButton"
												value="上一步" />
											<input type="button" class="input_submit" value="提交"
												id="addDriverButton" />
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
					<%-- 添加司机 end  --%>
				</div>
			</div>
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
							<input type="text" id="driver_bizName" />
							<input type="hidden" id="driver_bizId" />
							<a href="javascript:void(0)" id="driver_biz_btn"></a>
							<div id="driver_treeDiv">
								<%-- 放置组织架构树 --%>
							</div> <select id="driver_license_type">
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
							<a href="javascript:void(0);" class="search_button" id="driver_search_btn"></a> 
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

	</body>
</html>


