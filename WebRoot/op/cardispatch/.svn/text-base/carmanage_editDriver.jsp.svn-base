<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>司机信息</title>
		<link rel="stylesheet" href="css/base.css" type="text/css" />
		<link rel="stylesheet" href="css/public.css" type="text/css" />
		<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
		<link rel="stylesheet" href="css/p_information.css" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="../../jslib/jquery/css/jquery.treeview.css"></link>

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
		<script type="text/javascript" src="js/tool/new_formcheck.js">
</script>
		<script type="text/javascript" src="js/tool/showedit2.js">
</script>
		<script type="text/javascript" src="js/class/driver.js">
</script>

		<script type="text/javascript">
$(function() {
	/*input样式*/
	$(":button,:submit").mousedown(function() {
		$(this).addClass("input_button_down");
	});
	$(":button,:submit").mouseup(function() {
		$(this).removeClass("input_button_down");
	});
	//打开选择区域
	$("#choose_button").click(function() {
		$("#chooseOrgDiv").slideToggle("fast");
	});
})

var driverInfo = "driverInfo";
var editDriver_showedit = null;
var driverId = "";

$(function() {
	driverId = "${param.driverId}";
	if (driverId == "") {
		return;
	}
	var ajaxFormSub = "";
	providerOrgTree();

	
	
	//表单验证
	formcheck( {
		"form" : $("#editDriverInfo_form"),
		"subButton" : $("#saveBtn"),
		"isAjax" : true,
		"showLoading" : true,
		"ajaxSuccess" : function(data) {
			var src = $("#driver_info_left_img").attr("src");
			$("#driver_img").attr( {
				"src" : src
			});
			alert("操作成功");
			var op = window.opener.document;
			$(op).find("#simpleQueryButton").click();
		}
	});
	$("#editDriverInfo_form .formcheckspan").attr("editButton", "true");

	//表单区域内容转换
	showedit("#editDriverInfo_form");

	//查询司机信息
	$.ajax( {
		"url" : "cardispatchManage!findDriverInfoList.action",
		"data" : {
			"driver#driverId" : driverId
		},
		"type" : "post",
		"async" : true,
		"success" : function(data) {
			data = eval("(" + data + ")");
			if (data.length > 0) {
				var driver = new Driver(data[0]);
				driver.putInfo("body");
			}
		}
	});

	//选择图片
	$("#driverImgFileChoiceBtn,#driver_img").click(function() {
		$("#driverFile").click();
	});
	$("#driverFile").change(function() {
		$("#driver_info_left_img").attr( {
			"src" : "images/big_loading.gif"
		});
		$("#driver_img_form").empty();
		var dFile = $("#driverFile").clone();
		$(dFile).attr( {
			"id" : "cf"
		});
		$("#driver_img_form").append($(dFile));
		$("#driver_img_form").ajaxSubmit(function(data) {
			$("#cf").remove();
			$("#driver_info_left_img").attr( {
				"src" : data
			});
			$("#driverPic").val(data);
		});
	});
});

/***************** 组织结构 *******************/

//生成组织架构树
function providerOrgTree() {
	var orgId = "16";

	$.ajax( {
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action",
		"type" : "post",
		"async" : true,
		"success" : function(data) {
			data = eval("(" + data + ")");
			orgId = data.orgId;
			if (orgId == null || orgId == "") {
				orgId = "16";
			}
			var values = {
				"orgId" : orgId
			}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl, values, function(data) {
				createOrgTreeOpenFirstNode(data, "chooseOrgDiv",
						"drivermanage_org_div", "a", "orgTreeClick");
			}, "json");
		}
	});

}
function a() {
}

//显示组织信息
function orgTreeClick(dataStr, tableId) {
	var data = eval("(" + dataStr + ")");
	var orgId = data.orgId;
	$("#bizunitNameText").val(data.name);
	$("#driver_bizId").val(data.orgId);
	$("#chooseOrgDiv").slideUp("fast");
}

window.onload = function(){
	setTimeout(function(){
		$("#editDriverInfo_form").css({"display":"block"});
	},500);
	
	
}
</script>
	</head>
	<style type="text/css">
.content_left {
	height: 190px;
	width: 170px;
	text-align: center;
	overflow: hidden;
	position: relative;
}

.content_left_title {
	position: absolute;
	bottom: 0px;
	left: 0px;
	width: 100%;
	background: #eee;
	border-top: 1px solid #ccc;
	line-height: 28px;
	cursor: pointer;
}

#returnBtn,#saveBtn {
	display: none;
}

