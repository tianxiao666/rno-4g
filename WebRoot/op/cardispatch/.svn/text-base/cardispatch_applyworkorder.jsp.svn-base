<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>提交申请单</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css" href="css/car_gb.css" />
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js">
</script>
		<script type="text/javascript" src="../../js/public.js">
</script>
		<script type="text/javascript" src="../../jslib/date/date.js">
</script>
		<script type="text/javascript"
			src="../../jslib/jquery/jquery.treeview.js">
</script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js">
</script>
		<script type="text/javascript" src="js/tool/new_formcheck.js">
</script>
		<script type="text/javascript" src="js/tool/custom_autocomplete.js">
</script>
		<script type="text/javascript" src="js/util/dateutil.js">
</script>
		<script type="text/javascript" src="js/util/objutil.js">
</script>
		<script type="text/javascript" src="js/tool/areaselect.js">
</script>
		<script type="text/javascript" src="js/cardispatch_applyworkorder.js">
</script>
		<script>

var stationId = "";
var baseStationId = "";
var baseStationType = "";
var WOID = "";
var TOID = "";
var workType = "";
var criticalClass = "";

$(document).ready(function() {
	stationId = "${param.stationId}";
	baseStationId = "${param.baseStationId}";
	baseStationType = "${param.baseStationType}";

	//抢修进入预填
		WOID = "${param.WOID}";
		TOID = "${param.TOID}";
		workType = "${param.workType}";
		criticalClass = "${param.criticalClass}";
		if (WOID) {
			$("#woId").val(WOID);
		}
		if (TOID) {
			$("#toId").val(TOID);
		}
		if (workType) {
			$("#workType").val(workType);
		}
		if (criticalClass) {
			var criClass = "";
			if (criticalClass == "normal") {
				criClass = "一般";
			} else if (criticalClass == "urgent") {
				criClass = "特急";
			} else if (criticalClass == "critical") {
				criClass = "紧急";
			}
			$("#criticalClass").find("option:contains('" + criClass + "')")
					.attr( {
						"selected" : "selected"
					});
		}
	});
</script>


		<script>
/**
 * 显示、限制textarea输入字数
 * 调用举例：textareaDisplayValidator($('#SMS_CONTENT'),$('#total'),$('#used'),$('#page'),'<s:text name="validator.SMS_CONTENT.onfocus"/>');
 * @param textArea，输入文字内容的文本域对象
 * @param total，展示最大输入字数的文本框对象
 * @param used，展示已输入字数的文本框对象
 * @param page，展示短信页数的文本框对象
 * @param message，输入字数超过最大限制的提示消息
 */
function textareaSmContentValidator(textArea, total, used, page, message) {
	if ($.browser.msie) { //IE浏览器
		$(textArea).unbind("propertychange")
				.bind(
						"propertychange",
						function(e) {
							e.preventDefault();
							textareaSmContentProc(textArea, total, used, page,
									message);
						});
	} else { //ff浏览器
		$(textArea).unbind("input").bind("input", function(e) {
			e.preventDefault();
			textareaSmContentProc(textArea, total, used, page, message);
		});
	}
}

function textareaSmContentProc(textArea, total, used, page, message) {
	var max;
	max = $(total).val();
	if ($(textArea).val().length > max) {
		$(textArea).val($(textArea).val().substring(0, max));
		$(used).val(max);
		$(page).val(2);
		alert(message);
	} else {
		var messageLength = $(textArea).val().length;
		$(used).val(messageLength);
		var bb = Math.ceil(messageLength / 70);
		$(page).val(bb);
	}
}

/**
 * 限制textarea输入字数
 * 调用举例：textareaDisplayValidator($('#SMS_CONTENT'),20,'<s:text name="validator.SMS_CONTENT.onfocus"/>');
 * @param textArea，输入文字内容的文本域对象
 * @param total，最大输入字数
 * @param message，输入字数超过最大限制的提示消息
 */
function textareaMaxValidator(textArea, total, message) {
	if ($.browser.msie) { //IE浏览器
		$(textArea).unbind("propertychange").bind("propertychange",
				function(e) {
					e.preventDefault();
					textareaMaxProc(textArea, total, message);
				});
	} else { //ff浏览器
		$(textArea).unbind("input").bind("input", function(e) {
			e.preventDefault();
			textareaMaxProc(textArea, total, message);
		});
	}
}

function textareaMaxProc(textArea, total, message) {
	var max;
	max = total;
	if ($(textArea).val().length > max) {
		$(textArea).val($(textArea).val().substring(0, max));
		alert(message);
	}
}
</script>





		<script type="text/javascript"
			src="js/obj/cardispatch_applyworkorder_obj.js">
</script>
		<script>

$(document).ready(function() {
	addFocus();
});
</script>
		<style>
