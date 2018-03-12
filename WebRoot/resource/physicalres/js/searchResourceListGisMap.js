
var map_resource_list = null;		//地图元素信息集合
var map = null;						//gis地图对象
var default_center = null;			//默认地图中心
var default_mapZoom = 13;
var map_resouce_marker = new Array();	//地图marker集合
var marker_icon_list = {
		"Station" : "icon/zhanzhi.png" , 
		"ManWell" : "icon/well.png" , 
		"Pole" : "icon/pole.png" , 
		"HangWall" : "icon/hangWall.png" , 
		"MarkPost" : "icon/labelStone.png" , 
		"FiberCrossCabinet" : "icon/gjjx.png" , 
		"FiberDistributionCabinet" : "icon/fiberTerminalCase.png" , 
		"FiberTerminalCase" : "icon/gfxh.png" , 
		"GeneralBaseStation" : "icon/jizhan.png" , 
		"BaseStation_TD" : "icon/jizhan.png" , 
		"BaseStation_GSM" : "icon/jizhan.png" , 
		"BaseStation_repeater" : "icon/jizhan.png" , 
		"BaseStation_WLAN" : "icon/jizhan.png" , 
		"Terminal" : "icon/odf.png" , 
		"ODF" : "icon/odf.png" 
};				//marker图标
var infowindow = null;			//marker信息窗
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
};			//marker英对中

var map_line_list = null;




//设置地图元素集合
function setMapResourceList( list ) {
	map_resource_list = list;
	createMarker(list);
}

function setMapCenterByResourceId ( resource_id ) {
	if ( !map_resouce_marker || map_resouce_marker == null ) {
		return;
	}
	for ( var i = 0 ; i < map_resouce_marker.length ; i++ ) {
		var marker = map_resouce_marker[i];
		var info = marker._data;
		if ( info.id == resource_id ) {
			var pos = marker.getPosition();
			map.setCenter(pos);
			if (infowindow != null) {
				infowindow.close();
				infowindow = null;
			}
			infowindow = new IInfoWindow("", {
				"position" : marker.getPosition()
			});
			infowindow.setContent("");
			var aTitle = createInfoWindow( marker );
			infowindow.setContent(aTitle);
			marker.openInfoWindow(infowindow);
		}
	}
}



//初始化地图
function initMap () {
	default_center = new ILatLng(23.14651977, 113.34106554);
	map = new IMap("gis_map", default_center, default_mapZoom, {});
}

$(document).ready(function () {
	initMap();
	bindResourceLineEvent();
});

//绑定逻辑网资源按钮事件
function bindResourceLineEvent () {
	$("#Pipe").click(function(){
		getPipeRouteInfo();
	});
	$("#Fiber").click(function(){
		getFiberInfo();
	});
	$("#FiberCore").click(function(){
		getFiberCoreInfo();
	});
	$("#OpticalRoute").click(function(){
		getOpticalRouteInfo();
	});
	$("#Transmission").click(function(){
		getTransmissionInfo();
	});
}













/** TODO 网络资源 begin***************************************************/

//创建marker
function createMarker ( res_list ) {
	//清除所有地图marker
	hideAllMarker();
	
	//清空【地图marker集合】
	map_resouce_marker = new Array();
	
	if ( !res_list || res_list == null ) {
		return;
	}
	//循环【地图元素信息集合】
	for( var i = 0 ; i < res_list.length ; i++ ) {
		var info = res_list[i];
		var lng = info.longitude;
		var lat = info.latitude;
		if ( lng == null || lng == "" || lat == null || lat == ""  ) {
			continue;
		}
		//创建marker
		var latlng = new ILatLng(lat, lng);
		var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
		var marker = new IMarker(mapLatLng, info.name, {});
		var icon = marker_icon_list[info._entityType];
		marker.setOptions({"icon":icon});
		//设置marker的title
		if (info.name) {
			marker.title = info.name;
		}
		else {
			marker.title = info.label;
		}
		
		marker.id = info.id;
		marker.type_ = info._entityType;
		//保存元素信息到marker
		marker._data = info;
		if ( merkerName[info._entityType] ) {
			marker.chinese_type_ = merkerName[info._entityType];
		} else {
			marker.chinese_type_ = "未知";
		}
		
		//marker添加click事件
		bindMarkerEvent(marker);
		
		
		//把marker添加到【地图marker集合】
		map_resouce_marker.push(marker);
	}
	
	//调用显示【地图marker集合】
	showAllMarker();
}

