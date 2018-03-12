var $ = jQuery.noConflict();
var gisCellDisplayLib;// 负责显示小区的对象

var map;
var ge;
var cellDetails = new Array();// 小区详情

var errFillColor = "#FF0000"; //问题小区颜色
var errFillOpacity = 0.7; //问题小区透明度
var errOverFillColor = "#FF0000"; //问题问题小区颜色
var errOverFillOpacity = 1.0; //重叠问题小区透明度

var titleLabels = new Array(); //覆盖物标签列表

var problemCell=new Array(); //问题小区列表

var minLng;
var minLat;
var maxLng;
var maxLat;

//
var minZoom = 17;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 1000;// 在不需要全部显示的时候，最大随机显示的个数
var preZoom = 16;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间

var mapGridLib; //用于将地图分成网格加载小区的对象

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var currentAreaCenter;// 当前所选区域的中心点

var totalCellCnt = 0;// 小区总数
var isShowCellName = false; //地图是否显示小区名字
var configOption = new Object(); //地图初始化配置
//相似度判断阈值
var DiffAzimuth = 20;// 角度
var DiffDistance = 20;// 距离（米）

$(document).ready(function() {
	// 隐藏详情面板
	hideDetailDiv();
	//$("#tabs").tabs();
	resetQueryCondition();
	// loadScript("initMap");
	//console.log("js:glomapid:"+glomapid);
	// 初始化地图
	initMap();
	initEvent();
/*	if(glomapid=='null' || glomapid=='baidu'){
		//console.log("baidu");
		// 初始化地图
		initMap();
	}else{
		//console.log("google");
		//initGeMap();
		initGeMap();
	}*/

});

/**
 * 通过session存储地图ID
 */
function storageMapId(mapid){
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'storageMapIdBySessForAjaxAction',
		data : {
			'mapId' : mapid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			glomapid=data;
			//console.log("data:"+data);
//			var c = data;
			try{
//			initRedirect("initGisCellDisplayAction");
			window.location.href=window.location.href;
			}catch(err){
			
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
 * initGisCellDisplayAction
 */
/*function initRedirect(action){
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : action,
		dataType : 'json',
		type : 'post',
		cache:false,
		async:false,
		ifModified :true, 
		beforeSend :function(xmlHttp){
		xmlHttp.setRequestHeader("If-Modified-Since","0");
		xmlHttp.setRequestHeader("Cache-Control","no-cache");
		    }, 
		success : function(data) {
			//console.log(glomapid);
//			var c = data;
			try{
			//self.location.reload();
			 history.go(0);
			 location.reload();
			 document.execCommand('Refresh');
			}catch(err){
			
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
	
}*/
/*function initGeMap() {
	
	gisCellDisplayLib = new GisCellDisplayLib(ge,null,null,null,{
				'clickFunction' : handleClick});
	//116.3902750,39.9914944 北京 113.591766357422003000,24.791721545708778500 武江
//	var lng = "113.591766357422003000";//
//	var lat = "24.791721545708778500";//
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
//	map = new google.maps.Map(document.getElementById("map3d"));
//	map = new GMap2(document.getElementById("map3d"));
	gisCellDisplayLib.initGeMap("map_canvas", lng, lat);
	//setInterval(function(){ge=gisCellDisplayLib.ge;},500);
	bindEvent();
	// 加载数据
	currentloadtoken = getLoadToken();
	getGisCell(currentloadtoken);
}*/
/**
 * 初始化地图
 */
function initMap() {
	document.getElementById('map_canvas').style.position = 'absolute';
	document.getElementById('map_canvas').style.zIndex = '0';
	//配置相似度阀值和是否显示小区名
	configOption['showCellLabel']=isShowCellName;
	configOption['DiffAzimuth']=DiffAzimuth;
	configOption['DiffDistance']=DiffDistance;
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
			null, {
				'clickFunction' : handleClick,
				'mouseoverFunction' : handleMouseOver,
				'mouseoutFunction' : handleMouseOut,
				'rightclickFunction':handleRrigthClick
			},configOption,handleContextMenu, preZoom);
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
	mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip","",true); 
	//以城市为单位创建区域网格
	var cityName = $("#cityId").find("option:selected").text().trim();
	mapGridLib.createMapGrids(cityName);
	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/
				
	// 创建信息窗口
//	gisCellDisplayLib.createInfoWindow("");
}

/**
 * 初始化事件
 */
function initEvent(){
	//控制地图小区名称是否显示
	$("#showCellName").val(isShowCellName?"关闭小区名字":"显示小区名字");
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
		/* var temp = $(this).val();
		 var sessId;
		 if(temp=="切换百度"){
		 	console.log("切换前是谷歌");
		 	$(this).val("切换谷歌");
		 	console.log("切换后是百度");
		 	sessId="baidu";
		 	
		 	//重新刷新本页面
		 }else{
		 console.log("切换前是百度");
		  $(this).val("切换百度");
		  console.log("切换后是谷歌");
		  sessId="google";
		  //重新刷新本页面
		 }*/
		 storageMapId(sessId);
		 });
	$("#showpbcList").click(function(){
		$(".switch_hidden").trigger("click");
		$("#div_tab ul li")[2].click();
		showProblemCellList();
	});
	
	$("#loadltecell").click(function(){
		 	/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
		 	//清除缓存数据
		 	clearAll();
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
			//console.log("参数设置完成");
			/*var gsmtype = $("input[name='freqType']:checked").val();
			var params = {
				'freqType' : gsmtype
			};*/
			//t-minResponseInterval保证不延迟加载
			//mapGridLib.loadLteCell(t-minResponseInterval, currentloadtoken, minResponseInterval);
		 	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/	
			
		 	//getGisCell(currentloadtoken);
			loadLteCellAboutPm(t-minResponseInterval, currentloadtoken, minResponseInterval);
		 });
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

function bindEvent() {

	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(function() {
		getSubAreas("cityId", "queryCellAreaId", "区/县", function(data) {
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

					//$("#queryCellAreaId").trigger("change");
				});
			});

	// 区域选择改变
	$("#queryCellAreaId").change(function() {
		var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
		//console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		clearAll();
		problemCell.splice(0, problemCell.length);
		var crt = new Date().getTime();
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
		loadLteCellAboutPm(crt-minResponseInterval, currentloadtoken, minResponseInterval);

		/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
		//以城市为单位创建区域网格
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
		/******* peng.jm 2014-9-29 网格形式加载小区  end ********/	
		
		$("span#loadingProbCellTip").html("");
	});
}

// 清除全部的数据
function clearAll() {

	// var pl;
	// for ( var i = 0; i < allPolygons.length; i++) {
	// pl = allPolygons[i];
	// if (pl._isShow == true) {
	// pl._isShow = false;
	// map.removeOverlay(pl);
	// }
	// }

	try {
		//map.closeInfoWindow();
	} catch (e) {

	}
	try {
		gisCellDisplayLib.clearOverlays();
	} catch (e) {

	}

	cellDetails.splice(0, cellDetails.length);
	
	$("span#loadingCellTip").html("");

	gisCellDisplayLib.clearData();

}

function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

}

