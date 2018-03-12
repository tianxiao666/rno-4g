
var gisCellDisplayLib;// 负责显示小区的对象
var map;

var currentloadtoken = "";// 加载任务的token

var minZoom = 17;// 只有大于 该 缩放级别，才真正
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数

var onLoadingGisCell = false;// 是否正在加载小区信息

var celllabel = "";

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label

var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label
var targetCellColor = "#B40431";// 暗红色
var targetNcellColor = "#2EFE2E";// 耀眼的绿色
var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var mapGridLib //用于将地图分成网格加载小区的对象

var isShowCellName = false; //地图是否显示小区
var dynaPolygon_12,dynaPolygon_3; //动态覆盖图形
var dynaPolyline_12,dynaPolyline_3; //矢量线
var dynaArrow_12,dynaArrow_3;  //矢量线的箭头
var dynaCellColor = "#1C1C1C"; //当前动态覆盖图所属小区的颜色
var dynaPolygonColor_12 = "blue";
var dynaPolygonColor_3 = "red";
var dynaPolylineColor_12 = "#2EFE2E"; 
var dynaPolylineColor_3 = "red"; 

//地图小区右键菜单
var contextMenu = [
	{
		text:'动态覆盖图1',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='showDynaCoverage(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				showDynaCoverage(polygon);
			}
		}
	},
	{
		text:'动态覆盖图2',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='showDynaCoverage2(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				showDynaCoverage2(polygon);
			}
		}
	},
	{
		text:'IN干扰小区连线',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='InInterCellLine(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				InInterCellLine(polygon);
			}
		}
	},
	{
		text:'OUT干扰小区连线',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='OutInterCellLine(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				OutInterCellLine(polygon);
			}
		}
	}
];

$(document).ready(
		function() {
			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					randomShowCnt, null, {
					    'clickFunction' : clickFunction,
					    'mouseoverFunction' : mouseoverFunction,
						'mouseoutFunction' : mouseoutFunction
					}, {'showCellLabel' : false}, contextMenu, preZoom);
			var lng = $("#hiddenLng").val();
			var lat = $("#hiddenLat").val();
			map = gisCellDisplayLib.initMap("map_canvas", lng, lat,{
										'movestart' : handleMovestartAndZoomstart,
										'zoomstart' : handleMovestartAndZoomstart,
										'moveend' : handleMoveendAndZoomend,
										'zoomend' : handleMoveendAndZoomend });
			
			//以网格形式在地图加载小区
			mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
			//以城市为单位创建区域网格
			var cityName = $("#cityId").find("option:selected").text().trim();
			mapGridLib.createMapGrids(cityName);

			var areaId = $("#cityId").val();


			// 获取gis数据数据
			currentloadtoken = getLoadToken();

			bindNormalEvent();
			
			//区域联动
			initAreaCascade()

		
			
		$("#loadCellToMap").click(function(){
			$("#hiddenCurrentPage").val("1");
			clearAll(); //加载前清除地图上覆盖物
			//getGisCellByFreqType(currentloadtoken);

			//初始化网格状态
			mapGridLib.initMapGrids();
			//设置地图加载小区状态为true
			mapGridLib.setIsLoading(true);
			//设置当前屏幕经纬度范围
			var winMinLng = map.getBounds().getSouthWest().lng;
			var winMinLat = map.getBounds().getSouthWest().lat;
			var winMaxLng = map.getBounds().getNorthEast().lng;
			var winMaxLat = map.getBounds().getNorthEast().lat;
			mapGridLib.setWinLngLatRange(winMinLng,winMinLat,winMaxLng,winMaxLat);
			//设置当前加载标识
			currentloadtoken = getLoadToken();
			mapGridLib.setCurrentloadtoken(currentloadtoken);
			//加载小区
			var t = new Date().getTime();
			var gsmtype = $("input[name='freqType']:checked").val();
			var params = {
				'freqType' : gsmtype
			};
			//t-minResponseInterval保证不延迟加载
			mapGridLib.loadLteCell(t-minResponseInterval, currentloadtoken, minResponseInterval,params);
		});

//		$("#locatecell").click(function(){
//			gisCellDisplayLib.panTo(113.36079487718192,23.179828049350228);
//			var option_serverCell={
//					'fillColor':'#FF0000'
//			};
//			var cell="GBVDGZ3";
//			gisCellDisplayLib.changeCellPolygonOptions(cell,option_serverCell,false);	
//		});
	
		//控制地图小区名称是否显示
		$("#showCellName").click(function() {
			if(isShowCellName) {
				$("#showCellName").val("显示小区名字");
				isShowCellName = false;
				gisCellDisplayLib.hidePolygonsLabel();
			} else {
				$("#showCellName").val("关闭小区名字");
				isShowCellName = true;
				gisCellDisplayLib.showPolygonsLabel();
			}
		});
		/*$("#interDetailTab").chromatable({   
			width: "100%",   
			height: "100%",   
			scrolling: "yes"   
			}); */
		//jQuery.fn.CloneTableHeader("interDetailTab", "interDetailDiv");
}, false);

