
//enterprisePage - 企业信息列表分页实例
//enterprise_look_dialog - 查看企业信息对话框实例

$(document).ready(function(){
		//企业信息列表
		find_all_enterprise_list();
})

//查找所有企业信息
function find_all_enterprise_list () {
	//读取所有企业信息
	$.ajax({
			"url" : "enterprisemanage_ajax!findAllEnterpriseInfoAjax.action" , 
			"type" : "post" , 
			"success" : function( result ) {
							result = eval ( result );
							enterprisePage.setDataArray(result);
							enterprisePage.refreshTable();
							enterprisePage.checkButton();
						} 
	});
	
}

//居中显示对话框
function dialogShow ( dialog , speed ) {
	
	var window = $(dialog).parents(document);
	var window_width = $(document).width();
	var window_height = $(document).height();
	
	var dialog_width =  $(dialog).width() + 10;
	var dialog_height =  $(dialog).height();
	
	var dialog_top = ( window_height / 2 ) - ( dialog_height / 2 );
	var dialog_left = ( window_width / 2 ) - ( dialog_width / 2 );
	//alert(dialog_top + " " + dialog_left );
	$(dialog).css({ 
			"top" : 100+"px" , 
			"left" : dialog_left+"px" 
	});
	$(dialog).fadeIn(speed);
}


//美工代码
$(function(){
	//查看企业信息弹出框
	$(".dialog_closeBtn").click(function(){
		$("#ent_Dialog").fadeOut(200);
		$(".black").fadeOut(200);
	});
	//添加企业信息弹出框
	$(".entAdd_showBtn").click(function(){
		dialogShow($("#entAdd_Dialog"),200);
		$(".black").fadeIn(200);
		$("#save_enterprise_form").find(":reset").click();
	});
	$(".dialog_closeBtn").click(function(){
		$("#entAdd_Dialog").hide();
		$(".black").hide();
	});
})