function loadLteCellAboutPm(lastOperTime, loadToken, minResponseInterval,params){

	var me = mapGridLib;
	
	//console.log("lastOperTime:"+lastOperTime);
	var ct = new Date().getTime();
	//经过2秒再执行
	if (ct - lastOperTime < minResponseInterval 
				&& loadToken == me.currentloadtoken) {
		//console.log("too frequently.");
		window.setTimeout(function() {
			loadLteCellAboutPm(lastOperTime, loadToken, minResponseInterval, params);
		}, 1000);
		//console.log("还不到2秒,不执行");
		return;
	}

	//判断延迟加载后是否还是当前最新的加载
	if(loadToken != me.currentloadtoken) {
		return;
	}
	//console.log("开始加载");
	//获取需要显示的网格，设置其加载状态为true
	me.setDisplayGrids();
	//循环网格列,先加载第一个
	var i = 0;

	if(params != undefined) {
		me.params = params;
	}
	//showProblemCellList();	
	//console.log("获取PM小区");
	getGisCellByMapGrid(i, loadToken);
	//显示屏幕内的小区，隐藏屏幕外的小区，在缩放级别一定值时只显示默认数量的小区
	me.showCellObj.handleMoveEnd();
	me.showCellObj.handleZoomEnd();
}

/**
 * 获取gis小区
 */
function getGisCellByMapGrid(i,loadToken) {
	var me = mapGridLib;
	var gridGrade = me.defGridGrade;
	if(me.cityMapGrids.length <= 0) {
		return;
	}
	if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
		$("span#"+me.tipSpanId).html("加载小区中...");
	}
	if(loadToken != me.currentloadtoken) {
		//console.log("新的加载开始了，这次加载结束");
		return;
	}
	//判断是否超出一百个网格
	if(i >= gridGrade*gridGrade) {
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("数据加载完成");
		}
		return;
	}
	//判断地图是否处于加载状态
	if(!me.isLoading) {
		//console.log("小区加载状态："+isLoading);
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("");
		}
		return;
	}
	//判断网格是否加载中
	if(!me.cityMapGrids[i].getIsLoading()) {
		//console.log("mapGrids["+i+"]不处于加载中");
		i++;
		getGisCellByMapGrid(i, loadToken);
		return;
	} else {
		//判断网格是否加载完成
		if(me.cityMapGrids[i].getIsFinished()) {
			//console.log("mapGrids["+i+"]加载完成");
			i++;
			getGisCellByMapGrid(i, loadToken);
			return;
		} 
	}
	var sendData;
/*	var	gridId = $("#hiddenTotalCnt").val();
	var	minLng = $("#hiddenTotalCnt").val();
	var	minLat = $("#hiddenTotalCnt").val();
	var	maxLng = $("#hiddenTotalCnt").val();
	var	maxLat = $("#hiddenTotalCnt").val();
	console.log("minLng"+minLng);
	var pageSize = $("#hiddenPageSize").val();
	var currentPage = $("#hiddenCurrentPage").val();
	var totalPageCnt = $("#hiddenTotalPageCnt").val();
 	var totalCnt = $("#hiddenTotalCnt").val();*/
	var	gridId = me.cityMapGrids[i].getGridId();
	var	minLng = me.cityMapGrids[i].getMinLng();
	var	minLat = me.cityMapGrids[i].getMinLat();
	var	maxLng = me.cityMapGrids[i].getMaxLng();
	var	maxLat = me.cityMapGrids[i].getMaxLat();
	var pageSize = me.cityMapGrids[i].getPageSize();
	var currentPage = me.cityMapGrids[i].getCurrentPage();
	var totalPageCnt = me.cityMapGrids[i].getTotalPageCnt();
 	var totalCnt = me.cityMapGrids[i].getTotalCnt();
	sendData={
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	$("#conditionForm").ajaxSubmit(
			{
				url : 'getLteCellByMapGridWithStsCellListForAjaxAction',
				data:sendData,
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
						//console.log("调用显示方法");
						// obj = eval("(" + data + ")");
						// alert(obj['celllist']);
						showLteCell(obj['lteCells']);
						//gisCellDisplayLib.showGisCell(obj['gisCells']);
						var page = obj['page'];
						if (page) {
							var pageSize = page['pageSize'] ? page['pageSize']: 0;
							$("#hiddenPageSize").val(pageSize);
							me.cityMapGrids[i].setPageSize($("#hiddenPageSize").val());

							var currentPage = page['currentPage'] ? page['currentPage']: 1;
							currentPage++;// 向下一页
							$("#hiddenCurrentPage").val(currentPage);
							me.cityMapGrids[i].setCurrentPage($("#hiddenCurrentPage").val());
							
							var totalPageCnt = new Number(	page['totalPageCnt'] ? page['totalPageCnt']: 0);
							$("#hiddenTotalPageCnt").val(totalPageCnt);
							me.cityMapGrids[i].setTotalPageCnt($("#hiddenTotalPageCnt").val());
							
							var totalCnt = page['totalCnt'] ? page['totalCnt']	: 0;
							totalCellCnt = totalCnt;
							$("#hiddenTotalCnt").val(totalCnt);
							me.cityMapGrids[i].setTotalCnt($("#hiddenTotalCnt").val());
							
							//console.log("网格["+i+"]第"+(currentPage-1)+"页数据加入了缓存");
							if (totalPageCnt >= currentPage) {
								//console.log("继续获取grid["+mapGrids[i].getGridId()+"]的下一页小区");
								getGisCellByMapGrid(i, loadToken);
							}else{
								//console.log("grid["+grid.getGridId()+"]加载完成");
								me.cityMapGrids[i].setIsFinished(true);
							}
						}
						// 如果没有获取完成，则继续获取
					} catch (err) {
						// console.log("返回的数据有问题:" + err);
					}
				},
				error : function(xmh, textstatus, e) {
					//alert("出错啦！" + textstatus);
					//停止加载
					me.cityMapGrids[i].setIsLoading(false);
				},
				complete : function() {
					//hideOperTips("loadingDataDiv");
					//判断上一个网格是否加载完，完成才加载下一个网格；这里加入是为了防止（totalPageCnt>=currentPage）条件不满足时还能继续循环
					if(me.cityMapGrids[i].getIsFinished()) {
						i++;
						getGisCellByMapGrid(i, loadToken);
					}
				}
			});
}



