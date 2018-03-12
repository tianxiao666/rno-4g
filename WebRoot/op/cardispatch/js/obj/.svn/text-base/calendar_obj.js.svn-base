var tt = null;
var calendar = null;
var actionUrlAjax = "cardispatchDuty_ajax!";
var freq_info = {};
var carSearchTablePage = null;
var carShowTablePage = null;
var default_edit = true;

$(document).ready(function(){

		$("#carSearchBtn_dialog").live("click",function(){
			var bizId = $("#selectWorkPlaceId2").val();
			var carNumber = $("#carNumber_dialog").val();
			//显示车辆数据
			var url = "cardispatchManage_ajax!findCarPairInfoList.action";
			$.ajax({
				"url" : url , 
				"data" : { "car#carBizId" : bizId , "car#carNumber" : carNumber } , 
				"type" : "post" , 
				"success" : function( result ){
								result = eval("(" + result + ")");
								carSearchTablePage.setDataArray(result);
								carSearchTablePage.refreshTable();
								carSearchTablePage.checkButton();
								tt.refreshTd();
							}
			});
		});
		
		
		$("#add_Fre_btn").live("click",function () {
			var fre_opt_val = $(":radio:checked").attr("txt");//.attr("txt");
			//TODO刷新日历控件
			//显示
			var bc = $(".banci").text();
			$(".banci").show();
			var time = freq_info[fre_opt_val]["STARTTIME"] + " - " + freq_info[fre_opt_val]["ENDTIME"];
			time = fre_opt_val + "(" + time + ")";
			$(".banci").text(time);
			$("#addBanci").fadeOut();
			$("#showTable_div,.fr").fadeIn("fast");
			$(".container-bottom").css("visibility","visible");
		});
	
	
		//车辆排班table2Table
		tt = new Table2Table({
			"addClass" : ".add_people,.err_people" , 
			"delClass" : ".delete_people" , 
			"delMethod" : function( result , widget ) {
							$(widget).parent().parent().remove();
							carShowTablePage.delData2Array(function( data ){
								return result.carId == data.carId ;
							});
							carShowTablePage.checkButton();
							carShowTablePage.refreshTable();
							isOpera = true;
							return result.carId;
				 		  } , 
			"addMethod" : function ( result , widget ) {
							carShowTablePage.addData2Array( result ,function ( data ) {
								return result.carId == data.carId ;
							});
							carShowTablePage.checkButton();
							carShowTablePage.refreshTable();
							isOpera = true;
							return result.carId;
				    	  } , 
			"attrName" : "resource" , 
			"checkDataId" : function ( result ) {
								return result.carId;
							} , 
			"addSame" : function ( widget ) {
							$(widget).attr({"class":"err_people"});
						 } , 
			"different" : function ( widget ) {
								var data = eval( "(" + $(widget).attr("resource") + ")" );
								$(widget).attr({"class":"add_people"});
							}
		});
		
		//获取当前登录人所在组织
		getLoginUserBiz();
		
		//日期控件
		var cal = new Calendar({
			"div" : $("#duty_div") , 
			"tableId" : "cal_table" , 
			"dateSpan" : [$("#date_span")] , 
			"url" : actionUrlAjax + "carDutyCalendarAction.action" , 
			"urlParam" : function () {
							if ( $("#searchBizunitIdText").val() == "" ) {
								getLoginUserBiz();
							}
							var param = {
											"carBizId" : $("#searchBizunitIdText").val() , 
											"carNumber" : $("#carNumber").val() 
										};
							return param;
						} , 
			"showInfo" : function ( result ) {
							var str = "";
						 	for ( var i = 0 , j = 0 ; i < result.length ; i++ ) {
						 		if (typeof(result[i]["carNumber"]) == "undefined") {
						 			continue;
						 		}
								if ( j < 2 ) {
									str += result[i]["carNumber"]+"<br/>";
									j ++;
								} else {
									str += "....";
									break;
								}
							}
							return str;
						 } , 
			"freq_click" : function ( data , date ) {
							 var cnow = date.toDate("yyyy-MM-dd");
							 var now = new Date().clearTime();
							 var i = now.dateCompareTo(cnow);
							 if ( i <= 0 ) {
								 cal_date_click( data , date , null , false , true );
							 } else {
								 cal_date_click( data , date , null , false , false );
							 }
						   } , 
			"td_click" : function( date , widget ) {
							var len = $(widget).find(".day_and_night_div").length;
							if ( len == 2 ) {
								return;
							}
							var cnow = date.toDate("yyyy-MM-dd");
							var now = new Date().clearTime();
							var i = now.dateCompareTo(cnow);
							
							if ( i <= 0 ) {
								if ( enable && enable == "true") {
									cal_date_click( {} , date , true , widget , true );
								}
							}
						 } ,
			"loadingData" : function ( data ){
								data["carId"] = carId;
								data["carBizId"] = $("#searchBizunitIdText").val();
								data["carNumber"] = $("#carNumber").val();
								loadDutyList( data );
							}
		});
		
		calendar = cal;
		
		//获取班次信息
		$.ajax({
			"url" : actionUrlAjax + "findCarDutyFreqAction.action" , 
			"type" : "post" , 
			"success" : function ( data ) {
				data = eval ( "(" + data + ")" ) ;
				for ( var i = 0 ; i < data.length ; i++ ) {
					freq_info[data[i].FREQUENCYNAME] = data[i];
				}
			}
		});
		
		
		
		
		var opt = {
			divId : "carNumberAutoComplete_div" , 
			inputSelector : "#carNumber" , 
			url : "cardispatchCommon_ajax!carNumberAutoComplete.action" , 
			showMethodName : "autoCompleteShowInfo" , 
			clickMethodName : "autoCompleteOnClick" , 
			eventName : "keyup" , 
			params : {'carBizId' : '#searchBizunitIdText' , "carNumber" : "#carNumber" } , 
			width : 120 
		};
		customAutoComplete(opt);
})
	function autoCompleteShowInfo ( data ) {
		return data.carNumber;
	}
	
	
	function autoCompleteOnClick ( data ) {
		$("#carNumber").val(data.carNumber);
		$("#carHiddenId").val(data.carId);
	}


	//日期控件单击
	function cal_date_click ( data , date , isTd , widget , isEdit ) {
		if ( data == null || data.length <= 0 ) {
			return;
		}
		var isDateClick = data instanceof Array;
		
		var addBanci_style = !isTd?"style='display:none;'":"";
		var addCar_style = !isTd?"":"style='display:none;'";
		
		var personInfoHtml="";
		var info;
		var dutyId;
		var frequencyName;
		var dutyDate;
		default_edit = isEdit;
		info = data[0];
		if ( !data[0] ) {
			info = {
				"dutyId" : "", 
				"FREQUENCYNAME" : "", 
				"dutyDate" : date, 
				"STARTTIME" : "", 
				"ENDTIME" : ""
			};
		}
		
		dutyId = info.dutyId;
		frequencyName = info.FREQUENCYNAME;
		dutyDate = info.dutyDate;
		var duty_information = $("<div class='duty_information'></div>");
		var duty_information_top = $("<div class='duty_information_top clearfix'></div>").appendTo($(duty_information));
		var banci = $("<span/>").text("班次：" + dutyDate ).appendTo($(duty_information_top));
		var ban = $("<span class='banci'/>").text(frequencyName + "(" + info.startTime + "-" + info.endTime + ")")
		$(ban).appendTo($(duty_information_top));
		if ( !data[0] ){
			$(ban).hide();
		}
		var addBanci = $("<span id='addBanci' " + addBanci_style + "/>").css({"position": "absolute","right": "10px"});
		var ii = 0 ;
		for ( var key in freq_info ) {
			var optFre = $("<input type='radio' name='add_fre'/>").attr({ "id" : "rdo_fre_"+freq_info[key].ID , "txt" : freq_info[key].FREQUENCYNAME , "value" : freq_info[key].ID });
			ii++;
			var optLabel = $("<label/>").attr({"for":"rdo_fre_"+freq_info[key].ID}).text( freq_info[key].FREQUENCYNAME );
			if ( freq_info[key].FREQUENCYNAME == "白班") {
				var len = $(widget).find(".day_div").length;
				if ( len == 1 ) {
					$(optLabel).css({
						"display" : "none"
					});
					$(optFre).css({
						"display" : "none"
					});
					ii--;
				} else {
					if ( ii == 1 ) {
						$(optFre).attr({"checked" : "checked"});
					}
				}
			} else if ( freq_info[key].FREQUENCYNAME == "晚班") {
				var len = $(widget).find(".night_div").length;
				if ( len == 1 ) {
					$(optLabel).css({
						"display" : "none"
					});
					$(optFre).css({
						"display" : "none"
					});
					ii--;
				} else {
					if ( ii == 1 ) {
						$(optFre).attr({"checked" : "checked"});
					}
				}
			} 
			$(addBanci).append("&nbsp;");
			$(addBanci).append($(optFre));
			$(addBanci).append($(optLabel));
		}
		var add_Fre_btn = $("<input type='button' " + enableEdit + " />").attr("id","add_Fre_btn").val("确定").appendTo($(addBanci));
		$(duty_information_top).append($(addBanci));
		
		
		var fr = $("<span class='fr' " + addCar_style + "></span>").appendTo($(duty_information_top));
		var delBtn = $("<input type='button' " + enableEdit + " value='删除排班' class='del_duty_button' />"); 
		$(delBtn).appendTo($(fr));
		
		var addBtn = $("<input type='button' " + enableEdit + " value='添加车辆' class='add_people_button' />"); 
		$(addBtn).appendTo($(fr));
		
		
		
		var container_main_table1 = $("<div id='showTable_div' class='container-main-table1' " + addCar_style + "></div>").appendTo($(duty_information));
		var main_table = $("<table class='main-table1 tc' id='duty_car_table_dialog' ></table>").appendTo($(container_main_table1));
		var main_table_thead = $("<thead/>").appendTo($(main_table));
		var thTr = $("<tr/>").appendTo($(main_table_thead));
		var teamTh = $("<th style='text-align:center;'/>").text("所属组织").appendTo($(thTr));
		var carNumberTh = $("<th style='text-align:center;'/>").text("车牌").appendTo($(thTr));
		var driverTh = $("<th style='text-align:center;'/>").text("司机").appendTo($(thTr));
		var operaTh = $("<th  style='text-align:center;' " + enableEdit + " />").text("操作").appendTo($(thTr));
		
		
		var main_table_tbody = $("<tbody/>").appendTo($(main_table));
		
		var add_people_table = $("<div id='container-main-table1_div' class='container-main-table1 add_people_table' style='display:none; position:relative'></div>").appendTo($(duty_information));
		var duty_information_tool = $("<div class='duty_information_tool'></div>").appendTo($(add_people_table));
		var selectWorkPlaceText2 = $("<input type='text' id='selectWorkPlaceText2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceId2 = $("<input type='hidden' id='selectWorkPlaceId2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceButton2 = $("<a href='javascript:void(0);' class='select_button selectWorkPlaceButton2' title='选择组织' style='margin-left:-25px;'></a>").appendTo($(duty_information_tool));
		var carNumberSearchText_dialog = $(" <input id='carNumber_dialog' type='text' />").appendTo($(duty_information_tool));
		var carSearchBtn_dialog = $(" <input id='carSearchBtn_dialog' type='button' value='搜索' />").appendTo($(duty_information_tool));
		var carId_dialog = $("<input id='carId_dialog' type='hidden' />").appendTo($(duty_information_tool));
		var selectWorkPlace2 = $("<div id='selectWorkPlace2' style='display:none;'></div>").appendTo($(duty_information_tool));
		
		var main_table1_tc2 = $("<table class='main-table1 tc' id='search_car_table_dialog'></table>").appendTo($(add_people_table));
		var thead_main_table1_tc = $("<thead/>").appendTo($(main_table1_tc2));
		var tr_main_table1_tc2 = $("<tr/>").appendTo($(thead_main_table1_tc));
		var td_biz_tc2 = $("<th>所属组织</th>").appendTo($(tr_main_table1_tc2));
		var td_carNumber_tc2 = $("<th>车牌号</th>").appendTo($(tr_main_table1_tc2));
		var td_driver_tc2 = $("<th>司机</th>").appendTo($(tr_main_table1_tc2));
		var td_opera_tc2 = $("<th>操作</th>").appendTo($(tr_main_table1_tc2));
		var tbody_main_table1_tc = $("<tbody/>").appendTo($(main_table1_tc2));
		
		
        var container_bottom = $("<div class='container-bottom' id='con_bottom' style='position:relative;'></div>").appendTo($(duty_information));
        var sureBtn = $("<input " + enableEdit + " class='addDutyBtn' type='button' value='确认'/>").appendTo($(container_bottom));
        var cancelBtn = $("<input " + enableEdit + " type='button' value='取消' onclick='dialogClose();'/>").appendTo($(container_bottom));
		
        if ( !isEdit ) {
			$(operaTh).remove();
			$(addBanci).remove();
			$(container_bottom).remove();
			$(fr).remove();
		}
        
        
		
		$("#personInfo").html($(duty_information).html());
		
		$( "#dutyPersonInfo" ).dialog({
			modal: true,
			close : function (){
				tt.clearData();
			}
		});
		var doc_width = $(window).width();
		var doc_height = $(window).height();
		
		var dialog_width = doc_width * 0.4;
		var dialog_height = doc_height * 0.4;
		var dialog_left = ( doc_width * 0.5 ) - ( dialog_width * 0.5 );
		var dialog_top = ( doc_height * 0.5 ) - ( dialog_height * 0.5 );
		
		$(".ui-dialog").css({ position: "absolute", left: "50%", top: "50%", "margin-left" : "0px", "margin-top" : "0px", background:"#eee", border: "1px solid #ccc",width:"0px" , "min-height" : "0px"})
		$(".ui-dialog").animate({width: "500px" , "min-height" : dialog_height+"px" , "top" : dialog_top+"px" , "left" : dialog_left+"px" },600)
		$("#dutyPersonInfo").css({"min-height" : dialog_height+"px" });
		
		var darr = new Array();
		for( var key in data ) {
			var d = data[key];
			if ( d["carNumber"] ) {
				darr.push(d);
			}
		}
		
		
		
		
		
		
		//显示车辆数据
		var opt = {
						"pageSize" : pageSize , 
						"dataArray" : darr , 
						"table" : $("#duty_car_table_dialog") , 
						"columnMethod" : function ( i,key,tdData , tr ){
											if ( !tdData["carNumber"] ) {
												return;
											}
											var carBizNameTd = $("<td/>").text(getObjectData(tdData,"carBizName",""));
											var carNumber = getObjectData(tdData,"carNumber","") ;
											var carId = getObjectData(tdData,"carId","") ;
											var carNumberA = $("<a />").text(carNumber).attr({
												"href" : "cargeneral_index.jsp?carId="+carId , 
												"target" : "_blank"
											});
											var carNumberTd = $("<td/>").append($(carNumberA));
											var driverNameTd = $("<td/>").text(getObjectData(tdData,"driverName",""));
											var operaTd = $("<td " + enableEdit + "/>");
											var res = obj2String(tdData);
											var operaA = $("<a/>").attr({"title":"删除车辆", "resource" : res ,"class":"delete_people","href":"javascript:void(0);","opera_data":tdData["carId"]});
											
											
											$(tr).append($(carBizNameTd));
											$(tr).append($(carNumberTd));
											$(tr).append($(driverNameTd));
											if ( !default_edit ) {
												$(operaTd).remove();
											} else {
												$(operaTd).append($(operaA));
												$(tr).append($(operaTd));
											}
										 }
					}
		carShowTablePage = new TablePage(opt);
		tt.clearShow();
		tt.addShow( function( index , value ){
						return value.staffId;
					} , darr );
		//搜索页面
		var opt = {
					"pageSize" : pageSize , 
					"dataArray" : new Array() , 
					"table" : $("#search_car_table_dialog") , 
					"columnMethod" : function ( i,key,tdData , tr ){
										if ( !tdData["carNumber"] ) {
											return;
										}
										var bizNameTd = $("<td/>").text(getObjectData(tdData,"carBizName",""));
										var carNumberTd = $("<td/>").text(getObjectData(tdData,"carNumber",""));
										var driverNameTd = $("<td/>").text(getObjectData(tdData,"driverName",""));
										var operaTd = $("<td/>");
										var resource = obj2String(tdData);
										var existsData = carShowTablePage.dataArray;
										var exists = false;
										for ( var i = 0 ; i < existsData.length ; i++ ) {
											var d = existsData[i];
											var carId = d["carId"];
											if ( tdData["carId"] == carId ) {
												exists = true;
												break;
											}
										}
										var operaA = null;
										if ( exists ) {
											operaA = $("<a/>").attr({"title":"添加车辆" , "resource" : resource ,"class":"err","href":"javascript:void(0);","opera_data":tdData["carId"]});
										} else {
											operaA = $("<a/>").attr({"title":"添加车辆" , "resource" : resource ,"class":"add_people","href":"javascript:void(0);","opera_data":tdData["carId"]});
										}
										
										$(operaTd).append($(operaA));
										$(tr).append($(bizNameTd));
										$(tr).append($(carNumberTd));
										$(tr).append($(driverNameTd));
										$(tr).append($(operaTd));
									 }
				}
		carSearchTablePage = new TablePage(opt);
		
		//对话框 删除排班按钮 (删除排班信息)
		$("#dutyPersonInfo").find(".del_duty_button").click( { "dutyDate" : info.dutyDate , "freId" : info.freId } , function($event){
			var dutyDate = $event.data.dutyDate;
			var freId = $event.data.freId;
			if ( !freId || freId == "" ) {
				freId = $(":checked[name='add_fre']").val();
			}
			$.ajax({
				"url" : actionUrlAjax + "deleteDuty.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freId } , 
				"async" : true , 
				"type" : "post" , 
				"success" : function ( result ) {
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
		});
		//对话框 确认按钮 (添加排班信息)
		$(".container-bottom").find(".addDutyBtn").click( { "dutyDate" : info.dutyDate , "freId" : info.freId } ,function($event){
			var dutyDate = $event.data.dutyDate;
			var freId = $event.data.freId;
			if ( !freId || freId == "" ) {
				freId = $(":radio:checked").val();
			}
			var del = tt.getDelArray();
			var add = tt.getAddArray();
			var delArr = new Array();
			var addArr = new Array();
			for ( var key in del ) {
				var delData = del[key];
				delArr.push({"carId" : delData.carId });
			}
			
			for ( var key in add ) {
				var addData = add[key];
				addArr.push({"carId" : addData.carId });
			}
			$.ajax({
				"url" : actionUrlAjax + "dutyUpdateAction.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freId , "delIds" : obj2String(delArr) , "addIds" : obj2String(addArr) } ,
				"type" : "post" , 
				"async" : true , 
				"success" : function ( result ) {
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
			
		});
		
		$(".container-main-table1.add_people_table").find(".pageDiv :button").click(function(){
			tt.refreshTd();
		});
	}

	//获取当前登录人所在组织
	function getLoginUserBiz  () {
		$.ajax({
			"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
			"type" : "post" , 
			"async" : true , 
			"dataType" : "json",
			"success" : function ( data ) {
				orgId = data.orgId;//yuan.yw
				$("#searchBizunitNameText").val(data.name);
				$("#searchBizunitIdText").val(data.orgId);
			}
		});
	}



