$(function(){
	var taskCount = $(".troudleshootingTime").size();
	var timeCount = 0;
	var timeAverage = 0;
	var timeAverageString = "";
	$(".troudleshootingTime").each(function(){
		if($(this).text()){
			timeCount = timeCount + parseFloat($(this).text());
		}
	});
	timeAverage = timeCount / taskCount;
	if(taskCount == 0){
		timeAverage = 0;
	}
	timeAverageString = timeAverage + "";
	if(timeAverageString.indexOf('.') > 0){
		timeAverageString = timeAverageString.substring(0,timeAverageString.indexOf('.')+3);
	}
	var timeCountString = timeCount + "";
	if(timeCountString.indexOf('.') > 0){
		timeCountString = timeCountString.substring(0,timeCountString.indexOf('.')+3);
	}
	$("#taskCount").text(taskCount);
	$("#timeCount").text(timeCountString);
	$("#timeAverage").text(timeAverageString);
	pagingColumnByForeground("table-gaging",$("#main_table_task .main_table_task_tr"),10);
});

	function clickWOID(woid){
		var varhost = window.location.host;
		window.open("http://"+ varhost + "/ops/op/urgentrepair/loadUrgentRepairWorkOrderPageAction?WOID="+woid);
	}