/**
 * 处理polygon的点击事件
 * 
 * @param {}
 *            polygon
 */
function handleClick(polygon,cell,beginTime) {
	if(!cell) {
		cell  = gisCellDisplayLib.getComposeMarkerByShape(polygon).getCell();
	}
	showLteCellDetail(cell,beginTime);
}
//
///**
// * 处理鼠标移动事件
// * 
// * @param {}
// *            polygon
// * @param {}
// *            event
// */
//function handleMouseover(polygon, event) {
//	var cmk = polygon._data;
//	if (cmk.getCount() > 1) {
//		// scatterAll(polygon);
//	} else {
//		// showCellTitle(polygon);
//	}
//}
//
//// 显示小区标题
//function showCellTitle(polygon) {
//	// console.log("show cell title");
//}
//
///**
// * 展开重叠的小区
// * 
// * @param {}
// *            polygon
// */
//function scatterAll(polygon) {
//
//	var html = formOverlapHtmlContent(polygon._data);
//	html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
//			+ polygon._data.getCount() + "个）</h4>" + html;
//
//	gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
//
//}
//
/**
 * 把重叠区域的内容形成html
 * 
 * @param {}
 *            composeMark
 */
/*function formOverlapHtmlContent(composeMark) {
	if (!composeMark) {
		return "";
	}
	var cellArray = composeMark.getCellArray();
	var html = "";
	var cell;
	for ( var i = 0; i < cellArray.length; i++) {
		cell = cellArray[i];
		html += "<a onclick='javascript:showCellDetail(\"" + cell.cell
				+ "\")' target='_blank'>"
				+ (cell.chineseName ? cell.chineseName : cell.cell)
				+ "</a><br/><br/>";
	}
	return html;
}*/

//function showCellDetailFromPolygon(polygon) {
//	var label = polygon._data.getCell();
//	// alert("显示"+label+"详情");
//	showCellDetail(label);
//}

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
 * 显示关联的	LTE小区详情
 * @param label
 */
