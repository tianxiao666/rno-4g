 //yuan.yw  获取个人人员排班
var calendar = null;
var isOpera = false;
var pageSize = [5];
var searchTablePage = null;
var showTablePage = null;
var tt = null;
var freq_info = {};
var enableEdit = "";
var enable = false;

 $(document).ready(function(){
	   /*********************** 对话框 *************************/
	   /* 点击添加人员弹出 */
		$(".add_people_button").live("click",function(){
			$(".add_people_table").slideToggle("fast");
			//绑定资源树
			dialogProviderOrgTree();
		})
		
		
		$(".selectWorkPlaceButton2").live("click",function(){
			$("#selectWorkPlace2").slideToggle("fast");
		})
		$("#carSearchBtn_dialog").live("click",function(){
			var bizId = $("#selectWorkPlaceId2").val();
			var staffName = $("#carNumber_dialog").val();
			//显示车辆数据
			var url = "staff_planduty_ajax!findStaffListByStaffName.action";
			$.ajax({
				"url" : url , 
				"data" : { "bizId" : bizId , "staffName" : staffName } , 
				"type" : "post" , 
				"success" : function( result ){
								result = eval("(" + result + ")");
								searchTablePage.setDataArray(result);
								searchTablePage.refreshTable();
								searchTablePage.checkButton();
								tt.refreshTd();
							}
			});
		});
		
		tt = new Table2Table({
									"addClass" : ".add_people,.err_people" , 
									"delClass" : ".delete_people" , 
									"delMethod" : function( result , widget ) {
													$(widget).parent().parent().remove();
													showTablePage.delData2Array(function( data ){
														return result.staffId == data.staffId ;
													});
													showTablePage.checkButton();
													showTablePage.refreshTable();
													isOpera = true;
													return result.staffId;
										 		  } , 
									"addMethod" : function ( result , widget ) {
										 			showTablePage.addData2Array( result ,function ( data ) {
										 				return result.staffId == data.staffId;
													});
										 			showTablePage.checkButton();
													showTablePage.refreshTable();
													isOpera = true;
													return result.staffId;
										    	  } , 
									"attrName" : "resource" , 
									"checkDataId" : function ( result ) {
														return result.staffId;
													} , 
									"addSame" : function ( widget ) {
													$(widget).attr({"class":"err_people"});
												 } , 
									"different" : function ( widget ) {
														var data = eval( "(" + $(widget).attr("resource") + ")" );
														$(widget).attr({"class":"add_people"});
													}
								});
		/***************** 菜单栏 ********************/
	   //批量添加排班
	   $("#batchAddDutyBtn").click(function(){
	   		//验证
	   		if ( $("[name='freq']").length == 0 ) {
	   			alert("请选择班次");
	   			return;
	   		}
	   		if ( $("#b_time").val().trim() == "" ) {
	   			alert("请填写起始时间");
	   			return;
	   		}
	   		if ( $("#e_time").val().trim() == "" ) {
	   			alert("请填写结时间");
	   			return;
	   		}
	   		//TODO
	   		//验证时间
	   		$("#batchAddDuty_form").ajaxSubmit(function(){
	   			calendar.refreshCalendar();
	   		});
	   });
	   
	   //添加页头组织架构
	   searchProviderOrgTree();
	   
	   //人员自动补全
	   var opt = {
			divId : "staffAutoComplete_div" , 
			inputSelector : "#staffText" , 
			url : "cardispatchCommon_ajax!notDriverStaffAutoComplete.action" , 
			showMethodName : "autoCompleteShowInfo" , 
			clickMethodName : "autoCompleteOnClick" , 
			eventName : "keyup" , 
			params : {'bizId' : '#selectWorkPlaceId' , "name" : "#staffText" } , 
			width : 140 
		};
		customAutoComplete(opt);
		
		$("#queryButton").click(function(){
			calendar.refreshCalendar();
		});
})


	//人员自动补全
	function autoCompleteShowInfo ( data ) {
		return data.name;
	}
	//人员自动补全
	function autoCompleteOnClick ( data ) {
		$("#staffText").val(data.name);
		$("#staffId").val(data.account);
	}



