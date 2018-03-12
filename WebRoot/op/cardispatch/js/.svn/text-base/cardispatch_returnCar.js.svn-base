

$(document).ready(function(){
	
	findFeerecord();
	
	$.ajax({
		"url" : "cardispatchWorkorder_ajax!findSingleCardispatchWorkorder.action" , 
		"type" : "post" , 
		"data" : {"woId" : woId} , 
		"success" : function(data){			
			data = eval ( "(" + data + ")" ) ;
			workorder = new cardispatchworkorder(data);
			workorder.putInfo($("body"));
			$("#carGisBtn").val("行车轨迹\n("+data.carNumber+")");
			$("#carInfo_a").attr({"target":"_blank","href":"cargeneral_index.jsp?carId="+data.carId});
			$("#carGisBtn").click( { "d" : data } , function( _ev ){
				var d = _ev.data.d;
				var carNumber = encodeURI(encodeURI(d.carNumber));
				var now = new Date().toString("yyyy-MM-dd")+" 23:59";
				var url = "loadCarStateMonitoringPageAction?woId=" + woId + "&carNumber=" + carNumber + "&beginTime=" + d.realUseCarTime + "&endTime=" + now;
				window.open(url);
			});
		}
	});

	workOrderTracingRecordSection(woId,"serverTrackRecord_div");
	
	$("#returnCar_btn").click(function(){
		var returnMileage = $("#realReturnCarMileage").val().replace(" ","");
		if ( returnMileage == "" ) {
			alert("请填写里程数!");
			return ;
		}
		returnMileage = parseInt(returnMileage);
		var useMileage = parseInt($("#realUseCarMileage").val().replace(" ",""));
		if ( useMileage > returnMileage ) {
			alert("还车里程不能比用车里程小!");
			$("#realReturnCarMileage").val(useMileage);
			return;
		}
		$("#returnCar_form").submit();
	});
	
	
	$("#save_traveling_btn").click(function(){
		var feeAmount = $("#feeAmount").val().replace(" ","");
		if ( feeAmount == "" ) {
			alert("请填写金额!");
			$("#feeAmount").focus();
			return;
		}
		else if(isNaN(feeAmount))
		{
			alert("请正确填写金额");
			$("#feeAmount").focus();
			return;
		}
		
		$("#save_traveling_cost_form").ajaxSubmit(function(data){
			//刷新费用记录
			findFeerecord();
			$("#feeAmount").val("");
			$("#description").val("");
			alert("费用添加成功");
		});
	});
})


function findFeerecord () {
	$.ajax({
		"url" : "cardispatchWorkorder_ajax!findFeerecordListByWoId.action" , 
		"type" : "post" , 
		"data" : { "woId" : woId } , 
		"success" : function(data){
			data = eval( "(" + data + ")" );
			feePage.setDataArray(data);
			feePage.refreshTable();
			feePage.checkButton();
			workOrderTracingRecordSection(woId,"serverTrackRecord_div");
		}
	});
}
