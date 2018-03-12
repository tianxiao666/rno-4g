/************************* 变量 begin ************************/

var isMapClick = false;
var mapEntityDivID = "";
var mapEntityTypes = "";

var map = null;
var center = null;
var allMarkerArray = new Array();
var stationArray = new Array();//存放所有站址
var baseStationArray = new Array();	//存放所有基站
var manWellArray = new Array();//存放所有人井
var poleArray = new Array();//存放所有电杆
var markPostArray = new Array();//存放所有标桩
var hangWallArray = new Array();//存放所有挂墙点
var odfArray = new Array();//存放所有ODF
var fiberCrossCabinetArray = new Array();//存放所有光交接箱
var fiberDistributionCabinetArray = new Array();//存放所有光分纤箱
var fiberTerminalCaseArray = new Array();//存放所有终端盒
var jointArray = new Array();//存放所有接头

var marker_list = {};

var pipeLineArray = new Array();//存放所有管道段
var buriedLineArray = new Array();//存放所有直埋段
var poleLineArray = new Array();//存放所有吊线段

var fiberSectionArray = new Array();//存放所有光缆段
var fiberCoreArray = new Array();//存放所有纤芯
var opticalRouteSP2SPArray = new Array();//存放所有局向纤芯
var opticalRouteP2PArray = new Array();//存放所有光路纤芯
var transmissionSectionArray = new Array();//存放所有传输段

var curElement = null;//当前展示的点元素
var curLineArray = new Array();//当前展示的线性元素
var curLine = null;//当前选择的线
var curLineType = null;//当前选择的线的类型
var lineIndex = 0;
var infowindow = null;//当前所显示的信息框

var curView = "PipeView";//当前所处逻辑网
var transmissionView = false;//是否进入过传输拓扑逻辑网
var opticalRouteView = false;//是否进入过光路逻辑网
var loadResourceKey = "";

var isMiddle_show = true;
var all_repair_info_page = null;
var repairPageSize = [5];
var dialog_showImg_count = 4;

var please_wait_second = {
	"res" : 1 , 
	"line" : 1
}
var pleaseWaitTimer = {
	"res" : null , 
	"line" : null
};

var marker_load_second = 3;					
var line_load_second = 3;
var cache_map_data = {};
var isAround = true;

var loadingMarker_map = {};


var merkerName = {
	"Station" : "站址" , 
	"BaseStation" : "基站" , 
	"ManWell" : "人井" , 
	"Pole" : "电杆" , 
	"HangWall" : "挂墙点" , 
	"MarkPost" : "标桩" ,      
	"FiberCrossCabinet" : "光交接箱" , 
	"FiberDistributionCabinet" : "光分纤箱" , 
	"FiberTerminalCase" : "终端盒" , 
	"BaseStation_GSM" : "GSM基站" , 
	"BaseStation_repeater" : "直放站" , 
	"BaseStation_TD" : "TD基站" , 
	"BaseStation_WLAN" : "WLAN基站" , 
	"Joint" : "接头"  
}
   
   
/************************* 变量 end ************************/   
   
   
   
   
   
//数据初始化       
$(function() {
	loadingMarker_map = {
		"Station" : "#cb_station" , 
		"Pole" : "#cb_pole" , 
		"BaseStation" : "#cb_generalBaseStation" , 
		"ManWell" : "#cb_manWell" , 
		"MarkPost" : "#cb_markPost" , 
		"HangWall" : "#cb_hangWall" , 
		"FiberCrossCabinet" : "#cb_fiberCrossCabinet" , 
		"FiberDistributionCabinet" : "#cb_fiberDistributionCabinet" , 
		"FiberTerminalCase" : "#cb_fiberTerminalCase" 
	};
	for ( var key in loadingMarker_map ) {
		$(loadingMarker_map[key]).attr({"loading":"false"});
	}
	marker_list = {
		"All" : allMarkerArray , 
		"Station" : stationArray , 
		"BaseStation" : baseStationArray , 
		"ManWell" : manWellArray , 
		"Pole" : poleArray , 
		"MarkPost" : markPostArray , 
		"HangWall" : hangWallArray , 
		"Odf" : odfArray , 
		"FiberCrossCabinet" : fiberCrossCabinetArray , 
		"FiberDistributionCabinet" : fiberDistributionCabinetArray , 
		"FiberTerminalCase" : fiberTerminalCaseArray , 
		"Joint" : jointArray 
	};

	$("#Pipe").click(function(){
		if($(this).attr("class")=="not_selected2 onslected"){
			clearPipeLine(); clearPoleLine();  clearBuriedLine();
			$(this).removeClass("onslected");
		}else{
			$(":button.not_selected2").removeClass("onslected");
			$(this).addClass("onslected");
			showWaitCue( "line" , line_load_second );
			getPipeRouteInfo();
		}
		
	});
	
	$("#Fiber").click(function(){
		if($(this).attr("class")=="not_selected2 onslected"){
		    clearFiberSection();
		    $(this).removeClass("onslected");
		}else{
			$(":button.not_selected2").removeClass("onslected");
			$(this).addClass("onslected");
			showWaitCue( "line" , line_load_second );
			getFiberInfo();
		}
		
	
	});
	$("#FiberCore").click(function(){
		if($(this).attr("class")=="not_selected2 onslected"){
			clearFiberCore(); 
			$(this).removeClass("onslected");
		}else{
			$(":button.not_selected2").removeClass("onslected");
			$(this).addClass("onslected");
			showWaitCue( "line" , line_load_second );
			getFiberCoreInfo();
		}
		
	});
	$("#OpticalRoute").click(function(){
		if($(this).attr("class")=="not_selected2 onslected"){
			 clearOpticalRouteSP2SP();
			 $(this).removeClass("onslected");
		}else{
			$(":button.not_selected2").removeClass("onslected");
			$(this).addClass("onslected");
			showWaitCue( "line" , line_load_second );
			getOpticalRouteInfo();
		}
		
	});
	$("#Transmission").click(function(){
		if($(this).attr("class")=="not_selected2 onslected"){
			clearTransmissionSection();
			$(this).removeClass("onslected");
		}else{
			$(":button.not_selected2").removeClass("onslected");
			$(this).addClass("onslected");
			showWaitCue( "line" , line_load_second );
			getTransmissionInfo();
		}
		
	});

	//资源设施菜单
	$("#facility").click(function() {
		clearCurElements();
		$("#show").slideDown("fast");
		$("#show2").slideUp("fast");
	});
	$(".resources").mouseover(function() {
		if ( $("#show").is(":hidden") ) {
			$("#show").slideDown("fast");
		}
	});
	$(".resources").mouseleave(function(){
		$("#show2").slideUp("fast");
		$("#show").slideUp("fast");
		$("#resources_title").slideUp(200);
	});
	$("#facility").mouseover(function() {
		var html = "在地图上 <span style='font-weight:bold;'>显示</span> 或 <span style='font-weight:bold;'>隐藏</span> 资源";
		$("#resources_title").html(html);
		$("#resources_title").css({"left":"-35px","display":"none"});
		$("#resources_title").slideDown(200);
	});
	$(".refresh_icon").mouseover(function() {
		var html = "<span style='font-weight:bold;'>重新加载</span>地图上的资源";
		$("#resources_title").html(html);
		$("#resources_title").css({"left":"-12px","display":"none"});
		$("#resources_title").slideDown(200);
	});
	$("#show").mouseleave(function() {
		$("#show").slideUp("fast");
	});
	$("#show2").bind("mouseleave",function() {
		$("#show2").slideUp("fast");
	});


	$("#is_show_resourceName_chk").change(function(){
		if ( $(this).is(":checked") ) {
			addResourceText();
		} else {
			clearResourceText();
		}
	});
	
	
	
	var repair_opt = {
				"pageSize" : repairPageSize , 
				"dataArray" : new Array() , 
				"table" : $("#repair_all_info_table") , 
				"columnMethod" : function ( i , key , tdData , tr ){
									if ( tdData == null || !tdData["id"] ) {
										return;
									}
									//唯一标识
									var did = tdData["id"] == undefined || tdData["id"] == "null" ? "&nbsp;" : tdData["id"] ;
									var tdDid = $("<td/>").html(did);
									$(tr).append($(tdDid));
									//业务模块
									var biz_module = tdData["biz_module"] == undefined || tdData["biz_module"] == "null" ? "&nbsp;" : tdData["biz_module"] ;
									var tdBiz_module = $("<td/>").html(biz_module);
									$(tr).append($(tdBiz_module));
									//操作场景
									var op_scene = tdData["op_scene"] == undefined || tdData["op_scene"] == "null" ? "&nbsp;" : tdData["op_scene"] ;
									var tdOp_scene = $("<td/>").html(op_scene);
									$(tr).append($(tdOp_scene));
									//操作原因
									var op_cause = tdData["op_cause"] == undefined || tdData["op_cause"] == "null" ? "&nbsp;" : tdData["op_cause"] ;
									var tdOp_cause = $("<td/>").html(op_cause);
									$(tr).append($(tdOp_cause));
									//操作分类
									var op_category = tdData["op_category"] == undefined || tdData["op_category"] == "null" ? "&nbsp;" : tdData["op_category"] ;
									var tdOp_category = $("<td/>").html(op_category);
									$(tr).append($(tdOp_category));
									//链接
									var linkurl = tdData["linkurl"] == undefined || tdData["linkurl"] == "null" ? "&nbsp;" : tdData["linkurl"] ;
									var tdLinkurl = $("<td/>").html(linkurl);
									$(tr).append($(tdLinkurl));
									//详细内容
									var content = tdData["content"] == undefined || tdData["content"] == "null" ? [] : tdData["content"].split("$_$");
									var c = "&nbsp;";
									for ( var i = 0 ; i < content.length ; i++ ) {
										c += content[i] + "<br/>";
									}
									var tdContent = $("<td/>").html(c);
									$(tr).append($(tdContent));
									//操作人
									var user_name = tdData["user_name"] == undefined || tdData["user_name"] == "null" ? "&nbsp;" : tdData["user_name"] ;
									var tdUser_name = $("<td/>").html(user_name);
									$(tr).append($(tdUser_name));
									//终端设备
									var src_teminal = tdData["src_teminal"] == undefined || tdData["src_teminal"] == "null" ? "&nbsp;" : tdData["src_teminal"] ;
									var tdSrc_teminal = $("<td/>").html(src_teminal);
									$(tr).append($(tdSrc_teminal));
									//操作时间
									var op_time = tdData["op_time"] == undefined || tdData["op_time"] == "null" ? "&nbsp;" : tdData["op_time"] ;
									var tdOp_time = $("<td/>").html(op_time);
									$(tr).append($(tdOp_time));
									
									
									//记录类型
									var record_type = "";
									if ( tdData.record_type ) {
										if(tdData.record_type == 0){
											record_type = "业务调用";
										}else if(tdData.record_type == 1){
											record_type = "系统强制";
										}else if(tdData.record_type == 2){
											record_type = "其他";
										}
										var tdRecord_type = $("<td/>").html(record_type	);
										$(tr).append($(tdRecord_type));
									}
								 }
			}
	all_repair_info_page = new TablePage(repair_opt);
	$("#repair_all_info_dialog").edrag();
	$("#repair_single_info_dialog").edrag();
	$(".info_dialog_close").click(function( $e ){
		$(".info_dialog_black").css({"visibility":"hidden"});
		$("#repair_all_info_dialog").css({"visibility":"hidden"});
		$("#repair_single_info_dialog").css({"visibility":"hidden"});
		$e.stopPropagation();
	});
	
	//esc关闭infoWindow
	$(document).keyup(function( $e ){
		var keyCode = $e.keyCode;
		//判断是否esc
		if ( keyCode == 27 ) {
			var flag = true;			
			//关闭弹出框
			if( $("#repair_all_info_dialog").is(":visible") && $("#repair_all_info_dialog").css("visibility") == "visible" ) {
				$("#repair_all_info_dialog").css({"visibility":"hidden"});
				flag = false;
			} 
			if( $("#repair_single_info_dialog").is(":visible") && $("#repair_single_info_dialog").css("visibility") == "visible" ) {
				$("#repair_single_info_dialog").css({"visibility":"hidden"});
				flag = false;
			}
			if( $(".info_dialog_black").is(":visible") && $(".info_dialog_black").css("visibility") == "visible" ) {
				$(".info_dialog_black").css({"visibility":"hidden"});
				flag = false;
			}
			if ( infowindow != null && flag ) {
				infowindow.close();
				infowindow = null;
			}
			
		}
	});
});