//排班
window.onload = function(){
		var cal = new Calendar({
			"div" : $("#duty_div") , 
			"tableId" : "cal_table" , 
			"dateSpan" : [$("#date_span")] , 
			"url" : "staff_planduty_ajax!staffNewDutyCalendarAction.action" , 
			"urlParam" : function () {
							var param = {
											"bizId" : $("#selectWorkPlaceId").val() , 
											"name" : $("#staffText").val() ,
											"staffId" : staffId
										};
							return param;
						} , 
			"showInfo" : function ( result ) {
							var str = "";
						 	for ( var i = 0 , j = 0 ; i < result.length ; i++ ) {
						 		if (typeof(result[i]["staffName"]) == "undefined") {
						 			continue;
						 		}
								if ( j < 2 ) {
									str += result[i]["staffName"]+"<br/>";
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
									 cal_date_click( data , date , true );
								 } else {
									 cal_date_click( data , date , false );
								 }
						   } , 
			"td_click" : function(  date , widget  ) {
							var len = $(widget).find(".day_and_night_div").length;
							if ( len == 2 ) {
								return;
							}
							var cnow = date.toDate("yyyy-MM-dd");
							var now = new Date().clearTime();
							var i = now.dateCompareTo(cnow);
							 if ( i <= 0 ) {
								if ( enable && enable == "true") {
									cal_td_click( widget , date );
								}
							}
						 } , 
			"loadingData" : function ( data ){
								data["name"] = $("#staffText").val();
								data["bizId"] = $("#selectWorkPlaceId").val();
								data["staffId"]= staffId;
								loadDutyList( data );
							}
		});
		
		//获取班次信息
		$.ajax({
			"url" : "staff_planduty_ajax!loadStaffDutyFreq.action" , 
			"type" : "post" , 
			"success" : function ( data ) {
				data = eval ( "(" + data + ")" ) ;
				for ( var i = 0 ; i < data.length ; i++ ) {
					freq_info[data[i].frequencyName] = data[i];
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
	}
	
	
	//排班列表
	function loadDutyList ( param ) {
		var date = param.date;
		$.ajax({
			"url" : "staff_planduty_ajax!staffNewDutyListAction.action" , 
			"type" : "post" , 
			"data" : param , 
			"success" : function ( data ) {
							data = eval("(" + data + ")");
							var table = $("#month_work_info_table tbody");
							$(table).empty();
							$(".weeked_color").removeClass("weeked_color");
							date = eval( "(" + date + ")" );
							var j = 0 ;
							for ( var name_key in data ) {
								j++;
								if ( name_key == null ) {
									continue;
								}
								var staffName = name_key;
								var date_map = data[name_key];
								var tr = $("<tr/>").appendTo($(table));
								var staffName_td = $("<td/>").css({"width":"75px"}).text(staffName).appendTo($(tr));
								var freq_td = $("<td/>").css({"width":"85px"}).appendTo($(tr));
								var freq_ul = $("<ul/>").appendTo($(freq_td));
								var morning_li = $("<li/>").appendTo($(freq_ul));
								var night_li = $("<li/>").appendTo($(freq_ul));
								var morningCount = 0;
								var nightCount = 0;
								for ( var i = 0 ; i < 31 ; i++ ) {
									if ( date.length <= i ) {
										var date_td = $("<td/>").css({"width":"45px","height" : "20px"}).appendTo($(tr));
										continue;
									}
									var date_info = date_map[date[i]];
									var d_date = date[i].toDate();
									var isWeek = d_date.isWeek();
									
									//isWeekday
									var date_td = $("<td/>").css({"width":"45px","height" : "20px"}).attr({"cal_date":date[i],"w":date[i].toDate().getUTCDay()}).appendTo($(tr));
									
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
	
	
	function cal_date_click ( data , click_date , isEdit ) {
		if ( data == null || data.length <= 0 ) {
			return;
		}
		var isDateClick = data instanceof Array;
		
		var personInfoHtml="";
		var info;
		var dutyId;
		var frequencyName;
		var dutyDate;
		default_edit = isEdit;
		
		info = data[0];
		dutyId = info.dutyId;
		frequencyName = info.frequencyName;
		freId = info.freId;
		dutyDate = info.dutyDate;
		
		var duty_information = $("<div class='duty_information'/>");
		var duty_information_top = $("<div class='duty_information_top'/>").appendTo($(duty_information));
		var banci = $("<span/>").text("班次：" + dutyDate ).appendTo($(duty_information_top));
		var ban = $("<span/>").text(frequencyName + "(" + info.startTime + "-" + info.endTime + ")")
		$(ban).appendTo($(duty_information_top));
		var fr = $("<span class='fr'></span>").appendTo($(duty_information_top));
		var delBtn = $("<input type='button' " + enableEdit + " value='删除排班' class='del_duty_button' />");
		$(delBtn).appendTo($(fr));
		
		var addBtn = $("<input type='button' " + enableEdit + " value='添加人员' class='add_people_button' />");
		$(addBtn).appendTo($(fr));
		$(delBtn).after("&nbsp;");
		
		
		
		var container_main_table1 = $("<div class='container-main-table1'/>").appendTo($(duty_information));
		var main_table = $("<table class='main-table1 tc' id='duty_car_table_dialog' />").appendTo($(container_main_table1));
		var main_table_thead = $("<thead/>").appendTo($(main_table));
		var thTr = $("<tr style='display: table-row;'/>").appendTo($(main_table_thead));
		var teamTh = $("<th style='text-align:center;'/>").text("所属组织").appendTo($(thTr));
		var driverTh = $("<th style='text-align:center;'/>").text("人员姓名").appendTo($(thTr));
		var operaTh = $("<th  style='text-align:center;' " + enableEdit + " />").text("操作").appendTo($(thTr));
		
		if ( !default_edit ) {
			$(fr).css({"display":"none"});
			$(operaTh).css({"display":"none"});
		}
		
		var main_table_tbody = $("<tbody/>").appendTo($(main_table));
		
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
		var td_driver_tc2 = $("<th>人员姓名</th>").appendTo($(tr_main_table1_tc2));
		var td_opera_tc2 = $("<th>操作</th>").appendTo($(tr_main_table1_tc2));
		var tbody_main_table1_tc = $("<tbody/>").appendTo($(main_table1_tc2));
		
		
        var container_bottom = $("<div class='container-bottom' style='position:relative;'/>").appendTo($(duty_information));
        var sureBtn = $("<input " + enableEdit + " class='addDutyBtn' type='button' value='确认'/>").appendTo($(container_bottom));
        var cancelBtn = $("<input " + enableEdit + " type='button' value='取消' onclick='dialogClose();'/>").appendTo($(container_bottom));
		if ( !default_edit ) {
			$(fr).remove();
			$(operaTh).remove();
			$(container_bottom).remove();
			$(td_opera_tc2).remove();
		}
		
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
			if ( d["staffName"] ) {
				darr.push(d);
			}
		}
		//显示人员数据
		var opt = {
						"pageSize" : pageSize , 
						"dataArray" : darr , 
						"table" : $("#duty_car_table_dialog") , 
						"columnMethod" : function ( i,key,tdData , tr ){
											if ( !tdData["staffName"] ) {
												return;	
											}
											var bizNameTd = $("<td/>").text(tdData["bizName"]);
											var staffNameA = $("<a/>").attr({"target":"_blank","href":"showStaffInfo.jsp?staffId=" + tdData["account"]}).text(tdData["staffName"]);
											var staffNameTd = $("<td/>").append($(staffNameA));
											var operaTd = $("<td " + enableEdit + "/>");
											var res = obj2String(tdData);
											var operaA = $("<a/>").attr({"title":"删除人员", "resource" : res ,"class":"delete_people","href":"javascript:void(0);","opera_data":tdData["staffId"]});
											
											$(tr).append($(bizNameTd));
											$(tr).append($(staffNameTd));
											if ( !default_edit ) {
												$(operaTd).remove();
											} else {
												$(operaTd).append($(operaA));
												$(tr).append($(operaTd));
											}
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
										if ( !tdData["staffName"] ) {
											return;
										}
										var bizNameTd = $("<td/>").text(tdData["bizName"]);
										var staffNameTd = $("<td/>").text(tdData["staffName"]);
										var operaTd = $("<td/>");
										var resource = obj2String(tdData);
										//TODO
										var existsData = showTablePage.dataArray;
										var exists = false;
										for ( var i = 0 ; i < existsData.length ; i++ ) {
											var d = existsData[i];
											var staffId = d["staffId"];
											if ( tdData["staffId"] == staffId ) {
												exists = true;
												break;
											}
										}
										if ( exists ) {
											var operaA = $("<a/>").attr({"title":"添加人员" , "resource" : resource ,"class":"err","href":"javascript:void(0);","opera_data":tdData["staffId"]});
										} else {
											var operaA = $("<a/>").attr({"title":"添加人员" , "resource" : resource ,"class":"add_people","href":"javascript:void(0);","opera_data":tdData["staffId"]});
										}
										
										
										$(operaTd).append($(operaA));
										$(tr).append($(bizNameTd));
										$(tr).append($(staffNameTd));
										$(tr).append($(operaTd));
									 }
				}
		searchTablePage = new TablePage(opt);
		
		$("#dutyPersonInfo").find(".del_duty_button").click( { "dutyDate" : info.dutyDate , "freId" : info.freId } , function($event){
			var dutyDate = $event.data.dutyDate;
			var freId = $event.data.freId;
			$.ajax({
				"url" : "staff_planduty_ajax!deleteStaffDutyTemplateByDate.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freId } , 
				"async" : true , 
				"success" : function ( result ) {
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
		});
		
		$(".container-bottom").find(".addDutyBtn").click( { "dutyDate" : info.dutyDate , "freqId" : info.freId } ,function($event){
			var dutyDate = $event.data.dutyDate;
			var freqId = $event.data.freqId;
			var del = tt.getDelArray();
			var add = tt.getAddArray();
			var delArr = new Array();
			var addArr = new Array();
			
			
			for ( var key in del ) {
				var delData = del[key];
				delArr.push({"staffId" : delData.staffId , "staffName" : delData.staffName , "account" : delData.account});
			}
			
			for ( var key in add ) {
				var addData = add[key];
				addArr.push({"staffId" : addData.staffId , "staffName" : addData.staffName , "account" : addData.account});
			}
			
			$.ajax({
				"url" : "staff_planduty_ajax!staffDutyUpdateAction.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freqId , "delIds" : obj2String(delArr) , "addIds" : obj2String(addArr) } , 
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
	
	
	
	
	
	//日历控件td点击事件
	function cal_td_click ( widget , data ) {
		if ( !enable ) {
			return;
		}
		var personInfoHtml="";
		var dutyString = $(widget).attr("cal_date");
		
		var duty_information = $("<div class='duty_information'/>");
		var duty_information_top = $("<div class='duty_information_top'/>").appendTo($(duty_information));
		var banci = $("<span class='banci'/>").text("班次：" + dutyString ).appendTo($(duty_information_top));
		var ban = $("<span class='add_Fre_opt_span' " + enableEdit + " style='float:right;'/>");
		var ii = 0;
		for ( var key in freq_info ) {
			var optFre = $("<input/>").attr({ "id" : "rdo_fre_"+freq_info[key].id , "txt" : freq_info[key].frequencyName , "type" : "radio" , "value" : freq_info[key].id , "name" : "add_fre" });
			var optLabel = $("<label/>").attr({"for":"rdo_fre_"+freq_info[key].id}).text( freq_info[key].frequencyName );
			ii++;
			if ( freq_info[key].frequencyName == "白班") {
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
			} else if ( freq_info[key].frequencyName == "晚班") {
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
			$(ban).append("&nbsp;");
			$(ban).append($(optFre));
			$(ban).append($(optLabel));
		}
		var add_Fre_btn = $("<input type='button' " + enableEdit + " />").attr("id","add_Fre_btn").val("添加班次").appendTo($(ban));				
		$(ban).appendTo($(duty_information_top));
		var fr = $("<span class='fr' style='display:none'>" + 
						"<input type='button' " + enableEdit + " value='删除排班' class='del_duty_button' />" + 
						"<input type='button' " + enableEdit + " value='添加人员' class='add_people_button' />" + 
				   "</span>"
				   ).appendTo($(duty_information_top));
		
		var container_main_table1 = $("<div class='container-main-table1' id='container-main-table1_div' style='display:none;'/>").appendTo($(duty_information));
		var main_table = $("<table class='main-table1 tc' id='duty_car_table_dialog' />").appendTo($(container_main_table1));
		var main_table_thead = $("<thead>").appendTo($(main_table));
		
		var thTr = $("<tr/>").appendTo($(main_table_thead));
		var teamTh = $("<th style='text-align:center;'/>").text("所属组织").appendTo($(thTr));
		var nameTh = $("<th style='text-align:center;'/>").text("人员姓名").appendTo($(thTr));
		var operaTh = $("<th  style='text-align:center;' " + enableEdit + " />").text("操作").appendTo($(thTr));
		
		
		var main_table_tbody = $("<tbody>").appendTo($(main_table));
		
		var add_people_table = $("<div class='container-main-table1 add_people_table' style='display:none;'/>").appendTo($(duty_information));
		var duty_information_tool = $("<div class='duty_information_tool'/>").appendTo($(add_people_table));
		var selectWorkPlaceText2 = $("<input type='text' id='selectWorkPlaceText2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceId2 = $("<input type='hidden' id='selectWorkPlaceId2' />").appendTo($(duty_information_tool));
		var selectWorkPlaceButton2 = $("<a href='javascript:void(0);' class='select_button selectWorkPlaceButton2' title='选择组织' style='margin-left:-25px;'></a>").appendTo($(duty_information_tool));
		var carNumberSearchText_dialog = $("<input id='carNumber_dialog' type='text' value='' />").appendTo($(duty_information_tool));
		var carSearchBtn_dialog = $("<input id='carSearchBtn_dialog' type='button' value='搜索' />").appendTo($(duty_information_tool));
		var carId_dialog = $("<input id='carId_dialog' type='hidden' />").appendTo($(duty_information_tool));
		var selectWorkPlace2 = $("<div id='selectWorkPlace2' style='display:none;'/>").appendTo($(duty_information_tool));
		
		var main_table1_tc2 = $("<table class='main-table1 tc' id='search_car_table_dialog'/>").appendTo($(add_people_table));
		var thead_main_table1_tc = $("<thead/>").appendTo($(main_table1_tc2));
		var tr_main_table1_tc2 = $("<tr/>").appendTo($(thead_main_table1_tc));
		var td_biz_tc2 = $("<th>所属组织</th>").appendTo($(tr_main_table1_tc2));
		var td_carNumber_tc2 = $("<th>人员姓名</th>").appendTo($(tr_main_table1_tc2));
		var td_opera_tc2 = $("<th>操作</th>").appendTo($(tr_main_table1_tc2));
		var tbody_main_table1_tc = $("<tbody/>").appendTo($(main_table1_tc2));
		
        var container_bottom = $("<div class='container-bottom' style='visibility:hidden;'/>").appendTo($(duty_information));
        var sureBtn = $("<input " + enableEdit + " class='addDutyBtn' type='button' value='确认'/>").appendTo($(container_bottom));
        var cancelBtn = $("<input " + enableEdit + " type='button' value='取消'/>").appendTo($(container_bottom));
		
		$("#personInfo").html($(duty_information).html());
		
		$( "#dutyPersonInfo" ).dialog({
			modal: true,
			close : function (){
				tt.clearData();
			}
		});
		
		$("#add_Fre_btn").click(function () {
 			var fre_opt_val = $(":radio[name='add_fre']:checked").val();
 			
			//var txt = $(":radio[name='add_fre']:checked").attr("txt");
			var txt = $(":radio:checked").attr("txt");
			//提交表单
		/*	$.ajax({ 
			       type : "post", 
			       url : "addFre", 
			       async : false,
			       dataType : "json", 
			       data : {"date" : dutyString , "freId" : fre_opt_val } , 
			       success : function($data){ 
			       		$("#dutytemplate_id").val($data);
			       }
			});*/
			//TODO刷新日历控件
			
			//显示
			var bc = $(".banci").text();
			var time = "";
			if ( freq_info[txt] != null && freq_info[txt].beginTime != undefined && freq_info[txt].endTime != undefined ) {
				 time = freq_info[txt].beginTime + " - " + freq_info[txt].endTime;
			}
			time = bc + txt + "(" + time + ")";
			$(".banci").text(time);
			$(".add_Fre_opt_span").fadeOut("fast");
			$("#container-main-table1_div,.fr").fadeIn("fast");
			$(".container-bottom").css("visibility","visible");
			
		});
		
		$(selectWorkPlaceText2).val($("#bizunitNameText").val());
		$(selectWorkPlaceId2).val($("#bizunitIdText").val());
		$(".ui-dialog").css({ position: "absolute", left: "50%", top: "250px",marginLeft: "0px",marginTop: "0px", background:"#eee", border: "1px solid #ccc",width:"0px"})
		$(".ui-dialog").animate({width: "650px","min-height" : "450px", marginLeft: "-300px",marginTop: "-125px"},500)
		//显示人员数据
		var opt = {
						"pageSize" : pageSize , 
						"dataArray" : new Array() , 
						"table" : $("#duty_car_table_dialog") , 
						"columnMethod" : function ( i,key,tdData , tr ){
											if ( !tdData["staffName"] ) {
												return;	
											}
											var bizNameTd = $("<td/>").text(tdData["bizName"]);
											var staffNameTd = $("<td/>").text(tdData["staffName"]);
											var operaTd = $("<td/>");
											var res = obj2String(tdData);
											var operaA = $("<a/>").attr({"title":"删除人员", "resource" : res ,"class":"delete_people","href":"javascript:void(0);","opera_data":tdData["staffId"]});
											$(operaTd).append($(operaA));
											$(tr).append($(bizNameTd));
											$(tr).append($(staffNameTd));
											$(tr).append($(operaTd));
										 }
					}
		showTablePage = new TablePage(opt);
		tt.clearShow();
		tt.addShow( function( index , value ){
						return value.staffId;
					} , new Array() );
		//搜索页面
		var opt = {
					"pageSize" : pageSize , 
					"dataArray" : new Array() , 
					"table" : $("#search_car_table_dialog") , 
					"columnMethod" : function ( i,key,tdData , tr ){
										if ( !tdData["staffName"] ) {
											return;
										}
										var bizNameTd = $("<td/>").text(tdData["bizName"]);
										var staffNameTd = $("<td/>").text(tdData["staffName"]);
										var operaTd = $("<td/>");
										var resource = obj2String(tdData);
										var operaA = $("<a/>").attr({"title":"添加人员" , "resource" : resource ,"class":"add_people","href":"javascript:void(0);","opera_data":tdData["staffId"]});
										$(operaTd).append($(operaA));
										$(tr).append($(bizNameTd));
										$(tr).append($(staffNameTd));
										$(tr).append($(operaTd));
									 }
				}
		searchTablePage = new TablePage(opt);
		
		
		//添加班次按钮
		$("#dutyPersonInfo").find("#add_Fre_btn").click( { "dutyDate" : dutyString } , function( $event ){
			var dutyDate = $event.data.dutyDate;
			var freqId = $(".duty_information_top").find("input:checked").val();
		});
		
		
		$("#dutyPersonInfo").find(".del_duty_button").click( { "dutyDate" : dutyString } , function($event){
			var dutyDate = $event.data.dutyDate;
			var freqId = $(".duty_information_top").find("input:checked").val();
			$.ajax({
				"url" : "staff_planduty_ajax!deleteStaffDutyTemplateByDate.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freqId } , 
				"async" : true , 
				"success" : function ( result ) {
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
		});
		$(".container-bottom").find(".addDutyBtn").click( { "dutyDate" : dutyString } ,function($event){
			var dutyDate = $event.data.dutyDate;
			var freqId = $(".duty_information_top").find("input:checked").val();
			var add = tt.getAddArray();
			var delArr = new Array();
			var addArr = new Array();
			
			for ( var key in add ) {
				var addData = add[key];
				addArr.push({"staffId" : addData.staffId , "staffName" : addData.staffName , "account" : addData.account });
			}
			
			$.ajax({
				"url" : "staff_planduty_ajax!staffDutyUpdateAction.action" , 
				"data" : { "dutyDate" : dutyDate , "freId" : freqId , "delIds" : obj2String(delArr) , "addIds" : obj2String(addArr) } , 
				"async" : true , 
				"success" : function ( result ) {
								//TODO
								calendar.refreshCalendar();
								isOpera = false;
								dialogClose();
							}
			});
		});
	}
	

/************* 搜索条组织架构树 begin ***************/
   
//生成服务商的组织架构树
function searchProviderOrgTree(){
	var orgId = "16";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"async" : true , 
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.orgId;
			
			
			
			$("#selectWorkPlaceText").val(data.name);
			$("#selectWorkPlaceId").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"selectWorkPlace","tree1","a","searchOrgTreeClick");
			},"json");
		}
	});
}

//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#selectWorkPlaceText").val(data.name);
	$("#selectWorkPlaceId").val(data.orgId);
	$("#selectWorkPlace").slideUp("fast");
}


/************** 搜索条组织架构树 end *************/


/************* 对话框组织架构树 begin **************/
	   
//生成服务商的组织架构树
function dialogProviderOrgTree(){
	var orgId = $("#selectWorkPlaceId").val();;
	if(orgId==null||orgId==""){
		orgId="16";
	}
	var values = {"orgId":orgId}
	var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"selectWorkPlace2","tree2","a","dialogOrgTreeClick");
		
		$("#selectWorkPlaceText2").val($("#selectWorkPlaceText").val());
		$("#selectWorkPlaceId2").val($("#selectWorkPlaceId").val());
		$("#carSearchBtn_dialog").click();
	},"json");
}
function a(){}

//显示服务商的组织信息
function dialogOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#selectWorkPlaceText2").val(data.name);
	$("#selectWorkPlaceId2").val(data.orgId);
	$("#selectWorkPlace2").slideUp("fast");
}
	
/************* 对话框组织架构树 end **************/
	