function bindMarkerEvent ( marker ) {
	IMapEvent.addListener(marker, "click", function() {
		if (infowindow != null) {
			infowindow.close();
			infowindow = null;
		}
		infowindow = new IInfoWindow("", {
			"position" : marker.getPosition()
		});
		infowindow.setContent("");
	
		var aTitle = createInfoWindow( marker );
		infowindow.setContent(aTitle);
		marker.openInfoWindow(infowindow);
	});
}



//创建信息窗体
function createInfoWindow ( marker ) {
	var markerPoint = marker.getPosition();
	var data = marker._data;//获取数据
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
	var detail_param = {
		"currentEntityType" : marker.type_ , 
		"currentEntityId" : marker.id , 
		"areaId" : $("#saveAreaId").val() , 
		"modelType" : "view"
	}; 
	detailink = createUrl("getPhysicalresForOperaAction",detail_param);
	var edit_param = {
		"currentEntityType" : marker.type_ , 
		"currentEntityId" : marker.id , 
		"parentEntityType" : undefined , 
		"parentEntityId" : undefined , 
		"areaId" : $("#saveAreaId").val()
	}; 
	editlink = createUrl("getPhysicalresForOperaAction",edit_param); 
	
	//获取资源工单/任务单
	var workorder_list = "";
	var workorder_param = { "resourceType" : marker.type_ , "resourceId" : marker.id , "orderType" : "workOrder" };
	$.ajax({
		"url" : "getWorkOrdersByResourceIdByStatusAction" , 
		"type" : "post" , 
		"async" : false , 
		"data" : workorder_param , 
		"success" : function(result) {
			var data = eval(result);
			for( var i = 0 ; i < data.length ; i++ ) {
				var info = data[i];
				var woId = info.WOID;
				var status = info.STATUSNAME;
				var wotitle = info.WOTITLE;
				
				var a = "";
				if ( woId != undefined && woId != null && woId != "" ) {
					a = "<em style='color:blue;'>" + woId + "</em>  ";
				}
				var li = "";
				if ( wotitle != undefined && wotitle != null && wotitle != "" ) {
					li = "<li><input type='radio'/>" + a + wotitle + status + "</li>";
				}
				workorder_list += li;
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
							
							gis_dialog += "<div class='dialog_img_empty_box'></div>";
		gis_dialog += 		"	<div class='dialog_info'> " +
							"		<div class='clearfix'> " + 
							"			<p class='dialog_info_tab'> " + 
							"				<span>工单/任务单</span> " + 
							"			</p> " + 
							"			<p class='dialog_info_other'> " + 
							"				<a href='javascript:void();' onclick='move2MapCenterAndZoomMax( " + markerPoint.getLongitude() + " , " + markerPoint.getLatitude() + " );'><em class='other_icon1'></em>周边环境</a> " + 
							"				<a href='" + editlink + "' target='_blank'><em class='other_icon2'></em>修改</a> " + 
							"			</p> " + 
							"		</div> " + 
							"		<div class='dialog_info_detail'> " + 
							"			<ul id='resource_workorder_taskorder'> " + 
											workorder_list + 
							"			</ul> " + 
							"		</div>" + 
							"	</div> " + 
							"</div>";
	return gis_dialog;
}




//把【地图marker集合】从地图中清除
function hideAllMarker () {
	if ( !map_resouce_marker || map_resouce_marker == null ) {
		return;
	}
	for ( var i = 0 ; i < map_resouce_marker.length ; i++ ) {
		var marker = map_resouce_marker[i];
		marker.setMap(null);
	}
}

//把【地图marker集合】添加到地图中
function showAllMarker () {
	if ( map == null ) {
		return;
	}
	if ( !map_resouce_marker || map_resouce_marker == null ) {
		return;
	}
	for ( var i = 0 ; i < map_resouce_marker.length ; i++ ) {
		var marker = map_resouce_marker[i];
		marker.setMap(map);
	}
}








/** 网络资源 end *******************************************/




/** TODO 逻辑资源 begin *******************************/

var line_temp_marker = null;


//获取管道的相关设施信息
function getPipeRouteInfo() {
	//clearAllLines();
	//还原线的属性，清除信息框，当前操作标记
	/*clearFiberSection();//清除光缆段
	clearFiberCore();//清除纤芯
	clearOpticalRouteSP2SP();//清除局向纤芯
	clearTransmissionSection();//清除传输段*/
	
	//清除地图逻辑网络
	if ( map_line_list && map_line_list != null ) {
		for ( var i = 0 ; i < map_line_list.length ; i++ ) {
			var line = map_line_list[i];
			line.setMap(null);
		}
	}
	
	if ( line_temp_marker != null ) {
		//清除所有地图marker
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			line_temp_marker[i].setMap(null);
		}
	}
	map_line_list = new Array();
	
	//清空【地图marker集合】
	line_temp_marker = new Array();
	
	var areaId = $("#areaId").val();
	
	var values = {
		"areaId" : areaId
	}
	
	
	$.ajax( {
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
						if ( station && station != null ) {
							createTempMarker(station);
						}
						//标记电杆
						if ( pole && pole != null ) {
							createTempMarker(pole);
						}
						//标记人井
						if ( manWell && manWell != null ) {
							createTempMarker(manWell);
						}
						//标记标桩
						if ( markPost && markPost != null ) {
							createTempMarker(markPost);
						}
						//标记挂墙点
						if ( hangWall && hangWall != null ) {
							createTempMarker(hangWall);
						}
						
						//生成管道段
						if (pipeLine && pipeLine != null) {
							var color = "#0071c1";
							createLine(pipeLine, color,
									"PipelineSection");
						}
						//生成直埋段
						if (buriedLine && buriedLine != null) {
							var color = "#99a1cf";
							createLine(buriedLine, color,
									"BuriedlineSection");
						}
						//生成吊线段
						if ( poleLine && poleLine != null ) {
							var color = "#00b24e";
							createLine(poleLine, color,
									"PolelineSection");
						}
					}
					
				}
			});
}

//获取光缆的相关设施信息
function getFiberInfo() {
	//clearAllLines();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除纤芯
	//clearOpticalRouteSP2SP();//清除局向纤芯
	//clearTransmissionSection();//清除传输段
	
	//清除地图逻辑网络
	if ( map_line_list && map_line_list != null ) {
		for ( var i = 0 ; i < map_line_list.length ; i++ ) {
			var line = map_line_list[i];
			line.setMap(null);
		}
	}
	
	if ( line_temp_marker && line_temp_marker != null ) {
		//清除所有地图marker
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			line_temp_marker[i].setMap(null);
		}
	}
	map_line_list = new Array();
	
	//清空【地图marker集合】
	line_temp_marker = new Array();
	var areaId = $("#areaId").val(); 
	
	var values = {
		"areaId" : areaId
	};
	
	$.ajax( {
		url : "../gis/getFiberInfoAction",
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
						var fiberSection = data.FiberSection;
						var fiber = data.Fiber;
						var odf = data.ODF;
				
						
						//标记站址
						if ( station && station != null ) {
							createTempMarker(station);
						}
						//标记电杆
						if ( pole && pole != null ) {
							createTempMarker(pole);
						}
						//标记人井
						if ( manWell && manWell != null ) {
							createTempMarker(manWell);
						}
				
						//标记光交接箱
						if ( fiberCrossCabinet && fiberCrossCabinet != null ) {
							createTempMarker(fiberCrossCabinet);
						}
						//标记光分纤箱
						if ( fiberDistributionCabinet && fiberDistributionCabinet != null ) {
							createTempMarker(fiberDistributionCabinet);
						}
						//标记终端盒
						if ( fiberTerminalCase && fiberTerminalCase != null ) {
							createTempMarker(fiberTerminalCase);
						}
				
						//标记ODF(站址的子)
						if (  odf && odf != null  ) {
							createTempMarker(odf);
						}
						//标记接头(人井，电杆的子)
						if (  joint && joint != null  ) {
							createTempMarker(joint);
						}
						//生成光缆段
						if (fiberSection && fiberSection != null) {
							createLine(fiberSection, "#92d343",
									"FiberSection");
						}
			}
		}
	});

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
	//clearAllLines();
	
	//清除地图逻辑网络
	if ( map_line_list && map_line_list != null ) {
		for ( var i = 0 ; i < map_line_list.length ; i++ ) {
			var line = map_line_list[i];
			line.setMap(null);
		}
	}
	
	if ( line_temp_marker != null ) {
		//清除所有地图marker
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			line_temp_marker[i].setMap(null);
		}
	}
	map_line_list = new Array();
	//清空【地图marker集合】
	line_temp_marker = new Array();
	
	opticalRouteView = true;
	var areaId = $("#areaId").val(); 

	var values = {
		area : "" , 
		"areaId" : areaId
	}
	
	if ( line_temp_marker != null ) {
		//清除所有地图marker
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			line_temp_marker[i].setMap(null);
		}
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
						var terminal = data.Terminal;

						//标记站址
						if ( station && station != null ) {
							createTempMarker(station);
						}
				
						//标记光交接箱
						if ( fiberCrossCabinet && fiberCrossCabinet != null ) {
							createTempMarker(fiberCrossCabinet);
						}
						//标记光分纤箱
						if ( fiberDistributionCabinet && fiberDistributionCabinet != null ) {
							createTempMarker(fiberDistributionCabinet);
						}
						//标记终端盒
						if ( fiberTerminalCase && fiberTerminalCase != null ) {
							createTempMarker(fiberTerminalCase);
						}
				
						//标记ODF(站址的子)
						if (  odf && odf != null  ) {
							createTempMarker(odf);
						}
						
						//标记ODF(站址的子)
						if (  terminal && terminal != null  ) {
							createTempMarker(terminal);
						}
						//生成局向纤芯
						if ( opticalRouteSP2SP && opticalRouteSP2SP != null ) {
							opticalRouteSP2SPArray = createLine(
									opticalRouteSP2SP, "#fcc300",
									"OpticalRouteSP2SP");
						}
					}
				}
			});
}

