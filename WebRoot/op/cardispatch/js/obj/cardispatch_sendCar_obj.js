
var workorder = null;
var carPageSize = [5];
var carPage = null;

$(document).ready(function(){
	
	
	
	if ( driverCarId ) {
		//查询pairId的车辆牌照
		$.ajax({
			"url" : "cardispatchManage!getCarByCarDriverPairId.action" , 
			"type" : "post" , 
			"data" : {"cardriverpairId" : driverCarId } , 
			"success" : function(data) {
				data = eval("(" + data + ")");
				$("#carNumber").val(data["carNumber"]);
				$("#carId").val(data["carId"]);
			}
		});
	}
	
	
	if ( isView ) {
		$("#carNumber_search_td").empty();
		var span = $("<span/>").attr({"column":"cardispatchworkorder#carNumber"});
		$("#carNumber_search_td").append($(span));
		var isSendCar = "否";
		if( driverCarId ) {
			isSendCar = "是";
		}
		$("#isSendCar").text(isSendCar);
		$("#dispatchDescription").empty();
		$("#dispatchDescription").attr({"column":"cardispatchworkorder#dispatchDescription"});
		$(".container-bottom").hide();
		$("#carGisBtn").hide();
	}
	
	
	
	
	
	
	
	
	
	customAutoComplete({
		divId : "carNumberAutoComplete_div" , 
		inputSelector : "#carNumber" , 
		url : "cardispatchCommon_ajax!carNumberAutoComplete.action" , 
		showMethodName : "autoCompleteShowInfo" , 
		clickMethodName : "autoCompleteOnClick" , 
		eventName : "keyup" , 
		params : { "carNumber" : "#carNumber" } , 
		width : 120 
	});
	
	
})


	function autoCompleteShowInfo ( data ) {
		return data.carNumber;
	}
	
	
	function autoCompleteOnClick ( data ) {
		$("#carNumber").val(data.carNumber);
		$("#cardriverId").val(data.cardriverpairId);
	}
	
	
	
	