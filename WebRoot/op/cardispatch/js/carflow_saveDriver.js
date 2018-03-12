var driverTablePage;
var pageSize = [ 5 ];

$(function() {
	providerOrgTree();
	
	//获取当前登录人企业后缀
	$.ajax({
		"url" : "cardispatchForeign_ajax!findLoginUserEnterprise.action" , 
		"type" : "post" , 
		"async" : false , 
		"success" : function ( result ) {
			var data = eval( "(" + result + ")" );
			$("#accountSuffix").text(data.enterpriseSuffix);
		}
	});
	
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
	
	/***************** 绑定司机js begin *********************/
	//司机组织div弹出效果
	$("#driver_biz_btn").bind("click", function() {
		$("#driver_treeDiv").toggle("fast");
	});

	//搜索司机按钮
	$("#driver_search_btn").click(function() {
		$.ajax( {
			"url" : "cardispatchManage_ajax!findDriverInfoList.action",
			"type" : "post",
			"data" : {
				"driver#driverBizId" : $("#driver_bizId").val(),
				"driver#driverLicenseType" : $("#driver_license_type").val() , 
				"driver#driverName" : $("#driver_name").val() , 
				"isFree" : "true" 
			},
			"success" : function(result) {
				result = eval(result);
				driverTablePage.setDataArray(result);
				driverTablePage.refreshTable();
				driverTablePage.checkButton();
			}
		});
	});
	

	/***************** 绑定司机js end *********************/

	
	/***************** 添加司机js begin *******************/
	//选择图片
	$("#driverFile").change(function() {
		$("#driver_info_left_img").attr( {
			"src" : "images/big_loading.gif"
		});
		$("#driver_img_form").ajaxSubmit(function(data) {
			$("#driver_info_left_img").attr( {"src" : data});
			$("#driverPic").val(data);
		});
	});
	/***************** 添加司机js end *******************/

	//上一步按钮
	$(".backStepButton").each( function() {
		$(this).click(function() {
			window.location.href = "carflow_saveCar.jsp?carId=" + carId + "&bizunitInstanceId=" + bizunitInstanceId;
		});
	});

	//table切换
	$(".tab_1 ul li").each(function(index) {
		$(this).click(function() {
			$(".tab_1 ul li").removeClass("ontab");
			$(this).addClass("ontab");
			$(this).find($("input:radio")).attr("checked", "checked");
			$(".tab_content").hide();
			$(".tab_content").eq(index).show();
		})
	});

	/*弹出alert_div*/
	//更换司机
	$("#driver_change_btn").click(function() {
		$("#driver_div_table tbody").empty();
		$("#driver_alert").fadeIn(200);
		$("#driver_name").val("");
	})

	/*关闭alert_div*/
	$(".close_alert_div").click(function() {
		$(this).parents(".alert_div").fadeOut(100);
	});

	/*绑定司机和车辆*/
	$("#bindCDButton").click(function() {
		//验证
		var driverIdText = $("#driverIdText").val();
		var driverIdNull = $("#driverIdNull");
		$(driverIdNull).css("display0", "none");
		if (driverIdText == "") {
			$(driverIdNull).fadeIn(1000);
			return false;
		}
		$("#bindCDForm").ajaxSubmit(function( data ){
			if ( data && data.length > 0 ) {
				data = eval( "(" + data + ")" );
				data.bizId = bizunitInstanceId;
				nextPage(data);
			}
			
		});
	});

	//添加和绑定司机
	formcheck( {
		"form" : $("#saveBindCDForm"),
		"subButton" : $("#addDriverButton"),
		"isAjax" : true , 
		"formSubmiting" : function () {
							var bizunitText = $("#bizunitText").val();
							if ( !bizunitText || bizunitText == "" ) {
								alert("组织为空");
								return false;
							}
		
							var account = $("#ITAccountIdText").val();
							var accountSuffix = $("#accountSuffix").text();
							var accountId = "";
							if ( account != "" && accountSuffix != ""  ) {
								accountId = account + accountSuffix;
							} else {
								alert("填写的账号有误！");
								return false;
							}
							$("#accountId_hidden").val(accountId);
						} , 
		"ajaxSuccess" : function(data) {
			data = data.replace("\"success\"","");
			data = eval("(" + data + ")");
			data.bizId = bizunitInstanceId;
			data.carId = carId;
			nextPage(data);
		}
	});
});

	
function nextPage ( data ) {
	var flag = confirm("司机已经添加成功，是否继续添加终端呢？");
	var url = "";
	var urlOpt = {};
	if (flag) {
		url = "carflow_saveBindTerminal.jsp";
		urlOpt = {
			"bizunitInstanceId" : data.bizId,
			"driverId" : data.driverId,
			"carId" : data.carId
		};
	} else {
		url = "cargeneral_index.jsp";
		urlOpt = {
			"enable" : 1,
			"carId" : data.carId
		};
	}
	url = createUrl(url, urlOpt);
	location.href = url;
	
}


function checkDriverAccount (  ) {
	var ITAccountIdText = $("#ITAccountIdText").val();
	var accountSuffix = $("#accountSuffix").text();
	var flag = false;
	$.ajax({
		"url" : "cardispatchManage!checkAccountIdIsExists.action" , 
		"type" : "post" , 
		"data" : { "driver#accountId" : (ITAccountIdText+accountSuffix) } , 
		"async" : false , 
		"success" : function ( result ) {
			flag = !(result=="true");
		}
	});
	return flag;
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
			$("#driver_bizName").val(data.name);
			$("#driver_bizId").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}  
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"driver_treeDiv","carmanage_org_div","a","orgTreeClick");
			},"json");
		}
	});
	
}
function a(){}

//显示组织信息
function orgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#driver_bizName").val(data.name);
	$("#driver_bizId").val(data.orgId);
	$("#bizunitText").val(data.orgId);
	$("#driver_treeDiv").slideUp("fast");
} 