#choose_button {
	display: none
}
</style>
	<body>
		<div class="p_container" style="height: 330px;">
			<%--主体开始--%>
			<div class="pi_right">
				<div class="pi_menu_title clearfix">
					<p class="fl">
						填写司机信息
					</p>
				</div>
				<%-- content开始 --%>
				<div class="pi_right_content">
					<div class="tab_content">
						<form id="driver_img_form" name="img_form" action="cardispatchCommon_ajax!getFileURL.action" method="post"></form>
						<form id="editDriverInfo_form" action="cardispatchManage!updateDriverById.action" method="post">
							<input type="hidden" name="driver#id" column="driver#driverId" value="${ param.driverId}" />
							<div class="content_m">
								<h4 class="content_m_tit">
									<span class="doc_tit">司机信息</span>
									<%-- 按钮 --%>
									<span class="doc_btn"> 
										<input type="button" class="input_button" editBtn="true" id="editBtn" value="修改" />
										<input type="button" class="input_button" saveBtn="true" id="saveBtn" value="保存" />
										<input type="button" class="input_button" cancelBtn="true" removeMsgSpan="true" id="returnBtn" value="返回" /> 
									</span>
								</h4>
							</div>
							
							<div class="content_left" style="margin-right: 10px;">
								<img class="content_left_img" id="driver_img"
									column="driver#driverPic" width="170" height="185"
									showSpan="true" showImg="true" />
								<div editDiv="true" id="aa"
									style="height: 185px; width: 170px; text-align: center;">
									<div style="width: 170px; height: 165px;">
										<img class="content_left_img" id="driver_info_left_img" src=""
											width="170" height="165" editImg="true"
											column="driver#driverPic" />
									</div>
									<input type="button" style="margin-top: 2px;"
										id="driverImgFileChoiceBtn" value="选择图片" />
									<input type="file" id="driverFile"
										style="z-index: 999; position: relative; left: -75px; top: -24px; filter: alpha(opacity = 0); opacity: 0;"
										name="file" />
									<input type="hidden" name="driver#driverPic" id="driverPic"
										column="driver#driverPic" />
								</div>
							</div>
							<div class="content_right">
								<table class="pi_table">
									<tbody>
									<tr>
										<td class="menuTd">
											司机姓名：
										</td>
										<td>
											<span showSpan="true" column="driver#driverName"></span>
											<input type="text" checkchinese="姓名必须是中文" editInput="true"
												checknull="司机姓名不能为空" name="driver#driverName" />
										</td>
										<td class="menuTd">
											IT账号：
										</td>
										<td>
											<span column="driver#account"></span>
											<span column="driver#accountSuffix"></span>
											<input type="hidden" name="driver#accountId"
												column="driver#accountId" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											电话号码：
										</td>
										<td>
											<span showSpan="true" column="driver#driverPhone"></span>
											<input type="text" checkphone="电话格式不规范" checknull="电话号码不能为空"
												editInput="true" name="driver#driverPhone" />
										</td>
										<td class="menuTd">
											年龄：
										</td>
										<td>
											<span showSpan="true" column="driver#driverAge"></span>
											<input type="text" number="keypress" editInput="true"
												name="driver#driverAge" checknumber="亲,请输入数字喔!" />
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											身份证号：
										</td>
										<td>
											<span showSpan="true" column="driver#identificationId"></span>
											<input type="text" checkidcard="身份证格式不规范" editInput="true"
												name="driver#identificationId" />
										</td>
										<td class="menuTd">
											工资：
										</td>
										<td>
											<span showSpan="true" column="driver#wage"></span>
											<input type="text" dnumber="keypress" editInput="true"
												name="driver#wage" checknumber="亲,请输入数字喔!"/>
										</td>
									</tr>
									<tr>
										<td class="menuTd">
											所属区域：
										</td>
										<td>
											<span showSpan="true" id="bizName"
												column="driver#driverBizName"></span>
											<input type="text" editInput="true" readonly="readonly"
												id="bizunitNameText" column="driver#driverBizName" />
											<input type="hidden" name="driver#driverBizId"
												id="driver_bizId" column="driver#driverBizId" />
											<input type="button" id="choose_button" class="input_button"
												value="选择区域" editButton="true" checknull="所属区域不能为空"
												checktarget="#bizunitNameText" />

											<div id="chooseOrgDiv"
												style="width: 200px; min-height: 200px; z-index: 999; overflow-y: auto; position: absolute; border: 1px solid #ccc; background: #fff; margin-top: -2px; display: none;">
												<%-- 放置组织架构树 --%>
											</div>
											<td class="menuTd">
												驾照类型：
											</td>
											<td>
												<span showSpan="true" column="driver#driverLicenseType"></span>
												<input type="text" editInput="true" class="input_text"
													name="driver#driverLicenseType" checkOpera="亲,不要输入特殊字符喔！"/>
											</td>
									</tr>
									<tr>
										<td class="menuTd">
											住址：
										</td>
										<td colspan="3">
											<span showSpan="true" id="address"
												column="driver#driverAddress"></span>
											<input type="text" editInput="true"
												name="driver#driverAddress" checkOpera="亲,不要输入特殊字符喔！"/>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</form>
					</div>

					<div class="pi_right_content_bottom_tc">
					</div>
					
				</div>
			</div>
			<%-- content结束 --%>
		</div>
		<%--主体结束--%>
	</body>
</html>
