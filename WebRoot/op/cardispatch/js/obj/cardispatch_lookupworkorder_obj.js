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
						            $(tr).append($(feeTypeTd));
						            $(tr).append($(feeAmountTd));
						            $(tr).append($(descriptionTd));
								 }
			}
	feePage = new TablePage(feeOpt);
	
	
})