/************************** 资源loading begin *****************************/

//显示稍等提示
function showWaitCue ( waitType , max) {
	var wait_div = null;
	if ( waitType == "res" ) {
		wait_div = $("#please_res_wait_cue_div");
	} else if ( waitType == "line" ) {
		wait_div = $("#please_line_wait_cue_div");
	}
	if ( $(wait_div).is(":visible") ) {
		return ;
	}
	if ( pleaseWaitTimer[waitType] != null ) {
		clearInterval(pleaseWaitTimer[waitType]);
		pleaseWaitTimer[waitType] = null;
	}
	please_wait_second[waitType] = 1;
	//info_dialog_black
	
	$(wait_div).find(".please_wait_second").text(please_wait_second[waitType]);
	
	var left = ($(window).width()*0.5) - ($(wait_div).width()*0.5);
	var top = ($(window).height()*0.5) - ($(wait_div).height()*0.5);
	$(wait_div).css( {"background-color":"#666"});
	$(wait_div).css({"top":top,"left":left , "min-width" : "400px" , "min-height" : "80px" , "text-align" : "center" , "font-size" : "20px" , "line-height" : "80px" });
	
	$(".wait_white").fadeIn(1000);
	$(wait_div).fadeIn(1000);
	pleaseWaitTimer[waitType] = setInterval( "pleaseWaitCount('" + waitType + "'," + max + ")" , 1000 ); 
	//clearInterval
}

//开始计时刷新地图时间
function pleaseWaitCount ( waitType , max ) {
	var wait_div = null;
	if ( waitType == "res" ) {
		wait_div = $("#please_res_wait_cue_div");
	} else if ( waitType == "line" ) {
		wait_div = $("#please_line_wait_cue_div");
	}
	if ( max == please_wait_second[waitType] ) {
		$(".wait_white").fadeOut(1000);
		var wait_show_div_count = $(".please_wait_cue_div:visible").length;
		var top = 23;
		if ( wait_show_div_count > 1 ) {
			top += 32 * (wait_show_div_count - 1) ;
		}
		$(wait_div).animate({"top": top+"px","left":"50%" , "min-width" : "250px" , "border-radius" : "15px" , "font-size" : "12px" , "line-height" : "20px" , "min-height" : "20px" },1200, function(){
			$(wait_div).css( {"background-color":"#777"});
		});
	}
	$(wait_div).find(".please_wait_second").text(please_wait_second[waitType]);
	please_wait_second[waitType]++;
}


//停止计时刷新地图时间
function closePleaseWaitTimer ( waitType ) {
	var closing_wait_div = null;
	var opening_wait_div = null;
	if ( waitType == "res" ) {
		wait_div = $("#please_res_wait_cue_div");
		opening_wait_div = $("#please_line_wait_cue_div");
	} else if ( waitType == "line" ) {
		wait_div = $("#please_line_wait_cue_div");
		opening_wait_div = $("#please_res_wait_cue_div");
	}
	if ( pleaseWaitTimer[waitType] != null ) {
		clearInterval(pleaseWaitTimer[waitType]);
		pleaseWaitTimer[waitType] = null;
	}
	$(wait_div).fadeOut(1000);
	$(".wait_white").fadeOut(1000);
	please_wait_second[waitType] = 1;
	
	//
	$(opening_wait_div).animate({"top": 23+"px"},1000);
}

var changeColorMaxTimes = 4;
var changeColorTimer = null;
var changeColorCount = 0;

//改变资源颜色
function resourceCueDivChangeColor () {
	var wait_div = $("#please_res_wait_cue_div");
	if ( changeColorTimer && changeColorTimer != null ) {
		$(wait_div).css({"border" : "1px white solid"});
		clearInterval(changeColorTimer);
	}
	if ( !$(wait_div).is(":visible") ) {
		return;
	}
	changeColorCount = 0;
	changeColorTimer = setInterval("loadingDivChangeColor('please_res_wait_cue_div' , 'white' , 'red' )" , 300);
}

function loadingDivChangeColor ( div , oldColor , newColor ) {
	var div = $("#"+div);
	if ( changeColorCount >= changeColorMaxTimes ) {
		clearInterval(changeColorTimer);
		return;
	}
	if ( changeColorCount % 2 == 0 ) {
		$(div).css({"border" : "1px " + newColor + " solid"});
	} else {
		$(div).css({"border" : "1px " + oldColor + " solid"});
	}
	changeColorCount++;
}

/************************** 资源loading end *****************************/


/***************************** 显示资源名 begin **********************************/

//清除资源名
function clearResourceText() {
	var ok = $("#show em");
	$(ok).each(function(index){
		var arrKey = $(this).attr("name"); 
		var arr = marker_list[arrKey];
		try{
			var a = arr.length;
		} catch(e){
			console.log( arrKey );
		}
		for ( var i = 0 ; i < arr.length ; i++ ) {
			var marker = arr[i];
			delMarkerTitle(marker);
		}
	});
}

//添加资源名
function addResourceText() {
	var ok = $(".ok-y");
	$(ok).each(function(index){
		var arrKey = $(this).attr("name");
		var arr = marker_list[arrKey];
		for ( var i = 0 ; i < arr.length ; i++ ) {
			var marker = arr[i];
			createMarkerTitle(marker);
		}
	});
}

/***************************** 显示资源名 end **********************************/

window.onload = function() {
	//initMap();
	//loadMarker("Station,Pole,ManWell,GeneralBaseStation");
	
	//IMapEvent.addListener(map, "mousemove", function ($event) {
	//	mousePosition = $event.latLng;
	//});
}

var mapZoom = 12;

//初始化地图
function initMap() {
	center = new ILatLng(23.14651977, 113.34106554);
	map = new IMap("map_resource", center, mapZoom, {});
	IMapEvent.addListener(map, "click", function() {
		if(!isMapClick){
			$("#rightInformation").hide();
			$("#right_hide_blue").show();
			$(".resources").css("right","100px");
			$("#middle_show2").css("right","0px");
				//$("#treeDiv").hide();
				//$("#middle_show").css("margin-left","26px");
			$("#middle_show").css("left","26px");
			$("#search_div").hide();
			$("#tree").hide();
			$("#menu").hide();
		}
	});
	IMapEvent.addListener(map, "zoom_changed", function() {
		var zoom = map.getZoom();
		if ( zoom < 13 ) {
			clearResourceText();
		} else {
			clearResourceText();
			addResourceText();
		}
		//显示资源
		if ( zoom < 12 ) {
			hideAllResourceMarker();
		} else if ( zoom == 12 && mapZoom < zoom ) {
			showAllResourceMarker();
		}
		mapZoom = zoom;
	});
}


//还原线的属性，清除信息框，当前操作标记
function clearCurElements() {
	if (curElement != null) {
		//		curElement.setMap(null);
		curElement = null;
	}
	for ( var i = 0; i < curLineArray.length; i++) {
		curLineArray[i][1].setOptions( {
			"strokeColor" : getLineColor(curLineArray[i][0]),
			"strokeWeight" : 3
		});
		if (curView == "TransmissionView"
				&& curLineArray[i][0] == "OpticalRouteSP2SP") {
			curLineArray[i][1].setMap(null);
		}
	}
	curLineArray = new Array();
	lineIndex = 0;

	if (infowindow != null) {
		infowindow.close();
		infowindow = null;
	}
	if (curLine != null) {
		curLine.setOptions( {
			"strokeColor" : getLineColor(curLineType),
			"strokeWeight" : 3
		});
	}
}


/******************************* 标记 begin **********************************/

function addResourceArray ( type , arr ) {
	var resArr = [];
	if ( type == "Station" ) { 
		resArr = marker_list["Station"];
	} else if ( type == "BaseStation" || 
				type == "GeneralBaseStation" || 
				type == "BaseStation_WLAN" || 
				type == "BaseStation_repeater" || 
				type == "BaseStation_TD" || 
				type == "BaseStation_GSM" ) {
		resArr = marker_list["BaseStation"];
	} else if ( type == "Pole" ) {
		resArr = marker_list["Pole"];
	} else if ( type == "ManWell" ) {
		resArr = marker_list["ManWell"];
	} else if ( type == "MarkPost" ) {
		resArr = marker_list["MarkPost"];
	} else if ( type == "HangWall" ) {
		resArr = marker_list["HangWall"];
	} else if ( type == "FiberCrossCabinet" ) {
		resArr = marker_list["FiberCrossCabinet"];
	} else if ( type == "FiberDistributionCabinet" ) {
		resArr = marker_list["FiberDistributionCabinet"];
	} else if ( type == "FiberTerminalCase" ) {
		resArr = marker_list["FiberTerminalCase"];
	}
	for ( var i = 0 ; i < arr.length ; i++ ) {
		resArr.push(arr[i]);
	}
}

var loadingMarker = [];
var showingMarker = [];
var isLoadingMarker = false;

function addLoadMarkerResource( resType ) {
	if ( !(resType in loadingMarker) ) {
		loadingMarker.push(resType);
		showWaitCue( "res" , marker_load_second );
	}
}

//读取资源
function showMarkerResource () {
	isLoadingMarker = true;
	if ( showingMarker.length == 0 ) {
		isLoadingMarker = false;
		closePleaseWaitTimer("res");
		$("#res_loading_name").text("");
		//无等待读取资源
		return ;
	}
	showWaitCue( "res" , marker_load_second );
	var resType = showingMarker.shift();
	var cname = merkerName[resType];
	if ( $("#res_loading_name").text() != "" ) {
		resourceCueDivChangeColor();
	}
	$("#res_loading_name").text(" " + cname + " ");
	showHideResourceMarker(resType,false);
}