function bindNormalEvent() {

	//打开查找小区窗口
	$(".queryButton").click(function() {
		//$("#searchDiv").slideToggle();
		$("#searchDiv").toggle();
	});
	// 根据条件搜索小区
	$("#searchCellBtn").click(function() {
		searchCell();
	});
	// 搜索邻区
	$("#searchNcellBtn").click(function() {
		searchNcell();
	});
	// 搜频点
	$("#searchFreqBtn").click(function() {
		searchFreq();
	});
	// 清除搜索结果
	$("#clearSearchResultBtn").click(function() {
		clearSpecRendPolygons();
	});
	
	//清除地图上的动态覆盖图
	$("#clearCoverPolygon").click(function() {
		//清除动态覆盖图，方向线，箭头
		map.removeOverlay(dynaPolygon_12);
		map.removeOverlay(dynaPolygon_3);
		map.removeOverlay(dynaPolyline_12);
//		map.removeOverlay(dynaPolyline_3);
		map.removeOverlay(dynaArrow_12);
//		map.removeOverlay(dynaArrow_3);
		//清除额外覆盖物
		gisCellDisplayLib.clearOnlyExtraOverlay();
		//恢复默认polygon颜色
		gisCellDisplayLib.resetPolygonToDefaultOutlook();
	});
}


function initPageParam() {
	$("#hiddenPageSize").val(100);
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
}


/**
 * 区域联动事件
 */
function initAreaCascade() {
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});

	$("#cityId").change(
			function() {
				
				// stsResult = getRnoTrafficRenderer('pdinterference');
				getSubAreas("cityId", "areaId", "区/县", function(data) {
					$("#hiddenAreaLngLatDiv").html("");
					$("#hiddenLng").val("");
					$("#hiddenLat").val("");
					// console.log("data===" + data.toSource());
					if (data) {
						var html = "";
						for ( var i = 0; i < data.length; i++) {
							var one = data[i];
							html += "<input type=\"hidden\" id=\"areaid_"
									+ one['area_id'] + "\" value=\""
									+ one["longitude"] + "," + one["latitude"]
									+ "\" />";
							// console.log("lng="+one["longitude"]);
							// console.log("lat="+one["latitude"]);
							if (i == 0) {
								$("#hiddenLng").val(one["longitude"]);
								$("#hiddenLat").val(one["latitude"]);
							}
						}
						$("#hiddenAreaLngLatDiv").append(html);
					}
					//$("#areaId").trigger("change");
					$("#areaId").append("<option value=\"-1\">全部</option>");
				});
			});

	$("#areaId").change(function() {
		var lnglat = $("#areaid_" + $("#areaId").val()).val();
		if (lnglat) {
			// 地图中心点
			var lls = lnglat.split(",");
			if (lls[0] == 0 || lls[1] == 0) {
				// console.warn("未设置该区域的中心点经纬度。");
			} else {
				// 地图移动
				gisCellDisplayLib.panTo(lls[0], lls[1]);
			}
		}

		//以城市为单位创建区域网格
		clearAll(); //清除缓存
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
	});

}

// 重置查询表单的值
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

}

function clickFunction(){
	
}

function clearInterDetails() {

//	//清空小区频点干扰详情
//	$("#freqintersituation tr:gt(0)").remove();
//	//清空频点对应的邻区干扰详情
//	$("#detailedintersituation tr:gt(0)").remove();

	//恢复地图默认图元外观
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
}

/**
 * 查看小区动态覆盖图(曲线)
 */
