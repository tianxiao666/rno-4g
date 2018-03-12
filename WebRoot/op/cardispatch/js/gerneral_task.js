
var taskTablePage = null;
var taskPageSize = 10;
var allTask = {
				"all" : [] , 
				"finish" : [] , 
				"unfinish" : []
			};


$(document).ready(function(){
	promptCueDiv($("#woTitle"),"请输入工单标题");
	//预填时间
	var now_date = new Date();
	var task_from = now_date.getFirstDate();
	$("#task_beginTime").val(task_from.toString("yyyy-MM-dd"));
	$("#task_endTime").val(now_date.toString("yyyy-MM-dd"));
	
	//行车费用分页
	var task_opt = {
				"pageSize" : taskPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#task_table") , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["woId"] ) {
										return;
									}
									var useCarTypeTd = $("<td/>").text(tdData["workorderApplyType"]==undefined?" ":tdData["workorderApplyType"]);
									var toIdUrl = "cardispatch_lookupworkorder.jsp?WOID="+tdData["woId"];
									var toA = $("<a/>").attr({"href":toIdUrl , "target" : "blank"});
									$(toA).text(tdData["woId"]==undefined?"":tdData["woId"]);
									var toIdTd = $("<td/>").html(toA);
									var realTakeCarTimeTd = $("<td/>").text(getObjectData(tdData,"realUseCarTime",""));
									var realReturnCarTimeTd = $("<td/>").text(getObjectData(tdData,"realReturnCarTime",""));
									var useTimeTd = $("<td/>").text(getObjectData(tdData,"diffHour",""));
									var gpsMilageTd = $("<td/>").text(getObjectData(tdData,"totalgpsMileage",""));
									var realMilageTd = $("<td/>").text(getObjectData(tdData,"totalMileage",""));
									var amountTd = $("<td/>").text(getObjectData(tdData,"totalFee",""));
									var looklook = $("<a/>");
									var beginTime = getObjectData(tdData,"realUseCarTime","");
									var endTime = getObjectData(tdData,"realReturnCarTime","");
									var carNumber = encodeURI(encodeURI(tdData["carNumber"]));
									var aurl = "loadCarStateMonitoringPageAction?carNumber=" + carNumber + "&beginTime=" + beginTime + "&endTime=" + endTime ;
									$(looklook).attr({"href":aurl , "target" : "blank" }).text("查看");
									var lookTd = $("<td/>").append($(looklook));
									$(tr).append($(useCarTypeTd));
									$(tr).append($(toIdTd));
									$(tr).append($(realTakeCarTimeTd));
									$(tr).append($(realReturnCarTimeTd));
									$(tr).append($(useTimeTd));
									$(tr).append($(gpsMilageTd));
									$(tr).append($(realMilageTd));
									$(tr).append($(amountTd));
									$(tr).append($(lookTd));
									
								 }
			}
	taskTablePage = new TablePage(task_opt);
	
	//创建底部分页信息
	createBottomTaskPageDiv();
	
	
	//查询用车任务按钮
	$("#carTask_search_btn").click(function(){
		var btime = $("#task_beginTime").val()+" 00:00:00";
		var etime = $("#task_endTime").val()+" 23:59:59";
		taskTablePage.showLoading();
		$.ajax({
			"url" : "cardispatchWorkorder!findCardispatchWordorderByState.action" , 
			"data" : {	
						"workorder#carId" : $("#carId").val() , 
						"workorder#apply.associateWorkType" : $("#associateWorkType").val() , 
						"workorder#createStartTime" : btime , 
						"workorder#createEndTime" : etime , 
						"workorder#woTitle" : $("#woTitle").val()
					} ,  
			"type" : "post" , 
			"success" : function( result ){
							result = eval ( result );
							var data = [];
							for ( var i = 0 ; i < result.length ; i++ ) {
								var r = result[i];
								if ( r["taskId"] ){
									data.push(r);
								} 
							}
							allTask = {
										"all" : [] , 
										"finish" : [] , 
										"unfinish" : []
									};
							
							//计算任务单
							var taskArray = new Object();
							var taskCount = 0;
							var useTimeTotal = 0;
							var gpsTotal = 0;
							var realMilageTotal = 0;
							var amountTotal = 0;
							
							
							for ( var i = 0 ; i < result.length ; i++ ) {
								var info = result[i];
								var woId = info["woId"];
								if ( !taskArray[woId] ) {
									taskCount++;
									useTimeTotal += parseFloat(info["diffHour"]);
									gpsTotal += parseFloat(info["totalgpsMileage"]);
									realMilageTotal += parseFloat(info["totalMileage"]);
									amountTotal += info["totalFee"]== undefined?0:parseFloat(info["totalFee"]);
								}
								if ( info["workorderStatus"] == 7 ) {
									allTask["finish"].push(info);
								} else {
									allTask["unfinish"].push(info);
								}
								allTask["all"].push(info);
							}
							
							
							var tf = $("#task_finish").attr("checked");
							var tu = $("#task_unfinish").attr("checked");
							var showArr = [];
							if ( tu == "checked" && tf == "checked" ) {
								showArr = allTask["all"];
							} else if ( tu == "checked" ) {
								showArr = allTask["unfinish"];
							} else if ( tf == "checked" ) {
								showArr = allTask["finish"];
							}
							refreshTaskTable(showArr);
							
							$("#task_unfinish_em").text(" "+allTask["unfinish"].length+" ");
							$("#task_finish_em").text(" "+allTask["finish"].length+" ");
							$("#taskTaskCount").text(taskCount);
							useTimeTotal = taskCount==undefined || useTimeTotal == undefined?"0":useTimeTotal/taskCount;
							useTimeTotal = cutPoint(useTimeTotal,1);
							$("#taskAvgTime").text( useTimeTotal );
							realMilageTotal =  taskCount==undefined || realMilageTotal == undefined?"0":realMilageTotal/taskCount;
							realMilageTotal = cutPoint(realMilageTotal,1);
							$("#taskRealMilage").text(realMilageTotal);
							gpsTotal = taskCount==undefined || gpsTotal == undefined?"0":gpsTotal/taskCount;
							gpsTotal = cutPoint(gpsTotal,1);
							$("#taskGpsMilage").text(gpsTotal);
							amountTotal = taskCount==undefined || amountTotal == undefined?"0":amountTotal/taskCount;
							amountTotal = cutPoint(amountTotal,1);
							
							$("#taskAmount").text(amountTotal);
						}
		});
	});
	
	$("#carTask_search_btn").click();
	
	$("[name='task_isFinish']").live("click",function( $event ){
		showFinish();
		var checkboxs = $("#task_info_div [name='task_isFinish']:checked");
		var checks = $("#task_info_div [name='task_isFinish']");
		if ( checkboxs.length == 0 ) {
			if ( $(this).attr("id") == $(checks).eq(0).attr("id") ) {
				$(checks).eq(1).attr({"checked":"checked"});
			} else {
				$(checks).eq(0).attr({"checked":"checked"});
			}
		}
		showFinish();
	});
	
	$(".task_isFinish_label").mouseup(function(){
		var checkboxs = $("#task_info_div [name='task_isFinish']:checked");
		console.log(checkboxs.length);
	});
})


