
window.onload = function(){
	//setContentIframeHeightWidth();
	$(window).resize(function(){
		//setContentIframeHeightWidth();
	});
}

//设置内容iframe高宽
function setContentIframeHeightWidth () {
	//设置内容宽度
	var menu_width = $(".projectInfoManage_menu").width();	
	var window_width = $(window).width();
	var content_width = parseInt( menu_width / window_width * 100 );
	content_width = 99 - content_width +"%";
	$("#right_content").css({"width":content_width});
	$("#content_iframe").css({"width":"100%"});
	
	var content_height = $(".projectInfoManage_menu").outerHeight();
	$("#right_content").css({"height" : content_height+"px" });
	$("#content_iframe").css({"min-height" : content_height+"px" });
}

$(function(){

	/****************** 美工代码 begin ********************/
	
	
	//查看休息项目信息弹出框
	$(".project_showBtn").click(function(){
		$("#project_Dialog").show();
		$(".black").show();
	});
	$(".dialog_closeBtn").click(function(){
		$("#project_Dialog").hide();
		$(".black").hide();
	});
	
	//添加项目信息弹出框
	$(".projectAdd_showBtn").click(function(){
		$("#projectAdd_Dialog").show();
		$(".black").show();
	});
	$(".dialog_closeBtn").click(function(){
		$("#projectAdd_Dialog").hide();
		$(".black").hide();
	});
	
	/****************** 美工代码 end ********************/
	
	
	/****************** 菜单 begin **********************/
	
	//左侧菜单点击
	$(".projectInfoManage_menu li").click(function(){
		var title = $(this).attr("title");
		var url = iframeUrl[title];
		$("#content_iframe").attr({"src":url});
	});
	
	/****************** 菜单 end **********************/
	
	$("#content_iframe").load(function(){
		var title = $(this).contents().find("title").text();
		$(document).find("title").text(title);
	});
})

var iframeUrl = {
	"企业信息管理" : "informationIndex_enterprise_manage_info_list.jsp" , 
	"项目基本信息管理" : "informationIndex_project_base_info_list.jsp" , 
	"项目网络资源" : "informationIndex_networkresource_manage_info_list.jsp"
};

/************* 页面 ***********/
$(document).ready(function(){
	$(".projectInfoManage_menuInfo").click(function(){
		$(".menu_selected").removeClass("menu_selected");
		$(this).find("li").addClass("menu_selected");
	});
})
