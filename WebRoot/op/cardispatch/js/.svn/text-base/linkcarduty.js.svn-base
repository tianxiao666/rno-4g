 
var calendar = null;
var isOpera = false;
var pageSize = [5];
var searchTablePage = null;
var showTablePage = null;
var tt = null;
var freq_info = {};
var enableEdit = "";
var enable = false;

//排班
$(document).ready(function(){


		if ( !carId ) {
			var obj = getUrlParamStringToObj(subUrlParamString(location.href));
			carId = obj.carId;
		}
		
		
		tt = new Table2Table({
									"addClass" : ".add_people,.err_people" , 
									"delClass" : ".delete_people" , 
									"delMethod" : function( result , widget ) {
													$(widget).parent().parent().remove();
													showTablePage.delData2Array(function( data ){
														return result.carId == data.carId ;
													});
													showTablePage.checkButton();
													showTablePage.refreshTable();
													isOpera = true;
													return result.carId;
										 		  } , 
									"addMethod" : function ( result , widget ) {
													showTablePage.addData2Array(result);
													showTablePage.refreshTable();
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
														var data = eval($(widget).attr("resource"));
														$(widget).attr({"class":"add_people"});
													}
								});
		
		
		
			enableEdit = "enableEdit='true'";
		var cal = new Calendar({
			"div" : $("#duty_div") , 
			"tableId" : "cal_table" , 
			"dateSpan" : [$("#date_span")] , 
			"url" : "cardispatchDuty!carDutyCalendarAction.action" , 
			"urlParam" : function () {
							if ( (carId+"").indexOf("#") != -1 ) {
								carId = carId.replace("#","");
							}
							var param = {
											"carId" : carId , 
											"carNumber" : $("#carNumber_span").text()
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
			"freq_click" : function ( data ) {
								cal_date_click( data );
						   } , 
			"loadingData" : function ( data ){
								data["carId"] = carId;
								data["carNumber"] = $("#carNumber_span").text();
								loadDutyList( data );
							}
		});
		
		//获取班次信息
		$.ajax({
			"url" : "cardispatchDuty!findCarDutyFreqAction.action" , 
			"type" : "post" , 
			"success" : function ( data ) {
				data = eval ( "(" + data + ")" ) ;
				for ( var i = 0 ; i < data.length ; i++ ) {
					freq_info[data[i].FREQUENCYNAME] = data[i];
				}
			}
		})
		
		calendar = cal;
		$("#right_btn").click(function(){
			calendar.nextMonth();
		});
		$("#left_btn").click(function(){
			calendar.preMonth();
		});
		$("#selectWorkPlace").find("li").click(function(){
			calendar.refreshCalendar();
		});
	})
	
	
	//排班列表
	function loadDutyList ( param ) {
		var date = eval(param.date);
		$.ajax({
			"url" : "cardispatchDuty!carDudyListAction.action" , 
			"type" : "post" , 
			"data" : param , 
			"success" : function ( data ) {
							data = eval("(" + data + ")");
							var table = $("#month_work_info_table tbody");
							$(table).empty();
							$(".weeked_color").removeClass("weeked_color");
							var j = 0 ;
							for ( var name_key in data ) {
								j++;
								if ( name_key == null ) {
									continue;
								}
								var carNumber = name_key;
								var date_map = data[name_key];
								var tr = $("<tr/>").appendTo($(table));
								
								var staffName_td = $("<td/>").css({"width":"75px"}).text(carNumber).appendTo($(tr));
								var freq_td = $("<td/>").css({"width":"85px"}).appendTo($(tr));
								var freq_ul = $("<ul/>").appendTo($(freq_td));
								var morning_li = $("<li/>").appendTo($(freq_ul));
								var night_li = $("<li/>").appendTo($(freq_ul));
								var morningCount = 0;
								var nightCount = 0;
								for ( var i = 0 ; i < date.length ; i++ ) {
									var date_info = date_map[date[i]];
									var d_date = date[i].toDate();
									var isWeek = d_date.isWeek();
									
									//isWeekday
									var date_td = $("<td/>").css({"width":"45px","height" : "20px"}).attr({"cal_date":date[i],"w":date[i].toDate().getUTCDay()}).appendTo($(tr));
									
									$(date_td).hover(
										  function () {
										    	var tds = $(this).parent("tr").find("td");
										    	$(tds).css({"border-bottom" : "2px red solid"});
										  },
										  function () {
										    	var tds = $(this).parent("tr").find("td");
										    	$(tds).css({"border-bottom" : "1px #CCCCCC solid"});
										  }
									);
									
									if ( isWeek ) {
										$(date_td).addClass("weeked_color");
										$("th[title='td" + d_date.getDate() + "']").addClass("weeked_color");
									}
									var date_ul = $("<ul/>").appendTo($(date_td));
									var date_morning_li = $("<li/>").height(20).appendTo($(date_ul));
									var date_night_li = $("<li/>").height(20).appendTo($(date_ul));
									if ( !date_info ) {
										continue;
									} 
									if ( date_info["morning"] ) {
										$(date_morning_li).attr({"class":"day_show"});
										morningCount++;
									} 
									if ( date_info["night"] ) {
										$(date_night_li).attr({"class":"night_show"});
										nightCount++;
									}
								}
								$(morning_li).text("白：" + morningCount);
								$(night_li).text("晚：" + nightCount);
							}
						}
		});
	}
	
	
	/**
	 * 关闭对话框操作
	 */
	function dialogClose () {
		$("span[class='ui-icon ui-icon-closethick']").click();
		isOpera = false;
		tt.clearData();
	}
	
	
	function cal_date_click ( data ) {
		if ( data == null || data.length <= 0 ) {
			return;
		}
		var isDateClick = data instanceof Array;
		
		var personInfoHtml="";
		var info;
		var dutyId;
		var frequencyName;
		var dutyDate;
		
		info = data[0];
		dutyId = info.dutyId;
		frequencyName = info.FREQUENCYNAME;
		dutyDate = info.dutyDate;
		
		var duty_information = $("<div class='duty_information'/>");
		var duty_information_top = $("<div class='duty_information_top'/>").appendTo($(duty_information));
		var banci = $("<span/>").text("班次：" + dutyDate ).appendTo($(duty_information_top));
		var ban = $("<span/>").text(frequencyName + "(" + info.STARTTIME + "-" + info.ENDTIME + ")")
		$(ban).appendTo($(duty_information_top));
		var fr = $("<span class='fr'></span>").appendTo($(duty_information_top));
		var delBtn = $("<input type='button' " + enableEdit + " value='删除排班' class='del_duty_button' />"); 
		$(delBtn).appendTo($(fr));
		
		var addBtn = $("<input type='button' " + enableEdit + " value='添加车辆' class='add_people_button' />"); 
		$(addBtn).appendTo($(fr));
		
		var container_main_table1 = $("<div class='container-main-table1'/>").appendTo($(duty_information));
		var main_table = $("<table class='main-table1 tc' id='duty_car_table_dialog' />").appendTo($(container_main_table1));
		var main_table_thead = $("<thead>").appendTo($(main_table));
		var thTr = $("<tr/>").appendTo($(main_table_thead));
		var teamTh = $("<th style='text-align:center;'/>").text("所属组织").appendTo($(thTr));
		var carNumberTh = $("<th style='text-align:center;'/>").text("车牌").appendTo($(thTr));
		var driverTh = $("<th style='text-align:center;'/>").text("司机").appendTo($(thTr));
		var operaTh = $("<th  style='text-align:center;' " + enableEdit + " />").text("操作").appendTo($(thTr));
		
		var main_table_tbody = $("<tbody>").appendTo($(main_table));
		
		var add_people_table = $("<div class='container-main-table1 add_people_table' style='display:none;'/>").appendTo($(duty_information));
		var duty_information_tool = $("<div class='duty_information_tool'/>").appendTo($(add_people_table));
		var selectWorkPlaceText2 = $("<input type='text' id='selectWorkPlaceText2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceId2 = $("<input type='hidden' id='selectWorkPlaceId2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceButton2 = $("<a href='javascript:void(0);' class='select_button selectWorkPlaceButton2' title='选择组织' style='margin-left:-25px;'></a>").appendTo($(duty_information_tool));
		var carNumberSearchText_dialog = $(" <input id='carNumber_dialog' type='text' />").appendTo($(duty_information_tool));
		var carSearchBtn_dialog = $(" <input id='carSearchBtn_dialog' type='button' value='搜索' />").appendTo($(duty_information_tool));
		var carId_dialog = $("<input id='carId_dialog' type='hidden' />").appendTo($(duty_information_tool));
		var selectWorkPlace2 = $("<div id='selectWorkPlace2' style='display:none;'/>").appendTo($(duty_information_tool));
		
		var main_table1_tc2 = $("<table class='main-table1 tc' id='search_car_table_dialog'/>").appendTo($(add_people_table));
		var thead_main_table1_tc = $("<thead/>").appendTo($(main_table1_tc2));
		var tr_main_table1_tc2 = $("<tr/>").appendTo($(thead_main_table1_tc));
		var td_biz_tc2 = $("<th>所属组织</th>").appendTo($(tr_main_table1_tc2));
		var td_carNumber_tc2 = $("<th>车牌号</th>").appendTo($(tr_main_table1_tc2));
		var td_driver_tc2 = $("<th>司机</th>").appendTo($(tr_main_table1_tc2));
		var td_opera_tc2 = $("<th>操作</th>").appendTo($(tr_main_table1_tc2));
		var tbody_main_table1_tc = $("<tbody/>").appendTo($(main_table1_tc2));
		
		
        var container_bottom = $("<div class='container-bottom' style='position:relative;'/>").appendTo($(duty_information));
        var sureBtn = $("<input " + enableEdit + " class='addDutyBtn' type='button' value='确认'/>").appendTo($(container_bottom));
        var cancelBtn = $("<input " + enableEdit + " type='button' value='取消' onclick='dialogClose();'/>").appendTo($(container_bottom));
		
		
		$("#personInfo").html($(duty_information).html());
		
		$( "#dutyPersonInfo" ).dialog({
			modal: true,
			close : function (){
				tt.clearData();
			}
		});
		/*
		showDutyCarList();
		$(selectWorkPlaceText2).val($("#bizunitNameText").val());
		$(selectWorkPlaceId2).val($("#bizunitIdText").val());
		*/
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
		
		//显示人员数据
		var opt = {
						"pageSize" : pageSize , 
						"dataArray" : darr , 
						"table" : $("#duty_car_table_dialog") , 
						"columnMethod" : function ( i,key,tdData , tr ){
											if ( !tdData["carNumber"] ) {
												return;	
											}
											var carBizNameTd = $("<td/>").text(tdData["carBizName"]);
											var carNumberTd = $("<td/>").text(tdData["carNumber"]);
											var driverNameTd = $("<td/>").text(tdData["driverName"]);
											var operaTd = $("<td " + enableEdit + "/>");
											var res = obj2String(tdData);
											var operaA = $("<a/>").attr({"title":"删除车辆", "resource" : res ,"class":"delete_people","href":"javascript:void(0);","opera_data":tdData["carNumber"]});
											$(operaTd).append($(operaA));
											$(tr).append($(carBizNameTd));
											$(tr).append($(carNumberTd));
											$(tr).append($(driverNameTd));
											$(tr).append($(operaTd));
										 }
					}
		showTablePage = new TablePage(opt);
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
										var bizNameTd = $("<td/>").text(tdData["bizName"]);
										var carNumberTd = $("<td/>").text(tdData["carNumber"]);
										var driverNameTd = $("<td/>").text(tdData["driverName"]);
										var operaTd = $("<td/>");
										var resource = tdData.toSource();
										var operaA = $("<a/>").attr({"title":"添加人员" , "resource" : resource ,"class":"add_people","href":"javascript:void(0);","opera_data":tdData["carNumber"]});
										$(operaTd).append($(operaA));
										$(tr).append($(bizNameTd));
										$(tr).append($(carNumberTd));
										$(tr).append($(driverNameTd));
										$(tr).append($(operaTd));
									 }
				}
		searchTablePage = new TablePage(opt);
		
		//无改
		$("#dutyPersonInfo").find(".del_duty_button").click( { "dutyId" : data[0].dutyId } , function($event){
			var dutyId = $event.data.dutyId;
			$.ajax({
				"url" : "delDutyTemp.action" , 
				"data" : { "dutyTempId" : dutyId } , 
				"async" : true , 
				"success" : function ( result ) {
								//TODO
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
		});
		//无改
		$(".container-bottom").find(".addDutyBtn").click( { "dutyDate" : data[0].DUTYDATE , "freqId" : data[0].freqId } ,function($event){
			var dutyDate = $event.data.dutyDate;
			var freqId = $event.data.freqId;
			var del = tt.getDelArray();
			var add = tt.getAddArray();
			var delArr = new Array();
			var addArr = new Array();
			
			
			for ( var key in del ) {
				var delData = del[key];
				delArr.push({"staffId" : delData.staffId , "staffName" : delData.staffName });
			}
			
			for ( var key in add ) {
				var addData = add[key];
				addArr.push({"staffId" : addData.staffId , "staffName" : addData.staffName });
			}
			
			$.ajax({
				"url" : "delAndAddDuty.action" , 
				"data" : { "dutyDate" : dutyDate , "freqId" : freqId , "delIds" : delArr.toSource() , "addIds" : addArr.toSource() } , 
				"async" : true , 
				"success" : function ( result ) {
								//TODO
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
	
	
	
	
	
	
