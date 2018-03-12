	


	
	$(function(){
		
		/******************* 绑定终端js begin ***********************/
		providerOrgTree();
		
		//查询车辆司机信息
		//查询车辆信息
		$.ajax( {
				"url" : "cardispatchManage_ajax!findCarInfoList.action",
				"type" : "post",
				"data" : {
					"car#carId" : carId 
				},
				"success" : function(result) {
					result = eval( "(" + result + ")" );
					if ( result.length > 0 ){
						var c = new Car(result[0]);
						c.putInfo("body");
					}
				}
		});
		$.ajax( {
				"url" : "cardispatchManage_ajax!findDriverInfoList.action",
				"type" : "post",
				"data" : {
					"driver#driverId" : driverId 
				},
				"success" : function(result) {
					result = eval( "(" + result + ")" );
					if ( result.length > 0 ){
						var d = new Driver(result[0]);
						d.putInfo("body");
					}
				}
		});
		
		
		
		//搜索终端按钮
		$("#mobile_search_btn").click(function(){
			$.ajax({
				"url" : "cardispatchManage_ajax!findMobileBindingList.action" , 
				"type" : "post" ,      
				"data" : { "isFree" : "true" , "terminal#clientimei" : $("#mobile_phone").val() , "terminal#terminalBizId" : $("#mobile_bizId").val() , "terminal#mobileType" : $("#mobileType").val() } , 
				"success" : function( result ) {
								result = eval ( result );
								mobileTablePage.setDataArray(result);
								mobileTablePage.refreshTable();
								mobileTablePage.checkButton();
							}  
			});
		});
		
				
		/******************* 绑定终端js end ***********************/
		
		/******************* 添加绑定终端js begin ****************************/
		//选择图片
		$("#mobileFile").change(function(){
			$("#mobile_info_left_img").attr({"src":"images/big_loading.gif"});
			$("#mobile_img_form").ajaxSubmit( function ( data ) {
				$("#mobile_info_left_img").attr({"src":data});
				$("#terminalPic").val(data);
			});
		});
		
		
		/******************* 添加绑定终端js end ***********************/
		
		 
		$(".prevPageBtn").click(function(){
			backToDriverPage();
		});
	})
	
	
	var submitFlag = false;
	$(document).ready(function () {
		formcheck({
			"form" : $("#saveBindCMForm") , 
			"subButton" : $("#mobileAddBindBtn") , 
			"isAjax" : true , 
			"ajaxSuccess" : function( data ) {
				data = eval( "(" + data + ")" );
				var flag = confirm("终端已经添加成功，是否需要查看车辆的综合信息？");
				var url = "";
				var urlOpt = {};
				if ( flag ) {
					url = "cargeneral_index.jsp";
					urlOpt = {
						"enable" : 1 , 
						"carId" : carId
					};
				} else {
					windowClose(data);
				}
				url = createUrl ( url , urlOpt );
				location.href = url;
			}
		});		
				
		$("#bindBtn").click(function(){
			$("#form").ajaxSubmit(function(data){
				var flag = confirm("终端已经绑定成功，是否需要查看车辆的综合信息？");
				var url = "";
				var urlOpt = {};
				if ( flag ) {
					url = "cargeneral_index.jsp";
					urlOpt = {
						"enable" : 1 , 
						"carId" : carId
					};
				} else {
					windowClose(data);
				}
				url = createUrl ( url , urlOpt );
				location.href = url;
			});
		});
	})
	
	//上一步 - 返回修改车辆页面
	function  backToDriverPage() {
		var url = "carflow_saveBindDriver.jsp?carId=" + carId + "&bizunitInstanceId=" + bizunitInstanceId ;
		location.href=url;
	}
	
	
	
	
	
	
	
	
	
/***************** 组织结构 *******************/	
	
//生成组织架构树
function providerOrgTree(){
	var orgId = "16";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"async" : true , 
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.orgId;
			$("#mobile_bizName").val(data.name);
			$("#mobile_bizId").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}  
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"mobile_treeDiv","carmanage_org_div","a","orgTreeClick");
			},"json");
		}
	});
	
}
function a(){}

//显示组织信息
function orgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#mobile_bizName").val(data.name);
	$("#mobile_bizId").val(data.orgId);
	$("#mobile_treeDiv").slideUp("fast");
} 


/************** 页面js *************/

$(document).ready(function(){
	$("#choose_button").click(function(){
		$("#choose_place_div").slideToggle("fast");
	})
	$(".tab_1 ul li").each(function(index){
				$(this).click(function(){
					$(".tab_1 ul li").removeClass("ontab");
					$(this).addClass("ontab");
					$(this).find($("input:radio")).attr("checked","checked");
					$(".tab_content").hide();
					$(".tab_content").eq(index).show();
					})
			})
	$(".select_button").click(function(){
		$(this).parent().find($(".td_div")).slideToggle("fast");
	})
	/*弹出alert_div*/
	//更换终端
	$("#mobile_change_btn").click(function(){
		$("#mobile_div_table tbody").empty();
		$("#mobile_alert").fadeIn(200);
	})
	/*关闭alert_div*/
	$(".close_alert_div").click(function(){
		$(this).parents(".alert_div").fadeOut(100);
	});
	$("#mobile_biz_btn").bind( "click" , function(){
		$("#mobile_treeDiv").toggle("fast");
	});
})

//关闭窗体
function windowClose ( data ) {
	if (  window.opener != null ) {
		var doc = window.opener.document;
		$(doc).find("#firstPage").click();
	}
	window.close();
}