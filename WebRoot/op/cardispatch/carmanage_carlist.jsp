<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆管理</title>
<%-- 基本css --%>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="css/informationManager.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
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
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
<script type="text/javascript" src="js/obj/carmanage_carlist_obj.js"></script>
<script type="text/javascript" src="js/carmanage_carlist.js"></script>
<script type="text/javascript">
	$(function() {
		$("#multiQueryButton").click(function() {
			$(".advanced-search").toggle("fast");
		})
		$("#selectWorkPlaceButton").click(function() {
			$("#selectWorkPlace").toggle("fast");
		})
		$("#cancelSearch").click(function() {
			$(".advanced-search").toggle("fast");
		})
	})
</script>
</head>

<body>
	<div class="top_right_tool">
		<input name="choice_bizName" value="" type="text" readonly="readonly"
			id="choice_bizName" />
		<input name="choice_bizId" value="" type="hidden"
			id="choice_bizId" />
		<a href="javascript:void(0);" class="select_button selectWorkPlaceButton" id="selectWorkPlaceButton" title="选择组织"></a>
		<div id="selectWorkPlace" class="text_org_tree">
			<%-- 放置组织架构树 --%>
		</div>
		<input name="carNumber" value="" type="text"
			id="carNumberSQText" />
		<input class="input_button" value="搜索" id="simpleQueryButton"
			type="button" onclick="searchDriver();" />
		<input class="input_button" value="高级搜索" id="multiQueryButton"
			type="button" />
	
		<div class="advanced-search">
			<h4 class="advanced-search-title">填写查询信息</h4>
			<table class="main-table1 tl" style="margin-bottom: 0px;">
				<tr>
					<td class="menuTd">车牌号码：</td>
					<td>
						<input type="text" class="input_text" id="carNumber" />
					</td>
				</tr>
				<tr>
					<td class="menuTd">车辆类型：</td>
					<td>
						<select class="input_select" id="carType" style="width:120px; *+width:116px;">
							<option>请选择</option>
							<option>面包车</option>
							<option>小轿车</option>
							<option>小货车</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="menuTd">司机姓名：</td>
					<td>
						<input type="text" class="input_text" id="driverName" />
					</td>
				</tr>
				<tr>
					<td class="menuTd">车载终端号：</td>
					<td>
						<input type="text" class="input_text" id="clientimei" />
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center;">
						<input type="button" class="input_button" id="searchBtn" value="查询" onclick="highSearchBtn();" />
						&nbsp;
						<input type="button" class="input_button" id="cancelSearch" value="取消" />
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>


<div class="container-main-table1">
	<h4 class="container-main-title">车辆信息列表</h4>
	<div id="carInfoDiv" class="container-main-table1">
		<table id="carInfoTable" class="main-table1 tc">
			<thead>
				<tr>
					<th style="width: 35px;"></th>
					<th>
						车牌号码
					</th>
					<th>
						车辆类型
					</th>
					<th>
						所属区域
					</th>
					<th>
						司机姓名
					</th>
					<th>
						司机号码
					</th>
					<th>
						车载终端
					</th>
				</tr>
			</thead>
			<tbody>

			</tbody>
		</table>
		<div style="padding: 0px; padding-top: none; float:left;" id="car_foot_div">
			<input type="checkbox" class="input_checkbox" id="allcheckbox"
				onclick="allCheck();" />
			全选&nbsp;
			<input type="button" class="input_button" value="删除"
				onclick="deleteCar();" />
			&nbsp;
			<input type="button" class="input_button" value="添加车辆"
				onclick="window.open('carflow_saveCar.jsp');" />
		</div>
	</div>
</div>

</body>
</html>