//获取传输拓扑的相关设施信息   
function getTransmissionInfo() {
	
	
	//清除地图逻辑网络
	if ( map_line_list && map_line_list != null ) {
		for ( var i = 0 ; i < map_line_list.length ; i++ ) {
			var line = map_line_list[i];
			line.setMap(null);
		}
	}
	
	if ( line_temp_marker != null ) {
		//清除所有地图marker
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			line_temp_marker[i].setMap(null);
		}
	}
	map_line_list = new Array();
	//清空【地图marker集合】
	line_temp_marker = new Array();
	return;
	
	
	
	//clearCurElements();
	/*clearPipeLine();//清除管道段
	clearPoleLine();//清除吊线段
	clearBuriedLine();//清除直埋段*/
	//clearFiberSection();//清除光缆段
	//clearFiberCore();//清除纤芯
	//clearOpticalRouteSP2SP();//清除局向纤芯
	//clearAllLines();
	curView = "TransmissionView";
	transmissionView = true;
	var areaId = $("#areaId").val(); 

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
			
//							alert("管道段"+lat+"=="+lng+"=="+facility[j].name);
			var latlng = new ILatLng(lat, lng);
			var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
			linePath.push(mapLatLng);
		}
		var polyline = new IPolyline(linePath, color, 3, {
			"map" : map
		});
		//		polyline.id = data[i].line.id;
		map_line_list.push(polyline);
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
		//showLogicalres(line.id, resType, "view");
	});
}


