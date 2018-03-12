
var workorder = null;
var carPageSize = [5];
var carPage = null;

$(document).ready(function(){
	
	searchProviderOrgTree();
    
    
    
	
	//信息集合分页
	var carOpt = {
				"pageSize" : carPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#resultListTable") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["carNumber"] ) {
										return;
									}
									var carId = getObjectData(tdData,["carId"],"");
									var carNumber = getObjectData(tdData,["carNumber"],"");
									var carDriverPairId = getObjectData(tdData,["cardriverpairId"],"");
									var accountId = getObjectData(tdData,["accountId"],"");
									var driverName = getObjectData(tdData,["driverName"],"");
									var telphone = getObjectData(tdData,["driverPhone"],"");
									var carBizName =  getObjectData(tdData,["carBizName"],"");
									var terminalState =  getObjectData(tdData,["terminalState"],"");
									if ( terminalState == "0" ) {
										terminalState = "待初始化";
									} else if ( terminalState == "1" ) {
										terminalState = "行驶中";
									} else if ( terminalState == "2" ) {
										terminalState = "静止";
									} else if ( terminalState == "3" ) {
										terminalState = "离线";
									} else {
										terminalState = "";
									}
									var isArranged =  tdData["isArranged"];
									var carType =  tdData["carType"];
									
									var radioValue = carNumber+","+carDriverPairId+","+accountId+","+driverName+","+telphone;
									var selectionTd = $("<td><input type=\"radio\" name=\"selectedCarNumber\" value=\""+radioValue+"\" /></td>");
									var carNumberTd = $("<td><a href=\"javascript:void(0)\" onclick=\"moreMenu('"+carId+"','"+carNumber+"',this)\" >"+carNumber+"</a></td>");
									var belongToTd = $("<td>"+carBizName+"</td>");
									var typeTd = $("<td>"+carType+"</td>");
									var totalTask = tdData['totalTask'];
						            var driverName = $("<td>"+carBizName+"</td>");
						            var phoneTd = $("<td>"+telphone+"</td>");
						            var carStateNameTd = $("<td>"+terminalState+"</td>");
						            
						            var totalTask = $("<td><a href=\"#\" onclick=\"getCarTaskInfoForGantt('"+carDriverPairId+"',this)\">"+totalTask+"</a></td>");
						            alert(isArranged);
						            //判断是否排班控制样式
						            if(isArranged=='是'){
						            	isArranged = $("<td style='background-color: green;'>"+isArranged+"</td>");
						            }else{
						            	isArranged = $("<td>"+isArranged+"</td>");
						            }
						            $(tr).append($(selectionTd));
						            $(tr).append($(carNumberTd));
						            $(tr).append($(totalTask));
						            $(tr).append($(belongToTd));
						            $(tr).append($(typeTd));
						            $(tr).append($(phoneTd));
						            $(tr).append($(carStateNameTd));
						            $(tr).append($(isArranged));
								 }
			}
	carPage = new TablePage(carOpt);
	
	
})


	function autoCompleteShowInfo ( data ) {
		return data.carNumber;
	}
	
	
	function autoCompleteOnClick ( data ) {
		$("#carNumber").val(data.carNumber);
		$("#carId").val(data.carId);
	}
	
	
	
	