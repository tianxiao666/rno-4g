var workorderData = null; 

$(document).ready(function(){

	$.ajax({
		"url" : "cardispatchWorkorder_ajax!findSingleCardispatchWorkorder.action" , 
		"type" : "post" , 
		"data" : {"woId" : woId} , 
		"async" : true , 
		"success" : function(data){	
			data = eval ( "(" + data + ")" ) ;
			workorder = new cardispatchworkorder(data);
			workorder.putInfo($("body"));
			
			$("#carInfo_a").attr({"target":"_blank","href":"cargeneral_index.jsp?carId="+data.carId});
			workorderData = data;
			/*
			$("#carGisBtn").click( { "d" : data } , function( _ev ){
				var d = _ev.data.d;
				var carNumber = encodeURI(encodeURI(d.carNumber));
				var planUseCarTime = d.planUseCarTime;
				var returnCarTime = d.planReturnCarTime;
				var url = "loadCarStateMonitoringPageAction?woId=" + woId + "&carNumber=" + carNumber + "&beginTime=" + planUseCarTime + "&endTime=" + returnCarTime;
				window.open(url);
			});
			$("#carGisBtn").val("行车轨迹\n("+data.carNumber+")");
			*/
			$.ajax({
				"url" : "cardispatchManage_ajax!findCarLastMileage.action" , 
				"type" : "post" , 
				"data" : {"carId" : workorderData.carId} , 
				"success" : function(result){	
					$("#mileage").val(result);
				}
			});
		} , 
		"error" : function( XMLHttpRequest, textStatus, errorThrown ){
			location.href = "cardispatchWorkorder!enterCardispatchWorkorderAction?WOID=" + woId;
		}
	});
	workOrderTracingRecordSection(woId,"serverTrackRecord_div");
	
	
	
	
})