//读取资源
function loadMarkerResource () {
	isLoadingMarker = true;
	if ( loadingMarker.length == 0 ) {
		isLoadingMarker = false;
		closePleaseWaitTimer("res");
		$("#res_loading_name").text("");
		//无等待读取资源
		return ;
	}
	var resType = loadingMarker.shift();
	var cname = merkerName[resType];
	if ( $("#res_loading_name").text() != "" ) {
		resourceCueDivChangeColor();
	}
	$("#res_loading_name").text(" " + cname + " ");
	loadMarker(resType);
}


//读取标记
function loadMarker(resType) {
	var areaId = $("#treeRoot").children().eq(2).val(); 
	loadResourceKey = new Date().getTime();
	var key = loadResourceKey;
	if ( resType == "" ) {
		loadMarkerResource();
		return;
	}
	var params = {
		"resType" : resType , 
		"areaId" : areaId
	}
	
	//清除marker
	$.ajax({
		"url" : "../gis/getMarkersAction" , 
		"data" : params , 
		"type" : "post" , 
		"success" : function (result){
			if ( loadResourceKey != key ) {
				return;
			}
			var data = eval("(" + result + ")");
			var ty = "";
			//标记站址
			if ("Station" in data) {
				ty = "Station";
				var station = data.Station;
				var icon = "icon/zhanzhi.png";
				stationArray = createMarkers(station, icon, "Station",true);
				
			}
			//普通基站
			if ( "GeneralBaseStation" in data 
					|| "BaseStation" in data 
					|| "BaseStation_GSM" in data 
					|| "BaseStation_repeater" in data 
					|| "BaseStation_TD" in data 
					|| "BaseStation_WLAN" in data ) {
				
				baseStationArray = new Array();
				
				
				if ( "GeneralBaseStation" in data ) {
					ty = "BaseStation";
					var baseStation = data.GeneralBaseStation;
					var icon = "icon/jizhan.png";
					baseStations = createMarkers(baseStation, icon, "GeneralBaseStation",
							true);
					for ( var i = 0 ; i < baseStations.length ; i++ ) {
						var bs = baseStations[i];
						baseStationArray.push(bs);
					}
					
				}
				//通用基站    
				if ( "BaseStation" in data ) {
					ty = "BaseStation";
					var baseStation = data.BaseStation;
					var icon = "icon/jizhan.png";
					baseStations = createMarkers(baseStation, icon, "BaseStation",
							true);
					for ( var i = 0 ; i < baseStations.length ; i++ ) {
						var bs = baseStations[i];
						baseStationArray.push(bs);
					}
				}
				//GSM基站
				if ( "BaseStation_GSM" in data ) {
					ty = "BaseStation";
					var baseStation_GSM = data.BaseStation_GSM;
					var icon = "icon/jizhan.png";
					baseStation_GSMs = createMarkers(baseStation_GSM, icon, "BaseStation_GSM",
							true);
					for ( var i = 0 ; i < baseStation_GSMs.length ; i++ ) {
						var bs = baseStation_GSMs[i];
						baseStationArray.push(bs);
					}
				}
				//repeater基站    
				if ( "BaseStation_repeater" in data ) {
					ty = "BaseStation";
					var baseStation_repeater = data.BaseStation_repeater;
					var icon = "icon/jizhan.png";
					baseStation_repeaters = createMarkers(baseStation_repeater, icon, "BaseStation_repeater",
							true);
					for ( var i = 0 ; i < baseStation_repeaters.length ; i++ ) {
						var bs = baseStation_repeaters[i];
						baseStationArray.push(bs);
					}
				}
				//TD基站    
				if ( "BaseStation_TD" in data ) {
					ty = "BaseStation";
					var baseStation_TD = data.BaseStation_TD;
					var icon = "icon/jizhan.png";
					baseStation_TDs = createMarkers(baseStation_TD, icon, "BaseStation_TD",
							true);
					for ( var i = 0 ; i < baseStation_TDs.length ; i++ ) {
						var bs = baseStation_TDs[i];
						baseStationArray.push(bs);
					}
				}
				//WLAN基站    
				if ( "BaseStation_WLAN" in data ) {
					ty = "BaseStation";
					var baseStation_WLAN = data.BaseStation_WLAN;
					var icon = "icon/jizhan.png";
					baseStation_WLANs = createMarkers(baseStation_WLAN, icon, "BaseStation_WLAN",
							true);
					for ( var i = 0 ; i < baseStation_WLANs.length ; i++ ) {
						var bs = baseStation_WLANs[i];
						baseStationArray.push(bs);
					}
				}
				
			}
			
			//标记电杆
			if ( "Pole" in data ) {
				ty = "Pole";
				var pole = data.Pole;
				var icon = "icon/pole1.png";
				poleArray = createMarkers(pole, icon, "Pole", true);
			}
			//标记人井
			if ("ManWell" in data) {
				ty = "ManWell";
				var manWell = data.ManWell;
				var icon = "icon/well.png";
				manWellArray = createMarkers(manWell, icon, "ManWell",
						true);
			}
			//标记标桩
			if ("MarkPost" in data) {
				ty = "MarkPost";
				var markPost = data.MarkPost;
				var icon = "icon/labelStone.png";
				markPostArray = createMarkers(markPost, icon,
						"MarkPost", true);
				
			}
			//标记挂墙点
			if ("HangWall" in data) {
				ty = "HangWall";
				var hangWall = data.HangWall;
				var icon = "icon/hangWall.png";
				hangWallArray = createMarkers(hangWall, icon,
						"HangWall", true);
				
			}
			//标记光交接箱
			if ("FiberCrossCabinet" in data) {
				ty = "FiberCrossCabinet";
				var fiberCrossCabinet = data.FiberCrossCabinet;
				var icon = "icon/gjjx.png";
				fiberCrossCabinetArray = createMarkers(
						fiberCrossCabinet, icon, "FiberCrossCabinet",
						true);
			}
			//标记光分纤箱
			if ("FiberDistributionCabinet" in data) {
				ty = "FiberDistributionCabinet";
				var fiberDistributionCabinet = data.FiberDistributionCabinet;
				var icon = "icon/gfxh.png";
				fiberDistributionCabinetArray = createMarkers(
						fiberDistributionCabinet, icon,
						"FiberDistributionCabinet", true);
				
			}
			//标记终端盒
			if ("FiberTerminalCase" in data) {
				ty = "FiberTerminalCase";
				var fiberTerminalCase = data.FiberTerminalCase;
				var icon = "icon/fiberTerminalCase.png";
				fiberTerminalCaseArray = createMarkers(
						fiberTerminalCase, icon, "FiberTerminalCase",
						true);
			}
			showHideResourceMarker(resType,false);
		}
	});
}



//生成标记(站址,人井,挂墙点,标桩,光交接箱,光分纤箱,终端盒,ODF,接头)
function createMarkers(data, icon, resType, visible) {
	var tempArray = new Array();
	visible = false;
	if ( data && data.length > 0) {
		for ( var i = 0; i < data.length; i++) {
			var lng, lat;
			var info = data[i];
			if ("parent" in info) {
				lng = info.parent.longitude;
				lat = info.parent.latitude;
			} else {
				lng = info.longitude;
				lat = info.latitude;
			}
			if (lat != "" && lat != null && lng != "" && lng != null) {
				lat = parseFloat(lat);
				lng = parseFloat(lng);
				var latlng = new ILatLng(lat, lng);
				var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
				var marker = new IMarker(mapLatLng, info.name, {});
				marker.setOptions({"icon":icon});
				if (visible) {
					marker.setMap(map);
				}
				
				if (info.name)
					marker.title = info.name;
				else
					marker.title = info.label;
				//创建标记标题
				createMarkerTitle(marker);
				
				marker.type_ = info._entityType;
				if ( merkerName[marker.type_] ) {
					marker.chinese_type_ = merkerName[marker.type_];
				} else {
					marker.chinese_type_ = "未知";
				}
				marker.id = info.id;
				marker.data_ = info;
				tempArray.push(marker);
				allMarkerArray.push(marker);
				bindMarkerEvent(marker, resType);
				if (resType == "Station") {
					if (i == data.length - 1) {
						center = mapLatLng;
						map.setCenter(center);
					}
				}
			}
		}
	}
	addResourceArray(resType,tempArray);
	return tempArray;
}


function checkLoadingMarker() {
	for ( var key in loadingMarker_map ) {
		var flag = $(loadingMarker_map[key]).attr("loading");
		if ( flag == "true" ) {
			return;
		}
	}
	
}

//创建标记标题
function createMarkerTitle( marker ) {
	if ( !$("#is_show_resourceName_chk").is(":checked") || map.getZoom() < 13 ) {
		return ;
	}
	var pos = marker.getPosition();
	var title = "<lable style=' border-radius:5px; background-color:white; opacity:0.8; color:blue; font-size:13px;'>"+marker.title+"</lable>"
	marker.textOverlay_ = new ITextOverlay(pos,title,{});
	marker.textOverlay_.setMap(map);
}

//删除标记标题
function delMarkerTitle( marker ) {
	if ( !marker.textOverlay_ ) {
		return ;
	}
	marker.textOverlay_.setMap(null);
	marker.textOverlay_ = undefined;
}

//点击地图上的图标时的事件
function bindMarkerEvent(marker, resType) {
	IMapEvent.addListener(marker, "click", function() {
		if ( isMapClick ) { 
			var arr = mapEntityTypes.split(",");
			for(var i = 0;arr.length > i;i++){
				if( arr[i] == resType ){
					var flag = confirm("是否确认选择资源 ：" + marker.title + "?");
					if ( flag ){
						clickMapLINKHide(mapEntityDivID , marker.title , resType , marker.id );
						break;
					}
				}
			}
			return;
		}
		if (infowindow != null) {
			infowindow.close();
			infowindow = null;
		}
		$("#show").hide();
		
		infowindow = new IInfoWindow("", {
			"position" : marker.getPosition()
		});
		infowindow.setContent("");
		
		var aTitle = createInfoWindow( marker );
		//var aTitle = marker.title;
		infowindow.setContent(aTitle);
		//marker.openInfoWindow(infowindow);
		//infowindow.open(map);
		openInfoWindow( marker , infowindow);
		//$(".gis_dialog").parent().parent().prev("div").css({"right":"30px","top":"26px"});
		//$(".gis_dialog").parent().parent().parent().prev("div").css({"width":"900px"});
		//$(".gis_dialog").parent().parent().css({"min-height":"192px","min-width":"324px","left":"2%","top":"2%"});
		//$(".gis_dialog").parent().parent().parent().prev("div").css({"padding":"0px"});
		//调用Js显示右边
		var currentEntityId = marker.id;
		var currentEntityType = marker.type_;
		var params = {
			currentEntityId : currentEntityId,
			currentEntityType : currentEntityType
		}
		$.post("../physicalres/getPhysicalresAction", params,
			function(data) {
				$("#rightInformation").html(data);
				/*$("#imgRight").attr("src", "image/hide.png");
				$("#rightInformation").show();
				
				//右边信息层控制
				$("#middle_show2").css("right","320px");
				if($("#rightInformation").css("display") == "block"){
					$("#right_show_blue").hide();
					$("#right_hide_blue").hide();
					$("#right_hide_grey").hide();
				}else{
					$("#right_show_blue").hide();
					$("#right_hide_blue").hide();
					$("#right_hide_grey").show();
				}*/
				//右边信息层控制
				var isCheck = !!$("#notejectCheckBox").attr("checked");
				if ( !isCheck ) {
					$("#middle_show2").css("right","320px");
					$("#rightInformation").show();
					$("#right_hide_blue").hide();
					$("#right_show_blue").show();
					$(".resources").css("right","10px");
				}
		});
	});
}


