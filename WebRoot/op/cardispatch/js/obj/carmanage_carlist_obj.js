var carPageSize = 10;
var carPage = null;

$(document).ready(function(){
	//信息集合分页
	var carOpt = {
				"pageSize" : carPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#carInfoTable") , 
				"effect" : 2 , 
				"columnMethod" : function ( i,key,tdData , tr ){
									if ( tdData == null || !tdData["carNumber"] ) {
										return;
									}
									url = "cargeneral_index.jsp?carId="+tdData.carId
									var checkBoxTd = $("<td/>").css({"cursor":"pointer"}).appendTo($(tr));
									var checkBox = $("<input/>").attr({ "type" : "checkbox" , "class" : "input_checkbox" , "title" : tdData.carId }).appendTo($(checkBoxTd));//复选框
									//车牌号码
									var carNoTd = $("<td/>").appendTo($(tr));
									var carNo = $("<a/>").attr({ "href" : url , "target" :"_blank"}).text(tdData.carNumber==null?"":tdData.carNumber).appendTo($(carNoTd));
									//车辆类型
									var carTypeTd = $("<td/>").text(tdData.carType==null?"":tdData.carType).appendTo($(tr));
									//所属区域
									var bizTd = $("<td/>").text(tdData.carBizName==null?"":tdData.carBizName).appendTo($(tr));
									//司机姓名
									var driverTd = $("<td/>").text(tdData.driverName==null?"":tdData.driverName).appendTo($(tr));
									//司机号码
									var driverPhoneTd = $("<td/>").text(tdData.driverPhone==null?"":tdData.driverPhone).appendTo($(tr));
									//车载终端号
									var clientimeiTd = $("<td/>").text(tdData.clientimei==null?"":tdData.clientimei).appendTo($(tr));
								}
			}
	carPage = new TablePage(carOpt);
	$(".paging_div").prepend($("#car_foot_div"));
})