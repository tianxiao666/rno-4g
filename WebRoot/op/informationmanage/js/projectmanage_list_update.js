

$(document).ready(function(){
	
	updateServerProviderOrgTree();
	updateClientProviderOrgTree();
	
	/** 文件上传 ******/
	var update_uploadFile = new UploadUtil({
												"fileUrl" : "informationmanage" , 
												"uploaded" : function ( result ) {
														var data = eval( "(" + result + ")" );
														$("#update_upload_text").val(data.filePath);
														$("#update_upload_hidden").val(data.filePath);
												 }
											});
	$("#update_upload_btn").click(function(){
		update_uploadFile.uploadFile();
	});
	
	
	
	
	//验证
	checkValue({
		"form" : $("#update_checkproId_div") , 
		"formName" : "update_project_form" , 
		"subButton" : $("#update_checkProjectProId_btn") , 
		"isAjax" : true , 
		"ajaxSuccess" : function( data ){
			return false;
		}
	});
	$("#update_project_server_enterprise_select").change(function(){
		$("#update_server_choice_bizName").val("");
		$("#update_server_orgId").val("");
		updateServerProviderOrgTree();
	});
	
	$("#update_project_client_enterprise_select").change(function(){
		$("#update_client_choice_bizName").val("");
		$("#update_client_orgId").val("");
		updateClientProviderOrgTree();
	});
	$("#update_area_treeView_btn").click(function(){
		$("#update_area_treeDiv ").toggle();
	});
	//验证
	formcheck({
		"form" : $("#update_project_form") , 
		"subButton" : $("#updateProject_btn") , 
		"isAjax" : true , 
		"ajaxSuccess" : function( data ){
			data = parseInt(data);
			find_all_project_list();
			$(".dialog_closeBtn").click();
			if ( data > 0 ) {
				alert("操作成功!");
			} else {
				alert("操作失败!");
			}
		}
	});
	
	$("#update_project_form .formcheckspan").attr("editButton","true");
	showedit("#project_Dialog");
	
	//删除企业信息
	$("#deleteEnterprise_btn").click(function(){
		delete_single_enterprise();
	});
	
	$("#update_client_choice_btn").click(function(){
		$("#update_client_treeDiv").toggle(500);
	});
	/*
	$("#update_server_choice_btn").click(function(){
		$("#update_server_treeDiv").toggle(500);
	});*/
	
	$("#delete_btn").click(function(){
		$.ajax({
			"url" : "projectmanage!deleteProjectInfo.action" , 
			"type" : "post" , 
			"data" : { "id" : $("#update_project_id").val() } , 
			"async" : true , 
			"success" : function( result ) {
				if ( result ) {
					result = parseInt(result);
					find_all_project_list();
					if ( result > 0 ) {
						alert("操作成功!");
					} else {
						alert("操作失败!");
					}
				}
				$(".dialog_closeBtn").click();
			}
		});
	});
})




/***************** 更新组织架构树 begin **********************/

//TODO need to modify 
//生成客户的组织架构树
function updateClientProviderOrgTree(){
	var orgId = $("#update_project_client_enterprise_select").find(":selected").attr("enterid");
	if(orgId==null||orgId==""){
		return;
	}
	var values = {"enterpriseId":orgId,"enterpriseType":"CARRIEROPERATOR"};
	var myUrl = "../organization/getProviderOrgTreeByEnterpriseIdAjaxAction";
	//alert("客户update："+values);
	$.post(myUrl,values,function(data){
		newCreateOrgTree(data,"update_client_treeDiv","update_client_tree","a","updateDialogClientOrgTreeClick");
	},"json");
}


//显示客户的组织信息
function updateDialogClientOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	//var url = "information_foreign_ajax!findClientUpOrgListByOrgId.action" ;
	var url = "information_foreign_ajax!findTopOrgLstByOrgId.action" ;
	$.ajax({
		"url" : url , 
		"type" : "post" , 
		"data" : { "orgId" : orgId } , 
		"success" : function( result ) {
						var orgStr = "";
						var selected_orgId = "";
						   
						result = eval ( "(" + result + ")" );
						if ( result != null && result.length > 0 ) {
							for ( var i = 0 ; i < result.length ; i++ ) {
								var info = result[i];
								selected_orgId = info.orgId ;
								if ( i == 0 ) {
									//orgStr = info.name + orgStr;
									orgStr += info.name;
								} else {
									//orgStr = info.name + "/" + orgStr;
									orgStr = orgStr + "/" +info.name ;
								}
							}
							$("#update_client_choice_bizName").val(orgStr);
							$("#update_client_orgId").val(selected_orgId);//result[0].orgId
							$("#update_client_choice_btn").click();
						}
					}
	});
}



 

//生成服务商的组织架构树
function updateServerProviderOrgTree(){
	var enterId = $("#update_project_server_enterprise_select").find(":selected").attr("enterId");
	if(enterId==null||enterId==""){
		return;
	}
	var values = {"enterpriseId":enterId,"enterpriseType":"SERVER"};
	var myUrl = "../organization/getProviderOrgTreeByEnterpriseIdAjaxAction";
	//alert("服务商update："+values);
	$.post(myUrl,values,function(data){
		newCreateOrgTree(data,"update_server_treeDiv","update_server_tree","a","updateDialogServerOrgTreeClick");
	},"json");
}

function a(){} 

//显示服务商的组织信息
function updateDialogServerOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	//var url = "information_foreign_ajax!findServerUpOrgListByOrgId.action" ;
	var url = "information_foreign_ajax!findTopOrgLstByOrgId.action" ;
	$.ajax({
		"url" : url , 
		"type" : "post" , 
		"data" : { "orgId" : orgId } , 
		"success" : function( result ) {
						var orgStr = "";
						result = eval ( "" + result + "" );
						var selected_orgId = "";
						for ( var i = 0 ; i < result.length ; i++ ) {
							
							var info = result[i];
							selected_orgId = info.orgId ;
							if ( i == 0 ) {
								//orgStr = info.name + orgStr;
								orgStr += info.name;
							} else {
								//orgStr = info.name + "/" + orgStr;
								orgStr = orgStr + "/" +info.name ;
							}
							
						}
						$("#update_server_choice_bizName").val(orgStr);
						$("#update_server_orgId").val(selected_orgId);//
						$("#update_server_choice_btn").click();
					}
	});
}

/***************** 更新组织架构树 end **********************/





//查看单个企业的信息
function find_single_project ( data ) {
	$.ajax({
			"url" : "projectmanage_ajax!findSingleProjectInfo.action" , 
			"type" : "post" , 
			"data" : { "id" : data.id } , //data.id
			"async" : false ,
			"success" : function( result ) {
				result = eval ( "(" + result + ")" ) ;
				project_look_dialog = new Project( result );
				project_look_dialog.putInfo($("#project_Dialog"));
				setTimeout(function(){
					updateClientProviderOrgTree();
					updateServerProviderOrgTree();
				},500);
			}
		});
}

//删除单个企业的信息
function delete_single_project () {
	var id = $("#enterprise_update_id").val();
	$.ajax({
			"url" : "projectmanage_ajax!deleteProjectInfo.action" , 
			"type" : "post" , 
			"data" : { "id" : id } , 
			"async" : true ,
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
