


$(document).ready(function(){
	saveServerProviderOrgTree();
	saveClientProviderOrgTree();
	
	
	/** 文件上传 ******/
	var update_uploadFile = new UploadUtil({
												"fileUrl" : "informationmanage" , 
												"uploaded" : function ( result ) {
														var data = eval( "(" + result + ")" );
														$("#save_upload_text").val(data.filePath);
														$("#save_upload_hidden").val(data.filePath);
												 }
											});
	$("#save_upload_btn").click(function(){
		update_uploadFile.uploadFile();
	});
	
	
	$("#save_custom_enterprise_select").change(function(){
		//ou.jh 2013-04-09 14:30:00 选择客户（甲方）时，清空现有客户负责组织预填
		$("#save_client_choice_bizName").val("");
		$("#save_client_orgId").val("");
		saveClientProviderOrgTree();
	});
	
	checkValue({
		"form" : $("#save_checkproId_div") , 
		"formName" : "save_project_form" , 
		"subButton" : $("#save_checkProjectProId_btn") , 
		"isAjax" : true , 
		"ajaxSuccess" : function( data ){
			return false;
		}
	});
	
	$("#save_project_server_enterprise_select").change(function(){
		//ou.jh 2013-04-09 14:27:00 选择服务商（乙方）时，清空现有服务商负责组织预填
		$("#save_server_choice_bizName").val("");
		$("#save_server_orgId").val("");
		saveServerProviderOrgTree();
	});
	$("#save_area_treeView_btn").click(function(){
		//alert("click");
		$("#save_area_treeDiv ").toggle();
	});
	
	 //验证
	formcheck({
		"form" : $("#save_project_form") , 
		"subButton" : $("#save_project_submit") , 
		"isAjax" : true , 
		"ajaxSuccess" : function( data ){
			data = parseInt(data);
			
			if ( data > 0 ) {
				alert("操作成功!");
			} else {
				alert("操作失败!");
			}
			find_all_project_list();
			$(".dialog_closeBtn").click();
		}
	});
})




/***************** 添加组织架构树 begin **********************/

//(添加)生成客户的组织架构树
function saveClientProviderOrgTree(){
	var orgId = $("#save_custom_enterprise_select").find("option:selected").attr("enterid");
	if(orgId==null||orgId==""){
		return;
	}
	//var values = {"enterpriseId":orgId};
	//var myUrl = "findCustomerOrgTreeByEnterpriseIdAction";
	var values = {"enterpriseId":orgId,"enterpriseType":"CARRIEROPERATOR"};
	var myUrl = "../organization/getProviderOrgTreeByEnterpriseIdAjaxAction";
	//alert("客户save："+values);
	$.ajax({
		"url" : myUrl , 
		"async" : false , 
		"type" : "post" , 
		"data" : values , 
		"dataType" : "json" , 
		"success" : function(data){
			newCreateOrgTree(data,"save_client_treeDiv","save_client_tree","a","saveDialogClientOrgTreeClick");
		}
	});
}


//(添加)显示客户的组织信息
function saveDialogClientOrgTreeClick(dataStr,tableId){
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
						result = eval ( "" + result + "" );
						for ( var i = 0 ; i < result.length ; i++ ) {
							var info = result[i];
							selected_orgId = info.orgId ;
							//alert(selected_orgId);
							if ( i == 0 ) {
								//orgStr = info.name + orgStr;
								orgStr += info.name;
							} else {
								//orgStr = info.name + "/" + orgStr;
								orgStr = orgStr + "/" +info.name ;
							}
						}
						
						//alert("last:"+selected_orgId);
						$("#save_client_choice_bizName").val(orgStr);
						$("#save_client_orgId").val(selected_orgId);//result[0].orgId
						$("#save_client_choice_btn").click();
					}
	});
}



 
//TODO  need to modify  
//(添加)生成服务商的组织架构树
function saveServerProviderOrgTree(){
	var enterId = $("#save_project_server_enterprise_select").find(":selected").attr("enterId");
	if(enterId==null||enterId==""){
		return;
	}
	//ou.jg 2013-04-11 16:35:00 修改生成服务商的组织架构树ACTION名
	var values = {"enterpriseId":enterId,"enterpriseType":"SERVER"};
	var myUrl = "../organization/getProviderOrgTreeByEnterpriseIdAjaxAction";
	//alert("服务商save："+values);
	$.ajax({
		"url" : myUrl , 
		"type" : "post" , 
		"async" : true , 
		"data" : values , 
		"dataType" : "json" , 
		"success" : function(data){
			createOrgTreeOpenFirstNode(data,"save_server_treeDiv","save_server_tree","a","saveDialogServerOrgTreeClick");
		}
	});
}


//(添加)显示服务商的组织信息
function saveDialogServerOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#save_server_orgId").val(orgId);
	//var url = "information_foreign_ajax!findServerUpOrgListByOrgId.action" ;
	var url = "information_foreign_ajax!findTopOrgLstByOrgId.action" ;
	$.ajax({
		"url" : url , 
		"type" : "post" , 
		"data" : { "orgId" : orgId } , 
		"async" : true , 
		"success" : function( result ) {
						var orgStr = "";
						var selected_orgId = "";
						result = eval ( "" + result + "" );
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
						$("#save_server_choice_bizName").val(orgStr);
						//$("#save_server_orgId").val(result[0].id);
						$("#save_server_choice_btn").click();
					}
	});
}

/***************** 添加组织架构树 end **********************/




