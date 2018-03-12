$(document).ready(function() {
	organizationSelect();
	validateOrganization();
	$("#orgParentCode").change(function() {
		var orgCode = $("#orgParentCode").find('option:selected').attr("name");
		var orgLevel = $("#orgParentCode").find('option:selected').attr("id");
		$("#orgLevel").val(parseInt(orgLevel)+1);
		$("#orgCode").val(orgCode);
	});
	checkBoxCopy();
});

function checkBoxCopy() {
	$("#orgPhoneCheckbox").click(function() {
		if ($(this).attr("checked") == true) {
			var id = $("#orgParentCode").val() + "";
			if (id != "0") {
				var myurl = "getOrganizationByIdAction?id=" + id;
				$.ajax( {
					url : myurl,
					type : "POST",
					dataType : 'json',
					success : function(result) {
						$("#orgPhone").val(result.organization.orgPhone);
					}
				});
			}

		} else {
			$("#orgPhone").val("");
		}
	});

	$("#orgFaxCheckbox").click(function() {
		if ($(this).attr("checked") == true) {
			var id = $("#orgParentCode").val() + "";
			if (id != "0") {
				var myurl = "getOrganizationByIdAction?id=" + id;
				$.ajax( {
					url : myurl,
					type : "POST",
					dataType : 'json',
					success : function(result) {
						$("#orgFax").val(result.organization.orgFax);
					}
				});
			}
		} else {
			$("#orgFax").val("");
		}
	});

	$("#orgHomePageCheckbox").click(function() {
		if ($(this).attr("checked") == true) {
			var id = $("#orgParentCode").val() + "";
			if (id != "0") {
				var myurl = "getOrganizationByIdAction?id=" + id;
				$.ajax( {
					url : myurl,
					type : "POST",
					dataType : 'json',
					success : function(result) {
						$("#orgHomePage").val(result.organization.orgHomePage);
					}
				});
			}
		} else {
			$("#orgHomePage").val("");
		}
	});

	$("#orgAddressCheckbox").click(function() {
		if ($(this).attr("checked") == true) {
			var id = $("#orgParentCode").val() + "";
			if (id != "0") {
				var myurl = "getOrganizationByIdAction?id=" + id;
				$.ajax( {
					url : myurl,
					type : "POST",
					dataType : 'json',
					success : function(result) {
						$("#orgAddress").val(result.organization.orgAddress);
					}
				});
			}
		} else {
			$("#orgAddress").val("");
		}
	});
}

function validateOrganization() {
	$("#addOrganization").validate( {
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
							return $("#orgName").val();
						}
					}
				}
			},
			orgCode : {
				remote : {
					url : "validateOrgCodeAction", //后台处理程序 
					type : "post", //数据发送方式 
					dataType : "json", //接受数据格式    
					data : { //要传递的数据 
						orgCode : function() {
							return $("#orgCode").val();
						}
					}
				}
			}
		},
		messages : {
			orgName : {
				remote : '已存在'
			},
			orgCode : {
				remote : '已存在'
			}
		},
		submitHandler : function(form) {
			onSubmit();
		}

	});
}

function organizationSelect() {
	var organizationUrl = "getAllOrganizationsAction";
	//动态加载组织选择框
	$.ajax( {
		url : organizationUrl,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			$.each(result, function(key, value) {
				$("#orgParentCode").append(
						"<option  value=" + value.organization.substanceId + "  name="
								+ value.organization.orgCode + "  id=" + value.organization.orgLevel
								+ ">" + value.organization.orgName + "</option>");
			});
		}
	});
}
/**
 * 获取页面的数据通过ajax提交
 */
function onSubmit() {
	var values = {
		"organization.orgName" : $("#orgName").val(),
		"orgParentName" : $("#orgParentCode").find(
				"option:selected").text(),
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
	var myurl = "addOrganizationAction";
	$.ajax( {
		url : myurl,
		data : values,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			if (result != "false") {
				alert("成功");
          window.location.href= "organizationDetailForView.html?id="+result.substanceId;
			} else {
				alert("添加失败!");
			}

		},
		error : function(result) {
			alert("添加失败!");
		}

	});
}