/******************************* 标记 end **********************************/



/************************** 资源网络 begin *******************************/

//获取管道的相关设施信息
function getPipeRouteInfo() {
	clearAllLines();
	//还原线的属性，清除信息框，当前操作标记
	/*clearFiberSection();//清除光缆段
	clearFiberCore();//清除纤芯
	clearOpticalRouteSP2SP();//清除局向纤芯
	clearTransmissionSection();//清除传输段*/
	var areaId = $("#treeRoot").children().eq(2).val(); 

	var values = {
		"areaId" : areaId
	}
	showWaitCue( line_load_second );
	$
			.ajax( {
				url : "../gis/getPipeRouteInfoAction",
				type : "POST",
				data : values,
				dataType : "text",
				async : true,
				success : function(result) {
					if (result != "") {
						var data = eval("(" + result + ")");
						var station = data.Station;
						var pole = data.Pole;
						var markPost = data.MarkPost;
						var manWell = data.ManWell;
						var hangWall = data.HangWall;
						var pipeLine = data.PipeLine;
						var buriedLine = data.BuriedLine;
						var poleLine = data.PoleLine;

						//标记站址
						if (stationArray.length == 0) {
							var icon = "icon/zhanzhi.png";
							stationArray = createMarkers(station, icon,
									"Station", true);
						}

						//标记电杆
						if (poleArray.length == 0) {
							var icon = "icon/pole1.png";
							poleArray = createMarkers(pole, icon, "Pole", true);
						}
						//标记人井
						if (manWellArray.length == 0) {
							var icon = "icon/well.png";
							manWellArray = createMarkers(manWell, icon,
									"ManWell", true);
						}
						//标记标桩
						if (markPostArray.length == 0) {
							var icon = "icon/labelStone.png";
							markPostArray = createMarkers(markPost, icon,
									"MarkPost", false);
						}
						//标记挂墙点
						if (hangWallArray.length == 0) {
							var icon = "icon/hangWall.png";
							hangWallArray = createMarkers(hangWall, icon,
									"HangWall", false);
						}
						//生成管道段
						if (pipeLine.length != 0) {
							var color = "#0071c1";
							pipeLineArray = createLine(pipeLine, color,
									"PipelineSection");
						}
						//生成直埋段
						if (buriedLine.length != 0) {
							var color = "#99a1cf";
							buriedLineArray = createLine(buriedLine, color,
									"BuriedlineSection");
						}
						//生成吊线段
						if (poleLine.length != 0) {
							var color = "#00b24e";
							poleLineArray = createLine(poleLine, color,
									"PolelineSection");
						}
					}
					
					$("#a_station").show();
					$("#a_manWell").show();
					$("#a_pole").show();
					$("#a_markPost").show();
					$("#a_hangWall").show();
					$("#a_ODF").hide();
					$("#a_fiberCrossCabinet").hide();
					$("#a_fiberDistributionCabinet").hide();
					$("#a_fiberTerminalCase").hide();
					$("#a_joint").hide();
					closePleaseWaitTimer("line");
				}
			});
}

//获取光缆的相关设施信息
function getFiberInfo() {
	clearAllLines();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除纤芯
	//clearOpticalRouteSP2SP();//清除局向纤芯
	//clearTransmissionSection();//清除传输段
	
	var areaId = $("#treeRoot").children().eq(2).val(); 
	
	var values = {
		"areaId" : areaId
	}
	
	$.ajax( {
		url : "../gis/getFiberInfoAction",
		type : "POST",
		data : values,
		dataType : "text",
		async : true,
		success : function(result) {
					if (result != "") {
						var data = eval("(" + result + ")");
						//				alert(data.toSource())
						var station = data.Station;
						var pole = data.Pole;
						var markPost = data.MarkPost;
						var manWell = data.ManWell;
						var hangWall = data.HangWall;
						var fiberCrossCabinet = data.FiberCrossCabinet;
						var fiberDistributionCabinet = data.FiberDistributionCabinet;
						var fiberTerminalCase = data.FiberTerminalCase;
						var joint = data.Joint;
						var fiberSection = data.FiberSection;
						var fiber = data.Fiber;
						var odf = data.ODF;
				
						//标记站址
						if (stationArray.length == 0) {
							var icon = "icon/zhanzhi.png";
							stationArray = createMarkers(station, icon, "Station", true);
						}
				
						//标记电杆
						//				alert(pole.length)
						if (poleArray.length == 0) {
							var icon = "icon/pole1.png";
							poleArray = createMarkers(pole, icon, "Pole", true);
						}
						//标记人井
						if (manWellArray.length == 0) {
							var icon = "icon/well.png";
							manWellArray = createMarkers(manWell, icon, "ManWell", true);
						}
				
						//标记光交接箱
						if (fiberCrossCabinetArray.length == 0) {
							var icon = "icon/gjjx.png";
							fiberCrossCabinetArray = createMarkers(fiberCrossCabinet, icon,
									"FiberCrossCabinet", false);
						}
						//标记光分纤箱
						if (fiberDistributionCabinetArray.length == 0) {
							var icon = "icon/gfxh.png";
							fiberDistributionCabinetArray = createMarkers(
									fiberDistributionCabinet, icon, "FiberDistributionCabinet",
									false);
						}
						//标记终端盒
						if (fiberTerminalCaseArray.length == 0) {
							var icon = "icon/fiberTerminalCase.png";
							fiberTerminalCaseArray = createMarkers(fiberTerminalCase, icon,
									"FiberTerminalCase", false);
						}
				
						//标记ODF(站址的子)
						if (odfArray.length == 0) {
							var icon = "icon/odf.png";
							odfArray = createMarkers(odf, icon, "ODF", false);
						}
						//标记接头(人井，电杆的子)
						if (jointArray.length == 0) {
							var icon = "icon/joint.png";
							jointArray = createMarkers(joint, icon, "Joint", false);
						}
						//生成光缆段
						if (fiberSection.length != 0) {
							fiberSectionArray = createLine(fiberSection, "#92d343",
									"FiberSection");
						}
			}
			closePleaseWaitTimer("line");
		}
	});

	$("#a_station").show();
	$("#a_manWell").show();
	$("#a_pole").show();
	$("#a_ODF").show();
	$("#a_fiberCrossCabinet").show();
	$("#a_fiberDistributionCabinet").show();
	$("#a_fiberTerminalCase").show();
	$("#a_joint").show();
	$("#a_markPost").hide();
	$("#a_hangWall").hide();
}

//获取纤芯的相关设施信息
function getFiberCoreInfo() {
	//clearCurElements();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除光缆段
	//clearOpticalRouteSP2SP();//清除局向纤芯
	//clearTransmissionSection();//清除传输段
	clearAllLines();
	var areaId = $("#treeRoot").children().eq(2).val(); 

	var values = {
		area : "" , 
		"areaId" : areaId
	}
	$.ajax( {
		url : "../gis/getFiberCoreInfoAction",
		type : "POST",
		data : values,
		dataType : "text",
		async : true,
		success : function(result) {
			if (result != "") {
				var data = eval("(" + result + ")");

				var station = data.Station;
				var pole = data.Pole;
				var markPost = data.MarkPost;
				var manWell = data.ManWell;
				var hangWall = data.HangWall;
				var fiberCrossCabinet = data.FiberCrossCabinet;
				var fiberDistributionCabinet = data.FiberDistributionCabinet;
				var fiberTerminalCase = data.FiberTerminalCase;
				var joint = data.Joint;
				var fiberCore = data.FiberCore;
				var fiberSection = data.FiberSection;
				var odf = data.ODF;

				//标记站址
				if (stationArray.length == 0) {
					var icon = "icon/zhanzhi.png";
					stationArray = createMarkers(station, icon, "Station", true);
				}
		
				//标记电杆
				if (poleArray.length == 0) {
					var icon = "icon/pole1.png";
					poleArray = createMarkers(pole, icon, "Pole", true);
				}
				//标记人井
				if (manWellArray.length == 0) {
					var icon = "icon/well.png";
					manWellArray = createMarkers(manWell, icon, "ManWell", true);
				}
		
				//标记光交接箱
				if (fiberCrossCabinetArray.length == 0) {
					var icon = "icon/gjjx.png";
					fiberCrossCabinetArray = createMarkers(fiberCrossCabinet, icon,
							"FiberCrossCabinet", false);
				}
				//标记光分纤箱
				if (fiberDistributionCabinetArray.length == 0) {
					var icon = "icon/gfxh.png";
					fiberDistributionCabinetArray = createMarkers(
							fiberDistributionCabinet, icon, "FiberDistributionCabinet",
							false);
				}
				//标记终端盒
				if (fiberTerminalCaseArray.length == 0) {
					var icon = "icon/fiberTerminalCase.png";
					fiberTerminalCaseArray = createMarkers(fiberTerminalCase, icon,
							"FiberTerminalCase", false);
				}
		
				//标记ODF(站址的子)
				if (odfArray.length == 0) {
					var icon = "icon/odf.png";
					odfArray = createMarkers(odf, icon, "ODF", false);
				}
				//标记接头(人井，电杆的子)
				if (jointArray.length == 0) {
					var icon = "icon/joint.png";
					jointArray = createMarkers(joint, icon, "Joint", false);
				}
				//生成纤芯
				if (fiberCore.length != 0)
					fiberCoreArray = createLine(fiberCore, "#fbf638", "FiberCore");
			}
			closePleaseWaitTimer("line");	
		}
	});

	$("#a_station").show();
	$("#a_manWell").show();
	$("#a_pole").show();
	$("#a_markPost").hide();
	$("#a_hangWall").hide();
	$("#a_ODF").show();
	$("#a_fiberCrossCabinet").show();
	$("#a_fiberDistributionCabinet").show();
	$("#a_fiberTerminalCase").show();
	$("#a_joint").show();
}