/*张声*/
#header960 {
	text-align: left;
}
</style>
	</head>

	<body>

		<%--头部开始--%>
		<div id="header960">
			<div class="header-top">
				<h2>
					车辆调度申请单
				</h2>
			</div>
			<div class="header-main">
				<span class="fl">申请单号：</span>
				<span class="fr">申请单状态：</span>
				<span>创建时间：</span>
			</div>
			<%-- 状态 --%>
			<div class="list_pic clearfix">
				<ul>
					<li>
						<em class="list_pic_li list_pic_li_ing">申请</em>
						<em class="next_ico"></em>
						<span class="list_pic_tit"></span>
					</li>
					<li>
						<em class="list_pic_li">派车</em>
						<em class="next_ico"></em>
						<span class="list_pic_tit"></span>
					</li>
					<li>
						<em class="list_pic_li">用车</em>
						<em class="next_ico"></em>
						<span class="list_pic_tit"></span>
					</li>
					<li>
						<em class="list_pic_li">还车</em>
						<span class="list_pic_tit"></span>
					</li>
				</ul>
				<div class="list_pic_info">
					<h4>
						里程（仪表）：--
						<br />
						里程（GPS）：--
					</h4>
				</div>

			</div>
		</div>
		<%--头部结束--%>

		<%--主体container开始--%>
		<div id="container960">
			<%--tab1开始--%>
			<div class="container-tab1">
				<fieldset id="fieldset-1">
					<legend>
						<input type="checkbox" checked="checked" />
						<span>工单信息</span>
					</legend>
					<%--container-main开始--%>
					<div class="container-main">
						<%--container-main-table1开始--%>
						<div class="container-main-table1 container-main-table1-tab">
							<form
								action="cardispatchWorkorder!createCardispatchWorkorderAction.action"
								id="applyCardispatch_form" method="post"
								enctype="multipart/form-data">
								<input type="hidden" id="useCarPersonAccountId"
									name="Carworkorder_save#useCarPersonId" />
								<input type="hidden" id="planUseCarAddress"
									name="Carworkorder_save#planUseCarAddress" />
								<input type="hidden" id="woId" name="apply_save#associateWoId" />
								<input type="hidden" id="toId" name="apply_save#associateToId" />
								<input type="hidden" id="workType"
									name="apply_save#associateWorkType" />

								<table class="main-table1">
									<tr class="main-table1-tr">
										<td colspan="4" class="main-table1-title">
											车辆调度申请单信息
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											车型：
										</td>
										<td>
											<select name="Carworkorder_save#useCarType" id="carType">
												<option value="面包车">
													面包车
												</option>
												<option value="小轿车">
													小轿车
												</option>
												<option value="小货车">
													小货车
												</option>
												<option value="皮卡">
													皮卡
												</option>
											</select>
											<input type="hidden" checkTarget="#carType"
												checknull="车型不能为空" />
										</td>
										<td class="menuTd">
											用车紧急程度：
										</td>
										<td>
											<select name="Carworkorder_save#criticalClass"
												id="criticalClass">
												<option value="特急">
													特急
												</option>
												<option value="紧急">
													紧急
												</option>
												<option value="一般">
													一般
												</option>
											</select>
											<input type="hidden" checkTarget="#criticalClass"
												checknull="紧急程度不能为空" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											用车时间：
										</td>
										<td style="width: 43%;">
											<input type="text" readonly="readonly" id="bg_time"
												name="Carworkorder_save#planUseCarTime" />
											<a id="bg_date_button" class="date_button" style="margin-left:-27px; margin-left:-25px \0;*+margin-left:-25px;"
												onclick="bg_date_buttonClick(event);"></a> 至
											<input type="text" readonly="readonly" id="ed_time"
												name="Carworkorder_save#planReturnCarTime" />
											<a id="ed_date_button" class="date_button" style="margin-left:-27px;margin-left:-25px \0;*+margin-left:-25px;"
												onclick="ed_date_buttonClick(event);"></a>
											<input type="hidden" checknull="用车时间不能为空"
												checkTarget="#bg_time" />
										</td>
										<td class="menuTd">
											用车人：
										</td>
										<td>
											<input type="text" id="useCarPersonName" checknull="用车人不能为空"
												checkTarget="#useCarPersonAccountId" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											用车地点：
										</td>
										<td colspan="3">
											<div id="area_div" style="display: inline;"></div>
											<input type="text" id="areaDescription" maxlength="50" checklength="{'maxLength':'60','minLength':'0','msg':'长度不能超过60'}" 
												value="" checknull="用车地点不能为空" />
											<input type="hidden" id="address"
												name="workorder#realUseCarMeetAddress" />
											<input type="hidden" checkTarget="#areaDescription" checklength="{'minLength':'0','maxLength':'50','msg':'地点字数过长'}" />
										</td>
									</tr>
									<tr>
										<td class="menuTd higherLine">
											备注：
										</td>
										<td colspan="3">
											<textarea rows="4" style="width: 75%" cols="2" id="description"
												name="Carworkorder_save#applyDescription"></textarea>
										</td>
									</tr>
								</table>
								<div class="container-bottom">
									<input type="button" id="applyCardispatchWorkorderBtn"
										value="申请" style="width: 60px;" />
									<input type="reset" value="重置" style="width: 60px;" />
								</div>
							</form>
						</div>
						<%--container-main-table1结束--%>

					</div>
					<%--container-main结束--%>
				</fieldset>
			</div>
		</div>
	</body>
</html>

