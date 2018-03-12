var light = [ "#create" , "#send" , "#use" , "#return" ];
var lightSpan = [ "#createSpan" , "#sendSpan" , "#useSpan" , "#returnSpan" ];
   
$(document).ready(function(){
	
	$.ajax({
		"url" : "cardispatchWorkorder_ajax!findSingleCardispatchWorkorder.action" , 
		"type" : "post" , 
		"data" : {"woId" : woId} , 
		"success" : function(data){			
			data = eval ( "(" + data + ")" ) ;
			workorder = new cardispatchworkorder(data);
			workorder.putInfo($("body"));
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
			if ( workorder.workorderStateCName == "已撤销") {
				$("#create").addClass("list_pic_li_on");
			} else if( workorder.workorderStateCName ) {
				for ( var i = 0 ; i < lightSpan.length ; i++ ) {
					var span = $(lightSpan[i]).text().replace(" ","");
					if ( span != "" ) {
						$(light[i]).addClass("list_pic_li_on");
					} else {
						$(light[i]).addClass("list_pic_li_ing");
						break;
					}
				}
				if ( i > 1 ) {
					$(".list_pic_info").show();
				} else {
					$(".list_pic_info").hide();
				}
			}
			if ( isView && data["carNumber"] ) {
				$("#isSendCar").text("是");
				$("#workorderState").text("待用车");
			}
		}
	});
	
	
	
	$("#carsearch_btn").click(function(){
		 var resultStr = window.showModalDialog("queryCarInfoListByConditions.jsp",{'WOID':woId},"dialogWidth=1024px;dialogHeight=768px;");
		 if(resultStr!="" && resultStr!=null){
		 	var values = resultStr.split(",");
		 	var cardriverId = values[1];
		 	var carNumber = values[0];
		 	$("#carNumber").val(carNumber);
		 	$("#cardriverId").val(cardriverId);
		 }
	});
	
	$("#sendCarBtn").click(function(){
		var checkText = $("[name='is_pc']:checked").val();
		if ( checkText == "true" ) {
			if ( $("#cardriverId").val().replace(" ","") == "" ) {
				alert("请选择车辆");
				return;
			}
		}
		$("#sendCar_form").ajaxSubmit(function( data ){
			if ( !!data ) {
				 var isSendCar = $("[name='is_pc']:checked").val();
				if ( isSendCar == "true" ) {
					alert("派车成功");
				} else {
					alert("撤销成功");
				}
				location.href = "cardispatchWorkorder!enterCardispatchWorkorderAction?WOID="+woId+"&type=view";
			}
		});
	});
	
	workOrderTracingRecordSection(woId,"serverTrackRecord_div");
})