var feePageSize = [5];
var feePage = null;

$(document).ready(function(){

	//信息集合分页
	var feeOpt = {
				"pageSize" : feePageSize , 
				"dataArray" : new Array() , 
				"table" : $("#traveling_cost_table") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["feeId"] ) {
										return;
									}
									var feeType = tdData["feeType"];
									var feeAmount = tdData["feeAmount"];
									var description = tdData["description"];
									var feeId = tdData["feeId"];
									
									var feeTypeTd = $("<td/>").text(feeType);
									var feeAmountTd = $("<td/>").text(feeAmount);
									var descriptionTd = $("<td/>").text(description);
									var operaTd = $("<td/>");
									var operaA = $("<em class='close_tr' feeId='" + feeId + "' title='删除'></em>");
									$(operaTd).append($(operaA));
						            $(tr).append($(feeTypeTd));
						            $(tr).append($(feeAmountTd));
						            $(tr).append($(descriptionTd));
						            $(tr).append($(operaTd));
						            
						            $(operaTd).click( { "fId" : feeId } , function(_ev){
						            	var fId = _ev.data.fId;
						            	$.ajax({
						            		"url" : "cardispatchWorkorder!deleteFeeAmount.action" , 
						            		"type" : "post" , 
						            		"data" : { "id" : fId } , 
						            		"success" : function( data ){
						            			findFeerecord();
						            		}
						            	});
						            });
								 }
			}
	feePage = new TablePage(feeOpt);
	
	//验证
	formcheck({
		"form" : $("#applyCardispatch_form") , 
		"subButton" : $("#applyCardispatchWorkorderBtn") , 
		"isAjax" : true , 
		"formSubmiting" : function () {
			var pro = $("#pro_select").find(":selected").text();
			var city = $("#city_select").find(":selected").text();
			var area = $("#area_select").find(":selected").text();
			var street = $("#street_select").find(":selected").text();
			var areaDescription = $("#areaDescription").val();
			var address = pro + city + area + street + areaDescription;
			$("#planUseCarAddress").val(address);
		} , 
		"ajaxSuccess" : function( data ){
			if ( data && data != null && data != "" ) {
				alert("申请成功");
				location.href = "cardispatch_lookupworkorder.jsp?WOID="+data;
			} else {
				alert("申请失败");
			}
		}
	});
})