//获取光路的相关设施信息
function getOpticalRouteInfo() {
	//clearCurElements();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除光缆段
	//clearFiberCore();//清除纤芯
	//clearTransmissionSection();//清除传输段
	clearAllLines();
	opticalRouteView = true;
	var areaId = $("#treeRoot").children().eq(2).val(); 

	var values = {
		area : "" , 
		"areaId" : areaId
	}
	
	$
			.ajax( {
				url : "../gis/getOpticalRouteInfoAction",
				type : "POST",
				data : values,
				dataType : "text",
				async : true,
				success : function(result) {
					if (result != "") {
						var data = eval("(" + result + ")");

						var station = data.Station;
						var fiberCrossCabinet = data.FiberCrossCabinet;
						var fiberDistributionCabinet = data.FiberDistributionCabinet;
						var fiberTerminalCase = data.FiberTerminalCase;
						var opticalRouteSP2SP = data.OpticalRouteSP2SP;
						var opticalRouteP2P = data.OpticalRouteP2P;
						var opticalRoute = data.OpticalRoute;
						var odf = data.ODF;

						//标记站址
						if (stationArray.length == 0) {
							var icon = "icon/zhanzhi.png";
							stationArray = createMarkers(station, icon,
									"Station", true);
						}

						//标记光交接箱
						if (fiberCrossCabinetArray.length == 0) {
							var icon = "icon/gjjx.png";
							fiberCrossCabinetArray = createMarkers(
									fiberCrossCabinet, icon,
									"FiberCrossCabinet", false);
						}
						//标记光分纤箱
						if (fiberDistributionCabinetArray.length == 0) {
							var icon = "icon/gfxh.png";
							fiberDistributionCabinetArray = createMarkers(
									fiberDistributionCabinet, icon,
									"FiberDistributionCabinet", false);
						}
						//标记终端盒
						if (fiberTerminalCaseArray.length == 0) {
							var icon = "icon/fiberTerminalCase.png";
							fiberTerminalCaseArray = createMarkers(
									fiberTerminalCase, icon,
									"FiberTerminalCase", false);
						}

						//标记ODF(站址的子)
						if (odfArray.length == 0) {
							var icon = "icon/odf.png";
							odfArray = createMarkers(odf, icon, "ODF", false);
						}
						//生成局向纤芯
						if (opticalRouteSP2SP.length == 0) {
							opticalRouteSP2SPArray = createLine(
									opticalRouteSP2SP, "#fcc300",
									"OpticalRouteSP2SP");
						} else if (transmissionView == true) {
							opticalRouteSP2SPArray = new Array();
							opticalRouteSP2SPArray = createLine(
									opticalRouteSP2SP, "#fcc300",
									"OpticalRouteSP2SP");
						}
					}
					closePleaseWaitTimer("line");	
				}
			});

	$("#a_station").show();
	$("#a_manWell").hide();
	$("#a_pole").hide();
	$("#a_markPost").hide();
	$("#a_hangWall").hide();
	$("#a_ODF").show();
	$("#a_fiberCrossCabinet").show();
	$("#a_fiberDistributionCabinet").show();
	$("#a_fiberTerminalCase").show();
	$("#a_joint").hide();
}

//获取传输拓扑的相关设施信息   
function getTransmissionInfo() {
	//clearCurElements();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除光缆段
	//clearFiberCore();//清除纤芯
	//clearOpticalRouteSP2SP();//清除局向纤芯
	clearAllLines();
	curView = "TransmissionView";
	transmissionView = true;
	var areaId = $("#treeRoot").children().eq(2).val(); 

	var values = {
		area : "" , 
		"areaId" : areaId
	}
	$
			.ajax( {
				url : "../gis/getTransmissionInfoAction",
				type : "POST",
				data : values,
				dataType : "text",
				async : true,
				success : function(result) {
					if (result != "") {
						var data = eval("(" + result + ")");
						var station = data.Station;
						var transmissionSection = data.TransmissionSection;
						var transmissionNetwork = data.TransmissionNetwork;

						//标记站址
						if (stationArray.length == 0) {
							var icon = "icon/zhanzhi.png";
							stationArray = createMarkers(station, icon,
									"Station", true);
						}
						//生成传输段
						if (transmissionSectionArray.length == 0)
							transmissionSectionArray = createLine(
									transmissionSection, "#9c0e0d",
									"TransmissionSection");
						//生成局向纤芯，但是暂不显示
						if (opticalRouteSP2SPArray.length == 0) {
							for ( var i = 0; i < transmissionSection.length; i++) {
								var opticalRouteSP2SP = transmissionSection[i].opticalRouteSP2SP;
								opticalRouteSP2SPArray = createLine(
										opticalRouteSP2SP, "#fcc300",
										"OpticalRouteSP2SP");
								//不显示
								for ( var j = 0; j < opticalRouteSP2SPArray.length; j++) {
									opticalRouteSP2SPArray[j].setMap(null);
								}
							}
						}
					}
					closePleaseWaitTimer("line");	
				}
			});

	$("#a_station").show();
	$("#a_manWell").hide();
	$("#a_pole").hide();
	$("#a_markPost").hide();
	$("#a_hangWall").hide();
	$("#a_ODF").hide();
	$("#a_fiberCrossCabinet").hide();
	$("#a_fiberDistributionCabinet").hide();
	$("#a_fiberTerminalCase").hide();
	$("#a_joint").hide();
}


//生成线性元素(管道段,直埋段,吊线段,光缆段,纤芯,局向光纤,传输段)
function createLine(data, color, resType) {
	var tempArray = new Array();
	for ( var i = 0; i < data.length; i++) {
		var facility;
		if ("parentFacility" in data[i])
			facility = data[i].parentFacility;
		else
			facility = data[i].connFacility;

		var linePath = new Array();
		for ( var j = 0; j < facility.length; j++) {
			var lng = facility[j].longitude;
			var lat = facility[j].latitude;
			lat = parseFloat(lat);
			lng = parseFloat(lng);
			//				alert("管道段"+lat+"=="+lng+"=="+facility[j].name);
			var latlng = new ILatLng(lat, lng);
			var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
			linePath.push(mapLatLng);
		}
		var polyline = new IPolyline(linePath, color, 3, {
			"map" : map
		});
		//		polyline.id = data[i].line.id;
		polyline.id = data[i].id;
		var id = data[i].belong.id;
		if (id) {
			polyline.parentid = id;
		} else {
			polyline.parentid = "";
		}
		if ("grandBelong" in data[i]) {
			id = data[i].grandBelong.id;
			if (id)
				polyline.grandid = id;
			else
				polyline.grandid = "";
		}
		tempArray.push(polyline);
		bindLineEvent(polyline, resType);
	}
	return tempArray;
}

/************************** 资源网络 end *******************************/


/************************** 信息框 begin *********************************/

//创建信息窗体
function createInfoWindow ( marker ) {
	var markerPoint = marker.getPosition();
	var data = marker.data_;//获取数据
	//标题
	var titleName = "";
	if ( data["name"] ) {
		titleName = data["name"];
	} else if ( data["label"] ){
		titleName = data["label"];
	}
	
	//获取区域
	var detailink = "#";
	var editlink = "#"; 
	$.ajax({
		"url" : "infow_ajax!getResourceArea.action" , 
		"type" : "post" , 
		"async" : false , 
		"data" : { "resourceType" : marker.type_ , "resourceId" : marker.id } , 
		"success" : function(result) {
			result = eval( "(" + result + ")" );
			var detail_param = {
				"currentEntityType" : marker.type_ , 
				"currentEntityId" : marker.id , 
				"areaId" : result.id , 
				"modelType" : "view"
			}; 
			detailink = createUrl("../physicalres/getPhysicalresForOperaAction",detail_param);
			var edit_param = {
				"currentEntityType" : marker.type_ , 
				"currentEntityId" : marker.id , 
				"parentEntityType" : undefined , 
				"parentEntityId" : undefined , 
				"areaId" : result.id
			}; 
			editlink = createUrl("../physicalres/getPhysicalresForOperaAction",edit_param); 
		}
	});
	
	
	//获取图片
	var img_maxPage = 0;
	var img_pageIndex = 0;
	var img_list = "";
	var img_page_display="";
	$.ajax({
		"url" : "infow_ajax!getResourceImg.action" , 
		"type" : "post" , 
		"async" : false , 
		"data" : { "resourceType" : marker.type_ , "resourceId" : marker.id } , 
		"success" : function(result) {
			result = eval( "(" + result + ")" );
			if ( !result ) {
				return;
			}
			for ( var i = 0 ; i < result.length ; i++ ) {
				img_list += "<li><img src='../../upload/" + result[i].uuidname + "' /></li>";
			}
			
			if ( result.length > 0 ) {
				img_pageIndex = 1;
				img_maxPage = Math.ceil(result.length / dialog_showImg_count);
			} else if ( result.length == 0 ) {
				img_pageIndex = 0;
				img_maxPage = 0;
			}
			if ( img_maxPage <= 1 ) {
				img_page_display = "style='display:none;'" ; 
			}
		}
	});
	
	//获取资源维护记录
	var repair_list = "";
	var sTime = new Date().toString("yyyy-MM-dd HH:mm:ss");
	var eTime = new Date().addMonths(-1).toString("yyyy-MM-dd HH:mm:ss");
	var repair_param = { "resourceType" : marker.type_ , "resourceId" : marker.id , "sTime" : eTime , "eTime" : sTime };
	$.ajax({
		"url" : "loadNetworkResourceMaintenanceAction.action" , 
		"type" : "post" , 
		"async" : false , 
		"data" : repair_param , 
		"success" : function(result) {
			result = eval(result);
			marker.resourceRepair_ = result ;
			if ( !result || result.length == 0 ) {
				repair_list += "<li>没有记录</li>";
				return ;
			}
			for ( var i = 0 ; i < 6 && i < result.length ; i++ ) {
				repair_list += "<li info='" + result[i].toSource() + "' onclick='showSingleOpenInfoWindow(" + result[i].id + ",this)'>" + (i+1) + "</li>";
			}
			if ( result.length > 6 ) {
				repair_list += "<li onclick='showAllOpenInfoWindow(" + repair_param.toSource() + ",this)'>全部</li>";
			}
		}
	});
	
	//对话框外层div
	var gis_dialog = 		"<div class='gis_dialog'> " + 
							"	<h2> " + 
							"		<b>" + marker.chinese_type_ + "：</b> " + 
							"		<span>" + titleName + "</span> " + 
							"		<a href='" + detailink + "' target='_blank'>详情浏览</a> " + 
							"	</h2> ";
							
							if ( img_maxPage != 0 ) {  
								gis_dialog +=	"	<div class='dialog_img_box'> " + 
												"		<div class='dialog_img_info'> " + 
												"			<ul> " + 
																img_list + 
												"			</ul> " + 
												"		</div> " + 
												"		<div class='dialog_img_arrow' " + img_page_display + "> " + 
												"			<p class='dialog_img_left' onclick='infoWindow_img_leftClick();'></p> " + 
												"			<p class='dialog_img_page'> " + 
												"				<span class='dialog_pageIndex'>" + img_pageIndex + "</span> " + 
												"				<span>/</span> " + 
												"				<span class='dialog_maxPage'>" + img_maxPage + "</span> " + 
												"			</p> " +
												"			<p class='dialog_img_right' onclick='infoWindow_img_rightClick();'></p> " + 
												"		</div> " + 
												"	</div> ";
							} else {
								gis_dialog += "<div class='dialog_img_empty_box'></div>";
							} 
							
		gis_dialog += 		"	<div class='dialog_info'> " + 
							"		<div class='clearfix'> " + 
							"			<p class='dialog_info_tab'> " + 
							"				<span>维护与服务</span> " + 
							"			</p> " + 
							"			<p class='dialog_info_other'> " + 
							"				<a href='javascript:void();' onclick='move2MapCenterAndZoomMax( " + markerPoint.getLongitude() + " , " + markerPoint.getLatitude() + " );'><em class='other_icon1'></em>周边环境</a> " + 
							"				<a href='" + editlink + "' target='_blank'><em class='other_icon2'></em>修改</a> " + 
							"			</p> " + 
							"		</div> " + 
							"		<div class='dialog_info_detail'> " + 
							"			<div class='maintain_record clearfix'> " + 
							"				<div class='maintain_record_name'> " + 
							"					<p>维护记录：<span class='fc'>[近1个月内]</span></p> " + 
							"				</div> " + 
							"				<ul class='maintain_record_list'> " + 
												repair_list + 
							"				</ul> " + 
							"			</div> " + 
							"			<div class='maintain_task clearfix'> " + 
							"				<div class='maintain_task_name'>维护任务：</div> " + 
							"				<ul class='maintain_task_list'> " + 
							"					<li>1</li> " + 
							"					<li>2</li> " + 
							"					<li>3</li> " + 
							"					<li>4</li> " + 
							"					<li>5</li> " + 
							"					<li>全部</li> " + 
							"				</ul> " + 
							"			</div> " + 
							"		</div> " + 
							"	</div> " + 
							"</div>";
	return gis_dialog;
}


