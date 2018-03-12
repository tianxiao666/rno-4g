var driverPageSize = 10;
var driverPage = null;

$(document).ready(function(){
	//信息集合分页
	var driverOpt = {
				"pageSize" : driverPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#resultListTable") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["driverName"] ) {
										return;
									}
									var checkBoxTd = $("<td/>").appendTo($(tr));
									var checkBox = $("<input/>").attr({ "type" : "checkbox" , "class" : "input_checkbox" , "title" : tdData.driverId }).appendTo($(checkBoxTd));//复选框
									//司机姓名 
									var driverNameTd = $("<td/>").appendTo($(tr));
									var driverNameA= $("<a/>").attr({"href" : "carmanage_editDriver.jsp?driverId="+tdData.driverId , "target" : "_blank"} ).text(tdData.driverName==null?"":tdData.driverName).appendTo($(driverNameTd));
									//司机所属区域
									var bizTd = $("<td/>").text(tdData.driverBizName==null?"":tdData.driverBizName).appendTo($(tr));
									//司机电话
									var phoneTd = $("<td/>").text(tdData.driverPhone==null?"":tdData.driverPhone).appendTo($(tr));
									//司机身份证
									var identificationIdTd = $("<td/>").text(tdData.identificationId==null?"":tdData.identificationId).appendTo($(tr));
								}
			}
	driverPage = new TablePage(driverOpt);
	$(".paging_div").prepend($("#driver_foot_div"));
})