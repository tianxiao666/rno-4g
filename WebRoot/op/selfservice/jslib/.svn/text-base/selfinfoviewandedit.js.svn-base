$(document).ready(function() {
	//
		$(".modify_personal_info").click(function() {
			$(".modified_person_info").hide();
			$(".now_person_info").show();
			//保存、取消按钮可用
				$("#saveBtn").removeAttr("disabled");
				$("#cancelBtn").removeAttr("disabled");
				
				//拷贝值
				$("#emailAddress").val($("#hidden_emailAddress").val());
				$("#backUpEmailAddress").val($("#hidden_backUpEmailAddress").val());
				$("#cellPhoneNumber").val($("#hidden_cellPhoneNumber").val());
				$("#mobileEmailAddress").val($("#hidden_mobileEmailAddress").val());
			})
				$(".cancel_personal_info").click(function() {
					$(".now_person_info").hide();
					$(".modified_person_info").show();
				})

		$("#personalInfoForm").validate( {
			debug : true,
			focusCleanup : false,
			errorClass : "error",
			validClass : "success",
			success : function(label) {
				label.addClass("repeat_info_right").text("Ok!")
			},
			submitHandler : function(form) {
				 $(form).ajaxSubmit({
					 success:function(d){
					    var data=eval("("+d+")");
					    if(data['flag']==true){
					    	alert("修改成功");
					    	window.location.href="loadSelfServiceInfoViewAndEditAction";
					    }else{
					    	alert("修改失败！");
					    }
					 }
				 });
			}
		});
	
		
	});