function showLteCellDetail(label,beginTime) {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	// alert("关联的小区为：" + polygon._data.getCell());
	// 看本地缓存有没有
	var cell = null;
	var find = false;
	for ( var i = 0; i < cellDetails.length; i++) {
		cell = cellDetails[i];
		if (cell['cell'] === label&&cell.startTime==beginTime) {
			console.log("从缓存找到详情。");
			find = true;
			break;
		}
	}
	if (find) {
		displayLteCellDetail(cell);
		$(".loading_cover").css("display", "block");
		return;
	}
	$.ajax({
		url : 'queryG4StsQuotaAjaxAction',
		data : {
			'label' : label,
			'beginTime':beginTime
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var c = eval("(" + data + ")");
			if (c) {
				cellDetails.push(c);
				displayLteCellDetail(c);
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
 * 展现LTE小区详情
 * @param lteCell
 */
function displayLteCellDetail(lteCell) {
	var data = eval(lteCell);
	displayLteCellIndex(data.indexInfo);
	displayLteCellThreshold(data.thresholdInfo);
}

/**
 * 展现LTE小区指标
 * @param lteCell
 */
function displayLteCellIndex(lteCellIndexInfo) {
	$("#lteStsCellIndex").empty();
	$(".switch_hidden").trigger("click");
	$("#div_tab ul li")[0].click();
	var st="";
	var a = [
	        [ "cellName", "小区名" ], 
	        [ "startTime", "测量开始时间" ],
			[ "endTime", "测量结束时间" ],
			[ "rrc_ConnEstabSucc", "RRC连接建立成功率" ],
			[ "erab_EstabSucc", "E-RAB建立成功率" ], 
			[ "wireConn", "无线接通率" ],
			[ "erab_Drop_CellLevel", "E-RAB掉线率(小区级)" ],
			[ "rrc_ConnRebuild", "RRC连接重建比率" ], 
			[ "switchSucc", "切换成功率" ],
			[ "wireDrop_CellLevel", "无线掉线率(小区级)" ],
			[ "rrc_ConnEstabSucc_SuccTimes", "RRC连接建立成功次数" ],
			[ "rrc_ConnEstabSucc_ReqTimes", "RRC连接建立请求次数" ],
			[ "erab_EstabSucc_SuccTimes", "E-RAB建立成功次数" ],
			[ "erab_EstabSucc_ReqTimes", "E-RAB建立请求次数" ],
			[ "erab_Drop_ReqTimes_CellLevel", "E-RAB请求次数(小区级)" ],
			[ "erab_Drop_DropTimes_CellLevel", "E-RAB掉线次数(小区级)" ],
			[ "switchSucc_SuccTimes", "切换成功次数" ],
			[ "switchSucc_ReqTimes", "切换请求次数" ],			
			[ "wireConn_ReqTimes", "无线接通请求次数" ],			
			[ "wireConn_SuccTimes", "无线接通成功次数" ],

			[ "wireDrop_DropTimes_CellLevel", "无线掉线次数(小区级)" ],
			[ "wireDrop_ReqTimes_CellLevel", "无线请求次数(小区级)" ],
			[ "emUplinkSerBytes", "空口上行业务字节数" ],
			[ "emDownlinkSerBytes", "空口下行业务字节数" ],
			[ "erab_ConnSuccQCI1", "E-RAB建立成功率(QCI=1)" ],
			[ "erab_ConnSuccQCI2", "E-RAB建立成功率(QCI=2)" ],
			[ "erab_ConnSuccQCI3", "E-RAB建立成功率(QCI=3)" ],
			[ "erab_ConnSuccQCI4", "E-RAB建立成功率(QCI=4)" ],
			[ "erab_ConnSuccQCI5", "E-RAB建立成功率(QCI=5)" ],
			[ "erab_ConnSuccQCI6", "E-RAB建立成功率(QCI=6)" ],
			[ "erab_ConnSuccQCI7", "E-RAB建立成功率(QCI=7)" ],
			[ "erab_ConnSuccQCI8", "E-RAB建立成功率(QCI=8)" ],
			[ "erab_ConnSuccQCI9", "E-RAB建立成功率(QCI=9)" ],
			[ "wireConnQCI1", "无线接通率(QCI=1)" ],
			[ "wireConnQCI2", "无线接通率(QCI=2)" ],
			[ "wireConnQCI3", "无线接通率(QCI=3)" ],
			[ "wireConnQCI4", "无线接通率(QCI=4)" ],
			[ "wireConnQCI5", "无线接通率(QCI=5)" ],
			[ "wireConnQCI6", "无线接通率(QCI=6)" ],
			[ "wireConnQCI7", "无线接通率(QCI=7)" ],
			[ "wireConnQCI8", "无线接通率(QCI=8)" ],
			[ "wireConnQCI9", "无线接通率(QCI=9)" ],
			[ "erab_DropQCI1_CellLevel", "E-RAB掉线率(QCI=1)(小区级)" ],
			[ "erab_DropQCI2_CellLevel", "E-RAB掉线率(QCI=2)(小区级)" ],
			[ "erab_DropQCI3_CellLevel", "E-RAB掉线率(QCI=3)(小区级)" ],
			[ "erab_DropQCI4_CellLevel", "E-RAB掉线率(QCI=4)(小区级)" ],
			[ "erab_DropQCI5_CellLevel", "E-RAB掉线率(QCI=5)(小区级)" ],
			[ "erab_DropQCI6_CellLevel", "E-RAB掉线率(QCI=6)(小区级)" ],
			[ "erab_DropQCI7_CellLevel", "E-RAB掉线率(QCI=7)(小区级)" ],
			[ "erab_DropQCI8_CellLevel", "E-RAB掉线率(QCI=8)(小区级)" ],
			[ "erab_DropQCI9_CellLevel", "E-RAB掉线率(QCI=9)(小区级)" ],
			[ "erab_DropQCI1", "E-RAB掉线率(QCI=1)" ],
			[ "erab_DropQCI2", "E-RAB掉线率(QCI=2)" ],
			[ "erab_DropQCI3", "E-RAB掉线率(QCI=3)" ],
			[ "erab_DropQCI4", "E-RAB掉线率(QCI=4)" ],
			[ "erab_DropQCI5", "E-RAB掉线率(QCI=5)" ],
			[ "erab_DropQCI6", "E-RAB掉线率(QCI=6)" ],
			[ "erab_DropQCI7", "E-RAB掉线率(QCI=7)" ],
			[ "erab_DropQCI8", "E-RAB掉线率(QCI=8)" ],
			[ "erab_DropQCI9", "E-RAB掉线率(QCI=9)" ],
			[ "emUplinkSerBytesQCI1", "空口业务上行字节数(QCI=1)" ],
			[ "emUplinkSerBytesQCI2", "空口业务上行字节数(QCI=2)" ],
			[ "emUplinkSerBytesQCI3", "空口业务上行字节数(QCI=3)" ],
			[ "emUplinkSerBytesQCI4", "空口业务上行字节数(QCI=4)" ],
			[ "emUplinkSerBytesQCI5", "空口业务上行字节数(QCI=5)" ],
			[ "emUplinkSerBytesQCI6", "空口业务上行字节数(QCI=6)" ],
			[ "emUplinkSerBytesQCI7", "空口业务上行字节数(QCI=7)" ],
			[ "emUplinkSerBytesQCI8", "空口业务上行字节数(QCI=8)" ],
			[ "emUplinkSerBytesQCI9", "空口业务上行字节数(QCI=9)" ],
			[ "emDownlinkSerBytesQCI1", "空口业务下行字节数(QCI=1)" ],
			[ "emDownlinkSerBytesQCI2", "空口业务下行字节数(QCI=2)" ],
			[ "emDownlinkSerBytesQCI3", "空口业务下行字节数(QCI=3)" ],
			[ "emDownlinkSerBytesQCI4", "空口业务下行字节数(QCI=4)" ],
			[ "emDownlinkSerBytesQCI5", "空口业务下行字节数(QCI=5)" ],
			[ "emDownlinkSerBytesQCI6", "空口业务下行字节数(QCI=6)" ],
			[ "emDownlinkSerBytesQCI7", "空口业务下行字节数(QCI=7)" ],
			[ "emDownlinkSerBytesQCI8", "空口业务下行字节数(QCI=8)" ],
			[ "emDownlinkSerBytesQCI9", "空口业务下行字节数(QCI=9)" ],
			];

	$.each(a,function(i){
	st+="<tr>";
	st+="<td>"+a[i][1]+"</td>";
	st+="<td>"+getValidValue(lteCellIndexInfo[a[i][0]],'')+"</td>";
	st+="</tr>";
	});
	$("#lteStsCellIndex").append(st);
}
/**
 * 展示LTE小区监控阈值
 * @param lteCell
 */
function displayLteCellThreshold(lteCellThresholdInfo) {
	$("#lteStsCellThreshold").empty();
	var st="";
	var a = [
	        [ "cellName", "小区名" ], 
	        [ "startTime", "测量开始时间" ],
			[ "endTime", "测量结束时间" ],
			
			[ "erab_DropRate_Flag", "E-RAB掉线率" ],
			[ "erab_EstabSuccRate_Flag", "E-RAB建立成功率" ], 
			[ "rrc_ConnEstabSuccRate_Flag", "RRC连接建立成功率" ],
			[ "zeroFlow_ZeroTeltra_Flag", "零流量零话务" ],
			[ "wireConnRate_Flag", "无线接通率" ], 
			[ "cellBar_rrc_Conn_FailTimes_Flag", "CellBar_RRC连接失败次数" ],
			[ "cellBar_DropTimes_Flag", "CellBar_掉线次数" ],
			[ "rrc_ConnFailTimes_Flag", "RRC连接失败次数" ],
			[ "dropTimes_Flag", "掉线次数" ],
			[ "connFailTimes_Flag", "接通失败次数" ],
			[ "switchSuccRate_Flag", "切换成功率" ],
			[ "switchFailTimes_Flag", "切换失败次数" ],
			[ "wireDropRate_Flag", "无线掉线率" ],
			];

	$.each(a,function(i){
		var value = getValidValue(lteCellThresholdInfo[a[i][0]],"");
		var tmp = "<font>"+value+"</font>";
		if (typeof (value) == 'boolean') {
			if (value) {
				tmp = "<font color='red'>异常</font>";
			} else {
				tmp = "<font>正常</font>";
			}
		}
		st+="<tr>";
		st+="<td>"+a[i][1]+"</td>";
		st+="<td>"+tmp+"</td>";
		st+="</tr>";
		});
	$("#lteStsCellThreshold").append(st);
}


/***************** 地图小区加载改造 start ********************/
function handleMoveendAndZoomend(e, lastOperTime) {
	var winMinLng = map.getBounds().getSouthWest().lng;
	var winMinLat = map.getBounds().getSouthWest().lat;
	var winMaxLng = map.getBounds().getNorthEast().lng;
	var winMaxLat = map.getBounds().getNorthEast().lat;
	console.log(winMinLng+","+winMinLat+";"+winMaxLng+","+winMaxLng);
	//设置当前屏幕经纬度范围
	mapGridLib.setWinLngLatRange(winMinLng,winMinLat,winMaxLng,winMaxLat);
	//每一次移动，缩放结束的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
	/*var gsmtype = $("input[name='freqType']:checked").val();
	var params = {
		'freqType' : gsmtype
	};*/
	//mapGridLib.loadLteCell(lastOperTime, currentloadtoken, minResponseInterval);
	//getGisCell(currentloadtoken);
	loadLteCellAboutPm(lastOperTime, currentloadtoken, minResponseInterval);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
/***************** 地图小区加载改造 end ********************/
/**
 * 显示小区
 */
function showLteCell(data) {
	//console.log("in GisCellDisplayLib.prototype.showGisCell");
	if (!data) {
		//console.log("数据不存在");
		return;
	}
	//console.log("开始装载参数");
	try {
		var composeMarkers = gisCellDisplayLib.composeMarkers;
		var allPolygons = gisCellDisplayLib.allPolygons;
		var visiblePolygons = gisCellDisplayLib.visiblePolygons;
		var cellToPolygon = gisCellDisplayLib.cellToPolygon;
		//var map = gisCellDisplayLib.map;
		var multiColor = gisCellDisplayLib.multiColor;
		//var singleColor = gisCellDisplayLib.singleColor;

		//var start = composeMarkers.length;// 新获取的小区对象在数组中的起始位置
		var cmk;
		var j = 0;
		var polygon;
		
		for ( var i = 0; i < data.length; i++) {
			var gisCell = data[i];
			//--2014-9-22 pjm add 去重复--//
			if(gisCellDisplayLib.existCell(gisCell['cell'])){
				continue;
			}
			//--2014-5-30 gmh add-- exclude cells without lng,lat//
			if(!gisCell || gisCell['allLngLats']==''){
				continue;
			}
			if(gisCell['thFlag']){
					//console.log("增加问题小区");
					//problemCell[gisCell.cell]=gisCell;
				var pFlag = true;
				var pCell = [gisCell.cell,gisCell];
				for(var k=0;k<problemCell.length;k++){
					if(problemCell[k][0]==pCell[0]){
						pFlag = false;
					}
				}
				if(pFlag){
					problemCell.push(pCell);
					$("#loadingProbCellTip").html("已有问题小区:<font size=\"3\" color=\"red\">"+problemCell.length+"</font>");
				}				
			}
			var cmklen = composeMarkers.length;
			for (j = 0; j < cmklen; j++) {
				cmk = composeMarkers[j];
				if (cmk.similiar(gisCell, gisCellDisplayLib.DiffAzimuth, gisCellDisplayLib.DiffDistance)) {
					//console.log("加小区");
					cmk.addCell(gisCell);
					//console.log("加小区成功");
					//cellToPolygon[cmk.getCell()] = allPolygons[j];
					cellToPolygon[gisCell.cell]=allPolygons[j];// 小区到polygon
					if (cmk.getCount() > 1) {
						// 重新渲染
						polygon = allPolygons[j];
						// console.log("旧的polygon" + polygon);
						if (polygon) {				
							if(cmk._thFlag){
								//polygon = enlargePolygon(polygon,2.5);
								polygon.setFillColor(errOverFillColor);
								polygon.setFillOpacity(errOverFillOpacity);
							}else{
							polygon.setFillColor(multiColor);
							}
						}
					}
					//console.log("相似小区="+gisCell['chineseName']);
					break;
				}
			}
			//console.log("j=" + j);
			if (j >= cmklen) {
				// console.log("准备添加进单独的marker");
				var onecmk = new ComposeMarker(gisCell);
				if (onecmk && onecmk.getCount()>0) {
					composeMarkers.push(onecmk);// 不与任何点重复，加入
					polygon = createPolygonFromComposeMark(onecmk);
					if(onecmk._thFlag){
						//polygon = enlargePolygon(polygon,2.5);
						polygon.setFillColor(errFillColor);
						polygon.setFillOpacity(errFillOpacity);
					}
					polygon = gisCellDisplayLib.polygonToSector(polygon,40);
					allPolygons.push(polygon);
					polygon._isShow = false;
					cellToPolygon[onecmk.getCell()] = polygon;
					var visib = gisCellDisplayLib.shouldDisplay(polygon);
					if (visib === true) {
						gisCellDisplayLib.showOnePolygon(polygon);
						visiblePolygons.push(polygon);
					}
				} else {
					onecmk=null;
				}
			}
		}
		/*for ( var index = start; index < composeMarkers.length; index++) {
			cmk = composeMarkers[index];
			polygon = createPolygonFromComposeMark(cmk);
			if(cmk._thFlag){
				polygon = enlargePolygon(polygon,2.5);
				polygon.setFillColor(errFillColor);
			}
			polygon = gisCellDisplayLib.polygonToSector(polygon);
			//this.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);//polygon对象创建右键菜单
			// console.log("create polygon = "+polygon);
			allPolygons.push(polygon);
			//console.log("allPolygons的长度为："+allPolygons.length);
			polygon._isShow = false;
			//console.log("index=="+index);
			//---2014-11-20 pjm 加入 
			//cmk对象包含的所有cell都映射到这个polygon中
			cellToPolygon[cmk.getCell()] = polygon;
			// 是否要显示？
			//console.log("是否要显示？");
			var visib = gisCellDisplayLib.shouldDisplay(polygon);
			if (visib === true) {
				// console.log("可见，将在地图显示");
				// polygon._isShow = true;
				// map.addOverlay(polygon);
				gisCellDisplayLib.showOnePolygon(polygon);
				visiblePolygons.push(polygon);
			}
		}*/
/*		console.log("改变颜色");
		var option={'fillColor':errFillColor,'fillOpacity':1};
		for ( var k = 0; k < data.length; k++) {
			var gisCell = data[k];
			//--2014-9-22 pjm add 去重复--//
			if(gisCellDisplayLib.existCell(gisCell['cell'])){
				continue;
			}
			//--2014-5-30 gmh add-- exclude cells without lng,lat//
			if(!gisCell || gisCell['allLngLats']==''){
				continue;
			}*/
			//console.log("改变颜色");
			//console.log(probC.length);
		
/*			for(var pc=0;pc<probC.length;pc++){
				polygon=cellToPolygon[probC[pc]];
				console.log("多边形"+polygon);
				if (polygon) {
					var visib = gisCellDisplayLib.shouldDisplay(polygon);
					if (visib === true) {
						console.log(errFillColor);
						polygon.setFillColor(errFillColor);
						// console.log("可见，将在地图显示");
						// polygon._isShow = true;
						// map.addOverlay(polygon);
						gisCellDisplayLib.showOnePolygon(polygon);
						visiblePolygons.push(polygon);
					}
				}
			}
			probCell=probCell.concat(probC);*/
		//gisCellDisplayLib.changeCellPolygonOptions(gisCell,option,false);
		//}
	} catch (err) {
		console.error(err);
	}
	}

function showProblemCellList(){
	$("#probCellList").empty();
	//console.log("问题小区列表长度="+problemCell.length);
	var html="";
	if(problemCell.length>0){
		$.each(problemCell,function(i){
			//console.log(problemCell[i][0]+","+problemCell[i][1].chineseName);
			html+="<tr>";
			//html+="<td id='"+problemCell[i][0]+"'><a href=\"javascript:void(0)\" onclick=\"javascript:gisCellDisplayLib.panTo(problemCell["+i+"][1].lng, problemCell["+i+"][1].lat)\" >"+problemCell[i][1].chineseName+"</td>";
			html+="<td id='"+problemCell[i][0]+"'><a href=\"javascript:void(0)\" onclick=\"javascript:goCellPoint(problemCell["+i+"][0])\" >"+problemCell[i][1].chineseName+"</td>";
			//html+="<td id='"+problemCell[i][0]+"'><a href=\"javascript:void(0)\" onclick=\"javascript:gisCellDisplayLib.centerAndZoom(problemCell["+i+"][1].lng,problemCell["+i+"][1].lat,"+minZoom+")\" >"+problemCell[i][1].chineseName+"</td>";
			html+="</tr>";
		});
	}else{
		html="未找到问题小区";
	}
	$("#probCellList").append(html);
}
function goCellPoint(cell){
	//gisCellDisplayLib.centerAndZoom(cell.lng,cell.lat,minZoom);
	gisCellDisplayLib.panToCell(cell,minZoom);
}
/**
 * 放大polygon到size倍
 * @param polygon
 * @param size
 */
function enlargePolygon(polygon,size){
	var pa=polygon.getPath();
	var t=Math.sqrt(size);
	if(pa.length==3&&pa[0]!=pa[pa.length-1]){
	var newP1_lng=(pa[1].lng-pa[0].lng)*t+pa[0].lng;
	var newP1_lat=(pa[1].lat-pa[0].lat)*t+pa[0].lat;
	var newP2_lng=(pa[pa.length-1].lng-pa[0].lng)*t+pa[0].lng;
	var newP2_lat=(pa[pa.length-1].lat-pa[0].lat)*t+pa[0].lat;
	var newP1=new BMap.Point(newP1_lng,newP1_lat);
	var newP2=new BMap.Point(newP2_lng,newP2_lat);
	pa.splice(1,1,newP1);
	pa.splice(pa.length-1,1,newP2);
	polygon.setPath(pa);
	}

	return polygon;
}
/*function polygonToSector(polygon){
		var pa=polygon.getPath();
		//console.log("Math.atan2(pa[1].lng-pa[0].lng, pa[1].lat-pa[0].lat)="+Math.atan2(pa[1].lng-pa[0].lng, pa[1].lat-pa[0].lat)/Math.PI)
		var sDegree=Math.atan2(pa[1].lng-pa[0].lng, pa[1].lat-pa[0].lat)/Math.PI*180;
		var eDegree=Math.atan2(pa[2].lng-pa[0].lng, pa[2].lat-pa[0].lat)/Math.PI*180;
		//var radius=(map.getDistance(pa[0],pa[1])+map.getDistance(pa[0],pa[2]))/2;
		var radius=(Math.sqrt((pa[0].lng-pa[1].lng)*(pa[0].lng-pa[1].lng)+(pa[0].lat-pa[1].lat)*(pa[0].lat-pa[1].lat))+Math.sqrt((pa[0].lng-pa[2].lng)*(pa[0].lng-pa[2].lng)+(pa[0].lat-pa[2].lat)*(pa[0].lat-pa[2].lat)))/2;
		var opts={};
		//console.log("参数为sDegree="+sDegree+"--eDegree="+eDegree+"--radius="+radius);
		var newPolygon = Sector(pa[0], radius, sDegree, eDegree);
		polygon.setPath(newPolygon.getPath());
		//console.log("成功组装扇形");
		return polygon;
}
function Sector(point, radius, sDegree, eDegree, strokeColour, strokeWeight, Strokepacity, fillColour, fillOpacity, opts) {
	//console.log("进入扇形方法");
	    var points = [];
	    var step = ((eDegree - sDegree) / 20) || 1;
	    points.push(point);
	    for ( var i = sDegree; i <=eDegree - 0.0000001; i += step) {
	        points.push(EOffsetBearing(point, radius, i));
	    }
	    points.push(point);
	    //console.log("points.length="+points.length);
	    var polygon = new BMap.Polygon(points, {strokeColor:strokeColour, strokeWeight:strokeWeight, strokeOpacity:Strokepacity, fillColor: fillColour, fillOpacity:fillOpacity});
	    
	    return polygon;
}

function EOffsetBearing(point, dist, bearing) {
	    //var latConv = map.getDistance(point, new BMap.Point(point.lng + 0.5, point.lat)) * 2;
	    //var lngConv = map.getDistance(point, new BMap.Point(point.lng, point.lat + 0.5)) * 2;
	    //var avgConv=Math.sqrt((latConv*latConv+lngConv*lngConv)/2);
	var avgConv=1;
	   // console.log("latConv="+latConv+"--lngConv="+lngConv+"---avgConv="+avgConv+"avgold="+(latConv+lngConv)/2);
	    var lat = dist * Math.cos(bearing * Math.PI / 180) / avgConv;
	    var lng = dist * Math.sin(bearing * Math.PI / 180) / avgConv;
	    return new BMap.Point(point.lng + lng, point.lat + lat);
}*/


function createPolygonFromComposeMark  (cmk) {
	try {
		if (!cmk) {
			//console.log("空的参数");
			return null;
		}
		var me = gisCellDisplayLib;
		var pa = cmk.getPointArray();
		var polygon = new BMap.Polygon(pa, cmk.getPolygonOptions(
				me.singleColor, me.multiColor));
		
//		var p4=new BMap.Point((pa[1].lng+pa[2].lng)/2,(pa[1].lat+pa[2].lat)/2);
//		polygon=new BMap.Polyline([pa[0],p4],{strokeWeight:1});
		
		
		polygon._data = cmk;// 相互引用
		cmk.setPolygon(polygon);

		// 2013-12-13 gmh add label
		// 同一个起点的众多扇形，只显示其中的一个的名称，其他的不显示，免得太拥挤
		var key = cmk.getLng() + "_" + cmk.getLat();
		var plys = me.sameLnglatPolyArray[key];
		if (!plys) {
			plys = new Array();
			me.sameLnglatPolyArray[key]=plys;
		}
		plys.push(polygon);
		if (plys.length == 1) {
			//只有第一个需要配label
			var angle = cmk.getAzimuth();
			var labelPosition = null;
			var edgePosition = null;
			var startPosition = null;
			var cellname = cmk.getFirstCellNameChineseFirst();

			if (pa && pa.length > 2) {
				var p1 = pa[1];
				var p2 = pa[2];
				edgePosition = new BMap.Point((p1.lng + p2.lng) / 2,
						(p1.lat + p2.lat) / 2);
				startPosition = pa[0];

			} else {
				if (pa) {
					edgePosition = pa;
					startPosition = pa;
				} else {
					edgePosition = null;
					startPosition = null;
				}
			}
			if (edgePosition != null) {

				if (angle > 180) {
					angle = angle - 180;
					labelPosition = edgePosition;
				} else {
					labelPosition = startPosition;
				}
				angle = angle - 90;
				var label = new BMap.Label(cellname, {
					'position' : labelPosition,
					'offset' : {
						width : 0,
						height : 0
					}
				});

				label.setStyle({
					'border' : 'none',
					'backgroundColor' : 'transparent',
					'color' : '#2E2EFE',
					'transform' : 'rotate(' + (angle) + 'deg)'
				});

				polygon._label = label;
			}
		}
		// /

		polygon.addEventListener('click', function(event) {
			//加入判断是否是重叠小区
			var cmk = this._data;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					html+="<div id=\"celltimelist\" >";
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];						
						cellId=cell.lcid?cell.lcid:cell.cell;
						if(cell.beginTimes.length<=1){
						html += "<a onclick='("+me.clickFunction+")(this,\""+cellId+"\")' " +
						//html += "<a onclick=\"javascript:gisCellDisplayLib.clickFunction(gisCellDisplayLib.cellToPolygon[cellId],"+cellId+")\""+
						" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)	+ "</a><br/><br/>";
						}else{
							html += "<a onclick='("+toggleCellBegtimeList+")()' style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
							//html += "<a onclick=\""+$(this).find("ul").toggle()+"\" style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
							//html += "<a onclick=\"javascript:toggleCellBegtimeList()\" style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
							for(var bt=0;bt<cell.beginTimes.length;bt++){
							html+="<li><a onclick='("+me.clickFunction+")(this,\""+cellId+"\",\""+cell.beginTimes[bt]+"\")' style=\"color:"+(cell.flags[bt]?"red":"")+";\"" +
							" href=\"javascript:void(0)\">"+ (cell.beginTimes[bt])+ "</a></li>";
							}
							html+="</ul><br/><br/>";
						}
					}
					html+="</div>";
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					me.showInfoWindow(html, me.getOriginPointByShape(this));
			} else {
				var html = "";
				if (!cmk) {
					return "";
				}
				var cell=cmk.getCellArray()[0];
				if(!cell) {
					return "";
				}
				if(cell.beginTimes.length>1){
					for(var bt=0;bt<cell.beginTimes.length;bt++){
						html+="<a onclick='("+me.clickFunction+")(this,\""+cell.cell+"\",\""+cell.beginTimes[bt]+"\")' style=\"color:"+(cell.flags[bt]?"red":"")+";\"" +
						" href=\"javascript:void(0)\">"+ (cell.beginTimes[bt])+ "</a><br/><br/>";
						}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+(cell.chineseName ? cell.chineseName : cellId)+"<br/>任务次数（"
						+ cell.beginTimes.length + "次）</h4>" + html;
				me.showInfoWindow(html, me.getOriginPointByShape(this));
				}else{
				me.clickFunction(this,cell.cell,cell.beginTimes[0]);
				};
			}
			
			//me.clickFunction(this, event);
		});
		polygon.addEventListener('mouseover', function(event) {
			me.mouseoverFunction(this, event);
		});
		polygon.addEventListener('mouseout', function(event) {
			me.mouseoutFunction(this, event);
		});
//		console.log("contextMenu?contextMenu:defaultcontextMenu:"+me.contextMenu);
		me.rightClickMenuItemForPolygon(polygon,typeof me.contextMenu=="undefined"?null:me.contextMenu);
//		me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);
		
		polygon.addEventListener('rightclick', function(event) {
			//me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);//polygon对象创建右键菜单
//			console.log("me.contextMenu:"+me.contextMenu);
			//me.clearOnlyExtraOverlay();//清除额外覆盖物
			//me.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
			var aa=typeof me.contextMenu=="undefined"?null:me.contextMenu;
			//typeof me.contextMenu!='undefined' && me.contextMenu!=null
//			console.log("aa:"+aa.length);
			if (aa.length!=0) {
				//var obj=eval("("+txtMenuItem+")");
				var bb={'polygon':polygon};
				aa.push(bb);
				me.contextMenu=aa;
				//console.log("txtMenuItem="+txtMenuItem[txtMenuItem.length-1].polygon);
			}
//		  console.log("me.contextMenu:"+me.contextMenu.length);
		  me.rightclickFunction(polygon,event);
		//me.rightClickMenuItemForPolygon(polygon,aa);//polygon对象创建右键菜单
		//me.rightclickFunction(polygon,aa);
});
		return polygon;
	} catch (err) {
		console.error(err);
		return null;
	}
}
/**
 * 切换小区测量开始时间列表
 */
function toggleCellBegtimeList(){
 $("#celltimelist a ul").show();
 $("#celltimelist a").live('click',function(){
  $(this).find("ul")[0].toggle();
 });
 $("#celltimelist a ul").hide();
}
/**
 * 鼠标移入事件
 * @param polygon
 * @param event
 */
function handleMouseOver(polygon, event) {
/*//	var point = new BMap.Point(polygon._data.getLng(), polygon._data.getLat());
//	var offset = typeof gisCellDisplayLib!="undefined"?gisCellDisplayLib.getOffsetSize(3,3):new BMap.Size(3, 3);
//	var label = new BMap.Label(gisCellDisplayLib.getTitleContent(polygon), {
//		'position' : point,
//		'offset' : offsetSize
//	});
	
	var label=new IsTextLabel(polygon._data.getLng(), polygon._data.getLat(),gisCellDisplayLib.getTitleContent(polygon));
	label.setStyle({
		'backgroundColor' : '#A9F5D0',
		'border':'1px solid'
	});
	titleLabels.push(label);
	gisCellDisplayLib.addOverlay(label);*/
}
/**
 * 鼠标移出事件
 * @param polygon
 * @param event
 */
function handleMouseOut(polygon, event) {
/*	for ( var i = 0; i < titleLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(titleLabels[i]);
	}
	titleLabels.splice(0, titleLabels.length);*/
}
/**
 * 右击事件
 * @param polygon
 * @param event
 */
function handleRrigthClick(polygon,event){	
}
/**
 * 右键菜单
 */
function handleContextMenu() {	
}