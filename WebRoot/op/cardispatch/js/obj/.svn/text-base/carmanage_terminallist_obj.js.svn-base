var terminalPageSize = 10;
var terminalPage = null;

$(document).ready(function(){
	//信息集合分页
	var terminalOpt = {
				"pageSize" : terminalPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#mobileList_table") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["terminalId"] ) {
										return;
									}
									var checkBoxTd = $("<td/>").appendTo($(tr));
									var checkBox = $("<input/>").attr({ "type" : "checkbox" , "class" : "input_checkbox" , "title" : tdData.terminalId }).appendTo($(checkBoxTd));//复选框
									var imeiTd = $("<td/>").appendTo($(tr));//终端号
									var imei = $("<a/>").attr({ "href" : "carmanage_editTerminal.jsp?terminalId="+tdData.terminalId, "target" : "_blank"}).text(tdData.clientimei==null?"":tdData.clientimei).appendTo($(imeiTd));
									var typeTd = $("<td/>").text(tdData.mobileType==null?"":tdData.mobileType).appendTo($(tr));//类型
									var teamTd = $("<td/>").text(tdData.terminalBizName==null?"":tdData.terminalBizName).appendTo($(tr));//所属组织架构
									var launchedTimeTd = $("<td/>").text(tdData.launchedTime==null?"":tdData.launchedTime).appendTo($(tr));//开通时间
								}
			}
	terminalPage = new TablePage(terminalOpt);
	$(".paging_div").prepend($("#terminal_foot_div"));
})