

var carTablePage = null;
var mobileTablePage = null;
var driverTablePage = null;
var pageSize = [5,10];
	
var page_url = location.href;
var page_param = null;
var car = null;
var driver = null;
var terminal = null;



$(document).ready(function(){
	
	page_param = getUrlParamStringToObj( subUrlParamString(page_url) );
	var loadCarInfo_url = "cardispatchManage!findCarInfoById.action";
	carId = page_param.carId;
	
	$.ajax({
		"url" : loadCarInfo_url , 
		"data" : page_param , 
		"type" : "post" , 
		"async" : true , 
		"success" : function( result ) {
						result = eval( "(" + result + ")" );
						car = new Car(result);
						car.putInfo("body");
						driver = new Driver(result);
						driver.putInfo("body");
						terminal = new Terminal(result);
						terminal.putInfo("body");
						var carNumber = encodeURI(encodeURI(result["carNumber"]));
						var link = "loadCarStateMonitoringPageAction?carNumber="+carNumber+"&curPosition=true";
						$("#position").text(result["address"]);//yuan.yw
						var carId=result["carId"];
						$("#position").attr({"href":link});
						$("#position_icon").click(function(){		
							//yuan.yw
							var params = {carId:carId};
						    $.post("cardispatchWorkorder_ajax!getAddressByLngLatAction.action",params,function(data){
						    	if(data.address==""){
						    		//$("#position").text(str);
						    	}else{
						    		$("#position").text(data.address);
						    	}
						    	
						    },"json");
							/*$.ajax({
								"url" : "cardispatchManage!getCarTopGps.action" , 
								"data" : {"carNumber" : $("#carNumber_span").text()} ,  
								"type" : "post" , 
								"async" : true , 
								"success" : function( result ) {
												if ( result ) {
													result = eval( "(" + result + ")" );
													var str = "0 , 0";
													if ( result["jingdu"] && result["weidu"] ) {
														str = result["jingdu"] + " , " + result["weidu"];
													}
													$("#position").text(str);
												}
											}
							});*/
						});
						//$("#position_icon").click();
					}
	});
	 
	
	
	//生成组织架构树
	providerOrgTree();
	
   //搜索司机按钮
   $("#driver_search_btn").click(function(){
	   var driverName = $("#driver_name").val();
		$.ajax({
			"url" : "cardispatchManage_ajax!findDriverInfoList.action" , 
			"type" : "post" ,   
			"data" : { "isFree" : "true" , "driver#driverName" : $("#driver_name").val() , "driver#driverBizId" : $("#driver_bizId").val() , "driver#driverLicenseType" : $("#driver_license_type").val() } , 
			"success" : function( result ) {
							result = eval ( result );
							driverTablePage.setDataArray(result);
							driverTablePage.refreshTable();
							driverTablePage.checkButton();
						} 
		});
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
		
		
	//司机分页
	var driver_opt = {
				"pageSize" : pageSize , 
				"dataArray" : new Array() , 
				"table" : $("#driver_div_table") , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["driverName"] ) {
										return;
									}
									var driverNameTd = $("<td/>").text(tdData["driverName"]==undefined?"":tdData["driverName"]);
									var driverBizNameTd = $("<td/>").text(tdData["driverBizName"]==undefined?"":tdData["driverBizName"]);
									var addressTd = $("<td/>").text(tdData["driverAddress"]==undefined?"":tdData["driverAddress"]);
									var driverLicenseTypeTd = $("<td/>").text(tdData["driverLicenseType"]==undefined?"":tdData["driverLicenseType"]);
									var operaTd = $("<td/>");
									var resource = tdData;
									var operaA = $("<a/>").attr({"title":"选择司机","href":"javascript:void(0);"});
									$(operaA).text("选择");
									$(operaTd).append($(operaA));
									$(tr).append($(driverNameTd));
									$(tr).append($(driverBizNameTd));
									$(tr).append($(addressTd));
									$(tr).append($(driverLicenseTypeTd));
									$(tr).append($(operaTd));
									$(operaA).bind( "click" , {"tdData" : tdData} , function(_event){
										var d = _event.data.tdData;
										var flag = confirm("确定更换司机为" + d.driverName );
										if ( flag ) {
											$.ajax({
												"url" : "cardispatchManage!bindCarAndDriverAjax.action" , 
												"type" : "post" , 
												"data" : { "carId" : $("#carId").val() , "driverId" : d.driverId , "isFree" : "true" } , 
												"async" : true , 
												"success" : function ( result ) {
																$(".close_alert_div").click();
																driver = new Driver(d);
																driver.putInfo("body");
															} 
											});
										} else {
											$(".close_alert_div").click();
										}
									});
								 }
			}
	driverTablePage = new TablePage(driver_opt);
	
	
	//终端分页
	var mobile_opt = {
				"pageSize" : pageSize , 
				"dataArray" : new Array() , 
				"table" : $("#mobile_div_table") , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["clientimei"] ) {
										return;
									} 
									var mobileTypeTd = $("<td/>").text(tdData["mobileType"]==undefined?"":tdData["mobileType"]);
									var terminalBizNameTd = $("<td/>").text(tdData["terminalBizName"]==undefined?"":tdData["terminalBizName"]);
									var imeiTd = $("<td/>").text(tdData["clientimei"]==undefined?"":tdData["clientimei"]);
									var telphoneTd = $("<td/>").text(tdData["telphoneNo"]==undefined?"":tdData["telphoneNo"]);
									var operaTd = $("<td/>");
									var resource = tdData;
									var operaA = $("<a/>").attr({"title":"选择终端","href":"javascript:void(0);"});
									$(operaA).text("选择");
									$(operaTd).append($(operaA));
									$(tr).append($(mobileTypeTd));
									$(tr).append($(terminalBizNameTd));
									$(tr).append($(imeiTd));
									$(tr).append($(telphoneTd));
									$(tr).append($(operaTd));
									$(operaA).bind( "click" , {"tdData" : tdData} , function(_event){
										var d = _event.data.tdData;
										var flag = confirm("确定更换终端为" + d.clientimei );
										if ( flag ) {
											$.ajax({
												"url" : "cardispatchManage!bindCarAndTerminalAjax.action" , 
												"type" : "post" , 
												"data" : { "carId" : $("#carId").val() , "terminalId" : d.terminalId , "isFree" : "true" } , 
												"async" : true , 
												"success" : function ( result ) {
																$(".close_alert_div").click();
																terminal = new Terminal(d);
																terminal.putInfo("body");
															} 
											});
										}
									});
								 }
			}
	mobileTablePage = new TablePage(mobile_opt);
	
	
	//选择图片
	$("#carFile").change(function(){
		$("#car_info_left_img").attr({"src":"images/big_loading.gif"});
		$("#car_img_form").ajaxSubmit( function ( data ) {
			$("#car_info_left_img").attr({"src":data});
			$("#carPic").val(data);
		});
	});
})


 function getPath(obj) {
	  if (obj) {
		  if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
		  	obj.select(); return document.selection.createRange().text;
		  }
		  else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
		  	if (obj.files) {
		  		return obj.files.item(0).getAsDataURL();
		  	}
		  	return obj.value;
		  }
		  return obj.value;
	  }
  }  





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
			$("#driver_bizName").val(data.name);
			$("#driver_bizId").val(data.orgId);
			$("#mobile_bizName").val(data.name);
			$("#mobile_bizId").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}  
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"chooseOrgDiv","carmanage_org_div","a","carOrgTreeClick");
				createOrgTreeOpenFirstNode(data,"driver_treeDiv","drivermanage_org_div","a","driverOrgTreeClick");
				createOrgTreeOpenFirstNode(data,"mobile_treeDiv","terminalmanage_org_div","a","terminalOrgTreeClick");
			},"json");
		}
	});
	
}

//显示组织信息
function carOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#carBizName").val(data.name);
	$("#carBizId").val(data.orgId);
	$("#chooseOrgDiv").slideUp("fast");
} 
function driverOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#driver_bizName").val(data.name);
	$("#driver_bizId").val(data.orgId);
	$("#driver_treeDiv").slideUp("fast");
} 
function terminalOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#mobile_bizName").val(data.name);
	$("#mobile_bizId").val(data.orgId);
	$("#mobile_treeDiv").slideUp("fast");
} 


 


/************************* 页面 js *************************/
$(document).ready(function(){
	$("#mobile_biz_btn").bind( "click" , function(){
		$("#mobile_treeDiv").toggle("fast");
   });
   $("#driver_biz_btn").bind( "click" , function(){
		$("#driver_treeDiv").toggle("fast");
   });
   $("#choose_button").click(function() {
		$("#chooseOrgDiv").slideToggle("fast");
	});
})


