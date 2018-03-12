
var payTablePage = null;
var payPageSize = 10;

$(document).ready(function(){
	
	//预填时间
	var now_date = new Date();
	var task_from = now_date.getFirstDate();
	$("#search_pay_fromDate").val(task_from.toString("yyyy-MM-dd"));
	$("#search_pay_endDate").val(now_date.toString("yyyy-MM-dd"));
	
	
	//行车费用分页
	var pay_opt = {
				"pageSize" : payPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#pay_table") , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["woId"] ) {
										return;
									}
									var feeTypeTd = $("<td/>").text(tdData["feeType"]==undefined?"":tdData["feeType"]);
									var feeAmountTd = $("<td/>").text(tdData["feeAmount"]==undefined?"":tdData["feeAmount"]);
									var descriptionTd = $("<td/>").text(tdData["description"]==undefined?"":tdData["description"]);
									var woIdTd = $("<td/>").text(tdData["woId"]==undefined?"":tdData["woId"]);
									var happenTimeTd = $("<td/>").text(tdData["happenTime"]==undefined?"":tdData["happenTime"]);
									var staffNameTd = $("<td/>").text(tdData["staffName"]==undefined?"":tdData["staffName"]);
									$(tr).append($(feeTypeTd));
									$(tr).append($(feeAmountTd));
									$(tr).append($(descriptionTd));
									$(tr).append($(woIdTd));
									$(tr).append($(happenTimeTd));
									$(tr).append($(staffNameTd));
									
								 }
			}
	payTablePage = new TablePage(pay_opt);
	
	//创建底部分页信息
	createBottomPageDiv();
	
	//查询行车费用按钮
	$("#carPay_search_btn").click(function(){
		var btime = $("#search_pay_fromDate").val()+" 00:00:00";
		var etime = $("#search_pay_endDate").val()+" 23:59:59";
		payTablePage.showLoading();
		$.ajax({
			"url" : "cardispatchWorkorder!findFeerecordList.action" , 
			"data" : {	
						"workorder#carId" : $("#carId").val() , 
						"startTime" : btime , 
						"endTime" : etime , 
						"workorder#feeAmount" : $("#feeAmount").val(), 
						"workorder#feeType" : $("#feeType").val()
					} , 
			"type" : "post" , 
			"success" : function( result ){
							result = eval ( result );
							payTablePage.setDataArray(result);
							payTablePage.refreshTable();
							payTablePage.checkButton();
							var task = {};
							var work = {};
							var taskCount = result.length;
							var taskUseTime = 0;			//用时
							var woOverCount = 0;
							$(result).each(function( index ){
								var info = this;
								taskUseTime += parseFloat(info["diffHour"]);
								if ( !!info["isOverTime"] ) {
									woOverCount++;
								}
							});
							$("#payTaskCount").text(taskCount);
							$("#payOverTimeCount").text(woOverCount);
							$("#payUseTime").text(taskUseTime+"小时");
						}
		});
	});
	
	$("#carPay_search_btn").click();
})


//创建底部分页div
function createBottomPageDiv () {
	var div = $("<div/>").css({ "height" : "25px" , "float" : "left" }).attr({"class":"table_div_bottom"});
	$("#pay_info_div .pageDiv").css({"margin-top":"5px"}).prepend($(div));
	var greySpan = $("<span class='grey'/>").css({"height":"25px"}).appendTo($(div));
	$(greySpan).append("&nbsp;&nbsp;任务单：");
	var taskEm = $("<em class='red' id='payTaskCount'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;&nbsp;超时工单：");
	var overTimeEm = $("<em class='red' id='payOverTimeCount'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;&nbsp;平均每张任务单历时：");
	var useTimeEm = $("<em class='red' id='payUseTime'/>").appendTo($(greySpan)).text(0);
}


