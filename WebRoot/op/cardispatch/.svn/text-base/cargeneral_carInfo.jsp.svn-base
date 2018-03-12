<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<script type="text/javascript" src="js/gerneral_car.js"></script>

<style>
	#carTreeDiv *,#carTreeDiv{ z-index:999; }
	[checknull],.formcheckspan{display:none;}
	.content_left {border-radius: 5px 5px 5px 20px; }
	#car_img , #driver_img , #mobile_img {border-radius: 5px 5px 5px 20px; }
</style>

<div id="car_div">
	<form id="car_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post">
		<div class="content_left" style=" width:170px; height:190px;">
			<img class="content_left_img"  id="car_img"  column="car#carPic" width="170" height="190" showSpan="true" showImg="true" />
			<div editDiv="true" id="aa" style="height:190px; text-align:center;" >
				<div style="width:170px; height:160px;">
					<img class="content_left_img" id="car_info_left_img" column="car#carPic" width="170" height="160" editImg="true" />
				</div>
				<input type="button" value="选择图片" />
				<input type="file" id="carFile" name="file" style="z-index:999;position:relative;left:-75px; top:-20px; filter:alpha(opacity=0);opacity:0;" />
			</div>	
		</div>
	</form>
	<form id="carForm" action="cardispatchManage!updateCar.action" method="post">
		<input type="hidden" id="carId" name="car#id"/>
		<input type="hidden" id="carPic"  name="car#carPic" column="car#carPic" />
		<div class="content_right">
			<table class="pi_table">
				<tr>
					<td class="menuTd">
						所属组织：
					</td>
					<td colspan="2">
						<%-- 广东怡创 → 第一事业部 → 海珠一体化项目组 → 东部片区 --%>
						<span id="carBizName_span" showSpan="true" column="car#carBizName"></span>
						<input type="text" class="input_text" editInput="true" 
									name="carBizName" id="carBizName" />
						
						<input type="hidden" class="input_text" column="car#carBizId"
									name="car#carBizId" id="carBizId" />
						<input type="button" id="choose_button" class="input_button"
							editButton="true" value="选择区域" checknull="区域不能为空" checktarget="#carBizName"  />
						<div id="chooseOrgDiv" style=" z-index:100; position: absolute; border: 1px solid #ccc; background: #fff; margin-top: -2px; color:black; display: none;width:180px;height:160px;overflow-y:auto;  padding: 2px;">
							<%-- 放置组织架构树 --%>
						</div>
					</td>
					<td class="tr">
						<input type="button" id="updateBtn" editBtn="true" value="修改" />
						<input type="button" class="input_button" value="保存" id="sbtn" saveBtn="true" />
						<input type="button" class="input_button" value="返回" cancelBtn="true" />
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						车辆牌照：
					</td>
					<td>
						<span showSpan="true" id="carNumber_span" column="car#carNumber"></span>
						<input type="text" class="input_text" editInput="true"
							name="car#carNumber" id="carNumber_text"
							checknull="车牌不能为空" checkopera="请不要输入特殊字符!";
							checkajax="{'url':'cardispatchManage!checkCarNumberExists.action','msg':'车牌重复了','param':{'carNumber':'#carNumber_text','carId':'#carId'}}" />
					</td>
					<td class="menuTd">
						车辆类型：
					</td>
					<td>
						<span showSpan="true" id="carType_span" column="car#carType"></span>
						<select class="input_select" editInput="true"
							name="car#carType" id="carType_text">
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
						车辆年龄：
					</td>
					<td>
						<span showSpan="true" id="carAge_span" column="car#carAge"></span>
						<input type="text" class="input_text" editInput="true" 
							name="car#carAge" id="carAge_text" checknumber="给个数字啦!亲" />
						（年）
					</td>
					<td class="menuTd">
						车辆型号：
					</td>
					<td>
						<span showSpan="true" id="carModel_span" column="car#carModel"></span>
						<input type="text" class="input_text" editInput="true" 
							name="car#carModel" id="carModel_text" checkopera="请不要输入特殊字符!";/>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						租赁费用：
					</td>
					<td>
						<span showSpan="true" id="leasePay_span" column="car#leasePay"></span>
						<input type="text" class="input_text" editInput="true"
							name="car#leasePay" id="leasePay_text" checknumber="给个数字啦!亲" />
						（元/每月）
					</td>
					<td class="menuTd">
						保管费用：
					</td>
					<td>
						<span showSpan="true" id="custodyFee_span" column="car#custodyFee"></span>
						<input type="text" class="input_text" editInput="true"
							name="car#custodyFee" id="custodyFee_text" checknumber="给个数字啦!亲"/>
						（元/每月）
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						载客数量：
					</td>
					<td>
						<span showSpan="true" id="passengerNumber_span" column="car#passengerNumber"></span>
						<input type="text" class="input_text" editInput="true"
							name="car#passengerNumber" id="passengerNumber_text" checknumber="给个数字啦!亲" checkTarget="#passengerNumber_text" checknull="载客数量不能为空"/>
						（位）
						<input type="hidden"  checkTarget="#passengerNumber_text" />
					</td>
					<td class="menuTd">
						载重数量：
					</td>
					<td>
						<span showSpan="true" id="loadWeight_span" column="car#loadWeight"></span>
						<input type="text" class="input_text" editInput="true"
							name="car#loadWeight" id="loadWeight_text" checknumber="给个数字啦!亲"/>
						（吨）
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						当前位置：
					</td>
					<td>
						<a href="#" target="_blank" id="position"></a>
						<em class="pi_position_ico" id="position_icon" title="点击获取最新当前位置"></em>
					</td>
					<td colspan="2">
						
					</td>
				</tr>
			</table>
		</div>	
	</form>
