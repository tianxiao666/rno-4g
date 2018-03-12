$(document).ready( function() {
	showUpdateStaff();
	validateStaff();
});

// 验证表单
function validateStaff() {
	$("#updateStaff").validate( {
		errorPlacement : function(error, element) {
			error.appendTo(element.next().next().addClass("color: red;"));
		},
		rules : {
			staffCode : {
				remote : {
					url : "validateStaffCodeAction", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
			staffCode : function() {

							if ($("#staffCode").val() == $("#staffCode2").val()) {
								return true;
							}
							return $("#staffCode").val();
						}
					}
				}
			},
			staffEmail : {
				remote : {
					url : "validateStaffEmailAction", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
				staffEmail : function() {
			
				if ($("#staffEmail").val() == $("#staffEmail2").val()) {
					return true;
				}
							return $("#staffEmail").val();
						}
					}
				}
			}
		},
		messages : {
			staffCode : {
				remote : '已存在'
			},
			staffEmail : {
				remote : '已存在'
			}
		},
		submitHandler : function(form) {
			onUpdateSubmit();
		}

	});
}

/**
 * 获取页面的数据通过ajax提交
 */
function showUpdateStaff() {
	var id = request("id");
	var myurl = "getStaffBySubstanceIdAction?substanceId=" + id;
	$.ajax( {
		url : myurl,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			var json = result;
			fillUpdateData(json);
		}
	});
}

/**
 * 将通过ajax请求获取到的内容填充到页面
 * 
 * @param {Object}
 *            data
 */
function fillUpdateData(data) {
	var staff = data.staff;
	var staffEnrollDay = staff.staffEnrollDay.toString().split(" ", 1);
	$("#substanceId").val(staff.substanceId);
	$("#staffName").val(staff.name);
	$("#staffIdentity").val(staff.staffIdentity);
	$("#staffCode").val(staff.staffCode);
	$("#staffCode2").val(staff.staffCode);
	$("#staffGender").val(staff.staffGender);
	$("#staffPhone").val(staff.staffPhone);
	$("#staffEnrollDay").val(staffEnrollDay);
	$("#staffStatus").val(staff.staffStatus);
	$("#staffPoliticalStatus").val(staff.staffPoliticalStatus);
	$("#organizationName").val(data.organizationName);
	$("#staffBirthPalce").val(staff.staffBirthPalce);
	$("#staffAddress").val(staff.staffAddress);
	$("#staffEmail").val(staff.staffEmail);
	$("#staffEmail2").val(staff.staffEmail);
	var organizationUrl = "getAllOrganizationsAction";
	// 动态加载组织选择框
	$
			.ajax( {
				url : organizationUrl,
				type : "POST",
				dataType : 'json',
				success : function(result) {
					$
							.each(
									result,
									function(key, value) {
										if (value.organization.orgName == data.organizationName) {
											$("#orgSubstanceId")
													.append(
															"<option selected='selected' value="
																	+ value.organization.substanceId
																	+ ">"
																	+ value.organization.orgName
																	+ "</option>");
										} else {
											$("#orgSubstanceId")
													.append(
															"<option value="
																	+ value.organization.substanceId
																	+ ">"
																	+ value.organization.orgName
																	+ "</option>");
										}

									});
				}
			});
}

/**
 * 获取页面的数据通过ajax提交
 */
function onUpdateSubmit() {
	var r = confirm("确认修改？")
	if (r == false) {
		return false;
	}
	var values = {
		"staff.name" : $("#staffName").val(),
		"staff.staffIdentity" : $("#staffIdentity").val(),
		"staff.staffCode" : $("#staffCode").val(),
		"staff.staffGender" : $("#staffGender").val(),
		"staff.staffPhone" : $("#staffPhone").val(),
		"staff.staffEnrollDay" : $("#staffEnrollDay").val(),
		"staff.staffStatus" : $("#staffStatus").val(),
		"staff.staffPoliticalStatus" : $("#staffPoliticalStatus").val(),
		"orgSubstanceId" : $("#orgSubstanceId").val(),
		"staff.staffBirthPalce" : $("#staffBirthPalce").val(),
		"staff.staffAddress" : $("#staffAddress").val(),
		"staff.staffEmail" : $("#staffEmail").val(),
		"substanceId" : $("#substanceId").val()
	};
	var myurl = "updateStaffAction";
	$.ajax( {
		url : myurl,
		data : values,
		type : "POST",
		dataType : 'text',
		success : function(result) {
			if (result = "success") {
				alert("修改成功");
				window.location.href = "updateQueryStaffMain.html";
			} else {
				alert("修改失败");
			}
		},
		error : function(result) {
			alert("修改失败");
		}

	});
}
