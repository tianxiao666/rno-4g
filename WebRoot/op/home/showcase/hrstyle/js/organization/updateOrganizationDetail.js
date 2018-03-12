$(document).ready(function() {
	showUpdateOrganization();
	validateOrganization();
});

/**
 * 获取页面的数据通过ajax提交
 */
function showUpdateOrganization() {
	var id = request("id");
	var myurl = "getOrganizationByIdAction?id=" + id;
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

function validateOrganization() {
	$("#updateOrganization").validate( {
		errorPlacement : function(error, element) {
			error.appendTo(element.next().next().addClass("color: red;"));
		},

		rules : {
			orgName : {
				    remote : {
					url : "validateOrgNameAction", //后台处理程序 
					type : "post", //数据发送方式 
					dataType : "json", //接受数据格式    
					data : { //要传递的数据 
						orgName : function() {

							if ($("#orgName").val() == $("#orgName2").val()) {
								return true;
							}
							return $("#orgName").val();
						}
					}
				}
			}
		},
		messages : {
			orgName : {
				remote : '已存在'
			}
		},
		submitHandler : function(form) {
			onUpdateSubmit();
		}

	});
}

/**
 * 将通过ajax请求获取到的内容填充到页面
 * @param {Object} data
 */
function fillUpdateData(data) {
	var organization = data.organization;
	var orgSetupDay = organization.orgSetupDay.toString().split(" ", 1);
	$("#orgName").val(organization.orgName);
	$("#orgName2").val(organization.orgName);
	$("#orgParentCode").val(data.orgParentCode);
	$("#orgParentName").val(data.orgParentName);
	$("#orgParentName2").html(data.orgParentName);
	$("#orgSetupDay").val(orgSetupDay);
	$("#orgChargeMan").val(organization.orgChargeMan);
	$("#orgCode").val(organization.orgCode);
	$("#orgCode2").html(organization.orgCode);
	$("#orgLevel").val(organization.orgLevel);
	$("#orgLevel2").html(organization.orgLevel);
	$("#orgStatus").val(organization.orgStatus);
	$("#orgProvince").val(organization.orgProvince);
	$("#orgCity").val(organization.orgCity);
	$("#orgPostCode").val(organization.orgPostCode);
	$("#orgHomePage").val(organization.orgHomePage);
	$("#orgPhone").val(organization.orgPhone);
	$("#orgFax").val(organization.orgFax);
	$("#orgAddress").val(organization.orgAddress);
	$("#orgResponsibility").val(organization.orgResponsibility);
	$("#substanceId").val(organization.substanceId);

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
		"id" : $("#substanceId").val(),
		"organization.orgName" : $("#orgName").val(),
		"orgParentName" : $("#orgParentName").val(),
		"orgParentCode" : $("#orgParentCode").val(),
		"organization.orgCode" : $("#orgCode").val(),
		"organization.orgLevel" : $("#orgLevel").val(),
		"organization.orgChargeMan" : $("#orgChargeMan").val(),
		"organization.orgPhone" : $("#orgPhone").val(),
		"organization.orgSetupDay" : $("#orgSetupDay").val(),
		"organization.orgFax" : $("#orgFax").val(),
		"organization.orgProvince" : $("#orgProvince").val(),
		"organization.orgCity" : $("#orgCity").val(),
		"organization.orgPostCode" : $("#orgPostCode").val(),
		"organization.orgAddress" : $("#orgAddress").val(),
		"organization.orgHomePage" : $("#orgHomePage").val(),
		"organization.orgResponsibility" : $("#orgResponsibility").val()
	}

	var myurl = "updateOrganizationAction";
	$.ajax( {
		url : myurl,
		data : values,
		type : "POST",
		dataType : 'text',
		success : function(result) {
			if (result = "success") {
				alert("修改成功");
				window.location.href = "updateQueryOrganizationMain.html";
			} else {
				alert("修改失败");
			}
		},
		error : function(result) {
			alert("修改失败");
		}

	});
}
