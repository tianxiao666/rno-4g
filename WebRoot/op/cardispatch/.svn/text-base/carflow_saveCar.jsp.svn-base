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
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" href="css/carflow_saveCar.css" type="text/css" />
		
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
		<script src="js/util/urlutil.js"></script>
		<script type="text/javascript" src="js/class/car.js"></script>
		<script>
			var carId = "";
			$(document).ready(function(){
				carId = "${param.carId}";
				if ( carId ) {
					$.ajax({
						"url" : "cardispatchManage!findCarInfoList.action" , 
						"type" : "post" , 
						"data" : { "car#carId" : carId } , 
						"async" : true , 
						"success" : function ( data ) {
							data = eval( "(" + data + ")" );
							var car = new Car(data[0]);
							car.putInfo("body");
							$("input").blur();
						}
					});
					
				}
			})
		</script>
		<script type="text/javascript" src="js/carflow_saveCar.js"></script>
		<title>车辆管理流程1 - 添加车辆</title>
	</head>
	<body>
		<%-- 流程标志 --%>
		<div id="flow_pic">
			<div class="top_number"></div>
		</div>

		<%-- 车辆信息 --%>
		<div id="car_div">
			
			
				
				<div class="content_left">
					<div editDiv="true" id="aa"
						style="height: 195px; width: 170px; text-align: center;">
						<div style="height: 170px; width: 170px;">
							<img id="car_info_left_img" src="" width="170" height="170" column="car#carPic"
								editImg="true" />
							<br />
						</div>
						<form id="car_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post" enctype="multipart/form-data">
							<input type="button" value="选择图片" />
							<input type="file" id="carFile" style="z-index:999;position:relative;left:-75px; top:-24px; filter:alpha(opacity=0);opacity:0;" name="file" />
						</form>
					</div>
				</div>
				<form id="carForm" action="cardispatchManage!saveCarAjax.action" method="post" enctype="multipart/form-data">
				<input type="hidden" id="carId" name="car#id" value="${param.carId}" />
				<input type="hidden" id="carPic" name="car#carPic" column="car#carPic"	/>
				<div class="content_right">
					<table class="pi_table">
						<tr>
							<td class="menuTd">
								车辆牌照：
							</td>
							<td>
								<input type="text" class="input_text"
									name="car#carNumber" id="carNumber_text" 
									column="car#carNumber"
									checknull="车牌不能为空" promtInfo="车牌不能为空" 
									checkopera="存在特殊字符"
									checkajax="{'url':'cardispatchManage!checkCarNumberExists.action','msg':'车牌重复了','param':{'carNumber':'#carNumber_text','carId':'#carId'}}" />
							</td>
							<td class="menuTd">
								车辆类型：
							</td>
							<td>
								<select class="input_select" name="car#carType" column="car#carType"
									id="carType_text">
									<option value="面包车">
										面包车
									</option>
									<option value="小轿车">
										小轿车
									</option>
									<option value="小货车">
										小货车
									</option>
								</select>
							</td>
						</tr>
						<tr>
							<td class="menuTd">
								所属组织：
							</td>
							<td>
								<%-- 广东怡创 → 第一事业部 → 海珠一体化项目组 → 东部片区 --%>
								<input type="text" class="input_text"  column="car#carBizName"
									readonly="readonly" name="carBizName" id="carBizName" />
								<input type="hidden" class="input_text" name="car#carBizId" column="car#carBizId"
									id="carBizId" />
								<input type="button" id="choose_button" class="input_button"
									value="选择区域" checknull="区域不能为空" checktarget="#carBizName" />
								<div id="chooseOrgDiv"
									style="position: absolute; min-height: 208px; min-width: 160px; border: 1px solid #ccc; background: #fff; margin-top: -2px; display: none;">
										<%-- 放置组织架构树 --%>
								</div>
							</td>

							<td class="menuTd">
								载客数量：
							</td>
							<td>
								<input type="text" class="input_text" number="keypress"  column="car#passengerNumber"
									promtInfo="载客数量不能为空" name="car#passengerNumber" checknumber="亲,请输入数字!"
									id="passengerNumber_text" />
								（位）
								<input type="hidden" checknull="载客数量不能为空"
									checkTarget="#passengerNumber_text" />
							</td>

						</tr>
						<tr>
							<td class="menuTd">
								车辆年龄：
							</td>
							<td>
								<input type="text" class="input_text" dnumber="keypress"  column="car#carAge"
									name="car#carAge" id="carAge_text" checknumber="亲,请输入数字!"/>
								（年）
							</td>
							<td class="menuTd">
								车辆型号：
							</td>
							<td>
								<input type="text" class="input_text" column="car#carModel"
									name="car#carModel" id="carModel_text" checkOpera="亲,不要输入特殊字符喔！"/>
							</td>
						</tr>
						<tr>
							<td class="menuTd">
								租赁费用：
							</td>
							<td>
								<input type="text" class="input_text" dnumber="keypress" column="car#leasePay"
									name="car#leasePay" id="leasePay_text" checknumber="亲,请输入数字!"/>
								（元/每月）
							</td>
							<td class="menuTd">
								保管费用：
							</td>
							<td>
								<input type="text" class="input_text" dnumber="keypress" column="car#custodyFee"
									name="car#custodyFee" id="custodyFee_text" checknumber="亲,请输入数字!"/>
								（元/每月）
							</td>
						</tr>
						<tr>
							<td class="menuTd">
								车辆厂家：
							</td>
							<td class="blue">
								<input type="text" class="input_text" column="car#carMarker"
									name="car#carMarker" id="carMarker_text" checkOpera="亲,不要输入特殊字符喔！"/>
							</td>

							<td class="menuTd">
								车载重量：
							</td>
							<td>
								<input type="text" class="input_text" dnumber="keypress" column="car#loadWeight"
									name="car#loadWeight" id="loadWeight_text" checknumber="亲,请输入数字!"/>
								（吨）
							</td>
						</tr>
						<tr>
							<td
								style="background: #fff; height: 30px; line-height: 20px; text-align: center; border: none;"
								colspan="4">
								<input type="button" class="input_button" value="提交" org="true"
									id="addCarButton" />
								<input type="button" class="input_button" value="提交" org="false"
									id="addCarButtonByOrg" />
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</body>
</html>