//打开提示框
function openInfoWindow ( marker , infowindow ) {
	isAround = true;
	marker.openInfoWindow(infowindow);
	setTimeout(function(){
		$(".gis_dialog img").large();
	},1000);
	//infowindow.open(map);
}

/************************** 信息框 end *********************************/



//移动地图中心点并把视角放大
function move2MapCenterAndZoomMax( lng , lat ) {
	//判断是否放大或者返回视图
	if ( isAround ) {
		//保存地图原有信息
		var latlng = map.getCenter();
		cache_map_data["lng"] = latlng.getLongitude();
		cache_map_data["lat"] = latlng.getLatitude();
		cache_map_data["zoom"] = map.getZoom();
		//平移地图
		map.setZoom(21);
		var pos = new ILatLng(lat,lng);
		map.setCenter(pos);
	} else {
		map.setZoom(cache_map_data["zoom"]);
		var pos = new ILatLng(cache_map_data["lat"],cache_map_data["lng"]);
		map.setCenter(pos);
	}
	isAround = !isAround;
}


//显示所有维护记录的列表信息
function showAllOpenInfoWindow ( data , widget ) {
	var repair_param = eval(data);
	$.ajax({
		"url" : "loadNetworkResourceMaintenanceAction.action" , 
		"type" : "post" , 
		"async" : false , 
		"data" : repair_param , 
		"success" : function(result) {
			result = eval(result);
			all_repair_info_page.setDataArray(result);
			all_repair_info_page.refreshTable();
			all_repair_info_page.checkButton();
			
			var win_width = $(window).width();
	        var dialog_width = $("#repair_all_info_dialog").width();
	        var win_height = $(window).height();
	        var dialog_height = $("#repair_all_info_dialog").height();
	        
	        
	        var left = (win_width*0.5) - (dialog_width*0.5);
	        var top = (win_height*0.5) - (dialog_height*0.5);
	        $("#repair_all_info_dialog").offset({ "top": top, "left": left });
			$(".info_dialog_black").css({"visibility":"visible"});
			$("#repair_all_info_dialog").css({"visibility":"visible"});
		}
	});
}

