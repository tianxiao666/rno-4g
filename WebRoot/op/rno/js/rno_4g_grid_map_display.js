var $ = jQuery.noConflict();
var gisCellDisplayLib;// 负责显示小区的对象

var map;
var ge;
var cellDetails = new Array();// 小区详情

//
var minZoom = 17;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间

var mapGridLib; //用于将地图分成网格加载小区的对象

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var currentAreaCenter;// 当前所选区域的中心点

var totalCellCnt = 0;// 小区总数

$(document).ready(function() {
	// 隐藏详情面板
	//hideDetailDiv();
	
	// 初始化地图
		initMap();
	 $("#trigger").click(function(){
	 	 //console.log("glomapid:"+glomapid);
	 	 if(glomapid=='null' || glomapid=='baidu'){
//	 	  console.log("切换前是百度");
		  $(this).val("切换百度");
//		  console.log("切换后是谷歌");
		  sessId="google";
	 	 }else{
//	 	 	console.log("切换前是谷歌");
		 	$(this).val("切换谷歌");
//		 	console.log("切换后是百度");
		 	sessId="baidu";
	 	 }
		
		 storageMapId(sessId);
		 });
	
	$("#loadgisgrid").click(function(){
		 	
		showAreaGrid();
		 });
	$("#cityId").change(function(){
		$("#exportgisgridcell").attr('disabled',false);
	});
	/*$("#exportgisgridcell").click(function(){
	 
        exportgisgridcell();
		var cityId=$("#cityId").find("option:selected").val();
		$(".loading_cover").css("display", "block");
		exportExcelFile(cityId);
		 });*/
});

/**
 * 导出网格小区数据excel文件
 */
function exportExcelFile() {
	 var cityId=$("#cityId").find("option:selected").val();	
	 var url="exportLteGridDataByCityAjaxAction.action?ctId="+cityId;
	 window.location.href=url;	 
}
/**
 * 初始化地图
 */
function initMap() {
	document.getElementById('map_canvas').style.position = 'absolute';
	document.getElementById('map_canvas').style.zIndex = '0';
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
			null, {
				'clickFunction' : handleClick,
				'mouseoverFunction' : null,
				'mouseoutFunction' : null,
				'rightclickFunction':null
			},"","", preZoom);
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
	map = gisCellDisplayLib.initMap("map_canvas", lng, lat, {
		"tilesloaded" : function() {
			bindEvent();
			// 加载数据
			currentloadtoken = getLoadToken();
			//getGisCell(currentloadtoken);
		},
		'movestart' : handleMovestartAndZoomstart,
		'zoomstart' : handleMovestartAndZoomstart,
		'moveend' : handleMoveendAndZoomend,
		'zoomend' : handleMoveendAndZoomend 
	});
	
	/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
	//以网格形式在地图加载小区
	mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
	//以城市为单位创建区域网格
	var cityName = $("#cityId").find("option:selected").text().trim();
	mapGridLib.createMapGrids(cityName);
	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/
				
	// 创建信息窗口
//	gisCellDisplayLib.createInfoWindow("");
}

// 隐藏详情面板
function hideDetailDiv() {

	$("a.siwtch").hide();
	$(".switch_hidden").show();
	$(".resource_list_icon").animate({
		right : '0px'
	}, 'fast');
	$(".resource_list_box").hide("fast");
}

// 清除全部的数据
function clearAll() {
	try {
		//map.closeInfoWindow();
	} catch (e) {

	}
	try {
		gisCellDisplayLib.clearOverlays();
	} catch (e) {

	}

	cellDetails.splice(0, cellDetails.length);
	
	gisCellDisplayLib.clearData();

}

function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

}



/**
 * 获取gis小区
 */
