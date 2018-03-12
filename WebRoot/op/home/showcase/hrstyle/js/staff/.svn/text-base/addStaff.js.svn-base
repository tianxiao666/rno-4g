$(document).ready(function() {
	showOrganization();
    validateStaff();
}); 
//验证表单
function validateStaff() {
	$("#addStaff").validate( {
		errorPlacement : function(error, element) {
			error.appendTo(element.next().next().addClass("color: red;"));
		},
		rules : {
			staffCode : {
				remote : {
					url : "validateStaffCodeAction", //后台处理程序 
					type : "post", //数据发送方式 
					dataType : "json", //接受数据格式    
					data : { //要传递的数据 
			staffCode : function() {
							return $("#staffCode").val();
						}
					}
				}
			},
			staffEmail : {
				remote : {
					url : "validateStaffEmailAction", //后台处理程序 
					type : "post", //数据发送方式 
					dataType : "json", //接受数据格式    
					data : { //要传递的数据 
				staffEmail : function() {
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
			onSubmitStaff();
		}

	});
}


/**
 * 获取页面的数据通过ajax提交
 */
function showOrganization(){
	var myurl ="getAllOrganizationsAction";
	//动态加载组织选择框
	$.ajax({
           url : myurl,
           cache : false,
           data:{},
           async : true,
           type : "POST",
           dataType : 'json',
           success : function (result){
				$.each(result, function(key, value) {
						$("#orgSubstanceId").append(
								"<option value=" + value.organization.substanceId + ">"
										+ value.organization.orgName + "</option>");

				});
           }
       });
}

/**
 * 获取页面的数据通过ajax提交
 */
function onSubmitStaff(){
	var values={
		"orgSubstanceId":$("#orgSubstanceId").val(),
		"staff.staffCode":$("#staffCode").val(),
		"staff.name":$("#staffName").val(),
		"staff.staffIdentity":$("#staffIdentity").val(),
		"staff.staffGender":$("#staffGender").val(),
		"staff.staffPhone":$("#staffPhone").val(),
		"staff.staffEnrollDay":$("#enrollDay").val(),
		"staff.staffPoliticalStatus":$("#staffPoliticalStatus").val(),
		"staff.staffBirthPalce":$("#staffBirthPalce").val(),
		"staff.staffAddress":$("#staffAddress").val(),
		"staff.staffEmail":$("#staffEmail").val()
	}  ;
	var myurl ="addStaffAction";
	$.ajax({
           url : myurl,
           data:values,
           type : "POST",
           dataType : 'text',
           success : function (result){
               var re = result ;
              if(re=="failed1"){
            	   alert("添加失败!");
               }else {
	             window.location.href = "staffDetailForView.html?id="+re;
               }
           }
       });
}
