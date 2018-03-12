

$(document).ready(function(){
	/******************** 更新对话框 begin **********************/
	//验证
	formcheck({
		"form" : $("#update_enterprise_form") , 
		"subButton" : $("#updateEnterprise_btn") , 
		"isAjax" : true , 
		"formSubmiting" : function () {
			 	//alert('ok');
				var description_txt = $("#enterprise_update").val();
                if (description_txt.length > 200 ) {
                   //alert("经营范围不能超过200个字符");
                     $("#enterprise_div_update").html("经营范围不能超过200个字符");
                     $("#enterprise_update").focus();
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
	$("#update_enterprise_form .formcheckspan").attr("editButton","true");
	showedit("#ent_Dialog");
	
	//删除企业信息
	$("#deleteEnterprise_btn").click(function(){
		var enterpriseName = $("#update_enterpriseName").text();
		var flag = confirm("是否想要删除【" + enterpriseName + "】企业的信息？");
		if ( flag ) {
			delete_single_enterprise();
		}
	});
	/******************** 更新对话框 end **********************/
	
	
	
})



/***************** 更新对话框 begin ********************/

//查看单个企业的信息
function find_single_enterprise ( data ) {
	$.ajax({
			"url" : "enterprisemanage_ajax!findSingleEnterpriseInfo.action" , 
			"type" : "post" , 
			"data" : { "id" : data.id } , 
			"async" : false ,
			"success" : function( result ){
				result = eval ( "(" + result + ")" ) ;
				enterprise_look_dialog = new Enterprise( result );
				enterprise_look_dialog.putInfo("#ent_Dialog");
			}
		});
}

//删除单个企业的信息
function delete_single_enterprise () {
	var id = $("#enterprise_update_id").val();
	$.ajax({
			"url" : "enterprisemanage_ajax!deleteEnterpriseInfo.action" , 
			"type" : "post" , 
			"data" : { "id" : id } , 
			"async" : false ,
			"success" : function( result ){
				result = parseInt (result);
				if ( result > 0 ) {
					alert("操作成功！");
					find_all_enterprise_list();
					$(".dialog_closeBtn").click();
				}
			}
		});
}
