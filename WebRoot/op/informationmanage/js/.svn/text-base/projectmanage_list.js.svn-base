
//projectPage - 项目信息列表分页实例
//project_look_dialog - 查看项目信息对话框实例
//updateAreaTreeView - 更新对话框地区数
var uploadInput = "";

$(document).ready(function(){
		//更新、保存对话框的区域树
		areaTree();
		//企业信息列表
		
		find_all_project_list();
		
		//读取服务商客户企业信息
		find_server_client_enterprise_map();
		
})
function areaTreeUpdateClick( data , tableId ){
		//alert("update");
		data = eval("(" + data + ")");
		$("#update_city_txt").val(data.name);
		$("#update_cityId").val(data.areaId);//data.id
		$("#update_area_treeDiv").toggle(200);															
	}
function areaTreeAddClick( data , tableId ){
		//alert("add");
		data = eval("(" + data + ")");
		$("#save_city_txt").val(data.name);
		$("#save_cityId").val(data.areaId);
		$("#save_area_treeDiv").toggle(200);															
	}			
			
			
			
//更新、保存对话框的区域树
function areaTree () {
	var url = "information_area_ajax!areaTreeAction.action";
	$.ajax({
		"url" : url , 
		"type" : "post" , 
		"data" : { "max" : 2 , "provinceId" : 0 } , 
		"async" : false , 
		"success" : function( result ) {
			result = eval ( "(" + result + ")" );
			//更新
			createAllAreaTree(result,"update_area_treeDiv","update_area_tree","","areaTreeUpdateClick");
			//添加	
			createAllAreaTree(result,"save_area_treeDiv","add_area_tree","","areaTreeAddClick");
			//更新
		/*	createAreaTree( $("#update_area_treeDiv") , 
							result , 
							"update_areaTree" , 
							function( lv , data ){
							alert("xxx:"+data);
								$("#update_city_txt").val(data.name);
								$("#update_cityId").val(data.areaId);//data.id
								$("#update_area_treeDiv").toggle(200);															
							});
			
			//添加				
			createAreaTree( $("#save_area_treeDiv") , 
							result , 
							"save_areaTree" , 
							function( lv , data ){
								$("#save_city_txt").val(data.name);
								$("#save_cityId").val(data.areaId);
								$("#save_area_treeDiv").toggle(200);															
							});		*/		
		}
	});
}



//查找客户服务商企业信息
function find_server_client_enterprise_map () {
	$.ajax({
			"url" : "enterprisemanage_ajax!findClientServerEnterpriseInfo.action" , 
			"type" : "post" , 
			"success" : function( result ) {
							result = eval ( "(" + result + ")" );
							for ( var key in result ) {
								var list = result[key];
								var select = null;
								if ( key == "server" ) {
									select = $(".server_select");
								} else if ( key == "client" ) {
									select = $(".client_select");
								}
								$(select).empty();
								$("<option value=''>请选择</option>").appendTo($(select));
								for ( var i = 0 ; list != null && i < list.length ; i++ ) {
									var info = list[i];
									var option = $("<option/>");
									var enterName = getObjectData(info,"fullName","");
									$(option).attr({"enterId":info.id});
									$(option).text(enterName).val(enterName);
									$(option).appendTo($(select));
								}
							}
						} 
	});
}


//查找所有企业信息
function find_all_project_list () {
	//读取所有企业信息
	$.ajax({
			"url" : "projectmanage_ajax!findAllProjectInfoAjax.action" , 
			"type" : "post" , 
			"success" : function( result ) {
							result = eval ( result );
							projectPage.setDataArray(result);
							projectPage.refreshTable();
							projectPage.checkButton();
						} 
	});
}



//居中显示对话框
function dialogShow ( dialog , speed , isupdate ) {
	
	//默认时间
	var from_date = new Date().toString("yyyy-MM-dd");
	var end_date = new Date().addYears(1).getLastDate().toString("yyyy-MM-dd");
	$("#save_startDate").val(from_date);
	$("#save_planEndDate").val(end_date);
	if ( $("#update_startDate").val() == "" ) {
		$("#update_startDate").val(from_date);
	}
	if ( $("#update_planEndDate").val() == "" ) {
		$("#update_planEndDate").val(end_date);
	}
	
	
	setTimeout(function(){
		$("#save_client_treeDiv").css({"display":"block"});
		$("#save_server_treeDiv").css({"display":"block"});
		$("#save_client_treeDiv>ul>li").eq(0).find(">span").click();
		$("#save_server_treeDiv>ul>li").eq(0).find(">span").click();
		
		
		
		setTimeout(function(){
			
			$("#save_client_treeDiv").css({"display":"none"});
			$("#save_server_treeDiv").css({"display":"none"});
			$("#update_server_treeDiv").css({"display":"none"});
			$("#update_client_treeDiv").css({"display":"none"});
			$("#save_area_treeDiv").css({"display":"none"});
			$("#update_area_treeDiv").css({"display":"none"});
			var window = $(dialog).parents(document);
			var window_width = $(document).width();
			var window_height = $(document).height();
			
			
			var dialog_width =  $(dialog).width() + 10 ;
			var dialog_height =  $(dialog).height();
			
			var dialog_top = ( ( window_height / 2 ) - ( dialog_height / 2 ) ) - 100;
			var dialog_left = ( window_width - dialog_width )/ 2;
			//alert(dialog_top + " " + dialog_left );
			$(dialog).css({ 
					"top" : 100+"px", 
					"left" : dialog_left+"px" 
			});
			$(dialog).fadeIn(speed);
		},500);
		
	},500);
	
}


//美工代码
$(function(){
	//显示-隐藏服务商组织树
	$("#update_server_choice_btn").click(function(){
		$("#update_server_treeDiv").slideToggle("fast");
	}); 
	
	$("#save_server_choice_btn").click(function(){
		$("#save_server_treeDiv").slideToggle("fast");
	});
	
	$("#save_client_choice_btn").click(function(){
		$("#save_client_treeDiv").slideToggle("fast");
	});
		
	//显示-隐藏服务商组织树
	$(".client_treeButton").click(function(){
		$("#update_client_treeDiv").slideToggle("fast");
	});
	$(".cancelBtn").click(function(){
		$(".dialog_closeBtn").click();
	});
	//查看企业信息弹出框
	$(".dialog_closeBtn").click(function(){
		$("#project_Dialog").fadeOut(200);
		$(".black").fadeOut(200);
	});
	//添加企业信息弹出框
	$(".projectAdd_showBtn").click(function(){
		$("#projectAdd_Dialog :text,#projectAdd_Dialog textarea,#projectAdd_Dialog :file").val("");
		saveServerProviderOrgTree();
		saveClientProviderOrgTree();
		
		setTimeout( function () {
			dialogShow($("#projectAdd_Dialog"),200);
			$(".black").fadeIn(200);
		},500);
	});
	$(".dialog_closeBtn").click(function(){
		//ou.jh 2013-04-09 14:12:00 清除验证提示.
		$(".update_project_form_span").remove();
		$("#projectAdd_Dialog").hide();
		$(".black").hide();
	});
})


function fClearDate() {
	
}