function getGisCell(loadToken) {
	$("#conditionForm").ajaxSubmit(
			{
				url : 'getGisCellByPageForAjaxAction',
				dataType : 'json',
				success : function(data) {
					if (loadToken != currentloadtoken) {
						// 新的加载开始了，这些旧的数据要丢弃
						// console.log("丢弃此次的加载结果。");
						return;
					}
					// console.log(data);
					var obj = data;
					try {
						// obj = eval("(" + data + ")");
						// alert(obj['celllist']);
						// showGisCell(obj['gisCells']);
						gisCellDisplayLib.showGisCell(obj['gisCells']);
						var page = obj['page'];
						if (page) {
							var pageSize = page['pageSize'] ? page['pageSize']
									: 0;
							$("#hiddenPageSize").val(pageSize);

							var currentPage = new Number(
									page['currentPage'] ? page['currentPage']
											: 1);
							currentPage++;// 向下一页
							$("#hiddenCurrentPage").val(currentPage);

							var totalPageCnt = new Number(
									page['totalPageCnt'] ? page['totalPageCnt']
											: 0);
							$("#hiddenTotalPageCnt").val(totalPageCnt);

							var totalCnt = page['totalCnt'] ? page['totalCnt']
									: 0;
							totalCellCnt = totalCnt;
							$("#hiddenTotalCnt").val(totalCnt);

							if (totalPageCnt >= currentPage) {
								// console.log("继续获取下一页小区");
								getGisCell(loadToken);
							}
						}
						// 如果没有获取完成，则继续获取
					} catch (err) {
						// console.log("返回的数据有问题:" + err);
					}
				},
				error : function(xmh, textstatus, e) {
//					alert("出错啦！" + textstatus);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}





/**
 * 显示所关联的小区的详情
 * 
 * @param {}
 *            polygon
 */
function showCellDetail(label) {
	// alert("关联的小区为：" + polygon._data.getCell());

	// 看本地缓存有没有
	var cell = null;
	var find = false;
	for ( var i = 0; i < cellDetails.length; i++) {
		cell = cellDetails[i];
		if (cell['label'] === label) {
			 //console.log("从缓存找到详情。")
			find = true;
			break;
		}
	}

	if (find) {
		displayCellDetail(cell);
		return;
	}

	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'getCellDetailForAjaxAction',
		data : {
			'label' : label
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var c = data;
			// try {
			// //c = eval("(" + data + ")");
			// } catch (e) {
			// alert("获取小区详情失败！");
			// return;
			// }
			if (c) {
				cellDetails.push(c);
				displayCellDetail(c);
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 处理polygon的点击事件
 * 
 * @param {}
 *            polygon
 */
function handleClick(polygon, cell) {
	if(!cell) {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		cell = cmk.getCell();
	}
	showCellDetail(cell);
}
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
	/*var gsmtype = $("input[name='freqType']:checked").val();
	var params = {
		'freqType' : gsmtype
	};*/
	mapGridLib.loadGisCell(lastOperTime, currentloadtoken, minResponseInterval);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
function bindEvent() {

	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(
			function() {
				getSubAreas("provinceId", "queryCellAreaId", "市", function(data) {
					$("#hiddenAreaLngLatDiv").html("");
					$("#hiddenLng").val("");
					$("#hiddenLat").val("");
					$("#exportgisgridcell").attr('disabled',true);
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
							/*if (i == 0) {
								$("#hiddenLng").val(one["longitude"]);
								$("#hiddenLat").val(one["latitude"]);
							}*/
							if (one['area_id'] == $("#cityId").find("option:selected").val()) {
								$("#hiddenLng").val(one["longitude"]);
								$("#hiddenLat").val(one["latitude"]);
								//map.panTo(new BMap.Point(lls[0], lls[1]));// 这个偏移可以不理会
								gisCellDisplayLib.panTo(one["longitude"], one["latitude"]);
							}
						}
						$("#hiddenAreaLngLatDiv").append(html);
					}

//					$("#queryCellAreaId").trigger("change");
				});
			});

	// 区域选择改变
	$("#queryCellAreaId").change(function() {
		var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
		//console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		clearAll();

		// 地图中心点
		var lls = lnglat.split(",");
		//map.panTo(new BMap.Point(lls[0], lls[1]));// 这个偏移可以不理会
		gisCellDisplayLib.panTo(lls[0], lls[1]);
		// 重置查询表单的数据
		resetQueryCondition();
		// 重新获取新区域的数据
		// 重新获取新区域的数据
		currentloadtoken = getLoadToken();
		//getGisCell(currentloadtoken);

		/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
		//以城市为单位创建区域网格
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
		/******* peng.jm 2014-9-29 网格形式加载小区  end ********/	
	});
}

/**
 * 
 * @title 显示区域网格
 * @author chao.xj
 * @date 2015-7-6下午12:17:57
 * @company 怡创科技
 * @version 1.2
 */
function showAreaGrid() {
	var cityId=$("#cityId").find("option:selected").val();
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'queryLteGridDataByCityAjaxAction',
		data : {
			'cityId' : cityId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var c = data;
			if(data.length>0){
				$("#exportgisgridcell").attr('disabled',false);
			}
			for ( var i = 0; i < data.length; i++) {
//				var a=data[i]["GRID_LNGLATS"];
//				console.log("lnglats===="+a.split(";"));
				var lnglat=data[i]["GRID_CENTER"].split(",");
				
				gisCellDisplayLib.createLabel(lnglat[0],lnglat[1],data[i]["GRID_ID"],data[i]["GRID_DESC"]);
				gisCellDisplayLib.createPolygonFromLngLats(data[i]["GRID_LNGLATS"]);
			}
//			gisCellDisplayLib.createPolygonFromLngLats(aa);
			// try {
			// //c = eval("(" + data + ")");
			// } catch (e) {
			// alert("获取小区详情失败！");
			// return;
			// }
//			gisCellDisplayLib.createPolygonFromLngLats();
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/*function exportgisgridcell() {
	var cityId=$("#cityId").find("option:selected").val();
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'exportLteGridDataByCityAjaxAction',
		data : {
			'cityId' : cityId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(data.length>0){
				$("#exportgisgridcell").attr('disabled',false);
			}
			
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}*/