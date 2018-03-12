	
	$(document).ready(function(){
		providerOrgTree();
		//打开选择区域
		$("#choose_button").click(function(){
			$("#chooseOrgDiv").slideToggle("fast");
		});
		orgModule();
	    
		//选择图片
		$("#carImgFileChoiceBtn,#car_info_left_img").click(function(){
			$("#carFile").click();
		});
		
		$("#carFile").change(function(){
			$("#car_info_left_img").attr({"src":"images/big_loading.gif"});
			$("#car_img_form").ajaxSubmit( function ( data ) {
				$("#car_info_left_img").attr({"src":data});
				$("#carPic").val(data);
			});
		});
	});

	function orgModule() {
		//伍俊倪调用
		var url = subUrlParamString(location.href);
		url_obj = getUrlParamStringToObj(url);
		var bizId = url_obj.bizId;
		var module = url_obj.module;
		if ( !module || module == "undefined" ) {
			$("[org='true']").show();
			formcheck({
				"form" : $("#carForm") , 
				"subButton" : $("#addCarButton") , 
				"isAjax" : true , 
				"ajaxSuccess" : function( data ){
					data = eval( "(" + data + ")" );
					var flag = confirm("车辆已经添加成功，是否继续添加司机呢？");
					var url = "";
					var urlOpt = {};
					if ( flag ) {
						url = "carflow_saveBindDriver.jsp";
						urlOpt = {
							"bizunitInstanceId" : data.carBizId , 
							"carId" : data.carId
						};
					} else {
						url = "cargeneral_index.jsp";
						urlOpt = {
							"enable" : 1 , 
							"carId" : data.carId
						};
					}
					url = createUrl ( url , urlOpt );
					location.href = url;
				}
			});
			return;
		}
		$("#bizunitText").val(bizId);
	 	var bizName = $("#chooseOrgDiv :hidden[value='"+bizId+"']").prev("span").text();
	 	$("#bizunitNameText").val(bizName);
	 	$("[org='false']").show();
	 	
		formcheck({
			"form" : $("#carForm") , 
			"subButton" : $("#addCarButtonByOrg") , 
			"isAjax" : true , 
			"ajaxSuccess" : function( data ){
				alert("车辆保存成功！");
				window.close();
			}
		});
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
			$("#carBizName").val(data.name);
			$("#carBizId").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}  
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"chooseOrgDiv","carmanage_org_div","a","orgTreeClick");
			},"json");
		}
	});
	
}
function a(){}

//显示组织信息
function orgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#carBizName").val(data.name);
	$("#carBizId").val(data.orgId);
	$("#chooseOrgDiv").slideUp("fast");
} 