//还原线的属性，清除信息框，当前操作标记
function clearCurElements() {
	/*
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
	lineIndex = 0;*/

	if (infowindow != null) {
		infowindow.close();
		infowindow = null;
	}
	/*
	if (curLine != null) {
		curLine.setOptions( {
			"strokeColor" : getLineColor(curLineType),
			"strokeWeight" : 3
		});
	}
	*/
}



//TODO 创建逻辑资源的临时marker
function createTempMarker ( res_list ) {
	
	if ( !res_list || res_list == null ) {
		return;
	}
	//循环【地图元素信息集合】
	for( var i = 0 ; i < res_list.length ; i++ ) {
		var info = res_list[i];
		var lng = info.longitude;
		var lat = info.latitude;
		if ( lng == null || lng == "" || lat == null || lat == ""  ) {
			continue;
		}
		lat = parseFloat(lat);
		lng = parseFloat(lng);
		//创建marker
		var latlng = new ILatLng(lat, lng);
		var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
		var marker = new IMarker(mapLatLng, info.name, {});
		var icon = marker_icon_list[info._entityType];
		marker.setOptions({"icon":icon});
		//设置marker的title
		if (info.name) {
			marker.title = info.name;
		} else {
			marker.title = info.label;
		}
		
		marker.id = info.id;
		marker.type_ = info._entityType;
		//保存元素信息到marker
		marker._data = info;
		if ( merkerName[info._entityType] ) {
			marker.chinese_type_ = merkerName[info._entityType];
		} else {
			marker.chinese_type_ = "未知";
		}
		
		//marker添加click事件
		bindMarkerEvent(marker);
		
		
		//把marker添加到【地图marker集合】
		line_temp_marker.push(marker);
	}
	
	//调用显示【地图marker集合】
	if ( line_temp_marker && line_temp_marker != null) {
		for ( var i = 0 ; i < line_temp_marker.length ; i++ ) {
			var marker = line_temp_marker[i];
			marker.setMap(map);
		}
	}
}

/** 逻辑资源 end *******************************/


/** infowindow begin ************************************/


//移动地图中心点并把视角放大
function move2MapCenterAndZoomMax( lng , lat ) {
	
	//平移地图
	map.setZoom(21);
	var pos = new ILatLng(lat,lng);
	map.setCenter(pos);
	
}

/** infowindow end ************************************/



