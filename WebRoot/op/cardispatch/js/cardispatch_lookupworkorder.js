var workorder = null;
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
			if ( !data.carNumber || data.carNumber == null || data.carNumber == "null" ) {
				$("#carGisBtn").hide();
			} else {
				$("#carGisBtn").val("行车轨迹\n("+data.carNumber+")");
			}
			
			$("#carInfo_a").attr({"target":"_blank","href":"cargeneral_index.jsp?carId="+data.carId});
			if ( workorder.workorderStateCName == "已撤销") {
				$("#create").addClass("list_pic_li_on");
			} else if( workorder.workorderStateCName ) {
				var i = 0 ;
				for (i = 0 ; i < lightSpan.length ; i++ ) {
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
			$("#carGisBtn").click( { "d" : data } , function( _ev ){
				var d = _ev.data.d;
				var carNumber = encodeURI(encodeURI(d.carNumber));
				var url = "loadCarStateMonitoringPageAction?woId=" + woId + "&carNumber=" + carNumber + "&beginTime=" + d.realUseCarTime + "&endTime=" + d.realReturnCarTime;
				window.open(url);
			});
		}
	});
	
	workOrderTracingRecordSection(woId,"serverTrackRecord_div");
	
	findFeerecord();
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
		}
	});
}