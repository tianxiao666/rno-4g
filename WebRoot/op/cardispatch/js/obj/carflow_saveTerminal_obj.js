
var mobileTablePage = null;
var pageSize = [5];	

$(document).ready(function(){
	//终端分页
	var mobile_opt = {
				"pageSize" : pageSize , 
				"dataArray" : new Array() , 
				"table" : $("#mobile_div_table") , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["terminalId"] ) {
										return;
									} 
									var mobileTypeTd = $("<td/>").text(tdData["mobileType"]==undefined?"":tdData["mobileType"]);
									var mobileBizNameTd = $("<td/>").text(tdData["terminalBizName"]==undefined?"":tdData["terminalBizName"]);
									var imeiTd = $("<td/>").text(tdData["clientimei"]==undefined?"":tdData["clientimei"]);
									var telphoneTd = $("<td/>").text(tdData["telphoneNo"]==undefined?"":tdData["telphoneNo"]);
									var operaTd = $("<td/>");
									var operaA = $("<a/>").attr({"title":"选择终端","href":"javascript:void(0);"});
									$(operaA).text("选择");
									$(operaTd).append($(operaA));
									$(tr).append($(mobileTypeTd));
									$(tr).append($(mobileBizNameTd));
									$(tr).append($(imeiTd));
									$(tr).append($(telphoneTd));
									$(tr).append($(operaTd));
									$(operaA).bind( "click" , {"d" : tdData} , function(_event){
										var d = _event.data.d;
										$(".close_alert_div").click();
										terminal = new Terminal(d);
										terminal.putInfo("body");
									});
								 }
			}
	mobileTablePage = new TablePage(mobile_opt);
	
})