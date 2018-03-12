//var tt = null;	//车辆table2Table

//var calendar = null;	//车辆排班控件

var isOpera = false;
var pageSize = [5];
//var carSearchTablePage = null;		查找车辆表格
//var carShowTablePage = null;			显示车辆表格
var tt = null;

var enable = false;
var carId = "";

//排班
$(document).ready(function(){
		
		$(".selectWorkPlaceButton2").live("click",function(){
			$("#selectWorkPlace2").slideToggle("fast");
		})
		
		
		
		searchProviderOrgTree();
		
		$("#right_btn").click(function(){
			calendar.nextMonth();
		});
		$("#left_btn").click(function(){
			calendar.preMonth();
		});
		$("#selectWorkPlace").find("li").click(function(){
			calendar.refreshCalendar();
			calendar.refreshNumber();
		});
		
		/* 点击添加人员弹出 */
		$(".add_people_button").live("click",function(){
			$("#selectWorkPlaceText2").val($("#searchBizunitNameText").val());
			$("#selectWorkPlaceId2").val($("#searchBizunitIdText").val());
			dialogProviderOrgTree();
			$(".add_people_table").slideToggle("fast");
			$(".add_people_table").find("#carSearchBtn_dialog").click();
		})
		
		/*弹出组织架构树*/
		$("#chooseAreaButton").click(function(){
			$("#treeDiv").toggle("fast");
		});
		
		$("#queryButton").click(function(){
			calendar.refreshCalendar();
		});
		
	})
	
	//排班列表
	function loadDutyList ( param ) {
		var date = param.date;
		$.ajax({
			"url" : actionUrlAjax + "carDudyListAction.action" , 
			"type" : "post" , 
			"data" : param , 
			"success" : function ( data ) {
							data = eval("(" + data + ")");
							var table = $("#month_work_info_table tbody");
							$(table).empty();
							$(".weeked_color").removeClass("weeked_color");
							var j = 0 ;
							date = eval( "(" + date + ")" );
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
	
	
	
	
/******************* 组织架构树 *******************/	

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
				$("#searchBizunitNameText").val(data.name);
				$("#searchBizunitIdText").val(data.orgId);
				orgId = $("#searchBizunitIdText").val();
				if(orgId==null||orgId==""){
					orgId="16";
				}
				var values = {"orgId":orgId}
				var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
				$.post(myUrl,values,function(data){
					createOrgTreeOpenFirstNode(data,"treeDiv","carDutySearch_org_div","a","searchOrgTreeClick");
				},"json");
			}
		});
	
}

//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#searchBizunitNameText").val(data.name);
	$("#searchBizunitIdText").val(data.orgId);
	$("#treeDiv").slideUp("fast");
}


/************** 搜索条组织架构树 end *************/


/************* 对话框组织架构树 begin **************/
	
//生成服务商的组织架构树
function dialogProviderOrgTree(){
	var orgId = $("#searchBizunitIdText").val();;
	if(orgId==null||orgId==""){
		orgId="16";
	}
	var values = {"orgId":orgId}
	var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"selectWorkPlace2","carDutyDialog_org_div","a","dialogOrgTreeClick");
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
	
	
	