//创建底部分页div
function createBottomTaskPageDiv () {
	//$("#task_info_div .tfoot").css({"margin-top":"5px"}).prepend($(div));
	var div = $("#task_table tfoot td").css({"text-align":"left"});
	var greySpan = $("<span class='grey'/>").css({"height":"25px"}).appendTo($(div));
	$(greySpan).append("&nbsp;&nbsp;任务单：");
	var taskTaskCountEm = $("<em class='red' id='taskTaskCount'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;&nbsp;平均历时：");
	var taskGpsMilageEm = $("<em class='red' id='taskAvgTime'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;小时&nbsp;&nbsp;平均GPS里程：");
	var taskUseTimeEm = $("<em class='red' id='taskGpsMilage'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;公里&nbsp;&nbsp;平均仪表里程：");
	var taskGpsMilageEm = $("<em class='red' id='taskRealMilage'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;公里&nbsp;&nbsp;平均行车费用：");
	var useTimeEm = $("<em class='red' id='taskAmount'/>").appendTo($(greySpan)).text(0);
	$(greySpan).append("&nbsp;元");
	//$("#task_info_div .pageDiv").css({"margin-top":"5px"}).append($(div));
}


//显示完成
function showFinish () {
	var checkboxs = $("#task_info_div [name='task_isFinish']:checked");
	var showArr = [];
	if ( checkboxs.length == 2 ) {
		refreshTaskTable(allTask["all"]);
	} else if ( $(checkboxs).eq(0).attr("id") == "task_finish" ) {
		refreshTaskTable(allTask["finish"]);
	} else if ( $(checkboxs).eq(0).attr("id") == "task_unfinish" ) {
		refreshTaskTable(allTask["unfinish"]);
	}
}

//刷新数据
function refreshTaskTable ( result ) {
	taskTablePage.setDataArray(result);
	taskTablePage.refreshTable();
	taskTablePage.checkButton();
}
