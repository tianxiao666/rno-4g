
//enterprisePage - 企业信息列表分页实例
//enterprise_look_dialog - 查看企业信息对话框实例

$(document).ready(function(){
		
		/******************** 添加对话框 begin **********************/
		//验证
		formcheck({
			"form" : $("#save_enterprise_form") , 
			"subButton" : $("#saveEnterprise_btn") , 
			"isAjax" : true , 
			"formSubmiting" : function () {
				var suffix = "@" + $("#save_suffix").val();
				$("#save_enterpriseSuffix").val(suffix);
				
				var description_txt = $("#enterprise_save").val();
                if (description_txt.length > 200 ) {
                  // alert("经营范围不能超过200个字符");
                  $("#enterprise_div_save").html("经营范围不能超过200个字符");
                  $("#enterprise_save").focus();
                   return false;
                 } 
               
			} ,
			"ajaxSuccess" : function( data ){
				data = parseInt(data);
				if ( data > 0 ) {
					alert("操作成功!");
					find_all_enterprise_list();
					$(".dialog_closeBtn").click();
				}
			}
		});
		/******************** 添加对话框 end **********************/
		
})