</div>


<%-- 司机信息 --%>
<div class="content_m">
	<h4 class="content_m_tit">
		<span class="doc_tit">司机信息</span>
		<span class="right_tool_bar" style="right:5px;"><input type="button" id="driver_change_btn" value="更换司机" class="show_alert_div" /> </span>
	</h4>
</div>
<div class="content_left">
	<div style="width:170px; height:190px;">
		<img class="content_left_img" column="driver#driverPic"
			id="driver_img" width="170" height="190" />
	</div>
</div>
<div class="content_right">
	<table class="pi_table">
		<tr>
			<td class="menuTd">
				姓名：
			</td>
			<td>
				<span id="staffName_span" column="driver#driverName"></span>
			</td>
			<td class="menuTd">
				电话：
			</td>
			<td>
				<span id="phone_span" column="driver#driverPhone"></span> （
				<span id="shortNumber_span"></span>）
			</td>
		</tr>
		<tr>
			<td class="menuTd">
				身份证：
			</td>
			<td>
				<span id="identificationId_span" column="driver#identificationId"></span>
			</td>
			<td class="menuTd">
				年龄：
			</td>
			<td>
				<span id="age_span" column="driver#driverAge"></span>
			</td>
		</tr>
		<tr>
			<td class="menuTd">
				驾照：
			</td>
			<td>
				<span id="driverLicenseType_span" column="driver#driverLicenseType"></span>
			</td>
			<td class="menuTd">
				所属组织：
			</td>
			<td >
				<span id="staff_bizName_span" column="driver#driverBizName"></span>
				<input type="hidden" value="24" id="staff_bizId" column="driver#driverBizId" />
			</td>
		</tr>
		<tr>
			<td class="menuTd">
				系统账号：
			</td>
			<td>
				<span id="staffId_span" column="driver#accountId"></span>
			</td>
			<td class="menuTd">
				住址：
			</td>
			<td >
				<span id="address_span" column="driver#driverAddress"></span>
			</td>
		</tr>
	</table>
</div>



<%-- 终端信息 --%>
<input type="hidden" id="mobileId" />
<div class="content_m">
	<h4 class="content_m_tit">
		<span class="doc_tit">车载终端信息</span>
		<span class="right_tool_bar" style="right:5px;"><input type="button" id="mobile_change_btn" value="更换终端" /> </span>
	</h4>
</div>
<div class="content_left">
	<div style="width:170px; height:190px;">
		<img class="content_left_img"  id="mobile_img" column="terminal#terminalPic"
			width="170" height="190" />
	</div>
</div>
<div class="content_right">
	<table class="pi_table">
		<tr>
			<td class="menuTd">
				终端类型：
			</td>
			<td>
				<span id="mobileType_span" column="terminal#mobileType"></span>
			</td>
			<td class="menuTd">
				终端IMEI码：
			</td>
			<td>
				<span id="clientimei_span" column="terminal#clientimei"></span>
			</td>
		</tr>
		<tr>
			<td class="menuTd">
				终端SIM卡号：
			</td>
			<td>
				<span id="telphoneNo_span" column="terminal#telphoneNo"></span>
			</td>
			<td class="menuTd">
				名称：
			<td>
				<span id="telphoneNo_span" column="terminal#terminalName"></span>
			</td>
			
			</td>
			<%--<td class="menuTd">
				SIM卡运营商：
			</td>
			<td>
				<span id="simOperator_span" column="terminal#simOperator"></span>
			</td>
			--%>
		</tr>
		<%--<tr>
			<td class="menuTd">
				SIM卡月租：
			</td>
			<td>
				<span id="monthlyRent_span" column="terminal#monthlyRent"></span>（元/每月）
			</td>
			<td class="menuTd">
				SIM卡余额：
			</td>
			<td>
				0（元）
			</td>
		</tr>
		--%><tr>
			<td class="menuTd">
				开通时间：
			</td>
			<td>
				<span id="launchedTime_span" column="terminal#launchedTime"></span>
			</td>
			<td class="menuTd">
				到期时间：
			</td>
			<td>
				<span id="expiredTime_span" column="terminal#expiredTime"></span>
			</td>
			<tr>
				<td class="menuTd">
					所属组织：
				</td>
				<td>
					<span column="terminal#terminalBizName">东部片区</span>
				</td>
			</tr>
		</tr>
	</table>
</div>