function showDynaCoverage(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	$("#interDetailTab tr:gt(0)").remove(); 
	//$("#interDetailTab tr").not("tr:first").remove();
	//清空界面数据
	clearAll();

	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	//获取城市id
	var cityId = $("#cityId").val();
	//获取日期范围
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	
	//获取图形大小系数
//	var imgCoeff = $("#imgCoeff").val();
//	//console.log(Number(imgCoeff));
//	var valiNumber = /^[+]?[0-9]+(\.[0-9]+)?$/;   //验证数字
//	if(!valiNumber.test(Number(imgCoeff))) {
//		alert("图形大小系数请输入数字且大于0！");
//		return;
//	}
	//获取图形大小系数
	var imgSizeCoeff = $("#imgSizeCoeff").val();
	//console.log(Number(imgSizeCoeff));
	var valiNumber = /^[+]?[0-9]+(\.[0-9]+)?$/;   //验证数字
	if(!valiNumber.test(Number(imgSizeCoeff))) {
		alert("折线图形大小系数请输入数字且值大于0.001小于1000！");
		return;
	}
	if(Number(imgSizeCoeff) <= 0.001) {
		alert("折线图形大小系数请输入数字且值大于0.001小于10000！");
		return;
	}
	if(Number(imgSizeCoeff) >= 10000) {
		alert("折线图形大小系数请输入数字且值大于0.001小于10000！");
		return;
	}
	var celllng,celllat;
	if(lng) {
		celllng = lng;
	} else {
		celllng = polygon._data.getLng();
	}
	if(lat) {
		celllat = lat;
	} else {
		celllat = polygon._data.getLat();
	}	
	
	showOperTips("loadingDataDiv", "loadContentId", "正在生成动态覆盖图");

	$.ajax({
		url : 'get4GDynaCoverageDataForAction',
		data : {
			'cityId' : cityId,
			'lteCellId' : celllabel,
			'startDate' : sDate,
			'endDate' : eDate,
			'imgSizeCoeff':imgSizeCoeff
			//'imgCoeff' : imgCoeff
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			
			if(data != null) {
				
				var curvePoints_12 = data['curvePoints_12'];
				var curvePoints_3 = data['curvePoints_3'];
				var resInterDetail = data['resInterDetail'];
				
				if(curvePoints_12 != null && curvePoints_3 != null) {
					var vectorPoints_12 = data['vectorPoint_12'];
					var vecLng_12 = vectorPoints_12[0]['lng'];
					var vecLat_12 = vectorPoints_12[0]['lat'];
					
					var vectorPoints_3 = data['vectorPoint_3'];
					var vecLng_3 = vectorPoints_3[0]['lng'];
					var vecLat_3 = vectorPoints_3[0]['lat'];
					
					//console.log(points);
					var pointArray_12 = new Array();
					for ( var i = 0; i < curvePoints_12.length; i++) {
						var lng = curvePoints_12[i]["lng"];
						var lat = curvePoints_12[i]["lat"];
						pointArray_12.push(new BMap.Point(lng, lat));
					}
					var pointArray_3 = new Array();
					for ( var i = 0; i < curvePoints_3.length; i++) {
						var lng = curvePoints_3[i]["lng"];
						var lat = curvePoints_3[i]["lat"];
						pointArray_3.push(new BMap.Point(lng, lat));
					}
					for ( var i = 0; i < resInterDetail.length; i++) {
						var cellId = resInterDetail[i]["CELL_ID"];
						var ncellId = resInterDetail[i]["NCELL_ID"];
						var cosi1 = resInterDetail[i]["VAL1"];
						var cosi2 = resInterDetail[i]["VAL2"];
						var rsr0 = resInterDetail[i]["RSRPTIMES0"];
						var rsr1 = resInterDetail[i]["RSRPTIMES1"];
						var dis = resInterDetail[i]["DISTANCE"];
						$("#interDetailTab").append(
								"<tr><td>" + cellId + "</td><td>" + ncellId
										+ "</td><td>" + toConverVal(cosi1)
										+ "</td><td>" + toConverVal(cosi2)
										+ "</td><td>" + rsr0 + "</td><td>"
										+ rsr1 + "</td><td>" + toConverVal(dis)
										+ "</td></tr>");
					}
					new TableSorter("interDetailTab"); //为table加上点击排序
//					$("#interDetailTab")
					//暂不填充颜色
					//添加动态覆盖图
					dynaPolygon_12 = new BMap.Polygon(pointArray_12, 
			        		{strokeColor:dynaPolygonColor_12,fillColor:dynaPolygonColor_12, strokeWeight:3, strokeOpacity:0.1,fillOpacity:0.3,});
					dynaPolygon_3 = new BMap.Polygon(pointArray_3, 
			        		{strokeColor:dynaPolygonColor_3,fillColor:dynaPolygonColor_3, strokeWeight:3, strokeOpacity:0.1,fillOpacity:0.3,}); 
			        map.addOverlay(dynaPolygon_12);
			        map.addOverlay(dynaPolygon_3);
			        //添加方向线
			        dynaPolyline_12 = new BMap.Polyline(
			        					[new BMap.Point(celllng, celllat),new BMap.Point(vecLng_12, vecLat_12)],
			        					{strokeColor:dynaPolylineColor_12, strokeWeight:3, strokeOpacity:0.5}); 
//			        dynaPolyline_3 = new BMap.Polyline(
//        					[new BMap.Point(celllng, celllat),new BMap.Point(vecLng_3, vecLat_3)],
//        					{strokeColor:dynaPolylineColor_3, strokeWeight:3, strokeOpacity:0.5});  
					map.addOverlay(dynaPolyline_12);
//					map.addOverlay(dynaPolyline_3);
					//添加方向线箭头
					addArrow(dynaPolyline_12,20,Math.PI/7,"1");
//					addArrow(dynaPolyline_3,20,Math.PI/7,"2");
					//为目标小区填充颜色
					gisCellDisplayLib.changeCellPolygonOptions(celllabel, {'fillColor':dynaCellColor,'fillOpacity':1}, true);
				} else {
					hideOperTips("loadingDataDiv");
					alert("该小区在搜索的时间段内没有数据！");
				}
			} else {
				hideOperTips("loadingDataDiv");
				alert("该小区在搜索的时间段内没有数据！");
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
 * 查看小区动态覆盖图(折线)
 */
function showDynaCoverage2(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	$("#interDetailTab tr:gt(0)").remove(); 
	//清空界面数据
	clearAll();

	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	//获取城市id
	var cityId = $("#cityId").val();
	//获取日期范围
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	
	//获取图形大小系数
	var imgCoeff = $("#imgCoeff").val();
	//console.log(Number(imgCoeff));
	var valiNumber = /^[+]?[0-9]+(\.[0-9]+)?$/;   //验证数字
	if(!valiNumber.test(Number(imgCoeff))) {
		alert("折线图系数请输入数字且值大于0小于0.5！");
		return;
	}
	if(Number(imgCoeff) <= 0) {
		alert("折线图系数请输入数字且值大于0小于0.5！");
		return;
	}
	if(Number(imgCoeff) >= 0.5) {
		alert("折线图系数请输入数字且值大于0小于0.5！");
		return;
	}
	
	//获取图形大小系数
	var imgSizeCoeff = $("#imgSizeCoeff").val();
	//console.log(Number(imgSizeCoeff));
	var valiNumber = /^[+]?[0-9]+(\.[0-9]+)?$/;   //验证数字
	if(!valiNumber.test(Number(imgSizeCoeff))) {
		alert("折线图形大小系数请输入数字且值大于0.001小于10000！");
		return;
	}
	if(Number(imgSizeCoeff) <= 0.001) {
		alert("折线图形大小系数请输入数字且值大于0.001小于10000！");
		return;
	}
	if(Number(imgSizeCoeff) >= 10000) {
		alert("折线图形大小系数请输入数字且值大于0.001小于10000！");
		return;
	}
	var celllng,celllat;
	if(lng) {
		celllng = lng;
	} else {
		celllng = polygon._data.getLng();
	}
	if(lat) {
		celllat = lat;
	} else {
		celllat = polygon._data.getLat();
	}	
	
	showOperTips("loadingDataDiv", "loadContentId", "正在生成动态覆盖图");

	$.ajax({
		url : 'get4GDynaCoverageData2ForAction',
		data : {
			'cityId' : cityId,
			'lteCellId' : celllabel,
			'startDate' : sDate,
			'endDate' : eDate,
			'imgCoeff' : imgCoeff,
			'imgSizeCoeff':imgSizeCoeff
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			
			if(data != null) {
				
				var curvePoints_12 = data['curvePoints_12'];
				var curvePoints_3 = data['curvePoints_3'];
				var resInterDetail = data['resInterDetail'];
				if(curvePoints_12 != null && curvePoints_3 != null) {
					var vectorPoints_12 = data['vectorPoint_12'];
					var vecLng_12 = vectorPoints_12[0]['lng'];
					var vecLat_12 = vectorPoints_12[0]['lat'];
					
					var vectorPoints_3 = data['vectorPoint_3'];
					var vecLng_3 = vectorPoints_3[0]['lng'];
					var vecLat_3 = vectorPoints_3[0]['lat'];
					
					//console.log(points);
					var pointArray_12 = new Array();
					for ( var i = 0; i < curvePoints_12.length; i++) {
						var lng = curvePoints_12[i]["lng"];
						var lat = curvePoints_12[i]["lat"];
						pointArray_12.push(new BMap.Point(lng, lat));
					}
					var pointArray_3 = new Array();
					for ( var i = 0; i < curvePoints_3.length; i++) {
						var lng = curvePoints_3[i]["lng"];
						var lat = curvePoints_3[i]["lat"];
						pointArray_3.push(new BMap.Point(lng, lat));
					}
					for ( var i = 0; i < resInterDetail.length; i++) {
						var cellId = resInterDetail[i]["CELL_ID"];
						var ncellId = resInterDetail[i]["NCELL_ID"];
						var cosi1 = resInterDetail[i]["VAL1"];
						var cosi2 = resInterDetail[i]["VAL2"];
						var rsr0 = resInterDetail[i]["RSRPTIMES0"];
						var rsr1 = resInterDetail[i]["RSRPTIMES1"];
						var dis = resInterDetail[i]["DISTANCE"];
						$("#interDetailTab").append(
								"<tr><td>" + cellId + "</td><td>" + ncellId
										+ "</td><td>" + toConverVal(cosi1)
										+ "</td><td>" + toConverVal(cosi2)
										+ "</td><td>" + rsr0 + "</td><td>"
										+ rsr1 + "</td><td>" + toConverVal(dis)
										+ "</td></tr>");
					}
					new TableSorter("interDetailTab"); //为table加上点击排序
					//暂不填充颜色
					//添加动态覆盖图
					dynaPolygon_12 = new BMap.Polygon(pointArray_12, 
			        		{strokeColor:dynaPolygonColor_12,fillColor:dynaPolygonColor_12, strokeWeight:3, strokeOpacity:0.1,fillOpacity:0.3,});
					dynaPolygon_3 = new BMap.Polygon(pointArray_3, 
			        		{strokeColor:dynaPolygonColor_3,fillColor:dynaPolygonColor_3, strokeWeight:3, strokeOpacity:0.1,fillOpacity:0.3,}); 
			        map.addOverlay(dynaPolygon_12);
			        map.addOverlay(dynaPolygon_3);
			        //添加方向线
			        dynaPolyline_12 = new BMap.Polyline(
			        					[new BMap.Point(celllng, celllat),new BMap.Point(vecLng_12, vecLat_12)],
			        					{strokeColor:dynaPolylineColor_12, strokeWeight:3, strokeOpacity:0.5}); 
//			        dynaPolyline_3 = new BMap.Polyline(
//        					[new BMap.Point(celllng, celllat),new BMap.Point(vecLng_3, vecLat_3)],
//        					{strokeColor:dynaPolylineColor_3, strokeWeight:3, strokeOpacity:0.5});  
					map.addOverlay(dynaPolyline_12);
//					map.addOverlay(dynaPolyline_3);
					//添加方向线箭头
					addArrow(dynaPolyline_12,20,Math.PI/7,"1");
//					addArrow(dynaPolyline_3,20,Math.PI/7,"2");
					//为目标小区填充颜色
					gisCellDisplayLib.changeCellPolygonOptions(celllabel, {'fillColor':dynaCellColor,'fillOpacity':1}, true);
				} else {
					hideOperTips("loadingDataDiv");
					alert("该小区在搜索的时间段内没有数据！");
				}
			} else {
				hideOperTips("loadingDataDiv");
				alert("该小区在搜索的时间段内没有数据！");
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}






var titleLabels = new Array();
var offsetSize;
if(typeof gisCellDisplayLib!="undefined")
offsetSize = gisCellDisplayLib.getOffsetSize(3, 3);


// 鼠标移动事件的响应函数
function mouseoverFunction(polygon, event) {
	var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
	//将显示信息的坐标lng偏移+0.0005
	var lng = Number(cmk.getLng())+0.0005;
	var lat = Number(cmk.getLat());
	var point = gisCellDisplayLib.createPoint(lng,lat);
//	var point = new BMap.Point(polygon._data.getLng(), polygon._data.getLat());
	if(map instanceof BMap.Map){
	
	var label = new BMap.Label(gisCellDisplayLib.getTitleContent(polygon), {
		'position' : point,
		'offset' : offsetSize
	});
	}
	titleLabels.push(label);
	gisCellDisplayLib.addOverlay(label);
}
// 鼠标移过事件的响应函数
function mouseoutFunction(polygon, event) {
	for ( var i = 0; i < titleLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(titleLabels[i]);
	}
	titleLabels.splice(0, titleLabels.length);
}

/**
 * 清除全部的数据
 */
function clearAll() {
	//清除动态覆盖图，方向线，箭头
	map.removeOverlay(dynaPolygon_12);
	map.removeOverlay(dynaPolygon_3);
	map.removeOverlay(dynaPolyline_12);
//	map.removeOverlay(dynaPolyline_3);
	map.removeOverlay(dynaArrow_12);
//	map.removeOverlay(dynaArrow_3);
	gisCellDisplayLib.clearOnlyExtraOverlay();
	//恢复默认polygon颜色
	gisCellDisplayLib.resetPolygonToDefaultOutlook();
	
	//清除查询form
	resetQueryCondition();
}

//初始化form下的page信息
function initFormPage(formId){
	var form=$("#"+formId);
	if(!form){
		return;
	}
	form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

/**
 * 画出百度地图线的箭头
 * @param polyline  需要画箭头的线
 * @param length  长度
 * @param angleValue  箭头角度
 */
function addArrow(polyline,length,angleValue,type){ //绘制箭头的函数  
	var linePoint=polyline.getPath();//线的坐标串  
	var arrowCount=linePoint.length;  
	for(var i =1;i<arrowCount;i++){ //在拐点处绘制箭头  
		var pixelStart=map.pointToPixel(linePoint[i-1]);  
		var pixelEnd=map.pointToPixel(linePoint[i]);  
		var angle=angleValue;//箭头和主线的夹角  
		var r=length; // r/Math.sin(angle)代表箭头长度  
		var delta=0; //主线斜率，垂直时无斜率  
		var param=0; //代码简洁考虑  
		var pixelTemX,pixelTemY;//临时点坐标  
		var pixelX,pixelY,pixelX1,pixelY1;//箭头两个点  
		if(pixelEnd.x-pixelStart.x==0){ //斜率不存在是时  
		    pixelTemX=pixelEnd.x;  
		    if(pixelEnd.y>pixelStart.y){  
		    	pixelTemY=pixelEnd.y-r;  
		    } else {  
		    	pixelTemY=pixelEnd.y+r;  
		    }     
		    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
		    pixelX=pixelTemX-r*Math.tan(angle);   
		    pixelX1=pixelTemX+r*Math.tan(angle);  
		    pixelY=pixelY1=pixelTemY;  
		} 
		//斜率存在时  
		else {  
		    delta=(pixelEnd.y-pixelStart.y)/(pixelEnd.x-pixelStart.x);  
		    param=Math.sqrt(delta*delta+1);  
		  
		    //第二、三象限 
		    if((pixelEnd.x-pixelStart.x)<0) {  
		    	pixelTemX=pixelEnd.x+ r/param;  
		    	pixelTemY=pixelEnd.y+delta*r/param;  
		    }  
		    //第一、四象限  
		    else {  
		    	pixelTemX=pixelEnd.x- r/param;  
		    	pixelTemY=pixelEnd.y-delta*r/param;  
		    }  
		    //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法  
		    pixelX=pixelTemX+ Math.tan(angle)*r*delta/param;  
		    pixelY=pixelTemY-Math.tan(angle)*r/param;  
		  
		    pixelX1=pixelTemX- Math.tan(angle)*r*delta/param;  
		    pixelY1=pixelTemY+Math.tan(angle)*r/param;  
		}  
	  
		var pointArrow=map.pixelToPoint(new BMap.Pixel(pixelX,pixelY));  
		var pointArrow1=map.pixelToPoint(new BMap.Pixel(pixelX1,pixelY1));
		if(type=="1") {
			dynaArrow_12 = new BMap.Polyline([pointArrow,linePoint[i],pointArrow1], 
					{strokeColor:dynaPolylineColor_12, strokeWeight:3, strokeOpacity:0.5});  
			map.addOverlay(dynaArrow_12);  	
		}
//		else if(type=="2") {
//			dynaArrow_3 = new BMap.Polyline([pointArrow,linePoint[i],pointArrow1], 
//					{strokeColor:dynaPolylineColor_3, strokeWeight:3, strokeOpacity:0.5});  
//			map.addOverlay(dynaArrow_3);  
//		}

	}  
}  



/***************** 查找地图小区 start ********************/
// 按条件搜索小区
function searchCell() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找小区");
	
	// 获取输入的值
	var inputValue = $.trim($("#conditionValue").val());
	var conditionType = $("#conditionType").val();

	//console.log("conditionType="+conditionType+",inputValue="+inputValue);
	if ($.trim(inputValue) == "") {
		alert("请输入搜索条件");
		hideOperTips("loadingDataDiv");
		return;
	}

	// 将特别渲染的polygon恢复默认
	clearSpecRendPolygons();
	
	//对所需的小区数据进行预加载，再渲染展示
	var cityId = $("#cityId").val();
	var areaId = $("#areaId").val();
	mapGridLib.loadLteRelaCellByCellParamAndCityIdOnMap(inputValue,conditionType,cityId,areaId,function(){
		var composeMarkers=gisCellDisplayLib.get("composeMarkers");
		// 开始遍历
		size = composeMarkers.length;
		var cmk;
		var findcmk;
		var pl;
		var find=false;
		for ( var i = 0; i < size; i++) {
			cmk = composeMarkers[i];
			// console.log("cmk.get("+conditionType+")="+cmk.get(conditionType));
			if (cmk.hasLikeCell(conditionType, inputValue)) {
				find=true;
				findcmk=cmk;
				pl=gisCellDisplayLib.getShapeObjByComposeMarker(findcmk);
				gisCellDisplayLib.changeCellPolygonOptions(findcmk.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
			}
		}
	
		if (find===false) {
			alert("未找到符合条件的小区");
		}else{
			// 移动地图
			var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
			gisCellDisplayLib.panTo(cmk.getLng(), cmk.getLat());
		}
		hideOperTips("loadingDataDiv");
	});
}


// 按条件搜索邻区
function searchNcell() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找邻区");
	var inputcell = $.trim($("#cellForNcell").val());
	if (inputcell === "") {
		//hideLoadTip("loadingDataDiv");
		alert("请输入小区名！");
		hideOperTips("loadingDataDiv");
		return;
	}
	var size = ncellsOfCell.length;
	var one;
	var ct = new Date().getTime();
	for ( var i = 0; i < size; i++) {
		one = ncellsOfCell[i];
		if (one.isSame(inputcell)) {
			if (one.getCreateTime() - ct > maxCacheNcellsTime) {
				// 超过缓存时间
				ncellsOfCell.splice(i, 1);
				one = null;
			}
			break;
		}
		one = null;
	}
	if (!one) {
		//对所需的邻区数据进行预加载，再渲染展示
		var cityId = $("#cityId").val();
		var areaId = $("#areaId").val();
		mapGridLib.loadNcellDetailsByCellOnMap(inputcell,cityId,areaId,function(ncells){
			if (ncells.length == 0) {
				alert("未找到指定小区的邻区！");
				hideOperTips("loadingDataDiv");
				return;
			} else {
				var cellToNcell = new CellToNcell(inputcell,gisCellDisplayLib.get("composeMarkers"));
				if (!cellToNcell) {
					alert("指定的小区尚不存在！");
					hideOperTips("loadingDataDiv");
					return;
				} else {
					for ( var i = 0; i < ncells.length; i++) {
						cellToNcell.addNcell(ncells[i]);
					}
				}
				// 展现
				showNcell(cellToNcell);
				hideOperTips("loadingDataDiv");
			}
		});
	}
	
}
/**
 * 渲染邻区
 * 
 * @param cellToNcell
 */
function showNcell(cellToNcell) {
	if (!cellToNcell) {
		return;
	}
	// 先清除之前的搜索结果
	clearSpecRendPolygons();
	// 渲染
	var pl = cellToNcell.getPolygon();
	if (pl) {
		// 先移动。移动会触发事件
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
		gisCellDisplayLib.panTo(cmk.getLng(),cmk.getLat());
//		gisCellDisplayLib.panTo(pl._data.getLng(),pl._data.getLat());
		gisCellDisplayLib.changeCellPolygonOptions(cellToNcell.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
	}
	var npls = cellToNcell.getNcells();
	var size=0;
	if(npls instanceof Array){
		size = npls.length;
	}
	for ( var i = 0; i < size; i++) {
		gisCellDisplayLib.changeCellPolygonOptions(npls[i], {'fillColor':targetNcellColor,'fillOpacity':1}, true);
	}

	hideOperTips("loadingDataDiv");
}

// 按条件搜索频点
function searchFreq() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找频点");
	
	var freq = $.trim($("#freqValue").val());
	if (freq == "") {
		hideOperTips("loadingDataDiv");
		alert("请输入需要查找的频点");
		return;
	}
	if (isNaN(freq)) {
		hideOperTips("loadingDataDiv");
		alert("请输入数字频点！");
		return;
	}

	clearSpecRendPolygons();

	// 可视区域
	var visiblePolygons=gisCellDisplayLib.get("visiblePolygons");
	var size = visiblePolygons.length;
	var pl;
	var cont;
	var label;
	var cnt=0;
	for ( var i = 0; i < size; i++) {
		cont = null;
		pl = visiblePolygons[i];
		if(!pl){continue;}
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
		cont = cmk.getContainsFreqCell(freq);
		if (cont != null) {
			cnt++;
			//var pt=gisCellDisplayLib.getCellEdgeCenterPoint(cmk.getCell());
			var pt=gisCellDisplayLib.getLnglatObjByComposeMarker(cmk);
			if(i==1){
				gisCellDisplayLib.panTo(pt.lng, pt.lat);
			}
			if(pt!=null){
				label = new IsTextLabel(pt.lng, pt.lat, cont,null,gisCellDisplayLib);
			}else{
			    label = new IsTextLabel(cmk.getLng(), cmk.getLat(), cont,null,gisCellDisplayLib);
			}
			if (label) {
				specRenderderLabel.push(label);
				gisCellDisplayLib.addOverlay(label);
			}
		}
	}
	
	hideOperTips("loadingDataDiv");
	if(cnt==0){
		alert("未搜索到指定的频点的信息!");
	}
}

/**
 * 清理特殊渲染的图形
 */
function clearSpecRendPolygons() {
	
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();

	// -----label---------//
	size = specRenderderLabel.length;
	if (size > 0) {
		for ( var i = 0; i < size; i++) {
			try {
				gisCellDisplayLib.removeOverlay(specRenderderLabel[i]);
			} catch (er) {
				// ignore
			}
		}
	}
	specRenderderLabel.splice(0, size);
}
/***************** 查找地图小区 end ********************/


/***************** 地图小区加载改造 start ********************/
function handleMoveendAndZoomend(e, lastOperTime) {
	var winMinLng = map.getBounds().getSouthWest().lng;
	var winMinLat = map.getBounds().getSouthWest().lat;
	var winMaxLng = map.getBounds().getNorthEast().lng;
	var winMaxLat = map.getBounds().getNorthEast().lat;
	//设置当前屏幕经纬度范围
	mapGridLib.setWinLngLatRange(winMinLng,winMinLat,winMaxLng,winMaxLat);
	//每一次移动，缩放结束的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
	var gsmtype = $("input[name='freqType']:checked").val();
	var params = {
		'freqType' : gsmtype
	};
	mapGridLib.loadLteCell(lastOperTime, currentloadtoken, minResponseInterval, params);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
/***************** 地图小区加载改造 end ********************/

/**************** ************************************/

/**
 * 
 * @title I N干扰小区连线：主小区被所有小区检测的连线。
 * @param polygon
 * @param cell
 * @param lng
 * @param lat
 * @author chao.xj
 * @date 2015-6-12上午11:28:15
 * @company 怡创科技
 * @version 1.2
 */
function InInterCellLine(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	$("#interDetailTab tr:gt(0)").remove(); 
	//清空界面数据
	clearAll();

	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	//获取城市id
	var cityId = $("#cityId").val();
	//获取日期范围
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	
	//获取图形大小系数
	var imgCoeff = $("#imgCoeff").val();
	//console.log(Number(imgCoeff));
	var valiNumber = /^[+]?[0-9]+(\.[0-9]+)?$/;   //验证数字
	
	var celllng,celllat;
	if(lng) {
		celllng = lng;
	} else {
		celllng = polygon._data.getLng();
	}
	if(lat) {
		celllat = lat;
	} else {
		celllat = polygon._data.getLat();
	}	
	
	showOperTips("loadingDataDiv", "loadContentId", "正在获取IN干扰数据");

	$.ajax({
		url : 'get4GDynaCoverageInInferDataForAction',
		data : {
			'cityId' : cityId,
			'lteCellId' : celllabel,
			'startDate' : sDate,
			'endDate' : eDate
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
//			console.log("data========>>>>>>>>>>>"+data);
			if(data != null) {
				for ( var i = 0; i < data.length; i++) {
					var cellId=data[i]['CELL_ID'];
					var cellLat=data[i]['CELL_LAT'];
					var cellLon=data[i]['CELL_LON'];
					var ncellId=data[i]['NCELL_ID'];
					var ncellLat=data[i]['NCELL_LAT'];
					var ncellLon=data[i]['NCELL_LON'];
//					console.log(cellId+" "+cellLon+" "+cellLat+" "+ncellId+" "+ncellLon+" "+ncellLat);
					//连线
					gisCellDisplayLib.drawLineBetweenPoints(cellLon,cellLat,ncellLon,ncellLat,{'strokeColor':'red',"strokeWeight":1});
				}
					//为目标小区填充颜色
					gisCellDisplayLib.changeCellPolygonOptions(celllabel, {'fillColor':dynaCellColor,'fillOpacity':1}, true);
				} else {
					hideOperTips("loadingDataDiv");
					alert("该小区在搜索的时间段内没有数据！");
				}
			
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}
/**
 * 
 * @title OUT干扰小区连线：主小区检测到所有邻小区连线。
 * @param polygon
 * @param cell
 * @param lng
 * @param lat
 * @author chao.xj
 * @date 2015-6-12上午11:28:15
 * @company 怡创科技
 * @version 1.2
 */
function OutInterCellLine(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	$("#interDetailTab tr:gt(0)").remove(); 
	//清空界面数据
	clearAll();

	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	//获取城市id
	var cityId = $("#cityId").val();
	//获取日期范围
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	
	
	var celllng,celllat;
	if(lng) {
		celllng = lng;
	} else {
		celllng = polygon._data.getLng();
	}
	if(lat) {
		celllat = lat;
	} else {
		celllat = polygon._data.getLat();
	}	
	
	showOperTips("loadingDataDiv", "loadContentId", "正在获取OUT干扰数据");

	$.ajax({
		url : 'get4GDynaCoverageOutInferDataForAction',
		data : {
			'cityId' : cityId,
			'lteCellId' : celllabel,
			'startDate' : sDate,
			'endDate' : eDate
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			
			if(data != null) {
				for ( var i = 0; i < data.length; i++) {
					var cellId=data[i]['CELL_ID'];
					var cellLat=data[i]['CELL_LAT'];
					var cellLon=data[i]['CELL_LON'];
					var ncellId=data[i]['NCELL_ID'];
					var ncellLat=data[i]['NCELL_LAT'];
					var ncellLon=data[i]['NCELL_LON'];
//					console.log(cellId+" "+cellLon+" "+cellLat+" "+ncellId+" "+ncellLon+" "+ncellLat);
					//连线
					gisCellDisplayLib.drawLineBetweenPoints(cellLon,cellLat,ncellLon,ncellLat,{'strokeColor':'red',"strokeWeight":1});
				}
					//为目标小区填充颜色
					gisCellDisplayLib.changeCellPolygonOptions(celllabel, {'fillColor':dynaCellColor,'fillOpacity':1}, true);

				
			} else {
				hideOperTips("loadingDataDiv");
				alert("该小区在搜索的时间段内没有数据！");
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}
function toConverVal(val) {
	var num = Number(val);
	if (num != 0) {
		return num.toFixed(4);
	} else {
		return num;
	}
}
(function($){   
	$.chromatable = {   
	defaults: {   
	width: "900px",   
	height: "300px",   
	scrolling: "yes"   
	}   
	}; 
$.fn.chromatable = function(options){   
	var options = $.extend({}, $.chromatable.defaults, options);   
	return this.each(function(){   
	var $this = $(this);   
	var $uniqueID = $(this).attr("ID") + ("wrapper");   
	$(this).css('width', options.width).addClass("_scrolling");   
	$(this).wrap('<div class="scrolling_outer"><div id="'+$uniqueID+'" class="scrolling_inner"></div></div>');   
	$(".scrolling_outer").css({'position':'relative'});   
	$("#"+$uniqueID).css(   
	{'border':'1px solid #CCCCCC',   
	'overflow-x':'hidden',   
	'overflow-y':'auto',   
	'padding-right':'17px'   
	});   
	$("#"+$uniqueID).css('height', options.height);   
	$("#"+$uniqueID).css('width', options.width);   
	$(this).before($(this).clone().attr("id", "").addClass("_thead").css(   
	{'width' : 'auto',   
	'display' : 'block',   
	'position':'absolute',   
	'border':'none',   
	'border-bottom':'1px solid #CCC',   
	'top':'1px'   
	}));   
	$('._thead').children('tbody').remove();   
	$(this).each(function( $this ){   
	if (options.width == "100%" || options.width == "auto") {   
	$("#"+$uniqueID).css({'padding-right':'0px'});   
	}   
	if (options.scrolling == "no") {   
	$("#"+$uniqueID).before('<a href="#" class="expander" style="width:100%;">Expand table</a>');   
	$("#"+$uniqueID).css({'padding-right':'0px'});   
	$(".expander").each(   
	function(int){   
	$(this).attr("ID", int);   
	$( this ).bind ("click",function(){   
	$("#"+$uniqueID).css({'height':'auto'});   
	$("#"+$uniqueID+" ._thead").remove();   
	$(this).remove();   
	});   
	});   
	$("#"+$uniqueID).resizable({ handles: 's' }).css("overflow-y", "hidden");   
	}   
	});   
	$curr = $this.prev();   
	$("thead:eq(0)>tr th",this).each( function (i) {   
	$("thead:eq(0)>tr th:eq("+i+")", $curr).width( $(this).width());   
	});   
	if (options.width == "100%" || "auto"){   
	$(window).resize(function(){   
	resizer($this);   
	});   
	}   
	});   
	};   
	function resizer($this) {   
	$curr = $this.prev();   
	$("thead:eq(0)>tr th", $this).each( function (i) {   
	$("thead:eq(0)>tr th:eq("+i+")", $curr).width( $(this).width());   
	});   
	};   
	})(jQuery);   


jQuery.fn.CloneTableHeader = function(tableId, tableParentDivId) {

    var obj = document.getElementById("tableHeaderDiv" + tableId);

    if (obj) {

        jQuery(obj).remove();

    }

    var browserName = navigator.appName;

    var ver = navigator.appVersion;

    var browserVersion = parseFloat(ver.substring(ver.indexOf("MSIE") + 5, ver.lastIndexOf("Windows")));

    var content = document.getElementById(tableParentDivId);

    var scrollWidth = content.offsetWidth - content.clientWidth;

    var tableOrg = jQuery("#" + tableId)

    var table = tableOrg.clone();

    table.attr("id", "cloneTable");

    var tableClone = jQuery(tableOrg).find("tr").each(function() {

    });

    var tableHeader = jQuery(tableOrg).find("thead");

    var tableHeaderHeight = tableHeader.height();

    tableHeader.hide();

    var colsWidths = jQuery(tableOrg).find("tbody tr:first td").map(function() {

        return jQuery(this).width();

    });

    var tableCloneCols = jQuery(table).find("thead tr:first td")

    if (colsWidths.size() > 0) {

        for (i = 0; i < tableCloneCols.size(); i++) {

            if (i == tableCloneCols.size() - 1) {

                if (browserVersion == 8.0)

                    tableCloneCols.eq(i).width(colsWidths[i] + scrollWidth);

                else

                    tableCloneCols.eq(i).width(colsWidths[i]);

            } else {

                tableCloneCols.eq(i).width(colsWidths[i]);

            }

        }

    }

    var headerDiv = document.createElement("div");

    headerDiv.appendChild(table[0]);

    jQuery(headerDiv).css("height", tableHeaderHeight);

    jQuery(headerDiv).css("overflow", "hidden");

    jQuery(headerDiv).css("z-index", "20");

    jQuery(headerDiv).css("width", "100%");

    jQuery(headerDiv).attr("id", "tableHeaderDiv" + tableId);

    jQuery(headerDiv).insertBefore(tableOrg.parent());

}