//显示单个维护记录信息
function showSingleOpenInfoWindow(id,widget){
	var context = "";
	$("#repair_single_info_dialog").css({"visibility":"hidden"});
	$.ajax({
		"url" : "../maintain/loadNetworkResourceMaintenanceByIdAction",
		"data" : {mId:id}, 
		"async" : false , 
		"success" : function(data){
			data = eval("(" + data + ")");
			if(data){
				var did = "";
				if(data.id && data.id != "null"){
					did = data.id;
				}
					context = context
				+"<tr>"
		                +" <td class='info_dialog_td'>唯一标识</td>"
		               + " <td>"+did+"</td>"
		            		+" </tr>";
		            		var biz_module = "";
				if(data.biz_module && data.biz_module != "null"){
					biz_module = data.biz_module;
				}
		            		context = context
		     	      +"<tr>"
		                +" <td class='info_dialog_td'><span class='blue'>业务模块</span></td>"
		               + " <td>"+biz_module+"</td>"
		            		+" </tr>";
		            		var op_scene = "";
				if(data.op_scene && data.op_scene != "null"){
					op_scene = data.op_scene;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'><span class='blue'>操作场景</span></td>"
		               + " <td>"+op_scene+"</td>"
		            		+" </tr>";
		            		var op_cause = "";
				if(data.op_cause && data.op_cause != "null"){
					op_cause = data.op_cause;
				}
		        context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'><span class='blue'>操作原因</span></td>"
		               + " <td>"+op_cause+"</td>"
		            		+" </tr>";
		            		var op_category = "";
				if(data.op_category && data.op_category != "null"){
					op_category = data.op_category;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>操作分类</td>"
		               + " <td>"+op_category+"</td>"
		            		+" </tr>";
		            		var linkurl = "";
				if(data.linkurl && data.linkurl != "null"){
					linkurl = data.linkurl;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>链接</td>"
		               + " <td>"+linkurl+"</td>"
		            		+" </tr>";
		            		var content;
		            		var contents = "";
				if(data.content && data.content != "null"){
					content = data.content.split("$_$");
				}
				if(content){
					$.each(content,function(k,v){
					contents = contents + v + "</br>"
					});
				}
		        context = context +" <tr>"
					              +"   <td class='info_dialog_td'><span class='blue'>详细内容</span></td>"
					              +"   <td>"+contents+"</td>"
					              +" </tr>";
		            		var user_name = "";
				if(data.user_name && data.user_name != "null"){
					user_name = data.user_name;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'><span class='blue'>操作人</span></td>"
		               + " <td>"+user_name+"</td>"
		            		+" </tr>";
		            		var src_teminal = "";
				if(data.src_teminal && data.src_teminal != "null"){
					src_teminal = data.src_teminal;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>终端设备</td>"
		               + " <td>"+src_teminal+"</td>"
		            		+" </tr>";
		            		var op_time = "";
				if(data.op_time && data.op_time != "null"){
					op_time = data.op_time;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'><span class='blue'>操作时间</span></td>"
		               + " <td>"+op_time+"</td>"
		            		+" </tr>";
		            		var longitude = "";
				if(data.longitude && data.longitude != "null"){
					longitude = data.longitude;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>经度</td>"
		               + " <td>"+longitude+"</td>"
		            		+" </tr>";
		            		var latitude = "";
				if(data.latitude && data.latitude != "null"){
					latitude = data.latitude;
				}
		            		context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>纬度</td>"
		               + " <td>"+latitude+"</td>"
		            		+" </tr>";
		            		var record_type = "";
				if(data.record_type && data.record_type != "null"){
					if(data.record_type == 0){
						record_type = "业务调用";
					}else if(data.record_type == 1){
						record_type = "系统强制";
					}else if(data.record_type == 2){
						record_type = "其他";
					}
				}
		        context = context
		            		+"<tr>"
		                +" <td class='info_dialog_td'>记录类型</td>"
		               + " <td>"+record_type+"</td>"
		            		+" </tr>";
		        
			}
	        //要加载的div
	        var div = $(widget).parents(".gis_dialog").parent();
	        
	        $("#repair_single_info_dialog_table").html(context);
	        
	        
	        var win_width = $(window).width();
	        var dialog_width = $("#repair_single_info_dialog").width();
	        var win_height = $(window).height();
	        var dialog_height = $("#repair_single_info_dialog").height();
	        
	        
	        var left = (win_width*0.5) - (dialog_width*0.5);
	        var top = (win_height*0.5) - (dialog_height*0.5);
	        $("#repair_single_info_dialog").offset({ "top": top, "left": left });
	        $(".info_dialog_black").css({"visibility":"visible"});
	        $("#repair_single_info_dialog").css({"visibility":"visible"});
		}
	});
	
}
			


//点击地图上的线时的事件
function bindLineEvent(line, resType) {
	IMapEvent.addListener(line, "click", function() {
		$("#show").hide();
		clearCurElements();
		curLine = line;
		curLineType = resType;
		curLine.setOptions( {
			"strokeColor" : "red",
			"strokeWeight" : 4
		});
		showLogicalres(line.id, resType, "view");
	});
}

//响应操作，在地图显示相应元素
function showElements(id, resType) {
	clearCurElements();
	switch (resType) {
	case "Station"://站址
		for ( var i = 0; i < stationArray.length; i++) {
			if (stationArray[i].id == id) {
				curElement = stationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "BaseStation"://通用基站
		for ( var i = 0; i < baseStationArray.length; i++) {
			if (baseStationArray[i].id == id) {
				curElement = baseStationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "BaseStation_WLAN"://wlan基站
		for ( var i = 0; i < baseStationArray.length; i++) {
			if (baseStationArray[i].id == id) {
				curElement = baseStationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "BaseStation_TD"://td基站
		for ( var i = 0; i < baseStationArray.length; i++) {
			if (baseStationArray[i].id == id) {
				curElement = baseStationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
    case "BaseStation_GSM"://gsm基站
		for ( var i = 0; i < baseStationArray.length; i++) {
			if (baseStationArray[i].id == id) {
				curElement = baseStationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
    case "BaseStation_repeater"://repeater基站
		for ( var i = 0; i < baseStationArray.length; i++) {
			if (baseStationArray[i].id == id) {
				curElement = baseStationArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;		
	case "ManWell"://人井
		for ( var i = 0; i < manWellArray.length; i++) {
			if (manWellArray[i].id == id) {
				curElement = manWellArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "Pole"://电杆
		for ( var i = 0; i < poleArray.length; i++) {
			if (poleArray[i].id == id) {
				curElement = poleArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "MarkPost"://标桩
		for ( var i = 0; i < markPostArray.length; i++) {
			if (markPostArray[i].id == id) {
				curElement = markPostArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "HangWall"://挂墙点
		for ( var i = 0; i < hangWallArray.length; i++) {
			if (hangWallArray[i].id == id) {
				curElement = hangWallArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "FiberCrossCabinet"://光交接箱
		for ( var i = 0; i < fiberCrossCabinetArray.length; i++) {
			if (fiberCrossCabinetArray[i].id == id) {
				curElement = fiberCrossCabinetArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "FiberDistributionCabinet"://光分纤箱
		for ( var i = 0; i < fiberDistributionCabinetArray.length; i++) {
			if (fiberDistributionCabinetArray[i].id == id) {
				curElement = fiberDistributionCabinetArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "FiberTerminalCase"://终端盒
		for ( var i = 0; i < fiberTerminalCaseArray.length; i++) {
			if (fiberTerminalCaseArray[i].id == id) {
				curElement = fiberTerminalCaseArray[i];
				map.setCenter(curElement.getPosition());
			}
		}
		break;
	case "PipelineSection"://管道段
		for ( var i = 0; i < pipeLineArray.length; i++) {
			if (pipeLineArray[i].id == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "PipelineSection";
				curLineArray[lineIndex][1] = pipeLineArray[i];
				lineIndex++;
			}
		}
		break;
	case "BuriedlineSection"://直埋段
		for ( var i = 0; i < buriedLineArray.length; i++) {
			if (buriedLineArray[i].id == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "BuriedlineSection";
				curLineArray[lineIndex][1] = buriedLineArray[i];
				lineIndex++;
			}
		}
		break;
	case "PipeRoute"://管道
		for ( var i = 0; i < pipeLineArray.length; i++) {
			if (pipeLineArray[i].parentid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "PipelineSection";
				curLineArray[lineIndex][1] = pipeLineArray[i];
				lineIndex++;
			}
		}
		for ( var i = 0; i < buriedLineArray.length; i++) {
			if (buriedLineArray[i].parentid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "BuriedlineSection";
				curLineArray[lineIndex][1] = buriedLineArray[i];
				lineIndex++;
			}
		}
		break;
	case "PolelineSection"://吊线段
		for ( var i = 0; i < poleLineArray.length; i++) {
			for ( var i = 0; i < poleLineArray.length; i++) {
				if (poleLineArray[i].parentid == id) {
					curLineArray[lineIndex] = new Array();
					curLineArray[lineIndex][0] = "PolelineSection";
					curLineArray[lineIndex][1] = poleLineArray[i];
					lineIndex++;
				}
			}
		}
		break;
	case "PoleRoute"://杆路
		for ( var i = 0; i < poleLineArray.length; i++) {
			for ( var i = 0; i < poleLineArray.length; i++) {
				if (poleLineArray[i].parentid == id) {
					curLineArray[lineIndex] = new Array();
					curLineArray[lineIndex][0] = "PolelineSection";
					curLineArray[lineIndex][1] = poleLineArray[i];
					lineIndex++;
				}
			}
		}
		break;
	case "FiberSection"://光缆段
		if (fiberCoreArray.length == 0) {
			for ( var i = 0; i < fiberSectionArray.length; i++) {
				if (fiberSectionArray[i].id == id) {
					curLineArray[lineIndex] = new Array();
					curLineArray[lineIndex][0] = "FiberSection";
					curLineArray[lineIndex][1] = fiberSectionArray[i];
					lineIndex++;
				}
			}
		} else {
			for ( var i = 0; i < fiberCoreArray.length; i++) {
				if (fiberCoreArray[i].parentid == id) {
					curLineArray[lineIndex] = new Array();
					curLineArray[lineIndex][0] = "FiberCore";
					curLineArray[lineIndex][1] = fiberCoreArray[i];
					lineIndex++;
				}
			}
			if (lineIndex == 0) {
				for ( var i = 0; i < fiberSectionArray.length; i++) {
					if (fiberSectionArray[i].id == id) {
						curLineArray[lineIndex] = new Array();
						curLineArray[lineIndex][0] = "FiberSection";
						curLineArray[lineIndex][1] = fiberSectionArray[i];
						lineIndex++;
					}
				}
			}
		}

		break;
	case "Fiber"://光缆
		for ( var i = 0; i < fiberSectionArray.length; i++) {
			if (fiberSectionArray[i].parentid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "FiberSection";
				curLineArray[lineIndex][1] = poleLineArray[i];
				lineIndex++;
			}
		}
		break;
	case "FiberCore"://纤芯
		for ( var i = 0; i < fiberCoreArray.length; i++) {
			if (fiberCoreArray[i].id == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "FiberCore";
				curLineArray[lineIndex][1] = fiberCoreArray[i];
				lineIndex++;
			}
		}
		break;
	case "OpticalRouteSP2SP"://局向光纤
		for ( var i = 0; i < opticalRouteSP2SPArray.length; i++) {
			if (opticalRouteSP2SPArray[i].id == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "OpticalRouteSP2SP";
				curLineArray[lineIndex][1] = opticalRouteSP2SPArray[i];
				lineIndex++;
			}
		}
		break;
	case "OpticalRouteP2P"://光路纤芯
		for ( var i = 0; i < opticalRouteSP2SPArray.length; i++) {
			if (opticalRouteSP2SPArray[i].parentid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "OpticalRouteSP2SP";
				curLineArray[lineIndex][1] = opticalRouteSP2SPArray[i];
				lineIndex++;
			}
		}
		break;
	case "OpticalRoute"://光路
		for ( var i = 0; i < opticalRouteSP2SPArray.length; i++) {
			if (opticalRouteSP2SPArray[i].grandid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "OpticalRouteSP2SP";
				curLineArray[lineIndex][1] = opticalRouteSP2SPArray[i];
				lineIndex++;
			}
		}
		break;
	case "TransmissionSection"://传输段
		for ( var i = 0; i < transmissionSectionArray.length; i++) {
			if (transmissionSectionArray[i].id == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "TransmissionSection";
				curLineArray[lineIndex][1] = transmissionSectionArray[i];
				lineIndex++;
			}
		}
		break;
	case "TransmissionNetwork"://传输系统
		for ( var i = 0; i < transmissionSectionArray.length; i++) {
			if (transmissionSectionArray[i].parentid == id) {
				curLineArray[lineIndex] = new Array();
				curLineArray[lineIndex][0] = "TransmissionSection";
				curLineArray[lineIndex][1] = transmissionSectionArray[i];
				lineIndex++;
			}
		}
		break;
	}
	for ( var i = 0; i < curLineArray.length; i++) {
		curLineArray[i][1].setOptions( {
			"strokeColor" : "red",
			"strokeWeight" : 4
		});
	}
	if (curElement != null) {
		if (infowindow != null) {
			infowindow.close();
			infowindow = null;
		}
		infowindow = new IInfoWindow("", {
			"position" : curElement.getPosition()
		});
		var aTitle = createInfoWindow( curElement );
		infowindow.setContent(aTitle);
		//marker.openInfoWindow(infowindow);
		//infowindow.open(map);
		openInfoWindow( curElement , infowindow);
	}
}

//绑定checkbox事件
function markerDisplayCtrl(resType) {
	clearResourceText();
	switch (resType) {
	case "GeneralBaseStation":
		if ($("#cb_generalBaseStation").attr("class") == "ok-y") {
			for ( var i = 0; i < baseStationArray.length ; i++) {
				baseStationArray[i].setMap(null);
			}
			$("#cb_generalBaseStation").attr("class", "");
		} else {
			$("#cb_generalBaseStation").attr("class", "ok-y");
			if ( baseStationArray.length == 0 ) {
				addLoadMarkerResource("BaseStation");
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push("BaseStation");
				showMarkerResource( "BaseStation" ,false);
			}
		}
		break;
	case "Station":
		if ($("#cb_station").attr("class") == "ok-y") {
			for ( var i = 0; i < stationArray.length; i++) {
				stationArray[i].setMap(null);
			}
			$("#cb_station").attr("class", "");
		} else {
			$("#cb_station").attr("class", "ok-y");
			if ( stationArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "ManWell":
		if ($("#cb_manWell").attr("class") == "ok-y") {
			for ( var i = 0; i < manWellArray.length; i++) {
				manWellArray[i].setMap(null);
			}
			$("#cb_manWell").attr("class", "");
		} else {
			$("#cb_manWell").attr("class", "ok-y");
			if ( manWellArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "Pole":
		if ($("#cb_pole").attr("class") == "ok-y") {
			for ( var i = 0; i < poleArray.length; i++) {
				poleArray[i].setMap(null);
			}
			$("#cb_pole").attr("class", "");
		} else {
			$("#cb_pole").attr("class", "ok-y");
			if ( poleArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "ODF":
		if ($("#cb_ODF").attr("class") == "ok-y") {
			for ( var i = 0; i < odfArray.length; i++) {
				odfArray[i].setMap(null);
			}
			$("#cb_ODF").attr("class", "");
		} else {
			$("#cb_ODF").attr("class", "ok-y");
			if ( odfArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "HangWall":
		
		if ($("#cb_hangWall").attr("class") == "ok-y") {
			for ( var i = 0; i < hangWallArray.length; i++) {
				hangWallArray[i].setMap(null);
			}
			$("#cb_hangWall").attr("class", "");
		} else {
			$("#cb_hangWall").attr("class", "ok-y");
			if ( hangWallArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "MarkPost":
		if ($("#cb_markPost").attr("class") == "ok-y") {
			for ( var i = 0; i < markPostArray.length; i++) {
				markPostArray[i].setMap(null);
			}
			$("#cb_markPost").attr("class", "");
		} else {
			$("#cb_markPost").attr("class", "ok-y");
			if ( markPostArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "FiberCrossCabinet":
		if ($("#cb_fiberCrossCabinet").attr("class") == "ok-y") {
			for ( var i = 0; i < fiberCrossCabinetArray.length; i++) {
				fiberCrossCabinetArray[i].setMap(null);
			}
			$("#cb_fiberCrossCabinet").attr("class", "");
		} else {
			$("#cb_fiberCrossCabinet").attr("class", "ok-y");
			if ( fiberCrossCabinetArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "FiberDistributionCabinet":
		if ($("#cb_fiberDistributionCabinet").attr("class") == "ok-y") {
			for ( var i = 0; i < fiberDistributionCabinetArray.length; i++) {
				fiberDistributionCabinetArray[i].setMap(null);
			}
			$("#cb_fiberDistributionCabinet").attr("class", "");
		} else {
			$("#cb_fiberDistributionCabinet").attr("class", "ok-y");
			if ( fiberDistributionCabinetArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource( resType ,false);
			}
		}
		break;
	case "FiberTerminalCase":
		if ($("#cb_fiberTerminalCase").attr("class") == "ok-y") {
			for ( var i = 0; i < fiberTerminalCaseArray.length; i++) {
				fiberTerminalCaseArray[i].setMap(null);
			}
			$("#cb_fiberTerminalCase").attr("class", "");
		} else {
			$("#cb_fiberTerminalCase").attr("class", "ok-y");
			if ( fiberTerminalCaseArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showMarkerResource();
			}
		}
		break;
	case "Joint":
		if ($("#cb_joint").attr("class") == "ok-y") {
			for ( var i = 0; i < jointArray.length; i++) {
				jointArray[i].setMap(null);
			}
			$("#cb_joint").attr("class", "");
		} else {
			$("#cb_joint").attr("class", "ok-y");
			if ( jointArray.length == 0 ) {
				addLoadMarkerResource(resType);
				if (!isLoadingMarker){
					loadMarkerResource();
				}
			} else {
				showingMarker.push(resType);
				showHideResourceMarker( resType ,false);
			}
		}
		break;
	}
	
	clearResourceText();
	addResourceText();
}

//清除所有资源
function clearAllResource(){
	//清除地图上的站址
	clearStation();
	//清除基站
	clearBaseStation();
	//清除地图上的人井
	clearManWell();
	//清除地图上的所有标桩
	clearMarkPost();
	//清除地图上的所有电杆
	clearPole();
	//清除地图上的所有挂墙点
	clearHangWall();
	//清除地图上的所有ODF
	clearOdf();
	//清除地图上的所有光交接箱
	clearFiberCrossCabinet();
	//清除地图上的所有光分纤箱
	clearFiberDistributionCabinet();
	//清除地图上的所有终端盒
	clearFiberTerminalCase();
	//清除地图上的所有接头
	clearJoint();
}

//清除地图上的基站
function clearBaseStation( ) {
	baseStationArray = marker_list["BaseStation"];
	if ( baseStationArray.length != null && baseStationArray.length > 0) {
		for ( var i = 0; i < baseStationArray.length; i++) {
			baseStationArray[i].setMap(null);
		}
		baseStationArray = new Array();
	}
}

//清除地图上的站址
function clearStation() {
	if (stationArray.length > 0) {
		for ( var i = 0; i < stationArray.length; i++) {
			stationArray[i].setMap(null);
		}
		stationArray = new Array();
	}
}


//清除地图上的人井
function clearManWell() {
	if (manWellArray.length > 0) {
		for ( var i = 0; i < manWellArray.length; i++) {
			manWellArray[i].setMap(null);
		}
		manWellArray = new Array();
	}
}

//清除地图上的所有标桩
function clearMarkPost() {
	if (markPostArray.length > 0) {
		for ( var i = 0; i < markPostArray.length; i++) {
			markPostArray[i].setMap(null);
		}
		markPostArray = new Array();
	}
}

//清除地图上的所有电杆
function clearPole() {
	if (poleArray.length > 0) {
		for ( var i = 0; i < poleArray.length; i++) {
			poleArray[i].setMap(null);
		}
		poleArray = new Array();
	}
}

//清除地图上的所有挂墙点
function clearHangWall() {
	if (hangWallArray.length > 0) {
		for ( var i = 0; i < hangWallArray.length; i++) {
			hangWallArray[i].setMap(null);
		}
		hangWallArray = new Array();
	}
}

//清除地图上的所有ODF
function clearOdf() {
	if (odfArray.length > 0) {
		for ( var i = 0; i < odfArray.length; i++) {
			odfArray[i].setMap(null);
		}
		odfArray = new Array();
	}
}

//清除地图上的所有光交接箱
function clearFiberCrossCabinet() {
	if (fiberCrossCabinetArray.length > 0) {
		for ( var i = 0; i < fiberCrossCabinetArray.length; i++) {
			fiberCrossCabinetArray[i].setMap(null);
		}
		fiberCrossCabinetArray = new Array();
	}
}

//清除地图上的所有光分纤箱
function clearFiberDistributionCabinet() {
	if (fiberDistributionCabinetArray.length > 0) {
		for ( var i = 0; i < fiberDistributionCabinetArray.length; i++) {
			fiberDistributionCabinetArray[i].setMap(null);
		}
		fiberDistributionCabinetArray = new Array();
	}
}

//清除地图上的所有终端盒
function clearFiberTerminalCase() {
	if (fiberTerminalCaseArray.length > 0) {
		for ( var i = 0; i < fiberTerminalCaseArray.length; i++) {
			fiberTerminalCaseArray[i].setMap(null);
		}
		fiberTerminalCaseArray = new Array();
	}
}

//清除地图上的所有接头
function clearJoint() {
	if (jointArray.length > 0) {
		for ( var i = 0; i < jointArray.length; i++) {
			jointArray[i].setMap(null);
		}
		jointArray = new Array();
	}
}


//清除地图上的所有管道段
function clearPipeLine() {
	if (pipeLineArray.length > 0) {
		for ( var i = 0; i < pipeLineArray.length; i++) {
			pipeLineArray[i].setMap(null);
		}
		pipeLineArray = new Array();
	}
}

//清除地图上的所有吊线段
function clearPoleLine() {
	if (poleLineArray.length > 0) {
		for ( var i = 0; i < poleLineArray.length; i++) {
			poleLineArray[i].setMap(null);
		}
		poleLineArray = new Array();
	}
}

//清除地图上的所有直埋段
function clearBuriedLine() {
	if (buriedLineArray.length > 0) {
		for ( var i = 0; i < buriedLineArray.length; i++) {
			buriedLineArray[i].setMap(null);
		}
		buriedLineArray = new Array();
	}
}

//清除地图上的所有光缆段
function clearFiberSection() {
	if (fiberSectionArray.length > 0) {
		for ( var i = 0; i < fiberSectionArray.length; i++) {
			fiberSectionArray[i].setMap(null);
		}
		fiberSectionArray = new Array();
	}
}
//清除地图上的所有纤芯
function clearFiberCore() {
	for ( var i = 0; i < fiberCoreArray.length; i++) {
		fiberCoreArray[i].setMap(null);
	}
	fiberCoreArray = new Array();
}
//清除地图上的所有局向光纤
function clearOpticalRouteSP2SP() {
	for ( var i = 0; i < opticalRouteSP2SPArray.length; i++) {
		opticalRouteSP2SPArray[i].setMap(null);
	}
	opticalRouteSP2SPArray = new Array();
}
//清除地图上的所有传输段
function clearTransmissionSection() {
	for ( var i = 0; i < transmissionSectionArray.length; i++) {
		transmissionSectionArray[i].setMap(null);
	}
	transmissionSectionArray = new Array();
}

//清除所有逻辑网资源
function clearAllLines () {
	//清除地图上的所有管道段
	clearPipeLine();
	//清除地图上的所有吊线段
	clearPoleLine();
	//清除地图上的所有直埋段
	clearBuriedLine();
	//清除地图上的所有传输段
	clearTransmissionSection();
	//清除地图上的所有局向光纤
	clearOpticalRouteSP2SP();
	//清除地图上的所有纤芯
	clearFiberCore();
	//清除地图上的所有光缆段
	clearFiberSection();
}

//清除地图上的所有标记
function clearAll() {
	//清除地图上的站址
	clearStation();
	//清除地图上的人井
	clearStation();
	//清除地图上的所有标桩
	clearMarkPost();
	//清除地图上的所有电杆
	clearPole();
	//清除地图上的所有挂墙点
	clearHangWall();
	//清除地图上的所有ODF
	clearOdf();
	//清除地图上的所有光交接箱
	clearFiberCrossCabinet();
	//清除地图上的所有光分纤箱
	clearFiberDistributionCabinet();
	//清除地图上的所有终端盒
	clearFiberTerminalCase();
	//清除地图上的所有接头
	clearJoint();
	//清除地图上的所有管道段
	clearPipeLine();
	//清除地图上的所有吊线段
	clearPoleLine();
	//清除地图上的所有直埋段
	clearBuriedLine();
}

//获取线的颜色
function getLineColor(resType) {
	switch (resType) {
	case "PipelineSection":
		return "#0071c1";
	case "PolelineSection":
		return "#00b24e"
	case "BuriedlineSection":
		return "#99a1cf";
	case "FiberSection":
		return "#92d343";
	case "FiberCore":
		return "#fbf638";
	case "OpticalRouteSP2SP":
		//		if(curView=="TransmissionView")
		//			return"#9c0e0d";
		return "#fcc300";
	case "OpticalRouteP2P":
		return "#9c0e0d";
	case "TransmissionSection":
		return "#9c0e0d";
	}
}


var mouseIcon = {
		"station" : "icon/zhanzhi.png" , 
		"manWell" : "icon/well.png" , 
		"pole" : "icon/pole.png" , 
		"hangWall" : "icon/hangWall.png" , 
		"markPost" : "icon/labelStone.png" , 
		"fiberCrossCabinet" : "icon/gjjx.png" , 
		"fiberDistributionCabinet" : "icon/fiberTerminalCase.png" , 
		"fiberTerminalCase" : "icon/gfxh.png" , 
		"generalBaseStation" : "icon/jizhan.png" 
};

var markerClick = null;
var mapMove = null;
var addMarker = null;


/**
 * 鼠标添加资源
 * @param {Object} opt 参数对象
 */
function addResourceModel( opt ) {
	addMarker = IMapEvent.addListener(map, "click", function () {
		var rType = opt.resourceType;
		var addReInco = mouseIcon[rType];  
		//var ltg = $event.latLng;
		var newMarker = new IMarker(mousePosition,"新增资源",{"icon":addReInco});
		newMarker.setMap(map);
		var lon = newMarker.getPosition().getLongitude();
		var lat = newMarker.getPosition().getLatitude();
		IMapEvent.removeListener(addMarker);
		var url = "../physicalres/loadAddPhysicalresPageAction?" + 
						"addedResEntityType=" + opt.addedResEntityType + 
						"&addedResParentEntityType=" + opt.addedResParentEntityType  + 
						"&addedResParentEntityId=" + opt.addedResParentEntityId  + 
						"&areaId=" + opt.areaId  + 
						"&longitude=" + lon + "&latitude="+lat;
		IMapEvent.addListener(newMarker, "click", function ($event) {
			var win = window.open(url);
			newMarker.setMap(null);
			newMarker = null;
		});
	});
}



/************************* 显示 隐藏 *****************************/

var loadingStep = 5;
var goonShowHide = true;

function showHideRes( type , len , isHide ) {
	var resArray = marker_list[type];
	if ( !goonShowHide ) {
		for ( var i = 0 ; resArray[i] && i < resArray.length; i++) {
			try {
				var m = resArray[i];
				m.setMap(null);
			} catch ( e ) {
				console.log(e);
			} 
		}
		return;
	}
	var nextLen = len + parseInt(loadingStep);
	var li = $(".ok-y[name='" + type + "']");
	if ( $(li).length == 0 ) {
		for ( var i = 0 ; resArray[i] && i < resArray.length; i++) {
			try {
				var m = resArray[i];
				m.setMap(null);
			} catch ( e ) {
				console.log(e);
			} 
		}
		loadMarkerResource();
		return;
	}
	if ( resArray.length != null && resArray.length > 0 && resArray.length > len ) {
		for ( var i = len ; resArray[i] && i < nextLen; i++) {
			try {
				var m = resArray[i];
				if ( isHide ) {
					m.setMap(null);
				} else {
					m.setMap(map);
				}
			} catch ( e ) {
				console.log(e);
			} 
		}
		setTimeout("showHideRes( '" + type + "' , " + nextLen + " , " + isHide + " )" , 50 );
	} else {
		loadMarkerResource();
	}
}


function showHideResourceMarker ( type , isHide ) {
	if ( type == "GeneralBaseStation") {
		type = "BaseStation";
	}
	showHideRes(type , 0 , isHide);
}

function showAllResourceMarker() {
	var isHide = false;
	for ( var key in marker_list ) {
		showHideResourceMarker( key , isHide);
	}
}      
function hideAllResourceMarker() {
	var isHide = true;
	for ( var key in marker_list ) {
		showHideResourceMarker( key , isHide);
	}
}      

/************************ 工具 begin *************************/

function threadSleep ( milsecond ) {
	var startTime = new Date().getTime();
	
	while ( 1==1 ) {
		var endTime  = new Date().getTime();
		var totalTime = endTime - startTime;
		if ( totalTime >= milsecond ) {
			break;
		}
	}
}


/************************ 工具 end *************************/
